package com.tecacet.jquotes.iex;

import com.tecacet.jquotes.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
                    .build();
        }
        var map = new HashMap<String, SortedMap<LocalDate, Quote>>();
        for (String symbol : request.getSymbols()) {
            var range =  getRange(request.getFromDate());
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
                .adjusted(false)
                .includeDividends(request.isIncludeDividends())
                .includeSplits(request.isIncludeSplits())
                .periodType(request.getPeriodType())
                .build();
    }

    private Range getRange(LocalDate startDate) {
        long c = ChronoUnit.DAYS.between(startDate, LocalDate.now());
        if (c <= 28) {
            return Range.ONE_MONTH;
        }
        if (c <= 90) {
            return Range.THREE_MONTHS;
        }
        if (c <= 180) {
            return Range.SIX_MONTHS;
        }
        if (c <= 360) {
            return Range.ONE_YEAR;
        }
        if (c <= 720) {
            return Range.TWO_YEARS;
        }
        return Range.FIVE_YEARS;
    }

    private void addDividends(String symbol, Range range,
                              QuoteRequest request,
                              SortedMap<LocalDate, Quote> quotes) {
        if (PeriodType.DAY == request.getPeriodType() && request.isIncludeDividends()) {
            var dividends = iexClient.getDividends(symbol, range);
            for (Dividend dividend : dividends) {
                var quote = quotes.get(dividend.getExDate());
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
