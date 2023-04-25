package com.tecacet.jquotes.iex;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class IexQuote {

    private String symbol;
    private BigDecimal avgTotalVolume;
    private String calculationPrice;
    private BigDecimal change;
    private BigDecimal changePercent;
    private BigDecimal close;
    private String closeSource;
    private Long closeTime;
    private String companyName;
    private String currency;
    private BigDecimal delayedPrice;
    private Long delayedPriceTime;
    private BigDecimal extendedChange;
    private BigDecimal extendedChangePercent;
    private BigDecimal extendedPrice;
    private Long extendedPriceTime;
    private BigDecimal high;
    private String highSource;
    private Long highTime;
    private BigDecimal iexAskPrice;
    private int iexAskSize;
    private BigDecimal iexBidPrice;
    private int iexBidSize;
    private BigDecimal iexClose;
    private Long iexCloseTime;
    private Long iexLastUpdated;
    private BigDecimal iexMarketPercent;
    private BigDecimal iexOpen;
    private Long iexOpenTime;
    private BigDecimal iexRealtimePrice;
    private int iexRealtimeSize;
    private int iexVolume;
    private Long lastTradeTime;
    private BigDecimal latestPrice;
    private String latestSource;
    private String latestTime;
    private Long latestUpdate;
    private int latestVolume;
    private BigDecimal low;
    private String lowSource;
    private Long lowTime;
    private BigDecimal marketCap;
    private BigDecimal oddLotDelayedPrice;
    private Long oddLotDelayedPriceTime;
    private BigDecimal open;
    private Long openTime;
    private String openSource;
    private BigDecimal peRatio;
    private BigDecimal previousClose;
    private int previousVolume;
    private String primaryExchange;
    private int volume;
    private BigDecimal week52High;
    private BigDecimal week52Low;
    private BigDecimal ytdChange;
    private boolean isUSMarketOpen;

}
