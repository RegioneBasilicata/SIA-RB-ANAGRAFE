package it.csi.smranag.smrgaa.presentation.client;

import it.csi.papua.papuaserv.dto.messaggistica.DettagliMessaggio;
import it.csi.papua.papuaserv.dto.messaggistica.ListaMessaggi;
import it.csi.papua.papuaserv.exception.messaggistica.LogoutException;
import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioFabbricato;
import it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioTerreno;
import it.csi.sigmater.sigtersrv.dto.daticatastali.Titolarita;
import it.csi.smranag.smrgaa.business.GaaFacadeBean;
import it.csi.smranag.smrgaa.business.GaaFacadeLocal;
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
import it.csi.smranag.smrgaa.ws.sianfa.SianEsito;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoFolderVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoIdDocVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoVO;
import it.csi.smruma.umaserv.dto.AssegnazioneVO;
import it.csi.smruma.umaserv.dto.DittaUmaVO;
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
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;

import javax.naming.InitialContext;

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
public class GaaFacadeClient
{
	 private transient GaaFacadeLocal gaaFacade;
	 
	 private static GaaFacadeClient instance;

	 private static boolean initialized = false;
	 public static GaaFacadeClient getInstance() {
	      if (!initialized) {
	          synchronized (GaaFacadeClient.class) {
	              if (!initialized) {
	            	  instance = new GaaFacadeClient();
	                  initialized = true;
	              }
	          }
	     }
	     return instance;
	  }
	  
	  private GaaFacadeClient()
	  {
	    try
	    {
	      InitialContext ctx = new InitialContext();
	      //ctx.lookup("java:app/smrgaa/comp/env/solmr/anag/AnagFacade");
	      Object lookup = ctx.lookup("java:app/smrgaa/"+GaaFacadeBean.jndiName);
	      gaaFacade = GaaFacadeWrapper.wrap(lookup);
	   
	    }
	    catch (Exception ex)
	    {
	    	ex.printStackTrace();
	    }   
	  }

  /** ******************************************** */
  /** ***** ALLEVAMENTI START ****** */
  /** ******************************************** */
  public TipoSottoCategoriaAnimale[] getTipiSottoCategoriaAnimale(long idCategoriaAnimale) throws Exception, SolmrException
  {
    return gaaFacade.getTipiSottoCategoriaAnimale(idCategoriaAnimale);
  }

  public TipoSottoCategoriaAnimale getTipoSottoCategoriaAnimale(long idSottoCategoriaAnimale) throws Exception, SolmrException
  {
    return gaaFacade.getTipoSottoCategoriaAnimale(idSottoCategoriaAnimale);
  }
  
  public TipoMungitura[] getTipiMungitura() throws Exception, SolmrException
  {
    return gaaFacade.getTipiMungitura();
  }
  
  public Vector<SottoCategoriaAllevamento> getTipiSottoCategoriaAllevamento(long idAllevamento) throws SolmrException, Exception
  {
    return gaaFacade.getTipiSottoCategoriaAllevamento(idAllevamento);
  }
  
  public Vector<StabulazioneTrattamento> getStabulazioni(long idAllevamento,boolean modifica) throws SolmrException, Exception
  {
    return gaaFacade.getStabulazioni(idAllevamento,modifica);
  }
  
  public BaseCodeDescription[] getTipiStabulazione(long idSottoCategoriaAnimale) throws SolmrException, Exception
  {
    return gaaFacade.getTipiStabulazione(idSottoCategoriaAnimale);
  }
  
  public Vector<TipoTrattamento> getTipiTrattamento() throws SolmrException, Exception
  {
    return gaaFacade.getTipiTrattamento();
  }

  public TipoTrattamento getTipoTrattamento(long idTrattamento) throws SolmrException, Exception
  {
    return gaaFacade.getTipoTrattamento(idTrattamento);
  }
  
  public SottoCategoriaAnimStab getSottoCategoriaAnimStab(long idSottoCategoriaAnimale,boolean palabile, long idStabulazione) throws SolmrException, Exception
  {
    return gaaFacade.getSottoCategoriaAnimStab(idSottoCategoriaAnimale,palabile, idStabulazione);
  }
  
  public Vector<AllevamentoVO> getElencoAllevamentiExcel(long idAzienda, Date dataInserimentiDich, Long idUte) throws SolmrException, Exception
  {
    return gaaFacade.getElencoAllevamentiExcel(idAzienda, dataInserimentiDich, idUte);
  }
  
  public Vector<AllevamentoBioVO> getAllevamentiBio(Date dataInserimentoDichiarazione, long idAllevamento) 
    throws SolmrException, Exception
  {
    return gaaFacade.getAllevamentiBio(dataInserimentoDichiarazione, idAllevamento);
  }
  
  public Vector<TipoDestinoAcquaLavaggio> getElencoDestAcquaLavaggio()  
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoDestAcquaLavaggio();
  }
  
  public Vector<AllevamentoAcquaLavaggio> getElencoAllevamentoAcquaLavaggio(long idAllevamento)
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoAllevamentoAcquaLavaggio(idAllevamento);
  }
  
  public Vector<Long> getElencoSpecieAzienda(long idAzienda) 
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoSpecieAzienda(idAzienda);
  }
  
  /*public HashMap<Long,BigDecimal> getAllevamentoTotAcquaLavaggio(long idAzienda)  
      throws SolmrException, Exception
  {
    return gaaFacade.getAllevamentoTotAcquaLavaggio(idAzienda);
  }*/
  
  public Vector<AllevamentoVO> getElencoAllevamentiSian(String cuaa, 
      long idTipoSpecie, String codAziendaZoo) 
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoAllevamentiSian(cuaa, idTipoSpecie, codAziendaZoo);
  }
  
  public Vector<EsitoControlloAllevamento> getElencoEsitoControlloAllevamento(long idAllevamento)
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoEsitoControlloAllevamento(idAllevamento);
  }
  
  public HashMap<Long, ControlloAllevamenti> getEsitoControlliAllevamentiAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getEsitoControlliAllevamentiAzienda(idAzienda);
  }   
  
  public HashMap<Long, ControlloAllevamenti> getSegnalazioniControlliAllevamentiAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getSegnalazioniControlliAllevamentiAzienda(idAzienda);
  }      

  /** ******************************************** */
  /** ***** ALLEVAMENTI END ****** */
  /** ******************************************** */
  
  
  /*************************************************************************/
  /********************** Fabbricati START ***********************************/
  /*************************************************************************/
  
  public TipoTipologiaFabbricatoVO getInfoTipologiaFabbricato(
      Long idTipologiaFabbricato) throws Exception, SolmrException
  {
    return gaaFacade.getInfoTipologiaFabbricato(idTipologiaFabbricato);
  }
  
  public TipoFormaFabbricatoVO getTipoFormaFabbricato(Long idFormaFabbricato)
    throws Exception, SolmrException
  {
    return gaaFacade.getTipoFormaFabbricato(idFormaFabbricato);
  }
  
  public Vector<TipoTipologiaFabbricatoVO> getListTipoFabbricatoStoccaggio() throws SolmrException, Exception
  {
    return gaaFacade.getListTipoFabbricatoStoccaggio();
  }
  
  public FabbricatoBioVO getFabbricatoBio(long idFabbricato, long idAzienda, 
      Date dataInserimentoDichiarazione) throws SolmrException, Exception
  {
    return gaaFacade.getFabbricatoBio(idFabbricato, idAzienda, dataInserimentoDichiarazione);
  }
  
  public HashMap<Long,String> esisteFabbricatoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
  throws SolmrException, Exception
  {
    return gaaFacade.esisteFabbricatoAttivoByConduzioneAndAzienda(elencoConduzioni, idAzienda);
  }
  
  /*************************************************************************/
  /********************** Fabbricati END ***********************************/
  /*************************************************************************/

  /** ******************************************** */
  /** ***** AGRISERV BEGIN ****** */
  /** ******************************************** */
  
  public RitornoAgriservVO searchPraticheProcedimento(long idParticella,
      Long idAzienda, int tipologiaStati, Long idDichiarazioneConsistenza, 
      Long idProcedimento, Long annoCampagna, int tipoOrdinamento) 
      throws SolmrException, Exception
  {
    return gaaFacade.searchPraticheProcedimento(idParticella,
        idAzienda, tipologiaStati, idDichiarazioneConsistenza, idProcedimento,
        annoCampagna, tipoOrdinamento);
  }

  public BaseCodeDescription[] baseDecodeUtilizzoByIdRange(long ids[])
      throws SolmrException, Exception
  {
    return gaaFacade.baseDecodeUtilizzoByIdRange(ids);
  }
  
  public RitornoPraticheCCAgriservVO searchPraticheContoCorrente(long[] idContoCorrente, 
      int tipologiaStati, Long idDichiarazioneConsistenza, Long idProcedimento, 
      Long annoCampagna, int tipoOrdinamento) throws SolmrException, Exception
  {
    return gaaFacade.searchPraticheContoCorrente(idContoCorrente, 
        tipologiaStati, idDichiarazioneConsistenza, idProcedimento, annoCampagna, 
        tipoOrdinamento);
    
  }
  
  public RitornoAgriservUvVO existPraticheEstirpoUV(long[] idParticella, Long idAzienda, 
      Long idDichiarazioneConsistenza, Long idProcedimento, 
      Long annoCampagna, int tipoOrdinamento) throws SolmrException, Exception
  {
    return gaaFacade.existPraticheEstirpoUV(idParticella, idAzienda, 
        idDichiarazioneConsistenza, idProcedimento, annoCampagna, 
        tipoOrdinamento);
  }
  
  /** ******************************************** */
  /** ***** AGRISERV END ****** */
  /** ******************************************** */
  
  
  
  /*************************************************************************/
  /********************** Comunicazione 10R START ***********************************/
  /*************************************************************************/
  
  public Comunicazione10RVO getComunicazione10RByIdUteAndPianoRifererimento(long idUte, long idPianoRiferimento) 
    throws SolmrException, Exception
  {
    
    return gaaFacade.getComunicazione10RByIdUteAndPianoRifererimento(idUte, idPianoRiferimento);
  }
  
  public Vector<EffluenteVO> getListEffluenti(long idComunicazione10R) throws SolmrException, Exception
  {
    return gaaFacade.getListEffluenti(idComunicazione10R);
  }
  
  public Vector<EffluenteVO> getListEffluenti(long idComunicazione10R[]) throws SolmrException, Exception
  {
    return gaaFacade.getListEffluenti(idComunicazione10R);
  }
  
  public Vector<EffluenteVO> getListEffluentiStampa(long idComunicazione10R[]) throws SolmrException, Exception
  {
    return gaaFacade.getListEffluentiStampa(idComunicazione10R);
  }
  
  public Vector<TipoCausaleEffluenteVO> getListTipoCausaleEffluente() throws SolmrException, Exception
  {
    return gaaFacade.getListTipoCausaleEffluente();
  }
  
  public Vector<TipoEffluenteVO> getListTipoEffluente() throws SolmrException, Exception
  {
    return gaaFacade.getListTipoEffluente();
  }
  
  public TipoEffluenteVO getTipoEffluenteById(long idEffluente) 
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoEffluenteById(idEffluente);
  }
  
  public Vector<TipoAcquaAgronomicaVO> getListTipoAcquaAgronomica() throws SolmrException, Exception
  {
    return gaaFacade.getListTipoAcquaAgronomica();
  }
  
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquByidComunicazione(long idComunicazione10R, Long idTipoCausale) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListEffluentiCessAcquByidComunicazione(idComunicazione10R, idTipoCausale);
  }
  
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquPerStampa(long idComunicazione10R[]) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListEffluentiCessAcquPerStampa(idComunicazione10R);
  }
  
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquByidComunicazione(long idComunicazione10R[], Long idTipoCausale) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListEffluentiCessAcquByidComunicazione(idComunicazione10R, idTipoCausale);
  }

  public Vector<EffluenteStocExtVO> getListStoccaggiExtrAziendali(long idComunicazione10R) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListStoccaggiExtrAziendali(idComunicazione10R);
  }
  
  public Vector<EffluenteStocExtVO> getListStoccaggiExtrAziendali(long idComunicazione10R[]) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListStoccaggiExtrAziendali(idComunicazione10R);
  }
  
  public Vector<AcquaExtraVO> getListAcquaExtra(long idComunicazione10R) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListAcquaExtra(idComunicazione10R);
  }
  
  public Vector<AcquaExtraVO> getListAcquaExtra(long idComunicazione10R[]) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListAcquaExtra(idComunicazione10R);
  }
  
  public PlSqlCodeDescription storicizzaComunicazione10R(long idUtenteAggiornamento, long idAzienda,
	      Vector<Comunicazione10RVO> vCom10r, Vector<EffluenteVO> vEffluentiTratt, 
	      Vector<EffluenteCesAcqVO> vCessioniAcquisizioni, Vector<EffluenteCesAcqVO> vCessioni,
	      Vector<EffluenteStocExtVO> vStoccaggi, Vector<AcquaExtraVO> vAcqueExtra) throws SolmrException, Exception
  {
    return gaaFacade.storicizzaComunicazione10R(idUtenteAggiornamento, idAzienda,
        vCom10r, vEffluentiTratt, vCessioniAcquisizioni, vCessioni, vStoccaggi, vAcqueExtra);
  }
  
  public boolean hasEffluenteProdotto(long idEffluente, long idUte) 
    throws SolmrException, Exception
  {
    return gaaFacade.hasEffluenteProdotto(idEffluente, idUte);
  }
  
  public Comunicazione10RVO[] getComunicazione10RByPianoRifererimento(long idAzienda, long idPianoRiferimento) 
    throws SolmrException, Exception
  {
    return gaaFacade.getComunicazione10RByPianoRifererimento(idAzienda, idPianoRiferimento);
  }
  
  public PlSqlCodeDescription controlloQuantitaEffluentePlSql(long idUte, long idEffluente) 
    throws SolmrException, Exception
  {
    return gaaFacade.controlloQuantitaEffluentePlSql(idUte, idEffluente);
  }

  public PlSqlCodeDescription calcolaQuantitaAzotoPlSql(long idUte, long idComunicazione10r, 
      long idCausaleEffluente, long idEffluente, BigDecimal quantita) 
    throws SolmrException, Exception
  {
    return gaaFacade.calcolaQuantitaAzotoPlSql(idUte, idComunicazione10r, 
        idCausaleEffluente, idEffluente, quantita);
  }
  
  public PlSqlCodeDescription ricalcolaPlSql(long idAzienda, 
      long idUtente) 
    throws SolmrException, Exception
  {
    return gaaFacade.ricalcolaPlSql(idAzienda, idUtente);
  }
  
  public Vector<BigDecimal> getSommaEffluentiCessAcquPerStampa(long idComunicazione10R) 
    throws SolmrException, Exception
  {
    return gaaFacade.getSommaEffluentiCessAcquPerStampa(idComunicazione10R);
  }
  
  public BigDecimal getSommaEffluenti10RPerStampa(long idComunicazione10R, boolean palabile) 
    throws SolmrException, Exception
  {
    return gaaFacade.getSommaEffluenti10RPerStampa(idComunicazione10R, palabile);
  }
  
  public PlSqlCodeDescription calcolaM3EffluenteAcquisitoPlSql(long idAzienda, 
      long idAziendaCess, long idCausaleEffluente, long idEffluente) 
    throws SolmrException, Exception
  {
    return gaaFacade.calcolaM3EffluenteAcquisitoPlSql(
        idAzienda, idAziendaCess, idCausaleEffluente, idEffluente);
  }
  
  public boolean controlloRefluoPascolo(long idUte) 
      throws SolmrException, Exception
  {
    return gaaFacade.controlloRefluoPascolo(idUte);
  }
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteTrattamenti(long idUte)
      throws SolmrException, Exception
  {
    return gaaFacade.getListTipoEffluenteTrattamenti(idUte);
  }  
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteByLegameId(long idEffluente) 
      throws SolmrException, Exception
  {
    return gaaFacade.getListTipoEffluenteByLegameId(idEffluente);
  }
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteAndValueByLegameId(
      long idComunicazione10R, long idEffluente) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListTipoEffluenteAndValueByLegameId(idComunicazione10R, idEffluente);
  }
  
  public Vector<EffluenteVO> getListTrattamenti(long idComunicazione10R[]) 
      throws SolmrException, Exception
  {
    return gaaFacade.getListTrattamenti(idComunicazione10R);
  }
  
  public Vector<EffluenteVO> getListTrattamenti(long idComunicazione10R) 
      throws SolmrException, Exception
  {
    return gaaFacade.getListTrattamenti(idComunicazione10R);
  }
  
  public Vector<EffluenteVO> getListEffluentiNoTratt(long idComunicazione10R[]) 
      throws SolmrException, Exception
  {
    return gaaFacade.getListEffluentiNoTratt(idComunicazione10R);
  }
  
  public Vector<EffluenteVO> getListEffluentiNoTratt(long idComunicazione10R) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListEffluentiNoTratt(idComunicazione10R);
  }
  
  public Vector<RefluoEffluenteVO> getRefluiComunocazione10r(long idUte, long idComunicazione10r, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException, Exception
  {
    return gaaFacade.getRefluiComunocazione10r(idUte, idComunicazione10r, 
        dataInserimentoDichiarazione);
  }
  
  public Vector<RefluoEffluenteVO> getListRefluiStampa(long idComunicazione10R[]) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListRefluiStampa(idComunicazione10R);
  }
  
  public PlSqlCodeDescription calcolaVolumePioggeM3PlSql(long idUte)
      throws SolmrException, Exception
  {
    return gaaFacade.calcolaVolumePioggeM3PlSql(idUte);
  }
  
  public PlSqlCodeDescription calcolaAcqueMungituraPlSql(long idUte)
      throws SolmrException, Exception
  {
    return gaaFacade.calcolaAcqueMungituraPlSql(idUte);
  }
  
  public PlSqlCodeDescription calcolaCesAcquisizionePlSql(long idComunicazione10r) 
    throws SolmrException, Exception
  {
    return gaaFacade.calcolaCesAcquisizionePlSql(idComunicazione10r);
  }    
  
  /*************************************************************************/
  /********************** Comunicazione 10R END ***********************************/
  /*************************************************************************/
  
  
  /*************************************************************************/
  /********************** ANAGRAFE START ***********************************/
  /*************************************************************************/
  
  public PlSqlCalcoloOteVO calcolaIneaPlSql(long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.calcolaIneaPlSql(idAzienda);
  }
  
  public PlSqlCalcoloOteVO calcolaUluPlSql(long idAzienda, Long idUde)
    throws SolmrException, Exception
  {
    return gaaFacade.calcolaUluPlSql(idAzienda, idUde);
  }
  
  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAzienda(long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.getListActiveAziendaAtecoSecByIdAzienda(idAzienda);
  }
  
  public Vector<AziendaAtecoSecVO> getListActiveAziendaAtecoSecByIdAziendaAndValid(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return gaaFacade.getListActiveAziendaAtecoSecByIdAziendaAndValid(idAzienda, idDichiarazioneConsistenza);
  }
  
  public CodeDescription getAttivitaAtecoAllaValid(long idAzienda,
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return gaaFacade.getAttivitaAtecoAllaValid(idAzienda, idDichiarazioneConsistenza);
  }
  
  public Vector<TipoDimensioneAziendaVO> getListActiveTipoDimensioneAzienda() 
    throws SolmrException, Exception
  {
    return gaaFacade.getListActiveTipoDimensioneAzienda();
  }
  
  public TipoAttivitaOteVO getTipoAttivitaOteByPrimaryKey(long idAttivitaOte)
    throws SolmrException, Exception
  {
    return gaaFacade.getTipoAttivitaOteByPrimaryKey(idAttivitaOte);
  }
  
  
  public long[] ricercaIdAziendeCollegate(
      FiltriRicercaAziendeCollegateVO filtriRicercaAziendeCollegateVO) 
    throws SolmrException, Exception
  {
    return gaaFacade.ricercaIdAziendeCollegate(filtriRicercaAziendeCollegateVO);
  }
  
  public RigaRicercaAziendeCollegateVO[] getRigheRicercaAziendeCollegateByIdAziendaCollegata(
      long ids[]) throws SolmrException, Exception
  {
    return gaaFacade.getRigheRicercaAziendeCollegateByIdAziendaCollegata(ids);
  }
  
  public int ricercaNumVariazioni(FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO, 
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza) 
    throws SolmrException, Exception
  {
    return gaaFacade.ricercaNumVariazioni(filtriRicercaVariazioniAziendaliVO, utenteAbilitazioni, ruoloUtenza);
  }
  
  public void insertVisioneVariazione(Vector<Long> elencoIdPresaVisione, RuoloUtenza ruoloUtenza) 
    throws SolmrException, Exception
  {
    gaaFacade.insertVisioneVariazione(elencoIdPresaVisione, ruoloUtenza);
  } 
  
  public RigaRicercaVariazioniAziendaliVO[] getRigheRicercaVariazioni(FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza, boolean excel) 
    throws SolmrException, Exception
  {
    return gaaFacade.getRigheRicercaVariazioni(filtriRicercaVariazioniAziendaliVO, utenteAbilitazioni, ruoloUtenza, excel);
  }
  
  public RigaRicercaVariazioniAziendaliVO[] getRigheVariazioniVisione(Vector<Long> elencoIdPresaVisione,FiltriRicercaVariazioniAziendaliVO filtriRicercaVariazioniAziendaliVO) 
    throws SolmrException, Exception
  {
    return gaaFacade.getRigheVariazioniVisione(elencoIdPresaVisione,filtriRicercaVariazioniAziendaliVO) ;
  }

  public boolean isPresaVisione(Vector<Long> elencoIdPresaVisione, RuoloUtenza ruoloUtenza)
    throws SolmrException, Exception
  {
    return gaaFacade.isPresaVisione(elencoIdPresaVisione, ruoloUtenza);
  }
  
  public BigDecimal[] getTOTSupCondottaAndSAU(long idAzienda) throws SolmrException, Exception
  {
    return gaaFacade.getTOTSupCondottaAndSAU(idAzienda);
  }
  
  public HashMap<Long,DelegaVO> getDelegaAndIntermediario(long ids[]) throws SolmrException, Exception
  {
    return gaaFacade.getDelegaAndIntermediario(ids);
  }
  
  public boolean isAziendeCollegataFiglia(long idAzienda, long idAziendaSearch) 
    throws SolmrException, Exception
  {
    return gaaFacade.isAziendeCollegataFiglia(
        idAzienda, idAziendaSearch);
  }
  
  public boolean isAziendeCollegataMenu(
      long idAzienda) throws SolmrException, Exception
  {
    return gaaFacade.isAziendeCollegataMenu(
        idAzienda);
  }
  
  public Vector<AziendaUmaExcelVO> getScaricoExcelElencoSociUma(
      long idAzienda) throws SolmrException, Exception
  {
    return gaaFacade.getScaricoExcelElencoSociUma(idAzienda);
  }
  
  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociColturaVarieta(
      long idAzienda) throws SolmrException, Exception
  {
    return gaaFacade.getScaricoExcelElencoSociColturaVarieta(idAzienda);
  }
  
  public Vector<AziendaColVarExcelVO> getScaricoExcelElencoSociFruttaGuscio(
      long idAzienda) throws SolmrException, Exception
  {
    return gaaFacade.getScaricoExcelElencoSociFruttaGuscio(idAzienda);
  }
  
  public boolean isInAziendaProvenienza(long idAzienda) 
      throws SolmrException,  Exception
  {
    return gaaFacade.isInAziendaProvenienza(idAzienda);
  }
  
  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntive(long idAzienda)
      throws SolmrException,  Exception
  {
    return gaaFacade.getTipoInfoAggiuntive(idAzienda);
  }
  
  public Vector<GruppoGreeningVO> getListGruppiGreening(Long idDichConsistenza, Long idAzienda) 
  		throws SolmrException, Exception {
  	return gaaFacade.getListGruppiGreening(idDichConsistenza, idAzienda);
  }
  
  public PlSqlCodeDescription calcolaGreeningPlSql(long idAzienda, long IdUtente, 
  		Long idDichiarazioneConsistenza) throws SolmrException, Exception {
  	return gaaFacade.calcolaGreeningPlSql(idAzienda, IdUtente, idDichiarazioneConsistenza);
  }
  
  public PlSqlCodeDescription calcolaEfaPlSql(long idAzienda, Long idDichiarazioneConsistenza,
      long IdUtente) throws SolmrException, Exception 
  {
    return gaaFacade.calcolaEfaPlSql(idAzienda, idDichiarazioneConsistenza, IdUtente);
  }
  
  public Vector<AziendaSezioniVO> getListActiveAziendaSezioniByIdAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getListActiveAziendaSezioniByIdAzienda(idAzienda);
  }
  /*************************************************************************/
  /********************** ANAGRAFE END ***********************************/
  /*************************************************************************/
  
  /** ******************************************** */
  /** ***** SIGMATER BEGIN ****** */
  /** ******************************************** */
  public DettaglioTerreno cercaTerreno(String codIstatComune,
			 String codBelfioreComune,
			 String sezione,
			 String foglio,
			 String numero,
			 String subalterno,
			 String progressivo)
	throws SolmrException, Exception
	{
	  return gaaFacade.cercaTerreno(codIstatComune,codBelfioreComune,sezione,
			  							  foglio,numero,subalterno,progressivo);
	}
  
  	public DettaglioFabbricato cercaFabbricato(String codIstatComune,
											   String codBelfioreComune,
											   String sezione,
											   String foglio,
											   String numero,
											   String subalterno,
											   String progressivo)
  		throws SolmrException, Exception 
	{
		return gaaFacade.cercaFabbricato(codIstatComune,codBelfioreComune,sezione,
				  foglio,numero,subalterno,progressivo);
	}	

	public Titolarita[] cercaTitolaritaOggettoCatastale(String codIstatComune,
														 String codBelfioreComune,
														 String sezione,
														 String idImmobile,
														 String tipoImmobile,
														 String dataDa,
														 String dataA)
		throws SolmrException, Exception
	{
		return gaaFacade.cercaTitolaritaOggettoCatastale(codIstatComune,codBelfioreComune,sezione,
		idImmobile, tipoImmobile, dataDa, dataA);
	}
	
	
	public void scaricoTitolarita(String codIstat, String cuaa, long idAzienda,
      Date dataDa, Date dataA)
  throws SolmrException, Exception
  {
	  gaaFacade.scaricoTitolarita(codIstat, cuaa, idAzienda,
	      dataDa, dataA);
  }
	
	public Long getIdTipoDiritto(String codice)
      throws SolmrException, Exception
  {
    return gaaFacade.getIdTipoDiritto(codice);
  }
	
	public void importaTitolaritaSigmater(long idParticella, 
      Titolarita[] titolarita, long idUtente) throws SolmrException, Exception
  {    
	  gaaFacade.importaTitolaritaSigmater(idParticella, titolarita, idUtente);
  }
	
  /** ******************************************** */
  /** ***** SIGMATER END ****** */
  /** ******************************************** */ 

  /** ******************************************** */
  /** ***** ABIO BEGIN ****** */
  /** ******************************************** */

	public OperatoreBiologicoVO getOperatoreBiologicoByIdAzienda(Long idAzienda, Date dataInizioAttivita) 
	  	throws SolmrException, Exception
	{
		return gaaFacade.getOperatoreBiologicoByIdAzienda(idAzienda, dataInizioAttivita);
	}
	
	public PosizioneOperatoreVO[] getAttivitaBiologicheByIdAzienda(
			Long idOperatoreBiologico, Date dataFineValidita, boolean checkStorico)  
  		throws SolmrException, Exception
	{
		return gaaFacade.getAttivitaBiologicheByIdAzienda(idOperatoreBiologico, dataFineValidita, checkStorico);
	}
	
	public CodeDescription[] getODCbyIdOperatoreBiologico(
			Long idOperatoreBiologico, Date dataInizioValidita, boolean pianoCorrente) 
			throws SolmrException, Exception
	{
		return gaaFacade.getODCbyIdOperatoreBiologico(idOperatoreBiologico, dataInizioValidita, pianoCorrente);
	}
	
	public OperatoreBiologicoVO getOperatoreBiologicoAttivo(Long idAzienda)
    throws SolmrException, Exception
  {
	  return gaaFacade.getOperatoreBiologicoAttivo(idAzienda);
  }
  /** ******************************************** */
  /** ***** ABIO END ****** */
  /** ******************************************** */ 
	
	
	/** ******************************************** */
  /** ***** DOCUMENTO BEGIN ****** */
  /** ******************************************** */

  public Vector<BaseCodeDescription> getCategoriaDocumentiTerritorialiAzienda(
      long idAzienda)  throws SolmrException, Exception
  {
    return gaaFacade.getCategoriaDocumentiTerritorialiAzienda(idAzienda);
  }
  
  public Vector<BaseCodeDescription> getDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException, Exception
  {
    return gaaFacade.getDocumentiTerritorialiAzienda(idAzienda);
  }
  
  public Vector<BaseCodeDescription> getProtocolliDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException, Exception
  {
    return gaaFacade.getProtocolliDocumentiTerritorialiAzienda(idAzienda);
  }
  
  public boolean isDocumentoIstanzaRiesame(long idDocumento) throws SolmrException, Exception
  {
    return gaaFacade.isDocumentoIstanzaRiesame(idDocumento);
  }
  
  public Vector<BaseCodeDescription> getCausaleModificaDocumentoValid() 
    throws SolmrException, Exception
  {
    return gaaFacade.getCausaleModificaDocumentoValid();
  }
  
  public Vector<Long> esisteDocumentoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
  throws SolmrException, Exception
  {
    return gaaFacade.esisteDocumentoAttivoByConduzioneAndAzienda(elencoConduzioni, idAzienda);
  }
  
  public Vector<TipoReportVO> getListTipiDocumentoStampaProtocollo(long idDocumento[]) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListTipiDocumentoStampaProtocollo(idDocumento);
  }
  
  public String getTipoDocumentoStampaProtocollo(long idTipoReport) 
    throws SolmrException, Exception
  {
    return gaaFacade.getTipoDocumentoStampaProtocollo(idTipoReport);
  }
  
  public int getCountDocumentoCompatibile(long idTipoReport, long[] idDocumento) 
      throws SolmrException, Exception
  {
    return gaaFacade.getCountDocumentoCompatibile(idTipoReport, idDocumento);
  }
  
  public Vector<CodeDescription> getElencoPropietariDocumento(long[] idDocumento)
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoPropietariDocumento(idDocumento);
  }
  
  public TipoReportVO getTipoReport(long idTipoReport)
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoReport(idTipoReport);
  }
  
  public TipoReportVO getTipoReportByCodice(String codiceReport)
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoReportByCodice(codiceReport);
  }
  
  public Vector<TipoAttestazioneVO> getAttestStampaProtoc(long idReportSubReport)
      throws SolmrException, Exception
  {
    return gaaFacade.getAttestStampaProtoc(idReportSubReport);
  }
  
  public boolean isNumeroProtocolloValido(String numeroProtocollo)
      throws SolmrException, Exception
  {
    return gaaFacade.isNumeroProtocolloValido(numeroProtocollo);
  }
  
  public Vector<ParticellaVO> getElencoParticelleVarCat(long idAzienda, long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoParticelleVarCat(idAzienda, idDichiarazioneConsistenza);
  }    
    
  
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoIdoneita(Long idAzienda, 
      Long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return gaaFacade.getStampaUVRiepilogoIdoneita(idAzienda, idDichiarazioneConsistenza);
  }
  
  public Vector<TipoFormaConduzioneVO> getFormaConduzioneSezioneAnagrafica(Long idAzienda,
      Date dataRiferimento) throws SolmrException, Exception
  {
    return gaaFacade.getFormaConduzioneSezioneAnagrafica(idAzienda, dataRiferimento);
  }
  
  public Vector<TipoInfoAggiuntivaVO> getTipoInfoAggiuntiveStampa(long idAzienda, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException, Exception
  {
    return gaaFacade.getTipoInfoAggiuntiveStampa(idAzienda, dataInserimentoDichiarazione);
  }
  
  public Vector<AnagAziendaVO> getAziendeAssociateStampa(long idAzienda, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException, Exception
  {
    return gaaFacade.getAziendeAssociateStampa(idAzienda, dataInserimentoDichiarazione);
  }
  
  public Vector<ConduzioneParticellaVO> getRiepilogoConduzioneStampa(long idAzienda, 
      Long codFotografia) throws SolmrException, Exception
  {
    return gaaFacade.getRiepilogoConduzioneStampa(idAzienda, codFotografia);
  }
  
  public Vector<StoricoParticellaVO> getRiepilogoComuneStampa(long idAzienda, 
      Long codFotografia) throws SolmrException, Exception
  {
    return gaaFacade.getRiepilogoComuneStampa(idAzienda, codFotografia);
  }   
  
  public Vector<RiepiloghiUnitaArboreaVO> getStampaUVRiepilogoVitignoIdoneita(Long idAzienda, 
      Long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return gaaFacade.getStampaUVRiepilogoVitignoIdoneita(idAzienda, 
        idDichiarazioneConsistenza);
  }
  
  public Vector<FabbricatoVO> getStampaFabbricati(
      Long idAzienda, Date dataRiferimento)  throws SolmrException, Exception
  {
    return gaaFacade.getStampaFabbricati(idAzienda, dataRiferimento);
  }  
  
  public Vector<DocumentoVO> getDocumentiStampaMd(Long idAzienda, Long idDichiarazioneConsistenza,
      String cuaa, java.util.Date dataInserimentoDichiarazione) throws SolmrException, Exception
  {
    return gaaFacade.getDocumentiStampaMd(idAzienda, idDichiarazioneConsistenza, cuaa, dataInserimentoDichiarazione);
  }
  
  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiStoricoPartTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia) throws SolmrException, Exception
  {
    return gaaFacade.getStampaRiepiloghiStoricoPartTipoArea(tipoArea, idAzienda, codFotografia);
  }
  
  public Vector<RiepilogoStampaTerrVO> getStampaRiepiloghiFoglioTipoArea(
      String tipoArea, Long idAzienda, Long codFotografia)  throws SolmrException, Exception
  {
    return gaaFacade.getStampaRiepiloghiFoglioTipoArea(tipoArea, idAzienda, codFotografia);
  }
  
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAlPianoAttuale(
      Long idAzienda, String voceMenu, boolean flagCondizionalita)  throws SolmrException, Exception
  {
    return gaaFacade.getListAttestazioniDichiarazioniAlPianoAttuale(idAzienda, voceMenu, flagCondizionalita);
  }
  
  public Vector<TipoAttestazioneVO> getListAttestazioniDichiarazioniAllaValidazione(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String voceMenu, boolean flagCondizionalita)  
    throws SolmrException, Exception
  {
    return gaaFacade.getListAttestazioniDichiarazioniAllaValidazione(
        codiceFotografiaTerreni, dataInserimentoDichiarazione, voceMenu, flagCondizionalita);
  }  
  
  public Vector<TipoAttestazioneVO> getElencoAllegatiAllaValidazionePerStampa(
      Long codiceFotografiaTerreni, Date dataInserimentoDichiarazione, String codiceAttestazione)
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoAllegatiAllaValidazionePerStampa(codiceFotografiaTerreni, 
        dataInserimentoDichiarazione, codiceAttestazione);
  }  
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReport(long idReportSubReport, 
      Date dataInserimentoDichiarazione) throws SolmrException, Exception
  {
    return gaaFacade.getAttestazioniFromSubReport(idReportSubReport, 
        dataInserimentoDichiarazione);
  }
  
  public Vector<TipoAttestazioneVO> getAttestazioniFromSubReportValidDettagli(long idReportSubReport, 
      Date dataInserimentoDichiarazione, Long codiceFotografiaTerreni) throws SolmrException, Exception
  {
    return gaaFacade.getAttestazioniFromSubReportValidDettagli(idReportSubReport, 
        dataInserimentoDichiarazione, codiceFotografiaTerreni);
  }
  
  public String getTipoReportByValidazioneEAllegato(
      long idDichiarazioneConsistenza, int idTipoAllegato) throws SolmrException, Exception
  {
    return gaaFacade.getTipoReportByValidazioneEAllegato(idDichiarazioneConsistenza, 
        idTipoAllegato);
  } 
  
  public TipoAllegatoVO getTipoAllegatoById(int idTipoAllegato) 
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoAllegatoById(idTipoAllegato);
  } 
  
  public Vector<TipoAllegatoVO> getTipoAllegatoForValidazione(int idMotivoDichiarazione, Date dataInserimentoDichiarazione) 
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoAllegatoForValidazione(idMotivoDichiarazione, dataInserimentoDichiarazione);
  }  
  
  public Vector<TipoAllegatoVO> getTipoAllegatoObbligatorioForValidazione(int idMotivoDichiarazione) 
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoAllegatoObbligatorioForValidazione(idMotivoDichiarazione);
  } 
  
  public TipoFirmatarioVO getTipoFirmatarioById(int idTipoFirmatario)
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoFirmatarioById(idTipoFirmatario);
  } 
  
  //************** Fine stampe *****
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegati(
      long idDocumento)
  throws SolmrException, Exception
  {
    return gaaFacade.getElencoFileAllegati(idDocumento);
  }
  
  public void deleteFileAllegato(long idAllegato, long idDocumento) 
      throws SolmrException, Exception
  {
    gaaFacade.deleteFileAllegato(idAllegato, idDocumento);
  }
  
  public void deleteFileAllegatoAgriWell(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException, Exception
  {
    gaaFacade.deleteFileAllegatoAgriWell(allegatoDocumentoVO);
  }
  
  public void deleteFileAllegatoRichiesta(long idAllegato, long idRichiestaDocumento) 
      throws SolmrException, Exception
  {
    gaaFacade.deleteFileAllegatoRichiesta(idAllegato, idRichiestaDocumento);
  }
  
  public void deleteFileAllegatoNotifica(long idAllegato, long idNotifica) 
      throws SolmrException, Exception
  {
    gaaFacade.deleteFileAllegatoNotifica(idAllegato, idNotifica);
  }
  
  public Long insertFileAllegato(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException, Exception
  {
    return gaaFacade.insertFileAllegato(allegatoDocumentoVO);
  }
  
  public Long insertFileAllegatoRichiesta(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException, Exception
  {
    return gaaFacade.insertFileAllegatoRichiesta(allegatoDocumentoVO);
  }
  
  public Long insertFileAllegatoNotifica(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException, Exception
  {
    return gaaFacade.insertFileAllegatoNotifica(allegatoDocumentoVO);
  }
  
  public AllegatoDocumentoVO getFileAllegato(
      long idAllegato)
  throws SolmrException, Exception
  {
    return gaaFacade.getFileAllegato(idAllegato);
  }
  
  public Date getFirstDataInserimentoDocumento(long idDocumento)
      throws SolmrException, Exception
  {
    return gaaFacade.getFirstDataInserimentoDocumento(idDocumento);
  }
  
  public boolean isIstanzaRiesameFotoInterpretataByDocumento(long idDocumento) 
      throws SolmrException, Exception
  {
    return gaaFacade.isIstanzaRiesameFotoInterpretataByDocumento(idDocumento);
  }
  
  public boolean exitsOtherDocISForParticellaAndAzienda(long idParticella, long idAzienda, Long idDocumento) 
      throws SolmrException, Exception
  {
    return gaaFacade.exitsOtherDocISForParticellaAndAzienda(idParticella, idAzienda, idDocumento);
  }
  
  /*public boolean isFaseIstanzaRiesameEvasa(long idAzienda, int fase, Vector<Long> vIdParticella, String parametro) 
      throws SolmrException, Exception
  {
    return gaaFacade.isFaseIstanzaRiesameEvasa(idAzienda, fase, vIdParticella, parametro);
  }*/
  
  public boolean exsitsDocFaseIstanzaRiesameFasePrec(long idAzienda, int fase, 
      long idParticella, String parametro) throws SolmrException, Exception
  {
    return gaaFacade.exsitsDocFaseIstanzaRiesameFasePrec(idAzienda, fase, 
        idParticella, parametro);
  }
  
  public boolean isPossCreateDocFaseIstanzaRiesameFaseSucc(long idAzienda, long idParticella,
      int idFase, String parametro) throws SolmrException, Exception
  {
    return gaaFacade.isPossCreateDocFaseIstanzaRiesameFaseSucc(idAzienda,
        idParticella, idFase, parametro);
  }
  
  public Date getDateLastBatchIstanzaRiesameOk() throws SolmrException, Exception
  {
    return gaaFacade.getDateLastBatchIstanzaRiesameOk();
  }
  
  public boolean isSitiConvocaValid(long idAzienda, int anno, int fase) throws SolmrException, Exception
  {
    return gaaFacade.isSitiConvocaValid(idAzienda, anno, fase);
  }
  
  public boolean isDataSospensioneScaduta(long idAzienda, long idParticella, int anno) throws SolmrException, 
    Exception
  {
    return gaaFacade.isDataSospensioneScaduta(idAzienda, idParticella, anno);
  }
  
  public boolean isParticellaEvasa(long idAzienda, long idParticella, int fase, int anno) 
      throws SolmrException, Exception
  {
    return gaaFacade.isParticellaEvasa(idAzienda, idParticella, fase, anno);
  }
  
  public boolean isVisibleTastoElimina(long idAzienda, int anno, int fase)  throws SolmrException, 
    Exception
  {
    return gaaFacade.isVisibleTastoElimina(idAzienda, anno, fase);
  }
  
  public Vector<DocumentoVO> getListDocFromRicerca(String strDescDocumento, boolean attivi) 
      throws SolmrException , Exception
  {
    return gaaFacade.getListDocFromRicerca(strDescDocumento, attivi);
  }
  
  public Vector<ParticellaIstanzaRiesameVO> getLisParticellaFromIstanzaFoto(long idAzienda, int anno)  
      throws SolmrException, Exception
  {
    return gaaFacade.getLisParticellaFromIstanzaFoto(idAzienda, anno);
  }
  
  public String annullaIstanzaRiesame(Vector<Long> vIdIstanzaRiesame, long idAzienda, int anno, 
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception
  {
    return gaaFacade.annullaIstanzaRiesame(vIdIstanzaRiesame, idAzienda, anno, ruoloUtenza);
  }
  
  public boolean existAltraFaseFotoParticella(long idAzienda, long idParticella, int anno, long idFase)
      throws SolmrException, Exception
  {
    return gaaFacade.existAltraFaseFotoParticella(idAzienda, idParticella, anno, idFase);
  }
  
  public Vector<TipoDocumentoVO> getDocumentiNuovaIscrizione() 
      throws SolmrException, Exception
  {
    return gaaFacade.getDocumentiNuovaIscrizione();
  }
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiRichiesta(
      long idRichiestaDocumento) throws SolmrException, Exception
  {
    return gaaFacade.getElencoFileAllegatiRichiesta(idRichiestaDocumento);
  }
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiNotifica(
      long idNotifica) throws SolmrException, Exception
  {
    return gaaFacade.getElencoFileAllegatiNotifica(idNotifica);
  }
  
  public boolean isIstanzaAttiva(long idAzienda) throws SolmrException, Exception
  {
    return gaaFacade.isIstanzaAttiva(idAzienda);
  }
  
  public boolean isParticellaIstRiesameCancellabile(long idAzienda, 
      Vector<Long> vIdConduzioneParticella) throws SolmrException, Exception
  {
    return gaaFacade.isParticellaIstRiesameCancellabile(idAzienda, vIdConduzioneParticella);
  }
  
  public boolean existIstanzaEsameAttivaFase(long idAzienda, 
      Long idParticella, long idFase) throws SolmrException, Exception
  {
    return gaaFacade.existIstanzaEsameAttivaFase(idAzienda, idParticella, idFase);
  }
  
  public FaseRiesameDocumentoVO getFaseRiesameDocumentoByIdDocumento(long idDocumento) 
      throws SolmrException, Exception
  {
    return gaaFacade.getFaseRiesameDocumentoByIdDocumento(idDocumento);
  }
  
  public boolean isParticellaInPotenziale(long idAzienda, 
      long idParticella, int anno) throws SolmrException, Exception
  {
    return gaaFacade.isParticellaInPotenziale(idAzienda, idParticella, anno);
  }
  
  public boolean isParticellaInPotenzialeContra(long idAzienda, 
      long idParticella, int anno) throws SolmrException, Exception
  {
    return gaaFacade.isParticellaInPotenzialeContra(idAzienda, idParticella, anno);
  }
  
  public boolean isNotPossibleIstanzaRiesameFaseSuccessiva(long idAzienda, 
      long idParticella, int idFase, int anno, String parametro) 
    throws SolmrException, Exception
  {
    return gaaFacade.isNotPossibleIstanzaRiesameFaseSuccessiva(
        idAzienda, idParticella, idFase, anno, parametro);
  }
  
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazione(
      long idDichiarazioneConsistenza, long idTipoAllegato)
    throws SolmrException, Exception
  {
    return gaaFacade.getAllegatoDichiarazioneFromIdDichiarazione(idDichiarazioneConsistenza, 
        idTipoAllegato);
  }
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione(
      long idDichiarazioneConsistenza, String tipoStampa)
    throws SolmrException, Exception
  {
    return gaaFacade.getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione(idDichiarazioneConsistenza, tipoStampa);
  }
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdAllegato(
      long idAllegato)
    throws SolmrException, Exception
  {
    return gaaFacade.getAllegatoDichiarazioneFromIdAllegato(idAllegato);
  }
  
  /*public Vector<AllegatoDichiarazioneVO> getElencoAllegatiDichiarazioneDefault(
      Date dataInserimentoValidazione, int idMotivoDichiarazione) 
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoAllegatiDichiarazioneDefault(
        dataInserimentoValidazione, idMotivoDichiarazione);
  }*/
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatiAttiviDichiarazione(
      long idDichiarazioneConsistenza) 
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoAllegatiAttiviDichiarazione(idDichiarazioneConsistenza);
  }
  
  public Vector<AllegatoDichiarazioneVO> getAllElencoAllegatiDichiarazione(
      long idDichiarazioneConsistenza) 
    throws SolmrException, Exception
  {
    return gaaFacade.getAllElencoAllegatiDichiarazione(idDichiarazioneConsistenza);
  }
  
  public Vector<TipoFirmaVO> getElencoTipoFirma()
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoFirma();
  }
  
  /** ******************************************** */
  /** ***** DOCUMENTO END ****** */
  /** ******************************************** */ 
  
  
  /*************************************************************************/
  /********************** TERRENI START ***********************************/
  /*************************************************************************/
  
  public PlSqlCodeDescription allineaSupEleggibilePlSql(long idAzienda, 
      long idUtenteAggiornamento)
    throws SolmrException, Exception
  {
    return gaaFacade.allineaSupEleggibilePlSql(idAzienda, idUtenteAggiornamento);
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComune(long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.riepilogoDestinazioneProduttivaComune(idAzienda);
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComuneDichiarato(long idDichiarazioneConsistenza)
    throws SolmrException, Exception
  {
    return gaaFacade.riepilogoDestinazioneProduttivaComuneDichiarato(idDichiarazioneConsistenza);
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVino(long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.riepilogoDestinazioneProduttivaUvaDaVino(idAzienda);
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVinoDichiarato(long idDichiarazioneConsistenza)
    throws SolmrException, Exception
  {
    return gaaFacade.riepilogoDestinazioneProduttivaUvaDaVinoDichiarato(idDichiarazioneConsistenza);
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOP(long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.riepilogoDestinazioneProduttivaVinoDOP(idAzienda);
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOPDichiarato(long idDichiarazioneConsistenza)
    throws SolmrException, Exception
  {
    return gaaFacade.riepilogoDestinazioneProduttivaVinoDOPDichiarato(idDichiarazioneConsistenza);
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOPDichiarato(
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return gaaFacade.riepilogoProvinciaVinoDOPDichiarato(idDichiarazioneConsistenza);
  }
  
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOP(long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.riepilogoProvinciaVinoDOP(idAzienda);
  }
  
  public TreeMap<String,Vector<RiepiloghiUnitaArboreaVO>> riepilogoElencoSociProvinciaVinoDOP(long idAzienda) 
    throws SolmrException, Exception
  {  
    return gaaFacade.riepilogoElencoSociProvinciaVinoDOP(idAzienda);
  }
  
  public Vector<Long> esisteUVValidataByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.esisteUVValidataByConduzioneAndAzienda(elencoConduzioni, idAzienda);
  }
  
  public Vector<Long> esisteUVByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.esisteUVByConduzioneAndAzienda(elencoConduzioni, idAzienda);
  }
  
  public Vector<Long> esisteUVModProcVITIByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.esisteUVModProcVITIByConduzioneAndAzienda(elencoConduzioni, idAzienda);
  }
  
  
  
  public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelSempliceByParameters( 
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO)
    throws SolmrException, Exception
  {
    return gaaFacade.searchStoricoUnitaArboreaExcelSempliceByParameters(idAzienda, filtriUnitaArboreaRicercaVO);
  }
  
  public Vector<StoricoParticellaVO> getListUVForInserimento(Long idParticellaCurr, Vector<Long> vIdParticella, Long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.getListUVForInserimento(idParticellaCurr, vIdParticella, idAzienda);
  }
  
  public BigDecimal getSupEleggibilePlSql(long idParticellaCertificata, long idCatalogoMatrice)
      throws SolmrException, Exception
  {
    return gaaFacade.getSupEleggibilePlSql(idParticellaCertificata, idCatalogoMatrice);
  }
  
  public PlSqlCodeDescription compensazioneAziendalePlSql(long idAzienda, 
      long idUtenteAggiornamento)  throws SolmrException, Exception
  {
    return gaaFacade.compensazioneAziendalePlSql(idAzienda, idUtenteAggiornamento);
  }
  
  public Vector<UVCompensazioneVO> getUVPerCompensazione(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getUVPerCompensazione(idAzienda);
  }
  
  public CompensazioneAziendaVO getCompensazioneAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getCompensazioneAzienda(idAzienda);
  }
  
  public Vector<RiepilogoCompensazioneVO> getRiepilogoPostAllinea(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getRiepilogoPostAllinea(idAzienda);
  }
  
  public Vector<RiepilogoCompensazioneVO> getRiepilogoDirittiVitati(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getRiepilogoDirittiVitati(idAzienda);
  }
  
  public CompensazioneAziendaVO getCompensazioneAziendaByIdAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getCompensazioneAziendaByIdAzienda(idAzienda);
  }
  
  public int countUVAllineabiliGis(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.countUVAllineabiliGis(idAzienda);
  }
  
  public int countUVIstRiesameCompen(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.countUVIstRiesameCompen(idAzienda);
  }
  
  public int countSupUVIIrregolari(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.countSupUVIIrregolari(idAzienda);
  }
  
  public Date getMaxDataAggiornamentoConduzioniAndUV(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getMaxDataAggiornamentoConduzioniAndUV(idAzienda);
  }
  
  public Date getMaxDataFotoInterpretazioneUV(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getMaxDataFotoInterpretazioneUV(idAzienda);
  }
  
  public boolean existsIsoleParcDopoVarSched(long idAzienda, long isolaDichiarata)
      throws SolmrException, Exception
  {
    return gaaFacade.existsIsoleParcDopoVarSched(idAzienda, isolaDichiarata);
  }
  
  public BigDecimal getSupNonCompensabile(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getSupNonCompensabile(idAzienda);
  }
  
  public int countPercPossessoCompensazioneMag100(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.countPercPossessoCompensazioneMag100(idAzienda);
  }
  
  public BigDecimal getPercUtilizzoEleggibile(long idAzienda, long idParticella)
      throws SolmrException, Exception
  {
    return gaaFacade.getPercUtilizzoEleggibile(idAzienda, idParticella);
  }
  
  public Vector<Vector<DirittoCompensazioneVO>> getDirittiCalcolati(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getDirittiCalcolati(idAzienda);
  }
  
  public BigDecimal getSumAreaGiaAssegnata(long idUnitaArborea)
      throws SolmrException, Exception
  {
    return gaaFacade.getSumAreaGiaAssegnata(idUnitaArborea);
  }
  
  public BigDecimal getSumAreaMaxAssegnabile(long idUnitaArborea)
      throws SolmrException, Exception
  {
    return gaaFacade.getSumAreaMaxAssegnabile(idUnitaArborea);
  }
  
  public void allineaUVaCompensazione(long idAzienda, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception
  {
    gaaFacade.allineaUVaCompensazione(idAzienda, ruoloUtenza);
  }
  
  /*public void allineaUVinTolleranzaGIS(long idAzienda, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception
  {
    gaaFacade.allineaUVinTolleranzaGIS(idAzienda, ruoloUtenza);
  }*/
  
  public ParticellaBioVO getParticellaBio(long idParticella, long idAzienda, Date dataInserimentoDichiarazione)
    throws SolmrException, Exception
  {
    return gaaFacade.getParticellaBio(idParticella, idAzienda, dataInserimentoDichiarazione);
  }
  
  public BigDecimal getSumUtilizziPrimariNoIndicati(long idAzienda, long idParticella, Vector<String> vIdUtilizzo)
    throws SolmrException, Exception
  {
    return gaaFacade.getSumUtilizziPrimariNoIndicati(idAzienda, idParticella, vIdUtilizzo);
  }
  
  public BigDecimal getSumUtilizziPrimariParticellaAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) throws SolmrException, Exception
  {
    return gaaFacade.getSumUtilizziPrimariParticellaAltreConduzioni(idAzienda, idParticella, vIdConduzioneParticella);
  }
  
  public BigDecimal getSumPercentualPossessoAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) throws SolmrException, Exception
  {
    return gaaFacade.getSumPercentualPossessoAltreConduzioni(idAzienda, idParticella, vIdConduzioneParticella);
  }
  
  public BigDecimal getSumSupCondottaAltreConduzioni(long idAzienda, long idParticella,
      Vector<Long> vIdConduzioneParticella) throws SolmrException, Exception
  {
    return gaaFacade.getSumSupCondottaAltreConduzioni(idAzienda, idParticella, vIdConduzioneParticella);
  }
  
  public Vector<IstanzaRiesameVO> getFasiIstanzaRiesame(long idAzienda, long idParticella,
      Date dataInserimentoDichiarazione) throws SolmrException, Exception
  {
    return gaaFacade.getFasiIstanzaRiesame(idAzienda, idParticella, dataInserimentoDichiarazione);
  }
  
  public int calcolaP30PlSql(long idStoricoParticella, Date dataInizioValidita)
      throws SolmrException, Exception
  {
    return gaaFacade.calcolaP30PlSql(idStoricoParticella, dataInizioValidita);
  }
  
  public int calcolaP25PlSql(long idStoricoParticella, Date dataInizioValidita)
      throws SolmrException, Exception
  {
    return gaaFacade.calcolaP25PlSql(idStoricoParticella, dataInizioValidita);
  }
  
  public int calcolaP26PlSql(long idAzienda, long idParticella, Long idParticellaCertificata)
      throws SolmrException, Exception
  {
    return gaaFacade.calcolaP26PlSql(idAzienda, idParticella, idParticellaCertificata);
  }
  
  public Vector<CasoParticolareVO> getCasiParticolari(String particellaObbligatoria)
      throws SolmrException, Exception
  {
    return gaaFacade.getCasiParticolari(particellaObbligatoria);
  }
  
  public Vector<TipoRiepilogoVO> getTipoRiepilogo(String funzionalita, String codiceRuolo)
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoRiepilogo(funzionalita, codiceRuolo);
  }
  
  public boolean isAltreUvDaSchedario(long idParticella)
      throws SolmrException, Exception
  {
    return gaaFacade.isAltreUvDaSchedario(idParticella);
  }
  
  public Vector<TipoEsportazioneDatiVO> getTipoEsportazioneDati(String codMenu, String codiceRuolo)
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoEsportazioneDati(codMenu, codiceRuolo);
  }
  
  public boolean isParticellAttivaStoricoParticella(String istatComune, String sezione,
      String foglio, String particella, String subalterno) 
    throws SolmrException, Exception
  {
    return gaaFacade.isParticellAttivaStoricoParticella(istatComune, sezione, 
        foglio, particella, subalterno);
  }
  
  public Vector<StoricoParticellaVO> getElencoParticelleForPopNotifica(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO)
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoParticelleForPopNotifica(filtriParticellareRicercaVO);
  }
  
  public StoricoParticellaVO findStoricoParticellaDichCompleto(long idStoricoParticella, 
      long idDichiarazioneConsistenza)
    throws SolmrException, Exception
  {
    return gaaFacade.findStoricoParticellaDichCompleto(idStoricoParticella, idDichiarazioneConsistenza);
  }
  
  public Vector<TipoEfaVO> getListTipoEfa() throws SolmrException, Exception
  {
    return gaaFacade.getListTipoEfa();
  }
  
  public Vector<TipoEfaVO> getListLegendaTipoEfa() throws SolmrException, Exception
  {
    return gaaFacade.getListLegendaTipoEfa();
  }
  
  public TipoEfaVO getTipoEfaFromIdCatalogoMatrice(long idCatalogoMatrice)
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoEfaFromIdCatalogoMatrice(idCatalogoMatrice);
  }
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSemina() 
      throws SolmrException, Exception
  {
    return gaaFacade.getListTipoPeriodoSemina();
  }
  
  public Vector<TipoUtilizzoVO> getListTipoUtilizzoEfa(long idTipoEfa)
      throws SolmrException, Exception
  {
    return gaaFacade.getListTipoUtilizzoEfa(idTipoEfa);
  }
   
  public Vector<TipoVarietaVO> getListTipoVarietaEfaByMatrice(long idTipoEfa, 
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso)
      throws SolmrException, Exception
  {
    return gaaFacade.getListTipoVarietaEfaByMatrice(idTipoEfa, idUtilizzo, 
        idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);
  }
  
  public Vector<TipoDestinazioneVO> getListTipoDestinazioneEfa(long idTipoEfa, long idUtilizzo)
      throws SolmrException, Exception
  {
    return gaaFacade.getListTipoDestinazioneEfa(idTipoEfa, idUtilizzo);
  }    
   
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione)  throws SolmrException, Exception
  {
    return gaaFacade.getListDettaglioUsoEfaByMatrice(idTipoEfa, idUtilizzo, idTipoDestinazione);
  }
  
  public Vector<TipoQualitaUsoVO> getListQualitaUsoEfaByMatrice(long idTipoEfa,
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso) 
          throws SolmrException, Exception
  {
    return gaaFacade.getListQualitaUsoEfaByMatrice(idTipoEfa, idUtilizzo, 
        idTipoDestinazione, idTipoDettaglioUso);
  }
  
  public Integer getAbbPonderazioneByMatrice(
      long idTipoEfa, long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso,
      long idTipoQualitaUso, long idVarieta)
    throws SolmrException, Exception
  {
    return gaaFacade.getAbbPonderazioneByMatrice(idTipoEfa, idUtilizzo, 
        idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso, idVarieta);
  }
  
  public String getValoreAttivoTipoAreaFromParticellaAndId(
      long idParticella, long idTipoArea)
          throws SolmrException, Exception
  {
    return gaaFacade.getValoreAttivoTipoAreaFromParticellaAndId(idParticella, idTipoArea);
  }
  
  public Vector<TipoAreaVO> getValoriTipoAreaParticella(long idParticella, Date dataInserimentoValidazione)
      throws SolmrException, Exception
  {
    return gaaFacade.getValoriTipoAreaParticella(idParticella, dataInserimentoValidazione);
  }
  
  public Vector<TipoAreaVO> getAllValoriTipoArea()
      throws SolmrException, Exception
  {
    return gaaFacade.getAllValoriTipoArea();
  }
  
  public Vector<TipoAreaVO> getDescTipoAreaBrogliaccio(Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return gaaFacade.getDescTipoAreaBrogliaccio(idDichiarazioneConsistenza);
  }    
  
  public Vector<TipoAreaVO> getValoriTipoAreaFoglio(String comune, String foglio, String sezione)
      throws SolmrException, Exception
  {
    return gaaFacade.getValoriTipoAreaFoglio(comune, foglio, sezione);
  }
  
  public Vector<TipoMetodoIrriguoVO> getElencoMetodoIrriguo()
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoMetodoIrriguo();
  }
  
  public Vector<TipoPeriodoSeminaVO> getListTipoPeriodoSeminaByCatalogo(long idUtilizzo,
      long idDestinazione, long idDettUso, long idQualiUso, long idVarieta)
      throws SolmrException, Exception
  {
    return gaaFacade.getListTipoPeriodoSeminaByCatalogo(idUtilizzo,
        idDestinazione, idDettUso, idQualiUso, idVarieta);
  }
  
  public Vector<TipoFonteVO> getElencoAllTipoFonte() throws SolmrException, Exception
  {
    return gaaFacade.getElencoAllTipoFonte();
  }
  
  public boolean isUtilizzoAttivoSuMatrice(long idUtilizzo) throws SolmrException, Exception
  {
    return gaaFacade.isUtilizzoAttivoSuMatrice(idUtilizzo);
  }
  
  public Vector<TipoAreaVO> riepilogoTipoArea(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.riepilogoTipoArea(idAzienda);
  }
  
  public Vector<TipoAreaVO> riepilogoTipoAreaDichiarato(long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return gaaFacade.riepilogoTipoAreaDichiarato(idDichiarazioneConsistenza);
  }
  
  public Vector<TipoAreaVO> getValoriTipoAreaFiltroElenco(Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return gaaFacade.getValoriTipoAreaFiltroElenco(idDichiarazioneConsistenza);
  }
      
  
  public boolean isDettaglioUsoObbligatorio(long idTipoEfa,
      long idVarieta) throws SolmrException, Exception
  {
    return gaaFacade.isDettaglioUsoObbligatorio(idTipoEfa, idVarieta);
  }
  
  public TipoEfaVO getTipoEfaFromPrimaryKey(long idTipoEfa)
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoEfaFromPrimaryKey(idTipoEfa);
  }
  
  public Vector<Long> getIdParticellaByIdConduzione(Vector<Long> vIdConduzioneParticella)
    throws SolmrException, Exception
  {
    return gaaFacade.getIdParticellaByIdConduzione(vIdConduzioneParticella);
  }
  
  public Vector<ProvinciaVO> getListProvincieParticelleByIdAzienda(Long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.getListProvincieParticelleByIdAzienda(idAzienda);  
  }
  
  public Vector<ProvinciaVO> getListProvincieParticelleByIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return gaaFacade.getListProvincieParticelleByIdDichiarazioneConsistenza(idDichiarazioneConsistenza);
  }
  
  public Vector<IsolaParcellaVO> getElencoIsoleParcelle(String nomeLib, long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoIsoleParcelle(nomeLib, idAzienda);
  }
  
  public Vector<StoricoParticellaVO> associaParcelleGisLettura(String nomeLib, long idAzienda,long idUtente)
    throws SolmrException, Exception
  {
    return gaaFacade.associaParcelleGisLettura(nomeLib, idAzienda,idUtente);
  }
  
  public void associaParcelleGisConferma(long idAzienda, long idUtente, Vector<Long> vIdUnarParcellaSel)
    throws SolmrException, Exception
  {
    gaaFacade.associaParcelleGisConferma(idAzienda,idUtente,vIdUnarParcellaSel);
  }
  
  public ConsistenzaVO getDichConsUVParcelleDoppie(long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.getDichConsUVParcelleDoppie(idAzienda);
  }
  
  public void allineaUVaGIS(Vector<Long> vIdIsolaParcella, long idAzienda, long idDichiarazioneConsistenza, RuoloUtenza ruoloUtenza)
    throws SolmrException, Exception
  {
    gaaFacade.allineaUVaGIS(vIdIsolaParcella, idAzienda, idDichiarazioneConsistenza, ruoloUtenza);
  }
  
  /*public Integer getTolleranzaPlSql(String nomeLib, long idAzienda, long idUnitaArborea)
    throws SolmrException, Exception
  {
    return gaaFacade.getTolleranzaPlSql(nomeLib, idAzienda, idUnitaArborea);
  }*/
  
  public Vector<RegistroPascoloVO> getRegistroPascoliPianoLavorazione(
      long idParticellaCertificata) throws SolmrException, Exception
  {
    return gaaFacade.getRegistroPascoliPianoLavorazione(idParticellaCertificata);
  }
  
  public Vector<RegistroPascoloVO> getRegistroPascoliPianoLavorazioneChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno) 
    throws SolmrException, Exception
  {
    return gaaFacade.getRegistroPascoliPianoLavorazioneChiaveCatastale(
        istatComune, foglio, particella, sezione, subalterno);
  }
  
  public Vector<RegistroPascoloVO> getRegistroPascoliDichCons(
      long idParticellaCertificata, int annoDichiarazione)
    throws SolmrException, Exception
  {
    return gaaFacade.getRegistroPascoliDichCons(idParticellaCertificata, annoDichiarazione);
  }
  
  public Vector<RegistroPascoloVO> getRegistroPascoliDichConsChiaveCatastale(
      String istatComune, String foglio, String particella, String sezione, String subalterno, int annoDichiarazione)
    throws SolmrException, Exception
  {
    return gaaFacade.getRegistroPascoliDichConsChiaveCatastale(
        istatComune, foglio, particella, sezione, subalterno, annoDichiarazione);
  }
  
  public boolean isRegistroPascoliPratoPolifita(
      long idParticellaCertificata) throws SolmrException, Exception
  {
    return gaaFacade.isRegistroPascoliPratoPolifita(idParticellaCertificata);
  }
  
  
  public Vector<Long> getIdConduzioneFromIdAziendaIdParticella(long idAzienda, long idParticella)
      throws SolmrException, Exception
  {
    return gaaFacade.getIdConduzioneFromIdAziendaIdParticella(idAzienda, idParticella);
  }
  
  public Vector<AnagParticellaExcelVO> searchParticelleStabGisExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.searchParticelleStabGisExcelByParameters(
        filtriParticellareRicercaVO, idAzienda);
  }
  
  public Vector<AnagParticellaExcelVO> searchParticelleAvvicExcelByParameters(
      FiltriParticellareRicercaVO filtriParticellareRicercaVO, Long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.searchParticelleAvvicExcelByParameters(filtriParticellareRicercaVO, 
        idAzienda);
  }
  
  /*public HashMap<Long,HashMap<Integer,AvvicendamentoVO>> getElencoAvvicendamento( 
      Vector<Long> vIdParticella, long idAzienda, Integer annoPartenza, int numeroAnni, 
      Long idDichiarazioneConsistenza) 
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoAvvicendamento(vIdParticella, idAzienda, annoPartenza, 
        numeroAnni, idDichiarazioneConsistenza);
  }*/
  
  public Vector<Long> getIdUtilizzoFromIdIdConduzione(long idConduzioneParticella) 
      throws SolmrException, Exception
  {
    return gaaFacade.getIdUtilizzoFromIdIdConduzione(idConduzioneParticella);
  }
  
  public BigDecimal getSumSupUtilizzoParticellaAzienda(long idAzienda, long idParticella) 
      throws SolmrException, Exception
  {
    return gaaFacade.getSumSupUtilizzoParticellaAzienda(idAzienda, idParticella);
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaPianoLavorazione(long idAzienda) 
      throws SolmrException, Exception
  {
    return gaaFacade.riepilogoTipoEfaPianoLavorazione(idAzienda);
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoEfaDichiarazione(long idDichiarazioneConsistenza)  
      throws SolmrException, Exception
  {
    return gaaFacade.riepilogoTipoEfaDichiarazione(idDichiarazioneConsistenza);
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningPianoLavorazione(
      long idAzienda) throws SolmrException, Exception
  {
    return gaaFacade.riepilogoTipoGreeningPianoLavorazione(idAzienda);
  }
  
  public Vector<UtilizzoParticellaVO> riepilogoTipoGreeningDichiarazione(long idDichiarazioneConsistenza) 
      throws SolmrException, Exception
  {
    return gaaFacade.riepilogoTipoGreeningDichiarazione(idDichiarazioneConsistenza);
  }
  
  public Vector<TipoEfaVO> getElencoTipoEfaForAzienda(long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoEfaForAzienda(idAzienda);
  }
  
  public Vector<TipoDestinazioneVO> getElencoTipoDestinazioneByMatrice(long idUtilizzo)
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoDestinazioneByMatrice(idUtilizzo);
  }
  
  public Vector<TipoVarietaVO> getElencoTipoVarietaByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso)
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoVarietaByMatrice(idUtilizzo, 
        idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);
  }
  
  public Vector<TipoQualitaUsoVO> getElencoTipoQualitaUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione, long idTipoDettaglioUso) 
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoQualitaUsoByMatrice(
        idUtilizzo, idTipoDestinazione, idTipoDettaglioUso);
  }
  
  public CatalogoMatriceVO getCatalogoMatriceFromMatrice(long idUtilizzo, long idVarieta,
      long idTipoDestinazione, long idTipoDettaglioUso, long idTipoQualitaUso) 
          throws SolmrException, Exception
  {
    return gaaFacade.getCatalogoMatriceFromMatrice(
        idUtilizzo, idVarieta, idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);
  }
  
  public CatalogoMatriceVO getCatalogoMatriceFromPrimariKey(long idCatalogoMatrice) 
      throws SolmrException, Exception
  {
    return gaaFacade.getCatalogoMatriceFromPrimariKey(idCatalogoMatrice);
  }
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaDefault(long idCatalogoMatrice)
      throws SolmrException, Exception
  {
    return gaaFacade.getCatalogoMatriceSeminaDefault(idCatalogoMatrice);
  }
  
  public CatalogoMatriceSeminaVO getCatalogoMatriceSeminaByIdTipoPeriodo(long idCatalogoMatrice, 
      long idTipoPeriodoSemina) throws SolmrException, Exception
  {
    return gaaFacade.getCatalogoMatriceSeminaByIdTipoPeriodo(
        idCatalogoMatrice, idTipoPeriodoSemina);
  }
  
  public Vector<Long> getListIdPraticaMantenimentoPlSql(
      long idCatalogoMatrice, String flagDefault) throws SolmrException, Exception
  {
    return gaaFacade.getListIdPraticaMantenimentoPlSql(idCatalogoMatrice, flagDefault);
  }
  
  public Vector<TipoSeminaVO> getElencoTipoSemina() throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoSemina();
  }
  
  public Vector<TipoPraticaMantenimentoVO> getElencoPraticaMantenimento(
      Vector<Long> vIdMantenimento) throws SolmrException, Exception
  {
    return gaaFacade.getElencoPraticaMantenimento(vIdMantenimento);
  }
  
  public Vector<TipoFaseAllevamentoVO> getElencoFaseAllevamento() throws SolmrException, Exception
  {
    return gaaFacade.getElencoFaseAllevamento();
  }
  
  public String cambiaPercentualePossessoMassivo(RuoloUtenza ruoloUtenza, 
      long idAzienda, BigDecimal percentualePossesso) throws SolmrException, Exception
  {
    return gaaFacade.cambiaPercentualePossessoMassivo(ruoloUtenza, idAzienda, percentualePossesso);
  }
  
  public BigDecimal getSumSupCondottaAsservimentoParticellaAzienda(long idAzienda, long idParticella) 
      throws SolmrException, Exception
  {
    return gaaFacade.getSumSupCondottaAsservimentoParticellaAzienda(idAzienda, idParticella);
  }
  
  public String cambiaPercentualePossessoSupUtilizzataMassivo(RuoloUtenza ruoloUtenza, Long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.cambiaPercentualePossessoSupUtilizzataMassivo(ruoloUtenza, idAzienda);
  }
  
  public void avviaConsolidamento(long idAzienda, RuoloUtenza ruoloUtenza) 
      throws SolmrException, Exception
  {
    gaaFacade.avviaConsolidamento(idAzienda, ruoloUtenza);
  }
  
  public void modificaConduzioneEleggibileUV(HashMap<Long, ConduzioneEleggibilitaVO> hPartCondEleg)
      throws SolmrException, Exception
  {
    gaaFacade.modificaConduzioneEleggibileUV(hPartCondEleg);
  }
  
  public void allineaPercUsoElegg(long idAzienda, Vector<Long> vIdParticella, long idUtente)
      throws SolmrException, Exception
  {
    gaaFacade.allineaPercUsoElegg(idAzienda, vIdParticella, idUtente);
  }
  
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione) 
      throws SolmrException, Exception
  {
    return gaaFacade.getListDettaglioUsoByMatrice(
        idUtilizzo, idTipoDestinazione);
  }
  
  public TipoDettaglioUsoVO findDettaglioUsoByPrimaryKey(
      long idTipoDettaglioUso) throws SolmrException, Exception
  {
    return gaaFacade.findDettaglioUsoByPrimaryKey(idTipoDettaglioUso);
  }
 
  
  /*************************************************************************/
  /********************** TERRENI END ***********************************/
  /*************************************************************************/
  
  
  /*************************************************************************/
  /********************** STAMPA START ***********************************/
  /*************************************************************************/
  
  public RichiestaTipoReportVO[] getElencoSubReportRichiesta(
      String codiceReport, java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    return gaaFacade.getElencoSubReportRichiesta(codiceReport, dataRiferimento);
  }
  
  public AnagAziendaVO getAnagraficaAzienda(Long idAzienda,
      java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    return gaaFacade.getAnagraficaAzienda(idAzienda, dataRiferimento);
  }
  
  public Vector<ParticellaVO> getParticelleUtilizzoIstanzaRiesame(long idDocumento)
      throws SolmrException, Exception
  {
    return gaaFacade.getParticelleUtilizzoIstanzaRiesame(idDocumento);
  }
  
  public BigDecimal getSumSupCatastaleTerreniIstanzaRiesame(long idDocumento)
    throws SolmrException, Exception
  {
    return gaaFacade.getSumSupCatastaleTerreniIstanzaRiesame(idDocumento);
  }
  
  public BigDecimal[] getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame(long idDocumento)
    throws SolmrException, Exception
  {
    return gaaFacade.getSumSupAgronomicaAndCondottaTerreniIstanzaRiesame(idDocumento);
  }
  
  public Vector<TipoReportVO> getElencoTipoReport(
      String codiceTipoReport, java.util.Date dataRiferimento) throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoReport(codiceTipoReport, dataRiferimento);
  }
  
  public Vector<TipoReportVO> getElencoTipoReportByValidazione(
      String codiceTipoReport, Long idDichiarazioneConsistenza) 
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoReportByValidazione(codiceTipoReport, idDichiarazioneConsistenza);
  }
  
  public DichiarazioneConsistenzaGaaVO getDatiDichiarazioneConsistenza(
      long idDichiarazioneConsistenza) throws SolmrException, Exception
  {
    return gaaFacade.getDatiDichiarazioneConsistenza(idDichiarazioneConsistenza);
  }
  
  public LogoVO getLogo(String istatRegione, String provincia) 
      throws SolmrException, Exception
  {
    return gaaFacade.getLogo(istatRegione, provincia);
  }
  
  public InfoFascicoloVO getInfoFascicolo(
      long idAzienda, java.util.Date dataRiferimento, Long codFotografia) 
    throws SolmrException, Exception
  {
    return gaaFacade.getInfoFascicolo(idAzienda, dataRiferimento, codFotografia);
  }
  
  public ValoriCondizionalitaVO getValoriCondizionalita(Long idAzienda, Long codiceFotografia) 
    throws SolmrException, Exception
  {
    return gaaFacade.getValoriCondizionalita(idAzienda, codiceFotografia);
  }
  
  public TipoAttestazioneVO[] getListAttestazioniAlPianoAttuale(
      Long idAzienda, Long idAllegato) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListAttestazioniAlPianoAttuale(idAzienda, idAllegato);
  }
  
  public TipoAttestazioneVO[] getListAttestazioneAllaDichiarazione(
      Long codiceFotografiaTerreni, java.util.Date dataAnnoCampagna, Long idAllegato) 
    throws SolmrException, Exception
  {
    return gaaFacade.getListAttestazioneAllaDichiarazione(
        codiceFotografiaTerreni, dataAnnoCampagna, idAllegato);
  }
  
  
  public Vector<ContoCorrenteVO> getStampaContiCorrenti(
      Long idAzienda, java.util.Date dataInserimentoDichiarazione)
    throws SolmrException, Exception
  {
    return gaaFacade.getStampaContiCorrenti(idAzienda, dataInserimentoDichiarazione);
  }
  
  /*************************************************************************/
  /********************** STAMPA END ***********************************/
  /*************************************************************************/
  
  /*************************************************************************/
  /********************** POLIZZA START ***********************************/
  /*************************************************************************/
  
  public Vector<Integer> getElencoAnnoCampagnaByIdAzienda(long idAzienda) 
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoAnnoCampagnaByIdAzienda(idAzienda);
  }
  
  public Vector<BaseCodeDescription> getElencoInterventoByIdAzienda(long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoInterventoByIdAzienda(idAzienda);
  }
  
  public Vector<PolizzaVO> getElencoPolizze(long idAzienda, Integer annoCampagna, Long idIntervento)
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoPolizze(idAzienda, annoCampagna, idIntervento);
  }
  
  public PolizzaVO getDettaglioPolizza(long idPolizzaAssicurativa)
    throws SolmrException, Exception
  {
    return gaaFacade.getDettaglioPolizza(idPolizzaAssicurativa);
  }
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaColtura(long idPolizzaAssicurativa)
    throws SolmrException, Exception
  {
    return gaaFacade.getDettaglioPolizzaColtura(idPolizzaAssicurativa);
  }
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaStruttura(long idPolizzaAssicurativa)
    throws SolmrException, Exception
  {
    return gaaFacade.getDettaglioPolizzaStruttura(idPolizzaAssicurativa);
  }
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaZootecnia(long idPolizzaAssicurativa)
    throws SolmrException, Exception
  {
    return gaaFacade.getDettaglioPolizzaZootecnia(idPolizzaAssicurativa);
  }
  
  /*************************************************************************/
  /********************** POLIZZA END ***********************************/
  /*************************************************************************/
  
  
  /*************************************************************************/
  /********************** MANUALE START ***********************************/
  /*************************************************************************/
  
  public Vector<ManualeVO> getElencoManualiFromRuoli(String descRuolo) 
    throws SolmrException, Exception
  {
    return gaaFacade.getElencoManualiFromRuoli(descRuolo);
  }
  
  public ManualeVO getManuale(long idManuale) throws SolmrException, Exception
  {
    return gaaFacade.getManuale(idManuale);
  }
  
  /*************************************************************************/
  /********************** MANUALE END ***********************************/
  /*************************************************************************/
  
  
  /*************************************************************************/
  /********************** CONSISTENZA START ***********************************/
  /*************************************************************************/
  
  public InvioFascicoliVO getLastSchedulazione(long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return gaaFacade.getLastSchedulazione(idDichiarazioneConsistenza);
  }
  
  public boolean trovaSchedulazioneAttiva(long idAzienda, long idDichiarazioneConsistenza)
      throws SolmrException, Exception
  {
    return gaaFacade.trovaSchedulazioneAttiva(idAzienda, idDichiarazioneConsistenza);
  }
  
  public void insertSchedulazione(InvioFascicoliVO invioFascicoliVO, long idUtente)
      throws SolmrException, Exception
  {
    gaaFacade.insertSchedulazione(invioFascicoliVO, idUtente);
  }
  
  public void deleteSchedulazione(long idInvioFascicoli) 
      throws SolmrException, Exception
  {
    gaaFacade.deleteSchedulazione(idInvioFascicoli);
  }
  
  /*************************************************************************/
  /********************** CONSISTENZA END ***********************************/
  /*************************************************************************/
  
  
  /*************************************************************************/
  /********************** NUOVA ISCRIZIONE START ***********************************/
  /*************************************************************************/
  
  public AziendaNuovaVO getAziendaNuovaIscrizione(String cuaa, long[] arrTipoRichiesta)      
      throws SolmrException, Exception
  {
    return gaaFacade.getAziendaNuovaIscrizione(cuaa, arrTipoRichiesta);
  }
  
  public AziendaNuovaVO getAziendaNuovaIscrizioneEnte(String codEnte, long[] arrTipoRichiesta)      
      throws SolmrException, Exception
  {
    return gaaFacade.getAziendaNuovaIscrizioneEnte(codEnte, arrTipoRichiesta);
  }
  
  public AziendaNuovaVO getAziendaNuovaIscrizioneByPrimaryKey(Long idAziendaNuova)     
      throws SolmrException, Exception
  {
    return gaaFacade.getAziendaNuovaIscrizioneByPrimaryKey(idAziendaNuova);
  }
  
  public Long insertAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento)     
      throws SolmrException, Exception
  {
    return gaaFacade.insertAziendaNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento);
  }
  
  public Long insertAziendaNuovaRichiestaValCess(RichiestaAziendaVO richiestaAziendaVO)     
      throws SolmrException, Exception
  {
    return gaaFacade.insertAziendaNuovaRichiestaValCess(richiestaAziendaVO);
  }
  
  public Long insertAziendaNuovaRichiestaVariazione(RichiestaAziendaVO richiestaAziendaVO)
      throws SolmrException, Exception
  {
    return gaaFacade.insertAziendaNuovaRichiestaVariazione(richiestaAziendaVO);
  }
  
  public void updateAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento)     
      throws SolmrException, Exception
  {
    gaaFacade.updateAziendaNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento);
  }
  
  public Vector<UteAziendaNuovaVO> getUteAziendaNuovaIscrizione(
      long idAziendaNuova)  throws SolmrException, Exception
  {
    return  gaaFacade.getUteAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public void aggiornaUteAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento, Vector<UteAziendaNuovaVO> vUteAziendaNuova)     
      throws SolmrException, Exception
  {
    gaaFacade.aggiornaUteAziendaNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, vUteAziendaNuova);
  }
  
  public Vector<FabbricatoAziendaNuovaVO> getFabbrAziendaNuovaIscrizione(long idAziendaNuova)     
      throws SolmrException, Exception
  {
    return gaaFacade.getFabbrAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public void aggiornaFabbrAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<FabbricatoAziendaNuovaVO> vFabbrAziendaNuova)  throws SolmrException, Exception
  {
    gaaFacade.aggiornaFabbrAziendaNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, vFabbrAziendaNuova);
  }
  
  public boolean existsDependenciesUte(long idUteAziendaNuova)      
      throws SolmrException, Exception
  {
    return gaaFacade.existsDependenciesUte(idUteAziendaNuova);
  } 
  
  public Vector<ParticellaAziendaNuovaVO> getParticelleAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return gaaFacade.getParticelleAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public Vector<UnitaMisuraVO> getListUnitaMisura()  throws SolmrException, Exception
  {
    return gaaFacade.getListUnitaMisura();
  }
  
  public void aggiornaParticelleAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<ParticellaAziendaNuovaVO> vParticelleAziendaNuova) 
    throws SolmrException, Exception
  {
    gaaFacade.aggiornaParticelleAziendaNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, vParticelleAziendaNuova);
  }
  
  public Vector<AllevamentoAziendaNuovaVO> getAllevamentiAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return gaaFacade.getAllevamentiAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public void aggiornaAllevamentiAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento, 
      Vector<AllevamentoAziendaNuovaVO> vAllevamentiAziendaNuova) throws SolmrException, Exception
  {
    gaaFacade.aggiornaAllevamentiAziendaNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, vAllevamentiAziendaNuova);
  }
  
  public Vector<CCAziendaNuovaVO> getCCAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return gaaFacade.getCCAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public void aggiornaCCAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento, 
      Vector<CCAziendaNuovaVO> vCCAziendaNuova) throws SolmrException, Exception
  {
    gaaFacade.aggiornaCCAziendaNuovaIscrizione(aziendaNuovaVO, idUtenteAggiornamento, vCCAziendaNuova);
  }
  
  public Vector<RichiestaAziendaDocumentoVO> getAllegatiAziendaNuovaIscrizione(
      long idAziendaNuova, long idTipoRichiesta) throws SolmrException, Exception
  {
    return gaaFacade.getAllegatiAziendaNuovaIscrizione(idAziendaNuova, idTipoRichiesta);
  }
  
  public Long insertRichAzDocAziendaNuova(RichiestaAziendaDocumentoVO richiestaAziendaDocumentoVO)
      throws SolmrException, Exception
  {
    return gaaFacade.insertRichAzDocAziendaNuova(richiestaAziendaDocumentoVO);
  }
  
  public void deleteDocumentoRichiesta(long idRichiestaDocumento) throws SolmrException,
    Exception
  {
    gaaFacade.deleteDocumentoRichiesta(idRichiestaDocumento);
  }
  
  public void insertFileStampa(long idRichiestaAzienda,  byte ba[])
      throws SolmrException, Exception
  {
    gaaFacade.insertFileStampa(idRichiestaAzienda, ba);
  }
  
  public void aggiornaStatoNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, IterRichiestaAziendaVO iterRichiestaAziendaVO)     
        throws SolmrException, Exception
  {
    gaaFacade.aggiornaStatoNuovaIscrizione(aziendaNuovaVO, 
        idUtenteAggiornamento, iterRichiestaAziendaVO);
  }
  
  public void aggiornaStatoRichiestaValCess(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, IterRichiestaAziendaVO iterRichiestaAziendaVO)     
      throws SolmrException, Exception
  {
    gaaFacade.aggiornaStatoRichiestaValCess(aziendaNuovaVO, 
        idUtenteAggiornamento, iterRichiestaAziendaVO);
  }
  
  public Vector<Long> getElencoIdRichiestaAzienda(
      Long idTipoRichiesta, Long idStatoRichiesta, String cuaa, String partitaIva, String denominazione,
      Long idAzienda, RuoloUtenza ruoloUtenza) throws SolmrException, Exception
  {
    return gaaFacade.getElencoIdRichiestaAzienda(idTipoRichiesta, idStatoRichiesta, cuaa,
        partitaIva, denominazione, idAzienda, ruoloUtenza);
  }
  
  public Vector<Long> getElencoIdRichiestaAziendaGestCaa(
      Long idTipoRichiesta, Long idStatoRichiesta, String cuaa, String partitaIva, String denominazione, 
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception
  {
    return gaaFacade.getElencoIdRichiestaAziendaGestCaa(idTipoRichiesta, 
        idStatoRichiesta, cuaa, partitaIva, denominazione, ruoloUtenza);
  }
  
  public Vector<Long> getElencoRichieseteAziendaByIdRichiestaAzienda(long idAzienda, String codiceRuolo) 
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoRichieseteAziendaByIdRichiestaAzienda(idAzienda, codiceRuolo);
  }
  
  public Vector<AziendaNuovaVO> getElencoAziendaNuovaByIdRichiestaAzienda(
      Vector<Long> vIdRichiestaAzienda) throws SolmrException, Exception
  {
    return gaaFacade.getElencoAziendaNuovaByIdRichiestaAzienda(vIdRichiestaAzienda);
  }
  
  public Vector<CodeDescription> getListTipoRichiesta() throws SolmrException, Exception
  {
    return gaaFacade.getListTipoRichiesta();
  }
  
  public Vector<CodeDescription> getListTipoRichiestaVariazione(String codiceRuolo) throws SolmrException,
     Exception
  {
    return gaaFacade.getListTipoRichiestaVariazione(codiceRuolo);
  }
  
  public Vector<StatoRichiestaVO> getListStatoRichiesta() throws SolmrException, Exception
  {
    return gaaFacade.getListStatoRichiesta();
  }
  
  public RichiestaAziendaVO getPdfAziendaNuova(
      long idRichiestaAzienda) throws SolmrException, Exception
  {
    return gaaFacade.getPdfAziendaNuova(idRichiestaAzienda);
  }
  
  public PlSqlCodeDescription ribaltaAziendaPlSql(long idRichiestaAzienda) 
      throws SolmrException, Exception
  {
    return gaaFacade.ribaltaAziendaPlSql(idRichiestaAzienda);
  }
  
  public boolean isPartitaIvaPresente(String partitaIva, long[] arrTipoRichiesta) 
      throws SolmrException, Exception
  {
    return gaaFacade.isPartitaIvaPresente(partitaIva, arrTipoRichiesta);
  }
  
  public void updateFlagDichiarazioneAllegati(long idRichiestaAzienda, 
      String flagDichiarazioneAllegati) throws SolmrException, Exception
  {
    gaaFacade.updateFlagDichiarazioneAllegati(idRichiestaAzienda, flagDichiarazioneAllegati);
  }
  
  public Vector<MotivoRichiestaVO> getListMotivoRichiesta(int idTipoRichiesta)
      throws SolmrException, Exception
  {
    return gaaFacade.getListMotivoRichiesta(idTipoRichiesta);
  }
  
  public AziendaNuovaVO getRichAzByIdAzienda(long idAzienda, long idTipoRichiesta)
      throws SolmrException, Exception
  {
    return gaaFacade.getRichAzByIdAzienda(idAzienda, idTipoRichiesta);
  }
  
  public AziendaNuovaVO getRichAzByIdAziendaConValida(
      long idAzienda, long idTipoRichiesta) throws SolmrException, Exception
  {
    return gaaFacade.getRichAzByIdAziendaConValida(idAzienda, idTipoRichiesta);
  }
  
  public void updateRichiestaAzienda(RichiestaAziendaVO richiestaAziendaVO) 
      throws SolmrException, Exception
  {
    gaaFacade.updateRichiestaAzienda(richiestaAziendaVO);
  }
  
  public Vector<SoggettoAziendaNuovaVO> getSoggettiAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return gaaFacade.getSoggettiAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public SoggettoAziendaNuovaVO getRappLegaleNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return gaaFacade.getRappLegaleNuovaIscrizione(idAziendaNuova);
  }
  
  public void aggiornaSoggettiAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<SoggettoAziendaNuovaVO> vSoggettoAziendaNuova)     
        throws SolmrException, Exception
  {
    gaaFacade.aggiornaSoggettiAziendaNuovaIscrizione(aziendaNuovaVO, 
        idUtenteAggiornamento, vSoggettoAziendaNuova);
  }
  
  public void aggiornaMacchineIrrAziendaNuova(AziendaNuovaVO aziendaNuovaVO, long idUtenteAggiornamento,
      Vector<MacchinaAziendaNuovaVO> vMacchineNuovaRichiesta)     
        throws SolmrException, Exception
  {
    gaaFacade.aggiornaMacchineIrrAziendaNuova(aziendaNuovaVO, 
        idUtenteAggiornamento, vMacchineNuovaRichiesta);
  }
  
  public void aggiornaAzAssociateCaaAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception
  {
    gaaFacade.aggiornaAzAssociateCaaAziendaNuovaIscrizione(aziendaNuovaVO, 
        idUtenteAggiornamento, vAssAziendaNuova);
  }
  
  public void aggiornaAzAssociateCaaRichiestaVariazione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception
  {
    gaaFacade.aggiornaAzAssociateCaaRichiestaVariazione(
        aziendaNuovaVO, idUtenteAggiornamento, vAssAziendaNuova);
  }
  
  public void aggiornaAzAssociateRichiestaVariazione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception
  {
    gaaFacade.aggiornaAzAssociateRichiestaVariazione(
        aziendaNuovaVO, idUtenteAggiornamento, vAssAziendaNuova);
  }
  
  public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return gaaFacade.getAziendeAssociateCaaAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public HashMap<String, AzAssAziendaNuovaVO> getAziendeAssociateCaaAziendaRichVariazione(
      long idRichiestaAzienda) throws SolmrException, Exception
  {
    return gaaFacade.getAziendeAssociateCaaAziendaRichVariazione(idRichiestaAzienda);
  }
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return gaaFacade.getAziendeAssociateAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateAziendaRichVariazione(
      long idRichiestaAzienda) throws SolmrException, Exception
  {
    return gaaFacade.getAziendeAssociateAziendaRichVariazione(idRichiestaAzienda);
  }
  
  public void aggiornaAzAssociateAziendaNuovaIscrizione(AziendaNuovaVO aziendaNuovaVO, 
      long idUtenteAggiornamento, Vector<AzAssAziendaNuovaVO> vAssAziendaNuova)     
        throws SolmrException, Exception
  {
    gaaFacade.aggiornaAzAssociateAziendaNuovaIscrizione(aziendaNuovaVO, 
        idUtenteAggiornamento, vAssAziendaNuova);
  }
  
  public Vector<AzAssAziendaNuovaVO> getAziendeAssociateCaaStampaAziendaNuovaIscrizione(
      long idAziendaNuova) throws SolmrException, Exception
  {
    return gaaFacade.getAziendeAssociateCaaStampaAziendaNuovaIscrizione(idAziendaNuova);
  }
  
  public void caricaMacchineNuovaRichiesta(long idAzienda, long idRichiestaAzienda) 
      throws SolmrException, Exception
  {
    gaaFacade.caricaMacchineNuovaRichiesta(idAzienda, idRichiestaAzienda);
  }
  
  public void caricaAziendeAssociateRichiesta(long idAzienda, long idRichiestaAzienda, String flagSoloAggiunta) 
      throws SolmrException, Exception
  {
    gaaFacade.caricaAziendeAssociateRichiesta(idAzienda, idRichiestaAzienda, flagSoloAggiunta);
  }
  
  public void caricaAziendeAssociateCaaRichiesta(long idAzienda, long idRichiestaAzienda)     
      throws SolmrException, Exception
  {
    gaaFacade.caricaAziendeAssociateCaaRichiesta(idAzienda, idRichiestaAzienda);
  }
  
  public void ribaltaMacchineNuovaRichiesta(long idRichiestaAzienda, long idUtenteAggiornamento)     
      throws SolmrException, Exception
  {
    gaaFacade.ribaltaMacchineNuovaRichiesta(idRichiestaAzienda, idUtenteAggiornamento);
  }
      
  public Vector<MacchinaAziendaNuovaVO> getMacchineAzNuova(long idRichiestaAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.getMacchineAzNuova(idRichiestaAzienda);
  }
  
  public boolean isUtenteAbilitatoPresaInCarico(long idTipoRichiesta, String codiceRuolo) 
      throws SolmrException, Exception
  {
    return gaaFacade.isUtenteAbilitatoPresaInCarico(idTipoRichiesta, codiceRuolo);
  }
  
  /*************************************************************************/
  /********************** NUOVA ISCRIZIONE END ***********************************/
  /*************************************************************************/
  
  /*************************************************************************/
  /********************** MESSAGGISTICA START ***********************************/
  /*************************************************************************/
  
  public void confermaLetturaMessaggio(long idElencoMessaggi, String codiceFiscale) 
      throws SolmrException, Exception
  {
    gaaFacade.confermaLetturaMessaggio(idElencoMessaggi, codiceFiscale);
  }
  
  public byte[] getAllegato(long idAllegato) 
      throws SolmrException, Exception
  {
    return gaaFacade.getAllegato(idAllegato);
  }
  
  public ListaMessaggi getListaMessaggi(int idProcedimento, String codiceRuolo, 
      String codiceFiscale, int tipoMessaggio, Boolean letto, Boolean obbligatorio, Boolean visibile ) 
    throws SolmrException, LogoutException, Exception
  {
    return gaaFacade.getListaMessaggi(idProcedimento, codiceRuolo, codiceFiscale, tipoMessaggio, letto, obbligatorio, visibile);
  }
  
  public DettagliMessaggio getDettagliMessaggio(long idElencoMessaggi, String codiceFiscale) 
    throws SolmrException, Exception
  {
    return gaaFacade.getDettagliMessaggio(idElencoMessaggi, codiceFiscale);
  }
  
  /*************************************************************************/
  /********************** MESSAGGISTICA END ***********************************/
  /*************************************************************************/
  
  /*************************************************************************/
  /********************** WSBRIDGE START ***********************************/
  /*************************************************************************/
  
  public Hashtable<BigDecimal, SianAllevamentiVO> leggiAnagraficaAllevamenti(String cuaa, String dataRichiesta)
      throws SolmrException, Exception
  {
    return gaaFacade.leggiAnagraficaAllevamenti(cuaa, dataRichiesta);
  }
  
  public Hashtable<BigDecimal, SianAllevamentiVO> leggiAnagraficaAllevamentiNoProfile(String cuaa, String dataRichiesta)
      throws SolmrException, Exception
  {
    return gaaFacade.leggiAnagraficaAllevamentiNoProfile(cuaa, dataRichiesta);
  }
  
  public ResponseWsBridgeVO serviceConsistenzaStatisticaMediaAllevamento(
      String cuaa)  throws SolmrException, Exception
  {
    return gaaFacade.serviceConsistenzaStatisticaMediaAllevamento(cuaa);
  }
  
  public ResponseWsBridgeVO serviceConsistenzaUbaCensimOvini(
      String cuaa)  throws SolmrException, Exception
  {
    return gaaFacade.serviceConsistenzaUbaCensimOvini(cuaa);
  }
  
  public void serviceWsBridgeAggiornaDatiBDN(String cuaa) 
      throws SolmrException, Exception
  {
    gaaFacade.serviceWsBridgeAggiornaDatiBDN(cuaa);
  }
  
  /*************************************************************************/
  /********************** WSBRIDGE END ***********************************/
  /*************************************************************************/
  
  
  /*************************************************************************/
  /********************** AgriWell START ***********************************/
  /*************************************************************************/
  
  public void insertFileAllegatoAgriWell(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException, Exception
  {
    gaaFacade.insertFileAllegatoAgriWell(allegatoDocumentoVO);
  }
  
  public Long insertFileValidazioneAgriWell(AnagAziendaVO anagAziendaVO,
      Long idDichiarazioneConsistenza, byte[] fileStampa,
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception
  {
    return gaaFacade.insertFileValidazioneAgriWell(anagAziendaVO, 
        idDichiarazioneConsistenza, fileStampa, ruoloUtenza);
  }
  
  public Long insertFileValidazioneRigeneraAgriWell(AnagAziendaVO anagAziendaVO,
      Long idDichiarazioneConsistenza, byte[] fileStampa,
      RuoloUtenza ruoloUtenza, int idTipoAllegato) throws SolmrException, Exception
  {
    return gaaFacade.insertFileValidazioneRigeneraAgriWell(anagAziendaVO, 
        idDichiarazioneConsistenza, fileStampa, ruoloUtenza, idTipoAllegato);
  }
  
  public Long insertFileValidazioneAllegatiRigeneraAgriWell(AnagAziendaVO anagAziendaVO,
      Long idDichiarazioneConsistenza, byte[] fileStampa,
      RuoloUtenza ruoloUtenza, int idTipoAllegato) throws SolmrException, Exception
  {
    return gaaFacade.insertFileValidazioneAllegatiRigeneraAgriWell(
        anagAziendaVO, idDichiarazioneConsistenza, fileStampa, ruoloUtenza, idTipoAllegato);
  }
  
  public Long insertFileValidazioneAllegatiAgriWell(AnagAziendaVO anagAziendaVO,
      Long idDichiarazioneConsistenza, byte[] fileStampa,
      RuoloUtenza ruoloUtenza, int tipoAllegato) throws SolmrException, Exception
  {
    return gaaFacade.insertFileValidazioneAllegatiAgriWell(anagAziendaVO, 
        idDichiarazioneConsistenza, fileStampa, ruoloUtenza, tipoAllegato);
  }
  
  public void insertFileRichiestaAgriWell(AziendaNuovaVO aziendaNuovaVO, byte[] fileStampa, 
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception
  {
    gaaFacade.insertFileRichiestaAgriWell(aziendaNuovaVO, fileStampa, ruoloUtenza);
  }
  
  public void insertFileRichiestaValidazioneAgriWell(AziendaNuovaVO aziendaNuovaVO, byte[] fileStampa, 
      RuoloUtenza ruoloUtenza) throws SolmrException, Exception
  {
    gaaFacade.insertFileRichiestaValidazioneAgriWell(aziendaNuovaVO, fileStampa, ruoloUtenza);
  }
  
  public Long insertFileRichiestaVariazioneAgriWell(
      AziendaNuovaVO aziendaNuovaVO, byte[] fileStampa, RuoloUtenza ruoloUtenza)
      throws SolmrException, Exception
  {
    return gaaFacade.insertFileRichiestaVariazioneAgriWell(aziendaNuovaVO, fileStampa, ruoloUtenza);
  }
  
  public AgriWellEsitoVO agriwellServiceLeggiDoquiAgri(long idDocumentoIndex)
      throws SolmrException, Exception
  {
    return gaaFacade.agriwellServiceLeggiDoquiAgri(idDocumentoIndex);
  }
  
  public AgriWellEsitoFolderVO agriwellFindFolderByPadreProcedimentoRuolo(int idProcedimento,
      String codRuoloUtente, Long idFolderMadre, boolean noEmptyFolder, Long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.agriwellFindFolderByPadreProcedimentoRuolo(idProcedimento, 
        codRuoloUtente, idFolderMadre, noEmptyFolder, idAzienda);
  }
  
  public AgriWellEsitoIdDocVO agriwellFindListaDocumentiByIdFolder(long idFolder, int idProcedimento,
      String codRuoloUtente, Long idAzienda)
    throws SolmrException, Exception
  {
    return gaaFacade.agriwellFindListaDocumentiByIdFolder(idFolder, idProcedimento, codRuoloUtente, idAzienda);
  }
  
  public AgriWellEsitoDocumentoVO agriwellFindDocumentoByIdRange(long[] idDoc)
    throws SolmrException, Exception
  {
    return gaaFacade.agriwellFindDocumentoByIdRange(idDoc);
  }
  
  public void aggiornaValidazioneAgriWell(AllegatoDichiarazioneVO allegatoDichiarazioneVO,
      Long idTipoFirma, String note, RuoloUtenza ruoloUtenza) throws SolmrException,
      Exception
  {
    gaaFacade.aggiornaValidazioneAgriWell(allegatoDichiarazioneVO, 
        idTipoFirma, note, ruoloUtenza);
  }
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
      long idDichiarazioneConsistenza, long idTipoAllegato) 
    throws SolmrException, Exception
  {
    return gaaFacade.getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
        idDichiarazioneConsistenza, idTipoAllegato);
  }
  
  /*************************************************************************/
  /********************** AgriWell END ***********************************/
  /*************************************************************************/
  
  /*************************************************************************/
  /********************** UmaServGaa START ***********************************/
  /*************************************************************************/
  
  public DittaUmaVO[] umaservGetAssegnazioniByIdAzienda(
      long idAzienda, String[] arrCodiceStatoAnag)  throws SolmrException, Exception
  {
    return gaaFacade.umaservGetAssegnazioniByIdAzienda(idAzienda, arrCodiceStatoAnag);
  }
  
  public AssegnazioneVO[] umaservGetDettAssegnazioneByRangeIdDomAss(
      long[] arrIdDomandaAssegnazione)  throws SolmrException, Exception
  {
    return gaaFacade.umaservGetDettAssegnazioneByRangeIdDomAss(arrIdDomandaAssegnazione);
  }
  
  public Vector<MacchinaVO> serviceGetElencoMacchineByIdAzienda(Long idAzienda,
      Boolean storico, Long idGenereMacchina)  throws SolmrException, Exception
  {
    return gaaFacade.serviceGetElencoMacchineByIdAzienda(
        idAzienda, storico, idGenereMacchina);
  }
  
  public Vector<Long> serviceGetElencoAziendeUtilizzatrici(Long idMacchina)
      throws SolmrException, Exception
  {
    return gaaFacade.serviceGetElencoAziendeUtilizzatrici(idMacchina);
  }
  
  public UtilizzoVO serviceGetUtilizzoByIdMacchinaAndIdAzienda(
      Long idMacchina, Long idAzienda) throws SolmrException, Exception
  {
    return gaaFacade.serviceGetUtilizzoByIdMacchinaAndIdAzienda(idMacchina, idAzienda);
  }
  
  /*************************************************************************/
  /********************** UmaServGaa END ***********************************/
  /*************************************************************************/
  
  
  /*************************************************************************/
  /********************** ModolServGaa START ***********************************/
  /*************************************************************************/
  
  public byte[] callModol(byte[] xmlInput, AttributiModuloVO attributiModuloVO)  
      throws SolmrException, Exception
  {
	throw new Exception("Non è possibile richiamare Modol, bisogna migrare a JasperReport! ");  
    //return gaaFacade.callModol(xmlInput, attributiModuloVO);
  }
  
  /*************************************************************************/
  /********************** ModolServGaa END ***********************************/
  /*************************************************************************/
  
  
  /*************************************************************************/
  /********************** MacchineAgricoleGaa START ***********************************/
  /*************************************************************************/
  
  public long[] ricercaIdPossessoMacchina(FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO)  
      throws SolmrException, Exception
  {
    return gaaFacade.ricercaIdPossessoMacchina(filtriRicercaMacchineAgricoleVO);
  }
  
  public long[] ricercaIdPossessoMacchinaImport(
      FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO) throws SolmrException, Exception
  {
    return gaaFacade.ricercaIdPossessoMacchinaImport(filtriRicercaMacchineAgricoleVO);
  }
      
  public RigaRicercaMacchineAgricoleVO[] getRigheRicercaMacchineAgricoleById(
      long ids[]) throws SolmrException, Exception
  {
    return gaaFacade.getRigheRicercaMacchineAgricoleById(ids);
  }
  
  public Vector<PossessoMacchinaVO> getElencoMacchineAgricoleForStampa(
      long idAzienda, Date dataInserimentoValidazione) throws SolmrException, Exception
  {
    return gaaFacade.getElencoMacchineAgricoleForStampa(idAzienda, dataInserimentoValidazione);
  }
  
  public void popolaTabelleMacchineAgricoleConServizio(
      long idAzienda) throws SolmrException, Exception
  {
    gaaFacade.popolaTabelleMacchineAgricoleConServizio(idAzienda);
  }
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchina() 
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoGenereMacchina();
  }
  
  public TipoGenereMacchinaGaaVO getTipoGenereMacchinaByPrimaryKey(long idGenereMacchina) 
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoGenereMacchinaByPrimaryKey(idGenereMacchina);
  }
  
  public PossessoMacchinaVO getPosessoMacchinaFromId(long idPossessoMacchina)
      throws SolmrException, Exception
  {
    return gaaFacade.getPosessoMacchinaFromId(idPossessoMacchina);
  }
  
  public Vector<PossessoMacchinaVO> getElencoDitteUtilizzatrici(
      long idMacchina, Date dataScarico) throws SolmrException, Exception
  {
    return gaaFacade.getElencoDitteUtilizzatrici(idMacchina, dataScarico);
  }
  
  public Vector<PossessoMacchinaVO> getElencoPossessoDitteUtilizzatrici(
      long idMacchina, long idAzienda) throws SolmrException, Exception
  {
    return gaaFacade.getElencoPossessoDitteUtilizzatrici(idMacchina, idAzienda);
  }
  
  public Vector<TipoMacchinaGaaVO> getElencoTipoMacchina(String flaIrroratrice) 
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoMacchina(flaIrroratrice);
  }
  
  public Vector<TipoGenereMacchinaGaaVO> getElencoTipoGenereMacchinaFromRuolo(
      long idTipoMacchina, String codiceRuolo) throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoGenereMacchinaFromRuolo(idTipoMacchina, codiceRuolo);
  }
  
  public Vector<TipoCategoriaGaaVO> getElencoTipoCategoria(long idGenereMacchina) 
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoCategoria(idGenereMacchina);
  }
  
  public Vector<CodeDescription> getElencoTipoMarca()
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoMarca();
  }
  
  public Vector<CodeDescription> getElencoTipoMarcaByIdGenere(long idGenereMacchina) 
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoMarcaByIdGenere(idGenereMacchina);
  }
    
  public Vector<TipoFormaPossessoGaaVO> getElencoTipoFormaPossesso()
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoFormaPossesso();
  }
  
  public boolean existsMotoreAgricolo(Long idMarca, String modello,
      Integer annoCostruzione, String matricolaTelaio) throws SolmrException, Exception
  {
    return gaaFacade.existsMotoreAgricolo(idMarca, modello, annoCostruzione, matricolaTelaio);
  }
  
  public void inserisciMacchinaAgricola(PossessoMacchinaVO possessoMacchinaVO) 
      throws SolmrException, Exception
  {
    gaaFacade.inserisciMacchinaAgricola(possessoMacchinaVO);
  }
  
  public void modificaMacchinaAgricola(long idPossessoMacchina, PossessoMacchinaVO possessoMacchinaVO, boolean flagModUte) 
      throws SolmrException, Exception
  {
    gaaFacade.modificaMacchinaAgricola(idPossessoMacchina, possessoMacchinaVO, flagModUte);
  }
  
  public void importaMacchinaAgricola(long idPossessoMacchina, PossessoMacchinaVO possessoMacchinaVO) 
      throws SolmrException, Exception
  {
    gaaFacade.importaMacchinaAgricola(idPossessoMacchina, possessoMacchinaVO);
  }
  
  public boolean isMacchinaModificabile(long idPossessoMacchina, String codiceRuolo)
      throws SolmrException, Exception
  {
    return gaaFacade.isMacchinaModificabile(idPossessoMacchina, codiceRuolo);
  }
  
  public boolean isMacchinaPossMultiplo(long idMacchina)
      throws SolmrException, Exception
  {
    return gaaFacade.isMacchinaPossMultiplo(idMacchina);
  }
  
  public Vector<CodeDescription> getElencoTipoScarico()
      throws SolmrException, Exception
  {
    return gaaFacade.getElencoTipoScarico();
  }
  
  public boolean isMacchinaGiaPossesso(long idMacchina, long idAzienda)
      throws SolmrException, Exception
  {
    return gaaFacade.isMacchinaGiaPossesso(idMacchina, idAzienda);
  }
  
  public BigDecimal percMacchinaGiaInPossesso(long idMacchina)
      throws SolmrException, Exception
  {
    return gaaFacade.percMacchinaGiaInPossesso(idMacchina);
  }
  
  public TipoCategoriaGaaVO getTipoCategoriaFromPK(long idCategoria) 
      throws SolmrException, Exception
  {
    return gaaFacade.getTipoCategoriaFromPK(idCategoria);
  }
  
  /*************************************************************************/
  /********************** MacchineAgricoleGaa END ***********************************/
  /*************************************************************************/
  
  
  /*************************************************************************/
  /********************** Marcatemporale BEGIN ***********************************/
  /*************************************************************************/
  
  public byte[] getMarcaTemporale(byte[] fileToMark) 
	      throws SolmrException, Exception
	  {
	    return gaaFacade.getMarcaTemporale(fileToMark);
	  }
	  
  /*************************************************************************/
  /********************** Marcatemporale END ***********************************/
  /*************************************************************************/
	
  
  /*
   * INIZIO SIANFA
   */
  public SianEsito getAggiornamentiFascicolo(String cuaa) throws SolmrException, Exception{
	  SolmrLogger.info(this, "BEGIN getAggiornamentiFascicolo");
	  SianEsito esitoAggFascicolo =  gaaFacade.getAggiornamentiFascicolo(cuaa);
	  SolmrLogger.info(this, "END getAggiornamentiFascicolo");
	  return esitoAggFascicolo;
  }
  
  public Boolean aggiornaFascicoloAziendale(String cuaa, Long idUtente) throws SolmrException, Exception{
	  SolmrLogger.info(this, "BEGIN aggiornaFascicoloAziendale");
	  Boolean aggiornaFasc =  gaaFacade.aggiornaFascicoloAziendale(cuaa, idUtente);
	  SolmrLogger.info(this, "END aggiornaFascicoloAziendale");
	  return aggiornaFasc;  
  }
  
  /*
   * FINE SIANFA
   */
  
}
