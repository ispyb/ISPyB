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

var dataCollectionGroupsList;
var dataCollectionGroupGrid;
var xrfSpectraGrid;
var energyScanGrid;
var sessionForm;
var crystalClassGrid;
// main entry point for dataCollectionGroup page
var IspybCollectionGroup = {
	start : function(targetId, sessionId) {
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

		this.showCollectionGroup(targetId, sessionId);
	},

	showCollectionGroup : function(targetId, sessionId) {
		var _this = this;
		// ajax calls
		collectionGroup = new CollectionGroup();
		// listen for events
		collectionGroup.onSaved.attach(function(sender, newData) {
					_this.refreshDataCollectionGroup(newData);
					_this.refreshEnergyScan(newData);
					_this.refreshXRFSpectra(newData);
				});

		var items = [];
		dataCollectionGroupsList = [];
		xrfSpectraList = [];
		energyScanList = [];

		// data have been retrieved:
		collectionGroup.collectionGroupRetrieved.attach(function(evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			dataCollectionGroupsList = data.dataCollectionGroupList;
			xrfSpectraList = data.xrfSpectraList;
			listOfCrystalClass = data.listOfCrystalClass;
			energyScanList = data.energyScansList;
			dataCollectionGroupGrid = new DataCollectionGroupGrid(data);
			xrfSpectraGrid = new XRFSpectraGrid(data);
			energyScanGrid = new EnergyScanGrid(data);
			crystalClassGrid = new CrystalClassGrid(data);

			// listen for events
			dataCollectionGroupGrid.onSaveButtonClicked.attach(function(sender,
							args) {
						var listDataCollectionGroup = dataCollectionGroupGrid
								.getListDataCollectionGroup();
						var sessionId = dataCollectionGroupGrid.getSessionId();
						collectionGroup.saveDataCollectionGroup(
								listDataCollectionGroup, sessionId);
					});

			xrfSpectraGrid.onSaveButtonClicked.attach(function(sender, args) {
						var listXRFSpectra = xrfSpectraGrid.getListXRFSpectra();
						var sessionId = xrfSpectraGrid.getSessionId();
						collectionGroup.saveXRFSpectra(listXRFSpectra,
								sessionId);
					});

			energyScanGrid.onSaveButtonClicked.attach(function(sender, args) {
						var listEnergyScan = energyScanGrid.getListEnergyScan();
						var sessionId = energyScanGrid.getSessionId();
						collectionGroup.saveEnergyScan(listEnergyScan,
								sessionId);
					});

			// sessionForm for session panel
			sessionForm = new SessionForm(data);
			var panelSessionWidth = 400;
			var panelSessionHeight = 300;
			if (data.isFx) {
				panelSessionHeight = 420;
			}
			if (data.isIx) {
				panelSessionHeight = 370;
			}
			
			if (data && data.session){
				panelSession = Ext.widget({
					xtype : 'form',
					layout : 'fit',
					collapsible : true,
					id : 'sessionForm',
					frame : true,
					title : 'Session Information',
					bodyPadding : '5 5 0',
					fieldDefaults : {
						msgTarget : 'side',
						labelWidth : 90
					},
					style : {
						marginTop : '10px'
					},
					defaultType : 'textfield',
					items : [sessionForm.getPanel(data.session)],
					buttons : [{
						text : 'Save',
						handler : function() {
							var session = sessionForm.getSession();
							if (this.up('form').getForm().isValid()) {
								this.up('form').getForm().submit({
									url : 'viewDataCollectionGroup.do?reqCode=saveSession&session=' + JSON.stringify(sessionForm.getSession()),
									success : function(rp, o) {
										_this.refreshSession(data);
									}
								});
							}
						}
					}]
				});
			}
			var tabs = Ext.create('Ext.tab.Panel', {

						activeTab : 0,
						defaults : {
							bodyPadding : 0,
							autoScroll : true
						},
						style : {
							marginTop : '10px'
						},
						items : [{
							tabConfig : {
								id : 'dataCollectionGroup',
								title : "DataCollectionGroups"
							},
							items : [
									dataCollectionGroupGrid
											.getGrid(dataCollectionGroupsList),
									crystalClassGrid.getGrid(data.listCrystal)]
						}, {
							tabConfig : {
								id : 'energyScans',
								title : "EnergyScans"
							},
							items : [energyScanGrid.getGrid(energyScanList)]
						}, {
							tabConfig : {
								id : 'xrfspectra',
								title : "XRFSpectra"
							},
							items : [xrfSpectraGrid.getGrid(xrfSpectraList)]
						}]
					});

			// builds the header panel with References, Stats, reports
			referencePanel = new ReferencePanel();
			
			var sessionStatsPanel = new SessionStatsPanel();
			
			var reportSessionPanel = new ReportSessionPanel();
			
			var headerPanelItems = [];
			if (data && data.session){
				headerPanelItems.push(panelSession);
			}
			if (data && data.session){
				headerPanelItems.push(sessionStatsPanel.getPanel(data.session));
			}
			if (data && data.listOfReferences && data.listOfReferences.length > 0) {
				headerPanelItems.push(referencePanel.getPanel(data));
			}
			if (data && data.sessionHasMXpressOWF && data.sessionHasMXpressOWF == true) {
				headerPanelItems.push(reportSessionPanel.getPanel(data));
			}

			var headerPanel = Ext.create('Ext.panel.Panel', {
						plain : true,
						frame : false,
						border : 0,
						layout : {
							type : 'hbox'
							// pack: 'start'
							// , align: 'stretch'
						},
						style : {
							padding : 0
						},
						items : headerPanelItems

					});
			var mainItems = [];
			mainItems.push(headerPanel);
			mainItems.push(tabs);

			// main panel
			_this.panel = Ext.create('Ext.panel.Panel', {
						plain : true,
						frame : false,
						border : 0,
						layout : {
							type : 'vbox',
							align : 'stretch'
						},
						renderTo : targetId,
						items : mainItems

					});

		});

		collectionGroup.sessionRetrieved.attach(function(evt, data) {
					sessionForm.refresh(data.session);
				});
		if (sessionId && sessionId != "null"){
			collectionGroup.getCollectionGroupBySession();
		}else{
			collectionGroup.getCollectionGroupBySample();
		}
	},

	refreshDataCollectionGroup : function(newDataCollectionGroups) {
		var _this = this;
		dataCollectionGroupGrid.refresh(newDataCollectionGroups);
		crystalClassGrid.refresh(newDataCollectionGroups);
	},

	refreshXRFSpectra : function(newXRFSpectra) {
		var _this = this;
		xrfSpectraGrid.refresh(newXRFSpectra);
	},

	refreshEnergyScan : function(newEnergyScan) {
		var _this = this;
		energyScanGrid.refresh(newEnergyScan);
	},

	refreshSession : function() {
		var _this = this;
		var sessionId = dataCollectionGroupGrid.getSessionId();
		collectionGroup.getSessionInformation(sessionId);
	}
};
