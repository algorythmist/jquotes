package com.tecacet.jquotes.tiingo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiingoIexQuote {

    private String ticker;
    private String timestamp;
    private String quoteTimestamp;
    private String lastSaleTimeStamp;
    private BigDecimal last;
    private int lastSize;
    private BigDecimal tngoLast;
    private BigDecimal prevClose;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal mid;
    private int volume;
    private int bidSize;
    private BigDecimal bidPrice;
    private int askSize;
    private BigDecimal askPrice;
}
