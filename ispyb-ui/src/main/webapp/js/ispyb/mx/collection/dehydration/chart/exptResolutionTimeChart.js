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
// chart for dehydration
function ExptResolutionTimeChart() {
	var _this = this;
	this.store = null;
	this.chart = null;
	this.panel = null;
	this.width = 1000;
	this.height = 500;
}

//build chart
ExptResolutionTimeChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				theme : 'Category1',
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Numeric',
							position : 'left',
							fields : ['relativeHumidity'],
							minorTickSteps : 1,
							title : 'Relative humidity (%)',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Numeric',
							position : 'right',
							fields : ['rankingResolution', 'labelitR1',
									'labelitR2'],
							minorTickSteps : 1,
							title : 'Resolution (A)',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['time'],
							title : 'Time (seconds)'

						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'time',
							yField : 'relativeHumidity',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'time',
							yField : 'labelitR1',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'time',
							yField : 'labelitR2',
							markerConfig : {
								type : 'line',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'time',
							yField : 'rankingResolution',
							markerConfig : {
								type : 'triangle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};

// builds and returns the main panel with the chart
ExptResolutionTimeChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.store = data;
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtnL') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtnL',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	this.panel = Ext.create('Ext.Panel', {
				//width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};
