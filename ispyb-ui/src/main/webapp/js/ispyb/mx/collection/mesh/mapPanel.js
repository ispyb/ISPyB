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

// main panel with mesh map
function MapPanel(args) {
	var _this = this;
	this.width = 1350;
	this.height = 500;
	this.mapTitle = 'Map title';
	this.minValue = 0;
	this.maxValue = 1000;
	this.cellWidthValue = 30;
	
	this.id = 0;

	this.grid = null;
	this.snapshot = null;
	this.axisXName = null;
	this.axisYName = null;

	this.stepX = 0.05;
	this.stepY = 0.05;

	this.nbDecX = this.getNbDec("" + this.stepX);
	this.nbDecY = this.getNbDec("" + this.stepY);

	this.axisXName = "GridIndexY";
	this.axisYName = "GridIndexZ";

	this.colorRangeManager = null;

	this.data = null;
	this.positions = null;
	this.images = null;
	this.meshValues = null;
	this.scaleDataX = null;
	this.scaleDataY = null;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	// Define the model for a Criteria
	Ext.define('CriteriaPlotModel', {
			extend : 'Ext.data.Model',
			fields : [{
					type : 'string',
					name : 'id'
				}, {
					type : 'string',
					name : 'name'
				}]
		});
}

MapPanel.prototype.getNbDec = function (value) {
	var sep = value.indexOf(".") + 1;
	return value.length - sep;
};

MapPanel.prototype.getValue = function (value, nbDec) {
	if (value) { 
		return (value).toFixed(nbDec);
	}
	else { 
		return null;
	}
};

MapPanel.prototype.getValueRound = function (value, nbDec, step) {
	var val = this.getValue(value, nbDec);
	var n = 1 / (step * 2);
	return (Math.floor((val * n)) / n);
};

MapPanel.prototype.getScaleDataX = function (meshData) {
	if (meshData.length > 0) {
		// var x = this.getValueRound(meshData[0].phiY, this.nbDecX,
		// this.stepX);
		var x = meshData[0].motorPosition.gridIndexY;
		var min = x;
		var max = x;
		for (var i = 1; i < meshData.length; i++) {
			// var x = this.getValueRound(meshData[i].phiY, this.nbDecX,
			// this.stepX);
			x = meshData[i].motorPosition.gridIndexY;
			if (x < min) {
				min = x;
			} else if (x > max) {
				max = x;
			}
		}
		// return array
		var array = [];
		var v = 0;
		if (min) {
			v = min;
		}
		while (v < max) {
			array.push(v);
			v = Number(v) + 1;
			// v = Number(v)+ Number(this.stepX);
			// v = Number(v.toFixed(this.nbDecX));
		}
		if (max) { 
			array.push(max);
		}
		else { 
			array.push(0);
		}
		return array;
	}
	return [];
};

MapPanel.prototype.getScaleDataY = function (meshData) {
	if (meshData.length > 0) {
		// var y = this.getValueRound(meshData[0].phiZ, this.nbDecY,
		// this.stepY);
		var y = meshData[0].motorPosition.gridIndexZ;
		var min = y;
		var max = y;
		for (var i = 1; i < meshData.length; i++) {
			// var y = this.getValueRound(meshData[i].phiZ, this.nbDecY,
			// this.stepY);
			y = meshData[i].motorPosition.gridIndexZ;
			if (y < min) {
				min = y;
			} else if (y > max) {
				max = y;
			}
		}
		// return array
		var array = [];
		var v = 0;
		if (min) {
			v = min;
		}
		while (v < max) {
			array.push(v);
			v = Number(v) + 1;
			// v = Number(v) + Number(this.stepY);
			// v = Number(v.toFixed(this.nbDecY));
		}
		if (max) {
			array.push(max);
		} else {
			array.push(0);
		}
		return array;
	}
	return [];
};

MapPanel.prototype.getValueForCriteria = function (d, criteria) {
	if (criteria == "totalSpots") {
		return d.spotTotal;
	} else if (criteria == "goodBraggCandidates") {
		return d.goodBraggCandidates;
	} else if (criteria == "method1Res") {
		return d.method1Res;
	} else if (criteria == "method2Res") {
		return d.method2Res;
	} else if (criteria == "totalIntegratedSignal") {
		return d.totalIntegratedSignal;
	}else if (criteria == "dozor_score") {
		return d.dozor_score;
	}
};

MapPanel.prototype.buildDataCriteria = function (criteria) {
	var nbRow = this.meshValues.length;
	this.data = new Array(nbRow);
	for (var i = 0; i < nbRow; i++) {
		var nbColumn = this.meshValues[i].length;
		this.data[i] = new Array(nbColumn);
		for (var j = 0; j < nbColumn; j++) {
			var d = this.meshValues[i][j];
			this.data[i][j] = this.getValueForCriteria(d, criteria);
		}
	}
};

MapPanel.prototype.buildData = function(meshData) {
	this.meshValues = [];
	if (meshData) {
		if (meshData.gridInfo && meshData.gridInfo.steps_x) {
			this.stepX = meshData.gridInfo.steps_x;
			this.nbDecX = this.getNbDec("" + this.stepX);
		}
		if (meshData.gridInfo && meshData.gridInfo.steps_y) {
			this.stepY = meshData.gridInfo.steps_y;
			this.nbDecY = this.getNbDec("" + this.stepY);
		}
		// scale: todo: how to retrieve the scale??
		this.scaleDataX = this.getScaleDataX(meshData);
		this.scaleDataY = this.getScaleDataY(meshData);
		var nbX = this.scaleDataX.length; // nb column
		var nbY = this.scaleDataY.length; // nb row
		// init array
		this.meshValues = new Array(nbY);
		this.positions = new Array(nbY);
		this.images = new Array(nbY);
		for (var i = 0; i < nbY; i++) {
			this.meshValues[i] = new Array(nbX);
			this.positions[i] = new Array(nbX);
			this.images[i] = new Array(nbX);
			for (var j = 0; j < nbX; j++) {
				this.meshValues[i][j] = this.getDefaultMeshData();
				this.positions[i][j] = "Unknown";
				this.positions[i][j] = "";
			}
		}
		// dispatch data
		for (var i = 0; i < meshData.length; i++) {
			var d = meshData[i];
			// var x = this.getValue(d.phiY, this.nbDecX);
			// var y = this.getValue(d.phiZ, this.nbDecY);
			var x = d.motorPosition.gridIndexY;
			var y = d.motorPosition.gridIndexZ;
			var pos = this.getCoordPos(x, y);
			this.meshValues[pos[0]][pos[1]] = d;
			var phiY = "";
			if (d.motorPosition.phiY) {
				phiY = d.motorPosition.phiY.toFixed(3);
			}
			var sampY = "";
			if (d.motorPosition.sampY) {
				sampY = d.motorPosition.sampY.toFixed(3);
			}
			var sampX = "";
			if (d.motorPosition.sampX) {
				sampX = d.motorPosition.sampX.toFixed(3);
			}
			this.positions[pos[0]][pos[1]] = "phiY=" + phiY + " sampX=" + sampX + " sampY=" + sampY;
			this.images[pos[0]][pos[1]] = d.imageId;
		}
		// build data
		this.buildDataCriteria("dozor_score");
	}
};

MapPanel.prototype.getDefaultMeshData = function() {
	var meshData = [];
	meshData.dataCollectionId = -1;
	meshData.imageId = -1;
	meshData.imageQualityIndicatorsId = -1;
	meshData.imageFileLocation = "";
	meshData.imageFilePresent = false;
	meshData.imagePrefix = "";
	meshData.dataCollectionNumber = 0;
	meshData.motorPosition = [];
	meshData.motorPosition.phiX = 0;
	meshData.motorPosition.phiY = 0;
	meshData.motorPosition.phiZ = 0;
	meshData.motorPosition.sampX = 0;
	meshData.motorPosition.sampY = 0;
	meshData.motorPosition.omega = 0;
	meshData.motorPosition.kappa = 0;
	meshData.motorPosition.phi = 0;
	meshData.motorPosition.gridIndexY = 0;
	meshData.motorPosition.gridIndexZ = 0;
	meshData.gridInfo = [];
	meshData.gridInfo.xOffset = 0;
	meshData.gridInfo.yOffset = 0;
	meshData.gridInfo.dx_mm = 0;
	meshData.gridInfo.dy_mm = 0;
	meshData.gridInfo.steps_x = 0;
	meshData.gridInfo.steps_y = 0;
	meshData.gridInfo.meshAngle = 0;
	meshData.spotTotal = 0;
	meshData.goodBraggCandidates = 0;
	meshData.method1Res = 0;
	meshData.method2Res = 0;
	meshData.totalIntegratedSignal = 0;
	meshData.dozor_score = 0;
	return meshData;
};

MapPanel.prototype.getCoordPos = function(x, y) {
	var pos = new Array(2);
	var nbX = this.scaleDataX.length;
	var nbY = this.scaleDataY.length;
	var i = 0;
	while (x > this.scaleDataX[i] && i < nbX) {
		i++;
	}
	var j = 0;
	while (y > this.scaleDataY[j] && j < nbY) {
		j++;
	}

	pos[0] = j < nbY ? j : (nbY - 1);
	pos[1] = i < nbX ? i : (nbX - 1);
	// console.log("x="+pos[0]+", y="+pos[1]);
	return pos;
};

MapPanel.prototype.saveSettings = function(params) {
	var _this = this;
	this.mapTitle = params.mapTitle;
	this.maxValue = params.maxValue;
	this.minValue = params.minValue;
};

MapPanel.prototype._refresh = function() {
	var dataRange = this._getDataRange();
	this.colorRangeManager.loadData(dataRange, this.reverseOrder);
	this.refresh(this.data, this.positions, this.images, this.scaleDataX,
			this.scaleDataY, this.colorRangeManager, this.mapTitle,
			this.axisXName, this.axisYName, this.cellWidthValue);
};

MapPanel.prototype.sliderChange = function() {
	this.cellWidthValue = this.slider.getSliderValue();
	this._refresh();
};

MapPanel.prototype.getPanel = function(meshData, snapshot, id) {
	var _this = this;
	_this.id = id;
	this.snapshot = snapshot;
	// create color range object
	this.colorRangeManager = new ColorRangeManager();

	// get Data
	this.buildData(meshData);
	this.maxValue = this._calculateMaxValue();
	//this.minValue = this._calculateMinValue();

	// load map
	var dataRange = _this._getDataRange();
	this.colorRangeManager.loadData(dataRange, this.reverseOrder);

	// slider
	this.slider = new SliderWidget('slider');
	// this.slider.draw();
	this.slider.sliderChange.attach(function() {
				_this.sliderChange();
			});

	var diffHandler = function(btn) {
		if (btn.id == _this.id+'_settingsBtn') {
			var mapSettingsWindow = new MapSettingsWindow();
			mapSettingsWindow.onSaved.attach(function(evt, mapTitle, maxValue, minValue) {
						_this.saveSettings(mapTitle, maxValue, minValue);
						_this._refresh();

					});
			mapSettingsWindow.draw(_this.mapTitle, _this.maxValue, _this.minValue);
			return;
		}
		if (btn.id == _this.id+'_saveAsImageBtn') {
			// var canvas = document.getElementById("canvas");
			// var img = canvas.toDataURL("image/png");
			// var w=window.open('about:blank','image from canvas');
			// w.document.write("<img src='"+img+"' alt='from canvas'/>");
			_this.saveAsImage();
			return;
		}
		if (btn.id == _this.id+'_loadImgBtn') {
			_this.loadImage();
			return;
		}
		if (btn.id == _this.id+'_unloadImgBtn') {
			_this.unloadImage();
			return;
		}
		return alert('not yet implemented');
	};

	var enabledButton = (snapshot && snapshot.filePresent);

	var saveAsImgButton = new Ext.Button({
				id : _this.id+'_saveAsImageBtn',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var loadImgButton = new Ext.Button({
				id : _this.id+'_loadImgBtn',
				text : '',
				disabled : !enabledButton,
				tooltip : 'Load the crystal image',
				icon : '../images/image_add.png',
				handler : diffHandler
			});
	var unloadImgButton = new Ext.Button({
				id : _this.id+'_unloadImgBtn',
				text : '',
				disabled : !enabledButton,
				tooltip : 'Unload the crystal image',
				icon : '../images/image_remove.png',
				handler : diffHandler
			});
	var settingsButton = new Ext.Button({
				id : _this.id+'_settingsBtn',
				text : '',
				tooltip : 'map settings...',
				icon : '../images/cog.png',
				handler : diffHandler
			});

	var criteria = [{
				"id" : "dozor_score",
				"name" : "Dozor Score"
			},
			{
				"id" : "totalIntegratedSignal",
				"name" : "Total Integrated Signal"
			}, {
				"id" : "goodBraggCandidates",
				"name" : "Good bragg candidates"
			}, {
				"id" : "totalSpots",
				"name" : "Total Spots"
			}, {
				"id" : "method1Res",
				"name" : "Method 1 resolution"
			}, {
				"id" : "method2Res",
				"name" : "Method 2 resolution"
			}];
	var criteriaPlotStore = Ext.create('Ext.data.Store', {
				autoDestroy : true,
				model : 'CriteriaPlotModel',
				data : criteria
			});
	var cbPlot = Ext.create('Ext.form.field.ComboBox', {
				fieldLabel : 'Select Criteria for plotting',
				displayField : 'name',
				width : 300,
				labelWidth : 140,
				store : criteriaPlotStore,
				queryMode : 'local',
				typeAhead : true,
				listeners : {
					// public event change - when selection1 dropdown is changed
					change : function(field, newValue, oldValue) {
						_this.updateCriteria(newValue);
					}
				}
			});
	cbPlot.setValue("dozor_score");

	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton, loadImgButton, unloadImgButton,
						settingsButton, cbPlot, this.slider.draw()]
			});

	_this.panel = Ext.create('Ext.Panel', {
		layout : 'fit',
		tbar : diffToolBar,
		width:"100%",
		listeners : {
			'afterrender' : function(thisCmp) {
				_this.postRender();

			}
		},
		html : "<div style='position:left; ' id='scaleColorMapPanel_"+id+"'></div> <div id='container_"+id+"'></div><div id='canvasarea_"+id+"' style='text-align:right'><canvas id='canvas_"+id+"' width='0' height='0' style='position:absolute; top:1000; left:1000;'>No map found</canvas></div>"
	});
	return _this.panel;
};

MapPanel.prototype.postRender = function() {
	var _this = this;
	var canvas = document.getElementById("canvas_"+_this.id+"");
	var svg = document.getElementById("svg_"+_this.id+"");
	var rowsCount = _this.data.length;
	var columnsCount = 0;
	if (_this.data[0])
		columnsCount = _this.data[0].length;

	var rowsName = _this.getRowsNamesBDA(rowsCount);
	var columnsNames = _this.getColumnsNamesBDA(columnsCount);

	_this.grid = new Grid(0, 20, (rowsName.length * _this.cellWidthValue),
			(columnsNames.length * _this.cellWidthValue), canvas, document
					.getElementById("container_"+_this.id+""), _this.id);
	_this.grid.fill(rowsName, columnsNames, _this.getDataBDA(rowsCount,
					columnsCount), _this.data, _this.positions, _this.images,
			this.mapTitle, _this.axisXName, _this.axisYName,
			_this.cellWidthValue, _this.minValue, _this.maxValue, _this.colorRangeManager);
	_this.grid.model.render();
	_this.grid.refresh();

	// _this.colorRangeManager.createColorGradient('scaleColorMapPanel',
	// _this.colorRangeManager.getMaxValue());

};

MapPanel.prototype._getDataRange = function() {
	var _this = this;
	var dataRange = {
		min : 0,
		max : 0
	};
	dataRange.min = _this.minValue;
	dataRange.max = _this.maxValue;
	return dataRange;
};


MapPanel.prototype._calculateMaxValue = function() {
	var max = 0;
	var nbRow = this.data.length;
	for (var i = 0; i < nbRow; i++) {
		var nbCol = this.data[i].length;
		for (var j = 0; j < nbCol; j++) {
			max = Math.max(max, this.data[i][j]);
		}
	}
	return max;
};


MapPanel.prototype._calculateMinValue = function() {
	var min = 0;
	var nbRow = this.data.length;
	if (nbRow > 0 && this.data[0].length > 0){
		min = this.data[0][0];
	}
	for (var i = 0; i < nbRow; i++) {
		var nbCol = this.data[i].length;
		for (var j = 0; j < nbCol; j++) {
			min = Math.min(min, this.data[i][j]);
		}
	}
	return min;
};


MapPanel.prototype.updateCriteria = function(newCriteria) {
	var criteria = "";
	if (newCriteria == "Total Integrated Signal") {
		criteria = "totalIntegratedSignal";
	} else if (newCriteria == "Good bragg candidates") {
		criteria = "goodBraggCandidates";
	} else if (newCriteria == "Total Spots") {
		criteria = "totalSpots";
	} else if (newCriteria == "Method 1 resolution") {
		criteria = "method1Res";
	} else if (newCriteria == "Method 2 resolution") {
		criteria = "method2Res";
	}else if (newCriteria == "Dozor Score") {
		criteria = "dozor_score";
	}
	if (criteria != "") {
		this.buildDataCriteria(criteria);
		this.maxValue = this._calculateMaxValue();
		//this.minValue = this._calculateMinValue();
		this.reverseOrder = false;
		if (criteria == "method1Res" || criteria == "method2Res"){
			this.reverseOrder = true;
		}
		this._refresh();
	}
};

MapPanel.prototype.saveAsImage = function() {
	// var svg = document.getElementById('container');
	// var svg_xml = (new XMLSerializer).serializeToString(svg);
	// open("data:image/svg+xml," + encodeURIComponent(svg_xml));
	svgenie.save("meshSvgId_"+this.id, {
				name : "mesh.png"
			});
};

MapPanel.prototype.refresh = function(data, positions, images, dataScaleX,
		dataScaleY, colorRangeManager, title, axisXName, axisYName,
		cellWidthValue) {
	var _this = this;

	_this.data = data;
	_this.positions = positions;
	_this.images = images;
	_this.colorRangeManager = colorRangeManager;

	_this.scaleDataX = dataScaleX;
	_this.scaleDataY = dataScaleY;

	_this.mapTitle = title;
	_this.axisXName = axisXName;
	_this.axisYName = axisYName;

	var canvas = document.getElementById("canvas_"+_this.id+"");
	var svg = document.getElementById("svg_"+_this.id+"");
	var rowsCount = _this.data.length;
	var columnsCount = _this.data[0].length;
	var squareSize = cellWidthValue;

	var rowsName = _this.getRowsNamesBDA(rowsCount);
	var columnsNames = _this.getColumnsNamesBDA(columnsCount);
	_this.grid.fill(rowsName, columnsNames, _this.getDataBDA(rowsCount,
					columnsCount), _this.data, _this.positions, _this.images,
			_this.mapTitle, _this.axisXName, _this.axisYName, squareSize,
			_this.colorRangeManager.getMinValue(),
			_this.colorRangeManager.getMaxValue(), _this.colorRangeManager);
	_this.grid.refresh();

	// document.getElementById('scaleColorMapPanel').innerHTML = "";
	// _this.colorRangeManager.createColorGradient('scaleColorMapPanel',
	// _this.colorRangeManager.getMaxValue());
};

MapPanel.prototype.getRowsNamesBDA = function(value) {
	var _this = this;
	var array = [];
	for (var i = 0; i < value; i++) {
		array.push(_this.scaleDataY[i]);
	}
	return array;
};

MapPanel.prototype.getColumnsNamesBDA = function(value) {
	var _this = this;
	var array = [];
	for (var i = 0; i < value; i++) {
		array.push(_this.scaleDataX[i]);
	}
	return array;
};

MapPanel.prototype.getDataBDA = function(rowsCount, columnsCount) {
	var _this = this;
	var data = [];
	for (var i = 0; i < rowsCount; i++) {
		data[i] = [];
	}

	var rowNames = [];
	var columnNames = [];

	for (var j = 0; j < columnsCount; j++) {
		columnNames[j] = "COLUMN_#" + j;
		for (var i = 0; i < rowsCount; i++) {
			data[i][j] = _this.getColorCell(i, j);
			rowNames[i] = "ROW_NAMES_#" + i;
		}
	}

	return data;
};

MapPanel.prototype.getColorCell = function(row, column) {
	var _this = this;
	return this.colorRangeManager.getCellColorString(_this.data[row][column]);
};

MapPanel.prototype.loadImage = function() {
	if (this.snapshot && this.snapshot.filePresent) {
		var url = "imageDownload.do?reqCode=getImageJpgFromFile&file=" + this.snapshot.fileLocation;
		this.grid.loadImage(url);
	}
};

MapPanel.prototype.unloadImage = function() {
	this.grid.unloadImage();
};
