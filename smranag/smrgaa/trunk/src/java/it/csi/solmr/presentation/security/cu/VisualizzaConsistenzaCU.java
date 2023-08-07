package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.presentation.security.Autorizzazione;
import it.csi.solmr.util.SolmrLogger;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaConsistenzaCU extends Autorizzazione
{

  /**
   * 
   */
  private static final long serialVersionUID = -3747700715948045142L;

  public VisualizzaConsistenzaCU()
  {
    this.cuName = "VISUALIZZA_CONSISTENZA";
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
 
  @SuppressWarnings("rawtypes")
  public String writeMenu(Htmpl htmpl, HttpServletRequest request)
  {
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO");
    RuoloUtenza ruoloUtenza = (RuoloUtenza)request.getSession().getAttribute("ruoloUtenza");
    Object objForMenu = (Vector) request
        .getAttribute("dichiarazioniConsistenza");
    
    Iride2AbilitazioniVO iride2AbilitazioniVO =(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");

    super.writeBanner(htmpl, request);
    super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
    super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
    if (anagAziendaVO != null && !anagAziendaVO.isFlagAziendaProvvisoria())
    {
      if (objForMenu != null)
      {
        if (((Vector) objForMenu).size() > 0)
        {
          htmpl.newBlock("blkDichiarazioniConsistenza.blkDettaglio");
          if (super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO))
          {
            htmpl.newBlock("blkDichiarazioniConsistenza.blkEliminaConsistenza");            
            htmpl.newBlock("blkDichiarazioniConsistenza.blkProtocollaDichiarazione");
            htmpl.newBlock("blkDichiarazioniConsistenza.blkModificaDichiarazione");
            htmpl.newBlock("blkDichiarazioniConsistenza.blkRipristinaConsistenza");
            htmpl.newBlock("blkDichiarazioniConsistenza.blkDichConsAllegati");
          }
          
          
          if (iride2AbilitazioniVO.isUtenteAbilitato("INVIO_FASCICOLI"))
          {            
            htmpl.newBlock("blkDichiarazioniConsistenza.blkInvioFascicoli");
          }
          
        }
        if (super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO))
        {
          htmpl.newBlock("blkDichiarazioniConsistenza.blkConsistenzaNuova");
        }
        
        
        //Verifica Consistenza
        if (iride2AbilitazioniVO.isUtenteAbilitato("VERIFICA_CONSISTENZA"))
        {
                   
          if (anagAziendaVO.getDataCessazione() == null
              && anagAziendaVO.getDataCessazioneStr() == null
              && anagAziendaVO.getDataFineVal() == null)
          {
            htmpl.newBlock("blkDichiarazioniConsistenza.blkConsistenzaVerifica");
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
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO");
    if (anagAziendaVO == null)
    {
      SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO
          + ": anagAziendaVO null");
      return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
    }
    return validateGenericUser(ruoloUtenza, anagAziendaVO,
        anagFacadeClient, request);
  }
}
