package smrcomm;

import static org.junit.Assert.*;
import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.csi.wrapper.CSIException;
import it.csi.solmr.dto.comune.IntermediarioVO;
import it.csi.solmr.dto.profile.CodeDescription;
//import it.csi.smrcomms.smrcomm.interfacecsi.IAgriWellCSIInterface;
import it.csi.solmr.interfaceCSI.comune.services.ComuneServiceCSIInterface;

import java.io.FileInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestJUnitSmrcommComuneService {
	
	private ComuneServiceCSIInterface pd;

	@Before
	public void setUp() throws Exception {		
		try {
			InfoPortaDelegata infoPD = PDConfigReader.read(new FileInputStream("C:\\workspace\\smrgaa\\src\\test\\smrcomm\\pdComuneService.xml"));			
			pd = (ComuneServiceCSIInterface)(PDProxy.newInstance(infoPD));						
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
			System.out.println("testResuources() comuneService OK");
		} 
		catch (CSIException e) {			
			System.err.println("testResuources() comuneService KO");
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
	public void testserviceGetTipiAmmComp() {
		try {			
			CodeDescription[] elencoCodeDescr = pd.serviceGetTipiAmmComp();
			System.out.println("serviceGetTipiAmmComp() comuneService OK");
		} 
		catch (CSIException e) {			
			System.err.println("serviceGetTipiAmmComp() comuneService KO ="+e.getMessage());
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
	public void testserviceGetTipiIntermediario() {
		try {			
			CodeDescription[] codeDescr = pd.serviceGetTipiIntermediario();
			System.out.println("serviceGetTipiIntermediario() comuneService OK");
		} 
		catch (CSIException e) {			
			System.err.println("serviceGetTipiIntermediario() comuneService KO ="+e.getMessage());
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
	public void testserviceFindIntermediarioByIdIntermediarioRange() {
		try {			
			long[] arrLong = new long[1];
			arrLong[0] = 128;
			IntermediarioVO[] intermediarioList = pd.serviceFindIntermediarioByIdIntermediarioRange(arrLong);
			System.out.println("serviceFindIntermediarioByIdIntermediarioRange() comuneService OK");
		} 
		catch (CSIException e) {			
			System.err.println("serviceFindIntermediarioByIdIntermediarioRange() comuneService KO ="+e.getMessage());
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
	public void testserviceGetAmmCompetenzaByLivelloCD() {
		try {			
			CodeDescription[] codeDescr = pd.serviceGetAmmCompetenzaByLivelloCD(new Long(1));
			System.out.println("serviceGetAmmCompetenzaByLivelloCD() comuneService OK");
		} 
		catch (CSIException e) {			
			System.err.println("serviceGetAmmCompetenzaByLivelloCD() comuneService KO ="+e.getMessage());
			fail();
		} 
		catch (Exception e) {			
			fail();
		} 
		finally {			
		}
	}
	
	@Test
	public void testserviceFindAmmCompetenzaById() {
		try {			
			pd.serviceFindAmmCompetenzaById(new Long(249));
			System.out.println("serviceFindAmmCompetenzaById() comuneService OK");
		} 
		catch (CSIException e) {			
			System.err.println("serviceFindAmmCompetenzaById() comuneService KO");
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
	public void testserviceFindAmmCompetenzaByIdRange() {
		try {			
			String[] arrayString = new String[1];
			arrayString[0] = new String("249");
			
			pd.serviceFindAmmCompetenzaByIdRange(arrayString);
			System.out.println("serviceFindAmmCompetenzaByIdRange() comuneService OK");
		} 
		catch (CSIException e) {			
			System.err.println("serviceFindAmmCompetenzaByIdRange() comuneService KO ="+e.getMessage());
			fail();
		} 
		catch (Exception e) {	
			System.err.println("serviceFindAmmCompetenzaByIdRange() comuneService KO ="+e.getMessage());
			fail();
		} 
		finally {			
		}
	}

}
