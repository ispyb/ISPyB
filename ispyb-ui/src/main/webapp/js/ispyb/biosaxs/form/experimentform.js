function ExperimentForm(args) {
	this.id = BUI.id();

	if (args != null) {
	}

	this.onSaved = new Event(this);
}

ExperimentForm.prototype._getItems = function(experiment) {
	this.experiment = experiment;
	var typeCombo = Ext.create('Ext.form.ComboBox', {
		id : this.id + 'type',
		fieldLabel : 'Type',
		store : [ "STATIC", "CALIBRATION", "HPLC" ],
		queryMode : 'local',
		labelWidth : 120,
		displayField : 'name',
		valueField : 'name',
		value : experiment.json.type,
		disabled : (experiment.json.type == 'TEMPLATE')
	});
	
	var items = [];
	
	
	if (experiment.json.type == "HPLC" ){
		var typeMacromolecule = BIOSAXS_COMBOMANAGER.getComboMacromoleculeByMacromolecules(BIOSAXS.proposal.getMacromolecules(), {labelWidth : 120, width: "120px"});
		if (experiment.getHPLCMacromolecule() != null){
			typeMacromolecule.setValue(experiment.getHPLCMacromolecule().macromoleculeId);
			items.push(typeMacromolecule);
		}
	}
	
	
	items.push(typeCombo, {
		id : this.id + 'name',
		xtype : 'textfield',
		fieldLabel : 'Name',
		labelWidth : 120,
		width : '100%',
		value : experiment.json.name
	}, {
		id : this.id + 'comments',
		xtype : 'textareafield',
		name : 'comments',
		fieldLabel : 'Comments',
		labelWidth : 120,
		height : 120,
		width : '100%',
		value : experiment.json.comments
	});
	return items;
};
ExperimentForm.prototype.getPanel = function(experiment) {
	var _this = this;
	
	this.panel = Ext.createWidget({
		xtype : 'container',
		layout : 'vbox',
		border : 0,
		style : {
			padding : '10px'
		},
		fieldDefaults : {
			labelAlign : 'left',
			labelWidth : 50
		},
		items : this._getItems(experiment)
	});
	return this.panel;
};

ExperimentForm.prototype.input = function() {
	return new ExperimentHeaderForm().input();
};

ExperimentForm.prototype.test = function(targetId) {
	var experimentForm = new ExperimentForm();
	var panel = experimentForm.getPanel(new Experiment(experimentForm.input().experiment));
	panel.render(targetId);
};
