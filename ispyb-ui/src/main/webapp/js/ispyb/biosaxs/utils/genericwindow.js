 function GenericWindow(args){
       this.title = "title";
       this.width = 700;
       this.height = 500;
       this.id = BUI.id();
       
       this.close = false;
       this.draggable = true;
       this.modal = true;
       
       if (args != null){
          if (args.actions != null){
             this.actions = args.actions;
          }
          if (args.form != null){
              this.form = args.form;
           }
          if (args.width != null){
              this.width = args.width;
           }
          if (args.modal != null){
              this.modal = args.modal;
           }
          
          if (args.height != null){
              this.height = args.height;
           }
          if (args.title != null){
              this.title = args.title;
           }
          if (args.form != null){
              this.form = args.form;
           }
          if (args.close != null){
              this.close = args.close;
           }
          if (args.draggable != null){
              this.draggable = args.draggable;
           }
          
       }
       /** Events **/
       this.onSaved = new Event(this);
 };
 
 

GenericWindow.prototype.getButtons = function() {
	var _this = this;
	
	if (this.close){
		return [ {
			text : 'Close',
			handler : function() {
				_this.panel.close();
			}
		} ];
	}
	else{
		return [ {
			text : 'Save',
			handler : function() {
				_this.save();
				
			}
		}, {
			text : 'Cancel',
			handler : function() {
				_this.panel.close();
			}
		} ];
	}
};

GenericWindow.prototype.save = function (){
	 alert("Method save of GenerciWindow class is abstract");
};

GenericWindow.prototype._postRender = function(data, experiment){
	
};


GenericWindow.prototype.draw = function (data, experiment){
	this._render(data, experiment);
};

GenericWindow.prototype.refresh = function(data, experiment){
	 this.data = data;
	 this.experiment = experiment;
	 this.form.refresh(data, experiment);
};

GenericWindow.prototype._render = function(data, experiment){
    this.data = data;
 	var _this = this;
	if (this.panel == null){
		this.panel  = Ext.create('Ext.Window', {
			id				: this.id,
			title 			: this.title,
			resizable		: true,
			constrain		: true,
			border			: 1,
			modal			: this.modal,
			frame			: false,
			draggable		: this.draggable,
			closable		: true,
			autoscroll		: true,
			layout			: { type: 'vbox',align: 'stretch'},
			width			: this.width,
            height			: this.form.height,
			buttonAlign		:'right',
			buttons			: this.getButtons(),
			items			: this.form.getPanel(data, experiment),
	 		listeners: {
	 						scope		: this,
	 						minimize	: function(){
	 										this.panel.hide();
	 						},
	 						destroy		: function(){
	 										delete this.panel;
	 						}
	    	}
		});
		this.panel.setLoading();
	}
	
	this.panel.show();
	this._postRender();
};