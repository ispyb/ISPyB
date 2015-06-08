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
// panel with snaphsot (linked to dataCollection Group) and grid position
function DetailsPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "200";

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
DetailsPanel.prototype.getPanel = function() {
	var _this = this;

	// position Grid
	var positionGrid = new PositionGrid();
	positionGrid = positionGrid.getGrid();

	// crystal snapshot panel
	var xtalSnapshotPanel = new XtalSnapshotPanel();
	xtalSnapshotPanel = xtalSnapshotPanel.getPanel();

	// main panel
	_this.panel = Ext.create('Ext.Panel', {
				width : 800, // panel's width
				height : 200,// panel's height
				layout : 'border',
				items : [{
							region : 'center',
							split : true,
							collapsible : false,
							floatable : false,
							items : [positionGrid]
						}, {
							region : 'east',
							width : 200,
							height : 200,
							split : true,
							collapsible : false,
							floatable : false,
							items : [xtalSnapshotPanel]
						}]
			});

	return _this.panel;
};