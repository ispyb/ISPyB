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


// Search criteria to launch the data confidentiality for not protected sessions
function LaunchDataConfidentialityPanel(args) {
	var _this = this;
	this.width = 400;
	this.height = 500;

	this.onLaunchDataConfidentiality = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	
}

// returns the launch criteria selected by the user
LaunchDataConfidentialityPanel.prototype.getLaunchCriteria = function () {
	var _this = this;
	var dateTo = Ext.getCmp(this.panel.getItemId()).getValues().dateTo;
	var dateFrom = Ext.getCmp(this.panel.getItemId()).getValues().dateFrom;
	var sesId = Ext.getCmp(this.panel.getItemId()).getValues().sesId;
	var launch = [];
	launch.dateTo = dateTo;
	launch.dateFrom = dateFrom;
	launch.sesId = sesId;
	return launch;
};

// build the main panel
LaunchDataConfidentialityPanel.prototype.getPanel = function () {
	var _this = this;
	
	//dates by default
	var dateTo = new Date();
	var t = dateTo.getTime();
	var day = (1000 * 60 * 60 * 24);
	t = t - 2 * day;
	dateTo = dateTo.setTime(t);
	t = t - 3 * day;
	var dateFrom = new Date();
	dateFrom = dateFrom.setTime(t);
	var sesId;
	
	_this.panel = Ext.widget({
		xtype : 'form',
		//layout : 'form',
		collapsible : true,
		id : 'launchDataConfidentialityForm',
		frame : true,
		title : 'Launch Data Confidentiality',
		width : this.width,
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 100
		},
		style : {
			marginBottom : '10px'
		},
		defaultType : 'textfield',
		layout : {
			type : 'table',
			columns : 2
		},
		defaults : {
			labelStyle : 'padding-left:10px;'
		},
		items : [
		    {
				xtype : 'datefield',
				name : 'dateFrom',
				fieldLabel : '(DD/MM/YYYY)',
				format : 'd/m/Y',
				submitFormat : 'd/m/Y',
				value: dateFrom,
				colspan : 2
		    },
		    {
				xtype : 'datefield',
				name : 'dateTo',
				fieldLabel : '(DD/MM/YYYY)',
				format : 'd/m/Y',
				submitFormat : 'd/m/Y',
				value: dateTo,
				colspan : 2
		    },
		    {
				xtype : 'numberfield',
				name : 'sesId',
				fieldLabel : 'sessionId',
				value: sesId,
				colspan : 2
		    }
		],

		buttons : [{
			text : 'Launch',
			handler : function() {
				_this.onLaunchDataConfidentiality.notify();
			}
		}, {
			text : 'Reset',
			handler : function() {
				this.up('form').getForm().reset();
			}
		}]
	});
	
	return _this.panel;
};