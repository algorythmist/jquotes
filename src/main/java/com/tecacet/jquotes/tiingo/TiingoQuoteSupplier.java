package com.tecacet.jquotes.tiingo;

import com.tecacet.jquotes.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
        var map = new TreeMap<String, SortedMap<LocalDate, Quote>>();
        for (String symbol : request.getSymbols()) {
            try {
                var quotes = tiingoClient.getQuoteHistory(symbol, request.getFromDate(), request.getToDate());
                NavigableMap<LocalDate, Quote> timeSeries = toSortedMap(quotes, request.isAdjusted());
                if (request.getPeriodType() != PeriodType.DAY) {
                    timeSeries = QuoteUtils.resample(timeSeries, request.getPeriodType());
                }
                map.put(symbol, timeSeries);
            } catch (IOException ioe) {
                log.warn("Could not retrieve data for {}", symbol);
            }

        }
        return QuoteResponse.builder()
                .quotes(map)
                .adjusted(request.isAdjusted())
                .includeDividends(true)
                .includeSplits(true)
                .periodType(request.getPeriodType())
                .build();
    }

    @Override
    @SneakyThrows
    public Map<String, IntradayQuote> getIntradayQuotes(String... symbols) {
        return tiingoClient.getCurrentQuotes(symbols).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> toIntradayQuote(e.getKey(), e.getValue())));
    }

    private IntradayQuote toIntradayQuote(String symbol, TiingoIexQuote quote) {
        return IntradayQuote.builder()
                .symbol(symbol)
                .ask(quote.getAskPrice())
                .bid(quote.getBidPrice())
                .last(quote.getLast())
                .volume(Long.valueOf(quote.getVolume()))
                .open(quote.getOpen())
                .previousClose(quote.getPreviousClose())
                .timestamp(quote.getTimestamp())
                .build();
    }

    private NavigableMap<LocalDate, Quote> toSortedMap(List<TiingoQuote> quotes, boolean adjusted) {
        return quotes.stream()
                .map(adjusted ? this::getAdjustedQuote : Function.identity())
                .collect(Collectors.toMap(Quote::getDate, Function.identity(),
                        (a, b) -> a,
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
