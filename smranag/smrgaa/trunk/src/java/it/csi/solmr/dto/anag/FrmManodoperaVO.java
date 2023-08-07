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

public class FrmManodoperaVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -8393614830188235857L;

  private String idManodopera = null;
  private String codiceINPS = null;
  private String matrciolaInail = null;
  private String numPersTempoPieno = null;
  private String numPersTempoParziale = null;
  private String numSalariatiAvventizi = null;
  private TipoFormaConduzioneVO tipoFormaConduzioneVO = null;
  private Date dataInizioValiditaDate = null;
  private String dataInizioValidita = null;
  private Date dataFineValiditaDate = null;
  private String dataFineValidita = null;


  public FrmManodoperaVO() {
  }

  public String getDump() {
    return null;
  }

  public ValidationErrors validate() {
    ValidationErrors errors = new ValidationErrors();

    return errors;
  }

  public String getIdManodopera() {
    return idManodopera;
  }

  public void setIdManodopera(String idManodopera) {
    this.idManodopera = idManodopera;
  }

  public Long getIdManodoperaLong()
  {
    try
    {
      return new Long(idManodopera);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setIdManodoperaLong(Long idManodopera)
  {
    this.idManodopera = idManodopera==null?null:idManodopera.toString();
  }

  public String getNumPersTempoPieno() {
    return numPersTempoPieno;
  }

  public void setNumPersTempoPieno(String numPersTempoPieno) {
    this.numPersTempoPieno = numPersTempoPieno;
  }

  public Long getNumPersTempoPienoLong()
  {
    try
    {
      return new Long(numPersTempoPieno);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setNumPersTempoPienoLong(Long numPersTempoPieno)
  {
    this.numPersTempoPieno = numPersTempoPieno==null?null:numPersTempoPieno.toString();
  }

  public String getNumPersTempoParz() {
    return numPersTempoParziale;
  }

  public void setNumPersTempoParz(String numPersTempoParz) {
    this.numPersTempoParziale = numPersTempoParz;
  }

  public Long getNumPersTempoParzLong()
  {
    try
    {
      return new Long(numPersTempoParziale);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setNumPersTempoParzLong(Long numPersTempoParziale)
  {
    this.numPersTempoParziale = numPersTempoParziale==null?null:numPersTempoParziale.toString();
  }

  public String getNumSalariatiAvventizi() {
    return numSalariatiAvventizi;
  }

  public void setNumSalariatiAvventizi(String numSalariatiAvventizi) {
    this.numSalariatiAvventizi = numSalariatiAvventizi;
  }

  public Long getNumSalariatiAvventiziLong()
  {
    try
    {
      return new Long(numSalariatiAvventizi);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setNumSalariatiAvventiziLong(Long numSalariatiAvventizi)
  {
    this.numSalariatiAvventizi = numSalariatiAvventizi==null?null:numSalariatiAvventizi.toString();
  }

  public TipoFormaConduzioneVO getTipoFormaConduzioneVO() {
    return tipoFormaConduzioneVO;
  }

  public void setTipoFormaConduzioneVO(TipoFormaConduzioneVO
                                       tipoFormaConduzioneVO) {
    this.tipoFormaConduzioneVO = tipoFormaConduzioneVO;
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

  public String getCodiceINPS()
  {
    return codiceINPS;
  }

  public void setCodiceINPS(String codiceINPS)
  {
    this.codiceINPS = codiceINPS;
  }

  public String getMatrciolaInail()
  {
    return matrciolaInail;
  }

  public void setMatrciolaInail(String matrciolaInail)
  {
    this.matrciolaInail = matrciolaInail;
  }
  
  
}