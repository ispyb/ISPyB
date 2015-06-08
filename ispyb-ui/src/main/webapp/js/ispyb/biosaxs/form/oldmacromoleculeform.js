///**
// * 
// * @witdh
// * @height
// */
//function MacromoleculeForm(args) {
//	this.id = BUI.id();
//	this.width = 700;
//	this.height = 500;
//
//	if (args != null) {
//		if (args.width != null) {
//			this.width = args.width;
//		}
//		if (args.height != null) {
//			this.height = args.height;
//		}
//	}
//
//	this.onClose = new Event(this);
//}
//
//MacromoleculeForm.prototype.getMacromolecule = function() {
//	this.macromolecule.name = Ext.getCmp(this.panel.getItemId()).getValues().name;
//	this.macromolecule.acronym = Ext.getCmp(this.panel.getItemId()).getValues().acronym;
//	this.macromolecule.comments = Ext.getCmp(this.panel.getItemId()).getValues().comments;
//	this.macromolecule.extintionCoefficient = Ext.getCmp(this.panel.getItemId()).getValues().extintionCoefficient;
//	this.macromolecule.molecularMass = Ext.getCmp(this.panel.getItemId()).getValues().molecularMass;
//	return this.macromolecule;
//};
//
//MacromoleculeForm.prototype.setMacromolecule = function(macromolecule) {
//	this.pdbStore.loadData(macromolecule.structure3VOs);
//
//};
//
//MacromoleculeForm.prototype.getForm = function(macromolecule) {
//	this.panel = Ext.createWidget('form', {
//		frame : false,
//		border : 0,
//		padding : 15,
//		width : 550,
//		height : 350,
//		items : [ {
//			xtype : 'container',
//			layout : 'hbox',
//			items : [ {
//				xtype : 'container',
//				flex : 1,
//				border : false,
//				layout : 'anchor',
//				defaultType : 'requiredtext',
//				items : [ {
//					fieldLabel : 'Name',
//					name : 'name',
//					anchor : '95%',
//					tooltip : "Name of the macromolecule",
//					value : macromolecule.name
//				}, {
//					fieldLabel : 'Acronym',
//					name : 'acronym',
//					anchor : '95%',
//					value : macromolecule.acronym
//				} ]
//			}, {
//				xtype : 'container',
//				flex : 1,
//				layout : 'anchor',
//				defaultType : 'textfield',
//				items : [ {
//					xtype : 'numberfield',
//					fieldLabel : 'Mol. Mass (Da)',
//					name : 'molecularMass',
//					value : macromolecule.molecularMass,
//					decimalPrecision : 6
//				}, {
//					xtype : 'numberfield',
//					fieldLabel : 'Extinction coef.',
//					name : 'extintionCoefficient',
//					value : macromolecule.extintionCoefficient,
//					decimalPrecision : 6
//				} ]
//			} ]
//		}, {
//			xtype : 'textareafield',
//			name : 'comments',
//			fieldLabel : 'Comments',
//			value : macromolecule.comments,
//			width : this.width - 10
//		},
//
//		{
//			html : BUI.getWarningHTML("The macromolecule is unsaved. Save the macromolecule to get access to other tabs"),
//			margin : "150 10 10 10",
//			id : this.id + "unsavedWarning",
//			hidden : !(!macromolecule.macromoleculeId)
//		}
//
//		]
//	});
//	return this.panel;
//};
//
//MacromoleculeForm.prototype.save = function() {
//	var _this = this;
//
//	var adapter = new BiosaxsDataAdapter();
//	adapter.onSuccess.attach(function(sender, proposal) {
//		BIOSAXS.proposal.setItems(proposal);
//		_this.panel.setLoading(false);
//
//		Ext.getCmp(_this.id + "assembly").enable()
//		Ext.getCmp(_this.id + "advanced").enable();
//		Ext.getCmp(_this.id + "unsavedWarning").hide();
//	});
//
//	if (this.getMacromolecule().name == "") {
//		BUI.showError("Name field is mandatory");
//		return;
//	}
//	if (this.getMacromolecule().acronym == "") {
//		BUI.showError("Acroynm field is mandatory");
//		return;
//	}
//
//	/** Check if acronym is unique * */
//	if (this.getMacromolecule().macromoleculeId == null) {
//		if (BIOSAXS.proposal.getMacromoleculeByAcronym(this.getMacromolecule().acronym) == null) {
//			this.panel.setLoading("ISPyB: Saving Macromolecule")
//			adapter.saveMacromolecule(this.getMacromolecule());
//		} else {
//			alert("There is already an existing macromolecule with the same acronym");
//
//		}
//	} else {
//		this.panel.setLoading("ISPyB: Saving Macromolecule")
//		adapter.saveMacromolecule(this.getMacromolecule());
//	}
//
//};
//
//MacromoleculeForm.prototype.getPanel = function(macromolecule) {
//	var _this = this;
//	this.macromolecule = macromolecule;
//	return Ext.createWidget('tabpanel', {
//		height : this.height,
//		margin : 5,
//		plain : true,
//		style : {
//			padding : 5
//		},
//		items : [ {
//			tabConfig : {
//				title : "General",
//				disabled : false
//			},
//			items : [ this.getForm(macromolecule) ],
//			bbar : [ "->", {
//				text : 'Save',
//				cls : 'btn-with-border',
//				style : {
//
//					border : 1
//				},
//				handler : function() {
//					_this.save();
//				}
//			}, {
//				text : 'Close',
//				cls : 'btn-with-border',
//				handler : function() {
//					_this.onClose.notify();
//				}
//			} ]
//		}, {
//			tabConfig : {
//				id : this.id + "assembly",
//				title : "Assembly",
//				tooltip : 'Description of subunits present in the macromolecule',
//				// hidden : (!macromolecule.macromoleculeId),
//				disabled : (!macromolecule.macromoleculeId)
//			},
//			items : [ this.getMolarityGrid(macromolecule) ]
//		}, {
//			tabConfig : {
//				id : this.id + "advanced",
//				title : "Advanced Modeling",
//				// hidden : (!macromolecule.macromoleculeId),
//				tooltip : 'Definition of the description contacts and symetries',
//				disabled : (!macromolecule.macromoleculeId)
//			},
//			items : [ this.getRigidBodyForm(macromolecule) ]
//		} ]
//	});
//};
//
//MacromoleculeForm.prototype.getRigidBodyForm = function(macromolecule) {
//	var _this = this;
//
//	// [P1, P2, P3, P4 ,P5 ,P6 ,32, P42, P52, P62] + P222
//	var symmetry = Ext.create('Ext.data.Store', {
//		fields : [ 's' ],
//		data : [ {
//			"s" : "P1"
//		}, {
//			"s" : "P2"
//		}, {
//			"s" : "P3"
//		}, {
//			"s" : "P4"
//		}, {
//			"s" : "P5"
//		}, {
//			"s" : "P6"
//		}, {
//			"s" : "P32"
//		}, {
//			"s" : "P42"
//		}, {
//			"s" : "P52"
//		}, {
//			"s" : "P62"
//		}, {
//			"s" : "P222"
//		} ]
//	});
//
//	if (macromolecule.symmetry == null) {
//		macromolecule.symmetry = "P1";
//	}
//	var comboBox = Ext.create('Ext.form.ComboBox', {
//		fieldLabel : 'Symmetry',
//		store : symmetry,
//		id : 'comboSym',
//		queryMode : 'local',
//		displayField : 's',
//		valueField : 's',
//		value : macromolecule.symmetry,
//		margin : "10 0 0 0",
//		listeners : {
//			change : function(combo, newValue, oldValue, eOpts) {
//				macromolecule.symmetry = newValue;
//			}
//		}
//	});
//
//	this.rigidBodyPanel = Ext.createWidget('form', {
//		frame : false,
//		border : 0,
//		padding : 10,
//		width : 550,
//		height : 400,
//		items : [ {
//			xtype : 'container',
//			layout : 'hbox',
//			items : [ {
//				xtype : 'container',
//				border : false,
//				layout : 'hbox',
//				items : [ {
//					xtype : 'label',
//					forId : 'myFieldId',
//					text : 'Contact Desc:',
//					width : 105,
//					margin : '0 0 0 0'
//				}, {
//					xtype : 'textfield',
//					hideLabel : true,
//					id : "contactsDescriptionFilePath",
//					margin : '0 0 0 0',
//					width : 300,
//					value : macromolecule.contactsDescriptionFilePath
//				}, {
//					text : 'Upload',
//					xtype : 'button',
//					margin : "0 0 0 20",
//					width : 100,
//					handler : function() {
//						_this._openUploadDialog(macromolecule.macromoleculeId, "CONTACTS", "Upload Contact Description File");
//					}
//				} ]
//			} ]
//		},
//
//		comboBox, {
//			xtype : 'checkbox',
//			margin : '10 0 0 5',
//			boxLabel : "<span style='font-weight:bold;'>I want rigid body modeling run on this stuff</span>",
//			checked : true,
//			width : 300
//		}, _this.getPDBGrid(macromolecule) ]
//	});
//	return this.rigidBodyPanel;
//};
//
//MacromoleculeForm.prototype.update = function() {
//	var _this = this;
//	BIOSAXS.proposal.onInitialized.attach(function() {
//		if (BIOSAXS.proposal != null) {
//			var macromolecules = BIOSAXS.proposal.macromolecules;
//			for (var i = 0; i < macromolecules.length; i++) {
//				if (macromolecules[i].macromoleculeId == _this.macromolecule.macromoleculeId) {
//					_this.macromolecule = macromolecules[i];
//					_this.setMacromolecule(_this.macromolecule);
//					_this.molarityStore.loadData(_this.parseMolarityData(_this.macromolecule));
//					_this.pdbGrid.setLoading(false);
//					Ext.getCmp("contactsDescriptionFilePath").setValue(_this.macromolecule.contactsDescriptionFilePath)
//					_this.molarityGrid.setLoading(false);
//
//				}
//			}
//		}
//	});
//	this.molarityGrid.setLoading("Updating");
//	this.pdbGrid.setLoading("Updating");
//	BIOSAXS.proposal.init();
//};
//
///*******************************************************************************
// * MOLARITY GRID
// ******************************************************************************/
//
//MacromoleculeForm.prototype.parseMolarityData = function(macromolecule) {
//	var data = [];
//	if (macromolecule.stoichiometry != null) {
//		for (var i = 0; i < macromolecule.stoichiometry.length; i++) {
//			data.push({
//				ratio : macromolecule.stoichiometry[i].ratio,
//				acronym : macromolecule.stoichiometry[i].macromolecule3VO.acronym,
//				comments : macromolecule.stoichiometry[i].macromolecule3VO.comments,
//				stoichiometryId : macromolecule.stoichiometry[i].stoichiometryId,
//				name : macromolecule.stoichiometry[i].macromolecule3VO.name
//			});
//		}
//	}
//	return data;
//};
//
//MacromoleculeForm.prototype.getMolarityGrid = function(macromolecule) {
//	var _this = this;
//
//	this.molarityStore = Ext.create('Ext.data.Store', {
//		fields : [ 'acronym', 'ratio', 'comments', 'stoichiometryId', 'name' ],
//		data : this.parseMolarityData(macromolecule),
//		sorters : {
//			property : 'ratio',
//			direction : 'DESC'
//		}
//	});
//
//	this.molarityGrid = Ext.create('Ext.grid.Panel', {
//		store : this.molarityStore,
//		height : 350,
//		padding : 5,
//		columns : [
//
//		{
//			text : 'Subunit',
//			columns : [ {
//				text : "Acronym",
//				width : 100,
//				hidden : false,
//				dataIndex : 'acronym',
//				sortable : true
//			}, {
//				text : "Name",
//				width : 100,
//				hidden : false,
//				dataIndex : 'name',
//				sortable : true
//			}, {
//				text : "Comments",
//				width : 100,
//				dataIndex : 'comments',
//				sortable : true
//			} ]
//		}, {
//			text : "Number <br/><span style='font-size:10px'>in assymmetric units</span>",
//			width : 150,
//			dataIndex : 'ratio',
//			tooltip : 'Number of times this subunit is present in the macromolecule',
//			sortable : true
//		}, {
//			id : this.id + 'MOLARITY_REMOVE',
//			flex : 0.1,
//			sortable : false,
//			renderer : function(value, metaData, record, rowIndex, colIndex, store) {
//				return BUI.getRedButton('REMOVE');
//			}
//		} ],
//		listeners : {
//			cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
//				if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'MOLARITY_REMOVE') {
//					var dataAdapter = new BiosaxsDataAdapter();
//					dataAdapter.onSuccess.attach(function() {
//						_this.molarityGrid.setLoading(false);
//						_this.update();
//					});
//					dataAdapter.removeStoichiometry(record.data.stoichiometryId);
//					_this.molarityGrid.setLoading("Removing Structure");
//				}
//			}
//		},
//		buttons : [ {
//			text : 'Add molarity',
//			handler : function() {
//
//				function onClose() {
//					w.destroy();
//					_this.update();
//				}
//				var w = Ext.create('Ext.window.Window', {
//					title : 'Molarity',
//					height : 300,
//					width : 500,
//					modal : true,
//					buttons : [ {
//						text : 'Save',
//						handler : function() {
//							var macromoleculeId = (_this.macromoleculeCombo.getValue());
//							var ratio = Ext.getCmp(_this.id + "ratio").getValue();
//							var comments = "";
//							var dataAdapter = new BiosaxsDataAdapter();
//							dataAdapter.onSuccess.attach(function(sender, args) {
//								_this.update();
//								w.destroy();
//							});
//							dataAdapter.saveStoichiometry(_this.macromolecule.macromoleculeId, macromoleculeId, ratio, comments);
//						}
//					}, {
//						text : 'Cancel',
//						handler : function() {
//							onClose();
//						}
//					} ],
//					items : [ _this.getMolarityForm(macromolecule) ],
//					listeners : {
//						onEsc : function() {
//							onClose();
//						},
//						close : function() {
//							onClose();
//						}
//					}
//				}).show();
//			}
//		} ]
//	});
//	return this.molarityGrid;
//};
//
///*******************************************************************************
// * PDB GRID
// ******************************************************************************/
//
//MacromoleculeForm.prototype._openUploadDialog = function(macromoleculeId, type, title) {
//	var _this = this;
//	function onClose() {
//		w.destroy();
//		_this.update();
//	}
//
//	var w = Ext.create('Ext.window.Window', {
//		title : title,
//		height : 200,
//		width : 400,
//		modal : true,
//		buttons : [ {
//			text : 'Close',
//			handler : function() {
//				onClose();
//			}
//		} ],
//		layout : 'fit',
//		items : {
//			html : "<iframe style='width:500px' src='uploadPdbFileSAXS.do?reqCode=display&macromoleculeId=" + macromoleculeId + "&type=" + type + "'></iframe>"
//		},
//		listeners : {
//			onEsc : function() {
//				onClose();
//			},
//			close : function() {
//				onClose();
//			}
//		}
//	}).show();
//};
//
//MacromoleculeForm.prototype._getPlugins = function() {
//	var _this = this;
//	var plugins = [];
//	// if (this.updateRowEnabled) {
//	plugins.push(Ext.create('Ext.grid.plugin.RowEditing', {
//		clicksToEdit : 1,
//		listeners : {
//			validateedit : function(grid, e) {
//				/** Comments are always updatable* */
//				e.record.raw.symmetry = e.newValues.symmetry;
//				e.record.raw.multiplicity = e.newValues.multiplicity;
//
//				var adapter = new BiosaxsDataAdapter();
//				adapter.onSuccess.attach(function(sender, measurement) {
//					// _this.grid.setLoading(false);
//				});
//				adapter.onError.attach(function() {
//					alert("Error");
//					// _this.grid.setLoading(false);
//				});
//
//				// _this.grid.setLoading();
//				adapter.saveStructure(e.record.raw);
//			}
//		}
//	}));
//	// }
//	return plugins;
//};
//
//MacromoleculeForm.prototype.getPDBGrid = function(macromolecule) {
//	var _this = this;
//
//	var data = macromolecule.structure3VOs;
//
//	// /** Getting PDB from subunits **/
//	// if (macromolecule != null){
//	// if (macromolecule.stoichiometry != null){
//	// for (var i =0; i < macromolecule.stoichiometry.length; i++){
//	// var stoichiometry = macromolecule.stoichiometry[i];
//	// if (stoichiometry.macromolecule3VO != null){
//	// if (stoichiometry.macromolecule3VO.structure3VOs != null){
//	// for (var j = 0; j < stoichiometry.macromolecule3VO.structure3VOs.length;
//	// j++) {
//	// var structure = stoichiometry.macromolecule3VO.structure3VOs[j];
//	// structure["isSubunit"] = stoichiometry.macromolecule3VO.acronym;
//	// data.push(structure)
//	// }
//	// }
//	// }
//	// }
//	// }
//	// }
//
//	this.pdbStore = Ext.create('Ext.data.Store', {
//		fields : [ 'filePath', 'structureId', 'structureType', 'symmetry', 'structureId', 'name', 'multiplicity' ],
//		data : macromolecule.structure3VOs,
//		groupField : 'structureType',
//		sorters : {
//			property : 'structureId',
//			direction : 'DESC'
//		}
//	});
//
//	var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
//		groupHeaderTpl : Ext.create('Ext.XTemplate',
//				"<div style='background:#0ca3d2; color:white; float:left; font-size:10px; margin:6px 8px 0 0; padding:5px 8px;'>{name:this.formatName}</div>", {
//					formatName : function(name) {
//						return name;
//					}
//				}),
//		hideGroupedHeader : true,
//		startCollapsed : false
//	});
//
//	this.pdbGrid = Ext.create('Ext.grid.Panel', {
//		margin : "15 0 0 5",
//		height : 250,
//		store : this.pdbStore,
//		plugins : _this._getPlugins(),
//		buttons : [ {
//			// text : 'Add PDB file',
//			text : 'Add Modeling Option',
//			handler : function() {
//				_this._openUploadDialog(macromolecule.macromoleculeId, "PDB", 'Upload PDB File');
//			}
//		}
//
//		],
//		columns : [
//				{
//					text : "structureId",
//					flex : 0.2,
//					hidden : true,
//					dataIndex : 'structureId',
//					sortable : true
//				},
//				{
//					text : "File",
//					flex : 0.5,
//					dataIndex : 'filePath',
//					sortable : true,
//					hidden : true
//				},
//				{
//					text : "PDB",
//					flex : 0.4,
//					dataIndex : 'name',
//					sortable : true
//				},
//				{
//					text : "Symmetry",
//					flex : 0.4,
//					dataIndex : 'symmetry',
//					sortable : true,
//					editor : {
//						xtype : 'combobox',
//						typeAhead : true,
//						triggerAction : 'all',
//						selectOnTab : true,
//						store : [ [ "P1", "P1" ], [ "P2", "P2" ], [ "P3", "P3" ], [ "P4", "P4" ], [ "P5", "P5" ], [ "P6", "P6" ], [ "P32", "P32" ], [ "P42", "P42" ],
//								[ "P52", "P52" ], [ "P62", "P62" ], [ "P222", "P222" ] ],
//					}
//				}, {
//					text : "Multiplicity",
//					flex : 0.4,
//					dataIndex : 'multiplicity',
//					sortable : true,
//					editor : {
//						xtype : 'textfield'
//					}
//
//				}, {
//					text : "Subunit",
//					flex : 0.2,
//					dataIndex : 'isSubunit',
//					sortable : true,
//					hidden : true
//				}, {
//					text : "Type",
//					flex : 0.2,
//					dataIndex : 'structureType',
//					sortable : true,
//					hidden : true
//				},
//
//				{
//					id : this.id + 'REMOVE',
//					flex : 0.2,
//					hidden : true,
//					sortable : false,
//					renderer : function(value, metaData, record, rowIndex, colIndex, store) {
//						return BUI.getRedButton('REMOVE');
//					}
//				}, ],
//
//		listeners : {
//			itemdblclick : function(dataview, record, item, e) {
//				_this._editExperiment(record.raw.experimentId);
//			},
//			cellclick : function(grid, td, cellIndex, record, tr, rowIndex, e, eOpts) {
//
//				if (grid.getGridColumns()[cellIndex].getId() == _this.id + 'REMOVE') {
//					var dataAdapter = new BiosaxsDataAdapter();
//					dataAdapter.onSuccess.attach(function() {
//						_this.pdbGrid.setLoading(false);
//						_this.update();
//					});
//					dataAdapter.removeStructure(record.data.structureId);
//					_this.pdbGrid.setLoading("Removing PDB file");
//				}
//
//			}
//		},
//		viewConfig : {
//			getRowClass : function(record, rowIdx, params, store) {
//				if (record.raw.isSubunit != null) {
//					return "blue-row";
//				}
//			}
//		}
//	});
//
//	return this.pdbGrid;
//};
//
//MacromoleculeForm.prototype.getMolarityForm = function(macromolecule) {
//	var _this = this;
//	var data = [];
//	for (var i = 0; i < BIOSAXS.proposal.macromolecules.length; i++) {
//		var m = BIOSAXS.proposal.macromolecules[i];
//		if (m.macromoleculeId != macromolecule.macromoleculeId) {
//			data.push(m);
//		}
//
//	}
//	this.macromoleculeCombo = BIOSAXS_COMBOMANAGER.getComboMacromoleculeByMacromolecules(data, {
//		width : 250,
//		labelWidth : 100,
//		margin : 10
//	});
//
//	return Ext.createWidget('form', {
//
//		frame : false,
//		border : 0,
//		// padding : 15,
//		width : 550,
//		height : 350,
//		items : [ {
//			xtype : 'container',
//			flex : 1,
//			border : false,
//			layout : 'anchor',
//			defaultType : 'requiredtext',
//			items : [ this.macromoleculeCombo, {
//				xtype : 'numberfield',
//				name : 'Ratio',
//				id : _this.id + "ratio",
//				fieldLabel : 'Number in assymmetric units',
//				value : 1,
//				decimalPrecision : 6,
//				margin : 10
//			}, {
//				xtype : 'textareafield',
//				name : 'comments',
//				fieldLabel : 'Comments',
//				margin : 10,
//				width : 400,
//				value : ""
//
//			} ]
//		} ]
//	});
//
//};
//
///*******************************************************************************
// * JAVASCRIPT DOC
// ******************************************************************************/
//MacromoleculeForm.prototype.input = function() {
//	return {
//		macromolecule : DATADOC.getMacromolecule_10()
//	};
//};
//
//MacromoleculeForm.prototype.test = function(targetId) {
//	var macromoleculeForm = new MacromoleculeForm();
//	var panel = macromoleculeForm.getPanel(macromoleculeForm.input().macromolecule);
//	panel.render(targetId);
//};
