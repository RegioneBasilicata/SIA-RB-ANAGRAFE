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
public class SottoCategoriaAnimStab implements Serializable
{
  /**
   * serial version UID
   */
  private static final long serialVersionUID = 7211673080888071385L;

  private double coeffVolumeMax; // DB_SOTTOCATEGORIA_ANIM_STAB.COEFF_VOLUME_MAX
  private double coeffPesoMax;  // DB_SOTTOCATEGORIA_ANIM_STAB.COEFF_PESO_MAX
  private double coeffAzotoMax; // DB_SOTTOCATEGORIA_ANIM_STAB.COEFF_AZOTO_MAX
  private double coeffVolumeMin; // DB_SOTTOCATEGORIA_ANIM_STAB.COEFF_VOLUME_MIN
  private double coeffPesoMin;  // DB_SOTTOCATEGORIA_ANIM_STAB.COEFF_PESO_MIN
  private double coeffAzotoMin; // DB_SOTTOCATEGORIA_ANIM_STAB.COEFF_AZOTO_MIN
  private long idEffluente; //DB_TIPO_EFFLUENTE.ID_EFFLUENTE
  private double coeffM3TPua; // DB_TIPO_EFFLUENTE.COEFF_M3_T_PUA

  public double getCoeffVolumeMax()
  {
    return coeffVolumeMax;
  }

  public void setCoeffVolumeMax(double coeffVolumeMax)
  {
    this.coeffVolumeMax = coeffVolumeMax;
  }

  public double getCoeffPesoMax()
  {
    return coeffPesoMax;
  }

  public void setCoeffPesoMax(double coeffPesoMax)
  {
    this.coeffPesoMax = coeffPesoMax;
  }

  public double getCoeffAzotoMax()
  {
    return coeffAzotoMax;
  }

  public void setCoeffAzotoMax(double coeffAzotoMax)
  {
    this.coeffAzotoMax = coeffAzotoMax;
  }

  public double getCoeffVolumeMin()
  {
    return coeffVolumeMin;
  }

  public void setCoeffVolumeMin(double coeffVolumeMin)
  {
    this.coeffVolumeMin = coeffVolumeMin;
  }

  public double getCoeffPesoMin()
  {
    return coeffPesoMin;
  }

  public void setCoeffPesoMin(double coeffPesoMin)
  {
    this.coeffPesoMin = coeffPesoMin;
  }

  public double getCoeffAzotoMin()
  {
    return coeffAzotoMin;
  }

  public void setCoeffAzotoMin(double coeffAzotoMin)
  {
    this.coeffAzotoMin = coeffAzotoMin;
  }

  public long getIdEffluente()
  {
    return idEffluente;
  }

  public void setIdEffluente(long idEffluente)
  {
    this.idEffluente = idEffluente;
  }

  public double getCoeffM3TPua()
  {
    return coeffM3TPua;
  }

  public void setCoeffM3TPua(double coeffM3TPua)
  {
    this.coeffM3TPua = coeffM3TPua;
  }
  

}
