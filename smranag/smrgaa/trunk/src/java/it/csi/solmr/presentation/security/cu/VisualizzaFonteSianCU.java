package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.sian.SianFascicoloResponseVO;
import it.csi.solmr.dto.anag.sian.SianTrovaFascicoloVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisualizzaFonteSianCU extends AutorizzazioneCUStandard
{
  

  /**
   * 
   */
  private static final long serialVersionUID = -5197611068106579047L;


  public VisualizzaFonteSianCU()
  {
    this.cuName = "VISUALIZZA_FONTE_SIAN";
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
    SianFascicoloResponseVO sianFascicoloResponseVO = (SianFascicoloResponseVO) request.getSession()
        .getAttribute("sianFascicoloResponseVO");
    SianTrovaFascicoloVO sianTrovaFascicoloVO = null;
    if(sianFascicoloResponseVO != null)
      sianTrovaFascicoloVO = (SianTrovaFascicoloVO)sianFascicoloResponseVO.getContenuto();
    Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
    //ManodoperaVO manodoperaVO = (ManodoperaVO)request.getAttribute("manodoperaVO");
    
    super.writeBanner(htmpl, request);
    super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
    super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, null,
        anagAziendaVO, null, request);


    if (iride2AbilitazioniVO.isUtenteAbilitato("IMPORTA_FONTE_SIAN"))
    {
      if (ruoloUtenza != null && ruoloUtenza.isValid() && anagAziendaVO != null
          && !Validator.isNotEmpty(anagAziendaVO.getDataFineVal()))
      {
        if (sianTrovaFascicoloVO != null
            && super.hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO))
        {
        	scriviCheckBox(htmpl, request);
        }
        else
        {
      	  //Questa codice pemette di Visualizzare il check per i profili Arpea e Regione
      	  //anche se l'azienda presenta una delega
      	  if (sianTrovaFascicoloVO != null)
      	  {
      		  //CUAA Visualizzare il check solo per i profili Arpea e Regione
  	        if (ruoloUtenza.isUtenteOPRGestore() || ruoloUtenza.isUtenteRegionale())
  	        {
  	          scriviCheckBox(htmpl, request);
  	        }
      	  }
        }
      }
    }
    return null;

  }
  
  
  private void scriviCheckBox(Htmpl htmpl, HttpServletRequest request)
  {
    String numIscrInpsTributariaAtt = (String)request.getAttribute("numIscrInpsTributariaAtt");
    String tipoIscrInpsTributariaAtt = (String)request.getAttribute("tipoIscrInpsTributariaAtt");
    String datInInpsTributariaAtt = (String)request.getAttribute("datInInpsTributariaAtt");
    String datFinInpsTributariaAtt = (String)request.getAttribute("datFinInpsTributariaAtt");
    
    String numIscrRITributariaAtt = (String)request.getAttribute("numIscrRITributariaAtt");
    String datInRITributariaAtt = (String)request.getAttribute("datInRITributariaAtt");
    String datFinRITributariaAtt = (String)request.getAttribute("datFinRITributariaAtt");
    
    String numIscrREATributariaAtt = (String)request.getAttribute("numIscrREATributariaAtt");
    String datInREATributariaAtt = (String)request.getAttribute("datInREATributariaAtt");
    String datFinREATributariaAtt = (String)request.getAttribute("datFinREATributariaAtt");
    
    
    htmpl.newBlock("blkImporta");
    htmpl.newBlock("blkImportaRadio");
      
      
    if(Validator.isEmpty(numIscrInpsTributariaAtt))
      htmpl.newBlock("blkNoNumIscrInps");
    else
      htmpl.newBlock("blkImpNumIscrInps");
    
    if(Validator.isEmpty(tipoIscrInpsTributariaAtt))
      htmpl.newBlock("blkNoTipoIscrInps");
    else
      htmpl.newBlock("blkImpTipoIscrInps");
    
    if(Validator.isEmpty(datInInpsTributariaAtt))
      htmpl.newBlock("blkNoDatInInps");
    else
      htmpl.newBlock("blkImpDatInInps");
    
    if(Validator.isEmpty(datFinInpsTributariaAtt))
      htmpl.newBlock("blkNoDatFinInps");
    else
      htmpl.newBlock("blkImpDatFinInps");
    
    
    
    if(Validator.isEmpty(numIscrRITributariaAtt))
      htmpl.newBlock("blkNoNumIscrRI");
    else
      htmpl.newBlock("blkImpNumIscrRI");
    
    if(Validator.isEmpty(datInRITributariaAtt))
      htmpl.newBlock("blkNoDatInRI");
    else
      htmpl.newBlock("blkImpDatInRI");
    
    if(Validator.isEmpty(datFinRITributariaAtt))
      htmpl.newBlock("blkNoDatFinRI");
    else
      htmpl.newBlock("blkImpDatFinRI");
    
    
    if(Validator.isEmpty(numIscrREATributariaAtt))
      htmpl.newBlock("blkNoNumIscrREA");
    else
      htmpl.newBlock("blkImpNumIscrREA");
    
    if(Validator.isEmpty(datInREATributariaAtt))
      htmpl.newBlock("blkNoDatInREA");
    else
      htmpl.newBlock("blkImpDatInREA");
    
    if(Validator.isEmpty(datFinREATributariaAtt))
      htmpl.newBlock("blkNoDatFinREA");
    else
      htmpl.newBlock("blkImpDatFinREA");
      
      
    
      
    
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
    String CUAAselezionato = request.getParameter("CUAAselezionato");
    if (anagAziendaVO == null)
    {
      SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO
          + ": anagAziendaVO null");
      return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
    }
    else
    {
      // Ricerco nuovamente i dati dell'azienda perchè, per impedire l'accesso
      // alla funzionalità, voglio essere sicuro di lavorare
      // sui dati dell'azienda corrispondenti al momento esatto dell'accesso
      // dell'utente alla funzionalità.
      try
      {
        anagAziendaVO = anagFacadeClient.findAziendaAttiva(anagAziendaVO
            .getIdAzienda());
        request.getSession().setAttribute("anagAziendaVO", anagAziendaVO);
        if (!Validator.isNotEmpty(anagAziendaVO.getCUAA())
            && !Validator.isNotEmpty(CUAAselezionato))
        {
          return AnagErrors.ERRORE_CUAA_SIAN_NON_VALORIZZATO;
        }
      }
      catch (SolmrException se)
      {
        return AnagErrors.ERRORE_DI_SISTEMA_AZIENDA_NON_REPERITA;
      }
    }
    return validateGenericUser(ruoloUtenza, anagAziendaVO,
        anagFacadeClient, request);
  }

}
