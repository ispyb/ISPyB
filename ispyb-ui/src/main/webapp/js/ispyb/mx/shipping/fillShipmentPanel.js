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
 * Brenchereau, M. Bodin,  A. De Maria Antolinos
 ******************************************************************************/
/*global Event */
/*global document */
/*global Ext */
/*global DewarPanel */

// main panel, which contains a tab of dewarPanel
function FillShipmentPanel(args) {
	var _this = this;
	this.width = 550;
	this.height = 500;
	
	// Events
	this.onAddDewarButtonClicked = new Event(this);
	this.onEditDewarButtonClicked = new Event(this);
	this.onRemoveDewarButtonClicked = new Event(this);
	this.onAddPuckButtonClicked = new Event(this);
	this.onEditPuckButtonClicked = new Event(this);
	this.onRemovePuckButtonClicked = new Event(this);
	this.onSavePuckButtonClicked = new Event(this);
	this.onCopyPuckButtonClicked = new Event(this);
	this.onPastePuckButtonClicked = new Event(this);

	// the tab
	this.dewarTab = null;
	// shippingId (from database)
	this.shippingId = null;
	// list of the dewarPanel
	this.listDewarPanel = null;
	// array containing the dewar tab
	this.dewarItems = [];
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
FillShipmentPanel.prototype.getPanel = function (data) {
	var _this = this;
	_this.data = data;
	_this.shippingId = data.shippingId;
	// builds the dewars panel
	var items = _this.getItems(data);
	// builds the tab
	_this.dewarTab = _this.getDewarTab(items);
	// main panel
	_this.panel = Ext.create('Ext.panel.Panel', {
			plain : true,
			frame : false,
			border : 0,
			layout : {
				type : 'vbox'
			},
			autoScroll : true,
			style : {
				padding : 0
			},
			items : [_this.dewarTab]
		});

	return _this.panel;
};

// builds the dewar panels
FillShipmentPanel.prototype.getItems = function (data) {
	var _this = this;
	_this.dewarItems = [];
	_this.listDewarPanel = [];
	if (data && data.listDewar) {
		for (var i = data.listDewar.length - 1; i >= 0; i--) {
			var dewarPanel = new DewarPanel();
			// listen for events from dewar panel
			dewarPanel.onEditDewarButtonClicked.attach(function (sender, data) {
				_this.editDewar(data.dewarIndex);
			});
			
			dewarPanel.onRemoveDewarButtonClicked.attach(function (sender, data) {
				_this.removeDewar(data.dewarIndex);
			});
			
			dewarPanel.onAddPuckButtonClicked.attach(function (sender, data) {
				_this.addNewPuck(data.dewarId);
			});
			
			dewarPanel.onEditPuckButtonClicked.attach(function (sender, data) {
				_this.editPuck(data);
			});
			
			dewarPanel.onRemovePuckButtonClicked.attach(function (sender, data) {
				_this.deletePuck(data.containerId);
			});
			
			dewarPanel.onSavePuckButtonClicked.attach(function (sender, data) {
				_this.savePuck(data.listSamples, data.puckInfo);
			});
			
			dewarPanel.onCopyPuckButtonClicked.attach(function (sender, data) {
				_this.copyPuck(data.containerId, data.listSamples);
			});
			
			dewarPanel.onPastePuckButtonClicked.attach(function (sender, data) {
				_this.pastePuck(data.dewarId);
			});
			
			dewarPanel.onCopySampleButtonClicked.attach(function (sender, data) {
				_this.setSampleToCopy(data.sampleToCopy);
			});
			
			_this.dewarItems.push({
				tabConfig : {
					title : data.listDewar[i].code, 
					iconCls: 'icon-dewar'
				},
				items : [dewarPanel.getPanel(data, i)]
			});
			_this.listDewarPanel.push(dewarPanel);
		}
	}
			
	// the last dewar is add a new dewar
	_this.dewarItems.push({
		tabConfig : {
			title : "+", 
			name: 'add-dewar-tab',
			iconCls: 'icon-dewar', 
			tooltip : {
                text : 'Click here to add a new dewar',
                title : 'Add a new dewar'
            }
		}
	});
	return _this.dewarItems;
			
};

// returns the tab, containing the different dewar panels
FillShipmentPanel.prototype.getDewarTab = function (items) {
	var _this = this;
	_this.dewarTab = Ext.create('Ext.tab.Panel', {
			activeTab : 0,
			width: "100%",
			style : {
				marginTop : '10px'
			},
			defaults : {
				bodyPadding : 0,
				autoScroll : true
			},
			listeners: {
				
				render: function () {
					this.items.each(function (i) {
						i.tab.on('click', function () {
							if (i.tab.name == 'add-dewar-tab') {
								_this.addNewDewar(_this.shippingId);
							}
						});
					});
				}
			},
			items : items
		});	
	return _this.dewarTab;
};

// refresh data
FillShipmentPanel.prototype.refresh = function (data) {
	var _this = this;
	_this.data = data;
	// clear component in the  panel
	var f;
	while (f = _this.panel.items.first()) {
		_this.panel.remove(f, true);
	}
	var items = _this.getItems(data);
	_this.dewarTab = _this.getDewarTab(items);
	_this.panel.add(_this.dewarTab);
	
	_this.panel.doLayout();
};

// add new puck: throws an Event
FillShipmentPanel.prototype.addNewPuck = function (dewarId) {
	this.onAddPuckButtonClicked.notify({
		'dewarId' : dewarId
	});
};


// add new dewar: throws a new dewar
FillShipmentPanel.prototype.addNewDewar = function (shippingId) {
	this.onAddDewarButtonClicked.notify({
		'shippingId' : shippingId
	});
};

// set the first tab activ
FillShipmentPanel.prototype.setFirstActiveTab = function () {
	var _this = this;
	_this.dewarTab.setActiveTab(0);
};


// set the last tab of dewars activ
FillShipmentPanel.prototype.setLastActiveTab = function () {
	var _this = this;
	var nbTab = _this.dewarTab.items.getCount();
	var id = nbTab - 1;
	if (nbTab > 1) {
		id = nbTab - 2;
	}
	_this.dewarTab.setActiveTab(id);
};

// returns the current dewarPanel
FillShipmentPanel.prototype.getActivDewarPanel = function () {
	var _this = this;
	var activTab = _this.dewarTab.getActiveTab();
	var idx = _this.dewarTab.items.indexOf(activTab);
	return _this.listDewarPanel[idx];
};

// set the first puck of the current dewar activ
FillShipmentPanel.prototype.setPuckFirstActiveTab = function () {
	var _this = this;
	var dewarPanel = _this.getActivDewarPanel();
	dewarPanel.setFirstActiveTab();
};

// set the last puck of the current dewar activ
FillShipmentPanel.prototype.setPuckLastActiveTab = function () {
	var _this = this;
	var dewarPanel = _this.getActivDewarPanel();
	dewarPanel.setLastActiveTab();
};

// a sample has been copied: gives info to all pucks for all dewars
FillShipmentPanel.prototype.setSampleToCopy = function (sampleToCopy) {
	var _this = this;
	for (var d=0; d<_this.listDewarPanel.length; d++){
		_this.listDewarPanel[d].setSampleToCopy(sampleToCopy);
	}
};

// editDewar action: throws an Event
FillShipmentPanel.prototype.editDewar = function (dewarIndex) {
	var _this = this;
	var dewarName = _this.data.listDewar[dewarIndex].code;
	var dewarId = _this.data.listDewar[dewarIndex].dewarId;
	_this.onEditDewarButtonClicked.notify({
		'dewarId' : dewarId, 
		'dewarName' : dewarName
	});
};

// remove dewar action: throws an event
FillShipmentPanel.prototype.removeDewar = function (dewarIndex) {
	var _this = this;
	var dewarId = _this.data.listDewar[dewarIndex].dewarId;
	_this.onRemoveDewarButtonClicked.notify({
		'dewarId' : dewarId
	});
};

// edit puck action: throws an event
FillShipmentPanel.prototype.editPuck = function (args) {
	var _this = this;
	var puckName = args.puckName;
	var containerId = args.containerId;
	_this.onEditPuckButtonClicked.notify({
		'containerId' : containerId, 
		'puckName' : puckName
	});
};

//deletepuck action: throws an event
FillShipmentPanel.prototype.deletePuck = function (containerId) {
	var _this = this;
	_this.onRemovePuckButtonClicked.notify({
		'containerId' : containerId
	});
};


// save puck: throws an event
FillShipmentPanel.prototype.savePuck = function (listSamples, puckInfo) {
	var _this = this;
	_this.onSavePuckButtonClicked.notify({
		'listSamples' : listSamples, 
		'puckInfo' : puckInfo
	});
};

// copy puck action: throws an event
FillShipmentPanel.prototype.copyPuck = function (containerId, listSamples) {
	var _this = this;
	_this.onCopyPuckButtonClicked.notify({
		'containerId' : containerId, 
		'listSamples' : listSamples
	});
};

// set the button paste puck activ on all dewar panel
FillShipmentPanel.prototype.setPuckPasteActiv = function (activ) {
	var _this = this;
	var nbT = _this.dewarTab.items.length - 1;
	for (var i = 0; i < nbT; i++) {
		var dewarPanel = _this.listDewarPanel[i];
		dewarPanel.setPuckPasteActiv(activ);
	}
};

// paste puck action: throws an event
FillShipmentPanel.prototype.pastePuck = function (dewarId) {
	var _this = this;
	_this.onPastePuckButtonClicked.notify({
		'dewarId' : dewarId
	});
};
