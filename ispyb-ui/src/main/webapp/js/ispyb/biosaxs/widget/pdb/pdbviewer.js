function PDBViewer(args) {
	this.id = BUI.id();
	this.glMol = null;

	this.width = 600;
	this.height = 600;
	this.title = "";

	this.margin = "10 0 0 5";
	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
		if (args.title != null) {
			this.title = args.title;
		}
		if (args.margin != null) {
			this.margin = args.margin;
		}
	}
}

PDBViewer.prototype.getTitle = function() {
	return "<div style='width:" + this.width + "px; height:20px; font-weight:bold;background-color: #E6E6E6;color:black'>" + this.title + "</div>";
};

PDBViewer.prototype.getTextAreaId = function() {
	return this.id + "_src";
};

PDBViewer.prototype.getCanvas = function() {
	/** For text Area **/
	var textAreaStyle = "width:" + this.width + "px; height: " + this.height + "px;display: none;";
	var textArea = "<textarea id='" + this.getTextAreaId() + "'; style='" + textAreaStyle + "' ></textarea>";
	var style = "width: " + this.width + "px; height: " + this.height + "px; background-color: black;";
	return textArea + "<div id='" + this.id + "'; style='" + style + "' ></div>";

};

PDBViewer.prototype.getDownload = function(type, abInitioModelId) {
	/** For title **/
	var url = BUI.getPdbURL() + '&type=' + type + '&abInitioModelId=' + abInitioModelId;
	html = '<a href=' + url + ' style="color:blue;font-weight:bold;"  height="80" width="80" >Download</a><br /><br />';
	return "<div style='width:" + this.width + "px; height:20px; font-weight:bold;background-color: #336699;color:white'>" + html + "</div>";
};

PDBViewer.prototype.getBar = function() {
	/** For title **/
	return "<div style='width:" + this.width + "px; height:20px; font-weight:bold;background-color: #336699;color:white'></div>";
};

PDBViewer.prototype.refresh = function(models) {
	var _this = this;
	if (BUI.isWebGLEnabled()) {
		this.models = models;
		var adapter = new BiosaxsDataAdapter();
		_this.panel.setLoading("Rendering");
		adapter.onSuccess.attach(function(sender, data) {
			if (data.models != null){
				if (data.models[0] != null){
					document.getElementById(_this.getTextAreaId()).innerHTML = data.models[0].XYZ;
					if (_this.glMol == null) {
						_this.glMol = new GLmol(_this.id);
					} else {
						_this.glMol.loadMolecule();
					}
				}
			}
			_this.panel.setLoading(false);
		});
		adapter.onError.attach(function(sender, data) {
			_this.panel.setLoading("Not available");
		});
		adapter.getDataPDB(models, []);
		
	} else {
		this.webGLNotAvailable();
	}
};

PDBViewer.prototype.getOpacity = function(text) {
	if (text == 'Invisible') {
		return '0';
	}
	if (text == 'Minimum') {
		return '0.2';
	}
	if (text == 'Medium') {
		return '0.5';
	}
	if (text == 'High') {
		return '0.7';
	}
	return '1';
};

PDBViewer.prototype.getMenu = function(model) {
	var _this = this;
	function onItemCheck(comp, checked, eOpts) {
		if (checked) {
			var i = null;
			if (comp.group == 'Opacity') {
				for (i = 0; i < _this.models.length; i++) {
					var opacity = _this.getOpacity(comp.text);
					model.opacity = opacity;
				}
			}

			if (comp.group == 'Radius') {
				for (i = 0; i < _this.models.length; i++) {
					var radius = _this.getOpacity(comp.text);
					model.radius = radius;
				}
			}

			_this.refresh(_this.models);
		}
	}

	return Ext.create('Ext.menu.Menu', {
		items : [ {
			text : 'Opacity',
			menu : {
				items : [ {
					text : 'Invisible',
					checked : false,
					group : 'Opacity',
					checkHandler : onItemCheck
				}, {
					text : 'Minimum',
					checked : false,
					group : 'Opacity',
					checkHandler : onItemCheck
				}, {
					text : 'Medium',
					checked : false,
					group : 'Opacity',
					checkHandler : onItemCheck
				}, {
					text : 'High',
					checked : false,
					group : 'Opacity',
					checkHandler : onItemCheck
				}, {
					text : 'Opaque',
					checked : false,
					group : 'Opacity',
					checkHandler : onItemCheck
				} ]
			}
		}

		]
	});
};

PDBViewer.prototype.getTbar = function() {
	var _this = this;

	var tb = Ext.create('Ext.toolbar.Toolbar');

	var colorItems = [];
	for (var i = 0; i < this.models.length; i++) {
		tb.add({
			text : this.models[i].title,
			menu : this.getMenu(this.models[i])
		});
		var color = "#" + this.models[i].color.replace("0x", "");
		colorItems.push({
			html : "<table><tr><td width='15px'>" + BUI.getRectangleColorDIV(color, 10, 10) + "</td><td>" + this.models[i].title + "</td></table>"
		});
	}

	tb.add({
		xtype : 'numberfield',
		labelWidth : 50,
		width : 120,
		fieldLabel : 'Radius',
		value : 3,
		maxValue : 10,
		step : 0.2,
		minValue : 0.1,
		listeners : {
			change : function(cmp) {
				var radius = cmp.getValue();
				for (var i = 0; i < _this.models.length; i++) {
					_this.models[i].radius = radius;
				}
				_this.refresh(_this.models);
			}
		}
	});
	tb.add("->");
	tb.add(colorItems);
	return tb;
};

PDBViewer.prototype.getPanel = function() {
	var _this = this;
	this.panel = Ext.create('Ext.panel.Panel', {
		margin : this.margin,
		border : 0,
		layout : {
			type : 'vbox'
		},
		width : this.width - 4,
		height : this.height + 30,
		items : [ {
			html : this.getCanvas()
		} ],
		listeners : {
			afterRender : function() {
			}
		}
	});

	this.panel.setLoading("Rendering");
	return this.panel;
};

PDBViewer.prototype.webGLNotAvailable = function() {
	document.getElementById(_this.id).innerHTML = BUI.getWarningHTML("Your browser doesn't support WebGL");
	document.getElementById(_this.id).innerHTML = document.getElementById(_this.id).innerHTML + "<br />";
	document.getElementById(_this.id).innerHTML = document.getElementById(_this.id).innerHTML
			+ BUI.getTipHTML("<a href='http://www.browserleaks.com/webgl#howto-enable-disable-webgl'>How to enable WebGL</a>");
	document.getElementById(_this.id).innerHTML = document.getElementById(_this.id).innerHTML + "<br />";
	document.getElementById(_this.id).innerHTML = document.getElementById(_this.id).innerHTML + BUI.getTipHTML("<a href='http://caniuse.com/webgl'>Can I use WebGL?</a>");
};

/** Deprecated **/
PDBViewer.prototype.draw = function(models) {
	this.models = models;
	var _this = this;
	this.panel = Ext.create('Ext.panel.Panel', {
		margin : 2,
		layout : {
			type : 'vbox'
		},
		tbar : this.getTbar(),
		width : this.width - 4,
		height : this.height + 30,
		items : [ {
			html : this.getCanvas()
		}

		],
		listeners : {
			afterRender : function() {
				_this.refresh(models);
			}
		}
	});

	this.panel.setLoading("Rendering");
	return this.panel;
};

SuperpositionPDBViewer.prototype.draw = PDBViewer.prototype.draw;
SuperpositionPDBViewer.prototype.getBar = PDBViewer.prototype.getBar;
SuperpositionPDBViewer.prototype.getCanvas = PDBViewer.prototype.getCanvas;
SuperpositionPDBViewer.prototype.getDownload = PDBViewer.prototype.getDownload;
SuperpositionPDBViewer.prototype.getMenu = PDBViewer.prototype.getMenu;
SuperpositionPDBViewer.prototype.getOpacity = PDBViewer.prototype.getOpacity;
SuperpositionPDBViewer.prototype.getPanel = PDBViewer.prototype.getPanel;
SuperpositionPDBViewer.prototype.getTbar = PDBViewer.prototype.getTbar;
SuperpositionPDBViewer.prototype.getTextAreaId = PDBViewer.prototype.getTextAreaId;
SuperpositionPDBViewer.prototype.getTitle = PDBViewer.prototype.getTitle;
SuperpositionPDBViewer.prototype.refresh = PDBViewer.prototype.refresh;
SuperpositionPDBViewer.prototype.webGLNotAvailable = PDBViewer.prototype.webGLNotAvailable;

function SuperpositionPDBViewer(args) {
	this.width = 550;
	this.height = 500;
	if (args != null) {
		if (args.actions != null) {
			this.actions = args.actions;
		}
	}

	var _this = this;
	this.form = new BufferForm();
	this.onSuccess = new Event();
	PDBViewer.prototype.constructor.call(this, args);
};

SuperpositionPDBViewer.prototype.refresh = function(superpositions) {
	var _this = this;
	if (BUI.isWebGLEnabled()) {
		this.models = superpositions;
		var adapter = new BiosaxsDataAdapter();
		_this.panel.setLoading("Rendering");
		adapter.onSuccess.attach(function(sender, data) {
			document.getElementById(_this.getTextAreaId()).innerHTML = data.XYZ;
			if (_this.glMol == null) {
				_this.glMol = new GLmol(_this.id);
			} else {
				_this.glMol.loadMolecule();
			}
			_this.panel.setLoading(false);
		});
		adapter.onError.attach(function(sender, data) {
			_this.panel.setLoading("Not available");
		});
		adapter.getAbinitioPDBContentBySuperpositionList(superpositions);
	} else {
		this.webGLNotAvailable();
	}
};




AlignedSuperpositionPDBViewer.prototype.draw = PDBViewer.prototype.draw;
AlignedSuperpositionPDBViewer.prototype.getBar = PDBViewer.prototype.getBar;
AlignedSuperpositionPDBViewer.prototype.getCanvas = PDBViewer.prototype.getCanvas;
AlignedSuperpositionPDBViewer.prototype.getDownload = PDBViewer.prototype.getDownload;
AlignedSuperpositionPDBViewer.prototype.getMenu = PDBViewer.prototype.getMenu;
AlignedSuperpositionPDBViewer.prototype.getOpacity = PDBViewer.prototype.getOpacity;
AlignedSuperpositionPDBViewer.prototype.getPanel = PDBViewer.prototype.getPanel;
AlignedSuperpositionPDBViewer.prototype.getTbar = PDBViewer.prototype.getTbar;
AlignedSuperpositionPDBViewer.prototype.getTextAreaId = PDBViewer.prototype.getTextAreaId;
AlignedSuperpositionPDBViewer.prototype.getTitle = PDBViewer.prototype.getTitle;
AlignedSuperpositionPDBViewer.prototype.refresh = PDBViewer.prototype.refresh;
AlignedSuperpositionPDBViewer.prototype.webGLNotAvailable = PDBViewer.prototype.webGLNotAvailable;

function AlignedSuperpositionPDBViewer(args) {
	this.width = 550;
	this.height = 500;
	if (args != null) {
		if (args.actions != null) {
			this.actions = args.actions;
		}
	}

	var _this = this;
	this.form = new BufferForm();
	this.onSuccess = new Event();
	PDBViewer.prototype.constructor.call(this, args);
};

AlignedSuperpositionPDBViewer.prototype.refresh = function(superpositions) {
	var _this = this;
	if (BUI.isWebGLEnabled()) {
		this.models = superpositions;
		
		_this.panel.setLoading("Rendering PDB");
		

		var adapter = new BiosaxsDataAdapter();
		adapter.onError.attach(function(sender, pdbContent){
			console.log(sender)
		});
		adapter.onSuccess.attach(function(sender, data){
			var superposition = data.superpositions[0][superpositions[0]];
			document.getElementById(_this.id).innerHTML = ""
			_this.glMol = new GLmol(_this.id, false, false, superposition.apriori.data);
			_this.glMol.addSuperpositionXYZ(superposition.aligned.XYZ, "0xFFFFFF", 0.3, 1);
			
			var view = _this.glMol.getView();
			_this.glMol.setView(view);
   
			
			_this.panel.setLoading(false);
		});
		if (superpositions != null){
			if (superpositions.length != 0){
				adapter.getDataPDB([], superpositions);
			}
		}
		
		
		
	} else {
		this.webGLNotAvailable();
	}
};







StructurePDBViewer.prototype.draw = PDBViewer.prototype.draw;
StructurePDBViewer.prototype.getBar = PDBViewer.prototype.getBar;
StructurePDBViewer.prototype.getCanvas = PDBViewer.prototype.getCanvas;
StructurePDBViewer.prototype.getDownload = PDBViewer.prototype.getDownload;
StructurePDBViewer.prototype.getMenu = PDBViewer.prototype.getMenu;
StructurePDBViewer.prototype.getOpacity = PDBViewer.prototype.getOpacity;
StructurePDBViewer.prototype.getPanel = PDBViewer.prototype.getPanel;
StructurePDBViewer.prototype.getTbar = PDBViewer.prototype.getTbar;
StructurePDBViewer.prototype.getTextAreaId = PDBViewer.prototype.getTextAreaId;
StructurePDBViewer.prototype.getTitle = PDBViewer.prototype.getTitle;
StructurePDBViewer.prototype.refresh = PDBViewer.prototype.refresh;
StructurePDBViewer.prototype.webGLNotAvailable = PDBViewer.prototype.webGLNotAvailable;

function StructurePDBViewer(args) {
	this.width = 550;
	this.height = 500;
	if (args != null) {
		if (args.actions != null) {
			this.actions = args.actions;
		}
	}

	var _this = this;
	this.form = new BufferForm();
	this.onSuccess = new Event();
	PDBViewer.prototype.constructor.call(this, args);
};

StructurePDBViewer.prototype.refresh = function(structures) {
	var _this = this;
	if (BUI.isWebGLEnabled()) {
		this.models = structures;
//		var adapter = new BiosaxsDataAdapter();
		_this.panel.setLoading("Rendering");
//		adapter.onSuccess.attach(function(sender, data) {
//			document.getElementById(_this.getTextAreaId()).innerHTML = data.XYZ;
//			if (_this.glMol == null) {
//				_this.glMol = new GLmol(_this.id);
//			} else {
//				_this.glMol.loadMolecule();
//			}
//			_this.panel.setLoading(false);
//		});
		
		var adapter = new BiosaxsDataAdapter();
		adapter.onSuccess.attach(function(sender, pdbContent){
//			_this.pdbContainer.setLoading("Rendering PDB");
			
			document.getElementById(_this.getTextAreaId()).innerHTML = pdbContent;
			var glmol02 = new GLmol(_this.id);
			
			
			_this.panel.setLoading(false);
		});
//		_this.pdbContainer.setLoading("Reading PDB");
//		adapter.getStructureFile(172);
		
//		adapter.onError.attach(function(sender, data) {
//			_this.panel.setLoading("Not available");
//		});
//		adapter.getPDBContentBySuperpositionList(superpositions);
	} else {
		this.webGLNotAvailable();
	}
};