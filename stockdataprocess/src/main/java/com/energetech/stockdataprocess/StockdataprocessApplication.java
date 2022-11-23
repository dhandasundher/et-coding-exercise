package com.energetech.stockdataprocess;

import com.energetech.stockdataprocess.consumer.TradeDataEventConsumer;
import com.energetech.stockdataprocess.dao.MongoConnection;
import com.energetech.stockdataprocess.producer.TradeDataEventProducer;

public class StockdataprocessApplication {

	public static void main(String[] args) {
		MongoConnection.open();
		TradeDataEventProducer eventProd = new TradeDataEventProducer();
		TradeDataEventConsumer eventCon = new TradeDataEventConsumer();
		eventProd.produceEvents()
			.forEach(tradeData -> eventCon.consumeEvents(tradeData));
		eventCon.handlers.forEach(h -> h.flushToDB());
		eventCon.log();
		MongoConnection.close();
	}
}
