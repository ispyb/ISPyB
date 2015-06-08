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
// ** view **/
function GridView(svg, canvas, id) {

	this._svg = svg;
	this._canvas = canvas;
	this.id = id;
	this._fontSize = 9;
	this._fontTitleSize = 12;

	this.cellInfo = true;

	this.columnName_id_prefix = null;
	this.columnName_name_class = null;
	this.columnHeaderName_name_class = null;

	this.column_id_prefix = null;
	this.column_class = null;

	this.rowName_id_prefix = null;
	this.rowName_name_class = null;

	// title and axis names
	this.title = "";
	this.axisXName = "";
	this.axisYName = "";

	// Array
	this.rowNames = null;

	this.columnNames = null;
	this.rowsName = null;
	this.data = null;
	this.scores = null;
	this.positions = null;
	this.images = null;

	this.columnConfiguration = null;
	this.enableDraggin = true;
	this.draggin = false;

	this.originColumnX = null;
	this.originColumnID = null;

	this.columnIndex = [];
	this.columnPosX = [];

	this.cellZoomId = id + "_cellZoom";

	/* Moving grid */
	this.enableMovingGrid = true;
	this.dragginGrid = false;
	this.movingGridPrevX = null;
	this.movingGridcurrentX = null;

	this.gridOffset = 0;
	this.previousGridOffset = 0;

	this.prevMouse = null;
	this.dragTarget = null;

	this.left = null;
	this.topGrid = null;
	this.cellWidth = null;

	var _this = this;

	this.mousedown = new Event(this);
	this.moveColumnsEvent = new Event(this);
	this.refreshEvent = new Event(this);

	this.cellSelected = new Event(this);

	this._svg.addEventListener("mousedown", function(event) {
				_this.onmousedown(event, _this);
			}, false);
	this._svg.addEventListener("mousemove", function(event) {
				_this.onmousemove(event, _this);
			}, false);
	this._svg.addEventListener("mouseup", function(event) {
				_this.onmouseup(event, _this);
			}, false);

	this.columnNamesXpos = [];

	this.lastColumnNameHeaderSelected = null;
	this.lastRowNameHeaderSelected = null;

	this.rowSelected = null;
	this.columnSelected = null;
}

GridView.prototype.setFontSize = function(size) {
	this._fontSize = size;
	this.refreshEvent.notify();
};

GridView.prototype.checkRangeForMovingColumns = function(columnOver) {
	return ((columnOver < this.columnNames.length) && (columnOver >= 0));
};

GridView.prototype.doOverEffect = function(columnOver, rowOver) {
	/*
	 * if ((columnOver < 0) || (rowOver < 0)) return;
	 * 
	 * 
	 * if (this.lastColumnNameHeaderSelected != null) { if
	 * (this.lastColumnNameHeaderSelected != this.columnName_id_prefix+
	 * columnOver) { var p =
	 * document.getElementById(this.lastColumnNameHeaderSelected); if (p !=
	 * null) p.setAttribute("style", ""); } if (this.lastRowNameHeaderSelected !=
	 * this.rowName_id_prefix+ rowOver) { var t =
	 * document.getElementById(this.lastRowNameHeaderSelected); if (t != null)
	 * t.setAttribute("style", ""); } } this.lastColumnNameHeaderSelected =
	 * this.columnName_id_prefix+ columnOver; this.lastRowNameHeaderSelected =
	 * this.rowName_id_prefix + rowOver;
	 */
};

GridView.prototype.recolocateColumns = function() {
	for (var i = 0; i < this.columnIndex.length; i++) {
		document.getElementById(this.columnIndex[i]).setAttribute("x",
				this.columnPosX[i]);
		document.getElementById(this.columnIndex[i]).setAttributeNS(null,
				"transform", "translate(" + this.gridOffset + ")");
	}
};

GridView.prototype.moveClientSideColumnNames = function(selectedColumn, over) {
	var selectedID = this.columnIndex[selectedColumn];
	var columnName = this.columnNames[selectedColumn];
	if (selectedColumn < over) {
		for (var i = parseInt(parseInt(selectedColumn) + parseInt(1)); parseInt(over) >= i; i++) {
			this.columnIndex[(i - 1)] = this.columnIndex[i];
			this.columnNames[(i - 1)] = this.columnNames[i];
		}
	}
	if (selectedColumn > over) {
		for (var i = parseInt(parseInt(selectedColumn)); parseInt(over) < i; i--) {
			this.columnIndex[(i)] = this.columnIndex[(i - 1)];
			this.columnNames[(i)] = this.columnNames[(i - 1)];
		}
	}
	// document.getElementById(selectedID
	// ).setAttributeNS(null,"transform","translate("+this.gridOffset+")");
	this.columnIndex[over] = selectedID;
	this.columnNames[over] = columnName;
	this.recolocateColumns();
};

GridView.prototype.moveHorizontaleColumns = function(value) {
	this.gridOffset = value + this.previousGridOffset;
	// columns
	for (var i = 0; i < this.columnIndex.length; i++) {
		// var attribute =
		// document.getElementById(this.columnIndex[i]).getAttributeNS(null,
		// "transform", "translate("+parseInt(value+
		// this.previousGridOffset)+")");
		document.getElementById(this.columnIndex[i]).setAttributeNS(null,
				"transform",
				"translate(" + parseInt(value + this.previousGridOffset) + ")");

		// var attribute = document.getElementById(this.columnName_id_prefix
		// + i).getAttributeNS(null, "transform", "translate("+value+")");
		document.getElementById(this.columnName_id_prefix + i).setAttributeNS(
				null, "transform",
				"translate(" + parseInt(value + this.previousGridOffset) + ")");

		if (document.getElementById("LINE_" + i) != null) {
			document.getElementById(this.id_dom_prefix_class + i)
					.setAttributeNS(
							null,
							"transform",
							"translate(" + parseInt(value + this.previousGridOffset) + ")");
			document.getElementById("LINE_" + i).setAttributeNS(
					null,
					"transform",
					"translate(" + parseInt(value + this.previousGridOffset) + ")");
		}

	}
	this.clearColumnsName();
	this.renderClientSideColumnNames(this.columnConfiguration);
	try {
		this.renderClassesName();
	} catch (e) {
	}
};

GridView.prototype.moveHorizontalGrid = function(value) {
	this.moveHorizontaleColumns(value);
};

GridView.prototype.drawCellInfoDiv = function(rowOver, columnOver, event) {
	var mouse = getMouseCoords(event, this._svg);
	this.rowSelected = rowOver;
	this.columnSelected = columnOver;

	if ((rowOver < 0) || (columnOver < 0) || (rowOver > this.rowNames.length) || (columnOver > this.columnNames.length)) {
		this.removeCellInfoDiv();
		return;
	}

	if (this.columnIndex[columnOver] == null)
		return;

	// var title1 = this.columnNames[columnOver];
	var title1 = "";
	if (this.images != null) {
		title1 = "imageDownload.do?reqCode=getImageJpgThumb&imageId=" + this.images[rowOver][this.columnIndex[columnOver].replace(this.column_id_prefix, "")];
	}
	var title2 = "";
	var title3 = "";

	if (this.scores != null) {
		title3 = "Position: " + this.positions[rowOver][this.columnIndex[columnOver].replace(this.column_id_prefix, "")];
		title2 = "Value = " + this.scores[rowOver][this.columnIndex[columnOver].replace(this.column_id_prefix, "")];
	}
	var color = this.data[rowOver][this.columnIndex[columnOver].replace(
			this.column_id_prefix, "")];

	this.drawCellInfoSVG(mouse.x + 10, mouse.y + 10, this._svg, title1, title2,title3, color);
	this.doOverEffect(columnOver, rowOver);
};

GridView.prototype.drawCellInfoSVG = function(x, y, canvasSVG, title1, title2, title3, color) {
	var group = SVG.createGroup([['id', this.cellZoomId]]);

	var rectAttributes = [['fill-opacity', '0.85'], ['fill', 'white'],
			['stroke', 'black'], ['rx', 5], ['ry', 5]];
	var rect = SVG.createRectangle(x, y, 350, 210, rectAttributes);

	var textVerticalSpace = 20;

	var snapshotX = x + 10;
	var snapshotY = y + 25;

	var snapshotAtrributes = [['fill-opacity', '1'], ['x', snapshotX],
			['y', snapshotY], ['stroke', 'black'], ['fill', color]];
	var snapshot = SVG.createRectangle(snapshotX, snapshotY, 12, 12,
			snapshotAtrributes);

	var textAtrributes = [["font-size", '12']];
	// var text1 = SVG.createText(x + 30, y + textVerticalSpace, title1,
	// textAtrributes);
	var svgns = 'http://www.w3.org/2000/svg';
	var img = document.createElementNS(svgns, 'image');
	img.setAttributeNS(null, 'x', x + 30);
	img.setAttributeNS(null, 'y', y + (textVerticalSpace * 3));
	img.setAttributeNS(null, 'height', 125);
	img.setAttributeNS(null, 'width', 125);
	var xlinkns = "http://www.w3.org/1999/xlink";
	img.setAttributeNS(xlinkns, 'xlink:href', title1);

	textAtrributes = [["font-size", '12']];
	var text2 = SVG.createText(x + 30, y + (textVerticalSpace * 1), title2, textAtrributes);

	textAtrributes = [["font-size", '12']];
	var text3 = SVG.createText(x + 30, y + (textVerticalSpace * 2), title3, textAtrributes);

	group.appendChild(rect);
	group.appendChild(snapshot);

	group.appendChild(text2);
	group.appendChild(text3);
	group.appendChild(img);

	canvasSVG.appendChild(group);
};

GridView.prototype.removeCellInfoDiv = function() {
	var zoomer = document.getElementById(this.cellZoomId);
	if (zoomer != null)
		this._svg.removeChild(zoomer);
};

GridView.prototype.onmousemove = function(e, view) {
	var mouse = getMouseCoords(e, this._svg);
	var rowOver = this.getRowOver(e);
	var columnOver = this.getColumnOver(e);

	if (this.data != null) {
		if (this.cellInfo) {
			this.removeCellInfoDiv();

			if (rowOver < this.rowNames.length)
				this.drawCellInfoDiv(rowOver, columnOver, e);
		}
	}

	if (this.dragginGrid && this.enableMovingGrid) {
		this.movingGridcurrentX = mouse.x;
		this.gridOffset = this.movingGridcurrentX - this.movingGridPrevX + this.previousGridOffset;
		this.moveHorizontalGrid(this.movingGridcurrentX - this.movingGridPrevX);

	}

	if (this.draggin && this.enableDraggin) {
		switch (this.dragTarget.className.animVal) {
			case this.columnName_name_class :

				var id = (this.dragTarget.id.replace(this.column_id_prefix, ""));

				if (this.checkRangeForMovingColumns(this.selectedColumn,
						columnOver)) {
					this.dragTarget._translateX += mouse.x - this.prevMouse.x;
					var despX = (mouse.x - this.prevMouse.x);
					var transform = 'translate(' + despX + ', ' + 65 + ')';

					document.getElementById(this.column_id_prefix + id)
							.setAttributeNS(null, "transform",
									"translate(" + this.offsetGrid + ")");
					document.getElementById(this.column_id_prefix + id)
							.setAttributeNS(null, "x",
									(mouse.x - this.cellWidth / 2));
					this.doOverEffect(columnOver, rowOver);
				}
				break;
			default :
				break;
		}
	}
};

GridView.prototype.movingOpacity = function(over) {
	var selectedColumn = this.dragTarget.id.replace(this.column_id_prefix, "");
	document.getElementById(this.columnName_id_prefix + selectedColumn)
			.setAttributeNS(null, "opacity", "1");
	document.getElementById(this.columnName_id_prefix + over).setAttributeNS(
			null, "opacity", "1");
	try {
		document.getElementById(this.columnName_id_prefix + parseInt(over - 1)).setAttributeNS(null, "opacity", "0.5");
		document.getElementById(this.columnName_id_prefix + parseInt(over + 1)).setAttributeNS(null, "opacity", "0.5");
	} catch (err) {
	}
};

GridView.prototype.removeOpacity = function() {
	for (var i = 0; i < this.columnNames.length; i++) {
		document.getElementById(this.column_id_prefix + i).setAttributeNS(null,
				"opacity", "1");
	}
};

GridView.prototype.doOpacity = function() {
	var selectedColumn = this.dragTarget.id.replace(this.column_id_prefix, "");

	if (this.columnNames == null)
		return;

	for (var i = 0; i < this.columnNames.length; i++) {
		if (i != selectedColumn) {
			document.getElementById(this.column_id_prefix + i).setAttributeNS(
					null, "opacity", "0.5");
		}
	}
};

GridView.prototype.getIndexColumnID = function(id) {
	for (var i = 0; i < this.columnIndex.length; i++) {
		if (this.columnIndex[i] == id)
			return i;
	}
};

GridView.prototype.onmousedown = function(e, view) {
	// if (e.button == 0) {
	// if (this.dragginGrid) {
	// this.dragginGrid = false;

	// this.previousGridOffset = this.gridOffset;
	// return;

	// }

	// if (this.draggin && this.enableDraggin) {
	// switch (this.dragTarget.className.animVal) {
	// case this.columnName_name_class:

	// var selectedColumn = this
	// .getIndexColumnID(this.dragTarget.id);
	// var overColumn = this.getColumnOver(e);
	// this.selectedColumn = selectedColumn;
	// this.overColumn = overColumn;

	// if (this.overColumn < 0)
	// this.overColumn = 0;
	// if (this.overColumn > this.columnNames.length - 1)
	// this.overColumn = this.columnNames.length - 1;

	// if (selectedColumn != overColumn) {
	// if (this.checkRangeForMovingColumns(
	// this.selectedColumn, this.overColumn)) {
	// this.moveClientSideColumnNames(this.selectedColumn,
	// this.overColumn);
	// }
	// }
	this.moveColumnsEvent.notify();

	// this.removeOpacity();
	// this.clearColumnsName();

	// this.renderClientSideColumnNames(this.columnConfiguration);
	// this.recolocateColumns();

	// break;
	// }
	// this.draggin = false;
	// this.dragTarget = null;
	// } else {
	// if (e.target.className.animVal == this.columnName_name_class) {
	// var overColumn = this.getColumnOver(e);

	// this.draggin = true;
	// this.dragTarget = e.target;
	// this.moveOnTop(this.dragTarget);

	// var selectedColumnID = this.column_id_prefix+
	// this.dragTarget.id.replace(this.column_id_prefix,"");
	// this.originColumnX =
	// document.getElementById(selectedColumnID).getAttribute("x");
	// this.originColumnID = document.getElementById(selectedColumnID).id;

	// this.selectedColumn = overColumn;

	// if (this.enableDraggin)
	// this.doOpacity();
	// }

	// if (e.target.className.animVal == this.columnHeaderName_name_class) {

	// var mouse = getMouseCoords(e, this._svg);
	// this.movingGridPrevX = mouse.x;
	// this.dragginGrid = true;

	// }

	// }
	// }

	// if (e.button == 2)
	// this.draggin = false;
	// this.prevMouse = getMouseCoords(e, this._svg);
};

GridView.prototype.onmouseup = function(e, view) {
	var mouse = getMouseCoords(e, this._svg);
	var rowOver = this.getRowOver(e);
	var columnOver = this.getColumnOver(e);
	if (this.data != null) {
		if (rowOver < this.rowNames.length) {
			this.clickOnCellGrid(rowOver, columnOver, e);
		}
	}

};

GridView.prototype.clickOnCellGrid = function(rowOver, columnOver, event) {
	this.rowSelected = rowOver;
	this.columnSelected = columnOver;

	if ((rowOver < 0) || (columnOver < 0) || (rowOver > this.rowNames.length) || (columnOver > this.columnNames.length)) {
		return;
	}

	if (this.columnIndex[columnOver] == null)
		return;
	var imageId = this.images[rowOver][this.columnIndex[columnOver].replace(
			this.column_id_prefix, "")];
	window.location.href = "viewResults.do?reqCode=viewJpegImage&imageId=" + imageId;
};

GridView.prototype.moveOnTop = function(elem) {
	this._svg.appendChild(elem);
};

GridView.prototype.getRowOver = function(e) {
	var mouse = getMouseCoords(e, this._svg);

	// return Math.floor(((mouse.y + (this.cellHeight / 2) - this.topGrid) /
	// this.cellHeight));
	return Math.floor(((mouse.y - this.topGrid) / this.cellHeight));
};

GridView.prototype.getColumnOver = function(e) {
	var mouse = getMouseCoords(e, this._svg);
	// return Math.floor(((mouse.x + (this.cellWidth / 2) - this.left -
	// this.gridOffset) / this.cellWidth));
	return Math
			.floor(((mouse.x - this.left - this.gridOffset) / this.cellWidth));
};

// Clearing methods
GridView.prototype.clearCanvas = function() {
	this.clearRowsName();
	this.clearColumnsName();
	this.clearTitle();
	this.clearColorRange();
	this.clearAxisYName();
	this.clearAxisXName();
};

GridView.prototype.clearGrid = function(a, b) {
	if (a > b) {
		var aux = a;
		a = b;
		b = aux;
	}
	for (var i = a; i <= b; i++) {
		var element = document.getElementById(this.column_id_prefix + i);
		if (element != null){
			this._svg.removeChild(element);
		}
		element = document.getElementById(this.columnName_id_prefix + i);
		if (element != null)
			this._svg.removeChild(element);
		element = document.getElementById(this.row_id_prefix + i);
		if (element != null){
			this._svg.removeChild(element);
		}
		element = document.getElementById(this.rowName_id_prefix + i);
		if (element != null)
			this._svg.removeChild(element);
	}
};

GridView.prototype.clearColumnsName = function() {
	var texts = this._svg.getElementsByTagName(this.id+"_text");
	for (var i = 0; i < this.columnNames.length; i++) {
		for (var j = 0; j < texts.length; j++) {
			if (texts[j].id == this.columnName_id_prefix + i) {
				this._svg.removeChild(texts[j]);
			}
		}
	}
};

GridView.prototype.clearRowsName = function() {
	var texts = this._svg.getElementsByTagName(this.id+"_text");
	for (var i = 0; i < this.rowNames.length; i++) {
		for (var j = 0; j < texts.length; j++) {
			if (texts[j].id == this.rowName_id_prefix + i) {
				this._svg.removeChild(texts[j]);
			}
		}
	}
};

GridView.prototype.clearTitle = function() {
	var element = document.getElementById(this.id+"_title");
	if (element != null)
		this._svg.removeChild(element);
};

GridView.prototype.clearAxisXName = function() {
	var element = document.getElementById(this.id+"_axisXName");
	if (element != null)
		this._svg.removeChild(element);
};

GridView.prototype.clearAxisYName = function() {
	var element = document.getElementById(this.id+"_axisYName");
	if (element != null)
		this._svg.removeChild(element);
};

GridView.prototype.clearColorRange = function() {
	var element = document.getElementById(this.id+"_minValue");
	if (element != null)
		this._svg.removeChild(element);
	element = document.getElementById(this.id+"_maxValue");
	if (element != null)
		this._svg.removeChild(element);
	var i = 0;
	element = document.getElementById(this.id+"_rect_" + i);
	while (element != null) {
		this._svg.removeChild(element);
		i++;
		element = document.getElementById(this.id+"_rect_" + i);
	}
};

GridView.prototype.clearBackgroundImage = function() {
	var element = document.getElementById("backgroundImage");
	if (element != null)
		this._svg.removeChild(element);
};
// Rendering methods

GridView.prototype.renderColorRanger = function(top, left, minValue, maxValue,
		colorRangeManager, cellWidth, cellHeight, column_id_prefix,
		column_name_class, id) {
	var nb = colorRangeManager.getLen();
	var svgns = "http://www.w3.org/2000/svg";
	var attributes = [['id', id+"_minValue"], ['class', column_name_class],
			['fill', '#000000'], ['font-size', this._fontTitleSize + 'px']];
	SVG.drawText(left, top, minValue, this._svg, attributes);
	for (var i = 0; i < nb; i++) {
		var color = colorRangeManager.getColorRange(i);
		// SVG.drawText(left + i*cellWidth , top , '&#9608;', this._svg,
		// attributes);
		var rect = document.createElementNS(svgns, 'rect');
		rect.setAttributeNS(null, 'x', left + i * cellWidth);
		rect.setAttributeNS(null, 'y', top);
		rect.setAttributeNS(null, 'height', cellWidth);
		rect.setAttributeNS(null, 'width', cellHeight);
		rect.setAttributeNS(null, 'fill', color);
		rect.setAttributeNS(null, 'id', id+"_rect_" + i);
		this._svg.appendChild(rect);
	}
	attributes = [['id', id+"_maxValue"], ['class', column_name_class],
			['fill', '#000000'], ['font-size', this._fontTitleSize + 'px']];
	SVG.drawText(left + (nb) * cellWidth, top, maxValue, this._svg, attributes);
};

GridView.prototype.renderTitle = function(top, left, title, dom_id_prefix,
		dom_name_class) {
	this.title = title;
	this.topGrid = top;

	var attributes = [['id', dom_id_prefix], ['class', dom_name_class],
			['fill', '#000000'], ['id', dom_id_prefix],
			['font-size', this._fontTitleSize + 'px']];
	SVG.drawText(left + 100, top, title, this._svg, attributes);
};

GridView.prototype.renderAxisYName = function(top, left, axisYName,
		dom_id_prefix, dom_name_class) {
	this.axisYName = axisYName;
	var attributes = [['id', dom_id_prefix], ['class', dom_name_class],
			['fill', '#000000'], ['id', dom_id_prefix],
			['font-size', this._fontSize + 'px']];
	SVG.drawText(left, top, axisYName, this._svg, attributes);
};

GridView.prototype.renderAxisXName = function(top, left, axisXName,
		dom_id_prefix, dom_name_class) {
	this.axisXName = axisXName;
	var attributes = [['id', dom_id_prefix], ['class', dom_name_class],
			['fill', '#000000'], ['id', dom_id_prefix],
			['font-size', this._fontSize + 'px']];
	SVG.drawText(left, top, axisXName, this._svg, attributes);
};

GridView.prototype.renderRowNames = function(top, left, cellHeight, rowNames,
		dom_id_prefix, dom_name_class) {
	this.rowNames = rowNames;
	this.rowName_id_prefix = dom_id_prefix;
	this.rowName_name_class = dom_name_class;
	// this.topGrid = top;
	for (var i = 0; i < rowNames.length; i++) {

		var attributes = [['id', dom_id_prefix + i], ['class', dom_name_class],
				['fill', '#000000'], 
				['font-size', this._fontSize + 'px']];
		SVG.drawText(left, cellHeight * i + top + (cellHeight / 2),
				rowNames[i], this._svg, attributes);
	}
};

GridView.prototype.renderClientSideColumnNames = function(columnConfiguration) {
	this.renderColumnNames(columnConfiguration.top, columnConfiguration.left,
			columnConfiguration.a, columnConfiguration.b,
			columnConfiguration.cellWidth, this.columnNames,
			columnConfiguration.dom_id_prefix,
			columnConfiguration.dom_name_class);

};

GridView.prototype.renderColumnNames = function(top, left, a, b, cellWidth,
		columnNames, dom_id_prefix, dom_name_class) {
	this.columnConfiguration = new ColumnConfiguration(top, left, a, b,
			cellWidth, columnNames, dom_id_prefix, dom_name_class);
	if (a > b) {
		var aux = a;
		a = b;
		b = aux;
	}
	this.left = left;
	this.columnNames = columnNames;
	this.columnName_id_prefix = dom_id_prefix;
	this.columnHeaderName_name_class = dom_name_class;

	for (var i = a; i <= b; i++) {
		var attributes = [
				[
						'transform',
						'rotate(-45, ' + 
								parseInt(left + i * cellWidth + (cellWidth / 2)) + ',' + 
								parseInt(top) + '), translate(0,' + 
								this.gridOffset + ')'],
				['id', dom_id_prefix + i], ['class', dom_name_class],
				['cursor', 'move'], ['fill', '#000000'],
				['id', dom_id_prefix + i], ['font-size', this._fontSize + 'px']];
		SVG.drawText(left + (i - 1) * cellWidth + (cellWidth / 2), top, columnNames[i], this._svg, attributes);

	}

	// this.columnNames = columnNames;
	// this.columnName_id_prefix = dom_id_prefix;
	// this.columnHeaderName_name_class = dom_name_class;
	// this.topGrid = top;

	// for ( var i = 0; i < columnNames.length; i++) {

	// var attributes = [ [ 'id', dom_id_prefix + i ],[ 'class', dom_name_class
	// ], [ 'fill', '#000000' ],
	// [ 'id', dom_id_prefix + i ],[ 'font-size', this._fontSize + 'px']];
	// SVG.drawText(left + (i-1) * cellWidth + (cellWidth / 2),
	// top,columnNames[i], this._svg, attributes);
	// }

};

GridView.prototype.renderGrid = function(top, left, a, b, cellWidth,
		cellHeight, spanCell, column_id_prefix, column_name_class, data,
		scores, positions, images) {
	this.cellWidth = cellWidth;
	this.cellHeight = cellHeight;
	this.data = data;
	this.scores = scores;
	this.images = images;
	this.positions = positions;
	this.column_id_prefix = column_id_prefix;
	this.column_class = column_name_class;
	this._canvas.width = cellWidth + 1;
	this._canvas.height = cellHeight * this.data.length;
	this.topGrid = top;

	this.renderMatrixGridByCanvas(top, left, a, b, cellWidth, cellHeight,
			spanCell, column_id_prefix, column_name_class, data);

};

GridView.prototype.renderMatrixGridByCanvas = function(top, left, a, b,
		cellWidth, cellHeight, spanCell, column_id_prefix, column_name_class,
		data) {
	this.column_id_prefix = column_id_prefix;
	this.columnName_name_class = column_name_class;

	if (a > b) {
		var aux = a;
		a = b;
		b = aux;
	}
	this._canvas.height = this._canvas.height + 1;
	for (var j = a; j <= b; j++) {

		this._canvas.width = this._canvas.width;
		for (var i = 0; i < this.data.length; i++) {

			this._canvas.getContext('2d').shadowOffsetX = 1;
			this._canvas.getContext('2d').shadowOffsetY = 1;
			this._canvas.getContext('2d').shadowBlur = 0;
			var rectangle = new Rectangle(cellHeight * i + 1, 1, cellHeight - spanCell, cellWidth - spanCell);
			rectangle.setColor(data[i][j]);
			rectangle.render(this._canvas.getContext('2d'));
		}
		this.columnIndex.push(column_id_prefix + j);
		this.columnPosX.push((left + j * cellWidth));

		CanvasToSVG.convert(this._canvas, this._svg, (left + j * cellWidth),
				top + 1, column_id_prefix + j, [["cursor", "pointer"],
						["class", column_name_class]]);
	}

	this._canvas.width = 0;
	this._canvas.height = 0;

};

GridView.prototype.renderMatrixGridBySVG = function(top, left, a, b, cellWidth,
		cellHeight, spanCell, column_id_prefix, column_name_class, data) {
	if (a > b) {
		var aux = a;
		a = b;
		b = aux;
	}
	var obj = 0;
	for (var j = a; j <= b; j++) {
		this._canvas.width = this._canvas.width;
		for (var i = 0; i < this.data.length; i++) {
			spanCell = 0;
			SVG.drawRectangle(left + j * cellWidth, cellHeight * i + top,
					data[i][j], cellHeight - spanCell, cellWidth - spanCell, 1,
					column_id_prefix + j, column_name_class, this._svg);

			obj++;
		}
	}

	this._canvas.width = 0;
	this._canvas.height = 0;

};

GridView.prototype.loadBackgroundImage = function(backgroundImage, top, left) {
	if (backgroundImage) {
		var svgimg = document.createElementNS('http://www.w3.org/2000/svg',
				'image');
		svgimg.setAttributeNS(null, 'height', '197');
		svgimg.setAttributeNS(null, 'width', '316');
		svgimg.setAttributeNS('http://www.w3.org/1999/xlink', 'href',
				backgroundImage);
		svgimg.setAttributeNS(null, 'x', left);
		svgimg.setAttributeNS(null, 'y', top);
		svgimg.setAttributeNS(null, 'visibility', 'visible');
		svgimg.setAttributeNS(null, 'id', 'backgroundImage');
		svgimg.setAttributeNS(null, 'opacity', 0.5);
		this._svg.appendChild(svgimg);
	}
};

function ColumnConfiguration(top, left, a, b, cellWidth, columnNames,
		dom_id_prefix, dom_name_class) {
	this.top = top;
	this.left = left;
	this.a = a;
	this.b = b;
	this.cellWidth = cellWidth;
	this.columnNames = columnNames;
	this.dom_id_prefix = dom_id_prefix;
	this.dom_name_class = dom_name_class;
}

// hopefully return the mouse coordinates inside parent element
function getMouseCoords(e, parent) {
	var x, y;

	muna = parent;

	if (document.getBoxObjectFor) {
		// sorry for the deprecated use here, but see below
		var boxy = document.getBoxObjectFor(parent);
		x = e.pageX - boxy.x;
		y = e.pageY - boxy.y;
	} else if (parent.getBoundingClientRect) {
		// NOTE: buggy for FF 3.5:
		// https://bugzilla.mozilla.org/show_bug.cgi?id=479058
		/*
		 * I have also noticed that the returned coordinates may change
		 * unpredictably after the DOM is modified by adding some children to
		 * the SVG element
		 */
		// var lefttop = parent.getBoundingClientRect();
		// console.log(parent.id + " " + lefttop.left + " " + lefttop.top);
		// console.log(e.clientX+" "+e.clientY);
		// console.log(parent.offsetLeft+" "+parent.offsetTop);
		// x = e.clientX - Math.floor(lefttop.left);
		// y = e.clientY - Math.floor(lefttop.top);
		var pt = parent.createSVGPoint();
		pt.x = e.clientX;
		pt.y = e.clientY;
		var cursorpt = pt.matrixTransform(parent.getScreenCTM().inverse());
		x = cursorpt.x;
		y = cursorpt.y;
		// console.log("(" + cursorpt.x + ", " + cursorpt.y + ")");

	} else {
		x = e.pageX - (parent.offsetLeft || 0);
		y = e.pageY - (parent.offsetTop || 0);
	}

	// console.log(x +" " +y+" " );
	return {
		x : x,
		y : y
	};
}
