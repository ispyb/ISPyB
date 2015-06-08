/**
 * Shows an experiments with the specimens, measurements, analysis tabs where results are shown and the frames widget
 * 
 * @targetId
 */
function HPLCTabs(targetId) {
	this.height = 1400;
	this.targetId = targetId;

	this.id = BUI.id();

	this.gridHeight = 600;
	this.pointsCount = 1036;
	this.plotHeight = 350;
	this.plotWidth = 800;
	this.plotPanelPadding = 5;

	var _this = this;

	this.analysisGrid = new HPLCAnalysisGrid({
		height : Ext.getBody().getViewSize().height * 0.9 - 300,
		positionColumnsHidden : true,
		sorters : [ {
			property : 'priorityLevelId',
			direction : 'ASC'
		} ]
	});

	this.frameGrid = new FrameGrid({
		width : 600,
		height : 60,
		collapsed : false,
		tbar : false
	});
	
	this.peakGrid = new PeakGrid({
		width : 550,
		height : 500,
		collapsed : false,
		tbar : false
	});
	
	this.peakGrid2 = new PeakGrid({
		width : 102,
		height : this.plotHeight,
		collapsed : false,
		tbar : false,
		showExtendedColumns : true
	});
	
	this.mainPlotPanel = new HPLCGraph({
		title : 'I0',
		width : this.plotWidth - 110,
		height : this.plotHeight,
		bbar : true,
		plots : {
			"I0" : true,
			"Rg" : true
		},
		xlabel : "HPLC Frames",
		scaled : true,
		interactionModel : {
			'dblclick' : function(event, g, context) {
				_this._selectFrame(g.lastx_);
				var annotations = [];
				annotations.push({
					series : g.selPoints_[0].name,
					x : g.lastx_,
					width : 100,
					height : 23,
					tickHeight : 4,
					shortText : g.lastx_,
					text : g.lastx_,
					attachAtBottom : true
				});
				g.setAnnotations(annotations);

			}
		}
	});

	this.intensityPlotPanel = new MergesHPLCGraph({
		title : 'Scattering',
		width : this.plotWidth,
		height : 500,
		showRangeSelector : false,
		xParam : 0,
		xlabel : "scattering_I",
		plots : {
			"scattering_I" : true,
			"subtracted_I" : true,
			"buffer_I" : true
		}
	});

}

HPLCTabs.prototype._selectFrame = function(frameNumber) {
	try{
		this._renderScatteringCurve(frameNumber);
		this.frameGrid.refresh([this.mainPlotPanel.getDataByFrameNumber(frameNumber)], this.experiment.experimentId);
	}
	catch(e){
		console.log(e);
	}
};

HPLCTabs.prototype.error = function(error) {
	var e = JSON.parse(error);
	showError(e);
	this.panel.setLoading(false);
};

HPLCTabs.prototype.loadDataMainPlot = function(data) {
	var zeroArray = [];
	for ( var i = 0; i < data.I0.length; i++) {
		zeroArray.push(0);
	}
	data = [ {
		param : "I0",
		data : data.I0,
		std : data.I0_Stdev,
		color : '#0066CC',
		label : "I0"
	}, 
	{
		param : "sum_I",
		label : "sum_I",
		color : "#00FF00",
		data : data.sum_I,
		std : zeroArray
	},
	{
		param : "Rg",
		label : "Rg",
		color : "#21610B",
		data : data.Rg,
		std : data.Rg_Stdev
	}, {
		param : "Mass",
		data : data.mass,
		std : data.mass_Stdev,
		color : '#FF9900',
		label : "Mass"
	}, {
		param : "Vc",
		data : data.Vc,
		std : data.Vc_Stdev,
		color : '#990099',
		label : "Vc"
	}, {
		param : "Qr",
		data : data.Qr,
		std : data.Qr_Stdev,
		color : '#FF0066',
		label : "Qr"
	}, {
		param : "quality",
		label : "quality",
		color : "#FF00FF",
		data : data.quality,
		std : zeroArray
	} ];
	this.data = data;
	this.mainPlotPanel.loadData(data);
};

HPLCTabs.prototype._loadIntensityPlotByFrameNumber = function(frameNumber) {
	var _this = this;
	
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, data) {
		var array = [];
		data = [ {
			param : "q",
			data : data[frameNumber].q,
			std : data[frameNumber].q,
			fstd : function(a) {
				return parseFloat(a);
			},
			color : '#green',
			label : "q",
			showOnMenu : false
		}, 
		{
			param : "scattering_I",
			data : data[frameNumber].scattering_I,
			fdata : function(a) {
				return Math.log(parseFloat(a));
			},
			std : data[frameNumber].scattering_Stdev,
			fstd : function(a) {
				return Math.log(Math.abs(parseFloat(a)));
			},
			color : 'green',
			label : "Log(I) Sample"
		}, 
		{
			param : "buffer_I",
			data : data[frameNumber].buffer_I,
			fdata : function(a) {
				return Math.log(parseFloat(a));
			},
			fstd : function(a) {
				return Math.log(Math.abs(parseFloat(a)));
			},
			std : data[frameNumber].subtracted_Stdev,
			color : '#0000FF',
			label : "Log(I) Avg Buf."
		}, 
		{
			param : "subtracted_I",
			data : data[frameNumber].subtracted_I,
			fdata : function(a) {
				var value = (Math.log(parseFloat(a)));
				if (isNumber(value))
					return value;
			},
			fstd : function(a) {
				var value =  Math.log(Math.abs(parseFloat(a)));
				if (isNumber(value))
					return value;
			},
			std : data[frameNumber].subtracted_Stdev,
			color : 'red',
			label : "Log(I) Sub."
		}
		];

		_this.intensityPlotPanel.xlabel = "Frame " + frameNumber + " (q, nm-1)";

		if (_this.intensityPlotPanel.hplcData == null) {
			/** It creates also top bar **/
			_this.intensityPlotPanel.loadData(data);
		} else {
			_this.intensityPlotPanel.reloadData(data);
			
		}
		_this.intensityPlotPanel.panel.setLoading(false);
	});
	adapter.onError.attach(function(sender, data) {
		_this.intensityPlotPanel.panel.setLoading(false);
	});
	this.intensityPlotPanel.panel.setLoading("Retrieving Frame " + frameNumber);
	adapter.getH5FrameScattering(this.experiment.json.experimentId, frameNumber);
};

HPLCTabs.prototype._renderScatteringCurve = function(frameNumber) {
	var _this = this;
	
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, data) {
		_this.intensityPlotPanel.setPeaks(data);
		_this._loadIntensityPlotByFrameNumber(frameNumber);
		try{
			var peaks = [];
			for(key in data){
				peaks.push({
					start	: key.split("-")[0],
					end	: key.split("-")[1],
					experimentId	: _this.experiment.json.experimentId
				});
			}
			_this.peakGrid2.refresh(JSON.parse(JSON.stringify(peaks)));
			_this.peakGrid.refresh(peaks);
//			console.log(peaks);
		}
		catch(e){
			showError(e);
		}
	});
	adapter.onError.attach(function(sender, data) {
		_this.intensityPlotPanel.setLoading(false);
	});
	this.intensityPlotPanel.panel.setLoading("Reading HDF5 File ");
	adapter.getH5FramesMerge(this.experiment.json.experimentId);
};

HPLCTabs.prototype._postRenderOverviewPanel = function() {
	var _this = this;
	var adapter = new BiosaxsDataAdapter();
	adapter.onError.attach(function(sender, data) {
		data = data.replace(/NaN/g, '0');
		_this.loadDataMainPlot(JSON.parse(data));
		

	});
	adapter.onSuccess.attach(function(sender, data) {
		_this.loadDataMainPlot(data);
		_this._renderScatteringCurve(0);
	});
	adapter.getH5fileParameters(this.experiment.json.experimentId, [
		"I0", "I0_Stdev", "sum_I","Rg", "Rg_Stdev", "Vc", "Vc_Stdev", "Qr", "Qr_Stdev", "mass", "mass_Stdev", "quality" ]);

};

HPLCTabs.prototype._postRenderDetailsPanel = function() {
	if (this.data != null) {
		this.I0PlotPanel.loadData(this.data);
		this.RgPlotPanel.loadData(this.data);
		this.MassPlotPanel.loadData(this.data);
		this.VcPlotPanel.loadData(this.data);
	}

};

HPLCTabs.prototype.draw = function(experiment) {
	var _this = this;
	this.renderDataAcquisition(experiment);
};

HPLCTabs.prototype.getExperimentTitle = function() {
	var experimentHeaderForm = new ExperimentHeaderForm();
	return experimentHeaderForm.getPanel(this.experiment);
};

HPLCTabs.prototype.renderDataAcquisition = function(experiment) {
	var _this = this;
	this.experiment = experiment;
	if (this.experimentPanel == null) {
		this.experimentPanel = Ext.create('Ext.container.Container', {
			bodyPadding : 2,
			height : this.height,
			width : Ext.getBody().getViewSize().width * 0.9,
			renderTo : this.targetId,
			style : {
				padding : 2
			},
			items : [ this.getExperimentTitle(), this.getPanel() ],
			listeners : {
				afterrender : function() {
					var adapter = new BiosaxsDataAdapter();
					adapter.onSuccess.attach(function(sender, data) {
						_this.analysisGrid.refresh(data);
					});
					adapter.getAnalysisInformationByExperimentId(_this.experiment.experimentId);
				}
			}
		});
	}
	return this.experimentPanel;
};

HPLCTabs.prototype.getPanel = function() {
	var _this = this;
	this.panel = Ext.createWidget('tabpanel',
			{
				plain : true,
				height : this.height,
//				width : 900,
				style : {
					padding : 2
				},
				items : [
					{
						tabConfig : {
							title : "Overview"

						},
						items : [ {
							xtype : 'container',
							layout : 'hbox',
							border : 1,
							flex : 1,
							items : [ {
								xtype : 'container',
								layout : 'vbox',
								border : 1,
								items : [ {
									xtype : 'container',
									margin : '10px',
									border : 1,
									items : [ 
											{
												xtype : 'container',
												margin : '0px',
												layout : 'hbox',
												border : 1,
												items : [ 
												         this.peakGrid2.getPanel([]),
												         this.mainPlotPanel.getPanel()
												         ]
											 },
									          {
													xtype : 'container',
													layout : 'vbox',
													border : 1,
													items : [
														         {
														        	 html: '<div style="font-weight:bold;width:600px;border-bottom:1px solid #000000;">Select a frame by double-clicking on the HPLC Frames plot</div>',
														        	 margin: 5,
														        	 border : 0
														         },
														          this.frameGrid.getPanel([]), 
														          this.intensityPlotPanel.getPanel() 
													         ]
									          }
									         
									          ]
								}],
								listeners : {
									afterrender : function() {
										_this._postRenderOverviewPanel();
									}
								}
							} ]
						} ]
					},
//					{
//						tabConfig : {
//							title : "Details"
//						},
//						items : [ {
//							xtype : 'container',
//							layout : 'hbox',
//							border : 1,
//							flex : 1,
//							items : [ {
//								xtype : 'container',
//								layout : 'vbox',
//								border : 1,
//								items : [ {
//									xtype : 'container',
//									padding : '1px',
//									items : [
//										this.I0PlotPanel.getPanel(), this.RgPlotPanel.getPanel(), this.MassPlotPanel.getPanel(),
//										this.VcPlotPanel.getPanel() ]
//								} ],
//								listeners : {
//									afterrender : function() {
//										_this._postRenderDetailsPanel();
//									}
//								}
//							} ]
//						} ]
//					}, 
					{
						tabConfig : {
							title : "Analysis"
						},
						items : [ {
							xtype : 'container',
							layout : 'vbox',
							padding : '5px 0px 10px 10px',
							items : [ _this.analysisGrid.getPanel([]) ]
						} ]
					},
					{
						tabConfig : {
							title : "File Manager"
						},
						items : [ {
										xtype : 'container',
										layout : 'vbox',
										padding : '5px 0px 10px 10px',
										items : [
										         {
										        	 html: '<div style="font-weight:bold;width:900px;border-bottom:1px solid #000000;">Custom Download</div>',
										        	 margin : '10 0 10 5',
										        	 border : 0
										         },
										         {
										        	 html: '<div style="font-weight:bold;width:900px;border:1px solid red;color:red">Download has been temporary disabled. Contact your labcontact if you want to extract the frames from the HDF5 file</div>',
										        	 margin : '10 0 10 5',
										        	 hidden : _this.experiment.experimentId == 2602,
										        	 border : 0
										         },
										         {
														xtype : 'container',
														layout : 'hbox',
														margin : '0 0 0 20',
														flex : 1,
														items :[
																	{
															            xtype: 'numberfield',
															            id: 'field_start',
															            fieldLabel: 'Frames from',
															            value: 0,
															            minValue: 0
															        },
															        {
															            xtype: 'numberfield',
															            id: 'field_end',
															            fieldLabel: 'to',
															            labelWidth : 20,
															            margin : '0 0 0 10',
															            minValue: 0
															        },
															        {
															            xtype: 'button',
															            /** allowing test account to download frames **/
															            disabled : _this.experiment.experimentId != 2602,
															            text : 'Download',
															            margin : '0 0 0 10',
															            handler : function() {
															            	var experimentId = _this.experiment.experimentId;
															            	var start = Ext.getCmp("field_start").getValue();
															            	var end = Ext.getCmp("field_end").getValue();
															            	/** Checking if start is bigger than end **/
															            	if (start > end){
															            		var aux = end;
															            		end = start;
															            		start = aux;
															            	}
															            	var url = BUI.getZipFrameHPLCUrl(experimentId, start, end);
															            	window.open(url)
															            }
															        }
																] 
													},
											         _this.peakGrid.getPanel([])
											        
								         ]
									}
						]
					} 
					]
			});

	return this.panel;
};
