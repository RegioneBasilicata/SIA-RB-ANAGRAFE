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
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.SolmrErrors;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.CommonDAO;
import it.csi.solmr.integration.anag.TipoEventoDAO;
import it.csi.solmr.integration.anag.TipoUtilizzoDAO;
import it.csi.solmr.util.SolmrLogger;

import java.util.Date;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/AnagDenominazioni",mappedName="comp/env/solmr/anag/AnagDenominazioni")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class AnagDenominazioniBean implements AnagDenominazioniLocal {
	/**
	 *
	 */
	private static final long serialVersionUID = 5443485179457576022L;
	SessionContext sessionContext;
	private transient CommonDAO commonDAO;
	private transient TipoUtilizzoDAO tipoUtilizzoDAO;
	private transient TipoEventoDAO tipoEventoDAO;

	
	@Resource
	public void setSessionContext(SessionContext sessionContext) throws EJBException {
		this.sessionContext = sessionContext;
		initializeDAO();
	}

	private void initializeDAO() throws EJBException {
		try {
			commonDAO = new CommonDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
			tipoUtilizzoDAO = new TipoUtilizzoDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
			tipoEventoDAO = new TipoEventoDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
		} catch (ResourceAccessException ex) {
			SolmrLogger.fatal(this, ex.getMessage());
			throw new EJBException(ex);
		}
	}

	public Vector<CodeDescription> getTipiZonaAltimetrica() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_ZONA_ALTIMETRICA);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiAreaA() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptions((String)SolmrConstants.get("TAB_TIPO_AREA_A"));
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiAreaB() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptions((String)SolmrConstants.get("TAB_TIPO_AREA_B"));
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiAreaC() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptions((String)SolmrConstants.get("TAB_TIPO_AREA_C"));
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiAreaD() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptions((String)SolmrConstants.get("TAB_TIPO_AREA_D"));
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiCasoParticolare() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_CASO_PARTICOLARE);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getProcedimenti() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptions(SolmrConstants.TAB_PROCEDIMENTO);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiAttivitaOTE() throws Exception, NotFoundException {
		try {
			return this.getTipiAttivitaOTE(null, null);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiAttivitaATECO(String code, String description) throws Exception {
		try {
			return commonDAO.getTipiAttivitaATECO(code, description);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiAttivitaOTE(String code, String description) throws Exception, NotFoundException {
		try {
			return commonDAO.getAttivitaLike(code, description, SolmrConstants.TAB_TIPO_ATTIVITA_OTE);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiAzienda() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_AZIENDA);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiFabbricato() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_FABBRICATO);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiMotivoDichiarazione() throws Exception
  {
    try
    {
      return commonDAO.getMotiviDichiarazione();
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
	}

	public Vector<CodeDescription> getTipiFormaGiuridica(Long idTipologiaAzienda) throws Exception, NotFoundException
	{
		try
		{
			return commonDAO.getCodeDescriptionsFormaGiuridica(idTipologiaAzienda);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}
	
	public Vector<CodeDescription> getCodeDescriptionsFormaGiuridica() 
	    throws Exception, NotFoundException
  {
    try
    {
      return commonDAO.getCodeDescriptionsFormaGiuridica();
    } catch (DataAccessException ex) {
      throw new Exception(ex.getMessage());
    }
  }

	public TipoTipologiaAziendaVO getTipologiaAzienda(Long idTipologiaAzienda)
	throws Exception, NotFoundException
	{
		try
		{
			return commonDAO.getTipologiaAzienda(idTipologiaAzienda);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<TipoTipologiaAziendaVO> getTipiTipologiaAzienda(Boolean flagControlliUnivocita,
			Boolean flagAziendaProvvisoria)
	throws Exception, NotFoundException
	{
		try
		{
			return commonDAO.getTipiTipologiaAzienda(flagControlliUnivocita,flagAziendaProvvisoria);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiFormaGiuridica() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptionsFormaGiuridica(SolmrConstants.TAB_TIPO_FORMA_GIURIDICA,
					SolmrConstants.CD_DESCRIPTION);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiIntermediario() throws Exception, NotFoundException {
		try {
			return commonDAO.getTipiIntermediario();
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiIntermediarioUmaProv() throws Exception, NotFoundException {
		try {
			Vector<CodeDescription> result = commonDAO.getTipiIntermediario();
			CodeDescription cd = new CodeDescription();
			cd.setCode(new Integer(SolmrConstants.CODE_FITTIZIO_UFF_UMA_PROV));
			cd.setDescription(SolmrConstants.DESC_FITTIZIO_UFF_UMA_PROV);
			result.addElement(cd);
			return result;
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<CodeDescription> getTipiUtilizzo() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_UTILIZZO);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public String getProcedimento(Integer code) throws Exception {
		try {
			return commonDAO.getDescriptionFromCode(SolmrConstants.TAB_PROCEDIMENTO, code);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public CodeDescription getTipoAttivitaATECO(Integer code) throws Exception {
		try {
			return commonDAO.getAttivitaFromCode(SolmrConstants.TAB_TIPO_ATTIVITA_ATECO, code);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public CodeDescription getTipoAttivitaOTE(Integer code) throws Exception {
		try {
			return commonDAO.getAttivitaFromCode(SolmrConstants.TAB_TIPO_ATTIVITA_OTE, code);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public String getTipoAzienda(Integer code) throws Exception {
		try {
			return commonDAO.getDescriptionFromCode(SolmrConstants.TAB_TIPO_AZIENDA, code);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public String getTipoCasoParticolare(Integer code) throws Exception {
		try {
			return commonDAO.getDescriptionFromCode(SolmrConstants.TAB_TIPO_CASO_PARTICOLARE, code);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public String getTipoFabbricato(Integer code) throws Exception {
		try {
			return commonDAO.getDescriptionFromCode(SolmrConstants.TAB_TIPO_FABBRICATO, code);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public String getTipoFormaGiuridica(Integer code) throws Exception {
		try {
			return commonDAO.getDescriptionFromCode(SolmrConstants.TAB_TIPO_FORMA_GIURIDICA, code);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public String getTipoIntermediario(Integer code) throws Exception {
		try {
			return commonDAO.getDescriptionFromCode(SolmrConstants.TAB_TIPO_INTERMEDIARIO, code);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public String getTipoUtilizzo(Integer code) throws Exception {
		try {
			return commonDAO.getDescriptionFromCode(SolmrConstants.TAB_TIPO_UTILIZZO, code);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public String getTipoZonaAltimetrica(Integer code) throws Exception {
		try {
			return commonDAO.getDescriptionFromCode(SolmrConstants.TAB_TIPO_ZONA_ALTIMETRICA, code);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Vector<ProvinciaVO> getProvinceByRegione(String idRegione) throws Exception{
		try {
			return commonDAO.getProvinceByRegione(idRegione);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}
	
	public Vector<ProvinciaVO> getProvince() throws Exception{
    try {
      return commonDAO.getProvince();
    } catch (DataAccessException ex) {
      throw new Exception(ex.getMessage());
    }
  }

	public Vector<ComuneVO> getComuniLikeByRegione(String idRegione, String like) throws Exception, NotFoundException, SolmrException{
		if(like == null || like.equals(""))
			throw new SolmrException(SolmrErrors.EXC_DESCOM_VOID);
		try {
			return commonDAO.getComuniLikeByRegione(idRegione, like);
		} catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	// Metodo per recuperare l'elenco dei comuni a partire dalla provincia e dal comune
	// N.B:Non occorre che siano entrambi valorizzati solo perchè il javascript delle pagine nelle quali lo
	// utilizzo mi intercetta la possibile eccezione dovuta al fatto che sia la provincia che il comune possano
	// essere null.
	public Vector<ComuneVO> getComuniLikeByProvAndCom(String provincia, String comune) throws DataAccessException, NotFoundException, DataControlException, Exception {
		Vector<ComuneVO> elencoComuni = null;

		try {
			elencoComuni = commonDAO.getComuniLikeByProvAndCom(provincia,comune);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		return elencoComuni;
	}

  public Vector<ComuneVO> getComuniByDescCom(String comune) throws DataAccessException, NotFoundException, DataControlException, Exception 
  {
    Vector<ComuneVO> elencoComuni = null;

    try 
    {
      elencoComuni = commonDAO.getComuniByDescCom(comune);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
    return elencoComuni;
  }


	// Metodo per recuperare comuni e province non esti
	public Vector<ComuneVO> getComuniNonEstintiLikeByProvAndCom(String provincia, String comune, String flagEstero) throws DataAccessException, NotFoundException, DataControlException, Exception {
		Vector<ComuneVO> elencoComuni = null;

		try {
			elencoComuni = commonDAO.getComuniNonEstintiLikeByProvAndCom(provincia,comune, flagEstero);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		return elencoComuni;
	}

	/**
     * Metodo che mi restituisce l'elenco degli stati esteri in relazione
     * ai parametri
     * 
     * @param statoEstero
     * @param estinto
     * @param flagCatastoAttivo
     * @return java.util.Vector
     * @throws Exception
     */
    public Vector<ComuneVO> ricercaStatoEstero(String statoEstero, String estinto, String flagCatastoAttivo) throws Exception {
		try {
			return commonDAO.ricercaStatoEstero(statoEstero, estinto, flagCatastoAttivo);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	// Metodo per recuperare il codice istat a partire dalla descrizione del comune
	public String ricercaCodiceComune(String descrizioneComune, String siglaProvincia) throws DataAccessException, NotFoundException, DataControlException, Exception {

		String codiceIstat = null;
		try {
			codiceIstat = commonDAO.ricercaCodiceComune(descrizioneComune,siglaProvincia);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		return codiceIstat;
	}

	// Metodo per recuperare l'id dell'attività OTE partendo dal codice e dalla descrizione
	public Long ricercaIdAttivitaOTE(String codice, String descrizione) throws Exception, SolmrException {
		try {
			return commonDAO.ricercaIdAttivitaOTE(codice, descrizione, true);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	// Metodo per recuperare l'id dell'attività OTE partendo dal codice e dalla descrizione
	public Long ricercaIdAttivitaOTE(String codice, String descrizione, boolean forPopup) throws Exception, SolmrException {
		try {
			return commonDAO.ricercaIdAttivitaOTE(codice, descrizione, forPopup);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}


	// Metodo per recuperare l'id dell'attività ATECO partendo dal codice e dalla descrizione
	public Long ricercaIdAttivitaATECO(String codice, String descrizione) throws Exception, SolmrException {
		try {
			return commonDAO.ricercaIdAttivitaATECO(codice, descrizione, true);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	// Metodo per recuperare l'id dell'attività ATECO partendo dal codice e dalla descrizione
	public Long ricercaIdAttivitaATECO(String codice, String descrizione, boolean forPopup) throws Exception, SolmrException {
		try {
			return commonDAO.ricercaIdAttivitaATECO(codice, descrizione, forPopup);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}


	// Metodo per recuerare il codice fiscale del comune
	public String ricercaCodiceFiscaleComune(String descrizioneComune, String siglaProvincia) throws Exception, DataControlException {
		try {
			return commonDAO.ricercaCodiceFiscaleComune(descrizioneComune, siglaProvincia);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	// Metodo per recuperare una descrizione a partire dal codice
	public String getDescriptionFromCode(String tableName, Integer code) throws DataAccessException, Exception {
		try {
			return commonDAO.getDescriptionFromCode(tableName, code);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	// Metodo per recuperare le decodifiche dei codici SIAN
	public String getDescriptionSIANFromCode(String tableName, String code) throws SolmrException, Exception {
		try {
			return commonDAO.getDescriptionSIANFromCode(tableName, code);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	// Metodo per recuperare i tipi forma giuridica dell'azienda ad esclusione di "DITTA INDIVIDUALE"
	public Vector<CodeDescription> getTipiFormaGiuridicaNonIndividuale() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptionsExceptValue3(SolmrConstants.TAB_TIPO_FORMA_GIURIDICA,
					SolmrConstants.CD_DESCRIPTION,
					SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE,
					Integer.decode(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO));
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	// Metodo per recuperare i tipi ruolo ad esclusione di "TITOLARE/RAPPRESENTANTE LEGALE"
	public Vector<CodeDescription> getTipiRuoloNonTitolare() throws Exception, NotFoundException {
		try {
			return commonDAO.getCodeDescriptionsExceptValue(SolmrConstants.TAB_TIPO_RUOLO,
					SolmrConstants.CD_DESCRIPTION,
					new Integer(SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG));
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	// Metodo per recuperare i tipi ruolo ad esclusione di "TITOLARE/RAPPRESENTANTE LEGALE"
	public Vector<CodeDescription> getTipiRuoloNonTitolareAndNonSpecificato() throws Exception, SolmrException {
		try {
			return commonDAO.getCodeDescriptionsExceptValue2(SolmrConstants.TAB_TIPO_RUOLO,
					SolmrConstants.CD_DESCRIPTION,
					new Integer(SolmrConstants.TIPORUOLO_TITOL_RAPPR_LEG),
					new Integer(((Long)SolmrConstants.get("TIPORUOLO_NON_SPECIFICATO")).toString()));
		}
		catch (DataAccessException ex) {
			throw new Exception(ex.getMessage());
		}
		catch(NotFoundException ne) {
			throw new SolmrException(ne.getMessage());
		}
	}

	public Vector<StringcodeDescription> getProvincePiemonte() throws Exception, SolmrException {
		try {
			return commonDAO.getProvincePiemonte();
		}
		catch(DataAccessException dae) {
			throw new SolmrException(dae.getMessage());
		}
	}

	public ProvinciaVO getProvinciaByCriterio(String criterio) throws Exception, SolmrException {
		try {
			return commonDAO.getProvinciaByCriterio(criterio);
		}
		catch(DataAccessException dae) {
			throw new SolmrException(dae.getMessage());
		}
	}

	public String ricercaCodiceComuneNonEstinto(String descrizioneComune, String siglaProvincia) 
	    throws Exception, SolmrException {
		try {
			return commonDAO.ricercaCodiceComuneNonEstinto(descrizioneComune,siglaProvincia);
		}
		catch(DataAccessException dae) {
			throw new SolmrException(dae.getMessage());
		}
		catch(DataControlException dce) {
			throw new SolmrException(dce.getMessage());
		}
	}
	
	public String ricercaCodiceComuneFlagEstinto(String descrizioneComune,
      String siglaProvincia, String estinto)
          throws Exception, SolmrException 
  {
    try {
      return commonDAO.ricercaCodiceComuneFlagEstinto(descrizioneComune, siglaProvincia, estinto);
    }
    catch(DataAccessException dae) {
      throw new SolmrException(dae.getMessage());
    }
    catch(DataControlException dce) {
      throw new SolmrException(dce.getMessage());
    }
  }

	// Metodo per recuperare i tipiConduzione tranne "proprietà"
	public Vector<CodeDescription> getTipiTitoloPossessoExceptProprieta() throws SolmrException, Exception {
		Vector<CodeDescription> elencoTipiTitoloPossesso = null;
		try {
			elencoTipiTitoloPossesso = commonDAO.getCodeDescriptionsExceptValue(SolmrConstants.TAB_TIPO_TITOLO_POSSESSO,
					SolmrConstants.CD_DESCRIPTION,
					new Integer(((Long)SolmrConstants.get("ID_TITOLO_POSSESSO_PROPRIETA")).toString()));
		}
		catch(NotFoundException ne) {
			throw new SolmrException(ne.getMessage());
		}
		catch(DataAccessException dae) {
			throw new SolmrException(dae.getMessage());
		}
		return elencoTipiTitoloPossesso;
	}

	// Metodo per recuperare l'elenco dei titoli di studio
	public Vector<CodeDescription> getTitoliStudio() throws SolmrException, Exception {
		Vector<CodeDescription> elencoTitoliStudio = null;
		try {
			elencoTitoliStudio = commonDAO.getCodeDescriptions((String)SolmrConstants.get("TAB_TIPO_TITOLO_STUDIO"),
					(String)SolmrConstants.get("CD_CODE"));
		}
		catch(NotFoundException nfe) {
			throw new SolmrException(nfe.getMessage());
		}
		catch(DataAccessException dae) {
			throw new SolmrException(dae.getMessage());
		}
		return elencoTitoliStudio;
	}
	// Metodo per recuperare l'elenco dei prefissi per un cellulare
	public Vector<CodeDescription> getPrefissiCellulare()throws SolmrException, Exception {
		Vector<CodeDescription> elencoPrefissiCellulare = null;
		try{
			elencoPrefissiCellulare = commonDAO.getPrefissiCellulare();			
		}
		catch(NotFoundException nfe) {
			throw new SolmrException(nfe.getMessage());
		}
		catch(DataAccessException dae) {
			throw new SolmrException(dae.getMessage());
		}
		return elencoPrefissiCellulare;
	}

	// Metodo per recuperare l'indirizzo di studio in relazione al titolo selezionato
	public Vector<CodeDescription> getIndirizzoStudioByTitolo(Long idTitoloStudio) throws SolmrException, Exception {
		Vector<CodeDescription> elencoIndirizziStudio = null;
		try {
			elencoIndirizziStudio = commonDAO.getIndirizzoStudioByTitolo(idTitoloStudio);
		}
		catch(SolmrException se) {
			throw new SolmrException(se.getMessage());
		}
		return elencoIndirizziStudio;
	}
	// Metodo per recuperare i tipi di fabbricato
	public Vector<CodeDescription> getTipiTipologiaFabbricato() throws SolmrException, Exception
  { 
    Vector<CodeDescription> elencoTipiFabbricato = null;
    try
    {
      elencoTipiFabbricato = commonDAO.getTipologiaFabbricati();
    }
    catch(DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    return elencoTipiFabbricato;
	}

	// Metodo per recuperare le forme dei fabbricati
  public Vector<CodeDescription> getTipiFormaFabbricato(Long idTipologiaFabbricato) throws SolmrException, Exception
  {
    Vector<CodeDescription> elencoTipiFormaFabbricato = null;
    try
    {
      elencoTipiFormaFabbricato = commonDAO.getTipiFormaFabbricato(idTipologiaFabbricato);
    }
    catch(DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    return elencoTipiFormaFabbricato;
	}


  // Metodo per recuperare i tipi di cultura delle serre
  public Vector<CodeDescription> getTipiColturaSerra(Long idTipologiaFabbricato) throws SolmrException, Exception
  {
    Vector<CodeDescription> elencoTipiColturaSerra = null;
    try
    {
      elencoTipiColturaSerra = commonDAO.getTipiColturaSerra(idTipologiaFabbricato);
    }
    catch(DataAccessException dae)
    {
      throw new SolmrException(dae.getMessage());
    }
    return elencoTipiColturaSerra;
  }




	public String getUnitaMisuraByTipoFabbricato(Long idTipologiaFabbricato)
            throws SolmrException, Exception
        {
		String unitaMisura = null;
		try {
			unitaMisura = commonDAO.getUnitaMisuraByTipoFabbricato(idTipologiaFabbricato);
		}
		catch(SolmrException se) {
			throw new SolmrException();
		}
		return unitaMisura;
	}

        public int getMesiRiscaldamentoBySerra(String tipologiaColturaSerra)
          throws SolmrException, Exception
        {
             int numeroMesi=0;
             try {
                     numeroMesi = commonDAO.getMesiRiscaldamentoBySerra(tipologiaColturaSerra);
             }
             catch(SolmrException se) {
                     throw new SolmrException();
             }
             return numeroMesi;
         }


        // Metodo per recuperare il fattore di cubatura in relazione alla forma del fabbricato
        public double getFattoreCubaturaByFormaFabbricato(String idFormaFabbricato)
            throws SolmrException, Exception
        {
          double fattoreCubatura = 0;
          try
          {
            fattoreCubatura = commonDAO.getFattoreCubaturaByFormaFabbricato(idFormaFabbricato);
          }
          catch(SolmrException se)
          {
            throw new SolmrException();
          }
          return fattoreCubatura;
        }






	// Metodo per recuperare gli indirizzi degli utilizzi
	public Vector<CodeDescription> getElencoIndirizziUtilizzi() throws SolmrException, Exception {
		Vector<CodeDescription> elencoIndirizziUtilizzi = null;
		try {
			elencoIndirizziUtilizzi = commonDAO.getCodeDescriptions((String)SolmrConstants.get("TAB_TIPO_INDIRIZZO_UTILIZZO"),
					(String)SolmrConstants.get("CD_DESCRIPTION"));
		}
		catch(NotFoundException nfe) {
			throw new SolmrException(nfe.getMessage());
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		return elencoIndirizziUtilizzi;
	}

	// Metodo per recuperare il valore dalla tabella parametro: importante per la gestione dei terreni
	public String getValoreFromParametroByIdCode(String codice) throws Exception, SolmrException {
		String valore = null;
		try {
			valore = commonDAO.getValoreFromParametroByIdCode(codice);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		return valore;
	}

	// Metodo per recuperare i valori della tabella DB_TIPO_TIPOLOGIA_NOTIFICA
	public Vector<CodeDescription> getTipiTipologiaNotifica() throws SolmrException, Exception 
	{
		Vector<CodeDescription> elencoTipologiaNotifica = null;
		try 
		{
			elencoTipologiaNotifica = commonDAO.getTipiTipologiaNotifica();
		}
		catch(DataAccessException dae) 
		{
			throw new Exception(dae.getMessage());
		}
		return elencoTipologiaNotifica;
	}
	
	public Vector<CodeDescription> getTipologiaNotificaFromRuolo(RuoloUtenza ruoloUtenza) 
	    throws SolmrException, Exception 
  {
    Vector<CodeDescription> elencoTipologiaNotifica = null;
    try 
    {
      elencoTipologiaNotifica = commonDAO.getTipologiaNotificaFromRuolo(ruoloUtenza);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
    return elencoTipologiaNotifica;
  }
	
	public Vector<CodeDescription> getTipiTipologiaNotificaFromEntita(String tipoEntita)
	  throws SolmrException, Exception 
  {
    Vector<CodeDescription> elencoTipologiaNotifica = null;
    try 
    {
      elencoTipologiaNotifica = commonDAO.getTipiTipologiaNotificaFromEntita(tipoEntita);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
    return elencoTipologiaNotifica;
  }
	
	public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromRuolo(RuoloUtenza ruoloUtenza)
	  throws SolmrException, Exception 
	{
	  Vector<TipoCategoriaNotificaVO> elencoTipoCategoriaNotifica = null;
    try 
    {
      elencoTipoCategoriaNotifica = commonDAO.getTipiCategoriaNotificaFromRuolo(ruoloUtenza);
    }
    catch (DataAccessException dae) 
    {
      throw new SolmrException(dae.getMessage());
    }
    return elencoTipoCategoriaNotifica;
  }
	
	public Vector<TipoCategoriaNotificaVO> getTipiCategoriaNotificaFromEntita(String tipoEntita)
	  throws SolmrException, Exception 
  {
    Vector<TipoCategoriaNotificaVO> elencoTipoCategoriaNotifica = null;
    try 
    {
      elencoTipoCategoriaNotifica = commonDAO.getTipiCategoriaNotificaFromEntita(tipoEntita);
    }
    catch (DataAccessException dae) 
    {
      throw new SolmrException(dae.getMessage());
    }
    return elencoTipoCategoriaNotifica;
  }
	
	public Vector<CodeDescription> getCategoriaNotifica() throws SolmrException, Exception 
  {
    Vector<CodeDescription> vCategoriaNotifica = null;
    try 
    {
      vCategoriaNotifica = commonDAO.getCategoriaNotifica();
    }
    catch (DataAccessException dae) 
    {
      throw new SolmrException(dae.getMessage());
    }
    return vCategoriaNotifica;
  }

	/**
	 * Tipo Forma Conduzione
	 * @return Vector
	 * @throws SolmrException
	 * @throws Exception
	 */
	public Vector<CodeDescription> getTipoFormaConduzione() throws SolmrException, Exception {
		Vector<CodeDescription> elencoTipoFormaConduzione = null;
		try {
			elencoTipoFormaConduzione = commonDAO.getTipoFormaConduzione();
		}
		catch (DataAccessException dae) {
			throw new SolmrException(dae.getMessage());
		}
		return elencoTipoFormaConduzione;
	}

	/**
	 * Tipo Attivita Complementari
	 * @return Vector
	 * @throws SolmrException
	 * @throws Exception
	 */
	public Vector<CodeDescription> getTipoAttivitaComplementari() throws SolmrException,
	Exception {
		try {
			return commonDAO.getCodeDescriptions( (String) SolmrConstants.get(
					"TAB_TIPO_ATTIVITA_COMPLEMENTARI"));
		}
		catch (NotFoundException nfe) {
			throw new SolmrException(nfe.getMessage());
		}
		catch (DataAccessException dae) {
			throw new SolmrException(dae.getMessage());
		}
	}

	/**
	 * Tipo Classi Manodopera
	 * @return Vector
	 * @throws SolmrException
	 * @throws Exception
	 */
	public Vector<CodeDescription> getTipoClassiManodopera() throws SolmrException,
	Exception {
		Vector<CodeDescription> elencoTipoClassiManodopera = null;
		try {
			elencoTipoClassiManodopera = commonDAO.getTipoClassiManodopera();
		}
		catch (DataAccessException dae) {
			throw new SolmrException(dae.getMessage());
		}
		return elencoTipoClassiManodopera;
	}

	// Metodo per recuperare i tipi ufficio zona intermediario
	public Vector<CodeDescription> getElencoUfficiZonaIntermediarioByIdIntermediario(UtenteAbilitazioni utenteAbilitazioni) throws Exception {
		Vector<CodeDescription> elencoUfficiZonaIntermediario = null;
		try {
			elencoUfficiZonaIntermediario = commonDAO.getElencoUfficiZonaIntermediarioByIdIntermediario(utenteAbilitazioni);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		return elencoUfficiZonaIntermediario;
	}
	
	
	public Vector<CodeDescription> getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(
	    UtenteAbilitazioni utenteAbilitazioni) throws Exception {
    Vector<CodeDescription> elencoUfficiZonaIntermediario = null;
    try {
      elencoUfficiZonaIntermediario = commonDAO.getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(utenteAbilitazioni);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
    return elencoUfficiZonaIntermediario;
  }

	// Metodo per recuperare i tipi documento
	public Vector<CodeDescription> getTipiDocumento() throws Exception {
		Vector<CodeDescription> elencoTipiDocumento = null;
		try {
			elencoTipiDocumento = commonDAO.getCodeDescriptions((String)SolmrConstants.get("TAB_TIPO_DOCUMENTO"));
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch(NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
		return elencoTipiDocumento;
	}
	
	public String getRegioneByProvincia(String siglaProvincia) throws Exception  
	{
    try 
    {
      return commonDAO.getRegioneByProvincia(siglaProvincia);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
  }

	/**
	 * Metodo per recuperare l'istat della provincia a partire dalla sigla
	 * @param siglaProvincia String
	 * @return String
	 * @throws Exception
	 */
	public String getIstatProvinciaBySiglaProvincia(String siglaProvincia) throws Exception  {
		try {
			return commonDAO.getIstatProvinciaBySiglaProvincia(siglaProvincia);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo per recuperare l'elenco dei dati dalla tabella di decodifica DB_TIPO_TIPOLOGIA_DOCUMENTO
	 * @return CodeDescription
	 * @throws Exception
	 */
	public Vector<CodeDescription> getTipiTipologiaDocumento() throws Exception {
		try {
			return commonDAO.getCodeDescriptions((String)SolmrConstants.get("TAB_TIPO_TIPOLOGIA_DOCUMENTO"), (String)SolmrConstants.get("CD_DESCRIPTION"));
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch(NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}
	
	public Vector<CodeDescription> getTipiTipologiaDocumento(boolean cessata) throws Exception 
	{
    try 
    {
      return commonDAO.getTipiTipologiaDocumento(cessata);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
  }

	public boolean isRuolosuAnagrafe(String codiceFiscale, long idAzienda)
	throws Exception
	{
		try
		{
			return commonDAO.isRuolosuAnagrafe(codiceFiscale, idAzienda);
		}
		catch(DataAccessException dae)
		{
			throw new Exception(dae.getMessage());
		}
	}

	public PersonaFisicaVO getRuolosuAnagrafe(String codiceFiscale, long idAzienda,String codRuoloAAEP)
	throws Exception
	{
		try
		{
			return commonDAO.getRuolosuAnagrafe(codiceFiscale, idAzienda, codRuoloAAEP);
		}
		catch(DataAccessException dae)
		{
			throw new Exception(dae.getMessage());
		}
	}

        public boolean getRuolosuAnagrafe(String codiceFiscale, long idAzienda)
        throws Exception
        {
                try
                {
                        return commonDAO.getRuolosuAnagrafe(codiceFiscale, idAzienda);
                }
                catch(DataAccessException dae)
                {
                        throw new Exception(dae.getMessage());
                }
        }


	public String getIstatByDescComune(String descComune)
	throws Exception
	{
		try
		{
			return commonDAO.getIstatByDescComune(descComune);
		}
		catch(DataAccessException dae)
		{
			throw new Exception(dae.getMessage());
		}

	}

	/**
	 * Metodo per recuperare l'elenco dei mandati a partire dall'utente
	 */
	public Vector<DelegaVO> getMandatiByUtente(UtenteAbilitazioni utenteAbilitazioni, boolean forZona, java.util.Date dataDal, java.util.Date dataAl) throws SolmrException, Exception {
		Vector<DelegaVO> elencoMandati = null;
		try {
			elencoMandati = commonDAO.getMandatiByUtente(utenteAbilitazioni, forZona,dataDal,dataAl);
			if(elencoMandati == null || elencoMandati.size() == 0) {
				throw new SolmrException((String)AnagErrors.get("ERR_NESSUN_MANDATO"));
			}
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		return elencoMandati;
	}

	/**
	 * Recupero l'elenco di CAA(espresso con un vector di CODE-DESCRIPTION perchè mi serve per popolare una combo)
	 * in relazione all'utente che si è loggato
	 */
	public Vector<CodeDescription> getElencoCAAByUtente(UtenteAbilitazioni utenteAbilitazioni) throws Exception, SolmrException {
		Vector<CodeDescription> elencoCAA = null;
		try {
			elencoCAA = commonDAO.getElencoCAAByUtente(utenteAbilitazioni);
			if(elencoCAA == null || elencoCAA.size() == 0) {
				throw new SolmrException((String)AnagErrors.get("ERR_NO_CAA"));
			}
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		return elencoCAA;
	}

	/**
	 * Metodo per recuperare l'elenco dei mandati validati in un determinato periodo di tempo
	 */
	public Vector<DelegaVO> getMandatiValidatiByUtente(UtenteAbilitazioni utenteAbilitazioni, boolean forZona, java.util.Date dataDal, java.util.Date dataAl) throws Exception, SolmrException {
		Vector<DelegaVO> elencoMandati = null;
		try {
			elencoMandati = commonDAO.getMandatiValidatiByUtente(utenteAbilitazioni, forZona, dataDal, dataAl);
			if(elencoMandati == null || elencoMandati.size() == 0) {
				throw new SolmrException((String)AnagErrors.get("ERR_NESSUN_MANDATO"));
			}
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		return elencoMandati;
	}

	/**
	 * Metodo per reperire tutte le informazioni relative all'utente di tipo intermediario associato ad uno specifico ufficio di zona
	 * @param idUfficioZonaIntermediario Long
	 * @return it.csi.solmr.dto.IntermediarioVO
	 * @throws Exception
	 */
	public IntermediarioVO getIntermediarioVOByIdUfficioZonaIntermediario(Long idUfficioZonaIntermediario) throws Exception {
		try {
			return commonDAO.getIntermediarioVOByIdUfficioZonaIntermediario(idUfficioZonaIntermediario);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo per recuperare l'intermediario a partire dalla chiave primaria
	 * @param idIntermediario Long
	 * @return it.csi.solmr.dto.IntermediarioVO
	 * @throws Exception
	 */
	public IntermediarioVO findIntermediarioVOByPrimaryKey(Long idIntermediario) throws Exception {
		try {
			return commonDAO.findIntermediarioVOByPrimaryKey(idIntermediario);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo che mi restituisce l'elenco dei titoli di possesso in relazione al criterio
	 * di ordinamento indicato dall'utente
	 *
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public CodeDescription[] getListTipiTitoloPossesso(String orderBy) throws Exception {
		try 
		{
			Vector<CodeDescription> temp = commonDAO.getListTipiTitoloPossesso(orderBy);
			return (CodeDescription[])temp.toArray(new CodeDescription[temp.size()]);
		}
		catch(DataAccessException dae) 
		{
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo utilizzato per estrarre l'elenco dei casi particolari
	 *
	 * @param orderBy
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public it.csi.solmr.dto.CodeDescription[] getListTipoCasoParticolare(String orderBy) throws Exception {
		Vector<CodeDescription> temp = new Vector<CodeDescription>();
		try {
			temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_CASO_PARTICOLARE, orderBy);
			return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch(NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}

	/**
	 * Metodo utilizzato per estrarre l'elenco delle zone altimetriche
	 *
	 * @param orderBy
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public CodeDescription[] getListTipoZonaAltimetrica(String orderBy) throws Exception {
		Vector<CodeDescription> temp = new Vector<CodeDescription>();
		try {
			temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_ZONA_ALTIMETRICA, orderBy);
			return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch(NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}
	

	/**
	 * Metodo utilizzato per estrarre l'elenco delle area A
	 *
	 * @param orderBy
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public CodeDescription[] getListTipoAreaA(String orderBy) throws Exception {
		Vector<CodeDescription> temp = new Vector<CodeDescription>();
		try {
			temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_A, orderBy);
			return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch(NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}
	
	
	/**
   * Metodo utilizzato per estrarre l'elenco delle area A
   * con data fine validità a null
   *
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
	public CodeDescription[] getValidListTipoAreaA(String orderBy) throws Exception 
	{
    Vector<CodeDescription> temp = new Vector<CodeDescription>();
    try {
      temp = commonDAO.getValidCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_A, orderBy);
      return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
  }

	/**
	 * Metodo utilizzato per estrarre l'elenco delle area B
	 *
	 * @param orderBy
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public CodeDescription[] getListTipoAreaB(String orderBy) throws Exception {
		Vector<CodeDescription> temp = new Vector<CodeDescription>();
		try {
			temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_B, orderBy);
			return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch(NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}
	
	/**
   * Metodo utilizzato per estrarre l'elenco delle area B
   * con data fine validità a null
   *
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public CodeDescription[] getValidListTipoAreaB(String orderBy) throws Exception {
    Vector<CodeDescription> temp = new Vector<CodeDescription>();
    try {
      temp = commonDAO.getValidCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_B, orderBy);
      return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
  }

	/**
	 * Metodo utilizzato per estrarre l'elenco delle area C
	 *
	 * @param orderBy
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public CodeDescription[] getListTipoAreaC(String orderBy) throws Exception {
		Vector<CodeDescription> temp = new Vector<CodeDescription>();
		try {
			temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_C, orderBy);
			return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch(NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}
	
	
	/**
   * Metodo utilizzato per estrarre l'elenco delle area C
   * con data fine validità a null
   *
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public CodeDescription[] getValidListTipoAreaC(String orderBy) throws Exception {
    Vector<CodeDescription> temp = new Vector<CodeDescription>();
    try {
      temp = commonDAO.getValidCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_C, orderBy);
      return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
  }

	/**
	 * Metodo utilizzato per estrarre l'elenco delle area D
	 *
	 * @param orderBy
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public CodeDescription[] getListTipoAreaD(String orderBy) throws Exception {
		Vector<CodeDescription> temp = new Vector<CodeDescription>();
		try {
			temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_D, orderBy);
			return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch(NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}
	
	public CodeDescription[] getListTipoAreaM(String orderBy) throws Exception {
    Vector<CodeDescription> temp = new Vector<CodeDescription>();
    try {
      temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_M, orderBy);
      return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
    catch(NotFoundException nfe) {
      throw new Exception(nfe.getMessage());
    }
  }
	
	/**
   * Metodo utilizzato per estrarre l'elenco delle area D
   * con data fine validità a null
   *
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public CodeDescription[] getValidListTipoAreaD(String orderBy) throws Exception {
    Vector<CodeDescription> temp = new Vector<CodeDescription>();
    try {
      temp = commonDAO.getValidCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_D, orderBy);
      return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
  }
	
	/**
	 * Metodo utilizzato per estrarre l'elenco delle area E
	 *
	 * @param orderBy
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public CodeDescription[] getListTipoAreaE(String orderBy) throws Exception {
		Vector<CodeDescription> temp = new Vector<CodeDescription>();
		try {
			temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_E, orderBy);
			return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch(NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}
	
	/**
	 * Metodo utilizzato per estrarre l'elenco delle area F
	 *
	 * @param orderBy
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public CodeDescription[] getListTipoAreaF(String orderBy) throws Exception {
		Vector<CodeDescription> temp = new Vector<CodeDescription>();
		try {
			temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_F, orderBy);
			return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch(NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}
	
	/**
   * Metodo utilizzato per estrarre l'elenco delle area PSN
   *
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public CodeDescription[] getListTipoAreaPSN(String orderBy) throws Exception {
    Vector<CodeDescription> temp = new Vector<CodeDescription>();
    try {
      temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_PSN, orderBy);
      return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
    catch(NotFoundException nfe) {
      throw new Exception(nfe.getMessage());
    }
  }
  
  /**
   * Metodo utilizzato per estrarre l'elenco dalla Fascia Fluviale
   *
   * @param orderBy
   * @return it.csi.solmr.dto.CodeDescription[]
   * @throws Exception
   */
  public CodeDescription[] getListTipoFasciaFluviale(String orderBy) throws Exception {
    Vector<CodeDescription> temp = new Vector<CodeDescription>();
    try {
      temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_FASCIA_FLUVIALE, orderBy);
      return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
    catch(NotFoundException nfe) {
      throw new Exception(nfe.getMessage());
    }
  }

	/**
	 * Metodo utilizzato per estrarre l'elenco delle causali modifica particella
	 *
	 * @param orderBy
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public CodeDescription[] getListTipoCausaleModParticella(String orderBy) throws Exception {
		Vector<CodeDescription> temp = new Vector<CodeDescription>();
		try {
			temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_CAUSALE_MOD_PARTICELLA, orderBy);
			return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
		catch(NotFoundException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}

	/**
	 * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_IMPIANTO
	 *
	 * @param onlyActive
	 * @param orderBy
	 * @return it.csi.solmr.dto.anag.terreni.TipoImpiantoVO[]
	 * @throws Exception
	 */
	public TipoImpiantoVO[] getListTipoImpianto(boolean onlyActive, String orderBy) throws Exception {
		try {
			return commonDAO.getListTipoImpianto(onlyActive, orderBy);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_INDIRIZZO_UTILIZZO
	 *
	 * @param colturaSecondaria
	 * @param orderBy
	 * @param onlyActive
	 * @return it.csi.solmr.dto.CodeDescription[]
	 * @throws Exception
	 */
	public it.csi.solmr.dto.CodeDescription[] getListTipoIndirizzoUtilizzo(String colturaSecondaria, String orderBy, boolean onlyActive) throws Exception {
		try {
			return tipoUtilizzoDAO.getListTipoIndirizzoUtilizzo(colturaSecondaria, orderBy, onlyActive);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_IRRIGAZIONE
	 *
	 * @param orderBy
	 * @param onlyActive
	 * @return it.csi.solmr.dto.anag.terreni.TipoIrrigazioneVO[]
	 * @throws Exception
	 */
	public TipoIrrigazioneVO[] getListTipoIrrigazione(String orderBy, boolean onlyActive) throws Exception {
		try {
			return commonDAO.getListTipoIrrigazione(orderBy, onlyActive);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}

	/**
	 * Metodo che mi restituisce il Comune in funzione dei parametri, del suo stato
	 * di vita e dello stato del catasto.
	 *
	 * @param descComune
	 * @param siglaProvincia
	 * @param flagEstinto
	 * @param flagCatastoAttivo
	 * @param flagEstero
	 * @return
	 * @throws Exception
	 */
	public ComuneVO getComuneByParameters(String descComune, String siglaProvincia, String flagEstinto, String flagCatastoAttivo, String flagEstero) throws Exception {
		try {
			return commonDAO.getComuneByParameters(descComune, siglaProvincia, flagEstinto, flagCatastoAttivo, flagEstero);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}
	
	/**
	 * Metodo che mi consente di reperire l'elenco dei comuni in funzione
	 * dei parametri di ricerca associati
	 * 
	 * @param descComune
	 * @param siglaProvincia
	 * @param flagEstinto
	 * @param flagCatastoAttivo
	 * @param flagEstero
	 * @param orderBy
	 * @return java.util.Vector
	 * @throws Exception
	 * @throws SolmrException
	 */
	public Vector<ComuneVO> getComuniByParameters(String descComune, String siglaProvincia, String flagEstinto, String flagCatastoAttivo, String flagEstero, String[] orderBy) throws Exception, SolmrException {
		try {
			return commonDAO.getComuniByParameters(descComune, siglaProvincia, flagEstinto, flagCatastoAttivo, flagEstero, orderBy);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}
	
	public Vector<ComuneVO> getComuniAttiviByIstatProvincia(String istatProvincia) 
	    throws Exception, SolmrException
	{
	  try 
	  {
      return commonDAO.getComuniAttiviByIstatProvincia(istatProvincia);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
  }
	    
	    
	
	/**
	 * Metodo che mi restituisce l'elenco dei tipi evento in relazione al criterio
	 * di ordinamento indicato dall'utente
	 *
	 * @param orderBy
	 * @return 
	 * @throws Exception
	 */
	public Vector<TipoEventoVO> getListTipiEvento() throws Exception 
	{
		try 
		{
			return tipoEventoDAO.getListTipiEvento();
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}
	
	/**
	 * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_CESSAZIONE
	 *
	 * @param orderBy
	 * @param onlyActive
	 * @return it.csi.solmr.dto.anag.TipoCessazioneVO[]
	 * @throws Exception
	 */
	public TipoCessazioneVO[] getListTipoCessazione(String orderBy, boolean onlyActive) throws Exception {
		try {
			return commonDAO.getListTipoCessazione(orderBy, onlyActive);
		}
		catch(DataAccessException dae) {
			throw new Exception(dae.getMessage());
		}
	}
	
	/**
	 * Ricavo l'oggeto intermediario VO passando il suo codice fiscale.
	 * 
	 * @param codice_fiscale
	 * @return
	 * @throws Exception
	 */
	/*public IntermediarioVO getIntermediarioVOByCodiceFiscale(String codice_fiscale) throws Exception {
    try {
      return commonDAO.getIntermediarioVOByCodiceFiscale(codice_fiscale);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
  }*/
	
	public it.csi.solmr.dto.CodeDescription[] getListTipoAreaG(String orderBy) throws Exception {
    Vector<CodeDescription> temp = new Vector<CodeDescription>();
    try {
      temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_G, orderBy);
      return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
    catch(NotFoundException nfe) {
      throw new Exception(nfe.getMessage());
    }
  }
	
	public it.csi.solmr.dto.CodeDescription[] getListTipoAreaH(String orderBy) throws Exception {
    Vector<CodeDescription> temp = new Vector<CodeDescription>();
    try {
      temp = commonDAO.getCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_H, orderBy);
      return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
    catch(NotFoundException nfe) {
      throw new Exception(nfe.getMessage());
    }
  }
	
	
	public it.csi.solmr.dto.CodeDescription[] getValidListTipoAreaM(String orderBy) throws Exception 
	{
    Vector<CodeDescription> temp = new Vector<CodeDescription>();
    try 
    {
      temp = commonDAO.getValidCodeDescriptions(SolmrConstants.TAB_TIPO_AREA_M, orderBy);
      return (it.csi.solmr.dto.CodeDescription[])temp.toArray(new it.csi.solmr.dto.CodeDescription[temp.size()]);
    }
    catch(DataAccessException dae) 
    {
      throw new Exception(dae.getMessage());
    }
  }
	
	
	/**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_POTENZIALITA_IRRIGUA
   *
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoPotenzialitaIrriguaVO[]
   * @throws Exception
   */
  public TipoPotenzialitaIrriguaVO[] getListTipoPotenzialitaIrrigua(String orderBy, Date dataRiferiemento) 
    throws Exception 
  {
    try 
    {
      return commonDAO.getListTipoPotenzialitaIrrigua(orderBy, dataRiferiemento);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_TERRAZZAMENTO
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoTerrazzamentoVO[]
   * @throws DataAccessException
   */
  public TipoTerrazzamentoVO[] getListTipoTerrazzamento(String orderBy,
      Date dataRiferiemento) throws Exception 
  {
    try 
    {
      return commonDAO.getListTipoTerrazzamento(orderBy, dataRiferiemento);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
  }
  
  
  /**
   * Metodo che si occupa di reperire i dati dalla tabella DB_TIPO_ROTAZIONE_COLTURALE
   * 
   * @param orderBy
   * @param onlyActive
   * @return it.csi.solmr.dto.anag.terreni.TipoRotazioneColturaleVO[]
   * @throws DataAccessException
   */
  public TipoRotazioneColturaleVO[] getListTipoRotazioneColturale(String orderBy,
      Date dataRiferiemento) throws Exception 
  {
    try 
    {
      return commonDAO.getListTipoRotazioneColturale(orderBy, dataRiferiemento);
    }
    catch(DataAccessException dae) {
      throw new Exception(dae.getMessage());
    }
  }
	
	
	
	
}
