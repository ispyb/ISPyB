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
// panel which contains the mapPanel and the positionGrid
function MeshScanPanel(args) {
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
MeshScanPanel.prototype.getPanel = function(meshData, snapshot, id) {
	var _this = this;
	var mapPanel = new MapPanel();
	mapPanel = mapPanel.getPanel(meshData, snapshot, id);

	var positionGrid = new PositionGrid();
	positionGrid = positionGrid.getGrid(meshData);
	
	var items = [];
	items.push(mapPanel);
	items.push(positionGrid);
	

	_this.panel = Ext.create('Ext.Panel', {
				//id : 'meshScanPanel',
				//width : "100%", // panel's width
				//height : this.height,// panel's height
				layout : {
					type : 'vbox'
				},
				items : items
			});

	return _this.panel;
};