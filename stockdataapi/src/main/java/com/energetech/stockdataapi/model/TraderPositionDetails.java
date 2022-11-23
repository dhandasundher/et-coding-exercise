package com.energetech.stockdataapi.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TraderPositionDetails {

	String trader;
	LocalDateTime ts;
	Map<String, Integer> positions = new HashMap<>();
	
	public TraderPositionDetails(String trader, LocalDateTime ts) {
		this.trader = trader;
		this.ts = ts;
	}
	
	public void addCompanyPosition(String company, Integer position) {
		Integer curPos = positions.getOrDefault(company, 0);
		positions.put(company, position+curPos);
	}

	public String getTrader() {
		return trader;
	}

	public LocalDateTime getTs() {
		return ts;
	}

	public Map<String, Integer> getPositions() {
		return positions;
	}

}
