function ShippingWidget(args) {
	this.id = BUI.id();
	this.height = 800;
	this.width = 900;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	var _this = this;

	this.stockSolutionGrid = new StockSolutionGrid({
		width : this.width,
		minHeight : 300,
		height : 350,
		tbar : true
	});

	this.stockSolutionGrid.onProposalChanged.attach(function(sender) {
		_this.refresh();
	});

	this.shipmentGrid = new ShipmentGrid({
		width : this.width,
		minHeight : 300,
		height : 440
	});
}

ShippingWidget.prototype.draw = function(targetId) {
	this.renderPanel(targetId);
	this.refresh();
};

ShippingWidget.prototype.renderPanel = function(targetId) {
	this.panel = Ext.create('Ext.panel.Panel', {
		plain : true,
		frame : false,
		border : 0,
		height : this.height,
		width : this.width,
		renderTo : targetId,
		items : [ {
			xtype : 'container',
			layout : 'hbox',
			margin : "0, 0, 0, 0",
			items : [ {
				xtype : 'container',
				layout : 'vbox',
				margin : "0, 0, 0, 0",
				items : [ this.stockSolutionGrid.getPanel() ]
			}
			]
		}, {
			xtype : 'container',
			layout : 'hbox',
			margin : "10, 0, 0, 0",
			items : [ this.shipmentGrid.getPanel([]) ]
		}

		]
	});
};

ShippingWidget.prototype.refresh = function() {
	var _this = this;
	BIOSAXS.proposal.onInitialized = new Event();
	BIOSAXS.proposal.onInitialized.attach(function(sender) {
		_this.stockSolutionGrid.refresh(BIOSAXS.proposal.getStockSolutions());
		_this.shipmentGrid.refresh(BIOSAXS.proposal.getShipments());
		_this.stockSolutionGrid.grid.setLoading(false);
		_this.shipmentGrid.grid.setLoading(false);
	});

	this.stockSolutionGrid.grid.setLoading("ISPyB: Retrieving Stock Solutions");
	this.shipmentGrid.grid.setLoading("ISPyB: Retrieving shipments");
	BIOSAXS.proposal.init();

};
