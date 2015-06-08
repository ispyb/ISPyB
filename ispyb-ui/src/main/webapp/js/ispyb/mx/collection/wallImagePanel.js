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
// Image panel, presented as a wall
function WallImagePanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 800;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	// image datamodel
	ImageModel = Ext.define('ImageModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'name'
						}, {
							name : 'url'
						}, {
							name : 'target'
						}, {
							name : 'size',
							type : 'float'
						}]
			});

}

// prepare data, format 
WallImagePanel.prototype.prepareData = function(imageList) {
	this.features = [];
	var listImages = imageList;
	for (var i = 0; i < listImages.length; i++) {
		var o = {};
		var image = o.name = listImages[i].fileName;
		o.url = 'imageDownload.do?reqCode=getImageJpgThumb&imageId=' + listImages[i].imageId;
		o.size = 125;
		o.target = 'viewResults.do?reqCode=viewJpegImage&imageId=' + 
				listImages[i].imageId + '&previousImageId=' + 
				listImages[i].previousImageId + '&nextImageId=' + 
				listImages[i].nextImageId;
		this.features.push(o);
	}
};


// builds and returns the main panel
WallImagePanel.prototype.getPanel = function(imageList) {
	var _this = this;
	//format data
	this.prepareData(imageList);
	this.store = Ext.create('Ext.data.Store', {
				model : 'ImageModel'
			});
	this.store.loadData(this.features, false);

	var items = [];

	// location
	if (imageList && imageList.length > 0) {
		items.push({
					xtype : 'displayfield',
					name : 'location',
					labelWidth : 175,
					width : "100%",
					fieldLabel : 'Location',
					value : imageList[0].fileLocation
				});
	}
	// nbRows to display
	var nbRow = (this.features.length * 125 / this.width) + 1;
	
	// creates the view
	var view = Ext.create('Ext.view.View', {
		store : this.store,
		tpl : [
				'<tpl for=".">',
				'<div class="thumb-wrap"  id="{name:stripTags}">',
				'<div  class="thumb"><a href="{target}" ><img src="{url}" title="{name:htmlEncode}" border="0" width="{size}" height="{size}"></a></div>',
				'</div>', '</tpl>', '<div class="x-clear"></div>'],
		multiSelect : false,
		// width: "100%",
		height : 200 * nbRow,
		trackOver : true,
		// autoScroll:true,
		emptyText : 'No images to display',
		prepareData : function(data) {
			Ext.apply(data, {
						shortName : Ext.util.Format.ellipsis(data.name, 45),
						sizeString : Ext.util.Format.fileSize(data.size)
					});
			return data;
		}
	});
	items.push(view);

	// main panel
	_this.panel = Ext.create('Ext.Panel', {
				width: "100%",
				// height: this.height,
				//id : 'images-view',
				autoScroll : true,
				layout : 'vbox',
				border : 0,
				items : items
				
			});

	return _this.panel;
};