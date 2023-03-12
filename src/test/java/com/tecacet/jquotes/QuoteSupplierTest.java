package com.tecacet.jquotes;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class QuoteSupplierTest {

    @Test
    void getTiingoDailyQuotes() {
        var request = QuoteRequest.builder()
                .quoteProvider(QuoteProvider.TIINGO)
                .addSymbols("GOOGL", "IBM", "NOTASYMBOL")
                .fromDate(LocalDate.of(2022, 1,1))
                .toDate(LocalDate.of(2023, 12, 31))
                .periodType(PeriodType.DAY)
                .build();
        validateRequest(request);

        var supplier = QuoteSupplier.getInstance();
        var response = supplier.getHistoricalQuotes(request);
        validateResponse(response);
        var quotes = response.getQuotes();
        var ibm = quotes.get("IBM");
        var divQuote = ibm.get(LocalDate.of(2023,2,9));
        assertEquals(1.65, divQuote.getDividend().get().doubleValue(), 0.0001);
        //TODO: assertTrue(divQuote.getSplitRatio().isEmpty());

        var googl = quotes.get("GOOGL");
        var splitQuote = googl.get(LocalDate.of(2022,7,18));
        assertEquals(20.0, splitQuote.getSplitRatio().get().doubleValue(), 0.0001);
        //TODO: assertTrue(splitQuote.getDividend().isEmpty());
    }

    @Test
    void getYahooDailyQuotes() {
        var request = QuoteRequest.builder()
                .quoteProvider(QuoteProvider.YAHOO)
                .addSymbols("GOOGL", "IBM", "NOTASYMBOL")
                .fromDate(LocalDate.of(2022, 1,1))
                .toDate(LocalDate.of(2023, 12, 31))
                .periodType(PeriodType.DAY)
                .build();
        validateRequest(request);
        var suplier = QuoteSupplier.getInstance();
        var response = suplier.getHistoricalQuotes(request);
        validateResponse(response);

        assertTrue(response.isAdjusted());
        var quotes = response.getQuotes();
        var ibm = quotes.get("IBM");
        var divQuote = ibm.get(LocalDate.of(2023,2,9));
        assertEquals(1.65, divQuote.getDividend().get().doubleValue(), 0.0001);
        assertTrue(divQuote.getSplitRatio().isEmpty());

        var googl = quotes.get("GOOGL");
        var splitQuote = googl.get(LocalDate.of(2022,7,18));
        assertEquals(20.0, splitQuote.getSplitRatio().get().doubleValue(), 0.0001);
        assertTrue(splitQuote.getDividend().isEmpty());
    }

    private static void validateResponse(QuoteResponse response) {
        assertTrue(response.isIncludeSplits());
        assertTrue(response.isIncludeDividends());
        assertEquals(2, response.getQuotes().size());
    }

    private static void validateRequest(QuoteRequest request) {
        assertTrue(request.isIncludeDividends());
        assertTrue(request.isIncludeSplits());
        assertFalse(request.isAdjusted());
    }
}