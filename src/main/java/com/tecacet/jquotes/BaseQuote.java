package com.tecacet.jquotes;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
public class BaseQuote implements Quote {

    protected LocalDate date;
    protected BigDecimal open;
    protected BigDecimal high;
    protected BigDecimal low;
    protected BigDecimal close;
    protected Long volume;

    @Setter
    protected BigDecimal dividend;
    @Setter
    protected BigDecimal splitRatio;

    @Override
    public Optional<BigDecimal> getDividend() {
        return Optional.ofNullable(dividend);
    }

    @Override
    public Optional<BigDecimal> getSplitRatio() {
        return Optional.ofNullable(splitRatio);
    }

}
