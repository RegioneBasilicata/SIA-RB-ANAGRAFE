package it.csi.solmr.presentation.security;



import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.iride2.Iride2AbilitazioniVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.Validator;

import java.lang.reflect.Method;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe per la gestione delle autorizzazioni dei CU "standard"
 *
 * <p>Title: SMRGAA</p>
 *
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: CSI - PIEMONTE</p>
 *
 * @author Mauro Vocale
 * @version 1.0
 */

public abstract class AutorizzazioneCUStandard extends Autorizzazione {


  /**
   * 
   */
  private static final long serialVersionUID = 2959544032991395452L;
  

  /**
   * Metodo che si occupa di far visualizzare tutte le funzionalità specifiche
   * dei casi d'uso che presentano le funzionalità standard di modifica,
   * elimina, cessazione
   * 
   * @param ruoloUtenza
   * @param anagAziendaVO
   * @param htmpl
   * @param nomeBlocco
   * @param obj
   * @param metodoCausaleAutorizzazione
   * @param request
   */
  @SuppressWarnings("rawtypes")
  public void writeGenericMenu(RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO, Htmpl htmpl, String nomeBlocco, Object obj, String metodoCausaleAutorizzazione, HttpServletRequest request) {
    super.writeGenericMenu(ruoloUtenza, anagAziendaVO, htmpl, request);
    // Verifico che il profilo ci sia e sia corretto
    if(ruoloUtenza != null && ruoloUtenza.isValid()) 
    {
      // Controllo che l'azienda ci sia e che l'utente non stia visualizzando un record storicizzato
      if(anagAziendaVO != null && !Validator.isNotEmpty(anagAziendaVO.getDataFineVal())) 
      {
        if(obj != null) 
        {
          //livello introdotto per le aziende collegate
          //solo se l'azienda è la root faccio vedere modifica
          Long level = (Long)request.getSession().getAttribute("levelAziendaCollegata");
          if(level !=null)
          {
            if(level.compareTo(new Long(1)) == 0) //primo livello
            {
              //htmpl.newBlock("linkModificaLivelloRoot");
              // Visualizzo il blocco solo se trattasi di aziende associate ed ha un padre!
              if(anagAziendaVO.isFlagEnteAppartenenza())
              {
                htmpl.newBlock("blkAziendeCollegate.linkEnteAppartenenza");
              }
            }
          }
          if(obj instanceof Vector) 
          {
            if(((Vector)obj).size() > 0) 
            {
              htmpl.newBlock(nomeBlocco + ".blkDettaglio");
              if(hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)) 
              {
                htmpl.newBlock(nomeBlocco + ".blkModifica");
                htmpl.newBlock(nomeBlocco + ".blkCessazione");
                htmpl.newBlock(nomeBlocco + ".blkElimina");
              }
            }
          }
          else if(obj instanceof Object[]) 
          {
        	  if(((Object[])obj).length > 0) 
        	  {
        		  htmpl.newBlock(nomeBlocco + ".blkDettaglio");
              if(hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)) 
              {
            	  htmpl.newBlock(nomeBlocco + ".blkModifica");
            	  htmpl.newBlock(nomeBlocco + ".blkCessazione");
            	  htmpl.newBlock(nomeBlocco + ".blkElimina");
              }
            }
          }
          else 
          {
            htmpl.newBlock(nomeBlocco + ".blkDettaglio");
            
            
            //Nuova gestione revoca delega del 01/09/2011
            Iride2AbilitazioniVO iride2AbilitazioniVO=(Iride2AbilitazioniVO) request.getSession().getAttribute("iride2AbilitazioniVO");
            if(iride2AbilitazioniVO.isUtenteAbilitato("REVOCA_DELEGA"))
            {
              //Controllo che l'azienda sia ancora attiva e abbia una delega attiva
              if(anagAziendaVO.getDataCessazione() == null 
                  && anagAziendaVO.getDataCessazioneStr() == null 
                  && anagAziendaVO.getDataFineVal() == null
                  && anagAziendaVO.isPossiedeDelegaAttiva())
              {
                //se il ruolo è provinciale deve essere della sua provincia di competenza
                //oppure non avere provincia di competenza
                if(ruoloUtenza.isUtenteProvinciale())
                {
                  if(ruoloUtenza.getCodiceEnte() != null 
                    && ruoloUtenza.getCodiceEnte().equals(anagAziendaVO.getProvCompetenza())
                    || anagAziendaVO.getProvCompetenza() == null)
                  {                
                    htmpl.newBlock("linkRevocaDelega");
                  }
                }
                else
                {
                  htmpl.newBlock("linkRevocaDelega");
                }
              }
            }
            
            if(hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)) 
            {
              if(obj instanceof AnagAziendaVO) 
              {
                
                htmpl.newBlock("linkModifica");
                
                htmpl.newBlock("linkCessa");
                if(anagAziendaVO.getTipoFormaGiuridica() != null && anagAziendaVO.getTipoFormaGiuridica().getCode() != null) {
                	// Se l'azienda ha forma giuridica "ditta individuale" o "persona fisica che non esercita attività di impresa"
                	if(anagAziendaVO.getTipoFormaGiuridica().getCode().toString().equalsIgnoreCase(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE.toString()) || anagAziendaVO.getTipoFormaGiuridica().getCode().toString().equalsIgnoreCase(SolmrConstants.FORMA_GIURIDICA_PERSONA_FISICA_NO_IMPRESA)) {
                		htmpl.newBlock("linkCambioTitolare");
                	}
                	else {
                		htmpl.newBlock("linkCambioRappresentante");
                	}
                }
                if(anagAziendaVO.isFlagAziendaProvvisoria()) {
                  htmpl.newBlock("linkDichiarazioneInsediamento");
                }
                //Vecchia revoca delega
                /*if(ruoloUtenza.isUtenteIntermediario()) {
                  htmpl.newBlock("linkRevocaDelega");
                }
                else {
                  if(anagAziendaVO.isPossiedeDelegaAttiva()) {
                    htmpl.newBlock("linkRevocaDelega");
                  }
                }*/
              }
              else 
              {
                Method[] elencoMetodi = obj.getClass().getMethods();
                Object valoreCausale = null;
                if(metodoCausaleAutorizzazione == null) 
                {
                  htmpl.newBlock(nomeBlocco + ".blkModifica");
                  htmpl.newBlock(nomeBlocco + ".blkCessazione");
                  htmpl.newBlock(nomeBlocco + ".blkElimina");
                }
                else 
                {
                  for(int i = 0; elencoMetodi.length > i; i++) 
                  {
                    Method m = elencoMetodi[i];
                    if(m.getName().equalsIgnoreCase(metodoCausaleAutorizzazione)) 
                    {
                      try 
                      {
                        valoreCausale = m.invoke(obj, (Object[])null);
                      }
                      catch (java.lang.IllegalAccessException iae) 
                      {}
                      catch (java.lang.reflect.InvocationTargetException ite) 
                      {}
                      if (valoreCausale == null) 
                      {
                        htmpl.newBlock(nomeBlocco + ".blkModifica");
                        htmpl.newBlock(nomeBlocco + ".blkCessazione");
                        htmpl.newBlock(nomeBlocco + ".blkElimina");
                      }
                      break;
                    }
                  }
                }
              }
            }
            else
            {
              if(ruoloUtenza.isReadWrite() && (ruoloUtenza.isUtenteRegionale() 
                  || ruoloUtenza.isUtenteOPRGestore() || ruoloUtenza.isUtenteProvinciale()))
              {
                if (ruoloUtenza.isUtenteProvinciale())
                {
                  //l'utente può cessare l'azienda solo se nella sua provincia
                  if (ruoloUtenza.getIstatProvincia().equals(anagAziendaVO.getProvCompetenza()))
                  {
                    htmpl.newBlock("linkCessa");
                    htmpl.newBlock("linkModifica");
                  }
                }
                else
                {
                  htmpl.newBlock("linkCessa");
                  htmpl.newBlock("linkModifica");
                }          
                 
              }
            }
          }
        }
        if(hasPropertiesToModifyAnagrafe(ruoloUtenza, anagAziendaVO)) 
        {
          htmpl.newBlock(nomeBlocco + ".blkInserisci");
          
          //livello introdotto per le aziende collegate
          //solo se l'azienda è la root faccio vedere modifica
          if((anagAziendaVO.getFlagFormaAssociata() != null) && (anagAziendaVO.getFlagFormaAssociata().equalsIgnoreCase(SolmrConstants.FLAG_S)))
          {
            Long level = (Long)request.getSession().getAttribute("levelAziendaCollegata");
            if(level !=null)
            {
              if(level.compareTo(new Long(1)) == 0) //primo livello
              {
                htmpl.newBlock("blkAziendeCollegate.linkModificaLivelloRoot");
              }
            }
          }
          
        }
      }
    }
  }
}

