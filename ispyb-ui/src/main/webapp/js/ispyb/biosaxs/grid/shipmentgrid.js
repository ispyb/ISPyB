/**
 * shows shipments
 * 
 * @height
 * @width
 * @minHeight
 * @btnEditVisible
 */
function ShipmentGrid(args) {
	this.id = BUI.id();
	this.height = 100;
	this.width = null;
	this.minHeight = null;
	this.btnEditVisible = true;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.minHeight != null) {
			this.minHeight = args.minHeight;
		}
		if (args.btnEditVisible != null) {
			this.btnEditVisible = args.btnEditVisible;
		}
	}
}

ShipmentGrid.prototype._getColumns = function() {
	var _this = this;
	var columns = [
		{
			text : 'Name',
			dataIndex : 'shippingName',
			flex : 1
		},
		{
			header : 'Type',
			dataIndex : 'shippingType',
			flex : 1,
			hidden : true,
			renderer : function(val, comp, record) {
				if (val != null) {
					return val.toUpperCase();
				}

			}
		},
		{
			header : 'Status',
			type : 'string',
			flex : 1,
			hidden : false,
			renderer : function(comp, val, record) {
				if (record.raw.shippingStatus != null) {
					if (new String(record.raw.shippingStatus).toUpperCase() == 'OPENED') {
						return "<span style='color:green;font-weight:bold;'>" + new String(record.raw.shippingStatus).toUpperCase() + "</span>";
					}
					return "<span style='font-weight:bold;'>" + new String(record.raw.shippingStatus).toUpperCase() + "</span>";
				}
			}
		},
		{
			text : 'Cases',
			flex : 1,
			hidden : false,
			renderer : function(comp, val, record) {
				var shipment = BIOSAXS.proposal.getShipmentById(record.data.shippingId);
				var container = "<table><tr>";
				if (shipment.dewarVOs.length > 0) {
					container = container + "<td><span style='font-size:18px'>" + shipment.dewarVOs.length + "</span>x <img src='../images/box-icon-very-small.png'></td>";
				} else {
					return "<span style='font-size:10px;'>Empty</span>";
				}
				return container + "</tr></table>";
			}
		}, {
			header : 'Comments',
			dataIndex : 'comments',
			flex : 1,
			hidden : false
		}, {
			header : 'Creation Date',
			dataIndex : 'creationDate',
			hidden : true
		} ];

	if (this.btnEditVisible) {
		columns.push({
			id : _this.id + 'buttonEdit',
			text : '',
			hidden : !_this.btnEditVisible,
			width : 100,
			sortable : false,
			renderer : function(value, metaData, record, rowIndex, colIndex, store) {
				return BUI.getGreenButton('EDIT');
			}
		});
	}

	columns.push({
		id : _this.id + 'buttonRemove',
		text : '',
		width : 100,
		sortable : false,
		renderer : function(value, metaData, record, rowIndex, colIndex, store) {
			var shipment = BIOSAXS.proposal.getShipmentById(record.data.shippingId);
			if (shipment.dewarVOs.length == 0) {
				return BUI.getRedButton('REMOVE');
			}

		}
	});

	return columns;
};

ShipmentGrid.prototype._getTopButtons = function() {
	var _this = this;
	/** Actions buttons **/
	var actions = [];
	actions.push(Ext.create('Ext.Action', {
		icon : '../images/add.png',
		text : 'Add Shipment',
		handler : function(widget, event) {
			//window.location = BUI.getCreateShipmentURL();
			var _this = this;

			var shipmentForm = new ShipmentForm({
				creationMode : true,
				showTitle : false
			});
			shipmentForm.onSaved.attach(function(sender, shipment) {
				_this.showShipmentTabs(shipment, targetId);
			});

			var window = Ext.create('Ext.window.Window', {
				title : 'New Shipment',
				height : 600,
				width : 800,
				layout : 'fit',
				items : [ shipmentForm.getPanel() ]
			}).show();

		}
	}));
	return actions;
};

ShipmentGrid.prototype.refresh = function(shippings) {
	this.features = shippings;
	this.store.loadData(this._prepareData(), false);
};

ShipmentGrid.prototype._prepareData = function() {
	var data = [];
	for ( var i = 0; i < this.features.length; i++) {
		data.push(this.features[i]);
	}
	return data;
};

ShipmentGrid.prototype.getPanel = function(shipments) {
	this.features = shipments;
	return this._renderGrid();
};

ShipmentGrid.prototype.edit = function(shippingId) {
	window.location = BUI.getShippingURL(shippingId);
};

ShipmentGrid.prototype._getStoreFields = function(data) {
	var _this = this;
	return [ {
		name : 'shippingId'
	}, {
		name : 'shippingName'
	}, {
		name : 'shippingStatus'
	}, {
		name : 'shippingType'
	}, {
		name : 'creationDate'
	}, {
		name : 'comments'
	} ];
};

ShipmentGrid.prototype._renderGrid = function() {
	var _this = this;

	/** Store **/
	var data = this._prepareData();
	this.store = Ext.create('Ext.data.Store', {
		fields : this._getStoreFields(data),
		autoload : true,
		data : data
	});

	this.store.loadData(data, false);

	this.grid = Ext.create('Ext.grid.Panel', {
		title : "Shipping",
		icon : '/ispyb/images/plane.gif',
		width : this.width,
		minWidth : this.minWidth,
		height : this.height,
		store : this.store,
		columns : this._getColumns(),
		viewConfig : {
			stripeRows : true,
			listeners : {
				itemdblclick : function(dataview, record, item, e) {
					_this.edit(record.raw.shippingId);
				},
				cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonEdit') {
						_this.edit(record.data.shippingId);
					}

					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonRemove') {
						var shipment = BIOSAXS.proposal.getShipmentById(record.data.shippingId);
						if (shipment.dewarVOs.length == 0) {

							var adapter = new BiosaxsDataAdapter();
							_this.grid.setLoading("ISPyB: Removing shipment");
							adapter.onSuccess.attach(function(sender) {
								BIOSAXS.proposal.onInitialized.attach(function(sender) {
									_this.refresh(BIOSAXS.proposal.getShipments());
									_this.grid.setLoading(false);
								});
								BIOSAXS.proposal.init();
							});

							adapter.onError.attach(function(sender) {
								alert("Error");
								_this.grid.setLoading(false);
							});
							adapter.removeShipment(record.data.shippingId);
						}
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

ShipmentGrid.prototype.input = function() {
	return {
		proposal : new MeasurementGrid().input().proposal,
		shippings : DATADOC.getShippings_10()
	};
};

ShipmentGrid.prototype.test = function(targetId) {
	var shipmentGrid = new ShipmentGrid({
		minHeight : 300,
		height : 440
	});
	BIOSAXS.proposal = new Proposal(shipmentGrid.input().proposal);
	var panel = shipmentGrid.getPanel(targetId);
	panel.render(targetId);
	shipmentGrid.refresh(shipmentGrid.input().shippings);
};
