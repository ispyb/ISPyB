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
/*global document */

/*global contextPath */

// ajax calls for the dataCollections
function Collection(arg) {
	this.type = "POST";
	this.json = arg;
	// Events
	this.collectionRetrieved = new Event(this);
	this.workflowRetrieved = new Event(this);
	this.referencesRetrieved = new Event(this);
	this.criteriaRetrieved = new Event(this);
	this.onRank = new Event(this);
	this.onRankAutoProc = new Event(this);
	this.onSaved = new Event(this);
	/** Error * */
	this.onError = new Event(this);
}

// gets data for manager View Last Collects
Collection.prototype.getLastCollect = function (beamlineName, startDate,
		endDate, startTime, endTime) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading Last Collects');
	$.ajax({
			type : this.type,
			url : "viewDataCollection.do?reqCode=getLastCollect&searchBeamline=" +  beamlineName + '&startDate=' + startDate +
				'&endDate=' + endDate + '&startTime=' + startTime + 
				'&endTime=' + endTime,
			data : {},
			datatype : "text/json",
			success : function (dataCollection) {

				_this.collectionRetrieved.notify(dataCollection);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {
				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
};

// get Criteria for Last Collects for manager account
Collection.prototype.getSearchCriteria = function () {
	var _this = this;
	// var box = Ext.MessageBox.wait('Please wait...', 'Loading data');
	$.ajax({
			type : this.type,
			url : "viewDataCollection.do?reqCode=getSearchCriteria",
			data : {},
			datatype : "text/json",
			success : function (criteria) {

				_this.criteriaRetrieved.notify(criteria);
				// box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {
				_this.onError.notify(xhr.responseText);
				// box.hide();
			}
		});
};


// gets the collects for 1 dataCollectionGroup
Collection.prototype.getDataCollectionByDataCollectionGroup = function (dataCollectionGroupId) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading data');
	$.ajax({
		type : this.type,
		url : "viewDataCollection.do?reqCode=getDataCollectionByDataCollectionGroup&dataCollectionGroupId=" + dataCollectionGroupId,
		datatype : "text/json",
		success : function (dataCollection) {

			_this.collectionRetrieved.notify(dataCollection);
			box.hide();
		},
		error : function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};

// gets the collects for 1 session
Collection.prototype.getDataCollectionBySession = function () {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading data');
	$.ajax({
			type : this.type,
			url : "viewDataCollection.do?reqCode=getDataCollectionBySession",
			datatype : "text/json",
			success : function (dataCollection) {

				_this.collectionRetrieved.notify(dataCollection);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {
				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
};

// save data Collection information (comments)
Collection.prototype.saveDataCollection = function (listDataCollection) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Saving Data');
	$.ajax({
			type : this.type,
			url : "viewDataCollection.do?reqCode=saveAllDataCollection",
			data : {
				dataCollectionList : listDataCollection
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


// rank collect EDNA
Collection.prototype.rank = function (listDataCollection) {
	var _this = this;
	$.ajax({
			type : this.type,
			url : "viewDataCollection.do?reqCode=rankJs&dataCollectionIdList=" + listDataCollection,
			data : {},
			datatype : "text/json",
			success : function (data) {
				_this.onRank.notify();
			},
			error : function (xhr, ajaxOptions, thrownError) {
				_this.onError.notify(xhr.responseText);
			}
		});
};


// rank collects for AutoProc
Collection.prototype.rankAutoProc = function (listDataCollection) {
	var _this = this;
	$.ajax({
		type : this.type,
		url : "viewDataCollection.do?reqCode=rankAutoProcJs&dataCollectionIdList=" + listDataCollection,
		data : {},
		datatype : "text/json",
		success : function (data) {
			_this.onRankAutoProc.notify();
		},
		error : function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
		}
	});
};


// gets the worklfow information
Collection.prototype.getWorkflow = function () {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading data');
	$.ajax({
			type : this.type,
			url : "viewWorkflow.do?reqCode=getWorkflow",
			datatype : "text/json",
			success : function (data) {

				_this.workflowRetrieved.notify(data);
				box.hide();
			},
			error : function (xhr, ajaxOptions, thrownError) {
				_this.onError.notify(xhr.responseText);
				box.hide();
			}
		});
};


// gets the dataCollections for 1 sample
Collection.prototype.getDataCollectionBySample = function () {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading data');
	$.ajax({
		type : this.type,
		url : "viewDataCollection.do?reqCode=getDataCollectionBySample",
		datatype : "text/json",
		success : function (dataCollection) {

			_this.collectionRetrieved.notify(dataCollection);
			box.hide();
		},
		error : function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};


// gets the dataCollections for 1 protein
Collection.prototype.getDataCollectionByProtein = function () {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading data');
	$.ajax({
		type : this.type,
		url : "viewDataCollection.do?reqCode=getDataCollectionByProtein",
		datatype : "text/json",
		success : function (dataCollection) {

			_this.collectionRetrieved.notify(dataCollection);
			box.hide();
		},
		error : function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};


// gets the dataCollections for 1 query (search)
Collection.prototype.getDataCollectionByCustomQuery = function () {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading data');
	$.ajax({
		type : this.type,
		url : "viewDataCollection.do?reqCode=getDataCollectionByCustomQuery",
		datatype : "text/json",
		success : function (dataCollection) {

			_this.collectionRetrieved.notify(dataCollection);
			box.hide();
		},
		error : function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};

// gets dataCollection for 1 sampleId
Collection.prototype.getDataCollectionBySampleId = function () {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading data');
	$.ajax({
		type : this.type,
		url : "viewDataCollection.do?reqCode=getDataCollectionBySampleId",
		datatype : "text/json",
		success : function (dataCollection) {

			_this.collectionRetrieved.notify(dataCollection);
			box.hide();
		},
		error : function (xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};

