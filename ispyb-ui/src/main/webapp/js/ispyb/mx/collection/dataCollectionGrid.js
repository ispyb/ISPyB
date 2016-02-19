/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * ISPyB is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P.
 * Brenchereau, M. Bodin
 ******************************************************************************/

/*global Event */
/*global Ext */
/*global document */
/*global contextPath */

// dataCollection Grid, used in the worklfow page, dataCollection page, lastCollect page (manager)
function DataCollectionGrid(args) {

	var contextPath = "";

	this.pageSize = 100;
	this.title = "Last Data Collection";
	this.listOfCrystalClass = [];
	this.contextPath = "";
	this.displayLastCollect = false;
	this.isManager = false;
	this.isIndustrial = false;
	this.pagingMode = false;
	this.groupFieldDefault = '';
	this.setArgs(args);
	// Events
	this.onSaveButtonClicked = new Event(this);
	this.onPagingMode = new Event(this);
	this.onRankButtonClicked = new Event(this);
	this.onRankAutoProcButtonClicked = new Event(this);

	this.hideCol = true;
	this.rankEDNA = true;

	this.minDCId = 1;
	this.maxDCId = 0;
	
	this.rMergeCutoff = 10;
	this.iSigmaCutoff = 1;
	
	// dataCollection data model
	Ext.define('DataCollectionModel', {
			extend : 'Ext.data.Model',
			fields : [{
					name : 'dataCollectionId',
					mapping : 'dataCollectionId'
				}, {
					name : 'experimentType',
					mapping : 'experimentType'
				}, {
					name : 'startTime',
					mapping : 'startTime',
					type : 'date',
					dateFormat : 'd-m-Y H:i:s'
				}, {
				    name : 'endTime',
					mapping : 'endTime',
					type : 'date',
					dateFormat : 'd-m-Y H:i:s'
				}, {
					name : 'beamLineName',
					mapping : 'beamLineName'
				}, {
					name : 'proposal',
					mapping : 'proposal'
				}, {
					name : 'login',
					mapping : 'login'		
				}, {
					name : 'imagePrefix',
					mapping : 'imagePrefix'
				}, {
					name : 'dataCollectionNumber',
					mapping : 'dataCollectionNumber'
				}, {
					name : 'proteinAcronym',
					mapping : 'proteinAcronym'
				}, {
					name : 'pdbFileName',
					mapping : 'pdbFileName'
				}, {
					name : 'numberOfImages',
					mapping : 'numberOfImages'
				}, {
					name : 'hasSnapshot',
					mapping : 'hasSnapshot'
				}, {
					name : 'runStatus',
					mapping : 'runStatus'
				}, {
					name : 'autoProc',
					mapping : 'autoProc'
				}, {
					name : 'autoProcStatisticsOverall',
					mapping : 'autoProcStatisticsOverall'
				}, {
					name : 'autoProcStatisticsInner',
					mapping : 'autoProcStatisticsInner'
				}, {
					name : 'autoProcStatisticsOuter',
					mapping : 'autoProcStatisticsOuter'
				}, {
					name : 'screeningOutput',
					mapping : 'screeningOutput'
				}, {
					name : 'lattice',
					mapping : 'lattice'
				}, {
					name : 'comments',
					mapping : 'comments'
				}, {
					name : 'autoprocessingStatus',
					mapping : 'autoprocessingStatus'
				}, {
					name : 'autoprocessingStep',
					mapping : 'autoprocessingStep'
				}, {
					name : 'wavelength',
					mapping : 'wavelength'
				}, {
					name : 'transmission',
					mapping : 'transmission'
				}, {
					name : 'exposureTime',
					mapping : 'exposureTime'
				}, {
					name : 'axisStart',
					mapping : 'axisStart'
				}, {
					name : 'axisRange',
					mapping : 'axisRange'
				}, {
					name : 'resolution',
					mapping : 'resolution'
				}, {
					name : 'transmission',
					mapping : 'transmission'
				}, {
					name : 'hasAutoProcAttachment',
					mapping : 'hasAutoProcAttachment'
				}, {
					name : 'printableForReport',
					mapping : 'printableForReport'
				}, {
					name : 'skipForReport',
					mapping : 'skipForReport'
				}],
			idProperty : 'dataCollectionId'
		});
}

// args passed to the constructor
DataCollectionGrid.prototype.setArgs = function (args) {
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.title != null) {
			this.title = args.title;
		}
		if (args.listOfCrystalClass != null) {
			this.listOfCrystalClass = args.listOfCrystalClass;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.displayLastCollect != null) {
			this.displayLastCollect = args.displayLastCollect;
		}
		if (args.isManager != null) {
			this.isManager = args.isManager;
		}
		if (args.pagingMode != null) {
			this.pagingMode = args.pagingMode;
		}
		if (args.isIndustrial != null) {
			this.isIndustrial = args.isIndustrial;
		}
		if (args.groupFieldDefault != null) {
			this.groupFieldDefault = args.groupFieldDefault;
		}
		if (args.iSigmaCutoff != null) {
			this.iSigmaCutoff = args.iSigmaCutoff;
		}
		if (args.rMergeCutoff != null) {
			this.rMergeCutoff = args.rMergeCutoff;
		}
		
	}
};

// sort by default
DataCollectionGrid.prototype._sort = function () {
	this.store.sort('startTime', 'DESC');
};


// get grids
DataCollectionGrid.prototype.getGrid = function (dataCollections) {
	this.features = dataCollections;
	return this.renderGrid(dataCollections);
};

// refresh data: reload
DataCollectionGrid.prototype.refresh = function (data) {
	this.features = data.dataCollectionList;
	this.store.loadData(this.features, false);
	this.title = "Last " + this.features.length + " DataCollections";
	this.grid.setTitle(this.title);
};

// build the grid
DataCollectionGrid.prototype.renderGrid = function () {
	var _this = this;

	// min & max ID collect
	if (this.features && this.features.length > 0) {
		this.minDCId = this.features[0].dataCollectionId;
		this.maxDCId = this.features[0].dataCollectionId;
		var fl = this.features.length;
		for (var i = 0; i < fl; i++) {
			var id = this.features[i].dataCollectionId;
			if (id > this.maxDCId) {
				this.maxDCId = id;
			}
			if (id < this.minDCId) { 
				this.minDCId = id;
			}
		}
	}
	/** Prepare data * */
	var data = [];
	// build columns
	var columns = this._getColumns();
	
	
	// grid data
	if (this.pagingMode) {
		this.store = Ext.create('Ext.data.Store', {
				model : 'DataCollectionModel',
				autoload : false,
				pageSize : this.pageSize,
				proxy : {
					type : 'pagingmemory',
					data : this.features,
					reader : {
						type : 'array'
					}
				}
			});
	} else if (_this.groupFieldDefault) {
		this.store = Ext.create('Ext.data.Store', {
				model : 'DataCollectionModel',
				autoload : false,
				groupField: _this.groupFieldDefault
			});
	} else {
		this.store = Ext.create('Ext.data.Store', {
			model : 'DataCollectionModel',
			autoload: false
		});
	}
	
	// bottom bar
	var bbar = null;
	var pagingButtonText = "Pagination";
	if (this.pagingMode) {
		pagingButtonText = "Remove pagination";
		bbar = Ext.create('Ext.PagingToolbar', {
				store : this.store,
				pageSize : this.pageSize,
				displayInfo : true,
				displayMsg : 'Displaying dataCollections {0} - {1} of {2}',
				emptyMsg : "No dataCollection to display",
				listeners : {
					afterrender : function () {
						var a = Ext.query("button[data-qtip=Refresh]");
						for (var x = 0; x < a.length; x++) {
							a[x].style.display = "none";
						}
					}
				}
			});

	}

	// this._sort(this.store);
	this.store.loadData(this.features, false);
	// set title
	if (this.displayLastCollect) {
		this.title = "Last " + this.features.length + " DataCollections";
	} else {
		this.title = this.features.length + " DataCollections";
	}

	// this grid can be edited
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});

	// grouping grid
	_this.groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
		groupHeaderTpl : '{columnName}: {name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
		hideGroupedHeader : true,
		startCollapsed : false,
		id : 'dataCollectionGrouping'
	});
	var groups = this.store.getGroups();
	var len = groups.length;
	var toggleGroup = function (item) {
		var groupName = item.text;
		if (item.checked) {
			_this.groupingFeature.expand(groupName, true);
		} else {
			_this.groupingFeature.collapse(groupName, true);
		}
	};

	// Define the model for a rank
	Ext.regModel('Rank', {
			fields : [{
					type : 'string',
					name : 'name'
				}]
		});
	var ranks = [{
			"name" : "EDNA"
		}, {
			"name" : "AutoProc"
		}];
	// The data store holding the ranks
	var rankStore = Ext.create('Ext.data.Store', {
			model : 'Rank',
			data : ranks
		});

	// Simple ComboBox using the data store
	var rankCombo = Ext.create('Ext.form.field.ComboBox', {
			displayField : 'name',
			width : 100,
			labelWidth : 10,
			store : rankStore,
			queryMode : 'local',
			typeAhead : true,
			forceSelection : true,
			value : 'EDNA',
			listeners : {
				change : function (field, newValue, oldValue) {
					_this.changeRank();
				}
			}
		});

	// top bar with buttons
	var tbar = [];
	if (!this.displayLastCollect) {
		tbar.push({
				text : 'Save',
				tooltip : 'Save dataCollections: comments and skip status',
				icon : '../images/save.gif',
				handler : function () {
					_this.onSaveButtonClicked.notify({});
				}
			});
		tbar.push({
				text : 'Experiment Parameters',
				tooltip : 'Expand or Collapse Experiment Parameter',
				icon : '../images/Info_16x16_01.png',
				handler : function () {
					_this.hideAndShowExperimentParameter();
				}
			});
		tbar.push({
				text : 'Rank',
				tooltip : 'Start Sample Ranking',
				handler : function () {
					_this.startRank();
				}
			}, rankCombo);
	}

	if (this.displayLastCollect) {
		tbar.push({
				text : pagingButtonText,
				handler : function () {
					_this.onPagingMode.notify();
				}
			});
	}
	tbar.push('->');
	tbar.push({
		text : 'Expand All',
		handler : function () {
			var rows = _this.groupingFeature.view.getEl().query('.x-grid-group-body');

			Ext.each(rows, function (row) {
						_this.groupingFeature.expand(Ext.get(row));
					});
		}
	}, {
		text : 'Collapse All',
		handler : function () {
			var rows = _this.groupingFeature.view.getEl().query('.x-grid-group-body');

			Ext.each(rows, function (row) {
						_this.groupingFeature.collapse(Ext.get(row));
					});
		}
	});
	tbar.push({
			text : 'Clear Grouping',
			iconCls : 'icon-clear-group',
			handler : function () {
				_this.groupingFeature.disable();
			}
		});

	// Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	
	// grid creation
	this.grid = Ext.create('Ext.grid.Panel', {
		style : {
			padding : 0
		},
		width : "100%",
		height : "100%",
		model : 'DataCollectionModel',
		store : this.store,
		resizable : true,
		//height:800,
		columns : columns,
		title : this.title,
		viewConfig : {
			stripeRows : true,
			enableTextSelection : true
		},
		selModel : {
			mode : 'SINGLE'
		},
		plugins : [cellEditing],
		features : [_this.groupingFeature],

		tbar : tbar,
		// paging bar on the bottom
		bbar : bbar
			// stateId: "dataCollectionGridStateId", // this is unique state ID
			// stateful: true, // state should be preserved
			// stateEvents: ['columnresize', 'columnmove', 'show', 'hide' ] //
			// which event should cause store state
	});
	if (this.pagingMode) {
		this.store.load();
	}
	// hideCol
	// this._hideCol();
	//this._sort(this.store);
	return this.grid;
};

// returns the index of the column, -1 if not found
DataCollectionGrid.prototype._getIndex = function (dataIndex) {
	var gridColumns = this.grid.columns;
	var gl = gridColumns.length;
	for (var i = 0; i < gl; i++) {
		if (gridColumns[i].dataIndex == dataIndex) {
			return i;
		}
	}
	return -1;
};

// hide the columns experiments
DataCollectionGrid.prototype._hideCol = function () {
	if (!this.displayLastCollect) {
		var id = this._getIndex("experimentParameters");
		if (id != -1) {
			this.grid.columns[id].setVisible(this.hideCol);
		}
		// We use 'Wavelength' and 'Detector Resolution' as start and stop
		// points and hide everything in between.
		var id0 = this._getIndex("wavelength");
		var id1 = this._getIndex("resolution");
		if (id0 != -1 && id1 != -1) {
			var nb = id1 - id0 + 1;
			for (var i = 0; i < nb; i++) {
				this.grid.columns[id0 + i].setVisible(!this.hideCol);
			}
		}
	}
};

// build the columns
DataCollectionGrid.prototype._getColumns = function () {
	var _this = this;
	// render functions
	
	// render date
	function renderStartDate(value, p, record) {
		var time = Ext.Date.format(value, 'H:i:s');
		var date = Ext.Date.format(value, 'd-m-Y');
		return time + "<br />" + date;
	}

	// render image prefix with link
	function renderImagePrefix(value, p, record) {
		return '<div style="white-space:normal !important;word-wrap: break-word">' + '<a href="' +
		 _this.contextPath +
		'/user/viewResults.do?reqCode=display&dataCollectionId=' + 
		record.getId() + '">' + value + '</a>' + '</div>';
	}

	// render protein acronym
	function renderProteinAcronym(value, p, record) {
		if (record.data.pdbFileName && record.data.pdbFileName !== "") {
			p.tdAttr = 'data-qtip="' + 
			("pdb file uploaded:" + record.data.pdbFileName) + '"';
			return "<img src='../images/attach.png' border='0'  />" + value;
		} else {
			return value;
		}
	}

	// format nb with 2 digits
	function renderFixed2(value, p, record) {
		if (value) { 
			return value.toFixed(2);
		}
		else {
			return "";
		}
	}

	// format nb with 3 digits
	function renderFixed3(value, p, record) {
		if (value) { 
			return value.toFixed(3);
		}
		else { 
			return "";
		}
	}

	// return the message for status
	function statusPopup(icon1, mess1, icon2, mess2, icon3, mess3, icon4, mess4) {
		var popupMessage = "";
		popupMessage += "<TABLE cellSpacing=2 cellPadding=0 width=100% border=0>";
		popupMessage += "<TR><TD valign=middle><img src='" + icon1 +
			"' border=0\/><\/TD><TD class='smallText_black' nowrap>" + mess1 + "<\/TD><\/TR>";
		popupMessage += "<TR><TD valign=middle><img src='" + icon2 +
			"' border=0\/><\/TD><TD class='smallText_black' nowrap>" + mess2 + "<\/TD><\/TR>";
		popupMessage += "<TR><TD valign=middle><img src='" + icon3 +
			"' border=0\/><\/TD><TD class='smallText_black' nowrap>" + mess3 + "<\/TD><\/TR>";
		popupMessage += "<TR><TD valign=middle><img src='" + icon4 +
		"' border=0\/><\/TD><TD class='smallText_black' nowrap>" + mess4 + "<\/TD><\/TR>";
		popupMessage += "<\/TABLE>";
		return popupMessage;
	}
	
	// render status with balls
	function renderStatus(value, p, record) {
		var icon1 = '../images/Sphere_White_12.png';
		var icon2 = '../images/Sphere_White_12.png';
		var icon3 = '../images/Sphere_White_12.png';
		var icon4 = '../images/Sphere_White_12.png';
		var mess1 = "";
		var mess2 = "";
		var mess3 = "";
		var mess4 = "";

		// crystal snapshot
		if (record.data.hasSnapshot && record.data.hasSnapshot) {
			icon1 = "../images/Sphere_Green_12.png";
			mess1 = "This collection has a crystal snapshot.";
		} else {
			icon1 = "../images/Sphere_White_12.png";
			mess1 = "This collection has no crystal snapshot.";
		}
		// run status
		if (record.data.runStatus) {
			mess2 = record.data.runStatus;
			if (record.data.runStatus.search("successful") != -1) {
				icon2 = "../images/Sphere_Green_12.png";
			} else if (record.data.runStatus.search("failed") != -1) {
				icon2 = "../images/Sphere_Red_12.png";
			} else if (record.data.runStatus.search("Running") != -1) {
				icon2 = "../images/Sphere_Orange_12.png";
			} else {
				icon2 = "../images/Sphere_Yelow_12.png";
			}
		}
		// EDNA status
		if (record.data.screeningOutput) {
			if (record.data.screeningOutput.indexingSuccess) {
				if (record.data.screeningOutput.strategySuccess) {
					if (record.data.screeningOutput.indexingSuccess == 1) {
						if (record.data.screeningOutput.strategySuccess == 1) {
							icon3 = "../images/Sphere_Green_12.png";
							mess3 = "Indexing is successful.";
						} else {
							icon3 = "../images/Sphere_Orange_12.png";
							mess3 = "Indexing is successful but no strategy.";
						}
					} else {
						icon3 = "../images/Sphere_Red_12.png";
						mess3 = "Indexing failed.";
					}
				} else {
					icon3 = "../images/Sphere_Yelow_12.png";
					mess3 = "Indexing status unknown.";
				}
			} else {
				icon3 = "../images/Sphere_Yelow_12.png";
				mess3 = "Indexing status unknown.";
			}
		} else {
			icon3 = "../images/Sphere_White_12.png";
			mess3 = "Indexing has not been launched.";
		}
		// Autoprocessing status
		if (record.data.autoprocessingStatus) {
			if (record.data.autoprocessingStatus == "Successful") {
				icon4 = "../images/Sphere_Green_12.png";
				mess4 = "Autoprocessing: " + record.data.autoprocessingStep + " Successful";
			} else if (record.data.autoprocessingStatus == "Launched") {
				icon4 = "../images/Sphere_Orange_12.png";
				mess4 = "Autoprocessing: " + record.data.autoprocessingStep + " Launched";
			} else if (record.data.autoprocessingStatus == "Failed") {
				icon4 = "../images/Sphere_Red_12.png";
				mess4 = "Autoprocessing: " + record.data.autoprocessingStep + " Failed";
			}
		} else {
			icon4 = "../images/Sphere_White_12.png";
			mess4 = "Autoprocessing status unknown.";
		}
		//
		var myToolTipText = statusPopup(icon1, mess1, icon2, mess2, icon3,
				mess3, icon4, mess4);
		p.tdAttr = 'data-qtip="' + myToolTipText + '"';
		return "<img src='" + icon1 + "' border=0>" + "<img src='" + icon2 + "' border=0>" + "<img src='" + icon3 + "' border=0>" +
		"<img src='" + icon4 + "' border=0>";
	}

	// render completeness
	function renderCompleteness(value, p, record) {
		if (record.data.autoProcStatisticsOverall && record.data.autoProcStatisticsOuter && record.data.autoProcStatisticsInner) {
			var percentage1 = record.data.autoProcStatisticsInner.completeness;
			var percentage2 = record.data.autoProcStatisticsOuter.completeness;
			var percentage3 = record.data.autoProcStatisticsOverall.completeness;

			var initial = -61;
			var imageWidth = 122;
			var eachPercent = (imageWidth / 2) / 100;

			var percentageWidth1 = eachPercent * percentage1;
			var actualWidth1 = initial + percentageWidth1;
			var colour1 = percentage1 < 85 ? 4 : 1;

			var percentageWidth2 = eachPercent * percentage2;
			var actualWidth2 = initial + percentageWidth2;
			var colour2 = percentage2 < 85 ? 4 : 1;

			var percentageWidth3 = eachPercent * percentage3;
			var actualWidth3 = initial + percentageWidth3;
			var colour3 = percentage3 < 85 ? 4 : 1;

			var s = "";
			if (!(percentage1 === 0 && percentage2 === 0 && percentage3 === 0)) {
				s += '<img " src="../images/percentImage_small.png" title="' +
						percentage1 + '% (inner)" alt="' + percentage1 +
						'% (inner)" class="percentImage' + colour1 +
						'" style="background-position: ' + actualWidth1 +
						'px 0pt; "/><br/>';
				s += '<img  src="../images/percentImage_small.png" title="' + 
						percentage2 + '% (outer)" alt="' + percentage2 + 
						'% (outer)" class="percentImage' + colour2 + 
						'" style="background-position: ' + actualWidth2 + 
						'px 0pt; "/><br/>';
				s += '<img  src="../images/percentImage_small.png" title="' + 
						percentage3 + '% (overall)" alt="' + percentage3 + 
						'% (overall)" class="percentImage' + colour3 + 
						'" style="background-position: ' + actualWidth3 + 
						'px 0pt; "/><br/>';
			}
			return s;
		}
		return "";
	}

	// render resolution
	function renderResolution(value, p, record) {
		if (record.data.autoProcStatisticsOverall && record.data.autoProcStatisticsOuter && record.data.autoProcStatisticsInner) {
			var s = (record.data.autoProcStatisticsInner.resolutionLimitLow ? record.data.autoProcStatisticsInner.resolutionLimitLow : "") + 
					" - " +
					(record.data.autoProcStatisticsInner.resolutionLimitHigh ? record.data.autoProcStatisticsInner.resolutionLimitHigh : "") + "<br />";
			s += (record.data.autoProcStatisticsOuter.resolutionLimitLow ? record.data.autoProcStatisticsOuter.resolutionLimitLow : "") +
					" - " +
					(record.data.autoProcStatisticsOuter.resolutionLimitHigh ? record.data.autoProcStatisticsOuter.resolutionLimitHigh : "") + "<br />";
			s += (record.data.autoProcStatisticsOverall.resolutionLimitLow ? record.data.autoProcStatisticsOverall.resolutionLimitLow : "") +
					" - " +
					(record.data.autoProcStatisticsOverall.resolutionLimitHigh ? record.data.autoProcStatisticsOverall.resolutionLimitHigh : "") + "<br />";
			return s;
		} else if (record.data.screeningOutput) {
			return Number(record.data.screeningOutput.rankingResolution)
					.toFixed(2);
		}
		return "";
	}

	// render rsymm
	function renderRsymm(value, p, record) {
		if (record.data.autoProcStatisticsOverall && record.data.autoProcStatisticsOuter && record.data.autoProcStatisticsInner) {
			var s = (record.data.autoProcStatisticsInner.rmerge ? Number(record.data.autoProcStatisticsInner.rmerge).toFixed(1): "") + "<br />";
			s += (record.data.autoProcStatisticsOuter.rmerge ? Number(record.data.autoProcStatisticsOuter.rmerge).toFixed(1) : "") + "<br />";
			s += (record.data.autoProcStatisticsOverall.rmerge ? Number(record.data.autoProcStatisticsOverall.rmerge).toFixed(1): "") + "<br />";
			return s;
		}
		return "";
	}

	// render unit cell
	function renderUnitCell(value, p, record) {
		var nbDec = 2;
		if (record.data.autoProc) {
			var s = (record.data.autoProc.refinedCellA ? Number(record.data.autoProc.refinedCellA).toFixed(nbDec) + ", " : "");
			s += (record.data.autoProc.refinedCellB ? Number(record.data.autoProc.refinedCellB).toFixed(nbDec) + ", ": "");
			s += (record.data.autoProc.refinedCellC ? Number(record.data.autoProc.refinedCellC).toFixed(nbDec) + "<br />": "");
			s += (record.data.autoProc.refinedCellAlpha ? Number(record.data.autoProc.refinedCellAlpha).toFixed(nbDec) + ", " : "");
			s += (record.data.autoProc.refinedCellBeta ? Number(record.data.autoProc.refinedCellBeta).toFixed(nbDec) + ", " : "");
			s += (record.data.autoProc.refinedCellGamma ? Number(record.data.autoProc.refinedCellGamma).toFixed(nbDec): "");
			return s;
		} else if (record.data.lattice) {
			var s1 = (record.data.lattice.unitCell_a ? Number(record.data.lattice.unitCell_a).toFixed(nbDec) + ", ": "");
			s1 += (record.data.lattice.unitCell_b ? Number(record.data.lattice.unitCell_b).toFixed(nbDec) + ", ": "");
			s1 += (record.data.lattice.unitCell_c ? Number(record.data.lattice.unitCell_c).toFixed(nbDec) + "<br />": "");
			s1 += (record.data.lattice.unitCell_alpha ? Number(record.data.lattice.unitCell_alpha).toFixed(nbDec) + ", ": "");
			s1 += (record.data.lattice.unitCell_beta ? Number(record.data.lattice.unitCell_beta).toFixed(nbDec) + ", ": "");
			s1 += (record.data.lattice.unitCell_gamma ? Number(record.data.lattice.unitCell_gamma).toFixed(nbDec) : "");
			return s1;
		}
		return "";
	}

	// render spaceGroup, edna or autoProc
	function renderSpaceGroup(value, p, record) {
		if (record.data.autoProc) {
			return record.data.autoProc.spaceGroup;
		} else {
			// edna space group
			if (record.data.lattice) {
				return record.data.lattice.spaceGroup;
			} else {
				return "";
			}
		}
	}

	// column wrap
	function columnWrap(value) {
		if (value) { 
			return '<div style="white-space:normal !important;">' + value + '</div>';
		}
		else { 
			return  "";
		}
	}

	// render experiment parameters (col hidden or expanded)
	function renderExperimentParameter(value, p, record) {
		var icon = '../images/Info_16x16_01.png';
		var myToolTipText = "";
		myToolTipText += "Wavelength: " + (record.data.wavelength ? record.data.wavelength.toFixed(3): "");
		myToolTipText += "<br/>";
		myToolTipText += "Transmission: " + (record.data.transmission ? record.data.transmission.toFixed(3) : "");
		myToolTipText += "<br/>";
		myToolTipText += "ex. Time: " + (record.data.exposureTime ? record.data.exposureTime.toFixed(2) : "");
		myToolTipText += "<br/>";
		myToolTipText += "PhiStart: " + (record.data.axisStart ? record.data.axisStart.toFixed(2): "");
		myToolTipText += "<br/>";
		myToolTipText += "PhiRange: " + (record.data.axisRange ? record.data.axisRange.toFixed(2): "");
		myToolTipText += "<br/>";
		myToolTipText += "Detector Resolution: " + (record.data.resolution ? record.data.resolution.toFixed(2): "");
		p.tdAttr = 'data-qtip="' + myToolTipText + '"';
		return "<img src='" + icon + "' border=0>";
	}

	// render Rank Column
	function renderCheckBoxRank(value, p, record) {
		var ednaChecked = false;
		var autoProcChecked = false;
		if (record.data.screeningOutput) {
			if (record.data.screeningOutput.indexingSuccess) {
				if (record.data.screeningOutput.strategySuccess) {
					if (record.data.screeningOutput.indexingSuccess == 1) {
						ednaChecked = true;
					}
				}
			}
		} else if (record.data.autoProc) {
			autoProcChecked = true;
		}
		var s = "";
		if (ednaChecked) {
			s += "<input checked='" + ednaChecked + "' name='rankIdList[" + 
					record.data.dataCollectionId + 
					"]' value='on' type='checkbox'>";
			s += "<img  src='../images/checkbox_grey.png' id='rankIdImage[" + 
					record.data.dataCollectionId + "]' style='display:none'>";
		}
		if (autoProcChecked) {
			s += "<input checked='" + autoProcChecked + 
					"' name='rankIdAutoProcList[" + 
					record.data.dataCollectionId + 
					"]' value='on' type='checkbox' style='display:none'>";
			s += "<img  src='../images/checkbox_grey.png' id='rankIdAutoProcImage[" + 
					record.data.dataCollectionId + "]' style='display:none'>";
		}
		if (!ednaChecked && !autoProcChecked) { 
			return "<img src='../images/checkbox_grey.png' border=0>";
		}
		else { 
			return s;
		}
	}
	
	// render Skip
	function renderCheckBoxSkip(value, p, record) {
		var s = "<input " + (value ? "checked='checked'" : "") + " property='skipIdList[" + 
					record.data.dataCollectionId + 
					"]'  value='1' type='checkbox'>";						
		return s;		
	}
	
	// hide element
	function actionItemRenderer(value, meta, record, rowIx, ColIx, store) {
		if (!record.get('hasAutoProcAttachment')) {
			return 'x-hide-display';
		}
	}

	// build columns
	var columns = [];
	if (this.displayLastCollect) {
		columns.push({
				text : 'Beamline<br/>Name',
				dataIndex : 'beamLineName',
				flex : 0.08,
				id : 'beamLineName'
			});
	}
	if (this.isManager) {
		columns.push({
				text : 'Proposal',
				dataIndex : 'proposal',
				flex : 0.1,
				id : 'proposal'
			});
	}
    if (this.isManager) {
        columns.push({
                        text : 'User',
                        dataIndex : 'login',
                        flex : 0.1,
                        id : 'login'
                });
    }
    
	columns.push(
	{
			text : 'Image<br/>Prefix',
			dataIndex : 'imagePrefix',
			width : 70,
			renderer : renderImagePrefix
		}, {
			text : 'Run<br/>No',
			dataIndex : 'dataCollectionNumber',
			flex : 0.02
		}, {
			text : '#<br/>images',
			dataIndex : 'numberOfImages',
			flex : 0.05
		});

	if (!this.displayLastCollect) {
		columns.push({
				text : 'Exp.<br/>Param.<br/>',
				dataIndex : 'experimentParameters',
				flex : 0.05,
				renderer : renderExperimentParameter
			});

		columns.push({
				text : 'Wavelenth',
				dataIndex : 'wavelength',
				flex : 0.07,
				renderer : renderFixed3
			}, {
				text : 'Transm.',
				dataIndex : 'transmission',
				flex : 0.07,
				renderer : renderFixed3
			});
		if (this.isIndustrial) {
			columns.push({
					text : 'Distance',
					dataIndex : 'detectorDistance',
					flex : 0.05,
					renderer : renderFixed2
				});
		}
		columns.push({
				text : 'Ex.<br>Time',
				dataIndex : 'exposureTime',
				flex : 0.05,
				renderer : renderFixed3
			}, {
				text : 'Phi<br>start',
				dataIndex : 'axisStart',
				flex : 0.05,
				renderer : renderFixed2
			}, {
				text : 'Phi<br>range',
				dataIndex : 'axisRange',
				flex : 0.05,
				renderer : renderFixed2
			});
		if (this.isIndustrial) {
			columns.push({
					text : 'Xbeam',
					dataIndex : 'xbeam',
					flex : 0.05,
					renderer : renderFixed2
				}, {
					text : 'Ybeam',
					dataIndex : 'ybeam',
					flex : 0.05,
					renderer : renderFixed2
				});
		}
		columns.push({
				text : 'Detector<br/>Resolution',
				dataIndex : 'resolution',
				flex : 0.07,
				renderer : renderFixed2
			});
	}
	columns.push({
			text : 'Status',
			dataIndex : 'status',
			flex : 0.05,
			renderer : renderStatus
		}, {
			text : 'Space<br/>Group',
			dataIndex : 'autoProc',
			width : 70,
			renderer : renderSpaceGroup
		}, {
			text : 'Completeness',
			dataIndex : 'completeness',
			width : 70,
			renderer : renderCompleteness
		}, {
			text : 'Resolution',
			dataIndex : 'resolution',
			width : 80,
			renderer : renderResolution
		}, {
			text : 'Rsymm<br/><small><i>Inner<br/>Outer<br/>Overall</i></small>',
			dataIndex : 'autoProcStatisticsOverall',
			width : 40,
			renderer : renderRsymm
		}, {
			text : 'Unit_cell<br/><small><i>a, b, c<br/>alpha, beta, gamma</i></small>',
			dataIndex : 'autoProcStatisticsInner',
			width : 120,
			renderer : renderUnitCell
		}, {
			text : 'Exp.<br/>Type',
			dataIndex : 'experimentType',
			flex : 0.05
		}, {
			text : 'Protein<br>Acronym',
			dataIndex : 'proteinAcronym',
			flex : 0.05,
			renderer : renderProteinAcronym
		}, {
			text : 'Start<br/>time',
			dataIndex : 'startTime',
			width : 75,
			renderer : renderStartDate
		} );

	if (!this.displayLastCollect) {
		columns.push({
				header : 'Sample<br/>Ranking',
				dataIndex : 'dataCollectionId',
				flex : 0.05,
				renderer : renderCheckBoxRank
			});
	}
	
	if (!this.isIndustrial) {
		columns.push(
			{
				xtype : 'checkcolumn',
				header : 'Skip',
				dataIndex : 'skipForReport',
				flex : 0.04,
				renderer : renderCheckBoxSkip
			}
		);
	}
	
	if (this.displayLastCollect || this.isIndustrial) {
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				flex : 0.2,
				renderer : columnWrap
			});
	} else {
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				flex : 0.2,
				editor : {
					xtype : 'textfield',
					allowBlank : true
				},
				renderer : columnWrap
			});
	}
	
	columns.push({	
		xtype : 'actioncolumn',
		width : 50,
		header : 'Download<br>autoproc<br>files',		
		items : [{
			icon : '../images/download.png',
			tooltip : 'Download autoproc',
			getClass : actionItemRenderer,
			handler : function (grid, rowIndex, colIndex) {
				// console.log("View collect: " + rowIndex + " = " +_this.store.getAt(rowIndex).data.dataCollectionId);
				_this.downloadAutoproc(_this.store.getAt(rowIndex).data.dataCollectionId);
			}
		}],
		sortable : false
	});
	
	return columns;

};


// redirection to the detail of a collect
DataCollectionGrid.prototype.viewDataCollection = function (dataCollectionId) {
	document.location.href = this.contextPath + 
			"/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForDataCollectionGroup&dataCollectionGroupId=" + dataCollectionId;
};


// returns the paging mode
DataCollectionGrid.prototype.getPagingMode = function () {
	return this.pagingMode;
};

// hode or show the experiment parameters columns
DataCollectionGrid.prototype.hideAndShowExperimentParameter = function () {
	this.hideCol = !this.hideCol;
	this._hideCol();
};


// returns the dataCollections as json
DataCollectionGrid.prototype.getListDataCollection = function () {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};

// change rank EDNA or AutoProc
DataCollectionGrid.prototype.changeRank = function () {
	this.rankEDNA = !this.rankEDNA;
	this.setRankValue();
};

// set the rank value, EDNA or autoProc
DataCollectionGrid.prototype.setRankValue = function () {
	for (var i = this.minDCId; i <= this.maxDCId; i++) {
		var rankList = document.getElementsByName("rankIdList[" + i + "]");
		if (rankList != null && rankList.item(0) != null) {
			if (this.rankEDNA) {
				rankList.item(0).style.display = '';
			} else {
				rankList.item(0).style.display = 'none';
			}
		}
		var rankAutoProcList = document.getElementsByName("rankIdAutoProcList[" + i + "]");
		if (rankAutoProcList != null && rankAutoProcList.item(0) != null) {
			if (this.rankEDNA) {
				rankAutoProcList.item(0).style.display = 'none';
			} else {
				rankAutoProcList.item(0).style.display = '';
			}
		}
		var rankImage = document.getElementById("rankIdImage[" + i + "]");
		if (rankImage != null) {
			if (this.rankEDNA) {
				rankImage.style.display = 'none';
			} else {
				rankImage.style.display = '';
			}
		}
		var rankAutoProcImage = document.getElementById("rankIdAutoProcImage[" + i + "]");
		if (rankAutoProcImage != null) {
			if (this.rankEDNA) {
				rankAutoProcImage.style.display = '';
			} else {
				rankAutoProcImage.style.display = 'none';
			}
		}
	}

};

// rank click: throws an event
DataCollectionGrid.prototype.startRank = function () {
	if (this.rankEDNA) {
		this.onRankButtonClicked.notify({});
	} else {
		this.onRankAutoProcButtonClicked.notify({});
	}
};

// returns the list of collect to rank for EDNA
DataCollectionGrid.prototype.getListDataCollectionRank = function () {
	var dataCollectionIds = [];
	for (var i = this.minDCId; i <= this.maxDCId; i++) {
		var rankList = document.getElementsByName("rankIdList[" + i + "]");
		if (rankList != null && rankList.item(0) != null) {
			dataCollectionIds.push(i);
		}
	}
	return dataCollectionIds;
};

// returns the list of collect to rank for autoproc
DataCollectionGrid.prototype.getListDataCollectionRankAutoProc = function () {
	var dataCollectionIds = [];
	for (var i = this.minDCId; i <= this.maxDCId; i++) {
		var rankList = document.getElementsByName("rankIdAutoProcList[" + i + "]");
		if (rankList != null && rankList.item(0) != null) {
			dataCollectionIds.push(i);
		}
	}
	return dataCollectionIds;
};

// return the list of collects to skip
DataCollectionGrid.prototype.getListDataCollectionSkip = function () {
	var dataCollectionIds = [];
	for (var i = this.minDCId; i <= this.maxDCId; i++) {
		var skipList = document.getElementsByName("skipIdList[" + i + "]");
		if (skipList != null && skipList.item(0) != null && skipList.item(0) == false ) {
			dataCollectionIds.push(i);
		}
	}
	return dataCollectionIds;
};


// download autoprocessing files redirection
DataCollectionGrid.prototype.downloadAutoproc = function (dataCollectionId) {
	var rmerge = this.rMergeCutoff;
	var isigma = this.iSigmaCutoff;
	document.location.href = this.contextPath +  "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=download&dataCollectionId=" + dataCollectionId + "&rmerge=" + rmerge + "&isigma=" + isigma + "&anomalous=false";
	// "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=download&dataCollectionId=" + dataCollectionId + "&rmerge=" + vdcForm.getRMergeCutoff() + "&isigma=" + vdcForm.getISigmaCutoff() + "&anomalous=false";
};
