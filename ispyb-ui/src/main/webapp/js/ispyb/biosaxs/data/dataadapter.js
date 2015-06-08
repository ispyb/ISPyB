/**
 * This class executes the actions
 */

function BiosaxsDataAdapter() {
	this.type = "POST";
	/** Events * */
	this.onSuccess = new Event(this);
	this.onError = new Event(this);
}

BiosaxsDataAdapter.prototype._doAction = function(reqCode, data) {
	var _this = this;
	$.ajax({
		type : "POST",
		url : "dataadapter.do?reqCode=" + reqCode,
		data : data,
		datatype : "text/json",
		success : function(json) {
			_this.onSuccess.notify(json);
		},
		error : function(xhr) {
			_this.onError.notify(xhr.responseText);
		}
	});
};


BiosaxsDataAdapter.prototype.getDataPlot = function(frames, subtractions, models, fits, rbms) {
	if (frames == null){
		frames = [];
	}
	if (subtractions == null){
		subtractions = [];
	}
	if (models == null){
		models = [];
	}
	if (fits == null){
		fits = [];
	}
	if (rbms == null){
		rbms = [];
	}
	this._doAction("getDataPlot", {
		frames : frames.toString(),
		subtractions : subtractions.toString(),
		models : models.toString(),
		fits : fits.toString(),
		rbms : rbms.toString()
	});
};

BiosaxsDataAdapter.prototype.getDataPDB = function(models, superpositions) {
	if (models == null){
		models = [];
	}
	if (superpositions == null){
		superpositions = [];
	}
	this._doAction("getDataPDB", {
		models : JSON.stringify(models), /** This is a json **/
		superpositions : superpositions.toString()
	});
};



BiosaxsDataAdapter.prototype.getFitStructureToExperimentalDataBySubtractionId = function(subtractionId) {
	this._doAction("getFitStructureToExperimentalDataBySubtractionId", {
		subtractionId : subtractionId
	});
};

BiosaxsDataAdapter.prototype.getAbinitioModelsBySubtractionId = function(subtractionIdList) {
	this._doAction("getAbinitioModelsBySubtractionId", {
		subtractionIdList : subtractionIdList.toString()
	});
};

BiosaxsDataAdapter.prototype.saveStructure = function(structure) {
	this._doAction("saveStructure", {
		structure : JSON.stringify(structure)
	});
};

BiosaxsDataAdapter.prototype.getSuperpositionBySubtractionId = function(subtractionId) {
	this._doAction("getSuperpositionBySubtractionId", {
		subtractionId : subtractionId
	});
};

BiosaxsDataAdapter.prototype.getRigidBodyModelingBySubtractionId = function(subtractionId) {
	this._doAction("getRigidBodyModelingBySubtractionId", {
		subtractionId : subtractionId
	});
};

BiosaxsDataAdapter.prototype.addFitStructureData = function(fitStructureToExperimentalData) {
	this._doAction("addFitStructureData", {
		fitStructureToExperimentalData : JSON.stringify(fitStructureToExperimentalData)
	});
};

BiosaxsDataAdapter.prototype.addWorkflow = function(workflow, inputParameters) {
	this._doAction("addWorkflow", {
		workflow : JSON.stringify(workflow),
		inputParameters : JSON.stringify(inputParameters)
	});
};

BiosaxsDataAdapter.prototype.getFitStructureToExperimentalDataFile = function(fitStructureToExperimentalDataId, type, format) {
	this._doAction("getFitStructureToExperimentalDataFile", {
		fitStructureToExperimentalDataId : fitStructureToExperimentalDataId,
		type : type,
		format : format
	});
};

BiosaxsDataAdapter.prototype.getStructureFile = function(structureId) {
	this._doAction("getStructureFile", {
		structureId : structureId
	});
};

BiosaxsDataAdapter.prototype.getCompactAnalysisByProposalId = function(limit) {
	if (limit == null) {
		limit = 100;
	}
	this._doAction("getCompactAnalysisByProposalId", {
		limit : limit

	});
};

BiosaxsDataAdapter.prototype.getCompactAnalysisByMacromoleculeId = function(macromoleculeId) {
	this._doAction("getCompactAnalysisByMacromoleculeId", {
		macromoleculeId : macromoleculeId
	});
};


BiosaxsDataAdapter.prototype.getCompactAnalysisBySubtractionId = function(subtractionId) {
	this._doAction("getCompactAnalysisBySubtractionId", {
		subtractionId : subtractionId
	});
};

/*
 BiosaxsDataAdapter.prototype.getAbinitioById = function(abinitioId) {
 this._doAction("getAbinitioById", {
 abinitioId : abinitioId
 });
 };
 */
BiosaxsDataAdapter.prototype.getAbinitioByIdsList = function(abinitioIdList) {
	this._doAction("getAbinitioByIdsList", {
		abinitioIdsList : abinitioIdList.toString()
	});
};

BiosaxsDataAdapter.prototype.getCompactAnalysisByExperimentId = function(experimentId) {
	this._doAction("getCompactAnalysisByExperimentId", {
		experimentId : experimentId
	});
};

BiosaxsDataAdapter.prototype.saveExperiment = function(experimentId, name, type, comments) {
	this._doAction("saveExperiment", {
		experimentId : experimentId,
		name : name,
		type : type,
		comments : comments
	});
};

BiosaxsDataAdapter.prototype.saveStoichiometry = function(hostmacromoleculeId, macromoleculeId, ratio, comments) {
	this._doAction("saveStoichiometry", {
		macromoleculeId : macromoleculeId,
		hostmacromoleculeId : hostmacromoleculeId,
		ratio : ratio,
		comments : comments
	});
};

BiosaxsDataAdapter.prototype.removeStoichiometry = function(stoichiometryId) {
	this._doAction("removeStoichiometry", {
		stoichiometryId : stoichiometryId
	});
};

BiosaxsDataAdapter.prototype.removeStructure = function(structureId) {
	this._doAction("removeStructure", {
		structureId : structureId
	});
};

BiosaxsDataAdapter.prototype.sortMeasurements = function(experimentId, type) {
	this._doAction("sortMeasurements", {
		experimentId : experimentId,
		sortBy : type
	});
};

BiosaxsDataAdapter.prototype.setMeasurementParameters = function(measurementIds, parameter, value) {
	this._doAction("setMeasurementParameters", {
		measurementIds : measurementIds.toString(),
		parameter : parameter,
		value : value
	});
};

BiosaxsDataAdapter.prototype.getZipFileByAverageListId = function(mergeIdsList) {
	this._doAction("getZipFileByAverageListId", {
		mergeIdsList : mergeIdsList.toString(),
		fileName : "test"
	});
};

BiosaxsDataAdapter.prototype.getZipFileByMacromoleculeId = function(macromoleculeId) {
	this._doAction("getZipFileByMacromoleculeId", {
		macromoleculeId : macromoleculeId
	});
};

BiosaxsDataAdapter.prototype.getH5FrameScattering = function(experimentId, frames) {
	this._doAction("getH5FrameScattering", {
		experimentId : experimentId,
		frames : frames
	});
};

BiosaxsDataAdapter.prototype.getH5FramesMerge = function(experimentId, frames) {
	this._doAction("getH5FramesMerge", {
		experimentId : experimentId,
		frames : frames
	});
};

BiosaxsDataAdapter.prototype.getH5fileParameters = function(experimentId, parameters) {
	this._doAction("getH5fileParameters", {
		experimentId : experimentId,
		parameters : parameters.toString()
	});
};

BiosaxsDataAdapter.prototype.mergeSpecimens = function(specimenId, targetId) {
	this._doAction("mergeSpecimens", {
		specimenTargetId : targetId,
		specimenOriginId : specimenId
	});
};

BiosaxsDataAdapter.prototype.getReport = function(experimentId, filename, type, format) {
	var data = {
		experimentId : experimentId,
		format : format,
		filename : filename,
		type : 'DATA_ACQUISITION'
	};
	if (Ext.urlDecode(window.location.href).sessionId != null) {
		data.sessionId = Ext.urlDecode(window.location.href).sessionId;
	}
	this._doAction("getReport", data);
};

BiosaxsDataAdapter.prototype.updateDataBaseFromSMIS = function() {
	this._doAction("updateDataBaseFromSMIS", {});
};

/** type is 'FIT' or 'FIR' * */
BiosaxsDataAdapter.prototype.getModelFile = function(type, modelId, format) {
	if (format == null) {
		format = 'json';
	}
	this._doAction("getModelFile", {
		type : type,
		modelId : modelId,
		format : format
	});
};

BiosaxsDataAdapter.prototype.getImage = function(subtractionId, type) {
	this._doAction("getImage", {
		subtractionId : subtractionId,
		type : type
	});
};

BiosaxsDataAdapter.prototype.removeExperimentContentById = function(experimentId) {
	this._doAction("removeExperimentContent", {
		experimentId : experimentId
	});
};

BiosaxsDataAdapter.prototype.getAbinitioModelsByExperimentId = function(experimentId) {
	this._doAction("getAbinitioModelsByExperimentId", {
		experimentId : experimentId
	});
};

BiosaxsDataAdapter.prototype.getMergesByIdsList = function(mergeIdsList) {
	this._doAction("getMergesByIdsList", {
		mergeIdsList : mergeIdsList.toString()
	});
};

BiosaxsDataAdapter.prototype.getScatteringCurveByFrameIdsList = function(frameIdsList, mergeIdsList, subtractionIds, fitToExperimentDataIds) {
	if (!fitToExperimentDataIds) {
		fitToExperimentDataIds = [3141];
	}
	this._doAction("getScatteringCurveByFrameIdsList", {
		frameIdsList : frameIdsList.toString(),
		mergeIdsList : mergeIdsList.toString(),
		subtractionIds : subtractionIds.toString(),
		fitToExperimentDataIds : fitToExperimentDataIds.toString()
	});
};

BiosaxsDataAdapter.prototype.getSubtractionsByIdsList = function(subtractionIds) {
	this._doAction("getSubtractionByIdList", {
		subtractionIdList : subtractionIds.toString()
	});
};

BiosaxsDataAdapter.prototype.removeExperimentById = function(experimentId) {
	this._doAction("removeExperiment", {
		experimentId : experimentId
	});
};

BiosaxsDataAdapter.prototype.getProposal = function(sessionId, scope) {
	this._doAction("getProposal", {
		sessionId : sessionId,
		scope : scope
	});
};

BiosaxsDataAdapter.prototype.getExperimentById = function(experimentId, scope) {
	if (scope == null) {
		scope = "MINIMAL";
	}
	this._doAction("getExperimentById", {
		experimentId : experimentId,
		scope : scope,
		sessionId : Ext.urlDecode(window.location.href).sessionId
	});
};

BiosaxsDataAdapter.prototype._getInformationByMacromoleculeId = function(data) {
	this._doAction("getInformationByMacromoleculeId", {
		data : JSON.stringify(data)
	});
};

BiosaxsDataAdapter.prototype.getInformationByMacromoleculeId = function(macromoleculeId, bufferId) {
	var data = {
		macromoleculeId : macromoleculeId
	};
	if (bufferId != null) {
		data.bufferId = bufferId;
	}
	var result = [ data ];
	this._getInformationByMacromoleculeId(result);
};

BiosaxsDataAdapter.prototype.getPDBContentByModelList = function(models) {
	this._doAction("getPDBContentByModelList", {
		properties : JSON.stringify(models)
	});
};

BiosaxsDataAdapter.prototype.getAbinitioPDBContentBySuperpositionList = function(models) {
	this._doAction("getAbinitioPDBContentBySuperpositionList", {
		properties : JSON.stringify(models)
	});
};

BiosaxsDataAdapter.prototype.getAlignedPDBContentBySuperpositionList = function(models) {
	this._doAction("getAlignedPDBContentBySuperpositionList", {
		properties : JSON.stringify(models)
	});
};


BiosaxsDataAdapter.prototype.getAnalysisCalibrationByProposalId = function() {
	this._doAction("getAnalysisCalibrationByProposalId", {
		sessionId : Ext.urlDecode(window.location.href).sessionId
	});
};

BiosaxsDataAdapter.prototype.getAnalysisInformationByExperimentId = function(experimentId) {
	this._doAction("getAnalysisInformationByExperimentId", {
		experimentId : experimentId
	});
};

BiosaxsDataAdapter.prototype.createTemplate = function(name, comments, measurements, experimentId) {
	this._doAction("createTemplate", {
		experimentId : experimentId,
		name : name,
		comments : comments,
		measurements : measurements.toString()
	});
};

BiosaxsDataAdapter.prototype.addMeasurements = function(name, comments, measurements, experimentId) {
	this._doAction("createTemplate", {
		experimentId : experimentId,
		name : name,
		comments : comments,
		measurements : measurements.toString()
	});
};

BiosaxsDataAdapter.prototype.getExperimentInformationByProposalId = function(sessionId) {
	this._doAction("getExperimentInformationByProposalId", {
		sessionId : sessionId
	});
};

BiosaxsDataAdapter.prototype.getAllInformationByExperimentId = function(experimentId) {
	this._doAction("getAllInformationByExperimentId", {
		experimentId : experimentId
	});
};

BiosaxsDataAdapter.prototype.getAnalysisByProposalId = function() {
	this._doAction("getAnalysisByProposalId", {});
};

BiosaxsDataAdapter.prototype.getExperimentsByProposalId = function(scope) {
	if (scope == null) {
		scope = "MINIMAL";
	}
	this._doAction("getExperimentsByProposalId", {
		scope : scope
	});
};

BiosaxsDataAdapter.prototype.saveMacromolecule = function(macromolecule) {
	this._doAction("saveMacromolecule", {
		macromolecule : JSON.stringify(macromolecule)
	});
};

BiosaxsDataAdapter.prototype.saveBuffer = function(buffer) {
	this._doAction("saveBuffer", {
		buffer : JSON.stringify(buffer)
	});
};

BiosaxsDataAdapter.prototype.saveSpecimen = function(specimen, experiment) {
	this._doAction("saveSpecimen", {
		specimen : JSON.stringify(specimen),
		experimentId : experiment.json.experimentId
	});
};

BiosaxsDataAdapter.prototype.saveMeasurement = function(measurement, experiment) {
	this._doAction("saveMeasurement", {
		measurement : JSON.stringify(measurement),
		experimentId : experiment.json.experimentId
	});
};

BiosaxsDataAdapter.prototype.removeMeasurement = function(measurement) {
	this._doAction("removeMeasurement", {
		measurementId : JSON.stringify(measurement)
	});
};

BiosaxsDataAdapter.prototype.savePlates = function(plates, experimentId) {
	var data = {
		plates : JSON.stringify(plates)
	};
	if (experimentId != null) {
		data.experimentId = experimentId;
		data.scope = "MEDIUM";
	}
	this._doAction("savePlate", data);
};

BiosaxsDataAdapter.prototype.emptyPlate = function(sampleplateId, experimentId) {
	this._doAction("emptyPlate", {
		scope : 'MEDIUM',
		sampleplateId : sampleplateId,
		experimentId : experimentId
	});
};

BiosaxsDataAdapter.prototype.autoFillPlate = function(sampleplateId, experimentId) {
	this._doAction("autoFillPlate", {
		scope : 'MEDIUM',
		sampleplateId : sampleplateId,
		experimentId : experimentId
	});
};

BiosaxsDataAdapter.prototype.createShipment = function(shippingId, name, status, comments, sendingLabContactId, returnLabContactId, returnCourier, courierAccount,
		BillingReference, dewarAvgCustomsValue, dewarAvgTransportValue) {
	var _this = this;
	$.ajax({
		type : this.type,
		url : "dataadapter.do?reqCode=saveShipment",
		data : {
			shippingId : shippingId,
			name : name,
			status : status,
			comments : comments,
			sendingLabContactId : sendingLabContactId,
			returnLabContactId : returnLabContactId,
			returnCourier : returnCourier,
			courierAccount : courierAccount,
			BillingReference : BillingReference,
			dewarAvgCustomsValue : dewarAvgCustomsValue,
			dewarAvgTransportValue : dewarAvgTransportValue
		},
		datatype : "text/json",
		success : function(data) {
			_this.onSuccess.notify(data);
		},
		error : function(xhr) {
			_this.onError.notify(xhr.responseText);
		}
	});
};

BiosaxsDataAdapter.prototype.removeShipment = function(shippingId) {
	this._doAction("removeShipment", {
		shippingId : shippingId
	});
};

BiosaxsDataAdapter.prototype.addCase = function(shippingId) {
	this._doAction("addCase", {
		shippingId : shippingId
	});
};

BiosaxsDataAdapter.prototype.saveCase = function(shippingId, dewar) {
	/** We dont want to store session so: * */
	dewar.sessionVO = null;
	this._doAction("saveCase", {
		dewar : JSON.stringify(dewar),
		dewarId : dewar.dewarId,
		shippingId : shippingId
	});
};

BiosaxsDataAdapter.prototype.removeCase = function(dewarId, shippingId) {
	this._doAction("removeCase", {
		dewarId : dewarId,
		shippingId : shippingId
	});
};

BiosaxsDataAdapter.prototype.removeStockSolution = function(stockSolutionId) {
	this._doAction("removeStockSolution", {
		stockSolutionId : stockSolutionId
	});
};

BiosaxsDataAdapter.prototype.saveStockSolution = function(stockSolution) {
	this._doAction("saveStockSolution", {
		stockSolution : JSON.stringify(stockSolution)
	});
};

/** ERROR * */
function showError(error) {
	error = JSON.parse(error);
	var title = "Internal server error";
	var meesage = error.message;

	if (this.panel == null) {
		this.panel = Ext.create('Ext.Window', {
			title : title,
			resizable : false,
			constrain : false,
			closable : true,
			padding : '15',
			items : [ {
				xtype : 'label',
				forId : 'myFieldId',
				text : meesage,
				margin : '5 0 0 10'
			}, {
				xtype : 'textareafield',
				value : error.trace,
				fieldLabel : 'Trace',
				width : 600,
				height : 300,
				margin : '20 0 0 10'
			} ],
			width : 680,
			height : 400,
			buttonAlign : 'right',
			listeners : {
				scope : this,
				minimize : function() {
					this.panel.hide();
				},
				destroy : function() {
					delete this.panel;
				}
			}
		});
		this.panel.setLoading();
	}
	this.panel.show();
}

function showWarning(warnings) {
	BUI.showWarning(warnings);
}
