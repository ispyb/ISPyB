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
// tab panel in the workflow page
function MeshTabPanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 700;

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
MeshTabPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	if (data && data.listOfImages ) {
		var nb = data.listOfImages.length;
		for (var i = 0; i < nb; i++) {
			var meshScanPanel = new MeshScanPanel();
			var title = ""+i;
			if (data.listOfCollect && data.listOfCollect.length > i){
				collects = data.listOfCollect[i];
				if (collects.length > 0){
					title = collects[0].imagePrefix;
				}
			}
			var meshData = null;
			if (data.listOfMeshData && data.listOfMeshData.length > i)
				meshData = data.listOfMeshData[i];
			var snapshot = null;
			if (data.listOfSnapshot && data.listOfSnapshot.length > i)
				snapshot = data.listOfSnapshot[i];
				
			items.push({
						tabConfig : {
							title : title
						},
						items : [meshScanPanel.getPanel( meshData, snapshot, i)]
					});
		}
	}

	var tabs = Ext.create('Ext.tab.Panel', {
				layout : 'fit',
				activeTab : 0,
				defaults : {
					bodyPadding : 0
				},
				items : items
			});

	_this.panel = Ext.create('Ext.Panel', {
				layout : 'fit',
				items : [tabs]
			});

	return _this.panel;
};