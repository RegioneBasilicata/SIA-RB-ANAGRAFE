package it.csi.solmr.business.anag.stampe;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.stampe.ConsistenzaZootecnicaStampa;
import it.csi.smranag.smrgaa.dto.stampe.QuadroDTerreni;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.DocumentoProprietarioVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.DocumentoDAO;
import it.csi.solmr.integration.anag.TipoDocumentoDAO;
import it.csi.solmr.integration.anag.stampe.StampeDAO;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/Stampe",mappedName="comp/env/solmr/anag/Stampe")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class StampeBean implements StampeLocal
{
  /**
   *
   */
  private static final long serialVersionUID = 5267978348006207623L;

  SessionContext sessionContext;

  private transient StampeDAO stampeDAO = null;
  private transient DocumentoDAO documentoDAO = null;
  private transient TipoDocumentoDAO tipoDocumentoDAO = null;
  private transient CommonDAO commonDAO = null;

 

  // ============================================================================
  // Inizializzazione SessionContext e richiamo dell'inizializzazione dei DAO
  // ============================================================================
  @Resource
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
    initializeDAO();
  }

  // ============================================================================
  // Inizializzazione dei DAO con le costanti definite in SolmrCostanrs
  // ============================================================================
  private void initializeDAO() throws EJBException
  {
    try
    {
      stampeDAO = new StampeDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      documentoDAO = new DocumentoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      commonDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoDocumentoDAO = new TipoDocumentoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      throw new EJBException(ex);
    }
  }
  

  public Vector<ParticellaVO> getElencoParticelleQuadroI1(Long idAzienda,Long codFotografia)
    throws SolmrException,Exception
  {
    try
    {
      Vector<ParticellaVO> vParticelle = stampeDAO.getElencoParticelleQuadroI1(idAzienda,codFotografia);
      if(vParticelle != null)
      {
        HashMap<Long, Vector<Long>> hPartFonti = null;
        if(Validator.isNotEmpty(codFotografia))
          hPartFonti = stampeDAO.getFontiParticellaByValidazione(codFotografia);
        else
          hPartFonti = stampeDAO.getFontiParticellaByAzienda(idAzienda);
        
        for(int i=0;i<vParticelle.size();i++)
        {
          vParticelle.get(i).setvIdFonte(hPartFonti.get(vParticelle.get(i).getIdParticella()));
        }
      }
      
      return vParticelle;
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public BigDecimal[] getTotSupQuadroI1CondottaAndAgronomica(Long idAzienda,Long codFotografia)
    throws SolmrException,Exception
  {
    try
    {
      return stampeDAO.getTotSupQuadroI1CondottaAndAgronomica(idAzienda,codFotografia);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public BigDecimal[] getTotSupQuadroI1CatastaleAndGrafica(Long idAzienda,Long codFotografia)
    throws SolmrException,Exception
  {
    try
    {
      return stampeDAO.getTotSupQuadroI1CatastaleAndGrafica(idAzienda, codFotografia);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public Long getCodFotTerreniQuadroI1(Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getCodFotTerreniQuadroI1(idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }


  public Vector<UteVO> getUteQuadroB(Long idAzienda, java.util.Date dataRiferimento)
      throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getUteQuadroB(idAzienda, dataRiferimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public Vector<TipoFormaConduzioneVO> getFormeConduzioneQuadroD() throws SolmrException,
      Exception
  {
    try
    {
      return stampeDAO.getFormeConduzioneQuadroD();
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public Long getFormaConduzioneQuadroD(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getFormaConduzioneQuadroD(idAzienda, dataRiferimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public Vector<CodeDescription> getAttivitaComplementariQuadroE(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getAttivitaComplementariQuadroE(idAzienda,
          dataRiferimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public Long getIdManodoperaQuadroF(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getIdManodoperaQuadroF(idAzienda, dataRiferimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public Vector<Long> getAllevamentiQuadroG(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getAllevamentiQuadroG(idAzienda, dataRiferimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  public Vector<ParticellaVO> getFabbricatiParticelle(Long idFabbricato,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getFabbricatiParticelle(idFabbricato, dataRiferimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<FabbricatoVO> getFabbricati(
      Long idAzienda, java.util.Date dataRiferimento,boolean comunicazione10R) 
      throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getFabbricati(idAzienda, dataRiferimento, comunicazione10R);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<BaseCodeDescription> getTerreniQuadroI4(Long idAzienda, Long codFotografia) 
    throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getTerreniQuadroI4(idAzienda, codFotografia);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  public Vector<BaseCodeDescription> getTerreniQuadroI5(Long idAzienda,java.util.Date dataRiferimento, Long codFotografia) 
    throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getTerreniQuadroI5(idAzienda,dataRiferimento, codFotografia);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  

  public Vector<DocumentoVO> getDocumenti(String idDocumenti[]) throws Exception
  {
    try
    {
      return stampeDAO.getDocumenti(idDocumenti);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che si occupa di reperire tutti i dati del dettaglio del documento.
   * E' analogo al metodo getDettaglioDocumento che si trova in DocumentoBean.
   * L'unica differenza che questo cerca i proprietari e le particelle associate
   * relativamente ad una dichiarazione di consistenza
   *
   * @param idDocumento
   *          Long
   * @return DocumentoVO
   * @throws Exception
   * @throws SolmrException
   */
  public DocumentoVO getDettaglioDocumento(Long idDocumento,
      java.util.Date dataConsistenza, Long idDichiarazioneConsistenza)
      throws Exception, SolmrException
  {
    DocumentoVO documentoVO = null;
    try
    {
      // Recupero i dati da DB_DOCUMENTO
      documentoVO = documentoDAO.findDocumentoVOByPrimaryKey(idDocumento);
      // Recupero i dati da DB_TIPO_DOCUMENTO e da DB_TIPO_TIPOLOGIA_DOCUMENTO
      TipoDocumentoVO tipoDocumentoVO = tipoDocumentoDAO
          .findTipoDocumentoVOByPrimaryKey(documentoVO.getExtIdDocumento());
      String descTipoTipologiaDocumento = commonDAO.getDescriptionFromCode(
          (String) SolmrConstants.get("TAB_TIPO_TIPOLOGIA_DOCUMENTO"), Integer
              .decode(tipoDocumentoVO.getIdTipologiaDocumento().toString()));
      CodeDescription codeTipologiaDocumento = new CodeDescription(Integer
          .decode(tipoDocumentoVO.getIdTipologiaDocumento().toString()),
          descTipoTipologiaDocumento);
      tipoDocumentoVO.setTipoTipologiaDocumento(codeTipologiaDocumento);
      documentoVO.setTipoDocumentoVO(tipoDocumentoVO);
      // Recupero i dati relativi all'utente
      UtenteIrideVO utenteIrideVO = commonDAO.getUtenteIrideById(documentoVO
          .getUtenteUltimoAggiornamento());
      documentoVO.setUtenteAggiornamento(utenteIrideVO);
      if (Validator.isNotEmpty(documentoVO.getTipoDocumentoVO()
          .getFlagAnagTerr())
          && documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
              .equalsIgnoreCase(
                  (String) SolmrConstants
                      .get("FLAG_ANAG_TERR_DOCUMENTI_TERRENI")))
      {
        // Recupero i dati da DB_DOCUMENTO_PROPRIETARIO
        Vector<DocumentoProprietarioVO> elencoProprietari = stampeDAO.getListProprietariDocumento(
            idDocumento, dataConsistenza);
        documentoVO.setElencoProprietari(elencoProprietari);
        Vector<DocumentoConduzioneVO> elencoConduzioni = stampeDAO.getListDocumentoConduzioni(
            idDocumento, dataConsistenza);
        if (elencoConduzioni != null && elencoConduzioni.size() > 0)
        {
          Vector<Object> elencoParticelle = new Vector<Object>();
          for (int i = 0; i < elencoConduzioni.size(); i++)
          {
            DocumentoConduzioneVO documentoConduzioneVO = (DocumentoConduzioneVO) elencoConduzioni
                .elementAt(i);
            // Recupero i dati da DB_DOCUMENTO_CONDUZIONE
            ParticellaVO particellaVO = stampeDAO
                .getParticellaVOByIdConduzioneParticella(documentoConduzioneVO
                    .getIdConduzioneParticella(), idDichiarazioneConsistenza);
            if (particellaVO != null)
              elencoParticelle.add(particellaVO);
          }
          documentoVO.setElencoParticelle(elencoParticelle);
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    catch (SolmrException se)
    {
    }

    return documentoVO;
  }
  
  /***************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * 
   * 
   * inizio metodi usati dalla stampa della comunicazione 10R
   * 
   * 
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   * *************************************************************************************************************************
   */
  
  //===================================================================
  // QUADRO C - Consistenza zootecnica
  //
  // prendere i dati da DB_ALLEVAMENTO
  // ===================================================================
  public Vector<ConsistenzaZootecnicaStampa> getAllevamentiQuadroC10R(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getAllevamentiQuadroC10R(idAzienda, dataRiferimento);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  //===================================================================
  // QUADRO D - Terreni
  //
  // all situazione attuale
  // ===================================================================
  public Vector<QuadroDTerreni> getTerreniQuadroD10R(Long idAzienda) throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getTerreniQuadroD10R(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  //===================================================================
  // QUADRO D - Terreni
  //
  // alla dichiarazione di consistenza
  // ===================================================================
  public Vector<QuadroDTerreni> getTerreniQuadroD10R(java.util.Date dataRiferimento,Long codFotografia) throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getTerreniQuadroD10R(dataRiferimento,codFotografia);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  //===================================================================
  // QUADRO N - Anomalie
  //
  // alla dichiarazione di consistenza
  // ===================================================================
  
  public Vector<String[]> getAnomalie(Long idAzienda, Long idDichiarazioneConsistenza) 
    throws SolmrException, Exception
  {
    try
    {
      return stampeDAO.getAnomalie(idAzienda, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  //===================================================================
  // Quadro O - Documenti
  //
  // Stampa alla situazione attiva
  //
  // Recupero i documenti associati all'azienda
  // ===================================================================
  public Vector<DocumentoVO> getDocumentiStampa(Long idAzienda, Long idDichiarazioneConsistenza,
      String cuaa, java.util.Date dataInserimentoDichiarazione)
    throws SolmrException, Exception
  {
    try
    {
      Vector<DocumentoVO> vDocumenti = null;
      if (idDichiarazioneConsistenza == null)
      {
        vDocumenti = stampeDAO.getDocumentiStampa(idAzienda, cuaa);
      }
      else
      {
        vDocumenti = stampeDAO.getDocumentiStampaDichCons(idAzienda, dataInserimentoDichiarazione);
      }
      
      return vDocumenti;
      
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
}
