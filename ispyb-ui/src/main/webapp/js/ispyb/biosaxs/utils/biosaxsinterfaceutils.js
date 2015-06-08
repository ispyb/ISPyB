var BUI = {
	//interval : 60000,
		interval : 40000,
	rainbow : function(numOfSteps, step) {
		// This function generates vibrant, "evenly spaced" colours (i.e. no clustering). This is ideal for creating easily distinguishable vibrant markers in Google Maps and other apps.
		// Adam Cole, 2011-Sept-14
		// HSV to RBG adapted from: http://mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript
		var r, g, b;
		var h = step / numOfSteps;
		var i = ~~(h * 6);
		var f = h * 6 - i;
		var q = 1 - f;
		switch (i % 6) {
			case 0:
				r = 1, g = f, b = 0;
				break;
			case 1:
				r = q, g = 1, b = 0;
				break;
			case 2:
				r = 0, g = 1, b = f;
				break;
			case 3:
				r = 0, g = q, b = 1;
				break;
			case 4:
				r = f, g = 0, b = 1;
				break;
			case 5:
				r = 1, g = 0, b = q;
				break;
		}
		var c = "#" + ("00" + (~~(r * 255)).toString(16)).slice(-2) + ("00" + (~~(g * 255)).toString(16)).slice(-2)
				+ ("00" + (~~(b * 255)).toString(16)).slice(-2);
		return (c);
	},
	getFileNameByPath : function(filePath) {
		if (filePath != null){
			var split = filePath.split("/");
			if (split.length > 0){
				return split[split.length - 1];
			}
		}
		return "Not file";
	},
	getUpdateInterval : function() {
		this.interval = this.interval + 2000;
		return this.interval;
	},
	getRadiationDamageThreshold : function() {
		return 0.7;
	},
	getQualityThreshold : function() {
		return 0.7;
	},
	getCreateShipmentURL : function() {
		return '/ispyb/user/viewProjectList.do?reqCode=display&menu=create_shipment';
	},
	getCreateShipmentList : function() {
		return '/ispyb/user/viewProjectList.do?reqCode=display&menu=list_shipment';
	},
	getShippingURL : function(shippingId) {
		return '/ispyb/user/viewProjectList.do?reqCode=display&menu=shipment&shippingId=' + shippingId;
	},

	getMacromoleculeResultsURLByMultipleSearch : function(array) {
		return '/ispyb/user/viewProjectList.do?reqCode=display&menu=macromolecule&search='
				+ JSON.stringify(array).replace(new RegExp("\"", 'g'), "'");
	},
	getMacromoleculeResultsURL : function(macromoleculeId) {
		return '/ispyb/user/viewProjectList.do?reqCode=display&menu=macromolecule&macromoleculeId=' + macromoleculeId;
	},
	getMacromoleculeBufferResultsURL : function(macromoleculeId, bufferId) {
		return '/ispyb/user/viewProjectList.do?reqCode=display&menu=macromolecule&bufferId=' + bufferId + '&macromoleculeId=' + macromoleculeId;
	},

	getMacromoleculeHeader : function(macromoleculeId) {

		function getHTMLSource(macromoleculeId) {
			if (macromoleculeId != null) {
				var html = BUI.createFormLabel("Name :", BIOSAXS.proposal.getMacromoleculeById(macromoleculeId).name, 75, 400);
				html = html + BUI.createFormLabel("Acronym :", BIOSAXS.proposal.getMacromoleculeById(macromoleculeId).acronym, 75, 400);
				if (BIOSAXS.proposal.getMacromoleculeById(macromoleculeId).comments != null) {
					html = html + BUI.createFormLabel("Comments :", BIOSAXS.proposal.getMacromoleculeById(macromoleculeId).comments, 75, 400);
				}
				return html;
			}
		}

		return Ext.create('Ext.container.Container', {
			frame : false,
			layout : 'hbox',
			title : 'Macromolecule',
			bodyPadding : '5',
			width : 890,
			margin : '0 0 10 0',
			height : 100,
			style : {
				borderColor : '#BDBDBD',
				borderStyle : 'solid',
				borderWidth : '1px'
			},
			fieldDefaults : {
				msgTarget : 'side',
				labelWidth : 100
			},
			items : [ {
				margin : "10 0 0 10",
				width : 475,
				border : 0,
				html : getHTMLSource(macromoleculeId)
			}, {
				margin : "10 0 0 10",
				width : 475,
				border : 0,
				html : BUI.getZipHTMLByMacromoleculeId(macromoleculeId)
			}
			]
		});
	},

	getZipHTMLByMacromoleculeId : function(macromoleculeId) {
		if (macromoleculeId != null) {
			var fileName = BIOSAXS.proposal.getMacromoleculeById(macromoleculeId).acronym;
			return "<div><a style='color:blue;' href='/ispyb/user/dataadapter.do?reqCode=getZipFileByMacromoleculeId&fileName=" + fileName
					+ "&macromoleculeId=" + macromoleculeId + "'><img src='../images/download.png' /> Download</a></div>";
		}
	},
	getZipHTMLByExperimentId : function(experimentId, filename) {
		if (filename == null){
			filename = "experiment";
		}
		return "<div><a style='color:blue;' href='/ispyb/user/dataadapter.do?reqCode=getZipFileByExperimentId&fileName=" + filename
				+ "&experimentId=" + experimentId + "'><img src='../images/download.png' /> Download</a></div>";
	},
	
	getZipURLByAverageId : function(averageId, filename) {
		if (filename == null){
			filename = "experiment";
		}
		return "/ispyb/user/dataadapter.do?reqCode=getZipFileByAverageListId&f&mergeIdsList=" + averageId + "&fileName=" + filename;
	},
	getZipURLBySubtractionId : function(subtractionId, filename) {
		if (filename == null){
			filename = "experiment";
		}
		return "/ispyb/user/dataadapter.do?reqCode=getZipFileByAverageListId&f&subtractionIdList=" + subtractionId + "&fileName=" + filename;
	},
	
	
	getZipHTMLByFrameRangeId : function(experimentId, start, end) {
		var fileName = "experiment";
		return "<div><a style='color:blue;' href='/ispyb/user/dataadapter.do?reqCode=getZipFileH5ByFramesRange&f&experimentId=" + experimentId + 
				"&start=" + Number(start) +"&end="+ Number(end)+"'><img src='../images/download.png' /> Download</a></div>";
	},
	getZipFrameHPLCUrl : function(experimentId, start, end) {
		return "/ispyb/user/dataadapter.do?reqCode=getZipFileH5ByFramesRange&f&experimentId=" + experimentId + "&start=" + Number(start) +"&end="+ Number(end);
	},
	
	getQueueUrl : function() {
		return "/ispyb/user/dataadapter.do?reqCode=getPagingCompactAnalysisByProposalId";
	},
	
	getQueueUrlByExperiment: function(experimentId) {
		return "/ispyb/user/dataadapter.do?reqCode=getPagingCompactAnalysisByExperimentId&f&experimentId=" + experimentId;
	},
	getStandardDeviation : function(values) {
		var sum = 0;
		var count = 0;
		var avg = null;

		var curatedValues = new Array();
		for ( var i = 0; i < values.length; i++) {
			var value = values[i];
			if (value != null) {
				if (!isNaN(value)) {
					count = count + 1;
					sum = sum + Number(value);
					curatedValues.push(Number(value));
				}
			}
		}

		if (count > 0) {
			avg = sum / count;
		} else {
			avg = sum;
		}
		var aux = 0;
		for ( var i = 0; i < curatedValues.length; i++) {
			var value = curatedValues[i];
			aux = aux + Math.pow(value - avg, 2);
		}
		/** std **/
		var std = Math.sqrt(aux / count);
		return {
			std : (std),
			sum : (sum),
			avg : (avg),
			validNumber : count,
			totalNumber : values.length,
			values : values
		};
	},
	
	getHTMLTableForFrameAveraged : function(bufferAcronym, macromoleculeAcronym, bbmerges, molmerges, bamerges, totalframes, bufferId,macromoleculeId, macromoleculeColor) {
		
		function getFrameSpan(framesMerged, total) {
			return "<td style='font:normal 9px tahoma,arial,verdana,sans-serif;color:" + "black" + "'>(" + framesMerged + " of " + total + ")</td>";
		}

		function getColorFrame(framesMerged, total) {
			if (framesMerged / total < 0.5) {
				return "#FA5858";
			}
			if ((framesMerged / total >= 0.5) && (framesMerged / total < 0.8)) {
				return "#FF9900";
			}
			return "white";
		}

		function getRow(color, acroynm, framesMerged, totalframes) {
			return 	"<tr style='background-color:" 
					+ getColorFrame(framesMerged, totalframes)
					+ ";height:12px;padding:1px;'><td style=' width:10px; height:10px;'> " 
					+ BUI.getRectangleColorDIV(color, 10, 10)
					+ "</td> <td style='padding:5px;'> " + acroynm + "</td>" 
					+ getFrameSpan(framesMerged, totalframes) + "</tr>";
		}

		var html = "<table style='margin: 1,1,1,1;width:100%;font:normal 10px tahoma,arial,verdana,sans-serif;'>";

		/** Buffer Before **/
		if (bufferAcronym != null) {
			if (bbmerges != null) {
				color = BIOSAXS.proposal.bufferColors[bufferId];
				html = html + getRow(color, bufferAcronym, bbmerges, totalframes);
			}
		}

		/** Molecule **/
		if (macromoleculeAcronym != null) {
			if (molmerges != null) {
				if (macromoleculeColor == null){
					color = BIOSAXS.proposal.macromoleculeColors[macromoleculeId];
				}
				else{
					color = macromoleculeColor; //BIOSAXS.proposal.macromoleculeColors[macromoleculeId];
				}
				html = html + getRow(color, macromoleculeAcronym, molmerges, totalframes);
			}
		}

		/** Buffer After **/
		if (bufferAcronym != null) {
			if (bamerges != null) {
				color = BIOSAXS.proposal.bufferColors[bufferId];
				html = html + getRow(color, bufferAcronym, bamerges, totalframes);
			}
		}
		return html + "</table>";
	},
	isWebGLEnabled : function(return_context) {
		if (!!window.WebGLRenderingContext) {
			var canvas = document.createElement("canvas");
			names = [ "webgl", "experimental-webgl", "moz-webgl", "webkit-3d" ];
			context = false;
			for ( var i = 0; i < 4; i++) {
				try {
					context = canvas.getContext(names[i]);
					if (context && typeof context.getParameter == "function") {
						// WebGL is enabled                    
						if (return_context) {
							// return WebGL object if the function's argument is present
							return {
								name : names[i],
								gl : context
							};
						}
						// else, return just true
						return true;
					}
				} catch (e) {
				}
			}
			// WebGL is supported, but disabled
			return false;
		}
		// WebGL not supported28.    
		return false;
	},
	getHTMLTableForPrefixes : function(bufferBeforeaverageFilePath, averageFilePath, bufferAfterAverageFilePath) {

		function getRow(bufferBeforeaverageFilePath) {
			file = bufferBeforeaverageFilePath;
			try {
				file = bufferBeforeaverageFilePath.split("/")[bufferBeforeaverageFilePath.split("/").length - 1];
			} catch (e) {
				file = "NA";
			}
			return "<tr><td  style='height:12px;padding:5px;'>" + file + "</td></tr>";
		}
		var html = "<table style='margin: 1,1,1,1;width:100%;font:normal 10px tahoma,arial,verdana,sans-serif;'>";

		/** Buffer Before **/
		if (bufferBeforeaverageFilePath != null) {
			html = html + getRow(bufferBeforeaverageFilePath);
		}

		/** Molecule **/
		if (averageFilePath != null) {
			html = html + getRow(averageFilePath);
		}

		/** Buffer After **/
		if (bufferAfterAverageFilePath != null) {
			html = html + getRow(bufferAfterAverageFilePath);
		}
		return html + "</table>";
	},

	getBaseURL : function() {
		return '/ispyb/user/dataadapter.do';
	},

	getPrintcomponentURL : function(dewarId) {
		return '/ispyb/user/viewDewarAction.do?reqCode=generateLabels&dewarId=' + dewarId;

	},
	getPDBVisualizerURL : function(modelId, subtractionId, experimentId) {
		return '/ispyb/user/viewProjectList.do?reqCode=display&menu=PDBVisualizer&modelId=' + modelId + '&experimentId=' + experimentId
				+ '&subtractionId=' + subtractionId;
	},

	getURL : function() {
		return this.getBaseURL() + '?reqCode=getImage';

	},
	getAbinitioImageURL : function() {
		return this.getBaseURL() + '?reqCode=getAbinitioImage';
	},
	getNSDImageURL : function(modelListId) {
		return BUI.getAbinitioImageURL() + '&type=NSD&modelListId=' + modelListId;
	},
	getCHI2ImageURL : function(modelListId) {
		return BUI.getAbinitioImageURL() + '&type=CHI2&modelListId=' + modelListId;
	},
	getModelFile : function(type, modelId, format) {
		return this.getBaseURL() + '?reqCode=getModelFile' + "&type=" + type + "&modelId=" + modelId + "&format=" + format;
	},
	getPdbURL : function() {
		return this.getBaseURL() + '?reqCode=getPdbFiles';
	},
	getStvArray : function(value, error) {
		value = Number(value);
		error = Number(error);
		return [ value - error, value, value + error ];
	},
	getPointArrayForDygraph : function(x, y, error) {
		return [ x, BUI.getStvArray(y, error) ];
	},
	createDIV : function(text, width, className, backgroundColor) {
		var nameContainer = document.createElement("div");
		var nameSpan = document.createElement("span");
		if (className != null) {
			nameSpan.setAttribute("class", className);
		}
		if (backgroundColor != null) {
			nameSpan.setAttribute("style", "float:left;width:" + width + "px;height:18px;background-color:" + backgroundColor);
		} else {
			nameSpan.setAttribute("style", "float:left;width:" + width + "px;height:18px;");
		}
		nameSpan.appendChild(document.createTextNode(text));
		nameContainer.appendChild(nameSpan);
		return nameContainer;
	},

	createFormLabel : function(labelText, text, labelWidth, textWidth, backgroundColor) {
		var div = document.createElement("div");

		div.appendChild(BUI.createDIV(labelText, labelWidth, "bLabel", backgroundColor));
		div.appendChild(BUI.createDIV(text, textWidth, "btext", backgroundColor));
		return div.innerHTML;
	},

	createTextArea : function(labelText, text, labelWidth, rows, cols) {
		var div = document.createElement("div");
		div.appendChild(BUI.createDIV(labelText, labelWidth, "bLabel"));
		var textArea = document.createElement("textarea");
		textArea.setAttribute("rows", rows);
		textArea.setAttribute("cols", cols);
		textArea.appendChild(document.createTextNode(text));
		div.appendChild(textArea);
		return div.innerHTML;
	},

	showBetaWarning : function() {
		alert("ISPyB for Biosaxs version Beta has not this functionality enabled");
	},

	formatValuesErrorUnitsScientificFormat : function(val, error, unit, args) {
		var fontSize = 14;
		var decimals = 2;
		var errorFontSize = 10;
		/** line break **/
		var lineBreak = true;
		if (args != null) {
			if (args.fontSize != null) {
				fontSize = args.fontSize;
			}
			if (args.decimals != null) {
				decimals = args.decimals;
			}
			if (args.errorFontSize != null) {
				errorFontSize = args.errorFontSize;
			}
			if (args.lineBreak != null) {
				lineBreak = args.lineBreak;
			}

		}

		if (val == "") {
			return "";
		}
		if (error == null) {
			return "<span style='font-size:" + fontSize + "px'>" + Number(val).toFixed(decimals) + "</span>";
		}
		var html = "<span style='font-size:" + fontSize + "px'>" + Number(val).toFixed(decimals) + "<span style='font-size:" + errorFontSize
				+ "px;color:gray'>  " + unit + "</span></span>";
		if (lineBreak) {
			html = html + "<br/>";
		}
		return html + "<span style='font-size:" + errorFontSize + "px'> &#177; " + Number(Number(error).toFixed(3)).toExponential()
				+ "</span></span>";
	},

	formatValuesErrorUnits : function(val, error, unit, args) {
		var fontSize = 16;
		var decimals = 2;
		var errorFontSize = 10;
		/** line break **/
		var lineBreak = true;
		if (args != null) {
			if (args.fontSize != null) {
				fontSize = args.fontSize;
			}
			if (args.decimals != null) {
				decimals = args.decimals;
			}
			if (args.errorFontSize != null) {
				errorFontSize = args.errorFontSize;
			}
			if (args.lineBreak != null) {
				lineBreak = args.lineBreak;
			}

		}

		if (val == "") {
			return "";
		}
		if (error == null) {
			return "<span style='font-size:" + fontSize + "px'>" + Number(val).toFixed(decimals) + "</span>";
		}
		var html = "<span style='font-size:" + fontSize + "px'>" + Number(val).toFixed(decimals) + "<span style='font-size:" + errorFontSize
				+ "px;color:gray'>  " + unit + "</span></span>";
		if (lineBreak) {
			html = html + "<br/>";
		}
		return html + "<span style='font-size:" + errorFontSize + "px'> &#177; " + Number(Number(error).toFixed(8)).toExponential()
				+ "</span></span>";
	},

	formatValuesUnits : function(val, unit, args) {
		var fontSize = 12;
		var decimals = 2;
		var unitsFontSize = 10;
		if (args != null) {
			if (args.fontSize != null) {
				fontSize = args.fontSize;
			}
			if (args.decimals != null) {
				decimals = args.decimals;
			}
			if (args.unitsFontSize != null) {
				unitsFontSize = args.unitsFontSize;
			}

		}

		if (val === "") {
			return "";
		}
		if (unit == null) {
			return "<span style='font-size:" + fontSize + "px'>" + Number(val).toFixed(decimals) + "</span>";
		}
		return "<span style='font-size:" + fontSize + "px'>" + Number(val).toFixed(decimals) + " </span><span style='font-size:" + unitsFontSize
				+ "px;color:gray'>  " + unit + "</span></span>";
	},

	getGreenButton : function(text, args) {
		var width = 70;
		var height = 20;
		if (args != null) {
			if (args.width != null) {
				width = args.width;
			}
			if (args.height != null) {
				height = args.height;
			}
		}

		return '<input type="button" name="btn" style= "font-size:9px;width:' + width + 'px; height:' + height + 'px" class="btn-green" value="' + text + '"/>';
	},
//	getBlueButton : function(text, args) {
//			var width = 70;
//			var height = 20;
//			if (args != null) {
//				if (args.width != null) {
//					width = args.width;
//				}
//				if (args.height != null) {
//					height = args.height;
//				}
//			}
//
//			return '<input type="button" name="btn" style= "font-size:9px;width:' + width + 'px; height:' + height + 'px" class="btn-blue" value="' + text + '"/>';
//	},

	getBlueButton : function(text, args) {
		var width = 70;
		var height = 20;
		var href = null;
		if (args != null) {
			if (args.width != null) {
				width = args.width;
			}
			if (args.height != null) {
				height = args.height;
			}
			if (args.href != null) {
				href = args.href;
			}
		}
		if (href != null) {
			return '<a href=' + href + '><input type="button"  name="btn" style= "width:' + width + 'px; height:' + height
					+ 'px" class="btn-blue" value="' + text + '"></input></a>';
		} else {
			return '<input type="button" name="btn" style= "font-size:9px;width:' + width + 'px; height:' + height + 'px" class="btn-blue" value="' + text + '"/>';
//			return '<a><input type="button"  name="btn" style= "width:' + width + 'px; height:' + height + 'px" class="btn-blue" value="' + text
//					+ '"></input></a>';
		}
	},
	
	getSubmitGreenButton : function(text) {
		return '<input type="submit" type="button" name="btn" class="btn-green" value="' + text + '"/>';
	},

	getRedButton : function(text) {
		return '<input type="button"  name="btn" class="btn-red" value="' + text + '" />';

	},
	getRectangleColorDIV : function(color, height, width) {
		return '<div style="border: 1px solid gray;background-color: ' + color + '; height:' + height + 'px;width:' + width + 'px" ></div>';
	},

	openBufferWindow : function(bufferId) {
		var window = new BufferWindow();
		window.draw(tabs.experiment.getBufferById(bufferId), tabs.experiment);
	},

	/** Render for safety levels on grids **/
	safetyRenderer : function(val, y, specimen) {
		var color = val;
		if (val == "YELLOW") {
			color = "#E9AB17";
		}
		return '<span style="color:' + color + ';">' + val + '</span>';
	},

	getSampleColor : function() {
		return '#CB181D';
	},

	getLightSampleColor : function() {
		return '#FCBBA1';
	},

	getBufferColor : function() {
		return '#6A51A3';
	},
	getLightBufferColor : function() {
		return '#BCBDDC';
	},

	formatConcentration : function(val) {
		if (val != null) {
			return "<span style='font-size:16px'>" + Number(val).toFixed(2) + "</span><span style='font-size:9px'> mg/ml </span>";
		}
		return val;
	},

	formatVolume : function(sample, volume) {
		if (Number(sample.data.volumeToLoad) > Number(sample.data.volume)) {
			return "<span style='color:red;font-weight:bold;'>" + volume + "</span><span style='font-size:9;color:red;'> �l</span>";
		}
		if (Number(sample.data.volumeToLoad) == Number(sample.data.volume)) {
			return "<span style='color:orange;font-weight:bold;'>" + volume + "</span><span style='font-size:9;color:orange;'> �l</span>";
		}
		return "<span>" + volume + "</span><span style='font-size:9'> �l</span>";
	},

	getProposal : function() {
		return new Proposal();
	},

	getSampleNameRenderer : function(val, y, record) {
		var sample = record.data;
		if (record.raw.macromolecule3VO == null) {
			return '<span style="color:blue;">' + sample.code + '</span>';
		} else {
			return '<span style="color:green;">' + sample.code + '</span>';
		}
	},

	getSafetyLevels : function() {
		var safetyLevels = new Array();
		safetyLevels.push({
			safetyLevelId : "",
			name : "UNKNOWN"
		});
		safetyLevels.push({
			safetyLevelId : 1,
			name : "GREEN"
		});
		safetyLevels.push({
			safetyLevelId : 2,
			name : "YELLOW"
		});
		safetyLevels.push({
			safetyLevelId : 3,
			name : "RED"
		});
		return safetyLevels;
	},

	getErrorColor : function() {
		return '#F6CED8';
	},
	getWarningColor : function() {
		return '#F5DA81';
	},
	getValidColor : function() {
		return '#E0F8E0';
	},
	getSamplePlateLetters : function() {
		return [ "A", "B", "C", "D", "E", "F", "G", "H" ];
	},
	getSamplePositionHTML : function(sample, experiment) {
		var plate = "";
		var row = "";
		var column = "";
		var rows = this.getSamplePlateLetters();
		if (sample.sampleplateposition3VO != null) {
			var samplePlate = experiment.getSamplePlateById(sample.sampleplateposition3VO.samplePlateId);
			if (samplePlate != null) {
				plate = (samplePlate.slotPositionColumn);
				row = (sample.sampleplateposition3VO.rowNumber);
				column = (sample.sampleplateposition3VO.columnNumber);
				//				   var html = "<span style='font-weight:italic'>Plate: </span>" + "<span style='font-weight:bold'>" + plate + "</span>";
				//				   html = html +  "<span style='font-weight:italic'>, Row: </span>" + "<span style='font-weight:bold'>" +  rows[row - 1] + "</span>";
				//				   html = html +  "<span style='font-weight:italic'>, Column: </span>" + "<span style='font-weight:bold'>" + column + "</span>";
				var html = "<span style='font-weight:italic'>Plate: </span><span style='font-weight:bold'>" + plate
						+ "</span>-<span style='font-weight:bold'>" + rows[row - 1] + "</span><span style='font-weight:bold'>" + column + "</span>";
				return html;
			}
		}
		return "";
	},

	getSafetyLabelName : function(safetyLevelId) {
		var safetyLevels = BUI.getSafetyLevels();
		for ( var i = 0; i < safetyLevels.length; i++) {
			if (safetyLevels[i].safetyLevelId == safetyLevelId) {
				return safetyLevels[i].name;
			}
		}
		return "UNKNOWN";
	},
	/** generate random id **/
	id : function() {
		var text = "";
		var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		for ( var i = 0; i < 5; i++)
			text += possible.charAt(Math.floor(Math.random() * possible.length));

		return text;
	},
	showWarning : function(message) {
		Ext.Msg.show({
			title : 'Warning',
			msg : message,
			icon : Ext.Msg.WARNING,
			animEl : 'elId'
		});
	},
	showError : function(message) {
		Ext.Msg.show({
			title : 'Warning',
			msg : message,
			icon : Ext.Msg.ERROR,
			animEl : 'elId'
		});
	},
	getTipHTML : function(message) {
		//return "<div  class='panelMacro' ><table class='tipMacro'><colgroup span='1'><col span='1' width='24'><col span='1'></colgroup><tbody><tr><td colspan='1' rowspan='1' valign='top'><img align='middle' src='https://cwiki.apache.org/confluence/images/emoticons/check.gif' width='16' height='16' alt='' border='0'></td><td colspan='1' rowspan='1'><b>Tip</b><br clear='none'>"
		//		+ message + "</td></tr></tbody></table></div>";
		return "<div  class='panelMacro' ><table class='tipMacro'><colgroup span='1'><col span='1' width='24'><col span='1'></colgroup><tbody><tr><td colspan='1' rowspan='1' valign='top'></td><td colspan='1' rowspan='1'><b>Tip</b><br clear='none'>"
		+ message + "</td></tr></tbody></table></div>";
	},

	getWarningHTML : function(message) {
		return "<div class='panelMacro' ><table class='warningMacro'><colgroup span='1'><col span='1' width='24'><col span='1'></colgroup><tbody><tr><td colspan='1' rowspan='1' valign='top'></td><td colspan='1' rowspan='1'><b>Warning</b><br clear='none'>"
				+ message + "</td></tr></tbody></table></div>";
	},

	getErrorHTML : function(message) {
		return "<div class='panelMacro' ><table class='errorMacro'><colgroup span='1'><col span='1' width='24'><col span='1'></colgroup><tbody><tr><td colspan='1' rowspan='1' valign='top'><img align='middle' src='https://cwiki.apache.org/confluence/images/emoticons/forbidden.gif' width='16' height='16' alt='' border='0'></td><td colspan='1' rowspan='1'><b>Error</b><br clear='none'>"
				+ message + "</td></tr></tbody></table></div>";
	},

	getProgessBar : function(percentage, text) {
		/** percentage 100% green **/
		var color = "#0a0";

		color = "#99CC00";
		if (percentage > 100) {
			color = "yellow";
			percentage = 100;
		}
		if (isNaN(percentage)) {
			color = "white";
			percentage = 0;
		}

		var defaultText = percentage + "%";
		if (text != null) {
			defaultText = text;
		}

		return "<div class='meter-wrap'><div class='meter-value' style='background-color: " + color + " ; width: " + percentage
				+ "%;'><div class='meter-text'>" + defaultText + "</div></div></div>";
	},
	getFileName : function(filePath){
		if (filePath != null){
			return filePath.split("/")[filePath.split("/").length - 1]
		}
		return "";
	}

};

Ext.ux.form.RequiredCombo = Ext.extend(Ext.form.ComboBox, {
	config : {
		cls : 'custom-field-text-required'
	},
	initComponent : function() {
		Ext.ux.form.RequiredCombo.superclass.initComponent.apply(this, arguments);
	},
	checkChange : function() {
		if ((this.getValue() == null) || String(this.getValue()).length == 0) {
			this.addCls('custom-field-text-required');
		} else {
			this.removeCls('custom-field-text-required');
		}
	}
});

Ext.define('Ext.form.field.RequiredNumber', {
	extend : 'Ext.form.field.Number',
	alias : 'widget.requirednumberfield',
	alternateClassName : [ 'Ext.form.RequiredNumberField', 'Ext.form.RequiredNumber' ],
	config : {
		cls : 'custom-field-text-required'
	},

	initComponent : function() {
		var me = this;
		me.callParent();
		me.setMinValue(me.minValue);
		me.setMaxValue(me.maxValue);
	},

	onChange : function() {
		if ((this.getValue() == null) || String(this.getValue()).length == 0) {
			this.addCls('custom-field-text-required');
		} else {
			this.removeCls('custom-field-text-required');
		}
		this.toggleSpinners();
		this.callParent(arguments);
	}
});

Ext.define('Ext.form.field.RequiredText', {
	extend : 'Ext.form.field.Text',
	alias : 'widget.requiredtext',
	requires : [ 'Ext.form.field.VTypes', 'Ext.layout.component.field.Text' ],
	alternateClassName : [ 'Ext.form.RequiredTextField', 'Ext.form.RequiredText' ],
	config : {
		cls : 'custom-field-text-required'
	},
	initComponent : function() {
		var me = this;
		if (me.allowOnlyWhitespace === false) {
			me.allowBlank = false;
		}
		me.callParent();
		me.addEvents(
		/**
		 * @event autosize
		 * Fires when the **{@link #autoSize}** function is triggered and the field is resized according to the
		 * {@link #grow}/{@link #growMin}/{@link #growMax} configs as a result. This event provides a hook for the
		 * developer to apply additional logic at runtime to resize the field if needed.
		 * @param {Ext.form.field.Text} this This text field
		 * @param {Number} width The new field width
		 */
		'autosize',

		/**
		 * @event keydown
		 * Keydown input field event. This event only fires if **{@link #enableKeyEvents}** is set to true.
		 * @param {Ext.form.field.Text} this This text field
		 * @param {Ext.EventObject} e
		 */
		'keydown',
		/**
		 * @event keyup
		 * Keyup input field event. This event only fires if **{@link #enableKeyEvents}** is set to true.
		 * @param {Ext.form.field.Text} this This text field
		 * @param {Ext.EventObject} e
		 */
		'keyup',
		/**
		 * @event keypress
		 * Keypress input field event. This event only fires if **{@link #enableKeyEvents}** is set to true.
		 * @param {Ext.form.field.Text} this This text field
		 * @param {Ext.EventObject} e
		 */
		'keypress');
		me.addStateEvents('change');
		me.setGrowSizePolicy();
	},
	checkChange : function() {
		if ((this.getValue() == null) || String(this.getValue()).length == 0) {
			this.addCls('custom-field-text-required');
		} else {
			this.removeCls('custom-field-text-required');
		}
	}
});

var BIOSAXS_COMBOMANAGER = {

	getComboMacromoleculeByMacromolecules : function(macromolecules, args) {
		var labelWidth = 150;
		var margin = "0 0 5 0";
		var width = 300;

		if (args != null) {
			if (args.labelWidth != null) {
				labelWidth = args.labelWidth;
			}
			if (args.margin != null) {
				margin = args.margin;
			}
			if (args.width != null) {
				width = args.width;
			}
		}

		var store = Ext.create('Ext.data.Store', {
			fields : [ 'macromoleculeId', 'acronym' ],
			data : macromolecules,
			sorters : [ 'acronym' ]
		});

		return Ext.create('Ext.form.ComboBox', {
			fieldLabel : 'Macromolecules',
			labelWidth : labelWidth,
			width : width,
			margin : margin,
			store : store,
			editable: false,
			queryMode : 'local',
			displayField : 'acronym',
			valueField : 'macromoleculeId'
		});
	},
	getComboBuffers : function(buffers, args) {
		var labelWidth = 150;
		var margin = "0 0 5 0";
		var width = 300;
		var fieldLabel = 'Buffer';

		if (args != null) {
			if (args.labelWidth != null) {
				labelWidth = args.labelWidth;
			}
			if (args.margin != null) {
				margin = args.margin;
			}
			if (args.width != null) {
				width = args.width;
			}
			if (args.noLabel != null) {
				fieldLabel = null;
			}
		}

		var store = Ext.create('Ext.data.Store', {
			fields : [ 'bufferId', 'acronym' ],
			data : buffers,
			sorters : [ 'acronym' ]
		});

		return Ext.create('Ext.form.ComboBox', {
			fieldLabel : fieldLabel,
			labelWidth : labelWidth,
			width : width,
			margin : margin,
			editable: false,
			store : store,
			queryMode : 'local',
			displayField : 'acronym',
			valueField : 'bufferId'
		});
	},
	getComboSessions : function(sessions, args) {
		var labelWidth = 150;
		var margin = "0 0 5 0";
		var width = 300;

		if (args != null) {
			if (args.labelWidth != null) {
				labelWidth = args.labelWidth;
			}
			if (args.margin != null) {
				margin = args.margin;
			}
			if (args.width != null) {
				width = args.width;
			}
		}

		for ( var i = 0; i < sessions.length; i++) {
			sessions[i]["startDateFormatted"] = moment(sessions[i].startDate).format("MMM Do YY");
			sessions[i]["sorter"] = moment(sessions[i].startDate).format("YYYYMMDD");
		}

		var store = Ext.create('Ext.data.Store', {
			fields : [ 'sessionId', 'startDateFormatted', 'beamlineName', 'startDate', 'endDate', 'beamlineOperator' ],
			data : sessions,
			sorters : [ 'sorter' ]
		});

		return Ext.create('Ext.form.ComboBox', {
			fieldLabel : 'Sessions',
			labelWidth : labelWidth,
			width : width,
			margin : margin,
			store : store,
			queryMode : 'local',
			//		    	    displayField			: 'startDate',
			valueField : 'sessionId',
			// Template for the dropdown menu.
			// Note the use of "x-boundlist-item" class,
			// this is required to make the items selectable.
			tpl : Ext.create('Ext.XTemplate', '<tpl for=".">',
					'<div class="x-boundlist-item">{startDateFormatted}<span style="font-weight:bold"> {beamlineName}</span></div>', '</tpl>'),
			// template for the content inside text field
			displayTpl : Ext.create('Ext.XTemplate', '<tpl for=".">', '{startDateFormatted}', '</tpl>')

		});
	}
};
