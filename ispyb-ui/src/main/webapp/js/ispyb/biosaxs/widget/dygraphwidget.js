/**
 * Using dygraph it plots a chart. Params: targetId, labelsContainerId, args
 * 
 * @width
 * @height
 * @labelsWidth
 * @targetId
 * @customBars
 * @ylabel
 * @xlabel
 * @showRangeSelector Show or hide the range selector widget.
 */
function DygraphWidget(targetId, args) {
	this.width = 1000;
	this.height = 600;
	this.labelsWidth = 100;
	this.targetId = targetId;
	this.customBars = false;
	this.ylabel = "";
	this.xlabel = "";
	this.id = BUI.id();
	this.showRangeSelector = false;
	this.interactionModel = null;
	this.labelsDivStyles = null;

	this.ranges = {};

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}

		if (args.labelsWidth != null) {
			this.labelsWidth = args.labelsWidth;
		}
		if (args.labelsDivStyles != null) {
			this.labelsDivStyles = args.labelsDivStyles;
		}
		if (args.customBars != null) {
			this.customBars = args.customBars;
		}
		if (args.ylabel != null) {
			this.ylabel = args.ylabel;
		}
		if (args.xlabel != null) {
			this.xlabel = args.xlabel;
		}
		if (args.showRangeSelector != null) {
			this.showRangeSelector = args.showRangeSelector;
		}
		if (args.interactionModel != null) {
			this.interactionModel = args.interactionModel;
		}

		if (args.scaled != null) {
			this.scaled = args.scaled;
		}
		if (args.ranges != null) {
			this.ranges = args.ranges;
		}

	}

	this.onZoomX = new Event(this);
	this.onResetZoom = new Event(this);
	this.dblclick = new Event(this);
}

/** Draws it on targetId 
 * data: dygraphs.com/data.html
 * 
 * **/

DygraphWidget.prototype.dblclick = function(event, g, context) {
	g.widget.dblclick.notify({
		x : g.lastx_
	});
};

DygraphWidget.prototype._createHTLMWrapper = function(data, colors, labels) {
	/** If plot is set in a tab it is possible that it is not renderer yet **/
	if (document.getElementById(this.targetId) == null) {
		return;
	}
	document.getElementById(this.targetId).innerHTML = "";
	/** Creating legend in a table **/
	var table = document.createElement("table");
	var tr = document.createElement("tr");
	var tdCanvas = document.createElement("td");

	this.canvasDiv = document.createElement("div");
	this.canvasDiv.setAttribute("id", "dygraph_canvas_" + this.id);
	this.canvasDiv.setAttribute("style", "width:" + this.width + "px;height:" + this.height + "px");
	tdCanvas.appendChild(this.canvasDiv);

	this.legendDiv = document.createElement("div");
	tr.appendChild(tdCanvas);
	table.appendChild(tr);
	document.getElementById(this.targetId).appendChild(table);
};

DygraphWidget.prototype.draw = function(data, colors, labels) {
	this._createHTLMWrapper(data, colors, labels);
	this.dygraph = new Dygraph(this.canvasDiv, data, {
		labels : labels,
		labelsDiv : this.legendDiv,
		labelsSeparateLines : true,
		highlightCircleSize : 3,
		strokeWidth : 1,
		customBars : this.customBars,
		colors : colors,
		scaled : this.scaled,
		ranges : this.ranges,
		xlabel : this.xlabel,
		ylabel : this.ylabel,
		showRangeSelector : this.showRangeSelector,
		rangeSelectorPlotStrokeColor : 'rgba(50,50,50,0.3)',
		rangeSelectorPlotFillColor : 'rgba(50,50,50,0.1)',
		labelsDivStyles : this.labelsDivStyles,
		interactionModel : this.interactionModel
	});

};
