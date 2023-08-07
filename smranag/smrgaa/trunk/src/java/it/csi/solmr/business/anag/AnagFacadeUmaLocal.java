package it.csi.solmr.business.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.ProvinciaVO;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.AnagraficaAzVO;
import it.csi.solmr.dto.anag.DelegaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.SolmrException;

import java.util.Date;

public interface AnagFacadeUmaLocal extends javax.ejb.EJBLocalObject {
	public ProvinciaVO[] getProvinceByRegione(String idRegione)
			throws Exception;

	public AnagAziendaVO findAziendaAttiva(Long idAzienda)
			throws DataAccessException, NotFoundException, Exception;

	public String getRappLegaleTitolareByIdAzienda(Long idAzienda)
			throws Exception, SolmrException;

	public AnagraficaAzVO getDatiAziendaPerMacchine(Long idAzienda)
			throws Exception, SolmrException;

	public PersonaFisicaVO getTitolareORappresentanteLegaleAzienda(
			Long idAzienda, Date data) throws NotFoundException, Exception,
			SolmrException;

	public CodeDescription[] getTipiIntermediario()
			throws NotFoundException, Exception;

	public CodeDescription[] getTipiIntermediarioUmaProv()
			throws NotFoundException, Exception;

	public String getFlagPartitaIva(Long idTipoFormaGiuridica)
			throws Exception, SolmrException;

	public String getFormaGiuridicaFlagCCIAA(Long idFormaGiuridica)
			throws Exception, SolmrException;

	public boolean isProvinciaReaValida(String siglaProvincia)
			throws Exception, SolmrException;

	public String ricercaCodiceComune(String descrizioneComune,
			String siglaProvincia) throws DataAccessException,
			NotFoundException, DataControlException, Exception;

	public String ricercaCodiceComuneNonEstinto(String descrizioneComune,
			String siglaProvincia) throws Exception, SolmrException;

	public String ricercaCodiceFiscaleComune(String descrizioneComune,
			String siglaProvincia) throws Exception, DataControlException;

	public void checkPartitaIVA(String partitaIVA, Long idAzienda)
			throws DataAccessException, Exception, SolmrException;

	public void checkIsCUAAPresent(String cuaa, Long idAzienda)
			throws Exception, SolmrException;

	public AnagAziendaVO getAziendaById(Long idAnagAzienda)
			throws NotFoundException, Exception, SolmrException;

	public PersonaFisicaVO getPersonaFisica(String cuaa)
			throws DataAccessException, Exception, SolmrException;

	public ComuneVO getComuneByCUAA(String cuaa) throws Exception,
			SolmrException;

	public PersonaFisicaVO getRappresentanteLegaleFromIdAnagAzienda(
			Long idAnagAzienda) throws Exception, SolmrException;

	public AnagAziendaVO findAziendaAttivabyCriterio(String cuaa,
			String partitaIva) throws Exception, SolmrException;

	public PersonaFisicaVO[] getSoggetti(Long idAzienda, Date data)
			throws Exception, SolmrException;

	public PersonaFisicaVO[] getSoggetti(Long idAzienda, Boolean storico)
			throws Exception, SolmrException;

	public DelegaVO getDelegaByAzienda(Long idAzienda,
			Long idProcedimento) throws Exception;

	public String getDenominazioneByIdAzienda(Long idAzienda) throws Exception,
			SolmrException;

	public ComuneVO[] ricercaStatoEstero(String statoEstero,
			String estinto, String flagCatastoAttivo) throws Exception;

	public ComuneVO[] getComuniLikeProvAndCom(String provincia,
			String comune) throws NotFoundException, SolmrException, Exception;

	public ComuneVO[] getComuniNonEstintiLikeProvAndCom(String provincia,
			String comune, String flagEstero) throws NotFoundException,
			SolmrException, Exception;

	public StringcodeDescription[] getProvincePiemonte()
			throws Exception, SolmrException;

	public CodeDescription[] getTipiFormaGiuridica(Long idTipologiaAzienda)
			throws Exception, NotFoundException;

}
