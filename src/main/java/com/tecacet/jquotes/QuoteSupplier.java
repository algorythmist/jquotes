package com.tecacet.jquotes;

import com.tecacet.jquotes.tiingo.TiingoClient;
import com.tecacet.jquotes.tiingo.TiingoQuoteSupplier;

import java.util.Map;
import java.util.Objects;

public interface QuoteSupplier {

    QuoteResponse getHistoricalQuotes(QuoteRequest request);

    Map<String, IntradayQuote> getIntradayQuotes(String... symbols);

    default  IntradayQuote getIntradayQuote(String symbol) {
        var quotes = getIntradayQuotes(symbol);
        return quotes.isEmpty()? null : quotes.get(symbol);
    }


    static QuoteSupplier getInstance() {
        return getInstance(QuoteProvider.TIINGO);
    }

    static QuoteSupplier getInstance(QuoteProvider quoteProvider) {
        return getInstance(quoteProvider, null);
    }

    static QuoteSupplier getInstance(QuoteProvider quoteProvider, String token) {
        validate(quoteProvider, token);
        if (Objects.requireNonNull(quoteProvider) == QuoteProvider.TIINGO) {
            return new TiingoQuoteSupplier(TiingoClient.getInstance(token));
        }
        throw new IllegalArgumentException("Provider not supported");
    }

    private static void validate(QuoteProvider quoteProvider, String providerToken) {
        Map<String, String> env = System.getenv();
        if (QuoteProvider.TIINGO == quoteProvider &&
                providerToken == null
                && !env.containsKey("TIINGO_TOKEN")) {
            throw new IllegalArgumentException("A tokem must be supplied or the environment variable TIINGO_TOKEN must be set");
        }
    }
}
