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

//Energy Scan grid  in the dataCollectionGroup page
function EnergyScanGrid(args) {

	this.onSaveButtonClicked = new Event(this);

	var isFx = false;
	this.contextPath = "";
	this.title = "EnergyScan";
	this.listOfCrystalClass = [];
	this.canSave = false;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.isFx != null) {
			this.isFx = args.isFx;
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

	// format crystal class data
	this._setCrystalClass();

	// Energy Scan data model
	Ext.define('EnergyScanModel', {
			extend : 'Ext.data.Model',
			fields : [{
					name : 'energyScanId',
					mapping : 'energyScanId'
				}, {
					name : 'fluorescenceDetector',
					mapping : 'fluorescenceDetector'
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
					name : 'sampleName',
					mapping : 'sampleName'
				},  {
					name : 'crystalClass'
				}, {
					name : 'comments',
					mapping : 'comments'
				}, {
					name : 'scanFileFullPath',
					mapping : 'scanFileFullPath'
				}, {
					name : 'choochFileFullPath',
					mapping : 'choochFileFullPath'
				}, {
					name : 'jpegChoochFileFullPath',
					mapping : 'jpegChoochFileFullPath'
				}, {
					name : 'element',
					mapping : 'element'
				}, {
					name : 'startEnergy',
					mapping : 'startEnergy'
				}, {
					name : 'endEnergy',
					mapping : 'endEnergy'
				}, {
					name : 'transmissionFactor',
					mapping : 'transmissionFactor'
				}, {
					name : 'exposureTime',
					mapping : 'exposureTime'
				}, {
					name : 'synchrotronCurrent',
					mapping : 'synchrotronCurrent'
				}, {
					name : 'temperature',
					mapping : 'temperature'
				}, {
					name : 'peakEnergy',
					mapping : 'peakEnergy'
				}, {
					name : 'peakFPrime',
					mapping : 'peakFPrime'
				}, {
					name : 'peakFDoublePrime',
					mapping : 'peakFDoublePrime'
				}, {
					name : 'inflectionEnergy',
					mapping : 'inflectionEnergy'
				}, {
					name : 'inflectionFPrime',
					mapping : 'inflectionFPrime'
				}, {
					name : 'inflectionFDoublePrime',
					mapping : 'inflectionFDoublePrime'
				}, {
                    name : 'remoteEnergy',
                    mapping : 'remoteEnergy'
                }, {
                    name : 'remoteFPrime',
                    mapping : 'remoteFPrime'
                }, {
                    name : 'remoteFDoublePrime',
                    mapping : 'remoteFDoublePrime'
                }, {
					name : 'xrayDose',
					mapping : 'xrayDose'
				}, {
					name : 'edgeEnergy',
					mapping : 'edgeEnergy'
				}, {
					name : 'filename',
					mapping : 'filename'
				}, {
					name : 'beamSizeVertical',
					mapping : 'beamSizeVertical'
				}, {
					name : 'beamSizeHorizontal',
					mapping : 'beamSizeHorizontal'
				}, {
					name : 'scanFileFullPathExists',
					mapping : 'scanFileFullPathExists'
				}, {
					name : 'shortFileName',
					mapping : 'shortFileName'
				}, {
					name : 'choochExists',
					mapping : 'choochExists'
				}, {
					name : 'jpegChoochFileFitPath',
					mapping : 'jpegChoochFileFitPath'
				}],
			idProperty : 'energyScanId'
		});
}


// no filters
EnergyScanGrid.prototype.getFilterTypes = function () {
	return [];
};

// sort by default by time
EnergyScanGrid.prototype._sort = function (store) {
	store.sort('startTime', 'DESC');
};

// prepare crystal class names
EnergyScanGrid.prototype._setCrystalClass = function () {
	this.crystalClass = [];
	for (var i = 0; i < this.listOfCrystalClass.length; i++) {
		this.crystalClass.push(this.listOfCrystalClass[i].crystalClassName);
	}
};

// get Grid
EnergyScanGrid.prototype.getGrid = function (energyScans) {
	this.features = energyScans;
	return this.renderGrid(energyScans);
};


// refresh data: upload
EnergyScanGrid.prototype.refresh = function (data) {
	this.features = data.energyScansList;
	this.store.loadData(this.features, false);
};

// no actions
EnergyScanGrid.prototype.getActions = function () {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};


// builds the grid
EnergyScanGrid.prototype.renderGrid = function () {
	var _this = this;

	/** Prepare data * */
	var data = [];
	// get columns
	var columns = this._getColumns();
	// data for grid
	this.store = Ext.create('Ext.data.Store', {
			model : 'EnergyScanModel',
			autoload : false
		});

	this._sort(this.store);
	this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);

	// if no data:
	var noEnergyScanDataPanel = Ext.create('Ext.Panel', {
			id : 'noEnergyScanDataPanel',
			layout : {
				type : 'fit'
			},
			html : '<html><h4>No Energy Scans have been found</h4></html>'
		});

	if (this.features.length == 0) { 
		return noEnergyScanDataPanel;
	}

	// this grid can be edited
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});

	// top bar buttons
	var bar = [];
	if (this.canSave) {
		bar.push({
				text : 'Save Comments',
				tooltip : 'Save energy scans',
				icon : '../images/save.gif',
				handler : function () {
					_this.onSaveButtonClicked.notify({});
				}
			});
	}
	// grid
	this.grid = Ext.create('Ext.grid.Panel', {
			style : {
				padding : 0
			},
			width : "100%",
			model : 'EnergyScanModel',
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
			tbar : bar

		});

	return this.grid;
};


// returns the energy Scan list as json
EnergyScanGrid.prototype.getListEnergyScan = function () {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};

// returns the sessionId
EnergyScanGrid.prototype.getSessionId = function () {
	return this.sessionId;
};


// build columns
EnergyScanGrid.prototype._getColumns = function () {
	var _this = this;
	
	// render functions
	// render date
	function renderStartDate(value, p, record) {
		return Ext.Date.format(value, 'd-m-Y H:i:s');
	}

	// render column wrap
	function columnWrap(value) {
		if (value) { 
			return '<div style="white-space:normal !important;word-wrap: break-word;">' + value + '</div>';
		}
		else {
			return "";
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

	// format on 3 digits
	function renderNumberFormat3(value, p, record) {
		if (value === null) { 
			return "";
		}
		return Number(value).toFixed(3);
	}

	// format on 1digit
	function renderNumberFormat1(value, p, record) {
		if (value === null) { 
			return "";
		}
		return Number(value).toFixed(1);
	}

	// render energy scan file
	function renderScanFile(value, p, record) {
		if (!record.data.scanFileFullPath) {
			return "";
		} else {
			if (!record.data.scanFileFullPathExists) {
				return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Raw data file not found:<br>" + value + "</h4></div>";
			} else {
				return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
						"<a  href='viewDataCollection.do?reqCode=downloadFile&fullFilePath= " + 
						value + 
						"'  styleClass='LIST'>" + 
						record.data.shortFileName + "</a></div>";
			}
		}
	}

	// render image output
	function renderChoochOutput(value, p, record) {
		if (!record.data.jpegChoochFileFullPath) {
			return "";
		} else {
			if (!record.data.choochExists) {
				return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Chooch output not found:<br>" + value + "</h4></div>";
			} else {
				return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
						"<a  href='viewResults.do?reqCode=viewJpegImageFromFile&file=" + 
						record.data.jpegChoochFileFitPath + 
						"'  styleClass='LIST'>" + 
						"<img width='150' height='110' src='imageDownload.do?reqCode=getImageJpgFromFile&file=" + 
						record.data.jpegChoochFileFitPath + 
						"' border='0' alt='Click to zoom the image' >" + 
						"</a></div>";
			}
		}
	}

	// create the combo instance

	var crystalClassStore = Ext.create('Ext.data.Store', {
			id : 0,
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
	
	// build columns
	var columns = [{
			text : 'Element',
			dataIndex : 'element',
			flex : 0.05,
			id : 'element'
		}, {
			text : 'Sample name',
			dataIndex : 'sampleName',
			flex : 0.10
		},{
			text : 'Inflection<br>Energy<br>(keV)',
			dataIndex : 'inflectionEnergy',
			flex : 0.075,
			renderer : renderNumberFormat3,
			id : 'inflectionEnergy'
		}, {
			text : 'Exposure<br>Time<br>(s)',
			dataIndex : 'exposureTime',
			flex : 0.075,
			renderer : renderNumberFormat1,
			id : 'exposureTime'
		}, {
			text : 'Inflection <br>f\'<br>(e)',
			dataIndex : 'inflectionFPrime',
			flex : 0.075,
			renderer : renderNumberFormat1,
			id : 'inflectionFPrime'
		}, {
			text : 'Inflection <br>f\'\'<br>(e)',
			dataIndex : 'inflectionFDoublePrime',
			flex : 0.075,
			renderer : renderNumberFormat1,
			id : 'inflectionFDoublePrime'
		}, {
			text : 'Peak<br>Energy<br>(keV)',
			dataIndex : 'peakEnergy',
			flex : 0.05,
			renderer : renderNumberFormat3,
			id : 'peakEnergy'
		}, {
			text : 'Peak <br>f\'<br>(e)',
			dataIndex : 'peakFPrime',
			flex : 0.05,
			renderer : renderNumberFormat1,
			id : 'peakFPrime'
		}, {
			text : 'Peak <br>f\'\'<br>(e)',
			dataIndex : 'peakFDoublePrime',
			flex : 0.05,
			renderer : renderNumberFormat1,
			id : 'peakFDoublePrime'
		}, {
            text : 'Remote<br>Energy<br>(keV)',
            dataIndex : 'remoteEnergy',
            flex : 0.05,
            renderer : renderNumberFormat3,
            id : 'remoteEnergy'
        }, {
            text : 'Remote <br>f\'<br>(e)',
            dataIndex : 'remoteFPrime',
            flex : 0.05,
            renderer : renderNumberFormat1,
            id : 'remoteFPrime'
        }, {
            text : 'Remote <br>f\'\'<br>(e)',
            dataIndex : 'remoteFDoublePrime',
            flex : 0.05,
            renderer : renderNumberFormat1,
            id : 'remoteFDoublePrime'
        }, {
			text : 'Start Time',
			dataIndex : 'startTime',
			renderer : Ext.util.Format.dateRenderer('d-m-Y H:i:s'),
			flex : 0.15
		},

		{
			text : 'End Time',
			dataIndex : 'endTime',
			renderer : Ext.util.Format.dateRenderer('d-m-Y H:i:s'),
			flex : 0.15
		}, {
			text : 'Beam<br>size Hor.<br>(&#181m)',
			dataIndex : 'beamSizeHorizontal',
			flex : 0.05,
			id : 'beamSizeHorizontal'
		}, {
			text : 'Beam<br>size Ver.<br>(&#181m)',
			dataIndex : 'beamSizeVertical',
			flex : 0.05,
			id : 'beamSizeVertical'
		}, {
			text : 'Transm.<br>Factor<br>(%)',
			dataIndex : 'transmissionFactor',
			flex : 0.05,
			renderer : renderNumberFormat3,
			id : 'transmissionFactor'
		}, {
			text : 'Raw data File',
			dataIndex : 'scanFileFullPath',
			flex : 0.2,
			renderer : renderScanFile,
			id : 'scanFileFullPath'
		}, {
			text : 'Chooch Output',
			dataIndex : 'jpegChoochFileFullPath',
			flex : 0.2,
			renderer : renderChoochOutput,
			id : 'jpegChoochFileFullPath'
		}];
	if (_this.isFx) {
		columns.push({
			text : 'Crystal class',
			dataIndex : 'crystalClass',
			flex : 0.2,
			editor : combo, // specify reference to combo instance
			renderer : Ext.util.Format.comboRenderer(combo)
				// pass combo instance to reusable renderer
		});
	}
	if (this.canSave) {
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

	return columns;

};

EnergyScanGrid.prototype.getListEnergyScan = function () {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};
