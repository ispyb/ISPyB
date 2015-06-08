
/**
 * #onSaved
 */
function ExperimentWindow(args){
   this.id = BUI.id();
   this.actions = new Array();

   if (args != null){
      if (args.actions != null){
         this.actions = args.actions;
      }
   }
   
   this.onSaved = new Event(this);
};

ExperimentWindow.prototype.getButtons = function(){
		var _this = this;
		
		return [ {
			text : 'Save',
			handler : function() {
					var experimentId =	_this.experiment.json.experimentId;
					var name =	Ext.getCmp(_this.form.id + 'name').getValue();
					var type =	Ext.getCmp(_this.form.id + 'type').getValue();
					var comments =	Ext.getCmp(_this.form.id + 'comments').getValue();
					var adapter = new BiosaxsDataAdapter();
					_this.form.panel.setLoading("ISPyB: Saving experiment");
					adapter.onSuccess.attach(function(sender){
						_this.form.panel.setLoading(false);
						_this.onSaved.notify(
								{
									name		: name,
									type		: type,
									comments	: comments
								}
						);
						_this.panel.close();
					});
					adapter.onError.attach(function(sender){
						_this.form.panel.setLoading(false);
						BUI.showError("Ooops: There was an error");
					});
					adapter.saveExperiment(experimentId, name, type, comments);
			}
		}, {
			text : 'Cancel',
			handler : function() {
				_this.panel.close();
			}
		} ];
	};

ExperimentWindow.prototype.show = function(experiment){
	this.experiment = experiment;
	this.form = new ExperimentForm();
	this.panel = Ext.create('Ext.window.Window', {
	    title			: 'Experiment',
	    height			: 300,
	    width			: 500,
	    layout			: 'fit',
	    buttonAlign		:'right',
		buttons			: this.getButtons(),
	    items			: [ 	this.form.getPanel(experiment)	]
	});
	this.panel.show();
};
