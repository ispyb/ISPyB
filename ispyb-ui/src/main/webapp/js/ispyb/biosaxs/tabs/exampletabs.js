/** 
 * Example of a tab panel to be populated with widgets 
 * 
 * @width width in pixels
 * @height height in pixels
 * 
 * #myEvent event that this class is supposed to throw
 * 
 **/
function ExampleTabs(args) {
	this.width = 500;
	this.height = 500;
	
	if (args != null){
		if (args.width != null){
			this.width= args.width;
		}
		if (args.height != null){
			this.height= args.height;
		}
	}
	
	/** Events **/
	this.myEvent = new Event();
}

/** Populate the widget **/
ExampleTabs.prototype.refresh = function(macromolecule) {
	
};

/** It creates a tab panel with the specified with and height **/
ExampleTabs.prototype.getPanel = function() {
	this.panel = Ext.createWidget('tabpanel', {
		height  : this.height,
		width	: this.width,
		style : {
			padding : 2
		},
		items : [ {
			tabConfig : {
				title : 'Test',
				icon : '/ispyb/images/plane-small.gif'
			},
			items : [ {
				xtype : 'container',
				margin : '5 5 5 5',
				items : [ ]
			} ]
		} ]
	});
	return this.panel;
};
