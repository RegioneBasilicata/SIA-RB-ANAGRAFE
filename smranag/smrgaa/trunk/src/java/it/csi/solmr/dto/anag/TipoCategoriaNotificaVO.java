package it.csi.solmr.dto.anag;

import java.io.*;

/**
 * Value Object che mappa la tabella DB_TIPO_CATEGORIA_NOTIFICA
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
public class TipoCategoriaNotificaVO implements Serializable 
{
	
	/**
   * 
   */
  private static final long serialVersionUID = -7193045024848804357L;
  
  
  private Integer idCategoriaNotifica = null;
	private Integer idTipologiaNotifica = null;
	private String descrizione = null;
	private Integer idTipoEntita = null;
	private String inviaEmail = null;
	
	
  public Integer getIdCategoriaNotifica()
  {
    return idCategoriaNotifica;
  }
  public void setIdCategoriaNotifica(Integer idCategoriaNotifica)
  {
    this.idCategoriaNotifica = idCategoriaNotifica;
  }
  public Integer getIdTipologiaNotifica()
  {
    return idTipologiaNotifica;
  }
  public void setIdTipologiaNotifica(Integer idTipologiaNotifica)
  {
    this.idTipologiaNotifica = idTipologiaNotifica;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public Integer getIdTipoEntita()
  {
    return idTipoEntita;
  }
  public void setIdTipoEntita(Integer idTipoEntita)
  {
    this.idTipoEntita = idTipoEntita;
  }
  public String getInviaEmail()
  {
    return inviaEmail;
  }
  public void setInviaEmail(String inviaEmail)
  {
    this.inviaEmail = inviaEmail;
  }
	
	
	
}
