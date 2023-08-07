package solmruma;

import static org.junit.Assert.fail;
import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.csi.wrapper.SystemException;
import it.csi.solmr.dto.uma.DittaUMAVO;
import it.csi.solmr.dto.uma.MacchinaVO;
import it.csi.solmr.interfaceCSI.uma.UmaCSIInterface;

import java.io.FileInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestJUnitSolmruma {
	
	private UmaCSIInterface pd;

	@Before
	public void setUp() throws Exception {
		try {
			String path = "C:\\workspace\\smrgaa\\src\\test\\solmruma\\pdUmaService.xml";
			InfoPortaDelegata infoPD = PDConfigReader.read(new FileInputStream(path));			
			pd = (UmaCSIInterface)(PDProxy.newInstance(infoPD));						
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
	public void testserviceGetDittaUmaByIdAzienda(){
		try {
			DittaUMAVO dittaUmaVo =  pd.serviceGetDittaUmaByIdAzienda(new Long(117));
			System.out.println("testserviceGetDittaUmaByIdAzienda() solmruma OK");
		} 
		catch (SystemException e) {
			System.err.println("testserviceGetDittaUmaByIdAzienda()  KO");
			fail();
		}
		catch (Exception e) {
			System.err.println("testserviceGetDittaUmaByIdAzienda()  KO ="+e.getMessage());
			fail();
		} 
	}
	
	
	@Test
	public void testServiceGetElencoAziendeUtilizzatrici(){
		try {
			long[] result = pd.serviceGetElencoAziendeUtilizzatrici(new Long(7));
			System.out.println("testServiceGetElencoAziendeUtilizzatrici() solmruma OK");
		} 
		catch (SystemException e) {
			System.err.println("testServiceGetElencoAziendeUtilizzatrici()  KO");
			fail();
		}
		catch (Exception e) {
			System.err.println("testServiceGetElencoAziendeUtilizzatrici()  KO");
			fail();
		} 
	}
	
	
	@Test
	public void testServiceGetElencoMacchineByIdAzienda(){
		try {
			MacchinaVO[] result = pd.serviceGetElencoMacchineByIdAzienda(new Long(7), true, new Long(1));
			System.out.println("testServiceGetElencoMacchineByIdAzienda() solmruma OK");
		} 
		catch (SystemException e) {
			System.err.println("testServiceGetElencoMacchineByIdAzienda()  KO");
			fail();
		}
		catch (Exception e) {
			System.err.println("testServiceGetElencoMacchineByIdAzienda()  KO");
			fail();
		} 
	}
	
	

}
