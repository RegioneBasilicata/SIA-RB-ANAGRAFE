package it.csi.smranag.smrgaa.dto.stampe.protocollo;

import java.util.Vector;

import it.csi.solmr.dto.anag.DocumentoProprietarioVO;

public class DocProprietari {
	private Vector<DocumentoProprietarioVO> elencoProprietari;

	public Vector<DocumentoProprietarioVO> getElencoProprietari() {
		return elencoProprietari;
	}

	public void setElencoProprietari(Vector<DocumentoProprietarioVO> elencoProprietari) {
		this.elencoProprietari = elencoProprietari;
	}
	
	
}
