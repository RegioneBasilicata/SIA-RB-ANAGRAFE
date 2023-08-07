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

public class RichiestaAziendaVO implements Serializable
{  

  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 4332514875038738189L;
  
  
  
  private Long idRichiestaAzienda;
  private Long idTipoRichiesta;
  private Long idAziendaNuova;
  private Long idAzienda;
  private String codiceEnte;
  private Date dataAggiornamento;
  private Long idUtenteAggiornamento;
  private String note;
  private byte[] fileStampa;
  private Integer idMotivoRichiesta;
  private Integer idMotivoDichiarazione;
  private Long extIdDocumentoIndex;
  private Long idCessazione;
  private Date dataCessazione;
  private String cuaaSubentrante;
  private String denominazioneSubentrante;
  private Long idAziendaSubentrante;
  private String flagSoloAggiunta;
  
  public Long getIdRichiestaAzienda()
  {
    return idRichiestaAzienda;
  }
  public void setIdRichiestaAzienda(Long idRichiestaAzienda)
  {
    this.idRichiestaAzienda = idRichiestaAzienda;
  }
  public Long getIdTipoRichiesta()
  {
    return idTipoRichiesta;
  }
  public void setIdTipoRichiesta(Long idTipoRichiesta)
  {
    this.idTipoRichiesta = idTipoRichiesta;
  }
  public Long getIdAziendaNuova()
  {
    return idAziendaNuova;
  }
  public void setIdAziendaNuova(Long idAziendaNuova)
  {
    this.idAziendaNuova = idAziendaNuova;
  }
  public Long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public String getCodiceEnte()
  {
    return codiceEnte;
  }
  public void setCodiceEnte(String codiceEnte)
  {
    this.codiceEnte = codiceEnte;
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
  public byte[] getFileStampa()
  {
    return fileStampa;
  }
  public void setFileStampa(byte[] fileStampa)
  {
    this.fileStampa = fileStampa;
  }
  public Integer getIdMotivoRichiesta()
  {
    return idMotivoRichiesta;
  }
  public void setIdMotivoRichiesta(Integer idMotivoRichiesta)
  {
    this.idMotivoRichiesta = idMotivoRichiesta;
  }
  public Integer getIdMotivoDichiarazione()
  {
    return idMotivoDichiarazione;
  }
  public void setIdMotivoDichiarazione(Integer idMotivoDichiarazione)
  {
    this.idMotivoDichiarazione = idMotivoDichiarazione;
  }
  public Long getExtIdDocumentoIndex()
  {
    return extIdDocumentoIndex;
  }
  public void setExtIdDocumentoIndex(Long extIdDocumentoIndex)
  {
    this.extIdDocumentoIndex = extIdDocumentoIndex;
  }
  public Long getIdCessazione()
  {
    return idCessazione;
  }
  public void setIdCessazione(Long idCessazione)
  {
    this.idCessazione = idCessazione;
  }
  public Date getDataCessazione()
  {
    return dataCessazione;
  }
  public void setDataCessazione(Date dataCessazione)
  {
    this.dataCessazione = dataCessazione;
  }
  public String getCuaaSubentrante()
  {
    return cuaaSubentrante;
  }
  public void setCuaaSubentrante(String cuaaSubentrante)
  {
    this.cuaaSubentrante = cuaaSubentrante;
  }
  public String getDenominazioneSubentrante()
  {
    return denominazioneSubentrante;
  }
  public void setDenominazioneSubentrante(String denominazioneSubentrante)
  {
    this.denominazioneSubentrante = denominazioneSubentrante;
  }
  public Long getIdAziendaSubentrante()
  {
    return idAziendaSubentrante;
  }
  public void setIdAziendaSubentrante(Long idAziendaSubentrante)
  {
    this.idAziendaSubentrante = idAziendaSubentrante;
  }
  public String getFlagSoloAggiunta()
  {
    return flagSoloAggiunta;
  }
  public void setFlagSoloAggiunta(String flagSoloAggiunta)
  {
    this.flagSoloAggiunta = flagSoloAggiunta;
  }
  
  
  
  
}
