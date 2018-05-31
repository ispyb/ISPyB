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
 * Brenchereau, M. Bodin,  A. De Maria Antolinos
 ******************************************************************************/

/*global Event */
/*global Ext */
/*global document */


// puck grid, used in the puck creation or fill Shipment interface 
function CreatePuckGrid(args) {

	// Events
	// save puck
	this.onSaveButtonClicked = new Event(this);
	// edit puck
	this.onEditPuckButtonClicked = new Event(this);
	// remove puck
	this.onRemovePuckButtonClicked = new Event(this);
	// copy puck
	this.onCopyPuckButtonClicked = new Event(this);
	// copy sample
	this.onCopySampleButtonClicked = new Event(this);

	this.title = "Create a new Puck";
	this.saveButton = "Save";
	//protein list  for the combo box
	this.proteinList = [];
	// sapce Group list to be displayed in the combo box
	this.spaceGroupList = [];
	// experiment type list to be displayed in the combo box 
	this.experimentTypeList = [];
	// crystal class list to be displayed in the combo box
	this.crystalValuesList = [];
	// represents the data linked to the puck(list of samples, puckId, etc)
	this.puckData =  null;
	// sample to be pasted
	this.sampleToCopy = null;
	
	// if false: puck creation, if true: fill Shipment view
	this.fillShipmentMode  = false;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
	}

	this.changeSampleNameAuto = false;

	// sample information datamodel
	Ext.define('SampleModel', {
			extend : 'Ext.data.Model',
			fields : [{
					name : 'sampleId',
					mapping : 'sampleId'
				}, {
					name : 'position',
					mapping : 'position'
				}, {
					name : 'sampleName',
					mapping : 'sampleName'
				}, {
					name : 'proteinAcronym',
					mapping : 'proteinAcronym'
				}, {
					name : 'spaceGroup',
					mapping : 'spaceGroup'
				}, {
					name : 'preObservedResolution',
					mapping : 'preObservedResolution'
				}, {
					name : 'neededResolution',
					mapping : 'neededResolution'
				}, {
					name : 'preferredBeamDiameter',
					mapping : 'preferredBeamDiameter'
				}, {
					name : 'experimentType',
					mapping : 'experimentType'
				}, {
					name : 'numberOfPositions',
					mapping : 'numberOfPositions'
				}, {
					name : 'radiationSensitivity',
					mapping : 'radiationSensitivity'
				}, {
					name : 'aimedMultiplicity',
					mapping : 'aimedMultiplicity'
				}, {
					name : 'aimedCompleteness',
					mapping : 'aimedCompleteness'
				}, {
					name : 'unitCellA',
					mapping : 'unitCellA'
				}, {
					name : 'unitCellB',
					mapping : 'unitCellB'
				}, {
					name : 'unitCellC',
					mapping : 'unitCellC'
				}, {
					name : 'unitCellC',
					mapping : 'unitCellC'
				}, {
					name : 'unitCellAlpha',
					mapping : 'unitCellAlpha'
				}, {
					name : 'unitCellBeta',
					mapping : 'unitCellBeta'
				}, {
					name : 'unitCellGamma',
					mapping : 'unitCellGamma'
				}, {
					name : 'smiles',
					mapping : 'smiles'
				}, {
					name : 'comments',
					mapping : 'comments'
				}, {
					name : 'pinBarcode',
					mapping : 'pinBarcode'
				}, {
					name : 'minOscWidth',
					mapping : 'minOscWidth'
				}, {
					name : 'axisRange',
					mapping : 'axisRange'
				}],
			idProperty : 'sampleId'
		});
}

// no filters
CreatePuckGrid.prototype.getFilterTypes = function () {
	return [];
};

// default sort by position in the puck ASC
CreatePuckGrid.prototype._sort = function (store) {
	store.sort('position', 'ASC');
};

// builds the protein list to be displayed in the combo box: list of acronym
CreatePuckGrid.prototype._setProteinList = function (data) {
	this.proteinList = [];
	if (data && data.proteinList) {
		for (var i = 0; i < data.proteinList.length; i++) {
			var value = [];
			value.proteinAcronym = data.proteinList[i];
			this.proteinList.push(value);
		}
	}
};


// builds the space group list to be displayed in the combo box
CreatePuckGrid.prototype._setSpaceGroupList = function (data) {
	this.spaceGrouplist = [];
	if (data && data.spaceGroupList) {
		for (var i = 0; i < data.spaceGroupList.length; i++) {
			var value = [];
			value.spaceGroup = data.spaceGroupList[i];
			this.spaceGroupList.push(value);
		}
	}
};

// builds the experiment type list to be displayed in the combo box
CreatePuckGrid.prototype._setExperimentTypeList = function (data) {
	this.experimentTypeList = [];
	if (data && data.experimentTypeList) {
		for (var i = 0; i < data.experimentTypeList.length; i++) {
			var value = [];
			value.experimentType = data.experimentTypeList[i];
			this.experimentTypeList.push(value);
		}
	}
};


// builds the crystal class list to be displayed in the combo box
CreatePuckGrid.prototype._setCrystalValuesList = function (data) {
	this.crystalValuesList = [];
	if (data && data.crystalValuesList) {
		for (var i = 0; i < data.crystalValuesList.length; i++) {
			var value = data.crystalValuesList[i];
			this.crystalValuesList.push(value);
		}
	}
};


// builds and returns the grid: set the lists, initializes the grid and build it
CreatePuckGrid.prototype.getGrid = function (data) {
	this.puckData =  data;
	this._setProteinList(data);
	this._setSpaceGroupList(data);
	this._setExperimentTypeList(data);
	this._setCrystalValuesList(data);

	this.initGrid();
	this.updateListSamples(data);
	return this.renderGrid();
};

// initGrid: set the position value and the default values
CreatePuckGrid.prototype.initGrid = function () {
	this.features = [];
	for (var i = 0; i < 16; i++) {
		this.features.push({
			sampleId : -1,
			position : i + 1,
			// sampleName: "sample"+this._formatPostionNumber(i+1),
			experimentType : "Default"
		});
	}
};

// refresh the grid with new data
CreatePuckGrid.prototype.updateListSamples = function (data) {
	for (var i = 0; i < data.listSamples.length; i++) {
		var pos = data.listSamples[i].position;
		this.features[pos - 1] = data.listSamples[i];
		this.features[pos - 1].position = Number(data.listSamples[i].position);
	}
	// change the button title
	if (data.listSamples.length > 0) {
		this.title = "Update Puck";
		//this.saveButton = "Update";
	} else {
		this.title = "Create Puck";
		//this.saveButton = "Save";
	}
	this.fillShipmentMode  = false;
	if (data && data.fillShipmentMode && data.fillShipmentMode == true) {
		this.title = "";
		this.fillShipmentMode  = true;
		//this.saveButton = "";
	}
	
};

// format the position number on 2 digit, in order to have a correct sort
CreatePuckGrid.prototype._formatPostionNumber = function (i) {
	if (i && i < 16) {
		return "0" + i;
	}
	return i;
};

// refresh the grid: refresh the samples and reload the data
CreatePuckGrid.prototype.refresh = function (data) {
	this.initGrid();
	this.puckData = data;
	this.updateListSamples(data);

	this.store.loadData(this.features, false);
	this._sort(this.store);
};


// no actions
CreatePuckGrid.prototype.getActions = function () {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};


// builds and returns the grid
CreatePuckGrid.prototype.renderGrid = function () {
	var _this = this;

	/** Prepare data * */
	var columns = this._getColumns();
	// data to be displayed in the grid
	this.store = Ext.create('Ext.data.Store', {
		model : 'SampleModel',
		autoload : false
	});

	this._sort(this.store);
	this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);

	// edition if a cell, possible on a click
	// some events are thrown after edition: 
	// change sample name: change sample name automatically
	// change protein: set the  values linked to the crystal form
	// change experiment type: disable or not some values
	
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
		clicksToEdit : 1,
		listeners : {
			afteredit : function (e, eOpts) {
				var rec = eOpts.record;
				var field = eOpts.field;
				var val = eOpts.value;
				var idRow = eOpts.rowIdx;
				if (field == 'sampleName') {
					_this.changeSampleName(val, idRow);
				} else if (field == 'proteinAcronym') {
					_this.changeProteinAcronym(val, idRow);
				} else if (field == 'experimentType') {
					_this.changeExperimentType(val, idRow);
				}
			}
		}
	});

	// top bar
	var tbar = [];
	// edit puck label, copy puck, cut puck
	if (_this.fillShipmentMode == true) {
		tbar.push({
			tooltip : {
				text : 'Edit the puck label',
				title : 'Edit puck'
			},
			icon : '../images/Edit_16x16_01.png',
			handler : function () {
				_this.editPuck();
			}
					
		}, {xtype: 'tbseparator'} 
		);
		tbar.push( 
				{
				tooltip : {
					text : 'Copy the puck with the samples. Then you can paste it in any dewar; it will create a new puck with the same samples.',
					title : 'Copy puck'
				},
				id: 'button_copy_' + _this.puckData.puckInfo.containerId,
				icon : '../images/editcopy.png', 
				handler : function () {
					_this.copyPuck();
				}
						
			}, 
				{
				tooltip : {
					text : 'Copy the puck and remove it. Then you can paste it in any dewar.',
					title : 'Cut puck'
				},
				id: 'button_cut_' + _this.puckData.puckInfo.containerId,
				icon : '../images/editcut.png'
						
			}, {xtype: 'tbseparator'});
	}
	// save button, reset, change sample name automatically
	tbar.push({
			text : this.saveButton,
			tooltip : {
				text : 'Save all samples contained in this puck',
				title : 'Save samples'
			},
			icon : '../images/save.gif',
			handler : function () {
				_this.onSaveButtonClicked.notify({
					'listSamples': _this.getListSamples(), 
					'puckInfo': _this.puckData.puckInfo
				});
			}
		}, 
		{xtype: 'tbseparator'}, {
			text : 'Reset',
			tooltip : {
				text : 'Reset the samples table: all fields are set to their default value.',
				title : 'Reset the samples'
			},
			icon : '../images/recur.png',
			handler : function () {
				_this.reset();
			}
		}, {
			xtype : 'checkboxfield',
			name : 'cbchangeName',
			boxLabel : 'Change sample name automatically',
			checked : false,
			inputValue : 'all',
			handler : function (field, value) {
				var checkValue = field.getValue();
				_this.changeSampleNameAuto = checkValue;
			}
		});
	// remove puck
	if (_this.fillShipmentMode == true) {
		tbar.push({xtype: 'tbseparator'}, 
				{
				tooltip : {
					text : 'Remove this puck together with samples inside',
					title : 'Delete puck'
				},
				icon : '../images/cancel.png', 
				handler : function () {
					_this.deletePuck();
				}
						
			});
	}

	// build grid
	this.grid = Ext.create('Ext.grid.Panel', {
			style : {
				padding : 0
			},
			width : "100%",
			model : 'SampleModel',
			height : "100%",
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
			tbar : tbar
		});
	// depending of the value of the experiment type, the oscillation range column could be disabled
	this.grid.on('beforeedit', function (editor, a, eOpts) {
		var idCol = a.colIdx;
		var isOscRange = this.columns[idCol].dataIndex == 'oscillationRange';
		
		var idRow = a.record.index;
		var experimentType = _this.getExperimentTypeValue(idRow);
		if (isOscRange && !_this.canEditOscillationRange(experimentType)) {
			return false;
		}
		return true;
    });


	return this.grid;
};

// get the experiment type value at the given id row
CreatePuckGrid.prototype.getExperimentTypeValue = function (idRow) {
	return this.grid.store.getAt(idRow).get('experimentType');
};


// edit puck clikc: throws an event 
CreatePuckGrid.prototype.editPuck = function () {
	var _this = this;
	_this.onEditPuckButtonClicked.notify({'containerId' : _this.puckData.puckInfo.containerId, 
	'puckName' : _this.puckData.puckInfo.code});
};

// changes sample automatically
CreatePuckGrid.prototype.changeSampleName = function (value, idRow) {
	if (this.changeSampleNameAuto && value) {
		var l = value.length;
		var c = 0;
		// parse the sample name
		if (l >= 1) {
			var e = value.substring(l - 1);
			if (!isNaN(parseFloat(e)) && isFinite(e)) {
				c = 1;
			}
		}
		if (l >= 2) {
			var e = value.substring(l - 2);
			if (!isNaN(parseFloat(e)) && isFinite(e)) {
				c = 2;
			}
		}
		var newValue = value.substring(0, l - c);
		var proteinValue = this.grid.store.getAt(idRow).data.proteinAcronym;
		var idToIncrement = 1;
		// set the new values automatically for the same protein acronym
		for (var i = 0; i < 16; i++) {
			var currentProtein = this.grid.store.getAt(i).data.proteinAcronym;
			if (proteinValue == currentProtein) {
				this.grid.store.getAt(i).set('sampleName',
					newValue + this._formatPostionNumber(idToIncrement));
				idToIncrement = idToIncrement + 1;
			}
		}
	}
};


// builds the columns for the puck grid
CreatePuckGrid.prototype._getColumns = function () {
	var _this = this;

	// render: wrap column value
	function columnWrap(value) {
		if (value){
			return '<div style="white-space:normal !important;word-wrap: break-word;">' + value + '</div>';
		}else{
			return "";
		}
	}
	
	// render sample name: light red color background
	function renderSample(value, metadata) {
		metadata.tdAttr = 'style="background-color: #FFEBEB"';
		if (value) {
			return '<div style="white-space:normal !important;word-wrap: break-word;">' + value + '</div>';
		} else {
			return "";
		}
	}

	// render protein:  light red color background
	function renderProtein(value, metadata) {
		metadata.tdAttr = 'style="background-color: #FFEBEB"';
		return value;
	}
	
	// oscillation: disabled depending of the experiment type
	function renderOscillationRange(value, metadata, record) {
		var experimentType = _this.getExperimentTypeValue(record.index);
		if (!_this.canEditOscillationRange(experimentType)) {
			metadata.tdAttr = 'style="background-color: #EBE8E8"';
		}
		
		return value;
	}
	
	// create reusable renderer
	Ext.util.Format.comboRenderer = function (combo) {
		return function (value) {
			var record = combo.findRecord(combo.valueField, value);
			return record ? record.get(combo.displayField): combo.valueNotFoundText;
		};
	};

	// format number on 3 digit
	function renderNumberFormat3(value, p, record) {
		if (value == null) {
			return "";
		}
		return Number(value).toFixed(3);
	}

	// format number on 1 digit
	function renderNumberFormat1(value, p, record) {
		if (value == null) {
			return "";
		}
		return Number(value).toFixed(1);
	}

	// render position (without formating)
	function renderPosition(value, metadata, p, record) {
		if (value == null){
			return "";
		}
		if (value > 10){
			metadata.tdAttr = 'style="background-color: #ffcc66"';
		}	
		return Number(value);
	}

	// create the combo instance
	// for protein, space group, experiment type
	var proteinClassStore = Ext.create('Ext.data.Store', {
		id : 0,
		fields : ['proteinAcronym'],
		data : _this.proteinList
	});

	var spaceGroupClassStore = Ext.create('Ext.data.Store', {
		id : 0,
		fields : ['spaceGroup'],
		data : _this.spaceGroupList
	});

	var experimentTypeClassStore = Ext.create('Ext.data.Store', {
		id : 0,
		fields : ['experimentType'],
		data : _this.experimentTypeList
	});

	var comboProtein = new Ext.form.ComboBox({
		mode : 'local',
		emptyText : '---',
		store : proteinClassStore,
		valueField : 'proteinAcronym',
		displayField : 'proteinAcronym',
		queryMode: 'local',
		allowBlank : false,
		typeAhead : true,
		triggerAction : 'all', 
		listConfig: {cls: 'small-combo-text'}
	});
	var comboSpaceGroup = new Ext.form.ComboBox({
		mode : 'local',
		emptyText : '---',
		store : spaceGroupClassStore,
		valueField : 'spaceGroup',
		displayField : 'spaceGroup',
		allowBlank : true,
		queryMode: 'local',
		typeAhead : true,
		triggerAction : 'all'
	});

	var comboExperimentType = new Ext.form.ComboBox({
		mode : 'local',
		emptyText : '---',
		store : experimentTypeClassStore,
		valueField : 'experimentType',
		displayField : 'experimentType',
		allowBlank : false,
		queryMode: 'local',
		typeAhead : true,
		triggerAction : 'all', 
		listConfig: {cls: 'small-combo-text'}
	});
	comboExperimentType.setValue("Default");

	// builds the columns
	var columns = [{
		text : 'Sample<br>position',
		dataIndex : 'position',
		flex : 0.04,
		renderer : renderPosition
	}, {
		text : 'Protein<br>acronym (*)',
		dataIndex : 'proteinAcronym',
		flex : 0.08,
		editor : comboProtein,
		renderer : renderProtein
	}, {
		text : 'Sample<br>name (*)',
		dataIndex : 'sampleName',
		flex : 0.09,
		editor : {
			xtype : 'textfield',
			allowBlank : false,
			maxLength : 50,
			regex: /^[a-zA-Z0-9_-]+$/,
			regexText: 'Make sure the sample name contains only a-z , A-Z or 0-9 or - or _ characters!',
			msgTarget: 'under'
		},
		renderer : renderSample
	}, {
		text : 'Pin<br>barcode',
		dataIndex : 'pinBarcode',
		flex : 0.07,
		editor : {
			xtype : 'textfield'
		}
	}, {
		text : 'Space<br>group',
		dataIndex : 'spaceGroup',
		flex : 0.075,
		editor : comboSpaceGroup // specify reference to combo instance
		//renderer : Ext.util.Format.comboRenderer(comboSpaceGroup)
	}, {
		text : 'Pre-observed<br>resolution',
		dataIndex : 'preObservedResolution',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Needed<br>resolution',
		dataIndex : 'neededResolution',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Preferred<br>beam<br>diameter',
		dataIndex : 'preferredBeamDiameter',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Min.<br>osc.<br>width',
		dataIndex : 'minOscWidth',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Experiment<br>type',
		dataIndex : 'experimentType',
		flex : 0.075,
		editor : comboExperimentType // specify reference to combo
										// instance
		//renderer : Ext.util.Format.comboRenderer(comboExperimentType)
	}, {
		text : 'Number<br>of<br>positions',
		dataIndex : 'numberOfPositions',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Radiation<br>sensitivity',
		dataIndex : 'radiationSensitivity',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Aimed<br>multiplicity',
		dataIndex : 'aimedMultiplicity',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Aimed<br>completeness',
		dataIndex : 'aimedCompleteness',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Unit<br>Cell a',
		dataIndex : 'unitCellA',
		flex : 0.04,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Unit<br>Cell b',
		dataIndex : 'unitCellB',
		flex : 0.04,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Unit<br>Cell c',
		dataIndex : 'unitCellC',
		flex : 0.04,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Unit<br>Cell<br>alpha',
		dataIndex : 'unitCellAlpha',
		flex : 0.04,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Unit<br>Cell<br>beta',
		dataIndex : 'unitCellBeta',
		flex : 0.04,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : '<center>Unit<br>Cell<br>gamma</center>',
		dataIndex : 'unitCellGamma',
		flex : 0.04,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Axis<br>range',
		dataIndex : 'axisRange',
		flex : 0.1,
		editor : {
			xtype : 'textfield',
			allowBlank : true,
			maxLength : 10
		}
	}, 
	 {
		text : 'SMILES',
		dataIndex : 'smiles',
		flex : 0.08,
		editor : {
			xtype : 'textfield',
			allowBlank : true,
			maxLength : 400
		},
		renderer : columnWrap
	}, 
	{
		text : 'Comments',
		dataIndex : 'comments',
		flex : 0.1,
		editor : {
			xtype : 'textfield',
			allowBlank : true,
			maxLength : 1024
		},
		renderer : columnWrap
	}];

	// actions: copy paste sample. We can add a delete sample
	columns.push({
			xtype : 'actioncolumn',
			width : 50,
			header : 'Action',
			sortable : false,
			renderer: function (val, metadata, record) {
				this.items[1].disabled = !_this.canCopySample();
				return val;
			},
			items : [{
				icon : '../images/editcopy.png',
				tooltip : 'Copy sample',
				handler : function (grid, rowIndex, colIndex) {
					_this.copySample(_this.store.getAt(rowIndex).data);
				}
			}, 
			{
				icon : '../images/editpaste.png',
				tooltip : 'Paste sample',
				handler : function (grid, rowIndex, colIndex) {
					_this.pasteSample(rowIndex);
				}
			}]
		});
	return columns;

};

// copy sample click: set in memory the sample to copy and throws an event
CreatePuckGrid.prototype.copySample = function (data) {
	var _this = this;
	if (data){
		_this.sampleToCopy = data;
		_this.copyId = 1;
		_this.grid.getView().refresh();
		this.onCopySampleButtonClicked.notify({
			'sampleToCopy' : _this.sampleToCopy
		});
	}
};

// return true if a sample has been copied
CreatePuckGrid.prototype.canCopySample = function () {
	var _this = this;
	return _this.sampleToCopy && _this.sampleToCopy.position;
};

// set the sample to be copied in memory (could be a sample from another puck)
CreatePuckGrid.prototype.setSampleToCopy = function (sampleToCopy) {
	var _this = this;
	_this.sampleToCopy = sampleToCopy;
	if (!_this.copyId){
		_this.copyId = 1;
	}
	_this.grid.getView().refresh();
};

// paste the sample into the line
CreatePuckGrid.prototype.pasteSample = function (rowIndex) {
	var _this = this;
	if (_this.canCopySample()) {
		_this.grid.store.getAt(rowIndex).set('proteinAcronym', _this.sampleToCopy.proteinAcronym);
		_this.grid.store.getAt(rowIndex).set('sampleName', _this.sampleToCopy.sampleName + "_" + _this.copyId);
		_this.grid.store.getAt(rowIndex).set('pinBarcode', _this.sampleToCopy.pinBarcode);
		_this.grid.store.getAt(rowIndex).set('spaceGroup', _this.sampleToCopy.spaceGroup);
		_this.grid.store.getAt(rowIndex).set('preObservedResolution', _this.sampleToCopy.preObservedResolution);
		_this.grid.store.getAt(rowIndex).set('neededResolution', _this.sampleToCopy.neededResolution);
		_this.grid.store.getAt(rowIndex).set('preferredBeamDiameter', _this.sampleToCopy.preferredBeamDiameter);
		_this.grid.store.getAt(rowIndex).set('experimentType', _this.sampleToCopy.experimentType);
		_this.grid.store.getAt(rowIndex).set('numberOfPositions', _this.sampleToCopy.numberOfPositions);
		_this.grid.store.getAt(rowIndex).set('radiationSensitivity', _this.sampleToCopy.radiationSensitivity);
		_this.grid.store.getAt(rowIndex).set('aimedCompleteness', _this.sampleToCopy.aimedCompleteness);
		_this.grid.store.getAt(rowIndex).set('aimedMultiplicity', _this.sampleToCopy.aimedMultiplicity);

		_this.grid.store.getAt(rowIndex).set('unitCellA', _this.sampleToCopy.unitCellA);
		_this.grid.store.getAt(rowIndex).set('unitCellB', _this.sampleToCopy.unitCellB);
		_this.grid.store.getAt(rowIndex).set('unitCellC', _this.sampleToCopy.unitCellC);
		_this.grid.store.getAt(rowIndex).set('unitCellAlpha', _this.sampleToCopy.unitCellAlpha);
		_this.grid.store.getAt(rowIndex).set('unitCellBeta', _this.sampleToCopy.unitCellBeta);
		_this.grid.store.getAt(rowIndex).set('unitCellGamma', _this.sampleToCopy.unitCellGamma);
		_this.grid.store.getAt(rowIndex).set('smiles', _this.sampleToCopy.smiles);
		_this.grid.store.getAt(rowIndex).set('minOscWidth', _this.sampleToCopy.minOscWidth);
		_this.grid.store.getAt(rowIndex).set('axisRange', _this.sampleToCopy.axisRange);
		_this.grid.store.getAt(rowIndex).set('comments', _this.sampleToCopy.comments);
		
		_this.copyId = _this.copyId + 1;
		_this.changeSampleName(_this.sampleToCopy.sampleName, _this.sampleToCopy.position - 1);
	}
};

// returns the json data of samples in this grid
CreatePuckGrid.prototype.getListSamples = function () {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};

// returns the puck information
CreatePuckGrid.prototype.getPuckInfo = function () {
	var _this = this;
	var puckInfo = [];
	puckInfo.shipmentName = _this.puckData.puckInfo.shipmentName;
	puckInfo.dewarCode = _this.puckData.puckInfo.dewarCode;
	puckInfo.puckCode = _this.puckData.puckInfo.code;
	puckInfo.shippingId = _this.puckData.puckInfo.shippingId;
	puckInfo.dewarId = _this.puckData.puckInfo.dewarId;
	puckInfo.containerId = _this.puckData.puckInfo.containerId;
	return puckInfo;
};

			
// protein has been selected: change the values depending of the crystal form
CreatePuckGrid.prototype.changeProteinAcronym = function (newValue, idRow) {
	var crystal = this.getCellValue(newValue);
	var i = 0;
	this.grid.store.getAt(idRow).set('unitCellA', crystal.cellA);
	this.grid.store.getAt(idRow).set('unitCellB',  crystal.cellB);
	this.grid.store.getAt(idRow).set('unitCellC',  crystal.cellC);
	this.grid.store.getAt(idRow).set('unitCellAlpha', crystal.cellAlpha);
	this.grid.store.getAt(idRow).set('unitCellBeta', crystal.cellBeta);
	this.grid.store.getAt(idRow).set('unitCellGamma',  crystal.cellGamma);
	this.grid.store.getAt(idRow).set('spaceGroup',  crystal.spaceGroup);
	this.grid.store.getAt(idRow).set('comments',  crystal.comments);
	if (crystal.diffractionPlanVO) {
		this.grid.store.getAt(idRow).set('preferredBeamDiameter',  crystal.diffractionPlanVO.preferredBeamDiameter);
		this.grid.store.getAt(idRow).set('neededResolution',  crystal.diffractionPlanVO.requiredResolution);
		this.grid.store.getAt(idRow).set('preObservedResolution',  crystal.diffractionPlanVO.observedResolution);
		this.grid.store.getAt(idRow).set('experimentType',  crystal.diffractionPlanVO.experimentKind);
		this.grid.store.getAt(idRow).set('numberOfPositions',  crystal.diffractionPlanVO.numberOfPositions);
		this.grid.store.getAt(idRow).set('radiationSensitivity', crystal.diffractionPlanVO.radiationSensitivity);
		this.grid.store.getAt(idRow).set('aimedCompleteness', crystal.diffractionPlanVO.aimedCompleteness);
		this.grid.store.getAt(idRow).set('aimedMultiplicity', crystal.diffractionPlanVO.aimedMultiplicity);
		this.grid.store.getAt(idRow).set('minOscWidth', crystal.diffractionPlanVO.minOscWidth);
		this.grid.store.getAt(idRow).set('axisRange', crystal.diffractionPlanVO.axisRange);
	}
};

// returns true if the oscillation range can be edited
CreatePuckGrid.prototype.canEditOscillationRange = function (value) {
	return (value != "MXPressE" && value != "MXPressO" && value != "MXPressL" && value != "MXScore");
};

// experiment type has been selected: change the oscillation range value and set it disabled or not
CreatePuckGrid.prototype.changeExperimentType = function (newValue, idRow) {
	if (!this.canEditOscillationRange(newValue)) {
		this.grid.store.getAt(idRow).set('oscillationRange',  "");
	}
};

// search the crystal value from a protein value
CreatePuckGrid.prototype.getCellValue = function (proteinValue) {
	var cell = new Array(6);
	// initialize
	for (var i = 0; i < 7; i++) {
		cell[i] = "";
	}
	// get protein index
	var index = -1;
	for (var i = 0; i < this.proteinList.length; i++) {
		if (this.proteinList[i].proteinAcronym == proteinValue) {
			index = i;
			break;
		}
	}
	// search crystal values
	if (index != -1) {
		cell = this.crystalValuesList[index];
	}
	return cell;
};

// reset the whole grid
CreatePuckGrid.prototype.reset = function () {
	var f = [];
	for (var i = 0; i < 16; i++) {
		f.push({
					sampleId : this.features[i].sampleId,
					position : i + 1,
					// sampleName: "sample"+this._formatPostionNumber(i+1),
					experimentType : "Default"
				});
	}
	this.features = f;
	this.store.loadData(this.features, false);
};

// delete a puck click: after confirmation throws an event
CreatePuckGrid.prototype.deletePuck = function () {
	var _this = this;
	if (confirm("Do you want to remove this puck?")) {
		this.onRemovePuckButtonClicked.notify({
					'containerId' : _this.puckData.puckInfo.containerId
		});
	}
};

// copy puck click: throws an event
CreatePuckGrid.prototype.copyPuck = function () {
	this.onCopyPuckButtonClicked.notify({
		'containerId' : this.puckData.puckInfo.containerId, 
		'listSamples': this.getListSamples()
	});
};

