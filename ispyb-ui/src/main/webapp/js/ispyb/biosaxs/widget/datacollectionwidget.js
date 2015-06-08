function DataCollectionWidget() {

};

DataCollectionWidget.prototype.refresh = function(subtractionId) {

};

DataCollectionWidget.prototype.getMacromolecule = function(data) {
	for (var i = 0; i < data.length; i++) {
		if (data[i].macromoleculeId != null) {
			return BIOSAXS.proposal.getMacromoleculeById(data[i].macromoleculeId);
		}
	}
	return null;
};

DataCollectionWidget.prototype.getMacromoleculeContainer = function(data) {
	var macromolecule = this.getMacromolecule(data);

	var disabled = false;
	if (macromolecule == null) {
		disabled = true;
	}

	var acronym = (macromolecule == null ? "" : macromolecule.acronym);
	var mm = (macromolecule == null ? "" : macromolecule.molecularMass);
	var comments = (macromolecule == null ? "" : macromolecule.comments);

	return Ext.create('Ext.Panel', {
		width : 500,
		height : 200,
		disabled : disabled,
		title : "Macromolecule",
		layout : 'form',
		bodyPadding : 5,
		defaultType : 'textfield',
		tbar : {
			defaultButtonUI : 'default',
			items : [ {
				xtype : 'button',
				text : 'Edit',
				cls : 'btn-with-border',
				handler : function() {
					var macromoleculeWindow = new MacromoleculeWindow();
					macromoleculeWindow.draw(macromolecule);
					/** TODO: update when save **/
				}
			} ]
		},
		items : [ {
			fieldLabel : 'Acronym',
			name : 'first',
			readOnly : true,
			value : acronym
		}, {
			fieldLabel : 'Molecular Mass',
			name : 'last',
			readOnly : true,
			value : mm
		}, {
			xtype : 'textareafield',
			fieldLabel : 'Comments',
			value : '',
			name : 'last',
			readOnly : true,
			value : comments
		} ]
	});
};

DataCollectionWidget.prototype.getBufferContainer = function(data) {
	var _this = this;
	var buffer = BIOSAXS.proposal.getBufferById(data[0].bufferId);

	return Ext.create('Ext.Panel', {
		width : 500,
		height : 200,
		title : "Buffer",
		layout : 'form',
		//	    collapsed : true,
		//	    collapsible : true,
		bodyPadding : 5,
		defaultType : 'textfield',
		style : {
			padding : '0px 0px 0px 2px'
		},
		tbar : {
			defaultButtonUI : 'default',
			items : [ {
				xtype : 'button',
				text : 'Edit',
				cls : 'btn-with-border',
				handler : function() {
					var bufferWindow = new BufferWindow();
					bufferWindow.draw(BIOSAXS.proposal.getBufferById(data[0].bufferId));
					/** TODO: update when save **/
				}

			} ]
		},
		items : [ {
			fieldLabel : 'Buffer',
			name : 'acronym',
			readOnly : true,
			value : buffer.acronym
		}, {
			fieldLabel : 'Composition',
			name : 'last',
			readOnly : true,
			value : buffer.composition
		}, {
			xtype : 'textareafield',
			fieldLabel : 'Comments',
			value : buffer.comments,
			name : 'last',
			readOnly : true

		} ]
	});
};

DataCollectionWidget.prototype.getSpecimenContainer = function(data) {
	return Ext.create('Ext.container.Container', {
		layout : {
			type : 'hbox'
		},
		style : {
			margin : '10px 10px 10px 10px'
		},
		border : 0,
		style : {
			borderColor : '#000000',
			borderStyle : 'solid',
			borderWidth : '1px'
		},
		defaults : {
			labelWidth : 80,
			// implicitly create Container by specifying xtype
			xtype : 'datefield',
			flex : 1,

		},
		items : [ this.getMacromoleculeContainer(data), this.getBufferContainer(data) ]
	});
};

DataCollectionWidget.prototype.getSeparator = function() {
	return {
		html : "<br/>",
		border : 0
	}
};

DataCollectionWidget.prototype.getFitStructurePanel = function(data) {
	var _this = this;

	return Ext.create('Ext.container.Container', {
		layout : {
			type : 'hbox'
		},
		style : {
			margin : '10px 10px 10px 10px'
		},
		border : 0,
		style : {
			borderColor : '#000000',
			borderStyle : 'solid',
			borderWidth : '1px'
		},
		defaults : {
			labelWidth : 80,
			xtype : 'datefield',
			flex : 1
		},
		items : [

		]
	});
};

DataCollectionWidget.prototype.getFitStructurePanelWorkflow = function(data) {
	var _this = this;
	var macromolecule = this.getMacromolecule(data);

	var items = [];

	/** Adding all the pdb files linked to the macromolecule **/
	//	if (macromolecule.structure3VOs != null) {
	//		if (macromolecule.structure3VOs.length > 0) {
	//			
	//			items.push();
	//		}
	//	} else {
	//		items.push({
	//			html : "No apriori information added to this macromolecule. Apriori information needed for further analysis",
	//			margin : "5 5 5 5"
	//		});
	//	}
	var fitStructureToExperimentDataGrid = new FitStructureToExperimentDataGrid();

	fitStructureToExperimentDataGrid.refresh(_this.data[0].subtractionId, _this.getMacromolecule(data));
	return Ext.create('Ext.container.Container', {
		style : {
			margin : '10px 10px 10px 10px'
		},
		border : 0,

		style : {
			borderColor : '#000000',
			borderStyle : 'solid',
			borderWidth : '1px'
		},
		defaults : {
			labelWidth : 80,
			xtype : 'datefield',
			flex : 1
		},
		items : [ fitStructureToExperimentDataGrid.getPanel() ],
		listeners : {
			afterrender : function() {
			}
		}
	});
};
/** Static method **/
DataCollectionWidget.prototype.RUNFitScattering = function(subtractionId, structureId) {
	var _this = this;
	/** Add to Workflow **/
	var adapter = new BiosaxsDataAdapter();
	var workflow = {
		'workflowTitle' : 'FitExperimentalDatatoStructure',
		'comments' : 'FitExperimentalDatatoStructure run from ISPyB for subtractionId: ' + subtractionId + ' and structureId ' + structureId
	}

	var inputs = [ {
		name : 'subtractionId',
		value : subtractionId
	}, {
		name : 'structureId',
		value : structureId
	} ];

	adapter.onSuccess.attach(function(sender, data) {
		/** Add to Fit **/
		var adapter2 = new BiosaxsDataAdapter();
		var fit = {
			'workflowId' : data.workflowId,
			'subtractionId' : subtractionId,
			'structureId' : structureId,
			'comments' : 'Sent to workflow engine'
		}
		adapter2.onSuccess.attach(function(sender, fit) {

		});
		adapter2.addFitStructureData(fit);

	});
	adapter.addWorkflow(workflow, inputs);

};

DataCollectionWidget.prototype.getSuperpositionTab = function(data) {
	var _this = this;

	this.superpositionGrid = new SuperpositionGrid();

	return Ext.create('Ext.container.Container', {
		style : {
			margin : '10px 10px 10px 10px'
		},
		border : 0,
		style : {
			borderColor : '#000000',
			borderStyle : 'solid',
			borderWidth : '1px'
		},
		items : [ _this.superpositionGrid.getPanel() ],
		listeners : {
			cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {

			},
			afterrender : function() {
				/** Getting Rigid bodies **/
				var adapter = new BiosaxsDataAdapter();
				adapter.onSuccess.attach(function(sender, result) {
					_this.superpositionGrid.refresh(result);
				});
				adapter.getSuperpositionBySubtractionId(data[1].subtractionId);
			}
		}
	});
};

DataCollectionWidget.prototype.getAdvancedTab = function(data) {
	var _this = this;
	return Ext.create('Ext.container.Container', {
		style : {
			margin : '10px 10px 10px 10px'
		},
		border : 0,
		style : {
			borderColor : '#000000',
			borderStyle : 'solid',
			borderWidth : '1px'
		},
		items : [ _this.getFitStructurePanel(data), _this.getFitStructurePanelWorkflow(data) ]
	});
};

DataCollectionWidget.prototype.getRigiBodyForm = function(data) {
	var _this = this;
	_this.rigiBodyGrid = new RigidModelGrid();
	return _this.rigiBodyGrid.getPanel();

};

DataCollectionWidget.prototype.getRigidBodyModelingTab = function(data) {
	var _this = this;
	return Ext.create('Ext.container.Container', {
		style : {
			margin : '10px 10px 10px 10px'
		},
		border : 0,
		style : {
			borderColor : '#000000',
			borderStyle : 'solid',
			borderWidth : '1px'
		},
		items : [ _this.getRigiBodyForm(data) ],
		listeners : {
			cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {

			},
			afterrender : function() {
				/** Getting Rigid bodies **/
				var adapter = new BiosaxsDataAdapter();
				adapter.onSuccess.attach(function(sender, result) {
					_this.rigiBodyGrid.refresh(result);
				});
				adapter.getRigidBodyModelingBySubtractionId(data[1].subtractionId);
			}
		}
	});
};

DataCollectionWidget.prototype.getTabs = function(data) {
	var _this = this;

	this.subtractionCurveVisualizer = new SubtractionCurveVisualizer();

	this.tabs = Ext.createWidget('tabpanel', {
		activeTab : 1,
		plain : true,
		defaults : {
			autoScroll : true,
			bodyPadding : 10
		},
		items : [ {
			title : 'Solution',
			items : [ _this.getSpecimenContainer(data) ]
		}, {
			title : 'Primary Data Reduction',
			active : true,
			tabConfig : {
				xtype : 'tab',
				margins : '0 0 0 20',
			},
			items : [ _this.subtractionCurveVisualizer.getPanel() ]
		}, {
			title : 'Abinitio',
			items : [ _this.getAbinitioModellingContainer(data), _this.getSeparator(), _this.getModelViz(data) ]
		}, {
			title : 'Mixtures',
			items : [ _this.getAdvancedTab(data) ]
		}, {
			title : 'Superpositions',
			items : [ _this.getSuperpositionTab(data) ]
		}, {
			title : 'Rigid Body Modeling',
			items : [ _this.getRigidBodyModelingTab(data) ]
		} ]
	});
	return this.tabs;
};

DataCollectionWidget.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;
	this.panel = Ext.create('Ext.container.Container', {
		width : 1000,
		layout : {
			type : 'vbox',
			align : 'stretch',
			padding : 5
		},
		items : [ _this.getTabs(data)

		]
	});

	this.panel.on("afterrender", function() {
		_this.subtractionCurveVisualizer.refresh([ _this.data[1].mergeId ], [ _this.data[1].subtractionId ]);
	});
	return this.panel;
};

DataCollectionWidget.prototype.getModelViz = function(data) {
	this.modelViz = new ModelVisualizerForm({
		height : 800,
		width : 1000
	});
	return this.modelViz.getPanel();
};

/**
 * PRIMARY DATA PROCESSING
 */
DataCollectionWidget.prototype.getPrimaryDataProcessingContainer = function(data) {
	var _this = this;
	this.primaryDataProcessingGrid = new PrimaryDataProcessingGrid({
		height : 250,
		width : 1000,
		title : 'Primary Data Processing'
	});

	this.primaryDataProcessingGrid.onSelected.attach(function(sender, selected) {
		/** Refresh tabs **/
		var averagesId = [];
		var subtractionIds = [];

		var subtractionKeys = {};
		for (var i = 0; i < selected.length; i++) {
			if (selected[i].mergeId != null) {
				averagesId.push(selected[i].mergeId);
			}

			if (selected[i].subtractionId != null) {
				if (selected[i].macromoleculeId != null) {
					if (subtractionKeys[selected[i].subtractionId] == null) {
						subtractionIds.push(selected[i].subtractionId);
					}
					subtractionKeys[selected[i].subtractionId] = true;
				}
			}
		}

		/** Refreshing Primary Data Reduction **/
		_this.subtractionCurveVisualizer.refresh(averagesId, subtractionIds);

	});
	return this.primaryDataProcessingGrid.getPanel(data);
};
/**
 * getAbinitioModellingContainer
 */
DataCollectionWidget.prototype.getAbinitioModellingContainer = function(data) {
	var _this = this;

	this.abinitioGrid = new AbinitioGrid();
	this.abinitioGrid.onSelected.attach(function(sender, models) {
		_this.modelViz.refresh(models);

	});
	/** It may be abinitio models linked to the buffers **/
	var abinitioIdList = [];
	for (var i = 0; i < data.length; i++) {
		abinitioIdList.push(data[i].abInitioId);
	}

	var uniqueIds = [];
	$.each(abinitioIdList, function(i, el) {
		if ($.inArray(el, uniqueIds) === -1)
			uniqueIds.push(el);
	});

	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, data) {
		_this.abinitioGrid.refresh(data);
	});
	adapter.getAbinitioByIdsList(uniqueIds);
	return this.abinitioGrid.getPanel([]);
};
