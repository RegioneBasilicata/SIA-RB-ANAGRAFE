package it.csi.solmr.business.anag;

import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.terreni.FaseRiesameDocumentoVO;
import it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameAziendaVO;
import it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameVO;
import it.csi.smranag.smrgaa.integration.DocumentoGaaDAO;
import it.csi.smranag.smrgaa.integration.terreni.ParticellaGaaDAO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.DocumentoProprietarioVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.EsitoControlloDocumentoVO;
import it.csi.solmr.dto.anag.ParticellaAssVO;
import it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.anag.TipoStatoDocumentoVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.anag.ConsistenzaDAO;
import it.csi.solmr.integration.anag.DocumentoDAO;
import it.csi.solmr.integration.anag.EsitoControlloDocumentoDAO;
import it.csi.solmr.integration.anag.EventoParticellaDAO;
import it.csi.solmr.integration.anag.StoricoParticellaDAO;
import it.csi.solmr.integration.anag.TipoCategoriaDocumentoDAO;
import it.csi.solmr.integration.anag.TipoDocumentoDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Bean per la gestione e l'esposizione dei metodi relativi al documentale
 * 
 * <p>
 * Title: SMRGAA
 * </p>
 * 
 * <p>
 * Description: Anagrafe delle Imprese Agricole e Agro-Alimentari
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: CSI - PIEMONTE
 * </p>
 * 
 * @author Mauro Vocale
 * @version 1.0
 */
@Stateless(name="comp/env/solmr/anag/Documento",mappedName="comp/env/solmr/anag/Documento")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class DocumentoBean implements DocumentoLocal
{

  /**
   * 
   */
  private static final long serialVersionUID = 1643358795841304575L;
  SessionContext sessionContext;
  private transient DocumentoDAO documentoDAO = null;
  private transient StoricoParticellaDAO storicoParticellaDAO = null;
  private transient TipoCategoriaDocumentoDAO tipoCategoriaDocumentoDAO = null;
  private transient TipoDocumentoDAO tipoDocumentoDAO = null;
  private transient EsitoControlloDocumentoDAO esitoControlloDocumentoDAO = null;
  private transient ConsistenzaDAO consistenzaDAO = null;
  private transient EventoParticellaDAO eventoParticellaDAO = null;
  private transient DocumentoGaaDAO documentoGaaDAO = null;
  private transient ParticellaGaaDAO particellaGaaDAO = null;

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
      documentoDAO = new DocumentoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      storicoParticellaDAO = new StoricoParticellaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoCategoriaDocumentoDAO = new TipoCategoriaDocumentoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoDocumentoDAO = new TipoDocumentoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      esitoControlloDocumentoDAO = new EsitoControlloDocumentoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      consistenzaDAO = new ConsistenzaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      eventoParticellaDAO = new EventoParticellaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      documentoGaaDAO = new DocumentoGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      particellaGaaDAO = new ParticellaGaaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.error(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

  /**
   * Metodo per recuperare tutti i dati presenti nella tabella
   * DB_TIPO_STATO_DOCUMENTO
   * 
   * @param isActive
   *          boolean
   * @return Vector
   * @throws Exception
   * @throws SolmrException
   */
  public Vector<TipoStatoDocumentoVO> getListTipoStatoDocumento(boolean isActive)
      throws Exception, SolmrException
  {
    try
    {
      return documentoDAO.getListTipoStatoDocumento(isActive);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per ricercare i documenti in relazione ai filtri di ricerca
   * 
   * @param documentoVO
   *          DocumentoVO
   * @param protocollazione
   *          String
   * @param orderBy
   * @return Vector
   * @throws Exception
   */
  public Vector<DocumentoVO> searchDocumentiByParameters(
      DocumentoVO documentoVO, String protocollazione, String[] orderBy)
      throws Exception
  {
    try
    {
      Vector<DocumentoVO> vDocumenti =  documentoDAO.searchDocumentiByParameters(documentoVO,
          protocollazione, orderBy);
      //Aggiungo gli allegati!!!
      for(int i=0;i<vDocumenti.size();i++)
      {
        vDocumenti.get(i).setvAllegatoDocumento(
          documentoGaaDAO.getElencoFileAllegati(vDocumenti.get(i).getIdDocumento().longValue()));
      }
      
      return vDocumenti;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di estrarre l'elenco dei tipi documento in funzione
   * della tipologia documento indicata: recupera solo i records attivi se
   * isActive == true
   * 
   * @param idTipologiaDocumento
   *          Long
   * @param isActive
   *          boolean
   * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
   * @throws Exception
   * @throws SolmrException
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdTipologiaDocumento(
      Long idTipologiaDocumento, boolean isActive) throws Exception,
      SolmrException
  {
    try
    {
      return tipoDocumentoDAO.getListTipoDocumentoByIdTipologiaDocumento(
          idTipologiaDocumento, isActive);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare il record tipo documento a partire dalla chiave
   * primaria
   * 
   * @param idDocumento
   *          Long
   * @return TipoDocumentoVO
   * @throws Exception
   */
  public TipoDocumentoVO findTipoDocumentoVOByPrimaryKey(Long idDocumento)
      throws Exception
  {
    try
    {
      return tipoDocumentoDAO.findTipoDocumentoVOByPrimaryKey(idDocumento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di verificare se esiste già un record su DB_DOCUMENTO
   * con i dati chiave dell'area anagrafica del documento
   * 
   * @param documentoFiltroVO
   *          DocumentoVO
   * @return DocumentoVO
   * @throws Exception
   */
  public DocumentoVO findDocumentoVOBydDatiAnagrafici(
      DocumentoVO documentoFiltroVO) throws Exception
  {
    try
    {
      return documentoDAO.findDocumentoVOBydDatiAnagrafici(documentoFiltroVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per calcolare il numero protocollo
   * 
   */
  public String mathNumeroProtocollo(RuoloUtenza ruoloUtenza, String anno)
      throws Exception
  {
    String numeroProtocollo = null;
    try
    {
      // Recupero il numero protocollo
      numeroProtocollo = documentoDAO.getNumeroProtocollo(ruoloUtenza, anno);
      // Se non lo trovo lo inserisco
      if (numeroProtocollo == null)
      {
        documentoDAO.insertNumeroProtocollo(ruoloUtenza, anno);
        numeroProtocollo = documentoDAO.getNumeroProtocollo(ruoloUtenza, anno);
      }
      // Alla fine delle operazioni aggiorno comunque il record e libero la
      // tabella
      documentoDAO.aggiornaProgressivoNumeroProtocollo(ruoloUtenza, anno);
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
        numeroProtocollo = documentoDAO.getNumeroProtocollo(ruoloUtenza, anno);
        // Alla fine delle operazioni aggiorno comunque il record e libero la
        // tabella
        documentoDAO.aggiornaProgressivoNumeroProtocollo(ruoloUtenza, anno);
      }
      catch (DataAccessException dae)
      {
        sessionContext.setRollbackOnly();
        throw new Exception(dae.getMessage());
      }
    }
    return numeroProtocollo;
  }

  /**
   * Metodo che si occupa di inserire il documento in tutte le sue parti
   * 
   * @param documentoVO
   *          DocumentoVO
   * @param ruoloUtenza
   *          RuoloUtenza
   * @param anno
   *          String
   * @param elencoProprietari
   *          Vector
   * @param elencoParticelle
   * @return Long
   * @throws Exception
   */
  public Long inserisciDocumento(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String anno,
      Vector<DocumentoProprietarioVO> elencoProprietari,
      StoricoParticellaVO[] elencoParticelle,
      Vector<ParticellaAssVO> particelleAssociate) throws Exception
  {
    Long idDocumento = null;
    try
    {
      // Se il flag protocolla era disabilitato o non valorizzato calcolo
      // automaticamente il valore del numero protocollo
      if ((Validator.isNotEmpty(documentoVO.getTipoDocumentoVO()
          .getFlagObbligoProtocollo()) && documentoVO.getTipoDocumentoVO()
          .getFlagObbligoProtocollo().equalsIgnoreCase(SolmrConstants.FLAG_S))
          || Validator.isNotEmpty(documentoVO.getProtocolla()))
      {
        String numeroProtocollo = mathNumeroProtocollo(ruoloUtenza, anno);
        documentoVO.setNumeroProtocollo(numeroProtocollo);
        documentoVO.setDataProtocollo(new Date(System.currentTimeMillis()));
      }
      idDocumento = documentoDAO.insertDocumento(documentoVO, ruoloUtenza);
      // La gestione dei proprietari e delle particelle legate al documento
      // è vincolato al tipo documento selezionato: importante soprattutto
      // per i servizi dal momento che smrgaa gestisce tutto ciò lato client
      if (documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(
          SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
          || documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
              .equalsIgnoreCase(
                  SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))
      {
        // Se sono stati indicati i proprietari...
        if (elencoProprietari != null && elencoProprietari.size() > 0)
        {
          // Li inserisco sul DB
          for (int i = 0; i < elencoProprietari.size(); i++)
          {
            DocumentoProprietarioVO documentoProprietarioVO = (DocumentoProprietarioVO) elencoProprietari
                .elementAt(i);
            documentoProprietarioVO.setIdDocumento(idDocumento);
            documentoDAO.insertDocumentoProprietario(documentoProprietarioVO,
                ruoloUtenza);
          }
        }
        // Se sono state inserite delle particelle
        if (elencoParticelle != null && elencoParticelle.length > 0)
        {
          // Le inserisco sul DB
          HashMap<Long, StoricoParticellaVO> hParticella = null;
          for (int i = 0; i < elencoParticelle.length; i++)
          {
            //mi serv per inserire istanza di riesame
            if(hParticella == null)
            {
              hParticella = new HashMap<Long, StoricoParticellaVO>();
            }
            
            StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle[i];
            
            if(hParticella.get(storicoParticellaVO.getIdParticella()) == null)
            {
              hParticella.put(storicoParticellaVO.getIdParticella(), storicoParticellaVO);
            }
            
            DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
            
            if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
            {
              documentoConduzioneVO.setNote(storicoParticellaVO.getElencoConduzioni()[0]
                .getElencoDocumentoConduzione()[0].getNote());
              documentoConduzioneVO.setLavorazionePrioritaria(storicoParticellaVO.getElencoConduzioni()[0]
                .getElencoDocumentoConduzione()[0].getLavorazionePrioritaria());
            }          
            
            documentoConduzioneVO.setIdConduzioneParticella(storicoParticellaVO
                .getElencoConduzioni()[0].getIdConduzioneParticella());
            documentoConduzioneVO.setIdDocumento(idDocumento);
            documentoConduzioneVO.setDataInserimento(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            documentoConduzioneVO.setDataInizioValidita(new java.util.Date(
                new Timestamp(System.currentTimeMillis()).getTime()));
            if (documentoVO.getDataFineValidita() != null)
            {
              documentoConduzioneVO.setDataFineValidita(new Timestamp(
                  documentoVO.getDataFineValidita().getTime()));
            }
            documentoDAO.insertDocumentoConduzione(documentoConduzioneVO);  
            
          }
          
          
          //Inserisco su db_istanza_riesame 
          if((documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO)
            || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
            || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_SOPRA))
          {
            if(Validator.isNotEmpty(hParticella))
            {
              Collection<StoricoParticellaVO> collection = hParticella.values();
              Iterator<StoricoParticellaVO> iterator = collection.iterator();
              
              
              String protocolloIstanza = documentoGaaDAO.getProtocolloIstanza(documentoVO.getIdAzienda().longValue(), 
                  DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
              if(Validator.isEmpty(protocolloIstanza))
              {
                long idIstanza = documentoGaaDAO.getNewIdMaxProtocolloIstanza(DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
                protocolloIstanza = "IST-"+DateUtils.extractYearFromDate(documentoVO.getDataInserimento())
                    +"-"+idIstanza;
                
                
                IstanzaRiesameAziendaVO istanzaRiesameAziendaVO = new IstanzaRiesameAziendaVO();
                istanzaRiesameAziendaVO.setIdAzienda(documentoVO.getIdAzienda());
                istanzaRiesameAziendaVO.setAnnoIstanza(DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
                istanzaRiesameAziendaVO.setProtocolloIstanza(protocolloIstanza);
                
                documentoGaaDAO.insertIstanzaRiesameAzienda(istanzaRiesameAziendaVO);
              }
              
              
              
              
              while (iterator.hasNext())
              {
                StoricoParticellaVO storicoParticellaVO = iterator.next();
                IstanzaRiesameVO istanzaRiesameVO = new IstanzaRiesameVO();
                
                if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo()))
                {
                  istanzaRiesameVO.setIdStatoSitiConvoca(new Long(0));
                }
                
                istanzaRiesameVO.setIdFaseIstanzaRiesame(new Long(documentoVO.getFaseIstanzaRiesame()).longValue());
                istanzaRiesameVO.setAnno(new Integer(anno).intValue());
                istanzaRiesameVO.setIdAzienda(documentoVO.getIdAzienda().longValue());
                istanzaRiesameVO.setIdParticella(storicoParticellaVO.getIdParticella().longValue());
                istanzaRiesameVO.setDataRichiesta(new Date());
                istanzaRiesameVO.setDataAggiornamento(new Date());
                istanzaRiesameVO.setLavorazionePrioritaria(storicoParticellaVO.getElencoConduzioni()[0]
                  .getElencoDocumentoConduzione()[0].getLavorazionePrioritaria());
                istanzaRiesameVO.setNote(storicoParticellaVO.getElencoConduzioni()[0]
                  .getElencoDocumentoConduzione()[0].getNote());
                istanzaRiesameVO.setProtocolloIstanza(protocolloIstanza);
                
                Long idIstanzaRiesame = documentoGaaDAO.insertIstanzaRiesame(istanzaRiesameVO, ruoloUtenza.getIdUtente());
                
                FaseRiesameDocumentoVO faseRiesameDocumentoVO = documentoGaaDAO
                    .getFaseRiesameDocumentoByIdDocumento(documentoVO.getExtIdDocumento().longValue());
                if(!"N".equalsIgnoreCase(faseRiesameDocumentoVO.getExtraSistema()))
                {
                  documentoGaaDAO.updateIstanzaPotenziale(documentoVO.getIdAzienda().longValue(), 
                      storicoParticellaVO.getIdParticella().longValue(), 
                      DateUtils.extractYearFromDate(documentoVO.getDataInserimento()) , idIstanzaRiesame, 
                      ruoloUtenza.getIdUtente());
                }
                
                
                //aggiorno la fase precedente in caso di protocollo diverso
                if(documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
                {
                  IstanzaRiesameVO istanzaRiesameEvasaVO = documentoGaaDAO.getIstanzaRiesameFaseEvasa(documentoVO.getIdAzienda().longValue(), 
                      storicoParticellaVO.getIdParticella().longValue(), SolmrConstants.FASE_IST_RIESAM_FOTO, DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
                  if(Validator.isNotEmpty(istanzaRiesameEvasaVO))
                  {
                    //se il protocollo è diverso devo aggiornare il protocollo della fase prec
                    if(!istanzaRiesameEvasaVO.getProtocolloIstanza().equalsIgnoreCase(protocolloIstanza))
                    {
                      documentoGaaDAO.updateIstanzaProtocolloFasePrec(
                          istanzaRiesameEvasaVO.getIdIstanzaRiesame(), protocolloIstanza);
                    }                    
                    
                  }
                  
                
                
                }
                
              }
            }
          }
          
          
          
        }
      }
      // Devo inserire le particelle associate
      if (documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(
          SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))
      {
        if (particelleAssociate != null)
        {
          int size = particelleAssociate.size();
          for (int i = 0; i < size; i++)
            eventoParticellaDAO.insertDocCorParticella(
                (ParticellaAssVO) particelleAssociate.get(i), idDocumento);
        }
      }

    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
    return idDocumento;
  }

  /**
   * Metodo per recuperare il documento a partire dalla chiave primaria
   * 
   * @param idDocumento
   *          Long
   * @return DocumentoVO
   * @throws Exception
   */
  public DocumentoVO findDocumentoVOByPrimaryKey(Long idDocumento)
      throws Exception
  {
    try
    {
      return documentoDAO.findDocumentoVOByPrimaryKey(idDocumento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di reperire tutti i dati del dettaglio del documento
   * 
   * @param idDocumento
   *          Long
   * @param legamiAttivi
   * @return DocumentoVO
   * @throws Exception
   */
  public DocumentoVO getDettaglioDocumento(Long idDocumento, boolean legamiAttivi) throws Exception, SolmrException
  {
    DocumentoVO documentoVO = null;
    try
    {
      // Recupero i dati da DB_DOCUMENTO
      documentoVO = documentoDAO.findDocumentoVOByPrimaryKey(idDocumento);
      
      //solo per i doc fotoint
      if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
      {
        FaseRiesameDocumentoVO  faseRiesameDocumentoVO = documentoGaaDAO.getFaseRiesameDocumentoByIdDocumento(documentoVO
            .getTipoDocumentoVO().getIdDocumento().longValue());
        int faseIstanzaRiesame = faseRiesameDocumentoVO.getIdFaseIstanzaRiesame().intValue();
        documentoVO.setFaseIstanzaRiesame(faseIstanzaRiesame);
        documentoVO.setExtraSistemaIstanzaRiesame(faseRiesameDocumentoVO.getExtraSistema());
        
        //Usato solo nella modifica
        if((SolmrConstants.FASE_IST_RIESAM_FOTO == faseIstanzaRiesame)
          || (SolmrConstants.FASE_IST_RIESAM_CONTRO == faseIstanzaRiesame))
        {
          if(Validator.isNotEmpty(documentoVO.getDataFineValidita()))
            documentoVO.setFlagTastoParticelle("N");  
        }
        
        
        if(SolmrConstants.FASE_IST_RIESAM_FOTO == faseIstanzaRiesame)
        {
          //Controllo solo per doc fotoint, validazione non correttiva dopo
          //data inserimento documento
          java.util.Date dataInserimentoDichNoCorr = consistenzaDAO.getLastDateDichConsNoCorrettiva(
              documentoVO.getIdAzienda());
          if(Validator.isNotEmpty(dataInserimentoDichNoCorr) 
            && dataInserimentoDichNoCorr.after(documentoVO.getDataInserimento()))
          {
            //trovata almeno una lavorata
            //Sempre controllo solo per doc fotoint
            if(documentoGaaDAO.isIstanzaRiesameFotoInterpretataByDocumento(
                documentoVO.getIdDocumento().longValue()))
            {
              documentoVO.setFlagIstanzaRiesameNoModTotale("S");
            }
          }
        }
      }
      
      
      documentoVO.setvAllegatoDocumento(documentoGaaDAO.getElencoFileAllegati(idDocumento.longValue()));
      TipoDocumentoVO tipoDocumentoVO = tipoDocumentoDAO
          .findTipoDocumentoVOByPrimaryKey(documentoVO.getExtIdDocumento());
      documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
      if (Validator.isNotEmpty(documentoVO.getTipoDocumentoVO()
          .getFlagAnagTerr())
          && (documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI) || documentoVO
              .getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(
                  SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)))
      {
        // Recupero i dati da DB_DOCUMENTO_PROPRIETARIO
        Vector<DocumentoProprietarioVO> elencoProprietari = documentoDAO
            .getListProprietariDocumento(idDocumento);
        documentoVO.setElencoProprietari(elencoProprietari);
        Vector<StoricoParticellaVO> elencoParticelle = null;
        String[] orderBy = new String[] { SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY };
        if (legamiAttivi)
        {
          elencoParticelle = storicoParticellaDAO
              .getListParticelleByIdDocumentoAlPianoCorrente(idDocumento, null,
                  orderBy);
        }
        else
        {
          elencoParticelle = storicoParticellaDAO
              .getListParticelleByIdDocumento(idDocumento, orderBy);
        }
        documentoVO.setElencoParticelle(elencoParticelle);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return documentoVO;
  }

  /**
   * Metodo che si occupa di aggiornare i dati del documento
   * 
   * @param documentoVO
   *          DocumentoVO
   * @param RuoloUtenza
   *          ruoloUtenza
   * @param operazioneRichiesta
   *          String
   * @return Long
   * @throws Exception
   */
  public Long aggiornaDocumento(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String operazioneRichiesta)
      throws Exception
  {
    Long idDocumento = null;
    try
    {
      Vector<IstanzaRiesameVO> vIstanzaRiesameIns = null;
      // Se si tratta di storicizzazione ed è possibile effettuarla
      if (operazioneRichiesta.equalsIgnoreCase("storicizza"))
      {
        // Effettuo le operazioni per storicizzare il documento
        documentoDAO.storicizzaDocumentoNoNote(documentoVO, ruoloUtenza);
        // Cesso i legami attivi tra il documento e le particelle ad esso
        // associate
        DocumentoConduzioneVO documentoCessaConduzioneVO = new DocumentoConduzioneVO();
        documentoCessaConduzioneVO.setIdDocumento(documentoVO.getIdDocumento());
        documentoCessaConduzioneVO.setDataFineValidita(new java.util.Date(
            new Timestamp(System.currentTimeMillis()).getTime()));
        documentoDAO
            .cessaDocumentoConduzioneByIdDocumento(documentoCessaConduzioneVO);
        // Una volta storicizzato il documento ripristino lo stato a null
        documentoVO.setIdStatoDocumento(null);
        // Se il documento selezionato era già stato protocollato oppure il tipo
        // documento prevede l'obbligo di protocollazione...
        if (Validator.isNotEmpty(documentoVO.getNumeroProtocollo())
            || documentoVO.getTipoDocumentoVO().getFlagObbligoProtocollo()
                .equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          //Se selezionata come causale modifica la correzione anomalia
          //non devo protocollare          
          if(!(Validator.isNotEmpty(documentoVO.getIdCausaleModificaDocumento()) 
              && documentoVO.getIdCausaleModificaDocumento().longValue() == 1))
          {
            String numeroProtocollo = mathNumeroProtocollo(ruoloUtenza, DateUtils
                .getCurrentYear().toString());
            documentoVO.setNumeroProtocollo(numeroProtocollo);
            documentoVO.setDataProtocollo(new Date(System.currentTimeMillis()));
          }
        }
        Long idDocumentoPrecedente = documentoVO.getIdDocumento();
        //idDocumentoForDelete = documentoVO.getIdDocumento();
        documentoVO.setIdDocumentoPrecedente(idDocumentoPrecedente);
        //Eliminato per richeista Sergio/Teresa 27/03/2012
        //documentoVO.setDataInserimento(new java.util.Date(new Timestamp(System
            //.currentTimeMillis()).getTime()));
        idDocumento = documentoDAO.insertDocumento(documentoVO, ruoloUtenza);
        //sono presenti allegati
        if(documentoVO.getvAllegatoDocumento() != null)
        {
          for(int j=0;j<documentoVO.getvAllegatoDocumento().size();j++)
          {
            documentoGaaDAO.insertAllegatoDocumento(idDocumento.longValue(),
                documentoVO.getvAllegatoDocumento().get(j).getIdAllegato().longValue());
          }          
        }
        documentoVO.setIdDocumento(idDocumento);
        // Se il documento era di tipo T
        if (documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
            .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
            || documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
                .equalsIgnoreCase(
                    SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))
        {
          // Se inseriti
          if (documentoVO.getElencoProprietari() != null
              && documentoVO.getElencoProprietari().size() > 0)
          {
            // Aggiungo i nuovi proprietari presenti a video
            for (int i = 0; i < documentoVO.getElencoProprietari().size(); i++)
            {
              DocumentoProprietarioVO documentoProprietarioVO = (DocumentoProprietarioVO) documentoVO
                  .getElencoProprietari().elementAt(i);
              documentoProprietarioVO.setIdDocumento(documentoVO
                  .getIdDocumento());
              documentoDAO.insertDocumentoProprietario(documentoProprietarioVO,
                  ruoloUtenza);
            }
          }
          // Se sono state inserite a video delle particelle...
          if (documentoVO.getElencoParticelle() != null
              && documentoVO.getElencoParticelle().size() > 0)
          {
            
            HashMap<Long, StoricoParticellaVO> hParticella = null;
            
            
            // ...le inserisco su DB
            for (int j = 0; j < documentoVO.getElencoParticelle().size(); j++)
            {
              StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) documentoVO
                  .getElencoParticelle().elementAt(j);
              
              //mi serve per inserire istanza di riesame
              if(hParticella == null)
              {
                hParticella = new HashMap<Long, StoricoParticellaVO>();
              }
              
              if(hParticella.get(storicoParticellaVO.getIdParticella()) == null)
              {
                hParticella.put(storicoParticellaVO.getIdParticella(), storicoParticellaVO);
              }
              
              DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
              if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
              {
                documentoConduzioneVO.setNote(storicoParticellaVO.getElencoConduzioni()[0]
                  .getElencoDocumentoConduzione()[0].getNote());
                documentoConduzioneVO.setLavorazionePrioritaria(storicoParticellaVO.getElencoConduzioni()[0]
                  .getElencoDocumentoConduzione()[0].getLavorazionePrioritaria());
              }
              documentoConduzioneVO
                  .setIdConduzioneParticella(storicoParticellaVO
                      .getElencoConduzioni()[0].getIdConduzioneParticella());
              documentoConduzioneVO
                  .setIdDocumento(documentoVO.getIdDocumento());
              documentoConduzioneVO.setDataInserimento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              documentoConduzioneVO.setDataInizioValidita(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              if (documentoVO.getDataFineValidita() != null)
              {
                documentoConduzioneVO.setDataFineValidita(new Timestamp(
                    documentoVO.getDataFineValidita().getTime()));
              }
              documentoDAO.insertDocumentoConduzione(documentoConduzioneVO);
            }
            
            
            
            
            //Inserisco su db_istanza_riesame
            if((documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO)
              || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
              || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_SOPRA))
            {
              if(Validator.isNotEmpty(hParticella))
              {
                Collection<StoricoParticellaVO> collection = hParticella.values();
                Iterator<StoricoParticellaVO> iterator = collection.iterator();
                
                String protocolloIstanza = documentoGaaDAO.getProtocolloIstanza(documentoVO.getIdAzienda().longValue(), 
                    DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
                if(Validator.isEmpty(protocolloIstanza))
                {
                  long idIstanza = documentoGaaDAO.getNewIdMaxProtocolloIstanza(DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
                  protocolloIstanza = "IST-"+DateUtils.extractYearFromDate(documentoVO.getDataInserimento())
                      +"-"+idIstanza;
                  
                  
                  IstanzaRiesameAziendaVO istanzaRiesameAziendaVO = new IstanzaRiesameAziendaVO();
                  istanzaRiesameAziendaVO.setIdAzienda(documentoVO.getIdAzienda());
                  istanzaRiesameAziendaVO.setAnnoIstanza(DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
                  istanzaRiesameAziendaVO.setProtocolloIstanza(protocolloIstanza);
                  
                  documentoGaaDAO.insertIstanzaRiesameAzienda(istanzaRiesameAziendaVO);
                  
                }
                
                
                while (iterator.hasNext())
                {
                  StoricoParticellaVO storicoParticellaVO = iterator.next();                              
                  
                  if(!documentoGaaDAO.existAltraFaseFotoParticella(documentoVO.getIdAzienda(),
                      storicoParticellaVO.getIdParticella(), DateUtils.extractYearFromDate(documentoVO.getDataInserimento()),
                      documentoVO.getFaseIstanzaRiesame()))
                  {                 
                    IstanzaRiesameVO istanzaRiesameVO = new IstanzaRiesameVO();                    
                    
                    if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo()))
                    {
                      istanzaRiesameVO.setIdStatoSitiConvoca(new Long(0));
                    }
                    istanzaRiesameVO.setIdFaseIstanzaRiesame(new Long(documentoVO.getFaseIstanzaRiesame()));       
                    istanzaRiesameVO.setAnno(DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
                    istanzaRiesameVO.setIdAzienda(documentoVO.getIdAzienda().longValue());
                    istanzaRiesameVO.setIdParticella(storicoParticellaVO.getIdParticella().longValue());
                    istanzaRiesameVO.setDataRichiesta(new Date());
                    istanzaRiesameVO.setDataAggiornamento(new Date());
                    istanzaRiesameVO.setLavorazionePrioritaria(storicoParticellaVO.getElencoConduzioni()[0]
                      .getElencoDocumentoConduzione()[0].getLavorazionePrioritaria());
                    istanzaRiesameVO.setNote(storicoParticellaVO.getElencoConduzioni()[0]
                      .getElencoDocumentoConduzione()[0].getNote());
                    istanzaRiesameVO.setProtocolloIstanza(protocolloIstanza);
                    
                    if(vIstanzaRiesameIns == null)
                    {
                      vIstanzaRiesameIns = new Vector<IstanzaRiesameVO>();
                    }
                    
                    vIstanzaRiesameIns.add(istanzaRiesameVO);
                    
                  }
                  
                }
              }
            }
            
            
            
            
            
          }
          
        }
        // Devo inserire le particelle associate
        if (documentoVO
            .getTipoDocumentoVO()
            .getFlagAnagTerr()
            .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))
        {
          if (documentoVO.getParticelleAssociate() != null)
          {
            int size = documentoVO.getParticelleAssociate().size();
            for (int i = 0; i < size; i++)
              eventoParticellaDAO
                  .insertDocCorParticella((ParticellaAssVO) documentoVO
                      .getParticelleAssociate().get(i), idDocumento);
          }
        }

      }
      else if(operazioneRichiesta.equalsIgnoreCase("modificaNote"))
      {
        documentoDAO.updateDocumento(documentoVO, ruoloUtenza);
      }
      
      //da fare solo per i doc istanza di riesame
      if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
      {        
      
        PlSqlCodeDescription plCode = particellaGaaDAO.annullaIstanzaPlSql(documentoVO.getIdAzienda().longValue(), 
            DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), documentoVO.getFaseIstanzaRiesame(), 
            ruoloUtenza.getIdUtente().longValue());
        
        if(plCode !=null)
        {
          if(Validator.isNotEmpty(plCode.getDescription()))
          {
            throw new Exception("Aggiorna documento errore al plsql annullaIstanzaRiesame: "+plCode.getDescription()+"-"
                +plCode.getOtherdescription());       
          }
        }
        
        
        //inserisco du db_istanza_risame
        if(vIstanzaRiesameIns != null)
        {
          for(int i=0;i<vIstanzaRiesameIns.size();i++)
          {
            Long idIstanzaRiesame = documentoGaaDAO.insertIstanzaRiesame(vIstanzaRiesameIns.get(i), ruoloUtenza.getIdUtente());            
            
            
            //aggiorno la fase precedente in caso di protocollo diverso
            if(documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
            {
              IstanzaRiesameVO istanzaRiesameEvasaVO = documentoGaaDAO.getIstanzaRiesameFaseEvasa(documentoVO.getIdAzienda().longValue(), 
                  vIstanzaRiesameIns.get(i).getIdParticella(), SolmrConstants.FASE_IST_RIESAM_FOTO, DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
              if(Validator.isNotEmpty(istanzaRiesameEvasaVO))
              {
                //se il protocollo è diverso devo aggiornare il protocollo della fase prec
                if(!istanzaRiesameEvasaVO.getProtocolloIstanza().equalsIgnoreCase(vIstanzaRiesameIns.get(i).getProtocolloIstanza()))
                {
                  documentoGaaDAO.updateIstanzaProtocolloFasePrec(
                      istanzaRiesameEvasaVO.getIdIstanzaRiesame(), vIstanzaRiesameIns.get(i).getProtocolloIstanza());
                }                    
                
              }
            }            
            
            
            FaseRiesameDocumentoVO faseRiesameDocumentoVO = documentoGaaDAO
                .getFaseRiesameDocumentoByIdDocumento(documentoVO.getExtIdDocumento().longValue());
            if(!"N".equalsIgnoreCase(faseRiesameDocumentoVO.getExtraSistema()))
            {
              documentoGaaDAO.updateIstanzaPotenziale(documentoVO.getIdAzienda().longValue(), 
                  vIstanzaRiesameIns.get(i).getIdParticella(), 
                  DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), idIstanzaRiesame,
                  ruoloUtenza.getIdUtente());
            }
          
          }          
        }
        else if (!(documentoVO.getElencoParticelle() != null
            && documentoVO.getElencoParticelle().size() > 0))
        {          
          documentoDAO.updateDocumentoIstanza(documentoVO.getIdDocumento(), null);
        }
        
        
        
      }
      
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
    return idDocumento;
  }
  
  
  
  public Long aggiornaDocumentoIstanzaLimitato(DocumentoVO documentoVO,
      RuoloUtenza ruoloUtenza, String operazioneRichiesta)
      throws Exception
  {
    Long idDocumento = null;
    try
    {
      Vector<IstanzaRiesameVO> vIstanzaRiesameIns = null;
      // Se si tratta di storicizzazione ed è possibile effettuarla
      if (operazioneRichiesta.equalsIgnoreCase("storicizza"))
      {
        // Effettuo le operazioni per storicizzare il documento
        documentoDAO.storicizzaDocumentoNoNote(documentoVO, ruoloUtenza);
        //devo farlo prima perchè poi storicizzo le conduzioni attive
        Vector<Long> vPartFotoInter = documentoGaaDAO
            .getParticelleIstanzaRiesameFotoInterpretataByDocumento(documentoVO.getIdDocumento().longValue());
        // Cesso i legami attivi tra il documento e le particelle ad esso
        // associate
        DocumentoConduzioneVO documentoCessaConduzioneVO = new DocumentoConduzioneVO();
        documentoCessaConduzioneVO.setIdDocumento(documentoVO.getIdDocumento());
        documentoCessaConduzioneVO.setDataFineValidita(new java.util.Date(
            new Timestamp(System.currentTimeMillis()).getTime()));
        documentoDAO
            .cessaDocumentoConduzioneByIdDocumento(documentoCessaConduzioneVO);
        // Una volta storicizzato il documento ripristino lo stato a null
        documentoVO.setIdStatoDocumento(null);
        // Se il documento selezionato era già stato protocollato oppure il tipo
        // documento prevede l'obbligo di protocollazione...
        if (Validator.isNotEmpty(documentoVO.getNumeroProtocollo())
            || documentoVO.getTipoDocumentoVO().getFlagObbligoProtocollo()
                .equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          //Se selezionata come causale modifica la correzione anomalia
          //non devo protocollare          
          if(!(Validator.isNotEmpty(documentoVO.getIdCausaleModificaDocumento()) 
              && documentoVO.getIdCausaleModificaDocumento().longValue() == 1))
          {
            String numeroProtocollo = mathNumeroProtocollo(ruoloUtenza, DateUtils
                .getCurrentYear().toString());
            documentoVO.setNumeroProtocollo(numeroProtocollo);
            documentoVO.setDataProtocollo(new Date(System.currentTimeMillis()));
          }
        }
        Long idDocumentoPrecedente = documentoVO.getIdDocumento();
        //idDocumentoForDelete = documentoVO.getIdDocumento();
        documentoVO.setIdDocumentoPrecedente(idDocumentoPrecedente);
        //Eliminato per richeista Sergio/Teresa 27/03/2012
        //documentoVO.setDataInserimento(new java.util.Date(new Timestamp(System
            //.currentTimeMillis()).getTime()));
        idDocumento = documentoDAO.insertDocumento(documentoVO, ruoloUtenza);
        //sono presenti allegati
        if(documentoVO.getvAllegatoDocumento() != null)
        {
          for(int j=0;j<documentoVO.getvAllegatoDocumento().size();j++)
          {
            documentoGaaDAO.insertAllegatoDocumento(idDocumento.longValue(),
                documentoVO.getvAllegatoDocumento().get(j).getIdAllegato().longValue());
          }          
        }
        documentoVO.setIdDocumento(idDocumento);
        // Se il documento era di tipo T

        if (documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
            .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
            || documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
                .equalsIgnoreCase(
                    SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))
        {
          // Se inseriti
          if (documentoVO.getElencoProprietari() != null
              && documentoVO.getElencoProprietari().size() > 0)
          {
            // Aggiungo i nuovi proprietari presenti a video
            for (int i = 0; i < documentoVO.getElencoProprietari().size(); i++)
            {
              DocumentoProprietarioVO documentoProprietarioVO = (DocumentoProprietarioVO) documentoVO
                  .getElencoProprietari().elementAt(i);
              documentoProprietarioVO.setIdDocumento(documentoVO
                  .getIdDocumento());
              documentoDAO.insertDocumentoProprietario(documentoProprietarioVO,
                  ruoloUtenza);
            }
          }
          // Se sono presenti a video delle particelle...
          if (documentoVO.getElencoParticelle() != null
              && documentoVO.getElencoParticelle().size() > 0)
          {
            
            HashMap<Long, StoricoParticellaVO> hParticella = null;
            
            // ...le inserisco su DB
            for (int j = 0; j < documentoVO.getElencoParticelle().size(); j++)
            {
              StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) documentoVO
                  .getElencoParticelle().elementAt(j);
              
              
              //mi serve per inserire istanza di riesame
              if(hParticella == null)
              {
                hParticella = new HashMap<Long, StoricoParticellaVO>();
              }
              
              if(hParticella.get(storicoParticellaVO.getIdParticella()) == null)
              {
                hParticella.put(storicoParticellaVO.getIdParticella(), storicoParticellaVO);
              }
                      
              DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
              if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
              {
                documentoConduzioneVO.setNote(storicoParticellaVO.getElencoConduzioni()[0]
                  .getElencoDocumentoConduzione()[0].getNote());
                documentoConduzioneVO.setLavorazionePrioritaria(storicoParticellaVO.getElencoConduzioni()[0]
                  .getElencoDocumentoConduzione()[0].getLavorazionePrioritaria());
              }
              documentoConduzioneVO
                  .setIdConduzioneParticella(storicoParticellaVO
                      .getElencoConduzioni()[0].getIdConduzioneParticella());
              documentoConduzioneVO
                  .setIdDocumento(documentoVO.getIdDocumento());               
           
              documentoConduzioneVO.setDataInserimento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              documentoConduzioneVO.setDataInizioValidita(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              if (documentoVO.getDataFineValidita() != null)
              {
                documentoConduzioneVO.setDataFineValidita(new Timestamp(
                    documentoVO.getDataFineValidita().getTime()));
              }
              
              documentoDAO.insertDocumentoConduzione(documentoConduzioneVO);         
              
              
            }
            
            
            
            //Inserisco su db_istanza_riesame solo se foto
            if((documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_FOTO)
                || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
                || (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_SOPRA))
            {
              if(Validator.isNotEmpty(hParticella))
              {
                Collection<StoricoParticellaVO> collection = hParticella.values();
                Iterator<StoricoParticellaVO> iterator = collection.iterator();
                
                String protocolloIstanza = documentoGaaDAO.getProtocolloIstanza(documentoVO.getIdAzienda().longValue(), 
                    DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
                if(Validator.isEmpty(protocolloIstanza))
                {
                  long idIstanza = documentoGaaDAO.getNewIdMaxProtocolloIstanza(DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
                  protocolloIstanza = "IST-"+DateUtils.extractYearFromDate(documentoVO.getDataInserimento())
                      +"-"+idIstanza;
                  
                  IstanzaRiesameAziendaVO istanzaRiesameAziendaVO = new IstanzaRiesameAziendaVO();
                  istanzaRiesameAziendaVO.setIdAzienda(documentoVO.getIdAzienda());
                  istanzaRiesameAziendaVO.setAnnoIstanza(DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
                  istanzaRiesameAziendaVO.setProtocolloIstanza(protocolloIstanza);
                  
                  documentoGaaDAO.insertIstanzaRiesameAzienda(istanzaRiesameAziendaVO);
                }
                
                
                
                
                while (iterator.hasNext())
                {
                  StoricoParticellaVO storicoParticellaVO = iterator.next();                  
                  
                  if(!documentoGaaDAO.existAltraFaseFotoParticella(documentoVO.getIdAzienda(),
                      storicoParticellaVO.getIdParticella(), DateUtils.extractYearFromDate(documentoVO.getDataInserimento()),
                      documentoVO.getFaseIstanzaRiesame()))
                  {                 
                    IstanzaRiesameVO istanzaRiesameVO = new IstanzaRiesameVO();
                    
                    if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo()))
                    {
                      istanzaRiesameVO.setIdStatoSitiConvoca(new Long(0));
                    }
                    
                    istanzaRiesameVO.setIdFaseIstanzaRiesame(new Long(documentoVO.getFaseIstanzaRiesame()).longValue());                    
                    istanzaRiesameVO.setAnno(DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
                    istanzaRiesameVO.setIdAzienda(documentoVO.getIdAzienda().longValue());
                    istanzaRiesameVO.setIdParticella(storicoParticellaVO.getIdParticella().longValue());
                    istanzaRiesameVO.setDataRichiesta(new Date());
                    istanzaRiesameVO.setDataAggiornamento(new Date());
                    istanzaRiesameVO.setLavorazionePrioritaria(storicoParticellaVO.getElencoConduzioni()[0]
                      .getElencoDocumentoConduzione()[0].getLavorazionePrioritaria());
                    istanzaRiesameVO.setNote(storicoParticellaVO.getElencoConduzioni()[0]
                      .getElencoDocumentoConduzione()[0].getNote());                    
                    istanzaRiesameVO.setProtocolloIstanza(protocolloIstanza);
                    
                    if(vIstanzaRiesameIns == null)
                    {
                      vIstanzaRiesameIns = new Vector<IstanzaRiesameVO>();
                    }
                    
                    vIstanzaRiesameIns.add(istanzaRiesameVO);
                    
                  }
                  
                }
              }
            }
            
            
            
            
            
          }
          
          
          //re inserisco quelle storicizzate fotointerpretate
          if(Validator.isNotEmpty(vPartFotoInter))
          {
            for(int j=0;j<vPartFotoInter.size();j++)
            {
              Vector<DocumentoConduzioneVO> vDocCond = documentoGaaDAO.
                  getDocCondIstanzaRiesameFotoInterpretataByDocumentoAndParticella(idDocumentoPrecedente.longValue(), 
                      vPartFotoInter.get(j).longValue());
              //ripristini tutti i legami di particelle evase non presenti a video...
              for(int k=0;k<vDocCond.size();k++)
              {
                DocumentoConduzioneVO documentoConduzioneVO = vDocCond.get(k);
                documentoConduzioneVO.setIdDocumento(documentoVO.getIdDocumento());
                documentoDAO.insertDocumentoConduzione(documentoConduzioneVO);
              }            
              
            }
          }
          
        }
        // Devo inserire le particelle associate
        if (documentoVO
            .getTipoDocumentoVO()
            .getFlagAnagTerr()
            .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))
        {
          if (documentoVO.getParticelleAssociate() != null)
          {
            int size = documentoVO.getParticelleAssociate().size();
            for (int i = 0; i < size; i++)
              eventoParticellaDAO
                  .insertDocCorParticella((ParticellaAssVO) documentoVO
                      .getParticelleAssociate().get(i), idDocumento);
          }
        }

      }
      else if(operazioneRichiesta.equalsIgnoreCase("modificaNote"))
      {
        documentoDAO.updateDocumento(documentoVO, ruoloUtenza);
      }
      
      
      //da fare solo per i doc istanza di riesame
      if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
      {        
      
        PlSqlCodeDescription plCode = particellaGaaDAO.annullaIstanzaPlSql(documentoVO.getIdAzienda().longValue(), 
            DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), documentoVO.getFaseIstanzaRiesame(),
            ruoloUtenza.getIdUtente().longValue());
        
        if(plCode !=null)
        {
          if(Validator.isNotEmpty(plCode.getDescription()))
          {
            throw new Exception("Aggiorna documento errore al plsql annullaIstanzaRiesame: "+plCode.getDescription()+"-"
                +plCode.getOtherdescription());       
          }
        }
        
        
        //inserisco du db_istanza_risame
        if(vIstanzaRiesameIns != null)
        {
          for(int i=0;i<vIstanzaRiesameIns.size();i++)
          {
            Long idIstanzaRiesame = documentoGaaDAO.insertIstanzaRiesame(vIstanzaRiesameIns.get(i), ruoloUtenza.getIdUtente());            
            
            //aggiorno la fase precedente in caso di protocollo diverso
            if(documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
            {
              IstanzaRiesameVO istanzaRiesameEvasaVO = documentoGaaDAO.getIstanzaRiesameFaseEvasa(documentoVO.getIdAzienda().longValue(), 
                  vIstanzaRiesameIns.get(i).getIdParticella(), SolmrConstants.FASE_IST_RIESAM_FOTO, DateUtils.extractYearFromDate(documentoVO.getDataInserimento()));
              if(Validator.isNotEmpty(istanzaRiesameEvasaVO))
              {
                //se il protocollo è diverso devo aggiornare il protocollo della fase prec
                if(!istanzaRiesameEvasaVO.getProtocolloIstanza().equalsIgnoreCase(vIstanzaRiesameIns.get(i).getProtocolloIstanza()))
                {
                  documentoGaaDAO.updateIstanzaProtocolloFasePrec(
                      istanzaRiesameEvasaVO.getIdIstanzaRiesame(), vIstanzaRiesameIns.get(i).getProtocolloIstanza());
                }                    
                
              }
            }
            
            
            
            
            FaseRiesameDocumentoVO faseRiesameDocumentoVO = documentoGaaDAO
                .getFaseRiesameDocumentoByIdDocumento(documentoVO.getExtIdDocumento().longValue());
            if(!"N".equalsIgnoreCase(faseRiesameDocumentoVO.getExtraSistema()))
            {
              documentoGaaDAO.updateIstanzaPotenziale(documentoVO.getIdAzienda().longValue(), 
                  vIstanzaRiesameIns.get(i).getIdParticella(), 
                  DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), idIstanzaRiesame,
                  ruoloUtenza.getIdUtente());
            }
          
          }          
        }
        else if (!(documentoVO.getElencoParticelle() != null
            && documentoVO.getElencoParticelle().size() > 0))
        {          
          documentoDAO.updateDocumentoIstanza(documentoVO.getIdDocumento(), null);
        }
        
        
      }
      
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
    return idDocumento;
  }

  /**
   * Metodo che si occupa di eliminare i documenti
   * 
   */
  public void deleteDocumenti(String[] documentiDaEliminare, String note, long idUtenteAggiornamento) throws Exception
  {
    try
    {
      for (int i = 0; i < documentiDaEliminare.length; i++)
      {
        String documento = (String) documentiDaEliminare[i];
        DocumentoVO documentoVO = documentoDAO.findDocumentoVOByPrimaryKey(Long
            .decode(documento));
        documentoVO.setIdStatoDocumento(Long
            .decode(SolmrConstants.ID_STATO_DOCUMENTO_ANNULLATO));
        documentoVO.setNote(note);
        documentoDAO.storicizzaDocumento(documentoVO, idUtenteAggiornamento);
        // Cesso i legami attivi tra il documento e le particelle ad esso
        // associate
        DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
        documentoConduzioneVO.setIdDocumento(Long.decode(documento));
        documentoConduzioneVO.setDataFineValidita(new java.util.Date(
            new Timestamp(System.currentTimeMillis()).getTime()));
        documentoDAO
            .cessaDocumentoConduzioneByIdDocumento(documentoConduzioneVO);
        
        
        if("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
        {
          FaseRiesameDocumentoVO  faseRiesameDocumentoVO = documentoGaaDAO.getFaseRiesameDocumentoByIdDocumento(documentoVO
              .getTipoDocumentoVO().getIdDocumento().longValue());
          int faseIstanzaRiesame = faseRiesameDocumentoVO.getIdFaseIstanzaRiesame().intValue();
          documentoVO.setFaseIstanzaRiesame(faseIstanzaRiesame);
        
          PlSqlCodeDescription plCode = particellaGaaDAO.annullaIstanzaPlSql(documentoVO.getIdAzienda().longValue(), 
              DateUtils.extractYearFromDate(documentoVO.getDataInserimento()), documentoVO.getFaseIstanzaRiesame(),
              idUtenteAggiornamento);
          
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
      
      
     
      
      
      
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di effettuare la protocollazione dei documenti
   * 
   */
  public void protocollaDocumenti(String[] documentiDaProtocollare, Long idAzienda,
      RuoloUtenza ruoloUtenza) throws Exception
  {
    try
    {
      Date dataOdierna = DateUtils.parseDate(DateUtils
          .getCurrent((String) SolmrConstants.get("DATE_EUROPE_FORMAT")));
      DocumentoVO documentoVO = null;
      for (int i = 0; i < documentiDaProtocollare.length; i++)
      {
        documentoVO = getDettaglioDocumento(Long
            .decode((String) documentiDaProtocollare[i]), true);
        Date dataInserimento = DateUtils.parseDate(DateUtils.formatDate(
            documentoVO.getDataInserimento()).substring(0, 10));
        // Se il documento selezionato non aveva il numero di protocollo oppure
        // è già stata fatta una storicizzazione nella giornata corrente
        if (Validator.isEmpty(documentoVO.getNumeroProtocollo())
            || dataInserimento.compareTo(dataOdierna) == 0)
        {
          // Lo calcolo
          String numeroProtocollo = mathNumeroProtocollo(ruoloUtenza, 
              String.valueOf(DateUtils.getCurrentYear()));
          documentoVO.setNumeroProtocollo(numeroProtocollo);
          documentoDAO.protocollaDocumento(documentoVO, ruoloUtenza.getIdUtente());
        }
        // Altrimenti storicizzo il documento vecchio e inserisco il nuovo con
        // il nuovo numero di protocollo
        else
        {
          documentoVO.setIdStatoDocumento(Long
              .decode(SolmrConstants.ID_STATO_DOCUMENTO_STORICIZZATO));
          documentoVO.setNote("Riprotocollazione del documento");
          documentoDAO.storicizzaDocumento(documentoVO, ruoloUtenza.getIdUtente());
          documentoVO.setIdStatoDocumento(null);
          documentoVO.setNote(null);
          // calcolo il nuovo protocollo
          String numeroProtocollo = mathNumeroProtocollo(ruoloUtenza, 
              String.valueOf(DateUtils.getCurrentYear()));
          documentoVO.setNumeroProtocollo(numeroProtocollo);
          documentoVO.setDataProtocollo(new Date(System.currentTimeMillis()));
          // Inserisco il nuovo documento
          documentoVO.setDataUltimoAggiornamento(new java.util.Date(
              new Timestamp(System.currentTimeMillis()).getTime()));
          //NOn metto più la sysdate richiesta voluta Sergio/Teresa 26/03/2012
          //documentoVO.setDataInserimento(new java.util.Date(new Timestamp(
              //System.currentTimeMillis()).getTime()));
          Long idDocumento = documentoDAO.insertDocumento(documentoVO, ruoloUtenza);
          // Recupero i vecchi proprietari
          Vector<DocumentoProprietarioVO> elencoProprietari = documentoVO.getElencoProprietari();
          Vector<?> elencoParticelle = documentoVO.getElencoParticelle();
          documentoVO.setIdDocumento(idDocumento);
          // Se si tratta di un documento di tipologia diversa da "anagrafico"
          if (documentoVO.getTipoDocumentoVO() != null
              && Validator.isNotEmpty(documentoVO.getTipoDocumentoVO()
                  .getIdTipologiaDocumento())
              && documentoVO
                  .getTipoDocumentoVO()
                  .getIdTipologiaDocumento()
                  .compareTo(SolmrConstants.ID_TIPOLOGIA_DOCUMENTO_TERRITORIALE) == 0)
          {
            // Inserisco i nuovi proprietari
            if (elencoProprietari != null && elencoProprietari.size() > 0)
            {
              for (int j = 0; j < elencoProprietari.size(); j++)
              {
                DocumentoProprietarioVO documentoProprietarioVO = (DocumentoProprietarioVO) elencoProprietari
                    .elementAt(j);
                documentoProprietarioVO.setIdDocumento(documentoVO
                    .getIdDocumento());
                documentoDAO.insertDocumentoProprietario(
                    documentoProprietarioVO, ruoloUtenza);
              }
            }
          }
          // Inserisco, se ci sono, le nuove particelle
          if (elencoParticelle != null && elencoParticelle.size() > 0)
          {
            for (int j = 0; j < documentoVO.getElencoParticelle().size(); j++)
            {
              StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) documentoVO
                  .getElencoParticelle().elementAt(j);
              DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
              documentoConduzioneVO
                  .setIdConduzioneParticella(storicoParticellaVO
                      .getElencoConduzioni()[0].getIdConduzioneParticella());
              documentoConduzioneVO
                  .setIdDocumento(documentoVO.getIdDocumento());
              documentoConduzioneVO.setDataInserimento(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              documentoConduzioneVO.setDataInizioValidita(new java.util.Date(
                  new Timestamp(System.currentTimeMillis()).getTime()));
              documentoDAO.insertDocumentoConduzione(documentoConduzioneVO);
            }
          }
        }
        
        //Da fare solo se il doc è istanza riesame fase 2
        if(documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
        {
          Vector<StoricoParticellaVO> elencoPartIstanza = storicoParticellaDAO.getAllParticelleByIdDocumento(documentoVO.getIdDocumento()); 
          if(Validator.isNotEmpty(elencoPartIstanza))
          {
            Vector<Long> vIdParticella = new Vector<Long>();
            for(int j=0;j<elencoPartIstanza.size();j++)
            {
              StoricoParticellaVO storicoParticellaVO = elencoPartIstanza.get(j);
              Long idParticella = storicoParticellaVO.getIdParticella();
              if(!vIdParticella.contains(idParticella))
              {
                Long idIstanzaRiesame = documentoGaaDAO.getParticellaIstRiesameDocProto(idAzienda, idParticella, SolmrConstants.FASE_IST_RIESAM_CONTRO);
                if(Validator.isNotEmpty(idIstanzaRiesame))
                {
                  documentoGaaDAO.aggiornaStatoSiticonvoca(idIstanzaRiesame, 0, ruoloUtenza.getIdUtente());                  
                }
                vIdParticella.add(idParticella);
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
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Metodo che si occupa di recuperare l'elenco dei documenti a partire
   * dall'id_conduzione_particella o dall'id_conduzione_dichiarata in relazione
   * al boolean isStorico
   * 
   * @param idConduzione
   * @param isStorico
   * @param altreParticelle
   * @return java.util.Vector
   * @throws Exception
   */
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzione(
      Long idConduzione, boolean isStorico, boolean altreParticelle)
      throws Exception, SolmrException
  {
    Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();
    StoricoParticellaVO oldStoricoParticellaVO = null;
    try
    {
      // Recupero i dati dei documenti da DB_DOCUMENTO
      if (!isStorico)
      {
        //Prima era false, ora true modifica voluta da Terry il 13/05/2010
        elencoDocumenti = documentoDAO
            .getListDocumentiByIdConduzioneParticella(idConduzione, true); 
        if (altreParticelle)
        {
          oldStoricoParticellaVO = storicoParticellaDAO
              .findStoricoParticellaVOByIdConduzioneParticella(idConduzione);
        }
      }
      else
      {
        elencoDocumenti = documentoDAO
            .getListDocumentiByIdConduzioneDichiarata(idConduzione);
        if (altreParticelle)
        {
          oldStoricoParticellaVO = storicoParticellaDAO
              .findStoricoParticellaVOByIdConduzioneDichiarata(idConduzione);
        }
      }
      if (elencoDocumenti != null && elencoDocumenti.size() > 0)
      {
        Iterator<DocumentoVO> iteraDocumenti = elencoDocumenti.iterator();
        while (iteraDocumenti.hasNext())
        {
          DocumentoVO documentoVO = (DocumentoVO) iteraDocumenti.next();
          // Recupero i dati da DB_DOCUMENTO_PROPRIETARIO
          Vector<DocumentoProprietarioVO> elencoProprietari = documentoDAO
              .getListProprietariDocumento(documentoVO.getIdDocumento());
          documentoVO.setElencoProprietari(elencoProprietari);
          // Recupero le particelle associate al documento
          Vector<StoricoParticellaVO> elencoParticelle = null;
          String[] orderBy = new String[] { SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY };
          if (!isStorico)
          {
            if (altreParticelle)
            {
              elencoParticelle = storicoParticellaDAO
                  .getListParticelleByIdDocumentoAlPianoCorrente(documentoVO
                      .getIdDocumento(), oldStoricoParticellaVO
                      .getIdStoricoParticella(), orderBy);
            }
            else
            {
              elencoParticelle = storicoParticellaDAO
                  .getListParticelleByIdDocumentoAlPianoCorrente(documentoVO
                      .getIdDocumento(), null, orderBy);
            }
          }
          else
          {
            ConsistenzaVO consistenzaVO = consistenzaDAO
                .findDichiarazioneConsistenzaByIdConduzioneDichiarata(idConduzione);
            if (altreParticelle)
            {
              elencoParticelle = storicoParticellaDAO
                  .getListParticelleByIdDocumentoAllaDichiarazione(documentoVO
                      .getIdDocumento(), oldStoricoParticellaVO
                      .getIdStoricoParticella(), consistenzaVO
                      .getDataInserimentoDichiarazione(), orderBy);
            }
            else
            {
              elencoParticelle = storicoParticellaDAO
                  .getListParticelleByIdDocumentoAllaDichiarazione(documentoVO
                      .getIdDocumento(), null, consistenzaVO
                      .getDataInserimentoDichiarazione(), orderBy);
            }
          }
          documentoVO.setElencoParticelle(elencoParticelle);
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoDocumenti;
  }
  
  
  /**
   * usato nella pop up dei documenti al piano in lavorazione
   * 
   * 
   * @param idConduzione
   * @return
   * @throws Exception
   * @throws SolmrException
   */
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzionePopUp(Long idConduzioneParticella)
      throws Exception, SolmrException
  {
    Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();
    //StoricoParticellaVO oldStoricoParticellaVO = null;
    try
    {
      // Recupero i dati dei documenti da DB_DOCUMENTO
      elencoDocumenti = documentoDAO.getListDocumentiByIdConduzioneParticellaLegami(idConduzioneParticella);
      if (elencoDocumenti != null && elencoDocumenti.size() > 0)
      {
        Iterator<DocumentoVO> iteraDocumenti = elencoDocumenti.iterator();
        while (iteraDocumenti.hasNext())
        {
          DocumentoVO documentoVO = (DocumentoVO) iteraDocumenti.next();
          // Recupero i dati da DB_DOCUMENTO_PROPRIETARIO
          Vector<DocumentoProprietarioVO> elencoProprietari = documentoDAO
              .getListProprietariDocumento(documentoVO.getIdDocumento());
          documentoVO.setElencoProprietari(elencoProprietari);
          // Recupero le particelle associate al documento
          Vector<StoricoParticellaVO> elencoParticelle = null;
          String[] orderBy = new String[] { SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY };           
          elencoParticelle = storicoParticellaDAO
              .getListParticelleByIdDocumentoAlPianoCorrenteLegamiScaduti(documentoVO.getIdDocumento(), orderBy);            
          documentoVO.setElencoParticelle(elencoParticelle);
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoDocumenti;
  }
  
  /**
   * usato nella pop up dei documenti alla dichiarazione di consistenza
   * 
   * 
   * 
   * @param idConduzioneDichiarata
   * @return
   * @throws Exception
   * @throws SolmrException
   */
  public Vector<DocumentoVO> getListDettaglioDocumentoByIdConduzioneDichiarataPopUp(
      Long idConduzioneDichiarata)
      throws Exception, SolmrException
  {
    Vector<DocumentoVO> elencoDocumenti = new Vector<DocumentoVO>();
    StoricoParticellaVO oldStoricoParticellaVO = null;
    try
    {
      
      elencoDocumenti = documentoDAO
          .getListDocumentiByIdConduzioneDichiarataPopUp(idConduzioneDichiarata);
      oldStoricoParticellaVO = storicoParticellaDAO
        .findStoricoParticellaVOByIdConduzioneDichiarata(idConduzioneDichiarata);
       
      if (elencoDocumenti != null && elencoDocumenti.size() > 0)
      {
        Iterator<DocumentoVO> iteraDocumenti = elencoDocumenti.iterator();
        while (iteraDocumenti.hasNext())
        {
          DocumentoVO documentoVO = (DocumentoVO) iteraDocumenti.next();
          // Recupero i dati da DB_DOCUMENTO_PROPRIETARIO
          Vector<DocumentoProprietarioVO> elencoProprietari = documentoDAO
              .getListProprietariDocumento(documentoVO.getIdDocumento());
          documentoVO.setElencoProprietari(elencoProprietari);
          // Recupero le particelle associate al documento
          Vector<StoricoParticellaVO> elencoParticelle = null;
          String[] orderBy = new String[] { SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY };
          
          ConsistenzaVO consistenzaVO = consistenzaDAO
              .findDichiarazioneConsistenzaByIdConduzioneDichiarata(idConduzioneDichiarata);
            
          elencoParticelle = storicoParticellaDAO
            .getListParticelleByIdDocumentoAllaDichiarazione(documentoVO
            .getIdDocumento(), oldStoricoParticellaVO
            .getIdStoricoParticella(), consistenzaVO
            .getDataInserimentoDichiarazione(), orderBy);         
          documentoVO.setElencoParticelle(elencoParticelle);
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoDocumenti;
  }

  /**
   * Metodo che mi restituisce l'elenco dei documenti associati ad un'azienda
   * 
   * @param idAzienda
   * @param onlyActive
   * @param orderBy
   * @param idTipologiaDocumento
   * @return it.csi.solmr.dto.anag.DocumentoVO[]
   * @throws Exception
   */
  public DocumentoVO[] getListDocumentiByIdAzienda(Long idAzienda,
      boolean onlyActive, String[] orderBy, Long idTipologiaDocumento)
      throws Exception
  {
    DocumentoVO[] elencoDocumenti = null;
    try
    {
      elencoDocumenti = documentoDAO.getListDocumentiByIdAzienda(idAzienda,
          onlyActive, orderBy, idTipologiaDocumento);
      for (int i = 0; i < elencoDocumenti.length; i++)
      {
        DocumentoVO documentoVO = (DocumentoVO) elencoDocumenti[i];
        TipoDocumentoVO tipoDocumentoVO = tipoDocumentoDAO
            .findTipoDocumentoVOByPrimaryKey(documentoVO.getExtIdDocumento());
        documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
        documentoVO.setFlagIstanzaRiesame("N");
        if(documentoDAO.isDocIstanzaRiesame(documentoVO.getExtIdDocumento().longValue()))
        {
          documentoVO.setFlagIstanzaRiesame("S");
        }
        elencoDocumenti[i] = documentoVO;
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoDocumenti;
  }

  /**
   * Metodo per recuperare tutti i dati presenti nella tabella
   * DB_CATEGORIA_DOCUMENTO
   * 
   * @param idTipologiaDocumento
   * @param orderBy[]
   * @return it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO[]
   * @throws Exception
   */
  public TipoCategoriaDocumentoVO[] getListTipoCategoriaDocumentoByIdTipologiaDocumento(
      Long idTipologiaDocumento, String orderBy[]) throws Exception
  {
    try
    {
      return tipoCategoriaDocumentoDAO
          .getListTipoCategoriaDocumentoByIdTipologiaDocumento(
              idTipologiaDocumento, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare tutti i dati presenti nella tabella DB_TIPO_DOCUMENTO
   * a partire dall'id_documento_categoria
   * 
   * @param idCategoriaDocumento
   * @param orderBy[]
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
   * @throws Exception
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumento(
      Long idCategoriaDocumento, String orderBy[], boolean onlyActive,
      Boolean cessata) throws Exception
  {
    try
    {
      return tipoDocumentoDAO.getListTipoDocumentoByIdCategoriaDocumento(
          idCategoriaDocumento, orderBy, onlyActive, cessata);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare il record su DB_CATEGORIA_DOCUMENTO a partire dalla
   * chiave primaria
   * 
   * @param idCategoriaDocumento
   * @return it.csi.solmr.dto.anag.TipoCategoriaDocumentoVO
   * @throws Exception
   */
  public TipoCategoriaDocumentoVO findTipoCategoriaDocumentoByPrimaryKey(
      Long idCategoriaDocumento) throws Exception
  {
    try
    {
      return tipoCategoriaDocumentoDAO
          .findTipoCategoriaDocumentoByPrimaryKey(idCategoriaDocumento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare tutti i dati attivi presenti nella tabella
   * DB_TIPO_DOCUMENTO a partire dall'id_documento_categoria più quelli cessati
   * relativi all'azienda a cui sono collegati i documento
   * 
   * @param idCategoriaDocumento
   * @param idAzienda
   * @param orderBy[]
   * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
   * @throws Exception
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda(
      Long idCategoriaDocumento, Long idAzienda, String orderBy[])
      throws Exception
  {
    try
    {
      return tipoDocumentoDAO
          .getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda(
              idCategoriaDocumento, idAzienda, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di estrarre la data max di esecuzione controlli dei
   * documenti relativi ad una determinata azienda
   * 
   * @param idAzienda
   * @return dataEsecuzione
   * @throws Exception
   */
  public String getDataMaxEsecuzioneDocumento(Long idAzienda)
      throws Exception
  {
    try
    {
      return documentoDAO.getDataMaxEsecuzioneDocumento(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per estrarre i cuaa di tutti i proprietari dei documenti
   * dell'azienda agricola indicata
   * 
   * @param idAzienda
   * @param cuaa
   * @param onlyActive
   * @return java.lnag.String[]
   * @throws Exception
   */
  public String[] getCuaaProprietariDocumentiAzienda(Long idAzienda,
      String cuaa, boolean onlyActive) throws Exception
  {
    try
    {
      return documentoDAO.getCuaaProprietariDocumentiAzienda(idAzienda, cuaa,
          onlyActive);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco delle anomalie relative al documento
   * selezionato
   * 
   * @param idDocumento
   * @param orderBy
   * @return it.csi.solmr.dto.anag.EsitoControlloDocumentoVO[]
   * @throws Exception
   */
  public EsitoControlloDocumentoVO[] getListEsitoControlloDocumentoByIdDocumento(
      Long idDocumento, String[] orderBy) throws Exception
  {
    try
    {
      return esitoControlloDocumentoDAO
          .getListEsitoControlloDocumentoByIdDocumento(idDocumento, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di reperire tutti i documenti di un'azienda in
   * funzione di un determinato id controllo: necessario per la correzione delle
   * anomalie
   * 
   * @param anagAziendaVO
   * @param idControllo
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.DocumentoVO[]
   * @throws Exception
   */
  public DocumentoVO[] getListDocumentiAziendaByIdControllo(
      AnagAziendaVO anagAziendaVO, Long idControllo, boolean onlyActive,
      String[] orderBy) throws Exception
  {
    try
    {
      return documentoDAO.getListDocumentiAziendaByIdControllo(anagAziendaVO,
          idControllo, onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di recuperare l'elenco dei tipi documenti relativi
   * ad un determinato id controllo
   * 
   * @param idControllo
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.TipoDocumentoVO[]
   * @throws Exception
   */
  public TipoDocumentoVO[] getListTipoDocumentoByIdControllo(Long idControllo,
      boolean onlyActive, String orderBy[]) throws Exception
  {
    try
    {
      return tipoDocumentoDAO.getListTipoDocumentoByIdControllo(idControllo,
          onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi permette di reperire tutti i documenti di un'azienda in
   * funzione di un determinato id controllo, particella e tipologia documento:
   * necessario per la correzione delle anomalie
   * 
   * @param anagAziendaVO
   * @param idControllo
   * @param idStoricoParticella
   * @param tipologiaDocumento
   * @param onlyActive
   * @param orderBy
   * @return it.csi.solmr.dto.anag.DocumentoVO[]
   * @throws Exception
   */
  public DocumentoVO[] getListDocumentiAziendaByIdControlloAndParticella(
      AnagAziendaVO anagAziendaVO, Long idControllo, Long idStoricoParticella,
      boolean onlyActive, String[] orderBy) throws Exception
  {
    try
    {
      return documentoDAO.getListDocumentiAziendaByIdControlloAndParticella(
          anagAziendaVO, idControllo, idStoricoParticella, onlyActive, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella(
      AnagAziendaVO anagAziendaVO, Long idControllo, Long idStoricoParticella,
      String annoCampagna, String[] orderBy) throws Exception
  {
    try
    {
      return documentoDAO
          .getListDocumentiPerDichCorrettiveAziendaByIdControlloAndParticella(
              anagAziendaVO, idControllo, idStoricoParticella, annoCampagna,
              orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public DocumentoVO[] getListDocumentiPerDichCorrettiveAziendaByIdControllo(
      AnagAziendaVO anagAziendaVO, Long idControllo, String annoCampagna,
      String[] orderBy) throws Exception
  {
    try
    {
      return documentoDAO
          .getListDocumentiPerDichCorrettiveAziendaByIdControllo(anagAziendaVO,
              idControllo, annoCampagna, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<DocumentoConduzioneVO> getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
      Long idDocumento, Long idConduzioneParticella, boolean onlyActive) throws Exception 
  {
    try
    {
      return documentoDAO
          .getListDocumentoConduzioniByIdDocumentoAndIdConduzioneParticella(
              idDocumento, idConduzioneParticella, onlyActive);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<StoricoParticellaVO> getListParticelleByIdDocumentoAlPianoCorrente(
      Long idDocumento) throws Exception 
  {
    try
    {
      String[] orderBy = new String[] { SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY };
      return storicoParticellaDAO.getListParticelleByIdDocumentoAlPianoCorrente(idDocumento, null, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

}
