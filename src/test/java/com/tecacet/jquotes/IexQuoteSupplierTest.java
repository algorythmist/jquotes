package com.tecacet.jquotes;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IexQuoteSupplierTest {

    @Test
    void getDailyQuotes() {
        var startDate = LocalDate.now().minusYears(2);
        var endDate = LocalDate.now().minusMonths(1);

        var request = QuoteRequest.builder()
                .quoteProvider(QuoteProvider.IEX)
                .addSymbols("GOOGL", "IBM", "NOTASYMBOL")
                .fromDate(startDate)
                .toDate(endDate)
                .periodType(PeriodType.DAY)
                .build();

        var supplier = QuoteSupplier.getInstance();
        var response = supplier.getHistoricalQuotes(request);
        var quotes = response.getQuotes();
        assertEquals(2, quotes.size());
        var ibm = quotes.get("IBM");
        var divQuote = ibm.get(LocalDate.of(2023,2,9));
        assertEquals(1.65, divQuote.getDividend().get().doubleValue(), 0.0001);
        assertTrue(divQuote.getSplitRatio().isEmpty());

        var googl = quotes.get("GOOGL");
        var splitQuote = googl.get(LocalDate.of(2022,7,18));
        assertEquals(20.0, splitQuote.getSplitRatio().get().doubleValue(), 0.0001);
        assertTrue(splitQuote.getDividend().isEmpty());
    }
}
