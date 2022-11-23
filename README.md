# et-coding-exercise

Assumption:
1. Start of Trading is 9 am.
2. Close of Trading is 4 pm.
3. Filing Date is the position closing date. So the position of the trader is calculated as the open position at the close (4 pm) of trading day. And the position would be considered closed at the filing date time.
4. PnL is calculated as below.
	Realised PnL @ Filing Date (if time is after 4 pm) = (Price at Filing Date Close * Quantity) - (Price * Quantity)
	Realised PnL @ Filing Date (if time is before 4 pm) = (Price at Filing Date Open * Quantity) - (Price * Quantity)
	UnRealised PnL @ Trading Date Close = (Price at Trading Date Close * Quantity) - (Price * Quantity)


This project is split into 2 modules.


1. Stock Data Process: This module processes the file and calculates the Position and PnL for the trader and stores the information into separate collections in Mongo DB.
	Each record is treated as a trade data event. And this event is passed to 2 Event Handlers. One is the PositionHandler and the other is PNLHandler.
	PositionHandler:
		This handler inserts into "trader_position" collection.
		Below is a sample record.
		{
  			"_id": {
			    "$oid": "637de7f0abf99f2469a0fc6b"
			},
			"trader_name": "Cirne Lewis",
			"company": "0 Relic Inc.",
			"as_of": ISODate("2020-09-01T16:00:00.000+00:00")
			},
			"position": -35500
		}
		Each record in the file will be converted into 2 position records. 
		Record 1: Open position at the end of trading day. This will be same as the quantity.
		Record 2: Closed Position at the filing date time. This will be 0.
	PNLHandler:
		This handler inserts into "trader_pnl" collection.
		Below is a sample record.
		{
			"_id": {
				"$oid": "637de7f1abf99f2469a10053"
			},
			"trader_name": "Cirne Lewis",
			"as_of": ISODate("2020-09-01T16:00:00.000+00:00"),
			"pnl": -2129.9645,
			"realised": false
		}
		Each record in the file will be converted into 2 PnL records.
		Record 1: Realised PnL, calculated based on the price @ filing date open/close.
		Record 2: UnRealised PnL, calculated based on the price @ trade date close.

2. Stock Data API: This module exposes REST endpoints for accessing the Position and PnL for the traders.