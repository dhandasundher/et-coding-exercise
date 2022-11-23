package com.energetech.stockdataapi.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PNLDetails {

	Map<String, BigDecimal> realised = new HashMap<>();
	BigDecimal unRealised;

	public void addRealised(String date, BigDecimal pnl) {
		this.realised.put(date, pnl);
	}

	public void setUnRealised(BigDecimal unRealised) {
		this.unRealised = unRealised;
	}

	public Map<String, BigDecimal> getRealised() {
		return realised;
	}

	public BigDecimal getUnRealised() {
		return unRealised;
	}

}
