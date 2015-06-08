BufferWindow.prototype.getButtons = GenericWindow.prototype.getButtons; 
BufferWindow.prototype._render = GenericWindow.prototype._render; 
BufferWindow.prototype._postRender = GenericWindow.prototype._postRender; 

function BufferWindow(args){
	  this.width = 550;
      this.height = 500;

       if (args != null){
          if (args.actions != null){
             this.actions = args.actions;
          }
       }
       
       var _this = this;
       this.form = new BufferForm();
       
       
       this.onSuccess = new Event();
       GenericWindow.prototype.constructor.call(this, {form:this.form, width:this.width, height:this.height});
 };

 
BufferWindow.prototype.save = function (){
	var _this = this;
	
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, proposal){
		BIOSAXS.proposal.setItems(proposal);
		_this.onSuccess.notify(proposal);
		_this.panel.close();
	});
	
	if (this.form.getBuffer().name == ""){
		BUI.showError("Name field is mandatory");
		return;
	}
	if (this.form.getBuffer().acronym == ""){
		BUI.showError("Acroynm field is mandatory");
		return;
	}
	if (this.form.getBuffer().composition == ""){
		BUI.showError("Composition field is mandatory");
		return;
	}
	this.panel.setLoading("ISPyB: Saving buffer")
	adapter.saveBuffer(this.form.getBuffer());
};


BufferWindow.prototype.draw = function (buffer){
	if (buffer.bufferId == null){
		this.title =  'New buffer';
	}
	else{
		this.title =  buffer.name;
	}
	this._render(buffer);
};