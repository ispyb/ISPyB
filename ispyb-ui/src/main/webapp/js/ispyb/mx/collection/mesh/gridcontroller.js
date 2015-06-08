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
// *** CONTROLERS **/
function GridController(model, view, svg, canvas, top, left, height, width,
		cellWidth, id) {
		
	this._model = model;
	this._view = view;

	this._top = top;
	this._left = left;
	this._height = height;
	this._width = width;
	this.id = id;

	this._cellWidth = cellWidth;
	this._cellHeight = null;

	// Columns
	this._columnNameSpace = 10;
	this._columnNameHeight = 20;
	this._pixelPerCharacter = 6;

	// Rows
	this._rowNameWidth = null;
	this._rowNameSpace = 10;

	// cell
	this._spanCell = 1;

	// DOM id's and classes
	this._row_id_prefix = id+'_row_name_';
	this._row_name_class = 'NO_EVENT';

	this._columnName_id_prefix = id+'_columnName_';
	this._columnName_name_class = 'COLUMN';

	this._columnNameHeader_name_class = 'COLUMN_HEADER';

	this._title_id_prefix = id+'_title';
	this._title_name_class = 'NO_EVENT';

	// p
	this._column_id_prefix = id+'_column_';
	this._column_name_class = 'COLUMN';

	var _this = this;

	// attach model listeners
	this._model.rowsNameAdded.attach(function() {
				_this._cellHeight = _this._height / _this._model.getRowNamesCount();
				_this.renderRowsName();
			});

	this._model.columnsNameAdded.attach(function() {
				_this._cellWidth = _this._width / _this._model.getColumnNamesCount();
				_this.renderColumnsName(0, _this._model.getColumnNamesCount() - 1);
			});

	this._model.dataAdded.attach(function() {
				_this._cellWidth = _this._width / _this._model.getColumnNamesCount();
				_this._cellHeight = _this._height / _this._model.getRowNamesCount();
				_this.renderGrid(0, _this._model.getColumnNamesCount() - 1);
			});

	this._view.mousedown.attach(function() {
				// console.log("mouseDown");
			});

	this._view.refreshEvent.attach(function() {
				_this.refresh();
			});

	this._view.moveColumnsEvent.attach(function(a) {

				

			});

}

GridController.prototype.setHeight = function(value) {
	this._height = value;
	this.setCellSize();
};

GridController.prototype.setWidth = function(value) {
	this._width = value;
	this.setCellSize();
};

GridController.prototype.getHeight = function() {
	return this._height;
};

GridController.prototype.getWidth = function() {
	return this._width;
};

GridController.prototype.setCellSize = function() {
	this._cellHeight = this._height / this._model.getRowNamesCount();
	this._cellWidth = this._width / this._model.getColumnNamesCount();
};

GridController.prototype.getRelativeTop = function() {
	return 10;
};

GridController.prototype.setSpaceCell = function(value) {
	this._spanCell = value;
	this.renderGrid(0, this._model.getColumnNamesCount() - 1);
};

GridController.prototype.setRowNameSpace = function(value) {
	this._rowNameWidth = value;
};

GridController.prototype.setColumnNameSpace = function(value) {
	this._columnNameSpace = value;
};

GridController.prototype._getCalculatedcolumnNameHeight = function(value) {
	return this._getLengthColumnNames() * this._pixelPerCharacter;
};

GridController.prototype._getCalculatedAxisYNameSpace = function(value) {
	return this._model.getAxisYName().length * this._pixelPerCharacter;
};

GridController.prototype._getCalculatedTitleHeight = function(value) {
	return this._pixelPerCharacter;
};

GridController.prototype._getCalculatedRowNameWidth = function(value) {
	return this._getLengthRowNames() * this._pixelPerCharacter;
};

GridController.prototype._getMaxLengthArray = function(array) {
	var max = 0;
	for (var i = 0; i < array.length; i++) {
		if (array[i]) {
			if (max < array[i].length) {
				max = array[i].length;
			}
		}
	}
	return max;
};

GridController.prototype._getLengthRowNames = function() {
	if (this._model.getRowNames() == null)
		return 5;
	else
		return this._getMaxLengthArray(this._model.getRowNames());
};

GridController.prototype._getLengthColumnNames = function() {
	if (this._model.getColumnNames() == null)
		return 5;
	else
		return this._getMaxLengthArray(this._model.getColumnNames());
};

GridController.prototype._getLengthTitle = function() {
	return 6;
};

// refresh
GridController.prototype.refresh = function() {
	this._view.clearRowsName();
	this._view.clearColumnsName();
	this._view.clearGrid(0, this._model.getColumnNamesCount() - 1);
	this._view.clearTitle();
	this._view.clearAxisYName();
	this._view.clearAxisXName();
	this._view.clearColorRange();
	this.unloadBackgroundImage();

	this.renderTitle();
	this.renderAxisYName();
	this.renderAxisXName();
	this.renderRowsName();
	this.renderColumnsName(0, this._model.getColumnNamesCount() - 1);
	this.renderGrid(0, this._model.getColumnNamesCount() - 1);
	this.renderColorRange();
	this.loadBackgroundImage();
};

// paint
GridController.prototype.paint = function() {
	this.renderTitle();
	this.renderAxisYName();
	this.renderAxisXName();
	this.renderRowsName();
	this.renderColumnsName(0, this._model.getColumnNamesCount() - 1);
	this.renderGrid(0, this._model.getColumnNamesCount() - 1);
	this.renderColorRange();
};

// render

GridController.prototype.renderColorRange = function() {
	var relative_local = this.getRelativeTop();
	var top_local = this._top + this._columnNameHeight + this._columnNameSpace + ((this._model.getRowNamesCount() + 1) * this._cellHeight) + relative_local + this._getCalculatedTitleHeight() * 5;
	var left_local = this._left + this._rowNameWidth + this._rowNameSpace + this._getCalculatedAxisYNameSpace() + this._cellHeight;
	this._view.renderColorRanger(top_local, left_local, this._model.getMinValue(), this._model.getMaxValue(), this._model.getColorRangeManager(), 10, 10, this._column_id_prefix, this._column_name_class, this.id);
};

GridController.prototype.renderTitle = function() {
	var relative_local = this.getRelativeTop();
	var top_local = this._top + this._columnNameSpace + relative_local;
	var left_local = this._left + this._rowNameWidth + this._rowNameSpace;
	this._view.renderTitle(top_local, left_local, this._model.getTitle(),
			this._title_id_prefix, this._title_name_class);
};

GridController.prototype.renderAxisYName = function() {
	var relative_local = this.getRelativeTop();
	if (this._columnNameHeight == null)
		this._columnNameHeight = this._getCalculatedcolumnNameHeight();
	var top_local = this._top + this._columnNameHeight + this._columnNameSpace + this.getRelativeTop() + (this._model.getRowNamesCount() * this._cellHeight) / 2 + this._getCalculatedTitleHeight();
	var left_local = this._left;
	this._view.renderAxisYName(top_local, left_local, this._model.getAxisYName(), this.id+'_axisYName', this._row_name_class);
};

GridController.prototype.renderAxisXName = function() {
	var relative_local = this.getRelativeTop();
	if (this._columnNameHeight == null)
		this._columnNameHeight = this._getCalculatedcolumnNameHeight();
	var top_local = this._top + this._columnNameHeight + this._columnNameSpace + ((this._model.getRowNamesCount() + 1) * this._cellHeight) + relative_local + this._getCalculatedTitleHeight() * 3;
	var left_local = this._left + this._rowNameWidth + this._rowNameSpace + this._getCalculatedAxisYNameSpace() + (this._model.getRowNamesCount() * this._cellHeight) / 3;
	this._view.renderAxisYName(top_local, left_local, this._model.getAxisXName(), this.id+'_axisXName', this._row_name_class);
};

GridController.prototype.renderRowsName = function() {
	if (this._columnNameHeight == null)
		this._columnNameHeight = this._getCalculatedcolumnNameHeight();
	var relative_local = this.getRelativeTop();
	var top_local = this._top + this._columnNameSpace + this._columnNameHeight + this._cellHeight / 2 + relative_local + this._getCalculatedTitleHeight();
	var left_local = this._left + this._getCalculatedAxisYNameSpace()+ this._cellHeight / 2;
	this._view.renderRowNames(top_local, left_local, this._cellHeight,this._model.getRowNames(), this._row_id_prefix, this._row_name_class);
};

GridController.prototype.renderColumnsName = function(a, b) {
	if (this._rowNameWidth == null)
		this._rowNameWidth = this._getCalculatedRowNameWidth();
	var left = this._left + this._rowNameWidth + this._rowNameSpace + this._cellWidth / 2 + this._getCalculatedAxisYNameSpace() + (this._cellHeight / 2);
	if (this._columnNameHeight == null)
		this._columnNameHeight = this._getCalculatedcolumnNameHeight();
	var topC = this._top + this.getRelativeTop() + this._columnNameSpace + this._columnNameHeight + this._getCalculatedTitleHeight() + ((this._model.getRowNamesCount()) * this._cellHeight) + this._getCalculatedTitleHeight();
	this._view.renderColumnNames(topC, left, a, b, this._cellWidth, this._model.getColumnNames(), this._columnName_id_prefix, this._columnNameHeader_name_class);

};

GridController.prototype.renderGrid = function(a, b) {
	if (this._rowNameWidth == null)
		this._rowNameWidth = this._getCalculatedRowNameWidth();
	var left = this._left + this._rowNameWidth + this._rowNameSpace + this._getCalculatedAxisYNameSpace() + this._cellHeight;

	if (this._columnNameHeight == null)
		this._columnNameHeight = this._getCalculatedcolumnNameHeight();
	var top = this._top + this._columnNameHeight + this._columnNameSpace + this.getRelativeTop() + this._getCalculatedTitleHeight();
	this._view.renderGrid(top, left, a, b, this._cellWidth, this._cellHeight, this._spanCell, this._column_id_prefix, this._column_name_class, this._model.getData(), this._model.getScores(), this._model.getPositions(), this._model.getImages());
};

GridController.prototype.loadBackgroundImage = function() {
	if (this._rowNameWidth == null)
		this._rowNameWidth = this._getCalculatedRowNameWidth();
	var left = this._left + this._rowNameWidth + this._rowNameSpace + this._getCalculatedAxisYNameSpace();
	var top = this._top + this._columnNameHeight + this._columnNameSpace + this.getRelativeTop() + this._getCalculatedTitleHeight();
	this._view.loadBackgroundImage(this._model.getBackgroundImage(), top, left);
};

GridController.prototype.unloadBackgroundImage = function() {
	this._view.clearBackgroundImage();
};
