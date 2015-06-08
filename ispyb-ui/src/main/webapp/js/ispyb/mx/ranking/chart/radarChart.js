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
// radar chart for the autoproc ranking 
function RadarChart(targetId) {
	var _this = this;
	this.targetId = targetId;
	this.store = null;
	this.chart = null;
	this.panel = null;
}


// format the data for the radar chart
RadarChart.prototype.formatData = function(data) {
	var _this = this;
	var d = [];
	for (i = 0; i < data.length; i++) {
		var s = {
			dataCollectionId : data[i].dataCollectionId,
			imgPrefix : data[i].imagePrefix + ' '+ data[i].dataCollectionNumber,
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


// builds and returns the radar chart
RadarChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				id : 'chartCmp',
				xtype : 'chart',
				style : 'background:#fff',
				theme : 'Category2',
				insetPadding : 20,
				animate : true,
				store : _this.store,
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Radial',
							position : 'radial',
							label : {
								display : true
							}
						}],
				series : [{
							showInLegend : true,
							type : 'radar',
							xField : 'imgPrefix',
							yField : 'rFactor',
							style : {
								opacity : 0.4
							}
						}, {
							showInLegend : true,
							type : 'radar',
							xField : 'imgPrefix',
							yField : 'highestResolution',
							style : {
								opacity : 0.5
							}
						}, {
							showInLegend : true,
							type : 'radar',
							xField : 'imgPrefix',
							yField : 'completeness',
							style : {
								opacity : 0.4
							}
						}]
			});

	return _this.chart;

};


// builds and returns the panel containing the radar chart
RadarChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.formatData(data);
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtnR') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtnR',
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
