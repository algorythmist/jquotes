package com.tecacet.jquotes;

import com.tecacet.jquotes.yahoo.Split;
import com.tecacet.jquotes.yahoo.YahooFinanceClient;
import com.tecacet.jquotes.yahoo.YahooQuote;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.SortedMap;

@RequiredArgsConstructor
public class YahooQuoteSupplier implements QuoteSupplier {

    private final YahooFinanceClient yahooFinanceClient;

    @Override
    public QuoteResponse getHistoricalQuotes(QuoteRequest request) {
        var map = new HashMap<String, SortedMap<LocalDate, Quote>>();

        for (String symbol : request.getSymbols()) {

            var quoteList = yahooFinanceClient.getHistoricalQuotes(symbol, request.getFromDate(), request.getToDate(),
                    request.getPeriodType());
            var quotes = QuoteUtils.toSortedMap(quoteList);
            addDividends(request, symbol, quotes);
            addSplits(request, symbol, quotes);
            //TODO: handle missing symbols
            map.put(symbol, quotes);
        }
        return QuoteResponse.builder()
                .quotes(map)
                .adjusted(true)
                .includeDividends(request.isIncludeDividends() && PeriodType.DAY == request.getPeriodType())
                .includeSplits(request.isIncludeSplits() && PeriodType.DAY == request.getPeriodType())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .periodType(request.getPeriodType())
                .symbols(request.getSymbols())
                .build();
    }

    private void addSplits(QuoteRequest request, String symbol, SortedMap<LocalDate, Quote> quotes) {
        if (PeriodType.DAY == request.getPeriodType() && request.isIncludeSplits()) {
            var splits =
                    yahooFinanceClient.getSplitHistory(symbol, request.getFromDate(), request.getToDate());
            for (Split split : splits) {
                var quote = quotes.get(split.getDate());
                if (quote != null) {
                    ((YahooQuote) quote).setSplitRatio(split.getSplitRatio());
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
                    ((YahooQuote) quote).setDividend(dividends.get(date));
                }
            }
        }
    }

}
