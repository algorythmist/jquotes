package com.tecacet.jquotes;

import com.tecacet.jquotes.yahoo.YahooFinanceClient;
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
            var quotes = yahooFinanceClient.getHistoricalQuotes(symbol, request.getFromDate(), request.getToDate(),
                    request.getPeriodType());
            //TODO: handle missing symbols
            //TODO: get dividends and splits
            map.put(symbol, QuoteUtils.toSortedMap(quotes));
        }
        return QuoteResponse.builder()
                .quotes(map)
                .adjusted(true)
                .includeDividends(request.isIncludeDividends())
                .includeSplits(request.isIncludeSplits())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .periodType(request.getPeriodType())
                .symbols(request.getSymbols())
                .build();
    }

}
