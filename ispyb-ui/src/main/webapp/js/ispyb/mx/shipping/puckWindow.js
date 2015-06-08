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
/*global PuckForm */
/*global BUI */

// puck window, in order to edit a puck (puck form inside). The parent is a Generic Window
function PuckWindow(args) {
	this.width = 400;
	this.height = 100;
	this.addMode = true;
	if (args != null) {
		if (args.addMode != null) {
			this.addMode = args.addMode;
		}
	}
			
	this.form = new PuckForm(args);
	GenericWindow.prototype.constructor.call(this, {
		form : this.form,
		width : this.width,
		height : this.height
	});
}

PuckWindow.prototype.getButtons = GenericWindow.prototype.getButtons;
PuckWindow.prototype._render = GenericWindow.prototype._render;
PuckWindow.prototype._postRender = GenericWindow.prototype._postRender;

// save button: throws an event and close the panel
PuckWindow.prototype.save = function () {
	if (!this.form.isValid()) {
		BUI.showError("The puck label is required");
		return;
	}
	this.onSaved.notify(this.form.getPuck());
	this.panel.close();
};

//cancel button
PuckWindow.prototype.cancel = function () {
	this.onCancelled.notify();
	this.panel.close();
};

// display the window
PuckWindow.prototype.draw = function (puck) {
	this.title = "Edit puck";
	if (this.addMode == true) {
		this.title = "Add a new puck";
	}
	this._render(puck);
};