package com.tecacet.jquotes.tiingo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiingoQuote {

    private BigDecimal adjClose;
    private BigDecimal adjHigh;
    private BigDecimal adjLow;
    private BigDecimal adjOpen;
    private Long adjVolume;
    private BigDecimal close;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate date;
    private BigDecimal divCash;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal open;
    private BigDecimal splitFactor;
    private Long volume;

}
