package com.tecacet.jquotes;

import java.time.LocalDate;
import java.util.List;

public interface QuoteSupplier {

    List<Quote> getHistoricalQuotes(String ticker, LocalDate fromDate, LocalDate toDate, final PeriodType periodType);

    default List<Quote> getDailyQuotes(String ticker, LocalDate fromDate, LocalDate toDate) {
        return getHistoricalQuotes(ticker, fromDate, toDate, PeriodType.DAY);
    }
}
