package com.tecacet.jquotes;

import org.junit.jupiter.api.Test;

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
}