/*************************************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin,  A. De Maria Antolinos
 ****************************************************************************************************/
/*global Event */
/*global Ext */

// contains the ajax calls to the server, for the shipping 
function Shipping(arg) {
	this.type = "POST";
	this.json = arg;
	// Events
	this.onSaved = new Event(this);
	this.shippingRetrieved = new Event(this);
	this.shipmentRetrieved = new Event(this);
	this.onDewarUpdated = new Event(this);
	this.onPuckUpdated = new Event(this);
	this.onPuckCopied = new Event(this);
	this.onPuckPasted = new Event(this);
	/** Error **/
	this.onError = new Event(this);
}

// retrieve information for a puck
Shipping.prototype.getInformationForPuck = function () {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading Data');
	$.ajax({
		type: this.type,
		url: "createPuckAction.do?reqCode=getInformationForPuck",
		data: {},
		datatype: "text/json",
		success: function (data) {
			_this.shippingRetrieved.notify(data);
			box.hide();
		},
		error: function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};


// retrieve information for a shipping
Shipping.prototype.getInformationForShipping = function () {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading Data');
	$.ajax({
		type: this.type,
		url: "fillShipmentAction.do?reqCode=getInformationForShipment",
		data: {},
		datatype: "text/json",
		success: function (data) {
			_this.shipmentRetrieved.notify(data);
			box.hide();
		},
		error: function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};

// update a puck (from create Puck) and all info for the dewar and shipping
Shipping.prototype.updatePuck= function (listSamples, puckInfo) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Saving Data');
	var dataS = {
			listSamples	: listSamples, 
			shipmentName: puckInfo.shipmentName,
			dewarCode: puckInfo.dewarCode,
			puckCode : puckInfo.puckCode, 
			shippingId: puckInfo.shippingId,
			dewarId: puckInfo.dewarId,
			containerId: puckInfo.containerId
		};
	$.ajax({
		type: this.type,
		url: "fillShipmentAction.do?reqCode=savePuck",
		data: dataS,
		datatype: "text/json",
		success: function (data) {
			_this.onPuckUpdated.notify(data);
			box.hide();
		},
		error: function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};



// save a puck 
Shipping.prototype.savePuck = function (listSamples, puckInfo) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Saving Data');
	var dataS = {
			listSamples: listSamples, 
			shipmentName: puckInfo.shipmentName,
			dewarCode: puckInfo.dewarCode,
			puckCode : puckInfo.puckCode, 
			shippingId: puckInfo.shippingId,
			dewarId: puckInfo.dewarId,
			containerId: puckInfo.containerId
		};
	$.ajax({
		type: this.type,
		url: "createPuckAction.do?reqCode=savePuck",
		data: dataS,
		datatype: "text/json",
		success: function (data) {
			_this.onSaved.notify(data);
			box.hide();
		},
		error: function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};

// add a new dewar in a shipping
Shipping.prototype.addDewar = function (newDewar) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Adding dewar');
	if (newDewar) {
		$.ajax({
			type : this.type,
			url : "fillShipmentAction.do?reqCode=addDewar",
			data : {
				dewarName : newDewar.dewarName
			},
			datatype : "text/json",
			success : function (data) {
				_this.onDewarUpdated.notify(data);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {

				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
	}
	return null;
};

// edit a dewar
Shipping.prototype.editDewar = function (dewar) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Editing dewar');
	if (dewar) {
		$.ajax({
			type : this.type,
			url : "fillShipmentAction.do?reqCode=editDewar",
			data : {
				dewarName : dewar.dewarName, 
				dewarId : dewar.dewarId 
			},
			datatype : "text/json",
			success : function (data) {
				_this.onDewarUpdated.notify(data);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {

				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
	}
	return null;
};


//remove a dewar
Shipping.prototype.removeDewar = function (dewarId) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Removing dewar');
	if (dewarId) {
		$.ajax({
			type : this.type,
			url : "fillShipmentAction.do?reqCode=removeDewar",
			data : {
				dewarId : dewarId 
			},
			datatype : "text/json",
			success : function (data) {
				_this.onDewarUpdated.notify(data);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {

				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
	}
	return null;
};

// add a new puck in a dewar
Shipping.prototype.addPuck = function (newPuck) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Adding puck');
	if (newPuck) {
		$.ajax({
			type : this.type,
			url : "fillShipmentAction.do?reqCode=addPuck",
			data : {
				puckName : newPuck.puckName, 
				dewarId : newPuck.dewarId
			},
			datatype : "text/json",
			success : function (data) {
				_this.onPuckUpdated.notify(data);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {

				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
	}
	return null;
};

//edit a puck
Shipping.prototype.editPuck = function (puck) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Editing puck');
	if (puck) {
		$.ajax({
			type : this.type,
			url : "fillShipmentAction.do?reqCode=editPuck",
			data : {
				puckName : puck.puckName, 
				containerId : puck.containerId 
			},
			datatype : "text/json",
			success : function (data) {
				_this.onPuckUpdated.notify(data);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {

				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
	}
	return null;
};

// remove puck from a dewar
Shipping.prototype.removePuck = function (containerId) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Removing puck');
	if (containerId) {
		$.ajax({
			type : this.type,
			url : "fillShipmentAction.do?reqCode=removePuck",
			data : {
				containerId : containerId 
			},
			datatype : "text/json",
			success : function (data) {
				_this.onPuckUpdated.notify(data);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {

				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
	}
	return null;
};


// copy puck 
Shipping.prototype.copyPuck = function (containerId, listSamples) {
	var _this = this;
	if (containerId) {
		$.ajax({
			type : this.type,
			url : "fillShipmentAction.do?reqCode=copyPuck",
			data : {
				containerId: containerId, 
				listSamples: listSamples
			},
			datatype : "text/json",
			success : function (data) {
				_this.onPuckCopied.notify(data);
			},
			error : function (xhr, ajaxOptions, thrownError) {

				_this.onError.notify(xhr.responseText);
			}
		});
	}
	return null;
};


// paste a puck in a dewar
Shipping.prototype.pastePuck = function (dewarId) {
	var _this = this;
	if (dewarId) {
		$.ajax({
			type : this.type,
			url : "fillShipmentAction.do?reqCode=pastePuck",
			data : {
				dewarId : dewarId
			},
			datatype : "text/json",
			success : function (data) {
				_this.onPuckPasted.notify(data);
			},
			error : function (xhr, ajaxOptions, thrownError) {

				_this.onError.notify(xhr.responseText);
			}
		});
	}
	return null;
};