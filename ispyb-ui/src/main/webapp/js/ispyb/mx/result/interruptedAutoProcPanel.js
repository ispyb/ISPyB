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
// panel with the grid 
function InterruptedAutoProcPanel(args) {
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
InterruptedAutoProcPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	// no interrupted autoProc
	var html = _this.getHtml(data);
	// if yes, create the grid
	if (_this.hasStatusData(data)) {
		for (var i=0; i<data.interruptedAutoProcEvents.length; i++){
			var autoProcStatusGrid = new AutoProcStatusGrid();
			var dataGrid = _this.getDataGrid(data.interruptedAutoProcEvents[i]);
			items.push(autoProcStatusGrid.getGrid(dataGrid));
		}
		
	}

	_this.panel = Ext.create('Ext.Panel', {
				layout : {
					type : 'vbox',
					border : 0,
					bodyPadding : 0
				},
				title : 'Interrupted Autoprocessing',
				collapsible : true,
				frame : true,
				items : items,
				html : html
			});

	return _this.panel;
};


InterruptedAutoProcPanel.prototype.getDataGrid = function(data) {
	var d = [];
	if (data) {
		for (var i = 0; i < data.length; i++) {
			d.push(data[i]);
		}
	}
	return d;
};

InterruptedAutoProcPanel.prototype.hasStatusData = function(data) {
	return (data && data.interruptedAutoProcEvents &&  data.interruptedAutoProcEvents.length > 0);
};

InterruptedAutoProcPanel.prototype.getHtml = function(data) {
	var _this = this;
	var html = "";
	if (!_this.hasStatusData(data)) {
		html = '<html><h4>No Status has been found</h4></html>';
	}
	return html;
};
