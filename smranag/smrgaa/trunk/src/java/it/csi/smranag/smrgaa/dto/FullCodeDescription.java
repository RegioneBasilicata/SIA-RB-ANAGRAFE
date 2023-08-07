package it.csi.smranag.smrgaa.dto;

import java.util.Date;

/**
 * Estende BaseCodeDescription aggiungendo alcuni campi che possono essere
 * utili nel mapping di tabelle di decodifica estese.
 * Oct 22, 2008
 * @author TOBECONFIG
 *
 */
public class FullCodeDescription extends BaseCodeDescription
{
  /** serialVersionUID */
  private static final long serialVersionUID = -7240989320034861154L;
  /** Data inizio della decodifica (per le tabelle che lo prevedono) */
  private Date              dataInizio;
  /** Data fine della decodifica (per le tabelle che lo prevedono) */
  private Date              dataFine;

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public Date getDataFine()
  {
    return dataFine;
  }

  public void setDataFine(Date dataFine)
  {
    this.dataFine = dataFine;
  }
}
