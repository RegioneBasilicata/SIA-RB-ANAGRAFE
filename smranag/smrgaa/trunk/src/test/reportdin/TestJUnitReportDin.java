package reportdin;

import static org.junit.Assert.fail;
import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.csi.wrapper.CSIException;
import it.csi.csi.wrapper.SystemException;
import it.csi.smrcomms.reportdin.dto.AmmCompetenzaReportDinVO;
import it.csi.smrcomms.reportdin.dto.IntermediarioReportDinVO;
import it.csi.smrcomms.reportdin.dto.MacroCategoriaReportOutput;
import it.csi.smrcomms.reportdin.interfacecsi.ReportDinCSIInterface;
import it.csi.solmr.dto.comune.AmmCompetenzaVO;

import java.io.FileInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestJUnitReportDin {
	
	private ReportDinCSIInterface pd;

	@Before
	public void setUp() throws Exception {
		try {
			String path = "C:\\workspace\\smrgaa\\src\\test\\reportdin\\pdReportDin.xml";
			InfoPortaDelegata infoPD = PDConfigReader.read(new FileInputStream(path));			
			pd = (ReportDinCSIInterface)(PDProxy.newInstance(infoPD));						
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
			System.out.println("testResuources()  OK");
		} 
		catch (CSIException e) {			
			System.err.println("testResuources()  KO");
			fail();
		} 
		catch (Exception e) {
			System.err.println("testResuources()  KO");
			fail();
		} 
		finally {			
		}
	}
	
	
	@Test
	public void testGetElencoCategorieReportByProcedimentoAndRuolo(){
		try {
			MacroCategoriaReportOutput[] result = pd.getElencoCategorieReportByProcedimentoAndRuolo(new Long(7), "FUNZIONARIO@REG_PMN_AGRI");
			System.out.println("testResuources() smrcomm OK");
		} 
		catch (SystemException e) {
			System.err.println("getElencoCategorieReportByProcedimentoAndRuolo()  KO");
			fail();
		}
		catch (Exception e) {
			System.err.println("getElencoCategorieReportByProcedimentoAndRuolo()  KO");
			fail();
		} 
	}
	
	@Test
	public void testServiceFindAmmCompetenzaByCodiceAmm() {
		try {			
			AmmCompetenzaVO ammCompetenzaVO =  pd.serviceFindAmmCompetenzaByCodiceAmm("ASLTO03");
			System.out.println("serviceFindAmmCompetenzaByCodiceAmm()  OK");
		} 
		catch (CSIException e) {			
			System.err.println("serviceFindAmmCompetenzaByCodiceAmm()  KO");
			fail();
		} 
		catch (Exception e) {
			System.err.println("serviceFindAmmCompetenzaByCodiceAmm()  KO");
			fail();
		} 
		finally {			
		}
	}
	
	@Test
	public void testServiceFindAmmCompetenzaReportDinByCodiceAmm() {
		try {			
			AmmCompetenzaReportDinVO ammCompetenzaVO =  pd.serviceFindAmmCompetenzaReportDinByCodiceAmm("01");
			System.out.println("serviceFindAmmCompetenzaByCodiceAmm()  OK");
		} 
		catch (CSIException e) {			
			System.err.println("serviceFindAmmCompetenzaByCodiceAmm()  KO");
			fail();
		} 
		catch (Exception e) {
			System.err.println("serviceFindAmmCompetenzaByCodiceAmm()  KO");
			fail();
		} 
		finally {			
		}
	}
	
	@Test
	public void testGetElencoColonneRisultatoByIdTipologia() {
		try {			
			it.csi.smrcomms.reportdin.dto.ColonneRisultatoOutput[] result =  pd.getElencoColonneRisultatoByIdTipologia(new Long(1));
			System.out.println("getElencoColonneRisultatoByIdTipologia()  OK");
		} 
		catch (CSIException e) {			
			System.err.println("getElencoColonneRisultatoByIdTipologia()  KO");
			fail();
		} 
		catch (Exception e) {
			System.err.println("getElencoColonneRisultatoByIdTipologia()  KO");
			fail();
		} 
		finally {			
		}
	}
	
	
	@Test
	public void testGetElencoVariabiliByIdTipologia() {
		try {			
			it.csi.smrcomms.reportdin.dto.VariabileReportOutput[] result =  pd.getElencoVariabiliByIdTipologia(new Long(1));
			System.out.println("getElencoVariabiliByIdTipologia()  OK");
		} 
		catch (CSIException e) {			
			System.err.println("getElencoVariabiliByIdTipologia()  KO ="+e.getMessage());
			fail();
		} 
		catch (Exception e) {
			System.err.println("getElencoVariabiliByIdTipologia()  KO"+e.getMessage());
			fail();
		} 
		finally {			
		}
	}
	
	
	
	
	@Test
	public void testServiceFindIntermediarioReportDinByCodiceFiscale() {
		try {			
			IntermediarioReportDinVO intermReport = pd.serviceFindIntermediarioReportDinByCodiceFiscale("AAAAAA00B77B000F");
			System.out.println("serviceFindIntermediarioReportDinByCodiceFiscale()  OK");
		} 
		catch (CSIException e) {			
			System.err.println("serviceFindIntermediarioReportDinByCodiceFiscale()  KO");
			fail();
		} 
		catch (Exception e) {
			System.err.println("serviceFindIntermediarioReportDinByCodiceFiscale()  KO");
			fail();
		} 
		finally {			
		}
	}

}
