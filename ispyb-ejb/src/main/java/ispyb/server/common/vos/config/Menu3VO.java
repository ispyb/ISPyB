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

import ispyb.server.common.vos.ISPyBValueObject;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * Menu3 value object mapping table Menu
 * 
 */
@Entity
@Table(name = "Menu")
public class Menu3VO extends ISPyBValueObject implements Cloneable {

	private final static Logger LOG = Logger.getLogger(Menu3VO.class);

	// generate the serialVersionUID using the 'serialver' tool of java and enter it here
	// this prevents later invalid class version exceptions when the value object evolves
	private static final long serialVersionUID = 1234567901234567890L;

	@Id
	@GeneratedValue
	@Column(name = "menuId")
	private Integer menuId;

	@Column(name = "parentId")
	private Integer parentId;

	@Column(name = "name")
	private String name;

	@Column(name = "action")
	private String action;

	@Column(name = "sequence")
	private Integer sequence;

	@Column(name = "description")
	private String description;

	@Column(name = "expType")
	private String expType;

	@ManyToMany
	@JoinTable(name = "Menu_has_MenuGroup", joinColumns = { @JoinColumn(name = "menuId", referencedColumnName = "menuId") }, inverseJoinColumns = { @JoinColumn(name = "menuGroupId", referencedColumnName = "menuGroupId") })
	private Set<MenuGroup3VO> menuGroupVOs;

	public Menu3VO() {
		super();
	}

	public Menu3VO(Integer menuId, Integer parentId, String name, String action, Integer sequence, String description) {
		super();
		this.menuId = menuId;
		this.parentId = parentId;
		this.name = name;
		this.action = action;
		this.sequence = sequence;
		this.description = description;
	}

	public Menu3VO(Menu3VO vo) {
		super();
		this.menuId = vo.getMenuId();
		this.parentId = vo.getParentId();
		this.name = vo.getName();
		this.action = vo.getAction();
		this.sequence = vo.getSequence();
		this.description = vo.getDescription();
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExpType() {
		return expType;
	}

	public void setExpType(String expType) {
		this.expType = expType;
	}

	public Set<MenuGroup3VO> getMenuGroupVOs() {
		return menuGroupVOs;
	}

	public void setMenuGroupVOs(Set<MenuGroup3VO> menuGroupVOs) {
		this.menuGroupVOs = menuGroupVOs;
	}

	/**
	 * Checks the values of this value object for correctness and completeness. Should be done before persisting the
	 * data in the DB.
	 * 
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @throws Exception
	 *             if the data of the value object is not correct
	 */
	@Override
	public void checkValues(boolean create) throws Exception {
		// TODO
	}

	/**
	 * used to clone an entity to set the linked collectio9ns to null, for webservices
	 */
	@Override
	public Menu3VO clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Menu3VO) super.clone();
	}
}
