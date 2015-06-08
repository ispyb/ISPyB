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
// Experiment tab
function ExperimentPanel(args) {
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
ExperimentPanel.prototype.getPanel = function (data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	var marginBottom = "0 0 20 0";
	var dataCollection = data.dataCollection;
	
	var wavelength = "";
	if (dataCollection && dataCollection.wavelength) {
		wavelength = dataCollection.wavelength + " &Aring;";
	}
		
	var energy = "";
	if (dataCollection && dataCollection.energy) {
		energy = dataCollection.energy + " keV";
	}
	var omegaStart = "";
	if (dataCollection) {
		omegaStart = dataCollection.axisStart + " &deg";
	}
	
	var totalAbsorbedDose = "";
	if (dataCollection && dataCollection.totalAbsorbedDose) {
		totalAbsorbedDose = dataCollection.totalAbsorbedDose + " Gy";
	}
	
	var items = [];
	items.push({
			xtype : 'displayfield',
			name : 'dataCollection.imageDirectory',
			fieldLabel : 'Img directory',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.imageDirectory:""
		}, 
		{
			xtype : 'displayfield',
			name : 'dataCollection.imageprefix',
			fieldLabel : 'Img prefix',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.imagePrefix:""
		},
		{
			xtype : 'displayfield',
			name : 'dataCollection.numberOfImages',
			fieldLabel : 'Nb of images',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.numberOfImages:""
		},

		{
			xtype : 'displayfield',
			name : 'dataCollection.dataCollectionNumber',
			fieldLabel : 'Run no',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.dataCollectionNumber:""
		}, 
		{
			xtype : 'displayfield',
			name : 'dataCollection.startTime',
			fieldLabel : 'Start Time',
			labelStyle : labelStyle,
			value : dataCollection ? dataCollection.startTime:""
		}, 
		{
			xtype : 'displayfield',
			name : 'dataCollection.endTime',
			fieldLabel : 'End Time',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.endTime:""
		}, 
		{
			xtype : 'displayfield',
			name : 'dataCollectionGroup.experimentType',
			fieldLabel : 'Type of experiment',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.experimentType:"", 
			margin: marginBottom
		}
	);
	
	items.push(
		{
			xtype : 'displayfield',
			name : 'dataCollectionGroup.wavelength',
			fieldLabel : 'Wavelength',
			labelStyle : labelStyle,
			value : wavelength
		}, 
		{
			xtype : 'displayfield',
			name : 'dataCollectionGroup.energy',
			fieldLabel : 'Energy',
			labelStyle : labelStyle,
			value : energy, 
			margin: marginBottom
		}
	);
	items.push(
		{
			xtype : 'displayfield',
			name : 'omegaStart',
			fieldLabel : dataCollection?dataCollection.axisStartLabel:"omegaStart",
			labelStyle : labelStyle,
			value : omegaStart
		}, 
		{
			xtype : 'displayfield',
			name : 'axisRange',
			fieldLabel : 'Oscillation range',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.axisRange+ " &deg"):""
		},
		{
			xtype : 'displayfield',
			name : 'overlap',
			fieldLabel : 'Overlap',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.overlap+ " &deg"):""
		},
		{
			xtype : 'displayfield',
			name : 'exposureTime',
			fieldLabel : 'Exposure Time',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.exposureTime+ " s"):""
		},
		{
			xtype : 'displayfield',
			name : 'totalExposureTime',
			fieldLabel : 'Total Exposure Time',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.totalExposureTime+ " s"):""
		},
		{
			xtype : 'displayfield',
			name : 'estimatedTotalAbsorbedDose',
			fieldLabel : 'Estimated Total Absorbed Dose',
			labelStyle : labelStyle,
			value : totalAbsorbedDose
		},
		{
			xtype : 'displayfield',
			name : 'numberOfPasses',
			fieldLabel : 'Number of passes',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.numberOfPasses:"", 
			margin: marginBottom
		}
	);
		
	items.push(
		{
			xtype : 'displayfield',
			name : 'detectorDistance',
			fieldLabel : 'Detector Distance',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.detectorDistance?(dataCollection.detectorDistance.toFixed(2)+ " mm"):""):""
		},
		{
			xtype : 'displayfield',
			name : 'resolutionAtEdge',
			fieldLabel : 'Resolution at edge',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.resolution?(dataCollection.resolution.toFixed(2)+" &Aring;"):""):""
		},
		{
			xtype : 'displayfield',
			name : 'resolutionAtCorner',
			fieldLabel : 'Resolution at corner',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.resolutionAtCorner?(dataCollection.resolutionAtCorner.toFixed(2)+" &Aring;"):""):"", 
			margin: marginBottom
		}
	);
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'xbeam',
				fieldLabel : 'Xbeam',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.xbeam?(dataCollection.xbeam.toFixed(2)+ " mm"):""):""
			},
			{
				xtype : 'displayfield',
				name : 'ybeam',
				fieldLabel : 'Ybeam',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.ybeam?(dataCollection.ybeam.toFixed(2)+ " mm"):""):"", 
				margin: marginBottom
			}
	);
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'kappa',
				fieldLabel : 'Kappa',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.kappa:""
			},

			{
				xtype : 'displayfield',
				name : 'phi',
				fieldLabel : 'Phi',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.phi:"", 
				margin: marginBottom
			}
	);
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'comment',
				fieldLabel : 'Experiment comment',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.comments:""
			}
	);
	
	_this.panel =  Ext.create('Ext.form.Panel', {
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