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
// main entry point for the graph page (no more used)
Ext.Loader.setConfig({
			enabled : true
		});

var IspybAutoProcAttachmentGraph = {
	start : function(targetId, autoProcProgramAttachmentId) {
		this.tartgetId = targetId;
		this.autoProcProgramAttachmentId = autoProcProgramAttachmentId;
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

		this.showGraph(targetId, autoProcProgramAttachmentId);
	},

	showGraph : function(targetId, autoProcProgramAttachmentId) {
		var _this = this;
		var autoProcProgramAttachmentGraph = new AutoProcProgramAttachmentGraph();
		var args = [];
		args.changeSize = false;
		autoProcProgramAttachmentGraphPanel = new AutoProcProgramAttachmentGraphPanel(args);

		var items = [];
		autoProcProgramAttachmentGraph.dataRetrieved.attach(
				function(evt, data) {
					if (data.autoProcessingData && data.autoProcessingData.length > 0) {
						items.push(autoProcProgramAttachmentGraphPanel.getPanel(data));
						_this.panel = Ext.create('Ext.panel.Panel', {
									plain : true,
									frame : false,
									border : 0,
									renderTo : targetId,
									style : {
										padding : 0
									},
									items : items

								});
					}
				});
		autoProcProgramAttachmentGraph.getData(autoProcProgramAttachmentId);
	}

};
