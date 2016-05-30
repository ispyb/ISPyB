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
// Beamline Parameters tab
function BeamlinePanel(args) {
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
BeamlinePanel.prototype.getPanel = function(data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	var marginBottom = "0 0 20 0";
	var dataCollection = data.dataCollection;
	var beamline = data.beamline;
	var session = data.session;
	var detector = data.detector;
	
	var undulatorTypes = "";
	if (beamline){
		if (beamline.undulatorType1){
			undulatorTypes += beamline.undulatorType1+" ";
		}
		if (beamline.undulatorType2){
			undulatorTypes += beamline.undulatorType2+" ";
		}
		if (beamline.undulatorType3){
			undulatorTypes += beamline.undulatorType3+" ";
		}
	}
	
	var flux = "";
	if (dataCollection && dataCollection.flux){
		flux = dataCollection.flux.toExponential() +"  photons/sec";
	}
	
	var flux_end = "";
	if (dataCollection && dataCollection.flux_end){
		flux_end = dataCollection.flux_end.toExponential() +"  photons/sec";
	}
	
	var detectorType = "";
	var detectorName = "";
	var detectorManufacturer = "";
	var detectorMode = "";
	if (detector){
		detectorType = detector.detectorType;
		detectorName = detector.detectorModel;
		detectorManufacturer = detector.detectorManufacturer;
		detectorMode = detector.detectorMode;
	}
	
	var items = [];
	items.push(
			{
				xtype : 'displayfield',
				name : 'synchrotronName',
				fieldLabel : 'Synchrotron name',
				labelStyle : labelStyle,
				value : beamline?beamline.synchrotronName:""
			},
			{
				xtype : 'displayfield',
				name : 'synchrotronMode',
				fieldLabel : 'Synchrotron filling mode',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.synchrotronMode:"",
				margin: marginBottom
			}
	);
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'beamlineName',
				fieldLabel : 'Beamline name',
				labelStyle : labelStyle,
				value : session?session.beamlineName:""
			},
			{
				xtype : 'displayfield',
				name : 'undulatorTypes',
				fieldLabel : 'Undulator types',
				labelStyle : labelStyle,
				value : undulatorTypes
			},
			{
				xtype : 'displayfield',
				name : 'undulatorGaps',
				fieldLabel : 'Undulator gaps',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.undulatorGaps:"",
				margin: marginBottom
			}
	);
		
	items.push(
			{
				xtype : 'displayfield',
				name : 'beamTransmission',
				fieldLabel : 'Beam transmission',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.transmission?(dataCollection.transmission.toFixed(2)+ " %" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'slitGapHorizontalMicro',
				fieldLabel : 'Slit gap Hor',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.slitGapHorizontalMicro?(dataCollection.slitGapHorizontalMicro+ " &mu;m" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'slitGapVerticalMicro',
				fieldLabel : 'Slit gap Vert',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.slitGapVerticalMicro?(dataCollection.slitGapVerticalMicro+ " &mu;m" ):""):"",
				margin: marginBottom
			}
	);
		
	items.push(
			{
				xtype : 'displayfield',
				name : 'detectorType',
				fieldLabel : 'Detector type',
				labelStyle : labelStyle,
				value : detectorType 
			},
			{
				xtype : 'displayfield',
				name : 'detectorName',
				fieldLabel : 'Detector name',
				labelStyle : labelStyle,
				value : detectorName 
			}, 
			{
				xtype : 'displayfield',
				name : 'detectorManufacturer',
				fieldLabel : 'Detector manufacturer',
				labelStyle : labelStyle,
				value : detectorManufacturer 
			},
			{
				xtype : 'displayfield',
				name : 'detectorMode',
				fieldLabel : 'Detector mode',
				labelStyle : labelStyle,
				value : detectorMode 
			}, 
			{
				xtype : 'displayfield',
				name : 'detectorPixelSizeHorizontalMicro',
				fieldLabel : 'Detector pixel size Hor',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.detectorPixelSizeHorizontalMicro?(dataCollection.detectorPixelSizeHorizontalMicro +"  mm"):""):""
			}, 
			{
				xtype : 'displayfield',
				name : 'detectorPixelSizeVerticalMicro',
				fieldLabel : 'Detector pixel size Vert',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.detectorPixelSizeVerticalMicro?(dataCollection.detectorPixelSizeVerticalMicro +"  mm"):""):"", 
				margin: marginBottom
			}
	);
	
	
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'focusingOptic',
				fieldLabel : 'Focusing optics',
				labelStyle : labelStyle,
				value : beamline?beamline.focusingOptic:""
			},
			{
				xtype : 'displayfield',
				name : 'monochromatorType',
				fieldLabel : 'Monochromator type',
				labelStyle : labelStyle,
				value : beamline?beamline.monochromatorType :""
			},
			{
				xtype : 'displayfield',
				name : 'beamShape',
				fieldLabel : 'Beam shape',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.beamShape :""
			},
			{
				xtype : 'displayfield',
				name : 'flux',
				fieldLabel : 'Flux',
				labelStyle : labelStyle,
				value : flux
			},
			{
				xtype : 'displayfield',
				name : 'fluxEnd',
				fieldLabel : 'Flux end',
				labelStyle : labelStyle,
				value :flux_end 
			},
			{
				xtype : 'displayfield',
				name : 'beamSizeAtSampleXMicro',
				fieldLabel : 'Beam size at sample Hor',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.beamSizeAtSampleXMicro?(dataCollection.beamSizeAtSampleXMicro  + " &mu;m" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'beamSizeAtSampleYMicro',
				fieldLabel : 'Beam size at sample Vert',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.beamSizeAtSampleYMicro?(dataCollection.beamSizeAtSampleYMicro  + " &mu;m" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'beamDivergenceHorizontalInt',
				fieldLabel : 'Beam divergence Hor',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.beamDivergenceHorizontalInt?(dataCollection.beamDivergenceHorizontalInt  + " &mu;rad" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'beamDivergenceVerticalInt',
				fieldLabel : 'Beam divergence Vert',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.beamDivergenceVerticalInt?(dataCollection.beamDivergenceVerticalInt  + " &mu;rad" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'polarisation',
				fieldLabel : 'Polarisation',
				labelStyle : labelStyle,
				value : beamline?(beamline.polarisation?(beamline.polarisation  + " &deg"):""):""
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