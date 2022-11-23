package com.energetech.stockdataapi.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.energetech.stockdataapi.model.PNLDetails;
import com.energetech.stockdataapi.repository.PNLRepository;

@RestController
@RequestMapping("/pnl")
public class PNLController {

	@Autowired
	PNLRepository repo;
	
	public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm:ss");
	
	@GetMapping
	public PNLDetails getTraderPNL(@RequestParam String trader, @RequestParam(required = false) String ts) {
		LocalDateTime asOf = null;
		if (ts != null) asOf = LocalDateTime.parse(ts, DATE_FORMATTER);
		List<Document> realised = repo.getTraderRealisedPNL(trader, asOf);
		List<Document> unRealised = repo.getTraderUnRealisedPNL(trader, asOf);
		return getPNLDetails(realised, unRealised);
	}

	private PNLDetails getPNLDetails(List<Document> realised, List<Document> unRealised) {
		PNLDetails details = new PNLDetails();
		if (unRealised != null && unRealised.size() > 0) {
			details.setUnRealised(((Decimal128)unRealised.get(0).get("pnl")).bigDecimalValue().setScale(4));
		}
		if (realised != null && realised.size() > 0) {
			realised.forEach(doc -> {
				System.out.println(doc);
				Document id = doc.get("_id", Document.class);
				String date = id.getString("year") + "-" + id.getString("month");
				BigDecimal pnl = ((Decimal128)doc.get("pnl")).bigDecimalValue().setScale(4);
				details.addRealised(date, pnl);
			});
		}
		return details;
	}
}
