/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * ISPyB is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P.
 * Brenchereau, M. Bodin
 ******************************************************************************/
// takes care of the color range 
function ColorRangeManager(args) {
	var _this = this;
	// --------------------------------
	// constants
	// --------------------------------

	this.redFrequency = 0.1;
	this.grnFrequency = 0.2;
	this.bluFrequency = 0.3;
	this.center = 128;
	this.width = 127;
	this.phase1 = 0;
	this.phase2 = 0;
	this.phase3 = 0;

	this.len = 20;

	this.reverseOrder =false;
	// --------------------------------
	// Private Attributes
	// --------------------------------

	this._emptyDataColor = "#E8DFE8";

	// other
	this._dataRange = {
		min : null,
		max : null
	};
	this._debug = false;
	this._colorRange = null;

	// calculated
	this._dataStep = null;
	this._maxDataSpace = null;

	if (args != null) {

		if (args.dataRange != null) {
			this._dataRange = args.dataRange;
		}
	}

	// setup color space
	this._colorRange = [];
	this._setupColorRange();
}

ColorRangeManager.prototype.loadData = function(dataRange, reverseOrder) {
	this._dataRange = dataRange;
	this._setupColorRange();
	this.reverseOrder = reverseOrder;
};

ColorRangeManager.prototype.getMaxValue = function() {
	return this._dataRange.max;
};


ColorRangeManager.prototype.getMinValue = function() {
	return this._dataRange.min;
};

// when given an RBGA object it returns a canvas-formatted string for that color
// if the RGBA is empty or ill-defined it returns a string for the empty data
// color
ColorRangeManager.prototype.getCellColorString = function(dataValue) {
	var colorValue = this.getCellColorRGBA(dataValue);
	return colorValue;
};

// returns an RBGA object with the color for the given dataValue
ColorRangeManager.prototype.getCellColorRGBA = function(dataValue) {
	if (dataValue == null) {
		return this._emptyDataColor;
	}
	var maxColors = this.getLen();

	var dataBin = dataValue / this._dataStep;
	var binOffset = this._dataRange.min / this._dataStep;
	var newDataBin = (dataBin - binOffset);
	newDataBin = dataValue * this._colorRange.length / this._dataRange.max;
	// round
	if (newDataBin < 0)
		newDataBin = Math.ceil(newDataBin);
	else
		newDataBin = Math.floor(newDataBin);

	this._log('value: ' + dataValue + ' bin: ' + dataBin + ' new bin: ' + newDataBin + ' / nbCol=' + this._colorRange.length + ' & max= ' + this._dataRange.max);
	if (this.reverseOrder){
		newDataBin = this._dataRange.max/ this._dataStep - newDataBin;
	}
	// assure bounds
	if (newDataBin < 0)
		newDataBin = 0;
	if (newDataBin >= this._colorRange.length)
		newDataBin = (this._colorRange.length) - 1;

	return this._colorRange[newDataBin];
};

// --------------------------------
// Private Methods
// --------------------------------

// maps data ranges to colors
ColorRangeManager.prototype._setupColorRange = function() {
	var dataRange = this._dataRange;
	var maxColors = this.getLen();
	this._colorRange = [];
	this._addColorsToRange();

	// if (maxColors > 256)
	// maxColors = 256;
	// if (maxColors < 1) {
	// maxColors = 1;
	// }
	// calc data step
	this._maxDataSpace = Math.abs(dataRange.min) + Math.abs(dataRange.max);
	this._dataStep = this._maxDataSpace / maxColors;

	this._log('dataStep: ' + this._dataStep);
};

// append colors to the end of the color Range, splitting the number of colors
// up evenly
ColorRangeManager.prototype._addColorsToRange = function() {
	var len = this.getLen();
	for (var i = 0; i < len; ++i) {
		var red = Math.sin(this.redFrequency * i + this.phase1) * this.width + this.center;
		var grn = Math.sin(this.grnFrequency * i + this.phase2) * this.width + this.center;
		var blu = Math.sin(this.bluFrequency * i + this.phase3) * this.width + this.center;
		this._colorRange[this._colorRange.length] = this._RGB2Color(red, grn, blu);
	}

};

ColorRangeManager.prototype._RGB2Color = function(r, g, b) {
	return '#' + this._byte2Hex(r) + this._byte2Hex(g) + this._byte2Hex(b);
};

ColorRangeManager.prototype._byte2Hex = function(n) {
	var nybHexString = "0123456789ABCDEF";
	return String(nybHexString.substr((n >> 4) & 0x0F, 1)) + nybHexString.substr(n & 0x0F, 1);
};

ColorRangeManager.prototype.getLen = function() {
	return this.len;
};

ColorRangeManager.prototype.createColorGradient = function(targetId, maxValue) {
	var len = this.getLen();
	document.getElementById(targetId).innerHTML = "0 ";
	for (var i = 0; i < len; ++i) {
		var red = Math.sin(this.redFrequency * i + this.phase1) * this.width + this.center;
		var grn = Math.sin(this.grnFrequency * i + this.phase2) * this.width + this.center;
		var blu = Math.sin(this.bluFrequency * i + this.phase3) * this.width + this.center;
		document.getElementById(targetId).innerHTML += '<font color="' + this._RGB2Color(red, grn, blu) + '">&#9608;</font>';
	}
	document.getElementById(targetId).innerHTML += maxValue;
};

ColorRangeManager.prototype.getColorRange = function(i) {
	i = i-this._dataRange.min/ this._dataStep ;
	if (this.reverseOrder){
		i = Math.floor(this._dataRange.max/ this._dataStep - i);
	}
	var red = Math.sin(this.redFrequency * i + this.phase1) * this.width + this.center;
	var grn = Math.sin(this.grnFrequency * i + this.phase2) * this.width + this.center;
	var blu = Math.sin(this.bluFrequency * i + this.phase3) * this.width + this.center;
	return this._RGB2Color(red, grn, blu);
};

ColorRangeManager.prototype._log = function(message) {
	if (this._debug) {
		console.log(message);
	}
};
