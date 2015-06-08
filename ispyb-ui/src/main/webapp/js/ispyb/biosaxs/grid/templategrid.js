/**
 * See ExperimentGrid
 * 
 */
function TemplateGrid(args) {

	if (args == null) {
		args = {};
	}
	args.sorters = [ {
		property : 'experimentId',
		direction : 'DESC'
	} ];

	ExperimentGrid.prototype.constructor.call(this, args);
}

TemplateGrid.prototype._getFilterTypes = ExperimentGrid.prototype._getFilterTypes;
TemplateGrid.prototype._prettyPrintMacromolecules = ExperimentGrid.prototype._prettyPrintMacromolecules;
TemplateGrid.prototype._getPercentage = ExperimentGrid.prototype._getPercentage;
TemplateGrid.prototype._getPercentageCollected = ExperimentGrid.prototype._getPercentageCollected;
TemplateGrid.prototype.getPercentageMerged = ExperimentGrid.prototype.getPercentageMerged;
TemplateGrid.prototype._prepareData = ExperimentGrid.prototype._prepareData;
TemplateGrid.prototype.getPanel = ExperimentGrid.prototype.getPanel;
TemplateGrid.prototype._renderGrid = ExperimentGrid.prototype._renderGrid;
TemplateGrid.prototype._editExperiment = ExperimentGrid.prototype._editExperiment;
TemplateGrid.prototype._removeExperimentById = ExperimentGrid.prototype._removeExperimentById;

TemplateGrid.prototype._getTopButtons = function() {
	/** Actions buttons * */
	var actions = [];

	actions.push(Ext.create('Ext.Action', {
		icon : '../images/add.png',
		text : 'Add experiment',
		handler : function(widget, event) {
			var wizardWidget = new WizardWidget({
				windowMode : true
			});

			wizardWidget.onFinished.attach(function(sender, result) {
				var adapter = new BiosaxsDataAdapter();
				adapter.onSuccess.attach(function(sender, data) {
					var experiment = new Experiment(data);
					BIOSAXS.openExperimentByExperiment(experiment);
					wizardWidget.window.close();
				});
				wizardWidget.current.setLoading("ISPyB: Creating experiment");
				adapter.createTemplate(result.name, "comments", result.data);
			});

//			wizardWidget.draw(this.targetId, new ExperimentTypeWizardForm());
			wizardWidget.draw(this.targetId, new MeasurementCreatorStepWizardForm(BIOSAXS.proposal.getMacromolecules()));
		}
	}));
	return actions;
};

TemplateGrid.prototype.refresh = function(data) {
	var filtered = [];
	for ( var i = 0; i < data.length; i++) {
		if (data[i].experimentType == "TEMPLATE") {
			filtered.push(data[i]);
		}
	}
	this.store.loadData(this._prepareData(filtered), false);
};

TemplateGrid.prototype._getColumns = function() {
	var _this = this;
	function actionItemRenderer(value, meta, record, rowIx, ColIx, store) {
		if (record.raw.buffer3VOs != null) {
			if (record.raw.buffer3VOs.length > 0) {
				return 'x-hide-display';
			}
		}

		if (record.data.platesCount > 0) {
			return 'x-hide-display';
		}
	}

	return [ {
		xtype : 'rownumberer',
		width : 40
	}, {
		text : 'experimentId',
		dataIndex : 'experimentId',
		name : 'experimentId',
		type : 'string',
		hidden : true
	}, {
		text : 'Name',
		dataIndex : 'name',
		name : 'name',
		type : 'string',
		flex : 1
	},

	{
		text : 'Type',
		dataIndex : 'type',
		name : 'type',
		type : 'string',
		hidden : true,
		flex : 1,
		renderer : function(val) {
			return val;
		}
	}, {
		text : 'Macromolecules',
		name : 'macromolecules_names',
		dataIndex : 'macromolecules_names',
		flex : 1,
		renderer : function(val) {
			return " <span style='font-weight:bold'>" + val + "</span>";
		}
	}, {
		id : _this.id + 'GO',
		width : 80,
		sortable : false,
		renderer : function(value, metaData, record, rowIndex, colIndex, store) {
			return BUI.getGreenButton('EDIT');
		}
	}, {
		id : _this.id + 'REMOVE',
		width : 100,
		sortable : false,
		renderer : function(value, metaData, record, rowIndex, colIndex, store) {
			return BUI.getRedButton('REMOVE');
		}
	} ];
};

TemplateGrid.prototype.input = function() {
	var experiments = DATADOC.getExperimentList_10();
	return {
		experiments : experiments,
		proposal : new MeasurementGrid().input().proposal

	};
};

TemplateGrid.prototype.test = function(targetId) {
	var experimentGrid = new TemplateGrid({
		height : 350,
		minHeight : 350,
		width : 1000,
		gridType : 'Ext.grid.Panel',
		title : 'Experiments',
		grouping : false,
		tbar : true

	});
	BIOSAXS.proposal = new Proposal(experimentGrid.input().proposal);
	var panel = experimentGrid.getPanel(experimentGrid.input().experiments);
	experimentGrid.refresh(experimentGrid.input().experiments);
	panel.render(targetId);
};
