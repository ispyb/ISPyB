function SamplePlateGroupWidget(args){
	this.id = BUI.id();
	this.width = 900;
	this.height = 300;

	this.titleTypePlate = 20;

	this.margin = 0;
	this.samplePlateWidgets = [];

	this.bbar = false;
	this.border = 1;
	this.showTitle = true;
	this.title = null;
	
	this.nodeSize = 8;
	
	if (args != null){
		if (args.margin != null){
			this.margin = args.margin;
		}
		if (args.bbar != null){
			this.bbar = args.bbar;
		}

		if (args.showTitle != null){
			this.showTitle = args.showTitle;
			if (this.showTitle){
				this.title = "Plates Groups";
			}
		}
		if (args.border != null){
			this.border = args.border;
		}
		if (args.height != null){
			this.height = args.height;
		}
	}
	
	this.heightPlates = this.height - this.titleTypePlate - 60;

	/** Events * */
	this.onClick = new Event(this);
	this.onExperimentChanged = new Event(this);
}


SamplePlateGroupWidget.prototype.drawPlate = function(experiment, plate, targetId){
	var _this = this;
	
	var samplePlateWidget = new SamplePlateWidget(
			{
				width		: (this.width/3) - 5, 
				height		: this.heightPlates + 10 , 
				nodeSize	: this.nodeSize, 
				fontSize	: 0, 
				strokeWidth	: 1.5
			});

	if (this.isVerticalLayout()){
		samplePlateWidget.width = this.width - 5;
		samplePlateWidget.height = this.heightPlates - 10;
	}
	
	samplePlateWidget.draw(experiment, plate, targetId );
	samplePlateWidget.onVertexUp.attach(function(sender, args){
		_this.onClick.notify(
				{
					samplePlate	: args.samplePlate, 
					row			: args.row, 
					column		: args.column

				}
		);
	});

	this.samplePlateWidgets.push(samplePlateWidget);
};

SamplePlateGroupWidget.prototype.drawPlates = function(experiment){
	if (experiment){
		var plateGroups = experiment.getPlateGroups();
		for ( var i = 0; i < plateGroups.length; i++) {
			var id = plateGroups[i].plateGroupId;
			var plates = experiment.getPlatesByPlateGroupId(id);
			for ( var j = 0; j < plates.length; j++) {
				var targetId = ('id', this.id + "_" + plates[j].samplePlateId);
				if (document.getElementById(targetId) != null){
					this.drawPlate(experiment, plates[j], targetId);
				}
			}
		} 
	}
};

/*
SamplePlateGroupWidget.prototype._sortPlates = function(a, b) {
	return a.slotPositionColumn - b.slotPositionColumn;
};
*/

/** This returns maxSlotPositionRow and maxSlotPositionColumn to set visually the sample changer layout **/
SamplePlateGroupWidget.prototype.getDimensions = function(plates) {
	var maxSlotPositionRow = 0;
	var maxSlotPositionColumn = 0;
	
	if (plates != null){
		for (var i = 0; i < plates.length; i++) {
			/** Row **/
			var slotPositionRow = plates[i].slotPositionRow;
			if (slotPositionRow != null){
				slotPositionRow = parseFloat(slotPositionRow);
				if (slotPositionRow > maxSlotPositionRow){
					maxSlotPositionRow = slotPositionRow;
				}
			}
			/** Column **/
			var slotPositionColumn = plates[i].slotPositionColumn;
			if (slotPositionColumn != null){
				slotPositionColumn = parseFloat(slotPositionColumn);
				if (slotPositionColumn > maxSlotPositionColumn){
					maxSlotPositionColumn = slotPositionColumn;
				}
			}
		}
	}
	return {
		"maxSlotPositionRow" 		: parseFloat(maxSlotPositionRow),
		"maxSlotPositionColumn"		: parseFloat(maxSlotPositionColumn)
	}
};

/** return true or false if the plates are going to be displayed vertically or horizontally **/
SamplePlateGroupWidget.prototype.isVerticalLayout = function() {
	var dimensions = this.getDimensions(this.experiment.getSamplePlates())
	if (dimensions.maxSlotPositionRow < dimensions.maxSlotPositionColumn){
		return false;
	}
	return true;
};

/** This returns a sample plate object based on the position it occupies on the sample plate **/
SamplePlateGroupWidget.prototype.getPlateBySlotPosition = function(plates, row, column) {
	if (plates != null){
		for (var i = 0; i < plates.length; i++) {
			/** Row **/
			if ((plates[i].slotPositionRow == row)&&(plates[i].slotPositionColumn == column)){
				return plates[i];
			}
		}
	}
	return null;
};

/** Returns the html that will be used to display the plates **/
SamplePlateGroupWidget.prototype.getPlatesContainer = function(experiment){
	var plateGroups = [];
	
	if (experiment!= null){
		plateGroups = experiment.getPlateGroups();
	}

	var div  = document.createElement("div");
	var table  = document.createElement("table");

	table.setAttribute('width', this.width - 30 + 'px');
	table.setAttribute('height', this.heightPlates + 'px');
	
	for ( var i = 0; i < plateGroups.length; i++) {
		var id = plateGroups[i].plateGroupId;
		var plates = experiment.getPlatesByPlateGroupId(id);
		var dimensions = this.getDimensions(plates);
		
		for ( var j = 1; j <= dimensions.maxSlotPositionRow; j++) {
			var tr = document.createElement("tr");
			for ( var k = 1; k <= dimensions.maxSlotPositionColumn; k++) {
				var plate = this.getPlateBySlotPosition(plates,j,k);
				var td = document.createElement("td");
				td.setAttribute('id', this.id + "_" + plate.samplePlateId);
				td.setAttribute('style', "background-color:#E6E6E6;border-width:1px;border-style:solid;");
				/** plate Type title * */
				var divTitle = document.createElement("div");
				divTitle.setAttribute("class", "menu-title");
				var text = document.createTextNode(plate.platetype3VO.name);
				divTitle.appendChild(text);
				
				td.appendChild(divTitle);
				tr.appendChild(td);
			}
			table.appendChild(tr);
		}
	} 
	div.appendChild(table);
	return div.innerHTML;
};

SamplePlateGroupWidget.prototype.selectSpecimens = function(specimens){
	/** Clear previous selected * */
	for ( var i = 0; i < this.samplePlateWidgets.length; i++) {
		this.samplePlateWidgets[i].clearSelection();
	}
	for ( var i = 0; i < specimens.length; i++) {
		this.selectSpecimen(specimens[i]);
	}
};

SamplePlateGroupWidget.prototype.selectSpecimen = function(specimen){
	if (specimen.sampleplateposition3VO != null){
		var samplePlateId = specimen.sampleplateposition3VO.samplePlateId;
		for ( var i = 0; i < this.samplePlateWidgets.length; i++) {
			var samplePlateId = this.samplePlateWidgets[i].samplePlate.samplePlateId;
			if ( this.samplePlateWidgets[i].samplePlate.samplePlateId == specimen.sampleplateposition3VO.samplePlateId){
				this.samplePlateWidgets[i].selectSpecimen(specimen);
				return;
			}
		}
	}
};

SamplePlateGroupWidget.prototype._refreshBbar = function(){
	if (this.panel != null){
		this.panel.removeDocked(Ext.getCmp( this.id + 'bbar'));
		this.panel.addDocked(this.getBbar());
	}
};

SamplePlateGroupWidget.prototype.refresh = function(experiment){
	this.experiment = experiment;
	this.samplePlateWidgets = [];

	if (document.getElementById(this.id + "_container") != null){
		document.getElementById(this.id + "_container").innerHTML = "";
		document.getElementById(this.id + "_container").innerHTML = this.getPlatesContainer(experiment);
		this.drawPlates(experiment);
	}

	/** We refrsh also the bbar  but it could not exist yet* */
	this._refreshBbar();	
	

};

SamplePlateGroupWidget.prototype._getAutoFillButton = function(){
	var _this = this;
	var item = null;
	function onButtonClick(){

	}

	function showResult(answer){
		if (answer == 'yes'){
			var samplePlateId = item.samplePlateId;
			var adapter = new BiosaxsDataAdapter();
			_this.panel.setLoading("ISPyB: Saving Plate");
			adapter.onSuccess.attach(function(sender, json){
				_this.onExperimentChanged.notify(json);
				_this.panel.setLoading(false);
			});
			adapter.onError.attach(function(sender){
				alert("error");
				_this.panel.setLoading(false);
			});
			adapter.autoFillPlate(samplePlateId, _this.experiment.experimentId);
		}
	}

	function onItemClick(i){
		item = i;
		Ext.MessageBox.confirm('Confirm', 'Are you sure you want to fill "'+ _this.experiment.getSamplePlateById(item.samplePlateId).platetype3VO.name + '" plate?', showResult);
	}


	var items = [];
	items.push( '<b class="menu-title">Select a plate:</b>');

	if (this.experiment){
		var plates = this.experiment.getSamplePlates();
		plates.sort(function(a,b){return a.slotPositionColumn - b.slotPositionColumn;});
		for ( var i = 0; i < plates.length; i++) {
			items.push({
				samplePlateId			:  plates[i].samplePlateId,
				text					: (i+1) + '.- <b> ' + plates[i].platetype3VO.name +'</b>', 
				handler				: onItemClick
			});
		}
	}

	return Ext.create('Ext.button.Split', {
		text		: 'Auto Fill',
		handler		: onButtonClick,
		tooltip		: {text:'This will fill, if there is place, the specimens in the plate', title:'Auto Fill'},
		menu : {
			items: items
		}
	});
};

SamplePlateGroupWidget.prototype._getZoomButton = function(){
	var _this = this;
	var item = null;

	function onItemClick(item){
// Ext.MessageBox.confirm('Confirm', 'Are you sure you want to empty "'+
// _this.experiment.getSamplePlateById(item.samplePlateId).platetype3VO.name +
// '" plate?', showResult);
		Ext.create('Ext.window.Window', {
			title	: _this.experiment.getSamplePlateById(item.samplePlateId).platetype3VO.name,
			height	: 600,
			width	: 900,
			layout	: 'fit',
			modal	: true,
			listeners: {
				afterrender: function(){
					var samplePlateWidget = new SamplePlateWidget(
							{
								width		: 880, 
								height		: 560, 
								nodeSize	: 4, 
								fontSize	: 0, 
								strokeWidth	: 1.5,
								showLabels	: true,
								backgroundColor : '#FFFFFF'
							});

					samplePlateWidget.draw(_this.experiment, _this.experiment.getSamplePlateById(item.samplePlateId), "plateZoom" );
				}
			},
			items	: [
			     	   {
			     		   html 	: '<div id="plateZoom" style="background-color:red"></div>',
			     		   padding 	: 5
			     	   }
			     	   ]
		}).show();

	}


	var items = [];
	if (this.experiment){
		var plates = this.experiment.getSamplePlates();
		plates.sort(function(a,b){return a.slotPositionColumn - b.slotPositionColumn;});
	
		items.push( '<b class="menu-title">Select a plate:</b>');
		for ( var i = 0; i < plates.length; i++) {
			items.push({
				samplePlateId			:  plates[i].samplePlateId,
				text					: (i+1) + '.- <b> ' + plates[i].platetype3VO.name +'</b>', 
				handler				: onItemClick
			});
		}
	}

	return Ext.create('Ext.button.Split', {
		text		: 'Zoom In',
		tooltip		: {text:'This will show a plate bigger', title:'Zoom In Action'},
		menu 		: {
			items: items
		}
	});
};


SamplePlateGroupWidget.prototype._getEmptyButton = function(){
	var _this = this;
	var item = null;
	function onButtonClick(){

	}

	function showResult(answer){
		if (answer == 'yes'){
			var samplePlateId = item.samplePlateId;
			var adapter = new BiosaxsDataAdapter();
			_this.panel.setLoading("ISPyB: Saving Plate");
			adapter.onSuccess.attach(function(sender, json){
				_this.onExperimentChanged.notify(json);
				_this.panel.setLoading(false);
			});
			adapter.onError.attach(function(sender){
				alert("error");
				_this.panel.setLoading(false);
			});
			adapter.emptyPlate(samplePlateId, _this.experiment.experimentId);
		}
	}

	function onItemClick(i){
		item = i;
		Ext.MessageBox.confirm('Confirm', 'Are you sure you want to empty "'+ _this.experiment.getSamplePlateById(item.samplePlateId).platetype3VO.name + '" plate?', showResult);
	}


	var items = [];
	
	if (this.experiment){
		var plates = this.experiment.getSamplePlates();
		plates.sort(function(a,b){return a.slotPositionColumn - b.slotPositionColumn;});
	
		items.push( '<b class="menu-title">Select a plate:</b>');
		for ( var i = 0; i < plates.length; i++) {
			items.push({
				samplePlateId			:  plates[i].samplePlateId,
				text					: (i+1) + '.- <b> ' + plates[i].platetype3VO.name +'</b>', 
				handler				: onItemClick
			});
		}
	}
	return Ext.create('Ext.button.Split', {
		text		: 'Empty',
		handler		: onButtonClick,
		tooltip		: {text:'This will empty the specimen of a plate. It will NOT remove the specimen but will removed their position', title:'Empty Action'},
		menu : {
			items: items
		}
	});
};

SamplePlateGroupWidget.prototype._getPlateTypes = function(){
	var _this = this;
	var item = null;
	function onButtonClick(){

	}

	function changeType(answer){
		if (answer == 'yes'){
			var samplePlateId = item.samplePlateId;
			var plateTypeId = item.plateTypeId;

			var plateType = BIOSAXS.proposal.getPlateTypeById(plateTypeId);
			var plate = _this.experiment.getSamplePlateById(samplePlateId);
			plate.platetype3VO = plateType;

			var samplePlateId = item.samplePlateId;
			var adapter = new BiosaxsDataAdapter();
			_this.panel.setLoading("ISPyB: Saving Plate");
			adapter.onSuccess.attach(function(sender, json){
				_this.onExperimentChanged.notify(json);
				_this.panel.setLoading(false);
			});
			adapter.onError.attach(function(sender){
				alert("error");
				_this.panel.setLoading(false);
			});
			adapter.savePlates([plate], _this.experiment.experimentId);
		}
	}

	function onItemCheck(i){
		item = i;
		var samplePlateId = i.samplePlateId;
		var plateTypeId = i.plateTypeId;

		if (_this.experiment.getSamplePlateById(samplePlateId).platetype3VO.plateTypeId != plateTypeId){
			Ext.MessageBox.confirm('Confirm', 'Are you sure you want to change the type of "'+ _this.experiment.getSamplePlateById(item.samplePlateId).platetype3VO.name + '" plate?', changeType);
		}
	}


	var items = [];
	items.push( '<b class="menu-title">Select an EMPTY plate:</b>');
	
	var plates = [];
	if (this.experiment != null){
		plates = this.experiment.getSamplePlates();
	}
	plates.sort(function(a,b){return a.slotPositionColumn - b.slotPositionColumn;});


	var types = BIOSAXS.proposal.getPlateTypes();

	for ( var i = 0; i < plates.length; i++) {
		var plate = plates[i];
		var itemTypes = [];
		for ( var j = 0; j < types.length; j++) {

			itemTypes.push({
				text			: types[j].name,
				plateTypeId		: types[j].plateTypeId,
				samplePlateId	: plate.samplePlateId,
				checked			: (plate.platetype3VO.plateTypeId == types[j].plateTypeId),
				group			: 'theme' + plate.samplePlateId,
				checkHandler	: onItemCheck
			});
		}

		items.push({
			text		: (i+1) + '.- <b> ' + plate.platetype3VO.name +'</b>',
			disabled	: (_this.experiment.getSpecimensBySamplePlateId(plate.samplePlateId).length > 0),
			menu		: {       
				items		: itemTypes
			}
		});
	}

	return Ext.create('Ext.button.Split', {
		text		: 'Plate Types',
		handler		: onButtonClick,
		tooltip		: {text:'This will change the type of a plate.', title:'Change plate type'},
		menu : {
			items: items
		}
	});
};

SamplePlateGroupWidget.prototype.getBbar = function(experiment){
	var _this = this;
	if (this.bbar){
		return  Ext.create('Ext.toolbar.Toolbar', {
			id	: _this.id + 'bbar',
			dock: 'bottom',
			items: [
			        this._getPlateTypes(),
			        "-",
			        this._getAutoFillButton(),
			        this._getEmptyButton(),
			        "-",
			        this._getZoomButton()
			        ]
		});
	}
	return null;
};

SamplePlateGroupWidget.prototype.getPanel = function(){
	var _this = this;
	
	var id = this.id + "_container"; 
	this.panel =  Ext.create('Ext.panel.Panel', {
		title		: this.title,
		bbar		: this.getBbar(),
		height		: this.height,
		width		: this.width,
		border		: this.border,
		items		: [
					        {
					        	border		: 0,
					        	height		: this.height,
					        	id			: id,
					        	html		: "<div id='"+ id +"'></div>"
					        }
					   ]
	});

	this.panel.on("afterrender", function(){
		document.getElementById(id).innerHTML = _this.getPlatesContainer(_this.experiment);
		_this.drawPlates(_this.experiment);
		_this._refreshBbar();
	});
	return this.panel ;
};