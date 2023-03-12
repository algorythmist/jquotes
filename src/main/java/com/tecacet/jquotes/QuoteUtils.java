package com.tecacet.jquotes;

import java.time.*;
import java.util.List;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;


public class QuoteUtils {

    public static NavigableMap<LocalDate, Quote> toSortedMap(List<? extends Quote> quotes) {
        return quotes.stream().collect(Collectors.toMap(Quote::getDate, Function.identity(),
                (a , b) -> a,
                TreeMap::new));
    }

    public static <T> SortedMap<LocalDate, T> truncate(SortedMap<LocalDate, T> quotes,
                                                       LocalDate fromDate, LocalDate toDate) {
        var truncated =  quotes.tailMap(fromDate);
        return truncated.isEmpty() ? truncated : truncated.headMap(toDate.plusDays(1)) ;
    }

    public static <T> NavigableMap<LocalDate, T> resample(NavigableMap<LocalDate, T> quotes,
                                                       PeriodType periodType) {
        if (null == periodType || PeriodType.DAY == periodType) {
            throw new IllegalArgumentException("Cannot resample at this period");
        }
        var resampled = new TreeMap<LocalDate, T>();
        LocalDate date = quotes.firstKey();
        while (date.compareTo(quotes.lastKey()) <= 0) {
            var endOfPeriod = endOfPeriod(date, periodType);
            if (endOfPeriod.compareTo(quotes.lastKey()) > 0) {
                break;
            }
            date = quotes.headMap(endOfPeriod, true).lastKey();
            if (date != null) {
                resampled.put(date, quotes.get(date));
            }
            date = endOfPeriod.plusDays(1);
        }
        return resampled;
    }

    private static LocalDate endOfPeriod(LocalDate date, PeriodType periodType) {
        switch (periodType) {
            case MONTH:
                return YearMonth.from(date).atEndOfMonth();
            case YEAR:
                return Year.from(date).atMonth(Month.DECEMBER).atEndOfMonth();
            case WEEK:
                var dayOfWeek = DayOfWeek.from(date);
                int daysUntilFriday = DayOfWeek.FRIDAY.getValue() - dayOfWeek.getValue();
                if (daysUntilFriday < 0) {
                    daysUntilFriday += 7;
                }
                return date.plusDays(daysUntilFriday);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
