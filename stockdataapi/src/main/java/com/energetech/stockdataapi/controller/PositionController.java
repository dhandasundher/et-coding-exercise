package com.energetech.stockdataapi.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.energetech.stockdataapi.model.TraderPositionDetails;
import com.energetech.stockdataapi.repository.PositionRepository;

@RestController
@RequestMapping("/position")
public class PositionController {

	@Autowired
	PositionRepository repo;
	
	public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm:ss");
	
	@GetMapping
	public TraderPositionDetails getTraderPosition(@RequestParam String trader, @RequestParam(required = false) String ts) {
		LocalDateTime asOf = null;
		if (ts != null) asOf = LocalDateTime.parse(ts, DATE_FORMATTER);
		List<Document> list = repo.getTraderPosition(trader, asOf);
		return getTraderPositionDetails(list, trader, asOf);
	}
	
	@GetMapping("/portfolio")
	public TraderPositionDetails getTraderPositionForPortfolio(@RequestParam String traders, @RequestParam(required = false) String ts) {
		LocalDateTime asOf = null;
		if (ts != null) asOf = LocalDateTime.parse(ts, DATE_FORMATTER);
		List<Document> list = repo.getTraderPositionForPortfolio(Arrays.asList(traders.split(",")), asOf);
		return getTraderPositionDetails(list, traders, asOf);
	}

	private TraderPositionDetails getTraderPositionDetails(List<Document> list, String trader, LocalDateTime asOf) {
		TraderPositionDetails tpd = new TraderPositionDetails(trader, asOf);
		list.forEach(doc -> {
			System.out.println(doc);
			List<Document> records = doc.getList("records", Document.class);
			if (records != null && records.size() > 0) {
				Document d = records.get(0);
				String comp = d.getString("company");
				Integer pos = d.getInteger("position");
				tpd.addCompanyPosition(comp, pos);
			}
		});
		return tpd;
	}
}
