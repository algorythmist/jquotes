package com.tecacet.jquotes;

import java.time.LocalDate;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;


public class QuoteUtils {

    public static SortedMap<LocalDate, Quote> toMap(List<? extends Quote> quotes) {
        return quotes.stream().collect(Collectors.toMap(Quote::getDate, Function.identity(),
                (a , b) -> a,
                TreeMap::new));
    }
}
