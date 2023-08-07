package it.csi.solmr.business.anag;

import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaSezioniVO;
import it.csi.smranag.smrgaa.integration.AnagrafeGaaDAO;
import it.csi.smranag.smrgaa.integration.MacchineAgricoleGaaDAO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.UfficioZonaIntermediarioVO;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.AnagAAEPAziendaVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagraficaAzVO;
import it.csi.solmr.dto.anag.AziendaCollegataVO;
import it.csi.solmr.dto.anag.AziendaDestinazioneVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.IntermediarioAnagVO;
import it.csi.solmr.dto.anag.NotificaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.ProcedimentoAziendaVO;
import it.csi.solmr.dto.anag.TipoSezioniAaepVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.anag.services.DelegaAnagrafeVO;
import it.csi.solmr.dto.anag.sian.SianFascicoloResponseVO;
import it.csi.solmr.dto.anag.sian.SianTrovaFascicoloVO;
import it.csi.solmr.dto.comune.IntermediarioVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.dto.uma.DittaUMAVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.SolmrErrors;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.AnagWriteDAO;
import it.csi.solmr.integration.anag.AnagrafeDAO;
import it.csi.solmr.integration.anag.AziendaDestinazioneDAO;
import it.csi.solmr.integration.anag.ConsistenzaDAO;
import it.csi.solmr.integration.anag.DelegaDAO;
import it.csi.solmr.integration.anag.DocumentoDAO;
import it.csi.solmr.integration.anag.FascicoloDAO;
import it.csi.solmr.integration.anag.NotificaDAO;
import it.csi.solmr.integration.anag.SianDAO;
import it.csi.solmr.integration.anag.SoggettiDAO;
import it.csi.solmr.integration.anag.UteDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;
import it.csi.solmr.ws.infoc.RappresentanteLegale;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

@Stateless(name="comp/env/solmr/anag/AnagAzienda",mappedName="comp/env/solmr/anag/AnagAzienda")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class AnagAziendaBean implements AnagAziendaLocal
{
  /**
   * 
   */
  private static final long                serialVersionUID       = -7717744009108647672L;
  SessionContext                           sessionContext;

  private transient AnagrafeDAO            aDAO                   = null;
  private transient AnagWriteDAO           wDAO                   = null;
  private transient CommonDAO              cDAO                   = null;
  private transient SoggettiDAO            sDAO                   = null;
  private transient FascicoloDAO           fascicoloDAO           = null;
  private transient NotificaDAO            notificaDAO            = null;
  private transient DocumentoDAO           documentoDAO           = null;
  private transient DelegaDAO              delegaDAO              = null;
  private transient AziendaDestinazioneDAO aziendaDestinazioneDAO = null;
  private transient SianDAO                sianDAO                = null;
  private transient ConsistenzaDAO         consDAO                = null;
  private transient AnagrafeGaaDAO         aGaaDAO                = null;
  //private transient NuovaIscrizioneDAO     nuovaIscrizioneDAO     = null;
  private transient MacchineAgricoleGaaDAO macchineAgricoleGaaDAO = null;
  private transient UteDAO uteDAO = null;

  @EJB
  DocumentoLocal documentoLocal = null;


  @Resource
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
    initializeDAO();
  }

  private void initializeDAO() throws EJBException
  {
    try
    {
      aDAO = new AnagrafeDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      wDAO = new AnagWriteDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      cDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      sDAO = new SoggettiDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      fascicoloDAO = new FascicoloDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      notificaDAO = new NotificaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      documentoDAO = new DocumentoDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      delegaDAO = new DelegaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      aziendaDestinazioneDAO = new AziendaDestinazioneDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      sianDAO = new SianDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      consDAO = new ConsistenzaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      aGaaDAO = new AnagrafeGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      //nuovaIscrizioneDAO = new NuovaIscrizioneDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      macchineAgricoleGaaDAO = new MacchineAgricoleGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      uteDAO = new UteDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

  public AnagAziendaVO getAziendaById(Long idAnagAzienda) throws Exception, NotFoundException
  {
    AnagAziendaVO result = null;
    try
    {
      // Il 2 parametro impostato a false significa che la ricerca viene fatta
      // su ID_ANAGRAFICA_AZIENDA, il terzo parametro viene ignorato
      result = aDAO.findByPrimaryKey(idAnagAzienda, false, null);
      DelegaVO dVO = delegaDAO.getDelegaByAziendaAndIdProcedimento(result.getIdAzienda(), (Long) SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
      result.setDelegaVO(dVO);
      result.setPossiedeDelegaAttiva(aDAO.controllaPresenzaDelega(result));
      
      //controlla se è figlio di qualche azienda sulla tabella DB_AZIENDE_COLLEGATE
      result.setFlagEnteAppartenenza(aDAO.hasEntiAppartenenza(result.getIdAzienda().longValue()));
      //***************************************************
    }
    catch (SolmrException e)
    {
      throw new Exception(e.getMessage());
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  public Vector<AnagAziendaVO> getAziendaCUAA(String CUAA) throws Exception, NotFoundException, SolmrException
  {
    Vector<AnagAziendaVO> result = null;
    try
    {
      result = aDAO.getAziendaByCriterio(CUAA);
      if (result != null)
      {
        for (int i = 0; i < result.size(); i++)
        {
          AnagAziendaVO aaVO = (AnagAziendaVO) result.elementAt(i);
          DelegaVO dVO = delegaDAO.getDelegaByAziendaAndIdProcedimento(aaVO.getIdAzienda(), (Long) SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
          aaVO.setDelegaVO(dVO);
          aaVO.setPossiedeDelegaAttiva(aDAO.controllaPresenzaDelega(aaVO));
        }
      }
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  public AnagAziendaVO getAziendaCUAA(String CUAA, Date dataSituazioneAl) throws Exception, NotFoundException, SolmrException
  {
    AnagAziendaVO result = null;
    try
    {
      result = aDAO.getAziendaByCriterio(SolmrConstants.CRITERIO_CUAA, CUAA, dataSituazioneAl);
      DelegaVO dVO = delegaDAO.getDelegaByAziendaAndIdProcedimento(result.getIdAzienda(), (Long) SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
      result.setDelegaVO(dVO);
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  public AnagAziendaVO getAziendaPartitaIVA(String partitaIVA, Date dataSituazioneAl) throws Exception, NotFoundException, SolmrException
  {
    AnagAziendaVO result = null;
    try
    {
      result = aDAO.getAziendaByCriterio(SolmrConstants.CRITERIO_PARTITA_IVA, partitaIVA, dataSituazioneAl);
      DelegaVO dVO = delegaDAO.getDelegaByAziendaAndIdProcedimento(result.getIdAzienda(), (Long) SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
      result.setDelegaVO(dVO);
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  public Vector<Long> getListIdAziende(AnagAziendaVO aaVO, Date dataSituazioneAl, boolean attivitaBool) throws Exception, NotFoundException, SolmrException
  {
    Vector<Long> result = null;

    try
    {
      /**
       * Il metodo getListIdAziende del DAO riceve 5 parametri invece di tre
       * perchè due servono a vitivinicolo, quindi dato che qua non servono sono
       * stati impostati a false
       */
      result = aDAO.getListIdAziende(aaVO, dataSituazioneAl, attivitaBool, false, false);
    }
    catch (DataControlException se)
    {
      throw new SolmrException(SolmrErrors.EXC_TROPPI_RECORD_SELEZIONATI);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  public Vector<Long> getListIdAziendeFlagProvvisorio(AnagAziendaVO aaVO, Date dataSituazioneAl, boolean attivitaBool, boolean provvisorio) throws Exception, NotFoundException, SolmrException
  {
    Vector<Long> result = null;

    try
    {
      result = aDAO.getListIdAziendeFlagProvvisorio(aaVO, dataSituazioneAl, attivitaBool, provvisorio);
    }
    catch (DataControlException se)
    {
      throw new SolmrException(SolmrErrors.EXC_TROPPI_RECORD_SELEZIONATI);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  // restituisce Vectore di IdAzienda
  public Vector<Long> getListOfIdAzienda(AnagAziendaVO aaVO, Date dataSituazioneAl, boolean attivitaBool) throws Exception, NotFoundException, SolmrException
  {
    Vector<Long> result = null;

    try
    {
      /**
       * Il metodo getListIdAziende del DAO riceve 5 parametri invece di tre
       * perchè due servono a vitivinicolo, quindi dato che qua non servono sono
       * stati impostati a false
       */
      result = aDAO.getListIdAziende(aaVO, dataSituazioneAl, attivitaBool, false, true);
    }
    catch (DataControlException se)
    {
      throw new SolmrException(SolmrErrors.EXC_TROPPI_RECORD_SELEZIONATI);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  public Vector<CodeDescription> getTipiAttivitaOTE(String code, String description) throws Exception, NotFoundException
  {
    Vector<CodeDescription> result = null;
    try
    {
      result = cDAO.getAttivitaLike(code, description, SolmrConstants.TAB_TIPO_ATTIVITA_OTE);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
    return result;
  }

  /**
   * La ricerca è fatta per ID_ANAGRAFICA_AZIENDA
   * 
   * @param idAnagAzienda
   * @return
   * @throws Exception
   * @throws NotFoundException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getListAziendeByIdRange(Vector<Long> idAnagAzienda) throws Exception, NotFoundException, SolmrException
  {
    Vector<AnagAziendaVO> result = null;
    try
    {
      // Il 2 parametro impostato a false significa che la ricerca viene fatta
      // su ID_ANAGRAFICA_AZIENDA
      result = aDAO.getListAziendeByIdRange(idAnagAzienda, false, null);

      /**
       * */
      Vector<Long> vectAzienda = new Vector<Long>();
      for (int i = 0; i < result.size(); i++)
        vectAzienda.addElement(((AnagAziendaVO) result.elementAt(i)).getIdAzienda());
      Vector<DelegaVO> deleghe = aDAO.getRangeDelegaByRangeAzienda(vectAzienda);
      for (int i = 0; i < result.size(); i++)
      {        
        AnagAziendaVO aaVO = (AnagAziendaVO) result.elementAt(i);
        
        //controlla se è figlio di qualche azienda sulla tabella DB_AZIENDE_COLLEGATE
        aaVO.setFlagEnteAppartenenza(aDAO.hasEntiAppartenenza(aaVO.getIdAzienda().longValue()));
        //***************************************************
        
        aaVO.setPossiedeDelegaAttiva(aDAO.controllaPresenzaDelega(aaVO));
        for (int j = 0; j < deleghe.size(); j++)
        {
          DelegaVO dVO = (DelegaVO) deleghe.elementAt(j);
          if (aaVO.getIdAzienda().equals(dVO.getIdAzienda()))
          {
            aaVO.setDelegaVO(dVO);
            break;
          }
        }
      }
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  /**
   * La ricerca è fatta per ID_AZIENDA
   * 
   * @param idAzienda
   * @return
   * @throws Exception
   * @throws NotFoundException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getAziendeByIdAziendaRange(Vector<Long> idAzienda) throws Exception, NotFoundException, SolmrException
  {
    Vector<AnagAziendaVO> result = null;
    try
    {
      // Il 2 parametro impostato a true significa che la ricerca viene fatta
      // su ID_AZIENDA
      result = aDAO.getListAziendeByIdRange(idAzienda, true, null);

      /**
       * */
      Vector<Long> vectAzienda = new Vector<Long>();
      for (int i = 0; i < result.size(); i++)
        vectAzienda.addElement(((AnagAziendaVO) result.elementAt(i)).getIdAzienda());
      Vector<DelegaVO> deleghe = aDAO.getRangeDelegaByRangeAzienda(vectAzienda);
      for (int i = 0; i < result.size(); i++)
      {
        AnagAziendaVO aaVO = (AnagAziendaVO) result.elementAt(i);
        
        //controlla se è figlio di qualche azienda sulla tabella DB_AZIENDE_COLLEGATE
        aaVO.setFlagEnteAppartenenza(aDAO.hasEntiAppartenenza(aaVO.getIdAzienda().longValue()));
        //***************************************************
        
        aaVO.setPossiedeDelegaAttiva(aDAO.controllaPresenzaDelega(aaVO));
        for (int j = 0; j < deleghe.size(); j++)
        {
          DelegaVO dVO = (DelegaVO) deleghe.elementAt(j);
          if (aaVO.getIdAzienda().equals(dVO.getIdAzienda()))
          {
            aaVO.setDelegaVO(dVO);
            break;
          }
        }
      }
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  // restituisco vettore di aziende passando un vettore di idAzienda
  public Vector<AnagAziendaVO> getListAziendeByIdRangeFromIdAzienda(Vector<Long> idAzienda) throws Exception, NotFoundException, SolmrException
  {
    Vector<AnagAziendaVO> result = null;
    try
    {
      // Il 2 parametro impostato a true significa che la ricerca viene fatta
      // su ID_AZIENDA
      result = aDAO.getListAziendeByIdRange(idAzienda, true, null);

      Vector<Long> vectAzienda = new Vector<Long>();
      for (int i = 0; i < result.size(); i++)
        vectAzienda.addElement(((AnagAziendaVO) result.elementAt(i)).getIdAzienda());
      Vector<DelegaVO> deleghe = aDAO.getRangeDelegaByRangeAzienda(vectAzienda);
      for (int i = 0; i < result.size(); i++)
      {
        AnagAziendaVO aaVO = (AnagAziendaVO) result.elementAt(i);
        
        //controlla se è figlio di qualche azienda sulla tabella DB_AZIENDE_COLLEGATE
        aaVO.setFlagEnteAppartenenza(aDAO.hasEntiAppartenenza(aaVO.getIdAzienda().longValue()));
        //***************************************************
        
        aaVO.setPossiedeDelegaAttiva(aDAO.controllaPresenzaDelega(aaVO));
        for (int j = 0; j < deleghe.size(); j++)
        {
          DelegaVO dVO = (DelegaVO) deleghe.elementAt(j);
          if (aaVO.getIdAzienda().equals(dVO.getIdAzienda()))
          {
            aaVO.setDelegaVO(dVO);
            break;
          }
        }
      }

    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  public PersonaFisicaVO getTitolareORappresentanteLegaleAzienda(Long idAzienda, Date dataSituazioneAl) throws Exception, SolmrException
  {
    PersonaFisicaVO personaVO = null;
    try
    {
      personaVO = aDAO.getTitolareORappresentanteLegaleAzienda(idAzienda, dataSituazioneAl);
    }
    catch (NotFoundException e)
    {
      throw new SolmrException(AnagErrors.NESSUN_RAPPRESENTANTE_LEGALE);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }

    return personaVO;
  }

  /**
   * Questo metodo controlla se è possibile inserire una delega per la gestione
   * di fascicolo aziendale. Se ci sono dei problemi rilancia un'eccezione.
   * restituisce true se va tutto bene, mentre restituisce false se l'azienda
   * non prevede l'obbligo del fascicolo, nel qual caso bisogna chiedere
   * all'utente se vuole proseguire
   * 
   */
  public boolean controllaRegistrazioneMandato(AnagAziendaVO aziendaVO, String codiceEnte, DelegaAnagrafeVO delegaAnagrafeVO) throws Exception, SolmrException
  {
    boolean conferma = false;
    try
    {
      if (aDAO.controllaAziendaCessata(aziendaVO.getIdAnagAzienda()))
        throw new SolmrException((String) AnagErrors.get("ERR_REG_MANDATO_AZIENDA_CESSATA"));
      if (aDAO.controllaAziendaFineValidita(aziendaVO.getIdAnagAzienda()))
        throw new SolmrException((String) AnagErrors.get("ERR_REG_MANDATO_AZIENDA_STORICIZZATA"));
      // Controllo che se la delega è presente...
      if (delegaAnagrafeVO != null)
      {
        if (aziendaVO.isPossiedeDelegaAttiva()
            && (codiceEnte.equalsIgnoreCase(aziendaVO.getDelegaVO().getCodiceFiscaleIntermediario()) || SolmrConstants.FLAG_S.equalsIgnoreCase(delegaAnagrafeVO
                .getFlagFiglio())))
        {
          throw new SolmrException((String) AnagErrors.get("ERR_REG_MANDATO_PRESENTE"));
        }
        else
        {
          throw new SolmrException((String) AnagErrors.get("ERR_REG_MANDATO_ALTRO_ENTE") + " " + delegaAnagrafeVO.getCodiceFiscIntermediario());
        }

      }
      conferma = aDAO.controllaDelegaObbligoFascicolo(aziendaVO);
    }
    catch (SolmrException exc)
    {
      throw exc;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return conferma;
  }
  
  /**
   * 
   * Questo metodo controlla se è possibile procedere nella revoca del mandato
   * Se ci sono dei problemi rilancia un'eccezione.
   * restituisce true se va tutto bene.
   * 
   * 
   * 
   * @param aziendaVO
   * @param profile
   * @param delegaAnagrafeVO
   * @return
   * @throws Exception
   * @throws SolmrException
   */
  public boolean controllaRevocaMandato(AnagAziendaVO anagAziendaVO, RuoloUtenza ruoloUtenza, DelegaAnagrafeVO delegaAnagrafeVO) 
    throws Exception, SolmrException
  {
    boolean conferma = true;
    try
    {
      if (aDAO.controllaAziendaCessata(anagAziendaVO.getIdAnagAzienda()))
        throw new SolmrException(AnagErrors.ERR_REV_MANDATO_AZIENDA_CESSATA);
      if (aDAO.controllaAziendaFineValidita(anagAziendaVO.getIdAnagAzienda()))
        throw new SolmrException(AnagErrors.ERR_REV_MANDATO_AZIENDA_STORICIZZATA);
      // Controllo che se la delega è presente...
      if (delegaAnagrafeVO != null)
      {
        //se intermediario devo controllare se la delega è la mia
        if(ruoloUtenza.isUtenteIntermediario())
        {
          /*if (aziendaVO.isPossiedeDelegaAttiva()
              && (profile.getRuoloUtenza().getCodiceEnte().equalsIgnoreCase(aziendaVO.getDelegaVO().getCodiceFiscaleIntermediario()) || SolmrConstants.FLAG_S.equalsIgnoreCase(delegaAnagrafeVO
                  .getFlagFiglio())))
          {
            conferma = true;
          }*/
          
          //competenza esclusiva della PA!!!
          if(!aDAO.controllaDelegaObbligoFascicolo(anagAziendaVO))
          {
            throw new SolmrException((String)AnagErrors.get("ERR_REG_MANDATO_NON_OBBLIGATORIO"));
          }
        }
        
        if(ruoloUtenza.isUtenteProvinciale())
        {
          if(!(ruoloUtenza.getCodiceEnte() != null 
            && ruoloUtenza.getCodiceEnte().equals(anagAziendaVO.getProvCompetenza())
            || anagAziendaVO.getProvCompetenza() == null))
          {                
            throw new SolmrException(AnagErrors.ERR_REV_MANDATO_AZIENDA_DIFF_PROV);
          }
        }
      }
    }
    catch (SolmrException exc)
    {
      throw exc;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return conferma;
  }

  public boolean controllaObbligoFascicolo(AnagAziendaVO aziendaVO) throws Exception
  {
    try
    {
      return aDAO.controllaObbligoFascicolo(aziendaVO);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Questo metodo controlla se è presente una delega per questa azienda
   * relativamente al procedimento di anagrafe. Se è presente restituisce true.
   * Se non è presente restituisce false
   * 
   * @param aziendaVO
   *          AnagAziendaVO
   * @throws Exception
   * @return boolean
   */
  public boolean controllaPresenzaDelega(AnagAziendaVO aziendaVO) throws Exception
  {
    try
    {
      return aDAO.controllaPresenzaDelega(aziendaVO);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }

  }

  // INIZIO modifica cessa azienda con controlli se procedimenti
  // attivi!!!!!!!!!!!!!!!!!!
  public void checkDataCessazione(Long anagAziendaPK, String dataCessazione) throws SolmrException
  {
    try
    {
      // Il 2 parametro impostato a false significa che la ricerca viene fatta
      // su ID_ANAGRAFICA_AZIENDA, il terzo parametro viene ignorato
      AnagAziendaVO anagVO = aDAO.findByPrimaryKey(anagAziendaPK, false, null);

      if (anagVO.getDataCessazione() != null)
        throw new SolmrException(AnagErrors.AZIENDA_CESSATA);

    }
    catch (SolmrException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new SolmrException(e.getMessage());
    }
  }

  public void cessaAzienda(AnagAziendaVO anagVO, Date dataCess, String causale, long idUtenteAggiornamento) throws SolmrException
  {
    try
    {
      // Cessazione aziende collegate
      // caso azienda è padre      
      aDAO.updateAziendaCollegataCessazione(anagVO.getIdAzienda().longValue(), 
          idUtenteAggiornamento, true);
      

      // caso azienda è figlia
      aDAO.updateAziendaCollegataCessazione(anagVO.getIdAzienda().longValue(), 
          idUtenteAggiornamento, false);
     

      // cessazione azienda su DB_ANAGRAFICA_AZIENDA
      anagVO.setCausaleCessazione(causale);
      anagVO.setDataCessazione(dataCess);
      // Effettuo la cessazione di DB_ANAGRAFICA_AZIENDA
      aDAO.updateAnagAziendaForCessazione(anagVO.getIdAnagAzienda(), anagVO, idUtenteAggiornamento);
      
     
      
      
      //Controllo sui possessi delle macchine!!
      macchineAgricoleGaaDAO.storicizzaPossessoMacchineAzienda(anagVO.getIdAzienda().longValue(), 
          idUtenteAggiornamento);     
      
      
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }

  // FINE modifica cessa azienda con controlli se procedimenti
  // attivi!!!!!!!!!!!!!!!!!!

  // Metodo per effettuare la modifica della sede legale
  public void updateSedeLegale(AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento) throws SQLException, Exception, NotFoundException, DataAccessException, DataControlException
  {
    try
    {
      aDAO.updateSedeLegale(anagAziendaVO, idUtenteAggiornamento);
    }
    catch (ObjectNotFoundException onfe)
    {
      throw new Exception(onfe.getMessage());
    }
    catch (Exception re)
    {
      throw new Exception(re.getMessage());
    }
  }

  // Metodo per effettuare la storicizzazione della sede legale
  public void storicizzaSedeLegale(AnagAziendaVO anagAziendaVO) throws SQLException, Exception, NotFoundException, DataAccessException, DataControlException
  {
    try
    {
      aDAO.insertAnagAzienda(anagAziendaVO).longValue();
      wDAO.storicizzaAzienda(anagAziendaVO.getIdAnagAzienda());
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per recuperare l'azienda attiva
  public AnagAziendaVO findAziendaAttiva(Long idAzienda) throws DataAccessException, NotFoundException, Exception
  {

    AnagAziendaVO anagAziendaVO = null;
    try
    {
      anagAziendaVO = aDAO.findAziendaAttiva(idAzienda);
      DelegaVO dVO = delegaDAO.getDelegaByAziendaAndIdProcedimento(idAzienda, (Long) SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
      anagAziendaVO.setDelegaVO(dVO);
      anagAziendaVO.setPossiedeDelegaAttiva(aDAO.controllaPresenzaDelega(anagAziendaVO));
      
      //controlla se è figlio di qualche azienda sulla tabella DB_AZIENDE_COLLEGATE
      anagAziendaVO.setFlagEnteAppartenenza(aDAO.hasEntiAppartenenza(anagAziendaVO.getIdAzienda().longValue()));
      //***************************************************
      
      
      
    }
    catch (SolmrException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (NotFoundException nfe)
    {
      throw new Exception(nfe.getMessage());
    }
    return anagAziendaVO;
  }

  public void checkCUAAandCodFiscale(String cuaa, String partitaIVA) throws DataAccessException, Exception, SolmrException
  {
    try
    {
      if (wDAO.checkIsCUAAAlreadyPresent(cuaa) != null)
        throw new SolmrException(AnagErrors.CUAA_GIA_ESISTENTE);

      if (wDAO.checkIsPivaAlreadyPresent(partitaIVA) != null)
        throw new SolmrException(AnagErrors.PIVA_GIA_ESISTENTE);
    }
    catch (Exception exc)
    {
      throw new SolmrException(exc.getMessage());
    }
  }

  public CodeDescription[] getListCuaaAttiviProvDestByIdAzienda(Long idAzienda) throws Exception
  {
	try{
	  return wDAO.getListCuaaAttiviProvDestByIdAzienda(idAzienda);
	}
	catch(DataAccessException dae) {
	  throw new Exception(dae.getMessage());
	}
  }
  
  public AnagAziendaVO getAziendaCUAAandCodFiscale(String cuaa, String partitaIVA) throws DataAccessException, Exception, SolmrException
  {
    try
    {
      AnagAziendaVO anagAziendaVO = null;
      Long idAnagraficaAzienda = wDAO.checkIsCUAAAlreadyPresent(cuaa);
      if (idAnagraficaAzienda == null)
        idAnagraficaAzienda = wDAO.checkIsPivaAlreadyPresent(partitaIVA);
      try
      {
        if (idAnagraficaAzienda != null)
        {
          anagAziendaVO = aDAO.findByPrimaryKey(idAnagraficaAzienda, false, null);
          //controlla se è figlio di qualche azienda sulla tabella DB_AZIENDE_COLLEGATE
          anagAziendaVO.setFlagEnteAppartenenza(aDAO.hasEntiAppartenenza(anagAziendaVO.getIdAzienda().longValue()));
          //***************************************************
        }
      }
      catch (NotFoundException ne)
      {
      }
      return anagAziendaVO;
    }
    catch (Exception exc)
    {
      throw new SolmrException(exc.getMessage());
    }
  }

  public void checkPartitaIVA(String partitaIVA, Long idAzienda) throws DataAccessException, Exception, SolmrException
  {
    try
    {
      if (wDAO.checkIsPivaPresent(partitaIVA, idAzienda) != null)
        throw new SolmrException(AnagErrors.PIVA_GIA_ESISTENTE);
    }
    catch (Exception exc)
    {
      throw new SolmrException(exc.getMessage());
    }
  }

  public AnagAziendaVO getAltraAziendaFromPartitaIVA(String partitaIVA, Long idAzienda) throws DataAccessException, Exception, SolmrException
  {
	SolmrLogger.debug(this, "BEGIN getAltraAziendaFromPartitaIVA");
    try
    {
      AnagAziendaVO anagAziendaVO = null;
      
      Long idAnagraficaAzienda = wDAO.checkIsPivaPresent(partitaIVA, idAzienda);
      try
      {
        if (idAnagraficaAzienda != null)
        {
          SolmrLogger.debug(this, "-- idAnagraficaAzienda != null");
          anagAziendaVO = aDAO.findByPrimaryKey(idAnagraficaAzienda, false, null);
          //controlla se è figlio di qualche azienda sulla tabella DB_AZIENDE_COLLEGATE
          anagAziendaVO.setFlagEnteAppartenenza(aDAO.hasEntiAppartenenza(anagAziendaVO.getIdAzienda().longValue()));
          //***************************************************
        }
      }
      catch (NotFoundException ne)
      {
      }
      return anagAziendaVO;
    }
    catch (Exception exc)
    {
      throw new SolmrException(exc.getMessage());
    }
    finally{
    	SolmrLogger.debug(this, "END getAltraAziendaFromPartitaIVA");
    }
  }

  public void checkIsCUAAPresent(String cuaa, Long idAzienda) throws Exception, SolmrException
  {
    try
    {
      wDAO.checkIsCUAAPresent(cuaa, idAzienda);
    }
    catch (Exception exc)
    {
      throw new SolmrException(exc.getMessage());
    }
  }

  public void checkCUAA(String cuaa) throws DataAccessException, Exception, SolmrException
  {
    try
    {
      if (wDAO.checkIsCUAAAlreadyPresent(cuaa) != null)
        throw new SolmrException(AnagErrors.CUAA_GIA_ESISTENTE);
    }
    catch (Exception exc)
    {
      throw new SolmrException(exc.getMessage());
    }
  }

  public PersonaFisicaVO getPersonaFisica(String cuaaIVA) throws DataAccessException, Exception, SolmrException
  {
    try
    {
      return wDAO.getPersonaFisicaFromCUAA(cuaaIVA);
    }
    catch (Exception exc)
    {
      throw new SolmrException(exc.getMessage());
    }
  }

  public Long insertAzienda(AnagAziendaVO aaVO, PersonaFisicaVO pfVO, UteVO ute, 
      long idUtenteAggiornamento, SianFascicoloResponseVO sianFascicoloResponseVO) throws DataAccessException,
      Exception, SolmrException
  {
	SolmrLogger.debug(this, "BEGIN insertAzienda");
    Long idAnagAzienda = null;
    try
    {
      /**
       * Se è un'azienda provvisoria non devo controllare se il CUAA è già
       * esistente o meno
       */
      Integer idTipoAzienda = null;
      idTipoAzienda = Integer.decode(aaVO.getTipiAzienda());

      if (!aaVO.isFlagAziendaProvvisoria() && isFlagUnivocitaAzienda(idTipoAzienda))
      {
        checkCUAAandCodFiscale(aaVO.getCUAA(), aaVO.getPartitaIVA());
      }

      Long idOpr = null;
      Date dataAperFasc = null;
      Date dataChiusFasc = null;
      String codRet = null;
      SianTrovaFascicoloVO sianTrovaFascicoloVO = null;

      if (sianFascicoloResponseVO != null)
      {
        codRet = sianFascicoloResponseVO.getCodRet();
        sianTrovaFascicoloVO = (SianTrovaFascicoloVO)sianFascicoloResponseVO.getContenuto();
      }

      
      if (sianFascicoloResponseVO != null 
        && (SolmrConstants.SIAN_CODICE_NO_FASCICOLI.equals(codRet) 
            || sianTrovaFascicoloVO != null))
      {
        // Se il richiamo al ws Trova Fascicolo è terminato con
        // esito positivo, e il fascicolo non risulta di
        // competenza di alcun OPR (id_opr non valorizzato),
        // valorizzare i seguenti dati della tabella db_azienda
        // Id_opr 5.
        // Data_apertura_fascicolo sysdate
        // Data_chiusura_fascicolo null
        if (SolmrConstants.SIAN_CODICE_NO_FASCICOLI.equals(codRet) 
            || sianTrovaFascicoloVO.getOrganismoPagatore() == null)
        {
          idOpr = new Long(SolmrConstants.ID_OPR_PIEMONTE);
          dataAperFasc = new Date(System.currentTimeMillis());
        }
        else
        {
          try
          {
            dataAperFasc = DateUtils.parseDate(StringUtils.parseDateFieldToEuropeStandard("yyyyMMdd", "dd/MM/yyyy", sianTrovaFascicoloVO.getDataAperturaFascicolo()));
          }
          catch (Exception e)
          {
            SolmrLogger.error(this, "DateUtils.parseDate(StringUtils.parseDateFieldToEuropeStandard(yyyyMMdd, dd/MM/yyyy,oprResponse.getRisposta20().getDataAperturaFascicolo()));" + e.toString());
          }
          // dataChiusFasc deve essere impostata a null
          // come richiesto da segnalazione SMRANAG-216 CU-GAA04 Nuova azienda
          /*
           * try { dataChiusFasc = DateUtils.parseDate(StringUtils.
           * parseDateFieldToEuropeStandard( "yyyyMMdd", "dd/MM/yyyy",
           * oprResponse.getRisposta10().getDataChiusuraFascicolo())); }
           * catch(Exception e) { SolmrLogger.error(this,
           * "DateUtils.parseDate(StringUtils.parseDateFieldToEuropeStandard(yyyyMMdd,
           * dd/MM/yyyy,oprResponse.getRisposta10().getDataChiusuraFascicolo()));" +
           * e.toString()); }
           */
          idOpr = sianDAO.getIdOprFormSianCode(sianTrovaFascicoloVO.getOrganismoPagatore());
        }

      }

      Long idAzienda = wDAO.insertAzienda(aaVO, idOpr, dataAperFasc, dataChiusFasc);
      aaVO.setIdAzienda(idAzienda);
      //cambiato qui per evitare problemi nell'inserimento nella sezione sede!!!!
      aaVO.setSedelegComune(aaVO.getSedelegIstatComune());
      aaVO.setSedelegEstero(aaVO.getSedelegIstatEstero());
      idAnagAzienda = wDAO.insertAnagraficaAzienda(aaVO, idUtenteAggiornamento);
      // Se è stato indicato l'id_azienda_provenienza ...
      if (aaVO.getIdAziendaProvenienza() != null || aaVO.getIdAziendaSubentro() != null)
      {
        // Inserisco il record sulla tabella DB_AZIENDA_DESTINAZIONE
        AziendaDestinazioneVO aziendaDestinazioneVO = new AziendaDestinazioneVO();
        if (aaVO.getIdAziendaProvenienza() != null)
          aziendaDestinazioneVO.setIdAzienda(aaVO.getIdAziendaProvenienza());
        else
          aziendaDestinazioneVO.setIdAzienda(aaVO.getIdAziendaSubentro());
        aziendaDestinazioneVO.setDataAggiornamento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
        aziendaDestinazioneVO.setIdUtenteAggiornamento(idUtenteAggiornamento);
        aziendaDestinazioneVO.setIdAziendaDiDestinazione(idAzienda);
        aziendaDestinazioneDAO.insertAziendaDestinazione(aziendaDestinazioneVO);
      }
      Long idSoggetto = null;
      if (pfVO.isNewPersonaFisica())
      {
        idSoggetto = wDAO.insertSoggetto(SolmrConstants.FLAG_S);
        pfVO.setIdSoggetto(idSoggetto);
        wDAO.insertPersonaFisica(pfVO, idUtenteAggiornamento);
      }
      else
      {
        // Se l'utente ha deciso di storicizzare la modifica della persona
        // fisica
        if (pfVO.isStoricizzaResidenza())
        {
          storicizzaDatiResidenza(pfVO, idUtenteAggiornamento);
          idSoggetto = pfVO.getIdSoggetto();
        }
        // Altrimenti effettuo solo l'update della persona fisica
        else
        {
          wDAO.updatePersonaFisica(pfVO.getIdPersonaFisica(), pfVO, idUtenteAggiornamento);
          idSoggetto = pfVO.getIdSoggetto();
        }
      }
      wDAO.insertContitolare(idAzienda, idSoggetto);
      if (aaVO.getUnitaProduttiva().equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        CodeDescription uteAttivitaATECO = new CodeDescription();
        uteAttivitaATECO = aaVO.getTipoAttivitaATECO();
        ute.setTipoAttivitaATECO(uteAttivitaATECO);
        ute.setTipoAttivitaOTE(aaVO.getTipoAttivitaOTE());
        ute.setIndirizzo(aaVO.getSedelegIndirizzo());
        ute.setIstat(aaVO.getSedelegComune());
        ute.setCap(aaVO.getSedelegCAP());
        ute.setIdAzienda(idAzienda);
        wDAO.insertUTE(ute, idUtenteAggiornamento);
      }
      // Non inserisco più la delega in automatico se l'utente è un
      // intermediario
      /*
       * if(aaVO.getIdIntermediarioDelegato() != null) {
       * wDAO.insertDelega(aaVO,prof); }
       */
      /**
       * Se è valorizzato idAziendaProvenienza chiamo la procedura plsql per il
       * ribaltamento
       */
      if (aaVO.getIdAziendaSubentro() != null || aaVO.getIdAziendaProvenienza() != null)
        wDAO.ribaltamentoPLQSL(aaVO);

      /**
       * La chiamata alla stored procedure deve essere fatta solo se: L'azienda
       * non è di tipo provvisorio (FLAG_AZIENDA_PROVVISORIA su DB_AZIENDA is
       * null oppure 'N') Non c'è un'azienda di provenienza
       * (ID_AZIENDA_PROVENIENZA su DB_AZIENDA is null)
       */
      // Commentato metodo per richiesta Teresa Martone in data 10/11/2011
      /*if (!aaVO.isFlagAziendaProvvisoria() || (aaVO.getIdAziendaProvenienza() == null && aaVO.getIdAziendaSubentro() == null))
        wDAO.ribaltamentoConsistenzaPLQSL(idAzienda.longValue());*/
      
      
      //inserisco tutte quelle dell'interfaccia utente
      @SuppressWarnings("unchecked")
      Vector<AziendaAtecoSecVO> vAziendeAtecoSec = aaVO.getVAziendaATECOSec();
      if(vAziendeAtecoSec != null)
      {
        for(int i=0;i<vAziendeAtecoSec.size();i++)
        {
          AziendaAtecoSecVO aziendaAtecoSecVO = (AziendaAtecoSecVO)vAziendeAtecoSec.get(i);
          aziendaAtecoSecVO.setIdAzienda(aaVO.getIdAzienda().longValue());
          aziendaAtecoSecVO.setDataInizioValidita(new Date());
          aGaaDAO.insertAziendaAtecoSec(aziendaAtecoSecVO);
        }
      }

    }
    catch (Exception exc)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(exc.getMessage());
    }
    finally{
      SolmrLogger.debug(this, "END insertAzienda");
    }
    return idAnagAzienda;
  }

  public void insertUte(UteVO uVO, long idUtenteAggiornamento) throws DataAccessException, Exception, SolmrException
  {
    try
    {
      String codiceOte = "";
      String descrizioneOte = "";
      String codiceAteco = "";
      String descrizioneAteco = "";
      if (uVO.getCodeOte() != null && !uVO.getCodeOte().equals(""))
      {
        codiceOte = uVO.getCodeOte();
      }

      if (uVO.getDescOte() != null && !uVO.getDescOte().equals(""))
      {
        descrizioneOte = uVO.getDescOte();
      }

      if (uVO.getCodeAteco() != null && !uVO.getCodeAteco().equals(""))
      {
        codiceAteco = uVO.getCodeAteco();
      }

      if (uVO.getDescAteco() != null && !uVO.getDescAteco().equals(""))
      {
        descrizioneAteco = uVO.getDescAteco();
      }

      Vector<CodeDescription> ote = null;
      CodeDescription idOte = null;
      if (Validator.isNotEmpty(codiceOte) && Validator.isNotEmpty(descrizioneOte))
      {
        ote = getTipiAttivitaOTE(codiceOte, descrizioneOte);
        idOte = (CodeDescription) ote.firstElement();
        uVO.setIdOte(idOte.getCode().toString());
      }
      else
      {
        uVO.setIdOte(null);
      }
      Vector<CodeDescription> ateco = null;
      CodeDescription idAteco = null;
      if (Validator.isNotEmpty(codiceAteco) && Validator.isNotEmpty(descrizioneAteco))
      {
        ateco = cDAO.getTipiAttivitaATECO(codiceAteco, descrizioneAteco);
        if(ateco != null)
        {
          idAteco = (CodeDescription) ateco.firstElement();
          uVO.setIdAteco(idAteco.getCode().toString());
        }
      }
      else
      {
        uVO.setIdAteco(null);
      }
      
      Long idUte = wDAO.insertUTE(uVO, idUtenteAggiornamento);
      
      if(uVO.getvUteAtecoSec() != null)
      {
        Vector<AziendaAtecoSecVO> vAzienaAtecoSec = aGaaDAO.getListActiveAziendaAtecoSecByIdAzienda(uVO.getIdAzienda());
        for(int i=0;i<uVO.getvUteAtecoSec().size();i++)
        {
          uVO.getvUteAtecoSec().get(i).setIdUte(idUte);
          uVO.getvUteAtecoSec().get(i).setDataInizioValidita(new Date());
          uteDAO.insertUteAtecoSecondari(uVO.getvUteAtecoSec().get(i));
          
          
          //Controllo che non sia già presente nell'azienda eventualmente lo aggiungo!!
          if(vAzienaAtecoSec == null)
          {
            //Aggiungo tutto
            AziendaAtecoSecVO aziendaAtecoSecVO = new AziendaAtecoSecVO();
            aziendaAtecoSecVO.setIdAzienda(uVO.getIdAzienda());
            aziendaAtecoSecVO.setIdAttivitaAteco(uVO.getvUteAtecoSec().get(i).getIdAttivitaAteco());
            aziendaAtecoSecVO.setDataInizioValidita(new Date());
            aGaaDAO.insertAziendaAtecoSec(aziendaAtecoSecVO);
          }
          else
          {
            //Controllo se nn c'e' aggiungo!!!
            Boolean trovato = false;
            for(int h=0;h<vAzienaAtecoSec.size();h++)
            {
              if(vAzienaAtecoSec.get(h).getIdAttivitaAteco() == uVO.getvUteAtecoSec().get(i).getIdAttivitaAteco())
              {
                trovato = true;
                break;
              }                      
            }
            
            if(!trovato)
            {
              AziendaAtecoSecVO aziendaAtecoSecVO = new AziendaAtecoSecVO();
              aziendaAtecoSecVO.setIdAzienda(uVO.getIdAzienda());
              aziendaAtecoSecVO.setIdAttivitaAteco(uVO.getvUteAtecoSec().get(i).getIdAttivitaAteco());
              aziendaAtecoSecVO.setDataInizioValidita(new Date());
              aGaaDAO.insertAziendaAtecoSec(aziendaAtecoSecVO);                      
            }
            
          }
          
          
          
        }
      }
      
      
      
    }
    catch (Exception exc)
    {
      throw new SolmrException(exc.getMessage());
    }
  }

  public void countUteByAziendaAndComune(Long idAzienda, String comune) throws DataAccessException, Exception, SolmrException
  {

    try
    {
      aDAO.countUteByAziendaAndComune(idAzienda, comune);
    }
    catch (Exception exc)
    {
      throw new SolmrException(exc.getMessage());
    }
  }

  public void cambiaRappresentanteLegale(Long aziendaPK, PersonaFisicaVO personaVO, long idUtenteAggiornamento) throws Exception, SolmrException
  {
    PersonaFisicaVO pfVO = null;
    try
    {
      pfVO = wDAO.getPersonaFisicaFromCUAA(personaVO.getCodiceFiscale());
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    catch (SolmrException ex)
    {
      // Non faccio niente. Eseguo successivamente il controllo su pfVO.
    }
    try
    {
      if (pfVO == null)
      { // Inserire nuova persona fisica
        Long idSoggetto = wDAO.insertSoggetto(SolmrConstants.FLAG_S);
        personaVO.setIdSoggetto(idSoggetto);
        wDAO.insertPersonaFisica(personaVO, idUtenteAggiornamento);
      }
      else
      { // Update dati persona fisica
        personaVO.setIdSoggetto(pfVO.getIdSoggetto());
        personaVO.setIdPersonaFisica(pfVO.getIdPersonaFisica());
        personaVO.setDataInizioResidenza(pfVO.getDataInizioResidenza());
        if (personaVO.isStoricizzaResidenza())
        {
          storicizzaDatiResidenza(personaVO, idUtenteAggiornamento);
        }
        else
        {
          wDAO.updatePersonaFisica(pfVO.getIdPersonaFisica(), personaVO, idUtenteAggiornamento);
        }
      }
      wDAO.cessaRappresentanteLegaleDate(aziendaPK, personaVO.getDataInizioRuolo());
      wDAO.cessaRuoliAttiviRappresentanteLegale(aziendaPK, personaVO.getIdSoggetto());
      wDAO.insertContitolareDate(aziendaPK, personaVO.getIdSoggetto(), personaVO.getDataInizioRuolo());
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }

  public ComuneVO getComuneByCUAA(String cuaa) throws Exception, SolmrException
  {
    try
    {
      return cDAO.getComuneByCUAA(cuaa);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  public ComuneVO getComuneByISTAT(String istat) throws Exception, SolmrException
  {
    try
    {
      return cDAO.getComuneByISTAT(istat);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  // Metodo per controllare il valore del flag CCIAA di una forma giuridica
  public String getFormaGiuridicaFlagCCIAA(Long idFormaGiuridica) throws Exception, SolmrException
  {

    try
    {
      return wDAO.getFormaGiuridicaFlagCCIAA(idFormaGiuridica);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
  }

  // Metodo per recuperare il comune a partire dal codice fiscale
  public String getComuneFromCF(String codiceFiscale) throws Exception, SolmrException
  {
    try
    {
      return cDAO.getComuneFromCF(codiceFiscale);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    catch (NotFoundException nfe)
    {
      throw new SolmrException(nfe.getMessage());
    }
  }

  public void storicizzaAzienda(AnagAziendaVO anagVO, long idUtenteAggiornamento) throws SolmrException, Exception
  {
	SolmrLogger.debug(this, "BEGIN storicizzaAzienda");
    try
    {
      SolmrLogger.debug(this,"-- storicizzaAzienda");
      wDAO.storicizzaAzienda(anagVO.getIdAnagAzienda());
      SolmrLogger.debug(this,"-- insertAnagraficaAzienda");
      wDAO.insertAnagraficaAzienda(anagVO, idUtenteAggiornamento);
      
      if(anagVO.isModificaAtecoSec())
      {
        Vector<AziendaAtecoSecVO> vAziendaAtecoSecDB = aGaaDAO.getListActiveAziendaAtecoSecByIdAzienda(
              anagVO.getIdAzienda().longValue());
        
        if(vAziendaAtecoSecDB != null)
        {
          Vector<ConsistenzaVO> vDichCons = consDAO.getDichiarazioniConsistenza(anagVO.getIdAzienda());
          for(int i=0;i<vAziendaAtecoSecDB.size();i++)
          {
            boolean flagTrovata = false;
            AziendaAtecoSecVO aziendaAtecoSecDBVO = (AziendaAtecoSecVO)vAziendaAtecoSecDB.get(i);
            if ((vDichCons != null) && (vDichCons.size() > 0))
            {
              for (int j = 0; j < vDichCons.size(); j++)
              {
                ConsistenzaVO consVO = (ConsistenzaVO) vDichCons.get(j);
                if (aziendaAtecoSecDBVO.getDataInizioValidita().before(
                    consVO.getDataInserimentoDichiarazione()))
                {
                  flagTrovata = true;
                  break;
                }
              }
            }
            
            if(flagTrovata)
            {
              aziendaAtecoSecDBVO.setDataFineValidita(new Date());
              aGaaDAO.updateAziendaAtecoSec(aziendaAtecoSecDBVO);
            }
            else
            {
              aGaaDAO.deleteAziendaAtecoSec(aziendaAtecoSecDBVO.getIdAziendaAtecoSec());
            }
            
          }
          
        }
        
        //inserisco tutte quelle dell'interfaccia utente
        @SuppressWarnings("unchecked")
        Vector<AziendaAtecoSecVO> vAziendeAtecoSec = anagVO.getVAziendaATECOSec();
        if(vAziendeAtecoSec != null)
        {
          for(int i=0;i<vAziendeAtecoSec.size();i++)
          {
            AziendaAtecoSecVO aziendaAtecoSecVO = (AziendaAtecoSecVO)vAziendeAtecoSec.get(i);
            aziendaAtecoSecVO.setIdAzienda(anagVO.getIdAzienda().longValue());
            aziendaAtecoSecVO.setDataInizioValidita(new Date());
            aGaaDAO.insertAziendaAtecoSec(aziendaAtecoSecVO);
          }
        }        
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
    catch (Exception ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
    finally{
    	SolmrLogger.debug(this, "END storicizzaAzienda");
    }
  }

  public void updateAzienda(AnagAziendaVO anagVO) throws Exception, SolmrException
  {
    try
    {

      wDAO.updateAzienda(anagVO);
      
      if(anagVO.isModificaAtecoSec())
      {
        Vector<AziendaAtecoSecVO> vAziendaAtecoSecDB = aGaaDAO.getListActiveAziendaAtecoSecByIdAzienda(
              anagVO.getIdAzienda().longValue());
        
        if(vAziendaAtecoSecDB != null)
        {
          Vector<ConsistenzaVO> vDichCons = consDAO.getDichiarazioniConsistenza(anagVO.getIdAzienda());
          for(int i=0;i<vAziendaAtecoSecDB.size();i++)
          {
            boolean flagTrovata = false;
            AziendaAtecoSecVO aziendaAtecoSecDBVO = (AziendaAtecoSecVO)vAziendaAtecoSecDB.get(i);
            if ((vDichCons != null) && (vDichCons.size() > 0))
            {
              for (int j = 0; j < vDichCons.size(); j++)
              {
                ConsistenzaVO consVO = (ConsistenzaVO) vDichCons.get(j);
                if (aziendaAtecoSecDBVO.getDataInizioValidita().before(
                    consVO.getDataInserimentoDichiarazione()))
                {
                  flagTrovata = true;
                  break;
                }
              }
            }
            
            if(flagTrovata)
            {
              aziendaAtecoSecDBVO.setDataFineValidita(new Date());
              aGaaDAO.updateAziendaAtecoSec(aziendaAtecoSecDBVO);
            }
            else
            {
              aGaaDAO.deleteAziendaAtecoSec(aziendaAtecoSecDBVO.getIdAziendaAtecoSec());
            }
            
          }
          
        }
        
        //inserisco tutte quelle dell'interfaccia utente
        @SuppressWarnings("unchecked")
        Vector<AziendaAtecoSecVO> vAziendeAtecoSec = anagVO.getVAziendaATECOSec();
        if(vAziendeAtecoSec != null)
        {
          for(int i=0;i<vAziendeAtecoSec.size();i++)
          {
            AziendaAtecoSecVO aziendaAtecoSecVO = (AziendaAtecoSecVO)vAziendeAtecoSec.get(i);
            aziendaAtecoSecVO.setIdAzienda(anagVO.getIdAzienda().longValue());
            aziendaAtecoSecVO.setDataInizioValidita(new Date());
            aGaaDAO.insertAziendaAtecoSec(aziendaAtecoSecVO);
          }
        }
        
      }
      
      
     
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
    catch (Exception ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }

  }

  public void utenteConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda) throws Exception, SolmrException
  {
    try
    {
      aDAO.utenteConDelega(utenteAbilitazioni, idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    catch (DataControlException dce)
    {
      throw new SolmrException(dce.getMessage());
    }
  }

  public void updateRappLegale(PersonaFisicaVO pfVO, long idUtenteAggiornamento) throws Exception, SolmrException
  {
    try
    {
      if (pfVO.isStoricizzaResidenza())
      {
        storicizzaDatiResidenza(pfVO, idUtenteAggiornamento);
      }
      else
      {
        wDAO.updateRappLegale(pfVO, idUtenteAggiornamento);
      }
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  // Metodo per effettuare il subentro del nuovo titolare
  public Long updateTitolareAzienda(AnagAziendaVO anagAziendaVO, 
      PersonaFisicaVO personaTitolareOldVO, PersonaFisicaVO personaTitolareNewVO, 
      long idUtenteAggiornamento) throws Exception,  SolmrException
  {
    Long result = null;
    try
    {
      // Se la data inizio validita dell'azienda è diversa da quella odierna
      // storicizzo la modifica
      if (!DateUtils.isToday(anagAziendaVO.getDataInizioVal()))
      {
        wDAO.storicizzaAzienda(anagAziendaVO.getIdAnagAzienda());
        String motivoModifica = "passaggio di proprietà da " + personaTitolareOldVO.getCognome().toUpperCase() + " " + personaTitolareOldVO.getNome().toUpperCase();
        if (personaTitolareOldVO.getCodiceFiscale() != null)
        {
          motivoModifica += " (" + personaTitolareOldVO.getCodiceFiscale().toUpperCase() + ") ";
        }
        motivoModifica += "a " + personaTitolareNewVO.getCognome().toUpperCase() + " " + personaTitolareNewVO.getNome().toUpperCase() + " (" + personaTitolareNewVO.getCodiceFiscale().toUpperCase()
            + ")";

        if (anagAziendaVO.getDataAvvenutoPassaggio() != null && !anagAziendaVO.getDataAvvenutoPassaggio().equals(""))
        {
          motivoModifica += " avvenuto il " + anagAziendaVO.getDataAvvenutoPassaggio();
        }
        anagAziendaVO.setMotivoModifica(motivoModifica);
        result = wDAO.insertAnagraficaAzienda(anagAziendaVO, idUtenteAggiornamento);
        wDAO.cessaRappresentanteLegale(anagAziendaVO.getIdAzienda());
        if (personaTitolareNewVO.isNewPersonaFisica())
        {
          Long idSoggetto = wDAO.insertSoggetto(SolmrConstants.FLAG_S);
          personaTitolareNewVO.setIdSoggetto(idSoggetto);
          wDAO.insertPersonaFisica(personaTitolareNewVO, idUtenteAggiornamento);
        }
        else
        {
          wDAO.cessaRuoliAttiviRappresentanteLegale(anagAziendaVO.getIdAzienda(), personaTitolareNewVO.getIdSoggetto());
          if (personaTitolareNewVO.isStoricizzaResidenza())
          {
            storicizzaDatiResidenza(personaTitolareNewVO, idUtenteAggiornamento);
          }
          else
          {
            wDAO.updatePersonaFisica(personaTitolareNewVO.getIdPersonaFisica(), personaTitolareNewVO, idUtenteAggiornamento);
          }
        }
        if (personaTitolareOldVO.getIdRuolo().compareTo(new Long(SolmrConstants.TIPORUOLO_COADIUVANTE)) == 0)
        {
          wDAO.insertContitolareRuolo(anagAziendaVO.getIdAzienda(), personaTitolareOldVO.getIdSoggetto(), personaTitolareOldVO.getIdRuolo());
        }
        wDAO.insertContitolare(anagAziendaVO.getIdAzienda(), personaTitolareNewVO.getIdSoggetto());
      }
      else
      {
        String motivoModifica = "passaggio di proprietà da " + personaTitolareOldVO.getCognome().toUpperCase() + " " + personaTitolareOldVO.getNome().toUpperCase();
        if (personaTitolareOldVO.getCodiceFiscale() != null)
        {
          motivoModifica += " (" + personaTitolareOldVO.getCodiceFiscale().toUpperCase() + ") ";
        }
        motivoModifica += "a " + personaTitolareNewVO.getCognome().toUpperCase() + " " + personaTitolareNewVO.getNome().toUpperCase() + " (" + personaTitolareNewVO.getCodiceFiscale().toUpperCase()
            + ")";

        if (anagAziendaVO.getDataAvvenutoPassaggio() != null && !anagAziendaVO.getDataAvvenutoPassaggio().equals(""))
        {
          motivoModifica += " avvenuto il " + anagAziendaVO.getDataAvvenutoPassaggio();
        }
        anagAziendaVO.setMotivoModifica(motivoModifica);
        anagAziendaVO.setDataFineVal(new Date(System.currentTimeMillis()));
        wDAO.updateAzienda(anagAziendaVO);
        wDAO.cessaRappresentanteLegale(anagAziendaVO.getIdAzienda());
        if (personaTitolareNewVO.isNewPersonaFisica())
        {
          Long idSoggetto = wDAO.insertSoggetto(SolmrConstants.FLAG_S);
          personaTitolareNewVO.setIdSoggetto(idSoggetto);
          wDAO.insertPersonaFisica(personaTitolareNewVO, idUtenteAggiornamento);
        }
        else
        {
          wDAO.cessaRuoliAttiviRappresentanteLegale(anagAziendaVO.getIdAzienda(), personaTitolareNewVO.getIdSoggetto());
          if (personaTitolareNewVO.isStoricizzaResidenza())
          {
            storicizzaDatiResidenza(personaTitolareNewVO, idUtenteAggiornamento);
          }
          else
          {
            wDAO.updatePersonaFisica(personaTitolareNewVO.getIdPersonaFisica(), personaTitolareNewVO, idUtenteAggiornamento);
          }
        }
        if (personaTitolareOldVO.getIdRuolo().compareTo(new Long(SolmrConstants.TIPORUOLO_COADIUVANTE)) == 0)
        {
          wDAO.insertContitolareRuolo(anagAziendaVO.getIdAzienda(), personaTitolareOldVO.getIdSoggetto(), personaTitolareOldVO.getIdRuolo());
        }
        wDAO.insertContitolare(anagAziendaVO.getIdAzienda(), personaTitolareNewVO.getIdSoggetto());
        result = anagAziendaVO.getIdAnagAzienda();
      }
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
    catch (Exception ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
    return result;
  }

  // Metodo per recuperare il flag partita iva relativo ad una specifica forma
  // giuridica
  public String getFlagPartitaIva(Long idTipoFormaGiuridica) throws Exception, SolmrException
  {
    String flagPartitaIva = null;
    try
    {
      flagPartitaIva = cDAO.getFlagPartitaIva(idTipoFormaGiuridica);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    return flagPartitaIva;
  }
  
  
  public String getObbligoGfFromFormaGiuridica(Long idTipoFormaGiuridica)
      throws Exception, SolmrException
  {
    String obbligoGf = null;
    try
    {
      obbligoGf = cDAO.getObbligoGfFromFormaGiuridica(idTipoFormaGiuridica);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    return obbligoGf;
  }

  public Long getIdTipologiaAziendaByFormaGiuridica(Long idTipoFormaGiuridica, Boolean flagAziendaProvvisoria) throws Exception, SolmrException
  {
    try
    {
      return cDAO.getIdTipologiaAziendaByFormaGiuridica(idTipoFormaGiuridica, flagAziendaProvvisoria);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
  }

  // Metodo per recupare l'id della forma giuridica nostra dato il codice di
  // AAEP
  public Long getIdTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP) throws Exception, SolmrException
  {
    try
    {
      return cDAO.getIdTipoFormaGiuridica(idTipoFormaGiuridicaAAEP);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
  }

  // Metodo per recupare la descrizione di una nostra forma giuridica data il
  // codice
  // della forma giuridica di AAEP
  public String getDescTipoFormaGiuridica(String idTipoFormaGiuridicaAAEP) throws Exception, SolmrException
  {
    try
    {
      return cDAO.getDescTipoFormaGiuridica(idTipoFormaGiuridicaAAEP);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
  }

  // Metodo per verificare se la provincia REA inserita è valida oppure no
  public boolean isProvinciaReaValida(String siglaProvincia) throws Exception, SolmrException
  {
    boolean isValida = false;
    try
    {
      isValida = cDAO.isProvinciaReaValida(siglaProvincia);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    return isValida;
  }

  public boolean isFlagUnivocitaAzienda(Integer idTipoAzienda) throws Exception, SolmrException
  {
    boolean isUnivoca = false;
    try
    {
      isUnivoca = cDAO.isFlagUnivocitaAzienda(idTipoAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return isUnivoca;
  }

  // Metodo per reperire il rappresentante legale di una società a partire
  // dall'id_anagrafica_azienda
  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAzienda(Long idAnagAzienda) throws Exception, SolmrException
  {
    try
    {
      return wDAO.getRappresentanteLegaleFromIdAnagAzienda(idAnagAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    catch (NotFoundException nfe)
    {
      throw new SolmrException(nfe.getMessage());
    }
  }
  
  
  public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) 
    throws Exception, SolmrException
  {
    try
    {
      return wDAO.getRappresentanteLegaleFromIdAnagAziendaAndDichCons(idAnagAzienda,dataDichiarazione);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
  }
  
  public Vector<PersonaFisicaVO> getVAltriSoggettiFromIdAnagAziendaAndDichCons(
      long idAnagAzienda, Date dataDichiarazione) 
    throws Exception, SolmrException
  {
    try
    {
      return wDAO.getVAltriSoggettiFromIdAnagAziendaAndDichCons(idAnagAzienda,dataDichiarazione);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
  }

  public UtenteIrideVO getUtenteIrideById(Long idUtente) throws Exception, SolmrException
  {
    try
    {
      return cDAO.getUtenteIrideById(idUtente);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public AnagAziendaVO findAziendaAttivabyCriterio(String cuaa, String partitaIva) throws Exception, SolmrException
  {
    AnagAziendaVO anagAziendaVO = null;
    try
    {
      anagAziendaVO = aDAO.findAziendaAttivabyCriterio(cuaa, partitaIva);
      anagAziendaVO.setPossiedeDelegaAttiva(aDAO.controllaPresenzaDelega(anagAziendaVO));
      
      //controlla se è figlio di qualche azienda sulla tabella DB_AZIENDE_COLLEGATE
      anagAziendaVO.setFlagEnteAppartenenza(aDAO.hasEntiAppartenenza(anagAziendaVO.getIdAzienda().longValue()));
      //***************************************************
      
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    catch (NotFoundException nfe)
    {
      return null;
    }
    return anagAziendaVO;
  }

  /*
   * questo metodo serve solo nel caso in cui all'azienda non sia associato
   * alcun rapp. leg./titolare nel caso della modifica in assenza di dati fa un
   * inserimento nelle tre tabelle coinvolte
   */
  public void insertRappLegaleTitolare(Long idAzienda, PersonaFisicaVO pfVO, long idUtenteAggiornamento) throws Exception, SolmrException
  {
    try
    {
      Long idSoggetto = null;
      idSoggetto = wDAO.insertSoggetto(SolmrConstants.FLAG_S);
      pfVO.setIdSoggetto(idSoggetto);
      wDAO.insertPersonaFisica(pfVO, idUtenteAggiornamento);
      wDAO.insertContitolare(idAzienda, idSoggetto);
    }
    catch (Exception exc)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(exc.getMessage());
    }
  }

  public String getDenominazioneByIdAzienda(Long idAzienda) throws Exception, SolmrException
  {
    String result = null;
    try
    {
      result = aDAO.getDenominazioneByIdAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    catch (NotFoundException nfe)
    {
      throw new SolmrException(nfe.getMessage());
    }
    return result;
  }

  public String getRappLegaleTitolareByIdAzienda(Long idAzienda) throws Exception, SolmrException
  {
    String result = null;
    try
    {
      result = aDAO.getRappLegaleTitolareByIdAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return result;
  }

  public Vector<AnagAziendaVO> findAziendeByIdAziende(Vector<Long> idAziendeVect) throws Exception, SolmrException
  {
    try
    {
      return aDAO.findAziendeByIdAziende(idAziendeVect);
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    catch (SolmrException ex)
    {
      throw ex;
    }
  }

  public String getSiglaProvinciaByIstatProvincia(String istatProvincia) throws Exception, SolmrException
  {
    try
    {
      return cDAO.getSiglaProvinciaByIstatProvincia(istatProvincia);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  /**
   * Metodo che si occupa di registrare il mandato ed eventualmente di inserire
   * e protocollare il documento
   * 
   */
  public Long insertDelegaForMandato(AnagAziendaVO anagAziendaVO, RuoloUtenza ruoloUtenza, DelegaVO delegaVO, DocumentoVO documentoVO) throws Exception, SolmrException
  {
    Long idDelega = null;
    try
    {
      // Per gestire il problema degli accessi contemporanei controllo che non
      // sia stata inserita
      // una delega per l'azienda selezionata
      SolmrLogger.debug(this, "Invocating controllaPresenzaDelega in insertDelegaForMandato in AnagAziendaBean");
      if (aDAO.controllaPresenzaDelega(anagAziendaVO))
      {
        SolmrLogger.debug(this, "Throwing SolmrException in insertDelegaForMandato in AnagAziendaBean with this message: " + (String) AnagErrors.get("ERR_DELEGA_CONTEMPORANEA"));
        throw new SolmrException((String) AnagErrors.get("ERR_DELEGA_CONTEMPORANEA"));
      }
      else
      {
        idDelega = delegaDAO.insertDelega(delegaVO);
        // La gestione del documentale è vincolata alla presenza del CUAA
        // dell'azienda agricola
        // selezionata
        if (Validator.isNotEmpty(anagAziendaVO.getCUAA()))
        {
          // Cerco il vecchio documento relativo alla revoca del mandato di
          // assistenza
          DocumentoVO oldDocumentoVO = documentoDAO.findDocumentoMandatoOrRevocaAssistenza(anagAziendaVO, SolmrConstants.ID_TIPO_DOCUMENTO_REVOCA_MANDATO_ASSISTENZA);
          // Se c'è, lo storicizzo
          if (oldDocumentoVO != null)
          {
            oldDocumentoVO.setIdStatoDocumento(Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_STORICIZZATO));
            oldDocumentoVO.setDataFineValidita(new Date(System.currentTimeMillis()));
            documentoDAO.storicizzaDocumento(oldDocumentoVO, ruoloUtenza.getIdUtente());
          }
          if (documentoVO != null)
          {
            String numeroProtocollo = null;
            try
            {
              // Recupero il numero protocollo
              numeroProtocollo = documentoDAO.getNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
              // Se non lo trovo lo inserisco
              if (numeroProtocollo == null)
              {
                documentoDAO.insertNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
                numeroProtocollo = documentoDAO.getNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
              }
              // Alla fine delle operazioni aggiorno comunque il record e libero
              // la tabella
              documentoDAO.aggiornaProgressivoNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
            }
            catch (DataAccessException dae)
            {
              sessionContext.setRollbackOnly();
              throw new Exception(dae.getMessage());
            }
            catch (SolmrException se)
            {
              try
              {
                numeroProtocollo = documentoDAO.getNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
                // Alla fine delle operazioni aggiorno comunque il record e
                // libero la tabella
                documentoDAO.aggiornaProgressivoNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
              }
              catch (DataAccessException dae)
              {
                sessionContext.setRollbackOnly();
                throw new Exception(dae.getMessage());
              }
            }
            documentoVO.setNumeroProtocollo(numeroProtocollo);
            documentoVO.setDataProtocollo(new Date(System.currentTimeMillis()));
            documentoVO.setDataInizioValidita(delegaVO.getDataInizioMandato());
            documentoDAO.insertDocumento(documentoVO, ruoloUtenza);
          }
        }
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(ex.getMessage());
    }
    return idDelega;
  }

  public AnagraficaAzVO getDatiAziendaPerMacchine(Long idAzienda) throws Exception, SolmrException
  {
    try
    {
      return aDAO.getDatiAziendaPerMacchine(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
    catch (NotFoundException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  public Vector<AnagAziendaVO> getListaStoricoAzienda(Long idAzienda) throws Exception, SolmrException
  {
    Vector<AnagAziendaVO> result = null;
    try
    {
      result = wDAO.getListaStoricoAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    return result;
  }

  // Metodo per effettuare la sostituzione del vecchio soggetto con il nuovo in
  // tutti i legami tra aziende
  // e persona fisiche
  public void changeLegameBetweenPersoneAndAziende(Long newIdSoggetto, Long oldIdSoggetto, Long idAzienda) throws SolmrException, Exception
  {
    try
    {
      wDAO.changeLegameBetweenPersoneAndAziende(newIdSoggetto, oldIdSoggetto, idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per cessare il legame tra una persona e l'azienda
  public void cessaLegameBetweenPersonaAndAzienda(Long idSoggetto, Long idAzienda) throws SolmrException, Exception
  {
    try
    {
      wDAO.cessaLegameBetweenPersonaAndAzienda(idSoggetto, idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception();
    }
  }

  // Metodo per verificare lo stato dell'azienda in relazione alle dichiarazioni
  // di consistenza
  // e alle notifiche
  public void checkStatoAzienda(Long idAzienda) throws SolmrException, Exception
  {

    Date dataUltimaConsistenza = null;
    Date dataMaxDichConsistenza = null;
    String messaggio = "";
    int errorType = 0;

    // Recupero l'azienda
    AnagAziendaVO anagAziendaVO = null;
    try
    {
      anagAziendaVO = aDAO.findByPrimaryKey(idAzienda, true, null);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (NotFoundException nfe)
    {
      throw new Exception(nfe.getMessage());
    }

    // La sezione relativa alle dichiarazioni di consistenza la effettuo solo se
    // data cessazione = null
    // e data fine validità = null.Quest'ultima condizione viene già inclusa
    // nella findByPrimaryKey
    // che estrae solo l'azienda con data fine validità = null
    if (anagAziendaVO != null && anagAziendaVO.getDataCessazione() == null)
    {
      // SEZIONE DICHIARAZIONI DI CONSISTENZA
      try
      {
        dataUltimaConsistenza = fascicoloDAO.getDataUltimoAggiornamentoConsistenza(idAzienda);
        dataMaxDichConsistenza = fascicoloDAO.getMaxDataDichiarazioneConsistenza(idAzienda);
      }
      catch (SolmrException se)
      {
      }

      if (dataUltimaConsistenza != null && dataMaxDichConsistenza != null)
      {
        if (dataUltimaConsistenza.after(dataMaxDichConsistenza))
        {
          // throw new
          // SolmrException((String)AnagErrors.get("ERR_CONSISTENZA_VARIATA"));
          messaggio = (String) AnagErrors.get("ERR_CONSISTENZA_VARIATA") + "%";
          errorType = 1;
        }
      }
    }

    // SEZIONE NOTIFICHE
    Vector<NotificaVO> elencoNotifiche = null;
    NotificaVO notificaVO = new NotificaVO();
    notificaVO.setIdAzienda(idAzienda);
    notificaVO.setIdProcedimentoDestinatario((Long) SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
    try
    {
      elencoNotifiche = notificaDAO.getElencoNotificheByIdAzienda(notificaVO, new Boolean(false), (String) SolmrConstants.get("ORDINAMENTO_NOTIFICHE_GRAVITA"));
    }
    catch (SolmrException se)
    {
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }

    if (elencoNotifiche != null && elencoNotifiche.size() > 0)
    {
      Iterator<NotificaVO> iteraNotifiche = elencoNotifiche.iterator();
      if (iteraNotifiche.hasNext())
      {
        NotificaVO notificaElencoVO = (NotificaVO) iteraNotifiche.next();
        if (notificaElencoVO.getIdTipologiaNotifica().compareTo((Long) SolmrConstants.get("ID_TIPO_TIPOLOGIA_BLOCCANTE")) == 0)
        {
          messaggio += (String) AnagErrors.get("ERR_AZIENDA_BLOCCATA");
          errorType = 2;
        }
        else if (notificaElencoVO.getIdTipologiaNotifica().compareTo((Long) SolmrConstants.get("ID_TIPO_TIPOLOGIA_WARNING")) == 0)
        {
          messaggio += (String) AnagErrors.get("ERR_AZIENDA_WARNING");
          errorType = 3;
        }
        else if (notificaElencoVO.getIdTipologiaNotifica().compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_BLOCCAPROCEDIMENTI) == 0)
        {
          messaggio += AnagErrors.ERR_AZIENDA_BLOCCOPROCEDIMENTO;
          errorType = 5;
        }
        else if (notificaElencoVO.getIdTipologiaNotifica().compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_VARIAZIONECATASTALE) == 0)
        {
          messaggio += AnagErrors.ERR_AZIENDA_VARIAZIONECATASTALE;
          errorType = 6;
        }
        else
        {
          messaggio += (String) AnagErrors.get("ERR_AZIENDA_NOTIFICHE");
          errorType = 4;
        }
      }
      
      Vector<String> vFiltroNotifiche = new Vector<String>();
      for(int i=0;i<elencoNotifiche.size();i++)
      {
        notificaVO = elencoNotifiche.get(i);
        //filtro su tipo e tipologia della notifica
        String notifFiltro = notificaVO.getIdTipologiaNotifica()+"_"+ notificaVO.getIdCategoriaNotifica(); 
        if(!vFiltroNotifiche.contains(notifFiltro))
        {
          vFiltroNotifiche.add(notifFiltro);
          
          messaggio += "*"+notificaVO.getIdTipologiaNotifica()+","+notificaVO.getDescCategoriaNotifica();          
        }
      }
      
      
    }
    
    
    

    if (Validator.isNotEmpty(messaggio))
    {
      SolmrException se = new SolmrException(messaggio);
      se.setErrorType(errorType);
      throw se;
    }
  }

  // Metodo che effettua l'aggiornamento dei dati dell'anagrafe in funzione
  // dell'importazione
  // di quelli dell'anagrafe tributaria
  // e viene usato per storicizzare i dati di residenza a seguito di
  // un'importazione dei dati dal sian. E' prevista una sola
  // storicizzazione al giorno, quindi nel caso di più modifiche dei
  // dati di residenza all'interno della stessa giornata verranno
  // direttamente modificati i dati di residenza sulla tabella
  // dp_persona_fisica( non fare update di data_inizio_residenza)
  public void updateAnagrafe(AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento, 
      PersonaFisicaVO pfVO, boolean isCuaaChanged, PersonaFisicaVO pfVOTributaria, 
      Vector<Long> vIdAtecoTrib) throws Exception
  {
	SolmrLogger.debug(this, "BEGIN updateAnagrafe");
    try
    {
      Date today = DateUtils.parseDate(DateUtils.getCurrent());
      //Se settato a true devo rendere consistenti i dati col rappresentante legale!!! 
      boolean modRappLegale = false;
      CodeDescription tipoFormaGiuridica = anagAziendaVO.getTipoFormaGiuridica();
      if(Validator.isNotEmpty(tipoFormaGiuridica))
      {
        if(tipoFormaGiuridica.getCode().toString().equalsIgnoreCase(
            SolmrConstants.FORMA_GIURIDICA_INDIVIDUALE)
          || tipoFormaGiuridica.getCode().toString().equalsIgnoreCase(
              SolmrConstants.FORMA_GIURIDICA_PERSONA_FISICA_NO_IMPRESA))
        {
          if(isCuaaChanged)
          {
            modRappLegale = true;
          }
        }        
      }
      
      // Se è stata già fatta una storicizzazione effettuo solo la modifica del
      // record
      // su DB_ANAGRAFICA_AZIENDA      
      if (anagAziendaVO.getDataInizioVal().getTime() == today.getTime())
      {
    	SolmrLogger.debug(this, "--- E' già fatta una storicizzazione effettuo solo la modifica su DB_ANAGRAFICA_AZIENDA");  
        wDAO.updateAzienda(anagAziendaVO);
      }
      // Altrimenti procedo con le operazioni di storicizzazione
      else
      {
    	SolmrLogger.debug(this, "-- Aggiorno il record su DB_AZIENDA");
        // Aggiorno il record su DB_AZIENDA
        wDAO.updateDataAzienda(anagAziendaVO.getIdAzienda(), today);
        SolmrLogger.debug(this, "-- Storicizzo il record attivo su DB_ANAGRAFICA_AZIENDA");
        // Storicizzo il record attivo su DB_ANAGRAFICA_AZIENDA
        wDAO.storicizzaAzienda(anagAziendaVO.getIdAnagAzienda());
        SolmrLogger.debug(this, "-- Inserisco il nuovo record su DB_ANAGRAFICA_AZIENDA");
        // Inserisco il nuovo record su DB_ANAGRAFICA_AZIENDA
        wDAO.insertAnagraficaAzienda(anagAziendaVO, idUtenteAggiornamento);
      }
      
      if(Validator.isNotEmpty(vIdAtecoTrib))
      {
        //cancello gli ateco presenti e ne inserisco nuovi..
        aGaaDAO.storicizzaAziendaAtecoSecForAzienda(anagAziendaVO.getIdAzienda().longValue());
        Date dateNow = new Date();
        for(int j=0;j<vIdAtecoTrib.size();j++)
        {
          AziendaAtecoSecVO aziendaAtecoSecVO = new AziendaAtecoSecVO();
          aziendaAtecoSecVO.setIdAzienda(anagAziendaVO.getIdAzienda().longValue());
          aziendaAtecoSecVO.setIdAttivitaAteco(vIdAtecoTrib.get(j).longValue());
          aziendaAtecoSecVO.setDataInizioValidita(dateNow);
          
          aGaaDAO.insertAziendaAtecoSec(aziendaAtecoSecVO);
        }
      }
      
      
      if(modRappLegale)
      {
        Long idSoggetto = cDAO.getSoggettoInAnagrafe(anagAziendaVO.getCUAA());
        //il soggetto non è presente in anagrafica
        if(Validator.isEmpty(idSoggetto))
        {
          idSoggetto = wDAO.insertSoggetto(SolmrConstants.FLAG_S);
          pfVOTributaria.setIdSoggetto(idSoggetto);
          wDAO.insertPersonaFisicaForAT(pfVOTributaria, idUtenteAggiornamento);
        }        
        wDAO.updateContitolareAT(anagAziendaVO.getIdAzienda().longValue());
        wDAO.insertContitolare(anagAziendaVO.getIdAzienda(), idSoggetto);
      }
      else
      {
        if (pfVO.getDataInizioResidenza().getTime() == today.getTime())
          wDAO.updatePersonaFisica(pfVO.getIdPersonaFisica(), pfVO, idUtenteAggiornamento, false);
        else
        {
          // Vado a leggere i vecchi dati di persona fisica per poterli
          // storicizzare
          PersonaFisicaVO pfVOOld = sDAO.findPersonaFisica(pfVO.getIdPersonaFisica());
          wDAO.insertStoricoResidenza(pfVOOld, idUtenteAggiornamento);
          // Aggiorno con i nuovi dati Persona fisica
          wDAO.updatePersonaFisica(pfVO.getIdPersonaFisica(), pfVO, idUtenteAggiornamento, true);
        }
      }

    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
    finally{
    	SolmrLogger.debug(this, "END updateAnagrafe");
    }
  }
  
  
  //aggiorno con storicizzazione solo la tabella db_anagrafica_azienda
  public void updateAnagrafeSemplice(AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento) 
      throws Exception
  {
	SolmrLogger.debug(this, "BEGIN updateAnagrafeSemplice");  
    try
    {
      Date today = DateUtils.parseDate(DateUtils.getCurrent());
      
      // Se è stata già fatta una storicizzazione effettuo solo la modifica del
      // record
      // su DB_ANAGRAFICA_AZIENDA      
      if (anagAziendaVO.getDataInizioVal().getTime() == today.getTime())
      {
    	SolmrLogger.debug(this,"-- è stata già fatta una storicizzazione effettuo solo la modifica del record su su DB_ANAGRAFICA_AZIENDA  ");
        wDAO.updateAzienda(anagAziendaVO);
      }
      // Altrimenti procedo con le operazioni di storicizzazione
      else
      {
    	SolmrLogger.debug(this,"-- Aggiorno il record su DB_AZIENDA");
        // Aggiorno il record su DB_AZIENDA
        wDAO.updateDataAzienda(anagAziendaVO.getIdAzienda(), today);
        SolmrLogger.debug(this,"-- Storicizzo il record attivo su DB_ANAGRAFICA_AZIENDA");
        // Storicizzo il record attivo su DB_ANAGRAFICA_AZIENDA
        wDAO.storicizzaAzienda(anagAziendaVO.getIdAnagAzienda());
        SolmrLogger.debug(this,"-- Inserisco il nuovo record su DB_ANAGRAFICA_AZIENDA");
        // Inserisco il nuovo record su DB_ANAGRAFICA_AZIENDA
        wDAO.insertAnagraficaAzienda(anagAziendaVO, idUtenteAggiornamento);
      }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
    finally{
    	SolmrLogger.debug(this, "END updateAnagrafeSemplice");
    }
  }

  /**
   * Questo metodo viene usato per storicizzare i dati di residenza a seguito di
   * un'importazione dei dati dal sian. E' prevista una sola storicizzazione al
   * giorno, quindi nel caso di più modifiche dei dati di residenza all'interno
   * della stessa giornata verranno direttamente modificati i dati di residenza
   * sulla tabella dp_persona_fisica( non fare update di data_inizio_residenza)
   * 
   * @return Long
   */
  /*public void importaDatiResidenza(PersonaFisicaVO pfVO, long idUtenteAggiornamento) throws Exception, SolmrException
  {
    try
    {
      // Vado a leggere i vecchi dati di persona fisica per poterli storicizzare
      PersonaFisicaVO pfVOOld = sDAO.findPersonaFisica(pfVO.getIdPersonaFisica());
      wDAO.insertStoricoResidenza(pfVOOld, idUtenteAggiornamento);
      // Aggiorno con i nuovi dati Persona fisica
      wDAO.updatePersonaFisica(pfVO.getIdPersonaFisica(), pfVO, idUtenteAggiornamento, true);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    catch (Exception exc)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(exc.getMessage());
    }
  }*/

  /** *********************************************************************** */
  /** ****************************** AAEP *********************************** */
  /**
   * 
   */
  public Long importaDatiAAEP(AnagAAEPAziendaVO anagAAEPAziendaVO, AnagAziendaVO anagAziendaVO, 
      long idUtenteAggiornamento, boolean denominazione, boolean partitaIVA, boolean descrizioneAteco,
      boolean provinciaREA, boolean numeroREA, boolean annoIscrizione, boolean numeroRegistroImprese, 
      boolean pec, boolean sedeLegale, boolean titolareRappresentante, boolean formaGiuridica, boolean sezione,
      boolean descrizioneAtecoSec, boolean dataIscrizioneREA, boolean dataCancellazioneREA, boolean dataIscrizioneRI)
      throws Exception, SolmrException
  {
	SolmrLogger.debug(this, "BEGIN importaDatiAAEP");
	
    Long idAnagAzienda = anagAziendaVO.getIdAnagAzienda();
    RappresentanteLegale rappresentanteLegaleAAEP = anagAAEPAziendaVO.getRappresentanteLegale();
    
    PersonaFisicaVO personaOldVO = getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
    
    //Se l'utente ha deciso di importare la sede legale devo contrallare che l'istat del comune non sia null
    if (sedeLegale)
    {
    	if (Validator.isEmpty(anagAAEPAziendaVO.getSedeLegaleIstatAAEP()) || Validator.isEmpty(anagAAEPAziendaVO.getSedeLegaleCAPAAEP()))
    		throw new SolmrException((String) AnagErrors.get("ERR_AAEP_DATI_SEDE_LEGALE_INCOMPLETI"));
    }

    if (denominazione || partitaIVA || descrizioneAteco || provinciaREA 
        || numeroREA || annoIscrizione || numeroRegistroImprese || pec || sedeLegale 
        || formaGiuridica  || dataIscrizioneREA || dataCancellazioneREA || dataIscrizioneRI)
    {
      /**
       * Se l'utente ha scelto di importare almeno un dato della sezione Dati
       * anagrafici oppure della sezione Sede legale deve essere storicizzato il
       * record attivo dell'anagrafica azienda e creato un nuovo record attivo
       * aggiornando i dati dell'anagrafica con quelli selezionati dall'utente.
       */
      try
      {
        idAnagAzienda = wDAO.updateDatiAnagSedeLegAAEP(anagAAEPAziendaVO, anagAziendaVO, 
            idUtenteAggiornamento, denominazione, partitaIVA, descrizioneAteco, provinciaREA, numeroREA, annoIscrizione,
            numeroRegistroImprese, pec, sedeLegale, formaGiuridica, dataIscrizioneREA ,dataCancellazioneREA ,dataIscrizioneRI);
      }
      catch (DataAccessException e)
      {
        throw new Exception(e.getMessage());
      }
    }
    
    //L'utente ha deciso di importare i dato della sezione
    if(sezione)
    {
      try
      {
        //cancello gli ateco presenti e ne inserisco nuovi..
        aGaaDAO.storicizzaAziendaSezioniForAzienda(anagAziendaVO.getIdAzienda().longValue());
        if(Validator.isNotEmpty(anagAAEPAziendaVO.getvTipoSezAAEP()))
        {
          
          Date dateNow = new Date();
          for(int j=0;j<anagAAEPAziendaVO.getvTipoSezAAEP().size();j++)
          {
            TipoSezioniAaepVO tipoSezioniAaepVO = anagAAEPAziendaVO.getvTipoSezAAEP().get(j);
            AziendaSezioniVO aziendaSezioniVO = new AziendaSezioniVO();
            aziendaSezioniVO.setIdAzienda(anagAziendaVO.getIdAzienda().longValue());
            aziendaSezioniVO.setIdTipoSezioniAaep(tipoSezioniAaepVO.getIdTipoSezioniAaep());
            aziendaSezioniVO.setDataInizioValidita(dateNow);
            
            aGaaDAO.insertAziendaSezione(aziendaSezioniVO);
          }
        }
      }
      catch (DataAccessException e)
      {
        throw new Exception(e.getMessage());
      }
    }
    
    //L'utente ha deciso di importare gli ateco secondari
    if(descrizioneAtecoSec)
    { 
      try
      {
        //cancello gli ateco presenti e ne inserisco nuovi..
        aGaaDAO.storicizzaAziendaAtecoSecForAzienda(anagAziendaVO.getIdAzienda().longValue());
        if(Validator.isNotEmpty(anagAAEPAziendaVO.getvAtecoSecAAEP()))
        {
          
          Date dateNow = new Date();
          for(int j=0;j<anagAAEPAziendaVO.getvAtecoSecAAEP().size();j++)
          {
            CodeDescription cd = anagAAEPAziendaVO.getvAtecoSecAAEP().get(j);
            AziendaAtecoSecVO aziendaAtecoSecVO = new AziendaAtecoSecVO();
            aziendaAtecoSecVO.setIdAzienda(anagAziendaVO.getIdAzienda().longValue());
            aziendaAtecoSecVO.setIdAttivitaAteco(cd.getCode().longValue());
            aziendaAtecoSecVO.setDataInizioValidita(dateNow);
            
            aGaaDAO.insertAziendaAtecoSec(aziendaAtecoSecVO);
          }
        }
      }
      catch (DataAccessException e)
      {
        throw new Exception(e.getMessage());
      }
    }
    
    if (titolareRappresentante)
    {
      /**
       * Se l'utente ha scelto di importare i dati della sezione
       * Titolare/Rappresentante legale
       */
      if (anagAAEPAziendaVO.isRapLegCodFisUguale())
      {
        /**
         * Se il codice fiscale del titolare dell'azienda
         * (db_contitolare.id_ruolo = 1 e db_contitolare.data_fine_validità a
         * null) è uguale al codice fiscale del titolare/rappr.legale restituito
         * dal servizio di AAEP controllare se il comune e il cap sono cambiati
         */
        if (!anagAAEPAziendaVO.isRapLegResidenzaUguale())
        {
        	if(Validator.isEmpty(rappresentanteLegaleAAEP.getCodComuneResidenza())
        			|| Validator.isEmpty(rappresentanteLegaleAAEP.getCap()))
        		throw new SolmrException((String) AnagErrors.get("ERR_AAEP_DATI_TITOLARE_INCOMPLETI"));
        	
          // il comune e il cap sono cambiati
          // storicizzo l'indirizzo di residenza nel seguente modo:
          // Aggiorno prima i dati della persona fisica con quelli provenienti
          // da
          // AAEP
          try
          {
            personaOldVO.setResIndirizzo(anagAAEPAziendaVO.convertNull(rappresentanteLegaleAAEP.getTipoVia().getValue()) + " " + anagAAEPAziendaVO.convertNull(rappresentanteLegaleAAEP.getIndirizzo().getValue()) + " "
                + anagAAEPAziendaVO.convertNull(rappresentanteLegaleAAEP.getNumeroCivico().getValue()));
            personaOldVO.setResComune(rappresentanteLegaleAAEP.getCodComuneResidenza().getValue());
            personaOldVO.setResCAP(rappresentanteLegaleAAEP.getCap().getValue());
            personaOldVO.setResCittaEstero(null);
            personaOldVO.setStatoEsteroRes(null);
          }
          catch (Exception e)
          {
          }
          // poi storicizzo
          storicizzaDatiResidenza(personaOldVO, idUtenteAggiornamento);
        }
      }
      else
      {
        /**
         * Se il codice fiscale del titolare dell'azienda
         * (db_contitolare.id_ruolo = 1 e db_contitolare.data_fine_validità a
         * null) è diverso dal codice fiscale del titolare/rappr.legale
         * restituito dal servizio di AAEP, ricercare se il codice fiscale è già
         * presente tra le persone fisiche censite dal sistema
         * (Db_Persona_fisica).
         */
        boolean codFiscalePresente = false;
        Long idSoggetto = null;
        if (anagAAEPAziendaVO.getRapLegCodFisAAEP() != null)
        {
          try
          {
            PersonaFisicaVO temp = wDAO.getPersonaFisicaFromCUAA(anagAAEPAziendaVO.getRapLegCodFisAAEP());
            
            //Controllo che i dati che sono presenti sul DB siano valorizzati. Se non sono valorizzati leggo quello di AAEP
            //Se anche questi non sono valorizzati segnalo un errore e non permetto l'importazione, se invece 
            //quelli di AAEP sono valorizzati verranno inseriti in quelli di anagrafe. Se modifico quelli della residenza 
            //dovrò anche storicizzare
            
            boolean modifica=false,modificaResidenza=false,noDati=false;
            
            //Controllo uno ad uno i valori presenti in anagrafe e se non sono valorizzati se posso li sostituisco
            //con quelli di AAEP
            
            //Controllo il codice fiscale
            if (Validator.isEmpty(temp.getCodiceFiscale()))
            {
            	modifica=true;
            	if (Validator.isEmpty(rappresentanteLegaleAAEP.getCodiceFiscale().getValue()))
            		noDati=true;
            	else temp.setCodiceFiscale(rappresentanteLegaleAAEP.getCodiceFiscale().getValue());
            }
            
            //Controllo il cognome
            if (Validator.isEmpty(temp.getCognome()))
            {
            	modifica=true;
            	if (Validator.isEmpty(rappresentanteLegaleAAEP.getCognome().getValue()))
            		noDati=true;
            	else temp.setCognome(rappresentanteLegaleAAEP.getCognome().getValue());
            }
            
            //Controllo il nome
            if (Validator.isEmpty(temp.getNome()))
            {
            	modifica=true;
            	if (Validator.isEmpty(rappresentanteLegaleAAEP.getNome().getValue()))
            		noDati=true;
            	else temp.setNome(rappresentanteLegaleAAEP.getNome().getValue());
            }
            
            //Controllo il comune di nascita
            if (Validator.isEmpty(temp.getNascitaComune()))
            {
            	modifica=true;
            	if (Validator.isEmpty(rappresentanteLegaleAAEP.getCodComuneNascita().getValue()))
            		noDati=true;
            	else temp.setNascitaComune(rappresentanteLegaleAAEP.getCodComuneNascita().getValue());
            }
            
            //Controllo il sesso
            if (Validator.isEmpty(temp.getSesso()))
            {
            	modifica=true;
            	if (Validator.isEmpty(rappresentanteLegaleAAEP.getSesso()))
            		noDati=true;
            	else temp.setSesso(rappresentanteLegaleAAEP.getSesso().getValue());
            }
            
            //Controllo la data di nascita
            if (Validator.isEmpty(temp.getNascitaData()))
            {
            	modifica=true;
            	if (Validator.isEmpty(rappresentanteLegaleAAEP.getDataNascita()))
            		noDati=true;
            	else
            	{
            	  try
            	  {
            	    temp.setNascitaData(DateUtils.parseDate(rappresentanteLegaleAAEP.getDataNascita().getValue()));
            	  }
            	  catch(Exception ex)
            	  {}
            	}
            }
            
            //Controllo quelli della residenza... se sono diversi questi devo storicizzare
            
            //Controllo il comune di residenza
            if (Validator.isEmpty(temp.getResComune()))
            {
            	modificaResidenza=true;
            	if (Validator.isEmpty(rappresentanteLegaleAAEP.getCodComuneResidenza().getValue()))
            		noDati=true;
            	else temp.setResComune(rappresentanteLegaleAAEP.getCodComuneResidenza().getValue());
            }
            //Controllo il cap di residenza
            if (Validator.isEmpty(temp.getResCAP()))
            {
            	modificaResidenza=true;
            	if (Validator.isEmpty(rappresentanteLegaleAAEP.getCap().getValue()))
            		noDati=true;
            	else temp.setResCAP(rappresentanteLegaleAAEP.getCap().getValue());
            }
            //Controllo l'indirizzo di residenza
            if (Validator.isEmpty(temp.getResIndirizzo()))
            {
            	modificaResidenza=true;
            	String indirizzo=anagAAEPAziendaVO.convertNull(rappresentanteLegaleAAEP.getTipoVia().getValue()) + " " + anagAAEPAziendaVO.convertNull(rappresentanteLegaleAAEP.getIndirizzo().getValue()) + " "
              + anagAAEPAziendaVO.convertNull(rappresentanteLegaleAAEP.getNumeroCivico().getValue());
            	indirizzo=indirizzo.trim();
            	if (Validator.isEmpty(indirizzo))
            		noDati=true;
            	else temp.setResIndirizzo(indirizzo);
            }
            
            temp.setResCittaEstero(null);
            temp.setStatoEsteroRes(null);
	          
            if (noDati)
            	throw new SolmrException((String) AnagErrors.get("ERR_AAEP_DATI_TITOLARE_INCOMPLETI"));
            
	          if (modificaResidenza)
	          {	          
	          	// poi storicizzo
	          	storicizzaDatiResidenza(temp, idUtenteAggiornamento);
	          }
	          else
		          if (modifica)
		          {
		          	// Aggiorno con i nuovi dati Persona fisica
		            wDAO.updatePersonaFisica(temp.getIdPersonaFisica(), temp, idUtenteAggiornamento, true);
		          }
         
            
            idSoggetto = temp.getIdSoggetto();
            /**
             * Record trovato
             */
            codFiscalePresente = true;
          }
          catch (SolmrException ne)
          {
            /**
             * Non è stato trovato nessun record
             */
            codFiscalePresente = false;
          }
          catch (DataAccessException e)
          {
            throw new Exception(e.getMessage());
          }
          if (!codFiscalePresente)
          {
            /**
             * Se il codice fiscale del titolare/rappr.legale restituito dal
             * servizio di AAEP non è ancora presente sulla tabella delle
             * persone fisiche, verificare che tutti i dati restituiti dal
             * servizio AAEP siano valorizzati. Se uno tra i campi comune di
             * nascita, data di nascita, indirizzo di residenza, cognome, nome,
             * codice fiscale non è impostato , visualizzare il messaggio
             * Impossibile importare i dati del Titolare/Rappr.Legale in quanto
             * non sono completi e non importare i dati del nuovo soggetto. In
             * caso contrario inserire il nuovo soggetto nel sistema:
             */
            if (rappresentanteLegaleAAEP != null)
            {
              if (Validator.isNotEmpty(rappresentanteLegaleAAEP.getCodComuneNascita()) 
              		&& rappresentanteLegaleAAEP.getDataNascita() != null
                  && Validator.isNotEmpty(rappresentanteLegaleAAEP.getIndirizzo()) 
                  && Validator.isNotEmpty(rappresentanteLegaleAAEP.getCognome())
                  && Validator.isNotEmpty(rappresentanteLegaleAAEP.getNome())
                  && Validator.isNotEmpty(rappresentanteLegaleAAEP.getCodiceFiscale())
                  && Validator.isNotEmpty(rappresentanteLegaleAAEP.getSesso())
                  && Validator.isNotEmpty(rappresentanteLegaleAAEP.getCodComuneResidenza())
                  && Validator.isNotEmpty(rappresentanteLegaleAAEP.getCap())
                  )
              {
                try
                {
                  idSoggetto = wDAO.insertSoggetto(SolmrConstants.FLAG_S);
                  PersonaFisicaVO personaVO = new PersonaFisicaVO();
                  personaVO.setIdSoggetto(idSoggetto);
                  personaVO.setCodiceFiscale(rappresentanteLegaleAAEP.getCodiceFiscale().getValue());
                  personaVO.setCognome(rappresentanteLegaleAAEP.getCognome().getValue());
                  personaVO.setNome(rappresentanteLegaleAAEP.getNome().getValue());
                  personaVO.setNascitaComune(rappresentanteLegaleAAEP.getCodComuneNascita().getValue());
                  personaVO.setSesso(rappresentanteLegaleAAEP.getSesso().getValue());
                  personaVO.setResComune(rappresentanteLegaleAAEP.getCodComuneResidenza().getValue());
                  personaVO.setNascitaData(DateUtils.parseDate(rappresentanteLegaleAAEP.getDataNascita().getValue()));
                  personaVO.setResIndirizzo(anagAAEPAziendaVO.convertNull(rappresentanteLegaleAAEP.getTipoVia().getValue()) + " " + anagAAEPAziendaVO.convertNull(rappresentanteLegaleAAEP.getIndirizzo().getValue()) + " "
                      + anagAAEPAziendaVO.convertNull(rappresentanteLegaleAAEP.getNumeroCivico().getValue()));
                  personaVO.setResCAP(rappresentanteLegaleAAEP.getCap().getValue());
                  wDAO.insertPersonaFisica(personaVO, idUtenteAggiornamento);
                }
                catch (Exception e)
                {
                  throw new Exception(e.getMessage());
                }
              }
              else
              {
                throw new SolmrException((String) AnagErrors.get("ERR_AAEP_DATI_TITOLARE_INCOMPLETI"));
              }
            }
            else
            {
              throw new SolmrException((String) AnagErrors.get("ERR_AAEP_DATI_TITOLARE_INCOMPLETI"));
            }
          }
          /**
           * Ne caso in cui il codice fiscale dell'attuale titolare/rappr.legale
           * sia diverso dal codice fiscale del titolare/rappr.legale restituito
           * dal sistema (e sia andato a buon fine l'eventuale inserimento del
           * nuovo soggetto), cessare il ruolo del titolare attuale dell'azienda
           * e inserire il legame tra il nuovo soggetto e l'azienda con il ruolo
           * di titolare:
           */
          try
          {
            wDAO.cessaTitolare(anagAziendaVO, personaOldVO);
            wDAO.insertContitolare(anagAziendaVO.getIdAzienda(), idSoggetto);
          }
          catch (DataAccessException e)
          {
            throw new Exception(e.getMessage());
          }
        }
      }
    }
    SolmrLogger.debug(this, "END importaDatiAAEP");
    return idAnagAzienda;
  }
  
  /** *********************************************************************** */
  /** ****************************** AAEP *********************************** */
  /** *********************************************************************** */

  /**
   * I seguenti metodi sono usati solo per fornire dei servizi (non per uma)
   */
  /** *********************************************************************** */
  /** *********************************************************************** */
  
  
  /**
   *  Usato in anagrafe!!!!!
   *   
   * @param idAzienda
   *          Long
   * @throws Exception
   * @throws SolmrException
   * @return AnagAziendaVO
   */
  public AnagAziendaVO getAziendaByIdAzienda(Long idAzienda) throws Exception, SolmrException
  {
    
    AnagAziendaVO anagAziendaVO = null;
    try
    {
      // Il 2 parametro impostato a true significa che la ricerca viene fatta
      // su ID_AZIENDA, il terzo parametro indica di utlizzare l'anagrafica
      // azienda valida alla data attuale
      anagAziendaVO = aDAO.findByPrimaryKey(idAzienda, true, null);
      
      DelegaVO dVO = delegaDAO.getDelegaByAziendaAndIdProcedimento(idAzienda, (Long) SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
      anagAziendaVO.setDelegaVO(dVO);
      anagAziendaVO.setPossiedeDelegaAttiva(aDAO.controllaPresenzaDelega(anagAziendaVO));
      
      //controlla se è figlio di qualche azienda sulla tabella DB_AZIENDE_COLLEGATE
      anagAziendaVO.setFlagEnteAppartenenza(aDAO.hasEntiAppartenenza(anagAziendaVO.getIdAzienda().longValue()));
      //***************************************************

    }
    catch (NotFoundException ne)
    {
      return null;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return anagAziendaVO;
  }
  
  public Vector<AnagAziendaVO> getAssociazioniCollegateByIdAzienda(Long idAzienda, Date dataFineValidita) throws Exception, SolmrException
  {
    
    Vector<AnagAziendaVO> result = null;
    try
    {
      result = aDAO.getAssociazioniCollegateByIdAzienda(idAzienda, dataFineValidita);
    }
    catch (NotFoundException ne)
    {
      return null;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }


  public AnagAziendaVO[] serviceGetListAziendeByIdRange(Vector<Long> idAnagAzienda, Date dataSituazioneAl) throws Exception, SolmrException
  {

    Vector<AnagAziendaVO> result = null;
    try
    {
      // Il 2 parametro impostato a true significa che la ricerca viene fatta
      // su ID_AZIENDA
      result = aDAO.getListAziendeByIdRange(idAnagAzienda, true, dataSituazioneAl);

      /**
       * Il caricamento delle deleghe per glia altri procedimenti non deve più
       * essere fatto perchè vanno tramite comune
       */
      /*
       * Vector vectAzienda = new Vector(); for(int i = 0; i<result.size();
       * i++)
       * vectAzienda.addElement(((AnagAziendaVO)result.elementAt(i)).getIdAzienda());
       * Vector deleghe = aDAO.getRangeDelegaByRangeAzienda(vectAzienda);
       * for(int i = 0; i<result.size(); i++) { AnagAziendaVO aaVO =
       * (AnagAziendaVO)result.elementAt(i);
       * aaVO.setPossiedeDelegaAttiva(aDAO.controllaPresenzaDelega(aaVO));
       * for(int j = 0; j<deleghe.size(); j++) { DelegaVO dVO =
       * (DelegaVO)deleghe.elementAt(j);
       * if(aaVO.getIdAzienda().equals(dVO.getIdAzienda())) {
       * aaVO.setDelegaVO(dVO); break; } } }
       */
      if (result == null || result.size() == 0)
        return null;
      else
        return (AnagAziendaVO[]) result.toArray(new AnagAziendaVO[0]);
    }
    catch (NotFoundException ne)
    {
      return null;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /*public Vector<Long> serviceGetListIdAziende(AnagAziendaVO aaVO, String attivita, Boolean schedario) throws Exception, SolmrException
  {
    
    Vector<Long> result = null;
    try
    {
      // Il 5 parametro impostato a true significa che la ricerca viene fatta
      // su ID_AZIENDA
      result = aDAO.getListIdAziende(aaVO, null, attivita, schedario.booleanValue(), true);
      if (result == null)
        return null;
      if (result.size() == 0)
        return null;
    }
    catch (DataControlException dce)
    {
      throw new SolmrException(ErrorTypes.STR_MAX_RECORD, ErrorTypes.MAX_RECORD);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return result;
  }*/

  public AnagAziendaVO serviceGetAziendaById(Long idAzienda, Date dataSituazioneAl) throws Exception, SolmrException
  {
    
    AnagAziendaVO result = null;
    try
    {
      // Il 2 parametro impostato a true significa che la ricerca viene fatta
      // su ID_AZIENDA, il terzo parametro indica di utlizzare l'anagrafica
      // azienda valida alla data indicata
      result = aDAO.findByPrimaryKey(idAzienda, true, dataSituazioneAl);

      /*
       * DelegaVO dVO = aDAO.getDelegaByAzienda(result.getIdAzienda());
       * result.setDelegaVO(dVO);
       * result.setPossiedeDelegaAttiva(aDAO.controllaPresenzaDelega(result));
       */

      // Dal momento che tutti gli altri procedimenti non utilizzano anagrafe ma
      // bensì comune
      // per controllare la presenza della delega forzo a null la delega in modo
      // che tutti gli altri
      // servizi siano costretti a cercarla attraverso SMR_COMUNE
      // Prima di modificare il metodo chiedere a Borgogno
      result.setDelegaVO(null);

    }
    catch (NotFoundException ne)
    {
      return null;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  public DelegaVO getIntermediarioPerDelega(Long idIntermediario) throws Exception, SolmrException
  {
    try
    {
      return cDAO.getIntermediarioPerDelega(idIntermediario);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public IntermediarioAnagVO getIntermediarioAnagByIdIntermediario(long idIntermediario)
    throws Exception
  {
    try
    {
      return cDAO.getIntermediarioAnagByIdIntermediario(idIntermediario);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public IntermediarioAnagVO findIntermediarioVOByCodiceEnte(String codEnte)
    throws Exception
  {
    try
    {
      return cDAO.findIntermediarioVOByCodiceEnte(codEnte);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public IntermediarioAnagVO findIntermediarioVOByIdAzienda(long idAzienda)
      throws Exception
  {
    try
    {
      return cDAO.findIntermediarioVOByIdAzienda(idAzienda);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public boolean isAziendaIntermediario(long idAzienda)
     throws Exception
  {
    try
    {
      return cDAO.isAziendaIntermediario(idAzienda);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioVOById(long idIntemediario)
    throws Exception
  {
    try
    {
      return cDAO.findFigliIntermediarioVOById(idIntemediario);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public Vector<IntermediarioAnagVO> findFigliIntermediarioAziendaVOById(long idIntemediario)
    throws Exception
  {
    try
    {
      return cDAO.findFigliIntermediarioAziendaVOById(idIntemediario);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Questo metodo viene usato per storicizzare i dati di residenza
   * 
   * @return Long
   */
  public Long storicizzaDatiResidenza(PersonaFisicaVO pfVO, long idUtenteAggiornamento) throws Exception, SolmrException
  {
    Long idStoricoResidenza = null;
    try
    {
      // Vado a leggere i vecchi dati di persona fisica per poterli storicizzare
      PersonaFisicaVO pfVOOld = sDAO.findPersonaFisica(pfVO.getIdPersonaFisica());
      idStoricoResidenza = wDAO.insertStoricoResidenza(pfVOOld, idUtenteAggiornamento);
      // Aggiorno con i nuovi dati Persona fisica
      wDAO.updatePersonaFisica(pfVO.getIdPersonaFisica(), pfVO, idUtenteAggiornamento, true);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    catch (Exception exc)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(exc.getMessage());
    }
    return idStoricoResidenza;
  }

  /*
   * Aggiunto il 6/12/2004 per aggirare (temporaneamente) il mancato inserimento
   * della chiamata al metodo omonimo di DittaUmaDAO di UMA tramite CSI
   */
  // #-#
  public DittaUMAVO getDittaUmaByIdAzienda(Long idAzienda) throws Exception, SolmrException
  {
    
    try
    {
      return aDAO.getDittaUmaByIdAzienda(idAzienda);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  public void updateInsediamentoGiovani(Long idAzienda) throws Exception
  {
    try
    {
      wDAO.updateInsediamentoGiovani(idAzienda);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  // INIZIO PROFILAZIONE

  // Metodo per controllare se l'utente intermediario che si è loggato possiede
  // una delega diretta
  // o tramite id_intermediario padre
  public DelegaVO intermediarioConDelega(UtenteAbilitazioni utenteAbilitazioni, Long idAzienda) throws Exception, SolmrException
  {
    DelegaVO delegaVO = null;
    try
    {
      delegaVO = aDAO.intermediarioConDelega(utenteAbilitazioni, idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return delegaVO;
  }

  public boolean isIntermediarioConDelegaDiretta(long idIntermediario, Long idAzienda) throws Exception
  {
    try
    {
      return aDAO.isIntermediarioConDelegaDiretta(idIntermediario, idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public boolean isIntermediarioPadre(long idIntermediario, Long idAzienda) throws Exception
  {
    try
    {
      return aDAO.isIntermediarioPadre(idIntermediario, idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  // FINE PROFILAZIONE

  public boolean testResources() throws Exception
  {
    try
    {
      return aDAO.testResources();

    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  public String[] cessazioneAziendaPLQSL(Long idAzienda) throws Exception, SolmrException
  {
    try
    {
      return aDAO.cessazioneAziendaPLQSL(idAzienda);
    }
    catch (SolmrException d)
    {
      throw new SolmrException(d.getMessage());
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  // Metodo per recuperare recuperare l'ufficio zona intermediario partendo
  // dalla chiave primaria
  public UfficioZonaIntermediarioVO findUfficioZonaIntermediarioVOByPrimaryKey(Long idUfficioZonaIntermediario) throws Exception
  {
    UfficioZonaIntermediarioVO ufficioZonaIntermediarioVO = null;
    try
    {
      ufficioZonaIntermediarioVO = wDAO.findUfficioZonaIntermediarioVOByPrimaryKey(idUfficioZonaIntermediario);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return ufficioZonaIntermediarioVO;
  }

  // Metodo per recuperare l'elenco delle aziende in relazione all'intermediario
  // delegato
  public Vector<AnagAziendaVO> getElencoAziendeByCAA(DelegaVO delegaVO) throws Exception, SolmrException
  {
    Vector<AnagAziendaVO> elencoAziende = null;
    try
    {
      elencoAziende = wDAO.getElencoAziendeByCAA(delegaVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoAziende;
  }

  // Metodo per aggiornare e recuperare le pratiche relative ad un'azienda
  // agricola
  public Vector<ProcedimentoAziendaVO> updateAndGetPraticheByAzienda(Long idAzienda) throws SolmrException, Exception
  {
    Vector<ProcedimentoAziendaVO> elencoPratiche = null;
    try
    {
      wDAO.updateProcedimentoAzienda(idAzienda);
      elencoPratiche = wDAO.getElencoProcedimentiByIdAzienda(idAzienda, null, null, null);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoPratiche;
  }

  /**
   * Metodo per recuperare l'elenco dei CUAA relativi ad un'id_azienda
   * 
   * @param idAzienda
   *          Long
   * @return Vector
   * @throws Exception
   */
  public Vector<String> getListCUAAByIdAzienda(Long idAzienda) throws Exception
  {
    try
    {
      return wDAO.getListCUAAByIdAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di estrarre la denominazione più recente di un'azienda
   * partendo dal CUAA e dall'id_azienda
   * 
   * @param idAzienda
   *          Long
   * @param cuaa
   *          String
   * @return String
   * @throws Exception
   */
  public String getDenominazioneAziendaByCuaaAndIdAzienda(Long idAzienda, String cuaa) throws Exception
  {
    try
    {
      return wDAO.getDenominazioneAziendaByCuaaAndIdAzienda(idAzienda, cuaa);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce la MIN relativa alla data inizio validita di
   * un'azienda
   * 
   * @param idAzienda
   * @return java.util.Date
   * @throws Exception
   */
  public Date getMinDataInizioValiditaAnagraficaAzienda(Long idAzienda) throws Exception
  {
    try
    {
      return wDAO.getMinDataInizioValiditaAnagraficaAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per effettuare la modifica del record su DB_AZIENDA in
   * funzione dell'OPR delegato al fascicolo
   * 
   * @param anagAziendaVO
   * @throws Exception
   */
  public void modificaGestoreFascicolo(AnagAziendaVO anagAziendaVO) throws Exception
  {
    try
    {
      wDAO.modificaGestoreFascicolo(anagAziendaVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce la data max di fine mandato
   * 
   * @param idAzienda
   * @return java.util.Date maxDataFineMandato
   * @throws Exception
   */
  public java.util.Date getDataMaxFineMandato(Long idAzienda) throws Exception
  {
    try
    {
      return delegaDAO.getDataMaxFineMandato(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce la delega di un dato procedimento relativo ad una
   * determinata azienda agricola
   * 
   * @param idAzienda
   * @param idProcedimento
   * @return it.csi.solmr.dto.anag.DelegaVO
   * @throws Exception
   */
  public DelegaVO getDelegaByAziendaAndIdProcedimento(Long idAzienda, Long idProcedimento) throws Exception
  {
    try
    {
      return delegaDAO.getDelegaByAziendaAndIdProcedimento(idAzienda, idProcedimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce la data max di inizio mandato
   * 
   * @param idAzienda
   * @return java.util.Date maxDataFineMandato
   * @throws Exception
   */
  public java.util.Date getDataMaxInizioMandato(Long idAzienda) throws Exception
  {
    try
    {
      return delegaDAO.getDataMaxInizioMandato(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di storicizzare il mandato di delega in anagrafe, di
   * fatto revocandolo
   * 
   */
  public void storicizzaDelega(DelegaVO dVO, RuoloUtenza ruoloUtenza, DocumentoVO documentoVO, AnagAziendaVO anagAziendaVO) throws Exception
  {
    try
    {
      delegaDAO.storicizzaDelega(dVO, ruoloUtenza);
      // Cerco il vecchio documento relativo al mandato di assistenza
      DocumentoVO oldDocumentoVO = documentoDAO.findDocumentoMandatoOrRevocaAssistenza(anagAziendaVO, SolmrConstants.ID_TIPO_DOCUMENTO_MANDATO_ASSISTENZA);
      // Se c'è, lo storicizzo
      if (oldDocumentoVO != null)
      {
        oldDocumentoVO.setIdStatoDocumento(Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_STORICIZZATO));
        oldDocumentoVO.setNote("Revoca mandato di assistenza");
        oldDocumentoVO.setDataFineValidita(dVO.getDataFineMandato());
        documentoDAO.storicizzaDocumento(oldDocumentoVO, ruoloUtenza.getIdUtente());
      }
      // Gestisco l'inserimento del documento
      if (documentoVO != null)
      {
        String numeroProtocollo = null;
        try
        {
          // Recupero il numero protocollo
          numeroProtocollo = documentoDAO.getNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
          // Se non lo trovo lo inserisco
          if (numeroProtocollo == null)
          {
            documentoDAO.insertNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
            numeroProtocollo = documentoDAO.getNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
          }
          // Alla fine delle operazioni aggiorno comunque il record e libero la
          // tabella
          documentoDAO.aggiornaProgressivoNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
        }
        catch (DataAccessException dae)
        {
          sessionContext.setRollbackOnly();
          throw new Exception(dae.getMessage());
        }
        catch (SolmrException se)
        {
          try
          {
            numeroProtocollo = documentoDAO.getNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
            // Alla fine delle operazioni aggiorno comunque il record e libero
            // la tabella
            documentoDAO.aggiornaProgressivoNumeroProtocollo(ruoloUtenza, String.valueOf(DateUtils.getCurrentYear()));
          }
          catch (DataAccessException dae)
          {
            sessionContext.setRollbackOnly();
            throw new Exception(dae.getMessage());
          }
        }
        documentoVO.setNumeroProtocollo(numeroProtocollo);
        documentoVO.setDataProtocollo(new Date(System.currentTimeMillis()));
        documentoDAO.insertDocumento(documentoVO, ruoloUtenza);
      }
    }
    catch (DataAccessException e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
  }
  
  
  /**
   * Storicizza la delega in maniera temporanea in attesa del batch che chiudrà la delega
   * in modo definitivo.
   * In più aggiunge una notifica.
   * 
   * 
   * @param delegaVO
   * @param profile
   * @param anagAziendaVO
   * @throws Exception
   */
  public int storicizzaDelegaTemporanea(DelegaVO delegaVO, RuoloUtenza ruoloUtenza, AnagAziendaVO anagAziendaVO) throws Exception
  {
    int valore = SolmrConstants.NO_RICHIESTE_REVOCA_ATTIVE;
    try
    {
      if(delegaDAO.existsRichiestaRevoca(anagAziendaVO.getIdAzienda().longValue()))
      {
        valore = SolmrConstants.SI_RICHIESTE_REVOCA_ATTIVE;
      }
      
      if(valore == SolmrConstants.NO_RICHIESTE_REVOCA_ATTIVE)
      {
      
        delegaDAO.storicizzaDelegaTemporanea(delegaVO, ruoloUtenza);
        NotificaVO notificaVO = new NotificaVO();
        notificaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
        BigDecimal categoriaNotificaBg = (BigDecimal)cDAO.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_CAT_NOTIFICA_REVOCA);
        notificaVO.setIdCategoriaNotifica(new Long(categoriaNotificaBg.longValue()));
        Long idTipologiaNotifica = notificaDAO.getIdTipologiaNotificaFromCategoria(
            categoriaNotificaBg.longValue());
        notificaVO.setIdTipologiaNotifica(idTipologiaNotifica);
        
        String parametroGDEL = cDAO.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_GDEL);
        int incremento = new Integer(parametroGDEL).intValue();
        Calendar cDateTmp = Calendar.getInstance();
        cDateTmp.setTime(delegaVO.getDataRicevutaRitornoDelega());
        cDateTmp.roll(Calendar.DAY_OF_YEAR, incremento);
        
        //se è minore della data odierna dopo l'incremento di 15 GG
        if(cDateTmp.getTime().before(new Date()))
        {
          cDateTmp = Calendar.getInstance();
          cDateTmp.roll(Calendar.DAY_OF_YEAR, 1);
        }
        
        
        
        String dataTmp = DateUtils.formatDate(cDateTmp.getTime());
        String descrizione = "Il mandato presso "+delegaVO.getDenomIntermediario()+
          " verrà revocato automaticamente in data "+dataTmp;
        notificaVO.setDescrizione(descrizione);
        notificaVO.setIdProcedimentoDestinatario(new Long(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
        notificaVO.setIdProcedimentoMittente(new Long(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
        
        notificaDAO.insertNotifica(notificaVO, SolmrConstants.ID_UTENTE_DEFAULT);
        
      }
      
      
    }
    catch (DataAccessException e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
    catch (SolmrException e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
    
    return valore;
  }

  /**
   * Metodo che mi restituisce lo storico delle deleghe di un dato procedimento
   * relativo ad una determinata azienda agricola
   * 
   * @param idAzienda
   * @param idProcedimento
   * @param orderBy[]
   * @return it.csi.solmr.dto.anag.DelegaVO[]
   * @throws Exception
   */
  public DelegaVO[] getStoricoDelegheByAziendaAndIdProcedimento(Long idAzienda, Long idProcedimento, String[] orderBy) throws Exception
  {
    try
    {
      return delegaDAO.getStoricoDelegheByAziendaAndIdProcedimento(idAzienda, idProcedimento, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo utilizzato per recuperare l'elenco dell'aziende in funzione del CUAA
   * 
   * @param cuaa
   * @param onlyActive
   * @param isCessata
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AnagAziendaVO[]
   * @throws Exception
   */
  public AnagAziendaVO[] getListAnagAziendaVOByCuaa(String cuaa, boolean onlyActive, boolean isCessata, String[] orderBy) throws Exception
  {
    try
    {
      return aDAO.getListAnagAziendaVOByCuaa(cuaa, onlyActive, isCessata, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce la MAX relativa alla data inizio validita di
   * un'azienda
   * 
   * @param idAzienda
   * @return java.util.Date
   * @throws Exception
   */
  public Date getMaxDataInizioValiditaAnagraficaAzienda(Long idAzienda) throws Exception
  {
    try
    {
      return wDAO.getMaxDataInizioValiditaAnagraficaAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di estrarre tutte le occorrenze dalla tabella
   * DB_ANAGRAFICA_AZIENDA in relazione all'id_azienda di destinazione
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.AnagAziendaVO[]
   * @throws Exception
   */
  public AnagAziendaVO[] getListAnagAziendaDestinazioneByIdAzienda(Long idAzienda, boolean onlyActive, String[] orderBy) throws Exception
  {
    try
    {
      return aDAO.getListAnagAziendaDestinazioneByIdAzienda(idAzienda, onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public Boolean serviceIsEsenteDelega(Long idAzienda) throws Exception, SolmrException
  {
    Boolean isEsenteDelega = null;

    try
    {
      isEsenteDelega = aDAO.serviceIsEsenteDelega(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
    return isEsenteDelega;
  }

  public DelegaAnagrafeVO serviceGetDelega(Long idAzienda, String codiceEnte, Boolean ricercaSuEntiFigli, Date data) throws Exception, SolmrException
  {

    try
    {
      return aDAO.serviceGetDelega(idAzienda, codiceEnte, ricercaSuEntiFigli, data);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public boolean[] serviceGetVariazioneDatiAnagrafici(Long idAzienda, Date data) throws SolmrException, Exception
  {
    try
    {
      return wDAO.serviceGetVariazioneDatiAnagrafici(idAzienda, data);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public Vector<AnagAziendaVO> getAziendaByIntermediarioAndCuaa(Long intermediario, String cuaa) throws Exception, NotFoundException, SolmrException
  {
    try
    {
      return aDAO.getAziendaByIntermediarioAndCuaa(intermediario, cuaa);
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  public Vector<AnagAziendaVO> getAziendeByListOfId(Vector<Long> vIdAzienda) throws Exception, NotFoundException, SolmrException
  {
    try
    {
      return aDAO.getAziendeByListOfId(vIdAzienda);
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Metodo che si occupa di storicizzare il mandato di delega in anagrafe, di
   * fatto revocandolo lavora sulle tabelle DB_DELEGA e DB_DOCUMENTO
   * 
   */
  public void storicizzaDelegaBlocco(RuoloUtenza ruoloUtenza, Vector<AnagAziendaVO> vAnagAziendaVO, String oldIntermediario, String newIntermediario) throws Exception, SolmrException
  {

    try
    {
      AnagAziendaVO anagAziendaVO = null;
      DocumentoVO documentoVO = null;
      IntermediarioVO intermediarioVOOld = cDAO.findIntermediarioVOByPrimaryKey(new Long(oldIntermediario));
      IntermediarioVO intermediarioVONew = cDAO.findIntermediarioVOByPrimaryKey(new Long(newIntermediario));
      String parametroComuneMapr = cDAO.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_COMUNE_MAPR);
      for (int i = 0; i < vAnagAziendaVO.size(); i++)
      {
        anagAziendaVO = (AnagAziendaVO) vAnagAziendaVO.get(i);
        anagAziendaVO.getDelegaVO().setDataFineMandato(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
        delegaDAO.storicizzaDelega(anagAziendaVO.getDelegaVO(), ruoloUtenza);
        
        
        SolmrLogger.debug(this, "primi tre valori del vecchio caa: "+intermediarioVOOld.getCodiceFiscale().substring(0, 3));
        SolmrLogger.debug(this, "primi tre valori del nuovo caa: "+intermediarioVONew.getCodiceFiscale().substring(0, 3));
        
        //Eseguo le operazioni sul documento se trattasi di due caa diversi.        
        if(!intermediarioVOOld.getCodiceFiscale().substring(0, 3)
            .equalsIgnoreCase(intermediarioVONew.getCodiceFiscale().substring(0, 3)))
        {
          // Cerco il vecchio documento relativo alla revoca del mandato di
          // assistenza
          DocumentoVO oldDocumentoVOMandAss = documentoDAO.findDocumentoMandatoOrRevocaAssistenza(anagAziendaVO, SolmrConstants.ID_TIPO_DOCUMENTO_REVOCA_MANDATO_ASSISTENZA);
          // Se c'è, lo storicizzo e lo chiudo perche ne creo uno nuovo dopo
          if (oldDocumentoVOMandAss != null)
          {
            oldDocumentoVOMandAss.setIdStatoDocumento(Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_STORICIZZATO));
            oldDocumentoVOMandAss.setNote("mandato di assistenza");
            oldDocumentoVOMandAss.setDataFineValidita(anagAziendaVO.getDelegaVO().getDataFineMandato());
            documentoDAO.updateDocumento(oldDocumentoVOMandAss, ruoloUtenza);
          }
  
          // inizio revoca mandato
          if (Validator.isNotEmpty(parametroComuneMapr) && parametroComuneMapr.equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            documentoVO = new DocumentoVO();
            documentoVO.setExtIdDocumento(SolmrConstants.ID_TIPO_DOCUMENTO_REVOCA_MANDATO_ASSISTENZA);
            documentoVO.setIdAzienda(anagAziendaVO.getIdAzienda());
            documentoVO.setCuaa(anagAziendaVO.getCUAA());
            documentoVO.setDataInizioValidita(anagAziendaVO.getDelegaVO().getDataInizioMandato());
            documentoVO.setDataFineValidita(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
            documentoVO.setDataUltimoAggiornamento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
            documentoVO.setDataInserimento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
  
            //intermediarioVOOld = cDAO.findIntermediarioVOByPrimaryKey(new Long(oldIntermediario));
            documentoVO.setNote("CAA: " + intermediarioVOOld.getCodiceFiscale());
  
          }
          //delegaDAO.storicizzaDelega(anagAziendaVO.getDelegaVO(), profile.getRuoloUtenza());
          // Cerco il vecchio documento relativo al mandato di assistenza
          DocumentoVO oldDocumentoVO = documentoDAO.findDocumentoMandatoOrRevocaAssistenza(anagAziendaVO, SolmrConstants.ID_TIPO_DOCUMENTO_MANDATO_ASSISTENZA);
          // Se c'è, lo storicizzo e lo chiudo perche ne creo uno nuovo dopo
          if (oldDocumentoVO != null)
          {
            oldDocumentoVO.setIdStatoDocumento(Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_STORICIZZATO));
            oldDocumentoVO.setNote("Revoca mandato di assistenza");
            oldDocumentoVO.setDataFineValidita(anagAziendaVO.getDelegaVO().getDataFineMandato());
            documentoDAO.updateDocumento(oldDocumentoVO, ruoloUtenza);
          }
          // Gestisco l'inserimento del documento
          if (documentoVO != null)
          {
            String numeroProtocollo = null;
            InitialContext ctx = new InitialContext();            
            numeroProtocollo = documentoLocal.mathNumeroProtocollo(ruoloUtenza, DateUtils.getCurrentYear().toString());
  
            documentoVO.setNumeroProtocollo(numeroProtocollo);
            documentoVO.setDataProtocollo(new Date(System.currentTimeMillis()));
            documentoDAO.insertDocumento(documentoVO, ruoloUtenza);
          }
          // Fine revoca mandato
          // Inizio nuovo mandato
          documentoVO = null;
          oldDocumentoVO = null;
          //intermediarioVONew = cDAO.findIntermediarioVOByPrimaryKey(new Long(newIntermediario));
  
          if (Validator.isNotEmpty(parametroComuneMapr) && parametroComuneMapr.equalsIgnoreCase(SolmrConstants.FLAG_S))
          {
            documentoVO = new DocumentoVO();
            documentoVO.setExtIdDocumento(SolmrConstants.ID_TIPO_DOCUMENTO_MANDATO_ASSISTENZA);
            documentoVO.setIdAzienda(anagAziendaVO.getIdAzienda());
            documentoVO.setCuaa(anagAziendaVO.getCUAA());
            documentoVO.setDataInizioValidita(new java.util.Date(System.currentTimeMillis()));
            documentoVO.setDataUltimoAggiornamento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
            documentoVO.setDataInserimento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
            documentoVO.setNote("CAA: " + intermediarioVONew.getCodiceFiscale());
          }
        }

        UfficioZonaIntermediarioVO ufficioZonaIntermediarioVO = wDAO.findUfficioZonaIntermediarioVOByIdIntemediario(Long.decode(newIntermediario));
        DelegaVO delegaVO = new DelegaVO();
        delegaVO.setIdIntermediario(intermediarioVONew.getIdIntermediarioLong());
        delegaVO.setIdProcedimento(Long.decode(String.valueOf(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE)));
        delegaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
        delegaVO.setDataInizio(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
        delegaVO.setIdUtenteInsDelega(ruoloUtenza.getIdUtente());
        delegaVO.setIdUfficioZonaIntermediario(ufficioZonaIntermediarioVO.getIdUfficioZonaIntermediario());
        delegaVO.setDataInizioMandato(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));

        SolmrLogger.debug(this, "Invocating controllaPresenzaDelega in insertDelegaForMandato in AnagAziendaBean");
        if (aDAO.controllaPresenzaDelega(anagAziendaVO))
        {
          SolmrLogger.info(this, "Throwing SolmrException in insertDelegaForMandato in AnagAziendaBean with this message: " + (String) AnagErrors.get("ERR_DELEGA_CONTEMPORANEA"));
          throw new SolmrException((String) AnagErrors.get("ERR_DELEGA_CONTEMPORANEA"));
        }
        else
        {
          delegaDAO.insertDelega(delegaVO);
          // La gestione del documentale è vincolata alla presenza del CUAA
          // dell'azienda agricola
          // selezionata
          if (documentoVO != null)
          {
            String numeroProtocollo = null;
            InitialContext ctx = new InitialContext();            
            numeroProtocollo = documentoLocal.mathNumeroProtocollo(ruoloUtenza, DateUtils.getCurrentYear().toString());

            documentoVO.setNumeroProtocollo(numeroProtocollo);
            documentoVO.setDataProtocollo(new Date(System.currentTimeMillis()));
            documentoVO.setDataInizioValidita(delegaVO.getDataInizioMandato());
            documentoDAO.insertDocumento(documentoVO, ruoloUtenza);
          }
        }
        // fine nuovo mandato
      }
    }
    catch (SolmrException se)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(se.getMessage());
    }
    catch (DataAccessException e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Metodo per recuperare l'elenco dei procedimenti legati ad un'azienda
   * agricola. Restituisce un vettore di ProcedimentoAziendaVO.
   * 
   * @param idAzienda
   * @param annoProc
   * @param idProcedimento
   * @return
   * @throws Exception
   * @throws NotFoundException
   * @throws SolmrException
   */
  public Vector<ProcedimentoAziendaVO> getElencoProcedimentiByIdAzienda(Long idAzienda, Long annoProc, Long idProcedimento, Long idAziendaSelezionata) throws Exception, NotFoundException, SolmrException
  {
    try
    {
      return wDAO.getElencoProcedimentiByIdAzienda(idAzienda, annoProc, idProcedimento, idAziendaSelezionata);
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Cerca un'azienda con la chiave primaria ID_ANAGRAFICA_AZIENDA
   * 
   * @param idAzienda
   * @param flagStorico
   * @return
   * @throws Exception
   * @throws SolmrException
   */
  public AnagAziendaVO findAziendaByIdAnagAzienda(Long anagAziendaPK) throws Exception, SolmrException
  {
    try
    {
      AnagAziendaVO anagVO = aDAO.findAziendaByIdAnagAzienda(anagAziendaPK);
      //controlla se è figlio di qualche azienda sulla tabella DB_AZIENDE_COLLEGATE
      anagVO.setFlagEnteAppartenenza(aDAO.hasEntiAppartenenza(anagVO.getIdAzienda().longValue()));      
      return anagVO;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Restituisce un Vector di AnagAziendaVO. I parametri sono IdAzienda e
   * flagStorico, se a true devo fare vedere anche le aziende storicizzate
   * 
   * @param idAzienda
   * @param flagStorico
   * 
   * @return
   * @throws DataAccessException
   * @throws NotFoundException
   * @throws SolmrException
   */
  public Vector<AziendaCollegataVO> getAziendeCollegateByIdAzienda(Long idAzienda, boolean flagStorico) throws Exception, SolmrException
  {
    try
    {
      return aDAO.getAziendeCollegateByIdAzienda(idAzienda, flagStorico);
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  /**
   * Restituisce un Vector di AnagAziendaVO. I parametri sono IdAzienda e
   * flagStorico, se a true devo fare vedere anche le aziende storicizzate
   * 
   * @param idAzienda
   * @param flagStorico
   * 
   * @return
   * @throws DataAccessException
   * @throws NotFoundException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getEntiAppartenenzaByIdAzienda(Long idAzienda, boolean flagStorico) throws Exception, SolmrException
  {

    try
    {
      return aDAO.getEntiAppartenenzaByIdAzienda(idAzienda, flagStorico);
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Metodo utilizzato per recuperare l'elenco delle aziende su cui insistono le
   * particelle ad asservimento: va utilizzato dopo il search delle particelle
   * in modo da ottenere gli id_storico_particelle indispensabili per la query
   * 
   * @param idStoricoParticella
   * @param idAzienda
   * @param idTitoloPossesso
   * @return it.csi.solmr.dto.anag.AnagAziendaVO[]
   * @throws Exception
   */
  public AnagAziendaVO[] getListAziendeParticelleAsservite(Long idStoricoParticella, 
      Long idAzienda, Long idTitoloPossesso, Date dataInserimentoDichiarazione) throws Exception
  {
    try
    {
      return aDAO.getListAziendeParticelleAsservite(idStoricoParticella, idAzienda, idTitoloPossesso, dataInserimentoDichiarazione);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Restituisce un Vector di AnagAziendaVO. I parametri sono vIdAzienda un
   * vettore che contiene gli id_azienda_collegata
   * 
   * @param idAzienda
   * 
   * 
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AziendaCollegataVO> getAziendeCollegateByRangeIdAziendaCollegata(Vector<Long> vIdAziendaCollegata) throws Exception, SolmrException
  {
    try
    {
      return aDAO.getAziendeCollegateByRangeIdAziendaCollegata(vIdAziendaCollegata);
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Passo come parametro l'azienda della quale la query restituisce un vector
   * di AnagAziendaVO con valorizzati solo l'id_azienda_collegata e l'id_azienda
   * dei padri/nonni/ecc. Se non ne ha restitusce null!
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getIdAziendaCollegataAncestor(Long idAzienda) throws Exception, SolmrException
  {
    try
    {
      return aDAO.getIdAziendaCollegataAncestor(idAzienda);
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Passo come parametro l'azienda della quale la query restituisce un vector
   * di AnagAziendaVO con valorizzati solo l'id_azienda_collegata e l'id_azienda
   * dei discendenti. Se non ne ha restitusce null!
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getIdAziendaCollegataDescendant(Long idAzienda) throws Exception, SolmrException
  {
    try
    {
      return aDAO.getIdAziendaCollegataDescendant(idAzienda);
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  public boolean controlloAziendeAssociate(String CUAApadre, Long idAziendaCollegata) throws Exception
  {
    try
    {
      return aDAO.controlloAziendeAssociate(CUAApadre, idAziendaCollegata);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Restituisce un Vector di AnagAziendaVO. I parametri sono vIdAzienda un
   * vettore che contiene gli id_azienda
   * 
   * @param idAzienda
   * 
   * 
   * @return
   * @throws DataAccessException
   * @throws SolmrException
   */
  public Vector<AnagAziendaVO> getAziendeCollegateByRangeIdAzienda(Vector<Long> vIdAzienda, Long idAziendaPadre) throws Exception, SolmrException
  {
    try
    {
      return aDAO.getAziendeCollegateByRangeIdAzienda(vIdAzienda, idAziendaPadre);
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Metodo che si occupa di storicizzare le aziende collegate. Lavora sulla
   * tabella DB_AZIENDE_COLLEGATE
   * 
   * @param profile
   * @param vAnagAziendaCollegateVO
   * @throws Exception
   * @throws SolmrException
   */
  public void storicizzaAziendeCollegateBlocco(RuoloUtenza ruoloUtenza, Vector<AziendaCollegataVO> vAnagAziendaCollegateVO) throws Exception, SolmrException
  {
    try
    {
      for (int i = 0; i < vAnagAziendaCollegateVO.size(); i++)
      {
        AziendaCollegataVO azCollVO = (AziendaCollegataVO) vAnagAziendaCollegateVO.get(i);
        boolean flagStoricoCurr = false;
        boolean flagModifica = false;
        // select
        AziendaCollegataVO azCollVOTmp = null;
        //Se idAziendaCollagata è valorizzato sono in modifica 
        if(azCollVO.getIdAziendaCollegata() != null)
        {
          azCollVOTmp = aDAO.findAziendaCollegataByPrimaryKey(azCollVO.getIdAziendaCollegata().longValue());
        }
        
        if (azCollVOTmp != null) // Trovata l'azienda collegata attiva
        {
          flagModifica = true;
          // Controllo se è già stato fatto un aggiornamento in data odierna
          int year = DateUtils.extractYearFromDate(azCollVOTmp.getDataAggiornamento());
          int month = DateUtils.extractMonthFromDate(azCollVOTmp.getDataAggiornamento());
          int day = DateUtils.extractDayFromDate(azCollVOTmp.getDataAggiornamento());
          if ((DateUtils.getCurrentYear().intValue() == year) && (DateUtils.getCurrentMonth().intValue() == month) && (DateUtils.getCurrentDay().intValue() == day))
          {

            Vector<ConsistenzaVO> vDichCons = consDAO.getDichiarazioniConsistenza(azCollVO.getIdAzienda());
            boolean flagTrovata = false;
            // Record corrente
            // Se trovo una dichiarazione di consistenza che ha la data di
            // inserimento posteriore
            // alla data di creazione dell'associazione storicizzo
            if ((vDichCons != null) && (vDichCons.size() > 0))
            {
              for (int j = 0; j < vDichCons.size(); j++)
              {
                ConsistenzaVO consVO = (ConsistenzaVO) vDichCons.get(j);
                if (azCollVOTmp.getDataInizioValidita().before(consVO.getDataInserimentoDichiarazione()))
                {
                  flagTrovata = true;
                  break;
                }
              }
            }

            // Se la dichiarazione è già stata storicizzata nella giornata
            // odierna e non ha dichiarazioni di consistenza
            // sucessive (flagTrovata = true) aggiornare solo il record senza
            // storicizzare
            if (!flagTrovata)
            {
              flagStoricoCurr = true;
              azCollVOTmp.setDataIngresso(azCollVO.getDataIngresso());
              // se inserita data uscita devo settare anche data fine validita
              if (azCollVO.getDataUscita() != null)
              {
                azCollVOTmp.setDataUscita(azCollVO.getDataUscita());
                azCollVOTmp.setDataFineValidita(new Date());
              }
            }
            else
            // Storicizzo
            {
              azCollVOTmp.setDataFineValidita(new Date());
            }
          }
          else
          // Storicizzo
          {
            azCollVOTmp.setDataFineValidita(new Date());
          }

          azCollVOTmp.setDataAggiornamento(new Date());
          azCollVOTmp.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());

          aDAO.updateAziendaCollegata(azCollVOTmp);

        }

        if (!flagStoricoCurr) // O nuova azienda o dopo storicizzazione
        {
          azCollVO.setDataAggiornamento(new Date());
          azCollVO.setDataInizioValidita(new Date());
          azCollVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());

          if (azCollVO.getDataUscita() != null)
          {
            azCollVO.setDataFineValidita(new Date());
          }
          
          Long idSoggettoAssociato = null;
          //Arrivo da una modifica non devo aggiornare id_soggetto_associato
          if(flagModifica)
          {
            idSoggettoAssociato = azCollVOTmp.getIdSoggettoAssociato();
            azCollVO.setIdAziendaAssociata(azCollVOTmp.getIdAziendaAssociata());
          }
          else //Sono nell'inserimento
          {
            if(azCollVO.getSoggettoAssociato() != null)
            {
              idSoggettoAssociato = aDAO.insertSoggettoAssociato(azCollVO.getSoggettoAssociato());
            }
          }

          azCollVO.setIdSoggettoAssociato(idSoggettoAssociato);
          
          aDAO.insertAziendaCollegata(azCollVO);
        }
      }
    }
    catch (SolmrException se)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(se.getMessage());
    }
    catch (DataAccessException e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
  }

  /**
   * 
   * @param profile
   * @param vIdAziendaVO
   * @throws Exception
   * @throws SolmrException
   */
  public void eliminaAziendeCollegateBlocco(long idUtenteAggiornamento, Long idAziendaFather, Vector<Long> vIdAziendaCollegate) throws Exception, SolmrException
  {

    try
    {
      for (int i = 0; i < vIdAziendaCollegate.size(); i++)
      {
        Long idAziendaCollegata = (Long) vIdAziendaCollegate.get(i);
        AziendaCollegataVO azCollVOTmp = aDAO.findAziendaCollegataByPrimaryKey(idAziendaCollegata.longValue());
        boolean flagTrovata = false;
        if (azCollVOTmp != null)
        {
          Vector<ConsistenzaVO> vDichCons = consDAO.getDichiarazioniConsistenza(idAziendaFather);

          // Record corrente
          // Se trovo una dichiarazione di consistenza che ha la data di
          // inserimento posteriore
          // alla data di creazione dell'associazione storicizzo
          if ((vDichCons != null) && (vDichCons.size() > 0))
          {
            for (int j = 0; j < vDichCons.size(); j++)
            {
              ConsistenzaVO consVO = (ConsistenzaVO) vDichCons.get(j);
              if (azCollVOTmp.getDataInizioValidita().before(consVO.getDataInserimentoDichiarazione()))
              {
                azCollVOTmp.setDataFineValidita(new Date());
                azCollVOTmp.setIdUtenteAggiornamento(idUtenteAggiornamento);
                aDAO.updateAziendaCollegata(azCollVOTmp);
                flagTrovata = true;
                break;
              }
            }
          }

          if (!flagTrovata)
          {            
            aDAO.deleteAziendaCollegata(azCollVOTmp.getIdAziendaCollegata());
            if(azCollVOTmp.getIdSoggettoAssociato() != null)
            {
              aDAO.deleteSoggettoAssociato(azCollVOTmp.getIdSoggettoAssociato().longValue());
              Vector<AziendaCollegataVO> vAziendeCollegate = aDAO.getAllAziendeCollegateIdSoggettoAssociato(
                  idAziendaFather, azCollVOTmp.getIdSoggettoAssociato());
              if (vAziendeCollegate != null)
              {
                for (int j = 0; j < vAziendeCollegate.size(); j++)
                {
                  boolean flagTrovataStorico = false;
                  AziendaCollegataVO azCollVO = (AziendaCollegataVO) vAziendeCollegate.get(j);
                  for (int k = 0; k < vDichCons.size(); k++)
                  {
                    ConsistenzaVO consVO = (ConsistenzaVO) vDichCons.get(k);
                    if (azCollVO.getDataInizioValidita().before(consVO.getDataInserimentoDichiarazione()))
                    {
                      flagTrovataStorico = true;
                      break;
                    }
                  }
  
                  if (!flagTrovataStorico)
                  {
                    aDAO.deleteAziendaCollegata(azCollVO.getIdAziendaCollegata());
                    aDAO.deleteSoggettoAssociato(azCollVO.getIdSoggettoAssociato().longValue());
                  }
                }
              }
            }
            // Record storicizzati
            // Se non ho trovato una dichiarazione di consistenza posteriore al
            // record corrente
            // verifico tra quelli storicizzati se hanno una dichiarazione
            // successiva!!!
            else
            {
              Vector<AziendaCollegataVO> vAziendeCollegate = aDAO.getAllAziendeCollegateIdAziendaAssociata(idAziendaFather, azCollVOTmp.getIdAziendaAssociata());
              if (vAziendeCollegate != null)
              {
                for (int j = 0; j < vAziendeCollegate.size(); j++)
                {
                  boolean flagTrovataStorico = false;
                  AziendaCollegataVO azCollVO = (AziendaCollegataVO) vAziendeCollegate.get(j);
                  for (int k = 0; k < vDichCons.size(); k++)
                  {
                    ConsistenzaVO consVO = (ConsistenzaVO) vDichCons.get(k);
                    if (azCollVO.getDataInizioValidita().before(consVO.getDataInserimentoDichiarazione()))
                    {
                      flagTrovataStorico = true;
                      break;
                    }
                  }
  
                  if (!flagTrovataStorico)
                  {
                    aDAO.deleteAziendaCollegata(azCollVO.getIdAziendaCollegata());
                  }
                }
              }

            }
            
          }

        }
        // Se == null vuol dire che cercavo di eliminare una azienda che era
        // solo messa in sessione
        // col tasto inserimento dentro aziendeCollegateModifica, quindi non
        // ancora salvata su DB.

      }
    }
    catch (SolmrException se)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(se.getMessage());
    }
    catch (DataAccessException e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
  }

  public boolean isCUAAAlreadyPresentInsediate(String cuaa) throws Exception
  {
    try
    {
      return wDAO.isCUAAAlreadyPresentInsediate(cuaa);
    }
    catch (DataAccessException exc)
    {
      throw new Exception(exc.getMessage());
    }
  }

  public Vector<AnagAziendaVO> getAziendaByCriterioCessataAndProvvisoria(String CUAA) throws Exception, SolmrException
  {
    Vector<AnagAziendaVO> result = null;
    try
    {
      result = aDAO.getAziendaByCriterioCessataAndProvvisoria(CUAA, true, false);
      if (result != null)
      {
        for (int i = 0; i < result.size(); i++)
        {
          AnagAziendaVO aaVO = (AnagAziendaVO) result.elementAt(i);
          DelegaVO dVO = delegaDAO.getDelegaByAziendaAndIdProcedimento(aaVO.getIdAzienda(), (Long) SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
          aaVO.setDelegaVO(dVO);
          aaVO.setPossiedeDelegaAttiva(aDAO.controllaPresenzaDelega(aaVO));
        }
      }
    }
    catch (SolmrException se)
    {
      throw se;
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }

  public void insediamentoAtomico(AnagAziendaVO modAnagAziendaVO, PersonaFisicaVO modPersonaVO, 
      HttpServletRequest request, AnagAziendaVO anagAziendaVO, 
      long idUtenteAggiornamento) throws Exception, Exception
  {
    /*
     * Se sono arrivato qui vuol dire che posso salvare i dati Storicizzare
     * tenendo presente che: Se in data corrente, per l'azienda in questione, è
     * già stata effettuata una variazione storicizzata, il sistema effettua
     * direttamente la correzione dei dati. (E' ammessa una sola storicizzazione
     * al giorno) a meno che venga modificato il cuaa ed in tal caso
     * storicizzare. Nel caso dell'aggiornamento dei dati, senza tenere storia
     * della modifica, il sistema memorizza però l'utente che opera e la data.
     * Nella storicizzazione indicare la motivazione della modifica
     * "Dichiarazione di insediamento".
     */

    boolean personaInserita = false;

    // Recupero la persona fisica presente su DB che si intende modificare
    // partendo da id_azienda
    PersonaFisicaVO oldPersonaFisicaVO = getRappresentanteLegaleFromIdAnagAzienda(modAnagAziendaVO.getIdAnagAzienda());

    PersonaFisicaVO personaForBonificaVO = null;
    try
    {
      personaForBonificaVO = getPersonaFisica(modPersonaVO.getCodiceFiscale().toUpperCase());
    }
    catch (SolmrException se)
    {
    }

    // Se non trovo un'altra persona con quel codice fiscale allora controllo
    // che non sia stata modificata
    // l'identità della persona e cioè non siano stati modificati(cognome, nome,
    // data di nascita e comune
    // di nascita)
    if (personaForBonificaVO == null)
    {
      if (modPersonaVO.isEqualIdentity(oldPersonaFisicaVO))
      {
        // Effettuo la modifica su DB_PERSONA_FISICA ed eventualmente storicizzo
        // la residenza su
        // DB_STORICO_RESIDENZA
        modPersonaVO.setIdPersonaFisica(oldPersonaFisicaVO.getIdPersonaFisica());
        // Se la data di inizio residenza è diversa da quella di sistema allora
        // confronto la persona
        // fisica prelevata dal DB con la nuova persona fisica derivante dai
        // nuovi dati inseriti come input
        // dall'utente.
        if (oldPersonaFisicaVO.getDataInizioResidenza().compareTo(DateUtils.parseDate(DateUtils.getCurrent())) != 0)
        {
          // Se è diversa la mando ad una pagina che richiede se l'utente
          // intende effettuare una modifica o una correzione
          if (!modPersonaVO.equalsResidenza(oldPersonaFisicaVO))
          {
            // Devo storicizzare
            storicizzaPersonaFisica(request, anagAziendaVO, idUtenteAggiornamento, modPersonaVO);
            personaInserita = true;
          }
          else
            updateRappLegale(modPersonaVO, idUtenteAggiornamento);
        }
        else
          updateRappLegale(modPersonaVO, idUtenteAggiornamento);
      }
      // Altrimenti cesso su DB_CONTITOLARE il ruolo del vecchio soggetto e
      // inserisco un nuovo soggetto
      // legandolo all'azienda con un ruolo attivo
      else
      {
        cessaLegameBetweenPersonaAndAzienda(oldPersonaFisicaVO.getIdSoggetto(), anagAziendaVO.getIdAzienda());
        insertRappLegaleTitolare(anagAziendaVO.getIdAzienda(), modPersonaVO, idUtenteAggiornamento);
      }
      PersonaFisicaVO personaModVO = getTitolareORappresentanteLegaleAzienda(anagAziendaVO.getIdAzienda(), DateUtils.parseDate(anagAziendaVO.getDataSituazioneAlStr()));
      request.getSession().removeAttribute("personaVO");
      request.getSession().setAttribute("personaVO", personaModVO);
      personaInserita = true;
    }
    // Se invece la trova sostituisce il vecchio soggetto con il nuovo in tutti
    // i legami con le aziende
    // ed effettue le modifiche su DB_PERSONA_FISICA ed eventualmente la
    // storicizzazione della residenza
    // su DB_STORICO_RESIDENZA
    else
    {
      // Se la data di inizio residenza è diversa da quella di sistema allora
      // confronto la persona
      // fisica prelevata dal DB con la nuova persona fisica derivante dai nuovi
      // dati inseriti come input
      // dall'utente.
      if (personaForBonificaVO.getDataInizioResidenza().compareTo(DateUtils.parseDate(DateUtils.getCurrent())) != 0)
      {
        // Se è diversa la mando ad una pagina che richiede se l'utente intende
        // effettuare una modifica o una correzione
        if (!modPersonaVO.equalsResidenza(oldPersonaFisicaVO))
        {
          // Devo storicizzare
          storicizzaPersonaFisica(request, anagAziendaVO, idUtenteAggiornamento, modPersonaVO);
          personaInserita = true;
        }
        else
          if (!modPersonaVO.equalsResidenza(personaForBonificaVO))
          {
            // Devo storicizzare
            storicizzaPersonaFisica(request, anagAziendaVO, idUtenteAggiornamento, modPersonaVO);
            personaInserita = true;
          }
          else
          {
            changeLegameBetweenPersoneAndAziende(personaForBonificaVO.getIdSoggetto(), oldPersonaFisicaVO.getIdSoggetto(), null);
            modPersonaVO.setIdPersonaFisica(personaForBonificaVO.getIdPersonaFisica());
            modPersonaVO.setIdSoggetto(personaForBonificaVO.getIdSoggetto());
            updateRappLegale(modPersonaVO, idUtenteAggiornamento);
          }
      }
      else
      {
        changeLegameBetweenPersoneAndAziende(personaForBonificaVO.getIdSoggetto(), oldPersonaFisicaVO.getIdSoggetto(), null);
        // ///////////////////////////////////////////////////
        modPersonaVO.setIdPersonaFisica(personaForBonificaVO.getIdPersonaFisica());
        modPersonaVO.setIdSoggetto(personaForBonificaVO.getIdSoggetto());
        updateRappLegale(modPersonaVO, idUtenteAggiornamento);
      }
    }

    // Se non ho ancora messo i dati in sessione devo farlo ora
    if (!personaInserita)
    {
      // Recupero il titolare
      PersonaFisicaVO personaModVO = getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
      request.getSession().removeAttribute("personaVO");
      // Metto l'attributo modificato in sessione
      request.getSession().setAttribute("personaVO", personaModVO);
    }
    // elimino l'attributo che ho usato per la modifica
    request.getSession().removeAttribute("modPersonaVO");

    // Controlla i dati relativi all'azienda
    updateAzienda(request, idUtenteAggiornamento, modAnagAziendaVO);

  }

  // Storicizzazione persona fisica
  private void storicizzaPersonaFisica(HttpServletRequest request, 
      AnagAziendaVO anagAziendaVO, long idUtenteAggiornamento, PersonaFisicaVO pfVOModifica) 
   throws Exception
  {
    // Recupero i dati inseriti dall'utente
    pfVOModifica.setIsModificaResidenza(false);
    pfVOModifica.setIsStoricizzaResidenza(true);

    // Recupero la persona fisica presente su DB che si intende modificare
    // partendo da id_azienda
    PersonaFisicaVO oldPersonaFisicaVO = getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());

    PersonaFisicaVO personaForBonificaVO = null;
    try
    {
      personaForBonificaVO = getPersonaFisica(pfVOModifica.getCodiceFiscale().toUpperCase());
    }
    catch (Exception se1)
    {
    }

    // Effettuo la storicizzazione
    if (personaForBonificaVO == null)
      updateRappLegale(pfVOModifica, idUtenteAggiornamento);
    else
    {
      pfVOModifica.setIdPersonaFisica(personaForBonificaVO.getIdPersonaFisica());
      pfVOModifica.setIdSoggetto(personaForBonificaVO.getIdSoggetto());
      changeLegameBetweenPersoneAndAziende(personaForBonificaVO.getIdSoggetto(), oldPersonaFisicaVO.getIdSoggetto(), null);
      updateRappLegale(pfVOModifica, idUtenteAggiornamento);
    }
    request.getSession().removeAttribute("personaVO");

    PersonaFisicaVO personaModVO = getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
    request.getSession().removeAttribute("personaVO");
    request.getSession().setAttribute("personaVO", personaModVO);
  }

  private void updateAzienda(HttpServletRequest request, long idUtenteAggiornamento, AnagAziendaVO voAnagModifica) throws Exception
  {

    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession().getAttribute("anagAziendaVO");

    // Se viene modificato il CUAA devo storicizzare comunque
    /*
     * boolean storicizza = anagAziendaVO.getCUAA() == null ||
     * !anagAziendaVO.getCUAA().equalsIgnoreCase(voAnagModifica.getCUAA());
     */

    // if (DateUtils.isToday(anagAziendaVO.getDataInizioVal()) && !storicizza)
    if (DateUtils.isToday(anagAziendaVO.getDataInizioVal()))
    {
      updateAzienda(voAnagModifica);
      AnagAziendaVO nuovoAnagAziendaVO = new AnagAziendaVO();
      nuovoAnagAziendaVO = findAziendaAttiva(voAnagModifica.getIdAzienda());
      nuovoAnagAziendaVO.setTipiFormaGiuridica(nuovoAnagAziendaVO.getTipoFormaGiuridica().getDescription());
      nuovoAnagAziendaVO.setStrCCIAAnumeroREA(voAnagModifica.getStrCCIAAnumeroREA());
      request.getSession().removeAttribute("anagAziendaVO");
      request.getSession().setAttribute("anagAziendaVO", nuovoAnagAziendaVO);
    }
    else
    {
      // Se sono stati modificati i dati effettuo una storicizzazione
      // dell'azienda
      if (!voAnagModifica.equalsForUpdateNew(anagAziendaVO))
      {
        // Inserisco come motivo della modifica "DICHIARAZIONE INSEDIAMENTO"
        String motivoModifica = SolmrConstants.DICHIARAZIONE_INSEDIAMENTO;
        voAnagModifica.setMotivoModifica(motivoModifica);
        // Storicizzo l'azienda
        storicizzaAzienda(voAnagModifica, idUtenteAggiornamento);
        // Recupero i nuovi dati della ditta modificata
        AnagAziendaVO nuovoAnagAziendaVO = new AnagAziendaVO();

        nuovoAnagAziendaVO = findAziendaAttiva(voAnagModifica.getIdAzienda());
        nuovoAnagAziendaVO.setTipiFormaGiuridica(nuovoAnagAziendaVO.getTipoFormaGiuridica().getDescription());
        nuovoAnagAziendaVO.setStrCCIAAnumeroREA(voAnagModifica.getStrCCIAAnumeroREA());
        request.getSession().removeAttribute("anagAziendaVO");
        request.getSession().setAttribute("anagAziendaVO", nuovoAnagAziendaVO);
      }
    }
    request.getSession().removeAttribute("modAnagAziendaVO");
  }
  
  
  public String[] getCUAACollegati(String cuaa) throws Exception
  {
    try
    {
      if (cuaa==null) return null;
      return aDAO.getCUAACollegati(cuaa);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public Vector<Long> getIdAnagAziendeCollegatebyCUAA(String cuaa) throws Exception
  {
    try
    {
      return aDAO.getIdAnagAziendeCollegatebyCUAA(cuaa);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  
  public boolean getDelegaBySocio(String codFiscIntermediario, Long idAziendaAssociata) 
    throws Exception
  {
    try
    {
      return aDAO.getDelegaBySocio(codFiscIntermediario, idAziendaAssociata);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public AziendaCollegataVO findAziendaCollegataByFatherAndSon(Long idAziendaFather, Long idAziendaSon,
      Date dataSituazione) throws Exception
  {
    try
    {
      return aDAO.findAziendaCollegataByFatherAndSon(idAziendaFather, idAziendaSon, dataSituazione);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  public boolean isSoggettoAssociatoByFatherAndSon(Long idAziendaFather, String cuaaSon,
      Date dataSituazione) throws Exception
  {
    try
    {
      return aDAO.isSoggettoAssociatoByFatherAndSon(idAziendaFather, cuaaSon, dataSituazione);
    }
    catch (DataAccessException e)
    {
      throw new Exception(e.getMessage());
    }
  }
  
  

}
