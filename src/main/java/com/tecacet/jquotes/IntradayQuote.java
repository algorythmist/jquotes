package com.tecacet.jquotes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IntradayQuote {

    LocalDateTime getTimestamp();

    BigDecimal getOpen();

    BigDecimal getPreviousClose();

    BigDecimal getBid();

    BigDecimal getAsk();

    Long getVolume();
}
