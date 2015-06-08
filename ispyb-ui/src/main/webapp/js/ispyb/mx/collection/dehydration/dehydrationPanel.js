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
// Dehydration panel with charts
function DehydrationPanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 700;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}


// builds and returns the main panel
DehydrationPanel.prototype.getPanel = function(data) {
	var _this = this;

	var dehydrationData = data.dehydrationValues;
	// the different grphs
	var exptCellTimeChart = new ExptCellTimeChart();
	var exptCellChart = new ExptCellChart();
	var exptLabelitChart = new ExptLabelitChart();
	var exptResChart = new ExptResChart();
	var exptResolutionTimeChart = new ExptResolutionTimeChart();
	var exptSpotsTimeChart = new ExptSpotsTimeChart();
	// format data for graphs
	var d = [];
	for (i = 0; i < dehydrationData.length; i++) {
		var s = {
			time : dehydrationData[i].time,
			relativeHumidity : dehydrationData[i].relativeHumidity,
			labelitR1 : dehydrationData[i].labelitR1,
			labelitR2 : dehydrationData[i].labelitR2,
			numberOfSpots : dehydrationData[i].numberOfSpots,
			braggPeaks : dehydrationData[i].braggPeaks,
			totalIntegratedSignal : dehydrationData[i].totalIntegratedSignal,
			cellEdgeA : dehydrationData[i].cellEdgeA,
			cellEdgeB : dehydrationData[i].cellEdgeB,
			cellEdgeC : dehydrationData[i].cellEdgeC,
			mosaicSpread : dehydrationData[i].mosaicSpread,
			rankingResolution : dehydrationData[i].rankingResolution
		};
		d.push(s);
	}

	// data for charts
	var store = Ext.create('Ext.data.JsonStore', {
				fields : ['time', 'relativeHumidity', 'rFactor', 'labelitR1',
						'labelitR2', 'numberOfSpots', 'braggPeaks',
						'totalIntegratedSignal', 'cellEdgeA', 'cellEdgeB',
						'cellEdgeC', 'mosaicSpread', 'rankingResolution'],
				data : d
			});
	// tabs with the different graphs
	var tabs = Ext.create('Ext.tab.Panel', {
				//width : this.width,
				//height : this.height,
				activeTab : 0,
				defaults : {
					bodyPadding : 0
				},
				items : [{
							tabConfig : {
								id : 'exptCellTimeChart',
								title : "Expt Cell Time"
							},
							items : [exptCellTimeChart.getPanel(store)]
						}, {
							tabConfig : {
								id : 'exptCellChart',
								title : "Expt Cell"
							},
							items : [exptCellChart.getPanel(store)]
						}, {
							tabConfig : {
								id : 'exptLabelitChart',
								title : "Expt labelit"
							},
							items : [exptLabelitChart.getPanel(store)]
						}, {
							tabConfig : {
								id : 'exptResChart',
								title : "Expt res"
							},
							items : [exptResChart.getPanel(store)]
						}, {
							tabConfig : {
								id : 'exptResolutionTimeChart',
								title : "Expt resolution time"
							},
							items : [exptResolutionTimeChart.getPanel(store)]
						}, {
							tabConfig : {
								id : 'exptSpotsTimeChart',
								title : "Expt spots time"
							},
							items : [exptSpotsTimeChart.getPanel(store)]
						}]
			});

	_this.panel = Ext.create('Ext.Panel', {
				//width : this.width, // panel's width
				//height : this.height,// panel's height
				layout : 'fit',
				items : [tabs]
			});

	return _this.panel;
};