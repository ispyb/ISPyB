function FinalStepWizardForm(measurements) {
	this.result = {};

	this.id = BUI.id();
	this.title = "Confirmation";
	this.description = "This is the last step of this wizard. Write a name and a description (optional) for your experiment. Remember you are able to add new measurements later on";
	this.height = 500;

	this.data = {
		measurements : measurements
	};
	var _this = this;
	this.onNext = function(data) {

		this.result = {
			name : Ext.getCmp(_this.id + "name").getValue(),
			comments : Ext.getCmp(_this.id + "description").getValue(),
			data : _this.data.measurements
		};

		_this.onWizardFinished.notify(this.result);
	};
	this.onBack = function() {
	};
	this.onWizardFinished = new Event(this);
}

FinalStepWizardForm.prototype.getForm = function() {
	this.panel = Ext.create('Ext.panel.Panel', {
		plain : true,
		frame : false,
		border : 1,
		margin : "100 100 100 100",

		fieldDefaults : {
			labelAlign : 'left',
			labelWidth : 90,
			anchor : '100%'
		},
		items : [ {
			xtype : 'textfield',
			name : 'Name',
			id : this.id + "name",
			width : 600,
			fieldLabel : 'Name',
			margin : '20 20 20 20',
			value : ''
		}, {
			xtype : 'textareafield',
			name : 'textarea1',
			id : this.id + "description",
			margin : '20 20 20 20',
			width : 600,
			fieldLabel : 'Description',
			value : ''
		} ]
	});
	return this.panel;
};

FinalStepWizardForm.prototype.getNextForm = function() {
	return null;
};

FinalStepWizardForm.prototype.input = function() {
	return [];
};

FinalStepWizardForm.prototype.test = function(targetId) {
	var experimentTypeWizardForm = new FinalStepWizardForm();
	var form = experimentTypeWizardForm.getForm();
	form.render(targetId);
};
