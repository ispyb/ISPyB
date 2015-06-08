/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * ISPyB is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P.
 * Brenchereau, M. Bodin,  A. De Maria Antolinos
 ******************************************************************************/

/*global GenericWindow */
/*global DewarForm */

// Dewar window (from genericWindow) to edit the dewar (just label for now)
function DewarWindow(args) {
	this.width = 400;
	this.height = 100;
	this.addMode = true;
	if (args != null) {
		if (args.addMode != null) {
			this.addMode = args.addMode;
		}
	}
	// form panel inside		
	this.form = new DewarForm(args);
	GenericWindow.prototype.constructor.call(this, {
		form : this.form,
		width : this.width,
		height : this.height
	});
}

DewarWindow.prototype.getButtons = GenericWindow.prototype.getButtons;
DewarWindow.prototype._render = GenericWindow.prototype._render;
DewarWindow.prototype._postRender = GenericWindow.prototype._postRender;	

// save button: check the form, throws an event and close the window
DewarWindow.prototype.save = function () {
	if (!this.form.isValid()){
		BUI.showError("The dewar label is required");
		return;
	}
	this.onSaved.notify(this.form.getDewar());
	this.panel.close();
};

// cancel button
DewarWindow.prototype.cancel = function () {
	this.onCancelled.notify();
	this.panel.close();
};


// display the window, with the given dewar values
DewarWindow.prototype.draw = function (dewar) {
	this.title = "Edit dewar";
	if (this.addMode == true){
		this.title = "Add a new dewar";
	}
	this._render(dewar);
};