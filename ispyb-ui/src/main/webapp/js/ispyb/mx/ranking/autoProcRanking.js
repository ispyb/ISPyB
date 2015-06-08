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
 * Brenchereau, M. Bodin, M. Bodin
 ******************************************************************************/

Ext.Loader.setConfig({
			enabled : true
		});

Ext.QuickTips.init();

// main entry point for the autoproc ranking charts
var IspybAutoProcRanking = {
	start : function(targetId) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.util.*', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.util.*', 'Ext.state.*',
						'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.form.MultiSelect', 'Ext.ux.form.ItemSelector',
						'Ext.chart.*', 'Ext.Window',
						'Ext.layout.container.Fit', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);

		this.showChartTabs(targetId);
	},

	showChartTabs : function(targetId) {
		// object with the ajax calls to the server
		var autoProcRankingData = new AutoProcRankingData();
		// event listener: data for autoProc Ranking have been found
		autoProcRankingData.autoProcRankingRetrieved.attach(
				function(evt, data) {
					// bar char creation
					var barChart = new StackedBarChart(targetId);
					// radar chart
					var radarChart = new RadarChart(targetId);
					// parallel chart
					var parallelChart = new ChartPanel(targetId);
					// line chart
					var lineChart = new LineChart(targetId);
					// tabs creation
					var tabs = Ext.create('Ext.tab.Panel', {
								renderTo : targetId,
								width : 800,
								height : 500,
								activeTab : 0,
								defaults : {
									bodyPadding : 0
								},
								items : [{
											tabConfig : {
												id : 'barChart',
												title : "Stacked bar chart"
											},
											items : [barChart.getPanel(data)]
										}, {
											tabConfig : {
												id : 'radarChart',
												title : "Radar chart"
											},
											items : [radarChart.getPanel(data)]
										}, {
											tabConfig : {
												id : 'lineChart',
												title : "Line chart"
											},
											items : [lineChart.getPanel(data)]
										}]
							});
				});
		// call to retrieve the data for autoProcRanking
		autoProcRankingData.getAutoProcRanking();
	}

};
