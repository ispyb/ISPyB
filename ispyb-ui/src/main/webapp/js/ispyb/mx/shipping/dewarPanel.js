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
/*global Ext */
/*global CreatePuckGrid */


// Dewar panel: contains a bar (with buttons) and puck tabs
function DewarPanel(args) {
	var _this = this;
	this.width = 550;
	this.height = 500;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	// index of the dewar in the dewar tabs
	this.dewarIndex = -1;
	// dewarId (from the database)
	this.dewarId = -1;
	this.contextPath = "";
	// pucks tab
	this.puckTab = null;
	// list of the puck panels
	this.listPuckPanel = null;
	
	//Events
	// add puck
	this.onAddPuckButtonClicked = new Event(this);
	// edit dewar
	this.onEditDewarButtonClicked = new Event(this);
	// remove dewar
	this.onRemoveDewarButtonClicked = new Event(this);
	// edit puck
	this.onEditPuckButtonClicked = new Event(this);
	// remove puck
	this.onRemovePuckButtonClicked = new Event(this);
	// save puck
	this.onSavePuckButtonClicked = new Event(this);
	// copy puck
	this.onCopyPuckButtonClicked = new Event(this);
	// paste puck
	this.onPastePuckButtonClicked = new Event(this);
	// copy sample
	this.onCopySampleButtonClicked = new Event(this);
}

// builds and returns the panel
DewarPanel.prototype.getPanel = function (data, dewarIndex) {
	var _this = this;
	// dewar index in the dewar tabs
	_this.dewarIndex = dewarIndex;
	_this.dewarId = -1;
	_this.contextPath = "";
	if (data) {
		_this.contextPath = data.contextPath;
		_this.dewarId = data.listDewar[dewarIndex].dewarId;
	}
	// builds the pucks panels
	var puckItems = _this.getItems(data);
	// builds the tab		
	_this.puckTab = _this.getPuckTab(puckItems);
	
	// print icon and text depend of the data
	var icon = '../images/print_16x16.png';
	var tooltip = 'Please print your component labels and stick them on the transport box.';
	var alertMsg = "";
	if (data && data.listDewar && data.listDewar[dewarIndex]) {
		var isWarningIcon = data.listDewar[dewarIndex].warningIcon;
		if (data.listDewar[dewarIndex].warningIcon == true) {
			icon = '../images/print_warning_16x16.png';
		}
		tooltip = data.listDewar[dewarIndex].tooltip;
		alertMsg = data.listDewar[dewarIndex].alertMessage;
	}
	// paste icon enable if something has been copied
	var pasteDisabled = true;
	if (data && data.canPaste && data.canPaste == true) {
		pasteDisabled = false;
	}
		
	// builds the main panel
	_this.panel = Ext.widget({
		xtype : 'form',
		layout : {
			type : 'fit'
		},
		collapsible : false,
		frame : true,
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 100
		},
		defaultType : 'textfield',
		defaults : {
			labelStyle : 'padding-bottom:10px;'
		},
		style : {
			marginBottom : '10px'
		},
		tbar: [{
				icon : '../images/Edit_16x16_01.png', 
				tooltip : {
						text : 'Edit the dewar label',
						title : 'Edit dewar'
					},
				handler : function () {
						_this.editDewar();
					}
			}, {
				icon : icon, 
				tooltip : {
						text : tooltip,
						title : 'Print labels'
					},
				handler : function () {
					if (confirm(alertMsg)) {
						window.location.href = _this.contextPath + "/reader/viewDewarAction.do?reqCode=generateLabels&dewarId=" + _this.dewarId;
					}
				}
			}, {xtype: 'tbseparator'}, 
			{
					tooltip : {
						text : 'Paste the copied puck with samples in this dewar',
						title : 'Paste puck'
					},
					id: 'button_paste_' + _this.dewarId,
					icon : '../images/editpaste.png', 
					disabled: pasteDisabled, 
					handler : function () {
						_this.pastePuck();
					}
							
				}, {xtype: 'tbseparator'},
				{
				icon : '../images/cancel.png', 
				tooltip : {
						text : 'Delete this dewar together with the pucks inside',
						title : 'Delete dewar'
					},
				handler : function () {
					_this.deleteDewar();
				}
			}],
		items : [_this.puckTab]
	});

	return _this.panel;
};

// builds the puck grids for each containers
DewarPanel.prototype.getItems = function (data) {
	var _this = this;
	_this.puckItems = [];
	_this.listPuckPanel = [];
	var containers = [];
	if (data && data.listOfContainer) {
		containers = data.listOfContainer[_this.dewarIndex];
	}
	for (var j = containers.length - 1; j >= 0; j--) {
		var createPuckGrid = new CreatePuckGrid();
		// create puck grid listen events
		createPuckGrid.onEditPuckButtonClicked.attach(function (sender, data) {
			_this.editPuck(data.containerId, data.puckName);
		});
		createPuckGrid.onRemovePuckButtonClicked.attach(function (sender, data) {
			_this.deletePuck(data.containerId);
		});
		
		createPuckGrid.onSaveButtonClicked.attach(function (sender, args) {
			//var listSamples = createPuckGrid.getListSamples();
			//var puckInfo = createPuckGrid.getPuckInfo();
			
			_this.savePuck(args.listSamples, args.puckInfo);
		});
		
		createPuckGrid.onCopyPuckButtonClicked.attach(function (sender, data) {
			//var listSamples = createPuckGrid.getListSamples();
			_this.copyPuck(data.containerId, data.listSamples);
		});
		
		createPuckGrid.onCopySampleButtonClicked.attach(function (sender, data) {
			var sampleToCopy = data.sampleToCopy;
			_this.onCopySampleButtonClicked.notify({
				'sampleToCopy' : sampleToCopy
			});
		});
		
		var puckData = [];
		puckData.proteinList = data.proteinList;
		puckData.spaceGroupList = data.spaceGroupList;
		puckData.experimentTypeList =  data.experimentTypeList;
		puckData.crystalValuesList = data.crystalValuesList;
		puckData.listSamples = data.listOfSamples[_this.dewarIndex][j];
		puckData.fillShipmentMode = true;
		var puckInfo = [];
		puckInfo.containerId = containers[j].containerId;
		puckInfo.code = containers[j].code;
		puckInfo.shippingId = data.shippingId;
		puckInfo.dewarId = data.listDewar[_this.dewarIndex].dewarId;
		puckInfo.dewarCode = data.listDewar[_this.dewarIndex].code;
		puckInfo.shipmentName = data.shippingName;
		puckData.puckInfo = puckInfo;
		
		_this.puckItems.push({
				tabConfig : {
					title : containers[j].code, 
					iconCls: 'icon-puck'
				},
				items : [createPuckGrid.getGrid(puckData)]
			});
		_this.listPuckPanel.push(createPuckGrid);
	} // end of iterator
	// the last tab is to add a new puck
	_this.puckItems.push({
		tabConfig : {
			title : "+", 
			name: 'add-puck-tab',
			iconCls: 'icon-puck', 
			tooltip : {
                text : 'Click here to add a new puck',
                title : 'Add a new puck'
            }
		}
	});
	return _this.puckItems;
			
};

// a sample has been copied: gives info to all grids
DewarPanel.prototype.setSampleToCopy = function (sampleToCopy) {
	var _this = this;
	for (var g=0; g<_this.listPuckPanel.length; g++){
		_this.listPuckPanel[g].setSampleToCopy(sampleToCopy);
	}
};

// builds the tab
DewarPanel.prototype.getPuckTab = function (items) {
	var _this = this;
	_this.puckTab = Ext.create('Ext.tab.Panel', {
		activeTab : 0,
		width: "100%",
		style : {
				marginTop : '10px'
			},
		defaults : {
			bodyPadding : 0,
			autoScroll : true
		},
		items : items, 
		listeners: {
			render: function () {
				this.items.each(function (i) {
					i.tab.on('click', function () {
						if (i.tab.name == 'add-puck-tab') {
							_this.addNewPuck(_this.dewarId);
						}
					});
				});
			}
		}
	});
	return _this.puckTab;
};

// refresh data: clear all and rebuild
DewarPanel.prototype.refresh = function (data) {
	var _this = this;
	_this.data = data;
	// clear component in the  panel
	var f;
	while (f = _this.panel.items.first()) {
		_this.panel.remove(f, true);
	}
	var items = _this.getItems(data);
	_this.puckTab = _this.getPuckTab(items);
	_this.panel.add(_this.puckTab);
	
	_this.panel.doLayout();
};


// set the first tab activ
DewarPanel.prototype.setFirstActiveTab = function () {
	var _this = this;
	_this.puckTab.setActiveTab(0);
};

// set the last puck tab activ
DewarPanel.prototype.setLastActiveTab = function () {
	var _this = this;
	var nbTab = _this.puckTab.items.getCount();
	var id = nbTab - 1;
	if (nbTab > 1) {
		id = nbTab - 2;
	}
	_this.puckTab.setActiveTab(id);
};

// click on add new puck: throws an event
DewarPanel.prototype.addNewPuck = function (dewarId) {
	this.onAddPuckButtonClicked.notify({
		'dewarId' : dewarId
	});
};

// edit dewar click: throws an event
DewarPanel.prototype.editDewar = function () {
	var _this = this;
	_this.onEditDewarButtonClicked.notify({'dewarIndex' : _this.dewarIndex});
};

// delete dewar click: after confirmation throws an event
DewarPanel.prototype.deleteDewar = function () {
	var _this = this;
	if (confirm("Do you want to remove this dewar?")) {
		this.onRemoveDewarButtonClicked.notify({
			'dewarIndex' : _this.dewarIndex
		});
	}
};


// edit puck click: throws an event
DewarPanel.prototype.editPuck = function (containerId, puckName) {
	var _this = this;
	_this.onEditPuckButtonClicked.notify({
		'containerId' : containerId, 
		'puckName' : puckName
	});
};


// delete puck click: throws an event
DewarPanel.prototype.deletePuck = function (containerId) {
	var _this = this;
	_this.onRemovePuckButtonClicked.notify({
		'containerId' : containerId
	});
};


// save puck click: throws an event
DewarPanel.prototype.savePuck = function (listSamples, puckInfo) {
	var _this = this;
	_this.onSavePuckButtonClicked.notify({
		'listSamples' : listSamples, 
		'puckInfo' : puckInfo
	});
};

// copy puck click: throws an event
DewarPanel.prototype.copyPuck = function (containerId, listSamples) {
	var _this = this;
	_this.onCopyPuckButtonClicked.notify({
		'containerId' : containerId, 
		'listSamples' : listSamples
	});
};

// returns the activ puck grid selected
DewarPanel.prototype.getActivPuckPanel = function () {
	var _this = this;
	var activTab = _this.puckTab.getActiveTab();
	var idx = _this.puckTab.items.indexOf(activTab);
	return _this.listPuckPanel[idx];
};

// a puck has been copied: the paste button is enabled (or not)
DewarPanel.prototype.setPuckPasteActiv = function (activ) {
	var _this = this;
	Ext.getCmp('button_paste_' + _this.dewarId).setDisabled(!activ);
};

// paste puck click: throws an event
DewarPanel.prototype.pastePuck = function () {
	var _this = this;
	_this.onPastePuckButtonClicked.notify({'dewarId' : _this.dewarId});
};