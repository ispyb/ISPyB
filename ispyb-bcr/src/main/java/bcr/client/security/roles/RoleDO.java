/*
 * Created on Nov 29, 2004
 *
 * Ricardo LEAL
 * ESRF - European Synchrotron Radiation Facility
 * B.P. 220
 * 38043 Grenoble Cedex - France
 * Phone: 00 33 (0)4 38 88 19 59
 * Fax: 00 33 (0)4 76 88 25 42
 * ricardo.leal@esrf.fr
 */
package bcr.client.security.roles;

import java.io.Serializable;

/**
 * RolesDO
 */
public class RoleDO implements Serializable {

    int id;
    private String name;
    // store welcome page value
    private String value;
    
    
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "[ " + this.id + " : " + this.name + " : " + this.value + " ]"; 
    }
}
