package it.csi.smranag.smrgaa.presentation.client.stampe.stampaFascicolo;

import inetsoft.report.ReportSheet;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.papua.papuaserv.dto.legacy.axis.RuoloUtenzaPapua;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
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

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class FirmaFascicolo extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/stampaFascicolo/FirmaFascicolo.srt";
  private final static String CODICE_SUB_REPORT = "FIRMA_FASCICOLO";

  public FirmaFascicolo() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    TabularSheet layout = (TabularSheet) subReport;
    
    RuoloUtenza ruoloUtenza = (RuoloUtenza) request.getSession()
        .getAttribute("ruoloUtenza");
    
    DichiarazioneConsistenzaGaaVO  dichConsistenza = 
      (DichiarazioneConsistenzaGaaVO)parametri.get("dichConsistenza");
    
    Date dataDichiarazioneStampa = (Date)parametri.get("dataDichiarazioneStampa");
    Long idDichiarazioneConsistenza = (Long)parametri.get("idDichiarazioneConsistenza");
    InfoFascicoloVO infoFascicoloVO = (InfoFascicoloVO)parametri.get("infoFascicoloVO");
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)parametri.get("personaFisicaVO");
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    
    
    //Stampa testo
    subReport.setElement("testo", StampeGaaServlet.checkNull(richiestaTipoReportVO.getTesto()));
    
    
    //Quadro firma
    String responsabile = null, operatore = null;
    if (dichConsistenza == null)
    {
      // Sto stampando alla date attuale
      // Se l'utente appartiene ad uno dei seguenti casi devo recuperarmi il
      // responsabile
      if (ruoloUtenza.isUtenteOPR() || ruoloUtenza.isUtenteOPRGestore()
          || ruoloUtenza.isUtenteIntermediario())
      {
        subReport.removeElement("SeparatorDichiarazioni");
        subReport.removeElement("txtQuadroFirmaProd");
        subReport.removeElement("nlQuadroFirmaProd");
        //removeQuadroF1(report);
        if (ruoloUtenza.isUtenteOPR())
        {
          it.csi.solmr.dto.comune.AmmCompetenzaVO ammCompetenzaVO = anagFacadeClient
              .serviceFindAmmCompetenzaByCodiceAmm(ruoloUtenza
                  .getCodiceEnte());
          if (ammCompetenzaVO != null)
            responsabile = ammCompetenzaVO.getResponsabile();
        }
        else
        {

          it.csi.solmr.dto.comune.IntermediarioVO intermediarioVO = anagFacadeClient
              .serviceFindIntermediarioByCodiceFiscale(ruoloUtenza
                  .getCodiceEnte());
          if (intermediarioVO != null)
          {
            intermediarioVO = anagFacadeClient
                .serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(
                    intermediarioVO.getIdIntermediarioLong(), new Long(
                        SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE), null);
            if (intermediarioVO != null)
              responsabile = intermediarioVO.getResponsabile();
          }
        }
      }
      else
      {
        removeQuadroFirmaCaa(layout);
        //removeQuadroF2(report);
      }      
      
      responsabile = null;// rimuovo il responsabile come richiesto dal
                          // dominio in data 30/04/2007
    }
    else
    {
      // Sto stampando una dichiarazione
      if (!(ruoloUtenza.isUtenteOPR() || ruoloUtenza.isUtenteOPRGestore() || ruoloUtenza
          .isUtenteIntermediario()))
      {
        removeQuadroFirmaCaa(layout);
        //removeQuadroF2(report);
      }
      else
      {
        subReport.removeElement("SeparatorDichiarazioni");
        subReport.removeElement("txtQuadroFirmaProd");
        subReport.removeElement("nlQuadroFirmaProd");
        
        //removeQuadroF1(report);
        responsabile = dichConsistenza.getResponsabile();
        
        if (dichConsistenza.getIdUtente() != null)
        {
          //ruoloUtenza = anagFacadeClient.serviceGetRuoloUtenzaByIdUtente(new Long(
              //dichConsistenza.getIdUtente()));
          
          UtenteAbilitazioni utenteAbilitazioni =  anagFacadeClient.getUtenteAbilitazioniByIdUtenteLogin(dichConsistenza.getIdUtente());
          ruoloUtenza = new RuoloUtenzaPapua(utenteAbilitazioni);
          
          if (ruoloUtenza != null)
          {
            String descrizioneEnte = null;
            String codiceEnte = ruoloUtenza.getCodiceEnte();
            if (ruoloUtenza.isUtenteOPR())
            {
              it.csi.solmr.dto.comune.AmmCompetenzaVO ammCompetenzaVO = anagFacadeClient
                  .serviceFindAmmCompetenzaByCodiceAmm(ruoloUtenza
                      .getCodiceEnte());
              if (ammCompetenzaVO != null)
                descrizioneEnte = ammCompetenzaVO.getDenominazione1();
            }
            else
            {
              it.csi.solmr.dto.comune.IntermediarioVO intermediarioVO = anagFacadeClient
                  .serviceFindIntermediarioByCodiceFiscale(ruoloUtenza
                      .getCodiceEnte());
              if (intermediarioVO != null)
              {
                intermediarioVO = anagFacadeClient
                    .serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(
                        intermediarioVO.getIdIntermediarioLong(), new Long(
                            SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE), null);
                if (intermediarioVO != null)
                  descrizioneEnte = intermediarioVO.getDenominazione();
              }
            }

            operatore = "Operatore: " + ruoloUtenza.getDenominazione() + " ("
                + descrizioneEnte + " - " + codiceEnte + ")";
          }
        }
      }
    }
    
    
    if (operatore != null)
    {
      subReport.setElement("txtOperatore", StampeGaaServlet.checkNull(operatore));
    }
    
    String dataDichiarazioneStampaStr = DateUtils.formatDateNotNull(dataDichiarazioneStampa);
    
    if (responsabile != null)
    {
      
      DefaultTableLens tblLuogoDataAmm = null;

      // Recupero le coordinate della cella in cui mi trovo
      tblLuogoDataAmm = new DefaultTableLens(subReport.getTable("tblLuogoDataAmm"));
      tblLuogoDataAmm.setColWidth(0, 219);
      tblLuogoDataAmm.setColWidth(1, 300);
      
      if(idDichiarazioneConsistenza !=null)
      {
        String luogoData = StampeGaaServlet.checkNull(infoFascicoloVO.getDescComuneIntermediario())
         + StampeGaaServlet.checkNull(" ") + StampeGaaServlet.checkNull("("+
             infoFascicoloVO.getSiglaProvIntermediario()+")");
        
        
        luogoData += StampeGaaServlet.checkNull(", ") + StampeGaaServlet.checkNull(dataDichiarazioneStampaStr);
        
        
        
        tblLuogoDataAmm.setObject(0, 0, StampeGaaServlet.checkNull(luogoData));
      }      
      tblLuogoDataAmm.setObject(0, 1, StampeGaaServlet.checkNull(responsabile));
      
      subReport.setElement("tblLuogoDataAmm", tblLuogoDataAmm);
    }      
    /*else
    {
      subReport.removeElement("SeparatorDichiarazioni");
    }*/
    
    DefaultTableLens tblLuogoDataProd = null;
    // Recupero le coordinate della cella in cui mi trovo
    tblLuogoDataProd = new DefaultTableLens(subReport.getTable("tblLuogoDataProd"));
    tblLuogoDataProd.setColWidth(0, 219);
    tblLuogoDataProd.setColWidth(1, 300);
    tblLuogoDataProd.setObject(0, 1, StampeGaaServlet.checkNull(personaFisicaVO
        .getCognome()) + StampeGaaServlet.checkNull(" ") +
        StampeGaaServlet.checkNull(personaFisicaVO.getNome()));
    
    
    
    
    
    if(idDichiarazioneConsistenza !=null)
    {
      String luogoData = "";
      if(infoFascicoloVO.getCodiceFiscale() != null)
      {
        luogoData += StampeGaaServlet.checkNull(infoFascicoloVO.getDescComuneIntermediario())
         + StampeGaaServlet.checkNull(" ") + StampeGaaServlet.checkNull("("+infoFascicoloVO.getSiglaProvIntermediario()+")");
      }
      else
      {
        luogoData += StampeGaaServlet.checkNull(anagAziendaVO.getDescrizioneProvCompetenza());
      }
      
      luogoData += StampeGaaServlet.checkNull(", ") + StampeGaaServlet.checkNull(dataDichiarazioneStampaStr);
      
      tblLuogoDataProd.setObject(0, 0, luogoData);
    }
    
    subReport.setElement("tblLuogoDataProd", tblLuogoDataProd);
   
    
    
  }
  
  
  private void removeQuadroFirmaCaa(TabularSheet report) throws SolmrException,
    CloneNotSupportedException
  {
    StampeGaaServlet.removeRows(report, "lblSezione", 2);   
  }
  
}