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

/*global Event */
/*global Ext */
// ajax calls to server concerning dataCollectionGroup and Session Summary (report)
function CollectionGroup(arg) {
	this.type = "POST";
	this.json = arg;
	// Events
	this.collectionGroupRetrieved = new Event(this);
	this.sessionRetrieved = new Event(this);
	this.imageSaved = new Event(this);
	this.onSaved = new Event(this);
	/** Error * */
	this.onError = new Event(this);
}


// get the collection Groups for 1 session
CollectionGroup.prototype.getCollectionGroupBySession = function () {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading Data');
	$.ajax({
		type : this.type,
		url : "viewDataCollectionGroup.do?reqCode=getDataCollectionGroupBySession",
		data : {},
		datatype : "text/json",
		success : function (dataCollectionGroup) {
			_this.collectionGroupRetrieved.notify(dataCollectionGroup);
			box.hide();
		},
		error : function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};

// gets the dataCollectionGroups for 1 sample
CollectionGroup.prototype.getCollectionGroupBySample = function () {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading Data');
	$.ajax({
		type : this.type,
		url : "viewDataCollectionGroup.do?reqCode=getDataCollectionGroupBySample",
		data : {},
		datatype : "text/json",
		success : function (dataCollectionGroup) {
			_this.collectionGroupRetrieved.notify(dataCollectionGroup);
			box.hide();
		},
		error : function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};


// save dataCollection Groups (comments and crystal class)
CollectionGroup.prototype.saveDataCollectionGroup = function (
		listDataCollectionGroup, sessionId) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Saving Data');
	$.ajax({
			type : this.type,
			url : "viewDataCollectionGroup.do?reqCode=saveAllDataCollectionGroups",
			data : {
				sessionId : sessionId,
				dataCollectionGroupList : listDataCollectionGroup
			},
			datatype : "text/json",
			success : function (dataCollectionGroup) {
				_this.onSaved.notify(dataCollectionGroup);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {
				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
};


// save XRFSpectra (comments and crystal class)
CollectionGroup.prototype.saveXRFSpectra = function (listXFESpectra, sessionId) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Saving Data');
	$.ajax({
			type : this.type,
			url : "viewDataCollectionGroup.do?reqCode=saveAllXFE",
			data : {
				sessionId : sessionId,
				listXFESpectra : listXFESpectra
			},
			datatype : "text/json",
			success : function (xrfSpectra) {
				_this.onSaved.notify(xrfSpectra);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {
				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
};

// gets the session information (session form)
CollectionGroup.prototype.getSessionInformation = function (sessionId) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading Data');
	$.ajax({
			type : this.type,
			url : "viewDataCollectionGroup.do?reqCode=getSessionInformation",
			data : {
				sessionId : sessionId
			},
			datatype : "text/json",
			success : function (data) {
				_this.sessionRetrieved.notify(data);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {
				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
};

// save image graphs to be included in the reports
CollectionGroup.prototype.saveImageForExport = function (listSvg, sessionId, listId) {
	var _this = this;
	var data = {
			listSvg : JSON.stringify(listSvg)
		};
	data.sessionId = sessionId;
	data.listId = JSON.stringify(listId);
	$.ajax({
			type : this.type,
			url : "exportDataCollection.do?reqCode=saveImageForExport",
			data : data,
			dataType: "json",
			success : function (data) {
				_this.imageSaved.notify(data);
			},
			error : function (xhr, ajaxOptions, thrownError) {
				_this.onError.notify(xhr.responseText);
			}
		});
};

