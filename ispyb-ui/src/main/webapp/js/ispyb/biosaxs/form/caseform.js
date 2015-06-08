/**
 * @showTitle
 *
 * #onSaved
 * #onAddPlates
 * #onRemovePlates
 **/
function CaseForm(args) {
	this.width = 700;
	this.showTitle = true;
	if (args != null) {
		if (args.showTitle != null) {
			this.showTitle = args.showTitle;
		}
	}

	var _this = this;
	this.stockSolutionGrid = new StockSolutionGrid({
		width : this.width - 10,
		minHeight : 300,
		height : 300,
		tbar : true,
		showTitle : true,
		isPackedVisible : false,
		btnAddExisting : true,
		btnRemoveVisible : false,
		btnUnpackVisible : true
	});

	/** When selecting existing stock solutions **/
	this.stockSolutionGrid.onStockSolutionSelected.attach(function(sender, stockSolutions) {
		if (stockSolutions != null) {
			for ( var i = 0; i < stockSolutions.length; i++) {
				_this.saveStockSolution(stockSolutions[i]);
			}
		}
	});

	/** it can be because it has been added a new one or removed **/
	this.stockSolutionGrid.onProposalChanged.attach(function(sender, stockSolution) {
		if (stockSolution != null) {
			_this.saveStockSolution(stockSolution);
		} else {
			BIOSAXS.proposal.onInitialized.attach(function() {
				_this.stockSolutionGrid.refresh(BIOSAXS.proposal.getStockSolutionsByDewarId(_this.dewar.dewarId));

				_this.stockSolutionGrid.grid.setLoading(false);
			});
			BIOSAXS.proposal.init();

		}

	});

	this.onSaved = new Event(this);
	this.onAddPlates = new Event(this);
	this.onRemovePlates = new Event(this);
}

CaseForm.prototype.saveStockSolution = function(stockSolution) {
	var _this = this;
	var adapter = new BiosaxsDataAdapter();
	this.stockSolutionGrid.grid.setLoading("ISPyB: setting case to Stock solution");
	adapter.onSuccess.attach(function(sender, stockSolution) {
		BIOSAXS.proposal.onInitialized.attach(function() {
			_this.stockSolutionGrid.refresh(BIOSAXS.proposal.getStockSolutionsByDewarId(_this.dewar.dewarId));
			_this.stockSolutionGrid.grid.setLoading(false);
		});
		BIOSAXS.proposal.init();
	});
	adapter.onError.attach(function(sender, data) {
		_this.stockSolutionGrid.grid.setLoading(false);
		BUI.showError(data);
	});
	stockSolution.boxId = _this.dewar.dewarId;
	adapter.saveStockSolution(stockSolution);
};

CaseForm.prototype.fillStores = function() {
	var _this = this;
	this.panel.setLoading("Loading Labcontacts from database");

	var proposal = BUI.getProposal();
	proposal.onDataRetrieved.attach(function(sender, data) {
		_this.labContactForSendingStore.loadData(data, false);
		_this.labContactForReturnStore.loadData(data, false);
		_this.panel.setLoading(false);
	});
	proposal.getLabContactsByProposalId();

};

CaseForm.prototype.refresh = function(dewar) {
	this.setDewar(dewar);
	this.stockSolutionGrid.refresh(BIOSAXS.proposal.getStockSolutionsByDewarId(dewar.dewarId));
};

CaseForm.prototype.getDewar = function() {
	this.dewar.code = Ext.getCmp("dewar_code").getValue();
	this.dewar.comments = Ext.getCmp("dewar_comments").getValue();
	this.dewar.trackingNumberFromSynchrotron = Ext.getCmp("dewar_trackingNumberFromSynchrotron").getValue();
	this.dewar.trackingNumberToSynchrotron = Ext.getCmp("dewar_trackingNumberToSynchrotron").getValue();
	this.dewar.transportValue = Ext.getCmp("dewar_transportValue").getValue();
	this.dewar.storageLocation = Ext.getCmp("dewar_storageLocation").getValue();
	this.dewar.firstExperimentId = this.macromoleculeCombo.getValue();
	return this.dewar;
};

CaseForm.prototype.setDewar = function(dewar) {
	this.dewar = dewar;
	Ext.getCmp("dewar_code").setValue(this.dewar.code);
	Ext.getCmp("dewar_dewarStatus").setText(new String(this.dewar.dewarStatus).toUpperCase());
	Ext.getCmp("dewar_comments").setValue(this.dewar.comments);
	Ext.getCmp("dewar_trackingNumberFromSynchrotron").setValue(this.dewar.trackingNumberFromSynchrotron);
	Ext.getCmp("dewar_trackingNumberToSynchrotron").setValue(this.dewar.trackingNumberToSynchrotron);
	Ext.getCmp("dewar_transportValue").setValue(this.dewar.transportValue);
	Ext.getCmp("dewar_storageLocation").setValue(this.dewar.storageLocation);
	if (dewar.sessionVO != null) {
		this.macromoleculeCombo.setValue(dewar.sessionVO.sessionId);
	}
};

CaseForm.prototype.getSessionCombo = function() {
	this.macromoleculeCombo = BIOSAXS_COMBOMANAGER.getComboSessions(BIOSAXS.proposal.getSessions(), {
		labelWidth : 100,
		margin : '5 0 00 0',
		width : 250
	});
	return this.macromoleculeCombo;
};

CaseForm.prototype.getInformationPanel = function() {
	if (this.panel == null) {
		this.informationPanel = Ext.create('Ext.form.Panel', {
			width : this.width - 10,
			border : 0,
			items : [ {
				xtype : 'container',
				margin : "2 2 2 2",
				collapsible : false,
				defaultType : 'textfield',
				layout : 'anchor',
				items : [ {
					xtype : 'container',
					layout : 'hbox',
					items : [ {
						xtype : 'requiredtext',
						fieldLabel : 'Code',
						allowBlank : false,
						name : 'code',
						id : 'dewar_code',
						anchor : '50%'
					}, {
						xtype : 'label',
						margin : '0 0 0 20',
						readOnly : true,
						id : 'dewar_dewarStatus',
						anchor : '50%'
					} ]
				}, this.getSessionCombo(), {
					margin : '5 0 0 0',
					xtype : 'textareafield',
					name : 'comments',
					id : 'dewar_comments',
					width : this.width - 50,
					fieldLabel : 'Comments'
				} ]
			}, {
				xtype : 'fieldset',
				title : 'Courier Accounts Details for Return',
				collapsible : false,
				layout : 'anchor',
				defaults : {
					anchor : '100%'
				},
				items : [ {
					xtype : 'container',
					layout : 'hbox',
					items : [ {
						xtype : 'textfield',
						labelWidth : 200,
						width : 280,
						fieldLabel : 'Track Number From Synchrotron',
						id : 'dewar_trackingNumberFromSynchrotron'
					}, {
						xtype : 'numberfield',
						width : 190,
						labelWidth : 110,
						margin : '0 0 0 30',
						fieldLabel : 'Transport Value',
						id : 'dewar_transportValue'
					}

					]
				}, {
					xtype : 'container',
					layout : 'hbox',
					margin : '10 0 0 0',
					items : [

					{
						xtype : 'textfield',
						labelWidth : 200,
						width : 280,
						fieldLabel : 'Track Number To Synchrotron',
						id : 'dewar_trackingNumberToSynchrotron'
					}, {
						xtype : 'textfield',
						margin : '0 0 0 30',
						width : 190,
						labelWidth : 110,
						fieldLabel : 'Storage Location',
						id : 'dewar_storageLocation'
					}

					]
				} ]
			} ]
		});
	}

	return this.informationPanel;
};

CaseForm.prototype.getPanel = function(dewar) {
	this.dewar = dewar;
	this.panel = Ext.createWidget({
		xtype : 'container',
		layout : 'vbox',
		width : this.width,
		border : 0,
		items : [ {
			items : {
				xtype : "container",
				layout : "vbox",
				margin : "5 5 5 5",
				items : [ this.getInformationPanel(dewar), this.stockSolutionGrid.getPanel() ]

			}
		} ]
	});

	this.refresh(dewar);
	return this.panel;

};

CaseForm.prototype.input = function() {
	return {
		dewar : {
			"dewarId" : 305861,
			"code" : "ESRF-TEST",
			"comments" : "comments",
			"storageLocation" : "FRIDGE",
			"dewarStatus" : "opened",
			"timeStamp" : null,
			"isStorageDewar" : null,
			"barCode" : "ESRF305861",
			"customsValue" : null,
			"transportValue" : null,
			"trackingNumberToSynchrotron" : "3333",
			"trackingNumberFromSynchrotron" : "224466",
			"type" : "Dewar",
			"sessionVO" : {
				"sessionId" : 31697,
				"expSessionPk" : null,
				"projectCode" : null,
				"startDate" : "2012 07 21",
				"endDate" : "2012 07 23",
				"beamlineName" : "BM29",
				"scheduled" : 1,
				"nbShifts" : 2,
				"comments" : null,
				"beamlineOperator" : "PERNOT  P",
				"usedFlag" : null,
				"sessionTitle" : null,
				"structureDeterminations" : null,
				"dewarTransport" : null,
				"databackupFrance" : null,
				"databackupEurope" : null,
				"visit_number" : null,
				"operatorSiteNumber" : "14061",
				"timeStamp" : "2012 04 25"
			}
		},
		proposal : {
			"assemblies" : [],
			"sessions" : [ {
				"sessionId" : 31697,
				"expSessionPk" : null,
				"projectCode" : null,
				"startDate" : "2012 07 21",
				"endDate" : "2012 07 23",
				"beamlineName" : "BM29",
				"scheduled" : 1,
				"nbShifts" : 2,
				"comments" : null,
				"beamlineOperator" : "PERNOT  P",
				"usedFlag" : null,
				"sessionTitle" : null,
				"structureDeterminations" : null,
				"dewarTransport" : null,
				"databackupFrance" : null,
				"databackupEurope" : null,
				"visit_number" : null,
				"operatorSiteNumber" : "14061",
				"timeStamp" : "2012 04 25"
			} ],
			"labcontacts" : [ {
				"labContactId" : 787,
				"personVO" : {
					"personId" : 304252,
					"personUUID" : null,
					"familyName" : "KIM",
					"givenName" : "Henry",
					"title" : null,
					"emailAddress" : "henry-sung.kim@ibs.fr",
					"phoneNumber" : "",
					"login" : "",
					"passwd" : "",
					"faxNumber" : ""
				},
				"cardName" : "KIM-Institut de Bio",
				"defaultCourrierCompany" : "22",
				"courierAccount" : "",
				"billingReference" : "",
				"dewarAvgCustomsValue" : 0,
				"dewarAvgTransportValue" : 0
			} ],
			"buffers" : [ {
				"bufferId" : 811,
				"proposalId" : 3124,
				"safetyLevelId" : null,
				"name" : "EDTA",
				"acronym" : "EDTA",
				"ph" : null,
				"composition" : "",
				"bufferhasadditive3VOs" : [],
				"comments" : ""
			}, {
				"bufferId" : 810,
				"proposalId" : 3124,
				"safetyLevelId" : null,
				"name" : "HEPES",
				"acronym" : "HEPES",
				"ph" : null,
				"composition" : "",
				"bufferhasadditive3VOs" : [],
				"comments" : ""
			} ],
			"shippings" : [ {
				"shippingId" : 304107,
				"shippingName" : "TEST",
				"deliveryAgentAgentName" : null,
				"deliveryAgentShippingDate" : null,
				"deliveryAgentDeliveryDate" : null,
				"deliveryAgentAgentCode" : null,
				"deliveryAgentFlightCode" : null,
				"shippingStatus" : "opened",
				"timeStamp" : "2013 09 25",
				"laboratoryId" : null,
				"isStorageShipping" : null,
				"creationDate" : "2013 09 25",
				"comments" : "test",
				"sendingLabContactVO" : {
					"labContactId" : 787,
					"personVO" : {
						"personId" : 304252,
						"personUUID" : null,
						"familyName" : "KIM",
						"givenName" : "Henry",
						"title" : null,
						"emailAddress" : "henry-sung.kim@ibs.fr",
						"phoneNumber" : "",
						"login" : "",
						"passwd" : "",
						"faxNumber" : ""
					},
					"cardName" : "KIM-Institut de Bio",
					"defaultCourrierCompany" : "22",
					"courierAccount" : "",
					"billingReference" : "",
					"dewarAvgCustomsValue" : 0,
					"dewarAvgTransportValue" : 0
				},
				"returnLabContactVO" : {
					"labContactId" : 787,
					"personVO" : {
						"personId" : 304252,
						"personUUID" : null,
						"familyName" : "KIM",
						"givenName" : "Henry",
						"title" : null,
						"emailAddress" : "henry-sung.kim@ibs.fr",
						"phoneNumber" : "",
						"login" : "",
						"passwd" : "",
						"faxNumber" : ""
					},
					"cardName" : "KIM-Institut de Bio",
					"defaultCourrierCompany" : "22",
					"courierAccount" : "",
					"billingReference" : "",
					"dewarAvgCustomsValue" : 0,
					"dewarAvgTransportValue" : 0
				},
				"returnCourier" : null,
				"dateOfShippingToUser" : null,
				"shippingType" : "DewarTracking",
				"dewarVOs" : [ {
					"dewarId" : 305861,
					"code" : "ESRF-TEST",
					"comments" : "comments",
					"storageLocation" : "FRIDGE",
					"dewarStatus" : "opened",
					"timeStamp" : null,
					"isStorageDewar" : null,
					"barCode" : "ESRF305861",
					"customsValue" : null,
					"transportValue" : null,
					"trackingNumberToSynchrotron" : "3333",
					"trackingNumberFromSynchrotron" : "224466",
					"type" : "Dewar",
					"sessionVO" : {
						"sessionId" : 31697,
						"expSessionPk" : null,
						"projectCode" : null,
						"startDate" : "2012 07 21",
						"endDate" : "2012 07 23",
						"beamlineName" : "BM29",
						"scheduled" : 1,
						"nbShifts" : 2,
						"comments" : null,
						"beamlineOperator" : "PERNOT  P",
						"usedFlag" : null,
						"sessionTitle" : null,
						"structureDeterminations" : null,
						"dewarTransport" : null,
						"databackupFrance" : null,
						"databackupEurope" : null,
						"visit_number" : null,
						"operatorSiteNumber" : "14061",
						"timeStamp" : "2012 04 25"
					}
				} ]
			} ],
			"macromolecules" : [ {
				"macromoleculeId" : 5933,
				"safetylevelId" : null,
				"proposalId" : 3124,
				"name" : "A",
				"acronym" : "A",
				"molecularMass" : "",
				"extintionCoefficient" : "",
				"sequence" : null,
				"creationDate" : null,
				"comments" : "",
				"macromoleculeregion3VOs" : [],
				"stoichiometry3VOsForHostMacromoleculeId" : [],
				"structure3VOs" : []
			} ],
			"stockSolutions" : [ {
				"stockSolutionId" : 6,
				"proposalId" : 3124,
				"macromoleculeId" : 5933,
				"bufferId" : 811,
				"instructionSet3VO" : null,
				"boxId" : 305861,
				"storageTemperature" : "20",
				"volume" : "300",
				"concentration" : "1.2",
				"comments" : "Buffer EDTA with A",
				"name" : "A_EDTA_1.2",
				"samples" : []
			} ]
		}

	};
};

CaseForm.prototype.test = function(targetId) {
	var caseForm = new CaseForm();
	BIOSAXS.proposal = new Proposal(caseForm.input().proposal);
	var panel = caseForm.getPanel(caseForm.input().dewar);
	panel.render(targetId);
};
