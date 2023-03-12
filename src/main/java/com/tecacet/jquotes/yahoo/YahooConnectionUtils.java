package com.tecacet.jquotes.yahoo;


import com.tecacet.jquotes.PeriodType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

@Slf4j
public class YahooConnectionUtils {

    private static final String HISTQUOTES_BASE_URL = "https://query1.finance.yahoo.com/v7/finance/download/";

    private static final String QUOTE_BASE_URL = "https://query1.finance.yahoo.com/v7/finance/quote?lang=en-US&region=US&corsDomain=finance.yahoo.com&symbols=%s";

    private static final int CONNECTION_TIMEOUT = 10000;

    public static String getQuoteBaseUrl(String... quotes) {
        return String.format(QUOTE_BASE_URL, String.join(",", quotes));
    }

    public static Map<String, String> getRequestParams(LocalDate from, LocalDate to, PeriodType periodType) throws IOException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("period1", String.valueOf(toSeconds(from)));
        params.put("period2", String.valueOf(toSeconds(to)));
        params.put("interval", getPeriodCode(periodType));
        // crumb
        params.put("crumb", CrumbManager.getCrumb());
        return params;
    }

    public static InputStream getUrlStream(String symbol, Map<String, String> params) throws IOException {
        String url = HISTQUOTES_BASE_URL + URLEncoder.encode(symbol, StandardCharsets.UTF_8) + "?" + getURLParameters(params);

        // Get CSV from Yahoo
        log.info("Sending request: {}", url);

        URL request = new URL(url);
        RedirectableRequest redirectableRequest = new RedirectableRequest(request, 5);
        redirectableRequest.setConnectTimeout(CONNECTION_TIMEOUT);
        redirectableRequest.setReadTimeout(CONNECTION_TIMEOUT);
        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put("Cookie", CrumbManager.getCookie());
        URLConnection connection = redirectableRequest.openConnection(requestProperties);
        return connection.getInputStream();
    }

    private static String getURLParameters(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            String key = entry.getKey();
            String value = entry.getValue();
            key = URLEncoder.encode(key, StandardCharsets.UTF_8);
            value = URLEncoder.encode(value, StandardCharsets.UTF_8);
            sb.append(String.format("%s=%s", key, value));
        }
        return sb.toString();
    }

    private static long toSeconds(LocalDate date) {
        ZoneId zoneId = ZoneId.of("America/New_York");
        return date.atStartOfDay(zoneId).toEpochSecond();
    }

    private static String getPeriodCode(PeriodType periodType) throws IllegalArgumentException {
        switch (periodType) {
            case DAY:
                return "1d";
            case WEEK:
                return "5d";
            case MONTH:
                return "1mo";
            case YEAR:
            default:
                throw new IllegalArgumentException("Period type " + periodType + " not supported");
        }
    }


}
