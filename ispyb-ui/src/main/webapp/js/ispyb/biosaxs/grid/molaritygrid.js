/**
 * A shipment may contains one or more cases where stock solutions and sample
 * plates are stored
 * 
 * @height
 * @btnEditVisible
 * @btnRemoveVisible
 * 
 * #onEditButtonClicked
 */
function MolarityGrid(args) {
	this.height = 100;
	this.width = 100;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.width != null) {
			this.width = args.width;
		}
	}

	var _this = this;
	
	this.molarityForm = new MolarityForm({height : 180, width : 455});

	this.molarityForm.onSave.attach(function(sender){
		_this.molarityWindow.destroy();
		_this.updateProposal();
		
	});
	
	this.molarityForm.onClose.attach(function(sender){
		_this.molarityWindow.destroy();
		
	});
	
	/** Events * */
	this.onEditButtonClicked = new Event(this);
}

MolarityGrid.prototype._getColumns = function() {
	return [ {
		text : 'Subunit',
		columns : [ {
			text : "Acronym",
			width : 100,
			hidden : false,
			dataIndex : 'acronym',
			sortable : true
		}, {
			text : "Name",
			width : 125,
			hidden : false,
			dataIndex : 'name',
			sortable : true
		}, {
			text : "MM Est.",
			width : 100,
			dataIndex : 'molecularMass',
			sortable : true,
			renderer : function(grid, cls, record){
				return BUI.formatValuesUnits(record.data.molecularMass , "Da", 10, 2); 
				
			}
		} ]
	}, {
//		text : "Number <br/><span style='font-size:10px'>in assymmetric units</span>",
		text : "Ratio",
		width : 100,
		dataIndex : 'ratio',
		tooltip : 'Number of times the subunit is present in the macromolecule',
		sortable : true
	}, {
		id : this.id + 'MOLARITY_REMOVE',
		flex : 0.1,
		sortable : false,
		renderer : function(value, metaData, record, rowIndex, colIndex, store) {
			return BUI.getRedButton('REMOVE');
		}
	} ];
};

MolarityGrid.prototype._openMolarityWindow = function() {
	this.molarityWindow = Ext.create('Ext.window.Window', {
		title : 'Molarity',
		height : 220,
		width : 500,
		modal : true,
		items : [this.molarityForm.getPanel() ]
	}).show();
};

MolarityGrid.prototype._getButtons = function() {
	var _this = this;
	return [ {
		text : 'Add subunit',
		icon : '../images/add.png',
		handler : function() {
			_this._openMolarityWindow();
		}
    }];
};


MolarityGrid.prototype.updateProposal = function() {
	var _this = this;
	this.panel.setLoading();
	BIOSAXS.proposal.onInitialized.attach(function() {
		if (BIOSAXS.proposal != null) {
			var macromolecules = BIOSAXS.proposal.macromolecules;
			for (var i = 0; i < macromolecules.length; i++) {
				
				if (macromolecules[i].macromoleculeId == _this.macromolecule.macromoleculeId) {
					_this.refresh(macromolecules[i]);
					_this.panel.setLoading(false);
				}
			}
		}
	});
	BIOSAXS.proposal.init();
};


MolarityGrid.prototype.getPanel = function() {
	var _this = this;

	this.molarityStore = Ext.create('Ext.data.Store', {
		fields : [ 'acronym', 'ratio', 'comments', 'stoichiometryId', 'name', 'molecularMass' ],
		sorters : {
			property : 'ratio',
			direction : 'DESC'
		}
	});

	this.panel = Ext.create('Ext.grid.Panel', {
		store : this.molarityStore,
		height : this.height,
		padding : 5,
		columns : this._getColumns(),
		listeners : {
			cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {

				/** Remove entry * */
				if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'MOLARITY_REMOVE') {
					var dataAdapter = new BiosaxsDataAdapter();
					dataAdapter.onSuccess.attach(function(sender) {
						_this.updateProposal();
						
					});
					dataAdapter.removeStoichiometry(record.data.stoichiometryId);
					_this.panel.setLoading("Removing Structure");
				}
			}
		},
		tbar : this._getButtons()
	});
	return this.panel;
};

MolarityGrid.prototype._prepareData = function(macromolecule) {
	/** Return an array of [{ratio,acronym, stoichiometryId, name}] **/
		var data = [];
		if (macromolecule.stoichiometry != null) {
			for (var i = 0; i < macromolecule.stoichiometry.length; i++) {
				var hostMacromolecule = BIOSAXS.proposal.getMacromoleculeById(macromolecule.stoichiometry[i].macromoleculeId);
				data.push({
					ratio : macromolecule.stoichiometry[i].ratio,
					acronym : hostMacromolecule.acronym,
					comments : hostMacromolecule.comments,
					molecularMass : hostMacromolecule.molecularMass,
					stoichiometryId : macromolecule.stoichiometry[i].stoichiometryId,
					name : hostMacromolecule.name
				});
			}
		}
		return data;
};

MolarityGrid.prototype.refresh = function(macromolecule) {
	if (macromolecule != null){
		this.molarityStore.loadData(this._prepareData(macromolecule));
		this.molarityForm.refresh(macromolecule);
		this.macromolecule = macromolecule;
	}
};

MolarityGrid.prototype.input = function() {
	return {
		proposal : DATADOC.getProposal_10(),
		dewars : DATADOC.getDewars_10()

	};
};

MolarityGrid.prototype.test = function(targetId) {
	var MolarityGrid = new MolarityGrid({
		height : 150
	});
	BIOSAXS.proposal = new Proposal(MolarityGrid.input().proposal);
	var panel = MolarityGrid.getPanel(MolarityGrid.input().dewars);
	panel.render(targetId);

};
