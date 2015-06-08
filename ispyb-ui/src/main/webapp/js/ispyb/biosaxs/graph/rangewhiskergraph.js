RangeWhiskerGraph.prototype.cleanArray = GenericGraph.prototype.cleanArray;
RangeWhiskerGraph.prototype.plotAxes = GenericGraph.prototype.plotAxes;
RangeWhiskerGraph.prototype.plotRuler = GenericGraph.prototype.plotRuler;
RangeWhiskerGraph.prototype.drawSVGVerticalText = GenericGraph.prototype.drawSVGVerticalText;
RangeWhiskerGraph.prototype.getClassColor = GenericGraph.prototype.getClassColor;
RangeWhiskerGraph.prototype.getDimensions = GenericGraph.prototype.getDimensions;
RangeWhiskerGraph.prototype.calculate = GenericGraph.prototype.calculate;
RangeWhiskerGraph.prototype.getMedian = GenericGraph.prototype.getMedian;
RangeWhiskerGraph.prototype.pointToPixel = GenericGraph.prototype.pointToPixel;
RangeWhiskerGraph.prototype.maxValueWhisker = GenericGraph.prototype.maxValueWhisker;
RangeWhiskerGraph.prototype.refresh = GenericGraph.prototype.refresh;

/**
 * Subclass of GenericGraph
 * 
 * @plotHorizontalByCluster
 */
function RangeWhiskerGraph(args) {
	this.maxBoxWidth = 25;

	if (args == null) {
		args = {};
	}
	args.plotHorizontalByCluster = false;

	GenericGraph.call(this, args);

	if (args.maxBoxWidth != null) {
		this.maxBoxWidth = args.maxBoxWidth;
	}
}

RangeWhiskerGraph.prototype.refresh = function(data) {
	document.getElementById(this.targetId).innerHTML = "";
	this.draw(this.targetId, data);
};

RangeWhiskerGraph.prototype.isNumber = function(value) {
	if (value == "")
		return false;

	var d = parseInt(value);
	if (!isNaN(d))
		return true;
	else
		return false;

};

/**
 There are several different methods for calculating quartiles.[1] This calculator uses a method described by Moore and McCabe to find quartile values. 
 The same method also used by The TI-83 to calculate quartile values. 
 With this method, the first quartile is the median of the numbers below the median, the third quartile is the median of the numbers above the median.

 http://www.miniwebtool.com/quartile-calculator/
 http://www.alcula.com/calculators/statistics/box-plot/

 **/
RangeWhiskerGraph.prototype.getQ1 = function(array) {
	array = array.slice(0, array.length / 2);
	return this.getMedian(array);
};

RangeWhiskerGraph.prototype.getQ3 = function(array) {
	array = array.slice((array.length + 1) / 2);
	return this.getMedian(array);

};

RangeWhiskerGraph.prototype.getQ2 = function(array) {
	return this.getMedian(array);
};

RangeWhiskerGraph.prototype.getBelowOutliers = function(belowLimit, array) {
	var points = [];
	for ( var i = 0; i < array.length; i++) {
		if (array[i] <= belowLimit) {
			points.push(array[i]);
		}
	}
	return points;
};

RangeWhiskerGraph.prototype.getAboveOutliers = function(aboveLimit, array) {
	var points = [];
	for ( var i = 0; i < array.length; i++) {
		if (array[i] >= aboveLimit) {
			points.push(array[i]);
		}
	}
	return points;
};

RangeWhiskerGraph.prototype.minValueWhisker = function(belowLimit, q1, array) {
	var points = [];

	for ( var i = 0; i < array.length; i++) {
		if ((array[i] < q1) && (array[i]) > belowLimit) {
			points.push(array[i]);
		}
	}

	if (points.length > 0) {
		points.sort(function(a, b) {
			return a - b;
		});
		return points[0];
	}
	return null;
};

//RangeWhiskerGraph.prototype.maxValueWhisker = function(aboveLimit, q3, array){
//	var points = [];
//	for (var i = 0; i < array.length; i++){
//		if ((array[i] > q3) && (array[i]) <= aboveLimit){
//			points.push(array[i]);
//		}
//	}
//	if (points.length > 0){
//		points.sort(function(a, b){return a - b;});
//		return points[points.length - 1];
//	}
//	return null;
//};

RangeWhiskerGraph.prototype.drawPoints = function(boxProperties) {
	if (this.plotPoints) {
		for ( var i = 0; i < boxProperties.values.length; i++) {
			var value = boxProperties.values[i];
			var toPixel = this.pointToPixel(value, boxProperties);
			SVG.drawCircle(boxProperties.x, toPixel, this.pointRadius, this.svg,
					[
						[ "fill", "green" ], [ "fill-opacity", this.fillOpacityPoint ], [ 'stroke-opacity', this.strokeOpacityPoint ],
						[ "stroke", "black" ] ]);
		}
	}
};

RangeWhiskerGraph.prototype.plotRangeQ1Q3 = function(data, properties) {
	var posX = this.left + this.rulerWidth;

	var boxBordersPointsQ1 = [];
	var boxBordersPointsQ3 = [];
	var Q2 = [];

	for ( var i = 0; i < data.clusters.length; i++) {
		var cluster = data.clusters[i];
		/** inter cluster space **/
		posX = posX + this.interClustersSpace;

		for ( var j = 0; j < cluster.classes.length; j++) {
			var ratio = properties.width / (properties.xValues.range);
			var coorX = (cluster.x - properties.xValues.min) * ratio + this.rulerWidth;
			var boxProperties = {
				name : cluster.classes[j].name,
				values : cluster.classes[j].values,
				minPoint : properties.minPoint,
				maxPoint : properties.maxPoint,
				x : coorX + this.left,
				y : this.top,
				width : properties.classWidth,
				height : this.height - this.top - this.bottom - this.clusterTitleHeight - this.rulerHeight
			};

			var result = this.calculate(boxProperties.values);
			var boxColor = this.getClassColor(boxProperties.name);

			if (this.isNumber(result.Q1) && this.isNumber(result.Q3)) {
				var x = boxProperties.x;
				var y = this.pointToPixel(result.Q1, boxProperties);
				if (boxBordersPointsQ1[cluster.classes[j].name] == null) {
					boxBordersPointsQ1[cluster.classes[j].name] = [];
				}
				boxBordersPointsQ1[cluster.classes[j].name].push({
					x : x,
					y : y
				});

				x = boxProperties.x;
				y = this.pointToPixel(result.Q3, boxProperties);
				if (boxBordersPointsQ3[cluster.classes[j].name] == null) {
					boxBordersPointsQ3[cluster.classes[j].name] = [];
				}
				boxBordersPointsQ3[cluster.classes[j].name].push({
					x : x,
					y : y
				});
			} else {

				if (this.isNumber(result.Q2)) {

					if (boxBordersPointsQ1[cluster.classes[j].name] == null) {
						boxBordersPointsQ1[cluster.classes[j].name] = [];
					}

					if (boxBordersPointsQ3[cluster.classes[j].name] == null) {
						boxBordersPointsQ3[cluster.classes[j].name] = [];
					}

					boxBordersPointsQ1[cluster.classes[j].name].push({
						x : boxProperties.x,
						y : this.pointToPixel(result.Q2, boxProperties)
					});
					boxBordersPointsQ3[cluster.classes[j].name].push({
						x : boxProperties.x,
						y : this.pointToPixel(result.Q2, boxProperties)
					});
				}
			}
		}
	}
	for ( var classe in boxBordersPointsQ1) {
		var points = boxBordersPointsQ1[classe];
		var pointsSVG = "";
		for (var k = 0; k < points.length; k++) {
			pointsSVG = Number(points[k].x).toFixed(1) + "," + Number(points[k].y).toFixed(1) + " " + pointsSVG;
		}

		points = boxBordersPointsQ3[classe];
		for (var z = points.length - 1; z >= 0; z--) {
			pointsSVG = Number(points[z].x).toFixed(1) + "," + Number(points[z].y).toFixed(1) + " " + pointsSVG;
		}

		SVG.drawPoligon(pointsSVG, this.svg, [
			[ "fill", this.getClassColor(classe) ], [ "opacity", "0.3" ], [ "stroke", "black" ], [ "stroke-width", 1 ] ]);
	}
};

RangeWhiskerGraph.prototype.plotWhisters = function(data, properties) {
	var colors = [ "yellow", "orange", "green" ];

	this.plotRangeQ1Q3(data, properties);
	var Q2 = {};

	for ( var i = 0; i < data.clusters.length; i++) {
		var cluster = data.clusters[i];
		for ( var j = 0; j < cluster.classes.length; j++) {
			var ratio = properties.width / (properties.xValues.range);
			var coorX = (cluster.x - properties.xValues.min) * ratio + this.left + this.rulerWidth - this.rulerStroke;
			var boxProperties = {
				name : cluster.classes[j].name,
				values : cluster.classes[j].values,
				minPoint : properties.minPoint,
				maxPoint : properties.maxPoint,
				x : coorX,
				y : this.top,
				width : properties.classWidth,
				height : this.height - this.top - this.bottom - this.clusterTitleHeight - this.rulerHeight
			};

			this.drawPoints(boxProperties);

			/** PLOTTING Q2 **/
			var result = this.calculate(boxProperties.values);
			var boxColor = this.getClassColor(boxProperties.name);
			if (this.isNumber(result.Q2)) {
				if (Q2[boxProperties.name] != null) {
					SVG.drawLine(boxProperties.x, this.pointToPixel(result.Q2, boxProperties), Q2[boxProperties.name].x, Q2[boxProperties.name].y,
							this.svg, [ [ "stroke", boxColor ], [ "stroke-width", "2" ] ]);
				}
				Q2[boxProperties.name] = {
					x : boxProperties.x,
					y : this.pointToPixel(result.Q2, boxProperties)
				}
			}
		}
	}

};

RangeWhiskerGraph.prototype.draw = function(targetId, data) {
	//this.calculate(data);
	this.targetId = targetId;
	var properties = (this.getDimensions(data));
	this.svg = SVG.createSVGCanvas(document.getElementById(this.targetId), [ [ "width", this.width ], [ "height", this.height ] ]);
	this.plotAxes(properties);
	this.plotWhisters(data, properties);
};

RangeWhiskerGraph.prototype.input = function() {
	return DATADOC.getBoxWhikerData();
};

RangeWhiskerGraph.prototype.test = function(targetId) {
	var plot = new RangeWhiskerGraph({
		targetId : targetId,
		height : 350,
		width : 450,
		maxBoxWidth : 25
	});
	plot.refresh(this.input());
};
