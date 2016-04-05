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
// panel with the autoProc Grid 
function AutoProcListPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";
	this.autoProcGrid = null;
	/** Events * */
	this.onAutoProcSelected = new Event(this);

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
AutoProcListPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	var html = "";
	// there are some autoProcs => create the grid
	if (data && data.autoProcList && data.autoProcList.length > 0) {
		_this.autoProcGrid = new AutoProcGrid();
		_this.autoProcGrid.onRowClicked.attach(function(sender, args) {
					var autoProcId = args.autoProcId;
					_this.onAutoProcSelected.notify({
								'autoProcId' : autoProcId
							});
				});

		items.push(_this.autoProcGrid.getGrid(data));

		if (data && data.nbRemoved) {
			html = "<html><h4><font color=orange> " + data.nbRemoved + " other autoprocessing results exist without the selected RSymm - I/sigma thresholds. </font></h4></html>";
			items.push({
				xtype : 'panel',
				layout: 'fit',
				title : 'Other autoprocessings',
				html  : html
			});
		}
	} else {
		// No autoProcessings
		html = "<html><h4>No Autoprocessings have been found with selected RSymm - I/sigma  </h4></html>";
		if (data && data.nbRemoved) {
			html = "<html><h4><font color=orange>No Autoprocessings have been found with selected RSymm - I/sigma thresholds but " + data.nbRemoved + " autoprocessings exist </font></h4></html>" ;
		}
	}
	
	_this.panel = Ext.create('Ext.Panel', {
				layout : {
					//type : 'fit',
					border : 0,
					bodyPadding : 0
				},
				collapsible : false,
				frame : true,
				items : items,
				html : html
			});

	return _this.panel;
};


// set a autoProc selected, gives info to the grid
AutoProcListPanel.prototype.setSelectedAutoProcId = function(autoProcIdSelected) {
	var _this = this;
	if (_this.autoProcGrid)
		_this.autoProcGrid.setSelectedAutoProcId(autoProcIdSelected);
};