package it.csi.smranag.smrgaa.presentation.client.stampe.variazionecatastale;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class DichProdVarCat extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/variazionecatastale/DichProdVarCat.srt";
  private final static String CODICE_SUB_REPORT = "DICH_PROD_VAR_CAT";

  public DichProdVarCat() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);   
    
    
    Vector<TipoAttestazioneVO> vAttestazioneVO = gaaFacadeClient.getAttestStampaProtoc(
        richiestaTipoReportVO.getIdReportSubReport().longValue());
    
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)parametri.get("personaFisicaVO");
    
    if(Validator.isNotEmpty(vAttestazioneVO))
    {
      DefaultTableLens tblAttestazioni = new DefaultTableLens(subReport.getTable("tblAttestazioni"));
      tblAttestazioni.setFont(StampeGaaServlet.FONT_SERIF_10);
      tblAttestazioni.setColWidth(0, 600);
      
      StampeGaaServlet.setNoBorder(tblAttestazioni);     
      
      for(int i=0;i<vAttestazioneVO.size();i++)
      {        
        tblAttestazioni.addRow();
        
        tblAttestazioni.setAlignment(i, 0, StyleConstants.H_LEFT);
        tblAttestazioni.setObject(i, 0, StampeGaaServlet.checkNull(vAttestazioneVO.get(i).getDescrizione()));
      }
      
      
      subReport.setElement("tblAttestazioni", tblAttestazioni);
      
      
    }
    else
    {
      subReport.removeElement("tblAttestazioni");
    }
    
    
    DefaultTableLens tblLuogoDataProd = null;
    // Recupero le coordinate della cella in cui mi trovo
    tblLuogoDataProd = new DefaultTableLens(subReport.getTable("tblLuogoDataProd"));
    tblLuogoDataProd.setColWidth(0, 219);
    tblLuogoDataProd.setColWidth(1, 300);
    tblLuogoDataProd.setObject(0, 1, StampeGaaServlet.checkNull(personaFisicaVO
        .getCognome()) + StampeGaaServlet.checkNull(" ") +
        StampeGaaServlet.checkNull(personaFisicaVO.getNome()));
    
    subReport.setElement("tblLuogoDataProd", tblLuogoDataProd);
    
    
  }
  
}