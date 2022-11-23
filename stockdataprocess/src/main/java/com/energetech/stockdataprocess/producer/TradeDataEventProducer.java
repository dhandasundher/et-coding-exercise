package com.energetech.stockdataprocess.producer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.energetech.stockdataprocess.model.TradeData;
import com.opencsv.CSVReader;

public class TradeDataEventProducer {

	public Stream<TradeData> produceEvents() {
		try {
			File f = new File(getClass().getClassLoader().getResource("stock_trade_data.txt").toURI());
			CSVReader reader = new CSVReader(new FileReader(f));
			reader.skip(1);
			Iterator<String[]> iter = reader.iterator();
			Spliterator<String[]> spliterator = Spliterators.spliteratorUnknownSize(iter, Spliterator.ORDERED);
			return StreamSupport.stream(spliterator, false).map(line -> convertToBean(line));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return Stream.empty();
	}

	private TradeData convertToBean(String[] line) {
		TradeData d = null;
		try {
			d = new TradeData(line);
		} catch (Exception e) {
			System.out.println("Error Parsing -> " + Arrays.toString(line));
			return null;
		}
		return d;
	}
}
