package com.tecacet.jquotes.tiingo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tecacet.jquotes.token.EnvironmentTokenSupplier;
import com.tecacet.jquotes.token.FixedTokenSupplier;
import com.tecacet.jquotes.token.TokenSupplier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class TiingoClient {

    private static final String BASE_URL = "https://api.tiingo.com/tiingo";
    private static final String BASE_IEX_URL = "https://api.tiingo.com/iex";
    private static final String DAILY_URL_BASE = BASE_URL + "/daily";
    private final OkHttpClient httpClient = new OkHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final TokenSupplier tokenSupplier;

    public static TiingoClient getInstance(String token) {
        return token == null ? getInstance() : getInstance(new FixedTokenSupplier(token));
    }

    public static TiingoClient getInstance(TokenSupplier tokenSupplier) {
        return new TiingoClient(tokenSupplier);
    }

    public static TiingoClient getInstance() {
        return new TiingoClient(new EnvironmentTokenSupplier("TIINGO_TOKEN"));
    }

    @SneakyThrows
    public StockMetadata getMetadata(String symbol) {
        String url = DAILY_URL_BASE + "/" + symbol;
        log.info("Calling url {}", url);
        String content = execute(url);
        return objectMapper.readValue(content, StockMetadata.class);
    }

    /**
     * Get the last available daily quote
     * @param symbol the security symbol
     * @return the quote or null if not available
     * @throws IOException if the call fails
     */
    public TiingoQuote getLastDailyQuote(String symbol) throws IOException {
        String url = String.format("%s/%s/prices", DAILY_URL_BASE, symbol);
        log.info("Calling url {}", url);
        String content = execute(url);
        List<TiingoQuote> quotes = objectMapper.readValue(content, new TypeReference<>() {
        });
        return quotes.isEmpty() ? null : quotes.get(0);
    }

    /**
     * Get the quote history for a symbol between two dates
     * @param symbol the security symbol
     * @param startDate inclussive
     * @param endDate inclussive
     * @return a list of quotes
     * @throws IOException if the call fails
     */
    public List<TiingoQuote> getQuoteHistory(String symbol, LocalDate startDate, LocalDate endDate) throws IOException {
        String url = String.format("%s/%s/prices?startDate=%s&endDate=%s", DAILY_URL_BASE, symbol,
                startDate, endDate);
        log.info("Calling url {}", url);
        String content = execute(url);
        return objectMapper.readValue(content, new TypeReference<>() {
        });
    }

    public TiingoIexQuote getCurrentQuote(String symbol) throws IOException {
        String url = String.format("%s/%s", BASE_IEX_URL, symbol);
        String content = execute(url);
        List<TiingoIexQuote> quotes = objectMapper.readValue(content, new TypeReference<>() {
        });
        return quotes.isEmpty() ? null : quotes.get(0);
    }

    private String execute(String url) throws IOException {
        String token = tokenSupplier.getToken();
        Request request = new Request.Builder().url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", String.format("Token %s", token))
                .build();
        Response response = httpClient.newCall(request).execute();
        String content = response.body().string();
        if (!response.isSuccessful()) {
            throw new IOException(String.format("Call failed with code %d and message: %s", response.code(), content));
        }
        return content;
    }


}
