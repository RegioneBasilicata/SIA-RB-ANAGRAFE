package it.csi.smranag.smrgaa.dto.nuovaiscrizione;

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

public class IterRichiestaAziendaVO implements Serializable
{    
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 5609459880011315867L;
  
  
  private Long idIterRichiestaAzienda;
  private Long idRichiestaAzienda;
  private Long idStatoRichiesta;
  private Date dataInizioValidita;
  private Date dataFineValidita;
  private Date dataAggiornamento;
  private Long idUtenteAggiornamento;
  private String note;
  private String flagMailNotifica;
  
  
  
  public Long getIdIterRichiestaAzienda()
  {
    return idIterRichiestaAzienda;
  }
  public void setIdIterRichiestaAzienda(Long idIterRichiestaAzienda)
  {
    this.idIterRichiestaAzienda = idIterRichiestaAzienda;
  }
  public Long getIdRichiestaAzienda()
  {
    return idRichiestaAzienda;
  }
  public void setIdRichiestaAzienda(Long idRichiestaAzienda)
  {
    this.idRichiestaAzienda = idRichiestaAzienda;
  }
  public Long getIdStatoRichiesta()
  {
    return idStatoRichiesta;
  }
  public void setIdStatoRichiesta(Long idStatoRichiesta)
  {
    this.idStatoRichiesta = idStatoRichiesta;
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
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }
  public Long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }
  public void setIdUtenteAggiornamento(Long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public String getFlagMailNotifica()
  {
    return flagMailNotifica;
  }
  public void setFlagMailNotifica(String flagMailNotifica)
  {
    this.flagMailNotifica = flagMailNotifica;
  }
  
  
}
