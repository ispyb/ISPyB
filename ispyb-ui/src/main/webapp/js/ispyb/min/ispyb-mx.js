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


// Search criteria to launch the data confidentiality for not protected sessions
function LaunchDataConfidentialityPanel(args) {
	var _this = this;
	this.width = 400;
	this.height = 500;

	this.onLaunchDataConfidentiality = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	
}

// returns the launch criteria selected by the user
LaunchDataConfidentialityPanel.prototype.getLaunchCriteria = function () {
	var _this = this;
	var dateTo = Ext.getCmp(this.panel.getItemId()).getValues().dateTo;
	var dateFrom = Ext.getCmp(this.panel.getItemId()).getValues().dateFrom;
	var sesId = Ext.getCmp(this.panel.getItemId()).getValues().sesId;
	var launch = [];
	launch.dateTo = dateTo;
	launch.dateFrom = dateFrom;
	launch.sesId = sesId;
	return launch;
};

// build the main panel
LaunchDataConfidentialityPanel.prototype.getPanel = function () {
	var _this = this;
	
	//dates by default
	var dateTo = new Date();
	var t = dateTo.getTime();
	var day = (1000 * 60 * 60 * 24);
	t = t - 2 * day;
	dateTo = dateTo.setTime(t);
	t = t - 3 * day;
	var dateFrom = new Date();
	dateFrom = dateFrom.setTime(t);
	var sesId;
	
	_this.panel = Ext.widget({
		xtype : 'form',
		//layout : 'form',
		collapsible : true,
		id : 'launchDataConfidentialityForm',
		frame : true,
		title : 'Launch Data Confidentiality',
		width : this.width,
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 100
		},
		style : {
			marginBottom : '10px'
		},
		defaultType : 'textfield',
		layout : {
			type : 'table',
			columns : 2
		},
		defaults : {
			labelStyle : 'padding-left:10px;'
		},
		items : [
		    {
				xtype : 'datefield',
				name : 'dateFrom',
				fieldLabel : '(DD/MM/YYYY)',
				format : 'd/m/Y',
				submitFormat : 'd/m/Y',
				value: dateFrom,
				colspan : 2
		    },
		    {
				xtype : 'datefield',
				name : 'dateTo',
				fieldLabel : '(DD/MM/YYYY)',
				format : 'd/m/Y',
				submitFormat : 'd/m/Y',
				value: dateTo,
				colspan : 2
		    },
		    {
				xtype : 'numberfield',
				name : 'sesId',
				fieldLabel : 'sessionId',
				value: sesId,
				colspan : 2
		    }
		],

		buttons : [{
			text : 'Launch',
			handler : function() {
				_this.onLaunchDataConfidentiality.notify();
			}
		}, {
			text : 'Reset',
			handler : function() {
				this.up('form').getForm().reset();
			}
		}]
	});
	
	return _this.panel;
};
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

Ext.Loader.setConfig({
		enabled : true
	});

var dataConfidentialityPanel = null;

// main entry point for the data confidentiality log page
var IspybDataConfidentiality = {
	start : function (targetId) {
		this.targetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showDataConfidentialityPage(targetId);
	},

	// display the data confidentiality page
	showDataConfidentialityPage : function (targetId) {
		var _this = this;
		
		// data confidentiality panel
		dataConfidentialityPanel = new DataConfidentialityPanel();
		
		// this panel contains the different options available, to display the logs: per day, month or all
		var viewLogOptionPanel = new ViewLogOptionPanel();
		viewLogOptionPanel.onViewLog.attach(function(evt,data) {
			var searchCriteria = viewLogOptionPanel.getSearchCriteria();
			dataConfidentialityPanel.update(searchCriteria);
		});
		
		//launch data confidentiality panel
		var  launchDataConfidentialityPanel = new LaunchDataConfidentialityPanel();
		launchDataConfidentialityPanel.onLaunchDataConfidentiality.attach(function(evt,data) {
			var launchCriteria = launchDataConfidentialityPanel.getLaunchCriteria();
			dataConfidentialityPanel.launchDataConfidentiality(launchCriteria);
		});

		var horizontalPanel = Ext.create('Ext.panel.Panel', {
			plain : false,
			frame : false,
			layout : {
					type : 'hbox'
				},
			border : 0,
			style : {
				padding : 0
			},
			items : [
			    viewLogOptionPanel.getPanel(), 
			    launchDataConfidentialityPanel.getPanel()
			]
		});
				
		// main panel creation	
		_this.panel = Ext.create('Ext.panel.Panel', {
			plain : true,
			frame : false,
			border : 0,
			autoScroll : true,
			renderTo : targetId,
			style : {
				padding : 0
			},
			items : [
			    horizontalPanel,
			    dataConfidentialityPanel.getPanel()
			]
		});
	}
};


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


// Search criteria to display the data confidentiality log
function ViewLogOptionPanel(args) {
	var _this = this;
	this.width = 550;
	this.height = 500;

	this.onViewLog = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	this.all = false;
	this.perMonth = false;
	this.perDate = false;
}

// returns the search criteria selected by the user
ViewLogOptionPanel.prototype.getSearchCriteria = function () {
	var _this = this;
	var month = Ext.getCmp(this.panel.getItemId()).getValues().month;
	var year = Ext.getCmp(this.panel.getItemId()).getValues().year;
	var logDate = Ext.getCmp(this.panel.getItemId()).getValues().logDate;
	var search = [];
	search.all = _this.all;
	search.perMonth = _this.perMonth;
	search.month = month;
	search.year = year;
	search.perDate = _this.perDate;
	search.logDate = logDate;
	return search;
};


// disable or enable the options, depending of the choice (after a click on the radiobutton)
ViewLogOptionPanel.prototype.changeLogPeriod = function (selectedInput) {
	var _this = this;
	var allItems = Ext.getCmp(this.panel.getItemId()).items.items;
	
	for (var i = 0; i < allItems.length; i++) {
		var item = allItems[i];
		if (item.name == 'logDate') {
			item.setDisabled(!(selectedInput == 'perDate'));
		} else if (item.name == 'month') {
			item.setDisabled(!(selectedInput == 'perMonth'));
		} else if (item.name == 'year') {
			item.setDisabled(!(selectedInput == 'perMonth'));
		}
	}
	_this.perDate = (selectedInput == 'perDate');
	_this.perMonth  = (selectedInput == 'perMonth');
	_this.all = (selectedInput == 'all');
};


// build the main panel
ViewLogOptionPanel.prototype.getPanel = function () {
	var _this = this;
	
	var today = new Date();
	var mm = today.getMonth() + 1;
	var yyyy = today.getFullYear();
	
	_this.panel = Ext.widget({
		xtype : 'form',
		//layout : 'form',
		collapsible : true,
		id : 'viewLogForm',
		frame : true,
		title : 'View Data Protection Log',
		width : this.width,
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 100
		},
		style : {
			marginBottom : '10px'
		},
		defaultType : 'textfield',
		layout : {
			type : 'table',
			columns : 3
		},
		defaults : {
			labelStyle : 'padding-left:10px;'
		},
		items : [ {
			xtype: 'radio',
            boxLabel: 'All',
            name: 'log-period',
            inputValue: 'all', 
            colspan : 3, 
			listeners: {
                change: {
                    fn: function (field, newValue, oldValue, options) {
                        if (newValue == true) {
                            _this.changeLogPeriod(field.inputValue);
                        }
                    }
                }
            }
        }, {
			xtype: 'radio',
            boxLabel: 'Per month',
            checked: true,
            name: 'log-period',
            inputValue: 'perMonth', 
			listeners: {
                change: {
                    fn: function (field, newValue, oldValue, options) {
						if (newValue == true) {
							_this.changeLogPeriod(field.inputValue);
						}
                    }
                }
            }
        },
		{
            xtype: 'numberfield',
            name: 'month',
            fieldLabel: 'Month',
			labelWidth : 50,
            value: mm,
			width: 150,
            minValue: 1,
            maxValue: 12
        },
		{
            xtype: 'numberfield',
            name: 'year',
            fieldLabel: 'Year',
			labelWidth : 50,
			width: 200,
            value: yyyy,
            minValue: 2013
        },
		{
            xtype: 'radio',
			boxLabel: 'Per date',
            name: 'log-period',
            inputValue: 'perDate',
			listeners: {
                change: {
                    fn: function (field, newValue, oldValue, options) {
						if (newValue == true) {
							_this.changeLogPeriod(field.inputValue);
						}
                    }
                }
            }
        }, {
			xtype : 'datefield',
			name : 'logDate',
			fieldLabel : '(DD/MM/YYYY)',
			format : 'd/m/Y',
			submitFormat : 'd/m/Y',
			value: today,
			colspan : 2
		}],

		buttons : [{
			text : 'View Log',
			handler : function () {
				_this.onViewLog.notify();
			}
		}, {
			text : 'Reset',
			handler : function () {
				this.up('form').getForm().reset();
			}
		}]
	});
	_this.changeLogPeriod('perMonth');
	return _this.panel;
};
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

// crystal class Grid : summury of the nb Collect/crystal class
function CrystalClassGrid(args) {
	var isFx = false;
	var contextPath = "";
	this.title = "Summary";
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.title != null) {
			this.title = args.title;
		}
	}

	// data model
	Ext.define('CrystalClassModel', {
			extend : 'Ext.data.Model',
			fields : [{
					name : 'crystalClassCode',
					mapping : 'crystalClassCode'
				}, {
					name : 'crystalClassName',
					mapping : 'crystalClassName'
				}, {
					name : 'numberOfCrystals',
					mapping : 'numberOfCrystals'
				}],
			idProperty : 'crystalClassCode'
		});
}

// no filters
CrystalClassGrid.prototype.getFilterTypes = function () {
	return [];
};

// refresh data: reload
CrystalClassGrid.prototype.refresh = function (data) {
	this.features = data.listCrystal;
	this.store.loadData(this.features, false);
};

// sort by default
CrystalClassGrid.prototype._sort = function (store) {
	store.sort('crystalClassName', 'ASC');
};

// gets the grid
CrystalClassGrid.prototype.getGrid = function (listCrystal) {
	this.features = listCrystal;
	return this.renderGrid(listCrystal);
};

// builds the grid
CrystalClassGrid.prototype.renderGrid = function () {
	var _this = this;

	/** Prepare data * */
	var data = [];
	// builds columns
	var columns = this._getColumns();
	// grid data
	this.store = Ext.create('Ext.data.Store', {
			model : 'CrystalClassModel',
			autoload : false
		});

	this._sort(this.store);
	this.store.loadData(this.features, false);
	// if no data
	var noCrystalDataPanel = Ext.create('Ext.Panel', {
			id : 'noCrystalDataPanel',
			layout : {
				type : 'fit', // Arrange child items vertically
				align : 'center', // Each takes up full width
				bodyPadding : 0,
				border : 0
			},
			html : ''
		});

	if (this.features.length == 0) { 
		return noCrystalDataPanel;
	}

	// build the grid
	this.grid = Ext.create('Ext.grid.Panel', {
			style : {
				padding : 0,
				marginTop : '10px'
			},
			model : 'CrystalClassModel',
			store : this.store,
			columns : columns,
			title : this.title,
			viewConfig : {
				stripeRows : true,
				enableTextSelection : true
			},
			selModel : {
				mode : 'SINGLE'
			}
		});

	return this.grid;
};

// build the columns
CrystalClassGrid.prototype._getColumns = function () {
	var _this = this;

	var columns = [{
			text : 'Crystal class name',
			dataIndex : 'crystalClassName',
			flex : 0.2
		}, {
			text : 'Crystal class code',
			dataIndex : 'crystalClassCode',
			flex : 0.1
		}, {
			text : 'Number of crystals',
			dataIndex : 'numberOfCrystals',
			flex : 0.1
		}];
	return columns;

};

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

// dataCollection Grid, used in the worklfow page, dataCollection page, lastCollect page (manager)
function DataCollectionGrid(args) {

	var contextPath = "";

	this.pageSize = 100;
	this.title = "Last Data Collection";
	this.listOfCrystalClass = [];
	this.contextPath = "";
	this.displayLastCollect = false;
	this.isManager = false;
	this.isIndustrial = false;
	this.pagingMode = false;
	this.groupFieldDefault = '';
	this.setArgs(args);
	// Events
	this.onSaveButtonClicked = new Event(this);
	this.onPagingMode = new Event(this);
	this.onRankButtonClicked = new Event(this);
	this.onRankAutoProcButtonClicked = new Event(this);

	this.hideCol = true;
	this.rankEDNA = true;

	this.minDCId = 1;
	this.maxDCId = 0;
	
	this.rMergeCutoff = 10;
	this.iSigmaCutoff = 1;
	
	// dataCollection data model
	Ext.define('DataCollectionModel', {
			extend : 'Ext.data.Model',
			fields : [{
					name : 'dataCollectionId',
					mapping : 'dataCollectionId'
				}, {
					name : 'experimentType',
					mapping : 'experimentType'
				}, {
					name : 'startTime',
					mapping : 'startTime',
					type : 'date',
					dateFormat : 'd-m-Y H:i:s'
				}, {
				    name : 'endTime',
					mapping : 'endTime',
					type : 'date',
					dateFormat : 'd-m-Y H:i:s'
				}, {
					name : 'beamLineName',
					mapping : 'beamLineName'
				}, {
					name : 'proposal',
					mapping : 'proposal'
				}, {
					name : 'imagePrefix',
					mapping : 'imagePrefix'
				}, {
					name : 'dataCollectionNumber',
					mapping : 'dataCollectionNumber'
				}, {
					name : 'proteinAcronym',
					mapping : 'proteinAcronym'
				}, {
					name : 'pdbFileName',
					mapping : 'pdbFileName'
				}, {
					name : 'numberOfImages',
					mapping : 'numberOfImages'
				}, {
					name : 'hasSnapshot',
					mapping : 'hasSnapshot'
				}, {
					name : 'runStatus',
					mapping : 'runStatus'
				}, {
					name : 'autoProc',
					mapping : 'autoProc'
				}, {
					name : 'autoProcStatisticsOverall',
					mapping : 'autoProcStatisticsOverall'
				}, {
					name : 'autoProcStatisticsInner',
					mapping : 'autoProcStatisticsInner'
				}, {
					name : 'autoProcStatisticsOuter',
					mapping : 'autoProcStatisticsOuter'
				}, {
					name : 'screeningOutput',
					mapping : 'screeningOutput'
				}, {
					name : 'lattice',
					mapping : 'lattice'
				}, {
					name : 'comments',
					mapping : 'comments'
				}, {
					name : 'autoprocessingStatus',
					mapping : 'autoprocessingStatus'
				}, {
					name : 'autoprocessingStep',
					mapping : 'autoprocessingStep'
				}, {
					name : 'wavelength',
					mapping : 'wavelength'
				}, {
					name : 'transmission',
					mapping : 'transmission'
				}, {
					name : 'exposureTime',
					mapping : 'exposureTime'
				}, {
					name : 'axisStart',
					mapping : 'axisStart'
				}, {
					name : 'axisRange',
					mapping : 'axisRange'
				}, {
					name : 'resolution',
					mapping : 'resolution'
				}, {
					name : 'transmission',
					mapping : 'transmission'
				}, {
					name : 'hasAutoProcAttachment',
					mapping : 'hasAutoProcAttachment'
				}, {
					name : 'printableForReport',
					mapping : 'printableForReport'
				}, {
					name : 'skipForReport',
					mapping : 'skipForReport'
				}],
			idProperty : 'dataCollectionId'
		});
}

// args passed to the constructor
DataCollectionGrid.prototype.setArgs = function (args) {
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.title != null) {
			this.title = args.title;
		}
		if (args.listOfCrystalClass != null) {
			this.listOfCrystalClass = args.listOfCrystalClass;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.displayLastCollect != null) {
			this.displayLastCollect = args.displayLastCollect;
		}
		if (args.isManager != null) {
			this.isManager = args.isManager;
		}
		if (args.pagingMode != null) {
			this.pagingMode = args.pagingMode;
		}
		if (args.isIndustrial != null) {
			this.isIndustrial = args.isIndustrial;
		}
		if (args.groupFieldDefault != null) {
			this.groupFieldDefault = args.groupFieldDefault;
		}
		if (args.iSigmaCutoff != null) {
			this.iSigmaCutoff = args.iSigmaCutoff;
		}
		if (args.rMergeCutoff != null) {
			this.rMergeCutoff = args.rMergeCutoff;
		}
		
	}
};

// sort by default
DataCollectionGrid.prototype._sort = function () {
	this.store.sort('startTime', 'DESC');
};


// get grids
DataCollectionGrid.prototype.getGrid = function (dataCollections) {
	this.features = dataCollections;
	return this.renderGrid(dataCollections);
};

// refresh data: reload
DataCollectionGrid.prototype.refresh = function (data) {
	this.features = data.dataCollectionList;
	this.store.loadData(this.features, false);
	this.title = "Last " + this.features.length + " DataCollections";
	this.grid.setTitle(this.title);
};

// build the grid
DataCollectionGrid.prototype.renderGrid = function () {
	var _this = this;

	// min & max ID collect
	if (this.features && this.features.length > 0) {
		this.minDCId = this.features[0].dataCollectionId;
		this.maxDCId = this.features[0].dataCollectionId;
		var fl = this.features.length;
		for (var i = 0; i < fl; i++) {
			var id = this.features[i].dataCollectionId;
			if (id > this.maxDCId) {
				this.maxDCId = id;
			}
			if (id < this.minDCId) { 
				this.minDCId = id;
			}
		}
	}
	/** Prepare data * */
	var data = [];
	// build columns
	var columns = this._getColumns();
	
	
	// grid data
	if (this.pagingMode) {
		this.store = Ext.create('Ext.data.Store', {
				model : 'DataCollectionModel',
				autoload : false,
				pageSize : this.pageSize,
				proxy : {
					type : 'pagingmemory',
					data : this.features,
					reader : {
						type : 'array'
					}
				}
			});
	} else if (_this.groupFieldDefault) {
		this.store = Ext.create('Ext.data.Store', {
				model : 'DataCollectionModel',
				autoload : false,
				groupField: _this.groupFieldDefault
			});
	} else {
		this.store = Ext.create('Ext.data.Store', {
			model : 'DataCollectionModel',
			autoload: false
		});
	}
	
	// bottom bar
	var bbar = null;
	var pagingButtonText = "Pagination";
	if (this.pagingMode) {
		pagingButtonText = "Remove pagination";
		bbar = Ext.create('Ext.PagingToolbar', {
				store : this.store,
				pageSize : this.pageSize,
				displayInfo : true,
				displayMsg : 'Displaying dataCollections {0} - {1} of {2}',
				emptyMsg : "No dataCollection to display",
				listeners : {
					afterrender : function () {
						var a = Ext.query("button[data-qtip=Refresh]");
						for (var x = 0; x < a.length; x++) {
							a[x].style.display = "none";
						}
					}
				}
			});

	}

	// this._sort(this.store);
	this.store.loadData(this.features, false);
	// set title
	if (this.displayLastCollect) {
		this.title = "Last " + this.features.length + " DataCollections";
	} else {
		this.title = this.features.length + " DataCollections";
	}

	// this grid can be edited
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});

	// grouping grid
	_this.groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
		groupHeaderTpl : '{columnName}: {name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
		hideGroupedHeader : true,
		startCollapsed : false,
		id : 'dataCollectionGrouping'
	});
	var groups = this.store.getGroups();
	var len = groups.length;
	var toggleGroup = function (item) {
		var groupName = item.text;
		if (item.checked) {
			_this.groupingFeature.expand(groupName, true);
		} else {
			_this.groupingFeature.collapse(groupName, true);
		}
	};

	// Define the model for a rank
	Ext.regModel('Rank', {
			fields : [{
					type : 'string',
					name : 'name'
				}]
		});
	var ranks = [{
			"name" : "EDNA"
		}, {
			"name" : "AutoProc"
		}];
	// The data store holding the ranks
	var rankStore = Ext.create('Ext.data.Store', {
			model : 'Rank',
			data : ranks
		});

	// Simple ComboBox using the data store
	var rankCombo = Ext.create('Ext.form.field.ComboBox', {
			displayField : 'name',
			width : 100,
			labelWidth : 10,
			store : rankStore,
			queryMode : 'local',
			typeAhead : true,
			forceSelection : true,
			value : 'EDNA',
			listeners : {
				change : function (field, newValue, oldValue) {
					_this.changeRank();
				}
			}
		});

	// top bar with buttons
	var tbar = [];
	if (!this.displayLastCollect) {
		tbar.push({
				text : 'Save',
				tooltip : 'Save dataCollections: comments and skip status',
				icon : '../images/save.gif',
				handler : function () {
					_this.onSaveButtonClicked.notify({});
				}
			});
		tbar.push({
				text : 'Experiment Parameters',
				tooltip : 'Expand or Collapse Experiment Parameter',
				icon : '../images/Info_16x16_01.png',
				handler : function () {
					_this.hideAndShowExperimentParameter();
				}
			});
		tbar.push({
				text : 'Rank',
				tooltip : 'Start Sample Ranking',
				handler : function () {
					_this.startRank();
				}
			}, rankCombo);
	}

	if (this.displayLastCollect) {
		tbar.push({
				text : pagingButtonText,
				handler : function () {
					_this.onPagingMode.notify();
				}
			});
	}
	tbar.push('->');
	tbar.push({
		text : 'Expand All',
		handler : function () {
			var rows = _this.groupingFeature.view.getEl().query('.x-grid-group-body');

			Ext.each(rows, function (row) {
						_this.groupingFeature.expand(Ext.get(row));
					});
		}
	}, {
		text : 'Collapse All',
		handler : function () {
			var rows = _this.groupingFeature.view.getEl().query('.x-grid-group-body');

			Ext.each(rows, function (row) {
						_this.groupingFeature.collapse(Ext.get(row));
					});
		}
	});
	tbar.push({
			text : 'Clear Grouping',
			iconCls : 'icon-clear-group',
			handler : function () {
				_this.groupingFeature.disable();
			}
		});

	// Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	
	// grid creation
	this.grid = Ext.create('Ext.grid.Panel', {
		style : {
			padding : 0
		},
		width : "100%",
		height : "100%",
		model : 'DataCollectionModel',
		store : this.store,
		resizable : true,
		//height:800,
		columns : columns,
		title : this.title,
		viewConfig : {
			stripeRows : true,
			enableTextSelection : true
		},
		selModel : {
			mode : 'SINGLE'
		},
		plugins : [cellEditing],
		features : [_this.groupingFeature],

		tbar : tbar,
		// paging bar on the bottom
		bbar : bbar
			// stateId: "dataCollectionGridStateId", // this is unique state ID
			// stateful: true, // state should be preserved
			// stateEvents: ['columnresize', 'columnmove', 'show', 'hide' ] //
			// which event should cause store state
	});
	if (this.pagingMode) {
		this.store.load();
	}
	// hideCol
	// this._hideCol();
	//this._sort(this.store);
	return this.grid;
};

// returns the index of the column, -1 if not found
DataCollectionGrid.prototype._getIndex = function (dataIndex) {
	var gridColumns = this.grid.columns;
	var gl = gridColumns.length;
	for (var i = 0; i < gl; i++) {
		if (gridColumns[i].dataIndex == dataIndex) {
			return i;
		}
	}
	return -1;
};

// hide the columns experiments
DataCollectionGrid.prototype._hideCol = function () {
	if (!this.displayLastCollect) {
		var id = this._getIndex("experimentParameters");
		if (id != -1) {
			this.grid.columns[id].setVisible(this.hideCol);
		}
		// We use 'Wavelength' and 'Detector Resolution' as start and stop
		// points and hide everything in between.
		var id0 = this._getIndex("wavelength");
		var id1 = this._getIndex("resolution");
		if (id0 != -1 && id1 != -1) {
			var nb = id1 - id0 + 1;
			for (var i = 0; i < nb; i++) {
				this.grid.columns[id0 + i].setVisible(!this.hideCol);
			}
		}
	}
};

// build the columns
DataCollectionGrid.prototype._getColumns = function () {
	var _this = this;
	// render functions
	
	// render date
	function renderStartDate(value, p, record) {
		var time = Ext.Date.format(value, 'H:i:s');
		var date = Ext.Date.format(value, 'd-m-Y');
		return time + "<br />" + date;
	}

	// render image prefix with link
	function renderImagePrefix(value, p, record) {
		return '<div style="white-space:normal !important;word-wrap: break-word">' + '<a href="' +
		 _this.contextPath +
		'/user/viewResults.do?reqCode=display&dataCollectionId=' + 
		record.getId() + '">' + value + '</a>' + '</div>';
	}

	// render protein acronym
	function renderProteinAcronym(value, p, record) {
		if (record.data.pdbFileName && record.data.pdbFileName !== "") {
			p.tdAttr = 'data-qtip="' + 
			("pdb file uploaded:" + record.data.pdbFileName) + '"';
			return "<img src='../images/attach.png' border='0'  />" + value;
		} else {
			return value;
		}
	}

	// format nb with 2 digits
	function renderFixed2(value, p, record) {
		if (value) { 
			return value.toFixed(2);
		}
		else {
			return "";
		}
	}

	// format nb with 3 digits
	function renderFixed3(value, p, record) {
		if (value) { 
			return value.toFixed(3);
		}
		else { 
			return "";
		}
	}

	// return the message for status
	function statusPopup(icon1, mess1, icon2, mess2, icon3, mess3, icon4, mess4) {
		var popupMessage = "";
		popupMessage += "<TABLE cellSpacing=2 cellPadding=0 width=100% border=0>";
		popupMessage += "<TR><TD valign=middle><img src='" + icon1 +
			"' border=0\/><\/TD><TD class='smallText_black' nowrap>" + mess1 + "<\/TD><\/TR>";
		popupMessage += "<TR><TD valign=middle><img src='" + icon2 +
			"' border=0\/><\/TD><TD class='smallText_black' nowrap>" + mess2 + "<\/TD><\/TR>";
		popupMessage += "<TR><TD valign=middle><img src='" + icon3 +
			"' border=0\/><\/TD><TD class='smallText_black' nowrap>" + mess3 + "<\/TD><\/TR>";
		popupMessage += "<TR><TD valign=middle><img src='" + icon4 +
		"' border=0\/><\/TD><TD class='smallText_black' nowrap>" + mess4 + "<\/TD><\/TR>";
		popupMessage += "<\/TABLE>";
		return popupMessage;
	}
	
	// render status with balls
	function renderStatus(value, p, record) {
		var icon1 = '../images/Sphere_White_12.png';
		var icon2 = '../images/Sphere_White_12.png';
		var icon3 = '../images/Sphere_White_12.png';
		var icon4 = '../images/Sphere_White_12.png';
		var mess1 = "";
		var mess2 = "";
		var mess3 = "";
		var mess4 = "";

		// crystal snapshot
		if (record.data.hasSnapshot && record.data.hasSnapshot) {
			icon1 = "../images/Sphere_Green_12.png";
			mess1 = "This collection has a crystal snapshot.";
		} else {
			icon1 = "../images/Sphere_White_12.png";
			mess1 = "This collection has no crystal snapshot.";
		}
		// run status
		if (record.data.runStatus) {
			mess2 = record.data.runStatus;
			if (record.data.runStatus.search("successful") != -1) {
				icon2 = "../images/Sphere_Green_12.png";
			} else if (record.data.runStatus.search("failed") != -1) {
				icon2 = "../images/Sphere_Red_12.png";
			} else if (record.data.runStatus.search("Running") != -1) {
				icon2 = "../images/Sphere_Orange_12.png";
			} else {
				icon2 = "../images/Sphere_Yelow_12.png";
			}
		}
		// EDNA status
		if (record.data.screeningOutput) {
			if (record.data.screeningOutput.indexingSuccess) {
				if (record.data.screeningOutput.strategySuccess) {
					if (record.data.screeningOutput.indexingSuccess == 1) {
						if (record.data.screeningOutput.strategySuccess == 1) {
							icon3 = "../images/Sphere_Green_12.png";
							mess3 = "Indexing is successful.";
						} else {
							icon3 = "../images/Sphere_Orange_12.png";
							mess3 = "Indexing is successful but no strategy.";
						}
					} else {
						icon3 = "../images/Sphere_Red_12.png";
						mess3 = "Indexing failed.";
					}
				} else {
					icon3 = "../images/Sphere_Yelow_12.png";
					mess3 = "Indexing status unknown.";
				}
			} else {
				icon3 = "../images/Sphere_Yelow_12.png";
				mess3 = "Indexing status unknown.";
			}
		} else {
			icon3 = "../images/Sphere_White_12.png";
			mess3 = "Indexing has not been launched.";
		}
		// Autoprocessing status
		if (record.data.autoprocessingStatus) {
			if (record.data.autoprocessingStatus == "Successful") {
				icon4 = "../images/Sphere_Green_12.png";
				mess4 = "Autoprocessing: " + record.data.autoprocessingStep + " Successful";
			} else if (record.data.autoprocessingStatus == "Launched") {
				icon4 = "../images/Sphere_Orange_12.png";
				mess4 = "Autoprocessing: " + record.data.autoprocessingStep + " Launched";
			} else if (record.data.autoprocessingStatus == "Failed") {
				icon4 = "../images/Sphere_Red_12.png";
				mess4 = "Autoprocessing: " + record.data.autoprocessingStep + " Failed";
			}
		} else {
			icon4 = "../images/Sphere_White_12.png";
			mess4 = "Autoprocessing status unknown.";
		}
		//
		var myToolTipText = statusPopup(icon1, mess1, icon2, mess2, icon3,
				mess3, icon4, mess4);
		p.tdAttr = 'data-qtip="' + myToolTipText + '"';
		return "<img src='" + icon1 + "' border=0>" + "<img src='" + icon2 + "' border=0>" + "<img src='" + icon3 + "' border=0>" +
		"<img src='" + icon4 + "' border=0>";
	}

	// render completeness
	function renderCompleteness(value, p, record) {
		if (record.data.autoProcStatisticsOverall && record.data.autoProcStatisticsOuter && record.data.autoProcStatisticsInner) {
			var percentage1 = record.data.autoProcStatisticsInner.completeness;
			var percentage2 = record.data.autoProcStatisticsOuter.completeness;
			var percentage3 = record.data.autoProcStatisticsOverall.completeness;

			var initial = -61;
			var imageWidth = 122;
			var eachPercent = (imageWidth / 2) / 100;

			var percentageWidth1 = eachPercent * percentage1;
			var actualWidth1 = initial + percentageWidth1;
			var colour1 = percentage1 < 85 ? 4 : 1;

			var percentageWidth2 = eachPercent * percentage2;
			var actualWidth2 = initial + percentageWidth2;
			var colour2 = percentage2 < 85 ? 4 : 1;

			var percentageWidth3 = eachPercent * percentage3;
			var actualWidth3 = initial + percentageWidth3;
			var colour3 = percentage3 < 85 ? 4 : 1;

			var s = "";
			if (!(percentage1 === 0 && percentage2 === 0 && percentage3 === 0)) {
				s += '<img " src="../images/percentImage_small.png" title="' +
						percentage1 + '% (inner)" alt="' + percentage1 +
						'% (inner)" class="percentImage' + colour1 +
						'" style="background-position: ' + actualWidth1 +
						'px 0pt; "/><br/>';
				s += '<img  src="../images/percentImage_small.png" title="' + 
						percentage2 + '% (outer)" alt="' + percentage2 + 
						'% (outer)" class="percentImage' + colour2 + 
						'" style="background-position: ' + actualWidth2 + 
						'px 0pt; "/><br/>';
				s += '<img  src="../images/percentImage_small.png" title="' + 
						percentage3 + '% (overall)" alt="' + percentage3 + 
						'% (overall)" class="percentImage' + colour3 + 
						'" style="background-position: ' + actualWidth3 + 
						'px 0pt; "/><br/>';
			}
			return s;
		}
		return "";
	}

	// render resolution
	function renderResolution(value, p, record) {
		if (record.data.autoProcStatisticsOverall && record.data.autoProcStatisticsOuter && record.data.autoProcStatisticsInner) {
			var s = (record.data.autoProcStatisticsInner.resolutionLimitLow ? record.data.autoProcStatisticsInner.resolutionLimitLow : "") + 
					" - " +
					(record.data.autoProcStatisticsInner.resolutionLimitHigh ? record.data.autoProcStatisticsInner.resolutionLimitHigh : "") + "<br />";
			s += (record.data.autoProcStatisticsOuter.resolutionLimitLow ? record.data.autoProcStatisticsOuter.resolutionLimitLow : "") +
					" - " +
					(record.data.autoProcStatisticsOuter.resolutionLimitHigh ? record.data.autoProcStatisticsOuter.resolutionLimitHigh : "") + "<br />";
			s += (record.data.autoProcStatisticsOverall.resolutionLimitLow ? record.data.autoProcStatisticsOverall.resolutionLimitLow : "") +
					" - " +
					(record.data.autoProcStatisticsOverall.resolutionLimitHigh ? record.data.autoProcStatisticsOverall.resolutionLimitHigh : "") + "<br />";
			return s;
		} else if (record.data.screeningOutput) {
			return Number(record.data.screeningOutput.rankingResolution)
					.toFixed(2);
		}
		return "";
	}

	// render rsymm
	function renderRsymm(value, p, record) {
		if (record.data.autoProcStatisticsOverall && record.data.autoProcStatisticsOuter && record.data.autoProcStatisticsInner) {
			var s = (record.data.autoProcStatisticsInner.rmerge ? Number(record.data.autoProcStatisticsInner.rmerge).toFixed(1): "") + "<br />";
			s += (record.data.autoProcStatisticsOuter.rmerge ? Number(record.data.autoProcStatisticsOuter.rmerge).toFixed(1) : "") + "<br />";
			s += (record.data.autoProcStatisticsOverall.rmerge ? Number(record.data.autoProcStatisticsOverall.rmerge).toFixed(1): "") + "<br />";
			return s;
		}
		return "";
	}

	// render unit cell
	function renderUnitCell(value, p, record) {
		var nbDec = 2;
		if (record.data.autoProc) {
			var s = (record.data.autoProc.refinedCellA ? Number(record.data.autoProc.refinedCellA).toFixed(nbDec) + ", " : "");
			s += (record.data.autoProc.refinedCellB ? Number(record.data.autoProc.refinedCellB).toFixed(nbDec) + ", ": "");
			s += (record.data.autoProc.refinedCellC ? Number(record.data.autoProc.refinedCellC).toFixed(nbDec) + "<br />": "");
			s += (record.data.autoProc.refinedCellAlpha ? Number(record.data.autoProc.refinedCellAlpha).toFixed(nbDec) + ", " : "");
			s += (record.data.autoProc.refinedCellBeta ? Number(record.data.autoProc.refinedCellBeta).toFixed(nbDec) + ", " : "");
			s += (record.data.autoProc.refinedCellGamma ? Number(record.data.autoProc.refinedCellGamma).toFixed(nbDec): "");
			return s;
		} else if (record.data.lattice) {
			var s1 = (record.data.lattice.unitCell_a ? Number(record.data.lattice.unitCell_a).toFixed(nbDec) + ", ": "");
			s1 += (record.data.lattice.unitCell_b ? Number(record.data.lattice.unitCell_b).toFixed(nbDec) + ", ": "");
			s1 += (record.data.lattice.unitCell_c ? Number(record.data.lattice.unitCell_c).toFixed(nbDec) + "<br />": "");
			s1 += (record.data.lattice.unitCell_alpha ? Number(record.data.lattice.unitCell_alpha).toFixed(nbDec) + ", ": "");
			s1 += (record.data.lattice.unitCell_beta ? Number(record.data.lattice.unitCell_beta).toFixed(nbDec) + ", ": "");
			s1 += (record.data.lattice.unitCell_gamma ? Number(record.data.lattice.unitCell_gamma).toFixed(nbDec) : "");
			return s1;
		}
		return "";
	}

	// render spaceGroup, edna or autoProc
	function renderSpaceGroup(value, p, record) {
		if (record.data.autoProc) {
			return record.data.autoProc.spaceGroup;
		} else {
			// edna space group
			if (record.data.lattice) {
				return record.data.lattice.spaceGroup;
			} else {
				return "";
			}
		}
	}

	// column wrap
	function columnWrap(value) {
		if (value) { 
			return '<div style="white-space:normal !important;">' + value + '</div>';
		}
		else { 
			return  "";
		}
	}

	// render experiment parameters (col hidden or expanded)
	function renderExperimentParameter(value, p, record) {
		var icon = '../images/Info_16x16_01.png';
		var myToolTipText = "";
		myToolTipText += "Wavelength: " + (record.data.wavelength ? record.data.wavelength.toFixed(3): "");
		myToolTipText += "<br/>";
		myToolTipText += "Transmission: " + (record.data.transmission ? record.data.transmission.toFixed(3) : "");
		myToolTipText += "<br/>";
		myToolTipText += "ex. Time: " + (record.data.exposureTime ? record.data.exposureTime.toFixed(2) : "");
		myToolTipText += "<br/>";
		myToolTipText += "PhiStart: " + (record.data.axisStart ? record.data.axisStart.toFixed(2): "");
		myToolTipText += "<br/>";
		myToolTipText += "PhiRange: " + (record.data.axisRange ? record.data.axisRange.toFixed(2): "");
		myToolTipText += "<br/>";
		myToolTipText += "Detector Resolution: " + (record.data.resolution ? record.data.resolution.toFixed(2): "");
		p.tdAttr = 'data-qtip="' + myToolTipText + '"';
		return "<img src='" + icon + "' border=0>";
	}

	// render Rank Column
	function renderCheckBoxRank(value, p, record) {
		var ednaChecked = false;
		var autoProcChecked = false;
		if (record.data.screeningOutput) {
			if (record.data.screeningOutput.indexingSuccess) {
				if (record.data.screeningOutput.strategySuccess) {
					if (record.data.screeningOutput.indexingSuccess == 1) {
						ednaChecked = true;
					}
				}
			}
		} else if (record.data.autoProc) {
			autoProcChecked = true;
		}
		var s = "";
		if (ednaChecked) {
			s += "<input checked='" + ednaChecked + "' name='rankIdList[" + 
					record.data.dataCollectionId + 
					"]' value='on' type='checkbox'>";
			s += "<img  src='../images/checkbox_grey.png' id='rankIdImage[" + 
					record.data.dataCollectionId + "]' style='display:none'>";
		}
		if (autoProcChecked) {
			s += "<input checked='" + autoProcChecked + 
					"' name='rankIdAutoProcList[" + 
					record.data.dataCollectionId + 
					"]' value='on' type='checkbox' style='display:none'>";
			s += "<img  src='../images/checkbox_grey.png' id='rankIdAutoProcImage[" + 
					record.data.dataCollectionId + "]' style='display:none'>";
		}
		if (!ednaChecked && !autoProcChecked) { 
			return "<img src='../images/checkbox_grey.png' border=0>";
		}
		else { 
			return s;
		}
	}
	
	// render Skip
	function renderCheckBoxSkip(value, p, record) {
		var s = "<input " + (value ? "checked='checked'" : "") + " property='skipIdList[" + 
					record.data.dataCollectionId + 
					"]'  value='1' type='checkbox'>";						
		return s;		
	}
	
	// hide element
	function actionItemRenderer(value, meta, record, rowIx, ColIx, store) {
		if (!record.get('hasAutoProcAttachment')) {
			return 'x-hide-display';
		}
	}

	// build columns
	var columns = [];
	if (this.displayLastCollect) {
		columns.push({
				text : 'Beamline<br/>Name',
				dataIndex : 'beamLineName',
				flex : 0.08,
				id : 'beamLineName'
			});
	}
	if (this.isManager) {
		columns.push({
				text : 'Proposal',
				dataIndex : 'proposal',
				flex : 0.1,
				id : 'proposal'
			});
	}

	columns.push(
	{
			text : 'Image<br/>Prefix',
			dataIndex : 'imagePrefix',
			width : 70,
			renderer : renderImagePrefix
		}, {
			text : 'Run<br/>No',
			dataIndex : 'dataCollectionNumber',
			flex : 0.02
		}, {
			text : '#<br/>images',
			dataIndex : 'numberOfImages',
			flex : 0.05
		});

	if (!this.displayLastCollect) {
		columns.push({
				text : 'Exp.<br/>Param.<br/>',
				dataIndex : 'experimentParameters',
				flex : 0.05,
				renderer : renderExperimentParameter
			});

		columns.push({
				text : 'Wavelenth',
				dataIndex : 'wavelength',
				flex : 0.07,
				renderer : renderFixed3
			}, {
				text : 'Transm.',
				dataIndex : 'transmission',
				flex : 0.07,
				renderer : renderFixed3
			});
		if (this.isIndustrial) {
			columns.push({
					text : 'Distance',
					dataIndex : 'detectorDistance',
					flex : 0.05,
					renderer : renderFixed2
				});
		}
		columns.push({
				text : 'Ex.<br>Time',
				dataIndex : 'exposureTime',
				flex : 0.05,
				renderer : renderFixed2
			}, {
				text : 'Phi<br>start',
				dataIndex : 'axisStart',
				flex : 0.05,
				renderer : renderFixed2
			}, {
				text : 'Phi<br>range',
				dataIndex : 'axisRange',
				flex : 0.05,
				renderer : renderFixed2
			});
		if (this.isIndustrial) {
			columns.push({
					text : 'Xbeam',
					dataIndex : 'xbeam',
					flex : 0.05,
					renderer : renderFixed2
				}, {
					text : 'Ybeam',
					dataIndex : 'ybeam',
					flex : 0.05,
					renderer : renderFixed2
				});
		}
		columns.push({
				text : 'Detector<br/>Resolution',
				dataIndex : 'resolution',
				flex : 0.07,
				renderer : renderFixed2
			});
	}
	columns.push({
			text : 'Status',
			dataIndex : 'status',
			flex : 0.05,
			renderer : renderStatus
		}, {
			text : 'Space<br/>Group',
			dataIndex : 'autoProc',
			width : 70,
			renderer : renderSpaceGroup
		}, {
			text : 'Completeness',
			dataIndex : 'completeness',
			width : 70,
			renderer : renderCompleteness
		}, {
			text : 'Resolution',
			dataIndex : 'resolution',
			width : 80,
			renderer : renderResolution
		}, {
			text : 'Rsymm<br/><small><i>Inner<br/>Outer<br/>Overall</i></small>',
			dataIndex : 'autoProcStatisticsOverall',
			width : 40,
			renderer : renderRsymm
		}, {
			text : 'Unit_cell<br/><small><i>a, b, c<br/>alpha, beta, gamma</i></small>',
			dataIndex : 'autoProcStatisticsInner',
			width : 120,
			renderer : renderUnitCell
		}, {
			text : 'Exp.<br/>Type',
			dataIndex : 'experimentType',
			flex : 0.05
		}, {
			text : 'Protein<br>Acronym',
			dataIndex : 'proteinAcronym',
			flex : 0.05,
			renderer : renderProteinAcronym
		}, {
			text : 'Start<br/>time',
			dataIndex : 'startTime',
			width : 75,
			renderer : renderStartDate
		} );

	if (!this.displayLastCollect) {
		columns.push({
				header : 'Sample<br/>Ranking',
				dataIndex : 'dataCollectionId',
				flex : 0.05,
				renderer : renderCheckBoxRank
			});
	}
	
	if (!this.isIndustrial) {
		columns.push(
			{
				xtype : 'checkcolumn',
				header : 'Skip',
				dataIndex : 'skipForReport',
				flex : 0.04,
				renderer : renderCheckBoxSkip
			}
		);
	}
	
	if (this.displayLastCollect || this.isIndustrial) {
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				flex : 0.2,
				renderer : columnWrap
			});
	} else {
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				flex : 0.2,
				editor : {
					xtype : 'textfield',
					allowBlank : true
				},
				renderer : columnWrap
			});
	}
	
	columns.push({	
		xtype : 'actioncolumn',
		width : 50,
		header : 'Download<br>autoproc<br>files',		
		items : [{
			icon : '../images/download.png',
			tooltip : 'Download autoproc',
			getClass : actionItemRenderer,
			handler : function (grid, rowIndex, colIndex) {
				// console.log("View collect: " + rowIndex + " = " +_this.store.getAt(rowIndex).data.dataCollectionId);
				_this.downloadAutoproc(_this.store.getAt(rowIndex).data.dataCollectionId);
			}
		}],
		sortable : false
	});
	
	return columns;

};


// redirection to the detail of a collect
DataCollectionGrid.prototype.viewDataCollection = function (dataCollectionId) {
	document.location.href = this.contextPath + 
			"/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForDataCollectionGroup&dataCollectionGroupId=" + dataCollectionId;
};


// returns the paging mode
DataCollectionGrid.prototype.getPagingMode = function () {
	return this.pagingMode;
};

// hode or show the experiment parameters columns
DataCollectionGrid.prototype.hideAndShowExperimentParameter = function () {
	this.hideCol = !this.hideCol;
	this._hideCol();
};


// returns the dataCollections as json
DataCollectionGrid.prototype.getListDataCollection = function () {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};

// change rank EDNA or AutoProc
DataCollectionGrid.prototype.changeRank = function () {
	this.rankEDNA = !this.rankEDNA;
	this.setRankValue();
};

// set the rank value, EDNA or autoProc
DataCollectionGrid.prototype.setRankValue = function () {
	for (var i = this.minDCId; i <= this.maxDCId; i++) {
		var rankList = document.getElementsByName("rankIdList[" + i + "]");
		if (rankList != null && rankList.item(0) != null) {
			if (this.rankEDNA) {
				rankList.item(0).style.display = '';
			} else {
				rankList.item(0).style.display = 'none';
			}
		}
		var rankAutoProcList = document.getElementsByName("rankIdAutoProcList[" + i + "]");
		if (rankAutoProcList != null && rankAutoProcList.item(0) != null) {
			if (this.rankEDNA) {
				rankAutoProcList.item(0).style.display = 'none';
			} else {
				rankAutoProcList.item(0).style.display = '';
			}
		}
		var rankImage = document.getElementById("rankIdImage[" + i + "]");
		if (rankImage != null) {
			if (this.rankEDNA) {
				rankImage.style.display = 'none';
			} else {
				rankImage.style.display = '';
			}
		}
		var rankAutoProcImage = document.getElementById("rankIdAutoProcImage[" + i + "]");
		if (rankAutoProcImage != null) {
			if (this.rankEDNA) {
				rankAutoProcImage.style.display = '';
			} else {
				rankAutoProcImage.style.display = 'none';
			}
		}
	}

};

// rank click: throws an event
DataCollectionGrid.prototype.startRank = function () {
	if (this.rankEDNA) {
		this.onRankButtonClicked.notify({});
	} else {
		this.onRankAutoProcButtonClicked.notify({});
	}
};

// returns the list of collect to rank for EDNA
DataCollectionGrid.prototype.getListDataCollectionRank = function () {
	var dataCollectionIds = [];
	for (var i = this.minDCId; i <= this.maxDCId; i++) {
		var rankList = document.getElementsByName("rankIdList[" + i + "]");
		if (rankList != null && rankList.item(0) != null) {
			dataCollectionIds.push(i);
		}
	}
	return dataCollectionIds;
};

// returns the list of collect to rank for autoproc
DataCollectionGrid.prototype.getListDataCollectionRankAutoProc = function () {
	var dataCollectionIds = [];
	for (var i = this.minDCId; i <= this.maxDCId; i++) {
		var rankList = document.getElementsByName("rankIdAutoProcList[" + i + "]");
		if (rankList != null && rankList.item(0) != null) {
			dataCollectionIds.push(i);
		}
	}
	return dataCollectionIds;
};

// return the list of collects to skip
DataCollectionGrid.prototype.getListDataCollectionSkip = function () {
	var dataCollectionIds = [];
	for (var i = this.minDCId; i <= this.maxDCId; i++) {
		var skipList = document.getElementsByName("skipIdList[" + i + "]");
		if (skipList != null && skipList.item(0) != null && skipList.item(0) == false ) {
			dataCollectionIds.push(i);
		}
	}
	return dataCollectionIds;
};


// download autoprocessing files redirection
DataCollectionGrid.prototype.downloadAutoproc = function (dataCollectionId) {
	var rmerge = this.rMergeCutoff;
	var isigma = this.iSigmaCutoff;
	document.location.href = this.contextPath +  "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=download&dataCollectionId=" + dataCollectionId + "&rmerge=" + rmerge + "&isigma=" + isigma + "&anomalous=false";
	// "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=download&dataCollectionId=" + dataCollectionId + "&rmerge=" + vdcForm.getRMergeCutoff() + "&isigma=" + vdcForm.getISigmaCutoff() + "&anomalous=false";
};

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

// Data Collection Group grid
function DataCollectionGroupGrid(args) {

	// Events
	this.onSaveButtonClicked = new Event(this);

	var isFx = false;
	var isIndus = false;
	var contextPath = "";
	this.title = "DataCollectionGroups";
	this.canSave = false;
	this.listOfCrystalClass = [];
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.isIndus != null) {
			this.isIndus = args.isIndus;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.title != null) {
			this.title = args.title;
		}
		if (args.listOfCrystalClass != null) {
			this.listOfCrystalClass = args.listOfCrystalClass;
		}
		if (args.sessionId != null) {
			this.sessionId = args.sessionId;
		}
		if (args.canSave != null) {
			this.canSave = args.canSave;
		}
	}

	// format crystal class
	this._setCrystalClass();
	

	// data collection group datamodel
	Ext.define('DataCollectionGroupModel', {
		extend : 'Ext.data.Model',
		fields : [{
				name : 'dataCollectionGroupId',
				mapping : 'dataCollectionGroupId'
			}, {
				name : 'experimentType',
				mapping : 'experimentType',
				type: 'string'
			}, {
				name : 'startTime',
				mapping : 'startTime',
				type : 'date',
				dateFormat : 'd-m-Y H:i:s'
			}, {
				name : 'endTime',
				mapping : 'endTime',
				type : 'date',
				dateFormat : 'd-m-Y H:i:s'
			}, {
				name : 'crystalClass'
			}, {
				name : 'comments',
				mapping : 'comments'
			}, {
				name : 'detectorMode',
				mapping : 'detectorMode'
			}, {
				name : 'actualSampleBarcode',
				mapping : 'actualSampleBarcode'
			}, {
				name : 'actualSampleSlotInContainer',
				mapping : 'actualSampleSlotInContainer'
			}, {
				name : 'actualContainerBarcode',
				mapping : 'actualContainerBarcode'
			}, {
				name : 'actualContainerSlotInSC',
				mapping : 'actualContainerSlotInSC'
			}, {
				name : 'xtalSnapshotFileFullPath',
				mapping : 'xtalSnapshotFileFullPath'
			}, {
				name : 'blSampleVO',
				mapping : 'blSampleVO'
			}, {
				name : 'workflowVO',
				mapping : 'workflowVO'
			}, {
				name : 'dataCollectionVOs',
				mapping : 'dataCollectionVOs'
			}, {
				name : 'runNumber',
				mapping : 'runNumber'
			}, {
				name : 'sampleName',
				mapping : 'sampleName'
			}, {
				name : 'proteinAcronym',
				mapping : 'proteinAcronym'
			}, {
				name : 'imagePrefix',
				mapping : 'imagePrefix'
			}, 
			 {
				name : 'sampleNameProtein',
				mapping : 'sampleNameProtein'
			}, {
				name : 'groupExperimentType'
			}],
		idProperty : 'dataCollectionGroupId'
	});
}

// no filters
DataCollectionGroupGrid.prototype.getFilterTypes = function () {
	return [];
};

// sort by default
DataCollectionGroupGrid.prototype._sort = function (store, sampleNameHidden) {
	if (sampleNameHidden) {
		store.sort('startTime', 'DESC');
	} else {
		store.sort('startTime', 'ASC');
	}
};

// format data for crystal class
DataCollectionGroupGrid.prototype._setCrystalClass = function () {
	this.crystalClass = [];
	for (var i = 0; i < this.listOfCrystalClass.length; i++) {
		this.crystalClass.push(this.listOfCrystalClass[i].crystalClassName);
	}
};

// gets the grid
DataCollectionGroupGrid.prototype.getGrid = function (dataCollectionGroups) {
	this.features = dataCollectionGroups;
	return this.renderGrid(dataCollectionGroups);
};


// refresh data: reload data
DataCollectionGroupGrid.prototype.refresh = function (data) {
	this.features = data.dataCollectionGroupList;
	this.setGroupExperimentType();
	this.store.loadData(this.features, false);
};


//  no actions
DataCollectionGroupGrid.prototype.getActions = function () {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};

// set the experiment type, if it's a workflow, workflow type
DataCollectionGroupGrid.prototype.setGroupExperimentType = function () {
	var _this = this;
	for (var i = 0 ; i < _this.features.length ; i++) {
		if (_this.features[i].workflowVO){
			_this.features[i].groupExperimentType = _this.features[i].workflowVO.workflowType ;
		}else{
			_this.features[i].groupExperimentType = _this.features[i].experimentType;
		}
	}
};
	

// builds the grid
DataCollectionGroupGrid.prototype.renderGrid = function () {
	var _this = this;

	/** Prepare data * */
	var data = [];
	var sampleNameHidden = true;
	var defaultGroupField = null;
	// set the experiment type
	_this.setGroupExperimentType();
	// if some collects are linked to a sample, the column Sample is displayed, and group by this field
	for (var i = 0 ; i < _this.features.length ; i++) {
		if (_this.features[i].sampleName && _this.features[i].sampleName !== "") {
			sampleNameHidden = false;
			defaultGroupField = "sampleNameProtein";
			break;
		}
	}
	// get the columns
	var columns = this._getColumns(sampleNameHidden);
	//grid data
	this.store = Ext.create('Ext.data.Store', {
			model : 'DataCollectionGroupModel',
			autoload : false,
			groupField : defaultGroupField
		});

	
	//this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);
	this._sort(this.store, sampleNameHidden);
	// if no data
	var noGroupDataPanel = Ext.create('Ext.Panel', {
		id : 'noGroupDataPanel',
		layout : {
			type : 'fit', // Arrange child items vertically
			align : 'center', // Each takes up full width
			bodyPadding : 0,
			border : 0
		},
		html : '<html><h4>No Data Collections Groups have been found</h4></html>'
	});

	if (this.features.length == 0) {
		return noGroupDataPanel;
	}

	// this grid can be edited
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});

	// grouping grid
	var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
		groupHeaderTpl : '{columnName}: {name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
		hideGroupedHeader : true,
		startCollapsed : false,
		id : 'dataCollectionGroupGrouping'
	}), 
	groups = this.store.getGroups(),
	len = groups.length,
	//i = 0, 
	toggleGroup = function (
			item) {
		var groupName = item.text;
		if (item.checked) {
			groupingFeature.expand(groupName, true);
		} else {
			groupingFeature.collapse(groupName, true);
		}
	};

	// top bar with buttons
	var bar = [];
	if (this.canSave) {
		bar.push({
				text : 'Save Comments',
				tooltip : 'Save dataCollections Groups',
				icon : '../images/save.gif',
				handler : function () {
					_this.onSaveButtonClicked.notify({});
				}
			});
	}

	if (this.sessionId && this.sessionId != "null"){
		bar.push({
			text : 'View DataCollection for all groups',
			tooltip : 'View DataCollection for all groups',
			icon : '../images/magnif_16.png',
			handler : function () {
				document.location.href = contextPath + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForSession&sessionId=" + _this.sessionId;
			}
		});
				
	}
	bar.push('->');
	bar.push({
		text : 'Expand All',
		handler : function () {
			var rows = groupingFeature.view.getEl().query('.x-grid-group-body');

			Ext.each(rows, function (row) {
						groupingFeature.expand(Ext.get(row));
					});
		}
	}, {
		text : 'Collapse All',
		handler : function () {
			var rows = groupingFeature.view.getEl().query('.x-grid-group-body');

			Ext.each(rows, function (row) {
						groupingFeature.collapse(Ext.get(row));
					});
		}
	});
	
	bar.push({
			text : 'Clear Grouping',
			iconCls : 'icon-clear-group',
			handler : function () {
				groupingFeature.disable();
			}
		});
	
	// grid creation
	this.grid = Ext.create('Ext.grid.Panel', {
		style : {
			padding : 0
		},
		model : 'DataCollectionGroupModel',
		store : this.store,
		columns : columns,
		title : this.title,
		viewConfig : {
			stripeRows : true,
			enableTextSelection : true
		},
		selModel : {
			mode : 'SINGLE'
		},
		plugins : [cellEditing],
		features : [groupingFeature],
		tbar : bar
	});

	return this.grid;
};

// return the list of dataCollectionGroups as json
DataCollectionGroupGrid.prototype.getListDataCollectionGroup = function () {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};

// returns the sessionId
DataCollectionGroupGrid.prototype.getSessionId = function () {
	return this.sessionId;
};


// builds the columns
DataCollectionGroupGrid.prototype._getColumns = function (sampleNameHidden) {
	var _this = this;
	// render functions
	
	// render date
	function renderStartDate(value, p, record) {
		return Ext.Date.format(value, 'd-m-Y H:i:s');
	}


	// render sample position
	function renderSamplePosition(value, p, record) {
		var s = "";
		if (record.data.actualSampleSlotInContainer != null) { 
			s += record.data.actualSampleSlotInContainer;
		}
		if (record.data.actualContainerSlotInSC != null) {
			s += " (" + record.data.actualContainerSlotInSC + ")";
		}
		s += "<br />";
		if (record.data.actualSampleBarcode != null) { 
			s += record.data.actualSampleBarcode;
		}
		if (record.data.actualContainerBarcode != null) {
			s += " (" + record.data.actualContainerBarcode + ")";
		}
		return s;
	}

	// column wrap
	function columnWrap(value) {
		if (value) { 
			return '<div style="white-space:normal !important;word-wrap: break-word">' + value + '</div>';
		}
		else { 
			return "";
		}
	}

	// hide content
	function actionItemRenderer(value, meta, record, rowIx, ColIx, store) {
		if (record.get('dataCollectionVOs').length == 0) {
			return 'x-hide-display';
		}
	}

	// render nb data collections
	function renderNbDataCollections(value, p, record) {
		if (record.get('dataCollectionVOs') != null) { 
			return record.get('dataCollectionVOs').length;
		}
		else { 
			return 0;
		}
	}

	// render nb Images
	function renderNbImages(value, p, record) {
		if (record.get('dataCollectionVOs') !== null && record.get('dataCollectionVOs').length > 0) {
			return record.get('dataCollectionVOs')[0].numberOfImages;
		} else { 
			return 0;
		}
	}

	// render workflow column: with status ball
	function renderWorkflow(value, p, record) {
		if (record.data.workflowVO) {
			var mess = "";
			if (record.data.workflowVO.status) {
				mess = record.data.workflowVO.status;
			}
			var iconWorkflow = "../images/Sphere_White_12.png";
			if (mess) {
				if (mess.search("Error") != -1 || mess.search("Failure") != -1) {
					iconWorkflow = "../images/Sphere_Red_12.png";
				} else if (mess.search("Started") != -1 || mess.search("Launched") != -1) {
					iconWorkflow = "../images/Sphere_Orange_12.png";
				} else if (mess.search("Success") != -1) {
					iconWorkflow = "../images/Sphere_Green_12.png";
				}
			}
			var myToolTipText = " <img src='" + iconWorkflow + "' border=0\/>" +
					mess + "<br/>" + record.data.workflowVO.workflowTitle +
					"<br/>" + record.data.workflowVO.comments;
			p.tdAttr = 'data-qtip="' + myToolTipText + '"';
			return "<img src='" + iconWorkflow + "' border=0> <a href='" + contextPath + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewWorkflow.do?reqCode=display&workflowId=" + record.data.workflowVO.workflowId + "'>" + record.data.workflowVO.workflowType + "</a>";
		} else {
			return "";
		}
	}
	
	// render experiment type
	function renderExperimentType(value, p, record) {
		if (record.data.workflowVO) {
			var mess = "";
			if (record.data.workflowVO.status) {
				mess = record.data.workflowVO.status;
			}
			var iconWorkflow = "../images/Sphere_White_12.png";
			if (mess) {
				if (mess.search("Error") != -1 || mess.search("Failure") != -1) {
					iconWorkflow = "../images/Sphere_Red_12.png";
				} else if (mess.search("Started") != -1 || mess.search("Launched") != -1) {
					iconWorkflow = "../images/Sphere_Orange_12.png";
				} else if (mess.search("Success") != -1) {
					iconWorkflow = "../images/Sphere_Green_12.png";
				}
			}
			var com = "";
			if (record.data.workflowVO.comments){
				com = record.data.workflowVO.comments;
			}
			var myToolTipText = " <img src='" + iconWorkflow + "' border=0\/>" +
					mess + "<br/>" + record.data.workflowVO.workflowTitle +
					"<br/>" + com;
			p.tdAttr = 'data-qtip="' + myToolTipText + '"';
			return "<img src='" + iconWorkflow + "' border=0> <a href='" + contextPath + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewWorkflow.do?reqCode=display&workflowId=" + record.data.workflowVO.workflowId + "'>" + record.data.workflowVO.workflowType + "</a>";
		} else {
			return record.data.experimentType;
		}
	}

	// render crystal class
	function renderCrystalClass(value, p, record) {
		if (record.data.crystalClass != null) {
			var code = record.data.crystalClass;
			for (var i = 0; i < _this.listOfCrystalClass.length; i++) {
				var c = _this.listOfCrystalClass[i].crystalClassCode;
				if (c == code) {
					return _this.listOfCrystalClass[i].crystalClassName;
				}
				return code;
			}
		} else {
			return "";
		}
	}

	// create reusable renderer
	Ext.util.Format.comboRenderer = function (combo) {
		return function (value) {
			var record = combo.findRecord(combo.valueField, value);
			return record ? record.get(combo.displayField) : combo.valueNotFoundText;
		};
	};

	// create the combo instance

	var crystalClassStore = Ext.create('Ext.data.Store', {
			fields : ['crystalClassCode', 'crystalClassName'],
			data : _this.listOfCrystalClass
		});

	var combo = new Ext.form.ComboBox({
			mode : 'local',
			emptyText : '---',
			store : crystalClassStore,
			valueField : 'crystalClassCode',
			displayField : 'crystalClassName',
			allowBlank : true,
			typeAhead : true,
			triggerAction : 'all'
		});
	
	// columns
	var columns = [
		{
			text : 'Experiment<br/>Type',
			renderer : renderExperimentType,
			dataIndex:'groupExperimentType',
			flex : 0.10
		},
		{
			text : 'Protein<br/> Acronym',
			dataIndex : 'proteinAcronym',
			flex : 0.075
		}, {
			text : 'Image Prefix',
			dataIndex : 'imagePrefix',
			flex : 0.2
		},
		{
			text : 'Run No',
			dataIndex : 'runNumber',
			align: 'center',
			flex : 0.1
		},

		{
			text : 'Sample name',
			dataIndex : 'sampleName',
			hidden: sampleNameHidden,
			flex : 0.15
		},
		{
			text : 'Acronym - Sample name',
			dataIndex : 'sampleNameProtein',
			hidden: sampleNameHidden,
			flex : 0.15
		},
		{
			text : 'Sample position',
			dataIndex : 'actualSampleSlotInContainer',
			renderer : renderSamplePosition,
			align: 'center',
			type : 'string',
			flex : 0.15
		}, {
			text : 'Start Time',
			dataIndex : 'startTime',
			renderer : Ext.util.Format.dateRenderer('d-m-Y H:i:s'),
			flex : 0.15
		}];
	if (_this.isFx) {
		if (_this.canSave) {
			columns.push({
				text : 'Crystal class',
				dataIndex : 'crystalClass',
				flex : 0.2,
				editor : combo, // specify reference to combo instance
				renderer : Ext.util.Format.comboRenderer(combo)
					// pass combo instance to reusable renderer
			});
		} else {
			columns.push({
				text : 'Crystal class',
				dataIndex : 'crystalClass',
				flex : 0.2,
				renderer : Ext.util.Format.comboRenderer(combo)
					// pass combo instance to reusable renderer
			});
		}
	}
	if (_this.canSave) {
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				editor : {
					xtype : 'textfield',
					allowBlank : true
				},
				flex : 0.2,
				renderer : columnWrap
			});
	} else {
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				flex : 0.2,
				renderer : columnWrap
			});
	}
	columns.push({
			text : 'Nb<br/>Data<br/>Collections',
			dataIndex : 'dataCollectionVOs',
			type : 'int',
			align: 'center',
			renderer : renderNbDataCollections,
			flex : 0.05
		}, {
			text : 'Nb<br/>images',
			dataIndex : 'dataCollectionVOs',
			type : 'int',
			align: 'center',
			renderer : renderNbImages,
			flex : 0.04
		}

	);

	columns.push({
		xtype : 'actioncolumn',
		width : 70,
		header : 'View<br/>Collections',
		sortable : false,
		items : [{
			icon : '../images/magnif_16.png',
			tooltip : 'View Collections',
			getClass : actionItemRenderer,
			handler : function (grid, rowIndex, colIndex) {
				//console.log("View collect: "+rowIndex+" = "+_this.store.getAt(rowIndex).data.dataCollectionGroupId);
				_this.viewCollections(_this.store.getAt(rowIndex).data.dataCollectionGroupId);
			}
		}]

	});

	return columns;

};

// redirection to display view dataCollection for 1 dataCollectionGroupId 
DataCollectionGroupGrid.prototype.viewCollections = function (dataCollectionGroupId) {
	document.location.href = contextPath + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForDataCollectionGroup&dataCollectionGroupId=" + dataCollectionGroupId;
};


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
// chart for dehydration
function ExptCellChart() {
	var _this = this;
	this.store = null;
	this.chart = null;
	this.panel = null;
	this.width = 1000;
	this.height = 500;
}

// builds chart
ExptCellChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				theme : 'Category1',
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Numeric',
							position : 'left',
							fields : ['cellEdgeA', 'cellEdgeB', 'cellEdgeC'],
							minorTickSteps : 1,
							title : 'Unit cell axes (A)',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Numeric',
							position : 'right',
							fields : ['totalIntegratedSignal'],
							minorTickSteps : 1,
							title : 'Total integrated signal',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['relativeHumidity'],
							title : 'Relative Humidity (%)'

						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'relativeHumidity',
							yField : 'totalIntegratedSignal',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'relativeHumidity',
							yField : 'cellEdgeA',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'relativeHumidity',
							yField : 'cellEdgeB',
							markerConfig : {
								type : 'line',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'relativeHumidity',
							yField : 'cellEdgeC',
							markerConfig : {
								type : 'triangle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};


// builds and returns the main panel with the chart
ExptCellChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.store = data;
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtnL') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtnL',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	this.panel = Ext.create('Ext.Panel', {
				//width : "100%", // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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
// chart for dehydration
function ExptCellTimeChart() {
	var _this = this;
	this.store = null;
	this.chart = null;
	this.panel = null;
	this.width = 1000;
	this.height = 500;
}

// builds chart
ExptCellTimeChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				theme : 'Category1',
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Numeric',
							position : 'left',
							fields : ['relativeHumidity'],
							minorTickSteps : 1,
							title : 'Relative humidity (%)',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Numeric',
							position : 'right',
							fields : ['cellEdgeA', 'cellEdgeB', 'cellEdgeC'],
							minorTickSteps : 1,
							title : 'Unit cell edges (A)',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['time'],
							title : 'Time (seconds)'

						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'time',
							yField : 'relativeHumidity',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'time',
							yField : 'cellEdgeA',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'time',
							yField : 'cellEdgeB',
							markerConfig : {
								type : 'line',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'time',
							yField : 'cellEdgeC',
							markerConfig : {
								type : 'triangle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};


// builds and returns the main panel
ExptCellTimeChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.store = data;
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtnL') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtnL',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	this.panel = Ext.create('Ext.Panel', {
				//width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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
// chart for dehydration
function ExptLabelitChart() {
	var _this = this;
	this.store = null;
	this.chart = null;
	this.panel = null;
	this.width = 1000;
	this.height = 500;
}

// builds chart
ExptLabelitChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				theme : 'Category1',
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Numeric',
							position : 'left',
							fields : ['numberOfSpots', 'braggPeaks'],
							minorTickSteps : 1,
							title : 'Number of Bragg peaks or spots',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Numeric',
							position : 'right',
							fields : ['totalIntegratedSignal'],
							minorTickSteps : 1,
							title : 'Total integrated signal',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['relativeHumidity'],
							title : 'Relative Humidity (%)'

						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'relativeHumidity',
							yField : 'totalIntegratedSignal',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'relativeHumidity',
							yField : 'numberOfSpots',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'relativeHumidity',
							yField : 'braggPeaks',
							markerConfig : {
								type : 'line',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};


//builds and returns the main panel with the chart
ExptLabelitChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.store = data;
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtnL') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtnL',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	this.panel = Ext.create('Ext.Panel', {
				//width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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
// chart for dehydration
function ExptResChart() {
	var _this = this;
	this.store = null;
	this.chart = null;
	this.panel = null;
	this.width = 1000;
	this.height = 500;
}

// builds chart
ExptResChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				theme : 'Category1',
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Numeric',
							position : 'left',
							fields : ['rankingResolution', 'labelitR1',
									'labelitR2'],
							minorTickSteps : 1,
							title : 'Resolution (A)',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Numeric',
							position : 'right',
							fields : ['mosaicSpread'],
							minorTickSteps : 1,
							maximum : 0.3,
							title : 'Mosaic Spread',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['relativeHumidity'],
							title : 'Relative Humidity (%)'

						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'relativeHumidity',
							yField : 'rankingResolution',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'relativeHumidity',
							yField : 'mosaicSpread',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'relativeHumidity',
							yField : 'labelitR1',
							markerConfig : {
								type : 'line',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'relativeHumidity',
							yField : 'labelitR2',
							markerConfig : {
								type : 'triangle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};


// builds and returns the main panel
ExptResChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.store = data;
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtnL') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtnL',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	this.panel = Ext.create('Ext.Panel', {
				//width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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
// chart for dehydration
function ExptResolutionTimeChart() {
	var _this = this;
	this.store = null;
	this.chart = null;
	this.panel = null;
	this.width = 1000;
	this.height = 500;
}

//build chart
ExptResolutionTimeChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				theme : 'Category1',
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Numeric',
							position : 'left',
							fields : ['relativeHumidity'],
							minorTickSteps : 1,
							title : 'Relative humidity (%)',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Numeric',
							position : 'right',
							fields : ['rankingResolution', 'labelitR1',
									'labelitR2'],
							minorTickSteps : 1,
							title : 'Resolution (A)',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['time'],
							title : 'Time (seconds)'

						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'time',
							yField : 'relativeHumidity',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'time',
							yField : 'labelitR1',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'time',
							yField : 'labelitR2',
							markerConfig : {
								type : 'line',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'time',
							yField : 'rankingResolution',
							markerConfig : {
								type : 'triangle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};

// builds and returns the main panel with the chart
ExptResolutionTimeChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.store = data;
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtnL') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtnL',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	this.panel = Ext.create('Ext.Panel', {
				//width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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
// chart for dehydration
function ExptSpotsTimeChart() {
	var _this = this;
	this.store = null;
	this.chart = null;
	this.panel = null;
	this.width = 1000;
	this.height = 500;
}

// builds chart
ExptSpotsTimeChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				theme : 'Category1',
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Numeric',
							position : 'left',
							fields : ['relativeHumidity'],
							minorTickSteps : 1,
							title : 'Relative humidity (%)',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Numeric',
							position : 'right',
							fields : ['braggPeaks', 'numberOfSpots'],
							minorTickSteps : 1,
							title : 'Number of Bragg peaks or spots',
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['time'],
							title : 'Time (seconds)'

						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'time',
							yField : 'relativeHumidity',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'time',
							yField : 'braggPeaks',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'right',
							xField : 'time',
							yField : 'numberOfSpots',
							markerConfig : {
								type : 'line',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};

// builds and returns the main panel with the chart
ExptSpotsTimeChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.store = data;
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtnL') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtnL',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	this.panel = Ext.create('Ext.Panel', {
				//width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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
// Dehydration panel with charts
function DehydrationPanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 700;

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
DehydrationPanel.prototype.getPanel = function(data) {
	var _this = this;

	var dehydrationData = data.dehydrationValues;
	// the different grphs
	var exptCellTimeChart = new ExptCellTimeChart();
	var exptCellChart = new ExptCellChart();
	var exptLabelitChart = new ExptLabelitChart();
	var exptResChart = new ExptResChart();
	var exptResolutionTimeChart = new ExptResolutionTimeChart();
	var exptSpotsTimeChart = new ExptSpotsTimeChart();
	// format data for graphs
	var d = [];
	for (i = 0; i < dehydrationData.length; i++) {
		var s = {
			time : dehydrationData[i].time,
			relativeHumidity : dehydrationData[i].relativeHumidity,
			labelitR1 : dehydrationData[i].labelitR1,
			labelitR2 : dehydrationData[i].labelitR2,
			numberOfSpots : dehydrationData[i].numberOfSpots,
			braggPeaks : dehydrationData[i].braggPeaks,
			totalIntegratedSignal : dehydrationData[i].totalIntegratedSignal,
			cellEdgeA : dehydrationData[i].cellEdgeA,
			cellEdgeB : dehydrationData[i].cellEdgeB,
			cellEdgeC : dehydrationData[i].cellEdgeC,
			mosaicSpread : dehydrationData[i].mosaicSpread,
			rankingResolution : dehydrationData[i].rankingResolution
		};
		d.push(s);
	}

	// data for charts
	var store = Ext.create('Ext.data.JsonStore', {
				fields : ['time', 'relativeHumidity', 'rFactor', 'labelitR1',
						'labelitR2', 'numberOfSpots', 'braggPeaks',
						'totalIntegratedSignal', 'cellEdgeA', 'cellEdgeB',
						'cellEdgeC', 'mosaicSpread', 'rankingResolution'],
				data : d
			});
	// tabs with the different graphs
	var tabs = Ext.create('Ext.tab.Panel', {
				//width : this.width,
				//height : this.height,
				activeTab : 0,
				defaults : {
					bodyPadding : 0
				},
				items : [{
							tabConfig : {
								id : 'exptCellTimeChart',
								title : "Expt Cell Time"
							},
							items : [exptCellTimeChart.getPanel(store)]
						}, {
							tabConfig : {
								id : 'exptCellChart',
								title : "Expt Cell"
							},
							items : [exptCellChart.getPanel(store)]
						}, {
							tabConfig : {
								id : 'exptLabelitChart',
								title : "Expt labelit"
							},
							items : [exptLabelitChart.getPanel(store)]
						}, {
							tabConfig : {
								id : 'exptResChart',
								title : "Expt res"
							},
							items : [exptResChart.getPanel(store)]
						}, {
							tabConfig : {
								id : 'exptResolutionTimeChart',
								title : "Expt resolution time"
							},
							items : [exptResolutionTimeChart.getPanel(store)]
						}, {
							tabConfig : {
								id : 'exptSpotsTimeChart',
								title : "Expt spots time"
							},
							items : [exptSpotsTimeChart.getPanel(store)]
						}]
			});

	_this.panel = Ext.create('Ext.Panel', {
				//width : this.width, // panel's width
				//height : this.height,// panel's height
				layout : 'fit',
				items : [tabs]
			});

	return _this.panel;
};
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

//Energy Scan grid  in the dataCollectionGroup page
function EnergyScanGrid(args) {

	this.onSaveButtonClicked = new Event(this);

	var isFx = false;
	this.contextPath = "";
	this.title = "EnergyScan";
	this.listOfCrystalClass = [];
	this.canSave = false;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.title != null) {
			this.title = args.title;
		}
		if (args.listOfCrystalClass != null) {
			this.listOfCrystalClass = args.listOfCrystalClass;
		}
		if (args.sessionId != null) {
			this.sessionId = args.sessionId;
		}
		if (args.canSave != null) {
			this.canSave = args.canSave;
		}
	}

	// format crystal class data
	this._setCrystalClass();

	// Energy Scan data model
	Ext.define('EnergyScanModel', {
			extend : 'Ext.data.Model',
			fields : [{
					name : 'energyScanId',
					mapping : 'energyScanId'
				}, {
					name : 'fluorescenceDetector',
					mapping : 'fluorescenceDetector'
				}, {
					name : 'startTime',
					mapping : 'startTime',
					type : 'date',
					dateFormat : 'd-m-Y H:i:s'
				}, {
					name : 'endTime',
					mapping : 'endTime',
					type : 'date',
					dateFormat : 'd-m-Y H:i:s'
				}, {
					name : 'sampleName',
					mapping : 'sampleName'
				},  {
					name : 'crystalClass'
				}, {
					name : 'comments',
					mapping : 'comments'
				}, {
					name : 'scanFileFullPath',
					mapping : 'scanFileFullPath'
				}, {
					name : 'choochFileFullPath',
					mapping : 'choochFileFullPath'
				}, {
					name : 'jpegChoochFileFullPath',
					mapping : 'jpegChoochFileFullPath'
				}, {
					name : 'element',
					mapping : 'element'
				}, {
					name : 'startEnergy',
					mapping : 'startEnergy'
				}, {
					name : 'endEnergy',
					mapping : 'endEnergy'
				}, {
					name : 'transmissionFactor',
					mapping : 'transmissionFactor'
				}, {
					name : 'exposureTime',
					mapping : 'exposureTime'
				}, {
					name : 'synchrotronCurrent',
					mapping : 'synchrotronCurrent'
				}, {
					name : 'temperature',
					mapping : 'temperature'
				}, {
					name : 'peakEnergy',
					mapping : 'peakEnergy'
				}, {
					name : 'peakFPrime',
					mapping : 'peakFPrime'
				}, {
					name : 'peakFDoublePrime',
					mapping : 'peakFDoublePrime'
				}, {
					name : 'inflectionEnergy',
					mapping : 'inflectionEnergy'
				}, {
					name : 'inflectionFPrime',
					mapping : 'inflectionFPrime'
				}, {
					name : 'inflectionFDoublePrime',
					mapping : 'inflectionFDoublePrime'
				}, {
					name : 'xrayDose',
					mapping : 'xrayDose'
				}, {
					name : 'edgeEnergy',
					mapping : 'edgeEnergy'
				}, {
					name : 'filename',
					mapping : 'filename'
				}, {
					name : 'beamSizeVertical',
					mapping : 'beamSizeVertical'
				}, {
					name : 'beamSizeHorizontal',
					mapping : 'beamSizeHorizontal'
				}, {
					name : 'scanFileFullPathExists',
					mapping : 'scanFileFullPathExists'
				}, {
					name : 'shortFileName',
					mapping : 'shortFileName'
				}, {
					name : 'choochExists',
					mapping : 'choochExists'
				}, {
					name : 'jpegChoochFileFitPath',
					mapping : 'jpegChoochFileFitPath'
				}],
			idProperty : 'energyScanId'
		});
}


// no filters
EnergyScanGrid.prototype.getFilterTypes = function () {
	return [];
};

// sort by default by time
EnergyScanGrid.prototype._sort = function (store) {
	store.sort('startTime', 'DESC');
};

// prepare crystal class names
EnergyScanGrid.prototype._setCrystalClass = function () {
	this.crystalClass = [];
	for (var i = 0; i < this.listOfCrystalClass.length; i++) {
		this.crystalClass.push(this.listOfCrystalClass[i].crystalClassName);
	}
};

// get Grid
EnergyScanGrid.prototype.getGrid = function (energyScans) {
	this.features = energyScans;
	return this.renderGrid(energyScans);
};


// refresh data: upload
EnergyScanGrid.prototype.refresh = function (data) {
	this.features = data.energyScansList;
	this.store.loadData(this.features, false);
};

// no actions
EnergyScanGrid.prototype.getActions = function () {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};


// builds the grid
EnergyScanGrid.prototype.renderGrid = function () {
	var _this = this;

	/** Prepare data * */
	var data = [];
	// get columns
	var columns = this._getColumns();
	// data for grid
	this.store = Ext.create('Ext.data.Store', {
			model : 'EnergyScanModel',
			autoload : false
		});

	this._sort(this.store);
	this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);

	// if no data:
	var noEnergyScanDataPanel = Ext.create('Ext.Panel', {
			id : 'noEnergyScanDataPanel',
			layout : {
				type : 'fit'
			},
			html : '<html><h4>No Energy Scans have been found</h4></html>'
		});

	if (this.features.length == 0) { 
		return noEnergyScanDataPanel;
	}

	// this grid can be edited
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});

	// top bar buttons
	var bar = [];
	if (this.canSave) {
		bar.push({
				text : 'Save Comments',
				tooltip : 'Save energy scans',
				icon : '../images/save.gif',
				handler : function () {
					_this.onSaveButtonClicked.notify({});
				}
			});
	}
	// grid
	this.grid = Ext.create('Ext.grid.Panel', {
			style : {
				padding : 0
			},
			width : "100%",
			model : 'EnergyScanModel',
			height : "100%",
			store : this.store,
			columns : columns,
			title : this.title,
			viewConfig : {
				stripeRows : true,
				enableTextSelection : true
			},
			selModel : {
				mode : 'SINGLE'
			},
			plugins : [cellEditing],
			tbar : bar

		});

	return this.grid;
};


// returns the energy Scan list as json
EnergyScanGrid.prototype.getListEnergyScan = function () {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};

// returns the sessionId
EnergyScanGrid.prototype.getSessionId = function () {
	return this.sessionId;
};


// build columns
EnergyScanGrid.prototype._getColumns = function () {
	var _this = this;
	
	// render functions
	// render date
	function renderStartDate(value, p, record) {
		return Ext.Date.format(value, 'd-m-Y H:i:s');
	}

	// render column wrap
	function columnWrap(value) {
		if (value) { 
			return '<div style="white-space:normal !important;word-wrap: break-word;">' + value + '</div>';
		}
		else {
			return "";
		}
	}

	// render crystal class 
	function renderCrystalClass(value, p, record) {
		if (record.data.crystalClass != null) {
			var code = record.data.crystalClass;
			for (var i = 0; i < _this.listOfCrystalClass.length; i++) {
				var c = _this.listOfCrystalClass[i].crystalClassCode;
				if (c == code) {
					return _this.listOfCrystalClass[i].crystalClassName;
				}
				return code;
			}
		} else {
			return "";
		}
	}

	// create reusable renderer
	Ext.util.Format.comboRenderer = function (combo) {
		return function (value) {
			var record = combo.findRecord(combo.valueField, value);
			return record ? record.get(combo.displayField) : combo.valueNotFoundText;
		};
	};

	// format on 3 digits
	function renderNumberFormat3(value, p, record) {
		if (value === null) { 
			return "";
		}
		return Number(value).toFixed(3);
	}

	// format on 1digit
	function renderNumberFormat1(value, p, record) {
		if (value === null) { 
			return "";
		}
		return Number(value).toFixed(1);
	}

	// render energy scan file
	function renderScanFile(value, p, record) {
		if (!record.data.scanFileFullPath) {
			return "";
		} else {
			if (!record.data.scanFileFullPathExists) {
				return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Raw data file not found:<br>" + value + "</h4></div>";
			} else {
				return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
						"<a  href='viewDataCollection.do?reqCode=downloadFile&fullFilePath= " + 
						value + 
						"'  styleClass='LIST'>" + 
						record.data.shortFileName + "</a></div>";
			}
		}
	}

	// render image output
	function renderChoochOutput(value, p, record) {
		if (!record.data.jpegChoochFileFullPath) {
			return "";
		} else {
			if (!record.data.choochExists) {
				return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Chooch output not found:<br>" + value + "</h4></div>";
			} else {
				return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
						"<a  href='viewResults.do?reqCode=viewJpegImageFromFile&file=" + 
						record.data.jpegChoochFileFitPath + 
						"'  styleClass='LIST'>" + 
						"<img width='150' height='110' src='imageDownload.do?reqCode=getImageJpgFromFile&file=" + 
						record.data.jpegChoochFileFitPath + 
						"' border='0' alt='Click to zoom the image' >" + 
						"</a></div>";
			}
		}
	}

	// create the combo instance

	var crystalClassStore = Ext.create('Ext.data.Store', {
			id : 0,
			fields : ['crystalClassCode', 'crystalClassName'],
			data : _this.listOfCrystalClass
		});

	var combo = new Ext.form.ComboBox({
			mode : 'local',
			emptyText : '---',
			store : crystalClassStore,
			valueField : 'crystalClassCode',
			displayField : 'crystalClassName',
			allowBlank : true,
			typeAhead : true,
			triggerAction : 'all'

		});
	
	// build columns
	var columns = [{
			text : 'Element',
			dataIndex : 'element',
			flex : 0.05,
			id : 'element'
		}, {
			text : 'Sample name',
			dataIndex : 'sampleName',
			flex : 0.10
		},{
			text : 'Inflection<br>Energy<br>(keV)',
			dataIndex : 'inflectionEnergy',
			flex : 0.075,
			renderer : renderNumberFormat3,
			id : 'inflectionEnergy'
		}, {
			text : 'Exposure<br>Time<br>(s)',
			dataIndex : 'exposureTime',
			flex : 0.075,
			renderer : renderNumberFormat1,
			id : 'exposureTime'
		}, {
			text : 'Inflection <br>f\'<br>(e)',
			dataIndex : 'inflectionFPrime',
			flex : 0.075,
			renderer : renderNumberFormat1,
			id : 'inflectionFPrime'
		}, {
			text : 'Inflection <br>f\'\'<br>(e)',
			dataIndex : 'inflectionFDoublePrime',
			flex : 0.075,
			renderer : renderNumberFormat1,
			id : 'inflectionFDoublePrime'
		}, {
			text : 'Peak<br>Energy<br>(keV)',
			dataIndex : 'peakEnergy',
			flex : 0.05,
			renderer : renderNumberFormat3,
			id : 'peakEnergy'
		}, {
			text : 'Peak <br>f\'<br>(e)',
			dataIndex : 'peakFPrime',
			flex : 0.05,
			renderer : renderNumberFormat1,
			id : 'peakFPrime'
		}, {
			text : 'Peak <br>f\'\'<br>(e)',
			dataIndex : 'peakFDoublePrime',
			flex : 0.05,
			renderer : renderNumberFormat1,
			id : 'peakFDoublePrime'
		}, {
			text : 'Start Time',
			dataIndex : 'startTime',
			renderer : Ext.util.Format.dateRenderer('d-m-Y H:i:s'),
			flex : 0.15
		},

		{
			text : 'End Time',
			dataIndex : 'endTime',
			renderer : Ext.util.Format.dateRenderer('d-m-Y H:i:s'),
			flex : 0.15
		}, {
			text : 'Beam<br>size Hor.<br>(&#181m)',
			dataIndex : 'beamSizeHorizontal',
			flex : 0.05,
			id : 'beamSizeHorizontal'
		}, {
			text : 'Beam<br>size Ver.<br>(&#181m)',
			dataIndex : 'beamSizeVertical',
			flex : 0.05,
			id : 'beamSizeVertical'
		}, {
			text : 'Transm.<br>Factor<br>(%)',
			dataIndex : 'transmissionFactor',
			flex : 0.05,
			renderer : renderNumberFormat3,
			id : 'transmissionFactor'
		}, {
			text : 'Raw data File',
			dataIndex : 'scanFileFullPath',
			flex : 0.2,
			renderer : renderScanFile,
			id : 'scanFileFullPath'
		}, {
			text : 'Chooch Output',
			dataIndex : 'jpegChoochFileFullPath',
			flex : 0.2,
			renderer : renderChoochOutput,
			id : 'jpegChoochFileFullPath'
		}];
	if (_this.isFx) {
		columns.push({
			text : 'Crystal class',
			dataIndex : 'crystalClass',
			flex : 0.2,
			editor : combo, // specify reference to combo instance
			renderer : Ext.util.Format.comboRenderer(combo)
				// pass combo instance to reusable renderer
		});
	}
	if (this.canSave) {
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				editor : {
					xtype : 'textfield',
					allowBlank : true
				},
				flex : 0.2,
				renderer : columnWrap
			});
	} else {
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				flex : 0.2,
				renderer : columnWrap
			});
	}

	return columns;

};

EnergyScanGrid.prototype.getListEnergyScan = function () {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};

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
/*global Ext */


// Image Panel for the workflow: display by mesh or not
function ImagePanel(args) {
	var _this = this;
	this.width = "100%";
	this.height = 800;

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
ImagePanel.prototype.getPanel = function (snapshot, imageList, displayMesh, meshData) {
	var _this = this;
	// crystal snaphsot image
	var xtalSnapshotPanel = new XtalSnapshotPanel();
	xtalSnapshotPanel = xtalSnapshotPanel.getPanel(snapshot);

	// wall image
	var wallImagePanel = new WallImagePanel();
	wallImagePanel = wallImagePanel.getPanel(imageList);

	//if it's a mesh: special mesh wall
	if (displayMesh == 1) {
		wallImagePanel = new MeshWallImagePanel();
		wallImagePanel = wallImagePanel.getPanel(imageList, meshData);
	}

	
	var items = [];
	items.push(xtalSnapshotPanel);
	items.push(wallImagePanel);
	_this.panel = Ext.create('Ext.Panel', {
			// width:this.width, //panel's width
			// height:this.height,//panel's height
			layout : 'vbox',
			items : items
		});

	return _this.panel;
};
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
// Image Tab in the workflow page. This has been remmoved (temporarily?) because too much time consuming for big mesh
function ImageTabPanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 700;

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
ImageTabPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	if (data && data.listOfImages ) {
		var nb = data.listOfImages.length;
		for (var i = 0; i < nb; i++) {
			var imagePanel = new ImagePanel();
			var title = ""+i;
			if (data.listOfCollect && data.listOfCollect.length > i){
				collects = data.listOfCollect[i];
				if (collects.length > 0){
					title = collects[0].imagePrefix;
				}
			}
			var meshData = null;
			if (data.listOfMeshData && data.listOfMeshData.length > i)
				meshData = data.listOfMeshData[i];
			var snapshot = null;
			if (data.listOfSnapshot && data.listOfSnapshot.length > i)
				snapshot = data.listOfSnapshot[i];
			items.push({
						tabConfig : {
							title : title
						},
						items : [imagePanel.getPanel(snapshot, data.listOfImages[i], data.displayMesh, meshData)]
					});
		}
	}

	var tabs = Ext.create('Ext.tab.Panel', {
				layout : 'fit',
				activeTab : 0,
				defaults : {
					bodyPadding : 0
				},
				items : items
			});

	_this.panel = Ext.create('Ext.Panel', {
				layout : 'fit',
				items : [tabs]
			});

	return _this.panel;
};
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
/*global Ext:false */
/*global document:false */
/*global ImagePanel */
/*global WorkflowPanel */
/*global Collection */
/*global ErrorPanel */
/*global ResultPanel */
/*global ReportPanel */
/*global ReferencePanel */
/*global ParametersPanel */
/*global MeshScanPanel */
/*global DehydrationPanel */
/*global DataCollectionGrid */


Ext.Loader.setConfig({
	enabled : true
});

var dataCollectionsList;
var dataCollectionGrid;
var imagePanel;
var workflowPanel;
var meshScanPanel;
var dehydrationPanel;
var reportPanel;
var referencePanel = null;
var parametersPanel = null;
var reportPanel = null;
var resultPanel;
var parametersPanel;

// main entry point for the data Collection page
var IspybCollection = {
	start : function (targetId, mode)  {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showCollectionPage(targetId, mode);
	},

	showCollectionPage : function (targetId, mode) {
		var _this = this;
		// ajax calls
		var collection = new Collection();
		// listen for events
		collection.onSaved.attach(function (sender, newData) {
					_this.refreshDataCollection(newData);
				});

		collection.onRank.attach(function (sender, newData) {
			document.location.href = "/ispyb/user/sampleRanking.do?reqCode=display";
		});
		collection.onRankAutoProc.attach(function (sender, newData) {
			document.location.href = "/ispyb/user/autoProcRanking.do?reqCode=display";
		});

		dataCollectionsList = [];

		collection.collectionRetrieved.attach(function (evt, data) {
					if (data.errors && data.errors.length > 0) {
						var args = [];
						args.targetId = targetId;
						args.errors = data.errors;
						var errorPanel = new ErrorPanel(args);
						return;
					}
					// no errors
					dataCollectionsList = data.dataCollectionList;
					var listOfCrystalClass = data.listOfCrystalClass;
					data.pagingMode = false;
					if (dataCollectionsList.length > 500){
						data.pagingMode = true;
					}
					// grid
					dataCollectionGrid = new DataCollectionGrid(data);
					// listen for events
					dataCollectionGrid.onSaveButtonClicked.attach(function (
									sender, args) {
								var listDataCollection = dataCollectionGrid
										.getListDataCollection();
								collection
										.saveDataCollection(listDataCollection);
							});
					dataCollectionGrid.onRankButtonClicked.attach(function (
									sender, args) {
								var listDataCollectionRank = dataCollectionGrid
										.getListDataCollectionRank();
								collection.rank(listDataCollectionRank);

							});
					dataCollectionGrid.onRankAutoProcButtonClicked.attach(
							function (sender, args) {
								var listDataCollectionRank = dataCollectionGrid
										.getListDataCollectionRankAutoProc();
								collection.rankAutoProc(listDataCollectionRank);
							});

					var items = [];
					items.push({
							tabConfig : {
									title : "DataCollections"
								},
							items : [dataCollectionGrid
										.getGrid(dataCollectionsList)]
						});
//					if (data.displayImage == 1) {
//						imagePanel = new ImagePanel();
//						items.push({
//								tabConfig : {
//										title : "Images"
//									},
//								items : [imagePanel.getPanel(data.snapshot, data.imageList, data.displayMesh, data.meshData)]
//							});
//					}
					if (data.displayWorkflow == 1) {
						workflowPanel = new WorkflowPanel();
						items.push({
								tabConfig : {
									title : "Workflow"
								},
								items : [workflowPanel.getPanel(data)]
							});
					}
					if (data.displayMesh == 1) {
						meshScanPanel = new MeshScanPanel();
						items.push({
								tabConfig : {
										title : "Mesh"
									},
								items : [meshScanPanel.getPanel(data.meshData, data.snapshot, 0)]
							});
					}
					if (data.displayDehydration == 1) {
						dehydrationPanel = new DehydrationPanel();
						items.push({
								tabConfig : {
										title : "Dehydration"
									},
								items : [dehydrationPanel.getPanel(data)]
							});
					}
					if (data && data.workflowVO && data.workflowVO.resultFiles && data.workflowVO.resultFiles.length > 0) {
						resultPanel = new ResultPanel();
						items.push({
								tabConfig : {
										title : "Results"
									},
								items : [resultPanel.getPanel(data)]
							});
					}

					var tabs = Ext.create('Ext.tab.Panel', {
								// width: "100%",
							activeTab : 0,
							style : {
									marginTop : '10px'
								},
							defaults : {
									bodyPadding : 0,
									autoScroll : true
								},
							items : items
						});
				
					referencePanel = new ReferencePanel();
					parametersPanel = new ParametersPanel();
					reportPanel = new ReportPanel();
					
					// header panel build
					var headerPanelItems = [];
					headerPanelItems.push(reportPanel.getPanel(data));
					headerPanelItems.push(parametersPanel.getPanel(data));
					headerPanelItems.push(referencePanel.getPanel(data));
					
					var headerPanel = Ext.create('Ext.panel.Panel', {
						plain : true,
						frame : false,
						border : 0,
						collapsible : true,
						title: 'Data Collection info...',
						layout : {
							type : 'hbox'
						},
						style : {
							padding : 0
						},
						items : headerPanelItems

					});
					
					var mainItems = [];
					mainItems.push(headerPanel);
					mainItems.push(tabs);

					_this.panel = Ext.create('Ext.panel.Panel', {
							plain : true,
							frame : false,
							border : 0,
							renderTo : targetId,
							style : {
									padding : 0
								},
							items : mainItems

						});
					dataCollectionGrid.setRankValue();
					dataCollectionGrid._hideCol();
					dataCollectionGrid._sort();

				});
		if (mode == 1) {
			collection.getDataCollectionByDataCollectionGroup();
		} else if (mode == 0) {
			collection.getDataCollectionBySession();
		}
		
		if (mode == "session") {
			collection.getDataCollectionBySession();
		} else if (mode == "dataCollectionGroup") {
			collection.getDataCollectionByDataCollectionGroup();
		} else if (mode == "sample") {
			collection.getDataCollectionBySample();
		} else if (mode == "protein") {
			collection.getDataCollectionByProtein();
		} else if (mode == "customQuery") {
			collection.getDataCollectionByCustomQuery();
		} else if (mode == "sampleId") {
			collection.getDataCollectionBySampleId();
		}

	},

	refreshDataCollection : function (data) {
		var _this = this;
		dataCollectionGrid.refresh(data);
	}

};

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

Ext.Loader.setConfig({
			enabled : true
		});

var dataCollectionGroupsList;
var dataCollectionGroupGrid;
var xrfSpectraGrid;
var energyScanGrid;
var sessionForm;
var crystalClassGrid;
// main entry point for dataCollectionGroup page
var IspybCollectionGroup = {
	start : function(targetId, sessionId) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showCollectionGroup(targetId, sessionId);
	},

	showCollectionGroup : function(targetId, sessionId) {
		var _this = this;
		// ajax calls
		collectionGroup = new CollectionGroup();
		// listen for events
		collectionGroup.onSaved.attach(function(sender, newData) {
					_this.refreshDataCollectionGroup(newData);
					_this.refreshEnergyScan(newData);
					_this.refreshXRFSpectra(newData);
				});

		var items = [];
		dataCollectionGroupsList = [];
		xrfSpectraList = [];
		energyScanList = [];

		// data have been retrieved:
		collectionGroup.collectionGroupRetrieved.attach(function(evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			dataCollectionGroupsList = data.dataCollectionGroupList;
			xrfSpectraList = data.xrfSpectraList;
			listOfCrystalClass = data.listOfCrystalClass;
			energyScanList = data.energyScansList;
			dataCollectionGroupGrid = new DataCollectionGroupGrid(data);
			xrfSpectraGrid = new XRFSpectraGrid(data);
			energyScanGrid = new EnergyScanGrid(data);
			crystalClassGrid = new CrystalClassGrid(data);

			// listen for events
			dataCollectionGroupGrid.onSaveButtonClicked.attach(function(sender,
							args) {
						var listDataCollectionGroup = dataCollectionGroupGrid
								.getListDataCollectionGroup();
						var sessionId = dataCollectionGroupGrid.getSessionId();
						collectionGroup.saveDataCollectionGroup(
								listDataCollectionGroup, sessionId);
					});

			xrfSpectraGrid.onSaveButtonClicked.attach(function(sender, args) {
						var listXRFSpectra = xrfSpectraGrid.getListXRFSpectra();
						var sessionId = xrfSpectraGrid.getSessionId();
						collectionGroup.saveXRFSpectra(listXRFSpectra,
								sessionId);
					});

			energyScanGrid.onSaveButtonClicked.attach(function(sender, args) {
						var listEnergyScan = energyScanGrid.getListEnergyScan();
						var sessionId = energyScanGrid.getSessionId();
						collectionGroup.saveEnergyScan(listEnergyScan,
								sessionId);
					});

			// sessionForm for session panel
			sessionForm = new SessionForm(data);
			var panelSessionWidth = 400;
			var panelSessionHeight = 300;
			if (data.isFx) {
				panelSessionHeight = 420;
			}
			if (data.isIx) {
				panelSessionHeight = 370;
			}
			
			if (data && data.session){
				panelSession = Ext.widget({
					xtype : 'form',
					layout : 'fit',
					collapsible : true,
					id : 'sessionForm',
					frame : true,
					title : 'Session Information',
					bodyPadding : '5 5 0',
					fieldDefaults : {
						msgTarget : 'side',
						labelWidth : 90
					},
					style : {
						marginTop : '10px'
					},
					defaultType : 'textfield',
					items : [sessionForm.getPanel(data.session)],
					buttons : [{
						text : 'Save',
						handler : function() {
							var session = sessionForm.getSession();
							if (this.up('form').getForm().isValid()) {
								this.up('form').getForm().submit({
									url : 'viewDataCollectionGroup.do?reqCode=saveSession&session=' + JSON.stringify(sessionForm.getSession()),
									success : function(rp, o) {
										_this.refreshSession(data);
									}
								});
							}
						}
					}]
				});
			}
			var tabs = Ext.create('Ext.tab.Panel', {

						activeTab : 0,
						defaults : {
							bodyPadding : 0,
							autoScroll : true
						},
						style : {
							marginTop : '10px'
						},
						items : [{
							tabConfig : {
								id : 'dataCollectionGroup',
								title : "DataCollectionGroups"
							},
							items : [
									dataCollectionGroupGrid
											.getGrid(dataCollectionGroupsList),
									crystalClassGrid.getGrid(data.listCrystal)]
						}, {
							tabConfig : {
								id : 'energyScans',
								title : "EnergyScans"
							},
							items : [energyScanGrid.getGrid(energyScanList)]
						}, {
							tabConfig : {
								id : 'xrfspectra',
								title : "XRFSpectra"
							},
							items : [xrfSpectraGrid.getGrid(xrfSpectraList)]
						}]
					});

			// builds the header panel with References, Stats, reports
			referencePanel = new ReferencePanel();
			
			var sessionStatsPanel = new SessionStatsPanel();
			
			var reportSessionPanel = new ReportSessionPanel();
			
			var headerPanelItems = [];
			if (data && data.session){
				headerPanelItems.push(panelSession);
			}
			if (data && data.session){
				headerPanelItems.push(sessionStatsPanel.getPanel(data.session));
			}
			if (data && data.listOfReferences && data.listOfReferences.length > 0) {
				headerPanelItems.push(referencePanel.getPanel(data));
			}
			if (data && data.sessionHasMXpressOWF && data.sessionHasMXpressOWF == true) {
				headerPanelItems.push(reportSessionPanel.getPanel(data));
			}

			var headerPanel = Ext.create('Ext.panel.Panel', {
						plain : true,
						frame : false,
						border : 0,
						layout : {
							type : 'hbox'
							// pack: 'start'
							// , align: 'stretch'
						},
						style : {
							padding : 0
						},
						items : headerPanelItems

					});
			var mainItems = [];
			mainItems.push(headerPanel);
			mainItems.push(tabs);

			// main panel
			_this.panel = Ext.create('Ext.panel.Panel', {
						plain : true,
						frame : false,
						border : 0,
						layout : {
							type : 'vbox',
							align : 'stretch'
						},
						renderTo : targetId,
						items : mainItems

					});

		});

		collectionGroup.sessionRetrieved.attach(function(evt, data) {
					sessionForm.refresh(data.session);
				});
		if (sessionId && sessionId != "null"){
			collectionGroup.getCollectionGroupBySession();
		}else{
			collectionGroup.getCollectionGroupBySample();
		}
	},

	refreshDataCollectionGroup : function(newDataCollectionGroups) {
		var _this = this;
		dataCollectionGroupGrid.refresh(newDataCollectionGroups);
		crystalClassGrid.refresh(newDataCollectionGroups);
	},

	refreshXRFSpectra : function(newXRFSpectra) {
		var _this = this;
		xrfSpectraGrid.refresh(newXRFSpectra);
	},

	refreshEnergyScan : function(newEnergyScan) {
		var _this = this;
		energyScanGrid.refresh(newEnergyScan);
	},

	refreshSession : function() {
		var _this = this;
		var sessionId = dataCollectionGroupGrid.getSessionId();
		collectionGroup.getSessionInformation(sessionId);
	}
};

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

Ext.Loader.setConfig({
			enabled : true
		});

//var dataCollectionsList;
//var dataCollectionGrid;
var imagePanel;
var workflowPanel;
var meshScanPanel;
var dehydrationPanel;

var emptyPanel = null;
var referencePanel = null;
// main entry point for workflow in the old dataCollection page, no more used 
var IspybCollection2 = {
	start : function(targetId, mode, dataCollectionGroupId) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showCollectionPage(targetId, mode, dataCollectionGroupId);
	},

	showCollectionPage : function(targetId, mode, dataCollectionGroupId) {
		var _this = this;
		var collection = new Collection();

		collection.onSaved.attach(function(sender, newData) {
					_this.refreshDataCollection(newData);
				});

		collection.onRank.attach(function(sender, newData) {
			document.location.href = "/ispyb/user/sampleRanking.do?reqCode=display";
		});
		collection.onRankAutoProc.attach(function(sender, newData) {
			document.location.href = "/ispyb/user/autoProcRanking.do?reqCode=display";
		});

		//dataCollectionsList = new Array();

		collection.collectionRetrieved.attach(function(evt, data) {
					if (data.errors && data.errors.length > 0) {
						var args = [];
						args.targetId = targetId;
						args.errors = data.errors;
						var errorPanel = new ErrorPanel(args);
						return;
					}
					//dataCollectionsList = data.dataCollectionList;
					listOfCrystalClass = data.listOfCrystalClass;
					data.pagingMode = false;
					//dataCollectionGrid = new DataCollectionGrid(data);

					//dataCollectionGrid.onSaveButtonClicked.attach(function(
					//				sender, args) {
					//			var listDataCollection = dataCollectionGrid
					//					.getListDataCollection();
					//			collection
					//					.saveDataCollection(listDataCollection);
					//		});
					//dataCollectionGrid.onRankButtonClicked.attach(function(
					//				sender, args) {
					//			var listDataCollectionRank = dataCollectionGrid
					//					.getListDataCollectionRank();
					//			collection.rank(listDataCollectionRank);
//
					//		});
					/*dataCollectionGrid.onRankAutoProcButtonClicked.attach(
							function(sender, args) {
								var listDataCollectionRank = dataCollectionGrid
										.getListDataCollectionRankAutoProc();
								collection.rankAutoProc(listDataCollectionRank);
							});
*/
					var items = [];
//					items.push({
//								tabConfig : {
//									title : "DataCollections"
//								},
//								items : [dataCollectionGrid
//										.getGrid(dataCollectionsList)]
//							});
					
					if (data.displayWorkflow == 1) {
						workflowPanel = new WorkflowPanel();
						items.push({
									tabConfig : {
										title : "Workflow"
									},
									items : [workflowPanel.getPanel(data)]
								});
					}
//					if (data.displayImage == 1) {
//						imagePanel = new ImagePanel();
//						items.push({
//									tabConfig : {
//										title : "Images"
//									},
//									items : [imagePanel.getPanel(data.snapshot, data.imageList, data.displayMesh, data.meshData)]
//								});
//					}
					if (data.displayMesh == 1) {
						meshScanPanel = new MeshScanPanel();
						items.push({
									tabConfig : {
										title : "Mesh"
									},
									items : [meshScanPanel.getPanel(data.meshData, data.snapshot, 0)]
								});
					}
					if (data.displayDehydration == 1) {
						dehydrationPanel = new DehydrationPanel();
						items.push({
									tabConfig : {
										title : "Dehydration"
									},
									items : [dehydrationPanel.getPanel(data)]
								});
					}
					if (data && data.workflowVO && data.workflowVO.resultFiles && data.workflowVO.resultFiles.length > 0) {
						resultPanel = new ResultPanel();
						items.push({
									tabConfig : {
										title : "Results"
									},
									items : [resultPanel.getPanel(data)]
								});
					}
					if (items.length > 0){

						var tabs = Ext.create('Ext.tab.Panel', {
								width: "99%",
								activeTab : 0,
								defaults : {
									bodyPadding : 0,
									autoScroll : true
								},
								items : items
							});

						var mainItems = [];
					//referencePanel = new ReferencePanel();

					//mainItems.push(referencePanel.getPanel(data));

//					emptyPanel = Ext.create('Ext.Panel', {
//								id : 'emptyPanel',
//								border : false,
//								width : 600, // panel's width
//								height : 20
//							});

					//mainItems.push(emptyPanel);

						mainItems.push(tabs);

						_this.panel = Ext.create('Ext.panel.Panel', {
								plain : true,
								frame : false,
								width:"99%",
								border : 0,
								layout : 'fit',
								renderTo : targetId,
								style : {
									padding : 0
								},
								items : mainItems

							});
					}
					//dataCollectionGrid.setRankValue();
					//dataCollectionGrid._hideCol();
					//dataCollectionGrid._sort();

				});
		if (mode == "dataCollectionGroup") {
			collection.getDataCollectionByDataCollectionGroup(dataCollectionGroupId);
		} else if (mode == 0) {
			//collection.getDataCollectionBySession();
		}

	},

	refreshDataCollection : function(data) {
		var _this = this;
		//dataCollectionGrid.refresh(data);
	}

};

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
/*global Collection */
/*global SearchDataCollectionPanel */
/*global DataCollectionGrid */

Ext.Loader.setConfig({
	enabled : true
});

var dataCollectionList;
var dataCollectionGrid = null;
var searchDataCollectionPanel = null;
var emptyPanel = null;

var searchBeamline = "";
var startDate = "";
var endDate = "";
var startTime = "";
var endTime = "";

// main entry point for the last Collect in the manager view
var IspybLastCollect = {
	start : function (targetId) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showLastCollect(targetId);
	},

	showLastCollect : function (targetId) {
		var _this = this;
		// ajax calls
		var collection = new Collection();

		var items = [];
		dataCollectionList = [];
		// search criteria panel
		searchDataCollectionPanel = new SearchDataCollectionPanel();
		searchDataCollectionPanel.onSearchDataCollection.attach(function (evt,
						data) {
					var searchCriteria = searchDataCollectionPanel
							.getSearchCriteria();
					collection.getLastCollect(searchCriteria.beamline,
							searchCriteria.startDate, searchCriteria.endDate,
							searchCriteria.startTime, searchCriteria.endTime);
				});

		emptyPanel = Ext.create('Ext.Panel', {
			id : 'emptyPanel',
			border : false,
			bodyStyle : {
				"background-color" : "#99FFCC"
			},
			width : 600, // panel's width
			height : 20
		});
		// listen for events
		collection.collectionRetrieved.attach(function (evt, data) {
					dataCollectionList = data.dataCollectionList;
					listOfCrystalClass = data.listOfCrystalClass;

					if (_this.panel) {
						_this.refreshCollections(data);
					} else {
						items.push(searchDataCollectionPanel
								.getPanel(data.beamlines));
						searchDataCollectionPanel.setCriteria(searchBeamline,
								startDate, endDate, startTime, endTime);
						items.push(emptyPanel);
						if (data.dataCollectionList.length == 0) {
							items.push(_this.getNoCollectPanel());
						} else {
							data.pagingMode = true;
							// dataCollectionGrid = new DataCollectionGrid(data)
							// ;
							_this.createDataCollectionGrid(data);

							items.push(dataCollectionGrid
									.getGrid(dataCollectionList));
						}

						_this.panel = Ext.create('Ext.panel.Panel', {
								plain : true,
								frame : false,
								border : 0,
								bodyStyle : {
									"background-color" : "#99FFCC"
								},
								renderTo : targetId,
								style : {
									padding : 0
								},
								items : items

							});
					}
				});

		collection.criteriaRetrieved.attach(function (evt, data) {
					if (data) {
						searchBeamline = data.searchBeamline;
						startDate = data.startDate;
						endDate = data.endDate;
						startTime = data.startTime;
						endTime = data.endTime;

					}
					collection.getLastCollect(searchBeamline, startDate,
							endDate, startTime, endTime);
				});

		collection.getSearchCriteria();
	},

	getNoCollectPanel : function () {
		var noCollectDataPanel = Ext.create('Ext.Panel', {
			id : 'noCollectDataPanel',
			width : 600,
			height : 200,
			border : 0,
			bodyStyle : {
				"background-color" : "#99FFCC"
			},
			layout : {
				type : 'vbox', // Arrange child items vertically
				align : 'center', // Each takes up full width
				bodyPadding : 0,
				border : 0
			},
			html : '<html><h4>No Data Collections  have been found</h4></html>'
		});
		return noCollectDataPanel;

	},

	createDataCollectionGrid : function (data) {
		var _this = this;
		dataCollectionGrid = new DataCollectionGrid(data);
		dataCollectionGrid.onPagingMode.attach(function (evt, d2) {
					// console.log("paging"+(!dataCollectionGrid.getPagingMode()));
					data.pagingMode = !dataCollectionGrid.getPagingMode();
					_this.clearExtjsComponent();

					dataCollectionGrid.setArgs(data);
					_this.panel.add(dataCollectionGrid
							.getGrid(dataCollectionList));
					_this.panel.doLayout();
				});

	},

	clearExtjsComponent : function () {
		var _this = this;
		_this.panel.items.removeAt(2);
		// var f;
		// while(f = _this.panel.items.first()){
		// _this.panel.remove(f, true);
		// }
	},

	refreshCollections : function (data) {
		var _this = this;
		if (data.dataCollectionList.length == 0) {
			if (dataCollectionGrid != null) {
				this.clearExtjsComponent();
				dataCollectionGrid = null;
				_this.panel.add(_this.getNoCollectPanel());
				_this.panel.doLayout();
			}
		} else {
			this.clearExtjsComponent();
			data.pagingMode = true;
			_this.createDataCollectionGrid(data);
			_this.panel.add(dataCollectionGrid.getGrid(dataCollectionList));
			_this.panel.doLayout();
		}

	}

};

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
/*global ErrorPanel */
/*global CollectionGroup */
/*global SessionSummaryObject */
/*global  SessionViewGrid */
/*global  CrystalClassGrid*/
/*global  SessionForm*/
/*global  ReferencePanel*/
/*global  SessionStatsPanel*/
/*global  ReportSessionPanel*/

Ext.Loader.setConfig({
	enabled : true
});

var sessionForm;
var sessionViewGrid;
var reportSessionPanel;
var crystalClassGrid;
var collectionGroup;
var sessionSummary;

// main entry point for the session Report Page
var IspybSessionSummary = {
	start : function (targetId, sessionId, nbOfItems) {
		this.targetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showSessionSummary(targetId, sessionId, nbOfItems);
	},

	showSessionSummary : function (targetId, sessionId, nbOfItems) {
		var _this = this;
		// ajax calls
		collectionGroup = new CollectionGroup();
		sessionSummary = new SessionSummaryObject();

		// listen for events
		collectionGroup.onSaved.attach(function (sender, newData) {
			_this.refreshData(newData);
		});
		
		collectionGroup.imageSaved.attach(function (evt, data) {
			reportSessionPanel.exportSessionReport();
		});
		
		collectionGroup.sessionRetrieved.attach(function (evt, data) {
			sessionForm.refresh(data.session);
		});

		var items = [];

		sessionSummary.sessionSummaryRetrieved.attach(function (evt, data) {
			// errors in retrieving data
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			// no errors=> list of sessionDataInfo objects
			var listSessionDataObjectInformation = data.listSessionDataObjectInformation;
			// session summuray grid
			sessionViewGrid = new SessionViewGrid(data);
			// listen for events
			sessionViewGrid.onExportReportWithGraphSelected.attach(function (sender, args) {
				var listSvg = args.listSvg;
				var sessionId = args.sessionId;
				var listId = args.listId;
				collectionGroup.saveImageForExport(listSvg, sessionId, listId);
			});
			
			sessionViewGrid.onSaveButtonClicked.attach(function (sender, args) {
				var listData = sessionViewGrid.getListData();
				var sessionId = sessionViewGrid.getSessionId();
				var nbOfItems = sessionViewGrid.getNbOfItems();
				sessionSummary.saveData(listData, sessionId, nbOfItems);
			});
						
			sessionViewGrid.onPageChanged.attach(function (sender, args) {
				sessionSummary.changeSessionReportPageNumber(args.sessionReportCurrentPageNumber, args.sessionId);
			});

			// crystal class summury grid
			crystalClassGrid = new CrystalClassGrid(data);

			// session form for the session panel
			sessionForm = new SessionForm(data);
			var panelSessionWidth = 400;
			var panelSessionHeight = 300;
			if (data.isFx) {
				panelSessionHeight = 420;
			}
			if (data.isIx) {
				panelSessionHeight = 370;
			}
			var panelSession;
			
			// session panel information
			if (data && data.session) {
				panelSession = Ext.widget({
					xtype : 'form',
					layout : 'fit',
					collapsible : true,
					id : 'sessionForm',
					frame : true,
					title : 'Session Information',
					bodyPadding : '5 5 0',
					fieldDefaults : {
						msgTarget : 'side',
						labelWidth : 90
					},
					style : {
						marginTop : '10px'
					},
					defaultType : 'textfield',
					items : [sessionForm.getPanel(data.session)],
					buttons : [{
						text : 'Save',
						handler : function () {
							var session = sessionForm.getSession();
							if (this.up('form').getForm().isValid()) {
								this.up('form').getForm().submit({
									url : 'viewDataCollectionGroup.do?reqCode=saveSession&session=' + JSON.stringify(sessionForm.getSession()),
									success : function (rp, o) {
										_this.refreshSession(data);
									}
								});
							}
						}
					}]
				});
			}
			
			// reference panel
			var referencePanel = new ReferencePanel();
			
			// session stats panel
			var sessionStatsPanel = new SessionStatsPanel();
			// report panel
			var reportSessionPanel = new ReportSessionPanel();
			reportSessionPanel.onExportReportSelected.attach(function (sender, args) {
				sessionViewGrid.saveGraphAsImg();
			});
			
			// header panel with Session info, reference, stats and reports
			var headerPanelItems = [];
			if (data && data.session) {
				headerPanelItems.push(panelSession);
			}
			if (data && data.session) {
				headerPanelItems.push(sessionStatsPanel.getPanel(data.session));
			}
			if (data && data.listOfReferences && data.listOfReferences.length > 0) {
				headerPanelItems.push(referencePanel.getPanel(data));
			}
			if (data) {
				headerPanelItems.push(reportSessionPanel.getPanel(data));
			}

			var headerPanel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				border : 0,
				width : '100%',
				layout : {
					type : 'hbox'
						// pack: 'start'
						// , align: 'stretch'
				},
				style : {
					padding : 0
				},
				items : headerPanelItems

			});
			// in the main panel: header, grid and crytsalGrid
			var mainItems = [];
			mainItems.push(headerPanel);
			mainItems.push(sessionViewGrid.getGrid(listSessionDataObjectInformation));
			mainItems.push(crystalClassGrid.getGrid(data.listCrystal));

			_this.panel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				border : 0,
				width : '100%',
				layout : {
					type : 'vbox',
					align : 'stretch'
				},
				renderTo : targetId,
				items : mainItems

			});
		});

		// retrieve information for session
		if (sessionId && sessionId != "null") {
			sessionSummary.getSessionSummary(nbOfItems);
		}
		
	},
	
	refreshSession : function () {
		var _this = this;
		var sessionId = sessionViewGrid.getSessionId();
		collectionGroup.getSessionInformation(sessionId);
	},

	refreshData : function (newData) {
		var _this = this;
		sessionViewGrid.refresh(newData);
		crystalClassGrid.refresh(newData);
	}
};

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

Ext.Loader.setConfig({
			enabled : true
		});

var workflowPanel;
var meshScanPanel;
var dehydrationPanel;

var emptyPanel = null;
var referencePanel = null;
// main entry point for the workflow page
var IspybWorkflow = {
	start : function(targetId, workflowId) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showWorkflowPage(targetId, workflowId);
	},

	showWorkflowPage : function(targetId,  workflowId) {
		var _this = this;
		// ajax call
		collection = new Collection();

		// listen events
		collection.onSaved.attach(function(sender, newData) {
			_this.refreshDataCollection(newData);
		});

		collection.onRank.attach(function(sender, newData) {
			document.location.href = "/ispyb/user/sampleRanking.do?reqCode=display";
		});
		collection.onRankAutoProc.attach(function(sender, newData) {
			document.location.href = "/ispyb/user/autoProcRanking.do?reqCode=display";
		});

		dataCollectionsList = [];

		collection.workflowRetrieved.attach(function(evt, data) {
					// erros in retrieving data
					if (data.errors && data.errors.length > 0) {
						var args = [];
						args.targetId = targetId;
						args.errors = data.errors;
						var errorPanel = new ErrorPanel(args);
						return;
					}
					// no errors, collects in workflow:
					dataCollectionsList = [];
					for (var i=0; i<data.listOfCollect.length; i++){
						collects = data.listOfCollect[i];
						for (var j=0; j<collects.length; j++){
							dataCollectionsList.push(collects[j]);
						}
					}
					listOfCrystalClass = data.listOfCrystalClass;
					data.pagingMode = false;
					//data.groupFieldDefault = null;
					// dataCollection Grid
					dataCollectionGrid = new DataCollectionGrid(data);
					// listen for events
					dataCollectionGrid.onSaveButtonClicked.attach(function(
									sender, args) {
								var listDataCollection = dataCollectionGrid
										.getListDataCollection();
								collection
										.saveDataCollection(listDataCollection);
							});
					dataCollectionGrid.onRankButtonClicked.attach(function(
									sender, args) {
								var listDataCollectionRank = dataCollectionGrid
										.getListDataCollectionRank();
								collection.rank(listDataCollectionRank);

							});
					dataCollectionGrid.onRankAutoProcButtonClicked.attach(
							function(sender, args) {
								var listDataCollectionRank = dataCollectionGrid
										.getListDataCollectionRankAutoProc();
								collection.rankAutoProc(listDataCollectionRank);
							});

					var items = [];
					//tabs workflow: image has been removed (too much image, too much time to display)
					items.push({
								tabConfig : {
									title : "DataCollections"
								},
								items : [dataCollectionGrid
										.getGrid(dataCollectionsList)]
							});
//					if (data.displayImage == 1) {
//						if (data.listOfImages){
//							var nb  = data.listOfImages.length;
//							if (nb > 1 ){
//								var imageTabPanel = new ImageTabPanel();
//								items.push({
//									tabConfig : {
//										title : "Images"
//									},
//									items : [imageTabPanel.getPanel(data)]
//								});
//							}else if (nb == 1){
//								var imagePanel = new ImagePanel();
//								var meshData = null;
//								if (data.listOfMeshData && data.listOfMeshData.length > 0)
//									meshData = data.listOfMeshData[0];
//								var snapshot = null;
//								if (data.listOfSnapshot && data.listOfSnapshot.length > 0)
//									snapshot = data.listOfSnapshot[0];
//								items.push({
//											tabConfig : {
//												title : "Images"
//											},
//											items : [imagePanel.getPanel(snapshot, data.listOfImages[0], data.displayMesh, meshData)]
//										});
//							}
//						}
//						
//					}
					if (data.displayWorkflow == 1) {
						workflowPanel = new WorkflowPanel();
						items.push({
									tabConfig : {
										title : "Workflow"
									},
									items : [workflowPanel.getPanel(data)]
								});
					}
					if (data.displayMesh == 1) {
						var nb  = data.listOfMeshData.length;
						if (nb > 1 ){
							var meshTabPanel = new MeshTabPanel();
							items.push({
								tabConfig : {
									title : "Mesh"
								},
								items : [meshTabPanel.getPanel(data)]
							});
						}else if (nb == 1){
							var meshScanPanel = new MeshScanPanel();
							var meshData =data.listOfMeshData[0];
							var snapshot = null;
							if (data.listOfSnapshot && data.listOfSnapshot.length > 0)
								snapshot = data.listOfSnapshot[0];
							items.push({
										tabConfig : {
											title : "Mesh"
										},
										items : [meshScanPanel.getPanel(meshData,snapshot, 0)]
									});
						}
						
						
					}
					if (data.displayDehydration == 1) {
						dehydrationPanel = new DehydrationPanel();
						items.push({
									tabConfig : {
										title : "Dehydration"
									},
									items : [dehydrationPanel.getPanel(data)]
								});
					}
					if (data && data.workflowVO && data.workflowVO.resultFiles && data.workflowVO.resultFiles.length > 0) {
						resultPanel = new ResultPanel();
						items.push({
									tabConfig : {
										title : "Results"
									},
									items : [resultPanel.getPanel(data)]
								});
					}
					if (items.length > 0){

						var tabs = Ext.create('Ext.tab.Panel', {
								width: "99%",
								activeTab : 0,
								defaults : {
									bodyPadding : 0,
									autoScroll : true
								},
								items : items
							});

						var mainItems = [];
					

						mainItems.push(tabs);

						_this.panel = Ext.create('Ext.panel.Panel', {
								plain : true,
								frame : false,
								width:"99%",
								border : 0,
								layout : 'fit',
								renderTo : targetId,
								style : {
									padding : 0
								},
								items : mainItems

							});
					}
					dataCollectionGrid.setRankValue();
					dataCollectionGrid._hideCol();
					dataCollectionGrid._sort();

				});
		// retrieve data for the workflow
		collection.getWorkflow(workflowId);
		

	},

	refreshDataCollection : function(data) {
		var _this = this;
		dataCollectionGrid.refresh(data);
	}

};

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
// takes care of the color range 
function ColorRangeManager(args) {
	var _this = this;
	// --------------------------------
	// constants
	// --------------------------------

	this.redFrequency = 0.1;
	this.grnFrequency = 0.2;
	this.bluFrequency = 0.3;
	this.center = 128;
	this.width = 127;
	this.phase1 = 0;
	this.phase2 = 0;
	this.phase3 = 0;

	this.len = 20;

	this.reverseOrder =false;
	// --------------------------------
	// Private Attributes
	// --------------------------------

	this._emptyDataColor = "#E8DFE8";

	// other
	this._dataRange = {
		min : null,
		max : null
	};
	this._debug = false;
	this._colorRange = null;

	// calculated
	this._dataStep = null;
	this._maxDataSpace = null;

	if (args != null) {

		if (args.dataRange != null) {
			this._dataRange = args.dataRange;
		}
	}

	// setup color space
	this._colorRange = [];
	this._setupColorRange();
}

ColorRangeManager.prototype.loadData = function(dataRange, reverseOrder) {
	this._dataRange = dataRange;
	this._setupColorRange();
	this.reverseOrder = reverseOrder;
};

ColorRangeManager.prototype.getMaxValue = function() {
	return this._dataRange.max;
};


ColorRangeManager.prototype.getMinValue = function() {
	return this._dataRange.min;
};

// when given an RBGA object it returns a canvas-formatted string for that color
// if the RGBA is empty or ill-defined it returns a string for the empty data
// color
ColorRangeManager.prototype.getCellColorString = function(dataValue) {
	var colorValue = this.getCellColorRGBA(dataValue);
	return colorValue;
};

// returns an RBGA object with the color for the given dataValue
ColorRangeManager.prototype.getCellColorRGBA = function(dataValue) {
	if (dataValue == null) {
		return this._emptyDataColor;
	}
	var maxColors = this.getLen();

	var dataBin = dataValue / this._dataStep;
	var binOffset = this._dataRange.min / this._dataStep;
	var newDataBin = (dataBin - binOffset);
	newDataBin = dataValue * this._colorRange.length / this._dataRange.max;
	// round
	if (newDataBin < 0)
		newDataBin = Math.ceil(newDataBin);
	else
		newDataBin = Math.floor(newDataBin);

	this._log('value: ' + dataValue + ' bin: ' + dataBin + ' new bin: ' + newDataBin + ' / nbCol=' + this._colorRange.length + ' & max= ' + this._dataRange.max);
	if (this.reverseOrder){
		newDataBin = this._dataRange.max/ this._dataStep - newDataBin;
	}
	// assure bounds
	if (newDataBin < 0)
		newDataBin = 0;
	if (newDataBin >= this._colorRange.length)
		newDataBin = (this._colorRange.length) - 1;

	return this._colorRange[newDataBin];
};

// --------------------------------
// Private Methods
// --------------------------------

// maps data ranges to colors
ColorRangeManager.prototype._setupColorRange = function() {
	var dataRange = this._dataRange;
	var maxColors = this.getLen();
	this._colorRange = [];
	this._addColorsToRange();

	// if (maxColors > 256)
	// maxColors = 256;
	// if (maxColors < 1) {
	// maxColors = 1;
	// }
	// calc data step
	this._maxDataSpace = Math.abs(dataRange.min) + Math.abs(dataRange.max);
	this._dataStep = this._maxDataSpace / maxColors;

	this._log('dataStep: ' + this._dataStep);
};

// append colors to the end of the color Range, splitting the number of colors
// up evenly
ColorRangeManager.prototype._addColorsToRange = function() {
	var len = this.getLen();
	for (var i = 0; i < len; ++i) {
		var red = Math.sin(this.redFrequency * i + this.phase1) * this.width + this.center;
		var grn = Math.sin(this.grnFrequency * i + this.phase2) * this.width + this.center;
		var blu = Math.sin(this.bluFrequency * i + this.phase3) * this.width + this.center;
		this._colorRange[this._colorRange.length] = this._RGB2Color(red, grn, blu);
	}

};

ColorRangeManager.prototype._RGB2Color = function(r, g, b) {
	return '#' + this._byte2Hex(r) + this._byte2Hex(g) + this._byte2Hex(b);
};

ColorRangeManager.prototype._byte2Hex = function(n) {
	var nybHexString = "0123456789ABCDEF";
	return String(nybHexString.substr((n >> 4) & 0x0F, 1)) + nybHexString.substr(n & 0x0F, 1);
};

ColorRangeManager.prototype.getLen = function() {
	return this.len;
};

ColorRangeManager.prototype.createColorGradient = function(targetId, maxValue) {
	var len = this.getLen();
	document.getElementById(targetId).innerHTML = "0 ";
	for (var i = 0; i < len; ++i) {
		var red = Math.sin(this.redFrequency * i + this.phase1) * this.width + this.center;
		var grn = Math.sin(this.grnFrequency * i + this.phase2) * this.width + this.center;
		var blu = Math.sin(this.bluFrequency * i + this.phase3) * this.width + this.center;
		document.getElementById(targetId).innerHTML += '<font color="' + this._RGB2Color(red, grn, blu) + '">&#9608;</font>';
	}
	document.getElementById(targetId).innerHTML += maxValue;
};

ColorRangeManager.prototype.getColorRange = function(i) {
	i = i-this._dataRange.min/ this._dataStep ;
	if (this.reverseOrder){
		i = Math.floor(this._dataRange.max/ this._dataStep - i);
	}
	var red = Math.sin(this.redFrequency * i + this.phase1) * this.width + this.center;
	var grn = Math.sin(this.grnFrequency * i + this.phase2) * this.width + this.center;
	var blu = Math.sin(this.bluFrequency * i + this.phase3) * this.width + this.center;
	return this._RGB2Color(red, grn, blu);
};

ColorRangeManager.prototype._log = function(message) {
	if (this._debug) {
		console.log(message);
	}
};

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
// panel with snaphsot (linked to dataCollection Group) and grid position
function DetailsPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "200";

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
DetailsPanel.prototype.getPanel = function() {
	var _this = this;

	// position Grid
	var positionGrid = new PositionGrid();
	positionGrid = positionGrid.getGrid();

	// crystal snapshot panel
	var xtalSnapshotPanel = new XtalSnapshotPanel();
	xtalSnapshotPanel = xtalSnapshotPanel.getPanel();

	// main panel
	_this.panel = Ext.create('Ext.Panel', {
				width : 800, // panel's width
				height : 200,// panel's height
				layout : 'border',
				items : [{
							region : 'center',
							split : true,
							collapsible : false,
							floatable : false,
							items : [positionGrid]
						}, {
							region : 'east',
							width : 200,
							height : 200,
							split : true,
							collapsible : false,
							floatable : false,
							items : [xtalSnapshotPanel]
						}]
			});

	return _this.panel;
};
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
var SVG = {
	svgns : 'http://www.w3.org/2000/svg',
	xlinkns : "http://www.w3.org/1999/xlink",

	createSVGCanvas : function(parentNode, attributes) {

		attributes.push(['xmlns', SVG.svgns], ['xmlns:xlink',
						'http://www.w3.org/1999/xlink']);
		var svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
		this._setProperties(svg, attributes);
		parentNode.appendChild(svg);
		return svg;

	},

	createRectangle : function(x, y, width, height, attributes) {
		var rect = document.createElementNS(this.svgns, "rect");
		rect.setAttribute("x", x);
		rect.setAttribute("y", y);
		rect.setAttribute("width", width);
		rect.setAttribute("height", height);
		SVG._setProperties(rect, attributes);
		return rect;
	},

	drawImage64 : function(x, y, width, height, base64, svgNode, attributes) {
		var node = SVG.createImage64(x, y, width, height, base64, attributes);
		svgNode.appendChild(node);
		return node;
	},

	createImage64 : function(x, y, width, height, base64, attributes) {
		var img = document.createElementNS(this.svgns, "image");
		img.setAttribute("x", x);
		img.setAttribute("y", y);
		img.setAttribute("width", width);
		img.setAttribute("height", height);
		img.setAttribute("xlink:href", base64);
		SVG._setProperties(img, attributes);
		return img;
	},

	createLine : function(x1, y1, x2, y2, attributes) {
		var line = document.createElementNS(this.svgns, "line");
		line.setAttribute("x1", x1);
		line.setAttribute("y1", y1);
		line.setAttribute("x2", x2);
		line.setAttribute("y2", y2);
		SVG._setProperties(line, attributes);
		return line;
	},

	createClip : function(id, nodeToClip, attributes) {
		var clip = document.createElementNS(this.svgns, "clipPath");
		clip.setAttribute("id", id);
		clip.appendChild(nodeToClip);
		return clip;
	},

	drawClip : function(id, nodeToClip, svgNode) {
		var node = SVG.createClip(id, nodeToClip);
		svgNode.appendChild(node);
		return node;
	},

	drawRectangle : function(cx, cy, width, height, svgNode, attributes) {
		var node = null;
		try {
			node = SVG.createRectangle(cx, cy, width, height, attributes);
			svgNode.appendChild(node);
		} catch (e) {

			console.log("-------------------- ");
			console.log("Error on drawRectangle " + e);
			console.log(attributes);
			console.log("-------------------- ");
		}
		return node;
	},

	createEllipse : function(x, y, rx, ry, attributes) {
		var rect = document.createElementNS(this.svgns, "ellipse");
		rect.setAttribute("cx", x);
		rect.setAttribute("cy", y);
		rect.setAttribute("rx", rx);
		rect.setAttribute("ry", ry);
		SVG._setProperties(rect, attributes);
		return rect;
	},

	drawEllipse : function(cx, cy, rx, ry, svgNode, attributes) {
		var node = SVG.createEllipse(cx, cy, rx, ry, attributes);
		svgNode.appendChild(node);
		return node;
	},

	drawImage : function(x, y, canvasSVG, attributes) {
		var image = document.createElementNS(this.svgns, "image");
		image.setAttribute("x", x);
		image.setAttribute("y", y);
		canvasSVG.appendChild(image);
		SVG._setProperties(image, attributes);
	},

	drawLine : function(x1, y1, x2, y2, nodeSVG, attributes) {
		var line = null;
		try {
			line = SVG.createLine(x1, y1, x2, y2, attributes);
			nodeSVG.appendChild(line);
		} catch (e) {
			// debugger
		}
		return line;
	},

	drawPath : function(d, nodeSVG, attributes) {
		var path = SVG.createPath(d, attributes);
		nodeSVG.appendChild(path);
		return path;
	},

	createPoligon : function(points, attributes) {
		var poligon = document.createElementNS(this.svgns, "polygon");
		poligon.setAttribute("points", points);
		SVG._setProperties(poligon, attributes);
		return poligon;
	},

	drawPoligon : function(points, canvasSVG, attributes) {
		var poligon = SVG.createPoligon(points, attributes);
		canvasSVG.appendChild(poligon);
		return poligon;
	},
	// <polygon points="20,420, 300,420 160,20" />

	createPath : function(d, attributes) {
		var path = document.createElementNS(this.svgns, "path");
		path.setAttribute("d", d);
		SVG._setProperties(path, attributes);
		return path;
	},

	drawCircle : function(x, y, radio, canvasSVG, attributes) {

		var newText = document.createElementNS(this.svgns, "circle");
		newText.setAttribute("cx", x);
		newText.setAttribute("cy", y);
		newText.setAttribute("r", radio);

		canvasSVG.appendChild(newText);
		this._setProperties(newText, attributes);
		return newText;
	},

	_setProperties : function(node, attributes) {
		if (attributes instanceof Array) {
			for (var i = 0; i < attributes.length; i++) {
				node.setAttribute(attributes[i][0], attributes[i][1]);
			}
		} else {
			for (var key in attributes) {
				node.setAttribute(key, attributes[key]);
			}
		}
	},

	/*
	 * drawPath: function(pointsArray, canvasSVG, attributes){ var path =
	 * document.createElementNS(this.svgns,"polyline"); path.setAttribute ('id',
	 * id);
	 * 
	 * var d= pointsArray[0].x+ " "+ pointsArray[0].y; for (var i=1; i<
	 * pointsArray.length; i++) { d=d+" "+pointsArray[i].x+" "+pointsArray[i].y; }
	 * path.setAttribute ('points', d); canvasSVG.appendChild(path); },
	 */

	createText : function(x, y, text, attributes) {
		var node = document.createElementNS(this.svgns, "text");
		node.setAttributeNS(null, "x", x);
		node.setAttributeNS(null, "y", y);

		var textNode = document.createTextNode(text);
		node.appendChild(textNode);

		this._setProperties(node, attributes);
		return node;
	},

	drawText : function(x, y, text, canvasSVG, attributes) {
		var textC = SVG.createText(x, y, text, attributes);
		canvasSVG.appendChild(textC);
		return textC;
	},

	drawGroup : function(svgNode, attributes) {
		var group = SVG.createGroup(attributes);
		svgNode.appendChild(group);
		return group;
	},

	createGroup : function(attributes) {
		var group = document.createElementNS(this.svgns, "g");
		this._setProperties(group, attributes);
		return group;
	}

};

var CanvasToSVG = {

	convert : function(sourceCanvas, targetSVG, x, y, id, attributes) {

		var img = this._convert(sourceCanvas, targetSVG, x, y, id);

		for (var i = 0; i < attributes.length; i++) {
			img.setAttribute(attributes[i][0], attributes[i][1]);
		}
	},

	_convert : function(sourceCanvas, targetSVG, x, y, id) {
		var svgNS = "http://www.w3.org/2000/svg";
		var xlinkNS = "http://www.w3.org/1999/xlink";
		// get base64 encoded png from Canvas
		var image = sourceCanvas.toDataURL();

		// must be careful with the namespaces
		var svgimg = document.createElementNS(svgNS, "image");

		svgimg.setAttribute('id', id);

		// svgimg.setAttribute('class', class);
		// svgimg.setAttribute('xlink:href', image);
		svgimg.setAttributeNS(xlinkNS, 'xlink:href', image);

		svgimg.setAttribute('x', x ? x : 0);
		svgimg.setAttribute('y', y ? y : 0);
		svgimg.setAttribute('width', sourceCanvas.width);
		svgimg.setAttribute('height', sourceCanvas.height);
		// svgimg.setAttribute('cursor', 'pointer');
		svgimg.imageData = image;

		targetSVG.appendChild(svgimg);
		return svgimg;
	},

	importSVG : function(sourceSVG, targetCanvas) {
		svg_xml = sourceSVG;// (new
							// XMLSerializer()).serializeToString(sourceSVG);
		var ctx = targetCanvas.getContext('2d');

		var img = new Image();
		img.src = "data:image/svg+xml;base64," + btoa(svg_xml);
		// img.onload = function() {
		ctx.drawImage(img, 0, 0);
		// };
	}

};
/*
 * Graph.prototype.importSVG = function(sourceSVG, targetCanvas) { sourceSVG =
 * this._svg; targetCanvas = document.createElementNS('canvas'); //
 * https://developer.mozilla.org/en/XMLSerializer svg_xml = (new
 * XMLSerializer()).serializeToString(sourceSVG); var ctx =
 * targetCanvas.getContext('2d'); // this is just a JavaScript (HTML) image var
 * img = new Image(); // http://en.wikipedia.org/wiki/SVG#Native_support //
 * https://developer.mozilla.org/en/DOM/window.btoa img.src =
 * "data:image/svg+xml;base64," + btoa(svg_xml); img.onload = function() { //
 * after this, Canvas origin-clean is DIRTY ctx.drawImage(img, 0, 0); } };
 */

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

var SHAPE = 0;
var SQUARE = 1;
var RECTANGLE = 2;
var ROUNDEDREC = 3;
var CIRCLE = 4;
var TRIANGLE = 5;
var PANEL = 6;
var TEXT = 7;
var LINE = 9;

var VERTICAL = 0;
var HORIZONTAL = 1;

function Point(x, y) {
	this.x = x;
	this.y = y;
}
Point.prototype.getDistance = function(point) {

	var dx = Math.pow(this.x - point.x, 2);
	var dy = Math.pow(this.y - point.y, 2);
	return Math.sqrt(dx + dy);
};

function Shape(top, left) {
	this.top = top;
	this.left = left;
	this.color = "#000000";
	this.canvas = null;
	this.visible = true;
	this.type = SHAPE;

	this.borderColor = "#000000";
	this.borderWidth = 0;

}
Shape.prototype.toString = function() {
	return this.type + ': ' + this.top + ' ' + this.left;
};

Shape.prototype.setBorderColor = function(color) {
	this.borderColor = color;
};

Shape.prototype.setBorderWidth = function(size) {
	this.borderWidth = size;
};

Shape.prototype.setColor = function(color) {
	this.color = color;
};

Shape.prototype.render = function(canvas) {
	this.canvas = canvas;

};

Shape.prototype.setVisible = function(visible) {
	this.visible = visible;
};

/** ******** Rectangle *************** */
function Rectangle(top, left, height, width) {

	Shape.call(this, top, left);
	this.width = width;
	this.height = height;
	this.type = RECTANGLE;

}

Rectangle.prototype = new Shape();
Rectangle.prototype.constructor = Rectangle;
Rectangle.prototype.render = function(canvas) {
	if (!this.visible)
		return;
	canvas.save();

	canvas.lineWidth = 2;
	canvas.strokeStyle = "#000000";
	canvas.beginPath();
	canvas.moveTo(this.left, this.top);
	canvas.lineTo(this.left + this.width, this.top);
	canvas.lineTo(this.left + this.width, this.top + this.height);
	canvas.lineTo(this.left, this.top + this.height);
	canvas.closePath();
	canvas.stroke();

	canvas.fillStyle = this.color;
	// canvas.clearRect(this.left, this.top, this.width, this.height);

	canvas.fillRect(this.left, this.top, this.width, this.height);
	// canvas.strokeStyle = "#000000";
	// canvas.strokeRect(this.left, this.top, this.width, this.height);

	canvas.restore();
};

Rectangle.prototype.remove = function() {
	this.canvas.clearRect(this.left, this.top, this.width, this.height);
};

Rectangle.prototype.contains = function(x, y) {
	if (y >= this.left && y <= this.width + this.left) {
		if (x >= this.top && x <= this.height + this.top) {
			return true;
		} else
			return false;
	} else
		return false;
};

/** ******** RoundedRec *************** */
function RoundedRec(left, top, width, height, radio) {

	Rectangle.call(this, top, left, height, width);
	this.radio = radio;
	this.type = ROUNDEDREC;

}

RoundedRec.prototype = new Rectangle();
RoundedRec.prototype.constructor = RoundedRec;

RoundedRec.prototype.render = function(canvas) {
	if (!this.visible)
		return;
	canvas.save();
	canvas.fillStyle = this.color;
	canvas.beginPath();
	canvas.moveTo(this.left, this.top + this.radio);
	canvas.lineTo(this.left, this.top + this.height - this.radio);
	canvas.quadraticCurveTo(this.left, this.top + this.height, this.left+ this.radio, this.top + this.height);
	canvas.lineTo(this.left + this.width - this.radio, this.top + this.height);
	canvas.quadraticCurveTo(this.left + this.width, this.top + this.height,this.left + this.width, this.top + this.height - this.radio);
	canvas.lineTo(this.left + this.width, this.top + this.radio);
	canvas.quadraticCurveTo(this.left + this.width, this.top, this.left+ this.width - this.radio, this.top);
	canvas.lineTo(this.left + this.radio, this.top);
	canvas.quadraticCurveTo(this.left, this.top, this.left, this.top+ this.radio);
	canvas.fill();
	if (this.borderWidth != 0) {
		canvas.strokeStyle = this.borderColor;
		canvas.lineWidth = this.borderWidth;
		canvas.stroke();
	}
	canvas.restore();

};

/** ******* Square ************** */
function Square(left, top, lado) {

	Shape.call(this, top, left);
	this.height = lado;
	this.width = lado;
	this.type = SQUARE;
}

Square.prototype = new Shape();
Square.prototype.constructor = Square;

Square.prototype.remove = function() {
	this.canvas.clearRect(this.top, this.left, this.width, this.height);
};

Square.prototype.render = function(canvas) {
	Shape.prototype.render.call(this, canvas);
	if (!this.visible)
		return;
	canvas.save();

	canvas.fillStyle = this.color;
	canvas.fillRect(this.top, this.left, this.width, this.height);
	if (this.borderWidth != 0) {
		canvas.strokeStyle = this.borderColor;
		canvas.lineWidth = this.borderWidth;
		canvas.strokeRect(this.top, this.left, this.width, this.height);
	}
	canvas.restore();
};

Square.prototype.contains = function(x, y) {
	if (y >= this.left && y <= this.width + this.left) {
		if (x >= this.top && x <= this.height + this.top) {
			return true;
		} else
			return false;
	} else
		return false;
};

/** ********* Circle ********* */
function Circle(left, top, Radio) {
	Shape.call(this, top, left);
	this.radio = Radio;
	this.type = CIRCLE;
}

Circle.prototype = new Shape();
Circle.prototype.constructor = Circle;

Circle.prototype.render = function(canvas) {
	Shape.prototype.render.call(this, canvas);
	if (!this.visible)
		return;
	canvas.save();
	canvas.beginPath();
	canvas.fillStyle = this.color;
	canvas.arc(this.top + this.radio, this.left + this.radio, this.radio, 0,
			Math.PI * 2, true);

	if (this.borderWidth != 0) {
		canvas.strokeStyle = this.borderColor;
		canvas.lineWidth = this.borderWidth;
		canvas.stroke();
	}
	canvas.closePath();
	canvas.fill();
	canvas.restore();
};

Circle.prototype.contains = function(x, y) {
	var point = new Point(x, y);
	var distancia = point.getDistance(new Point(this.top + this.radio,
			this.left + this.radio));
	if (distancia <= this.radio)
		return true;
	else
		return false;
};

/** ******** Triangle *************** */
function Triangle(x1, y1, x2, y2, x3, y3) {
	Shape.call(this, x1, y1);
	this.x1 = x1;
	this.y1 = y1;
	this.x2 = x2;
	this.y2 = y2;
	this.x3 = x3;
	this.y3 = y3;
	this.type = TRIANGLE;

}

Triangle.prototype = new Shape();
Triangle.prototype.constructor = Triangle;
Triangle.prototype.render = function(canvas) {
	if (!this.visible)
		return;
	canvas.save();
	canvas.fillStyle = this.color;
	canvas.beginPath();
	canvas.moveTo(this.x1, this.y1);
	canvas.lineTo(this.x2, this.y2);
	canvas.lineTo(this.x3, this.y3);
	canvas.closePath();
	canvas.fill();
	if (this.borderWidth != 0) {
		canvas.strokeStyle = this.borderColor;
		canvas.lineWidth = this.borderWidth;
		canvas.stroke();
	}
	canvas.restore();
};

Triangle.prototype.toString = function() {
	return this.type;
};

Triangle.prototype.contains = function(x, y) {

	return false;
};

/** ******* Text *********** */
function Text(left, top, text) {
	Shape.call(this, top, left);
	this.text = text;
	this.type = TEXT;
	this.orientation = HORIZONTAL;

}

function Text(top, left, text, orientation) {
	Shape.call(this, top, left);
	this.text = text;
	this.type = TEXT;
	this.orientation = orientation;

}

Text.prototype = new Shape();
Text.prototype.constructor = Text;

Text.prototype.setFont = function(font) {
	this.font = font;
};

Text.prototype.render = function(canvas) {

	canvas.save();

	canvas.fillStyle = this.color;
	canvas.font = this.font;
	canvas.textBaseline = 'top';

	var dim = canvas.measureText(this.text);

	if (this.orientation != HORIZONTAL) {
		canvas.rotate(-Math.PI / 2);
		canvas.translate(-this.top, this.left);
	} else {

		// canvas.translate(this.top,this.left);
	}

	canvas.fillText(this.text, 0, 0);
	canvas.restore();
};

/** ******* LINE ************** */
function Line(pointX, pointY) {

	Shape.call(this, pointX.x, pointX.y);
	this.point = pointY;
	this.type = LINE;
	this.color = "#FFCCFF";
}

Line.prototype = new Shape();
Line.prototype.constructor = Line;

Line.prototype.remove = function() {
	this.canvas.clearRect(this.top, this.left, 1, 1);
};

Line.prototype.render = function(canvas) {
	Shape.prototype.render.call(this, canvas);

	canvas.save();

	canvas.fillStyle = this.color;

	canvas.beginPath();
	canvas.moveTo(this.left, this.top);
	canvas.lineTo(this.point.x, this.point.y);
	canvas.closePath();
	canvas.stroke();

	canvas.restore();
};

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

// Grid entity with the model, the controller and the view
function Grid(top, left, height, width, canvas, targetNode, id) {

	this._top = top;
	this._left = left;
	this._height = height;
	this._width = width;

	this._svg = SVG.createSVGCanvas(targetNode, [["id", "meshSvgId_"+id],
					["height", height + 260], ["width", width + 630]]);

	this._canvas = canvas;
	this.model = new GridModel();

	this.gridView = new GridView(this._svg, canvas, id);
	this.controller = new GridController(this.model, this.gridView, this._svg,
			this._canvas, top, left, height, width, 25, id);
}

Grid.prototype.refresh = function() {
	this.controller.setHeight((this.model.getRowNamesCount() * this.model
			.getSquareSize()));
	this.controller.setWidth((this.model.getColumnNamesCount() * this.model
			.getSquareSize()));
	return this.controller.refresh();
};

Grid.prototype.getColumnNameSpace = function() {
	return this.controller._columnNameHeight;
};

Grid.prototype.setColumnNameSpace = function(value) {
	this.controller.setColumnNameSpace(parseInt(value));
};

Grid.prototype.getRowNameSpace = function() {
	return this.controller._rowNameWidth;
};

Grid.prototype.setRowNameSpace = function(value) {
	this.controller.setRowNameSpace(parseInt(value));
};

Grid.prototype.getFontSize = function() {
	return this.gridView._fontSize;
};

Grid.prototype.setFontSize = function(value) {
	this.gridView._fontSize = value;
};

Grid.prototype.setWidth = function(value) {
	this.controller.setWidth(parseInt(value));
};

Grid.prototype.getWidth = function(value) {
	return this.controller.getWidth();
};

Grid.prototype.setHeight = function(value) {
	this.controller.setHeight(parseInt(value));
};

Grid.prototype.getHeight = function(value) {
	return this.controller.getHeight();
};

Grid.prototype.fill = function(rowNames, columnNames, data, scores, positions,
		images, title, axisXName, axisYName, squareSize, minValue, maxValue,
		colorRangeManager) {
	this.model.setRowsName(rowNames);
	this.model.setColumnsName(columnNames);
	this.model.setScores(scores);
	this.model.setPositions(positions);
	this.model.setData(data);
	this.model.setTitle(title);
	this.model.setAxisXName(axisXName);
	this.model.setAxisYName(axisYName);
	this.model.setSquareSize(squareSize);
	this.model.setMinValue(minValue);
	this.model.setMaxValue(maxValue);
	this.model.setColorRangeManager(colorRangeManager);
	this.model.setImages(images);
};

Grid.prototype.loadImage = function(fileLocation) {
	this.model.setBackgroundImage(fileLocation);
	this.controller.loadBackgroundImage();
};

Grid.prototype.unloadImage = function() {
	this.model.setBackgroundImage(null);
	this.controller.unloadBackgroundImage();
};

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
// *** CONTROLERS **/
function GridController(model, view, svg, canvas, top, left, height, width,
		cellWidth, id) {
		
	this._model = model;
	this._view = view;

	this._top = top;
	this._left = left;
	this._height = height;
	this._width = width;
	this.id = id;

	this._cellWidth = cellWidth;
	this._cellHeight = null;

	// Columns
	this._columnNameSpace = 10;
	this._columnNameHeight = 20;
	this._pixelPerCharacter = 6;

	// Rows
	this._rowNameWidth = null;
	this._rowNameSpace = 10;

	// cell
	this._spanCell = 1;

	// DOM id's and classes
	this._row_id_prefix = id+'_row_name_';
	this._row_name_class = 'NO_EVENT';

	this._columnName_id_prefix = id+'_columnName_';
	this._columnName_name_class = 'COLUMN';

	this._columnNameHeader_name_class = 'COLUMN_HEADER';

	this._title_id_prefix = id+'_title';
	this._title_name_class = 'NO_EVENT';

	// p
	this._column_id_prefix = id+'_column_';
	this._column_name_class = 'COLUMN';

	var _this = this;

	// attach model listeners
	this._model.rowsNameAdded.attach(function() {
				_this._cellHeight = _this._height / _this._model.getRowNamesCount();
				_this.renderRowsName();
			});

	this._model.columnsNameAdded.attach(function() {
				_this._cellWidth = _this._width / _this._model.getColumnNamesCount();
				_this.renderColumnsName(0, _this._model.getColumnNamesCount() - 1);
			});

	this._model.dataAdded.attach(function() {
				_this._cellWidth = _this._width / _this._model.getColumnNamesCount();
				_this._cellHeight = _this._height / _this._model.getRowNamesCount();
				_this.renderGrid(0, _this._model.getColumnNamesCount() - 1);
			});

	this._view.mousedown.attach(function() {
				// console.log("mouseDown");
			});

	this._view.refreshEvent.attach(function() {
				_this.refresh();
			});

	this._view.moveColumnsEvent.attach(function(a) {

				

			});

}

GridController.prototype.setHeight = function(value) {
	this._height = value;
	this.setCellSize();
};

GridController.prototype.setWidth = function(value) {
	this._width = value;
	this.setCellSize();
};

GridController.prototype.getHeight = function() {
	return this._height;
};

GridController.prototype.getWidth = function() {
	return this._width;
};

GridController.prototype.setCellSize = function() {
	this._cellHeight = this._height / this._model.getRowNamesCount();
	this._cellWidth = this._width / this._model.getColumnNamesCount();
};

GridController.prototype.getRelativeTop = function() {
	return 10;
};

GridController.prototype.setSpaceCell = function(value) {
	this._spanCell = value;
	this.renderGrid(0, this._model.getColumnNamesCount() - 1);
};

GridController.prototype.setRowNameSpace = function(value) {
	this._rowNameWidth = value;
};

GridController.prototype.setColumnNameSpace = function(value) {
	this._columnNameSpace = value;
};

GridController.prototype._getCalculatedcolumnNameHeight = function(value) {
	return this._getLengthColumnNames() * this._pixelPerCharacter;
};

GridController.prototype._getCalculatedAxisYNameSpace = function(value) {
	return this._model.getAxisYName().length * this._pixelPerCharacter;
};

GridController.prototype._getCalculatedTitleHeight = function(value) {
	return this._pixelPerCharacter;
};

GridController.prototype._getCalculatedRowNameWidth = function(value) {
	return this._getLengthRowNames() * this._pixelPerCharacter;
};

GridController.prototype._getMaxLengthArray = function(array) {
	var max = 0;
	for (var i = 0; i < array.length; i++) {
		if (array[i]) {
			if (max < array[i].length) {
				max = array[i].length;
			}
		}
	}
	return max;
};

GridController.prototype._getLengthRowNames = function() {
	if (this._model.getRowNames() == null)
		return 5;
	else
		return this._getMaxLengthArray(this._model.getRowNames());
};

GridController.prototype._getLengthColumnNames = function() {
	if (this._model.getColumnNames() == null)
		return 5;
	else
		return this._getMaxLengthArray(this._model.getColumnNames());
};

GridController.prototype._getLengthTitle = function() {
	return 6;
};

// refresh
GridController.prototype.refresh = function() {
	this._view.clearRowsName();
	this._view.clearColumnsName();
	this._view.clearGrid(0, this._model.getColumnNamesCount() - 1);
	this._view.clearTitle();
	this._view.clearAxisYName();
	this._view.clearAxisXName();
	this._view.clearColorRange();
	this.unloadBackgroundImage();

	this.renderTitle();
	this.renderAxisYName();
	this.renderAxisXName();
	this.renderRowsName();
	this.renderColumnsName(0, this._model.getColumnNamesCount() - 1);
	this.renderGrid(0, this._model.getColumnNamesCount() - 1);
	this.renderColorRange();
	this.loadBackgroundImage();
};

// paint
GridController.prototype.paint = function() {
	this.renderTitle();
	this.renderAxisYName();
	this.renderAxisXName();
	this.renderRowsName();
	this.renderColumnsName(0, this._model.getColumnNamesCount() - 1);
	this.renderGrid(0, this._model.getColumnNamesCount() - 1);
	this.renderColorRange();
};

// render

GridController.prototype.renderColorRange = function() {
	var relative_local = this.getRelativeTop();
	var top_local = this._top + this._columnNameHeight + this._columnNameSpace + ((this._model.getRowNamesCount() + 1) * this._cellHeight) + relative_local + this._getCalculatedTitleHeight() * 5;
	var left_local = this._left + this._rowNameWidth + this._rowNameSpace + this._getCalculatedAxisYNameSpace() + this._cellHeight;
	this._view.renderColorRanger(top_local, left_local, this._model.getMinValue(), this._model.getMaxValue(), this._model.getColorRangeManager(), 10, 10, this._column_id_prefix, this._column_name_class, this.id);
};

GridController.prototype.renderTitle = function() {
	var relative_local = this.getRelativeTop();
	var top_local = this._top + this._columnNameSpace + relative_local;
	var left_local = this._left + this._rowNameWidth + this._rowNameSpace;
	this._view.renderTitle(top_local, left_local, this._model.getTitle(),
			this._title_id_prefix, this._title_name_class);
};

GridController.prototype.renderAxisYName = function() {
	var relative_local = this.getRelativeTop();
	if (this._columnNameHeight == null)
		this._columnNameHeight = this._getCalculatedcolumnNameHeight();
	var top_local = this._top + this._columnNameHeight + this._columnNameSpace + this.getRelativeTop() + (this._model.getRowNamesCount() * this._cellHeight) / 2 + this._getCalculatedTitleHeight();
	var left_local = this._left;
	this._view.renderAxisYName(top_local, left_local, this._model.getAxisYName(), this.id+'_axisYName', this._row_name_class);
};

GridController.prototype.renderAxisXName = function() {
	var relative_local = this.getRelativeTop();
	if (this._columnNameHeight == null)
		this._columnNameHeight = this._getCalculatedcolumnNameHeight();
	var top_local = this._top + this._columnNameHeight + this._columnNameSpace + ((this._model.getRowNamesCount() + 1) * this._cellHeight) + relative_local + this._getCalculatedTitleHeight() * 3;
	var left_local = this._left + this._rowNameWidth + this._rowNameSpace + this._getCalculatedAxisYNameSpace() + (this._model.getRowNamesCount() * this._cellHeight) / 3;
	this._view.renderAxisYName(top_local, left_local, this._model.getAxisXName(), this.id+'_axisXName', this._row_name_class);
};

GridController.prototype.renderRowsName = function() {
	if (this._columnNameHeight == null)
		this._columnNameHeight = this._getCalculatedcolumnNameHeight();
	var relative_local = this.getRelativeTop();
	var top_local = this._top + this._columnNameSpace + this._columnNameHeight + this._cellHeight / 2 + relative_local + this._getCalculatedTitleHeight();
	var left_local = this._left + this._getCalculatedAxisYNameSpace()+ this._cellHeight / 2;
	this._view.renderRowNames(top_local, left_local, this._cellHeight,this._model.getRowNames(), this._row_id_prefix, this._row_name_class);
};

GridController.prototype.renderColumnsName = function(a, b) {
	if (this._rowNameWidth == null)
		this._rowNameWidth = this._getCalculatedRowNameWidth();
	var left = this._left + this._rowNameWidth + this._rowNameSpace + this._cellWidth / 2 + this._getCalculatedAxisYNameSpace() + (this._cellHeight / 2);
	if (this._columnNameHeight == null)
		this._columnNameHeight = this._getCalculatedcolumnNameHeight();
	var topC = this._top + this.getRelativeTop() + this._columnNameSpace + this._columnNameHeight + this._getCalculatedTitleHeight() + ((this._model.getRowNamesCount()) * this._cellHeight) + this._getCalculatedTitleHeight();
	this._view.renderColumnNames(topC, left, a, b, this._cellWidth, this._model.getColumnNames(), this._columnName_id_prefix, this._columnNameHeader_name_class);

};

GridController.prototype.renderGrid = function(a, b) {
	if (this._rowNameWidth == null)
		this._rowNameWidth = this._getCalculatedRowNameWidth();
	var left = this._left + this._rowNameWidth + this._rowNameSpace + this._getCalculatedAxisYNameSpace() + this._cellHeight;

	if (this._columnNameHeight == null)
		this._columnNameHeight = this._getCalculatedcolumnNameHeight();
	var top = this._top + this._columnNameHeight + this._columnNameSpace + this.getRelativeTop() + this._getCalculatedTitleHeight();
	this._view.renderGrid(top, left, a, b, this._cellWidth, this._cellHeight, this._spanCell, this._column_id_prefix, this._column_name_class, this._model.getData(), this._model.getScores(), this._model.getPositions(), this._model.getImages());
};

GridController.prototype.loadBackgroundImage = function() {
	if (this._rowNameWidth == null)
		this._rowNameWidth = this._getCalculatedRowNameWidth();
	var left = this._left + this._rowNameWidth + this._rowNameSpace + this._getCalculatedAxisYNameSpace();
	var top = this._top + this._columnNameHeight + this._columnNameSpace + this.getRelativeTop() + this._getCalculatedTitleHeight();
	this._view.loadBackgroundImage(this._model.getBackgroundImage(), top, left);
};

GridController.prototype.unloadBackgroundImage = function() {
	this._view.clearBackgroundImage();
};

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
/**
 * The Model. Model stores items and notifies observers about changes.
 */
function GridModel() {
	this._rows = null;
	this._columns = null;
	this._data = null;
	this._rowNames = null;
	this._columnNames = null;
	this.rowsNameAdded = new Event(this);
	this.columnsNameAdded = new Event(this);
	this.dataAdded = new Event(this);
	this._scores = null;
	this._images = null;
	this._positions = null;
	this._title = null;
	this._axisXName = null;
	this._axisYName = null;
	this._squareSize = null;
	this._minValue = null;
	this._maxValue = null;
	this._colorRangeManager = null;
	this._backgroundImage = null;

}

GridModel.prototype.setBackgroundImage = function(value) {
	this._backgroundImage = value;
};

GridModel.prototype.getBackgroundImage = function() {
	return this._backgroundImage;
};

GridModel.prototype.setScores = function(value) {
	this._scores = value;
};

GridModel.prototype.getScores = function() {
	return this._scores;
};

GridModel.prototype.setImages = function(value) {
	this._images = value;
};

GridModel.prototype.getImages = function() {
	return this._images;
};

GridModel.prototype.setPositions = function(value) {
	this._positions = value;
};

GridModel.prototype.getPositions = function() {
	return this._positions;
};
// set the name of the columns
GridModel.prototype.setColumnsName = function(value) {
	this._columnNames = value;
};

GridModel.prototype.getColumnNames = function() {
	return this._columnNames;
};

GridModel.prototype.getColumnNamesCount = function() {
	return this._columnNames.length;
};

// set the name of the rows
GridModel.prototype.setRowsName = function(value) {
	this._rowNames = value;
};

GridModel.prototype.getRowNames = function() {
	return this._rowNames;
};

GridModel.prototype.getRowNamesCount = function() {
	return this._rowNames.length;
};

// title & axisName
GridModel.prototype.setTitle = function(value) {
	this._title = value;
};

GridModel.prototype.getTitle = function() {
	return this._title;
};

GridModel.prototype.setAxisXName = function(value) {
	this._axisXName = value;
};

GridModel.prototype.getAxisXName = function() {
	return this._axisXName;
};

GridModel.prototype.setAxisYName = function(value) {
	this._axisYName = value;
};

GridModel.prototype.getAxisYName = function() {
	return this._axisYName;
};

// square size
GridModel.prototype.setSquareSize = function(value) {
	this._squareSize = value;
};

GridModel.prototype.getSquareSize = function() {
	return this._squareSize;
};

//min value
GridModel.prototype.setMinValue = function(value) {
	this._minValue = value;
};

GridModel.prototype.getMinValue = function() {
	return this._minValue;
};

// max value
GridModel.prototype.setMaxValue = function(value) {
	this._maxValue = value;
};

GridModel.prototype.getMaxValue = function() {
	return this._maxValue;
};

// colorRangemanager
GridModel.prototype.setColorRangeManager = function(value) {
	this._colorRangeManager = value;
};

GridModel.prototype.getColorRangeManager = function() {
	return this._colorRangeManager;
};

// render
GridModel.prototype.render = function() {
	this.rowsNameAdded.notify({
				item : this._rowNames
			});
	this.columnsNameAdded.notify({
				item : this._columnNames
			});
	this.dataAdded.notify(this._data);
};

// data
GridModel.prototype.setData = function(value) {
	this._data = value;
	this._rows = this._data.length;
	this._columns = 0;
	if (this._data[0])
		this._columns = this._data[0].length;
};

GridModel.prototype.getData = function() {
	return this._data;
};

// copy
GridModel.prototype.copy = function(a, b) {
	for (var i = 0; i < this.getRowNamesCount(); i++) {
		this._data[i][b] = this._data[i][a];
	}
};

// move column a to b index
GridModel.prototype.move = function(a, b) {
	var column_a = [];
	var column_b_name = this._columnNames[b];
	var column_a_name = this._columnNames[a];

	if (b > a) {
		// backup column a
		for (var i = 0; i < this.getRowNamesCount(); i++) {
			column_a.push(this._data[i][a]);
		}
		//
		for (var i = a; i <= b; i++) {
			var inc = parseInt(i) + 1;
			this.copy(inc, i);
			// console.log(this._columnNames[i] +"<<" + this._columnNames[inc] +
			// " i: "+i+ " inc:"+inc);

			this._columnNames[i] = this._columnNames[inc];
		}
		//
		for (var i = 0; i < this.getRowNamesCount(); i++) {
			this._data[i][b] = column_a[i];
			this._columnNames[b] = column_b_name;
		}

		this._columnNames[b] = column_a_name;
	} else {
		// backup column a
		for (var i = 0; i < this.getRowNamesCount(); i++) {
			column_a.push(this._data[i][a]);
		}
		//
		for (var i = a; i > b; i--) {
			this.copy(i - 1, i);
			this._columnNames[i] = this._columnNames[i - 1];
		}
		// restored column a
		for (var i = 0; i < this.getRowNamesCount(); i++) {
			this._data[i][b] = column_a[i];
		}

		this._columnNames[b] = column_a_name;

	}
};

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
// ** view **/
function GridView(svg, canvas, id) {

	this._svg = svg;
	this._canvas = canvas;
	this.id = id;
	this._fontSize = 9;
	this._fontTitleSize = 12;

	this.cellInfo = true;

	this.columnName_id_prefix = null;
	this.columnName_name_class = null;
	this.columnHeaderName_name_class = null;

	this.column_id_prefix = null;
	this.column_class = null;

	this.rowName_id_prefix = null;
	this.rowName_name_class = null;

	// title and axis names
	this.title = "";
	this.axisXName = "";
	this.axisYName = "";

	// Array
	this.rowNames = null;

	this.columnNames = null;
	this.rowsName = null;
	this.data = null;
	this.scores = null;
	this.positions = null;
	this.images = null;

	this.columnConfiguration = null;
	this.enableDraggin = true;
	this.draggin = false;

	this.originColumnX = null;
	this.originColumnID = null;

	this.columnIndex = [];
	this.columnPosX = [];

	this.cellZoomId = id + "_cellZoom";

	/* Moving grid */
	this.enableMovingGrid = true;
	this.dragginGrid = false;
	this.movingGridPrevX = null;
	this.movingGridcurrentX = null;

	this.gridOffset = 0;
	this.previousGridOffset = 0;

	this.prevMouse = null;
	this.dragTarget = null;

	this.left = null;
	this.topGrid = null;
	this.cellWidth = null;

	var _this = this;

	this.mousedown = new Event(this);
	this.moveColumnsEvent = new Event(this);
	this.refreshEvent = new Event(this);

	this.cellSelected = new Event(this);

	this._svg.addEventListener("mousedown", function(event) {
				_this.onmousedown(event, _this);
			}, false);
	this._svg.addEventListener("mousemove", function(event) {
				_this.onmousemove(event, _this);
			}, false);
	this._svg.addEventListener("mouseup", function(event) {
				_this.onmouseup(event, _this);
			}, false);

	this.columnNamesXpos = [];

	this.lastColumnNameHeaderSelected = null;
	this.lastRowNameHeaderSelected = null;

	this.rowSelected = null;
	this.columnSelected = null;
}

GridView.prototype.setFontSize = function(size) {
	this._fontSize = size;
	this.refreshEvent.notify();
};

GridView.prototype.checkRangeForMovingColumns = function(columnOver) {
	return ((columnOver < this.columnNames.length) && (columnOver >= 0));
};

GridView.prototype.doOverEffect = function(columnOver, rowOver) {
	/*
	 * if ((columnOver < 0) || (rowOver < 0)) return;
	 * 
	 * 
	 * if (this.lastColumnNameHeaderSelected != null) { if
	 * (this.lastColumnNameHeaderSelected != this.columnName_id_prefix+
	 * columnOver) { var p =
	 * document.getElementById(this.lastColumnNameHeaderSelected); if (p !=
	 * null) p.setAttribute("style", ""); } if (this.lastRowNameHeaderSelected !=
	 * this.rowName_id_prefix+ rowOver) { var t =
	 * document.getElementById(this.lastRowNameHeaderSelected); if (t != null)
	 * t.setAttribute("style", ""); } } this.lastColumnNameHeaderSelected =
	 * this.columnName_id_prefix+ columnOver; this.lastRowNameHeaderSelected =
	 * this.rowName_id_prefix + rowOver;
	 */
};

GridView.prototype.recolocateColumns = function() {
	for (var i = 0; i < this.columnIndex.length; i++) {
		document.getElementById(this.columnIndex[i]).setAttribute("x",
				this.columnPosX[i]);
		document.getElementById(this.columnIndex[i]).setAttributeNS(null,
				"transform", "translate(" + this.gridOffset + ")");
	}
};

GridView.prototype.moveClientSideColumnNames = function(selectedColumn, over) {
	var selectedID = this.columnIndex[selectedColumn];
	var columnName = this.columnNames[selectedColumn];
	if (selectedColumn < over) {
		for (var i = parseInt(parseInt(selectedColumn) + parseInt(1)); parseInt(over) >= i; i++) {
			this.columnIndex[(i - 1)] = this.columnIndex[i];
			this.columnNames[(i - 1)] = this.columnNames[i];
		}
	}
	if (selectedColumn > over) {
		for (var i = parseInt(parseInt(selectedColumn)); parseInt(over) < i; i--) {
			this.columnIndex[(i)] = this.columnIndex[(i - 1)];
			this.columnNames[(i)] = this.columnNames[(i - 1)];
		}
	}
	// document.getElementById(selectedID
	// ).setAttributeNS(null,"transform","translate("+this.gridOffset+")");
	this.columnIndex[over] = selectedID;
	this.columnNames[over] = columnName;
	this.recolocateColumns();
};

GridView.prototype.moveHorizontaleColumns = function(value) {
	this.gridOffset = value + this.previousGridOffset;
	// columns
	for (var i = 0; i < this.columnIndex.length; i++) {
		// var attribute =
		// document.getElementById(this.columnIndex[i]).getAttributeNS(null,
		// "transform", "translate("+parseInt(value+
		// this.previousGridOffset)+")");
		document.getElementById(this.columnIndex[i]).setAttributeNS(null,
				"transform",
				"translate(" + parseInt(value + this.previousGridOffset) + ")");

		// var attribute = document.getElementById(this.columnName_id_prefix
		// + i).getAttributeNS(null, "transform", "translate("+value+")");
		document.getElementById(this.columnName_id_prefix + i).setAttributeNS(
				null, "transform",
				"translate(" + parseInt(value + this.previousGridOffset) + ")");

		if (document.getElementById("LINE_" + i) != null) {
			document.getElementById(this.id_dom_prefix_class + i)
					.setAttributeNS(
							null,
							"transform",
							"translate(" + parseInt(value + this.previousGridOffset) + ")");
			document.getElementById("LINE_" + i).setAttributeNS(
					null,
					"transform",
					"translate(" + parseInt(value + this.previousGridOffset) + ")");
		}

	}
	this.clearColumnsName();
	this.renderClientSideColumnNames(this.columnConfiguration);
	try {
		this.renderClassesName();
	} catch (e) {
	}
};

GridView.prototype.moveHorizontalGrid = function(value) {
	this.moveHorizontaleColumns(value);
};

GridView.prototype.drawCellInfoDiv = function(rowOver, columnOver, event) {
	var mouse = getMouseCoords(event, this._svg);
	this.rowSelected = rowOver;
	this.columnSelected = columnOver;

	if ((rowOver < 0) || (columnOver < 0) || (rowOver > this.rowNames.length) || (columnOver > this.columnNames.length)) {
		this.removeCellInfoDiv();
		return;
	}

	if (this.columnIndex[columnOver] == null)
		return;

	// var title1 = this.columnNames[columnOver];
	var title1 = "";
	if (this.images != null) {
		title1 = "imageDownload.do?reqCode=getImageJpgThumb&imageId=" + this.images[rowOver][this.columnIndex[columnOver].replace(this.column_id_prefix, "")];
	}
	var title2 = "";
	var title3 = "";

	if (this.scores != null) {
		title3 = "Position: " + this.positions[rowOver][this.columnIndex[columnOver].replace(this.column_id_prefix, "")];
		title2 = "Value = " + this.scores[rowOver][this.columnIndex[columnOver].replace(this.column_id_prefix, "")];
	}
	var color = this.data[rowOver][this.columnIndex[columnOver].replace(
			this.column_id_prefix, "")];

	this.drawCellInfoSVG(mouse.x + 10, mouse.y + 10, this._svg, title1, title2,title3, color);
	this.doOverEffect(columnOver, rowOver);
};

GridView.prototype.drawCellInfoSVG = function(x, y, canvasSVG, title1, title2, title3, color) {
	var group = SVG.createGroup([['id', this.cellZoomId]]);

	var rectAttributes = [['fill-opacity', '0.85'], ['fill', 'white'],
			['stroke', 'black'], ['rx', 5], ['ry', 5]];
	var rect = SVG.createRectangle(x, y, 350, 210, rectAttributes);

	var textVerticalSpace = 20;

	var snapshotX = x + 10;
	var snapshotY = y + 25;

	var snapshotAtrributes = [['fill-opacity', '1'], ['x', snapshotX],
			['y', snapshotY], ['stroke', 'black'], ['fill', color]];
	var snapshot = SVG.createRectangle(snapshotX, snapshotY, 12, 12,
			snapshotAtrributes);

	var textAtrributes = [["font-size", '12']];
	// var text1 = SVG.createText(x + 30, y + textVerticalSpace, title1,
	// textAtrributes);
	var svgns = 'http://www.w3.org/2000/svg';
	var img = document.createElementNS(svgns, 'image');
	img.setAttributeNS(null, 'x', x + 30);
	img.setAttributeNS(null, 'y', y + (textVerticalSpace * 3));
	img.setAttributeNS(null, 'height', 125);
	img.setAttributeNS(null, 'width', 125);
	var xlinkns = "http://www.w3.org/1999/xlink";
	img.setAttributeNS(xlinkns, 'xlink:href', title1);

	textAtrributes = [["font-size", '12']];
	var text2 = SVG.createText(x + 30, y + (textVerticalSpace * 1), title2, textAtrributes);

	textAtrributes = [["font-size", '12']];
	var text3 = SVG.createText(x + 30, y + (textVerticalSpace * 2), title3, textAtrributes);

	group.appendChild(rect);
	group.appendChild(snapshot);

	group.appendChild(text2);
	group.appendChild(text3);
	group.appendChild(img);

	canvasSVG.appendChild(group);
};

GridView.prototype.removeCellInfoDiv = function() {
	var zoomer = document.getElementById(this.cellZoomId);
	if (zoomer != null)
		this._svg.removeChild(zoomer);
};

GridView.prototype.onmousemove = function(e, view) {
	var mouse = getMouseCoords(e, this._svg);
	var rowOver = this.getRowOver(e);
	var columnOver = this.getColumnOver(e);

	if (this.data != null) {
		if (this.cellInfo) {
			this.removeCellInfoDiv();

			if (rowOver < this.rowNames.length)
				this.drawCellInfoDiv(rowOver, columnOver, e);
		}
	}

	if (this.dragginGrid && this.enableMovingGrid) {
		this.movingGridcurrentX = mouse.x;
		this.gridOffset = this.movingGridcurrentX - this.movingGridPrevX + this.previousGridOffset;
		this.moveHorizontalGrid(this.movingGridcurrentX - this.movingGridPrevX);

	}

	if (this.draggin && this.enableDraggin) {
		switch (this.dragTarget.className.animVal) {
			case this.columnName_name_class :

				var id = (this.dragTarget.id.replace(this.column_id_prefix, ""));

				if (this.checkRangeForMovingColumns(this.selectedColumn,
						columnOver)) {
					this.dragTarget._translateX += mouse.x - this.prevMouse.x;
					var despX = (mouse.x - this.prevMouse.x);
					var transform = 'translate(' + despX + ', ' + 65 + ')';

					document.getElementById(this.column_id_prefix + id)
							.setAttributeNS(null, "transform",
									"translate(" + this.offsetGrid + ")");
					document.getElementById(this.column_id_prefix + id)
							.setAttributeNS(null, "x",
									(mouse.x - this.cellWidth / 2));
					this.doOverEffect(columnOver, rowOver);
				}
				break;
			default :
				break;
		}
	}
};

GridView.prototype.movingOpacity = function(over) {
	var selectedColumn = this.dragTarget.id.replace(this.column_id_prefix, "");
	document.getElementById(this.columnName_id_prefix + selectedColumn)
			.setAttributeNS(null, "opacity", "1");
	document.getElementById(this.columnName_id_prefix + over).setAttributeNS(
			null, "opacity", "1");
	try {
		document.getElementById(this.columnName_id_prefix + parseInt(over - 1)).setAttributeNS(null, "opacity", "0.5");
		document.getElementById(this.columnName_id_prefix + parseInt(over + 1)).setAttributeNS(null, "opacity", "0.5");
	} catch (err) {
	}
};

GridView.prototype.removeOpacity = function() {
	for (var i = 0; i < this.columnNames.length; i++) {
		document.getElementById(this.column_id_prefix + i).setAttributeNS(null,
				"opacity", "1");
	}
};

GridView.prototype.doOpacity = function() {
	var selectedColumn = this.dragTarget.id.replace(this.column_id_prefix, "");

	if (this.columnNames == null)
		return;

	for (var i = 0; i < this.columnNames.length; i++) {
		if (i != selectedColumn) {
			document.getElementById(this.column_id_prefix + i).setAttributeNS(
					null, "opacity", "0.5");
		}
	}
};

GridView.prototype.getIndexColumnID = function(id) {
	for (var i = 0; i < this.columnIndex.length; i++) {
		if (this.columnIndex[i] == id)
			return i;
	}
};

GridView.prototype.onmousedown = function(e, view) {
	// if (e.button == 0) {
	// if (this.dragginGrid) {
	// this.dragginGrid = false;

	// this.previousGridOffset = this.gridOffset;
	// return;

	// }

	// if (this.draggin && this.enableDraggin) {
	// switch (this.dragTarget.className.animVal) {
	// case this.columnName_name_class:

	// var selectedColumn = this
	// .getIndexColumnID(this.dragTarget.id);
	// var overColumn = this.getColumnOver(e);
	// this.selectedColumn = selectedColumn;
	// this.overColumn = overColumn;

	// if (this.overColumn < 0)
	// this.overColumn = 0;
	// if (this.overColumn > this.columnNames.length - 1)
	// this.overColumn = this.columnNames.length - 1;

	// if (selectedColumn != overColumn) {
	// if (this.checkRangeForMovingColumns(
	// this.selectedColumn, this.overColumn)) {
	// this.moveClientSideColumnNames(this.selectedColumn,
	// this.overColumn);
	// }
	// }
	this.moveColumnsEvent.notify();

	// this.removeOpacity();
	// this.clearColumnsName();

	// this.renderClientSideColumnNames(this.columnConfiguration);
	// this.recolocateColumns();

	// break;
	// }
	// this.draggin = false;
	// this.dragTarget = null;
	// } else {
	// if (e.target.className.animVal == this.columnName_name_class) {
	// var overColumn = this.getColumnOver(e);

	// this.draggin = true;
	// this.dragTarget = e.target;
	// this.moveOnTop(this.dragTarget);

	// var selectedColumnID = this.column_id_prefix+
	// this.dragTarget.id.replace(this.column_id_prefix,"");
	// this.originColumnX =
	// document.getElementById(selectedColumnID).getAttribute("x");
	// this.originColumnID = document.getElementById(selectedColumnID).id;

	// this.selectedColumn = overColumn;

	// if (this.enableDraggin)
	// this.doOpacity();
	// }

	// if (e.target.className.animVal == this.columnHeaderName_name_class) {

	// var mouse = getMouseCoords(e, this._svg);
	// this.movingGridPrevX = mouse.x;
	// this.dragginGrid = true;

	// }

	// }
	// }

	// if (e.button == 2)
	// this.draggin = false;
	// this.prevMouse = getMouseCoords(e, this._svg);
};

GridView.prototype.onmouseup = function(e, view) {
	var mouse = getMouseCoords(e, this._svg);
	var rowOver = this.getRowOver(e);
	var columnOver = this.getColumnOver(e);
	if (this.data != null) {
		if (rowOver < this.rowNames.length) {
			this.clickOnCellGrid(rowOver, columnOver, e);
		}
	}

};

GridView.prototype.clickOnCellGrid = function(rowOver, columnOver, event) {
	this.rowSelected = rowOver;
	this.columnSelected = columnOver;

	if ((rowOver < 0) || (columnOver < 0) || (rowOver > this.rowNames.length) || (columnOver > this.columnNames.length)) {
		return;
	}

	if (this.columnIndex[columnOver] == null)
		return;
	var imageId = this.images[rowOver][this.columnIndex[columnOver].replace(
			this.column_id_prefix, "")];
	window.location.href = "viewResults.do?reqCode=viewJpegImage&imageId=" + imageId;
};

GridView.prototype.moveOnTop = function(elem) {
	this._svg.appendChild(elem);
};

GridView.prototype.getRowOver = function(e) {
	var mouse = getMouseCoords(e, this._svg);

	// return Math.floor(((mouse.y + (this.cellHeight / 2) - this.topGrid) /
	// this.cellHeight));
	return Math.floor(((mouse.y - this.topGrid) / this.cellHeight));
};

GridView.prototype.getColumnOver = function(e) {
	var mouse = getMouseCoords(e, this._svg);
	// return Math.floor(((mouse.x + (this.cellWidth / 2) - this.left -
	// this.gridOffset) / this.cellWidth));
	return Math
			.floor(((mouse.x - this.left - this.gridOffset) / this.cellWidth));
};

// Clearing methods
GridView.prototype.clearCanvas = function() {
	this.clearRowsName();
	this.clearColumnsName();
	this.clearTitle();
	this.clearColorRange();
	this.clearAxisYName();
	this.clearAxisXName();
};

GridView.prototype.clearGrid = function(a, b) {
	if (a > b) {
		var aux = a;
		a = b;
		b = aux;
	}
	for (var i = a; i <= b; i++) {
		var element = document.getElementById(this.column_id_prefix + i);
		if (element != null){
			this._svg.removeChild(element);
		}
		element = document.getElementById(this.columnName_id_prefix + i);
		if (element != null)
			this._svg.removeChild(element);
		element = document.getElementById(this.row_id_prefix + i);
		if (element != null){
			this._svg.removeChild(element);
		}
		element = document.getElementById(this.rowName_id_prefix + i);
		if (element != null)
			this._svg.removeChild(element);
	}
};

GridView.prototype.clearColumnsName = function() {
	var texts = this._svg.getElementsByTagName(this.id+"_text");
	for (var i = 0; i < this.columnNames.length; i++) {
		for (var j = 0; j < texts.length; j++) {
			if (texts[j].id == this.columnName_id_prefix + i) {
				this._svg.removeChild(texts[j]);
			}
		}
	}
};

GridView.prototype.clearRowsName = function() {
	var texts = this._svg.getElementsByTagName(this.id+"_text");
	for (var i = 0; i < this.rowNames.length; i++) {
		for (var j = 0; j < texts.length; j++) {
			if (texts[j].id == this.rowName_id_prefix + i) {
				this._svg.removeChild(texts[j]);
			}
		}
	}
};

GridView.prototype.clearTitle = function() {
	var element = document.getElementById(this.id+"_title");
	if (element != null)
		this._svg.removeChild(element);
};

GridView.prototype.clearAxisXName = function() {
	var element = document.getElementById(this.id+"_axisXName");
	if (element != null)
		this._svg.removeChild(element);
};

GridView.prototype.clearAxisYName = function() {
	var element = document.getElementById(this.id+"_axisYName");
	if (element != null)
		this._svg.removeChild(element);
};

GridView.prototype.clearColorRange = function() {
	var element = document.getElementById(this.id+"_minValue");
	if (element != null)
		this._svg.removeChild(element);
	element = document.getElementById(this.id+"_maxValue");
	if (element != null)
		this._svg.removeChild(element);
	var i = 0;
	element = document.getElementById(this.id+"_rect_" + i);
	while (element != null) {
		this._svg.removeChild(element);
		i++;
		element = document.getElementById(this.id+"_rect_" + i);
	}
};

GridView.prototype.clearBackgroundImage = function() {
	var element = document.getElementById("backgroundImage");
	if (element != null)
		this._svg.removeChild(element);
};
// Rendering methods

GridView.prototype.renderColorRanger = function(top, left, minValue, maxValue,
		colorRangeManager, cellWidth, cellHeight, column_id_prefix,
		column_name_class, id) {
	var nb = colorRangeManager.getLen();
	var svgns = "http://www.w3.org/2000/svg";
	var attributes = [['id', id+"_minValue"], ['class', column_name_class],
			['fill', '#000000'], ['font-size', this._fontTitleSize + 'px']];
	SVG.drawText(left, top, minValue, this._svg, attributes);
	for (var i = 0; i < nb; i++) {
		var color = colorRangeManager.getColorRange(i);
		// SVG.drawText(left + i*cellWidth , top , '&#9608;', this._svg,
		// attributes);
		var rect = document.createElementNS(svgns, 'rect');
		rect.setAttributeNS(null, 'x', left + i * cellWidth);
		rect.setAttributeNS(null, 'y', top);
		rect.setAttributeNS(null, 'height', cellWidth);
		rect.setAttributeNS(null, 'width', cellHeight);
		rect.setAttributeNS(null, 'fill', color);
		rect.setAttributeNS(null, 'id', id+"_rect_" + i);
		this._svg.appendChild(rect);
	}
	attributes = [['id', id+"_maxValue"], ['class', column_name_class],
			['fill', '#000000'], ['font-size', this._fontTitleSize + 'px']];
	SVG.drawText(left + (nb) * cellWidth, top, maxValue, this._svg, attributes);
};

GridView.prototype.renderTitle = function(top, left, title, dom_id_prefix,
		dom_name_class) {
	this.title = title;
	this.topGrid = top;

	var attributes = [['id', dom_id_prefix], ['class', dom_name_class],
			['fill', '#000000'], ['id', dom_id_prefix],
			['font-size', this._fontTitleSize + 'px']];
	SVG.drawText(left + 100, top, title, this._svg, attributes);
};

GridView.prototype.renderAxisYName = function(top, left, axisYName,
		dom_id_prefix, dom_name_class) {
	this.axisYName = axisYName;
	var attributes = [['id', dom_id_prefix], ['class', dom_name_class],
			['fill', '#000000'], ['id', dom_id_prefix],
			['font-size', this._fontSize + 'px']];
	SVG.drawText(left, top, axisYName, this._svg, attributes);
};

GridView.prototype.renderAxisXName = function(top, left, axisXName,
		dom_id_prefix, dom_name_class) {
	this.axisXName = axisXName;
	var attributes = [['id', dom_id_prefix], ['class', dom_name_class],
			['fill', '#000000'], ['id', dom_id_prefix],
			['font-size', this._fontSize + 'px']];
	SVG.drawText(left, top, axisXName, this._svg, attributes);
};

GridView.prototype.renderRowNames = function(top, left, cellHeight, rowNames,
		dom_id_prefix, dom_name_class) {
	this.rowNames = rowNames;
	this.rowName_id_prefix = dom_id_prefix;
	this.rowName_name_class = dom_name_class;
	// this.topGrid = top;
	for (var i = 0; i < rowNames.length; i++) {

		var attributes = [['id', dom_id_prefix + i], ['class', dom_name_class],
				['fill', '#000000'], 
				['font-size', this._fontSize + 'px']];
		SVG.drawText(left, cellHeight * i + top + (cellHeight / 2),
				rowNames[i], this._svg, attributes);
	}
};

GridView.prototype.renderClientSideColumnNames = function(columnConfiguration) {
	this.renderColumnNames(columnConfiguration.top, columnConfiguration.left,
			columnConfiguration.a, columnConfiguration.b,
			columnConfiguration.cellWidth, this.columnNames,
			columnConfiguration.dom_id_prefix,
			columnConfiguration.dom_name_class);

};

GridView.prototype.renderColumnNames = function(top, left, a, b, cellWidth,
		columnNames, dom_id_prefix, dom_name_class) {
	this.columnConfiguration = new ColumnConfiguration(top, left, a, b,
			cellWidth, columnNames, dom_id_prefix, dom_name_class);
	if (a > b) {
		var aux = a;
		a = b;
		b = aux;
	}
	this.left = left;
	this.columnNames = columnNames;
	this.columnName_id_prefix = dom_id_prefix;
	this.columnHeaderName_name_class = dom_name_class;

	for (var i = a; i <= b; i++) {
		var attributes = [
				[
						'transform',
						'rotate(-45, ' + 
								parseInt(left + i * cellWidth + (cellWidth / 2)) + ',' + 
								parseInt(top) + '), translate(0,' + 
								this.gridOffset + ')'],
				['id', dom_id_prefix + i], ['class', dom_name_class],
				['cursor', 'move'], ['fill', '#000000'],
				['id', dom_id_prefix + i], ['font-size', this._fontSize + 'px']];
		SVG.drawText(left + (i - 1) * cellWidth + (cellWidth / 2), top, columnNames[i], this._svg, attributes);

	}

	// this.columnNames = columnNames;
	// this.columnName_id_prefix = dom_id_prefix;
	// this.columnHeaderName_name_class = dom_name_class;
	// this.topGrid = top;

	// for ( var i = 0; i < columnNames.length; i++) {

	// var attributes = [ [ 'id', dom_id_prefix + i ],[ 'class', dom_name_class
	// ], [ 'fill', '#000000' ],
	// [ 'id', dom_id_prefix + i ],[ 'font-size', this._fontSize + 'px']];
	// SVG.drawText(left + (i-1) * cellWidth + (cellWidth / 2),
	// top,columnNames[i], this._svg, attributes);
	// }

};

GridView.prototype.renderGrid = function(top, left, a, b, cellWidth,
		cellHeight, spanCell, column_id_prefix, column_name_class, data,
		scores, positions, images) {
	this.cellWidth = cellWidth;
	this.cellHeight = cellHeight;
	this.data = data;
	this.scores = scores;
	this.images = images;
	this.positions = positions;
	this.column_id_prefix = column_id_prefix;
	this.column_class = column_name_class;
	this._canvas.width = cellWidth + 1;
	this._canvas.height = cellHeight * this.data.length;
	this.topGrid = top;

	this.renderMatrixGridByCanvas(top, left, a, b, cellWidth, cellHeight,
			spanCell, column_id_prefix, column_name_class, data);

};

GridView.prototype.renderMatrixGridByCanvas = function(top, left, a, b,
		cellWidth, cellHeight, spanCell, column_id_prefix, column_name_class,
		data) {
	this.column_id_prefix = column_id_prefix;
	this.columnName_name_class = column_name_class;

	if (a > b) {
		var aux = a;
		a = b;
		b = aux;
	}
	this._canvas.height = this._canvas.height + 1;
	for (var j = a; j <= b; j++) {

		this._canvas.width = this._canvas.width;
		for (var i = 0; i < this.data.length; i++) {

			this._canvas.getContext('2d').shadowOffsetX = 1;
			this._canvas.getContext('2d').shadowOffsetY = 1;
			this._canvas.getContext('2d').shadowBlur = 0;
			var rectangle = new Rectangle(cellHeight * i + 1, 1, cellHeight - spanCell, cellWidth - spanCell);
			rectangle.setColor(data[i][j]);
			rectangle.render(this._canvas.getContext('2d'));
		}
		this.columnIndex.push(column_id_prefix + j);
		this.columnPosX.push((left + j * cellWidth));

		CanvasToSVG.convert(this._canvas, this._svg, (left + j * cellWidth),
				top + 1, column_id_prefix + j, [["cursor", "pointer"],
						["class", column_name_class]]);
	}

	this._canvas.width = 0;
	this._canvas.height = 0;

};

GridView.prototype.renderMatrixGridBySVG = function(top, left, a, b, cellWidth,
		cellHeight, spanCell, column_id_prefix, column_name_class, data) {
	if (a > b) {
		var aux = a;
		a = b;
		b = aux;
	}
	var obj = 0;
	for (var j = a; j <= b; j++) {
		this._canvas.width = this._canvas.width;
		for (var i = 0; i < this.data.length; i++) {
			spanCell = 0;
			SVG.drawRectangle(left + j * cellWidth, cellHeight * i + top,
					data[i][j], cellHeight - spanCell, cellWidth - spanCell, 1,
					column_id_prefix + j, column_name_class, this._svg);

			obj++;
		}
	}

	this._canvas.width = 0;
	this._canvas.height = 0;

};

GridView.prototype.loadBackgroundImage = function(backgroundImage, top, left) {
	if (backgroundImage) {
		var svgimg = document.createElementNS('http://www.w3.org/2000/svg',
				'image');
		svgimg.setAttributeNS(null, 'height', '197');
		svgimg.setAttributeNS(null, 'width', '316');
		svgimg.setAttributeNS('http://www.w3.org/1999/xlink', 'href',
				backgroundImage);
		svgimg.setAttributeNS(null, 'x', left);
		svgimg.setAttributeNS(null, 'y', top);
		svgimg.setAttributeNS(null, 'visibility', 'visible');
		svgimg.setAttributeNS(null, 'id', 'backgroundImage');
		svgimg.setAttributeNS(null, 'opacity', 0.5);
		this._svg.appendChild(svgimg);
	}
};

function ColumnConfiguration(top, left, a, b, cellWidth, columnNames,
		dom_id_prefix, dom_name_class) {
	this.top = top;
	this.left = left;
	this.a = a;
	this.b = b;
	this.cellWidth = cellWidth;
	this.columnNames = columnNames;
	this.dom_id_prefix = dom_id_prefix;
	this.dom_name_class = dom_name_class;
}

// hopefully return the mouse coordinates inside parent element
function getMouseCoords(e, parent) {
	var x, y;

	muna = parent;

	if (document.getBoxObjectFor) {
		// sorry for the deprecated use here, but see below
		var boxy = document.getBoxObjectFor(parent);
		x = e.pageX - boxy.x;
		y = e.pageY - boxy.y;
	} else if (parent.getBoundingClientRect) {
		// NOTE: buggy for FF 3.5:
		// https://bugzilla.mozilla.org/show_bug.cgi?id=479058
		/*
		 * I have also noticed that the returned coordinates may change
		 * unpredictably after the DOM is modified by adding some children to
		 * the SVG element
		 */
		// var lefttop = parent.getBoundingClientRect();
		// console.log(parent.id + " " + lefttop.left + " " + lefttop.top);
		// console.log(e.clientX+" "+e.clientY);
		// console.log(parent.offsetLeft+" "+parent.offsetTop);
		// x = e.clientX - Math.floor(lefttop.left);
		// y = e.clientY - Math.floor(lefttop.top);
		var pt = parent.createSVGPoint();
		pt.x = e.clientX;
		pt.y = e.clientY;
		var cursorpt = pt.matrixTransform(parent.getScreenCTM().inverse());
		x = cursorpt.x;
		y = cursorpt.y;
		// console.log("(" + cursorpt.x + ", " + cursorpt.y + ")");

	} else {
		x = e.pageX - (parent.offsetLeft || 0);
		y = e.pageY - (parent.offsetTop || 0);
	}

	// console.log(x +" " +y+" " );
	return {
		x : x,
		y : y
	};
}

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

// main panel with mesh map
function MapPanel(args) {
	var _this = this;
	this.width = 1350;
	this.height = 500;
	this.mapTitle = 'Map title';
	this.minValue = 0;
	this.maxValue = 1000;
	this.cellWidthValue = 30;
	
	this.id = 0;

	this.grid = null;
	this.snapshot = null;
	this.axisXName = null;
	this.axisYName = null;

	this.stepX = 0.05;
	this.stepY = 0.05;

	this.nbDecX = this.getNbDec("" + this.stepX);
	this.nbDecY = this.getNbDec("" + this.stepY);

	this.axisXName = "GridIndexY";
	this.axisYName = "GridIndexZ";

	this.colorRangeManager = null;

	this.data = null;
	this.positions = null;
	this.images = null;
	this.meshValues = null;
	this.scaleDataX = null;
	this.scaleDataY = null;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	// Define the model for a Criteria
	Ext.define('CriteriaPlotModel', {
			extend : 'Ext.data.Model',
			fields : [{
					type : 'string',
					name : 'id'
				}, {
					type : 'string',
					name : 'name'
				}]
		});
}

MapPanel.prototype.getNbDec = function (value) {
	var sep = value.indexOf(".") + 1;
	return value.length - sep;
};

MapPanel.prototype.getValue = function (value, nbDec) {
	if (value) { 
		return (value).toFixed(nbDec);
	}
	else { 
		return null;
	}
};

MapPanel.prototype.getValueRound = function (value, nbDec, step) {
	var val = this.getValue(value, nbDec);
	var n = 1 / (step * 2);
	return (Math.floor((val * n)) / n);
};

MapPanel.prototype.getScaleDataX = function (meshData) {
	if (meshData.length > 0) {
		// var x = this.getValueRound(meshData[0].phiY, this.nbDecX,
		// this.stepX);
		var x = meshData[0].motorPosition.gridIndexY;
		var min = x;
		var max = x;
		for (var i = 1; i < meshData.length; i++) {
			// var x = this.getValueRound(meshData[i].phiY, this.nbDecX,
			// this.stepX);
			x = meshData[i].motorPosition.gridIndexY;
			if (x < min) {
				min = x;
			} else if (x > max) {
				max = x;
			}
		}
		// return array
		var array = [];
		var v = 0;
		if (min) {
			v = min;
		}
		while (v < max) {
			array.push(v);
			v = Number(v) + 1;
			// v = Number(v)+ Number(this.stepX);
			// v = Number(v.toFixed(this.nbDecX));
		}
		if (max) { 
			array.push(max);
		}
		else { 
			array.push(0);
		}
		return array;
	}
	return [];
};

MapPanel.prototype.getScaleDataY = function (meshData) {
	if (meshData.length > 0) {
		// var y = this.getValueRound(meshData[0].phiZ, this.nbDecY,
		// this.stepY);
		var y = meshData[0].motorPosition.gridIndexZ;
		var min = y;
		var max = y;
		for (var i = 1; i < meshData.length; i++) {
			// var y = this.getValueRound(meshData[i].phiZ, this.nbDecY,
			// this.stepY);
			y = meshData[i].motorPosition.gridIndexZ;
			if (y < min) {
				min = y;
			} else if (y > max) {
				max = y;
			}
		}
		// return array
		var array = [];
		var v = 0;
		if (min) {
			v = min;
		}
		while (v < max) {
			array.push(v);
			v = Number(v) + 1;
			// v = Number(v) + Number(this.stepY);
			// v = Number(v.toFixed(this.nbDecY));
		}
		if (max) {
			array.push(max);
		} else {
			array.push(0);
		}
		return array;
	}
	return [];
};

MapPanel.prototype.getValueForCriteria = function (d, criteria) {
	if (criteria == "totalSpots") {
		return d.spotTotal;
	} else if (criteria == "goodBraggCandidates") {
		return d.goodBraggCandidates;
	} else if (criteria == "method1Res") {
		return d.method1Res;
	} else if (criteria == "method2Res") {
		return d.method2Res;
	} else if (criteria == "totalIntegratedSignal") {
		return d.totalIntegratedSignal;
	}else if (criteria == "dozor_score") {
		return d.dozor_score;
	}
};

MapPanel.prototype.buildDataCriteria = function (criteria) {
	var nbRow = this.meshValues.length;
	this.data = new Array(nbRow);
	for (var i = 0; i < nbRow; i++) {
		var nbColumn = this.meshValues[i].length;
		this.data[i] = new Array(nbColumn);
		for (var j = 0; j < nbColumn; j++) {
			var d = this.meshValues[i][j];
			this.data[i][j] = this.getValueForCriteria(d, criteria);
		}
	}
};

MapPanel.prototype.buildData = function(meshData) {
	this.meshValues = [];
	if (meshData) {
		if (meshData.gridInfo && meshData.gridInfo.steps_x) {
			this.stepX = meshData.gridInfo.steps_x;
			this.nbDecX = this.getNbDec("" + this.stepX);
		}
		if (meshData.gridInfo && meshData.gridInfo.steps_y) {
			this.stepY = meshData.gridInfo.steps_y;
			this.nbDecY = this.getNbDec("" + this.stepY);
		}
		// scale: todo: how to retrieve the scale??
		this.scaleDataX = this.getScaleDataX(meshData);
		this.scaleDataY = this.getScaleDataY(meshData);
		var nbX = this.scaleDataX.length; // nb column
		var nbY = this.scaleDataY.length; // nb row
		// init array
		this.meshValues = new Array(nbY);
		this.positions = new Array(nbY);
		this.images = new Array(nbY);
		for (var i = 0; i < nbY; i++) {
			this.meshValues[i] = new Array(nbX);
			this.positions[i] = new Array(nbX);
			this.images[i] = new Array(nbX);
			for (var j = 0; j < nbX; j++) {
				this.meshValues[i][j] = this.getDefaultMeshData();
				this.positions[i][j] = "Unknown";
				this.positions[i][j] = "";
			}
		}
		// dispatch data
		for (var i = 0; i < meshData.length; i++) {
			var d = meshData[i];
			// var x = this.getValue(d.phiY, this.nbDecX);
			// var y = this.getValue(d.phiZ, this.nbDecY);
			var x = d.motorPosition.gridIndexY;
			var y = d.motorPosition.gridIndexZ;
			var pos = this.getCoordPos(x, y);
			this.meshValues[pos[0]][pos[1]] = d;
			var phiY = "";
			if (d.motorPosition.phiY) {
				phiY = d.motorPosition.phiY.toFixed(3);
			}
			var sampY = "";
			if (d.motorPosition.sampY) {
				sampY = d.motorPosition.sampY.toFixed(3);
			}
			var sampX = "";
			if (d.motorPosition.sampX) {
				sampX = d.motorPosition.sampX.toFixed(3);
			}
			this.positions[pos[0]][pos[1]] = "phiY=" + phiY + " sampX=" + sampX + " sampY=" + sampY;
			this.images[pos[0]][pos[1]] = d.imageId;
		}
		// build data
		this.buildDataCriteria("dozor_score");
	}
};

MapPanel.prototype.getDefaultMeshData = function() {
	var meshData = [];
	meshData.dataCollectionId = -1;
	meshData.imageId = -1;
	meshData.imageQualityIndicatorsId = -1;
	meshData.imageFileLocation = "";
	meshData.imageFilePresent = false;
	meshData.imagePrefix = "";
	meshData.dataCollectionNumber = 0;
	meshData.motorPosition = [];
	meshData.motorPosition.phiX = 0;
	meshData.motorPosition.phiY = 0;
	meshData.motorPosition.phiZ = 0;
	meshData.motorPosition.sampX = 0;
	meshData.motorPosition.sampY = 0;
	meshData.motorPosition.omega = 0;
	meshData.motorPosition.kappa = 0;
	meshData.motorPosition.phi = 0;
	meshData.motorPosition.gridIndexY = 0;
	meshData.motorPosition.gridIndexZ = 0;
	meshData.gridInfo = [];
	meshData.gridInfo.xOffset = 0;
	meshData.gridInfo.yOffset = 0;
	meshData.gridInfo.dx_mm = 0;
	meshData.gridInfo.dy_mm = 0;
	meshData.gridInfo.steps_x = 0;
	meshData.gridInfo.steps_y = 0;
	meshData.gridInfo.meshAngle = 0;
	meshData.spotTotal = 0;
	meshData.goodBraggCandidates = 0;
	meshData.method1Res = 0;
	meshData.method2Res = 0;
	meshData.totalIntegratedSignal = 0;
	meshData.dozor_score = 0;
	return meshData;
};

MapPanel.prototype.getCoordPos = function(x, y) {
	var pos = new Array(2);
	var nbX = this.scaleDataX.length;
	var nbY = this.scaleDataY.length;
	var i = 0;
	while (x > this.scaleDataX[i] && i < nbX) {
		i++;
	}
	var j = 0;
	while (y > this.scaleDataY[j] && j < nbY) {
		j++;
	}

	pos[0] = j < nbY ? j : (nbY - 1);
	pos[1] = i < nbX ? i : (nbX - 1);
	// console.log("x="+pos[0]+", y="+pos[1]);
	return pos;
};

MapPanel.prototype.saveSettings = function(params) {
	var _this = this;
	this.mapTitle = params.mapTitle;
	this.maxValue = params.maxValue;
	this.minValue = params.minValue;
};

MapPanel.prototype._refresh = function() {
	var dataRange = this._getDataRange();
	this.colorRangeManager.loadData(dataRange, this.reverseOrder);
	this.refresh(this.data, this.positions, this.images, this.scaleDataX,
			this.scaleDataY, this.colorRangeManager, this.mapTitle,
			this.axisXName, this.axisYName, this.cellWidthValue);
};

MapPanel.prototype.sliderChange = function() {
	this.cellWidthValue = this.slider.getSliderValue();
	this._refresh();
};

MapPanel.prototype.getPanel = function(meshData, snapshot, id) {
	var _this = this;
	_this.id = id;
	this.snapshot = snapshot;
	// create color range object
	this.colorRangeManager = new ColorRangeManager();

	// get Data
	this.buildData(meshData);
	this.maxValue = this._calculateMaxValue();
	//this.minValue = this._calculateMinValue();

	// load map
	var dataRange = _this._getDataRange();
	this.colorRangeManager.loadData(dataRange, this.reverseOrder);

	// slider
	this.slider = new SliderWidget('slider');
	// this.slider.draw();
	this.slider.sliderChange.attach(function() {
				_this.sliderChange();
			});

	var diffHandler = function(btn) {
		if (btn.id == _this.id+'_settingsBtn') {
			var mapSettingsWindow = new MapSettingsWindow();
			mapSettingsWindow.onSaved.attach(function(evt, mapTitle, maxValue, minValue) {
						_this.saveSettings(mapTitle, maxValue, minValue);
						_this._refresh();

					});
			mapSettingsWindow.draw(_this.mapTitle, _this.maxValue, _this.minValue);
			return;
		}
		if (btn.id == _this.id+'_saveAsImageBtn') {
			// var canvas = document.getElementById("canvas");
			// var img = canvas.toDataURL("image/png");
			// var w=window.open('about:blank','image from canvas');
			// w.document.write("<img src='"+img+"' alt='from canvas'/>");
			_this.saveAsImage();
			return;
		}
		if (btn.id == _this.id+'_loadImgBtn') {
			_this.loadImage();
			return;
		}
		if (btn.id == _this.id+'_unloadImgBtn') {
			_this.unloadImage();
			return;
		}
		return alert('not yet implemented');
	};

	var enabledButton = (snapshot && snapshot.filePresent);

	var saveAsImgButton = new Ext.Button({
				id : _this.id+'_saveAsImageBtn',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var loadImgButton = new Ext.Button({
				id : _this.id+'_loadImgBtn',
				text : '',
				disabled : !enabledButton,
				tooltip : 'Load the crystal image',
				icon : '../images/image_add.png',
				handler : diffHandler
			});
	var unloadImgButton = new Ext.Button({
				id : _this.id+'_unloadImgBtn',
				text : '',
				disabled : !enabledButton,
				tooltip : 'Unload the crystal image',
				icon : '../images/image_remove.png',
				handler : diffHandler
			});
	var settingsButton = new Ext.Button({
				id : _this.id+'_settingsBtn',
				text : '',
				tooltip : 'map settings...',
				icon : '../images/cog.png',
				handler : diffHandler
			});

	var criteria = [{
				"id" : "dozor_score",
				"name" : "Dozor Score"
			},
			{
				"id" : "totalIntegratedSignal",
				"name" : "Total Integrated Signal"
			}, {
				"id" : "goodBraggCandidates",
				"name" : "Good bragg candidates"
			}, {
				"id" : "totalSpots",
				"name" : "Total Spots"
			}, {
				"id" : "method1Res",
				"name" : "Method 1 resolution"
			}, {
				"id" : "method2Res",
				"name" : "Method 2 resolution"
			}];
	var criteriaPlotStore = Ext.create('Ext.data.Store', {
				autoDestroy : true,
				model : 'CriteriaPlotModel',
				data : criteria
			});
	var cbPlot = Ext.create('Ext.form.field.ComboBox', {
				fieldLabel : 'Select Criteria for plotting',
				displayField : 'name',
				width : 300,
				labelWidth : 140,
				store : criteriaPlotStore,
				queryMode : 'local',
				typeAhead : true,
				listeners : {
					// public event change - when selection1 dropdown is changed
					change : function(field, newValue, oldValue) {
						_this.updateCriteria(newValue);
					}
				}
			});
	cbPlot.setValue("dozor_score");

	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton, loadImgButton, unloadImgButton,
						settingsButton, cbPlot, this.slider.draw()]
			});

	_this.panel = Ext.create('Ext.Panel', {
		layout : 'fit',
		tbar : diffToolBar,
		width:"100%",
		listeners : {
			'afterrender' : function(thisCmp) {
				_this.postRender();

			}
		},
		html : "<div style='position:left; ' id='scaleColorMapPanel_"+id+"'></div> <div id='container_"+id+"'></div><div id='canvasarea_"+id+"' style='text-align:right'><canvas id='canvas_"+id+"' width='0' height='0' style='position:absolute; top:1000; left:1000;'>No map found</canvas></div>"
	});
	return _this.panel;
};

MapPanel.prototype.postRender = function() {
	var _this = this;
	var canvas = document.getElementById("canvas_"+_this.id+"");
	var svg = document.getElementById("svg_"+_this.id+"");
	var rowsCount = _this.data.length;
	var columnsCount = 0;
	if (_this.data[0])
		columnsCount = _this.data[0].length;

	var rowsName = _this.getRowsNamesBDA(rowsCount);
	var columnsNames = _this.getColumnsNamesBDA(columnsCount);

	_this.grid = new Grid(0, 20, (rowsName.length * _this.cellWidthValue),
			(columnsNames.length * _this.cellWidthValue), canvas, document
					.getElementById("container_"+_this.id+""), _this.id);
	_this.grid.fill(rowsName, columnsNames, _this.getDataBDA(rowsCount,
					columnsCount), _this.data, _this.positions, _this.images,
			this.mapTitle, _this.axisXName, _this.axisYName,
			_this.cellWidthValue, _this.minValue, _this.maxValue, _this.colorRangeManager);
	_this.grid.model.render();
	_this.grid.refresh();

	// _this.colorRangeManager.createColorGradient('scaleColorMapPanel',
	// _this.colorRangeManager.getMaxValue());

};

MapPanel.prototype._getDataRange = function() {
	var _this = this;
	var dataRange = {
		min : 0,
		max : 0
	};
	dataRange.min = _this.minValue;
	dataRange.max = _this.maxValue;
	return dataRange;
};


MapPanel.prototype._calculateMaxValue = function() {
	var max = 0;
	var nbRow = this.data.length;
	for (var i = 0; i < nbRow; i++) {
		var nbCol = this.data[i].length;
		for (var j = 0; j < nbCol; j++) {
			max = Math.max(max, this.data[i][j]);
		}
	}
	return max;
};


MapPanel.prototype._calculateMinValue = function() {
	var min = 0;
	var nbRow = this.data.length;
	if (nbRow > 0 && this.data[0].length > 0){
		min = this.data[0][0];
	}
	for (var i = 0; i < nbRow; i++) {
		var nbCol = this.data[i].length;
		for (var j = 0; j < nbCol; j++) {
			min = Math.min(min, this.data[i][j]);
		}
	}
	return min;
};


MapPanel.prototype.updateCriteria = function(newCriteria) {
	var criteria = "";
	if (newCriteria == "Total Integrated Signal") {
		criteria = "totalIntegratedSignal";
	} else if (newCriteria == "Good bragg candidates") {
		criteria = "goodBraggCandidates";
	} else if (newCriteria == "Total Spots") {
		criteria = "totalSpots";
	} else if (newCriteria == "Method 1 resolution") {
		criteria = "method1Res";
	} else if (newCriteria == "Method 2 resolution") {
		criteria = "method2Res";
	}else if (newCriteria == "Dozor Score") {
		criteria = "dozor_score";
	}
	if (criteria != "") {
		this.buildDataCriteria(criteria);
		this.maxValue = this._calculateMaxValue();
		//this.minValue = this._calculateMinValue();
		this.reverseOrder = false;
		if (criteria == "method1Res" || criteria == "method2Res"){
			this.reverseOrder = true;
		}
		this._refresh();
	}
};

MapPanel.prototype.saveAsImage = function() {
	// var svg = document.getElementById('container');
	// var svg_xml = (new XMLSerializer).serializeToString(svg);
	// open("data:image/svg+xml," + encodeURIComponent(svg_xml));
	svgenie.save("meshSvgId_"+this.id, {
				name : "mesh.png"
			});
};

MapPanel.prototype.refresh = function(data, positions, images, dataScaleX,
		dataScaleY, colorRangeManager, title, axisXName, axisYName,
		cellWidthValue) {
	var _this = this;

	_this.data = data;
	_this.positions = positions;
	_this.images = images;
	_this.colorRangeManager = colorRangeManager;

	_this.scaleDataX = dataScaleX;
	_this.scaleDataY = dataScaleY;

	_this.mapTitle = title;
	_this.axisXName = axisXName;
	_this.axisYName = axisYName;

	var canvas = document.getElementById("canvas_"+_this.id+"");
	var svg = document.getElementById("svg_"+_this.id+"");
	var rowsCount = _this.data.length;
	var columnsCount = _this.data[0].length;
	var squareSize = cellWidthValue;

	var rowsName = _this.getRowsNamesBDA(rowsCount);
	var columnsNames = _this.getColumnsNamesBDA(columnsCount);
	_this.grid.fill(rowsName, columnsNames, _this.getDataBDA(rowsCount,
					columnsCount), _this.data, _this.positions, _this.images,
			_this.mapTitle, _this.axisXName, _this.axisYName, squareSize,
			_this.colorRangeManager.getMinValue(),
			_this.colorRangeManager.getMaxValue(), _this.colorRangeManager);
	_this.grid.refresh();

	// document.getElementById('scaleColorMapPanel').innerHTML = "";
	// _this.colorRangeManager.createColorGradient('scaleColorMapPanel',
	// _this.colorRangeManager.getMaxValue());
};

MapPanel.prototype.getRowsNamesBDA = function(value) {
	var _this = this;
	var array = [];
	for (var i = 0; i < value; i++) {
		array.push(_this.scaleDataY[i]);
	}
	return array;
};

MapPanel.prototype.getColumnsNamesBDA = function(value) {
	var _this = this;
	var array = [];
	for (var i = 0; i < value; i++) {
		array.push(_this.scaleDataX[i]);
	}
	return array;
};

MapPanel.prototype.getDataBDA = function(rowsCount, columnsCount) {
	var _this = this;
	var data = [];
	for (var i = 0; i < rowsCount; i++) {
		data[i] = [];
	}

	var rowNames = [];
	var columnNames = [];

	for (var j = 0; j < columnsCount; j++) {
		columnNames[j] = "COLUMN_#" + j;
		for (var i = 0; i < rowsCount; i++) {
			data[i][j] = _this.getColorCell(i, j);
			rowNames[i] = "ROW_NAMES_#" + i;
		}
	}

	return data;
};

MapPanel.prototype.getColorCell = function(row, column) {
	var _this = this;
	return this.colorRangeManager.getCellColorString(_this.data[row][column]);
};

MapPanel.prototype.loadImage = function() {
	if (this.snapshot && this.snapshot.filePresent) {
		var url = "imageDownload.do?reqCode=getImageJpgFromFile&file=" + this.snapshot.fileLocation;
		this.grid.loadImage(url);
	}
};

MapPanel.prototype.unloadImage = function() {
	this.grid.unloadImage();
};

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
// define the parameters of the map (title, range, etc) that can be set by the user
function MapSettingsPanel(args) {
	this.actions = new Array();

	if (args != null) {
		if (args.actions != null) {
			this.actions = args.actions;
		}
	}

}

MapSettingsPanel.prototype.getPanel = function(data) {
	var _this = this;
	var mapTitle = data.mapTitle;
	var maxValue = data.maxValue;
	var minValue = data.minValue;
	if (this.panel == null) {
		this.panel = Ext.createWidget('form', {
			bodyPadding : 5,
			width : 60,
			frame : true,
			fieldDefaults : {
				labelAlign : 'left',
				labelWidth : 60
			},
			items : [{
				xtype : 'container',
				layout : 'hbox',
				items : [{
					xtype : 'container',
					flex : 1,
					border : false,
					layout : 'anchor',
					defaultType : 'textfield',
					items : [{

								fieldLabel : 'Map title',
								name : 'mapTitle',
								id : 'mapTitle',
								tooltip : "Title of the map",
								value : mapTitle
							}, {
								xtype : 'container',
								flex : 1,
								layout : 'anchor',
								defaultType : 'textfield',
								items : [{
									xtype : 'numberfield',
									fieldLabel : 'Value max',
									allowNegative : false,
									name : 'maxValue',
									value : maxValue,
									decimalPrecision : 0,
									minValue : 1,
									// maxValue: 3000,
									validator : function(val) {
										if (!Ext.isEmpty(val)) {
											if (val < 0)
												return "Value cannot be negative";
											return true;
										} else {
											return "Value cannot be empty";
										}
									}

								}]
							}//, 
//							{
//								xtype : 'container',
//								flex : 1,
//								layout : 'anchor',
//								defaultType : 'textfield',
//								items : [{
//									xtype : 'numberfield',
//									fieldLabel : 'Value min',
//									allowNegative : false,
//									name : 'minValue',
//									value : minValue,
//									decimalPrecision : 0,
//									minValue : 0,
//									// maxValue: 3000,
//									validator : function(val) {
//										if (!Ext.isEmpty(val)) {
//											if (val < 0)
//												return "Value cannot be negative";
//											return true;
//										} else {
//											return "Value cannot be empty";
//										}
//									}
//
//								}]
//							}
							]
				}]
			}]
		});
	}
	return this.panel;
};

MapSettingsPanel.prototype.getMapTitle = function() {
	var mapTitle = Ext.getCmp(this.panel.getItemId()).getValues().mapTitle;
	return mapTitle;
};

MapSettingsPanel.prototype.getMaxValue = function() {
	var maxValue = Ext.getCmp(this.panel.getItemId()).getValues().maxValue;
	return maxValue;
};


MapSettingsPanel.prototype.getMinValue = function() {
	var minValue = Ext.getCmp(this.panel.getItemId()).getValues().minValue;
	return minValue;
};

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

MapSettingsWindow.prototype.getButtons = GenericWindow.prototype.getButtons;
MapSettingsWindow.prototype._render = GenericWindow.prototype._render;
MapSettingsWindow.prototype._postRender = GenericWindow.prototype._postRender;

// parameters of the mesh map that can be set by the user. Parent is a Generic Window
function MapSettingsWindow(args) {
	this.title = "Map Settings";
	this.height = 200;

	if (args != null) {
		if (args.actions != null) {
			this.actions = args.actions;
		}
	}
	this.onSavedButtonClicked = new Event(this);
	this.form = new MapSettingsPanel();
	GenericWindow.prototype.constructor.call(this, {
				form : this.form,
				title : this.title,
				height : this.height
			});

}

MapSettingsWindow.prototype.getButtons = function() {
	return [];
};

// cancel click: close panel
MapSettingsWindow.prototype.cancel = function() {
	this.panel.close();
};

// save click: save parameters
MapSettingsWindow.prototype.save = function() {
	var params = {
		mapTitle : '',
		maxValue : 0,
		minValue : 0
	};
	params.mapTitle = this.form.getMapTitle();
	params.maxValue = this.form.getMaxValue();
	//params.minValue = this.form.getMinValue();
	params.minValue = 0;
	this.onSaved.notify(params);
	this.panel.close();
};

// display the window
MapSettingsWindow.prototype.draw = function(title, maxValue, minValue) {
	var _this = this;
	var data = {
			mapTitle : title,
			maxValue : maxValue,
			minValue : minValue
		};
	this._render(data);
};

// return the buttons
MapSettingsWindow.prototype.getButtons = function() {
	var _this = this;
	return [{
				text : 'Save',
				handler : function() {
					_this.save();

				}
			}, {
				text : 'Cancel',
				handler : function() {
					_this.panel.close();
				}
			}];
};
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
// panel which contains the mapPanel and the positionGrid
function MeshScanPanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 700;

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
MeshScanPanel.prototype.getPanel = function(meshData, snapshot, id) {
	var _this = this;
	var mapPanel = new MapPanel();
	mapPanel = mapPanel.getPanel(meshData, snapshot, id);

	var positionGrid = new PositionGrid();
	positionGrid = positionGrid.getGrid(meshData);
	
	var items = [];
	items.push(mapPanel);
	items.push(positionGrid);
	

	_this.panel = Ext.create('Ext.Panel', {
				//id : 'meshScanPanel',
				//width : "100%", // panel's width
				//height : this.height,// panel's height
				layout : {
					type : 'vbox'
				},
				items : items
			});

	return _this.panel;
};
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
// tab panel in the workflow page
function MeshTabPanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 700;

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
MeshTabPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	if (data && data.listOfImages ) {
		var nb = data.listOfImages.length;
		for (var i = 0; i < nb; i++) {
			var meshScanPanel = new MeshScanPanel();
			var title = ""+i;
			if (data.listOfCollect && data.listOfCollect.length > i){
				collects = data.listOfCollect[i];
				if (collects.length > 0){
					title = collects[0].imagePrefix;
				}
			}
			var meshData = null;
			if (data.listOfMeshData && data.listOfMeshData.length > i)
				meshData = data.listOfMeshData[i];
			var snapshot = null;
			if (data.listOfSnapshot && data.listOfSnapshot.length > i)
				snapshot = data.listOfSnapshot[i];
				
			items.push({
						tabConfig : {
							title : title
						},
						items : [meshScanPanel.getPanel( meshData, snapshot, i)]
					});
		}
	}

	var tabs = Ext.create('Ext.tab.Panel', {
				layout : 'fit',
				activeTab : 0,
				defaults : {
					bodyPadding : 0
				},
				items : items
			});

	_this.panel = Ext.create('Ext.Panel', {
				layout : 'fit',
				items : [tabs]
			});

	return _this.panel;
};
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
// image wall page for a mesh, with the size mesh
function MeshWallImagePanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 800;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	// image datamodel
	ImageModel = Ext.define('ImageModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'name'
						}, {
							name : 'url'
						}, {
							name : 'target'
						}, {
							name : 'size',
							type : 'float'
						}]
			});

}

// format data
MeshWallImagePanel.prototype.prepareData = function(imageList) {
	this.features = [];
	var listImages = imageList;
	for (var i = 0; i < listImages.length; i++) {
		var o = {};
		var image = o.name = listImages[i].fileName;
		o.url = 'imageDownload.do?reqCode=getImageJpgThumb&imageId='+ listImages[i].imageId;
		o.size = 125; 
		o.target = 'viewResults.do?reqCode=viewJpegImage&imageId=' + 
				listImages[i].imageId + '&previousImageId=' + 
				listImages[i].previousImageId + '&nextImageId=' + 
				listImages[i].nextImageId;
		this.features.push(o);
	}
};


// calculates the nb of columns
MeshWallImagePanel.prototype.getNbCol = function(meshData) {
	if (meshData && meshData.length > 0) {
		
			
		var x = meshData[0].motorPosition.gridIndexY;
		var min = x;
		var max = x;
		for (var i = 1; i < meshData.length; i++) {
			x = meshData[i].motorPosition.gridIndexY;
			if (x < min) {
				min = x;
			} else if (x > max) {
				max = x;
			}
		}
		// return array
		var array = [];
		var v = 0;
		if (min) {
			v = min;
		}
		while (v < max) {
			array.push(v);
			v = Number(v) + 1;
		}
		if (max)
			array.push(max);
		else
			array.push(0);
		return array.length;
	}
};

// returns the position of a data
MeshWallImagePanel.prototype.getGridPosition = function(meshData, imgId) {
	for (var i = 0; i < meshData.length; i++) {
		if (Number((meshData[i].imageId)) === Number(imgId)) {
			var pos = new Array(2);
			pos[0] = meshData[i].motorPosition.gridIndexZ;
			pos[1] = meshData[i].motorPosition.gridIndexY;
			return pos;
		}
	}
	return null;
};


// builds and returns the main panel
MeshWallImagePanel.prototype.getPanel = function(imageList, meshData) {
	var _this = this;
	this.prepareData(imageList);
	this.store = Ext.create('Ext.data.Store', {
				model : 'ImageModel'
			});
	this.store.loadData(this.features, false);

	var items = [];
	if (imageList && imageList.length > 0) {
		items.push({
					xtype : 'displayfield',
					name : 'location',
					labelWidth : 175,
					width : "100%",
					fieldLabel : 'Location',
					value : imageList[0].fileLocation
				});
	}
	var nbCol = this.getNbCol(meshData);
	var height = 0;

	
	var listi = [];
	if (meshData && imageList) {
		var listImages = imageList;
		height = 140 * ((listImages.length / nbCol));
		if (height < 140 ){
			height= 140;
		}
		for (var i = 0; i < listImages.length; i++) {
			var imageId = listImages[i].imageId;
			var pos = this.getGridPosition(meshData, imageId);
			if (pos) {
				var image = Ext.create('Ext.Panel', {
					width : 125,
					height : 125,
					border : 0,
					margin : "5 5 5 5",
					html : '<a href="viewResults.do?reqCode=viewJpegImage&imageId=' + 
							listImages[i].imageId + 
							'&previousImageId=' + 
							listImages[i].previousImageId + 
							'&nextImageId=' + 
							listImages[i].nextImageId + 
							'" ><img src="imageDownload.do?reqCode=getImageJpgThumb&imageId=' + 
							listImages[i].imageId + 
							'" width="125" height="125" border="0" /></a>'
				});
				listi.push(image);
			}
		}
	}
	var view = Ext.create('Ext.Panel', {
				layout : {
					type : 'table',
					columns : nbCol
				},
				height : height,
				items : listi
			});
	items.push(view);

	_this.panel = Ext.create('Ext.Panel', {
				width: "100%",
				//id : 'images-view',
				autoScroll : true,
				layout : 'vbox',
				items : items
			});

	return _this.panel;
};
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
// position grid 
function PositionGrid(args) {

	var contextPath = "";

	this.title = "Positions";
	this.contextPath = "";
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.title != null) {
			this.title = args.title;
		}
		if (args.listOfCrystalClass != null) {
			this.listOfCrystalClass = args.listOfCrystalClass;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
	}

	// datamodel
	Ext.define('positionModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'dataCollectionId',
							mapping : 'dataCollectionId'
						}, {
							name : 'imagePrefix',
							mapping : 'imagePrefix'
						}, {
							name : 'dataCollectionNumber',
							mapping : 'dataCollectionNumber'
						}, {
							name : 'phiX',
							mapping : 'phiX'
						}, {
							name : 'phiY',
							mapping : 'phiY'
						}, {
							name : 'phiZ',
							mapping : 'phiZ'
						}, {
							name : 'sampX',
							mapping : 'sampX'
						}, {
							name : 'sampY',
							mapping : 'sampY'
						}, {
							name : 'omega',
							mapping : 'omega'
						}, {
							name : 'kappa',
							mapping : 'kappa'
						}, {
							name : 'phi',
							mapping : 'phi'
						}, {
							name : 'gridIndexY',
							mapping : 'gridIndexY'
						}, {
							name : 'gridIndexZ',
							mapping : 'gridIndexZ'
						}, {
							name : 'spotTotal',
							mapping : 'spotTotal'
						}, {
							name : 'goodBraggCandidates',
							mapping : 'goodBraggCandidates'
						}, {
							name : 'method1Res',
							mapping : 'method1Res'
						}, {
							name : 'method2Res',
							mapping : 'method2Res'
						}, {
							name : 'totalIntegratedSignal',
							mapping : 'totalIntegratedSignal'
						},
						{
							name : 'dozor_score',
							mapping : 'dozor_score'
						}

				],
				idProperty : 'dataCollectionId'
			});
}

// no filters
PositionGrid.prototype.getFilterTypes = function() {
	return [];
};

// format data
PositionGrid.prototype._prepareData = function(data) {
	var d = [];
	for (var i = 0; i < data.length; i++) {
		var dataPos = new Object();
		dataPos = data[i];
		dataPos.phiX = data[i].motorPosition.phiX;
		dataPos.phiY = data[i].motorPosition.phiY;
		dataPos.phiZ = data[i].motorPosition.phiZ;
		dataPos.sampX = data[i].motorPosition.sampX;
		dataPos.sampY = data[i].motorPosition.sampY;
		dataPos.omega = data[i].motorPosition.omega;
		dataPos.kappa = data[i].motorPosition.kappa;
		dataPos.phi = data[i].motorPosition.phi;
		dataPos.gridIndexY = data[i].motorPosition.gridIndexY;
		dataPos.gridIndexZ = data[i].motorPosition.gridIndexZ;
		d.push(dataPos);
	}

	return d;
}
// get grids;

PositionGrid.prototype.getGrid = function(meshData) {
	this.features = this._prepareData(meshData);
	return this.renderGrid(this.features);
};

// no actions
PositionGrid.prototype.getActions = function() {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};

// builds the gris
PositionGrid.prototype.renderGrid = function() {
	var _this = this;

	/** Prepare data * */
	// builds columns
	var columns = this._getColumns();
	// grid data
	this.store = Ext.create('Ext.data.Store', {
				model : 'positionModel',
				autoload : false
			});

	this._sort(this.store);
	this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);

	this.title = "(" + this.features.length + ") Positions";

	// build grid
	this.grid = Ext.create('Ext.grid.Panel', {
				style : {
					padding : 0
				},
				width : "100%",
				model : 'positionModel',
				height : "100%",
				store : this.store,
				columns : columns,
				title : this.title,
				viewConfig : {
					stripeRows : true,
					enableTextSelection : true
				},
				selModel : {
					mode : 'SINGLE'
				}

			});
	return this.grid;
};

// default sort
PositionGrid.prototype._sort = function(store) {
	store.sort('dozor_score', 'DESC');
};

// build columns
PositionGrid.prototype._getColumns = function() {
	var _this = this;

	function columnWrap(value) {
		if (value)
			return '<div style="white-space:normal !important;">' + value + '</div>';
		else
			return "";
	}

	var columns = [{
				text : 'Image Prefix',
				dataIndex : 'imagePrefix',
				flex : 0.15
			}, {
				text : 'Run No',
				dataIndex : 'dataCollectionNumber',
				flex : 0.05
			}, {
				text : 'phiX',
				dataIndex : 'phiX',
				flex : 0.075
			}, {
				text : 'phiY',
				dataIndex : 'phiY',
				flex : 0.075
			}, {
				text : 'phiZ',
				dataIndex : 'phiZ',
				flex : 0.075
			}, {
				text : 'sampX',
				dataIndex : 'sampX',
				flex : 0.075
			}, {
				text : 'sampY',
				dataIndex : 'sampY',
				flex : 0.075
			}, {
				text : 'omega',
				dataIndex : 'omega',
				flex : 0.075
			}, {
				text : 'kappa',
				dataIndex : 'kappa',
				flex : 0.075
			}, {
				text : 'phi',
				dataIndex : 'phi',
				flex : 0.075
			}, {
				text : 'gridIndexY',
				dataIndex : 'gridIndexY',
				flex : 0.075
			}, {
				text : 'gridIndexZ',
				dataIndex : 'gridIndexZ',
				flex : 0.075
			}, {
				text : 'Spot Total',
				dataIndex : 'spotTotal',
				flex : 0.075
			}, {
				text : 'Good Bragg Candidates',
				dataIndex : 'goodBraggCandidates',
				flex : 0.075
			}, {
				text : 'Method 1 Resolution',
				dataIndex : 'method1Res',
				flex : 0.075
			}, {
				text : 'Method 2 Resolution',
				dataIndex : 'method2Res',
				flex : 0.075
			}, {
				text : 'Total Integrated Signal',
				dataIndex : 'totalIntegratedSignal',
				flex : 0.075
			},
			{
				text : 'Dozor Score',
				dataIndex : 'dozor_score',
				flex : 0.075
			}];

	return columns;

};

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

// to zoom the cell size of the map
function SliderWidget(args) {
	var _this = this;
	this.sliderValue = 30;
	this.sliderChange = new Event(this);
}

// build the slider
SliderWidget.prototype.draw = function () {
	var _this = this;
	return Ext.create('Ext.slider.Single', {
			hideLabel : true,
			width : 214,
			value : _this.sliderValue,
			increment : 1,
			minValue : 10,
			maxValue : 50,
			listeners : {
				change : function (slider, val) {
					_this.sliderValue = val;
					_this.sliderChange.notify();
				}
			}
		});
};

// returns the slider value
SliderWidget.prototype.getSliderValue = function () {
	var _this = this;
	return _this.sliderValue;
};
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
// Parameter panel in the dataCollection page (ISig and rsymm)
function ParametersPanel(args) {
	var _this = this;
	this.width = 500;
	this.height = 250;

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
ParametersPanel.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'vbox'
				},
				style : {
					marginLeft : '10px', 
					marginTop : '10px'
				},
				collapsible : true,
				frame : true,
				title: 'Parameters',
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 180
				},
				defaultType : 'textfield',
				items : [{
							fieldLabel : 'Ignore RSymm in the<br/>  low resolution shell over',
							name : 'rsymm',
							value : data.rMerge
						}, {
							fieldLabel : 'Ignore I / Sigma in the<br/>  low resolution shell under',
							name : 'isigma',
							value : data.iSigma
						}],
				buttons : [{
							text : 'Update',
							handler : function() {
								_this.update();
							}
						}]

			});

	return _this.panel;
};


// update button: redirection
ParametersPanel.prototype.update = function() {
	var _this = this;
	var rMerge = Ext.getCmp(this.panel.getItemId()).getValues().rsymm;
	var iSigma = Ext.getCmp(this.panel.getItemId()).getValues().isigma;
	if (_this.data.dataCollectionId){
		var dataCollectionId = _this.data.dataCollectionId;
		document.location.href =   "viewResults.do?reqCode=display&dataCollectionId="+dataCollectionId+"&rmerge="+rMerge+"&isigma="+iSigma;
	}else if (_this.data.dataCollectionGroupId){
		var dataCollectionGroupId = _this.data.dataCollectionGroupId;
		document.location.href =   "viewDataCollection.do?reqCode=displayForDataCollectionGroup&dataCollectionGroupId="+dataCollectionGroupId+"&rmerge="+rMerge+"&isigma="+iSigma;
	}else{
		var sessionId = _this.data.sessionId;
		document.location.href =   "viewDataCollection.do?reqCode=displayForSession&sessionId="+sessionId+"&rmerge="+rMerge+"&isigma="+iSigma;
	}
	
};


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
// Reference Panel (for a session or a list of collects)
function ReferencePanel(args) {
	var _this = this;
	this.width = 300;
	this.height = 250;
	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	this.listOfRef = null;
}


// returns the list of references
ReferencePanel.prototype.getListOfReference = function() {
	return this.listOfRef;
};


// builds and returns the main panel
ReferencePanel.prototype.getPanel = function(data) {
	var _this = this;
	var links = "";
	var listIds = [];
	this.listOfRef = data.listOfReferences;
	if (data.referenceText) {
		links += "<h2>" + data.referenceText + "</h2>";
	}
	// build the links
	if (this.listOfRef) {
		for (var i = 0; i < this.listOfRef.length; i++) {
			var ref = this.listOfRef[i];
			links += "<a href=" + ref.referenceUrl + " target=_blank>" + ref.referenceName + "</a><br/> ";
			listIds.push(ref.referenceId);
		}
	}

	var url = data.contextPath+'/viewReference.do?reqCode=downloadListOfReference&listOfReference=' + listIds;
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : 'fit',
				collapsible : true,
				frame : true,
				autoScroll : true,
				title : 'References',
				width : this.width,
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 100
				},
				defaultType : 'textfield',
				style : {
					marginLeft : '10px', 
					marginTop : '10px'
				},
				items : [{
							xtype : 'panel',
							manageHeight : false,
							html : links
						}],
				buttons : [{
							xtype : 'button',
							text : 'Download as BibTeX',
							href : url,
							target : '_blank',
							hrefTarget : '_blank'
						}]

			});

	return _this.panel;
};
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
// Report panel in the dataCollection page
function ReportPanel(args) {
	var _this = this;
	this.width = 500;
	this.height = 250;
	
	this.isFx = false;
	
	this.isIndustrial = false;

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
ReportPanel.prototype.getPanel = function(data) {
	var _this = this;

	var contextPath = "";
	var sessionId = null;
	if (data && data.contextPath) {
		contextPath = data.contextPath;
	}
	if (data && data.sessionId ){
		sessionId = data.sessionId;
	}
	if (data && data.isFx){
		this.isFx = data.isFx;
	}
	
	if (data && data.isIndustrial){
		this.isIndustrial = data.isIndustrial;
	}
	
	var buttonDOC = new Ext.Button({
		icon : '../images/Word.png',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportAsRtf&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		tooltip : 'Export this table as DOC'
	});
	var buttonPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportAsPdf&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export this table as PDF'
	});
	var buttonCSV = new Ext.Button({
		icon : '../images/icon_excel07.jpg',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportAsCsv&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export this table as CSV'
	});
	
	var buttonSendPDF = new Ext.Button({
		icon : '../images/pdfSend.png',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportAndSendAsPdf&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export and send as PDF'
	});
	
	var buttonScreeningDOC = new Ext.Button({
		icon : '../images/Word.png',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportScreeningAsRtf&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		tooltip : 'Export Screenings into DOC'
	});
	var buttonScreeningPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : contextPath +"/user/exportDataCollection.do?reqCode=exportScreeningAsPdf&sessionId=" +sessionId,
		hrefTarget : '_self',
		scale:'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export Screenings into PDF'
	});
	
	var itemsGeneralReport = [];
	itemsGeneralReport.push(buttonDOC, buttonPDF, buttonCSV);
	if (_this.isFx && !_this.isIndustrial){
		itemsGeneralReport.push(buttonSendPDF);
	}
	
	_this.panelGeneralReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border:0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : itemsGeneralReport
	});
	
	var itemsScreeningReport = [];
	itemsScreeningReport.push(buttonScreeningDOC, buttonScreeningPDF);
	
	_this.panelScreeningReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border:0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : itemsScreeningReport
	});

	//main panel
	_this.panel = Ext.create('Ext.form.Panel', {
		layout : {
			type : 'vbox',
			border : 0,
			bodyPadding : 40
		},
		border:0,
		style : {
			marginLeft : '10px', 
			marginTop : '10px'
		},
		fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 200
		},
		collapsible : true,
		title: 'Reports', 
		frame : true,
		items : [
				{
					xtype : 'displayfield',
					fieldLabel : 'General report',
					labelStyle : 'font-weight:bold;',
					value : ''
				},
				_this.panelGeneralReport, 
				{
					xtype : 'displayfield',
					fieldLabel : 'Screening report',
					labelStyle : 'font-weight:bold;',
					value : ''
				},
				_this.panelScreeningReport
				]
	});
	

	return _this.panel;
};


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
/*global  Event*/
/*global  Ext*/
/*global  window*/

// Session Report page: report section
function ReportSessionPanel(args) {
	var _this = this;
	this.width = 500;
	this.height = 250;
	this.sessionId = null;
	this.contextPath = "";
	this.isFx = false;
	this.isIndus = false;
	this.send = false;
	/** Events * */
	this.onExportReportSelected = new Event(this);

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
ReportSessionPanel.prototype.getPanel = function (data) {
	var _this = this;

	_this.contextPath = "";
	_this.sessionId = null;
	if (data && data.contextPath) {
		_this.contextPath = data.contextPath;
	}
	if (data && data.sessionId) {
		_this.sessionId = data.sessionId;
	}
	if (data && data.isFx) {
		_this.isFx = data.isFx;
	}
	if (data && data.isIndus) {
		_this.isIndus = data.isIndus;
	}
	
	// MXPress reports
	var items = [];
//TODO fix pb of export in doc with images inside	
//	var buttonDOC = new Ext.Button({
//		icon : '../images/Word.png',
//		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportMXPressOWorkflowAsRtf&sessionId=" + _this.sessionId,
//		hrefTarget : '_self',
//		scale: 'large',
//		tooltip : 'Export the MXPressO/MXPressE pipeline report into DOC'
//	});
	var buttonPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportMXPressOWorkflowAsPdf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export the MXPressO/MXPressE pipeline report into PDF'
	});
	//items.push(buttonDOC, buttonPDF);
	items.push(buttonPDF);
	// mxPress panel reports
	_this.panelReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border: 0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : items
	});
	
	// General reports
	var itemsIndustrial = [];
	
	var buttonIndustrialDOC = new Ext.Button({
		icon : '../images/Word.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportIndustrialReportAsRtf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		tooltip : 'Export the report into DOC'
	});
	var buttonIndustrialPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportIndustrialReportAsPdf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export the report into PDF'
	});
	var buttonIndustrialPDFSend = new Ext.Button({
		icon : '../images/pdfSend.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportIndustrialReportAndSendAsPdf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export the report and send as PDF'
	});
	itemsIndustrial.push(buttonIndustrialDOC, buttonIndustrialPDF);
	if (_this.isFx && !_this.isIndus) {
		itemsIndustrial.push(buttonIndustrialPDFSend);
	}
	_this.panelIndustrialReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border: 0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : itemsIndustrial
	});
	
	
	// Detailed Screening reports
	var generalItems = [];
	_this.rtf = false;
	var buttonGDOC = new Ext.Button({
		icon : '../images/Word.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAsRtf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		tooltip : 'Export the Session report into DOC',
		handler: function (button, evt) {
			_this.rtf = true;
			_this.send = false;
			_this.onExportReportSelected.notify({});
		}
	});
	var buttonGPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAsPdf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export the Session report into PDF',
		handler: function (button, evt) {
			_this.rtf = false;
			_this.send = false;
			_this.onExportReportSelected.notify({});
		}
	});
	var buttonGPDFSend = new Ext.Button({
		icon : '../images/pdfSend.png',
		href : _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAsPdf&sessionId=" + _this.sessionId,
		hrefTarget : '_self',
		scale: 'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export the Session report and send as PDF',
		handler: function (button, evt) {
			_this.rtf = false;
			_this.send = true;
			_this.onExportReportSelected.notify({});
		}
	});
	generalItems.push(buttonGDOC, buttonGPDF);
	if (_this.isFx && !_this.isIndus) {
		generalItems.push(buttonGPDFSend);
	}
	_this.panelGeneralReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border: 0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : generalItems
	});
	
	
	// 
	var allItems = [];
	if (data.sessionSummary && data.sessionSummary == true) {
		allItems.push({
			xtype : 'displayfield',
			fieldLabel : 'General report',
			labelStyle : 'font-weight:bold;',
			value : ''
		},
			_this.panelIndustrialReport);
		allItems.push({
			xtype : 'displayfield',
			fieldLabel : 'Screening report',
			labelStyle : 'font-weight:bold;',
			value : ''
		},
			_this.panelGeneralReport);
	}
	if (data.sessionHasMXpressOWF && data.sessionHasMXpressOWF == true) {
		allItems.push({
			xtype : 'displayfield',
			fieldLabel : 'MXPressO-MXPressE report',
			labelStyle : 'font-weight:bold;',
			value : ''
		},
		_this.panelReport);
	}
	
	
	// main panel
	_this.panel = Ext.create('Ext.form.Panel', {
		layout : {
			type : 'vbox',
			border : 0,
			bodyPadding : 40
		},
		border: 0,
		style : {
			marginLeft : '10px', 
			marginTop : '10px'
		},
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 200
		},
		collapsible : true,
		title: 'Reports', 
		frame : true,
		items : allItems
	});
	
	
	

	return _this.panel;
};

// export the report event: redirection
ReportSessionPanel.prototype.exportSessionReport = function () {
	var _this = this;
	if (_this.rtf) {
		window.location.href = _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAsRtf&sessionId=" + _this.sessionId;
	}
	else {
		if (_this.send) {
			window.location.href = _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAndSendAsPdf&sessionId=" + _this.sessionId;
		} else {
			window.location.href = _this.contextPath + "/user/exportDataCollection.do?reqCode=exportSessionAsPdf&sessionId=" + _this.sessionId;
		}
	}
};

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
// Result panel of the workflow 
function ResultPanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 700;

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
ResultPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	if (data && data.workflowVO && data.workflowVO.resultFiles) {
		var nbResult = data.workflowVO.resultFiles.length;
		for (var i = 0; i < nbResult; i++) {
			var webPanel = new WebPanel();
			var title = data.workflowVO.resultFiles[i].directoryName;
			//var l = title.lastIndexOf("\\");
			//if (l != -1) {
			//	title = title.substring(0, l);
			//	l = title.lastIndexOf("\\");
			//	if (l != -1) {
			//		title = title.substring(l + 1);
			//	}
			//}
			items.push({
						tabConfig : {
							title : title
						},
						items : [webPanel.getPanel(data.workflowVO.resultFiles[i])]
					});
		}
	}

	var tabs = Ext.create('Ext.tab.Panel', {
				layout : 'fit',
				activeTab : 0,
				defaults : {
					bodyPadding : 0
				},
				items : items
			});

	_this.panel = Ext.create('Ext.Panel', {
				layout : 'fit',
				items : [tabs]
			});

	return _this.panel;
};
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
// search dataCollection criteria  panel for the last Collection in the manager view
function SearchDataCollectionPanel(args) {
	var _this = this;
	this.width = 550;
	this.height = 500;

	this.onSearchDataCollection = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}

// returns the search criteria filled by the user
SearchDataCollectionPanel.prototype.getSearchCriteria = function() {
	var bl = Ext.getCmp(this.panel.getItemId()).getValues().beamline;
	var d1 = Ext.getCmp(this.panel.getItemId()).getValues().searchStartDate;
	var d2 = Ext.getCmp(this.panel.getItemId()).getValues().searchEndDate;
	var t1 = Ext.getCmp(this.panel.getItemId()).getValues().searchStartTime;
	var t2 = Ext.getCmp(this.panel.getItemId()).getValues().searchEndTime;
	var search = [];
	search.beamline = bl;
	search.startDate = d1;
	search.endDate = d2;
	search.startTime = t1;
	search.endTime = t2;
	return search;
};

// select all beamlines
SearchDataCollectionPanel.prototype.selectAllBeamlines = function(value) {
	Ext.each(Ext.getCmp('beamlineGroup').items.items, function(c) {
				if (c.setValue) {
					c.setValue(value);
				}
				return true;
			}, Ext.getCmp('beamlineGroup'));

};

// set the search criteria 
SearchDataCollectionPanel.prototype.setCriteria = function(searchBeamline,
		startDate, endDate, startTime, endTime) {
	if (Ext.getCmp('beamlineGroup')) {
		Ext.each(Ext.getCmp('beamlineGroup').items.items, function(c) {
					if (searchBeamline && searchBeamline.length > 0) {
						var id = searchBeamline.indexOf(c.boxLabel);
						c.setValue(id != -1);
					}
					return true;
				}, Ext.getCmp('beamlineGroup'));
	}
	var allItems = Ext.getCmp(this.panel.getItemId()).items.items;
	for (var i = 0; i < allItems.length; i++) {
		var item = allItems[i];
		if (item.name == 'searchStartDate' && startDate && startDate != 'undefined') {
			item.setValue(startDate);
		} else if (item.name == 'searchEndDate' && endDate && endDate != 'undefined') {
			item.setValue(endDate);
		} else if (item.name == 'searchStartTime' && startTime && startTime != 'undefined') {
			item.setValue(startTime);
		} else if (item.name == 'searchEndTime' && endTime && endTime != 'undefined') {
			item.setValue(endTime);
		}
	}
};


//builds and returns the main panel
SearchDataCollectionPanel.prototype.getPanel = function(beamlines) {
	var _this = this;
	var d = [];
	for (i = 0; i < beamlines.length; i++) {
		d.push({
					xtype : 'checkbox',
					boxLabel : beamlines[i],
					name : 'beamline',
					checked : true,
					inputValue : beamlines[i]
				});
	}

	_this.panel = Ext.widget({
		xtype : 'form',
		//layout : 'form',
		collapsible : true,
		id : 'searchSessionForm',
		frame : true,
		title : 'Search DataCollection',
		width : this.width,
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 100
		},
		defaultType : 'textfield',
		layout : {
			type : 'table',
			columns : 2
		},
		defaults : {
			labelStyle : 'padding-left:10px;'
		},
		items : [{
					xtype : 'checkboxgroup',
					fieldLabel : 'Beamline',
					id : 'beamlineGroup',
					columns : [90, 90, 90, 90, 90],
					colspan : 2,
					allowBlank : false,
					itemId : 'beamlines',
					items : d
				}, {
					xtype : 'button',
					name : 'selectAllBl',
					text : 'Select all Beamlines',
					handler : function() {
						_this.selectAllBeamlines(true);
					}
				}, {
					xtype : 'button',
					name : 'deselectAllBl',
					text : 'Deselect all Beamlines',
					handler : function() {
						_this.selectAllBeamlines(false);
					}
				}, {
					xtype : 'datefield',
					name : 'searchStartDate',
					fieldLabel : 'Start Date (DD/MM/YYYY)',
					format : 'd/m/Y',
					submitFormat : 'd/m/Y'
				}, {
					xtype : 'timefield',
					name : 'searchStartTime',
					fieldLabel : 'Start Time (hh:mm)',
					format : 'H:i',
					submitFormat : 'H:i'
				}, {
					xtype : 'datefield',
					name : 'searchEndDate',
					fieldLabel : 'End Date (DD/MM/YYYY)',
					format : 'd/m/Y',
					submitFormat : 'd/m/Y'
				}, {
					xtype : 'timefield',
					name : 'searchEndTime',
					fieldLabel : 'End Time (hh:mm)',
					format : 'H:i',
					submitFormat : 'H:i'
				}],

		buttons : [{
			text : 'Search',
			handler : function() {
				var d1 = Ext.getCmp(_this.panel.getItemId()).getValues().searchStartDate;
				var d2 = Ext.getCmp(_this.panel.getItemId()).getValues().searchEndDate;
				var longPeriod = ((d1 == "" && d2 != "") || (d1 != "" && d2 == ""));
				if (d1 != "" && d2 != "") {
					var dateParts = d1.split("/");
					var date1 = new Date(dateParts[2], (dateParts[1] - 1),
							dateParts[0]);
					dateParts = d2.split("/");
					var date2 = new Date(dateParts[2], (dateParts[1] - 1),
							dateParts[0]);
					var WNbJours = date2.getTime() - date1.getTime();
					var nbDays = Math.ceil(WNbJours / (1000 * 60 * 60 * 24));
					longPeriod = (nbDays > 1);
				}
				var r = true;
				if (longPeriod == true) {
					r = confirm("This search may take a long time (period > 1 day). Are you sure to continue?");
				}
				if (r == true) {
					_this.onSearchDataCollection.notify();
				}
				// this.up('form').getForm().reset();
			}
		}, {
			text : 'Reset',
			handler : function() {
				this.up('form').getForm().reset();
			}
		}]
	});

	return _this.panel;
};
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

// session statistics panel: nb Collect, nbCharac., etc.
function SessionStatsPanel(args) {
	var _this = this;
	this.width = 300;
	this.height = 250;
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
SessionStatsPanel.prototype.getPanel = function (data) {
	var _this = this;
	_this.panel = Ext.widget({
			xtype : 'form',
			layout : {
				type : 'vbox'
			},
			style : {
				marginLeft : '10px', 
				marginTop : '10px'
			},
			collapsible : true,
			frame : true,
			title: 'Session Statistics',
			bodyPadding : '5 5 0',
			fieldDefaults : {
				msgTarget : 'side',
				labelWidth : 180
			},
			defaultType : 'displayfield',
			items : [{
					fieldLabel : 'Nb Collect',
					value : data.nbCollect,
					qtip: "> 4 Images",
					listeners: {
						render: function (c) {
							Ext.QuickTips.register({
								target: c.getEl(),
								text: c.qtip
							});
						}
					}
				}, {
					fieldLabel : 'Nb Test',
					value : data.nbTest,
					qtip: "<= 4 Images",
					listeners: {
						render: function (c) {
							Ext.QuickTips.register({
								target: c.getEl(),
								text: c.qtip
							});
						}
					}
				}, {
					fieldLabel : 'Nb Energy Scan',
					value : data.nbEnergyScans
				}, {
					fieldLabel : 'Nb XRFSpectra',
					value : data.nbXRFSpectra
				}]
			

		});


	return _this.panel;
};
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
/*global  Event*/
/*global  Ext*/
/*global  document*/
/*global  contextPath*/
/*global  canvg*/

// grid for the Session Report page 
function SessionViewGrid(args) {

	// Events:
	// save comments
	this.onSaveButtonClicked = new Event(this);
	// export reports
	this.onExportReportWithGraphSelected = new Event(this);
	// change page in the grid
	this.onPageChanged = new Event(this);
	
	var isFx = false;
    var isIndus = false;
	var contextPath = "";
	// list of the autoProcAttachmentIds
	this.listChartId = [];
	// 2nd graph column hidden
	this.graph2Hidden = true;
	// sample info column hidden
	this.proteinSampleHidden = true;
	this.canSave = false;
	this.listOfCrystalClass = [];
	this.startPageNumber = 1;
	
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.isIndus != null) {
			this.isIndus = args.isIndus;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.title != null) {
			this.title = args.title;
		}
		
		if (args.sessionId != null) {
			this.sessionId = args.sessionId;
		}
		if (args.nbOfItems != null) {
			this.nbOfItems = args.nbOfItems;
		}
		if (args.listOfCrystalClass != null) {
			this.listOfCrystalClass = args.listOfCrystalClass;
		}
		if (args.canSave != null) {
			this.canSave = args.canSave;
		}
		if (args.startPageNumber != null) {
			this.startPageNumber = args.startPageNumber;
		}
	}

	// prepare crystal class information
	this._setCrystalClass();
	
	// session report datamodel
	Ext.define('SessionDataModel', {
		extend : 'Ext.data.Model',
		fields : [{
				name : 'sessionId',
				mapping : 'sessionId'
			}, 
			{
				name : 'experimentType',
				mapping : 'experimentType'
			}, 
			{
				name : 'proteinAcronym',
				mapping : 'proteinAcronym'
			}, 
			{
				name : 'sampleName',
				mapping : 'sampleName'
			}, 
			{
				name : 'sampleNameProtein',
				mapping : 'sampleNameProtein'
			},
			{
				name : 'dataTime',
				mapping : 'dataTime',
				type : 'date',
				dateFormat : 'd-m-Y H:i:s'
			}, 
			{
				name : 'dataCollectionId',
				mapping : 'dataCollectionId'
			}, 
			{
				name : 'imagePrefix',
				mapping : 'imagePrefix'
			}, 
			{
				name : 'runNumber',
				mapping : 'runNumber'
			},
			{
				name : 'listParameters',
				mapping : 'listParameters'
			},
			{
				name : 'crystalClass',
				mapping : 'crystalClass'
			}, 
			{
				name : 'comments',
				mapping : 'comments'
			},
			{
				name : 'imageThumbnailExist',
				mapping : 'imageThumbnailExist'
			},
			{
				name : 'imageThumbnailPath',
				mapping : 'imageThumbnailPath'
			},
			{
				name : 'imageId',
				mapping : 'imageId'
			},
			{
				name : 'crystalSnapshotExist',
				mapping : 'crystalSnapshotExist'
			},
			{
				name : 'crystalSnapshotPath',
				mapping : 'crystalSnapshotPath'
			},
			{
				name : 'graphExist',
				mapping : 'graphExist'
			},
			{
				name : 'graphPath',
				mapping : 'graphPath'
			},
			{
				name : 'graph2Exist',
				mapping : 'graph2Exist'
			},
			{
				name : 'graph2Path',
				mapping : 'graph2Path'
			},
			{
				name : 'graphData',
				mapping : 'graphData'
			},
			{
				name : 'resultStatus',
				mapping : 'resultStatus'
			},
			{
				name : 'result',
				mapping : 'result'
			},
			{
				name : 'listResults',
				mapping : 'listResults'
			}, 
			{
				name : 'workflow',
				mapping : 'workflow'
			}, 
			{
				name : 'energyScan',
				mapping : 'energyScan'
			}, 
			{
				name : 'xrfSpectra',
				mapping : 'xrfSpectra'
			}, 
			{
				name : 'dataCollection',
				mapping : 'dataCollection'
			}, 
			{
				name : 'samplePosition',
				mapping : 'samplePosition'
			},
			{
				name : 'barcode',
				mapping : 'barcode'
			}]
	});
}

// get the grids
SessionViewGrid.prototype.getGrid = function (listSessionDataObject) {
	this.features = listSessionDataObject;
	return this.renderGrid(listSessionDataObject);
};

//prepare data for crystal class
SessionViewGrid.prototype._setCrystalClass = function () {
	this.crystalClass = [];
	for (var i = 0; i < this.listOfCrystalClass.length; i++) {
		this.crystalClass.push(this.listOfCrystalClass[i].crystalClassName);
	}
};

// refresh data: reload
SessionViewGrid.prototype.refresh = function (data) {
	this.features = data.listSessionDataObjectInformation;
	this.store.loadData(this.features, false);
};

// sort by time
SessionViewGrid.prototype._sort = function (store) {
	store.sort('dataTime', 'DESC');
};


// builds the grid
SessionViewGrid.prototype.renderGrid = function () {
	var _this = this;
	
	// by default the columns are hidden
	_this.graph2Hidden = true;
	_this.proteinSampleHidden = true;
	
	
	// grouping feature: by default the time
	var defaultGroupField = null;
	
	for (var f = 0; f < _this.features.length; f++) {
		if (_this.features[f].graph2Exist) {
			_this.graph2Hidden = false;
		}
		if (_this.features[f].sampleNameProtein && _this.features[f].sampleNameProtein !== "") {
			_this.proteinSampleHidden = false;
			defaultGroupField = "sampleNameProtein";
		}
		if (_this.features[f].graphData && _this.features[f].graphData.length > 0) {
			var idChart = _this.features[f].graphData[0].autoProcProgramAttachmentId;
			_this.listChartId.push(idChart);
		}
	}
					
	/** Prepare data * */
	var data = [];
	// get Columns
	var columns = this._getColumns();
	
	// paging mode if too much data
	_this.pagingMode = false;
	if (_this.features.length > 15) {
		_this.pagingMode = true;
	}
	if (_this.pagingMode) {
		this.store = Ext.create('Ext.data.Store', {
				model : 'SessionDataModel',
				autoload : false,
				pageSize : 10,
				groupField: 'dataTime',
				proxy : {
					type : 'pagingmemory',
					data : this.features,
					reader : {
						type : 'array'
					}
				}
			});
	} else {
		this.store = Ext.create('Ext.data.Store', {
			model : 'SessionDataModel',
			autoload : false,
			groupField : 'dataTime'
		});
	}

	// group
	var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
		groupHeaderTpl : '{columnName}: {name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
		hideGroupedHeader : true,
		startCollapsed : false,
		id : 'sessionReportGrouping'
	}), 
	groups = this.store.getGroups(),
	len = groups.length,
	//i = 0, 
	toggleGroup = function (
			item) {
		var groupName = item.text;
		if (item.checked) {
			groupingFeature.expand(groupName, true);
		} else {
			groupingFeature.collapse(groupName, true);
		}
	};
	
	// bottom bar
	var bbar = null;
	var pagingButtonText = "Pagination";
	if (_this.pagingMode) {
		pagingButtonText = "Remove pagination";
		bbar = Ext.create('Ext.PagingToolbar', {
				store : this.store,
				pageSize : this.pageSize,
				displayInfo : true,
				displayMsg : 'Displaying data {0} - {1} of {2}',
				emptyMsg : "No data to display",
				listeners : {
					afterrender : function () {
						var a = Ext.query("button[data-qtip=Refresh]");
						for (var x = 0; x < a.length; x++) {
							a[x].style.display = "none";
						}
					}
				}
			});
        bbar.on({
            change: function (pagingToolBar, changeEvent) {
				var currentPage = changeEvent.currentPage;
				_this.onPageChanged.notify({'sessionReportCurrentPageNumber' : currentPage, 'sessionId': _this.sessionId});
            }
        }); 

	}

	
	this.store.loadData(this.features, false);
	this._sort(this.store);
	// no data found
	var noGroupDataPanel = Ext.create('Ext.Panel', {
		layout : {
			type : 'fit', // Arrange child items vertically
			align : 'center', // Each takes up full width
			bodyPadding : 0,
			border : 0
		},
		html : '<html><h4>No Data have been found</h4></html>'
	});

	if (this.features.length == 0) {
		return noGroupDataPanel;
	}
	
	
	// top bar with buttons
	var bar = [];
	if (this.canSave) {
		bar.push({
				text : 'Save Comments',
				tooltip : 'Save comments',
				icon : '../images/save.gif',
				handler : function () {
					_this.onSaveButtonClicked.notify({});
				}
			});
	}
	
	if (this.sessionId && this.sessionId != "null") {
		bar.push({
			text : 'View All Summary info',
			tooltip : 'View all data for summary. May take some time.',
			icon : '../images/magnif_16.png',
			handler : function () {
				document.location.href = contextPath + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewSessionSummary.do?reqCode=displayAll&sessionId=" + _this.sessionId;
			}
				
		});
				
	}
	
	if (this.sessionId && this.sessionId != "null") {
		bar.push({
			text : 'View All DataCollection',
			tooltip : 'View all collects',
			icon : '../images/magnif_16.png',
			handler : function () {
				document.location.href = contextPath + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForSession&sessionId=" + _this.sessionId;
			}
				
		});
				
	}

	bar.push('->');
	bar.push({
		text : 'Expand All',
		handler : function () {
			var rows = groupingFeature.view.getEl().query('.x-grid-group-body');

			Ext.each(rows, function (row) {
						groupingFeature.expand(Ext.get(row));
					});
		}
	}, {
		text : 'Collapse All',
		handler : function () {
			var rows = groupingFeature.view.getEl().query('.x-grid-group-body');

			Ext.each(rows, function (row) {
						groupingFeature.collapse(Ext.get(row));
					});
		}
	});
	bar.push({
		text : 'Clear Grouping',
		iconCls : 'icon-clear-group',
		handler : function () {
			groupingFeature.disable();
		}
	});
	
	// this grid can be edited
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1
		});
		
	// grid
	this.grid = Ext.create('Ext.grid.Panel', {
		style : {
			padding : 0,
			marginTop : '10px'
		},
		model : 'SessionDataModel',
		store : this.store,
		width : '100%', 
		resizable : true,
		//height : 1000,
		autoHeight : true,
		title : 'Session',
		columns : columns,
		viewConfig : {
			stripeRows : true,
			enableTextSelection : true
		},
		selModel : {
			mode : 'SINGLE'
		},
		features : [groupingFeature],
		plugins : [cellEditing],
		tbar : bar,
		// paging bar on the bottom
		bbar : bbar
	});
	if (_this.pagingMode) {
		//this.store.load();
		this.store.loadPage(_this.startPageNumber);
	}	
	return this.grid;
};


// returns the columns
SessionViewGrid.prototype._getColumns = function () {
	var _this = this;
	
	// render functions
	
	// render date
	function renderStartDate(value, p, record) {
		//return Ext.Date.format(value, ' H:i:s d-m-Y');
		var time = Ext.Date.format(value, 'H:i:s');
		var date = Ext.Date.format(value, 'd-m-Y');
		return time + "<br />" + date;
	}
	
	// render Image Prefix: (last collect prefix for workflows), with sample location if filled
	function renderImagePrefix(value, p, record) {
		var hasToolTipText;
		var myToolTipText;
		if (record.data.dataCollectionId) {
			hasToolTipText = false;
			myToolTipText = "Barcode (SC Position): ";
			if (record.data.barcode && record.data.barcode !== "") {
				myToolTipText += record.data.barcode; 
				hasToolTipText = true;
			}
			if (record.data.samplePosition && record.data.samplePosition !== "") {
				myToolTipText += " (" + record.data.samplePosition + ")"; 
				hasToolTipText = true;
			}
			if (hasToolTipText) {
				p.tdAttr = 'data-qtip="' + myToolTipText + '"';
			}
			return "<div style='white-space:normal !important;word-wrap: break-word'>" + "<a target='_blank' href=' " + _this.contextPath +
			"/user/viewResults.do?reqCode=display&dataCollectionId=" + 
			record.data.dataCollectionId + " '>" + record.data.imagePrefix + "</a></div>";
		} else {
			hasToolTipText = false;
			myToolTipText = "Barcode (SC Position): ";
			if (record.data.barcode && record.data.barcode !== "") {
				myToolTipText += record.data.barcode; 
				hasToolTipText = true;
			}
			if (record.data.samplePosition && record.data.samplePosition !== "") {
				myToolTipText += " (" + record.data.samplePosition + ")"; 
				hasToolTipText = true;
			}
			if (hasToolTipText) {
				p.tdAttr = 'data-qtip="' + myToolTipText + '"';
			}
			return "<div style='white-space:normal !important;word-wrap: break-word'>" + record.data.imagePrefix + "</div>";
		}
	}
		
		// render Parameters columns
	function renderParameters(value, p, record) {
		if (record.data.listParameters) {
		
			var cellData = '<div style="white-space:normal !important;word-wrap: break-word">';
			for (var i = 0; i < record.data.listParameters.length; i++) {
				var param = record.data.listParameters[i];
				var text = param.text;
				var vp = param.value;
				cellData += "<font color='#8E8888'><i>" + text + ": </i></font>" + vp + "<br />";
			}
			cellData = cellData + '</div>'; 
			return cellData;
		}
		return "";
	}
	
	
	// render Image Thumbnail
	function renderImageThumbnail(value, p, record) {
		if (!record.data.imageThumbnailExist) {
			if (value) {
				return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Image not found:<br>" + value + "</h4></div>";
		    }
			else { 
				return "";
			}
		} else {
			if (record.data.imageThumbnailPath.search("noDiffractionThumbnail.jpg") != -1) {
				return "";
			}
			return MXUI.getJpegByImageIdandPath(record.data.imageId,record.data.imageThumbnailPath );
			
		}
	}
	
	// render crystal snapshot
	function renderCrystalSnapshot(value, p, record) {
		if (!record.data.crystalSnapshotExist) {
			if (value) { 
				return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Image not found:<br>" + value + "</h4></div>";
			}
			else {
				return "";
			}
		} else {
			if (record.data.crystalSnapshotPath.search("noXtalThumbnail.jpg") != -1) {
				return "";
			}
			return MXUI.getSnapshotByPath(record.data.crystalSnapshotPath);
		}
	}
	
	
	// render Graph: graph to build or image
	function renderGraph(value, p, record) {
		if (!record.data.graphExist) {
			if (record.data.graphData) {
				// build the graph
				var d = [];
				for (var i = 0; i < record.data.graphData.length; i++) {
					var s = {
						resolutionLimit : record.data.graphData[i].resolutionLimit,
						iSigma : record.data.graphData[i].iSigma
					};
					d.push(s);
				}
				var storeGraph = Ext.create('Ext.data.JsonStore', {
					fields : ['resolutionLimit', 'iSigma' ],
					data : d
				});
				
				var container_id = Ext.id();
				var idChart = '';
				if (record.data.graphData.length > 0) {
					idChart = record.data.graphData[0].autoProcProgramAttachmentId;
				}
				
				// delete the previous chart if needed
				if (record.chart) {
					record.chart.destroy();
				}
				// creates the chart
				Ext.defer(function () {
				    record.chart = Ext.create('Ext.chart.Chart', {
						xtype : 'chart',
						id : "chart_" + idChart,
						width : 140,
						height : 130,
						renderTo : container_id,
						style : 'background:#fff',
						animate : false,
						store : storeGraph,
						shadow : false,
				
						theme : 'Category1',
						axes : [{
							type : 'Numeric',
							position : 'left',
							fields : ['iSigma'],
							//title : 'I/Sigl',
							//labelTitle:{
							//	font: '8px Arial',
							//},
							label : {
								font: '7px Arial'
							},
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb'
									//'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['resolutionLimit'],
							title : 'I/Sigl vs Resolution',
							labelTitle : {
								font: '8px Arial'
							},
							label : {
								font: '7px Arial'
							},
							grid : true

						}],
					    series : [{
								type : 'line',
								highlight : {
									size : 3,
									radius : 3
								},
								axis : 'left',
								xField : 'resolutionLimit',
								yField : 'iSigma',
								markerConfig : {
									type : 'cross',
									size : 2,
									radius : 2,
									'stroke-width' : 0
								}
							}]
				    });
				}, 50);
				
                return '<canvas id="canvas_' + idChart + '" style="display:none;"></canvas><div id="' + container_id + '"></div>';
			} else if (record.data.graphPath) {
				return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Graph not found:<br>" + record.data.graphPath + "</h4></div>";
			} else {
				return "";
			}
		} else { // graph image
			return MXUI.getGraphByPath(record.data.graphPath);
			
		}
	}
	
	// render second graph: image
	function renderGraph2(value, p, record) {
		if (!record.data.graph2Exist) {
			if (record.data.graph2Path) {
				return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Graph not found:<br>" + record.data.graph2Path + "</h4></div>";
			} else {
				return "";
			}
		} else {
			return MXUI.getGraphByPath(record.data.graph2Path);
		}
	}
	
	// render Result Column
	function renderResult(value, p, record) {
		if (record.data.result) {
			var color = "black";
			if (record.data.resultStatus.search("FAILED") != -1) {
				color = "red";
			} else if (record.data.resultStatus.search("SUCCESS") != -1) {
				color = "green";
			} else if (record.data.resultStatus.search("NOTDONE") != -1) {
				color = "grey";
			}
			// result
			var res =  '<div style="white-space:normal !important;color: ' + color + '">' + record.data.result + '</div>';
			// listResults
			if (record.data.listResults) {
				var myToolTipText = "<table border='1' ><tr>";
				var hasToolTipText = false;
				var cellData = "<div style='white-space:normal !important;word-wrap: break-word;'>";
				for (var i = 0; i < record.data.listResults.length; i++) {
					var param = record.data.listResults[i];
					var text = param.text;
					var vp = param.value;
					// completeness case
					if (param.key == "completeness") {
						var stringToParse = vp;
						var id1 = vp.indexOf(",");
						var percentage1 = vp.substring(0, id1);
						stringToParse = vp.substring(id1 + 1, vp.length);
						var id2 = stringToParse.indexOf(",");
						var percentage2 = stringToParse.substring(0, id2);
						stringToParse = stringToParse.substring(id2 + 1, stringToParse.length);
						var percentage3 = stringToParse;
						
						var initial = -61;
						var imageWidth = 122;
						var eachPercent = (imageWidth / 2) / 100;

						var percentageWidth1 = eachPercent * percentage1;
						var actualWidth1 = initial + percentageWidth1;
						var colour1 = percentage1 < 85 ? 4 : 1;

						var percentageWidth2 = eachPercent * percentage2;
						var actualWidth2 = initial + percentageWidth2;
						var colour2 = percentage2 < 85 ? 4 : 1;

						var percentageWidth3 = eachPercent * percentage3;
						var actualWidth3 = initial + percentageWidth3;
						var colour3 = percentage3 < 85 ? 4 : 1;

						var s = "";
						if (!(percentage1 === 0 && percentage2 === 0 && percentage3 === 0)) {
							s += '<img " src="../images/percentImage_small.png" title="' +
									percentage1 + '% (inner)" alt="' + percentage1 +
									'% (inner)" class="percentImage' + colour1 +
									'" style="background-position: ' + actualWidth1 +
									'px 0pt; "/><br/>';
							s += '<img  src="../images/percentImage_small.png" title="' + 
									percentage2 + '% (outer)" alt="' + percentage2 + 
									'% (outer)" class="percentImage' + colour2 + 
									'" style="background-position: ' + actualWidth2 + 
									'px 0pt; "/><br/>';
							s += '<img  src="../images/percentImage_small.png" title="' + 
									percentage3 + '% (overall)" alt="' + percentage3 + 
									'% (overall)" class="percentImage' + colour3 + 
									'" style="background-position: ' + actualWidth3 + 
									'px 0pt; "/><br/>';
						}
						cellData += "<font color='#8E8888'> <i>" + text + ": </i> </font> <br />" + s;
					} else if (! param.isTooltip) {
						cellData += "<font color='#8E8888'> <i>" + text + ": </i> </font>" + vp + "<br />";
					} else {
						hasToolTipText = true;
						myToolTipText += "<td  style='text-align:center'><font color='#8E8888'><i>" + text + "</i></font></td>";
					}
					
				}
				myToolTipText += "</tr>";
				if (hasToolTipText) {
					myToolTipText += "<tr>";
					for (var i = 0; i < record.data.listResults.length; i++) {
						var param = record.data.listResults[i];
						var vp = param.value;
						if (param.isTooltip) {
							myToolTipText += "<td  style='text-align:center'>" + vp + "</td>";
						}
					}
					myToolTipText += "</tr>";
				}
				myToolTipText += "</table>";
				if (hasToolTipText) {
					p.tdAttr = 'data-qtip="' + myToolTipText + '"';
				}
				res += "</div>" + cellData;
			}
			return res;
		}
	}
	
	// column wrap
	function columnWrap(value) {
		if (value) { 
			return '<div style="white-space:normal !important;word-wrap: break-word">' + value + '</div>';
		}
		else { 
			return "";
		}
	}
	
	
	// render experiment Type
	function renderExperimentType(value, p, record) {
		if (record.data.workflow) {
			var mess = "";
			if (record.data.workflow.status) {
				mess = record.data.workflow.status;
			}
			var iconWorkflow = "../images/Sphere_White_12.png";
			if (mess) {
				if (mess.search("Error") != -1 || mess.search("Failure") != -1) {
					iconWorkflow = "../images/Sphere_Red_12.png";
				} else if (mess.search("Started") != -1 || mess.search("Launched") != -1) {
					iconWorkflow = "../images/Sphere_Orange_12.png";
				} else if (mess.search("Success") != -1) {
					iconWorkflow = "../images/Sphere_Green_12.png";
				}
			}
			var com = "";
			if (record.data.workflow.comments) {
				com = record.data.workflow.comments;
			}
			var myToolTipText = " <img src='" + iconWorkflow + "' border=0\/>" +
					mess + "<br/>" + record.data.workflow.workflowTitle +
					"<br/>" + com;
			p.tdAttr = 'data-qtip="' + myToolTipText + '"';
			return "<a href='" + contextPath + "/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewWorkflow.do?reqCode=display&workflowId=" + record.data.workflow.workflowId + "'>" + record.data.workflow.workflowType + "</a>";
		} else {
			return record.data.experimentType;
		}
	}
		
	// create reusable renderer
	Ext.util.Format.comboRenderer = function (combo) {
		return function (value) {
			var record = combo.findRecord(combo.valueField, value);
			return record ? record.get(combo.displayField) : combo.valueNotFoundText;
		};
	};

	// create the combo instance

	var crystalClassStore = Ext.create('Ext.data.Store', {
			fields : ['crystalClassCode', 'crystalClassName'],
			data : _this.listOfCrystalClass
		});

	var combo = new Ext.form.ComboBox({
			mode : 'local',
			emptyText : '---',
			store : crystalClassStore,
			valueField : 'crystalClassCode',
			displayField : 'crystalClassName',
			allowBlank : true,
			typeAhead : true,
			triggerAction : 'all',
			listConfig: {cls: 'small-combo-text'}
		});
	
	
	// create the columns
	var columns = [
	    {
	        text : 'Exp.<br/>Type',
	        dataIndex : 'experimentType',
			renderer : renderExperimentType,
	        flex : 0.10
		},
		{
			text : 'Acronym<br/>Sample name',
			dataIndex : 'sampleNameProtein',
			hidden : _this.proteinSampleHidden,
			renderer : columnWrap,
			flex : 0.10
		},
		{
			text : 'Image<br/>Prefix',
			dataIndex : 'imagePrefix',
			renderer : renderImagePrefix,
			flex : 0.10
		}, 
		{
			text : 'Run#',
			dataIndex : 'runNumber',
			flex : 0.03
		}, 
		{
			text : 'Start<br/>time',
			renderer : renderStartDate,
			dataIndex : 'dataTime',
			flex : 0.07
		}, 
		{
			text : 'Parameters',
			renderer : renderParameters,
			flex : 0.2
		},
		{
			text : 'Results',
			renderer : renderResult,
			flex : 0.2
		},
		{
			text : 'Image<br/>Thumbnail',
			renderer : renderImageThumbnail,
			flex : 0.15
		},
		{
			text : 'Crystal<br/>snapshot',
			renderer : renderCrystalSnapshot,
			flex : 0.15
		},
		{
			text : 'Graph',
			renderer : renderGraph,
			flex : 0.15
		},
		{
			text : 'Second Graph',
			renderer : renderGraph2,
			hidden : _this.graph2Hidden,
			flex : 0.15
		}
	];
		
	if (_this.isFx) {
		if (_this.canSave) {
			columns.push({
					text : 'Crystal<br/>class',
					dataIndex : 'crystalClass',
					flex : 0.10,
					editor : combo, // specify reference to combo instance
					renderer : Ext.util.Format.comboRenderer(combo)
						// pass combo instance to reusable renderer
				});
		} else {
			columns.push({
					text : 'Crystal<br/>class',
					dataIndex : 'crystalClass',
					flex : 0.10,
					renderer : Ext.util.Format.comboRenderer(combo)
						// pass combo instance to reusable renderer
				});
		}
	}
		
	if (_this.canSave) {
		columns.push({
			text : 'Comments',
			dataIndex : 'comments',
			editor : {
				xtype : 'textfield',
				allowBlank : true
			},
			flex : 0.15,
			renderer : columnWrap
		});
	} else {
		columns.push({
			text : 'Comments',
			dataIndex : 'comments',
			flex : 0.15,
			renderer : columnWrap
		});
	}
	
	return columns;

};


// save the graph as image: get the canvas and send the list of canvas to the server
SessionViewGrid.prototype.saveGraphAsImg = function () {
	var _this = this;
	var listSvg = [];
	var listId = [];
	for (var i = 0; i < _this.listChartId.length; i++) {
		var idC = "chart_" + _this.listChartId[i];
		var chartComp = Ext.getCmp(idC);
		var canvas = document.getElementById("canvas_" + _this.listChartId[i]);
		if (chartComp) {
			var svg = chartComp.el.dom.innerHTML;
			canvg('canvas_' + _this.listChartId[i], svg);
			var img = canvas.toDataURL();
			listId.push(_this.listChartId[i]);
			listSvg.push(img);
		}
	}
	_this.onExportReportWithGraphSelected.notify({'listSvg' : listSvg,  'sessionId' : _this.sessionId, 'listId' : listId});
};

// returns the sessionId
SessionViewGrid.prototype.getSessionId = function () {
	return this.sessionId;
};

SessionViewGrid.prototype.getNbOfItems = function () {
	return this.nbOfItems;
};
// returns the data as json
SessionViewGrid.prototype.getListData = function () {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};
/*******************************************************************************
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
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/

// add functions to be execute on the same event, onload here
function addEvent(obj, event, fct) {
    if (obj.attachEvent) //IE?
        obj.attachEvent("on" + event, fct);
    else
        obj.addEventListener(event, fct, true);
}


function onLoadPage(){
	// check archived status every 59s - stop after 5h
	var startTime = new Date().getTime();
	var interval = setInterval(
		function(){
			if(new Date().getTime() - startTime > 18000000){
				clearInterval(interval);
				return;
		}
		archivedRefresh();
	}, 59000);   
}



// ajax request to update the archiving status
function archivedRefresh(){
	var url = "viewDataCollection.do?reqCode=updateArchiving";
	$.ajax( {
		type: 'get',
		url: url,
		success: function( r ) {
			var pyarchArchivedDiv = document.getElementById('pyarchArchivedDiv');
			pyarchArchivedDiv.innerHTML = r;
		}
	} );
}
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
// Image panel, presented as a wall
function WallImagePanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 800;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	// image datamodel
	ImageModel = Ext.define('ImageModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'name'
						}, {
							name : 'url'
						}, {
							name : 'target'
						}, {
							name : 'size',
							type : 'float'
						}]
			});

}

// prepare data, format 
WallImagePanel.prototype.prepareData = function(imageList) {
	this.features = [];
	var listImages = imageList;
	for (var i = 0; i < listImages.length; i++) {
		var o = {};
		var image = o.name = listImages[i].fileName;
		o.url = 'imageDownload.do?reqCode=getImageJpgThumb&imageId=' + listImages[i].imageId;
		o.size = 125;
		o.target = 'viewResults.do?reqCode=viewJpegImage&imageId=' + 
				listImages[i].imageId + '&previousImageId=' + 
				listImages[i].previousImageId + '&nextImageId=' + 
				listImages[i].nextImageId;
		this.features.push(o);
	}
};


// builds and returns the main panel
WallImagePanel.prototype.getPanel = function(imageList) {
	var _this = this;
	//format data
	this.prepareData(imageList);
	this.store = Ext.create('Ext.data.Store', {
				model : 'ImageModel'
			});
	this.store.loadData(this.features, false);

	var items = [];

	// location
	if (imageList && imageList.length > 0) {
		items.push({
					xtype : 'displayfield',
					name : 'location',
					labelWidth : 175,
					width : "100%",
					fieldLabel : 'Location',
					value : imageList[0].fileLocation
				});
	}
	// nbRows to display
	var nbRow = (this.features.length * 125 / this.width) + 1;
	
	// creates the view
	var view = Ext.create('Ext.view.View', {
		store : this.store,
		tpl : [
				'<tpl for=".">',
				'<div class="thumb-wrap"  id="{name:stripTags}">',
				'<div  class="thumb"><a href="{target}" ><img src="{url}" title="{name:htmlEncode}" border="0" width="{size}" height="{size}"></a></div>',
				'</div>', '</tpl>', '<div class="x-clear"></div>'],
		multiSelect : false,
		// width: "100%",
		height : 200 * nbRow,
		trackOver : true,
		// autoScroll:true,
		emptyText : 'No images to display',
		prepareData : function(data) {
			Ext.apply(data, {
						shortName : Ext.util.Format.ellipsis(data.name, 45),
						sizeString : Ext.util.Format.fileSize(data.size)
					});
			return data;
		}
	});
	items.push(view);

	// main panel
	_this.panel = Ext.create('Ext.Panel', {
				width: "100%",
				// height: this.height,
				//id : 'images-view',
				autoScroll : true,
				layout : 'vbox',
				border : 0,
				items : items
				
			});

	return _this.panel;
};
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
// panel with a html page
function WebPanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 700;

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
WebPanel.prototype.getPanel = function(data) {
	var _this = this;
	var url = "viewResults.do?reqCode=displayEDNAFile&EDNAFilePath=" + data.fileFullPath;

	_this.panel = Ext.create('Ext.Panel', {
				layout : 'fit',
				//height : this.height,
				autoScroll : true,
				html : data.fullFileContent
			});

	return _this.panel;
};
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
// Workflow panel tab
function WorkflowPanel(args) {
	var _this = this;
	this.width = 1380;
	this.height = 700;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}


// returns the main panel
WorkflowPanel.prototype.getPanel = function(data) {
	var _this = this;

	var labelStyle = 'font-weight:bold;';
	// workflow
	var workflow = data.workflowVO;
	// Status
	var iconWorkflow = "../images/Sphere_White_12.png";
	var mess = "";
	if (workflow.status) {
		mess = workflow.status;
	}
	if (mess) {
		if (mess.search("Error") != -1 || mess.search("Failure") != -1) {
			iconWorkflow = "../images/Sphere_Red_12.png";
		} else if (mess.search("Started") != -1 || mess.search("Launched") != -1) {
			iconWorkflow = "../images/Sphere_Orange_12.png";
		} else if (mess.search("Success") != -1) {
			iconWorkflow = "../images/Sphere_Green_12.png";
		}
	}

	// data workflow
	var dataAvailable = false;
	if (workflow && workflow.dataFile && workflow.dataFile.existFile) {
		dataAvailable = (workflow.dataFile.existFile == 1);
	}
	var dataFileName = "";
	if (workflow && workflow.dataFile && workflow.dataFile.fileName) {
		dataFileName = workflow.dataFile.fileName;
	}
	var buttonDataFilePath = new Ext.Button({
		text : dataFileName,
		style : {
			marginBottom : '10px'
		},
		handler : function() {
			window.location.href = "viewDataCollection.do?reqCode=downloadFile&fullFilePath=" + workflow.dataFile.fileFullPath;
		},
		disabled : !dataAvailable
	});

	
	// log 
	var logAvailable = false;
	if (workflow.logFile && workflow.logFile.existFile) {
		logAvailable = workflow.logFile.existFile == 1;
	}
	var logFileName = "";
	if (workflow && workflow.logFile && workflow.logFile.fileName) {
		logFileName = workflow.logFile.fileName;
	}
	var buttonLogFilePath = new Ext.Button({
		text : logFileName,
		style : {
			marginBottom : '10px'
		},
		handler : function() {
			window.location.href = "viewDataCollection.do?reqCode=downloadFile&fullFilePath=" + workflow.logFile.fileFullPath;
		},
		disabled : !logAvailable
	});

	var value1 = "";
	var value2 = "";
	if (workflow && workflow.workflowMesh) {
		value1 = workflow.workflowMesh.value1;
		value2 = workflow.workflowMesh.value2;
	}

	// items to display
	var items = [{
		xtype : 'field',
		name : 'icon',
		fieldSubTpl : new Ext.XTemplate("<img src='" + iconWorkflow + "' border=0\/>")
	}, {
		xtype : 'displayfield',
		name : 'title',
		fieldLabel : 'Title',
		labelStyle : labelStyle,
		value : workflow.workflowTitle
	}, {
		xtype : 'textareafield',
		name : 'comments',
		fieldLabel : 'Comments',
		readOnly : true,
		columns : 30,
		width : 500,
		rows : 7,
		labelStyle : labelStyle,
		value : workflow.comments
	},

	{
		xtype : 'textareafield',
		name : 'status',
		fieldLabel : 'Status',
		labelStyle : labelStyle,
		readOnly : true,
		columns : 30,
		width : 500,
		rows : 4,
		value : workflow.status
	}];
	if (data.displayMesh == 1) {
		items.push({
					xtype : 'displayfield',
					name : 'value1',
					fieldLabel : 'Value 1',
					labelStyle : labelStyle,
					value : value1
				}, {
					xtype : 'displayfield',
					name : 'value2',
					fieldLabel : 'Value 2',
					labelStyle : labelStyle,
					value : value2
				});
	}
	if (logFileName != "")
		items.push(buttonLogFilePath);
	if (dataFileName != "")
		items.push(buttonDataFilePath);

	if (logAvailable) {
		items.push({
					xtype : 'textareafield',
					// grow: true,
					labelStyle : labelStyle,
					name : 'logFileTxt',
					fieldLabel : 'Log File',
					readOnly : true,
					columns : 30,
					width : 800,
					rows : 15,
					value : workflow.logFile.fullFileContent
				});
	}

	// main panel
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'vbox'
				},
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				items : items

			});

	return _this.panel;
};
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
//XRFSpectra grid (in DataCollectionGroup page)
function XRFSpectraGrid(args) {

	this.onSaveButtonClicked = new Event(this);

	var isFx = false;
	this.contextPath = "";
	this.title = "XRFSpectra";
	this.listOfCrystalClass = [];
	this.canSave = false;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.title != null) {
			this.title = args.title;
		}
		if (args.listOfCrystalClass != null) {
			this.listOfCrystalClass = args.listOfCrystalClass;
		}
		if (args.sessionId != null) {
			this.sessionId = args.sessionId;
		}
		if (args.canSave != null) {
			this.canSave = args.canSave;
		}
	}

	// initializes the crystal class
	this._setCrystalClass();

	// xrfSpectra datamodel
	Ext.define('XRFSpectraModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'xfeFluorescenceSpectrumId',
							mapping : 'xfeFluorescenceSpectrumId'
						}, {
							name : 'fittedDataFileFullPath',
							mapping : 'fittedDataFileFullPath'
						}, {
							name : 'startTime',
							mapping : 'startTime',
							type : 'date',
							dateFormat : 'd-m-Y H:i:s'
						}, {
							name : 'endTime',
							mapping : 'endTime',
							type : 'date',
							dateFormat : 'd-m-Y H:i:s'
						}, {
							name : 'sampleName',
							mapping : 'sampleName'
						},  {
							name : 'crystalClass'
						}, {
							name : 'comments',
							mapping : 'comments'
						}, {
							name : 'scanFileFullPath',
							mapping : 'scanFileFullPath'
						}, {
							name : 'jpegScanFileFullPath',
							mapping : 'jpegScanFileFullPath'
						}, {
							name : 'filename',
							mapping : 'filename'
						}, {
							name : 'energy',
							mapping : 'energy'
						}, {
							name : 'exposureTime',
							mapping : 'exposureTime'
						}, {
							name : 'beamTransmission',
							mapping : 'beamTransmission'
						}, {
							name : 'annotatedPymcaXfeSpectrum',
							mapping : 'annotatedPymcaXfeSpectrum'
						}, {
							name : 'beamSizeVertical',
							mapping : 'beamSizeVertical'
						}, {
							name : 'beamSizeHorizontal',
							mapping : 'beamSizeHorizontal'
						}, {
							name : 'blSampleVO',
							mapping : 'blSampleVO'
						}, {
							name : 'jpegScanExists',
							mapping : 'jpegScanExists'
						}, {
							name : 'jpegScanFileFullPathParser',
							mapping : 'jpegScanFileFullPathParser'
						}, {
							name : 'annotatedPymcaXfeSpectrumExists',
							mapping : 'annotatedPymcaXfeSpectrumExists'
						}, {
							name : 'shortFileNameAnnotatedPymcaXfeSpectrum',
							mapping : 'shortFileNameAnnotatedPymcaXfeSpectrum'
						}],
				idProperty : 'xfeFluorescenceSpectrumId'
			});
}


// no filters
XRFSpectraGrid.prototype.getFilterTypes = function() {
	return [];
};


// sort by default by inverse time
XRFSpectraGrid.prototype._sort = function(store) {
	store.sort('startTime', 'DESC');
};


// prepare crystal class data
XRFSpectraGrid.prototype._setCrystalClass = function() {
	this.crystalClass = [];
	for (var i = 0; i < this.listOfCrystalClass.length; i++) {
		this.crystalClass.push(listOfCrystalClass[i].crystalClassName);
	}
};

// get the grids
XRFSpectraGrid.prototype.getGrid = function(xfeSpectrums) {
	this.features = xfeSpectrums;
	return this.renderGrid(xfeSpectrums);
};

// refresh data: reload
XRFSpectraGrid.prototype.refresh = function(data) {
	this.features = data.xrfSpectraList;
	this.store.loadData(this.features, false);
};


// no actions
XRFSpectraGrid.prototype.getActions = function() {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};


// builds the grid
XRFSpectraGrid.prototype.renderGrid = function() {
	var _this = this;

	/** Prepare data * */
	var data = [];
	// set the columns
	var columns = this._getColumns();
	// data for the grid
	this.store = Ext.create('Ext.data.Store', {
				model : 'XRFSpectraModel',
				autoload : false
			});

	this._sort(this.store);
	this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);

	//if no data : just a panel
	var noXFRDataPanel = Ext.create('Ext.Panel', {
				id : 'noXFRDataPanel',
				layout : {
					type : 'fit'
				},
				html : '<html><h4>No XRF Spectra found</h4></html>'
			});

	if (this.features.length == 0)
		return noXFRDataPanel;

	// this grid can be edited
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
				clicksToEdit : 1
			});

	// top bar with buttons
	var bar = [];
	if (this.canSave){
		bar.push({
							text : 'Save Comments',
							tooltip : 'Save XRF Spectra',
							icon : '../images/save.gif',
							handler : function() {
								_this.onSaveButtonClicked.notify({});
							}
						});
	}
	// creates the grid
	this.grid = Ext.create('Ext.grid.Panel', {
				style : {
					padding : 0
				},
				width : "100%",
				model : 'XRFSpectraModel',
				height : "100%",
				store : this.store,
				columns : columns,
				title : this.title,
				viewConfig : {
					stripeRows : true,
					enableTextSelection : true
				},
				selModel : {
					mode : 'SINGLE'
				},
				plugins : [cellEditing],
				tbar : bar

			});

	return this.grid;
};

// returns the json data with XRFSpectra
XRFSpectraGrid.prototype.getListXfeSpectra = function() {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};


// returns the sessionId 
XRFSpectraGrid.prototype.getSessionId = function() {
	return this.sessionId;
};


// builds columns
XRFSpectraGrid.prototype._getColumns = function() {
	var _this = this;
	
	// render date
	function renderStartDate(value, p, record) {
		return Ext.Date.format(value, 'd-m-Y H:i:s');
	}

	// render a column wrap
	function columnWrap(value) {
		if (value)
			return '<div style="white-space:normal !important;word-wrap: break-word;">' + value + '</div>';
		else
			return "";
	}

	// render crystal class column
	function renderCrystalClass(value, p, record) {
		if (record.data.crystalClass != null) {
			var code = record.data.crystalClass;
			for (var i = 0; i < _this.listOfCrystalClass.length; i++) {
				var c = _this.listOfCrystalClass[i].crystalClassCode;
				if (c == code) {
					return _this.listOfCrystalClass[i].crystalClassName;
				}
				return code;
			}
		} else {
			return "";
		}
	}

	// create reusable renderer
	Ext.util.Format.comboRenderer = function(combo) {
		return function(value) {
			var record = combo.findRecord(combo.valueField, value);
			return record ? record.get(combo.displayField) : combo.valueNotFoundText;
		};
	};

	// format at 3 digits
	function renderNumberFormat3(value, p, record) {
		if (value == null)
			return "";
		return Number(value).toFixed(3);
	}

	// format at 1 digit
	function renderNumberFormat1(value, p, record) {
		if (value == null)
			return "";
		return Number(value).toFixed(1);
	}

	// render spectrum image
	function renderSpectrum(value, p, record) {
		if (!record.data.jpegScanExists) {
			return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Spectrum not found:<br>" + value + "</h4></div>";
		} else {
			return "<a href='viewResults.do?reqCode=viewJpegImageFromFile&file=" + 
					record.data.jpegScanFileFullPathParser + 
					"'>" + 
					"<img width='150' height='110' src='imageDownload.do?reqCode=getImageJpgFromFile&file=" + 
					record.data.jpegScanFileFullPathParser + 
					"'  border='0' alt='Click to zoom the image' ></a>";
		}
	}

	// render html report
	function renderHtmlReport(value, p, record) {
		if (!record.data.annotatedPymcaXfeSpectrumExists) {
			return "<div style='white-space:normal !important;word-wrap: break-word;'><h4>Html report not found:<br>" + value + "</h4></div>";
		} else {
			return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
					"<a  href='viewDataCollection.do?reqCode=downloadFile&fullFilePath= " + 
					record.data.annotatedPymcaXfeSpectrum + 
					"'  styleClass='LIST'>" + 
					"<img src='../images/download.png' alt='Click to download the report' >" + 
					"</a><br>" + 
					"<a  href='viewResults.do?reqCode=displayHtmlFile&HTML_FILE=" + 
					record.data.annotatedPymcaXfeSpectrum + 
					"'  styleClass='LIST'>" + 
					record.data.shortFileNameAnnotatedPymcaXfeSpectrum + 
					"</a></div>";
		}
	}

	// create the combo instance

	var crystalClassStore = Ext.create('Ext.data.Store', {
				id : 0,
				fields : ['crystalClassCode', 'crystalClassName'],
				data : _this.listOfCrystalClass
			});

	var combo = new Ext.form.ComboBox({
				mode : 'local',
				emptyText : '---',
				store : crystalClassStore,
				valueField : 'crystalClassCode',
				displayField : 'crystalClassName',
				allowBlank : true,
				typeAhead : true,
				triggerAction : 'all'

			});
	
	// columns
	var columns = [{
				text : 'Energy<br>(keV)',
				dataIndex : 'energy',
				flex : 0.075,
				renderer : renderNumberFormat3,
				id : 'energy'
			}, {
				text : 'Sample name',
				dataIndex : 'sampleName',
				flex : 0.10
			},{
				text : 'Exposure<br>Time<br>(s)',
				dataIndex : 'exposureTime',
				flex : 0.075,
				id : 'exposureTime'
			}, {
				text : 'Start Time',
				dataIndex : 'startTime',
				renderer : Ext.util.Format.dateRenderer('d-m-Y H:i:s'),
				flex : 0.15
			},

			{
				text : 'End Time',
				dataIndex : 'endTime',
				renderer : Ext.util.Format.dateRenderer('d-m-Y H:i:s'),
				flex : 0.15
			}, {
				text : 'Beam&nbsp;size Hor.<br>(&#181m)',
				dataIndex : 'beamSizeHorizontal',
				flex : 0.15
			}, {
				text : 'Beam&nbsp;size Ver.<br>(&#181m)',
				dataIndex : 'beamSizeVertical',
				flex : 0.15
			}, {
				text : 'Transm.<br>Factor<br>(%)',
				dataIndex : 'beamTransmission',
				flex : 0.15
			}, {
				text : 'Spectrum',
				dataIndex : 'jpegScanFileFullPath',
				renderer : renderSpectrum,
				flex : 0.2
			}, {
				text : 'Html Report',
				dataIndex : 'annotatedPymcaXfeSpectrum',
				renderer : renderHtmlReport,
				flex : 0.15
			}];
	if (_this.isFx) {
		columns.push({
			text : 'Crystal class',
			dataIndex : 'crystalClass',
			flex : 0.2,
			editor : combo, // specify reference to combo instance
			renderer : Ext.util.Format.comboRenderer(combo)
				// pass combo instance to reusable renderer
			});
	}
	
	if (this.canSave){
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				editor : {
					xtype : 'textfield',
					allowBlank : true
				},
				flex : 0.2,
				renderer : columnWrap
			});
	}else{
		columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				flex : 0.2,
				renderer : columnWrap
			});
	}
	return columns;

};

// return the data
XRFSpectraGrid.prototype.getListXRFSpectra = function() {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};

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
// In  the Image tab, panel with the crystal snapshot
function XtalSnapshotPanel(args) {
	var _this = this;
	this.width = 1200;
	this.height = 250;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}

//builds and returns the main panel
XtalSnapshotPanel.prototype.getPanel = function(snapshot) {
	var _this = this;

	var items = [];
	// location
	if (snapshot) {
		items.push({
					xtype : 'displayfield',
					name : 'location',
					labelWidth : 175,
					width : this.width,
					fieldLabel : 'Expected Snapshots location',
					value : snapshot.fileLocation
				});
	}

	// snapshot image
	if (snapshot && snapshot.filePresent) {
		var image = Ext.create('Ext.draw.Component', {
			width : 316,
			height : 197,
			listeners : {
				el : {
					click : function() {
						window.location.href = 'viewResults.do?reqCode=viewJpegImageFromFile&file=' + snapshot.fileLocation;
					}
				}
			},
			items : [{
				type : 'image',
				src : 'imageDownload.do?reqCode=getImageJpgFromFile&file=' + snapshot.fileLocation,
				width : 316,
				height : 197
			}]
		});
		items.push(image);
	}

	// main panel
	_this.panel = Ext.create('Ext.Panel', {
				// width: this.width,
				// height: this.height,
				layout : 'fit',
				width:"100%",
				border : 0,
				items : items
			});

	return _this.panel;
};
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

Ext.Loader.setConfig({
			enabled : true
		});

Ext.QuickTips.init();

// main entry point for the autoproc ranking charts
var IspybAutoProcRanking = {
	start : function(targetId) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.util.*', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.util.*', 'Ext.state.*',
						'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.form.MultiSelect', 'Ext.ux.form.ItemSelector',
						'Ext.chart.*', 'Ext.Window',
						'Ext.layout.container.Fit', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);

		this.showChartTabs(targetId);
	},

	showChartTabs : function(targetId) {
		// object with the ajax calls to the server
		var autoProcRankingData = new AutoProcRankingData();
		// event listener: data for autoProc Ranking have been found
		autoProcRankingData.autoProcRankingRetrieved.attach(
				function(evt, data) {
					// bar char creation
					var barChart = new StackedBarChart(targetId);
					// radar chart
					var radarChart = new RadarChart(targetId);
					// parallel chart
					var parallelChart = new ChartPanel(targetId);
					// line chart
					var lineChart = new LineChart(targetId);
					// tabs creation
					var tabs = Ext.create('Ext.tab.Panel', {
								renderTo : targetId,
								width : 800,
								height : 500,
								activeTab : 0,
								defaults : {
									bodyPadding : 0
								},
								items : [{
											tabConfig : {
												id : 'barChart',
												title : "Stacked bar chart"
											},
											items : [barChart.getPanel(data)]
										}, {
											tabConfig : {
												id : 'radarChart',
												title : "Radar chart"
											},
											items : [radarChart.getPanel(data)]
										}, {
											tabConfig : {
												id : 'lineChart',
												title : "Line chart"
											},
											items : [lineChart.getPanel(data)]
										}]
							});
				});
		// call to retrieve the data for autoProcRanking
		autoProcRankingData.getAutoProcRanking();
	}

};

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
// Line chart for autoproc ranking chart
function LineChart(targetId) {
	var _this = this;
	this.targetId = targetId;
	this.store = null;
	this.chart = null;
	this.panel = null;
}

// build the data for the chart
LineChart.prototype.formatData = function(data) {
	var _this = this;
	var d = [];
	for (i = 0; i < data.length; i++) {
		var s = {
			dataCollectionId : data[i].dataCollectionId,
			imgPrefix : data[i].imagePrefix,
			rFactor : data[i].overallRFactorValue,
			highestResolution : data[i].highestResolutionValue,
			completeness : data[i].completenessValue
		};
		d.push(s);
	}

	_this.store = Ext.create('Ext.data.JsonStore', {
				fields : ['dataCollectionId', 'imgPrefix', 'rFactor',
						'highestResolution', 'completeness'],
				data : d
			});
};

// builds and returns the line chart
LineChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				theme : 'Category1',
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Numeric',
							minimum : 0,
							position : 'left',
							fields : ['rFactor', 'highestResolution',
									'completeness'],
							minorTickSteps : 1,
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['imgPrefix'],
							label : {
								font : '9px Arial',
								rotate : {
									degrees : 300
								}
							}

						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'imgPrefix',
							yField : 'rFactor',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							smooth : true,
							xField : 'imgPrefix',
							yField : 'highestResolution',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, {
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							smooth : true,
							xField : 'imgPrefix',
							yField : 'completeness',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};


//builds and returns the panel containing the line chart
LineChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.formatData(data);
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtnL') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtnL',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	this.panel = Ext.create('Ext.Panel', {
				width : 800, // panel's width
				height : 400,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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
// radar chart for the autoproc ranking 
function RadarChart(targetId) {
	var _this = this;
	this.targetId = targetId;
	this.store = null;
	this.chart = null;
	this.panel = null;
}


// format the data for the radar chart
RadarChart.prototype.formatData = function(data) {
	var _this = this;
	var d = [];
	for (i = 0; i < data.length; i++) {
		var s = {
			dataCollectionId : data[i].dataCollectionId,
			imgPrefix : data[i].imagePrefix + ' '+ data[i].dataCollectionNumber,
			rFactor : data[i].overallRFactorValue,
			highestResolution : data[i].highestResolutionValue,
			completeness : data[i].completenessValue
		};
		d.push(s);
	}

	_this.store = Ext.create('Ext.data.JsonStore', {
				fields : ['dataCollectionId', 'imgPrefix', 'rFactor',
						'highestResolution', 'completeness'],
				data : d
			});
};


// builds and returns the radar chart
RadarChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				id : 'chartCmp',
				xtype : 'chart',
				style : 'background:#fff',
				theme : 'Category2',
				insetPadding : 20,
				animate : true,
				store : _this.store,
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Radial',
							position : 'radial',
							label : {
								display : true
							}
						}],
				series : [{
							showInLegend : true,
							type : 'radar',
							xField : 'imgPrefix',
							yField : 'rFactor',
							style : {
								opacity : 0.4
							}
						}, {
							showInLegend : true,
							type : 'radar',
							xField : 'imgPrefix',
							yField : 'highestResolution',
							style : {
								opacity : 0.5
							}
						}, {
							showInLegend : true,
							type : 'radar',
							xField : 'imgPrefix',
							yField : 'completeness',
							style : {
								opacity : 0.4
							}
						}]
			});

	return _this.chart;

};


// builds and returns the panel containing the radar chart
RadarChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.formatData(data);
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtnR') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtnR',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	this.panel = Ext.create('Ext.Panel', {
				width : 800, // panel's width
				height : 400,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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
// stacked bar chart for the autoproc ranking 
function StackedBarChart(targetId) {
	var _this = this;
	this.targetId = targetId;
	this.store = null;
	this.chart = null;
	this.panel = null;
}

// format the data for the chart
StackedBarChart.prototype.formatData = function(data) {
	var _this = this;
	var d = [];
	for (i = 0; i < data.length; i++) {
		var s = {
			dataCollectionId : data[i].dataCollectionId,
			imgPrefix : data[i].imagePrefix + ' ' + data[i].dataCollectionNumber,
			rFactor : data[i].overallRFactorValue,
			highestResolution : data[i].highestResolutionValue,
			completeness : data[i].completenessValue
		};
		d.push(s);
	}

	_this.store = Ext.create('Ext.data.JsonStore', {
				fields : ['dataCollectionId', 'imgPrefix', 'rFactor',
						'highestResolution', 'completeness'],
				data : d
			});
};


// builds and returns the stacked bar chart
StackedBarChart.prototype.getChart = function() {
	var _this = this;
	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				animate : true,
				shadow : true,
				store : _this.store,
				legend : {
					position : 'right'
				},
				axes : [{
							type : 'Numeric',
							position : 'bottom',
							fields : ['rFactor', 'highestResolution',
									'completeness'],
							title : false,
							grid : true
						}, {
							type : 'Category',
							position : 'left',
							fields : ['imgPrefix'],
							title : false
						}],
				series : [{
							type : 'bar',
							axis : 'bottom',
							gutter : 80,
							xField : 'dataCollectionId',
							yField : ['rFactor', 'highestResolution',
									'completeness'],
							stacked : true,
							tips : {
								trackMouse : true,
								width : 65,
								height : 28,
								renderer : function(storeItem, item) {
									this.setTitle(String(item.value[1]));
								}
							}
						}]

			});
	return _this.chart;

};


// builds and returns the main panel containing the stacked bar chart
StackedBarChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.formatData(data);
	_this.getChart();
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtn') {
			_this.chart.save({
						type : 'image/png'
					});
		}
	};
	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtn',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	this.panel = Ext.create('Ext.Panel', {
				width : 800, // panel's width
				height : 400,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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
// panel that contains the different charts
function ChartPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "500";

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	this.data = null;
}

// builds the main panel
ChartPanel.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;
	var diffHandler = function(btn) {
		if (btn.id == 'saveAsImageBtn2') {

		}
	};

	var saveAsImgButton = new Ext.Button({
				id : 'saveAsImageBtn2',
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});

	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});

	// main panel creation
	this.panel = Ext.create('Ext.Panel', {
				id : 'panelParallelChart',
				width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : {
					type : 'vbox', // Arrange child items vertically
					align : 'stretch', // Each takes up full width
					bodyPadding : 0,
					border : 0
				},
				listeners : {
					'afterrender' : function(thisCmp) {
						_this.postRender();
					}
				},
				html : "<div id='mainContent'></div>"
			});
	return this.panel;
};


// render of the panel
ChartPanel.prototype.postRender = function() {
	var _this = this;
	var items = [];
	if (_this.data.length == 0) {
		items.push({
					border : 0,
					padding : 0,
					html : BUI.getWarningHTML("No data have been found.")
				});
	}
	items.push(new ParallelChart('mainContent').render(_this.data));
};

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
// panel containning the value of Rsymm Isigma (autoProcParamPanel) and the report panel (if autoproc is selected)
function AutoProcActionPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";

	
	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	this.autoProcReportPanel = null;
}


// builds and returns the main panel
AutoProcActionPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	// panel containing the Rsymm and ISig value
	_this.autoProcParamPanel = new AutoProcParamPanel();
	
				
	items.push(_this.autoProcParamPanel.getPanel(data));
	
	
	// main panel
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'hbox'
				},
				collapsible : true,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				items : items

			});
	// if needed, add the report panel
	_this.addReport(data);
	return _this.panel;
};

// add the report panel if a autoProc is selected
AutoProcActionPanel.prototype.addReport = function(data) {
	var _this = this;
	if (_this.autoProcReportPanel == null && data && data.autoProcDetail) {
		_this.autoProcReportPanel = new AutoProcReportPanel();
		_this.panel.add(_this.autoProcReportPanel.getPanel(data));
		_this.panel.doLayout();
	}
};

// an autoProc is selected => set the panel report
AutoProcActionPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	
	_this.addReport(data);
	_this.autoProcReportPanel.setSelectedAutoProc(data);
	_this.panel.doLayout();

};
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
// panel containing the left (data about the selected autoProc) and the right panel (files list)
function AutoProcDataPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";
	// left panel: info about autoProc
	this.leftPanel = null;
	// right panel: files
	this.rightPanel = null;
	// main panel
	this.panel = null;
	/** Events * */
	this.onAutoProcGraphSelected = new Event(this);

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
AutoProcDataPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	// left Panel
	_this.leftPanel = new AutoProcLeftPanel();
	// right Panel
	_this.rightPanel = new AutoProcRightPanel();
	// listen for events
	_this.rightPanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});
	// add the panels in the list
	items.push(_this.leftPanel.getPanel(data));
	items.push(_this.rightPanel.getPanel(data));
	// main panel
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'hbox'
				},
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				items : items

			});
	return _this.panel;
};


// autoProc selected => gives info to the right panel and to the left panel
AutoProcDataPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	_this.leftPanel.setSelectedAutoProc(data);
	_this.rightPanel.setSelectedAutoProc(data);
};

// a graph should be displayed => gives info to the right panel
AutoProcDataPanel.prototype.displayGraphAttachment = function(dataAttachment) {
	var _this = this;
	_this.rightPanel.displayGraphAttachment(dataAttachment);
};

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

// AutoProc File Panel (one of the tab), composed of a part with files and a graph part
function AutoProcFilePanel(args) {
	var _this = this;
	this.width = 600;
	this.height = 700;
	/** Events * */
	this.onAutoProcGraphSelected = new Event(this);

	// in case of no file
	this.noFilePanel = null;
	// panel with the files list
	this.filesPanel = null;
	// panel with graph
	this.graphPanel = null;
	// data
	this.data = null;
	
	this.autoProcPlotStore = null;
	this.autoProcPlot= null;
	this.graphCombo = null;

	this.bodyStyle = 'background-color:#dfe8f5;';

	/* type 1 for XDS, 2 for SCALE, 3 for SCALA/AIMLESS, 4 for SCALEPACK, 5 for TRUNCATE */
	this.type = 1;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.type != null) {
			this.type = args.type;
		}
	}

	// Define the model for a Criteria
	Ext.define('AutoProcPlotModel', {
				extend : 'Ext.data.Model',
				fields : [{
							type : 'string',
							name : 'autoProcProgramAttachmentId'
						}, {
							type : 'string',
							name : 'fileName'
						}]
			});
}


// returns true if we are in the correct step
AutoProcFilePanel.prototype.isAttachmentInStep = function(step) {
	return ((this.type == 1 && step == "XDS") ||
			(this.type == 2 && step == "XSCALE") ||
			(this.type == 3 && step == "SCALA") ||
			(this.type == 4 && step == "SCALEPACK") || 
			(this.type == 5 && step == "TRUNCATE"));
};


// returns the items to display
AutoProcFilePanel.prototype.getItems = function(data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	var items = [];
	//title for Input Files
	var inputF = {
		xtype : 'displayfield',
		fieldLabel : 'Input Files',
		labelStyle : labelStyle
	};
	// title for Log files
	var logF = {
		xtype : 'displayfield',
		fieldLabel : 'Log Files',
		labelStyle : labelStyle
	};
	// title for Output files
	var outputF = {
		xtype : 'displayfield',
		fieldLabel : 'Output Files',
		labelStyle : labelStyle
	};
	// title for Correction files
	var correctionF = {
		xtype : 'displayfield',
		fieldLabel : 'Correction Files',
		labelStyle : labelStyle,
		labelWidth: 300
	};

	var inputFiles = [];
	var logFiles = [];
	var outputFiles = [];
	var correctionFiles = [];
	var graphFiles = [];

	// iterates over the attachments and send them in the correct category (input, output, log or correction)
	if (data && data.autoProcDetail && data.autoProcDetail.autoProcProgAttachmentsWebBeans) {
		for (var i = 0; i < data.autoProcDetail.autoProcProgAttachmentsWebBeans.length; i++) {
			attachment = data.autoProcDetail.autoProcProgAttachmentsWebBeans[i];
			// right step?
			if (attachment.ispybAutoProcAttachment &&  _this.isAttachmentInStep(attachment.ispybAutoProcAttachment.step)) {
				var category = attachment.ispybAutoProcAttachment.fileCategory;
				var href = 'viewResults.do?reqCode=displayAutoProcProgAttachment&autoProcProgramAttachmentId=' +  attachment.autoProcProgramAttachmentId;
				if (category == "correction") {
					href = 'viewResults.do?reqCode=displayCorrectionFile&fileName=' + attachment.fileName;
				}
				var buttonsItems = [];
				// create the buttons corresponding to the file
				var button = new Ext.Button({
							text : attachment.fileName,
							href : href,
							hrefTarget : '_self',
							tooltip : attachment.ispybAutoProcAttachment.description
						});
				buttonsItems.push(button);
				// can this attachment have a graph?
				var hasGraph = attachment.ispybAutoProcAttachment.hasGraph;
				// if yes, we create the button
				if (hasGraph == true){
					var buttonGraph = new Ext.Button({
							id:''+attachment.autoProcProgramAttachmentId,
							name:''+attachment.fileName,
							tooltip : 'View Graph',
							icon : '../images/icon_graph.png',
							handler : function(args) {
								_this.updateGraph(args.name);
							},
							style : {
								marginLeft : '10px'
							}
						});
					buttonsItems.push(buttonGraph);
				}
				// panel containing the file button and graph eventually
				var panelFile = Ext.create('Ext.Panel', {
						layout : {
							type : 'hbox', // Arrange child items vertically
							align : 'LEFT', // Each takes up full width
							bodyPadding : 10
						},
						border : 0,
						bodyStyle : _this.bodyStyle,
						collapsible : false,
						frame : false,
						style : {
								marginBottom : '10px',
								marginTop : '10px'
							},
						items : buttonsItems
					});
				
				// choose the correct category
				if (category == "input") {
					inputFiles.push(panelFile);
				} else if (category == "output") {
					outputFiles.push(panelFile);
				} else if (category == "log") {
					logFiles.push(panelFile);
				} else if (category == "correction") {
					correctionFiles.push(panelFile);
				}
				
			}
		}
		// input
		if (inputFiles.length > 0) {
			items.push(inputF);
		}
		for (var i = 0; i < inputFiles.length; i++) {
			items.push(inputFiles[i]);
		}
		// log
		if (logFiles.length > 0) {
			items.push(logF);
		}
		for (var i = 0; i < logFiles.length; i++) {
			items.push(logFiles[i]);
		}
		// output
		if (outputFiles.length > 0) {
			items.push(outputF);
		}
		for (var i = 0; i < outputFiles.length; i++) {
			items.push(outputFiles[i]);
		}
		// correction
		if (correctionFiles.length > 0) {
			items.push(correctionF);
		}
		for (var i = 0; i < correctionFiles.length; i++) {
			items.push(correctionFiles[i]);
		}
		
	}
	return items;
};

// builds and returns the main panel
AutoProcFilePanel.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;

	// get the items to display
	var items = _this.getItems(data);
	// get 'No file' eventually
	var html = _this.getHtml(items);
	// get the layout, vbox or fit
	var typeLayout = _this.getLayout(items);

	// graph panel
	_this.graphPanel = Ext.create('Ext.Panel', {
				layout : {
					type : "fit"
				},
				bodyBorder : false,
				bodyStyle : _this.bodyStyle,
				collapsible : false,
				frame : false,
				items : []
			});

	// no file panel
	_this.noFilePanel = Ext.create('Ext.Panel', {
				layout : {
					type : "fit"
				},
				bodyBorder : false,
				bodyStyle : _this.bodyStyle,
				collapsible : false,
				frame : false,
				html : html

			});
	// panel with all buttons
	_this.filesPanel = Ext.create('Ext.Panel', {
				layout : {
					type : "vbox"
				},
				bodyBorder : false,
				bodyStyle : _this.bodyStyle,
				collapsible : false,
				frame : false,
				items : items

			});

	// main panel
	_this.panel = Ext.create('Ext.Panel', {
				layout : {
					type : "fit"
				},
				bodyStyle : _this.bodyStyle,
				collapsible : false,
				frame : false,
				items : [_this.noFilePanel, _this.filesPanel, _this.graphPanel]

			});
	return _this.panel;
};

// return the code for No File information
AutoProcFilePanel.prototype.getHtml = function(items) {
	var html = "";
	if (items.length == 0) {
		html = "<html><h4>No Files have been found</h4></html>";
	}
	return html;
};

// returns the layout, depending if there are file or not
AutoProcFilePanel.prototype.getLayout = function(items) {
	var typeLayout = "vbox";
	if (items.length == 0) {
		typeLayout = "fit";
	}
	return typeLayout;
};

// an autoProc has been selected => clear the panels and rebuild
AutoProcFilePanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	_this.data = data;
	// clear component in the files panel
	var f;
	while (f = _this.filesPanel.items.first()) {
		_this.filesPanel.remove(f, true);
	}
	_this.filesPanel.doLayout();
	var items = _this.getItems(data);
	var html = _this.getHtml(items);
	_this.filesPanel.add(items);
	_this.noFilePanel.update(html);
	// graph clean
	_this.cleanGraphPanel();
	_this.panel.doLayout();
};


// graph to update
AutoProcFilePanel.prototype.updateGraph = function(value) {
	var _this = this;
	var autoProcProgramAttachmentId = -1;
	for (var i = 0; i < _this.data.autoProcDetail.autoProcProgAttachmentsWebBeans.length; i++) {
		if (value == _this.data.autoProcDetail.autoProcProgAttachmentsWebBeans[i].fileName) {
			autoProcProgramAttachmentId = _this.data.autoProcDetail.autoProcProgAttachmentsWebBeans[i].autoProcProgramAttachmentId;
			break;
		}
	}
	if (autoProcProgramAttachmentId != -1) {
		_this.onAutoProcGraphSelected.notify({
					'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
				});
	}
};

// clean the graph panel
AutoProcFilePanel.prototype.cleanGraphPanel = function() {
	var _this = this;
	var f;
	while (f = _this.graphPanel.items.first()) {
		_this.graphPanel.remove(f, true);
	}
	_this.graphPanel.doLayout();
};

// display a graph
AutoProcFilePanel.prototype.displayGraphAttachment = function(dataAttachment) {
	var _this = this;
	_this.cleanGraphPanel();
	var items = [];
	var args = [];
	args.width = _this.width;
	args.height = _this.height;
	var autoProcProgramAttachmentGraphPanel = new AutoProcProgramAttachmentGraphPanel(args);
	items.push(autoProcProgramAttachmentGraphPanel.getPanel(dataAttachment));
	_this.graphPanel.add(items);
	_this.graphPanel.doLayout();
	_this.panel.doLayout();
};

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
// this grid contains the list of autoProc of the dataCollection
function AutoProcGrid(args) {

	/** Events * */
	this.onRowClicked = new Event(this);

	var contextPath = "";

	this.title = "Autoprocessing Summary (click on an entry for more details)";
	this.contextPath = "";
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
	}
	// selected Row
	this.selectedRow = null;
	// autoProc datamodel
	Ext.define('autoProcModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'autoProcId',
							mapping : 'autoProcId'
						}, {
							name : 'name',
							mapping : 'name'
						}, {
							name : 'spaceGroup',
							mapping : 'spaceGroup'
						}, {
							name : 'anomalous',
							mapping : 'anomalous'
						}, {
							name : 'cellA',
							mapping : 'cellA'
						}, {
							name : 'cellB',
							mapping : 'cellB'
						}, {
							name : 'cellC',
							mapping : 'cellC'
						}, {
							name : 'cellAlpha',
							mapping : 'cellAlpha'
						}, {
							name : 'cellBeta',
							mapping : 'cellBeta'
						}, {
							name : 'cellGamma',
							mapping : 'cellGamma'
						}]
			});
}


// no filters
AutoProcGrid.prototype.getFilterTypes = function() {
	return [];
};


// prepare data , format
AutoProcGrid.prototype._prepareData = function(data) {
	var d = [];
	if (data && data.autoProcList) {
		for (var i = 0; i < data.autoProcList.length; i++) {
			d.push(data.autoProcList[i]);
		}
	}
	return d;
};


// returns the grid
AutoProcGrid.prototype.getGrid = function(data) {
	this.features = this._prepareData(data);
	return this.renderGrid();
};


// no actions
AutoProcGrid.prototype.getActions = function() {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};

// builds the grid
AutoProcGrid.prototype.renderGrid = function() {
	var _this = this;

	/** Prepare data * */
	var columns = this._getColumns();
	//data
	this.store = Ext.create('Ext.data.Store', {
				model : 'autoProcModel',
				autoload : false,
				groupField : 'anomalous'
			});

	this._sort(this.store);
	this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);
	// group by anomalous
	var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
		groupHeaderTpl : '{columnName}: {name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
		hideGroupedHeader : true,
		startCollapsed : false
	}), groups = this.store.getGroups(), len = groups.length, i = 0, toggleGroup = function(
			item) {
		var groupName = item.text;
		if (item.checked) {
			groupingFeature.expand(groupName, true);
		} else {
			groupingFeature.collapse(groupName, true);
		}
	};
	
	// grid
	this.grid = Ext.create('Ext.grid.Panel', {
				style : {
					padding : 0
				},
				width : 600,
				model : 'autoProcModel',
				height : "100%",
				cls: 'autoproc-grid', 
				store : this.store,
				columns : columns,
				features : [groupingFeature],
				title : this.title,
				viewConfig : {
					stripeRows : true,
					enableTextSelection : true
				},
				selModel : {
					mode : 'SINGLE'
				},
				listeners : {
					itemclick : function(g, record, item, index, e, eOpts) {
						var selectedRec = g.getSelectionModel().getSelection()[0];
						_this.selectAutoProc(selectedRec.get('autoProcId'));
					}
				}
			});
	return this.grid;
};


// set the row selected
AutoProcGrid.prototype.setSelectedAutoProcId = function(autoProcIdSelected) {
	var _this = this;
	var rowIndex = -1;
	for (var i = 0; i < this.features.length; i++) {
		if (autoProcIdSelected == this.features[i].autoProcId) {
			rowIndex = i;
			break;
		}
	}
	if (rowIndex != -1) {
		_this.grid.getView().select(rowIndex);
	}
};


// select a autoProc
AutoProcGrid.prototype.selectAutoProc = function(autoProcId) {
	this.selectedRow = autoProcId;
	this.onRowClicked.notify({
				'autoProcId' : autoProcId
			});
};


// no sort
AutoProcGrid.prototype._sort = function(store) {
	// store.sort('totalIntegratedSignal', 'DESC');
};


// returns the columns 
AutoProcGrid.prototype._getColumns = function() {
	var _this = this;

	function columnWrap(value) {
		if (value)
			return '<div style="white-space:normal !important;">' + value + '</div>';
		else
			return "";
	}

	var columns = [{
				text : 'Method',
				dataIndex : 'name',
				flex : 0.4
			}, {
				text : 'Anomalous',
				dataIndex : 'anomalous',
				flex : 0.2
			}, {
				text : 'Point<br/>Group',
				dataIndex : 'spaceGroup',
				flex : 0.2
			}, {
				text : 'Cell<br/>A',
				dataIndex : 'cellA',
				flex : 0.1
			}, {
				text : 'Cell<br/>B',
				dataIndex : 'cellB',
				flex : 0.1
			}, {
				text : 'Cell<br/>C',
				dataIndex : 'cellC',
				flex : 0.1
			}, {
				text : 'Cell<br/>Alpha',
				dataIndex : 'cellAlpha',
				flex : 0.1
			}, {
				text : 'Cell<br/>Beta',
				dataIndex : 'cellBeta',
				flex : 0.1
			}, {
				text : 'Cell<br/>Gamma',
				dataIndex : 'cellGamma',
				flex : 0.1
			}];

	return columns;

};

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
// left Panel with the data about the autoProcessing
function AutoProcLeftPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";

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
AutoProcLeftPanel.prototype.getPanel = function(data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'vbox'
				},
				collapsible : false,
				frame : true,
				title : 'Main Output Parameters',
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 150
				},
				defaultType : 'textfield',
				items : [{
							xtype : 'displayfield',
							fieldLabel : 'Overall',
							labelStyle : labelStyle
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Overall Resolution',
							id : 'overallResolution',
							value : data.autoProcDetail.overallResolution
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Overall Completeness',
							id : 'overallCompleteness',
							value : data.autoProcDetail.overallCompleteness
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Overall I over Sigma',
							id : 'overallIOverSigma',
							value : data.autoProcDetail.overallIOverSigma
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Overall Rsymm',
							id : 'overallRsymm',
							value : data.autoProcDetail.overallRsymm
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Overall Multiplicity',
							id : 'overallMultiplicity',
							value : data.autoProcDetail.overallMultiplicity
						},

						{
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell',
							labelStyle : labelStyle
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell Resolution',
							id : 'outerResolution',
							value : data.autoProcDetail.outerResolution
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell Completeness',
							id : 'outerCompleteness',
							value : data.autoProcDetail.outerCompleteness
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell I over Sigma',
							id : 'outerIOverSigma',
							value : data.autoProcDetail.outerIOverSigma
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell Rsymm',
							id : 'outerRsymm',
							value : data.autoProcDetail.outerRsymm
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Outer Shell Multiplicity',
							id : 'outerMultiplicity',
							value : data.autoProcDetail.outerMultiplicity
						},

						{
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell',
							labelStyle : labelStyle
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell A',
							id : 'unitCellA',
							value : data.autoProcDetail.unitCellA
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell B',
							id : 'unitCellB',
							value : data.autoProcDetail.unitCellB
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell C',
							id : 'unitCellC',
							value : data.autoProcDetail.unitCellC
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell Alpha',
							id : 'unitCellAlpha',
							value : data.autoProcDetail.unitCellAlpha
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell Beta',
							id : 'unitCellBeta',
							value : data.autoProcDetail.unitCellBeta
						}, {
							xtype : 'displayfield',
							fieldLabel : 'Unit Cell Gamma',
							id : 'unitCellGamma',
							value : data.autoProcDetail.unitCellGamma
						}]

			});
	return _this.panel;
};


// autoProc has been selected => set the correct values
AutoProcLeftPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	var field = Ext.getCmp('overallResolution');
	field.setValue(data.autoProcDetail.overallResolution);

	field = Ext.getCmp('overallCompleteness');
	field.setValue(data.autoProcDetail.overallCompleteness);

	field = Ext.getCmp('overallIOverSigma');
	field.setValue(data.autoProcDetail.overallIOverSigma);

	field = Ext.getCmp('overallRsymm');
	field.setValue(data.autoProcDetail.overallRsymm);

	field = Ext.getCmp('overallMultiplicity');
	field.setValue(data.autoProcDetail.overallMultiplicity);

	field = Ext.getCmp('outerResolution');
	field.setValue(data.autoProcDetail.outerResolution);

	field = Ext.getCmp('outerCompleteness');
	field.setValue(data.autoProcDetail.outerCompleteness);

	field = Ext.getCmp('outerIOverSigma');
	field.setValue(data.autoProcDetail.outerIOverSigma);

	field = Ext.getCmp('outerRsymm');
	field.setValue(data.autoProcDetail.outerRsymm);

	field = Ext.getCmp('outerMultiplicity');
	field.setValue(data.autoProcDetail.outerMultiplicity);

	field = Ext.getCmp('unitCellA');
	field.setValue(data.autoProcDetail.unitCellA);

	field = Ext.getCmp('unitCellB');
	field.setValue(data.autoProcDetail.unitCellB);

	field = Ext.getCmp('unitCellC');
	field.setValue(data.autoProcDetail.unitCellC);

	field = Ext.getCmp('unitCellAlpha');
	field.setValue(data.autoProcDetail.unitCellAlpha);

	field = Ext.getCmp('unitCellBeta');
	field.setValue(data.autoProcDetail.unitCellBeta);

	field = Ext.getCmp('unitCellGamma');
	field.setValue(data.autoProcDetail.unitCellGamma);

};

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
// panel with the autoProc Grid 
function AutoProcListPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";
	this.autoProcGrid = null;
	/** Events * */
	this.onAutoProcSelected = new Event(this);

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
AutoProcListPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	var html = "";
	// there are some autoProcs => create the grid
	if (data && data.autoProcList && data.autoProcList.length > 0) {
		_this.autoProcGrid = new AutoProcGrid();
		_this.autoProcGrid.onRowClicked.attach(function(sender, args) {
					var autoProcId = args.autoProcId;
					_this.onAutoProcSelected.notify({
								'autoProcId' : autoProcId
							});
				});

		items.push(_this.autoProcGrid.getGrid(data));
	} else {
		// No autoProcessings
		html = '<html><h4>No Autoprocessings have been found</h4></html>';
	}

	_this.panel = Ext.create('Ext.Panel', {
				layout : {
					type : 'fit',
					border : 0,
					bodyPadding : 0
				},
				collapsible : false,
				frame : true,
				items : items,
				html : html
			});

	return _this.panel;
};


// set a autoProc selected, gives info to the grid
AutoProcListPanel.prototype.setSelectedAutoProcId = function(autoProcIdSelected) {
	var _this = this;
	if (_this.autoProcGrid)
		_this.autoProcGrid.setSelectedAutoProcId(autoProcIdSelected);
};
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
// panel with the Rsymm and I/Sigma values
function AutoProcParamPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";
	
	this.data = null;

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
AutoProcParamPanel.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'hbox'
				},
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				items : [{
							fieldLabel : 'RSymm threshold in lower shell',
							name : 'rsymm',
							anchor : '95%',
							value : data.rMerge
						}, {
							fieldLabel : 'I/Sigma threshold in lower shell',
							name : 'isigma',
							anchor : '95%',
							value : data.iSigma,
							style : {
								marginLeft : '10px'
							}
						}],
				buttons : [{
							text : 'Update',
							handler : function() {
								_this.update();
							}
						}]

			});
	return _this.panel;
};


// update the values => call to the server
AutoProcParamPanel.prototype.update = function() {
	var _this = this;
	var rMerge = Ext.getCmp(this.panel.getItemId()).getValues().rsymm;
	var iSigma = Ext.getCmp(this.panel.getItemId()).getValues().isigma;
	var dataCollectionId = _this.data.dataCollectionId;
	document.location.href =   "viewResults.do?reqCode=display&dataCollectionId="+dataCollectionId+"&rmerge="+rMerge+"&isigma="+iSigma;
	
};



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
// ajax call for graph 
function AutoProcProgramAttachmentGraph(arg) {
	this.type = "POST";
	this.json = arg;
	this.dataRetrieved = new Event(this);
	/** Error * */
	this.onError = new Event(this);
}

AutoProcProgramAttachmentGraph.prototype.getData = function(
		autoProcProgramAttachmentId) {
	var _this = this;
	var box = Ext.MessageBox.wait('Please wait...', 'Loading Data');
	$.ajax({
		type : this.type,
		url : "viewResults.do?reqCode=getAutoProcessingData&autoProcProgramAttachmentId="+ autoProcProgramAttachmentId,
		data : {},
		datatype : "text/json",
		success : function(data) {
			_this.dataRetrieved.notify(data);
			box.hide();
		},
		error : function(xhr, ajaxOptions, thrownError) {
			_this.onError.notify(xhr.responseText);
			box.hide();
		}
	});
};

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
// creates the chart foran attachment
function AutoProcProgramAttachmentGraphChart(args) {
	var _this = this;
	this.store = null;
	this.chart = null;
	this.panel = null;
	this.width = 990;
	this.height = 580;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}

// creates the chart
AutoProcProgramAttachmentGraphChart.prototype.getChart = function() {
	var _this = this;

	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				theme : 'Category1',
				axes : [{
							type : 'Numeric',
							position : 'left',
							fields : [_this.xField],
							title : _this.xTitle,
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['resolutionLimit'],
							title : 'Resolution',
							grid : true

						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'resolutionLimit',
							yField : _this.xField,
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};


// builds and returns the panel with the chart
AutoProcProgramAttachmentGraphChart.prototype.getPanel = function(data, xField,
		xTitle) {
	var _this = this;
	_this.store = data;
	_this.xField = xField;
	_this.xTitle = xTitle;
	// builds the chart
	_this.getChart();
	var diffHandler = function(btn) {
		_this.chart.save({
					type : 'image/png'
				});
	};
	var saveAsImgButton = new Ext.Button({
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});
	// panel
	this.panel = Ext.create('Ext.Panel', {
				width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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

// panel containing the graph
function AutoProcProgramAttachmentGraphPanel(args) {
	var _this = this;
	this.width = 1000;
	this.height = 600;
	this.store = null;
	this.changeSize = true;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.changeSize != null) {
			this.changeSize = args.changeSize;
		}
	}
}

// builds and resturns the main panel
AutoProcProgramAttachmentGraphPanel.prototype.getPanel = function(data) {
	var _this = this;
	var d = [];
	var wilsonD = [];
	var cumIntD = [];
	// format data, depending of the graph
	for (i = 0; i < data.autoProcessingData.length; i++) {
		var s = {
			resolutionLimit : data.autoProcessingData[i].resolutionLimit,
			completeness : data.autoProcessingData[i].completeness,
			rFactorObserved : data.autoProcessingData[i].rFactorObserved,
			iSigma : data.autoProcessingData[i].iSigma,
			cc2 : data.autoProcessingData[i].cc2,
			sigAno : data.autoProcessingData[i].sigAno,
			anomalCorr : data.autoProcessingData[i].anomalCorr,
			resolution : data.autoProcessingData[i].resolution,
			wilsonPlot : data.autoProcessingData[i].wilsonPlot,
			z: data.autoProcessingData[i].z,
			acent_theor: data.autoProcessingData[i].acent_theor,
			acent_twin: data.autoProcessingData[i].acent_twin,
			acent_obser: data.autoProcessingData[i].acent_obser,
			centric_theor: data.autoProcessingData[i].centric_theor,
			centric_obser: data.autoProcessingData[i].centric_obser
		};
		d.push(s);
		if (data.autoProcessingData[i].wilsonPlot != null){
			wilsonD.push(s);
		}
		if (data.autoProcessingData[i].z != null){
			cumIntD.push(s);
		}
	}

	// data, depending of the graph
	var store = Ext.create('Ext.data.JsonStore', {
				fields : ['resolutionLimit', 'completeness', 'rFactorObserved',
						'iSigma', 'cc2', 'sigAno', 'anomalCorr', 'resolution',
						'wilsonPlot', 'z', 'acent_theor', 'acent_twin', 'acent_obser', 'centric_theor', 'centric_obser'],
				data : d
			});
	
	var storeWilson = Ext.create('Ext.data.JsonStore', {
		fields : ['resolutionLimit', 'completeness', 'rFactorObserved',
				'iSigma', 'cc2', 'sigAno', 'anomalCorr', 'resolution',
				'wilsonPlot', 'z', 'acent_theor', 'acent_twin', 'acent_obser', 'centric_theor', 'centric_obser'],
		data : wilsonD
	});
	
	var storeCumInt = Ext.create('Ext.data.JsonStore', {
		fields : ['resolutionLimit', 'completeness', 'rFactorObserved',
				'iSigma', 'cc2', 'sigAno', 'anomalCorr', 'resolution',
				'wilsonPlot', 'z', 'acent_theor', 'acent_twin', 'acent_obser', 'centric_theor', 'centric_obser'],
		data : cumIntD
	});

	var args = [];
	//args.width = _this.width - 10;
	//args.height = _this.height - 250;
	args.width = _this.width ;
	args.height = _this.height;
	if (_this.changeSize){
		args.width = _this.width - 10;
		args.height = _this.height - 250;
	}
	
	// creates the graphs
	var completeness = new AutoProcProgramAttachmentGraphChart(args);
	var rfactor = new AutoProcProgramAttachmentGraphChart(args);
	var iSigma = new AutoProcProgramAttachmentGraphChart(args);
	var cc2 = new AutoProcProgramAttachmentGraphChart(args);
	var sigAno = new AutoProcProgramAttachmentGraphChart(args);
	var anomalCorr = new AutoProcProgramAttachmentGraphChart(args);
	var wilsonPlot = new WilsonChart(args);
	var cumulativeIntensityDistribution = new CumulativeIntensityDistributionChart(args);

	if (data && data.xscaleLpData) {
		// XSCALE: tab with graphs based on resolution
		var tabs = Ext.create('Ext.tab.Panel', {
			width : this.width,
			height : this.height,
			activeTab : 0,
			defaults : {
				bodyPadding : 0
			},
			items : [
			{
				tabConfig : {
					title : "I/SigI vs Resolution"
				},
				items : [iSigma.getPanel(store, 'iSigma', 'I/Sigl')]
			}, {
				tabConfig : {
					title : "Completeness vs Resolution"
				},
				items : [completeness.getPanel(store, 'completeness',
						'Completeness')]
			}, {
				tabConfig : {
					title : "R-factor vs Resolution"
				},
				items : [rfactor.getPanel(store, 'rFactorObserved', 'R-factor')]
			},  {
				tabConfig : {
					title : "CC/2 vs Resolution"
				},
				items : [cc2.getPanel(store, 'cc2', 'CC/2')]
			}, {
				tabConfig : {
					title : "SigAno vs Resolution"
				},
				items : [sigAno.getPanel(store, 'sigAno', 'SigAno')]
			}, {
				tabConfig : {
					title : "Anom Corr vs Resolution"
				},
				items : [sigAno.getPanel(store, 'anomalCorr', 'Anom Corr')]
			}]
		});

		// panel with the tabs
		_this.panel = Ext.create('Ext.Panel', {
					layout : 'fit',
					width : this.width, // panel's width
					height : this.height,// panel's height
					items : [tabs]
				});
	} else if (data && data.truncateLogData) {
		// Wilson plot or Cumulative Intensity
		var tabs = Ext.create('Ext.tab.Panel', {
			width : this.width,
			height : this.height,
			activeTab : 0,
			defaults : {
				bodyPadding : 0
			},
			items : [{
				tabConfig : {
					title : "Wilson Plot"
				},
				items : [wilsonPlot.getPanel(storeWilson)]
			}, 
			{
				tabConfig : {
					title : "Cumulative Intensity Distribution"
				},
				items : [cumulativeIntensityDistribution.getPanel(storeCumInt)]
			}]
		});

		// main panel
		_this.panel = Ext.create('Ext.Panel', {
					layout : 'fit',
					width : this.width, // panel's width
					height : this.height,// panel's height
					items : [tabs]
				});
		
	}

	return _this.panel;
};
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
// panel with the report buttons
function AutoProcReportPanel(args) {
	var _this = this;
	this.width = "410";
	this.height = "100";

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	this.panel = null;
}

// builds the main panel
AutoProcReportPanel.prototype.getPanel = function(data) {
	var _this = this;
	var title = "<p>View Data Collection Statistics</p>";
	// get the itesm
	var items = _this.getItems(data);
	
	// panel
	_this.panelReport = Ext.create('Ext.Panel', {
		layout : {
			type : 'hbox',
			border : 0,
			bodyPadding : 10
		},
		border:0,
		bodyStyle : 'background-color:#dfe8f5;',
		collapsible : false,
		frame : false,
		items : items
	});
	// main panel (title + buttons)
	_this.panel = Ext.create('Ext.form.Panel', {
		layout : {
			type : 'vbox',
			border : 0,
			bodyPadding : 40
		},
		border:0,
		style : {
			marginLeft : '10px' 
		},
		fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 200
		},
		collapsible : false,
		frame : true,
		items : [
				{
					xtype : 'displayfield',
					fieldLabel : 'View Data Collection Statistics',
					labelStyle : 'font-weight:bold;',
					value : ''
				},
				_this.panelReport
				]
	});
	return _this.panel;
};


// builds the items
AutoProcReportPanel.prototype.getItems = function(data) {
	var _this = this;
	var autoprocId = null;
	if (data && data.autoProcDetail) {
		autoprocId = data.autoProcDetail.autoProcId;
	}
	var contextPath = "";
	if (data && data.contextPath) {
		contextPath = data.contextPath;
	}
	var items = [];
	
	// 2 buttons
	var buttonDOC = new Ext.Button({
		icon : '../images/Word.png',
		href : contextPath +"/user/exportAutoProc.do?reqCode=exportAutoProcAsRtf&autoProcId=" +autoprocId,
		hrefTarget : '_self',
		scale:'large',
		tooltip : 'Export Auto Processing into DOC'
	});
	var buttonPDF = new Ext.Button({
		icon : '../images/pdf.png',
		href : contextPath +"/user/exportAutoProc.do?reqCode=exportAutoProcAsPdf&autoProcId=" +autoprocId,
		hrefTarget : '_self',
		scale:'large',
		style : {
			marginLeft : '10px'
		},
		tooltip : 'Export Auto Processing into PDF'
	});
	
	items.push(buttonDOC, buttonPDF);
	return items;
	
};


// autoProc has been selected => update the panel
AutoProcReportPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	var f;
	while (f = _this.panelReport.items.first()) {
		_this.panelReport.remove(f, true);
	}
	var items = _this.getItems(data);
	_this.panelReport.add(items);
	_this.panelReport.doLayout();
	_this.panel.doLayout();
};
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

// Contains the files panel (all tabs)
function AutoProcRightPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";
	/** Events * */
	this.onAutoProcGraphSelected = new Event(this);

	this.data = null;
	this.tabs = null;

	// the different panels in the tab
	this.xdsPanel = null;
	this.xscalePanel = null;
	this.scalaPanel = null;
	this.scalePackPanel = null;
	this.truncatePanel = null;

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
AutoProcRightPanel.prototype.getPanel = function(data) {
	var _this = this;
	_this.data = data;
	var args = [];
	// XDS
	args.type = 1;
	_this.xdsPanel = new AutoProcFilePanel(args);
	_this.xdsPanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});
	// XSCALE
	args = [];
	args.type = 2;
	_this.xscalePanel = new AutoProcFilePanel(args);
	_this.xscalePanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});
	// SCALA / AIMLESS
	args = [];
	args.type = 3;
	_this.scalaPanel = new AutoProcFilePanel(args);
	_this.scalaPanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});
	// SCALEPACK
	args = [];
	args.type = 4;
	_this.scalePackPanel = new AutoProcFilePanel(args);
	_this.scalePackPanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});
	// TRUNCATE
	args = [];
	args.type = 5;
	_this.truncatePanel = new AutoProcFilePanel(args);
	_this.truncatePanel.onAutoProcGraphSelected.attach(function(sender, args) {
				var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
				_this.onAutoProcGraphSelected.notify({
							'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
						});
			});

	// tabs
	_this.tabs = Ext.create('Ext.tab.Panel', {
				activeTab : 0,
				width : 600,
				bodyStyle : 'background-color:#dfe8f5;',
				defaults : {
					bodyPadding : 0,
					autoScroll : true
				},
				items : [{
							title : 'XDS',
							items : [_this.xdsPanel.getPanel(data)]
						}, {
							title : 'XSCALE',
							items : [_this.xscalePanel.getPanel(data)]
						}, {
							title : 'SCALA/AIMLESS',
							items : [_this.scalaPanel.getPanel(data)]
						}, {
							title : 'SCALEPACK',
							items : [_this.scalePackPanel.getPanel(data)]
						}, {
							title : 'TRUNCATE',
							items : [_this.truncatePanel.getPanel(data)]
						}

				]
			});
	// main panel with tabs
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'fit'
				},
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				title : 'Files',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 90
				},
				defaultType : 'textfield',
				tbar : [{
					text : 'Download',
					tooltip : 'Download all attachments for this autoprocessing',
					icon : '../images/download.png',
					href : 'viewResults.do?reqCode=downloadAttachment&autoProcId=' + _this.data.autoProcDetail.autoProcId

				}],
				items : [_this.tabs]

			});
		
	return _this.panel;
};


// autoProc has been selected => inform the different panels
AutoProcRightPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	_this.xdsPanel.setSelectedAutoProc(data);
	_this.xscalePanel.setSelectedAutoProc(data);
	_this.scalaPanel.setSelectedAutoProc(data);
	_this.scalePackPanel.setSelectedAutoProc(data);
	_this.truncatePanel.setSelectedAutoProc(data);
	_this.tabs.setActiveTab(0);
	_this.panel.doLayout();
};

// graph to be displayed, inform the current tab (activ)
AutoProcRightPanel.prototype.displayGraphAttachment = function(dataAttachment) {
	var _this = this;
	var activTab = _this.tabs.getActiveTab();
	var idx = _this.tabs.items.indexOf(activTab);
	var activPanel = null;
	if (idx == 0) {
		activPanel = _this.xdsPanel;
	} else if (idx == 1) {
		activPanel = _this.xscalePanel;
	} else if (idx == 2) {
		activPanel = _this.scalaPanel;
	} else if (idx == 3) {
		activPanel = _this.scalePackPanel;
	}else if (idx == 4) {
		activPanel = _this.truncatePanel;
	}
	if (activPanel != null) {
		activPanel.displayGraphAttachment(dataAttachment);
	}
	_this.panel.doLayout();
};

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
// Grid with the autoProc status
function AutoProcStatusGrid(args) {

	var contextPath = "";

	this.title = "Status History";
	this.contextPath = "";
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
	}

	this.width = 700;

	// status datamodel
	Ext.define('statusModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'blTimeStamp',
							mapping : 'blTimeStamp',
							type : 'date',
							dateFormat : 'd-m-Y H:i:s'
						}, {
							name : 'step',
							mapping : 'step'
						}, {
							name : 'status',
							mapping : 'status'
						}, {
							name : 'comments',
							mapping : 'comments'
						}]
			});
}

// no filter
AutoProcStatusGrid.prototype.getFilterTypes = function() {
	return [];
};

// get the grid
AutoProcStatusGrid.prototype.getGrid = function(data) {
	this.features = data;
	return this.renderGrid(this.features);
};


// no actions
AutoProcStatusGrid.prototype.getActions = function() {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};


// builds the grid
AutoProcStatusGrid.prototype.renderGrid = function() {
	var _this = this;

	/** Prepare data * */
	var columns = this._getColumns();
	this.store = Ext.create('Ext.data.Store', {
				model : 'statusModel',
				autoload : false
			});

	this._sort(this.store);
	this.store.loadData(this.features, false);

	this.grid = Ext.create('Ext.grid.Panel', {
				style : {
					padding : 0
				},
				width : this.width,
				model : 'statusModel',
				height : "100%",
				store : this.store,
				columns : columns,
				// title : this.title,
				viewConfig : {
					stripeRows : true,
					enableTextSelection : true
				},
				selModel : {
					mode : 'SINGLE'
				}

			});
	return this.grid;
};


// no sort by default
AutoProcStatusGrid.prototype._sort = function(store) {
	// store.sort('totalIntegratedSignal', 'DESC');
};


// get the columns
AutoProcStatusGrid.prototype._getColumns = function() {
	var _this = this;

	function columnWrap(value) {
		if (value)
			return '<div style="white-space:normal !important;">' + value+ '</div>';
		else
			return "";
	}

	var columns = [{
				text : 'Date',
				dataIndex : 'blTimeStamp',
				flex : 0.2,
				renderer : Ext.util.Format.dateRenderer('d-m-Y H:i:s')
			}, {
				text : 'Step',
				dataIndex : 'step',
				flex : 0.1
			}, {
				text : 'Status',
				dataIndex : 'status',
				flex : 0.1
			}, {
				text : 'Comments',
				dataIndex : 'comments',
				flex : 0.3
			}];

	return columns;

};


// autoProc has been selected => update the data
AutoProcStatusGrid.prototype.setSelectedAutoProc = function(data) {
	this.features = data;
	this.store.loadData(this.features, false);
};

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
// panel containing the status Grid
function AutoProcStatusPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";

	this.autoProcStatusGrid = null;

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
AutoProcStatusPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	// no status 
	var html = _this.getHtml(data);
	// if status, creates the grids
	if (_this.hasStatusData(data)) {
		_this.autoProcStatusGrid = new AutoProcStatusGrid();
		var dataGrid = _this.getDataGrid(data);
		items.push(_this.autoProcStatusGrid.getGrid(dataGrid));
	}

	// main panel
	_this.panel = Ext.create('Ext.Panel', {
				layout : {
					type : 'fit',
					border : 0,
					bodyPadding : 0
				},
				title : 'Status History',
				collapsible : true,
				frame : true,
				items : items,
				html : html
			});

	return _this.panel;
};


// returns true if there are some status information
AutoProcStatusPanel.prototype.hasStatusData = function(data) {
	return (data && data.autoProcDetail && data.autoProcDetail.autoProcEvents && data.autoProcDetail.autoProcEvents.length > 0);
};


// return text for No status
AutoProcStatusPanel.prototype.getHtml = function(data) {
	var _this = this;
	var html = "";
	if (!_this.hasStatusData(data)) {
		html = '<html><h4>No Status has been found</h4></html>';
	}
	return html;
};


// prepare date for the grid
AutoProcStatusPanel.prototype.getDataGrid = function(data) {
	var d = new Array();
	if (data && data.autoProcDetail && data.autoProcDetail.autoProcEvents) {
		for (var i = 0; i < data.autoProcDetail.autoProcEvents.length; i++) {
			d.push(data.autoProcDetail.autoProcEvents[i]);
		}
	}
	return d;
};

// a autoProc has been selected => update the panel and the grid
AutoProcStatusPanel.prototype.setSelectedAutoProc = function(data) {
	var _this = this;
	var html = _this.getHtml(data);
	_this.panel.update(html);
	if (_this.hasStatusData(data)) {
		if (_this.autoProcStatusGrid == null) { // grid doesn't exist => create
			var items = new Array();
			_this.autoProcStatusGrid = new AutoProcStatusGrid();
			var dataGrid = _this.getDataGrid(data);
			items.push(_this.autoProcStatusGrid.getGrid(dataGrid));
			_this.panel.add(items);
		} else { // grid exsit already => just update
			var dataGrid = _this.getDataGrid(data);
			_this.autoProcStatusGrid.setSelectedAutoProc(dataGrid);
		}
	} else { // no data to display
		// clear component:
		var f;
		while (f = _this.panel.items.first()) {
			_this.panel.remove(f, true);
		}
		_this.autoProcStatusGrid = null;
	}

	_this.panel.doLayout();
};
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
// main autoProcessing panel, composed with the list of autoProcs, the actionPanel (rsymm, ISig and the reports)
// the data (right and left panel) and the status of autoProc and the interrupted autoProc
function AutoprocessingPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";
	/** Events * */
	this.onAutoProcSelected = new Event(this);
	this.onAutoProcGraphSelected = new Event(this);

	// panels inside:
	// list of autoProc
	this.autoProcListPanel = null;
	// ISig, rsymm panel and reports
	this.autoProcActionPanel = null;
	// infor about autoProc + files
	this.autoProcDataPanel = null;
	// status
	this.autoProcStatusPanel = null;
	// interrupted autoProc
	this.interruptedAutoProcPanel = null;
	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}

//builds and returns the main panel
AutoprocessingPanel.prototype.getPanel = function(data) {
	var _this = this;
	// list of autoProcessing
	_this.autoProcListPanel = new AutoProcListPanel();
	// lsiten for events
	_this.autoProcListPanel.onAutoProcSelected.attach(function(sender, args) {
				var autoProcId = args.autoProcId;
				_this.onAutoProcSelected.notify({
							'autoProcId' : autoProcId
						});
			});

	var items = [];
	// add to the list
	items.push(_this.autoProcListPanel.getPanel(data));

	// if we have some autoProc, add the Rymm and ISig panel, (and reports if autoProc is selected)
	if (data && data.autoProcList) {
		_this.autoProcActionPanel = new AutoProcActionPanel();
		
		items.push(_this.autoProcActionPanel.getPanel(data));
	}
	// if interrupted autoProc, add them 
	if (data && data.interruptedAutoProcEvents && data.interruptedAutoProcEvents.length > 0){
		_this.interruptedAutoProcPanel = new InterruptedAutoProcPanel();
		items.push(_this.interruptedAutoProcPanel.getPanel(data));
	}

	// main panel
	_this.panel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'vbox'
					// align: 'stretch' // Child items are stretched to full
					// width
				},
				collapsible : false,
				frame : true,
				items : items

			});

	return _this.panel;
};


// a autoProc is selected => gives info to the panel 
AutoprocessingPanel.prototype.setSelectedAutoProcId = function(
		autoProcIdSelected) {
	var _this = this;
	_this.autoProcListPanel.setSelectedAutoProcId(autoProcIdSelected);
};

// a graph should be displayed => gives info to the data panel (then to right panel)
AutoprocessingPanel.prototype.displayGraphAttachment = function(dataAttachment) {
	var _this = this;
	_this.autoProcDataPanel.displayGraphAttachment(dataAttachment);
};

// return true if there is some events linked to the autoProc
AutoprocessingPanel.prototype.hasStatusData = function(data) {
	return (data && data.autoProcDetail && data.autoProcDetail.autoProcEvents && data.autoProcDetail.autoProcEvents.length > 0);
};


// display a given autoProcessing
AutoprocessingPanel.prototype.displayAutoProc = function(data) {
	var _this = this;
	Ext.suspendLayouts();
	// select it in the list if needed
	if (_this.autoProcActionPanel != null) {
		_this.autoProcActionPanel.setSelectedAutoProc(data);
	}
	// creates if needed the data panel, and gives info about the selection
	if (_this.autoProcDataPanel == null) {
		_this.autoProcDataPanel = new AutoProcDataPanel();
		_this.autoProcDataPanel.onAutoProcGraphSelected.attach(function(sender,
				args) {
			var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
			_this.onAutoProcGraphSelected.notify({
						'autoProcProgramAttachmentId' : autoProcProgramAttachmentId
					});
		});
		_this.panel.add(_this.autoProcDataPanel.getPanel(data));
	} else {  
		_this.autoProcDataPanel.setSelectedAutoProc(data);
	}
	// there are some events linked to the autoProc => set the status panel if needed
	if (_this.hasStatusData(data)){
		if (_this.autoProcStatusPanel == null) {
			_this.autoProcStatusPanel = new AutoProcStatusPanel();
			_this.panel.add(_this.autoProcStatusPanel.getPanel(data));
		} else {
			_this.autoProcStatusPanel.setSelectedAutoProc(data);
		}
	}else{
		if (_this.autoProcStatusPanel == null) {
		} else {
			_this.panel.remove(_this.panel.items.last(), true);
			_this.autoProcStatusPanel = null;
		}
	}
	// redesign
	Ext.resumeLayouts();
	_this.panel.doLayout();
};
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
// Beamline Parameters tab
function BeamlinePanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";

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
BeamlinePanel.prototype.getPanel = function(data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	var marginBottom = "0 0 20 0";
	var dataCollection = data.dataCollection;
	var beamline = data.beamline;
	var session = data.session;
	var detector = data.detector;
	
	var undulatorTypes = "";
	if (beamline){
		if (beamline.undulatorType1){
			undulatorTypes += beamline.undulatorType1+" ";
		}
		if (beamline.undulatorType2){
			undulatorTypes += beamline.undulatorType2+" ";
		}
		if (beamline.undulatorType3){
			undulatorTypes += beamline.undulatorType3+" ";
		}
	}
	
	var flux = "";
	if (dataCollection && dataCollection.flux){
		flux = dataCollection.flux.toExponential() +"  photons/sec";
	}
	
	var flux_end = "";
	if (dataCollection && dataCollection.flux_end){
		flux_end = dataCollection.flux_end.toExponential() +"  photons/sec";
	}
	
	var detectorType = "";
	var detectorName = "";
	var detectorManufacturer = "";
	var detectorMode = "";
	if (detector){
		detectorType = detector.detectorType;
		detectorName = detector.detectorModel;
		detectorManufacturer = detector.detectorManufacturer;
		detectorMode = detector.detectorMode;
	}
	
	var items = [];
	items.push(
			{
				xtype : 'displayfield',
				name : 'synchrotronName',
				fieldLabel : 'Synchrotron name',
				labelStyle : labelStyle,
				value : beamline?beamline.synchrotronName:""
			},
			{
				xtype : 'displayfield',
				name : 'synchrotronMode',
				fieldLabel : 'Synchrotron filling mode',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.synchrotronMode:"",
				margin: marginBottom
			}
	);
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'beamlineName',
				fieldLabel : 'Beamline name',
				labelStyle : labelStyle,
				value : session?session.beamlineName:""
			},
			{
				xtype : 'displayfield',
				name : 'undulatorTypes',
				fieldLabel : 'Undulator types',
				labelStyle : labelStyle,
				value : undulatorTypes
			},
			{
				xtype : 'displayfield',
				name : 'undulatorGaps',
				fieldLabel : 'Undulator gaps',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.undulatorGaps:"",
				margin: marginBottom
			}
	);
	
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'beamTransmission',
				fieldLabel : 'Beam transmission',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.transmission?(dataCollection.transmission.toFixed(0)+ " %" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'slitGapHorizontalMicro',
				fieldLabel : 'Slit gap Hor',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.slitGapHorizontalMicro?(dataCollection.slitGapHorizontalMicro+ " &mu;m" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'slitGapVerticalMicro',
				fieldLabel : 'Slit gap Vert',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.slitGapVerticalMicro?(dataCollection.slitGapVerticalMicro+ " &mu;m" ):""):"",
				margin: marginBottom
			}
	);
	
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'detectorType',
				fieldLabel : 'Detector type',
				labelStyle : labelStyle,
				value : detectorType 
			},
			{
				xtype : 'displayfield',
				name : 'detectorName',
				fieldLabel : 'Detector name',
				labelStyle : labelStyle,
				value : detectorName 
			}, 
			{
				xtype : 'displayfield',
				name : 'detectorManufacturer',
				fieldLabel : 'Detector manufacturer',
				labelStyle : labelStyle,
				value : detectorManufacturer 
			},
			{
				xtype : 'displayfield',
				name : 'detectorMode',
				fieldLabel : 'Detector mode',
				labelStyle : labelStyle,
				value : detectorMode 
			}, 
			{
				xtype : 'displayfield',
				name : 'detectorPixelSizeHorizontalMicro',
				fieldLabel : 'Detector pixel size Hor',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.detectorPixelSizeHorizontalMicro?(dataCollection.detectorPixelSizeHorizontalMicro +"  mm"):""):""
			}, 
			{
				xtype : 'displayfield',
				name : 'detectorPixelSizeVerticalMicro',
				fieldLabel : 'Detector pixel size Vert',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.detectorPixelSizeVerticalMicro?(dataCollection.detectorPixelSizeVerticalMicro +"  mm"):""):"", 
				margin: marginBottom
			}
	);
	
	
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'focusingOptic',
				fieldLabel : 'Focusing optics',
				labelStyle : labelStyle,
				value : beamline?beamline.focusingOptic:""
			},
			{
				xtype : 'displayfield',
				name : 'monochromatorType',
				fieldLabel : 'Monochromator type',
				labelStyle : labelStyle,
				value : beamline?beamline.monochromatorType :""
			},
			{
				xtype : 'displayfield',
				name : 'beamShape',
				fieldLabel : 'Beam shape',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.beamShape :""
			},
			{
				xtype : 'displayfield',
				name : 'flux',
				fieldLabel : 'Flux',
				labelStyle : labelStyle,
				value : flux
			},
			{
				xtype : 'displayfield',
				name : 'fluxEnd',
				fieldLabel : 'Flux end',
				labelStyle : labelStyle,
				value :flux_end 
			},
			{
				xtype : 'displayfield',
				name : 'beamSizeAtSampleXMicro',
				fieldLabel : 'Beam size at sample Hor',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.beamSizeAtSampleXMicro?(dataCollection.beamSizeAtSampleXMicro  + " &mu;m" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'beamSizeAtSampleYMicro',
				fieldLabel : 'Beam size at sample Vert',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.beamSizeAtSampleYMicro?(dataCollection.beamSizeAtSampleYMicro  + " &mu;m" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'beamDivergenceHorizontalInt',
				fieldLabel : 'Beam divergence Hor',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.beamDivergenceHorizontalInt?(dataCollection.beamDivergenceHorizontalInt  + " &mu;rad" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'beamDivergenceVerticalInt',
				fieldLabel : 'Beam divergence Vert',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.beamDivergenceVerticalInt?(dataCollection.beamDivergenceVerticalInt  + " &mu;rad" ):""):""
			},
			{
				xtype : 'displayfield',
				name : 'polarisation',
				fieldLabel : 'Polarisation',
				labelStyle : labelStyle,
				value : beamline?(beamline.polarisation?(beamline.polarisation  + " &deg"):""):""
			}
	);
	_this.panel = Ext.create('Ext.form.Panel', {
				layout : 'vbox',
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 200
				},
				defaultType : 'textfield',
				items : items

			});

	return _this.panel;
};
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
// EDNA param panel tab
function CharacterisationParamPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";

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
CharacterisationParamPanel.prototype.getPanel = function(data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	var marginBottom = "0 0 20 0";
	var fieldStyleIndexing = 'color: black';
	var dataCollection = data.dataCollection;
	
	
	var screeningOutput = dataCollection.screeningOutput;
	var indexingStatus = "";
	var resolutionObserved = "";
	var mosaicity = "";
	var ioverSigma  = "N/A";
	var numSpotsUsed = "";
	var numSpotsFound = "";
	var spotDeviationTheta = "";
	var spotDeviationR = "";
	var beamShiftX  = "";
	var beamShiftY = "";
	var rankingResolution = "";
	if (screeningOutput){
		indexingStatus = screeningOutput.statusDescription;
		if (screeningOutput.mosaicity)
			mosaicity = screeningOutput.mosaicity.toFixed(2)+" &deg;";
		if (screeningOutput.resolutionObtained)
			resolutionObserved = screeningOutput.resolutionObtained+" &Aring;";
		if (screeningOutput.ioverSigma)
			ioverSigma = screeningOutput.ioverSigma;
		if (screeningOutput.numSpotsUsed)
			numSpotsUsed = screeningOutput.numSpotsUsed;
		if (screeningOutput.numSpotsFound)
			numSpotsFound = screeningOutput.numSpotsFound;	
		if (screeningOutput.spotDeviationTheta)
			spotDeviationTheta = screeningOutput.spotDeviationTheta.toFixed(2)+" &deg;";
		if (screeningOutput.spotDeviationR)
			spotDeviationR = screeningOutput.spotDeviationR;
		if (screeningOutput.beamShiftX)
			beamShiftX = screeningOutput.beamShiftX;	
		if (screeningOutput.beamShiftY)
			beamShiftY = screeningOutput.beamShiftY;	
		if (screeningOutput.rankingResolution)
			rankingResolution = screeningOutput.rankingResolution.toFixed(2)+" &Aring;";	
		if (screeningOutput.indexingSuccess){
			if (screeningOutput.indexingSuccess == 0){
				fieldStyleIndexing = 'color: red';
			}else{
				fieldStyleIndexing = 'color: green';
			}
		}
	}
	
	var screeningOutputLattice = dataCollection.lattice;
	var spaceGroup = "";
	var unitCellA = "";
	var unitCellB = "";
	var unitCellC = "";
	var unitCellAlpha = "";
	var unitCellBeta = "";
	var unitCellGamma = "";
	if (screeningOutputLattice){
		spaceGroup = screeningOutputLattice.spaceGroup;
		unitCellA = screeningOutputLattice.unitCell_a.toFixed(3)+" &Aring;";
		unitCellB = screeningOutputLattice.unitCell_b.toFixed(3)+" &Aring;";
		unitCellC = screeningOutputLattice.unitCell_c.toFixed(3)+" &Aring;";
		unitCellAlpha = screeningOutputLattice.unitCell_alpha.toFixed(3)+ " &deg";
		unitCellBeta = screeningOutputLattice.unitCell_beta.toFixed(3)+ " &deg";
		unitCellGamma = screeningOutputLattice.unitCell_gamma.toFixed(3)+ " &deg";
	}
	 
	
	var items = [];
	items.push({
					xtype : 'displayfield',
					name : 'indexingStatus',
					fieldLabel : 'Indexing status',
					fieldStyle: fieldStyleIndexing,
					labelStyle : labelStyle,
					value : indexingStatus
				}, 
				{
					xtype : 'displayfield',
					name : 'spaceGroup',
					fieldLabel : 'Spacegroup',
					labelStyle : labelStyle,
					value : spaceGroup
				}, 
				{
					xtype : 'displayfield',
					name : 'unitCellA',
					fieldLabel : 'Unit Cell a',
					labelStyle : labelStyle,
					value : unitCellA
				} ,
				{
					xtype : 'displayfield',
					name : 'unitCellB',
					fieldLabel : 'Unit Cell b',
					labelStyle : labelStyle,
					value : unitCellB
				},
				{
					xtype : 'displayfield',
					name : 'unitCellC',
					fieldLabel : 'Unit Cell c',
					labelStyle : labelStyle,
					value : unitCellC
				},
				{
					xtype : 'displayfield',
					name : 'unitCellAlpha',
					fieldLabel : 'Unit Cell alpha',
					labelStyle : labelStyle,
					value : unitCellAlpha
				},
				{
					xtype : 'displayfield',
					name : 'unitCellBeta',
					fieldLabel : 'Unit Cell beta',
					labelStyle : labelStyle,
					value : unitCellBeta
				},
				{
					xtype : 'displayfield',
					name : 'unitCellGamma',
					fieldLabel : 'Unit Cell gamma',
					labelStyle : labelStyle,
					value : unitCellGamma
				},
				{
					xtype : 'displayfield',
					name : 'mosaicity',
					fieldLabel : 'Mosaicity',
					labelStyle : labelStyle,
					value : mosaicity
				},
				{
					xtype : 'displayfield',
					name : 'resolutionObserved',
					fieldLabel : 'Resolution observed',
					labelStyle : labelStyle,
					value : resolutionObserved
				},
				{
					xtype : 'displayfield',
					name : 'ioverSigma',
					fieldLabel : 'I/Sigl at that resolution',
					labelStyle : labelStyle,
					value : ioverSigma
				},
				{
					xtype : 'displayfield',
					name : 'numSpotsUsed',
					fieldLabel : 'Number of spots used',
					labelStyle : labelStyle,
					value : numSpotsUsed
				},
				{
					xtype : 'displayfield',
					name : 'numSpotsFound',
					fieldLabel : 'Number of spots total',
					labelStyle : labelStyle,
					value : numSpotsFound
				},
				{
					xtype : 'displayfield',
					name : 'spotDeviationTheta',
					fieldLabel : 'Spot deviation angular',
					labelStyle : labelStyle,
					value : spotDeviationTheta
				},
				{
					xtype : 'displayfield',
					name : 'spotDeviationR',
					fieldLabel : 'Spot deviation positional',
					labelStyle : labelStyle,
					value : spotDeviationR?spotDeviationR.toFixed(3):""
				},
				{
					xtype : 'displayfield',
					name : 'beamShiftX',
					fieldLabel : 'Beam shift x',
					labelStyle : labelStyle,
					value : beamShiftX?beamShiftX.toFixed(3):""
				},
				{
					xtype : 'displayfield',
					name : 'beamShiftY',
					fieldLabel : 'Beam shift y',
					labelStyle : labelStyle,
					value : beamShiftY?beamShiftY.toFixed(3):""
				},
				{
					xtype : 'displayfield',
					name : 'rankingResolution',
					fieldLabel : 'Ranking resolution',
					labelStyle : labelStyle,
					value : rankingResolution
				}
		);
	
	
	_this.panel = Ext.create('Ext.form.Panel', {
				layout : 'vbox',
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 200
				},
				defaultType : 'textfield',
				items : items

			});

	return _this.panel;
};
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
// EDNA html results tab
function CharacterisationResultPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";

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
CharacterisationResultPanel.prototype.getPanel = function(data) {
	var _this = this;
	var dataCollection = data.dataCollection;
	// html page is in a IFrame component
	var iframe=  Ext.create('Ext.ux.SimpleIFrame',{
		src:"viewResults.do?reqCode=displayEDNAPagesContent&dataCollectionId="+data.dataCollectionId
	}) ;
	iframe.setSize(800,800);
	
	_this.panel = Ext.create('Ext.Panel', {
				layout : 'fit',
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				items : [iframe]

			});

	return _this.panel;
};
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
// chart cumulative Intensity Distribution
function CumulativeIntensityDistributionChart(args) {
	var _this = this;
	this.store = null;
	this.chart = null;
	this.panel = null;
	this.width = 980;
	this.height = 580;
	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}


// builds the chart
CumulativeIntensityDistributionChart.prototype.getChart = function() {
	var _this = this;

	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				legend : {
					position : 'right'
				},
				theme : 'Category1',
				axes : [{
							type : 'Numeric',
							position : 'left',
							fields : ['acent_theor'],
							maximum : 100,
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						},{
							type : 'Numeric',
							position : 'left',
							fields : ['acent_twin'],
							maximum : 100,
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						},{
							type : 'Numeric',
							position : 'left',
							fields : ['acent_obser'],
							maximum : 100,
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						},{
							type : 'Numeric',
							position : 'left',
							fields : ['centric_theor'],
							maximum : 100,
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Numeric',
							position : 'left',
							fields : ['centric_obser'],
							maximum : 100,
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['z'],
							title : 'Z',
							maximum : 1,
							grid : true,
							label : {
								rotation : {
									degrees : 325
								}

							}
						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'z',
							yField : 'acent_theor',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, 
						{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'z',
							yField : 'acent_twin',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, 
						{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'z',
							yField : 'acent_obser',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, 
						{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'z',
							yField : 'centric_theor',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}, 
						{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'z',
							yField : 'centric_obser',
							markerConfig : {
								type : 'circle',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};


// returns and builds the panel with the chart
CumulativeIntensityDistributionChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.store = data;
	_this.getChart();
	var diffHandler = function(btn) {
		_this.chart.save({
					type : 'image/png'
				});
	};
	var saveAsImgButton = new Ext.Button({
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});
	this.panel = Ext.create('Ext.Panel', {
				// width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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
// Denzo tab
function DenzoPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";

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
DenzoPanel.prototype.getPanel = function(data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	var url = 'viewResults.do?reqCode=displayDenzoFiles&dataCollectionId=' + data.dataCollectionId;
	_this.panel = Ext.widget({
				xtype : 'form',
				collapsible : false,
				frame : true,
				layout : 'vbox',
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 200
				},
				defaultType : 'textfield',
				items : [
					{
						xtype : 'button',
						text : 'Display Denzo Files',
						href : url,
						target : '_blank',
						hrefTarget : '_blank',
						disabled: !data.DenzonContentPresent
					}, 
					{
						xtype : 'displayfield',
						name : 'denzoFileLocation',
						fieldLabel : 'Denzo files location',
						labelStyle : labelStyle,
						value : data.fullDenzoPath 
					}
				]

			});

	return _this.panel;
};
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
// Experiment tab
function ExperimentPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";

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
ExperimentPanel.prototype.getPanel = function (data) {
	var _this = this;
	var labelStyle = 'font-weight:bold;';
	var marginBottom = "0 0 20 0";
	var dataCollection = data.dataCollection;
	
	var wavelength = "";
	if (dataCollection && dataCollection.wavelength) {
		wavelength = dataCollection.wavelength + " &Aring;";
	}
		
	var energy = "";
	if (dataCollection && dataCollection.energy) {
		energy = dataCollection.energy + " keV";
	}
	var omegaStart = "";
	if (dataCollection) {
		omegaStart = dataCollection.axisStart + " &deg";
	}
	
	var totalAbsorbedDose = "";
	if (dataCollection && dataCollection.totalAbsorbedDose) {
		totalAbsorbedDose = dataCollection.totalAbsorbedDose + " Gy";
	}
	
	var items = [];
	items.push({
			xtype : 'displayfield',
			name : 'dataCollection.imageDirectory',
			fieldLabel : 'Img directory',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.imageDirectory:""
		}, 
		{
			xtype : 'displayfield',
			name : 'dataCollection.imageprefix',
			fieldLabel : 'Img prefix',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.imagePrefix:""
		},
		{
			xtype : 'displayfield',
			name : 'dataCollection.numberOfImages',
			fieldLabel : 'Nb of images',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.numberOfImages:""
		},

		{
			xtype : 'displayfield',
			name : 'dataCollection.dataCollectionNumber',
			fieldLabel : 'Run no',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.dataCollectionNumber:""
		}, 
		{
			xtype : 'displayfield',
			name : 'dataCollection.startTime',
			fieldLabel : 'Start Time',
			labelStyle : labelStyle,
			value : dataCollection ? dataCollection.startTime:""
		}, 
		{
			xtype : 'displayfield',
			name : 'dataCollection.endTime',
			fieldLabel : 'End Time',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.endTime:""
		}, 
		{
			xtype : 'displayfield',
			name : 'dataCollectionGroup.experimentType',
			fieldLabel : 'Type of experiment',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.experimentType:"", 
			margin: marginBottom
		}
	);
	
	items.push(
		{
			xtype : 'displayfield',
			name : 'dataCollectionGroup.wavelength',
			fieldLabel : 'Wavelength',
			labelStyle : labelStyle,
			value : wavelength
		}, 
		{
			xtype : 'displayfield',
			name : 'dataCollectionGroup.energy',
			fieldLabel : 'Energy',
			labelStyle : labelStyle,
			value : energy, 
			margin: marginBottom
		}
	);
	items.push(
		{
			xtype : 'displayfield',
			name : 'omegaStart',
			fieldLabel : dataCollection?dataCollection.axisStartLabel:"omegaStart",
			labelStyle : labelStyle,
			value : omegaStart
		}, 
		{
			xtype : 'displayfield',
			name : 'axisRange',
			fieldLabel : 'Oscillation range',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.axisRange+ " &deg"):""
		},
		{
			xtype : 'displayfield',
			name : 'overlap',
			fieldLabel : 'Overlap',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.overlap+ " &deg"):""
		},
		{
			xtype : 'displayfield',
			name : 'exposureTime',
			fieldLabel : 'Exposure Time',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.exposureTime+ " s"):""
		},
		{
			xtype : 'displayfield',
			name : 'totalExposureTime',
			fieldLabel : 'Total Exposure Time',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.totalExposureTime+ " s"):""
		},
		{
			xtype : 'displayfield',
			name : 'estimatedTotalAbsorbedDose',
			fieldLabel : 'Estimated Total Absorbed Dose',
			labelStyle : labelStyle,
			value : totalAbsorbedDose
		},
		{
			xtype : 'displayfield',
			name : 'numberOfPasses',
			fieldLabel : 'Number of passes',
			labelStyle : labelStyle,
			value : dataCollection?dataCollection.numberOfPasses:"", 
			margin: marginBottom
		}
	);
		
	items.push(
		{
			xtype : 'displayfield',
			name : 'detectorDistance',
			fieldLabel : 'Detector Distance',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.detectorDistance?(dataCollection.detectorDistance.toFixed(2)+ " mm"):""):""
		},
		{
			xtype : 'displayfield',
			name : 'resolutionAtEdge',
			fieldLabel : 'Resolution at edge',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.resolution?(dataCollection.resolution.toFixed(2)+" &Aring;"):""):""
		},
		{
			xtype : 'displayfield',
			name : 'resolutionAtCorner',
			fieldLabel : 'Resolution at corner',
			labelStyle : labelStyle,
			value : dataCollection?(dataCollection.resolutionAtCorner?(dataCollection.resolutionAtCorner.toFixed(2)+" &Aring;"):""):"", 
			margin: marginBottom
		}
	);
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'xbeam',
				fieldLabel : 'Xbeam',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.xbeam?(dataCollection.xbeam.toFixed(2)+ " mm"):""):""
			},
			{
				xtype : 'displayfield',
				name : 'ybeam',
				fieldLabel : 'Ybeam',
				labelStyle : labelStyle,
				value : dataCollection?(dataCollection.ybeam?(dataCollection.ybeam.toFixed(2)+ " mm"):""):"", 
				margin: marginBottom
			}
	);
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'kappa',
				fieldLabel : 'Kappa',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.kappa:""
			},

			{
				xtype : 'displayfield',
				name : 'phi',
				fieldLabel : 'Phi',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.phi:"", 
				margin: marginBottom
			}
	);
	
	items.push(
			{
				xtype : 'displayfield',
				name : 'comment',
				fieldLabel : 'Experiment comment',
				labelStyle : labelStyle,
				value : dataCollection?dataCollection.comments:""
			}
	);
	
	_this.panel =  Ext.create('Ext.form.Panel', {
				layout : 'vbox',
				collapsible : false,
				frame : true,
				bodyPadding : '5 5 0',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 200
				},
				defaultType : 'textfield',
				items : items

			});

	return _this.panel;
};
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
// panel with the grid 
function InterruptedAutoProcPanel(args) {
	var _this = this;
	this.width = "800";
	this.height = "800";


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
InterruptedAutoProcPanel.prototype.getPanel = function(data) {
	var _this = this;
	var items = [];
	// no interrupted autoProc
	var html = _this.getHtml(data);
	// if yes, create the grid
	if (_this.hasStatusData(data)) {
		for (var i=0; i<data.interruptedAutoProcEvents.length; i++){
			var autoProcStatusGrid = new AutoProcStatusGrid();
			var dataGrid = _this.getDataGrid(data.interruptedAutoProcEvents[i]);
			items.push(autoProcStatusGrid.getGrid(dataGrid));
		}
		
	}

	_this.panel = Ext.create('Ext.Panel', {
				layout : {
					type : 'vbox',
					border : 0,
					bodyPadding : 0
				},
				title : 'Interrupted Autoprocessing',
				collapsible : true,
				frame : true,
				items : items,
				html : html
			});

	return _this.panel;
};


InterruptedAutoProcPanel.prototype.getDataGrid = function(data) {
	var d = [];
	if (data) {
		for (var i = 0; i < data.length; i++) {
			d.push(data[i]);
		}
	}
	return d;
};

InterruptedAutoProcPanel.prototype.hasStatusData = function(data) {
	return (data && data.interruptedAutoProcEvents &&  data.interruptedAutoProcEvents.length > 0);
};

InterruptedAutoProcPanel.prototype.getHtml = function(data) {
	var _this = this;
	var html = "";
	if (!_this.hasStatusData(data)) {
		html = '<html><h4>No Status has been found</h4></html>';
	}
	return html;
};

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
// main entry point for the graph page (no more used)
Ext.Loader.setConfig({
			enabled : true
		});

var IspybAutoProcAttachmentGraph = {
	start : function(targetId, autoProcProgramAttachmentId) {
		this.tartgetId = targetId;
		this.autoProcProgramAttachmentId = autoProcProgramAttachmentId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showGraph(targetId, autoProcProgramAttachmentId);
	},

	showGraph : function(targetId, autoProcProgramAttachmentId) {
		var _this = this;
		var autoProcProgramAttachmentGraph = new AutoProcProgramAttachmentGraph();
		var args = [];
		args.changeSize = false;
		autoProcProgramAttachmentGraphPanel = new AutoProcProgramAttachmentGraphPanel(args);

		var items = [];
		autoProcProgramAttachmentGraph.dataRetrieved.attach(
				function(evt, data) {
					if (data.autoProcessingData && data.autoProcessingData.length > 0) {
						items.push(autoProcProgramAttachmentGraphPanel.getPanel(data));
						_this.panel = Ext.create('Ext.panel.Panel', {
									plain : true,
									frame : false,
									border : 0,
									renderTo : targetId,
									style : {
										padding : 0
									},
									items : items

								});
					}
				});
		autoProcProgramAttachmentGraph.getData(autoProcProgramAttachmentId);
	}

};

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

Ext.Loader.setConfig({
			enabled : true
		});

var autoprocessingPanel = null;
var panel = null;
// main Entry point for the autoProcessing page
var IspybAutoProc = {
	start : function(targetId) {
		this.targetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
				'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*', 'Ext.state.*',
				'Ext.form.*', 'Ext.ux.CheckColumn', 'Ext.ux.RowExpander',
				'Ext.selection.CheckboxModel', 'Ext.selection.CellModel',
				'Ext.layout.container.Column', 'Ext.tab.*',
				'Ext.ux.grid.FiltersFeature', 'Ext.selection.CellModel',
				'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
				'Ext.ux.form.MultiSelect', 'Ext.ux.form.ItemSelector',
				'Ext.chart.*', 'Ext.Window', 'Ext.fx.target.Sprite',
				'Ext.layout.container.Fit', 'Ext.window.MessageBox',
				'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showAutoprocTab(targetId);
	},

	showAutoprocTab : function(targetId) {
		var _this = this;
		// ajax calls 
		var result = new Result();
		// autoproc ajax calls
		var autoProcProgramAttachmentGraph = new AutoProcProgramAttachmentGraph();

		// main panel for autoProc
		autoprocessingPanel = new AutoprocessingPanel();
		// listen for events
		autoprocessingPanel.onAutoProcSelected.attach(function(sender, args) {
					var autoProcId = args.autoProcId;
					result.getAutoprocessingDetails(autoProcId);
				});
		autoprocessingPanel.onAutoProcGraphSelected.attach(function(sender,
				args) {
			var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
			autoProcProgramAttachmentGraph.getData(autoProcProgramAttachmentId);
		});
		
		

		var items = [];
		// listen for events
		result.autoProcRetrieved.attach(function(evt, data) {
			if (panel == null){
					items.push(autoprocessingPanel.getPanel(data));
					panel = Ext.create('Ext.panel.Panel', {
								plain : true,
								frame : false,
								width : 900,
								border : 0,
								renderTo : targetId,
								style : {
									padding : 0
								},
								items : items

							});

				if (data && data.autoProcIdSelected && data.autoProcIdSelected != null) {
					autoprocessingPanel.setSelectedAutoProcId(data.autoProcIdSelected);
					result.getAutoprocessingDetails(data.autoProcIdSelected);
				}	
			}else{
				autoprocessingPanel.setAutoProcList(data);
			}
			
		});

		result.autoProcDetailRetrieved.attach(function(evt, data) {
					autoprocessingPanel.displayAutoProc(data);
				});

		autoProcProgramAttachmentGraph.dataRetrieved.attach(
				function(evt, data) {
					if (data.autoProcessingData && data.autoProcessingData.length > 0) {
						autoprocessingPanel.displayGraphAttachment(data);
					}
				});

		// retrieve info
		result.getAutoprocessingInfo(10.0, 1.0);
	}

};

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

Ext.Loader.setConfig({
			enabled : true
		});

var autoprocessingPanel = null;
var tabs = null;
// main entry point for the result Page
var IspybResult = {
	start : function(targetId) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
				'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*', 'Ext.state.*',
				'Ext.form.*', 'Ext.ux.CheckColumn', 'Ext.ux.RowExpander',
				'Ext.selection.CheckboxModel', 'Ext.selection.CellModel',
				'Ext.layout.container.Column', 'Ext.tab.*',
				'Ext.ux.grid.FiltersFeature', 'Ext.selection.CellModel',
				'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
				'Ext.ux.form.MultiSelect', 'Ext.ux.form.ItemSelector',
				'Ext.chart.*', 'Ext.Window', 'Ext.fx.target.Sprite',
				'Ext.layout.container.Fit', 'Ext.window.MessageBox','Ext.ux.SimpleIFrame',
				'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showResult(targetId);
	},

	showResult : function(targetId) {
		var _this = this;
		// ajax calls
		var result = new Result();

		// experiment tab
		var experimentPanel = new ExperimentPanel();
		//beamline param tab
		var beamlinePanel = new BeamlinePanel();
		// EDNA tabs
		var characterisationParamPanel = new CharacterisationParamPanel();
		
		var characterisationResultPanel = new CharacterisationResultPanel();
		// autoProc tab
		var autoProcProgramAttachmentGraph = new AutoProcProgramAttachmentGraph();
		// denso tab
		var denzoPanel = new DenzoPanel();

		// main autoProc panel 
		autoprocessingPanel = new AutoprocessingPanel();
		// listen for events
		autoprocessingPanel.onAutoProcSelected.attach(function(sender, args) {
					var autoProcId = args.autoProcId;
					result.getAutoprocessingDetails(autoProcId);
				});
		autoprocessingPanel.onAutoProcGraphSelected.attach(function(sender,args) {
			var autoProcProgramAttachmentId = args.autoProcProgramAttachmentId;
			autoProcProgramAttachmentGraph.getData(autoProcProgramAttachmentId);
		});

		// listen for events
		result.resultRetrieved.attach(function(evt, data) {
			// errrors
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			// no errors => create the tabs
			if (tabs == null){
				var items = [];
				var idCharacterisationResults = 0;
				var idAutoprocessing = 2;
				items.push(
					{
						tabConfig : {
							id : 'experiment',
							title : "Experiment parameters"
						},
						items : [experimentPanel.getPanel(data)]
					}, 
					{
						tabConfig : {
							id : 'beamline',
							title : "Beamline parameters"
						},
						items : [beamlinePanel.getPanel(data)]
					}
				);
				if (data.displayOutputParam) {
					idCharacterisationResults = 3;
					idAutoprocessing = 4;
					items.push(
						{
							tabConfig : {
								id : 'characterisationParameter',
								title : "Characterisation parameters"
							},
							items : [characterisationParamPanel.getPanel(data)]
						}, 
						{
							tabConfig : {
								id : 'characterisationResults',
								title : "Characterisation results"
							},
							items : [characterisationResultPanel.getPanel(data)]
						}
					);
				}
				
				items.push(
					{
						tabConfig : {
							id : 'autoprocessing',
							title : "AutoProcessing"
						},
						items : [autoprocessingPanel.getPanel(data)]
					}
				);
				
				if (data.displayDenzoContent) {
					items.push(
						{
							tabConfig : {
								id : 'denzo',
								title : "Denzo files"
							},
							items : [denzoPanel.getPanel(data)]
						}
					);
				}
				
				var tabs = Ext.create('Ext.tab.Panel', {
							renderTo : targetId,
							activeTab : 0,
							defaults : {
								bodyPadding : 0,
								autoScroll : true
							},
							items : items
				});
				
				// by default set activ the correct tab
				if (data.isEDNACharacterisation){
					tabs.setActiveTab(idCharacterisationResults);
				}
				if (data.isAutoprocessing) {
					tabs.setActiveTab(idAutoprocessing);
				}
				
				// if a autoProc is selected => update the data
				if (data && data.autoProcIdSelected && data.autoProcIdSelected != null) {
					autoprocessingPanel.setSelectedAutoProcId(data.autoProcIdSelected);
					result.getAutoprocessingDetails(data.autoProcIdSelected);
				}
			}else{
				autoprocessingPanel.setAutoProcList(data);
			}
		});	
		result.autoProcDetailRetrieved.attach(function(evt, data) {
			autoprocessingPanel.displayAutoProc(data);
		});

		autoProcProgramAttachmentGraph.dataRetrieved.attach(
			function(evt, data) {
				if (data.errors && data.errors.length > 0) {
					var args = [];
					args.targetId = targetId;
					args.errors = data.errors;
					var errorPanel = new ErrorPanel(args);
					return;
				}
				if (data.autoProcessingData && data.autoProcessingData.length > 0) {
					autoprocessingPanel.displayGraphAttachment(data);
				}
		});

		// retrieve the results for the collect
		result.getResult(10.0, 1.0);
	}

};

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
// wilson chart
function WilsonChart(args) {
	var _this = this;
	this.store = null;
	this.chart = null;
	this.panel = null;
	this.width = 980;
	this.height = 580;
	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}


// builds the chart itself
WilsonChart.prototype.getChart = function() {
	var _this = this;

	_this.chart = Ext.create('Ext.chart.Chart', {
				xtype : 'chart',
				style : 'background:#fff',
				animate : true,
				store : _this.store,
				shadow : true,
				theme : 'Category1',
				axes : [{
							type : 'Numeric',
							position : 'left',
							fields : ['wilsonPlot'],
							title : "ln(Mn(Fsquared/n_obs)/Mn(ff)",
							grid : {
								odd : {
									opacity : 1,
									fill : '#ddd',
									stroke : '#bbb',
									'stroke-width' : 0.5
								}
							}
						}, {
							type : 'Category',
							position : 'bottom',
							fields : ['resolution'],
							title : 'Resolution',
							grid : true,
							label : {
								rotation : {
									degrees : 325
								}

							}
						}],
				series : [{
							type : 'line',
							highlight : {
								size : 7,
								radius : 7
							},
							axis : 'left',
							xField : 'resolution',
							yField : 'wilsonPlot',
							markerConfig : {
								type : 'cross',
								size : 4,
								radius : 4,
								'stroke-width' : 0
							}
						}]
			});

	return _this.chart;

};


// builds and returns the panel with the chart
WilsonChart.prototype.getPanel = function(data) {
	var _this = this;
	_this.store = data;
	_this.getChart();
	var diffHandler = function(btn) {
		_this.chart.save({
					type : 'image/png'
				});
	};
	var saveAsImgButton = new Ext.Button({
				text : '',
				tooltip : 'Save as Image',
				icon : '../images/folder_go.png',
				handler : diffHandler
			});
	var diffToolBar = new Ext.Toolbar({
				items : [saveAsImgButton]
			});
	this.panel = Ext.create('Ext.Panel', {
				// width : this.width, // panel's width
				height : this.height,// panel's height
				tbar : diffToolBar,
				layout : 'fit',
				items : _this.chart
			});
	return _this.panel;
};

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

Ext.Loader.setConfig({
			enabled : true
		});
var session;
var sessions;
var sessionGrid;
var emptyPanel = null;

// main entry point for the session page -- not used yet
var IspybSession = {
	start : function(targetId, nbSessionsToDisplay) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showSessions(targetId, nbSessionsToDisplay);
	},

	showSessions : function(targetId, nbSessionsToDisplay) {
		var _this = this;
		// ajax call to server
		session = new Session();
		sessions = [];
		// search criteria panel
		var searchSessionPanel = new SearchSessionPanel();

		var items = [];

		session.sessionRetrieved.attach(function(evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			sessions = [];
			var listSessionInformation = data.listSessionInformation;
			for (var i = 0; i < listSessionInformation.length; i++) {
				sessions.push(new Session(listSessionInformation[i]));
			}
			items = [];

			items.push(searchSessionPanel.getPanel(data.beamlines));
			emptyPanel = Ext.create('Ext.Panel', {
						id : 'emptyPanel',
						border : false,
						width : 600, // panel's width
						height : 20
					});
			items.push(emptyPanel);
			if (sessions.length == 0) {
				items.push({
							border : 0,
							padding : 0,
							html : BUI
									.getWarningHTML("No sessions have been found.")
						});
			}

			// session grid object
			sessionGrid = new SessionGrid(data);
			sessionGrid.onEditButtonClicked.attach(function(sender, args) {
						var ses = session.getSessionById(args.sessionId,sessions);
						if (ses != null) {
							var sessionWindow = new SessionWindow(data);
							sessionWindow.onSaved.attach(function(evt,
											sessionSave) {
										session.saveSession(sessionSave);
									});
							sessionWindow.draw(ses);
						}
					});

			sessionGrid.onSubmitReportButtonClicked.attach(function(sender,
							args) {
						var ses = session.getSessionById(args.sessionId, sessions);
						if (ses != null) {
							session.submitReport(ses);
						}
					});

			sessionGrid.onRemoveButtonClicked.attach(function(sender, args) {
						var ses = session.getSessionById(args.sessionId,sessions);
						if (ses != null) {
							session.removeSession(ses);
						}
					});

			items.push(sessionGrid.getGrid(sessions));

			// creation of the main panel
			this.panel = Ext.create('Ext.panel.Panel', {
						plain : true,
						frame : false,
						border : 0,
						renderTo : targetId,
						style : {
							padding : 0
						},
						items : items
					});
		});

		session.onSaved.attach(function(sender, newSessions) {
					_this.refresh(newSessions);
				});

		session.getSessionByProposal(nbSessionsToDisplay);
	},

	refresh : function(newSessions) {
		var _this = this;
		sessions = [];
		for (var i = 0; i < newSessions.length; i++) {
			sessions.push(new Session(newSessions[i]));
		}
		sessionGrid.refresh(sessions);
	}
};

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
 * Brenchereau, A. De Maria Antolinos
 ******************************************************************************/

// Search criteria session panel
function SearchSessionPanel(args) {
	var _this = this;
	this.width = "340";
	this.height = "500";

	// click on search
	this.onSearchSession = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
}


// builds and returns the panel
SearchSessionPanel.prototype.getPanel = function(beamlines) {
	var _this = this;
	var d = [];
	for (i = 0; i < beamlines.length; i++) {
		var s = {
			beamlineName : beamlines[i]
		};
		d.push(s);
	}

	// store beamline 
	_this.beamlines = Ext.create('Ext.data.Store', {
				fields : ['beamlineName'],
				data : d
			});

	// form panel
	_this.panel = Ext.widget({
		xtype : 'form',
		layout : 'form',
		collapsible : true,
		id : 'searchSessionForm',
		frame : true,
		title : 'Search Session',
		bodyPadding : '5 5 0',
		width : 350,
		fieldDefaults : {
			msgTarget : 'side',
			labelWidth : 90
		},
		defaultType : 'textfield',
		items : [{
					xtype : 'combobox',
					fieldLabel : 'Beamline',
					id : 'cbBeamline',
					name : 'beamline',
					store : _this.beamlines,
					valueField : 'beamlineName',
					displayField : 'beamlineName',
					typeAhead : true,
					queryMode : 'local',
					emptyText : 'Select a beamline...'
				}, {
					xtype : 'datefield',
					name : 'startDate',
					id : 'fieldStartDate',
					fieldLabel : 'Start Date (DD/MM/YYYY)'
				}, {
					xtype : 'datefield',
					name : 'endDate',
					id : 'fieldEndDate',
					fieldLabel : 'End Date (DD/MM/YYYY)'
				}],

		buttons : [{
					text : 'Search',
					handler : function() {
						if (this.up('form').getForm().isValid()) {
							this.up('form').getForm().submit({
										url : 'viewSession.do?reqCode=getSessions',
										waitMsg : 'Searching sessions...',
										success : function(fp, o) {
											_this.onSearchSession.notify({
														'sessions' : fp
													});
										}
									});
							this.up('form').getForm().reset();
						}
					}
				}, {
					text : 'Reset',
					handler : function() {
						this.up('form').getForm().reset();
					}
				}]
	});

	return _this.panel;
};
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


// session form class, used in the session page or in hte header of the dataCollection page
function SessionForm(args) {
	this.actions = [];

	// industrial user
	this.isFx = false;
	this.isIx = false;

	if (args != null) {
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.isIx != null) {
			this.isIx = args.isIx;
		}
	}

	this.session = null;
	this.emailButton = null;

	/** STRUCTURES * */

}

// returns the session object from the values filled in the form
SessionForm.prototype.getSession = function() {
	var _this = this;
	this.session.beamlineOperator = Ext.getCmp(this.panel.getItemId())
			.getValues().beamlineOperator;
	this.session.comments = Ext.getCmp(this.panel.getItemId()).getValues().comments;
	if (_this.isFx) {
		this.session.sessionTitle = Ext.getCmp(this.panel.getItemId())
				.getValues().sessionTitle;
		this.session.structureDeterminations = Ext.getCmp(this.panel
				.getItemId()).getValues().structureDeterminations;
		if (this.session.structureDeterminations == "") {
			this.session.structureDeterminations = 0;
		}
	}
	if (_this.isFx || _this.isIx) {
		this.session.dewarTransport = Ext.getCmp(this.panel.getItemId())
				.getValues().dewarTransport;
		this.session.databackupFrance = Ext.getCmp(this.panel.getItemId())
				.getValues().databackupFrance;
		this.session.databackupEurope = Ext.getCmp(this.panel.getItemId())
				.getValues().databackupEurope;
		if (this.session.dewarTransport == "") {
			this.session.dewarTransport = 0;
		}
		if (this.session.databackupFrance == "") {
			this.session.databackupFrance = 0;
		}
		if (this.session.databackupEurope == "") {
			this.session.databackupEurope = 0;
		}
	}
	return this.session;
};


// refresh function, called after the change of the beamline operator
SessionForm.prototype.refresh = function(session) {
	this.session = session;
	this.emailButton.setTooltip('mail to: ' + this.session.beamLineOperatorEmail);
	this.emailButton.setHandler(function() {
				window.location = 'mailto:' + session.beamLineOperatorEmail;
			});
};


// builds and returns the session form
SessionForm.prototype.getPanel = function(session) {
	this.session = session;
	var _this = this;
	var sessionWidth = 380;
	var sessionHeight = 200;
	var labelWidth = 90;
	var sessionItems = [];
	// local contact
	sessionItems.push({
				fieldLabel : 'Local Contact',
				name : 'beamlineOperator',
				//anchor : '95%',
				tooltip : "Local contact beamline Operator",
				value : this.session.beamlineOperator
			});
	// email local contact
	this.emailButton = new Ext.Button({
				text : '',
				handler : function() {
					window.location = 'mailto:' + session.beamLineOperatorEmail;
				},
				tooltip : 'mail to: ' + this.session.beamLineOperatorEmail,
				icon : '../images/mail_generic.png'
			});
	if (this.session.beamLineOperatorEmail && this.session.beamLineOperatorEmail != "")
		sessionItems.push(this.emailButton);
	//comments
	sessionItems.push({
				xtype : 'textareafield',
				name : 'comments',
				width: 500,
				grow: true,
				fieldLabel : 'Comments',
				//anchor : '95%',
				tooltip : "Session comments",
				value : this.session.comments
			});
	// display only for FX accounts
	if (_this.isFx) {
		labelWidth = 160;
		sessionHeight = 330;
		sessionItems.push({
					fieldLabel : 'Session Title',
					name : 'sessionTitle',
					//anchor : '95%',
					tooltip : "Session title",
					value : this.session.sessionTitle
				}, {
					fieldLabel : 'Structure Determinations',
					name : 'structureDeterminations',
					//anchor : '95%',
					tooltip : "Structure Determinations",
					value : this.session.structureDeterminations,
					xtype : 'numberfield',
					hideTrigger : true
				}, {
					fieldLabel : 'Dewar Transport',
					name : 'dewarTransport',
					//anchor : '95%',
					tooltip : "Dewar Transport",
					value : this.session.dewarTransport,
					xtype : 'numberfield',
					hideTrigger : true
				}, {
					fieldLabel : 'Data backup &<br/>Express delivery France',
					name : 'databackupFrance',
					//anchor : '95%',
					tooltip : "Data backup & Express delivery France",
					value : this.session.databackupFrance,
					xtype : 'numberfield',
					hideTrigger : true
				}, {
					fieldLabel : 'Data backup &<br/>Express delivery Europe',
					name : 'databackupEurope',
					//anchor : '95%',
					tooltip : "Data backup & Express delivery Europe",
					value : this.session.databackupEurope,
					xtype : 'numberfield',
					hideTrigger : true
				});
	}
	// display only for IX accounts
	else if (_this.isIx) {
		labelWidth = 160;
		sessionHeight = 270;
		sessionItems.push({
					fieldLabel : 'Dewar Transport',
					name : 'dewarTransport',
					//anchor : '95%',
					tooltip : "Dewar Transport",
					value : this.session.dewarTransport,
					xtype : 'numberfield',
					hideTrigger : true
				}, {
					fieldLabel : 'Data backup &<br/>Express delivery France',
					name : 'databackupFrance',
					//anchor : '95%',
					tooltip : "Data backup & Express delivery France",
					value : this.session.databackupFrance,
					xtype : 'numberfield',
					hideTrigger : true
				}, {
					fieldLabel : 'Data backup &<br/>Express delivery Europe',
					name : 'databackupEurope',
					//anchor : '95%',
					tooltip : "Data backup & Express delivery Europe",
					value : this.session.databackupEurope,
					xtype : 'numberfield',
					hideTrigger : true
				});
	}
	if (this.panel == null) {
		this.panel = Ext.createWidget('form', {
					bodyPadding : 5,
					frame : true,
					border : 0,
					layout:'fit',
					fieldDefaults : {
						labelAlign : 'left',
						labelWidth : labelWidth
						//anchor : '100%'
					},
					items : [{
								xtype : 'container',
								border : false,
								defaultType : 'textfield',
								items : sessionItems
							}]

				});
	}
	return this.panel;
};

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
// Session grid
function SessionGrid(args) {
	/** Events * */
	// edit session
	this.onEditButtonClicked = new Event(this);
	// submit report
	this.onSubmitReportButtonClicked = new Event(this);
	// remove session
	this.onRemoveButtonClicked = new Event(this);

	// nb session per page
	this.pageSize = 20;

	// user type
	var isManager = false;
	var isLocalContact = false;
	var isIndustrial = false;
	var allowedToSubmitReport = false;
	var proposalIsBM14 = false;
	var isFx = false;
	var contextPath = "";
	this.title = "Sessions";
	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.isManager != null) {
			this.isManager = args.isManager;
		}
		if (args.isLocalContact != null) {
			this.isLocalContact = args.isLocalContact;
		}
		if (args.isIndustrial != null) {
			this.isIndustrial = args.isIndustrial;
		}
		if (args.allowedToSubmitReport != null) {
			this.allowedToSubmitReport = args.allowedToSubmitReport;
		}
		if (args.proposalIsBM14 != null) {
			this.proposalIsBM14 = args.proposalIsBM14;
		}
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.contextPath != null) {
			this.contextPath = args.contextPath;
		}
		if (args.title != null) {
			this.title = args.title;
		}
	}

	// session information datamodel
	Ext.define('SessionModel', {
				extend : 'Ext.data.Model',
				fields : [{
							name : 'sessionId',
							mapping : 'sessionId'
						}, {
							name : 'expSessionPk',
							mapping : 'expSessionPk'
						}, {
							name : 'startDate',
							mapping : 'startDate',
							type : 'date',
							dateFormat : 'd-m-Y'
						}, {
							name : 'endDate',
							mapping : 'endDate',
							type : 'date',
							dateFormat : 'd-m-Y'
						}, {
							name : 'beamlineName',
							mapping : 'beamlineName'
						}, {
							name : 'beamlineOperator',
							mapping : 'beamlineOperator'
						}, {
							name : 'beamLineOperatorEmail',
							mapping : 'beamLineOperatorEmail'
						}, {
							name : 'proposalCodeNumber',
							mapping : 'proposalCodeNumber'
						}, {
							name : 'hasDataCollectionGroup',
							mapping : 'hasDataCollectionGroup'
						}, {
							name : 'nbShifts',
							mapping : 'nbShifts'
						}, {
							name : 'sessionTitle',
							mapping : 'sessionTitle'
						}, {
							name : 'comments',
							mapping : 'comments'
						}],
				idProperty : 'sessionId'
			});
}

SessionGrid.prototype.getFilterTypes = function() {
	return [];
};

// format data
SessionGrid.prototype._prepareData = function() {
	var data = [];
	for (var i = 0; i < this.features.length; i++) {
		var dataSession = {};
		dataSession = this.features[i].json;
		data.push(dataSession);
	}

	return data;
};


// sort session by date DESC
SessionGrid.prototype._sort = function(store) {
	store.sort('startDate', 'DESC');
};


// returns the grid
SessionGrid.prototype.getGrid = function(sessions) {
	this.features = sessions;
	return this.renderGrid(sessions);
};


// refresh data
SessionGrid.prototype.refresh = function(sessions) {
	this.features = sessions;
	this.store.loadData(this._prepareData(), false);
};


// no actions
SessionGrid.prototype.getActions = function() {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};


// builds and returns the grid
SessionGrid.prototype.renderGrid = function() {
	var _this = this;

	/** Prepare data * */
	var data = [];
	// columns
	var columns = this._getColumns();
	// format data
	var myData = this._prepareData();
	// data
	this.store = Ext.create('Ext.data.Store', {
				model : 'SessionModel',
				autoload : false,
				pageSize : this.pageSize,
				proxy : {
					type : 'pagingmemory',
					data : myData,
					reader : {
						type : 'array'
					}
				}
			});

	this._sort(this.store);
	this.store.totalCount = this.features.length;
	this.store.loadData(myData, false);

	var pluginExpanded = true;
	this.grid = Ext.create('Ext.grid.Panel', {
				style : {
					padding : 0
				},
				width : "100%",
				model : 'SessionModel',
				height : this.height,
				store : this.store,
				columns : columns,
				title : this.title,
				viewConfig : {
					stripeRows : true,
					enableTextSelection : true,
					listeners : {
						itemdblclick : function(dataview, record, item, e) {
							_this.editSession(record.data.sessionId);
						}
					}
				},
				selModel : {
					mode : 'SINGLE'
				},
				// paging bar on the bottom
				bbar : Ext.create('Ext.PagingToolbar', {
							store : this.store,
							pageSize : this.pageSize,
							displayInfo : true,
							displayMsg : 'Displaying sessions {0} - {1} of {2}',
							emptyMsg : "No sessions to display",
							listeners : {
								afterrender : function() {
									var a = Ext
											.query("button[data-qtip=Refresh]");
									for (var x = 0; x < a.length; x++) {
										a[x].style.display = "none";
									}
								}
							}
						})
			});
	this.store.load();
	return this.grid;
};


// builds the columns for the session grid
SessionGrid.prototype._getColumns = function() {
	var _this = this;
	// render start Date (link to group)
	function renderStartDate(value, p, record) {
		return Ext.String
				.format(
						'<a href='+
								contextPath +
								'/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollectionGroup.do?reqCode=display&sessionId={1}>{0}</a>',
						Ext.Date.format(value, 'd-m-Y'), record.getId());
	}
	
	// render Aform column (link to SMIS)
	function renderAForm(value, p, record) {
		if (record.get('expSessionPk'))
			return Ext.String
					.format(
							'<a href="https://wwws.esrf.fr/misapps/SMISWebClient/protected/aform/manageAForm.do?action=view&currentTab=howtoTab&expSessionVO.pk={1}" target="_blank" >A-Form</a>',
							value, record.get('expSessionPk'));
		else
			return "";
	}

	// render beamline operator  (link with email)
	function renderBeamlineOperator(value, p, record) {
		if (record.get('beamLineOperatorEmail'))
			return Ext.String.format('<a href="mailto:{1}" " >{0}</a>', record
							.get('beamlineOperator'), record
							.get('beamLineOperatorEmail'));
		else
			return record.get('beamlineOperator');
	}

	// render a column wrap
	function columnWrap(value) {
		if (value)
			return '<div style="white-space:normal !important;">' + value + '</div>';
		else
			return "";
	}

	// hide content if no group
	function actionItemRenderer(value, meta, record, rowIx, ColIx, store) {
		if (!record.get('hasDataCollectionGroup')) {
			return 'x-hide-display';
		}
	}

	// builds the list of columns
	var columns = [{
				id : 'sessionId',
				text : 'Start Date',
				dataIndex : 'startDate',
				renderer : renderStartDate,
				flex : 0.075
			}, {
				text : 'End Date',
				dataIndex : 'endDate',
				renderer : Ext.util.Format.dateRenderer('d-m-Y'),
				flex : 0.075
			}, {
				text : 'Beamline',
				dataIndex : 'beamlineName',
				type : 'string',
				flex : 0.05
			}, {
				text : 'Local contact',
				dataIndex : 'beamlineOperator',
				renderer : renderBeamlineOperator,
				type : 'string',
				flex : 0.075
			}];
	// proposal info for manager and local contact
	if (_this.isManager || _this.isLocalContact) {
		columns.push({
					text : 'Proposal',
					dataIndex : 'proposalCodeNumber',
					type : 'string',
					flex : 0.075
				});
	}
	columns.push({
				text : '# Shifts',
				dataIndex : 'nbShifts',
				type : 'string',
				flex : 0.05
			});
	// indus
	if (_this.isFx) {
		columns.push({
					text : 'Title',
					dataIndex : 'sessionTitle',
					type : 'string',
					flex : 0.1
				});
	}
	columns.push({
				text : 'Comments',
				dataIndex : 'comments',
				type : 'string',
				flex : 0.3,
				renderer : columnWrap
			});
	// edit
	if (!_this.isIndustrial) {
		columns.push({
			xtype : 'actioncolumn',
			width : 50,
			header : 'Edit',
			sortable : false,
			items : [{
				icon : '../images/Edit_16x16_01.png',
				tooltip : 'Edit Session',
				handler : function(grid, rowIndex, colIndex) {
					_this
							.editSession(_this.store.getAt(rowIndex).data.sessionId);
				}
			}]
		});
	}
	// view groups, collects
	columns.push({
		xtype : 'actioncolumn',
		id : 'viewGroup',
		width : 70,
		header : 'View<br/>Collections<br/>Groups',
		sortable : false,
		items : [{
			icon : '../images/magnif_16.png',
			tooltip : 'View Collections Groups',
			getClass : actionItemRenderer,
			handler : function(grid, rowIndex, colIndex) {
				_this
						.viewCollectionsGroups(_this.store.getAt(rowIndex).data.sessionId);
			}
		}]

	}, {
		xtype : 'actioncolumn',
		id : 'viewCollect',
		width : 70,
		header : 'View<br/>Collections',
		sortable : false,
		items : [{
			icon : '../images/magnif_16.png',
			tooltip : 'View Collections',
			getClass : actionItemRenderer,
			handler : function(grid, rowIndex, colIndex) {
				_this.viewCollections(_this.store.getAt(rowIndex).data.sessionId);
			}
		}]

	});
	//submit report (BM14)
	if (_this.proposalIsBM14) {
		columns.push({
			xtype : 'actioncolumn',
			width : 50,
			header : 'Export',
			sortable : false,
			items : [{
				icon : '../images/export.gif',
				tooltip : 'Export information',
				handler : function(grid, rowIndex, colIndex) {
					_this.exportSession(_this.store.getAt(rowIndex).data.sessionId);
				}
			}]
		});
	}
	if (_this.allowedToSubmitReport) {
		columns.push({
			xtype : 'actioncolumn',
			width : 50,
			header : 'Submit<br/>Report',
			sortable : false,
			items : [{
				icon : '../images/submitReport_pdf.gif',
				tooltip : 'SubmitReport',
				handler : function(grid, rowIndex, colIndex) {
					_this.submitReport(_this.store.getAt(rowIndex).data.sessionId);
				}
			}]
		});
	}
	// delete session
	if (_this.isManager) {
		columns.push({
			xtype : 'actioncolumn',
			width : 50,
			header : 'Delete',
			sortable : false,
			items : [{
				icon : '../images/cancel.png',
				tooltip : 'Remove Session',
				handler : function(grid, rowIndex, colIndex) {
					_this.removeSession(_this.store.getAt(rowIndex).data.sessionId);
				}
			}]
		});
	}
	// Aform
	columns.push({
				text : 'SMIS<br/>A-Form',
				type : 'string',
				renderer : renderAForm,
				flex : 0.05,
				tdCls : 'wrap'
			});

	return columns;

};

// click on edit Session: throws a event edit
SessionGrid.prototype.editSession = function(sessionId) {
	this.onEditButtonClicked.notify({
				'sessionId' : sessionId
			});
};


// redirection to view groups page
SessionGrid.prototype.viewCollectionsGroups = function(sessionId) {
	document.location.href = contextPath +
			"/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollectionGroup.do?reqCode=display&sessionId=" +
			sessionId;
};

// redirection to view collects page
SessionGrid.prototype.viewCollections = function(sessionId) {
	document.location.href = contextPath +
			"/menuSelected.do?leftMenuId=-1&topMenuId=16&targetUrl=/user/viewDataCollection.do?reqCode=displayForSession&sessionId=" +
			sessionId;
};

// submit the report button: throws an event Submit
SessionGrid.prototype.submitReport = function(sessionId) {
	this.onSubmitReportButtonClicked.notify({
				'sessionId' : sessionId
			});
};

// remove session: throws an event Remove session
SessionGrid.prototype.removeSession = function(sessionId) {
	if (confirm("Do you want to remove this session?")) {
		this.onRemoveButtonClicked.notify({
					'sessionId' : sessionId
				});
	}
};

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

SessionWindow.prototype.getButtons = GenericWindow.prototype.getButtons;
SessionWindow.prototype._render = GenericWindow.prototype._render;
SessionWindow.prototype._postRender = GenericWindow.prototype._postRender;

// window which contains the sessionForm, opened when the user clicks on Edit Session
// Parent is a genericWindow
function SessionWindow(args) {
	this.title = "Edit session";
	this.width = 450;
	this.height = 300;

	this.isFx = false;
	this.isIx = false;

	if (args != null) {
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.isIx != null) {
			this.isIx = args.isIx;
		}
	}
	if (this.isFx) {
		this.height = 500;
	}
	if (this.isIx) {
		this.height = 450;
	}
	// form panel inside
	this.form = new SessionForm(args);
	// generic window parent
	GenericWindow.prototype.constructor.call(this, {
				form : this.form,
				width : this.width,
				height : this.height
			});
}

// save button click: throws a Save event and close the window
SessionWindow.prototype.save = function() {
	this.onSaved.notify(this.form.getSession());
	this.panel.close();
};

// cancel button: close the panel
SessionWindow.prototype.cancel = function() {
	this.panel.close();
};

// display of the window: initialize the title and the form with the given session
SessionWindow.prototype.draw = function(session) {
	this.title = "Edit session";
	this._render(session);
};
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
/*global document */


// puck grid, used in the puck creation or fill Shipment interface 
function CreatePuckGrid(args) {

	// Events
	// save puck
	this.onSaveButtonClicked = new Event(this);
	// edit puck
	this.onEditPuckButtonClicked = new Event(this);
	// remove puck
	this.onRemovePuckButtonClicked = new Event(this);
	// copy puck
	this.onCopyPuckButtonClicked = new Event(this);
	// copy sample
	this.onCopySampleButtonClicked = new Event(this);

	this.title = "Create a new Puck";
	this.saveButton = "Save";
	//protein list  for the combo box
	this.proteinList = [];
	// sapce Group list to be displayed in the combo box
	this.spaceGroupList = [];
	// experiment type list to be displayed in the combo box 
	this.experimentTypeList = [];
	// crystal class list to be displayed in the combo box
	this.crystalValuesList = [];
	// represents the data linked to the puck(list of samples, puckId, etc)
	this.puckData =  null;
	// sample to be pasted
	this.sampleToCopy = null;
	
	// if false: puck creation, if true: fill Shipment view
	this.fillShipmentMode  = false;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}
	}

	this.changeSampleNameAuto = false;

	// sample information datamodel
	Ext.define('SampleModel', {
			extend : 'Ext.data.Model',
			fields : [{
					name : 'sampleId',
					mapping : 'sampleId'
				}, {
					name : 'position',
					mapping : 'position'
				}, {
					name : 'sampleName',
					mapping : 'sampleName'
				}, {
					name : 'proteinAcronym',
					mapping : 'proteinAcronym'
				}, {
					name : 'spaceGroup',
					mapping : 'spaceGroup'
				}, {
					name : 'preObservedResolution',
					mapping : 'preObservedResolution'
				}, {
					name : 'neededResolution',
					mapping : 'neededResolution'
				}, {
					name : 'oscillationRange',
					mapping : 'oscillationRange'
				}, {
					name : 'experimentType',
					mapping : 'experimentType'
				}, {
					name : 'unitCellA',
					mapping : 'unitCellA'
				}, {
					name : 'unitCellB',
					mapping : 'unitCellB'
				}, {
					name : 'unitCellC',
					mapping : 'unitCellC'
				}, {
					name : 'unitCellC',
					mapping : 'unitCellC'
				}, {
					name : 'unitCellAlpha',
					mapping : 'unitCellAlpha'
				}, {
					name : 'unitCellBeta',
					mapping : 'unitCellBeta'
				}, {
					name : 'unitCellGamma',
					mapping : 'unitCellGamma'
				}, {
					name : 'smiles',
					mapping : 'smiles'
				}, {
					name : 'comments',
					mapping : 'comments'
				}, {
					name : 'pinBarcode',
					mapping : 'pinBarcode'
				}],
			idProperty : 'sampleId'
		});
}

// no filters
CreatePuckGrid.prototype.getFilterTypes = function () {
	return [];
};

// default sort by position in the puck ASC
CreatePuckGrid.prototype._sort = function (store) {
	store.sort('position', 'ASC');
};

// builds the protein list to be displayed in the combo box: list of acronym
CreatePuckGrid.prototype._setProteinList = function (data) {
	this.proteinList = [];
	if (data && data.proteinList) {
		for (var i = 0; i < data.proteinList.length; i++) {
			var value = [];
			value.proteinAcronym = data.proteinList[i];
			this.proteinList.push(value);
		}
	}
};


// builds the space group list to be displayed in the combo box
CreatePuckGrid.prototype._setSpaceGroupList = function (data) {
	this.spaceGrouplist = [];
	if (data && data.spaceGroupList) {
		for (var i = 0; i < data.spaceGroupList.length; i++) {
			var value = [];
			value.spaceGroup = data.spaceGroupList[i];
			this.spaceGroupList.push(value);
		}
	}
};

// builds the experiment type list to be displayed in the combo box
CreatePuckGrid.prototype._setExperimentTypeList = function (data) {
	this.experimentTypeList = [];
	if (data && data.experimentTypeList) {
		for (var i = 0; i < data.experimentTypeList.length; i++) {
			var value = [];
			value.experimentType = data.experimentTypeList[i];
			this.experimentTypeList.push(value);
		}
	}
};


// builds the crystal class list to be displayed in the combo box
CreatePuckGrid.prototype._setCrystalValuesList = function (data) {
	this.crystalValuesList = [];
	if (data && data.crystalValuesList) {
		for (var i = 0; i < data.crystalValuesList.length; i++) {
			var value = data.crystalValuesList[i];
			this.crystalValuesList.push(value);
		}
	}
};


// builds and returns the grid: set the lists, initializes the grid and build it
CreatePuckGrid.prototype.getGrid = function (data) {
	this.puckData =  data;
	this._setProteinList(data);
	this._setSpaceGroupList(data);
	this._setExperimentTypeList(data);
	this._setCrystalValuesList(data);

	this.initGrid();
	this.updateListSamples(data);
	return this.renderGrid();
};

// initGrid: set the position value and the default values
CreatePuckGrid.prototype.initGrid = function () {
	this.features = [];
	for (var i = 0; i < 10; i++) {
		this.features.push({
			sampleId : -1,
			position : i + 1,
			// sampleName: "sample"+this._formatPostionNumber(i+1),
			experimentType : "Default"
		});
	}
};

// refresh the grid with new data
CreatePuckGrid.prototype.updateListSamples = function (data) {
	for (var i = 0; i < data.listSamples.length; i++) {
		var pos = data.listSamples[i].position;
		this.features[pos - 1] = data.listSamples[i];
		this.features[pos - 1].position = Number(data.listSamples[i].position);
	}
	// change the button title
	if (data.listSamples.length > 0) {
		this.title = "Update Puck";
		//this.saveButton = "Update";
	} else {
		this.title = "Create Puck";
		//this.saveButton = "Save";
	}
	this.fillShipmentMode  = false;
	if (data && data.fillShipmentMode && data.fillShipmentMode == true) {
		this.title = "";
		this.fillShipmentMode  = true;
		//this.saveButton = "";
	}
	
};

// format the position number on 2 digit, in order to have a correct sort
CreatePuckGrid.prototype._formatPostionNumber = function (i) {
	if (i && i < 10) {
		return "0" + i;
	}
	return i;
};

// refresh the grid: refresh the samples and reload the data
CreatePuckGrid.prototype.refresh = function (data) {
	this.initGrid();
	this.puckData = data;
	this.updateListSamples(data);

	this.store.loadData(this.features, false);
	this._sort(this.store);
};


// no actions
CreatePuckGrid.prototype.getActions = function () {
	var _this = this;

	/** Actions buttons * */
	this.actions = [];

	return this.actions;
};


// builds and returns the grid
CreatePuckGrid.prototype.renderGrid = function () {
	var _this = this;

	/** Prepare data * */
	var columns = this._getColumns();
	// data to be displayed in the grid
	this.store = Ext.create('Ext.data.Store', {
		model : 'SampleModel',
		autoload : false
	});

	this._sort(this.store);
	this.store.totalCount = this.features.length;
	this.store.loadData(this.features, false);

	// edition if a cell, possible on a click
	// some events are thrown after edition: 
	// change sample name: change sample name automatically
	// change protein: set the  values linked to the crystal form
	// change experiment type: disable or not some values
	
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
		clicksToEdit : 1,
		listeners : {
			afteredit : function (e, eOpts) {
				var rec = eOpts.record;
				var field = eOpts.field;
				var val = eOpts.value;
				var idRow = eOpts.rowIdx;
				if (field == 'sampleName') {
					_this.changeSampleName(val, idRow);
				} else if (field == 'proteinAcronym') {
					_this.changeProteinAcronym(val, idRow);
				} else if (field == 'experimentType') {
					_this.changeExperimentType(val, idRow);
				}
			}
		}
	});

	// top bar
	var tbar = [];
	// edit puck label, copy puck, cut puck
	if (_this.fillShipmentMode == true) {
		tbar.push({
			tooltip : {
				text : 'Edit the puck label',
				title : 'Edit puck'
			},
			icon : '../images/Edit_16x16_01.png',
			handler : function () {
				_this.editPuck();
			}
					
		}, {xtype: 'tbseparator'} 
		);
		tbar.push( 
				{
				tooltip : {
					text : 'Copy the puck with the samples. Then you can paste it in any dewar; it will create a new puck with the same samples.',
					title : 'Copy puck'
				},
				id: 'button_copy_' + _this.puckData.puckInfo.containerId,
				icon : '../images/editcopy.png', 
				handler : function () {
					_this.copyPuck();
				}
						
			}, 
				{
				tooltip : {
					text : 'Copy the puck and remove it. Then you can paste it in any dewar.',
					title : 'Cut puck'
				},
				id: 'button_cut_' + _this.puckData.puckInfo.containerId,
				icon : '../images/editcut.png'
						
			}, {xtype: 'tbseparator'});
	}
	// save button, reset, change sample name automatically
	tbar.push({
			text : this.saveButton,
			tooltip : {
				text : 'Save all samples contained in this puck',
				title : 'Save samples'
			},
			icon : '../images/save.gif',
			handler : function () {
				_this.onSaveButtonClicked.notify({
					'listSamples': _this.getListSamples(), 
					'puckInfo': _this.puckData.puckInfo
				});
			}
		}, 
		{xtype: 'tbseparator'}, {
			text : 'Reset',
			tooltip : {
				text : 'Reset the samples table: all fields are set to their default value.',
				title : 'Reset the samples'
			},
			icon : '../images/recur.png',
			handler : function () {
				_this.reset();
			}
		}, {
			xtype : 'checkboxfield',
			name : 'cbchangeName',
			boxLabel : 'Change sample name automatically',
			checked : false,
			inputValue : 'all',
			handler : function (field, value) {
				var checkValue = field.getValue();
				_this.changeSampleNameAuto = checkValue;
			}
		});
	// remove puck
	if (_this.fillShipmentMode == true) {
		tbar.push({xtype: 'tbseparator'}, 
				{
				tooltip : {
					text : 'Remove this puck together with samples inside',
					title : 'Delete puck'
				},
				icon : '../images/cancel.png', 
				handler : function () {
					_this.deletePuck();
				}
						
			});
	}

	// build grid
	this.grid = Ext.create('Ext.grid.Panel', {
			style : {
				padding : 0
			},
			width : "100%",
			model : 'SampleModel',
			height : "100%",
			store : this.store,
			columns : columns,
			title : this.title,
			viewConfig : {
				stripeRows : true,
				enableTextSelection : true
			},
			selModel : {
				mode : 'SINGLE'
			},
			plugins : [cellEditing],
			tbar : tbar
		});
	// depending of the value of the experiment type, the oscillation range column could be disabled
	this.grid.on('beforeedit', function (editor, a, eOpts) {
		var idCol = a.colIdx;
		var isOscRange = this.columns[idCol].dataIndex == 'oscillationRange';
		
		var idRow = a.record.index;
		var experimentType = _this.getExperimentTypeValue(idRow);
		if (isOscRange && !_this.canEditOscillationRange(experimentType)) {
			return false;
		}
		return true;
    });


	return this.grid;
};

// get the experiment type value at the given id row
CreatePuckGrid.prototype.getExperimentTypeValue = function (idRow) {
	return this.grid.store.getAt(idRow).get('experimentType');
};


// edit puck clikc: throws an event 
CreatePuckGrid.prototype.editPuck = function () {
	var _this = this;
	_this.onEditPuckButtonClicked.notify({'containerId' : _this.puckData.puckInfo.containerId, 
	'puckName' : _this.puckData.puckInfo.code});
};

// changes sample automatically
CreatePuckGrid.prototype.changeSampleName = function (value, idRow) {
	if (this.changeSampleNameAuto && value) {
		var l = value.length;
		var c = 0;
		// parse the sample name
		if (l >= 1) {
			var e = value.substring(l - 1);
			if (!isNaN(parseFloat(e)) && isFinite(e)) {
				c = 1;
			}
		}
		if (l >= 2) {
			var e = value.substring(l - 2);
			if (!isNaN(parseFloat(e)) && isFinite(e)) {
				c = 2;
			}
		}
		var newValue = value.substring(0, l - c);
		var proteinValue = this.grid.store.getAt(idRow).data.proteinAcronym;
		var idToIncrement = 1;
		// set the new values automatically for the same protein acronym
		for (var i = 0; i < 10; i++) {
			var currentProtein = this.grid.store.getAt(i).data.proteinAcronym;
			if (proteinValue == currentProtein) {
				this.grid.store.getAt(i).set('sampleName',
					newValue + this._formatPostionNumber(idToIncrement));
				idToIncrement = idToIncrement + 1;
			}
		}
	}
};


// builds the columns for the puck grid
CreatePuckGrid.prototype._getColumns = function () {
	var _this = this;

	// render: wrap column value
	function columnWrap(value) {
		if (value){
			return '<div style="white-space:normal !important;word-wrap: break-word;">' + value + '</div>';
		}else{
			return "";
		}
	}
	
	// render sample name: light red color background
	function renderSample(value, metadata) {
		metadata.tdAttr = 'style="background-color: #FFEBEB"';
		if (value) {
			return '<div style="white-space:normal !important;word-wrap: break-word;">' + value + '</div>';
		} else {
			return "";
		}
	}

	// render protein:  light red color background
	function renderProtein(value, metadata) {
		metadata.tdAttr = 'style="background-color: #FFEBEB"';
		return value;
	}
	
	// oscillation: disabled depending of the experiment type
	function renderOscillationRange(value, metadata, record) {
		var experimentType = _this.getExperimentTypeValue(record.index);
		if (!_this.canEditOscillationRange(experimentType)) {
			metadata.tdAttr = 'style="background-color: #EBE8E8"';
		}
		
		return value;
	}
	
	// create reusable renderer
	Ext.util.Format.comboRenderer = function (combo) {
		return function (value) {
			var record = combo.findRecord(combo.valueField, value);
			return record ? record.get(combo.displayField): combo.valueNotFoundText;
		};
	};

	// format number on 3 digit
	function renderNumberFormat3(value, p, record) {
		if (value == null) {
			return "";
		}
		return Number(value).toFixed(3);
	}

	// format number on 1 digit
	function renderNumberFormat1(value, p, record) {
		if (value == null) {
			return "";
		}
		return Number(value).toFixed(1);
	}

	// render position (without formating)
	function renderPosition(value, p, record) {
		if (value == null){
			return "";
		}
		return Number(value);
	}

	// create the combo instance
	// for protein, space group, experiment type
	var proteinClassStore = Ext.create('Ext.data.Store', {
		id : 0,
		fields : ['proteinAcronym'],
		data : _this.proteinList
	});

	var spaceGroupClassStore = Ext.create('Ext.data.Store', {
		id : 0,
		fields : ['spaceGroup'],
		data : _this.spaceGroupList
	});

	var experimentTypeClassStore = Ext.create('Ext.data.Store', {
		id : 0,
		fields : ['experimentType'],
		data : _this.experimentTypeList
	});

	var comboProtein = new Ext.form.ComboBox({
		mode : 'local',
		emptyText : '---',
		store : proteinClassStore,
		valueField : 'proteinAcronym',
		displayField : 'proteinAcronym',
		queryMode: 'local',
		allowBlank : false,
		typeAhead : true,
		triggerAction : 'all', 
		listConfig: {cls: 'small-combo-text'}
	});
	var comboSpaceGroup = new Ext.form.ComboBox({
		mode : 'local',
		emptyText : '---',
		store : spaceGroupClassStore,
		valueField : 'spaceGroup',
		displayField : 'spaceGroup',
		allowBlank : true,
		queryMode: 'local',
		typeAhead : true,
		triggerAction : 'all'
	});

	var comboExperimentType = new Ext.form.ComboBox({
		mode : 'local',
		emptyText : '---',
		store : experimentTypeClassStore,
		valueField : 'experimentType',
		displayField : 'experimentType',
		allowBlank : false,
		queryMode: 'local',
		typeAhead : true,
		triggerAction : 'all', 
		listConfig: {cls: 'small-combo-text'}
	});
	comboExperimentType.setValue("Default");

	// builds the columns
	var columns = [{
		text : 'Sample<br>Position',
		dataIndex : 'position',
		flex : 0.04,
		renderer : renderPosition
	}, {
		text : 'Protein<br>Acronym (*)',
		dataIndex : 'proteinAcronym',
		flex : 0.1,
		editor : comboProtein,
		renderer : renderProtein
	}, {
		text : 'Sample<br>Name (*)',
		dataIndex : 'sampleName',
		flex : 0.09,
		editor : {
			xtype : 'textfield',
			allowBlank : false,
			maxLength : 50,
			regex: /^[a-zA-Z0-9_-]+$/,
			regexText: 'Make sure the sample name contains only a-z , A-Z or 0-9 or - or _ characters!',
			msgTarget: 'under'
		},
		renderer : renderSample
	}, {
		text : 'PinBarcode',
		dataIndex : 'pinBarcode',
		flex : 0.07,
		editor : {
			xtype : 'textfield'
		}
	}, {
		text : 'Space<br>Group',
		dataIndex : 'spaceGroup',
		flex : 0.075,
		editor : comboSpaceGroup // specify reference to combo instance
		//renderer : Ext.util.Format.comboRenderer(comboSpaceGroup)
	}, {
		text : 'Pre-Observed<br>resolution',
		dataIndex : 'preObservedResolution',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Needed<br>resolution',
		dataIndex : 'neededResolution',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Oscillation<br>Range',
		dataIndex : 'oscillationRange',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		},
		renderer : renderOscillationRange
	}, {
		text : 'Experiment<br>Type',
		dataIndex : 'experimentType',
		flex : 0.075,
		editor : comboExperimentType // specify reference to combo
										// instance
		//renderer : Ext.util.Format.comboRenderer(comboExperimentType)
	}, {
		text : 'Unit Cell<br>a',
		dataIndex : 'unitCellA',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Unit Cell<br>b',
		dataIndex : 'unitCellB',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Unit Cell<br>c',
		dataIndex : 'unitCellC',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Unit Cell<br>alpha',
		dataIndex : 'unitCellAlpha',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Unit Cell<br>beta',
		dataIndex : 'unitCellBeta',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, {
		text : 'Unit Cell<br>gamma',
		dataIndex : 'unitCellGamma',
		flex : 0.05,
		editor : {
			xtype : 'textfield',
			allowBlank : true
		}
	}, 
	 {
		text : 'SMILES',
		dataIndex : 'smiles',
		flex : 0.1,
		editor : {
			xtype : 'textfield',
			allowBlank : true,
			maxLength : 400
		},
		renderer : columnWrap
	}, 
	{
		text : 'Comments',
		dataIndex : 'comments',
		flex : 0.1,
		editor : {
			xtype : 'textfield',
			allowBlank : true,
			maxLength : 1024
		},
		renderer : columnWrap
	}];

	// actions: copy paste sample. We can add a delete sample
	columns.push({
			xtype : 'actioncolumn',
			width : 50,
			header : 'Action',
			sortable : false,
			renderer: function (val, metadata, record) {
				this.items[1].disabled = !_this.canCopySample();
				return val;
			},
			items : [{
				icon : '../images/editcopy.png',
				tooltip : 'Copy sample',
				handler : function (grid, rowIndex, colIndex) {
					_this.copySample(_this.store.getAt(rowIndex).data);
				}
			}, 
			{
				icon : '../images/editpaste.png',
				tooltip : 'Paste sample',
				handler : function (grid, rowIndex, colIndex) {
					_this.pasteSample(rowIndex);
				}
			}]
		});
	return columns;

};

// copy sample click: set in memory the sample to copy and throws an event
CreatePuckGrid.prototype.copySample = function (data) {
	var _this = this;
	if (data){
		_this.sampleToCopy = data;
		_this.copyId = 1;
		_this.grid.getView().refresh();
		this.onCopySampleButtonClicked.notify({
			'sampleToCopy' : _this.sampleToCopy
		});
	}
};

// return true if a sample has been copied
CreatePuckGrid.prototype.canCopySample = function () {
	var _this = this;
	return _this.sampleToCopy && _this.sampleToCopy.position;
};

// set the sample to be copied in memory (could be a sample from another puck)
CreatePuckGrid.prototype.setSampleToCopy = function (sampleToCopy) {
	var _this = this;
	_this.sampleToCopy = sampleToCopy;
	if (!_this.copyId){
		_this.copyId = 1;
	}
	_this.grid.getView().refresh();
};

// paste the sample into the line
CreatePuckGrid.prototype.pasteSample = function (rowIndex) {
	var _this = this;
	if (_this.canCopySample()) {
		_this.grid.store.getAt(rowIndex).set('proteinAcronym', _this.sampleToCopy.proteinAcronym);
		_this.grid.store.getAt(rowIndex).set('sampleName', _this.sampleToCopy.sampleName + "_" + _this.copyId);
		_this.grid.store.getAt(rowIndex).set('pinBarcode', _this.sampleToCopy.pinBarcode);
		_this.grid.store.getAt(rowIndex).set('spaceGroup', _this.sampleToCopy.spaceGroup);
		_this.grid.store.getAt(rowIndex).set('preObservedResolution', _this.sampleToCopy.preObservedResolution);
		_this.grid.store.getAt(rowIndex).set('neededResolution', _this.sampleToCopy.neededResolution);
		_this.grid.store.getAt(rowIndex).set('oscillationRange', _this.sampleToCopy.oscillationRange);
		_this.grid.store.getAt(rowIndex).set('experimentType', _this.sampleToCopy.experimentType);
		_this.grid.store.getAt(rowIndex).set('unitCellA', _this.sampleToCopy.unitCellA);
		_this.grid.store.getAt(rowIndex).set('unitCellB', _this.sampleToCopy.unitCellB);
		_this.grid.store.getAt(rowIndex).set('unitCellC', _this.sampleToCopy.unitCellC);
		_this.grid.store.getAt(rowIndex).set('unitCellAlpha', _this.sampleToCopy.unitCellAlpha);
		_this.grid.store.getAt(rowIndex).set('unitCellBeta', _this.sampleToCopy.unitCellBeta);
		_this.grid.store.getAt(rowIndex).set('unitCellGamma', _this.sampleToCopy.unitCellGamma);
		_this.grid.store.getAt(rowIndex).set('smiles', _this.sampleToCopy.smiles);
		_this.grid.store.getAt(rowIndex).set('comments', _this.sampleToCopy.comments);
		
		_this.copyId = _this.copyId + 1;
		_this.changeSampleName(_this.sampleToCopy.sampleName, _this.sampleToCopy.position - 1);
	}
};

// returns the json data of samples in this grid
CreatePuckGrid.prototype.getListSamples = function () {
	var jsonData = Ext.encode(Ext.pluck(this.store.data.items, 'data'));
	return jsonData;
};

// returns the puck information
CreatePuckGrid.prototype.getPuckInfo = function () {
	var _this = this;
	var puckInfo = [];
	puckInfo.shipmentName = _this.puckData.puckInfo.shipmentName;
	puckInfo.dewarCode = _this.puckData.puckInfo.dewarCode;
	puckInfo.puckCode = _this.puckData.puckInfo.code;
	puckInfo.shippingId = _this.puckData.puckInfo.shippingId;
	puckInfo.dewarId = _this.puckData.puckInfo.dewarId;
	puckInfo.containerId = _this.puckData.puckInfo.containerId;
	return puckInfo;
};

			
// protein has been selected: change the values depending of the crystal form
CreatePuckGrid.prototype.changeProteinAcronym = function (newValue, idRow) {
	var crystal = this.getCellValue(newValue);
	var i = 0;
	this.grid.store.getAt(idRow).set('unitCellA', crystal.cellA);
	this.grid.store.getAt(idRow).set('unitCellB',  crystal.cellB);
	this.grid.store.getAt(idRow).set('unitCellC',  crystal.cellC);
	this.grid.store.getAt(idRow).set('unitCellAlpha', crystal.cellAlpha);
	this.grid.store.getAt(idRow).set('unitCellBeta', crystal.cellBeta);
	this.grid.store.getAt(idRow).set('unitCellGamma',  crystal.cellGamma);
	this.grid.store.getAt(idRow).set('spaceGroup',  crystal.spaceGroup);
	this.grid.store.getAt(idRow).set('comments',  crystal.comments);
	if (crystal.diffractionPlanVO) {
		this.grid.store.getAt(idRow).set('oscillationRange',  crystal.diffractionPlanVO.oscillationRange);
		this.grid.store.getAt(idRow).set('neededResolution',  crystal.diffractionPlanVO.requiredResolution);
		this.grid.store.getAt(idRow).set('preObservedResolution',  crystal.diffractionPlanVO.observedResolution);
		this.grid.store.getAt(idRow).set('experimentType',  crystal.diffractionPlanVO.experimentKind);
	}
};

// returns true if the oscillation range can be edited
CreatePuckGrid.prototype.canEditOscillationRange = function (value) {
	return (value != "MXPressE" && value != "MXPressO" && value != "MXPressL" && value != "MXScore");
};

// experiment type has been selected: change the oscillation range value and set it disabled or not
CreatePuckGrid.prototype.changeExperimentType = function (newValue, idRow) {
	if (!this.canEditOscillationRange(newValue)) {
		this.grid.store.getAt(idRow).set('oscillationRange',  "");
	}
};

// search the crystal value from a protein value
CreatePuckGrid.prototype.getCellValue = function (proteinValue) {
	var cell = new Array(6);
	// initialize
	for (var i = 0; i < 7; i++) {
		cell[i] = "";
	}
	// get protein index
	var index = -1;
	for (var i = 0; i < this.proteinList.length; i++) {
		if (this.proteinList[i].proteinAcronym == proteinValue) {
			index = i;
			break;
		}
	}
	// search crystal values
	if (index != -1) {
		cell = this.crystalValuesList[index];
	}
	return cell;
};

// reset the whole grid
CreatePuckGrid.prototype.reset = function () {
	var f = [];
	for (var i = 0; i < 10; i++) {
		f.push({
					sampleId : this.features[i].sampleId,
					position : i + 1,
					// sampleName: "sample"+this._formatPostionNumber(i+1),
					experimentType : "Default"
				});
	}
	this.features = f;
	this.store.loadData(this.features, false);
};

// delete a puck click: after confirmation throws an event
CreatePuckGrid.prototype.deletePuck = function () {
	var _this = this;
	if (confirm("Do you want to remove this puck?")) {
		this.onRemovePuckButtonClicked.notify({
					'containerId' : _this.puckData.puckInfo.containerId
		});
	}
};

// copy puck click: throws an event
CreatePuckGrid.prototype.copyPuck = function () {
	this.onCopyPuckButtonClicked.notify({
		'containerId' : this.puckData.puckInfo.containerId, 
		'listSamples': this.getListSamples()
	});
};


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

/*global Ext */
// Dewar form (jsut the label for now)
function DewarForm(args) {
	
	this.dewar = null;

	/** STRUCTURES * */

}


// return the dewar with the values from the form
DewarForm.prototype.getDewar = function () {
	var _this = this;
	this.dewar.dewarName = Ext.getCmp(this.panel.getItemId()).getValues().dewarName;
	
	return this.dewar;
};


// refresh data with the given dewar
DewarForm.prototype.refresh = function (dewar) {
	this.dewar = dewar;
};


// builds and returns the panel 
DewarForm.prototype.getPanel = function (dewar) {
	this.dewar = dewar;
	var _this = this;
	if (this.panel == null) {
		this.panel = Ext.createWidget('form', {
			bodyPadding : 5,
			frame : true,
			border : 0,
			layout: 'fit',
			fieldDefaults : {
				labelAlign : 'left'
				//anchor : '100%'
			},
			items : [{
				xtype : 'container',
				border : false,
				defaultType : 'textfield',
				items : [{
					fieldLabel : 'Dewar label',
					name : 'dewarName',
					tooltip : "Dewar label",
					width: 350,
					allowBlank : false,
					maxLength: 45,
					value : this.dewar.dewarName
				}]
			}]

		});
	}
	return this.panel;
};

// is a valid form?
DewarForm.prototype.isValid = function () {
	return this.panel.getForm().isValid();
};

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

/*global GenericWindow */
/*global DewarForm */

// Dewar window (from genericWindow) to edit the dewar (just label for now)
function DewarWindow(args) {
	this.width = 400;
	this.height = 100;
	this.addMode = true;
	if (args != null) {
		if (args.addMode != null) {
			this.addMode = args.addMode;
		}
	}
	// form panel inside		
	this.form = new DewarForm(args);
	GenericWindow.prototype.constructor.call(this, {
		form : this.form,
		width : this.width,
		height : this.height
	});
}

DewarWindow.prototype.getButtons = GenericWindow.prototype.getButtons;
DewarWindow.prototype._render = GenericWindow.prototype._render;
DewarWindow.prototype._postRender = GenericWindow.prototype._postRender;	

// save button: check the form, throws an event and close the window
DewarWindow.prototype.save = function () {
	if (!this.form.isValid()){
		BUI.showError("The dewar label is required");
		return;
	}
	this.onSaved.notify(this.form.getDewar());
	this.panel.close();
};

// cancel button
DewarWindow.prototype.cancel = function () {
	this.onCancelled.notify();
	this.panel.close();
};


// display the window, with the given dewar values
DewarWindow.prototype.draw = function (dewar) {
	this.title = "Edit dewar";
	if (this.addMode == true){
		this.title = "Add a new dewar";
	}
	this._render(dewar);
};
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
/*global Ext */

// help panel for the fillShipment page or create puck
function HelpPanel(args) {
	var _this = this;
	this.width = 550;
	this.height = 500;
	
	// if true display the whole help, otherwise, only regarding the puck creation
	this.allHelp = true;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.allHelp != null) {
			this.allHelp = args.allHelp;
		}
	}
	
	
}

// builds and returns the main panel
HelpPanel.prototype.getPanel = function () {
	var _this = this;
	
	var helpText = "<div style=\"white-space:normal !important;word-wrap: break-word;\">";
	if (_this.allHelp == true){
		helpText += "<h4>Description</h4>";
		helpText += "This page allows you to describe the composition of your shipment, by editing the contents of the dewars.</br>";
	
		helpText += "</br><h4>Dewar level</h4>";
		helpText += "Each tab represents a dewar.</br>";
		helpText += "<img src=\"../images/Dewar.png\" alt=\"Dewar\">+: to add a new dewar to a shipment.</br>";
		helpText += "<img src=\"../images/Edit_16x16_01.png\" alt=\"Edit label\">: to change the name of the selected dewar. Remember that the names of the dewars must be unique within the same shipment.</br>";
		helpText += "<img src=\"../images/print_16x16.png\" alt=\"Print components labels\">: to print your component labels. Stick them to your shipment components prior to sending (see instructions on first page) in order to track progress to the beamline.</br>";
		helpText += "<img src=\"../images/editpaste.png\" alt=\"Paste puck\">: to paste the contents of a puck into the current dewar after you have copied them from the puck interface using " +
			"one of the two icons <img src=\"../images/editcopy.png\" alt=\"Copy puck\"> or <img src=\"../images/editcut.png\" alt=\"Cut puck\">. " +
			"You can then change the puck label and the samples names (using <img src=\"../images/Edit_16x16_01.png\" alt=\"Edit label\">at the appropriate level) which are automatically modified to avoid a duplication of the sample names in the same shipment.</br>";
		helpText += "<img src=\"../images/cancel.png\" alt=\"Delete dewar\">: to remove the selected dewar and its contents.</br>";
	
		helpText += "</br><h4>Puck level</h4>";
		helpText += "Each tab represents a puck. </br>";
	}
	helpText += "Each row of the table represents a sample at a given position in the puck. Make sure you provide data in all mandatory fields (highlighted in light red) for each sample. </br>";
	helpText += "Sample names: </br>";
	helpText += "- Make sure the sample name contains only a-z , A-Z or 0-9 or - or _ characters.</br>";
	helpText += "- Make sure the sample name is unique for a protein in the same shipment.</br>";
	helpText += "The experiment type should not be null, Default as default. </br>";
	helpText += "The top-left red icon in a cell <img src=\"../images/help/gridFieldUpdated.PNG\" alt=\"modification icon\"> indicates that the field has been updated. " +
			"You have to click on the <img src=\"../images/save.gif\" alt=\"Save\"> button to save any modifications. </br>";
	if (_this.allHelp == true){
		helpText += "<img src=\"../images/Basket.png\" alt=\"Puck\">+: to add a new puck to the selected dewar.</br>";
		helpText += "<img src=\"../images/Edit_16x16_01.png\" alt=\"Edit label\">: to change the selected puck label (to its barcode for example). Remember that the labels of the pucks must be unique within the same shipment.</br>";
		helpText += "<img src=\"../images/editcopy.png\" alt=\"Copy puck\">: to copy a puck and its contents. You can then paste this in any dewar (in this shipment or another), by clicking on the <img src=\"../images/editpaste.png\" alt=\"Paste puck\"> button at the dewar menu level.</br>";
		helpText += "<img src=\"../images/editcut.png\" alt=\"Cut puck\">: to copy the puck into the clipboard and remove it. You can then paste the puck into another dewar.</br>";
	}
	helpText += "<img src=\"../images/save.gif\" alt=\"Save\">: to save your modifications.</br>";
	if (_this.allHelp == true){
		helpText += "<img src=\"../images/cancel.png\" alt=\"Delete puck\">: to remove this puck and its contents.</br>";
	}
	helpText += "<img src=\"../images/recur.png\" alt=\"Reset\">: to reset the puck contents, all fields are set to their default values.</br>";
	helpText += "<img src=\"../images/help/changeSampleNameAuto.PNG\" alt=\"change sample name\">: to automatically update the names of the samples in the puck, with the same protein (protein acronym and space group), by adding an increment number to the name of the 1st sample.</br>";
	
	helpText += "</br><h4>Experiment type definitions</h4>";
	helpText += "These types are defined for automatic processes to be performed on that sample. This is an ongoing developement. These parameters will only be taken in account for specific pilote studies. Don't use this field if you don't belong to that studies.</br>";
	helpText += "<b>Default</b> = manual intervention by the crystallographer at the beamline</br>";
	helpText += "<b>MXPressE</b> = the sample is automatically going through an autoloop centring followed by an X-ray centring, by an EDNA enhanced characterisation and a data collection based on the EDNA proposed diffraction plan</br>";
	helpText += "<b>MXPressO</b> = the sample is automatically going through an autoloop centring followed by an X-ray centring and a data collection of 180&deg; by steps of 0.2&deg; oscillations with an attenuated beam (value calculated on an average protein crystal size)</br>";
	helpText += "<b>MXPressL</b> = the sample is automatically going through an autoloop centring followed by a data collection of 180&deg; by steps of 0.2&deg; oscillations with an attenuated beam (value calculated on an average protein crystal size)</br>";
	helpText += "<b>MXScore</b> = the sample is automatically going through an autoloop centring followed by an X-ray centring and an EDNA characterisation.</br>";
	
	helpText += "</div>";
	
	//main panel
	_this.panel = Ext.widget({
		xtype : 'form',
		layout : {
			type : 'fit'
		},
		collapsible : true,
		collapsed:true,
		frame : true,
		title: 'Help',
		
		style : {
			marginBottom : '10px'
		},
		
		items : [ {
            xtype: 'component',
            style: 'margin-top:10px',
            html: helpText
        }]
	});

	return _this.panel;
};


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
/*global Ext */
/*global CreatePuckGrid */
/*global Shipping */
/*global ErrorPanel */
/*global HelpPanel */
/*global PuckHeadPanel */
/*global document */

// main entry point for puck creation
Ext.Loader.setConfig({
	enabled : true
});

var createPuckGrid = null;
var puckHeadPanel = null;

var IspybCreatePuck = {
	start : function (targetId) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showCreatePuckPage(targetId);
	},

	showCreatePuckPage : function (targetId) {
		var _this = this;
		// ajax call class
		var shipping = new Shipping();
		// listen for events
		shipping.onSaved.attach(function (sender, newData) {
					if (newData && newData.errors && newData.errors.length > 0) {
						var args = [];
						args.targetId = targetId;
						args.errors = newData.errors;
						var errorPanel = new ErrorPanel(args);
						return;
					}
					_this.refreshPuck(newData);
				});

		// puck grid
		createPuckGrid = new CreatePuckGrid();
		// listen events on the grid
		createPuckGrid.onSaveButtonClicked.attach(function (sender, args) {
					var listSamples = createPuckGrid.getListSamples();
					var puckInfo = puckHeadPanel.getPuckInfo();
					shipping.savePuck(listSamples, puckInfo);
				});		
		
		shipping.shippingRetrieved.attach(function (evt, data) {
			// panel with info about shipping and dewar
			puckHeadPanel = new PuckHeadPanel();

			var argsHelp = [];
			argsHelp.allHelp = false;
			// help panel
			var helpPanel = new HelpPanel(argsHelp);

			var mainItems = [];
			mainItems.push(helpPanel.getPanel());
			mainItems.push(puckHeadPanel.getPanel(data));
			mainItems.push(createPuckGrid.getGrid(data));
			// panel with annotations
			var subPanel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'fit'
				},
				frame : true,

				style : {
					marginTop : '10px'
				},

				items : [ {
					xtype: 'component',
					style: 'margin-top:10px',
					html: "(*) mandatory field for each sample"
				}]
			});
			mainItems.push(subPanel);
			// main panel
			_this.panel = Ext.create('Ext.panel.Panel', {
				plain : true,
				frame : false,
				border : 0,
				width: "100%",
				renderTo : targetId,
				style : {
					padding : 0
				},
				items : mainItems
			});

		});
		// retrieve shipping information
		shipping.getInformationForPuck();
	},

	refreshPuck : function (newdata) {
		var _this = this;
		createPuckGrid.refresh(newdata);
		if (newdata && newdata.shippingId && newdata.shippingId != -1) {
			document.location.href = "viewSample.do?reqCode=displayForShipping&shippingId=" + newdata.shippingId;
		}
	}

};

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
/*global document */
/*global Shipping */
/*global CreatePuckGrid */
/*global DewarWindow */
/*global PuckWindow */
/*global ErrorPanel */
/*global HelpPanel */
/*global FillShipmentPanel */
/*global PuckHeadPanel */

Ext.Loader.setConfig({
	enabled : true
});

var createPuckGrid = null;
var fillShipmentPanel = null;
// main etry point for fillShipment page
var IspybFillShipment = {
	start : function (targetId) {
		this.tartgetId = targetId;
		Ext.Loader.setPath('Ext.ux', '../js/external/ux');
		Ext.require(['Ext.selection.CellModel', 'Ext.grid.*', 'Ext.data.*',
						'Ext.ux.data.PagingMemoryProxy', 'Ext.util.*',
						'Ext.state.*', 'Ext.form.*', 'Ext.ux.CheckColumn',
						'Ext.ux.RowExpander', 'Ext.selection.CheckboxModel',
						'Ext.selection.CellModel',
						'Ext.layout.container.Column', 'Ext.tab.*',
						'Ext.ux.grid.FiltersFeature',
						'Ext.selection.CellModel', 'Ext.state.*', 'Ext.form.*',
						'Ext.ux.CheckColumn', 'Ext.ux.form.MultiSelect',
						'Ext.ux.form.ItemSelector', 'Ext.chart.*',
						'Ext.Window', 'Ext.fx.target.Sprite',
						'Ext.layout.container.Fit', 'Ext.window.MessageBox',
						'Ext.tip.*']);
		Ext.QuickTips.init();

		this.showFillShipmentPage(targetId);
	},

	showFillShipmentPage : function (targetId) {
		var _this = this;
		// ajax call class
		var shipping = new Shipping();
		
		createPuckGrid = new CreatePuckGrid();
		var puckHeadPanel = new PuckHeadPanel();
		
		createPuckGrid.onSaveButtonClicked.attach(function (sender, args) {
					var listSamples = createPuckGrid.getListSamples();
					var puckInfo = puckHeadPanel.getPuckInfo();
					shipping.savePuck(listSamples, puckInfo);
				});
		
		// listen for events
		shipping.shipmentRetrieved.attach(function (evt, data) {
			// errors
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			// no errors: we create the main panel
			fillShipmentPanel = new FillShipmentPanel();
			// listen for events:
			fillShipmentPanel.onAddDewarButtonClicked.attach(function (sender, args) {
				var shippingId = args.shippingId;
				var dewar = [];
				dewar.dewarName = "";
				var argsWindow = [];
				argsWindow.addMode = true;
				var dewarWindow = new DewarWindow(argsWindow);
				dewarWindow.onSaved.attach(function (evt, dewarSave) {
					shipping.addDewar(dewarSave);
				});
				dewarWindow.onCancelled.attach(function (evt) {
					fillShipmentPanel.setFirstActiveTab();
				});
				dewarWindow.draw(dewar);
			});
			
			fillShipmentPanel.onEditDewarButtonClicked.attach(function (sender, args) {
				var dewar = [];
				dewar.dewarName = args.dewarName;
				dewar.dewarId = args.dewarId;
				var argsWindow = [];
				argsWindow.addMode = false;
				var dewarWindow = new DewarWindow(argsWindow);
				dewarWindow.onSaved.attach(function (evt, dewarSave) {
					shipping.editDewar(dewarSave);
				});
				dewarWindow.onCancelled.attach(function (evt) {
				});
				dewarWindow.draw(dewar);
			});
			
			fillShipmentPanel.onRemoveDewarButtonClicked.attach(function (sender, args) {
				shipping.removeDewar(args.dewarId);
				
			});
			
			fillShipmentPanel.onAddPuckButtonClicked.attach(function (sender, args) {
				var dewarId = args.dewarId;
				var puck = [];
				puck.puckName = "";
				puck.dewarId = args.dewarId;
				var argsWindow = [];
				argsWindow.addMode = true;
				var puckWindow = new PuckWindow(argsWindow);
				puckWindow.onSaved.attach(function (evt, puckSave) {
					shipping.addPuck(puckSave);
				});
				puckWindow.onCancelled.attach(function (evt) {
					fillShipmentPanel.setPuckFirstActiveTab();
				});
				puckWindow.draw(puck);
			});
			
			fillShipmentPanel.onPastePuckButtonClicked.attach(function (sender, args) {
				shipping.pastePuck(args.dewarId);
				
			});
			
			fillShipmentPanel.onEditPuckButtonClicked.attach(function (sender, args) {
				var puck = [];
				puck.puckName = args.puckName;
				puck.containerId = args.containerId;
				var argsWindow = [];
				argsWindow.addMode = false;
				var puckWindow = new PuckWindow(argsWindow);
				puckWindow.onSaved.attach(function (evt, puckSave) {
					shipping.editPuck(puckSave);
				});
				puckWindow.onCancelled.attach(function (evt) {
				});
				puckWindow.draw(puck);
			});
			
			fillShipmentPanel.onRemovePuckButtonClicked.attach(function (sender, args) {
				shipping.removePuck(args.containerId);
				
			});
			
			fillShipmentPanel.onSavePuckButtonClicked.attach(function (sender, args) {
				shipping.updatePuck(args.listSamples, args.puckInfo);
				
			});
			
			fillShipmentPanel.onCopyPuckButtonClicked.attach(function (sender, args) {
				shipping.copyPuck(args.containerId, args.listSamples);
			});
			
			// help panel
			var helpPanel = new HelpPanel();
			
			var mainItems = [];
			mainItems.push(helpPanel.getPanel());
			mainItems.push(fillShipmentPanel.getPanel(data));
			// panel with annotations
			var subPanel = Ext.widget({
				xtype : 'form',
				layout : {
					type : 'fit'
				},
				frame : true,
				
				style : {
					marginTop : '10px'
				},
				
				items : [ {
					xtype: 'component',
					style: 'margin-top:10px',
					html: "(*) mandatory field for each sample"
				}]
			});
			mainItems.push(subPanel);
			// main panel
			_this.panel = Ext.create('Ext.panel.Panel', {
					plain : true,
					frame : false,
					border : 0,
					width: "100%",
					renderTo : targetId,
					style : {
						padding : 0
					},
					items : mainItems
				});

		});
		
		// listen more events
		shipping.onDewarUpdated.attach(function (evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			fillShipmentPanel.refresh(data);
			fillShipmentPanel.setLastActiveTab();
		});
		
		shipping.onPuckUpdated.attach(function (evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			fillShipmentPanel.refresh(data);
			fillShipmentPanel.setPuckLastActiveTab();
		});
		
		shipping.onPuckCopied.attach(function (evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			fillShipmentPanel.setPuckPasteActiv(true);
		});
		
		shipping.onPuckPasted.attach(function (evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
			fillShipmentPanel.refresh(data);
			//fillShipmentPanel.setPuckPasteActiv(false);
		});
		
		shipping.onSaved.attach(function (evt, data) {
			if (data.errors && data.errors.length > 0) {
				var args = [];
				args.targetId = targetId;
				args.errors = data.errors;
				var errorPanel = new ErrorPanel(args);
				return;
			}
		});
		// retrieve shipping information	
		shipping.getInformationForShipping();
	}		

};
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

/*global Ext */

// Puck form to edit a puck (just the label for now)
function PuckForm(args) {
	this.puck = null;

	/** STRUCTURES * */

}

// returns the puck with new values
PuckForm.prototype.getPuck = function () {
	var _this = this;
	this.puck.puckName = Ext.getCmp(this.panel.getItemId()).getValues().puckName;
	
	return this.puck;
};

// refresh the data
PuckForm.prototype.refresh = function (puck) {
	this.puck = puck;
};


// builds and returns the panel- form
PuckForm.prototype.getPanel = function (puck) {
	this.puck = puck;
	var _this = this;
	if (this.panel == null) {
		this.panel = Ext.createWidget('form', {
			bodyPadding : 5,
			frame : true,
			border : 0,
			layout : {
				type : 'vbox'
			},
			fieldDefaults : {
				labelAlign : 'left'
				//anchor : '100%'
			},
			items : [{
				xtype : 'container',
				border : false,
				defaultType : 'textfield',
				items : [{
					fieldLabel : 'Puck label',
					name : 'puckName',
					tooltip : "Puck label",
					allowBlank : false,
					width: 350,
					maxLength: 45,
					value : this.puck.puckName
				}]
			}]

		});
	}
	return this.panel;
};

// is it a valid form?
PuckForm.prototype.isValid = function () {
	return this.panel.getForm().isValid();
};




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


// panel which contains information about shipping and dewar (in the Create Puck page)
function PuckHeadPanel(args) {
	var _this = this;
	this.width = 550;
	this.height = 500;

	this.onSavePuckInfo = new Event(this);

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}
	
	this.shippingId = null;
	this.dewarId = null;
	this.containerId = null;
	
}

// returns the information filled in the panel
PuckHeadPanel.prototype.getPuckInfo = function () {
	var shipmentName = Ext.getCmp(this.panel.getItemId()).getValues().shipmentName;
	var dewarCode = Ext.getCmp(this.panel.getItemId()).getValues().dewarCode;
	var puckCode = Ext.getCmp(this.panel.getItemId()).getValues().puckCode;
	var info = [];
	info.shipmentName = shipmentName;
	info.dewarCode = dewarCode;
	info.puckCode = puckCode;
	info.shippingId = this.shippingId;
	info.dewarId = this.dewarId;
	info.containerId = this.containerId;
	return info;
};


// builds and returns the panel
PuckHeadPanel.prototype.getPanel = function (data) {
	var _this = this;
	
	var shipmentName = "";
	var dewarCode = "";
	var puckCode = "";
	var readOnlyShip = false;
	var readOnlyPuck = false;
	// fill information
	if (data && data.puckInfo) {
		shipmentName = data.puckInfo.shipmentName;
		dewarCode = data.puckInfo.dewarCode;
		puckCode = data.puckInfo.puckCode;
		_this.shippingId = data.puckInfo.shippingId;
		_this.dewarId = data.puckInfo.dewarId;
		_this.containerId = data.puckInfo.containerId;
		if (data.puckInfo.shippingId && data.puckInfo.shippingId != "null")
			readOnlyShip = true;
		if (data.puckInfo.containerId && data.puckInfo.containerId != "null")
			readOnlyPuck = true;
	}
	// main panel
	_this.panel = Ext.widget({
		xtype : 'form',
		layout : {
			type : 'vbox'
		},
		collapsible : true,
		frame : true,
		title: 'Puck Information',
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
		items : [{
			fieldLabel : 'Shipping',
			name : 'shipmentName',
			tooltip : "Shipping Name",
			value: shipmentName,
			readOnly: readOnlyShip,
			allowBlank : false,
			maxLength : 45, 
			hideTrigger : true
		},
		{
			fieldLabel : 'Dewar',
			name : 'dewarCode',
			tooltip : "Dewar code",
			value: dewarCode,
			readOnly: readOnlyShip,
			allowBlank : false,
			maxLength : 45, 
			hideTrigger : true
		},
		{
			fieldLabel : 'Puck',
			name : 'puckCode',
			tooltip : "Puck code",
			value: puckCode,
			readOnly: readOnlyPuck,
			allowBlank : false,
			maxLength : 45, 
			hideTrigger : true
		}]
	});

	return _this.panel;
};
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

/*global GenericWindow */
/*global PuckForm */
/*global BUI */

// puck window, in order to edit a puck (puck form inside). The parent is a Generic Window
function PuckWindow(args) {
	this.width = 400;
	this.height = 100;
	this.addMode = true;
	if (args != null) {
		if (args.addMode != null) {
			this.addMode = args.addMode;
		}
	}
			
	this.form = new PuckForm(args);
	GenericWindow.prototype.constructor.call(this, {
		form : this.form,
		width : this.width,
		height : this.height
	});
}

PuckWindow.prototype.getButtons = GenericWindow.prototype.getButtons;
PuckWindow.prototype._render = GenericWindow.prototype._render;
PuckWindow.prototype._postRender = GenericWindow.prototype._postRender;

// save button: throws an event and close the panel
PuckWindow.prototype.save = function () {
	if (!this.form.isValid()) {
		BUI.showError("The puck label is required");
		return;
	}
	this.onSaved.notify(this.form.getPuck());
	this.panel.close();
};

//cancel button
PuckWindow.prototype.cancel = function () {
	this.onCancelled.notify();
	this.panel.close();
};

// display the window
PuckWindow.prototype.draw = function (puck) {
	this.title = "Edit puck";
	if (this.addMode == true) {
		this.title = "Add a new puck";
	}
	this._render(puck);
};
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
var MXUI = {
	//interval : 60000,
		interval : 40000,

	getShippingURL : function(shippingId) {
		return '/ispyb/user/viewProjectList.do?reqCode=display&menu=shipment&shippingId=' + shippingId;
	},
	
	getJpegByImageIdandPath : function(imageId, path) {
		return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
		"<a  target='_blank' href='viewResults.do?reqCode=viewJpegImage&imageId=" + 
		imageId + 
		"'  styleClass='LIST' >" + 
		"<img width='110' height='110' src='imageDownload.do?reqCode=getImageJpgFromFile&file=" + 
		path + 
		"' border='0' alt='Click to zoom the image'>" + 
		"</a></div>";
	},
	
	getSnapshotByPath : function(path) {
		return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
		"<a  target='_blank' href='viewResults.do?reqCode=getJpegImageFromFile&file=" + 
		path + 
		"'  styleClass='LIST'>" + 
		"<img width='110' height='83' src='imageDownload.do?reqCode=getImageJpgFromFile&file=" + 
		path + 
		"' border='0' alt='Click to zoom the image' >" + 
		"</a></div>";
		
	//	var url = "viewResults.do?reqCode=getJpegImageFromFile&file=" + path;
	//	var event = "OnClick= window.open('" + url + "')";
	//	return '<img src=' + url + '   height="83" width="110" border="0" alt="Click to zoom the image'  + event + '>';
	},
	
	getGraphByPath : function(path) {

		return "<div style='white-space:normal !important;word-wrap: break-word;'>" + 
		"<a  target='_blank' href='viewResults.do?reqCode=getJpegImageFromFile&file=" + 
		path + "'  styleClass='LIST'>" + 
		"<img width='120' height='100' src='imageDownload.do?reqCode=getImageJpgFromFile&file=" + 
		path + "' border='0' alt='Click to zoom the image' >" + "</a></div>";
	},

	getURL : function() {
		return this.getBaseURL() + '?reqCode=getImage';

	},

};

