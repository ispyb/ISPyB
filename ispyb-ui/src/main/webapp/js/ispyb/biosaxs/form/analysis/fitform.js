/**
 * Fit form
 * 
 * @witdh
 * @height
 */
function FitForm(args) {
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
	this.fitGrid = new FitStructureToExperimentDataGrid({
		width : null,
		height : 200
	});

	this.fitGrid.onSelected.attach(function(sender, fits) {
		var modelsIdList = [];
		for ( var i in fits) {
			modelsIdList.push(fits[i].fitStructureToExperimentalDataId);
		}
		_this._renderPlot(_this.subtractions[0].subtractionId, modelsIdList);
//		var adapter = new BiosaxsDataAdapter();
//		adapter.onSuccess.attach(function(sender, data) {
//
//		});
//		adapter.getScatteringCurveByFrameIdsList([], [], [], ids);
	});
	
	
	/** Dygraph Widget that plots fir files**/
	this.plotWidget = new PlotWidget({
		width : 745,
		height : 300,
		margin : "10 0 10 10"
	});
}

FitForm.prototype._renderPlot = function(subtractionId, modelsIdList) {
	/** Trying to plot tje subtraction and the models **/
	try {
		var colors = [ "#669900", "#0000FF" ];
		
		this.plotWidget.refresh([], [ ], [], modelsIdList, [], colors);
	} catch (e) {
		console.log(e);
	}
};

FitForm.prototype._getItems = function() {
	var _this = this;
	return [ {
		xtype : 'label',
		forId : 'myFieldId',
		text : 'INLINE HELP: To be updated',
		margin : '15 0 20 10',
		cls : "inline-help"
	}, this.fitGrid.getPanel(), this.plotWidget.getPanel() ]

};

FitForm.prototype._getButtons = function() {
	return [];
};

FitForm.prototype.getPanel = function() {
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
FitForm.prototype._populate = function() {
};

/** It populates the form * */
FitForm.prototype.refresh = function(subtractions) {
	this.subtractions = subtractions;
	this.fitGrid.refresh(subtractions);
};

FitForm.prototype.input = function() {
	return {};
};

/** It populates the form **/
FitForm.prototype.test = function(targetId) {
	var macromoleculeForm = new FitForm();
	var panel = macromoleculeForm.getPanel();
	panel.render(targetId);
};
