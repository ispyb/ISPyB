/**
 * #onSaved
 */
function StockSolutionForm(args) {
	this.id = BUI.id();
	this.actions = [];

	if (args != null) {
		if (args.actions != null) {
			this.actions = args.actions;
		}
	}
	this.onSaved = new Event(this);
}

StockSolutionForm.prototype.getStockSolution = function() {
	if (this.stockSolution != null) {
		this.stockSolution.concentration = Ext.getCmp(this.id + "stockSolution_concentration").getValue();
		this.stockSolution.storageTemperature = Ext.getCmp(this.id + "stockSolution_storageTemperature").getValue();
		this.stockSolution.volume = Ext.getCmp(this.id + "stockSolution_volume").getValue();
		this.stockSolution.comments = Ext.getCmp(this.id + "stockSolution_comments").getValue();
		this.stockSolution.name = Ext.getCmp(this.id + "stockSolution_name").getValue();
		this.stockSolution.bufferId = this.bufferCombo.getValue();

		if (this.macromoleculeCombo.getValue() != null) {
			this.stockSolution.macromoleculeId = this.macromoleculeCombo.getValue();
		} else {
			this.stockSolution.macromolecule3VO = null;
		}

	} else {
		return {
			concentration : Ext.getCmp(this.id + "stockSolution_concentration").getValue(),
			storageTemperature : Ext.getCmp(this.id + "stockSolution_storageTemperature").getValue(),
			volume : Ext.getCmp(this.id + "stockSolution_volume").getValue(),
			comments : Ext.getCmp(this.id + "stockSolution_comments").getValue(),
			name : Ext.getCmp(this.id + "stockSolution_name").getValue(),
			bufferId : this.bufferCombo.getValue(),
			macromoleculeId : this.macromoleculeCombo.getValue()
		};
	}
	return this.stockSolution;
};

StockSolutionForm.prototype.setStockSolution = function(stockSolution) {
	if (stockSolution != null) {
		if (stockSolution.macromoleculeId != null) {
			this.macromoleculeCombo.setValue(stockSolution.macromoleculeId);
		}
		this.bufferCombo.setValue(stockSolution.bufferId);
		Ext.getCmp(this.id + "stockSolution_concentration").setValue(this.stockSolution.concentration);
		Ext.getCmp(this.id + "stockSolution_storageTemperature").setValue(this.stockSolution.storageTemperature);
		Ext.getCmp(this.id + "stockSolution_volume").setValue(this.stockSolution.volume);
		Ext.getCmp(this.id + "stockSolution_name").setValue(this.stockSolution.name);
		Ext.getCmp(this.id + "stockSolution_comments").setValue(this.stockSolution.comments);
	}
};

StockSolutionForm.prototype.getBufferCombo = function() {
	this.bufferCombo = BIOSAXS_COMBOMANAGER.getComboBuffers(BIOSAXS.proposal.getBuffers(), {
		labelWidth : 120,
		margin : '0 0 10 0',
		width : 220

	});
	return this.bufferCombo;
};

StockSolutionForm.prototype.getMacromoleculeCombo = function() {
	this.macromoleculeCombo = BIOSAXS_COMBOMANAGER.getComboMacromoleculeByMacromolecules(BIOSAXS.proposal.getMacromolecules(), {
		labelWidth : 120,
		margin : '0 0 10 0',
		width : 220

	});
	return this.macromoleculeCombo;
};

StockSolutionForm.prototype.refresh = function() {
};

StockSolutionForm.prototype._getTopPanel = function() {
	return {
		xtype : 'container',
		layout : 'hbox',
		border : 0,
		items : [ {
			xtype : 'container',
			layout : 'hbox',
			items : [ {
				xtype : 'container',
				flex : 1,
				border : false,
				layout : 'anchor',
				defaultType : 'textfield',
				items : [

				this.getMacromoleculeCombo(), {
					xtype : 'requiredtext',
					id : this.id + 'stockSolution_name',
					fieldLabel : 'Acronym',
					labelWidth : 120,
					width : 250
				}, {
					xtype : 'requiredtext',
					id : this.id + 'stockSolution_concentration',
					fieldLabel : 'Conc. (mg/ml)',
					labelWidth : 120,
					width : 250
				},

				{
					id : this.id + 'stockSolution_storageTemperature',
					fieldLabel : 'Storage Temp.(C)',
					labelWidth : 120,
					width : 250
				}, {
					xtype : 'requiredtext',
					id : this.id + 'stockSolution_volume',
					fieldLabel : 'Volume in Well (&#181l)',
					labelWidth : 120,
					width : 250
				} ]
			} ]
		}, {
			xtype : 'container',
			flex : 1,
			layout : 'anchor',
			defaultType : 'textfield',
			margin : '0 0 0 10',
			items : [ this.getBufferCombo() ]
		} ]
	};

};

StockSolutionForm.prototype.getPanel = function(stockSolution) {
	this.stockSolution = stockSolution;
	this.panel = Ext.createWidget({
		xtype : 'container',
		layout : 'vbox',
		border : 0,
		style : {
			padding : '10px'
		},
		fieldDefaults : {
			labelAlign : 'left',
			labelWidth : 50
		},
		items : [ this._getTopPanel(stockSolution), {
			id : this.id + 'stockSolution_comments',
			xtype : 'textareafield',
			name : 'comments',
			fieldLabel : 'Comments',
			labelWidth : 120,
			width : '100%'
		} ]
	});

	this.setStockSolution(stockSolution);
	return this.panel;
};

StockSolutionForm.prototype.input = function() {
	return {
		stock : {
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
			"samples" : [],
			"buffer" : "EDTA",
			"macromolecule" : "A"
		},
		proposal : new MeasurementGrid().input().proposal
	};
};

StockSolutionForm.prototype.test = function(targetId) {
	var stockSolutionForm = new StockSolutionForm();
	BIOSAXS.proposal = new Proposal(stockSolutionForm.input().proposal);
	var panel = stockSolutionForm.getPanel(new Shipment(stockSolutionForm.input().stock));
	panel.render(targetId);
};
