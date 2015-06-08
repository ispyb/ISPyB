/**
 * 
 * @width
 * @height
 * 
 */
function DataCollectionPDBWidget(args) {
	this.id = BUI.id();

	this.width = 900;
	this.height = 600;
	this.title = "";

	this.mergedEnabled = true;
	this.damminEnabled = false;
	this.damaverEnabled = false;
	this.damfiltEnabled = false;

	this.radius = 3;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height - 100;
		}
	}
}

DataCollectionPDBWidget.prototype.getPDBViewerPanel = function(model, width, height) {
	return new PDBViewer({
		width : width,
		height : height,
		title : model.title

	}).draw(model);
};

DataCollectionPDBWidget.prototype.get2x2 = function(models, colors) {
	
	return [ {
		xtype : 'container',
		type : 'vbox',
		items : [ this.getPDBViewerPanel([ models[0] ], this.width / 2, this.height / 2),

		this.getPDBViewerPanel([ models[1] ], this.width / 2, this.height / 2) ]
	}, {
		xtype : 'container',
		type : 'vbox',
		padding : '0 0 0 5',
		items : [ this.getPDBViewerPanel([ models[2] ], this.width / 2, this.height / 2), this.getPDBViewerPanel(models[3], this.width / 2, this.height / 2) ]
	} ];
};

DataCollectionPDBWidget.prototype.getContainers = function(models) {
	
	var items = [];
	if (models.length < 4) {
		for (key in models) {
			var model = models[key];
			if (model.length == null) {
				model = [ model ];
			}

			items.push({
				xtype : 'container',
				type : 'vbox',
				margin : '2',
				items : [ this.getPDBViewerPanel(model, this.width / models.length, this.height) ]

			});
		}
	}

	if (models.length == 4) {
		/** First row **/
		return this.get2x2(models);
	}

	return items;

};

DataCollectionPDBWidget.prototype.getColors = function() {
	var colors = [];
	colors.push("0xFF6600");
	colors.push("0x00CC00");
	colors.push("0x0099FF");
	return colors;
};

DataCollectionPDBWidget.prototype.getPDBPanel = function(items) {
	var panel = Ext.create('Ext.panel.Panel', {
		layout : {
			type : 'hbox'
		},
		margin : '5 0 0 0',
		width : this.width,
		items : this.getContainers(items)
	});

	panel.addDocked({
		xtype : 'toolbar',
		items : this.getMenu()
	});
	return panel;
};

/** type is fir or fit **/
DataCollectionPDBWidget.prototype._getFirHTML = function(modelId, width, height, type, desc) {
	var html = "<table>";
	html = html + '<tr style="text-align: center;"><td><a style="color:blue;font-weight:bold;" target="_blank" href="' + BUI.getModelFile("FIT", modelId, "txt") + '" >dammin.' + type + '</a></td></tr>';
	html = html + '<tr style="text-align: center;font-style:italic;color:gray;"><td>' + desc + '</td></tr>';
	html = html + '<tr><td><div background-color="red" id="' + (this.id + type + modelId) + '" width="' + width + '" height="' + height + '"></div></td></tr>';
	html = html + '</table>';
	return html;
};

DataCollectionPDBWidget.prototype.getNSDPlot = function(width, height) {
	var html = "<table>";
	html = html + '<tr><td><div background-color="red"  width="' + width + '" height="' + height + '"></div></td></tr>';
	var url = BUI.getNSDImageURL(this.data.modelListId);
	var action = 'window.open("' + url + '")';
	var img = '<img style="width:' + width + 'px;height:' + height + 'px;"   src="' + url + '">';
	html = html + '<tr style="text-align: center;font-style:italic;color:gray;"><td><a href=' + url + ' style="color:blue;font-weight:bold;" target="_blank">NSD file</a></td></tr>';
	html = html + '<tr style="text-align: center;font-style:italic;color:gray;"><td>Normalized squared displacement comparison</td></tr>';
	html = html + '<tr style="text-align: center;font-style:italic;color:gray;"><td> ' + img + '</td></tr>';
	html = html + '</table>';
	return html;
};

DataCollectionPDBWidget.prototype.getCHI2Plot = function(width, height) {
	var html = "<table>";
	html = html + '<tr><td><div background-color="red"  width="' + width + '" height="' + height + '"></div></td></tr>';
	var url = BUI.getCHI2ImageURL(this.data.modelListId);
	var img = '<img style="width:' + width + 'px;height:' + height + 'px;"   src="' + url + '">';
	html = html + '<tr style="text-align: center;font-style:italic;color:gray;"><td><a href=' + url + ' style="color:blue;font-weight:bold;" target="_blank">CHI2 file</a></td></tr>';
	html = html + '<tr style="text-align: center;font-style:italic;color:gray;"><td>CHI  comparison</td></tr>';
	html = html + '<tr style="text-align: center;font-style:italic;color:gray;"><td> ' + img + '</td></tr>';
	html = html + '</table>';
	return html;
};

DataCollectionPDBWidget.prototype.getImagesPanel = function() {
	var _this = this;
	return {
		xtype : 'container',
		layout : 'hbox',
		width : this.width,
		height : 300,
		border : 1,
		margin : '5 0 0 0',
		items : [ {
			html : _this._getFirHTML(_this.data.shapeDeterminationModelId, (this.width / 4) - 5, 250, "fir", "Fit of the simulated scattering curve versus a smoothed experimental data (spline interpolation)"),
			width : (this.width / 4) - 5,
			height : 300,
			border : 1,
			margin : '0 5 0 0'
		}, {
			html : _this._getFirHTML(_this.data.shapeDeterminationModelId, (this.width / 4) - 5, 250, "fit", "Fit of the simulated scattering curve versus the experimental data."),
			width : (this.width / 4) - 5,
			height : 300,
			border : 1,
			margin : '0 5 0 0'
		}, {
			html : _this.getCHI2Plot(this.width / 4, 250),
			width : (this.width / 4) - 5,
			height : 300,
			border : 1,
			margin : '0 5 0 0'
		}, {
			html : _this.getNSDPlot(this.width / 4, 250),
			width : (this.width / 4) - 5,
			height : 300,
			border : 1
		}

		]
	};
};
DataCollectionPDBWidget.prototype.draw = function(data, targetId) {

	this.data = data;
	var _this = this;
	this.analysisGrid = new AnalysisGrid({
		height : 150,
		grouped : false,
		maxHeight : 200,
		positionColumnsHidden : true,
		sorters : [ {
			property : 'priorityLevelId',
			direction : 'ASC'
		} ]
	});

	this.panelPDB = this.getPDBPanel(this.getModels());
	this.panel = Ext.create('Ext.container.Container', {
		layout : {
			type : 'vbox'
		},
		width : this.width,
		renderTo : targetId,
		listeners : {
			afterrender : function() {
				/** For fir files **/
				var adapter = new BiosaxsDataAdapter();
				adapter.onSuccess.attach(function(sender, data) {
					var splitted = data.toString().split("\n");
					var array = [];
					for ( var i = 0; i < splitted.length; i++) {
						var line = splitted[i].trim();
						var line_splited = line.split(/\s*[\s,]\s*/);
						if (line_splited.length == 3) {
							array.push([ Number(line_splited[0]), BUI.getStvArray(line_splited[1], 0), BUI.getStvArray(line_splited[2], 0) ]);
						}
					}
					var dygraphWidget = new StdDevDyGraph(_this.id + "fit" + _this.data.shapeDeterminationModelId, {
						width : 400,
						height : 250,
						xlabel : 'q(nm<sup>-1</sup>)'
					});
					dygraphWidget.draw(array, [ "#FF0000", "#0000FF" ], [ "s", "I(exp)", "I(sim)" ]);
				});
				adapter.getModelFile("FIT", _this.data.shapeDeterminationModelId);

				/** For fit files **/
				adapter = new BiosaxsDataAdapter();
				adapter.onSuccess.attach(function(sender, data) {
				
					var splitted = data.toString().split("\n");
					var array = [];
					for ( var i = 0; i < splitted.length; i++) {
						var line = splitted[i].trim();
						var line_splited = line.split(/\s*[\s,]\s*/);
						if (line_splited.length == 4) {
							array.push([ Number(line_splited[0]), BUI.getStvArray(line_splited[1], line_splited[2]), BUI.getStvArray(line_splited[3], 0) ]);
						}
					}

					var dygraphWidget = new StdDevDyGraph(_this.id + "fir" + _this.data.shapeDeterminationModelId, {
						width : 400,
						height : 250,
						xlabel : 'q(nm<sup>-1</sup>)'
					});
					dygraphWidget.draw(array, [ "#FF0000", "#0000FF", "#FF00FF" ], [ 's', 'I(exp)', 'I(sim)' ]);
				});
				adapter.getModelFile("FIR", _this.data.shapeDeterminationModelId);
			}
		},
		items : [ this.analysisGrid.getPanel([ data ]), this.panelPDB, this.getImagesPanel() ]
	});

};

DataCollectionPDBWidget.prototype.getDamaverModel = function() {
	if (this.data.averagedModel != null){
		return {
			type : "AVERAGED",
			modelId : this.data.averagedModelId,
			abInitioModelId : this.data.abInitioModelId,
			color : this.getColors()[0],
			title : this.data.averagedModel.split("/")[this.data.averagedModel.split("/").length - 1],
			opacity : 0.5,
			radius : this.radius
		};
	}
};

DataCollectionPDBWidget.prototype.getDamfiltModel = function() {
	if (this.data.rapidShapeDeterminationModel != null){
		return {
			type : "RAPIDSHAPEDETERMINATIONMODEL",
			modelId : this.data.rapidShapeDeterminationModelId,
			abInitioModelId : this.data.abInitioModelId,
			color : this.getColors()[1],
			title : this.data.rapidShapeDeterminationModel.split("/")[this.data.rapidShapeDeterminationModel.split("/").length - 1],
			opacity : 0.5,
			radius : this.radius
		};
	}
};

DataCollectionPDBWidget.prototype.getDammin = function() {
	if (this.data.shapeDeterminationModel != null){
		return {
			type : "SHAPEDETERMINATIONMODEL",
			modelId : this.data.shapeDeterminationModelId,
			abInitioModelId : this.data.abInitioModelId,
			color : this.getColors()[2],
			title : this.data.shapeDeterminationModel.split("/")[this.data.shapeDeterminationModel.split("/").length - 1],
			opacity : 0.5,
			radius : this.radius
		};
	}
};

DataCollectionPDBWidget.prototype.refresh = function(models) {
	this.panel.remove(this.panelPDB);
	this.panelPDB = this.getPDBPanel(models);
	this.panel.insert(this.panelPDB, 0);
};

DataCollectionPDBWidget.prototype.getModels = function() {
	var models = [];
	var data = this.data;
	if (this.damaverEnabled) {
		models.push(this.getDamaverModel());
	}

	if (this.damfiltEnabled) {
		models.push(this.getDamfiltModel());
	}

	if (this.damminEnabled) {
		models.push(this.getDammin());
	}

	if (this.mergedEnabled) {
		/** If no models we merge all **/
		var colorsMerged = [];
		var modelsId = [];
		var title = "Merged [";
		var modelsMerged = [];
		if (models.length == 0) {
			var damaverModel = this.getDamaverModel();
			if (damaverModel != null){
				damaverModel.opacity = 0.2;
				modelsMerged.push(damaverModel);
			}

			var damfiltModel = this.getDamfiltModel();
			if (damfiltModel != null){
				damfiltModel.opacity = 0.6;
				modelsMerged.push(damfiltModel);
			}

			var damminModel = this.getDammin();
			if (damminModel != null){
				damminModel.opacity = 0.8;
				modelsMerged.push(damminModel);
			}
			modelsMerged = [ modelsMerged ];
		} else {
			modelsMerged = models;
			modelsMerged.push(JSON.parse(JSON.stringify(modelsMerged)));
		}

		return modelsMerged;
	}
	return models;
};

DataCollectionPDBWidget.prototype.getMenu = function() {
	var _this = this;
	/** Actions buttons **/
	var actions = [];

	actions.push({
		text : 'Dammaver',
		id : _this.id + 'damaverBtn',
		enableToggle : true,
		scope : this,
		toggleHandler : function(item, pressed) {
			_this.damaverEnabled = pressed;
			_this.refresh(_this.getModels());
		},
		pressed : this.damaverEnabled
	});

	actions.push({
		text : 'Damfilt',
		id : _this.id + 'damfiltBtn',
		enableToggle : true,
		scope : this,
		toggleHandler : function(item, pressed) {
			_this.damfiltEnabled = pressed;
			_this.refresh(_this.getModels());
		},
		pressed : this.damfiltEnabled
	});

	actions.push({
		text : 'Dammin',
		id : _this.id + 'damminBtn',
		enableToggle : true,
		scope : this,
		toggleHandler : function(item, pressed) {
			_this.damminEnabled = pressed;
			_this.refresh(_this.getModels());
		},
		pressed : this.damminEnabled
	});

	actions.push({
		text : 'Merged',
		id : _this.id + 'mergedBtn',
		enableToggle : true,
		scope : this,
		toggleHandler : function(item, pressed) {
			_this.mergedEnabled = pressed;
			_this.refresh(_this.getModels());
		},
		pressed : this.mergedEnabled
	});

	return actions;
};

DataCollectionPDBWidget.prototype.input = function() {
	return {
		data : DATADOC.getPDBData()
	};
};

DataCollectionPDBWidget.prototype.test = function(targetId) {
	var dataCollectionPDBWidget = new DataCollectionPDBWidget({
		width : 1000,
		height : 300,
		title : "Damaver"
	});
	dataCollectionPDBWidget.draw(dataCollectionPDBWidget.input().data, targetId);

};
