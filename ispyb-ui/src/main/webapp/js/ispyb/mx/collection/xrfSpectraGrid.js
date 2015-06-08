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
//XRFSpectra grid (in DataCollectionGroup page)
function XRFSpectraGrid(args) {

	this.onSaveButtonClicked = new Event(this);

	var isFx = false;
	this.contextPath = "";
	this.title = "XRFSpectra";
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

	// initializes the crystal class
	this._setCrystalClass();

	// xrfSpectra datamodel
	Ext.define('XRFSpectraModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'xfeFluorescenceSpectrumId',
							mapping : 'xfeFluorescenceSpectrumId'
						}, {
							name : 'fittedDataFileFullPath',
							mapping : 'fittedDataFileFullPath'
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
							name : 'jpegScanFileFullPath',
							mapping : 'jpegScanFileFullPath'
						}, {
							name : 'filename',
							mapping : 'filename'
						}, {
							name : 'energy',
							mapping : 'energy'
						}, {
							name : 'exposureTime',
							mapping : 'exposureTime'
						}, {
							name : 'beamTransmission',
							mapping : 'beamTransmission'
						}, {
							name : 'annotatedPymcaXfeSpectrum',
							mapping : 'annotatedPymcaXfeSpectrum'
						}, {
							name : 'beamSizeVertical',
							mapping : 'beamSizeVertical'
						}, {
							name : 'beamSizeHorizontal',
							mapping : 'beamSizeHorizontal'
						}, {
							name : 'blSampleVO',
							mapping : 'blSampleVO'
						}, {
							name : 'jpegScanExists',
							mapping : 'jpegScanExists'
						}, {
							name : 'jpegScanFileFullPathParser',
							mapping : 'jpegScanFileFullPathParser'
						}, {
							name : 'annotatedPymcaXfeSpectrumExists',
							mapping : 'annotatedPymcaXfeSpectrumExists'
						}, {
							name : 'shortFileNameAnnotatedPymcaXfeSpectrum',
							mapping : 'shortFileNameAnnotatedPymcaXfeSpectrum'
						}],
				idProperty : 'xfeFluorescenceSpectrumId'
			});
}


// no filters
XRFSpectraGrid.prototype.getFilterTypes = function() {
	return [];
};


// sort by default by inverse time
XRFSpectraGrid.prototype._sort = function(store) {
	store.sort('startTime', 'DESC');
};


// prepare crystal class data
XRFSpectraGrid.prototype._setCrystalClass = function() {
	this.crystalClass = [];
	for (var i = 0; i < this.listOfCrystalClass.length; i++) {
		this.crystalClass.push(listOfCrystalClass[i].crystalClassName);
	}
};

// get the grids
XRFSpectraGrid.prototype.getGrid = function(xfeSpectrums) {
	this.features = xfeSpectrums;
	return this.renderGrid(xfeSpectrums);
};

// refresh data: reload
XRFSpectraGrid.prototype.refresh = function(data) {
	this.features = data.xrfSpectraList;
	this.store.loadData(this.features, false);
};


// no actions
XRFSpectraGrid.prototype.getActions = function() {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};


// builds the grid
XRFSpectraGrid.prototype.renderGrid = function() {
	var _this = this;

	/** Prepare data * */
	var data = [];
	// set the columns
	var columns = this._getColumns();
	// data for the grid
	this.store = Ext.create('Ext.data.Store', {
				model : 'XRFSpectraModel',
				autoload : false
			});

	this._sort(this.store);
	this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);

	//if no data : just a panel
	var noXFRDataPanel = Ext.create('Ext.Panel', {
				id : 'noXFRDataPanel',
				layout : {
					type : 'fit'
				},
				html : '<html><h4>No XRF Spectra found</h4></html>'
			});

	if (this.features.length == 0)
		return noXFRDataPanel;

	// this grid can be edited
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
				clicksToEdit : 1
			});

	// top bar with buttons
	var bar = [];
	if (this.canSave){
		bar.push({
							text : 'Save Comments',
							tooltip : 'Save XRF Spectra',
							icon : '../images/save.gif',
							handler : function() {
								_this.onSaveButtonClicked.notify({});
							}
						});
	}
	// creates the grid
	this.grid = Ext.create('Ext.grid.Panel', {
				style : {
					padding : 0
				},
				width : "100%",
				model : 'XRFSpectraModel',
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

// returns the json data with XRFSpectra
XRFSpectraGrid.prototype.getListXfeSpectra = function() {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};


// returns the sessionId 
XRFSpectraGrid.prototype.getSessionId = function() {
	return this.sessionId;
};


// builds columns
XRFSpectraGrid.prototype._getColumns = function() {
	var _this = this;
	
	// render date
	function renderStartDate(value, p, record) {
		return Ext.Date.format(value, 'd-m-Y H:i:s');
	}

	// render a column wrap
	function columnWrap(value) {
		if (value)
			return '<div style="white-space:normal !important;word-wrap: break-word;">' + value + '</div>';
		else
			return "";
	}

	// render crystal class column
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
	Ext.util.Format.comboRenderer = function(combo) {
		return function(value) {
			var record = combo.findRecord(combo.valueField, value);
			return record ? record.get(combo.displayField) : combo.valueNotFoundText;
		};
	};

	// format at 3 digits
	function renderNumberFormat3(value, p, record) {
		if (value == null)
			return "";
		return Number(value).toFixed(3);
	}

	// format at 1 digit
	function renderNumberFormat1(value, p, record) {
		if (value == null)
			return "";
		return Number(value).toFixed(1);
	}

	// render spectrum image
	function renderSpectrum(value, p, record) {
		if (!record.data.jpegScanExists) {
			return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Spectrum not found:<br>" + value + "</h4></div>";
		} else {
			return "<a href='viewResults.do?reqCode=viewJpegImageFromFile&file=" + 
					record.data.jpegScanFileFullPathParser + 
					"'>" + 
					"<img width='150' height='110' src='imageDownload.do?reqCode=getImageJpgFromFile&file=" + 
					record.data.jpegScanFileFullPathParser + 
					"'  border='0' alt='Click to zoom the image' ></a>";
		}
	}

	// render html report
	function renderHtmlReport(value, p, record) {
		if (!record.data.annotatedPymcaXfeSpectrumExists) {
			return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Html report not found:<br>" + value + "</h4></div>";
		} else {
			return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
					"<a  href='viewDataCollection.do?reqCode=downloadFile&fullFilePath= " + 
					record.data.annotatedPymcaXfeSpectrum + 
					"'  styleClass='LIST'>" + 
					"<img src='../images/download.png' alt='Click to download the report' >" + 
					"</a><br>" + 
					"<a  href='viewResults.do?reqCode=displayHtmlFile&HTML_FILE=" + 
					record.data.annotatedPymcaXfeSpectrum + 
					"'  styleClass='LIST'>" + 
					record.data.shortFileNameAnnotatedPymcaXfeSpectrum + 
					"</a></div>";
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
	
	// columns
	var columns = [{
				text : 'Energy<br>(keV)',
				dataIndex : 'energy',
				flex : 0.075,
				renderer : renderNumberFormat3,
				id : 'energy'
			}, {
				text : 'Sample name',
				dataIndex : 'sampleName',
				flex : 0.10
			},{
				text : 'Exposure<br>Time<br>(s)',
				dataIndex : 'exposureTime',
				flex : 0.075,
				id : 'exposureTime'
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
				text : 'Beam&nbsp;size Hor.<br>(&#181m)',
				dataIndex : 'beamSizeHorizontal',
				flex : 0.15
			}, {
				text : 'Beam&nbsp;size Ver.<br>(&#181m)',
				dataIndex : 'beamSizeVertical',
				flex : 0.15
			}, {
				text : 'Transm.<br>Factor<br>(%)',
				dataIndex : 'beamTransmission',
				flex : 0.15
			}, {
				text : 'Spectrum',
				dataIndex : 'jpegScanFileFullPath',
				renderer : renderSpectrum,
				flex : 0.2
			}, {
				text : 'Html Report',
				dataIndex : 'annotatedPymcaXfeSpectrum',
				renderer : renderHtmlReport,
				flex : 0.15
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
	
	if (this.canSave){
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
	}else{
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				flex : 0.2,
				renderer : columnWrap
			});
	}
	return columns;

};

// return the data
XRFSpectraGrid.prototype.getListXRFSpectra = function() {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};
