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
// Denzo tab
function DenzoPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";

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
DenzoPanel.prototype.getPanel = function(data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	var url = 'viewResults.do?reqCode=displayDenzoFiles&dataCollectionId=' + data.dataCollectionId;
	_this.panel = Ext.widget({
				xtype : 'form',
				collapsible : false,
				frame : true,
				layout : 'vbox',
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 200
				},
				defaultType : 'textfield',
				items : [
					{
						xtype : 'button',
						text : 'Display Denzo Files',
						href : url,
						target : '_blank',
						hrefTarget : '_blank',
						disabled: !data.DenzonContentPresent
					}, 
					{
						xtype : 'displayfield',
						name : 'denzoFileLocation',
						fieldLabel : 'Denzo files location',
						labelStyle : labelStyle,
						value : data.fullDenzoPath 
					}
				]

			});

	return _this.panel;
};