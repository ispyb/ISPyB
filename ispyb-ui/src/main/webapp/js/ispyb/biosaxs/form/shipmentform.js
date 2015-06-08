/**
 * Same form as MX part
 * 
 * @creationMode if true a create button appears instead of save
 * @showTitle true or false
 */
function ShipmentForm(args) {
	this.id = BUI.id();

	this.creationMode = false;
	this.showTitle = true;
	if (args != null) {
		if (args.creationMode != null) {
			this.creationMode = args.creationMode;
		}
		if (args.showTitle != null) {
			this.showTitle = args.showTitle;
		}
	}

	this.onSaved = new Event(this);
}

ShipmentForm.prototype.fillStores = function() {
	this.panel.setLoading("Loading Labcontacts from database");
	this.labContactForSendingStore.loadData(BIOSAXS.proposal.getLabcontacts(), false);
	this.labContactForReturnStore.loadData(BIOSAXS.proposal.getLabcontacts(), false);
	this.panel.setLoading(false);
	if (this.shipment != null) {
		this.setShipment(this.shipment);
	}
};

ShipmentForm.prototype.draw = function(targetId) {
	this.getPanel().render(targetId);
};

ShipmentForm.prototype.setShipment = function(shipment) {
	this.shipment = shipment;
	var _this = this;
	Ext.getCmp(_this.id + "shippingName").setValue(shipment.json.shippingName);
	Ext.getCmp(_this.id + "shippingStatus").setValue(shipment.json.shippingStatus);
	Ext.getCmp(_this.id + "comments").setValue(shipment.json.comments);
	if (shipment.json.sendingLabContactVO != null) {
		this.labContactsSendingCombo.setValue(shipment.json.sendingLabContactVO.labContactId);
	}
	if (shipment.json.returnLabContactVO != null) {
		this.labContactsReturnCombo.setValue(shipment.json.returnLabContactVO.labContactId);
	}

};

ShipmentForm.prototype._saveShipment = function() {
	var _this = this;
	var shippingId = null;
	if (this.shipment != null) {
		shippingId = this.shipment.json.shippingId;
	}
	var json = {
		shippingId : shippingId,
		name : Ext.getCmp(_this.id + "shippingName").getValue(),
		status : Ext.getCmp(_this.id + "shippingStatus").getValue(),
		sendingLabContactId : Ext.getCmp(_this.id + "shipmentform_sendingLabContactId").getValue(),
		returnLabContactId : Ext.getCmp(_this.id + "returnLabContactId").getValue(),
		returnCourier : Ext.getCmp(_this.id + "returnCourier").getValue(),
		courierAccount : Ext.getCmp(_this.id + "courierAccount").getValue(),
		BillingReference : Ext.getCmp(_this.id + "BillingReference").getValue(),
		dewarAvgCustomsValue : Ext.getCmp(_this.id + "dewarAvgCustomsValue").getValue(),
		dewarAvgTransportValue : Ext.getCmp(_this.id + "dewarAvgTransportValue").getValue(),
		comments : Ext.getCmp(_this.id + "comments").getValue()
	};

	var dataAdapter = new BiosaxsDataAdapter();
	dataAdapter.onSuccess.attach(function(sender, shipment) {
		window.location = BUI.getShippingURL(shipment.shippingId);
	});

	dataAdapter.onError.attach(function(sender, error) {
		_this.onError.notify(error);
	});

	/** Cheking params **/
	if (json.name == "") {
		BUI.showError("Name field is mandatory");
		return;
	}

	if (json.sendingLabContactId == null) {
		BUI.showError("Lab contact for sending field is mandatory");
		return;
	}

	if (json.returnLabContactId == null) {
		BUI.showError("Lab contact for return field is mandatory");
		return;
	}

	dataAdapter.createShipment(json.shippingId, json.name, json.status, json.comments, json.sendingLabContactId, json.returnLabContactId,
			json.returnCourier, json.courierAccount, json.BillingReference, json.dewarAvgCustomsValue, json.dewarAvgTransportValue);

};

ShipmentForm.prototype.getPanel = function(shipment) {
	var _this = this;
	this.shipment = shipment;
	var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
	var buttons = [];

	if (_this.creationMode) {
		buttons.push({
			text : 'Create',
			scope : this,
			handler : function() {
				_this._saveShipment();
			}
		});
	} else {
		buttons.push({
			text : 'Save',
			scope : this,
			handler : function() {
				_this._saveShipment();
			}
		});

	}

	this.labContactForSendingStore = Ext.create('Ext.data.Store', {
		fields : [ 'cardName', 'labContactId' ]
	});

	this.labContactForReturnStore = Ext.create('Ext.data.Store', {
		fields : [ 'cardName', 'labContactId' ]
	});

	// Create the combo box, attached to the states data store
	this.labContactsSendingCombo = Ext.create('Ext.form.ComboBox', {
		id : _this.id + "shipmentform_sendingLabContactId",
		fieldLabel : 'Lab contact for sending',
		afterLabelTextTpl : required,
		store : this.labContactForSendingStore,
		queryMode : 'local',
		labelWidth : 200,
		displayField : 'cardName',
		valueField : 'labContactId'
	});

	this.labContactsReturnCombo = Ext.create('Ext.form.ComboBox', {
		id : _this.id + "returnLabContactId",
		fieldLabel : 'If No, Lab-Contact for Return',
		afterLabelTextTpl : required,
		store : this.labContactForReturnStore,
		queryMode : 'local',
		labelWidth : 200,
		displayField : 'cardName',
		valueField : 'labContactId',
		listeners : {
			change : function(x, newValue) {
				for ( var i = 0; i < x.getStore().data.items.length; i++) {
					if (x.getStore().data.items[i].raw.labContactId == newValue) {
						Ext.getCmp(_this.id + "returnCourier").setValue(x.getStore().data.items[i].raw.defaultCourrierCompany);
						Ext.getCmp(_this.id + "courierAccount").setValue(x.getStore().data.items[i].raw.courierAccount);
						Ext.getCmp(_this.id + "BillingReference").setValue(x.getStore().data.items[i].raw.billingReference);
						Ext.getCmp(_this.id + "dewarAvgCustomsValue").setValue(x.getStore().data.items[i].raw.dewarAvgCustomsValue);
						Ext.getCmp(_this.id + "dewarAvgTransportValue").setValue(x.getStore().data.items[i].raw.dewarAvgTransportValue);
					}
				}
			}
		}
	});

	if (this.panel == null) {
		this.panel = Ext.create('Ext.form.Panel', {
			bodyPadding : 5,
			width : 600,
			border : 1,
			items : [ {
				xtype : 'fieldset',
				title : 'Details',
				collapsible : false,
				defaultType : 'textfield',
				layout : 'anchor',
				defaults : {
					anchor : '100%'
				},
				items : [ {
					xtype : 'requiredtext',
					fieldLabel : 'Shipment Label',
					allowBlank : false,
					name : 'shippingName',
					id : _this.id + 'shippingName',
					value : '',
					anchor : '50%'
				}, {

					xtype : 'textareafield',
					name : 'comments',
					id : _this.id + 'comments',
					fieldLabel : 'Comments',
					value : ''
				}, {
					fieldLabel : 'Status',
					readOnly : true,
					id : _this.id + 'shippingStatus',
					value : 'Opened',
					anchor : '50%'
				} ]
			}, {
				xtype : 'fieldset',
				title : 'Lab-Contacts',
				collapsible : false,
				defaultType : 'textfield',
				layout : 'anchor',
				defaults : {
					anchor : '100%'
				},
				items : [ this.labContactsSendingCombo, this.labContactsReturnCombo ]
			}, {
				border : 0,
				html : BUI.getWarningHTML("These informations are relevant for all shipments")
			}, {
				xtype : 'fieldset',
				title : 'Courier accounts details for return',
				collapsible : false,
				layout : 'anchor',
				defaults : {
					anchor : '100%'
				},
				items : [ {
					xtype : 'textfield',
					labelWidth : 400,
					fieldLabel : 'Courier company for return (if ESRF sends a dewar back)',
					id : _this.id + 'returnCourier',
					value : ''
				}, {
					xtype : 'textfield',
					labelWidth : 400,
					fieldLabel : 'Courier account',
					id : _this.id + 'courierAccount',
					value : ''
				}, {
					xtype : 'textfield',
					labelWidth : 400,
					fieldLabel : 'Billing reference',
					id : _this.id + 'BillingReference',
					value : ''
				}, {
					xtype : 'numberfield',
					labelWidth : 400,
					fieldLabel : 'Average Customs value of a dewar (Euro)',
					id : _this.id + 'dewarAvgCustomsValue',
					value : ''
				}, {
					xtype : 'numberfield',
					labelWidth : 400,
					fieldLabel : 'Average Transport value of a dewar (Euro)',
					id : _this.id + 'dewarAvgTransportValue',
					value : ''
				} ]
			} ],
			buttons : buttons
		});
	}
	this.fillStores();
	if (this.showTitle) {
		this.panel.setTitle('Create a new Shipment');
	}
	return this.panel;
};

ShipmentForm.prototype.input = function() {
	return {
		proposal : DATADOC.getProposal_10()

	};
};

ShipmentForm.prototype.test = function(targetId) {
	var shipmentForm = new ShipmentForm({
		creationMode : true

	});
	BIOSAXS.proposal = new Proposal(shipmentForm.input().proposal);
	shipmentForm.getPanel().render(targetId);
};
