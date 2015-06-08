function PrepareExperimentWidget(args) {
	this.id = BUI.id();
	this.height = 800;
	this.width = 900;

	if (args != null) {
		if (args.width != null) {
			this.width = args.width;
		}
		if (args.height != null) {
			this.height = args.height;
		}
	}

	var _this = this;

	/** Macromolecule Grid **/
	this.macromoleculeGrid = new MacromoleculeGrid({
		width : 425,
		height : 350,
		collapsed : false,
		tbar : true
	});

	this.macromoleculeGrid.onMacromoleculesChanged.attach(function(sender) {
		_this.getProposal();

	});

	/** Buffer Grid **/
	this.bufferGrid = new BufferGrid({
		width : 425,
		height : 350,
		collapsed : false,
		tbar : true
	});

	/** Experiment Grid **/
	this.templateGrid = new TemplateGrid({
		width : this.width,
		minHeight : 300,
		height : 440,
		gridType : 'Ext.grid.Panel',
		title : 'Experiments',
		grouping : false,
		tbar : true

	});
}

PrepareExperimentWidget.prototype.draw = function(targetId) {
	this.renderPanel(targetId);
	this.getProposal();
	this.getTemplates();
};

PrepareExperimentWidget.prototype.renderPanel = function(targetId) {
	this.panel = Ext.create('Ext.panel.Panel', {
		plain : true,
		frame : false,
		border : 0,
		height : this.height,
		width : this.width,
		renderTo : targetId,
		items : [ {
			xtype : 'container',
			layout : 'hbox',
			margin : "0, 0, 0, 0",
			items : [ {
				xtype : 'container',
				layout : 'vbox',
				margin : "0, 0, 0, 0",
				width : 450,
				items : [ this.macromoleculeGrid.getPanel([]) ]
			}, this.bufferGrid.getPanel([]) ]
		}, {
			xtype : 'container',
			layout : 'hbox',
			margin : "10, 0, 0, 0",
			items : [ this.templateGrid.getPanel([]) ]
		}

		]
	});
};

PrepareExperimentWidget.prototype.getTemplates = function() {
	var _this = this;

	var adapter = new BiosaxsDataAdapter();

	adapter.onSuccess.attach(function(sender, data) {
		_this.templateGrid.refresh(data);
		_this.templateGrid.grid.setLoading(false);
	});
	_this.templateGrid.grid.setLoading("ISPyB: Retrieving Templates");
	adapter.getExperimentInformationByProposalId();

};

PrepareExperimentWidget.prototype.getProposal = function() {
	var _this = this;

	BIOSAXS.proposal.onInitialized = new Event();
	BIOSAXS.proposal.onInitialized.attach(function(sender) {
		_this.macromoleculeGrid.refresh(BIOSAXS.proposal.macromolecules);
		_this.bufferGrid.refresh(BIOSAXS.proposal.buffers);
		_this.macromoleculeGrid.grid.setLoading(false);
		_this.bufferGrid.grid.setLoading(false);
	});

	_this.macromoleculeGrid.grid.setLoading("ISPyB: Retrieving Macromolecules");
	_this.bufferGrid.grid.setLoading("ISPyB: Retrieving Buffers");
	BIOSAXS.proposal.init();

};
