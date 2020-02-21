package com.tecacet.jquotes;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Quote {

    LocalDate getDate();

    BigDecimal getOpen();

    BigDecimal getClose();

    BigDecimal getHigh();

    BigDecimal getLow();

    BigDecimal getAdjustedClose();

    Long getVolume();

}
