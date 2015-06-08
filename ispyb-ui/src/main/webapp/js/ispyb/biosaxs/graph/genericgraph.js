function GenericGraph(args) {
	this.width = 600;
	this.height = 400;

	this.targetId = null;
	/** Free spaces in the borders * */
	this.top = 10;
	this.left = 10;
	this.bottom = 50;
	this.right = 40;

	/** Ruler * */
	this.rulerHeight = 50;
	this.rulerWidth = 50;
	this.rulerStroke = 2;
	this.rulerVerticalMarksNumber = 5;

	this.rulerMaxValue = null;
	this.rulerMinValue = null;

	/** plot options * */
	this.plotPoints = true;
	this.pointRadius = 2;
	this.fillOpacityPoint = 0.2;
	this.strokeOpacityPoint = 0.2;

	/** Cluster titles * */
	this.clusterTitleHeight = 20;
	this.interClassesSpace = 2;
	this.interClustersSpace = 4;
	this.fontSize = 7;

	/**
	 * If true classes and title will be rendender in the rule otherwise will be
	 * integer values
	 */
	this.plotHorizontalByCluster = true;

	if (args != null) {
		if (args.targetId != null) {
			this.targetId = args.targetId;
		}
		if (args.plotHorizontalByCluster != null) {
			this.plotHorizontalByCluster = args.plotHorizontalByCluster;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.rulerMinValue != null) {
			this.rulerMinValue = args.rulerMinValue;
		}
		if (args.rulerMaxValue != null) {
			this.rulerMaxValue = args.rulerMaxValue;
		}
		if (args.rulerHeight != null) {
			this.rulerHeight = args.rulerHeight;
		}

	}
}

GenericGraph.prototype.calculate = function(data) {
	var result = {};

	var checked = this.cleanArray(data);

	/** sorting array * */
	checked.sort(function(a, b) {
		return a - b;
	});

	var median = this.getMedian(checked);

	result.median = median;
	result.Q1 = Number(this.getQ1(checked));
	result.Q2 = Number(this.getQ2(checked));
	result.Q3 = Number(this.getQ3(checked));
	result.population = checked;

	result.IQR = Number(result.Q3 - result.Q1);
	result.outlier_below_limit = result.Q1 - (1.5 * result.IQR);
	result.outlier_above_limit = result.Q3 + (1.5 * result.IQR);
	result.below_outliers = this.getBelowOutliers(result.outlier_below_limit, checked);
	result.above_outliers = this.getAboveOutliers(result.outlier_above_limit, checked);

	result.min_whisker = this.minValueWhisker(result.outlier_below_limit, result.Q1, checked);
	result.max_whisker = this.maxValueWhisker(result.outlier_above_limit, result.Q3, checked);

	return result;
};

GenericGraph.prototype.drawSVGVerticalText = function(x, y, text, properties) {
	var transform = [ "transform", "translate(" + x + ", " + y + "), rotate(-90) " ];
	properties.push(transform);
	SVG.drawText(0, 0, text, this.svg, properties);
};

/** Plot the numbers on the axis * */
GenericGraph.prototype.plotRuler = function(rulerProperties) {

	SVG.drawRectangle(rulerProperties.vertical.x, rulerProperties.vertical.y, rulerProperties.vertical.width, rulerProperties.vertical.height,
			this.svg, [ [ "fill", "black" ] ]);
	var distance = rulerProperties.vertical.height / (this.rulerVerticalMarksNumber + 1);
	for ( var i = 0; i <= this.rulerVerticalMarksNumber + 1; i++) {
		var deltaHeight = distance * i;
		var aux = rulerProperties.vertical.height - deltaHeight;

		var text = Number((aux / rulerProperties.vertical.height) * (rulerProperties.maxPoint - rulerProperties.minPoint) + rulerProperties.minPoint)
				.toFixed(3);

		SVG.drawLine(rulerProperties.horizontal.x, this.top + deltaHeight, rulerProperties.horizontal.x - rulerProperties.markWidth, 
				this.top + deltaHeight, this.svg, [ [ "stroke", "black" ] ]);
		SVG.drawText(this.left, this.top + distance * i + 5, text, this.svg, [ [ "style", "font-size:xx-small" ] ]);
		/** Drawing the mark up to the end * */
		SVG.drawLine(rulerProperties.horizontal.x, this.top + deltaHeight, this.width - this.right, this.top + deltaHeight, this.svg, [
			[ "stroke", rulerProperties.markColor ], [ "stroke-width", "0.3" ] ]);

		if (i == this.rulerVerticalMarksNumber + 1) {
			SVG.drawLine(rulerProperties.horizontal.x, this.top + deltaHeight, this.width - this.right, this.top + deltaHeight, this.svg, [ [
				"stroke", rulerProperties.markColor ] ]);
		}
	}

	/** Drawing horizontal rulers * */
	if (!this.plotHorizontalByCluster) {
		var width = rulerProperties.horizontal.width;
		var ratio = width / (rulerProperties.horizontal.xValues.range);
		for ( i = 0; i < rulerProperties.horizontal.xValues.values.length; i++) {
			var coorX = rulerProperties.horizontal.xValues.values[i] - rulerProperties.horizontal.xValues.min;
			SVG.drawText(rulerProperties.horizontal.x + coorX * ratio, this.height - (this.bottom + (this.clusterTitleHeight)),
					rulerProperties.horizontal.xValues.values[i], this.svg, [ [ "style", "font-size:small" ] ]);
		}
	}
};

GenericGraph.prototype.plotAxes = function(properties) {
	/**
	 * Drawing canvas plot-free spaces SVG.drawRectangle(0, this.top, this.left,
	 * this.height, this.svg, [["fill", "pink"]]); SVG.drawRectangle(this.width -
	 * this.right, this.top, plot.width, plot.height, this.svg, [["fill",
	 * "pink"]]); SVG.drawRectangle(0, 0, this.width, this.top, this.svg,
	 * [["fill", "red"]]); SVG.drawRectangle(0, this.height - this.bottom,
	 * this.width, this.bottom, this.svg, [["fill", "red"]]);
	 */

	/** Drawing ruler Space * */
	this.plotRuler({
		minPoint : Number(properties.minPoint),
		maxPoint : Number(properties.maxPoint),
		markColor : "black",
		markWidth : 20,
		vertical : {
			x : this.left + this.rulerWidth - this.rulerStroke,
			y : this.top,
			width : this.rulerStroke,
			height : this.height - (this.top + this.bottom + this.clusterTitleHeight) - this.rulerHeight
		},
		horizontal : {
			x : this.left + this.rulerWidth - this.rulerStroke,
			y : this.height - (this.bottom + this.clusterTitleHeight + this.rulerHeight),
			width : properties.width,
			height : this.rulerStroke,
			xValues : properties.xValues
		}
	});

};

/** Remove nulls and NaN elements in the array * */
GenericGraph.prototype.cleanArray = function(data) {
	var checked = [];

	/** checking data are numbers * */
	for ( var i = 0; i < data.length; i++) {
		if (data[i] != null) {
			if (!isNaN(data[i])) {
				checked.push(data[i]);
			}
		}
	}
	return checked;
};

GenericGraph.prototype.refresh = function(data) {
	document.getElementById(this.targetId).innerHTML = "";
	this.draw(this.targetId, data);
};

GenericGraph.prototype.getClassColor = function(className) {
	for ( var i = 0; i < this.data.clusters.length; i++) {
		var cluster = this.data.clusters[i];
		for ( var j = 0; j < cluster.classes.length; j++) {
			var classes = cluster.classes[j];
			if (classes.name == className) {
				if (classes.color != null) {
					return classes.color;
				}
			}
		}
	}
	return "black";
};

GenericGraph.prototype.getDimensions = function(data) {
	var results = {};
	var points = [];

	this.data = data;
	var classesNumber = 0;
	var xValues = [];

	for ( var i = 0; i < data.clusters.length; i++) {
		var cluster = data.clusters[i];
		if (!this.plotHorizontalByCluster) {
			xValues.push(data.clusters[i].x);
		}
		for ( var j = 0; j < cluster.classes.length; j++) {
			var classes = cluster.classes[j];
			points = points.concat(classes.values);
			classesNumber = classesNumber + 1;
		}
	}

	var checked = this.cleanArray(points);

	checked.sort(function(a, b) {
		return a - b;
	});

	results.minPoint = checked[0];
	if (this.rulerMinValue != null) {
		results.minPoint = this.rulerMinValue;
	}
	results.maxPoint = checked[checked.length - 1];
	if (this.rulerMaxValue != null) {
		results.maxPoint  = this.rulerMaxValue;
	}

	results.classesNumber = classesNumber;
	results.clusterNumber = data.clusters.length;

	var totalInterClassesFreeSpace = (classesNumber - data.clusters.length) * this.interClassesSpace;
	var totalInterClusterFreeSpace = (data.clusters.length) * this.interClustersSpace;
	results.classWidth = (this.width - (this.rulerWidth + this.left + this.right + totalInterClassesFreeSpace + totalInterClusterFreeSpace))/ classesNumber;
	results.width = this.width - (this.right + this.left) - this.rulerWidth + this.rulerStroke;

	results.xValues = {};

	xValues.sort(function(a, b) {
		return a - b;
	});
	results.xValues.values = xValues;
	if (xValues.length > 0) {
		results.xValues.min = xValues[0];
		results.xValues.max = xValues[xValues.length - 1];
		results.xValues.range = results.xValues.max - results.xValues.min;
	}
	return results;

};

GenericGraph.prototype.draw = function(targetId, data) {
	this.targetId = targetId;
	var properties = (this.getDimensions(data));
	this.svg = SVG.createSVGCanvas(document.getElementById(this.targetId), [ [ "width", this.width ], [ "height", this.height ] ]);
	this.plotAxes(properties);
};

GenericGraph.prototype.getMedian = function(checked) {
	/** Calculating median * */
	if (checked.length % 2 == 1) {
		return checked[Math.floor(checked.length / 2)];
	} else {
		return (Number(checked[(checked.length / 2) - 1]) + Number(checked[checked.length / 2])) / 2;
	}
};

GenericGraph.prototype.pointToPixel = function(value, boxProperties) {
	var ratio = (value - boxProperties.minPoint) / (boxProperties.maxPoint - boxProperties.minPoint);
	var pixelLength = boxProperties.height - boxProperties.y + this.top;
	return (-1 * ratio) * (pixelLength) + (boxProperties.y + boxProperties.height);
};

GenericGraph.prototype.maxValueWhisker = function(aboveLimit, q3, array) {
	var points = [];
	for ( var i = 0; i < array.length; i++) {
		if ((array[i] > q3) && (array[i]) <= aboveLimit) {
			points.push(array[i]);
		}
	}
	if (points.length > 0) {
		points.sort(function(a, b) {
			return a - b;
		});
		return points[points.length - 1];
	}
	return null;
};

GenericGraph.prototype.input = function() {
	return DATADOC.getBoxWhikerData();
};

GenericGraph.prototype.test = function(targetId) {
	var plot = new GenericGraph({
		targetId : targetId,
		height : 350,
		width : 450,
		maxBoxWidth : 25
	});
	plot.refresh(this.input());
};
