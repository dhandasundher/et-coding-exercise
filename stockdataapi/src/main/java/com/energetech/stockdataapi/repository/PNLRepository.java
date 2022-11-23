package com.energetech.stockdataapi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class PNLRepository {

	@Autowired
	MongoTemplate mt;

	public List<Document> getTraderRealisedPNL(String trader, LocalDateTime asOf) {
		MatchOperation match = null;
		if (asOf != null)
			match = Aggregation.match(Criteria.where("trader_name").is(trader).and("realised").is(true).and("as_of").lte(asOf));
		else
			match = Aggregation.match(Criteria.where("trader_name").is(trader).and("realised").is(true));
		
		SortOperation sort = Aggregation.sort(Direction.ASC, "as_of");
		GroupOperation group = Aggregation.group("as_of.year", "as_of.month")
								.sum("pnl").as("pnl");
		
		Aggregation agg = Aggregation.newAggregation(match, sort, group);
		return mt.aggregate(agg, "trader_pnl", Document.class).getMappedResults();
	}
	
	public List<Document> getTraderUnRealisedPNL(String trader, LocalDateTime asOf) {
		MatchOperation match = null;
		if (asOf != null)
			match = Aggregation.match(Criteria.where("trader_name").is(trader).and("realised").is(false).and("as_of").lte(asOf));
		else
			match = Aggregation.match(Criteria.where("trader_name").is(trader).and("realised").is(false));
		
		SortOperation sort = Aggregation.sort(Direction.DESC, "as_of");
		LimitOperation limit = Aggregation.limit(1);
		Aggregation agg = Aggregation.newAggregation(match, sort, limit);
		return mt.aggregate(agg, "trader_pnl", Document.class).getMappedResults();
	}
	
	
}
