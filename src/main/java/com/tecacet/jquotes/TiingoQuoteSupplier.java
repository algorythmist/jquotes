package com.tecacet.jquotes;

import com.tecacet.jquotes.tiingo.TiingoClient;
import com.tecacet.jquotes.tiingo.TiingoQuote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class TiingoQuoteSupplier implements QuoteSupplier {

    private final TiingoClient tiingoClient;

    @Override
    public QuoteResponse getHistoricalQuotes(QuoteRequest request) {
        var map = new HashMap<String, SortedMap<LocalDate, Quote>>();
        for (String symbol : request.getSymbols()) {
            try {
                var quotes = tiingoClient.getQuoteHistory(symbol, request.getFromDate(), request.getToDate());
                map.put(symbol, toSortedMap(quotes, request.isAdjusted()));
            } catch (IOException ioe) {
                log.warn("Could not retrieve data for {}", symbol);
            }
            //TODO: resample

        }
        return QuoteResponse.builder()
                .quotes(map)
                .symbols(new ArrayList<>(map.keySet()))
                .adjusted(request.isAdjusted())
                .includeDividends(true)
                .includeSplits(true)
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .periodType(request.getPeriodType())
                .build();
    }

    private SortedMap<LocalDate, Quote> toSortedMap(List<TiingoQuote> quotes, boolean adjusted) {
        return quotes.stream()
                .map(adjusted? this::getAdjustedQuote : Function.identity())
                .collect(Collectors.toMap(Quote::getDate, Function.identity(),
                (a , b) -> a,
                TreeMap::new));
    }

    private BaseQuote getAdjustedQuote(TiingoQuote tiingoQuote) {
        var quote = new BaseQuote();
        quote.setDate(tiingoQuote.getDate());
        quote.setOpen(tiingoQuote.getAdjOpen());
        quote.setClose(tiingoQuote.getAdjClose());
        quote.setLow(tiingoQuote.getAdjLow());
        quote.setHigh(tiingoQuote.getAdjHigh());
        quote.setVolume(tiingoQuote.getAdjVolume());
        quote.setSplitRatio(tiingoQuote.getSplitFactor());
        quote.setDividend(tiingoQuote.getDivCash());
        return quote;
    }
}
