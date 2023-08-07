package smrcomm;

import static org.junit.Assert.*;
import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.csi.wrapper.CSIException;
import it.csi.solmr.interfaceCSI.comune.services.ComuneServiceCSIInterface;
import it.csi.solmr.interfaceCSI.profile.services.ProfCSIInterface;

import java.io.FileInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.UtilConstants;

public class TestJUnitSmrcommProfileService {
	
	private ProfCSIInterface pd;

	@Before
	public void setUp() throws Exception {
		try {
			InfoPortaDelegata infoPD = PDConfigReader.read(new FileInputStream(UtilConstants.START_PATH_TEST_FILES + "smrcomm\\pdProfileService.xml"));			
			pd = (ProfCSIInterface)(PDProxy.newInstance(infoPD));						
		} 
		catch(Exception ex) {
		  System.err.println(ex.getMessage());
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTestResources() {
		try {			
			pd.testResources();
			System.out.println("testResuources() profileService OK");
		} 
		catch (CSIException e) {			
			System.err.println("testResuources()  profileService KO");
			fail();
		} 
		catch (Exception e) {			
			fail();
		} 
		finally {			
		}
	}
	
	
	// ********** Problemi con il servizio perchè restituisce in output un Vector	
	@Test
	public void testserviceGetUtenteProcedimento() {
		try {			
			Long[] idUtenti = new Long[1];
			idUtenti[0] = new Long(1);
			
			String codiceFiscale ="";
			Long idProcedimento = new Long(7);
			String ruolo = "";
			String codiceEnte = "";
			String dirittoAccesso  ="";
			Long idLivello = null;
			pd.serviceGetUtenteProcedimento(codiceFiscale,idProcedimento,ruolo,codiceEnte,dirittoAccesso,idLivello);
			System.out.println("testserviceGetUtenteProcedimento() profileService OK");
		} 
		catch (CSIException e) {			
			System.err.println("testserviceGetUtenteProcedimento()  profileService KO");
			fail();
		} 
		catch (Exception e) {			
			fail();
		} 
		finally {			
		}
	}
	
	
	// Attenzione : ci sono problemi con il servizio perchè riceve in input Long[]
	@Test
	public void testServiceGetRuoloUtenzaByIdRange() {
		try {			
			long[] idUtenti = new long[1];
			idUtenti[0] = 1;
			pd.serviceGetRuoloUtenzaByIdRange(idUtenti, false);
			System.out.println("testServiceGetRuoloUtenzaByIdRange() profileService OK");
		} 
		catch (CSIException e) {			
			System.err.println("testServiceGetRuoloUtenzaByIdRange()  profileService KO");
			fail();
		} 
		catch (Exception e) {			
			fail();
		} 
		finally {			
		}
	}

}
