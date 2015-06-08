
function AbinitioWindow(args) {

}

AbinitioWindow.prototype.getGrid = function(abinitios) {
	this.grid = new AbinitioGrid().getPanel(abinitios);
	return this.grid;
};

AbinitioWindow.prototype.show = function(models) {
	Ext.create('Ext.window.Window', {
		title : 'Abinitio Models',
		height : 500,
		width : 800,
		layout : 'fit',
		items : [ this.getGrid(models) ]
	}).show();
};
