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
 * Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/


// session form class, used in the session page or in hte header of the dataCollection page
function SessionForm(args) {
	this.actions = [];

	// industrial user
	this.isFx = false;
	this.isIx = false;

	if (args != null) {
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.isIx != null) {
			this.isIx = args.isIx;
		}
	}

	this.session = null;
	this.emailButton = null;

	/** STRUCTURES * */

}

// returns the session object from the values filled in the form
SessionForm.prototype.getSession = function() {
	var _this = this;
	this.session.beamlineOperator = Ext.getCmp(this.panel.getItemId())
			.getValues().beamlineOperator;
	this.session.comments = Ext.getCmp(this.panel.getItemId()).getValues().comments;
	if (_this.isFx) {
		this.session.sessionTitle = Ext.getCmp(this.panel.getItemId())
				.getValues().sessionTitle;
		this.session.structureDeterminations = Ext.getCmp(this.panel
				.getItemId()).getValues().structureDeterminations;
		if (this.session.structureDeterminations == "") {
			this.session.structureDeterminations = 0;
		}
	}
	if (_this.isFx || _this.isIx) {
		this.session.dewarTransport = Ext.getCmp(this.panel.getItemId())
				.getValues().dewarTransport;
		this.session.databackupFrance = Ext.getCmp(this.panel.getItemId())
				.getValues().databackupFrance;
		this.session.databackupEurope = Ext.getCmp(this.panel.getItemId())
				.getValues().databackupEurope;
		if (this.session.dewarTransport == "") {
			this.session.dewarTransport = 0;
		}
		if (this.session.databackupFrance == "") {
			this.session.databackupFrance = 0;
		}
		if (this.session.databackupEurope == "") {
			this.session.databackupEurope = 0;
		}
	}
	return this.session;
};


// refresh function, called after the change of the beamline operator
SessionForm.prototype.refresh = function(session) {
	this.session = session;
	this.emailButton.setTooltip('mail to: ' + this.session.beamLineOperatorEmail);
	this.emailButton.setHandler(function() {
				window.location = 'mailto:' + session.beamLineOperatorEmail;
			});
};


// builds and returns the session form
SessionForm.prototype.getPanel = function(session) {
	this.session = session;
	var _this = this;
	var sessionWidth = 380;
	var sessionHeight = 200;
	var labelWidth = 90;
	var sessionItems = [];
	// local contact
	sessionItems.push({
				fieldLabel : 'Local Contact',
				name : 'beamlineOperator',
				//anchor : '95%',
				tooltip : "Local contact beamline Operator",
				value : this.session.beamlineOperator
			});
	// email local contact
	this.emailButton = new Ext.Button({
				text : '',
				handler : function() {
					window.location = 'mailto:' + session.beamLineOperatorEmail;
				},
				tooltip : 'mail to: ' + this.session.beamLineOperatorEmail,
				icon : '../images/mail_generic.png'
			});
	if (this.session.beamLineOperatorEmail && this.session.beamLineOperatorEmail != "")
		sessionItems.push(this.emailButton);
	//comments
	sessionItems.push({
				xtype : 'textareafield',
				name : 'comments',
				width: 500,
				grow: true,
				fieldLabel : 'Comments',
				//anchor : '95%',
				tooltip : "Session comments",
				value : this.session.comments
			});
	// display only for FX accounts
	if (_this.isFx) {
		labelWidth = 160;
		sessionHeight = 330;
		sessionItems.push({
					fieldLabel : 'Session Title',
					name : 'sessionTitle',
					//anchor : '95%',
					tooltip : "Session title",
					value : this.session.sessionTitle
				}, {
					fieldLabel : 'Structure Determinations',
					name : 'structureDeterminations',
					//anchor : '95%',
					tooltip : "Structure Determinations",
					value : this.session.structureDeterminations,
					xtype : 'numberfield',
					hideTrigger : true
				}, {
					fieldLabel : 'Dewar Transport',
					name : 'dewarTransport',
					//anchor : '95%',
					tooltip : "Dewar Transport",
					value : this.session.dewarTransport,
					xtype : 'numberfield',
					hideTrigger : true
				}, {
					fieldLabel : 'Data backup &<br/>Express delivery France',
					name : 'databackupFrance',
					//anchor : '95%',
					tooltip : "Data backup & Express delivery France",
					value : this.session.databackupFrance,
					xtype : 'numberfield',
					hideTrigger : true
				}, {
					fieldLabel : 'Data backup &<br/>Express delivery Europe',
					name : 'databackupEurope',
					//anchor : '95%',
					tooltip : "Data backup & Express delivery Europe",
					value : this.session.databackupEurope,
					xtype : 'numberfield',
					hideTrigger : true
				});
	}
	// display only for IX accounts
	else if (_this.isIx) {
		labelWidth = 160;
		sessionHeight = 270;
		sessionItems.push({
					fieldLabel : 'Dewar Transport',
					name : 'dewarTransport',
					//anchor : '95%',
					tooltip : "Dewar Transport",
					value : this.session.dewarTransport,
					xtype : 'numberfield',
					hideTrigger : true
				}, {
					fieldLabel : 'Data backup &<br/>Express delivery France',
					name : 'databackupFrance',
					//anchor : '95%',
					tooltip : "Data backup & Express delivery France",
					value : this.session.databackupFrance,
					xtype : 'numberfield',
					hideTrigger : true
				}, {
					fieldLabel : 'Data backup &<br/>Express delivery Europe',
					name : 'databackupEurope',
					//anchor : '95%',
					tooltip : "Data backup & Express delivery Europe",
					value : this.session.databackupEurope,
					xtype : 'numberfield',
					hideTrigger : true
				});
	}
	if (this.panel == null) {
		this.panel = Ext.createWidget('form', {
					bodyPadding : 5,
					frame : true,
					border : 0,
					layout:'fit',
					fieldDefaults : {
						labelAlign : 'left',
						labelWidth : labelWidth
						//anchor : '100%'
					},
					items : [{
								xtype : 'container',
								border : false,
								defaultType : 'textfield',
								items : sessionItems
							}]

				});
	}
	return this.panel;
};
