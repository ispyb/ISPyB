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
/*global document */
/*global ErrorPanel */
/*global CollectionGroup */
/*global SessionSummaryObject */
/*global  SessionViewGrid */
/*global  CrystalClassGrid*/
/*global  SessionForm*/
/*global  ReferencePanel*/
/*global  SessionStatsPanel*/
/*global  ReportSessionPanel*/

Ext.Loader.setConfig({
	enabled : true
});

var sessionForm;
var sessionViewGrid;
var reportSessionPanel;
var crystalClassGrid;
var collectionGroup;
var sessionSummary;

// main entry point for the session Report Page
var IspybSessionSummary = {
	start : function (targetId, sessionId, nbOfItems) {
		this.targetId = targetId;
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

		this.showSessionSummary(targetId, sessionId, nbOfItems);
	},

	showSessionSummary : function (targetId, sessionId, nbOfItems) {
		var _this = this;
		// ajax calls
		collectionGroup = new CollectionGroup();
		sessionSummary = new SessionSummaryObject();

		// listen for events
		collectionGroup.onSaved.attach(function (sender, newData) {
			_this.refreshData(newData);
		});
		
		collectionGroup.imageSaved.attach(function (evt, data) {
			reportSessionPanel.exportSessionReport();
		});
		
		collectionGroup.sessionRetrieved.attach(function (evt, data) {
			sessionForm.refresh(data.session);
		});

		var items = [];

		sessionSummary.sessionSummaryRetrieved.attach(function (evt, data) {
			// errors in retrieving data
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			// no errors=> list of sessionDataInfo objects
			var listSessionDataObjectInformation = data.listSessionDataObjectInformation;
			// session summuray grid
			sessionViewGrid = new SessionViewGrid(data);
			// listen for events
			sessionViewGrid.onExportReportWithGraphSelected.attach(function (sender, args) {
				var listSvg = args.listSvg;
				var sessionId = args.sessionId;
				var listId = args.listId;
				collectionGroup.saveImageForExport(listSvg, sessionId, listId);
			});
			
			sessionViewGrid.onSaveButtonClicked.attach(function (sender, args) {
				var listData = sessionViewGrid.getListData();
				var sessionId = sessionViewGrid.getSessionId();
				var nbOfItems = sessionViewGrid.getNbOfItems();
				sessionSummary.saveData(listData, sessionId, nbOfItems);
			});
						
			sessionViewGrid.onPageChanged.attach(function (sender, args) {
				sessionSummary.changeSessionReportPageNumber(args.sessionReportCurrentPageNumber, args.sessionId);
			});

			// crystal class summury grid
			crystalClassGrid = new CrystalClassGrid(data);

			// session form for the session panel
			sessionForm = new SessionForm(data);
			var panelSessionWidth = 400;
			var panelSessionHeight = 300;
			if (data.isFx) {
				panelSessionHeight = 420;
			}
			if (data.isIx) {
				panelSessionHeight = 370;
			}
			var panelSession;
			
			// session panel information
			if (data && data.session) {
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
						handler : function () {
							var session = sessionForm.getSession();
							if (this.up('form').getForm().isValid()) {
								this.up('form').getForm().submit({
									url : 'viewDataCollectionGroup.do?reqCode=saveSession&session=' + JSON.stringify(sessionForm.getSession()),
									success : function (rp, o) {
										_this.refreshSession(data);
									}
								});
							}
						}
					}]
				});
			}
			
			// reference panel
			var referencePanel = new ReferencePanel();
			
			// session stats panel
			var sessionStatsPanel = new SessionStatsPanel();
			// report panel
			var reportSessionPanel = new ReportSessionPanel();
			reportSessionPanel.onExportReportSelected.attach(function (sender, args) {
				sessionViewGrid.saveGraphAsImg();
			});
			
			// header panel with Session info, reference, stats and reports
			var headerPanelItems = [];
			if (data && data.session) {
				headerPanelItems.push(panelSession);
			}
			if (data && data.session) {
				headerPanelItems.push(sessionStatsPanel.getPanel(data.session));
			}
			if (data && data.listOfReferences && data.listOfReferences.length > 0) {
				headerPanelItems.push(referencePanel.getPanel(data));
			}
			if (data) {
				headerPanelItems.push(reportSessionPanel.getPanel(data));
			}

			var headerPanel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				border : 0,
				width : '100%',
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
			// in the main panel: header, grid and crytsalGrid
			var mainItems = [];
			mainItems.push(headerPanel);
			mainItems.push(sessionViewGrid.getGrid(listSessionDataObjectInformation));
			mainItems.push(crystalClassGrid.getGrid(data.listCrystal));

			_this.panel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				border : 0,
				width : '100%',
				layout : {
					type : 'vbox',
					align : 'stretch'
				},
				renderTo : targetId,
				items : mainItems

			});
		});

		// retrieve information for session
		if (sessionId && sessionId != "null") {
			sessionSummary.getSessionSummary(nbOfItems);
		}
		
	},
	
	refreshSession : function () {
		var _this = this;
		var sessionId = sessionViewGrid.getSessionId();
		collectionGroup.getSessionInformation(sessionId);
	},

	refreshData : function (newData) {
		var _this = this;
		sessionViewGrid.refresh(newData);
		crystalClassGrid.refresh(newData);
	}
};
