package it.csi.solmr.dto.anag.services;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author: Mauro Vocale
 * @version 1.0
 */
import java.io.Serializable;

// Questo VO si occupa di codificare l'XML proveniente dalla servlet che richiama il servizio
// "anagrafica sintetica" del SIAN
public class SianAnagTributariaAtecoSecGaaVO implements Serializable 
{

  

  /**
   * 
   */
  private static final long serialVersionUID = 3012451726003034136L;
  
  
  
  // Variabili globali usate
  private long idAziendaTributaria;
  private long idAtecoSecTributaria;
  private Long   idAttivitaAteco;
  private String codiceAteco;
  private String descAttivitaAteco;
  
  
  
  public long getIdAziendaTributaria()
  {
    return idAziendaTributaria;
  }
  public void setIdAziendaTributaria(long idAziendaTributaria)
  {
    this.idAziendaTributaria = idAziendaTributaria;
  }
  public long getIdAtecoSecTributaria()
  {
    return idAtecoSecTributaria;
  }
  public void setIdAtecoSecTributaria(long idAtecoSecTributaria)
  {
    this.idAtecoSecTributaria = idAtecoSecTributaria;
  }
  public Long getIdAttivitaAteco()
  {
    return idAttivitaAteco;
  }
  public void setIdAttivitaAteco(Long idAttivitaAteco)
  {
    this.idAttivitaAteco = idAttivitaAteco;
  }
  public String getCodiceAteco()
  {
    return codiceAteco;
  }
  public void setCodiceAteco(String codiceAteco)
  {
    this.codiceAteco = codiceAteco;
  }
  public String getDescAttivitaAteco()
  {
    return descAttivitaAteco;
  }
  public void setDescAttivitaAteco(String descAttivitaAteco)
  {
    this.descAttivitaAteco = descAttivitaAteco;
  }
  

  
  
  
  
}
