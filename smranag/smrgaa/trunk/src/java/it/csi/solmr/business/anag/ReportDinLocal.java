package it.csi.solmr.business.anag;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smrcomms.reportdin.dto.TipologiaReportVO;
import it.csi.solmr.exception.SolmrException;

import java.util.HashMap;

import javax.ejb.Local;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * <p>
 * Title: SMRGAA
 * </p>
 * 
 * <p>
 * Description: Anagrafe delle Imprese Agricole e Agro-Alimentari
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: CSI - PIEMONTE
 * </p>
 * 
 * @author Mauro Vocale
 * @version 1.0
 */

@Local
public interface ReportDinLocal
{

  public String testDB() throws Exception;
  

  /*****************************************************************************
   * /* I METODI SEGUENTI SERVONO AI REPORT DINAMICI
   */
  /** ********************************************* */
  public HashMap<?, ?> getQueryPopolamento(String queryPopolamento,
      String idRptVariabileReport) throws Exception, SolmrException;

  public Htmpl getRisultatoQuery(TipologiaReportVO tipologiaReportVO,
      HashMap<?, ?> parametriFissiHtmpl, Htmpl layout) throws Exception,
      SolmrException;

  public HSSFWorkbook getRisultatoQuery(TipologiaReportVO tipologiaReportVO,
      HashMap<?, ?> parametriFissiHtmpl, HSSFWorkbook workBook,
      String nomeFoglio) throws Exception, SolmrException;
}
