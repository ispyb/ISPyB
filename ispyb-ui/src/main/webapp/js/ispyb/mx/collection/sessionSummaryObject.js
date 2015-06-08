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
function SessionSummaryObject(arg) {
	this.type = "POST";
	this.json = arg;
	// Events
	this.collectionGroupRetrieved = new Event(this);
	this.sessionRetrieved = new Event(this);
	this.imageSaved = new Event(this);
	this.sessionSummaryRetrieved = new Event(this);
	this.onSaved = new Event(this);
	/** Error * */
	this.onError = new Event(this);
}

// gets more recent data for the Session report page
SessionSummaryObject.prototype.getSessionSummary = function (nbOfItems) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading Data');
	$.ajax({
		type : this.type,
		url : "viewSessionSummary.do?reqCode=getSessionSummary&nbOfItems=" + nbOfItems,
		data : {},
		datatype : "text/json",
		success : function (data) {
			_this.sessionSummaryRetrieved.notify(data);
			box.hide();
		},
		error : function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};

// save data (comments and crystal class) of the session report page
SessionSummaryObject.prototype.saveData = function (listData, sessionId,nbOfItems) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Saving Data');
	$.ajax({
			type : this.type,
			url : "viewSessionSummary.do?reqCode=saveAllData",
			data : {
				nbOfItems : nbOfItems,
				sessionId : sessionId,
				listData : listData
			},
			datatype : "text/json",
			success : function (data) {
				_this.onSaved.notify(data);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {
				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
};

// chnage page in the session Report Page (notify to the server)
SessionSummaryObject.prototype.changeSessionReportPageNumber = function (currentPageNumber, sessionId) {
	var _this = this;
	$.ajax({
		type : this.type,
		url : "viewSessionSummary.do?reqCode=changeCurrentPageNumber",
		data : {
			sessionId : sessionId, 
			sessionReportCurrentPageNumber: currentPageNumber 
		},
		datatype : "text/json",
		success : function () {
		},
		error : function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
		}
	});
};