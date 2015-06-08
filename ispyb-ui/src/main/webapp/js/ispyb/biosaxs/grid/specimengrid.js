function SpecimenGrid(args) {
	this.id = BUI.id();
	this.height = 500;
	this.unitsFontSize = 9;
	this.editEnabled = false;
	this.isPositionColumnHidden = false;
	this.removeBtnEnabled = false;

	this.selectionMode = "MULTI";
	this.updateRowEnabled = false;
	this.grouped = true;
	this.width = 900;
	this.title = 'Specimens';
	
	this.margin = "0 0 0 0";
//	this.experimentColorBased = false;

	if (args != null) {
		if (args.height != null) {
			this.height = args.height;
		}

		if (args.showTitle == false) {
			this.title = null;
		}
		
		if (args.margin == false) {
			this.margin = args.margin;
		}

		if (args.grouped == false) {
			this.grouped = null;
		}

		if (args.width != null) {
			this.width = args.width;
		}


		if (args.editEnabled != null) {
			this.editEnabled = args.editEnabled;
		}
		if (args.removeBtnEnabled != null) {
			this.removeBtnEnabled = args.removeBtnEnabled;
		}
		if (args.isPositionColumnHidden != null) {
			this.isPositionColumnHidden = args.isPositionColumnHidden;
		}
		if (args.selectionMode != null) {
			this.selectionMode = args.selectionMode;
		}
		if (args.updateRowEnabled != null) {
			this.updateRowEnabled = args.updateRowEnabled;
		}

	}
	this.onClick = new Event(this);
	this.onSelected = new Event(this);
	this.onRemoved = new Event(this);
	this.onSpecimenChanged = new Event();
}

SpecimenGrid.prototype._prepareData = function(experiment) {
	var data = [];

	var samples = experiment.getSamples();
	for ( var i = 0; i < samples.length; i++) {
		var sample = samples[i];
		if (sample.macromolecule3VO != null) {
			sample.macromolecule = sample.macromolecule3VO.acronym;
			sample.exposureTemperature = [];
			sample.macromoleculeId = sample.macromolecule3VO.macromoleculeId;
		}

		if (sample.sampleplateposition3VO != null) {
			if (sample.sampleplateposition3VO.samplePlateId != null) {
				sample.samplePlateId = sample.sampleplateposition3VO.samplePlateId;
				sample.rowNumber = sample.sampleplateposition3VO.rowNumber;
				sample.columnNumber = sample.sampleplateposition3VO.columnNumber;
				if (experiment.getSamplePlateById(sample.sampleplateposition3VO.samplePlateId).plategroup3VO != null) {
					sample.plateGroupName = experiment.getSamplePlateById(sample.sampleplateposition3VO.samplePlateId).plategroup3VO.name;
					sample.samplePlateName = experiment.getSamplePlateById(sample.sampleplateposition3VO.samplePlateId).name + "  [" + sample.plateGroupName + "]";
					sample.slotPositionColumn = experiment.getSamplePlateById(sample.sampleplateposition3VO.samplePlateId).slotPositionColumn;
				}
			}
		} else {
			sample.samplePlateName = "Unallocated Specimens";
		}

		/** For grouping, because sencha has not option for multiple grouping I add a field to your store with a convert function that concatenates these two fields and then group by that field.**/
		sample.groupIndex = sample.bufferId + sample.macromoleculeId;
		var macromolecule = BIOSAXS.proposal.getMacromoleculeById(sample.macromoleculeId);

		sample.acronym = "Buffers";
		if (macromolecule != null) {
			sample.acronym = BIOSAXS.proposal.getMacromoleculeById(sample.macromoleculeId).acronym;
		}

		sample.buffer = experiment.getBufferById(sample.bufferId);

		sample.volumeToLoad = experiment.getVolumeToLoadBySampleId(sample.sampleId);
		data.push(sample);
	}
	return data;
};

SpecimenGrid.prototype.deselectAll = function() {
	this.grid.getSelectionModel().deselectAll();
};

SpecimenGrid.prototype.selectById = function(specimenId) {
	this.grid.getSelectionModel().deselectAll();
	for ( var i = 0; i < this.grid.getStore().data.items.length; i++) {
		var item = this.grid.getStore().data.items[i].raw;
		if (item.specimenId == specimenId) {
			this.grid.getSelectionModel().select(i);
		}
	}
};

SpecimenGrid.prototype.getStore = function() {
	return this.store;
};

SpecimenGrid.prototype.getPlugins = function() {
	var _this = this;

	var plugins = [];

	if (this.updateRowEnabled) {
		plugins.push(Ext.create('Ext.grid.plugin.RowEditing', {
			clicksToEdit : 1,
			listeners : {
				validateedit : function(grid, e) {
					var measurements = [];

					if (e.newValues.bufferId != e.record.raw.bufferId) {
						/** If buffer has changed we have to change all the specimens sharing same datacollection **/
						var dataCollections = [];
						if (e.record.raw.macromoleculeId == null) {
							dataCollections = dataCollections.concat(_this.experiment.getDataCollectionsBySpecimenId(e.record.raw.specimenId));
						} else {
							var sampleDataCollections = _this.experiment.getDataCollectionsBySpecimenId(e.record.raw.specimenId);
							for ( var i = 0; i < sampleDataCollections.length; i++) {
								var sampleDataCollection = sampleDataCollections[i];
								if (sampleDataCollection != null) {
									for ( var j = 0; j < sampleDataCollection.measurementtodatacollection3VOs.length; j++) {
										var measurementTODc = sampleDataCollection.measurementtodatacollection3VOs[j];
										if (measurementTODc.dataCollectionOrder == 1) {
											dataCollections = dataCollections.concat(_this.experiment.getDataCollectionsBySpecimenId(_this.experiment
													.getMeasurementById(measurementTODc.measurementId).specimenId));
										}
									}
								}
							}
						}
						var i = null;
						for ( i = 0; i < dataCollections.length; i++) {
							var dataCollection = dataCollections[i];
							var specimens = _this.experiment.getSpecimenByDataCollectionId(dataCollection.dataCollectionId);
							measurements = measurements.concat(specimens);
						}

						for ( i = 0; i < measurements.length; i++) {
							var measurement = measurements[i];
							var specimen = _this.experiment.getSpecimenById(measurement.specimenId);
							specimen.bufferId = e.newValues.bufferId;
							new BiosaxsDataAdapter().saveSpecimen(specimen, _this.experiment);
						}
					}

					/** Setting values **/
					e.record.raw.concentration = e.newValues.concentration;
					e.record.raw.volume = e.newValues.volume;

					/** Position **/
					if (e.record.raw.sampleplateposition3VO != null) {
						var samplePlate = _this.experiment.getSamplePlateBySlotPositionColumn(e.newValues.slotPositionColumn);
						if (samplePlate != null) {
							e.record.raw.sampleplateposition3VO = {
								columnNumber : e.newValues.columnNumber,
								rowNumber : e.newValues.rowNumber,
								samplePlateId : samplePlate.samplePlateId
							};
						}
					} else {
						if (e.newValues.slotPositionColumn != null) {
							var samplePlate = _this.experiment.getSamplePlateBySlotPositionColumn(e.newValues.slotPositionColumn);
							if (samplePlate != null) {
								e.record.raw.sampleplateposition3VO = {
									columnNumber : e.newValues.columnNumber,
									rowNumber : e.newValues.rowNumber,
									samplePlateId : samplePlate.samplePlateId
								};
							}
						}
					}

					var macromoleculeId = e.record.data.macromoleculeId;
					var adapter = new BiosaxsDataAdapter();
					adapter.onSuccess.attach(function(sender, specimen) {
						/** Because macromolecule3VO is fecthed LAZY **/
						if (macromoleculeId != null) {
							specimen.macromolecule3VO = BIOSAXS.proposal.getMacromoleculeById(macromoleculeId);
						}
						_this.onSpecimenChanged.notify(specimen);
						_this.grid.setLoading(false);
					});
					adapter.onError.attach(function() {
						alert("Error");
						_this.grid.setLoading(false);
					});
					_this.grid.setLoading();
					adapter.saveSpecimen(e.record.raw, _this.experiment);
				}
			}
		}));
	}
	return plugins;
};

SpecimenGrid.prototype._getRowCombo = function() {
	var data = [];
	for ( var i = 1; i <= 8; i++) {
		data.push({
			rowNumber : i,
			name : BUI.getSamplePlateLetters()[i - 1]
		});
	}

	var positionsStore = Ext.create('Ext.data.Store', {
		fields : [ 'rowNumber', 'name' ],
		data : data
	});

	return Ext.create('Ext.form.ComboBox', {
		store : positionsStore,
		queryMode : 'local',
		displayField : 'name',
		valueField : 'rowNumber'
	});
};

SpecimenGrid.prototype._getColumnCombo = function() {
	var data = [];
	for ( var i = 1; i <= 12; i++) {
		data.push({
			columnNumber : i
		});
	}

	var positionsStore = Ext.create('Ext.data.Store', {
		fields : [ 'columnNumber' ],
		data : data
	});

	return Ext.create('Ext.form.ComboBox', {
		store : positionsStore,
		queryMode : 'local',
		displayField : 'columnNumber',
		valueField : 'columnNumber'
	});
};

SpecimenGrid.prototype._getSlotColumBombo = function() {
	if (this.experiment){
		var length = this.experiment.getSamplePlates().length;
	
		var data = [];
		for ( var i = 1; i <= length; i++) {
			data.push({
				slotPositionColumn : i
			});
		}
	
		var positionsStore = Ext.create('Ext.data.Store', {
			fields : [ 'slotPositionColumn' ],
			data : data
		});
	
		return Ext.create('Ext.form.ComboBox', {
			store : positionsStore,
			queryMode : 'local',
			displayField : 'slotPositionColumn',
			valueField : 'slotPositionColumn'
		});
	}
};

SpecimenGrid.prototype.getPanelByExperiment = function(experiment) {
	this.experiment = experiment;
	var data = this._prepareData(experiment);
	return this.getPanel(data);
};

SpecimenGrid.prototype.refresh = function(experiment) {
	this.experiment = experiment;
	var data = this._prepareData(experiment);
	this.store.loadData(data);
};

SpecimenGrid.prototype.getPanel = function() {
	var _this = this;
	this.store = Ext.create('Ext.data.Store', {
		fields : [
			'buffer', 'bufferId', 'code', 'macromolecule', 'acronym', 'macromoleculeId', 'concentration', 'volume', 'samplePlateId',
			'slotPositionColumn', 'rowNumber', 'columnNumber', 'groupIndex' ],
		data : [],
		groupField : 'acronym'
	});
	this.store.sort([ {
		property : 'concentration',
		direction : 'ASC'
	}, {
		property : 'buffer',
		direction : 'ASC'
	} ]);

	var selModel = Ext.create('Ext.selection.RowModel', {
		allowDeselect : true,
		mode : this.selectionMode,
		listeners : {
			selectionchange : function(sm, selections) {
				var selected = [];
				for ( var i = 0; i < selections.length; i++) {
					selected.push(selections[i].raw);
				}
				_this.onSelected.notify(selected);
			}
		}
	});

	var features = [];

	if (this.grouped) {
		features.push({
			ftype : 'grouping',
			groupHeaderTpl : '{name}',
			hideGroupedHeader : false,
			startCollapsed : false,
			id : 'myGroupedStore'
		});
	}

	this.grid = Ext.create(
					'Ext.grid.Panel',
					{
						title 		: this.title,
						height 		: this.height,
						width 		: this.width,
						selModel 	: selModel,
						store 		: this.store,
						features 	: features,
						margin 		: this.margin,
						plugins	 : this.getPlugins(),
						columns : [
							{
								text : '',
								dataIndex : 'macromolecule',
								width : 20,
								renderer : function(val, y, sample) {
									var macromoleculeId = null;
									if (sample.raw.macromolecule3VO != null) {
										macromoleculeId = sample.raw.macromolecule3VO.macromoleculeId;
									}
									else{
										macromoleculeId = sample.raw.macromoleculeId;
									}
									
									if (macromoleculeId == null) return; 
									return BUI.getRectangleColorDIV(_this.experiment.macromoleculeColors[macromoleculeId], 10, 10);
								}
							},
							{
								text : 'Macromolecule',
								dataIndex : 'macromolecule',
								width : 100
							},
							{
								text : '',
								dataIndex : 'buffer',
								width : 20,
								renderer : function(val, y, sample) {
									var color = "black";
									if (sample.raw.bufferId != null) {
										if (_this.experiment.getDataCollectionsBySpecimenId(sample.raw.specimenId)[0] != null){
											color = _this.experiment.getSpecimenColorByBufferId(_this.experiment.getMeasurementById(_this.experiment.getDataCollectionsBySpecimenId(sample.raw.specimenId)[0].measurementtodatacollection3VOs[0].measurementId).specimenId);
										}
										return BUI.getRectangleColorDIV(color, 10, 10);
									}
								}
							}, {
								text : 'Buffer',
								dataIndex : 'bufferId',
								width : 140,
								renderer : function(val, y, sample) {
									if (sample.raw.bufferId != null) {
										return BIOSAXS.proposal.getBufferById(val).acronym;
									}
								},
								editor : BIOSAXS_COMBOMANAGER.getComboBuffers(BIOSAXS.proposal.getBuffers(), {
									noLabel : true,
									width : 300
								})
							}, {
								text : 'Conc.',
								dataIndex : 'concentration',
								width : 100,
								editor : {
									allowBlank : false
								},
								renderer : function(val, meta, sample) {
									if (isNaN(val)) {
										meta.tdCls = 'yellow-cell';
										return val;
									} else {
										if (val != 0) {
											return BUI.formatValuesUnits(val, 'mg/ml', {
												fontSize : 16,
												decimals : 3,
												unitsFontSize : this.unitsFontSize
											});
										} else {
											return;
										}
									}
								}
							}, {
								text : 'Vol. Well',
								dataIndex : 'volume',
								width : 70,
								editor : {
									allowBlank : true
								},
								renderer : function(val, y, sample) {
									return BUI.formatValuesUnits(sample.data.volume, '&#181l', {
										fontSize : 12,
										decimals : 2,
										unitsFontSize : this.unitsFontSize
									});
								}
							}, {
								text : 'Position',
								hidden : true,
								flex : 1,
								renderer : function(val, y, sample) {
									return BUI.getSamplePositionHTML(sample.raw, _this.experiment);
								}
							}, {
								text : 'samplePlateId',
								dataIndex : 'samplePlateId',
								hidden : true
							}, {
								text : 'Plate',
								hidden : this.isPositionColumnHidden,
								dataIndex : 'slotPositionColumn',
								editor : _this._getSlotColumBombo(),
								flex : 1,
								renderer : function(val, meta, sample) {
									if ((val != null) & (val != "")) {
										return val;
									} else {
										meta.tdCls = 'yellow-cell';
									}
								}
							}, {
								text : 'Row',
								hidden : this.isPositionColumnHidden,
								dataIndex : 'rowNumber',
								editor : this._getRowCombo(),
								flex : 1,
								renderer : function(val, meta, sample) {
									if ((val != null) && (val != "")) {
										return BUI.getSamplePlateLetters()[val - 1];
									} else {
										meta.tdCls = 'yellow-cell';
									}
								}
							}, {
								text : 'Well',
								hidden : this.isPositionColumnHidden,
								dataIndex : 'columnNumber',
								editor : this._getColumnCombo(),
								flex : 1,
								renderer : function(val, meta, sample) {
									if ((val != null) && (val != "")) {
										return val;
									} else {
										meta.tdCls = 'yellow-cell';
									}
								}
							}, {
								id : _this.id + 'buttonEditSample',
								text : 'Edit',
								width : 80,
								sortable : false,
								hidden : !_this.editEnabled,
								renderer : function(value, metaData, record, rowIndex, colIndex, store) {
									if (_this.editEnabled) {
										return BUI.getGreenButton('EDIT');
									}
								}
							}, {
								id : _this.id + 'buttonRemoveSample',
								text : '',
								hidden : !_this.removeBtnEnabled,
								width : 100,
								sortable : false,
								renderer : function(value, metaData, record, rowIndex, colIndex, store) {
									if (_this.removeBtnEnabled) {
										return BUI.getRedButton('REMOVE');
									}
								}
							}

						],
						viewConfig : {
							preserveScrollOnRefresh : true,
							stripeRows : true,
							getRowClass : function(record) {
								var specimens = _this.experiment.getSampleByPosition(record.data.samplePlateId, record.data.rowNumber,
										record.data.columnNumber);
								if (specimens.length > 1) {
									return 'red-row';

								}
							},
							listeners : {
								selectionchange : function(grid, selected) {
									_this.onClick.notify(record.raw);
								},
								cellclick : function(grid, td, cellIndex, record, tr) {
									if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonEditSample') {
									}
									if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'buttonRemoveSample') {
										grid.getStore().removeAt(rowIndex);
										_this.onRemoved.notify();
									}

								}

							}
						}
					});
	return this.grid;
};

SpecimenGrid.prototype.input = function() {
	return {
		experiment : DATADOC.getExperiment_10(),
		proposal : DATADOC.getProposal_10()
	};
};

SpecimenGrid.prototype.test = function(targetId) {
	var specimenGrid = new SpecimenGrid({
		height : 400,
		maxHeight : 400,
		width : 1000
	});
	BIOSAXS.proposal = new Proposal(specimenGrid.input().proposal);

	var experiment = new Experiment(specimenGrid.input().experiment);
	var panel = specimenGrid.getPanelByExperiment(experiment);
	panel.render(targetId);

};
