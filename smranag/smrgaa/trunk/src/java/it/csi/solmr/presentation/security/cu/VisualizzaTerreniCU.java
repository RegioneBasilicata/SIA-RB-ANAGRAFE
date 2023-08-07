package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.anag.sian.SianTerritorioVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaTerreniCU extends AutorizzazioneCUStandard
{
  private static final long serialVersionUID = -2425975587394864051L;

  public VisualizzaTerreniCU()
  {
    this.cuName = "VISUALIZZA_TERRENI";
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
    @SuppressWarnings("rawtypes")
    Vector elencoParticelle = (Vector) request.getAttribute("elencoParticelle");
    FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO) request
        .getSession().getAttribute("filtriParticellareRicercaVO");
    SianTerritorioVO[] sianTerritorioVO = (SianTerritorioVO[]) request
        .getSession().getAttribute("sianTerritorioVO");

    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
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
    if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_SIGMATER"))
        htmpl.newBlock("blkTerreni.blkSigmater");
    
    
    if (iride2AbilitazioniVO.isUtenteAbilitato("ALLINEA_UTILIZZO_A_ELEGGIBILITA")
        && anagAziendaVO != null 
        && super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO))
      htmpl.newBlock("blkTerreni.blkAllineaUtilizzoAEleggibilita");
    
    
    if (filtriParticellareRicercaVO != null)
    {
      
      //in questo caso nell'elenco elimino alcune colonne perchè sono col ruolo azienda
      if (iride2AbilitazioniVO.isUtenteAbilitato("AZIENDE_TITOLARE"))
      {
        request.setAttribute("elencoRidotto", "ok");
      }
      
      
      
      
      
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
          htmpl.newBlock("blkTerreni.blkAbacoIsole");
        }
      }
      
      //Abaco 3D - GIS 
      // Visualizzo solo il blocco se il parametro su DB ABCV è uguale ad 'S'
      String parametroAb3D = null;
      try 
      {
        parametroAb3D = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_AB3D);;
      }
      catch(SolmrException se) {
      }
      
      if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_GIS_AZIENDALE"))
      {
        htmpl.newBlock("blkTerreni.blkGisAziendale");
      }
      
      if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_GIS3D"))
      {
        if((parametroAb3D != null) && parametroAb3D.equalsIgnoreCase("S") )
        {
          htmpl.newBlock("blkTerreni.blkAbaco3D");
        }
      }
      
      
      
      // Gestione specifica delle voci di menù relative ai terreni
      if (elencoParticelle != null && elencoParticelle.size() > 0)
      {
        
        
        if (iride2AbilitazioniVO.isUtenteAbilitato("BROGLIACCIO"))
        {
          request.getSession().setAttribute("brogliaccio","true");
        }
        
        
        htmpl.newBlock("blkTerreni.blkDettaglio");
        if (elencoUte != null
            && elencoUte.size() > 0
            && anagAziendaVO != null
            && super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)
            && filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0)
        {
          htmpl.newBlock("blkTerreni.blkModificaMultipla");
          htmpl.newBlock("blkTerreni.blkModifica");
          htmpl.newBlock("blkTerreni.blkAllineaPercentuale");
        }
        if (anagAziendaVO != null
            && super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)
            && filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0)
          htmpl.newBlock("blkTerreni.blkElimina");
        
        
        //Nuova gestione macroCU
        if (iride2AbilitazioniVO.isUtenteAbilitato("PIANO_GRAFICO"))
        {
          @SuppressWarnings("unchecked")
          Vector<String> vActor = (Vector<String>)request.getSession().getAttribute("vActor");
          if (anagAziendaVO != null
              && super.hasPropertiesToModifyAnagrafeNew(ruoloUtenza, anagAziendaVO, vActor))
          {
            htmpl.newBlock("blkTerreni.blkPianoGrafico");
          }
        }
        
        
      }
      if (elencoUte != null && elencoUte.size() > 0 && anagAziendaVO != null
          && super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO))
      {
        htmpl.newBlock("blkTerreni.blkInserisci");
        
        if (iride2AbilitazioniVO.isUtenteAbilitato("IMPORTA_TERRENI"))
          htmpl.newBlock("blkTerreni.blkImportaParticelle");
        
        
        if (sianTerritorioVO != null && sianTerritorioVO.length > 0)
          htmpl.newBlock("blkImportaParticelleSIAN");
      }
      // Aggiunta per nuovo utente PALocale
      if (!ruoloUtenza.isUtentePALocale() && !ruoloUtenza.isUtentePALocaleSuper())
      {
        if (sianTerritorioVO != null && sianTerritorioVO.length > 0)
          htmpl.newBlock("blkScaricoExcelSIAN");
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
    String iridePageNameForCU = (String) request
        .getAttribute("iridePageNameForCU");

    // Se sono nella pagina di popup popupVariazioniParticellaCtrl.jsp o
    // popParticelleCtrl.jsp ed hoappena
    // fatto una ricerca potrei non avere anagaziendaVO in sessione, quindi non
    // non faccio i controlli sulla competenza del dato
    if ("popupVariazioniParticellaCtrl.jsp".equals(iridePageNameForCU)
        || "popParticelleCtrl.jsp".equals(iridePageNameForCU)
        || "popVariazioniEleggibilitaCtrl.jsp".equals(iridePageNameForCU) )
      return null;
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
