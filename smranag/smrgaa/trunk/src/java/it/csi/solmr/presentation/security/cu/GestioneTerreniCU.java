package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.anag.sian.SianTerritorioVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GestioneTerreniCU extends AutorizzazioneCUStandard
{
  /**
   * 
   */
  private static final long serialVersionUID = -8676879575417451270L;

  public GestioneTerreniCU()
  {
    this.cuName = "GESTIONE_TERRENI";
    this.isCUForReadWrite = true;
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
    FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO) request
        .getSession().getAttribute("filtriParticellareRicercaVO");
    SianTerritorioVO[] sianTerritorioVO = (SianTerritorioVO[]) request
        .getSession().getAttribute("sianTerritorioVO");
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    Vector<UteVO> elencoUte = null;

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

    super.writeBanner(htmpl, request);
    super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
    super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
    Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
    if (iride2AbilitazioniVO.isUtenteAbilitato("VISUALIZZA_SIGMATER"))
        htmpl.newBlock("blkTerreni.blkSigmater");
    if (filtriParticellareRicercaVO != null)
    {
      if (elencoUte != null && elencoUte.size() > 0 && anagAziendaVO != null
          && super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO))
      {
        htmpl.newBlock("blkTerreni.blkInserisci");
        
        if (iride2AbilitazioniVO.isUtenteAbilitato("IMPORTA_TERRENI"))
          htmpl.newBlock("blkTerreni.blkImportaParticelle");
        
        if (sianTerritorioVO != null && sianTerritorioVO.length > 0)
        {
          htmpl.newBlock("blkImportaParticelleSIAN");
        }
      }
      if (sianTerritorioVO != null && sianTerritorioVO.length > 0)
      {
        htmpl.newBlock("blkScaricoExcelSIAN");
      }
    }
    
    
    return null;
  }

  /**
   * Esegue i controlli sulla competenza di dato per l'utente in questione.
   */
  public String hasCompetenzaDato(HttpServletRequest request,
      HttpServletResponse response, RuoloUtenza ruoloUtenza,
      AnagFacadeClient anagFacadeClient)
  {
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO");
    String iridePageNameForCU = (String) request
        .getAttribute("iridePageNameForCU");
    
    
    //Se sono nell'inserimento delle unità vitate (pagine comune sia a terreni che uv CU lasciato ai terreni)
    //Non faccio nessun controllo. Serve per poter al provinciale inserire
    //uv a una azienda di diversa provincia dalla sua ma con particelle della stessa provincia della sua
    if ("popCercaParticelleInsUnarCtrl.jsp".equals(iridePageNameForCU))
      return null;
    
    
    
    
    if (anagAziendaVO == null)
    {
      SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
      return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
    }
    
    
    return validateGenericUser(ruoloUtenza, anagAziendaVO,
        anagFacadeClient, request);
  }

}
