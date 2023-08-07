package it.csi.smranag.smrgaa.dto.nuovaiscrizione;

import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

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

public class RichiestaAziendaDocumentoVO implements Serializable
{  
 
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -6708129328269506595L;
  
  
  private Long idRichiestaAziendaDocumento;
  private Long idRichiestaAzienda;
  private Long extIdDocumento;
  private String descDocumento;
  private Long idUtenteAggiornamento;
  private Date dataAggiornamento;
  private String note;
  private Vector<AllegatoDocumentoVO> vAllegatoDocumento;
  private String numeroDocumento;
  private String enteRilascioDocumento;
  private Date dataInizioValidita;
  private Date dataFineValidita;
  
  
  public Long getIdRichiestaAziendaDocumento()
  {
    return idRichiestaAziendaDocumento;
  }
  public void setIdRichiestaAziendaDocumento(Long idRichiestaAziendaDocumento)
  {
    this.idRichiestaAziendaDocumento = idRichiestaAziendaDocumento;
  }
  public Long getIdRichiestaAzienda()
  {
    return idRichiestaAzienda;
  }
  public void setIdRichiestaAzienda(Long idRichiestaAzienda)
  {
    this.idRichiestaAzienda = idRichiestaAzienda;
  }
  public Long getExtIdDocumento()
  {
    return extIdDocumento;
  }
  public void setExtIdDocumento(Long extIdDocumento)
  {
    this.extIdDocumento = extIdDocumento;
  }
  public Long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }
  public void setIdUtenteAggiornamento(Long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public String getDescDocumento()
  {
    return descDocumento;
  }
  public void setDescDocumento(String descDocumento)
  {
    this.descDocumento = descDocumento;
  }
  public Vector<AllegatoDocumentoVO> getvAllegatoDocumento()
  {
    return vAllegatoDocumento;
  }
  public void setvAllegatoDocumento(Vector<AllegatoDocumentoVO> vAllegatoDocumento)
  {
    this.vAllegatoDocumento = vAllegatoDocumento;
  }
  public String getNumeroDocumento()
  {
    return numeroDocumento;
  }
  public void setNumeroDocumento(String numeroDocumento)
  {
    this.numeroDocumento = numeroDocumento;
  }
  public String getEnteRilascioDocumento()
  {
    return enteRilascioDocumento;
  }
  public void setEnteRilascioDocumento(String enteRilascioDocumento)
  {
    this.enteRilascioDocumento = enteRilascioDocumento;
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
  
  
  
  
}
