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
/*global Ext */


// Image Panel for the workflow: display by mesh or not
function ImagePanel(args) {
	var _this = this;
	this.width = "100%";
	this.height = 800;

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
ImagePanel.prototype.getPanel = function (snapshot, imageList, displayMesh, meshData) {
	var _this = this;
	// crystal snaphsot image
	var xtalSnapshotPanel = new XtalSnapshotPanel();
	xtalSnapshotPanel = xtalSnapshotPanel.getPanel(snapshot);

	// wall image
	var wallImagePanel = new WallImagePanel();
	wallImagePanel = wallImagePanel.getPanel(imageList);

	//if it's a mesh: special mesh wall
	if (displayMesh == 1) {
		wallImagePanel = new MeshWallImagePanel();
		wallImagePanel = wallImagePanel.getPanel(imageList, meshData);
	}

	
	var items = [];
	items.push(xtalSnapshotPanel);
	items.push(wallImagePanel);
	_this.panel = Ext.create('Ext.Panel', {
			// width:this.width, //panel's width
			// height:this.height,//panel's height
			layout : 'vbox',
			items : items
		});

	return _this.panel;
};