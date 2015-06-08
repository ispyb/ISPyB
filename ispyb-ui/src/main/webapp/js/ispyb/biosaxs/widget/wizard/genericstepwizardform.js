/**
 * Class for managing wizards
 * 
 * @title
 * @description
 * @data
 * @onNextFn
 * @onBackFn
 **/ 
function GenericStepWizardForm(title, description, data, onNextFn, onBackFn){
	this.id = BUI.id();
	this.title = title;
	this.description = description;
	this.data = data;
	this.onNext = onNextFn;
	this.onBack = onBackFn;
}

GenericStepWizardForm.prototype.getForm = function(){
	alert("[getForm] Not implemented");

};

GenericStepWizardForm.prototype.getNextForm = function(){
	alert("[getNextForm] Not implemented");
};

/** When coming back to the same form **/
GenericStepWizardForm.prototype.reload = function(){
	alert("[getNextForm] Not implemented");
};


