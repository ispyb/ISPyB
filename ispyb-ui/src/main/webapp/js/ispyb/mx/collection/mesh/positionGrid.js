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
// position grid 
function PositionGrid(args) {

	var contextPath = "";

	this.title = "Positions";
	this.contextPath = "";
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.title != null) {
			this.title = args.title;
		}
		if (args.listOfCrystalClass != null) {
			this.listOfCrystalClass = args.listOfCrystalClass;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
	}

	// datamodel
	Ext.define('positionModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'dataCollectionId',
							mapping : 'dataCollectionId'
						}, {
							name : 'imagePrefix',
							mapping : 'imagePrefix'
						}, {
							name : 'dataCollectionNumber',
							mapping : 'dataCollectionNumber'
						}, {
							name : 'phiX',
							mapping : 'phiX'
						}, {
							name : 'phiY',
							mapping : 'phiY'
						}, {
							name : 'phiZ',
							mapping : 'phiZ'
						}, {
							name : 'sampX',
							mapping : 'sampX'
						}, {
							name : 'sampY',
							mapping : 'sampY'
						}, {
							name : 'omega',
							mapping : 'omega'
						}, {
							name : 'kappa',
							mapping : 'kappa'
						}, {
							name : 'phi',
							mapping : 'phi'
						}, {
							name : 'gridIndexY',
							mapping : 'gridIndexY'
						}, {
							name : 'gridIndexZ',
							mapping : 'gridIndexZ'
						}, {
							name : 'spotTotal',
							mapping : 'spotTotal'
						}, {
							name : 'goodBraggCandidates',
							mapping : 'goodBraggCandidates'
						}, {
							name : 'method1Res',
							mapping : 'method1Res'
						}, {
							name : 'method2Res',
							mapping : 'method2Res'
						}, {
							name : 'totalIntegratedSignal',
							mapping : 'totalIntegratedSignal'
						},
						{
							name : 'dozor_score',
							mapping : 'dozor_score'
						}

				],
				idProperty : 'dataCollectionId'
			});
}

// no filters
PositionGrid.prototype.getFilterTypes = function() {
	return [];
};

// format data
PositionGrid.prototype._prepareData = function(data) {
	var d = [];
	for (var i = 0; i < data.length; i++) {
		var dataPos = new Object();
		dataPos = data[i];
		dataPos.phiX = data[i].motorPosition.phiX;
		dataPos.phiY = data[i].motorPosition.phiY;
		dataPos.phiZ = data[i].motorPosition.phiZ;
		dataPos.sampX = data[i].motorPosition.sampX;
		dataPos.sampY = data[i].motorPosition.sampY;
		dataPos.omega = data[i].motorPosition.omega;
		dataPos.kappa = data[i].motorPosition.kappa;
		dataPos.phi = data[i].motorPosition.phi;
		dataPos.gridIndexY = data[i].motorPosition.gridIndexY;
		dataPos.gridIndexZ = data[i].motorPosition.gridIndexZ;
		d.push(dataPos);
	}

	return d;
}
// get grids;

PositionGrid.prototype.getGrid = function(meshData) {
	this.features = this._prepareData(meshData);
	return this.renderGrid(this.features);
};

// no actions
PositionGrid.prototype.getActions = function() {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};

// builds the gris
PositionGrid.prototype.renderGrid = function() {
	var _this = this;

	/** Prepare data * */
	// builds columns
	var columns = this._getColumns();
	// grid data
	this.store = Ext.create('Ext.data.Store', {
				model : 'positionModel',
				autoload : false
			});

	this._sort(this.store);
	this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);

	this.title = "(" + this.features.length + ") Positions";

	// build grid
	this.grid = Ext.create('Ext.grid.Panel', {
				style : {
					padding : 0
				},
				width : "100%",
				model : 'positionModel',
				height : "100%",
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

// default sort
PositionGrid.prototype._sort = function(store) {
	store.sort('dozor_score', 'DESC');
};

// build columns
PositionGrid.prototype._getColumns = function() {
	var _this = this;

	function columnWrap(value) {
		if (value)
			return '<div style="white-space:normal !important;">' + value + '</div>';
		else
			return "";
	}

	var columns = [{
				text : 'Image Prefix',
				dataIndex : 'imagePrefix',
				flex : 0.15
			}, {
				text : 'Run No',
				dataIndex : 'dataCollectionNumber',
				flex : 0.05
			}, {
				text : 'phiX',
				dataIndex : 'phiX',
				flex : 0.075
			}, {
				text : 'phiY',
				dataIndex : 'phiY',
				flex : 0.075
			}, {
				text : 'phiZ',
				dataIndex : 'phiZ',
				flex : 0.075
			}, {
				text : 'sampX',
				dataIndex : 'sampX',
				flex : 0.075
			}, {
				text : 'sampY',
				dataIndex : 'sampY',
				flex : 0.075
			}, {
				text : 'omega',
				dataIndex : 'omega',
				flex : 0.075
			}, {
				text : 'kappa',
				dataIndex : 'kappa',
				flex : 0.075
			}, {
				text : 'phi',
				dataIndex : 'phi',
				flex : 0.075
			}, {
				text : 'gridIndexY',
				dataIndex : 'gridIndexY',
				flex : 0.075
			}, {
				text : 'gridIndexZ',
				dataIndex : 'gridIndexZ',
				flex : 0.075
			}, {
				text : 'Spot Total',
				dataIndex : 'spotTotal',
				flex : 0.075
			}, {
				text : 'Good Bragg Candidates',
				dataIndex : 'goodBraggCandidates',
				flex : 0.075
			}, {
				text : 'Method 1 Resolution',
				dataIndex : 'method1Res',
				flex : 0.075
			}, {
				text : 'Method 2 Resolution',
				dataIndex : 'method2Res',
				flex : 0.075
			}, {
				text : 'Total Integrated Signal',
				dataIndex : 'totalIntegratedSignal',
				flex : 0.075
			},
			{
				text : 'Dozor Score',
				dataIndex : 'dozor_score',
				flex : 0.075
			}];

	return columns;

};
