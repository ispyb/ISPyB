StdDevDyGraph.prototype.dblclick = DygraphWidget.prototype.dblclick;
StdDevDyGraph.prototype._createHTLMWrapper = DygraphWidget.prototype._createHTLMWrapper;
StdDevDyGraph.prototype.draw = DygraphWidget.prototype.draw;

function StdDevDyGraph(targetId, args) {
	this.scaled = false;
	if (args == null) {
		args = {};
	}
	args.customBars = true;
	DygraphWidget.prototype.constructor.call(this, targetId, args);
}

StdDevDyGraph.prototype.input = function () {
	return {
		data : [ [ 1, [ 2, 3, 3.5 ], [ 4, 4.2, 5 ] ], [ 2, [ 5, 5.5, 5.7 ], [ 4, 4.2, 5 ] ] ],
		colors : [ "blue", "red" ],
		labels : [ "", 'data1', 'data2' ]
	};
};

StdDevDyGraph.prototype.test = function (targetId) {
	var dygraphObject = new StdDevDyGraph(targetId, {
		width : 500,
		height : 400,
		xlabel : "xLabel",
		showRangeSelector : false
	});

	dygraphObject.draw(dygraphObject.input().data, dygraphObject.input().colors, dygraphObject.input().labels);
};
