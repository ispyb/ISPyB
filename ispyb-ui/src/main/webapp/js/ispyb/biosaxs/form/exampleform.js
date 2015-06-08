/**
 * Example form
 * 
 * @witdh
 * @height
 */
function ExampleForm(args) {
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
}

ExampleForm.prototype._getItems = function() {
	return [{
		fieldLabel : 'First Name',
		name : 'first',
		allowBlank : false
	}, {
		fieldLabel : 'Last Name',
		name : 'last',
		allowBlank : false
	} ];
};

ExampleForm.prototype._getItems = function() {
	return [ ];
};

ExampleForm.prototype._getButtons = function() {
	return [ ];
};

ExampleForm.prototype.getPanel = function() {
	this.panel = Ext.create('Ext.form.Panel', {
		width : this.width,
		height : this.height,
		margin : 10,
		border : 1,
		defaultType : 'textfield',
		items : this._getItems(),
		buttons : this._getButtons()
	});
	return this.panel;
};

/** It populates the form **/
ExampleForm.prototype.refresh = function(macromolecule) {
};
