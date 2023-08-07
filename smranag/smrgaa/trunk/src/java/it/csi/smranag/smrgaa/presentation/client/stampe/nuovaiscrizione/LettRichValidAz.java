package it.csi.smranag.smrgaa.presentation.client.stampe.nuovaiscrizione;

import inetsoft.report.ReportSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class LettRichValidAz extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/nuovaiscrizione/LettRichValidAz.srt";
  private final static String CODICE_SUB_REPORT = "LETT_RICH_VALID_AZ";

  public LettRichValidAz() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)parametri.get("aziendaNuovaVO");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataConferma = (Date)parametri.get("dataConferma");
    Vector<PersonaFisicaVO> vPersonaFisica = anagFacadeClient.getSoggetti(anagAziendaVO.getIdAzienda(), dataConferma);
    RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
    String txtTestoDichiarazione = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_STM_INTRO_DV);
    String txtDichiarazione = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DICH_RICH_VAL_STMP);
    
    PersonaFisicaVO personaFisicaVO = null;
    PersonaFisicaVO rappVO = null;
    for(int i=0;i<vPersonaFisica.size();i++)
    {
      if(ruoloUtenza.getCodiceFiscale().equalsIgnoreCase(vPersonaFisica.get(i).getCodiceFiscale()))
      {
        personaFisicaVO = vPersonaFisica.get(i);
      }
      
      if(vPersonaFisica.get(i).getIdRuolo().compareTo(new Long(1)) == 0)
      {
        rappVO = vPersonaFisica.get(i);
      } 
    }
    
    String cognomeNome = "";
    String ruolo = "";
    if(Validator.isNotEmpty(personaFisicaVO))
    {
      cognomeNome = personaFisicaVO.getCognome()+" "+personaFisicaVO.getNome();
      ruolo = personaFisicaVO.getRuolo();
    }
    else
    {
      cognomeNome = rappVO.getCognome()+" "+rappVO.getNome();
      ruolo = rappVO.getRuolo();
    }
    
    
    subReport.setElement("txtDenominazione", StampeGaaServlet.checkNull(cognomeNome));
    subReport.setElement("txtRuoloInAzienda", StampeGaaServlet.checkNull(ruolo));
    subReport.setElement("txtCuaaDenominazione", StampeGaaServlet.checkNull(anagAziendaVO.getCUAA()+" - "+anagAziendaVO.getDenominazione()));
    subReport.setElement("txtTestoDichiarazione", StampeGaaServlet.checkNull(txtTestoDichiarazione));
    subReport.setElement("txtMotivoDichiarazione", StampeGaaServlet.checkNull(aziendaNuovaVO.getDescMotivoDichiarazione()));
    subReport.setElement("txtDichiarazione", StampeGaaServlet.checkNull(txtDichiarazione));
    subReport.setElement("txtNote", StampeGaaServlet.checkNull(aziendaNuovaVO.getNote()));
    
    
    
    Date dataDichiarazioneStampa = aziendaNuovaVO.getDataAggiornamentoIter();
   
    
    DefaultTableLens tblLuogoDataProd = null;
    // Recupero le coordinate della cella in cui mi trovo
    tblLuogoDataProd = new DefaultTableLens(subReport.getTable("tblLuogoDataProd"));
    tblLuogoDataProd.setColWidth(0, 318);
    String dataDichiarazioneStampaStr = DateUtils.formatDateNotNull(dataDichiarazioneStampa);
    String luogoData = StampeGaaServlet.checkNull(aziendaNuovaVO.getDescComune()) + StampeGaaServlet.checkNull(", ") + StampeGaaServlet.checkNull(dataDichiarazioneStampaStr);
    tblLuogoDataProd.setObject(0, 0, luogoData);
    tblLuogoDataProd.setColWidth(1, 250);
    tblLuogoDataProd.setObject(0, 1, StampeGaaServlet.checkNull(cognomeNome));   
    
    
    subReport.setElement("tblLuogoDataProd", tblLuogoDataProd);
    
    
    
    
    
  }

}