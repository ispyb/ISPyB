/**
 * 
 * DataCollectionFrameTree a list tree with the 1D files sorted by: none, macromolecules, datacollections
 * 
 * @height
 * #onSelectionChanged when clicked on an item
 */
function DataCollectionFrameTree(args) {
	this.height = 100;
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
	}
	this.id = BUI.id();
	this.framesIdToVisualize = [];
	this.framesId = {};
	this.labelsId = {};

	/** Events **/
	this.onSelectionChanged = new Event();
}

DataCollectionFrameTree.prototype.getPanel = function() {
	return {
		id : this.id,
		title : 'Criteria',
		split : true,
		width : 250,
		minHeight : 550,
		collapsible : true,
		collapseDirection : Ext.Component.DIRECTION_LEFT,
		margins : '0 5 5 0',
		layout : 'accordion',
		height : this.height - 90,

		layoutConfig : {
			animate : true
		},
		items : [ {
			title : 'List',
			autoScroll : true,
			border : false,
			items : [ this.getListGrid() ]
		},
		{
			title : 'Tree',
			border : false,
			autoScroll : true,
			items : [ this.getFramesTree() ]
		} ]
	};
};

/** Macromolecule Grid **/
DataCollectionFrameTree.prototype.getListGrid = function() {
	var _this = this;
	this.listStore = Ext.create('Ext.data.Store', {
		fields : [ 'frameId', 'filePath' ],
		autoload : false
	});

	this.listStore.sort('filePath');
	this.listGrid = Ext.create('Ext.grid.Panel', {
		id : 'listGrid' + this.id,
		store : this.listStore,
		columns : [ {
			text : 'File',
			dataIndex : 'filePath',
			flex : 1,
			renderer : function(val) {
				return val.split("/")[val.split("/").length - 1];

			}
		} ],
		columnLines : true,
		multiSelect : true,
		flex : 1,
		border : 0,
		listeners : {
			'selectionchange' : function(model, selected, eOpts) {
				var selectedFrames = [];
				for ( var i = 0; i < selected.length; i++) {
					selectedFrames.push(selected[i].data.frameId);
				}
				_this.notifySelectionChanged(selectedFrames);
			}
		}
	});
	return this.listGrid;
};

DataCollectionFrameTree.prototype.getDataCollectionCode = function(dataCollection) {
	var code = "";
	for ( var i = 0; i < dataCollection.measurementtodatacollection3VOs.length; i++) {
		var macromolecule = this.getMacromoleculeByMeasurementId(dataCollection.measurementtodatacollection3VOs[i].measurementId);//BIOSAXS.proposal.getMacromoleculeById(specimen);
		if (macromolecule != null) {
			return macromolecule.acronym;// + ":  " + parseFloat(sample.concentration).toFixed(2) + " mg/ml";
		}
	}
	return code;
};

DataCollectionFrameTree.prototype.getCollectionNode = function(dataCollection, merges) {
	return {
		File : this.getDataCollectionCode(dataCollection),
		expanded : true,
		children : this.getMeasurements(dataCollection, merges)
	};
};

DataCollectionFrameTree.prototype.getData = function(dataCollections, merges) {
	var treeData = [];
	for ( var i = 0; i < dataCollections.length; i++) {
		dataCollection = dataCollections[i];
		treeData.push(this.getCollectionNode(dataCollection, merges));
	}
	return treeData;
};

DataCollectionFrameTree.prototype.getSpecimenByMeasurementId = function(measurementId) {
	var measurement = this.experiments[0].getMeasurementById(measurementId);
	return this.experiments[0].getSpecimenById(measurement.specimenId);
};

DataCollectionFrameTree.prototype.getMacromoleculeByMeasurementId = function(measurementId) {
	var specimen = this.getSpecimenByMeasurementId(measurementId);//this.experiments[0].getSpecimenById(measurement.specimenId);
	return BIOSAXS.proposal.getMacromoleculeById(specimen);
};

DataCollectionFrameTree.prototype.getMeasurements = function(dataCollection, merges) {
	var children = [];
	for ( var i = 0; i < dataCollection.measurementtodatacollection3VOs.length; i++) {
		var sample = this.getSpecimenByMeasurementId(dataCollection.measurementtodatacollection3VOs[i].measurementId);
		var code = "BUFFER";
		if (sample.macromolecule3VO != null) {
			code = sample.macromolecule3VO.acronym + ":  " + parseFloat(sample.concentration).toFixed(2) + " mg/ml";
		} else {
			code = BIOSAXS.proposal.getBufferById(sample.bufferId);//this.getBufferById(sample.bufferId).acronym;
		}
		var childrenFrames = this.getFrames(dataCollection.measurementtodatacollection3VOs[i].measurementId, merges);
		if (childrenFrames.length > 0) {
			/** Removing if there are no frames **/
			var childrenFiltered = [];
			for ( var j = 0; j < childrenFrames.length; j++) {
				if (childrenFrames[j].children != null) {
					if (childrenFrames[j].children.length > 0) {
						childrenFiltered.push(childrenFrames[j]);
					}
				} else {
					childrenFiltered.push(childrenFrames[j]);
				}
			}

			children.push({
				File : code,
				dataCollectionOrder : dataCollection.measurementtodatacollection3VOs[i].dataCollectionOrder,
				expanded : false,
				children : childrenFiltered,
				frameId : null
			});
		}
	}
	return children;
};

DataCollectionFrameTree.prototype.getFrames = function(measurementId, merges) {
	var data = [];
	if (merges != null) {
		for ( var i = 0; i < merges.length; i++) {
			if (merges[i].measurementId == measurementId) {
				for ( var f = 0; f < merges[i].framelist3VO.frametolist3VOs.length; f++) {
					var fileName = merges[i].framelist3VO.frametolist3VOs[f].frame3VO.filePath.split("/")[merges[i].framelist3VO.frametolist3VOs[f].frame3VO.filePath.split("/").length - 1];
					this.framesId[merges[i].framelist3VO.frametolist3VOs[f].frame3VO.frameId] = this.getSpecimenByMeasurementId(measurementId);

					this.labelsId[merges[i].framelist3VO.frametolist3VOs[f].frame3VO.frameId] = fileName;
					if (fileName.indexOf("_ave.dat") != -1) {
						data.push({
							File : fileName,
							frameId : merges[i].framelist3VO.frametolist3VOs[f].frame3VO.frameId,
							id : merges[i].framelist3VO.frametolist3VOs[f].frame3VO.frameId,
							expanded : true,
							leaf : true
						});
						this.framesIdToVisualize.push(merges[i].framelist3VO.frametolist3VOs[f].frame3VO.frameId);
					}
				}
			}
		}
		data.push({
			File : "Frames",
			children : this.getFramesNoAverage(measurementId, merges),
			expanded : false,
			leaf : false
		});

	}
	return data;
};

DataCollectionFrameTree.prototype.getFramesNoAverage = function(measurementId, merges) {
	var data = [];
	if (merges != null) {
		for ( var i = 0; i < merges.length; i++) {
			if (merges[i].measurementId == measurementId) {
				for ( var f = 0; f < merges[i].framelist3VO.frametolist3VOs.length; f++) {
					var fileName = merges[i].framelist3VO.frametolist3VOs[f].frame3VO.filePath.split("/")[merges[i].framelist3VO.frametolist3VOs[f].frame3VO.filePath.split("/").length - 1];
					this.framesId[merges[i].framelist3VO.frametolist3VOs[f].frame3VO.frameId] = this.getSpecimenByMeasurementId(measurementId);
					this.labelsId[merges[i].framelist3VO.frametolist3VOs[f].frame3VO.frameId] = fileName;

					if (fileName.indexOf("_ave.dat") == -1) {
						data.push({
							File : fileName,
							frameId : merges[i].framelist3VO.frametolist3VOs[f].frame3VO.frameId,
							id : merges[i].framelist3VO.frametolist3VOs[f].frame3VO.frameId,
							expanded : false,
							leaf : true
						});
					}
				}
			}
		}
	}
	return data;
};

DataCollectionFrameTree.prototype.getSpecimenById = function(specimenId) {
	for ( var i = 0; i < this.experiments.length; i++) {
		var specimen = this.experiments[i].getMeasurementById(specimenId);
		if (specimen != null) {
			return specimen;
		}
	}
	return null;
};

DataCollectionFrameTree.prototype.loadDatacollections = function(dataCollections, merges, experiments) {
	this.experiments = experiments;
	var macromolecules = [];
	var macromoleculesHash = [];
	for ( var i = 0; i < merges.length; i++) {
		var measurement = this.experiments[0].getMeasurementById(merges[i].measurementId);
		var specimen = this.experiments[0].getSpecimenById(measurement.specimenId);
		var macromolecule = BIOSAXS.proposal.getMacromoleculeById(specimen);
		if (macromolecule != null) {
			if (macromoleculesHash[macromolecule.acronym] == null) {
				macromolecules.push(macromolecule);
				macromoleculesHash[macromolecule.acronym] = true;
			}
		}
	}
	this.store.getRootNode().removeAll();
	this.store.getRootNode().appendChild(this.getData(dataCollections, merges));
	var frames = new ExperimentList().getFrames(merges);
	this.listStore.loadData(frames);
};

DataCollectionFrameTree.prototype.getDataCollectionById = function(dataCollectionId) {
	for ( var i = 0; i < this.experiments.length; i++) {
		var dataCollection = this.experiments[i].getDataCollectionById(dataCollectionId);
		if (dataCollection != null) {
			return dataCollection;
		}
	}
	return null;
};

DataCollectionFrameTree.prototype.notifySelectionChanged = function(frameListSelectedIds) {
	this.onSelectionChanged.notify({
		ids : frameListSelectedIds,
		framesHash : this.framesId,
		labelsHash : this.labelsId

	});
};

DataCollectionFrameTree.prototype.getFramesTree = function() {
	var _this = this;
	Ext.define('File', {
		extend : 'Ext.data.Model',
		fields : [ {
			name : 'dataCollectionOrder',
			type : 'string'
		}, {
			name : 'File',
			type : 'string'
		}, {
			name : 'frameId',
			type : 'string'
		}, {
			name : 'id',
			type : 'string'
		} ]
	});

	this.store = Ext.create('Ext.data.TreeStore', {
		model : 'File',
		root : {
			expanded : true,
			children : []
		},
		autoload : true
	});

	this.store.sort([ {
		property : 'dataCollectionOrder',
		direction : 'ASC'
	}, {
		property : 'File',
		direction : 'ASC'
	} ]);

	this.tree = Ext.create('Ext.tree.Panel', {
		height : '465',
		minHeight : '465',
		selModel : {
			mode : 'MULTI',
			allowDeselect : true
		},
		allowDeselect : true,
		rootVisible : false,
		store : this.store,
		columns : [ {
			xtype : 'treecolumn',
			text : 'Data Collections',
			flex : 2,
			sortable : true,
			dataIndex : 'File',
			renderer : function(val, y, sample) {
				if (sample.raw.frameId == null) {
					return "<span style='font-size:small;'>" + val + "</span>";
				}

				if (val.indexOf("_ave.dat") != -1) {
					return "<span style='font-weight:bold;'>" + val + "</span>";
				}
				return "<span style='font-size:x-small;'>" + val + "</span>";
			}
		} ],
		listeners : {
			'selectionchange' : function(model, selected, eOpts) {
				var frameIdsList = [];
				for ( var i = 0; i < selected.length; i++) {
					if (selected[i].data.frameId != null) {
						if (selected[i].data.frameId != null) {
							if (selected[i].data.frameId != "") {
								frameIdsList.push(selected[i].data.frameId);
							}
						}
					}
				}

				if (frameIdsList.length > 0) {
					_this.notifySelectionChanged(frameIdsList);
				}
			}
		}
	});

	return this.tree;
};

DataCollectionFrameTree.prototype.input = function() {
	return DATADOC.getFrameTreeData();
};

DataCollectionFrameTree.prototype.test = function(targetId) {
	var dataCollectionFrameTree = new DataCollectionFrameTree({
		height : this.height
	});
	BIOSAXS.proposal = new Proposal(dataCollectionFrameTree.input().proposal);
	var panel = dataCollectionFrameTree.getPanel();
	dataCollectionFrameTree.loadDatacollections(dataCollectionFrameTree.input().dataCollections, dataCollectionFrameTree.input().merges, [ new Experiment(dataCollectionFrameTree.input().experiment) ]);
	Ext.create('Ext.panel.Panel', {
		width : 1000,
		items : [ panel ],
		renderTo : targetId
	});
};
