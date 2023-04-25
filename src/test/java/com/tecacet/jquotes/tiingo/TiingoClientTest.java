package com.tecacet.jquotes.tiingo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

//TODO: test dividend
class TiingoClientTest {

    TiingoClient tiingoClient = TiingoClient.getInstance();

    @Test
    void getMetadata() {

        StockMetadata result = tiingoClient.getMetadata("TSLA");
        assertTrue(result.getDescription().startsWith("Tesla Motors, Inc. "));
        assertEquals("Tesla Inc", result.getName());
        assertEquals("NASDAQ", result.getExchangeCode());
        assertEquals("TSLA", result.getTicker());
        assertEquals(LocalDate.of(2010, 6, 29), result.getStartDate());

    }

    @Test
    void getLastQuote() throws IOException {
        TiingoQuote quote = tiingoClient.getLastDailyQuote("TSLA");
        assertEquals(1.0, quote.getSplitFactor().doubleValue(), 0.001);
    }

    @Test
    void getQuoteHistory() throws IOException {
        List<TiingoQuote> quotes = tiingoClient.getQuoteHistory("TSLA",
                LocalDate.of(2019, 1, 2),
                LocalDate.of(2020, 9, 15));
        assertEquals(430, quotes.size());
        SortedMap<LocalDate, TiingoQuote> quoteMap = quotes.stream()
                .collect(Collectors.toMap(TiingoQuote::getDate, Function.identity(),
                        (a, b) -> a,
                        TreeMap::new));
        assertEquals(LocalDate.of(2019, 1, 2), quoteMap.firstKey());
        assertEquals(LocalDate.of(2020, 9, 15), quoteMap.lastKey());
        TiingoQuote splitQuote = quoteMap.get(LocalDate.of(2020, 8, 31));
        assertEquals(5.0, splitQuote.getSplitFactor().doubleValue(), 0.001);
        assertEquals(5.0, splitQuote.getSplitRatio().get().doubleValue(), 0.001);
    }

    @Test
    void mutualFunds() throws IOException {
        TiingoClient tiingoClient = TiingoClient.getInstance();
        StockMetadata metadata = tiingoClient.getMetadata("VMFXX");
        assertEquals("VANGUARD FEDERAL MONEY MARKET FUND INVESTOR SHARES", metadata.getName());

        TiingoQuote lastQuote = tiingoClient.getLastDailyQuote("FBCVX");
        assertEquals(0, lastQuote.getVolume());
        assertEquals(0, lastQuote.getAdjVolume());
        assertEquals(lastQuote.getClose(), lastQuote.getOpen());

        List<TiingoQuote> quotes = tiingoClient.getQuoteHistory("SFLNX",
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2020, 1, 11));
        assertEquals(259, quotes.size());

    }

    @Test
    void missingSymbol() {
        try {
            tiingoClient.getQuoteHistory("ABCDEF",
                    LocalDate.of(2019, 1, 1),
                    LocalDate.of(2020, 1, 11));
            fail();
        } catch (IOException ioe) {
            assertEquals("Call failed with code 404 and message: {\"detail\":\"Error: Ticker 'ABCDEF' not found\"}", ioe.getMessage());
        }
    }

    @Test
    void getCurrentQuotes() throws IOException {
        var quotes = tiingoClient.getCurrentQuotes("AAPL","GOOGL");
        assertEquals(2, quotes.size());
    }

}