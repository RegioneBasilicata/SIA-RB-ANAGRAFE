/**
 * 
 */
package smrcomm;


import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.csi.wrapper.CSIException;
import it.csi.csi.wrapper.UnrecoverableException;
/*import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellEsitoDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.AgriWellEsitoFolderVO;
import it.csi.smrcomms.smrcomm.exception.SmrcommInternalException;
import it.csi.smrcomms.smrcomm.interfacecsi.IAgriWellCSIInterface;*/

import java.io.FileInputStream;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import util.UtilConstants;


public class TestJUnitSmrcommAgriWell extends TestCase{
	
	//private IAgriWellCSIInterface pd;
	
	
	public TestJUnitSmrcommAgriWell(String name) {
		super(name);
	}
	
	
	//@Before
/*	public void setUp() throws Exception {
		try {
			InfoPortaDelegata infoPD = PDConfigReader.read(new FileInputStream(UtilConstants.START_PATH_TEST_FILES + "smrcomm\\pdAgriWellService.xml"));			
			pd = (IAgriWellCSIInterface)(PDProxy.newInstance(infoPD));						
		} 
		catch(Exception ex) {
		  System.err.println(ex.getMessage());
		}
		finally {
		
		}
	}

	

	@Test
	public void testTestResources() {
		try {			
			pd.testResources();
			System.out.println("testResuources agriWellk OK");
		} 
		catch (CSIException e) {			
			System.err.println("testResuources agriWellk KO");
			fail();
		} 
		catch (Exception e) {			
			fail();
		} 
		finally {			
		}
	}
	
	@Test
	public void testServiceFindAmmCompetenzaById() {
		try {			
			pd.agriwellFindFolderByPadreProcedimentoRuolo(7, "FUNZIONARIO@REG_PMN_AGRI", null, true, new Long(126238));
			System.out.println("serviceFindAmmCompetenzaById agriWellk OK");
		} 
		catch (CSIException e) {			
			System.err.println("serviceFindAmmCompetenzaById agriWellk KO ="+e.getMessage());
			fail();
		}
		catch (Exception e) {			
			System.err.println("serviceFindAmmCompetenzaById agriWellk KO ="+e.getMessage());
			fail();
		} 
		finally {			
		}
	}
	
	@Test
	public void testAgriwellFindDocumentoByIdRange() {
		try {			
			long[] idDoc = new long[1];
			idDoc[0] = 100;
			AgriWellEsitoDocumentoVO esitoDocVO =  pd.agriwellFindDocumentoByIdRange(idDoc);
			System.out.println("agriwellFindDocumentoByIdRange agriWellk OK");
		} 
		catch (CSIException e) {			
			System.err.println("agriwellFindDocumentoByIdRange agriWellk KO ="+e.getMessage());
			fail();
		}
		catch (Exception e) {			
			System.err.println("agriwellFindDocumentoByIdRange agriWellk KO ="+e.getMessage());
			fail();
		} 
		finally {			
		}
	}
	
	@Test
	public void testAgriwellFindFolderByPadreProcedimentoRuolo() {				
			try {
				AgriWellEsitoFolderVO esitoVO =  pd.agriwellFindFolderByPadreProcedimentoRuolo(7, "FUNZIONARIO@REG_PMN_AGRI", null, true, new Long(126238));
			} 
			catch (SmrcommInternalException e) {
				System.err.println("agriwellFindFolderByPadreProcedimentoRuolo agriWellk KO ="+e.getMessage());
				fail();
			} 
			catch (Exception e) {
				System.out.println("agriwellFindFolderByPadreProcedimentoRuolo agriWellk KO ="+e.getMessage());
				fail();
			}
			System.out.println("agriwellFindFolderByPadreProcedimentoRuolo agriWellk OK");		 		
	}
*/
	
}
