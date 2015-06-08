/** MEASUREMENTS SELECTOR **/
function MeasurementCreatorStepWizardForm(macromolecules, args) {
	var _this = this;

	this.height = 700;
	this.noNext = false;

	this.macromolecules = macromolecules;
	GenericStepWizardForm.call(this, "Define Measurements", {
		margin : "0 0 0 0",
		width : 475,
		border : 0,
		html : "Define only the macromolecule's measurement you want to make. This wizard will add <span style='font-weight:bold;'> buffers' measurement needed for substraction automatically. </span>"
	}, {
		measurementsSelected : null
	},
	/** Fuction next **/
	function() {

		if (_this.noNext) {
			var result = {
				name : "",
				comments : "",
				data : JSON.stringify(_this.parseMeasurements(_this.data.measurementsSelected))
			};
			_this.onWizardFinished.notify(result);
		}
	}, function() {
	});

	if (args != null) {
		if (args.noNext != null) {
			this.noNext = args.noNext;
		}
	}

	if (this.noNext) {
		this.onWizardFinished = new Event(this);
	}
}

MeasurementCreatorStepWizardForm.prototype.setData = function(data) {
	this.data.measurementsSelected = data;
	var buffers = {};
	var macromolecules = {};
	for ( var i = 0; i < this.data.measurementsSelected.length; i++) {
		var record = this.data.measurementsSelected[i];
		if (buffers[record.bufferId] == null) {
			buffers[record.bufferId] = {
				buffer : record.buffer_acronym,
				bufferId : record.bufferId,
				volume : 0,
				concentration : 0
			};
		}

		var macromoleculeId = record.macromoleculeId + "_" + record.bufferId + "_" + record.concentration;
		if (macromolecules[macromoleculeId] == null) {
			macromolecules[macromoleculeId] = {
				macromolecule : record.acronym,
				macromoleculeId : record.macromoleculeId,
				buffer : record.buffer_acronym,
				bufferId : record.bufferId,
				volume : 0,
				concentration : record.concentration
			};
		}

		buffers[record.bufferId].volume = Number(buffers[record.bufferId].volume) + (2 * record.volumeToLoad);
		macromolecules[macromoleculeId].volume = Number(macromolecules[macromoleculeId].volume) + (record.volumeToLoad);
	}

};

MeasurementCreatorStepWizardForm.prototype.getSingleMeasurementForm = function() {
	var macromoleculesComboManager = BIOSAXS_COMBOMANAGER.getComboMacromoleculeByMacromolecules(this.macromolecules, {
		width : 200,
		labelWidth : 100
	});
	var buffersCombo = BIOSAXS_COMBOMANAGER.getComboBuffers(BIOSAXS.proposal.getBuffers(), {
		margin : "0 0 0 80",
		width : 200,
		labelWidth : 100
	});

	var addButton = Ext.create('Ext.Button', {
		width : 200,
		height : 25,
		margin : "0 0 20 300",
		text : 'Add',
		scope : this,
		handler : function() {
			var quantity = Ext.getCmp(this.id + "quantity").getValue();
			var measurements = [];
			for (var i = 0; i < quantity; i++) {
				measurements.push(
						{
							acronym : BIOSAXS.proposal.getMacromoleculeById(macromoleculesComboManager.getValue()).acronym,
							macromoleculeId : macromoleculesComboManager.getValue(),
							bufferId : buffersCombo.getValue(),
							buffer_acronym : BIOSAXS.proposal.getBufferById(buffersCombo.getValue()).acronym,
							concentration : Ext.getCmp(this.id + "conc").getValue(),
							volumeToLoad : Ext.getCmp(this.id + "volume").getValue(),
							exposureTemperature : Ext.getCmp(this.id + "seu").getValue(),
							transmission : Ext.getCmp(this.id + "transmission").getValue(),
							waitTime : Ext.getCmp(this.id + "waitTime").getValue(),
							viscosity : Ext.getCmp(this.id + "viscosity").getValue(),
							flow : Ext.getCmp(this.id + "flow").getValue()
						});
			}
			this.measurementGrid.grid.getStore().loadData(measurements, true);

			this.setData(JSON.parse(Ext.encode(Ext.pluck(this.measurementGrid.grid.getStore().data.items, 'data'))));
		}
	});

	return Ext.create('Ext.panel.Panel', {
		padding : "0 0 0 0",
		border : 0,
		margin : "10 5 20 10",
		height : 220,
		items : [ {
			xtype : 'container',
			layout : 'hbox',
			items : [ macromoleculesComboManager, buffersCombo,  {
				xtype : 'numberfield',
				id : this.id + "quantity",
				fieldLabel : 'Repeat',
				labelWidth : 100,
				width : 200,
				margin : "0 0 0 50",
				minValue : 0,
				maxValue : 300,
				value : 1
			} ]
		}, {
			xtype : 'container',
			layout : 'hbox',
			items : [ {
				xtype : 'numberfield',
				id : this.id + "conc",
				fieldLabel : 'Conc. (mg/ml)',
				labelWidth : 100,
				width : 200,
				margin : "0 0 0 0",
				minValue : 0,
				maxValue : 300,
				value : 1
			}, {
				xtype : 'numberfield',
				id : this.id + "seu",
				fieldLabel : 'Exposure. Temp.',
				labelWidth : 100,
				width : 200,
				margin : "0 0 0 80",
				value : 4,
				minValue : 4,
				maxValue : 60
			} ]
		}, {
			xtype : 'container',
			margin : "5 0 0 0",
			layout : 'hbox',
			items : [ {
				xtype : 'numberfield',
				id : this.id + "volume",
				fieldLabel : 'Vol. To Load (&#181l)',
				labelWidth : 100,
				width : 200,
				value : 40,
				minValue : 10,
				maxValue : 300
			},

			{
				xtype : 'numberfield',
				id : this.id + "transmission",
				fieldLabel : 'Transmission (%)',
				labelWidth : 100,
				width : 200,
				margin : "0 0 0 80",
				value : 100,
				minValue : 0,
				maxValue : 100
			}

			]
		}, {
			xtype : 'container',
			layout : 'hbox',
			margin : "5 0 0 0",
			items : [ {
				xtype : 'numberfield',
				id : this.id + "waitTime",
				fieldLabel : 'Wait Time',
				labelWidth : 100,
				width : 200,
				value : 0,
				minValue : 0,
				maxValue : 100
			}, {
				xtype : 'combo',
				id : this.id + "viscosity",
				store : [ "low", "medium", "high" ],
				fieldLabel : 'SC Viscosity',
				value : "low",
				labelWidth : 100,
				width : 200,
				margin : "0 0 0 80"
			}, {
				xtype : 'checkbox',
				id : this.id + "flow",
				checked : true,
				fieldLabel : 'Flow',
				labelWidth : 100,
				width : 250,
				margin : "0 0 0 80"
			} ]
		}, {
			xtype : 'container',
			layout : 'hbox',
			margin : "30 0 0 0",
			items : [ addButton ]
		} ]
	});
};

MeasurementCreatorStepWizardForm.prototype.getConcentrationMeasurementForm = function() {

	var macromoleculesComboManager = BIOSAXS_COMBOMANAGER.getComboMacromoleculeByMacromolecules(this.macromolecules, {
		width : 200,
		labelWidth : 100
	});
	var buffersCombo = BIOSAXS_COMBOMANAGER.getComboBuffers(BIOSAXS.proposal.getBuffers(), {
		margin : "0 0 0 80",
		width : 200,
		labelWidth : 100
	});

	var addButton = Ext.create('Ext.Button', {
		width : 200,
		height : 25,
		margin : "0 0 20 300",
		text : 'Add',
		scope : this,
		handler : function() {
			var macromoleculeId = macromoleculesComboManager.getValue();
			var bufferId = buffersCombo.getValue();
			var concentrationCount = Ext.getCmp(this.id + "_cs_conc").getValue();
			var volume = Ext.getCmp(this.id + "_cs_volume").getValue();

			var transmission = Ext.getCmp(this.id + "_cs_transmission").getValue();
			var seu = Ext.getCmp(this.id + "_cs_seu").getValue();
			var waitTime = Ext.getCmp(this.id + "_cs_waitTime").getValue();
			var viscosity = Ext.getCmp(this.id + "_cs_viscosity").getValue();
			var flow = Ext.getCmp(this.id + "_cs_flow").getValue();

			var dataToBeLoaded = [];
			for ( var i = 1; i <= concentrationCount; i++) {
				dataToBeLoaded.push({
					acronym : BIOSAXS.proposal.getMacromoleculeById(macromoleculeId).acronym,
					macromoleculeId : macromoleculeId,
					bufferId : bufferId,
					buffer_acronym : BIOSAXS.proposal.getBufferById(bufferId).acronym,
					concentration : i,
					volumeToLoad : volume,
					exposureTemperature : seu,
					transmission : transmission,
					waitTime : waitTime,
					viscosity : viscosity,
					flow : flow

				});
			}
			this.measurementGrid.grid.getStore().loadData(dataToBeLoaded, true);
			this.setData(JSON.parse(Ext.encode(Ext.pluck(this.measurementGrid.grid.getStore().data.items, 'data'))));
		}
	});

	return Ext.create('Ext.panel.Panel', {
		padding : "0 0 0 0",
		border : 0,
		margin : "5 5 20 10",
		height : 220,
		items : [

		{
			xtype : 'container',
			layout : 'hbox',
			items : [ macromoleculesComboManager, buffersCombo ]
		}, {
			xtype : 'container',
			layout : 'hbox',
			items : [ {
				xtype : 'requirednumberfield',
				id : this.id + "_cs_conc",
				fieldLabel : 'How many unknow concentrations do you have?',
				labelWidth : 380,
				style : {

					fontWeight : "bold"
				},
				width : 480,
				margin : "10 0 0 0",
				minValue : 0,
				maxValue : 20,
				value : 0
			} ]
		}, {
			xtype : 'container',
			margin : "10 0 0 0",
			layout : 'hbox',
			items : [ {
				xtype : 'numberfield',
				id : this.id + "_cs_seu",
				fieldLabel : 'Exposure. Temp.',
				labelWidth : 100,
				width : 200,
				margin : "0 0 0 0",
				value : 4,
				minValue : 4,
				maxValue : 60
			}, {
				xtype : 'numberfield',
				margin : "0 0 0 80",
				id : this.id + "_cs_volume",
				fieldLabel : 'Vol. To Load (&#181l)',
				labelWidth : 100,
				width : 200,
				value : 40,
				minValue : 10,
				maxValue : 300
			},

			{
				xtype : 'numberfield',
				id : this.id + "_cs_transmission",
				fieldLabel : 'Transmission (%)',
				labelWidth : 100,
				width : 200,
				margin : "0 0 0 80",
				value : 100,
				minValue : 0,
				maxValue : 100
			}

			]
		}, {
			xtype : 'container',
			layout : 'hbox',
			margin : "5 0 0 0",
			items : [ {
				xtype : 'numberfield',
				id : this.id + "_cs_waitTime",
				fieldLabel : 'Wait Time',
				labelWidth : 100,
				width : 200,
				value : 0,
				minValue : 0,
				maxValue : 100
			}, {
				xtype : 'combo',
				id : this.id + "_cs_viscosity",
				store : [ "low", "medium", "high" ],
				fieldLabel : 'SC Viscosity',
				value : "low",
				labelWidth : 100,
				width : 200,
				margin : "0 0 0 80"
			}, {
				xtype : 'checkbox',
				id : this.id + "_cs_flow",
				fieldLabel : 'Flow',
				checked : true,
				labelWidth : 100,
				width : 250,
				margin : "0 0 0 80"
			} ]
		}, {
			xtype : 'container',
			layout : 'hbox',
			margin : "20 0 0 0",
			items : [ addButton ]
		} ]
	});
};

MeasurementCreatorStepWizardForm.prototype.getForm = function() {
	var _this = this;

	this.formPanel = Ext.create('Ext.tab.Panel', {
		padding : "0 0 0 0",
		margin : "20 5 20 10",
		height : 220,
		items : [ {
			tabConfig : {
				title : 'Concentration Series'
			},
			items : this.getConcentrationMeasurementForm()
		},{
			tabConfig : {
				title : 'Individual Measurement'
			},
			items : [ this.getSingleMeasurementForm() ]
		} ]


	});
	this.measurementGrid = new MeasurementGrid({
		height : 300,
		maxHeight : 300,
		minHeight : 300,
		width : 870,
		tbar : false,
		maxWidth : 870,
		resizable : false,
		margin : "0 10 10 10",
		isStatusColumnHidden : true,
		isTimeColumnHidden : true,
		removeBtnEnabled : true,
		isPriorityColumnHidden : true
	});

	this.measurementGrid.onRemoved.attach(function(sender, data) {
		_this.setData(JSON.parse(Ext.encode(Ext.pluck(_this.measurementGrid.grid.getStore().data.items, 'data'))));
	});

	this.panel = Ext.create('Ext.panel.Panel', {
		plain : true,
		frame : false,
		border : 0,
		items : [ this.formPanel, this.addButton, this.measurementGrid.getPanel([]) ]
	});

	if (this.data.measurementsSelected != null) {
		/** Recover Data **/
		this.measurementGrid.grid.getStore().loadData(this.data.measurementsSelected, false);
		this.setData(JSON.parse(Ext.encode(Ext.pluck(this.measurementGrid.grid.getStore().data.items, 'data'))));
	}
	return this.panel;
};

MeasurementCreatorStepWizardForm.prototype.getBuffers = function(measurements) {
	var buffers = {};
	var macromolecules = {};
	var record = null;
	for ( var i = 0; i < measurements.length; i++) {
		record = measurements[i];
		if (buffers[record.bufferId] == null) {
			buffers[record.bufferId] = {
				buffer : record.buffer_acronym,
				bufferId : record.bufferId,
				volumeToLoad : record.volumeToLoad,
				volume : 0,
				concentration : 0,
				transmission : record.transmission,
				flow : record.flow,
				viscosity : record.viscosity,
			};
		}

		buffers[record.bufferId].volume = Number(buffers[record.bufferId].volume) + (2 * record.volumeToLoad);
	}

	/** Removing all **/
	var bufferSpecimens = [];
	for ( var buffer in buffers) {
		if (buffers.hasOwnProperty(buffer)) {
			record = buffers[buffer];
			bufferSpecimens.push({
				bufferId : record.bufferId,
				buffer : BIOSAXS.proposal.getBufferById(record.bufferId).acronym,
				concentration : record.concentration,
				volume : record.volume,
				volumeToLoad : record.volumeToLoad,
				transmission : record.transmission,
				flow : record.flow,
				viscosity : record.viscosity
	
			});
		}
	}
	return bufferSpecimens;
};

//MeasurementCreatorStepWizardForm.prototype.getBufferPosition = function(i) {
//	return {
//				plate : 2,
//				row : Math.floor(i / 3) + 1,
//				well : Number(Number(i % 3) + 9)
//	};
//};
//
//MeasurementCreatorStepWizardForm.prototype.getSamplePosition = function(i) {
//	return {
//				plate : 2,
//				row : Math.floor(i / 3) + 1,
//				well : Number(Number(i % 3) + 9)
//	};
//};

MeasurementCreatorStepWizardForm.prototype.parseMeasurements = function(measurements) {
	var _this = this;
	var parsed = [];
	var measurement = null;
	var i = null;
	/** For buffers **/
	var specimenBuffers = this.getBuffers(measurements);
	for (  i = 0; i < specimenBuffers.length; i++) {
//		var position = this.getBufferPosition(i);

		measurement = specimenBuffers[i];
		parsed.push({
			SEUtemperature 	: "",
			buffer 			: measurement.buffer,
			buffername 		: measurement.buffer,
			code 			: measurement.buffer,
			comments 		: "",
			concentration 	: 0,
			enable 			: true,
			flow 			: measurement.flow,
			recuperate 		: false,
			title 			: "",
			transmission 	: measurement.transmission,
			macromolecule 	: measurement.buffer,
			type 			: "Buffer",
			typen 			: 1,
			viscosity 		: measurement.viscosity,
			volume 			: measurement.volume,
			volumeToLoad 	: measurement.volumeToLoad,
			waittime 		: 0,
			plate 			: null,
			row 			: null,
			well 			: null
		});
	}

//	var positionHash = {};
//	var savedPositions = 0;
	for ( i = 0; i < measurements.length; i++) {
		measurement = measurements[i];
		var type = "Buffer";
		var macromolecule = "";
		if (measurement.macromoleculeId != null) {
			type = "Sample";
			macromolecule = BIOSAXS.proposal.getMacromoleculeById(measurement.macromoleculeId).acronym;
		}

//		var position = {
//			plate : 2,
//			row : Math.floor(i / 8) + 1,
//			well : Number(i % 8) + 1 - savedPositions
//		};
		/** Position is importante because is the way to distiguish between same samples **/
//		var positionId = measurement.macromoleculeId + "_" + measurement.buffer_acronym + "_" + measurement.concentration;
//		if (positionHash[positionId] == null) {
//			positionHash[positionId] = position;
//		} else {
//			savedPositions = savedPositions + 1;
//		}
//		position = positionHash[positionId];

		parsed.push({
			SEUtemperature : measurement.exposureTemperature,
			buffer : measurement.buffer_acronym,
			buffername : measurement.buffer_acronym,
			code : macromolecule,
			comments : "",
			concentration : measurement.concentration,
			enable : true,
			flow : measurement.flow,
			recuperate : false,
			title : "",
			transmission : measurement.transmission,
			macromolecule : macromolecule,
			type : type,
			typen : 1,
			viscosity : measurement.viscosity,
			volume : measurement.volumeToLoad,
			volumeToLoad : measurement.volumeToLoad,
			waittime : measurement.waitTime,
			plate : null,
			row : null,
			well : null
		});
	}
	
	var automatic = new SampleAutomaticPositionFactory(parsed);
	return automatic.setPosition();
};

MeasurementCreatorStepWizardForm.prototype.getNextForm = function() {
	return new FinalStepWizardForm(JSON.stringify(this.parseMeasurements(this.data.measurementsSelected)));
};



/**
 * Given a set of measurements this class will set the a priori right position for each specimen
 * @sampleParsed Array of:{"SEUtemperature":"","buffer":"CaCl2","buffername":"CaCl2","code":"CaCl2","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"CaCl2","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":null,"row":null,"well":null}
 */
function SampleAutomaticPositionFactory(sampleParsed){
	this.samples = sampleParsed;
//	this.samples  = JSON.parse('[{"SEUtemperature":"","buffer":"CaCl2","buffername":"CaCl2","code":"CaCl2","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"CaCl2","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":9},{"SEUtemperature":"","buffer":"B2dtt","buffername":"B2dtt","code":"B2dtt","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"B2dtt","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":10},{"SEUtemperature":"","buffer":"BSA","buffername":"BSA","code":"BSA","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"BSA","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":11},{"SEUtemperature":"","buffer":"AE","buffername":"AE","code":"AE","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"AE","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":2,"well":9},{"SEUtemperature":"","buffer":"ATP","buffername":"ATP","code":"ATP","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"ATP","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":2,"well":10},{"SEUtemperature":"","buffer":"B1","buffername":"B1","code":"B1","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"B1","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":2,"well":11},{"SEUtemperature":"","buffer":"B2","buffername":"B2","code":"B2","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"B2","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":3,"well":9},{"SEUtemperature":"","buffer":"CB-RE","buffername":"CB-RE","code":"CB-RE","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"CB-RE","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":3,"well":10},{"SEUtemperature":"","buffer":"HEPES7","buffername":"HEPES7","code":"HEPES7","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"HEPES7","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":3,"well":11},{"SEUtemperature":"","buffer":"FUR","buffername":"FUR","code":"FUR","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"FUR","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":4,"well":9},{"SEUtemperature":"","buffer":"BTris","buffername":"BTris","code":"BTris","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"BTris","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":4,"well":10},{"SEUtemperature":"","buffer":"AA","buffername":"AA","code":"AA","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"AA","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":4,"well":11},{"SEUtemperature":"","buffer":"CVCBuf","buffername":"CVCBuf","code":"CVCBuf","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"CVCBuf","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":5,"well":9},{"SEUtemperature":"","buffer":"BufferMax","buffername":"BufferMax","code":"BufferMax","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"BufferMax","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":5,"well":10},{"SEUtemperature":"","buffer":"BufferCicine","buffername":"BufferCicine","code":"BufferCicine","comments":"","concentration":0,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"BufferCicine","type":"Buffer","typen":1,"viscosity":"low","volume":800,"volumeToLoad":40,"waittime":0,"plate":2,"row":5,"well":11},{"SEUtemperature":4,"buffer":"AA","buffername":"AA","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":1},{"SEUtemperature":4,"buffer":"AA","buffername":"AA","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":2},{"SEUtemperature":4,"buffer":"AA","buffername":"AA","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":3},{"SEUtemperature":4,"buffer":"AA","buffername":"AA","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":4},{"SEUtemperature":4,"buffer":"AA","buffername":"AA","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":5},{"SEUtemperature":4,"buffer":"AA","buffername":"AA","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":6},{"SEUtemperature":4,"buffer":"AA","buffername":"AA","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":7},{"SEUtemperature":4,"buffer":"AA","buffername":"AA","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":1,"well":8},{"SEUtemperature":4,"buffer":"AA","buffername":"AA","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":2,"well":1},{"SEUtemperature":4,"buffer":"AA","buffername":"AA","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":2,"well":2},{"SEUtemperature":4,"buffer":"AE","buffername":"AE","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":2,"well":3},{"SEUtemperature":4,"buffer":"AE","buffername":"AE","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":2,"well":4},{"SEUtemperature":4,"buffer":"AE","buffername":"AE","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":2,"well":5},{"SEUtemperature":4,"buffer":"AE","buffername":"AE","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":2,"well":6},{"SEUtemperature":4,"buffer":"AE","buffername":"AE","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":2,"well":7},{"SEUtemperature":4,"buffer":"AE","buffername":"AE","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":2,"well":8},{"SEUtemperature":4,"buffer":"AE","buffername":"AE","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":3,"well":1},{"SEUtemperature":4,"buffer":"AE","buffername":"AE","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":3,"well":2},{"SEUtemperature":4,"buffer":"AE","buffername":"AE","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":3,"well":3},{"SEUtemperature":4,"buffer":"AE","buffername":"AE","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":3,"well":4},{"SEUtemperature":4,"buffer":"ATP","buffername":"ATP","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":3,"well":5},{"SEUtemperature":4,"buffer":"ATP","buffername":"ATP","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":3,"well":6},{"SEUtemperature":4,"buffer":"ATP","buffername":"ATP","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":3,"well":7},{"SEUtemperature":4,"buffer":"ATP","buffername":"ATP","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":3,"well":8},{"SEUtemperature":4,"buffer":"ATP","buffername":"ATP","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":4,"well":1},{"SEUtemperature":4,"buffer":"ATP","buffername":"ATP","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":4,"well":2},{"SEUtemperature":4,"buffer":"ATP","buffername":"ATP","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":4,"well":3},{"SEUtemperature":4,"buffer":"ATP","buffername":"ATP","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":4,"well":4},{"SEUtemperature":4,"buffer":"ATP","buffername":"ATP","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":4,"well":5},{"SEUtemperature":4,"buffer":"ATP","buffername":"ATP","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":4,"well":6},{"SEUtemperature":4,"buffer":"B1","buffername":"B1","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":4,"well":7},{"SEUtemperature":4,"buffer":"B1","buffername":"B1","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":4,"well":8},{"SEUtemperature":4,"buffer":"B1","buffername":"B1","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":5,"well":1},{"SEUtemperature":4,"buffer":"B1","buffername":"B1","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":5,"well":2},{"SEUtemperature":4,"buffer":"B1","buffername":"B1","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":5,"well":3},{"SEUtemperature":4,"buffer":"B1","buffername":"B1","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":5,"well":4},{"SEUtemperature":4,"buffer":"B1","buffername":"B1","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":5,"well":5},{"SEUtemperature":4,"buffer":"B1","buffername":"B1","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":5,"well":6},{"SEUtemperature":4,"buffer":"B1","buffername":"B1","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":5,"well":7},{"SEUtemperature":4,"buffer":"B1","buffername":"B1","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":5,"well":8},{"SEUtemperature":4,"buffer":"B2","buffername":"B2","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":6,"well":1},{"SEUtemperature":4,"buffer":"B2","buffername":"B2","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":6,"well":2},{"SEUtemperature":4,"buffer":"B2","buffername":"B2","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":6,"well":3},{"SEUtemperature":4,"buffer":"B2","buffername":"B2","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":6,"well":4},{"SEUtemperature":4,"buffer":"B2","buffername":"B2","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":6,"well":5},{"SEUtemperature":4,"buffer":"B2","buffername":"B2","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":6,"well":6},{"SEUtemperature":4,"buffer":"B2","buffername":"B2","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":6,"well":7},{"SEUtemperature":4,"buffer":"B2","buffername":"B2","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":6,"well":8},{"SEUtemperature":4,"buffer":"B2","buffername":"B2","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":7,"well":1},{"SEUtemperature":4,"buffer":"B2","buffername":"B2","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":7,"well":2},{"SEUtemperature":4,"buffer":"B2dtt","buffername":"B2dtt","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":7,"well":3},{"SEUtemperature":4,"buffer":"B2dtt","buffername":"B2dtt","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":7,"well":4},{"SEUtemperature":4,"buffer":"B2dtt","buffername":"B2dtt","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":7,"well":5},{"SEUtemperature":4,"buffer":"B2dtt","buffername":"B2dtt","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":7,"well":6},{"SEUtemperature":4,"buffer":"B2dtt","buffername":"B2dtt","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":7,"well":7},{"SEUtemperature":4,"buffer":"B2dtt","buffername":"B2dtt","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":7,"well":8},{"SEUtemperature":4,"buffer":"B2dtt","buffername":"B2dtt","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":8,"well":1},{"SEUtemperature":4,"buffer":"B2dtt","buffername":"B2dtt","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":8,"well":2},{"SEUtemperature":4,"buffer":"B2dtt","buffername":"B2dtt","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":8,"well":3},{"SEUtemperature":4,"buffer":"B2dtt","buffername":"B2dtt","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":8,"well":4},{"SEUtemperature":4,"buffer":"BSA","buffername":"BSA","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":8,"well":5},{"SEUtemperature":4,"buffer":"BSA","buffername":"BSA","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":8,"well":6},{"SEUtemperature":4,"buffer":"BSA","buffername":"BSA","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":8,"well":7},{"SEUtemperature":4,"buffer":"BSA","buffername":"BSA","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":8,"well":8},{"SEUtemperature":4,"buffer":"BSA","buffername":"BSA","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":9,"well":1},{"SEUtemperature":4,"buffer":"BSA","buffername":"BSA","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":9,"well":2},{"SEUtemperature":4,"buffer":"BSA","buffername":"BSA","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":9,"well":3},{"SEUtemperature":4,"buffer":"BSA","buffername":"BSA","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":9,"well":4},{"SEUtemperature":4,"buffer":"BSA","buffername":"BSA","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":9,"well":5},{"SEUtemperature":4,"buffer":"BSA","buffername":"BSA","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":9,"well":6},{"SEUtemperature":4,"buffer":"BTris","buffername":"BTris","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":9,"well":7},{"SEUtemperature":4,"buffer":"BTris","buffername":"BTris","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":9,"well":8},{"SEUtemperature":4,"buffer":"BTris","buffername":"BTris","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":10,"well":1},{"SEUtemperature":4,"buffer":"BTris","buffername":"BTris","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":10,"well":2},{"SEUtemperature":4,"buffer":"BTris","buffername":"BTris","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":10,"well":3},{"SEUtemperature":4,"buffer":"BTris","buffername":"BTris","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":10,"well":4},{"SEUtemperature":4,"buffer":"BTris","buffername":"BTris","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":10,"well":5},{"SEUtemperature":4,"buffer":"BTris","buffername":"BTris","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":10,"well":6},{"SEUtemperature":4,"buffer":"BTris","buffername":"BTris","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":10,"well":7},{"SEUtemperature":4,"buffer":"BTris","buffername":"BTris","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":10,"well":8},{"SEUtemperature":4,"buffer":"BufferCicine","buffername":"BufferCicine","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":11,"well":1},{"SEUtemperature":4,"buffer":"BufferCicine","buffername":"BufferCicine","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":11,"well":2},{"SEUtemperature":4,"buffer":"BufferCicine","buffername":"BufferCicine","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":11,"well":3},{"SEUtemperature":4,"buffer":"BufferCicine","buffername":"BufferCicine","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":11,"well":4},{"SEUtemperature":4,"buffer":"BufferCicine","buffername":"BufferCicine","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":11,"well":5},{"SEUtemperature":4,"buffer":"BufferCicine","buffername":"BufferCicine","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":11,"well":6},{"SEUtemperature":4,"buffer":"BufferCicine","buffername":"BufferCicine","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":11,"well":7},{"SEUtemperature":4,"buffer":"BufferCicine","buffername":"BufferCicine","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":11,"well":8},{"SEUtemperature":4,"buffer":"BufferCicine","buffername":"BufferCicine","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":12,"well":1},{"SEUtemperature":4,"buffer":"BufferCicine","buffername":"BufferCicine","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":12,"well":2},{"SEUtemperature":4,"buffer":"BufferMax","buffername":"BufferMax","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":12,"well":3},{"SEUtemperature":4,"buffer":"BufferMax","buffername":"BufferMax","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":12,"well":4},{"SEUtemperature":4,"buffer":"BufferMax","buffername":"BufferMax","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":12,"well":5},{"SEUtemperature":4,"buffer":"BufferMax","buffername":"BufferMax","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":12,"well":6},{"SEUtemperature":4,"buffer":"BufferMax","buffername":"BufferMax","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":12,"well":7},{"SEUtemperature":4,"buffer":"BufferMax","buffername":"BufferMax","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":12,"well":8},{"SEUtemperature":4,"buffer":"BufferMax","buffername":"BufferMax","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":13,"well":1},{"SEUtemperature":4,"buffer":"BufferMax","buffername":"BufferMax","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":13,"well":2},{"SEUtemperature":4,"buffer":"BufferMax","buffername":"BufferMax","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":13,"well":3},{"SEUtemperature":4,"buffer":"BufferMax","buffername":"BufferMax","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":13,"well":4},{"SEUtemperature":4,"buffer":"CB-RE","buffername":"CB-RE","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":13,"well":5},{"SEUtemperature":4,"buffer":"CB-RE","buffername":"CB-RE","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":13,"well":6},{"SEUtemperature":4,"buffer":"CB-RE","buffername":"CB-RE","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":13,"well":7},{"SEUtemperature":4,"buffer":"CB-RE","buffername":"CB-RE","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":13,"well":8},{"SEUtemperature":4,"buffer":"CB-RE","buffername":"CB-RE","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":14,"well":1},{"SEUtemperature":4,"buffer":"CB-RE","buffername":"CB-RE","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":14,"well":2},{"SEUtemperature":4,"buffer":"CB-RE","buffername":"CB-RE","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":14,"well":3},{"SEUtemperature":4,"buffer":"CB-RE","buffername":"CB-RE","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":14,"well":4},{"SEUtemperature":4,"buffer":"CB-RE","buffername":"CB-RE","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":14,"well":5},{"SEUtemperature":4,"buffer":"CB-RE","buffername":"CB-RE","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":14,"well":6},{"SEUtemperature":4,"buffer":"CVCBuf","buffername":"CVCBuf","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":14,"well":7},{"SEUtemperature":4,"buffer":"CVCBuf","buffername":"CVCBuf","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":14,"well":8},{"SEUtemperature":4,"buffer":"CVCBuf","buffername":"CVCBuf","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":15,"well":1},{"SEUtemperature":4,"buffer":"CVCBuf","buffername":"CVCBuf","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":15,"well":2},{"SEUtemperature":4,"buffer":"CVCBuf","buffername":"CVCBuf","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":15,"well":3},{"SEUtemperature":4,"buffer":"CVCBuf","buffername":"CVCBuf","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":15,"well":4},{"SEUtemperature":4,"buffer":"CVCBuf","buffername":"CVCBuf","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":15,"well":5},{"SEUtemperature":4,"buffer":"CVCBuf","buffername":"CVCBuf","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":15,"well":6},{"SEUtemperature":4,"buffer":"CVCBuf","buffername":"CVCBuf","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":15,"well":7},{"SEUtemperature":4,"buffer":"CVCBuf","buffername":"CVCBuf","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":15,"well":8},{"SEUtemperature":4,"buffer":"CaCl2","buffername":"CaCl2","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":16,"well":1},{"SEUtemperature":4,"buffer":"CaCl2","buffername":"CaCl2","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":16,"well":2},{"SEUtemperature":4,"buffer":"CaCl2","buffername":"CaCl2","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":16,"well":3},{"SEUtemperature":4,"buffer":"CaCl2","buffername":"CaCl2","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":16,"well":4},{"SEUtemperature":4,"buffer":"CaCl2","buffername":"CaCl2","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":16,"well":5},{"SEUtemperature":4,"buffer":"CaCl2","buffername":"CaCl2","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":16,"well":6},{"SEUtemperature":4,"buffer":"CaCl2","buffername":"CaCl2","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":16,"well":7},{"SEUtemperature":4,"buffer":"CaCl2","buffername":"CaCl2","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":16,"well":8},{"SEUtemperature":4,"buffer":"CaCl2","buffername":"CaCl2","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":17,"well":1},{"SEUtemperature":4,"buffer":"CaCl2","buffername":"CaCl2","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":17,"well":2},{"SEUtemperature":4,"buffer":"FUR","buffername":"FUR","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":17,"well":3},{"SEUtemperature":4,"buffer":"FUR","buffername":"FUR","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":17,"well":4},{"SEUtemperature":4,"buffer":"FUR","buffername":"FUR","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":17,"well":5},{"SEUtemperature":4,"buffer":"FUR","buffername":"FUR","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":17,"well":6},{"SEUtemperature":4,"buffer":"FUR","buffername":"FUR","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":17,"well":7},{"SEUtemperature":4,"buffer":"FUR","buffername":"FUR","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":17,"well":8},{"SEUtemperature":4,"buffer":"FUR","buffername":"FUR","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":18,"well":1},{"SEUtemperature":4,"buffer":"FUR","buffername":"FUR","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":18,"well":2},{"SEUtemperature":4,"buffer":"FUR","buffername":"FUR","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":18,"well":3},{"SEUtemperature":4,"buffer":"FUR","buffername":"FUR","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":18,"well":4},{"SEUtemperature":4,"buffer":"HEPES7","buffername":"HEPES7","code":"A","comments":"","concentration":1,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":18,"well":5},{"SEUtemperature":4,"buffer":"HEPES7","buffername":"HEPES7","code":"A","comments":"","concentration":2,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":18,"well":6},{"SEUtemperature":4,"buffer":"HEPES7","buffername":"HEPES7","code":"A","comments":"","concentration":3,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":18,"well":7},{"SEUtemperature":4,"buffer":"HEPES7","buffername":"HEPES7","code":"A","comments":"","concentration":4,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":18,"well":8},{"SEUtemperature":4,"buffer":"HEPES7","buffername":"HEPES7","code":"A","comments":"","concentration":5,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":19,"well":1},{"SEUtemperature":4,"buffer":"HEPES7","buffername":"HEPES7","code":"A","comments":"","concentration":6,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":19,"well":2},{"SEUtemperature":4,"buffer":"HEPES7","buffername":"HEPES7","code":"A","comments":"","concentration":7,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":19,"well":3},{"SEUtemperature":4,"buffer":"HEPES7","buffername":"HEPES7","code":"A","comments":"","concentration":8,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":19,"well":4},{"SEUtemperature":4,"buffer":"HEPES7","buffername":"HEPES7","code":"A","comments":"","concentration":9,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":19,"well":5},{"SEUtemperature":4,"buffer":"HEPES7","buffername":"HEPES7","code":"A","comments":"","concentration":10,"enable":true,"flow":true,"recuperate":false,"title":"","transmission":100,"macromolecule":"A","type":"Sample","typen":1,"viscosity":"low","volume":40,"volumeToLoad":40,"waittime":0,"plate":2,"row":19,"well":6}]');
};

SampleAutomaticPositionFactory.prototype.getAvailablePlates = function() {
	var plates = BIOSAXS.proposal.getPlateByFlavour();
	var bufferPositions = [];
	var samplePositions = [];
	
	/** is there a 4x(8x3) Block **/
	for (var i = 0; i < plates.length; i++) {
		if (plates[i].plateTypeId == 2){
			/** Booking place for buffers **/
			for (var index = 0; index < 12; index++) {
				bufferPositions.push({
										plate : 2,
										row : Math.floor(index / 3) + 1,
										well : Number(Number(index % 3) + 9)
									});
			}
			/** Booking place for macromolecules **/
			var savedPositions = 0;
			var positionHash = {};
			for (var index = 0; index < 32; index++) {
					var position = {
						plate : 2,
						row : Math.floor(index / 8) + 1,
						well : Number(index % 8) + 1 - savedPositions
					};
					samplePositions.push(position);
			}
		}
	}

	/** Rest of the plates **/
	for (var i = 0; i < plates.length; i++) {
		if (plates[i].plateTypeId != 2){
			/** Booking place for buffers **/
			for (var indexRow = 0; indexRow < plates[i].rowCount; indexRow++) {
				for (var indexColumn = 0; indexColumn < plates[i].columnCount; indexColumn++) {
					bufferPositions.push({
											plate 	: i + 1, 
											row 	: indexRow + 1,
											well 	: indexColumn + 1
										});
				}
			}
			
			/** Booking place for samples **/
			for (var indexRow = 0; indexRow < plates[i].rowCount; indexRow++) {
				for (var indexColumn = 0; indexColumn < plates[i].columnCount; indexColumn++) {
					samplePositions.push({
											plate 	: i + 1,
											row 	: indexRow + 1,
											well 	: indexColumn + 1
										});
				}
			}
		}
	}
	return {
		buffer : bufferPositions.reverse(),
		sample : samplePositions.reverse()
	};
};

SampleAutomaticPositionFactory.prototype._getKey = function(plate, row, well) {
	return plate + ":" +  row + "-" + well;
};

SampleAutomaticPositionFactory.prototype.setPosition = function() {
	var positions = (this.getAvailablePlates());
	
	var occupied = {};
	/** First, we set positions for buffers **/
	for (var i = 0; i < this.samples.length; i++) {
		if (this.samples[i].type == 'Buffer'){
			var key = this._getKey(this.samples[i].plate, this.samples[i].row, this.samples[i].well);
			this.samples[i].plate = positions.buffer[ positions.buffer.length - 1].plate,
			this.samples[i].row = positions.buffer[ positions.buffer.length - 1].row;
			this.samples[i].well =  positions.buffer[ positions.buffer.length - 1].well;
			occupied[key] = true;
			positions.buffer.pop();
		}
	}
	
	var positionHash = {};
	
	for (var i = 0; i < this.samples.length; i++) {
		if (this.samples[i].type == 'Sample'){
			var key = this._getKey(positions.sample[ positions.sample.length - 1].plate,  positions.sample[ positions.sample.length - 1].row, positions.sample[ positions.sample.length - 1].well);
			
			/** If this position is already occupied by a buffer we pop the stack **/
			while (occupied[key] != null){
				positions.sample.pop();
				key = this._getKey(positions.sample[ positions.sample.length - 1].plate,  positions.sample[ positions.sample.length - 1].row, positions.sample[ positions.sample.length - 1].well);
			}
			
			/**
			 * Position is important as it is the way to distiguish between same sample 
			 * For temperatures series two measurements pointing to the same macromolecule and buffer and concentration
			 * means that it is the same specimen
			 * **/
			var positionId = this.samples[i].macromolecule + "_" + this.samples[i].buffer + "_" + this.samples[i].concentration;
			if (positionHash[positionId] == null) {
				positionHash[positionId] = {
						plate : positions.sample[ positions.sample.length - 1].plate,
						row : positions.sample[ positions.sample.length - 1].row,
						well : positions.sample[ positions.sample.length - 1].well,
				};
			} else {
				/** There is already a specimen so we point out to its position **/
				this.samples[i].plate = positionHash[positionId].plate,
				this.samples[i].row = positionHash[positionId].row;
				this.samples[i].well =  positionHash[positionId].well;
				continue;
			}
			
			this.samples[i].plate = positions.sample[ positions.sample.length - 1].plate,
			this.samples[i].row = positions.sample[ positions.sample.length - 1].row;
			this.samples[i].well =  positions.sample[ positions.sample.length - 1].well;
			
			occupied[key] = true;
			positions.sample.pop();
		}
	}
	return this.samples;
};





