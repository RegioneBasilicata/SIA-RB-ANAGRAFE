package it.csi.solmr.business.anag.sian;

import it.csi.csi.wrapper.SystemException;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.MacroCU;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.Ruolo;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.sian.ricercaAnagrafica.Risposta_RichiestaRispostaSincrona_ricercaAnagraficaAll;
import it.csi.smranag.smrgaa.dto.ws.cciaa.UvResponseCCIAA;
import it.csi.smrcomms.siapcommws.InvioPostaCertificata;
import it.csi.solmr.dto.DoubleStringcodeDescription;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.RespAnagFascicoloVO;
import it.csi.solmr.dto.anag.services.SianAnagTributariaGaaVO;
import it.csi.solmr.dto.anag.sian.SianAnagTributariaVO;
import it.csi.solmr.dto.anag.sian.SianEsitiRicevutiRispostaVO;
import it.csi.solmr.dto.anag.sian.SianEsitoDomandeRispostaVO;
import it.csi.solmr.dto.anag.sian.SianFascicoloResponseVO;
import it.csi.solmr.dto.anag.sian.SianQuoteLatteAziendaVO;
import it.csi.solmr.dto.anag.sian.SianTerritorioVO;
import it.csi.solmr.dto.anag.sian.SianTitoliAggregatiVO;
import it.csi.solmr.dto.anag.sian.SianTitoliMovimentatiVO;
import it.csi.solmr.dto.anag.sian.SianTitoloMovimentazioneRispostaVO;
import it.csi.solmr.dto.anag.sian.SianTitoloRispostaVO;
import it.csi.solmr.dto.anag.sian.SianUtenteVO;
import it.csi.solmr.dto.anag.teramo.ElencoRegistroDiStallaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ServiceSystemException;

import java.util.Vector;

import javax.ejb.Local;

@Local
public interface SianLocal
{
  //Nuovo servizio ricerca anagrafica
  public void aggiornaSIANAziendaTributaria(AnagAziendaVO anagAziendaVO, Risposta_RichiestaRispostaSincrona_ricercaAnagraficaAll risposta) throws SolmrException, Exception;

  public ElencoRegistroDiStallaVO elencoRegistriStalla(String codiceAzienda, String codiceSpecie, String cuaa, String tipo, String ordinamento) throws SolmrException, Exception;

  public SianTerritorioVO[] leggiPianoColturale(String cuaa, StringBuffer annoCampagna) throws SolmrException, Exception;

  public Vector<SianTitoliAggregatiVO> titoliProduttoreAggregati(String cuaa, String campagna) throws SolmrException, Exception;

  public SianTitoloRispostaVO titoliProduttoreConInfoPegni(String cuaa) throws SolmrException, Exception;
  
  public Vector<SianQuoteLatteAziendaVO> quoteLatte(String cuaa, String campagna) throws SolmrException, Exception;

  public SianFascicoloResponseVO trovaFascicolo(String cuaa) throws SolmrException, Exception;

  public StringcodeDescription getSianTipoSpecieByCodiceSpecie(String codiceSpecie) throws Exception, SolmrException;

  public StringcodeDescription getSianTipoSpecieByIdSpecieAnimale(long idTipoSpecieAnimale) throws Exception;

  public SianTitoloMovimentazioneRispostaVO serviceSianMovimentazioneTitoli(String cuaa, String idDocumento, String campagna, String fattispecie, String cedente, SianTitoliMovimentatiVO[] elencoTitoli)
      throws SolmrException, ServiceSystemException, SystemException, Exception;

  public SianEsitiRicevutiRispostaVO serviceSianEsitiRicevuti(String ultimoEsito) throws SolmrException, ServiceSystemException, SystemException, Exception;

  public SianEsitoDomandeRispostaVO serviceSianEsitoDomande() throws SolmrException, ServiceSystemException, SystemException, Exception;

  public void sianAggiornaDatiTributaria(String[] elencoCuaa, SianUtenteVO sianUtenteVO) throws Exception;

  public SianTitoloRispostaVO titoliProduttore(String CUAA, String campagna) throws SolmrException, Exception;

  public DoubleStringcodeDescription[] getListSianTipoOpr(boolean principale, String orderBy[]) throws Exception;

  public String getOrganismoPagatoreFormatted(String codiceSianOpr) throws Exception;

  public Boolean serviceSianAggiornaDatiTributaria(String cuaa, SianUtenteVO sianUtenteVO) throws SolmrException, ServiceSystemException, SystemException, Exception;

  public void serviceSianAggiornaDatiBDN(String CUAA) throws SolmrException, Exception;

  /**
   * Servizi utilizzati da smrgasv STOP
   */
  
  //public SianAnagDettaglioVO anagraficaDettaglioFromDB(String cuaa,boolean orderSocRapp,boolean storicoRappLeg, SianUtenteVO sianUtenteVO) throws SolmrException, Exception;
  
  //public SianAnagDettaglioVO anagraficaDettaglioFromDB(String cuaa, SianUtenteVO sianUtenteVO) throws SolmrException, Exception;
  
  public SianAnagTributariaGaaVO ricercaAnagraficaFromDB(String cuaa, SianUtenteVO sianUtenteVO)
      throws SolmrException, Exception;

  public SianAnagTributariaVO selectDatiAziendaTributaria(String cuaa) throws Exception;
  
  public RespAnagFascicoloVO getRespAnagFascicolo(Long idAzienda) throws SolmrException, Exception;

  /**
   * Servizi che accedono ai WS CCIAA START
   */
  public UvResponseCCIAA elencoUnitaVitatePerCUAA(String cuaa, Long idAzienda, RuoloUtenza ruolo) throws SolmrException, Exception;

  public UvResponseCCIAA elencoUnitaVitatePerParticella(String istat, String sezione, String foglio, String particella, Long idAzienda, RuoloUtenza ruolo) throws SolmrException, Exception;
  
  public void sianAggiornaDatiAlboVigneti(AnagAziendaVO anagAziendaVO) throws SolmrException, Exception;
  /**
   * Servizi che accedono ai WS CCIAA STOP
   */
  
  public StringcodeDescription getDescTipoIscrizioneInpsByCodice(String codiceTipoIscrizioneInps)
      throws Exception;
  
  
  
  /**
   * 
   * 
   * Inzio web service comune
   * 
   */
  public String serviceInviaPostaCertificata(InvioPostaCertificata invioPosta)
      throws SolmrException, Exception;
  /**
   * 
   * 
   * Fine web service comune
   * 
   */
  
  
  /**
   * 
   * 
   * Inzio web service papua
   * 
   */
  public Ruolo[] findRuoliForPersonaInApplicazione(String codiceFiscale, int livelloAutenticazione)
      throws SolmrException, Exception;
  
  public UtenteAbilitazioni loginPapua(String codiceFiscale, String cognome, 
      String nome, int livelloAutenticazione, String codiceRuolo)
      throws SolmrException, Exception;
  
  public MacroCU[] findMacroCUForAttoreInApplication(String codiceAttore)
      throws SolmrException, Exception;
  
  public boolean verificaGerarchia(long idUtente1, long idUtente2)
      throws SolmrException, Exception;
  
  public UtenteAbilitazioni getUtenteAbilitazioniByIdUtenteLogin(long idUtente)
      throws SolmrException, Exception;
  
  public UtenteAbilitazioni[] getUtenteAbilitazioniByIdUtenteLoginRange(long[] idUtente)
      throws SolmrException, Exception;
  /**
   * 
   * 
   * Fine web service papua
   */
  
  
}
