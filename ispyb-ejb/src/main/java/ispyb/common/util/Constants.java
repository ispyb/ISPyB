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
 ******************************************************************************************************************************/
package ispyb.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Constants class with constant attributes used in all application.
 * 
 * @author DELAGENI
 */
public final class Constants {
	private static Properties mProp = PropertyLoader.loadProperties("ISPyB");

	public enum SITE {
		SOLEIL, EMBL, ESRF, MAXIV, ALBA, GENERIC
	}

	/*
	 * role values
	 */
	public static final String FXMANAGE_ROLE_NAME = "Fedexmanager";

	public static final String ALL_MANAGE_ROLE_NAME = "Manager";

	public static final String ROLE_USER = "User";

	public static final String ROLE_INDUSTRIAL = "Industrial";

	public static final String ROLE_BLOM = "Blom";

	public static final String ROLE_LOCALCONTACT = "Localcontact";

	public static final String ROLE_STORE = "Store";

	public static final String ROLE_MANAGER = "Manager";
	
	public static final String ROLE_WS = "WebService";

	// DLS #####
	public static final String FULLNAME = "fullname";

	public static final String MY_VISITS = "My visits";

	public static final String ROLE_ADMIN = "Admin";

	public static final String DEFAULT_GOOGLE_CALENDAR_PREFIX = "ISPyB_";

	public static final String MAIL_FROM_PROPERTY = getProperty("mail.from");

	public static final String GOOGLE_EXPORTER = "ISPyB Google Exporter";

	public static final String PROXY_HOST_PROPERTY = getProperty("proxy.proxyHost");

	public static final String PROXY_PORT_PROPERTY = getProperty("proxy.proxyPort");

	public static final Integer IMAGE_WALL_DEFAULT_NB_IMAGES_HOR = 0;

	public static final Integer IMAGE_WALL_DEFAULT_NB_IMAGES_VERT = 0;

	public static final String IMAGE_WALL_DEFAULT_IMAGE_WIDTH = "125px";

	/*
	 * Site and database properties
	 */
	public static final String SITE_ATTRIBUTE = "SITE_ATTRIBUTE";

	public static final String SITE_PROPERTY = "ISPyB.site";

	public static final String SITE_ESRF = "ESRF";

	public static final String SITE_DLS = "DLS";

	public static final String SITE_SOLEIL = "SOLEIL";

	public static final String SITE_EMBL = "EMBL";

	public static final String SITE_MAXIV = "MAXIV";

	public static final String SITE_ALBA = "ALBA";

	public static final String SITE_GENERIC = "GENERIC";

	public static final boolean SITE_IS_ESRF() {
		return getProperty(SITE_PROPERTY).equals(SITE_ESRF);
	}

	public static final boolean SITE_IS_DLS() {
		return getProperty(SITE_PROPERTY).equals(SITE_DLS);
	}

	public static final boolean SITE_IS_EMBL() {
		return getProperty(SITE_PROPERTY).equals(SITE_EMBL);
	}

	public static final boolean SITE_IS_SOLEIL() {
		return getProperty(SITE_PROPERTY).equals(SITE_SOLEIL); // && DATABASE_IS_ORACLE(); //TODO
	}

	public static final boolean SITE_IS_GENERIC() {
		return getProperty(SITE_PROPERTY).equals(SITE_GENERIC);
	}

	public static final SITE getSite() {
		if (SITE_IS_SOLEIL()) {
			return SITE.SOLEIL;
		}
		if (SITE_IS_EMBL()) {
			return SITE.EMBL;
		}
		if (SITE_IS_MAXIV()) {
			return SITE.MAXIV;
		}
		if (SITE_IS_ALBA()) {
			return SITE.ALBA;
		}
        if (SITE_IS_ESRF()) {
            return SITE.ESRF;
        }
		return SITE.GENERIC;
	}

	public static final boolean SITE_IS_MAXIV() {
		return getProperty(SITE_PROPERTY).equals(SITE_MAXIV);
	}

	public static final boolean SITE_IS_ALBA() {
		return getProperty(SITE_PROPERTY).equals(SITE_ALBA);
	}

	public static final String DBDIALECT_PROPERTY = "ISPyB.dbDialect";

	public static final String DBDIALECT_MYSQL = "MYSQL";

	public static final String DBDIALECT_ORACLE = "ORACLE";

	public static final boolean DATABASE_IS_MYSQL() {
		return getProperty(DBDIALECT_PROPERTY).equals(DBDIALECT_MYSQL);
	}

	public static final boolean DATABASE_IS_ORACLE() {
		return getProperty(DBDIALECT_PROPERTY).equals(DBDIALECT_ORACLE);
	}

	/**
	 * MySQL or Oracle query for current date
	 */
	public static final String MYSQL_ORACLE_CURRENT_DATE = (DATABASE_IS_MYSQL()) ? "NOW()"
			: (DATABASE_IS_ORACLE()) ? "(select sysdate from dual)" : "";
	
	public static final String MYSQL_ORACLE_YESTERDAY = (DATABASE_IS_MYSQL()) ? "ADDDATE( NOW(), -1)"
			: (DATABASE_IS_ORACLE()) ? "(sysdate -1)" : "";

	public static final String SMIS_WS_URL = getProperty("userportal.webservices.url");
	
	public static final String USER_PORTAL_URL = getProperty("userportal.url");
	
	/*
	 * Configuration properties
	 */

	public static final boolean isAuthorisationActive() {
		String active = getProperty(AUTHORISATION_ACTIVE, "false");
		if (active != null ) 
			return new Boolean(active).booleanValue() ;
		return false;
	}

	public static final String AUTHORISATION_ACTIVE = "ISPyB.authorisation.active";
	
	public static final String ACCESS_DENIED = "Access not authorised.";
	
	public static final String DEPLOY_PROD = "prod" ;

	/*
	 * adminVar keys
	 */
	
	public static final Integer UPDATE_DAILY_NB_DAYS_PK = 5;
	public static final Integer UPDATE_PROPOSAL_NB_DAYS_WINDOW_PK = 6;

	/*
	 * proposal s types
	 */
	public static final String PROPOSAL_MX = "MX";

	public static final String PROPOSAL_MX_BX = "MB";

	public static final int PROPOSAL_BIOSAXS_EXPGROUP = 101;
	
	public static final int PROPOSAL_INDUSTRIAL_EXPGROUP = 106;

	public static final int PROPOSAL_ROLLING_TYPE = 3;
	
	public static final int PROPOSAL_BAG_TYPE = 2;

	public static final String PROPOSAL_BIOSAXS = "BX";
	
	public static final String PROPOSAL_OTHER = "OT";


	/*
	 * ldap attributes values
	 */
	public static final String LDAP_GivenName = "userGivenName";

	public static final String LDAP_LastName = "userLastName";

	public static final String LDAP_siteNumber = "userSiteNumber";

	public static final String LDAP_Email = "userEmail";

	public static final String LDAP_Employee_Resource = getProperty("ldap.employee.resource");

	public static final String LDAP_people = getProperty("ldap.people");
	
	public static final String LDAP_Employee_Identifier = getProperty("ldap.attribute");

	public static final String LDAP_base = SITE_IS_MAXIV() || SITE_IS_ALBA() ? getProperty("ldap.base") : "";
	
	public static final String LDAP_prefix = SITE_IS_MAXIV() || SITE_IS_ALBA() ? getProperty("ldap.principalDNPrefix") : "";
	
	public static final String LDAP_username = SITE_IS_MAXIV() ? getProperty("ldap.username") : "";
	
	public static final String LDAP_credential = SITE_IS_MAXIV() ? getProperty("ldap.credential") : "";

	/*
	 * login prefix
	 */
	public static final String LOGIN_PREFIX_IFX = "if";

	public static final String LOGIN_PREFIX_OPID = "opi";

	public static final String LOGIN_PREFIX_OPD = "opd";

	public static final String LOGIN_PREFIX_MXIHR = "mxi";

	public static final String LOGIN_PREFIX_EHTPX = "eh";

	public static final String LOGIN_PREFIX_BM = "bm";

	public static final String LOGIN_PREFIX_MX = "mx";

	/*
	 * mailing
	 */	
	public static final String MAIL_HOST = mProp.getProperty("mail.smtp.host");
	public static final String MAIL_FROM = mProp.getProperty("mail.from");
	public static final String MAIL_CC = mProp.getProperty("mail.cc");
	public static final String MAIL_TO = (SITE_IS_ESRF()) ? getProperty("mail.cc") : (SITE_IS_DLS()) ? getProperty("mail.cc")
			: (SITE_IS_MAXIV()) ? getProperty("mail.cc") : (SITE_IS_SOLEIL()) ? getProperty("mail.cc")
			: (SITE_IS_EMBL()) ? getProperty("mail.cc"): "";

	public static final String MAIL_TO_SITE = (SITE_IS_ESRF()) ? getProperty("mail.to") : (SITE_IS_DLS()) ? getProperty("mail.to")
			: (SITE_IS_MAXIV()) ? getProperty("mail.to") : (SITE_IS_SOLEIL()) ? getProperty("mail.to") 
			: (SITE_IS_EMBL()) ? getProperty("mail.to") : "";
			
	public static final boolean IS_INDUSTRY_MAILING_IN_PROD() {
				return getProperty("mail.report.industry").equals(DEPLOY_PROD);
			}

	
	/*
	 * phasing
	 */
	public static final Integer PHASING_STEP_PREPARE_PHASING_DATA = 1;

	public static final Integer PHASING_STEP_SUBSTRUCTURE_DETERMINATION = 2;

	public static final Integer PHASING_STEP_PHASING = 3;

	public static final Integer PHASING_STEP_MODEL_BUILDING = 4;

	public static final Integer[] PHASING_STEPS = { PHASING_STEP_PREPARE_PHASING_DATA, PHASING_STEP_SUBSTRUCTURE_DETERMINATION,
			PHASING_STEP_PHASING, PHASING_STEP_MODEL_BUILDING };

	/*
	 * Default date and date time formats.
	 */
	public static final String DATE_FORMAT = "dd-MM-yyyy";

	public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

	public static final String TIME_FORMAT = "HH:mm:ss";

	public static final String SELECTED_SESSION_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

	public static final String DATE_FORMAT_DDMMYYYY = "dd/MM/yyyy";

	/*
	 * Values for proposal
	 */
	public static final String PROPOSAL = "Proposal ";

	public static final String PROPOSAL_CODE_FX = "fx";

	public static final String PROPOSAL_CODE_IN = "in";

	public static final String PROPOSAL_CODE_IX = "ix";

	public static final String PROPOSAL_CODE_MXIHR = "mxi";

	public static final String PROPOSAL_CODE_ALL = "all";

	public static final String PROPOSAL_CODE_BM14 = "bm14u"; // bm14uXXX -> bm14u

	public static final String PROPOSAL_CODE_BM14xxxx = "bm14"; // bm14uXXXX -> bm14

	public static final String PROPOSAL_UOCODE_BM14 = "14-U";

	public static final String PROPOSAL_CODE_BM161 = "bm161";

	public static final String PROPOSAL_UOCODE_BM161 = "16-01";

	public static final String PROPOSAL_CODE_BM162 = "bm162";

	public static final String PROPOSAL_UOCODE_BM162 = "16-02";

	public static final String PROPOSAL_NUMBER_MANAGER = "manager";

	public static final String PROPOSAL_NUMBER_LOCAL_CONTACT = "localContact";

	public static final String PROPOSAL_NUMBER_STORE = "store";

	public static final String PROPOSAL_NUMBER_BLOM = "blom";

	public static final String PROTEIN_ACRONYM_SPACE_GROUP_SEPARATOR = " - ";

	public static final String DEWAR_BARCODE_STYLE = getProperty("dewar.barcode.style");

	// ShippingType values
	// Dewar Location
	public static final String DEWAR_STORES_IN = "STORES-IN";

	public static final String DEWAR_ID14 = "ID14";

	public static final String DEWAR_ID23 = "ID23";

	public static final String DEWAR_ID29 = "ID29";

	public static final String DEWAR_ID30 = "ID30";

	public static final String DEWAR_P12 = "P12";

	public static final String DEWAR_P13 = "P13";

	public static final String DEWAR_P14 = "P14";
	
	public static final String DEWAR_PE2 = "PE2";

	public static final String DEWAR_STORES_OUT = "STORES-OUT";

	public static final String DEWAR_I9113 = "MX";

	public static final String DEWAR_BIOMAX = "BioMAX";

	public static final String[] DEWAR_AUTOMATIC_PROCESSING_STATE = { DEWAR_ID30 };

	/**
	 * Value set by default when a diffractionPlan is created (web services)
	 */
	public final static String DEFAULT_DIFFRACTION_PLAN_EXPERIMENT_KIND = "Default";

	/**
	 * Sample or Container status for webservices
	 */
	public final static String PROCESSING_STATUS = "processing";

	public final static String EXPERIMENT_TYPE_MESH = "Mesh";

	public final static String EXPERIMENT_TYPE_DEHYDRATION = "Dehydration";

	public final static String AUTOPROC_ATTACHMENT_DEFAULT_FILETYPE = "Result";

	public final static String INDEX_FILE_NAME_WORKFLOW_RESULT = "index.html";

	// Dewar tracking status
	public static final String SHIPPING_STATUS_OPENED = "opened";

	public static final String SHIPPING_STATUS_PROCESS = "processing";

	public static final String SHIPPING_STATUS_CLOSED = "closed";

	public static final String SHIPPING_STATUS_SENT_TO_ESRF = "sent to " + getProperty("ISPyB.site");

	public static final String SHIPPING_STATUS_AT_ESRF = "at " + getProperty("ISPyB.site");

	public static final String SHIPPING_STATUS_SENT_TO_USER = "sent to User";

	public static final String SHIPPING_STATUS_READY_TO_GO = "ready to go";

	public static final String DEWAR_STATUS_AT_ESRF = "at " + getProperty("ISPyB.site");

	public static final String DEWAR_STATUS_PROCESS = "processing";

	public static final String DEWAR_STATUS_UNPROCESS = "unprocessing";

	public static final String DEWAR_STATUS_SENT_TO_USER = "sent to User";

	public static final String CONTAINER_STATUS_PROCESS = "processing";

	public static final String SAMPLE_STATUS_PROCESS = "Processing";

	/*
	 * workflows
	 */
	public static final String WORKFLOW_XRAYCENTERING = "XrayCentering";

	public static final String WORKFLOW_MXPRESSO = "MXPressO";
	
	public static final String WORKFLOW_MXPRESSI = "MXPressI";
	
	public static final String WORKFLOW_MXPRESSM = "MXPressM";
	
	public static final String WORKFLOW_MXPRESSA = "MXPressA";

	public static final String WORKFLOW_MXPRESSE = "MXPressE";

	public static final String WORKFLOW_MXPRESSL = "MXPressL";

	public static final String WORKFLOW_MXScore = "MXScore";

	public static final String WORKFLOW_MESHSCAN = "MeshScan";

	/* security filter driver name */
	public static String DB_DRIVER_NAME = "driverName";

	/*
	 * database connection url i.e.: for Mysql (jdbc:mysql://localhost:3308/ispyb_config) or for Oracle
	 * (jdbc:oracle:thin:@localhost:1521:xe)
	 */
	public static String DB_CONNECTION_URL = "databaseDriverUrl";

	/* database connection user name. */
	public static String DB_USER_NAME = "databaseUserName";

	/* database connection user password. */
	public static String DB_USER_PASSWORD = "databaseUserPassword";

	/* security filter query to retrieve user's username. */
	public static String DB_USER_QUERY = "userQuery";

	/* security filter query to retrieve user's role. */
	public static String DB_ROLE_QUERY = "roleQuery";

	/* debug database connection. */
	public static String DB_DEBUG = "debug";
	
	public static final String DB_DRIVER_NAME_VALUE = getProperty("database.driver");
	public static final String DB_CONNECTION_URL_VALUE = getProperty("database.url");
	public static final String DB_USER_NAME_VALUE = getProperty("database.user");
	public static final String DB_USER_PASSWORD_VALUE = getProperty("database.password");

	/* *************************************************************
	 * file paths
	 */
	                                                     
	public static final String DATA_PDB_FILEPATH_START = getProperty("ISPyB.uploaded.root.folder"); // Map file path starting by ...

	public static final String DATA_XDS_INPUT_FILEPATH_START = getProperty("ISPyB.upload.folder.xds");
	
	public static final String REPROCESSING_SCRIPT_PATH = getProperty("ISPyB.reprocessing.script");
	/*
	 * image related constants There is a virtual directory called /data which has the same name as the inicial path of the images and
	 * is protected from direct access It's only needed to concatenate these prefixs to the image directory in DB.
	 */
	public static final String IMG_DNA_URL_SUFIX = "dnafiles/index/";

	public static final String IMG_JPEG_URL_SUFIX = "jpeg_files/";

	public static final String IMG_THUMBS_URL_SUFIX = "thumbs_files/";

	public static final String IMG_VIRTUAL_SITE = "/data";

	public static final String IMG_DNA_PATH_PARAM = "DNApath";

	public static final String EDNA_IMAGE_PATH = "EDNAImagePath";

	public static final String EDNA_FILE_PATH = "EDNAFilePath";

	public static final String IMG_DENZO_URL_PREFIX = "denzo/Results/";

	public static final String IMG_DENZO_URL_SUFFIX = "";

	public static final String IMG_SNAPSHOT_URL_SUFFIX = ".snapshot.jpeg";

	public static final String IMG_SNAPSHOT_URL_PARAM = "file";

	public static final String BEAMLINE_NAME_BM14 = "bm14";

	public static final String DNA_FILES_INDEX_FILE = "index.html";

	public static final String DNA_FILES_DATA_PROC = "Data Processing Module Log";

	public static final String DNA_FILES_DATA_PROC_SUFIX = "dnafiles/index/";

	public static final String DNA_FILES_DATA_PROC_FILE = "dpm_log.html";

	public static final String DNA_FILES_INTEGRATION_SUFIX = "dnafiles/integrate/";

	public static final String DNA_FILES_INTEGRATION = "Integration Results";

	public static final String DNA_FILES_INTEGRATION_FILE = "index.html";

	public static final String DNA_FILES_STRATEGY_SUFIX = "dnafiles/strategy_results/";

	public static final String DNA_FILES_STRATEGY = "Strategy Results";

	public static final String DNA_FILES_STRATEGY_FILE = "index.html";

	public static final String DNA_FILES_RANKING_SUMMARY = "Ranking summary";

	public static final String DNA_FILES_LOG_FILE = "dna_log.txt";

	public static final String DNA_FILES_SCALA_FILE = "scala.log";

	public static final String DNA_FILES_POINTLESS_FILE = "pointless.log";

	public static final String DNA_FILES_MOSFLM_TRICLINT_FILE = "mosflm-triclintegrate.log";

	public static final String DNA_LOG_FILES_SUFIX = "dnafiles/";

	public static final String DNA_PREDICTION_IMAGE_SUFIX = "_pred_";

	public static final String DNA_FILES_BEST_FILE = "best.log";

	public static final String FULL_FILE_PATH = "fullFilePath";

	public static final String PATH_MAPPING_STYLE = getProperty("ISPyB.path.mappingstyle");

	// it should be used only in case of test for windows environment which corresponds to test on local machine
	public static final String DATA_FILEPATH_START = getProperty("ISPyB.root.folder"); // Map file path starting by ...

	public static final String DATA_FILEPATH_WINDOWS_MAPPING = "W:/"; // ... to windows drive

	public static final String DATA_PDB_FILEPATH_WINDOWS_MAPPING = "W://pdb/"; // ... to windows drive

	public static final String getDataFilePathForDevelopmentOnLinuxMapping() {
//		Properties mProp = PropertyLoader.loadProperties("ISPyB_dev_ESRF");
//		return mProp.get("data.path").toString();
		/* TODO: TO be changed when properties are back **/
		return "";
	}

	// EDNA

	public static final String EDNA_FILES_SUFIX = SITE_IS_DLS() ? "summary_html/" : "edna_html/";

	public static final String EDNA_FILES_STRATEGY_SUFIX = "strategy_1/";

	// Here position is number 5 right-to-left : .../<beamline>/<proposal>/<session date>/<protein acronym>/<sample
	// name>/<protein acronym>-<sample name>_<run number>_XXX.jpeg
	public static final int PROPOSAL_POSITION_IN_FILEPATH = Integer.parseInt(getProperty("proposal.positionInPath"));

	// left-to-right or right-to-left?
	public static final String PROPOSAL_POSITION_IN_FILEPATH_DIRECTION = getProperty("proposal.positionInPathDirection");

	public static final String URI_SCHEME = getProperty("ISPyB.uri-scheme");

	public static final boolean DENZO_ENABLED = getProperty("ISPyB.dataprocessing.denzo").equals("true");

	public static final boolean ALLOWED_TO_CREATE_PROTEINS = getProperty("protein.allowedToCreate").equals("true");

	public static final String SITE_AUTHENTICATION_METHOD_ATTRIBUTE = "SITE_AUTHENTICATION_METHOD";

	public static final String SITE_AUTHENTICATION_METHOD = getProperty("ISPyB.authentication.method");

	public static final String SITE_AUTHENTICATION_METHOD_LDAP = "LDAP";

	public static final String SITE_AUTHENTICATION_METHOD_CAS = "CAS";

	public static final boolean SITE_AUTHENTICATION_IS_CAS() {
		return getProperty("ISPyB.authentication.method").equals(SITE_AUTHENTICATION_METHOD_CAS);
	}

	public static final boolean SITE_AUTHENTICATION_IS_LDAP() {
		return getProperty("ISPyB.authentication.method").equals(SITE_AUTHENTICATION_METHOD_LDAP);
	}
	
	public static final String SITE_USERPORTAL_LINK_TYPE = getProperty("userportal.link.type");
	
	public static final String SITE_USERPORTAL_LINK_JSON = "JSON";
			
	public static final String SITE_USERPORTAL_LINK_SMIS = "SMIS";
	
	public static final boolean SITE_USERPORTAL_LINK_IS_SMIS() {
		return getProperty("userportal.link.type").equals(SITE_USERPORTAL_LINK_SMIS);
		
	}
	
	public static final String PATH_TO_UPLOAD_JSON = getProperty("ispyb.upload.folder.json");

	public static final String AUTHORISATION_PROPOSALS_SOURCE = getProperty("ISPyB.authorisation.proposals.source");

	public static final String JS_MINIMIZED = getProperty("js.minimized");

	/*
	 * Values for authentication and Menus Stored in Session
	 */
	public static final String MENU = "MENU"; // keep a MenuContext variable

	public static final String ROLES = "ROLES"; // keep a list of Roles the user has

	public static final String CURRENT_ROLE = "CURRENT_ROLE";

	public static final String SHIPPING_FORM = "SHIPPING_FORM"; // Saved when editing labcontact

	public static final String PROPOSALS = "Proposals";

	/*
	 * URL parameters used on menu
	 */
	public static final String TOP_MENU_ID = "topMenuId";

	public static final String LEFT_MENU_ID = "leftMenuId";

	public static final String TARGET_URL = "targetUrl";

	/*
	 * Default values
	 */
	public static final String DEFAULT_ROLE_NAME = "Guest";

	public static final String DEFAULT_ERROR_PAGE = "/defaultErrorPage.do";

	public static final String DEFAULT_ENCODING = "UTF8"; // to encode URL parameters values

	/*
	 * Parameters/attributes
	 */
	public static final String SUCCESS_VIEW = "viewType"; // TODO delete when change ViewSampleAction

	public static final String DIFFRACTION_PLAN = "difPlan";

	public static final String PROTEIN_LIST = "PROTEIN_LIST";

	public static final String SHIPPING_LIST = "SHIPPING_LIST";

	public static final String SESSION_LIST = "SESSION_LIST";

	public static final String ISPYB_AUTOPROC_ATTACH_LIST = "ispybAutoProcAttchamentList";

	public static final String ISPYB_CRYSTAL_CLASS_LIST = "ispybCrystalClassList";

	public static final String ISPYB_REFERENCE_LIST = "ispybReferenceList";

	public static final String ISPYB_ALLOWED_SPACEGROUPS_LIST = "allowedSpaceGroupsList";

	public static final String PARAMETER_REQ_CODE = "reqCode";

	public static final String SORT_VIEW = "sortView";

	public static final String NB_DATA_SESSION_OBJECTS = "nbOfItems";

	/*
	 * DewarFull constantes moved to here
	 */
	public static final String NOT_AT_STORES = "!STORES%";

	public static final String LOCATION_EMPTY = "EMPTY"; // to encode URL parameters values

	/*
	 * IDs
	 */
	public static final String PERSON_ID = "personId";

	public static final String PERSON_LOGIN = "login";

	public static final String PROPOSAL_ID = "proposalId";

	public static final String PROPOSAL_CODE = "PROPOSAL_CODE";

	public static final String PROPOSAL_NUMBER = "PROPOSAL_NUMBER";

	public static final String PROPOSAL_TYPE = "PROPOSAL_TYPE";

	public static final String TECHNIQUE = "TECHNIQUE";

	public static final String SHIPPING_ID = "shippingId";

	public static final String SHIPPING_ID_FROM_CONTAINER_ID = "shippingIdFromcontainerId";

	public static final String DIFFRACTION_PLAN_ID = "diffractionPlanId";

	public static final String DEWAR_ID = "dewarId";

	public static final String CONTAINER_ID = "containerId";

	public static final String PROTEIN_ID = "proteinId";

	public static final String CRYSTAL_ID = "crystalId";

	public static final String SESSION_ID = "sessionId";

	public static final String WORKFLOW_ID = "workflowId";

	public static final String DATA_COLLECTION_GROUP_ID = "dataCollectionGroupId";

	public static final String DATA_COLLECTION_ID = "dataCollectionId";

	public static final String BLSAMPLE_ID = "blSampleId";

	public static final String IMAGE_ID = "imageId";

	public static final String SCREENING_ID = "screeningId";

	public static final String SCREENING_STRATEGY_ID = "screeningStrategyId";

	public static final String SCREENING_RANK_SET_ID = "screeningRankSetId";

	public static final String LABCONTACT_ID = "labContactId";

	public static final String NEXT_IMAGE_ID = "nextImageId";

	public static final String PREVIOUS_IMAGE_ID = "previousImageId";

	public static final String TARGET_IMAGE_NUMBER = "targetImageNumber";

	public static final String DNA_SELECTED_FILE = "DNASelectedFile";

	public static final String DNA_RANKING_SELECTED_FILE = "DNARankingSelectedFile";

	public static final String REFERENCE_ID = "referenceId";

	/*
	 * Other Values
	 */

	public static final String PROTEIN = "Protein ";

	public static final String PROTEIN_ACRONYM = "proteinAcronym";

	public static final String SHIPPING = "shipping ";

	public static final String SPACE_GROUP = "Space group ";

	public static final String NAME = "name";

	public static final String SAMPLENAME = "sampleName";

	public static final String ID = "id";

	public static final String DEFAULT_DIFFRACTION_PLAN_ID = "1";

	public static final String SAMPLE_NAME = "Sample name ";

	public static final String SAMPLE_IN_SC = "Samples in sample changer";

	public static final String BEAMLINENAME = "beamlineName";

	public static final String EXPERIMENT_DATE_START = "experimentDateStart";

	public static final String EXPERIMENT_DATE_END = "experimentDateEnd";

	public static final String DATACOLLECTION_LIST = "dataCollectionList";

	public static final String DATACOLLECTIONGROUP_LIST = "dataCollectionGroupList";

	public static final String REFERENCES_LIST = "referencesList";

	public static final String SAMPLE_RANKING_LIST = "sampleRankingList";

	public static final String AUTOPROC_RANKING_LIST = "autoProcRankingList";

	public static final String ENERGYSCAN_LIST = "energyScanList";

	public static final String XFE_LIST = "xfeList";

	public static final String SESSION_DATA_OBJECT_LIST = "sessionDataObjectList";

	public static final String DATACOLLECTION_SUCCESSFUL = getProperty("dictionary.dataCollectionSuccessful");

	public static final String DATACOLLECTION_FAILED = getProperty("dictionary.dataCollectionFailed");

	public static final String DATACOLLECTION_RUNNING = getProperty("dictionary.dataCollectionRunning");

	public static final String PARCEL_DEWAR_TYPE = "Dewar";

	public static final String PARCEL_TOOLBOX_TYPE = "Toolbox";

	public static final String SHIPPING_CONTAINER_CAPACITY = getProperty("shipping.container.capacity");
	
	public static final String CONTAINER_TYPE_UNIPUCK = "Unipuck";
	
	public static final String CONTAINER_TYPE_SPINE = "Spinepuck";

	/* delivery agents names */

	public static final String SHIPPING_DELIVERY_AGENT_NAME_FEDEX = "Fedex";
	
	public static final String SHIPPING_DELIVERY_AGENT_FEDEX_ACCOUNT = getProperty("ispyb.shipping.fedex.account");

	public static final String SHIPPING_DELIVERY_AGENT_NAME_TNT = "TNT";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_CIBLEX = "Ciblex";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_DHL = "DHL";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_FASTRANS = "Fastrans";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_TAT = "TAT";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_UPS = "UPS";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_WORLDCOURIER = "World courier";

	public static final String SHIPPING_DELIVERY_AGENT_NAME_CHRONOPOST = "Chronopost";

	public static final String TRACKING_URL_FEDEX = "http://www.fedex.com/Tracking?ascend_header=1&clienttype=dotcomreg&cntry_code=gb&language=english";

	public static final String TRACKING_URL_TNT = "http://www.tnt.com/webtracker/tracking.do?requestType=GEN&searchType=CON&navigation=1&respLang=en&respCountry=GB&genericSiteIdent=";

	public static final String TRACKING_URL_DHL = "http://www.dhl.com/publish/g0/en/eshipping/track.high.html?pageToInclude=RESULTS&type=fasttrack";

	public static final String TRACKING_URL_UPS = "http://wwwapps.ups.com/WebTracking/track";

	public static final String TRACKING_URL_WORLD_COURIER = "http://www.worldcouriercrc.com/CRC/FTRetry.cfm?pickupD=";

	public static final String TRACKING_URL_CHRONOPOST = "http://www.chronopost.fr/transport-express/livraison-colis";

	/*
	 * others
	 */
	public static final String MAX_PAG_ITEMS = "20";

	public static final String MAX_PAG_ITEMS_SMALL = "10";

	public static final String TITLE = "TITLE"; // Title displayed when an action is performed

	public static final boolean CREATE_SHIPMENT_INCLUDE_ONGOING_SESSIONS = getProperty("shipment.create.sessionfindby").equals(
			"enddate");

	public static final String PROPOSAL_LIST_DISPLAY_ATTRIBUTE = "PROPOSAL_LIST_DISPLAY_ATTRIBUTE";

	public static final String PROPOSAL_LIST_DISPLAY = getProperty("proposal.list.display");

	/*
	 * sets of constants
	 */
	public static final String[] BEAMLINE_LOCATION_DLS = { "i02", "i03", "i04", "i04-1", "i24" };

	public static final String[] BEAMLINE_LOCATION_MAXIV = { "BioMAX", "MX" };

	public static final String[] BEAMLINE_LOCATION_SOLEIL = { "SWING", "PROXIMA1", "PROXIMA2" };

	public static final String[] BEAMLINE_LOCATION_ALBA = { "XALOC" };

	public static final String[] BEAMLINE_LOCATION_DEFAULT = { "Not", "yet", "defined", "for", "this", "site" };

	public static String getSAXSBeamline() {
		switch (Constants.getSite()) {
		case EMBL:
			return "P12";
		case ESRF:
			return "BM29";
		case SOLEIL:
			return "SWING";
		case ALBA:
			return "XALOC";
		default:
			return "UNKNOWN";
		}
	}

	// public static final String[] BEAMLINE_LOCATION = (SITE_IS_ESRF()) ? BEAMLINE_LOCATION_ESRF
	// : (SITE_IS_DLS()) ? BEAMLINE_LOCATION_DLS : BEAMLINE_LOCATION_DEFAULT;

	public static final String[] BEAMLINE_LOCATION = BEAMLINE_LOCATION_DEFAULT;

	public static final int LOCATIONS_IN_SC = Integer.parseInt(getProperty("samplechanger.locations", "5"));

	public static final String[] CONTAINER_TYPE_MAXIV = { "Unipuck", "Other" };

	public static final String[] CONTAINER_TYPE_DEFAULT = { "Spinepuck", "Unipuck", "Cane", "Other" };

	public static final String[] CONTAINER_TYPE = (SITE_IS_MAXIV()) ? CONTAINER_TYPE_MAXIV : CONTAINER_TYPE_DEFAULT;

	public static final String[] CONTAINER_CAPACITY_ESRF = { "10", "16", "6", "0" };

	public static final String[] CONTAINER_CAPACITY_DLS = { "16", "6", "0" };

	public static final String[] CONTAINER_CAPACITY_EMBL = { "10", "16", "6", "0" };

	public static final String[] CONTAINER_CAPACITY_MAXIV = { "16", "16", "6", "0" };

	public static final String[] CONTAINER_CAPACITY_SOLEIL = { "10", "16", "6", "0" };

	public static final String[] CONTAINER_CAPACITY_ALBA = { "10", "6", "0" };

	public static final String[] CONTAINER_CAPACITY = (SITE_IS_ESRF()) ? CONTAINER_CAPACITY_ESRF
			: (SITE_IS_DLS()) ? CONTAINER_CAPACITY_DLS : (SITE_IS_MAXIV()) ? CONTAINER_CAPACITY_MAXIV
					: (SITE_IS_SOLEIL()) ? CONTAINER_CAPACITY_SOLEIL
			: (SITE_IS_ALBA()) ? CONTAINER_CAPACITY_ALBA : CONTAINER_CAPACITY_ESRF;

	public static final String[] LIST_EXPERIMENT_KIND_MAXIV = { "Default", "OSC", "SAD", "MAD", "Fixed", "Ligand binding",
			"Refinement", "MAD - Inverse Beam", "SAD - Inverse Beam" };

//	public static final String[] LIST_EXPERIMENT_KIND_ESRF = { "Default", "MXPressE", "MXPressO", "MXPressE_SAD", "MXScore", "MXPressM","OSC", "SAD",
//		"MAD", "Fixed", "Ligand binding", "Refinement", "MAD - Inverse Beam", "SAD - Inverse Beam" };

	public static final String[] LIST_EXPERIMENT_KIND_ESRF = { "Default", "MXPressE", "MXPressO", "MXPressF", "MXPressI", "MXPressE_SAD", "MXScore", "MXPressM", "MXPressP", "MXPressP_SAD" };

	public static final String[] LIST_EXPERIMENT_KIND_ALBA = { "Default", "MXPressE", "MXPressO", "MXPressI", "MXPressE_SAD", "MXScore", "MXPressM", "MXPressA" };

	public static final String[] LIST_EXPERIMENT_KIND = (SITE_IS_ESRF()) ? LIST_EXPERIMENT_KIND_ESRF : LIST_EXPERIMENT_KIND_MAXIV;

	public static final String[] DATA_COLLECTION_TYPE = { "Peak", "Inflection", "High Energy Remote", "Low Energy Remote" };

	public static final String[] DATA_COLLECTION_KIND = { "Oscillation", "Inverse" };

	// public static final String[] CONTAINER_STATUS = {"opened", "not open or closed", "closed" };
	public static final String[] LOOP_TYPE = { "Nylon", "Litho Loop", "Micromount", "Other" };

	/*
	 * 
	 * constants used to identify pages from where actions are called
	 */
	public static final String PAGE_SAMPLE_CREATE_MENU = "/user/createSampleAction.do?reqCode=displayFromMenu";

	public static final String PAGE_SAMPLE_CREATE = "/user/createSampleAction.do?reqCode=display";

	public static final String PAGE_SAMPLE_CREATE_CONTAINER = "/user/createSampleAction.do?reqCode=display&containerId=";

	public static final String PAGE_SAMPLE_EDIT = "/user/editSampleAction.do?reqCode=editSample";

	public static final String PAGE_SAMPLE_EDIT_CONTAINER = "/user/editSampleAction.do?reqCode=editSample&containerId=";

	public static final String PAGE_SAMPLE_VIEW = "/user/viewSample.do?reqCode=";

	public static final String PAGE_SAMPLE_FREE = "fromFreeSample";

	public static final String PAGE_SHIPMENT = "fromShipment";

	public static final String PAGE_PREPARE = "fromPrepare";

	public static final String PAGE_MENU = "fromMenu";

	public static final String PAGE_SAMPLE = "fromSample";

	/*
	 * edit masks
	 */
	public static final String MASK_BASIC_CHARACTERS_WITH_DASH_UNDERSCORE_NO_SPACE = "([-a-zA-Z0-9_]*)";

	public static final String MASK_SHIPMENT_LEGAL_CHARACTERS = getProperty("shipment.upload.legalcharactersmask");

	public static final String MASK_BASIC_CHARACTERS_WITH_DASH_NO_SPACE = "([-a-zA-Z0-9]*)";

	public static final String MASK_BASIC_CHARACTERS_NO_SPACE = "([a-zA-Z0-9]*)";

	/*
	 * Upload
	 */
	
	public static final String UPLOAD_PATH= mProp.getProperty("upload.path");
	
	public static final String TEMPLATE_RELATIVE_DIRECTORY_PATH = "/tmp/";

	public static final String TEMPLATE_XLS_FILENAME = getProperty("shipment.upload.template");

	public static final String TEMPLATE_XLS_POPULATED_FILENAME = getProperty("shipment.upload.template");

	public static final String TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME1 = "ispyb-template5.1.1_1.xls";

	public static final String TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME2 = "ispyb-template5.1.1_2.xls";

	public static final String TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME3 = "ispyb-template5.1.1_3.xls";

	public static final String TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME4 = "ispyb-template5.1.1_4.xls";

	public static final String TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME5 = "ispyb-template5.1.1_5.xls";

	public static final String TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME6 = "ispyb-template5.1.1_6.xls";

	public static final String TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME7 = "ispyb-template5.1.1_7.xls";

	public static final String TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME8 = "ispyb-template5.1.1_8.xls";

	public static final String TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME9 = "ispyb-template5.1.1_9.xls";

	public static final String TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME10 = "ispyb-template5.1.1_10.xls";

	public static final String TEMPLATE_XLS_POPULATED_FROM_SHIPMENT = getProperty("shipment.upload.populatedTemplate");

	public static final String TEMPLATE_RELATIVE_PATH = TEMPLATE_RELATIVE_DIRECTORY_PATH + TEMPLATE_XLS_FILENAME;

	public static final String TEMPLATE_POPULATED_RELATIVE_PATH = TEMPLATE_RELATIVE_DIRECTORY_PATH + TEMPLATE_XLS_POPULATED_FILENAME;

	public static final String TEMPLATE_RTF_SCREENING_RTF_FILENAME = getProperty("screening.export.template");

	public static final String TEMPLATE_RTF_SCREENING_RELATIVE_PARH = TEMPLATE_RELATIVE_DIRECTORY_PATH
			+ TEMPLATE_RTF_SCREENING_RTF_FILENAME;

	public static final String TEMPLATE_RTF_DATA_COLLECTION_RTF_FILENAME = getProperty("datacollection.export.template");

	public static final String TEMPLATE_RTF_DATA_COLLECTION_RELATIVE_PARH = TEMPLATE_RELATIVE_DIRECTORY_PATH
			+ TEMPLATE_RTF_DATA_COLLECTION_RTF_FILENAME;

	public static final int NB_IMAGES_SCREENING = 2;

	public static final int NB_IMAGES_DATA_COLLECTION = 3;

	public static final String TEMPLATE_VERSION_N_1 = "ehtpx4";

	public static final String TEMPLATE_VERSION = "ehtpx5";

	public static final String TEMPLATE_FILE_TYPE = "fileType";

	public static final String TEMPLATE_FILE_TYPE_TEMPLATE = "template";

	public static final String TEMPLATE_FILE_TYPE_POPULATED_TEMPLATE = "populatedTemplate";

	public static final String TEMPLATE_FILE_TYPE_POPULATED_TEMPLATE_ADVANCED = "populatedTemplateAdvanced";

	public static final String TEMPLATE_FILE_TYPE_POPULATED_TEMPLATE_FROM_SHIPMENT = "populatedTemplateFromShipment";

	public static final String TEMPLATE_FILE_TYPE_EXPORT_SHIPPING = "exportShipping";
	
	public static final String BZIP2_PATH_W = mProp.getProperty("bzip2.path.windows");
	
	public static final String BZIP2_PATH_OUT = mProp.getProperty("bzip2.outputFilePath");

	public static final String BZIP2_PATH = mProp.getProperty("bzip2.path");

	public static final String BZIP2_ARGS = mProp.getProperty("bzip2.arguments");

	/*
	 * SampleOnBeamline Link Response
	 */
	public static final String CRYSTAL_UIID = "crystalUUID";

	// public static final String BLSAMPLE_ID = BLSAMPLE_ID;
	public static final String BLSAMPLE_NAME = "name";

	public static final Integer BLSAMPLE_NAME_NB_CHAR = 50;

	/*
	 * Parcel labels
	 */
	
	public static final String PATH_TO_PDF_TEMPLATES = getProperty("ispyb.upload.folder.pdf");
	
	/**
	 * PDF template used for parcel labels
	 */
	public static final String TEMPLATE_PDF_PARCEL_LABELS_FILENAME = getProperty("dewar.label.template");
	
	public static final String TEMPLATE_PDF_PARCEL_LABELS_PYARCH_PATH = PATH_TO_PDF_TEMPLATES
			+ TEMPLATE_PDF_PARCEL_LABELS_FILENAME;

	public static final String TEMPLATE_PDF_PARCEL_LABELS_RELATIVE_PATH = TEMPLATE_RELATIVE_DIRECTORY_PATH
			+ TEMPLATE_PDF_PARCEL_LABELS_FILENAME;

	/**
	 * PDF template used for parcel labels, with the World Courier checklist
	 */
	
	public static final String TEMPLATE_PDF_PARCEL_LABELS_WORLDCOURIER_FILENAME = getProperty("dewar.label.template.worldCourier");

	public static final String TEMPLATE_PDF_PARCEL_LABELS_WORLDCOURIER_RELATIVE_PATH = TEMPLATE_RELATIVE_DIRECTORY_PATH
			+ TEMPLATE_PDF_PARCEL_LABELS_WORLDCOURIER_FILENAME;
	
	public static final String TEMPLATE_PDF_PARCEL_LABELS_WORLDCOURIER_PYARCH_PATH = PATH_TO_PDF_TEMPLATES
			+ TEMPLATE_PDF_PARCEL_LABELS_WORLDCOURIER_FILENAME;


	/*
	 * Default Images
	 */
	public static final String IMAGE_NO_XTAL_THUMBNAILS_RELATIVE_PATH = "/images/noXtalThumbnail.jpg";

	public static final String IMAGE_NO_DIFFRACTION_THUMBNAILS_RELATIVE_PATH = "/images/noDiffractionThumbnail.jpg";

	public static final String IMAGE_NO_DNA_PREDICTION_RELATIVE_PATH = "/images/noDNAPredictionThumbnail.jpg";

	public static final String IMAGE_SUCCESS = "/images/Sphere_Green_12.png";

	public static final String IMAGE_FAILED = "/images/Sphere_Red_12.png";

	public static final String IMAGE_LAUNCHED = "/images/Sphere_Orange_12.png";

	public static final String IMAGE_BLANK = "/images/Sphere_White_12.png";

	/*
	 * Numbers
	 */
	public static final int SC_BASKET_CAPACITY = 5;

	public static final int BASKET_SAMPLE_CAPACITY = Integer.valueOf(getProperty("samplechanger.container.capacity"));
	
	public static final int SPINE_SAMPLE_CAPACITY = 10;
	
	public static final int UNIPUCK_SAMPLE_CAPACITY = 16;

	public static final Double SILLY_NUMBER = new Double(-9999);// used to display N/A instead of no value from MXCube

	/*
	 * Admin values
	 */
	public static final String MESSAGE_WARNING = "warningMessage"; // Var name in table AdminVar

	public static final String MESSAGE_INFO = "infoMessage"; // Var name in table AdminVar

	public static final String STATUS_LOGON = "LOGON"; // Table AdminActivity / Column action

	public static final String STATUS_LOGOFF = "LOGOFF"; // Table AdminActivity / Column action

	public static final long USER_ONLINE_MIN = 10; // Minutes (to be considered as an online user)

	public static final String CHART_VIEW = "chartView"; // View of chart to display

	public static final String CHART_TYPE = "chartType"; // bar, area, line,...

	public static final String CHART_TITLE = "chartTitle"; // Chart title

	public static final String CHART_UNIT = "chartUnit"; // Chart unit

	public static final String CHART_TEMP_DIR = "tmp_reports";

	/*
	 * WEB pages
	 */
	public static final String SESSION_VISIT = getProperty("dictionary.session");

	public static final String SESSIONS_VISITS = getProperty("dictionary.sessions");

	public static final String SESSION_VISIT_CAP = getProperty("dictionary.Session");

	public static final String SELECTED_SESSION_VISIT = "Selected " + SESSION_VISIT_CAP;

	public static final String BACK_TO_THIS_SESSION_VISIT = "Back to this " + SESSION_VISIT;

	public static final String BACK_TO_SESSION_VISIST = "Back to " + SESSIONS_VISITS;

	public static final String SEARCH_SESSIONS_VISITS = "Search " + SESSIONS_VISITS;

	public static final String VIEW_ALL_SESSIONS_VISITS = "View all " + SESSIONS_VISITS;

	public static final String SESSION_VISIT_COMMENTS = SESSION_VISIT + " Comments";

	public static final String ESRF_DLS = getProperty("dictionary.site");

	public static final String SITE_NAME = getProperty("ISPyB.site");

	public static final String DB_NAME = getProperty("ISPyB.dbDialect");

	public static final String BCM_NAME = getProperty("dictionary.BCM"); // displayed name

	public static final String BCM = getProperty("ISPyB.BCM"); // internal name

	public static final String BCM_ATTRIBUTE = "BCM"; // context attribute used by JSP

	/*
	 * Max record retrieved
	 */

	public static final String MAX_RETRIEVED_DATACOLLECTIONS = "100";

	/**
	 * DataCollection Sorting
	 */
	public static final String SORT_NoOrder = "sortNoOrder";

	public static final String SORT_OnImagePrefixAsc = "sortOnImagePrefixAsc";

	public static final String SORT_OnImagePrefixDesc = "sortOnImagePrefixDesc";

	public static final String SORT_OnStartTimeAsc = "sortOnStartTimeAsc";

	public static final String SORT_OnStartTimeDesc = "sortOnStartTimeDesc";

	/**
	 * Sample ranking
	 */
	public static final String DATACOLLECTIONID_SET = "dataCollectionIdSet";

	public static final String SAMPLE_RANKING_VALUE_LIST = "sampleRankingValueList";

	public static int TOP_DATACOLLECTIONS = 5;

	public static final String SAMPLE_RANKING_CRITERIA = "sampleRankingCriteria";

	public static final Integer SAMPLE_RANKING_DEFAULT_WEIGHT_RANKING_RESOLUTION = 1;

	public static final Integer SAMPLE_RANKING_DEFAULT_WEIGHT_EXPOSURE_TIME = 1;

	public static final Integer SAMPLE_RANKING_DEFAULT_WEIGHT_MOSAICITY = 1;

	public static final Integer SAMPLE_RANKING_DEFAULT_WEIGHT_NUMBER_OF_SPOTS = 1;

	public static final Integer SAMPLE_RANKING_DEFAULT_WEIGHT_NUMBER_OF_IMAGES = 1;

	public static final Integer SAMPLE_RANKING_DEFAULT_WEIGHT_NUMBER_OF_WEDGES = 1;

	public static final String AUTOPROC_RANKING_CRITERIA = "autoProcRankingCriteria";

	public static final Integer AUTOPROC_RANKING_DEFAULT_WEIGHT_OVERALL_RFACTOR = 1;

	public static final Integer AUTOPROC_RANKING_DEFAULT_WEIGHT_HIGHEST_RESOLUTION = 1;

	public static final Integer AUTOPROC_RANKING_DEFAULT_WEIGHT_COMPLETENESS = 1;

	public static final String AUTOPROC_RANKING_VALUE_LIST = "autoProcRankingValueList";

	/**
	 * path to the folder where the update scripts are stored
	 */
	public final static String PATH_TO_SCRIPTS = getProperty("ispyb.db.updatescripts.folder");


	/**
	 * returns true if the specified server is the server in the production environment
	 * 
	 * @param serverName
	 * @return
	 */
	public static boolean isServerProd(String serverName) {
		return serverName.equals(getProperty("ISPyB.server.name.prod"))
				|| serverName.equals(getProperty("ISPyB.server.name.prod.alias"))
				|| serverName.equals(getProperty("ISPyB.server.name.prod.ext"));
	}

	public final static char ANGSTROM = '\u00C5'; // ALT 0197

	public final static char DEGREE = '\u00BA'; // ALT 167

	public final static char ALPHA = '\u03B1';

	public final static char BETA = '\u03B2';

	public final static char GAMMA = '\u03B3';

	public final static char MICRO = '\u00B5'; // ALT 230

	public static String COPY_PUCK_SAMPLES = "COPY_PUCK_SAMPLES";

	public static String COPY_PUCK_CODE = "COPY_PUCK_CODE";

	public static String COPY_PUCK_NB = "COPY_PUCK_NB";

	public static String ERROR_LIST = "ERROR_LIST";

	/* proposal code which are allowed to create protein direclty in ispyb */
	public static final String[] PROPOSAL_CAN_CREATE_PROTEIN = { "opid", "mxihr", "opd" };

	public static final String RSYMM = "rmerge";

	public static final String ISIGMA = "isigma";

	public static final String AUTOPROC_FASTPROC = "%fastproc";

	public static final String AUTOPROC_PARALLELPROC = "%parallelproc";

	public static final String AUTOPROC_EDNAPROC = "EDNA%proc";

	public static final String AUTOPROC_AUTOPROC = "autoPROC";

	public static final String AUTOPROC_XIA2_DIALS = "XIA2_DIALS";

	public static final String AUTOPROC_FASTDP = "fastdp";

	public static final String EXPERIMENT_TYPE_CHARACTERIZATION = "Characterization";

	public final static String REFERENCE_ALL_BEAMLINE = "All";

	public final static String REFERENCE_ALL_XRF = "AllXRF";

	public final static String REFERENCE_XRF = "XRF";

	public final static String REFERENCE_MESH = "Mesh";

	public static final String DEWAR_BARCODE_PREFIX = getProperty("dewar.barcode.prefix");

	public static final String DEWAR_BARCODE_SUFFIX = getProperty("dewar.barcode.suffix");

	public static final String DEWAR_TRACKING_SHIPPING_TYPE = "DewarTracking";

	public static final String STANDARD_UPLOAD_SHIPPING_TYPE = "StandardUpload";

	public static final String CONTAINER_BATCH_NAME = "Batch Basket";

	public static final String DEWAR_BATCH_NAME = "Batch Dewar";

	public static final int NB_MAX_EXPERIMENT_KIND_DETAILS = 3;

	public static final double WAVELENGTH_TO_ENERGY_CONSTANT = 12.3984;

	public static final Integer NB_SESSIONS_TO_DISPLAY = 20;

	public static final String SESSIONS = getProperty("dictionary.sessions");

	public static final String ALLSESSIONS = getProperty("dictionary.allSessions");

	public static final String DEFAULT_SCIENTIST_NAME = "SMIS";

	public static final String DEFAULT_SCIENTIST_FIRSTNAME = "WebService";

	public static final String DEFAULT_SCIENTIST_LABO = "SMIS";
	
	public static final String DEFAULT_TEST_PROPOSAL_PK = Constants.getProperty("userportal.test.proposal.pk");

	public static String getDummyFamilyName() {
		return (SITE_IS_MAXIV()) ? getProperty("ISPyB.dummy.family.name.lab.contact") : "";
	}

	public static String getDummyGivenName() {
		return (SITE_IS_MAXIV()) ? getProperty("ISPyB.dummy.given.name.lab.contact") : "";
	}

	/*
	 * Functions
	 */
	
	public final static String getUserSmisLoginName() {
		return PropertyLoader.loadProperties("ISPyB").getProperty("smis.ws.username");
	}
	
	public final static String getUserSmisPassword() {
		return PropertyLoader.loadProperties("ISPyB").getProperty("smis.ws.password");
	}
			
	public static String getProperty(String propertyName) {
		return mProp.getProperty(propertyName);
	}

	public static String getProperty(String propertyName, String defaultValue) {
		String val = getProperty(propertyName);
		return (val == null) ? defaultValue : val;
	}

	
	public static Properties getProperties() {		
		return mProp;
	}
	

	public static Properties getPropertiesNew() {	
		
	String resourceName = "ISPyB.properties"; // could also be a constant
	ClassLoader loader = Thread.currentThread().getContextClassLoader();
	Properties props = new Properties();
	try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
	    props.load(resourceStream);
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	return props;
	}

	
}
