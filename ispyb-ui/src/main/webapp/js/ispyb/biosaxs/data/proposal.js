/**
 * Static class loaded almost always on entrypoint. It represent all the
 * information about the proposal
 * 
 * @json Serialization of a hashmap containing: macromolecules, buffers,
 *       assemblies, stockSolutions, labcontacts, shippings
 */
function Proposal(json) {
	this.macromolecules = null;

	/** Events * */
	this.onDataRetrieved = new Event(this);
	this.onError = new Event(this);
	this.onInitialized = new Event(this);

	this.buffers = null;
	this.macromolecules = null;
	this.stockSolutions = null;
	this.plateTypes = null;

	this._colors = null;
	this.bufferColors = {};
	this.macromoleculeColors = {};

	if (json != undefined) {
		this.setItems(json);
	}
	
	this.flavour = 'ESRF';
}

Proposal.prototype.getAdditiveTypes = function() {
	return [
		[ '', '' ], [ 'LIPID', 'LIPID' ], [ 'SALT', 'SALT' ], [ 'DETERGENT', 'DETERGENT' ],
		[ 'LIGAND', 'LIGAND' ], [ 'OTHER', 'OTHER' ] ];
};

Proposal.prototype.getBufferById = function(bufferId) {
	var buffers = this.getBuffers();
	for ( var i = 0; i < buffers.length; i++) {
		if (buffers[i].bufferId == bufferId) {
			return buffers[i];
		}
	}
};

Proposal.prototype.getShipmentByDewarId = function(dewarId) {
	var shippings = this.getShipments();
	for ( var i = 0; i < shippings.length; i++) {
		for ( var j = 0; j < shippings[i].dewarVOs.length; j++) {
			if (shippings[i].dewarVOs[j].dewarId == dewarId) {
				return shippings[i];
			}
		}
	}
	return null;
};

Proposal.prototype.getAssemblies = function() {
	return this.assemblies;
};

Proposal.prototype.getStockSolutions = function() {
	return this.stockSolutions;
};

Proposal.prototype.getSessions = function() {
	return this._data.sessions;
};

Proposal.prototype.getStructures = function() {
	var structures = [];
	var macromolecules = this.getMacromolecules();
	for (var i = 0; i < macromolecules.length; i++) {
		for (var j = 0; j < macromolecules[i].structure3VOs.length; j++) {
			structures.push(macromolecules[i].structure3VOs[j]);
		}
	}
	return structures;
};

Proposal.prototype.getStructuresById = function(structureId) {
	var structures = [];
	var macromolecules = this.getMacromolecules();
	for (var i = 0; i < macromolecules.length; i++) {
		for (var j = 0; j < macromolecules[i].structure3VOs.length; j++) {
			if (macromolecules[i].structure3VOs[j].structureId == structureId){
				return macromolecules[i].structure3VOs[j];
			}
		}
	}
	return structures;
};

Proposal.prototype.getUnpackedStockSolutions = function() {
	var stockSolutions = this.getStockSolutions();
	var result = [];
	for ( var i = 0; i < stockSolutions.length; i++) {
		if (stockSolutions[i].boxId == null) {
			result.push(stockSolutions[i]);
		}
	}
	return result;
};

Proposal.prototype.getStockSolutionsByDewarId = function(dewarId) {
	var stockSolutions = this.getStockSolutions();
	var result = [];
	for ( var i = 0; i < stockSolutions.length; i++) {
		if (stockSolutions[i].boxId == dewarId) {
			result.push(stockSolutions[i]);
		}
	}
	return result;
};

Proposal.prototype.sortByName = function(a, b) {
	if (a.acronym < b.acronym)
		return -1;
	if (a.acronym > b.acronym)
		return 1;
	return 0;
};

Proposal.prototype.setMacromolecules = function(macromolecules) {
	this.macromolecules = macromolecules;
	this.setColors();
};

Proposal.prototype._getColors = function() {
	if (this._colors == null) {
		var colors = [];
		var colorsCount = 8;
		for ( var i = 0; i < colorsCount; i++) {
			colors.push(BUI.rainbow(colorsCount, i));
		}
		this._colors = colors;
	}
	return this._colors;
};

Proposal.prototype.getMacromoleculeColors = function() {
	return this._getColors().slice(0, 3); // ["#ff0000", "#ffff00", "#00ff00",
											// "#00ffff", "#0000ff", "#ff00ff"];
};

Proposal.prototype.getBufferColors = function() {
	return [
	"#ffffcc",
	"#c7e9b4",
	"#7fcdbb",
	"#41b6c4",
	"#2c7fb8",
	"#253494"];
//	return ["blue"]
//	return this._getColors().slice(4, this._colors.length - 1); // [ "#ff99cc",
																// "#ffcc00",
																// "#00ff33",
																// "#00ffcc",
																// "#0099ff",
																// "#cc00cc"];
};

/** Set colors using the funcion rainbow on BUI /* */
Proposal.prototype.setColors = function() {
	var macromolecules = this.macromolecules.sort(this.sortByName);

	var colors = [];
	var colorsCount = 16;
	for ( var i = 0; i < colorsCount; i++) {
		colors.push(BUI.rainbow(colorsCount, i));
	}

	var macromoleculeColors = this.getMacromoleculeColors();
	var bufferColors = this.getBufferColors();
												
	var count = 0;
	for ( i = 0; i < macromolecules.length; i++) {
		this.macromoleculeColors[macromolecules[i].macromoleculeId] = macromoleculeColors[i% macromoleculeColors.length];
		count++;
	}
	
	var buffers = this.getBuffers().sort(this.sortByName);
	count = 0;
	if (buffers != null) {
		for (i = 0; i < buffers.length; i++) {
			this.bufferColors[buffers[i].bufferId] = bufferColors[i % bufferColors.length];
			count++;
		}
	}
};

Proposal.prototype.getBuffers = function() {
	return this.buffers;
};

Proposal.prototype.getLabcontacts = function() {
	return this.labcontacts;
};

Proposal.prototype.getShipments = function() {
	return this.shippings;
};

Proposal.prototype.getShipmentById = function(shipmentId) {
	var shipments = this.getShipments();
	for ( var i = 0; i < shipments.length; i++) {
		if (shipments[i].shippingId == shipmentId) {
			return shipments[i];
		}
	}
};

Proposal.prototype.getPlateTypes = function() {
	return this.plateTypes;
};

Proposal.prototype.getPlateByFlavour= function() {
	if (this.flavour == 'ESRF'){
		return [this.plateTypes[0], this.plateTypes[2], this.plateTypes[3]];
	}
};

Proposal.prototype.getPlateTypeById = function(plateTypeId) {
	var types = this.getPlateTypes();
	for ( var i = 0; i < types.length; i++) {
		if (types[i].plateTypeId == plateTypeId) {
			return types[i];
		}
	}
	return null;
};

Proposal.prototype.getSafetyLevels = function() {
	var safetyLevels = [];
	safetyLevels.push({
		safetyLevelId : "",
		name : "UNKNOWN"
	});
	safetyLevels.push({
		safetyLevelId : 1,
		name : "GREEN"
	});
	safetyLevels.push({
		safetyLevelId : 2,
		name : "YELLOW"
	});
	safetyLevels.push({
		safetyLevelId : 3,
		name : "RED"
	});
	return safetyLevels;
};

Proposal.prototype.setItems = function(data) {
	this._data = data;
	if (data.assemblies){
		this.assemblies = data.assemblies;
	}
	
	if (data.macromolecules){
		this.macromolecules = data.macromolecules;
	}
	if (data.buffers){
		this.buffers = data.buffers;
	}
	if (data.stockSolutions){
		this.stockSolutions = data.stockSolutions;
	}
	if (data.labcontacts){
		this.labcontacts = data.labcontacts;
	}
	if (data.shippings){
		this.shippings = data.shippings;
	}
	if (data.plateTypes){
		this.plateTypes = data.plateTypes;
	}
	this.setColors();
};

/** Get all the related items to the proposal : sessions, labcontacs, shippings, etc... **/
Proposal.prototype.init = function(sessionId) {
	var _this = this;
	var dataAdapter = new BiosaxsDataAdapter();
	dataAdapter.onSuccess.attach(function(sender, data) {
		_this.setItems(data);
		_this.onInitialized.notify();
	});
	dataAdapter.getProposal(sessionId);
};


/** scope: DATA, PROPOSAL **/
Proposal.prototype.initialize = function(sessionId, scope) {
	var _this = this;
	var dataAdapter = new BiosaxsDataAdapter();
	dataAdapter.onSuccess.attach(function(sender, data) {
		_this.setItems(data);
		_this.onInitialized.notify();
	});
	dataAdapter.getProposal(sessionId, scope);
};



/**
 * Remove the listeners in case we want to reinit the single class BIOSAXS.proposal 
 */
Proposal.prototype.removeListeners = function() {
	this.onInitialized = new Event(this);
};


Proposal.prototype.getMacromoleculeById = function(macromoleculeId) {
	for ( var i = 0; i < this.macromolecules.length; i++) {
		if (this.macromolecules[i].macromoleculeId == macromoleculeId) {
			return this.macromolecules[i];
		}
	}
	return null;
};

Proposal.prototype.getMacromoleculeByAcronym= function(acronym) {
	for ( var i = 0; i < this.macromolecules.length; i++) {
		if (this.macromolecules[i].acronym == acronym) {
			return this.macromolecules[i];
		}
	}
	return null;
};

Proposal.prototype.getStockSolutionById = function(stockSolutionId) {
	for ( var i = 0; i < this.stockSolutions.length; i++) {
		if (this.stockSolutions[i].stockSolutionId == stockSolutionId) {
			return this.stockSolutions[i];
		}
	}
	return null;
};

Proposal.prototype.getStockSolutionsBySpecimen = function(macromoleculeId, bufferId) {
	var result = [];
	for ( var i = 0; i < this.stockSolutions.length; i++) {
		if (this.stockSolutions[i].macromoleculeId == macromoleculeId) {
			if (this.stockSolutions[i].bufferId == bufferId) {
				result.push(this.stockSolutions[i]);
			}
		}
	}
	return result;
};

Proposal.prototype.getMacromolecules = function() {
	return this.macromolecules;
};

Proposal.prototype.getSafetyLabelName = function(safetyLevelId) {
	var safetyLevels = this.getSafetyLevels();
	for ( var i = 0; i < safetyLevels.length; i++) {
		if (safetyLevels[i].safetyLevelId == safetyLevelId) {
			return safetyLevels[i].name;
		}
	}
	return "UNKNOWN";
};

Proposal.prototype.getPlatesByProposal = function() {
	var _this = this;
	var dataAdapter = new BiosaxsDataAdapter();

	dataAdapter.onSuccess.attach(function(sender, data) {
		var experiments = [];
		for ( var i = 0; i < data.length; i++) {
			experiments.push(new Experiment(data[i]));
		}

		data = [];
		for (  i = 0; i < experiments.length; i++) {
			var samplePlates = experiments[i].getSamplePlates();
			for ( var j = 0; j < samplePlates.length; j++) {
				data.push(samplePlates[j]);
			}
		}

		_this.onDataRetrieved.notify(data);
	});
	dataAdapter.getExperimentsByProposalId("MEDIUM");
};

Proposal.prototype.getLabContactsByProposalId = function() {
	var _this = this;
	var dataAdapter = new BiosaxsDataAdapter();
	dataAdapter.onLabContactsRetrieved.attach(function(sender, labContacts) {
		_this.onDataRetrieved.notify(labContacts);
	});

	dataAdapter.onError.attach(function(sender, error) {
		_this.onError.notify(error);
	});

	dataAdapter.getLabContactsByProposalId();
};
