package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
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

public class AllineaUVAEleggibilitaCU extends AutorizzazioneCUStandard
{
  

  /**
   * 
   */
  private static final long serialVersionUID = -2476640875654527596L;
  
  
  

  public AllineaUVAEleggibilitaCU()
  {
    this.cuName = "ALLINEA_UV_A_ELEGGIBILITA";
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

    super.writeBanner(htmpl, request);
    super.writeBarraDatiAzienda(anagAziendaVO, htmpl);
    super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
    
    
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
    //Isole e parcelle - GIS 
    // Visualizzo solo il blocco se il parametro su DB ABCV è uguale ad 'S'
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
        htmpl.newBlock("blkAbacoIsole");
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
    //String iridePageNameForCU = (String) request
        //.getAttribute("iridePageNameForCU");
    
    
    
    //Se valorizzato o cliccato sul conferma nella jsp
    String[] idIsolaParcella = request.getParameterValues("idIsolaParcella");
    
    if (anagAziendaVO == null)
    {
      SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
      return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
    }
    
    boolean hasProperties = false;
    if (ruoloUtenza.isReadWrite())
    {
      if (anagAziendaVO.getDataCessazione() == null
          && anagAziendaVO.getDataCessazioneStr() == null
          && anagAziendaVO.getDataFineVal() == null)
      {
        if (ruoloUtenza.isUtenteRegionale()
            || ruoloUtenza.isUtenteIntermediario()
            || ruoloUtenza.isUtenteOPRGestore())
        {
          hasProperties = true;
        }
        else if(ruoloUtenza.isUtenteProvinciale()
            && (ruoloUtenza.getCodiceEnte() != null))
        {
          if(idIsolaParcella !=null)
          {
            long[] idIsolaParcellaL = new long[idIsolaParcella.length];
            for(int i=0;i<idIsolaParcella.length;i++)
            {
              idIsolaParcellaL[i] = new Long(idIsolaParcella[i]).longValue(); 
            }
            //elenco dei codici istat provincia dei comuni delle unitavitate
            Vector<String> vCodIstat = null;
            try
            {
              vCodIstat = anagFacadeClient.findProvinciaStoricoParticellaArboreaIsolaParcella(anagAziendaVO.getIdAzienda().longValue(),idIsolaParcellaL);
            }
            catch(SolmrException sex)
            {}
            if(vCodIstat !=null)
            {
              //Se esistono più codici istat significa che l'utente col ruolo provinciale ha
              //scelto unità vitate appartenenti a province diverse e dato che lui ha diritti di modifica solo
              //sulla propria provincia non può procedere
              if(vCodIstat.size() > 1)
              {
                return AnagErrors.ERRORE_UTENTE_PROVINCIALE_NON_AUTORIZZATO +ruoloUtenza.getProvincia();
              }
              //Se le unità vitate appartengono tutte alla stessa provincia per avere accesso tale provincia deve
              //corrispondere all'utente provinciale...
              else if(vCodIstat.size() == 1)
              {
                String istatProv = (String)vCodIstat.get(0); 
                if(ruoloUtenza.getCodiceEnte().equals(istatProv))
                {
                  hasProperties = true;
                }
              }
            }
          }
          else //premo su annulla (torno alla pagina precedente) o conferma quindi non ho in request idStoricoUnitaArborea
          {              
            hasProperties = true;
          }
        }
      }
      
      if (!hasProperties)
      {
        SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": hasProperties false riga 155");
        return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
      }
      else
      {
        return null;
      }
    }
    else
    {
      return validateGenericUser(ruoloUtenza, anagAziendaVO,
          anagFacadeClient, request);
    }
    
  }

}
