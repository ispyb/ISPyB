
/**
 *  Shows a list of experiment AKA data acquisitions
 *	@height
 *	@sorters 
 *	@minHeight
 *	@gridType: Ext.ux.LiveSearchGridPanel or Ext.grid.Panel
 *	@tbar true or false
 *	@grouping true or false
 *	@width
 *	@title
 *	#onEditButtonClicked
 */
function ExperimentGrid(args) {
	this.width = "100%";
	this.height = 700;
	this.minHeight = 500;

	this.id =  BUI.id();
	this.gridType = 'Ext.grid.Panel'; //'Ext.ux.LiveSearchGridPanel';
	this.tbar = false;
	this.hideHeaders = false;
	this.grouping = true;
	this.title = null;

	this.filtered = null;
	
	/** maximum row count **/
	this.limit = 100;
	
	this.sessionIdFilter = null;
	
	/** if not null filtered by date **/
	this.date = null;
	
	this.sorters = [ {
		property : 'date',
		direction : 'DESC'
	}, {
		property : 'time',
		direction : 'DESC'
	} ];

	this.dates = {};
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.limit != null) {
			this.limit = args.limit;
		}
		if (args.sorters != null) {
			this.sorters = args.sorters;
		}
		if (args.sessionId != null){
			this.sessionIdFilter = args.sessionId;
		}
		if (args.filtered != null) {
			this.filtered = args.filtered;
		}
		if (args.minHeight != null) {
			this.minHeight = args.minHeight;
		}
		if (args.gridType != null) {
			this.gridType = args.gridType;
		}
		if (args.tbar != null) {
			this.tbar = args.tbar;
		}
		if (args.grouping != null) {
			this.grouping = args.grouping;
		}
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.title != null) {
			this.title = args.title;
		}

	}
	/** Events **/
	this.onEditButtonClicked = new Event(this);
}

ExperimentGrid.prototype._getFilterTypes = function() {
	return [];
};

ExperimentGrid.prototype._prepareData = function(rows) {
	var data = [];
	var count = 0;
	
	rows.sort(function(a,b){return b.experimentId - a.experimentId;});
	for ( var i = 0; i < rows.length; i++) {
	var row = rows[i];
				this.dates[moment(row.creationDate).format("YYYYMMDD")] = moment(row.creationDate).format("MMM Do YY");
				if (
						  ( this.filtered == null || row.experimentType == this.filtered ) && (count < this.limit || this.limit == null )	&& (this.sessionIdFilter == null || this.date != null || (this.date == null && this.sessionIdFilter == row.sessionId)) 
					){

					data.push({
						experimentId : row.experimentId,
						status : row.status,
						dataAcquisitionFilePath : row.dataAcquisitionFilePath,
						type : row.experimentType,
						name : row.name,
						macromolecules_names : row.macromolecules,
						percentageAnalysed : {
							value : (row.dataCollectionDoneCount / row.dataCollectionCount) * 100,
							text : row.dataCollectionDoneCount + " of " + row.dataCollectionCount
						},
						percentageCollected : {
							value : (row.measurementDoneCount / row.measurementCount) * 100,
							text : row.measurementDoneCount + " of " + row.measurementCount
						},
						percentageMerged : {
							value : (row.measurementAveragedCount / row.measurementCount) * 100,
							text : row.measurementAveragedCount + " of " + row.measurementCount
						},
						date : moment(row.creationDate).format("YYYYMMDD"),
						time : moment(row.creationDate).format("YYYYMMDDHHmmss"),
						creationDate : row.creationDate
					});
					count ++;
		}
	}
	return data;
};

ExperimentGrid.prototype.getPanel = function(experiments) {
	this.features = experiments;
	return this._renderGrid(experiments);
};

ExperimentGrid.prototype.refresh = function(experiments) {
	this.experiments = experiments;
	var filtered = [];
	for ( var i = 0; i < experiments.length; i++) {
		if (experiments[i].experimentType != "TEMPLATE") {
			filtered.push(experiments[i]);
		}
	}

	this.parsedData = this._prepareData(filtered);
	
	var day = {};
	var dates = [];
	for ( var i = 0; i < this.experiments.length; i++) {
		var date  =  moment(this.experiments[i].creationDate).format("MMM Do YYYY");
		if (day[date] == null){
			dates.push({
				date : date,
				value :  moment(this.experiments[i].creationDate)
			});
			day[date] = true;
		}
	}
	
	this.storeDate.loadData(dates, false);
	this.store.loadData(this.parsedData, false);
	
	/** If it has already been filtered by date we keep the filter **/
	if (this.date != null){
		this._filterByDate(this.date);
	}
};

ExperimentGrid.prototype._getTopButtons = function() {
	var _this = this;
	/** Actions buttons **/
	var actions = [];

	actions.push(Ext.create('Ext.Action', {
		icon : '../images/calendar_icon.png',
		text : 'Show Calendar',
		disabled : false,
		handler : function(widget, event) {
			var window = Ext.create('Ext.window.Window', {
				title : 'Calendar',
				width : 600,
				height : 600,
				modal : true,
				closable : true,
				layout : {
					type : 'vbox',
					align : 'stretch'
				},
				items : [ {
					xtype : 'label',
					html : 'Click on a data acquisition to select:',
					margin : '5 5 5 5'
				}, {
					html : '<div id="calendar" style="height:450px;overflow-y: scroll;"></div>',
					margin : '5 5 5 5'
				}

				]
			}).show();

			var calendarWidget = new CalendarWidget({
				height : 450
			});
			var aux = _this.limit;
			/** we remove the limit temporarily **/
			_this.limit = null;
			_this.sessionIdFilter = null;
			calendarWidget.loadData(_this._prepareData(_this.experiments));
			_this.limit = aux;
			calendarWidget.draw('calendar');
			calendarWidget.onClick.attach(function(sender, date) {
				date = moment(date, "YYYY-MM-DD");
				_this._filterByDate(date);
				window.close();
				
			});

		}
	}));
	this.storeDate =  Ext.create('Ext.data.ArrayStore', {
		        fields: ['date', 'value'],
		        data : []
		    });
	
	this.dateMenu = Ext.create('Ext.form.field.ComboBox', {
	        hideLabel: true,
	        store: this.storeDate,
	        displayField: 'date',
	        typeAhead: true,
	        queryMode: 'local',
	        margin : '0 0 0 30',
	        triggerAction: 'all',
	        emptyText:'Select a date...',
	        selectOnFocus:true,
	        width:135,
	        listeners:{
	            scope: this,
	            'select': function (a,b,c){
	            	_this.limit = null;
	            	_this._filterByDate(moment(b[0].raw.value, "YYYY-MM-DD"));
	            }
	       }
	 });
	 
	actions.push(this.dateMenu);
	
	actions.push("->");
	if (_this.filtered != null){
		actions.push({
			html	: "<span>Experiment Type: " + _this.filtered +"</span>"
		});
	}
	else{
		actions.push({
			html	: "<span>Experiment Type: ALL</span>"
		});
	}
	return actions;
};

/**
 * Date format: "YYYY-MM-DD"
 */
ExperimentGrid.prototype._filterByDate = function(date) {
	var experimentsFiltered = [];
	/** Getting all the experiments of date**/
	for ( var i = 0; i < this.experiments.length; i++) {
		var experiment = this.experiments[i];
		if (experiment.creationDate != null) {
			var experimentDate = moment(experiment.creationDate);
			if (experimentDate.year() == date.year()) {
				if (experimentDate.month() == date.month()) {
					if (experimentDate.date() == date.date()) {
						experimentsFiltered.push(experiment);
					}
				}
			}
		}
	}
	var parsedData = this._prepareData(experimentsFiltered);
	this.store.loadData(parsedData);
	this.date = date;
};

/** Only for templates **/
ExperimentGrid.prototype._removeExperimentById = function(experimentId) {
	var _this = this;
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(evt, args) {
		_this.grid.setLoading(false);
		document.getElementById(BIOSAXS.targetId).innerHTML = "";
		BIOSAXS.start(BIOSAXS.targetId);
	});
	this.grid.setLoading("Removing experiment ");
	adapter.removeExperimentById(experimentId);
};

ExperimentGrid.prototype._renderGrid = function() {
	var _this = this;

	/** Store **/
	this.store = Ext.create('Ext.data.Store', {
		fields : this._getColumns(), 
		groupField : 'date',
		autoload : true,
		data : [],
		remoteSort: false,
		sorters : this.sorters
	});

	
	
	
	var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
		groupHeaderTpl : Ext.create('Ext.XTemplate',
				"<div style='background:#0ca3d2; color:white; float:left; margin:6px 8px 0 0; padding:5px 8px;'>{name:this.formatName}</div>", {
					formatName : function(name) {
						return _this.dates[name];
					}
				}),
		hideGroupedHeader : true,
		startCollapsed : false
	});

	this.features = [];
	if (this.grouping) {
		this.features.push(groupingFeature);
	}

	/** Grid **/
	this.grid = Ext.create(this.gridType, {
		hideHeaders : this.hideHeaders,
		resizable : true,
		title : this.title,
		width : this.width,
		minHeight : this.minHeight,
		height : this.height,
		features : this.features,
		store : this.store,
		columns : this._getColumns(),
		selModel : {
			mode : 'SINGLE'
		},
		viewConfig : {
			stripeRows : true,
			getRowClass : function(record, rowIdx, params, store) {
				if (record.raw.type == "TEMPLATE") {
					return "template-color-row";
				}
				if ((record.raw.type == "CALIBRATION") && (record.raw.status == "FINISHED")) {
					return "blue-row";
				}
			},
			listeners : {
				itemdblclick : function(dataview, record, item, e) {
					_this._editExperiment(record.raw.experimentId);
				},
				cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'GO') {
						_this._editExperiment(record.raw.experimentId);
					}

					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'REMOVE') {
						_this._removeExperimentById(record.raw.experimentId);
					}

				}
			}
		}
	});

	var actions = _this._getTopButtons();

	if (this.tbar) {
		this.grid.addDocked({
			xtype : 'toolbar',
			items : actions
		});
		this.grid.getSelectionModel().on({
			selectionchange : function(sm, selections) {
				if (selections.length) {
					for ( var i = 0; i < actions.length; i++) {
						if (actions[i].enable) {
							actions[i].enable();
						}
					}
				} else {
					for ( var i = 0; i < actions.length; i++) {
						if (actions[i].alwaysEnabled == false) {
							if (actions[i].disable) {
								actions[i].disable();
							}
						}
					}
				}
			}
		});
	}

	return this.grid;
};

ExperimentGrid.prototype._getColumns = function() {
	var _this = this;
	function actionItemRenderer(value, meta, record, rowIx, ColIx, store) {
		if (record.raw.buffer3VOs != null) {
			if (record.raw.buffer3VOs.length > 0) {
				return 'x-hide-display';
			}
		}

		if (record.data.platesCount > 0) {
			return 'x-hide-display';
		}
	}

	return [
		{
			text : 'experimentId',
			dataIndex : 'experimentId',
			name : 'experimentId',
			type : 'string',
			hidden : true
		},
		{
			xtype : 'rownumberer',
			width : 40
		},
		{
			text : 'Name',
			dataIndex : 'name',
			name : 'name',
			type : 'string',
			flex : 1
		},

		{
			text : 'Type',
			dataIndex : 'type',
			name : 'type',
			type : 'string',
			flex : 1,
			renderer : function(val) {
				if (val == "CALIBRATION") {
					return "<span style='color:blue;'>" + val + "</span>";
				}

				return val;
			}
		},
		{
			text : 'Macromolecules',
			name : 'macromolecules_names',
			dataIndex : 'macromolecules_names',
			flex : 1,
			renderer : function(val) {
				if (val != null) {
					return " <span style='font-weight:bold'>" + val + "</span>";
				}
				return " <span style='font-style:italic;color:gray;'>Information not available</span>";
			}
		},
		{
			text : 'Buffers',
			dataIndex : 'buffer_names',
			name : 'buffer_names',
			flex : 1,
			hidden : true,
			renderer : function(val) {
				return "Buffer/s: <span style='font-weight:bold'>" + val + "</span>";
			}
		},
		{
			text : 'Status',
			dataIndex : 'status',
			name : 'status',
			type : 'string',
			flex : 1,
			renderer : function(val, x, sample) {
				if (sample.raw.type == "TEMPLATE") {
					return "READY";
				}
				if (sample.raw.status == "ABORTED") {
					return "<span style='color:red;'>" + val + "</span>";
				}
				return "<span style='color:green;'>" + val + "</span>";
			}
		},
		{
			text : 'Download',
			dataIndex : 'creationDate',
			name : 'creationDate',
			renderer : function(val, x, sample) {
				if (sample != null) {
					if (sample.raw.type == "HPLC") {
						return;
					}
					return BUI.getZipHTMLByExperimentId(sample.raw.experimentId, sample.raw.name);
				}
			},
			width : 100

		},
		{
			header : 'Measurements',
			dataIndex : 'percentageCollected',
			name : 'percentageCollected',
			type : 'string',
			renderer : function(val, y, sample) {
				if ((sample.raw.type == "TEMPLATE") || (sample.raw.type == "HPLC")) {
					return;
				}
				return "<table><tr><td>" + BUI.getProgessBar(sample.raw.percentageCollected.value, sample.raw.percentageCollected.text) + "</td></table>";
			},
			width : 100
		},
		{
			header : 'Averaged',
			dataIndex : 'percentageMerged',
			name : 'percentageMerged',
			type : 'string',
			renderer : function(val, y, sample) {
				if ((sample.raw.type == "TEMPLATE") || (sample.raw.type == "HPLC")) {
					return;
				}
				return "<table><tr><td>" + BUI.getProgessBar(sample.raw.percentageMerged.value, sample.raw.percentageMerged.text) + "</td></table>";
			},
			width : 100
		},
		{
			header : 'Subtractions',
			dataIndex : 'percentageAnalysed',
			name : 'percentageAnalysed',
			type : 'string',
			renderer : function(val, y, sample) {
				if ((sample.raw.type == "TEMPLATE") || (sample.raw.type == "HPLC")) {
					return;
				}
				return "<table><tr><td>" + BUI.getProgessBar(sample.raw.percentageAnalysed.value, sample.raw.percentageAnalysed.text) + "</td></table>";
			},
			width : 100
		},

		{
			text : 'time',
			dataIndex : 'time',
			name : 'time',
			hidden : true,
			renderer : function(val) {
				return val;
			},
			width : 100

		}, {
			text : 'Date',
			dataIndex : 'date',
			name : 'date',
			renderer : function(val) {
				return val;
			},
			width : 100

		},

		{
			text : 'Time',
			dataIndex : 'creationDate',
			name : 'creationDate',
			renderer : function(val) {
				return moment(val).format(" HH:mm:ss");
			},
			width : 100

		}, {
			id : _this.id + 'GO',
			width : 80,
			sortable : false,
			renderer : function(value, metaData, record, rowIndex, colIndex, store) {
				return BUI.getGreenButton('GO');
			}
		} ];
};

/** Changes location.href in order to edit the experiment **/
ExperimentGrid.prototype._editExperiment = function(experimentId) {
	if (Ext.urlDecode(window.location.href).sessionId != null) {
		location.href = 'viewProjectList.do?reqCode=display&experimentId=' + experimentId + '&sessionId='+ Ext.urlDecode(window.location.href).sessionId;
	} else {
		location.href = 'viewProjectList.do?reqCode=display&experimentId=' + experimentId;
	}
};

ExperimentGrid.prototype.input = function() {
	var experiments = DATADOC.getExperimentList_10();
	return {
		experiments : experiments,
		proposal : new MeasurementGrid().input().proposal

	};
};

ExperimentGrid.prototype.test = function(targetId) {
	var experimentGrid = new ExperimentGrid({
		height : 350,
		minHeight : 350,
		width : 1000

	});
	BIOSAXS.proposal = new Proposal(experimentGrid.input().proposal);
	var panel = experimentGrid.getPanel(experimentGrid.input().experiments);
	experimentGrid.refresh(experimentGrid.input().experiments);
	panel.render(targetId);
};
