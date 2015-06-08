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
// image wall page for a mesh, with the size mesh
function MeshWallImagePanel(args) {
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

// format data
MeshWallImagePanel.prototype.prepareData = function(imageList) {
	this.features = [];
	var listImages = imageList;
	for (var i = 0; i < listImages.length; i++) {
		var o = {};
		var image = o.name = listImages[i].fileName;
		o.url = 'imageDownload.do?reqCode=getImageJpgThumb&imageId='+ listImages[i].imageId;
		o.size = 125; 
		o.target = 'viewResults.do?reqCode=viewJpegImage&imageId=' + 
				listImages[i].imageId + '&previousImageId=' + 
				listImages[i].previousImageId + '&nextImageId=' + 
				listImages[i].nextImageId;
		this.features.push(o);
	}
};


// calculates the nb of columns
MeshWallImagePanel.prototype.getNbCol = function(meshData) {
	if (meshData && meshData.length > 0) {
		
			
		var x = meshData[0].motorPosition.gridIndexY;
		var min = x;
		var max = x;
		for (var i = 1; i < meshData.length; i++) {
			x = meshData[i].motorPosition.gridIndexY;
			if (x < min) {
				min = x;
			} else if (x > max) {
				max = x;
			}
		}
		// return array
		var array = [];
		var v = 0;
		if (min) {
			v = min;
		}
		while (v < max) {
			array.push(v);
			v = Number(v) + 1;
		}
		if (max)
			array.push(max);
		else
			array.push(0);
		return array.length;
	}
};

// returns the position of a data
MeshWallImagePanel.prototype.getGridPosition = function(meshData, imgId) {
	for (var i = 0; i < meshData.length; i++) {
		if (Number((meshData[i].imageId)) === Number(imgId)) {
			var pos = new Array(2);
			pos[0] = meshData[i].motorPosition.gridIndexZ;
			pos[1] = meshData[i].motorPosition.gridIndexY;
			return pos;
		}
	}
	return null;
};


// builds and returns the main panel
MeshWallImagePanel.prototype.getPanel = function(imageList, meshData) {
	var _this = this;
	this.prepareData(imageList);
	this.store = Ext.create('Ext.data.Store', {
				model : 'ImageModel'
			});
	this.store.loadData(this.features, false);

	var items = [];
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
	var nbCol = this.getNbCol(meshData);
	var height = 0;

	
	var listi = [];
	if (meshData && imageList) {
		var listImages = imageList;
		height = 140 * ((listImages.length / nbCol));
		if (height < 140 ){
			height= 140;
		}
		for (var i = 0; i < listImages.length; i++) {
			var imageId = listImages[i].imageId;
			var pos = this.getGridPosition(meshData, imageId);
			if (pos) {
				var image = Ext.create('Ext.Panel', {
					width : 125,
					height : 125,
					border : 0,
					margin : "5 5 5 5",
					html : '<a href="viewResults.do?reqCode=viewJpegImage&imageId=' + 
							listImages[i].imageId + 
							'&previousImageId=' + 
							listImages[i].previousImageId + 
							'&nextImageId=' + 
							listImages[i].nextImageId + 
							'" ><img src="imageDownload.do?reqCode=getImageJpgThumb&imageId=' + 
							listImages[i].imageId + 
							'" width="125" height="125" border="0" /></a>'
				});
				listi.push(image);
			}
		}
	}
	var view = Ext.create('Ext.Panel', {
				layout : {
					type : 'table',
					columns : nbCol
				},
				height : height,
				items : listi
			});
	items.push(view);

	_this.panel = Ext.create('Ext.Panel', {
				width: "100%",
				//id : 'images-view',
				autoScroll : true,
				layout : 'vbox',
				items : items
			});

	return _this.panel;
};