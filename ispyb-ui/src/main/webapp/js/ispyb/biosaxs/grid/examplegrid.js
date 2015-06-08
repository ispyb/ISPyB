/**
 * A shipment may contains one or more cases where stock solutions and sample plates are stored
 * 
 * @height
 * @btnEditVisible
 * @btnRemoveVisible
 * 
 * #onEditButtonClicked
 * #onAddButtonClicked
 * #onRemoveButtonClicked
 * #onDuplicateButtonClicked
 */
function ExampleGrid(args) {

	this.height = 100;
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
	this.onEditButtonClicked = new Event(this);
	this.onAddButtonClicked = new Event(this);
	this.onRemoveButtonClicked = new Event(this);
	this.onDuplicateButtonClicked = new Event(this);
}

ExampleGrid.prototype._getColumns = function() {
	var _this = this;

	function actionItemRenderer(value, meta, record, rowIx, ColIx, store) {
		/*if (record.raw.specimen3VOs.length > 0){
			return 'x-hide-display';
		}*/
	}

	var columns = [
		{
			header : 'Code',
			dataIndex : 'code',
			name : 'code',
			type : 'string',
			flex : 1
		},
		{
			header : 'Bar Code',
			dataIndex : 'barCode',
			name : 'barCode',
			type : 'string',
			flex : 1,
			hidden : true
		},
		{
			header : 'BeamLine',
			dataIndex : 'barCode',
			type : 'string',
			flex : 1,
			renderer : function(comp, val, record) {
				if (record.raw.sessionVO != null) {
					return "<span style='font-weight:bold;'>" + record.raw.sessionVO.beamlineName + "</span>";
				}
			}
		},
		{
			header : 'Session',
			dataIndex : 'barCode',
			type : 'string',
			flex : 1,
			renderer : function(comp, val, record) {
				if (record.raw.sessionVO != null) {
					return "<span>" + moment(record.raw.sessionVO.startDate).format("MMM Do YY") + "</span>";
				}
			}
		},
		{
			header : 'Status',
			dataIndex : 'dewarStatus',
			name : 'dewarStatus',
			type : 'string',
			flex : 1,
			renderer : function(comp, val, record) {
				if (new String(record.raw.dewarStatus).toUpperCase() == 'OPENED') {
					return "<span style='color:green;font-weight:bold;'>" + new String(record.raw.dewarStatus).toUpperCase() + "</span>";
				}
				return "<span style='font-weight:bold;'>" + new String(record.raw.dewarStatus).toUpperCase() + "</span>";
			}
		},
		{
			header : 'Type',
			dataIndex : 'type',
			name : 'type',
			type : 'string',
			flex : 1,
			hidden : true
		},
		{
			header : 'Sample Plates',
			dataIndex : 'plates',
			name : 'plates',
			type : 'string',
			flex : 1,
			hidden : true
		},
		{
			header : 'Stock Solutions',
			dataIndex : 'plates',
			name : 'plates',
			type : 'string',
			width : 100,
			renderer : function(comp, val, record) {
				var html = "<div>";
				var stockSolutions = BIOSAXS.proposal.getStockSolutionsByDewarId(record.raw.dewarId);
				html = html + "<span style='font-size:18px'>" + stockSolutions.length + "</span> x<img height='15px' src='/ispyb/images/SampleHolder_24x24_01.png'>";
				return html + "</div>";
			}
		}, {
			header : 'isStorageDewar',
			dataIndex : 'isStorageDewar',
			name : 'isStorageDewar',
			type : 'string',
			flex : 1,
			hidden : true
		}, {
			header : 'Tracking Number From Synchrotron',
			dataIndex : 'trackingNumberFromSynchrotron',
			name : 'trackingNumberFromSynchrotron',
			type : 'string',
			flex : 1,
			hidden : true
		}, {
			header : 'Tracking Number To Synchrotron',
			dataIndex : 'trackingNumberToSynchrotron',
			name : 'trackingNumberToSynchrotron',
			type : 'string',
			flex : 1,
			hidden : true
		}, {
			header : 'Storage Location',
			dataIndex : 'storageLocation',
			name : 'storageLocation',
			type : 'string',
			flex : 1,
			hidden : false
		}, {
			header : 'Comments',
			dataIndex : 'comments',
			name : 'comments',
			type : 'string',
			flex : 1,
			hidden : false
		}
	];

	if (this.btnEditVisible) {
		columns.push({
			id : _this.id + 'buttonEdit',
			text : '',
			hidden : !_this.btnEditVisible,
			width : 85,
			sortable : false,
			renderer : function(value, metaData, record, rowIndex, colIndex, store) {
				return BUI.getGreenButton('EDIT');
			}
		});
	}

	if (this.btnRemoveVisible) {
		columns.push({
			id : _this.id + 'buttonRemove',
			width : 85,
			sortable : false,
			renderer : function(value, metaData, record, rowIndex, colIndex, store) {
				if (_this.btnRemoveVisible) {
					return BUI.getRedButton('REMOVE');
				}
			}
		});
	}

	columns.push({
		dataIndex : 'comments',
		type : 'string',
		width : 85,
		hidden : false,
		renderer : function(comp, val, record) {
			return BUI.getBlueButton("LABELS", {
				href : BUI.getPrintcomponentURL(record.raw.dewarId)
			});
		}
	});

	return columns;
};

ExampleGrid.prototype._getTopButtons = function() {
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

ExampleGrid.prototype.refresh = function(dewars) {
	this.features = dewars;
	this.store.loadData(this._prepareData(dewars), false);
};

ExampleGrid.prototype._sort = function(store) {
	store.sort('dewarId', 'DESC');
};

ExampleGrid.prototype._prepareData = function() {
	var data = [];
	for ( var i = 0; i < this.features.length; i++) {
		data.push(this.features[i]);
	}
	return data;
};

ExampleGrid.prototype.getPanel = function() {
	this.grid = Ext.create('Ext.grid.Panel', {
		style : {
			padding : 5
		},
		height : this.height,
		store : this.store,
		columns : this._getColumns(),
		viewConfig : {
			stripeRows : true,
			listeners : {
				itemdblclick : function(dataview, record, item, e) {
					_this._edit(record.raw);
				},
				cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonEdit') {
						_this._edit(record.raw);
					}

					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonRemove') {
						_this.onRemoveButtonClicked.notify(_this.store.getAt(rowIndex).raw.dewarId);
					}
				}
			}
		},
		selModel : {
			mode : 'SINGLE'
		}
	});

	var actions = _this._getTopButtons();
	this.grid.addDocked({
		xtype : 'toolbar',
		items : actions
	});

	this.grid.getSelectionModel().on({
		selectionchange : function(sm, selections) {
			if (selections.length) {
				for ( var i = 0; i < actions.length; i++) {
					if (actions[i].enable) {
						actions[i].enable();
					}
				}
			} else {
				for ( var i = 0; i < actions.length; i++) {
					if (actions[i].alwaysEnabled == false) {
						if (actions[i].disable) {
							actions[i].disable();
						}
					}
				}
			}
		}
	});
	return this.grid;
};


ExampleGrid.prototype._getStoreFields = function(data) {
	return [ {
		name : 'dewarId',
		type : 'string'
	}, {
		name : 'barCode',
		type : 'string'
	}, {
		name : 'code',
		type : 'string'
	}, {
		name : 'comments',
		type : 'string'
	}, {
		name : 'dewarStatus',
		type : 'string'
	}, {
		name : 'isStorageDewar',
		type : 'string'
	}, {
		name : 'plates',
		type : 'string'
	}, {
		name : 'transportValue',
		type : 'string'
	}, {
		name : 'trackingNumberFromSynchrotron',
		type : 'string'
	}, {
		name : 'trackingNumberToSynchrotron',
		type : 'string'
	}, {
		name : 'timeStamp',
		type : 'string'
	}, {
		name : 'storageLocation',
		type : 'string'
	}, {
		name : 'type',
		type : 'string'
	}

	];
};

ExampleGrid.prototype._renderGrid = function() {
	var _this = this;

	/** Store **/
	var data = this._prepareData();
	this.store = Ext.create('Ext.data.Store', {
		fields : this._getStoreFields(data),
		autoload : true,
		data : data
	});
	this._sort(this.store);

	this.store.loadData(data, false);

	this.grid = Ext.create('Ext.grid.Panel', {
		style : {
			padding : 5
		},
		height : this.height,
		store : this.store,
		columns : this._getColumns(),
		viewConfig : {
			stripeRows : true,
			listeners : {
				itemdblclick : function(dataview, record, item, e) {
					_this._edit(record.raw);
				},
				cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonEdit') {
						_this._edit(record.raw);
					}

					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonRemove') {
						_this.onRemoveButtonClicked.notify(_this.store.getAt(rowIndex).raw.dewarId);
					}
				}
			}
		},
		selModel : {
			mode : 'SINGLE'
		}
	});

	var actions = _this._getTopButtons();
	this.grid.addDocked({
		xtype : 'toolbar',
		items : actions
	});

	this.grid.getSelectionModel().on({
		selectionchange : function(sm, selections) {
			if (selections.length) {
				for ( var i = 0; i < actions.length; i++) {
					if (actions[i].enable) {
						actions[i].enable();
					}
				}
			} else {
				for ( var i = 0; i < actions.length; i++) {
					if (actions[i].alwaysEnabled == false) {
						if (actions[i].disable) {
							actions[i].disable();
						}
					}
				}
			}
		}
	});
	return this.grid;
};

ExampleGrid.prototype.input = function() {
	return {
		proposal : DATADOC.getProposal_10(),
		dewars : DATADOC.getDewars_10()

	};
};

ExampleGrid.prototype.test = function(targetId) {
	var ExampleGrid = new ExampleGrid({
		height : 150
	});
	BIOSAXS.proposal = new Proposal(ExampleGrid.input().proposal);
	var panel = ExampleGrid.getPanel(ExampleGrid.input().dewars);
	panel.render(targetId);

};
