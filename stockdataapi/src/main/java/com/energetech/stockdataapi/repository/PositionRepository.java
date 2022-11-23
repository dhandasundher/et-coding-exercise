package com.energetech.stockdataapi.repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class PositionRepository {

	@Autowired
	MongoTemplate mt;

	public List<Document> getTraderPosition(String trader, LocalDateTime ts) {
		MatchOperation match = null;
		if (ts != null)
			match = Aggregation.match(Criteria.where("trader_name").is(trader).and("as_of").lte(ts));
		else
			match = Aggregation.match(Criteria.where("trader_name").is(trader));

		GroupOperation group = Aggregation.group("company").max("as_of").as("maxAsOf").push("$$ROOT").as("records");
		return aggregate(match, group);
	}

	public List<Document> getTraderPositionForPortfolio(List<String> traders, LocalDateTime ts) {
		MatchOperation match = null;
		if (ts != null)
			match = Aggregation.match(Criteria.where("trader_name").in(traders).and("as_of").lte(ts));
		else
			match = Aggregation.match(Criteria.where("trader_name").in(traders));

		GroupOperation group = Aggregation.group("company", "trader_name").max("as_of").as("maxAsOf").push("$$ROOT")
				.as("records");
		return aggregate(match, group);
	}

	private List<Document> aggregate(MatchOperation match, GroupOperation group) {
		AggregationOperation redactOperation = aoc -> {
			return new Document("$redact", new Document("$cond",
					Arrays.asList(new Document("$eq", Arrays.asList(
							new Document("$ifNull", Arrays.asList("$as_of", "$$ROOT.maxAsOf")), "$$ROOT.maxAsOf")),
							"$$DESCEND", "$$PRUNE")));
		};

		Aggregation agg = Aggregation.newAggregation(match, group, redactOperation);
		return mt.aggregate(agg, "trader_position", Document.class).getMappedResults();
	}
}
