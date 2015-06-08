/**
 * Shows an template with the specimens, measurements and the experiment's
 * requirement
 * 
 * @targetId
 */
function TemplateTabs(targetId) {
	this.height = 600;
	this.targetId = targetId;

	this.id = BUI.id();
	var _this = this;

	this.gridHeight = 1000;
	/** data * */
	this.experiment = null;

	this.specimenSelected = null;

	/** Specimen Widget contains a specimenGrid and a sampleChangerWidget than can be displayed with are vertical or horizontal layout **/
	this.specimenWidget = new SpecimenWidget({
		height : this.height
	});
	
	this.samplePlateGroupWidget = new SamplePlateGroupWidget({
		showTitle : false,
		height : 250,
		margin : 5,
		bbar : true
	});
	
	this.samplePlateGroupWidget.onExperimentChanged.attach(function(sender, experiment) {
		_this.refresh(experiment);
	});

	this.samplePlateGroupWidget.onClick.attach(function(sender, args) {
	});

	this.volumePlanificator = new VolumeGrid();

	/** For Measurements * */
	var storeViscosity = Ext.create('Ext.data.Store', {
		fields : [ 'name' ],
		data : [ {
			"name" : "low"
		}, {
			"name" : "medium"
		}, {
			"name" : "high"
		} ]
	});

	// Create the combo box, attached to the states data store
	var viscosityEditor = Ext.create('Ext.form.ComboBox', {
		fieldLabel : '',
		store : storeViscosity,
		queryMode : 'local',
		displayField : 'name',
		valueField : 'name'
	});

	this.measurementGrid = new MeasurementGrid({
		maxWidth : 1500,
		estimateTime : false,
		positionColumnsHidden : true,
		isPriorityColumnHidden : true,
		isStatusColumnHidden : true,
		isTimeColumnHidden : true,
		updateRowEnabled : true,
		collapsed : true,
		removeBtnEnabled : true,
		showTitle : false,
		collapseBtnEnable : false,
		addBtnMultipleEdit : true,
		sortingBtnEnable : true,
		editor : {
			exposureTemperature : {
				xtype : 'textfield',
				allowBlank : true
			},
			comments : {
				xtype : 'textfield',
				allowBlank : true
			},
			volumeToLoad : {
				xtype : 'numberfield',
				allowBlank : true
			},
			transmission : {
				xtype : 'numberfield',
				allowBlank : true
			},
			viscosity : viscosityEditor,
			waitTime : {
				xtype : 'numberfield',
				allowBlank : true
			},
			flow : {
				xtype : 'checkbox',
				allowBlank : true
			}
		}
	});

	this.measurementGrid.onSelected.attach(function(sender, measurements) {
		var specimens = [];
		for ( var i = 0; i < measurements.length; i++) {
			specimens.push(_this.experiment.getSampleById(measurements[i].specimenId));
		}
	});

	this.measurementGrid.onMeasurementChanged.attach(function(sender, measurement) {
		_this.experiment.setMeasurement(measurement);
		_this.refresh(_this.experiment);
	});

	this.measurementGrid.onExperimentChanged.attach(function(sender, json) {
		_this.refresh(new Experiment(json));
	});

	this.measurementGrid.onRemoved.attach(function(sender, experiment) {
		_this.refreshSpecimen(new Experiment(experiment));
	});

	this.measurementGrid.onUpdateTime.attach(function(sender, args) {
		document.getElementById(_this.id + "_counter").innerHTML = args.hours + 'h,  ' + args.minutes + 'min and ' + args.seconds + ' seconds';
	});
	
}

TemplateTabs.prototype.refreshMeasurement = function(experiment) {
	this.experiment = experiment;
	this.experiment.onSaved = new Event(this);
	this.experiment.onSpecimenSaved = new Event(this);
	var experimentList = new ExperimentList([ this.experiment ]);
	this.measurementGrid.refresh(experimentList.getMeasurementsNotCollected(), experimentList);
};

TemplateTabs.prototype.refreshSpecimen = function(experiment) {
	this.experiment = experiment;
	this.experiment.onSaved = new Event(this);
	this.experiment.onSpecimenSaved = new Event(this);
	this.samplePlateGroupWidget.refresh(this.experiment);
	this.specimenWidget.refresh(this.experiment);
	this.volumePlanificator.refresh(this.experiment);
};

TemplateTabs.prototype.refresh = function(experiment) {
	// var start = new Date().getTime();
	this.experiment = experiment;
	this.experiment.onSaved = new Event(this);
	this.experiment.onSpecimenSaved = new Event(this);

	// var experimentList = new ExperimentList([this.experiment]);
	this.refreshMeasurement(experiment);
	this.refreshSpecimen(experiment);
	/** Refreshing grids * */
	this.panel.setLoading(false);

};

TemplateTabs.prototype.error = function(error) {
	var e = JSON.parse(error);
	showError(e);
	this.panel.setLoading(false);

};

TemplateTabs.prototype.draw = function(experiment) {
	this.render(experiment);
};


TemplateTabs.prototype.getExperimentTitle = function() {
	var _this = this;
	var experimentHeaderForm = new ExperimentHeaderForm();
	return experimentHeaderForm.getPanel(this.experiment);
};

TemplateTabs.prototype.render = function(experiment) {
	var _this = this;
	this.experiment = experiment;

	/** 
	 * 
	 * Depending on the sample Changer configuration we want to display the plates vertically or horizontally 
	 * Default is vertical
	 * 
	 * */
	
	var specimenContainer = this.specimenWidget.getPanel();
	this.specimenWidget.refresh(experiment);
	
	var experimentList = new ExperimentList([ _this.experiment ]);
	var measurementContainer = Ext.create('Ext.container.Container', {
		layout : {
			type : 'vbox'
		},
		defaults : {
			style : {
				padding : '5px 0px 0px 10px'
			}
		},
		items : [ _this.measurementGrid.getPanel(experimentList.getMeasurementsNotCollected(), experimentList) ]
	});

	this.panel = Ext
			.createWidget(
					'tabpanel',
					{
						plain : true,
						items : [
							{
								tabConfig : {
									title : 'Measurements'
								},
								items : [ {
									xtype : 'container',
									layout : 'vbox',
									border : 1,
//									height : _this.gridHeight,
									margin : "0 0 10 0",
									items : [ measurementContainer ]
								}

								]
							},
							{
								tabConfig : {
									id : 'genralTabl',
									title : "Specimens"
								// width : 900,
								// border : 3

								},
								items : [ specimenContainer ]
							},
							{
								tabConfig : {
									title : "Requirements"

								},
								items : [
									{
										html : BUI.getTipHTML("Estimated volume is the maximum volume required. Depending on the order of your measurements you may use less. Click on create stock solutions if you plan to ship these stock solutions"),
										margin : "10 10 10 10",
										border : 0
									}, this.volumePlanificator.getPanel(experiment) ]
							} ]
					});
	// );
	return this.getPanel(this.panel);
};

TemplateTabs.prototype.isTemplate = function() {
	if (this.experiment.json.type == "TEMPLATE") {
		return true;
	}
	return false;
};


TemplateTabs.prototype.getPanel = function(panel) {
	var _this = this;
	if (this.experimentPanel == null) {
		this.experimentPanel = Ext.create('Ext.container.Container', {
			bodyPadding : 2,
			width : 1000,//Ext.getBody().getViewSize().width * 0.9,
			renderTo : this.targetId,
			height : 500,
//			style : {
//				padding : 2
//			},
			items : [ 
			          this.getExperimentTitle(), 
			          this.panel 
	        ],
			listeners : {
				afterrender : function(thisCmp) {
					$("#SchemeReport" + _this.experiment.experimentId).click(function() {
						$(this).target = "_blank";
						window.open('viewProjectList.do?reqCode=display&menu=platescheme&experimentId=' + _this.experiment.experimentId);
						return false;
					});

				}
			}
		});
	}
	return this.experimentPanel;
};

TemplateTabs.prototype.input = function(targetId) {
	return new ExperimentTabs().input();
};

TemplateTabs.prototype.test = function(targetId) {
	var experimentTabs = new TemplateTabs(targetId);
	BIOSAXS.proposal = new Proposal(experimentTabs.input().proposal);
	experimentTabs.draw(new Experiment(experimentTabs.input().experiment));
};
