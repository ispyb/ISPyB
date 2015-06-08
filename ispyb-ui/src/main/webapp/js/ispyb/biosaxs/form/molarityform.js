/**
 * Example form
 * 
 * @witdh
 * @height
 */
function MolarityForm(args) {
	this.id = BUI.id();
	this.width = 700;
	this.height = 500;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	
	this.onSave = new Event(this);
	this.onClose = new Event(this);
}


MolarityForm.prototype._getNumericWithHelp = function(type, fieldLabel, fieldName, help) {
	return Ext.create('Ext.container.Container', {
		margin : "10 0 0 10",
		items : [ {
			xtype : type,
			fieldLabel : fieldLabel,
			name : fieldName,
			id : this.id + fieldName,
			decimalPrecision : 6
		}, {
			xtype : 'label',
			forId : 'myFieldId',
			text : help,
			margin : "5 0 0 105",
			cls : "inline-help"
		} ]
	});
};


MolarityForm.prototype._getItems = function() {
	this.macromoleculeCombo = BIOSAXS_COMBOMANAGER.getComboMacromoleculeByMacromolecules(this.getMacromuleculesCandidates(this.macromolecule), {
		width : 250,
		labelWidth : 100,
		margin : 10
	});
	
	return [ {
		xtype : 'container',
		flex : 1,
		margin : '10 0 0 10',
		border : 0,
		layout : 'anchor',
		defaultType : 'requiredtext',
		items : [ this.macromoleculeCombo,
		this._getNumericWithHelp("textfield", "Ratio", "ratio", "Number in assymmetric units")
		]
	} ];
};

MolarityForm.prototype._persist = function() {
	var _this = this;
	var macromoleculeId = this.macromoleculeCombo.getValue();
	var ratio = Ext.getCmp(this.id + "ratio").getValue();
	var comments = "Not used yet";
	var dataAdapter = new BiosaxsDataAdapter();
	this.panel.setLoading("Saving");
	dataAdapter.onSuccess.attach(function(sender, args) {
		_this.onSave.notify();
	});
	dataAdapter.saveStoichiometry(this.macromolecule.macromoleculeId, macromoleculeId, ratio, comments);
};

MolarityForm.prototype._getButtons = function() {
	var _this = this;
	
	function onClose() {
		_this.onClose.notify();
	}
	
	return [ {
		text : 'Save',
		handler : function() {
			_this._persist();
		}
	}, {
		text : 'Cancel',
		handler : function() {
			onClose();
		}
	} ];
};

MolarityForm.prototype.getPanel = function() {
	this.panel = Ext.create('Ext.form.Panel', {
//		width : null,
		height : this.height,
		margin : 2,
		border : 1,
		defaultType : 'requiredtext',
		items : this._getItems(),
		buttons : this._getButtons()
	});
	return this.panel;
};

/** macromolecules contains all macromolecules except this one **/
MolarityForm.prototype.getMacromuleculesCandidates = function(macromolecule) {
	var macromolecules = [];
	if ( BIOSAXS.proposal.macromolecules){
		for (var i = 0; i < BIOSAXS.proposal.macromolecules.length; i++) {
			var m = BIOSAXS.proposal.macromolecules[i];
			if (this.macromolecule != null){
				if (m.macromoleculeId != this.macromolecule.macromoleculeId) {
					macromolecules.push(m);
				}
			}
		}
	}
	return macromolecules;
};


/** It populates the form **/
MolarityForm.prototype.refresh = function(macromolecule) {
	this.macromolecule = macromolecule;
	
};


MolarityForm.prototype.input = function() {
	return {};
};


/** It populates the form **/
MolarityForm.prototype.test = function(targetId) {
	var macromoleculeForm = new MolarityForm();
	var panel = macromoleculeForm.getPanel();
	panel.render(targetId);
};
