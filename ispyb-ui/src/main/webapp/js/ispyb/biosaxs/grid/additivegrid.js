
function AdditiveGrid(args) {
	this.onRemoveButtonClicked = new Event(this);
}

AdditiveGrid.prototype.getBuffer = function() {
	return this.buffer;
};

AdditiveGrid.prototype._getActions = function() {
	var _this = this;
	/** Actions buttons **/
	var actions = [];

	actions.push(Ext.create('Ext.Action', {
		icon : '../images/add.png',
		text : 'Add',
		disabled : true,
		alwaysEnabled : true,
		handler : function(widget, event) {
			_this.buffer.bufferhasadditive3VOs.push(BIOSAXS_BEANS.getBufferhasAdditive3VO());
			_this.refresh(_this.buffer, _this.experiment);
		}
	}));

	return actions;
};

AdditiveGrid.prototype.refresh = function(buffer, experiment) {
	this.buffer = buffer;
	this.experiment = experiment;
	if (buffer) {
		if (buffer.bufferhasadditive3VOs) {
			this.features = buffer.bufferhasadditive3VOs;
		}
	}
	this.experiment = experiment;
	this.store.loadData(this._prepareData(), false);
};

AdditiveGrid.prototype.getAdditives = function() {
	var additives = [];
	for ( var i = 0; i < this.store.getCount(); i++) {
		var bufferHasAdditive = BIOSAXS_BEANS.getBufferhasAdditive3VO();
		var data = this.store.getAt(i).getData();

		bufferHasAdditive.additive3VO.name = data.name;
		bufferHasAdditive.additive3VO.comments = data.comments;
		bufferHasAdditive.additive3VO.additiveType = data.additiveType;

		bufferHasAdditive.bufferId = this.buffer.bufferId;
		bufferHasAdditive.bufferHasAdditiveId = data.bufferHasAdditiveId;
		bufferHasAdditive.quantity = data.quantity;
		additives.push(bufferHasAdditive);
	}

	return additives;
};

AdditiveGrid.prototype._getFields = function(buffers) {
	var columns = [

		{
			header : 'Name',
			dataIndex : 'name',
			type : 'string',
			editor : {
				allowBlank : true
			},
			flex : 1
		},
		{
			header : 'Type',
			name : 'additiveType',
			dataIndex : 'additiveType',
			editor : {
				xtype : 'combobox',
				typeAhead : true,
				triggerAction : 'all',
				selectOnTab : true,
				store : BIOSAXS.proposal.getAdditiveTypes(),
				lazyRender : true,
				listClass : 'x-combo-list-small'
			},
			flex : 0.6
		},
		{
			header : 'Quantity',
			dataIndex : 'quantity',
			name : 'quantity',
			editor : {
				allowBlank : true
			},
			type : 'string',
			flex : 1
		},
		{
			xtype : 'actioncolumn',
			items : [ {
				icon : '../images/cancel.png',
				tooltip : 'Delete additive',
				scope : this,
				handler : function(grid, rowIndex, colIndex) {
					if ((grid.getStore().getAt(rowIndex).data.bufferHasAdditiveId == null)|| (grid.getStore().getAt(rowIndex).data.bufferHasAdditiveId == "")) {
						grid.getStore().removeAt(rowIndex);
					} else {
						this.onRemoveButtonClicked.notify({
							'bufferId' : this.buffer.bufferId,
							'bufferHasAdditiveId' : this.store.getAt(rowIndex).data.bufferHasAdditiveId
						});
					}
				}
			} ]
		} ];

	return columns;
};

AdditiveGrid.prototype._prepareData = function() {
	var data = [];
	if (this.features == null) {
		this.features = [];
	}
	for (var i = 0; i < this.features.length; i++) {
		var object = this.features[i];
		object.name = this.features[i].additive3VO.name;
		object.additiveType = this.features[i].additive3VO.additiveType;
		object.comments = this.features[i].additive3VO.comments;
		object.additiveId = this.features[i].additive3VO.additiveId;
		data.push(object);
	}
	return data;
};

AdditiveGrid.prototype.getPanel = function(buffer, experiment) {
	this.buffer = buffer;
	this.features = buffer.bufferhasadditive3VOs;
	this.experiment = experiment;
	return this._renderGrid();
};

AdditiveGrid.prototype.getStore = function() {
	var _this = this;
	var store = Ext.create('Ext.data.Store', {
		fields : [ "name", "additiveType", "comments", "additiveId", "bufferHasAdditiveId", "quantity" ],
		autoload : false,
		data : this._prepareData(),
		listeners : {
			update : function(store, record) {
				record.index = _this.grid.getSelectionModel().getCurrentPosition().row;
				_this.buffer.bufferhasadditive3VOs[record.index].additive3VO.name = record.data.name;
				_this.buffer.bufferhasadditive3VOs[record.index].additive3VO.additiveType = record.data.additiveType;
				_this.buffer.bufferhasadditive3VOs[record.index].additive3VO.comments = record.data.comments;
				_this.buffer.bufferhasadditive3VOs[record.index].quantity = record.data.quantity;
			}
		}
	});

	//	store.sort('bufferHasAdditiveId', 'ASC');
	store.loadData(this._prepareData(), false);
	return store;
};

AdditiveGrid.prototype._renderGrid = function() {
	var _this = this;
	this.store = this.getStore();

	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
		clicksToEdit : 1
	});

	this.grid = Ext.create('Ext.grid.Panel', {
		dockedItems : [ {
			xtype : 'toolbar',
			items : this._getActions()
		} ],
		store : this.store,
		height : 230,
		title : "Additives",
		width : "100%",
		columns : _this._getFields(),
		plugins : [ cellEditing ],
		viewConfig : {
			stripeRows : true,
			listeners : {
				itemcontextmenu : function(view, rec, node, index, e) {
					e.stopEvent();
					contextMenu.showAt(e.getXY());
					return false;
				}
			}
		},
		selModel : {
			mode : 'SINGLE'
		}
	});

	return this.grid;
};

AdditiveGrid.prototype.input = function() {
};

AdditiveGrid.prototype.test = function(targetId) {
	var grid = new AdditiveGrid();
	var panel = grid.getPanel([]);
	panel.render(targetId);

};
