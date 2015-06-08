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
// Grid with the autoProc status
function AutoProcStatusGrid(args) {

	var contextPath = "";

	this.title = "Status History";
	this.contextPath = "";
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
	}

	this.width = 700;

	// status datamodel
	Ext.define('statusModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'blTimeStamp',
							mapping : 'blTimeStamp',
							type : 'date',
							dateFormat : 'd-m-Y H:i:s'
						}, {
							name : 'step',
							mapping : 'step'
						}, {
							name : 'status',
							mapping : 'status'
						}, {
							name : 'comments',
							mapping : 'comments'
						}]
			});
}

// no filter
AutoProcStatusGrid.prototype.getFilterTypes = function() {
	return [];
};

// get the grid
AutoProcStatusGrid.prototype.getGrid = function(data) {
	this.features = data;
	return this.renderGrid(this.features);
};


// no actions
AutoProcStatusGrid.prototype.getActions = function() {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};


// builds the grid
AutoProcStatusGrid.prototype.renderGrid = function() {
	var _this = this;

	/** Prepare data * */
	var columns = this._getColumns();
	this.store = Ext.create('Ext.data.Store', {
				model : 'statusModel',
				autoload : false
			});

	this._sort(this.store);
	this.store.loadData(this.features, false);

	this.grid = Ext.create('Ext.grid.Panel', {
				style : {
					padding : 0
				},
				width : this.width,
				model : 'statusModel',
				height : "100%",
				store : this.store,
				columns : columns,
				// title : this.title,
				viewConfig : {
					stripeRows : true,
					enableTextSelection : true
				},
				selModel : {
					mode : 'SINGLE'
				}

			});
	return this.grid;
};


// no sort by default
AutoProcStatusGrid.prototype._sort = function(store) {
	// store.sort('totalIntegratedSignal', 'DESC');
};


// get the columns
AutoProcStatusGrid.prototype._getColumns = function() {
	var _this = this;

	function columnWrap(value) {
		if (value)
			return '<div style="white-space:normal !important;">' + value+ '</div>';
		else
			return "";
	}

	var columns = [{
				text : 'Date',
				dataIndex : 'blTimeStamp',
				flex : 0.2,
				renderer : Ext.util.Format.dateRenderer('d-m-Y H:i:s')
			}, {
				text : 'Step',
				dataIndex : 'step',
				flex : 0.1
			}, {
				text : 'Status',
				dataIndex : 'status',
				flex : 0.1
			}, {
				text : 'Comments',
				dataIndex : 'comments',
				flex : 0.3
			}];

	return columns;

};


// autoProc has been selected => update the data
AutoProcStatusGrid.prototype.setSelectedAutoProc = function(data) {
	this.features = data;
	this.store.loadData(this.features, false);
};
