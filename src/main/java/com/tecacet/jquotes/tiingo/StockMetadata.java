package com.tecacet.jquotes.tiingo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockMetadata {
    private String description;
    private LocalDate startDate;
    private String exchangeCode;
    private String name;
    private String ticker;
}
