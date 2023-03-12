package com.tecacet.jquotes.yahoo;

import com.tecacet.jquotes.PeriodType;
import com.tecacet.jquotes.yahoo.model.Split;
import com.tecacet.jquotes.yahoo.model.YahooHistoricalQuote;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YahooFinanceClientTest {

    YahooFinanceClient yahooFinanceClient = YahooFinanceClient.getInstance();

    @Test
    void getHistoricalQuotes() throws IOException {

        LocalDate fromDate = LocalDate.of(2018, 1, 2);
        LocalDate toDate = LocalDate.of(2018, 11, 1);
        List<YahooHistoricalQuote> prices = yahooFinanceClient.getHistoricalQuotes("AAPL", fromDate, toDate, PeriodType.DAY);
        assertEquals(211, prices.size());

        YahooHistoricalQuote firstPrice = prices.get(0);
        assertEquals(fromDate, firstPrice.getDate());
        //TODO validate quotes

        YahooHistoricalQuote lastPrice = prices.get(prices.size() - 1);
        assertEquals(LocalDate.of(2018, 10, 31), lastPrice.getDate());
        assertEquals(153435600, lastPrice.getVolume());

    }

    @Test
    void testGetSplitHistory() {

        List<Split> splits = yahooFinanceClient.getSplitHistory("AAPL", LocalDate.of(2005, 1, 1), LocalDate.of(2015, 12, 31));
        assertEquals(2, splits.size());
        Split split1 = splits.get(1);
        assertEquals(LocalDate.of(2014, 6, 9), split1.getDate());
        assertEquals("7:1 on 2014-06-09", split1.toString());
        Split split2 = splits.get(0);
        assertEquals(LocalDate.of(2005, 2, 28), split2.getDate());
        assertEquals("2:1 on 2005-02-28", split2.toString());
        assertFalse(split2.isReverse());
    }

    @Test
    void testReverseSplit() {

        List<Split> splits = yahooFinanceClient.getSplitHistory("ZSL", LocalDate.of(2010, 1, 1),
                LocalDate.of(2016, 12, 31));
        assertEquals(4, splits.size());

        Split split1 = splits.get(3);
        assertEquals("2:1 on 2015-11-13", split1.toString());
        assertFalse(split1.isReverse());
        assertEquals(LocalDate.of(2015, 11, 13), split1.getDate());
        Split split4 = splits.get(1);
        assertEquals("1:4 on 2011-02-25", split4.toString());
        assertTrue(split4.isReverse());
        assertEquals(LocalDate.of(2011, 2, 25), split4.getDate());
    }

    @Test
    void testGetHistoricalDividends() {

        Map<LocalDate, BigDecimal> dividends = yahooFinanceClient.getHistoricalDividends("IBM", LocalDate.of(2014, 1, 1), LocalDate.of(2016, 1, 1));
        assertEquals(8, dividends.size());
        String[] expected = {"2014-02-06", "2014-05-07", "2014-08-06", "2014-11-06", "2015-02-06", "2015-05-06", "2015-08-06", "2015-11-06"};
        LocalDate[] dates = dividends.keySet().stream().sorted().toArray(LocalDate[]::new);
        double[] prices = {0.908222, 1.0516, 1.0516, 1.0516, 1.0516, 1.24283, 1.24283, 1.24283};
        for (int i = 0; i < expected.length; i++) {
            LocalDate date = dates[i];
            assertEquals(expected[i], date.toString());
            assertEquals(prices[i], dividends.get(date).doubleValue(), 0.001,
                    "difference at index " + i);
        }
    }

    @Test
    void getEntireDividendHistory() {
        Map<LocalDate, BigDecimal> dividends = yahooFinanceClient.getHistoricalDividends("AGG", LocalDate.of(2000, 1, 1), LocalDate.of(2016, 11, 9));
        assertEquals(157, dividends.size());
    }

    @Test
    void getLatestQuote() throws IOException {
        var quote = yahooFinanceClient.getLatestQuote("IBM");
        assertEquals("USD", quote.getCurrency());
        assertEquals("IBM", quote.getDisplayName());
    }

    @Test
    void getBadQuote() {
        try {
            yahooFinanceClient.getLatestQuote("NOTEXISTS");
            fail();
        } catch (IOException ioe) {

        }
    }
}