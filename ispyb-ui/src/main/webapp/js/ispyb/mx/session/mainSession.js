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
 * Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/

Ext.Loader.setConfig({
			enabled : true
		});
var session;
var sessions;
var sessionGrid;
var emptyPanel = null;

// main entry point for the session page -- not used yet
var IspybSession = {
	start : function(targetId, nbSessionsToDisplay) {
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

		this.showSessions(targetId, nbSessionsToDisplay);
	},

	showSessions : function(targetId, nbSessionsToDisplay) {
		var _this = this;
		// ajax call to server
		session = new Session();
		sessions = [];
		// search criteria panel
		var searchSessionPanel = new SearchSessionPanel();

		var items = [];

		session.sessionRetrieved.attach(function(evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			sessions = [];
			var listSessionInformation = data.listSessionInformation;
			for (var i = 0; i < listSessionInformation.length; i++) {
				sessions.push(new Session(listSessionInformation[i]));
			}
			items = [];

			items.push(searchSessionPanel.getPanel(data.beamlines));
			emptyPanel = Ext.create('Ext.Panel', {
						id : 'emptyPanel',
						border : false,
						width : 600, // panel's width
						height : 20
					});
			items.push(emptyPanel);
			if (sessions.length == 0) {
				items.push({
							border : 0,
							padding : 0,
							html : BUI
									.getWarningHTML("No sessions have been found.")
						});
			}

			// session grid object
			sessionGrid = new SessionGrid(data);
			sessionGrid.onEditButtonClicked.attach(function(sender, args) {
						var ses = session.getSessionById(args.sessionId,sessions);
						if (ses != null) {
							var sessionWindow = new SessionWindow(data);
							sessionWindow.onSaved.attach(function(evt,
											sessionSave) {
										session.saveSession(sessionSave);
									});
							sessionWindow.draw(ses);
						}
					});

			sessionGrid.onSubmitReportButtonClicked.attach(function(sender,
							args) {
						var ses = session.getSessionById(args.sessionId, sessions);
						if (ses != null) {
							session.submitReport(ses);
						}
					});

			sessionGrid.onRemoveButtonClicked.attach(function(sender, args) {
						var ses = session.getSessionById(args.sessionId,sessions);
						if (ses != null) {
							session.removeSession(ses);
						}
					});

			items.push(sessionGrid.getGrid(sessions));

			// creation of the main panel
			this.panel = Ext.create('Ext.panel.Panel', {
						plain : true,
						frame : false,
						border : 0,
						renderTo : targetId,
						style : {
							padding : 0
						},
						items : items
					});
		});

		session.onSaved.attach(function(sender, newSessions) {
					_this.refresh(newSessions);
				});

		session.getSessionByProposal(nbSessionsToDisplay);
	},

	refresh : function(newSessions) {
		var _this = this;
		sessions = [];
		for (var i = 0; i < newSessions.length; i++) {
			sessions.push(new Session(newSessions[i]));
		}
		sessionGrid.refresh(sessions);
	}
};
