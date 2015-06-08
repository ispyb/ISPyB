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
 * Brenchereau, A. De Maria Antolinos
 ******************************************************************************/

// Search criteria session panel
function SearchSessionPanel(args) {
	var _this = this;
	this.width = "340";
	this.height = "500";

	// click on search
	this.onSearchSession = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}


// builds and returns the panel
SearchSessionPanel.prototype.getPanel = function(beamlines) {
	var _this = this;
	var d = [];
	for (i = 0; i < beamlines.length; i++) {
		var s = {
			beamlineName : beamlines[i]
		};
		d.push(s);
	}

	// store beamline 
	_this.beamlines = Ext.create('Ext.data.Store', {
				fields : ['beamlineName'],
				data : d
			});

	// form panel
	_this.panel = Ext.widget({
		xtype : 'form',
		layout : 'form',
		collapsible : true,
		id : 'searchSessionForm',
		frame : true,
		title : 'Search Session',
		bodyPadding : '5 5 0',
		width : 350,
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 90
		},
		defaultType : 'textfield',
		items : [{
					xtype : 'combobox',
					fieldLabel : 'Beamline',
					id : 'cbBeamline',
					name : 'beamline',
					store : _this.beamlines,
					valueField : 'beamlineName',
					displayField : 'beamlineName',
					typeAhead : true,
					queryMode : 'local',
					emptyText : 'Select a beamline...'
				}, {
					xtype : 'datefield',
					name : 'startDate',
					id : 'fieldStartDate',
					fieldLabel : 'Start Date (DD/MM/YYYY)'
				}, {
					xtype : 'datefield',
					name : 'endDate',
					id : 'fieldEndDate',
					fieldLabel : 'End Date (DD/MM/YYYY)'
				}],

		buttons : [{
					text : 'Search',
					handler : function() {
						if (this.up('form').getForm().isValid()) {
							this.up('form').getForm().submit({
										url : 'viewSession.do?reqCode=getSessions',
										waitMsg : 'Searching sessions...',
										success : function(fp, o) {
											_this.onSearchSession.notify({
														'sessions' : fp
													});
										}
									});
							this.up('form').getForm().reset();
						}
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