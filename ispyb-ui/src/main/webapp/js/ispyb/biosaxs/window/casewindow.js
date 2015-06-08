CaseWindow.prototype.getButtons = GenericWindow.prototype.getButtons; 
CaseWindow.prototype._render = GenericWindow.prototype._render; 
CaseWindow.prototype._postRender = GenericWindow.prototype._postRender; 

function CaseWindow(args){
	  this.width = 720;
      this.height = 500;

       if (args != null){
          if (args.actions != null){
             this.actions = args.actions;
          }
       }
       
       var _this = this;
       this.onRemoveAdditive = new Event(this);
       
       this.form = new CaseForm();
       this.form.onAddPlates.attach(function(sender, plates){
    	   _this.onSavePlates.notify(plates);
    	   
       });
       
       this.form.onRemovePlates.attach(function(sender, plate){
    	   _this.onSavePlates.notify([plate]);
       });

       GenericWindow.prototype.constructor.call(this, {
    	   													title		: 'Edit Case',
    	   													form		: this.form, 
    	   													width		: this.width, 
    	   													height		: this.height
	   													});
       
 }

CaseWindow.prototype.save = function (){
		/** Checking values **/
		if (this.form.getDewar().code == ""){
			BUI.showError("Code field is mandatory");
			return;
		}
	
	  this.onSaved.notify(this.form.getDewar());
	  this.panel.close();
};


CaseWindow.prototype.draw = function (dewar, plates){
	this.dewar = dewar;
	this.plates = plates;
	this._render(dewar, plates);
};