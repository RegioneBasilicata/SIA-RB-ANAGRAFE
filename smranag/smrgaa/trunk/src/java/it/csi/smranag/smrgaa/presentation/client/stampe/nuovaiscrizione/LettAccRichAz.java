package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class LettAccRichAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/LettAccRichAz.srt";
  private final static String CODICE_SUB_REPORT = "LETT_ACC_RICH_AZ";

  public LettAccRichAz() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");
    
    String txtDescEstesa = aziendaNuovaVO.getDescEstesaTipoRichiesta();
    subReport.setElement("txtDescEstesa", StampeGaaServlet.checkNull(txtDescEstesa));
    
    String denominazione = aziendaNuovaVO.getCognome()+" "+aziendaNuovaVO.getNome();
    subReport.setElement("txtDenominazione", StampeGaaServlet.checkNull(denominazione));
    
    String txtTestoRichiesta = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_STM_INTRO_NAP);
    txtTestoRichiesta += " per ";
    if("S".equalsIgnoreCase(aziendaNuovaVO.getFlagNoteObbligatorie()))
    {
      txtTestoRichiesta += aziendaNuovaVO.getNote();
    }
    else
    {
      txtTestoRichiesta += aziendaNuovaVO.getDescMotivoRichiesta();
    }
    subReport.setElement("txtTestoRichiesta", StampeGaaServlet.checkNull(txtTestoRichiesta));
    
    String txtNomeAllegato = aziendaNuovaVO.getNomAllegato();
    subReport.setElement("txtNomeAllegato", StampeGaaServlet.checkNull(txtNomeAllegato));
    
    
    
    
    Date dataDichiarazioneStampa = (Date)parametri.get("dataStampa");    
   
    
    DefaultTableLens tblLuogoDataProd = null;
    // Recupero le coordinate della cella in cui mi trovo
    tblLuogoDataProd = new DefaultTableLens(subReport.getTable("tblLuogoDataProd"));
    tblLuogoDataProd.setColWidth(0, 318);
    String dataDichiarazioneStampaStr = DateUtils.formatDateTimeNotNull(dataDichiarazioneStampa);
    String comune = aziendaNuovaVO.getDescComune();
    if(Validator.isEmpty(comune))
    {
      comune = aziendaNuovaVO.getDescResComune();
    }
    String luogoData = StampeGaaServlet.checkNull(comune) + StampeGaaServlet.checkNull(", ") + StampeGaaServlet.checkNull(dataDichiarazioneStampaStr);
    tblLuogoDataProd.setObject(0, 0, luogoData);
    tblLuogoDataProd.setColWidth(1, 250);
    tblLuogoDataProd.setObject(0, 1, StampeGaaServlet.checkNull(denominazione));
    
    
    
    
    
    
    
    subReport.setElement("tblLuogoDataProd", tblLuogoDataProd);
    
    
    
    
    
  }

}