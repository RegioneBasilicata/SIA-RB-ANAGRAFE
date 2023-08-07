package it.csi.solmr.dto.anag;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */

public class BancaSportelloVO implements Serializable
{
  /**
   * Serial version
   */
  static final long serialVersionUID = 6842609385484958572L;
  private String denominazioneBanca; //DB_TIPO_BANCA.DENOMINAZIONE
  private String bic;
  private String cab; // DB_TIPO_SPORTELLO.CAB
  private String indirizzoSportello; // DB_TIPO_SPORTELLO.INDIRIZZO
  private String capSportello; // DB_TIPO_SPORTELLO.CAP
  private String istatComuneSportello; // DB_TIPO_SPORTELLO.COMUNE
  private String denominazioneSportello; // DB_TIPO_SPORTELLO.DENOMINAZIONE
  private String descrizioneComuneSportello; // COMUNE.DESCOM da  DB_TIPO_SPORTELLO.COMUNE
  private String abi; // DB_TIPO_BANCA.ABI
  private Date dataInizioValiditaBanca; // DB_TIPO_BANCA.DATA_INIZIO_VALIDITA
  private Date dataInizioValiditaSportello; // DB_TIPO_SPORTELLO.DATA_INIZIO_VALIDITA
  private Date dataFineValiditaSportello; // DB_TIPO_SPORTELLO.DATA_FINE_VALIDITA
  private Date dataFineValiditaBanca; // DB_TIPO_BANCA.DATA_FINE_VALIDITA
  private Long idBanca; // DB_TIPO_BANCA.ID_BANCA
  private Long idSportello; // DB_TIPO_SPORTELLO.ID_SPORTELLO
  private String provinciaSportello; // PROVINCIA.DESCRIZIONE da COMUNE.ISTAT_PROVINCIA da DB_TIPO_SPORTELLO.COMUNE
  private String siglaProvincia; // PROVINCIA.SIGLA_PROVINCIA da COMUNE.ISTAT_PROVINCIA da DB_TIPO_SPORTELLO.COMUNE
  private String codPaese;
  private String flagSportelloGf;
  
  public BancaSportelloVO()
  {
  }
  public Long getIdBanca()
  {
    return idBanca;
  }
  public void setIdBanca(Long idBanca)
  {
    this.idBanca = idBanca;
  }
  public void setAbi(String abi)
  {
    this.abi = abi;
  }
  public String getAbi()
  {
    return abi;
  }
  public void setDenominazioneBanca(String denominazioneBanca)
  {
    this.denominazioneBanca = denominazioneBanca;
  }
  public String getDenominazioneBanca()
  {
    return denominazioneBanca;
  }
  public void setBic(String bic){
	  this.bic = bic;
  }
  public String getBic(){
	  return this.bic;
  }
  public void setIdSportello(Long idSportello)
  {
    this.idSportello = idSportello;
  }
  public Long getIdSportello()
  {
    return idSportello;
  }
  public void setCab(String cab)
  {
    this.cab = cab;
  }
  public String getCab()
  {
    return cab;
  }
  public void setIndirizzoSportello(String indirizzoSportello)
  {
    this.indirizzoSportello = indirizzoSportello;
  }
  public String getIndirizzoSportello()
  {
    return indirizzoSportello;
  }
  public void setCapSportello(String capSportello)
  {
    this.capSportello = capSportello;
  }
  public String getCapSportello()
  {
    return capSportello;
  }
  public void setIstatComuneSportello(String istatComuneSportello)
  {
    this.istatComuneSportello = istatComuneSportello;
  }
  public String getIstatComuneSportello()
  {
    return istatComuneSportello;
  }
  public void setDenominazioneSportello(String denominazioneSportello)
  {
    this.denominazioneSportello = denominazioneSportello;
  }
  public String getDenominazioneSportello()
  {
    return denominazioneSportello;
  }
  public void setDataInizioValiditaBanca(Date dataInizioValiditaBanca)
  {
    this.dataInizioValiditaBanca = dataInizioValiditaBanca;
  }
  public Date getDataInizioValiditaBanca()
  {
    return dataInizioValiditaBanca;
  }
  public void setDataFineValiditaBanca(Date dataFineValiditaBanca)
  {
    this.dataFineValiditaBanca = dataFineValiditaBanca;
  }
  public Date getDataFineValiditaBanca()
  {
    return dataFineValiditaBanca;
  }
  public void setDataInizioValiditaSportello(Date dataInizioValiditaSportello)
  {
    this.dataInizioValiditaSportello = dataInizioValiditaSportello;
  }
  public Date getDataInizioValiditaSportello()
  {
    return dataInizioValiditaSportello;
  }
  public void setDataFineValiditaSportello(Date dataFineValiditaSportello)
  {
    this.dataFineValiditaSportello = dataFineValiditaSportello;
  }
  public Date getDataFineValiditaSportello()
  {
    return dataFineValiditaSportello;
  }
  public void setDescrizioneComuneSportello(String descrizioneComuneSportello)
  {
    this.descrizioneComuneSportello = descrizioneComuneSportello;
  }
  public String getDescrizioneComuneSportello()
  {
    return descrizioneComuneSportello;
  }
  public void setProvinciaSportello(String provinciaSportello)
  {
    this.provinciaSportello = provinciaSportello;
  }
  public String getProvinciaSportello()
  {
    return provinciaSportello;
  }
  public void setSiglaProvincia(String siglaProvincia)
  {
    this.siglaProvincia = siglaProvincia;
  }
  public String getSiglaProvincia()
  {
    return siglaProvincia;
  }
  public void setCodPaeseSportello(String codPaese){
	  this.codPaese = codPaese;
  }
  public String getCodPaeseSportello(){
	  return this.codPaese;
  }
  public String getFlagSportelloGf()
  {
    return flagSportelloGf;
  }
  public void setFlagSportelloGf(String flagSportelloGf)
  {
    this.flagSportelloGf = flagSportelloGf;
  }
}
