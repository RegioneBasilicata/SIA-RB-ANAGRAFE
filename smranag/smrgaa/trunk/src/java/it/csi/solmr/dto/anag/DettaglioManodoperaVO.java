package it.csi.solmr.dto.anag;

import java.io.Serializable;
import it.csi.solmr.util.*;
import java.util.Date;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Nadia B.
 * @version 1.0
 */

public class DettaglioManodoperaVO implements Serializable, Cloneable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 1129347338577275771L;


  private String idDettaglioManodopera = null;
  private String idManodopera = null;
  private String idClasseManodopera = null;
  private String uomini = null;
  private String donne = null;
  private String giornateAnnue = null;

  private Date dataInizioValiditaDate = null;
  private String dataInizioValidita = null;
  private Date dataFineValiditaDate = null;
  private String dataFineValidita = null;
  private TipoFormaConduzioneVO tipoFormaConduzioneVO = null;
  private String codTipoClasseManodopera = null;
  private String desTipoClasseManodopera = null;
  
  private String codiceInps = null;
  private String matricolaInail = null;

  public DettaglioManodoperaVO() {
  }

  public String getDump() {
    return null;
  }

  public ValidationErrors validate() {
    ValidationErrors errors = new ValidationErrors();

    return errors;
  }

  public Object clone()
  {
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException ex) {
      return null;
    }
  }


  public String getIdDettaglioManodopera() {
    return idDettaglioManodopera;
  }

  public void setIdDettaglioManodopera(String idDettaglioManodopera) {
    this.idDettaglioManodopera = idDettaglioManodopera;
  }

  public Long getIdDettaglioManodoperaLong() {
    try {
      return new Long(idDettaglioManodopera);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setIdDettaglioManodoperaLong(Long idDettaglioManodopera) {
    this.idDettaglioManodopera = idDettaglioManodopera == null ? null :
        idDettaglioManodopera.toString();
  }

  public String getIdManodopera() {
    return idManodopera;
  }

  public void setIdManodopera(String idManodopera) {
    this.idManodopera = idManodopera;
  }

  public Long getIdManodoperaLong() {
    try {
      return new Long(idManodopera);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setIdManodoperaLong(Long idManodopera) {
    this.idManodopera = idManodopera == null ? null : idManodopera.toString();
  }

  public String getIdClasseManodopera() {
    return idClasseManodopera;
  }

  public void setIdClasseManodopera(String idClasseManodopera) {
    this.idClasseManodopera = idClasseManodopera;
  }

  public Long getIdClasseManodoperaLong() {
    try {
      return new Long(idClasseManodopera);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setIdClasseManodoperaLong(Long idClasseManodopera) {
    this.idClasseManodopera = idClasseManodopera == null ? null :
        idClasseManodopera.toString();
  }

  public String getUomini() {
    return uomini;
  }

  public void setUomini(String uomini) {
    this.uomini = uomini;
  }

  public Long getUominiLong() {
    try {
      return new Long(uomini);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setUominiLong(Long uomini) {
    this.uomini = uomini == null ? null : uomini.toString();
  }

  public String getDonne() {
    return donne;
  }

  public void setDonne(String donne) {
    this.donne = donne;
  }

  public Long getDonneLong() {
    try {
      return new Long(donne);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setDonneLong(Long donne) {
    this.donne = donne == null ? null : donne.toString();
  }

  public String getGiornateAnnue() {
    return giornateAnnue;
  }

  public void setGiornateAnnue(String giornateAnnue) {
    this.giornateAnnue = giornateAnnue;
  }

  public Long getGiornateAnnueLong() {
    try {
      return new Long(giornateAnnue);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setGiornateAnnueLong(Long giornateAnnue) {
    this.giornateAnnue = giornateAnnue == null ? null : giornateAnnue.toString();
  }

  public String getDataInizioValidita() {
    return dataInizioValidita;
  }

  public void setDataInizioValidita(String dataInizioValidita) {
    this.dataInizioValidita = dataInizioValidita;
    try {
      this.dataInizioValiditaDate = DateUtils.parseDate(dataInizioValidita);
    }
    catch (Exception ex) {
    }
  }

  public Date getDataInizioValiditaDate() {
    return dataInizioValiditaDate;
  }

  public void setDataInizioValiditaDate(Date dataInizioValiditaDate) {
    this.dataInizioValiditaDate = dataInizioValiditaDate;
    try {
      this.dataInizioValidita = DateUtils.formatDate(dataInizioValiditaDate);
    }
    catch (Exception ex) {
    }
  }

  public String getDataFineValidita() {
    return dataFineValidita;
  }

  public void setDataFineValidita(String dataFineValidita) {
    this.dataFineValidita = dataFineValidita;
    try {
      this.dataFineValiditaDate = DateUtils.parseDate(dataFineValidita);
    }
    catch (Exception ex) {
    }
  }

  public Date getDataFineValiditaDate() {
    return dataFineValiditaDate;
  }

  public void setDataFineValiditaDate(Date dataFineValiditaDate) {
    this.dataFineValiditaDate = dataFineValiditaDate;
    try {
      this.dataFineValidita = DateUtils.formatDate(dataFineValiditaDate);
    }
    catch (Exception ex) {
    }
  }

  public TipoFormaConduzioneVO getTipoFormaConduzioneVO() {
    return tipoFormaConduzioneVO;
  }

  public void setTipoFormaConduzioneVO(TipoFormaConduzioneVO
                                       tipoFormaConduzioneVO) {
    this.tipoFormaConduzioneVO = tipoFormaConduzioneVO;
  }

  public String getCodTipoClasseManodopera() {
    return codTipoClasseManodopera;
  }

  public void setCodTipoClasseManodopera(String codTipoClasseManodopera) {
    this.codTipoClasseManodopera = codTipoClasseManodopera;
  }

  public String getDesTipoClasseManodopera() {
    return desTipoClasseManodopera;
  }

  public void setDesTipoClasseManodopera(String desTipoClasseManodopera) {
    this.desTipoClasseManodopera = desTipoClasseManodopera;
  }

  public String getCodiceInps()
  {
    return codiceInps;
  }

  public void setCodiceInps(String codiceInps)
  {
    this.codiceInps = codiceInps;
  }

  public String getMatricolaInail()
  {
    return matricolaInail;
  }

  public void setMatricolaInail(String matricolaInail)
  {
    this.matricolaInail = matricolaInail;
  }
  
  
}