package com.tecacet.jquotes.yahoo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class Split {

    private final LocalDate date;
    private final int fromFactor;
    private final int toFactor;

    public BigDecimal getSplitRatio() {
        return new BigDecimal(toFactor).divide(new BigDecimal(fromFactor), RoundingMode.HALF_EVEN);
    }

    public boolean isReverse() {
        return fromFactor > toFactor;
    }

    @Override
    public String toString() {
        return String.format("%d:%d on %s", toFactor, fromFactor, date);
    }
}
