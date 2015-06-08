

/**
 * Rigid body grid to show PDB, symmetry and multiplicity
 * 
 * 
 * #onUploadFile click on upload file
 */
function AprioriRigidBodyGrid(args) {

	this.height = 250;
	this.btnEditVisible = true;
	this.btnRemoveVisible = true;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.btnEditVisible != null) {
			this.btnEditVisible = args.btnEditVisible;
		}
		if (args.btnRemoveVisible != null) {
			this.btnRemoveVisible = args.btnRemoveVisible;
		}
	}

	/** Events **/
	this.onUploadFile = new Event(this);
	this.onRemove = new Event(this);
}

AprioriRigidBodyGrid.prototype._getColumns = function() {
};

AprioriRigidBodyGrid.prototype._getTopButtons = function() {
	var _this = this;
	/** Actions buttons **/
	var actions = [];

	/** ADD BUTTON **/
	actions.push(Ext.create('Ext.Action', {
		icon : '../images/add.png',
		text : 'Add',
		disabled : false,
		handler : function(widget, event) {
			_this.onAddButtonClicked.notify();
		}
	}));

	return actions;
};

AprioriRigidBodyGrid.prototype.refresh = function(macromolecule) {
	this.macromolecule = macromolecule;
	if (macromolecule != null){
		this.pdbStore.loadData(macromolecule.structure3VOs);
	}
};

AprioriRigidBodyGrid.prototype._prepareData = function() {
	var data = [];
	for ( var i = 0; i < this.features.length; i++) {
		data.push(this.features[i]);
	}
	return data;
};

AprioriRigidBodyGrid.prototype._getPlugins = function() {
	var _this = this;
	var plugins = [];
	plugins.push(Ext.create('Ext.grid.plugin.RowEditing', {
		clicksToEdit : 1,
		listeners : {
			validateedit : function(grid, e) {
				/** Comments are always updatable* */
				e.record.raw.symmetry = e.newValues.symmetry;
				e.record.raw.multiplicity = e.newValues.multiplicity;

				var adapter = new BiosaxsDataAdapter();
				adapter.onSuccess.attach(function(sender, proposal) {
					BIOSAXS.proposal.setItems(proposal);
					_this.refresh(BIOSAXS.proposal.getMacromoleculeById(_this.macromolecule.macromoleculeId));
					_this.panel.setLoading(false);
				});
				adapter.onError.attach(function() {
					alert("Error");
				});

				_this.panel.setLoading();
				adapter.saveStructure(e.record.raw);
			}
		}
	}));
	return plugins;
};

AprioriRigidBodyGrid.prototype.getPanel = function() {
	var _this = this;

	this.pdbStore = Ext.create('Ext.data.Store', {
		fields : [ 'filePath', 'structureId', 'structureType', 'symmetry', 'structureId', 'name', 'multiplicity' ],
		groupField : 'structureType',
		sorters : {
			property : 'structureId',
			direction : 'DESC'
		}
	});

	var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
		groupHeaderTpl : Ext.create('Ext.XTemplate',
				"<div style='background:#0ca3d2; color:white; float:left; font-size:10px; margin:6px 8px 0 0; padding:5px 8px;'>{name:this.formatName}</div>", {
					formatName : function(name) {
						return name;
					}
				}),
		hideGroupedHeader : true,
		startCollapsed : false
	});

	this.panel = Ext.create('Ext.grid.Panel', {
		margin : "15 10 0 10",
		height : this.height,
		store : this.pdbStore,
		plugins : _this._getPlugins(),
		tbar : [ {
			text : 'Add Modeling Option (PDB)',
			icon : '../images/add.png',
			handler : function() {
				_this.onUploadFile.notify('PDB', 'Upload PDB File');
			}
		}

		],
		columns : [
				{
					text : "structureId",
					flex : 0.2,
					hidden : true,
					dataIndex : 'structureId',
					sortable : true
				},
				{
					text : "File",
					flex : 0.5,
					dataIndex : 'filePath',
					sortable : true,
					hidden : true
				},
				{
					text : "PDB",
					flex : 0.4,
					dataIndex : 'name',
					sortable : true
				},
				{
					text : "Symmetry",
					flex : 0.2,
					dataIndex : 'symmetry',
					sortable : true,
					editor : {
						xtype : 'combobox',
						typeAhead : true,
						triggerAction : 'all',
						selectOnTab : true,
						store : [ [ "P1", "P1" ], [ "P2", "P2" ], [ "P3", "P3" ], [ "P4", "P4" ], [ "P5", "P5" ], [ "P6", "P6" ], [ "P32", "P32" ], [ "P42", "P42" ],
								[ "P52", "P52" ], [ "P62", "P62" ], [ "P222", "P222" ] ],
					}
				}, {
					text : "Multiplicity",
					flex : 0.2,
					dataIndex : 'multiplicity',
					sortable : true,
					editor : {
						xtype : 'textfield'
					}

				}, {
					text : "Subunit",
					flex : 0.2,
					dataIndex : 'isSubunit',
					sortable : true,
					hidden : true
				}, {
					text : "Type",
					flex : 0.2,
					dataIndex : 'structureType',
					sortable : true,
					hidden : true
				},

				{
					id : this.id + 'REMOVE',
					flex : 0.2,
					sortable : false,
					renderer : function(value, metaData, record, rowIndex, colIndex, store) {
						return BUI.getRedButton('REMOVE');
					}
				}, ],

		listeners : {
			itemdblclick : function(dataview, record, item, e) {
				_this._editExperiment(record.raw.experimentId);
			},
			cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {

				if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'REMOVE') {
					var dataAdapter = new BiosaxsDataAdapter();
					dataAdapter.onSuccess.attach(function() {
						_this.panel.setLoading(false);
						_this.onRemove.notify();
					});
					_this.panel.setLoading("Removing PDB file");
					dataAdapter.removeStructure(record.data.structureId);
				}

			}
		},
		viewConfig : {
			getRowClass : function(record, rowIdx, params, store) {
				if (record.raw.isSubunit != null) {
					return "blue-row";
				}
			}
		}
	});

	return this.panel;
};




AprioriRigidBodyGrid.prototype.input = function() {
	return {
		proposal : DATADOC.getProposal_10(),
		dewars : DATADOC.getDewars_10()

	};
};

AprioriRigidBodyGrid.prototype.test = function(targetId) {
	var AprioriRigidBodyGrid = new AprioriRigidBodyGrid({
		height : 150
	});
	BIOSAXS.proposal = new Proposal(AprioriRigidBodyGrid.input().proposal);
	var panel = AprioriRigidBodyGrid.getPanel(AprioriRigidBodyGrid.input().dewars);
	panel.render(targetId);

};
