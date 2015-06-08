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


// Data Confidentiality log panel
function DataConfidentialityPanel(args) {
	var _this = this;
	this.width = 550;
	this.height = 500;
	
	this.onLaunch = new Event(this);
	this.onError = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}		
}

// update the data after a search
DataConfidentialityPanel.prototype.update = function (searchCriteria) {
	var _this = this;
	// clear component in the  panel
	var f;
	while (f = _this.panel.items.first()) {
		_this.panel.remove(f, true);
	}
	var iframe=  _this.getIFrame (searchCriteria.all, searchCriteria.perMonth, searchCriteria.month, searchCriteria.year, 
			searchCriteria.perDate, searchCriteria.logDate);
	_this.panel.add(iframe);
	_this.panel.doLayout();
};

// builds and return the iFrame in which are displayed the logs
DataConfidentialityPanel.prototype.getIFrame = function (all, perMonth, month, year, perDate, logDate) {
	var iframe =  Ext.create('Ext.ux.SimpleIFrame', {
		src: "dataConfidentialityAction.do?reqCode=getDataConfidentialityLog&all=" + all + 
		"&perMonth=" + perMonth + "&month=" + month + "&year=" + year +
		"&perDate=" + perDate + "&logDate=" + logDate
	});
	iframe.setSize(600, 600);
	return iframe;
};

//launch the protection of sessions
DataConfidentialityPanel.prototype.launchDataConfidentiality = function (launchCriteria) {
		var _this = this;
		$.ajax({
				type : this.type,
				url : "/ispyb/manager/launchDataConfidentiality.do?reqCode=launchJs&dateTo=" + launchCriteria.dateTo + "&dateFrom=" + launchCriteria.dateFrom+ "&sesId=" + launchCriteria.sesId,
				data : {},
				datatype : "text/json",
				success : function (data) {
					_this.onLaunch.notify(
							{'listSessions' : sessionsIds});
				},
				error : function (xhr, ajaxOptions, thrownError) {
					_this.onError.notify(xhr.responseText);
				}
			});

    };


// build the main panel
DataConfidentialityPanel.prototype.getPanel = function () {
	var _this = this;
	_this.dcLog = "";
	var today = new Date();
	var mm = today.getMonth()+1;
	var yyyy = today.getFullYear();
	
	// build iframe		
	var iframe =  _this.getIFrame(null, true, mm, yyyy, false, null);
		
	_this.panel = Ext.create('Ext.Panel', {
			layout : 'fit',
			collapsible : false,
			frame : true,
			bodyPadding : '5 5 0',
			items : [iframe]

		});

	return _this.panel;
};



