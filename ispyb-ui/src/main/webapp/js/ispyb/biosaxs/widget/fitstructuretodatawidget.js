/**
 * Edit the information of a buffer
 * 
 * #onSaved 
 * #onRemoveAdditive
 * 
 */
function FitStructureToDataWidget(args) {
	this.id = BUI.id();
	this.width = 875;
	this.height = 565;
}

FitStructureToDataWidget.prototype.getPlotContainerDimensions = function() {
	return {
		width : this.width*2/3 - 4,
		height : this.height - 8
	};
};

FitStructureToDataWidget.prototype.getPlotId = function() {
	return this.id + "plot";
};

FitStructureToDataWidget.prototype.getTextAreaHeight = function() {
	return 200;
};

FitStructureToDataWidget.prototype.getPlotContainer = function() {
	var _this = this;
	
	var textAreaHeight = this.getTextAreaHeight();
	var width = this.getPlotContainerDimensions().width;
	var height = this.getPlotContainerDimensions().height;
	
	function getHTML(){
		var style= "width:" + (width  - 5) + "px;height:" + (height - textAreaHeight - 8) +  "px;";
		return "<div id='"+ _this.getPlotId() +"' style='" + style + "'></div>";
	}
	this.plot =  Ext.create('Ext.panel.Panel', {
		margin : "2 2 2 2",
		layout : 'hbox',
		width : _this.width,
		height : (height - textAreaHeight - 6),
		border : 0,
		items : [
		         {
		        	 html : getHTML()
		         },
		         this.getSummaryContainer()
		       
		],
		listeners : {
			afterrender : function() {
			}
		}
	});
	return this.plot;
};



FitStructureToDataWidget.prototype.getSecondaryContainerDimensions = function() {
	return {
		width  : this.width/3 - 16,
		height : (this.height - 8) / 2
		
	}
};


FitStructureToDataWidget.prototype.getPdbId = function() {
	return this.id + "pdb";
};


FitStructureToDataWidget.prototype.getPdbContainer = function() {
	var width = this.getSecondaryContainerDimensions().width;
	var height = this.getSecondaryContainerDimensions().height;
	
	var _this = this;
	function getHTML(){
		var style= "background-color:black;width:" + (width - 5) + "px;height:" + (height - 5) +  "px;";
		return "<textarea id='" + _this.getPdbId() + "_src'; style='display: none;'></textarea><div id='"+ _this.getPdbId() +"' style='" + style + "'></div>";
	}
	
	this.pdbContainer = Ext.create('Ext.panel.Panel', {
		margin : "2 2 2 2",
		width : width,
		height : height,
		items : [
		         {
		        	 html : getHTML()
		         }
		]
	});
	return this.pdbContainer;
};


FitStructureToDataWidget.prototype.getDataContainer = function() {
	return Ext.create('Ext.panel.Panel', {
		margin : "2 2 2 2",
		width : this.getSecondaryContainerDimensions().width,
		height : this.getSecondaryContainerDimensions().height ,
		items : [
		]
	});
};

FitStructureToDataWidget.prototype.getSummaryContainer = function() {
	return Ext.create('Ext.panel.Panel', {
		margin : "2 2 2 2",
		width : this.width/3 - 10,
		height : this.height - 15 - this.getTextAreaHeight() - 1,
		layout : 'vbox',
		items : [
		         this.getPdbContainer() 
//		         this.getDataContainer()
		],
		listeners : {
			afterrender : function() {
			}
		}
	});
};

FitStructureToDataWidget.prototype.getPanel = function() {
	var _this = this;
	var textAreaHeight = this.getTextAreaHeight();
	
	this.panel = Ext.create('Ext.container.Container', {
		layout : 'vbox',
		width : this.width,
		height : this.height,
		items : [
		         	this.getPlotContainer(),
		         	
		         	
		         	  {
			        	 id			: _this.id + "textArea",
			             xtype      : 'textareafield',
			             grow       : true,
//			             style		: {
//			            	 				font-size : '6px'
//			             },
			             height    : textAreaHeight - 10,
			             width 	   : this.width  - 10,
			             name      : 'message',
//			             fieldLabel: 'Message',
			             anchor    : '100%',
			             margin	   : 5
			         }
		],
		listeners : {
			afterrender : function() {
			}
		}
	});
	return this.panel;
};






FitStructureToDataWidget.prototype.refreshPlot = function(fitStructureToExperimentalDataId) {
	var _this = this;
	/** Plotting FIT file * */
	var adapterFit = new BiosaxsDataAdapter();
	this.plot.setLoading("Plotting");
	adapterFit.onSuccess.attach(function(sender, data){
		var colors = [];
		var labels = ["X"];
		var parsed = [];
		
		function getCoordinates(y, error){
			error = parseFloat(error);
			y = parseFloat(y);
			return DataCollectionCurveVisualizer.prototype._getCoordinates(y,error);
		}
		
		labels.push("fit");
		labels.push("scattering");
		colors.push("#58ACFA");
		colors.push("green");
		
		/** Creating X coordinates * */
		var pointsCount = 0;
		if (data != null){
			if (data.length > 0){
				for ( var key in data[0]) {
					
					var firstFile = data[0][key];
					for (var i = 0; i < firstFile.length; i++) {
						var n = parseFloat(firstFile[i][1]);
						parsed.push([parseFloat(firstFile[i][0])]);
						parsed[parsed.length - 1].push(getCoordinates(firstFile[i][1], firstFile[i][2]));
						var n3 = parseFloat(firstFile[i][3]);
						parsed[parsed.length - 1].push(getCoordinates(n3, n3));
						pointsCount ++;
					}
				}
			}
		}
		/** Plotting * */
		var dygraphObject = new StdDevDyGraph(_this.getPlotId(), {
			width 				: _this.getPlotContainerDimensions().width - 20,
			height				: _this.getPlotContainerDimensions().height - 10 - _this.getTextAreaHeight(),
			xlabel				: "q (nm-1)",
			showRangeSelector	: false
		});
		dygraphObject.draw(parsed, colors, labels);
		_this.plot.setLoading(false);
		
	});   
	adapterFit.getFitStructureToExperimentalDataFile(fitStructureToExperimentalDataId, "fit", "json");
};

FitStructureToDataWidget.prototype.refreshPdbViz = function(structureId){
	var _this =this;
	/** Plotting PDB file **/
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, pdbContent){
		_this.pdbContainer.setLoading("Rendering PDB");
		document.getElementById(_this.getPdbId() +'_src').innerHTML = pdbContent;
		var glmol02 = new GLmol(_this.getPdbId());
		_this.pdbContainer.setLoading(false);
	});
	_this.pdbContainer.setLoading("Reading PDB");
	adapter.getStructureFile(structureId);
};


FitStructureToDataWidget.prototype.refreshOutputfileContent = function(fitStructureToExperimentalDataId){
	var _this =this;
	/** Plotting PDB file **/
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, content){
		console.log(content);
		Ext.getCmp(_this.id + "textArea").setValue(content);
		
	});
	adapter.getFitStructureToExperimentalDataFile(fitStructureToExperimentalDataId, "OUTPUT", "txt");
};

FitStructureToDataWidget.prototype.refresh = function(fitStructureToExperimentalDataId, structureId) {
	this.refreshPlot(fitStructureToExperimentalDataId);
	this.refreshOutputfileContent(fitStructureToExperimentalDataId);
	this.refreshPdbViz(structureId);
};