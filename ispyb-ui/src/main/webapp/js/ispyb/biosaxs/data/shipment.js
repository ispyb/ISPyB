function Shipment(json) {
	this.json = json;
	this.onSaved = new Event(this);
	this.onError = new Event(this);
}

Shipment.prototype.getDewars = function() {
	return this.json.dewarVOs;
};

Shipment.prototype.getDewars = function() {
	return this.json.dewarVOs;
};

Shipment.prototype.removeCase = function(dewarId) {
	var _this = this;
	var dataAdapter = new BiosaxsDataAdapter();
	dataAdapter.onSuccess.attach(function(sender, shipment) {
		_this.onSaved.notify(shipment);
	});

	dataAdapter.onError.attach(function(sender, error) {
		_this.onError.notify(error);
	});

	dataAdapter.removeCase(dewarId, this.json.shippingId);
};
