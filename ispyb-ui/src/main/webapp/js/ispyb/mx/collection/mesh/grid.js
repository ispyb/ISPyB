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

// Grid entity with the model, the controller and the view
function Grid(top, left, height, width, canvas, targetNode, id) {

	this._top = top;
	this._left = left;
	this._height = height;
	this._width = width;

	this._svg = SVG.createSVGCanvas(targetNode, [["id", "meshSvgId_"+id],
					["height", height + 260], ["width", width + 630]]);

	this._canvas = canvas;
	this.model = new GridModel();

	this.gridView = new GridView(this._svg, canvas, id);
	this.controller = new GridController(this.model, this.gridView, this._svg,
			this._canvas, top, left, height, width, 25, id);
}

Grid.prototype.refresh = function() {
	this.controller.setHeight((this.model.getRowNamesCount() * this.model
			.getSquareSize()));
	this.controller.setWidth((this.model.getColumnNamesCount() * this.model
			.getSquareSize()));
	return this.controller.refresh();
};

Grid.prototype.getColumnNameSpace = function() {
	return this.controller._columnNameHeight;
};

Grid.prototype.setColumnNameSpace = function(value) {
	this.controller.setColumnNameSpace(parseInt(value));
};

Grid.prototype.getRowNameSpace = function() {
	return this.controller._rowNameWidth;
};

Grid.prototype.setRowNameSpace = function(value) {
	this.controller.setRowNameSpace(parseInt(value));
};

Grid.prototype.getFontSize = function() {
	return this.gridView._fontSize;
};

Grid.prototype.setFontSize = function(value) {
	this.gridView._fontSize = value;
};

Grid.prototype.setWidth = function(value) {
	this.controller.setWidth(parseInt(value));
};

Grid.prototype.getWidth = function(value) {
	return this.controller.getWidth();
};

Grid.prototype.setHeight = function(value) {
	this.controller.setHeight(parseInt(value));
};

Grid.prototype.getHeight = function(value) {
	return this.controller.getHeight();
};

Grid.prototype.fill = function(rowNames, columnNames, data, scores, positions,
		images, title, axisXName, axisYName, squareSize, minValue, maxValue,
		colorRangeManager) {
	this.model.setRowsName(rowNames);
	this.model.setColumnsName(columnNames);
	this.model.setScores(scores);
	this.model.setPositions(positions);
	this.model.setData(data);
	this.model.setTitle(title);
	this.model.setAxisXName(axisXName);
	this.model.setAxisYName(axisYName);
	this.model.setSquareSize(squareSize);
	this.model.setMinValue(minValue);
	this.model.setMaxValue(maxValue);
	this.model.setColorRangeManager(colorRangeManager);
	this.model.setImages(images);
};

Grid.prototype.loadImage = function(fileLocation) {
	this.model.setBackgroundImage(fileLocation);
	this.controller.loadBackgroundImage();
};

Grid.prototype.unloadImage = function() {
	this.model.setBackgroundImage(null);
	this.controller.unloadBackgroundImage();
};
