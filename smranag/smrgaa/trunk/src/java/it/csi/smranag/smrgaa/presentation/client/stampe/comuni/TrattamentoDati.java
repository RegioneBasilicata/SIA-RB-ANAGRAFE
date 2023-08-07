package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class TrattamentoDati extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/TrattamentoDati.srt";
  private final static String CODICE_SUB_REPORT = "TRATTAMENTO_DATI";

  public TrattamentoDati() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)parametri.get("personaFisicaVO");
    
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    InfoFascicoloVO infoFascicoloVO = (InfoFascicoloVO)parametri.get("infoFascicoloVO");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataDichiarazioneStampa = (Date)parametri.get("dataDichiarazioneStampa");
    
    
    
    
    DefaultTableLens tblLuogoDataPers = null;
    // Recupero le coordinate della cella in cui mi trovo
    tblLuogoDataPers = new DefaultTableLens(subReport.getTable("tblLuogoDataPers"));
    tblLuogoDataPers.setColWidth(0, 219);
    tblLuogoDataPers.setColWidth(1, 300); 
    tblLuogoDataPers.setObject(0, 1, StampeGaaServlet.checkNull(personaFisicaVO
        .getCognome()) + StampeGaaServlet.checkNull(" ") +
        StampeGaaServlet.checkNull(personaFisicaVO.getNome()));
    
    
    
    String dataDichiarazioneStampaStr = DateUtils.formatDateNotNull(dataDichiarazioneStampa);
    
    if(idDichiarazioneConsistenza !=null)
    {
      String luogoData = "";
      if(infoFascicoloVO.getCodiceFiscale() != null)
      {
        luogoData += StampeGaaServlet.checkNull(infoFascicoloVO.getDescComuneIntermediario())
         + StampeGaaServlet.checkNull(" ") + StampeGaaServlet.checkNull("("+
             infoFascicoloVO.getSiglaProvIntermediario()+")");
      }
      else
      {
        luogoData += StampeGaaServlet.checkNull(anagAziendaVO.getDescrizioneProvCompetenza());
      }
      
      
      luogoData += StampeGaaServlet.checkNull(", ") + StampeGaaServlet.checkNull(dataDichiarazioneStampaStr);
      
      
      tblLuogoDataPers.setObject(0, 0, luogoData);
    }
    
    subReport.setElement("tblLuogoDataPers", tblLuogoDataPers);
    
    
    
  }
}