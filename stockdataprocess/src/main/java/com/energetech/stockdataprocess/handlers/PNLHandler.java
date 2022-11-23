package com.energetech.stockdataprocess.handlers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.energetech.stockdataprocess.dao.MongoConnection;
import com.energetech.stockdataprocess.model.TradeData;
import com.energetech.stockdataprocess.utils.Constants;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertManyResult;

public class PNLHandler implements ITradeDataEventHandler {
	
	MongoCollection<Document> traderPNLCollection;
	
	public PNLHandler() {
		traderPNLCollection = MongoConnection.db.getCollection("trader_pnl");
		traderPNLCollection.drop();
	}
	
	@Override
	public void handleEvent(TradeData data) {
		if (data.getTradeDateClose().doubleValue() == 0 || data.getFileDateClose().doubleValue() == 0) return;
		
		BigDecimal quantity = new BigDecimal(data.getQuantity());
		BigDecimal value = data.getPrice().multiply(quantity);
		
		LocalDateTime tradeDateClose = LocalDateTime.of(data.getTradeDate(), Constants.CLOSE_OF_BUS_TIME);
		BigDecimal tradeDateValue = data.getTradeDateClose().multiply(quantity);
		BigDecimal tradeDatePNL = tradeDateValue.subtract(value);
		
		LocalDateTime filingDate = data.getFilingDate();
		BigDecimal filingDateValue = null;
		if (filingDate.toLocalTime().isBefore(Constants.CLOSE_OF_BUS_TIME)) {
			filingDateValue = data.getFileDateOpen().multiply(quantity);
		} else {
			filingDateValue = data.getFileDateClose().multiply(quantity);
		}
		BigDecimal filingDatePNL = filingDateValue.subtract(value);
		
		writeTODB(data, tradeDateClose, tradeDatePNL, false);
		writeTODB(data, filingDate, filingDatePNL, true);
	}
	
	List<Document> batch = new ArrayList<>();
	int count = 0;
	
	private void writeTODB(TradeData data, LocalDateTime asOf, BigDecimal pnl, boolean realisedOrUnrealised) {
		pnl = pnl.setScale(Constants.SCALE, RoundingMode.HALF_UP);
		Document doc = new Document("trader_name", data.getTraderName())
				.append("as_of", asOf)
				.append("pnl", pnl)
				.append("realised", realisedOrUnrealised);
		batch.add(doc);
		if (batch.size() == BATCH_SIZE) {
			InsertManyResult result = traderPNLCollection.insertMany(batch);
			count = count + result.getInsertedIds().size();
			System.out.format("PNL Inserted = %d. Total Inserted = %d \n", result.getInsertedIds().size(), count);
			batch.clear();
		}
	}

	@Override
	public void flushToDB() {
		if (batch.size() > 0) {
			InsertManyResult result = traderPNLCollection.insertMany(batch);
			count = count + result.getInsertedIds().size();
			System.out.format("PNL Inserted = %d. Total Inserted = %d \n", result.getInsertedIds().size(), count);
			batch.clear();
		}
		System.out.format("%d total PNL flushed to DB...\n", count);
	}

}
