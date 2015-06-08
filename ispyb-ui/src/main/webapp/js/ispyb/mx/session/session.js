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
 * Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/

// session ajax calls
function Session(arg) {
	this.type = "POST";
	this.json = arg;
	this.sessionRetrieved = new Event(this);
	this.dataRetrieved = new Event(this);
	this.onSaved = new Event(this);
	/** Error * */
	this.onError = new Event(this);
}


//load session for a given proposal, with a max nb of sessions to display
Session.prototype.getSessionByProposal = function(nbSessionsToDisplay) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading Session');
	$.ajax({
				type : this.type,
				url : "viewSession.do?reqCode=getSessions&nbSessionsToDisplay=" + nbSessionsToDisplay,
				data : {},
				datatype : "text/json",
				success : function(session) {
					_this.sessionRetrieved.notify(session);
					box.hide();
				},
				error : function(xhr, ajaxOptions, thrownError) {
					_this.onError.notify(xhr.responseText);
					box.hide();
				}
			});
};


// returns the session object with the given sessionId from the given list
Session.prototype.getSessionById = function(sessionId, sessions) {
	if (sessions) {
		for (var i = 0; i < sessions.length; i++) {
			if (sessions[i].json.sessionId == sessionId) {
				return sessions[i].json;
			}
		}
	}
	return null;
};

// save the given session
Session.prototype.saveSession = function(session) {
	var _this = this;

	var box = Ext.MessageBox.wait('Please wait...', 'Saving Session');
	if (session) {
		$.ajax({
					type : this.type,
					url : "viewSession.do?reqCode=saveSession&nbSessionsToDisplay="+ nbSessionsToDisplay,
					data : {
						session : JSON.stringify(session)
					},
					datatype : "text/json",
					success : function(data) {

						_this.onSaved.notify(data.listSessionInformation);
						box.hide();
					},
					error : function(xhr, ajaxOptions, thrownError) {

						_this.onError.notify(xhr.responseText);
						box.hide();
					}
				});
	}
	return null;
};


// submit the report for the given session (for bm14)
Session.prototype.submitReport = function(session) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Submit Report');
	if (session) {
		$.ajax({
					type : this.type,
					url : "viewSession.do?reqCode=submitReport&nbSessionsToDisplay="+ nbSessionsToDisplay,
					data : {
						session : JSON.stringify(session)
					},
					datatype : "text/json",
					success : function(data) {
						box.hide();
					},
					error : function(xhr, ajaxOptions, thrownError) {

						_this.onError.notify(xhr.responseText);
						box.hide();
					}
				});
	}
	return null;
};


// remove the given session
Session.prototype.removeSession = function(session) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Remove Session');
	if (session) {
		$.ajax({
					type : this.type,
					url : "viewSession.do?reqCode=removeSession&nbSessionsToDisplay="+ nbSessionsToDisplay,
					data : {
						session : JSON.stringify(session)
					},
					datatype : "text/json",
					success : function(data) {
						_this.onSaved.notify(data.listSessionInformation);
						box.hide();
					},
					error : function(xhr, ajaxOptions, thrownError) {

						_this.onError.notify(xhr.responseText);
						box.hide();
					}
				});
	}
	return null;
};
