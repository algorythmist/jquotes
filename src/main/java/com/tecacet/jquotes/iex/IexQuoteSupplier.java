package com.tecacet.jquotes.iex;

import com.tecacet.jquotes.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.SortedMap;

@RequiredArgsConstructor
@Slf4j
public class IexQuoteSupplier implements QuoteSupplier {

    private final IexClient iexClient;
    @Override
    public QuoteResponse getHistoricalQuotes(QuoteRequest request) {
        var firstDate = LocalDate.now().minusYears(5);
        if (firstDate.isAfter(request.getFromDate())) {
            log.warn("The last date for which data is available is {}. Data will be truncated",
                    firstDate);
        }
        if (firstDate.isAfter(request.getToDate())) {
            log.warn("The last date for which data is available is {}. No data can be obtained",
                    firstDate);
            return QuoteResponse.builder()
                    .symbols(Collections.emptyList())
                    .build();
        }
        var map = new HashMap<String, SortedMap<LocalDate, Quote>>();
        for (String symbol : request.getSymbols()) {
            //TODO: set range according to query
            var range =  Range.FIVE_YEARS;
            try {
                var quotes = iexClient.getDailyQuotes(symbol,range);
                NavigableMap<LocalDate, Quote> timeSeries = QuoteUtils.toSortedMap(quotes);
                addDividends(symbol,range, request, timeSeries);
                addSplits(symbol,range, request, timeSeries);
                //TODO: handle adjusted
                if (request.getPeriodType() != PeriodType.DAY) {
                    timeSeries = QuoteUtils.resample(timeSeries, request.getPeriodType());
                }
                map.put(symbol, QuoteUtils.truncate(timeSeries, request.getFromDate(), request.getToDate()));
            } catch (IOException ioe) {
                log.warn("Could not retrieve data for {}", symbol);
            }
        }
        return QuoteResponse.builder()
                .quotes(map)
                .symbols(map.keySet())
                .adjusted(false)
                .includeDividends(request.isIncludeDividends())
                .includeSplits(request.isIncludeSplits())
                .periodType(request.getPeriodType())
                .build();
    }

    private void addDividends(String symbol, Range range,
                              QuoteRequest request,
                              SortedMap<LocalDate, Quote> quotes) {
        if (PeriodType.DAY == request.getPeriodType() && request.isIncludeDividends()) {
            var dividends = iexClient.getDividends(symbol, range);
            for (Dividend dividend : dividends) {
                var quote = quotes.get(dividend.getRecordDate());
                if (quote != null) {
                    ((IexHistoricalQuote) quote).setDividend(dividend.getAmount());
                }
            }
        }
    }

    private void addSplits(String symbol, Range range,
                           QuoteRequest request,
                           SortedMap<LocalDate, Quote> quotes) {
        if (PeriodType.DAY == request.getPeriodType() && request.isIncludeSplits()) {
            var splits = iexClient.getSplits(symbol, range);
            for (Split split : splits) {
                var quote = quotes.get(split.getExDate());
                if (quote != null) {
                    ((IexHistoricalQuote) quote).setSplitRatio(split.getSplitRatio());
                }
            }
        }
    }
    
}
