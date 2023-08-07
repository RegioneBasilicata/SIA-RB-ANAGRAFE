package it.csi.solmr.business.anag;

import it.csi.solmr.dto.anag.FabbricatoVO;
import it.csi.solmr.dto.anag.services.FabbricatoParticellaVO;


import javax.ejb.Local;

@Local
public interface FabbricatoLocal {
	public FabbricatoParticellaVO[] getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(Long idConduzioneParticella, Long idAzienda, String[] orderBy, boolean onlyActive) throws Exception;
	public FabbricatoVO[] getListFabbricatiAziendaByPianoRifererimento(Long idAzienda, Long idPianoRiferimento, Long idUte, String[] orderBy) throws Exception;
}
