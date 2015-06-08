/** MACROMOLECULES SELECTOR **/
function MacromoleculeSelectorWizardForm(args) {
	var _this = this;
	this.height = 500;
	GenericStepWizardForm.call(this, "Select Macromolecules", {
		margin : "0 0 0 0",
		width : 475,
		border : 0,
		html : "Define the macromolecules involved in the experiment.<br /> Click on the macromolecule you want to select and <span style='font-weight:bold'>hold CONTROL</span> to select severals"
	}, {
		macromoleculesSelected : null
	}, function() {
		if (_this.nextForm != null) {
		}
	}, function() {

	});

}

MacromoleculeSelectorWizardForm.prototype.reload = function() {
	if (this.data.macromoleculesSelected != null) {
		for ( var i = 0; i < this.data.macromoleculesSelected.length; i++) {
			this.MacromoleculeGrid.selectById(this.data.macromoleculesSelected[i].macromoleculeId);
		}
	}
};

MacromoleculeSelectorWizardForm.prototype.getForm = function() {
	var _this = this;
	this.MacromoleculeGrid = new MacromoleculeGrid({
		height : 310,
		searchBar : false,
		tbar : false,
		collapsed : false,
		collapsible : false,
		btnEditVisible : false,
		btnRemoveVisible : false,
		multiselect : true,
		cssFontStyle : "font-size:small;"

	});

	this.MacromoleculeGrid.onSelected.attach(function(sender, selections) {
		_this.data.macromoleculesSelected = selections;
		var text = "Selected: ";
		for ( var i = 0; i < selections.length; i++) {
			text = text + selections[i].acronym + ", ";
			if (i == 5) {
				text = text + "<span style='font-weight:bold;'> and " + (selections.length - 6) + " more</span>";
				i = selections.length - 1;
			}
		}
		document.getElementById(_this.id + "selectHTML").innerHTML = text;
	});

	this.panel = Ext.create('Ext.panel.Panel', {
		plain : true,
		frame : false,
		height : 450,
		border : 0,
		margin : "20 5 5 10",
		items : [ _this.MacromoleculeGrid.getPanel([]), {
			margin : "10 0 0 10",
			width : 475,
			border : 0,
			html : "<div id='" + _this.id + "selectHTML'>None selected</div>"
		} ]
	});

	BIOSAXS.proposal.onInitialized.attach(function(sender) {
		_this.macromolecules = BIOSAXS.proposal.macromolecules;
		_this.MacromoleculeGrid.refresh(_this.macromolecules);
	});
	BIOSAXS.proposal.init();
	return this.panel;
};

MacromoleculeSelectorWizardForm.prototype.getNextForm = function() {
	if (this.nextForm == null) {
		this.nextForm = new MeasurementCreatorStepWizardForm(this.data.macromoleculesSelected);
	}
	return this.nextForm;
};
