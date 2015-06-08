
function FitStructureToExperimentDataGrid(args) {
	this.height = null;
	this.width = null;
	this.id = BUI.id();

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;

			if (args.width != null) {
				this.width = args.width;
			}
		}
	}
	
	this.onSelected = new Event(this);
}

FitStructureToExperimentDataGrid.prototype.getStructuresByMacromolecule = function(macromolecule) {
	var structures = [];
	if (macromolecule.structure3VOs != null) {
		if (macromolecule.structure3VOs.length > 0) {
			for (var i = 0; i < macromolecule.structure3VOs.length; i++) {
				structures.push(macromolecule.structure3VOs[i]);
			}
		}
	}
	return structures;
};

FitStructureToExperimentDataGrid.prototype._prepareData = function(subtractions) {
	var data = [];
	for (var i = 0; i < subtractions.length; i++) {
		
		for (var j = 0; j < subtractions[i].fitStructureToExperimentalData3VOs.length; j++) {
			var fit = subtractions[i].fitStructureToExperimentalData3VOs[j];
			data.push({
				fit : fit.fitFilePath,
				fitStructureToExperimentalDataId : fit.fitStructureToExperimentalDataId,
				mixtureToStructure3VOs : fit.mixtureToStructure3VOs,
				subtractedFile : subtractions[i].substractedFilePath
			});
		}
	}
	return data;

};

FitStructureToExperimentDataGrid.prototype.refresh = function(subtractions) {
	this.store.loadData(this._prepareData(subtractions), false);
};

FitStructureToExperimentDataGrid.prototype.getPanel = function() {
	var _this = this;
	this.store = Ext.create('Ext.data.Store', {
		fields : [ 'fit', 'subtractedFile', 'structureId', 'fitFilePath', 'mixtureToStructure3VOs' ],
		data : []
	});

	this.store.sort([ {
		property : 'name',
		direction : 'ASC'
	} ]);

	this.selModel = Ext.create('Ext.selection.RowModel', {
		allowDeselect : true,
		mode : 'MULTI',
		listeners : {
			selectionchange : function(sm, selections) {
				var selected = [];
				for (var i = 0; i < selections.length; i++) {
					selected.push(selections[i].raw);
				}
				_this.onSelected.notify(selected);
			}
		}
	});
	
	this.panel = Ext.create('Ext.grid.Panel', {
		store : this.store,
		deferredRender : false,
		width : this.width,
		height : this.height,
		margin : 10,
		selModel : this.selModel,
		columns : [ {
			text : 'Name',
			dataIndex : 'fit',
			flex : 1,
			renderer : function(val, b, record) {
				return BUI.getFileName(val);
			}
		}, {
			text : 'PDB',
			dataIndex : 'mixtureToStructure3VOs',
			flex : 1,
			renderer : function(values, b, record) {
				var html = "<table>";
				for (var i = 0; i < values.length; i++) {
					var structure = BIOSAXS.proposal.getStructuresById(values[i].structureId);
					html = html + "<tr>";
					if (structure != null){
						html = html + "<td style='width:200px'>" + structure.name + "</td>";
					}
					html = html + "</tr>";
				}
				return html + "</table>";
			}
		}, {
			text : 'Volume Fraction',
			dataIndex : 'mixtureToStructure3VOs',
			flex : 1,
			renderer : function(values, b, record) {
				var html = "<table>";
				for (var i = 0; i < values.length; i++) {
					html = html + "<tr>";
					html = html + "<td>" + values[i].volumeFraction + "</td>";
					html = html + "</tr>";
				}

				return html + "</table>";
			}
		},{
			text : 'Subtraction',
			dataIndex : 'subtractedFile',
			flex : 1,
			renderer : function(values, b, record) {
				return values.split("/")[values.split("/").length - 1];
			}
		},],

		viewConfig : {
			enableTextSelection : true,
			preserveScrollOnRefresh : true
		},
		listeners : {
			cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
			},
			afterrender : function() {
			}
		}
	});
	return this.panel;
};

/** Static method **/
FitStructureToExperimentDataGrid.prototype.RUNFitScattering = function(subtractionId, structureId) {
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
			_this.refresh(_this.subtractionId, _this.macromolecule);
		});
		adapter2.addFitStructureData(fit);

	});
	adapter.addWorkflow(workflow, inputs);

};
