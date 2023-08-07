package it.csi.smranag.smrgaa.dto.manuali;

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

public class ManualeVO implements Serializable
{ 
  
  /**
   * 
   */
  private static final long serialVersionUID = -2547603087701987262L;
  
  
  
  private Date dataVersione; 
  private long idManuale;
  private String descManuale;
  private String descSezione;
  private Long idSezione;
  private String versione;
  private byte[] testoManuale;
  private String nomeFile; 
  
  public Date getDataVersione()
  {
    return dataVersione;
  }
  public void setDataVersione(Date dataVersione)
  {
    this.dataVersione = dataVersione;
  }
  public long getIdManuale()
  {
    return idManuale;
  }
  public void setIdManuale(long idManuale)
  {
    this.idManuale = idManuale;
  }
  public String getDescManuale()
  {
    return descManuale;
  }
  public void setDescManuale(String descManuale)
  {
    this.descManuale = descManuale;
  }
  public String getDescSezione()
  {
    return descSezione;
  }
  public void setDescSezione(String descSezione)
  {
    this.descSezione = descSezione;
  }
  public Long getIdSezione()
  {
    return idSezione;
  }
  public void setIdSezione(Long idSezione)
  {
    this.idSezione = idSezione;
  }
  public String getVersione()
  {
    return versione;
  }
  public void setVersione(String versione)
  {
    this.versione = versione;
  }
  public byte[] getTestoManuale()
  {
    return testoManuale;
  }
  public void setTestoManuale(byte[] testoManuale)
  {
    this.testoManuale = testoManuale;
  }
  public String getNomeFile()
  {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile)
  {
    this.nomeFile = nomeFile;
  }
  
  
  
  

}
