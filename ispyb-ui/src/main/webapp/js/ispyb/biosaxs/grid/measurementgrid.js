/**
 * Shows measurements with their attributes as specimen, exposure temperature,
 * volume to load, transmission etc...
 * 
 * @addBtnMultipleEdit: if true add a button for changing measurements'
 *                      parameters by choosing multiple measurements. It opens
 *                      <span
 *                      style="color:blue;">MultipleEditMeasurementGridWindow</span>
 * @collapsed: if true it doesn't show buffer's measurements
 * @editor: hashmap containing columns to be edited. It is an array of a json
 *          like editor of Ext
 * @selModel
 * @collapseBtnEnable
 * @removeBtnEnabled Ext.selection.RowModel
 * @addBtnEnable
 * @updateRowEnabled true/false if update plugin is set to the grid
 * @showTitle
 * @title
 * @isStatusColumnHidden
 * @isPriorityColumnHidden
 * @isTimeColumnHidden
 * @estimateTime
 * @margin
 * @tbar
 * @height
 * @maxHeight
 * @minHeight
 * @width
 * @maxWidth
 * @resizable
 * @experimentColorBased when colors for buffers and macromolecules are not
 *                       selected by the proposal but by experiment, so the
 *                       number of colors is smaller
 * 
 * #onClick #onSelected #onRemoved #onUpdateTime #onMeasurementChanged
 * #onExperimentChanged
 */
function MeasurementGrid(args) {
	this.id = BUI.id();

	this.height = 500;
	this.width = 900;

	this.maxWidth = 1200;
	this.maxHeight = 600;
	this.minHeight = 500;

	this.unitsFontSize = 9;
	this.title = "Measurements";
	this.estimateTime = false;
	this.collapsed = true;
	this.tbar = true;

	this.showTitle = true;
	this.resizable = true;
	this.updateRowEnabled = true;

	/**
	 * Hash map containing the keys of the editable columns. Ex:
	 * 'exposureTemperature' *
	 */
	this.editor = {
		comments : {
			xtype : 'textfield',
			allowBlank : true
		}
	};

	this.isTimeColumnHidden = false;
	this.isStatusColumnHidden = false;
	this.isPriorityColumnHidden = true;
	this.margin = "0 0 0 0";

	this.addBtnEnable = true;
	this.sorter = [ {
		property : 'priority',
		direction : 'ASC'
	} ];

	this.removeBtnEnabled = false;
	this.collapseBtnEnable = true;
	this.addBtnMultipleEdit = false;
	this.sortingBtnEnable = false;

	var _this = this;
	this.selModel = Ext.create('Ext.selection.RowModel', {
		allowDeselect : true,
		mode : 'MULTI',
		listeners : {
			selectionchange : function(sm, selections) {
				var selected = [];
				for (var i = 0; i < selections.length; i++) {
					selected.push(selections[i].raw);
				}
				_this.onSelected.notify(selected);
			}
		}
	});

	if (args != null) {

		if (args.selModel != null) {
			this.selModel = args.selModel;
		}

		if (args.removeBtnEnabled != null) {
			this.removeBtnEnabled = args.removeBtnEnabled;
		}

		if (args.addBtnMultipleEdit != null) {
			this.addBtnMultipleEdit = args.addBtnMultipleEdit;
		}

		// if (args.experimentColorBased != null) {
		// this.experimentColorBased = args.experimentColorBased;
		// }

		if (args.collapsed != null) {
			this.collapsed = args.collapsed;
		}
		if (args.resizable != null) {
			this.resizable = args.resizable;
		}

		if (args.editor != null) {
			this.editor = args.editor;
		}

		if (args.collapseBtnEnable != null) {
			this.collapseBtnEnable = args.collapseBtnEnable;
		}

		if (args.addBtnEnable != null) {
			this.addBtnEnable = args.addBtnEnable;
		}
		if (args.sortingBtnEnable != null) {
			this.sortingBtnEnable = args.sortingBtnEnable;
		}

		if (args.isPriorityColumnHidden != null) {
			this.isPriorityColumnHidden = args.isPriorityColumnHidden;
		}

		if (args.width != null) {
			this.width = args.width;
		}

		if (args.updateRowEnabled != null) {
			this.updateRowEnabled = args.updateRowEnabled;
		}

		if (args.showTitle != null) {
			this.showTitle = args.showTitle;
			if (this.showTitle == false) {
				this.title = null;
			}
		}

		if (args.height != null) {
			this.height = args.height;
		}

		if (args.maxHeight != null) {
			this.maxHeight = args.maxHeight;
		}

		if (args.minHeight != null) {
			this.minHeight = args.minHeight;
		}

		if (args.maxWidth != null) {
			this.maxWidth = args.maxWidth;
		}

		if (args.isStatusColumnHidden != null) {
			this.isStatusColumnHidden = args.isStatusColumnHidden;
		}
		if (args.isTimeColumnHidden != null) {
			this.isTimeColumnHidden = args.isTimeColumnHidden;
		}

		if (args.title != null) {
			this.title = args.title;
		}
		if (args.estimateTime != null) {
			this.estimateTime = args.estimateTime;
		}

		if (args.margin != null) {
			this.margin = args.margin;
		}

		if (args.tbar != null) {
			this.tbar = args.tbar;
		}

		if (args.sorter != null) {
			this.sorter = args.sorter;
		}

	}
	this.onClick = new Event(this);
	this.onSelected = new Event(this);
	this.onRemoved = new Event(this);
	this.onUpdateTime = new Event(this);
	this.onMeasurementChanged = new Event(this);
	this.onExperimentChanged = new Event(this);
}

MeasurementGrid.prototype._sortBy = function(sort) {
	var _this = this;
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, data) {
		_this.onExperimentChanged.notify(data);
		_this.grid.setLoading(false);
		// wizardWidget.window.close();
	});
	adapter.onError.attach(function(sender, data) {
		_this.grid.setLoading(false);
		alert("Oops, there was a problem");
	});
	_this.grid.setLoading("Sorting");
	adapter.sortMeasurements(this.experiments.experiments[0].experimentId, sort);
};

MeasurementGrid.prototype._getMenu = function() {
	var _this = this;
	if (this.tbar) {

		var items = [];
		if (_this.addBtnEnable) {
			items.push({
				icon : '../images/add.png',
				text : 'Add measurements',
				handler : function() {
					_this._openAddMeasurementWindow();
				}
			});
		}

		if (_this.addBtnMultipleEdit) {
			items.push({
				icon : '../images/Edit_16x16_01.png',
				text : 'Multiple Edit',
				handler : function() {
					var multipleEditMeasurementGridWindow = new MultipleEditMeasurementGridWindow();
					multipleEditMeasurementGridWindow.onExperimentChanged.attach(function(sender, data) {
						_this.onExperimentChanged.notify(data);
					});

					multipleEditMeasurementGridWindow.draw(_this.measurements, _this.experiments);

				}
			});
		}

		items.push("->");

		if (_this.sortingBtnEnable) {
			var split = Ext.create('Ext.button.Split', {
				text : 'Sort by',
				// handle a click on the button itself
				handler : function() {
					// alert("The button was clicked");
				},
				menu : new Ext.menu.Menu({
					items : [
					{
						text : 'First Created First Measured',
						handler : function() {
							_this._sortBy("FIFO");
						}
					}, "-", {
						text : 'Default',
						handler : function() {
							_this._sortBy("DEFAULT");
						}
					} ]
				})
			});
			items.push(split);
		}

		if (_this.collapseBtnEnable) {
			items.push({
				text : 'Collapse buffers',
				enableToggle : true,
				scope : this,
				toggleHandler : function(item, pressed) {
					this.collapsed = pressed;
					this.grid.getStore().loadData(this._prepareData(this.measurements, this.experiments), false);
				},
				pressed : this.collapsed
			});
		}
		var tb = Ext.create('Ext.toolbar.Toolbar', {
			items : items
		});
		return tb;
	}
	return null;
};

/** Opens WizardWidget for adding new measurements * */
MeasurementGrid.prototype._openAddMeasurementWindow = function(measurements, experiments) {
	var _this = this;
	var wizardWidget = new WizardWidget();
	wizardWidget.onFinished.attach(function(sender, result) {
		var adapter = new BiosaxsDataAdapter();
		adapter.onSuccess.attach(function(sender, data) {
			_this.onExperimentChanged.notify(data);
			_this.grid.setLoading(false);
			wizardWidget.window.close();
		});
		wizardWidget.current.setLoading("ISPyB: Adding measurements");
		adapter.addMeasurements(result.name, "comments", result.data, _this.experiments.experiments[0].experimentId);
	});

	wizardWidget.draw(null, new MeasurementCreatorStepWizardForm(BIOSAXS.proposal.getMacromolecules(), {
		noNext : true
	}));
};

/*******************************************************************************
 * Opens WizardWidget for adding new measurements
 * 
 * @Measurements
 * @Experiments experimentList Object
 ******************************************************************************/
MeasurementGrid.prototype._prepareData = function(measurements, experiments) {
	var data = [];
	for (var i = 0; i < measurements.length; i++) {
		var measurement = measurements[i];
		var specimen = experiments.getSampleById(measurement.specimenId);
		var buffer = experiments.getBufferById(specimen.bufferId);
		measurement.buffer_acronym = buffer.acronym;
		measurement.bufferId = buffer.bufferId;
		measurement.volume = specimen.volume;
		if (specimen.macromolecule3VO != null) {
			measurement.acronym = specimen.macromolecule3VO.acronym;
			measurement.macromoleculeId = specimen.macromolecule3VO.macromoleculeId;
		}
		measurement.concentration = specimen.concentration;
		if (measurement.run3VO != null) {
			measurement.energy = measurement.run3VO.energy;
			measurement.expExposureTemperature = measurement.run3VO.exposureTemperature;
			measurement.storageTemperature = measurement.run3VO.storageTemperature;
			measurement.timePerFrame = measurement.run3VO.timePerFrame;
			measurement.radiationAbsolute = measurement.run3VO.radiationAbsolute;
			measurement.radiationRelative = measurement.run3VO.radiationRelative;
			measurement.status = "DONE";

			try {
				if (measurement.run3VO.timeStart != null) {
					if (measurement.run3VO.timeStart != "") {
						measurement.miliseconds = moment(measurement.run3VO.timeStart).format("X");
					}
				}
			} catch (E) {
				console.log(E);
			}
		}

		if (experiments.getDataCollectionByMeasurementId(measurement.measurementId).length > 0) {
			var measurementtodatacollection3VOs = experiments.getDataCollectionByMeasurementId(measurement.measurementId)[0].measurementtodatacollection3VOs;
			for (var k = 0; k < measurementtodatacollection3VOs.length; k++) {
				if (measurementtodatacollection3VOs[k].dataCollectionOrder == 1) {
					var specimenBuffer = experiments.getSampleById(experiments.getMeasurementById(measurementtodatacollection3VOs[k].measurementId).specimenId);
					if (specimenBuffer.sampleplateposition3VO != null) {
						measurement.bufferSampleplateposition3VO = specimenBuffer.sampleplateposition3VO;
						measurement.bufferSampleplate = (experiments.getSamplePlateById(specimenBuffer.sampleplateposition3VO.samplePlateId));
					}
				}
			}
		}

		if (this.collapsed) {
			/** If collapsed only the samples * */
			if (specimen.macromolecule3VO != null) {
				data.push(measurement);
			}
		} else {
			data.push(measurement);
		}

	}
	return data;
};

/**
 * Refresh data grid with the measurements and the experiments
 * 
 * @measurements array with measurement3VO objects
 * @experiments array with experiments objects
 */
MeasurementGrid.prototype.refresh = function(measurements, experiments) {
	this.experiments = experiments;
	this.measurements = measurements;
	this.store.loadData(this._prepareData(measurements, experiments), false);
};

/**
 * Set status bar to busy (refreshing icon)
 * 
 * @msg message to be displayed on the bar
 */
MeasurementGrid.prototype._showStatusBarBusy = function(msg) {
	var statusBar = Ext.getCmp(this.id + 'basic-statusbar');
	statusBar.setStatus({
		text : msg,
		iconCls : 'x-status-busy',
		clear : false
	});
};

/**
 * Set status bar to ready (ok icon)
 * 
 * @msg message to be displayed on the bar
 */
MeasurementGrid.prototype._showStatusBarReady = function(msg) {
	var statusBar = Ext.getCmp(this.id + 'basic-statusbar');
	statusBar.setStatus({
		text : msg,
		iconCls : 'x-status-valid',
		clear : false
	});
};

/**
 * If updateRowEnabled returns an array with Ext.grid.plugin.RowEditing
 */
MeasurementGrid.prototype._getPlugins = function() {
	var _this = this;
	var plugins = [];
	if (this.updateRowEnabled) {
		plugins.push(Ext.create('Ext.grid.plugin.RowEditing', {
			clicksToEdit : 1,
			listeners : {
				validateedit : function(grid, e) {
					/** Setting values * */
					for ( var key in _this.editor) {
						e.record.raw[key] = e.newValues[key];
					}
					/** Comments are always updatable* */
					e.record.raw.comments = e.newValues.comments;

					var adapter = new BiosaxsDataAdapter();
					adapter.onSuccess.attach(function(sender, measurement) {
						_this.onMeasurementChanged.notify(measurement);
						_this.grid.setLoading(false);
					});
					adapter.onError.attach(function() {
						alert("Error");
						_this.grid.setLoading(false);
					});

					_this.grid.setLoading();
					adapter.saveMeasurement(e.record.raw, _this.experiments.experiments[0]);
				}
			}
		}));
	}
	return plugins;
};

/**
 * @key name of the columns mathing the this.editor[key]
 */
MeasurementGrid.prototype._getEditor = function(key) {
	if (this.editor[key] != null) {
		return this.editor[key];
	}
	return null;
};

MeasurementGrid.prototype.getPanel = function(measurements, experiments) {
	this.experiments = experiments;
	this.measurements = measurements;
	var _this = this;
	this.store = Ext.create('Ext.data.Store', {
		fields : [ {
			name : 'miliseconds',
			type : 'int'
		}, 'priority', 'bufferId', {
			name : 'exposureTemperature',
			type : 'numeric'
		}, 'volumeToLoad','code', 'transmission', 'viscosity', 'waitTime', 'flow', 'buffer_acronym', 'macromoleculeId', 'acronym', 'concentration', 'extraFlowTime', 'volume', 'energy',
				'expExposureTemperature', 'storageTemperature', 'timePerFrame', 'radiationAbsolute', 'radiationRelative', 'status', 'comments' ],
		data : this._prepareData(measurements, experiments)
	});
	this.store.sort(this.sorter);
	var bbar = {};
	try {
		bbar = Ext.create('Ext.ux.StatusBar', {
			id : _this.id + 'basic-statusbar',
			defaultText : 'Ready',
			text : 'Ready',
			iconCls : 'x-status-valid',
			items : []
		});
	} catch (exp) {
		console.log("bbar error");
	}

	this.grid = Ext
			.create(
					'Ext.grid.Panel',
					{
						id : this.id,
						title : this.title,
						store : this.store,
						selModel : this.selModel,
						plugins : this._getPlugins(),
						resizable : this.resizable,
						margin : this.margin,
						maxHeight : this.maxHeight,
						minHeight : this.minHeight,
						maxWidth : this.maxWidth,
						width : this.width,
						tbar : this._getMenu(),
						columns : [
								{
									text : 'Order',
									dataIndex : 'priority',
									width : 50,
									hidden : _this.isPriorityColumnHidden,
									sortable : true

								},
								{
									text : 'Run Number',
									dataIndex : 'code',
									width : 50,
									hidden : true,
									sortable : true

								},

								{
									text : 'Specimen',
									columns : [

											{
												text : '',
												dataIndex : 'macromoleculeId',
												width : 30,
												renderer : function(val, y, sample) {
													if (val != null) {
														if (_this.experiments == null) {
															return BUI.getRectangleColorDIV(BIOSAXS.proposal.macromoleculeColors[val], 10, 10);
														} else {
															return BUI.getRectangleColorDIV(_this.experiments.macromoleculeColors[val], 10, 10);
														}
													}
												},
												sortable : true
											},
											{
												text : 'Macromo.',
												dataIndex : 'acronym',
												width : 80,
												renderer : function(val, y, sample) {
													return val;
												},
												sortable : true
											},
											{
												text : 'Conc. ',
												dataIndex : 'concentration',
												width : 80,
												renderer : function(val, y, sample) {
													if (sample.raw.macromoleculeId == null) {
														return "";
													}
													if (isNaN(val))
														return val;

													if (val != 0) {
														return BUI.formatValuesUnits(val, '', {
															fontSize : 16,
															decimals : 3,
															unitsFontSize : this.unitsFontSize
														});
													} else {
														return;
													}

												},
												sortable : true
											},
											{
												text : '',
												dataIndex : 'bufferId',
												width : 30,
												hidden : false,
												renderer : function(val, y, sample) {
													if (val != null) {
														var color = '#FFCCFF';
														if (_this.experiments != null) {
															var dc = _this.experiments.getDataCollectionByMeasurementId(sample.raw.measurementId);
															if (dc != null) {
																if (dc.length > 0) {
																	color = _this.experiments.getSpecimenColorByBufferId(_this.experiments
																			.getMeasurementById(dc[0].measurementtodatacollection3VOs[0].measurementId).specimenId);
																}
															}
														} else {
															color = BIOSAXS.proposal.bufferColors[val];
														}
														return BUI.getRectangleColorDIV(color, 10, 10);
													}
												},
												sortable : true
											},
											{
												text : 'Buffer',
												dataIndex : 'buffer_acronym',
												width : 120,
												renderer : function(val, y, sample) {
													if (sample.raw.bufferSampleplateposition3VO != null) {
														return BIOSAXS.proposal.getBufferById(sample.raw.bufferId).acronym + "<span style='font-style:oblique;'> Plate: ["
																+ sample.raw.bufferSampleplate.slotPositionColumn + ", "
																+ BUI.getSamplePlateLetters()[sample.raw.bufferSampleplateposition3VO.rowNumber - 1] + "-"
																+ sample.raw.bufferSampleplateposition3VO.columnNumber + "]</span>";
													}
													return val;
												},
												sortable : true
											}, {
												text : 'Position',
												width : 100,
												hidden : true,
												renderer : function(val, y, sample) {
													if (_this.experiments != null) {
														return BUI.getSamplePositionHTML(_this.experiments.getSampleById(sample.raw.specimenId), _this.experiments.experiments[0]);
													}
												}
											} ]
								},
								{
									text : 'Parameters',
									columns : [
											{
												text : 'Ex. Flow. time (s)',
												dataIndex : 'extraFlowTime',
												width : 100,
												hidden : true,
												renderer : function(val, y, sample) {
													return BUI.formatValuesUnits(val, 's', {
														fontSize : 12,
														decimals : 0,
														unitsFontSize : this.unitsFontSize
													});
												}
											},
											{
												text : 'Exp. Temp.',
												dataIndex : 'exposureTemperature',
												width : 70,
												renderer : function(val, y, sample) {
													if (Number(val)) {
														return BUI.formatValuesUnits(val, 'C', {
															fontSize : 12,
															decimals : 2,
															unitsFontSize : this.unitsFontSize
														});
													}
													return null;
												},
												sortable : true,
												editor : this._getEditor("exposureTemperature")
											},
											{
												text : 'Vol. Load',
												dataIndex : 'volumeToLoad',
												width : 60,
												hidden : false,
												editor : this._getEditor("volumeToLoad"),
												renderer : function(val, y, sample) {
													// return val+ " &#181l";
													return BUI.formatValuesUnits(val, '&#181l', {
														fontSize : 12,
														decimals : 2,
														unitsFontSize : this.unitsFontSize
													});
												}
											},
											{
												text : 'Volume in Well',
												dataIndex : 'volume',
												hidden : true,
												editor : this._getEditor("volume"),
												width : 80,
												renderer : function(val, y, sample) {
													// return val + "(&#181l)";
													return BUI.formatValuesUnits(val, '&#181l', {
														fontSize : 12,
														decimals : 2,
														unitsFontSize : this.unitsFontSize
													});
												}
											},
											{
												text : 'Trans.',
												dataIndex : 'transmission',
												width : 60,
												editor : this._getEditor("transmission"),
												renderer : function(val, y, sample) {
													return BUI.formatValuesUnits(val, '%', {
														fontSize : 12,
														decimals : 0,
														unitsFontSize : this.unitsFontSize
													});
												}
											},
											{
												text : 'Wait T.',
												dataIndex : 'waitTime',
												editor : this._getEditor("waitTime"),
												width : 50,
												renderer : function(val, y, sample) {
													// if (val != 0) {
													return BUI.formatValuesUnits(val, 's', {
														fontSize : 12,
														decimals : 0,
														unitsFontSize : this.unitsFontSize
													});
													// }
												}
											},
											{
												text : 'Flow',
												dataIndex : 'flow',
												editor : this._getEditor("flow"),
												width : 50,
												renderer : function(val, y, sample) {
													if (val == true) {
														return "yes";
													}
													return null;
												}
											},
											{
												text : 'Viscosity',
												dataIndex : 'viscosity',
												tooltip : 'The viscosity of a fluid is a measure of its resistance to gradual deformation by shear stress or tensile stress. For liquids, it corresponds to the informal notion of "thickness"',
												editor : this._getEditor("viscosity"),
												width : 50,
												renderer : function(val, y, sample) {
													return val;
												}
											} ]
								}, {
									text : 'Status',
									dataIndex : 'status',
									width : 50,
									hidden : _this.isStatusColumnHidden,
									renderer : function(val, y, sample) {
										if (val != null) {
											if (val == 'DONE') {
												return "<span style='color:green; font-weight:bold;'>" + val + " </span>";
											}
										}
									}
								}, {
									text : 'Time',
									dataIndex : 'time',
									width : 80,
									hidden : _this.isTimeColumnHidden,
									renderer : function(val, y, sample) {
										if (sample.raw.run3VO != null) {
											if (sample.raw.run3VO.timeStart != null) {
												if (sample.raw.run3VO.timeStart != "") {
													var m = moment(sample.raw.run3VO.timeStart);
													return m.format("hh:mm:ss a");
												}
											}
										}
									}
								}, {
									text : 'Energy',
									dataIndex : 'energy',
									width : 100,
									hidden : true
								}, {
									text : 'Real Exp. Temp.(C)',
									width : 100,
									dataIndex : 'expExposureTemperature',
									hidden : true
								}, {
									text : 'Storage Temp.(C)',
									width : 100,
									dataIndex : 'storageTemperature',
									hidden : true
								}, {
									text : 'Time/Frame (s)',
									width : 100,
									dataIndex : 'timePerFrame',
									hidden : true
								}, {
									text : 'Radiation Relative',
									dataIndex : 'radiationRelative',
									width : 100,
									hidden : true
								}, {
									text : 'Radiation Absolute',
									dataIndex : 'radiationAbsolute',
									width : 100,
									hidden : true
								}, {
									text : 'Comments',
									dataIndex : 'comments',
									flex : 1,
									hidden : false,
									editor : this._getEditor("comments")

								}, {
									id : _this.id + 'buttonRemoveSample',
									text : '',
									hidden : !_this.removeBtnEnabled,
									width : 100,
									sortable : false,
									renderer : function(value, metaData, record, rowIndex, colIndex, store) {
										if (record.raw.macromoleculeId != null) {
											if (_this.removeBtnEnabled) {
												return BUI.getRedButton('REMOVE');
											}
										}
									}
								} ],
						bbar : bbar,
						viewConfig : {
							stripeRows : true,
							getRowClass : function(record, index, rowParams, store) {
								if (record.data.status == "DONE") {
									return 'green-row';
								}

							},
							listeners : {
								'itemclick' : function(grid, record, item, index, e, eOpts) {
									_this.onClick.notify({
										specimen : record.raw
									});
								},
								'cellclick' : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
									if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonRemoveSample') {
										grid.getStore().removeAt(rowIndex);

										if (record.raw.measurementId != null) {
											/** For testing * */
											grid.setLoading("ISPyB: Removing measurement");
											var adapter = new BiosaxsDataAdapter();
											adapter.onSuccess.attach(function(sender, data) {
												grid.setLoading(false);
												/**
												 * We get and refresh experiment
												 * because specimens has changed *
												 */
												var adapter2 = new BiosaxsDataAdapter();
												adapter2.onSuccess.attach(function(sender, experiment) {
													_this.onRemoved.notify(experiment);
													_this._showStatusBarReady('Ready');
												});
												if (_this.experiments.experiments[0].experimentId != null) {
													adapter2.getExperimentById(_this.experiments.experiments[0].experimentId, "MEDIUM");
													_this._showStatusBarBusy("ISPyB: Removing Unused Specimens");
												}
											});

											adapter.onError.attach(function(sender, data) {
												alert("Error: " + data);
												grid.setLoading(false);
											});

											adapter.removeMeasurement(record.raw);
										}
									}

								}

							}
						}
					});

	this.grid.on("afterrender", function() {

		function updateTime() {
			try {
				_this.estimatedTime = _this.estimatedTime - 1;

				_this.onUpdateTime.notify({
					hours : (Number(_this.estimatedTime / 3600).toFixed()),
					minutes : (Number((_this.estimatedTime / 60) % 60).toFixed()),
					seconds : (Number(_this.estimatedTime % 60).toFixed())

				});

				if (Number(_this.estimatedTime) < 0) {
					_this.timer = null;
					grid.setTitle(_this.title);
				}

			} catch (e) {
				console.log(e);
				_this.timer = null;
			}
		}

		if (_this.estimateTime) {
			var experimentList = _this.experiments;
			var collected = experimentList.getMeasurementsCollected();
			if (collected.length > 0) {
				if (collected[0].run3VO != null) {
					try {
						var end = collected[0].run3VO.timeEnd;
						var start = collected[0].run3VO.timeStart;
						var dstart = moment(start);
						var dend = moment(end);
						var seconds = Number(dend.diff(dstart) / 1000).toFixed();

						_this.estimatedTime = (seconds * experimentList.getMeasurementsNotCollected().length);

						if (_this.estimatedTime > 0) {
							updateTime();
							_this.timer = setInterval(function() {
								updateTime();
							}, 1000);
						}

					} catch (e) {
					}
				}
			}
		}
	});

	return this.grid;
};

/** Method for testing * */
MeasurementGrid.prototype.input = function() {
	var experiment = DATADOC.getExperiment_10();
	var measurements = DATADOC.getMeasurements_10();
	var proposal = DATADOC.getProposal_10();
	return {
		experiment : experiment,
		measurements : measurements,
		proposal : proposal
	};
};

MeasurementGrid.prototype.test = function(targetId) {
	var measurementGrid = new MeasurementGrid({
		tbar : true

	});
	BIOSAXS.proposal = new Proposal(measurementGrid.input().proposal);
	var panel = measurementGrid.getPanel(measurementGrid.input().measurements, new ExperimentList([ new Experiment(measurementGrid.input().experiment) ]));
	panel.render(targetId);
};
