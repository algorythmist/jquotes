package com.tecacet.jquotes;

public interface QuoteSupplier {

    QuoteResponse getHistoricalQuotes(QuoteRequest request);
    
}
