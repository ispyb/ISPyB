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
/*global Collection */
/*global SearchDataCollectionPanel */
/*global DataCollectionGrid */

Ext.Loader.setConfig({
	enabled : true
});

var dataCollectionList;
var dataCollectionGrid = null;
var searchDataCollectionPanel = null;
var emptyPanel = null;

var searchBeamline = "";
var startDate = "";
var endDate = "";
var startTime = "";
var endTime = "";

// main entry point for the last Collect in the manager view
var IspybLastCollect = {
	start : function (targetId) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showLastCollect(targetId);
	},

	showLastCollect : function (targetId) {
		var _this = this;
		// ajax calls
		var collection = new Collection();

		var items = [];
		dataCollectionList = [];
		// search criteria panel
		searchDataCollectionPanel = new SearchDataCollectionPanel();
		searchDataCollectionPanel.onSearchDataCollection.attach(function (evt,
						data) {
					var searchCriteria = searchDataCollectionPanel
							.getSearchCriteria();
					collection.getLastCollect(searchCriteria.beamline,
							searchCriteria.startDate, searchCriteria.endDate,
							searchCriteria.startTime, searchCriteria.endTime);
				});

		emptyPanel = Ext.create('Ext.Panel', {
			id : 'emptyPanel',
			border : false,
			bodyStyle : {
				"background-color" : "#99FFCC"
			},
			width : 600, // panel's width
			height : 20
		});
		// listen for events
		collection.collectionRetrieved.attach(function (evt, data) {
					dataCollectionList = data.dataCollectionList;
					listOfCrystalClass = data.listOfCrystalClass;

					if (_this.panel) {
						_this.refreshCollections(data);
					} else {
						items.push(searchDataCollectionPanel
								.getPanel(data.beamlines));
						searchDataCollectionPanel.setCriteria(searchBeamline,
								startDate, endDate, startTime, endTime);
						items.push(emptyPanel);
						if (data.dataCollectionList.length == 0) {
							items.push(_this.getNoCollectPanel());
						} else {
							data.pagingMode = true;
							// dataCollectionGrid = new DataCollectionGrid(data)
							// ;
							_this.createDataCollectionGrid(data);

							items.push(dataCollectionGrid
									.getGrid(dataCollectionList));
						}

						_this.panel = Ext.create('Ext.panel.Panel', {
								plain : true,
								frame : false,
								border : 0,
								bodyStyle : {
									"background-color" : "#99FFCC"
								},
								renderTo : targetId,
								style : {
									padding : 0
								},
								items : items

							});
					}
				});

		collection.criteriaRetrieved.attach(function (evt, data) {
					if (data) {
						searchBeamline = data.searchBeamline;
						startDate = data.startDate;
						endDate = data.endDate;
						startTime = data.startTime;
						endTime = data.endTime;

					}
					collection.getLastCollect(searchBeamline, startDate,
							endDate, startTime, endTime);
				});

		collection.getSearchCriteria();
	},

	getNoCollectPanel : function () {
		var noCollectDataPanel = Ext.create('Ext.Panel', {
			id : 'noCollectDataPanel',
			width : 600,
			height : 200,
			border : 0,
			bodyStyle : {
				"background-color" : "#99FFCC"
			},
			layout : {
				type : 'vbox', // Arrange child items vertically
				align : 'center', // Each takes up full width
				bodyPadding : 0,
				border : 0
			},
			html : '<html><h4>No Data Collections  have been found</h4></html>'
		});
		return noCollectDataPanel;

	},

	createDataCollectionGrid : function (data) {
		var _this = this;
		dataCollectionGrid = new DataCollectionGrid(data);
		dataCollectionGrid.onPagingMode.attach(function (evt, d2) {
					// console.log("paging"+(!dataCollectionGrid.getPagingMode()));
					data.pagingMode = !dataCollectionGrid.getPagingMode();
					_this.clearExtjsComponent();

					dataCollectionGrid.setArgs(data);
					_this.panel.add(dataCollectionGrid
							.getGrid(dataCollectionList));
					_this.panel.doLayout();
				});

	},

	clearExtjsComponent : function () {
		var _this = this;
		_this.panel.items.removeAt(2);
		// var f;
		// while(f = _this.panel.items.first()){
		// _this.panel.remove(f, true);
		// }
	},

	refreshCollections : function (data) {
		var _this = this;
		if (data.dataCollectionList.length == 0) {
			if (dataCollectionGrid != null) {
				this.clearExtjsComponent();
				dataCollectionGrid = null;
				_this.panel.add(_this.getNoCollectPanel());
				_this.panel.doLayout();
			}
		} else {
			this.clearExtjsComponent();
			data.pagingMode = true;
			_this.createDataCollectionGrid(data);
			_this.panel.add(dataCollectionGrid.getGrid(dataCollectionList));
			_this.panel.doLayout();
		}

	}

};
