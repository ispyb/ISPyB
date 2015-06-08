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
 * Brenchereau, M. Bodin, M. Bodin
 ******************************************************************************/

function AutoProcRankingData(arg) {
	this.type = "POST";
	this.json = arg;
	this.autoProcRankingRetrieved = new Event(this);
	/** Error * */
	this.onError = new Event(this);
}

// retrieve the data for the autoProcRanking, produces an event autoProcRankingRetrieved 
AutoProcRankingData.prototype.getAutoProcRanking = function() {
	var _this = this;
	$.ajax({
				type : this.type,
				url : "autoProcRanking.do?reqCode=getAutoProcData",
				data : {},
				datatype : "text/json",
				success : function(data) {
					_this.autoProcRankingRetrieved.notify(data);
				},
				error : function(xhr, ajaxOptions, thrownError) {
					_this.onError.notify(xhr.responseText);
				}
			});
};