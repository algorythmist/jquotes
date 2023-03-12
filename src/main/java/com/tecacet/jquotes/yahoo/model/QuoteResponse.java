package com.tecacet.jquotes.yahoo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("quoteResponse")
public class QuoteResponse {
    @JsonProperty("result")
    private List<YahooQuote> quotes;

    @JsonProperty("error")
    private String error;
}

