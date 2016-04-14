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
/*
 * ShippingInformation
 * @author ludovic.launer@esrf.fr
 * Feb 20, 2008
 */

package ispyb.common.util.upload;

import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.util.ArrayList;

// ----------------------------------- ShippingInformation ------------------------------------------------------
public class ShippingInformation {
	public Shipping3VO shipping = null;

	public ArrayList<DewarInformation> listDewars = new ArrayList<DewarInformation>();

	// ---------------------------------------- DewarInformation -----------------------------------------
	public class DewarInformation {
		public Dewar3VO dewar = null;

		public ArrayList<ContainerInformation> listContainers = new ArrayList<ContainerInformation>();

		// ------------------------------------------- ContainerInformation -------------------
		public class ContainerInformation {
			public Container3VO container = null;

			public ArrayList<SampleInformation> listSamples = new ArrayList<SampleInformation>();

			// ---------------------------------------------- SampleInformation ----------
			public class SampleInformation {
				public BLSample3VO sample = null;

				public Crystal3VO crystal = null;

				public Protein3VO protein = null;

				public Crystal3VO getCrystal() {
					return crystal;
				}

				public void setCrystal(Crystal3VO crystal) {
					this.crystal = crystal;
				}

				public Protein3VO getProtein() {
					return protein;
				}

				public void setProtein(Protein3VO protein) {
					this.protein = protein;
				}

				public BLSample3VO getSample() {
					return sample;
				}

				public void setSample(BLSample3VO sample) {
					this.sample = sample;
				}
			} // --------------------------------------------------------------------------

			public Container3VO getContainer() {
				return container;
			}

			public void setContainer(Container3VO container) {
				this.container = container;
			}

			public ArrayList<SampleInformation> getListSamples() {
				return listSamples;
			}

			public void etListSamples(ArrayList<SampleInformation> listSamples) {
				this.listSamples = listSamples;
			}
		} // -----------------------------------------------------------------------------------

		public Dewar3VO getDewar() {
			return dewar;
		}

		public void setDewar(Dewar3VO dewar) {
			this.dewar = dewar;
		}

		public ArrayList<ContainerInformation> getListContainers() {
			return listContainers;
		}

		public void setListContainers(ArrayList<ContainerInformation> listContainers) {
			this.listContainers = listContainers;
		}
	} // --------------------------------------------------------------------------------------------------

	public ArrayList<DewarInformation> getListDewars() {
		return listDewars;
	}

	public void setListDewars(ArrayList<DewarInformation> listDewars) {
		this.listDewars = listDewars;
	}

	public Shipping3VO getShipping() {
		return shipping;
	}

	public void setShipping(Shipping3VO shipping) {
		this.shipping = shipping;
	}
}
