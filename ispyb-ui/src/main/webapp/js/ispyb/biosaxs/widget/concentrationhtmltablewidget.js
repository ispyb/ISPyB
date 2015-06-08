/**
 * It shows a table with the guinier and gnom data as well as passed and
 * discarded measurements
 * 
 * @height
 * @showBufferColumns
 */
function ConcentrationHTMLTableWidget(args) {
	this.id = BUI.id();

	this.showBufferColumns = true;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.showBufferColumns != null) {
			this.showBufferColumns = args.showBufferColumns;
		}
	}
}

ConcentrationHTMLTableWidget.prototype.refresh = function(parsedData) {
	document.getElementById(this.id).innerHTML = this.getPanel(parsedData);
};

ConcentrationHTMLTableWidget.prototype.getPanel = function(parsedData) {
	var html = "<div  style='overflow-y: auto;height:" + this.height + "px;'; id='" + this.id + "'>";
	html = html + "<table class='ver-minimalist'  style='width:880px;'>";

	/** Title * */
	html = html + "<tr>";
	html = html + "<th rowspan='2' >Concentration</th>";
	if (this.showBufferColumns) {
		html = html + "<th rowspan='2' >Buffer</th>";
	}
	html = html + "<th  colspan='2'>Measurements</th>";
	html = html + "<th  colspan='3'>Guinier</th>";
	html = html + "<th  colspan='3'>Gnom</th>";
	html = html + "</tr>";

	html = html + "<tr>";
	html = html + "<th>Passed</th>";
	html = html + "<th>Discarded</th>";
	html = html + "<th>Rg</th>";
	html = html + "<th>I0</th>";
	html = html + "<th>Quality</th>";
	html = html + "<th>Rg</th>";
	html = html + "<th>dMax</th>";
	html = html + "</tr>";

	parsedData.concentration.concentrations.sort(function(a, b) {
		return a.concentration - b.concentration;
	});
	/** Row * */
	for ( var i = 0; i < parsedData.concentration.concentrations.length; i++) {
		var row = parsedData[i];

		var tr = "<tr>";
		if (i % 2 == 1) {
			tr = "<tr class='ver-minimalist-odd'>";
		}
		var goodMeasurements = (parsedData.concentration.concentrations[i].frames.length - parsedData.concentration.concentrations[i].frames_warning);
		if (goodMeasurements == 0) {
			tr = "<tr class='ver-minimalist-error'>";
		}
		html = html + tr;
		html = html + "<td>" + parsedData.concentration.concentrations[i].concentration + "</td>";
		if (this.showBufferColumns) {
			html = html + "<td>" + parsedData.concentration.concentrations[i].bufferAcronym + "</td>";
		}
		html = html + "<td><span style='color:green;font-weight:bold;'>" + goodMeasurements + "</span> of " + parsedData.concentration.concentrations[i].frames.length + "</td>";
		html = html + "<td style='color:orange;font-weight:bold;'>" + parsedData.concentration.concentrations[i].frames_warning + "</td>";
		html = html + "<td>" + BUI.formatValuesErrorUnits(parsedData.concentration.concentrations[i].calculation.rgGuinier.avg, parsedData.concentration.concentrations[i].calculation.rgGuinier.std, "nm", {
			lineBreak : false
		}) + "</td>";
		html = html + "<td>" + BUI.formatValuesErrorUnits(parsedData.concentration.concentrations[i].calculation.i0Guinier.avg, parsedData.concentration.concentrations[i].calculation.i0Guinier.std, " ", {
			lineBreak : false
		}) + "</td>";
		html = html + "<td>" + BUI.formatValuesErrorUnits(parsedData.concentration.concentrations[i].calculation.quality.avg, parsedData.concentration.concentrations[i].calculation.quality.std, " ", {
			lineBreak : false
		}) + "</td>";
		html = html + "<td>" + BUI.formatValuesErrorUnits(parsedData.concentration.concentrations[i].calculation.rgGnom.avg, parsedData.concentration.concentrations[i].calculation.rgGnom.std, " ", {
			lineBreak : false
		}) + "</td>";
		html = html + "<td>" + BUI.formatValuesErrorUnits(parsedData.concentration.concentrations[i].calculation.dMax.avg, parsedData.concentration.concentrations[i].calculation.dMax.std, " ", {
			lineBreak : false
		}) + "</td>";
		html = html + "</tr>";
	}
	return html + "</table>";
};

ConcentrationHTMLTableWidget.prototype.input = function() {
	return {
		data : {
			"dataCollectionCount" : 4,
			"buffers" : [ {
				"bufferId" : "422"
			} ],
			"concentration" : {
				"concentrations" : [ {
					"concentration" : "7.17",
					"id" : "7.1699999999999999",
					"bufferId" : "422.00",
					"bufferAcronym" : "B1",
					"rgGuinier" : [ "5.12723" ],
					"frames" : [ {
						"total" : "0.515705406286",
						"bufferBeforeMeasurementId" : 15045,
						"sampleMergeId" : 8176,
						"averagedModelId" : null,
						"I0" : "183.457461646",
						"proposalCode" : "OPD",
						"averagedModel" : null,
						"bufferAfterMergeId" : 8177,
						"framesCount" : "10",
						"bufferBeforeMergeId" : 8175,
						"averageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_065_ave.dat",
						"bufferAfterMeasurementId" : 15048,
						"rgGuinier" : "5.12723",
						"subtractedFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_065_sub.dat",
						"firstPointUsed" : "17",
						"chi2RgFilePath" : null,
						"abInitioModelId" : null,
						"macromoleculeId" : 649,
						"code" : "_4.0_7.17",
						"transmission" : "100.0",
						"timeStart" : "2013-06-05 15:43:54.348723",
						"bufferAcronym" : "B1",
						"quality" : "0.853011",
						"shapeDeterminationModelId" : null,
						"expermientComments" : "[BsxCube] Generated from BsxCube",
						"bufferId" : 422,
						"isagregated" : "False",
						"dmax" : "25.63615",
						"bufferBeforeAverageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_064_ave.dat",
						"exposureTemperature" : "4.0",
						"subtractionId" : 3377,
						"conc" : "7.1699999999999999",
						"rapidShapeDeterminationModel" : null,
						"experimentCreationDate" : "Jun 5, 2013 3:30:30 PM",
						"lastPointUsed" : "36",
						"modelListId" : null,
						"framesMerge" : "9",
						"rapidShapeDeterminationModelId" : null,
						"bufferAfterAverageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_066_ave.dat",
						"nsdFilePath" : null,
						"bufferAfterFramesMerged" : "10",
						"experimentId" : 700,
						"macromoleculeAcronym" : "L9",
						"i0stdev" : "0.139364435146",
						"sampleMeasurementId" : 15047,
						"measurementComments" : "[5]",
						"priorityLevelId" : 10,
						"bufferBeforeFramesMerged" : "10",
						"substractionCreationTime" : "Jun 5, 2013 3:46:31 PM",
						"proposalNumber" : "29",
						"rgGnom" : "5.40297914939",
						"volume" : "475.302",
						"shapeDeterminationModel" : null,
						"comments" : null,
						"groupeField" : "L9 B1"
					} ],
					"frames_warning" : 0,
					"calculation" : {
						"rgGuinier" : {
							"std" : 0,
							"sum" : 5.12723,
							"avg" : 5.12723,
							"validNumber" : 1,
							"totalNumber" : 1,
							"values" : [ "5.12723" ]
						},
						"i0Guinier" : {
							"std" : 0,
							"sum" : 183.457461646,
							"avg" : 183.457461646,
							"validNumber" : 1,
							"totalNumber" : 1,
							"values" : [ "183.457461646" ]
						},
						"quality" : {
							"std" : 0,
							"sum" : 0.853011,
							"avg" : 0.853011,
							"validNumber" : 1,
							"totalNumber" : 1,
							"values" : [ "0.853011" ]
						},
						"rgGnom" : {
							"std" : 0,
							"sum" : 5.40297914939,
							"avg" : 5.40297914939,
							"validNumber" : 1,
							"totalNumber" : 1,
							"values" : [ "5.40297914939" ]
						},
						"dMax" : {
							"std" : 0,
							"sum" : 25.63615,
							"avg" : 25.63615,
							"validNumber" : 1,
							"totalNumber" : 1,
							"values" : [ "25.63615" ]
						}
					}
				}, {
					"concentration" : "3.53",
					"id" : "3.5299999999999998",
					"bufferId" : "422.00",
					"bufferAcronym" : "B1",
					"rgGuinier" : [ "4.38278" ],
					"frames" : [ {
						"total" : "0.497164741078",
						"bufferBeforeMeasurementId" : 15048,
						"sampleMergeId" : 8178,
						"averagedModelId" : null,
						"I0" : "160.023229462",
						"proposalCode" : "OPD",
						"averagedModel" : null,
						"bufferAfterMergeId" : 8179,
						"framesCount" : "10",
						"bufferBeforeMergeId" : 8177,
						"averageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_067_ave.dat",
						"bufferAfterMeasurementId" : 15051,
						"rgGuinier" : "4.38278",
						"subtractedFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_067_sub.dat",
						"firstPointUsed" : "36",
						"chi2RgFilePath" : null,
						"abInitioModelId" : null,
						"macromoleculeId" : 649,
						"code" : "_4.0_3.53",
						"transmission" : "100.0",
						"timeStart" : "2013-06-05 15:47:04.630129",
						"bufferAcronym" : "B1",
						"quality" : "0.742825",
						"shapeDeterminationModelId" : null,
						"expermientComments" : "[BsxCube] Generated from BsxCube",
						"bufferId" : 422,
						"isagregated" : "False",
						"dmax" : "15.33973",
						"bufferBeforeAverageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_066_ave.dat",
						"exposureTemperature" : "4.0",
						"subtractionId" : 3378,
						"conc" : "3.5299999999999998",
						"rapidShapeDeterminationModel" : null,
						"experimentCreationDate" : "Jun 5, 2013 3:30:30 PM",
						"lastPointUsed" : "61",
						"modelListId" : null,
						"framesMerge" : "10",
						"rapidShapeDeterminationModelId" : null,
						"bufferAfterAverageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_068_ave.dat",
						"nsdFilePath" : null,
						"bufferAfterFramesMerged" : "10",
						"experimentId" : 700,
						"macromoleculeAcronym" : "L9",
						"i0stdev" : "0.1499898017",
						"sampleMeasurementId" : 15050,
						"measurementComments" : "[6]",
						"priorityLevelId" : 12,
						"bufferBeforeFramesMerged" : "10",
						"substractionCreationTime" : "Jun 5, 2013 3:49:42 PM",
						"proposalNumber" : "29",
						"rgGnom" : "4.40615474861",
						"volume" : "372.656",
						"shapeDeterminationModel" : null,
						"comments" : null,
						"groupeField" : "L9 B1"
					} ],
					"frames_warning" : 0,
					"calculation" : {
						"rgGuinier" : {
							"std" : 0,
							"sum" : 4.38278,
							"avg" : 4.38278,
							"validNumber" : 1,
							"totalNumber" : 1,
							"values" : [ "4.38278" ]
						},
						"i0Guinier" : {
							"std" : 0,
							"sum" : 160.023229462,
							"avg" : 160.023229462,
							"validNumber" : 1,
							"totalNumber" : 1,
							"values" : [ "160.023229462" ]
						},
						"quality" : {
							"std" : 0,
							"sum" : 0.742825,
							"avg" : 0.742825,
							"validNumber" : 1,
							"totalNumber" : 1,
							"values" : [ "0.742825" ]
						},
						"rgGnom" : {
							"std" : 0,
							"sum" : 4.40615474861,
							"avg" : 4.40615474861,
							"validNumber" : 1,
							"totalNumber" : 1,
							"values" : [ "4.40615474861" ]
						},
						"dMax" : {
							"std" : 0,
							"sum" : 15.33973,
							"avg" : 15.33973,
							"validNumber" : 1,
							"totalNumber" : 1,
							"values" : [ "15.33973" ]
						}
					}
				}, {
					"concentration" : "1.75",
					"id" : "1.75",
					"bufferId" : "422.00",
					"bufferAcronym" : "B1",
					"rgGuinier" : [],
					"frames" : [ {
						"total" : "0.5538035065",
						"bufferBeforeMeasurementId" : 15051,
						"sampleMergeId" : 8180,
						"averagedModelId" : null,
						"I0" : "146.927428571",
						"proposalCode" : "OPD",
						"averagedModel" : null,
						"bufferAfterMergeId" : 8181,
						"framesCount" : "10",
						"bufferBeforeMergeId" : 8179,
						"averageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_069_ave.dat",
						"bufferAfterMeasurementId" : 15054,
						"rgGuinier" : "4.27139",
						"subtractedFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_069_sub.dat",
						"firstPointUsed" : "33",
						"chi2RgFilePath" : null,
						"abInitioModelId" : null,
						"macromoleculeId" : 649,
						"code" : "_4.0_1.75",
						"transmission" : "100.0",
						"timeStart" : "2013-06-05 15:50:09.395824",
						"bufferAcronym" : "B1",
						"quality" : "0.765999",
						"shapeDeterminationModelId" : null,
						"expermientComments" : "[BsxCube] Generated from BsxCube",
						"bufferId" : 422,
						"isagregated" : "False",
						"dmax" : "14.949865",
						"bufferBeforeAverageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_068_ave.dat",
						"exposureTemperature" : "4.0",
						"subtractionId" : 3379,
						"conc" : "1.75",
						"rapidShapeDeterminationModel" : null,
						"experimentCreationDate" : "Jun 5, 2013 3:30:30 PM",
						"lastPointUsed" : "62",
						"modelListId" : null,
						"framesMerge" : "6",
						"rapidShapeDeterminationModelId" : null,
						"bufferAfterAverageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_070_ave.dat",
						"nsdFilePath" : null,
						"bufferAfterFramesMerged" : "10",
						"experimentId" : 700,
						"macromoleculeAcronym" : "L9",
						"i0stdev" : "0.191914285714",
						"sampleMeasurementId" : 15053,
						"measurementComments" : "[7]",
						"priorityLevelId" : 14,
						"bufferBeforeFramesMerged" : "10",
						"substractionCreationTime" : "Jun 5, 2013 3:52:31 PM",
						"proposalNumber" : "29",
						"rgGnom" : "4.28379338749",
						"volume" : "356.326",
						"shapeDeterminationModel" : null,
						"comments" : null,
						"groupeField" : "L9 B1"
					} ],
					"frames_warning" : 1,
					"calculation" : {
						"rgGuinier" : {
							"std" : null,
							"sum" : 0,
							"avg" : 0,
							"validNumber" : 0,
							"totalNumber" : 0,
							"values" : []
						},
						"i0Guinier" : {
							"std" : null,
							"sum" : 0,
							"avg" : 0,
							"validNumber" : 0,
							"totalNumber" : 0,
							"values" : []
						},
						"quality" : {
							"std" : null,
							"sum" : 0,
							"avg" : 0,
							"validNumber" : 0,
							"totalNumber" : 0,
							"values" : []
						},
						"rgGnom" : {
							"std" : null,
							"sum" : 0,
							"avg" : 0,
							"validNumber" : 0,
							"totalNumber" : 0,
							"values" : []
						},
						"dMax" : {
							"std" : null,
							"sum" : 0,
							"avg" : 0,
							"validNumber" : 0,
							"totalNumber" : 0,
							"values" : []
						}
					}
				}, {
					"concentration" : "0.91",
					"id" : "0.91000000000000003",
					"bufferId" : "422.00",
					"bufferAcronym" : "B1",
					"rgGuinier" : [],
					"frames" : [ {
						"total" : null,
						"bufferBeforeMeasurementId" : 15054,
						"sampleMergeId" : 8182,
						"averagedModelId" : null,
						"I0" : "0.0",
						"proposalCode" : "OPD",
						"averagedModel" : null,
						"bufferAfterMergeId" : 8183,
						"framesCount" : "10",
						"bufferBeforeMergeId" : 8181,
						"averageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_071_ave.dat",
						"bufferAfterMeasurementId" : 15057,
						"rgGuinier" : null,
						"subtractedFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_071_sub.dat",
						"firstPointUsed" : "0",
						"chi2RgFilePath" : null,
						"abInitioModelId" : null,
						"macromoleculeId" : 649,
						"code" : "_4.0_.91",
						"transmission" : "100.0",
						"timeStart" : "2013-06-05 15:52:58.133705",
						"bufferAcronym" : "B1",
						"quality" : "0.0",
						"shapeDeterminationModelId" : null,
						"expermientComments" : "[BsxCube] Generated from BsxCube",
						"bufferId" : 422,
						"isagregated" : "False",
						"dmax" : null,
						"bufferBeforeAverageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_070_ave.dat",
						"exposureTemperature" : "4.0",
						"subtractionId" : 3380,
						"conc" : "0.91000000000000003",
						"rapidShapeDeterminationModel" : null,
						"experimentCreationDate" : "Jun 5, 2013 3:30:30 PM",
						"lastPointUsed" : "0",
						"modelListId" : null,
						"framesMerge" : "10",
						"rapidShapeDeterminationModelId" : null,
						"bufferAfterAverageFilePath" : " /data/pyarch/bm29/opd29/700/1d/data_072_ave.dat",
						"nsdFilePath" : null,
						"bufferAfterFramesMerged" : "10",
						"experimentId" : 700,
						"macromoleculeAcronym" : "L9",
						"i0stdev" : "0.0",
						"sampleMeasurementId" : 15056,
						"measurementComments" : "[8]",
						"priorityLevelId" : 16,
						"bufferBeforeFramesMerged" : "10",
						"substractionCreationTime" : "Jun 5, 2013 3:55:35 PM",
						"proposalNumber" : "29",
						"rgGnom" : null,
						"volume" : null,
						"shapeDeterminationModel" : null,
						"comments" : null,
						"groupeField" : "L9 B1"
					} ],
					"frames_warning" : 1,
					"calculation" : {
						"rgGuinier" : {
							"std" : null,
							"sum" : 0,
							"avg" : 0,
							"validNumber" : 0,
							"totalNumber" : 0,
							"values" : []
						},
						"i0Guinier" : {
							"std" : null,
							"sum" : 0,
							"avg" : 0,
							"validNumber" : 0,
							"totalNumber" : 0,
							"values" : []
						},
						"quality" : {
							"std" : null,
							"sum" : 0,
							"avg" : 0,
							"validNumber" : 0,
							"totalNumber" : 0,
							"values" : []
						},
						"rgGnom" : {
							"std" : null,
							"sum" : 0,
							"avg" : 0,
							"validNumber" : 0,
							"totalNumber" : 0,
							"values" : []
						},
						"dMax" : {
							"std" : null,
							"sum" : 0,
							"avg" : 0,
							"validNumber" : 0,
							"totalNumber" : 0,
							"values" : []
						}
					}
				} ]
			},
			"concentrationLabel" : "7.17 mg/ml ,3.53 mg/ml ,1.75 mg/ml ,0.91 mg/ml "
		}
	};
};

ConcentrationHTMLTableWidget.prototype.test = function(targetId) {
	var concentrationHTMLTableWidget = new ConcentrationHTMLTableWidget();
	document.getElementById(targetId).innerHTML = concentrationHTMLTableWidget.getPanel(concentrationHTMLTableWidget.input().data);
};
