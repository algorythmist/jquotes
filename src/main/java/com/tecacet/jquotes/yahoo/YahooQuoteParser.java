package com.tecacet.jquotes.yahoo;

import com.tecacet.jflat.CSVReader;
import org.apache.commons.io.input.BOMInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class YahooQuoteParser {

    private final static String[] PRICE_PROPERTIES = new String[] {"date", "open", "close", "volume", "high", "low", "adjustedClose"};
    private final static String[] PRICE_COLUMNS = new String[] {"Date", "Open", "Close", "Volume", "High", "Low", "Adj Close"};

    private final CSVReader<YahooQuote> reader;

    public YahooQuoteParser() {
        this(PRICE_PROPERTIES, PRICE_COLUMNS);
    }

    public YahooQuoteParser(String[] properties, String[] columns) {
        super();
        reader = CSVReader.readerWithHeaderMapping(YahooQuote.class, columns, properties);
    }

    public List<YahooQuote> parse(InputStream is) throws IOException {
        return reader.readAll(new BOMInputStream(is));
    }

}