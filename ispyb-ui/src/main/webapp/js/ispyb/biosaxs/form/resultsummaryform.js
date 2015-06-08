function ResultSummaryForm() {
	this.radiationDamageThreshold = BUI.getRadiationDamageThreshold();
	this.qualityThreshold = BUI.getQualityThreshold();

	/** Data clusters with bufers **/
	this.clusterByBuffers = false;

	/** Visualization are groupeb by concenttrations. Ex: 4.5mg/ml and 4.7mg/ml will be clusterized together **/
	this.collapseConcentrations = true;

	this.summaryHeight = 350;
	this.id = BUI.id();

	var _this = this;

	this.qualityControlResultsWidget = new QualityControlResultsWidget({
		qualityThreshold : this.radiationDamageThreshold,
		radiationDamageThreshold : this.qualityThreshold

	});
	this.qualityControlResultsWidget.onQualityChanged.attach(function(sender, value) {
		_this.qualityThreshold = value;
		_this.thresholdsChanged();
	});

	this.qualityControlResultsWidget.onRadiationDatamageChanged.attach(function(sender, value) {
		_this.radiationDamageThreshold = value;
		_this.thresholdsChanged();

	});
	this.concentrationHTMLTableWidget = new ConcentrationHTMLTableWidget({
		height : 250,
		showBufferColumns : this.clusterByBuffers
	});

	this.plot = new BoxWhiskerGraph({
		targetId : _this.id + '_boxPlot',
		height : 350,
		width : 450,
		maxBoxWidth : 25
	});

	this.rangePlot = new RangeWhiskerGraph({
		targetId : _this.id + '_rangePlot',
		height : 350,
		width : 450,
		maxBoxWidth : 25
	});
}

ResultSummaryForm.prototype.thresholdsChanged = function() {
	var parsedData = this.prepareData(this.rawData);

	var filtered = JSON.parse(JSON.stringify(parsedData));
	filtered.concentration.concentrations = [];
	for ( var conc in parsedData.concentration.concentrations) {
		if (parsedData.concentration.concentrations[conc].calculation.rgGnom.validNumber > 0) {
			filtered.concentration.concentrations.push(parsedData.concentration.concentrations[conc]);
		}
	}

	this.plotWhisker(filtered);
	this.plotRange(filtered);

	this.concentrationHTMLTableWidget.refresh(parsedData);
};

/** Given a frame object (object returned by analyzeFrames()) and depending of the threshold indicates if a datacollection has radiation damage **/
ResultSummaryForm.prototype.hasRadiationDamage = function(frameObject) {
	var bb = frameObject.bufferBeforeFramesMerged / frameObject.framesCount;
	var ba = frameObject.bufferAfterFramesMerged / frameObject.framesCount;
	var mol = frameObject.framesMerge / frameObject.framesCount;
	return !((bb >= this.radiationDamageThreshold) && (ba >= this.radiationDamageThreshold) && (mol >= this.radiationDamageThreshold));
};

/** Return (frameObject) an object with the information about the frames for a data collection**/
ResultSummaryForm.prototype.analyzeFrames = function(dataCollectionRow) {
	return {
		bufferAfterFramesMerged : dataCollectionRow.bufferAfterFramesMerged,
		bufferBeforeFramesMerged : dataCollectionRow.bufferBeforeFramesMerged,
		framesCount : dataCollectionRow.framesCount,
		framesMerge : dataCollectionRow.framesMerge
	};
};

ResultSummaryForm.prototype.detectDataCollectionWarnings = function(dataCollectionRow) {
	var frameObject = this.analyzeFrames(dataCollectionRow);
	var warnings = {
		count : 0,
		type : []
	};

	if (this.hasRadiationDamage(frameObject)) {
		warnings.count = warnings.count + 1;
		warnings.type.push("RADIATION DAMAGE");
	}

	if (Number(dataCollectionRow.quality) < this.qualityThreshold) {
		warnings.count = 1;//warnings.count + 1;
		warnings.type.push("Quality <" + this.qualityThreshold);
	}
	return warnings;
};

/** Return array composed by {concentration} objects **/
ResultSummaryForm.prototype.getConcentrations = function(data) {
	var concentrationsId = {};

	for ( var i = 0; i < data.length; i++) {
		var warning = this.detectDataCollectionWarnings(data[i]);
		var id = data[i].conc;// + "_" +  data[i].bufferId;
		if (this.clusterByBuffers) {
			id = data[i].conc + "_" + data[i].bufferId;
		}

		if (concentrationsId[id] == null) {
			concentrationsId[id] = {
				id : id,
				concentration : Number(data[i].conc).toFixed(2),
				bufferId : data[i].bufferId,
				bufferAcronym : data[i].bufferAcronym,
				rgGuinier : [],
				i0Guinier : [],
				rgGnom : [],
				dMax : [],
				quality : [],
				frames : [],
				frames_warning : warning.count
			};
		} else {
			concentrationsId[id].frames_warning = concentrationsId[id].frames_warning + warning.count;
		}

		concentrationsId[id].frames.push(data[i]);

		if (warning.count == 0) {
			concentrationsId[id].rgGuinier.push(data[i].rgGuinier);
			concentrationsId[id].i0Guinier.push(data[i].I0);
			concentrationsId[id].quality.push(data[i].quality);
			concentrationsId[id].rgGnom.push(data[i].rgGnom);
			concentrationsId[id].dMax.push(data[i].dmax);
		}

	}
	var concentrations = [];
	for ( var item in concentrationsId) {
		if (concentrationsId.hasOwnProperty(item)) {
			concentrations.push({
				concentration : concentrationsId[item].concentration,
				id : item,
				bufferId : Number(concentrationsId[item].bufferId).toFixed(2),
				bufferAcronym : concentrationsId[item].bufferAcronym,
				rgGuinier : concentrationsId[item].rgGuinier,
				/** Frames **/
				frames : concentrationsId[item].frames,
				frames_warning : concentrationsId[item].frames_warning,
				/** Calculation **/
				calculation : {
					rgGuinier : BUI.getStandardDeviation(concentrationsId[item].rgGuinier),
					i0Guinier : BUI.getStandardDeviation(concentrationsId[item].i0Guinier),
					quality : BUI.getStandardDeviation(concentrationsId[item].quality),
					rgGnom : BUI.getStandardDeviation(concentrationsId[item].rgGnom),
					dMax : BUI.getStandardDeviation(concentrationsId[item].dMax)

				}
			});
		}
	}

	return {
		concentrations : concentrations
	};
};

ResultSummaryForm.prototype.prepareData = function(data) {
	/** Return array composed by {acronym, bufferId} objects **/
	function getBuffers(data) {
		var buffersId = {};
		for ( var i = 0; i < data.length; i++) {
			buffersId[data[i].bufferId] = data[i].acronym;
		}
		var buffers = [];
		for ( var id in buffersId) {
			if (buffersId.hasOwnProperty(id)) {
				buffers.push({
					acronym : buffersId[id],
					bufferId : id
				});
			}
		}
		return buffers;
	}

	/** Get a string with all the concentrations **/
	function getConcentrationString(parseConcentrations) {
		var concentrations = [];
		for ( var i = 0; i < parseConcentrations.concentrations.length; i++) {
			concentrations.push(parseConcentrations.concentrations[i].concentration + " mg/ml ");
		}
		return concentrations.toString();
	}

	var parseConcentrations = this.getConcentrations(data);

	return {
		dataCollectionCount : data.length,
		buffers : getBuffers(data),
		concentration : parseConcentrations,
		concentrationLabel : getConcentrationString(parseConcentrations)
	};
};

ResultSummaryForm.prototype.getDataForWhisker = function(data) {
	var clusters = [];

	var concentrations = {};
	var i = 0;
	var conc = 0;
	if (this.collapseConcentrations) {
		for (i = 0; i < data.concentration.concentrations.length; i++) {
			conc = data.concentration.concentrations[i];
			var concentration = Number(conc.concentration).toFixed(0);
			if (concentrations[concentration] == null) {
				concentrations[concentration] = {};
				concentrations[concentration].concentration = concentration;
				concentrations[concentration].calculation = {};
				concentrations[concentration].calculation.rgGuinier = {};
				concentrations[concentration].calculation.rgGuinier.values = [];
				concentrations[concentration].calculation.rgGuinier.values = conc.calculation.rgGuinier.values;
				concentrations[concentration].calculation.rgGnom = {};
				concentrations[concentration].calculation.rgGnom.values = [];
				concentrations[concentration].calculation.rgGnom.values = conc.calculation.rgGnom.values;
			} else {
				concentrations[concentration].calculation.rgGuinier.values = concentrations[concentration].calculation.rgGuinier.values
						.concat(conc.calculation.rgGuinier.values);
				concentrations[concentration].calculation.rgGnom.values = concentrations[concentration].calculation.rgGnom.values
						.concat(conc.calculation.rgGnom.values);
			}
		}

		/** From object to array **/
		var array = [];
		for ( var key in concentrations) {
			if (concentrations.hasOwnProperty(key)) {
				array.push(concentrations[key]);
			}
		}
		data.concentration.concentrations = array;
	}

	for (i = 0; i < data.concentration.concentrations.length; i++) {
		conc = data.concentration.concentrations[i];
		clusters.push({
			name : conc.concentration + "mg/ml",
			concentration : Number(conc.concentration),
			x : Number(conc.concentration),
			classes : []
		});
		clusters[clusters.length - 1].classes.push({
			name : "Guinier",
			color : '#9A2EFE',
			values : conc.calculation.rgGuinier.values

		});
		clusters[clusters.length - 1].classes.push({
			name : "P(r)",
			color : '#2E64FE',
			values : conc.calculation.rgGnom.values

		});
	}
	return {
		clusters : clusters.sort(function(a, b) {
			return a.concentration - b.concentration;
		})
	};
};

ResultSummaryForm.prototype.getDataForWhiskerQuality = function(data) {
	var clusters = [];

	for ( var i = 0; i < data.concentration.concentrations.length; i++) {
		var conc = data.concentration.concentrations[i];
		clusters.push({
			name : conc.concentration + "mg/ml",
			classes : []
		});
		clusters[clusters.length - 1].classes.push({
			name : "Quality",
			values : conc.calculation.quality.values

		});
	}

	return {
		clusters : clusters
	};
};

ResultSummaryForm.prototype.plotQualityWhisker = function(parsedData) {
	this.qualityPlot.refresh(this.getDataForWhiskerQuality(parsedData));
};

ResultSummaryForm.prototype.plotWhisker = function(parsedData) {
	this.plot.refresh(this.getDataForWhisker(parsedData));
};

ResultSummaryForm.prototype.plotRange = function(parsedData) {
	this.rangePlot.refresh(this.getDataForWhisker(parsedData));
};

ResultSummaryForm.prototype.getPanel = function(data) {
	var _this = this;
	_this.rawData = data;
	var parsedData = this.prepareData(data);

	this.panel = Ext
			.createWidget(
					'form',
					{
						bodyPadding : 20,
						frame : false,
						border : 0,
						width : this.width,
						height : this.summaryHeight + 1000,
						items : [ {
							xtype : 'container',
							layout : 'hbox',
							items : [ {
								xtype : 'container',
								flex : 1,
								border : false,
								layout : 'anchor',
								defaultType : 'textfield',
								items : [
									_this.qualityControlResultsWidget.getPanel(),
									{
										xtype : 'fieldset',
										width : 950,
										margin : "20, 0 0 0",
										height : this.summaryHeight + 500,
										items : [
											{
												html : "<div align='center' style='background-color:#336699;color:white;border:1px solid black; font-weight:bold'>Concentration Analysis</div>",
												border : 0,
												width : 900

											},
											{
												html : this.concentrationHTMLTableWidget.getPanel(parsedData),
												border : 0,
												width : 900,
												margin : "10, 0 0 10"

											},
											{
												xtype : 'container',
												layout : 'vbox',
												border : 5,
												items : [
													{
														html : "<div align='center' style='background-color:#336699;color:white;border:1px solid black; font-weight:bold'> Rg based on Guinier and Gnom</div>",
														border : 8,
														width : 900,
														margin : "20 0 0 10"

													}, {
														xtype : 'container',
														layout : 'hbox',
														items : [ {
															xtype : 'container',
															layout : 'vbox',
															items : [ {
																html : "<div id='" + _this.id + "_boxPlot'></div>",
																border : 0,
																width : this.plot.width,
																padding : "10 0 0 20",
																listeners : {
																	afterrender : function() {
																		_this.plotWhisker(parsedData);

																	}
																}
															}, {
																html : "<div align='center' style='color:black;'> Concentration (mg/ml)</div>",
																width : 450,
																border : 0,
																margin : "-50 0 0 0"

															} ]
														}, {
															xtype : 'container',
															layout : 'vbox',
															items : [ {
																html : "<div id='" + _this.id + "_rangePlot'></div>",
																border : 0,
																width : this.rangePlot.width,
																padding : "10 0 0 10",
																listeners : {
																	afterrender : function() {
																		_this.plotRange(parsedData);
																	}
																}
															}, {
																html : "<div align='center' style='color:black;'> Concentration (mg/ml)</div>",
																width : 450,
																border : 0,
																margin : "-50 0 0 0"

															} ]
														} ]
													} ]
											} ]
									} ]
							}

							]
						} ]
					});
	return this.panel;

};

ResultSummaryForm.prototype.input = function() {
	return {
		data : DATADOC.getData_3367()
	};
};

ResultSummaryForm.prototype.test = function(targetId) {
	var resultSummaryForm = new ResultSummaryForm();
	var panel = resultSummaryForm.getPanel(resultSummaryForm.input().data);
	panel.render(targetId);

};
