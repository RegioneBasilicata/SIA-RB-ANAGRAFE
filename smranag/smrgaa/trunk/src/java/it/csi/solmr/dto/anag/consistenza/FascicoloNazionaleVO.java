package it.csi.solmr.dto.anag.consistenza;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_FASCICOLO_NAZIONALE_BACKUP
 * 
 * @author TOBECONFIG
 *
 */
public class FascicoloNazionaleVO implements Serializable {
	
	/**
   * 
   */
  private static final long serialVersionUID = 6015851351636817112L;
  
  
  
  
  private String codErroreFascicolo;
	private String msgErroreFascicolo;
	private String codErroreConsistenza;
  private String msgErroreConsistenza;
  private String codErroreUV;
  private String msgErroreUV;
  private String codErroreFabbricati;
  private String msgErroreFabbricati;
  private String codErroreCC;
  private String msgErroreCC;
  
  
  
  
  public String getCodErroreFascicolo()
  {
    return codErroreFascicolo;
  }
  public void setCodErroreFascicolo(String codErroreFascicolo)
  {
    this.codErroreFascicolo = codErroreFascicolo;
  }
  public String getMsgErroreFascicolo()
  {
    return msgErroreFascicolo;
  }
  public void setMsgErroreFascicolo(String msgErroreFascicolo)
  {
    this.msgErroreFascicolo = msgErroreFascicolo;
  }
  public String getCodErroreConsistenza()
  {
    return codErroreConsistenza;
  }
  public void setCodErroreConsistenza(String codErroreConsistenza)
  {
    this.codErroreConsistenza = codErroreConsistenza;
  }
  public String getMsgErroreConsistenza()
  {
    return msgErroreConsistenza;
  }
  public void setMsgErroreConsistenza(String msgErroreConsistenza)
  {
    this.msgErroreConsistenza = msgErroreConsistenza;
  }
  public String getCodErroreUV()
  {
    return codErroreUV;
  }
  public void setCodErroreUV(String codErroreUV)
  {
    this.codErroreUV = codErroreUV;
  }
  public String getMsgErroreUV()
  {
    return msgErroreUV;
  }
  public void setMsgErroreUV(String msgErroreUV)
  {
    this.msgErroreUV = msgErroreUV;
  }
  public String getCodErroreFabbricati()
  {
    return codErroreFabbricati;
  }
  public void setCodErroreFabbricati(String codErroreFabbricati)
  {
    this.codErroreFabbricati = codErroreFabbricati;
  }
  public String getMsgErroreFabbricati()
  {
    return msgErroreFabbricati;
  }
  public void setMsgErroreFabbricati(String msgErroreFabbricati)
  {
    this.msgErroreFabbricati = msgErroreFabbricati;
  }
  public String getCodErroreCC()
  {
    return codErroreCC;
  }
  public void setCodErroreCC(String codErroreCC)
  {
    this.codErroreCC = codErroreCC;
  }
  public String getMsgErroreCC()
  {
    return msgErroreCC;
  }
  public void setMsgErroreCC(String msgErroreCC)
  {
    this.msgErroreCC = msgErroreCC;
  }
	
	
	
	

	
	
			
}