package it.csi.solmr.business.anag;

import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.smrcomms.smrcomm.dto.datientiprivati.DatiEntePrivatoVO;
import it.csi.smrcomms.smrcomm.dto.filtro.EntePrivatoFiltroVO;
import it.csi.solmr.dto.comune.AmmCompetenzaVO;
import it.csi.solmr.dto.comune.IntermediarioVO;
import it.csi.solmr.dto.comune.TecnicoAmministrazioneVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.dto.profile.TipoProcedimentoVO;
import it.csi.solmr.dto.profile.UtenteIride2VO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.InvalidParameterException;

import java.util.Date;

import javax.ejb.Local;

@Local
public interface SmrCommLocal 
{
	//public Vector<UtenteProcedimento> serviceGetUtenteProcedimento(String codiceFiscale,Long idProcedimento,String ruolo, String codiceEnte, String dirittoAccesso, Long idLivello) throws SystemException,SolmrException,Exception;
	public AmmCompetenzaVO[] serviceFindAmmCompetenzaByIdRange(String idAmmCompetenza[]) throws SolmrException,Exception;
	public AmmCompetenzaVO serviceFindAmmCompetenzaById(Long idAmmCompetenza) throws SolmrException,Exception;
	public AmmCompetenzaVO[] serviceGetListAmmCompetenzaByComuneCollegato(String istatComune,String tipoAmministrazione) throws SolmrException,Exception;
	public AmmCompetenzaVO serviceFindAmmCompetenzaByCodiceAmm(String codiceAmm) throws SolmrException,Exception;
	public Boolean isUtenteConRuoloSuProcedimento(String codiceFiscale, Long idProcedimento) throws SystemException, InvalidParameterException, UnrecoverableException, SolmrException,Exception;
	public Boolean isUtenteAbilitatoProcedimento(UtenteIride2VO utenteIride2VO) throws SystemException, InvalidParameterException, UnrecoverableException, SolmrException,Exception;
	public Long writeAccessLogUser(UtenteIride2VO utenteIride2VO) throws SystemException, InvalidParameterException, UnrecoverableException, SolmrException,Exception;
	public RuoloUtenza loadRoleUser(UtenteIride2VO utenteIride2VO) throws SystemException, InvalidParameterException, UnrecoverableException, SolmrException,Exception;
	public TipoProcedimentoVO serviceFindTipoProcedimentoByDescrizioneProcedimento( String descrizione) throws SystemException, InvalidParameterException, UnrecoverableException, SolmrException,Exception;
	public IntermediarioVO serviceFindIntermediarioByIdIntermediario(Long idIntermediario) throws Exception, SolmrException;
	public IntermediarioVO serviceFindIntermediarioByCodiceFiscale(String codiceFiscale) throws Exception, SolmrException;
	public IntermediarioVO serviceFindResponsabileByIdIntermediarioAndIdProcToDateRiferimento(Long idIntermediario, Long idProcedimento, Date dataRiferimento) throws Exception, SolmrException;
	public AmmCompetenzaVO[] serviceGetListAmmCompetenza() throws Exception, SolmrException;
	//public boolean serviceVerificaGerarchia(Long idUtenteConnesso, Long idUtentePratica) throws Exception, SolmrException;
	public TecnicoAmministrazioneVO[] serviceGetTecnicoAmmByIdAmmCompAndIdProcedimento(Long idAmmCompetenza,
      Long idProcedimento)  throws SolmrException,Exception;
	public long[] smrcommSearchListIdEntiPrivatiByCodiceEntePrivatoRange(String[] arrCodiceEntePrivato,
      boolean flagCessazione) throws SolmrException,Exception;
	public DatiEntePrivatoVO[] smrcommGetEntiPrivatiByIdEntePrivatoRange(long[] idEntePrivato,
      int tipoRisultato, EntePrivatoFiltroVO filtro) throws SolmrException,Exception;
}
