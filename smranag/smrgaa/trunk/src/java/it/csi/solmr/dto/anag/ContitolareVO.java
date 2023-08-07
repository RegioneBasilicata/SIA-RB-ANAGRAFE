package it.csi.solmr.dto.anag;

import java.io.Serializable;

/**
 * <p>Title: SMRGAA</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */

public class ContitolareVO implements Serializable
{

  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -1319776027737203162L;

  private Long idContitolare;
  private Long idSoggetto;
  private Long idRuolo;
  private Long idAzienda;
  private java.util.Date dataInizioRuolo;
  private java.util.Date dataFineRuolo;
  private String descrizioneRuolo;
  private String flagAttivo;
  public ContitolareVO()
  {
  }

  public Long getIdContitolare()
  {
    return idContitolare;
  }

  public void setIdContitolare(Long idContitolare)
  {
    this.idContitolare = idContitolare;
  }

  public void setIdSoggetto(Long idSoggetto)
  {
    this.idSoggetto = idSoggetto;
  }

  public Long getIdSoggetto()
  {
    return idSoggetto;
  }
  public void setIdRuolo(Long idRuolo)
  {
    this.idRuolo = idRuolo;
  }

  public Long getIdRuolo()
  {
    return idRuolo;
  }

  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public Long getIdAzienda()
  {
    return idAzienda;
  }

  public void setDataInizioRuolo(java.util.Date dataInizioRuolo)
  {
    this.dataInizioRuolo = dataInizioRuolo;
  }

  public java.util.Date getDataInizioRuolo()
  {
    return dataInizioRuolo;
  }

  public void setDataFineRuolo(java.util.Date dataFineRuolo)
  {
    this.dataFineRuolo = dataFineRuolo;
  }

  public java.util.Date getDataFineRuolo()
  {
    return dataFineRuolo;
  }

  public void setDescrizioneRuolo(String descrizioneRuolo)
  {
    this.descrizioneRuolo = descrizioneRuolo;
  }

  public String getDescrizioneRuolo()
  {
    return descrizioneRuolo;
  }

  public void setFlagAttivo(String flagAttivo)
  {
    this.flagAttivo = flagAttivo;
  }

  public String getFlagAttivo()
  {
    return flagAttivo;
  }
}