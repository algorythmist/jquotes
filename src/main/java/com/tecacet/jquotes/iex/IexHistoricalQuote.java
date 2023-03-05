package com.tecacet.jquotes.iex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecacet.jquotes.BaseQuote;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class IexHistoricalQuote extends BaseQuote {


}
