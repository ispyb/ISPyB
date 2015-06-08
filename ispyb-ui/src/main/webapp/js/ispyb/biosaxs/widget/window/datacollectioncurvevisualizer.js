
function DataCollectionCurveVisualizer(args){
		this.id = BUI.id();
		this.width = 1000;
		this.height = 650;
		this.linear = false;
		
		if (args != null){
			if (args.width != null){
				this.width = args.width;
			}
			
			if (args.height != null){
				this.height = args.height;
			}
			
		}
		
		this.orderColors = {
								1	: "#58ACFA",
								2	: "#0B610B",
								3	: "#08088A"
		}
 };

 
DataCollectionCurveVisualizer.prototype.draw = function (){
	Ext.create('Ext.window.Window', {
	    title: 'Primary Data Processing',
	    height: this.height,
	    width: this.width,
	    layout: 'fit',
	    items: [this.getPanel()]              
	}).show();
};
 
/**
 * Get the path of the average files
 */
DataCollectionCurveVisualizer.prototype._getPrefixes = function (record){
	var bb = ""; //BufferBefore
	var sm = ""; //SampleMacromolecule
	var ba = "";//BufferAfter
	try{
		if (record.bufferBeforeAverageFilePath != null){
			bb = record.bufferBeforeAverageFilePath.split("/")[record.bufferBeforeAverageFilePath.split("/").length - 1].replace("_ave.dat", "");
		}
		if (record.averageFilePath != null){
			sm = record.averageFilePath.split("/")[record.averageFilePath.split("/").length - 1].replace("_ave.dat", "");
		}
		if (record.bufferAfterAverageFilePath != null){
			ba = record.bufferAfterAverageFilePath.split("/")[record.bufferAfterAverageFilePath.split("/").length - 1].replace("_ave.dat", "");
		}
	}
	catch(e){
		console.log(e);
	}
	return [bb, sm, ba]; 
};
	

DataCollectionCurveVisualizer.prototype._getFrames = function (merges, experimentList){
	var frames = new Array();
	for ( var i = 0; i < merges.length; i++) {
		var merge = merges[i];
		for ( var j = 0; j < merge.framelist3VO.frametolist3VOs.length; j++) {
			/** Sorting by frame Id **/
			merge.framelist3VO.frametolist3VOs.sort(function(a, b){return a.frame3VO.frameId - b.frame3VO.frameId;})
			var frametolist3VOs = merge.framelist3VO.frametolist3VOs[j];
			
			/** @ESRF the ave, sub files belong to the set of frames.
			 *  This behaviour is deprecated but old experiments still contains such files as frames 
			 *  **/
			if ((frametolist3VOs.frame3VO.filePath.indexOf("ave.dat") == -1) 
					&& frametolist3VOs.frame3VO.filePath.indexOf("sub.dat") == -1
				    && frametolist3VOs.frame3VO.filePath.indexOf("sub.out") == -1 ){
				/** Work around ESRF because averbuffer is whitin the list of frames **/
				 //&& frametolist3VOs.frame3VO.filePath.indexOf("averbuffer.dat") == -1
				    	
				frames.push(frametolist3VOs.frame3VO);
				frames[frames.length - 1]["specimen"] = experimentList.getSampleById(experimentList.getMeasurementById(merge.measurementId).specimenId);
				frames[frames.length - 1]["merged"] = false;
				frames[frames.length - 1]["merge"] = merge;
				
				if (frametolist3VOs.frame3VO.filePath.indexOf("averbuffer.dat") == -1){
					if (merge.framesMerge != null){
						if (Number(merge.framesMerge) > (j)){
							frames[frames.length - 1]["merged"] = true;
						}
					}
				}
				else{
					frames[frames.length - 1]["merged"] = true;
				}
			}
		}
	}
	return frames;
};

DataCollectionCurveVisualizer.prototype._getAverages = function (merges, experimentList){
	var averages = new Array();
	merges.sort(function(a, b){return a.mergeId - b.mergeId;})
	for ( var i = 0; i < merges.length; i++) {
		averages.push({
			filePath : merges[i].averageFilePath,
			specimen : experimentList.getSampleById(experimentList.getMeasurementById(merges[i].measurementId).specimenId),
			merged	: true,
			merge : merges[i]
		});
	}
	
	/** Work around for experiment where best buffer is within the list of frames **/
//	for ( var i = 0; i < merges.length; i++) {
//		var merge = merges[i];
//		for ( var j = 0; j < merge.framelist3VO.frametolist3VOs.length; j++) {
//			/** Sorting by frame Id **/
//			merge.framelist3VO.frametolist3VOs.sort(function(a, b){return a.frame3VO.frameId - b.frame3VO.frameId;})
//			var frametolist3VOs = merge.framelist3VO.frametolist3VOs[j];
//			
//			/** @ESRF the ave, sub files belong to the set of frames.
//			 *  This behaviour is deprecated but old experiments still contains such files as frames 
//			 *  **/
//			if (frametolist3VOs.frame3VO.filePath.indexOf("averbuffer.dat") != -1){
//				averages.push({
//					filePath : frametolist3VOs.frame3VO.filePath,
//					specimen : experimentList.getSampleById(experimentList.getMeasurementById(merges[i].measurementId).specimenId),
//					merged	: true,
//					merge : merges[i]
//				});
//			}
//		}
//	}
	return averages;
};

DataCollectionCurveVisualizer.prototype._getSubtractionByDataCollectionId = function (dataCollectionId, experimentList){
	if(experimentList.getDataCollectionById(dataCollectionId).substraction3VOs != null){
		if(experimentList.getDataCollectionById(dataCollectionId).substraction3VOs.length > 0){
			return experimentList.getDataCollectionById(dataCollectionId).substraction3VOs[0];
		}
	}
	return null;
};

/**
 * For multiple averages for the same measurementId this methods return the last mergeId processed that should be
 * the one with most of frames
 */
DataCollectionCurveVisualizer.prototype.getLastMerges = function (merges, experimentList, dataCollectionId){
	var result = [];
	function getLastMergeByMeasurementId(merges, measurementId){
		var aux = [];
		for (var i = 0; i < merges.length; i++) {
			var merge = merges[i];
			if (merge.measurementId == measurementId){
				aux.push(merge);
			}
		}
		aux.sort(function(a,b){return a.mergeId - b.mergeId;});
		return aux[aux.length -1];
	}
	
	var datacollection = experimentList.getDataCollectionById(dataCollectionId);
	if (datacollection.measurementtodatacollection3VOs != null){
		for (var i = 0; i < datacollection.measurementtodatacollection3VOs.length; i++) {
			var measurementtodatacollection3VO = datacollection.measurementtodatacollection3VOs[i];
			var measurementId = measurementtodatacollection3VO.measurementId;
			result.push(getLastMergeByMeasurementId(merges, measurementId));
		}
	}
	return result;
};

DataCollectionCurveVisualizer.prototype._prepareData = function (merges, experimentList, dataCollectionId, record){
	var data = [];
	var merges = this.getLastMerges(merges, experimentList, dataCollectionId);
	var subtraction = this._getSubtractionByDataCollectionId(dataCollectionId, experimentList);
	var subtractionId = null;
	if (subtraction != null){
		subtractionId = subtraction.subtractionId;
		var type = "SUBTRACTED";
		data.push({
			filePath		: subtraction.substractedFilePath.split("/")[subtraction.substractedFilePath.split("/").length-1],
			type			: type,
			name			: type,
			frameId			: null,
			mergeId			: null,
			specimen		: null,
			color			: "#DF01D7",
			merged			: true,
			merge			: null,
			subtractionId	: subtractionId
		});
		
	}
	
	/** Json structure for frames containing, specimen, merged, merge, etc... **/
	var averages =  this._getAverages(merges, experimentList);
	for ( var i = 0; i < averages.length; i++) {
		var type = "AVERAGED";
		data.push({
			filePath		: averages[i].filePath.split("/")[averages[i].filePath.split("/").length-1],
			type			: type,
			name			: type,
			frameId			: null,
			mergeId			: averages[i].merge.mergeId,
			specimen		: averages[i].specimen,
			color			: this._getSpecimenColor(averages[i].specimen, averages[i].merge, experimentList, dataCollectionId, averages[i].merged, type),
			merged			: averages[i].merged,
			merge			: averages[i].merge,
			subtractionId	: subtractionId
		});
	}
	
	
	
	/** Json structure for frames containing, specimen, merged, merge, etc... **/
	var frames = this._getFrames(merges, experimentList);
	/** Filling the frames **/
	for ( var i = 0; i < frames.length; i++) {
		var type = "FRAMES";
		data.push({
			filePath		: frames[i].filePath.split("/")[frames[i].filePath.split("/").length-1],
			type			: type,
			name			: type,
			frameId			: frames[i].frameId,
			specimen		: frames[i].specimen,
			color			: this._getSpecimenColor(frames[i].specimen, frames[i].merge, experimentList, dataCollectionId, frames[i].merged, type),
			merged			: frames[i].merged,
			merge			: frames[i].merge,
			subtractionId	: subtractionId
		});
	}
	return data;
};


DataCollectionCurveVisualizer.prototype._renderSpecimenBox = function (merges, experimentList, dataCollectionId, record){
	/** For buffer **/
	try{
			merges = this.getLastMerges(merges, experimentList, dataCollectionId);
			
			var measurement = experimentList.getMeasurementById(merges[1].measurementId);
			var specimen = experimentList.getSampleById(measurement.specimenId);
			var buffer = BIOSAXS.proposal.getBufferById(specimen.bufferId);
			var macromolecule = BIOSAXS.proposal.getMacromoleculeById(record.macromoleculeId);
			
			 bufferAcronym = buffer.acronym;
			 macromoleculeAcronym = macromolecule.acronym; 
			 
			 if (merges[0] != null){
				 bbmerges = merges[0].framesMerge;
			 }
			 else{
				 bbmerges = 'None'; 
			 }
			 
			 if (merges[1] != null){
				 if (merges[1].framesMerge != null){
					 molmerges = merges[1].framesMerge; 
				 }
				 if (merges[1].framesCount != null){
					 totalframes =  merges[1].framesCount; 
				 }
			 }
			 else{
				 molmerges = 'None'; 
			 }

			 if (merges[2] != null){
				 if (merges[2].framesMerge != null){
					 bamerges =  merges[2].framesMerge; 
				 }
			 }
			 else{
				 bamerges =  'None'; 
			 }
			 bufferId = buffer.bufferId; 
			 macromoleculeId = macromolecule.macromoleculeId; 
			 document.getElementById(this.id + '_header').innerHTML = BUI.getHTMLTableForFrameAveraged(bufferAcronym, macromoleculeAcronym, bbmerges, molmerges, bamerges, totalframes, bufferId, macromoleculeId);
	}
	catch(e){
		console.log(e);
	}	 
};

DataCollectionCurveVisualizer.prototype._getImageHTML = function (title, type, subtractionId, height, width){
	var url = BUI.getURL() + '&type=' + type + '&subtractionId=' + subtractionId;
	var event = "OnClick= window.open('" + url + "')";
	return '<table ><tr><td><img src='+ url +'   height="' + height + '" width="' + width + '" ' + event +'></td></tr><tr><td style="font-weight:bold;text-align:center;">'+ title + '</td></tr></table>'  ;
};

DataCollectionCurveVisualizer.prototype.refresh = function (merges, experimentList, dataCollectionId, record){
	var data = this._prepareData(merges, experimentList, dataCollectionId, record);
	this.store.loadData(data);
	this._renderSpecimenBox(merges, experimentList, dataCollectionId, record);
			 
	 /** Images **/
	var width = this.width*1/5 - 50;
	var height = width;
	if (data[0].subtractionId != null){
		document.getElementById(this.id + '_scattering').innerHTML = this._getImageHTML("Scattering", "scattering", data[0].subtractionId, height, width);
		document.getElementById(this.id + '_kratky').innerHTML = this._getImageHTML("Kratky Plot","kratky", data[0].subtractionId, height, width);
		document.getElementById(this.id + '_guinier').innerHTML = this._getImageHTML("Guinier Approx.","guinier", data[0].subtractionId, height, width);
		document.getElementById(this.id + '_gnom').innerHTML = this._getImageHTML("P(r)","gnom", data[0].subtractionId, height, width);
		
	}
	
	/** Loading frames **/
	var selected = new Array();
	for ( var i = 0; i < data.length; i++) {
		var record = data[i];
		if ((record.type == 'AVERAGED')||(record.type == 'SUBTRACTED')){
			selected.push(record);
		}
	}
	
	this._renderPlot(selected);
	this.legendStore.loadData(selected);
};


DataCollectionCurveVisualizer.prototype._getSpecimenColor = function (specimen, merge, experimentList, dataCollectionId, merged, type){
	var dataCollection = experimentList.getDataCollectionById(dataCollectionId);
	var order = null;
	for ( var i = 0; i < dataCollection.measurementtodatacollection3VOs.length; i++) {
		var measurementtodatacollection3VO = dataCollection.measurementtodatacollection3VOs[i];
		if (measurementtodatacollection3VO.measurementId == merge.measurementId){
			order = measurementtodatacollection3VO.dataCollectionOrder;
		}
	}
	
	if (!merged){
			if (type == "FRAMES"){
				return 'gray';
			}
	}
	
	if (type == "SUBTRACTED"){
		return "#DF01D7";
	}
	
	return this.orderColors[order];
};

DataCollectionCurveVisualizer.prototype._getDataStore = function (){
	return Ext.create('Ext.data.Store', {
	    fields			: ['filePath', 'type', 'frameId', 'specimen', 'subtractionId'],
	    data 			: [],
	    groupField		: 'type',
	    sorters			: [{
							        //property: 'frameId',
								property : 'filePath',
							        direction: 'ASC' 
	    }]
	});
	
};

	
DataCollectionCurveVisualizer.prototype.getLegendGrid = function (){
	this.legendStore = this._getDataStore();
	var height = this.height*2/3 - 10 - 60; // 1- is the padding and 50 is the radio button for linear and log
	var width =  this.width/5 - 10;
	
	var panel = this.getFrameGrid(this.legendStore, width, height, 'Legend', false);  
	return panel;
};

DataCollectionCurveVisualizer.prototype.getSettingsPanel = function (width){
     var _this = this;
	 return {
     				xtype 		: 'container',
     				layout		: 'vbox',
     				items		: [
			     	     		  {
			     	     	        xtype: 'fieldset',
//			     	     	        border	: 1,
		     	     		    	height	: 50,
		     	     		    	width	: width,
		     	     		    	margin	: '5 0 5 0',
			     	     	        items: [{
			     	     	        	 		xtype: 'radiogroup',
					     	     	        	listeners : {
					     	     					change : function(checkbox, newValue, oldValue, eOpts) {
					     	     						_this.linear = newValue.linear;
					     	     						_this._renderPlot(_this.selected);
					     	     					}
					     	     				},
					     	     	            items: [
					     	     	                    {boxLabel: 'Log', name: 'linear', inputValue: false, checked: true},
					     	     	                  	{boxLabel: 'Linear', name: 'linear', inputValue: true}
					     	     	            ]
			     	     	        		}]
			     	     	        }
			     	]
     }
};

DataCollectionCurveVisualizer.prototype.getFrameGrid = function (store, width, height, title, selectionMode){
	var _this = this;
	var selModel = null;
	if (selectionMode){
		selModel = Ext.create('Ext.selection.RowModel', {
			allowDeselect		: true,
			mode				: 'MULTI',
			listeners			: {
							        selectionchange: function (sm, selections) {
							            var selected = new Array();
							            for (var i = 0; i < selections.length; i++) {
							                selected.push(selections[i].raw);
							            }
							            _this._renderPlot(selected);
							            _this.legendStore.loadData(selected);
							        }
			}
		});
	}
	
	return Ext.create('Ext.grid.Panel', {
	    store				: store,
	    height 				: height - 120,
	    width				: width,
	    selModel			: selModel,
	    features 			: [{ftype:'grouping'}],
	    columns				: [
	           				   		{ 
	           				   			text			: title,  
	           				   			dataIndex		: 'filePath', 
	           				   			flex			: 1,
	           				   			renderer		:   function(val,b,record,a){ 
	           				   									var fontweight = 'italic';
	           				   									var color = record.raw.color;
	           				   									
	           				   									if (record.raw.merged){
	           				   										fontweight = 'bold';
	           				   									}
	           				   									else{
	           				   										if (record.raw.type == "FRAMES"){
	           				   											val = val + " <span style='font-size:9px;font-weight:bold;color:red';>[discarded]</span>"
	           				   										}
	           				   									}
	           				   									
	           				   									if (((record.raw.type == "SUBTRACTED")||(record.raw.type == "OUT"))&&(color == _this.orderColors[1])){
	           				   										val = val + " <span style='font-size:9px;font-weight:bold;color:gray';>[Previous data collection]</span>"
	           				   									}
	           				   									
	           				   									return "<span style='font-weight:" + fontweight +";color:" + color + "'>" + val + "</span>";
	           				   			}
           				   			},
	           				   		{ text: 'Type',  dataIndex: 'type', flex: 1, hidden: true}
	           				   	
	    ]
	});
};

DataCollectionCurveVisualizer.prototype.getGrid = function (){
	var _this = this;
	
	this.store = this._getDataStore();
	var height = this.height;// - 50;
	var width =  this.width/5 - 5;
	this.framesPanel = this.getFrameGrid(this.store, width, height, 'Frames', true);  
	
	this.framesPanel.on("afterrender", function(){
	});
	return this.framesPanel;
};

DataCollectionCurveVisualizer.prototype._renderPlotFromData = function (data){
//	
	
	var targetId = this.id + '_plot';
	var input = new Array();
	/** Getting point X **/
	if (data != null){
		if (data.length > 0){
			for ( var i = 0; i < data.length; i++) {
				for ( var frameId in data[i]) {
					input.push(data[i][frameId])
				}
			}
		}
	}
	
	/** Getting labels and colors**/
	var labels = ["X"];
	var colors = new Array();
	if (data != null){
		if (data.length > 0){
			for ( var i = 0; i < data.length; i++) {
				for ( var frameId in data[i]) {
					labels.push(frameId);
				}
			}
		}
	}
	for ( var k = 0; k < this.selected.length; k++) {
//		if ((this.selected[k].frameId == frameId)||(this.selected[k].mergeId == frameId)||(this.selected[k].subtractionId == frameId)){
			colors.push(this.selected[k].color);
//		}
	}
	
	
	var parsed = new Array();
	var points = 10000000;
	
	/** It may happen that different files contains different number of points 
	 * in that case we that the file with the smaller number of points
	 * **/
	var index = 0;
	for ( var i = 0; i < input.length; i++) {
		var nPoints = Number(input[i].length);
		if (nPoints < points){
			points = nPoints;
			index = i;
		}
	}
	
	for ( var i = 0; i < input[index].length; i++) {
		var x = Number(input[index][i][0]);
		parsed.push([x]);
	}
	
	for ( var i = 0; i < input.length; i++) {
		for ( var j = 0; j < points; j++) {
			try{
			var y =   (parseFloat(input[i][j][1]));
			var error = Math.abs(parseFloat(input[i][j][2]));
			if (j == 1705){
				
			}
			parsed[j].push(this._getCoordinates(y, error));
			}
			catch(e){
				
			}
		}
	}
	
	this._renderDygraph(targetId, parsed, colors, labels);
};

DataCollectionCurveVisualizer.prototype._renderDygraph = function (targetId, parsed, colors, labels){
	var dygraphObject = new StdDevDyGraph(targetId, {
		width 				: this.width*3/5 - 60,
		height				: this.height*2/3 - 30,
		xlabel				: "q (nm-1)",
		showRangeSelector	: false
	});
	
	dygraphObject.draw(parsed, colors, labels);
};


DataCollectionCurveVisualizer.prototype._getCoordinates = function (y, error){
	var minus = y- error;
	var max = y + error;
	
	if (this.linear){
		return [Math.abs(minus), y , Math.abs(max)];
	}
	if ((minus!= 0)&&(max != 0)){
		return [Math.log(Math.abs( minus)), Math.log(y), Math.log(Math.abs(max))];
	}
	else{
		return [Math.log(y), Math.log(y), Math.log(y)];
	}
};


DataCollectionCurveVisualizer.prototype._renderPlot = function (selected){
	var _this = this;
	
	this.selected = selected;
	
	
	var frameIds = new Array();
	var mergeIds = new Array();
	var subtractionIds = new Array();
	for ( var i = 0; i < selected.length; i++) {
		if (selected[i].frameId != null){
			frameIds.push(selected[i].frameId);
		}
		if (selected[i].mergeId != null){
			mergeIds.push(selected[i].mergeId);
		}
		
		if ((selected[i].mergeId == null)&&(selected[i].frameId == null)){
			if (selected[i].subtractionId != null){
				subtractionIds.push(selected[i].subtractionId);
			}
		}
	}
	
	var dataAdapter = new BiosaxsDataAdapter();
	dataAdapter.onSuccess.attach(function(sender, data){
		_this._renderPlotFromData(data);
	});

	dataAdapter.onError.attach(function(sender, error){
		alert("Oooops... there was an error")
	});
	
	
	dataAdapter.getScatteringCurveByFrameIdsList(frameIds, mergeIds, subtractionIds);
};

DataCollectionCurveVisualizer.prototype.getPanel = function (){
	var _this = this;
	this.panel =  Ext.create('Ext.form.Panel', {
			id 		: this.id + 'main', 
	        border	: 0,
	        height	: this.height,
	        width 	: this.width,
	        layout: {
	            type: 'table',
	            columns: 3
	        },
	        layout: 'hbox',
	        items: [
	               
	                {
	                	xtype 		: 'container',
	                	layout		: 'vbox',
	                	items		: [
	                	     		    {
	                	     		    	html	: '<div style="background-color:gray;" id="'+ _this.id + '_header";></div>',
	                	     		    	height	: 70,
	                	     		    	width	: _this.width/5 - 5,
	                	     		    	margin	: '0 0 5 0'
	                	     		    },
	                	     		   	this.getGrid()
	                	     		   
	                	]
	                },
	                {
	                	xtype 		: 'container',
	                	layout		: 'vbox',
	                	items		: [
			                	     		{
			              	                	xtype 		: 'container',
			              	                	layout		: 'hbox',
			              	                	width		: this.width*4/5 - 40,
			              	                	height		: this.height*2/3 - 10,
			              	                	items		: [
			              	                	     		  {
				      			              	                	html	: '<br/><div id="' + this.id + '_plot" ></div>',
				      			              	                	width	: this.width*3/5 - 40,
				      			              	                	height	: this.height*2/3 - 10,
				      			              	                	margin	: '0 5 0 5'
			              	                	     		  },
				              	                	     	{
				      			              	                	xtype 		: 'container',
				      			              	                	layout		: 'vbox',
				      			              	                	items 		: [
				      			              	                	      		   	this.getLegendGrid(),
				      			              	                	      		   	this.getSettingsPanel(this.width/5 - 10)
			              	                	     		  					]
				              	                	     		}
			              	                	]
			              	                },
			              	                {
			              	                	xtype 		: 'container',
			              	                	layout		: 'hbox',
			              	                	width		: this.width*4/5 - 40,
			              	                	height		: this.height/2 - 10,
			              	                	margin		: "5 0 0 0",
			              	                	items		: [
			              	                	     		  {
				      			              	                	html			: '<div id="' + this.id + '_scattering" ></div>',
				      			              	                	width			: this.width*1/5 - 20,
				      			              	                	height			: this.height/3 - 45,
				      			              	                	margin			: '0 5 0 5'
			              	                	     		  },
			              	                	     		  {
				              	                	     			  html			: '<div id="' + this.id + '_kratky" ></div>',
				              	                	     			  width			: this.width*1/5  - 20,
				              	                	     			  height		: this.height/3 - 45,
				              	                	     			  margin		: '0 5 0 5'
			              	                	     		  },
			              	                	     		  {
				      			              	                	html			: '<div id="' + this.id + '_guinier" ></div>',
				      			              	                	width			: this.width*1/5  - 20,
				      			              	                	height			: this.height/3 - 45,
				      			              	                	margin			: '0 5 0 5'
			              	                	     		  },
			              	                	     		  {
				      			              	                	html			: '<div id="' + this.id + '_gnom" ></div>',
				      			              	                	width			: this.width*1/5  - 20,
				      			              	                	height			: this.height/3 - 45,
				      			              	                	margin			: '0 5 0 5'
			              	                	     		  },
			              	                	]
			              	                }
	                	]
	                }
	               
	                
            ]
	    });
	
	 return this.panel;
};
 







function CurveVisualizer(args){
	if (args == null){
		args = {};
	}
	this.width = 800;
	this.height = 500;
	if (args != null){
		if (args.width != null){
			this.width = args.width;
		}
		
		if (args.height != null){
			this.height = args.height;
		}
	}
	
	DataCollectionCurveVisualizer.prototype.constructor.call(this, {width : this.width, height : this.height});
};

CurveVisualizer.prototype.draw = DataCollectionCurveVisualizer.prototype.draw;
//CurveVisualizer.prototype._getPrefixes =	DataCollectionCurveVisualizer.prototype._getPrefixes;
CurveVisualizer.prototype._getFrames =DataCollectionCurveVisualizer.prototype._getFrames;
CurveVisualizer.prototype._getAverages = DataCollectionCurveVisualizer.prototype._getAverages;
CurveVisualizer.prototype._getSubtractionByDataCollectionId = DataCollectionCurveVisualizer.prototype._getSubtractionByDataCollectionId ;
CurveVisualizer.prototype.getLastMerges = DataCollectionCurveVisualizer.prototype.getLastMerges;
CurveVisualizer.prototype._prepareData = DataCollectionCurveVisualizer.prototype._prepareData;
CurveVisualizer.prototype._getImageHTML = DataCollectionCurveVisualizer.prototype._getImageHTML;
CurveVisualizer.prototype._getDataStore = DataCollectionCurveVisualizer.prototype._getDataStore;
//CurveVisualizer.prototype.getLegendGrid = DataCollectionCurveVisualizer.prototype.getLegendGrid;
//CurveVisualizer.prototype.getSettingsPanel = DataCollectionCurveVisualizer.prototype.getSettingsPanel;
CurveVisualizer.prototype.getGrid = DataCollectionCurveVisualizer.prototype.getGrid;
CurveVisualizer.prototype._renderPlotFromData = DataCollectionCurveVisualizer.prototype._renderPlotFromData;
CurveVisualizer.prototype._getCoordinates = DataCollectionCurveVisualizer.prototype._getCoordinates;
//CurveVisualizer.prototype._renderPlot = DataCollectionCurveVisualizer.prototype._renderPlot;

CurveVisualizer.prototype.refresh = function(mergeIdsList){
	this.mergeIdsList = mergeIdsList;
	this.draw();
	var _this = this;
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, averageList){
		var averageListId = [];
		if (averageList != null){
			if (averageList.length > 0){
				for(var i = 0; i < averageList.length; i++){
					var average = averageList[i];
					averageListId.push(average.mergeId);
					if (average.framelist3VO != null){
						var frames = [];
						if (average.framelist3VO.frametolist3VOs != null){
							var framesId = [];
							/** Adding averaged **/
							frames.push({
								type : "AVERAGE",
								color : "blue",
								merged : "true",
								filePath : average.averageFilePath,
								frameId : null,
								mergeId : average.mergeId
								
							});
						
							
							/** Sorting average.framelist3VO.frametolist3VOs **/
							average.framelist3VO.frametolist3VOs.sort(function(a,b){ return a.frame3VO.frameId - b.frame3VO.frameId;});
							for (var j = 0; j < average.framelist3VO.frametolist3VOs.length; j++){
								if (average.framelist3VO.frametolist3VOs[j].frame3VO != null){
									framesId.push(average.framelist3VO.frametolist3VOs[j].frame3VO.frameId);
									frames.push(average.framelist3VO.frametolist3VOs[j].frame3VO);
									
									if ((parseInt(average.framesMerge)) > j){
										frames[frames.length -1]["type"] = "FRAMES";
										frames[frames.length -1]["merged"] = true;
										frames[frames.length -1]["color"] = "green";
									}
									else{
										frames[frames.length -1]["type"] = "FRAMES";
										frames[frames.length -1]["merged"] = false;
										frames[frames.length -1]["color"] = "red";
									}
								}
							}
							/** All frames are selected so they will be plotted **/
							_this.selected = frames;
							_this.store.loadData(frames, true);
						}
					}
				}
				var adapter2 = new BiosaxsDataAdapter();
				adapter2.onSuccess.attach(function(sender, data){
					
					_this._renderPlotFromData(data);
				});
				/** Read the file and returns a json with the three columns values **/
				adapter2.getScatteringCurveByFrameIdsList(framesId, averageListId, []);
			}
		}
	});
	adapter.getMergesByIdsList(mergeIdsList);	
};



CurveVisualizer.prototype.getPanel = function (){
	var _this = this;
	this.panel =  Ext.create('Ext.form.Panel', {
			id 		: this.id + 'main', 
	        bodyStyle: 'padding:5px 5px 5px  5px',
	        height	: this.height,
	        width 	: this.width,
	        layout: 'hbox',
	        items: [  {
		                	xtype 		: 'container',
		                	layout		: 'vbox',
		                	items		: [this.getGrid()  ]
		              },
		     		  {
	  	                	html	: '<br/><div id="' + this.id + '_plot" ></div>',
	  	                	width	: this.width*4/5 - 30,
	  	                	height	: this.height - 50,
	  	                	margin	: '0 5 0 5'
		     		  }]
	    });
	
	 return this.panel;
};
 
CurveVisualizer.prototype.getFrameGrid = function (store, width, height, title, selectionMode){
	var _this = this;
	var selModel = null;
	if (selectionMode){
		selModel = Ext.create('Ext.selection.RowModel', {
			allowDeselect		: true,
			mode				: 'MULTI',
			listeners			: {
							        selectionchange: function (sm, selections) {
							            var selected = new Array();
							            for (var i = 0; i < selections.length; i++) {
							                selected.push(selections[i].raw);
							            }
							            _this._renderPlot(selected);
							        }
			}
		});
	}
	
	return Ext.create('Ext.grid.Panel', {
	    store				: store,
	    height 				: height - 50,
	    width				: width,
	    selModel			: selModel,
	    features 			: [{ftype:'grouping'}],
	    dockedItems: [{
	        xtype: 'toolbar',
	        dock: 'bottom',
	        items: [
	            { 
	            	xtype	: 'button', 
	            	text	: 'Download',
	            	handler	: function(){
	            		var fileName = "download";
	            		var url = "/ispyb/user/dataadapter.do?reqCode=getZipFileByAverageListId&fileName=" + fileName
    					+ "&mergeIdsList=" + _this.mergeIdsList.toString() + "&subtractionIdList=" + _this.subtractionIdList.toString();
	            		
	            		console.log(url);
	            		window.location.href = url;
	            	} 
	            }
	        ]
	    }],
	    columns				: [
	           				   		{ 
	           				   			text			: title,  
	           				   			dataIndex		: 'filePath', 
	           				   			flex			: 1,
	           				   			renderer		:   function(val,b,record,a){ 
	           				   									val = val.split("/")[val.split("/").length -1];
	           				   									var fontweight = 'italic';
	           				   									var color = record.raw.color;
	           				   									
	           				   									if (record.raw.merged){
	           				   										fontweight = 'bold';
	           				   									}
	           				   									else{
	           				   										if (record.raw.type == "FRAMES"){
	           				   											val = val;
	           				   										}
	           				   									}
	           				   									if (((record.raw.type == "SUBTRACTED")||(record.raw.type == "OUT"))&&(color == _this.orderColors[1])){
	           				   										val = val + " <span style='font-size:9px;font-weight:bold;color:gray';>[Previous data collection]</span>"
	           				   									}
	           				   									return "<span style='font-weight:" + fontweight +";color:" + color + "'>" + val + "</span>";
	           				   			}
           				   			},
	           				   		{ text: 'Type',  dataIndex: 'type', flex: 1, hidden: true}
	           				   	
	    ]
	});
};

CurveVisualizer.prototype._renderDygraph = function (targetId, parsed, colors, labels){
	var dygraphObject = new StdDevDyGraph(targetId, {
		width 				: this.width*4/5 - 60,
		height				: this.height - 50,
		xlabel				: "q (nm-1)",
		showRangeSelector	: false
	});
	dygraphObject.draw(parsed, colors, labels);
};


/**
SUBTRACTION CURVE VISUALIZER
**/
function SubtractionCurveVisualizer(args){
	if (args == null){
		args = {};
	}
	this.width = 800;
	this.height = 500;
	if (args != null){
		if (args.width != null){
			this.width = args.width;
		}
		
		if (args.height != null){
			this.height = args.height;
		}
	}
	
	CurveVisualizer.prototype.constructor.call(this, {width : this.width, height : this.height});
};

SubtractionCurveVisualizer.prototype.draw = CurveVisualizer.prototype.draw;
SubtractionCurveVisualizer.prototype._getFrames =CurveVisualizer.prototype._getFrames;
SubtractionCurveVisualizer.prototype._getAverages = CurveVisualizer.prototype._getAverages;
SubtractionCurveVisualizer.prototype._getSubtractionByDataCollectionId = CurveVisualizer.prototype._getSubtractionByDataCollectionId ;
SubtractionCurveVisualizer.prototype.getLastMerges = CurveVisualizer.prototype.getLastMerges;
SubtractionCurveVisualizer.prototype._prepareData = CurveVisualizer.prototype._prepareData;
SubtractionCurveVisualizer.prototype._getImageHTML = CurveVisualizer.prototype._getImageHTML;
SubtractionCurveVisualizer.prototype._getDataStore = CurveVisualizer.prototype._getDataStore;
SubtractionCurveVisualizer.prototype.getGrid = CurveVisualizer.prototype.getGrid;
SubtractionCurveVisualizer.prototype._renderPlotFromData = CurveVisualizer.prototype._renderPlotFromData;
SubtractionCurveVisualizer.prototype._getCoordinates = CurveVisualizer.prototype._getCoordinates;

SubtractionCurveVisualizer.prototype.refresh = function(mergeIdsList, subtractionIdList){
	this.mergeIdsList = mergeIdsList;
	this.subtractionIdList = subtractionIdList;
//	this.draw();
	var _this = this;
	var adapter = new BiosaxsDataAdapter();
	_this.rawFrameData = [];
	/** Clean store **/
	_this.store.loadData([], false);
	
	var framesId = [];
	adapter.onSuccess.attach(function(sender, averageList){
		var averageListId = [];
		_this.selected = [];
		if (averageList != null){
			if (averageList.length > 0){
				for(var i = 0; i < averageList.length; i++){
					var average = averageList[i];
					averageListId.push(average.mergeId);
					if (average.framelist3VO != null){
						var frames = [];
						if (average.framelist3VO.frametolist3VOs != null){
							
							/** Adding averaged **/
							frames.push({
								type : "AVERAGE",
								color : "blue",
								merged : "true",
								filePath : average.averageFilePath,
								frameId : null,
								mergeId : average.mergeId,
								key : average.mergeId
							});
						
							/** Selection only the averages **/
							_this.selected.push(frames[frames.length-1]);

							/** Sorting average.framelist3VO.frametolist3VOs **/
							average.framelist3VO.frametolist3VOs.sort(function(a,b){ return a.frame3VO.frameId - b.frame3VO.frameId;});
							for (var j = 0; j < average.framelist3VO.frametolist3VOs.length; j++){
								if (average.framelist3VO.frametolist3VOs[j].frame3VO != null){
									var frameId = average.framelist3VO.frametolist3VOs[j].frame3VO.frameId;
									framesId.push(frameId);
									frames.push(average.framelist3VO.frametolist3VOs[j].frame3VO);
									
									frames[frames.length -1]["type"] = "FRAME";
									frames[frames.length -1]["key"] = frameId;
									
									if ((parseInt(average.framesMerge)) > j){
										frames[frames.length -1]["merged"] = true;
										frames[frames.length -1]["color"] = "#00CC66";
										
									}
									else{
										frames[frames.length -1]["merged"] = false;
										frames[frames.length -1]["color"] = "#FF6666";
									}
								}
							}
							/** NOT All frames are selected so they will NOT be plotted **/
							//_this.selected = frames;
							_this.store.loadData(frames, true);
						}
					}
				}
				var adapter2 = new BiosaxsDataAdapter();
				adapter2.onSuccess.attach(function(sender, data){
					/** We keep all the data in order to redisplay when users click in a frame **/
					_this.rawFrameData = _this.rawFrameData.concat(data);
					/**
					 * Frames to be added to the list of frames
					 */
					var subtractionFrames = [];
					var CONSTANT_SUBSTRACTION = "*SUBTRACTION*";
					
					/** Looking for the subtracted **/
					for (index in Object.keys(data)){
						var key = Object.keys(data[index])[0];
						
						if (key.indexOf(CONSTANT_SUBSTRACTION) != -1){
							framesId.push(key);
							/** Getting the filename **/
							var fileName = key.split("*")[key.split("*").length -1];
							subtractionFrames.push({
									filePath : fileName,
									color : "purple",
									merged : true,
									type : "SUBTRACTION",
									key : key
							});
						}
					}
					
					/** 
					 * This data could contains the average sample and buffer in case of having subtractions,
					 * These files are composed by:
					 * subtractionId + "_SAMPLE_AVERAGE" + filename
					 * subtractionId + "_BUFFER_AVERAGE" + filename
					 */
					var CONSTANT_SAMPLE = "*SAMPLE*AVERAGE*";
					var CONSTANT_BUFFER = "*BUFFER*AVERAGE*";
				
					for (index in Object.keys(data)){
						var key = Object.keys(data[index])[0];
						if (key.indexOf(CONSTANT_SAMPLE) != -1){
							framesId.push(key);
							/** Getting the filename **/
							var fileName = key.split("*")[key.split("*").length -1];
							subtractionFrames.push({
									filePath : fileName,
									color : "black",
									merged : true,
									type : "SUBTRACTION",
									key : key
							});
							
						}
						if (key.indexOf(CONSTANT_BUFFER) != -1){
							framesId.push(key);
							/** Getting the filename **/
							var fileName = key.split("*")[key.split("*").length -1];
							subtractionFrames.push({
									filePath : fileName,
									color : "grey",
									merged : true,
									type : "SUBTRACTION",
									key : key
							});
						}
					}
					
					_this.selected = _this.selected.concat(subtractionFrames);
					_this.store.loadData(subtractionFrames, true);
					
					/** Selecting the data from the selected **/
					var selectedKeys = {};
					for (var i in _this.selected){
						selectedKeys[_this.selected[i].key] = true;
					}
					
					var selectedData = [];
					for (var i =0; i< data.length; i++){
						var key = Object.keys(data[i])[0];
						if (selectedKeys[key] != null){
							selectedData.push(data[i]);
						}
					}
					

					_this._renderPlotFromData(selectedData);
//					_this.framesPanel.setLoading(false);
				});
				
				/** Read the file and returns a json with the three columns values **/
				adapter2.getScatteringCurveByFrameIdsList(framesId, averageListId, subtractionIdList);
			}
		}
	});
//	this.framesPanel.setLoading("Retrieving");
	adapter.getMergesByIdsList(mergeIdsList);	
};



SubtractionCurveVisualizer.prototype.getPanel = function (){
	var _this = this;
	this.panel =  Ext.create('Ext.form.Panel', {
			id 		: this.id + 'main', 
	        bodyStyle: 'padding:5px 5px 5px  5px',
	        height	: this.height,
	        width 	: this.width,
	        layout: 'hbox',
	        items: [  {
		                	xtype 		: 'container',
		                	layout		: 'vbox',
		                	items		: [this.getGrid()  ]
		              },
		     		  {
	  	                	html	: '<br/><div id="' + this.id + '_plot" ></div>',
	  	                	width	: this.width*4/5 - 30,
	  	                	height	: this.height - 50,
	  	                	margin	: '0 5 0 5'
		     		  }]
	    });
	
	 return this.panel;
};
 
SubtractionCurveVisualizer.prototype.getFrameGrid = function (store, width, height, title, selectionMode){
	var _this = this;
	var selModel = null;
	if (selectionMode){
		selModel = Ext.create('Ext.selection.RowModel', {
			allowDeselect		: true,
			mode				: 'MULTI',
			listeners			: {
							        selectionchange: function (sm, selections) {
							            var selected = new Array();
							            for (var i = 0; i < selections.length; i++) {
							                selected.push(selections[i].raw);
							            }
							            
							            /** This is because selection changes from not selected values to selected values **/
							            if (selected.length > 0){
							            	_this._renderPlot(selected);
							            }
							        }
			}
		});
	}
	this.frameGrid =  Ext.create('Ext.grid.Panel', {
	    store				: store,
	    height 				: height - 50,
	    width				: width,
	    selModel			: selModel,
	    features 			: [{ftype:'grouping'}],
	    dockedItems: [{
	        xtype: 'toolbar',
	        dock: 'bottom',
	        items: [
	            { 
	            	xtype	: 'button', 
	            	text	: 'Download',
	            	handler	: function(){
	            		var fileName = "download";
	            		var url = "/ispyb/user/dataadapter.do?reqCode=getZipFileByAverageListId&fileName=" + fileName
    					+ "&mergeIdsList=" + _this.mergeIdsList.toString() + "&subtractionIdList=" + _this.subtractionIdList.toString();
	            		window.location.href = url;
	            	} 
	            }
	        ]
	    }],
	    columns				: [
	           				   		{ 
	           				   			text			: title,  
	           				   			dataIndex		: 'filePath', 
	           				   			flex			: 1,
	           				   			renderer		:   function(val,b,record,a){ 
	           				   									val = val.split("/")[val.split("/").length -1];
	           				   									var fontweight = 'italic';
	           				   									var color = record.raw.color;
	           				   									
	           				   									if (record.raw.merged){
	           				   										fontweight = 'bold';
	           				   									}
	           				   									else{
	           				   										if (record.raw.type == "FRAMES"){
	           				   											val = val;
	           				   										}
	           				   									}
	           				   									if (((record.raw.type == "SUBTRACTED")||(record.raw.type == "OUT"))&&(color == _this.orderColors[1])){
	           				   										val = val + " <span style='font-size:9px;font-weight:bold;color:gray';>[Previous data collection]</span>"
	           				   									}
	           				   									return "<span style='font-weight:" + fontweight +";color:" + color + "'>" + val + "</span>";
	           				   			}
           				   			},
	           				   		{ text: 'Type',  dataIndex: 'type', flex: 1, hidden: true}
	           				   	
	    ]
	});
	
	return this.frameGrid;
};

SubtractionCurveVisualizer.prototype._renderDygraph = function (targetId, parsed, colors, labels){
	var dygraphObject = new StdDevDyGraph(targetId, {
		width 				: this.width*4/5 - 60,
		height				: this.height - 50,
		xlabel				: "q (nm-1)",
		showRangeSelector	: false
	});
	dygraphObject.draw(parsed, colors, labels);
};


SubtractionCurveVisualizer.prototype._renderPlot = function (selected){
	var _this = this;
	
	this.selected = selected;
	
	var data = [];
	for (var j = 0; j < selected.length; j++) {
		for (var i = 0; i < _this.rawFrameData.length; i++){
			if (Object.keys(_this.rawFrameData[i])[0] == selected[j].key){
				data.push(_this.rawFrameData[i]);
			}
		}
	}

	this._renderPlotFromData(data);
};
