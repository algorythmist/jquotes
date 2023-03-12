package com.tecacet.jquotes;

import com.tecacet.jquotes.iex.IexClient;
import com.tecacet.jquotes.iex.IexQuoteSupplier;
import com.tecacet.jquotes.tiingo.TiingoClient;
import com.tecacet.jquotes.tiingo.TiingoQuoteSupplier;
import com.tecacet.jquotes.yahoo.YahooFinanceClient;
import com.tecacet.jquotes.yahoo.YahooQuoteSupplier;

public interface QuoteSupplier {

    QuoteResponse getHistoricalQuotes(QuoteRequest request);

    static QuoteSupplier getInstance() {
        return new QuoteSupplier() {
            @Override
            public QuoteResponse getHistoricalQuotes(QuoteRequest request) {
                return getInstance(request.getQuoteProvider()).getHistoricalQuotes(request);
            }
        };
    }

    static QuoteSupplier getInstance(String token) {
        return new QuoteSupplier() {
            @Override
            public QuoteResponse getHistoricalQuotes(QuoteRequest request) {
                return getInstance(request.getQuoteProvider(), token).getHistoricalQuotes(request);
            }
        };
    }

    private static QuoteSupplier getInstance(QuoteProvider quoteProvider) {
        switch (quoteProvider) {
            case TIINGO:
                return new TiingoQuoteSupplier(TiingoClient.getInstance());
            case IEX:
                return new IexQuoteSupplier(IexClient.getInstance());
            case YAHOO:
                return new YahooQuoteSupplier(new YahooFinanceClient());
            default:
                throw new IllegalArgumentException("Provider not supported");
        }
    }

    private static QuoteSupplier getInstance(QuoteProvider quoteProvider, String token) {
        switch (quoteProvider) {
            case TIINGO:
                return new TiingoQuoteSupplier(TiingoClient.getInstance(token));
            case IEX:
                return new IexQuoteSupplier(IexClient.getInstance(token));
            case YAHOO:
                return new YahooQuoteSupplier(new YahooFinanceClient());
            default:
                throw new IllegalArgumentException("Provider not supported");
        }
    }
}
