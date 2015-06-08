
function MultipleEditMeasurementGridWindow(){
	this.id = BUI.id();
	var _this = this;
	this.measurementGrid = new MeasurementGrid({
		height					: 400, 
		maxHeight				: 400, 
		width					: 960, 
		maxWidth				: 1000,
		resizable				: false,
		estimateTime			: false, 
		positionColumnsHidden	: true, 
		isPriorityColumnHidden	: true,
		isStatusColumnHidden	: true,
		isTimeColumnHidden		: true,
		updateRowEnabled		: false,
		collapsed				: true,
		addBtnEnable			: false,
		showTitle				: false,
		removeBtnEnabled		: false,
		collapseBtnEnable		: false,
		addBtnMultipleEdit		: false,
		selModel 				: Ext.create('Ext.selection.CheckboxModel', {
									 	allowDeselect : true,
									 	scope			: this,
									 	mode			: 'MULTI',
								        listeners: {
								            selectionchange: function(sm, selections) {
								            	if (selections.length == 0){
								            		this.scope.measurementGrid._showStatusBarBusy("No measurements Selected");
								            		this.scope.saveBtn.setDisabled(true);
								            		this.scope.macromoleculeSaveBtn.setDisabled(true);
								            		this.scope.bufferSaveBtn.setDisabled(true);
								            	}
								            	else{
								            		this.scope.measurementGrid._showStatusBarReady(selections.length + " measurements selected");
								            		this.scope.saveBtn.setDisabled(false);
								            		this.scope.macromoleculeSaveBtn.setDisabled(false);
								            		this.scope.bufferSaveBtn.setDisabled(false);
								            	}
								            	
								            }
								        }
	    }),
		editor					: {},
		sorter					: [
		      					   		{
		      					   				property : 'priority',
		      					   				direction: 'ASC'
										}
										]
	});
	
	this.onExperimentChanged = new Event(this);
	
};


MultipleEditMeasurementGridWindow.prototype.getMacromoleculeCombo = function(){
	 this.macromoleculeCombo = BIOSAXS_COMBOMANAGER.getComboMacromoleculeByMacromolecules(BIOSAXS.proposal.getMacromolecules(), 
			 {
		 			labelWidth 	: 100,
		 			margin 		: '0 0 0 10',
		 			width 		: 300
		 
			 });
	this.macromoleculeCombo.setFieldLabel("or Macromolecule");
	return this.macromoleculeCombo;
};

MultipleEditMeasurementGridWindow.prototype.getBufferCombo = function(){
	 this.bufferCombo = BIOSAXS_COMBOMANAGER.getComboBuffers(BIOSAXS.proposal.getBuffers(),  {
		 	labelWidth 	: 100,
			margin 		: '0 0 0 10',
			width 		: 300

	 });
	 this.bufferCombo.setFieldLabel("or Buffer");
	 return this.bufferCombo;
};

MultipleEditMeasurementGridWindow.prototype.save = function(parameter, value){
		var selections = this.measurementGrid.grid.getSelectionModel().getSelection();
		var ids = new Array();
		for (var i = 0; i < selections.length; i++){
			ids.push(selections[i].raw.measurementId);
		}
		var _this = this;
		if (parameter != ""){
			this.measurementGrid.grid.setLoading("ISPyB: Saving measurements")
			if (ids.length > 0){
				var adapter = new BiosaxsDataAdapter();
				adapter.onSuccess.attach(function(sender, data){
					var experimentAdapter = new BiosaxsDataAdapter();
					experimentAdapter.onSuccess.attach(function(sender, data){
						var experiment = new Experiment(data);
						_this.measurementGrid.refresh(experiment.getMeasurements(), new ExperimentList([experiment]));
						_this.measurementGrid.grid.setLoading(false);
						_this.onExperimentChanged.notify(data);
						_this.measurementGrid._showStatusBarReady("Ready");
					});
					_this.measurementGrid.grid.setLoading("ISPyB: Getting Experiment")
					experimentAdapter.getExperimentById(_this.experiments.experiments[0].experimentId, "MEDIUM");
				});
				adapter.setMeasurementParameters( ids, parameter, value);
			}
		}
};

MultipleEditMeasurementGridWindow.prototype.draw = function(measurements, experients){
	var _this = this;
	this.experiments = experients;
	this.parameterComboBox =  Ext.create('Ext.form.ComboBox', {
	    fieldLabel				: 'Select Parameter',
	    labelWidth				: 100,
  	    width					: 300,
	    margin 					: "10 0 10 10",
	    store					: ["Exp. Temp.", "Volume To Load", "Transmission", "Wait Time", "Viscosity"],
	    queryMode				: 'local'
	});
	
	this.macromoleculeComboBox =  this.getMacromoleculeCombo();
	this.bufferComboBox = this.getBufferCombo();
	
	this.saveBtn = Ext.create('Ext.Button', {
	    text    		: 'Save',
	    width			: 100,
	    disabled		: true,
	    margin 			: "10 0 10 20",
	    scope			: this,
	    handler 		: function() {
	            						_this.save(this.parameterComboBox.getRawValue(), Ext.getCmp(this.id + "value").getValue());
	    }
	});
	
	this.macromoleculeSaveBtn = Ext.create('Ext.Button', {
	    text    		: 'Save',
	    width			: 100,
	    disabled		: true,
	    margin 			: "0 0 10 20",
	    scope			: this,
	    handler 		: function() {
	    								_this.save("macromoleculeId", this.macromoleculeComboBox.getValue());
	    }
	});
	
	this.bufferSaveBtn = Ext.create('Ext.Button', {
	    text    		: 'Save',
	    width			: 100,
	    disabled		: true,
	    margin 			: "0 0 10 20",
	    scope			: this,
	    handler 		: function() {
	    								_this.save("bufferId", this.bufferComboBox.getValue());
	    }
	});
	
	this.panel  = Ext.create('Ext.Window', {
		id				: this.id,
		title 			: "Multiple Edit",
		resizable		: true,
		constrain		: true,
		border			: 1,
		modal			: true,
		frame			: false,
		closable		: true,
		
		autoscroll		: true,
		layout			: { type: 'vbox',align: 'stretch'},
		width			: this.measurementGrid.width + 50,
        height			: this.measurementGrid.height +200,
		buttonAlign		:'right',
		items			:  [{
								xtype		: 'container',
								layout		: 'vbox',
								padding			: 10,
								items		: [
								     		   		this.measurementGrid.getPanel(measurements, experients),
								     		   		{
														xtype		: 'container',
														layout		: 'hbox',
														items		: [
														     		   		this.parameterComboBox,
															     		   	{
																          			xtype			: 'requiredtext',
																          			fieldLabel 		: 'Value',
																          			id				: _this.id + "value",
																          			margin 			: "10 0 10 20",
																          			width 			: '200',
																          			labelWidth		: 50,
																          			value 			: ''
																          	},
																          	this.saveBtn
														]
													},
													{
														xtype		: 'container',
														layout		: 'hbox',
														items		: [
														     		   		this.macromoleculeComboBox,
																          	this.macromoleculeSaveBtn
														]
													}
//													,
//													{
//														xtype		: 'container',
//														layout		: 'hbox',
//														items		: [
//														     		   		this.bufferComboBox,
//																          	this.bufferSaveBtn
//														]
//													}
								]
							}
		],
 		listeners: {
 						scope		: this,
 						minimize	: function(){
 										this.panel.hide();
 						},
 						destroy		: function(){
 										delete this.panel;
 						}
    	}
	});
	this.panel.show();
	
};







