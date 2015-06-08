/**
 * Given data analysis parsed with ResultsAssemblyWidget makes a selector grid with buffer/protein
 **/
function SpecimenSelectorResultGrid() {
	this.id = BUI.id();
}

SpecimenSelectorResultGrid.prototype._prepareData = function(data) {
	var parsed = [];
	for ( var i = 0; i < data.length; i++) {
		var row = data[i];
		for ( var j = 0; j < row.conditions.length; j++) {
			parsed.push({
				bufferId : row.conditions[j].bufferId,
				macromoleculeId : row.macromoleculeId,
				macromoleculeAcronym : BIOSAXS.proposal.getMacromoleculeById(row.macromoleculeId).acronym,
				bufferAcronym : BIOSAXS.proposal.getBufferById(row.conditions[j].bufferId).acronym
			});
		}
	}
	return parsed;
};

SpecimenSelectorResultGrid.prototype.refresh = function(data) {
	this.store.loadData(this._prepareData(data));
};

SpecimenSelectorResultGrid.prototype.getPanel = function(data) {
	var _this = this;
	this.store = Ext.create('Ext.data.Store', {
		fields : [ 'macromoleculeId', 'bufferId', 'macromoleculeAcronym', 'bufferAcronym', 'concentration' ],
		data : this._prepareData(data),
		groupField : 'macromoleculeAcronym'
	});

	this.store.sort('concentration');
	this.grid = Ext.create('Ext.grid.Panel', {
		id : this.id,
		store : this.store,
		width : this.width,
		height : this.height,
		maxHeight : this.maxHeight,
		border : 1,
		features : [ {
			ftype : 'grouping',
			groupHeaderTpl : '{name}',
			hideGroupedHeader : true,
			startCollapsed : false
		} ],
		selModel : Ext.create('Ext.selection.CheckboxModel', {
			allowDeselect : true,
			mode : 'MULTI',
			listeners : {
				selectionchange : function(sm, selections) {
					_this.selected = [];
					for ( var i = 0; i < selections.length; i++) {
						_this.selected.push(selections[i].raw);
					}
				}
			}
		}),
		margin : 10,
		sortableColumns : true,
		columns : [ {
			text : 'Macromolecule',
			dataIndex : 'macromoleculeAcronym',
			flex : 1
		}, {
			text : '',
			dataIndex : 'bufferId',
			width : 20,
			hidden : false,
			renderer : function(val, y, sample) {
				return BUI.getRectangleColorDIV(BIOSAXS.proposal.bufferColors[val], 10, 10);
			}
		}, {
			text : 'Buffer',
			dataIndex : 'bufferAcronym',
			flex : 1
		}, {
			text : 'Concentration',
			dataIndex : 'concentration',
			flex : 1,
			renderer : function(val, y, sample) {
				return val + " mg/ml";
			}
		} ]
	});

	/** Adding the tbar **/
	if (this.tbar) {
		this.grid.addDocked({
			xtype : 'toolbar',
			items : this.getTbar()
		});
	}
	return this.grid;
};

SpecimenSelectorResultGrid.prototype.input = function() {
	return {
		data : new ResultsAssemblyGrid()._prepareData(new ResultsAssemblyGrid().input().data),
		proposal : new ResultsAssemblyGrid().input().proposal
	};
};

SpecimenSelectorResultGrid.prototype.test = function(targetId) {
	var specimenSelectorResultGrid = new SpecimenSelectorResultGrid();
	BIOSAXS.proposal = new Proposal(specimenSelectorResultGrid.input().proposal);
	var panel = specimenSelectorResultGrid.getPanel(specimenSelectorResultGrid.input().data);
	panel.render(targetId);
};

/**
 * Show all buffer conditions for each macromolecules pointing out the measurements quality
 * 
 * @height
 * @maxHeight
 * @width
 * @searchBar
 * @tbar
 * @btnResultVisible
 * 
 * #onClick
 */
function ResultsAssemblyGrid(args) {
	this.height = 500;
	this.id = BUI.id();
	this.maxHeight = this.height;

	this.width = 900;
	this.searchBar = false;
	this.tbar = false;
	this.btnResultVisible = false;

	/** For processing **/
	this.processed = {};
	this.renderedPlotIndex = 0;

	this.plotWidth = 210;
	this.plotHeight = 80;

	/** Colors **/
	this.validColor = BUI.getValidColor();
	this.warningColor = BUI.getWarningColor();
	this.notValidColor = BUI.getErrorColor();

	/** Show warning if guinier quality less than **/
	this.guinierQuality = BUI.getQualityThreshold();
	this.framePercentageThreshold = BUI.getRadiationDamageThreshold();

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
			this.maxHeight = this.height;
		}
		if (args.maxHeight != null) {
			this.maxHeight = args.maxHeight;
		}
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.searchBar != null) {
			this.searchBar = args.searchBar;
		}

		if (args.tbar != null) {
			this.tbar = args.tbar;
		}
		if (args.btnResultVisible != null) {
			this.btnResultVisible = args.btnResultVisible;
		}

	}

	this.onClick = new Event();
}

ResultsAssemblyGrid.prototype.edit = function(macromoleculeId) {
	var _this = this;
	var window = new MacromoleculeWindow();
	window.onSuccess.attach(function(sender, proposal) {
		_this.store.loadData(_this._prepareData(BIOSAXS.proposal.getMacromolecules()));
	});
	window.draw(BIOSAXS.proposal.getMacromoleculeById(macromoleculeId));
};

ResultsAssemblyGrid.prototype.getTbar = function() {
	var _this = this;
	var actions = [];

	actions.push(Ext.create('Ext.Action', {
		icon : '../images/add.png',
		text : 'Add Macromolecule',
		disabled : false,
		handler : function(widget, event) {
			var window = new MacromoleculeWindow();
			window.onSuccess.attach(function(sender) {
				_this.refresh();
			});
			window.draw({});
		}
	}));

	actions.push(Ext.create('Ext.Action', {
		icon : '../images/add.png',
		text : 'Define an Assembly',
		disabled : false,
		handler : function(widget, event) {
			var createAssemblywindow = new CreateAssemblyWindow();
			createAssemblywindow.onSaved.attach(function(evt, args) {
				if (args.macromoleculeIds.length > 0) {
					var adapter = new BiosaxsDataAdapter();
					adapter.onSuccess.attach(function(sender, proposal) {
						_this.refresh();
					});
					adapter.saveAssembly(args.assemblyId, args.macromoleculeIds);

				}

			});
			createAssemblywindow.draw(_this.experiment);
		}
	}));

	return actions;
};

ResultsAssemblyGrid.prototype.refresh = function() {
	this.store.loadData(this._prepareData());
};

ResultsAssemblyGrid.prototype.addCondition = function(record, data_parsed, i) {
	function getCondition(record) {
		return {
			concentration : record.conc,
			quality : record.quality,
			bufferBeforeFramesMerged : record.bufferBeforeFramesMerged,
			bufferAfterFramesMerged : record.bufferAfterFramesMerged,
			framesCount : record.framesCount,
			framesMerge : record.framesMerge
		};
	}

	if (data_parsed[i].conditions != null) {
		var bufferFound = false;
		for ( var index in data_parsed[i].conditions) {
			condition = data_parsed[i].conditions[index];

			if ((condition.macromoleculeId == record.macromoleculeId) && (condition.bufferId == record.bufferId)) {
				data_parsed[i].conditions[index].concentrations.push(getCondition(record));
				bufferFound = true;
			}
		}
		if (!bufferFound) {
			data_parsed[i].conditions.push({
				macromoleculeId : record.macromoleculeId,
				bufferId : record.bufferId,
				concentrations : [ getCondition(record) ]
			});
		}
	}
};

ResultsAssemblyGrid.prototype.process = function(record, data_parsed) {
	if (this.processed[record.macromoleculeId] == null) {
		this.processed[record.macromoleculeId] = true;
		record.measurementCount = 1;
		record.subtractionCount = 1;
		record.averageCount = 1;
		record.conditions = [];
		data_parsed.push(record);
	}

	for ( var i = 0; i < data_parsed.length; i++) {
		if (data_parsed[i].macromoleculeId == record.macromoleculeId) {
			data_parsed[i].measurementCount = data_parsed[i].measurementCount + 1;

			if (record.subtractionId != null) {
				data_parsed[i].subtractionCount = data_parsed[i].subtractionCount + 1;
				this.addCondition(record, data_parsed, i);
			}

			if (record.framesMerge != null) {
				data_parsed[i].averageCount = data_parsed[i].averageCount + 1;
			}

			if (record.timeStart != null) {
				if (data_parsed[i].timeStart != null) {
					if (moment(data_parsed[i].timeStart).format("X") > moment(record.timeStart).format("X")) {
						data_parsed[i].timeStart = record.timeStart;
					}
				}
			}
		}
	}

	return data_parsed;

};

ResultsAssemblyGrid.prototype._prepareData = function(data) {
	var data_parsed = [];
	for ( var i = 0; i < data.length; i++) {
		data_parsed = this.process(data[i], data_parsed);
	}
	this.data = data_parsed;
	return data_parsed;
};

/** Given an array of conditions it returns distinct(concentrations) order by concentration and hash map with number of ocurrences**/
ResultsAssemblyGrid.prototype.parseConcentrations = function(val, conditions, differentConcentration, quality) {
	var conditions = [];
	var differentConcentration = {};
	var quality = [];
	for ( var i = 0; i < val.length; i++) {
		var conc = Number(val[i].concentration).toFixed(1);
		var quality = Number(val[i].quality).toFixed(2);
		if (differentConcentration[conc] == null) {
			differentConcentration[conc] = 0;
			conditions.push({
				concentration : conc,
				quality : [ quality ],
				frames : [ {
					bufferAfterFramesMerged : val[i].bufferAfterFramesMerged,
					bufferBeforeFramesMerged : val[i].bufferBeforeFramesMerged,
					framesMerge : val[i].framesMerge,
					framesCount : val[i].framesCount
				} ]
			});
		} else {
			/** Add quality **/
			for ( var j = 0; j < conditions.length; j++) {
				if (conditions[j].concentration == conc) {
					conditions[j].quality.push(quality);
					conditions[j].frames.push({
						bufferAfterFramesMerged : val[i].bufferAfterFramesMerged,
						bufferBeforeFramesMerged : val[i].bufferBeforeFramesMerged,
						framesMerge : val[i].framesMerge,
						framesCount : val[i].framesCount
					});
				}
			}
		}

		differentConcentration[conc] = differentConcentration[conc] + 1;
	}
	/** sorting concentrations **/
	conditions.sort(function(a, b) {
		return a.concentration - b.concentration;
	});
	return {
		concentrations : conditions,
		differentConcentration : differentConcentration
	};
};

ResultsAssemblyGrid.prototype.getConditionWarnings = function(condition) {

	var withWarnings = 0;

	for ( var i = 0; i < condition.frames.length; i++) {
		if (condition.quality[i] == null) {
			withWarnings = withWarnings + 1;
			continue;
		} else {
			if (Number(condition.quality[i]) < this.guinierQuality) {
				withWarnings = withWarnings + 1;
				continue;
			}
		}

		if (condition.frames[i].bufferBeforeFramesMerged / condition.frames[i].framesCount < this.framePercentageThreshold|| condition.frames[i].framesMerged / condition.frames[i].framesCount < this.framePercentageThreshold|| condition.frames[i].bufferAfterFramesMerged / condition.frames[i].framesCount < this.framePercentageThreshold) {
			withWarnings = withWarnings + 1;
			continue;
		}
	}

	return {
		withWarnings : withWarnings,
		withoutWarnings : condition.frames.length - withWarnings
	};
};

ResultsAssemblyGrid.prototype.getFrameHTMLTable = function(warnings) {
	var html = "";
	if (warnings.withWarnings > 0) {
		html = html + "<div style='cursor: pointer;'><span style='font-size:9px;padding-left:5px;'>" + warnings.withWarnings +
				"x </span> <img height='15px' src='../images/warning_01.png'> </div>";
	}

	if (warnings.withoutWarnings > 0) {
		html = html + "<div style='cursor: pointer;'><span style='font-size:9px;padding-left:5px;'>" + warnings.withoutWarnings +
				"x </span> <img height='15px' src='../images/accept.png'> </div>";
	}
	return html;
};

ResultsAssemblyGrid.prototype.createConcentrationRow = function(numberOcu, condition, warnings){
	var html = "<tr><td>";
	if (numberOcu > 1){
		html = html + "<span style='font-size:9px;'>" +numberOcu + "x </span>" + BUI.formatConcentration(condition.concentration);
	}
	else{
		html = html + BUI.formatConcentration(condition.concentration);
	}
	html = html + "</td><td>";
	html = html + this.getFrameHTMLTable(warnings); + "</td>";
	html = html + "</tr>";
	return html;
};


ResultsAssemblyGrid.prototype.getConditionHTMLTable = function(val, style, record) {
	var maxNumberColumns = 2;
	var html = "<div><table class='conditions'><tr>";
	var nColumns = 0;
	for ( var r = 0; r < val.length; r++) {
		if (nColumns == maxNumberColumns) {
			nColumns = 0;
			html = html + "<tr>";
		}
		html = html + "<td>";
		var value = val[r];

		var bufferAcronym = (BIOSAXS.proposal.getBufferById(value.bufferId).acronym);

		var parsed = (this.parseConcentrations(value.concentrations));
		var conditions = parsed.concentrations;
		var differentConcentration = parsed.differentConcentration;

		/** Checking warnings **/
		var warnings = [];
		var concentrationValidPerconcentration = 0;
		var measurements = 0;
		for ( var i = 0; i < conditions.length; i++) {
			measurements = measurements + conditions[i].frames.length;
			var warning = this.getConditionWarnings(conditions[i]);
			warnings.push(warning);
			if (warning.withoutWarnings > 0) {
				concentrationValidPerconcentration = concentrationValidPerconcentration + 1;
			}
		}

		this.validColor = '#E0F8E0';
		this.warningColor = '#F5DA81';
		this.notValidColor = '#F6CED8';

		var color = this.warningColor;
		if (concentrationValidPerconcentration > 2) {
			color = this.validColor;
		}
		/** More measurement need to be done **/
		if (measurements < 3) {
			color = this.notValidColor;
		}

		html = html + "<table class='condition''>";
		html = html + "<tr style='background-color:" + color + ";'><th  ><a style='color:blue;' href='" +
				BUI.getMacromoleculeBufferResultsURL(record.raw.macromoleculeId, val[r].bufferId) + "'>" + bufferAcronym.toUpperCase() +
				"</a></th></tr>";
		for ( var i = 0; i < conditions.length; i++) {
			html = html + this.createConcentrationRow(differentConcentration[conditions[i].concentration], conditions[i], warnings[i]);
		}

		html = html + "</td></tr>";
		html = html + "</table>";

		html = html + "</td>";
		nColumns = nColumns + 1;
	}
	html = html + "</tr></table></div>";
	return html;
};

ResultsAssemblyGrid.prototype._getTbar = function() {
	function goTo(url) {
		window.location = url;
	}

	var _this = this;
	return [ {
		icon : '../images/application_view_list.png',
		text : 'Multiple Select',
		handler : function() {
			var specimenSelectorResultGrid = new SpecimenSelectorResultGrid();
			var window = Ext.create('Ext.window.Window', {
				title : 'Multiple select',
				height : 600,
				width : 600,
				layout : 'fit',
				items : [ specimenSelectorResultGrid.getPanel(_this.data) ],
				buttons : [ {
					text : 'Go',
					handler : function() {
						var array = [];
						for ( var i = 0; i < specimenSelectorResultGrid.selected.length; i++) {
							var row = specimenSelectorResultGrid.selected[i];
							array.push({
								macromoleculeId : row.macromoleculeId,
								bufferId : row.bufferId
							});
						}
						goTo(BUI.getMacromoleculeResultsURLByMultipleSearch(array));

					}
				}, {
					text : 'Cancel',
					handler : function() {
						window.close();
					}
				} ]

			}).show();

		}
	} ];
};

ResultsAssemblyGrid.prototype.getLegendPanel = function() {
	return {
		html : '<table><tr><td>' + BUI.getRectangleColorDIV(this.validColor, 10, 10) + 
				'</td><td style="padding-left:5px;">Good quality measurements</td><td style="padding-left:20px;">' + 
				BUI.getRectangleColorDIV(this.warningColor, 10, 10) +
				'</td><td style="padding-left:5px;">Probably valid with manual processing</td><td style="padding-left:20px;">' +
				BUI.getRectangleColorDIV(this.notValidColor, 10, 10) +
				'</td><td style="padding-left:5px;">More measurements need to be done</td></tr></table>'
	};
};
/** Returns the grid **/
ResultsAssemblyGrid.prototype.getPanel = function(macromolecules) {
	var _this = this;

	this.store = Ext.create('Ext.data.Store', {
		fields : [
			'macromoleculeId', 'macromoleculeAcronym', 'measurementCount', 'subtractionCount', 'averageCount', 'timeStart', 'concentrationArray',
			'bufferConditions', 'conditions' ],
		data : this._prepareData(macromolecules)
	});

	this.store.sort('macromoleculeAcronym');

	this.grid = Ext.create('Ext.grid.Panel', {
		id : this.id,
		title : 'Macromolecules',
		store : this.store,
		tbar : this._getTbar(),
		bbar : [ this.getLegendPanel() ],
		width : this.width,
		height : this.height,
		maxHeight : this.maxHeight,
		sortableColumns : true,
		columns : [
			{
				text : '',
				dataIndex : 'macromoleculeId',
				width : 20,
				hidden : false,
				renderer : function(val, y, sample) {
					return BUI.getRectangleColorDIV(BIOSAXS.proposal.macromoleculeColors[val], 10, 10);
				}
			},
			{
				text : 'Macromolecule',
				dataIndex : 'macromoleculeAcronym',
				width : 200
			},
			{
				text : 'Buffer Conditions',
				dataIndex : 'conditions',
				flex : 1,
				renderer : function(val, style, record) {
					return _this.getConditionHTMLTable(val, style, record);
				}
			},
			{
				header : 'Average',
				dataIndex : 'percentageCollected',
				name : 'percentageCollected',
				type : 'string',
				hidden : true,
				renderer : function(val, y, sample) {
					return "<table><tr><td>" +
							BUI.getProgessBar((sample.raw.averageCount / sample.raw.measurementCount) * 100, sample.raw.averageCount + "/" +
							sample.raw.measurementCount) + "</td></table>";
				},
				width : 100,
				sorter : false
			},
			{
				header : 'Subtractions',
				dataIndex : 'percentageCollected',
				name : 'percentageCollected',
				type : 'string',
				hidden : true,
				renderer : function(val, y, sample) {
					return "<table><tr><td>"+  BUI.getProgessBar((sample.raw.subtractionCount / sample.raw.measurementCount) * 100, sample.raw.subtractionCount + "/" +
									sample.raw.measurementCount) + "</td></table>";
				},
				width : 100
			}, {
				header : 'Date',
				dataIndex : 'timeStart',
				name : 'timeStart',
				type : 'string',
				renderer : function(value, metaData, record, rowIndex, colIndex, store) {
					if (record.raw.timeStart != null) {
						return moment(record.raw.timeStart).format("MMM Do YY");
					}
				}
			}, {
				header : 'Download',
				dataIndex : 'percentageCollected',
				name : 'percentageCollected',
				type : 'string',
				renderer : function(val, y, sample) {
					return BUI.getZipHTMLByMacromoleculeId(sample.raw.macromoleculeId);
				},
				width : 100
			},

			{
				id : 'btnResultVisible',
				width : 85,
				sortable : false,
				renderer : function(value, metaData, record, rowIndex, colIndex, store) {
					return BUI.getGreenButton('GO');
				}
			} ],
		viewConfig : {
			stripeRows : true,
			listeners : {
				afterrender : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
				},
				celldblclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
					_this.edit(record.data.macromoleculeId);
				},
				cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
					if (grid.getGridColumns()[cellIndex].getId() == 'buttonEditMacromolecule') {
						_this.edit(record.data.macromoleculeId);
					}
					if (grid.getGridColumns()[cellIndex].getId() == 'buttonRemoveMacromolecule') {
						BUI.showBetaWarning();
					}
					if (grid.getGridColumns()[cellIndex].getId() == 'btnResultVisible') {
						window.location = BUI.getMacromoleculeResultsURL(record.data.macromoleculeId);
					}
				}
			}
		}
	});

	/** Adding the tbar **/
	if (this.tbar) {
		this.grid.addDocked({
			xtype : 'toolbar',
			items : this.getTbar()
		});
	}
	return this.grid;
};

ResultsAssemblyGrid.prototype.input = function(targetId) {
	return {
		data : DATADOC.getData_3367(),
		proposal : DATADOC.getProposal_3367()
	};
};

ResultsAssemblyGrid.prototype.test = function(targetId) {
	var grid = new ResultsAssemblyGrid({
		height : 600,
		searchBar : false,
		tbar : false,
		btnResultVisible : true,
		width : 1000
	});
	BIOSAXS.proposal = new Proposal(grid.input().proposal);
	var panel = grid.getPanel(grid.input().data);
	panel.render(targetId);

};
