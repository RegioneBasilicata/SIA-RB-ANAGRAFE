package it.csi.smranag.smrgaa.dto.search.paginazione;

import java.io.Serializable;

public class PaginazioneGruppo implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 8808052408718839367L;
  
  
  public static final String CLASS_GRUPPO = "gruppo";
  private int                idx;
  private boolean            sinistra;
  private boolean            gruppo;
  private long value;

  public PaginazioneGruppo(int idx, long value, boolean sinistra, boolean gruppo)
  {
    this.idx = idx;
    this.sinistra = sinistra;
    this.gruppo = gruppo;
    this.value=value;
  }

  public int getIdx()
  {
    return idx;
  }

  public void setIdx(int idx)
  {
    this.idx = idx;
  }

  public boolean isSinistra()
  {
    return sinistra;
  }

  public void setSinistra(boolean sinistra)
  {
    this.sinistra = sinistra;
  }

  public boolean isGruppo()
  {
    return gruppo;
  }

  public void setGruppo(boolean gruppo)
  {
    this.gruppo = gruppo;
  }

  public long getValue()
  {
    return value;
  }

  public void setValue(long value)
  {
    this.value = value;
  }

}