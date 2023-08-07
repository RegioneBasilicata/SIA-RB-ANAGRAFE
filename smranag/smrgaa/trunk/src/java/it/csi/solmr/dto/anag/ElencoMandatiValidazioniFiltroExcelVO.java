package it.csi.solmr.dto.anag;

import java.io.Serializable;

public class ElencoMandatiValidazioniFiltroExcelVO implements Serializable{
	
	private static final long serialVersionUID = -4357785007188733127L;

	private String dataValidazioneDal = null;
	private String dataValidazioneAl = null;
	private String dataRiferimentoValidazione = null;
	
	public ElencoMandatiValidazioniFiltroExcelVO(){
		super();
	}
	
	public void setDataValidazioneDal(String dal){
		this.dataValidazioneDal = dal;
	}
	public String getDataValidazioneDal(){
		return this.dataValidazioneDal;
	}
	public void setDataValidazioneAl(String al){
		this.dataValidazioneAl = al;
	}
	public String getDataValidazioneAl(){
		return this.dataValidazioneAl;
	}
	public void setDataRiferimentoValidazione(String dal,String al){
		this.dataRiferimentoValidazione = "dal " + dal + " al " + al;
	}
	public String getDataRiferimentoValidazione(){
		return this.dataRiferimentoValidazione;
	}
	
	
}
