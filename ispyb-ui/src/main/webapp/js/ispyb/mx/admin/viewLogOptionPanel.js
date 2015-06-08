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


// Search criteria to display the data confidentiality log
function ViewLogOptionPanel(args) {
	var _this = this;
	this.width = 550;
	this.height = 500;

	this.onViewLog = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	this.all = false;
	this.perMonth = false;
	this.perDate = false;
}

// returns the search criteria selected by the user
ViewLogOptionPanel.prototype.getSearchCriteria = function () {
	var _this = this;
	var month = Ext.getCmp(this.panel.getItemId()).getValues().month;
	var year = Ext.getCmp(this.panel.getItemId()).getValues().year;
	var logDate = Ext.getCmp(this.panel.getItemId()).getValues().logDate;
	var search = [];
	search.all = _this.all;
	search.perMonth = _this.perMonth;
	search.month = month;
	search.year = year;
	search.perDate = _this.perDate;
	search.logDate = logDate;
	return search;
};


// disable or enable the options, depending of the choice (after a click on the radiobutton)
ViewLogOptionPanel.prototype.changeLogPeriod = function (selectedInput) {
	var _this = this;
	var allItems = Ext.getCmp(this.panel.getItemId()).items.items;
	
	for (var i = 0; i < allItems.length; i++) {
		var item = allItems[i];
		if (item.name == 'logDate') {
			item.setDisabled(!(selectedInput == 'perDate'));
		} else if (item.name == 'month') {
			item.setDisabled(!(selectedInput == 'perMonth'));
		} else if (item.name == 'year') {
			item.setDisabled(!(selectedInput == 'perMonth'));
		}
	}
	_this.perDate = (selectedInput == 'perDate');
	_this.perMonth  = (selectedInput == 'perMonth');
	_this.all = (selectedInput == 'all');
};


// build the main panel
ViewLogOptionPanel.prototype.getPanel = function () {
	var _this = this;
	
	var today = new Date();
	var mm = today.getMonth() + 1;
	var yyyy = today.getFullYear();
	
	_this.panel = Ext.widget({
		xtype : 'form',
		//layout : 'form',
		collapsible : true,
		id : 'viewLogForm',
		frame : true,
		title : 'View Data Protection Log',
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
			columns : 3
		},
		defaults : {
			labelStyle : 'padding-left:10px;'
		},
		items : [ {
			xtype: 'radio',
            boxLabel: 'All',
            name: 'log-period',
            inputValue: 'all', 
            colspan : 3, 
			listeners: {
                change: {
                    fn: function (field, newValue, oldValue, options) {
                        if (newValue == true) {
                            _this.changeLogPeriod(field.inputValue);
                        }
                    }
                }
            }
        }, {
			xtype: 'radio',
            boxLabel: 'Per month',
            checked: true,
            name: 'log-period',
            inputValue: 'perMonth', 
			listeners: {
                change: {
                    fn: function (field, newValue, oldValue, options) {
						if (newValue == true) {
							_this.changeLogPeriod(field.inputValue);
						}
                    }
                }
            }
        },
		{
            xtype: 'numberfield',
            name: 'month',
            fieldLabel: 'Month',
			labelWidth : 50,
            value: mm,
			width: 150,
            minValue: 1,
            maxValue: 12
        },
		{
            xtype: 'numberfield',
            name: 'year',
            fieldLabel: 'Year',
			labelWidth : 50,
			width: 200,
            value: yyyy,
            minValue: 2013
        },
		{
            xtype: 'radio',
			boxLabel: 'Per date',
            name: 'log-period',
            inputValue: 'perDate',
			listeners: {
                change: {
                    fn: function (field, newValue, oldValue, options) {
						if (newValue == true) {
							_this.changeLogPeriod(field.inputValue);
						}
                    }
                }
            }
        }, {
			xtype : 'datefield',
			name : 'logDate',
			fieldLabel : '(DD/MM/YYYY)',
			format : 'd/m/Y',
			submitFormat : 'd/m/Y',
			value: today,
			colspan : 2
		}],

		buttons : [{
			text : 'View Log',
			handler : function () {
				_this.onViewLog.notify();
			}
		}, {
			text : 'Reset',
			handler : function () {
				this.up('form').getForm().reset();
			}
		}]
	});
	_this.changeLogPeriod('perMonth');
	return _this.panel;
};