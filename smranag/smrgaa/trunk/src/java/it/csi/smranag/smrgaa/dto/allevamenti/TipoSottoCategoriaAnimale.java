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

public class TipoSottoCategoriaAnimale implements Serializable
{
  /**
   * serial version UID
   */
  private static final long serialVersionUID = 6790271017050275050L;

  private long              idSottocategoriaAnimale;                // DB_TIPO_SOTTOCATEGORIA_ANIMALE.ID_SOTTOCATEGORIA_ANIMALE
  private long              idCategoriaAnimale;                     // DB_TIPO_SOTTOCATEGORIA_ANIMALE.ID_CATEGORIA_ANIMALE
  private String            descrizione;                            // DB_TIPO_SOTTOCATEGORIA_ANIMALE.DESCRIZIONE
  private double            pesoVivoMedio;                          // DB_TIPO_SOTTOCATEGORIA_ANIMALE.PESO_VIVO_MEDIO
  private double            pesoVivoMin;                            // DB_TIPO_SOTTOCATEGORIA_ANIMALE.PESO_VIVO_MIN
  private double            pesoVivoMax;                            // DB_TIPO_SOTTOCATEGORIA_ANIMALE.PESO_VIVO_MAX
  private double            pesoVivoAzoto;                          // DB_TIPO_SOTTOCATEGORIA_ANIMALE.PESO_VIVO_AZOTO
  private Date              dataInizioValidita;                     // DB_TIPO_SOTTOCATEGORIA_ANIMALE.DATA_INIZIO_VALIDITA
  private Date              dataFineValidita;                       // DB_TIPO_SOTTOCATEGORIA_ANIMALE.DATA_FINE_VALIDITA
  private boolean           flagSottocatFittizia;                   // DB_TIPO_SOTTOCATEGORIA_ANIMALE.FLAG_SOTTOCAT_FITTIZIA
  private int               ggVuotoSanitario;                       // DB_TIPO_SOTTOCATEGORIA_ANIMALE.GIORNI_VUOTO_SANITARIO
  private int               ggDurataCiclo;                          // DB_TIPO_SOTTOCATEGORIA_ANIMALE.GIORNI_DURATA_CICLO

  public long getIdSottocategoriaAnimale()
  {
    return idSottocategoriaAnimale;
  }

  public void setIdSottocategoriaAnimale(long idSottocategoriaAnimale)
  {
    this.idSottocategoriaAnimale = idSottocategoriaAnimale;
  }

  public long getIdCategoriaAnimale()
  {
    return idCategoriaAnimale;
  }

  public void setIdCategoriaAnimale(long idCategoriaAnimale)
  {
    this.idCategoriaAnimale = idCategoriaAnimale;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public double getPesoVivoMedio()
  {
    return pesoVivoMedio;
  }

  public void setPesoVivoMedio(double pesoVivoMedio)
  {
    this.pesoVivoMedio = pesoVivoMedio;
  }

  public double getPesoVivoMin()
  {
    return pesoVivoMin;
  }

  public void setPesoVivoMin(double pesoVivoMin)
  {
    this.pesoVivoMin = pesoVivoMin;
  }

  public double getPesoVivoMax()
  {
    return pesoVivoMax;
  }

  public void setPesoVivoMax(double pesoVivoMax)
  {
    this.pesoVivoMax = pesoVivoMax;
  }

  public double getPesoVivoAzoto()
  {
    return pesoVivoAzoto;
  }

  public void setPesoVivoAzoto(double pesoVivoAzoto)
  {
    this.pesoVivoAzoto = pesoVivoAzoto;
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

  public boolean isFlagSottocatFittizia()
  {
    return flagSottocatFittizia;
  }

  public void setFlagSottocatFittizia(boolean flagSottocatFittizia)
  {
    this.flagSottocatFittizia = flagSottocatFittizia;
  }

  public int getGgVuotoSanitario()
  {
    return ggVuotoSanitario;
  }

  public void setGgVuotoSanitario(int ggVuotoSanitario)
  {
    this.ggVuotoSanitario = ggVuotoSanitario;
  }

  public int getGgDurataCiclo()
  {
    return ggDurataCiclo;
  }

  public void setGgDurataCiclo(int ggDurataCiclo)
  {
    this.ggDurataCiclo = ggDurataCiclo;
  }
}
