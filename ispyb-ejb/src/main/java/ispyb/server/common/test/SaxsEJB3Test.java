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

package ispyb.server.common.test;

import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.io.File;
import java.io.FileReader;
import java.rmi.RMISecurityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Random;

import javax.security.auth.login.LoginContext;

import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.junit.BeforeClass;

public class SaxsEJB3Test {

	protected static Properties properties;
	protected static Ejb3ServiceLocator serviceLocator;
	
	protected static String RANDOM_ACRONYM;
	protected static SimpleDateFormat DATEFORMAT;
	protected static Calendar NOW;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Properties ispybProperties = new Properties();
		ispybProperties.load(new FileReader(new File("build.properties")));
		String jbossPath =  ispybProperties.getProperty("jboss.home") + "/server/default/conf/testJAAS.config";
		String jbossPolicy =  ispybProperties.getProperty("jboss.home") + "/server/default/conf/standaloneClient.policy";
		
		System.out.println("Setting environment up. Fix path is required " + jbossPath);
		
		System.setProperty("java.security.auth.login.config", jbossPath);
        UsernamePasswordHandler handler = null;
        handler = new UsernamePasswordHandler("mx9999", "");
        LoginContext lc = new LoginContext("testEJB", handler);
        lc.login();
        System.setProperty("java.security.policy", jbossPolicy);
        System.setSecurityManager(new RMISecurityManager());
        properties = new Properties();
		properties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        properties.put("java.naming.factory.url.pkgs", "=org.jboss.naming:org.jnp.interfaces");
        properties.put("java.naming.provider.url", "localhost:1099");
        serviceLocator = Ejb3ServiceLocator.getInstance(); 
        
        
        DATEFORMAT = new SimpleDateFormat("hh:mm:ss");
		NOW = GregorianCalendar.getInstance();
		
	}

	protected Macromolecule3VO getRandomMacromolecule(String acronym, String AssemblyAcronym) {
		Macromolecule3VO macromolecule = new Macromolecule3VO();
		RANDOM_ACRONYM = acronym;
		macromolecule.setName(RANDOM_ACRONYM);
		macromolecule.setAcronym(RANDOM_ACRONYM);
		macromolecule.setMolecularMass(String.valueOf(Math.random()));
		macromolecule.setExtintionCoefficient(String.valueOf(Math.random()));
		macromolecule.setCreationDate(GregorianCalendar.getInstance().getTime());
		macromolecule.setProposalId(415);
//		macromolecule.setSafetylevel3VO(safetylevel3VO);
		macromolecule.setSafetylevelId(0);
		if (AssemblyAcronym == null){
			macromolecule.setComments("This is an assembly " + RANDOM_ACRONYM + " for investigating");
		}
		else{
			macromolecule.setComments("This macromolecule " + RANDOM_ACRONYM + " belongs to the assembly " + AssemblyAcronym);
			
		}
		return macromolecule;
	}
	

	protected Macromolecule3VO getRandomMacromolecule(String acronym) {
		return this.getRandomMacromolecule(acronym, null);
	}

	protected Macromolecule3VO getRandomMacromolecule() {
		return this.getRandomMacromolecule(this.getRandomComplexAcronym());
	}
	
	
	protected java.sql.Date getDate(int day, int month, int year) throws ParseException{
		String date = year + "/" + month + "/" + day;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		java.util.Date utilDate = formatter.parse(date);
		return new java.sql.Date(utilDate.getTime());
	}
	
	protected int getRandom(int Min, int Max) throws ParseException{
		return  Min + (int)(Math.random() * ((Max - Min) + 1));
	}
	
	protected String getRandomFlag() throws ParseException{
		ArrayList<String> flags = new ArrayList<String>();
		flags.add("GREEN");
		flags.add("YELLOW");
		flags.add("RED");
		
		return flags.get(this.getRandom(0, 2));
	}
	
	protected String getRandomComplexAcronym(){
//		return this.getRandomComplexAcronym(3);
//		return "ABC";
		return "DEF";
	}
	
	protected String getRandomComplexAcronym(int size){
		int Max = size;
		int Min = size;
		int randomNum =  Min + (int)(Math.random() * ((Max - Min) + 1));
		
		 Random r = new Random();
		 StringBuilder sb = new StringBuilder();
		 String alphabet = "ABCDEFGHIJKLMNNOPQRSTUWXYZ";
		 for (int j = 0; j < randomNum; j++) {
			 sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
		 } 
		 return sb.toString();
	}
	

}
