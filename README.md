# jquotes
Stock quotes for Java applications

Store Tiingo token in TIINGO_TOKEN

Store Tiingo token in IEX_TOKEN

```java
        var supplier = QuoteSupplier.getInstance();

        var startDate = LocalDate.now().minusYears(2);
        var endDate = LocalDate.now().minusMonths(1);
        var request = QuoteRequest.builder()
                .quoteProvider(QuoteProvider.IEX)
                .symbols("GOOGL", "IBM", "NOTASYMBOL")
                .fromDate(startDate)
                .toDate(endDate)
                .periodType(PeriodType.DAY)
                .build();
        
        var response = supplier.getHistoricalQuotes(request);
```