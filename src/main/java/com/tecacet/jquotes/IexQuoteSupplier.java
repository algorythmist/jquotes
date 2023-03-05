package com.tecacet.jquotes;

import com.tecacet.jquotes.iex.IexClient;
import com.tecacet.jquotes.iex.Range;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashMap;
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
            //TODO: return
        }
        var map = new HashMap<String, SortedMap<LocalDate, Quote>>();
        for (String symbol : request.getSymbols()) {
            //TODO: set range according to query
            var quotes = iexClient.getDailyQuotes(symbol, Range.FIVE_YEARS);
            //TODO: handle missing symbols
            //TODO: get dividends and splits
            //TODO: handle adjusted
            //TODO: truncate range
            map.put(symbol, QuoteUtils.toSortedMap(quotes));
        }
        return QuoteResponse.builder()
                .quotes(map)
                .adjusted(request.isAdjusted())
                .includeDividends(request.isIncludeDividends())
                .includeSplits(request.isIncludeSplits())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .periodType(request.getPeriodType())
                .symbols(request.getSymbols())
                .build();
    }
    
}
