
/**
 * Shows an experiments with the specimens, measurements, analysis tabs where
 * results are shown and the frames widget
 * 
 * @targetId
 */
function ExperimentTabs(targetId) {
	this.height = 900;
	this.targetId = targetId;

	this.id = BUI.id();
	var _this = this;

	this.INTERVAL_UPDATE = BUI.getUpdateInterval();

	this.gridHeight = 1000;
	/** data * */
	this.experiment = null;

	
	/** Specimen Widget contains a specimenGrid and a sampleChangerWidget than can be displayed with are vertical or horizontal layout **/
	this.specimenWidget = new SpecimenWidget({
		height : 600
	});
	
	
	/** For Overview * */
	/*this.samplePlateGroupWidget = new SamplePlateGroupWidget({
		showTitle : false,
		height : 250,
		margin : 5,
		border : 0

	});*/

	/** For Measurements * */
	/*this.measurementSamplePlateGroupWidget = new SamplePlateGroupWidget({
		showTitle : false,
		height : 250,
		margin : '5 0 0 0',
		border : 0
	});*/

	this.measurementGridDone = new MeasurementGrid({
		height : 600,
		minHeight : 400,
		maxHeight : 800,
		positionColumnsHidden : true,
		showTitle : false,
		estimateTime : true,
		width : 900,
		maxWidth : 1500,
		addBtnEnable : false,
		markDone : true,
		removeBtnEnabled : false
	});
	this.measurementGridDone.onSelected.attach(function(sender, measurements) {
		var specimens = [];
		for ( var i = 0; i < measurements.length; i++) {
			specimens.push(_this.experiment.getSampleById(measurements[i].specimenId));
		}
		//_this.measurementSamplePlateGroupWidget.selectSpecimens(specimens);
	});

	/** AnalysisGrid * */
	this.analysisGrid = new AnalysisGrid({
		height : Ext.getBody().getViewSize().height * 0.9 - 300,
		positionColumnsHidden : true,
		sorters : [ {
			property : 'priorityLevelId',
			direction : 'ASC'
		} ]
	});
	
	
	/** Queue * */
	this.queueGrid = new QueueGrid({
		url : BUI.getQueueUrlByExperiment(),
		height : Ext.getBody().getViewSize().height * 0.9 - 300,
		width : Ext.getBody().getViewSize().width * 0.9 - 300,
		isGrouped : true,
		tbar : false,
		bbar : false,
		sorter : [{
	        property: 'measurementId',
	        direction: 'DESC'
	    }]
	});

}

ExperimentTabs.prototype.error = function(error) {
	var e = JSON.parse(error);
	showError(e);
	this.panel.setLoading(false);
};

ExperimentTabs.prototype.draw = function(experiment) {
	this.renderDataAcquisition(experiment);
};

ExperimentTabs.prototype.refreshAnalysisData = function() {
	var _this = this;
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, data) {
		_this.analysisGrid.refresh(data, {experiment : _this.experiment});
	});
	adapter.getAnalysisInformationByExperimentId(this.experiment.experimentId);
	
	/** Auto load **/
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, data) {
		_this.queueGrid.refresh(data);
	});
	adapter.getCompactAnalysisByExperimentId(this.experiment.experimentId);
	this.queueGrid.store.proxy.url = BUI.getQueueUrlByExperiment(this.experiment.experimentId);
	this.queueGrid.refresh();
};

ExperimentTabs.prototype.getSpecimenContainerHeight = function(experiment) {
	var maxItems = 0;
	if (maxItems < experiment.getSamples().length + 1) {
		maxItems = experiment.getSamples().length + 1;
	}

	var height = (maxItems + 1) * 40 + 40;
	if (height > 400) {
		height = 400;
	}
	return height;
};

ExperimentTabs.prototype.getExperimentTitle = function() {
	var experimentHeaderForm = new ExperimentHeaderForm();
	return experimentHeaderForm.getPanel(this.experiment);
};

ExperimentTabs.prototype.renderDataAcquisition = function(experiment) {
	var _this = this;
	this.experiment = experiment;

	var specimenGrid = new SpecimenGrid({
		height : 400,
		maxHeight : 500,
		width : 890
	});

	specimenGrid.onClick.attach(function(sender, args) {
	});

	specimenGrid.onSelected.attach(function(sender, specimens) {
		//_this.samplePlateGroupWidget.selectSpecimens(specimens);
	});
	var specimenContainer = this.specimenWidget.getPanel();
	this.specimenWidget.refresh(experiment);
	/*
	var specimenContainer = Ext.create('Ext.container.Container', {
		layout : 'hbox',
		width : 900,
		padding : '10 0 0 2',
		items : [ specimenGrid.getPanel() ]
	});
	
	specimenGrid.refresh(experiment);*/
	
	var experimentList = new ExperimentList([ _this.experiment ]);
	var measurementContainer = Ext.create('Ext.container.Container', {
		layout : 'vbox',
		padding : '5px 0px 0px 10px',
		items : []
	});
	measurementContainer.insert(0, _this.measurementGridDone.getPanel(this.experiment.getMeasurements(), experimentList));
//	measurementContainer.insert(1, _this.measurementSamplePlateGroupWidget.getPanel(experiment));

	// this.dataCollectionCurveVisualizer = new DataCollectionCurveVisualizer();
	// this.dataCollectionCurveVisualizer.experiments = [experiment];
	// this.dataCollectionCurveVisualizer.dataCollectionFrameTree.experiments =
	// [experiment];
	// this.dataCollectionCurveVisualizer.dataCollections =
	// experiment.getDataCollections();

	this.panel = Ext.createWidget('tabpanel', {
		plain : true,
		style : {
			padding : 2
		},
		items : [ {
			tabConfig : {
				id : 'genralTabl',
				title : "Overview"
			},
			items : [  specimenContainer ]
		}, {
			tabConfig : {
				title : 'Measurements'
			},
			items : [ measurementContainer]
		}, {
			tabConfig : {
				id : 'SpecimenTab',
				title : 'Analysis',
				hidden : this.isTemplate()
			},
			items : [ {
				xtype : 'container',
				layout : 'vbox',
				padding : '5px 0px 10px 10px',
				items : [ _this.analysisGrid.getPanel([]) ]
			} ]
		},
		{
			tabConfig : {
				id : 'newAnalysisTab',
				title : 'Analysis <span style="font-size:8px;color:red;">BETA</span>',
				hidden : this.isTemplate()
			},
			items : [ {
				xtype : 'container',
				layout : 'vbox',
				padding : '5px 0px 10px 10px',
				items : [ _this.queueGrid.getPanel([]) ]
			} ]
		}
		]
	});

	return this.getPanel(this.panel);
};

ExperimentTabs.prototype.isTemplate = function() {
	if (this.experiment.json.type == "TEMPLATE") {
		return true;
	}
	return false;
};

ExperimentTabs.prototype.update = function() {
	var _this = this;
	var inter;
	if (!_this.isTemplate()) {
		function updateExperiments() {
			_this.refreshAnalysisData();
			window.clearInterval(inter);
			inter = setInterval(function() {
				updateExperiments();
			}, _this.INTERVAL_UPDATE);
			var adapter = new BiosaxsDataAdapter();
			adapter.onSuccess.attach(function(sender, data) {
				var experiment = new Experiment(data);
				_this.measurementGridDone.refresh(experiment.getMeasurements(), new ExperimentList([ experiment ]));
			});
			adapter.getExperimentById(_this.experiment.json.experimentId, "MEDIUM");
		}
		inter = setInterval(function() {
			updateExperiments();
		}, _this.INTERVAL_UPDATE);
	}
};

ExperimentTabs.prototype.getPanel = function(panel) {
	var _this = this;
	if (this.experimentPanel == null) {
		this.experimentPanel = Ext.create('Ext.container.Container', {
			bodyPadding : 2,
			width : Ext.getBody().getViewSize().width * 0.9,
			renderTo : this.targetId,
			style : {
				padding : 2
			},
			items : [ this.getExperimentTitle(), this.panel ],
			listeners : {
				afterrender : function() {
					_this.refreshAnalysisData();
					_this.update();
				}
			}
		});
	}

	return this.experimentPanel;
};

ExperimentTabs.prototype.input = function() {
	return {
		experiment : DATADOC.getExperiment_10(),
		proposal : DATADOC.getProposal_10()
	};
};

ExperimentTabs.prototype.test = function(targetId) {
	var experimentTabs = new ExperimentTabs(targetId);
	BIOSAXS.proposal = new Proposal(experimentTabs.input().proposal);
	experimentTabs.draw(new Experiment(experimentTabs.input().experiment));
};


