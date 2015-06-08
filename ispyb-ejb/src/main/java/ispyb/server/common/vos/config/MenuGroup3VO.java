/*************************************************************************************************
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
 ****************************************************************************************************/

package ispyb.server.common.vos.config;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ispyb.server.common.vos.ISPyBValueObject;

import org.apache.log4j.Logger;

/**
 * MenuGroup3 value object mapping table MenuGroup
 * 
 */
@Entity
@Table(name = "MenuGroup")
public class MenuGroup3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(MenuGroup3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "menuGroupId")
	private Integer menuGroupId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "welcomePage")
	private String welcomePage;
	
	@Column(name = "description")
	private String description;

	public MenuGroup3VO() {
		super();
	}

	public MenuGroup3VO(Integer menuGroupId, String name, String welcomePage,
			String description) {
		super();
		this.menuGroupId = menuGroupId;
		this.name = name;
		this.welcomePage = welcomePage;
		this.description = description;
	}
	
	public MenuGroup3VO(MenuGroup3VO vo) {
		super();
		this.menuGroupId = vo.getMenuGroupId();
		this.name = vo.getName();
		this.welcomePage = vo.getWelcomePage();
		this.description = vo.getDescription();
	}


	public Integer getMenuGroupId() {
		return menuGroupId;
	}

	public void setMenuGroupId(Integer menuGroupId) {
		this.menuGroupId = menuGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWelcomePage() {
		return welcomePage;
	}

	public void setWelcomePage(String welcomePage) {
		this.welcomePage = welcomePage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Checks the values of this value object for correctness and
	 * completeness. Should be done before persisting the data in the DB.
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @throws Exception if the data of the value object is not correct
	 */
	public void checkValues(boolean create) throws Exception {
		//TODO
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public MenuGroup3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (MenuGroup3VO) super.clone();
	}
}
