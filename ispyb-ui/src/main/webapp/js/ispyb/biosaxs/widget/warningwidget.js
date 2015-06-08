function WarningWidget(args) {
	this.actions = [];

	this.width = 580;
	this.height = 380;
	this.title = "Warning";
	this.message = null;
	this.messageType = 1; // "TIP",  "WARNING", "ERROR" -> 0,1,2
	this.pageSize = 3;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.title != null) {
			this.title = args.title;
		}

		if (args.messageType != null) {
			this.messageType = args.messageType;
		}

		if (args.message != null) {
			this.message = args.message;
		}

	}
	this.onVertexUp = new Event(this);
}

WarningWidget.prototype.draw = function(warning) {
	var _this = this;
	var myData = [];
	for ( var i = 0; i < warning.length; i++) {
		myData.push({
			warnings : warning[i]
		});
	}

	var store = Ext.create('Ext.data.Store', {
		fields : [ 'warnings' ],
		remoteSort : true,
		pageSize : this.pageSize,
		data : myData,
		proxy : {
			type : 'pagingmemory'

		}
	});

	var grid = Ext.createWidget('gridpanel', {
		store : store,
		height : 264,
		border : 0,
		flex : 1,
		columns : [ {
			text : 'Description',
			sortable : true,
			dataIndex : 'warnings',
			flex : 1
		} ],

		bbar : Ext.create('Ext.PagingToolbar', {
			pageSize : this.pageSize,
			store : store,
			displayInfo : true,
			plugins : Ext.create('Ext.ux.SlidingPager', {})
		})
	});

	if (this.message == null) {
		this.height = this.height - 55;
	}

	var items = [];
	if (this.message != null) {
		if (this.messageType == 0) {
			items.push({
				padding : "0 0 0 2",
				border : 0,
				html : BUI.getTipHTML(this.message)
			});
		}

		if (this.messageType == 1) {
			items.push({
				padding : "0 0 0 2",
				border : 0,
				html : BUI.getWarningHTML(this.message)
			});
		}

		if (this.messageType == 2) {
			items.push({
				padding : "0 0 0 2",
				border : 0,
				html : BUI.getErrorHTML(this.message)
			});
		}
	}
	items.push(grid);

	this.window = Ext.create('Ext.Window', {
		title : this.title,
		//resizable			: false,
		//constrain			: true,
		//closable			: true,
		items : items,
		height : this.height,
		width : this.width,
		buttonAlign : 'right',
		listeners : {
			scope : this,
			minimize : function() {
				this.window.hide();
			},
			destroy : function() {
				delete this.window;
			}
		},
		buttons : [ {
			text : 'Close',
			handler : function() {
				_this.window.hide();
			}
		} ]
	});
	this.window.show();
};
