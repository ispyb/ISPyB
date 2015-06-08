/**
 * 
 * @nodeSize
 * @fontSize
 * @strokeWidth
 * @showTooltip
 * @showBorderLabels
 * @showFullName
 * @showLabels
 * @backgroundColor
 * 
 **/

function SamplePlateWidget(args) {
	this.actions = [];

	this.width = "1";
	this.height = "1";

	this.legendFontSize = 7;

	this.backgroundColor = "#E6E6E6";
	this.wellColor = "#FFFFFF ";

	this.id = BUI.id();
	this.notSupportedMessage = "IE doesn't support HTML5 features";

	this.nodeSize = 14;
	this.fontSize = 10;
	this.strokeWidth = 2;
	this.showTooltip = true;
	this.showBorderLabels = true;
	this.showFullName = false;
	this.showLabels = false;

	if (args != null) {
		if (args.showBorderLabels != null) {
			this.showBorderLabels = args.showBorderLabels;
		}
		if (args.showLabels != null) {
			this.showLabels = args.showLabels;
		}
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.nodeSize != null) {
			this.nodeSize = args.nodeSize;
		}
		if (args.notSupportedMessage != null) {
			this.notSupportedMessage = args.notSupportedMessage;
		}
		if (args.fontSize != null) {
			this.fontSize = args.fontSize;
		}
		if (args.showFullName != null) {
			this.showFullName = args.showFullName;
		}
		if (args.showTooltip != null) {
			this.showTooltip = args.showTooltip;
		}
		if (args.backgroundColor != null) {
			this.backgroundColor = args.backgroundColor;
		}
		if (args.strokeWidth != null) {
			this.strokeWidth = args.strokeWidth;
		}
	}

	/** this is the ids[specimenId] = nodeId **/
	this.ids = {};
	this.onVertexUp = new Event(this);
	this.selectedSVGNodes = [];
	this.markedSpecimenId = {};
}

SamplePlateWidget.prototype.clear = function(experiment, samplePlate, targetId) {
	if (document.getElementById(this.targetId) != null) {
		document.getElementById(this.targetId).innerHTML = "";
	}
};

SamplePlateWidget.prototype.draw = function(experiment, samplePlate, targetId, windowContainerId) {
	var _this = this;

	/** This is the id of the window where the sampleplateform is just to position correctly the tooltips **/
	this.windowContainerId = windowContainerId;
	if (Ext.isIE6 || Ext.isIE7 || Ext.isIE8) {
		document.getElementById(targetId).innerHTML = BUI.getWarningHTML(this.notSupportedMessage);
		return;
	}

	this.onVertexUp = new Event(this);
	this.samplePlate = samplePlate;
	this.experiment = experiment;

	this.targetId = targetId;

	var rows = this.samplePlate.platetype3VO.rowCount;
	var columns = this.samplePlate.platetype3VO.columnCount;

	this.network = new NetworkWidget({
		targetId : targetId
	});
	var dataset = new GraphDataset();
	var formatter = new NetworkDataSetFormatter({
		defaultFormat : {
			type : "LineEdgeNetworkFormatter",
			'fill-opacity' : 1,
			fill : this.wellColor,
			'stroke-width' : this.strokeWidth,
			'stroke-opacity' : 1,
			stroke : "#000000",
			size : this.nodeSize,
			title : {
				fontSize : this.fontSize,
				fill : "#000000"
			}
		}
	}, null, {
		labeled : false,
		height : this.height,
		width : this.width,
		right : this.width,
		backgroundColor : this.backgroundColor,
		balanceNodes : false,
		nodesMaxSize : 12,
		nodesMinSize : 2
	});

	formatter.dataBind(dataset);
	var layout = new LayoutDataset();
	layout.dataBind(dataset);
	this.network.draw(dataset, formatter, layout);

	for ( var i = 1; i <= rows; i++) {
		for ( var j = 1; j <= columns; j++) {
			this.network.getDataset().addNode("", {
				row : i,
				column : j
			});

			if (this.samplePlate.platetype3VO.name == " 4 x ( 8 + 3 ) Block") {
				if (j < 9) {
					this.network.getFormatter().vertices[this.network.getDataset().getVerticesCount() - 1].getDefault().setSize(this.nodeSize * 0.8);
				} else {
					this.network.getFormatter().vertices[this.network.getDataset().getVerticesCount() - 1].getDefault().setSize(this.nodeSize * 1.4);
				}
			}

		}
	}

	/** EVENT WHEN USER CLICK ON A WELL **/
	this.network.graphCanvas.onVertexUp.attach(function(sender, nodeId) {
		_this.onVertexUp.notify({
			samplePlate : _this.samplePlate,
			row : _this.network.getDataset().getVertexById(nodeId).args.row,
			column : _this.network.getDataset().getVertexById(nodeId).args.column

		});
	});

	this.network.graphCanvas.onVertexOver.attach(function(sender, nodeId) {
	});

	this.relayout(this.network, rows, columns);
	this.fillSimulator(this.experiment.getSamples());

	if (this.showBorderLabels) {
		this.drawBorders();
	}

};

SamplePlateWidget.prototype.drawBorders = function() {
	var xArray = {};
	var yArray = {};

	var xValues = [];
	var yValues = [];

	var verticesLayout = this.network.graphCanvas.getLayout().vertices;
	for ( var vertice in verticesLayout) {
		var verticeX = verticesLayout[vertice].x;
		if (xArray[verticeX] == null) {
			xArray[verticeX] = true;
			xValues.push(verticeX);
		}

		var verticeY = verticesLayout[vertice].y;
		if (yArray[verticeY] == null) {
			yArray[verticeY] = true;
			yValues.push(verticeY);
		}
	}

	/** LEGENG **/
	var letters = BUI.getSamplePlateLetters();
	for ( var i = 1; i <= xValues.length; i++) {
		SVG.drawText(xValues[i - 1] * this.network.graphCanvas.getWidth() - 5, this.network.graphCanvas.getHeight(), i, this.network.graphCanvas._svg,
				[ [ 'font-size', this.legendFontSize + 'px' ], [ 'font-weight', 'bold' ], [ 'fill', 'black' ] ]);
	}

	for ( var i = 1; i <= yValues.length; i++) {
		SVG.drawText(5, yValues[i - 1] * this.network.graphCanvas.getHeight() + 5, letters[i - 1], this.network.graphCanvas._svg, 
				[ [ 'font-size', this.legendFontSize + 'px' ], [ 'font-weight', 'bold' ], [ 'fill', 'black' ] ]);
	}

};

SamplePlateWidget.prototype.addOkIcon = function(x, y, id, specimen) {
	var svg = this.network.graphCanvas._svg;
	var _this = this;

	var id = id + "_marked";
	if (this.markedSpecimenId[id] == null) {
		SVG.drawRectangle(x - 10, y - 10, 22, 22, svg, [ [ "id", id ], [ "fill", "gray" ],["stroke-opacity", "0.5"], [ "fill-opacity", "0.2" ], [ 'stroke', 'black' ] ]);
		$('#' + id).qtip({
			content : {
				text : _this._getToolTipContent(specimen)
			},
			position : {
				adjust : {
					x : 0,
					y : 20
				}
			},
			style : {
				width : true,
				classes : 'ui-tooltip-shadow'
			}
		});
		this.markedSpecimenId[id] = true;
	}
};

SamplePlateWidget.prototype.selectSpecimen = function(specimen) {
	var vertex = this.getVertexByPosition(specimen.sampleplateposition3VO.rowNumber, specimen.sampleplateposition3VO.columnNumber);
	var x = this.network.getLayout().vertices[vertex.id].x * this.width;
	var y = this.network.getLayout().vertices[vertex.id].y * this.height;
	var svg = this.network.graphCanvas._svg;
	this.selectedSVGNodes.push(SVG.drawRectangle(x - 9, y - 9, 20, 20, svg, [["fill", "red"], ["fill-opacity", "0"], ['stroke', 'blue' ], [ 'stroke-width', '2' ] ]));
};

SamplePlateWidget.prototype.clearSelection = function() {
	var svg = this.network.graphCanvas._svg;
	for ( var i = 0; i < this.selectedSVGNodes.length; i++) {
		svg.removeChild(this.selectedSVGNodes[i]);
	}
	this.selectedSVGNodes = [];
};

SamplePlateWidget.prototype.getVertexByPosition = function(row, column) {
	var vertices = this.network.getDataset().getVertices();
	for ( var vertexId in vertices) {
		if ((vertices[vertexId].args.row == row) && (vertices[vertexId].args.column == column)) {
			return vertices[vertexId];
		}
	}
	return null;
};

//SamplePlateWidget.prototype.getOpacity = function(specimen) {
//	var concentrations = this.experiment.getConcentrationsBysample(specimen);
//	var normalized = Normalizer.normalizeArray(concentrations);
//	for ( var i = 0; i < concentrations.length; i++) {
//		if (concentrations[i] == specimen.concentration) {
//			if (normalized[i] == 0)
//				return 0.2;
//			return 0.2 + normalized[i] * 0.6;
//		}
//	}
//	return 1;
//
//};

SamplePlateWidget.prototype.showLabel = function(row, column, specimen) {
	if (specimen != null) {
		var vertex = this.getVertexByPosition(row, column);
		if (this.fontSize != 0) {
			if (specimen.macromolecule3VO != null) {
				if (this.showFullName) {
					vertex.setName(specimen.code);
				} else {
					vertex.setName(specimen.macromolecule3VO.acronym);
				}
			} else {
				if (this.showFullName) {
					vertex.setName(specimen.code);
				} else {
					vertex.setName(specimen.macromolecule3VO.acronym);
				}
			}
		}
	}
};

SamplePlateWidget.prototype.getNodeIdBySpecimenId = function(specimenId) {
	var nodeId = this.ids[specimenId];
};

SamplePlateWidget.prototype.markSpecimenAsOk = function(specimen, x, y, id) {
	if (specimen.measurements != null) {
		for ( var j = 0; j < specimen.measurements.length; j++) {
			if (specimen.measurements[j].merge3VOs != null) {
				if (specimen.measurements[j].merge3VOs.length > 0) {
					this.addOkIcon((x * this.network.graphCanvas.getWidth()), y * this.network.graphCanvas.getHeight(), id, specimen);
				}
			}
		}
	}
};
/**
 * Input: specimenId of the sample containing a macromolecule
 * Ouput: color of the specimen buffer associated
 */
SamplePlateWidget.prototype.getSpecimenBufferColorFromSampleSpecimenId = function(specimenId) {
	var dcs = this.experiment.getDataCollectionsBySpecimenId(specimenId);
	if (dcs.length > 0){
		var dataCollection = dcs[0];
		for (var i = 0; i < dataCollection.measurementtodatacollection3VOs.length; i++) {
			var measurementToDc = dataCollection.measurementtodatacollection3VOs[i];
			if (measurementToDc.dataCollectionOrder == 1){
				var measurement = this.experiment.getMeasurementById(measurementToDc.measurementId);
				if (measurement != null){
					return this.experiment.getSpecimenColorByBufferId(measurement.specimenId);
				}
				return 'pink';
			}
		}
	}
	return 'orange';
};

SamplePlateWidget.prototype.fillWell = function(row, column, specimen) {
	var _this = this;
	if (specimen != null) {
		var vertex = this.getVertexByPosition(row, column);
		if (vertex == null) {
			console.log(row + ", " + column + " not found");
			return;
		}
		var id = this.network.getGraphCanvas().getSVGNodeId(vertex.id);

		this.showLabel(row, column, specimen);
		this.ids[specimen.specimenId] = vertex.id;
		var x = this.network.getLayout().vertices[vertex.id].x;
		var y = this.network.getLayout().vertices[vertex.id].y;
		this.markSpecimenAsOk(specimen, x, y, id);

		if (specimen.macromolecule3VO == null) {
			/** BUFFER * */
			var color = this.experiment.getSpecimenColorByBufferId(specimen.specimenId);
			this.network.getFormatter().getVertexById(vertex.id).getDefault().setFill(color);
//			this.network.getFormatter().getVertexById(vertex.id).getDefault().setStroke(this.experiment.getSpecimenColorByBufferId(specimen.specimenId));
			this.network.getFormatter().getVertexById(vertex.id).getDefault().setStroke('blue');
			this.network.getFormatter().getVertexById(vertex.id).getDefault().setStrokeWidth(2);

		} else {
			this.network.getFormatter().getVertexById(vertex.id).getDefault().setFill(this.experiment.macromoleculeColors[specimen.macromolecule3VO.macromoleculeId]);
//			this.network.getFormatter().getVertexById(vertex.id).getDefault().setStroke(this.getSpecimenBufferColorFromSampleSpecimenId(specimen.specimenId));
			this.network.getFormatter().getVertexById(vertex.id).getDefault().setStroke('black');
			this.network.getFormatter().getVertexById(vertex.id).getDefault().setStrokeWidth(1);
		}

		if (this.showLabels) {
			x = this.network.getLayout().vertices[vertex.id].x;
			y = this.network.getLayout().vertices[vertex.id].y;
			if (specimen.macromolecule3VO != null) {
				var acronym = specimen.macromolecule3VO.acronym;
				if (acronym.length > 10) {
					acronym = acronym.slice(0, 10) + "...";
				}

				SVG.drawText((x * this.network.graphCanvas.getWidth()) - this.nodeSize - 10, y * this.network.graphCanvas.getHeight() + (this.nodeSize * 3) + 20, acronym, this.network.graphCanvas._svg, [
					[ 'font-size', 'xx-small' ], [ 'fill', 'black' ] ]);

				SVG.drawText((x * this.network.graphCanvas.getWidth()) - this.nodeSize - 10, y * this.network.graphCanvas.getHeight() + (this.nodeSize * 3) + 35, Number(specimen.concentration).toFixed(2) + " mg/ml", this.network.graphCanvas._svg, [
					[ 'font-size', 'xx-small' ], [ 'fill', 'black' ] ]);
			}

			var bufferName = this.experiment.getBufferById(specimen.bufferId).acronym;
			if (bufferName.length > 10) {
				bufferName = bufferName.slice(0, 10) + "...";
			}

			SVG.drawText((x * this.network.graphCanvas.getWidth()) - this.nodeSize - 10, y * this.network.graphCanvas.getHeight() + (this.nodeSize * 3) + 5, this.experiment.getBufferById(specimen.bufferId).acronym, this.network.graphCanvas._svg, [
				[ 'font-size', 'xx-small' ], [ 'fill', 'blue' ] ]);
		}

		if (this.showTooltip) {
			var id = this.network.getGraphCanvas().getSVGNodeId(vertex.id);
			if (_this.windowContainerId != null) {
				$('#' + id).qtip({
					content : {
						text : _this._getToolTipContent(specimen)
					},
					position : {
						adjust : {
							x : 0,
							y : 20
						}
					}
				});
			} else {
				$('#' + id).qtip({
					content : {
						text : _this._getToolTipContent(specimen)
					},
					position : {
						adjust : {
							x : 0,
							y : 20
						}
					}
				});

			}
		}
	}
};

SamplePlateWidget.prototype._getToolTipContent = function(specimen) {
	var content = "<table style='font-size:11px;'>";
	content = content + "<TR><TD>" + "Specimen " + "</TD><TD style='color:black; font-weight:bold;'>" + specimen.code + "</TD></TR>";
	content = content + "<TR style='height:20px'><TD>" + "" + "</TD><TD style='color:black; font-weight:bold;'></TD></TR>";
	content = content + "<TR><TD>" + "Buffer: " + "</TD><TD style='color:blue; font-weight:bold;'>" + this.experiment.getBufferById(specimen.bufferId).name + "</TD></TR>";
	if (specimen.macromolecule3VO != null) {
		content = content + "<TR><TD>" + "Macromolecule: " + "</TD><TD  style='color:green; font-weight:bold;'>" + specimen.macromolecule3VO.acronym + "</TD></TR>";
		content = content + "<TR><TD>" + "Concentration: " + "</TD><TD >" + specimen.concentration + "</TD></TR>";
	}
	//	content = content + "<TR><TD>" + "Temperature: "+ "</TD><TD>" + specimen.exposureTemperature + "</TD></TR>";
	content = content + "<TR><TD>" + "Volume: " + "</TD><TD>" + specimen.volume + "</TD></TR>";

	if (specimen.viscosity != null) {
		content = content + "<TR><TD>" + "Viscosity: " + "</TD><TD>" + specimen.viscosity + "</TD></TR>";
	}

	content = content + "</table>";
	return content;
};

SamplePlateWidget.prototype.fillSimulator = function(samples) {
	for ( var i = 0; i < samples.length; i++) {
		var sample = samples[i];
		if (sample.sampleplateposition3VO != null) {
			if (sample.sampleplateposition3VO.samplePlateId == this.samplePlate.samplePlateId) {
				var position = sample.sampleplateposition3VO;
				this.fillWell(position.rowNumber, position.columnNumber, sample);
			}
		}
	}

};

SamplePlateWidget.prototype.relayout = function(network, rows, columns) {
	if (this.samplePlate.platetype3VO.shape == "REC") {
		this.squareRelayout(network, rows, columns);
	}
	if (this.samplePlate.platetype3VO.shape == "CIR") {
		this.circleRelayout(network, rows, columns);
	}
};

SamplePlateWidget.prototype.squareRelayout = function(network, rows, columns) {
	var count = network.getDataset()._getVerticesCount();
	var yMin = 0.07;
	var yMax = 0.9;
	var xMin = 0.075;
	var xMax = 0.95;
	var stepY = (yMax - yMin) / (rows - 1);
	var stepX = (xMax - xMin) / (columns - 1);

	var verticesCoordinates = [];
	for ( var i = 0; i < rows; i++) {
		for ( var j = 0; j < columns; j++) {
			y = i * stepY + yMin;
			x = j * stepX + xMin;

			verticesCoordinates.push({
				'x' : x,
				'y' : y
			});

		}
	}
	var aux = 0;
	for ( var vertex in network.getDataset().getVertices()) {
		if (network.getLayout().vertices[vertex] == null) {
			this.vertices[vertex] = new NodeLayout(vertex, 0, 0);
		}
		network.getLayout().vertices[vertex].setCoordinates(verticesCoordinates[aux].x, verticesCoordinates[aux].y);
		aux++;
		network.getLayout().vertices[vertex].changed.attach(function(sender, item) {
			_this.changed.notify(item);
		});
	}
};

SamplePlateWidget.prototype.circleRelayout = function(network, rows, columns) {
	network.getLayout().getLayout("CIRCLE");
};


