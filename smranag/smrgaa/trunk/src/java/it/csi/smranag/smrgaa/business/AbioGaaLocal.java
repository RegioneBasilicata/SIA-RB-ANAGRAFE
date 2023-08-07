package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.abio.OperatoreBiologicoVO;
import it.csi.smranag.smrgaa.dto.abio.PosizioneOperatoreVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.exception.SolmrException;

import java.util.Date;

import javax.ejb.EJBLocalObject;
import javax.ejb.Local;

@Local
public interface AbioGaaLocal {
	public OperatoreBiologicoVO getOperatoreBiologicoByIdAzienda(
			Long idAzienda, Date dataInizioAttivita) throws SolmrException;
	
	public PosizioneOperatoreVO[] getAttivitaBiologicheByIdAzienda(
		    Long idOperatoreBiologico, Date dataFineValidita, boolean checkStorico) throws SolmrException;

	public CodeDescription[] getODCbyIdOperatoreBiologico(
			Long idOperatoreBiologico, Date dataInizioValidita, boolean pianoCorrente) throws SolmrException;
	
	public OperatoreBiologicoVO getOperatoreBiologicoAttivo(Long idAzienda)
    throws SolmrException;

}
