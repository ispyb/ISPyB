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
// panel containning the value of Rsymm Isigma (autoProcParamPanel) and the report panel (if autoproc is selected)
function AutoProcActionPanel(args) {
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
	this.autoProcReportPanel = null;
}


// builds and returns the main panel
AutoProcActionPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	// panel containing the Rsymm and ISig value
	_this.autoProcParamPanel = new AutoProcParamPanel();
	
				
	items.push(_this.autoProcParamPanel.getPanel(data));
	
	
	// main panel
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'hbox'
				},
				collapsible : true,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				items : items

			});
	// if needed, add the report panel
	_this.addReport(data);
	return _this.panel;
};

// add the report panel if a autoProc is selected
AutoProcActionPanel.prototype.addReport = function(data) {
	var _this = this;
	if (_this.autoProcReportPanel == null && data && data.autoProcDetail) {
		_this.autoProcReportPanel = new AutoProcReportPanel();
		_this.panel.add(_this.autoProcReportPanel.getPanel(data));
		_this.panel.doLayout();
	}
};

// an autoProc is selected => set the panel report
AutoProcActionPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	
	_this.addReport(data);
	_this.autoProcReportPanel.setSelectedAutoProc(data);
	_this.panel.doLayout();

};