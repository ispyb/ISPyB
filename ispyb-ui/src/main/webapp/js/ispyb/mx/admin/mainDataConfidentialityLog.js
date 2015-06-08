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

Ext.Loader.setConfig({
		enabled : true
	});

var dataConfidentialityPanel = null;

// main entry point for the data confidentiality log page
var IspybDataConfidentiality = {
	start : function (targetId) {
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

		this.showDataConfidentialityPage(targetId);
	},

	// display the data confidentiality page
	showDataConfidentialityPage : function (targetId) {
		var _this = this;
		
		// data confidentiality panel
		dataConfidentialityPanel = new DataConfidentialityPanel();
		
		// this panel contains the different options available, to display the logs: per day, month or all
		var viewLogOptionPanel = new ViewLogOptionPanel();
		viewLogOptionPanel.onViewLog.attach(function(evt,data) {
			var searchCriteria = viewLogOptionPanel.getSearchCriteria();
			dataConfidentialityPanel.update(searchCriteria);
		});
		
		//launch data confidentiality panel
		var  launchDataConfidentialityPanel = new LaunchDataConfidentialityPanel();
		launchDataConfidentialityPanel.onLaunchDataConfidentiality.attach(function(evt,data) {
			var launchCriteria = launchDataConfidentialityPanel.getLaunchCriteria();
			dataConfidentialityPanel.launchDataConfidentiality(launchCriteria);
		});

		var horizontalPanel = Ext.create('Ext.panel.Panel', {
			plain : false,
			frame : false,
			layout : {
					type : 'hbox'
				},
			border : 0,
			style : {
				padding : 0
			},
			items : [
			    viewLogOptionPanel.getPanel(), 
			    launchDataConfidentialityPanel.getPanel()
			]
		});
				
		// main panel creation	
		_this.panel = Ext.create('Ext.panel.Panel', {
			plain : true,
			frame : false,
			border : 0,
			autoScroll : true,
			renderTo : targetId,
			style : {
				padding : 0
			},
			items : [
			    horizontalPanel,
			    dataConfidentialityPanel.getPanel()
			]
		});
	}
};

