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
 * Brenchereau, M. Bodin,  A. De Maria Antolinos
 ******************************************************************************/
/*global Ext */

// help panel for the fillShipment page or create puck
function HelpPanel(args) {
	var _this = this;
	this.width = 550;
	this.height = 500;
	
	// if true display the whole help, otherwise, only regarding the puck creation
	this.allHelp = true;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.allHelp != null) {
			this.allHelp = args.allHelp;
		}
	}		
}

// builds and returns the main panel
HelpPanel.prototype.getPanel = function () {
	var _this = this;
	
	var helpText = "<div style=\"white-space:normal !important;word-wrap: break-word;\">";
	if (_this.allHelp == true){
		helpText += "<h4>Description</h4>";
		helpText += "This page allows you to describe the composition of your shipment, by editing the contents of the dewars.</br>";
	
		helpText += "</br><h4>Dewar level</h4>";
		helpText += "Each tab represents a dewar.</br>";
		helpText += "<img src=\"../images/Dewar.png\" alt=\"Dewar\">+: to add a new dewar to a shipment.</br>";
		helpText += "<img src=\"../images/Edit_16x16_01.png\" alt=\"Edit label\">: to change the name of the selected dewar. Remember that the names of the dewars must be unique within the same shipment.</br>";
		helpText += "<img src=\"../images/print_16x16.png\" alt=\"Print components labels\">: to print your component labels. Stick them to your shipment components prior to sending (see instructions on first page) in order to track progress to the beamline.</br>";
		helpText += "<img src=\"../images/editpaste.png\" alt=\"Paste puck\">: to paste the contents of a puck into the current dewar after you have copied them from the puck interface using " +
			"one of the two icons <img src=\"../images/editcopy.png\" alt=\"Copy puck\"> or <img src=\"../images/editcut.png\" alt=\"Cut puck\">. " +
			"You can then change the puck label and the samples names (using <img src=\"../images/Edit_16x16_01.png\" alt=\"Edit label\">at the appropriate level) which are automatically modified to avoid a duplication of the sample names in the same shipment.</br>";
		helpText += "<img src=\"../images/cancel.png\" alt=\"Delete dewar\">: to remove the selected dewar and its contents.</br>";
	
		helpText += "</br><h4>Puck level</h4>";
		helpText += "Each tab represents a puck. </br>";
	}
	helpText += "Each row of the table represents a sample at a given position in the puck. Make sure you provide data in all mandatory fields (highlighted in light red) for each sample. </br>";
	helpText += "<span style='background-color: #ffcc66'>11 to 16 rows</span> of the table are only used in case of UNIPUCKS pucks, not with SPINE Pucks. </br>";
	helpText += "Sample names: </br>";
	helpText += "- Make sure the sample name contains only a-z , A-Z or 0-9 or - or _ characters.</br>";
	helpText += "- Make sure the sample name is unique for a protein in the same shipment.</br>";
	helpText += "The experiment type should not be null, Default as default. </br>";
	helpText += "The top-left red icon in a cell <img src=\"../images/help/gridFieldUpdated.PNG\" alt=\"modification icon\"> indicates that the field has been updated. " +
			"You have to click on the <img src=\"../images/save.gif\" alt=\"Save\"> button to save any modifications. </br>";
	if (_this.allHelp == true){
		helpText += "<img src=\"../images/Basket.png\" alt=\"Puck\">+: to add a new puck to the selected dewar.</br>";
		helpText += "<img src=\"../images/Edit_16x16_01.png\" alt=\"Edit label\">: to change the selected puck label (to its barcode for example). Remember that the labels of the pucks must be unique within the same shipment.</br>";
		helpText += "<img src=\"../images/editcopy.png\" alt=\"Copy puck\">: to copy a puck and its contents. You can then paste this in any dewar (in this shipment or another), by clicking on the <img src=\"../images/editpaste.png\" alt=\"Paste puck\"> button at the dewar menu level.</br>";
		helpText += "<img src=\"../images/editcut.png\" alt=\"Cut puck\">: to copy the puck into the clipboard and remove it. You can then paste the puck into another dewar.</br>";
	}
	helpText += "<img src=\"../images/save.gif\" alt=\"Save\">: to save your modifications.</br>";
	if (_this.allHelp == true){
		helpText += "<img src=\"../images/cancel.png\" alt=\"Delete puck\">: to remove this puck and its contents.</br>";
	}
	helpText += "<img src=\"../images/recur.png\" alt=\"Reset\">: to reset the puck contents, all fields are set to their default values.</br>";
	helpText += "<img src=\"../images/help/changeSampleNameAuto.PNG\" alt=\"change sample name\">: to automatically update the names of the samples in the puck, with the same protein (protein acronym and space group), by adding an increment number to the name of the 1st sample.</br>";
	
	helpText += "</br><h4>Experiment type definitions</h4>";
	helpText += "These types are defined for automatic processes to be performed on that sample. This is an ongoing development. These parameters will only be taken in account for specific pilot studies. Don't use this field if you don't belong to that studies.</br>";
	helpText += "<b>Default</b> = manual intervention by the crystallographer at the beamline</br>";
	helpText += "<b>MXPressE</b> = the sample is automatically going through an autoloop centring followed by an X-ray centring, by an EDNA enhanced characterisation and a data collection based on the EDNA proposed diffraction plan</br>";
	helpText += "<b>MXPressO</b> = the sample is automatically going through an autoloop centring followed by an X-ray centring and a data collection of 180&deg; by steps of 0.2&deg; oscillations with an attenuated beam (value calculated on an average protein crystal size)</br>";
	helpText += "<b>MXPressI</b> = the sample is automatically going through an autoloop centring followed by an X-ray centring, an EDNA characterisation and a data collection of 180° by steps of 0.2° oscillations with an attenuated beam (value calculated on an average protein crystal size) using the start angle and resolution coming from the EDNA proposed diffraction plan.</br>";
	helpText += "<b>MXPressL</b> = the sample is automatically going through an autoloop centring followed by a data collection of 180&deg; by steps of 0.2&deg; oscillations with an attenuated beam (value calculated on an average protein crystal size)(MXPressO without X-ray centering)</br>";
	helpText += "<b>MXScore</b> = the sample is automatically going through an autoloop centring followed by an X-ray centring and an EDNA characterisation.</br>";
	
	helpText += "</br><h4>Resolution definitions</h4>";
	helpText += "By default if the fields are empty, a 2A resolution will be used for MXpressO and MXScore while the real sample resolution will be used for MXpressE, MXpressI.</br>";
	helpText += "<b>Pre-observed resolution</b> = resolution at which the detector will be set at the beginning of any worflow.</br>";
	helpText += "<b>Needed resolution</b> = in the case of a workflow with a successful EDNA characterisation, a collect will not be performed if the resolution observed is worse than this value.</br>";
	
	helpText += "</div>";
	
	//main panel
	_this.panel = Ext.widget({
		xtype : 'form',
		layout : {
			type : 'fit'
		},
		collapsible : true,
		collapsed:true,
		frame : true,
		title: 'Help',
		
		style : {
			marginBottom : '10px'
		},
		
		items : [ {
            xtype: 'component',
            style: 'margin-top:10px',
            html: helpText
        }]
	});

	return _this.panel;
};

