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
 * ContainerDO.java
 * 
 * Created on Jun 21, 2005
 *
 * ESRF - The European Synchrotron Radiation Facility
 * 6 RUE JULES HOROWITZ,
 * BP 220, 38043 GRENOBLE CEDEX 9, FRANCE
 * Tel +33 (0)4 76 88 20 00 - Fax +33 (0)4 76 88 20 20
 */
package ispyb.client.mx.container;

/**
 * 
 * @author ricardo.leal@esrf.fr
 * 
 * Jun 21, 2005
 *
 */
public class ContainerDO {

    private int index;
    private String type;
    private String capacity;
    
    
    /**
     * @param name
     * @param capacity
     */
    public ContainerDO(String type, String capacity) {
        this.type = type;
        this.capacity = capacity;
    }
    
    /**
     * @param index
     * @param type
     * @param capacity
     */
    public ContainerDO(int index, String type, String capacity) {
        this.index = index;
        this.type = type;
        this.capacity = capacity;
    }
    /**
     * @return Returns the capacity.
     */
    public String getCapacity() {
        return capacity;
    }
    /**
     * @param capacity The capacity to set.
     */
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
    /**
     * @return Returns the name.
     */
    public String getType() {
        return type;
    }
    /**
     * @param name The name to set.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return Returns the index.
     */
    public int getIndex() {
        return index;
    }
    /**
     * @param index The index to set.
     */
    public void setIndex(int index) {
        this.index = index;
    }
    

}
