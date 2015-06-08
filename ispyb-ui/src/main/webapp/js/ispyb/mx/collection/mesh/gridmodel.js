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
/**
 * The Model. Model stores items and notifies observers about changes.
 */
function GridModel() {
	this._rows = null;
	this._columns = null;
	this._data = null;
	this._rowNames = null;
	this._columnNames = null;
	this.rowsNameAdded = new Event(this);
	this.columnsNameAdded = new Event(this);
	this.dataAdded = new Event(this);
	this._scores = null;
	this._images = null;
	this._positions = null;
	this._title = null;
	this._axisXName = null;
	this._axisYName = null;
	this._squareSize = null;
	this._minValue = null;
	this._maxValue = null;
	this._colorRangeManager = null;
	this._backgroundImage = null;

}

GridModel.prototype.setBackgroundImage = function(value) {
	this._backgroundImage = value;
};

GridModel.prototype.getBackgroundImage = function() {
	return this._backgroundImage;
};

GridModel.prototype.setScores = function(value) {
	this._scores = value;
};

GridModel.prototype.getScores = function() {
	return this._scores;
};

GridModel.prototype.setImages = function(value) {
	this._images = value;
};

GridModel.prototype.getImages = function() {
	return this._images;
};

GridModel.prototype.setPositions = function(value) {
	this._positions = value;
};

GridModel.prototype.getPositions = function() {
	return this._positions;
};
// set the name of the columns
GridModel.prototype.setColumnsName = function(value) {
	this._columnNames = value;
};

GridModel.prototype.getColumnNames = function() {
	return this._columnNames;
};

GridModel.prototype.getColumnNamesCount = function() {
	return this._columnNames.length;
};

// set the name of the rows
GridModel.prototype.setRowsName = function(value) {
	this._rowNames = value;
};

GridModel.prototype.getRowNames = function() {
	return this._rowNames;
};

GridModel.prototype.getRowNamesCount = function() {
	return this._rowNames.length;
};

// title & axisName
GridModel.prototype.setTitle = function(value) {
	this._title = value;
};

GridModel.prototype.getTitle = function() {
	return this._title;
};

GridModel.prototype.setAxisXName = function(value) {
	this._axisXName = value;
};

GridModel.prototype.getAxisXName = function() {
	return this._axisXName;
};

GridModel.prototype.setAxisYName = function(value) {
	this._axisYName = value;
};

GridModel.prototype.getAxisYName = function() {
	return this._axisYName;
};

// square size
GridModel.prototype.setSquareSize = function(value) {
	this._squareSize = value;
};

GridModel.prototype.getSquareSize = function() {
	return this._squareSize;
};

//min value
GridModel.prototype.setMinValue = function(value) {
	this._minValue = value;
};

GridModel.prototype.getMinValue = function() {
	return this._minValue;
};

// max value
GridModel.prototype.setMaxValue = function(value) {
	this._maxValue = value;
};

GridModel.prototype.getMaxValue = function() {
	return this._maxValue;
};

// colorRangemanager
GridModel.prototype.setColorRangeManager = function(value) {
	this._colorRangeManager = value;
};

GridModel.prototype.getColorRangeManager = function() {
	return this._colorRangeManager;
};

// render
GridModel.prototype.render = function() {
	this.rowsNameAdded.notify({
				item : this._rowNames
			});
	this.columnsNameAdded.notify({
				item : this._columnNames
			});
	this.dataAdded.notify(this._data);
};

// data
GridModel.prototype.setData = function(value) {
	this._data = value;
	this._rows = this._data.length;
	this._columns = 0;
	if (this._data[0])
		this._columns = this._data[0].length;
};

GridModel.prototype.getData = function() {
	return this._data;
};

// copy
GridModel.prototype.copy = function(a, b) {
	for (var i = 0; i < this.getRowNamesCount(); i++) {
		this._data[i][b] = this._data[i][a];
	}
};

// move column a to b index
GridModel.prototype.move = function(a, b) {
	var column_a = [];
	var column_b_name = this._columnNames[b];
	var column_a_name = this._columnNames[a];

	if (b > a) {
		// backup column a
		for (var i = 0; i < this.getRowNamesCount(); i++) {
			column_a.push(this._data[i][a]);
		}
		//
		for (var i = a; i <= b; i++) {
			var inc = parseInt(i) + 1;
			this.copy(inc, i);
			// console.log(this._columnNames[i] +"<<" + this._columnNames[inc] +
			// " i: "+i+ " inc:"+inc);

			this._columnNames[i] = this._columnNames[inc];
		}
		//
		for (var i = 0; i < this.getRowNamesCount(); i++) {
			this._data[i][b] = column_a[i];
			this._columnNames[b] = column_b_name;
		}

		this._columnNames[b] = column_a_name;
	} else {
		// backup column a
		for (var i = 0; i < this.getRowNamesCount(); i++) {
			column_a.push(this._data[i][a]);
		}
		//
		for (var i = a; i > b; i--) {
			this.copy(i - 1, i);
			this._columnNames[i] = this._columnNames[i - 1];
		}
		// restored column a
		for (var i = 0; i < this.getRowNamesCount(); i++) {
			this._data[i][b] = column_a[i];
		}

		this._columnNames[b] = column_a_name;

	}
};
