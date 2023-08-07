package it.csi.solmr.business.anag;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smrcomms.reportdin.dto.TipologiaReportVO;
import it.csi.smrcomms.reportdin.util.ReportQueryUtil;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.util.SolmrLogger;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.StatefulTimeout;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Bean per la gestione e l'esposizione dei metodi di utilità comune all'anagrafe
 *
 * <p>Title: SMRGAA</p>
 *
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: CSI - PIEMONTE</p>
 *
 * @author Mauro Vocale
 * @version 1.0
 */

@Stateless(name="comp/env/solmr/anag/ReportDin",mappedName="comp/env/solmr/anag/ReportDin")
@TransactionManagement(TransactionManagementType.CONTAINER)
@StatefulTimeout(unit = java.util.concurrent.TimeUnit.SECONDS, value = 295)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class ReportDinBean implements ReportDinLocal {

	
	/**
   * 
   */
  private static final long serialVersionUID = -2281584648384237012L;
  
  
  SessionContext sessionContext;
	private transient CommonDAO commonDAO = null;

	
    @Resource
	public void setSessionContext(SessionContext sessionContext) {
		this.sessionContext = sessionContext;
		initializeDAO();
	}

	private void initializeDAO() throws EJBException {
		try {
			commonDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
		}
		catch (ResourceAccessException ex) {
			SolmrLogger.error(this, ex.getMessage());
			throw new EJBException(ex);
		}
	}

	



  /*************************************************
  /* I METODI SEGUENTI SERVONO AI REPORT DINAMICI */
  /************************************************/


  /**
    * Esegue la query con i parametri ed i valori selezionati dell'utente
    * @param idTipologia
    * @param parameters
    * @return
    * @throws Exception
    */
	 public Htmpl getRisultatoQuery(TipologiaReportVO tipologiaReportVO, HashMap<?,?> parametriFissiHtmpl, Htmpl layout) throws 
	 Exception,SolmrException
	    {
	       Htmpl tRisultatoQuery = null;

	       try {
	          String queryWithClause = ReportQueryUtil.transform(tipologiaReportVO.getQuery(),
	                                tipologiaReportVO.getVariabiliReportArr(), parametriFissiHtmpl);

	          SolmrLogger.debug(this, "Caricamento risultato query...");
	          tRisultatoQuery = commonDAO.executeQueryReportDin(queryWithClause, tipologiaReportVO.getVariabiliReportArr(), tipologiaReportVO.getColonneRisultArr(), layout);
	       }
	       catch (SolmrException ex) {
	         throw new SolmrException(ex.getMessage());
	       }
	       catch (Exception ex) {
	           throw ex;
	       }

	       return tRisultatoQuery;
	    }

	 public HSSFWorkbook getRisultatoQuery(TipologiaReportVO tipologiaReportVO, HashMap<?,?> parametriFissiHtmpl, HSSFWorkbook workBook,String nomeFoglio) throws Exception,SolmrException
	   {
	      HSSFWorkbook result = null;

	      try {
	         String queryWithClause = ReportQueryUtil.transform(tipologiaReportVO.getQuery(),
	                               tipologiaReportVO.getVariabiliReportArr(), parametriFissiHtmpl);

	         SolmrLogger.debug(this, "Caricamento risultato query...");
	         result = commonDAO.executeQueryReportDin(queryWithClause, tipologiaReportVO.getVariabiliReportArr(), tipologiaReportVO.getColonneRisultArr(), workBook, nomeFoglio);
	      }
	      catch (SolmrException ex) {
	        throw new SolmrException(ex.getMessage());
	      }
	      catch (Exception ex) {
	       throw new Exception(ex.getMessage());
	      }

	      return result;
	   }


  /**
   * Elenco variabili e valori associate alla query selezionata
   */
  public HashMap<?,?> getQueryPopolamento(String queryPopolamento, String idRptVariabileReport)
      throws Exception,SolmrException
  {
     HashMap<?,?> result = null;

     try
     {
        SolmrLogger.debug(this, "queryPopolamento: "+queryPopolamento);
        result = commonDAO.getQueryPopolamento(queryPopolamento, idRptVariabileReport);
     }
     catch (SolmrException ex) {
        throw new SolmrException(ex.getMessage());
     }
     catch (Exception ex) {
       throw new Exception(ex.getMessage());
     }

     return result;
  }
  
  
  
  
  
  public String testDB() throws Exception
  {
      return commonDAO.testDB();
  }
  
  
        

}
