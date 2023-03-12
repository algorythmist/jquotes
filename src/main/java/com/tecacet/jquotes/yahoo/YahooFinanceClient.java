package com.tecacet.jquotes.yahoo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecacet.jquotes.PeriodType;
import com.tecacet.jquotes.yahoo.model.QuoteResponse;
import com.tecacet.jquotes.yahoo.model.Split;
import com.tecacet.jquotes.yahoo.model.YahooHistoricalQuote;
import com.tecacet.jquotes.yahoo.model.YahooQuote;
import lombok.Getter;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class YahooFinanceClient {

    private final YahooQuoteParser quoteParser = new YahooQuoteParser();
    private final YahooSplitParser splitParser = new YahooSplitParser();
    private final YahooDividendParser dividendParser = new YahooDividendParser();

    private final OkHttpClient httpClient = new OkHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static YahooFinanceClient getInstance() {
        return new YahooFinanceClient();
    }

    @Getter
    static class QuoteResponseWrapper {
        private QuoteResponse quoteResponse;
    }
    public YahooQuote getLatestQuote(String symbol) throws IOException {
        var quotes = getLatestQuotes(symbol);
        if (quotes.isEmpty()) {
            throw new IOException("Quote not found");
        }
        return quotes.get(0);
    }

    public List<YahooQuote> getLatestQuotes(String... symbol) throws IOException {
        String url = YahooConnectionUtils.getQuoteBaseUrl(symbol);
        Request request = new Request.Builder().url(url).build();
        Response response = httpClient.newCall(request).execute();
        String content = response.body().string();
        QuoteResponse quoteResponse =  objectMapper.readValue(content, QuoteResponseWrapper.class)
                .getQuoteResponse();
        if (quoteResponse.getError() != null) {
            throw new IOException(quoteResponse.getError());
        }
        return quoteResponse.getQuotes();
    }

    public List<YahooHistoricalQuote> getHistoricalQuotes(String ticker, LocalDate fromDate, LocalDate toDate, PeriodType periodType)
        throws IOException {
        Map<String, String> params = YahooConnectionUtils.getRequestParams(fromDate, toDate, periodType);
        InputStream is = YahooConnectionUtils.getUrlStream(ticker, params);
        return quoteParser.parse(is);
    }

    @SneakyThrows
    public List<Split> getSplitHistory(String symbol, LocalDate fromDate, LocalDate toDate) {
        Map<String, String> params = YahooConnectionUtils.getRequestParams(fromDate, toDate, PeriodType.DAY);
        params.put("events", "split");
        InputStream is = YahooConnectionUtils.getUrlStream(symbol, params);
        return splitParser.parse(is);
    }

    @SneakyThrows
    public Map<LocalDate, BigDecimal> getHistoricalDividends(String symbol, LocalDate fromDate, LocalDate toDate) {
        Map<String, String> params = YahooConnectionUtils.getRequestParams(fromDate, toDate, PeriodType.DAY);
        params.put("events", "div");
        InputStream is = YahooConnectionUtils.getUrlStream(symbol, params);
        return dividendParser.parse(is);
    }
}
