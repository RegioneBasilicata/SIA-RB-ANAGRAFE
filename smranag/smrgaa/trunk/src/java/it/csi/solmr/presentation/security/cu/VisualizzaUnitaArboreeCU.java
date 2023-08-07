package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.dto.terreni.CompensazioneAziendaVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaUnitaArboreeCU extends AutorizzazioneCUStandard
{
  /**
   * 
   */
  private static final long serialVersionUID = -7663647241758067787L;

  public VisualizzaUnitaArboreeCU()
  {
    this.cuName = "VISUALIZZA_UNITA_ARBOREE";
    this.isCUForReadWrite = false;
  }

  /**
   * Genera il menu delle pagine html relative a questo macro CU Iride2
   * 
   * @param htmpl
   *          Htmpl
   * @param request
   *          HttpServletRequest
   * @return java.lang.String
   */
  public String writeMenu(Htmpl htmpl, HttpServletRequest request)
  {

    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO");
    RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
    SolmrException sex = (SolmrException) request.getAttribute("statoAzienda");
    FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO) request
        .getSession().getAttribute("filtriUnitaArboreaRicercaVO");
    StoricoParticellaVO[] elencoParticelleArboree = (StoricoParticellaVO[]) request
        .getAttribute("elencoParticelleArboree");
    StoricoParticellaVO[] elencoUnitaArboreeImportabili = (StoricoParticellaVO[]) request
        .getAttribute("elencoUnitaArboreeImportabili");

    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
    Vector<UteVO> elencoUte = null;

    super.writeBanner(htmpl, request);
    super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
    super.writeBarraStato(sex, htmpl);

    if (anagAziendaVO != null)
    {
      try
      {
        elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(),
            new Boolean(false));
      }
      catch (SolmrException se)
      {
      }
    }
    // Gestione delle voci di menù di anagrafe
    super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
    Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
    
    
    if (iride2AbilitazioniVO.isUtenteAbilitato("ESPORTA_DATI_UV"))
    {
      request.getSession().setAttribute("esportaDatiUV","true");
    }
    
    
    // Voci di menù relative alle unità arboree
    if (filtriUnitaArboreaRicercaVO != null)
    {
      
      
      //in questo caso nell'elenco  elimino alcune colonne perchè sono col ruolo azienda
      if (iride2AbilitazioniVO.isUtenteAbilitato("AZIENDE_TITOLARE"))
      {
        request.setAttribute("elencoRidotto", "ok");
      }
      
      
      if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_SCHEDARIO"))
      {
        htmpl.newBlock("blkUnitaVitate.blkVisualizzaSchedario");
      }
      
      if (iride2AbilitazioniVO.isUtenteAbilitato("ALBO_VIGNETI_CCIAA"))
      {
        htmpl.newBlock("blkUnitaVitate.blkCCIAAUnitaVitate");
        htmpl.newBlock("blkUnitaVitate.blkUnitaArboreeDettaglioAlbo");
      }
      if (elencoParticelleArboree != null && elencoParticelleArboree.length > 0)
      {
        htmpl.newBlock("blkUnitaVitate.blkUnitaArboreeDettaglio");
        
        //Isole e parcelle - GIS 
        // Visualizzo solo il blocco se il parametro su DB ABIS è uguale ad 'S'
        String parametroAbis = null;
        try {
          parametroAbis = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_ABIS);;
        }
        catch(SolmrException se) {
        }
        
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_ISOLEPARCELLE"))
        {
          if((parametroAbis != null) && parametroAbis.equalsIgnoreCase("S") )
          {
            htmpl.newBlock("blkUnitaVitate.blkAbacoIsole");
          }
        }
        
        
        if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_GIS_AZIENDALE"))
        {
          htmpl.newBlock("blkUnitaVitate.blkGisAziendale");
        }
        
        
        
        //Il controllo sul provinciale vien fatto sulla conferma 
        //nella jsp
        if (iride2AbilitazioniVO.isUtenteAbilitato("ALLINEA_UV_A_ELEGGIBILITA"))
        {
          htmpl.newBlock("blkUnitaVitate.blkAllineaCompConsoUnitaArboree");
          if(anagAziendaVO != null)
          {
            try 
            {
              CompensazioneAziendaVO compensazioneAziendaVO = gaaFacadeClient.getCompensazioneAzienda(anagAziendaVO.getIdAzienda().longValue());
              //se la data consolidamento già è valorizzata non devo far vedere il gruppo di label
              if(!(Validator.isNotEmpty(compensazioneAziendaVO) 
                  && (compensazioneAziendaVO.getDataConsolidamentoGis() != null)))
              {                
                htmpl.newBlock("blkUnitaVitate.blkAllineaGisUnitaArboree");                
              }
            }
            catch(SolmrException se)
            {}
            catch(Exception se)
            {}
          }
        }
       
        //Visualizzabile solo al pianoin lavorazione
        if (super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)
            && (elencoUte != null) && (elencoUte.size() > 0)
            && (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento() !=null)
            && (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().compareTo(new Long(-1)) == 0))
        {
          htmpl.newBlock("blkUnitaVitate.blkRibaltaUnitaArboree");
        }
      }
      if (super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)
          && elencoUte != null && elencoUte.size() > 0)
      {
        if (elencoUnitaArboreeImportabili != null
            && elencoUnitaArboreeImportabili.length > 0)
        {
          htmpl.newBlock("blkUnitaArboree");
        }
        
        //htmpl.newBlock("blkUnitaVitate.blkInserisciImportUnitaArboree");
      }
      // Deroga relativa alle unità vitate
      if (ruoloUtenza.isReadWrite())
      {
        if (anagAziendaVO.getDataCessazione() == null
            && anagAziendaVO.getDataCessazioneStr() == null
            && anagAziendaVO.getDataFineVal() == null)
        {
          //Controllo se almeno una particella dell'azienda è della stessa
          //provincia nel caso il ruolo dell'utente sia provinciale
          /*boolean flagAlmenoUnaProvincia = false;
          try 
          {
            if(ruoloUtenza.isUtenteProvinciale() &&
                (ruoloUtenza.getCodiceEnte() != null))
            {
              String[] provinceParticelle = anagFacadeClient
                .getIstatProvFromConduzione(anagAziendaVO.getIdAzienda().longValue());
              if(provinceParticelle != null)
              {
                for(int i=0;i<provinceParticelle.length;i++)
                {
                  if(ruoloUtenza.getCodiceEnte().equalsIgnoreCase(provinceParticelle[i]))
                  {
                    flagAlmenoUnaProvincia = true;
                    break;
                  }
                }
              }
            }
          }
          catch(SolmrException se) 
          {}*/
          
          //Query per estrarre le provincie delle unità arboree
          if (ruoloUtenza.isUtenteRegionale()
              || ruoloUtenza.isUtenteProvinciale()
              || ruoloUtenza.isUtenteIntermediario()
              || ruoloUtenza.isUtenteOPRGestore())
          {
            htmpl.newBlock("blkUnitaVitate.blkInserisciUnitaArboree");
          }
          //Nuova versione unita arboree
          //per cessazione validazione e modifica cotrollo se 
          //l'unità vitata fa parte della provincia dell'utente provinciale PA.
          //isFlagProvinciaCompetenza == true significa che esite almeno
          //una unità vitata facente parte della stessa provincia del PA provinciale.  
          if (ruoloUtenza.isUtenteRegionale()
              || ruoloUtenza.isUtenteProvinciale()
                  //&& filtriUnitaArboreaRicercaVO.isFlagProvinciaCompetenza())
              || ruoloUtenza.isUtenteIntermediario()
              || ruoloUtenza.isUtenteOPRGestore())
          {
            if (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0
                && elencoUte != null
                && elencoUte.size() > 0
                && elencoParticelleArboree != null
                && elencoParticelleArboree.length > 0)
            {
              htmpl.newBlock("blkUnitaVitate.blkCessaUnitaArboree");
              htmpl.newBlock("blkUnitaVitate.blkModificaUnitaArboree");
              htmpl.newBlock("blkUnitaVitate.blkDuplicaUnitaArboree");
              htmpl.newBlock("blkUnitaVitate.blkAssociaPerUsoEleggUnitaArboree");
            }
          }
          
          //Nuova versione unita arboree
          //per cessazione validazione e modifica cotrollo se 
          //l'unità vitata fa parte della provincia dell'utente provinciale PA
          //isFlagProvinciaCompetenza == true significa che esite almeno
          //una unità vitata facente parte della stessa provincia del PA provinciale.  
          if ((ruoloUtenza.isUtenteRegionale() 
              || ruoloUtenza.isUtenteProvinciale())
                 // && filtriUnitaArboreaRicercaVO.isFlagProvinciaCompetenza()))
              && (elencoParticelleArboree != null && elencoParticelleArboree.length > 0))
          {
            htmpl.newBlock("blkUnitaVitate.blkValidaUnitaArboree");
          }
          
          //Catasto Viticolo Regional - GIS 
          if (ruoloUtenza.isUtenteRegionale()
              || (ruoloUtenza.isUtenteProvinciale()))
          {
            
            // Visualizzo solo il blocco se il parametro su DB ABCV è uguale ad 'S'
            String parametroAbcv = null;
            try 
            {
              parametroAbcv = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_ABCV);;
            }
            catch(SolmrException se) {
            }
            
            if((parametroAbcv != null) && parametroAbcv.equalsIgnoreCase("S"))
            {
              htmpl.newBlock("blkUnitaVitate.blkCatVitReg");
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * Esegue i controlli sulla competenza di dato per l'utente in questione.
   * 
   */
  public String hasCompetenzaDato(HttpServletRequest request,
      HttpServletResponse response, RuoloUtenza ruoloUtenza,
      AnagFacadeClient anagFacadeClient)
  {
    //String iridePageNameForCU = (String) request
        //.getAttribute("iridePageNameForCU");

    // Se sono nella pagina di popup popupVariazioniParticellaCtrl.jsp o
    // popParticelleCtrl.jsp ed hoappena
    // fatto una ricerca potrei non avere anagaziendaVO in sessione, quindi non
    // non faccio i controlli sulla competenza del dato
    /*if ("popupVariazioniParticellaCtrl.jsp".equals(iridePageNameForCU)
        || "popParticelleCtrl.jsp".equals(iridePageNameForCU))
      return null;*/
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO"); 
    if (anagAziendaVO == null)
    {
      SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
      return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
    }
    return validateGenericUser(ruoloUtenza, anagAziendaVO,
        anagFacadeClient, request);
  }

}
