package com.tecacet.jquotes;

public enum PeriodType {

	DAY(365), WEEK(52), MONTH(12), YEAR(1);

	private final int periodsInYear;

	PeriodType(int periods) {
		this.periodsInYear = periods;
	}

	public int getPeriodsInYear() {
		return periodsInYear;
	}

}
