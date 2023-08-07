package it.csi.smranag.smrgaa.dto.nuovaiscrizione;

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

public class MotivoRichiestaVO implements Serializable
{  
 
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -7098598080205808049L;
  
  
  private Integer idMotivoRichiesta;
  private String descrizione;
  private String noteObbligatorie;
  private Integer idTipoRichiesta;
  
  
  
  
  public Integer getIdMotivoRichiesta()
  {
    return idMotivoRichiesta;
  }
  public void setIdMotivoRichiesta(Integer idMotivoRichiesta)
  {
    this.idMotivoRichiesta = idMotivoRichiesta;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getNoteObbligatorie()
  {
    return noteObbligatorie;
  }
  public void setNoteObbligatorie(String noteObbligatorie)
  {
    this.noteObbligatorie = noteObbligatorie;
  }
  public Integer getIdTipoRichiesta()
  {
    return idTipoRichiesta;
  }
  public void setIdTipoRichiesta(Integer idTipoRichiesta)
  {
    this.idTipoRichiesta = idTipoRichiesta;
  }
  
  
  
  
}
