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

// Data Collection Group grid
function DataCollectionGroupGrid(args) {

	// Events
	this.onSaveButtonClicked = new Event(this);

	var isFx = false;
	var isIndus = false;
	var contextPath = "";
	this.title = "DataCollectionGroups";
	this.canSave = false;
	this.listOfCrystalClass = [];
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.isIndus != null) {
			this.isIndus = args.isIndus;
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
		if (args.sessionId != null) {
			this.sessionId = args.sessionId;
		}
		if (args.canSave != null) {
			this.canSave = args.canSave;
		}
	}

	// format crystal class
	this._setCrystalClass();
	

	// data collection group datamodel
	Ext.define('DataCollectionGroupModel', {
		extend : 'Ext.data.Model',
		fields : [{
				name : 'dataCollectionGroupId',
				mapping : 'dataCollectionGroupId'
			}, {
				name : 'experimentType',
				mapping : 'experimentType',
				type: 'string'
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
				name : 'crystalClass'
			}, {
				name : 'comments',
				mapping : 'comments'
			}, {
				name : 'detectorMode',
				mapping : 'detectorMode'
			}, {
				name : 'actualSampleBarcode',
				mapping : 'actualSampleBarcode'
			}, {
				name : 'actualSampleSlotInContainer',
				mapping : 'actualSampleSlotInContainer'
			}, {
				name : 'actualContainerBarcode',
				mapping : 'actualContainerBarcode'
			}, {
				name : 'actualContainerSlotInSC',
				mapping : 'actualContainerSlotInSC'
			}, {
				name : 'xtalSnapshotFileFullPath',
				mapping : 'xtalSnapshotFileFullPath'
			}, {
				name : 'blSampleVO',
				mapping : 'blSampleVO'
			}, {
				name : 'workflowVO',
				mapping : 'workflowVO'
			}, {
				name : 'dataCollectionVOs',
				mapping : 'dataCollectionVOs'
			}, {
				name : 'runNumber',
				mapping : 'runNumber'
			}, {
				name : 'sampleName',
				mapping : 'sampleName'
			}, {
				name : 'proteinAcronym',
				mapping : 'proteinAcronym'
			}, {
				name : 'imagePrefix',
				mapping : 'imagePrefix'
			}, 
			 {
				name : 'sampleNameProtein',
				mapping : 'sampleNameProtein'
			}, {
				name : 'groupExperimentType'
			}],
		idProperty : 'dataCollectionGroupId'
	});
}

// no filters
DataCollectionGroupGrid.prototype.getFilterTypes = function () {
	return [];
};

// sort by default
DataCollectionGroupGrid.prototype._sort = function (store, sampleNameHidden) {
	if (sampleNameHidden) {
		store.sort('startTime', 'DESC');
	} else {
		store.sort('startTime', 'ASC');
	}
};

// format data for crystal class
DataCollectionGroupGrid.prototype._setCrystalClass = function () {
	this.crystalClass = [];
	for (var i = 0; i < this.listOfCrystalClass.length; i++) {
		this.crystalClass.push(this.listOfCrystalClass[i].crystalClassName);
	}
};

// gets the grid
DataCollectionGroupGrid.prototype.getGrid = function (dataCollectionGroups) {
	this.features = dataCollectionGroups;
	return this.renderGrid(dataCollectionGroups);
};


// refresh data: reload data
DataCollectionGroupGrid.prototype.refresh = function (data) {
	this.features = data.dataCollectionGroupList;
	this.setGroupExperimentType();
	this.store.loadData(this.features, false);
};


//  no actions
DataCollectionGroupGrid.prototype.getActions = function () {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};

// set the experiment type, if it's a workflow, workflow type
DataCollectionGroupGrid.prototype.setGroupExperimentType = function () {
	var _this = this;
	for (var i = 0 ; i < _this.features.length ; i++) {
		if (_this.features[i].workflowVO){
			_this.features[i].groupExperimentType = _this.features[i].workflowVO.workflowType ;
		}else{
			_this.features[i].groupExperimentType = _this.features[i].experimentType;
		}
	}
};
	

// builds the grid
DataCollectionGroupGrid.prototype.renderGrid = function () {
	var _this = this;

	/** Prepare data * */
	var data = [];
	var sampleNameHidden = true;
	var defaultGroupField = null;
	// set the experiment type
	_this.setGroupExperimentType();
	// if some collects are linked to a sample, the column Sample is displayed, and group by this field
	for (var i = 0 ; i < _this.features.length ; i++) {
		if (_this.features[i].sampleName && _this.features[i].sampleName !== "") {
			sampleNameHidden = false;
			defaultGroupField = "sampleNameProtein";
			break;
		}
	}
	// get the columns
	var columns = this._getColumns(sampleNameHidden);
	//grid data
	this.store = Ext.create('Ext.data.Store', {
			model : 'DataCollectionGroupModel',
			autoload : false,
			groupField : defaultGroupField
		});

	
	//this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);
	this._sort(this.store, sampleNameHidden);
	// if no data
	var noGroupDataPanel = Ext.create('Ext.Panel', {
		id : 'noGroupDataPanel',
		layout : {
			type : 'fit', // Arrange child items vertically
			align : 'center', // Each takes up full width
			bodyPadding : 0,
			border : 0
		},
		html : '<html><h4>No Data Collections Groups have been found</h4></html>'
	});

	if (this.features.length == 0) {
		return noGroupDataPanel;
	}

	// this grid can be edited
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});

	// grouping grid
	var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
		groupHeaderTpl : '{columnName}: {name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
		hideGroupedHeader : true,
		startCollapsed : false,
		id : 'dataCollectionGroupGrouping'
	}), 
	groups = this.store.getGroups(),
	len = groups.length,
	//i = 0, 
	toggleGroup = function (
			item) {
		var groupName = item.text;
		if (item.checked) {
			groupingFeature.expand(groupName, true);
		} else {
			groupingFeature.collapse(groupName, true);
		}
	};

	// top bar with buttons
	var bar = [];
	if (this.canSave) {
		bar.push({
				text : 'Save Comments',
				tooltip : 'Save dataCollections Groups',
				icon : '../images/save.gif',
				handler : function () {
					_this.onSaveButtonClicked.notify({});
				}
			});
	}

	if (this.sessionId && this.sessionId != "null"){
		bar.push({
			text : 'View DataCollection for all groups',
			tooltip : 'View DataCollection for all groups',
			icon : '../images/magnif_16.png',
			handler : function () {
				document.location.href = contextPath + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForSession&sessionId=" + _this.sessionId;
			}
		});
				
	}
	bar.push('->');
	bar.push({
		text : 'Expand All',
		handler : function () {
			var rows = groupingFeature.view.getEl().query('.x-grid-group-body');

			Ext.each(rows, function (row) {
						groupingFeature.expand(Ext.get(row));
					});
		}
	}, {
		text : 'Collapse All',
		handler : function () {
			var rows = groupingFeature.view.getEl().query('.x-grid-group-body');

			Ext.each(rows, function (row) {
						groupingFeature.collapse(Ext.get(row));
					});
		}
	});
	
	bar.push({
			text : 'Clear Grouping',
			iconCls : 'icon-clear-group',
			handler : function () {
				groupingFeature.disable();
			}
		});
	
	// grid creation
	this.grid = Ext.create('Ext.grid.Panel', {
		style : {
			padding : 0
		},
		model : 'DataCollectionGroupModel',
		store : this.store,
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
		features : [groupingFeature],
		tbar : bar
	});

	return this.grid;
};

// return the list of dataCollectionGroups as json
DataCollectionGroupGrid.prototype.getListDataCollectionGroup = function () {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};

// returns the sessionId
DataCollectionGroupGrid.prototype.getSessionId = function () {
	return this.sessionId;
};


// builds the columns
DataCollectionGroupGrid.prototype._getColumns = function (sampleNameHidden) {
	var _this = this;
	// render functions
	
	// render date
	function renderStartDate(value, p, record) {
		return Ext.Date.format(value, 'd-m-Y H:i:s');
	}


	// render sample position
	function renderSamplePosition(value, p, record) {
		var s = "";
		if (record.data.actualSampleSlotInContainer != null) { 
			s += record.data.actualSampleSlotInContainer;
		}
		if (record.data.actualContainerSlotInSC != null) {
			s += " (" + record.data.actualContainerSlotInSC + ")";
		}
		s += "<br />";
		if (record.data.actualSampleBarcode != null) { 
			s += record.data.actualSampleBarcode;
		}
		if (record.data.actualContainerBarcode != null) {
			s += " (" + record.data.actualContainerBarcode + ")";
		}
		return s;
	}

	// column wrap
	function columnWrap(value) {
		if (value) { 
			return '<div style="white-space:normal !important;word-wrap: break-word">' + value + '</div>';
		}
		else { 
			return "";
		}
	}

	// hide content
	function actionItemRenderer(value, meta, record, rowIx, ColIx, store) {
		if (record.get('dataCollectionVOs').length == 0) {
			return 'x-hide-display';
		}
	}

	// render nb data collections
	function renderNbDataCollections(value, p, record) {
		if (record.get('dataCollectionVOs') != null) { 
			return record.get('dataCollectionVOs').length;
		}
		else { 
			return 0;
		}
	}

	// render nb Images
	function renderNbImages(value, p, record) {
		if (record.get('dataCollectionVOs') !== null && record.get('dataCollectionVOs').length > 0) {
			return record.get('dataCollectionVOs')[0].numberOfImages;
		} else { 
			return 0;
		}
	}

	// render workflow column: with status ball
	function renderWorkflow(value, p, record) {
		if (record.data.workflowVO) {
			var mess = "";
			if (record.data.workflowVO.status) {
				mess = record.data.workflowVO.status;
			}
			var iconWorkflow = "../images/Sphere_White_12.png";
			if (mess) {
				if (mess.search("Error") != -1 || mess.search("Failure") != -1) {
					iconWorkflow = "../images/Sphere_Red_12.png";
				} else if (mess.search("Started") != -1 || mess.search("Launched") != -1) {
					iconWorkflow = "../images/Sphere_Orange_12.png";
				} else if (mess.search("Success") != -1) {
					iconWorkflow = "../images/Sphere_Green_12.png";
				}
			}
			var myToolTipText = " <img src='" + iconWorkflow + "' border=0\/>" +
					mess + "<br/>" + record.data.workflowVO.workflowTitle +
					"<br/>" + record.data.workflowVO.comments;
			p.tdAttr = 'data-qtip="' + myToolTipText + '"';
			return "<img src='" + iconWorkflow + "' border=0> <a href='" + contextPath + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewWorkflow.do?reqCode=display&workflowId=" + record.data.workflowVO.workflowId + "'>" + record.data.workflowVO.workflowType + "</a>";
		} else {
			return "";
		}
	}
	
	// render experiment type
	function renderExperimentType(value, p, record) {
		if (record.data.workflowVO) {
			var mess = "";
			if (record.data.workflowVO.status) {
				mess = record.data.workflowVO.status;
			}
			var iconWorkflow = "../images/Sphere_White_12.png";
			if (mess) {
				if (mess.search("Error") != -1 || mess.search("Failure") != -1) {
					iconWorkflow = "../images/Sphere_Red_12.png";
				} else if (mess.search("Started") != -1 || mess.search("Launched") != -1) {
					iconWorkflow = "../images/Sphere_Orange_12.png";
				} else if (mess.search("Success") != -1) {
					iconWorkflow = "../images/Sphere_Green_12.png";
				}
			}
			var com = "";
			if (record.data.workflowVO.comments){
				com = record.data.workflowVO.comments;
			}
			var myToolTipText = " <img src='" + iconWorkflow + "' border=0\/>" +
					mess + "<br/>" + record.data.workflowVO.workflowTitle +
					"<br/>" + com;
			p.tdAttr = 'data-qtip="' + myToolTipText + '"';
			return "<img src='" + iconWorkflow + "' border=0> <a href='" + contextPath + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewWorkflow.do?reqCode=display&workflowId=" + record.data.workflowVO.workflowId + "'>" + record.data.workflowVO.workflowType + "</a>";
		} else {
			return record.data.experimentType;
		}
	}

	// render crystal class
	function renderCrystalClass(value, p, record) {
		if (record.data.crystalClass != null) {
			var code = record.data.crystalClass;
			for (var i = 0; i < _this.listOfCrystalClass.length; i++) {
				var c = _this.listOfCrystalClass[i].crystalClassCode;
				if (c == code) {
					return _this.listOfCrystalClass[i].crystalClassName;
				}
				return code;
			}
		} else {
			return "";
		}
	}

	// create reusable renderer
	Ext.util.Format.comboRenderer = function (combo) {
		return function (value) {
			var record = combo.findRecord(combo.valueField, value);
			return record ? record.get(combo.displayField) : combo.valueNotFoundText;
		};
	};

	// create the combo instance

	var crystalClassStore = Ext.create('Ext.data.Store', {
			fields : ['crystalClassCode', 'crystalClassName'],
			data : _this.listOfCrystalClass
		});

	var combo = new Ext.form.ComboBox({
			mode : 'local',
			emptyText : '---',
			store : crystalClassStore,
			valueField : 'crystalClassCode',
			displayField : 'crystalClassName',
			allowBlank : true,
			typeAhead : true,
			triggerAction : 'all'
		});
	
	// columns
	var columns = [
		{
			text : 'Experiment<br/>Type',
			renderer : renderExperimentType,
			dataIndex:'groupExperimentType',
			flex : 0.10
		},
		{
			text : 'Protein<br/> Acronym',
			dataIndex : 'proteinAcronym',
			flex : 0.075
		}, {
			text : 'Image Prefix',
			dataIndex : 'imagePrefix',
			flex : 0.2
		},
		{
			text : 'Run No',
			dataIndex : 'runNumber',
			align: 'center',
			flex : 0.1
		},

		{
			text : 'Sample name',
			dataIndex : 'sampleName',
			hidden: sampleNameHidden,
			flex : 0.15
		},
		{
			text : 'Acronym - Sample name',
			dataIndex : 'sampleNameProtein',
			hidden: sampleNameHidden,
			flex : 0.15
		},
		{
			text : 'Sample position',
			dataIndex : 'actualSampleSlotInContainer',
			renderer : renderSamplePosition,
			align: 'center',
			type : 'string',
			flex : 0.15
		}, {
			text : 'Start Time',
			dataIndex : 'startTime',
			renderer : Ext.util.Format.dateRenderer('d-m-Y H:i:s'),
			flex : 0.15
		}];
	if (_this.isFx) {
		if (_this.canSave) {
			columns.push({
				text : 'Crystal class',
				dataIndex : 'crystalClass',
				flex : 0.2,
				editor : combo, // specify reference to combo instance
				renderer : Ext.util.Format.comboRenderer(combo)
					// pass combo instance to reusable renderer
			});
		} else {
			columns.push({
				text : 'Crystal class',
				dataIndex : 'crystalClass',
				flex : 0.2,
				renderer : Ext.util.Format.comboRenderer(combo)
					// pass combo instance to reusable renderer
			});
		}
	}
	if (_this.canSave) {
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				editor : {
					xtype : 'textfield',
					allowBlank : true
				},
				flex : 0.2,
				renderer : columnWrap
			});
	} else {
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				flex : 0.2,
				renderer : columnWrap
			});
	}
	columns.push({
			text : 'Nb<br/>Data<br/>Collections',
			dataIndex : 'dataCollectionVOs',
			type : 'int',
			align: 'center',
			renderer : renderNbDataCollections,
			flex : 0.05
		}, {
			text : 'Nb<br/>images',
			dataIndex : 'dataCollectionVOs',
			type : 'int',
			align: 'center',
			renderer : renderNbImages,
			flex : 0.04
		}

	);

	columns.push({
		xtype : 'actioncolumn',
		width : 70,
		header : 'View<br/>Collections',
		sortable : false,
		items : [{
			icon : '../images/magnif_16.png',
			tooltip : 'View Collections',
			getClass : actionItemRenderer,
			handler : function (grid, rowIndex, colIndex) {
				//console.log("View collect: "+rowIndex+" = "+_this.store.getAt(rowIndex).data.dataCollectionGroupId);
				_this.viewCollections(_this.store.getAt(rowIndex).data.dataCollectionGroupId);
			}
		}]

	});

	return columns;

};

// redirection to display view dataCollection for 1 dataCollectionGroupId 
DataCollectionGroupGrid.prototype.viewCollections = function (dataCollectionGroupId) {
	document.location.href = contextPath + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForDataCollectionGroup&dataCollectionGroupId=" + dataCollectionGroupId;
};

