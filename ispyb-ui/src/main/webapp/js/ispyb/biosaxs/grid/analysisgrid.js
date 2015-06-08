/** AnalysisGrid **/
function AnalysisGrid(args) {
	var _this = this;

	this.id = BUI.id();
	if (Ext.get("mainPanel")){
		this.width = Ext.get("mainPanel").getWidth();
	}
	else{
		this.width = this.maxWidth;
	}
	this.maxWidth = 1500;

	/** Visibles **/
	this.isGuinierTabVisible = true;
	this.isGnomTabVisible = true;
	this.isPorodTabVisible = true;
	this.isScatteringPlotVisible = true;
	this.isAbinitioTabVisible = true;
	this.showButtonsVisible = true;
	this.isI0Visible = true;

	this.margin = null;
	this.grouped = true;

	this.hideNulls = false;
	this.decimals = 4;
	this.height = null;
	this.sorters = [ {
		property : 'conc',
		direction : 'ASC'
	} ];

	if (args != null) {
		if (args.grouped != null) {
			this.grouped = args.grouped;
		}

		if (args.showButtonsVisible != null) {
			this.showButtonsVisible = args.showButtonsVisible;
		}
		if (args.isI0Visible != null) {
			this.isI0Visible = args.isI0Visible;
		}
		if (args.sorters != null) {
			this.sorters = args.sorters;
		}
		if (args.isScatteringPlotVisible != null) {
			this.isScatteringPlotVisible = args.isScatteringPlotVisible;
		}
		if (args.isGuinierTabVisible != null) {
			this.isGuinierTabVisible = args.isGuinierTabVisible;
		}
		if (args.isGnomTabVisible != null) {
			this.isGnomTabVisible = args.isGnomTabVisible;
		}
		if (args.isPorodTabVisible != null) {
			this.isPorodTabVisible = args.isPorodTabVisible;
		}
		if (args.hideNulls != null) {
			this.hideNulls = args.hideNulls;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.maxWidth != null) {
			this.maxWidth = args.maxWidth;
		}
	}
}

AnalysisGrid.prototype.refresh = function(data, args) {
	this.store.loadData(this._prepareData(data), false);
	if (args != null){
		if (args.experiment != null){
			this.experiment = args.experiment;
		}
	}
};

AnalysisGrid.prototype._getPorod = function() {
	return {
		text : 'Porod',
		name : 'Porod',
		columns : [
			{
				text : 'Volume',
				dataIndex : 'volumeEdna',
				width : 75,
				sortable : true,
				hidden : !this.isPorodTabVisible,
				renderer : function(val, y, sample) {
					if (sample.raw.volume != null)
						return BUI.formatValuesUnits(sample.raw.volume, '') + "<span style='font-size:8px;color:gray;'> nm<sub>3</sub></span>";
					}
			},
			{
				text : 'MM Vol. est.',
				dataIndex : 'volumeEdna',
				tooltip : '[Volume/2 - Volume/1.5] (Guinier)',
				sortable : true,
				width : 95,
				hidden : !this.isPorodTabVisible,
				renderer : function(val, y, sample) {
					if (sample.raw.volume != null)
						return Number(sample.raw.volume / 2).toFixed(1) + " - " + Number(sample.raw.volume / 1.5).toFixed(1)+ "<span style='font-size:8px;color:gray;'>kD</span>";
				}
			} ]
	};
};


AnalysisGrid.prototype._getFramesColumn = function() {
	var _this = this;
	return {
		text : 'Frames (Averaged/Total)',
		dataIndex : 'datacollection',
		name : 'datacollection',
		sortable : true,
		width : 150,
		renderer : function(dataCollections, y, data) {
			/** Bug of Webservices: frames count were swapped with frames averages **/
			if (data.raw.bufferAfterFramesCount){
				if (data.raw.bufferAfterFramesMerged){
					if (parseInt(data.raw.bufferAfterFramesMerged) > parseInt(data.raw.bufferAfterFramesCount)){
						var aux =  parseInt(data.raw.bufferAfterFramesCount);
						data.raw.bufferAfterFramesCount= data.raw.bufferAfterFramesMerged;
						data.raw.bufferAfterFramesMerged =  aux;
					}
				}
			}
			
			if (data.raw.bufferBeforeFramesCount){
				if (data.raw.bufferBeforeFramesMerged){
					if (parseInt(data.raw.bufferBeforeFramesMerged) > parseInt(data.raw.bufferBeforeFramesCount)){
						var aux =  parseInt(data.raw.bufferBeforeFramesCount);
						data.raw.bufferBeforeFramesCount= data.raw.bufferBeforeFramesMerged;
						data.raw.bufferBeforeFramesMerged =  aux;
					}
				}
				
			}
				
			var bufferAcronym = data.raw.bufferAcronym;
			var macromoleculeAcronym = data.raw.macromoleculeAcronym;
			var bbmerges = data.raw.bufferBeforeFramesMerged;
			var molmerges = data.raw.framesMerge;
			var bamerges = data.raw.bufferAfterFramesMerged;
			var totalframes = data.raw.framesCount;
			var bufferId = data.raw.bufferId;
			var macromoleculeId = data.raw.macromoleculeId;
			var macromoleculeColor = null;
			if (_this.experiment != null){
				macromoleculeColor = _this.experiment.macromoleculeColors[data.raw.macromoleculeId];
			}
			
			/** BUG in the database to be fixed **/
			try{
				if (totalframes != null){
					if(molmerges != null){
						if (parseFloat(totalframes) < parseFloat(molmerges)){
							var aux = totalframes;
							totalframes = molmerges;
							molmerges = aux;
						}
					}
				}
			}
			catch(e){
				
			}
			
			return BUI.getHTMLTableForFrameAveraged(bufferAcronym, 
					macromoleculeAcronym, 
					bbmerges, 
					molmerges, 
					bamerges, 
					totalframes, 
					bufferId,
					macromoleculeId,
					macromoleculeColor);
		}
	};
};

AnalysisGrid.prototype._getColumns = function() {
	var _this = this;
	return [
		{
			name : 'groupeField',
			dataIndex : 'groupeField',
			hidden : true

		},
		{
			"header" : "subtractionId",
			hidden : true,
			"dataIndex" : "subtractionId",
			"name" : "subtractionId"
		},
		{
			header : "Macromolecule",
			dataIndex : "macromoleculeAcronym",
			name : "macromoleculeAcronym",
			renderer : function(val, y, sample) {
				return '<table><tr><td><span style="color:green;font-size:14;">' + val + '</span></td></tr><tr><td>' +
					BUI.formatValuesUnits(sample.raw.conc, "mg/ml", 10, this.decimals) + '</td></tr><tr><td>' +
					BUI.formatValuesUnits(sample.raw.exposureTemperature, "C", 10, this.decimals) + '</td></tr></table>'
			}
		},
		{
			header : "Order",
			dataIndex : "priorityLevelId",
			name : "priorityLevelId",
			hidden : true
		},
		{
			header : "Code",
			dataIndex : "code",
			name : "code",
			hidden : true
		},
		{
			header : "Conc.",
			dataIndex : "conc",
			width : 80,
			name : "conc",
			type : "number",
			hidden : true,
			renderer : function(val, y, sample) {
				return BUI.formatValuesUnits(val, "mg/ml", 10, this.decimals);
			}
		},
		{
			text : 'Scattering',
			width : 100,
			dataIndex : 'subtractionId',
			name : 'subtractionId',
			hidden : !this.isScatteringPlotVisible,
			renderer : function(val, y, sample) {
				var url = BUI.getURL() + '&type=scattering&subtractionId=' + sample.raw.subtractionId;
				var event = "OnClick= window.open('" + url + "')";
				return '<img src=' + url + '   height="80" width="80" ' + event + '>';
			}
		},
		{
			text : 'Kratky',
			width : 100,
			dataIndex : 'subtractionId',
			hidden : true,
			name : 'subtractionId',
			renderer : function(val, y, sample) {
				var url = BUI.getURL() + '&type=kratky&subtractionId=' + sample.raw.subtractionId;
				var event = "OnClick= window.open('" + url + "')";
				return '<img src=' + url + '   height="80" width="80" ' + event + '>';
			}
		},
		this._getFramesColumn(),
		{
			text : 'File Name',
			dataIndex : 'averageFilePath',
			name : 'averageFilePath',
			sortable : true,
			width : 150,
			hidden : true,
			renderer : function(dataCollections, y, data) {
				return BUI.getHTMLTableForPrefixes(data.raw.bufferBeforeAverageFilePath, data.raw.averageFilePath,
						data.raw.bufferAfterAverageFilePath);
			}
		},

		{
			text : 'Guinier',
			name : 'Guinier',
			columns : [
				{
					text : 'Rg',
					dataIndex : 'rgGuinier',
					name : 'rgGuinier',
					hidden : !this.isGuinierTabVisible,
					width : 75,
					tooltip : 'In polymer physics, the radius of gyration is used to describe the dimensions of a polymer chain.',
					sortable : true,
					renderer : function(val, y, sample) {
						if (sample.raw.rgGuinier != null) {
							/** Show warning if rgGuinier and rgGnom differ more than 10% **/
							if (Math.abs(sample.raw.rgGuinier - sample.raw.rgGnom) > (sample.raw.rgGuinier * 0.1)) {
								return "<span style='color:orange;'>" + BUI.formatValuesUnits(sample.raw.rgGuinier, "") + "</span>";

							}
							return BUI.formatValuesUnits(sample.raw.rgGuinier, "nm", 12, this.decimals);
						}
					}
				},
				{
					text : 'Points',
					dataIndex : 'points',
					sortable : true,
					width : 100,
					type : 'string',
					hidden : !this.isGuinierTabVisible,
					renderer : function(val, y, sample) {
						if ((sample.raw.firstPointUsed == "") || (sample.raw.firstPointUsed == null))
							return;
						return "<span>" + sample.raw.firstPointUsed + " - " + sample.raw.lastPointUsed + " (" +
								(sample.raw.lastPointUsed - sample.raw.firstPointUsed) + " )</span>";
					}
				},
				{
					text : 'Quality',
					dataIndex : 'quality',
					hidden : !this.isGuinierTabVisible,
					tooltip : 'Estimated data quality. 1.0 - means ideal quality, 0.0 - unusable data. In table format it is given in percent (100% - ideal quality, 0% - unusable data). Please note that this estimation is based only on the Guinier interval (very low angles).',
					width : 60,
					sortable : true,
					renderer : function(val, y, sample) {
						if (sample.raw.quality != null) {
							val = sample.raw.quality;
							if ((val != null) && (val != "")) {
								return "<span>" + (Number(val) * 100).toFixed(2) + "</span><span style='font-size:8px;color:gray;'> %</span>";
							}
						}
					}
				}, {
					text : 'I(0)',
					dataIndex : 'I0',
					sortable : true,
					hidden : !this.isI0Visible,
					tooltip : 'Extrapolated scattering intensity at zero angle I(0) (forward scattering)',
					width : 75,
					type : 'string',
					renderer : function(val, y, sample) {
						if (sample.raw.I0 != null) {
							return BUI.formatValuesErrorUnitsScientificFormat(sample.raw.I0, sample.raw.i0stdev, "");
						}
					}
				}, {
					text : 'Aggregated',
					tooltip : "If aggregation was detected from the slope of the data curve at low angles the value is '1', otherwise '0'.",
					dataIndex : 'isagregated',
					hidden : true,
					width : 75,
					renderer : function(val, y, sample) {
						if ((sample.raw.isagregated != null)) {
							if (val == true) {
								return "Yes";
							} else {
								return "No";
							}
						}
					}
				}, {
					text : 'Guinier',
					sortable : true,
					dataIndex : 'subtractionId',
					type : 'string',
					width : 100,
					hidden : true,
					renderer : function(val, y, sample) {

						if (sample.raw.subtractionId != null) {
							var url = BUI.getURL() + '&type=guinier&subtractionId=' + sample.raw.subtractionId;
							var event = "OnClick= window.open('" + url + "')";
							return '<img src=' + url + '   height="80" width="80" ' + event + '>';
						}
					}
				} ]
		},
		{
			text : 'Gnom',
			name : 'Gnom',

			columns : [ {
				text : 'Rg',
				dataIndex : 'rgGnom',
				type : 'string',
				width : 65,
				hidden : !this.isGnomTabVisible,
				sortable : true,
				renderer : function(val, y, sample) {
					/** Show warning if rgGuinier and rgGnom differ more than 10% **/
					if (sample.raw.rgGnom != null) {
						if (Math.abs(sample.raw.rgGuinier - sample.raw.rgGnom) > (sample.raw.rgGuinier * 0.1)) {
							return "<span style='color:orange;'>" + BUI.formatValuesUnits(sample.raw.rgGnom, "") + "</span>";

						}
						return BUI.formatValuesUnits(sample.raw.rgGnom, "nm");
					}
				}
			}, {
				text : 'Total',
				dataIndex : 'total',
				width : 65,
				hidden : !this.isGnomTabVisible,
				sortable : true,
				renderer : function(val, y, sample) {
					if (sample.raw.total != null)
						return BUI.formatValuesUnits(sample.raw.total, '');
				}
			}, {
				text : 'D<sup>max</sup>',
				dataIndex : 'dmax',
				sortable : true,
				width : 75,
				hidden : !this.isGnomTabVisible,
				renderer : function(val, y, sample) {
					if (sample.raw.dmax != null)
						return BUI.formatValuesUnits(sample.raw.dmax, "") + "<span style='font-size:8px;color:gray;'> nm</span>";
				}
			}, {
				text : 'P(r)',
				sortable : true,
				hidden : true,
				width : 100,
				dataIndex : 'subtractionId',
				type : 'string',
				renderer : function(val, y, sample) {
					var url = BUI.getURL() + '&type=gnom&subtractionId=' + sample.raw.subtractionId;
					var event = "OnClick= window.open('" + url + "')";
					return '<img src=' + url + '   height="80" width="80" ' + event + '>';
				}
			} ]
		},
		this._getPorod(),
		{
			text : 'AbInitio Modeling',
			name : 'AbInitio_modeling',

			columns : [
				{
					text : 'NSD',
					dataIndex : 'volumeEdna',
					width : 100,
					sortable : true,
					hidden : true,
					renderer : function(val, y, sample) {
						var url = BUI.getNSDImageURL(sample.raw.modelListId); 
						var event = "OnClick= window.open('" + url + "')";
						return '<img src=' + url + '   height="80" width="80" ' + event + '>';
					}
				},
				{
					text : 'Chi2',
					dataIndex : 'volumeEdna',
					sortable : true,
					hidden : true,
					width : 100,
					renderer : function(val, y, sample) {
						var url = BUI.getCHI2ImageURL(sample.raw.modelListId);
						var event = "OnClick= window.open('" + url + "')";
						return '<img src=' + url + '   height="80" width="80" ' + event + '>';
					}
				},
				{
					text : 'Pdb',
					dataIndex : 'volumeEdna',
					tooltip : 'Damaver: The program suite DAMAVER is a set of programs to align ab initio models, select the most typical one and build an averaged model. Dammif : Rapid ab initio shape determination by simulated annealing using a single phase dummy atom model. Dammin :Ab initio shape determination by simulated annealing using a single phase dummy atom model',
					sortable : true,
					width : 125,
					hidden : !this.isAbinitioTabVisible,
					renderer : function(val, y, sample) {
						var html = new String();
						var split = null;
						var url = null;
						var file = sample.raw.averagedModel;
						if (file != null) {
							split = file.split("/");
							if (split.length > 0) {
								url = BUI.getPdbURL() + '&type=AVERAGED&abInitioModelId=' + sample.raw.abInitioModelId;
								html = html + '<a href=' + url + ' style="color:blue;font-weight:bold;"  height="80" width="80" >'+ split[split.length - 1] + '</a><br /><br />';

							}
						}

						file = sample.raw.rapidShapeDeterminationModel;
						if (file != null) {
							split = file.split("/");
							if (split.length > 0) {
								url = BUI.getPdbURL() + '&type=RAPIDSHAPEDETERMINATIONMODEL&abInitioModelId=' + sample.raw.abInitioModelId;
								html = html + '<a href=' + url + ' style="color:blue;font-weight:bold;"  height="80" width="80" >' + split[split.length - 1] + '</a><br /><br />';
							}
						}

						file = sample.raw.shapeDeterminationModel;
						if (file != null) {
							split = file.split("/");
							if (split.length > 0) {
								url = BUI.getPdbURL() + '&type=SHAPEDETERMINATIONMODEL&abInitioModelId=' + sample.raw.abInitioModelId;
								html = html + '<a href=' + url + ' style="color:blue;font-weight:bold;"  height="80" width="80" >'+ split[split.length - 1] + '</a>';
							}
						}
						return html;
					}
				},
				{
					text : 'Damaver',
					dataIndex : 'volumeEdna',
					hidden : true,
					tooltip : 'Damaver: The program suite DAMAVER is a set of programs to align ab initio models, select the most typical one and build an averaged model. ',
					sortable : true,
					width : 125,
					renderer : function(val, y, sample) {
						var file = sample.raw.averagedModel;
						if (file != null) {
							var split = file.split("/");
							if (split.length > 0) {
								var url = BUI.getPdbURL() + '&type=AVERAGED&abInitioModelId=' + sample.raw.abInitioModelId;
								return '<a href=' + url + ' style="color:blue;font-weight:bold;"  height="80" width="80" >' + split[split.length - 1]+ '</a>';
							}
						}
						return sample.raw.averagedModel;
					}
				},
				{
					text : 'Dammif',
					dataIndex : 'volumeEdna',
					hidden : true,
					tooltip : 'Dammif : Rapid ab initio shape determination by simulated annealing using a single phase dummy atom model',
					sortable : true,
					width : 125,
					renderer : function(val, y, sample) {
						var file = sample.raw.rapidShapeDeterminationModel;
						if (file != null) {
							var split = file.split("/");
							if (split.length > 0) {
								var url = BUI.getPdbURL() + '&type=RAPIDSHAPEDETERMINATIONMODEL&abInitioModelId=' + sample.raw.abInitioModelId;
								return '<a href=' + url + ' style="color:blue;font-weight:bold;"  height="80" width="80" >' + split[split.length - 1] + '</a>';
							}
						}
						return sample.raw.averagedModel;
					}
				},
				{
					text : 'Dammin',
					dataIndex : 'volumeEdna',
					hidden : true,
					tooltip : 'Dammin :Ab initio shape determination by simulated annealing using a single phase dummy atom model',
					sortable : true,
					width : 125,
					renderer : function(val, y, sample) {
						var file = sample.raw.shapeDeterminationModel;
						if (file != null) {
							var split = file.split("/");
							if (split.length > 0) {
								var url = BUI.getPdbURL() + '&type=SHAPEDETERMINATIONMODEL&abInitioModelId=' + sample.raw.abInitioModelId;
								return '<a href=' + url + ' style="color:blue;font-weight:bold;"  height="80" width="80" >' + split[split.length - 1]+ '</a>';
							}
						}
						return sample.raw.averagedModel;
					}
				} ]
		}, {
			text : 'Time',
			dataIndex : 'substractionCreationTime',
			name : 'substractionCreationTime',
			hidden : true,
			width : 80,
			sortable : true,
			renderer : function(value, metaData, record, rowIndex, colIndex, store) {
				try {
					if (record.raw.substractionCreationTime != null) {
						return moment(record.raw.substractionCreationTime).format('h:mm:ss a');
					}
				} catch (e) {
					return "NA";
				}
			}
		}, {
			text : 'Date',
			dataIndex : 'substractionCreationTime',
			name : 'substractionCreationTime',
			hidden : true,
			width : 80,
			sortable : true,
			renderer : function(value, metaData, record, rowIndex, colIndex, store) {
				try {
					if (record.raw.substractionCreationTime != null) {
						return moment(record.raw.substractionCreationTime).format("LL");
					}
				} catch (e) {
					return "NA";
				}
			}
		}, 
		this._getButtons()];
};

AnalysisGrid.prototype._getButtons = function() {
	return {
		id : this.id + 'buttonPlot',
		name : 'buttonPlot',
		hidden : !this.showButtonsVisible,
		width : 110,
		sortable : false,
		renderer : function(value, metaData, sample, rowIndex, colIndex, store) {
			var html = "<table><tr><td style='padding:2px;'>" + BUI.getGreenButton('Calibration', {
				height : 20,
				width : 100
			}) + "</td></tr>";
	
			if (sample.raw.subtractionId) {
				html = html + "<tr><td style='padding:2px;'>" + BUI.getGreenButton('Primary Data Proc.', {
					height : 20,
					width : 100
				}) + "</td></tr>";
				if (sample.raw.rapidShapeDeterminationModelId != null) {
					html = html + "<tr><td  style='padding:2px;'>" + BUI.getGreenButton('AbInitio Modeling', {
						height : 20,
						width : 100
					}) + "<td></tr>";
				}
			}
			return html + "</table>";
		}
	};
};
AnalysisGrid.prototype._prepareData = function(data) {
	if (this.hideNulls) {
		var result = [];
		for ( var i = 0; i < data.length; i++) {
			if (data[i].subtractionId != null) {
				data[i].groupeField = data[i].macromoleculeAcronym + " " + data[i].bufferAcronym;
				result.push(data[i]);
			}
		}
		return result;
	} else {
		return data;
	}
};

AnalysisGrid.prototype.getPanel = function(data) {
	var _this = this;
	var columns = this._getColumns();

	var fields = JSON.parse(JSON.stringify(columns));
	fields.push({
		name : 'experimentId',
		dataIndex : 'experimentId'

	});
	this.store = Ext.create('Ext.data.Store', {
		fields : fields,
		autoload : true,
		data : this._prepareData(data),
		groupField : 'groupeField'
	});

	this.store.sort(this.sorters);

	var features = [];

	if (this.grouped) {
		features.push({
			ftype : 'grouping',
			groupHeaderTpl : '{name}',
			hideGroupedHeader : false,
			startCollapsed : false
		});
	}

	this.grid = Ext.create('Ext.grid.Panel', {
		style : {
			padding : 5
		},
		maxWidth : this.maxWidth,
		width : this.width,
		height : this.height,
		store : this.store,
		columns : columns,
		resizable : true,
		features : features,
		viewConfig : {
			preserveScrollOnRefresh : true,
			stripeRows : true,
			listeners : {
				cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {

					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonPlot') {
						if (e.target.defaultValue == 'AbInitio Modeling') {
							var url = BUI.getPDBVisualizerURL(record.raw.averagedModelId, record.raw.subtractionId, record.raw.experimentId);
							window.open(url, "_blank");
						}

						if (e.target.defaultValue == 'Primary Data Proc.') {
							_this._edit(record.raw);
						}

						if (e.target.defaultValue == 'Calibration') {
							_this._showCalibration(record.raw);
						}
					}
				}
			}
		},
		selModel : {
			mode : 'SINGLE'
		}
	});

	return this.grid;

};

AnalysisGrid.prototype._openVisualizarBySubstractionId = function(record) {
	//experimentId, subtractionId) {
	var experimentId = record.experimentId;
	var subtractionId = record.subtractionId;

	var _this = this;
	var adapter = new BiosaxsDataAdapter();
	this.subtractionId = subtractionId;

	adapter.onSuccess.attach(function(sender, data) {
		var experiment = new Experiment(data);
		var experimentList = new ExperimentList([ experiment ]);
		var subtraction = experiment.getSubtractionById(_this.subtractionId);
		_this.grid.setLoading(false);
		_this._openVisualizer(experimentList, subtraction, record);
	});
	this.grid.setLoading("ISPyB: Fetching experiment");
	adapter.getExperimentById(experimentId, "MEDIUM");
};

AnalysisGrid.prototype._edit = function(record) {
	var experimentId = record.experimentId;
	var _this = this;

	if (BIOSAXS.proposal.macromolecules == null) {
		this.grid.setLoading("ISPyB: Fetching proposal");
		BIOSAXS.proposal.onInitialized.attach(function(sender, data) {
			_this._openVisualizarBySubstractionId(record);
		});
		BIOSAXS.proposal.init();
	} else {
		this._openVisualizarBySubstractionId(record);
	}
};


AnalysisGrid.prototype._showCalibration = function(row) {
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, data) {
		Ext.create('Ext.window.Window', {
			title : 'Calibration',
			width : 900,
			resizable : true,
			height : 400,
			modal : true,
			frame : false,
			draggable : true,
			closable : true,
			autoscroll : true,
			paddin : 5,
			layout : {
				type : 'vbox',
				align : 'stretch'
			},
			items : [ new AnalysisGrid({
				isGuinierTabVisible : false,
				isGnomTabVisible : false,
				isPorodTabVisible : false,
				isAbinitioTabVisible : false,
				isI0Visible : true,
				showButtonsVisible : false,
				height : 350,
				sorters : [ {
					property : 'experimentId',
					direction : 'DESC'
				} ]
			}).getPanel(data) ]
		}).show();
	});
	adapter.getAnalysisCalibrationByProposalId();
};

AnalysisGrid.prototype._openVisualizer = function(experimentList, subtraction, record) {
	var merges = experimentList.getMergesByDataCollectionId(subtraction.dataCollectionId);
	var mergeIdList = [];
	for ( var i = 0; i < merges.length; i++) {
		mergeIdList.push(merges[i].mergeId);
	}

	var dataAdapter = new BiosaxsDataAdapter();
	dataAdapter.onSuccess.attach(function(sender, merges) {
		var dataCollectionCurveVisualizer = new DataCollectionCurveVisualizer();
		dataCollectionCurveVisualizer.draw();
		dataCollectionCurveVisualizer.refresh(merges, experimentList, subtraction.dataCollectionId, record);
	});

	dataAdapter.onError.attach(function(sender, error) {
	});

	dataAdapter.getMergesByIdsList(mergeIdList);
};

AnalysisGrid.prototype.input = function() {
	return {
		data : DATADOC.getData_3367().slice(20, 30),//[{"total":"0.447505552082","bufferBeforeMeasurementId":22150,"sampleMergeId":12373,"averagedModelId":43636,"I0":"32.50776","proposalCode":"OPD","averagedModel":"/data/pyarch/bm29/opd29/1137/1d/damaver.pdb","bufferAfterMergeId":12374,"framesCount":"10","bufferBeforeMergeId":12372,"averageFilePath":" /data/pyarch/bm29/opd29/1137/1d/MG386_016_ave.dat","bufferAfterMeasurementId":22152,"rgGuinier":"2.96883","subtractedFilePath":" /data/pyarch/bm29/opd29/1137/1d/MG386_016_sub.dat","firstPointUsed":"30","chi2RgFilePath":"/data/pyarch/bm29/opd29/1137/1d/chi2_R.png","abInitioModelId":272,"macromoleculeId":112,"code":"_20.0_1.25","transmission":"100.0","timeStart":"2013-07-17 17:10:25.743734","bufferAcronym":"MES","quality":"0.87091","shapeDeterminationModelId":43638,"expermientComments":"[BsxCube] Generated from BsxCube","bufferId":707,"isagregated":"False","dmax":"10.390905","bufferBeforeAverageFilePath":" /data/pyarch/bm29/opd29/1137/1d/MG386_015_ave.dat","exposureTemperature":"20.0","subtractionId":5165,"conc":"1.25","rapidShapeDeterminationModel":"/data/pyarch/bm29/opd29/1137/1d/damfilt.pdb","experimentCreationDate":"Jul 17, 2013 5:08:37 PM","lastPointUsed":"92","modelListId":272,"framesMerge":"10","rapidShapeDeterminationModelId":43637,"bufferAfterAverageFilePath":" /data/pyarch/bm29/opd29/1137/1d/MG386_017_ave.dat","nsdFilePath":"/data/pyarch/bm29/opd29/1137/1d/nsd.png","bufferAfterFramesMerged":"10","experimentId":1137,"macromoleculeAcronym":"MG386","i0stdev":"0.05726128","sampleMeasurementId":22151,"measurementComments":"[1] MES","priorityLevelId":2,"bufferBeforeFramesMerged":"10","substractionCreationTime":"Jul 17, 2013 5:12:32 PM","proposalNumber":"29","rgGnom":"3.05242714339","volume":"44.1406","shapeDeterminationModel":"/data/pyarch/bm29/opd29/1137/1d/dammin.pdb","comments":null},{"total":"0.582641941147","bufferBeforeMeasurementId":22152,"sampleMergeId":12375,"averagedModelId":43809,"I0":"31.4366153846","proposalCode":"OPD","averagedModel":"/data/pyarch/bm29/opd29/1137/1d/damaver.pdb","bufferAfterMergeId":12376,"framesCount":"10","bufferBeforeMergeId":12374,"averageFilePath":" /data/pyarch/bm29/opd29/1137/1d/MG386_018_ave.dat","bufferAfterMeasurementId":22155,"rgGuinier":"2.95541","subtractedFilePath":" /data/pyarch/bm29/opd29/1137/1d/MG386_018_sub.dat","firstPointUsed":"21","chi2RgFilePath":"/data/pyarch/bm29/opd29/1137/1d/chi2_R.png","abInitioModelId":273,"macromoleculeId":112,"code":"_20.0_0.65000000000000002","transmission":"100.0","timeStart":"2013-07-17 17:12:55.837462","bufferAcronym":"MES","quality":"0.870164","shapeDeterminationModelId":43811,"expermientComments":"[BsxCube] Generated from BsxCube","bufferId":707,"isagregated":"False","dmax":"10.343935","bufferBeforeAverageFilePath":" /data/pyarch/bm29/opd29/1137/1d/MG386_017_ave.dat","exposureTemperature":"20.0","subtractionId":5166,"conc":"0.65000000000000002","rapidShapeDeterminationModel":"/data/pyarch/bm29/opd29/1137/1d/damfilt.pdb","experimentCreationDate":"Jul 17, 2013 5:08:37 PM","lastPointUsed":"80","modelListId":273,"framesMerge":"10","rapidShapeDeterminationModelId":43810,"bufferAfterAverageFilePath":" /data/pyarch/bm29/opd29/1137/1d/MG386_019_ave.dat","nsdFilePath":"/data/pyarch/bm29/opd29/1137/1d/nsd.png","bufferAfterFramesMerged":"10","experimentId":1137,"macromoleculeAcronym":"MG386","i0stdev":"0.100250461538","sampleMeasurementId":22154,"measurementComments":"[2] MES","priorityLevelId":4,"bufferBeforeFramesMerged":"10","substractionCreationTime":"Jul 17, 2013 5:15:02 PM","proposalNumber":"29","rgGnom":"2.9963404542","volume":"42.2255","shapeDeterminationModel":"/data/pyarch/bm29/opd29/1137/1d/dammin.pdb","comments":null}],
		proposal : new ResultsAssemblyGrid().input().proposal
	};
};

AnalysisGrid.prototype.test = function(targetId) {
	var analysisGrid = new AnalysisGrid();
	BIOSAXS.proposal = new Proposal(analysisGrid.input().proposal);
	var panel = analysisGrid.getPanel(analysisGrid.input().data);
	panel.render(targetId);
};


function HPLCAnalysisGrid(args) {
	AnalysisGrid.prototype.constructor.call(this, args);
}
	
HPLCAnalysisGrid.prototype._edit = AnalysisGrid.prototype._edit;
HPLCAnalysisGrid.prototype._getColumns = AnalysisGrid.prototype._getColumns;
HPLCAnalysisGrid.prototype._openVisualizarBySubstractionId = AnalysisGrid.prototype._openVisualizarBySubstractionId;
HPLCAnalysisGrid.prototype._openVisualizer = AnalysisGrid.prototype._openVisualizer;
HPLCAnalysisGrid.prototype._prepareData = AnalysisGrid.prototype._prepareData;
HPLCAnalysisGrid.prototype._showCalibration = AnalysisGrid.prototype._showCalibration;
HPLCAnalysisGrid.prototype.getPanel = AnalysisGrid.prototype.getPanel;
HPLCAnalysisGrid.prototype.input = AnalysisGrid.prototype.input;
HPLCAnalysisGrid.prototype.test = AnalysisGrid.prototype.test;
HPLCAnalysisGrid.prototype.refresh = AnalysisGrid.prototype.refresh;
HPLCAnalysisGrid.prototype._getPorod = AnalysisGrid.prototype._getPorod;

HPLCAnalysisGrid.prototype._getButtons = function() {
	return {
		id : this.id + 'buttonPlot',
		name : 'buttonPlot',
		hidden : !this.showButtonsVisible,
		width : 110,
		sortable : false,
		renderer : function(value, metaData, sample, rowIndex, colIndex, store) {
			var html = "<table>";
	
			if (sample.raw.subtractionId) {
				if (sample.raw.rapidShapeDeterminationModelId != null) {
					html = html + "<tr><td  style='padding:2px;'>" + BUI.getGreenButton('AbInitio Modeling', {
						height : 20,
						width : 100
					}) + "<td></tr>";
				}
			}
			return html + "</table>";
		}
	};
};

HPLCAnalysisGrid.prototype._getFramesColumn = function() {
	return {
		text : 'Frames',
		dataIndex : 'datacollection',
		name : 'datacollection',
		sortable : true,
		width : 150,
		renderer : function(dataCollections, y, data) {
			molmerges = data.raw.framesMerge;
			totalframes = data.raw.framesCount;
			return molmerges +" - "+  totalframes;
		}
	};
};

