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

// Contains the files panel (all tabs)
function AutoProcRightPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";
	/** Events * */
	this.onAutoProcGraphSelected = new Event(this);

	this.data = null;
	this.tabs = null;

	// the different panels in the tab
	this.xdsPanel = null;
	this.xscalePanel = null;
	this.scalaPanel = null;
	this.scalePackPanel = null;
	this.truncatePanel = null;
	this.dimplePanel = null;

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
AutoProcRightPanel.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;
	var args = [];
	// XDS
	args.type = 1;
	_this.xdsPanel = new AutoProcFilePanel(args);
	_this.xdsPanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});
	// XSCALE
	args = [];
	args.type = 2;
	_this.xscalePanel = new AutoProcFilePanel(args);
	_this.xscalePanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});
	// SCALA / AIMLESS
	args = [];
	args.type = 3;
	_this.scalaPanel = new AutoProcFilePanel(args);
	_this.scalaPanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});
	// SCALEPACK
	args = [];
	args.type = 4;
	_this.scalePackPanel = new AutoProcFilePanel(args);
	_this.scalePackPanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});
	// TRUNCATE
	args = [];
	args.type = 5;
	_this.truncatePanel = new AutoProcFilePanel(args);
	_this.truncatePanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});
	// DIMPLE
	args = [];
	args.type = 6;
	_this.dimplePanel = new AutoProcFilePanel(args);
	_this.dimplePanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});

	// tabs
	_this.tabs = Ext.create('Ext.tab.Panel', {
				activeTab : 0,
				width : 600,
				bodyStyle : 'background-color:#dfe8f5;',
				defaults : {
					bodyPadding : 0,
					autoScroll : true
				},
				items : [{
							title : 'XDS',
							items : [_this.xdsPanel.getPanel(data)]
						}, {
							title : 'XSCALE',
							items : [_this.xscalePanel.getPanel(data)]
						}, {
							title : 'SCALA/AIMLESS',
							items : [_this.scalaPanel.getPanel(data)]
						}, {
							title : 'SCALEPACK/XTRIAGE',
							items : [_this.scalePackPanel.getPanel(data)]
						}, {
							title : 'TRUNCATE',
							items : [_this.truncatePanel.getPanel(data)]
						}, {
							title : 'DIMPLE',
							items : [_this.dimplePanel.getPanel(data)]
						}

				]
			});
	// main panel with tabs
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'fit'
				},
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				title : 'Files',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				tbar : [{
					text : 'Download',
					tooltip : 'Download all attachments for this autoprocessing',
					icon : '../images/download.png',
					href : 'viewResults.do?reqCode=downloadAttachment&autoProcId=' + _this.data.autoProcDetail.autoProcId

				}],
				items : [_this.tabs]

			});
		
	return _this.panel;
};


// autoProc has been selected => inform the different panels
AutoProcRightPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	_this.xdsPanel.setSelectedAutoProc(data);
	_this.xscalePanel.setSelectedAutoProc(data);
	_this.scalaPanel.setSelectedAutoProc(data);
	_this.scalePackPanel.setSelectedAutoProc(data);
	_this.truncatePanel.setSelectedAutoProc(data);
	_this.dimplePanel.setSelectedAutoProc(data);
	_this.tabs.setActiveTab(0);
	_this.panel.doLayout();
};

// graph to be displayed, inform the current tab (activ)
AutoProcRightPanel.prototype.displayGraphAttachment = function(dataAttachment) {
	var _this = this;
	var activTab = _this.tabs.getActiveTab();
	var idx = _this.tabs.items.indexOf(activTab);
	var activPanel = null;
	if (idx == 0) {
		activPanel = _this.xdsPanel;
	} else if (idx == 1) {
		activPanel = _this.xscalePanel;
	} else if (idx == 2) {
		activPanel = _this.scalaPanel;
	} else if (idx == 3) {
		activPanel = _this.scalePackPanel;
	} else if (idx == 4) {
		activPanel = _this.truncatePanel;	
	} else if (idx == 5) {
		activPanel = _this.dimplePanel;
	}
	if (activPanel != null) {
		activPanel.displayGraphAttachment(dataAttachment);
	}
	_this.panel.doLayout();
};
