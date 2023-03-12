package com.tecacet.jquotes;

import com.tecacet.jquotes.tiingo.TiingoClient;
import com.tecacet.jquotes.tiingo.TiingoQuoteSupplier;
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
                    .quoteProvider(QuoteProvider.TIINGO)
                    .toDate(LocalDate.of(2023, 2, 28))
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
                .quoteProvider(QuoteProvider.TIINGO)
                .fromDate(LocalDate.of(2020, 1, 1))
                .toDate(LocalDate.of(2023, 2, 28))
                .addSymbols("AAPL", "IBM")
                .build());
        var quotes = response.getQuotes();
        assertEquals(2, quotes.size());
    }
}