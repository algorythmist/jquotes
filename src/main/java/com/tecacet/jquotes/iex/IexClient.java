package com.tecacet.jquotes.iex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tecacet.jquotes.token.EnvironmentTokenSupplier;
import com.tecacet.jquotes.token.FixedTokenSupplier;
import com.tecacet.jquotes.token.TokenSupplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class IexClient {

    private static final String URL_BASE = "https://cloud.iexapis.com/v1";
    private static final String URL_BATCH_BASE = URL_BASE + "/stock/market/batch";
    private static final String CHART_URL = URL_BASE + "/stock/%s/chart/%s";
    private static final String DIVIDEND_URL = URL_BASE + "/stock/%s/dividends/%s";
    private static final String SPLIT_URL = URL_BASE + "/stock/%s/splits/%s";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final OkHttpClient httpClient = new OkHttpClient();
    private final TokenSupplier tokenSupplier;

    public static IexClient getInstance(String token) {
        return getInstance(new FixedTokenSupplier(token));
    }

    static IexClient getInstance(TokenSupplier tokenSupplier) {
        return new IexClient(tokenSupplier);
    }

    static IexClient getInstance() {
        return new IexClient(new EnvironmentTokenSupplier("IEX_TOKEN"));
    }

    public IexQuote getDelayedQuote(String symbol) {
        Map<String, IexQuote> quotes = getDelayedQuotes(symbol);
        return quotes.get(symbol);
    }

    @SneakyThrows
    public Map<String, IexQuote> getDelayedQuotes(String... symbols) {
        String symbolsString = String.join(",", symbols);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_BATCH_BASE).newBuilder();
        urlBuilder.addQueryParameter("token", tokenSupplier.getToken());
        urlBuilder.addQueryParameter("symbols", symbolsString);
        urlBuilder.addQueryParameter("types", "quote");
        String url = urlBuilder.build().toString();
        log.info("Calling {} for symbols {}", URL_BATCH_BASE, symbolsString);
        val content = execute(url);
        return parseQuotes(content);

    }

    @SneakyThrows
    public List<IexHistoricalQuote> getDailyQuotes(String symbol, Range range) {
        String url = String.format(CHART_URL, symbol, range.getCode());
        log.info("Calling {}", url);
        val content = execute(buildUrl(url));
        return objectMapper.readValue(content, new TypeReference<>() {
        });
    }

    @SneakyThrows
    public List<Dividend> getDividends(String symbol, Range range) {
        String url = String.format(DIVIDEND_URL, symbol, range.getCode());
        log.info("Calling {}", url);
        val content = execute(buildUrl(url));
        return objectMapper.readValue(content, new TypeReference<>() {
        });
    }

    @SneakyThrows
    public List<Split> getSplits(String symbol, Range range) {
        String url = String.format(SPLIT_URL, symbol, range.getCode());
        log.info("Calling {}", url);
        val content = execute(buildUrl(url));
        return objectMapper.readValue(content, new TypeReference<>() {
        });
    }

    private String buildUrl(String url) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("token", tokenSupplier.getToken());
        return urlBuilder.build().toString();
    }

    private String execute(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = httpClient.newCall(request).execute();
        String content = response.body().string();
        if (!response.isSuccessful()) {
            throw new IOException(String.format("Call failed with code %d and message: %s", response.code(), content));
        }
        return content;
    }

    private Map<String, IexQuote> parseQuotes(String json) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(json);
        Map<String, IexQuote> quotes = new HashMap<>();
        for (Iterator<Map.Entry<String, JsonNode>> it = root.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = it.next();
            QuoteWrapper qw = objectMapper.convertValue(entry.getValue(), QuoteWrapper.class);
            quotes.put(entry.getKey(), qw.getQuote());
        }
        return quotes;
    }

}

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class QuoteWrapper {

    private IexQuote quote;
}