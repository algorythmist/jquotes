package com.tecacet.jquotes.yahoo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class YahooQuote {

    private String symbol;
    private Long regularMarketTime;
    private Long regularMarketVolume;
    private BigDecimal regularMarketChange;
    private BigDecimal regularMarketChangePercent;
    private BigDecimal regularMarketDayHigh;
    private String regularMarketDayRange;
    private BigDecimal regularMarketDayLow;
    private BigDecimal regularMarketPrice;
    private BigDecimal regularMarketPreviousClose;

    private BigDecimal bid;
    private BigDecimal ask;
    private Integer bidSize;
    private Integer askSize;

    private BigDecimal fiftyTwoWeekLowChange;
    private BigDecimal fiftyTwoWeekLowChangePercent;
    private String fiftyTwoWeekRange;
    private BigDecimal fiftyTwoWeekHighChange;
    private BigDecimal fiftyTwoWeekHighChangePercent;
    private BigDecimal fiftyTwoWeekLow;
    private BigDecimal fiftyTwoWeekHigh;

    private String currency;
    private Long firstTradeDateMilliseconds;
    private Long gmtOffSetMilliseconds;
    private Integer priceHint;
    private Long dividendDate;
    private String exchange;
    private String shortName;
    private String longName;
    private String messageBoardId;
    private String market;
    private String exchangeTimezoneName;
    private String exchangeTimezoneShortName;
    private String marketState;
    private Boolean esgPopulated;


    private String fullExchangeName;
    private String financialCurrency;
    private BigDecimal regularMarketOpen;
    private Long averageDailyVolume3Month;
    private Long averageDailyVolume10Day;
    private Long earningsTimestamp;
    private Long earningsTimestampStart;
    private Long earningsTimestampEnd;
    private BigDecimal trailingAnnualDividendRate;
    private BigDecimal trailingPE;
    private BigDecimal trailingAnnualDividendYield;
    private BigDecimal epsTrailingTwelveMonths;
    private BigDecimal epsForward;
    private BigDecimal epsCurrentYear;
    private BigDecimal priceEpsCurrentYear;
    private Long sharesOutstanding;
    private BigDecimal bookValue;
    private BigDecimal fiftyDayAverage;
    private BigDecimal fiftyDayAverageChange;
    private BigDecimal fiftyDayAverageChangePercent;
    private BigDecimal twoHundredDayAverage;
    private BigDecimal twoHundredDayAverageChange;
    private BigDecimal twoHundredDayAverageChangePercent;
    private Long marketCap;
    private BigDecimal forwardPE;
    private BigDecimal priceToBook;
    private Integer sourceInterval;
    private Integer exchangeDataDelayedBy;
    private String averageAnalystRating;
    private Boolean tradeable;
    private Boolean cryptoTradeable;
    private String displayName;


}
