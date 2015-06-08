/**
 * Edit the information of a buffer
 * 
 * #onSaved
 * #onRemoveAdditive
 */
function BufferForm() {
	var _this = this;

	this.additiveGrid = new AdditiveGrid();
	this.additiveGrid.onRemoveButtonClicked.attach(function(sender, args) {
		_this.onRemoveAdditive.notify(args);
	});

	this.onSaved = new Event(this);
	this.onRemoveAdditive = new Event(this);
}

BufferForm.prototype.getBuffer = function() {
	this.buffer.name = Ext.getCmp("buffer_name").getValue();
	this.buffer.acronym = Ext.getCmp("buffer_acronym").getValue();
	this.buffer.comments = Ext.getCmp("buffer_comments").getValue();
	this.buffer.ph = Ext.getCmp("buffer_ph").getValue();
	this.buffer.composition = Ext.getCmp("buffer_composition").getValue();
	this.buffer.bufferhasadditive3VOs = this.additiveGrid.getAdditives();
	return this.buffer;
};

BufferForm.prototype._getTopPanel = function() {
	return {
		xtype : 'container',
		layout : 'hbox',
		border : 0,
		frame : true,
		items : [ {
			xtype : 'container',
			layout : 'hbox',
			items : [ {
				xtype : 'container',
				flex : 1,
				border : false,
				layout : 'anchor',
				defaultType : 'textfield',
				items : [ {
					xtype : 'requiredtext',
					id : 'buffer_name',
					fieldLabel : 'Name',
					name : 'name',
					width : '200px',
					value : this.buffer.name
				}, {
					xtype : 'requiredtext',
					id : 'buffer_acronym',
					fieldLabel : 'Acronym',
					name : 'acronym',
					width : '200px',
					value : this.buffer.acronym
				} ]
			} ]
		}, {
			xtype : 'container',
			flex : 1,
			layout : 'anchor',
			defaultType : 'textfield',
			margin : '0 0 0 10',
			items : [ {
				id : 'buffer_ph',
				fieldLabel : 'pH',
				name : 'ph',
				value : this.buffer.ph,
				xtype : 'numberfield',
				width : 200,
				minValue : 0,
				maxValue : 15
			}, {
				xtype : 'requiredtext',
				id : 'buffer_composition',
				fieldLabel : 'Composition',
				name : 'composition',
				width : 200,
				value : this.buffer.composition
			} ]
		} ]
	};
};

BufferForm.prototype.getPanel = function(buffer) {
	this.buffer = buffer;
	this.panel = Ext.createWidget({
		xtype : 'container',
		layout : 'vbox',
		style : {
			padding : '10px'
		},
		fieldDefaults : {
			labelAlign : 'left',
			labelWidth : 50

		},
		items : [ this._getTopPanel(), {
			id : 'buffer_comments',
			xtype : 'textareafield',
			name : 'comments',
			fieldLabel : 'Comments',
			width : '100%',
			value : buffer.comments
		}, this.additiveGrid.getPanel(buffer) ]
	});
	return this.panel;
};

BufferForm.prototype.input = function() {
	return {
		buffer : {
			"bufferId" : 422,
			"proposalId" : 10,
			"safetyLevelId" : null,
			"name" : "B1",
			"acronym" : "B1",
			"ph" : null,
			"composition" : null,
			"bufferhasadditive3VOs" : [],
			"comments" : null
		}
	};
};

BufferForm.prototype.test = function(targetId) {
	var bufferForm = new BufferForm();
	var panel = bufferForm.getPanel(bufferForm.input().buffer);
	panel.render(targetId);
};
