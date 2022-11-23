package com.energetech.stockdataprocess.handlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.energetech.stockdataprocess.dao.MongoConnection;
import com.energetech.stockdataprocess.model.TradeData;
import com.energetech.stockdataprocess.utils.Constants;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertManyResult;

public class PositionHandler implements ITradeDataEventHandler {

	Map<String, Map<String, Integer>> traderLatestPosition = new HashMap<>();
	MongoCollection<Document> traderPositionCollection;
	
	public PositionHandler() {
		traderPositionCollection = MongoConnection.db.getCollection("trader_position");
		traderPositionCollection.drop();
	}
	
	List<Document> batch = new ArrayList<>();
	int count = 0;
	
	@Override
	public void handleEvent(TradeData data) {
		Document openTrade = new Document("trader_name", data.getTraderName())
				.append("company", data.getCompany())
				.append("as_of", LocalDateTime.of(data.getTradeDate(), Constants.CLOSE_OF_BUS_TIME))
				.append("position", data.getQuantity());
		batch.add(openTrade);
		
		Document closeTrade = new Document("trader_name", data.getTraderName())
				.append("company", data.getCompany())
				.append("as_of", data.getFilingDate())
				.append("position", 0);
		batch.add(closeTrade);
		if (batch.size() == BATCH_SIZE) {
			InsertManyResult result = traderPositionCollection.insertMany(batch);
			count = count + result.getInsertedIds().size();
			System.out.format("Positions Inserted = %d. Total Inserted = %d \n", result.getInsertedIds().size(), count);
			batch.clear();
		}
	}
	
	@Override
	public void flushToDB() {
		if (batch.size() > 0) {
			InsertManyResult result = traderPositionCollection.insertMany(batch);
			count = count + result.getInsertedIds().size();
			System.out.format("Positions Inserted = %d. Total Inserted = %d \n", result.getInsertedIds().size(), count);
			batch.clear();
		}
		System.out.format("%d total Positions flushed to DB...\n", count);
	}

}
