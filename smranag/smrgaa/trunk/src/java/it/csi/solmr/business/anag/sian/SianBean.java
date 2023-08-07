package it.csi.solmr.business.anag.sian;

import it.csi.cciaa.albovigneti.DatiUv;
import it.csi.cciaa.albovigneti.UvResponse;
import it.csi.csi.wrapper.SystemException;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.InternalException;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.MacroCU;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.PapuaservLocator;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.PapuaservSoapBindingStub;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.Ruolo;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.sian.ricercaAnagrafica.ISCodifica;
import it.csi.sian.ricercaAnagrafica.ISDatiAnagraficiPro2P;
import it.csi.sian.ricercaAnagrafica.Risposta_RichiestaRispostaSincrona_ricercaAnagraficaAll;
import it.csi.smranag.smrgaa.dto.ws.cciaa.DatiUvCCIAA;
import it.csi.smranag.smrgaa.dto.ws.cciaa.UvResponseCCIAA;
import it.csi.smranag.smrgaa.util.comparator.DatiUvCCIAAComparator;
import it.csi.smrcomms.siapcommws.InvalidParameterException;
import it.csi.smrcomms.siapcommws.InvioPostaCertificata;
import it.csi.smrcomms.siapcommws.InvioPostaCertificataResponse;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.DoubleStringcodeDescription;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.RespAnagFascicoloVO;
import it.csi.solmr.dto.anag.services.SianAnagTributariaGaaVO;
import it.csi.solmr.dto.anag.sian.SianAllevamentiRispostaVO;
import it.csi.solmr.dto.anag.sian.SianAllevamentiVO;
import it.csi.solmr.dto.anag.sian.SianAnagTributariaVO;
import it.csi.solmr.dto.anag.sian.SianCredenzialiVO;
import it.csi.solmr.dto.anag.sian.SianEsitiRicevutiRispostaVO;
import it.csi.solmr.dto.anag.sian.SianEsitoDomandeRispostaVO;
import it.csi.solmr.dto.anag.sian.SianFascicoloResponseVO;
import it.csi.solmr.dto.anag.sian.SianQuoteLatteAziendaRispostaVO;
import it.csi.solmr.dto.anag.sian.SianQuoteLatteAziendaVO;
import it.csi.solmr.dto.anag.sian.SianResponseVO;
import it.csi.solmr.dto.anag.sian.SianTerritorioRispostaVO;
import it.csi.solmr.dto.anag.sian.SianTerritorioVO;
import it.csi.solmr.dto.anag.sian.SianTitoliAggregatiRispostaVO;
import it.csi.solmr.dto.anag.sian.SianTitoliAggregatiVO;
import it.csi.solmr.dto.anag.sian.SianTitoliMovimentatiVO;
import it.csi.solmr.dto.anag.sian.SianTitoloMovimentazioneRispostaVO;
import it.csi.solmr.dto.anag.sian.SianTitoloRispostaVO;
import it.csi.solmr.dto.anag.sian.SianUtenteVO;
import it.csi.solmr.dto.anag.sian.SianUtilizzoTerraVO;
import it.csi.solmr.dto.anag.teramo.ElencoRegistroDiStallaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.exception.services.ServiceSystemException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.ParticellaCertificataDAO;
import it.csi.solmr.integration.anag.SianDAO;
import it.csi.solmr.integration.anag.StoricoParticellaDAO;
import it.csi.solmr.integration.anag.TipoUtilizzoDAO;
import it.csi.solmr.integration.anag.TipoVarietaDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SianConstants;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.TerritoriComparator;
import it.csi.solmr.util.TerritoriIstatComparator;
import it.csi.solmr.util.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;

//Questo bean ha il compito di invocare delle servlet per richiamare i servizi del SIAN
//e restituirne i valori oltre che scrivere sulle tabelle di anagrafe relative ai dati
//SIAN indispensabili per gli altri procedimenti.
@Stateless(name="comp/env/solmr/anag/Sian",mappedName="comp/env/solmr/anag/Sian")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class SianBean implements SianLocal
{

  private static final long                  serialVersionUID                  = 1588964853418059788L;

  SessionContext                             sessionContext;

  private transient SianDAO                  sianDAO                           = null;
  private transient CommonDAO                commonDAO                         = null;
  private transient TipoUtilizzoDAO          tipoUtilizzoDAO                   = null;
  private transient TipoVarietaDAO           tipoVarietaDAO                    = null;
  private transient StoricoParticellaDAO     storicoParticellaDAO              = null;
  private transient ParticellaCertificataDAO particellaCertificataDAO          = null;

  // da verificare quale tempo di timeout impostare..
  private static final int                   CONNECTION_SIAN_BEAN_TIMEOUT      = ((Long) SolmrConstants
                                                                                   .get("CONNECTION_SIAN_BEAN_TIMEOUT"))
                                                                                   .intValue();
  private static final int                   CONNECTION_SIAN_BEAN_DATA_TIMEOUT = ((Long) SolmrConstants
                                                                                   .get("CONNECTION_SIAN_BEAN_DATA_TIMEOUT"))
                                                                                   .intValue();

 
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
      sianDAO = new SianDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      commonDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoUtilizzoDAO = new TipoUtilizzoDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      tipoVarietaDAO = new TipoVarietaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      storicoParticellaDAO = new StoricoParticellaDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      particellaCertificataDAO = new ParticellaCertificataDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }
  
  
  //Nuovo servizio ricarica anagrafica
  public void aggiornaSIANAziendaTributaria(AnagAziendaVO anagAziendaVO,
      Risposta_RichiestaRispostaSincrona_ricercaAnagraficaAll risposta) throws SolmrException, Exception
  {
    try
    {
      SianAnagTributariaVO sianAnagTributariaVO = new SianAnagTributariaVO();
      ISDatiAnagraficiPro2P isDatiAnagraficiPro2P = null;
      // Recupero l'esito dell'operazione
      if (risposta != null)
      {
        ISCodifica iSCodifica = null;
        if (risposta.getRisposta() != null)
        {
          isDatiAnagraficiPro2P = risposta.getRisposta();
        }
        if (risposta.getEsito() != null)
        {
          iSCodifica = risposta.getEsito();
        }
        if (iSCodifica != null)
        {
          if (iSCodifica.getCodice() != null
            && iSCodifica.getCodice().equalsIgnoreCase(SolmrConstants.SIAN_CODICE_SERVIZIO_OK))
          {
            sianAnagTributariaVO.setCuaa(anagAziendaVO.getCUAA().toUpperCase());
            sianAnagTributariaVO.setFlagPresente(SolmrConstants.SIAN_FLAG_PRESENTE_AT);
            insertAziendaTributaria(isDatiAnagraficiPro2P, sianAnagTributariaVO, true);
          }
          else if ((iSCodifica.getCodice() != null)
            && (iSCodifica.getCodice().equalsIgnoreCase(SolmrConstants.SIAN_CUAA_NON_PRESENTE)
             || iSCodifica.getCodice().equalsIgnoreCase(SolmrConstants.SIAN_SOGGETTO_NON_PRESENTE)
             || iSCodifica.getCodice().equalsIgnoreCase(SolmrConstants.SIAN_CUAA_ASSOCIATO_A_PIU_PERSONE)))
          {
            sianAnagTributariaVO.setCuaa(anagAziendaVO.getCUAA()
                .toUpperCase());
            sianAnagTributariaVO.setFlagPresente(SolmrConstants.SIAN_FLAG_NON_PRESENTE_AT);
            sianAnagTributariaVO.setMessaggioErrore(iSCodifica.getDescrizione());
            insertAziendaTributaria(isDatiAnagraficiPro2P, sianAnagTributariaVO, false);
          }
          else
          {
            sianAnagTributariaVO.setCuaa(anagAziendaVO.getCUAA()
                .toUpperCase());
            sianAnagTributariaVO.setFlagPresente(SolmrConstants.SIAN_FLAG_PRESENTE_AT_ERRORE);
            if (iSCodifica.getCodice() != null)
            {
              sianAnagTributariaVO.setMessaggioErrore(iSCodifica.getDescrizione());
            }
            insertAziendaTributaria(isDatiAnagraficiPro2P, sianAnagTributariaVO, false);
          }
        }
      }
      else
      {
        sianAnagTributariaVO.setCuaa(anagAziendaVO.getCUAA().toUpperCase());
        sianAnagTributariaVO.setFlagPresente(SolmrConstants.SIAN_FLAG_PRESENTE_AT_INDISPONIBILE);
        sianAnagTributariaVO.setMessaggioErrore((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
        insertAziendaTributaria(isDatiAnagraficiPro2P, sianAnagTributariaVO, false);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  
  //Nuovo servizio ricerca anagrafica
  private void insertAziendaTributaria(ISDatiAnagraficiPro2P isDatiAnagraficiPro2P,
      SianAnagTributariaVO sianAnagTributariaVO, boolean isPresenteAt)
      throws DataAccessException
  {
    String cuaa=sianAnagTributariaVO.getCuaa().toUpperCase();
    if (isPresenteAt)
    {
      SianAnagTributariaVO sianAnagTributariaOldVO = sianDAO.selectDatiAziendaTributaria(cuaa);
      if(Validator.isNotEmpty(sianAnagTributariaOldVO))
      {
        //cancello eventuali record su db_ateco_sec_tributaria
        sianDAO.deleteAtecoSecTributaria(sianAnagTributariaOldVO.getIdAziendaTributaria());
        // Se anagrafe dettaglio mi ha risposto correttamente vado a cancellare
        // l'eventuale vecchio record e poi inserisco quello nuovo su DB
        // Cancello il vecchio record presente su DB_AZIENDA_TRIBUTARIA
        sianDAO.deleteAziendaTributaria(cuaa);
      }

      sianDAO.insertAziendaTributaria(isDatiAnagraficiPro2P,
          sianAnagTributariaVO);
      
    }
    else
    {
      //L'anagrafe tributaria non ha risposto correttamente quindi vado 
      //solo a modificare il messaggio di errore, il flagpresenteat e la data
      //di aggiornamento se c'era già un record presente... altrimenti 
      //inserisco un nuovo record con questi dati
      if (sianDAO.selectAziendaTributariaValido(cuaa))
      {
        //devo fare l'update
        sianDAO.updateAziendaTributaria(sianAnagTributariaVO);
      }
      else
      {
        SianAnagTributariaVO sianAnagTributariaOldVO = sianDAO.selectDatiAziendaTributaria(cuaa);
        if(Validator.isNotEmpty(sianAnagTributariaOldVO))
        {
          //cancello eventuali record su db_ateco_sec_tributaria
          sianDAO.deleteAtecoSecTributaria(sianAnagTributariaOldVO.getIdAziendaTributaria());
          // Se anagrafe dettaglio mi ha risposto correttamente vado a cancellare
          // l'eventuale vecchio record e poi inserisco quello nuovo su DB
          // Cancello il vecchio record presente su DB_AZIENDA_TRIBUTARIA
          sianDAO.deleteAziendaTributaria(cuaa);
        }
        //devo inserire un record
        sianDAO.insertAziendaTributaria(sianAnagTributariaVO);
      }
    }
  }

  

  /**
   * Metodo che mi restituisce il registro di stalla BDN tramite collegamento
   * con il web-service elenco_RegistriStalla_Cod di Teramo
   * 
   * @param cuaa
   *          String
   * @return Hashtable
   * @throws SolmrException
   * @throws Exception
   */
  public ElencoRegistroDiStallaVO elencoRegistriStalla(String codiceAzienda,
      String codiceSpecie, String cuaa, String tipo, String ordinamento)
      throws SolmrException, Exception
  {
    SolmrLogger
        .info(this, "Invocating elencoRegistriStalla method in SianBean");
    if (cuaa != null)
      cuaa = cuaa.toUpperCase();
    ElencoRegistroDiStallaVO registroStalla = null;
    try
    {
      registroStalla = elencoRegistriStallaTeramo(codiceAzienda, codiceSpecie,
          cuaa, tipo, ordinamento);
      if (registroStalla != null
          && registroStalla.getRegistroDiStalla() != null)
      {
        // Scorro tutti i record restuiti dal servizio per andare a convertire
        // le date
        // e per recuperare informazioni che sono sul nostro DB
        for (int i = 0; i < registroStalla.getRegistroDiStalla().length; i++)
        {

          // Converto DT_NASCITA
          String data = registroStalla.getRegistroDiStalla()[i].getDtNascita();
          if (data != null)
          {
            data = data.substring(0, 10);
            registroStalla.getRegistroDiStalla()[i].setDtNascita(StringUtils
                .parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy",
                    data));
          }

          // Converto DT_INGRESSO
          data = registroStalla.getRegistroDiStalla()[i].getDtIngresso();
          if (data != null)
          {
            data = data.substring(0, 10);
            registroStalla.getRegistroDiStalla()[i].setDtIngresso(StringUtils
                .parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy",
                    data));
          }

          // Converto DATA_MORTE_VENDITA
          data = registroStalla.getRegistroDiStalla()[i].getDataMorteVendita();
          if (data != null)
          {
            data = data.substring(0, 10);
            registroStalla.getRegistroDiStalla()[i]
                .setDataMorteVendita(StringUtils
                    .parseDateFieldToEuropeStandard("yyyy-MM-dd", "dd/MM/yyyy",
                        data));
          }
          // decodifico caricoNascita
          String caricoNascita = registroStalla.getRegistroDiStalla()[i]
              .getCaricoNascita();
          if (caricoNascita != null)
            registroStalla.getRegistroDiStalla()[i].setCaricoNascita(sianDAO
                .getDescCaricoNascita(caricoNascita));

          // decodifico scaricoMorte
          String scaricoMorte = registroStalla.getRegistroDiStalla()[i]
              .getScaricoMorte();
          if (scaricoMorte != null)
            registroStalla.getRegistroDiStalla()[i].setScaricoMorte(sianDAO
                .getDescScaricoMorte(scaricoMorte));

        }
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(
              this,
              "SolmrException during the invocation of elencoRegistriStalla method in SianBean: "
                  + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger
          .fatal(this,
              "Exception during the invocation of elencoRegistriStalla method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_SERVIZIO_BDN_NON_DISPONIBILE"));
    }

    SolmrLogger.debug(this, "Invocated elencoRegistriStalla method in SianBean");
    return registroStalla;
  }


  /**
   * Metodo che mi restituisce l'elenco delle particelle ad una determinata
   * azienda agricola, tramite collegamento con web-service SIAN, ordinate per
   * comune,sezione,foglio, particella e subalterno
   * 
   * @param cuaa
   *          String
   * @return SianTerritorioVO[]
   * @throws SolmrException
   * @throws Exception
   */
  public SianTerritorioVO[] leggiPianoColturale(String cuaa,
      StringBuffer annoCampagna) throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating leggiPianoColturale method in SianBean");
    SianTerritorioVO[] elencoSianTerritorioVO = null;
    PostMethod method = null;
    try
    {
      // Mi connetto alla servlet che si occupa di chiamare il WEB-SERVICE sian
      // "leggiPianoColturale"
      // e mi restituisce l'XML contenente i dati
      HttpClient client = new HttpClient();
      SolmrLogger
          .debug(this,
              "Setting CONNECTION_TIMEOUT in method leggiPianoColturale in SianBean");
      
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method leggiPianoColturale in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method leggiPianoColturale in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "SianLeggiPianoColturaleServlet");
      // Configure the form parameters
      method.addParameter("cuaa", cuaa);
      method
          .addParameter("annoCampagna", DateUtils.getCurrentYear().toString());

      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .fatal(
                this,
                "Throwing SolmrException in leggiPianoColturale method in SianBean for connection bean timeout");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();

        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();

        if (obj != null)
        {
          if (obj instanceof SianTerritorioRispostaVO)
          {
            elencoSianTerritorioVO = ((SianTerritorioRispostaVO) obj)
                .getRisposta();
            SianResponseVO sianResponseVO = ((SianTerritorioRispostaVO) obj)
                .getResponse();
            if (sianResponseVO != null)
            {
              if (sianResponseVO.getCodRet().equalsIgnoreCase(SolmrConstants.SIAN_CODICE_SERVIZIO_OK))
              {
                if (elencoSianTerritorioVO != null
                    && elencoSianTerritorioVO.length > 0)
                {
                  obj = ois.readObject();
                  if (obj instanceof String)
                    annoCampagna.append(obj);
                  // Recupero la descrizioni dei codici che il SIAN non me li
                  // fornisce
                  // Inoltre devo cambiare al volo alcuni istat comune
                  // restituiti dal sian
                  String istatOld = "@@@@@@";
                  try
                  {
                    Arrays.sort(elencoSianTerritorioVO,
                        new TerritoriIstatComparator());

                    for (int i = 0; i < elencoSianTerritorioVO.length; i++)
                    {
                      String istatComune = null;
                      if (elencoSianTerritorioVO[i].getProvincia() != null
                          && elencoSianTerritorioVO[i].getComune() != null)
                      {
                        istatComune = elencoSianTerritorioVO[i].getProvincia()
                            + elencoSianTerritorioVO[i].getComune();
                      }
                      if (istatOld.equals(istatComune))
                      {
                        elencoSianTerritorioVO[i].setComune(istatComune);
                        elencoSianTerritorioVO[i]
                            .setDescComune(elencoSianTerritorioVO[i - 1]
                                .getDescComune());
                        elencoSianTerritorioVO[i]
                            .setSiglaProvincia(elencoSianTerritorioVO[i - 1]
                                .getSiglaProvincia());
                        elencoSianTerritorioVO[i]
                            .setPiemonte(elencoSianTerritorioVO[i - 1]
                                .isPiemonte());
                      }
                      else
                      {
                        if (istatComune != null)
                        {
                          elencoSianTerritorioVO[i].setComune(istatComune);
                          ComuneVO comuneVO = commonDAO
                              .getComuneByISTAT(elencoSianTerritorioVO[i]
                                  .getComune());
                          elencoSianTerritorioVO[i].setDescComune(comuneVO
                              .getDescom());
                          elencoSianTerritorioVO[i].setSiglaProvincia(comuneVO
                              .getSiglaProv());
                          elencoSianTerritorioVO[i].setPiemonte(commonDAO
                              .isComunePiemontese(elencoSianTerritorioVO[i]
                                  .getComune()));
                        }
                      }
                      istatOld = istatComune;

                      SianUtilizzoTerraVO sianUtilizzoTerraVO = elencoSianTerritorioVO[i]
                          .getDestinazione();
                      SolmrLogger
                          .debug(
                              this,
                              "Value of parameter [CODICE_PRODOTTO] from SIAN in method leggiPianoColturale in SianBean: "
                                  + sianUtilizzoTerraVO.getCodiceProdotto()
                                  + "\n");
                      // Recupero l'utilizzo principale in relazione al codice
                      // restituito dal SIAN
                      TipoUtilizzoVO[] elencoTipiUtilizzo = tipoUtilizzoDAO
                          .getListTipiUsoSuoloByCodice(sianUtilizzoTerraVO
                              .getCodiceProdotto(), true, null, null,
                              SolmrConstants.FLAG_S);
                      // Se è stato ritrovato un tipo utilizzo in anagrafe in
                      // funzione
                      // del codice SIAN restituito
                      if (elencoTipiUtilizzo != null
                          && elencoTipiUtilizzo.length > 0)
                      {
                        TipoUtilizzoVO tipoUtilizzoVO = elencoTipiUtilizzo[0];
                        sianUtilizzoTerraVO
                            .setDescCodiceProdotto(tipoUtilizzoVO
                                .getDescrizione());
                        sianUtilizzoTerraVO.setIdProdotto(Integer
                            .decode(tipoUtilizzoVO.getIdUtilizzo().toString()));
                        TipoVarietaVO[] elencoVarieta = tipoVarietaDAO
                            .getListTipoVarietaByIdUtilizzoAndCodice(Long
                                .decode(sianUtilizzoTerraVO.getIdProdotto()
                                    .toString()), sianUtilizzoTerraVO
                                .getCodiceVarieta(), true, null);
                        // Cerco una varietà per il tipo utilizzo riscontrato
                        if (elencoVarieta != null && elencoVarieta.length > 0)
                        {
                          TipoVarietaVO tipoVarieta = elencoVarieta[0];
                          sianUtilizzoTerraVO.setCodiceVarieta(tipoVarieta
                              .getCodiceVarieta());
                          sianUtilizzoTerraVO.setDescCodiceVarieta(tipoVarieta
                              .getDescrizione());
                          sianUtilizzoTerraVO.setIdVarieta(Integer
                              .decode(tipoVarieta.getIdVarieta().toString()));
                        }
                        elencoSianTerritorioVO[i].setImportabile(true);
                      }
                      // Se per il codice prodotto SIAN non sono censiti records
                      // in anagrafe...
                      else
                      {
                        // Se il SIAN restituisce il codice prodotto avviso
                        // l'utente che non è
                        // stato reperito nulla in anagrafe
                        if (Validator.isNotEmpty(sianUtilizzoTerraVO
                            .getCodiceProdotto()))
                        {
                          sianUtilizzoTerraVO
                              .setDescCodiceProdotto("Codice utilizzo non presente in Anagrafe");
                        }
                        elencoSianTerritorioVO[i].setImportabile(false);
                      }

                      if (elencoSianTerritorioVO[i].isImportabile())
                      {
                        // D. Possono essere importate unicamente le particelle
                        // che sono presenti sulla
                        // tabella db_storico_particella oppure su
                        // db_particella_certificata.
                        // Accedere alla tabella
                        // db_storico_particella/db_particella_certificata per:
                        // Comune
                        // Sezione (può essere null)
                        // Foglio
                        // Particella
                        // Subalterno (può essere null)
                        // Data fine validità a nul

                        String sezione = elencoSianTerritorioVO[i].getSezione(), foglio = elencoSianTerritorioVO[i]
                            .getFoglio();
                        String particella = elencoSianTerritorioVO[i]
                            .getParticella(), subalterno = elencoSianTerritorioVO[i]
                            .getSubalterno();

                        if (sezione != null)
                          sezione = sezione.trim();
                        if (foglio != null)
                          foglio = foglio.trim();
                        if (particella != null)
                          particella = particella.trim();
                        if (subalterno != null)
                          subalterno = subalterno.trim();

                        StoricoParticellaVO[] temp = storicoParticellaDAO
                            .getListStoricoParticellaVOByParameters(
                                elencoSianTerritorioVO[i].getComune(), sezione,
                                foglio, particella, subalterno, true, null,
                                null);
                        if (temp == null || temp.length == 0)
                        {
                          // non ho trovato niente su db_storico_particella,
                          // provo con l'altra tabella
                          if (particellaCertificataDAO
                              .findParticellaCertificataByParameters(
                                  elencoSianTerritorioVO[i].getComune(),
                                  sezione, foglio, particella, subalterno,
                                  true, null) == null)
                            elencoSianTerritorioVO[i].setImportabile(false);
                        }
                      }

                      elencoSianTerritorioVO[i]
                          .setDestinazione(sianUtilizzoTerraVO);
                    }
                  }
                  catch (DataAccessException dae)
                  {
                    throw new SolmrException((String) AnagErrors
                        .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
                        + " " + dae.getMessage());
                  }
                  Arrays
                      .sort(elencoSianTerritorioVO, new TerritoriComparator());
                }
                else
                {
                  SolmrLogger
                      .fatal(this,
                          "SolmrException during the invocation of leggiPianoColturale!");
                  throw new SolmrException((String) AnagErrors
                      .get("ERR_SIAN_NO_TERRENI"));
                }
              }
              else
              {
                if (sianResponseVO.getCodRet().equalsIgnoreCase(SolmrConstants.SIAN_CODICE_SERVIZIO_NO_TERRENI))
                {
                  throw new SolmrException((String) AnagErrors
                      .get("ERR_SIAN_NO_TERRENI"));
                }
                else
                {
                  SolmrLogger
                      .fatal(
                          this,
                          "Code of response is different from standard ok in leggiPianoColturale method in SianBean and values: "
                              + sianResponseVO.getCodRet());
                  throw new SolmrException((String) AnagErrors
                      .get("ERR_SERVIZIO_SIAN_ERRATO")
                      + " "
                      + sianResponseVO.getCodRet()
                      + " - "
                      + sianResponseVO.getMsgRet());
                }
              }
            }
            else
            {
              SolmrLogger
                  .fatal(this,
                      "Throwing SolmrException in leggiPianoColturale method in SianBean");
              throw new SolmrException((String) AnagErrors
                  .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
            }
          }
          else
          {
            SolmrLogger
                .fatal(this,
                    "Throwing SolmrException in leggiPianoColturale method in SianBean");
            throw new SolmrException((String) AnagErrors
                .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
          }
        }
        else
        {
          SolmrLogger
              .fatal(this,
                  "Throwing SolmrException in leggiPianoColturale method in SianBean");
          throw new SolmrException((String) AnagErrors
              .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
        }
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(
              this,
              "SolmrException during the invocation of leggiPianoColturale method in SianBean: "
                  + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
            .fatal(
                this,
                "org.apache.commons.httpclient.HttpException during the invocation of leggiPianoColturale method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_TIMEOUT"));
      }
      else
      {
        SolmrLogger
            .fatal(this,
                "IOException during the invocation of leggiPianoColturale method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
            + " " + ie.getClass() + " - " + ie.getMessage());
      }
    }
    catch (ClassNotFoundException cnfe)
    {
      SolmrLogger
          .fatal(
              this,
              "ClassNotFoundException during the invocation of leggiPianoColturale method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + cnfe.getClass() + " - " + cnfe.getMessage());
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .fatal(
              this,
              "IllegalArgumentException during the invocation of leggiPianoColturale method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + iae.getClass() + " - " + iae.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger
          .fatal(this,
              "Exception during the invocation of leggiPianoColturale method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
    }
    finally
    {
      method.releaseConnection();
      SolmrLogger.debug(this,
          "Connection released in method leggiPianoColturale in SianBean");
    }
    SolmrLogger.debug(this, "Invocated leggiPianoColturale method in SianBean");
    return elencoSianTerritorioVO;
  }

  /**
   * Metodo che si collega al web-service SIAN per recuperare l'elenco dei
   * titoli produttore aggregati
   * 
   * @param cuaa
   *          String
   * @param campagna
   *          String
   * @return Vector
   * @throws SolmrException
   */
  public Vector<SianTitoliAggregatiVO> titoliProduttoreAggregati(String cuaa, String campagna)
      throws SolmrException, Exception
  {
    Vector<SianTitoliAggregatiVO> elencoSianTitoliAggregatiVO = null;
    PostMethod method = null;
    SolmrLogger.debug(this,
        "Invocating titoliProduttoreAggregati method in SianBean");
    try
    {
      // Mi connetto alla servlet che si occupa di chiamare il WEB-SERVICE sian
      // "titoliProduttoreAggregati"
      HttpClient client = new HttpClient();
      SolmrLogger
          .debug(this,
              "Setting CONNECTION_TIMEOUT in method titoliProduttoreAggregati in SianBean");
      
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method titoliProduttoreAggregati in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method titoliProduttoreAggregati in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "SianTitoliProdAggServlet");
      // Configure the form parameters
      method.addParameter("cuaa", cuaa);
      if (Validator.isNotEmpty(campagna))
      {
        method.addParameter("campagna", campagna);
      }
      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .fatal(
                this,
                "Throwing SolmrException in titoliProduttoreAggregati method in SianBean for connection bean timeout");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof SianTitoliAggregatiRispostaVO)
          {
            SianTitoliAggregatiVO[] elencoSianTitoli = ((SianTitoliAggregatiRispostaVO) obj)
                .getRisposta();
            SianResponseVO sianResponseVO = ((SianTitoliAggregatiRispostaVO) obj)
                .getResponse();
            if (sianResponseVO != null)
            {
              if (sianResponseVO.getCodRet().equalsIgnoreCase(SolmrConstants.SIAN_CODICE_SERVIZIO_OK))
              {
                if (elencoSianTitoli != null && elencoSianTitoli.length > 0)
                {
                  // Recupero le descrizioni dei codici del SIAN
                  String descrizioneTipoTitolo = null;
                  elencoSianTitoliAggregatiVO = new Vector<SianTitoliAggregatiVO>();
                  for (int i = 0; i < elencoSianTitoli.length; i++)
                  {
                    SianTitoliAggregatiVO sianTitoliAggregatiVO = (SianTitoliAggregatiVO) elencoSianTitoli[i];
                    descrizioneTipoTitolo = commonDAO
                        .getDescriptionSIANFromCode((String) SolmrConstants
                            .get("TAB_SIAN_TIPO_TITOLO"), sianTitoliAggregatiVO
                            .getCodiceTipoTitolo());
                    sianTitoliAggregatiVO
                        .setDescrizioneTipoTitolo(descrizioneTipoTitolo);
                    elencoSianTitoliAggregatiVO.add(sianTitoliAggregatiVO);
                  }
                }
              }
              else
              {
                SolmrLogger
                    .fatal(
                        this,
                        "Code of response is different from standard ok in titoliProduttoreAggregati method in SianBean and values: "
                            + sianResponseVO.getCodRet());
                throw new SolmrException((String) AnagErrors
                    .get("ERR_SERVIZIO_SIAN_ERRATO")
                    + " "
                    + sianResponseVO.getCodRet()
                    + " - "
                    + sianResponseVO.getMsgRet());
              }
            }
            else
            {
              SolmrLogger
                  .fatal(this,
                      "Throwing SolmrException in titoliProduttoreAggregati method in SianBean");
              throw new SolmrException((String) AnagErrors
                  .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
            }
          }
          else
          {
            SolmrLogger
                .fatal(this,
                    "Throwing SolmrException in titoliProduttoreAggregati method in SianBean");
            throw new SolmrException((String) AnagErrors
                .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
          }
        }
        else
        {
          SolmrLogger
              .fatal(this,
                  "Throwing SolmrException in titoliProduttoreAggregati method in SianBean");
          throw new SolmrException((String) AnagErrors
              .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
        }
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(
              this,
              "SolmrException during the invocation of titoliProduttoreAggregati method in SianBean with this message: "
                  + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
            .fatal(
                this,
                "org.apache.commons.httpclient.HttpException during the invocation of titoliProduttoreAggregati method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        SolmrLogger
            .fatal(
                this,
                "IOException during the invocation of titoliProduttoreAggregati method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
            + " " + ie.getClass() + " - " + ie.getMessage());
      }
    }
    catch (ClassNotFoundException cnfe)
    {
      SolmrLogger
          .fatal(
              this,
              "ClassNotFoundException during the invocation of titoliProduttoreAggregati method in SianBean with this message: "
                  + cnfe.getMessage());
      throw new SolmrException((String) AnagErrors
          .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
    }
    catch (DataAccessException dae)
    {
      SolmrLogger
          .fatal(
              this,
              "DataAccessException during the invocation of titoliProduttoreAggregati method in SianBean: "
                  + dae.getMessage());
      throw new SolmrException((String) AnagErrors
          .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .fatal(
              this,
              "IllegalArgumentException during the invocation of titoliProduttoreAggregati method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + iae.getClass() + " - " + iae.getMessage());
    }
    finally
    {
      method.releaseConnection();
      SolmrLogger
          .debug(this,
              "Connection released in method titoliProduttoreAggregati in SianBean");
    }
    SolmrLogger.debug(this,
        "Invocated titoliProduttoreAggregati method in SianBean");
    return elencoSianTitoliAggregatiVO;
  }

  /**
   * Metodo che si collega al web-service SIAN trovaFascicolo
   * 
   * @param cuaa
   *          String
   * @return ISWSToOprResponse
   * @throws SolmrException
   */
  public SianFascicoloResponseVO trovaFascicolo(String cuaa)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating trovaFascicolo method in SianBean");

    SianFascicoloResponseVO sianFascicoloResponseVO = null;
    PostMethod method = null;
    SolmrLogger.debug(this, "Invocating trovaFascicolo method in SianBean");
    try
    {
      // Mi connetto alla servlet che si occupa di chiamare il WEB-SERVICE sian
      // "trovaFascicolo"
      HttpClient client = new HttpClient();
      SolmrLogger.debug(this,
          "Setting CONNECTION_TIMEOUT in method trovaFascicolo in SianBean");
      
      
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method trovaFascicolo in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method trovaFascicolo in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")    		 
          + "SianTrovaFascicoloFS50Servlet");
      // Configure the form parameters
      method.addParameter("cuaa", cuaa);
      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .fatal(
                this,
                "Throwing SolmrException in trovaFascicolo method in SianBean for connection bean timeout");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof SianFascicoloResponseVO)
            sianFascicoloResponseVO = (SianFascicoloResponseVO) obj;
          else
          {
            SolmrLogger.fatal(this,
                "Throwing SolmrException in trovaFascicolo method in SianBean");
            throw new SolmrException((String) AnagErrors
                .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
          }
        }
        else
        {
          SolmrLogger.fatal(this,
              "Throwing SolmrException in trovaFascicolo method in SianBean");
          throw new SolmrException((String) AnagErrors
              .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
        }
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(
              this,
              "SolmrException during the invocation of trovaFascicolo method in SianBean with this message: "
                  + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
            .fatal(
                this,
                "org.apache.commons.httpclient.HttpException during the invocation of trovaFascicolo method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        SolmrLogger
            .fatal(this,
                "IOException during the invocation of trovaFascicolo method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
            + " " + ie.getClass() + " - " + ie.getMessage());
      }
    }
    catch (ClassNotFoundException cnfe)
    {
      SolmrLogger
          .fatal(
              this,
              "ClassNotFoundException during the invocation of trovaFascicolo method in SianBean with this message: "
                  + cnfe.getMessage());
      throw new SolmrException((String) AnagErrors
          .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .fatal(
              this,
              "IllegalArgumentException during the invocation of trovaFascicolo method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + iae.getClass() + " - " + iae.getMessage());
    }
    finally
    {
      method.releaseConnection();
      SolmrLogger.debug(this,
          "Connection released in method trovaFascicolo in SianBean");
    }
    SolmrLogger.debug(this, "Invocated trovaFascicolo method in SianBean");
    
    return sianFascicoloResponseVO;
  }

  

  /**
   * Metodo che richiama il web-service SIAN per la visualizzazione delle quote
   * latte
   * 
   * @param cuaa
   *          String
   * @param campagna
   *          String
   * @return Vector
   * @throws SolmrException
   * @throws Exception
   */
  public Vector<SianQuoteLatteAziendaVO> quoteLatte(String cuaa, String campagna) throws SolmrException,
      Exception
  {
    Vector<SianQuoteLatteAziendaVO> elencoQuoteLatteAziendaVO = null;
    SolmrLogger.debug(this, "Invocating quoteLatte method in SianBean");
    PostMethod method = null;
    try
    {
      // Mi connetto alla servlet che si occupa di chiamare il WEB-SERVICE sian
      // "quoteLatte"
      HttpClient client = new HttpClient();
      SolmrLogger.debug(this,
          "Setting CONNECTION_TIMEOUT in method quoteLatte in SianBean");
      
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method quoteLatte in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method quoteLatte in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "SianQuoteLatteServlet");
      // Configure the form parameters
      method.addParameter("cuaa", cuaa);
      if (Validator.isNotEmpty(campagna))
      {
        method.addParameter("campagna", campagna);
      }
      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .fatal(
                this,
                "Throwing SolmrException in quoteLatte method in SianBean for connection bean timeout");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();

        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof SianQuoteLatteAziendaRispostaVO)
          {
            SianQuoteLatteAziendaVO[] elencoSianQuoteLatteAziendaVO = ((SianQuoteLatteAziendaRispostaVO) obj)
                .getRisposta();
            SianResponseVO sianResponseVO = ((SianQuoteLatteAziendaRispostaVO) obj)
                .getResponse();
            if (sianResponseVO != null)
            {
              if (sianResponseVO.getCodRet().equalsIgnoreCase(SolmrConstants.SIAN_CODICE_SERVIZIO_OK)
                  ||
                  sianResponseVO.getCodRet().equalsIgnoreCase(SolmrConstants.SIAN_CODICE_SERVIZIO_NO_TERRENI)
              )
              {
                if (elencoSianQuoteLatteAziendaVO != null
                    && elencoSianQuoteLatteAziendaVO.length > 0)
                {
                  elencoQuoteLatteAziendaVO = new Vector<SianQuoteLatteAziendaVO>();
                  for (int i = 0; i < elencoSianQuoteLatteAziendaVO.length; i++)
                  {
                    SianQuoteLatteAziendaVO quoteLatteAziendaVO = (SianQuoteLatteAziendaVO) elencoSianQuoteLatteAziendaVO[i];
                    ComuneVO comuneVO = commonDAO
                        .getComuneByISTAT(quoteLatteAziendaVO.getUte());
                    quoteLatteAziendaVO.setComuneUteVO(comuneVO);
                    elencoQuoteLatteAziendaVO.add(quoteLatteAziendaVO);
                  }
                }
              }
              else
              {
                SolmrLogger
                    .fatal(
                        this,
                        "Code of response is different from standard ok in quoteLatte method in SianBean and values: "
                            + sianResponseVO.getCodRet());
                throw new SolmrException((String) AnagErrors
                    .get("ERR_SERVIZIO_SIAN_ERRATO")
                    + " "
                    + sianResponseVO.getCodRet()
                    + " - "
                    + sianResponseVO.getMsgRet());
              }
            }
            else
            {
              SolmrLogger.fatal(this,
                  "Throwing SolmrException in quoteLatte method in SianBean");
              throw new SolmrException((String) AnagErrors
                  .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
            }
          }
          else
          {
            SolmrLogger.fatal(this,
                "Throwing SolmrException in quoteLatte method in SianBean");
            throw new SolmrException((String) AnagErrors
                .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
          }
        }
        else
        {
          SolmrLogger.fatal(this,
              "Throwing SolmrException in quoteLatte method in SianBean");
          throw new SolmrException((String) AnagErrors
              .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
        }
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(
              this,
              "SolmrException during the invocation of quoteLatte method in SianBean with this message: "
                  + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
            .fatal(
                this,
                "org.apache.commons.httpclient.HttpException during the invocation of quoteLatte method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        SolmrLogger
            .fatal(this,
                "IOException during the invocation of quoteLatte method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
            + " " + ie.getClass() + " - " + ie.getMessage());
      }
    }
    catch (ClassNotFoundException cnfe)
    {
      SolmrLogger
          .fatal(
              this,
              "ClassNotFoundException during the invocation of quoteLatte method in SianBean with this message: "
                  + cnfe.getMessage());
      throw new SolmrException((String) AnagErrors
          .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
    }
    catch (DataAccessException dae)
    {
      SolmrLogger
          .fatal(
              this,
              "DataAccessException during the invocation of quoteLatte method in SianBean with this message: "
                  + dae.getMessage());
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + dae.getClass() + " - " + dae.getMessage());
    }
    catch (NotFoundException nfe)
    {
      SolmrLogger
          .fatal(
              this,
              "NotFoundException during the invocation of quoteLatte method in SianBean with this message: "
                  + nfe.getMessage());
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + nfe.getClass() + " - " + nfe.getMessage());
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .fatal(
              this,
              "IllegalArgumentException during the invocation of quoteLatte method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + iae.getClass() + " - " + iae.getMessage());
    }
    finally
    {
      method.releaseConnection();
      SolmrLogger.debug(this,
          "Connection released in method quoteLatte in SianBean");
    }
    SolmrLogger.debug(this, "Invocated quoteLatte method in SianBean");
    return elencoQuoteLatteAziendaVO;
  }

  /**
   * Metodo per recuperare l'oggetto per decodificare la specie animale
   * proveniente dal SIAN in relazione al codice specie
   * 
   * @param codiceSpecie
   *          String
   * @return StringcodeDescription
   * @throws Exception
   * @throws SolmrException
   */
  public StringcodeDescription getSianTipoSpecieByCodiceSpecie(
      String codiceSpecie) throws Exception, SolmrException
  {
    try
    {
      return sianDAO.getSianTipoSpecieByCodiceSpecie(codiceSpecie);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo per recuperare l'oggetto per decodificare la specie animale
   * proveniente dal SIAN in relazione al idTipoSpecieAnimale
   * 
   * @param idTipoSpecieAnimale
   *          long
   * @return StringcodeDescription
   * @throws DataAccessException
   */
  public StringcodeDescription getSianTipoSpecieByIdSpecieAnimale(
      long idTipoSpecieAnimale) throws Exception
  {
    try
    {
      return sianDAO.getSianTipoSpecieByIdSpecieAnimale(idTipoSpecieAnimale);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi consente, attraverso il richiamo del web-service SIAN di
   * recuperare i titoli produttore
   * 
   * @param CUAA
   *          String
   * @param campagna
   *          String
   * @return SianTitoloRispostaVO
   * @throws SolmrException
   * @throws Exception
   */
  public SianTitoloRispostaVO titoliProduttore(String CUAA, String campagna)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating titoliProduttore method in SianBean");
    SianTitoloRispostaVO sianTitoloRispostaVO = null;
    PostMethod method = null;
    try
    {
      HttpClient client = new HttpClient();
      SolmrLogger.debug(this,
          "Setting CONNECTION_TIMEOUT in method titoliProduttore in SianBean");
      
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      
      
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method titoliProduttore in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method titoliProduttore in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "SianTitoliProdServlet");
      // Configure the form parameters
      method.addParameter("cuaa", CUAA);
      if (Validator.isNotEmpty(campagna))
      {
        method.addParameter("campagna", campagna);
      }
      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .error(
                this,
                "Throwing SolmrException in titoliProduttore method in SianBean for connection bean timeout");
        throw new SolmrException(
            AnagErrors.ERRORE_SERVIZIO_SIAN_NON_DISPONIBILE);
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof SianTitoloRispostaVO)
          {
            sianTitoloRispostaVO = (SianTitoloRispostaVO) obj;
          }
          else
          {
            throw (Exception) obj;
          }
        }
        else
          return null;
      }
    }
    catch (ClassNotFoundException cnfe)
    {
      SolmrLogger
          .error(
              this,
              "ClassNotFoundException during the invocation of titoliProduttore method in SianBean");
      throw new SolmrException(
          AnagErrors.ERRORE_GENERIC_CONNECTION_EXCEPTION_SIAN + " "
              + cnfe.getMessage());
    }
    catch (IOException ie)
    {
      SolmrLogger
          .error(this,
              "IOException during the invocation of titoliProduttore method in SianBean");
      throw new SolmrException(
          AnagErrors.ERRORE_GENERIC_CONNECTION_EXCEPTION_SIAN + " "
              + ie.getMessage());
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .error(
              this,
              "IllegalArgumentException during the invocation of titoliProduttore method in SianBean");
      throw new SolmrException(
          AnagErrors.ERRORE_GENERIC_CONNECTION_EXCEPTION_SIAN + " "
              + iae.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger
          .error(this,
              "Exception during the invocation of titoliProduttore method in SianBean");
      throw new Exception(e.getMessage());
    }
    finally
    {
      method.releaseConnection();
      SolmrLogger.debug(this,
          "Connection released in method titoliProduttore in SianBean");
    }
    SolmrLogger.debug(this, "Invocated titoliProduttore method in SianBean");
    return sianTitoloRispostaVO;
  }

  
  
  
  
  /**
   * Metodo che mi consente, attraverso il richiamo del web-service SIAN di
   * recuperare i titoli produttorecon info pegni
   * 
   * @param CUAA String
   * @return SianTitoloRispostaVO
   * @throws SolmrException
   * @throws Exception
   */
  public SianTitoloRispostaVO titoliProduttoreConInfoPegni(String CUAA)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating titoliProduttoreConInfoPegni method in SianBean");
    SianTitoloRispostaVO sianTitoloRispostaVO = null;
    PostMethod method = null;
    try
    {
      HttpClient client = new HttpClient();
      SolmrLogger.debug(this,
          "Setting CONNECTION_TIMEOUT in method titoliProduttoreConInfoPegni in SianBean");
      
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method titoliProduttoreConInfoPegni in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method titoliProduttoreConInfoPegni in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "SianTitoliProdInfoPegniServlet");
      // Configure the form parameters
      method.addParameter("cuaa", CUAA);

      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .error(
                this,
                "Throwing SolmrException in titoliProduttoreConInfoPegni method in SianBean for connection bean timeout");
        throw new SolmrException(
            AnagErrors.ERRORE_SERVIZIO_SIAN_NON_DISPONIBILE);
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof SianTitoloRispostaVO)
          {
            sianTitoloRispostaVO = (SianTitoloRispostaVO) obj;
          }
          else
          {
            throw (SolmrException) obj;
          }
        }
        else
          return null;
      }
    }
    catch (ClassNotFoundException cnfe)
    {
      SolmrLogger
          .error(
              this,
              "ClassNotFoundException during the invocation of titoliProduttoreConInfoPegni method in SianBean");
      throw new SolmrException(
          AnagErrors.ERRORE_GENERIC_CONNECTION_EXCEPTION_SIAN + " "
              + cnfe.getMessage());
    }
    catch (IOException ie)
    {
      SolmrLogger
          .error(this,
              "IOException during the invocation of titoliProduttoreConInfoPegni method in SianBean");
      throw new SolmrException(
          AnagErrors.ERRORE_GENERIC_CONNECTION_EXCEPTION_SIAN + " "
              + ie.getMessage());
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .error(
              this,
              "IllegalArgumentException during the invocation of titoliProduttoreConInfoPegni method in SianBean");
      throw new SolmrException(
          AnagErrors.ERRORE_GENERIC_CONNECTION_EXCEPTION_SIAN + " "
              + iae.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger
          .error(this,
              "Exception during the invocation of titoliProduttoreConInfoPegni method in SianBean");
      throw new Exception(e.getMessage());
    }
    finally
    {
      method.releaseConnection();
      SolmrLogger.debug(this,
          "Connection released in method titoliProduttoreConInfoPegni in SianBean");
    }
    SolmrLogger.debug(this, "Invocated titoliProduttoreConInfoPegni method in SianBean");
    return sianTitoloRispostaVO;
  }
  
  
  /**
   * Method serviceSianTitoliProduttoreConInfoPegni
   * 
   * @param CUAA String
   * @return SianTitoloRispostaVO
   * @throws SolmrException
   * @throws ServiceSystemException
   * @throws SystemException
   * @throws Exception
   */
  public SianTitoloRispostaVO serviceSianTitoliProduttoreConInfoPegni(String CUAA) 
    throws SolmrException, ServiceSystemException,SystemException, Exception
  {
    SolmrLogger.debug(this,
        "Invocating serviceSianTitoliProduttoreConInfoPegni method in SianBean");
    SianTitoloRispostaVO sianTitoloRispostaVO = null;
    PostMethod method = null;
    if ("".equals(CUAA) || CUAA == null)
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);

    try
    {
      HttpClient client = new HttpClient();
      SolmrLogger
          .debug(this,
              "Setting CONNECTION_TIMEOUT in method serviceSianTitoliProduttoreConInfoPegni in SianBean");
      
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);      
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method serviceSianTitoliProduttoreConInfoPegni in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method serviceSianTitoliProduttoreConInfoPegni in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "SianTitoliProdInfoPegniServlet");
      // Configure the form parameters
      method.addParameter("cuaa", CUAA);

      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .fatal(
                this,
                "Throwing ServiceSystemException in serviceSianTitoliProduttoreConInfoPegni method in SianBean for connection bean timeout");
        throw new ServiceSystemException(ErrorTypes.STR_SERVICE_SIAN_EXCEPTION,
            ErrorTypes.SERVICE_SIAN_EXCEPTION);
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();

        ObjectInputStream ois = new ObjectInputStream(is);

        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof SianTitoloRispostaVO)
          {
            sianTitoloRispostaVO = (SianTitoloRispostaVO) obj;
          }
          else
          {
            throw (SolmrException) obj;
          }
        }
        else
          return null;
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(
              this,
              "SolmrException during the invocation of serviceSianTitoliProduttoreConInfoPegni method in SianBean");
      throw new ServiceSystemException(ErrorTypes.STR_SERVICE_SIAN_EXCEPTION,
          ErrorTypes.SERVICE_SIAN_EXCEPTION);
    }
    catch (ClassNotFoundException cnfe)
    {
      SolmrLogger
          .fatal(
              this,
              "ClassNotFoundException during the invocation of serviceSianTitoliProduttoreConInfoPegni method in SianBean");
      throw new ServiceSystemException(ErrorTypes.STR_SERVICE_SIAN_EXCEPTION,
          ErrorTypes.SERVICE_SIAN_EXCEPTION);
    }
    catch (IOException ie)
    {
      SolmrLogger
          .fatal(
              this,
              "IOException during the invocation of serviceSianTitoliProduttoreConInfoPegni method in SianBean");
      throw new ServiceSystemException(ErrorTypes.STR_SERVICE_SIAN_EXCEPTION,
          ErrorTypes.SERVICE_SIAN_EXCEPTION);
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .fatal(
              this,
              "IllegalArgumentException during the invocation of serviceSianTitoliProduttoreConInfoPegni method in SianBean");
      throw new ServiceSystemException(ErrorTypes.STR_SERVICE_SIAN_EXCEPTION,
          ErrorTypes.SERVICE_SIAN_EXCEPTION);
    }
    catch (Exception e)
    {
      SolmrLogger
          .fatal(
              this,
              "Exception during the invocation of serviceSianTitoliProduttoreConInfoPegni method in SianBean");
      throw new ServiceSystemException(ErrorTypes.STR_SERVICE_SIAN_EXCEPTION,
          ErrorTypes.SERVICE_SIAN_EXCEPTION);
    }
    finally
    {
      method.releaseConnection();
      SolmrLogger
          .debug(this,
              "Connection released in method serviceSianTitoliProduttoreConInfoPegni in SianBean");
    }
    SolmrLogger.debug(this,
        "Invocated serviceSianTitoliProduttoreConInfoPegni method in SianBean");
    return sianTitoloRispostaVO;
  }  
  
  
  //Nuovo metodo pe ril nuovo servizio ricerca anagrafica
  public Boolean serviceSianAggiornaDatiTributaria(String cuaa, SianUtenteVO sianUtenteVO)
      throws ServiceSystemException, SystemException, Exception,
      SolmrException
  {
    SolmrLogger.debug(this, "Invocating  serviceSianAggiornaDatiTributaria ");
    SolmrLogger.debug(this, "cuaa " + cuaa);
    SolmrLogger.debug(this, "\n\n\n\n\n\n\n");
   
    Risposta_RichiestaRispostaSincrona_ricercaAnagraficaAll  risposta = null;
    try
    {
      // controllare se c'è un record su DB_AZIENDA_TRIBUTARIA
      // inserito da un numero di giorni non superiori a quelli
      // indicati in DB_PARAMETRO per codice=GGAT.
      if (sianDAO.selectAziendaTributaria(cuaa))
        return new Boolean(true);
      
      //cerco il codice fiscale in base al ruolo
      SianCredenzialiVO sianCredenzialiVO = new SianCredenzialiVO();
      if(sianUtenteVO != null)
      {
        sianCredenzialiVO.setUtenteCF(getCFForSian(sianUtenteVO));
      }
      
      //iSWSReturnDett = anagraficaDettaglio(cuaa, sianCredenzialiVO);
      risposta = ricercaAnagrafica(cuaa, sianCredenzialiVO);
      if((risposta != null) && (risposta.getRisposta() != null)
          && (risposta.getRisposta().getSegnalazione() != null))
      {
        SolmrLogger.debug(this, "\n\n\n\n\n\n\n");
        SolmrLogger.debug(this, "risposta.getRisposta().getSegnalazione().getEsito().getCodice() "
            + risposta.getRisposta().getSegnalazione().getEsito().getCodice());
        SolmrLogger.debug(this, "risposta.getRisposta().getSegnalazione().getEsito().getDescrizione() "
            + risposta.getRisposta().getSegnalazione().getEsito().getDescrizione());
        SolmrLogger.debug(this, "\n\n\n\n\n\n\n");
      }
    }
    catch (Exception e)
    {
      // Non faccio nulla. iSWSReturnDett resta null e il metodo
      // aggiornaSIANAziendaTributaria() lo gestisce scrivendo un record
      // appropriato su db.
    }
    try
    {
      AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
      anagAziendaVO.setCUAA(cuaa);
      aggiornaSIANAziendaTributaria(anagAziendaVO, risposta);
    }   
    catch (Exception e)
    {
      throw new ServiceSystemException(ErrorTypes.STR_SERVICE_SIAN_EXCEPTION,
          ErrorTypes.SERVICE_SIAN_EXCEPTION);
    }
    return new Boolean(true);
  }
  
  
  
  
  

  public SianTitoloMovimentazioneRispostaVO serviceSianMovimentazioneTitoli(
      String cuaa, String idDocumento, String campagna, String fattispecie,
      String cedente, SianTitoliMovimentatiVO[] elencoTitoli)
      throws SolmrException, ServiceSystemException, SystemException,
      Exception
  {
    SolmrLogger.debug(this,
        "Invocating serviceSianMovimentazioneTitoli method in SianBean");
    SianTitoloMovimentazioneRispostaVO sianTitoloMovimentazioneRispostaVO = null;
    PostMethod method = null;
    try
    {
      // Mi connetto alla servlet che si occupa di chiamare il WEB-SERVICE sian
      // "movimentazione titoli"
      // e mi restituisce l'XML contenente i dati
      HttpClient client = new HttpClient();

      SolmrLogger
          .debug(
              this,
              "Setting CONNECTION_TIMEOUT in method serviceSianMovimentazioneTitoli in SianBean");
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method serviceSianMovimentazioneTitoli in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method serviceSianMovimentazioneTitoli in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "SianMovimentazioneTitoliServlet");

      // Configure the form parameters
      if (cuaa != null)
        method.addParameter("cuaa", cuaa);
      if (idDocumento != null)
        method.addParameter("idDocumento", idDocumento);
      if (campagna != null)
        method.addParameter("campagna", campagna);
      if (fattispecie != null)
        method.addParameter("fattispecie", fattispecie);
      if (cedente != null)
        method.addParameter("cedente", cedente);

      if (elencoTitoli != null)
      {
        method.addParameter("arraySize", "" + elencoTitoli.length);
        int i = 0;
        int z = elencoTitoli.length;
        while (i < z)
        {
          SianTitoliMovimentatiVO sianTitoliMovimentati = (SianTitoliMovimentatiVO) elencoTitoli[i];
          if (sianTitoliMovimentati.getPrimoIdentificativo() != null)
            method.addParameter("paramA" + i, sianTitoliMovimentati
                .getPrimoIdentificativo());
          if (sianTitoliMovimentati.getUltimoIdentificativo() != null)
            method.addParameter("paramB" + i, sianTitoliMovimentati
                .getUltimoIdentificativo());
          if (sianTitoliMovimentati.getSuperficieUba() != null)
            method.addParameter("paramC" + i, sianTitoliMovimentati
                .getSuperficieUba());
          if (sianTitoliMovimentati.getDataScadenzaAffitto() != null)
            method.addParameter("paramD" + i, sianTitoliMovimentati
                .getDataScadenzaAffitto());
          if (sianTitoliMovimentati.getCuaaTerzoSoggetto() != null)
            method.addParameter("paramE" + i, sianTitoliMovimentati
                .getCuaaTerzoSoggetto());
          i++;
        }
      }

      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .fatal(
                this,
                "Throwing SolmrException in serviceSianMovimentazioneTitoli method in SianBean for connection bean timeout");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof SianTitoloMovimentazioneRispostaVO)
          {
            sianTitoloMovimentazioneRispostaVO = (SianTitoloMovimentazioneRispostaVO) obj;
          }
          else
          {
            SolmrLogger
                .fatal(this,
                    "Throwing SolmrException in serviceSianMovimentazioneTitoli method in SianBean");
            throw new SolmrException((String) AnagErrors
                .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
          }
        }
        else
        {
          SolmrLogger
              .fatal(this,
                  "Throwing SolmrException in serviceSianMovimentazioneTitoli method in SianBean");
          throw new SolmrException((String) AnagErrors
              .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
        }
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(
              this,
              "SolmrException during the invocation of serviceSianMovimentazioneTitoli method in SianBean");
      se.printStackTrace();
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
            .fatal(
                this,
                "org.apache.commons.httpclient.HttpException during the invocation of serviceSianMovimentazioneTitoli method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        SolmrLogger
            .fatal(
                this,
                "IOException during the invocation of serviceSianMovimentazioneTitoli method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
            + " " + ie.getClass() + " - " + ie.getMessage());
      }
    }
    catch (ClassNotFoundException cnfe)
    {
      SolmrLogger
          .fatal(
              this,
              "ClassNotFoundException during the invocation of serviceSianMovimentazioneTitoli method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + cnfe.getClass() + " - " + cnfe.getMessage());
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .fatal(
              this,
              "IllegalArgumentException during the invocation of serviceSianMovimentazioneTitoli method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + iae.getClass() + " - " + iae.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger
          .fatal(
              this,
              "Exception during the invocation of serviceSianMovimentazioneTitoli method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
    }
    finally
    {
      method.releaseConnection();
      SolmrLogger
          .debug(this,
              "Connection released in method serviceSianMovimentazioneTitoli in SianBean");
    }
    SolmrLogger.debug(this,
        "Invocated serviceSianMovimentazioneTitoli method in SianBean");
    return sianTitoloMovimentazioneRispostaVO;
  }

  public SianEsitiRicevutiRispostaVO serviceSianEsitiRicevuti(String ultimoEsito)
      throws SolmrException, ServiceSystemException, SystemException,
      Exception
  {
    SolmrLogger.debug(this,
        "Invocating serviceSianEsitiRicevuti method in SianBean");
    SianEsitiRicevutiRispostaVO sianEsitiRicevutiRispostaVO = null;
    PostMethod method = null;
    try
    {
      // Mi connetto alla servlet che si occupa di chiamare il WEB-SERVICE sian
      // "esiti ricevuti"
      // e mi restituisce l'XML contenente i dati
      HttpClient client = new HttpClient();

      SolmrLogger
          .debug(this,
              "Setting CONNECTION_TIMEOUT in method serviceSianEsitiRicevuti in SianBean");
      
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method serviceSianEsitiRicevuti in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method serviceSianEsitiRicevuti in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "SianEsitiRicevutiServlet");

      // Configure the form parameters

      method.addParameter("ultimoEsito", ultimoEsito);

      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .fatal(
                this,
                "Throwing SolmrException in serviceSianEsitiRicevuti method in SianBean for connection bean timeout");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof SianEsitiRicevutiRispostaVO)
          {
            sianEsitiRicevutiRispostaVO = (SianEsitiRicevutiRispostaVO) obj;
          }
          else
          {
            SolmrLogger
                .fatal(this,
                    "Throwing SolmrException in serviceSianEsitiRicevuti method in SianBean");
            throw new SolmrException((String) AnagErrors
                .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
          }
        }
        else
        {
          SolmrLogger
              .fatal(this,
                  "Throwing SolmrException in serviceSianEsitiRicevuti method in SianBean");
          throw new SolmrException((String) AnagErrors
              .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
        }
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(
              this,
              "SolmrException during the invocation of serviceSianEsitiRicevuti method in SianBean");
      se.printStackTrace();
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
            .fatal(
                this,
                "org.apache.commons.httpclient.HttpException during the invocation of serviceSianEsitiRicevuti method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        SolmrLogger
            .fatal(
                this,
                "IOException during the invocation of serviceSianEsitiRicevuti method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
            + " " + ie.getClass() + " - " + ie.getMessage());
      }
    }
    catch (ClassNotFoundException cnfe)
    {
      SolmrLogger
          .fatal(
              this,
              "ClassNotFoundException during the invocation of serviceSianEsitiRicevuti method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + cnfe.getClass() + " - " + cnfe.getMessage());
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .fatal(
              this,
              "IllegalArgumentException during the invocation of serviceSianEsitiRicevuti method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + iae.getClass() + " - " + iae.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger
          .fatal(
              this,
              "Exception during the invocation of serviceSianEsitiRicevuti method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
    }
    finally
    {
      method.releaseConnection();
      SolmrLogger.debug(this,
          "Connection released in method serviceSianEsitiRicevuti in SianBean");
    }
    SolmrLogger.debug(this,
        "Invocated serviceSianEsitiRicevuti method in SianBean");
    return sianEsitiRicevutiRispostaVO;
  }

  public SianEsitoDomandeRispostaVO serviceSianEsitoDomande()
      throws SolmrException, ServiceSystemException, SystemException,
      Exception
  {
    SolmrLogger.debug(this,
        "Invocating serviceSianEsitoDomande method in SianBean");
    SianEsitoDomandeRispostaVO sianEsitoDomandeRispostaVO = null;
    PostMethod method = null;
    try
    {
      // Mi connetto alla servlet che si occupa di chiamare il WEB-SERVICE sian
      // "esito domande"
      // e mi restituisce l'XML contenente i dati
      HttpClient client = new HttpClient();

      SolmrLogger
          .debug(this,
              "Setting CONNECTION_TIMEOUT in method serviceSianEsitoDomande in SianBean");
      
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method serviceSianEsitoDomande in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method serviceSianEsitoDomande in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "SianEsitoDomandeServlet");

      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .fatal(
                this,
                "Throwing SolmrException in serviceSianEsitoDomande method in SianBean for connection bean timeout");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof SianEsitoDomandeRispostaVO)
          {
            sianEsitoDomandeRispostaVO = (SianEsitoDomandeRispostaVO) obj;
          }
          else
          {
            SolmrLogger
                .fatal(this,
                    "Throwing SolmrException in serviceSianEsitoDomande method in SianBean");
            throw new SolmrException((String) AnagErrors
                .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
          }
        }
        else
        {
          SolmrLogger
              .fatal(this,
                  "Throwing SolmrException in serviceSianEsitoDomande method in SianBean");
          throw new SolmrException((String) AnagErrors
              .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
        }
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(
              this,
              "SolmrException during the invocation of serviceSianEsitoDomande method in SianBean");
      se.printStackTrace();
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
            .fatal(
                this,
                "org.apache.commons.httpclient.HttpException during the invocation of serviceSianEsitoDomande method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        SolmrLogger
            .fatal(
                this,
                "IOException during the invocation of serviceSianEsitoDomande method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
            + " " + ie.getClass() + " - " + ie.getMessage());
      }
    }
    catch (ClassNotFoundException cnfe)
    {
      SolmrLogger
          .fatal(
              this,
              "ClassNotFoundException during the invocation of serviceSianEsitoDomande method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + cnfe.getClass() + " - " + cnfe.getMessage());
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .fatal(
              this,
              "IllegalArgumentException during the invocation of serviceSianEsitoDomande method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + iae.getClass() + " - " + iae.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger
          .fatal(this,
              "Exception during the invocation of serviceSianEsitoDomande method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
    }
    finally
    {
      method.releaseConnection();
      SolmrLogger.debug(this,
          "Connection released in method serviceSianEsitoDomande in SianBean");
    }
    SolmrLogger.debug(this,
        "Invocated serviceSianEsitoDomande method in SianBean");
    return sianEsitoDomandeRispostaVO;
  }

  // Servizi utilizzati da smrgasv STOP
  //vecchi servizi
  /*public SianAnagDettaglioVO anagraficaDettaglioFromDB(String cuaa, SianUtenteVO sianUtenteVO)
    throws SolmrException, Exception
  {
    return anagraficaDettaglioFromDB(cuaa,false,false, sianUtenteVO);
  } */


  /*public SianAnagDettaglioVO anagraficaDettaglioFromDB(String cuaa,boolean orderSocRapp,boolean storicoRappLeg, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception
  {
    try
    {
      SolmrLogger.debug(this, "Invocating  anagraficaDettaglioFromDB ");
      SolmrLogger.debug(this, "cuaa " + cuaa);
      SolmrLogger.debug(this, "\n\n\n\n\n\n\n");
      String elenco[] = new String[1];
      elenco[0] = cuaa;
      sianAggiornaDatiTributaria(elenco, sianUtenteVO);
      SianAnagDettaglioVO anagDettaglio = sianDAO
          .selectDatiAziendaTributaria(cuaa);
      if (anagDettaglio.getAnagraficaDettaglio() != null)
      {
        anagDettaglio.getAnagraficaDettaglio().setCfCollegati(
            sianDAO.selectCodiciFiscaliCollegatiSian(anagDettaglio
                .getIdAziendaTributaria()));
        anagDettaglio.getAnagraficaDettaglio().setPartiteIvaAttribuite(
            sianDAO.selectPartiteIvaAttribuiteSian(anagDettaglio
                .getIdAziendaTributaria()));
        anagDettaglio.getAnagraficaDettaglio().setSocietaRappresentate(
            sianDAO.selectSocietaRappresentataSian(anagDettaglio
                .getIdAziendaTributaria(),orderSocRapp));
        anagDettaglio.getAnagraficaDettaglio().setDomiciliFiscaliVariati(
            sianDAO.selectDomicilioFiscaleVariatoSian(anagDettaglio
                .getIdAziendaTributaria()));
        anagDettaglio.getAnagraficaDettaglio().setResidenzeVariate(
            sianDAO.selectResidenzaVariataSian(anagDettaglio
                .getIdAziendaTributaria()));
        anagDettaglio.getAnagraficaDettaglio().setRappresentantiSocieta(
            sianDAO.selectRappresentantiSocietaSian(anagDettaglio
                .getIdAziendaTributaria(),storicoRappLeg));

      }
      if (anagDettaglio.getISWSResponse().getMsgRet() != null)
      {
        throw new SolmrException(anagDettaglio.getISWSResponse().getMsgRet());
      }

      return anagDettaglio;
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }

    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }*/
  
  
  public SianAnagTributariaGaaVO ricercaAnagraficaFromDB(String cuaa, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception
  {
    try
    {
      SolmrLogger.debug(this, "Invocating  ricercaAnagraficaFromDB ");
      SolmrLogger.debug(this, "cuaa " + cuaa);
      String elenco[] = new String[1];
      elenco[0] = cuaa;
      sianAggiornaDatiTributaria(elenco, sianUtenteVO);
      SianAnagTributariaGaaVO sianAnagTributariaGaaVO = sianDAO.selectDatiAziendaTributaria(cuaa);
      if(sianAnagTributariaGaaVO != null)
      {
        sianAnagTributariaGaaVO.setvAtecoSecSian(sianDAO
            .selectDatiAziendaTributariaAtecoSec(sianAnagTributariaGaaVO.getIdAziendaTributaria()));        
      }
      if (sianAnagTributariaGaaVO.getMessaggioErrore() != null)
      {
        throw new SolmrException(sianAnagTributariaGaaVO.getMessaggioErrore());
      }

      return sianAnagTributariaGaaVO;
    }
    catch (SolmrException se)
    {
      throw new SolmrException(se.getMessage());
    }

    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  
  public SianAnagTributariaVO selectDatiAziendaTributaria(String cuaa)
    throws Exception
  {
    SianAnagTributariaVO sianAnagTributariaVO=null;
    try
    {
      sianAnagTributariaVO = sianDAO.selectDatiAziendaTributaria(cuaa);
    }
    catch (Exception dae) {}
    
    
    return sianAnagTributariaVO;
  }  
  
  /**
   * 
   * Metodo che si occupa di richiamare ricercaAnagrafica 
   * 
   * 
   * @param cuaa
   * @param sianCredenzialiVO
   * @return
   * @throws SolmrException
   * @throws Exception
   */
  private Risposta_RichiestaRispostaSincrona_ricercaAnagraficaAll ricercaAnagrafica(
      String cuaa, SianCredenzialiVO sianCredenzialiVO) throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating AnagraficaDettaglio method in SianBean");
    try
    {
      it.csi.sian.ricercaAnagrafica.ISValidazioneAnagrafeWSSoapBindingStub binding;
  
      it.csi.sian.ricercaAnagrafica.ISValidazioneAnagrafeWSServiceLocator ricercaAnagrafica_ServiceLocator = new it.csi.sian.ricercaAnagrafica.ISValidazioneAnagrafeWSServiceLocator();
      
      SolmrLogger.debug(this, "-- SIAN_URL_RICERCA_ANAGRAFICA ="+SianConstants.SIAN_URL_RICERCA_ANAGRAFICA);
  
      ricercaAnagrafica_ServiceLocator.setISValidazioneAnagrafeWSPortEndpointAddress(SianConstants.SIAN_URL_RICERCA_ANAGRAFICA);
     
      binding = (it.csi.sian.ricercaAnagrafica.ISValidazioneAnagrafeWSSoapBindingStub)
          ricercaAnagrafica_ServiceLocator.getISValidazioneAnagrafeWSPort();
  
      // Time out after a minute
      SolmrLogger.debug(this, "SIAN_TIME_OUT ");
      SolmrLogger.debug(this, "" + SianConstants.SIAN_TIME_OUT);
      binding.setTimeout(SianConstants.SIAN_TIME_OUT);
      
      it.csi.sian.ricercaAnagrafica.Richiesta_RichiestaRispostaSincrona_ricercaAnagraficaAll richiesta = 
          new it.csi.sian.ricercaAnagrafica.Richiesta_RichiestaRispostaSincrona_ricercaAnagraficaAll();
      
      it.csi.sian.ricercaAnagrafica.IdentificativoFiscale idFiscale = new it.csi.sian.ricercaAnagrafica.IdentificativoFiscale();
      
   
      if (cuaa.length() == 11){
        idFiscale.setCodiceFiscalePersonaGiuridica(cuaa);
        SolmrLogger.debug(this, "----- CodiceFiscalePersonaGiuridica ="+cuaa);
      }  
      else{
        idFiscale.setCodiceFiscalePersonaFisica(cuaa);
        SolmrLogger.debug(this, "----- CodiceFiscalePersonaFisica ="+cuaa);
      }                  
      
      richiesta.setIdentificativo(idFiscale);
      
      
      it.csi.sian.ricercaAnagrafica.ISAutenticazione utente = new it.csi.sian.ricercaAnagrafica.ISAutenticazione();
      
      //utente.setUtenteCF(sianCredenzialiVO.getUtenteCF());
     
      // Per Sian Test
      /*SolmrLogger.debug(this, "-- FORZATO CODICE FISCALE UTENTE ABILITATO = AAABBB00L04H501F");
      utente.setUtenteCF("AAABBB00L04H501F");*/
      
      // Per Sian Produzione
      SolmrLogger.debug(this, "-- FORZATO CODICE FISCALE UTENTE ABILITATO = SRSLSN79D64L219H");
      utente.setUtenteCF("SRSLSN79D64L219H");
      
      
      //Assolutamente da togliere sempre***********
      //utente.setUtenteCF("DPLGNZ55A09F952X"); //Cuaa valido per la produzione
      //Non usato ancora...usato solo il cuaa
      //utente.setRuolo(sianCredenzialiVO.getRuolo());
      //utente.setIdUtenteEnte(sianCredenzialiVO.getIdUtenteEnte());
      richiesta.setUtente(utente);
  
      SolmrLogger.debug(this, "-- PRIMA DELLA CHIAMATA A ricercaAnagraficaAll");
      Risposta_RichiestaRispostaSincrona_ricercaAnagraficaAll rs = binding.ricercaAnagraficaAll(richiesta);
      SolmrLogger.debug(this, "-- DOPO DELLA CHIAMATA A ricercaAnagraficaAll");
      
      if (rs == null)
      {
        SolmrLogger
           .error(this,
               "Throwing SolmrException in ricercaAnagrafica method in SianBean");
        throw new SolmrException((String) AnagErrors
           .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
        return rs;
    }
    catch (SolmrException se)
    {
      SolmrLogger
         .error(
             this,
             "SolmrException during the invocation of ricercaAnagrafica method in SianBean");
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
           .error(
               this,
               "org.apache.commons.httpclient.HttpException during the invocation of ricercaAnagrafica method in SianBean");
        throw new SolmrException((String) AnagErrors
           .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        SolmrLogger.error(this,
           "IOException during the invocation of ricercaAnagrafica method in SianBean"
               + ie.getClass() + " - " + ie.getMessage());
        throw new SolmrException((String) AnagErrors
           .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
           + " " + ie.getClass() + " - " + ie.getMessage());
      }
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
         .error(
             this,
             "IllegalArgumentException during the invocation of ricercaAnagrafica method in SianBean"
                 + iae.getClass() + " - " + iae.getMessage());
      throw new SolmrException((String) AnagErrors
         .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
         + " " + iae.getClass() + " - " + iae.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger
         .error(this,
             "Exception during the invocation of ricercaAnagrafica method in SianBean");
      e.printStackTrace();
      throw new SolmrException((String) AnagErrors
         .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
    }
  
  }
  
  
  

  public void serviceSianAggiornaDatiBDN(String CUAA) throws SolmrException,
      Exception
  {
    SianAllevamentiRispostaVO sianAllevamentiRispostaVO = null;
    SianAllevamentiVO[] elencoSianAllevamentiVO = null;
    SianResponseVO sianResponseVO = null;

    try
    {
      sianAllevamentiRispostaVO = leggiAllevamentiTeramo(CUAA);
      elencoSianAllevamentiVO = sianAllevamentiRispostaVO.getRisposta();
      sianResponseVO = sianAllevamentiRispostaVO.getResponse();
    }
    catch (SolmrException se)
    {
      if (se.getMessage() == (String) AnagErrors.get("ERR_SIAN_NO_ALLEVAMENTI"))
      {
        sianAllevamentiRispostaVO = new SianAllevamentiRispostaVO();
      }
    }
    catch (Exception e)
    {
      // Non faccio nulla. iSWSReturnDett resta null e il metodo
      // lo gestisce scrivendo un record
      // appropriato su db.
    }
    try
    {

      sianDAO.deleteAllevamentiSian(CUAA);

      if (sianAllevamentiRispostaVO != null)
      {
        if (sianResponseVO == null)
        {
          if (elencoSianAllevamentiVO != null
              && elencoSianAllevamentiVO.length > 0)
          {
            for (int i = 0; i < elencoSianAllevamentiVO.length; i++)
            {
              sianDAO.insertAllevamentiSian(elencoSianAllevamentiVO[i], CUAA,
                  null, SolmrConstants.SIAN_FLAG_PRESENTE_AT, true);
            }
          }
          else
            sianDAO
                .insertAllevamentiSian(null, CUAA, null, SolmrConstants.SIAN_FLAG_NON_PRESENTE_AT,
                    false);
        }
        else
        {

          if (!sianResponseVO.getCodRet().equalsIgnoreCase(SolmrConstants.SIAN_CODICE_SERVIZIO_ALLEVAMENTI_NO_RECORD))
          {
            sianDAO.insertAllevamentiSian(null, CUAA, sianResponseVO
                .getMsgRet(), SolmrConstants.SIAN_FLAG_NON_PRESENTE_AT, false);
          }
          else
            sianDAO.insertAllevamentiSian(null, CUAA, sianResponseVO
                .getMsgRet(), SolmrConstants.SIAN_FLAG_PRESENTE_AT_ERRORE, false);
        }
      }
      else
      {
        sianDAO.insertAllevamentiSian(null, CUAA, (String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"), SolmrConstants.SIAN_FLAG_PRESENTE_AT_ERRORE, false);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  public void sianAggiornaDatiAlboVigneti(AnagAziendaVO anagAziendaVO) 
    throws SolmrException,Exception
  {
    UvResponse response = null;
    String codice=null,descrizioneEsito=null;
    
    try
    {
      response = elencoUnitaVitatePerCUAACCIAA(anagAziendaVO.getCUAA());
    }
    catch (Exception e)
    {
      // Non faccio nulla. response resta null e il metodo
      // lo gestisce scrivendo un record
      // appropriato su db.
      String temp=e.toString();
      if (temp!=null && temp.length()>200) temp=temp.substring(0,200);
      descrizioneEsito=temp;
    }
    try
    { 
      if (response!=null && response.getRispostaType() != null)
      {
        codice=response.getRispostaType().getCodRet();
        descrizioneEsito=response.getRispostaType().getSegnalazione();
      }
      //Se Response.CodRet=012 (Operazione correttamente eseguita) o 
      //Response.CodRet=016 (Dati non trovati - NON RISULTA REGISTRATO ALCUN ALBO VIGNETO ALLA CCIAA)
      if (SolmrConstants.CCIAA_CODICE_SERVIZIO_OK.equals(codice)
          || SolmrConstants.CCIAA_CODICE_SERVIZIO_NO_RECORD.equals(codice))
      {
        //1.  Eliminare tutte i record presenti sulla tavola db_cciaa_albo_dettaglio figli 
        //della tavola db_cciaa_albo_vigneti accedendo per id_azienda.
        //Eliminare il record presente sulla tavola db_cciaa_albo_vigneti accedendo per id_azienda
        sianDAO.deleteCciaaAlboVigneti(anagAziendaVO.getIdAzienda());
        //2.  Inserire un record sulla tabella: db_cciaa_albo_vigneti
        Long idCciaaAlboVigneti=sianDAO.insertCciaaAlboVigneti(anagAziendaVO.getIdAzienda(), codice, descrizioneEsito); 
        
        if (response.getRisposta()!=null && response.getRisposta().length>0)
        {
          //3.  Inserire le UV: Per ogni DatiUv restitui inserire un record su db_cciaa_albo_dettaglio
          sianDAO.insertAllCciaaAlboDettaglio(response.getRisposta(),idCciaaAlboVigneti); 
        }
      }
      else
      {
        //Altrimenti (Response.CodRet<>012,016 oppure il servizio non è raggiungibile, oppure si è verificata in’eccezione , ecc)
        
        //1.  Verificare se esiste un record su db_cciaa_albo_vigneti filtrando per id_azienda 
        //e per (codice_esito=012 oppure 016), 
        //nel caso esistesse non proseguire altrimenti precodere ai punti 2 e 3 
        if (!sianDAO.selectAlboVignetiValido(anagAziendaVO.getIdAzienda()))
        {
          //2.  Eliminare tutte i record presenti sulla tavola db_cciaa_albo_dettaglio figli della
          //tavola db_cciaa_albo_vigneti accedendo per id_azienda.
          //Eliminare il record presente sulla tavola db_cciaa_albo_vigneti accedendo per id_azienda.
          sianDAO.deleteCciaaAlboVigneti(anagAziendaVO.getIdAzienda());
          //3.  Procedere nell’inserimento di un record sulla tabella db_cciaa_albo_vigneti:
          sianDAO.insertCciaaAlboVigneti(anagAziendaVO.getIdAzienda(), codice, descrizioneEsito); 
        }
      }
    
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che mi restituisce l'elenco degli allevamenti relativi ad una
   * determinata azienda agricola tramite collegamento con web-service di Teramo
   * 
   * @param cuaa
   *          String
   * @return SianAllevamentiRispostaVO
   * @throws SolmrException
   * @throws Exception
   */
  private SianAllevamentiRispostaVO leggiAllevamentiTeramo(String CUAA)
      throws SolmrException
  {
    SolmrLogger.debug(this,
        "Invocating leggiAllevamentiTeramo method in SianBean");
    SianAllevamentiRispostaVO sianAllevamentiRispostaVO = new SianAllevamentiRispostaVO();
    SianResponseVO response = null;
    SianAllevamentiVO[] sianAllevamentiVO = null;
    PostMethod method = null;
    it.csi.teramo.ageaAut.Root root = null;
    try
    {
      // Mi connetto alla servlet che si occupa di chiamare il WEB-SERVICE di
      // teramo "anagrafica_Allevamenti"
      // che mi restituisce l'oggetto serializzato
      HttpClient client = new HttpClient();

      SolmrLogger
          .debug(this,
              "Setting CONNECTION_TIMEOUT in method leggiAllevamentiTeramo in SianBean");
      
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method leggiAllevamentiTeramo in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method leggiAllevamentiTeramo in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "TeramoAnagraficaAllevamentiServlet");

      // Configure the form parameters
      method.addParameter("cuaa", CUAA);
      method.addParameter("dataRichiesta", "");
      // Execute the POST method

      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .debug(
                this,
                "Throwing SolmrException in leggiAllevamentiTeramo method in SianBean for connection bean timeout");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof it.csi.teramo.ageaAut.Root)
          {
            root = (it.csi.teramo.ageaAut.Root) obj;
          }
          else
          {
            SolmrLogger
                .debug(this,
                    "Throwing SolmrException in leggiAllevamentiTeramo method in SianBean");
            throw new SolmrException((String) AnagErrors
                .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
          }
        }
        else
        {
          SolmrLogger
              .debug(this,
                  "Throwing SolmrException in leggiAllevamentiTeramo method in SianBean");
          throw new SolmrException((String) AnagErrors
              .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
        }
      }
      //if (root == null)
        //throw new SolmrException((String) AnagErrors
          //  .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      if (root.getError_info() != null)
      {
        if (root.getError_info().getError() != null)
        {
          if (SolmrConstants.TERAMO_CODICE_NO_ALLEVAMENTI.equals(root
              .getError_info().getError().getId()))
            throw new SolmrException((String) AnagErrors
                .get("ERR_SIAN_NO_ALLEVAMENTI"));
          if (root.getError_info().getError().getDes() != null
              && !"".equals(root.getError_info().getError().getDes()))
          {
            response = new SianResponseVO();
            response.setCodRet(root.getError_info().getError().getId());
            response.setMsgRet(root.getError_info().getError().getDes());
          }
        }
      }
      if (response == null)
      {
        if (root.getDati() == null)
          throw new SolmrException((String) AnagErrors
              .get("ERR_SIAN_NO_ALLEVAMENTI"));
        it.csi.teramo.ageaAut.RootDatiANAGRAFICA_ALLEVAMENTO rootDatiANAGRAFICA_ALLEVAMENTO[] = root
            .getDati().getDsANAGRAFICA_ALLEVAMENTI();
        if (rootDatiANAGRAFICA_ALLEVAMENTO == null
            || rootDatiANAGRAFICA_ALLEVAMENTO.length == 0)
          throw new SolmrException((String) AnagErrors
              .get("ERR_SIAN_NO_ALLEVAMENTI"));

        sianAllevamentiVO = new SianAllevamentiVO[rootDatiANAGRAFICA_ALLEVAMENTO.length];

        for (int i = 0; i < rootDatiANAGRAFICA_ALLEVAMENTO.length; i++)
          sianAllevamentiVO[i] = convertiSianAllevamentiVO(rootDatiANAGRAFICA_ALLEVAMENTO[i]);
      }
      sianAllevamentiRispostaVO.setResponse(response);
      sianAllevamentiRispostaVO.setRisposta(sianAllevamentiVO);
      return sianAllevamentiRispostaVO;

    }
    catch (SolmrException se)
    {
      SolmrLogger
          .debug(
              this,
              "SolmrException during the invocation of leggiAllevamentiTeramo method in SianBean");
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
            .debug(
                this,
                "org.apache.commons.httpclient.HttpException during the invocation of leggiAllevamentiTeramo method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        SolmrLogger
            .debug(
                this,
                "IOException during the invocation of leggiAllevamentiTeramo method in SianBean");
        throw new SolmrException((String) AnagErrors
            .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
            + " " + ie.getClass() + " - " + ie.getMessage());
      }
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .debug(
              this,
              "IllegalArgumentException during the invocation of leggiAllevamentiTeramo method in SianBean");
      throw new SolmrException((String) AnagErrors
          .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
          + " " + iae.getClass() + " - " + iae.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger
          .debug(this,
              "Exception during the invocation of leggiAllevamentiTeramo method in SianBean");
      e.printStackTrace();
      throw new SolmrException((String) AnagErrors
          .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
    }
  }

  /**
   * Metodo che mi restituisce il registro di stalla BDN tramite collegamento
   * con il web-service elenco_RegistriStalla_Cod di Teramo
   * 
   * @param String
   *          cuaa
   * 
   * @return SianAllevamentiRispostaVO
   * @throws SolmrException
   * @throws Exception
   */
  private ElencoRegistroDiStallaVO elencoRegistriStallaTeramo(
      String codiceAzienda, String codiceSpecie, String cuaa, String tipo,
      String ordinamento) throws SolmrException
  {
    SolmrLogger.debug(this,
        "Invocating elencoRegistriStallaTeramo method in SianBean");
    ElencoRegistroDiStallaVO registro = null;
    PostMethod method = null;
    try
    {
      // Mi connetto alla servlet che si occupa di chiamare il WEB-SERVICE di
      // teramo "elenco_RegistriStalla_Cod"
      // che mi restituisce l'oggetto serializzato
      HttpClient client = new HttpClient();

      SolmrLogger
          .debug(this,
              "Setting CONNECTION_TIMEOUT in method elencoRegistriStallaTeramo in SianBean");
      
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method elencoRegistriStallaTeramo in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method elencoRegistriStallaTeramo in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "TeramoElencoRegistriStallaCodServlet");

      // Configure the form parameters
      if (codiceAzienda != null)
        method.addParameter("codiceAzienda", codiceAzienda);
      if (codiceSpecie != null)
        method.addParameter("codiceSpecie", codiceSpecie);
      if (cuaa != null)
        method.addParameter("cuaa", cuaa);
      if (tipo != null)
        method.addParameter("tipo", tipo);
      if (ordinamento != null)
        method.addParameter("ordinamento", ordinamento);
      // Execute the POST method

      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .fatal(
                this,
                "Throwing SolmrException in elencoRegistriStallaTeramo method in SianBean for connection bean timeout");
        throw new SolmrException(AnagErrors.ERR_SERVIZIO_BDN_NON_DISPONIBILE);
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof ElencoRegistroDiStallaVO)
            registro = (ElencoRegistroDiStallaVO) obj;
          else
          {
            SolmrLogger
                .fatal(this,
                    "Throwing SolmrException in elencoRegistriStallaTeramo method in SianBean");
            throw new SolmrException(
                AnagErrors.ERR_SERVIZIO_BDN_NON_DISPONIBILE);
          }
        }
        else
        {
          SolmrLogger
              .fatal(this,
                  "Throwing SolmrException in elencoRegistriStallaTeramo method in SianBean");
          throw new SolmrException(AnagErrors.ERR_SERVIZIO_BDN_NON_DISPONIBILE);
        }
      }
      //if (registro == null)
        //throw new SolmrException(AnagErrors.ERR_SERVIZIO_BDN_NON_DISPONIBILE);

      return registro;

    }
    catch (SolmrException se)
    {
      SolmrLogger
          .error(
              this,
              "SolmrException during the invocation of elencoRegistriStallaTeramo method in SianBean");
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
            .error(
                this,
                "org.apache.commons.httpclient.HttpException during the invocation of elencoRegistriStallaTeramo method in SianBean:"
                    + ie.toString());
        throw new SolmrException(AnagErrors.ERR_SERVIZIO_BDN_NON_DISPONIBILE);
      }
      else
      {
        SolmrLogger
            .error(
                this,
                "IOException during the invocation of elencoRegistriStallaTeramo method in SianBean:"
                    + ie.toString());
        throw new SolmrException(AnagErrors.ERR_SERVIZIO_BDN_NON_DISPONIBILE);
      }
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .error(
              this,
              "IllegalArgumentException during the invocation of elencoRegistriStallaTeramo method in SianBean:"
                  + iae.toString());
      throw new SolmrException(AnagErrors.ERR_SERVIZIO_BDN_NON_DISPONIBILE);
    }
    catch (Exception e)
    {
      SolmrLogger
          .error(
              this,
              "Exception during the invocation of elencoRegistriStallaTeramo method in SianBean:"
                  + e.toString());
      e.printStackTrace();
      throw new SolmrException(AnagErrors.ERR_SERVIZIO_BDN_NON_DISPONIBILE);
    }
  }

  public RespAnagFascicoloVO getRespAnagFascicolo(
      Long idAzienda) throws SolmrException, Exception
  {
    try
    {
      return sianDAO.getRespAnagFascicolo(idAzienda);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Questo metodo è utilizzato per convertire l'oggetto iSWSResponse ricevuto
   * da SIAN con il nostro oggetto SianResponseVO
   */
  /*public SianResponseVO convertiISWSResponse(String nomeMetodo,
      it.csi.sian.anagrafeAT.ISWSResponse iSWSResponse)
  {
    String THIS_METHOD = "SianBean::convertiISWSResponse(" + nomeMetodo
        + ") - ";
    SolmrLogger.debug(this, THIS_METHOD + "BEGIN.");
    SianResponseVO response = null;
    if (iSWSResponse != null)
    {
      response = new SianResponseVO();
      response.setCodRet(iSWSResponse.getCodRet());
      response.setMsgRet(iSWSResponse.getMsgRet());
      SolmrLogger.debug(this, THIS_METHOD + "iSWSResponse.getCodRet(): "
          + iSWSResponse.getCodRet());
      SolmrLogger.debug(this, THIS_METHOD + "iSWSResponse.getMsgRet(): "
          + iSWSResponse.getMsgRet());
    }
    SolmrLogger.debug(this, THIS_METHOD + "END.");
    return response;
  }*/

  private SianAllevamentiVO convertiSianAllevamentiVO(
      it.csi.teramo.ageaAut.RootDatiANAGRAFICA_ALLEVAMENTO allevamento)
  {
    if (allevamento == null)
      return null;
    SianAllevamentiVO risposta = new SianAllevamentiVO();
    risposta.setAllevId(allevamento.getALLEV_ID());
    risposta.setAutorizzazioneLatte(allevamento.getAUTORIZZAZIONE_LATTE());
    risposta.setAziendaCodice(allevamento.getAZIENDA_CODICE());
    risposta.setCap(allevamento.getCAP());
    risposta.setCapiTotali(allevamento.getCAPI_TOTALI());
    risposta.setCodFiscaleDeten(allevamento.getCOD_FISCALE_DETEN());
    risposta.setCodFiscaleProp(allevamento.getCOD_FISCALE_PROP());
    risposta.setComune(allevamento.getCOMUNE());
    risposta.setDataCalcoloCapi(allevamento.getDATA_CALCOLO_CAPI());
    risposta.setDenomDetentore(allevamento.getDENOM_DETENTORE());
    risposta.setDenomProprietario(allevamento.getDENOM_PROPRIETARIO());
    risposta.setDenominazione(allevamento.getDENOMINAZIONE());
    risposta.setDtFineAttivita(allevamento.getDT_FINE_ATTIVITA());
    risposta.setDtFineDetentore(allevamento.getDT_FINE_DETENTORE());
    risposta.setDtInizioAttivita(allevamento.getDT_INIZIO_ATTIVITA());
    risposta.setDtInizioDetentore(allevamento.getDT_INIZIO_DETENTORE());
    risposta.setFoglioCatastale(allevamento.getFOGLIO_CATASTALE());
    risposta.setIndirizzo(allevamento.getINDIRIZZO());
    risposta.setLatitudine(allevamento.getLATITUDINE());
    risposta.setLocalita(allevamento.getLOCALITA());
    risposta.setLongitudine(allevamento.getLONGITUDINE());
    risposta.setParticella(allevamento.getPARTICELLA());
    risposta.setSezione(allevamento.getSEZIONE());
    risposta.setSoccida(allevamento.getSOCCIDA());
    risposta.setSpeCodice(allevamento.getSPE_CODICE());
    risposta.setSubalterno(allevamento.getSUBALTERNO());
    risposta.setTipoProduzione(allevamento.getTIPO_PRODUZIONE());
    risposta
        .setOrientamentoProduttivo(allevamento.getORIENTAMENTO_PRODUTTIVO());
    return risposta;
  }

 

  //associtao al nuovo srevizoo ricerca anagrafica
  /**
   * Metodo che si occupa di invocare il webservice SIAN per ottenere i dati
   * dell'anagrafe tributaria e aggiornare le tabelle in anagrafe
   * 
   * @param elencoCuaa
   * @throws Exception
   */
  public void sianAggiornaDatiTributaria(String[] elencoCuaa, SianUtenteVO sianUtenteVO)
      throws Exception
  {
    try
    {
      Risposta_RichiestaRispostaSincrona_ricercaAnagraficaAll rispostaRicerca = null;
      for (int i = 0; i < elencoCuaa.length; i++)
      {
        // controllare se c'è un record su DB_AZIENDA_TRIBUTARIA
        // inserito da un numero di giorni non superiori a quelli
        // indicati in DB_PARAMETRO per codice=GGAT.
        try
        {
          
          if (sianDAO.selectAziendaTributaria(elencoCuaa[i]))
            continue;
          
          //cerco il codice fiscale in base al ruolo
          SianCredenzialiVO sianCredenzialiVO = new SianCredenzialiVO();
          if(sianUtenteVO != null)
          {
            sianCredenzialiVO.setUtenteCF(getCFForSian(sianUtenteVO));
          }
          
          rispostaRicerca = ricercaAnagrafica(elencoCuaa[i], sianCredenzialiVO);
        }
        catch (Exception e)
        {
          // Non faccio nulla. Risposta_RichiestaRispostaSincrona_ricercaAnagrafica 
          // resta null e il metodo
          // aggiornaSIANAziendaTributaria() lo gestisce scrivendo un record
          // appropriato su db.
        }
        AnagAziendaVO anagAziendaVO = new AnagAziendaVO();
        anagAziendaVO.setCUAA(elencoCuaa[i]);
        aggiornaSIANAziendaTributaria(anagAziendaVO, rispostaRicerca);
      }
    }    
    catch (SolmrException se)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(se.getMessage());
    }
    catch (Exception re)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(re.getMessage());
    }
  }
  
  

  /**
   * Metodo che mi restituisce l'elenco dei TIPI_OPR decodificati da SIAN
   * 
   * @param principale
   * @param orderBy []
   * @return it.csi.solmr.dto.DoubleStringcodeDescription[]
   * @throws Exception
   */
  public DoubleStringcodeDescription[] getListSianTipoOpr(boolean principale,
      String orderBy[]) throws Exception
  {
    try
    {
      return sianDAO.getListSianTipoOpr(principale, orderBy);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Recupera organismo pagatore formattato "CODICE-DESCRIZIONE" utilizzato nel
   * getore fascicolo
   * 
   * @throws Exception
   * @return String
   */
  public String getOrganismoPagatoreFormatted(String codiceSianOpr)
      throws Exception
  {
    try
    {
      return sianDAO.getOrganismoPagatoreFormatted(codiceSianOpr);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  /**
   * Metodo che richiama il WS elencoUnitaVitatePerCUAA (tramite la webApp
   * SIAN).
   * 
   * @param cuaa
   *          String
   * @return Hashtable
   * @throws SolmrException
   * @throws Exception
   */
  public UvResponseCCIAA elencoUnitaVitatePerCUAA(String cuaa, Long idAzienda, RuoloUtenza ruolo)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this,
        "Invocating elencoUnitaVitatePerCUAA method in SianBean");
    if (cuaa != null)
      cuaa = cuaa.toUpperCase();
    UvResponseCCIAA uvResponseCCIAA = new UvResponseCCIAA();
    UvResponse response = null;
    try
    {
      response = elencoUnitaVitatePerCUAACCIAA(cuaa);
      if (response.getRispostaType() != null)
      {
        uvResponseCCIAA.setCodRet(response.getRispostaType().getCodRet());
        uvResponseCCIAA.setSegnalazione(response.getRispostaType()
            .getSegnalazione());
        if (SolmrConstants.CCIAA_CODICE_SERVIZIO_OK.equals(response
            .getRispostaType().getCodRet()))
        {
          // Se il servizio ha restituito ok vado a leggere i dati e li
          // trasformo
          // per la visualizzazione
          if (response.getRisposta() != null
              && response.getRisposta().length > 0)
          {
            // Il servizio ha restituito delle U.V.
            int size = response.getRisposta().length;
            DatiUvCCIAA datiUvCCIAA[] = new DatiUvCCIAA[size];
            double totSup = 0;
            TreeMap<Object,Object> albo = new TreeMap<Object,Object>(); // mi servono per preparare il filtro
                                          // sugli albi
            TreeMap<Object,Object> vitigno = new TreeMap<Object,Object>(); // mi servono per preparare il
                                              // filtro sui vitigni
            for (int i = 0; i < size; i++)
            {
              datiUvCCIAA[i] = convertiUvCCIAA(response.getRisposta()[i]);
              /*
               * Evidenziare in rosso quelle u.v. che non trovano una
               * corrispondenza come chiave catastale nell’archivio
               * dell’anagrafe considerando quindi la chiave catastale ritornata
               * dalla CCIAA (Comune, Sz., Fgl., Part.) deve esistere su
               * db_storico_particella (occorrenza attiva ovvero
               * data_fine_validita=null) legata alla tavola delle unità arobree
               * (db_storico_unita_arborea, solo occorrenze attive ovvero
               * data_fine_validita=null , solo di tipologia “VINO” ovvero
               * prog_unar=1, solo dell’azienda considerata id_azienda=xxxxx)
               * tramite id_particella. Nella verifica della corrispondenza è
               * stato omesso il subalterno perché attualmente non è fornito
               * dalla CCIAA.Tenere presente che se nel campo Particella fornita
               * dalla CCIAA è presente la lettera “P” questa deve essere omessa
               * prima di procedere nella ricerca di una corrispondenza e
               * comunque tale campo deve essere numerico.
               */
              datiUvCCIAA[i].setVUnitaArboreaCCIAA(sianDAO
                  .confrontaInformazioniCCIAA(idAzienda, datiUvCCIAA[i]));

              // Devo recuperaee la descrizione del comune e la sigla della
              // provincia
              if (datiUvCCIAA[i].getIstatComune() != null)
              {
                ComuneVO comuneVO = commonDAO.getComuneByISTAT(datiUvCCIAA[i]
                    .getIstatComune());
                datiUvCCIAA[i].setDescComune(comuneVO.getDescom());
                datiUvCCIAA[i].setSiglaProvincia(comuneVO.getSiglaProv());
                
                if(ruolo.isUtenteProvinciale()
                    && (ruolo.getCodiceEnte() != null))
                {
                  if(ruolo.getCodiceEnte().equals(comuneVO.getIstatProvincia()))
                  {
                    datiUvCCIAA[i].setStessaProvinciaCompetenza(true);
                  }
                }
                
                
              }

              albo.put(datiUvCCIAA[i].getDescAlbo(), datiUvCCIAA[i]
                  .getDescAlbo());

              if (datiUvCCIAA[i].getVarieta() != null)
                vitigno.put(datiUvCCIAA[i].getVarieta(), datiUvCCIAA[i]
                    .getVarieta());
              totSup += datiUvCCIAA[i].getSuperficieH();
            }

            // Popolo il filtro degli albi, utilizzo una treeMap per eliminare
            // i doppioni e per ordinare gli elementi
            Collection<Object> alboCol = albo.values();
            String alboStr[] = new String[albo.size()];
            Iterator<Object> alboIt = alboCol.iterator();
            int i = 0;
            while (alboIt.hasNext())
              alboStr[i++] = "" + alboIt.next();

            uvResponseCCIAA.setAlbo(alboStr);

            // Popolo il filtro dei vitigni, utilizzo una treeMap per eliminare
            // i doppioni e per ordinare gli elementi
            Collection<Object> vitignoCol = vitigno.values();
            String vitignoStr[] = new String[vitigno.size()];
            Iterator<Object> vitignoIt = vitignoCol.iterator();
            i = 0;
            while (vitignoIt.hasNext())
              vitignoStr[i++] = "" + vitignoIt.next();

            uvResponseCCIAA.setVitigno(vitignoStr);

            uvResponseCCIAA.setTotSuperficieH(totSup);

            // L’elenco deve essere ordinato per albo
            Arrays.sort(datiUvCCIAA, new DatiUvCCIAAComparator(
                DatiUvCCIAAComparator.ORD_ALBO_ASC));

            uvResponseCCIAA.setDatiUv(datiUvCCIAA);
          }
        }
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(
              this,
              "SolmrException during the invocation of elencoUnitaVitatePerCUAA method in SianBean: "
                  + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger
          .fatal(
              this,
              "Exception during the invocation of elencoUnitaVitatePerCUAA method in SianBean"
                  + e.getMessage());
      throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
    }

    SolmrLogger.debug(this,
        "Invocated elencoUnitaVitatePerCUAA method in SianBean");
    return uvResponseCCIAA;
  }

  /**
   * Metodo che richiama il WS elencoUnitaVitatePerParticella (tramite la webApp
   * SIAN).
   * 
   * @param cuaa
   *          String
   * @return Hashtable
   * @throws SolmrException
   * @throws Exception
   */
  public UvResponseCCIAA elencoUnitaVitatePerParticella(String istat,
      String sezione, String foglio, String particella, Long idAzienda, RuoloUtenza ruolo) throws SolmrException,
      Exception
  {
    SolmrLogger.debug(this,
        "Invocating elencoUnitaVitatePerParticella method in SianBean");
    UvResponseCCIAA uvResponseCCIAA = new UvResponseCCIAA();
    UvResponse response = null;
    //ricerca con particella P
    UvResponse responseP = null;
    try
    {
      response = elencoUnitaVitatePerParticellaCCIAA(istat, sezione, foglio,
          particella);
      
      responseP = elencoUnitaVitatePerParticellaCCIAA(istat, sezione,
          foglio, particella + "P");
      
      if ((response.getRispostaType() != null) 
            || (responseP.getRispostaType() != null))
      {
        
        if (SolmrConstants.CCIAA_CODICE_SERVIZIO_NO_RECORD.equals(response
            .getRispostaType().getCodRet()) 
            &&
            SolmrConstants.CCIAA_CODICE_SERVIZIO_NO_RECORD.equals(responseP
            .getRispostaType().getCodRet()))
        {
          //Solo se tutti e 2 non hanno trovato nulla allora metto nessun record trovato
          uvResponseCCIAA.setCodRet(SolmrConstants.CCIAA_CODICE_SERVIZIO_NO_RECORD);
          uvResponseCCIAA.setSegnalazione(response.getRispostaType()
              .getSegnalazione());
        }
        else if (SolmrConstants.CCIAA_CODICE_SERVIZIO_OK.equals(response
            .getRispostaType().getCodRet()) 
            ||
            SolmrConstants.CCIAA_CODICE_SERVIZIO_OK.equals(responseP
            .getRispostaType().getCodRet()))
        {
          //Se almeno uno dei 2 è ok allora devo indicare che tutto è ok
          uvResponseCCIAA.setCodRet(SolmrConstants.CCIAA_CODICE_SERVIZIO_OK);
          // Se il servizio ha restituito ok vado a leggere i dati e li
          // trasformo
          // per la visualizzazione
          DatiUvCCIAA[] datiUvCCIAA = null;
          DatiUvCCIAA[] datiUvCCIAAP = null;
          DatiUvCCIAA[] datiUvCCIAATot = null;
          double totSup = 0;
          double totSupP = 0;
          double totSupTot = 0;
          if(SolmrConstants.CCIAA_CODICE_SERVIZIO_OK.equals(response
            .getRispostaType().getCodRet()))
          {
            uvResponseCCIAA.setSegnalazione(response.getRispostaType()
                .getSegnalazione());
            if (response.getRisposta() != null
                && response.getRisposta().length > 0)
            {
              // Il servizio ha restituito delle U.V.
              int size = response.getRisposta().length;
              datiUvCCIAA = new DatiUvCCIAA[size];
              for (int i = 0; i < size; i++)
              {
                datiUvCCIAA[i] = convertiUvCCIAA(response.getRisposta()[i]);
                
                
                datiUvCCIAA[i].setVUnitaArboreaCCIAA(sianDAO
                    .confrontaInformazioniCCIAA(idAzienda, datiUvCCIAA[i]));
  
                // Devo recuperaee la descrizione del comune e la sigla della
                // provincia
                if (datiUvCCIAA[i].getIstatComune() != null)
                {
                  ComuneVO comuneVO = commonDAO.getComuneByISTAT(datiUvCCIAA[i]
                      .getIstatComune());
                  datiUvCCIAA[i].setDescComune(comuneVO.getDescom());
                  datiUvCCIAA[i].setSiglaProvincia(comuneVO.getSiglaProv());
                  
                  
                  if(ruolo.isUtenteProvinciale()
                      && (ruolo.getCodiceEnte() != null))
                  {
                    if(ruolo.getCodiceEnte().equals(comuneVO.getIstatProvincia()))
                    {
                      datiUvCCIAA[i].setStessaProvinciaCompetenza(true);
                    }
                  }
                  
                  
                }
  
                // Devo recuperaee la descrizione del comune e la sigla della
                // provincia di residenza
                if (datiUvCCIAA[i].getIstatResidenza() != null)
                {
                  ComuneVO comuneVO = commonDAO.getComuneByISTAT(datiUvCCIAA[i]
                      .getIstatResidenza());
                  datiUvCCIAA[i].setDescComuneRes(comuneVO.getDescom());
                  datiUvCCIAA[i].setSiglaProvinciaRes(comuneVO.getSiglaProv());
                }
  
                totSup += datiUvCCIAA[i].getSuperficieH();
              }
            }
          }  
          
          
          if(SolmrConstants.CCIAA_CODICE_SERVIZIO_OK.equals(responseP
              .getRispostaType().getCodRet()))
          {
            uvResponseCCIAA.setSegnalazione(responseP.getRispostaType()
                .getSegnalazione());
            if (responseP.getRisposta() != null
                && responseP.getRisposta().length > 0)
            {
              // Il servizio ha restituito delle U.V.
              int size = responseP.getRisposta().length;
              datiUvCCIAAP = new DatiUvCCIAA[size];
              for (int i = 0; i < size; i++)
              {
                datiUvCCIAAP[i] = convertiUvCCIAA(responseP.getRisposta()[i]);
                
                datiUvCCIAAP[i].setVUnitaArboreaCCIAA(sianDAO
                    .confrontaInformazioniCCIAA(idAzienda, datiUvCCIAAP[i]));
  
                // Devo recuperaee la descrizione del comune e la sigla della
                // provincia
                if (datiUvCCIAAP[i].getIstatComune() != null)
                {
                  ComuneVO comuneVO = commonDAO.getComuneByISTAT(datiUvCCIAAP[i]
                      .getIstatComune());
                  datiUvCCIAAP[i].setDescComune(comuneVO.getDescom());
                  datiUvCCIAAP[i].setSiglaProvincia(comuneVO.getSiglaProv());
                  
                  
                  if(ruolo.isUtenteProvinciale()
                      && (ruolo.getCodiceEnte() != null))
                  {
                    if(ruolo.getCodiceEnte().equals(comuneVO.getIstatProvincia()))
                    {
                      datiUvCCIAAP[i].setStessaProvinciaCompetenza(true);
                    }
                  }
                  
                }
  
                // Devo recuperaee la descrizione del comune e la sigla della
                // provincia di residenza
                if (datiUvCCIAAP[i].getIstatResidenza() != null)
                {
                  ComuneVO comuneVO = commonDAO.getComuneByISTAT(datiUvCCIAAP[i]
                      .getIstatResidenza());
                  datiUvCCIAAP[i].setDescComuneRes(comuneVO.getDescom());
                  datiUvCCIAAP[i].setSiglaProvinciaRes(comuneVO.getSiglaProv());
                }
  
                totSupP += datiUvCCIAAP[i].getSuperficieH();
              }
            }
          }
          
          
          totSupTot = totSup + totSupP;
          uvResponseCCIAA.setTotSuperficieH(totSupTot);
          
          if((datiUvCCIAA != null) && (datiUvCCIAAP != null))
          {
            int size = datiUvCCIAA.length + datiUvCCIAAP.length;
            datiUvCCIAATot = new DatiUvCCIAA[size];
            int j=0;
            for(int i=0;i<datiUvCCIAA.length;i++)
            {
              datiUvCCIAATot[j] = datiUvCCIAA[i];
              j++;
            }
            for(int i=0;i<datiUvCCIAAP.length;i++)
            {
              datiUvCCIAATot[j] = datiUvCCIAAP[i];
              j++;
            }
          }
          else if(datiUvCCIAA != null)
          {
            datiUvCCIAATot = datiUvCCIAA;
          }
          else if(datiUvCCIAAP != null)
          {
            datiUvCCIAATot = datiUvCCIAAP;
          }
          
          
          Arrays.sort(datiUvCCIAATot, new DatiUvCCIAAComparator(
              DatiUvCCIAAComparator.ORD_ALBO_CUAA_ASC));

          uvResponseCCIAA.setDatiUv(datiUvCCIAATot);
          
            
            
        }
        
      }
    }
    catch (SolmrException se)
    {
      SolmrLogger
          .fatal(
              this,
              "SolmrException during the invocation of elencoUnitaVitatePerParticella method in SianBean: "
                  + se.getMessage());
      throw new SolmrException(se.getMessage());
    }
    catch (Exception e)
    {
      SolmrLogger
          .fatal(
              this,
              "Exception during the invocation of elencoUnitaVitatePerParticella method in SianBean"
                  + e.getMessage());
      throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
    }

    SolmrLogger.debug(this,
        "Invocated elencoUnitaVitatePerParticella method in SianBean");
    return uvResponseCCIAA;
  }

  private DatiUvCCIAA convertiUvCCIAA(DatiUv datiUv)
  {
    DatiUvCCIAA result = new DatiUvCCIAA();

    result.setAnnoVegetativo(datiUv.getANNO_VEGETATIVO());
    result.setCapResidenza(datiUv.getCAP_RESIDENZA());
    result.setCodiceMipaf(datiUv.getCODICE_MIPAF());
    result.setCodVitigno(datiUv.getCOD_VITIGNO());
    result.setComuneResidenza(datiUv.getCOMUNE_RESIDENZA());
    if(datiUv.getCUAA() != null)
    {  
      result.setCuaa(datiUv.getCUAA().trim());
    }
    result.setDenominazioneAzienda(datiUv.getDENOMINAZIONE_AZIENDA());
    result.setDescAlbo(datiUv.getDESC_ALBO());
    result.setFoglio(datiUv.getFOGLIO());    
    result.setIndirizzoResidenza(datiUv.getINDIRIZZO_RESIDENZA());
    result.setIstatComune(datiUv.getISTAT_COMUNE());
    result.setIstatResidenza(datiUv.getISTAT_RESIDENZA());
    result.setNrMatricola(datiUv.getNR_MATRICOLA());
    result.setNumCeppi(datiUv.getNUM_CEPPI());
    result.setParticella(datiUv.getPARTICELLA());

    
    // Tenere presente che se nel campo Particella fornita dalla CCIAA
    // è presente la lettera “P” questa deve essere omessa prima di procedere
    // nella ricerca di una corrispondenza
    result.setFoglioSearch(StringUtils.sostituisceCaratteriNonNumeroConSpazio(datiUv.getFOGLIO()));
    
    // Tenere presente che se nel campo Particella fornita dalla CCIAA
    // è presente la lettera “P” questa deve essere omessa prima di procedere
    // nella ricerca di una corrispondenza
    result.setParticellaSearch(StringUtils.sostituisceCaratteriNonNumeroConSpazio(datiUv.getPARTICELLA()));

    result.setPvResidenza(datiUv.getPV_RESIDENZA());
    result.setSezione(datiUv.getSEZIONE());
    result.setSiglaCciaa(datiUv.getSIGLA_CCIAA());

    // trasformo la sup. da metri quadri a ettati
    result.setSuperficieH((double) datiUv.getSUPERFICIE_MQ() / 10000);

    result.setVarieta(datiUv.getVARIETA());
    result.setPIva(datiUv.getP_IVA());

    return result;
  }

  /**
   * Metodo che richiama il WS elencoUnitaVitatePerCUAA (tramite la webApp
   * SIAN).
   * 
   * @param cuaa
   * @return
   * @throws SolmrException
   */
  private UvResponse elencoUnitaVitatePerCUAACCIAA(String cuaa)
      throws SolmrException
  {
    SolmrLogger.debug(this,
        "Invocating elencoUnitaVitatePerCUAACCIAA method in SianBean");
    UvResponse response = null;
    PostMethod method = null;
    try
    {
      // Mi connetto alla servlet che si occupa di chiamare il WEB-SERVICE di
      // CCIAA "elencoUnitaVitatePerCUAA"
      // che mi restituisce l'oggetto serializzato
      HttpClient client = new HttpClient();

      SolmrLogger
          .debug(
              this,
              "Setting CONNECTION_TIMEOUT in method elencoUnitaVitatePerCUAACCIAA in SianBean");
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method elencoUnitaVitatePerCUAACCIAA in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method elencoUnitaVitatePerCUAACCIAA in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "CciaaElencoUnitaVitatePerCUAASerlvet");

      // Configure the form parameters
      if (cuaa != null)
        method.addParameter("cuaa", cuaa);
      // Execute the POST method

      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .fatal(
                this,
                "Throwing SolmrException in elencoUnitaVitatePerCUAACCIAA method in SianBean for connection bean timeout");
        throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof UvResponse)
            response = (UvResponse) obj;
          else
          {
            if (obj instanceof Exception)
            {
              Exception e = (Exception) obj;
              SolmrLogger
                  .fatal(
                      this,
                      "Throwing SolmrException in elencoUnitaVitatePerCUAACCIAA method in SianBean:obj instanceof Exception: "
                          + e.getMessage() + "  -  " + e.toString());
              throw new SolmrException(
                  AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
            }
            else
            {
              SolmrLogger
                  .fatal(
                      this,
                      "Throwing SolmrException in elencoUnitaVitatePerCUAACCIAA method in SianBean:obj not instanceof UvResponse or Exception");
              throw new SolmrException(
                  AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
            }
          }
        }
        else
        {
          SolmrLogger
              .fatal(
                  this,
                  "Throwing SolmrException in elencoUnitaVitatePerCUAACCIAA method in SianBean: obj null");
          throw new SolmrException(
              AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
        }
      }
      //if (response == null)
        //throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);

      return response;

    }
    catch (SolmrException se)
    {
      SolmrLogger
          .error(
              this,
              "SolmrException during the invocation of elencoUnitaVitatePerCUAACCIAA method in SianBean");
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
            .error(
                this,
                "org.apache.commons.httpclient.HttpException during the invocation of elencoUnitaVitatePerCUAACCIAA method in SianBean:"
                    + ie.toString());
        throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
      }
      else
      {
        SolmrLogger
            .error(
                this,
                "IOException during the invocation of elencoUnitaVitatePerCUAACCIAA method in SianBean:"
                    + ie.toString());
        throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
      }
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .error(
              this,
              "IllegalArgumentException during the invocation of elencoUnitaVitatePerCUAACCIAA method in SianBean:"
                  + iae.toString());
      throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
    }
    catch (Exception e)
    {
      SolmrLogger
          .error(
              this,
              "Exception during the invocation of elencoUnitaVitatePerCUAACCIAA method in SianBean:"
                  + e.toString());
      e.printStackTrace();
      throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
    }
  }

  /**
   * Metodo che richiama il WS elencoUnitaVitatePerParticella (tramite la webApp
   * SIAN).
   * 
   * @param cuaa
   *          String
   * @param sezione
   *          String
   * @param foglio
   *          String
   * @param particella
   *          String
   * @throws SolmrException
   * @return UvResponse
   */
  private UvResponse elencoUnitaVitatePerParticellaCCIAA(String istat,
      String sezione, String foglio, String particella) throws SolmrException
  {
    SolmrLogger.debug(this,
        "Invocating elencoUnitaVitatePerParticellaCCIAA method in SianBean");
    UvResponse response = null;
    PostMethod method = null;
    try
    {
      // Mi connetto alla servlet che si occupa di chiamare il WEB-SERVICE di
      // CCIAA "elencoUnitaVitatePerParticella"
      // che mi restituisce l'oggetto serializzato
      HttpClient client = new HttpClient();

      SolmrLogger
          .debug(
              this,
              "Setting CONNECTION_TIMEOUT in method elencoUnitaVitatePerParticellaCCIAA in SianBean");
      HttpConnectionManager connManager = client.getHttpConnectionManager();
      HttpConnectionParams params = connManager.getParams();
      params.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      params.setSoTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      //client.setConnectionTimeout(CONNECTION_SIAN_BEAN_TIMEOUT);
      //client.setTimeout(CONNECTION_SIAN_BEAN_DATA_TIMEOUT);
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_TIMEOUT in method elencoUnitaVitatePerParticellaCCIAA in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_TIMEOUT + " ms");
      SolmrLogger
          .debug(
              this,
              "Setted CONNECTION_SIAN_BEAN_DATA_TIMEOUT in method elencoUnitaVitatePerParticellaCCIAA in SianBean with value: "
                  + CONNECTION_SIAN_BEAN_DATA_TIMEOUT + " ms");
      method = new PostMethod((String) SolmrConstants.get("SIAN_SERVLET_URL")
          + "CciaaElencoUnitaVitatePerParticellaSerlvet");

      // Configure the form parameters
      if (istat != null)
        method.addParameter("istat", istat);
      if (sezione != null)
        method.addParameter("sezione", sezione);
      if (foglio != null)
        method.addParameter("foglio", foglio);
      if (particella != null)
        method.addParameter("particella", particella);
      // Execute the POST method

      // Execute the POST method
      int statusCode = client.executeMethod(method);
      if (statusCode != HttpStatus.SC_OK)
      {
        SolmrLogger
            .fatal(
                this,
                "Throwing SolmrException in elencoUnitaVitatePerParticellaCCIAA method in SianBean for connection bean timeout");
        throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
      }
      else
      {
        InputStream is = method.getResponseBodyAsStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        Object obj = ois.readObject();
        if (obj != null)
        {
          if (obj instanceof UvResponse)
            response = (UvResponse) obj;
          else
          {
            if (obj instanceof Exception)
            {
              Exception e = (Exception) obj;
              SolmrLogger
                  .fatal(
                      this,
                      "Throwing SolmrException in elencoUnitaVitatePerParticellaCCIAA method in SianBean:obj instanceof Exception: "
                          + e.getMessage() + "  -  " + e.toString());
              throw new SolmrException(
                  AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
            }
            else
            {
              SolmrLogger
                  .fatal(
                      this,
                      "Throwing SolmrException in elencoUnitaVitatePerParticellaCCIAA method in SianBean:obj not instanceof UvResponse or Exception");
              throw new SolmrException(
                  AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
            }
          }
        }
        else
        {
          SolmrLogger
              .fatal(
                  this,
                  "Throwing SolmrException in elencoUnitaVitatePerParticellaCCIAA method in SianBean: obj null");
          throw new SolmrException(
              AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
        }
      }
      //if (response == null)
        //throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);

      return response;

    }
    catch (SolmrException se)
    {
      SolmrLogger
          .error(
              this,
              "SolmrException during the invocation of elencoUnitaVitatePerParticellaCCIAA method in SianBean");
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
            .error(
                this,
                "org.apache.commons.httpclient.HttpException during the invocation of elencoUnitaVitatePerParticellaCCIAA method in SianBean:"
                    + ie.toString());
        throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
      }
      else
      {
        SolmrLogger
            .error(
                this,
                "IOException during the invocation of elencoUnitaVitatePerParticellaCCIAA method in SianBean:"
                    + ie.toString());
        throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
      }
    }
    catch (IllegalArgumentException iae)
    {
      SolmrLogger
          .error(
              this,
              "IllegalArgumentException during the invocation of elencoUnitaVitatePerParticellaCCIAA method in SianBean:"
                  + iae.toString());
      throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
    }
    catch (Exception e)
    {
      SolmrLogger
          .error(
              this,
              "Exception during the invocation of elencoUnitaVitatePerParticellaCCIAA method in SianBean:"
                  + e.toString());
      e.printStackTrace();
      throw new SolmrException(AnagErrors.ERR_SERVIZIO_CCIAA_NON_DISPONIBILE);
    }
  }
  
  
  private String getCFForSian(SianUtenteVO sianUtenteVO)
    throws SolmrException
  {
    //Ricavo i ruoli per cui non si può
    //usare il proprio codice fiscale
    
    String codiceFiscale = "";
    try
    {
      StringTokenizer st = new StringTokenizer(SolmrConstants.RUOLI_ESCLUSI, ",");
      boolean trovato = false;
      while (st.hasMoreTokens()) 
      {
        if(st.nextToken().equalsIgnoreCase(sianUtenteVO.getRuolo()))
        {
          trovato = true;
          break;
        }
      }
      
      //se trovo il ruolo pesca il codice fiscale del richiedente
      //dal DB
      if(trovato)
      {
        codiceFiscale = commonDAO
              .getCFFromProcedimento(sianUtenteVO.getIdProcedimento());
        
        if(Validator.isEmpty(codiceFiscale))
        {
          throw new SolmrException(AnagErrors.ERR_CODICE_FISCALE_NON_TROVATO);
        }
      }
      else
      {
        codiceFiscale = sianUtenteVO.getCodiceFiscale();
      }
      
      return codiceFiscale;
    }
    catch(Exception e)
    {
      throw new SolmrException(AnagErrors.ERR_CODICE_FISCALE_NON_TROVATO);
    }
  }
  
  
  public StringcodeDescription getDescTipoIscrizioneInpsByCodice(String codiceTipoIscrizioneInps)
    throws Exception
  {
    try
    {
      return sianDAO.getDescTipoIscrizioneInpsByCodice(codiceTipoIscrizioneInps);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  
  private InvioPostaCertificataResponse invioPostaCertificata(
      InvioPostaCertificata invioPosta) throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating invioPostaCertificata method in SianBean");
    //InvioPostaCertificataOut invioPostaOut = new InvioPostaCertificataOut();
    try
    {
      it.csi.smrcomms.siapcommws.SmrcommSrvServiceSoapBindingStub binding;
  
      it.csi.smrcomms.siapcommws.SmrcommSrvServiceLocator invioPosta_ServiceLocator = new it.csi.smrcomms.siapcommws.SmrcommSrvServiceLocator();
  
      invioPosta_ServiceLocator.setSmrcommSrvPortEndpointAddress(SianConstants.SMRCOMM_URL_POSTA_CERTIFICATA);
      
     
      binding = (it.csi.smrcomms.siapcommws.SmrcommSrvServiceSoapBindingStub)
          invioPosta_ServiceLocator.getSmrcommSrvPort();
  
      // Time out after a minute
      SolmrLogger.debug(this, "SIAN_TIME_OUT ");
      SolmrLogger.debug(this, "" + SianConstants.SIAN_TIME_OUT);
      binding.setTimeout(SianConstants.SIAN_TIME_OUT);
      
      
      InvioPostaCertificataResponse response = binding.invioPostaCertificata(invioPosta);
      
      if (response == null)
      {
        SolmrLogger
           .error(this,
               "Throwing SolmrException in invioPostaCertificata method in SianBean");
        throw new SolmrException((String) AnagErrors
           .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        return  response;
      }
    }
    catch (InvalidParameterException se)
    {
      SolmrLogger
         .error(
             this,
             "SolmrException during the invocation of invioPostaCertificata method in SianBean");
      throw new SolmrException(se.getMessage());
    }
    catch (IOException ie)
    {
      if (ie instanceof org.apache.commons.httpclient.HttpException)
      {
        SolmrLogger
           .error(
               this,
               "org.apache.commons.httpclient.HttpException during the invocation of invioPostaCertificata method in SianBean");
        throw new SolmrException((String) AnagErrors
           .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
      }
      else
      {
        SolmrLogger.error(this,
           "IOException during the invocation of invioPostaCertificata method in SianBean"
               + ie.getClass() + " - " + ie.getMessage());
        throw new SolmrException((String) AnagErrors
           .get("ERR_GENERIC_CONNECTION_EXCEPTION_SIAN")
           + " " + ie.getClass() + " - " + ie.getMessage());
      }
    }
    catch (Exception e)
    {
      SolmrLogger
         .error(this,
             "Exception during the invocation of invioPostaCertificata method in SianBean");
      e.printStackTrace();
      throw new SolmrException((String) AnagErrors
         .get("ERR_SERVIZIO_SIAN_NON_DISPONIBILE"));
    }
  
  }
  
  
  
  public String serviceInviaPostaCertificata(InvioPostaCertificata invioPosta)
      throws  SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating  serviceInviaPostaCertificata ");
    SolmrLogger.debug(this, "invioPosta " + invioPosta);
    SolmrLogger.debug(this, "\n\n\n\n\n\n\n");
    
    String valResp = "";
   
    InvioPostaCertificataResponse  risposta = null;
    try
    {
      
      
      //iSWSReturnDett = anagraficaDettaglio(cuaa, sianCredenzialiVO);
      risposta = invioPostaCertificata(invioPosta);
      if((risposta != null) && (risposta.getResult() != null)
          && (risposta.getResult().getEsito() != null))
      {
        SolmrLogger.debug(this, "\n\n\n\n\n\n\n");
        SolmrLogger.debug(this, "risposta.getResult().getEsito().getCodice() "
            + risposta.getResult().getEsito().getCodice());
        SolmrLogger.debug(this, "risposta.getResult().getEsito().getMessaggio() "
            + risposta.getResult().getEsito().getMessaggio());
        SolmrLogger.debug(this, "\n\n\n\n\n\n\n");
      }
    }
    catch (Exception e)
    {
      // Non faccio nulla. iSWSReturnDett resta null e il metodo
      // aggiornaSIANAziendaTributaria() lo gestisce scrivendo un record
      // appropriato su db.
     SolmrLogger.error(this, "--- Exception in invioPostaCertificata ="+e.getMessage());
    }
    
    
    
    return valResp;
    
  }
  
  
  
  private PapuaservSoapBindingStub bindingPapuaWs() throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating invioPostaCertificata method in SianBean");
    PapuaservSoapBindingStub binding;
    try
    {  
      PapuaservLocator papuaservLocator = new PapuaservLocator();
  
      SolmrLogger.debug(this, " --- setpapuaservPortEndpointAddress :"+SianConstants.PAPUA_URL_WS);
      papuaservLocator.setpapuaservPortEndpointAddress(SianConstants.PAPUA_URL_WS);
      
      SolmrLogger.debug(this, " ---  prima di getpapuaservPort");
      binding = (PapuaservSoapBindingStub)
          papuaservLocator.getpapuaservPort();
      SolmrLogger.debug(this, " ---  dopo di getpapuaservPort");
  
      // Time out after a minute
      SolmrLogger.debug(this, "SIAN_TIME_OUT ");
      SolmrLogger.debug(this, "" + SianConstants.SIAN_TIME_OUT);
      binding.setTimeout(SianConstants.SIAN_TIME_OUT);
      
      return binding;
      
    }
    catch (Exception e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of bindingPapuaWs method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception bonding ws papua", e);
      throw new SolmrException(e.getMessage());
    }
  
  }
  
  
  public Ruolo[] findRuoliForPersonaInApplicazione(String codiceFiscale, int livelloAutenticazione)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating  findRuoliForPersonaInApplicazione ");
    SolmrLogger.debug(this, "codiceFiscale " + codiceFiscale);
    SolmrLogger.debug(this, "livelloAutenticazione " + livelloAutenticazione);
    
    Ruolo[] ruoloPapua = null;
    try
    {
      SolmrLogger.debug(this, "-- prima di findRuoliForPersonaInApplicazione");
      SolmrLogger.debug(this, "---- codiceFiscale ="+codiceFiscale);
      SolmrLogger.debug(this, "---- livelloAutenticazione ="+livelloAutenticazione);
      ruoloPapua = bindingPapuaWs().findRuoliForPersonaInApplicazione(codiceFiscale, 
        livelloAutenticazione, SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE);
      SolmrLogger.debug(this, "-- dopo di findRuoliForPersonaInApplicazione");
    }
    catch (InternalException e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of loginPapua method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception loginPapua papua", e);
      String message = e.getFaultString();
      throw new SolmrException(message);
    }
    catch (Exception e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of loginPapua method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception loginPapua papua", e);
      throw new SolmrException(e.getMessage());
    }
    
    return ruoloPapua;
    
  }
  
  
  public UtenteAbilitazioni loginPapua(String codiceFiscale, String cognome, 
      String nome, int livelloAutenticazione, String codiceRuolo)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating  loginPapua ");
    SolmrLogger.debug(this, "codiceFiscale " + codiceFiscale);
    SolmrLogger.debug(this, "cognome " + cognome);
    SolmrLogger.debug(this, "nome " + nome);
    SolmrLogger.debug(this, "livelloAutenticazione " + livelloAutenticazione);
    SolmrLogger.debug(this, "codiceRuolo " + codiceRuolo);
    
    UtenteAbilitazioni utenteAbilitazioni = null;
    try
    {
      utenteAbilitazioni = bindingPapuaWs().login(codiceFiscale, cognome, nome, 
        livelloAutenticazione, SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE, codiceRuolo);
    }
    catch (InternalException e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of loginPapua method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception loginPapua papua", e);
      String message = e.getFaultString();
      throw new SolmrException(message);
    }
    catch (Exception e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of loginPapua method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception loginPapua papua", e);
      throw new SolmrException(e.getMessage());
    }
    
    return utenteAbilitazioni;
    
  }
  
  public MacroCU[] findMacroCUForAttoreInApplication(String codiceAttore)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating  findMacroCUForAttoreInApplication ");
    SolmrLogger.debug(this, "codiceAttore " + codiceAttore);
    
    MacroCU[] arrMacroCu = null;
    try
    {
      arrMacroCu = bindingPapuaWs().findMacroCUForAttoreInApplication(codiceAttore, SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE);
    }
    catch (InternalException e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of findMacroCUForAttoreInApplication method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception findMacroCUForAttoreInApplication papua", e);
      String message = e.getFaultString();
      throw new SolmrException(message);
    }
    catch (Exception e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of findMacroCUForAttoreInApplication method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception findMacroCUForAttoreInApplication papua", e);
      throw new SolmrException(e.getMessage());
    }
    
    return arrMacroCu;
    
  }
  
  
  public boolean verificaGerarchia(long idUtente1, long idUtente2)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating  verificaGerarchia ");
    SolmrLogger.debug(this, "idUtente1 " + idUtente1);
    SolmrLogger.debug(this, "idUtente2 " + idUtente2);
    
    boolean controllo = false;
    try
    {
      controllo = bindingPapuaWs().verificaGerarchia(idUtente1, idUtente2);
    }
    catch (InternalException e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of verificaGerarchia method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception verificaGerarchia papua", e);
      String message = e.getFaultString();
      throw new SolmrException(message);
    }
    catch (Exception e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of verificaGerarchia method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception verificaGerarchia papua", e);
      throw new SolmrException(e.getMessage());
    }
    
    return controllo;    
  }
  
  
  public UtenteAbilitazioni getUtenteAbilitazioniByIdUtenteLogin(long idUtente)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating  getUtenteAbilitazioniByIdUtenteLogin ");
    SolmrLogger.debug(this, "idUtente " + idUtente);
    
    UtenteAbilitazioni utenteAbilitazioni = null;
    try
    {
      utenteAbilitazioni = bindingPapuaWs().getUtenteAbilitazioniByIdUtenteLogin(idUtente);
    }
    catch (InternalException e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of getUtenteAbilitazioniByIdUtenteLogin method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception getUtenteAbilitazioniByIdUtenteLogin papua", e);
      String message = e.getFaultString();
      throw new SolmrException(message);
    }
    catch (Exception e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of getUtenteAbilitazioniByIdUtenteLogin method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception getUtenteAbilitazioniByIdUtenteLogin papua", e);
      throw new SolmrException(e.getMessage());
    }
    
    return utenteAbilitazioni;    
  }
  
  
  public UtenteAbilitazioni[] getUtenteAbilitazioniByIdUtenteLoginRange(long[] idUtente)
      throws SolmrException, Exception
  {
    SolmrLogger.debug(this, "Invocating  getUtenteAbilitazioniByIdUtenteLogin ");
    SolmrLogger.debug(this, "idUtente " + idUtente);
    
    UtenteAbilitazioni[] utenteAbilitazioni = null;
    try
    {
      utenteAbilitazioni = bindingPapuaWs().getUtenteAbilitazioniByIdUtenteLoginRange(idUtente);
    }
    catch (InternalException e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of getUtenteAbilitazioniByIdUtenteLogin method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception getUtenteAbilitazioniByIdUtenteLogin papua", e);
      String message = e.getFaultString();
      throw new SolmrException(message);
    }
    catch (Exception e)
    {
      SolmrLogger.error(this, 
             "Exception during the invocation of getUtenteAbilitazioniByIdUtenteLogin method in SianBean");
      SolmrLogger.dumpStackTrace(this, "Dump exception getUtenteAbilitazioniByIdUtenteLogin papua", e);
      throw new SolmrException(e.getMessage());
    }
    
    return utenteAbilitazioni;    
  }
  
  
  
  /*public String loginProva(InvioPostaCertificata invioPosta)
      throws ServiceSystemException, SystemException, Exception
  {
    SolmrLogger.debug(this, "Invocating  serviceInviaPostaCertificata ");
    SolmrLogger.debug(this, "invioPosta " + invioPosta);
    SolmrLogger.debug(this, "\n\n\n\n\n\n\n");
    
    String valResp = "";
   
    InvioPostaCertificataResponse  risposta = null;
    try
    {
      
      
      //iSWSReturnDett = anagraficaDettaglio(cuaa, sianCredenzialiVO);
      //risposta = bindingPapuaWs().login(codiceFiscale, cognome, nome, livelloAutenticazione, idProcedimento, codiceRuolo)
      //bindingPapuaWs().findRuoliForPersonaInApplicazione(codiceFiscale, livelloAutenticazione, idProcedimento);
      //livello autenticazione lo prendo da identita.
      
      //in sessione al posto di ruoloUtenza
      //RuoloUtenzaPapua prova = new RuoloUtenzaPapua(UtenteAbilitazioni);
       * utenteAbilitazioni.getEnteAppartenenza().getIntermediario().g
      
      
      UtenteAbilitazioni utente = new UtenteAbilitazioni();
      utente.get
      
      
      if((risposta != null) && (risposta.getResult() != null)
          && (risposta.getResult().getEsito() != null))
      {
        SolmrLogger.debug(this, "\n\n\n\n\n\n\n");
        SolmrLogger.debug(this, "risposta.getResult().getEsito().getCodice() "
            + risposta.getResult().getEsito().getCodice());
        SolmrLogger.debug(this, "risposta.getResult().getEsito().getMessaggio() "
            + risposta.getResult().getEsito().getMessaggio());
        SolmrLogger.debug(this, "\n\n\n\n\n\n\n");
      }
    }
    catch (Exception e)
    {
      // Non faccio nulla. iSWSReturnDett resta null e il metodo
      // aggiornaSIANAziendaTributaria() lo gestisce scrivendo un record
      // appropriato su db.
    }
    
    
    
    return valResp;
    
  }*/
  
  
  

}
