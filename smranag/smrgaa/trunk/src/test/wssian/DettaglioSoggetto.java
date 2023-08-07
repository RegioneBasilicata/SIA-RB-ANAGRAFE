package wssian;

import it.csi.sian.oprFascicolo.ISWSDettaglioSoggetto;
import it.csi.sian.oprFascicolo.ISWSToOprResponse;
import it.csi.sian.oprFascicolo.InterscambioSOAPBindingStub;
import it.csi.sian.oprFascicolo.OprFascicolo_ServiceLocator;
import it.csi.sian.oprFascicolo.SOAPAutenticazione;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.util.performance.StopWatch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisProperties;


public class DettaglioSoggetto{
	
    // jdbc Connection
    private static Connection conn = null;
    private static PreparedStatement stmt = null;

	 
	public static void main(String[] args){
	    StopWatch stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
		InterscambioSOAPBindingStub binding = null;
			
		OprFascicolo_ServiceLocator locator = new OprFascicolo_ServiceLocator();

		// -- Url Test per Arpea 
		//locator.setOprFascicoloEndpointAddress("http://cooptest.sian.it/wspdd/services/OprFascicolo");
		
		// -- Url Prod Arpea
		//locator.setOprFascicoloEndpointAddress("http://cooperazione.sian.it/wspdd/services/OprFascicolo");
		
		// -- Url Test per TOBECONFIG
		//locator.setOprFascicoloEndpointAddress("https://cooptest.sian.it/wsTOAST/services/OprFascicolo");
		
		// -- Url Prod TOBECONFIG
		locator.setOprFascicoloEndpointAddress("https://cooperazione.sian.it/wsTOAST/services/OprFascicolo");
		
		try {
			binding = (InterscambioSOAPBindingStub) locator.getOprFascicolo();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		binding.setTimeout(250000);

		SOAPAutenticazione sOAPAutenticazione=new SOAPAutenticazione();				

		// -- Autenticazione Test TOBECONFIG
		/*sOAPAutenticazione.setUsername("regiTOBECONFIG");
		sOAPAutenticazione.setPassword("1regiTOBECONFIG1");*/
		
		// -- Autenticazione Prod TOBECONFIG
		sOAPAutenticazione.setUsername("wspddbasi09joke");
		sOAPAutenticazione.setPassword("jekyll23");				
		
		sOAPAutenticazione.setNomeServizio("DettaglioSoggettoFS1.0");
		binding.setHeader("http://cooperazione.sian.it/schema/SoapAutenticazione","SOAPAutenticazione", sOAPAutenticazione);  
		
		org.apache.axis.AxisProperties.setProperty("axis.socketSecureFactory","org.apache.axis.components.net.SunFakeTrustSocketFactory");
		// Set proxy  	  
  	  	AxisProperties.setProperty("http.proxyHost", "proxyto02.aizoon.it");
  	  	AxisProperties.setProperty("http.proxyPort", "8080");
  	    AxisProperties.setProperty("http.proxyUser", "alessandra.sorasio");
  	    AxisProperties.setProperty("http.proxyPassword", "Alesoras^^24");
  	  	AxisProperties.setProperty("http.proxySet","true"); 
		  	  	
  	    ISWSToOprResponse iSWSToOprResponse = null;
  	    long a = 0;
		try {			
			// Input : N Cuaa presi da SIAN_FASCICOLO (SMRGAA db TOBECONFIG)
			
			// ------------- Ottengo la connessione al db
			System.out.println(" -- Ottengo la connessione al db");
			
			
				 createConnection();
				
				 // 100 record
				 //String query ="select cuaa from sian_fascicolo where rownum < 101";
				 // 200 record
				 //String query ="select cuaa from sian_fascicolo where rownum < 201";
				 // 400 record
				 //String query ="select cuaa from sian_fascicolo where rownum < 401";
				 // 800 record
				 //String query ="select cuaa from sian_fascicolo where rownum < 801";				 
				 // 1000 record
				 String query ="select cuaa from sian_fascicolo where rownum < 1001";
				 
				 stmt = conn.prepareStatement(query);
				 System.out.println(" -- eseguo la query per avere i dati di input");
			     ResultSet rs = stmt.executeQuery();				 
				 
			     System.out.println(" -- Effettuo le chiamate a DettaglioSoggetto");
			     a = System.currentTimeMillis();			     
				 stopWatch.start();			
				 while (rs.next()){
				   String cuaa = rs.getString("cuaa");
				   
				   //System.out.println(" - cuaa ="+cuaa);
				   ISWSDettaglioSoggetto dettSoggInput = new ISWSDettaglioSoggetto();
				   dettSoggInput.setCuaa(cuaa);
				   dettSoggInput.setData("20100101");
				   binding.dettaglioSoggettoFS10(dettSoggInput);				   
			     }
				

		}
		catch (Exception e) {			
			e.printStackTrace();
		}
		finally{			
			try{
				System.out.println(" -- chiudo la connessione");
				shutdown();
		        stopWatch.dumpElapsed("DettaglioSoggettoFS1.0", "DettaglioSoggettoFS1.0", "Servizio DettaglioSoggettoFS1.0", "# ritorno: "+ (iSWSToOprResponse != null ? "null" : "non null"));
		        stopWatch.stop();
		        
		        long timeEnd = System.currentTimeMillis() - a;
		        System.err.println(" Time for operation DettaglioSoggettoFS1.0 = "+timeEnd);		        		      
		        
		        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeEnd);
		        System.err.println(" -- minutes ="+minutes);
		        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeEnd);
		        System.err.println(" -- seconds ="+seconds);
		        
		      }
		      catch (Exception ex){
		    	  ex.printStackTrace();
		      }
		}
	}  
	
	 private static void createConnection(){
	   try{
		   Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
	      //Get a connection
		  // conn = DriverManager.getConnection("jdbc:oracle:thin:@tst-domdb55.csi.it:1521:AGRI11T", "smrgaa","mypass");
		   conn = DriverManager.getConnection("jdbc:oracle:thin:@srv1-oraclesirs.hosting.int:1521/orcl.hosting.int", "SMRGAA", "MYPASS");
	    }
	    catch (Exception except){
	      except.printStackTrace();
	    }
	 }
	 
	 private static void shutdown(){
	   try{
	     if(stmt != null){
	       stmt.close();
	      }
	      if(conn != null){	        
	        conn.close();
	      }           
	    }
	    catch (SQLException sqlExcept){
	      sqlExcept.printStackTrace();
	    }
	  }
	    
}
