package it.csi.solmr.dto.anag;

import java.io.*;

/**
 * Value Object che mappa la tabella DB_TIPO_ISCTIZIONE_INPS
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
public class TipoIscrizioneINPSVO implements Serializable 
{
	
	
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -5178708851761054663L;
  
  
  
  private int idTipoIscrizioneINPS = 0;
	private String codiceTipoIscrizione = null;
	private String descrizione = null;
	
	
  public int getIdTipoIscrizioneINPS()
  {
    return idTipoIscrizioneINPS;
  }
  public void setIdTipoIscrizioneINPS(int idTipoIscrizioneINPS)
  {
    this.idTipoIscrizioneINPS = idTipoIscrizioneINPS;
  }
  public String getCodiceTipoIscrizione()
  {
    return codiceTipoIscrizione;
  }
  public void setCodiceTipoIscrizione(String codiceTipoIscrizione)
  {
    this.codiceTipoIscrizione = codiceTipoIscrizione;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
	
	
  
	
	
	
}
