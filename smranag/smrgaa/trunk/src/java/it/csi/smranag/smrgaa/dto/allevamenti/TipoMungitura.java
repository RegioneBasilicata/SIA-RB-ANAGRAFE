package it.csi.smranag.smrgaa.dto.allevamenti;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 0.1
 */

public class TipoMungitura implements Serializable
{
  /**
   * serial version UID
   */
  private static final long serialVersionUID = -5658134091141758760L;

  private long              idMungitura;                             // DB_TIPO_MUNGITURA.ID_MUNGITURA
  private String            descrizione;                             // DB_TIPO_MUNGITURA.DESCRIZIONE
  private double            coeffVolumeLavaggio;                     // DB_TIPO_MUNGITURA.COEFF_VOLUME_LAVAGGIO
  private Date              dataInizioValidita;                      // DB_TIPO_MUNGITURA.DATA_INIZIO_VALIDITA
  private Date              dataFineValidita;                        // DB_TIPO_MUNGITURA.DATA_FINE_VALIDITA

  public long getIdMungitura()
  {
    return idMungitura;
  }

  public void setIdMungitura(long idMungitura)
  {
    this.idMungitura = idMungitura;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public double getCoeffVolumeLavaggio()
  {
    return coeffVolumeLavaggio;
  }

  public void setCoeffVolumeLavaggio(double coeffVolumeLavaggio)
  {
    this.coeffVolumeLavaggio = coeffVolumeLavaggio;
  }

  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }

  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }

  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }

  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }

}
