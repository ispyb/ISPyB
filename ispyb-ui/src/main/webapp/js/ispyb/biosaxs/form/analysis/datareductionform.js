/**
 * Example form
 * 
 * @witdh
 * @height
 */
function DataReductionForm(args) {
	this.id = BUI.id();
	this.width = null;
	this.height = null;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	var _this = this;

	/** Widgets **/
	this.plotWidget = new PlotWidget({
		width : 650,
		height : 490
	});

	/** Selected frames to be displayed **/
	this.selectedItems = {
		frames : [],
		averages : [],
		subtractions : []
	};

}

DataReductionForm.prototype._parseSelectedItemsToIds = function(selectedArray) {
	var ids = [];
	if (selectedArray != null) {
		for (var i = 0; i < selectedArray.length; i++) {
			ids.push(selectedArray[i].id);
		}
	}
	return ids;
};

DataReductionForm.prototype.onSelectionChanged = function(columnName, selected) {
	if (selected != null) {
		if (columnName == "Frames") {
			this.selectedItems.frames = selected;
			this.selectedItems.subtractions = [];
		}
		if (columnName == "Averages") {
			this.selectedItems.averages = selected;
		}
		if (columnName == "Subtractions") {
			this.selectedItems.frames = [];
			this.selectedItems.subtractions = selected;
		}
	}
	this.plotWidget.refresh(this._parseSelectedItemsToIds(this.selectedItems.frames), this._parseSelectedItemsToIds(this.selectedItems.subtractions));
};

DataReductionForm.prototype._getFramesExtPanel = function(store, columnName, height) {
	var _this = this;

	var selModel = Ext.create('Ext.selection.RowModel', {
		allowDeselect : true,
		mode : 'MULTI',
		listeners : {
			selectionchange : function(sm, selections) {
				var selected = [];
				for (var i = 0; i < selections.length; i++) {
					selected.push(selections[i].raw);
				}
				_this.onSelectionChanged(columnName, selected);
			}
		}
	});

	return Ext.create('Ext.grid.Panel', {
		store : store,
		margin : 10,
		height : height,
		width : 200,
		selModel : selModel,
		columns : [ {
			text : columnName,
			dataIndex : 'fileName',
			flex : 1
		} ],
		viewConfig : {
		}
	});
};

DataReductionForm.prototype._getFramesPanel = function() {
	var fields = [ 'fileName', 'type', 'id' ];

	this.framesStore = Ext.create('Ext.data.Store', {
		fields : fields,
		sorters : 'fileName'
	});

	this.averagesStore = Ext.create('Ext.data.Store', {
		fields : fields
	});

	this.subtractionStore = Ext.create('Ext.data.Store', {
		fields : fields
	});

	var gridFrames = this._getFramesExtPanel(this.framesStore, "Frames", 375);
	var gridAvgs = this._getFramesExtPanel(this.averagesStore, "Averages", 125);
	var subtractionAvgs = this._getFramesExtPanel(this.subtractionStore, "Subtractions", 75);

	return {
		xtype : 'container',
		layout : 'vbox',
		items : [ gridFrames,  subtractionAvgs ]
	};
};

DataReductionForm.prototype._getImageContainer = function(name, help) {
	var html = "<div id='" + this.id + "_" + name + "'>" + name + "</div>"
	return {
		xtype : 'container',
		layout : 'vbox',
		items : [ {
			html : html,
			margin : "5 0 0 0",
			height : 95,
			width : 100
		}, {
			xtype : 'label',
			forId : 'myFieldId',
			text : help,
			margin : '5 0 0 0',
			cls : "inline-help"
		} ]
	}
};

DataReductionForm.prototype._getItems = function() {
	var _this = this;
	return [ {
		xtype : 'container',
		layout : 'hbox',
		items : [
				this._getFramesPanel(),
				this.plotWidget.getPanel(),
				{
					xtype : 'panel',
					width : 110,
					frame : true,
					margin : "10 5 5 5",
					border : 0,
					layout : 'vbox',
					items : [ this._getImageContainer("scattering", "Scattering"), this._getImageContainer("guinier", "Guinier Region"),
							this._getImageContainer("kratky", "Kratky Plot"), this._getImageContainer("gnom", "P(r) distribution") ]
				} ]
	} ]
};

DataReductionForm.prototype._getButtons = function() {
	return [];
};

DataReductionForm.prototype.getPanel = function() {
	var _this = this;
	this.panel = Ext.create('Ext.form.Panel', {
		width : this.width,
		height : this.height,
		border : 0,
		items : this._getItems(),
		buttons : this._getButtons(),
		listeners : {
			afterrender : function() {
				_this._populate();
			}
		}
	});
	return this.panel;
};

/** Populates could be call when the DOM is not filled yet **/
DataReductionForm.prototype._populate = function() {
};

/** It populates the form * */
DataReductionForm.prototype.refresh = function(subtractions) {
	if (subtractions != null) {
		for (var i = 0; i < subtractions.length; i++) {
			/** Loading frame grids **/
			var subtraction = subtractions[i];
			var averages = [ {
				fileName : BUI.getFileName(subtraction.bufferAverageFilePath),
				type : 'bufferAvg',
				id : subtraction.subtractionId
			}, {
				fileName : BUI.getFileName(subtraction.sampleAverageFilePath),
				type : 'sampleAvg',
				id : subtraction.subtractionId
			}

			];
			this.averagesStore.loadData(averages, true);
			this.subtractionStore.loadData([ {
				fileName : BUI.getFileName(subtraction.substractedFilePath),
				type : 'SUBTRACTION',
				id : subtraction.subtractionId
			} ], true);

			var frames = [];
			/** Buffers **/
			if (subtraction.bufferOneDimensionalFiles != null) {
				if (subtraction.bufferOneDimensionalFiles.frametolist3VOs != null) {
					for (var j = 0; j < subtraction.bufferOneDimensionalFiles.frametolist3VOs.length; j++) {
						var frametolist3VO = subtraction.bufferOneDimensionalFiles.frametolist3VOs[j];
						if (frametolist3VO.frame3VO != null) {
							frames.push({
								fileName : BUI.getFileName(frametolist3VO.frame3VO.filePath),
								type : 'BUFFER',
								id : frametolist3VO.frame3VO.frameId
							});
						}
					}
				}
			}
			/** Samples **/
			if (subtraction.sampleOneDimensionalFiles != null) {
				if (subtraction.sampleOneDimensionalFiles.frametolist3VOs != null) {
					for (var j = 0; j < subtraction.sampleOneDimensionalFiles.frametolist3VOs.length; j++) {
						var frametolist3VO = subtraction.sampleOneDimensionalFiles.frametolist3VOs[j];
						if (frametolist3VO.frame3VO != null) {
							frames.push({
								fileName : BUI.getFileName(frametolist3VO.frame3VO.filePath),
								type : 'SAMPLE',
								id : frametolist3VO.frame3VO.frameId
							});
						}
					}
				}
			}

			this.framesStore.loadData(frames, true);

			/** Loading images **/
			this._displayImage("scattering", subtraction.subtractionId);
			this._displayImage("kratky", subtraction.subtractionId);
			this._displayImage("guinier", subtraction.subtractionId);
			this._displayImage("gnom", subtraction.subtractionId);
		}
	}
};

DataReductionForm.prototype._displayImage = function(name, subtractionId) {
	var url = BUI.getURL() + '&type=' + name + '&subtractionId=' + subtractionId;
	var event = "OnClick= window.open('" + url + "')";
	document.getElementById(this.id + "_" + name).innerHTML = '<img src=' + url + '   height="90" width="90" ' + event + '>';
};

DataReductionForm.prototype.input = function() {
	return {};
};

/** It populates the form **/
DataReductionForm.prototype.test = function(targetId) {
	var macromoleculeForm = new DataReductionForm();
	var panel = macromoleculeForm.getPanel();
	panel.render(targetId);
};

function PlotWidget(args) {
	this.width = 600;
	this.height = 600;
	this.id = BUI.id();

	this.linear = false;

	this.margin =  "10 0 0 0";
	/** for each stat the max and minimum value when it is scaled in order to show correctly in the legend **/
	this.ranges = {};
	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.margin != null) {
			this.margin = args.margin;
		}
	}

}

PlotWidget.prototype.getMenu = function() {
	var _this = this;
	/** Actions buttons **/
	var actions = [];
	actions.push("->");
	actions.push({
		text : "Export as Image",
		scope : this,
		icon : '../images/save.gif',
		handler : function(item, pressed) {
			var largeImage = document.createElement("img");
			largeImage.style.display = 'block';
			largeImage.style.width = 200 + "px";
			largeImage.style.height = 200 + "px";
			largeImage.setAttribute('src', Dygraph.Export.asCanvas(this.dygraphObject.dygraph).toDataURL());
			window.open(Dygraph.Export.asCanvas(this.dygraphObject.dygraph).toDataURL(), 'Image', '');
		}
	});

	return actions;
};

/** Looks for the maximum value and then divide everything but that value **/
PlotWidget.prototype.scaledData = function(data) {
	for (var i = 0; i < data.length; i++) {
		var values = this.getMaxAndMinValue(data[i]);
		data[i] = this.divideValuesByMax(data[i], values.max);
		this.ranges[data[i].label] = values;
	}
	return data;
};

/** Given a stat float[] and a max number it will divide each value by max **/
PlotWidget.prototype.divideValuesByMax = function(stat, max) {
	for (var j = 0; j < stat.data.length; j++) {
		if (max != 0) {
			stat.data[j] = Number(stat.data[j]) / max;
			stat.std[j] = Number(stat.std[j]) / max;
		}
	}
	return stat;
};

/** returns max value of a stat **/
PlotWidget.prototype.getMaxAndMinValue = function(stat) {
	var max = 0;
	var min = stat.data[0];
	for (var j = 0; j < stat.data.length; j++) {
		if (Number(stat.data[j]) > max) {
			max = Number(stat.data[j]);
		}
		if (Number(stat.std[j]) > max) {
			max = Number(stat.std[j]);
		}
		if (Number(stat.data[j]) < min) {
			min = Number(stat.data[j]);
		}
	}
	return {
		max : Number(max),
		min : Number(min)
	};
};

PlotWidget.prototype._renderDygraph = function(parsed, colors, labels) {
	this.dygraphObject = new StdDevDyGraph(this.id, {
		width : this.width - 20,
		height : this.height - 40,
		xlabel : "",
	});

	this.dygraphObject.draw(parsed, colors, labels);

};

PlotWidget.prototype.getPanel = function() {
	this.panel = Ext.create('Ext.panel.Panel', {
		width : this.width,
		height : this.height,
		margin : this.margin,
		tbar : this.getMenu(),
		items : [ {
			html : "",
			id : this.id,
			width : this.width,
			height : this.height,
			padding : 10,
			margin : "0 0 0 -30",
			border : 0
		} ]
	});

	return this.panel;
};

PlotWidget.prototype.getPoint = function(y, error) {
	var minus = y - error;
	var max = y + error;

	if (this.linear) {
		return [ Math.abs(minus), y, Math.abs(max) ];
	}
	if ((minus != 0) && (max != 0)) {
		return [ Math.log(Math.abs(minus)), Math.log(y), Math.log(Math.abs(max)) ];
	} else {
		return [ Math.log(y), Math.log(y), Math.log(y) ];
	}

};

PlotWidget.prototype.getDataPlot = function(frames, subtractions, models, fits, rbms) {
	var files = [];
	var labels = [ "Intensity" ];
	if (frames != null) {
		for (var i = 0; i < frames.length; i++) {
			files.push(frames[i].data);
			labels.push(frames[i].fileName);
		}
	}
	 function splitData(data, column, errorColumn, name){
		 var result = []
		 for (var j = 0; j < data.length; j++) {
			 console.log(data[j][column]);
			result.push([j,data[j][column],data[j][errorColumn]]);//[0, data[i][column],0]]);
		}
		 files.push(result);
		 labels.push(name);
	 }
	 
	if (subtractions != null) {
		for (var i = 0; i < subtractions.length; i++) {
			/** For subtraction **/
			files.push(subtractions[i].subtraction.data);
			labels.push(subtractions[i].subtraction.fileName);
			/** For sample average **/
//			files.push(subtractions[i].sampleAvg.data);
//			labels.push(subtractions[i].sampleAvg.fileName);
			/** For buffer average **/
//			files.push(subtractions[i].bufferAvg.data);
//			labels.push(subtractions[i].bufferAvg.fileName);
		}
	}
	
	if (models != null) {
		for (var i = 0; i < models.length; i++) {
			for ( var key in models[i]) {
				 splitData(models[i][key].fir.data, 1, 2, "Intensity");
				 splitData(models[i][key].fir.data, 3, 3, "Fit");
			}
		}
	}
	
	if (fits != null) {
		for (var i = 0; i < fits.length; i++) {
			for ( var key in fits[i]) {
				
				/** adding fit file to be plotted **/
						 if (fits[i][key].fit.data[0].length == 3){
							 splitData(fits[i][key].fit.data, 1, 1, "Intensity");
							 splitData(fits[i][key].fit.data, 2, 2, "Fit");
						 }
						 
						 /** s, Iexp(s), err, Ifit(s). **/
						 if (fits[i][key].fit.data[0].length == 4){
							 splitData(fits[i][key].fit.data, 1, 2, "Intensity");
							 splitData(fits[i][key].fit.data, 3, 3, "Fit");
						 }
						 
						 if (fits[i][key].fit.data[0].length == 5){
							 /** X Intensity Fit Error Residues **/
							
							 splitData(fits[i][key].fit.data, 1, 3, "Intensity");
							 splitData(fits[i][key].fit.data, 2, 2, "Fit");
						 }
			}
		}
	}
	
	var dataPoints = [];
	if (files.length > 0) {
		for (var i = 0; i < files[0].length; i++) {
			dataPoints.push([ files[0][i][0], this.getPoint(files[0][i][1], files[0][i][2]) ]);
		}
		if (files.length > 1) {
			for (var i = 1; i < files.length; i++) {
				for (var j = 0; j < dataPoints.length; j++) {
					if (files[i][j] != null){
						dataPoints[j].push(this.getPoint(files[i][j][1], files[i][j][2]));
					}
					else{
						dataPoints[j].push([0,0,0]);
					}
				}
			}
		}
	}

	return {
		dataPoints : dataPoints,
		labels : labels
	}
};

PlotWidget.prototype.refresh = function(frames, subtractions, models, fits, rbms, colors) {
	
	var _this = this;
	this.panel.setLoading("Reading Files");
	
	var dataAdapter = new BiosaxsDataAdapter();
	dataAdapter.onSuccess.attach(function(sender, data) {
			_this.panel.setLoading("Rendering");
			var parsed = _this.getDataPlot(data.frames, data.subtractions, data.models, data.fits, rbms);
			_this._renderDygraph(parsed.dataPoints, colors, parsed.labels);
		_this.panel.setLoading(false);
	});
	dataAdapter.onError.attach(function(sender, data) {
		_this.panel.setLoading(false);
	});
	dataAdapter.getDataPlot(frames, subtractions, models, fits, rbms);
};

PlotWidget.prototype.input = function() {
	return DATADOC.getHPLCData();
};

