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
// EDNA html results tab
function CharacterisationResultPanel(args) {
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
CharacterisationResultPanel.prototype.getPanel = function(data) {
	var _this = this;
	var dataCollection = data.dataCollection;
	// html page is in a IFrame component
	var iframe=  Ext.create('Ext.ux.SimpleIFrame',{
		src:"viewResults.do?reqCode=displayEDNAPagesContent&dataCollectionId="+data.dataCollectionId
	}) ;
	iframe.setSize(800,800);
	
	_this.panel = Ext.create('Ext.Panel', {
				layout : 'fit',
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				items : [iframe]

			});

	return _this.panel;
};