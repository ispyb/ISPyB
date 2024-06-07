package bcr.common.util;

import java.util.Properties;

/**
 * Constants class with constant attributes used in all application.
 * 
 */
public final class Constants {
	private static Properties mProp = PropertyLoader.loadProperties("BCRprop");

	/*
	 * Values for authentication and Menus Stored in Session
	 */
	public static final String MENU = "MENU"; // keep a MenuContext variable

	public static final String ROLES = "ROLES"; // keep a list of Roles the user has

	public static final String CURRENT_ROLE = "CURRENT_ROLE";

	/*
	 * Default date and date time formats.
	 */
	public static final String DATE_FORMAT = "dd-MM-yyyy";

	public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

	public static final String DATE_FORMAT_DDMMYYYY = "dd/MM/yyyy";

	/*
	 * Default values
	 */
	public static final String DEFAULT_ROLE_NAME = "Guest";

	/*
	 * role values
	 */
	public static final String FXMANAGE_ROLE_NAME = "Fedexmanager";

	public static final String ALL_MANAGE_ROLE_NAME = "Manager";

	public static final String LOCALCONTACT_ROLE_NAME = "Localcontact";

	/*
	 * IDs
	 */
	public static final String PERSON_ID = "personId";

	public static final String PERSON_LOGIN = "login";

	/*
	 * Status values
	 */
	public static final String SHIPPING_STATUS_SENT_TO_ESRF = "sent to ESRF";

	public static final String SHIPPING_STATUS_AT_ESRF = "at ESRF";

	public static final String SHIPPING_STATUS_SENT_TO_USER = "sent to User";

	/*
	 * Courier values
	 */
	public static final String SHIPPING_DELIVERY_AGENT_NAME_FEDEX = "Fedex";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_TNT = "TNT";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_CIBLEX = "Ciblex";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_DHL = "DHL";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_FASTRANS = "Fastrans";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_TAT = "TAT";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_UPS = "UPS";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER = "World courier";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST = "Chronopost";

	/*
	 * Tracking url values
	 */
	public static final String TRACKING_URL_FEDEX = "http://www.fedex.com/Tracking?ascend_header=1&clienttype=dotcomreg&cntry_code=gb&language=english";

	public static final String TRACKING_URL_TNT = "http://www.tnt.com/webtracker/tracking.do?requestType=GEN&searchType=CON&navigation=1&respLang=en&respCountry=GB&genericSiteIdent=";

	public static final String TRACKING_URL_DHL = "http://www.dhl.com/publish/g0/en/eshipping/track.high.html?pageToInclude=RESULTS&type=fasttrack";

	public static final String TRACKING_URL_UPS = "http://wwwapps.ups.com/WebTracking/track";

	public static final String TRACKING_URL_WORLD_COURIER = "http://www.worldcouriercrc.com/CRC/FTRetry.cfm?pickupD=";

	public static final String TRACKING_URL_CHRONOPOST = "http://www.chronopost.fr/transport-express/livraison-colis";

	public static final String TRACKING_URL_HELP = "Track the Shipping";

	/*
	 * Courier list table
	 */
	public static final int COURIER_LIST_COURIER_NAME = 0; // Courier name used by ISPyB

	public static final int COURIER_LIST_COURIER_BARCODE = 1; // Courier name barcode must be uppercased (code

	public static final int COURIER_LIST_COURIER_URL = 2; // Courier url

	public static final int COURIER_LIST_COURIER_URL_PARAM = 3; // Tracking number parameter in courier url

	public static final String[][] COURIER_LIST = {
			{ SHIPPING_DELIVERY_AGENT_NAME_FEDEX, "FEDEX", TRACKING_URL_FEDEX, "&tracknumbers" },
			{ SHIPPING_DELIVERY_AGENT_NAME_TNT, "TNT", TRACKING_URL_TNT, "&cons" },
			{ SHIPPING_DELIVERY_AGENT_NAME_CIBLEX, "CIBLEX", "", "" },
			{ SHIPPING_DELIVERY_AGENT_NAME_DHL, "DHL", TRACKING_URL_DHL, "&AWB" },
			{ SHIPPING_DELIVERY_AGENT_NAME_FASTRANS, "FASTRANS", "", "" }, { SHIPPING_DELIVERY_AGENT_NAME_TAT, "TAT", "", "" },
			{ SHIPPING_DELIVERY_AGENT_NAME_UPS, "UPS", TRACKING_URL_UPS, "?trackNums" },
			{ SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST, "CHRONOPOST", TRACKING_URL_CHRONOPOST, "" },
			{ SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER, "WORLD-COURIER", TRACKING_URL_WORLD_COURIER, "&hwb" } };

	/*
	 * Ispyb values
	 */
	public static final String ISPYB_URL = "https://py-ispyb.esrf.fr/";
	public static final String EXI_URL = "http://exi.esrf.fr/";

	public static final String ISPYB_URL_HELP = "ISPYB from outside the firewall";

	public static final String ARRIVAL_LOCATION = "STORES-IN";

	public static final String RETURN_LOCATION = "STORES-OUT";

	public static final String PROPOSAL_CODE_FX = "FX";
	public static final String PROPOSAL_CODE_IX = "IX";
	public static final String PROPOSAL_CODE_OA = "OA";

	/*
	 * Misc
	 */

	public static final String DEBUG_BCC_EMAIL = "solange.delageniere@esrf.fr";

	/*
	 * Functions
	 */
	public static String getProperty(String propertyName) {
		return mProp.getProperty(propertyName);
	}

}