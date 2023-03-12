package com.tecacet.jquotes.io;

import com.tecacet.jquotes.Quote;
import com.tecacet.jquotes.yahoo.YahooQuoteParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVQuoteWriterTest {

    @Test
    void write() throws IOException {
        var parser = new YahooQuoteParser();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("TSLA.csv");
        List<? extends Quote> prices = parser.parse(is);
        var priceWriter = new CSVQuoteWriter();
        priceWriter.write("TEST.csv", prices);

        File file = new File("TEST.csv");
        assertTrue(file.exists());
        file.delete();
    }
}