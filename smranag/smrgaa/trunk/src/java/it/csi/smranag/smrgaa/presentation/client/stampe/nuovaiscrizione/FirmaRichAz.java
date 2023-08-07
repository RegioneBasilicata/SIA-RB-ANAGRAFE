package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class FirmaRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/FirmaRichAz.srt";
  private final static String CODICE_SUB_REPORT = "FIRMA_RICH_AZ";

  public FirmaRichAz() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");
    Date dataDichiarazioneStampa = (Date)parametri.get("dataStampa");
   
    
    
    Vector<TipoAttestazioneVO> vAttestazioneVO = gaaFacadeClient.getAttestStampaProtoc(
        richiestaTipoReportVO.getIdReportSubReport().longValue());
    
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
    
    
    
    
   
    
    DefaultTableLens tblLuogoDataProd = null;
    // Recupero le coordinate della cella in cui mi trovo
    tblLuogoDataProd = new DefaultTableLens(subReport.getTable("tblLuogoDataProd"));
    tblLuogoDataProd.setColWidth(0, 219);
    String dataDichiarazioneStampaStr = DateUtils.formatDateTimeNotNull(dataDichiarazioneStampa);
    String comune = aziendaNuovaVO.getDescComune();
    if(Validator.isEmpty(comune))
    {
      comune = aziendaNuovaVO.getDescResComune();
    }
    String luogoData = StampeGaaServlet.checkNull(comune) + StampeGaaServlet.checkNull(", ") + StampeGaaServlet.checkNull(dataDichiarazioneStampaStr);
    tblLuogoDataProd.setObject(0, 0, luogoData);
    tblLuogoDataProd.setColWidth(1, 300);
    tblLuogoDataProd.setObject(0, 1, StampeGaaServlet.checkNull(aziendaNuovaVO
        .getCognome()) + StampeGaaServlet.checkNull(" ") +
        StampeGaaServlet.checkNull(aziendaNuovaVO.getNome()));
    
    
    
    
    
    
    
    subReport.setElement("tblLuogoDataProd", tblLuogoDataProd);
   
    
    
  }
  
  
}