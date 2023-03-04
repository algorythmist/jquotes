package com.tecacet.jquotes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface Quote {

    LocalDate getDate();

    BigDecimal getOpen();

    BigDecimal getClose();

    BigDecimal getHigh();

    BigDecimal getLow();

    Long getVolume();
    
}
