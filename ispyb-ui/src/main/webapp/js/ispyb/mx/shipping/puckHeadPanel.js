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
/*global Event */
/*global Ext */


// panel which contains information about shipping and dewar (in the Create Puck page)
function PuckHeadPanel(args) {
	var _this = this;
	this.width = 550;
	this.height = 500;

	this.onSavePuckInfo = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	
	this.shippingId = null;
	this.dewarId = null;
	this.containerId = null;
	
}

// returns the information filled in the panel
PuckHeadPanel.prototype.getPuckInfo = function () {
	var shipmentName = Ext.getCmp(this.panel.getItemId()).getValues().shipmentName;
	var dewarCode = Ext.getCmp(this.panel.getItemId()).getValues().dewarCode;
	var puckCode = Ext.getCmp(this.panel.getItemId()).getValues().puckCode;
	var info = [];
	info.shipmentName = shipmentName;
	info.dewarCode = dewarCode;
	info.puckCode = puckCode;
	info.shippingId = this.shippingId;
	info.dewarId = this.dewarId;
	info.containerId = this.containerId;
	return info;
};


// builds and returns the panel
PuckHeadPanel.prototype.getPanel = function (data) {
	var _this = this;
	
	var shipmentName = "";
	var dewarCode = "";
	var puckCode = "";
	var readOnlyShip = false;
	var readOnlyPuck = false;
	// fill information
	if (data && data.puckInfo) {
		shipmentName = data.puckInfo.shipmentName;
		dewarCode = data.puckInfo.dewarCode;
		puckCode = data.puckInfo.puckCode;
		_this.shippingId = data.puckInfo.shippingId;
		_this.dewarId = data.puckInfo.dewarId;
		_this.containerId = data.puckInfo.containerId;
		if (data.puckInfo.shippingId && data.puckInfo.shippingId != "null")
			readOnlyShip = true;
		if (data.puckInfo.containerId && data.puckInfo.containerId != "null")
			readOnlyPuck = true;
	}
	// main panel
	_this.panel = Ext.widget({
		xtype : 'form',
		layout : {
			type : 'vbox'
		},
		collapsible : true,
		frame : true,
		title: 'Puck Information',
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 100
		},
		defaultType : 'textfield',
		defaults : {
			labelStyle : 'padding-bottom:10px;'
		},
		style : {
			marginBottom : '10px'
		},
		items : [{
			fieldLabel : 'Shipping',
			name : 'shipmentName',
			tooltip : "Shipping Name",
			value: shipmentName,
			readOnly: readOnlyShip,
			allowBlank : false,
			maxLength : 45, 
			hideTrigger : true
		},
		{
			fieldLabel : 'Dewar',
			name : 'dewarCode',
			tooltip : "Dewar code",
			value: dewarCode,
			readOnly: readOnlyShip,
			allowBlank : false,
			maxLength : 45, 
			hideTrigger : true
		},
		{
			fieldLabel : 'Puck',
			name : 'puckCode',
			tooltip : "Puck code",
			value: puckCode,
			readOnly: readOnlyPuck,
			allowBlank : false,
			maxLength : 45, 
			hideTrigger : true
		}]
	});

	return _this.panel;
};