/**
 * Example form
 * 
 * @witdh
 * @height
 */
function AssemblyForm(args) {
	this.id = BUI.id();
	this.width = 700;
	this.height = 500;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	this.molarityGrid = new MolarityGrid({height : this.height - 50});
}

AssemblyForm.prototype._getItems = function() {
	return [ {
		xtype : 'label',
		forId : 'myFieldId',
		text : 'List of previously defined macromolecules present in the assembly. This information will be used for additional cross-checks where possible',
		margin : '15 0 20 10',
		cls : "inline-help"
	}, this.molarityGrid.getPanel() ];
};

AssemblyForm.prototype._getButtons = function() {
	return [];
};

AssemblyForm.prototype.getPanel = function() {
	this.panel = Ext.create('Ext.form.Panel', {
		width : this.width,
		height : this.height,
		margin : 10,
		border : 0,
		defaultType : 'textfield',
		items : this._getItems(),
		buttons : this._getButtons()
	});
	return this.panel;
};


/** It populates the form **/
AssemblyForm.prototype.refresh = function(macromolecule) {
	this.macromolecule = macromolecule;
	this.molarityGrid.refresh(macromolecule);
};

AssemblyForm.prototype.input = function() {
	return {};
};


AssemblyForm.prototype.test = function(targetId) {
	var assemblyForm = new AssemblyForm();
	
	var panel = assemblyForm.getPanel();
	panel.render(targetId);
};