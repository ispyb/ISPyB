/**
 * It shows all the measurements done for a proposal sorted by run number
 * 
 * @showAveragesHistory shows all the averages instead of only the last one.
 *                      Specially useful when something has been reprocessed
 * @isGrouped to be grouped by day
 * @tbar top bar is visible
 */
function QueueGrid(args) {
	this.height = 700;
	this.searchBar = false;
	this.tbar = false;
	this.bbar = true;
	this.collapsed = false;
	this.id = BUI.id();

	this.margin = 0;
	this.key = {};
	var _this = this;
	this.filters = [ function(item) {
		if (item.data.dataCollectionId == null) {
			return false;
		}
		if (_this.key[item.data.dataCollectionId] == null) {
			_this.key[item.data.dataCollectionId] = [];
		}
		_this.key[item.data.dataCollectionId].push(item.data);
		
		return item.data.macromoleculeId != null;
	} ];

	this.sorter = [ {
		property : 'measurementId',
		direction : 'DESC'
	} ];

	this.url = BUI.getQueueUrl();

	this.showAveragesHistory = false;
	this.isGrouped = true;

	/** Limit of datacollection to be fecthed * */
	this._limit = 150;
	this.pageSize = 30;
	this.pageCount = null;
	this.currentPage = 1;

	// this.title =this.getTitle();
	this.hideSolvents = false;

	/** Selected items * */
	this.selected = [];

	/** This is the precission to be shown in the grid * */
	this.decimals = 3;
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.pageSize != null) {
			this.pageSize = args.pageSize;
		}
		if (args.sorter != null) {
			this.sorter = args.sorter;
		}
		if (args.margin != null) {
			this.margin = args.margin;
		}
		if (args.url != null) {
			this.url = args.url;
		}
		if (args.filters != null) {
			this.filters = args.filters;
		}
		if (args.bbar != null) {
			this.bbar = args.bbar;
		}
		if (args.searchBar != null) {
			this.searchBar = args.searchBar;
		}
		if (args.isGrouped != null) {
			this.isGrouped = args.isGrouped;
		}
		if (args.title != null) {
			this.title = args.title;
		}

		if (args.tbar != null) {
			this.tbar = args.tbar;
		}

		if (args.collapsed != null) {
			this.collapsed = args.collapsed;
		}

		if (args.width != null) {
			this.width = args.width;
		}

		if (args._limit != null) {
			this._limit = args._limit;
		}
	}
}

QueueGrid.prototype.getTitle = function() {
	return 'Last ' + this._limit + ' Data Collections'
};

QueueGrid.prototype.setLimit = function(limit) {
	this._limit = limit;
	this.grid.setTitle(this.getTitle());
};

QueueGrid.prototype.refresh = function(data) {
	if (data != null) {
		this.key = {}
		this.store.loadData(data);
	} else {
		this.store.load();

	}
};

QueueGrid.prototype._addRecord = function(record, data) {
	data.push(record);
	return data;

};

QueueGrid.prototype._prepareData = function(records) {
	var data = [];
	this.groupingKey = {};
	for (var i = 0; i < records.length; i++) {
		var record = records[i];
		records[i]["date"] = moment(records[i].creationDate).format("YYYYMMDD");
		if (record.dataCollectionId != null) {
			if (record.macromoleculeId != null) {
				this.groupingKey[record.dataCollectionId] = record.macromoleculeAcronym + "  " + record.concentration;
			}
		}
	}
	return records;
};

QueueGrid.prototype.getTbar = function() {
	var button = {
		text : 'Primary Data Processing ',
		xtype : 'button',
		icon : '../images/magnif.png',
		handler : function() {
			var mergeIds = [];
			var subtractionIds = [];
			for (var i = 0; i < _this.selected.length; i++) {
				if (_this.selected[i].mergeId != null) {
					mergeIds.push(_this.selected[i].mergeId);
				}
				/** Buffers row contains also their subtractionId * */
				if (_this.selected[i].macromoleculeId != null) {
					if (_this.selected[i].subtractionId != null) {
						subtractionIds.push(_this.selected[i].subtractionId);
					}
				}
			}
			_this.openCurveVisualizer(mergeIds, subtractionIds);

		}
	};

	var _this = this;
	function onItemCheck(option, b) {
		/** Only it does something when limit changes * */
		if (option.text != _this._limit) {
			if (option.text == "All") {
				_this.setLimit(2000);
			} else {
				_this.setLimit(option.text);
				_this.update();
			}
		}
	}

	function onHideSolventCheck(option, b) {
		_this.hideSolvents = option.checked;

		_this.refresh(_this.data);
	}

	return Ext.create('Ext.toolbar.Toolbar', {
		flex : 1,
		border : 1,
		items : [ {
			xtype : 'button',
			text : 'Search',
			icon : '../images/magnif.png',
			menu : [ {
				text : 'By date'
			}, {
				text : 'By macromolecule'
			} ]
		},

		{
			xtype : 'button',
			text : 'View',
			menu : [ {
				text : 'Hide solvents',
				checked : false,
				checkHandler : onHideSolventCheck
			}, {
				text : 'Fetch',
				menu : { // <-- submenu by nested config object
					items : [
					// stick any markup in a menu
					'<b class="menu-title">Choose a number of data collection to be fetched</b>', {
						text : '25',
						checked : true,
						group : 'theme',
						checkHandler : onItemCheck
					}, {
						text : '100',
						checked : false,
						group : 'theme',
						checkHandler : onItemCheck
					}, {
						text : '200',
						checked : false,
						group : 'theme',
						checkHandler : onItemCheck
					}, {
						text : '500',
						checked : false,
						group : 'theme',
						checkHandler : onItemCheck
					}, {
						text : '1000',
						checked : false,
						group : 'theme',
						checkHandler : onItemCheck
					}, {
						text : 'All',
						checked : false,
						group : 'theme',
						checkHandler : onItemCheck
					} ]
				}
			}

			]
		} ]
	});
};

QueueGrid.prototype._getPorod = function() {
	return {
		text : 'Porod',
		name : 'Porod',
		columns : [
				{
					text : 'Volume',
					dataIndex : 'volumePorod',
					width : 80,
					sortable : true,
					renderer : function(val, y, sample) {
						if (sample.raw.macromoleculeId != null) {
							if (sample.raw.subtractionId != null) {
								if (sample.raw.volumePorod != null)
									return BUI.formatValuesUnits(sample.raw.volumePorod, '') + "<span style='font-size:8px;color:gray;'> nm<sub>3</sub></span>";
							}
						}
					}
				},
				{
					text : 'MM Vol. est.',
					dataIndex : 'volumeEdna',
					tooltip : '[Volume/2 - Volume/1.5] (Guinier)',
					sortable : true,
					width : 95,
					renderer : function(val, y, sample) {
						var html = "";
						if (sample.raw.macromoleculeId != null) {
							if (sample.raw.subtractionId != null) {
								if (sample.raw.volume != null)
									html = html + Number(sample.raw.volumePorod / 2).toFixed(1) + " - " + Number(sample.raw.volumePorod / 1.5).toFixed(1)
											+ "<span style='font-size:8px;color:gray;'>kD</span>";
							}
						}
						return html;
					}
				} ]
	};
};

QueueGrid.prototype._getHTMLRow = function(key, value, error, units, decimals) {
	if (value != null) {
		if (decimals != null) {
			value = Number(value).toFixed(decimals);
		}

		if (error != null) {
			return "<td style='padding:2px;color:gray'>" + key + ": </td><td style='padding-left:10px;font-size:12px;'>" + value + " " + units
					+ "  <span style='font-size:10px;padding-left:5px;'>&#177; " + error + "</span></td>";

		} else {
			return "<tr><td style='padding:2px;color:gray'>" + key + ": </td><td style='padding-left:10px'>" + value + " " + units + "</td></tr>";
		}
	}
	return "";
};

QueueGrid.prototype.openCurveVisualizerBySelected = function(mergeIds, subtractionIds) {
	var mergeIds = [];
	var subtractionIds = [];
	for (var i = 0; i < this.selected.length; i++) {
		if (this.selected[i].mergeId != null) {
			mergeIds.push(this.selected[i].mergeId);
		}
		/** Buffers row contains also their subtractionId * */
		if (this.selected[i].macromoleculeId != null) {
			if (this.selected[i].subtractionId != null) {
				subtractionIds.push(this.selected[i].subtractionId);
			}
		}
	}
	this.openCurveVisualizer(mergeIds, subtractionIds);
};

QueueGrid.prototype.openCurveVisualizer = function(mergeIds, subtractionIds) {
	/**
	 * When showing subtraction we want to show 1) Sample Average 2) Buffer
	 * Average 3) Frames used for the subtraction
	 */
	var viz = new SubtractionCurveVisualizer();
	var panel = viz.getPanel();
	Ext.create('Ext.window.Window', {
		title : 'Primary Data Processing',
		height : 500,
		width : 800,
		layout : 'fit',
		items : [ panel ]
	}).show();

	viz.refresh(mergeIds, subtractionIds);
};

QueueGrid.prototype.getStore = function() {
	var _this = this;
	Ext.define('Queue', {
		extend : 'Ext.data.Model',
		fields : [ 'name', 'date', 'volumePorod', 'runCreationDate', 'measurementCode', 'macromoleculeAcronym', 'bufferAcronym', 'I0', 'I0Stdev', 'acronym', 'averageFilePath',
				'bufferAverageFilePath', 'bufferId', 'bufferOnedimensionalFiles', 'code', 'comments', 'composition', 'concentration', 'creationDate', 'creationTime',
				'dataAcquisitionFilePath', 'dataCollectionId', 'discardedFrameNameList', 'dmax', 'experimentId', 'experimentType', 'exposureTemperature', 'extintionCoefficient',
				'extraFlowTime', 'firstPointUsed', 'flow', 'frameListId', 'framesCount', 'framesMerge', 'gnomFilePath', 'gnomFilePathOutput', 'guinierFilePath', 'isagregated',
				'kratkyFilePath', 'lastPointUsed', {
					name : 'macromoleculeId',
					id : 'macromoleculeId'

				}, {
					name : 'measurementId',
					type : 'int'
				}, 'mergeId', 'molecularMass', 'name', 'pH', 'priorityLevelId', 'proposalId', 'quality', 'rg', 'rgGnom', 'rgGuinier', 'rgStdev', 'runId', 'safetyLevelId',
				'sampleAverageFilePath', 'sampleOneDimensionalFiles', 'samplePlatePositionId', 'scatteringFilePath', 'sequence', 'sessionId', 'sourceFilePath', 'specimenId',
				'status', 'stockSolutionId', 'substractedFilePath', 'subtractionId', 'total', 'transmission', 'viscosity', 'volume', 'volumeToLoad', 'waitTime', 'reference',
				'refined', 'fitCount', 'superposisitionCount', 'rigidbodyCount', 'abinitioCount' ]
	});

	this.store = Ext.create('Ext.data.Store', {
		model : 'Queue',
		groupField : 'dataCollectionId',
		groupDir : 'DESC',
		filters : this.filters,
		remoteFilter : false,
		pageSize : this.pageSize,
		remoteSort : true,
		listeners : {
			beforeload : function() {
				_this.key = {};
				return true;
			},
			load : function(store, records) {

			}
		},
		proxy : {
			autoload : false,
			type : 'ajax',
			url : this.url,
			reader : {
				type : 'json',
				totalProperty : 'results',
				root : 'rows'
			}
		}
	});
	return this.store;

};

QueueGrid.prototype.getPanel = function() {
	var _this = this;
	this.data = _this._prepareData([]);
	/**
	 * Store in Memory
	 */
	this.store = this.getStore();

	var features = [];

	if (this.isGrouped) {
		/** Grouping by date * */
		features.push(Ext.create('Ext.grid.feature.Grouping', {
			groupHeaderTpl : '{[values.rows[0].dataCollectionId]}',
			hideGroupedHeader : false,
			startCollapsed : false
		}));

	}

	/**
	 * Selection mode is multi in order to compare all the frames from different
	 * data collections (measurements)
	 */
	var selModel = Ext.create('Ext.selection.RowModel', {
		allowDeselect : true,
		mode : 'MULTI',
		listeners : {
			selectionchange : function(sm, selections) {
				_this.selected = new Array();
				for (var i = 0; i < selections.length; i++) {
					_this.selected.push(selections[i].raw);
				}
			}
		}
	});

	this.grid = Ext.create('Ext.grid.Panel', {
		title : this.title,
		collapsible : false,
		features : features,
		resizable : true,
		autoscroll : true,
		store : this.store,
		layout : 'fit',
		border : 1,
		height : this.height,
		emptyText : "No datacollections",
		width : this.width,
		columns : this.getColumns(),
		rowLines : false,
		margin : this.margin,
		viewConfig : {
			enableTextSelection : true,
			preserveScrollOnRefresh : true,
			stripeRows : false,
			rowLines : false,
			getRowClass : function(record, rowIdx, params, store) {
				// return "queue-grid-row";
			},
			listeners : {
				celldblclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
				},
				cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {

					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonAction') {
						if (e.target.defaultValue == 'Data Reduction') {
							//							_this.openCurveVisualizerBySelected();
							_this.onDataReductionButtonClicked(record.raw);
						}

						if (e.target.defaultValue == 'Abinitio') {
							_this.onAbinitioButtonClicked(record.raw);
						}

						if (e.target.defaultValue == 'Fit') {
							_this.onFitButtonClicked(record.raw);
						}

						if (e.target.defaultValue == 'Superposition') {
							_this.onSuperpositionButtonClicked(record.raw);
						}

						if (e.target.defaultValue == 'Rigid Body') {
							_this.onRigidBodyButtonClicked(record.raw);
						}
					}
				}
			}
		}
	});

	/** Adding the tbar * */
	if (this.tbar) {
		this.grid.addDocked(this.getTbar());
	}

	if (this.bbar) {
		this.grid.addDocked({
			xtype : 'pagingtoolbar',
			store : this.store,
			dock : 'bottom',
			displayInfo : true
		});
	}
	return this.grid;
};

QueueGrid.prototype.onDataReductionButtonClicked = function(record) {
	var adapter = new BiosaxsDataAdapter();
	var dataReductionForm = new DataReductionForm({});

	Ext.create('Ext.window.Window', {
		title : 'Data Reduction',
		height : 540,
		width : 1000,
		modal : true,
		items : [ dataReductionForm.getPanel() ]
	}).show();

	dataReductionForm.panel.setLoading();
	adapter.onSuccess.attach(function(sender, subtractions) {
		dataReductionForm.refresh(subtractions);
		dataReductionForm.panel.setLoading(false);
	});
	adapter.getAbinitioModelsBySubtractionId([ record.subtractionId ]);
};

QueueGrid.prototype.onAbinitioButtonClicked = function(record) {
	var adapter = new BiosaxsDataAdapter();
	var abinitioForm = new AbinitioForm({});

	Ext.create('Ext.window.Window', {
		title : 'Abinitio Modeling',
		height : 630,
		width : 800,
		items : [ abinitioForm.getPanel() ]
	}).show();

	abinitioForm.panel.setLoading();
	adapter.onSuccess.attach(function(sender, subtractions) {
		abinitioForm.refresh(subtractions);
		abinitioForm.panel.setLoading(false);
	});
	adapter.getAbinitioModelsBySubtractionId([ record.subtractionId ]);
};

QueueGrid.prototype.onRigidBodyButtonClicked = function(record) {
	var adapter = new BiosaxsDataAdapter();
	var rigidForm = new RigidBodyModelingResultForm({});

	Ext.create('Ext.window.Window', {
		title : 'Rigid Body Modeling',
		height : 600,
		width : 800,
		items : [ rigidForm.getPanel() ]
	}).show();

	rigidForm.panel.setLoading();
	adapter.onSuccess.attach(function(sender, subtractions) {
		rigidForm.refresh(subtractions);
		rigidForm.panel.setLoading(false);
	});
	adapter.getSubtractionsByIdsList([ record.subtractionId ]);
};

/** It opens the fit window form **/
QueueGrid.prototype.onFitButtonClicked = function(record) {
	var adapter = new BiosaxsDataAdapter();
	var fitForm = new FitForm({});

	Ext.create('Ext.window.Window', {
		title : 'Fit',
		height : 600,
		width : 800,
		items : [ fitForm.getPanel() ]
	}).show();

	fitForm.panel.setLoading();
	adapter.onSuccess.attach(function(sender, subtractions) {
		fitForm.refresh(subtractions);
		fitForm.panel.setLoading(false);
	});
	adapter.getSubtractionsByIdsList([ record.subtractionId ]);
};

/** It opens the superposition window form **/
QueueGrid.prototype.onSuperpositionButtonClicked = function(record) {
	var adapter = new BiosaxsDataAdapter();
	var superpositionForm = new SuperpositionForm({});

	Ext.create('Ext.window.Window', {
		title : 'Superposition',
		height : 620,
		width : 900,
		items : [ superpositionForm.getPanel() ]
	}).show();

	superpositionForm.panel.setLoading();
	adapter.onSuccess.attach(function(sender, subtractions) {
		superpositionForm.refresh(subtractions);
		superpositionForm.panel.setLoading(false);
	});
	adapter.getSubtractionsByIdsList([ record.subtractionId ]);
};

QueueGrid.prototype._openVisualizer = function(experimentList, subtraction, record) {
	var merges = experimentList.getMergesByDataCollectionId(subtraction.dataCollectionId);
	var mergeIdList = [];
	for (var i = 0; i < merges.length; i++) {
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

QueueGrid.prototype.getFramesHTML = function(sample) {
	var dataCollectionId = sample.data.dataCollectionId;
	var table = document.createElement("table");
	
	var measurementId = {};
	
	if (this.key[dataCollectionId] != null) {

		this.key[dataCollectionId].sort(function(a, b) {
			return b.measurementId - a.measurementId
		});

		for (i in this.key[dataCollectionId]) {
			var specimen = this.key[dataCollectionId][i];
			
			if (measurementId[specimen.measurementId] == null){
				measurementId[specimen.measurementId] = true;
				var tr = document.createElement("tr");
				var td = document.createElement("td");
				if (specimen.macromoleculeId == null) {
					td.setAttribute("style", "padding-top:8px;width:130px;color:gray;");
					td.appendChild(document.createTextNode(specimen.bufferAcronym));
				} else {
					td.setAttribute("style", "padding-top:8px;width:130px;font-weight:bold;");
					td.appendChild(document.createTextNode(specimen.macromoleculeAcronym));
				}
				tr.appendChild(td);
				var frames = specimen.framesMerge + "/" + specimen.framesCount;
				var tdFrames = document.createElement("td");
				if ( specimen.framesMerge < 5){
					tdFrames.setAttribute("style", "padding-top:8px;");
				}
				else{
					tdFrames.setAttribute("style", "padding-top:8px;");
				}
				tdFrames.appendChild(document.createTextNode(frames));
				tr.appendChild(tdFrames);
	
				var td = document.createElement("td");
				td.setAttribute("style", "padding-top:8px;");
				var a = document.createElement("a");
				if (specimen.macromoleculeId == null) {
					a.setAttribute("href", BUI.getZipURLByAverageId(specimen.mergeId, specimen.measurementCode));
				} else {
					a.setAttribute("href", BUI.getZipURLBySubtractionId(specimen.subtractionId, specimen.measurementCode));
				}
	
				var img = document.createElement("img");
				img.setAttribute("src", "../images/download.png");
				img.setAttribute("style", "width:12px;height:12px;cursor:pointer;");
				a.appendChild(img);
				td.appendChild(a);
				tr.appendChild(td);
	
				table.appendChild(tr);
			}
		}

	}
	return "<table>" + table.innerHTML + "</table>";
};

QueueGrid.prototype.getRunHTML = function(sample) {
	var dataCollectionId = sample.data.dataCollectionId;
	var table = document.createElement("table");
	
	var measurementId = {};
	if (this.key[dataCollectionId] != null) {

		this.key[dataCollectionId].sort(function(a, b) {
			return b.measurementId - a.measurementId
		});
		

		for (i in this.key[dataCollectionId]) {
			var specimen = this.key[dataCollectionId][i];
			if (measurementId[specimen.measurementId] == null){
				measurementId[specimen.measurementId] = true;
				var tr = document.createElement("tr");
				var td = document.createElement("td");
				if (specimen.macromoleculeId == null) {
					td.setAttribute("style", "padding-top:8px;width:130px;color:gray;");
					td.appendChild(document.createTextNode("# " + specimen.measurementCode));
				} else {
					td.setAttribute("style", "padding-top:8px;width:130px;font-weight:bold;");
					td.appendChild(document.createTextNode("# " + specimen.measurementCode));
				}
				tr.appendChild(td);
				table.appendChild(tr);
			}
		}

	}
	return "<table>" + table.innerHTML + "</table>";
};

QueueGrid.prototype.getColumns = function() {
	var _this = this;
	return [
			{
				header : "dataCollectionId",
				name : "dataCollectionId",
				dataIndex : "dataCollectionId",
				hidden : true
			},
			{
				header : "Exp. Id",
				name : "experimentId",
				dataIndex : "experimentId",
				hidden : true
			},
			{
				header : "Exp. Name",
				name : "name",
				dataIndex : "name",
				hidden : true
			},
			{
				header : "MeasurementId",
				name : "measurementId",
				dataIndex : "measurementId",
				hidden : true,
				renderer : function(val, y, sample) {
					return sample.raw.measurementId;
				}
			},
			{
				header : "Date",
				name : "date",
				width : 150,
				dataIndex : "runCreationDate",
				hidden : true
			},

			{
				header : "Run",
				width : 50,
				name : "runNumber",
				dataIndex : "measurementCode",
				renderer : function(val, y, sample) {
					return _this.getRunHTML(sample);//"<span style='font-weight:bold;'>#" + val + "</span>";
				}
			},
			{
				header : "Frames (Average/Total)",
				dataIndex : "macromoleculeId",
				name : "macromoleculeAcronym",
				width : 175,
				// locked : true,
				hidden : false,
				renderer : function(val, meta, sample) {
					return _this.getFramesHTML(sample);

				}
			},
			{
				text : 'Scattering',
				dataIndex : 'subtractionId',
				width : 70,
				// locked : true,
				name : 'subtractionId',
				renderer : function(val, y, sample) {
					if (sample.raw.macromoleculeId != null) {
						if (sample.raw.subtractionId != null) {
							var url = BUI.getURL() + '&type=scattering&subtractionId=' + sample.raw.subtractionId;
							var event = "OnClick= window.open('" + url + "')";
							return '<img src=' + url + '   height="60" width="60" ' + event + '>';
						}
					}
				}
			},
			{
				header : "Macromolecule",
				name : "macromoleculeAcronym",
				dataIndex : "macromoleculeAcronym",
				flex : 1,
				hidden : true
			},
			{
				header : "Concentration",
				name : "concentration",
				width : 100,
				dataIndex : "concentration",
				hidden : false,
				renderer : function(val, y, sample) {
					if (sample.raw.concentration != 0) {
						return BUI.formatValuesUnits(sample.raw.concentration, "mg/ml", 7, this.decimals);
					}
				}
			},
			{
				header : "Exp. Temp.",
				name : "bufferAcronym",
				width : 75,
				dataIndex : "bufferAcronym",
				hidden : true,
				renderer : function(val, y, sample) {
					return BUI.formatValuesUnits(sample.raw.exposureTemperature, "C", 10, this.decimals);
				}
			},
			{
				header : "Sample",
				dataIndex : "macromoleculeId",
				name : "macromoleculeAcronym",
				locked : false,
				hidden : true,
				renderer : function(val, y, sample) {
					var html = "<table>"
					var macromolecule = BIOSAXS.proposal.getMacromoleculeById(sample.raw.macromoleculeId);
					if (macromolecule != null) {
						html = html + '<tr><td style="vertical-align:center;">' + macromolecule.acronym + '</td></tr>';
					}
					if (sample.raw.bufferId != null) {
						if (BIOSAXS.proposal.getBufferById(sample.raw.bufferId) != null) {
							var bufferAcronym = BIOSAXS.proposal.getBufferById(sample.raw.bufferId).acronym;
							if ((bufferAcronym == "") || (bufferAcronym == null)) {
								bufferAcronym = "";
							}
							html = html + '<tr><td>' + bufferAcronym + '</td></tr>';
						}
					}
					return html + "</table>";
				}
			},
			{
				header : "Frames",
				dataIndex : "macromoleculeId",
				name : "macromoleculeAcronym",
				width : 85,
				locked : false,
				hidden : true,
				renderer : function(val, y, sample) {
					var html = "<table style='width:100%;text-align:center;'>";
					if (sample.raw.runId != null) {
						if (sample.raw.framesMerge != null) {
							/**
							 * Because of some problem on the webservices frames
							 * count and frames average was inverted for some
							 * time, just check that the values are coherent *
							 */
							if (sample.raw.framesCount != null) {
								if (parseFloat(sample.raw.framesCount) < parseFloat(sample.raw.framesMerge)) {
									var aux = sample.raw.framesCount;
									sample.raw.framesCount = sample.raw.framesMerge;
									sample.raw.framesMerge = aux;
								}
							}

							function getColorFrame(framesMerged, total) {
								if (framesMerged / total < 0.5) {
									return "#FA5858";
								}
								if ((framesMerged / total >= 0.5) && (framesMerged / total < 0.8)) {
									return "#FFCC80";
								}
								return null;
							}
							var color = getColorFrame(sample.raw.framesMerge, sample.raw.framesCount);
							if (color) {
								html = html + "<tr style='border:1px solid gray; width:10px;background-color:" + color + ";' ><td >" + sample.raw.framesMerge + " of "
										+ sample.raw.framesCount + "</td>";
							} else {
								html = html + "<tr><td >" + sample.raw.framesMerge + " of " + sample.raw.framesCount + "</td>";
							}
							html = html + "</table>";
							return html;
						}

					}
					if (this.showAveragesHistory) {
						if (sample.raw.averages != null) {
							for (var i = 1; i < sample.raw.averages.length; i++) {
								if (sample.raw.averages[i].framesMerge != null) {
									var f = 'new SubtractionCurveVisualizer().refresh([' + sample.raw.averages[i].mergeId + '], [])';
									html = html + "<tr onmouseover='' style='color:gray;font-style: italic; cursor: pointer;'  onclick='" + f + "'><td >"
											+ sample.raw.averages[i].framesMerge + " of " + sample.raw.averages[i].framesCount + "</td></tr>";
								}
							}
						}
					}
				}
			},
			{
				text : 'Kratky.',
				dataIndex : 'subtractionId',
				width : 66,
				hidden : true,
				name : 'subtractionId',
				renderer : function(val, y, sample) {
					if (sample.raw.macromoleculeId != null) {
						if (sample.raw.subtractionId != null) {
							var url = BUI.getURL() + '&type=kratky&subtractionId=' + sample.raw.subtractionId;
							var event = "OnClick= window.open('" + url + "')";
							return '<img src=' + url + '   height="60" width="60" ' + event + '>';
						}
					}
				}
			},
			{
				text : 'P(r).',
				hidden : true,
				width : 66,
				dataIndex : 'subtractionId',
				type : 'string',
				renderer : function(val, y, sample) {
					if (sample.raw.macromoleculeId != null) {
						if (sample.raw.subtractionId != null) {
							var url = BUI.getURL() + '&type=gnom&subtractionId=' + sample.raw.subtractionId;
							var event = "OnClick= window.open('" + url + "')";
							return '<img src=' + url + '   height="60" width="60" ' + event + '>';
						}
					}
				}
			},
			{
				text : 'Guinier.',
				hidden : true,
				width : 66,
				dataIndex : 'subtractionId',
				type : 'string',
				renderer : function(val, y, sample) {
					if (sample.raw.macromoleculeId != null) {
						if (sample.raw.subtractionId != null) {
							var url = BUI.getURL() + '&type=guinier&subtractionId=' + sample.raw.subtractionId;
							var event = "OnClick= window.open('" + url + "')";
							return '<img src=' + url + '   height="60" width="60" ' + event + '>sdfsdfs</img>';
						}
					}
				}
			},
			{
				text : 'Guinier',
				name : 'Guinier',
				columns : [
						{
							text : 'Rg',
							dataIndex : 'rg',
							name : 'rg',
							width : 80,
							tooltip : 'In polymer physics, the radius of gyration is used to describe the dimensions of a polymer chain.',
							sortable : true,
							renderer : function(val, y, sample) {
								val = sample.raw.rg;
								if (val != null) {
									if (sample.raw.macromoleculeId != null) {
										if (sample.raw.subtractionId != null) {
											/**
											 * Show warning if rgGuinier and
											 * rgGnom differ more than 10% *
											 */
											if (Math.abs(val - sample.raw.rgGnom) > (val * 0.1)) {
												return "<span style='color:orange;'>" + BUI.formatValuesUnits(val, "") + "</span>";

											}
											return BUI.formatValuesUnits(val, "nm", 12, this.decimals);
										}
									}
								}
							}
						},
						{
							text : 'Points',
							dataIndex : 'points',
							sortable : true,
							hidden : false,
							width : 80,
							type : 'string',
							renderer : function(val, y, sample) {
								if (sample.raw.macromoleculeId != null) {
									if (sample.raw.subtractionId != null) {
										if ((sample.raw.firstPointUsed == "") || (sample.raw.firstPointUsed == null))
											return;
										return "<span>" + sample.raw.firstPointUsed + " - " + sample.raw.lastPointUsed + "<br/> ("
												+ (sample.raw.lastPointUsed - sample.raw.firstPointUsed) + " )</span>";
									}
								}
							}
						},
						{
							text : 'I(0)',
							dataIndex : 'I0',
							sortable : true,
							tooltip : 'Extrapolated scattering intensity at zero angle I(0) (forward scattering)',
							width : 80,
							type : 'string',
							renderer : function(val, y, sample) {
								val = sample.raw.I0;
								if (sample.raw.macromoleculeId != null) {
									if (sample.raw.subtractionId != null) {
										if (val != null) {
											return BUI.formatValuesErrorUnitsScientificFormat(val, sample.raw.I0Stdev, "");
										}
									}
								}
							}
						},
						{
							text : 'Quality',
							dataIndex : 'quality',
							hidden : true,
							tooltip : 'Estimated data quality. 1.0 - means ideal quality, 0.0 - unusable data. In table format it is given in percent (100% - ideal quality, 0% - unusable data). Please note that this estimation is based only on the Guinier interval (very low angles).',
							width : 80,
							sortable : true,
							renderer : function(val, y, sample) {
								val = sample.raw.quality;
								if (sample.raw.macromoleculeId != null) {
									if (sample.raw.subtractionId != null) {
										if (val != null) {
											if ((val != null) && (val != "")) {
												return "<span>" + (Number(val)).toFixed(2); // +
												// "</span><span
												// style='font-size:8px;color:gray;'>
												// %</span>";
											}
										}
									}
								}
							}
						}, {
							text : 'Aggregated',
							tooltip : "If aggregation was detected from the slope of the data curve at low angles the value is '1', otherwise '0'.",
							dataIndex : 'isagregated',
							hidden : true,
							width : 80,
							renderer : function(val, y, sample) {
								if (sample.raw.macromoleculeId != null) {
									if (sample.raw.subtractionId != null) {
										if ((sample.raw.isagregated != null)) {
											if (val == true) {
												return "Yes";
											} else {
												return "No";
											}
										}
									}
								}
							}
						} ]
			}, {
				text : 'Gnom',
				name : 'Gnom',
				columns : [ {
					text : 'Rg',
					dataIndex : 'rgGnom',
					type : 'string',
					// flex : 1,
					sortable : true,
					renderer : function(val, y, sample) {
						if (sample.raw.macromoleculeId != null) {
							if (sample.raw.subtractionId != null) {
								/**
								 * Show warning if rgGuinier and rgGnom differ
								 * more than 10% *
								 */
								if (sample.raw.rgGnom != null) {
									if (Math.abs(sample.raw.rgGuinier - sample.raw.rgGnom) > (sample.raw.rgGuinier * 0.1)) {
										return "<span style='color:orange;'>" + BUI.formatValuesUnits(sample.raw.rgGnom, "") + "</span>";

									}
									return BUI.formatValuesUnits(sample.raw.rgGnom, "nm");
								}
							}
						}
					}
				}, {
					text : 'Total',
					dataIndex : 'total',
					width : 60,
					sortable : true,
					renderer : function(val, y, sample) {
						if (sample.raw.macromoleculeId != null) {
							if (sample.raw.subtractionId != null) {
								if (sample.raw.total != null)
									return BUI.formatValuesUnits(sample.raw.total, '');
							}
						}
					}
				}, {
					text : 'D<sup>max</sup>',
					dataIndex : 'dmax',
					width : 70,
					sortable : true,
					renderer : function(val, y, sample) {
						if (sample.raw.macromoleculeId != null) {
							if (sample.raw.subtractionId != null) {
								if (sample.raw.dmax != null)
									return BUI.formatValuesUnits(sample.raw.dmax, "") + "<span style='font-size:8px;color:gray;'> nm</span>";
							}
						}
					}
				}, {
					text : 'P(r)',
					sortable : true,
					hidden : true,
					// flex : 1,
					dataIndex : 'subtractionId',
					type : 'string',
					renderer : function(val, y, sample) {
						if (sample.raw.macromoleculeId != null) {
							if (sample.raw.subtractionId != null) {
								var url = BUI.getURL() + '&type=gnom&subtractionId=' + sample.raw.subtractionId;
								var event = "OnClick= window.open('" + url + "')";
								return '<img src=' + url + '   height="60" width="60" ' + event + '>';
							}
						}
					}
				} ]
			}, this._getPorod(), {
				text : 'Advanced',
				id : this.id + 'buttonAction',
				dataIndex : 'subtrationId',
				witdh : 100,
				renderer : function(val, y, sample) {
					// 'fitCount', 'superposisitionCount', 'rigidbodyCount',
					// 'abinitioCount'
					var html = "<table><tr><td style='padding-bottom: 1px;'>" + BUI.getGreenButton("Data Reduction", {
						width : 90,
						height : 15
					}) + "</td></tr>";

					if (sample.raw.abinitioCount > 0) {
						html = html + "<tr><td style='padding-bottom: 1px;'>" + BUI.getGreenButton("Abinitio", {
							width : 90,
							height : 15
						}) + "</td></tr>";
					} else {
						html = html + "<tr><td style='padding-bottom: 1px;'>" + BUI.getBlueButton("Abinitio", {
							width : 90,
							height : 15
						}) + "</td></tr>";
					}

					if (sample.raw.fitCount > 0) {
						html = html + "<tr><td style='padding-bottom: 1px;'>" + BUI.getGreenButton("Fit", {
							width : 90,
							height : 15
						}) + "</td></tr>";
					} else {
						html = html + "<tr><td style='padding-bottom: 1px;'>" + BUI.getBlueButton("Fit", {
							width : 90,
							height : 15
						}) + "</td></tr>";
					}

					if (sample.raw.superposisitionCount > 0) {
						html = html + "<tr><td style='padding-bottom: 1px;'>" + BUI.getGreenButton("Superposition", {
							width : 90,
							height : 15
						}) + "</td></tr>";
					} else {
						html = html + "<tr><td style='padding-bottom: 1px;'>" + BUI.getBlueButton("Superposition", {
							width : 90,
							height : 15
						}) + "</td></tr>";
					}
					if (sample.raw.rigidbodyCount > 0) {
						html = html + "<tr><td style='padding-bottom: 1px;'>" + BUI.getGreenButton("Rigid Body", {
							width : 90,
							height : 15
						}) + "</td></tr>";
					} else {
						html = html + "<tr><td style='padding-bottom: 1px;'>" + BUI.getBlueButton("Rigid Body", {
							width : 90,
							height : 15
						}) + "</td></tr>";
					}

					return html + "</table>";
				}

			} ]
};

function MacromoleculeConditionGrid(args) {

	this.height = 100;
	this.btnEditVisible = true;
	this.btnRemoveVisible = true;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.btnEditVisible != null) {
			this.btnEditVisible = args.btnEditVisible;
		}
		if (args.btnRemoveVisible != null) {
			this.btnRemoveVisible = args.btnRemoveVisible;
		}
	}

	/** Events **/
	this.onEditButtonClicked = new Event(this);
	this.onAddButtonClicked = new Event(this);
	this.onRemoveButtonClicked = new Event(this);
	this.onDuplicateButtonClicked = new Event(this);
}

MacromoleculeConditionGrid.prototype._getColumns = function() {
	var _this = this;

	function actionItemRenderer(value, meta, record, rowIx, ColIx, store) {
		/*if (record.raw.specimen3VOs.length > 0){
			return 'x-hide-display';
		}*/
	}

	var columns = [ {
		header : 'Code',
		dataIndex : 'code',
		name : 'code',
		type : 'string',
		flex : 1
	}, {
		header : 'Bar Code',
		dataIndex : 'barCode',
		name : 'barCode',
		type : 'string',
		flex : 1,
		hidden : true
	}, {
		header : 'BeamLine',
		dataIndex : 'barCode',
		type : 'string',
		flex : 1,
		renderer : function(comp, val, record) {
			if (record.raw.sessionVO != null) {
				return "<span style='font-weight:bold;'>" + record.raw.sessionVO.beamlineName + "</span>";
			}
		}
	}, {
		header : 'Session',
		dataIndex : 'barCode',
		type : 'string',
		flex : 1,
		renderer : function(comp, val, record) {
			if (record.raw.sessionVO != null) {
				return "<span>" + moment(record.raw.sessionVO.startDate).format("MMM Do YY") + "</span>";
			}
		}
	}, {
		header : 'Status',
		dataIndex : 'dewarStatus',
		name : 'dewarStatus',
		type : 'string',
		flex : 1,
		renderer : function(comp, val, record) {
			if (new String(record.raw.dewarStatus).toUpperCase() == 'OPENED') {
				return "<span style='color:green;font-weight:bold;'>" + new String(record.raw.dewarStatus).toUpperCase() + "</span>";
			}
			return "<span style='font-weight:bold;'>" + new String(record.raw.dewarStatus).toUpperCase() + "</span>";
		}
	}, {
		header : 'Type',
		dataIndex : 'type',
		name : 'type',
		type : 'string',
		flex : 1,
		hidden : true
	}, {
		header : 'Sample Plates',
		dataIndex : 'plates',
		name : 'plates',
		type : 'string',
		flex : 1,
		hidden : true
	}, {
		header : 'Stock Solutions',
		dataIndex : 'plates',
		name : 'plates',
		type : 'string',
		width : 100,
		renderer : function(comp, val, record) {
			var html = "<div>";
			var stockSolutions = BIOSAXS.proposal.getStockSolutionsByDewarId(record.raw.dewarId);
			html = html + "<span style='font-size:18px'>" + stockSolutions.length + "</span> x<img height='15px' src='/ispyb/images/SampleHolder_24x24_01.png'>";
			return html + "</div>";
		}
	}, {
		header : 'isStorageDewar',
		dataIndex : 'isStorageDewar',
		name : 'isStorageDewar',
		type : 'string',
		flex : 1,
		hidden : true
	}, {
		header : 'Tracking Number From Synchrotron',
		dataIndex : 'trackingNumberFromSynchrotron',
		name : 'trackingNumberFromSynchrotron',
		type : 'string',
		flex : 1,
		hidden : true
	}, {
		header : 'Tracking Number To Synchrotron',
		dataIndex : 'trackingNumberToSynchrotron',
		name : 'trackingNumberToSynchrotron',
		type : 'string',
		flex : 1,
		hidden : true
	}, {
		header : 'Storage Location',
		dataIndex : 'storageLocation',
		name : 'storageLocation',
		type : 'string',
		flex : 1,
		hidden : false
	}, {
		header : 'Comments',
		dataIndex : 'comments',
		name : 'comments',
		type : 'string',
		flex : 1,
		hidden : false
	} ];

	if (this.btnEditVisible) {
		columns.push({
			id : _this.id + 'buttonEdit',
			text : '',
			hidden : !_this.btnEditVisible,
			width : 85,
			sortable : false,
			renderer : function(value, metaData, record, rowIndex, colIndex, store) {
				return BUI.getGreenButton('EDIT');
			}
		});
	}

	if (this.btnRemoveVisible) {
		columns.push({
			id : _this.id + 'buttonRemove',
			width : 85,
			sortable : false,
			renderer : function(value, metaData, record, rowIndex, colIndex, store) {
				if (_this.btnRemoveVisible) {
					return BUI.getRedButton('REMOVE');
				}
			}
		});
	}

	columns.push({
		dataIndex : 'comments',
		type : 'string',
		width : 85,
		hidden : false,
		renderer : function(comp, val, record) {
			return BUI.getBlueButton("LABELS", {
				href : BUI.getPrintcomponentURL(record.raw.dewarId)
			});
		}
	});

	return columns;
};

MacromoleculeConditionGrid.prototype._getTopButtons = function() {
	var _this = this;
	/** Actions buttons **/
	var actions = [];

	/** ADD BUTTON **/
	actions.push(Ext.create('Ext.Action', {
		icon : '../images/add.png',
		text : 'Add',
		disabled : false,
		handler : function(widget, event) {
			_this.onAddButtonClicked.notify();
		}
	}));

	return actions;
};

MacromoleculeConditionGrid.prototype.refresh = function(dewars) {
	this.features = dewars;
	this.store.loadData(this._prepareData(dewars), false);
};

MacromoleculeConditionGrid.prototype._sort = function(store) {
	store.sort('dewarId', 'DESC');
};

MacromoleculeConditionGrid.prototype._prepareData = function() {
	var data = [];
	for (var i = 0; i < this.features.length; i++) {
		data.push(this.features[i]);
	}
	return data;
};

MacromoleculeConditionGrid.prototype.getPanel = function() {
	this.grid = Ext.create('Ext.grid.Panel', {
		style : {
			padding : 5
		},
		height : this.height,
		store : this.store,
		columns : this._getColumns(),
		viewConfig : {
			stripeRows : true,
			listeners : {
				itemdblclick : function(dataview, record, item, e) {
					_this._edit(record.raw);
				},
				cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonEdit') {
						_this._edit(record.raw);
					}

					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonRemove') {
						_this.onRemoveButtonClicked.notify(_this.store.getAt(rowIndex).raw.dewarId);
					}
				}
			}
		},
		selModel : {
			mode : 'SINGLE'
		}
	});

	var actions = _this._getTopButtons();
	this.grid.addDocked({
		xtype : 'toolbar',
		items : actions
	});

	this.grid.getSelectionModel().on({
		selectionchange : function(sm, selections) {
			if (selections.length) {
				for (var i = 0; i < actions.length; i++) {
					if (actions[i].enable) {
						actions[i].enable();
					}
				}
			} else {
				for (var i = 0; i < actions.length; i++) {
					if (actions[i].alwaysEnabled == false) {
						if (actions[i].disable) {
							actions[i].disable();
						}
					}
				}
			}
		}
	});
	return this.grid;
};

MacromoleculeConditionGrid.prototype._getStoreFields = function(data) {
	return [ {
		name : 'dewarId',
		type : 'string'
	}, {
		name : 'barCode',
		type : 'string'
	}, {
		name : 'code',
		type : 'string'
	}, {
		name : 'comments',
		type : 'string'
	}, {
		name : 'dewarStatus',
		type : 'string'
	}, {
		name : 'isStorageDewar',
		type : 'string'
	}, {
		name : 'plates',
		type : 'string'
	}, {
		name : 'transportValue',
		type : 'string'
	}, {
		name : 'trackingNumberFromSynchrotron',
		type : 'string'
	}, {
		name : 'trackingNumberToSynchrotron',
		type : 'string'
	}, {
		name : 'timeStamp',
		type : 'string'
	}, {
		name : 'storageLocation',
		type : 'string'
	}, {
		name : 'type',
		type : 'string'
	}

	];
};

MacromoleculeConditionGrid.prototype._renderGrid = function() {
	var _this = this;

	/** Store **/
	var data = this._prepareData();
	this.store = Ext.create('Ext.data.Store', {
		fields : this._getStoreFields(data),
		autoload : true,
		data : data
	});
	this._sort(this.store);

	this.store.loadData(data, false);

	this.grid = Ext.create('Ext.grid.Panel', {
		style : {
			padding : 5
		},
		height : this.height,
		store : this.store,
		columns : this._getColumns(),
		viewConfig : {
			stripeRows : true,
			listeners : {
				itemdblclick : function(dataview, record, item, e) {
					_this._edit(record.raw);
				},
				cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonEdit') {
						_this._edit(record.raw);
					}

					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonRemove') {
						_this.onRemoveButtonClicked.notify(_this.store.getAt(rowIndex).raw.dewarId);
					}
				}
			}
		},
		selModel : {
			mode : 'SINGLE'
		}
	});

	var actions = _this._getTopButtons();
	this.grid.addDocked({
		xtype : 'toolbar',
		items : actions
	});

	this.grid.getSelectionModel().on({
		selectionchange : function(sm, selections) {
			if (selections.length) {
				for (var i = 0; i < actions.length; i++) {
					if (actions[i].enable) {
						actions[i].enable();
					}
				}
			} else {
				for (var i = 0; i < actions.length; i++) {
					if (actions[i].alwaysEnabled == false) {
						if (actions[i].disable) {
							actions[i].disable();
						}
					}
				}
			}
		}
	});
	return this.grid;
};

MacromoleculeConditionGrid.prototype.input = function() {
	return {
		proposal : DATADOC.getProposal_10(),
		dewars : DATADOC.getDewars_10()

	};
};

MacromoleculeConditionGrid.prototype.test = function(targetId) {
	var MacromoleculeConditionGrid = new MacromoleculeConditionGrid({
		height : 150
	});
	BIOSAXS.proposal = new Proposal(MacromoleculeConditionGrid.input().proposal);
	var panel = MacromoleculeConditionGrid.getPanel(MacromoleculeConditionGrid.input().dewars);
	panel.render(targetId);

};