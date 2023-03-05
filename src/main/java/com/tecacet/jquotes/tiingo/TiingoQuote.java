package com.tecacet.jquotes.tiingo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tecacet.jquotes.Quote;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiingoQuote implements Quote {

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    protected LocalDate date;

    protected BigDecimal open;
    protected BigDecimal high;
    protected BigDecimal low;
    protected BigDecimal close;
    protected Long volume;
    private BigDecimal adjClose;
    private BigDecimal adjHigh;
    private BigDecimal adjLow;
    private BigDecimal adjOpen;
    private Long adjVolume;

    private BigDecimal splitFactor;
    private BigDecimal divCash;

    @Override
    public Optional<BigDecimal> getDividend() {
        return Optional.ofNullable(divCash);
    }

    @Override
    public Optional<BigDecimal> getSplitRatio() {
        return Optional.ofNullable(splitFactor);
    }

}
