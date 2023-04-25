package com.tecacet.jquotes;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class IntradayQuote {

    private final String symbol;
    private final BigDecimal open;
    private final BigDecimal previousClose;
    private final BigDecimal bid;
    private final BigDecimal ask;
    private final BigDecimal last;
    private final Long volume;
    private final String timestamp;
}
