var tabs;
Ext.Loader.setConfig({
	enabled : true
});
Ext.Loader.setPath('Ext.ux', '../js/external/ux');
Ext.QuickTips.init();

var BIOSAXS = {
	updateCount : 0,
	proposal : new Proposal(),
	getBeans : function() {
		return BIOSAXS_BEANS;
	},
	welcome : function(targetId) {
		this.fetchWelcome();
	},
	start : function(targetId) {
		/** For IE * */
		var console = console || {
			"log" : function() {
			}
		};

		this.targetId = targetId;
		var _this = this;
		Ext.require([ 'Ext.grid.*', 'Ext.data.*', 'Ext.util.*', 'Ext.state.*', 'Ext.form.*', 'Ext.tab.*', 'Ext.ux.CheckColumn', 'Ext.ux.RowExpander',
				'Ext.selection.CheckboxModel', 'Ext.layout.container.Column', 'Ext.ux.grid.FiltersFeature', 'Ext.selection.CellModel', 'Ext.util.*', 'Ext.form.*',
				'Ext.ux.form.MultiSelect', 'Ext.ux.form.ItemSelector', 'Ext.chart.*', 'Ext.Window', 'Ext.fx.target.Sprite', 'Ext.layout.container.Fit', 'Ext.window.MessageBox',
				'Ext.ux.data.PagingMemoryProxy', 'Ext.toolbar.Paging', 'Ext.ux.SlidingPager', 'Ext.form.Panel', 'Ext.ux.form.MultiSelect', 'Ext.ux.form.ItemSelector',
				'Ext.ux.ajax.JsonSimlet', 'Ext.ux.ajax.SimManager', 'Ext.tip.QuickTipManager', 'Ext.layout.*', 'Ext.form.Label', 'Ext.selection.*', 'Ext.tab.Panel', 'Ext.tree.*',
				'Ext.ux.LiveSearchGridPanel' ]);

		BIOSAXS.proposal.onInitialized = new Event();

		if (Ext.urlDecode(window.location.href).menu) {
			if (Ext.urlDecode(window.location.href).menu == "stocksolutions") {
				this.showStockSolutionsGrid(targetId);
			}
			if (Ext.urlDecode(window.location.href).menu == "results") {
				this.showResults(targetId);
			}
			if (Ext.urlDecode(window.location.href).menu == "macromolecule") {
				if ((Ext.urlDecode(window.location.href).macromoleculeId != null) || (Ext.urlDecode(window.location.href).search)) {
					this.showMacromolecule(targetId, Ext.urlDecode(window.location.href).macromoleculeId);
				}
			}
			if (Ext.urlDecode(window.location.href).menu == "prepareexperiment") {
				new PrepareExperimentWidget().draw(this.targetId);
			}
			if (Ext.urlDecode(window.location.href).menu == "macromolecules") {
				this.showMacromoleculesGrid(targetId);
			}
			if (Ext.urlDecode(window.location.href).menu == "buffers") {
				this.showBuffersGrid(targetId);
			}
			if (Ext.urlDecode(window.location.href).menu == "templates") {
				this.showTemplateGrid(targetId);
			}

			if (Ext.urlDecode(window.location.href).menu == "platescheme") {
				this.showPlateScheme(targetId);
			}

			if (Ext.urlDecode(window.location.href).menu == "plates") {
				this.showPlatesGrid(targetId);
			}

			if (Ext.urlDecode(window.location.href).menu == "datacollection") {
				this.showDataCollection(targetId);
			}

			if (Ext.urlDecode(window.location.href).menu == "PDBVisualizer") {
				if (Ext.urlDecode(window.location.href).modelId != null) {
					var adapter = new BiosaxsDataAdapter();
					adapter.onSuccess.attach(function(sender, data) {
						if (data != null) {
							for (var i = 0; i < data.length; i++) {
								if (data[i].subtractionId == Ext.urlDecode(window.location.href).subtractionId) {
									var modelId = Ext.urlDecode(window.location.href).modelId;
									var dataCollectionPDBWidget = new DataCollectionPDBWidget({
										width : Ext.getBody().getWidth() - 200,
										height : 600,
										title : "Damaver"
									});
									dataCollectionPDBWidget.draw(data[i], targetId);
								}
							}
						}
					});
					adapter.getAnalysisInformationByExperimentId(Ext.urlDecode(window.location.href).experimentId);
				}
			}
			/** Wizard for creating a new experiment **/
			if (Ext.urlDecode(window.location.href).menu == "design") {
				var wizardWidget = new WizardWidget({
					windowMode : true
				});

				wizardWidget.onFinished.attach(function(sender, result) {
					var adapter = new BiosaxsDataAdapter();
					adapter.onSuccess.attach(function(sender, data) {
						var experiment = new Experiment(data);
						_this.openExperimentByExperiment(experiment);
					});
					adapter.createTemplate(result.name, "comments", result.data);
				});

				wizardWidget.draw(this.targetId, new ExperimentTypeWizardForm());
			}

			if (Ext.urlDecode(window.location.href).menu == "list_shipment") {
				new ShippingWidget().draw(this.targetId);
			}
			if (Ext.urlDecode(window.location.href).menu == "shipments") {
				this.openShipments(targetId);
			}

			if (Ext.urlDecode(window.location.href).menu == "shipment") {
				this.openShipmentById(targetId);
			}

			if (Ext.urlDecode(window.location.href).menu == "show_datacollection") {
				this.showCreateDataCollectionList(targetId);
			}

			if (Ext.urlDecode(window.location.href).menu == "runs") {
				this.showRuns(targetId);
			}
			if (Ext.urlDecode(window.location.href).menu == "samplechanger") {
				this.showExperiments(targetId, "STATIC");
			}
			if (Ext.urlDecode(window.location.href).menu == "hplc") {
				this.showExperiments(targetId, "HPLC");
			}
			if (Ext.urlDecode(window.location.href).menu == "queue") {
				this.showQueue(targetId);
			}

			if (Ext.urlDecode(window.location.href).menu == "calibration") {
				this.showExperiments(targetId, "CALIBRATION");
			}
		} else {
			if (Ext.urlDecode(window.location.href).experimentId != null) {
				this.openExperiment(Ext.urlDecode(window.location.href).experimentId);
			} else {
				this.showExperiments(targetId);
			}

		}
	},

	showDataCollection : function(targetId) {

		var panel = Ext.create('Ext.panel.Panel', {
			plain : true,
			frame : false,
			width : 1000,
			border : 0,
			renderTo : targetId,
			style : {
				padding : 10
			},
			items : []
		});

		BIOSAXS.proposal.onInitialized.attach(function() {
			var adapter = new BiosaxsDataAdapter();
			adapter.onSuccess.attach(function(sender, data) {
				var dataCollectionWidget = new DataCollectionWidget();
				/** Removing event **/
				BIOSAXS.proposal.onInitialized = new Event(BIOSAXS.proposal);
				panel.add(dataCollectionWidget.getPanel(data));
			});

			var subtractionId = Ext.urlDecode(window.location.href).subtractionId;
			adapter.getCompactAnalysisBySubtractionId(subtractionId);
		});
		BIOSAXS.proposal.init();
	},
	showQueue : function(targetId) {
		var _this = this;

		var i = 0;
		var key = {};
		var grid = new QueueGrid({
			          isGrouped : false
			
		});
		grid.getPanel().render(targetId);
		BIOSAXS.proposal.onInitialized.attach(function() {
			grid.refresh();
		});

		BIOSAXS.proposal.init();
	},

	showPlateScheme : function(targetId) {
		var _this = this;
		new SchemeReport().draw(targetId, Ext.urlDecode(window.location.href).experimentId);
	},

	fetchWelcome : function() {
		BIOSAXS.proposal.onInitialized.attach(function() {
			if (BIOSAXS.proposal != null) {
				document.getElementById("macromolecule_panel").innerHTML = "NA";
				document.getElementById("buffer_panel").innerHTML = "NA";
				document.getElementById("stocksolution_panel").innerHTML = "NA";
				document.getElementById("shipment_panel").innerHTML = "NA";
				if (BIOSAXS.proposal.getMacromolecules() != null) {
					document.getElementById("macromolecule_panel").innerHTML = BIOSAXS.proposal.getMacromolecules().length;
				}
				if (BIOSAXS.proposal.getBuffers() != null) {
					document.getElementById("buffer_panel").innerHTML = BIOSAXS.proposal.getBuffers().length;
				}

				if (BIOSAXS.proposal.getStockSolutions() != null) {
					document.getElementById("stocksolution_panel").innerHTML = BIOSAXS.proposal.getStockSolutions().length;
				}

				if (BIOSAXS.proposal.getShipments() != null) {
					document.getElementById("shipment_panel").innerHTML = BIOSAXS.proposal.getShipments().length;
				}
			}
		});
		BIOSAXS.proposal.init();

		var adapter2 = new BiosaxsDataAdapter();
		adapter2.onSuccess.attach(function(sender, data) {
			var templateCount = 0;
			if (data != null) {
				for (var i = 0; i < data.length; i++) {
					if (data[i].experimentType != null) {
						if (data[i].experimentType == 'TEMPLATE') {
							templateCount++;
						}
					}
				}
			}
			document.getElementById("experiment_panel").innerHTML = templateCount;
		});
		adapter2.getExperimentInformationByProposalId();
	},

	/**
	 * PREPARE EXPERIMENT
	 * MACROMOLECULES
	 **/
	showMacromoleculesGrid : function(targetId) {
		BIOSAXS.proposal.onInitialized.attach(function() {
			var grid = new MacromoleculeGrid({
				width : 900,
				collapsed : false,
				searchBar : true,
				tbar : true,
				height : 700

			}).getPanel(BIOSAXS.proposal.getMacromolecules());
			var panel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				border : 0,
				height : 710,
				width : 900,
				renderTo : targetId,
				style : {
					padding : 0
				},
				items : [ grid ]
			});
		});
		BIOSAXS.proposal.init();
	},

	/**
	 * PREPARE EXPERIMENT
	 * BUFFERS
	 **/
	showBuffersGrid : function(targetId) {
		BIOSAXS.proposal.onInitialized.attach(function(sender) {
			var grid = new BufferGrid({
				height : 700,
				searchBar : true,
				tbar : true
			});
			this.panel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				height : 725,
				border : 0,
				renderTo : targetId,
				style : {
					padding : 10
				},
				items : [ grid.getPanel(BIOSAXS.proposal.getBuffers()) ]
			});

		});
		BIOSAXS.proposal.init();
	},

	showTemplateGrid : function(targetId) {
		BIOSAXS.proposal.onInitialized.attach(function(sender) {
			var _this = this;
			var grid = new TemplateGrid({
				height : 700,
				gridType : 'Ext.grid.Panel',
				title : 'Experiments',
				grouping : false,
				tbar : true
			});

			this.panel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				height : 725,
				border : 0,
				renderTo : targetId,
				style : {
					padding : 10
				},
				items : [ grid.getPanel([]) ]
			});

			var adapter = new BiosaxsDataAdapter();

			adapter.onSuccess.attach(function(sender, data) {
				grid.refresh(data);
				_this.panel.setLoading(false);
			});

			this.panel.setLoading("ISPyB: Retrieving Templates");
			adapter.getExperimentInformationByProposalId();
		});
		BIOSAXS.proposal.init();
	},

	showMacromolecule : function(targetId, macromoleculeId) {
		BIOSAXS.proposal.onInitialized.attach(function() {
			var adapter = null;
			if (Ext.urlDecode(window.location.href).search != null) {
				adapter = new BiosaxsDataAdapter();
				adapter.onSuccess.attach(function(sender, data) {
					new ResultTabs().draw(targetId, data, macromoleculeId);
				});
				adapter._getInformationByMacromoleculeId(JSON.parse(Ext.urlDecode(window.location.href).search.replace(new RegExp("'", 'g'), "\"")));
			} else {
				adapter = new BiosaxsDataAdapter();
				adapter.onSuccess.attach(function(sender, data) {
					new ResultTabs().draw(targetId, data, macromoleculeId);
				});
				adapter.getInformationByMacromoleculeId(macromoleculeId, Ext.urlDecode(window.location.href).bufferId);
			}
		});
		BIOSAXS.proposal.init();
	},

	showResults : function(targetId) {
		var _this = this;
		
		var macromoleculeGrid = new MacromoleculeGrid({
			width : 900,
			height : 250,
			collapsed : false,
			tbar : false,
			multiselect : false
		});
		
		var grid = new QueueGrid({
	          isGrouped : false,
	          margin : "10 0 0 0",
	          height : 600,
	          tbar : false,
	          bbar : false
		});
		
		var panel = Ext.create('Ext.panel.Panel', {
			plain : true,
			frame : false,
			height : 900,
			width : Ext.getBody().getWidth() - 200,
			border : 0,
			renderTo : targetId,
			padding : 10,
			items : [
			         macromoleculeGrid.getPanel(),
			         grid.getPanel()]
		});
		
		macromoleculeGrid.grid.setLoading("ISPyB: Retrieving proposal info");
		
		macromoleculeGrid.onSelected.attach(function(sender, macromolecules){
			var adapter = new BiosaxsDataAdapter();
			adapter.onSuccess.attach(function(sender, data){
				grid.refresh(data);
			});
			adapter.getCompactAnalysisByMacromoleculeId(macromolecules[0].macromoleculeId);
		});
		
		BIOSAXS.proposal.onInitialized.attach(function(sender) {
			macromoleculeGrid.refresh(BIOSAXS.proposal.macromolecules);
			
			var adapter2 = new BiosaxsDataAdapter();
			adapter2.onSuccess.attach(function(sender, data) {
				console.log(data)
				macromoleculeGrid.grid.setLoading(false);
				
			});
			adapter2.onError.attach(function(sender, data) {
				macromoleculeGrid.grid.setLoading(false);
			});
			macromoleculeGrid.grid.setLoading("ISPyB:  Retrieving data collections info");
			adapter2.getAnalysisByProposalId();
			
		});
		BIOSAXS.proposal.init();
		
//		var _this = this;
//		var args = {
//			height : 600,
//			searchBar : false,
//			tbar : false,
//			btnResultVisible : true,
//			width : Ext.getBody().getWidth() - 250
//		};
//
//		var panel = Ext.create('Ext.panel.Panel', {
//			plain : true,
//			frame : false,
//			height : 725,
//			width : Ext.getBody().getWidth() - 200,
//			border : 0,
//			renderTo : targetId,
//			padding : 10,
//			items : []
//		});
//		panel.setLoading("ISPyB: Getting information");
//		BIOSAXS.proposal.onInitialized.attach(function(sender) {
//			var adapter = new BiosaxsDataAdapter();
//			adapter.onSuccess.attach(function(sender, data) {
//				debugger
////				var grid = new ResultsAssemblyGrid(args);
////				panel.insert(0, grid.getPanel(data));
////				panel.setLoading(false);
//			});
//			adapter.onError.attach(function(sender, data) {
//				alert("There was an error");
//				panel.setLoading(false);
//			});
//			adapter.getAnalysisByProposalId();
//		});
//		BIOSAXS.proposal.init();
	},

	showRuns : function(targetId) {
		var items = [];
		var framesetGrid = new FramesetGrid({
			height : 400
		});

		var grid = framesetGrid.getGrid([]);
		var panel = Ext.create('Ext.panel.Panel', {
			plain : true,
			frame : false,
			border : 0,
			height : 565,
			renderTo : targetId,
			style : {
				padding : 0
			},
			items : [ grid ]
		});

		grid.setLoading("ISPyB: getting RUNS");

		var dataAdapter = new BiosaxsDataAdapter();
		dataAdapter.onRunsRetrieved.attach(function(evt, runs) {
			framesetGrid.refresh(runs);
			grid.setLoading(false);
		});
		dataAdapter.getAllRuns();
	},

	openShipmentById : function(targetId) {
		var _this = this;
		BIOSAXS.proposal.onInitialized = new Event();
		BIOSAXS.proposal.onInitialized.attach(function(sender) {
			var shippingId = Ext.urlDecode(window.location.href).shippingId;
			for (var i = 0; i < BIOSAXS.proposal.getShipments().length; i++) {
				if (BIOSAXS.proposal.getShipments()[i].shippingId == shippingId) {
					_this.showShipmentTabs(BIOSAXS.proposal.getShipments()[i], targetId);
				}
			}

		});
		BIOSAXS.proposal.init();
	},
	openShipments : function(targetId) {
		var _this = this;
		var grid = new ShipmentGrid({
			height : 700,
			title : 'Shipments',
			minHeight : 700
		});

		this.panel = Ext.create('Ext.panel.Panel', {
			plain : true,
			frame : false,
			height : 725,
			border : 0,
			renderTo : targetId,
			style : {
				padding : 10
			},
			items : [ grid.getPanel([]) ]
		});

		BIOSAXS.proposal.onInitialized = new Event();
		BIOSAXS.proposal.onInitialized.attach(function(sender) {
			grid.refresh(BIOSAXS.proposal.getShipments());
			_this.panel.setLoading(false);
		});

		this.panel.setLoading("ISPyB: Retrieving shipments");
		BIOSAXS.proposal.init();
	},

	showShipmentTabs : function(shipment, targetId) {
		BIOSAXS.proposal.onInitialized = new Event();
		document.getElementById(targetId).innerHTML = "";
		var tabs = new ShipmentTabs(targetId);

		var tabsPanel = tabs.getPanel(new Shipment(shipment));
		var panel = Ext.create('Ext.panel.Panel', {
			plain : true,
			frame : false,
			border : 0,
			height : 900,
			renderTo : targetId,
			style : {
				padding : 0
			},
			items : tabsPanel
		});

	},

	/**
	 * DATA ACQUISITION
	 * MAIN MENU / ALL
	 **/
	showExperiments : function(targetId, filtered) {
		var _this = this;
		var items = [];
		this.experimentGrid = new ExperimentGrid({
			width : '96%',
			tbar : true,
			filtered : filtered,
			sessionId : Ext.urlDecode(window.location.href).sessionId
		});
		var grid = this.experimentGrid.getPanel([]);
		this.panel = Ext.create('Ext.panel.Panel', {
			plain : true,
			frame : false,
			resizable : true,
			border : 0,
			renderTo : targetId,
			style : {
				padding : 0
			},
			items : [ grid ]
		});

		grid.setLoading("ISPyB: Retrieving Experiments");

		this.proposal.onInitialized.attach(function(sender) {
			var adapter = new BiosaxsDataAdapter();
			adapter.onSuccess.attach(function(sender, data) {
				if (data.length === 0) {
					_this.panel.insert(0, {
						border : 0,
						padding : 0,
						html : BUI.getWarningHTML("No experiments found for this proposal. Go to menu Prepare Experiment -> Experiment -> Create") + "<br/>"
					});
				}
				_this.experimentGrid.refresh(data);
				grid.setLoading(false);

				function updateExperiments() {
					var adapter = new BiosaxsDataAdapter();
					adapter.onSuccess.attach(function(sender, data) {
						grid.setLoading("Updating");
						_this.experimentGrid.refresh(data);
						grid.setLoading(false);

						BIOSAXS.updateCount = BIOSAXS.updateCount + 1;
						window.clearInterval(inter);
						inter = setInterval(function() {
							updateExperiments();
						}, BUI.getUpdateInterval());
					});
					adapter.getExperimentInformationByProposalId(Ext.urlDecode(window.location.href).sessionId);

				}
				var inter = setInterval(function() {
					updateExperiments();
				}, BUI.getUpdateInterval());

			});

			adapter.getExperimentInformationByProposalId(Ext.urlDecode(window.location.href).sessionId);
		});

		this.proposal.init(Ext.urlDecode(window.location.href).sessionId);
	},

	/** Prepare Experiment --> View --> Plates * */
	showPlatesGrid : function(targetId) {
		var dataAdapter = new BiosaxsDataAdapter();
		dataAdapter.onExperimentByProposalRetrieved.attach(function(evt, data) {
			var experiments = [];
			var i = null;
			for (i = 0; i < data.length; i++) {
				experiments.push(new Experiment(data[i]));
			}

			data = [];
			for (i = 0; i < experiments.length; i++) {
				var samplePlates = experiments[i].getSamplePlates();
				for (var j = 0; j < samplePlates.length; j++) {
					data.push(samplePlates[j]);
				}
			}

			var items = [];
			if (data.length === 0) {
				items.push({
					border : 0,
					html : BUI.getWarningHTML("No plates found. Go into the experiment and go to tab Plates and click on add to create a new one")
				});
			}

			items.push(new PlatesMacromoleculesGrid({
				height : 400
			}).getGrid(experiments));

			this.panel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				border : 0,
				height : 550,
				renderTo : targetId,
				style : {
					padding : 10
				},
				items : items
			});
		});
		dataAdapter.getExperimentsByProposalId();
	},

	showStockSolutionsGrid : function(targetId) {
		var _this = this;

		BIOSAXS.proposal.onInitialized.attach(function(sender) {
			var grid = new StockSolutionGrid({
				height : 700,
				searchBar : false,
				tbar : true
			});
			_this.test = grid;
			var items = [];
			items.push(grid.getPanel(BIOSAXS.proposal.getStockSolutions()));

			this.panel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				height : 725,
				border : 0,
				renderTo : targetId,
				style : {
					padding : 10
				},
				items : items
			});

		});
		BIOSAXS.proposal.init();
	},
	openExperiment : function(experimentId) {
		var _this = this;
		if (this.panel == null) {
			this.panel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				height : 725,
				border : 0,
				renderTo : this.targetId,
				style : {
					padding : 10
				},
				items : []
			});
		}

		this.panel.setLoading("Retrieving Experiment");

		if (BIOSAXS.proposal.macromolecules == null) {

			BIOSAXS.proposal.onInitialized.attach(function(sender) {
				if (experimentId == null) {
					experimentId = Ext.urlDecode(window.location.href).experimentId;
				}
				var dataAdapter = new BiosaxsDataAdapter();
				dataAdapter.onSuccess.attach(function(evt, data) {
					_this.panel.setLoading(false);
					_this.openExperimentByExperiment(new Experiment(data));
				});
				dataAdapter.getExperimentById(experimentId, "MEDIUM");

			});
			BIOSAXS.proposal.init(Ext.urlDecode(window.location.href).sessionId);
		} else {
			var dataAdapter = new BiosaxsDataAdapter();
			dataAdapter.onSuccess.attach(function(evt, data) {
				_this.panel.setLoading(false);
				_this.openExperimentByExperiment(new Experiment(data));
			});
			dataAdapter.getExperimentById(experimentId, "MEDIUM");
		}
	},
	openExperimentByExperiment : function(experimentEntry) {
		document.getElementById("mainPanel").innerHTML = "";
		if ((experimentEntry.json.type == "STATIC") || (experimentEntry.json.type == "CALIBRATION")) {
			tabs = new ExperimentTabs("mainPanel");
		} else {
			if (experimentEntry.json.type == "HPLC") {
				tabs = new HPLCTabs("mainPanel");
			} else {
				tabs = new TemplateTabs("mainPanel");
			}
		}
		tabs.draw(experimentEntry);
	}
};

/** DEPRECATED * */
function openExperiment(experimentId) {
	BIOSAXS.openExperiment(experimentId);
}
