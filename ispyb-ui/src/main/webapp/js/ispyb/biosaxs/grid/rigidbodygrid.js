
function RigidModelGrid(args) {
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


RigidModelGrid.prototype.refresh = function(subtractions) {
	var data  = [];
	if (subtractions != null){
		if (subtractions.length > 0){
			for (j in subtractions){
				var subtraction = subtractions[j];
				if (subtraction.rigidBodyModeling3VOs != null){
					for (i in subtraction.rigidBodyModeling3VOs){
						data.push(subtraction.rigidBodyModeling3VOs[i]);
					}
				}
			}
		}
	}
	this.store.loadData(data);
};

RigidModelGrid.prototype.getPanel = function() {
	var _this = this;
	this.store = Ext.create('Ext.data.Store', {
		fields : [ 'symmetry', 'subtractionId', 'subUnitConfigFilePath', 'rigidBodyModelingId', 'rigidBodyModelFilePath', 'logFilePath', 'fitFilePath', 'curveConfigFilePath',
				'crossCorrConfigFilePath', 'contactDescriptionFilePath' ],
		data : []
	});

	this.store.sort([ {
		property : 'name',
		direction : 'ASC'
	} ]);

	this.panel = Ext.create('Ext.grid.Panel', {
		store : this.store,
		width : this.width,
		height : this.height,
		margin : 10,
		deferredRender : false,
		columns : [ {
			text : 'RBM',
			dataIndex : 'rigidBodyModelFilePath',
			hidden : false,
			flex : 1,
			renderer : function(val){
				return BUI.getFileName(val);
			}
		}, {
			text : 'Sub Unit Conf.',
			dataIndex : 'subUnitConfigFilePath',
			hidden : true,
			flex : 1,
			renderer : function(val){
				return BUI.getFileName(val);
			}
		}, {
			text : 'Log',
			dataIndex : 'logFilePath',
			hidden : true,
			flex : 1,
			renderer : function(val){
				return BUI.getFileName(val);
			}
		}, {
			text : 'Fit',
			dataIndex : 'fitFilePath',
			hidden : false,
			flex : 1,
			renderer : function(val){
				return BUI.getFileName(val);
			}
		}, {
			text : 'Curve Conf.',
			dataIndex : 'curveConfigFilePath',
			hidden : false,
			flex : 1,
			renderer : function(val){
				return BUI.getFileName(val);
			}
		}, {
			text : 'Cross Corr.',
			dataIndex : 'crossCorrConfigFilePath',
			hidden : false,
			flex : 1,
			renderer : function(val){
				return BUI.getFileName(val);
			}
		}, {
			text : 'Contact Desc.',
			dataIndex : 'contactDescriptionFilePath',
			hidden : true,
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

