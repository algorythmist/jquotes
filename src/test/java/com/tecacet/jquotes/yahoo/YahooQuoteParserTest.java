package com.tecacet.jquotes.yahoo;



import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class YahooQuoteParserTest {

    @Test
    void testParseStockHistory() throws IOException {
        YahooQuoteParser parser = new YahooQuoteParser();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("TSLA.csv");
        List<YahooQuote> prices = parser.parse(is);
        assertEquals(502, prices.size());
        YahooQuote first = prices.get(0);
        assertEquals(150.43, first.getAdjustedClose().doubleValue(), 0.001);
        assertEquals(150.43, first.getClose().doubleValue(), 0.001);
        assertEquals(4262400L, first.getVolume());
        assertEquals(LocalDate.of(2013, 12, 31), first.getDate());
    }

}
