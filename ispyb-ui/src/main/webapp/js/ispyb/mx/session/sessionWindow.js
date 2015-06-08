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
 * Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/

SessionWindow.prototype.getButtons = GenericWindow.prototype.getButtons;
SessionWindow.prototype._render = GenericWindow.prototype._render;
SessionWindow.prototype._postRender = GenericWindow.prototype._postRender;

// window which contains the sessionForm, opened when the user clicks on Edit Session
// Parent is a genericWindow
function SessionWindow(args) {
	this.title = "Edit session";
	this.width = 450;
	this.height = 300;

	this.isFx = false;
	this.isIx = false;

	if (args != null) {
		if (args.isFx != null) {
			this.isFx = args.isFx;
		}
		if (args.isIx != null) {
			this.isIx = args.isIx;
		}
	}
	if (this.isFx) {
		this.height = 500;
	}
	if (this.isIx) {
		this.height = 450;
	}
	// form panel inside
	this.form = new SessionForm(args);
	// generic window parent
	GenericWindow.prototype.constructor.call(this, {
				form : this.form,
				width : this.width,
				height : this.height
			});
}

// save button click: throws a Save event and close the window
SessionWindow.prototype.save = function() {
	this.onSaved.notify(this.form.getSession());
	this.panel.close();
};

// cancel button: close the panel
SessionWindow.prototype.cancel = function() {
	this.panel.close();
};

// display of the window: initialize the title and the form with the given session
SessionWindow.prototype.draw = function(session) {
	this.title = "Edit session";
	this._render(session);
};