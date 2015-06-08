function ExperimentTypeWizardForm(args) {
	var _this = this;
	this.height = 500;
	GenericStepWizardForm.call(
					this,
					"Select your type of experiment",
					{
						margin : "0 0 0 0",
						width : 475,
						border : 0,
						html : "Two type of experiments are allowed: <span style='font-weight:bold'>STATIC</span> and  <span style='font-weight:bold'>HPLC</span>. Static use quartz capillary as a part of automated sample changer allowing temperature variations (from 4 to 60°C). HPLC (high performance liquid chromatography) system can be used in parallel with sample changer"
					}, {
						experimentType : "None"
					}, function() {
						console.log(_this.data);
					}, function() {
					});

}

ExperimentTypeWizardForm.prototype.getForm = function() {
	var _this = this;
	return Ext.create('Ext.panel.Panel', {
		width : 600,
		height : 250,
		border : 0,
		margin : "100 0 0 150",
		layout : "vbox",
		items : [ {
			xtype : 'fieldset',
			items : [ {
				xtype : 'checkboxgroup',
				width : 400,
				height : 100,
				border : 0,
				fieldLabel : 'Experiment Type',
				columns : 1,
				items : [ {
					boxLabel : 'Sample Changer',
					name : 'cb-col-2',
					checked : true
				}, {
					boxLabel : 'HPLC',
					name : 'cb-col-3',
					disabled : true
				} ]
			} ]
		} ]
	});
};

ExperimentTypeWizardForm.prototype.getNextForm = function() {
	return new MacromoleculeSelectorWizardForm();
};

ExperimentTypeWizardForm.prototype.input = function() {
	return [];
};

ExperimentTypeWizardForm.prototype.test = function(targetId) {
	var experimentTypeWizardForm = new ExperimentTypeWizardForm();
	var form = experimentTypeWizardForm.getForm();
	form.render(targetId);
};
