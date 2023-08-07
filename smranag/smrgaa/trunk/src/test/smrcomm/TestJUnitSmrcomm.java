package smrcomm;

import static org.junit.Assert.fail;
import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.csi.wrapper.CSIException;
import it.csi.smrcomms.smrcomm.dto.datientiprivati.DatiEntePrivatoVO;
import it.csi.smrcomms.smrcomm.interfacecsi.ISmrcommCSIInterface;

import java.io.FileInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.UtilConstants;


public class TestJUnitSmrcomm {
	
	private ISmrcommCSIInterface pd;

	@Before
	public void setUp() throws Exception {
		try {
			String path = UtilConstants.START_PATH_TEST_FILES+"smrcomm\\pdComuneServiceNP.xml";
			InfoPortaDelegata infoPD = PDConfigReader.read(new FileInputStream(path));			
			pd = (ISmrcommCSIInterface)(PDProxy.newInstance(infoPD));						
		} 
		catch(Exception ex) {
		  System.err.println(ex.getMessage());
		}
		finally {
		
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTestResources() {
		try {			
			pd.testResources();
			System.out.println("testResuources() smrcomm OK");
		} 
		catch (CSIException e) {			
			System.err.println("testResuources() smrcomm KO");
			fail();
		} 
		catch (Exception e) {			
			fail();
		} 
		finally {			
		}
	}
	
	@Test
	public void testsmrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange()  {
		try {			
			String[] codEnte = new String[1];
			codEnte[0] = "D00101005019";
			long[] id = pd.smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange(codEnte,false);			
			System.out.println("testsmrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange() smrcomm OK");
		} 
		catch (CSIException e) {			
			System.err.println("testsmrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange() smrcomm KO");
			fail();
		} 
		catch (Exception e) {			
			fail();
		} 
		finally {			
		}
	}
	
	@Test
	public void testsmrcommGetEntiPrivatiByIdEntePrivatoRange()  {
		try {			
			long[] arrIdEntePrivato = new long[1];
			arrIdEntePrivato[0] = 111;
			DatiEntePrivatoVO[] datiEntePrivArr = pd.smrcommGetEntiPrivatiByIdEntePrivatoRange(arrIdEntePrivato, 2, null);		
			System.out.println("testsmrcommGetEntiPrivatiByIdEntePrivatoRange() smrcomm OK");
		} 
		catch (CSIException e) {			
			System.err.println("testsmrcommGetEntiPrivatiByIdEntePrivatoRange() smrcomm KO");
			fail();
		} 
		catch (Exception e) {			
			fail();
		} 
		finally {			
		}
	}

}
