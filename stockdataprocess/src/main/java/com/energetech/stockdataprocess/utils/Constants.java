package com.energetech.stockdataprocess.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Constants {

	public static final DateTimeFormatter FILING_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	public static final DateTimeFormatter TRADE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public static final LocalTime START_OF_BUS_TIME = LocalTime.of(9, 0);
	public static final LocalTime CLOSE_OF_BUS_TIME = LocalTime.of(16, 0);
	
	public static final int SCALE = 4;
}
