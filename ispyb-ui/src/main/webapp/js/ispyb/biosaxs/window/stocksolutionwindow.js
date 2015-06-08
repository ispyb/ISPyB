StockSolutionWindow.prototype.getButtons = GenericWindow.prototype.getButtons; 
StockSolutionWindow.prototype._render = GenericWindow.prototype._render; 
StockSolutionWindow.prototype._postRender = GenericWindow.prototype._postRender; 

function StockSolutionWindow(args){
	  this.width = 550;
      this.height = 300;

       if (args != null){
          if (args.actions != null){
             this.actions = args.actions;
          }
       }
       
       _this = this;
       
       this.form = new StockSolutionForm();
       this.form.onSaved.attach(function(evt, buffer){
    	   _this.save();
       });
       
       GenericWindow.prototype.constructor.call(this, {form:this.form, width:this.width, height:this.height});
       
       /** Events **/
       this.onSaved = new Event(this);
 };

 
StockSolutionWindow.prototype.save = function (){
	var _this = this;
	var adapter = new BiosaxsDataAdapter();
	adapter.onSuccess.attach(function(sender, stockSolution){
		_this.panel.close();
		_this.onSaved.notify(stockSolution);
	});
	
//	if (this.form.getStockSolution().macromoleculeId == null){
//		BUI.showError("Macromolecule field is mandatory");
//		return;
//	}
	if (this.form.getStockSolution().bufferId == null){
		BUI.showError("Buffer field is mandatory");
		return;
	}
	
	if (this.form.getStockSolution().name == ""){
		BUI.showError("Acronym field is mandatory");
		return;
	}
	
	if (this.form.getStockSolution().concentration == ""){
		BUI.showError("Concentration field is mandatory");
		return;
	}
	
	if (this.form.getStockSolution().volume == ""){
		BUI.showError("Volume field is mandatory");
		return;
	}
	
	this.panel.setLoading("ISPyB: saving stock solution");
	adapter.saveStockSolution(this.form.getStockSolution());
	
};


StockSolutionWindow.prototype.draw = function (stockSolution, experiment){
	this.experiment = experiment;
	this.title = "Stock Solution";
	this._render(stockSolution, experiment);
};