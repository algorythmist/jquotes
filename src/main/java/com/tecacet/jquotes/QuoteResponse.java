package com.tecacet.jquotes;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class QuoteResponse {

    @Builder.Default
    private List<String> symbols = new ArrayList<>();

    private LocalDate fromDate;

    private LocalDate toDate;

    @Builder.Default
    private boolean adjusted = false;

    @Builder.Default
    private boolean includeDividends = true;

    @Builder.Default
    private boolean includeSplits = true;

    @Builder.Default
    private PeriodType periodType = PeriodType.DAY;

    private List<Quote> quotes;
}
