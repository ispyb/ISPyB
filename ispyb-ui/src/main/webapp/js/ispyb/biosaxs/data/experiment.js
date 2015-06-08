/**
 * 
 * This class represents an data Acquisition
 * 
 * @json a json with the serialization of Experiment3VO
 * 
 */
function Experiment(json) {
	this.json = json;
	this.onSaved = new Event(this);
	this.onError = new Event(this);

	this.name = json.name;
	this.experimentId = json.experimentId;
	this.creationDate = json.creationDate;

	/** For all specimens asynchronous **/
	this.onPlateSaved = new Event(this);

	this.bufferColors =  $.extend({}, BIOSAXS.proposal.bufferColors);
	
	this.specimenBuffersColors = this.getSpecimenColors();
	
	for ( var bufferId in this.bufferColors) {
		this.bufferColors[bufferId] = 'black';
	}
	this.setMacromoleculesColors();
}

Experiment.prototype.getSpecimenColorByBufferId = function(specimenId) {
	return this.specimenBuffersColors[specimenId];
};

Experiment.prototype.setMacromoleculesColors = function (){
	var colors = [
					"#66c2a5",
					"#fc8d62",
					"#8da0cb",
					"#e78ac3",
					"#a6d854",
					"#ffd92f",
					"#e5c494"]
	
	
	this.macromoleculeColors = {};
	var macromolecules = this.getMacromolecules();
	for (var i = 0; i < macromolecules.length; i++) {
		this.macromoleculeColors[macromolecules[i].macromoleculeId] = colors[i%colors.length];
	}
};

Experiment.prototype.getHPLCMacromolecule = function() {
	var dcs = this.getDataCollections()
	if (dcs.length >  0){
		for ( var i = 0; i < 1; i++) {
			var meToDc = dcs[i].measurementtodatacollection3VOs;
			if (meToDc != null){
				for ( var j = 0; j < meToDc.length; j++) {
					if (meToDc[j].dataCollectionOrder == 2){
						return this.getSampleById(this.getMeasurementById(meToDc[j].measurementId).specimenId).macromolecule3VO;
					}
				}
			}
		}
	}
};

/**
 * If ((specimen1.specimenId <> specimen2.specimenId)&&(specimen1.bufferId == specimen2.bufferId)) then specimen1.color < specimen2.color
 */
Experiment.prototype.getSpecimenColors = function() {
	var specimens = this.getSamples();
	var bufferSamples = [];

	for ( var i = 0; i < specimens.length; i++) {
		if (specimens[i].macromolecule3VO === null) {
			bufferSamples.push(specimens[i]);
		}
	}

	var colors = {};
	var buffercolors = BIOSAXS.proposal.getBufferColors();
	for ( i = 0; i < bufferSamples.length; i++) {
		colors[bufferSamples[i].specimenId] = buffercolors[i % buffercolors.length];
	}
	return colors;
};

Experiment.prototype.getBuffers = function() {
	var samples = this.getSamples();
	var bufferHash = {};
	var buffers = [];
	if (samples !== null) {
		for ( var i = 0; i < samples.length; i++) {
			if (bufferHash[samples[i].bufferId] === null) {
				buffers.push(BIOSAXS.proposal.getBufferById(samples[i].bufferId));
				bufferHash[samples[i].bufferId] = true;
			}
		}
	}
	return buffers;
};

/** BUFFER **/
Experiment.prototype.getBufferById = function(bufferId) {
	return BIOSAXS.proposal.getBufferById(bufferId);
};

/** STOCK SOLUTION **/
Experiment.prototype.getStockSolutions = function() {
	return this.json.stockSolution3VOs;
};

Experiment.prototype.getStockSolutionById = function(stockSolutionId) {
	var stockSolutions = this.getStockSolutions();
	for ( var i = 0; i < stockSolutions.length; i++) {
		if (stockSolutions[i].stockSolutionId == stockSolutionId) {
			return stockSolutions[i];
		}
	}
};

/** This method gets all the macromolecules of the Experiment **/
Experiment.prototype.getMacromolecules = function() {
	var specimens = this.getSamples();
	var macromoleculeIds = {};
	
	var macromolecules = [];
	for (var i = 0; i < specimens.length; i++) {
		if (specimens[i].macromolecule3VO != null){
			if (macromoleculeIds[specimens[i].macromolecule3VO.macromoleculeId] == null){
				macromolecules.push(specimens[i].macromolecule3VO);
				macromoleculeIds[specimens[i].macromolecule3VO.macromoleculeId] = true;
			}
		}
	}
	return macromolecules;
};

/** Return an array with all the specimens collected, it means, where run3VO is not null **/
Experiment.prototype.getMeasurementsWithSubtractionAssociated = function() {
	var specimens = this.getMeasurements();
	var specimenCollected = [];
	for ( var i = 0; i < specimens.length; i++) {
		var specimen = specimens[i];
		if (specimen.run3VO !== null) {
			specimenCollected.push(specimen);
		}
	}
	return specimenCollected;
};

Experiment.prototype.getSamples = function() {
	return this.json.samples3VOs;
};

/** Deprecated changed to getSpecimenById **/
Experiment.prototype.getSampleById = function(specimenId) {
	return this.getSpecimenById(specimenId);
};

Experiment.prototype.getSpecimenById = function(specimenId) {
	var samples = this.getSamples();
	for ( var i = 0; i < samples.length; i++) {
		if (samples[i].specimenId == specimenId) {
			return samples[i];
		}
	}
	return null;
};

Experiment.prototype.getSpecimenByDataCollectionId = function(dataCollectionId) {
	var dataCollection = this.getDataCollectionById(dataCollectionId);
	var measurementHash = {};
	var results = [];
	for ( var j = 0; j < dataCollection.measurementtodatacollection3VOs.length; j++) {
		var measurement = this.getMeasurementById(dataCollection.measurementtodatacollection3VOs[j].measurementId);
		if (measurementHash[measurement.specimenId] == null) {
			results.push(measurement);
			measurementHash[measurement.specimenId] = true;
		}
	}
	return results;
};

Experiment.prototype.setSpecimenById = function(specimen) {
	for ( var i = 0; i < this.json.samples3VOs.length; i++) {
		if (this.json.samples3VOs[i].specimenId == specimen.specimenId) {
			this.json.samples3VOs[i] = specimen;
			return;
		}
	}
	console.log("Specimen with id: " + specimen.specimenId + " not found");
};

Experiment.prototype.setMeasurement = function(measurement) {
	var measurements = this.getMeasurements();
	for ( var i = 0; i < measurements.length; i++) {
		if (measurements[i].measurementId == measurement.measurementId) {
			measurements[i] = measurement;
			return;
		}
	}
};

Experiment.prototype.getConcentrations = function() {
	var concentrations = {};
	var specimens = this.getMeasurements();
	for ( var i = 0; i < specimens.length; i++) {
		if (specimens[i].concentration != null) {
			concentrations[specimens[i].concentration] = [ specimens[i].concentration ];
		}
	}
	var concValues = [];
	for ( var conc in concentrations) {
		if (conc != "null") {
			concValues.push(conc);
		}

	}
	return concValues;
};

Experiment.prototype.getConcentrationsBysample = function(sample) {
	var concentrations = {};
	var samples = this.getSamples();
	for ( var i = 0; i < samples.length; i++) {
		if (samples[i].macromolecule3VO != null) {
			if (samples[i].macromolecule3VO.macromoleculeId == sample.macromolecule3VO.macromoleculeId) {
				if (samples[i].concentration != null) {
					concentrations[samples[i].concentration] = [ samples[i].concentration ];
				}
			}
		}
	}
	var concValues = [];
	for ( var conc in concentrations) {
		if (conc != "null") {
			concValues.push(conc);
		}

	}
	return concValues;
};

Experiment.prototype.setSpecimen = function(specimen) {
	var buffers = this.getBuffers();
	for ( var i = 0; i < buffers.length; i++) {
		var buffer = buffers[i];
		for ( var j = 0; j < buffer.specimen3VOs.length; j++) {
			var specimen3vo = buffer.specimen3VOs[j];
			if (specimen3vo.specimenId == specimen.specimenId) {
				buffer.specimen3VOs[j] = specimen;
				return buffer;
			}
		}
	}
};

/** SAMPLE PLATES **/
Experiment.prototype.getSamplePlates = function() {
	return this.json.samplePlate3VOs;
};

Experiment.prototype.getSamplePlateById = function(samplePlateId) {
	var plates = this.getSamplePlates();
	for ( var i = 0; i < plates.length; i++) {
		if (plates[i].samplePlateId == samplePlateId) {
			return plates[i];
		}
	}
};

Experiment.prototype.getSamplePlateBySlotPositionColumn = function(slotPositionColumn) {
	var plates = this.getSamplePlates();
	for ( var i = 0; i < plates.length; i++) {
		if (plates[i].slotPositionColumn == slotPositionColumn) {
			return plates[i];
		}
	}
};

Experiment.prototype.getSpecimenByPosition = function(samplePlateId, rowNumber, columnNumber) {
	return this.getSampleByPosition();
};

Experiment.prototype.getSpecimensBySamplePlateId = function(samplePlateId) {
	var specimens = this.getSamples();
	var result = [];
	for ( var i = 0; i < specimens.length; i++) {
		if (specimens[i].sampleplateposition3VO != null) {
			if (specimens[i].sampleplateposition3VO.samplePlateId == samplePlateId) {
				result.push(specimens[i]);
			}
		}
	}
	return result;
};

Experiment.prototype.getSampleByPosition = function(samplePlateId, rowNumber, columnNumber) {
	var specimens = this.getSamples();
	var result = [];
	for ( var i = 0; i < specimens.length; i++) {
		if (specimens[i].sampleplateposition3VO != null) {
			if ((specimens[i].sampleplateposition3VO.samplePlateId == samplePlateId) && (specimens[i].sampleplateposition3VO.rowNumber == rowNumber) && (specimens[i].sampleplateposition3VO.columnNumber == columnNumber)) {
				result.push(specimens[i]);
			}
		}
	}
	return result;
};

Experiment.prototype.getPlateGroups = function() {
	var plates = this.getSamplePlates();
	var plateGroups = [];
	var keys = {};
	for ( var i = 0; i < plates.length; i++) {
		if (plates[i].plategroup3VO != null) {
			var id = plates[i].plategroup3VO.plateGroupId;
			if (keys[id] == null) {
				plateGroups.push(plates[i].plategroup3VO);
				keys[id] = true;
			}
		}
	}
	return plateGroups;
};

Experiment.prototype.getPlatesByPlateGroupId = function(plateGroupId) {
	var plates = this.getSamplePlates();
	var result = [];
	for ( var i = 0; i < plates.length; i++) {
		if (plates[i].plategroup3VO != null) {
			if (plates[i].plategroup3VO.plateGroupId == plateGroupId) {
				result.push(plates[i]);
			}
		}
	}
	return result;
};

Experiment.prototype.getMeasurements = function() {
	var speciments = [];
	var samples = this.getSamples();
	if (samples == null) {
		return [];
	}
	for ( var i = 0; i < samples.length; i++) {
		var sample = samples[i];
		for ( var j = 0; j < sample.measurements.length; j++) {
			speciments.push(sample.measurements[j]);
		}
	}
	return speciments;
};

Experiment.prototype.getMeasurementById = function(measurementId) {
	var specimens = this.getMeasurements();
	for ( var i = 0; i < specimens.length; i++) {
		if (specimens[i].measurementId == measurementId) {
			return specimens[i];
		}
	}
	return null;
};

Experiment.prototype.getMeasurementByDataCollectionId = function(dataCollectionId) {
	var result = [];
	var dataCollection = this.getDataCollectionById(dataCollectionId);
	for ( var i = 0; i < dataCollection.measurementtodatacollection3VOs.length; i++) {
		result.push(this.getMeasurementById(dataCollection.measurementtodatacollection3VOs[i].measurementId));
	}
	return result;
};

/** Data Collection **/

Experiment.prototype.getDataCollections = function() {
	var dc = this.json.dataCollections;
	var dataCollections = [];
	for ( var i = 0; i < dc.length; i++) {
		if (dc[i].measurementtodatacollection3VOs.length != 0) {
			dataCollections.push(dc[i]);
		}
	}
	return dataCollections;
};

Experiment.prototype.getDataCollectionsBySpecimenId = function(specimenId) {
	var measurements = this.getMeasurementsBySpecimenId(specimenId);
	var result = [];
	var resultHash = {};
	for ( var i = 0; i < measurements.length; i++) {
		var measurement = measurements[i];
		var dcs = this.getDataCollectionByMeasurementId(measurement.measurementId);
		for ( var j = 0; j < dcs.length; j++) {
			if (resultHash[dcs[j].dataCollectionId] == null) {
				resultHash[dcs[j].dataCollectionId] = true;
				result.push(dcs[j]);
			}
		}
	}
	return result;
};

Experiment.prototype.getMeasurementsBySpecimenId = function(specimenId) {
	var measurements = this.getMeasurements();
	var result = [];
	for ( var i = 0; i < measurements.length; i++) {
		if (measurements[i].specimenId == specimenId) {
			result.push(measurements[i]);
		}
	}
	return result;
};

Experiment.prototype.getDataCollectionByMeasurementId = function(measurementId) {
	var dc = this.getDataCollections();
	var result = [];
	
	function localSort(a, b) {
		return a.dataCollectionOrder - b.dataCollectionOrder;
	}
	
	for ( var i = 0; i < dc.length; i++) {
		if (dc[i].measurementtodatacollection3VOs.length != 0) {
			for ( var j = 0; j < dc[i].measurementtodatacollection3VOs.length; j++) {
				var measurement = dc[i].measurementtodatacollection3VOs[j];
				if (measurement.measurementId == measurementId) {
					/** Sorting by dataCollectionOrder **/
					dc[i].measurementtodatacollection3VOs.sort(localSort);
					result.push(dc[i]);
				}
			}
		}
	}
	return result;
};

Experiment.prototype.getSubtractionById = function(subtractionId) {
	var dc = this.getDataCollections();
	for ( var i = 0; i < dc.length; i++) {
		if (dc[i].substraction3VOs != null) {
			if (dc[i].substraction3VOs.length > 0) {
				for ( var j = 0; j < dc[i].substraction3VOs.length; j++) {
					if (dc[i].substraction3VOs[j].subtractionId == subtractionId) {
						return dc[i].substraction3VOs[j];
					}
				}
			}
		}
	}
	return null;
};

Experiment.prototype.getDataCollectionById = function(dataCollectionId) {
	var dc = this.json.dataCollections;
	for ( var i = 0; i < dc.length; i++) {
		if (dc[i].dataCollectionId == dataCollectionId) {
			return dc[i];
		}
	}
};

/** For a specimen calculates the volume to load adding all the volume to load of all the measurements **/
Experiment.prototype.getVolumeToLoadBySampleId = function(specimenId) {
	var sample = this.getSpecimenById(specimenId);
	if (sample != null) {
		var volumeToLoad = 0;
		for ( var i = 0; i < sample.measurements.length; i++) {
			volumeToLoad = volumeToLoad + Number(sample.measurements[i].volumeToLoad);
		}
		return volumeToLoad;
	}
};
