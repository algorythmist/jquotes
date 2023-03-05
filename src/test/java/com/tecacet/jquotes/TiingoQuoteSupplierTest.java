package com.tecacet.jquotes;

import com.tecacet.jquotes.tiingo.TiingoClient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TiingoQuoteSupplierTest {

    TiingoClient tiingoClient = TiingoClient.getInstance();
    QuoteSupplier quoteSupplier = new TiingoQuoteSupplier(tiingoClient);

    @Test
    void missingRequiredParameter() {
        try {
            quoteSupplier.getHistoricalQuotes(QuoteRequest.builder()
                    .setQuoteProvider(QuoteProvider.TIINGO)
                    .setToDate(LocalDate.of(2023, 2, 28))
                    .addSymbols("AAPL", "IBM")
                    .build());
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("from and to date must not be null", iae.getMessage());
        }
    }

    @Test
    void getHistoricalQuotes() {
        var response = quoteSupplier.getHistoricalQuotes(QuoteRequest.builder()
                .setQuoteProvider(QuoteProvider.TIINGO)
                .setFromDate(LocalDate.of(2020, 1, 1))
                .setToDate(LocalDate.of(2023, 2, 28))
                .addSymbols("AAPL", "IBM")
                .build());
        var quotes = response.getQuotes();
        assertEquals(2, quotes.size());
    }
}