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
// In  the Image tab, panel with the crystal snapshot
function XtalSnapshotPanel(args) {
	var _this = this;
	this.width = 1200;
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

//builds and returns the main panel
XtalSnapshotPanel.prototype.getPanel = function(snapshot) {
	var _this = this;

	var items = [];
	// location
	if (snapshot) {
		items.push({
					xtype : 'displayfield',
					name : 'location',
					labelWidth : 175,
					width : this.width,
					fieldLabel : 'Expected Snapshots location',
					value : snapshot.fileLocation
				});
	}

	// snapshot image
	if (snapshot && snapshot.filePresent) {
		var image = Ext.create('Ext.draw.Component', {
			width : 316,
			height : 197,
			listeners : {
				el : {
					click : function() {
						window.location.href = 'viewResults.do?reqCode=viewJpegImageFromFile&file=' + snapshot.fileLocation;
					}
				}
			},
			items : [{
				type : 'image',
				src : 'imageDownload.do?reqCode=getImageJpgFromFile&file=' + snapshot.fileLocation,
				width : 316,
				height : 197
			}]
		});
		items.push(image);
	}

	// main panel
	_this.panel = Ext.create('Ext.Panel', {
				// width: this.width,
				// height: this.height,
				layout : 'fit',
				width:"100%",
				border : 0,
				items : items
			});

	return _this.panel;
};