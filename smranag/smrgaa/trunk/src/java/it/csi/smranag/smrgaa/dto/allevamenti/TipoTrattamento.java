package it.csi.smranag.smrgaa.dto.allevamenti;

import java.io.Serializable;

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
public class TipoTrattamento implements Serializable
{
  /**
   * serial version UID
   */
  private static final long serialVersionUID = 4204060856557684618L;

  private long    idTrattamento;    // DB_TIPO_TRATTAMENTO.ID_TRATTAMENTO
  private String  descrizione;      // DB_TIPO_TRATTAMENTO.DESCRIZIONE
  private double  percVolumeSolido; // DB_TIPO_TRATTAMENTO.PERC_VOLUME_SOLIDO
  private double  percAzotoSolido;  // DB_TIPO_TRATTAMENTO.PERC_AZOTO_SOLIDO
  private String  note;             // DB_TIPO_TRATTAMENTO.NOTE
  private String  codiceSiap;       // DB_TIPO_TRATTAMENTO.CODICE_SIAP
  private double  percAzotoVolatile; // DB_TIPO_TRATTAMENTO.PERC_AZOTO_VOLATILE
  private boolean flagCalcolo;       // DB_TIPO_TRATTAMENTO.FLAG_CALCOLO

  public long getIdTrattamento()
  {
    return idTrattamento;
  }

  public void setIdTrattamento(long idTrattamento)
  {
    this.idTrattamento = idTrattamento;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public double getPercVolumeSolido()
  {
    return percVolumeSolido;
  }

  public void setPercVolumeSolido(double percVolumeSolido)
  {
    this.percVolumeSolido = percVolumeSolido;
  }

  public double getPercAzotoSolido()
  {
    return percAzotoSolido;
  }

  public void setPercAzotoSolido(double percAzotoSolido)
  {
    this.percAzotoSolido = percAzotoSolido;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getCodiceSiap()
  {
    return codiceSiap;
  }

  public void setCodiceSiap(String codiceSiap)
  {
    this.codiceSiap = codiceSiap;
  }

  public double getPercAzotoVolatile()
  {
    return percAzotoVolatile;
  }

  public void setPercAzotoVolatile(double percAzotoVolatile)
  {
    this.percAzotoVolatile = percAzotoVolatile;
  }

  public boolean isFlagCalcolo()
  {
    return flagCalcolo;
  }

  public void setFlagCalcolo(boolean flagCalcolo)
  {
    this.flagCalcolo = flagCalcolo;
  }

}
