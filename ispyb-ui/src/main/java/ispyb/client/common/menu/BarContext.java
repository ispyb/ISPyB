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
 * Created on Nov 10, 2004 - 11:10:01 AM
 *
 * Ricardo LEAL
 * ESRF - The European Synchrotron Radiation Facility
 * BP220 - 38043 Grenoble Cedex - France
 * Phone: 00 33 (0)4 38 88 19 59
 * ricardo.leal@esrf.fr
 *
 */
package ispyb.client.common.menu;

import ispyb.common.util.Constants;
import ispyb.server.common.services.config.Menu3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.config.Menu3VO;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * This class holds the context of the context bar.
 * 
 * @author Ricardo Leal
 */
public class BarContext {

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private final static Logger LOG = Logger.getLogger(BarContext.class);

	private Menu3Service menuService;

	private ArrayList<Menu3VO> context;

	private MenuContext menu;

	private HttpServletRequest request;

	private static final String MENU_SELECT_ACTION = "/menuSelected.do";

	public BarContext(HttpServletRequest request) {
		this.menu = (MenuContext) request.getSession().getAttribute(Constants.MENU);
		this.context = new ArrayList<Menu3VO>();
		this.request = request;
	}

	public ArrayList<Menu3VO> getContext() {

		Menu3VO menuValue;

		// if menu is null (e.g. server restarted)
		if (this.menu == null) {
			request.getSession().invalidate();
			return new ArrayList<Menu3VO>();
		}

		// if there is a TopMenu Activated
		int activeTopMenuId;
		if ((activeTopMenuId = menu.getActiveTopMenu()) >= 0) {
			menuValue = getMenuValue(activeTopMenuId);
			MenuContext sessionMenu = (MenuContext) request.getSession().getAttribute(Constants.MENU);
			if (menuValue != null) {
				// because top menu is not a direct link
				String url = request.getContextPath() + MENU_SELECT_ACTION + "?" + Constants.TOP_MENU_ID + "="
						+ sessionMenu.getActiveTopMenu() + "&" + Constants.TARGET_URL + "=" + menuValue.getAction();
				menuValue.setAction(url);
				context.add(menuValue);
			}

			// if there is left menu activated
			int activeLeftMenuId;
			if ((activeLeftMenuId = menu.getActiveLeftMenu()) >= 0) {
				Menu3VO acivatedLeftMenuValue = getMenuValue(activeLeftMenuId);
				if (acivatedLeftMenuValue != null) {
					if (acivatedLeftMenuValue.getParentId() != null
							&& acivatedLeftMenuValue.getParentId().intValue() != activeTopMenuId) {
						menuValue = getMenuValue(acivatedLeftMenuValue.getParentId().intValue());
						if (menuValue != null) {
							if (menuValue.getAction() != null && !menuValue.getAction().equals("")) {
								String url = request.getContextPath() + MENU_SELECT_ACTION + "?"
										+ Constants.LEFT_MENU_ID + "=" + menuValue.getMenuId() + "&"
										+ Constants.TARGET_URL + "=" + menuValue.getAction();
								menuValue.setAction(url);
							}
							context.add(menuValue);
						}
					}
					String url = request.getContextPath() + MENU_SELECT_ACTION + "?" + Constants.LEFT_MENU_ID + "="
							+ acivatedLeftMenuValue.getMenuId() + "&" + Constants.TARGET_URL + "="
							+ acivatedLeftMenuValue.getAction();
					acivatedLeftMenuValue.setAction(url);
					context.add(acivatedLeftMenuValue);
				}
			}

		}

		return context;
	}

	/**
	 * @param menuId
	 * @return
	 */
	private Menu3VO getMenuValue(int menuId) {
		try {
			this.menuService = (Menu3Service) ejb3ServiceLocator.getLocalService(Menu3Service.class);
			Menu3VO menu = menuService.findByPk(new Integer(menuId), false);
			return menu;
		} catch (Exception e) {
			LOG.error(e.toString());
			return null;
		}
	}

	/**
	 * @return Returns the menu.
	 */
	public MenuContext getMenu() {
		return menu;
	}

	/**
	 * @param menu
	 *            The menu to set.
	 */
	public void setMenu(MenuContext menu) {
		this.menu = menu;
	}

	/**
	 * @return Returns the request.
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request
	 *            The request to set.
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @param context
	 *            The context to set.
	 */
	public void setContext(ArrayList<Menu3VO> context) {
		this.context = context;
	}
}
