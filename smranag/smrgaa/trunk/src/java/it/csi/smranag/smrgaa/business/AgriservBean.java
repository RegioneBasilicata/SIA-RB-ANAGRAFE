package it.csi.smranag.smrgaa.business;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.smranag.smrgaa.dto.AgriservChiamataVO;
import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.RigaPraticaParticellaVO;
import it.csi.smranag.smrgaa.dto.RitornoAgriservUvVO;
import it.csi.smranag.smrgaa.dto.RitornoAgriservVO;
import it.csi.smranag.smrgaa.dto.RitornoPraticheCCAgriservVO;
import it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoCCVO;
import it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.integration.AgriservDAO;
import it.csi.smranag.smrgaa.integration.AnagraficaDAO;
import it.csi.smranag.smrgaa.integration.ConduzioneParticellaGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.ParticellaGaaDAO;
import it.csi.smranag.smrgaa.interfacecsi.agriserv.IAgriservCSIInterface;
import it.csi.smranag.smrgaa.util.AnagUtils;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

/**
 * Classe Bean EJB con l'implementazione dei metodi di accesso alle informazioni
 * dei documenti
 * 
 * @author TOBECONFIG
 */
@Stateless(name=AgriservBean.jndiName,mappedName=AgriservBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AgriservBean implements AgriservLocal
{

  /**
   * Serial Version UID
   */

  /**
   * 
   */
  private static final long serialVersionUID = -4632814006699418477L;
  public static final String jndiName="comp/env/solmr/gaa/Agriserv";
  
  
  /** The session context */
  SessionContext             sessionContext;
  private AgriservDAO                agriservDAO             = null;
  private ParticellaGaaDAO           particellaGaaDAO        = null;
  private AnagraficaDAO              anagraficaDAO           = null;
  private ConduzioneParticellaGaaDAO conduzioneParticellaDAO = null;

  public AgriservBean()
  {
  }

  

  /**
   * Setta il sessionContext dell'EJB e crea i DAO
   */
  
  @Resource
  public void setSessionContext(SessionContext sessionContext) throws EJBException
  {
    SolmrLogger.debug(this, "[AgriservBean::setSessionContext] BEGIN.");
    this.sessionContext = sessionContext;
    initializeDAO();
    SolmrLogger.debug(this, "[AgriservBean::setSessionContext] END.");
  }

  public void initializeDAO() throws EJBException
  {
    SolmrLogger.debug(this, "[AgriservBean::initializeDAO] BEGIN.");
    try
    {
      agriservDAO = new AgriservDAO();
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, "[AgriservBean::initializeDAO] Eccezione nella creazione del DAO AgriservDAO. Eccezione: "
          + LoggerUtils.getStackTrace(e));
    }

    try
    {
      particellaGaaDAO = new ParticellaGaaDAO();
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this,
          "[AgriservBean::initializeDAO] Eccezione nella creazione del DAO ParticellaGaaDAO. Eccezione: "
              + LoggerUtils.getStackTrace(e));
    }

    try
    {
      anagraficaDAO = new AnagraficaDAO();
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this, "[AgriservBean::initializeDAO] Eccezione nella creazione del DAO AnagraficaDAO. Eccezione: "
          + LoggerUtils.getStackTrace(e));
    }

    try
    {
      conduzioneParticellaDAO = new ConduzioneParticellaGaaDAO();
    }
    catch (Exception e)
    {
      SolmrLogger.fatal(this,
          "[AgriservBean::initializeDAO] Eccezione nella creazione del DAO ConduzioneParticellaGaaDAO. Eccezione: "
              + LoggerUtils.getStackTrace(e));
    }

    SolmrLogger.debug(this, "[AgriservBean::initializeDAO] END.");
  }
  
  public RitornoAgriservVO searchPraticheProcedimento(long idParticella, Long idAzienda, int tipologiaStati,
      Long idDichiarazioneConsistenza, Long idProcedimento, Long annoCampagna, int tipoOrdinamento) throws SolmrException
  {
    Vector<Object> vChiamate = null;
    PraticaProcedimentoVO pratiche[] = null;
    PraticaProcedimentoVO praticheProcedimento[] = null;
    AgriservChiamataVO chiamataVO = null;
    long elencoIdStoricoParticella[] = null;
    RigaPraticaParticellaVO righePratiche[] = null;
    RigaPraticaParticellaVO riga = null;
    RitornoAgriservVO ritornoVO = null;
    Vector<String> vErrori = new Vector<String>();
    try
    {
      SolmrLogger.debug(this, "[AgriservBean::searchPraticheProcedimento] BEGIN.");
      AgriservChiamataVO chiamate[] = agriservDAO
          .getElencoChiamateAgriservByCodiceChiamata(SolmrConstants.CODICE_CHIAMATA_AGRISERV_DETTAGLIO_RICERCA_PARTICELLA_IDAZIENDA);
      int len = chiamate == null ? 0 : chiamate.length;
      vChiamate = new Vector<Object>();
      elencoIdStoricoParticella = particellaGaaDAO.getElencoIdStoricoParticellaByIdParticella(idParticella);
      for (int i = 0; i < len; ++i)
      {
        chiamataVO = chiamate[i];
        try
        {
          IAgriservCSIInterface iaci = getAgriservInstance(chiamataVO.getUrlPD(), chiamataVO.getNomeJndiPA(), chiamataVO
              .getCodiceEspositore());
          // Non faccio controlli su iaci, è diverso da null in quanto il metodo
          // getAgriservInstance avrebbe rilanciato eccezione se si fossero
          // verificati problemi durante il reperimento del proxy dinamico
          praticheProcedimento = iaci.agriservSearchPraticheProcedimento(elencoIdStoricoParticella, 
              idAzienda, tipologiaStati, idDichiarazioneConsistenza, idProcedimento, annoCampagna, 
              tipoOrdinamento);
          /** Escludoi null (anche se non dovrebbero essere presenti) */
          AnagUtils.addToVector(vChiamate, praticheProcedimento, true);
        }
        catch (Exception e)
        {
          // Log dell'errore
          Parametro parametri[] = new Parametro[]
          { new Parametro("elencoIdStoricoParticella", elencoIdStoricoParticella),
              new Parametro("idParticella", idParticella), new Parametro("tipologiaStati", tipologiaStati),
              new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
              new Parametro("idProcedimento", idProcedimento), new Parametro("annoCampagna", annoCampagna),
              new Parametro("tipoOrdinamento", tipoOrdinamento) };
          Variabile variabile[] = new Variabile[]
          { new Variabile("vChiamate", vChiamate), new Variabile("pratiche", pratiche),
              new Variabile("chiamataVO", chiamataVO), new Variabile("righePratiche", righePratiche),
              new Variabile("riga", riga), new Variabile("praticheProcedimento", praticheProcedimento) };

          LoggerUtils.logEJBError(this, "[AgriservBean::searchPraticheProcedimento]", e, variabile, parametri);

          vErrori.add(AnagErrors.ERRORE_ACCESSO_SERVIZIO_AGRISERV + ((chiamataVO == null) ? "SCONOSCIUTO" : chiamataVO
              .getDescrizioneChiamata()));
        }
      }
      int size = vChiamate.size();
      righePratiche = new RigaPraticaParticellaVO[size];
      if (size > 0)
      {
        pratiche = (PraticaProcedimentoVO[]) vChiamate.toArray(new PraticaProcedimentoVO[size]);
        HashMap<Long,BigDecimal> mapSupCatastale = particellaGaaDAO.getMapStoricoSupCatastaleByIdParticella(idParticella);
        HashMap<String, String[]> mapDatiAzienda = anagraficaDAO.getDatiAziendaDettaglioPraticheParticella(pratiche);
        Vector<Long> vIdDichiarazioneConsistenza = new Vector<Long>();
        for (int i = 0; i < size; ++i)
        {
          Long idDichiarazione = pratiche[i].getIdDichiarazioneConsistenza();
          if (idDichiarazione != null)
          {
            vIdDichiarazioneConsistenza.add(idDichiarazione);
          }
        }
        int numDichiarazioni = vIdDichiarazioneConsistenza.size();
        HashMap<Long,Vector<BaseCodeDescription>> mapConduzioni = null;
        if (numDichiarazioni > 0)
        {
          long aIdDichiarazioneConsistenza[] = new long[numDichiarazioni];
          for (int i = 0; i < numDichiarazioni; ++i)
          {
            // Sono != null quindi no NullPointerException
            aIdDichiarazioneConsistenza[i] = ((Long) vIdDichiarazioneConsistenza.get(i)).longValue();
          }
          mapConduzioni = conduzioneParticellaDAO.getMapConduzioniPraticheParticellaByIdDichiarazioneConsistenza(
              aIdDichiarazioneConsistenza, idParticella);
        }
        else
        {
          mapConduzioni = new HashMap<Long,Vector<BaseCodeDescription>>();
        }

        for (int i = 0; i < size; ++i)
        {
          riga = new RigaPraticaParticellaVO();
          PraticaProcedimentoVO praticaProcedimentoVO = (PraticaProcedimentoVO) vChiamate.get(i);
          riga.setPraticaProcedimentoVO(praticaProcedimentoVO);
          long idAziendaTmp = praticaProcedimentoVO.getIdAzienda();
          Long idDichiarazioneConsistenzaLong = praticaProcedimentoVO.getIdDichiarazioneConsistenza();
          String key = String.valueOf(idAziendaTmp) + "-" + idDichiarazioneConsistenzaLong;
          String dati[] = (String[]) mapDatiAzienda.get(key);
          if (dati != null)
          {
            riga.setCuaa(dati[0]);
            riga.setDenominazione(dati[1]);
          }
          if (idDichiarazioneConsistenzaLong != null)
          {
            Vector<BaseCodeDescription> vConduzioni = (Vector<BaseCodeDescription>) mapConduzioni.get(idDichiarazioneConsistenzaLong);
            if (vConduzioni != null && vConduzioni.size() > 0)
            {
              riga.setConduzioni((BaseCodeDescription[]) vConduzioni.toArray(new BaseCodeDescription[vConduzioni.size()]));
            }
          }
          riga.setSupCatastale((BigDecimal) mapSupCatastale.get(new Long(praticaProcedimentoVO.getIdStoricoParticella())));
          righePratiche[i] = riga;
        }
      }
      else
        righePratiche = null;
      ritornoVO = new RitornoAgriservVO();
      ritornoVO.setRighe(righePratiche);
      if (vErrori != null && vErrori.size() > 0)
      {
        String aErrori[] = (String[]) vErrori.toArray(new String[vErrori.size()]);
        ritornoVO.setErrori(aErrori);
      }
      return ritornoVO;
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[]
      { new Parametro("elencoIdStoricoParticella", elencoIdStoricoParticella),
          new Parametro("tipologiaStati", tipologiaStati),
          new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
          new Parametro("idProcedimento", idProcedimento), new Parametro("annoCampagna", annoCampagna),
          new Parametro("tipoOrdinamento", tipoOrdinamento) };
      Variabile variabile[] = new Variabile[]
      { new Variabile("vChiamate", vChiamate), new Variabile("pratiche", pratiche), new Variabile("chiamataVO", chiamataVO),
          new Variabile("righePratiche", righePratiche), new Variabile("riga", riga),
          new Variabile("praticheProcedimento", praticheProcedimento), new Variabile("ritornoVO", ritornoVO) };

      LoggerUtils.logEJBError(this, "[AgriservBean::searchPraticheProcedimento]", e, variabile, parametri);
      throw new SolmrException(e.getMessage());
    }
    finally
    {
      SolmrLogger.debug(this, "[AgriservBean::searchPraticheProcedimento] END.");
    }
  }
  
  
  public RitornoPraticheCCAgriservVO searchPraticheContoCorrente(long[] idContoCorrente, int tipologiaStati,
      Long idDichiarazioneConsistenza, Long idProcedimento, Long annoCampagna, int tipoOrdinamento) 
  throws SolmrException
  {
    Vector<Object> vChiamate = null;
    PraticaProcedimentoCCVO pratiche[] = null;
    PraticaProcedimentoCCVO praticheProcedimentoCC[] = null;
    AgriservChiamataVO chiamataVO = null;
    RitornoPraticheCCAgriservVO ritornoPraticheCCAgriservVO = null;
    HashMap<Long,PraticaProcedimentoCCVO[]> hContiCorrente = null;
    Vector<String> vErrori = new Vector<String>();
    try
    {
      SolmrLogger.debug(this, "[AgriservBean::searchPraticheContoCorrente] BEGIN.");
      AgriservChiamataVO chiamate[] = agriservDAO
          .getElencoChiamateAgriservByCodiceChiamata(SolmrConstants.CODICE_CHIAMATA_AGRISERV_PRATICHE_CONTO_CORRENTE);
      int len = chiamate == null ? 0 : chiamate.length;
      vChiamate = new Vector<Object>();
      
      for (int i = 0; i < len; ++i)
      {
        chiamataVO = chiamate[i];
        try
        {
          IAgriservCSIInterface iaci = getAgriservInstance(chiamataVO.getUrlPD(), chiamataVO.getNomeJndiPA(), chiamataVO
              .getCodiceEspositore());
          // Non faccio controlli su iaci, è diverso da null in quanto il metodo
          // getAgriservInstance avrebbe rilanciato eccezione se si fossero
          // verificati problemi durante il reperimento del proxy dinamico
          praticheProcedimentoCC = iaci.agriservSearchPraticheProcedimentoCC(idContoCorrente, 
              tipologiaStati, idDichiarazioneConsistenza, idProcedimento, annoCampagna, tipoOrdinamento);
          /** Escludoi null (anche se non dovrebbero essere presenti) */
          AnagUtils.addToVector(vChiamate, praticheProcedimentoCC, true);
        }
        catch (Exception e)
        {
          // Log dell'errore
          Parametro parametri[] = new Parametro[]
          { new Parametro("idContoCorrente", idContoCorrente),
              new Parametro("tipologiaStati", tipologiaStati),
              new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
              new Parametro("idProcedimento", idProcedimento), 
              new Parametro("annoCampagna", annoCampagna),
              new Parametro("tipoOrdinamento", tipoOrdinamento) };
          Variabile variabile[] = new Variabile[]
          {   new Variabile("vChiamate", vChiamate), 
              new Variabile("pratiche", pratiche),
              new Variabile("chiamataVO", chiamataVO), 
              new Variabile("hContiCorrente", hContiCorrente),
              new Variabile("ritornoPraticheCCAgriservVO", ritornoPraticheCCAgriservVO), 
              new Variabile("praticheProcedimentoCC", praticheProcedimentoCC) };

          LoggerUtils.logEJBError(this, "[AgriservBean::searchPraticheContoCorrente]", e, variabile, parametri);

          vErrori.add(AnagErrors.ERRORE_ACCESSO_SERVIZIO_AGRISERV + ((chiamataVO == null) ? "SCONOSCIUTO" : chiamataVO
              .getDescrizioneChiamata()));
        }
      }
      
      if(vChiamate != null)
      {
        hContiCorrente = new HashMap<Long,PraticaProcedimentoCCVO[]>();
        ritornoPraticheCCAgriservVO = new RitornoPraticheCCAgriservVO();
        int size = vChiamate.size();
        pratiche = new PraticaProcedimentoCCVO[size];
        if (size > 0)
        {
          pratiche = (PraticaProcedimentoCCVO[]) vChiamate.toArray(new PraticaProcedimentoCCVO[size]);
          for(int i=0;i<pratiche.length;i++)
          {
            Long idContoCorrenteLg = new Long(pratiche[i].getIdContoCorrente());
            if(hContiCorrente.get(idContoCorrenteLg) != null)
            {
              PraticaProcedimentoCCVO[] praticheTmp = 
                (PraticaProcedimentoCCVO[])hContiCorrente.get(idContoCorrenteLg);              
              PraticaProcedimentoCCVO[] praticheNew = new PraticaProcedimentoCCVO[praticheTmp.length+1];
              for(int j=0;j<praticheTmp.length;j++)
              {
                praticheNew[j] = praticheTmp[j];
              }
              
              praticheNew[praticheTmp.length] = pratiche[i];
              
              hContiCorrente.remove(idContoCorrenteLg);
              hContiCorrente.put(idContoCorrenteLg, praticheNew);
              
            }
            else //Prima volta che trovo qualcosa associato al contocorrente
            {
              PraticaProcedimentoCCVO[] praticheTmp = new PraticaProcedimentoCCVO[1];
              praticheTmp[0] = pratiche[i];
              hContiCorrente.put(idContoCorrenteLg, praticheTmp);              
            }
            
          }
          
          ritornoPraticheCCAgriservVO.setHPraticheCC(hContiCorrente);
        }
        
        if (vErrori != null && vErrori.size() > 0)
        {
          String aErrori[] = (String[]) vErrori.toArray(new String[vErrori.size()]);
          ritornoPraticheCCAgriservVO.setErrori(aErrori);
        }
      }
      
      
      return ritornoPraticheCCAgriservVO;
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[]
      { new Parametro("idContoCorrente", idContoCorrente),
          new Parametro("tipologiaStati", tipologiaStati),
          new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
          new Parametro("idProcedimento", idProcedimento), 
          new Parametro("annoCampagna", annoCampagna),
          new Parametro("tipoOrdinamento", tipoOrdinamento) };
      Variabile variabile[] = new Variabile[]
      { new Variabile("vChiamate", vChiamate), 
          new Variabile("pratiche", pratiche), 
          new Variabile("chiamataVO", chiamataVO),
          new Variabile("hContiCorrente", hContiCorrente), 
          new Variabile("pratiche", pratiche), 
          new Variabile("ritornoPraticheCCAgriservVO", ritornoPraticheCCAgriservVO) };

      LoggerUtils.logEJBError(this, "[AgriservBean::searchPraticheContoCorrente]", e, variabile, parametri);
      throw new SolmrException(e.getMessage());
    }
    finally
    {
      SolmrLogger.debug(this, "[AgriservBean::searchPraticheContoCorrente] END.");
    }
  }
  
  
  /**
   * 
   * mi ritorna il numero di pratiche di viti relative alle particelle
   * 
   * 
   * @param idParticella
   * @param idAzienda
   * @param tipologiaStati
   * @param idDichiarazioneConsistenza
   * @param idProcedimento
   * @param annoCampagna
   * @param tipoOrdinamento
   * @return
   * @throws SolmrException
   */
  public RitornoAgriservUvVO existPraticheEstirpoUV(long[] idParticella, Long idAzienda, Long idDichiarazioneConsistenza, 
      Long idProcedimento, Long annoCampagna, int tipoOrdinamento) throws SolmrException
  {
    Vector<Object> vChiamate = null;
    PraticaProcedimentoVO pratiche[] = null;
    PraticaProcedimentoVO praticheProcedimento[] = null;
    AgriservChiamataVO chiamataVO = null;
    HashMap<Long,Long> hCoppieIdStorIdPart = null;
    //se presente un record significa che ad esso è asociata almeno una pratica
    Vector<Long> vIdParticella = null;
    RitornoAgriservUvVO ritornoVO = null;
    Vector<String> vErrori = new Vector<String>();
    long[] elencoIdStoricoParticella = null;
    
    int tipologiaStati = PraticaProcedimentoVO.FLAG_STATO_SOLO_ULTIMO;
    tipologiaStati += PraticaProcedimentoVO.FLAG_STATO_ESCLUDI_ANNULLATO;
    tipologiaStati += PraticaProcedimentoVO.FLAG_STATO_ESCLUDI_ANNULLATO_PER_SOSTITUZIONE;
    
    
    try
    {
      SolmrLogger.debug(this, "[AgriservBean::existPraticheEstirpoUV] BEGIN.");
      AgriservChiamataVO chiamate[] = agriservDAO
          .getElencoChiamateAgriservByCodiceChiamata(SolmrConstants.CODICE_CHIAMATA_AGRISERV_RICERCA_PRATICHE_UV_IDAZIENDA);
      int len = chiamate == null ? 0 : chiamate.length;
      vChiamate = new Vector<Object>();
      hCoppieIdStorIdPart = particellaGaaDAO.getHashIdStoricoParticellaIdParticellaByIdParticella(idParticella);
      elencoIdStoricoParticella = new long[hCoppieIdStorIdPart.size()];
      Iterator<Long> it = hCoppieIdStorIdPart.keySet().iterator();
      int k=0;
      //mi estraggo tutti gli idStoricoParticella
      while(it.hasNext())
      {
        elencoIdStoricoParticella[k] = it.next().longValue();
        k++;
      }

      for (int i = 0; i < len; ++i)
      {
        chiamataVO = chiamate[i];
        try
        {
          IAgriservCSIInterface iaci = getAgriservInstance(chiamataVO.getUrlPD(), chiamataVO.getNomeJndiPA(), chiamataVO
              .getCodiceEspositore());
          // Non faccio controlli su iaci, è diverso da null in quanto il metodo
          // getAgriservInstance avrebbe rilanciato eccezione se si fossero
          // verificati problemi durante il reperimento del proxy dinamico
          praticheProcedimento = iaci.agriservSearchPraticheProcedimento(elencoIdStoricoParticella, 
              idAzienda, tipologiaStati, idDichiarazioneConsistenza, idProcedimento, annoCampagna, 
              tipoOrdinamento);
          /** Escludo i null (anche se non dovrebbero essere presenti) */
          AnagUtils.addToVector(vChiamate, praticheProcedimento, true);
        }
        catch (Exception e)
        {
          // Log dell'errore
          Parametro parametri[] = new Parametro[]
          { new Parametro("elencoIdStoricoParticella", elencoIdStoricoParticella),
              new Parametro("idParticella", idParticella), new Parametro("tipologiaStati", tipologiaStati),
              new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
              new Parametro("idProcedimento", idProcedimento), new Parametro("annoCampagna", annoCampagna),
              new Parametro("tipoOrdinamento", tipoOrdinamento) };
          Variabile variabile[] = new Variabile[]
          { new Variabile("vChiamate", vChiamate), new Variabile("pratiche", pratiche),
              new Variabile("chiamataVO", chiamataVO), new Variabile("hCoppieIdStorIdPart", hCoppieIdStorIdPart),
              new Variabile("vIdParticella", vIdParticella), new Variabile("praticheProcedimento", praticheProcedimento) };

          LoggerUtils.logEJBError(this, "[AgriservBean::existPraticheEstirpoUV]", e, variabile, parametri);

          vErrori.add(AnagErrors.ERRORE_ACCESSO_SERVIZIO_AGRISERV + ((chiamataVO == null) ? "SCONOSCIUTO" : chiamataVO
              .getDescrizioneChiamata()));
        }
      }
      int size = vChiamate.size();
      //righePratiche = new RigaPraticaParticellaVO[size];
      if (size > 0)
      {
        pratiche = (PraticaProcedimentoVO[]) vChiamate.toArray(new PraticaProcedimentoVO[size]);

        for (int i = 0; i < size; ++i)
        {
          if(vIdParticella == null)
          {
            vIdParticella = new Vector<Long>();
          }
          PraticaProcedimentoVO praticaProcedimentoVO = (PraticaProcedimentoVO) vChiamate.get(i);
          Long idParticellaTmp = hCoppieIdStorIdPart.get(new Long(praticaProcedimentoVO.getIdStoricoParticella()));
          if(!vIdParticella.contains(idParticellaTmp))
          {
            vIdParticella.add(idParticellaTmp);
          }
          
        }
      }
      
      ritornoVO = new RitornoAgriservUvVO();
      ritornoVO.setvPraticheIdParticella(vIdParticella);
      if (vErrori != null && vErrori.size() > 0)
      {
        String aErrori[] = (String[]) vErrori.toArray(new String[vErrori.size()]);
        ritornoVO.setErrori(aErrori);
      }
      return ritornoVO;
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[]
      { new Parametro("elencoIdStoricoParticella", elencoIdStoricoParticella),
          new Parametro("tipologiaStati", tipologiaStati),
          new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza),
          new Parametro("idProcedimento", idProcedimento), new Parametro("annoCampagna", annoCampagna),
          new Parametro("tipoOrdinamento", tipoOrdinamento) };
      Variabile variabile[] = new Variabile[]
      { new Variabile("vChiamate", vChiamate), new Variabile("pratiche", pratiche), new Variabile("chiamataVO", chiamataVO),
          new Variabile("hCoppieIdStorIdPart", hCoppieIdStorIdPart), new Variabile("vIdParticella", vIdParticella),
          new Variabile("praticheProcedimento", praticheProcedimento), new Variabile("ritornoVO", ritornoVO) };

      LoggerUtils.logEJBError(this, "[AgriservBean::existPraticheEstirpoUV]", e, variabile, parametri);
      throw new SolmrException(e.getMessage());
    }
    finally
    {
      SolmrLogger.debug(this, "[AgriservBean::existPraticheEstirpoUV] END.");
    }
  }
  
  
  
  
  
  

  private IAgriservCSIInterface getAgriservInstance(String urlt3, String jndiName, String codiceFornitore)
      throws SolmrException
  {
    try
    {
      SolmrLogger.debug(this, "[AgriservBean::getAgriservInstance] BEGIN.");
      InfoPortaDelegata pdInfo = PDConfigReader
          .read(getClass().getResourceAsStream(SolmrConstants.FILE_PD_AGRISERV_GENERIC));
      InfoPortaDelegata info = pdInfo.getPlugins()[0];
      SolmrLogger.debug(this, "-- jndiName ="+jndiName);
      info.setUrlPortaApplicativa(jndiName);
      SolmrLogger.debug(this, "-- url ="+urlt3);
      info.getProperties().put("javax.xml.rpc.service.endpoint.address", urlt3);
      return (IAgriservCSIInterface) PDProxy.newInstance(pdInfo);
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[]
      { new Parametro("urlt3", urlt3), new Parametro("jndiName", jndiName) };
      LoggerUtils.logEJBError(this, "[AgriservBean::getAgriservInstance]", e, null, parametri);
      throw new SolmrException("Errore di accesso all'interfaccia del servizo di agriserv del modulo " + codiceFornitore);
    }
  }

}
