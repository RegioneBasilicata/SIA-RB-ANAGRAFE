package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaIsoleParcelleCU extends AutorizzazioneCUStandard
{
  

  /**
   * 
   */
  private static final long serialVersionUID = 708241036729097955L;

  public VisualizzaIsoleParcelleCU()
  {
    this.cuName = "VISUALIZZA_ISOLEPARCELLE";
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

    super.writeBanner(htmpl, request);
    super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
    super.writeBarraStato(sex, htmpl);

    
    // Gestione delle voci di men� di anagrafe
    super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
    
    String arrivoDa = request.getParameter("arrivoDa");
    if("terreni".equalsIgnoreCase(arrivoDa))
    {
      htmpl.newBlock("blkTerreni.blkOnTerreni");
      htmpl.newBlock("blkUnitaVitate.blkOffUnitaVitate");
    }
    else
    {
      htmpl.newBlock("blkTerreni.blkOffTerreni");
      htmpl.newBlock("blkUnitaVitate.blkOnUnitaVitate");
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
