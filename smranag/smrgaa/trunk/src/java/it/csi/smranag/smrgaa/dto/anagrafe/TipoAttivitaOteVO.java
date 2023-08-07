package it.csi.smranag.smrgaa.dto.anagrafe;

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

public class TipoAttivitaOteVO implements Serializable
{ 
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 5881390283307425583L;
  
  
  
  
  
  private long idAttivitaOte;                  //DB_TIPO_ATTIVITA_OTE.ID_ATTIVITA_OTE
  private String descrizione;                  //DB_TIPO_ATTIVITA_OTE.DESCRIZIONE 
  private String codice;                       //DB_TIPO_ATTIVITA_OTE.CODICE 
  
  
  
  
  
  
  public long getIdAttivitaOte()
  {
    return idAttivitaOte;
  }
  public void setIdAttivitaOte(long idAttivitaOte)
  {
    this.idAttivitaOte = idAttivitaOte;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getCodice()
  {
    return codice;
  }
  public void setCodice(String codice)
  {
    this.codice = codice;
  }
  

  
  
  
  

  

}
