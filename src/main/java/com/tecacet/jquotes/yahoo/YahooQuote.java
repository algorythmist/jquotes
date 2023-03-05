package com.tecacet.jquotes.yahoo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecacet.jquotes.BaseQuote;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class YahooQuote extends BaseQuote {

    private BigDecimal adjustedClose;

}
