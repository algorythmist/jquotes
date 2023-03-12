package com.tecacet.jquotes.io;

import com.tecacet.jflat.CSVWriter;
import com.tecacet.jquotes.Quote;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * Utility to write quotes in a CSV file
 */
public class CSVQuoteWriter {

    private static final String[] HEADER = new String[] {"Date", "Open", "High", "Low", "Close", "Volume", "Adj Close"};
    private static final String[] PROPERTIES = new String[] {"date", "open", "high", "low", "close", "volume", "adjustedClose"};

    /**
     * Write a collection of quotes into a CSV file
     * @param filename name of the CSV file
     * @param quotes the quotes
     * @throws IOException if the operation fails
     */
    public void write(String filename, Collection<? extends Quote> quotes) throws IOException {
        try (FileWriter fw = new FileWriter(filename)) {
            CSVWriter writer = CSVWriter.writerForProperties(PROPERTIES);
            writer.withHeader(HEADER);
            writer.write(fw, quotes);
        }
    }
}
