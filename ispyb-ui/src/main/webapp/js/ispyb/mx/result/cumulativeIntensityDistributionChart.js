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
// chart cumulative Intensity Distribution
function CumulativeIntensityDistributionChart(args) {
	var _this = this;
	this.store = null;
	this.chart = null;
	this.panel = null;
	this.width = 980;
	this.height = 580;
	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}


// builds the chart
CumulativeIntensityDistributionChart.prototype.getChart = function() {
	var _this = this;

	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				legend : {
					position : 'right'
				},
				theme : 'Category1',
				axes : [{
							type : 'Numeric',
							position : 'left',
							fields : ['acent_theor'],
							maximum : 100,
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						},{
							type : 'Numeric',
							position : 'left',
							fields : ['acent_twin'],
							maximum : 100,
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						},{
							type : 'Numeric',
							position : 'left',
							fields : ['acent_obser'],
							maximum : 100,
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						},{
							type : 'Numeric',
							position : 'left',
							fields : ['centric_theor'],
							maximum : 100,
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
							position : 'left',
							fields : ['centric_obser'],
							maximum : 100,
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
							fields : ['z'],
							title : 'Z',
							maximum : 1,
							grid : true,
							label : {
								rotation : {
									degrees : 325
								}

							}
						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'z',
							yField : 'acent_theor',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, 
						{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'z',
							yField : 'acent_twin',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, 
						{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'z',
							yField : 'acent_obser',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, 
						{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'z',
							yField : 'centric_theor',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, 
						{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'z',
							yField : 'centric_obser',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};


// returns and builds the panel with the chart
CumulativeIntensityDistributionChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.store = data;
	_this.getChart();
	var diffHandler = function(btn) {
		_this.chart.save({
					type : 'image/png'
				});
	};
	var saveAsImgButton = new Ext.Button({
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});
	this.panel = Ext.create('Ext.Panel', {
				// width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};
