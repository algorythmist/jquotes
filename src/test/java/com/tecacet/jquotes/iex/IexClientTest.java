package com.tecacet.jquotes.iex;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IexClientTest {

    private final IexClient iexClient = IexClient.getInstance();

    @Test
    void invalidToken() {
        IexClient iexClient = IexClient.getInstance("ax");
        try {
            iexClient.getDelayedQuote("IBM");
            fail("Exception should be thrown");
        } catch (Exception ioe) {
            assertEquals("Call failed with code 403 and message: The API key provided is not valid.", ioe.getMessage());
        }
    }

    @Test
    void validQuote() {
        Quote quote = iexClient.getDelayedQuote("IBM");
        assertEquals("IBM", quote.getSymbol());
        assertEquals("International Business Machines Corp.", quote.getCompanyName());
    }

    @Test
    void getDailyQuotes() {
        val quotes1m = iexClient.getDailyQuotes("NFLX", Range.ONE_MONTH);
        assertTrue(quotes1m.size() > 10);

        val quotes1y = iexClient.getDailyQuotes("NFLX", Range.ONE_YEAR);
        assertTrue(quotes1y.size() > 250);
    }

    @Test
    void getDividends() {
        val dividends = iexClient.getDividends("AAPL", Range.ONE_YEAR);
        assertTrue(dividends.size() > 0); //This API appears to be faulty

        val dividend = dividends.get(0);
        assertEquals("Ordinary Shares", dividend.getDescription());
        assertEquals("quarterly", dividend.getFrequency());
    }

    @Test
    void getSplits() {
        val splits = iexClient.getSplits("AAPL", Range.FIVE_YEARS);
        assertEquals(1, splits.size());
        val split = splits.get(0);
        assertEquals(4, split.getToFactor());
        assertEquals(1, split.getFromFactor());
        assertEquals(LocalDate.of(2020, 8, 31), split.getExDate());
    }
}