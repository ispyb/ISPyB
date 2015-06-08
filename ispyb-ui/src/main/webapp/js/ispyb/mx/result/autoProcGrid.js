/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * ISPyB is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P.
 * Brenchereau, M. Bodin
 ******************************************************************************/
// this grid contains the list of autoProc of the dataCollection
function AutoProcGrid(args) {

	/** Events * */
	this.onRowClicked = new Event(this);

	var contextPath = "";

	this.title = "Autoprocessing Summary (click on an entry for more details)";
	this.contextPath = "";
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
	}
	// selected Row
	this.selectedRow = null;
	// autoProc datamodel
	Ext.define('autoProcModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'autoProcId',
							mapping : 'autoProcId'
						}, {
							name : 'name',
							mapping : 'name'
						}, {
							name : 'spaceGroup',
							mapping : 'spaceGroup'
						}, {
							name : 'anomalous',
							mapping : 'anomalous'
						}, {
							name : 'cellA',
							mapping : 'cellA'
						}, {
							name : 'cellB',
							mapping : 'cellB'
						}, {
							name : 'cellC',
							mapping : 'cellC'
						}, {
							name : 'cellAlpha',
							mapping : 'cellAlpha'
						}, {
							name : 'cellBeta',
							mapping : 'cellBeta'
						}, {
							name : 'cellGamma',
							mapping : 'cellGamma'
						}]
			});
}


// no filters
AutoProcGrid.prototype.getFilterTypes = function() {
	return [];
};


// prepare data , format
AutoProcGrid.prototype._prepareData = function(data) {
	var d = [];
	if (data && data.autoProcList) {
		for (var i = 0; i < data.autoProcList.length; i++) {
			d.push(data.autoProcList[i]);
		}
	}
	return d;
};


// returns the grid
AutoProcGrid.prototype.getGrid = function(data) {
	this.features = this._prepareData(data);
	return this.renderGrid();
};


// no actions
AutoProcGrid.prototype.getActions = function() {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};

// builds the grid
AutoProcGrid.prototype.renderGrid = function() {
	var _this = this;

	/** Prepare data * */
	var columns = this._getColumns();
	//data
	this.store = Ext.create('Ext.data.Store', {
				model : 'autoProcModel',
				autoload : false,
				groupField : 'anomalous'
			});

	this._sort(this.store);
	this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);
	// group by anomalous
	var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
		groupHeaderTpl : '{columnName}: {name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
		hideGroupedHeader : true,
		startCollapsed : false
	}), groups = this.store.getGroups(), len = groups.length, i = 0, toggleGroup = function(
			item) {
		var groupName = item.text;
		if (item.checked) {
			groupingFeature.expand(groupName, true);
		} else {
			groupingFeature.collapse(groupName, true);
		}
	};
	
	// grid
	this.grid = Ext.create('Ext.grid.Panel', {
				style : {
					padding : 0
				},
				width : 600,
				model : 'autoProcModel',
				height : "100%",
				cls: 'autoproc-grid', 
				store : this.store,
				columns : columns,
				features : [groupingFeature],
				title : this.title,
				viewConfig : {
					stripeRows : true,
					enableTextSelection : true
				},
				selModel : {
					mode : 'SINGLE'
				},
				listeners : {
					itemclick : function(g, record, item, index, e, eOpts) {
						var selectedRec = g.getSelectionModel().getSelection()[0];
						_this.selectAutoProc(selectedRec.get('autoProcId'));
					}
				}
			});
	return this.grid;
};


// set the row selected
AutoProcGrid.prototype.setSelectedAutoProcId = function(autoProcIdSelected) {
	var _this = this;
	var rowIndex = -1;
	for (var i = 0; i < this.features.length; i++) {
		if (autoProcIdSelected == this.features[i].autoProcId) {
			rowIndex = i;
			break;
		}
	}
	if (rowIndex != -1) {
		_this.grid.getView().select(rowIndex);
	}
};


// select a autoProc
AutoProcGrid.prototype.selectAutoProc = function(autoProcId) {
	this.selectedRow = autoProcId;
	this.onRowClicked.notify({
				'autoProcId' : autoProcId
			});
};


// no sort
AutoProcGrid.prototype._sort = function(store) {
	// store.sort('totalIntegratedSignal', 'DESC');
};


// returns the columns 
AutoProcGrid.prototype._getColumns = function() {
	var _this = this;

	function columnWrap(value) {
		if (value)
			return '<div style="white-space:normal !important;">' + value + '</div>';
		else
			return "";
	}

	var columns = [{
				text : 'Method',
				dataIndex : 'name',
				flex : 0.4
			}, {
				text : 'Anomalous',
				dataIndex : 'anomalous',
				flex : 0.2
			}, {
				text : 'Point<br/>Group',
				dataIndex : 'spaceGroup',
				flex : 0.2
			}, {
				text : 'Cell<br/>A',
				dataIndex : 'cellA',
				flex : 0.1
			}, {
				text : 'Cell<br/>B',
				dataIndex : 'cellB',
				flex : 0.1
			}, {
				text : 'Cell<br/>C',
				dataIndex : 'cellC',
				flex : 0.1
			}, {
				text : 'Cell<br/>Alpha',
				dataIndex : 'cellAlpha',
				flex : 0.1
			}, {
				text : 'Cell<br/>Beta',
				dataIndex : 'cellBeta',
				flex : 0.1
			}, {
				text : 'Cell<br/>Gamma',
				dataIndex : 'cellGamma',
				flex : 0.1
			}];

	return columns;

};
