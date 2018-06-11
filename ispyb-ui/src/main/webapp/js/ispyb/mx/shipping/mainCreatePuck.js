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
 * Brenchereau, M. Bodin,  A. De Maria Antolinos
 ******************************************************************************/
/*global Ext */
/*global CreatePuckGrid */
/*global Shipping */
/*global ErrorPanel */
/*global HelpPanel */
/*global PuckHeadPanel */
/*global document */

// main entry point for puck creation
Ext.Loader.setConfig({
	enabled : true
});

var createPuckGrid = null;
var puckHeadPanel = null;

var IspybCreatePuck = {
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

		this.showCreatePuckPage(targetId);
	},

	showCreatePuckPage : function (targetId) {
		var _this = this;
		// ajax call class
		var shipping = new Shipping();
		// listen for events
		shipping.onSaved.attach(function (sender, newData) {
					if (newData && newData.errors && newData.errors.length > 0) {
						var args = [];
						args.targetId = targetId;
						args.errors = newData.errors;
						var errorPanel = new ErrorPanel(args);
						return;
					}
					_this.refreshPuck(newData);
				});

		// puck grid
		createPuckGrid = new CreatePuckGrid();
		// listen events on the grid
		createPuckGrid.onSaveButtonClicked.attach(function (sender, args) {
					var listSamples = createPuckGrid.getListSamples();
					var puckInfo = puckHeadPanel.getPuckInfo();
					shipping.savePuck(listSamples, puckInfo);
				});		
		
		shipping.shippingRetrieved.attach(function (evt, data) {
			// panel with info about shipping and dewar
			puckHeadPanel = new PuckHeadPanel();

			var argsHelp = [];
			argsHelp.allHelp = false;
			// help panel
			var helpPanel = new HelpPanel(argsHelp);

			var mainItems = [];
			mainItems.push(helpPanel.getPanel());
			mainItems.push(puckHeadPanel.getPanel(data));
			mainItems.push(createPuckGrid.getGrid(data));
			// panel with annotations
			var msg = "(*) mandatory field for each sample.<br/><span style='background-color: #ffcc66'>11 to 16 rows</span> of the table are only used in case of unipucks.";			
			var subPanel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'fit'
				},
				frame : true,

				style : {
					marginTop : '10px'
				},

				items : [ {
					xtype: 'component',
					style: 'margin-top:10px',
					html: "(*) mandatory field for each sample.<br/><span style='background-color: #ffcc66'>11 to 16 rows</span> of the table are only used in case of unipucks."
				}]
			});
			mainItems.push(subPanel);
			// main panel
			_this.panel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				border : 0,
				width: "100%",
				renderTo : targetId,
				style : {
					padding : 0
				},
				items : mainItems
			});

		});
		// retrieve shipping information
		shipping.getInformationForPuck();
	},

	refreshPuck : function (newdata) {
		var _this = this;
		createPuckGrid.refresh(newdata);
		if (newdata && newdata.shippingId && newdata.shippingId != -1) {
			document.location.href = "viewSample.do?reqCode=displayForShipping&shippingId=" + newdata.shippingId;
		}
	}

};
