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
// EDNA param panel tab
function CharacterisationParamPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";

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
CharacterisationParamPanel.prototype.getPanel = function(data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	var marginBottom = "0 0 20 0";
	var fieldStyleIndexing = 'color: black';
	var dataCollection = data.dataCollection;
	
	
	var screeningOutput = dataCollection.screeningOutput;
	var indexingStatus = "";
	var resolutionObserved = "";
	var mosaicity = "";
	var ioverSigma  = "N/A";
	var numSpotsUsed = "";
	var numSpotsFound = "";
	var spotDeviationTheta = "";
	var spotDeviationR = "";
	var beamShiftX  = "";
	var beamShiftY = "";
	var rankingResolution = "";
	if (screeningOutput){
		indexingStatus = screeningOutput.statusDescription;
		if (screeningOutput.mosaicity)
			mosaicity = screeningOutput.mosaicity.toFixed(2)+" &deg;";
		if (screeningOutput.resolutionObtained)
			resolutionObserved = screeningOutput.resolutionObtained+" &Aring;";
		if (screeningOutput.ioverSigma)
			ioverSigma = screeningOutput.ioverSigma;
		if (screeningOutput.numSpotsUsed)
			numSpotsUsed = screeningOutput.numSpotsUsed;
		if (screeningOutput.numSpotsFound)
			numSpotsFound = screeningOutput.numSpotsFound;	
		if (screeningOutput.spotDeviationTheta)
			spotDeviationTheta = screeningOutput.spotDeviationTheta.toFixed(2)+" &deg;";
		if (screeningOutput.spotDeviationR)
			spotDeviationR = screeningOutput.spotDeviationR;
		if (screeningOutput.beamShiftX)
			beamShiftX = screeningOutput.beamShiftX;	
		if (screeningOutput.beamShiftY)
			beamShiftY = screeningOutput.beamShiftY;	
		if (screeningOutput.rankingResolution)
			rankingResolution = screeningOutput.rankingResolution.toFixed(2)+" &Aring;";	
		if (screeningOutput.indexingSuccess){
			if (screeningOutput.indexingSuccess == 0){
				fieldStyleIndexing = 'color: red';
			}else{
				fieldStyleIndexing = 'color: green';
			}
		}
	}
	
	var screeningOutputLattice = dataCollection.lattice;
	var spaceGroup = "";
	var unitCellA = "";
	var unitCellB = "";
	var unitCellC = "";
	var unitCellAlpha = "";
	var unitCellBeta = "";
	var unitCellGamma = "";
	if (screeningOutputLattice){
		spaceGroup = screeningOutputLattice.spaceGroup;
		unitCellA = screeningOutputLattice.unitCell_a.toFixed(3)+" &Aring;";
		unitCellB = screeningOutputLattice.unitCell_b.toFixed(3)+" &Aring;";
		unitCellC = screeningOutputLattice.unitCell_c.toFixed(3)+" &Aring;";
		unitCellAlpha = screeningOutputLattice.unitCell_alpha.toFixed(3)+ " &deg";
		unitCellBeta = screeningOutputLattice.unitCell_beta.toFixed(3)+ " &deg";
		unitCellGamma = screeningOutputLattice.unitCell_gamma.toFixed(3)+ " &deg";
	}
	 
	
	var items = [];
	items.push({
					xtype : 'displayfield',
					name : 'indexingStatus',
					fieldLabel : 'Indexing status',
					fieldStyle: fieldStyleIndexing,
					labelStyle : labelStyle,
					value : indexingStatus
				}, 
				{
					xtype : 'displayfield',
					name : 'spaceGroup',
					fieldLabel : 'Spacegroup',
					labelStyle : labelStyle,
					value : spaceGroup
				}, 
				{
					xtype : 'displayfield',
					name : 'unitCellA',
					fieldLabel : 'Unit Cell a',
					labelStyle : labelStyle,
					value : unitCellA
				} ,
				{
					xtype : 'displayfield',
					name : 'unitCellB',
					fieldLabel : 'Unit Cell b',
					labelStyle : labelStyle,
					value : unitCellB
				},
				{
					xtype : 'displayfield',
					name : 'unitCellC',
					fieldLabel : 'Unit Cell c',
					labelStyle : labelStyle,
					value : unitCellC
				},
				{
					xtype : 'displayfield',
					name : 'unitCellAlpha',
					fieldLabel : 'Unit Cell alpha',
					labelStyle : labelStyle,
					value : unitCellAlpha
				},
				{
					xtype : 'displayfield',
					name : 'unitCellBeta',
					fieldLabel : 'Unit Cell beta',
					labelStyle : labelStyle,
					value : unitCellBeta
				},
				{
					xtype : 'displayfield',
					name : 'unitCellGamma',
					fieldLabel : 'Unit Cell gamma',
					labelStyle : labelStyle,
					value : unitCellGamma
				},
				{
					xtype : 'displayfield',
					name : 'mosaicity',
					fieldLabel : 'Mosaicity',
					labelStyle : labelStyle,
					value : mosaicity
				},
				{
					xtype : 'displayfield',
					name : 'resolutionObserved',
					fieldLabel : 'Resolution observed',
					labelStyle : labelStyle,
					value : resolutionObserved
				},
				{
					xtype : 'displayfield',
					name : 'ioverSigma',
					fieldLabel : 'I/Sigl at that resolution',
					labelStyle : labelStyle,
					value : ioverSigma
				},
				{
					xtype : 'displayfield',
					name : 'numSpotsUsed',
					fieldLabel : 'Number of spots used',
					labelStyle : labelStyle,
					value : numSpotsUsed
				},
				{
					xtype : 'displayfield',
					name : 'numSpotsFound',
					fieldLabel : 'Number of spots total',
					labelStyle : labelStyle,
					value : numSpotsFound
				},
				{
					xtype : 'displayfield',
					name : 'spotDeviationTheta',
					fieldLabel : 'Spot deviation angular',
					labelStyle : labelStyle,
					value : spotDeviationTheta
				},
				{
					xtype : 'displayfield',
					name : 'spotDeviationR',
					fieldLabel : 'Spot deviation positional',
					labelStyle : labelStyle,
					value : spotDeviationR?spotDeviationR.toFixed(3):""
				},
				{
					xtype : 'displayfield',
					name : 'beamShiftX',
					fieldLabel : 'Beam shift x',
					labelStyle : labelStyle,
					value : beamShiftX?beamShiftX.toFixed(3):""
				},
				{
					xtype : 'displayfield',
					name : 'beamShiftY',
					fieldLabel : 'Beam shift y',
					labelStyle : labelStyle,
					value : beamShiftY?beamShiftY.toFixed(3):""
				},
				{
					xtype : 'displayfield',
					name : 'rankingResolution',
					fieldLabel : 'Ranking resolution',
					labelStyle : labelStyle,
					value : rankingResolution
				}
		);
	
	
	_this.panel = Ext.create('Ext.form.Panel', {
				layout : 'vbox',
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 200
				},
				defaultType : 'textfield',
				items : items

			});

	return _this.panel;
};