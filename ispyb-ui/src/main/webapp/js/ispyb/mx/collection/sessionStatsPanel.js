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
/*global Event */
/*global Ext */

// session statistics panel: nb Collect, nbCharac., etc.
function SessionStatsPanel(args) {
	var _this = this;
	this.width = 300;
	this.height = 250;
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
SessionStatsPanel.prototype.getPanel = function (data) {
	var _this = this;
	_this.panel = Ext.widget({
			xtype : 'form',
			layout : {
				type : 'vbox'
			},
			style : {
				marginLeft : '10px', 
				marginTop : '10px'
			},
			collapsible : true,
			frame : true,
			title: 'Session Statistics',
			bodyPadding : '5 5 0',
			fieldDefaults : {
				msgTarget : 'side',
				labelWidth : 180
			},
			defaultType : 'displayfield',
			items : [{
					fieldLabel : 'Nb Collect',
					value : data.nbCollect,
					qtip: "> 4 Images",
					listeners: {
						render: function (c) {
							Ext.QuickTips.register({
								target: c.getEl(),
								text: c.qtip
							});
						}
					}
				}, {
					fieldLabel : 'Nb Test',
					value : data.nbTest,
					qtip: "<= 4 Images",
					listeners: {
						render: function (c) {
							Ext.QuickTips.register({
								target: c.getEl(),
								text: c.qtip
							});
						}
					}
				}, {
					fieldLabel : 'Nb Energy Scan',
					value : data.nbEnergyScans
				}, {
					fieldLabel : 'Nb XRFSpectra',
					value : data.nbXRFSpectra
				}]
			

		});


	return _this.panel;
};