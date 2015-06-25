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