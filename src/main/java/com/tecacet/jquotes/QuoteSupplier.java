package com.tecacet.jquotes;

import com.tecacet.jquotes.iex.IexClient;
import com.tecacet.jquotes.iex.IexQuoteSupplier;
import com.tecacet.jquotes.tiingo.TiingoClient;
import com.tecacet.jquotes.tiingo.TiingoQuoteSupplier;
import com.tecacet.jquotes.yahoo.YahooFinanceClient;
import com.tecacet.jquotes.yahoo.YahooQuoteSupplier;

import java.util.Map;

public interface QuoteSupplier {

    QuoteResponse getHistoricalQuotes(QuoteRequest request);

    Map<String, IntradayQuote> getIntradayQuotes(String... symbols);

    default  IntradayQuote getIntradayQuote(String symbol) {
        var quotes = getIntradayQuotes(symbol);
        return quotes.isEmpty()? null : quotes.get(symbol);
    }


    static QuoteSupplier getInstance() {
        return getInstance(QuoteProvider.YAHOO);
    }

    static QuoteSupplier getInstance(QuoteProvider quoteProvider) {
        return getInstance(quoteProvider, null);
    }

    static QuoteSupplier getInstance(QuoteProvider quoteProvider, String token) {
        validate(quoteProvider, token);
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

    private static void validate(QuoteProvider quoteProvider, String providerToken) {
        Map<String, String> env = System.getenv();
        if (QuoteProvider.TIINGO == quoteProvider &&
                providerToken == null
                && !env.containsKey("TIINGO_TOKEN")) {
            throw new IllegalArgumentException("A tokem must be supplied or the environment variable TIINGO_TOKEN must be set");
        }
        if (QuoteProvider.IEX == quoteProvider &&
                providerToken == null
                && !env.containsKey("IEX_TOKEN")) {
            throw new IllegalArgumentException("A tokem must be supplied or the environment variable IEX_TOKEN must be set");
        }
    }
}
