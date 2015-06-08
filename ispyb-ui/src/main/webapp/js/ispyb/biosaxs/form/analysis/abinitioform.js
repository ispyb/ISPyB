/**
 * Example form
 * 
 * @witdh
 * @height
 */
function AbinitioForm(args) {
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
	this.abinitioGrid = new AbinitioGrid({
		width : null,
		height : 200
	});

	this.abinitioGrid.onSelected.attach(function(sender, models) {
		var modelsIdList = [];
		for ( var i in models) {
			modelsIdList.push(models[i].modelId);
		}
		_this._renderPlot(_this.subtractions[0].subtractionId, modelsIdList);
		_this._renderPDB(modelsIdList);
	});

	/** Dygraph Widget that plots fir files**/
	this.plotWidget = new PlotWidget({
		width : 745 / 2,
		height : 300,
		margin : "10 0 5 10"
	});

	/** PDB viewer **/
	this.viewer = new PDBViewer({
		width : 745 / 2,
		height : 300
	});

}

AbinitioForm.prototype._renderPlot = function(subtractionId, modelsIdList) {
	/** Trying to plot tje subtraction and the models **/
	try {
		var colors = [ "#669900", "#0000FF" ];
		this.plotWidget.refresh([], [  ], modelsIdList, [], [], colors);
	} catch (e) {
		console.log(e);
	}
};

AbinitioForm.prototype._renderPDB = function(modelsIdList) {
	/** Trying to plot the PDB file **/
	try {
		var viz = [];
		for (var i = 0; i < modelsIdList.length; i++) {
			viz.push({
				modelId : modelsIdList[i],
				color : "0xFF6600",
				opacity : 0.8
			});
		}
		this.viewer.refresh(viz);
	} catch (e) {
		console.log(e);
	}
};

AbinitioForm.prototype._getItems = function() {
	var _this = this;
	return [ {
		xtype : 'label',
		forId : 'myFieldId',
		text : 'INLINE HELP: To be updated',
		margin : '15 0 20 10',
		cls : "inline-help"
	}, this.abinitioGrid.getPanel(), {
		xtype : 'container',
		layout : 'hbox',
		items : [ this.plotWidget.getPanel(), this.viewer.getPanel() ]
	} ]
};

AbinitioForm.prototype._getButtons = function() {
	return [];
};

AbinitioForm.prototype.getPanel = function() {
	var _this = this;
	this.panel = Ext.create('Ext.form.Panel', {
		width : this.width,
		height : this.height,
		margin : 10,
		border : 1,
		defaultType : 'textfield',
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
AbinitioForm.prototype._populate = function() {
};

/** It populates the form * */
AbinitioForm.prototype.refresh = function(subtractions) {
	this.subtractions = subtractions;
	this.abinitioGrid.refresh(subtractions);
};

AbinitioForm.prototype.input = function() {
	return {};
};

/** It populates the form **/
AbinitioForm.prototype.test = function(targetId) {
	var macromoleculeForm = new AbinitioForm();
	var panel = macromoleculeForm.getPanel();
	panel.render(targetId);
};
