package com.tecacet.jquotes;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class QuoteSupplierTest {

    @Test
    void getTiingoDailyQuotes() {
        var request = QuoteRequest.builder()
                .symbols("GOOGL", "IBM", "NOTASYMBOL")
                .fromDate(LocalDate.of(2022, 1,1))
                .toDate(LocalDate.of(2023, 12, 31))
                .periodType(PeriodType.DAY)
                .build();
        validateRequest(request);

        var supplier = QuoteSupplier.getInstance(QuoteProvider.TIINGO);
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
    void getIntradayQuotes() {
        getIntradayQuotes(QuoteProvider.TIINGO);
        //These no longer work
        //getIntradayQuotes(QuoteProvider.YAHOO);
        //getIntradayQuotes(QuoteProvider.IEX);
    }

    @Test
    void getIntradayQuote() {
        var supplier = QuoteSupplier.getInstance(QuoteProvider.TIINGO);
        var quote = supplier.getIntradayQuote("AAPL");
        assertNotNull(quote);
        assertEquals("AAPL", quote.getSymbol());
    }

    @Test
    void getIntradayQuoteReturnsNullForInvalidSymbol() {
        var supplier = QuoteSupplier.getInstance(QuoteProvider.TIINGO);
        var quote = supplier.getIntradayQuote("NOTASYMBOL123456");
        assertNull(quote);
    }

    @Test
    void getInstanceWithNoArguments() {
        var supplier = QuoteSupplier.getInstance();
        assertNotNull(supplier);
        // Verify it returns a working instance by calling a method
        var quotes = supplier.getIntradayQuotes("AAPL");
        assertNotNull(quotes);
        assertFalse(quotes.isEmpty());
    }

    private void getIntradayQuotes(QuoteProvider quoteProvider) {
        var suplier = QuoteSupplier.getInstance(quoteProvider);
        var quotes = suplier.getIntradayQuotes("AAPL", "MSFT");
        var aapl = quotes.get("AAPL");
        var msft = quotes.get("MSFT");
        assertNotNull(aapl);
        assertNotNull(msft);
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