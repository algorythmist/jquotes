package com.tecacet.jquotes.tiingo;

import com.tecacet.jquotes.Quote;
import com.tecacet.jquotes.QuoteUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TiingoClientTest {

    TiingoClient tiingoClient = TiingoClient.getInstance();

    @Test
    void getMetadata() {

        StockMetadata result = tiingoClient.getMetadata("TSLA");
        assertTrue(result.getDescription().startsWith("Tesla Motors, Inc. "));
        assertEquals("Tesla Inc", result.getName());
        assertEquals("NASDAQ", result.getExchangeCode());
        assertEquals("TSLA", result.getTicker());
        assertEquals(LocalDate.of(2010,6,29), result.getStartDate());

    }

    @Test
    void getLastQuote() {
        TiingoQuote quote = tiingoClient.getLastQuote("TSLA");
        assertEquals(1.0, quote.getSplitFactor().doubleValue(), 0.001);
        System.out.println(quote);
    }

    @Test
    void getQuoteHistory() {
        List<TiingoQuote> quotes = tiingoClient.getQuoteHistory("TSLA",
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2020, 9, 15));
        assertEquals(430, quotes.size());
        Map<LocalDate, TiingoQuote> quoteMap = quotes.stream()
                .collect(Collectors.toMap(TiingoQuote::getDate, Function.identity()));
        TiingoQuote splitQuote = quoteMap.get(LocalDate.of(2020, 8, 31));
        assertEquals(5.0, splitQuote.getSplitFactor().doubleValue(), 0.001);
    }

    @Test
    void mutualFunds() {
        TiingoClient tiingoClient = TiingoClient.getInstance();
        StockMetadata metadata = tiingoClient.getMetadata("VMFXX");
        assertEquals("VANGUARD FEDERAL MONEY MARKET FUND INVESTOR SHARES", metadata.getName());

        TiingoQuote lastQuote = tiingoClient.getLastQuote("FBCVX");
        assertEquals(0, lastQuote.getVolume());
        assertEquals(0, lastQuote.getAdjVolume());
        assertEquals(lastQuote.getClose(), lastQuote.getOpen());

        List<TiingoQuote> quotes = tiingoClient.getQuoteHistory("SFLNX",
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2020, 1, 11));
        assertEquals(259, quotes.size());

    }

}