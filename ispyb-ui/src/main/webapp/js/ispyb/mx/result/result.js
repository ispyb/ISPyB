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
// ajax calls for the result Page
function Result(arg) {
	this.type = "POST";
	this.json = arg;
	// Events
	this.resultRetrieved = new Event(this);
	this.autoProcRetrieved = new Event(this);
	this.autoProcDetailRetrieved = new Event(this);
	/** Error * */
	this.onError = new Event(this);
}

// get the results data 
Result.prototype.getResult = function(rMerge, iSigma) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading Data');
	$.ajax({
				type : this.type,
				url : "viewResults.do?reqCode=getResultData&rMerge="+rMerge+"&iSigma="+iSigma,
				data : {},
				datatype : "text/json",
				success : function(data) {
					_this.resultRetrieved.notify(data);
					box.hide();
				},
				error : function(xhr, ajaxOptions, thrownError) {
					_this.onError.notify(xhr.responseText);
					box.hide();
				}
			});
};


// get AutoProcessing information for 1 collect
Result.prototype.getAutoprocessingInfo = function(rMerge, iSigma) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading Autoprocessings');
	$.ajax({
				type : this.type,
				url : "viewResults.do?reqCode=getAutoprocessingInfo&rMerge="+rMerge+"&iSigma="+iSigma,
				data : {},
				datatype : "text/json",
				success : function(data) {
					_this.autoProcRetrieved.notify(data);
					box.hide();
				},
				error : function(xhr, ajaxOptions, thrownError) {
					_this.onError.notify(xhr.responseText);
					box.hide();
				}
			});
};


// get the autoProcessing information for 1 autoProc
Result.prototype.getAutoprocessingDetails = function(autoProcId) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...',
			'Displaying Autoprocessing information');
	$.ajax({
				type : this.type,
				url : "viewResults.do?reqCode=getAutoprocessingDetails&autoProcId="+ autoProcId,
				data : {},
				datatype : "text/json",
				success : function(data) {
					_this.autoProcDetailRetrieved.notify(data);
					box.hide();
				},
				error : function(xhr, ajaxOptions, thrownError) {
					_this.onError.notify(xhr.responseText);
					box.hide();
				}
			});
};
