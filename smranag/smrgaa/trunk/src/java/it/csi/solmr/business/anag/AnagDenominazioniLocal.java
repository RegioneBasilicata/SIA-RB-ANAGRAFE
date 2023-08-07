package it.csi.solmr.business.anag;

import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.anag.TipoCategoriaNotificaVO;
import it.csi.solmr.dto.anag.TipoCessazioneVO;
import it.csi.solmr.dto.anag.TipoTipologiaAziendaVO;
import it.csi.solmr.dto.anag.terreni.TipoEventoVO;
import it.csi.solmr.dto.anag.terreni.TipoImpiantoVO;
import it.csi.solmr.dto.anag.terreni.TipoIrrigazioneVO;
import it.csi.solmr.dto.anag.terreni.TipoPotenzialitaIrriguaVO;
import it.csi.solmr.dto.anag.terreni.TipoRotazioneColturaleVO;
import it.csi.solmr.dto.anag.terreni.TipoTerrazzamentoVO;
import it.csi.solmr.dto.comune.IntermediarioVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.SolmrException;

import java.util.Date;
import java.util.Vector;

import javax.ejb.Local;

@Local
public interface AnagDenominazioniLocal
{
  public Vector<CodeDescription> getTipiAreaA() throws Exception,
      NotFoundException;

  public Vector<CodeDescription> getTipiAreaB() throws Exception,
      NotFoundException;

  public Vector<CodeDescription> getTipiAreaC() throws Exception,
      NotFoundException;

  public Vector<CodeDescription> getTipiAreaD() throws Exception,
      NotFoundException;

  public Vector<CodeDescription> getTipiZonaAltimetrica()
      throws Exception, NotFoundException;

  public Vector<CodeDescription> getTipiCasoParticolare()
      throws Exception, NotFoundException;

  public Vector<CodeDescription> getProcedimenti() throws Exception,
      NotFoundException;

  public Vector<CodeDescription> getTipiAttivitaOTE() throws Exception,
      NotFoundException;

  public Vector<CodeDescription> getTipiAttivitaATECO(String code,
      String description) throws Exception;

  public Vector<CodeDescription> getTipiAttivitaOTE(String code,
      String description) throws Exception, NotFoundException;

  public Vector<CodeDescription> getTipiAzienda() throws Exception,
      NotFoundException;

  public Vector<CodeDescription> getTipiIntermediario() throws Exception,
      NotFoundException;

  public Vector<CodeDescription> getTipiIntermediarioUmaProv()
      throws Exception, NotFoundException;

  public Vector<CodeDescription> getTipiFabbricato() throws Exception,
      NotFoundException;

  public Vector<CodeDescription> getTipiFormaGiuridica(Long idTipologiaAzienda)
      throws Exception, NotFoundException;
  
  public Vector<CodeDescription> getCodeDescriptionsFormaGiuridica() 
      throws Exception, NotFoundException;

  public TipoTipologiaAziendaVO getTipologiaAzienda(Long idTipologiaAzienda)
      throws Exception, NotFoundException;

  public Vector<TipoTipologiaAziendaVO> getTipiTipologiaAzienda(
      Boolean flagControlliUnivocita, Boolean flagAziendaProvvisoria)
      throws Exception, NotFoundException;

  public Vector<CodeDescription> getTipiFormaGiuridica()
      throws Exception, NotFoundException;

  public Vector<CodeDescription> getTipiFormaGiuridicaNonIndividuale()
      throws Exception, NotFoundException;

  public Vector<CodeDescription> getTipiRuoloNonTitolare()
      throws Exception, NotFoundException;

  public Vector<CodeDescription> getTipiUtilizzo() throws Exception,
      NotFoundException;

  public Vector<ProvinciaVO> getProvinceByRegione(String idRegione)
      throws Exception;
  
  public Vector<ProvinciaVO> getProvince()
      throws Exception;

  public Vector<ComuneVO> getComuniLikeByRegione(String idRegione, String like)
      throws Exception, NotFoundException, SolmrException;

  public Vector<ComuneVO> getComuniLikeByProvAndCom(String provincia,
      String comune) throws DataAccessException, NotFoundException,
      DataControlException, Exception;

  public Vector<ComuneVO> getComuniByDescCom(String comune)
      throws DataAccessException, NotFoundException, DataControlException,
      Exception;

  public Vector<ComuneVO> getComuniNonEstintiLikeByProvAndCom(String provincia,
      String comune, String flagEstero) throws DataAccessException,
      NotFoundException, DataControlException, Exception;

  public String getProcedimento(Integer code) throws Exception;

  public CodeDescription getTipoAttivitaATECO(Integer code)
      throws Exception;

  public CodeDescription getTipoAttivitaOTE(Integer code)
      throws Exception;

  public String getTipoAzienda(Integer code) throws Exception;

  public String getTipoCasoParticolare(Integer code) throws Exception;

  public String getTipoIntermediario(Integer code) throws Exception;

  public String getTipoFabbricato(Integer code) throws Exception;

  public String getTipoFormaGiuridica(Integer code) throws Exception;

  public String getTipoUtilizzo(Integer code) throws Exception;

  public String getTipoZonaAltimetrica(Integer code) throws Exception;

  public Vector<ComuneVO> ricercaStatoEstero(String statoEstero,
      String estinto, String flagCatastoAttivo) throws Exception;

  public String ricercaCodiceComune(String descrizioneComune,
      String siglaProvincia) throws DataAccessException, NotFoundException,
      DataControlException, Exception;

  public Long ricercaIdAttivitaOTE(String codice, String descrizione)
      throws Exception, SolmrException;

  public Long ricercaIdAttivitaOTE(String codice, String descrizione,
      boolean forPopup) throws Exception, SolmrException;

  public Long ricercaIdAttivitaATECO(String codice, String descrizione)
      throws Exception, SolmrException;

  public Long ricercaIdAttivitaATECO(String codice, String descrizione,
      boolean forPopup) throws Exception, SolmrException;

  public String ricercaCodiceFiscaleComune(String descrizioneComune,
      String siglaProvincia) throws Exception, DataControlException;

  public String getDescriptionFromCode(String tableName, Integer code)
      throws DataAccessException, Exception;

  public Vector<StringcodeDescription> getProvincePiemonte()
      throws Exception, SolmrException;

  public ProvinciaVO getProvinciaByCriterio(String criterio)
      throws Exception, SolmrException;

  public String ricercaCodiceComuneNonEstinto(String descrizioneComune,
      String siglaProvincia) throws Exception, SolmrException;
  
  public String ricercaCodiceComuneFlagEstinto(String descrizioneComune,
      String siglaProvincia, String estinto)
          throws Exception, SolmrException;

  public Vector<CodeDescription> getTipiTitoloPossessoExceptProprieta()
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTitoliStudio() throws SolmrException,
      Exception;

  public Vector<CodeDescription> getPrefissiCellulare() throws SolmrException,
      Exception;

  public Vector<CodeDescription> getTipiMotivoDichiarazione()
      throws Exception;

  public Vector<CodeDescription> getIndirizzoStudioByTitolo(Long idTitoloStudio)
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTipiTipologiaFabbricato()
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTipiFormaFabbricato(
      Long idTipologiaFabbricato) throws SolmrException, Exception;

  public Vector<CodeDescription> getTipiColturaSerra(Long idTipologiaFabbricato)
      throws SolmrException, Exception;

  public String getUnitaMisuraByTipoFabbricato(Long idTipologiaFabbricato)
      throws SolmrException, Exception;

  public int getMesiRiscaldamentoBySerra(String tipologiaColturaSerra)
      throws SolmrException, Exception;

  public double getFattoreCubaturaByFormaFabbricato(String idFormaFabbricato)
      throws SolmrException, Exception;

  public Vector<CodeDescription> getElencoIndirizziUtilizzi()
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTipiRuoloNonTitolareAndNonSpecificato()
      throws Exception, SolmrException;

  public String getValoreFromParametroByIdCode(String codice)
      throws Exception, SolmrException;

  public Vector<CodeDescription> getTipiTipologiaNotifica()
      throws SolmrException, Exception;
  
  public Vector<CodeDescription> getTipologiaNotificaFromRuolo(RuoloUtenza ruoloUtenza) 
      throws SolmrException, Exception;
  
  public Vector<CodeDescription> getTipiTipologiaNotificaFromEntita(String tipoEntita)
      throws SolmrException, Exception;
  
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromRuolo(RuoloUtenza ruoloUtenza)
    throws SolmrException, Exception;
  
  public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromEntita(String tipoEntita)
      throws SolmrException, Exception;
  
  public Vector<CodeDescription> getCategoriaNotifica() throws SolmrException, Exception;

  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws Exception;
  
  public Vector<CodeDescription> getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(
      UtenteAbilitazioni utenteAbilitazioni) throws Exception;

  public Vector<CodeDescription> getTipiDocumento() throws Exception;
  
  public String getRegioneByProvincia(String siglaProvincia) throws Exception;

  public String getIstatProvinciaBySiglaProvincia(String siglaProvincia)
      throws Exception;

  public Vector<CodeDescription> getTipiTipologiaDocumento()
      throws Exception;

  public Vector<CodeDescription> getTipiTipologiaDocumento(boolean cessata)
      throws Exception;

  public Vector<DelegaVO> getMandatiByUtente(UtenteAbilitazioni utenteAbilitazioni,
      boolean forZona, java.util.Date dataDal, java.util.Date dataAl)
      throws SolmrException, Exception;

  public Vector<CodeDescription> getElencoCAAByUtente(UtenteAbilitazioni utenteAbilitazioni)
      throws Exception, SolmrException;

  public Vector<DelegaVO> getMandatiValidatiByUtente(UtenteAbilitazioni utenteAbilitazioni,
      boolean forZona, java.util.Date dataDal, java.util.Date dataAl)
      throws Exception, SolmrException;

  public IntermediarioVO getIntermediarioVOByIdUfficioZonaIntermediario(
      Long idUfficioZonaIntermediario) throws Exception;

  public IntermediarioVO findIntermediarioVOByPrimaryKey(Long idIntermediario)
      throws Exception;

  public TipoCessazioneVO[] getListTipoCessazione(String orderBy,
      boolean onlyActive) throws Exception;

  public boolean isRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws Exception;

  public PersonaFisicaVO getRuolosuAnagrafe(String codiceFiscale,
      long idAzienda, String codRuoloAAEP) throws Exception;

  public boolean getRuolosuAnagrafe(String codiceFiscale, long idAzienda)
      throws Exception;

  public String getIstatByDescComune(String descComune) throws Exception;

  //public IntermediarioVO getIntermediarioVOByCodiceFiscale(String codice_fiscale)
      //throws Exception;

  /* Manodopera */
  public Vector<CodeDescription> getTipoFormaConduzione()
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTipoAttivitaComplementari()
      throws SolmrException, Exception;

  public Vector<CodeDescription> getTipoClassiManodopera()
      throws SolmrException, Exception;

  /* Manodopera */

  // SIAN
  // Metodo per recuperare le decodifiche dei codici SIAN
  public String getDescriptionSIANFromCode(String tableName, String code)
      throws SolmrException, Exception;

  // SIAN

  // NUOVO TERRITORIALE
  public CodeDescription[] getListTipiTitoloPossesso(
      String orderBy) throws Exception;

  public CodeDescription[] getListTipoCasoParticolare(
      String orderBy) throws Exception;

  public CodeDescription[] getListTipoZonaAltimetrica(
      String orderBy) throws Exception;

  public CodeDescription[] getListTipoAreaA(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaB(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaC(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaD(String orderBy)
      throws Exception;
  
  public CodeDescription[] getListTipoAreaM(String orderBy) 
      throws Exception;
  
  public CodeDescription[] getValidListTipoAreaA(String orderBy)
    throws Exception;
  
  public CodeDescription[] getValidListTipoAreaB(String orderBy)
    throws Exception;
  
  public CodeDescription[] getValidListTipoAreaC(String orderBy)
    throws Exception;
  
  public CodeDescription[] getValidListTipoAreaD(String orderBy)
    throws Exception;

  public CodeDescription[] getListTipoAreaE(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaF(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaG(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoAreaH(String orderBy)
      throws Exception;
  
  public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaM(String orderBy) 
      throws Exception;

  public CodeDescription[] getListTipoAreaPSN(String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoFasciaFluviale(
      String orderBy) throws Exception;

  public CodeDescription[] getListTipoCausaleModParticella(
      String orderBy) throws Exception;

  public TipoImpiantoVO[] getListTipoImpianto(boolean onlyActive, String orderBy)
      throws Exception;

  public CodeDescription[] getListTipoIndirizzoUtilizzo(
      String colturaSecondaria, String orderBy, boolean onlyActive)
      throws Exception;

  public TipoIrrigazioneVO[] getListTipoIrrigazione(String orderBy,
      boolean onlyActive) throws Exception;

  public ComuneVO getComuneByParameters(String descComune,
      String siglaProvincia, String flagEstinto, String flagCatastoAttivo,
      String flagEstero) throws Exception;

  public Vector<ComuneVO> getComuniByParameters(String descComune,
      String siglaProvincia, String flagEstinto, String flagCatastoAttivo,
      String flagEstero, String[] orderBy) throws Exception,
      SolmrException;
  
  public Vector<ComuneVO> getComuniAttiviByIstatProvincia(String istatProvincia) 
      throws Exception, SolmrException;

  public Vector<TipoEventoVO> getListTipiEvento()
      throws Exception;
  
  public TipoPotenzialitaIrriguaVO[] getListTipoPotenzialitaIrrigua(String orderBy, 
      Date dataRiferiemento) throws Exception;
  
  public TipoTerrazzamentoVO[] getListTipoTerrazzamento(String orderBy,
      Date dataRiferiemento) throws Exception;
  
  public TipoRotazioneColturaleVO[] getListTipoRotazioneColturale(String orderBy,
      Date dataRiferiemento) throws Exception;
  
  // FINE NUOVO TERRITORIALE
}
