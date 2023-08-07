package it.csi.solmr.dto.anag.services;

import java.io.Serializable;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author
 * @version 1.0
 */

public class FabbricatoParticellaVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -5545005090404126281L;

  private Long idFabbricatoParticella;
  private Long idFabbricato;
  private Long idParticella;
  private Long idStoricoParticella;
  private java.util.Date dataInizioValidita;
  private java.util.Date dataFineValidita;

  public Long getIdFabbricatoParticella() {
    return idFabbricatoParticella;
  }
  public void setIdFabbricatoParticella(Long idFabbricatoParticella) {
    this.idFabbricatoParticella = idFabbricatoParticella;
  }
  public Long getIdFabbricato() {
    return idFabbricato;
  }
  public void setIdFabbricato(Long idFabbricato) {
    this.idFabbricato = idFabbricato;
  }
  public Long getIdParticella() {
    return idParticella;
  }
  public void setIdParticella(Long idParticella) {
    this.idParticella = idParticella;
  }
  public Long getIdStoricoParticella() {
    return idStoricoParticella;
  }
  public void setIdStoricoParticella(Long idStoricoParticella) {
    this.idStoricoParticella = idStoricoParticella;
  }
  public java.util.Date getDataInizioValidita() {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(java.util.Date dataInizioValidita) {
    this.dataInizioValidita = dataInizioValidita;
  }
  public java.util.Date getDataFineValidita() {
    return dataFineValidita;
  }
  public void setDataFineValidita(java.util.Date dataFineValidita) {
    this.dataFineValidita = dataFineValidita;
  }

}
