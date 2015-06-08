/**
 * Example form
 * 
 * @witdh
 * @height
 */
function SuperpositionForm(args) {
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
	this.superpositionGrid = new SuperpositionGrid({
		width : null,
		height : 200
	});

	this.superpositionGrid.onSelected.attach(function(sender, superpositions) {
		var ids = [];
		for ( var i in superpositions) {
			ids.push(superpositions[i].superpositionId);
		}
//		_this._renderAbinitio(ids);
//		_this.aprioriPDBViewer.refresh();
		_this._renderAligned(ids);
		//		getAlignedPDBContentBySuperpositionList
	});

	/** PDB viewer **/
//	this.abinitioPDBViewer = new PDBViewer({
//		width : 860 / 2,
//		height : 300 / 2,
//		margin : 0
//	});
//
//	/** PDB viewer **/
//	this.aprioriPDBViewer = new StructurePDBViewer({
//		width : 860 / 2,
//		height : 300 / 2,
//		margin : 0
//	});

	this.alignedPDBViewer = new AlignedSuperpositionPDBViewer({
		width : 860 ,
		height : 300
	});

}

SuperpositionForm.prototype._renderAligned = function(modelsIdList) {
	/** Trying to plot the PDB file **/
	try {
		this.alignedPDBViewer.refresh(modelsIdList);
	} catch (e) {
		console.log(e);
	}
};

SuperpositionForm.prototype._renderAbinitio = function(modelsIdList) {
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
		this.abinitioPDBViewer.refresh(viz);
	} catch (e) {
		console.log(e);
	}
};

SuperpositionForm.prototype._getItems = function() {
	var _this = this;
	return [ {
		xtype : 'label',
		forId : 'myFieldId',
		text : 'INLINE HELP: To be updated',
		margin : '15 0 20 10',
		cls : "inline-help"
	}, this.superpositionGrid.getPanel(), {
		xtype : 'container',
		layout : 'hbox',
		items : [
//		         {
//			xtype : 'container',
//			layout : 'vbox',
//			items : [ this.abinitioPDBViewer.getPanel(), this.aprioriPDBViewer.getPanel()
//
//			]
//		},

		this.alignedPDBViewer.getPanel() ]
	} ]

};

SuperpositionForm.prototype._getButtons = function() {
	return [];
};

SuperpositionForm.prototype.getPanel = function() {
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
SuperpositionForm.prototype._populate = function() {
};

/** It populates the form * */
SuperpositionForm.prototype.refresh = function(subtractions) {
	this.subtractions = subtractions;
	this.superpositionGrid.refresh(subtractions);
};

SuperpositionForm.prototype.input = function() {
	return {};
};

/** It populates the form **/
SuperpositionForm.prototype.test = function(targetId) {
	var macromoleculeForm = new SuperpositionForm();
	var panel = macromoleculeForm.getPanel();
	panel.render(targetId);
};
