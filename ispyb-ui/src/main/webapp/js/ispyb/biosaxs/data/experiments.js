
function ExperimentList(experiments){
	this.experiments = experiments;
	
	this.macromoleculeColors = {}; 
	
	for (var i = 0; i < experiments.length; i++) {
		this.macromoleculeColors = $.extend({}, this.macromoleculeColors, experiments[i].macromoleculeColors); 
	}
}

ExperimentList.prototype.getSpecimenColorByBufferId = function (bufferId){
	for ( var i = 0; i < this.experiments.length; i++) {
		var experiment = this.experiments[i];
		var color = experiment.getSpecimenColorByBufferId(bufferId);
		if (color != null){
			return color;
		}
	}
	return 'black';
};

ExperimentList.prototype.getColorByMacromoleculeId = function (bufferId){
	for ( var i = 0; i < this.experiments.length; i++) {
		var experiment = this.experiments[i];
		var color = experiment.getColorByMacromoleculeId(bufferId);
		if (color != null){
			return color;
		}
	}
	return 'black';
};


ExperimentList.prototype.getMeasurements = function (){
	var result = [];
	for ( var i = 0; i < this.experiments.length; i++) {
		var experiment = this.experiments[i];
		var measurements = experiment.getMeasurements();
		for ( var j = 0; j < measurements.length; j++) {
			result.push(measurements[j]);
		} 
	} 
	return result;
};

ExperimentList.prototype.getMeasurementByDataCollectionId = function (dataCollectionId){
	for ( var i = 0; i < this.experiments.length; i++) {
		var dataCollection = this.experiments[i].getMeasurementByDataCollectionId(dataCollectionId);
		if (dataCollection.length != 0){
			return dataCollection;
		}
	}
	return [];
};

ExperimentList.prototype.getMergesByMeasurements = function (measurements){
	var merges = [];
	for ( var i = 0; i < measurements.length; i++) {
		if (measurements[i].merge3VOs != null){
			for ( var j = 0; j < measurements[i].merge3VOs.length; j++) {
				merges.push(measurements[i].merge3VOs[j]);
			}
		}
	}
	return merges;
};

ExperimentList.prototype.getSamplePlateById = function (samplePlateId){
	for ( var i = 0; i < this.experiments.length; i++) {
		var samplePlate = this.experiments[i].getSamplePlateById(samplePlateId);
		if (samplePlate != null){
			return samplePlate;
		}
	}
	return null;
};

ExperimentList.prototype.getMergesByDataCollectionId = function (dataCollectionId){
	return this.getMergesByMeasurements(this.getMeasurementByDataCollectionId(dataCollectionId));
};

ExperimentList.prototype.getMerges = function (){
	return this.getMergesByMeasurements(this.getMeasurements());
};

ExperimentList.prototype.getDataCollectionById = function (dataCollectionId){
	var result = [];
	for ( var i = 0; i < this.experiments.length; i++) {
		var experiment = this.experiments[i];
		var dc = experiment.getDataCollectionById(dataCollectionId);
		if (dc != null){
			return dc;
		}
	}
	return result;
};

ExperimentList.prototype.getDataCollectionByMeasurementId = function (measurementId){
	for ( var i = 0; i < this.experiments.length; i++) {
		var experiment = this.experiments[i];
		var result = experiment.getDataCollectionByMeasurementId(measurementId);
		if (result != null){
			return result;
		}
	}
	return null;
};


ExperimentList.prototype.getMeasurementsNotCollected = function (){
	var result = [];
	for ( var i = 0; i < this.experiments.length; i++) {
		var experiment = this.experiments[i];
		var measurements = experiment.getMeasurements();
		for ( var j = 0; j < measurements.length; j++) {
			if (measurements[j].run3VO == null){
				result.push(measurements[j]);
			}
		}
	}
	return result;
};

ExperimentList.prototype.getMeasurementsCollected = function (){
	var result = [];
	for ( var i = 0; i < this.experiments.length; i++) {
		var experiment = this.experiments[i];
		var measurements = experiment.getMeasurements();
		for ( var j = 0; j < measurements.length; j++) {
			if (measurements[j].run3VO != null){
				result.push(measurements[j]);
			}
		}
	}
	return result;
};

ExperimentList.prototype.getMeasurementById = function (specimenId){
	for ( var i = 0; i < this.experiments.length; i++) {
		var specimen = this.experiments[i].getMeasurementById(specimenId);
		if (specimen != null){
			return specimen;
		}
	}
	return null;
};

ExperimentList.prototype.getBufferById = function (bufferId){
	for ( var i = 0; i < this.experiments.length; i++) {
		var buffer = this.experiments[i].getBufferById(bufferId);
		if (buffer != null){
			return buffer;
		}
	}
	return null;
};

ExperimentList.prototype.getSampleById = function (sampleId){
	for ( var i = 0; i < this.experiments.length; i++) {
		var sample = this.experiments[i].getSampleById(sampleId);
		if (sample != null){
			return sample;
		}
	}
	return null;
};

ExperimentList.prototype.getSamplesByCondition = function (macromoleculeId, bufferId){
	var result = [];
	for ( var i = 0; i < this.experiments.length; i++) {
		var samples = this.experiments[i].getSamples();
		for ( var j = 0; j < samples.length; j++) {
			if (samples[j].macromolecule3VO != null){
				if (samples[j].macromolecule3VO.macromoleculeId == macromoleculeId){
					if (samples[j].bufferId == bufferId){
						result.push(samples[j]);
					}
				}
			}
		}
	}
	return result;
};

ExperimentList.prototype.getFrames = function (mergesList){
	var frames = [];
	for ( var i = 0; i < mergesList.length; i++) {
		var merge = mergesList[i];
		if (merge.framelist3VO != null){
			if (merge.framelist3VO.frametolist3VOs != null){
				for ( var j = 0; j < merge.framelist3VO.frametolist3VOs.length; j++) {
					var frame = merge.framelist3VO.frametolist3VOs[j].frame3VO;
					if (frame!= null){
						frames.push(frame);
					}
				}
			}
		}
	}
	return frames;
};
