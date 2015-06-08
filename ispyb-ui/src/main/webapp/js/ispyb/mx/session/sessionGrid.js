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
