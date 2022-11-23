package com.energetech.stockdataprocess.handlers;

import com.energetech.stockdataprocess.model.TradeData;

public interface ITradeDataEventHandler {

	static final int BATCH_SIZE = 1000;
	public void handleEvent(TradeData data);
	
	public void flushToDB();
}
