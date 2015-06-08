
function ShipmentTabs(targetId) {
	this.targetId = targetId;

	var _this = this;
	this.gridHeight = 500;
	/** data **/
	this.shipment = null;

	/** Shipment Form **/
	this.shipmentForm = new ShipmentForm({
		creationMode : false,
		showTitle : false
	});
	this.shipmentForm.onSaved.attach(function(sender, data) {
		_this.refresh(data);

	});


	/** Cases grid **/
	this.caseGrid = new CaseGrid({
		height : this.gridHeight
	});

	this.caseGrid.onAddButtonClicked.attach(function(sender, dewar) {
		_this.caseGrid.grid.setLoading("ISPyB: Creating a new case");
		var adapter = new BiosaxsDataAdapter();
		adapter.onSuccess.attach(function(sender, shipment) {
			/** updateing shipment on proposal **/
			for ( var i = 0; i < BIOSAXS.proposal.shippings.length; i++) {
				if (BIOSAXS.proposal.shippings[i].shippingId == shipment.shippingId) {
					BIOSAXS.proposal.shippings[i] = shipment;
				}
			}
			_this.refresh(shipment);
		});
		adapter.onError.attach(function(sender, shipment) {
			_this.caseGrid.grid.setLoading(false);
		});
		adapter.addCase(_this.shipment.json.shippingId);
	});

	this.caseGrid.onRemoveButtonClicked.attach(function(sender, dewarId) {
		_this.panel.setLoading("ISPyB: removing case");
		_this.shipment.onSaved.attach(function(sender, shipment) {
			_this.refresh(shipment);

		});
		_this.shipment.removeCase(dewarId);
	});
}

ShipmentTabs.prototype.refresh = function(shipment) {

	var _this = this;
	this.shipment = shipment;
	var proposal = new Proposal();
	proposal.onDataRetrieved.attach(function(sender, plates) {

		_this.refreshWithPlates(shipment, plates);
		_this.caseGrid.grid.setLoading(false);
	});
	proposal.getPlatesByProposal();
};

ShipmentTabs.prototype.refreshWithPlates = function(shipment, plates) {
	this.shipment = new Shipment(shipment);

	this.caseGrid.refresh(this.shipment.getDewars(), plates);
	this.panel.setLoading(false);
};

ShipmentTabs.prototype.error = function(error) {
	var e = JSON.parse(error);
	showError(e);
	this.panel.setLoading(false);
};

//ShipmentTabs.prototype.refreshTabTitles = function() {
/*Ext.getCmp("MacromoleculeTab").setText(this.getMacromoleculeTitle());
Ext.getCmp("BufferTab").setText(this.getBuffersTitle());
Ext.getCmp("SpecimenTab").setText(this.getSpecimenTitle());
Ext.getCmp("PlatesTab").setText(this.getPlatesTitle());
Ext.getCmp("AssembliesTab").setText(this.getShipmentTitle());
Ext.getCmp("PlateGroupsTab").setText(this.getPlateGroupsTitle());*/
//};
ShipmentTabs.prototype.draw = function(shipment) {
	var _this = this;
	_this.plates = [];
	_this.shipment = shipment;
	_this.render(shipment);

	//	var proposal = new Proposal();
	//	proposal.onDataRetrieved.attach(function(sender, plates){
	//		_this.plates = plates;
	//		_this.shipment = shipment;
	//		_this.render(shipment);
	//		
	//	});
	//	proposal.getPlatesByProposal();
};

//ShipmentTabs.prototype.getShipmentTitle = function() {
//	return 'Shipment';
//};

//ShipmentTabs.prototype.getMacromoleculeTitle = function() {
//	return 'Macromolecules (' + this.experiment.getMacromolecules().length + ')';
//};

//ShipmentTabs.prototype.getBuffersTitle = function() {
//	return 'Buffers (' + this.experiment.getBuffers().length + ')';
//};

//ShipmentTabs.prototype.getPlateGroupsTitle = function() {
//	return 'Plate Groups (' + this.experiment.getPlateGroups().length + ')';
//};

//ShipmentTabs.prototype.getSampleChangerTitle = function() {
//	return 'Sample Changer';
//};

//ShipmentTabs.prototype.getSpecimenTitle = function() {
//	return 'Specimens(' + this.experiment.getSpecimenCount() + ')';
//};

//ShipmentTabs.prototype.getPlatesTitle = function() {
//	return 'Plates(' + this.experiment.getSamplePlates().length + ')';
//};

//ShipmentTabs.prototype.getBuffersTip = function() {
/*if (this.experiment.getBuffers().length == 0){
	return BUI.getWarningHTML("There are no buffers. Click on add to create new ones. Click on edit button or double click to edit them");
	
}
else{
	return BUI.getTipHTML("Click on edit button or double click to edit them. Click on duplicate to create an identical buffer including its additives")
}*/
//};

//ShipmentTabs.prototype.refreshTips = function() {
/*Ext.getCmp("BufferTabTip").update(this.getBuffersTip());
Ext.getCmp("SpecimenTabTip").update(this.getSpecimensTip());*/
//};

ShipmentTabs.prototype.render = function(shipment) {
	return this.getPanel(shipment);
};

ShipmentTabs.prototype.getShipmentHeader = function(shipment) {
	var _this = this;
	function getHTMLSource() {
		var name = shipment.json.shippingName;
		var status = shipment.json.shippingStatus;
		var creationDate = shipment.json.creationDate;
		var html = BUI.createFormLabel("Name :", name, 75, 400);
		html = html + BUI.createFormLabel("Status :", status, 75, 400);
		html = html + BUI.createFormLabel("Date :", creationDate, 75, 400);
		return html;
	}

	return Ext.create('Ext.container.Container', {
		frame : false,
		layout : 'hbox',
		title : 'General',
		padding : 5,
		bodyPadding : '5 5 0 0',
		width : 890,
		margin : '0 0 10 0',
		height : 100,
		style : {
			borderColor : '#BDBDBD',
			borderStyle : 'solid',
			borderWidth : '1px'
		},
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 100
		},
		items : [ {
			margin : "0 0 0 0",
			width : 475,
			border : 0,
			html : getHTMLSource()
		}
		]
	});
};

ShipmentTabs.prototype.getTabPanel = function(shipment) {
	this.panel = Ext.createWidget('tabpanel', {
		height : 600,
		style : {
			padding : 2
		},
		items : [ {
			tabConfig : {
				id : 'Shipment',
				title : 'Shipment',
				icon : '/ispyb/images/plane-small.gif'
			},
			items : [ {
				xtype : 'container',
				margin : '5 5 5 5',
				items : [ this.shipmentForm.getPanel(shipment) ]
			} ]
		}, {
			tabConfig : {
				id : 'Cases',
				title : 'Cases',
				icon : '../images/box-icon-very-small.png'
			},
			items : [ {
				xtype : 'container',
				margin : '5 5 5 5',
				items : [ this.caseGrid.getPanel(shipment.getDewars(), this.plates) ]
			} ]
		} ]
	});
	return this.panel;
};

ShipmentTabs.prototype.getPanel = function(shipment) {
	var _this = this;
	this.shipment = shipment;
	if (this.plates == null) {
		this.plates = [];
	}

	if (this.shipPanel == null) {
		this.shipPanel = Ext.create('Ext.container.Container', {
			bodyPadding : 2,
			width : Ext.getBody().getViewSize().width * 0.9,
			renderTo : this.targetId,
			style : {
				padding : 2
			},
			items : [ this.getShipmentHeader(shipment), this.getTabPanel(shipment) ]
		});
	}

	return this.shipPanel;
};
