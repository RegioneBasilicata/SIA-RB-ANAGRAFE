package it.csi.smranag.smrgaa.business;

import it.csi.papua.papuaserv.dto.legacy.axis.RuoloUtenzaPapua;
import it.csi.papua.papuaserv.dto.messaggistica.DettagliMessaggio;
import it.csi.papua.papuaserv.dto.messaggistica.ListaMessaggi;
import it.csi.papua.papuaserv.exception.messaggistica.LogoutException;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.sigmater.sigtersrv.dto.daticatastali.DatiIdentificazioneTerreno;
import it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioFabbricato;
import it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioTerreno;
import it.csi.sigmater.sigtersrv.dto.daticatastali.SoggettoFisico;
import it.csi.sigmater.sigtersrv.dto.daticatastali.SoggettoGiuridico;
import it.csi.sigmater.sigtersrv.dto.daticatastali.TerrenoRid;
import it.csi.sigmater.sigtersrv.dto.daticatastali.Titolarita;
import it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO;
import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.RitornoAgriservUvVO;
import it.csi.smranag.smrgaa.dto.RitornoAgriservVO;
import it.csi.smranag.smrgaa.dto.RitornoPraticheCCAgriservVO;
import it.csi.smranag.smrgaa.dto.TipoFirmaVO;
import it.csi.smranag.smrgaa.dto.abio.OperatoreBiologicoVO;
import it.csi.smranag.smrgaa.dto.abio.PosizioneOperatoreVO;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoAcquaLavaggio;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoBioVO;
import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoVO;
import it.csi.smranag.smrgaa.dto.allevamenti.ControlloAllevamenti;
import it.csi.smranag.smrgaa.dto.allevamenti.EsitoControlloAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAnimStab;
import it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoDestinoAcquaLavaggio;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoMungitura;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoSottoCategoriaAnimale;
import it.csi.smranag.smrgaa.dto.allevamenti.TipoTrattamento;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaColVarExcelVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaSezioniVO;
import it.csi.smranag.smrgaa.dto.anagrafe.AziendaUmaExcelVO;
import it.csi.smranag.smrgaa.dto.anagrafe.GruppoGreeningVO;
import it.csi.smranag.smrgaa.dto.anagrafe.PlSqlCalcoloOteVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoAttivitaOteVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoDimensioneAziendaVO;
import it.csi.smranag.smrgaa.dto.anagrafe.TipoInfoAggiuntivaVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.AcquaExtraVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteCesAcqVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteStocExtVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.RefluoEffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoAcquaAgronomicaVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoCausaleEffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoEffluenteVO;
import it.csi.smranag.smrgaa.dto.fabbricati.FabbricatoBioVO;
import it.csi.smranag.smrgaa.dto.fascicoli.InvioFascicoliVO;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.manuali.ManualeVO;
import it.csi.smranag.smrgaa.dto.modol.AttributiModuloVO;
import it.csi.smranag.smrgaa.dto.modol.RiepilogoStampaTerrVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AllevamentoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AzAssAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.CCAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.FabbricatoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.IterRichiestaAziendaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.MacchinaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.MotivoRichiestaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.ParticellaAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaDocumentoVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.RichiestaAziendaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.SoggettoAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.StatoRichiestaVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.UnitaMisuraVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.UteAziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.polizze.PolizzaDettaglioVO;
import it.csi.smranag.smrgaa.dto.polizze.PolizzaVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaAziendeCollegateVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaMacchineAgricoleVO;
import it.csi.smranag.smrgaa.dto.search.FiltriRicercaVariazioniAziendaliVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaAziendeCollegateVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaMacchineAgricoleVO;
import it.csi.smranag.smrgaa.dto.search.RigaRicercaVariazioniAziendaliVO;
import it.csi.smranag.smrgaa.dto.sigmater.ParticellaSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.RichiestaSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.SoggettoSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.TitolaritaParticellaSigVO;
import it.csi.smranag.smrgaa.dto.sigmater.TitolaritaSigmaterVO;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.dto.stampe.LogoVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoFirmatarioVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.ValoriCondizionalitaVO;
import it.csi.smranag.smrgaa.dto.terreni.CasoParticolareVO;
import it.csi.smranag.smrgaa.dto.terreni.CatalogoMatriceSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.CatalogoMatriceVO;
import it.csi.smranag.smrgaa.dto.terreni.CompensazioneAziendaVO;
import it.csi.smranag.smrgaa.dto.terreni.ConduzioneEleggibilitaVO;
import it.csi.smranag.smrgaa.dto.terreni.DirittoCompensazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.FaseRiesameDocumentoVO;
import it.csi.smranag.smrgaa.dto.terreni.IsolaParcellaVO;
import it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameVO;
import it.csi.smranag.smrgaa.dto.terreni.ParticellaBioVO;
import it.csi.smranag.smrgaa.dto.terreni.ParticellaIstanzaRiesameVO;
import it.csi.smranag.smrgaa.dto.terreni.RegistroPascoloVO;
import it.csi.smranag.smrgaa.dto.terreni.RiepiloghiUnitaArboreaVO;
import it.csi.smranag.smrgaa.dto.terreni.RiepilogoCompensazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoEfaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoEsportazioneDatiVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFaseAllevamentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFonteVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoMetodoIrriguoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPeriodoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPraticaMantenimentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoRiepilogoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.UVCompensazioneVO;
import it.csi.smranag.smrgaa.dto.uma.PossessoMacchinaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoCategoriaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoFormaPossessoGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoGenereMacchinaGaaVO;
import it.csi.smranag.smrgaa.dto.uma.TipoMacchinaGaaVO;
import it.csi.smranag.smrgaa.dto.ws.ResponseWsBridgeVO;
import it.csi.smranag.smrgaa.util.FileUtils;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.smranag.smrgaa.ws.sianfa.SianEsito;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellDocumentoDaAggiornareVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoFolderVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoIdDocVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellMetadatoVO;
import it.csi.smruma.umaserv.dto.AssegnazioneVO;
import it.csi.smruma.umaserv.dto.DittaUmaVO;
import it.csi.solmr.business.anag.AnagAziendaLocal;
import it.csi.solmr.business.anag.ConsistenzaLocal;
import it.csi.solmr.business.anag.DocumentoLocal;
import it.csi.solmr.business.anag.sian.SianLocal;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagParticellaExcelVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.fabbricati.TipoFormaFabbricatoVO;
import it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO;
import it.csi.solmr.dto.anag.sian.SianAllevamentiVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoParticellaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.dto.uma.MacchinaVO;
import it.csi.solmr.dto.uma.UtilizzoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;
import it.csi.util.performance.StopWatch;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.InitialContext;
import javax.naming.NamingException;

//import com.lowagie.text.pdf.PdfReader;
/*import org.apache.pdfbox.PDFReader;
import org.apache.pdfbox.pdmodel.interactive.form.PDXFA;

import com.lowagie.text.pdf.XfaForm;
import com.lowagie.text.pdf.parser.ImageRenderInfo;
import com.lowagie.text.pdf.parser.PdfReaderContentParser;
import com.lowagie.text.pdf.parser.RenderListener;
import com.lowagie.text.pdf.parser.TextRenderInfo;*/

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 1.0
 */

@Stateless(name=GaaFacadeBean.jndiName,mappedName=GaaFacadeBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class GaaFacadeBean implements GaaFacadeLocal
{

  /**
   * Serial version UID
   */
  private static final long serialVersionUID = -8121352800174054756L;
  public static final String jndiName="comp/env/solmr/gaa/GaaFacade";

  SessionContext sessionContext;

  @EJB
  private transient AgriservLocal agriservLocal = null;
  @EJB
  private transient DecodificheLocal decodificheLocal = null;
  @EJB
  private transient AllevamentoLocal allevamentoLocal = null;
  @EJB
  private transient FabbricatoGaaLocal fabbricatoGaaLocal = null;
  @EJB
  private transient Comunicazione10RGaaLocal comunicazione10RGaaLocal = null;
  @EJB
  private transient AnagrafeGaaLocal anagrafeGaaLocal = null;
  @EJB
  private transient SigmaterLocal sigmaterLocal = null;
  @EJB
  private transient SigmaterGaaLocal sigmaterGaaLocal = null;
  @EJB
  private transient MessaggisticaGaaLocal messaggisticaGaaLocal = null;
  @EJB
  private transient WsBridgeGaaLocal wsBridgeGaaLocal = null;
  @EJB
  private transient AbioGaaLocal abioLocal = null;
  @EJB
  private transient DocumentoGaaLocal documentoGaaLocal = null;
  @EJB
  private transient TerreniGaaLocal terreniGaaLocal = null;
  @EJB
  private transient StampaGaaLocal stampaGaaLocal = null;
  @EJB
  private transient PolizzaGaaLocal polizzaGaaLocal = null;
  @EJB
  private transient ManualeGaaLocal manualeGaaLocal = null;
  @EJB
  private transient ConsistenzaGaaLocal consistenzaGaaLocal = null;
  @EJB
  private transient NuovaIscrizioneGaaLocal nuovaIscrizioneGaaLocal = null;
  @EJB
  private transient AnagAziendaLocal anagAziendaLocal = null;
  @EJB
  private transient AgriWellGaaLocal agriWellGaaLocal = null;
  @EJB
  private transient DocumentoLocal documentoLocal = null;
  @EJB
  private transient UmaServGaaLocal umaServGaaLocal = null;
  @EJB
  private transient ModolServGaaLocal modolServGaaLocal = null;
  @EJB
  private transient ConsistenzaLocal consistenzaLocal = null;
  @EJB
  private transient MacchineAgricoleGaaLocal macchineAgricoleGaaLocal = null;
  @EJB
  private transient SianLocal sianLocal = null;
  @EJB
  private transient MarcaTemporaleGaaLocal marcaTemporaleGaaLocal = null; 
  @EJB
  private transient SianFascicoloAllLocal sianFascicoloAllLocal = null; 

  @Resource
  public void setSessionContext(SessionContext sessionContext)
  {
      try {
		InitialContext ctx = new InitialContext();
		 this.sessionContext = sessionContext;
	} catch (NamingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  
  /** ******************************************** */
  /** ***** ALLEVAMENTI START ****** */
  /** ******************************************** */
  public TipoSottoCategoriaAnimale[] getTipiSottoCategoriaAnimale(
      long idCategoriaAnimale) throws SolmrException, Exception
  {
    return allevamentoLocal.getTipiSottoCategoriaAnimale(idCategoriaAnimale);
  }

  public TipoSottoCategoriaAnimale getTipoSottoCategoriaAnimale(
      long idSottoCategoriaAnimale) throws SolmrException, Exception
  {
    return allevamentoLocal
        .getTipoSottoCategoriaAnimale(idSottoCategoriaAnimale);
  }

  public TipoMungitura[] getTipiMungitura() throws SolmrException,
      Exception
  {
    return allevamentoLocal.getTipiMungitura();
  }

  public Vector<SottoCategoriaAllevamento> getTipiSottoCategoriaAllevamento(
      long idAllevamento) throws SolmrException, Exception
  {
    return allevamentoLocal.getTipiSottoCategoriaAllevamento(idAllevamento);
  }

  public Vector<StabulazioneTrattamento> getStabulazioni(long idAllevamento,
      boolean modifica) throws SolmrException, Exception
  {
    return allevamentoLocal.getStabulazioni(idAllevamento, modifica);
  }

  public BaseCodeDescription[] getTipiStabulazione(long idSottoCategoriaAnimale)
      throws SolmrException, Exception
  {
    return allevamentoLocal.getTipiStabulazione(idSottoCategoriaAnimale);
  }

  public Vector<TipoTrattamento> getTipiTrattamento() throws SolmrException,
      Exception
  {
    return allevamentoLocal.getTipiTrattamento();
  }

  public TipoTrattamento getTipoTrattamento(long idTrattamento)
      throws SolmrException, Exception
  {
    return allevamentoLocal.getTipoTrattamento(idTrattamento);
  }

  public SottoCategoriaAnimStab getSottoCategoriaAnimStab(
      long idSottoCategoriaAnimale, boolean palabile, long idStabulazione)
      throws SolmrException, Exception
  {
    return allevamentoLocal.getSottoCategoriaAnimStab(idSottoCategoriaAnimale,
        palabile, idStabulazione);
  }

  public Vector<AllevamentoVO> getElencoAllevamentiExcel(long idAzienda,
      Date dataInserimentiDich, Long idUte) throws SolmrException,
      Exception
  {
    return allevamentoLocal.getElencoAllevamentiExcel(idAzienda,
        dataInserimentiDich, idUte);
  }

  public Vector<AllevamentoBioVO> getAllevamentiBio(
      Date dataInserimentoDichiarazione, long idAllevamento)
      throws SolmrException, Exception
  {
    return allevamentoLocal.getAllevamentiBio(dataInserimentoDichiarazione,
        idAllevamento);
  }

  public Vector<TipoDestinoAcquaLavaggio> getElencoDestAcquaLavaggio()
      throws SolmrException, Exception
  {
    return allevamentoLocal.getElencoDestAcquaLavaggio();
  }

  public Vector<AllevamentoAcquaLavaggio> getElencoAllevamentoAcquaLavaggio(
      long idAllevamento) throws SolmrException, Exception
  {
    return allevamentoLocal.getElencoAllevamentoAcquaLavaggio(idAllevamento);
  }

  public Vector<Long> getElencoSpecieAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    return allevamentoLocal.getElencoSpecieAzienda(idAzienda);
  }

  /*
   * public HashMap<Long,BigDecimal> getAllevamentoTotAcquaLavaggio(long
   * idAzienda) throws SolmrException, Exception { return
   * allevamentoLocal.getAllevamentoTotAcquaLavaggio(idAzienda); }
   */

  public Vector<AllevamentoVO> getElencoAllevamentiSian(String cuaa,
      long idTipoSpecie, String codAziendaZoo) throws SolmrException,
      Exception
  {
    return allevamentoLocal.getElencoAllevamentiSian(cuaa, idTipoSpecie,
        codAziendaZoo);
  }

  public Vector<EsitoControlloAllevamento> getElencoEsitoControlloAllevamento(
      long idAllevamento) throws SolmrException, Exception
  {
    return allevamentoLocal.getElencoEsitoControlloAllevamento(idAllevamento);
  }

  public HashMap<Long, ControlloAllevamenti> getEsitoControlliAllevamentiAzienda(
      long idAzienda) throws SolmrException, Exception
  {
    return allevamentoLocal.getEsitoControlliAllevamentiAzienda(idAzienda);
  }

  public HashMap<Long, ControlloAllevamenti> getSegnalazioniControlliAllevamentiAzienda(
      long idAzienda) throws SolmrException, Exception
  {
    return allevamentoLocal
        .getSegnalazioniControlliAllevamentiAzienda(idAzienda);
  }

  /** ******************************************** */
  /** ***** ALLEVAMENTI END ****** */
  /** ******************************************** */

  /** ******************************************** */
  /** ***** FABBRICATI BEGIN ****** */
  /** ******************************************** */

  public TipoTipologiaFabbricatoVO getInfoTipologiaFabbricato(
      Long idTipologiaFabbricato) throws SolmrException, Exception
  {
    return fabbricatoGaaLocal.getInfoTipologiaFabbricato(idTipologiaFabbricato);
  }

  public TipoFormaFabbricatoVO getTipoFormaFabbricato(Long idFormaFabbricato)
      throws SolmrException, Exception
  {
    return fabbricatoGaaLocal.getTipoFormaFabbricato(idFormaFabbricato);
  }

  public Vector<TipoTipologiaFabbricatoVO> getListTipoFabbricatoStoccaggio()
      throws SolmrException, Exception
  {
    return fabbricatoGaaLocal.getListTipoFabbricatoStoccaggio();
  }

  public FabbricatoBioVO getFabbricatoBio(long idFabbricato, long idAzienda,
      Date dataInserimentoDichiarazione) throws SolmrException
  {
    return fabbricatoGaaLocal.getFabbricatoBio(idFabbricato, idAzienda,
        dataInserimentoDichiarazione);
  }

  public HashMap<Long, String> esisteFabbricatoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda) throws SolmrException
  {
    return fabbricatoGaaLocal.esisteFabbricatoAttivoByConduzioneAndAzienda(
        elencoConduzioni, idAzienda);
  }

  /** ******************************************** */
  /** ***** FABBRICATI END ****** */
  /** ******************************************** */

  /** ******************************************** */
  /** ***** AGRISERV BEGIN ****** */
  /** ******************************************** */

  public RitornoAgriservVO searchPraticheProcedimento(long idParticella,
      Long idAzienda, int tipologiaStati, Long idDichiarazioneConsistenza,
      Long idProcedimento, Long annoCampagna, int tipoOrdinamento)
      throws SolmrException, Exception
  {
    StopWatch stopWatch = null;
    RitornoAgriservVO results = null;
    try
    {
      SolmrLogger.debug(this,
          "[GaaFacadeBean::agriservSearchPraticheProcedimento] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = agriservLocal.searchPraticheProcedimento(idParticella,
          idAzienda, tipologiaStati, idDichiarazioneConsistenza,
          idProcedimento, annoCampagna, tipoOrdinamento);
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] {
          new Parametro("idParticella", idParticella),
          new Parametro("idAzienda", idAzienda),
          new Parametro("tipologiaStati", tipologiaStati),
          new Parametro("idDichiarazioneConsistenza",
              idDichiarazioneConsistenza),
          new Parametro("idProcedimento", idProcedimento),
          new Parametro("annoCampagna", annoCampagna),
          new Parametro("tipoOrdinamento", tipoOrdinamento) };
      LoggerUtils.logEJBError(this,
          "[GaaFacadeBean::agriservSearchPraticheProcedimento]", t, null,
          parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("GaaFacadeBean",
            "agriservSearchPraticheProcedimento", "Metodo della facade",
            "# ritorno: " + (results != null ? "null" : "non null"));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[GaaFacadeBean::agriservSearchPraticheProcedimento] END.");
    }
  }

  @SuppressWarnings("null")
  public BaseCodeDescription[] baseDecodeUtilizzoByIdRange(long ids[])
      throws SolmrException
  {
    StopWatch stopWatch = null;
    BaseCodeDescription[] results = null;
    try
    {
      SolmrLogger.debug(this,
          "[GaaFacadeBean::baseDecodeUtilizzoByIdRange] BEGIN.");
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();
      results = decodificheLocal.baseDecodeUtilizzoByIdRange(ids);
      return results;
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[] { new Parametro("ids", ids) };
      LoggerUtils.logEJBError(this,
          "[GaaFacadeBean::baseDecodeUtilizzoByIdRange]", e, null, parametri);
      throw new SolmrException(e.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed(
            "GaaFacadeBean",
            "agriservSearchPraticheProcedimento",
            "Metodo della facade",
            (results != null ? "ritorno: null" : "# ritorno: "
                + String.valueOf(results.length)));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[GaaFacadeBean::baseDecodeUtilizzoByIdRange] END.");
    }
  }

  public RitornoPraticheCCAgriservVO searchPraticheContoCorrente(
      long[] idContoCorrente, int tipologiaStati,
      Long idDichiarazioneConsistenza, Long idProcedimento, Long annoCampagna,
      int tipoOrdinamento) throws SolmrException, Exception
  {
    StopWatch stopWatch = null;
    RitornoPraticheCCAgriservVO results = null;
    try
    {
      SolmrLogger.debug(this,
          "[GaaFacadeBean::searchPraticheContoCorrente] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = agriservLocal.searchPraticheContoCorrente(idContoCorrente,
          tipologiaStati, idDichiarazioneConsistenza, idProcedimento,
          annoCampagna, tipoOrdinamento);
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] {
          new Parametro("idParticella", idContoCorrente),
          new Parametro("tipologiaStati", tipologiaStati),
          new Parametro("idDichiarazioneConsistenza",
              idDichiarazioneConsistenza),
          new Parametro("idProcedimento", idProcedimento),
          new Parametro("annoCampagna", annoCampagna),
          new Parametro("tipoOrdinamento", tipoOrdinamento) };
      LoggerUtils.logEJBError(this,
          "[GaaFacadeBean::searchPraticheContoCorrente]", t, null, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("GaaFacadeBean", "searchPraticheContoCorrente",
            "Metodo della facade", "# ritorno: "
                + (results != null ? "null" : "non null"));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this,
          "[GaaFacadeBean::searchPraticheContoCorrente] END.");
    }
  }

  public RitornoAgriservUvVO existPraticheEstirpoUV(long[] idParticella,
      Long idAzienda, Long idDichiarazioneConsistenza, Long idProcedimento,
      Long annoCampagna, int tipoOrdinamento) throws SolmrException,
      Exception
  {
    StopWatch stopWatch = null;
    RitornoAgriservUvVO results = null;
    try
    {
      SolmrLogger.debug(this, "[GaaFacadeBean::existPraticheEstirpoUV] BEGIN.");
      // Stopwatch
      stopWatch = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      stopWatch.start();

      // Richiamo Logica di business
      results = agriservLocal.existPraticheEstirpoUV(idParticella, idAzienda,
          idDichiarazioneConsistenza, idProcedimento, annoCampagna,
          tipoOrdinamento);
      return results;
    }
    catch (Throwable t)
    {
      Parametro parametri[] = new Parametro[] {
          new Parametro("idParticella", idParticella),
          new Parametro("idAzienda", idAzienda),
          new Parametro("idDichiarazioneConsistenza",
              idDichiarazioneConsistenza),
          new Parametro("idProcedimento", idProcedimento),
          new Parametro("annoCampagna", annoCampagna),
          new Parametro("tipoOrdinamento", tipoOrdinamento) };
      LoggerUtils.logEJBError(this, "[GaaFacadeBean::existPraticheEstirpoUV]",
          t, null, parametri);
      throw new Exception(t.getMessage());
    }
    finally
    {
      try
      {
        stopWatch.dumpElapsed("GaaFacadeBean", "existPraticheEstirpoUV",
            "Metodo della facade", "# ritorno: "
                + (results != null ? "null" : "non null"));
        stopWatch.stop();
      }
      catch (Exception ex)
      {
        // Nessuna operazione
      }
      SolmrLogger.debug(this, "[GaaFacadeBean::existPraticheEstirpoUV] END.");
    }
  }

  /** ******************************************** */
  /** ***** AGRISERV END ****** */
  /** ******************************************** */

  /******************************************************************/
  /******************** COMUNICAZIONE 10R BEGIN **********************/
  /******************************************************************/

  public Comunicazione10RVO getComunicazione10RByIdUteAndPianoRifererimento(
      long idUte, long idPianoRiferimento) throws SolmrException,
      Exception
  {

    return comunicazione10RGaaLocal
        .getComunicazione10RByIdUteAndPianoRifererimento(idUte,
            idPianoRiferimento);
  }

  public Vector<EffluenteVO> getListEffluenti(long idComunicazione10R)
      throws SolmrException, Exception
  {
    long id[] = { idComunicazione10R };
    return comunicazione10RGaaLocal.getListEffluenti(id);
  }

  public Vector<EffluenteVO> getListEffluentiStampa(long idComunicazione10R[])
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getListEffluentiStampa(idComunicazione10R);
  }

  public Vector<EffluenteVO> getListEffluenti(long idComunicazione10R[])
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getListEffluenti(idComunicazione10R);
  }

  public Vector<TipoCausaleEffluenteVO> getListTipoCausaleEffluente()
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getListTipoCausaleEffluente();
  }

  public Vector<TipoEffluenteVO> getListTipoEffluente() throws SolmrException,
      Exception
  {
    return comunicazione10RGaaLocal.getListTipoEffluente();
  }

  public TipoEffluenteVO getTipoEffluenteById(long idEffluente)
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getTipoEffluenteById(idEffluente);
  }

  public Vector<TipoAcquaAgronomicaVO> getListTipoAcquaAgronomica()
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getListTipoAcquaAgronomica();
  }

  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquByidComunicazione(
      long idComunicazione10R, Long idTipoCausale) throws SolmrException,
      Exception
  {
    long id[] = { idComunicazione10R };
    return comunicazione10RGaaLocal.getListEffluentiCessAcquByidComunicazione(
        id, idTipoCausale);
  }

  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquPerStampa(
      long idComunicazione10R[]) throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal
        .getListEffluentiCessAcquPerStampa(idComunicazione10R);
  }

  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquByidComunicazione(
      long idComunicazione10R[], Long idTipoCausale) throws SolmrException,
      Exception
  {
    return comunicazione10RGaaLocal.getListEffluentiCessAcquByidComunicazione(
        idComunicazione10R, idTipoCausale);
  }

  public Vector<EffluenteStocExtVO> getListStoccaggiExtrAziendali(
      long idComunicazione10R) throws SolmrException, Exception
  {
    long id[] = { idComunicazione10R };
    return comunicazione10RGaaLocal.getListStoccaggiExtrAziendali(id);
  }

  public Vector<EffluenteStocExtVO> getListStoccaggiExtrAziendali(
      long idComunicazione10R[]) throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal
        .getListStoccaggiExtrAziendali(idComunicazione10R);
  }

  public Vector<AcquaExtraVO> getListAcquaExtra(long idComunicazione10R)
      throws SolmrException, Exception
  {
    long id[] = { idComunicazione10R };
    return comunicazione10RGaaLocal.getListAcquaExtra(id);
  }

  public Vector<AcquaExtraVO> getListAcquaExtra(long idComunicazione10R[])
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getListAcquaExtra(idComunicazione10R);
  }

  public PlSqlCodeDescription storicizzaComunicazione10R(long idUtenteAggiornamento,
      long idAzienda, Vector<Comunicazione10RVO> vCom10r,
      Vector<EffluenteVO> vEffluentiTratt,
      Vector<EffluenteCesAcqVO> vCessioniAcquisizioni,
      Vector<EffluenteCesAcqVO> vCessioni,
      Vector<EffluenteStocExtVO> vStoccaggi, Vector<AcquaExtraVO> vAcqueExtra)
      throws SolmrException, Exception
  {

    PlSqlCodeDescription plSql = comunicazione10RGaaLocal
        .storicizzaComunicazione10R(idUtenteAggiornamento, idAzienda, vCom10r,
            vEffluentiTratt, vCessioniAcquisizioni, vCessioni, vStoccaggi,
            vAcqueExtra);


    return plSql;
  }

  public boolean hasEffluenteProdotto(long idEffluente, long idUte)
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.hasEffluenteProdotto(idEffluente, idUte);
  }

  public Comunicazione10RVO[] getComunicazione10RByPianoRifererimento(
      long idAzienda, long idPianoRiferimento) throws SolmrException,
      Exception
  {
    return comunicazione10RGaaLocal.getComunicazione10RByPianoRifererimento(
        idAzienda, idPianoRiferimento);
  }

  public PlSqlCodeDescription controlloQuantitaEffluentePlSql(long idUte,
      long idEffluente) throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.controlloQuantitaEffluentePlSql(idUte,
        idEffluente);
  }

  public PlSqlCodeDescription calcolaQuantitaAzotoPlSql(long idUte,
      long idComunicazione10r, long idCausaleEffluente, long idEffluente,
      BigDecimal quantita) throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.calcolaQuantitaAzotoPlSql(idUte,
        idComunicazione10r, idCausaleEffluente, idEffluente, quantita);
  }

  public PlSqlCodeDescription ricalcolaPlSql(long idAzienda, long idUtente)
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.ricalcolaPlSql(idAzienda, idUtente);
  }

  public Vector<BigDecimal> getSommaEffluentiCessAcquPerStampa(
      long idComunicazione10R) throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal
        .getSommaEffluentiCessAcquPerStampa(idComunicazione10R);
  }

  public BigDecimal getSommaEffluenti10RPerStampa(long idComunicazione10R,
      boolean palabile) throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getSommaEffluenti10RPerStampa(
        idComunicazione10R, palabile);
  }

  public PlSqlCodeDescription calcolaM3EffluenteAcquisitoPlSql(long idAzienda,
      long idAziendaCess, long idCausaleEffluente, long idEffluente)
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.calcolaM3EffluenteAcquisitoPlSql(idAzienda,
        idAziendaCess, idCausaleEffluente, idEffluente);
  }

  public boolean controlloRefluoPascolo(long idUte) throws SolmrException,
      Exception
  {
    return comunicazione10RGaaLocal.controlloRefluoPascolo(idUte);
  }

  public Vector<TipoEffluenteVO> getListTipoEffluenteTrattamenti(long idUte)
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getListTipoEffluenteTrattamenti(idUte);
  }

  public Vector<TipoEffluenteVO> getListTipoEffluenteByLegameId(long idEffluente)
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getListTipoEffluenteByLegameId(idEffluente);
  }

  public Vector<TipoEffluenteVO> getListTipoEffluenteAndValueByLegameId(
      long idComunicazione10R, long idEffluente) throws SolmrException,
      Exception
  {
    return comunicazione10RGaaLocal.getListTipoEffluenteAndValueByLegameId(
        idComunicazione10R, idEffluente);
  }

  public Vector<EffluenteVO> getListTrattamenti(long idComunicazione10R[])
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getListTrattamenti(idComunicazione10R);
  }

  public Vector<EffluenteVO> getListTrattamenti(long idComunicazione10R)
      throws SolmrException, Exception
  {
    long id[] = { idComunicazione10R };
    return comunicazione10RGaaLocal.getListTrattamenti(id);
  }

  public Vector<EffluenteVO> getListEffluentiNoTratt(long idComunicazione10R[])
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getListEffluentiNoTratt(idComunicazione10R);
  }

  public Vector<EffluenteVO> getListEffluentiNoTratt(long idComunicazione10R)
      throws SolmrException, Exception
  {
    long id[] = { idComunicazione10R };
    return comunicazione10RGaaLocal.getListEffluentiNoTratt(id);
  }

  public Vector<RefluoEffluenteVO> getRefluiComunocazione10r(long idUte,
      long idComunicazione10r, Date dataInserimentoDichiarazione)
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getRefluiComunocazione10r(idUte,
        idComunicazione10r, dataInserimentoDichiarazione);
  }

  public Vector<RefluoEffluenteVO> getListRefluiStampa(
      long idComunicazione10R[]) throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.getListRefluiStampa(idComunicazione10R);
  }

  public PlSqlCodeDescription calcolaVolumePioggeM3PlSql(long idUte)
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.calcolaVolumePioggeM3PlSql(idUte);
  }

  public PlSqlCodeDescription calcolaAcqueMungituraPlSql(long idUte)
      throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal.calcolaAcqueMungituraPlSql(idUte);
  }

  public PlSqlCodeDescription calcolaCesAcquisizionePlSql(
      long idComunicazione10r) throws SolmrException, Exception
  {
    return comunicazione10RGaaLocal
        .calcolaCesAcquisizionePlSql(idComunicazione10r);
  }

  /*******************************************************************/
  /******************* COMUNICAZIONE 10R END *************************/
  /*******************************************************************/

  /*******************************************************************/
  /******************* ANAGRAFE START *************************/
  /*******************************************************************/

  public PlSqlCalcoloOteVO calcolaIneaPlSql(long idAzienda)
      throws SolmrException, Exception
  {
    return anagrafeGaaLocal.calcolaIneaPlSql(idAzienda);
  }

  public PlSqlCalcoloOteVO calcolaUluPlSql(long idAzienda, Long idUde)
      throws SolmrException, Exception
  {
    return anagrafeGaaLocal.calcolaUluPlSql(idAzienda, idUde);
  }

  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAzienda(
      long idAzienda) throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getListActiveAziendaAtecoSecByIdAzienda(idAzienda);
  }
  
  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAziendaAndValid(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getListActiveAziendaAtecoSecByIdAziendaAndValid(idAzienda, idDichiarazioneConsistenza);
  }
  
  public CodeDescription getAttivitaAtecoAllaValid(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getAttivitaAtecoAllaValid(idAzienda, idDichiarazioneConsistenza);
  }

  public Vector<TipoDimensioneAziendaVO> getListActiveTipoDimensioneAzienda()
      throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getListActiveTipoDimensioneAzienda();
  }

  public TipoAttivitaOteVO getTipoAttivitaOteByPrimaryKey(long idAttivitaOte)
      throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getTipoAttivitaOteByPrimaryKey(idAttivitaOte);
  }

  public long[] ricercaIdAziendeCollegate(
      FiltriRicercaAziendeCollegateVO filtriRicercaAziendeCollegateVO)
      throws SolmrException, Exception
  {
    return anagrafeGaaLocal
        .ricercaIdAziendeCollegate(filtriRicercaAziendeCollegateVO);
  }

  public RigaRicercaAziendeCollegateVO[] getRigheRicercaAziendeCollegateByIdAziendaCollegata(
      long ids[]) throws SolmrException, Exception
  {
    return anagrafeGaaLocal
        .getRigheRicercaAziendeCollegateByIdAziendaCollegata(ids);
  }

  public int ricercaNumVariazioni(
      FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza) throws SolmrException, Exception
  {
    return anagrafeGaaLocal.ricercaNumVariazioni(
        filtriRicercaVariazioniAziendaliVO, utenteAbilitazioni, ruoloUtenza);
  }

  public void insertVisioneVariazione(Vector<Long> elencoIdPresaVisione,
      RuoloUtenza ruoloUtenza) throws SolmrException
  {
    anagrafeGaaLocal.insertVisioneVariazione(elencoIdPresaVisione, ruoloUtenza);
  }

  public RigaRicercaVariazioniAziendaliVO[] getRigheRicercaVariazioni(
      FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza, boolean excel) throws SolmrException,
      Exception
  {
    return anagrafeGaaLocal.getRigheRicercaVariazioni(
        filtriRicercaVariazioniAziendaliVO, utenteAbilitazioni, ruoloUtenza, excel);
  }

  public RigaRicercaVariazioniAziendaliVO[] getRigheVariazioniVisione(
      Vector<Long> elencoIdPresaVisione,
      FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO)
      throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getRigheVariazioniVisione(elencoIdPresaVisione,
        filtriRicercaVariazioniAziendaliVO);
  }

  public boolean isPresaVisione(Vector<Long> elencoIdPresaVisione,
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception
  {
    return anagrafeGaaLocal.isPresaVisione(elencoIdPresaVisione, ruoloUtenza);
  }

  public BigDecimal[] getTOTSupCondottaAndSAU(long idAzienda)
      throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getTOTSupCondottaAndSAU(idAzienda);
  }

  public HashMap<Long, DelegaVO> getDelegaAndIntermediario(long ids[])
      throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getDelegaAndIntermediario(ids);
  }

  public boolean isAziendeCollegataFiglia(long idAzienda, long idAziendaSearch)
      throws SolmrException, Exception
  {
    return anagrafeGaaLocal
        .isAziendeCollegataFiglia(idAzienda, idAziendaSearch);
  }

  public boolean isAziendeCollegataMenu(long idAzienda) throws SolmrException,
      Exception
  {
    return anagrafeGaaLocal.isAziendeCollegataMenu(idAzienda);
  }

  public Vector<AziendaUmaExcelVO> getScaricoExcelElencoSociUma(long idAzienda)
      throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getScaricoExcelElencoSociUma(idAzienda);
  }

  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociColturaVarieta(
      long idAzienda) throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getScaricoExcelElencoSociColturaVarieta(idAzienda);
  }

  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociFruttaGuscio(
      long idAzienda) throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getScaricoExcelElencoSociFruttaGuscio(idAzienda);
  }

  public boolean isInAziendaProvenienza(long idAzienda) throws SolmrException,
      Exception
  {
    return anagrafeGaaLocal.isInAziendaProvenienza(idAzienda);
  }

  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntive(long idAzienda)
      throws SolmrException, Exception
  {
    return anagrafeGaaLocal.getTipoInfoAggiuntive(idAzienda);
  }
  
  public Vector<GruppoGreeningVO> getListGruppiGreening(Long idDichConsistenza, Long idAzienda) 
  		throws SolmrException {
  	return anagrafeGaaLocal.getListGruppiGreening(idDichConsistenza, idAzienda);
  }
  
  public PlSqlCodeDescription calcolaGreeningPlSql(long idAzienda, long IdUtente, 
  		Long idDichiarazioneConsistenza) throws SolmrException {
  	return anagrafeGaaLocal.calcolaGreeningPlSql(idAzienda, IdUtente, idDichiarazioneConsistenza);
  }
  
  public PlSqlCodeDescription calcolaEfaPlSql(long idAzienda, Long idDichiarazioneConsistenza,
      long IdUtente) throws SolmrException 
  {
    return anagrafeGaaLocal.calcolaEfaPlSql(idAzienda, idDichiarazioneConsistenza, IdUtente);
  }
  
  public Vector<AziendaSezioniVO> getListActiveAziendaSezioniByIdAzienda(long idAzienda)
      throws SolmrException
  {
    return anagrafeGaaLocal.getListActiveAziendaSezioniByIdAzienda(idAzienda);
  }

  /*******************************************************************/
  /******************* ANAGRAFE END *************************/
  /*******************************************************************/

  /** ******************************************** */
  /** ***** SIGMATER BEGIN ****** */
  /** ******************************************** */
  public DettaglioTerreno cercaTerreno(String codIstatComune,
      String codBelfioreComune, String sezione, String foglio, String numero,
      String subalterno, String progressivo) throws SolmrException,
      Exception
  {
    return sigmaterLocal.cercaTerreno(codIstatComune, codBelfioreComune,
        sezione, foglio, numero, subalterno, progressivo);
  }

  public DettaglioFabbricato cercaFabbricato(String codIstatComune,
      String codBelfioreComune, String sezione, String foglio, String numero,
      String subalterno, String progressivo) throws SolmrException,
      Exception
  {
    return sigmaterLocal.cercaFabbricato(codIstatComune, codBelfioreComune,
        sezione, foglio, numero, subalterno, progressivo);
  }

  public Titolarita[] cercaTitolaritaOggettoCatastale(String codIstatComune,
      String codBelfioreComune, String sezione, String idImmobile,
      String tipoImmobile, String dataDa, String dataA) throws SolmrException,
      Exception
  {
    return sigmaterLocal.cercaTitolaritaOggettoCatastale(codIstatComune,
        codBelfioreComune, sezione, idImmobile, tipoImmobile, dataDa, dataA);
  }

  public void scaricoTitolarita(String codIstat, String cuaa, long idAzienda,
      Date dataDa, Date dataA) throws SolmrException, Exception
  {
    try
    {
      final String ERR_SIGMATER_LOG = "SIGMATER: ";

      if (Validator.isNotEmpty(cuaa))
      {

        Vector<BaseCodeDescription> vComuneSezione = sigmaterGaaLocal
            .getComuneAndSezioneForSigmater(idAzienda);

        // Torna null se nn trova nulla
        SoggettoGiuridico[] soggettoGiuridico = null;
        // Torna null se nn trova nulla
        SoggettoFisico[] soggettoFisico = null;
        String dataDaStr = null;
        String dataAStr = null;
        String codiceEsito = null;
        String descrizioneEsito = null;
        // Memorizzo i dati della chiamata al servizio per il log su DB
        RichiestaSigmaterVO richSigVO = null;
        if (dataDa == null)
        {
          dataDa = new Date();
        }
        if (dataA == null)
        {
          dataA = new Date();
        }
        dataDaStr = DateUtils.formatDate(dataDa);
        dataAStr = DateUtils.formatDate(dataA);
        Date currDate = new Date();
        if (cuaa.length() == 16) // Trattasi di codice fiscale
        {
          richSigVO = new RichiestaSigmaterVO();
          richSigVO.setIdAzienda(new Long(idAzienda));
          // richSigVO.setCodIstat(codIstat);
          richSigVO
              .setNomeServizio(SolmrConstants.SIGMATER_CERCASOGGETTOFISICO);
          codiceEsito = AnagErrors.SIGMATER_CODICE_SERVIZIO_OK;
          descrizioneEsito = null;

          RichiestaSigmaterVO richSigVOTmp = sigmaterGaaLocal
              .getRichiestaSigmater(richSigVO);

          if (vComuneSezione != null)
          {

            for (int i = 0; i < vComuneSezione.size(); i++)
            {

              try
              {
                BaseCodeDescription bcd = vComuneSezione.get(i);
                codIstat = bcd.getDescription();
                soggettoFisico = sigmaterLocal.cercaSoggettoFisico(codIstat,
                    null, null, null, cuaa, null);
                if (soggettoFisico != null)
                {
                  richSigVO.setCodIstat(codIstat);
                  break;
                }
              }
              catch (SolmrException oe)
              {
                soggettoFisico = null;
                codiceEsito = AnagErrors.SIGMATER_CODICE_SERVIZIO_KO_GESTITO;
                descrizioneEsito = getExcMax500Char(oe.getMessage());
              }
              catch (Exception ex)
              { // Se va in errore non blocco niente vado avanti
                soggettoFisico = null;
                codiceEsito = AnagErrors.SIGMATER_CODICE_SERVIZIO_KO_NON_GESTITO;
                descrizioneEsito = getExcMax500Char(ex.getMessage());
              }
            }
          }

          if (richSigVOTmp != null)
          {
            richSigVO = richSigVOTmp;
            richSigVO.setDataUltimaRichiesta(currDate);
            if (Validator.isNotEmpty(soggettoFisico)
                || codiceEsito
                    .equalsIgnoreCase(AnagErrors.SIGMATER_CODICE_SERVIZIO_OK))
            {
              richSigVO.setDataAggiornamento(currDate);
            }
            else
            // andata male la chiamata al servizio
            {
              richSigVO.setDescrizioneEsito(descrizioneEsito);
            }

            richSigVO.setCodiceEsito(codiceEsito);
            // update
            sigmaterGaaLocal.updateRichiestaSigmater(richSigVO);
          }
          else
          {
            richSigVO.setDataUltimaRichiesta(currDate);
            if (Validator.isNotEmpty(soggettoFisico)
                || codiceEsito
                    .equalsIgnoreCase(AnagErrors.SIGMATER_CODICE_SERVIZIO_OK))
            {
              richSigVO.setDataAggiornamento(currDate);
            }
            else
            // andata male la chiamata al servizio
            {
              richSigVO.setDescrizioneEsito(descrizioneEsito);
            }

            richSigVO.setCodiceEsito(codiceEsito);
            // insert
            richSigVO.setIdRichiestaSigmater(sigmaterGaaLocal
                .insertRichiestaSigmater(richSigVO));
          }

        }
        if (cuaa.length() == 11) // Trattasi di codice fiscale
        {

          richSigVO = new RichiestaSigmaterVO();
          richSigVO.setIdAzienda(new Long(idAzienda));
          richSigVO
              .setNomeServizio(SolmrConstants.SIGMATER_CERCASOGGETTOGIURIDICO);
          // richSigVO.setCodIstat(codIstat);
          codiceEsito = AnagErrors.SIGMATER_CODICE_SERVIZIO_OK;
          descrizioneEsito = null;

          RichiestaSigmaterVO richSigVOTmp = sigmaterGaaLocal
              .getRichiestaSigmater(richSigVO);

          if (vComuneSezione != null)
          {

            for (int i = 0; i < vComuneSezione.size(); i++)
            {
              try
              {
                BaseCodeDescription bcd = vComuneSezione.get(i);
                codIstat = bcd.getDescription();
                soggettoGiuridico = sigmaterLocal.cercaSoggettoGiuridico(
                    codIstat, null, cuaa, null, null);
                if (soggettoGiuridico != null)
                {
                  richSigVO.setCodIstat(codIstat);
                  break;
                }
              }
              catch (SolmrException oe)
              {
                soggettoGiuridico = null;
                codiceEsito = AnagErrors.SIGMATER_CODICE_SERVIZIO_KO_GESTITO;
                descrizioneEsito = getExcMax500Char(oe.getMessage());
              }
              catch (Exception ex)
              { // Se va in errore non blocco niente vado avanti
                soggettoGiuridico = null;
                codiceEsito = AnagErrors.SIGMATER_CODICE_SERVIZIO_KO_NON_GESTITO;
                descrizioneEsito = getExcMax500Char(ex.getMessage());
              }
            }
          }

          if (richSigVOTmp != null)
          {
            richSigVO = richSigVOTmp;
            richSigVO.setDataUltimaRichiesta(currDate);
            if (Validator.isNotEmpty(soggettoGiuridico)
                || codiceEsito
                    .equalsIgnoreCase(AnagErrors.SIGMATER_CODICE_SERVIZIO_OK))
            {
              richSigVO.setDataAggiornamento(currDate);
            }
            else
            // andata male la chiamata al servizio
            {
              richSigVO.setDescrizioneEsito(descrizioneEsito);
            }
            richSigVO.setCodiceEsito(codiceEsito);
            // update
            sigmaterGaaLocal.updateRichiestaSigmater(richSigVO);
          }
          else
          {
            richSigVO.setDataUltimaRichiesta(currDate);
            if (Validator.isNotEmpty(soggettoGiuridico)
                || codiceEsito
                    .equalsIgnoreCase(AnagErrors.SIGMATER_CODICE_SERVIZIO_OK))
            {
              richSigVO.setDataAggiornamento(currDate);
            }
            else
            // andata male la chiamata al servizio
            {
              richSigVO.setDescrizioneEsito(descrizioneEsito);
            }
            richSigVO.setCodiceEsito(codiceEsito);
            // insert
            richSigVO.setIdRichiestaSigmater(sigmaterGaaLocal
                .insertRichiestaSigmater(richSigVO));
          }

        }

        if (((Validator.isNotEmpty(soggettoFisico) && (soggettoFisico.length > 0)) || (Validator
            .isNotEmpty(soggettoGiuridico) && (soggettoGiuridico.length > 0)))
            && codiceEsito
                .equalsIgnoreCase(AnagErrors.SIGMATER_CODICE_SERVIZIO_OK))
        {

          // Lavoro sulla tabella DB_SOGGETTO_SIGMATER se le chiamate ai servizi
          // precedenti
          // sono andate correttamente
          SoggettoSigmaterVO soggSigVOTmp = sigmaterGaaLocal
              .getSoggettoSigmater(richSigVO.getIdRichiestaSigmater()
                  .longValue());

          String idSoggetto = null;
          String tipoSoggetto = null;
          String cognome = null;
          String nome = null;
          String denominazione = null;
          String sesso = null;
          Date dataNascita = null;
          String luogoNascita = null;
          String codiceFiscale = null;
          Date dataValidita = null;
          String sede = null;
          if (Validator.isNotEmpty(soggettoFisico)
              && (soggettoFisico.length > 0))
          {
            tipoSoggetto = "P";
            idSoggetto = soggettoFisico[0].getIdSoggetto();
            cognome = soggettoFisico[0].getCognome();
            nome = soggettoFisico[0].getNome();
            sesso = soggettoFisico[0].getSesso();
            try
            {
              dataNascita = DateUtils.parseDate(soggettoFisico[0]
                  .getDataNascita());
            }
            catch (Exception eData)
            {
            }
            luogoNascita = soggettoFisico[0].getLuogoNascita();
            codiceFiscale = soggettoFisico[0].getCodFiscale();
            try
            {
              dataValidita = DateUtils.parseDate(soggettoFisico[0]
                  .getDataValidita());
            }
            catch (Exception eData)
            {
            }
          }
          if (Validator.isNotEmpty(soggettoGiuridico)
              && (soggettoGiuridico.length > 0))
          {
            idSoggetto = soggettoGiuridico[0].getIdSoggetto();
            tipoSoggetto = "G";
            denominazione = soggettoGiuridico[0].getDenominazione();
            codiceFiscale = soggettoGiuridico[0].getCodFiscale();
            try
            {
              dataValidita = DateUtils.parseDate(soggettoGiuridico[0]
                  .getDataValidita());
            }
            catch (Exception eData)
            {
            }
            sede = soggettoGiuridico[0].getSede();
          }

          SoggettoSigmaterVO soggSigVO = new SoggettoSigmaterVO();
          soggSigVO.setIdRichiestaSigmater(richSigVO.getIdRichiestaSigmater());
          soggSigVO.setIdSigmater(new Long(idSoggetto));
          soggSigVO.setTipoSoggetto(tipoSoggetto);
          soggSigVO.setCognome(cognome);
          soggSigVO.setNome(nome);
          soggSigVO.setDenominazione(denominazione);
          soggSigVO.setSesso(sesso);
          soggSigVO.setDataNascita(dataNascita);
          soggSigVO.setLuogoNascita(luogoNascita);
          soggSigVO.setCodiceFiscale(codiceFiscale);
          soggSigVO.setDataAggiornamento(dataValidita);
          soggSigVO.setSede(sede);

          Long idSoggettoSigmater = null;
          if (Validator.isNotEmpty(soggSigVOTmp)) // Esiste già il record
          {
            idSoggettoSigmater = soggSigVOTmp.getIdSoggettoSigmater();
            sigmaterGaaLocal.updateSoggettoSigmater(soggSigVO);
          }
          else
          // inserisco poiche è la prima volta per questo soggetto
          {
            idSoggettoSigmater = sigmaterGaaLocal
                .insertSoggettoSigmater(soggSigVO);
          }

          if (vComuneSezione != null)
          {

            for (int i = 0; i < vComuneSezione.size(); i++)
            {
              Titolarita[] titolarita = null;
              BaseCodeDescription bcd = vComuneSezione.get(i);

              richSigVO = new RichiestaSigmaterVO();
              richSigVO.setIdAzienda(new Long(idAzienda));
              richSigVO
                  .setNomeServizio(SolmrConstants.SIGMATER_CERCATITOLARITASOGGETTOCATASTALE);
              String istatComune = bcd.getDescription();
              richSigVO.setCodIstat(istatComune);
              String sezione = (String) bcd.getItem();
              richSigVO.setSezione(sezione);
              codiceEsito = AnagErrors.SIGMATER_CODICE_SERVIZIO_OK;
              descrizioneEsito = null;
              // Oggetto per loggare la chiamata al sigmater se già presente su
              // DB
              RichiestaSigmaterVO richSigVOTmp = sigmaterGaaLocal
                  .getRichiestaSigmater(richSigVO);

              try
              {
                titolarita = sigmaterLocal.cercaTitolaritaSoggettoCatastale(
                    istatComune, null, sezione, idSoggetto, tipoSoggetto,
                    dataDaStr, dataAStr);
              }
              catch (SolmrException oe)
              {
                titolarita = null;
                codiceEsito = AnagErrors.SIGMATER_CODICE_SERVIZIO_KO_GESTITO;
                descrizioneEsito = getExcMax500Char(oe.getMessage());
              }
              catch (Exception ex)
              { // Se va in errore non blocco niente vado avanti
                titolarita = null;
                codiceEsito = AnagErrors.SIGMATER_CODICE_SERVIZIO_KO_NON_GESTITO;
                descrizioneEsito = getExcMax500Char(ex.getMessage());
              }

              if (richSigVOTmp != null)
              {
                // update
                richSigVO = richSigVOTmp;
                richSigVO.setDataUltimaRichiesta(currDate);
                if (Validator.isNotEmpty(titolarita))
                {
                  richSigVO.setDataAggiornamento(currDate);
                }
                else
                // andata male la chiamata al servizio
                {
                  richSigVO.setDescrizioneEsito(descrizioneEsito);
                }
                richSigVO.setCodiceEsito(codiceEsito);
                sigmaterGaaLocal.updateRichiestaSigmater(richSigVOTmp);
              }
              else
              {
                richSigVO.setDataUltimaRichiesta(currDate);
                // insert
                if (Validator.isNotEmpty(titolarita))
                {
                  richSigVO.setDataAggiornamento(currDate);
                }
                else
                // andata male la chiamata al servizio
                {
                  richSigVO.setDescrizioneEsito(descrizioneEsito);
                }
                richSigVO.setCodiceEsito(codiceEsito);
                richSigVO.setIdRichiestaSigmater(sigmaterGaaLocal
                    .insertRichiestaSigmater(richSigVO));
              }

              if (Validator.isNotEmpty(titolarita)
                  && (titolarita.length > 0)
                  && codiceEsito
                      .equalsIgnoreCase(AnagErrors.SIGMATER_CODICE_SERVIZIO_OK))
              {
                // Lavoro sulla tabella
                // DB_TITOLARITA_SIGMATER,DB_TITOLARITA_PARTICELLA_SIG,
                // DB_PARTICELLA_SIGMATER se le chiamate ai servizi precedenti
                // sono andate correttamente.
                // Cancello nel caso fosse presente già un record sulla tabella
                // DB_TITOLARITA_SIGMATER
                // e DB_TITOLARITA_PARTICELLA_SIG
                boolean esisteAlmenoUnaTitolarita = sigmaterGaaLocal
                    .esisteTitolaritaSigmaterFromIdRichiesta(richSigVO
                        .getIdRichiestaSigmater().longValue());
                if (esisteAlmenoUnaTitolarita)
                {
                  sigmaterGaaLocal
                      .deleteTitolaritaParticellaSigFromPadre(richSigVO
                          .getIdRichiestaSigmater().longValue());
                  sigmaterGaaLocal.deleteTitolaritaSigmater(richSigVO
                      .getIdRichiestaSigmater().longValue());
                }

                for (int k = 0; k < titolarita.length; k++)
                {
                  if (Validator.isNotEmpty(titolarita[k].getOggetto())
                      && "T".equalsIgnoreCase(titolarita[k].getOggetto()
                          .getTipoOggetto()))
                  {
                    // Inserisco il nuovo record su DB_TITOLARITA_SIGMATER
                    TitolaritaSigmaterVO titSigVO = new TitolaritaSigmaterVO();
                    titSigVO.setIdRichiestaSigmater(richSigVO
                        .getIdRichiestaSigmater());
                    titSigVO.setIdSoggettoSigmater(idSoggettoSigmater);
                    Long idDirittoSigmater = null;
                    if (Validator.isNotEmpty(titolarita[k].getCodiceDiritto()))
                    {
                      idDirittoSigmater = sigmaterGaaLocal
                          .getIdTipoDiritto(titolarita[k].getCodiceDiritto());
                    }

                    if (idDirittoSigmater == null)
                    {
                      throw new DataAccessException(
                          ERR_SIGMATER_LOG
                              + "attenzione non è stato trovato per il cuaa-"
                              + cuaa
                              + " la corrispondenza "
                              + "sulla tabella DB_TIPO_DIRITTO_SIGMATER per il codice-"
                              + titolarita[k].getCodiceDiritto());
                    }
                    titSigVO.setIdDirittoSigmater(idDirittoSigmater);
                    titSigVO.setQuotaNumeratore(underscoreToNull(titolarita[k]
                        .getQuotaNumeratore()));
                    titSigVO
                        .setQuotaDenominatore(underscoreToNull(titolarita[k]
                            .getQuotaDenominatore()));
                    titSigVO.setCodiceRegime(underscoreToNull(titolarita[k]
                        .getCodRegime()));
                    titSigVO
                        .setDescrizioneRegime(underscoreToNull(titolarita[k]
                            .getDescRegime()));

                    // inserisco sempre su DB_TITOLARITA_SIGMATER
                    Long idTitolaritaSigmater = sigmaterGaaLocal
                        .insertTitolaritaSigmater(titSigVO);

                    if (Validator.isNotEmpty(titolarita[k].getOggetto()
                        .getTerreno()))
                    {
                      TerrenoRid[] oggCatastale = titolarita[k].getOggetto()
                          .getTerreno();

                      for (int g = 0; g < oggCatastale.length; g++)
                      {
                        // controllo se esiste già il record su
                        // DB_PARTICELLA_SIGMATER
                        // con chiave catastale corrispondente
                        ParticellaSigmaterVO partSigVO = new ParticellaSigmaterVO();
                        partSigVO.setSezione(underscoreToNull(oggCatastale[g]
                            .getSezione()));
                        if (Validator.isNotEmpty(oggCatastale[g]
                            .getDatiIdentificazTerreno()))
                        {
                          DatiIdentificazioneTerreno datiIdTerreno = oggCatastale[g]
                              .getDatiIdentificazTerreno();
                          try
                          {
                            partSigVO.setFoglio(new Long(datiIdTerreno
                                .getFoglio()));
                          }
                          catch (Exception exc)
                          {
                            throw new DataAccessException(
                                ERR_SIGMATER_LOG
                                    + "attenzione non è stato possibile convertire il foglio in numerico "
                                    + "cuaa-" + cuaa + " foglio-"
                                    + datiIdTerreno.getFoglio());
                          }
                          try
                          {
                            partSigVO.setParticella(new Long(datiIdTerreno
                                .getNumero()));
                          }
                          catch (Exception exc)
                          {
                            throw new DataAccessException(
                                ERR_SIGMATER_LOG
                                    + "attenzione non è stato possibile convertire la particella in numerico "
                                    + "cuaa-" + cuaa + " particella-"
                                    + datiIdTerreno.getNumero());
                          }
                          partSigVO
                              .setSubalterno(underscoreToNull(datiIdTerreno
                                  .getSubalterno()));
                        }
                        ParticellaSigmaterVO partSigVOTmp = sigmaterGaaLocal
                            .getParticellaSigmater(partSigVO);

                        if (Validator.isNotEmpty(partSigVOTmp))
                        {
                          partSigVO = partSigVOTmp;
                          if (Validator.isNotEmpty(oggCatastale[g]
                              .getDatiIdentificazTerreno()))
                          {
                            DatiIdentificazioneTerreno datiIdTerreno = oggCatastale[g]
                                .getDatiIdentificazTerreno();
                            partSigVO
                                .setDenominatore(underscoreToNull(datiIdTerreno
                                    .getDenominatore()));
                            partSigVO
                                .setEdificialita(underscoreToNull(datiIdTerreno
                                    .getEdificalita()));
                            sigmaterGaaLocal
                                .updateParticellaSigmater(partSigVO);
                          }
                          TitolaritaParticellaSigVO titPartSigVO = new TitolaritaParticellaSigVO();
                          titPartSigVO
                              .setIdTitolaritaSigmater(idTitolaritaSigmater);
                          titPartSigVO.setIdImmobileSigmater(new Long(
                              oggCatastale[g].getIdImmobile()).longValue());
                          titPartSigVO.setIdParticellaSigmater(partSigVO
                              .getIdParticellaSigmater());
                          // if esiste già perchè doppio sigmater non inserisco
                          boolean giaPresente = sigmaterGaaLocal
                              .esisteTitolaritaParticellaSig(titPartSigVO);
                          if (!giaPresente)
                          {
                            sigmaterGaaLocal
                                .insertTitolaritaParticellaSig(titPartSigVO);
                          }
                        }
                        else
                        {
                          if (Validator.isNotEmpty(oggCatastale[g]
                              .getDatiIdentificazTerreno()))
                          {
                            DatiIdentificazioneTerreno datiIdTerreno = oggCatastale[g]
                                .getDatiIdentificazTerreno();
                            partSigVO
                                .setDenominatore(underscoreToNull(datiIdTerreno
                                    .getDenominatore()));
                            partSigVO
                                .setEdificialita(underscoreToNull(datiIdTerreno
                                    .getEdificalita()));
                          }

                          String istatComunePartSig = sigmaterGaaLocal
                              .getIstatComuneNonEstinfoFromCodFisc(oggCatastale[g]
                                  .getCodBelfioreComune());

                          if (Validator.isEmpty(istatComunePartSig))
                          {
                            throw new DataAccessException(ERR_SIGMATER_LOG
                                + "attenzione non è stato trovato per il cuaa-"
                                + cuaa
                                + " un comune valido per il codiceBelfiore -"
                                + oggCatastale[g].getCodBelfioreComune());
                          }

                          partSigVO.setComune(istatComunePartSig);

                          long idParticellaSigmater = sigmaterGaaLocal
                              .insertParticellaSigmater(partSigVO);
                          TitolaritaParticellaSigVO titPartSigVO = new TitolaritaParticellaSigVO();
                          titPartSigVO
                              .setIdTitolaritaSigmater(idTitolaritaSigmater);
                          titPartSigVO.setIdImmobileSigmater(new Long(
                              oggCatastale[g].getIdImmobile()).longValue());
                          titPartSigVO
                              .setIdParticellaSigmater(idParticellaSigmater);

                          // if esiste già perchè doppio sigmater non inserisco
                          boolean giaPresente = sigmaterGaaLocal
                              .esisteTitolaritaParticellaSig(titPartSigVO);
                          if (!giaPresente)
                          {
                            sigmaterGaaLocal
                                .insertTitolaritaParticellaSig(titPartSigVO);
                          }
                        }

                      }

                    }

                  }
                }
              }

            }
          }

        }

      }
    }
    catch (DataAccessException e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }

  public Long getIdTipoDiritto(String codice) throws SolmrException,
      Exception
  {
    return sigmaterGaaLocal.getIdTipoDiritto(codice);
  }

  public void importaTitolaritaSigmater(long idParticella,
      Titolarita[] titolarita, long idUtente) throws SolmrException,
      Exception
  {
    sigmaterGaaLocal.importaTitolaritaSigmater(idParticella, titolarita,
        idUtente);
  }

  /** ******************************************** */
  /** ***** SIGMATER END ****** */
  /** ******************************************** */

  /** ******************************************** */
  /** ***** ABIO BEGIN ****** */
  /** ******************************************** */

  public OperatoreBiologicoVO getOperatoreBiologicoByIdAzienda(Long idAzienda,
      Date dataInizioAttivita) throws SolmrException, Exception
  {
    return abioLocal.getOperatoreBiologicoByIdAzienda(idAzienda,
        dataInizioAttivita);
  }

  public PosizioneOperatoreVO[] getAttivitaBiologicheByIdAzienda(
      Long idOperatoreBiologico, Date dataFineValidita, boolean checkStorico)
      throws SolmrException, Exception
  {
    return abioLocal.getAttivitaBiologicheByIdAzienda(idOperatoreBiologico,
        dataFineValidita, checkStorico);
  }

  public CodeDescription[] getODCbyIdOperatoreBiologico(
      Long idOperatoreBiologico, Date dataInizioValidita, boolean pianoCorrente)
      throws SolmrException, Exception
  {
    return abioLocal.getODCbyIdOperatoreBiologico(idOperatoreBiologico,
        dataInizioValidita, pianoCorrente);
  }

  public OperatoreBiologicoVO getOperatoreBiologicoAttivo(Long idAzienda)
      throws SolmrException, Exception
  {
    return abioLocal.getOperatoreBiologicoAttivo(idAzienda);
  }

  /** ******************************************** */
  /** ***** ABIO END ****** */
  /** ******************************************** */

  /** ******************************************** */
  /** ***** DCUMENTO BEGIN ****** */
  /** ******************************************** */

  public Vector<BaseCodeDescription> getCategoriaDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException, Exception
  {
    return documentoGaaLocal
        .getCategoriaDocumentiTerritorialiAzienda(idAzienda);
  }

  public Vector<BaseCodeDescription> getDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException, Exception
  {
    return documentoGaaLocal.getDocumentiTerritorialiAzienda(idAzienda);
  }

  public Vector<BaseCodeDescription> getProtocolliDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException, Exception
  {
    return documentoGaaLocal
        .getProtocolliDocumentiTerritorialiAzienda(idAzienda);
  }

  public boolean isDocumentoIstanzaRiesame(long idDocumento)
      throws SolmrException, Exception
  {
    return documentoGaaLocal.isDocumentoIstanzaRiesame(idDocumento);
  }

  public Vector<BaseCodeDescription> getCausaleModificaDocumentoValid()
      throws SolmrException, Exception
  {
    return documentoGaaLocal.getCausaleModificaDocumentoValid();
  }

  public Vector<Long> esisteDocumentoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda) throws SolmrException,
      Exception
  {
    return documentoGaaLocal.esisteDocumentoAttivoByConduzioneAndAzienda(
        elencoConduzioni, idAzienda);
  }

  public Vector<AllegatoDocumentoVO> getElencoFileAllegati(long idDocumento)
      throws SolmrException, Exception
  {
    return documentoGaaLocal.getElencoFileAllegati(idDocumento);
  }

  public void deleteFileAllegato(long idAllegato, long idDocumento)
      throws SolmrException, Exception
  {
    documentoGaaLocal.deleteFileAllegato(idAllegato, idDocumento);
  }

  public void deleteFileAllegatoAgriWell(AllegatoDocumentoVO allegatoDocumentoVO)
      throws SolmrException, Exception
  {
    try
    {
      AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
          .agriwellServiceCancellaDoquiAgri(allegatoDocumentoVO
              .getExtIdDocumentoIndex());

      if (Validator.isNotEmpty(agriWellEsitoVO)
          && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito()
              .intValue())
      {
        documentoGaaLocal.deleteFileAllegato(
            allegatoDocumentoVO.getIdAllegato(),
            allegatoDocumentoVO.getIdDocumento());
      }
      else
      {
        String messaggio = "Attenzione si è verificato un problema nella lettura agriwell ";
        if (Validator.isNotEmpty(agriWellEsitoVO))
        {
          messaggio += "-" + agriWellEsitoVO.getMessaggio();
        }
        throw new SolmrException(messaggio);
      }

    }
    catch (Throwable t)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(t.getMessage());
    }
  }

  public void deleteFileAllegatoRichiesta(long idAllegato,
      long idRichiestaDocumento) throws SolmrException, Exception
  {
    documentoGaaLocal.deleteFileAllegatoRichiesta(idAllegato,
        idRichiestaDocumento);
  }

  public void deleteFileAllegatoNotifica(long idAllegato, long idNotifica)
      throws SolmrException, Exception
  {
    documentoGaaLocal.deleteFileAllegatoNotifica(idAllegato, idNotifica);
  }

  public Long insertFileAllegato(AllegatoDocumentoVO allegatoDocumentoVO)
      throws SolmrException, Exception
  {
    return documentoGaaLocal.insertFileAllegato(allegatoDocumentoVO);
  }

  public Long insertFileAllegatoRichiesta(
      AllegatoDocumentoVO allegatoDocumentoVO) throws SolmrException,
      Exception
  {
    return documentoGaaLocal.insertFileAllegatoRichiesta(allegatoDocumentoVO);
  }

  public Long insertFileAllegatoNotifica(AllegatoDocumentoVO allegatoDocumentoVO)
      throws SolmrException, Exception
  {
    return documentoGaaLocal.insertFileAllegatoNotifica(allegatoDocumentoVO);
  }

  public AllegatoDocumentoVO getFileAllegato(long idAllegato)
      throws SolmrException, Exception
  {
    return documentoGaaLocal.getFileAllegato(idAllegato);
  }

  public Date getFirstDataInserimentoDocumento(long idDocumento)
      throws SolmrException, Exception
  {
    return documentoGaaLocal.getFirstDataInserimentoDocumento(idDocumento);
  }

  public boolean isIstanzaRiesameFotoInterpretataByDocumento(long idDocumento)
      throws SolmrException, Exception
  {
    return documentoGaaLocal
        .isIstanzaRiesameFotoInterpretataByDocumento(idDocumento);
  }

  public boolean exitsOtherDocISForParticellaAndAzienda(long idParticella,
      long idAzienda, Long idDocumento) throws SolmrException, Exception
  {
    return documentoGaaLocal.exitsOtherDocISForParticellaAndAzienda(
        idParticella, idAzienda, idDocumento);
  }

  /*
   * public boolean isFaseIstanzaRiesameEvasa(long idAzienda, int fase,
   * Vector<Long> vIdParticella, String parametro) throws SolmrException,
   * Exception { return
   * documentoGaaLocal.isFaseIstanzaRiesameEvasa(idAzienda, fase, vIdParticella,
   * parametro); }
   */

  public boolean exsitsDocFaseIstanzaRiesameFasePrec(long idAzienda, int fase,
      long idParticella, String parametro) throws SolmrException,
      Exception
  {
    return documentoGaaLocal.exsitsDocFaseIstanzaRiesameFasePrec(idAzienda,
        fase, idParticella, parametro);
  }

  public boolean isPossCreateDocFaseIstanzaRiesameFaseSucc(long idAzienda,
      long idParticella, int idFase, String parametro) throws SolmrException,
      Exception
  {
    return documentoGaaLocal.isPossCreateDocFaseIstanzaRiesameFaseSucc(
        idAzienda, idParticella, idFase, parametro);
  }

  public Date getDateLastBatchIstanzaRiesameOk() throws SolmrException,
      Exception
  {
    return documentoGaaLocal.getDateLastBatchIstanzaRiesameOk();
  }

  public boolean isSitiConvocaValid(long idAzienda, int anno, int fase)
      throws SolmrException, Exception
  {
    return documentoGaaLocal.isSitiConvocaValid(idAzienda, anno, fase);
  }

  public boolean isDataSospensioneScaduta(long idAzienda, long idParticella,
      int anno) throws SolmrException, Exception
  {
    return documentoGaaLocal.isDataSospensioneScaduta(idAzienda, idParticella,
        anno);
  }

  public boolean isParticellaEvasa(long idAzienda, long idParticella, int fase,
      int anno) throws SolmrException, Exception
  {
    return documentoGaaLocal.isParticellaEvasa(idAzienda, idParticella, fase,
        anno);
  }

  public boolean isVisibleTastoElimina(long idAzienda, int anno, int fase)
      throws SolmrException, Exception
  {
    return documentoGaaLocal.isVisibleTastoElimina(idAzienda, anno, fase);
  }

  public Vector<DocumentoVO> getListDocFromRicerca(String strDescDocumento,
      boolean attivi) throws SolmrException, Exception
  {
    return documentoGaaLocal.getListDocFromRicerca(strDescDocumento, attivi);
  }

  public Vector<ParticellaIstanzaRiesameVO> getLisParticellaFromIstanzaFoto(
      long idAzienda, int anno) throws SolmrException, Exception
  {
    return documentoGaaLocal.getLisParticellaFromIstanzaFoto(idAzienda, anno);
  }

  public String annullaIstanzaRiesame(Vector<Long> vIdIstanzaRiesame,
      long idAzienda, int anno, RuoloUtenza ruoloUtenza) throws SolmrException,
      Exception
  {
    return documentoGaaLocal.annullaIstanzaRiesame(vIdIstanzaRiesame,
        idAzienda, anno, ruoloUtenza);
  }

  public boolean existAltraFaseFotoParticella(long idAzienda,
      long idParticella, int anno, long idFase) throws SolmrException,
      Exception
  {
    return documentoGaaLocal.existAltraFaseFotoParticella(idAzienda,
        idParticella, anno, idFase);
  }

  public Vector<TipoDocumentoVO> getDocumentiNuovaIscrizione()
      throws SolmrException, Exception
  {
    return documentoGaaLocal.getDocumentiNuovaIscrizione();
  }

  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiRichiesta(
      long idRichiestaDocumento) throws SolmrException, Exception
  {
    return documentoGaaLocal
        .getElencoFileAllegatiRichiesta(idRichiestaDocumento);
  }

  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiNotifica(
      long idNotifica) throws SolmrException, Exception
  {
    return documentoGaaLocal.getElencoFileAllegatiNotifica(idNotifica);
  }

  public boolean isIstanzaAttiva(long idAzienda) throws SolmrException,
      Exception
  {
    return documentoGaaLocal.isIstanzaAttiva(idAzienda);
  }

  public boolean isParticellaIstRiesameCancellabile(long idAzienda,
      Vector<Long> vIdConduzioneParticella) throws SolmrException,
      Exception
  {
    return documentoGaaLocal.isParticellaIstRiesameCancellabile(idAzienda,
        vIdConduzioneParticella);
  }

  public boolean existIstanzaEsameAttivaFase(long idAzienda, Long idParticella,
      long idFase) throws SolmrException, Exception
  {
    return documentoGaaLocal.existIstanzaEsameAttivaFase(idAzienda,
        idParticella, idFase);
  }

  public FaseRiesameDocumentoVO getFaseRiesameDocumentoByIdDocumento(
      long idDocumento) throws SolmrException, Exception
  {
    return documentoGaaLocal.getFaseRiesameDocumentoByIdDocumento(idDocumento);
  }

  public boolean isParticellaInPotenziale(long idAzienda, long idParticella,
      int anno) throws SolmrException, Exception
  {
    return documentoGaaLocal.isParticellaInPotenziale(idAzienda, idParticella,
        anno);
  }

  public boolean isParticellaInPotenzialeContra(long idAzienda,
      long idParticella, int anno) throws SolmrException, Exception
  {
    return documentoGaaLocal.isParticellaInPotenzialeContra(idAzienda,
        idParticella, anno);
  }

  public boolean isNotPossibleIstanzaRiesameFaseSuccessiva(long idAzienda,
      long idParticella, int idFase, int anno, String parametro)
      throws SolmrException, Exception
  {
    return documentoGaaLocal.isNotPossibleIstanzaRiesameFaseSuccessiva(
        idAzienda, idParticella, idFase, anno, parametro);
  }
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazione(
      long idDichiarazioneConsistenza, long idTipoAllegato)
    throws SolmrException, Exception
  {
    return documentoGaaLocal.getAllegatoDichiarazioneFromIdDichiarazione(
        idDichiarazioneConsistenza, idTipoAllegato);
  }
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione(
      long idDichiarazioneConsistenza, String tipoStampa)
    throws SolmrException, Exception
  {
    return documentoGaaLocal.getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione(idDichiarazioneConsistenza, tipoStampa);
  }
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdAllegato(
      long idAllegato)
    throws SolmrException, Exception
  {
    return documentoGaaLocal.getAllegatoDichiarazioneFromIdAllegato(idAllegato);
  }
  
  /*public Vector<AllegatoDichiarazioneVO> getElencoAllegatiDichiarazioneDefault(
      Date dataInserimentoValidazione, int idMotivoDichiarazione) 
    throws SolmrException, Exception
  {
    return documentoGaaLocal.getElencoAllegatiDichiarazioneDefault(dataInserimentoValidazione, idMotivoDichiarazione);
  }*/
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatiAttiviDichiarazione(
      long idDichiarazioneConsistenza) 
    throws SolmrException, Exception
  {
    return documentoGaaLocal.getElencoAllegatiAttiviDichiarazione(idDichiarazioneConsistenza);
  }
  
  public Vector<AllegatoDichiarazioneVO> getAllElencoAllegatiDichiarazione(
      long idDichiarazioneConsistenza) 
    throws SolmrException, Exception
  {
    return documentoGaaLocal.getAllElencoAllegatiDichiarazione(idDichiarazioneConsistenza);
  }
  
  public Vector<TipoFirmaVO> getElencoTipoFirma()
      throws SolmrException, Exception
  {
    return documentoGaaLocal.getElencoTipoFirma();
  }

  /** ******************************************** */
  /** ***** DOCUMENTO END ****** */
  /** ******************************************** */

  /*******************************************************************/
  /******************* TERRENI START *************************/
  /*******************************************************************/

  public PlSqlCodeDescription allineaSupEleggibilePlSql(long idAzienda,
      long idUtenteAggiornamento) throws SolmrException, Exception
  {
    return terreniGaaLocal.allineaSupEleggibilePlSql(idAzienda,
        idUtenteAggiornamento);
  }

  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComune(
      long idAzienda) throws SolmrException, Exception
  {
    return terreniGaaLocal.riepilogoDestinazioneProduttivaComune(idAzienda);
  }

  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComuneDichiarato(
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return terreniGaaLocal
        .riepilogoDestinazioneProduttivaComuneDichiarato(idDichiarazioneConsistenza);
  }

  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVino(
      long idAzienda) throws SolmrException, Exception
  {
    return terreniGaaLocal.riepilogoDestinazioneProduttivaUvaDaVino(idAzienda);
  }

  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVinoDichiarato(
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return terreniGaaLocal
        .riepilogoDestinazioneProduttivaUvaDaVinoDichiarato(idDichiarazioneConsistenza);
  }

  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOP(
      long idAzienda) throws SolmrException, Exception
  {
    return terreniGaaLocal.riepilogoDestinazioneProduttivaVinoDOP(idAzienda);
  }

  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOPDichiarato(
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return terreniGaaLocal
        .riepilogoDestinazioneProduttivaVinoDOPDichiarato(idDichiarazioneConsistenza);
  }

  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOP(
      long idAzienda) throws SolmrException, Exception
  {
    return terreniGaaLocal.riepilogoProvinciaVinoDOP(idAzienda);
  }

  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOPDichiarato(
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return terreniGaaLocal
        .riepilogoProvinciaVinoDOPDichiarato(idDichiarazioneConsistenza);
  }

  public TreeMap<String, Vector<RiepiloghiUnitaArboreaVO>> riepilogoElencoSociProvinciaVinoDOP(
      long idAzienda) throws SolmrException, Exception
  {
    return terreniGaaLocal.riepilogoElencoSociProvinciaVinoDOP(idAzienda);
  }

  public Vector<Long> esisteUVValidataByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda) throws SolmrException,
      Exception
  {
    return terreniGaaLocal.esisteUVValidataByConduzioneAndAzienda(
        elencoConduzioni, idAzienda);
  }

  public Vector<Long> esisteUVByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda) throws SolmrException,
      Exception
  {
    return terreniGaaLocal.esisteUVByConduzioneAndAzienda(elencoConduzioni,
        idAzienda);
  }

  public Vector<Long> esisteUVModProcVITIByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda) throws SolmrException,
      Exception
  {
    return terreniGaaLocal.esisteUVModProcVITIByConduzioneAndAzienda(
        elencoConduzioni, idAzienda);
  }

  public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelSempliceByParameters(
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.searchStoricoUnitaArboreaExcelSempliceByParameters(
        idAzienda, filtriUnitaArboreaRicercaVO);
  }

  public Vector<StoricoParticellaVO> getListUVForInserimento(
      Long idParticellaCurr, Vector<Long> vIdParticella, Long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getListUVForInserimento(idParticellaCurr,
        vIdParticella, idAzienda);
  }

  public BigDecimal getSupEleggibilePlSql(long idParticellaCertificata,
      long idCatalogoMatrice) throws SolmrException, Exception
  {
    return terreniGaaLocal.getSupEleggibilePlSql(idParticellaCertificata,
        idCatalogoMatrice);
  }

  public PlSqlCodeDescription compensazioneAziendalePlSql(long idAzienda,
      long idUtenteAggiornamento) throws SolmrException, Exception
  {
    return terreniGaaLocal.compensazioneAziendalePlSql(idAzienda,
        idUtenteAggiornamento);
  }

  public Vector<UVCompensazioneVO> getUVPerCompensazione(long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getUVPerCompensazione(idAzienda);
  }

  public CompensazioneAziendaVO getCompensazioneAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getCompensazioneAzienda(idAzienda);
  }

  public Vector<RiepilogoCompensazioneVO> getRiepilogoPostAllinea(long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getRiepilogoPostAllinea(idAzienda);
  }

  public Vector<RiepilogoCompensazioneVO> getRiepilogoDirittiVitati(
      long idAzienda) throws SolmrException, Exception
  {
    return terreniGaaLocal.getRiepilogoDirittiVitati(idAzienda);
  }

  public CompensazioneAziendaVO getCompensazioneAziendaByIdAzienda(
      long idAzienda) throws SolmrException, Exception
  {
    return terreniGaaLocal.getCompensazioneAziendaByIdAzienda(idAzienda);
  }

  public int countUVAllineabiliGis(long idAzienda) throws SolmrException,
      Exception
  {
    return terreniGaaLocal.countUVAllineabiliGis(idAzienda);
  }

  public int countUVIstRiesameCompen(long idAzienda) throws SolmrException,
      Exception
  {
    return terreniGaaLocal.countUVIstRiesameCompen(idAzienda);
  }

  public int countSupUVIIrregolari(long idAzienda) throws SolmrException,
      Exception
  {
    return terreniGaaLocal.countSupUVIIrregolari(idAzienda);
  }

  public Date getMaxDataAggiornamentoConduzioniAndUV(long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getMaxDataAggiornamentoConduzioniAndUV(idAzienda);
  }

  public Date getMaxDataFotoInterpretazioneUV(long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getMaxDataFotoInterpretazioneUV(idAzienda);
  }

  public boolean existsIsoleParcDopoVarSched(long idAzienda,
      long isolaDichiarata) throws SolmrException, Exception
  {
    return terreniGaaLocal.existsIsoleParcDopoVarSched(idAzienda,
        isolaDichiarata);
  }

  public BigDecimal getSupNonCompensabile(long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getSupNonCompensabile(idAzienda);
  }

  public int countPercPossessoCompensazioneMag100(long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.countPercPossessoCompensazioneMag100(idAzienda);
  }

  public BigDecimal getPercUtilizzoEleggibile(long idAzienda, long idParticella)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getPercUtilizzoEleggibile(idAzienda, idParticella);
  }

  public Vector<Vector<DirittoCompensazioneVO>> getDirittiCalcolati(
      long idAzienda) throws SolmrException, Exception
  {
    return terreniGaaLocal.getDirittiCalcolati(idAzienda);
  }

  public BigDecimal getSumAreaGiaAssegnata(long idUnitaArborea)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getSumAreaGiaAssegnata(idUnitaArborea);
  }

  public BigDecimal getSumAreaMaxAssegnabile(long idUnitaArborea)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getSumAreaMaxAssegnabile(idUnitaArborea);
  }

  public void allineaUVaCompensazione(long idAzienda, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception
  {
    terreniGaaLocal.allineaUVaCompensazione(idAzienda, ruoloUtenza);
  }

  /*public void allineaUVinTolleranzaGIS(long idAzienda, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception
  {
    terreniGaaLocal.allineaUVinTolleranzaGIS(idAzienda, ruoloUtenza);
  }*/

  public ParticellaBioVO getParticellaBio(long idParticella, long idAzienda,
      Date dataInserimentoDichiarazione) throws SolmrException, Exception
  {
    return terreniGaaLocal.getParticellaBio(idParticella, idAzienda,
        dataInserimentoDichiarazione);
  }

  public BigDecimal getSumUtilizziPrimariNoIndicati(long idAzienda,
      long idParticella, Vector<String> vIdUtilizzo) throws SolmrException,
      Exception
  {
    return terreniGaaLocal.getSumUtilizziPrimariNoIndicati(idAzienda,
        idParticella, vIdUtilizzo);
  }

  public BigDecimal getSumUtilizziPrimariParticellaAltreConduzioni(
      long idAzienda, long idParticella, Vector<Long> vIdConduzioneParticella)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getSumUtilizziPrimariParticellaAltreConduzioni(
        idAzienda, idParticella, vIdConduzioneParticella);
  }

  public BigDecimal getSumPercentualPossessoAltreConduzioni(long idAzienda,
      long idParticella, Vector<Long> vIdConduzioneParticella)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getSumPercentualPossessoAltreConduzioni(idAzienda,
        idParticella, vIdConduzioneParticella);
  }

  public BigDecimal getSumSupCondottaAltreConduzioni(long idAzienda,
      long idParticella, Vector<Long> vIdConduzioneParticella)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getSumSupCondottaAltreConduzioni(idAzienda,
        idParticella, vIdConduzioneParticella);
  }

  public Vector<IstanzaRiesameVO> getFasiIstanzaRiesame(long idAzienda,
      long idParticella, Date dataInserimentoDichiarazione)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getFasiIstanzaRiesame(idAzienda, idParticella,
        dataInserimentoDichiarazione);
  }

  public int calcolaP30PlSql(long idStoricoParticella, Date dataInizioValidita)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.calcolaP30PlSql(idStoricoParticella,
        dataInizioValidita);
  }

  public int calcolaP25PlSql(long idStoricoParticella, Date dataInizioValidita)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.calcolaP25PlSql(idStoricoParticella,
        dataInizioValidita);
  }

  public int calcolaP26PlSql(long idAzienda, long idParticella,
      Long idParticellaCertificata) throws SolmrException, Exception
  {
    return terreniGaaLocal.calcolaP26PlSql(idAzienda, idParticella,
        idParticellaCertificata);
  }

  public Vector<CasoParticolareVO> getCasiParticolari(
      String particellaObbligatoria) throws SolmrException, Exception
  {
    return terreniGaaLocal.getCasiParticolari(particellaObbligatoria);
  }

  public Vector<TipoRiepilogoVO> getTipoRiepilogo(String funzionalita,
      String codiceRuolo) throws SolmrException, Exception
  {
    return terreniGaaLocal.getTipoRiepilogo(funzionalita, codiceRuolo);
  }

  public Vector<TipoEsportazioneDatiVO> getTipoEsportazioneDati(String codMenu,
      String codiceRuolo) throws SolmrException, Exception
  {
    return terreniGaaLocal.getTipoEsportazioneDati(codMenu, codiceRuolo);
  }

  public boolean isParticellAttivaStoricoParticella(String istatComune,
      String sezione, String foglio, String particella, String subalterno)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.isParticellAttivaStoricoParticella(istatComune,
        sezione, foglio, particella, subalterno);
  }

  public Vector<StoricoParticellaVO> getElencoParticelleForPopNotifica(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO)
      throws SolmrException, Exception
  {
    return terreniGaaLocal
        .getElencoParticelleForPopNotifica(filtriParticellareRicercaVO);
  }

  public StoricoParticellaVO findStoricoParticellaDichCompleto(
      long idStoricoParticella, long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.findStoricoParticellaDichCompleto(
        idStoricoParticella, idDichiarazioneConsistenza);
  }
  
  public Vector<TipoEfaVO> getListTipoEfa()  
    throws SolmrException, Exception
  {
    return terreniGaaLocal.getListTipoEfa();
  }
  
  public Vector<TipoEfaVO> getListLegendaTipoEfa()
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getListLegendaTipoEfa();
  }
  
  public TipoEfaVO getTipoEfaFromIdCatalogoMatrice(long idCatalogoMatrice)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getTipoEfaFromIdCatalogoMatrice(idCatalogoMatrice);
  }
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSemina() 
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getListTipoPeriodoSemina();
  }
  
  public Vector<TipoUtilizzoVO> getListTipoUtilizzoEfa(long idTipoEfa)
     throws SolmrException, Exception
  {    
    return terreniGaaLocal.getListTipoUtilizzoEfa(idTipoEfa);    
  }
   
  public Vector<TipoVarietaVO> getListTipoVarietaEfaByMatrice(long idTipoEfa, 
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso)
    throws SolmrException, Exception
  {    
    return terreniGaaLocal.getListTipoVarietaEfaByMatrice(idTipoEfa, idUtilizzo, 
        idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);
  }
  
  public Vector<TipoDestinazioneVO> getListTipoDestinazioneEfa(long idTipoEfa, long idUtilizzo)
      throws SolmrException, Exception
  {    
    return terreniGaaLocal.getListTipoDestinazioneEfa(idTipoEfa, idUtilizzo);
  }
   
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione)  throws SolmrException, Exception
  {    
    return terreniGaaLocal.getListDettaglioUsoEfaByMatrice(idTipoEfa, idUtilizzo, idTipoDestinazione);
  }
  
  public Vector<TipoQualitaUsoVO> getListQualitaUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso) 
          throws SolmrException, Exception
  {    
    return terreniGaaLocal.getListQualitaUsoEfaByMatrice(idTipoEfa, idUtilizzo, 
        idTipoDestinazione, idTipoDettaglioUso);
  }
  
  public Integer getAbbPonderazioneByMatrice(
      long idTipoEfa, long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso,
      long idTipoQualitaUso, long idVarieta)
    throws SolmrException, Exception
  {    
    return terreniGaaLocal.getAbbPonderazioneByMatrice(idTipoEfa, idUtilizzo, 
        idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso, idVarieta);
  }
  
  public String getValoreAttivoTipoAreaFromParticellaAndId(
      long idParticella, long idTipoArea)
          throws SolmrException, Exception
  {    
    return terreniGaaLocal.getValoreAttivoTipoAreaFromParticellaAndId(idParticella, idTipoArea);
  }
  
  public Vector<TipoAreaVO> getValoriTipoAreaParticella(long idParticella, Date dataInserimentoValidazione)
      throws SolmrException, Exception
  {    
    return terreniGaaLocal.getValoriTipoAreaParticella(idParticella, dataInserimentoValidazione);
  }
  
  public Vector<TipoAreaVO> getAllValoriTipoArea()
      throws SolmrException, Exception
  {    
    return terreniGaaLocal.getAllValoriTipoArea();
  }
  
  public Vector<TipoAreaVO> getDescTipoAreaBrogliaccio(Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {    
    return terreniGaaLocal.getDescTipoAreaBrogliaccio(idDichiarazioneConsistenza);
  }
  
  public Vector<TipoAreaVO> getValoriTipoAreaFoglio(String comune, String foglio, String sezione)
      throws SolmrException, Exception
  {    
    return terreniGaaLocal.getValoriTipoAreaFoglio(comune, foglio, sezione);
  }
  
  public Vector<TipoMetodoIrriguoVO> getElencoMetodoIrriguo()
      throws SolmrException, Exception
  {    
    return terreniGaaLocal.getElencoMetodoIrriguo();
  }
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSeminaByCatalogo(long idUtilizzo,
      long idDestinazione, long idDettUso, long idQualiUso, long idVarieta)
      throws SolmrException, Exception
  {    
    return terreniGaaLocal.getListTipoPeriodoSeminaByCatalogo(idUtilizzo,
        idDestinazione, idDettUso, idQualiUso, idVarieta);
  }
  
  public Vector<TipoFonteVO> getElencoAllTipoFonte() throws SolmrException, Exception
  {    
    return terreniGaaLocal.getElencoAllTipoFonte();
  }
  
  public boolean isUtilizzoAttivoSuMatrice(long idUtilizzo) throws SolmrException, Exception
  {    
    return terreniGaaLocal.isUtilizzoAttivoSuMatrice(idUtilizzo);
  }
  
  public Vector<TipoAreaVO> riepilogoTipoArea(long idAzienda)
      throws SolmrException, Exception
  {    
    return terreniGaaLocal.riepilogoTipoArea(idAzienda);
  }
  
  public Vector<TipoAreaVO> riepilogoTipoAreaDichiarato(long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {    
    return terreniGaaLocal.riepilogoTipoAreaDichiarato(idDichiarazioneConsistenza);
  }
  
  public Vector<TipoAreaVO> getValoriTipoAreaFiltroElenco(Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {    
    return terreniGaaLocal.getValoriTipoAreaFiltroElenco(idDichiarazioneConsistenza);
  }
  
  public boolean isDettaglioUsoObbligatorio(long idTipoEfa,
      long idVarieta) throws SolmrException, Exception
  {    
    return terreniGaaLocal.isDettaglioUsoObbligatorio(idTipoEfa, idVarieta);
  }
  
  public TipoEfaVO getTipoEfaFromPrimaryKey(long idTipoEfa)
      throws SolmrException, Exception
  {    
    return terreniGaaLocal.getTipoEfaFromPrimaryKey(idTipoEfa);
  }

  public boolean isAltreUvDaSchedario(long idParticella) throws SolmrException,
      Exception
  {
    return terreniGaaLocal.isAltreUvDaSchedario(idParticella);
  }

  public Vector<Long> getIdParticellaByIdConduzione(
      Vector<Long> vIdConduzioneParticella) throws SolmrException,
      Exception
  {
    return terreniGaaLocal
        .getIdParticellaByIdConduzione(vIdConduzioneParticella);
  }

  public Vector<ProvinciaVO> getListProvincieParticelleByIdAzienda(
      Long idAzienda) throws SolmrException, Exception
  {
    return terreniGaaLocal.getListProvincieParticelleByIdAzienda(idAzienda);
  }

  public Vector<ProvinciaVO> getListProvincieParticelleByIdDichiarazioneConsistenza(
      Long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return terreniGaaLocal
        .getListProvincieParticelleByIdDichiarazioneConsistenza(idDichiarazioneConsistenza);
  }

  public Vector<IsolaParcellaVO> getElencoIsoleParcelle(String nomeLib,
      long idAzienda) throws SolmrException, Exception
  {
    return terreniGaaLocal.getElencoIsoleParcelle(nomeLib, idAzienda);
  }

  public Vector<StoricoParticellaVO> associaParcelleGisLettura(String nomeLib,
      long idAzienda, long idUtente) throws SolmrException, Exception
  {
    return terreniGaaLocal.associaParcelleGisLettura(nomeLib, idAzienda,
        idUtente);
  }

  public void associaParcelleGisConferma(long idAzienda, long idUtente,
      Vector<Long> vIdUnarParcellaSel) throws SolmrException, Exception
  {
    terreniGaaLocal.associaParcelleGisConferma(idAzienda, idUtente,
        vIdUnarParcellaSel);
  }

  public ConsistenzaVO getDichConsUVParcelleDoppie(long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getDichConsUVParcelleDoppie(idAzienda);
  }

  public void allineaUVaGIS(Vector<Long> vIdIsolaParcella, long idAzienda,
      long idDichiarazioneConsistenza, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception
  {
    terreniGaaLocal.allineaUVaGIS(vIdIsolaParcella, idAzienda,
        idDichiarazioneConsistenza, ruoloUtenza);
  }

  /*public Integer getTolleranzaPlSql(String nomeLib, long idAzienda,
      long idUnitaArborea) throws SolmrException, Exception
  {
    return terreniGaaLocal.getTolleranzaPlSql(nomeLib, idAzienda,
        idUnitaArborea);
  }*/

  public Vector<RegistroPascoloVO> getRegistroPascoliPianoLavorazione(
      long idParticellaCertificata) throws SolmrException, Exception
  {
    return terreniGaaLocal
        .getRegistroPascoliPianoLavorazione(idParticellaCertificata);
  }
  
  public Vector<RegistroPascoloVO> getRegistroPascoliPianoLavorazioneChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno) 
    throws SolmrException, Exception
  {
    return terreniGaaLocal
        .getRegistroPascoliPianoLavorazioneChiaveCatastale(
            istatComune, foglio, particella, sezione, subalterno);
  }

  public Vector<RegistroPascoloVO> getRegistroPascoliDichCons(
      long idParticellaCertificata, int annoDichiarazione)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getRegistroPascoliDichCons(idParticellaCertificata,
        annoDichiarazione);
  }
  
  public Vector<RegistroPascoloVO> getRegistroPascoliDichConsChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno, int annoDichiarazione)
    throws SolmrException, Exception
  {
    return terreniGaaLocal.getRegistroPascoliDichConsChiaveCatastale(
        istatComune, foglio, particella, sezione, subalterno, annoDichiarazione);
  }
  
  public boolean isRegistroPascoliPratoPolifita(
      long idParticellaCertificata) throws SolmrException, Exception
  {
    return terreniGaaLocal.isRegistroPascoliPratoPolifita(idParticellaCertificata);
  }

  public Vector<Long> getIdConduzioneFromIdAziendaIdParticella(long idAzienda,
      long idParticella) throws SolmrException, Exception
  {
    return terreniGaaLocal.getIdConduzioneFromIdAziendaIdParticella(idAzienda,
        idParticella);
  }

  public Vector<AnagParticellaExcelVO> searchParticelleStabGisExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.searchParticelleStabGisExcelByParameters(
        filtriParticellareRicercaVO, idAzienda);
  }

  public Vector<AnagParticellaExcelVO> searchParticelleAvvicExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.searchParticelleAvvicExcelByParameters(
        filtriParticellareRicercaVO, idAzienda);
  }

  /*public HashMap<Long, HashMap<Integer, AvvicendamentoVO>> getElencoAvvicendamento(
      Vector<Long> vIdParticella, long idAzienda, Integer annoPartenza,
      int numeroAnni, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception
  {
    return terreniGaaLocal.getElencoAvvicendamento(vIdParticella, idAzienda,
        annoPartenza, numeroAnni, idDichiarazioneConsistenza);
  }*/

  public Vector<Long> getIdUtilizzoFromIdIdConduzione(
      long idConduzioneParticella) throws SolmrException, Exception
  {
    return terreniGaaLocal
        .getIdUtilizzoFromIdIdConduzione(idConduzioneParticella);
  }

  public BigDecimal getSumSupUtilizzoParticellaAzienda(long idAzienda,
      long idParticella) throws SolmrException, Exception
  {
    return terreniGaaLocal.getSumSupUtilizzoParticellaAzienda(idAzienda,
        idParticella);
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaPianoLavorazione(long idAzienda) 
      throws SolmrException, Exception
  {
    return terreniGaaLocal.riepilogoTipoEfaPianoLavorazione(idAzienda);
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaDichiarazione(long idDichiarazioneConsistenza)  
      throws SolmrException, Exception
  {
    return terreniGaaLocal.riepilogoTipoEfaDichiarazione(idDichiarazioneConsistenza);
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningPianoLavorazione(
      long idAzienda) throws SolmrException, Exception
  {
    return terreniGaaLocal.riepilogoTipoGreeningPianoLavorazione(idAzienda);
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningDichiarazione(long idDichiarazioneConsistenza) 
      throws SolmrException, Exception
  {
    return terreniGaaLocal.riepilogoTipoGreeningDichiarazione(idDichiarazioneConsistenza);
  }
  
  public Vector<TipoEfaVO> getElencoTipoEfaForAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getElencoTipoEfaForAzienda(idAzienda);
  }
  
  public Vector<TipoDestinazioneVO> getElencoTipoDestinazioneByMatrice(long idUtilizzo)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getElencoTipoDestinazioneByMatrice(idUtilizzo);
  }
  
  public Vector<TipoVarietaVO> getElencoTipoVarietaByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso)
    throws SolmrException, Exception
  {
    return terreniGaaLocal.getElencoTipoVarietaByMatrice(idUtilizzo, idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);
  }
  
  public Vector<TipoQualitaUsoVO> getElencoTipoQualitaUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso) 
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getElencoTipoQualitaUsoByMatrice(
        idUtilizzo, idTipoDestinazione, idTipoDettaglioUso);
  }
  
  public CatalogoMatriceVO getCatalogoMatriceFromMatrice(long idUtilizzo, long idVarieta,
      long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso) 
          throws SolmrException, Exception
  {
    return terreniGaaLocal.getCatalogoMatriceFromMatrice(
        idUtilizzo, idVarieta, idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);
  }
  
  public CatalogoMatriceVO getCatalogoMatriceFromPrimariKey(long idCatalogoMatrice) 
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getCatalogoMatriceFromPrimariKey(idCatalogoMatrice);
  }
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaDefault(long idCatalogoMatrice)
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getCatalogoMatriceSeminaDefault(idCatalogoMatrice);
  }
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaByIdTipoPeriodo(long idCatalogoMatrice, 
      long idTipoPeriodoSemina) throws SolmrException, Exception
  {
    return terreniGaaLocal.getCatalogoMatriceSeminaByIdTipoPeriodo(
        idCatalogoMatrice, idTipoPeriodoSemina);
  }
  
  public Vector<Long> getListIdPraticaMantenimentoPlSql(
      long idCatalogoMatrice, String flagDefault) throws SolmrException, Exception
  {
    return terreniGaaLocal.getListIdPraticaMantenimentoPlSql(idCatalogoMatrice, flagDefault);
  }
  
  public Vector<TipoSeminaVO> getElencoTipoSemina() throws SolmrException, Exception
  {
    return terreniGaaLocal.getElencoTipoSemina();
  }
  
  public Vector<TipoPraticaMantenimentoVO> getElencoPraticaMantenimento(
      Vector<Long> vIdMantenimento) throws SolmrException, Exception
  {
    return terreniGaaLocal.getElencoPraticaMantenimento(vIdMantenimento);
  }
  
  public Vector<TipoFaseAllevamentoVO> getElencoFaseAllevamento() throws SolmrException, Exception
  {
    return terreniGaaLocal.getElencoFaseAllevamento();
  }
      

  public String cambiaPercentualePossessoMassivo(RuoloUtenza ruoloUtenza,
      long idAzienda, BigDecimal percentualePossesso) throws SolmrException,
      Exception
  {
    String msgErrore = terreniGaaLocal.cambiaPercentualePossessoMassivo(
        ruoloUtenza, idAzienda, percentualePossesso);
    if (Validator.isNotEmpty(msgErrore))
      sessionContext.setRollbackOnly();
    return msgErrore;
  }

  public BigDecimal getSumSupCondottaAsservimentoParticellaAzienda(
      long idAzienda, long idParticella) throws SolmrException, Exception
  {
    return terreniGaaLocal.getSumSupCondottaAsservimentoParticellaAzienda(
        idAzienda, idParticella);
  }

  public String cambiaPercentualePossessoSupUtilizzataMassivo(
      RuoloUtenza ruoloUtenza, Long idAzienda) throws SolmrException,
      Exception
  {
    String msgErrore = terreniGaaLocal
        .cambiaPercentualePossessoSupUtilizzataMassivo(ruoloUtenza, idAzienda);
    if (Validator.isNotEmpty(msgErrore))
      sessionContext.setRollbackOnly();
    return msgErrore;
  }

  public void avviaConsolidamento(long idAzienda, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception
  {
    terreniGaaLocal.avviaConsolidamento(idAzienda, ruoloUtenza);
  }

  public void modificaConduzioneEleggibileUV(
      HashMap<Long, ConduzioneEleggibilitaVO> hPartCondEleg)
      throws SolmrException, Exception
  {
    terreniGaaLocal.modificaConduzioneEleggibileUV(hPartCondEleg);
  }

  public void allineaPercUsoElegg(long idAzienda, Vector<Long> vIdParticella,
      long idUtente) throws SolmrException, Exception
  {
    terreniGaaLocal.allineaPercUsoElegg(idAzienda, vIdParticella, idUtente);
  }
  
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione) 
      throws SolmrException, Exception
  {
    return terreniGaaLocal.getListDettaglioUsoByMatrice(
        idUtilizzo, idTipoDestinazione);
  }
  
  public TipoDettaglioUsoVO findDettaglioUsoByPrimaryKey(
      long idTipoDettaglioUso) throws SolmrException, Exception
  {
    return terreniGaaLocal.findDettaglioUsoByPrimaryKey(idTipoDettaglioUso);
  }

  /*******************************************************************/
  /******************* TERRENI END *************************/
  /*******************************************************************/

  /*******************************************************************/
  /******************* STAMPA START *************************/
  /*******************************************************************/

  public RichiestaTipoReportVO[] getElencoSubReportRichiesta(
      String codiceReport, java.util.Date dataRiferimento)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getElencoSubReportRichiesta(codiceReport,
        dataRiferimento);
  }

  public AnagAziendaVO getAnagraficaAzienda(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    return stampaGaaLocal.getAnagraficaAzienda(idAzienda, dataRiferimento);
  }

  public Vector<ParticellaVO> getParticelleUtilizzoIstanzaRiesame(
      long idDocumento) throws SolmrException, Exception
  {
    return stampaGaaLocal.getParticelleUtilizzoIstanzaRiesame(idDocumento);
  }

  public BigDecimal getSumSupCatastaleTerreniIstanzaRiesame(long idDocumento)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getSumSupCatastaleTerreniIstanzaRiesame(idDocumento);
  }

  public BigDecimal[] getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame(
      long idDocumento) throws SolmrException, Exception
  {
    return stampaGaaLocal
        .getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame(idDocumento);
  }

  public Vector<TipoReportVO> getElencoTipoReport(String codiceTipoReport,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    return stampaGaaLocal
        .getElencoTipoReport(codiceTipoReport, dataRiferimento);
  }

  public Vector<TipoReportVO> getElencoTipoReportByValidazione(
      String codiceTipoReport, Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getElencoTipoReportByValidazione(codiceTipoReport,
        idDichiarazioneConsistenza);
  }

  public DichiarazioneConsistenzaGaaVO getDatiDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return stampaGaaLocal
        .getDatiDichiarazioneConsistenza(idDichiarazioneConsistenza);
  }

  public LogoVO getLogo(String istatRegione, String provincia)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getLogo(istatRegione, provincia);
  }

  public InfoFascicoloVO getInfoFascicolo(long idAzienda,
      java.util.Date dataRiferimento, Long codFotografia)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getInfoFascicolo(idAzienda, dataRiferimento,
        codFotografia);
  }

  public ValoriCondizionalitaVO getValoriCondizionalita(Long idAzienda,
      Long codiceFotografia) throws SolmrException, Exception
  {
    return stampaGaaLocal.getValoriCondizionalita(idAzienda, codiceFotografia);
  }

  public TipoAttestazioneVO[] getListAttestazioniAlPianoAttuale(Long idAzienda,
      Long idAllegato) throws SolmrException, Exception
  {
    return stampaGaaLocal.getListAttestazioniAlPianoAttuale(idAzienda,
        idAllegato);
  }

  public TipoAttestazioneVO[] getListAttestazioneAllaDichiarazione(
      Long codiceFotografiaTerreni, java.util.Date dataAnnoCampagna,
      Long idAllegato) throws SolmrException, Exception
  {
    return stampaGaaLocal.getListAttestazioneAllaDichiarazione(
        codiceFotografiaTerreni, dataAnnoCampagna, idAllegato);
  }

  public Vector<ContoCorrenteVO> getStampaContiCorrenti(Long idAzienda,
      java.util.Date dataInserimentoDichiarazione) throws SolmrException,
      Exception
  {
    return stampaGaaLocal.getStampaContiCorrenti(idAzienda,
        dataInserimentoDichiarazione);
  }

  public Vector<TipoReportVO> getListTipiDocumentoStampaProtocollo(
      long idDocumento[]) throws SolmrException, Exception
  {
    return stampaGaaLocal.getListTipiDocumentoStampaProtocollo(idDocumento);
  }

  public String getTipoDocumentoStampaProtocollo(long idTipoReport)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getTipoDocumentoStampaProtocollo(idTipoReport);
  }

  public int getCountDocumentoCompatibile(long idTipoReport, long[] idDocumento)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getCountDocumentoCompatibile(idTipoReport,
        idDocumento);
  }

  public Vector<CodeDescription> getElencoPropietariDocumento(long[] idDocumento)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getElencoPropietariDocumento(idDocumento);
  }

  public TipoReportVO getTipoReport(long idTipoReport) throws SolmrException,
      Exception
  {
    return stampaGaaLocal.getTipoReport(idTipoReport);
  }
  
  public TipoReportVO getTipoReportByCodice(String codiceReport)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getTipoReportByCodice(codiceReport);
  }

  public Vector<TipoAttestazioneVO> getAttestStampaProtoc(long idReportSubReport)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getAttestStampaProtoc(idReportSubReport);
  }

  public boolean isNumeroProtocolloValido(String numeroProtocollo)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.isNumeroProtocolloValido(numeroProtocollo);
  }

  public Vector<ParticellaVO> getElencoParticelleVarCat(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return stampaGaaLocal.getElencoParticelleVarCat(idAzienda,
        idDichiarazioneConsistenza);
  }

  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoIdoneita(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception
  {
    return stampaGaaLocal.getStampaUVRiepilogoIdoneita(idAzienda,
        idDichiarazioneConsistenza);
  }

  public Vector<TipoFormaConduzioneVO> getFormaConduzioneSezioneAnagrafica(
      Long idAzienda, Date dataRiferimento) throws SolmrException,
      Exception
  {
    return stampaGaaLocal.getFormaConduzioneSezioneAnagrafica(idAzienda,
        dataRiferimento);
  }

  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntiveStampa(
      long idAzienda, Date dataInserimentoDichiarazione) throws SolmrException,
      Exception
  {
    return stampaGaaLocal.getTipoInfoAggiuntiveStampa(idAzienda,
        dataInserimentoDichiarazione);
  }

  public Vector<AnagAziendaVO> getAziendeAssociateStampa(long idAzienda,
      Date dataInserimentoDichiarazione) throws SolmrException, Exception
  {
    return stampaGaaLocal.getAziendeAssociateStampa(idAzienda,
        dataInserimentoDichiarazione);
  }

  public Vector<ConduzioneParticellaVO> getRiepilogoConduzioneStampa(
      long idAzienda, Long codFotografia) throws SolmrException,
      Exception
  {
    return stampaGaaLocal
        .getRiepilogoConduzioneStampa(idAzienda, codFotografia);
  }

  public Vector<StoricoParticellaVO> getRiepilogoComuneStampa(long idAzienda,
      Long codFotografia) throws SolmrException, Exception
  {
    return stampaGaaLocal.getRiepilogoComuneStampa(idAzienda, codFotografia);
  }

  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoVitignoIdoneita(
      Long idAzienda, Long idDichiarazioneConsistenza) throws SolmrException,
      Exception
  {
    return stampaGaaLocal.getStampaUVRiepilogoVitignoIdoneita(idAzienda,
        idDichiarazioneConsistenza);
  }

  public Vector<FabbricatoVO> getStampaFabbricati(Long idAzienda,
      Date dataRiferimento) throws SolmrException, Exception
  {
    return stampaGaaLocal.getStampaFabbricati(idAzienda, dataRiferimento);
  }

  public Vector<DocumentoVO> getDocumentiStampaMd(Long idAzienda,
      Long idDichiarazioneConsistenza, String cuaa,
      java.util.Date dataInserimentoDichiarazione) throws SolmrException,
      Exception
  {
    return stampaGaaLocal.getDocumentiStampaMd(idAzienda,
        idDichiarazioneConsistenza, cuaa, dataInserimentoDichiarazione);
  }

  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiStoricoPartTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getStampaRiepiloghiStoricoPartTipoArea(tipoArea,
        idAzienda, codFotografia);
  }

  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiFoglioTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getStampaRiepiloghiFoglioTipoArea(tipoArea,
        idAzienda, codFotografia);
  }
  
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAlPianoAttuale(
      Long idAzienda, String voceMenu, boolean flagCondizionalita)  throws SolmrException, Exception
  {
    return stampaGaaLocal.getListAttestazioniDichiarazioniAlPianoAttuale(idAzienda, voceMenu, flagCondizionalita);
  }
  
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAllaValidazione(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String voceMenu, boolean flagCondizionalita)  
    throws SolmrException, Exception
  {
    return stampaGaaLocal.getListAttestazioniDichiarazioniAllaValidazione(
        codiceFotografiaTerreni, dataInserimentoDichiarazione, voceMenu, flagCondizionalita);
  }
  
  public Vector<TipoAttestazioneVO> getElencoAllegatiAllaValidazionePerStampa(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String codiceAttestazione)
    throws SolmrException, Exception
  {
    return stampaGaaLocal.getElencoAllegatiAllaValidazionePerStampa(codiceFotografiaTerreni, 
        dataInserimentoDichiarazione, codiceAttestazione);
  }
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReport(long idReportSubReport, 
    Date dataInserimentoDichiarazione) throws SolmrException, Exception
  {
    return stampaGaaLocal.getAttestazioniFromSubReport(idReportSubReport, 
        dataInserimentoDichiarazione);
  }
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReportValidDettagli(long idReportSubReport, 
      Date dataInserimentoDichiarazione, Long codiceFotografiaTerreni) throws SolmrException, Exception
  {
    return stampaGaaLocal.getAttestazioniFromSubReportValidDettagli(idReportSubReport, 
        dataInserimentoDichiarazione, codiceFotografiaTerreni);
  }
  
  public String getTipoReportByValidazioneEAllegato(
      long idDichiarazioneConsistenza, int idTipoAllegato) throws SolmrException, Exception
  {
    return stampaGaaLocal.getTipoReportByValidazioneEAllegato(idDichiarazioneConsistenza, 
        idTipoAllegato);
  }
  
  public TipoAllegatoVO getTipoAllegatoById(int idTipoAllegato)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getTipoAllegatoById(idTipoAllegato); 
  }
  
  public Vector<TipoAllegatoVO> getTipoAllegatoForValidazione(int idMotivoDichiarazione, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException, Exception
  {
    return stampaGaaLocal.getTipoAllegatoForValidazione(idMotivoDichiarazione, dataInserimentoDichiarazione);
  }
  
  public Vector<TipoAllegatoVO> getTipoAllegatoObbligatorioForValidazione(int idMotivoDichiarazione) 
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getTipoAllegatoObbligatorioForValidazione(idMotivoDichiarazione);
  }
  
  public TipoFirmatarioVO getTipoFirmatarioById(int idTipoFirmatario)
      throws SolmrException, Exception
  {
    return stampaGaaLocal.getTipoFirmatarioById(idTipoFirmatario);
  }

  /*******************************************************************/
  /******************* STAMPA END *************************/
  /*******************************************************************/

  /*******************************************************************/
  /******************* POLIZZA START *************************/
  /*******************************************************************/

  public Vector<Integer> getElencoAnnoCampagnaByIdAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    return polizzaGaaLocal.getElencoAnnoCampagnaByIdAzienda(idAzienda);
  }

  public Vector<BaseCodeDescription> getElencoInterventoByIdAzienda(
      long idAzienda) throws SolmrException, Exception
  {
    return polizzaGaaLocal.getElencoInterventoByIdAzienda(idAzienda);
  }

  public Vector<PolizzaVO> getElencoPolizze(long idAzienda,
      Integer annoCampagna, Long idIntervento) throws SolmrException,
      Exception
  {
    return polizzaGaaLocal.getElencoPolizze(idAzienda, annoCampagna,
        idIntervento);
  }

  public PolizzaVO getDettaglioPolizza(long idPolizzaAssicurativa)
      throws SolmrException, Exception
  {
    return polizzaGaaLocal.getDettaglioPolizza(idPolizzaAssicurativa);
  }

  public TreeMap<Long, Vector<PolizzaDettaglioVO>> getDettaglioPolizzaColtura(
      long idPolizzaAssicurativa) throws SolmrException, Exception
  {
    return polizzaGaaLocal.getDettaglioPolizzaColtura(idPolizzaAssicurativa);
  }

  public TreeMap<Long, Vector<PolizzaDettaglioVO>> getDettaglioPolizzaStruttura(
      long idPolizzaAssicurativa) throws SolmrException, Exception
  {
    return polizzaGaaLocal.getDettaglioPolizzaStruttura(idPolizzaAssicurativa);
  }

  public TreeMap<Long, Vector<PolizzaDettaglioVO>> getDettaglioPolizzaZootecnia(
      long idPolizzaAssicurativa) throws SolmrException, Exception
  {
    return polizzaGaaLocal.getDettaglioPolizzaZootecnia(idPolizzaAssicurativa);
  }

  /*******************************************************************/
  /******************* POLIZZA END *************************/
  /*******************************************************************/

  /*******************************************************************/
  /******************* MANUALE START *************************/
  /*******************************************************************/

  public Vector<ManualeVO> getElencoManualiFromRuoli(String descRuolo)
      throws SolmrException, Exception
  {
    return manualeGaaLocal.getElencoManualiFromRuoli(descRuolo);
  }

  public ManualeVO getManuale(long idManuale) throws SolmrException,
      Exception
  {
    return manualeGaaLocal.getManuale(idManuale);
  }

  /*******************************************************************/
  /******************* MANUALE END *************************/
  /*******************************************************************/

  /*******************************************************************/
  /******************* CONSISTENZA START *************************/
  /*******************************************************************/

  public InvioFascicoliVO getLastSchedulazione(long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return consistenzaGaaLocal.getLastSchedulazione(idDichiarazioneConsistenza);
  }

  public boolean trovaSchedulazioneAttiva(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return consistenzaGaaLocal.trovaSchedulazioneAttiva(idAzienda,
        idDichiarazioneConsistenza);
  }

  public void insertSchedulazione(InvioFascicoliVO invioFascicoliVO,
      long idUtente) throws SolmrException, Exception
  {
    consistenzaGaaLocal.insertSchedulazione(invioFascicoliVO, idUtente);
  }

  public void deleteSchedulazione(long idInvioFascicoli) throws SolmrException,
      Exception
  {
    consistenzaGaaLocal.deleteSchedulazione(idInvioFascicoli);
  }

  /*******************************************************************/
  /******************* CONSISTENZA END *************************/
  /*******************************************************************/

  /** ******************************************** */
  /** ***** NUOVA ISCRIZIONE START ****** */
  /** ******************************************** */
  public AziendaNuovaVO getAziendaNuovaIscrizione(String cuaa,
      long[] arrTipoRichiesta) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getAziendaNuovaIscrizione(cuaa,
        arrTipoRichiesta);
  }
  
  public AziendaNuovaVO getAziendaNuovaIscrizioneEnte(String codEnte, long[] arrTipoRichiesta)      
      throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getAziendaNuovaIscrizioneEnte(codEnte, arrTipoRichiesta);
  }

  public AziendaNuovaVO getAziendaNuovaIscrizioneByPrimaryKey(
      Long idAziendaNuova) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal
        .getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
  }

  public Long insertAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO,
      long idUtenteAggiornamento) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.insertAziendaNuovaIscrizione(aziendaNuovaVO,
        idUtenteAggiornamento);
  }

  public Long insertAziendaNuovaRichiestaValCess(
      RichiestaAziendaVO richiestaAziendaVO) throws SolmrException,
      Exception
  {
    return nuovaIscrizioneGaaLocal
        .insertAziendaNuovaRichiestaValCess(richiestaAziendaVO);
  }
  
  public Long insertAziendaNuovaRichiestaVariazione(RichiestaAziendaVO richiestaAziendaVO)
      throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal
        .insertAziendaNuovaRichiestaVariazione(richiestaAziendaVO);
  }

  public void updateAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO,
      long idUtenteAggiornamento) throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.updateAziendaNuovaIscrizione(aziendaNuovaVO,
        idUtenteAggiornamento);
  }

  public Vector<UteAziendaNuovaVO> getUteAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getUteAziendaNuovaIscrizione(idAziendaNuova);
  }

  public void aggiornaUteAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO,
      long idUtenteAggiornamento, Vector<UteAziendaNuovaVO> vUteAziendaNuova)
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaUteAziendaNuovaIscrizione(aziendaNuovaVO,
        idUtenteAggiornamento, vUteAziendaNuova);
  }

  public Vector<FabbricatoAziendaNuovaVO> getFabbrAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal
        .getFabbrAziendaNuovaIscrizione(idAziendaNuova);
  }

  public void aggiornaFabbrAziendaNuovaIscrizione(
      AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<FabbricatoAziendaNuovaVO> vFabbrAziendaNuova)
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaFabbrAziendaNuovaIscrizione(aziendaNuovaVO,
        idUtenteAggiornamento, vFabbrAziendaNuova);
  }

  public boolean existsDependenciesUte(long idUteAziendaNuova)
      throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.existsDependenciesUte(idUteAziendaNuova);
  }

  public Vector<ParticellaAziendaNuovaVO> getParticelleAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal
        .getParticelleAziendaNuovaIscrizione(idAziendaNuova);
  }

  public Vector<UnitaMisuraVO> getListUnitaMisura() throws SolmrException,
      Exception
  {
    return nuovaIscrizioneGaaLocal.getListUnitaMisura();
  }

  public void aggiornaParticelleAziendaNuovaIscrizione(
      AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<ParticellaAziendaNuovaVO> vParticelleAziendaNuova)
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaParticelleAziendaNuovaIscrizione(
        aziendaNuovaVO, idUtenteAggiornamento, vParticelleAziendaNuova);
  }

  public Vector<AllevamentoAziendaNuovaVO> getAllevamentiAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal
        .getAllevamentiAziendaNuovaIscrizione(idAziendaNuova);
  }

  public void aggiornaAllevamentiAziendaNuovaIscrizione(
      AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<AllevamentoAziendaNuovaVO> vAllevamentiAziendaNuova)
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaAllevamentiAziendaNuovaIscrizione(
        aziendaNuovaVO, idUtenteAggiornamento, vAllevamentiAziendaNuova);
  }

  public Vector<CCAziendaNuovaVO> getCCAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getCCAziendaNuovaIscrizione(idAziendaNuova);
  }

  public void aggiornaCCAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO,
      long idUtenteAggiornamento, Vector<CCAziendaNuovaVO> vCCAziendaNuova)
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaCCAziendaNuovaIscrizione(aziendaNuovaVO,
        idUtenteAggiornamento, vCCAziendaNuova);
  }

  public Vector<RichiestaAziendaDocumentoVO> getAllegatiAziendaNuovaIscrizione(
      long idAziendaNuova, long idTipoRichiesta) throws SolmrException,
      Exception
  {
    return nuovaIscrizioneGaaLocal.getAllegatiAziendaNuovaIscrizione(
        idAziendaNuova, idTipoRichiesta);
  }

  public Long insertRichAzDocAziendaNuova(
      RichiestaAziendaDocumentoVO richiestaAziendaDocumentoVO)
      throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal
        .insertRichAzDocAziendaNuova(richiestaAziendaDocumentoVO);
  }

  public void deleteDocumentoRichiesta(long idRichiestaDocumento)
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.deleteDocumentoRichiesta(idRichiestaDocumento);
  }

  public void insertFileStampa(long idRichiestaAzienda, byte ba[])
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.insertFileStampa(idRichiestaAzienda, ba);
  }

  public void aggiornaStatoNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO,
      long idUtenteAggiornamento, IterRichiestaAziendaVO iterRichiestaAziendaVO)
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaStatoNuovaIscrizione(aziendaNuovaVO,
        idUtenteAggiornamento, iterRichiestaAziendaVO);
  }

  public void aggiornaStatoRichiestaValCess(AziendaNuovaVO aziendaNuovaVO,
      long idUtenteAggiornamento, IterRichiestaAziendaVO iterRichiestaAziendaVO)
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaStatoRichiestaValCess(aziendaNuovaVO,
        idUtenteAggiornamento, iterRichiestaAziendaVO);
  }

  public Vector<Long> getElencoIdRichiestaAzienda(Long idTipoRichiesta,
      Long idStatoRichiesta, String cuaa, String partitaIva,
      String denominazione, Long idAzienda, RuoloUtenza ruoloUtenza) throws SolmrException,
      Exception
  {
    return nuovaIscrizioneGaaLocal.getElencoIdRichiestaAzienda(idTipoRichiesta,
        idStatoRichiesta, cuaa, partitaIva, denominazione, idAzienda, ruoloUtenza);
  }
  
  public Vector<Long> getElencoIdRichiestaAziendaGestCaa(
      Long idTipoRichiesta, Long idStatoRichiesta, String cuaa, String partitaIva, String denominazione, 
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getElencoIdRichiestaAziendaGestCaa(idTipoRichiesta, 
        idStatoRichiesta, cuaa, partitaIva, denominazione, ruoloUtenza);
  }
  
  public Vector<Long> getElencoRichieseteAziendaByIdRichiestaAzienda(long idAzienda, String codiceRuolo) 
      throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getElencoRichieseteAziendaByIdRichiestaAzienda(idAzienda, codiceRuolo);
  }

  public Vector<AziendaNuovaVO> getElencoAziendaNuovaByIdRichiestaAzienda(
      Vector<Long> vIdRichiestaAzienda) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal
        .getElencoAziendaNuovaByIdRichiestaAzienda(vIdRichiestaAzienda);
  }

  public Vector<CodeDescription> getListTipoRichiesta() throws SolmrException,
      Exception
  {
    return nuovaIscrizioneGaaLocal.getListTipoRichiesta();
  }
  
  public Vector<CodeDescription> getListTipoRichiestaVariazione(String codiceRuolo) throws SolmrException,
    Exception
  {
    return nuovaIscrizioneGaaLocal.getListTipoRichiestaVariazione(codiceRuolo);
  }

  public Vector<StatoRichiestaVO> getListStatoRichiesta()
      throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getListStatoRichiesta();
  }

  public RichiestaAziendaVO getPdfAziendaNuova(long idRichiestaAzienda)
      throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getPdfAziendaNuova(idRichiestaAzienda);
  }

  public PlSqlCodeDescription ribaltaAziendaPlSql(long idRichiestaAzienda)
      throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.ribaltaAziendaPlSql(idRichiestaAzienda);
  }

  public boolean isPartitaIvaPresente(String partitaIva, long[] arrTipoRichiesta)
      throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.isPartitaIvaPresente(partitaIva,
        arrTipoRichiesta);
  }

  public void updateFlagDichiarazioneAllegati(long idRichiestaAzienda,
      String flagDichiarazioneAllegati) throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.updateFlagDichiarazioneAllegati(idRichiestaAzienda,
        flagDichiarazioneAllegati);
  }

  public Vector<MotivoRichiestaVO> getListMotivoRichiesta(int idTipoRichiesta)
      throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getListMotivoRichiesta(idTipoRichiesta);
  }

  public AziendaNuovaVO getRichAzByIdAzienda(long idAzienda,
      long idTipoRichiesta) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getRichAzByIdAzienda(idAzienda,
        idTipoRichiesta);
  }
  
  public AziendaNuovaVO getRichAzByIdAziendaConValida(
      long idAzienda, long idTipoRichiesta) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getRichAzByIdAziendaConValida(idAzienda,
        idTipoRichiesta);
  }

  public void updateRichiestaAzienda(RichiestaAziendaVO richiestaAziendaVO)
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.updateRichiestaAzienda(richiestaAziendaVO);
  }
  
  public Vector<SoggettoAziendaNuovaVO> getSoggettiAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getSoggettiAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public SoggettoAziendaNuovaVO getRappLegaleNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return nuovaIscrizioneGaaLocal.getRappLegaleNuovaIscrizione(idAziendaNuova);
  }
  
  public void aggiornaSoggettiAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<SoggettoAziendaNuovaVO> vSoggettoAziendaNuova)     
        throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaSoggettiAziendaNuovaIscrizione(aziendaNuovaVO, 
        idUtenteAggiornamento, vSoggettoAziendaNuova);
  }
  
  public void aggiornaMacchineIrrAziendaNuova(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<MacchinaAziendaNuovaVO> vMacchineNuovaRichiesta)     
        throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaMacchineIrrAziendaNuova(aziendaNuovaVO, 
        idUtenteAggiornamento, vMacchineNuovaRichiesta);
  }
  
  public void aggiornaAzAssociateCaaAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaAzAssociateCaaAziendaNuovaIscrizione(aziendaNuovaVO, 
        idUtenteAggiornamento, vAssAziendaNuova);
  }
  
  public void aggiornaAzAssociateCaaRichiestaVariazione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaAzAssociateCaaRichiestaVariazione(
        aziendaNuovaVO, idUtenteAggiornamento, vAssAziendaNuova);
  }
  
  public void aggiornaAzAssociateRichiestaVariazione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaAzAssociateRichiestaVariazione(
        aziendaNuovaVO, idUtenteAggiornamento, vAssAziendaNuova);
  }
  
  public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return  nuovaIscrizioneGaaLocal.getAziendeAssociateCaaAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaRichVariazione(
      long idRichiestaAzienda) throws SolmrException, Exception
  {
    return  nuovaIscrizioneGaaLocal.getAziendeAssociateCaaAziendaRichVariazione(idRichiestaAzienda);
  }
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return  nuovaIscrizioneGaaLocal.getAziendeAssociateAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaRichVariazione(
      long idRichiestaAzienda) throws SolmrException, Exception
  {
    return  nuovaIscrizioneGaaLocal.getAziendeAssociateAziendaRichVariazione(idRichiestaAzienda);
  }
  
  public void aggiornaAzAssociateAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.aggiornaAzAssociateAziendaNuovaIscrizione(aziendaNuovaVO, 
        idUtenteAggiornamento, vAssAziendaNuova);
  }
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateCaaStampaAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return  nuovaIscrizioneGaaLocal.getAziendeAssociateCaaStampaAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public void caricaMacchineNuovaRichiesta(long idAzienda, long idRichiestaAzienda) 
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.caricaMacchineNuovaRichiesta(idAzienda, idRichiestaAzienda);
  }
  
  public void caricaAziendeAssociateRichiesta(long idAzienda, long idRichiestaAzienda, String flagSoloAggiunta)     
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.caricaAziendeAssociateRichiesta(idAzienda, idRichiestaAzienda, flagSoloAggiunta);
  }
  
  public void caricaAziendeAssociateCaaRichiesta(long idAzienda, long idRichiestaAzienda)     
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.caricaAziendeAssociateCaaRichiesta(idAzienda, idRichiestaAzienda);
  }
  
  public void ribaltaMacchineNuovaRichiesta(long idRichiestaAzienda, long idUtenteAggiornamento)     
      throws SolmrException, Exception
  {
    nuovaIscrizioneGaaLocal.ribaltaMacchineNuovaRichiesta(idRichiestaAzienda, idUtenteAggiornamento);
  }
  
  public Vector<MacchinaAziendaNuovaVO> getMacchineAzNuova(long idRichiestaAzienda)
      throws SolmrException, Exception
  {
    return  nuovaIscrizioneGaaLocal.getMacchineAzNuova(idRichiestaAzienda);
  }
  
  public boolean isUtenteAbilitatoPresaInCarico(long idTipoRichiesta, String codiceRuolo) 
      throws SolmrException, Exception
  {
    return  nuovaIscrizioneGaaLocal.isUtenteAbilitatoPresaInCarico(idTipoRichiesta, codiceRuolo);
  }

  /*******************************************************************/
  /******************* NUOVA ISCRIZIONE END *************************/
  /*******************************************************************/

  /** ******************************************** */
  /** ***** MESSAGGISTICA START ****** */
  /** ******************************************** */

  public void confermaLetturaMessaggio(long idElencoMessaggi,
      String codiceFiscale) throws SolmrException, Exception
  {
    messaggisticaGaaLocal.confermaLetturaMessaggio(idElencoMessaggi,
        codiceFiscale);
  }

  public byte[] getAllegato(long idAllegato) throws SolmrException,
      Exception
  {
    return messaggisticaGaaLocal.getAllegato(idAllegato);
  }

  public ListaMessaggi getListaMessaggi(int idProcedimento, String codiceRuolo,
      String codiceFiscale, int tipoMessaggio, Boolean letto,
      Boolean obbligatorio, Boolean visibile) throws SolmrException,
      LogoutException, Exception
  {
    return messaggisticaGaaLocal.getListaMessaggi(idProcedimento, codiceRuolo,
        codiceFiscale, tipoMessaggio, letto, obbligatorio, visibile);
  }

  public DettagliMessaggio getDettagliMessaggio(long idElencoMessaggi,
      String codiceFiscale) throws SolmrException, Exception
  {
    return messaggisticaGaaLocal.getDettagliMessaggio(idElencoMessaggi,
        codiceFiscale);
  }

  /** ******************************************** */
  /** ***** MESSAGGISTICA END ****** */
  /** ******************************************** */

  /** ******************************************** */
  /** ***** WSBRIDGE START ****** */
  /** ******************************************** */

  public Hashtable<BigDecimal, SianAllevamentiVO> leggiAnagraficaAllevamenti(
      String cuaa, String dataRichiesta) throws SolmrException, Exception
  {
    Hashtable<BigDecimal, SianAllevamentiVO> allevamenti = null;
    allevamenti = wsBridgeGaaLocal.serviceAnagraficaAllevamenti(cuaa,
        dataRichiesta);

    if (Validator.isNotEmpty(allevamenti)
        && allevamenti.containsKey(new BigDecimal(-1)))
    {
      /**
       * Non ho trovato nessun allevamento con questo CUAA, quindi provo a
       * vedere se ci sono CUAA collegati
       */
      String cuaaColl[] = anagAziendaLocal.getCUAACollegati(cuaa);
      if (cuaaColl != null && cuaaColl.length > 0)
      {
        // scorro tutti i CUAA in cerca di uno che abbia degli allevamenti... al
        // primo che mi risponde in
        // modo positivo mi fermo
        for (int i = 0; i < cuaaColl.length; i++)
        {
          allevamenti.remove(new BigDecimal(-1));

          allevamenti = wsBridgeGaaLocal.serviceAnagraficaAllevamenti(
              cuaaColl[i], dataRichiesta);
          if (Validator.isNotEmpty(allevamenti)
              && !allevamenti.containsKey(new BigDecimal(-1)))
          {
            break;
          }
        }
      }
    }

    return allevamenti;
  }

  public Hashtable<BigDecimal, SianAllevamentiVO> leggiAnagraficaAllevamentiNoProfile(
      String cuaa, String dataRichiesta) throws SolmrException, Exception
  {
    return wsBridgeGaaLocal.serviceAnagraficaAllevamenti(cuaa, dataRichiesta);
  }

  public ResponseWsBridgeVO serviceConsistenzaStatisticaMediaAllevamento(
      String cuaa) throws SolmrException, Exception
  {
    return wsBridgeGaaLocal.serviceConsistenzaStatisticaMediaAllevamento(cuaa);
  }

  public ResponseWsBridgeVO serviceConsistenzaUbaCensimOvini(String cuaa)
      throws SolmrException, Exception
  {
    return wsBridgeGaaLocal.serviceConsistenzaUbaCensimOvini(cuaa);
  }

  public void serviceWsBridgeAggiornaDatiBDN(String cuaa)
      throws SolmrException, Exception
  {
    wsBridgeGaaLocal.serviceWsBridgeAggiornaDatiBDN(cuaa);
  }

  /** ******************************************** */
  /** ***** WSBRIDGE END ****** */
  /** ******************************************** */

  /** ******************************************** */
  /** ***** AgriWell START ****** */
  /** ******************************************** */

  public void insertFileAllegatoAgriWell(AllegatoDocumentoVO allegatoDocumentoVO)
      throws SolmrException, Exception
  {

    // Controlli
    if (!Validator.isFilenameValid(allegatoDocumentoVO.getNomeFisico()))
    {
      throw new SolmrException(AnagErrors.ERRORE_NOME_FILE_ERRATO);
    }

    String estensione = FileUtils.getExtension(
        allegatoDocumentoVO.getNomeFisico(), ".");
    if (Validator.isEmpty(estensione))
    {
      throw new SolmrException(AnagErrors.ERRORE_NOME_FILE_ERRATO_ESTENSIONE);
    }

    try
    {
      DocumentoVO documentoVO = documentoLocal.getDettaglioDocumento(
          allegatoDocumentoVO.getIdDocumento(), true);

      // Creo l'oggetto da passare al servzio!!!
      AgriWellDocumentoVO agriWellDocumentoVO = new AgriWellDocumentoVO();
      agriWellDocumentoVO.setIdProcedimento(new Long(
          SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
      agriWellDocumentoVO.setNomeFile(allegatoDocumentoVO.getNomeFisico());
      agriWellDocumentoVO.setIdTipoDocumento(documentoVO.getTipoDocumentoVO()
          .getExtIdTipoDocumentoIndex());
      agriWellDocumentoVO.setContenutoFile(allegatoDocumentoVO
          .getFileAllegato());
      agriWellDocumentoVO.setEstensione(estensione);
      agriWellDocumentoVO.setIdUtenteAggiornamento(allegatoDocumentoVO
          .getIdUtenteAggiornamento());
      agriWellDocumentoVO.setDataInserimento(Calendar.getInstance()); 
      agriWellDocumentoVO.setNoteDocumento(documentoVO.getNote());
      agriWellDocumentoVO
          .setNumeroProtocollo(documentoVO.getNumeroProtocollo());
      agriWellDocumentoVO.setDataProtocollo(it.csi.solmr.util.DateUtils.convertDate(documentoVO.getDataProtocollo()));
      agriWellDocumentoVO.setAnno(DateUtils.getCurrentYear());
      agriWellDocumentoVO.setNomeCartella("ANAGRAFE");
      agriWellDocumentoVO.setExtIdAzienda(documentoVO.getIdAzienda());

      Vector<AgriWellMetadatoVO> vAgriWellMetadatoVO = new Vector<AgriWellMetadatoVO>();
      AgriWellMetadatoVO agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("id_tipo_visibilita");
      agriWellMetadatoVO.setValoreEtichetta("1");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("id_documento_anagrafe");
      agriWellMetadatoVO.setValoreEtichetta("" + documentoVO.getIdDocumento());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      if (Validator.isNotEmpty(documentoVO.getNumeroDocumento()))
      {
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("numero_documento");
        agriWellMetadatoVO.setValoreEtichetta(documentoVO.getNumeroDocumento());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      }
      if (Validator.isNotEmpty(documentoVO.getEnteRilascioDocumento()))
      {
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("ente_rilascio_documento");
        agriWellMetadatoVO.setValoreEtichetta(documentoVO
            .getEnteRilascioDocumento());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      }
      if (Validator.isNotEmpty(documentoVO.getNumeroProtocolloEsterno()))
      {
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("numero_protocollo_esterno");
        agriWellMetadatoVO.setValoreEtichetta(documentoVO
            .getNumeroProtocolloEsterno());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      }
      if (Validator.isNotEmpty(documentoVO.getUtenteAggiornamento()
          .getCodiceEnte()))
      {
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("codice_ente_utente");
        agriWellMetadatoVO.setValoreEtichetta(documentoVO
            .getUtenteAggiornamento().getCodiceEnte());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      }
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("titolo_documento");
      agriWellMetadatoVO
          .setValoreEtichetta(allegatoDocumentoVO.getNomeLogico());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("cuaa");
      agriWellMetadatoVO.setValoreEtichetta(documentoVO.getCuaa());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("classificazione_regione");
      agriWellMetadatoVO.setValoreEtichetta("5.80");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("fascicolazione");
      agriWellMetadatoVO.setValoreEtichetta("fa");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      
      if ("S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
      {
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("fase_istanza_riesame");
        agriWellMetadatoVO.setValoreEtichetta(""
            + documentoVO.getFaseIstanzaRiesame());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("istanza_extrasistema");
        agriWellMetadatoVO.setValoreEtichetta(documentoVO
            .getExtraSistemaIstanzaRiesame());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
        agriWellMetadatoVO = new AgriWellMetadatoVO();
      }
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("data_inizio");
      agriWellMetadatoVO.setValoreEtichetta(DateUtils
          .formatDateTimeNotNull(documentoVO.getDataInizioValidita()));
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      if (Validator.isNotEmpty(documentoVO.getDataFineValidita()))
      {
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("data_fine");
        agriWellMetadatoVO.setValoreEtichetta(DateUtils
            .formatDateTimeNotNull(documentoVO.getDataFineValidita()));
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      }
      if (Validator.isNotEmpty(documentoVO.getCuaaSoccidario()))
      {
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("cuaa_soccidario");
        agriWellMetadatoVO.setValoreEtichetta(documentoVO.getCuaaSoccidario());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      }
      if (Validator.isNotEmpty(documentoVO.getIdContoCorrente()))
      {
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("id_conto_corrente");
        agriWellMetadatoVO.setValoreEtichetta(""
            + documentoVO.getIdContoCorrente());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      }
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_firmato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_verificato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);

      agriWellDocumentoVO
          .setAMetadati((AgriWellMetadatoVO[]) vAgriWellMetadatoVO
              .toArray(new AgriWellMetadatoVO[vAgriWellMetadatoVO.size()]));

      AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
          .agriwellServiceScriviDoquiAgri(agriWellDocumentoVO);

      if (Validator.isNotEmpty(agriWellEsitoVO)
          && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito()
              .intValue())
      {
        allegatoDocumentoVO.setExtIdDocumentoIndex(agriWellEsitoVO
            .getIdDocumentoIndex());
        Long idAllegato = documentoGaaLocal
            .insertFileAllegatoNoFile(allegatoDocumentoVO);
        documentoGaaLocal.insertAllegatoDocumento(allegatoDocumentoVO
            .getIdDocumento().longValue(), idAllegato.longValue());
      }
      else
      {
        String messaggio = "Attenzione si è verificato un problema nell'inserimento agriwell ";
        if (Validator.isNotEmpty(agriWellEsitoVO))
        {
          messaggio += "-" + agriWellEsitoVO.getMessaggio();
        }
        throw new SolmrException(messaggio);
      }

    }
    catch (Throwable t)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(t.getMessage());
    }
  }
  
  public Long insertFileRichiestaAgriWell(AziendaNuovaVO aziendaNuovaVO, byte[] fileStampa, RuoloUtenza ruoloUtenza) 
      throws SolmrException, Exception
  {
    try
    {
         
      //Creo l'oggetto da passare al servzio!!!
      AgriWellDocumentoVO agriWellDocumentoVO = new AgriWellDocumentoVO();
      agriWellDocumentoVO.setIdProcedimento(new Long(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
      agriWellDocumentoVO.setNomeFile("richiestaIscrizione.pdf");
      agriWellDocumentoVO.setIdTipoDocumento(new Long(402));
      agriWellDocumentoVO.setContenutoFile(fileStampa);
      agriWellDocumentoVO.setEstensione("pdf");
      agriWellDocumentoVO.setIdUtenteAggiornamento(aziendaNuovaVO.getIdUtenteAggiornamentoRich());
      agriWellDocumentoVO.setDataInserimento(Calendar.getInstance());
      agriWellDocumentoVO.setNoteDocumento(aziendaNuovaVO.getNoteRichiestaAzienda());
      agriWellDocumentoVO.setAnno(DateUtils.getCurrentYear());
      agriWellDocumentoVO.setNomeCartella("ANAGRAFE");
      agriWellDocumentoVO.setExtIdAzienda(aziendaNuovaVO.getIdAzienda());
      
      Vector<AgriWellMetadatoVO> vAgriWellMetadatoVO = new Vector<AgriWellMetadatoVO>();
      AgriWellMetadatoVO agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("id_tipo_visibilita");
      agriWellMetadatoVO.setValoreEtichetta("1");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("codice_ente_utente");
      agriWellMetadatoVO.setValoreEtichetta(ruoloUtenza.getCodiceEnte());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("titolo_documento");
      agriWellMetadatoVO.setValoreEtichetta("richiesta iscrizione");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("cuaa");
      agriWellMetadatoVO.setValoreEtichetta(aziendaNuovaVO.getCuaa());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("classificazione_regione");
      agriWellMetadatoVO.setValoreEtichetta("5.80");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("fascicolazione");
      agriWellMetadatoVO.setValoreEtichetta("fa");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_firmato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_verificato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);      
      
      agriWellDocumentoVO.setAMetadati((AgriWellMetadatoVO[])vAgriWellMetadatoVO.toArray(new AgriWellMetadatoVO[vAgriWellMetadatoVO.size()]));
      
      AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal.agriwellServiceScriviDoquiAgri(agriWellDocumentoVO);
      
      if(Validator.isNotEmpty(agriWellEsitoVO)
        && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito().intValue())
      {
        nuovaIscrizioneGaaLocal.updateRichiestaAziendaIndex(aziendaNuovaVO.getIdRichiestaAzienda().longValue(), 
            agriWellEsitoVO.getIdDocumentoIndex().longValue());
      }
      else
      {
        String messaggio = "Attenzione si è verificato un problema nell'inserimento agriwell ";
        if(Validator.isNotEmpty(agriWellEsitoVO))
        {
          messaggio += "-"+agriWellEsitoVO.getMessaggio();
        }
        throw new SolmrException(messaggio);        
      }
      
      
      return agriWellEsitoVO.getIdDocumentoIndex();
      
    }
    catch (Throwable t)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(t.getMessage());
    }    
  }

  public Long insertFileValidazioneAgriWell(AnagAziendaVO anagAziendaVO,
      Long idDichiarazioneConsistenza, byte[] fileStampa,
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception
  {
    try
    {

      ConsistenzaVO consistenzaVO = consistenzaLocal
          .findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
      
      
      TipoAllegatoVO tipoAllegatoVO = stampaGaaLocal.getTipoAllegatoById(SolmrConstants.VALIDAZIONE_ALLEGATO);
      String nomeFileXDP = tipoAllegatoVO.getNomeFileXDP();
      Vector<RichiestaTipoReportVO> vRichiestaCoordinate = stampaGaaLocal.getElencoCoordinateFirma(SolmrConstants.VALIDAZIONE_ALLEGATO);
      
      /*
      //calcolo dati per pdf
      AttributiModuloVO attributiModuloVO = new AttributiModuloVO(SolmrConstants.CODICE_APPLICAZIONE_MODOL,
          SolmrConstants.DESCRIZIONE_APPLICAZIONE_MODOL, nomeFileXDP,
          nomeFileXDP, SolmrConstants.RIF_ADOBE_MODOL+""+nomeFileXDP+".xdp");
      
      byte[] fileStampaStatic = modolServGaaLocal.trasformStaticPDF(fileStampa, attributiModuloVO);
      
     
      PdfReader reader = new PdfReader(fileStampaStatic);
      int numeroPagine = reader.getNumberOfPages();
      reader.close();
      */int numeroPagine = 0;

      // Creo l'oggetto da passare al servzio!!!
      AgriWellDocumentoVO agriWellDocumentoVO = new AgriWellDocumentoVO();
      agriWellDocumentoVO.setIdProcedimento(new Long(
          SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
      agriWellDocumentoVO.setIdTipoDocumento(consistenzaVO.getExtIdTipoDocumentoIndex());
      agriWellDocumentoVO.setContenutoFile(fileStampa);
      agriWellDocumentoVO.setEstensione("pdf");
      agriWellDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza
          .getIdUtente());
      agriWellDocumentoVO.setDataInserimento(Calendar.getInstance());
      agriWellDocumentoVO.setNoteDocumento(consistenzaVO.getNote());
      if(Validator.isNotEmpty(consistenzaVO.getAnno()))
        agriWellDocumentoVO.setAnno(new Integer(consistenzaVO.getAnno()));
      agriWellDocumentoVO.setNomeCartella("ANAGRAFE");
      agriWellDocumentoVO.setExtIdAzienda(anagAziendaVO.getIdAzienda());
      if(Validator.isNotEmpty(anagAziendaVO.getDelegaVO())
          && Validator.isNotEmpty(anagAziendaVO.getDelegaVO().getIdIntermediario()))
      {
        agriWellDocumentoVO.setExtIdIntermediario(anagAziendaVO.getDelegaVO().getIdIntermediario());
      }
      String nomeFile = "";
      if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo()))
      {        
        
        //UtenteAbilitazioni utenteAbilitazioni =  sianLocal.getUtenteAbilitazioniByIdUtenteLogin(new Long(consistenzaVO.getIdUtente()));
        //RuoloUtenza ruoloUtenzaValidazione = new RuoloUtenzaPapua(utenteAbilitazioni);
        //        if(!ruoloUtenzaValidazione.isUtentePA())
          agriWellDocumentoVO.setDaFirmare("S");
//        else
//          agriWellDocumentoVO.setDaFirmare("U");
        
        agriWellDocumentoVO.setNumeroProtocollo(consistenzaVO.getNumeroProtocollo());
        agriWellDocumentoVO.setDataProtocollo(Calendar.getInstance());
        nomeFile = "Validazione_"+consistenzaVO.getNumeroProtocollo();
      }
      else
      {
        nomeFile = "Validazione_"+consistenzaVO.getIdDichiarazioneConsistenza();
      }
      nomeFile = nomeFile+".pdf"; 
      agriWellDocumentoVO.setNomeFile(nomeFile);
      
      Vector<AgriWellMetadatoVO> vAgriWellMetadatoVO = new Vector<AgriWellMetadatoVO>();
      AgriWellMetadatoVO agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("id_tipo_visibilita");
      agriWellMetadatoVO.setValoreEtichetta("1");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("codice_ente_utente");
      agriWellMetadatoVO.setValoreEtichetta(ruoloUtenza.getCodiceEnte());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("titolo_documento");
      agriWellMetadatoVO.setValoreEtichetta("validazione");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("cuaa");
      agriWellMetadatoVO.setValoreEtichetta(anagAziendaVO.getCUAA());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("classificazione_regione");
      agriWellMetadatoVO.setValoreEtichetta("5.80");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("fascicolazione");
      agriWellMetadatoVO.setValoreEtichetta("fa");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_firmato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_verificato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("data_inizio");
      agriWellMetadatoVO.setValoreEtichetta(DateUtils
          .formatDateTimeNotNull(consistenzaVO.getDataInserimentoDichiarazione()));
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      
      
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("Firmatario");
      agriWellMetadatoVO.setValoreEtichetta(anagAziendaLocal.getRappLegaleTitolareByIdAzienda(anagAziendaVO.getIdAzienda()));
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);     
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("paginaFirma");
      agriWellMetadatoVO.setValoreEtichetta(""+numeroPagine);
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      if(vRichiestaCoordinate != null)
      {
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("coordinataXsinistra");
        BigDecimal coordinata = vRichiestaCoordinate.get(0).getPosFirmaGrafoXSinistra();
        agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("coordinataXdestra");
        coordinata = vRichiestaCoordinate.get(0).getPosFirmaGrafoXDestra();
        agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("coordinataYalto");
        coordinata = vRichiestaCoordinate.get(0).getPosFirmaGrafoYAlto();
        agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("coordinataYbasso");
        coordinata = vRichiestaCoordinate.get(0).getPosFirmaGrafoYBasso();
        agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      }

      agriWellDocumentoVO
          .setAMetadati((AgriWellMetadatoVO[]) vAgriWellMetadatoVO
              .toArray(new AgriWellMetadatoVO[vAgriWellMetadatoVO.size()]));

      AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
          .agriwellServiceScriviDoquiAgri(agriWellDocumentoVO);

      /*AllegatoDocumentoVO allegatoDocumentoVO = new AllegatoDocumentoVO();
      allegatoDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
      allegatoDocumentoVO.setDataRagistrazione(new Date());
      allegatoDocumentoVO.setDataUltimoAggiornamento(new Date());
      allegatoDocumentoVO.setNomeFisico(nomeFile);
      allegatoDocumentoVO.setNomeLogico(nomeFile);
      allegatoDocumentoVO.setIdTipoAllegato(new Long(SolmrConstants.VALIDAZIONE_ALLEGATO));*/
      if (Validator.isNotEmpty(agriWellEsitoVO)
          && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito()
              .intValue())
      {
        
        
        AllegatoDichiarazioneVO allegatoDichiarazione = documentoGaaLocal
            .getAllegatoDichiarazioneFromIdDichiarazione(new Long(consistenzaVO.getIdDichiarazioneConsistenza())
            .longValue(), new Long(SolmrConstants.VALIDAZIONE_ALLEGATO)); 
        
        if(Validator.isNotEmpty(allegatoDichiarazione))
        {
          documentoGaaLocal.updateAllegatoForExtIdDocIndex(allegatoDichiarazione.getIdAllegato(), 
            ruoloUtenza.getIdUtente(), agriWellEsitoVO
            .getIdDocumentoIndex());
        }
        else
        {
          String messaggio = "Attenzione si è verificato un problema non esiste un record attivo " +
              "su db_allegato_dichiarazione per la validazione ";
          if (Validator.isNotEmpty(agriWellEsitoVO))
          {
            messaggio += "-" + agriWellEsitoVO.getMessaggio();
          }
          
          SolmrLogger.error(this, messaggio);
          
          throw new SolmrException(messaggio);
        }
        
        
        
        /*allegatoDocumentoVO.setExtIdDocumentoIndex(agriWellEsitoVO
            .getIdDocumentoIndex());
        Long idAllegato = documentoGaaLocal
            .insertFileAllegatoNoFile(allegatoDocumentoVO);
        documentoGaaLocal.insertAllegatoDichiarazione(
            new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue(),
            idAllegato.longValue());*/
      }
      else
      {
        /*allegatoDocumentoVO.setFileAllegato(fileStampaStatic);
        Long idAllegato = documentoGaaLocal
            .insertFileAllegatoSemplice(allegatoDocumentoVO);
        documentoGaaLocal.insertAllegatoDichiarazione(
            new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue(),
            idAllegato.longValue());*/
        
        
        String messaggio = "Attenzione si è verificato un problema nell'inserimento agriwell ";
        if (Validator.isNotEmpty(agriWellEsitoVO))
        {
          messaggio += "-" + agriWellEsitoVO.getMessaggio();
        }
        SolmrLogger.error(this, messaggio);
        throw new SolmrException(messaggio);
      }

      return agriWellEsitoVO.getIdDocumentoIndex();

    }
    catch (Throwable t)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(t.getMessage());
    }
  }
  
  
  public Long insertFileValidazioneRigeneraAgriWell(AnagAziendaVO anagAziendaVO,
      Long idDichiarazioneConsistenza, byte[] fileStampa,
      RuoloUtenza ruoloUtenza, int idTipoAllegato) throws SolmrException, Exception
  {
    try
    {

      ConsistenzaVO consistenzaVO = consistenzaLocal
          .findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
      TipoAllegatoVO tipoAllegatoVO = stampaGaaLocal.getTipoAllegatoById(idTipoAllegato);
      String nomeFileXDP = tipoAllegatoVO.getNomeFileXDP();
      
      //ricavo i dati dell'allegato
      AllegatoDichiarazioneVO allegatoDichiarazioneVO = documentoGaaLocal
          .getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
              idDichiarazioneConsistenza, new Long(idTipoAllegato));
      
      byte[] fileStampaSave = fileStampa;
    /*  if("S".equalsIgnoreCase(tipoAllegatoVO.getFlagDaFirmare()))
      {
        //calcolo dati per pdf
        AttributiModuloVO attributiModuloVO = new AttributiModuloVO(SolmrConstants.CODICE_APPLICAZIONE_MODOL,
            SolmrConstants.DESCRIZIONE_APPLICAZIONE_MODOL, nomeFileXDP,
            nomeFileXDP, SolmrConstants.RIF_ADOBE_MODOL+""+nomeFileXDP+".xdp");
        
        fileStampaSave = modolServGaaLocal.trasformStaticPDF(fileStampa, attributiModuloVO);
      }*/
      
      // Creo l'oggetto da passare al servzio!!!
      AgriWellDocumentoDaAggiornareVO agriWellDocumentoDaAggiornareVO = new AgriWellDocumentoDaAggiornareVO();
      agriWellDocumentoDaAggiornareVO.setEstensione("pdf");
      agriWellDocumentoDaAggiornareVO
        .setIdDocumentoIndex(allegatoDichiarazioneVO.getExtIdDocumentoIndex());
      agriWellDocumentoDaAggiornareVO.setContenutoFile(fileStampaSave);
      agriWellDocumentoDaAggiornareVO.setIdUtenteAggiornamento(ruoloUtenza
          .getIdUtente());
      String nomeFile = tipoAllegatoVO.getDescrizioneTipoAllegato().replace(" ", "");
      if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo()))
      {
        if("S".equalsIgnoreCase(tipoAllegatoVO.getFlagDaFirmare()))
        {
          //RuoloUtenza ruoloUtenzaValidazione =  smrCommLocal.serviceGetRuoloUtenzaByIdUtente(new Long(consistenzaVO.getIdUtente()));
//          if(!ruoloUtenzaValidazione.isUtentePA())
            agriWellDocumentoDaAggiornareVO.setDaFirmare("S");
//          else
//            agriWellDocumentoDaAggiornareVO.setDaFirmare("U");
        }
        agriWellDocumentoDaAggiornareVO.setNumeroProtocollo(consistenzaVO.getNumeroProtocollo());
        agriWellDocumentoDaAggiornareVO.setDataProtocollo(Calendar.getInstance());
        nomeFile += "_"+consistenzaVO.getNumeroProtocollo();
      }
      else
      {
        nomeFile += "_"+consistenzaVO.getIdDichiarazioneConsistenza();
      }
      
      nomeFile = nomeFile+".pdf"; 
      agriWellDocumentoDaAggiornareVO.setNomeFile(nomeFile);
      
      
     
      AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
          .agriwellServiceUpdateDoquiAgri(agriWellDocumentoDaAggiornareVO);

      /*AllegatoDocumentoVO allegatoDocumentoVO = new AllegatoDocumentoVO();
      allegatoDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
      allegatoDocumentoVO.setDataRagistrazione(new Date());
      allegatoDocumentoVO.setDataUltimoAggiornamento(new Date());
      allegatoDocumentoVO.setNomeFisico(nomeFile);
      allegatoDocumentoVO.setNomeLogico(nomeFile);
      allegatoDocumentoVO.setIdTipoAllegato(new Long(idTipoAllegato));*/
      if (Validator.isNotEmpty(agriWellEsitoVO)
          && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito()
              .intValue())
      {
        AllegatoDichiarazioneVO allegatoDichiarazione = documentoGaaLocal
            .getAllegatoDichiarazioneFromIdDichiarazione(new Long(consistenzaVO.getIdDichiarazioneConsistenza())
            .longValue(), new Long(tipoAllegatoVO.getIdTipoAllegato())); 
        
        
        if(Validator.isNotEmpty(allegatoDichiarazione))
        {
          documentoGaaLocal.updateAllegatoForExtIdDocIndex(allegatoDichiarazione.getIdAllegato(), 
            ruoloUtenza.getIdUtente(), agriWellEsitoVO
            .getIdDocumentoIndex());
        }
        else
        {
          String messaggio = "Attenzione si è verificato un problema non esiste un record attivo " +
              "su db_allegato_dichiarazione per quelli inseribili ";
          if (Validator.isNotEmpty(agriWellEsitoVO))
          {
            messaggio += "-" + agriWellEsitoVO.getMessaggio();
          }
          
          SolmrLogger.error(this, messaggio);
          
          throw new SolmrException(messaggio);
        }
        
      }
      else
      {
        String messaggio = "Attenzione si è verificato un problema nell'inserimento agriwell ";
        if (Validator.isNotEmpty(agriWellEsitoVO))
        {
          messaggio += "-" + agriWellEsitoVO.getMessaggio();
        }
        SolmrLogger.error(this, messaggio);
        throw new SolmrException(messaggio);
      }

      return agriWellEsitoVO.getIdDocumentoIndex();

    }
    catch (Throwable t)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(t.getMessage());
    }
  }
  
  
  public Long insertFileValidazioneAllegatiRigeneraAgriWell(AnagAziendaVO anagAziendaVO,
      Long idDichiarazioneConsistenza, byte[] fileStampa,
      RuoloUtenza ruoloUtenza, int idTipoAllegato) throws SolmrException, Exception
  {
    try
    {

      ConsistenzaVO consistenzaVO = consistenzaLocal
          .findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
      TipoAllegatoVO tipoAllegatoVO = stampaGaaLocal.getTipoAllegatoById(idTipoAllegato);
      String nomeFileXDP = tipoAllegatoVO.getNomeFileXDP();
      
      Vector<RichiestaTipoReportVO> vRichiestaCoordinate = stampaGaaLocal.getElencoCoordinateFirma(idTipoAllegato);
      
      //ricavo i dati dell'allegato
      AllegatoDichiarazioneVO allegatoDichiarazioneVO = documentoGaaLocal
          .getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
              idDichiarazioneConsistenza, new Long(idTipoAllegato));
      
      
      int numeroPagine = 0;
      byte[] fileStampaSave = fileStampa;
      /*if("S".equalsIgnoreCase(tipoAllegatoVO.getFlagDaFirmare()))
      {
        //calcolo dati per pdf
        AttributiModuloVO attributiModuloVO = new AttributiModuloVO(SolmrConstants.CODICE_APPLICAZIONE_MODOL,
            SolmrConstants.DESCRIZIONE_APPLICAZIONE_MODOL, nomeFileXDP,
            nomeFileXDP, SolmrConstants.RIF_ADOBE_MODOL+""+nomeFileXDP+".xdp");
        
        fileStampaSave = modolServGaaLocal.trasformStaticPDF(fileStampa, attributiModuloVO);
        
        PdfReader reader = new PdfReader(fileStampaSave);
        numeroPagine = reader.getNumberOfPages();
        reader.close();
      }*/
      
      Date dataAttuale = new Date();
      
      // Creo l'oggetto da passare al servzio!!!
      AgriWellDocumentoDaAggiornareVO agriWellDocumentoDaAggiornareVO = new AgriWellDocumentoDaAggiornareVO();
      agriWellDocumentoDaAggiornareVO.setEstensione("pdf");
      agriWellDocumentoDaAggiornareVO
        .setIdDocumentoIndex(allegatoDichiarazioneVO.getExtIdDocumentoIndex());
      agriWellDocumentoDaAggiornareVO.setContenutoFile(fileStampaSave);
      agriWellDocumentoDaAggiornareVO.setIdUtenteAggiornamento(ruoloUtenza
          .getIdUtente());
      String nomeFile = tipoAllegatoVO.getDescrizioneTipoAllegato().replace(" ", "");
      if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo()))
      {
        if("S".equalsIgnoreCase(tipoAllegatoVO.getFlagDaFirmare()))
        {
          //RuoloUtenza ruoloUtenzaValidazione =  smrCommLocal.serviceGetRuoloUtenzaByIdUtente(new Long(consistenzaVO.getIdUtente()));
//          if(!ruoloUtenzaValidazione.isUtentePA())
            agriWellDocumentoDaAggiornareVO.setDaFirmare("S");
//          else
//            agriWellDocumentoDaAggiornareVO.setDaFirmare("U");
        }
        agriWellDocumentoDaAggiornareVO.setNumeroProtocollo(consistenzaVO.getNumeroProtocollo());
        agriWellDocumentoDaAggiornareVO.setDataProtocollo(Calendar.getInstance());
        nomeFile += "_"+consistenzaVO.getNumeroProtocollo();
      }
      else
      {
        nomeFile += "_"+consistenzaVO.getIdDichiarazioneConsistenza();
      }
      
      nomeFile = nomeFile+".pdf"; 
      agriWellDocumentoDaAggiornareVO.setNomeFile(nomeFile);
      
      Vector<AgriWellMetadatoVO> vAgriWellMetadatoVO = new Vector<AgriWellMetadatoVO>();
      AgriWellMetadatoVO agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("id_tipo_visibilita");
      agriWellMetadatoVO.setValoreEtichetta("1");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("codice_ente_utente");
      agriWellMetadatoVO.setValoreEtichetta(ruoloUtenza.getCodiceEnte());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("titolo_documento");
      agriWellMetadatoVO.setValoreEtichetta("allegatoValidazione");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("cuaa");
      agriWellMetadatoVO.setValoreEtichetta(anagAziendaVO.getCUAA());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("classificazione_regione");
      agriWellMetadatoVO.setValoreEtichetta("5.80");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("fascicolazione");
      agriWellMetadatoVO.setValoreEtichetta("fa");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_firmato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_verificato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("data_inizio");
      agriWellMetadatoVO.setValoreEtichetta(DateUtils
          .formatDateTimeNotNull(dataAttuale));
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      
      
      if("S".equalsIgnoreCase(tipoAllegatoVO.getFlagDaFirmare()))
      {
        agriWellMetadatoVO = new AgriWellMetadatoVO();
        agriWellMetadatoVO.setNomeEtichetta("paginaFirma");
        agriWellMetadatoVO.setValoreEtichetta(""+numeroPagine);
        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
        
        if(vRichiestaCoordinate != null)
        {
          for(int j=0;j<vRichiestaCoordinate.size();j++)
          {
            String pedice = "";
            if(j!=0)
              pedice = ""+j;
            
            
            String firmatario = "";
            if("T".equalsIgnoreCase(vRichiestaCoordinate.get(j).getCodiceFirmatario()))
            {
              PersonaFisicaVO personaFisicaVO = anagAziendaLocal
                 .getRappresentanteLegaleFromIdAnagAziendaAndDichCons(anagAziendaVO
                 .getIdAnagAzienda().longValue(), consistenzaVO.getDataInserimentoDichiarazione());    
              firmatario = personaFisicaVO.getCognome()+" "+personaFisicaVO.getNome();
            }
            else if("O".equalsIgnoreCase(vRichiestaCoordinate.get(j).getCodiceFirmatario()))
            {
              AllegatoDichiarazioneVO allegatoDichiarazioneTmpVO = 
                  documentoGaaLocal.getAllegatoDichiarazioneFromIdDichiarazione(
                  idDichiarazioneConsistenza, tipoAllegatoVO.getIdTipoAllegato());
              
              UtenteAbilitazioni utenteAbilitazioni =  sianLocal.getUtenteAbilitazioniByIdUtenteLogin(allegatoDichiarazioneTmpVO.getIdUtenteAggiornamento());
              RuoloUtenza ruoloUtenzaFirm = new RuoloUtenzaPapua(utenteAbilitazioni);
              //RuoloUtenza ruoloUtenzaFirm = smrCommLocal.serviceGetRuoloUtenzaByIdUtente(allegatoDichiarazioneTmpVO.getIdUtenteAggiornamento());
              firmatario = ruoloUtenzaFirm.getDenominazione();
            
            }
            else if("R".equalsIgnoreCase(vRichiestaCoordinate.get(j).getCodiceFirmatario()))
            {
              InfoFascicoloVO infoFascicoloTempVO = stampaGaaLocal
                  .getInfoFascicolo(anagAziendaVO.getIdAzienda(), consistenzaVO.getDataDichiarazione(), consistenzaVO.getCodiceFotografiaTerreni());
              
              firmatario = infoFascicoloTempVO.getResponsabile();        
            }
            
            if(Validator.isEmpty(firmatario))
              firmatario = "firmatario1";
            
            agriWellMetadatoVO = new AgriWellMetadatoVO();
            agriWellMetadatoVO.setNomeEtichetta("firmatario"+pedice);
            agriWellMetadatoVO.setValoreEtichetta(firmatario);
            vAgriWellMetadatoVO.add(agriWellMetadatoVO);
            
            
            
            agriWellMetadatoVO = new AgriWellMetadatoVO();
            agriWellMetadatoVO.setNomeEtichetta("coordinataXsinistra"+pedice);
            BigDecimal coordinata = vRichiestaCoordinate.get(j).getPosFirmaGrafoXSinistra();
            agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
            vAgriWellMetadatoVO.add(agriWellMetadatoVO);
            agriWellMetadatoVO = new AgriWellMetadatoVO();
            agriWellMetadatoVO.setNomeEtichetta("coordinataXdestra"+pedice);
            coordinata = vRichiestaCoordinate.get(j).getPosFirmaGrafoXDestra();
            agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
            vAgriWellMetadatoVO.add(agriWellMetadatoVO);
            agriWellMetadatoVO = new AgriWellMetadatoVO();
            agriWellMetadatoVO.setNomeEtichetta("coordinataYalto"+pedice);
            coordinata = vRichiestaCoordinate.get(j).getPosFirmaGrafoYAlto();
            agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
            vAgriWellMetadatoVO.add(agriWellMetadatoVO);
            agriWellMetadatoVO = new AgriWellMetadatoVO();
            agriWellMetadatoVO.setNomeEtichetta("coordinataYbasso"+pedice);
            coordinata = vRichiestaCoordinate.get(j).getPosFirmaGrafoYBasso();
            agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
            vAgriWellMetadatoVO.add(agriWellMetadatoVO);
          }
        }
      }

      agriWellDocumentoDaAggiornareVO
          .setAMetadati((AgriWellMetadatoVO[]) vAgriWellMetadatoVO
              .toArray(new AgriWellMetadatoVO[vAgriWellMetadatoVO.size()]));
      
      
     
      AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
          .agriwellServiceUpdateDoquiAgri(agriWellDocumentoDaAggiornareVO);

      /*AllegatoDocumentoVO allegatoDocumentoVO = new AllegatoDocumentoVO();
      allegatoDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
      allegatoDocumentoVO.setDataRagistrazione(new Date());
      allegatoDocumentoVO.setDataUltimoAggiornamento(new Date());
      allegatoDocumentoVO.setNomeFisico(nomeFile);
      allegatoDocumentoVO.setNomeLogico(nomeFile);
      allegatoDocumentoVO.setIdTipoAllegato(new Long(idTipoAllegato));*/
      if (Validator.isNotEmpty(agriWellEsitoVO)
          && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito()
              .intValue())
      {
        AllegatoDichiarazioneVO allegatoDichiarazione = documentoGaaLocal
            .getAllegatoDichiarazioneFromIdDichiarazione(new Long(consistenzaVO.getIdDichiarazioneConsistenza())
            .longValue(), new Long(tipoAllegatoVO.getIdTipoAllegato())); 
        
        
        if(Validator.isNotEmpty(allegatoDichiarazione))
        {
          documentoGaaLocal.updateAllegatoForExtIdDocIndex(allegatoDichiarazione.getIdAllegato(), 
            ruoloUtenza.getIdUtente(), agriWellEsitoVO
            .getIdDocumentoIndex());
        }
        else
        {
          String messaggio = "Attenzione si è verificato un problema non esiste un record attivo " +
              "su db_allegato_dichiarazione per quelli inseribili ";
          if (Validator.isNotEmpty(agriWellEsitoVO))
          {
            messaggio += "-" + agriWellEsitoVO.getMessaggio();
          }
          
          SolmrLogger.error(this, messaggio);
          
          throw new SolmrException(messaggio);
        }
        
      }
      else
      {
        String messaggio = "Attenzione si è verificato un problema nell'inserimento agriwell ";
        if (Validator.isNotEmpty(agriWellEsitoVO))
        {
          messaggio += "-" + agriWellEsitoVO.getMessaggio();
        }
        SolmrLogger.error(this, messaggio);
        throw new SolmrException(messaggio);
      }

      return agriWellEsitoVO.getIdDocumentoIndex();

    }
    catch (Throwable t)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(t.getMessage());
    }
  }
  
  
  public Long insertFileValidazioneAllegatiAgriWell(AnagAziendaVO anagAziendaVO,
	      Long idDichiarazioneConsistenza, byte[] fileStampa,
	      RuoloUtenza ruoloUtenza, int idTipoAllegato) throws SolmrException, Exception
	  {
	    try
	    {

	      ConsistenzaVO consistenzaVO = consistenzaLocal
	          .findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
	      
	      TipoAllegatoVO tipoAllegatoVO = stampaGaaLocal.getTipoAllegatoById(idTipoAllegato);
	      String nomeFileXDP = tipoAllegatoVO.getNomeFileXDP();
	      
	      Vector<RichiestaTipoReportVO> vRichiestaCoordinate = stampaGaaLocal.getElencoCoordinateFirma(idTipoAllegato);
	      
	      Long extIdTipoDocumentoIndex = tipoAllegatoVO.getExtIdTipoDocumentoIndex();
	      
	      if(extIdTipoDocumentoIndex == null)
	        throw new SolmrException("ext_id_tipo_documento per l'allegato non trovato");
	      
	      
	      //solo per quelli che richiedono la firma
	      byte[] fileStampaSave = fileStampa;
	      int numeroPagine = 0;
	      /*
	      if("S".equalsIgnoreCase(tipoAllegatoVO.getFlagDaFirmare()))
	      {
	        //calcolo dati per pdf
	        AttributiModuloVO attributiModuloVO = new AttributiModuloVO(SolmrConstants.CODICE_APPLICAZIONE_MODOL,
	            SolmrConstants.DESCRIZIONE_APPLICAZIONE_MODOL, nomeFileXDP,
	            nomeFileXDP, SolmrConstants.RIF_ADOBE_MODOL+""+nomeFileXDP+".xdp");
	        
	        fileStampaSave = modolServGaaLocal.trasformStaticPDF(fileStampa, attributiModuloVO);
	          
	        PdfReader reader = new PdfReader(fileStampaSave);
	        numeroPagine = reader.getNumberOfPages();
	        reader.close();
	      }*/
	      
	      Date dataAttuale = new Date();

	      // Creo l'oggetto da passare al servzio!!!
	      AgriWellDocumentoVO agriWellDocumentoVO = new AgriWellDocumentoVO();
	      agriWellDocumentoVO.setIdProcedimento(new Long(
	          SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
	      agriWellDocumentoVO.setIdTipoDocumento(extIdTipoDocumentoIndex);
	      agriWellDocumentoVO.setContenutoFile(fileStampaSave);
	      agriWellDocumentoVO.setEstensione("pdf");
	      agriWellDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza
	          .getIdUtente());
	      agriWellDocumentoVO.setDataInserimento(it.csi.solmr.util.DateUtils.convertDate(dataAttuale));
	      if(Validator.isNotEmpty(consistenzaVO.getAnno()))
	        agriWellDocumentoVO.setAnno(new Integer(consistenzaVO.getAnno()));
	      agriWellDocumentoVO.setNomeCartella("ANAGRAFE");
	      agriWellDocumentoVO.setExtIdAzienda(anagAziendaVO.getIdAzienda());
	      if(Validator.isNotEmpty(anagAziendaVO.getDelegaVO())
	          && Validator.isNotEmpty(anagAziendaVO.getDelegaVO().getIdIntermediario()))
	      {
	        agriWellDocumentoVO.setExtIdIntermediario(anagAziendaVO.getDelegaVO().getIdIntermediario());
	      }
	      String nomeFile = tipoAllegatoVO.getDescrizioneTipoAllegato().replace(" ", "");
	      if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo()))
	      {
	        if("S".equalsIgnoreCase(tipoAllegatoVO.getFlagDaFirmare()))
	        {
	          //RuoloUtenza ruoloUtenzaValidazione =  smrCommLocal.serviceGetRuoloUtenzaByIdUtente(new Long(consistenzaVO.getIdUtente()));
//	          if(!ruoloUtenzaValidazione.isUtentePA())
	            agriWellDocumentoVO.setDaFirmare("S");
//	          else
//	            agriWellDocumentoVO.setDaFirmare("U");
	        }
	        agriWellDocumentoVO.setNumeroProtocollo(consistenzaVO.getNumeroProtocollo());
	        agriWellDocumentoVO.setDataProtocollo(Calendar.getInstance());
	        nomeFile += "_"+consistenzaVO.getNumeroProtocollo();
	      }
	      else
	      {
	        nomeFile += "_"+consistenzaVO.getIdDichiarazioneConsistenza();
	      }
	      nomeFile = nomeFile+".pdf"; 
	      agriWellDocumentoVO.setNomeFile(nomeFile);
	      
	      Vector<AgriWellMetadatoVO> vAgriWellMetadatoVO = new Vector<AgriWellMetadatoVO>();
	      AgriWellMetadatoVO agriWellMetadatoVO = new AgriWellMetadatoVO();
	      agriWellMetadatoVO.setNomeEtichetta("id_tipo_visibilita");
	      agriWellMetadatoVO.setValoreEtichetta("1");
	      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	      agriWellMetadatoVO = new AgriWellMetadatoVO();
	      agriWellMetadatoVO.setNomeEtichetta("codice_ente_utente");
	      agriWellMetadatoVO.setValoreEtichetta(ruoloUtenza.getCodiceEnte());
	      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	      agriWellMetadatoVO = new AgriWellMetadatoVO();
	      agriWellMetadatoVO.setNomeEtichetta("titolo_documento");
	      agriWellMetadatoVO.setValoreEtichetta("allegatoValidazione");
	      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	      agriWellMetadatoVO = new AgriWellMetadatoVO();
	      agriWellMetadatoVO.setNomeEtichetta("cuaa");
	      agriWellMetadatoVO.setValoreEtichetta(anagAziendaVO.getCUAA());
	      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	      agriWellMetadatoVO = new AgriWellMetadatoVO();
	      agriWellMetadatoVO.setNomeEtichetta("classificazione_regione");
	      agriWellMetadatoVO.setValoreEtichetta("5.80");
	      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	      agriWellMetadatoVO = new AgriWellMetadatoVO();
	      agriWellMetadatoVO.setNomeEtichetta("fascicolazione");
	      agriWellMetadatoVO.setValoreEtichetta("fa");
	      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	      agriWellMetadatoVO = new AgriWellMetadatoVO();
	      agriWellMetadatoVO.setNomeEtichetta("documento_firmato");
	      agriWellMetadatoVO.setValoreEtichetta("false");
	      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	      agriWellMetadatoVO = new AgriWellMetadatoVO();
	      agriWellMetadatoVO.setNomeEtichetta("documento_verificato");
	      agriWellMetadatoVO.setValoreEtichetta("false");
	      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	      agriWellMetadatoVO = new AgriWellMetadatoVO();
	      agriWellMetadatoVO.setNomeEtichetta("data_inizio");
	      agriWellMetadatoVO.setValoreEtichetta(DateUtils
	          .formatDateTimeNotNull(dataAttuale));
	      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	      
	      
	      if("S".equalsIgnoreCase(tipoAllegatoVO.getFlagDaFirmare()))
	      {
	        agriWellMetadatoVO = new AgriWellMetadatoVO();
	        agriWellMetadatoVO.setNomeEtichetta("paginaFirma");
	        agriWellMetadatoVO.setValoreEtichetta(""+numeroPagine);
	        vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	        
	        if(vRichiestaCoordinate != null)
	        {
	          for(int j=0;j<vRichiestaCoordinate.size();j++)
	          {
	            String pedice = "";
	            if(j!=0)
	              pedice = ""+j;
	            
	            
	            String firmatario = "";
	            if("T".equalsIgnoreCase(vRichiestaCoordinate.get(j).getCodiceFirmatario()))
	            {
	              PersonaFisicaVO personaFisicaVO = anagAziendaLocal
	                 .getRappresentanteLegaleFromIdAnagAziendaAndDichCons(anagAziendaVO
	                 .getIdAnagAzienda().longValue(), consistenzaVO.getDataInserimentoDichiarazione());    
	              firmatario = personaFisicaVO.getCognome()+" "+personaFisicaVO.getNome();
	            }
	            else if("O".equalsIgnoreCase(vRichiestaCoordinate.get(j).getCodiceFirmatario()))
	            {
	              AllegatoDichiarazioneVO allegatoDichiarazioneVO = 
	                  documentoGaaLocal.getAllegatoDichiarazioneFromIdDichiarazione(
	                  idDichiarazioneConsistenza, tipoAllegatoVO.getIdTipoAllegato());
	              
	              //RuoloUtenza ruoloUtenzaFirm = smrCommLocal.serviceGetRuoloUtenzaByIdUtente(allegatoDichiarazioneVO.getIdUtenteAggiornamento());
	              UtenteAbilitazioni utenteAbilitazioni =  sianLocal.getUtenteAbilitazioniByIdUtenteLogin(allegatoDichiarazioneVO.getIdUtenteAggiornamento());
	              RuoloUtenza ruoloUtenzaFirm = new RuoloUtenzaPapua(utenteAbilitazioni);
	              
	              firmatario = ruoloUtenzaFirm.getDenominazione();
	            
	            }
	            else if("R".equalsIgnoreCase(vRichiestaCoordinate.get(j).getCodiceFirmatario()))
	            {
	              InfoFascicoloVO infoFascicoloTempVO = stampaGaaLocal
	                  .getInfoFascicolo(anagAziendaVO.getIdAzienda(), consistenzaVO.getDataDichiarazione(), consistenzaVO.getCodiceFotografiaTerreni());
	              
	              firmatario = infoFascicoloTempVO.getResponsabile();        
	            }
	            
	            if(Validator.isEmpty(firmatario))
	              firmatario = "firmatario1";
	            
	            agriWellMetadatoVO = new AgriWellMetadatoVO();
	            agriWellMetadatoVO.setNomeEtichetta("firmatario"+pedice);
	            agriWellMetadatoVO.setValoreEtichetta(firmatario);
	            vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	            
	            
	            
	            agriWellMetadatoVO = new AgriWellMetadatoVO();
	            agriWellMetadatoVO.setNomeEtichetta("coordinataXsinistra"+pedice);
	            BigDecimal coordinata = vRichiestaCoordinate.get(j).getPosFirmaGrafoXSinistra();
	            agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
	            vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	            agriWellMetadatoVO = new AgriWellMetadatoVO();
	            agriWellMetadatoVO.setNomeEtichetta("coordinataXdestra"+pedice);
	            coordinata = vRichiestaCoordinate.get(j).getPosFirmaGrafoXDestra();
	            agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
	            vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	            agriWellMetadatoVO = new AgriWellMetadatoVO();
	            agriWellMetadatoVO.setNomeEtichetta("coordinataYalto"+pedice);
	            coordinata = vRichiestaCoordinate.get(j).getPosFirmaGrafoYAlto();
	            agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
	            vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	            agriWellMetadatoVO = new AgriWellMetadatoVO();
	            agriWellMetadatoVO.setNomeEtichetta("coordinataYbasso"+pedice);
	            coordinata = vRichiestaCoordinate.get(j).getPosFirmaGrafoYBasso();
	            agriWellMetadatoVO.setValoreEtichetta(""+coordinata.intValue());
	            vAgriWellMetadatoVO.add(agriWellMetadatoVO);
	          }
	        }
	      }
	      
	      agriWellDocumentoVO
	          .setAMetadati((AgriWellMetadatoVO[]) vAgriWellMetadatoVO
	              .toArray(new AgriWellMetadatoVO[vAgriWellMetadatoVO.size()]));

	      AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
	          .agriwellServiceScriviDoquiAgri(agriWellDocumentoVO);

	      /*AllegatoDocumentoVO allegatoDocumentoVO = new AllegatoDocumentoVO();
	      allegatoDocumentoVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
	      allegatoDocumentoVO.setDataRagistrazione(dataAttuale);
	      allegatoDocumentoVO.setDataUltimoAggiornamento(dataAttuale);
	      allegatoDocumentoVO.setNomeFisico(nomeFile);
	      allegatoDocumentoVO.setNomeLogico(nomeFile);
	      allegatoDocumentoVO.setIdTipoAllegato(new Long(tipoAllegatoVO.getIdTipoAllegato()));*/
	      if (Validator.isNotEmpty(agriWellEsitoVO)
	          && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito()
	              .intValue())
	      {
	        AllegatoDichiarazioneVO allegatoDichiarazione = documentoGaaLocal
	            .getAllegatoDichiarazioneFromIdDichiarazione(new Long(consistenzaVO.getIdDichiarazioneConsistenza())
	            .longValue(), new Long(tipoAllegatoVO.getIdTipoAllegato()));
	        
	        if(Validator.isNotEmpty(allegatoDichiarazione))
	        {
	          documentoGaaLocal.updateAllegatoForExtIdDocIndex(allegatoDichiarazione.getIdAllegato(), 
	            ruoloUtenza.getIdUtente(), agriWellEsitoVO
	            .getIdDocumentoIndex());
	        }
	        else
	        {
	          String messaggio = "Attenzione si è verificato un problema non esiste un record attivo " +
	              "su db_allegato_dichiarazione per quelli inseribili ";
	          if (Validator.isNotEmpty(agriWellEsitoVO))
	          {
	            messaggio += "-" + agriWellEsitoVO.getMessaggio();
	          }
	          
	          SolmrLogger.error(this, messaggio);
	          
	          throw new SolmrException(messaggio);
	        }
	        
	        /*if("S".equalsIgnoreCase(tipoAllegatoVO.getFlagInseribile()))
	        {
	          if(Validator.isNotEmpty(allegatoDichiarazione))
	          {
	            documentoGaaLocal.updateAllegatoForExtIdDocIndex(allegatoDichiarazione.getIdAllegato(), 
	              ruoloUtenza.getIdUtente(), agriWellEsitoVO
	              .getIdDocumentoIndex());
	          }
	          else
	          {
	            String messaggio = "Attenzione si è verificato un problema non esiste un record attivo " +
	            		"su db_allegato_dichiarazione per quelli inseribili ";
	            if (Validator.isNotEmpty(agriWellEsitoVO))
	            {
	              messaggio += "-" + agriWellEsitoVO.getMessaggio();
	            }
	            
	            SolmrLogger.error(this, messaggio);
	            
	            throw new SolmrException(messaggio);
	          }
	        }
	        else
	        {   
	          allegatoDocumentoVO.setExtIdDocumentoIndex(agriWellEsitoVO
	              .getIdDocumentoIndex());   
	          Long idAllegato = documentoGaaLocal
	              .insertFileAllegatoNoFile(allegatoDocumentoVO);
	          if(allegatoDichiarazione != null)
	          {
	            documentoGaaLocal.storicizzaAllegatoDichiarazione(allegatoDichiarazione.getIdAllegatoDichiarazione());
	          }
	          documentoGaaLocal.insertAllegatoDichiarazione(
	              new Long(consistenzaVO.getIdDichiarazioneConsistenza()).longValue(),
	              idAllegato.longValue());
	        }*/
	      }
	      else
	      {        
	        
	        String messaggio = "Attenzione si è verificato un problema nell'inserimento agriwell ";
	        if (Validator.isNotEmpty(agriWellEsitoVO))
	        {
	          messaggio += "-" + agriWellEsitoVO.getMessaggio();
	        }
	        
	        SolmrLogger.error(this, messaggio);
	        
	        throw new SolmrException(messaggio);
	      }

	      return agriWellEsitoVO.getIdDocumentoIndex();

	    }
	    catch (Throwable t)
	    {
	      sessionContext.setRollbackOnly();
	      throw new SolmrException(t.getMessage());
	    }
	  }
  
  
  
  

  public Long insertFileRichiestaValidazioneAgriWell(
      AziendaNuovaVO aziendaNuovaVO, byte[] fileStampa, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception
  {
    try
    {

      // Creo l'oggetto da passare al servzio!!!
      AgriWellDocumentoVO agriWellDocumentoVO = new AgriWellDocumentoVO();
      agriWellDocumentoVO.setIdProcedimento(new Long(
          SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
      agriWellDocumentoVO.setNomeFile("richiestaValidazione.pdf");
      agriWellDocumentoVO.setIdTipoDocumento(new Long(407));
      agriWellDocumentoVO.setContenutoFile(fileStampa);
      agriWellDocumentoVO.setEstensione("pdf");
      agriWellDocumentoVO.setIdUtenteAggiornamento(aziendaNuovaVO
          .getIdUtenteAggiornamentoRich());
      agriWellDocumentoVO.setDataInserimento(Calendar.getInstance());
      agriWellDocumentoVO.setNoteDocumento(aziendaNuovaVO
          .getNoteRichiestaAzienda());
      agriWellDocumentoVO.setAnno(DateUtils.getCurrentYear());
      agriWellDocumentoVO.setNomeCartella("ANAGRAFE");
      agriWellDocumentoVO.setExtIdAzienda(aziendaNuovaVO.getIdAzienda());

      Vector<AgriWellMetadatoVO> vAgriWellMetadatoVO = new Vector<AgriWellMetadatoVO>();
      AgriWellMetadatoVO agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("id_tipo_visibilita");
      agriWellMetadatoVO.setValoreEtichetta("1");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("codice_ente_utente");
      agriWellMetadatoVO.setValoreEtichetta(ruoloUtenza.getCodiceEnte());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("titolo_documento");
      agriWellMetadatoVO.setValoreEtichetta("richiesta validazione");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("cuaa");
      agriWellMetadatoVO.setValoreEtichetta(aziendaNuovaVO.getCuaa());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("classificazione_regione");
      agriWellMetadatoVO.setValoreEtichetta("5.80");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("fascicolazione");
      agriWellMetadatoVO.setValoreEtichetta("fa");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_firmato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_verificato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);

      agriWellDocumentoVO.setAMetadati((AgriWellMetadatoVO[]) vAgriWellMetadatoVO
              .toArray(new AgriWellMetadatoVO[vAgriWellMetadatoVO.size()]));

      AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
          .agriwellServiceScriviDoquiAgri(agriWellDocumentoVO);

      if (Validator.isNotEmpty(agriWellEsitoVO)
          && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito()
              .intValue())
      {
        nuovaIscrizioneGaaLocal.updateRichiestaAziendaIndex(aziendaNuovaVO
            .getIdRichiestaAzienda().longValue(), agriWellEsitoVO
            .getIdDocumentoIndex().longValue());
      }
      else
      {
        String messaggio = "Attenzione si è verificato un problema nell'inserimento agriwell ";
        if (Validator.isNotEmpty(agriWellEsitoVO))
        {
          messaggio += "-" + agriWellEsitoVO.getMessaggio();
        }
        throw new SolmrException(messaggio);
      }

      return agriWellEsitoVO.getIdDocumentoIndex();

    }
    catch (Throwable t)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(t.getMessage());
    }
  }

  /*public Long insertFileNuovaValidazioneAgriWell(AziendaNuovaVO aziendaNuovaVO,
      byte[] fileStampa, RuoloUtenza ruoloUtenza) throws SolmrException,
      Exception
  {
    try
    {

      // Creo l'oggetto da passare al servzio!!!
      AgriWellDocumentoVO agriWellDocumentoVO = new AgriWellDocumentoVO();
      agriWellDocumentoVO.setIdProcedimento(new Long(
          SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
      agriWellDocumentoVO.setNomeFile("richiestaValidazione.pdf");
      agriWellDocumentoVO.setIdTipoDocumento(new Long(407));
      agriWellDocumentoVO.setContenutoFile(fileStampa);
      agriWellDocumentoVO.setEstensione("pdf");
      agriWellDocumentoVO.setIdUtenteAggiornamento(aziendaNuovaVO
          .getIdUtenteAggiornamentoRich());
      agriWellDocumentoVO.setDataInserimento(new Date());
      agriWellDocumentoVO.setNoteDocumento(aziendaNuovaVO
          .getNoteRichiestaAzienda());
      agriWellDocumentoVO.setAnno(DateUtils.getCurrentYear());
      agriWellDocumentoVO.setNomeCartella("ANAGRAFE");
      agriWellDocumentoVO.setExtIdAzienda(aziendaNuovaVO.getIdAzienda());

      Vector<AgriWellMetadatoVO> vAgriWellMetadatoVO = new Vector<AgriWellMetadatoVO>();
      AgriWellMetadatoVO agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("id_tipo_visibilita");
      agriWellMetadatoVO.setValoreEtichetta("1");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("codice_ente_utente");
      agriWellMetadatoVO.setValoreEtichetta(ruoloUtenza.getCodiceEnte());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("titolo_documento");
      agriWellMetadatoVO.setValoreEtichetta("richiesta validazione");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("cuaa");
      agriWellMetadatoVO.setValoreEtichetta(aziendaNuovaVO.getCuaa());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("classificazione_regione");
      agriWellMetadatoVO.setValoreEtichetta("5.80");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("fascicolazione");
      agriWellMetadatoVO.setValoreEtichetta("fa");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);

      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_firmato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_verificato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);

      agriWellDocumentoVO
          .setaMetadati((AgriWellMetadatoVO[]) vAgriWellMetadatoVO
              .toArray(new AgriWellMetadatoVO[vAgriWellMetadatoVO.size()]));

      AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
          .agriwellServiceScriviDoquiAgri(agriWellDocumentoVO);

      if (Validator.isNotEmpty(agriWellEsitoVO)
          && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito()
              .intValue())
      {
        nuovaIscrizioneGaaLocal.updateRichiestaAziendaIndex(aziendaNuovaVO
            .getIdRichiestaAzienda().longValue(), agriWellEsitoVO
            .getIdDocumentoIndex().longValue());
      }
      else
      {
        String messaggio = "Attenzione si è verificato un problema nell'inserimento agriwell ";
        if (Validator.isNotEmpty(agriWellEsitoVO))
        {
          messaggio += "-" + agriWellEsitoVO.getMessaggio();
        }
        throw new SolmrException(messaggio);
      }

      return agriWellEsitoVO.getIdDocumentoIndex();

    }
    catch (Throwable t)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(t.getMessage());
    }
  }*/
  
  
  public Long insertFileRichiestaVariazioneAgriWell(
      AziendaNuovaVO aziendaNuovaVO, byte[] fileStampa, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception
  {
    try
    {

      // Creo l'oggetto da passare al servzio!!!
      AgriWellDocumentoVO agriWellDocumentoVO = new AgriWellDocumentoVO();
      agriWellDocumentoVO.setIdProcedimento(new Long(
          SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE));
      agriWellDocumentoVO.setNomeFile("richiestaVariazione.pdf");
      agriWellDocumentoVO.setIdTipoDocumento(new Long(409));
      agriWellDocumentoVO.setContenutoFile(fileStampa);
      agriWellDocumentoVO.setEstensione("pdf");
      agriWellDocumentoVO.setIdUtenteAggiornamento(aziendaNuovaVO
          .getIdUtenteAggiornamentoRich());
      agriWellDocumentoVO.setDataInserimento(Calendar.getInstance());
      agriWellDocumentoVO.setNoteDocumento(aziendaNuovaVO
          .getNoteRichiestaAzienda());
      agriWellDocumentoVO.setAnno(DateUtils.getCurrentYear());
      agriWellDocumentoVO.setNomeCartella("ANAGRAFE");
      agriWellDocumentoVO.setExtIdAzienda(aziendaNuovaVO.getIdAzienda());

      Vector<AgriWellMetadatoVO> vAgriWellMetadatoVO = new Vector<AgriWellMetadatoVO>();
      AgriWellMetadatoVO agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("id_tipo_visibilita");
      agriWellMetadatoVO.setValoreEtichetta("1");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("codice_ente_utente");
      agriWellMetadatoVO.setValoreEtichetta(ruoloUtenza.getCodiceEnte());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("titolo_documento");
      agriWellMetadatoVO.setValoreEtichetta("richiesta variazione");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("cuaa");
      agriWellMetadatoVO.setValoreEtichetta(aziendaNuovaVO.getCuaa());
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("classificazione_regione");
      agriWellMetadatoVO.setValoreEtichetta("5.80");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("fascicolazione");
      agriWellMetadatoVO.setValoreEtichetta("fa");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_firmato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);
      agriWellMetadatoVO = new AgriWellMetadatoVO();
      agriWellMetadatoVO.setNomeEtichetta("documento_verificato");
      agriWellMetadatoVO.setValoreEtichetta("false");
      vAgriWellMetadatoVO.add(agriWellMetadatoVO);

      agriWellDocumentoVO.setAMetadati((AgriWellMetadatoVO[]) vAgriWellMetadatoVO
              .toArray(new AgriWellMetadatoVO[vAgriWellMetadatoVO.size()]));

      AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
          .agriwellServiceScriviDoquiAgri(agriWellDocumentoVO);

      if (Validator.isNotEmpty(agriWellEsitoVO)
          && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito()
              .intValue())
      {
        nuovaIscrizioneGaaLocal.updateRichiestaAziendaIndex(aziendaNuovaVO
            .getIdRichiestaAzienda().longValue(), agriWellEsitoVO
            .getIdDocumentoIndex().longValue());
      }
      else
      {
        String messaggio = "Attenzione si è verificato un problema nell'inserimento agriwell ";
        if (Validator.isNotEmpty(agriWellEsitoVO))
        {
          messaggio += "-" + agriWellEsitoVO.getMessaggio();
        }
        throw new SolmrException(messaggio);
      }

      return agriWellEsitoVO.getIdDocumentoIndex();

    }
    catch (Throwable t)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(t.getMessage());
    }
  }

  public AgriWellEsitoVO agriwellServiceLeggiDoquiAgri(long idDocumentoIndex)
      throws SolmrException, Exception
  {
    return agriWellGaaLocal.agriwellServiceLeggiDoquiAgri(idDocumentoIndex);
  }

  public AgriWellEsitoFolderVO agriwellFindFolderByPadreProcedimentoRuolo(
      int idProcedimento, String codRuoloUtente, Long idFolderMadre,
      boolean noEmptyFolder, Long idAzienda) throws SolmrException,
      Exception
  {
    return agriWellGaaLocal
        .agriwellFindFolderByPadreProcedimentoRuolo(idProcedimento,
            codRuoloUtente, idFolderMadre, noEmptyFolder, idAzienda);
  }

  public AgriWellEsitoIdDocVO agriwellFindListaDocumentiByIdFolder(
      long idFolder, int idProcedimento, String codRuoloUtente, Long idAzienda)
      throws SolmrException, Exception
  {
    return agriWellGaaLocal.agriwellFindListaDocumentiByIdFolder(idFolder,
        idProcedimento, codRuoloUtente, idAzienda);
  }

  public AgriWellEsitoDocumentoVO agriwellFindDocumentoByIdRange(long[] idDoc)
      throws SolmrException, Exception
  {
    return agriWellGaaLocal.agriwellFindDocumentoByIdRange(idDoc);
  }
  
  public void aggiornaValidazioneAgriWell(AllegatoDichiarazioneVO allegatoDichiarazioneVO,
      Long idTipoFirma, String note, RuoloUtenza ruoloUtenza) throws SolmrException,
      Exception
  {
    try
    {
      
      if(Validator.isNotEmpty(note))
      {
        consistenzaLocal.updateNoteDichiarazioneConsistenza(note, 
            allegatoDichiarazioneVO.getIdDichiarazioneConsistenza().longValue());
      }
      
      if(Validator.isNotEmpty(idTipoFirma))
      {
        
        documentoGaaLocal.updateAllegatoForFirma(allegatoDichiarazioneVO
            .getIdAllegato().longValue(), ruoloUtenza.getIdUtente().longValue(), idTipoFirma);
        
        // Creo l'oggetto da passare al servzio!!!
        AgriWellDocumentoDaAggiornareVO agriWellDocumentoDaAggiornareVO = new AgriWellDocumentoDaAggiornareVO();
        agriWellDocumentoDaAggiornareVO
          .setIdDocumentoIndex(allegatoDichiarazioneVO.getExtIdDocumentoIndex());
        agriWellDocumentoDaAggiornareVO.setIdUtenteAggiornamento(ruoloUtenza
            .getIdUtente());
        Vector<TipoFirmaVO> tipiFirma = documentoGaaLocal.getElencoTipoFirma();
        for (TipoFirmaVO tipoFirmaVO : tipiFirma)
        {
          if(idTipoFirma.longValue() == tipoFirmaVO.getIdTipoFirma())
          {
            agriWellDocumentoDaAggiornareVO.setDaFirmare(tipoFirmaVO.getFlagFirmaDoquiAgri());
            break;
          }          
        }
//vecchia implementazione non più in uso.
//        agriWellDocumentoDaAggiornareVO.setDaFirmare("C");
        AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
            .agriwellServiceUpdateDoquiAgri(agriWellDocumentoDaAggiornareVO);
        
        if (Validator.isEmpty(agriWellEsitoVO) 
             || (Validator.isNotEmpty(agriWellEsitoVO)
                 && SolmrConstants.ESITO_AGRIWELL_OK != agriWellEsitoVO.getEsito().intValue()))
        {
          String messaggio = "Attenzione si è verificato un problema nell'aggiornamento su agriwell ";
          if (Validator.isNotEmpty(agriWellEsitoVO))
          {
            messaggio += "-" + agriWellEsitoVO.getMessaggio();
          }
          throw new SolmrException(messaggio);          
        }
      }
      //Era precedentemente selezionato ora deselezionaniamo il check
      else
      {
        documentoGaaLocal.updateAllegatoForFirma(allegatoDichiarazioneVO
            .getIdAllegato().longValue(), ruoloUtenza.getIdUtente().longValue(), null);
        
        // Creo l'oggetto da passare al servzio!!!
        AgriWellDocumentoDaAggiornareVO agriWellDocumentoDaAggiornareVO = new AgriWellDocumentoDaAggiornareVO();
        agriWellDocumentoDaAggiornareVO
          .setIdDocumentoIndex(allegatoDichiarazioneVO.getExtIdDocumentoIndex());
        agriWellDocumentoDaAggiornareVO.setIdUtenteAggiornamento(ruoloUtenza
            .getIdUtente());
        agriWellDocumentoDaAggiornareVO.setDaFirmare("S");
        
        AgriWellEsitoVO agriWellEsitoVO = agriWellGaaLocal
            .agriwellServiceUpdateDoquiAgri(agriWellDocumentoDaAggiornareVO);
        
        if (Validator.isEmpty(agriWellEsitoVO) 
             || (Validator.isNotEmpty(agriWellEsitoVO)
                 && SolmrConstants.ESITO_AGRIWELL_OK != agriWellEsitoVO.getEsito().intValue()))
        {
          String messaggio = "Attenzione si è verificato un problema nell'aggiornamento su agriwell ";
          if (Validator.isNotEmpty(agriWellEsitoVO))
          {
            messaggio += "-" + agriWellEsitoVO.getMessaggio();
          }
          throw new SolmrException(messaggio);          
        }
      }       
      

    }
    catch (Throwable t)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(t.getMessage());
    }
  }
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
      long idDichiarazioneConsistenza, long idTipoAllegato) 
    throws SolmrException, Exception
  {
    return documentoGaaLocal.getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
          idDichiarazioneConsistenza, idTipoAllegato);  
  }

  /** ******************************************** */
  /** ***** AgriWell END ****** */
  /** ******************************************** */

  /** ******************************************** */
  /** ***** UMASERVGAA START ****** */
  /** ******************************************** */

  public DittaUmaVO[] umaservGetAssegnazioniByIdAzienda(long idAzienda,
      String[] arrCodiceStatoAnag) throws SolmrException, Exception
  {
    return umaServGaaLocal.umaservGetAssegnazioniByIdAzienda(idAzienda,
        arrCodiceStatoAnag);
  }

  public AssegnazioneVO[] umaservGetDettAssegnazioneByRangeIdDomAss(
      long[] arrIdDomandaAssegnazione) throws SolmrException, Exception
  {
    return umaServGaaLocal
        .umaservGetDettAssegnazioneByRangeIdDomAss(arrIdDomandaAssegnazione);
  }
  
  public Vector<MacchinaVO> serviceGetElencoMacchineByIdAzienda(Long idAzienda,
      Boolean storico, Long idGenereMacchina)  throws SolmrException, Exception
  {
    return umaServGaaLocal.serviceGetElencoMacchineByIdAzienda(
        idAzienda, storico, idGenereMacchina);
  }
  
  public Vector<Long> serviceGetElencoAziendeUtilizzatrici(Long idMacchina)
      throws SolmrException, Exception
  {
    return umaServGaaLocal.serviceGetElencoAziendeUtilizzatrici(idMacchina);
  }
  
  public UtilizzoVO serviceGetUtilizzoByIdMacchinaAndIdAzienda(
      Long idMacchina, Long idAzienda) throws SolmrException, Exception
  {
    return umaServGaaLocal.serviceGetUtilizzoByIdMacchinaAndIdAzienda(idMacchina, idAzienda);
  }

  /** ******************************************** */
  /** ***** UMASERVGAA END ****** */
  /** ******************************************** */

  /** ******************************************** */
  /** ***** MODOLSERVGAA START ****** */
  /** ******************************************** */

  public byte[] callModol(byte[] xmlInput, AttributiModuloVO attributiModuloVO)
      throws SolmrException, Exception
  {
    return modolServGaaLocal.callModol(xmlInput, attributiModuloVO);
  }

  public byte[] trasformStaticPDF(byte[] xmlInput, AttributiModuloVO attributiModuloVO)
      throws SolmrException, Exception
  {
    return modolServGaaLocal.trasformStaticPDF(xmlInput, attributiModuloVO);
  }

  /** ******************************************** */
  /** ***** MODOLSERVGAA END ****** */
  /** ******************************************** */
  
  
  /** ******************************************** */
  /** ***** MACCHINEAGRICOLEGAA START ****** */
  /** ******************************************** */

  public long[] ricercaIdPossessoMacchina(FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO)
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.ricercaIdPossessoMacchina(filtriRicercaMacchineAgricoleVO);
  }
  
  public long[] ricercaIdPossessoMacchinaImport(
    FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO) throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.ricercaIdPossessoMacchinaImport(filtriRicercaMacchineAgricoleVO);
  }
  
  public RigaRicercaMacchineAgricoleVO[] getRigheRicercaMacchineAgricoleById(
      long ids[]) throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getRigheRicercaMacchineAgricoleById(ids);
  }
  
  public Vector<PossessoMacchinaVO> getElencoMacchineAgricoleForStampa(
      long idAzienda, Date dataInserimentoValidazione) throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getElencoMacchineAgricoleForStampa(idAzienda, dataInserimentoValidazione);
  }
  
  public void popolaTabelleMacchineAgricoleConServizio(
      long idAzienda) throws SolmrException, Exception
  {
    macchineAgricoleGaaLocal.popolaTabelleMacchineAgricoleConServizio(idAzienda);
  }
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchina() 
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getElencoTipoGenereMacchina();
  }
  
  public TipoGenereMacchinaGaaVO getTipoGenereMacchinaByPrimaryKey(long idGenereMacchina) 
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getTipoGenereMacchinaByPrimaryKey(idGenereMacchina);
  }
  
  public PossessoMacchinaVO getPosessoMacchinaFromId(long idPossessoMacchina)
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getPosessoMacchinaFromId(idPossessoMacchina);
  }
  
  public Vector<PossessoMacchinaVO> getElencoDitteUtilizzatrici(
      long idMacchina, Date dataScarico) throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getElencoDitteUtilizzatrici(idMacchina, dataScarico);
  }
  
  public Vector<PossessoMacchinaVO> getElencoPossessoDitteUtilizzatrici(
      long idMacchina, long idAzienda) throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getElencoPossessoDitteUtilizzatrici(idMacchina, idAzienda);
  }
  
  public Vector<TipoMacchinaGaaVO> getElencoTipoMacchina(String flaIrroratrice) 
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getElencoTipoMacchina(flaIrroratrice);
  }
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchinaFromRuolo(
      long idTipoMacchina, String codiceRuolo) throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getElencoTipoGenereMacchinaFromRuolo(idTipoMacchina, codiceRuolo);
  }
  
  public Vector<TipoCategoriaGaaVO> getElencoTipoCategoria(long idGenereMacchina) 
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getElencoTipoCategoria(idGenereMacchina);
  }
  
  public Vector<CodeDescription> getElencoTipoMarca()
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getElencoTipoMarca();
  }
  
  public Vector<CodeDescription> getElencoTipoMarcaByIdGenere(long idGenereMacchina) 
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getElencoTipoMarcaByIdGenere(idGenereMacchina);
  }
    
  public Vector<TipoFormaPossessoGaaVO> getElencoTipoFormaPossesso()
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getElencoTipoFormaPossesso();
  }
  
  public boolean existsMotoreAgricolo(Long idMarca, String modello,
      Integer annoCostruzione, String matricolaTelaio) throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.existsMotoreAgricolo(idMarca, modello, 
        annoCostruzione, matricolaTelaio);
  }
  
  public void inserisciMacchinaAgricola(PossessoMacchinaVO possessoMacchinaVO) 
      throws SolmrException, Exception
  {
    macchineAgricoleGaaLocal.inserisciMacchinaAgricola(possessoMacchinaVO);
  }
  
  public void modificaMacchinaAgricola(long idPossessoMacchina, PossessoMacchinaVO possessoMacchinaVO, boolean flagModUte) 
      throws SolmrException, Exception
  {
    macchineAgricoleGaaLocal.modificaMacchinaAgricola(idPossessoMacchina, possessoMacchinaVO, flagModUte);
  }
  
  public void importaMacchinaAgricola(long idPossessoMacchina, PossessoMacchinaVO possessoMacchinaVO) 
      throws SolmrException, Exception
  {
    macchineAgricoleGaaLocal.importaMacchinaAgricola(idPossessoMacchina, possessoMacchinaVO);
  }
  
  public boolean isMacchinaModificabile(long idPossessoMacchina, String codiceRuolo)
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.isMacchinaModificabile(idPossessoMacchina, codiceRuolo);
  }
  
  public boolean isMacchinaPossMultiplo(long idMacchina)
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.isMacchinaPossMultiplo(idMacchina);
  }
  
  public Vector<CodeDescription> getElencoTipoScarico()
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getElencoTipoScarico();
  }
  
  public boolean isMacchinaGiaPossesso(long idMacchina, long idAzienda)
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.isMacchinaGiaPossesso(idMacchina, idAzienda);
  }
  
  public BigDecimal percMacchinaGiaInPossesso(long idMacchina)
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.percMacchinaGiaInPossesso(idMacchina);
  }
  
  public TipoCategoriaGaaVO getTipoCategoriaFromPK(long idCategoria) 
      throws SolmrException, Exception
  {
    return macchineAgricoleGaaLocal.getTipoCategoriaFromPK(idCategoria);
  }

  /** ******************************************** */
  /** ***** MACCHINEAGRICOLEGAA END ****** */
  /** ******************************************** */

  
  
  
  /** ******************************************** */
  /** ***** MARCATEMPORALEGAA START ****** */
  /** ******************************************** */
  
  public byte[] getMarcaTemporale(byte[] fileToMark) 
	      throws SolmrException, Exception
	  {
	    return marcaTemporaleGaaLocal.getMarcaTemporale(fileToMark);
	  }
  
  /** ******************************************** */
  /** ***** MARCATEMPORALEGAA END ****** */
  /** ******************************************** */
  
  
  /*
   * 
   * INIZIO SIANFA
   */
  public SianEsito getAggiornamentiFascicolo(String cuaa) throws SolmrException, Exception{
	 SolmrLogger.info(this, "BEGIN getAggiornamentiFascicolo");
	 
	 SianEsito esitoAggFascicolo = sianFascicoloAllLocal.getAggiornamentiFascicolo(cuaa);
	 
     SolmrLogger.info(this, "END getAggiornamentiFascicolo");
     return esitoAggFascicolo;
  }
  
  
  public Boolean aggiornaFascicoloAziendale(String cuaa, Long IdUtente) throws SolmrException, Exception{
    SolmrLogger.info(this, "BEGIN aggiornaFascicoloAziendale");		 
    Boolean aggiornaFasc = sianFascicoloAllLocal.aggiornaFascicoloAziendale(cuaa, IdUtente);		 
	SolmrLogger.info(this, "END getAggiornamentiFascicolo");	     
	return aggiornaFasc;
  }
  
  /*
   * 
   * FINE SIANFA
   */
  
  
  

    private String getExcMax500Char(String str)
  {
    if (str.length() > 500)
    {
      str = str.substring(0, 500);
    }
    return str;
  }

  private String underscoreToNull(String str)
  {
    if (Validator.isNotEmpty(str))
    {
      str = str.trim();
      if (str.equalsIgnoreCase("_"))
        str = null;
    }
    return str;
  }

}
