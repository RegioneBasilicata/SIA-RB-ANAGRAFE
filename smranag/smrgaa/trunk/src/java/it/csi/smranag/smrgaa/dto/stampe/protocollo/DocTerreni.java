package it.csi.smranag.smrgaa.dto.stampe.protocollo;

import java.util.Vector;

import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;

public class DocTerreni {
	private Vector<StoricoParticellaVO> elencoParticelle;

	public Vector<StoricoParticellaVO> getElencoParticelle() {
		return elencoParticelle;
	}

	public void setElencoParticelle(Vector<StoricoParticellaVO> elencoParticelle) {
		this.elencoParticelle = elencoParticelle;
	}
	
	
}
