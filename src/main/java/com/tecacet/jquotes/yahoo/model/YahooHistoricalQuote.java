package com.tecacet.jquotes.yahoo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecacet.jquotes.BaseQuote;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class YahooHistoricalQuote extends BaseQuote {

    private BigDecimal adjustedClose;

}
