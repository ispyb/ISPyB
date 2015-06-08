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
// stacked bar chart for the autoproc ranking 
function StackedBarChart(targetId) {
	var _this = this;
	this.targetId = targetId;
	this.store = null;
	this.chart = null;
	this.panel = null;
}

// format the data for the chart
StackedBarChart.prototype.formatData = function(data) {
	var _this = this;
	var d = [];
	for (i = 0; i < data.length; i++) {
		var s = {
			dataCollectionId : data[i].dataCollectionId,
			imgPrefix : data[i].imagePrefix + ' ' + data[i].dataCollectionNumber,
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


// builds and returns the stacked bar chart
StackedBarChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				animate : true,
				shadow : true,
				store : _this.store,
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Numeric',
							position : 'bottom',
							fields : ['rFactor', 'highestResolution',
									'completeness'],
							title : false,
							grid : true
						}, {
							type : 'Category',
							position : 'left',
							fields : ['imgPrefix'],
							title : false
						}],
				series : [{
							type : 'bar',
							axis : 'bottom',
							gutter : 80,
							xField : 'dataCollectionId',
							yField : ['rFactor', 'highestResolution',
									'completeness'],
							stacked : true,
							tips : {
								trackMouse : true,
								width : 65,
								height : 28,
								renderer : function(storeItem, item) {
									this.setTitle(String(item.value[1]));
								}
							}
						}]

			});
	return _this.chart;

};


// builds and returns the main panel containing the stacked bar chart
StackedBarChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.formatData(data);
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtn') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtn',
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
