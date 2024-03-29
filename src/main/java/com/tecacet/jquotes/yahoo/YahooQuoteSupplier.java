package com.tecacet.jquotes.yahoo;

import com.tecacet.jquotes.*;
import com.tecacet.jquotes.yahoo.model.Split;
import com.tecacet.jquotes.yahoo.model.YahooHistoricalQuote;
import com.tecacet.jquotes.yahoo.model.YahooQuote;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class YahooQuoteSupplier implements QuoteSupplier {

    private final YahooFinanceClient yahooFinanceClient;

    @Override
    public QuoteResponse getHistoricalQuotes(QuoteRequest request) {
        var map = new HashMap<String, SortedMap<LocalDate, Quote>>();

        for (String symbol : request.getSymbols()) {

            try {
                var quoteList = yahooFinanceClient.getHistoricalQuotes(symbol, request.getFromDate(), request.getToDate(),
                        request.getPeriodType());
                var quotes = QuoteUtils.toSortedMap(quoteList);
                addDividends(request, symbol, quotes);
                addSplits(request, symbol, quotes);
                map.put(symbol, quotes);
            } catch (IOException ioe) {
                log.warn("Could not retrieve data for {}", symbol);
            }
        }
        return QuoteResponse.builder()
                .quotes(map)
                .adjusted(true)
                .includeDividends(request.isIncludeDividends() && PeriodType.DAY == request.getPeriodType())
                .includeSplits(request.isIncludeSplits() && PeriodType.DAY == request.getPeriodType())
                .periodType(request.getPeriodType())
                .build();
    }

    @Override
    @SneakyThrows
    public Map<String, IntradayQuote> getIntradayQuotes(String... symbols) {
        return yahooFinanceClient.getLatestQuotes(symbols).stream()
                .collect(Collectors.toMap(YahooQuote::getSymbol,
                        this::toIntradayQuote));
    }

    private IntradayQuote toIntradayQuote(YahooQuote quote) {
        return IntradayQuote.builder()
                .symbol(quote.getSymbol())
                .ask(quote.getAsk())
                .bid(quote.getBid())
                .last(quote.getRegularMarketPrice())
                .volume(quote.getRegularMarketVolume())
                .open(quote.getRegularMarketOpen())
                .previousClose(quote.getRegularMarketPreviousClose())
                .build();
    }

    private void addSplits(QuoteRequest request, String symbol, SortedMap<LocalDate, Quote> quotes) {
        if (PeriodType.DAY == request.getPeriodType() && request.isIncludeSplits()) {
            var splits =
                    yahooFinanceClient.getSplitHistory(symbol, request.getFromDate(), request.getToDate());
            for (Split split : splits) {
                var quote = quotes.get(split.getDate());
                if (quote != null) {
                    ((YahooHistoricalQuote) quote).setSplitRatio(split.getSplitRatio());
                }
            }
        }
    }

    private void addDividends(QuoteRequest request, String symbol, SortedMap<LocalDate, Quote> quotes) {
        if (PeriodType.DAY == request.getPeriodType() && request.isIncludeDividends()) {
            var dividends =
                    yahooFinanceClient.getHistoricalDividends(symbol, request.getFromDate(), request.getToDate());
            for (LocalDate date : dividends.keySet()) {
                var quote = quotes.get(date);
                if (quote != null) {
                    ((YahooHistoricalQuote) quote).setDividend(dividends.get(date));
                }
            }
        }
    }


}
