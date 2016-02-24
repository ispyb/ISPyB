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

Ext.Loader.setConfig({
			enabled : true
		});

//var dataCollectionsList;
//var dataCollectionGrid;
var imagePanel;
var workflowPanel;
var meshScanPanel;
var dehydrationPanel;

var emptyPanel = null;
var referencePanel = null;
// main entry point for workflow in the old dataCollection page, no more used 
var IspybCollection2 = {
	start : function(targetId, mode, dataCollectionGroupId) {
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

		this.showCollectionPage(targetId, mode, dataCollectionGroupId);
	},

	showCollectionPage : function(targetId, mode, dataCollectionGroupId) {
		var _this = this;
		var collection = new Collection();

		collection.onSaved.attach(function(sender, newData) {
					_this.refreshDataCollection(newData);
				});

		collection.onRank.attach(function(sender, newData) {
			document.location.href = "/ispyb/user/sampleRanking.do?reqCode=display";
		});
		collection.onRankAutoProc.attach(function(sender, newData) {
			document.location.href = "/ispyb/user/autoProcRanking.do?reqCode=display";
		});

		//dataCollectionsList = new Array();

		collection.collectionRetrieved.attach(function(evt, data) {
					if (data.errors && data.errors.length > 0) {
						var args = [];
						args.targetId = targetId;
						args.errors = data.errors;
						var errorPanel = new ErrorPanel(args);
						return;
					}
					//dataCollectionsList = data.dataCollectionList;
					listOfCrystalClass = data.listOfCrystalClass;
					data.pagingMode = false;
					//dataCollectionGrid = new DataCollectionGrid(data);

					//dataCollectionGrid.onSaveButtonClicked.attach(function(
					//				sender, args) {
					//			var listDataCollection = dataCollectionGrid
					//					.getListDataCollection();
					//			collection
					//					.saveDataCollection(listDataCollection);
					//		});
					//dataCollectionGrid.onRankButtonClicked.attach(function(
					//				sender, args) {
					//			var listDataCollectionRank = dataCollectionGrid
					//					.getListDataCollectionRank();
					//			collection.rank(listDataCollectionRank);
//
					//		});
					/*dataCollectionGrid.onRankAutoProcButtonClicked.attach(
							function(sender, args) {
								var listDataCollectionRank = dataCollectionGrid
										.getListDataCollectionRankAutoProc();
								collection.rankAutoProc(listDataCollectionRank);
							});
*/
					var items = [];
					var activeTab = 0;
//					items.push({
//								tabConfig : {
//									title : "DataCollections"
//								},
//								items : [dataCollectionGrid
//										.getGrid(dataCollectionsList)]
//							});
					
					if (data.displayWorkflow == 1) {
						workflowPanel = new WorkflowPanel();
						items.push({
									tabConfig : {
										title : "Workflow Log"
									},
									items : [workflowPanel.getPanel(data)]
								});
					}
//					if (data.displayImage == 1) {
//						imagePanel = new ImagePanel();
//						items.push({
//									tabConfig : {
//										title : "Images"
//									},
//									items : [imagePanel.getPanel(data.snapshot, data.imageList, data.displayMesh, data.meshData)]
//								});
//					}
//					if (data.displayMesh == 1) {
//						meshScanPanel = new MeshScanPanel();
//						items.push({
//									tabConfig : {
//										title : "Mesh"
//									},
//									items : [meshScanPanel.getPanel(data.meshData, data.snapshot, 0)]
//								});
//					}
//					if (data.displayDehydration == 1) {
//						dehydrationPanel = new DehydrationPanel();
//						items.push({
//									tabConfig : {
//										title : "Dehydration"
//									},
//									items : [dehydrationPanel.getPanel(data)]
//								});
//					}
					if (data && data.workflowVO && data.workflowVO.resultFiles && data.workflowVO.resultFiles.length > 0) {
						resultPanel = new ResultPanel();
						items.push({
									tabConfig : {
										title : "Results"
									},
									items : [resultPanel.getPanel(data)]
								});
						activeTab = items.length - 1;
					}
					if (items.length > 0){

						var tabs = Ext.create('Ext.tab.Panel', {
								width: "99%",
								activeTab : activeTab,
								defaults : {
									bodyPadding : 0,
									autoScroll : true
								},
								items : items
							});

						var mainItems = [];
					//referencePanel = new ReferencePanel();

					//mainItems.push(referencePanel.getPanel(data));

//					emptyPanel = Ext.create('Ext.Panel', {
//								id : 'emptyPanel',
//								border : false,
//								width : 600, // panel's width
//								height : 20
//							});

					//mainItems.push(emptyPanel);

						mainItems.push(tabs);

						_this.panel = Ext.create('Ext.panel.Panel', {
								plain : true,
								frame : false,
								width:"99%",
								border : 0,
								layout : 'fit',
								renderTo : targetId,
								style : {
									padding : 0
								},
								items : mainItems

							});
					}
					//dataCollectionGrid.setRankValue();
					//dataCollectionGrid._hideCol();
					//dataCollectionGrid._sort();

				});
		if (mode == "dataCollectionGroup") {
			collection.getDataCollectionByDataCollectionGroup(dataCollectionGroupId);
		} else if (mode == 0) {
			//collection.getDataCollectionBySession();
		}

	},

	refreshDataCollection : function(data) {
		var _this = this;
		//dataCollectionGrid.refresh(data);
	}

};
