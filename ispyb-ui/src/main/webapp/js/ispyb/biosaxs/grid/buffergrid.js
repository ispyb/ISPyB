/**
 * It shows buffer grid with a top bar with "Add" button
 * 
 * @height
 * @searchBar
 * @collapsed
 * @width
 */
function BufferGrid(args) {
	this.height = 500;
	this.searchBar = false;
	this.tbar = false;
	this.collapsed = false;
	this.id = BUI.id();

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.searchBar != null) {
			this.searchBar = args.searchBar;
		}

		if (args.tbar != null) {
			this.tbar = args.tbar;
		}

		if (args.collapsed != null) {
			this.collapsed = args.collapsed;
		}

		if (args.width != null) {
			this.width = args.width;
		}
	}
}

BufferGrid.prototype._edit = function(bufferId) {
	var _this = this;
	var window = new BufferWindow();
	window.onSuccess.attach(function(sender, proposal) {
		_this.store.loadData(BIOSAXS.proposal.getBuffers());
	});
	window.draw(BIOSAXS.proposal.getBufferById(bufferId));
};

BufferGrid.prototype.refresh = function(buffers) {
	this.store.loadData(this._prepareData(buffers), false);
};

BufferGrid.prototype._prepareData = function(buffers) {
	return buffers;
};

BufferGrid.prototype._getTbar = function() {
	var _this = this;
	var actions = [];

	actions.push(Ext.create('Ext.Action', {
		icon : '../images/add.png',
		text : 'Add buffer',
		disabled : false,
		handler : function(widget, event) {
			var window = new BufferWindow();
			window.onSuccess.attach(function(sender) {
				_this.store.loadData(BIOSAXS.proposal.getBuffers());
			});
			window.draw({});
		}
	}));
	return actions;
};

BufferGrid.prototype.getPanel = function(buffers) {
	var _this = this;

	this.store = Ext.create('Ext.data.Store', {
		fields : [ 'bufferId', 'acronym', 'name', 'composition' ],
		data : buffers
	});

	this.store.sort('acronym');

	var type = 'Ext.grid.Panel';
	if (this.searchBar == true) {
		type = 'Ext.ux.LiveSearchGridPanel';
	}

	this.grid = Ext.create(type, {
		title : 'Buffers',
		collapsible : true,
		collapsed : this.collapsed,
		store : this.store,
		height : this.height,
		width : this.width,
		columns : [ 
		/*{
			text : '',
			dataIndex : 'bufferId',
			width : 20,
			renderer : function(val, y, sample) {
				return BUI.getRectangleColorDIV(BIOSAXS.proposal.bufferColors[val], 10, 10);
			}
		},*/ 
		{
			text : 'Acronym',
			dataIndex : 'acronym',
			flex : 1
		}, {
			text : 'Name',
			dataIndex : 'name',
			flex : 1,
			hidden : true
		}, {
			text : 'Composition',
			dataIndex : 'composition',
			flex : 1,
			hidden : true
		}, {
			id : _this.id + 'buttonEditBuffer',
			width : 80,
			sortable : false,
			renderer : function(value, metaData, record, rowIndex, colIndex, store) {
				return BUI.getGreenButton('EDIT');
			}
		}, {
			id : _this.id + 'buttonRemoveBuffer',
			width : 85,
			hidden : true,
			sortable : false,
			renderer : function(value, metaData, record, rowIndex, colIndex, store) {
				return BUI.getRedButton('REMOVE');
			}
		} ],
		flex : 1,
		viewConfig : {
			stripeRows : true,
			listeners : {
				'celldblclick' : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
					_this._edit(record.data.bufferId);
				},
				'cellclick' : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonEditBuffer') {
						_this._edit(record.data.bufferId);
					}

					if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonRemoveBuffer') {
						BUI.showBetaWarning();
					}
				}

			}
		}
	});

	/** Adding the tbar **/
	if (this.tbar) {
		this.grid.addDocked({
			xtype : 'toolbar',
			items : this._getTbar()
		});
	}
	return this.grid;
};

BufferGrid.prototype.input = function() {
	return new MacromoleculeGrid().input();
};

BufferGrid.prototype.test = function(targetId) {
	var bufferGrid = new BufferGrid({
		width : 800,
		height : 350,
		collapsed : false,
		tbar : true
	});

	BIOSAXS.proposal = new Proposal(bufferGrid.input().proposal);
	var panel = bufferGrid.getPanel(BIOSAXS.proposal.macromolecules);
	panel.render(targetId);
};
