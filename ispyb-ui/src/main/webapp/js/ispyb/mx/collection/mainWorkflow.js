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

var workflowPanel;
var meshScanPanel;
var dehydrationPanel;

var emptyPanel = null;
var referencePanel = null;
// main entry point for the workflow page
var IspybWorkflow = {
	start : function(targetId, workflowId) {
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

		this.showWorkflowPage(targetId, workflowId);
	},

	showWorkflowPage : function(targetId,  workflowId) {
		var _this = this;
		// ajax call
		collection = new Collection();

		// listen events
		collection.onSaved.attach(function(sender, newData) {
			_this.refreshDataCollection(newData);
		});

		collection.onRank.attach(function(sender, newData) {
			document.location.href = "/ispyb/user/sampleRanking.do?reqCode=display";
		});
		collection.onRankAutoProc.attach(function(sender, newData) {
			document.location.href = "/ispyb/user/autoProcRanking.do?reqCode=display";
		});

		dataCollectionsList = [];

		collection.workflowRetrieved.attach(function(evt, data) {
					// erros in retrieving data
					if (data.errors && data.errors.length > 0) {
						var args = [];
						args.targetId = targetId;
						args.errors = data.errors;
						var errorPanel = new ErrorPanel(args);
						return;
					}
					// no errors, collects in workflow:
					dataCollectionsList = [];
					for (var i=0; i<data.listOfCollect.length; i++){
						collects = data.listOfCollect[i];
						for (var j=0; j<collects.length; j++){
							dataCollectionsList.push(collects[j]);
						}
					}
					listOfCrystalClass = data.listOfCrystalClass;
					data.pagingMode = false;
					//data.groupFieldDefault = null;
					// dataCollection Grid
					dataCollectionGrid = new DataCollectionGrid(data);
					// listen for events
					dataCollectionGrid.onSaveButtonClicked.attach(function(
									sender, args) {
								var listDataCollection = dataCollectionGrid
										.getListDataCollection();
								collection
										.saveDataCollection(listDataCollection);
							});
					dataCollectionGrid.onRankButtonClicked.attach(function(
									sender, args) {
								var listDataCollectionRank = dataCollectionGrid
										.getListDataCollectionRank();
								collection.rank(listDataCollectionRank);

							});
					dataCollectionGrid.onRankAutoProcButtonClicked.attach(
							function(sender, args) {
								var listDataCollectionRank = dataCollectionGrid
										.getListDataCollectionRankAutoProc();
								collection.rankAutoProc(listDataCollectionRank);
							});

					var items = [];
					var activeTab = 0;
					//tabs workflow: image has been removed (too much image, too much time to display)
					items.push({
								tabConfig : {
									title : "DataCollections"
								},
								items : [dataCollectionGrid
										.getGrid(dataCollectionsList)]
							});
//					if (data.displayImage == 1) {
//						if (data.listOfImages){
//							var nb  = data.listOfImages.length;
//							if (nb > 1 ){
//								var imageTabPanel = new ImageTabPanel();
//								items.push({
//									tabConfig : {
//										title : "Images"
//									},
//									items : [imageTabPanel.getPanel(data)]
//								});
//							}else if (nb == 1){
//								var imagePanel = new ImagePanel();
//								var meshData = null;
//								if (data.listOfMeshData && data.listOfMeshData.length > 0)
//									meshData = data.listOfMeshData[0];
//								var snapshot = null;
//								if (data.listOfSnapshot && data.listOfSnapshot.length > 0)
//									snapshot = data.listOfSnapshot[0];
//								items.push({
//											tabConfig : {
//												title : "Images"
//											},
//											items : [imagePanel.getPanel(snapshot, data.listOfImages[0], data.displayMesh, meshData)]
//										});
//							}
//						}
//						
//					}
					if (data.displayWorkflow == 1) {
						workflowPanel = new WorkflowPanel();
						items.push({
									tabConfig : {
										title : "Workflow Log"
									},
									items : [workflowPanel.getPanel(data)]
								});
					}
//					if (data.displayMesh == 1) {
//						var nb  = data.listOfMeshData.length;
//						if (nb > 1 ){
//							var meshTabPanel = new MeshTabPanel();
//							items.push({
//								tabConfig : {
//									title : "Mesh"
//								},
//								items : [meshTabPanel.getPanel(data)]
//							});
//						}else if (nb == 1){
//							var meshScanPanel = new MeshScanPanel();
//							var meshData =data.listOfMeshData[0];
//							var snapshot = null;
//							if (data.listOfSnapshot && data.listOfSnapshot.length > 0)
//								snapshot = data.listOfSnapshot[0];
//							items.push({
//										tabConfig : {
//											title : "Mesh"
//										},
//										items : [meshScanPanel.getPanel(meshData,snapshot, 0)]
//									});
//						}
//						
//						
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
					dataCollectionGrid.setRankValue();
					dataCollectionGrid._hideCol();
					dataCollectionGrid._sort();

				});
		// retrieve data for the workflow
		collection.getWorkflow(workflowId);
		

	},

	refreshDataCollection : function(data) {
		var _this = this;
		dataCollectionGrid.refresh(data);
	}

};
