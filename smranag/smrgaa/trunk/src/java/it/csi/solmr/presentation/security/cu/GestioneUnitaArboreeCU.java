package it.csi.solmr.presentation.security.cu;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.presentation.security.AutorizzazioneCUStandard;
import it.csi.solmr.util.SolmrLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GestioneUnitaArboreeCU extends AutorizzazioneCUStandard
{
  /**
   * 
   */
  private static final long serialVersionUID = 5928020522299445747L;

  public GestioneUnitaArboreeCU()
  {
    this.cuName = "GESTIONE_UNITA_ARBOREE";
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
    
    
    if (ruoloUtenza.isUtenteRegionale() || ruoloUtenza.isUtenteProvinciale())
    {
      htmpl.set("blkUnitaVitate.nomeFunzionalita", "valida");
      htmpl.set("blkUnitaVitate.nomeFunzionalitaMul", "valida");
    }
    else
    {
      htmpl.set("blkUnitaVitate.nomeFunzionalita", "modifica");
      htmpl.set("blkUnitaVitate.nomeFunzionalitaMul", "modifica multipla");
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
    String iridePageNameForCU = (String) request
        .getAttribute("iridePageNameForCU");
    
    //String[] idStoricoUnitaArborea = request.getParameterValues("idUnita");
    
    if (anagAziendaVO == null)
    {
      SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": anagAziendaVO null");
      return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
    }
    // Gestione della deroga per le unità vitate
    boolean hasProperties = false;
    if ("unitaArboreeInserisciCtrl.jsp".equals(iridePageNameForCU)
        || "popCercaParticelleInsUnarCtrl.jsp".equals(iridePageNameForCU)       
        || "popSchedarioViticoloImportCtrl.jsp".equals(iridePageNameForCU))        
    {
      if (ruoloUtenza.isReadWrite())
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
        
        
        
        
        if (anagAziendaVO.getDataCessazione() == null
            && anagAziendaVO.getDataCessazioneStr() == null
            && anagAziendaVO.getDataFineVal() == null)
        {
          if (ruoloUtenza.isUtenteRegionale()
              || ruoloUtenza.isUtenteProvinciale()
              || ruoloUtenza.isUtenteIntermediario()
              || ruoloUtenza.isUtenteOPRGestore())
          {
            hasProperties = true;
          }
        }
      }
      if (!hasProperties)
      {
        SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": hasProperties false riga 159");
        return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
      }
      else
      {
        return null;
      }
    }
    //Nuova versione unita arboree
    //per cessazione validazione e modifica cotrollo se 
    //l'unità vitata fa parte della provincia dell'utente provinciale se PA Provinciale
    //****************************************
    //Il doppio controllo qui e nel VisualizzaTerreniCU è fatto perchè può accadere
    //che lavorino in parallelo l'utente PA (provinciale/regionale) e ad es. un'intermediario
    else if ("popUnitaArboreeModificaCtrl.jsp".equals(iridePageNameForCU)
        || "unitaArboreeModificaCtrl.jsp".equals(iridePageNameForCU)
        || "unitaArboreeModificaMultiplaCtrl.jsp".equals(iridePageNameForCU)
        || "unitaArboreeEliminaCtrl.jsp".equals(iridePageNameForCU)
        || "unitaArboreeAssociaPercUsoEleggCtrl.jsp".equals(iridePageNameForCU)
        || "sceltaAllineaPercUsoEleggCtrl.jsp".equals(iridePageNameForCU)
        || "confermaAllineaPercUsoEleggCtrl.jsp".equals(iridePageNameForCU))
    {
      if (ruoloUtenza.isReadWrite())
      {
        if (anagAziendaVO.getDataCessazione() == null
            && anagAziendaVO.getDataCessazioneStr() == null
            && anagAziendaVO.getDataFineVal() == null)
        {
          if (ruoloUtenza.isUtenteRegionale()
              || ruoloUtenza.isUtenteProvinciale()
              || ruoloUtenza.isUtenteIntermediario()
              || ruoloUtenza.isUtenteOPRGestore())
          {
            hasProperties = true;
          }
          /*else if(ruoloUtenza.isUtenteProvinciale()
              && (ruoloUtenza.getCodiceEnte() != null))
          {
            if(idStoricoUnitaArborea !=null)
            {
              //idStoricoUnitàarborea delle unità vitate selezionate nell'elenco
              long[] idStoricoUnitaArboreaL = new long[idStoricoUnitaArborea.length];
              for(int i=0;i<idStoricoUnitaArborea.length;i++)
              {
                idStoricoUnitaArboreaL[i] = new Long(idStoricoUnitaArborea[i]).longValue(); 
              }
              //elenco dei codici istat provincia dei comuni delle unitavitate
              Vector<String> vCodIstat = null;
              try
              {
                vCodIstat = anagFacadeClient.findProvinciaStoricoParticellaArborea(idStoricoUnitaArboreaL);
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
          }*/
        }
      }
      if (!hasProperties)
      {
        SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": hasProperties false riga 238");
        return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
      }
      else
      {
        return null;
      }
    }
    else if ("unitaArboreeValidaCtrl.jsp".equalsIgnoreCase(iridePageNameForCU))
    {
      if (ruoloUtenza.isReadWrite())
      {
        if (anagAziendaVO.getDataCessazione() == null
            && anagAziendaVO.getDataCessazioneStr() == null
            && anagAziendaVO.getDataFineVal() == null)
        {
          if(ruoloUtenza.isUtenteRegionale()
            || ruoloUtenza.isUtenteProvinciale())
          {
            hasProperties = true;
          }
          /*else if(ruoloUtenza.isUtenteProvinciale()
              && (ruoloUtenza.getCodiceEnte() != null))
          {
            if(idStoricoUnitaArborea !=null)
            {
              //idUnitaArboreaDichiarata delle unità vitate selezionate nell'elenco
              long[] idUnitaArboreaDichiarataL = new long[idStoricoUnitaArborea.length];
              for(int i=0;i<idStoricoUnitaArborea.length;i++)
              {
                idUnitaArboreaDichiarataL[i] = new Long(idStoricoUnitaArborea[i]).longValue(); 
              }
              Vector<String> vCodIstat = null;
              try
              {
                vCodIstat = anagFacadeClient.findProvinciaParticellaArboreaDichiarata(idUnitaArboreaDichiarataL);
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
            else //premo su annulla o valida
            {              
              hasProperties = true;
            }
          }*/
        }        
      }
      if (!hasProperties)
      {
        SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": hasProperties false riga 306");
        return AnagErrors.ERRORE_AUT_NON_AUTORIZZATO;
      }
      else
      {
        return null;
      }
    }
    else if ("abacoCatVitRegCtrl.jsp".equalsIgnoreCase(iridePageNameForCU))
    {
      if (ruoloUtenza.isReadWrite())
      {
        if (anagAziendaVO.getDataCessazione() == null
            && anagAziendaVO.getDataCessazioneStr() == null
            && anagAziendaVO.getDataFineVal() == null)
        {
          if (ruoloUtenza.isUtenteRegionale())
          {
            hasProperties = true;
          }
          else if(ruoloUtenza.isUtenteProvinciale()
              && (ruoloUtenza.getCodiceEnte() != null))
          {
            hasProperties = true;
          }
        }
      }
      
      if (!hasProperties)
      {
        SolmrLogger.debug(this, AnagErrors.ERRORE_AUT_NON_AUTORIZZATO+": hasProperties false riga 336");
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
