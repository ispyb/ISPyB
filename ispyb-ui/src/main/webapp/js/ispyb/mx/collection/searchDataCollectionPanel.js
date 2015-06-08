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
// search dataCollection criteria  panel for the last Collection in the manager view
function SearchDataCollectionPanel(args) {
	var _this = this;
	this.width = 550;
	this.height = 500;

	this.onSearchDataCollection = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}

// returns the search criteria filled by the user
SearchDataCollectionPanel.prototype.getSearchCriteria = function() {
	var bl = Ext.getCmp(this.panel.getItemId()).getValues().beamline;
	var d1 = Ext.getCmp(this.panel.getItemId()).getValues().searchStartDate;
	var d2 = Ext.getCmp(this.panel.getItemId()).getValues().searchEndDate;
	var t1 = Ext.getCmp(this.panel.getItemId()).getValues().searchStartTime;
	var t2 = Ext.getCmp(this.panel.getItemId()).getValues().searchEndTime;
	var search = [];
	search.beamline = bl;
	search.startDate = d1;
	search.endDate = d2;
	search.startTime = t1;
	search.endTime = t2;
	return search;
};

// select all beamlines
SearchDataCollectionPanel.prototype.selectAllBeamlines = function(value) {
	Ext.each(Ext.getCmp('beamlineGroup').items.items, function(c) {
				if (c.setValue) {
					c.setValue(value);
				}
				return true;
			}, Ext.getCmp('beamlineGroup'));

};

// set the search criteria 
SearchDataCollectionPanel.prototype.setCriteria = function(searchBeamline,
		startDate, endDate, startTime, endTime) {
	if (Ext.getCmp('beamlineGroup')) {
		Ext.each(Ext.getCmp('beamlineGroup').items.items, function(c) {
					if (searchBeamline && searchBeamline.length > 0) {
						var id = searchBeamline.indexOf(c.boxLabel);
						c.setValue(id != -1);
					}
					return true;
				}, Ext.getCmp('beamlineGroup'));
	}
	var allItems = Ext.getCmp(this.panel.getItemId()).items.items;
	for (var i = 0; i < allItems.length; i++) {
		var item = allItems[i];
		if (item.name == 'searchStartDate' && startDate && startDate != 'undefined') {
			item.setValue(startDate);
		} else if (item.name == 'searchEndDate' && endDate && endDate != 'undefined') {
			item.setValue(endDate);
		} else if (item.name == 'searchStartTime' && startTime && startTime != 'undefined') {
			item.setValue(startTime);
		} else if (item.name == 'searchEndTime' && endTime && endTime != 'undefined') {
			item.setValue(endTime);
		}
	}
};


//builds and returns the main panel
SearchDataCollectionPanel.prototype.getPanel = function(beamlines) {
	var _this = this;
	var d = [];
	for (i = 0; i < beamlines.length; i++) {
		d.push({
					xtype : 'checkbox',
					boxLabel : beamlines[i],
					name : 'beamline',
					checked : true,
					inputValue : beamlines[i]
				});
	}

	_this.panel = Ext.widget({
		xtype : 'form',
		//layout : 'form',
		collapsible : true,
		id : 'searchSessionForm',
		frame : true,
		title : 'Search DataCollection',
		width : this.width,
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 100
		},
		defaultType : 'textfield',
		layout : {
			type : 'table',
			columns : 2
		},
		defaults : {
			labelStyle : 'padding-left:10px;'
		},
		items : [{
					xtype : 'checkboxgroup',
					fieldLabel : 'Beamline',
					id : 'beamlineGroup',
					columns : [90, 90, 90, 90, 90],
					colspan : 2,
					allowBlank : false,
					itemId : 'beamlines',
					items : d
				}, {
					xtype : 'button',
					name : 'selectAllBl',
					text : 'Select all Beamlines',
					handler : function() {
						_this.selectAllBeamlines(true);
					}
				}, {
					xtype : 'button',
					name : 'deselectAllBl',
					text : 'Deselect all Beamlines',
					handler : function() {
						_this.selectAllBeamlines(false);
					}
				}, {
					xtype : 'datefield',
					name : 'searchStartDate',
					fieldLabel : 'Start Date (DD/MM/YYYY)',
					format : 'd/m/Y',
					submitFormat : 'd/m/Y'
				}, {
					xtype : 'timefield',
					name : 'searchStartTime',
					fieldLabel : 'Start Time (hh:mm)',
					format : 'H:i',
					submitFormat : 'H:i'
				}, {
					xtype : 'datefield',
					name : 'searchEndDate',
					fieldLabel : 'End Date (DD/MM/YYYY)',
					format : 'd/m/Y',
					submitFormat : 'd/m/Y'
				}, {
					xtype : 'timefield',
					name : 'searchEndTime',
					fieldLabel : 'End Time (hh:mm)',
					format : 'H:i',
					submitFormat : 'H:i'
				}],

		buttons : [{
			text : 'Search',
			handler : function() {
				var d1 = Ext.getCmp(_this.panel.getItemId()).getValues().searchStartDate;
				var d2 = Ext.getCmp(_this.panel.getItemId()).getValues().searchEndDate;
				var longPeriod = ((d1 == "" && d2 != "") || (d1 != "" && d2 == ""));
				if (d1 != "" && d2 != "") {
					var dateParts = d1.split("/");
					var date1 = new Date(dateParts[2], (dateParts[1] - 1),
							dateParts[0]);
					dateParts = d2.split("/");
					var date2 = new Date(dateParts[2], (dateParts[1] - 1),
							dateParts[0]);
					var WNbJours = date2.getTime() - date1.getTime();
					var nbDays = Math.ceil(WNbJours / (1000 * 60 * 60 * 24));
					longPeriod = (nbDays > 1);
				}
				var r = true;
				if (longPeriod == true) {
					r = confirm("This search may take a long time (period > 1 day). Are you sure to continue?");
				}
				if (r == true) {
					_this.onSearchDataCollection.notify();
				}
				// this.up('form').getForm().reset();
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