package com.energetech.stockdataprocess.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;

import com.energetech.stockdataprocess.utils.Constants;

public class TradeData {

	String company;
	LocalDateTime filingDate;
	LocalDate tradeDate;
	String ticker;
	String traderName;
	String transactiontype;
	BigDecimal price;
	Integer quantity;
	BigDecimal value;
	BigDecimal fileDateOpen;
	BigDecimal fileDateClose;
	BigDecimal tradeDateOpen;
	BigDecimal tradeDateClose;

	public TradeData(String[] line) {
		this.company = line[0];
		this.filingDate = LocalDateTime.parse(line[1].strip(), Constants.FILING_DATE_FORMATTER);
		this.tradeDate = LocalDate.parse(line[2].strip(), Constants.TRADE_DATE_FORMATTER);;
		this.ticker = line[3];
		this.traderName = line[4];
		this.transactiontype = line[5];
		this.price = new BigDecimal(line[6]);
		this.quantity = Integer.valueOf(line[7]);
		this.value = new BigDecimal(line[8]);
		this.fileDateOpen = new BigDecimal(line[9]);
		this.fileDateClose = new BigDecimal(line[10]);
		this.tradeDateOpen = new BigDecimal(line[11]);
		this.tradeDateClose = new BigDecimal(line[12]);
	}

	public String getCompany() {
		return company;
	}

	public LocalDateTime getFilingDate() {
		return filingDate;
	}

	public LocalDate getTradeDate() {
		return tradeDate;
	}

	public String getTicker() {
		return ticker;
	}

	public String getTraderName() {
		return traderName;
	}

	public String getTransactiontype() {
		return transactiontype;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getValue() {
		return value;
	}

	public BigDecimal getFileDateOpen() {
		return fileDateOpen;
	}

	public BigDecimal getFileDateClose() {
		return fileDateClose;
	}

	public BigDecimal getTradeDateOpen() {
		return tradeDateOpen;
	}

	public BigDecimal getTradeDateClose() {
		return tradeDateClose;
	}

	@Override
	public String toString() {
		StandardToStringStyle style = new StandardToStringStyle();
	    style.setFieldSeparator(", ");
	    style.setUseClassName(false);
	    style.setUseIdentityHashCode(false);

	    return new ReflectionToStringBuilder(this, style).toString();
	}
}
