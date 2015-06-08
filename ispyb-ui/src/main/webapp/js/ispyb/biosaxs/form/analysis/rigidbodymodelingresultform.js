/**
 * Example form
 * 
 * @witdh
 * @height
 */
function RigidBodyModelingResultForm(args) {
	this.id = BUI.id();
	this.width = null;
	this.height = null;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	var _this = this;
	/** Widgets **/
	this.rigidModelGrid = new RigidModelGrid({
		width : null,
		height : 200
	});
	
	this.rigidModelGrid.onSelected.attach(function(sender, fits){
		var ids = [];
		for ( var i in fits) {
			ids.push(fits[i].fitStructureToExperimentalDataId);
		}
		var adapter = new BiosaxsDataAdapter();
		adapter.onSuccess.attach(function(sender, data){
//			debugger
			
		});
		
		adapter.getScatteringCurveByFrameIdsList([], [], [], ids);
	});

}

RigidBodyModelingResultForm.prototype._getItems = function() {
	var _this = this;
	return [ {
		xtype : 'label',
		forId : 'myFieldId',
		text : 'INLINE HELP: To be updated',
		margin : '15 0 20 10',
		cls : "inline-help"
	}, this.rigidModelGrid.getPanel() ]

};

RigidBodyModelingResultForm.prototype._getButtons = function() {
	return [];
};

RigidBodyModelingResultForm.prototype.getPanel = function() {
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
			afterrender : function() {
				_this._populate();

			}
		}
	});
	return this.panel;
};

/** Populates could be call when the DOM is not filled yet **/
RigidBodyModelingResultForm.prototype._populate = function() {
};

/** It populates the form * */
RigidBodyModelingResultForm.prototype.refresh = function(subtractions) {
	this.rigidModelGrid.refresh(subtractions);
};

RigidBodyModelingResultForm.prototype.input = function() {
	return {};
};

/** It populates the form **/
RigidBodyModelingResultForm.prototype.test = function(targetId) {
	var macromoleculeForm = new RigidBodyModelingResultForm();
	var panel = macromoleculeForm.getPanel();
	panel.render(targetId);
};
