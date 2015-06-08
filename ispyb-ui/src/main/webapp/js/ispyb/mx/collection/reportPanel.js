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
// Report panel in the dataCollection page
function ReportPanel(args) {
	var _this = this;
	this.width = 500;
	this.height = 250;
	
	this.isFx = false;
	
	this.isIndustrial = false;

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
ReportPanel.prototype.getPanel = function(data) {
	var _this = this;

	var contextPath = "";
	var sessionId = null;
	if (data && data.contextPath) {
		contextPath = data.contextPath;
	}
	if (data && data.sessionId ){
		sessionId = data.sessionId;
	}
	if (data && data.isFx){
		this.isFx = data.isFx;
	}
	
	if (data && data.isIndustrial){
		this.isIndustrial = data.isIndustrial;
	}
	
	var buttonDOC = new Ext.Button({
		icon : '../images/Word.png',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportAsRtf&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		tooltip : 'Export this table as DOC'
	});
	var buttonPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportAsPdf&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export this table as PDF'
	});
	var buttonCSV = new Ext.Button({
		icon : '../images/icon_excel07.jpg',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportAsCsv&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export this table as CSV'
	});
	
	var buttonSendPDF = new Ext.Button({
		icon : '../images/pdfSend.png',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportAndSendAsPdf&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export and send as PDF'
	});
	
	var buttonScreeningDOC = new Ext.Button({
		icon : '../images/Word.png',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportScreeningAsRtf&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		tooltip : 'Export Screenings into DOC'
	});
	var buttonScreeningPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportScreeningAsPdf&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export Screenings into PDF'
	});
	
	var itemsGeneralReport = [];
	itemsGeneralReport.push(buttonDOC, buttonPDF, buttonCSV);
	if (_this.isFx && !_this.isIndustrial){
		itemsGeneralReport.push(buttonSendPDF);
	}
	
	_this.panelGeneralReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border:0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : itemsGeneralReport
	});
	
	var itemsScreeningReport = [];
	itemsScreeningReport.push(buttonScreeningDOC, buttonScreeningPDF);
	
	_this.panelScreeningReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border:0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : itemsScreeningReport
	});

	//main panel
	_this.panel = Ext.create('Ext.form.Panel', {
		layout : {
			type : 'vbox',
			border : 0,
			bodyPadding : 40
		},
		border:0,
		style : {
			marginLeft : '10px', 
			marginTop : '10px'
		},
		fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 200
		},
		collapsible : true,
		title: 'Reports', 
		frame : true,
		items : [
				{
					xtype : 'displayfield',
					fieldLabel : 'General report',
					labelStyle : 'font-weight:bold;',
					value : ''
				},
				_this.panelGeneralReport, 
				{
					xtype : 'displayfield',
					fieldLabel : 'Screening report',
					labelStyle : 'font-weight:bold;',
					value : ''
				},
				_this.panelScreeningReport
				]
	});
	

	return _this.panel;
};

