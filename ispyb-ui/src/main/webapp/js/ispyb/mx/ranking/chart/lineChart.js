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
// Line chart for autoproc ranking chart
function LineChart(targetId) {
	var _this = this;
	this.targetId = targetId;
	this.store = null;
	this.chart = null;
	this.panel = null;
}

// build the data for the chart
LineChart.prototype.formatData = function(data) {
	var _this = this;
	var d = [];
	for (i = 0; i < data.length; i++) {
		var s = {
			dataCollectionId : data[i].dataCollectionId,
			imgPrefix : data[i].imagePrefix,
			rFactor : data[i].overallRFactorValue,
			highestResolution : data[i].highestResolutionValue,
			completeness : data[i].completenessValue
		};
		d.push(s);
	}

	_this.store = Ext.create('Ext.data.JsonStore', {
				fields : ['dataCollectionId', 'imgPrefix', 'rFactor',
						'highestResolution', 'completeness'],
				data : d
			});
};

// builds and returns the line chart
LineChart.prototype.getChart = function() {
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
							minimum : 0,
							position : 'left',
							fields : ['rFactor', 'highestResolution',
									'completeness'],
							minorTickSteps : 1,
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
							fields : ['imgPrefix'],
							label : {
								font : '9px Arial',
								rotate : {
									degrees : 300
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
							xField : 'imgPrefix',
							yField : 'rFactor',
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
							axis : 'left',
							smooth : true,
							xField : 'imgPrefix',
							yField : 'highestResolution',
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
							axis : 'left',
							smooth : true,
							xField : 'imgPrefix',
							yField : 'completeness',
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


//builds and returns the panel containing the line chart
LineChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.formatData(data);
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
				width : 800, // panel's width
				height : 400,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};
