/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/

package ispyb.client.mx.collection;

/**
 * a Param is composed by a text and a value. The key allows us to retrieve a param in  a list.
 * Keys are defined in ViewSessionSummaryAction KEY_...
 * @author BODIN
 *
 */
public class Param {
	protected String text;
	protected String value;
	
	protected String key;
	
	protected boolean isTooltip;
	
	public Param() {
		super();
	}
	
	public Param(String key, String text, String value, boolean isTooltip) {
		super();
		this.key = key;
		this.text = text;
		this.value = value;
		this.isTooltip = isTooltip;
	}
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isTooltip() {
		return isTooltip;
	}

	public void setTooltip(boolean isTooltip) {
		this.isTooltip = isTooltip;
	}
	
	
}
