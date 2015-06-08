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

// panel containing the graph
function AutoProcProgramAttachmentGraphPanel(args) {
	var _this = this;
	this.width = 1000;
	this.height = 600;
	this.store = null;
	this.changeSize = true;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.changeSize != null) {
			this.changeSize = args.changeSize;
		}
	}
}

// builds and resturns the main panel
AutoProcProgramAttachmentGraphPanel.prototype.getPanel = function(data) {
	var _this = this;
	var d = [];
	var wilsonD = [];
	var cumIntD = [];
	// format data, depending of the graph
	for (i = 0; i < data.autoProcessingData.length; i++) {
		var s = {
			resolutionLimit : data.autoProcessingData[i].resolutionLimit,
			completeness : data.autoProcessingData[i].completeness,
			rFactorObserved : data.autoProcessingData[i].rFactorObserved,
			iSigma : data.autoProcessingData[i].iSigma,
			cc2 : data.autoProcessingData[i].cc2,
			sigAno : data.autoProcessingData[i].sigAno,
			anomalCorr : data.autoProcessingData[i].anomalCorr,
			resolution : data.autoProcessingData[i].resolution,
			wilsonPlot : data.autoProcessingData[i].wilsonPlot,
			z: data.autoProcessingData[i].z,
			acent_theor: data.autoProcessingData[i].acent_theor,
			acent_twin: data.autoProcessingData[i].acent_twin,
			acent_obser: data.autoProcessingData[i].acent_obser,
			centric_theor: data.autoProcessingData[i].centric_theor,
			centric_obser: data.autoProcessingData[i].centric_obser
		};
		d.push(s);
		if (data.autoProcessingData[i].wilsonPlot != null){
			wilsonD.push(s);
		}
		if (data.autoProcessingData[i].z != null){
			cumIntD.push(s);
		}
	}

	// data, depending of the graph
	var store = Ext.create('Ext.data.JsonStore', {
				fields : ['resolutionLimit', 'completeness', 'rFactorObserved',
						'iSigma', 'cc2', 'sigAno', 'anomalCorr', 'resolution',
						'wilsonPlot', 'z', 'acent_theor', 'acent_twin', 'acent_obser', 'centric_theor', 'centric_obser'],
				data : d
			});
	
	var storeWilson = Ext.create('Ext.data.JsonStore', {
		fields : ['resolutionLimit', 'completeness', 'rFactorObserved',
				'iSigma', 'cc2', 'sigAno', 'anomalCorr', 'resolution',
				'wilsonPlot', 'z', 'acent_theor', 'acent_twin', 'acent_obser', 'centric_theor', 'centric_obser'],
		data : wilsonD
	});
	
	var storeCumInt = Ext.create('Ext.data.JsonStore', {
		fields : ['resolutionLimit', 'completeness', 'rFactorObserved',
				'iSigma', 'cc2', 'sigAno', 'anomalCorr', 'resolution',
				'wilsonPlot', 'z', 'acent_theor', 'acent_twin', 'acent_obser', 'centric_theor', 'centric_obser'],
		data : cumIntD
	});

	var args = [];
	//args.width = _this.width - 10;
	//args.height = _this.height - 250;
	args.width = _this.width ;
	args.height = _this.height;
	if (_this.changeSize){
		args.width = _this.width - 10;
		args.height = _this.height - 250;
	}
	
	// creates the graphs
	var completeness = new AutoProcProgramAttachmentGraphChart(args);
	var rfactor = new AutoProcProgramAttachmentGraphChart(args);
	var iSigma = new AutoProcProgramAttachmentGraphChart(args);
	var cc2 = new AutoProcProgramAttachmentGraphChart(args);
	var sigAno = new AutoProcProgramAttachmentGraphChart(args);
	var anomalCorr = new AutoProcProgramAttachmentGraphChart(args);
	var wilsonPlot = new WilsonChart(args);
	var cumulativeIntensityDistribution = new CumulativeIntensityDistributionChart(args);

	if (data && data.xscaleLpData) {
		// XSCALE: tab with graphs based on resolution
		var tabs = Ext.create('Ext.tab.Panel', {
			width : this.width,
			height : this.height,
			activeTab : 0,
			defaults : {
				bodyPadding : 0
			},
			items : [
			{
				tabConfig : {
					title : "I/SigI vs Resolution"
				},
				items : [iSigma.getPanel(store, 'iSigma', 'I/Sigl')]
			}, {
				tabConfig : {
					title : "Completeness vs Resolution"
				},
				items : [completeness.getPanel(store, 'completeness',
						'Completeness')]
			}, {
				tabConfig : {
					title : "R-factor vs Resolution"
				},
				items : [rfactor.getPanel(store, 'rFactorObserved', 'R-factor')]
			},  {
				tabConfig : {
					title : "CC/2 vs Resolution"
				},
				items : [cc2.getPanel(store, 'cc2', 'CC/2')]
			}, {
				tabConfig : {
					title : "SigAno vs Resolution"
				},
				items : [sigAno.getPanel(store, 'sigAno', 'SigAno')]
			}, {
				tabConfig : {
					title : "Anom Corr vs Resolution"
				},
				items : [sigAno.getPanel(store, 'anomalCorr', 'Anom Corr')]
			}]
		});

		// panel with the tabs
		_this.panel = Ext.create('Ext.Panel', {
					layout : 'fit',
					width : this.width, // panel's width
					height : this.height,// panel's height
					items : [tabs]
				});
	} else if (data && data.truncateLogData) {
		// Wilson plot or Cumulative Intensity
		var tabs = Ext.create('Ext.tab.Panel', {
			width : this.width,
			height : this.height,
			activeTab : 0,
			defaults : {
				bodyPadding : 0
			},
			items : [{
				tabConfig : {
					title : "Wilson Plot"
				},
				items : [wilsonPlot.getPanel(storeWilson)]
			}, 
			{
				tabConfig : {
					title : "Cumulative Intensity Distribution"
				},
				items : [cumulativeIntensityDistribution.getPanel(storeCumInt)]
			}]
		});

		// main panel
		_this.panel = Ext.create('Ext.Panel', {
					layout : 'fit',
					width : this.width, // panel's width
					height : this.height,// panel's height
					items : [tabs]
				});
		
	}

	return _this.panel;
};