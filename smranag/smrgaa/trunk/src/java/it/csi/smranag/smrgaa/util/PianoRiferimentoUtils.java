package it.csi.smranag.smrgaa.util;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.ProcedimentoAziendaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

public class PianoRiferimentoUtils
{
  
  @SuppressWarnings("unchecked")
  public void popolaComboPianoRiferimento(Htmpl htmpl, AnagFacadeClient client, Long idAzienda,
      String blocco, String idDichiarazioneConsistenza, String sezionePianoLavorazione, RuoloUtenza ruoloUtenza) throws SolmrException
  {
  
    
    Vector<ProcedimentoAziendaVO> vProcedimenti = null;
    
    try
    {
      vProcedimenti = client.updateAndGetPraticheByAzienda(idAzienda);
    }
    catch(SolmrException se) 
    {}
    
    
    
    
    TreeMap<Long,ConsistenzaVO> elencoPianiRiferimento = new TreeMap<Long,ConsistenzaVO>(new LongComparator());
    try 
    {
      boolean soloDichCons = false;
      String[] orderBy = {SolmrConstants.ORDER_BY_ANNO_CONSISTENZA_DESC, SolmrConstants.ORDER_BY_DATA_CONSISTENZA_DESC};
      ConsistenzaVO[] elencoDichCons = client.getListDichiarazioniConsistenzaByIdAzienda(idAzienda, orderBy);
      // Inserisco a mano il riferimento non reperibile da DB richiesti dal dominio
      if(Validator.isEmpty(ruoloUtenza) || (Validator.isNotEmpty(ruoloUtenza) 
          && ((ruoloUtenza.isReadWrite() && !ruoloUtenza.isUtenteODC())  
              || ruoloUtenza.isUtenteAziendaAgricola()
              || ruoloUtenza.isUtenteLegaleRappresentante()
              || ruoloUtenza.isUtenteTitolareCf()
              || ruoloUtenza.isUtenteServiziAgri())))
      {
        if(Validator.isNotEmpty(sezionePianoLavorazione))
        {
          if(sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_NORMALE)
              || sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_TERRENI)
              || sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_UV))
          {
            ConsistenzaVO consistenzaVO = new ConsistenzaVO();
            consistenzaVO.setIdDichiarazioneConsistenza("-1");
            elencoPianiRiferimento.put(new Long(-1), consistenzaVO);
          }
          
          if(sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_TERRENI)
              || sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_UV))
          {
            ConsistenzaVO consistenzaVO = new ConsistenzaVO();
            consistenzaVO.setIdDichiarazioneConsistenza("0");
            elencoPianiRiferimento.put(new Long(0), consistenzaVO);
          }
          
        }
      
      }
      //Sono nel caso che dovrei vedere solo le dichiarazioni di consistenza
      else
      {
        soloDichCons = true;        
      }
      
      if(elencoDichCons != null && elencoDichCons.length > 0) {
        for(int i = 0; i < elencoDichCons.length; i++) {
          ConsistenzaVO consistenzaElencoVO = (ConsistenzaVO)elencoDichCons[i];
          elencoPianiRiferimento.put(Long.decode(consistenzaElencoVO.getIdDichiarazioneConsistenza()), consistenzaElencoVO);
        }
      }
      else
      {
        //Non ho dichiarazioni di consistenza quindi devo fare vedere pe rforza il piano in lavorazione
        //anche se sono nei casi di "sola" lettura"
        if(soloDichCons)
        {
          if(Validator.isNotEmpty(sezionePianoLavorazione))
          {
            if(sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_NORMALE)
                || sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_TERRENI)
                || sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_UV))
            {
              ConsistenzaVO consistenzaVO = new ConsistenzaVO();
              consistenzaVO.setIdDichiarazioneConsistenza("-1");
              elencoPianiRiferimento.put(new Long(-1), consistenzaVO);
            }
            
            if(sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_TERRENI)
                || sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_UV))
            {
              ConsistenzaVO consistenzaVO = new ConsistenzaVO();
              consistenzaVO.setIdDichiarazioneConsistenza("0");
              elencoPianiRiferimento.put(new Long(0), consistenzaVO);
            }
            
          }
        }
      }
    }
    catch(SolmrException se) 
    {
      throw new SolmrException(AnagErrors.ERRORE_KO_PIANO_RIFERIMENTO);
    }
  
    // Verifico inoltre se il piano di riferimento corrente risulta essere
    // ripristinato da una precedente dichiarazione di consistenza
    boolean isRipristinata = false;
    try 
    {
      isRipristinata = client.isPianoRiferimentoRipristinato(idAzienda);
    }
    catch(SolmrException se) {
      throw new SolmrException(AnagErrors.ERRORE_KO_PIANO_RIFERIMENTO_RIPRISTINATO); 
    }
  
    int size = elencoPianiRiferimento.size();
    ConsistenzaVO dichiarazioneConsistenza = null;
    
    if(size > 0) 
    {
      Iterator<ConsistenzaVO> iteraPianiRiferimento = elencoPianiRiferimento.values().iterator();
      while(iteraPianiRiferimento.hasNext()) 
      {
        dichiarazioneConsistenza = (ConsistenzaVO)iteraPianiRiferimento.next();
        htmpl.newBlock(blocco);
        htmpl.set(blocco+".idDichiarazioneConsistenza", dichiarazioneConsistenza
            .getIdDichiarazioneConsistenza());
        
        String descrizione = "";
        if(dichiarazioneConsistenza.getIdDichiarazioneConsistenza().equalsIgnoreCase("-1")) 
        {
          descrizione = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione";
          if(isRipristinata) 
          {
            descrizione += " (RIPRISTINATA)";
          }
        }
        else 
        {
          if(dichiarazioneConsistenza.getIdDichiarazioneConsistenza().equalsIgnoreCase("0")) 
          {
            if(sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_TERRENI))
            {
               descrizione = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione (con conduzioni storicizzate)";
            }
            else if(sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_UV))
            {
              descrizione = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione (con UV storicizzate)";
            }             
          }
          else
          {
            descrizione = dichiarazioneConsistenza.getAnno()+" dichiarazione del "+StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.FULL_DATE_ORACLE_FORMAT, SolmrConstants.FULL_DATE_EUROPE_FORMAT, dichiarazioneConsistenza.getData());
            if(dichiarazioneConsistenza.getTipoMotivoDichiarazioneVO() != null 
              && dichiarazioneConsistenza.getTipoMotivoDichiarazioneVO().getTipoDichiarazione().equalsIgnoreCase(SolmrConstants.TIPO_DICHIARAZIONE_CORRETTIVA)) 
            {
              descrizione += " (CORRETTIVA)";
            }
            else if(dichiarazioneConsistenza.getTipoMotivoDichiarazioneVO() != null 
              && dichiarazioneConsistenza.getTipoMotivoDichiarazioneVO().getTipoDichiarazione().equalsIgnoreCase(SolmrConstants.TIPO_DICHIARAZIONE_COMUNICAZIONE_10R)) 
            {
              descrizione += " ("+SolmrConstants.LABEL_DICHIARAZIONE_COMUNICAZIONE_10R+")";
            }
          }
        }
        
        Vector<String> vAnnoCampagna = null;
        boolean flagPsr = false;
        boolean flagRpu = false;
        if(Validator.isNotEmpty(vProcedimenti))
        {
          for(int i=0;i<vProcedimenti.size();i++)
          {
            ProcedimentoAziendaVO procVO = vProcedimenti.get(i);
            if(procVO.getTipoProcedimentoVO() != null)
            {
              if((procVO.getTipoProcedimentoVO().getIdProcedimento() != null)
                 && (procVO.getTipoProcedimentoVO().getIdProcedimento().intValue() == SolmrConstants.ID_PROCEDIMENTO_PSR)
                 && (procVO.getIdDichiarazioneConsistenza() != null)
                 && (procVO.getIdDichiarazioneConsistenza().toString().equalsIgnoreCase(dichiarazioneConsistenza.getIdDichiarazioneConsistenza())))
              {
                flagPsr = true;
                if(procVO.getAnnoCampagna() != null)
                {
                  if(vAnnoCampagna == null)
                  {
                    vAnnoCampagna = new Vector<String>();
                  }
                  
                  if(!vAnnoCampagna.contains(procVO.getAnnoCampagna().toString()))
                  {
                    vAnnoCampagna.add(procVO.getAnnoCampagna().toString());
                  }
                }
              }
              
              if((procVO.getTipoProcedimentoVO().getIdProcedimento() != null)
                  && (procVO.getTipoProcedimentoVO().getIdProcedimento().intValue() == SolmrConstants.ID_PROCEDIMENTO_RPU)
                  && (procVO.getIdDichiarazioneConsistenza() != null)
                  && (procVO.getIdDichiarazioneConsistenza().toString().equalsIgnoreCase(dichiarazioneConsistenza.getIdDichiarazioneConsistenza())))
              {
             
                flagRpu = true;
                if(procVO.getAnnoCampagna() != null)
                {
                  if(vAnnoCampagna == null)
                  {
                    vAnnoCampagna = new Vector<String>();
                  }
                  
                  if(!vAnnoCampagna.contains(procVO.getAnnoCampagna().toString()))
                  {
                    vAnnoCampagna.add(procVO.getAnnoCampagna().toString());
                  }
                }
              }
              
              
            }
            
          }
          
          if(flagPsr)
          {
            descrizione += " PSR "; 
          }
          
          if(flagRpu)
          {
            if(flagPsr)
            {
              descrizione += "-"; 
            }
            descrizione += " RPU "; 
          }
          
          if(vAnnoCampagna != null)
          {
            for(int i=0;i<vAnnoCampagna.size();i++)
            {
              if(i!=0)
              {
                descrizione += "/";
              }
              descrizione += vAnnoCampagna.get(i);
            }
          }
        }
        
        
        htmpl.set(blocco+".descDichiarazioneConsistenza", descrizione);
        
        if(Validator.isNotEmpty(idDichiarazioneConsistenza))
        {
          Long idPianoRiferimentoLg = new Long(idDichiarazioneConsistenza);  
          if(idPianoRiferimentoLg.intValue() == Integer.parseInt(dichiarazioneConsistenza.getIdDichiarazioneConsistenza()))     
          {
            htmpl.set(blocco+".selected","selected");
          }
        }
      }
    }
  }
  
  
  
  @SuppressWarnings("unchecked")
  public void popolaComboPianoRiferimentoVarCat(Htmpl htmpl, AnagFacadeClient client, Long idAzienda,
      String blocco, String idDichiarazioneConsistenza, String sezionePianoLavorazione, RuoloUtenza ruoloUtenza) throws SolmrException
  {
  
    
    Vector<ProcedimentoAziendaVO> vProcedimenti = null;
    
    try
    {
      vProcedimenti = client.updateAndGetPraticheByAzienda(idAzienda);
    }
    catch(SolmrException se) 
    {}
    
    
    
    
    TreeMap<Long,ConsistenzaVO> elencoPianiRiferimento = new TreeMap<Long,ConsistenzaVO>(new LongComparator());
    try 
    {
      String[] orderBy = {SolmrConstants.ORDER_BY_ANNO_CONSISTENZA_DESC, SolmrConstants.ORDER_BY_DATA_CONSISTENZA_DESC};
      ConsistenzaVO[] elencoDichCons = client.getListDichiarazioniConsistenzaByIdAziendaVarCat(idAzienda, orderBy);
      
      
      if(elencoDichCons != null && elencoDichCons.length > 0) 
      {
        for(int i = 0; i < elencoDichCons.length; i++) {
          ConsistenzaVO consistenzaElencoVO = (ConsistenzaVO)elencoDichCons[i];
          elencoPianiRiferimento.put(Long.decode(consistenzaElencoVO.getIdDichiarazioneConsistenza()), consistenzaElencoVO);
        }
      }
    }
    catch(SolmrException se) 
    {
      throw new SolmrException(AnagErrors.ERRORE_KO_PIANO_RIFERIMENTO);
    }
  
    // Verifico inoltre se il piano di riferimento corrente risulta essere
    // ripristinato da una precedente dichiarazione di consistenza
    boolean isRipristinata = false;
    try 
    {
      isRipristinata = client.isPianoRiferimentoRipristinato(idAzienda);
    }
    catch(SolmrException se) {
      throw new SolmrException(AnagErrors.ERRORE_KO_PIANO_RIFERIMENTO_RIPRISTINATO); 
    }
  
    int size = elencoPianiRiferimento.size();
    ConsistenzaVO dichiarazioneConsistenza = null;
    
    if(size > 0) 
    {
      Iterator<ConsistenzaVO> iteraPianiRiferimento = elencoPianiRiferimento.values().iterator();
      while(iteraPianiRiferimento.hasNext()) 
      {
        dichiarazioneConsistenza = (ConsistenzaVO)iteraPianiRiferimento.next();
        htmpl.newBlock(blocco);
        htmpl.set(blocco+".idDichiarazioneConsistenza", dichiarazioneConsistenza
            .getIdDichiarazioneConsistenza());
        
        String descrizione = "";
        if(dichiarazioneConsistenza.getIdDichiarazioneConsistenza().equalsIgnoreCase("-1")) 
        {
          descrizione = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione";
          if(isRipristinata) 
          {
            descrizione += " (RIPRISTINATA)";
          }
        }
        else 
        {
          if(dichiarazioneConsistenza.getIdDichiarazioneConsistenza().equalsIgnoreCase("0")) 
          {
            if(sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_TERRENI))
            {
               descrizione = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione (con conduzioni storicizzate)";
            }
            else if(sezionePianoLavorazione.equalsIgnoreCase(SolmrConstants.PIANO_LAVORAZIONE_UV))
            {
              descrizione = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione (con UV storicizzate)";
            }             
          }
          else
          {
            descrizione = dichiarazioneConsistenza.getAnno()+" dichiarazione del "+StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.FULL_DATE_ORACLE_FORMAT, SolmrConstants.FULL_DATE_EUROPE_FORMAT, dichiarazioneConsistenza.getData());
            if(dichiarazioneConsistenza.getTipoMotivoDichiarazioneVO() != null 
              && dichiarazioneConsistenza.getTipoMotivoDichiarazioneVO().getTipoDichiarazione().equalsIgnoreCase(SolmrConstants.TIPO_DICHIARAZIONE_CORRETTIVA)) 
            {
              descrizione += " (CORRETTIVA)";
            }
            else if(dichiarazioneConsistenza.getTipoMotivoDichiarazioneVO() != null 
              && dichiarazioneConsistenza.getTipoMotivoDichiarazioneVO().getTipoDichiarazione().equalsIgnoreCase(SolmrConstants.TIPO_DICHIARAZIONE_COMUNICAZIONE_10R)) 
            {
              descrizione += " ("+SolmrConstants.LABEL_DICHIARAZIONE_COMUNICAZIONE_10R+")";
            }
          }
        }
        
        Vector<String> vAnnoCampagna = null;
        boolean flagPsr = false;
        boolean flagRpu = false;
        if(Validator.isNotEmpty(vProcedimenti))
        {
          for(int i=0;i<vProcedimenti.size();i++)
          {
            ProcedimentoAziendaVO procVO = vProcedimenti.get(i);
            if(procVO.getTipoProcedimentoVO() != null)
            {
              if((procVO.getTipoProcedimentoVO().getIdProcedimento() != null)
                 && (procVO.getTipoProcedimentoVO().getIdProcedimento().intValue() == SolmrConstants.ID_PROCEDIMENTO_PSR)
                 && (procVO.getIdDichiarazioneConsistenza() != null)
                 && (procVO.getIdDichiarazioneConsistenza().toString().equalsIgnoreCase(dichiarazioneConsistenza.getIdDichiarazioneConsistenza())))
              {
                flagPsr = true;
                if(procVO.getAnnoCampagna() != null)
                {
                  if(vAnnoCampagna == null)
                  {
                    vAnnoCampagna = new Vector<String>();
                  }
                  
                  if(!vAnnoCampagna.contains(procVO.getAnnoCampagna().toString()))
                  {
                    vAnnoCampagna.add(procVO.getAnnoCampagna().toString());
                  }
                }
              }
              
              if((procVO.getTipoProcedimentoVO().getIdProcedimento() != null)
                  && (procVO.getTipoProcedimentoVO().getIdProcedimento().intValue() == SolmrConstants.ID_PROCEDIMENTO_RPU)
                  && (procVO.getIdDichiarazioneConsistenza() != null)
                  && (procVO.getIdDichiarazioneConsistenza().toString().equalsIgnoreCase(dichiarazioneConsistenza.getIdDichiarazioneConsistenza())))
              {
             
                flagRpu = true;
                if(procVO.getAnnoCampagna() != null)
                {
                  if(vAnnoCampagna == null)
                  {
                    vAnnoCampagna = new Vector<String>();
                  }
                  
                  if(!vAnnoCampagna.contains(procVO.getAnnoCampagna().toString()))
                  {
                    vAnnoCampagna.add(procVO.getAnnoCampagna().toString());
                  }
                }
              }
              
              
            }
            
          }
          
          if(flagPsr)
          {
            descrizione += " PSR "; 
          }
          
          if(flagRpu)
          {
            if(flagPsr)
            {
              descrizione += "-"; 
            }
            descrizione += " RPU "; 
          }
          
          if(vAnnoCampagna != null)
          {
            for(int i=0;i<vAnnoCampagna.size();i++)
            {
              if(i!=0)
              {
                descrizione += "/";
              }
              descrizione += vAnnoCampagna.get(i);
            }
          }
        }
        
        
        htmpl.set(blocco+".descDichiarazioneConsistenza", descrizione);
        
        if(Validator.isNotEmpty(idDichiarazioneConsistenza))
        {
          Long idPianoRiferimentoLg = new Long(idDichiarazioneConsistenza);  
          if(idPianoRiferimentoLg.intValue() == Integer.parseInt(dichiarazioneConsistenza.getIdDichiarazioneConsistenza()))     
          {
            htmpl.set(blocco+".selected","selected");
          }
        }
      }
    }
  }
  
  
  
  @SuppressWarnings("rawtypes")
  //Recupero i valori relativi ai piani di riferimento
  class LongComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      if(o1 == null && o2 == null) {
          return 0;
        }
        else {
          if(o1 == null) {
              return -1;
            }
            else {
              if(o2 == null) {
                  return 1;
              }
            }
        }
        Long l1 = (Long)o1;
        Long l2 = (Long)o2;
        if(l1.toString().equalsIgnoreCase("-1") || l2.toString().equalsIgnoreCase("-1")) {
          return 1;
        }
        else if(l1.toString().equalsIgnoreCase("0") || l2.toString().equalsIgnoreCase("0")) {
          return 1;
        }
        else {
          return l2.compareTo(l1);
        }
    }
  }
  
  /**
   * Se utente in scrittura o azienda agricola ritorno il piano in lavorazione
   * attuale. Altrimenti se utente in sola lettura ritorno se esiste
   * l'ultima dichiarazione di consistenza, altrimenti il piano in lavorazione
   * 
   * 
   * @param client
   * @param ruoloUtenza
   * @param idAzienda
   * @return
   * @throws SolmrException
   */
  public Long primoIngressoAlPianoRiferimento(AnagFacadeClient client, RuoloUtenza ruoloUtenza, Long idAzienda, String provenienza) throws SolmrException
  {
    if(ruoloUtenza.isUtenteAziendaAgricola()
        || ruoloUtenza.isUtenteLegaleRappresentante()
        || ruoloUtenza.isUtenteTitolareCf()
        || ruoloUtenza.isUtenteServiziAgri()
        || ruoloUtenza.isReadWrite())
    {
      return new Long(-1);
    }
    else
    {
      String[] orderBy = {SolmrConstants.ORDER_BY_ANNO_CONSISTENZA_DESC, SolmrConstants.ORDER_BY_DATA_CONSISTENZA_DESC};
      ConsistenzaVO[] elencoDichCons = client.getListDichiarazioniConsistenzaByIdAzienda(idAzienda, orderBy);
      if(Validator.isNotEmpty(elencoDichCons) && elencoDichCons.length > 0)
      {
        return new Long(elencoDichCons[0].getIdDichiarazioneConsistenza());
      }
      
    }
    
    return new Long(-1);
    
    
  }
  
}
