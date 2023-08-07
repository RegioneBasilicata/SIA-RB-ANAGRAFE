package it.csi.solmr.dto.anag;

import java.io.*;
import java.util.Date;

/**
 * Value Object che mappa la tabella DB_TIPO_SEZIONI_AAEP
 *
 * <p>Title: SMRGAA</p>
 *
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: CSI - PIEMONTE</p>
 *
 * @author TOBECONFIG
 * @version 1.0
 */
public class TipoSezioniAaepVO implements Serializable 
{
	
	
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -1287010546776867532L;
  
  
  private long idTipoSezioniAaep;
	private String codiceSezione;
	private String descrizione;
	private Date dataInizioValidita;
	
	
  public long getIdTipoSezioniAaep()
  {
    return idTipoSezioniAaep;
  }
  public void setIdTipoSezioniAaep(long idTipoSezioniAaep)
  {
    this.idTipoSezioniAaep = idTipoSezioniAaep;
  }
  public String getCodiceSezione()
  {
    return codiceSezione;
  }
  public void setCodiceSezione(String codiceSezione)
  {
    this.codiceSezione = codiceSezione;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
	
	
	
}
