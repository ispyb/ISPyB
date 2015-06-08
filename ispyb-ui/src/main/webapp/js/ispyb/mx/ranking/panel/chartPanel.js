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
 * Brenchereau, M. Bodin, M. Bodin
 ******************************************************************************/
// panel that contains the different charts
function ChartPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "500";

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	this.data = null;
}

// builds the main panel
ChartPanel.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtn2') {

		}
	};

	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtn2',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});

	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	// main panel creation
	this.panel = Ext.create('Ext.Panel', {
				id : 'panelParallelChart',
				width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : {
					type : 'vbox', // Arrange child items vertically
					align : 'stretch', // Each takes up full width
					bodyPadding : 0,
					border : 0
				},
				listeners : {
					'afterrender' : function(thisCmp) {
						_this.postRender();
					}
				},
				html : "<div id='mainContent'></div>"
			});
	return this.panel;
};


// render of the panel
ChartPanel.prototype.postRender = function() {
	var _this = this;
	var items = [];
	if (_this.data.length == 0) {
		items.push({
					border : 0,
					padding : 0,
					html : BUI.getWarningHTML("No data have been found.")
				});
	}
	items.push(new ParallelChart('mainContent').render(_this.data));
};
