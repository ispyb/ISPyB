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

Ext.Loader.setConfig({
			enabled : true
		});

var autoprocessingPanel = null;
var tabs = null;
// main entry point for the result Page
var IspybResult = {
	start : function(targetId) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
				'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*', 'Ext.state.*',
				'Ext.form.*', 'Ext.ux.CheckColumn', 'Ext.ux.RowExpander',
				'Ext.selection.CheckboxModel', 'Ext.selection.CellModel',
				'Ext.layout.container.Column', 'Ext.tab.*',
				'Ext.ux.grid.FiltersFeature', 'Ext.selection.CellModel',
				'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
				'Ext.ux.form.MultiSelect', 'Ext.ux.form.ItemSelector',
				'Ext.chart.*', 'Ext.Window', 'Ext.fx.target.Sprite',
				'Ext.layout.container.Fit', 'Ext.window.MessageBox','Ext.ux.SimpleIFrame',
				'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showResult(targetId);
	},

	showResult : function(targetId) {
		var _this = this;
		// ajax calls
		var result = new Result();

		// experiment tab
		var experimentPanel = new ExperimentPanel();
		//beamline param tab
		var beamlinePanel = new BeamlinePanel();
		// EDNA tabs
		var characterisationParamPanel = new CharacterisationParamPanel();
		
		var characterisationResultPanel = new CharacterisationResultPanel();
		// autoProc tab
		var autoProcProgramAttachmentGraph = new AutoProcProgramAttachmentGraph();
		// denso tab
		var denzoPanel = new DenzoPanel();

		// main autoProc panel 
		autoprocessingPanel = new AutoprocessingPanel();
		// listen for events
		autoprocessingPanel.onAutoProcSelected.attach(function(sender, args) {
					var autoProcId = args.autoProcId;
					result.getAutoprocessingDetails(autoProcId);
				});
		autoprocessingPanel.onAutoProcGraphSelected.attach(function(sender,args) {
			var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
			autoProcProgramAttachmentGraph.getData(autoProcProgramAttachmentId);
		});

		// listen for events
		result.resultRetrieved.attach(function(evt, data) {
			// errrors
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			// no errors => create the tabs
			if (tabs == null){
				var items = [];
				var idCharacterisationResults = 0;
				var idAutoprocessing = 2;
				items.push(
					{
						tabConfig : {
							id : 'experiment',
							title : "Experiment parameters"
						},
						items : [experimentPanel.getPanel(data)]
					}, 
					{
						tabConfig : {
							id : 'beamline',
							title : "Beamline parameters"
						},
						items : [beamlinePanel.getPanel(data)]
					}
				);
				if (data.displayOutputParam) {
					idCharacterisationResults = 3;
					idAutoprocessing = 4;
					items.push(
						{
							tabConfig : {
								id : 'characterisationParameter',
								title : "Characterisation parameters"
							},
							items : [characterisationParamPanel.getPanel(data)]
						}, 
						{
							tabConfig : {
								id : 'characterisationResults',
								title : "Characterisation results"
							},
							items : [characterisationResultPanel.getPanel(data)]
						}
					);
				}
				
				items.push(
					{
						tabConfig : {
							id : 'autoprocessing',
							title : "AutoProcessing"
						},
						items : [autoprocessingPanel.getPanel(data)]
					}
				);
				
				if (data.displayDenzoContent) {
					items.push(
						{
							tabConfig : {
								id : 'denzo',
								title : "Denzo files"
							},
							items : [denzoPanel.getPanel(data)]
						}
					);
				}
				
				var tabs = Ext.create('Ext.tab.Panel', {
							renderTo : targetId,
							activeTab : 0,
							defaults : {
								bodyPadding : 0,
								autoScroll : true
							},
							items : items
				});
				
				// by default set activ the correct tab
				if (data.isEDNACharacterisation){
					tabs.setActiveTab(idCharacterisationResults);
				}
				if (data.isAutoprocessing) {
					tabs.setActiveTab(idAutoprocessing);
				}
				
				// if a autoProc is selected => update the data
				if (data && data.autoProcIdSelected && data.autoProcIdSelected != null) {
					autoprocessingPanel.setSelectedAutoProcId(data.autoProcIdSelected);
					result.getAutoprocessingDetails(data.autoProcIdSelected);
				}
			}else{
				autoprocessingPanel.setAutoProcList(data);
			}
		});	
		result.autoProcDetailRetrieved.attach(function(evt, data) {
			autoprocessingPanel.displayAutoProc(data);
		});

		autoProcProgramAttachmentGraph.dataRetrieved.attach(
			function(evt, data) {
				if (data.errors && data.errors.length > 0) {
					var args = [];
					args.targetId = targetId;
					args.errors = data.errors;
					var errorPanel = new ErrorPanel(args);
					return;
				}
				if (data.autoProcessingData && data.autoProcessingData.length > 0) {
					autoprocessingPanel.displayGraphAttachment(data);
				}
		});

		// retrieve the results for the collect
		result.getResult(10.0, 1.0);
	}

};
