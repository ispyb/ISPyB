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
/*global Event */
/*global Ext */
/*global document */
/*global Shipping */
/*global CreatePuckGrid */
/*global DewarWindow */
/*global PuckWindow */
/*global ErrorPanel */
/*global HelpPanel */
/*global FillShipmentPanel */
/*global PuckHeadPanel */

Ext.Loader.setConfig({
	enabled : true
});

var createPuckGrid = null;
var fillShipmentPanel = null;
// main etry point for fillShipment page
var IspybFillShipment = {
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

		this.showFillShipmentPage(targetId);
	},

	showFillShipmentPage : function (targetId) {
		var _this = this;
		// ajax call class
		var shipping = new Shipping();
		
		createPuckGrid = new CreatePuckGrid();
		var puckHeadPanel = new PuckHeadPanel();
		
		createPuckGrid.onSaveButtonClicked.attach(function (sender, args) {
					var listSamples = createPuckGrid.getListSamples();
					var puckInfo = puckHeadPanel.getPuckInfo();
					shipping.savePuck(listSamples, puckInfo);
				});
		
		// listen for events
		shipping.shipmentRetrieved.attach(function (evt, data) {
			// errors
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			// no errors: we create the main panel
			fillShipmentPanel = new FillShipmentPanel();
			// listen for events:
			fillShipmentPanel.onAddDewarButtonClicked.attach(function (sender, args) {
				var shippingId = args.shippingId;
				var dewar = [];
				dewar.dewarName = "";
				var argsWindow = [];
				argsWindow.addMode = true;
				var dewarWindow = new DewarWindow(argsWindow);
				dewarWindow.onSaved.attach(function (evt, dewarSave) {
					shipping.addDewar(dewarSave);
				});
				dewarWindow.onCancelled.attach(function (evt) {
					fillShipmentPanel.setFirstActiveTab();
				});
				dewarWindow.draw(dewar);
			});
			
			fillShipmentPanel.onEditDewarButtonClicked.attach(function (sender, args) {
				var dewar = [];
				dewar.dewarName = args.dewarName;
				dewar.dewarId = args.dewarId;
				var argsWindow = [];
				argsWindow.addMode = false;
				var dewarWindow = new DewarWindow(argsWindow);
				dewarWindow.onSaved.attach(function (evt, dewarSave) {
					shipping.editDewar(dewarSave);
				});
				dewarWindow.onCancelled.attach(function (evt) {
				});
				dewarWindow.draw(dewar);
			});
			
			fillShipmentPanel.onRemoveDewarButtonClicked.attach(function (sender, args) {
				shipping.removeDewar(args.dewarId);
				
			});
			
			fillShipmentPanel.onAddPuckButtonClicked.attach(function (sender, args) {
				var dewarId = args.dewarId;
				var puck = [];
				puck.puckName = "";
				puck.dewarId = args.dewarId;
				var argsWindow = [];
				argsWindow.addMode = true;
				var puckWindow = new PuckWindow(argsWindow);
				puckWindow.onSaved.attach(function (evt, puckSave) {
					shipping.addPuck(puckSave);
				});
				puckWindow.onCancelled.attach(function (evt) {
					fillShipmentPanel.setPuckFirstActiveTab();
				});
				puckWindow.draw(puck);
			});
			
			fillShipmentPanel.onPastePuckButtonClicked.attach(function (sender, args) {
				shipping.pastePuck(args.dewarId);
				
			});
			
			fillShipmentPanel.onEditPuckButtonClicked.attach(function (sender, args) {
				var puck = [];
				puck.puckName = args.puckName;
				puck.containerId = args.containerId;
				var argsWindow = [];
				argsWindow.addMode = false;
				var puckWindow = new PuckWindow(argsWindow);
				puckWindow.onSaved.attach(function (evt, puckSave) {
					shipping.editPuck(puckSave);
				});
				puckWindow.onCancelled.attach(function (evt) {
				});
				puckWindow.draw(puck);
			});
			
			fillShipmentPanel.onRemovePuckButtonClicked.attach(function (sender, args) {
				shipping.removePuck(args.containerId);
				
			});
			
			fillShipmentPanel.onSavePuckButtonClicked.attach(function (sender, args) {
				shipping.updatePuck(args.listSamples, args.puckInfo);
				
			});
			
			fillShipmentPanel.onCopyPuckButtonClicked.attach(function (sender, args) {
				shipping.copyPuck(args.containerId, args.listSamples);
			});
			
			// help panel
			var helpPanel = new HelpPanel();
			
			var mainItems = [];
			mainItems.push(helpPanel.getPanel());
			mainItems.push(fillShipmentPanel.getPanel(data));
			// panel with annotations
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
		
		// listen more events
		shipping.onDewarUpdated.attach(function (evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			fillShipmentPanel.refresh(data);
			fillShipmentPanel.setLastActiveTab();
		});
		
		shipping.onPuckUpdated.attach(function (evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			fillShipmentPanel.refresh(data);
			fillShipmentPanel.setPuckLastActiveTab();
		});
		
		shipping.onPuckCopied.attach(function (evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			fillShipmentPanel.setPuckPasteActiv(true);
		});
		
		shipping.onPuckPasted.attach(function (evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			fillShipmentPanel.refresh(data);
			//fillShipmentPanel.setPuckPasteActiv(false);
		});
		
		shipping.onSaved.attach(function (evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
		});
		// retrieve shipping information	
		shipping.getInformationForShipping();
	}		

};