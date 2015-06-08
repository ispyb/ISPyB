/**
 * Example form
 * 
 * @witdh
 * @height
 */
function RigibBodyModelingForm(args) {
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

	var _this = this;
	this.rigidBodyGrid = new AprioriRigidBodyGrid();
	
	this.rigidBodyGrid.onUploadFile.attach(function(sender, type, title){
		_this._openUploadDialog(_this.macromolecule.macromoleculeId,  type, title);
	});
	
	this.rigidBodyGrid.onRemove.attach(function(sender, type, title){
		_this._update();
	});
	
	this.onSave = new Event(this);
	
}

RigibBodyModelingForm.prototype._getItems = function() {
	var _this = this;
	

	return [ {
		xtype : 'label',
		forId : 'myFieldId',
		text : 'Information for model fit, mixture analysis and rigid body modeling',
		margin : '15 0 20 10',
		cls : "inline-help"
	},
	this.rigidBodyGrid.getPanel(), 
	{
		xtype : 'label',
		forId : 'myFieldId',
		text : 'Distance restraints may be imposed on the model using contacts conditions file (OPTIONAL)',
		margin : '25 0 5 10',
		cls : "inline-help"
	},
	{
		xtype : 'container',
		layout : 'hbox',
		items : [ {
			xtype : 'container',
			border : false,
			layout : 'hbox',
			items : [ {
				xtype : 'label',
				forId : 'myFieldId',
				text : 'Contact Description File: (Optional)',
				width : 150,
				margin : '10 0 0 10'
			}, {
				id : this.id + "contactsDescriptionFilePath",
				xtype : 'textfield',
				hideLabel : true,
				margin : '10 0 0 0',
				width : 200,
				disabled: true
			}, {
				text : 'Upload',
				xtype : 'button',
				margin : "10 0 0 10",
				width : 80,
				handler : function() {
					
					_this._openUploadDialog(_this.macromolecule.macromoleculeId, "CONTACTS", "Upload Contact Description File");
				}
			}, {
				text : 'Remove',
				id : _this.id + "_remove",
				xtype : 'button',
				margin : "10 0 0 10",
				width : 80,
				handler : function() {
					_this.macromolecule.contactsDescriptionFilePath = null;
					var adapter = new BiosaxsDataAdapter();
					adapter.onSuccess.attach(function(sender, proposal) {
						/** Updating the proposal global object **/
						BIOSAXS.proposal.setItems(proposal);
						_this.panel.setLoading(false);
						
						var saved = BIOSAXS.proposal.getMacromoleculeByAcronym(_this.macromolecule.acronym);
						/** Refreshing to check that everything is ok **/
						_this.refresh(saved);
						_this.onSave.notify(saved);
					});
					
					_this.panel.setLoading("Saving Macromolecule")
					adapter.saveMacromolecule(_this.macromolecule);
				}
			} ]
		} ]
	}, {
		xtype : 'panel',
		html : "<span class='inline-help' >Go to <a target='_blank' href='http://www.embl-hamburg.de/biosaxs/manuals/sasrefcvmx.html#input-format'>SASREF manual</a> for further information</span>",
		margin : "10 0 0 160",
		border : 0
		
	},
	{
		xtype : 'checkbox',
		margin : '10 0 0 5',
		boxLabel : "<span style='font-weight:bold;'>I want rigid body modeling run on this stuff</span>",
		checked : true,
		width : 300
	} ]

};

/** Because update is a jsp page we don't know if the user has uploaded a file or not  then we need to refresh **/
RigibBodyModelingForm.prototype._update = function(macromoleculeId, type, title) {
	var _this = this;
	BIOSAXS.proposal.onInitialized.attach(function() {
		if (BIOSAXS.proposal != null) {
			_this.refresh(BIOSAXS.proposal.getMacromoleculeById(_this.macromolecule.macromoleculeId));
			_this.panel.setLoading(false);
		}
	});
	this.panel.setLoading();
	BIOSAXS.proposal.init();
};

RigibBodyModelingForm.prototype._openUploadDialog = function(macromoleculeId, type, title) {
	var _this = this;
	function onClose() {
		w.destroy();
		_this._update();
		
	}

	var w = Ext.create('Ext.window.Window', {
		title : title,
		height : 200,
		width : 400,
		modal : true,
		buttons : [ {
			text : 'Close',
			handler : function() {
				onClose();
			}
		} ],
		layout : 'fit',
		items : {
			html : "<iframe style='width:500px' src='uploadPdbFileSAXS.do?reqCode=display&macromoleculeId=" + macromoleculeId + "&type=" + type + "'></iframe>"
		},
		listeners : {
			onEsc : function() {
				onClose();
			},
			close : function() {
				onClose();
			}
		}
	}).show();
};

RigibBodyModelingForm.prototype._getButtons = function() {
	return [];
};

RigibBodyModelingForm.prototype.getPanel = function() {
	var _this = this;
	this.panel = Ext.create('Ext.form.Panel', {
		width : this.width,
		height : this.height,
		margin : 10,
		border : 1,
		defaultType : 'textfield',
		items : this._getItems(),
		buttons : this._getButtons(),
		listeners : {
			afterrender : function(){
				_this._populate();
				
			}
		}
	});
	return this.panel;
};

/** Populates could be call when the DOM is not filled yet **/ 
RigibBodyModelingForm.prototype._populate = function() {
	if (this.macromolecule != null){
		if (Ext.getCmp(this.id + "contactsDescriptionFilePath") != null){
			if (this.macromolecule.contactsDescriptionFilePath != null){
				Ext.getCmp(this.id + "contactsDescriptionFilePath").setValue(this.macromolecule.contactsDescriptionFilePath);
				Ext.getCmp(this.id + "_remove").enable();
			}
			else{
				Ext.getCmp(this.id + "_remove").disable();
				Ext.getCmp(this.id + "contactsDescriptionFilePath").setValue("");
				
			}
		}
	}
};

/** It populates the form * */
RigibBodyModelingForm.prototype.refresh = function(macromolecule) {
	this.macromolecule = macromolecule;
	this.rigidBodyGrid.refresh(macromolecule);
	this._populate();
};

RigibBodyModelingForm.prototype.input = function() {
	return {};
};


/** It populates the form **/
RigibBodyModelingForm.prototype.test = function(targetId) {
	var macromoleculeForm = new RigibBodyModelingForm();
	var panel = macromoleculeForm.getPanel();
	panel.render(targetId);
};

