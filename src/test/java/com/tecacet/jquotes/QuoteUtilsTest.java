package com.tecacet.jquotes;

import com.tecacet.jquotes.yahoo.YahooQuoteParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class QuoteUtilsTest {

    @Test
    void truncate() {
        var map = new TreeMap<LocalDate, String>();
        for (LocalDate date = LocalDate.of(2020,1,1);
             date.isBefore(LocalDate.of(2021,12,31));
                     date = date.plusDays(1)) {
            map.put(date, "data");
        }
        var truncated = QuoteUtils.truncate(map, LocalDate.of(2020, 6, 2),
                LocalDate.of(2021, 3, 3));
        assertEquals(LocalDate.of(2020, 6, 2), truncated.firstKey());
        assertEquals(LocalDate.of(2021, 3, 3), truncated.lastKey());

        truncated = QuoteUtils.truncate(map, LocalDate.of(2018, 6, 2),
                LocalDate.of(2022, 3, 3));
        assertEquals(LocalDate.of(2020, 1, 1), truncated.firstKey());
        assertEquals(LocalDate.of(2021, 12, 30), truncated.lastKey());

        truncated = QuoteUtils.truncate(map, LocalDate.of(2018, 6, 2),
                LocalDate.of(2019, 3, 3));
        assertTrue(truncated.isEmpty());

        truncated = QuoteUtils.truncate(map, LocalDate.of(2022, 6, 2),
                LocalDate.of(2022, 3, 3));
        assertTrue(truncated.isEmpty());

        truncated = QuoteUtils.truncate(map, LocalDate.of(2021, 6, 2),
                LocalDate.of(2022, 3, 3));
        assertEquals(LocalDate.of(2021, 6, 2), truncated.firstKey());
        assertEquals(LocalDate.of(2021, 12, 30), truncated.lastKey());

    }

    @Test
    void resample() throws IOException {
        var parser = new YahooQuoteParser();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("TSLA.csv");
        var quotes = QuoteUtils.toSortedMap(parser.parse(is));
        assertEquals(LocalDate.of(2012, 1, 3), quotes.firstKey());
        assertEquals(LocalDate.of(2013, 12, 31), quotes.lastKey());
        var resampledByMonth = QuoteUtils.resample(quotes, PeriodType.MONTH);
        assertEquals(24, resampledByMonth.size());
        assertEquals(LocalDate.of(2012, 1, 31), resampledByMonth.firstKey());
        assertEquals(LocalDate.of(2013, 12, 31), resampledByMonth.lastKey());

        var resampledByYear= QuoteUtils.resample(quotes, PeriodType.YEAR);
        assertEquals(2, resampledByYear.size());
        assertEquals(LocalDate.of(2012, 12, 31), resampledByYear.firstKey());
        assertEquals(LocalDate.of(2013, 12, 31), resampledByYear.lastKey());

        var resampledByWeek= QuoteUtils.resample(quotes, PeriodType.WEEK);
        assertEquals(104, resampledByWeek.size());
        resampledByWeek.keySet().forEach(date -> {
            if (date.equals(LocalDate.of(2012,4,5)) ||
                date.equals(LocalDate.of(2013,3,28))) {
                assertEquals(DayOfWeek.THURSDAY, DayOfWeek.from(date));
            } else {
                assertEquals(DayOfWeek.FRIDAY, DayOfWeek.from(date), date.toString());
            }
                });



        try {
            QuoteUtils.resample(quotes, PeriodType.DAY);
            fail();
        } catch (IllegalArgumentException iae) {

        }

        try {
            QuoteUtils.resample(quotes, null);
            fail();
        } catch (IllegalArgumentException iae) {

        }

    }
}