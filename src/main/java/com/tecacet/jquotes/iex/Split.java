package com.tecacet.jquotes.iex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Split {

    private LocalDate exDate;
    private LocalDate declaredDate;
    private Integer toFactor;
    private Integer fromFactor;

    public BigDecimal getSplitRatio() {
        return new BigDecimal(fromFactor).divide(new BigDecimal(toFactor), RoundingMode.HALF_EVEN);
    }
}
