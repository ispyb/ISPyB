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
/*global  Event*/
/*global  Ext*/
/*global  window*/

// Session Report page: report section
function ReportSessionPanel(args) {
	var _this = this;
	this.width = 500;
	this.height = 250;
	this.sessionId = null;
	this.contextPath = "";
	this.isFx = false;
	this.isIndus = false;
	this.send = false;
	/** Events * */
	this.onExportReportSelected = new Event(this);

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
ReportSessionPanel.prototype.getPanel = function (data) {
	var _this = this;

	_this.contextPath = "";
	_this.sessionId = null;
	if (data && data.contextPath) {
		_this.contextPath = data.contextPath;
	}
	if (data && data.sessionId) {
		_this.sessionId = data.sessionId;
	}
	if (data && data.isFx) {
		_this.isFx = data.isFx;
	}
	if (data && data.isIndus) {
		_this.isIndus = data.isIndus;
	}
	
	// MXPress reports
	var items = [];
//TODO fix pb of export in doc with images inside	
//	var buttonDOC = new Ext.Button({
//		icon : '../images/Word.png',
//		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportMXPressOWorkflowAsRtf&sessionId=" + _this.sessionId,
//		hrefTarget : '_self',
//		scale: 'large',
//		tooltip : 'Export the MXPressO/MXPressE pipeline report into DOC'
//	});
	var buttonPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportMXPressOWorkflowAsPdf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export the MXPressO/MXPressE pipeline report into PDF'
	});
	//items.push(buttonDOC, buttonPDF);
	items.push(buttonPDF);
	// mxPress panel reports
	_this.panelReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border: 0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : items
	});
	
	// General reports
	var itemsIndustrial = [];
	
	var buttonIndustrialDOC = new Ext.Button({
		icon : '../images/Word.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportIndustrialReportAsRtf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		tooltip : 'Export the report into DOC'
	});
	var buttonIndustrialPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportIndustrialReportAsPdf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export the report into PDF'
	});
	var buttonIndustrialPDFSend = new Ext.Button({
		icon : '../images/pdfSend.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportIndustrialReportAndSendAsPdf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export the report and send as PDF'
	});
	itemsIndustrial.push(buttonIndustrialDOC, buttonIndustrialPDF);
	if (_this.isFx && !_this.isIndus) {
		itemsIndustrial.push(buttonIndustrialPDFSend);
	}
	_this.panelIndustrialReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border: 0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : itemsIndustrial
	});
	
	
	// Detailed Screening reports
	var generalItems = [];
	_this.rtf = false;
	var buttonGDOC = new Ext.Button({
		icon : '../images/Word.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAsRtf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		tooltip : 'Export the Session report into DOC',
		handler: function (button, evt) {
			_this.rtf = true;
			_this.send = false;
			_this.onExportReportSelected.notify({});
		}
	});
	var buttonGPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAsPdf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export the Session report into PDF',
		handler: function (button, evt) {
			_this.rtf = false;
			_this.send = false;
			_this.onExportReportSelected.notify({});
		}
	});
	var buttonGPDFSend = new Ext.Button({
		icon : '../images/pdfSend.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAsPdf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export the Session report and send as PDF',
		handler: function (button, evt) {
			_this.rtf = false;
			_this.send = true;
			_this.onExportReportSelected.notify({});
		}
	});
	generalItems.push(buttonGDOC, buttonGPDF);
	if (_this.isFx && !_this.isIndus) {
		generalItems.push(buttonGPDFSend);
	}
	_this.panelGeneralReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border: 0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : generalItems
	});
	
	
	// 
	var allItems = [];
	if (data.sessionSummary && data.sessionSummary == true) {
		allItems.push({
			xtype : 'displayfield',
			fieldLabel : 'General report',
			labelStyle : 'font-weight:bold;',
			value : ''
		},
			_this.panelIndustrialReport);
		allItems.push({
			xtype : 'displayfield',
			fieldLabel : 'Screening report',
			labelStyle : 'font-weight:bold;',
			value : ''
		},
			_this.panelGeneralReport);
	}
	if (data.sessionHasMXpressOWF && data.sessionHasMXpressOWF == true) {
		allItems.push({
			xtype : 'displayfield',
			fieldLabel : 'MXPressO-MXPressE report',
			labelStyle : 'font-weight:bold;',
			value : ''
		},
		_this.panelReport);
	}
	
	
	// main panel
	_this.panel = Ext.create('Ext.form.Panel', {
		layout : {
			type : 'vbox',
			border : 0,
			bodyPadding : 40
		},
		border: 0,
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
		items : allItems
	});
	
	
	

	return _this.panel;
};

// export the report event: redirection
ReportSessionPanel.prototype.exportSessionReport = function () {
	var _this = this;
	if (_this.rtf) {
		window.location.href = _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAsRtf&sessionId=" + _this.sessionId;
	}
	else {
		if (_this.send) {
			window.location.href = _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAndSendAsPdf&sessionId=" + _this.sessionId;
		} else {
			window.location.href = _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAsPdf&sessionId=" + _this.sessionId;
		}
	}
};
