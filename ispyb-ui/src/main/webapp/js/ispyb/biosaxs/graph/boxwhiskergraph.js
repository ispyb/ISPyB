
BoxWhiskerGraph.prototype.cleanArray = GenericGraph.prototype.cleanArray;
BoxWhiskerGraph.prototype.plotAxes = GenericGraph.prototype.plotAxes;
BoxWhiskerGraph.prototype.plotRuler = GenericGraph.prototype.plotRuler;
BoxWhiskerGraph.prototype.drawSVGVerticalText = GenericGraph.prototype.drawSVGVerticalText;
BoxWhiskerGraph.prototype.getClassColor = GenericGraph.prototype.getClassColor;
BoxWhiskerGraph.prototype.getDimensions = GenericGraph.prototype.getDimensions;
BoxWhiskerGraph.prototype.calculate = GenericGraph.prototype.calculate;
BoxWhiskerGraph.prototype.getMedian = GenericGraph.prototype.getMedian;
BoxWhiskerGraph.prototype.pointToPixel = GenericGraph.prototype.pointToPixel;
BoxWhiskerGraph.prototype.maxValueWhisker = GenericGraph.prototype.maxValueWhisker;
BoxWhiskerGraph.prototype.refresh = GenericGraph.prototype.refresh;

/**
 * Subclass of GenericGraph
 * 
 * @maxBoxWidth
 */
function BoxWhiskerGraph(args){
	this.maxBoxWidth = 25;
	
	if (args == null){
		args = new Object();
	}
	args["plotHorizontalByCluster"] = true;
	
	GenericGraph.call(this, args);
	
	if (args.maxBoxWidth != null){
		this.maxBoxWidth = args.maxBoxWidth;
	}
};



/**
There are several different methods for calculating quartiles.[1] This calculator uses a method described by Moore and McCabe to find quartile values. 
The same method also used by The TI-83 to calculate quartile values. 
With this method, the first quartile is the median of the numbers below the median, the third quartile is the median of the numbers above the median.

http://www.miniwebtool.com/quartile-calculator/
http://www.alcula.com/calculators/statistics/box-plot/
**/
BoxWhiskerGraph.prototype.getQ1 = function(array){
		array = array.slice(0, array.length/2);
		return this.getMedian(array);	
};

BoxWhiskerGraph.prototype.getQ3 = function(array){
		array = array.slice((array.length + 1)/2);
		return this.getMedian(array);
			
};

BoxWhiskerGraph.prototype.getQ2 = function(array){
	return  this.getMedian(array);
};

BoxWhiskerGraph.prototype.getBelowOutliers = function(belowLimit, array){
	var points = new Array();
	for (var i = 0; i < array.length; i++){
		if (array[i] <= belowLimit){
			points.push(array[i]);
		}
	}
	return points;
};

BoxWhiskerGraph.prototype.getAboveOutliers = function(aboveLimit, array){
	var points = new Array();
	for (var i = 0; i < array.length; i++){
		if (array[i] >= aboveLimit){
			points.push(array[i]);
		}
	}
	return points;
};


BoxWhiskerGraph.prototype.minValueWhisker = function(belowLimit, q1, array){
	var points = new Array();
	
	for (var i = 0; i < array.length; i++){
		if ((array[i] < q1) && (array[i]) > belowLimit){
			points.push(array[i]);
		}
	}
	
	if (points.length > 0){
		points.sort(function(a, b){return a - b;});
		return points[0];
	}
	return null;
};

BoxWhiskerGraph.prototype.isNumber = function(value){
	if (value=="") return false;

	var d = parseInt(value);
	if (!isNaN(d)) return true; else return false;		

};

BoxWhiskerGraph.prototype.plotBoxWhisker = function(boxProperties){
		if (this.maxBoxWidth != null){
			if (boxProperties.width > this.maxBoxWidth){
				boxProperties.x = boxProperties.x + (boxProperties.width/2) - (this.maxBoxWidth/2);
				boxProperties.width = this.maxBoxWidth;
			}
			
		}

		if (this.plotPoints){
			for(var i = 0; i < boxProperties.values.length; i++){
				var value = boxProperties.values[i];
				var toPixel = this.pointToPixel(value, boxProperties);
				SVG.drawCircle(boxProperties.x +  (boxProperties.width/2) + 2, toPixel, this.pointRadius, this.svg, [["fill", "green"], ["fill-opacity", this.fillOpacityPoint],['stroke-opacity', this.strokeOpacityPoint], ["stroke", "black"]]);	
			}
		}
		

		var boxColor = this.getClassColor(boxProperties.name);
		var result = this.calculate(boxProperties.values);
		/** Q1 **/
		
		if (this.isNumber(result.Q1)){
			SVG.drawLine(boxProperties.x, this.pointToPixel(result.Q1, boxProperties), boxProperties.x + boxProperties.width, this.pointToPixel(result.Q1, boxProperties), this.svg, [["stroke", boxColor]]);
		}
		/** Q2 **/
		if (this.isNumber(result.Q2)){
			SVG.drawLine(boxProperties.x, this.pointToPixel(result.Q2, boxProperties), boxProperties.x + boxProperties.width, this.pointToPixel(result.Q2, boxProperties), this.svg, [["stroke", "blue"], ["stroke-width", "2"]]);
		}
		
		/** Q3 **/
		if (this.isNumber(result.Q3)){
			SVG.drawLine(boxProperties.x, this.pointToPixel(result.Q3, boxProperties), boxProperties.x + boxProperties.width, this.pointToPixel(result.Q3, boxProperties), this.svg, [["stroke", boxColor]]);
		}
		
		/** Concenting Q1 and Q3 **/
		if (this.isNumber(result.Q1)&&this.isNumber(result.Q3)){
			SVG.drawLine(boxProperties.x, this.pointToPixel(result.Q1, boxProperties), boxProperties.x, this.pointToPixel(result.Q3, boxProperties), this.svg, [["stroke", boxColor]]);
			SVG.drawLine(boxProperties.x + boxProperties.width, this.pointToPixel(result.Q1, boxProperties), boxProperties.x + boxProperties.width, this.pointToPixel(result.Q3, boxProperties), this.svg, [["stroke", boxColor]]);
		}
		
		/** min-whisker **/
		if (result["min-whisker"] != null){
			if (this.isNumber(result.Q1)){
				SVG.drawLine(boxProperties.x + (boxProperties.width/2) + 2, this.pointToPixel(result.Q1, boxProperties), boxProperties.x + (boxProperties.width/2) + 2, this.pointToPixel(result["min-whisker"], boxProperties), this.svg, [["stroke", boxColor]]);
			}
			SVG.drawLine(boxProperties.x,this.pointToPixel(result["min-whisker"], boxProperties), boxProperties.x + boxProperties.width, this.pointToPixel(result["min-whisker"], boxProperties) , this.svg, [["stroke", boxColor]]);
			
		}
		
		/** max-whisker **/
		if (result["max-whisker"] != null){
			if (this.isNumber(result.Q3)){
				SVG.drawLine(boxProperties.x + (boxProperties.width/2) + 2, this.pointToPixel(result.Q3, boxProperties), boxProperties.x + (boxProperties.width/2) + 2, this.pointToPixel(result["max-whisker"], boxProperties), this.svg, [["stroke", boxColor]]);
			}
			SVG.drawLine(boxProperties.x  , this.pointToPixel(result["max-whisker"], boxProperties), boxProperties.x + boxProperties.width, this.pointToPixel(result["max-whisker"], boxProperties), this.svg, [["stroke", boxColor]]);
		
		}
		
		/** outliners **/
		if (result["above-outliers"] != null){
			for (var point in result["above-outliers"]){
				SVG.drawCircle(boxProperties.x +  (boxProperties.width/2) + 2, this.pointToPixel(result["above-outliers"][point], boxProperties), this.pointRadius, this.svg, [["fill", "red"], ["fill-opacity", "1"]]);
				//SVG.drawText(boxProperties.x +  (boxProperties.width/2) - 2, this.pointToPixel(result["above-outliers"][point], boxProperties) + 2, "x",this.svg, [["fill", "black"], ["font-size", "x-small"]]);
			}
		}
		if (result["below-outliers"] != null){
			for (var point in result["below-outliers"]){
					SVG.drawCircle(boxProperties.x +  (boxProperties.width/2) + 2, this.pointToPixel(result["below-outliers"][point], boxProperties), this.pointRadius, this.svg, [["fill", "red"], ["fill-opacity", "1"]]);	
					//SVG.drawText(boxProperties.x +  (boxProperties.width/2) - 2, this.pointToPixel(result["below-outliers"][point], boxProperties) + 2, "x",this.svg, [["fill", "black"], ["font-size", "x-small"]]);
			}
		}
};



BoxWhiskerGraph.prototype.plotClusterTitles = function(properties){
		/** Cluster Titles **/
		var posX = this.left + this.rulerWidth;
		var data = this.data;
		for(var i = 0; i < data.clusters.length; i++ ){
			/** title for the clusters **/
			posX = posX + this.interClustersSpace;
			
			/** Drawing title of classes **/
			var cluster = data.clusters[i];
			var posXClasses = posX;
			for(var j = 0; j < cluster.classes.length; j++){
				//SVG.drawRectangle(posXClasses, this.height - (this.bottom + this.clusterTitleHeight + this.rulerHeight), properties.classWidth, this.rulerHeight, this.svg, [["fill", "green"]]);
//				SVG.drawText(posXClasses + 1/6*properties.classWidth, this.height - (this.bottom + this.clusterTitleHeight + (this.rulerHeight*1/4)), cluster.classes[j].name, this.svg, [["style", "font-size:xx-small"]]);
				var color = cluster.classes[j].color;
				this.drawSVGVerticalText(posXClasses + 1/2*(properties.classWidth), this.height - (this.bottom - this.clusterTitleHeight + (this.rulerHeight)), cluster.classes[j].name , [["style", "font-size:x-small;"], ["fill",  color]]);
				
		//		SVG.drawRectangle(posXClasses, this.height - (this.bottom + this.clusterTitleHeight), properties.classWidth, 2, this.svg, [["fill", "black"]]);
				posXClasses = posXClasses + properties.classWidth + this.interClassesSpace;
			}
				
			var clusterTitleSpace = (data.clusters[i].classes.length * properties.classWidth) + ((data.clusters[i].classes.length - 1) * this.interClassesSpace);
			//SVG.drawRectangle(posX, this.height - (this.bottom + this.clusterTitleHeight), clusterTitleSpace, this.clusterTitleHeight, this.svg, [["fill", "pink"]]);
			SVG.drawRectangle(posX, this.height - (this.clusterTitleHeight+this.bottom), clusterTitleSpace, 1, this.svg, []);
			
//			var transform = ["translate", "(" + posX + 1/3*clusterTitleSpace +", " +  this.height - (this.bottom + (this.clusterTitleHeight*1/4)) +")"], ["transform", "rotate(180)"];
//			var transform = ["transform", "translate(" + (posX + 1/3*clusterTitleSpace) +", " + (this.height - (this.bottom + (this.clusterTitleHeight*1/4))) + "), rotate(-90) "];
//			this.drawSVGVerticalText(posX + 1/3*clusterTitleSpace, this.height - (this.bottom + (this.clusterTitleHeight*1/4)),data.clusters[i].name, [["style", "font-size:x-small"]]);
			SVG.drawText(posX , this.height - (this.bottom + (this.clusterTitleHeight*1/4)),data.clusters[i].name ,this.svg, [["style", "font-size:x-small"]]);
			posX = posX + clusterTitleSpace;
		}
};

BoxWhiskerGraph.prototype.plotWhisters = function(data, properties){
	var colors = ["yellow", "orange", "green"];
	var posX = this.left + this.rulerWidth;
	for(var i = 0; i < data.clusters.length; i++ ){
		var cluster = data.clusters[i];
		/** inter cluster space **/
		//SVG.drawRectangle(posX, this.top, this.interClustersSpace, this.height - this.top - this.bottom - this.clusterTitleHeight - this.rulerHeight, this.svg, [["fill", "black"]]);
		posX = posX + this.interClustersSpace;
		
		for (var j = 0; j < cluster.classes.length; j ++){
			
			//SVG.drawRectangle(posX, this.top, properties.classWidth, this.height - this.top - this.bottom - this.clusterTitleHeight - this.rulerHeight, this.svg, [["fill", colors[j]]]);
			this.plotBoxWhisker(
				{
					name	: cluster.classes[j].name,
					values	: cluster.classes[j].values,
					minPoint  : properties.minPoint,
					maxPoint  : properties.maxPoint,
					x		: posX,
					y		: this.top,
					width	: properties.classWidth,
					height	: this.height - this.top - this.bottom - this.clusterTitleHeight - this.rulerHeight
					
					
				}
			);
			posX = posX + properties.classWidth;
			//SVG.drawRectangle(posX, this.top, this.interClassesSpace, this.height - this.top - this.bottom - this.clusterTitleHeight - this.rulerHeight, this.svg, [["fill", "black"]]);
			if (j < cluster.classes.length - 1){
				posX = posX + this.interClassesSpace;
			}
		}
	}
};

BoxWhiskerGraph.prototype.draw = function(targetId, data){
	this.targetId = targetId;
	var properties = (this.getDimensions(data));
	this.svg = SVG.createSVGCanvas(document.getElementById(this.targetId), [["width", this.width], ["height", this.height]]);
	this.plotAxes(properties);
	this.plotClusterTitles(properties);
	this.plotWhisters(data,properties);
};

BoxWhiskerGraph.prototype.input = function(){
	return DATADOC.getBoxWhikerData();
};

BoxWhiskerGraph.prototype.test = function(targetId){
	var plot = new BoxWhiskerGraph(
			{
				targetId 	: targetId,
				height 		: 350,
				width  		: 450,
				maxBoxWidth	: 25
			}
	);
	plot.refresh(this.input());
};





