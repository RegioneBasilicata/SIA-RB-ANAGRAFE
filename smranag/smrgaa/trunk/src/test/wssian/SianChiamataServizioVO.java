package wssian;


import java.io.Serializable;
import java.util.Date;

public class SianChiamataServizioVO implements Serializable{
  
  static final long serialVersionUID = -8129265727489273009L;

  private Long idChiamata;
  private Long idServizio;
  private String cuaaInput;
  private Date dataChiamata;
  private String codRetOutput;
  private String segnalazioneOutput;
  private String idXmlOutput;
  private String idXmlInput;
  private String eccezione;
  
public Long getIdChiamata() {
	return idChiamata;
}
public void setIdChiamata(Long idChiamata) {
	this.idChiamata = idChiamata;
}
public Long getIdServizio() {
	return idServizio;
}
public void setIdServizio(Long idServizio) {
	this.idServizio = idServizio;
}
public String getCuaaInput() {
	return cuaaInput;
}
public void setCuaaInput(String cuaaInput) {
	this.cuaaInput = cuaaInput;
}
public Date getDataChiamata() {
	return dataChiamata;
}
public void setDataChiamata(Date dataChiamata) {
	this.dataChiamata = dataChiamata;
}
public String getCodRetOutput() {
	return codRetOutput;
}
public void setCodRetOutput(String codRetOutput) {
	this.codRetOutput = codRetOutput;
}
public String getSegnalazioneOutput() {
	return segnalazioneOutput;
}
public void setSegnalazioneOutput(String segnalazioneOutput) {
	this.segnalazioneOutput = segnalazioneOutput;
}
public String getIdXmlOutput() {
	return idXmlOutput;
}
public void setIdXmlOutput(String idXmlOutput) {
	this.idXmlOutput = idXmlOutput;
}
public String getIdXmlInput() {
	return idXmlInput;
}
public void setIdXmlInput(String idXmlInput) {
	this.idXmlInput = idXmlInput;
}
public String getEccezione() {
	return eccezione;
}
public void setEccezione(String eccezione) {
	this.eccezione = eccezione;
}
  
}
