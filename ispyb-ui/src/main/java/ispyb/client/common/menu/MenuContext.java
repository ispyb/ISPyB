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
 * Created on Oct 6, 2004 - 10:50:55 AM
 *
 * Ricardo LEAL
 * ESRF - The European Synchrotron Radiation Facility
 * BP220 - 38043 Grenoble Cedex - France
 * Phone: 00 33 (0)4 38 88 19 59
 * ricardo.leal@esrf.fr
 *
 */
package ispyb.client.common.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import ispyb.common.util.Constants;
import ispyb.server.common.services.config.Menu3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.config.Menu3VO;

/**
 * This class has everything related to TOP and LEFT Menus.
 * 
 */
public class MenuContext {

	private final static Logger LOG = Logger.getLogger(MenuContext.class);

	// group of user
	private int groupId;

	// list of MenuItem
	private ArrayList<MenuItem> menuTop = null;

	// List of MenuItem
	private ArrayList<MenuItem> menuLeft = null;

	// active Ids Menus
	// -1 means no menu active
	private int activeTopMenu = -1;

	private int activeLeftMenu = -1;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private Menu3Service menuService;

	private String proposalType = null;

	private void initServices() {
		try {
			this.menuService = (Menu3Service) ejb3ServiceLocator.getLocalService(Menu3Service.class);
		} catch (NamingException e) {
			LOG.error(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Constructor
	 * 
	 * @param groupId
	 * @param proposalCode
	 */
	public MenuContext(int groupId, String proposalType) {
		
		if (proposalType != null && proposalType.equals(Constants.PROPOSAL_OTHER)) {
			// for now for other techniques no new menus exist so take the one of MX
			// TODO change later or go to EXI
			this.setProposalType(Constants.PROPOSAL_MX);
		}
		else {
			this.proposalType = proposalType;
		}
		this.groupId = groupId;
		initServices();
		createTopMenu();
		createLeftMenu();
	}

	/**
	 * Creates the top menu
	 */
	private void createTopMenu() {
		ArrayList<MenuItem> menu = this.fetchTopMenu();
		if (menu != null) {
			Iterator<MenuItem> it = menu.iterator();
			this.menuTop = new ArrayList<MenuItem>();
			while (it.hasNext()) {
				MenuItem menuItem = it.next();
				if (menuItem.getParentId() == 0) {
					// LOG.debug("*** Creating TOP Menu:
					// " + menuItem.toString());
					this.menuTop.add(menuItem);
				}
			}
		}

	}

	/**
	 * Creates the left menu
	 */
	private void createLeftMenu() {

		// fecth 1st level menus
		ArrayList<MenuItem> menuListFirstLevel = this.fetchMenu(this.activeTopMenu);
		if (menuListFirstLevel != null) {
			Iterator<MenuItem> it = menuListFirstLevel.iterator();
			this.menuLeft = new ArrayList<MenuItem>();
			// iterate 1st level list
			while (it.hasNext()) {
				MenuItem menuItemFirstLevel = it.next();
				// LOG.debug("MenuLeft ##### " +
				// menuItemFirstLevel.name);
				ArrayList<MenuItem> menuListSecondLevel = this.fetchMenu(menuItemFirstLevel.id);
				if (menuListSecondLevel != null) {
					Iterator<MenuItem> it2 = menuListSecondLevel.iterator();
					menuItemFirstLevel.subMenus = new ArrayList<MenuItem>();
					while (it2.hasNext()) {
						MenuItem menuItemSecondLevel = it2.next();
						// LOG.debug("MenuLeft ##########
						// " + menuItemSecondLevel.name);
						menuItemFirstLevel.subMenus.add(menuItemSecondLevel);
					}
				}
				this.menuLeft.add(menuItemFirstLevel);
			}
		}
	}

	/**
	 * To Fecth data from database
	 * 
	 * @return
	 */
	public ArrayList<MenuItem> fetchTopMenu() {

		ArrayList<MenuItem> menuList = null;
		try {

			LOG.info("Fetching Menu for groupId=" + groupId + " filtering by proposal type= " + proposalType);

			// ArrayList menuFetched = (ArrayList) menu.findAll();
			// ArrayList menuFetched = (ArrayList) menu.findTopMenuByGroupId(new Integer(this.groupId));
			
			/** If it is not a user with no proposals **/
			if (((this.groupId == 1)&&(proposalType == null)) == false){
					List<Menu3VO> menuFetched = menuService.findFiltered(null, new Integer(this.groupId), proposalType);
					Iterator<Menu3VO> it = menuFetched.iterator();
		
					menuList = new ArrayList<MenuItem>();
		
					while (it.hasNext()) {
		
						Menu3VO menuValue = it.next();
						MenuItem menuItem = new MenuItem();
						// LOG.debug("Menu: " +
						// menuValue.toString());
		
						menuItem.setId(menuValue.getMenuId() == null ? 0 : menuValue.getMenuId().intValue());
						menuItem.setParentId(menuValue.getParentId() == null ? 0 : menuValue.getParentId().intValue());
						menuItem.setName(menuValue.getName());
						menuItem.setUrl(menuValue.getAction() == null ? null : menuValue.getAction());
						menuList.add(menuItem);
					}
			}

		} catch (NamingException ne) {
			LOG.error(ne.toString());
			ne.printStackTrace();
		} catch (CreateException ce) {
			LOG.error(ce.toString());
			ce.printStackTrace();
		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
		}
		return menuList;
	}

	/**
	 * Fecth menu for the given parentId
	 * 
	 * @param parentId
	 * @return
	 */
	public ArrayList<MenuItem> fetchMenu(int parentId) {

		ArrayList<MenuItem> menuList = null;
		try {

			List<Menu3VO> menuFetched = menuService.findFiltered(new Integer(parentId), new Integer(this.groupId), this.proposalType);
			Iterator<Menu3VO> it = menuFetched.iterator();

			menuList = new ArrayList<MenuItem>();

			while (it.hasNext()) {

				Menu3VO menuValue = it.next();
				MenuItem menuItem = new MenuItem();

				menuItem.setId(menuValue.getMenuId() == null ? 0 : menuValue.getMenuId().intValue());
				menuItem.setParentId(menuValue.getParentId() == null ? 0 : menuValue.getParentId().intValue());
				menuItem.setName(menuValue.getName());
				menuItem.setUrl(menuValue.getAction() == null ? null : menuValue.getAction());
				menuList.add(menuItem);
			}

		} catch (NamingException ne) {
			LOG.error(ne.toString());
			ne.printStackTrace();
		} catch (CreateException ce) {
			LOG.error(ce.toString());
			ce.printStackTrace();
		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
		}
		return menuList;
	}

	/**
	 * @return Returns the menuLeft.
	 */
	public ArrayList<MenuItem> getMenuLeft() {
		if (menuLeft == null)
			createLeftMenu();
		return menuLeft;
	}

	/**
	 * @return Returns the activeLeftMenu.
	 */
	public int getActiveLeftMenu() {
		return activeLeftMenu;
	}

	/**
	 * @param activeLeftMenu
	 *            The activeLeftMenu to set.
	 */
	public void setActiveLeftMenu(int activeLeftMenu) {
		this.activeLeftMenu = activeLeftMenu;
	}

	/**
	 * @return Returns the activeTopMenu.
	 */
	public int getActiveTopMenu() {
		return activeTopMenu;
	}

	/**
	 * @param activeTopMenu
	 *            The activeTopMenu to set.
	 */
	public void setActiveTopMenu(int activeTopMenu) {
		// every time ActiveTopMenu changes a new left menu has to be generated
		this.menuLeft = null;
		this.activeTopMenu = activeTopMenu;
	}

	/**
	 * @param menuLeft
	 *            The menuLeft to set.
	 */
	public void setMenuLeft(ArrayList<MenuItem> menuLeft) {
		this.menuLeft = menuLeft;
	}

	/**
	 * @return Returns the menuTop.
	 */
	public ArrayList<MenuItem> getMenuTop() {
		if (menuTop == null)
			createTopMenu();
		return menuTop;
	}

	/**
	 * @param menuTop
	 *            The menuTop to set.
	 */
	public void setMenuTop(ArrayList<MenuItem> menuTop) {
		this.menuTop = menuTop;
	}

	/**
	 * @return Returns the groupId.
	 */
	public int getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            The groupId to set.
	 */
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getProposalType() {
		return proposalType;
	}

	public void setProposalType(String proposalType) {
		this.proposalType = proposalType;
	}
}
