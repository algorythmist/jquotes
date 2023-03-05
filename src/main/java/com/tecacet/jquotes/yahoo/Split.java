package com.tecacet.jquotes.yahoo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class Split {

    private final LocalDate date;
    private final int numerator;
    private final int denominator;

    public BigDecimal getSplitRatio() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), RoundingMode.HALF_EVEN);
    }

    public boolean isReverse() {
        return numerator > denominator;
    }

    @Override
    public String toString() {
        return String.format("%d:%d on %s", denominator, numerator, date);
    }
}
