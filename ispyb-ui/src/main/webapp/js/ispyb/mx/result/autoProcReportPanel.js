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
// panel with the report buttons
function AutoProcReportPanel(args) {
	var _this = this;
	this.width = "410";
	this.height = "100";

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	this.panel = null;
}

// builds the main panel
AutoProcReportPanel.prototype.getPanel = function(data) {
	var _this = this;
	var title = "<p>View Data Collection Statistics</p>";
	// get the itesm
	var items = _this.getItems(data);
	
	// panel
	_this.panelReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border:0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : items
	});
	// main panel (title + buttons)
	_this.panel = Ext.create('Ext.form.Panel', {
		layout : {
			type : 'vbox',
			border : 0,
			bodyPadding : 40
		},
		border:0,
		style : {
			marginLeft : '10px' 
		},
		fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 200
		},
		collapsible : false,
		frame : true,
		items : [
				{
					xtype : 'displayfield',
					fieldLabel : 'View Data Collection Statistics',
					labelStyle : 'font-weight:bold;',
					value : ''
				},
				_this.panelReport
				]
	});
	return _this.panel;
};


// builds the items
AutoProcReportPanel.prototype.getItems = function(data) {
	var _this = this;
	var autoprocId = null;
	if (data && data.autoProcDetail) {
		autoprocId = data.autoProcDetail.autoProcId;
	}
	var contextPath = "";
	if (data && data.contextPath) {
		contextPath = data.contextPath;
	}
	var items = [];
	
	// 2 buttons
	var buttonDOC = new Ext.Button({
		icon : '../images/Word.png',
		href : contextPath +"/user/exportAutoProc.do?reqCode=exportAutoProcAsRtf&autoProcId=" +autoprocId,
		hrefTarget : '_self',
		scale:'large',
		tooltip : 'Export Auto Processing into DOC'
	});
	var buttonPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : contextPath +"/user/exportAutoProc.do?reqCode=exportAutoProcAsPdf&autoProcId=" +autoprocId,
		hrefTarget : '_self',
		scale:'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export Auto Processing into PDF'
	});
	
	items.push(buttonDOC, buttonPDF);
	return items;
	
};


// autoProc has been selected => update the panel
AutoProcReportPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	var f;
	while (f = _this.panelReport.items.first()) {
		_this.panelReport.remove(f, true);
	}
	var items = _this.getItems(data);
	_this.panelReport.add(items);
	_this.panelReport.doLayout();
	_this.panel.doLayout();
};