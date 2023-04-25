package com.tecacet.jquotes;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class QuoteRequest {

    private final List<String> symbols;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final boolean adjusted;
    private final boolean includeDividends;
    private final boolean includeSplits;
    private final PeriodType periodType;

    public static Builder builder() {
        return new QuoteRequest.Builder();
    }

    public static class Builder {

        private final List<String> symbols = new ArrayList<>();
        private LocalDate fromDate;
        private LocalDate toDate;
        private boolean adjusted = false;
        private boolean includeDividends = true;
        private boolean includeSplits = true;
        private PeriodType periodType = PeriodType.DAY;

        public Builder symbol(String symbol) {
            this.symbols.add(symbol);
            return this;
        }

        public Builder symbols(String... symbols) {
            Collections.addAll(this.symbols, symbols);
            return this;
        }

        public Builder fromDate(LocalDate fromDate) {
            this.fromDate = fromDate;
            return this;
        }

        public Builder toDate(LocalDate toDate) {
            this.toDate = toDate;
            return this;
        }

        public Builder adjusted(boolean adjusted) {
            this.adjusted = adjusted;
            return this;
        }

        public Builder includeDividends(boolean includeDividends) {
            this.includeDividends = includeDividends;
            return this;
        }

        public Builder includeSplits(boolean includeSplits) {
            this.includeSplits = includeSplits;
            return this;
        }

        public Builder periodType(PeriodType periodType) {
            this.periodType = periodType;
            return this;
        }

        public QuoteRequest build() {
            return new QuoteRequest(this);
        }
    }

    private QuoteRequest(Builder builder) {
        this.symbols = builder.symbols;
        this.fromDate = builder.fromDate;
        this.toDate = builder.toDate;
        this.adjusted = builder.adjusted;
        this.includeDividends = builder.includeDividends;
        this.includeSplits = builder.includeSplits;
        this.periodType = builder.periodType;
        validate();
    }

    private void validate() {
        if (toDate == null || fromDate == null) {
            throw new IllegalArgumentException("from and to date must not be null");
        }
        if (symbols.isEmpty()) {
            throw new IllegalArgumentException("At least one symbol must be supplied");
        }
    }

}
