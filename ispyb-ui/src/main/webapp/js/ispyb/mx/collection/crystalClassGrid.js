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

/*global Event */
/*global Ext */

// crystal class Grid : summury of the nb Collect/crystal class
function CrystalClassGrid(args) {
	var isFx = false;
	var contextPath = "";
	this.title = "Summary";
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.title != null) {
			this.title = args.title;
		}
	}

	// data model
	Ext.define('CrystalClassModel', {
			extend : 'Ext.data.Model',
			fields : [{
					name : 'crystalClassCode',
					mapping : 'crystalClassCode'
				}, {
					name : 'crystalClassName',
					mapping : 'crystalClassName'
				}, {
					name : 'numberOfCrystals',
					mapping : 'numberOfCrystals'
				}],
			idProperty : 'crystalClassCode'
		});
}

// no filters
CrystalClassGrid.prototype.getFilterTypes = function () {
	return [];
};

// refresh data: reload
CrystalClassGrid.prototype.refresh = function (data) {
	this.features = data.listCrystal;
	this.store.loadData(this.features, false);
};

// sort by default
CrystalClassGrid.prototype._sort = function (store) {
	store.sort('crystalClassName', 'ASC');
};

// gets the grid
CrystalClassGrid.prototype.getGrid = function (listCrystal) {
	this.features = listCrystal;
	return this.renderGrid(listCrystal);
};

// builds the grid
CrystalClassGrid.prototype.renderGrid = function () {
	var _this = this;

	/** Prepare data * */
	var data = [];
	// builds columns
	var columns = this._getColumns();
	// grid data
	this.store = Ext.create('Ext.data.Store', {
			model : 'CrystalClassModel',
			autoload : false
		});

	this._sort(this.store);
	this.store.loadData(this.features, false);
	// if no data
	var noCrystalDataPanel = Ext.create('Ext.Panel', {
			id : 'noCrystalDataPanel',
			layout : {
				type : 'fit', // Arrange child items vertically
				align : 'center', // Each takes up full width
				bodyPadding : 0,
				border : 0
			},
			html : ''
		});

	if (this.features.length == 0) { 
		return noCrystalDataPanel;
	}

	// build the grid
	this.grid = Ext.create('Ext.grid.Panel', {
			style : {
				padding : 0,
				marginTop : '10px'
			},
			model : 'CrystalClassModel',
			store : this.store,
			columns : columns,
			title : this.title,
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

// build the columns
CrystalClassGrid.prototype._getColumns = function () {
	var _this = this;

	var columns = [{
			text : 'Crystal class name',
			dataIndex : 'crystalClassName',
			flex : 0.2
		}, {
			text : 'Crystal class code',
			dataIndex : 'crystalClassCode',
			flex : 0.1
		}, {
			text : 'Number of crystals',
			dataIndex : 'numberOfCrystals',
			flex : 0.1
		}];
	return columns;

};
