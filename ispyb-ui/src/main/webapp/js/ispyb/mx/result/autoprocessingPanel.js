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
// main autoProcessing panel, composed with the list of autoProcs, the actionPanel (rsymm, ISig and the reports)
// the data (right and left panel) and the status of autoProc and the interrupted autoProc


function AutoprocessingPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";
	/** Events * */
	this.onAutoProcSelected = new Event(this);
	this.onAutoProcGraphSelected = new Event(this);

	// panels inside:
	// list of autoProc
	this.autoProcListPanel = null;
	// ISig, rsymm panel and reports
	this.autoProcActionPanel = null;
	// infor about autoProc + files
	this.autoProcDataPanel = null;
	// reprocessing panel
	this.reprocessingPanel = null;
	
	// status
	this.autoProcStatusPanel = null;
	// interrupted autoProc
	this.interruptedAutoProcPanel = null;
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
AutoprocessingPanel.prototype.getPanel = function(data) {
	var _this = this;
	// list of autoProcessing
	_this.autoProcListPanel = new AutoProcListPanel();
	// lsiten for events
	_this.autoProcListPanel.onAutoProcSelected.attach(function(sender, args) {
				var autoProcId = args.autoProcId;
				_this.onAutoProcSelected.notify({
							'autoProcId' : autoProcId
						});
			});
  
	var items = [];
	// add to the list
	items.push(_this.autoProcListPanel.getPanel(data));
	
	
	 // reprocessing panel
	if (data && data.hasReprocessing) {
		_this.reprocessingPanel = new ReprocessingPanel();
		items.push(_this.reprocessingPanel.getPanel(data));
	}

	// if we have some autoProc, add the Rymm and ISig panel, (and reports if autoProc is selected)
	if (data && data.autoProcList) {
		_this.autoProcActionPanel = new AutoProcActionPanel();
		
		items.push(_this.autoProcActionPanel.getPanel(data));
	}

	// if interrupted autoProc, add them 
	if (data && data.interruptedAutoProcEvents && data.interruptedAutoProcEvents.length > 0){
		_this.interruptedAutoProcPanel = new InterruptedAutoProcPanel();
		items.push(_this.interruptedAutoProcPanel.getPanel(data));
	}

	// main panel
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'vbox'
					// align: 'stretch' // Child items are stretched to full
					// width
				},
				collapsible : false,
				frame : true,
				items : items

			});

	return _this.panel;
};


// a autoProc is selected => gives info to the panel 
AutoprocessingPanel.prototype.setSelectedAutoProcId = function(
		autoProcIdSelected) {
	var _this = this;
	_this.autoProcListPanel.setSelectedAutoProcId(autoProcIdSelected);
};

// a graph should be displayed => gives info to the data panel (then to right panel)
AutoprocessingPanel.prototype.displayGraphAttachment = function(dataAttachment) {
	var _this = this;
	_this.autoProcDataPanel.displayGraphAttachment(dataAttachment);
};

// return true if there is some events linked to the autoProc
AutoprocessingPanel.prototype.hasStatusData = function(data) {
	return (data && data.autoProcDetail && data.autoProcDetail.autoProcEvents && data.autoProcDetail.autoProcEvents.length > 0);
};

// display a given autoProcessing
AutoprocessingPanel.prototype.displayAutoProc = function(data) {
	var _this = this;
	Ext.suspendLayouts();
	// select it in the list if needed
	if (_this.autoProcActionPanel != null) {
		_this.autoProcActionPanel.setSelectedAutoProc(data);
	}
	// creates if needed the data panel, and gives info about the selection
	if (_this.autoProcDataPanel == null) {
		_this.autoProcDataPanel = new AutoProcDataPanel();
		_this.autoProcDataPanel.onAutoProcGraphSelected.attach(function(sender,
				args) {
			var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
			_this.onAutoProcGraphSelected.notify({
						'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
					});
		});
		_this.panel.add(_this.autoProcDataPanel.getPanel(data));
	} else {  
		_this.autoProcDataPanel.setSelectedAutoProc(data);
	}
	// there are some events linked to the autoProc => set the status panel if needed
	if (_this.hasStatusData(data)){
		if (_this.autoProcStatusPanel == null) {
			_this.autoProcStatusPanel = new AutoProcStatusPanel();
			_this.panel.add(_this.autoProcStatusPanel.getPanel(data));
		} else {
			_this.autoProcStatusPanel.setSelectedAutoProc(data);
		}
	}else{
		if (_this.autoProcStatusPanel == null) {
		} else {
			_this.panel.remove(_this.panel.items.last(), true);
			_this.autoProcStatusPanel = null;
		}
	}
	// redesign
	Ext.resumeLayouts();
	_this.panel.doLayout();
};