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
// left Panel with the data about the autoProcessing
function AutoProcLeftPanel(args) {
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
AutoProcLeftPanel.prototype.getPanel = function(data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'vbox'
				},
				collapsible : false,
				frame : true,
				title : 'Main Output Parameters',
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 150
				},
				defaultType : 'textfield',
				items : [{
							xtype : 'displayfield',
							fieldLabel : 'Overall',
							labelStyle : labelStyle
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Overall Resolution',
							id : 'overallResolution',
							value : data.autoProcDetail.overallResolution
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Overall Completeness',
							id : 'overallCompleteness',
							value : data.autoProcDetail.overallCompleteness
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Overall I over Sigma',
							id : 'overallIOverSigma',
							value : data.autoProcDetail.overallIOverSigma
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Overall Rsymm',
							id : 'overallRsymm',
							value : data.autoProcDetail.overallRsymm
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Overall Multiplicity',
							id : 'overallMultiplicity',
							value : data.autoProcDetail.overallMultiplicity
						},

						{
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell',
							labelStyle : labelStyle
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell Resolution',
							id : 'outerResolution',
							value : data.autoProcDetail.outerResolution
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell Completeness',
							id : 'outerCompleteness',
							value : data.autoProcDetail.outerCompleteness
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell I over Sigma',
							id : 'outerIOverSigma',
							value : data.autoProcDetail.outerIOverSigma
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell Rsymm',
							id : 'outerRsymm',
							value : data.autoProcDetail.outerRsymm
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell Multiplicity',
							id : 'outerMultiplicity',
							value : data.autoProcDetail.outerMultiplicity
						},

						{
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell',
							labelStyle : labelStyle
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell A',
							id : 'unitCellA',
							value : data.autoProcDetail.unitCellA
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell B',
							id : 'unitCellB',
							value : data.autoProcDetail.unitCellB
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell C',
							id : 'unitCellC',
							value : data.autoProcDetail.unitCellC
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell Alpha',
							id : 'unitCellAlpha',
							value : data.autoProcDetail.unitCellAlpha
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell Beta',
							id : 'unitCellBeta',
							value : data.autoProcDetail.unitCellBeta
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell Gamma',
							id : 'unitCellGamma',
							value : data.autoProcDetail.unitCellGamma
						}]

			});
	return _this.panel;
};


// autoProc has been selected => set the correct values
AutoProcLeftPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	var field = Ext.getCmp('overallResolution');
	field.setValue(data.autoProcDetail.overallResolution);

	field = Ext.getCmp('overallCompleteness');
	field.setValue(data.autoProcDetail.overallCompleteness);

	field = Ext.getCmp('overallIOverSigma');
	field.setValue(data.autoProcDetail.overallIOverSigma);

	field = Ext.getCmp('overallRsymm');
	field.setValue(data.autoProcDetail.overallRsymm);

	field = Ext.getCmp('overallMultiplicity');
	field.setValue(data.autoProcDetail.overallMultiplicity);

	field = Ext.getCmp('outerResolution');
	field.setValue(data.autoProcDetail.outerResolution);

	field = Ext.getCmp('outerCompleteness');
	field.setValue(data.autoProcDetail.outerCompleteness);

	field = Ext.getCmp('outerIOverSigma');
	field.setValue(data.autoProcDetail.outerIOverSigma);

	field = Ext.getCmp('outerRsymm');
	field.setValue(data.autoProcDetail.outerRsymm);

	field = Ext.getCmp('outerMultiplicity');
	field.setValue(data.autoProcDetail.outerMultiplicity);

	field = Ext.getCmp('unitCellA');
	field.setValue(data.autoProcDetail.unitCellA);

	field = Ext.getCmp('unitCellB');
	field.setValue(data.autoProcDetail.unitCellB);

	field = Ext.getCmp('unitCellC');
	field.setValue(data.autoProcDetail.unitCellC);

	field = Ext.getCmp('unitCellAlpha');
	field.setValue(data.autoProcDetail.unitCellAlpha);

	field = Ext.getCmp('unitCellBeta');
	field.setValue(data.autoProcDetail.unitCellBeta);

	field = Ext.getCmp('unitCellGamma');
	field.setValue(data.autoProcDetail.unitCellGamma);

};
