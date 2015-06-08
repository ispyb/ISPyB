
var ISPYB_CONF = {
		load : function(config) {
			for (var key in config) {
				ISPYB_CONF[key] = config[key];
			}
		},
		SAMPLE_CHANGER_CONFIGURATION : {									
			"1": {
				"platetype3VO": {"plateTypeId": 1, "name": "Deep Well", "rowCount": 8, "columnCount": 12, "shape": "REC"},
				"name": "Deep Well",
				"slotPositionRow": "1",
				"slotPositionColumn": "1",
				"storageTemperature": "0",
				"sampleplateposition3VOs": [],
				"experimentId": 0
			}
		}
	};