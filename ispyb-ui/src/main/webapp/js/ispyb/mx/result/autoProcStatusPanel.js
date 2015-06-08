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
// panel containing the status Grid
function AutoProcStatusPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";

	this.autoProcStatusGrid = null;

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
AutoProcStatusPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	// no status 
	var html = _this.getHtml(data);
	// if status, creates the grids
	if (_this.hasStatusData(data)) {
		_this.autoProcStatusGrid = new AutoProcStatusGrid();
		var dataGrid = _this.getDataGrid(data);
		items.push(_this.autoProcStatusGrid.getGrid(dataGrid));
	}

	// main panel
	_this.panel = Ext.create('Ext.Panel', {
				layout : {
					type : 'fit',
					border : 0,
					bodyPadding : 0
				},
				title : 'Status History',
				collapsible : true,
				frame : true,
				items : items,
				html : html
			});

	return _this.panel;
};


// returns true if there are some status information
AutoProcStatusPanel.prototype.hasStatusData = function(data) {
	return (data && data.autoProcDetail && data.autoProcDetail.autoProcEvents && data.autoProcDetail.autoProcEvents.length > 0);
};


// return text for No status
AutoProcStatusPanel.prototype.getHtml = function(data) {
	var _this = this;
	var html = "";
	if (!_this.hasStatusData(data)) {
		html = '<html><h4>No Status has been found</h4></html>';
	}
	return html;
};


// prepare date for the grid
AutoProcStatusPanel.prototype.getDataGrid = function(data) {
	var d = new Array();
	if (data && data.autoProcDetail && data.autoProcDetail.autoProcEvents) {
		for (var i = 0; i < data.autoProcDetail.autoProcEvents.length; i++) {
			d.push(data.autoProcDetail.autoProcEvents[i]);
		}
	}
	return d;
};

// a autoProc has been selected => update the panel and the grid
AutoProcStatusPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	var html = _this.getHtml(data);
	_this.panel.update(html);
	if (_this.hasStatusData(data)) {
		if (_this.autoProcStatusGrid == null) { // grid doesn't exist => create
			var items = new Array();
			_this.autoProcStatusGrid = new AutoProcStatusGrid();
			var dataGrid = _this.getDataGrid(data);
			items.push(_this.autoProcStatusGrid.getGrid(dataGrid));
			_this.panel.add(items);
		} else { // grid exsit already => just update
			var dataGrid = _this.getDataGrid(data);
			_this.autoProcStatusGrid.setSelectedAutoProc(dataGrid);
		}
	} else { // no data to display
		// clear component:
		var f;
		while (f = _this.panel.items.first()) {
			_this.panel.remove(f, true);
		}
		_this.autoProcStatusGrid = null;
	}

	_this.panel.doLayout();
};