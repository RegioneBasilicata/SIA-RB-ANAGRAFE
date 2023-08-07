package it.csi.solmr.business.anag;

import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO;
import it.csi.smranag.smrgaa.integration.AnagrafeGaaDAO;
import it.csi.smranag.smrgaa.integration.DocumentoGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.ParticellaGaaDAO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.ElencoAziendeParticellaVO;
import it.csi.solmr.dto.anag.EsitoControlloParticellaVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.FoglioVO;
import it.csi.solmr.dto.anag.ParticellaAziendaVO;
import it.csi.solmr.dto.anag.ParticellaCertificataVO;
import it.csi.solmr.dto.anag.ParticellaUtilizzoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.dto.anag.services.FabbricatoParticellaVO;
import it.csi.solmr.dto.anag.sian.SianTerritorioVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.AnagWriteDAO;
import it.csi.solmr.integration.anag.ConduzioneParticellaDAO;
import it.csi.solmr.integration.anag.DocumentoDAO;
import it.csi.solmr.integration.anag.FabbricatoDAO;
import it.csi.solmr.integration.anag.FascicoloDAO;
import it.csi.solmr.integration.anag.ParticellaCertificataDAO;
import it.csi.solmr.integration.anag.StoricoUnitaArboreaDAO;
import it.csi.solmr.integration.anag.UteDAO;
import it.csi.solmr.integration.anag.UtilizzoConsociatoDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/Fascicolo",mappedName="comp/env/solmr/anag/Fascicolo")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class FascicoloBean implements FascicoloLocal
{
  /**
   * 
   */
  private static final long serialVersionUID = 8157612284684074349L;

  SessionContext sessionContext;
  private transient FascicoloDAO fascicoloDAO = null;
  private transient CommonDAO cDAO = null;
  private transient AnagWriteDAO wDAO = null;
  private transient ParticellaCertificataDAO particellaCertificataDAO = null;
  private transient DocumentoDAO documentoDAO = null;
  //private transient TipoVarietaDAO tipoVarietaDAO = null;
  private transient UtilizzoConsociatoDAO utilizzoConsociatoDAO = null;
  private transient ParticellaGaaDAO particellaGaaDAO = null;
  private transient StoricoUnitaArboreaDAO storicoUnitaArboreaDAO = null;
  private transient ConduzioneParticellaDAO conduzioneParticellaDAO = null;
  private transient FabbricatoDAO fabbricatoDAO = null;
  private transient DocumentoGaaDAO documentoGaaDAO = null;
  private transient UteDAO uteDAO = null;
  private transient AnagrafeGaaDAO aGaaDAO = null;

 
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
      fascicoloDAO = new FascicoloDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      cDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      wDAO = new AnagWriteDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      particellaCertificataDAO = new ParticellaCertificataDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      documentoDAO = new DocumentoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      //tipoVarietaDAO = new TipoVarietaDAO(
        //  SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      utilizzoConsociatoDAO = new UtilizzoConsociatoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      particellaGaaDAO = new ParticellaGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      storicoUnitaArboreaDAO = new StoricoUnitaArboreaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      conduzioneParticellaDAO = new ConduzioneParticellaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      fabbricatoDAO = new FabbricatoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      documentoGaaDAO = new DocumentoGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      uteDAO = new UteDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      aGaaDAO = new AnagrafeGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

  public Vector<UteVO> getUTE(Long idAzienda, Boolean storico) throws Exception,
      SolmrException
  {
    Vector<UteVO> collUte = null;
    try
    {
      collUte = fascicoloDAO.getUTE(idAzienda, storico);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }

    return collUte;
  }

  public UteVO getUteById(Long idUte) throws Exception
  {
    UteVO uteVO = null;
    try
    {
      uteVO = fascicoloDAO.getUteById(idUte);
      uteVO.setvUteAtecoSec(uteDAO.getElencoAtecoSecUte(idUte));
    }
    catch (NotFoundException dae)
    {
      return null;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return uteVO;
  }

  /*public UteVO serviceGetUteById(Long idUte) throws Exception
  {
    UteVO uteVO = null;
    try
    {
      uteVO = fascicoloDAO.getUteById(idUte);
    }
    catch (NotFoundException dae)
    {
      throw new Exception(SolmrErrors.EXC_NOT_FOUND_PK);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return uteVO;
  }*/

  /*public UteVO[] serviceGetUteByIdRange(Long idUte[]) throws Exception
  {
    UteVO uteVO[] = null;
    try
    {
      uteVO = fascicoloDAO.getUteByIdRange(idUte);
    }
    catch (NotFoundException dae)
    {
      return null;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return uteVO;
  }*/

  public void deleteUTE(Long idUte) throws SolmrException, Exception
  {
    try
    {
      fascicoloDAO.mayIDeleteUTE(idUte);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    try
    {
      fascicoloDAO.deleteUTEAtecoSec(idUte);
      fascicoloDAO.deleteUTE(idUte);
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  public void updateUTE(UteVO uteVO) throws SolmrException, Exception
  {
    try
    {
      String codiceOte = "";
      String descrizioneOte = "";
      String codiceAteco = "";
      String descrizioneAteco = "";
      if (!uteVO.getCodeOte().equals(""))
      {
        codiceOte = uteVO.getCodeOte();
      }

      if (!uteVO.getDescOte().equals(""))
      {
        descrizioneOte = uteVO.getDescOte();
      }

      if (!uteVO.getCodeAteco().equals(""))
      {
        codiceAteco = uteVO.getCodeAteco();
      }

      if (!uteVO.getDescAteco().equals(""))
      {
        descrizioneAteco = uteVO.getDescAteco();
      }

      if (!codiceOte.equals("") || !descrizioneOte.equals(""))
      {
        Vector<CodeDescription> ote = getTipiAttivitaOTE(codiceOte, descrizioneOte);
        CodeDescription idOte = (CodeDescription) ote.firstElement();
        uteVO.setIdOte(idOte.getCode().toString());
      }
      if (!codiceAteco.equals("") || !descrizioneAteco.equals(""))
      {
        Vector<CodeDescription> ateco = cDAO.getTipiAttivitaATECO(codiceAteco, descrizioneAteco);
        if(ateco != null)
        {
          CodeDescription idAteco = (CodeDescription) ateco.firstElement();
          uteVO.setIdAteco(idAteco.getCode().toString());
        }
      }
      fascicoloDAO.updateUTE(uteVO);
      
      uteDAO.storicizzaAtecoSecUte(uteVO.getIdUte().longValue());
      if(uteVO.getvUteAtecoSec() != null)
      {
        Vector<AziendaAtecoSecVO> vAzienaAtecoSec = aGaaDAO.getListActiveAziendaAtecoSecByIdAzienda(uteVO.getIdAzienda());
        for(int i=0;i<uteVO.getvUteAtecoSec().size();i++)
        {
          uteVO.getvUteAtecoSec().get(i).setIdUte(uteVO.getIdUte());
          uteVO.getvUteAtecoSec().get(i).setDataInizioValidita(new Date());
          uteDAO.insertUteAtecoSecondari(uteVO.getvUteAtecoSec().get(i));
          
          //Controllo che non sia già presente nell'azienda eventualmente lo aggiungo!!
          if(vAzienaAtecoSec == null)
          {
            //Aggiungo tutto
            AziendaAtecoSecVO aziendaAtecoSecVO = new AziendaAtecoSecVO();
            aziendaAtecoSecVO.setIdAzienda(uteVO.getIdAzienda());
            aziendaAtecoSecVO.setIdAttivitaAteco(uteVO.getvUteAtecoSec().get(i).getIdAttivitaAteco());
            aziendaAtecoSecVO.setDataInizioValidita(new Date());
            aGaaDAO.insertAziendaAtecoSec(aziendaAtecoSecVO);
          }
          else
          {
            //Controllo se nn c'e' aggiungo!!!
            Boolean trovato = false;
            for(int h=0;h<vAzienaAtecoSec.size();h++)
            {
              if(vAzienaAtecoSec.get(h).getIdAttivitaAteco() == uteVO.getvUteAtecoSec().get(i).getIdAttivitaAteco())
              {
                trovato = true;
                break;
              }                      
            }
            
            if(!trovato)
            {
              AziendaAtecoSecVO aziendaAtecoSecVO = new AziendaAtecoSecVO();
              aziendaAtecoSecVO.setIdAzienda(uteVO.getIdAzienda());
              aziendaAtecoSecVO.setIdAttivitaAteco(uteVO.getvUteAtecoSec().get(i).getIdAttivitaAteco());
              aziendaAtecoSecVO.setDataInizioValidita(new Date());
              aGaaDAO.insertAziendaAtecoSec(aziendaAtecoSecVO);                      
            }
            
          }
          
          
        }
      }
    }
    catch (Exception ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  public Vector<CodeDescription> getTipiAttivitaOTE(String code, String description)
      throws Exception, NotFoundException
  {
    Vector<CodeDescription> result = null;
    try
    {
      result = cDAO.getAttivitaLike(code, description,
          SolmrConstants.TAB_TIPO_ATTIVITA_OTE);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
    return result;
  }

  // Metodo per recuperare l'elenco delle unità produttive valide associate ad
  // un'azienda agricola
  public Vector<UteVO> getElencoUteAttiveForAzienda(Long idAzienda)
      throws SolmrException, Exception
  {
    Vector<UteVO> elencoUteAttive = null;
    try
    {
      elencoUteAttive = fascicoloDAO.getElencoUteAttiveForAzienda(idAzienda);
    }
    catch (NotFoundException ne)
    {
      throw new SolmrException((String) AnagErrors.get("ERR_UTE_ATTIVE"));
    }
    catch (DataAccessException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoUteAttive;
  }

  // Metodo per recuperare l'elenco delle sezioni relative ad uno specifico
  // comune
  public Vector<CodeDescription> getSezioniByComune(String istatComune) throws SolmrException,
      Exception
  {
    Vector<CodeDescription> elencoSezioni = null;
    try
    {
      elencoSezioni = fascicoloDAO.getSezioniByComune(istatComune);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoSezioni;
  }

  // Metodo per recuperare l'elenco dei fogli in relazione al comune o stato
  // estero
  // ed eventualmente la sezione
  public Vector<FoglioVO> getFogliByComuneAndSezione(String istatComune, String sezione,
      Long foglio) throws SolmrException, Exception
  {
    Vector<FoglioVO> elencoFogli = null;
    try
    {
      elencoFogli = fascicoloDAO.getFogliByComuneAndSezione(istatComune,
          sezione, foglio);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoFogli;
  }

  // Metodo per recuperare l'elenco delle particelle in relazione al comune o
  // stato estero
  // e il foglio
  public Vector<ParticellaVO> getParticelleByParametri(String descrizioneComune, Long foglio,
      String sezione, Long particella, String flagEstinto)
      throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO.getParticelleByParametri(
          descrizioneComune, foglio, sezione, particella, flagEstinto);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per recuperare la sezione a partire dall'istat del comune e dalla
  // sezione stessa
  public String ricercaSezione(String istatComune, String sezione)
      throws SolmrException, Exception
  {
    String sezioneParticella = null;
    try
    {
      sezioneParticella = fascicoloDAO.ricercaSezione(istatComune, sezione);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return sezioneParticella;
  }

  // Metodo per recuperare il foglio in relazione al comune o stato estero, il
  // foglio
  // stesso ed eventualmente la sezione
  public FoglioVO ricercaFoglio(String istatComune, String sezione, Long foglio)
      throws SolmrException, Exception
  {
    FoglioVO foglioVO = null;
    try
    {
      foglioVO = fascicoloDAO.ricercaFoglio(istatComune, sezione, foglio);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return foglioVO;
  }

  // Metodo per recuperare la particella in relazione al comune o stato estero
  // e il foglio
  public ParticellaVO ricercaParticellaAttiva(String istatComune,
      String sezione, Long foglio, Long particella, String subalterno)
      throws SolmrException, Exception
  {
    ParticellaVO particellaVO = null;
    try
    {
      particellaVO = fascicoloDAO.ricercaParticellaAttiva(istatComune, sezione,
          foglio, particella, subalterno);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return particellaVO;
  }

  // Metodo per controllare che una particella non sia già attribuita ad una
  // azienda
  public void checkParticellaByAzienda(Long idParticella, Long idAzienda)
      throws SolmrException, Exception
  {
    try
    {
      fascicoloDAO.checkParticellaByAzienda(idParticella, idAzienda);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
  }

  // Metodo per recuperare l'elenco delle aziende che hanno in conduzione la
  // particella selezionata
  public Vector<ElencoAziendeParticellaVO> elencoAziendeByParticellaAndConduzione(Long idParticella,
      Long idAzienda) throws SolmrException, Exception
  {
    Vector<ElencoAziendeParticellaVO> elencoAziende = null;
    try
    {
      elencoAziende = fascicoloDAO.elencoAziendeByParticellaAndConduzione(
          idParticella, idAzienda);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoAziende;
  }

  // Metodo per recuperare il valore dell'ultima data fine conduzione delle
  // particelle
  // associate ad un azienda agricola
  public java.util.Date getMaxDataFineConduzione(Long idParticella,
      Long idAzienda) throws SolmrException, Exception
  {
    java.util.Date dataFineConduzione = null;
    try
    {
      dataFineConduzione = fascicoloDAO.getMaxDataFineConduzione(idParticella,
          idAzienda);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return dataFineConduzione;
  }

  // Metodo per recuperare i tipi utilizzo attivi
  public Vector<CodeDescription> getTipiUtilizzoAttivi() throws SolmrException, Exception
  {
    Vector<CodeDescription> elencoTipiUtilizzo = null;
    try
    {
      elencoTipiUtilizzo = fascicoloDAO.getTipiUtilizzoAttivi();
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoTipiUtilizzo;
  }

  // Metodo per recuperare i tipi utilizzo attivi dato un indirizzo
  public Vector<CodeDescription> getTipiUtilizzoAttivi(int idIndirizzo) throws SolmrException,
      Exception
  {
    Vector<CodeDescription> elencoTipiUtilizzo = null;
    try
    {
      elencoTipiUtilizzo = fascicoloDAO.getTipiUtilizzoAttivi(idIndirizzo);
    }
    catch (DataAccessException se)
    {
      throw new Exception(se.getMessage());
    }
    return elencoTipiUtilizzo;
  }

  // Metodo per inserire un record in DB_PARTICELLA
  public Long insertParticella() throws SolmrException, Exception
  {
    Long primaryKey = null;
    try
    {
      primaryKey = fascicoloDAO.insertParticella();
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return primaryKey;
  }

  

  

  // Metodo per recuperare la particella provvisoria in relazione al comune o
  // stato estero
  // e il foglio
  public ParticellaVO ricercaParticellaProvvisoriaAttiva(String istatComune,
      String sezione, Long foglio) throws SolmrException, Exception
  {
    ParticellaVO particellaVO = null;
    try
    {
      particellaVO = fascicoloDAO.ricercaParticellaProvvisoriaAttiva(
          istatComune, sezione, foglio);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return particellaVO;
  }

  // Metodo per recuperare la particella in relazione al comune o stato estero
  // la sezione e il foglio indipendentemente dal fatto che sia attiva o meno
  public ParticellaVO ricercaParticella(String istatComune, String sezione,
      Long foglio, Long particella, String subalterno) throws SolmrException,
      Exception
  {
    ParticellaVO particellaVO = null;
    try
    {
      particellaVO = fascicoloDAO.ricercaParticella(istatComune, sezione,
          foglio, particella, subalterno);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return particellaVO;
  }

  public void cessaParticelleByIdParticellaRange(long idParticella[]) throws SolmrException,
  Exception {
    try {
      fascicoloDAO.cessaParticelleByIdParticellaRange(idParticella);
    }
    catch (DataAccessException se) {
      sessionContext.setRollbackOnly();
      throw new Exception(se.getMessage());
    }
  }

  // Metodo per recuperare il valore massimo inseribile per una particella non
  // presente in archivio
  // nata dal frazionamento di una particella già esistente in archivio che
  // presenta già una
  // particella nata dal suo frazionamento.....(Per chi lo legge in futuro so
  // che è complesso ma io l'hi
  // capito....) :)
  public double getMaxSupCatastaleInseribile(Long idParticella)
      throws SolmrException, Exception
  {
    double maxSupCatastale = 0;
    try
    {
      maxSupCatastale = fascicoloDAO.getMaxSupCatastaleInseribile(idParticella);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return maxSupCatastale;
  }

  // Metodo per recuperare la particella a partire dalla sua chiave primaria
  public ParticellaVO findParticellaByPrimaryKey(Long idStoricoParticella)
      throws SolmrException, Exception
  {
    ParticellaVO particellaVO = null;
    try
    {
      particellaVO = fascicoloDAO
          .findParticellaByPrimaryKey(idStoricoParticella);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return particellaVO;
  }

  // Metodo per recuperare l'elenco delle particelle ad uso fabbricato associate
  // ad una
  // unita produttiva
  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUte(Long idUte, boolean serra)
      throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO.getElencoParticelleFabbricatoByUte(idUte,
          serra);
    }
    catch (DataAccessException se)
    {
      throw new Exception(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per recuperare la somma delle superfici dei fabbricati che insistono
  // esclusivamente
  // sulla particella selezionata
  public String getSuperficiFabbricatiByParticella(Long idUte, Long idParticella)
      throws SolmrException, Exception
  {
    String sommaSuperfici = null;
    try
    {
      sommaSuperfici = fascicoloDAO.getSuperficiFabbricatiByParticella(idUte,
          idParticella);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return sommaSuperfici;
  }

  // Metodo per l'inserimento dei fabbricati
  public Long inserisciFabbricato(FabbricatoVO fabbricatoVO,
      Vector<ParticellaVO> elencoParticelleSelezionate, long idUtenteAggiornamento)
      throws SolmrException, Exception
  {
    Long primaryKey = null;
    try
    {
      primaryKey = fascicoloDAO.insertFabbricato(fabbricatoVO, idUtenteAggiornamento);
      fabbricatoVO.setIdFabbricato(primaryKey);
      if (elencoParticelleSelezionate != null
          && elencoParticelleSelezionate.size() != 0)
      {
        Iterator<ParticellaVO> iteraParticelleSelezionate = elencoParticelleSelezionate
            .iterator();
        while (iteraParticelleSelezionate.hasNext())
        {
          ParticellaVO particellaFabbricatoVO = (ParticellaVO) iteraParticelleSelezionate
              .next();
          fascicoloDAO.insertParticellaFabbricato(fabbricatoVO, particellaFabbricatoVO.getIdParticella());
        }
      }
    }
    catch (DataAccessException se)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(se.getMessage());
    }
    return primaryKey;
  }

  // Metodo per effettuare la ricerca dei fabbricati relativi all'azienda
  // agricola selezionata
  public Vector<FabbricatoVO> ricercaFabbricatiByAzienda(Long idAzienda,
      String dataSituazioneAl) throws SolmrException, Exception
  {
    Vector<FabbricatoVO> elencoFabbricati = null;
    try
    {
      elencoFabbricati = fascicoloDAO.ricercaFabbricatiByAzienda(idAzienda,
          dataSituazioneAl);
    }
    catch (DataAccessException se)
    {
      throw new Exception(se.getMessage());
    }
    return elencoFabbricati;
  }

  // Metodo per recuperare il fabbricato a partire dalla sua chiave primaria
  public FabbricatoVO findFabbricatoByPrimaryKey(Long idFabbricato)
      throws SolmrException, Exception
  {
    FabbricatoVO fabbricatoVO = null;
    try
    {
      fabbricatoVO = fascicoloDAO.findFabbricatoByPrimaryKey(idFabbricato);
    }
    catch (DataAccessException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return fabbricatoVO;
  }

  /**
   * Metodo per recuperare l'elenco delle particelle su cui insiste il
   * fabbricato selezionato Se modifica è uguale a true significa che sto
   * costruendo l'elenco delle particelle da usare nella modifica, quindi non
   * devo far vedere quelle particelle che hanno la DataFineValidità impostata
   * 
   * @param fabbricatoVO
   *          FabbricatoVO
   * @param modifica
   *          boolean
   * @return Vector
   * @throws Exception
   */
  public Vector<ParticellaVO> getElencoParticelleByFabbricato(FabbricatoVO fabbricatoVO,
      boolean modifica) throws Exception
  {
    try
    {
      return fascicoloDAO.getElencoParticelleByFabbricato(fabbricatoVO,
          modifica);
    }
    catch (DataAccessException se)
    {
      throw new Exception(se.getMessage());
    }
  }

  // Metodo per recuperare l'elenco delle particelle ad uso fabbricato associate
  // ad una
  // unita produttiva associabili ad un fabbricato
  public Vector<ParticellaVO> getElencoParticelleFabbricatoByUteAssociabili(Long idUte,
      Vector<Long> elencoParticelle, boolean serra) throws SolmrException,
      Exception
  {
    Vector<ParticellaVO> elencoParticelleAssociabili = null;
    try
    {
      elencoParticelleAssociabili = fascicoloDAO
          .getElencoParticelleFabbricatoByUteAssociabili(idUte,
              elencoParticelle, serra);
    }
    catch (DataAccessException se)
    {
      throw new Exception(se.getMessage());
    }
    return elencoParticelleAssociabili;
  }

  // Metodo per cessare l'utilizzo di una particella a fabbricato
  public void cessaUtilizzoParticellaFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException, Exception
  {
    try
    {
      fascicoloDAO.cessaUtilizzoParticellaFabbricato(fabbricatoVO);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
  }

  // Metodo per eliminare dei record da DB_FABBRICATO_PARTICELLA
  public void deleteParticellaFabbricato(ParticellaVO particellaFabbricatoVO)
      throws SolmrException, Exception
  {
    try
    {
      fascicoloDAO.deleteParticellaFabbricato(particellaFabbricatoVO);
    }
    catch (DataAccessException se)
    {
      throw new Exception(se.getMessage());
    }
  }

  // Metodo per effettuare la modifica del fabbricato
  public Long modificaFabbricato(FabbricatoVO fabbricatoVO,
      Vector<ParticellaVO> particelleForFabbricato, Vector<ParticellaVO> elencoParticelleAssociate,
      Vector<ParticellaVO> elencoParticelleAssociabili, long idUtenteAggiornamento, long idAzienda)
      throws SolmrException, Exception
  {
    Long primaryKey = null;
    try
    {
      /**
       * Devo controllare se sono stati modificati i dati del fabbricato
       */
      FabbricatoVO fabbricatoVOOld = fascicoloDAO
          .findFabbricatoByPrimaryKey(fabbricatoVO.getIdFabbricato());
      boolean dataInizioMinoreDataUltCons = true;
      boolean modificaFabbricato = fabbricatoVO
          .modificaFabbricato(fabbricatoVOOld);
      fabbricatoVO.setDataInizioValiditaFabbricato(fascicoloDAO
          .getDataInizioValidataFabbricato(fabbricatoVO.getIdFabbricato()
              .longValue()));
      dataInizioMinoreDataUltCons = fascicoloDAO.isDataInizioValida(idAzienda,
          fabbricatoVO.getDataInizioValiditaFabbricato());
      if (dataInizioMinoreDataUltCons && modificaFabbricato)
      {
        fascicoloDAO.cessaUtilizzoParticellaFabbricato(fabbricatoVO);
        // Setto la data inizio validità del nuovo fabbricato uguale alla data
        // di sistema
        try
        {
          fabbricatoVO.setDataInizioValiditaFabbricato(DateUtils
              .parseDate(DateUtils.getCurrent()));
        }
        catch (Exception e)
        {
        }
        primaryKey = fascicoloDAO.insertFabbricato(fabbricatoVO, idUtenteAggiornamento);
        fabbricatoVO.setIdFabbricato(primaryKey);
        if (particelleForFabbricato != null
            && particelleForFabbricato.size() != 0)
        {
          Iterator<ParticellaVO> iteraParticelleForFabbricato = particelleForFabbricato
              .iterator();
          while (iteraParticelleForFabbricato.hasNext())
          {
            ParticellaVO particellaFabbricatoVO = (ParticellaVO) iteraParticelleForFabbricato
                .next();
            fabbricatoVO
                .setIdUtilizzoParticellaFabbricato(particellaFabbricatoVO
                    .getIdUtilizzoParticella());
            SolmrLogger
                .debug(
                    this,
                    "Value of [DATA_INIZIO_VALIDITA] in first if condition in method modificaFabbricato in FascicoloBean: "
                        + fabbricatoVO.getDataInizioValiditaFabbricato() + "\n");
            fascicoloDAO.insertParticellaFabbricato(fabbricatoVO,
                particellaFabbricatoVO.getIdParticella());
          }
        }
        return primaryKey;
      }
      if (!dataInizioMinoreDataUltCons && modificaFabbricato)
      {
        fascicoloDAO.updateFabbricato(fabbricatoVO, idUtenteAggiornamento);
        primaryKey = fabbricatoVO.getIdFabbricato();
        fascicoloDAO.deleteParticellaFabbricato(primaryKey);
        if (particelleForFabbricato != null
            && particelleForFabbricato.size() != 0)
        {
          Iterator<ParticellaVO> iteraParticelleAssociabili = particelleForFabbricato
              .iterator();
          while (iteraParticelleAssociabili.hasNext())
          {
            ParticellaVO particellaAssociabileVO = (ParticellaVO) iteraParticelleAssociabili
                .next();
            fabbricatoVO.setIdUtilizzoParticellaFabbricato(fabbricatoVO
                .getIdUtilizzoParticellaFabbricato());
            Date temp = fabbricatoVO.getDataInizioValiditaFabbricato();
            fabbricatoVO.setDataInizioValiditaFabbricato(null);
            SolmrLogger
                .debug(
                    this,
                    "Value of [DATA_INIZIO_VALIDITA] in second if condition in method modificaFabbricato in FascicoloBean: "
                        + fabbricatoVO.getDataInizioValiditaFabbricato() + "\n");
            fascicoloDAO.insertParticellaFabbricato(fabbricatoVO,
                particellaAssociabileVO.getIdParticella());
            fabbricatoVO.setDataInizioValiditaFabbricato(temp);
          }
        }
        return primaryKey;
      }
      if (!modificaFabbricato)
      {
        fascicoloDAO.updateFabbricato(fabbricatoVO, idUtenteAggiornamento);
        primaryKey = fabbricatoVO.getIdFabbricato();
        if (elencoParticelleAssociate.size() > 0)
        {
          Iterator<ParticellaVO> iteraParticelleAssociate = elencoParticelleAssociate
              .iterator();
          while (iteraParticelleAssociate.hasNext())
          {
            ParticellaVO particellaVO = (ParticellaVO) iteraParticelleAssociate
                .next();
            if (fascicoloDAO.isDataInizioValida(idAzienda, particellaVO
                .getDataInizioValidita()))
            {
              fascicoloDAO.updateParticellaFabbricato(particellaVO);
            }
            else
            {
              fascicoloDAO.deleteParticellaFabbricato(particellaVO);
            }
          }
        }
        if (elencoParticelleAssociabili.size() > 0)
        {
          Iterator<ParticellaVO> iteraParticelleAssociabili = elencoParticelleAssociabili
              .iterator();
          while (iteraParticelleAssociabili.hasNext())
          {
            ParticellaVO particellaAssociabileVO = (ParticellaVO) iteraParticelleAssociabili
                .next();
            if (fascicoloDAO.isDataInizioValida(idAzienda, fabbricatoVO
                .getDataInizioValiditaFabbricato()))
            {

              Date temp = fabbricatoVO.getDataInizioValiditaFabbricato();
              fabbricatoVO.setDataInizioValiditaFabbricato(null);
              fascicoloDAO.insertParticellaFabbricato(fabbricatoVO,
                  particellaAssociabileVO.getIdParticella());
              fabbricatoVO.setDataInizioValiditaFabbricato(temp);
            }
            else
            {
              fascicoloDAO.insertParticellaFabbricato(fabbricatoVO,
                  particellaAssociabileVO.getIdParticella());
            }
          }
        }

        primaryKey = fabbricatoVO.getIdFabbricato();
        return primaryKey;
      }
    }
    catch (DataAccessException se)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(se.getMessage());
    }
    return primaryKey;
  }

  // Metodo per eliminare dei record da DB_FABBRICATO_PARTICELLA a partire dal
  // fabbricato
  public void deleteParticellaFabbricatoByIdFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException, Exception
  {
    try
    {
      fascicoloDAO.deleteParticellaFabbricatoByIdFabbricato(fabbricatoVO);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
  }

  // Metodo per recuperare il numero di record preseenti su
  // DB_FABBRICATO_PARTICELLA
  // a partire dall'id_fabbricato
  public int getNumRecordParticellaFabbricato(Long idFabbricato)
      throws SolmrException, Exception
  {
    int numRecord = 0;
    try
    {
      numRecord = fascicoloDAO.getNumRecordParticellaFabbricato(idFabbricato);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return numRecord;
  }

  // Metodo per eliminare un record da DB_FABBRICATO a partire dal fabbricato
  public void deleteFabbricato(FabbricatoVO fabbricatoVO)
      throws SolmrException, Exception
  {
    try
    {
      fascicoloDAO.deleteFabbricato(fabbricatoVO);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
  }

  public boolean isDataInizioValida(long idAzienda, Date dataInizio)
      throws SolmrException, Exception
  {
    try
    {
      return fascicoloDAO.isDataInizioValida(idAzienda, dataInizio);
    }
    catch (DataAccessException da)
    {
      throw new Exception(da.getMessage());
    }
  }

  // Metodo per eliminare un fabbricato e i suoi legami
  public void eliminaFabbricato(FabbricatoVO fabbricatoVO, long idAzienda)
      throws SolmrException, Exception
  {
    try
    {
      /**
       * Controllo che il fabbricato non sia presente in una dichiarazione di
       * consistenza
       */
      if (fascicoloDAO.isDataInizioValida(idAzienda, fabbricatoVO
          .getDataInizioValiditaFabbricato()))
      {
        throw new SolmrException(
            "Impossibile procedere con l''operazione. L''elemento "
                + "selezionato compare in una dichiarazione di consistenza. ");
      }
      fascicoloDAO.deleteParticellaFabbricatoByIdFabbricato(fabbricatoVO);
      int numRecord = fascicoloDAO
          .getNumRecordParticellaFabbricato(fabbricatoVO.getIdFabbricato());
      if (numRecord == 0)
      {
        fascicoloDAO.deleteFabbricato(fabbricatoVO);
      }
    }
    catch (SolmrException se)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(se.getMessage());
    }
    catch (DataAccessException da)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(da.getMessage());
    }
  }

  // Metodo per recuperare l'elenco delle unità produttive valide ad una certa
  // data associate
  // ad un'azienda agricola
  public Vector<UteVO> getElencoUteAttiveForDateAndAzienda(Long idAzienda, String data)
      throws SolmrException, Exception
  {
    Vector<UteVO> elencoUteAttive = null;
    try
    {
      elencoUteAttive = fascicoloDAO.getElencoUteAttiveForDateAndAzienda(
          idAzienda, data);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoUteAttive;
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola ed eventualmente
  // ad un'unità produttiva selezionata
  public Vector<ParticellaVO> getElencoParticelleForUteAndAzienda(Long idAzienda)
      throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO
          .getElencoParticelleForUteAndAzienda(idAzienda);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola,
  // ad un'unità produttiva e per riepilogo titolo possesso/comune
  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaPossAndComune(Long idAzienda)
      throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO
          .getElencoParticelleForUteAndAziendaPossAndComune(idAzienda);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle particelle relative ad un'azienda
  // agricola,
  // ad un'unità produttiva e per riepilogo comune
  public Vector<ParticellaVO> getElencoParticelleForUteAndAziendaComune(Long idAzienda)
      throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO
          .getElencoParticelleForUteAndAziendaComune(idAzienda);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per effettuare la ricerca delle particelle attive in relazione ai
  // parametri di
  // ricerca
  public Vector<ParticellaVO> ricercaParticelleAttiveByParametri(ParticellaVO particellaVO,
      String data, Long idAzienda) throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO.ricercaParticelleAttiveByParametri(
          particellaVO, data, idAzienda);
    }
    /*
     * catch (DataAccessException dae) { throw new
     * Exception(dae.getMessage()); }
     */
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per controllare che non esistano conduzioni attive di particelle
  // associate ad
  // una azienda agricola
  public void checkCessaAziendaByConduzioneParticella(Long idUte)
      throws SolmrException, Exception
  {
    try
    {
      fascicoloDAO.checkCessaAziendaByConduzioneParticella(idUte);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
  }

  // Metodo che mi restituisce un vettore contenente le date delle "fotografie"
  // effettuate
  // su una specifica azienda individuata attraverso l'id azienda
  public Vector<CodeDescription> getListaDateConsistenza(Long idAzienda) throws SolmrException,
      Exception
  {
    Vector<CodeDescription> elencoDate = new Vector<CodeDescription>();
    try
    {
      elencoDate = fascicoloDAO.getListaDateConsistenza(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoDate;
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAzienda(Long idAzienda,
      Long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO.getElencoConsistenzaParticelleForAzienda(
          idAzienda, idDichiarazioneConsistenza);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per verificare se l'ultimo aggiornamento della consistenza sia
  // avvenuto dopo
  // l'ultima data di dichiarazione di consistenza
  public void checkLastAggiornamentoAfterMaxDichConsistenza(Long idAzienda)
      throws SolmrException, Exception
  {

    Date dataUltimaConsistenza = null;
    Date dataMaxDichConsistenza = null;
    try
    {
      dataUltimaConsistenza = fascicoloDAO
          .getDataUltimoAggiornamentoConsistenza(idAzienda);
      dataMaxDichConsistenza = fascicoloDAO
          .getMaxDataDichiarazioneConsistenza(idAzienda);
    }
    catch (SolmrException se)
    {
    }

    if (dataUltimaConsistenza != null && dataMaxDichConsistenza != null)
    {
      if (dataUltimaConsistenza.after(dataMaxDichConsistenza))
      {
        throw new SolmrException((String) AnagErrors
            .get("ERR_CONSISTENZA_VARIATA"));
      }
    }
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndPossessoAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO
          .getElencoConsistenzaParticelleForAziendaAndPossessoAndComune(
              idAzienda, idDichiarazioneConsistenza);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e il codice fotografia terreni
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndComune(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO
          .getElencoConsistenzaParticelleForAziendaAndComune(idAzienda,
              idDichiarazioneConsistenza);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'anno successivo rispetto a quello di sistema nella
  // tabella degli utilizzi
  // inserito come previsione
  public String getAnnoPrevisioneUtilizzi(Long idAzienda)
      throws SolmrException, Exception
  {
    String annoPrevisione = null;
    try
    {
      annoPrevisione = fascicoloDAO.getAnnoPrevisioneUtilizzi(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return annoPrevisione;
  }

  // Metodo per effettuare la ricerca delle particelle storicizzate in relazione
  // ai parametri
  // di ricerca
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametri(
      ParticellaVO particellaVO, Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoParticelleStoricizzate = null;
    try
    {
      elencoParticelleStoricizzate = fascicoloDAO
          .ricercaParticelleStoricizzateByParametri(particellaVO,
              idDichiarazioneConsistenza);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelleStoricizzate;
  }

  // Metodo per verificare se, in presenza di particelle ad utilizzo vigneto,
  // sia possibile effettuare
  // l'eliminazione
  public void checkEliminaUtilizziVigneto(Vector<String> elencoIdUtilizziParticella)
      throws SolmrException, Exception
  {
    int count = 0;
    try
    {
      count = fascicoloDAO
          .countUtilizziVignetoByElencoIdUtilizzoParticella(elencoIdUtilizziParticella);
      if (count > 0)
      {
        String covi = cDAO
            .getValoreFromParametroByIdCode((String) SolmrConstants
                .get("ID_PARAMETRO_COVI"));
        if (covi.equalsIgnoreCase(SolmrConstants.FLAG_N))
        {
          throw new SolmrException((String) AnagErrors
              .get("ERR_ELIMINAZIONE_UTILIZZO_VIGNETO_IMPOSSIBILE"));
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
  }

  public Vector<ParticellaVO> getElencoParticelleForAziendaAndUtilizzo(Long idAzienda,
      String anno) throws SolmrException, Exception
  {
    try
    {
      return fascicoloDAO.getElencoParticelleForAziendaAndUtilizzo(idAzienda,
          anno);
    }
    catch (DataAccessException da)
    {
      throw new Exception(da.getMessage());
    }

  }

  // Metodo per recuperare i dati dell'uso del suolo e dei contratti della
  // particella a partire dall'id storico particella
  public Vector<ParticellaUtilizzoVO> getElencoParticellaUtilizzoVO(Long idConduzioneParticella,
      String anno) throws SolmrException, Exception
  {
    Vector<ParticellaUtilizzoVO> elencoParticelleUtilizzo = null;
    try
    {
      elencoParticelleUtilizzo = fascicoloDAO.getElencoParticellaUtilizzoVO(
          idConduzioneParticella, anno);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoParticelleUtilizzo;
  }

  // Metodo per recuperare il valore di tutte le superfici condotte da
  // un'azienda agricola
  public String getTotaleSupCondotteByAzienda(Long idAzienda, String data)
      throws SolmrException, Exception
  {
    try
    {
      return fascicoloDAO.getTotaleSupCondotteByAzienda(idAzienda, data);
    }
    catch (DataAccessException da)
    {
      throw new Exception(da.getMessage());
    }

  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e la destinazione d'uso
  public Vector<ParticellaVO> getElencoConsistenzaParticelleForAziendaAndDestinazioneUso(
      Long idAzienda, Long idDichiarazioneConsistenza,
      Long idConduzioneParticella) throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO
          .getElencoConsistenzaParticelleForAziendaAndDestinazioneUso(
              idAzienda, idDichiarazioneConsistenza, idConduzioneParticella);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso l'id azienda e l'uso secondario
  public Vector<ParticellaUtilizzoVO> getElencoConsistenzaParticelleForAziendaAndUsoSecondario(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception
  {
    Vector<ParticellaUtilizzoVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO
          .getElencoConsistenzaParticelleForAziendaAndUsoSecondario(idAzienda,
              idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id storico particella
  public ParticellaVO getDettaglioParticellaDatiTerritoriali(
      Long idStoricoParticella) throws SolmrException, Exception
  {
    ParticellaVO particellaVO = null;
    UtenteIrideVO utenteIrideVO = null;
    try
    {
      particellaVO = fascicoloDAO
          .getDettaglioParticellaDatiTerritoriali(idStoricoParticella);
      utenteIrideVO = cDAO.getUtenteIrideById(particellaVO
          .getIdUtenteAggiornamento());
      particellaVO.setDenominazioneUtenteAggiornamento(utenteIrideVO
          .getDenominazione()
          + " - " + utenteIrideVO.getDescrizioneEnteAppartenenza());
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return particellaVO;
  }

  // Metodo per recuperare il valore di tutte le superfici utilizzate da
  // un'azienda agricola
  public double getTotaleSupUtilizzateByIdConduzioneParticella(
      Long idConduzioneParticella, String anno) throws SolmrException,
      Exception
  {
    double totaleSupUtilizzate = 0;
    try
    {
      totaleSupUtilizzate = fascicoloDAO
          .getTotaleSupUtilizzateByIdConduzioneParticella(
              idConduzioneParticella, anno);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return totaleSupUtilizzate;
  }

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id dichiarazione
  // consistenza quindi si tratta di dati storicizzati
  public ParticellaVO getDettaglioParticellaStoricizzataDatiTerritoriali(
      Long idConduzioneDichiarata) throws SolmrException, Exception
  {
    ParticellaVO particellaVO = null;
    try
    {
      particellaVO = fascicoloDAO
          .getDettaglioParticellaStoricizzataDatiTerritoriali(idConduzioneDichiarata);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return particellaVO;
  }

  // Metodo per recuperare i dati della conduzione e dei contratti della
  // particella a partire dall'id conduzione
  // dichiarata e quindi si tratta di dati storicizzati
  public ParticellaVO getDettaglioParticellaStoricizzataConduzione(
      Long idConduzioneDichiarata) throws SolmrException, Exception
  {
    ParticellaVO particellaVO = null;
    try
    {
      particellaVO = fascicoloDAO
          .getDettaglioParticellaStoricizzataConduzione(idConduzioneDichiarata);
    }
    catch (DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return particellaVO;
  }

  // Metodo per recuperare i dati dell'uso del suolo della particella a partire
  // dall'id conduzione dichiarata
  // quindi sto prelevando l'elenco degli utilizzi storicizzati della particella
  public Vector<ParticellaUtilizzoVO> getElencoStoricoParticellaUtilizzoVO(
      Long idConduzioneDichiarata, Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    Vector<ParticellaUtilizzoVO> elencoUtilizziParticella = null;
    try
    {
      elencoUtilizziParticella = fascicoloDAO
          .getElencoStoricoParticellaUtilizzoVO(idConduzioneDichiarata,
              idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoUtilizziParticella;
  }

  // Metodo per recuperare i tipi utilizzo dato un indirizzo tipo utilizzo
  public Vector<CodeDescription> getTipiUtilizzoForIdIndirizzoTipoUtilizzo(
      Long idTipoIndirizzoUtilizzo) throws SolmrException, Exception
  {
    Vector<CodeDescription> elencoTipiUtilizzo = null;
    try
    {
      elencoTipiUtilizzo = fascicoloDAO
          .getTipiUtilizzoForIdIndirizzoTipoUtilizzo(idTipoIndirizzoUtilizzo);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoTipiUtilizzo;
  }

  // Metodo per recuperare l'elenco delle particelle in relazione a dei filtri
  // di ricerca e agli utilizzi
  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzi(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException,
      Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO.ricercaParticelleByParametriAndUtilizzi(
          particellaRicercaVO, idAzienda);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per effettuare la ricerca delle particelle a partire dai parametri
  // relativi alla particella
  // e ad uno speficifico utilizzo
  public Vector<ParticellaVO> ricercaParticelleByParametriAndUtilizzoSpecificato(
      Long idAzienda, ParticellaVO particellaRicercaVO) throws SolmrException,
      Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO
          .ricercaParticelleByParametriAndUtilizzoSpecificato(idAzienda,
              particellaRicercaVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle particelle in relazione a dei filtri
  // di ricerca e all'assenza
  // di utilizzi o al fatto che la somma delle superfici utilizzate relative ad
  // una conduzione non
  // sia uguale alla superficie condotta
  public Vector<ParticellaVO> ricercaParticelleByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO, Long idAzienda) throws SolmrException,
      Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO
          .ricercaParticelleByParametriSenzaUsoSuolo(particellaRicercaVO,
              idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per recuperare l'elenco delle fotografie relative alle particelle
  // attraverso i parametri relativi alla particella e l'id dichiarazione di
  // consistenza
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzo(
      ParticellaVO particellaRicercaVO) throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO
          .ricercaParticelleStoricizzateByParametriAndUtilizzo(particellaRicercaVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per effettuare la ricerca delle particelle storicizzate a partire
  // dai parametri relativi alla
  // particella e ad uno speficifico utilizzo
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato(
      ParticellaVO particellaRicercaVO) throws Exception, SolmrException
  {
    Vector<ParticellaVO> elencoParticelleUtilizzi = null;
    try
    {
      elencoParticelleUtilizzi = fascicoloDAO
          .ricercaParticelleStoricizzateByParametriAndUtilizzoSpecificato(particellaRicercaVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelleUtilizzi;
  }

  // Metodo per recuperare l'elenco delle particelle storicizzate in relazione a
  // dei filtri di ricerca e all'assenza
  // di utilizzi o al fatto che la somma delle superfici utilizzate relative ad
  // una conduzione non
  // sia uguale alla superficie condotta
  public Vector<ParticellaVO> ricercaParticelleStoricizzateByParametriSenzaUsoSuolo(
      ParticellaVO particellaRicercaVO) throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoParticelleUtilizzi = null;
    try
    {
      elencoParticelleUtilizzi = fascicoloDAO
          .ricercaParticelleStoricizzateByParametriSenzaUsoSuolo(particellaRicercaVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoParticelleUtilizzi;
  }

  // Metodo per controllare se è possibile modificare un uso del suolo associato
  // ad un'azienda agricola
  public void checkUpdateSuperficie(Long idAzienda) throws SolmrException,
      Exception
  {
    try
    {
      Date dataMassima = fascicoloDAO.getDataMaxForUpdateUsoDelSuolo();
      if (DateUtils.parseDate(DateUtils.getCurrentDateString()).compareTo(
          dataMassima) > 0)
      {
        String flagVariazioneUtilizziAmmessa = wDAO
            .getFlagVariazioneUtilizziAmmessa(idAzienda);
        if (flagVariazioneUtilizziAmmessa
            .equalsIgnoreCase(SolmrConstants.FLAG_N))
        {
          throw new SolmrException((String) AnagErrors
              .get("ERR_VARIAZIONE_UTILIZZI_NON_AMMESSA"));
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
  }

  // Metodo per recuperare i dati della particella e dell'utilizzo ad essa
  // relativa a partire
  // dall' id utilizzi particella
  public ParticellaVO getParticellaVOByIdUtilizzoParticella(
      Long idUtilizzoParticella) throws SolmrException, Exception
  {
    ParticellaVO particellaVO = null;
    try
    {
      particellaVO = fascicoloDAO
          .getParticellaVOByIdUtilizzoParticella(idUtilizzoParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return particellaVO;
  }

  // Metodo per recuperare i dati della particella a partire dall'
  // id_conduzione_particella
  public ParticellaVO getParticellaVOByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException, Exception
  {
    ParticellaVO particellaVO = null;
    try
    {
      particellaVO = fascicoloDAO
          .getParticellaVOByIdConduzioneParticella(idConduzioneParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return particellaVO;
  }

  // Metodo per recuperare il valore di tutte le superfici utilizzate da
  // un'azienda agricola esclusa
  // quella selezionata
  public double getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo(
      Long idConduzioneParticella, Long idUtilizzoParticella, String anno)
      throws SolmrException, Exception
  {
    double totSupUtilizzate = 0;
    try
    {
      totSupUtilizzate = fascicoloDAO
          .getTotSupUtilizzateByIdCondParticellaNotIncludeIdUtilizzo(
              idConduzioneParticella, idUtilizzoParticella, anno);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return totSupUtilizzate;
  }

  // Metodo per recuperare la data dell'ultimo dichiarazione di consistenza
  public Date getMaxDataDichiarazioneConsistenza(Long idAzienda)
      throws SolmrException, Exception
  {
    Date dataUltimadichiarazioneConsistenza = null;
    try
    {
      dataUltimadichiarazioneConsistenza = fascicoloDAO
          .getMaxDataDichiarazioneConsistenza(idAzienda);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return dataUltimadichiarazioneConsistenza;
  }

  // Metodo per recuperare il valore di tutte le superfici condotte attive
  // partendo dall 'id particella
  // quella della particella su cui si sta lavorando
  public String getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella(
      Long idConduzioneParticella, Long idParticella) throws SolmrException,
      Exception
  {
    String totSupCondotte = null;
    try
    {
      totSupCondotte = fascicoloDAO
          .getTotaleSupCondotteByIdParticellaNotIncludeIdConduzioneParticella(
              idConduzioneParticella, idParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return totSupCondotte;
  }

  // Metodo per recuperare l'elenco delle dichiarazioni di consistenza
  // attraverso l'id azienda
  public Vector<ParticellaVO> getElencoDichiarazioniConsistenzaByIdAzienda(Long idAzienda)
      throws SolmrException
  {
    Vector<ParticellaVO> elencoDichiarazioniConsistenza = null;
    try
    {
      elencoDichiarazioniConsistenza = fascicoloDAO
          .getElencoDichiarazioniConsistenzaByIdAzienda(idAzienda);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    return elencoDichiarazioniConsistenza;
  }

  // Metodo per recuperare il valore di tutte le superfici condotte relative ad
  // una dichiarazione di
  // consistenza di un'azienda agricola
  public String getTotaleSupCondotteDichiarateByAzienda(Long idAzienda,
      Long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    String totSupCondottedichiarate = null;
    try
    {
      totSupCondottedichiarate = fascicoloDAO
          .getTotaleSupCondotteDichiarateByAzienda(idAzienda,
              idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return totSupCondottedichiarate;
  }

  // Metodo per effettuare l'eliminazione massiva degli utilizzi della
  // particella
  public void eliminaUtilizzoParticella(Vector<String> elencoConduzioni,
      Vector<String> elencoIdUtilizzoParticella, long idUtenteAggiornamento)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this,
        "Invocating eliminaUtilizzoParticella in FascicoloBean");
    try
    {
      SolmrLogger.debug(this, "Creating object [PARTICELLAVO] in FascicoloBean");
      ParticellaVO particellaVO = new ParticellaVO();
      SolmrLogger.debug(this,
          "Created object [PARTICELLAVO] in FascicoloBean and it values: "
              + particellaVO);
      SolmrLogger.debug(this,
          "Setting object [PARTICELLAVO] modified in FascicoloBean");
      particellaVO.setRecordModificato(true);
      SolmrLogger.debug(this,
          "Setted object [PARTICELLAVO] modified in FascicoloBean");
      for (int i = 0; i < elencoConduzioni.size(); i++)
      {
        SolmrLogger.debug(this,
            "Cicle for in eliminaUtilizzoParticella in FascicoloBean");
        Long idConduzioneParticella = Long.decode((String) elencoConduzioni
            .elementAt(i));
        SolmrLogger
            .debug(
                this,
                "Value of parameter [ID_CONDUZIONE_PARTICELLA] in eliminaUtilizzoParticella in FascicoloBean: "
                    + idConduzioneParticella);
        Long idUtilizzoParticella = Long
            .decode((String) elencoIdUtilizzoParticella.elementAt(i));
        SolmrLogger
            .debug(
                this,
                "Value of parameter [ID_UTILIZZO_PARTICELLA] in eliminaUtilizzoParticella in FascicoloBean: "
                    + idUtilizzoParticella);
        SolmrLogger.debug(this,
            "Deleting utilizzo in eliminaUtilizzoParticella in FascicoloBean");
        fascicoloDAO.deleteUtilizzoParticella(idUtilizzoParticella);
        SolmrLogger.debug(this,
            "Deleted utilizzo in eliminaUtilizzoParticella in FascicoloBean");
        particellaVO.setIdConduzioneParticella(idConduzioneParticella);
        SolmrLogger
            .debug(
                this,
                "Invocating certificateUpdateParticella in eliminaUtilizzoParticella in FascicoloBean");
        particellaVO.setRecordModificato(true);
        fascicoloDAO.certificateUpdateParticella(particellaVO, idUtenteAggiornamento);
        SolmrLogger
            .debug(
                this,
                "Invocated certificateUpdateParticella in eliminaUtilizzoParticella in FascicoloBean");
      }
    }
    catch (DataAccessException dae)
    {
      SolmrLogger.error(this,
          "Intercepted DataAccessException in eliminaUtilizzoParticella in FascicoloBean"
              + "and converted into Exception with this message: "
              + dae.getMessage());
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
    SolmrLogger.debug(this,
        "Invocated eliminaUtilizzoParticella in FascicoloBean");
  }

  // Metodo per controllare le particelle selezionate sono già state inserite
  // all'interno di una
  // dichiarazione di consistenza
  public void checkParticellaLegataDichiarazioneConsistenza(
      Vector<Long> elencoParticelle) throws Exception, SolmrException
  {
    try
    {
      fascicoloDAO
          .checkParticellaLegataDichiarazioneConsistenza(elencoParticelle);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
  }

  // Metodo per effettuare la cancellazione degli utilizzi dalla tabella
  // DB_UTILIZZO_PARTICELLA
  // partendo dall'id conduzione particella
  public void deleteUtilizzoParticellaByIdConduzioneParticella(
      Long idConduzioneParticella) throws Exception, SolmrException
  {
    try
    {
      fascicoloDAO
          .deleteUtilizzoParticellaByIdConduzioneParticella(idConduzioneParticella);
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per effettuare la cancellazione delle conduzioni dalla tabella
  // DB_CONDUZIONE_PARTICELLA
  public void deleteConduzioneParticella(Long idConduzioneParticella)
      throws SolmrException, Exception
  {
    try
    {
      fascicoloDAO.deleteConduzioneParticella(idConduzioneParticella);
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per effettuare l'eliminazione delle particelle selezionate da un
  // utente a partire
  // dall'id conduzione particella
  public void eliminaParticelle(Vector<Long> elencoConduzioni, Long idAzienda, RuoloUtenza ruoloUtenza) throws SolmrException,
      Exception
  {
    try
    {
      for (int i = 0; i < elencoConduzioni.size(); i++)
      {
        Long idConduzioneEliminabile = (Long) elencoConduzioni.elementAt(i);
        
        //Controllo se esitono unità vitate associate alla conduzione
        Vector<StoricoUnitaArboreaVO> vUnitaArborea = 
          particellaGaaDAO.getUnitaArboreeAttiveForAziendaAndConduzione(
            idConduzioneEliminabile.longValue(), idAzienda.longValue());
        
        //Se ci sono fabbricati associati alla conduzione
        //li storicizza
        ConduzioneParticellaVO condVO = conduzioneParticellaDAO
          .findConduzioneParticellaByPrimaryKey(idConduzioneEliminabile);
        
        if((condVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) !=0)
          && (condVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO) !=0))
        {
          FabbricatoParticellaVO[] elencoFabbricati =  fabbricatoDAO
            .getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(idConduzioneEliminabile, 
                idAzienda, null, true);
          if (elencoFabbricati != null && elencoFabbricati.length > 0)
          {
            for(int j=0;j<elencoFabbricati.length;j++)
            {
              fabbricatoDAO.storicizzaFabbricatoParticellaByPrimaryKey(
                  elencoFabbricati[j].getIdFabbricatoParticella());            
            }
          }
        }
        
        // Elimino i records da DB_ESITO_CONTROLLO_PARTICELLA
        fascicoloDAO
            .deleteEsitoControlloParticellaByIdConduzioneParticella(idConduzioneEliminabile);
        // Elimino DB_UTILIZZO_CONSOCIATE
        utilizzoConsociatoDAO
            .deleteUtilizzoConsociatoByIdConduzioneParticella(idConduzioneEliminabile);
        // Elimino i records da DB_UTILIZZO_PARTICELLA
        fascicoloDAO
            .deleteUtilizzoParticellaByIdConduzioneParticella(idConduzioneEliminabile);
        
        //Mi devo ricavare i documenti prima di cessare le conduzioni del documento stesso
        //Istanza riesame
        // Recupero i documenti attivi legati alla conduzione modificata
        Vector<DocumentoVO> elencoDocumenti = documentoDAO
            .getListDocumentiByIdConduzioneParticella(
                idConduzioneEliminabile, true);
        // Elimino i records dalla tabella DB_DOCUMENTO_CONDUZIONE
        documentoDAO
            .deleteConduzioniDocumentoByIdConduzioneParticella(idConduzioneEliminabile);
        // Elimino i records dalla tabella DB_CONDUZIONE_PARTICELLA
        fascicoloDAO.deleteConduzioneParticella(idConduzioneEliminabile);
        
       
        // Se ce ne sono ...
        if (elencoDocumenti != null && elencoDocumenti.size() > 0)
        {
          for (int a = 0; a < elencoDocumenti.size(); a++)
          {
            DocumentoVO documentoVO = (DocumentoVO) elencoDocumenti
                .elementAt(a);  
                        
            if(documentoDAO.isDocIstanzaRiesame(documentoVO.getExtIdDocumento().longValue()))
            {
                       
              
              if(!documentoGaaDAO.existAltroLegameIstRiesameParticella(
                  idAzienda, condVO.getIdParticella().longValue(), 
                 DateUtils.getCurrentYear().intValue(), 
                 documentoVO.getExtIdDocumento().longValue()))
              {                 
                
                Long idIstanaRiesame = documentoGaaDAO.getIstRiesameParticellaFaseAnno(
                    idAzienda, condVO.getIdParticella().longValue(), 
                    DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame());
                if(Validator.isNotEmpty(idIstanaRiesame))
                {
                  Vector<Long> vIdIstanzaRiesame = new Vector<Long>();
                  vIdIstanzaRiesame.add(idIstanaRiesame);
                  documentoGaaDAO.annullaIstanzaFromId(vIdIstanzaRiesame, ruoloUtenza.getIdUtente());
                  PlSqlCodeDescription plCode = particellaGaaDAO.annullaIstanzaPlSql(documentoVO.getIdAzienda().longValue(), 
                      DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), documentoVO.getFaseIstanzaRiesame(), 
                      ruoloUtenza.getIdUtente().longValue());
                  
                  if(plCode !=null)
                  {
                    if(Validator.isNotEmpty(plCode.getDescription()))
                    {
                      throw new DataAccessException("Aggiorna documento errore al plsql annullaIstanzaRiesame: "+plCode.getDescription()+"-"
                          +plCode.getOtherdescription());       
                    }
                  }                  
                }               
              }
              
              
              //cessazione del documento
              Vector<DocumentoConduzioneVO> vDocumentoConduzione = documentoDAO
                  .getListDocumentoConduzioni(documentoVO.getIdDocumento(), false);
              Vector<Long> vIdParticella = null;
              boolean trovataUnaValida = false;
              for(int b=0;b<vDocumentoConduzione.size();b++)
              {
                if(Validator.isEmpty(vDocumentoConduzione.get(b).getDataFineValidita()))
                {
                  if(!trovataUnaValida)
                    trovataUnaValida = true;                 
                }
                else
                {
                  if(vIdParticella == null)
                  {
                    vIdParticella = new Vector<Long>();
                  }
                  if(!vIdParticella.contains(vDocumentoConduzione.get(b).getIdParticella()))
                  {
                    vIdParticella.add(vDocumentoConduzione.get(b).getIdParticella());
                  }
                }                
              }
              //non ho trovato conduzioni attive sul documento
              if(!trovataUnaValida)
              {
                Long idStatoDocumento = null;
                if(documentoGaaDAO.isAllIstanzaAnnullata(idAzienda, vIdParticella, 
                    DateUtils.getCurrentYear().intValue(), documentoVO.getFaseIstanzaRiesame()))
                {
                  idStatoDocumento = new Long(1);
                }               
                documentoDAO.updateDocumentoIstanza(documentoVO.getIdDocumento(), idStatoDocumento);
                
              }
              
            }        
            
          }
        }
        
        
        
        
        
        ///Se esitono unita vitate associate alla conduzione....
        if(vUnitaArborea != null)
        {
          //Se le unita arboree non sono associate ad altre coduzioni
          //le storicizzo
          if(!particellaGaaDAO.esistonoAltreConduzioni(
              vUnitaArborea.get(0).getIdParticella().longValue(), idAzienda.longValue()))
          {
            
            long[] vIdStoricoUnitaArboreaLg = new long[vUnitaArborea.size()]; 
            for(int j=0;j<vUnitaArborea.size();j++)
            {
              vIdStoricoUnitaArboreaLg[j] = vUnitaArborea.get(j).getIdStoricoUnitaArborea().longValue(); 
            }
            
            StoricoUnitaArboreaVO[] elencoUnitaArboree = storicoUnitaArboreaDAO
                .getListStoricoUnitaArboreaByVidSoricoUnitaArborea(vIdStoricoUnitaArboreaLg);
            // Se ce ne sono
            if (elencoUnitaArboree != null && elencoUnitaArboree.length > 0)
            {
              // Le storicizzo
              for (int a = 0; a < elencoUnitaArboree.length; a++)
              {
                StoricoUnitaArboreaVO storicoUnitaArboreaVO = (StoricoUnitaArboreaVO) elencoUnitaArboree[a];
                // Imposto data fine validita
                //storicoUnitaArboreaVO.setDataFineValidita(new java.util.Date(
                    //new Timestamp(System.currentTimeMillis()).getTime()));
                //storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                    //new Timestamp(System.currentTimeMillis()).getTime()));
                storicoUnitaArboreaDAO.storicizzaStoricoUnitaArborea(storicoUnitaArboreaVO.getIdStoricoUnitaArborea());
                    //.updateStoricoUnitaArborea(storicoUnitaArboreaVO);
                // Inserisco nuovo record su DB_STORICO_UNITA_ARBOREA
                storicoUnitaArboreaVO.setDataFineValidita(null);
                storicoUnitaArboreaVO.setIdStoricoUnitaArborea(null);
                storicoUnitaArboreaVO.setDataInizioValidita(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                storicoUnitaArboreaVO.setDataAggiornamento(new java.util.Date(
                    new Timestamp(System.currentTimeMillis()).getTime()));
                storicoUnitaArboreaVO.setIdUtenteAggiornamento(ruoloUtenza
                    .getIdUtente());
                storicoUnitaArboreaVO.setRecordModificato(null);
                storicoUnitaArboreaVO.setDataEsecuzione(null);
                storicoUnitaArboreaVO.setEsitoControllo(null);
                storicoUnitaArboreaVO.setIdAzienda(null);
                storicoUnitaArboreaVO.setIdGenereIscrizione(null);
                
                storicoUnitaArboreaDAO
                    .insertStoricoUnitaArborea(storicoUnitaArboreaVO);
              }
            }
            
            
          }
        }
        
        
        
      }
      
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per effettuare la modifica di un record su DB_FABBRICATO
  public void cessaFabbricato(Long idFabbricato, long idUtenteAggiornamento)
      throws SolmrException, Exception
  {
    try
    {
      fascicoloDAO.storicizzaFabbricatoParticella(idFabbricato);
      fascicoloDAO.storicizzaFabbricato(idFabbricato, idUtenteAggiornamento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per effettuare la cessazione dell'unità produttiva selezionata
  public void cessazioneUTE(UteVO uteVO, long idUtenteAggiornamento)
      throws SolmrException, Exception
  {
    try
    {
      fascicoloDAO.mayICessazioneUTE(uteVO.getIdUte());
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }

    try
    {
      fascicoloDAO.cessazioneUTE(uteVO, idUtenteAggiornamento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per recuperare i dati della particella a partire dall'
  // id_conduzione_particella e la
  // superficie libera cioè senza uso del suolo specificato
  public ParticellaVO getParticellaVOByIdConduzioneParticellaSenzaUsoSuolo(
      Long idConduzioneParticella, String anno) throws SolmrException,
      Exception
  {
    ParticellaVO particellaVO = null;
    try
    {
      particellaVO = fascicoloDAO
          .getParticellaVOByIdConduzioneParticellaSenzaUsoSuolo(
              idConduzioneParticella, anno);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return particellaVO;
  }

  // Metodo per recuperare i dati territoriali della particella a partire
  // dall'id conduzione particella
  public ParticellaVO getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella(
      Long idConduzioneParticella) throws SolmrException, Exception
  {
    ParticellaVO particellaVO = null;
    try
    {
      particellaVO = fascicoloDAO
          .getDettaglioParticellaDatiTerritorialiByIdConduzioneParticella(idConduzioneParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return particellaVO;
  }

  // Metodo per ribaltare gli utilizzi da una conduzione particella ad un'altra
  public void allegaUtilizziToNewConduzioneParticella(
      Long newIdConduzioneParticella, Long oldIdConduzioneParticella)
      throws SolmrException, Exception
  {
    try
    {
      fascicoloDAO.allegaUtilizziToNewConduzioneParticella(
          newIdConduzioneParticella, oldIdConduzioneParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per effettuare la ricerca del terreno in relazione ai parametri di
  // ricerca
  public Vector<ParticellaVO> ricercaTerreniByParametri(
      ParticellaVO particellaRicercaTerrenoVO) throws SolmrException,
      Exception
  {
    Vector<ParticellaVO> elencoTerreni = null;
    try
    {
      elencoTerreni = fascicoloDAO
          .ricercaTerreniByParametri(particellaRicercaTerrenoVO);
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoTerreni;
  }

  // Metodo verificare se esiste un contenzioso sulla particella selezionata,
  // cioè se sono presenti
  // delle conduzioni aperte a fronte di particelle selezionate su aziende
  // attive
  public boolean isParticellaContenziosoOnAzienda(Long idStoricoParticella)
      throws SolmrException, Exception
  {
    boolean isContenzioso = false;
    try
    {
      isContenzioso = fascicoloDAO
          .isParticellaContenziosoOnAzienda(idStoricoParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return isContenzioso;
  }

  // Metodo per recuperare la somma delle superfici condotte relativa ad una
  // particella
  public String getTotSupCondotteByIdStoricoParticella(Long idStoricoParticella)
      throws SolmrException, Exception
  {
    String totSupCondotte = null;
    try
    {
      totSupCondotte = fascicoloDAO
          .getTotSupCondotteByIdStoricoParticella(idStoricoParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return totSupCondotte;
  }

  // Metodo per recuperare l'elenco delle azienda e delle conduzioni a partire
  // dall 'id storico
  // particella
  public Vector<ParticellaAziendaVO> getElencoAziendeAndConduzioniByIdStoricoParticella(
      Long idStoricoParticella, boolean attive) throws Exception,
      SolmrException
  {
    Vector<ParticellaAziendaVO> elencoConduzioniAziende = null;
    try
    {
      elencoConduzioniAziende = fascicoloDAO
          .getElencoAziendeAndConduzioniByIdStoricoParticella(
              idStoricoParticella, attive);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoConduzioniAziende;
  }

  // Metodo per recuperare l'elenco degli utilizzi relativi a conduzioni attive
  // in un anno
  // specificato
  public Vector<ParticellaUtilizzoVO> getElencoUtilizziAttiviByAnnoAndIdStoricoParticella(
      Long idStoricoParticella, String anno) throws Exception,
      SolmrException
  {
    Vector<ParticellaUtilizzoVO> elencoUtilizziAttivi = null;
    try
    {
      elencoUtilizziAttivi = fascicoloDAO
          .getElencoUtilizziAttiviByAnnoAndIdStoricoParticella(
              idStoricoParticella, anno);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoUtilizziAttivi;
  }

  public int countParticelleConConduzioniAttive(long idParticella[]) throws
  SolmrException, Exception {
    int countParticelleConConduzioniAttive = 0;
    try {
      countParticelleConConduzioniAttive = fascicoloDAO.countParticelleConConduzioniAttive(
          idParticella);
    }
    catch (DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
    return countParticelleConConduzioniAttive;
  }

  // Metodo per recuperare l'elenco delle variazioni storiche di una determinata
  // particella a
  // partire dall'id storico particella
  public Vector<ParticellaVO> getElencoStoricoParticella(Long idParticella)
      throws SolmrException, Exception
  {
    Vector<ParticellaVO> elencoStoricoParticella = null;
    try
    {
      elencoStoricoParticella = fascicoloDAO
          .getElencoStoricoParticella(idParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoStoricoParticella;
  }

  /**
   * Metodo per recuperare il record dalla tabella DB_STORICO_PARTICELLA a
   * partire dalla chiave primaria
   * 
   * @param idStoricoParticella
   *          Long
   * @return ParticellaVO
   * @throws Exception
   */
  public ParticellaVO getStoricoParticella(Long idStoricoParticella)
      throws Exception
  {
    try
    {
      return fascicoloDAO.getStoricoParticella(idStoricoParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per recuperare l'elenco dei terreni su cui l'azienda ha o ha avuto
  // dei terreni
  public Vector<StringcodeDescription> getListaComuniTerreniByIdAzienda(Long idAzienda)
      throws Exception, SolmrException
  {
    Vector<StringcodeDescription> elencoComuniTerreni = null;
    try
    {
      elencoComuniTerreni = fascicoloDAO
          .getListaComuniTerreniByIdAzienda(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception();
    }
    return elencoComuniTerreni;
  }

  // Metodo per recuperare il totale delle superfici condotte attive associate
  // alla particella
  public String getTotaleSupCondotteAttiveByIdParticella(Long idParticella,
      Long idAzienda) throws Exception, SolmrException
  {
    String totSupCondotte = null;
    try
    {
      totSupCondotte = fascicoloDAO.getTotaleSupCondotteAttiveByIdParticella(
          idParticella, idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return totSupCondotte;
  }

  // Metodo per recuperare l'elenco delle particelle in relazione ad un anno
  // specificato
  // e agli usi secondari
  public Vector<ParticellaUtilizzoVO> getElencoParticelleForAziendaAndUsoSecondario(Long idAzienda,
      String anno) throws SolmrException, Exception
  {
    Vector<ParticellaUtilizzoVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO
          .getElencoParticelleForAziendaAndUsoSecondario(idAzienda, anno);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoParticelle;
  }

  // Metodo per recuperare le anomalie relative ad una particella
  public Vector<EsitoControlloParticellaVO> getElencoEsitoControlloParticella(Long idConduzioneParticella)
      throws Exception, SolmrException
  {
    Vector<EsitoControlloParticellaVO> elencoEsitoControlloParticella = null;
    try
    {
      elencoEsitoControlloParticella = fascicoloDAO
          .getElencoEsitoControlloParticella(idConduzioneParticella);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoEsitoControlloParticella;
  }

  // Metodo che restituisce la data di esecuzione controlli di una determinata
  // azienda agricola
  // in formato dd/mm/yyyy hh/mm/ss
  public String getDataUltimaEsecuzioneControlli(Long idAzienda)
      throws Exception
  {
    try
    {
      return fascicoloDAO.getDataUltimaEsecuzioneControlli(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo recuperare i dati della particella che abbiamo ricevuto dal SIAN in
  // relazione
  // alla particella selezionata dopo la ricerca particellare/piano colturale
  public ParticellaCertificataVO findParticellaCertificataByParametri(
      ParticellaVO particellaVO) throws Exception
  {
    ParticellaCertificataVO particellaCertificataVO = null;
    try
    {
      particellaCertificataVO = particellaCertificataDAO
          .findParticellaCertificataByParametri(particellaVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return particellaCertificataVO;
  }

  /**
   * Metodo per recuperare l'elenco delle particelle in relazione ad un'azienda
   * e al suo stato
   * 
   */
  public Vector<ParticellaVO> getElencoParticelleForImportByAzienda(
      AnagAziendaVO searchAnagAziendaVO, AnagAziendaVO anagAziendaVO,
      RuoloUtenza ruoloUtenza) throws Exception, SolmrException
  {
    SolmrLogger
        .debug(this,
            "Invocating method getElencoParticelleForImportByAzienda in FascicoloBean");
    Vector<ParticellaVO> elencoParticelle = null;
    try
    {
      elencoParticelle = fascicoloDAO.getElencoParticelleForImportByAzienda(
          searchAnagAziendaVO, anagAziendaVO, ruoloUtenza);
    }
    catch (DataAccessException dae)
    {
      SolmrLogger
          .error(
              this,
              "Intercepted DataAccessException in getElencoParticelleForImportByAzienda in FascicoloBean"
                  + "and converted into Exception with this message: "
                  + dae.getMessage());
    }
    SolmrLogger
        .debug(this,
            "Invocated method getElencoParticelleForImportByAzienda in FascicoloBean");
    return elencoParticelle;
  }

  /**
   * Metodo per recuperare l'elenco delle ute relative ad un comune
   * 
   * @param istatComune
   *          String
   * @param idAzienda
   *          Long
   * @param isActive
   *          boolean
   * @return Vector
   * @throws Exception
   * @throws SolmrException
   */
  public Vector<Long> getListIdUteByIstatComuneAndIdAzienda(String istatComune,
      Long idAzienda, boolean isActive) throws Exception, SolmrException
  {
    try
    {
      return fascicoloDAO.getListIdUteByIstatComuneAndIdAzienda(istatComune,
          idAzienda, isActive);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per recuperare gli indirizzi deli utilizzi
  public Vector<CodeDescription> getIndirizziTipiUtilizzoAttivi() throws SolmrException,
      Exception
  {
    Vector<CodeDescription> elencoIndirizziTipiUtilizzo = null;
    try
    {
      elencoIndirizziTipiUtilizzo = fascicoloDAO
          .getIndirizziTipiUtilizzoAttivi();
    }
    catch (DataAccessException se)
    {
      throw new Exception(se.getMessage());
    }
    return elencoIndirizziTipiUtilizzo;

  }

  /**
   * Metodo che si occupa di verificare la presenza del foglio
   * 
   * @param java.util.Array[]
   *          elencoElementi
   * @param java.util.TreeMap
   *          elencoSianTerritorioVO
   * @throws Exception
   */
  public SianTerritorioVO[] verificaCensimentoFoglio(
      SianTerritorioVO[] elencoSian) throws Exception
  {
    // Scorro l'array
    for (int i = 0; i < elencoSian.length; i++)
    {
      // Verifico la presenza su DB_FOGLIO
      try
      {
        if (elencoSian[i].isPiemonte())
          fascicoloDAO.ricercaFoglio(elencoSian[i].getComune(), elencoSian[i]
              .getSezione(), Long.decode(elencoSian[i].getFoglio()));
      }
      catch (SolmrException solmrSex)
      {
        // Se si verifica un errore diverso dal non reperimento di dati su
        // DB_FOGLIO
        // interrompo le operazioni
        if (!solmrSex.getMessage().equalsIgnoreCase(
            (String) AnagErrors.get("ERR_FOGLIO_INESISTENTE")))
        {
          SolmrLogger
              .error(
                  this,
                  "Catching SolmrException in method verificaCensimentoFoglio in FascicoloBean and throwing new Exception with this message: "
                      + solmrSex.getMessage() + "\n");
          throw new Exception(solmrSex.getMessage());
        }
        else
        {
          SolmrLogger.debug(this,
              "\n\n\n\n\n  Foglio non censito   \n\n\n\n\n\n\n");
          elencoSian[i].setFoglioNonCensito(true);
        }
      }
    }
    return elencoSian;
  }

}
