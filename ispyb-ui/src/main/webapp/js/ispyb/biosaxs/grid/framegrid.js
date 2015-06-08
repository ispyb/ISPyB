/**
 * It shows buffer grid with a top bar with "Add" button
 * 
 * @height
 * @searchBar
 * @collapsed
 * @width
 */
function FrameGrid(args) {
	this.height = 500;
	this.width = 500;
	this.searchBar = false;
	this.tbar = false;
	this.collapsed = false;
	
	
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

FrameGrid.prototype._edit = function(bufferId) {
	var _this = this;
	var window = new BufferWindow();
	window.onSuccess.attach(function(sender, proposal) {
		_this.store.loadData(BIOSAXS.proposal.getBuffers());
	});
	window.draw(BIOSAXS.proposal.getBufferById(bufferId));
};

FrameGrid.prototype.refresh = function(buffers, experimentId) {
	this.experimentId = experimentId;
	this.store.loadData(this._prepareData(buffers), false);
};

FrameGrid.prototype._prepareData = function(buffers) {
	return buffers;
};

FrameGrid.prototype._getTbar = function() {
	var _this = this;
	var actions = [];

	actions.push(Ext.create('Ext.Action', {
		icon : '../images/add.png',
		text : 'Add Buffer',
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

FrameGrid.prototype.getPanel = function(buffers) {
	var _this = this;

	this.store = Ext.create('Ext.data.Store', {
		fields : [ 'frameNumber', 'I0', 'Rg', 'Mass', 'Vc', 'Qr', 'quality'],
		data : buffers
	});

	this.store.sort('frameNumber');

	var type = 'Ext.grid.Panel';
	if (this.searchBar == true) {
		type = 'Ext.ux.LiveSearchGridPanel';
	}

	this.grid = Ext.create(type, {
		store : this.store,
		height : this.height,
		width : this.width,
		margin	: 5,
		columns : [{
			text : 'Frame',
			dataIndex : 'frameNumber',
			flex : 1
		},{
			text : 'I0',
			dataIndex : 'I0',
			flex : 1,
			renderer : function(val, y, sample) {
				return BUI.formatValuesUnits(sample.data.I0, "", 12, 3);
			}
		},{
			text : 'Rg',
			dataIndex : 'Rg',
			flex : 1,
			renderer : function(val, y, sample) {
				return BUI.formatValuesUnits(sample.data.Rg, "nm", 12, 3);
			}
		},{
			text : 'Mass',
			dataIndex : 'Mass',
			flex : 1,
			renderer : function(val, y, sample) {
				return BUI.formatValuesUnits(sample.data.Mass, "", 12, 3);
			}
		},{
			text : 'Vc',
			dataIndex : 'Vc',
			flex : 1,
			renderer : function(val, y, sample) {
				return BUI.formatValuesUnits(sample.data.Vc, "", 12, 3);
			}
		},{
			text : 'Qr',
			dataIndex : 'Qr',
			flex : 1,
			renderer : function(val, y, sample) {
				return BUI.formatValuesUnits(sample.data.Qr, "", 12, 3);
			}
		},{
			text : 'Quality',
			dataIndex : 'quality',
			flex : 1,
			renderer : function(val, y, sample) {
				return (Number(sample.data.quality) * 100).toFixed(2) + "%";
			}
		},
		{
			text : '',
			renderer : function(val, x, sample) {
				if (sample != null) {
					return BUI.getZipHTMLByFrameRangeId(_this.experimentId, sample.raw.frameNumber, sample.raw.frameNumber);
				}
			},
			width : 100
		}],
		flex : 1,
		viewConfig : {
			stripeRows : true
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

FrameGrid.prototype.input = function() {
	return [];
};

FrameGrid.prototype.test = function(targetId) {
	var frameGrid = new FrameGrid({
		width : 800,
		height : 350,
		collapsed : false,
		tbar : true
	});

	var panel = frameGrid.getPanel([]);
	panel.render(targetId);
};


function PeakGrid(args) {
	this.height = 500;
	this.searchBar = false;
	this.tbar = false;
	this.collapsed = false;
	this.width = 500;
	this.showExtendedColumns = false;
	
	if (args != null) {
		if (args.showExtendedColumns != null) {
			this.showExtendedColumns = args.showExtendedColumns;
		}
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


PeakGrid.prototype.refresh = function(buffers) {
	this.store.loadData(this._prepareData(buffers), false);
};

PeakGrid.prototype._prepareData = function(buffers) {
	
//	for ( var i = 0; i < buffers.length; i++) {
//		buffers[i].name = "Peak #" + (i+1);
//	}
	/** Adding information of buffer **/
	if (buffers.length > 0){
		buffers.unshift({
			name	: "BUFFER",
			start	: 0,
			experimentId	: buffers[0].experimentId,
			end		: buffers[0].start - 1
		});
	}
	
	
	return buffers;
};

PeakGrid.prototype._getTbar = function() {
	var _this = this;
	var actions = [];

	actions.push(Ext.create('Ext.Action', {
		icon : '../images/add.png',
		text : 'Add Buffer',
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

PeakGrid.prototype.getPanel = function(buffers) {
	var _this = this;
	this.peakCount = 0;
	this.store = Ext.create('Ext.data.Store', {
		fields : ['name', {name:'start', type : 'int'}, {name:'end', type : 'int'}],
		data : buffers
		
	});
	
	this.store.sort('start');

	var type = 'Ext.grid.Panel';
	if (this.searchBar == true) {
		type = 'Ext.ux.LiveSearchGridPanel';
	}
	this.grid = Ext.create(type, {
		store : this.store,
		height : this.height,
		width : this.width,
		margin	: 5,
		columns : [
		           {
		        	   text : '',
		        	   dataIndex : 'name',
		        	   width : 100,
		        	   hidden : _this.showExtendedColumns,
		        	   renderer : function(val, y, sample) {
		        		   if (sample.data.name == "BUFFER")
		        			   return "BUFFER";
		        		    _this.peakCount++;
							return  "Peak #" + _this.peakCount;
						}
		        	  
		        	   
		           },
		           {
						text : 'Peaks',
						dataIndex : 'start',
						sortable : true,
						width : 100,
						hidden: !_this.showExtendedColumns,
						renderer : function(val, y, sample) {
							return "From " + Number(sample.raw.start) + " to " + Number(sample.raw.end);
						}
					},
		           {
		   			text : 'Frames',
		   			hidden : _this.showExtendedColumns,
		   			columns : [
									{
										text : 'Start',
										dataIndex : 'start',
										sortable : true,
										hidden : _this.showExtendedColumns,
										width : 100,
										renderer : function(val, y, sample) {
											return Number(val);
										}
									},{
										text : 'End',
										dataIndex : 'end',
										width : 100,
										hidden : _this.showExtendedColumns,
										renderer : function(val, y, sample) {
											return Number(val);
										}
									},{
										text : 'Total',
										dataIndex : 'end',
										width : 100,
										hidden : _this.showExtendedColumns,
										renderer : function(val, y, sample) {
											return "<span style='font-style:italic'>" + (sample.raw.end - sample.raw.start) + " frames</span>";
										}
									}
					]
		           }
//					,
//					{
//						text : '',
//						  hidden : _this.showExtendedColumns,
//						renderer : function(val, x, sample) {
//							if (sample != null) {
//								return BUI.getZipHTMLByFrameRangeId(sample.raw.experimentId, sample.raw.start, sample.raw.end);
//							}
//						},
//						width : 100
//					}
		           ],
		flex : 1,
		viewConfig : {
			stripeRows : true,
			getRowClass : function(record, rowIdx, params, store) {
				if (record.raw.name == "BUFFER") {
					return "blue-row";
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

PeakGrid.prototype.input = function() {
	return [];
};

PeakGrid.prototype.test = function(targetId) {
	var PeakGrid = new PeakGrid({
		width : 800,
		height : 350,
		collapsed : false,
		tbar : true
	});

	var panel = PeakGrid.getPanel([]);
	panel.render(targetId);
};
