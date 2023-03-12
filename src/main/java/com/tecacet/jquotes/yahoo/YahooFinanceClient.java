package com.tecacet.jquotes.yahoo;

import com.tecacet.jquotes.PeriodType;
import lombok.SneakyThrows;

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

    public static YahooFinanceClient getInstance() {
        return new YahooFinanceClient();
    }

    public List<YahooQuote> getHistoricalQuotes(String ticker, LocalDate fromDate, LocalDate toDate, PeriodType periodType)
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
