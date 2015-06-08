
function SuperpositionGrid(args) {
	this.height = null;
	this.width = null;
	this.id = BUI.id();

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;

			if (args.width != null) {
				this.width = args.width;
			}
		}
	}
	
	this.onSelected = new Event(this);
}

SuperpositionGrid.prototype._prepareData = function(data) {
	return data;
};

SuperpositionGrid.prototype.refresh = function(subtractions) {
	var data  = [];
	if (subtractions != null){
		if (subtractions.length > 0){
			for (j in subtractions){
				var subtraction = subtractions[j];
				if (subtraction.superposition3VOs != null){
					for (i in subtraction.superposition3VOs){
						data.push(subtraction.superposition3VOs[i]);
					}
				}
			}
		}
	}
	this.store.loadData(data);
};

SuperpositionGrid.prototype.getPanel = function() {
	var _this = this;
	this.store = Ext.create('Ext.data.Store', {
		fields : [ 'abinitioModelPdbFilePath', 'aprioriPdbFilePath', 'alignedPdbFilePath' ],
		data : []
	});

	this.selModel = Ext.create('Ext.selection.RowModel', {
		allowDeselect : true,
		mode : 'MULTI',
		listeners : {
			selectionchange : function(sm, selections) {
				var selected = [];
				for (var i = 0; i < selections.length; i++) {
					selected.push(selections[i].raw);
				}
				_this.onSelected.notify(selected);
			}
		}
	});
	
	this.store.sort([ {
		property : 'name',
		direction : 'ASC'
	} ]);

	this.panel = Ext.create('Ext.grid.Panel', {
		store : this.store,
		selModel : this.selModel,
		width : this.width,
		height : this.height,
		margin : 10,
		deferredRender : false,
		columns : [ {
			text : 'Abinitio',
			dataIndex : 'abinitioModelPdbFilePath',
			hidden : false,
			flex : 1,
			renderer : function(val){
				return BUI.getFileName(val);
			}
		}, {
			text : 'Apriori',
			dataIndex : 'aprioriPdbFilePath',
			hidden : false,
			flex : 1,
			renderer : function(val){
				return BUI.getFileName(val);
			}
		}, {
			text : 'Aligned',
			dataIndex : 'alignedPdbFilePath',
			hidden : false,
			flex : 1,
			renderer : function(val){
				return BUI.getFileName(val);
			}
		} ],

		viewConfig : {
			enableTextSelection : true,
			preserveScrollOnRefresh : true
		},
		listeners : {
			cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {

			},
			afterrender : function() {
			}
		}
	});
	return this.panel;
};
