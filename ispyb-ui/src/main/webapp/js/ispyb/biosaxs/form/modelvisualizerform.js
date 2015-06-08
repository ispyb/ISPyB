

function ModelVisualizerForm(args){
	this.id =BUI.id();
	this.width = 600;
	this.height = 400;
	if (args!= null){
		if (args.width != null){
			this.width = args.width;
		}
		if (args.height != null){
			this.height = args.height;
		}
	}
};

ModelVisualizerForm.prototype._getFirHTML = function(modelId, width, height, type, desc) {
	var html = "<table>";
	html = html + '<tr style="text-align: center;"><td><a style="color:blue;font-weight:bold;" target="_blank" href="' + BUI.getModelFile("FIT", modelId, "txt") + '" >dammin.' + type + '</a></td></tr>';
	html = html + '<tr style="text-align: center;font-style:italic;color:gray;"><td>' + desc + '</td></tr>';
	html = html + '<tr><td><div background-color="red" id="' + (this.id + type + modelId) + '" width="' + width + '" height="' + height + '"></div></td></tr>';
	html = html + '</table>';
	return html;
};

ModelVisualizerForm.prototype.getItems = function(modelPanel){
	_this = this;
	var height = _this.height/2 - 60;
	var width = _this.width/2 - 10;
	
	return  Ext.create('Ext.container.Container', {
	    layout: {
	        type: 'vbox',       // Arrange child items vertically
	    },
	    items: [
	            	modelPanel,
	            	{
	            		 xtype : 'container',
	            		 layout: {
	            		        type: 'hbox',       // Arrange child items vertically
	            		    },
			            	items : [{
			            		html :  this._getFirHTML("id", width, height, "fir", "Fit of the simulated scattering curve versus a smoothed experimental data (spline interpolation)"),
			            		height :height,
			            		width : width,
			            		padding: 2
			            	},
			            	{
			            		html :  this._getFirHTML("id", width, height, "fit", "Fit of the simulated scattering curve versus the experimental data."),
			            		height : height,
			            		width :  width,
			            		padding: 2
			            	}]
	            	}
	            	
	    ]
	});
};

ModelVisualizerForm.prototype.refresh = function(models){
	var input = [];
//	var colors = ["008000", "F0A804",  "0000FF", "800080", "C0C0C0"];
	for (var i = 0; i < models.length; i++) {
		console.log(BUI.rainbow(models.length, i).replace("#", "0x"));
		input.push({
			color: BUI.rainbow(models.length, i).replace("#", "0x"),
			modelId: models[i].modelId,
			opacity: 0.8,
			radius: 3,
			title: BUI.getFileNameByPath(models[i].pdbFile),
			type: "SHAPEDETERMINATIONMODEL"
			
		});
	}
	
	this.panel.removeAll();
	this.panel.add(
			_this.getItems(
								new PDBViewer({
									width : this.width - 10,
									height : (this.height/2) - 10,
									title : ""
								}).draw(input)
			)
	);
	
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, data) {
		var splitted = data.toString().split("\n");
		var array = [];
		for ( var i = 0; i < splitted.length; i++) {
			var line = splitted[i].trim();
			var line_splited = line.split(/\s*[\s,]\s*/);
			if (line_splited.length == 4) {
				array.push([ Number(line_splited[0]), BUI.getStvArray(line_splited[1], line_splited[2]), BUI.getStvArray(line_splited[3], 0) ]);
			}
		}
		
		var id = (_this.id + "firid");
		var dygraphWidget = new StdDevDyGraph(id, {
			width : (_this.width/2) - 10,
			height :(_this.height/2) -110,
			xlabel : 'q(nm<sup>-1</sup>)'
		});
		dygraphWidget.draw(array, [ "#FF0000", "#0000FF", "#FF00FF" ], [ 's', 'I(exp)', 'I(sim)' ]);
	});
	
	adapter.getModelFile("FIR", models[0].modelId);
	
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, data) {
		var splitted = data.toString().split("\n");
		var array = [];
		for ( var i = 0; i < splitted.length; i++) {
			var line = splitted[i].trim();
			var line_splited = line.split(/\s*[\s,]\s*/);
			if (line_splited.length == 3) {
				array.push([ Number(line_splited[0]), BUI.getStvArray(line_splited[1], 0), BUI.getStvArray(line_splited[2], 0) ]);
			}
		}
		
		var id = (_this.id + "fitid");
		
		var dygraphWidget = new StdDevDyGraph(id, {
			width : (_this.width/2) - 10,
			height : (_this.height/2) - 110,
			xlabel : 'q(nm<sup>-1</sup>)'
		});
		dygraphWidget.draw(array, [ "#FF0000", "#0000FF" ], [ "s", "I(exp)", "I(sim)" ]);
	});
	adapter.getModelFile("FIT", models[0].modelId);
};

ModelVisualizerForm.prototype.getPanel = function(modelList){
	_this = this;
	this.modelList = modelList;
	this.panel =  Ext.create('Ext.Panel', {
	    title: 'Results',
	    width: this.width,
	    height: this.height,
	    layout: {
	        type: 'vbox',       // Arrange child items vertically
//	        align: 'stretch'    // Each takes up full width
	    },
	    items: [
		          
	    ],
		listeners : {
			afterrender : function(grid, eOpts) {
//				alert(_this.modelList)
			}
		}
	});
	
	return this.panel;

};

