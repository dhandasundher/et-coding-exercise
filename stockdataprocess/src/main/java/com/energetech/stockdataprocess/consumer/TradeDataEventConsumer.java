package com.energetech.stockdataprocess.consumer;

import java.util.List;

import com.energetech.stockdataprocess.handlers.ITradeDataEventHandler;
import com.energetech.stockdataprocess.handlers.PNLHandler;
import com.energetech.stockdataprocess.handlers.PositionHandler;
import com.energetech.stockdataprocess.model.TradeData;

public class TradeDataEventConsumer {
	
	public int invalidData = 0;
	
	public List<ITradeDataEventHandler> handlers = List.of(
				new PositionHandler(),
				new PNLHandler()
			);
	
	public void consumeEvents(TradeData data) {
		if (valid(data)) {
			handlers.forEach(handler -> handler.handleEvent(data));
		} else {
			invalidData++;
		}
	}

	private boolean valid(TradeData data) {
		return data != null && data.getQuantity() != 0;
	}

	public void log() {
		System.out.format("Invalid Data Count (Quantity == 0) = %d \n", invalidData);
	}
}
