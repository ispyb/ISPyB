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

var autoprocessingPanel = null;
var panel = null;
// main Entry point for the autoProcessing page
var IspybAutoProc = {
	start : function(targetId) {
		this.targetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
				'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*', 'Ext.state.*',
				'Ext.form.*', 'Ext.ux.CheckColumn', 'Ext.ux.RowExpander',
				'Ext.selection.CheckboxModel', 'Ext.selection.CellModel',
				'Ext.layout.container.Column', 'Ext.tab.*',
				'Ext.ux.grid.FiltersFeature', 'Ext.selection.CellModel',
				'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
				'Ext.ux.form.MultiSelect', 'Ext.ux.form.ItemSelector',
				'Ext.chart.*', 'Ext.Window', 'Ext.fx.target.Sprite',
				'Ext.layout.container.Fit', 'Ext.window.MessageBox',
				'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showAutoprocTab(targetId);
	},

	showAutoprocTab : function(targetId) {
		var _this = this;
		// ajax calls 
		var result = new Result();
		// autoproc ajax calls
		var autoProcProgramAttachmentGraph = new AutoProcProgramAttachmentGraph();

		// main panel for autoProc
		autoprocessingPanel = new AutoprocessingPanel();
		// listen for events
		autoprocessingPanel.onAutoProcSelected.attach(function(sender, args) {
					var autoProcId = args.autoProcId;
					result.getAutoprocessingDetails(autoProcId);
				});
		autoprocessingPanel.onAutoProcGraphSelected.attach(function(sender,
				args) {
			var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
			autoProcProgramAttachmentGraph.getData(autoProcProgramAttachmentId);
		});
		
		

		var items = [];
		// listen for events
		result.autoProcRetrieved.attach(function(evt, data) {
			if (panel == null){
					items.push(autoprocessingPanel.getPanel(data));
					panel = Ext.create('Ext.panel.Panel', {
								plain : true,
								frame : false,
								width : 900,
								border : 0,
								renderTo : targetId,
								style : {
									padding : 0
								},
								items : items

							});

				if (data && data.autoProcIdSelected && data.autoProcIdSelected != null) {
					autoprocessingPanel.setSelectedAutoProcId(data.autoProcIdSelected);
					result.getAutoprocessingDetails(data.autoProcIdSelected);
				}	
			}else{
				autoprocessingPanel.setAutoProcList(data);
			}
			
		});

		result.autoProcDetailRetrieved.attach(function(evt, data) {
					autoprocessingPanel.displayAutoProc(data);
				});

		autoProcProgramAttachmentGraph.dataRetrieved.attach(
				function(evt, data) {
					if (data.autoProcessingData && data.autoProcessingData.length > 0) {
						autoprocessingPanel.displayGraphAttachment(data);
					}
				});

		// retrieve info
		result.getAutoprocessingInfo(10.0, 1.0);
	}

};
