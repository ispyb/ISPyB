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
// panel containing the left (data about the selected autoProc) and the right panel (files list)
function AutoProcDataPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";
	// left panel: info about autoProc
	this.leftPanel = null;
	// right panel: files
	this.rightPanel = null;
	// main panel
	this.panel = null;
	/** Events * */
	this.onAutoProcGraphSelected = new Event(this);

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
AutoProcDataPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	// left Panel
	_this.leftPanel = new AutoProcLeftPanel();
	// right Panel
	_this.rightPanel = new AutoProcRightPanel();
	// listen for events
	_this.rightPanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});
	// add the panels in the list
	items.push(_this.leftPanel.getPanel(data));
	items.push(_this.rightPanel.getPanel(data));
	// main panel
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'hbox'
				},
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				items : items

			});
	return _this.panel;
};


// autoProc selected => gives info to the right panel and to the left panel
AutoProcDataPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	_this.leftPanel.setSelectedAutoProc(data);
	_this.rightPanel.setSelectedAutoProc(data);
};

// a graph should be displayed => gives info to the right panel
AutoProcDataPanel.prototype.displayGraphAttachment = function(dataAttachment) {
	var _this = this;
	_this.rightPanel.displayGraphAttachment(dataAttachment);
};
