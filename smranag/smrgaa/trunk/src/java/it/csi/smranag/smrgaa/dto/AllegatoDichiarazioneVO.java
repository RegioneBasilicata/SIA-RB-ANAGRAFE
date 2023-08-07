package it.csi.smranag.smrgaa.dto;

import java.util.Date;

import it.csi.solmr.util.ValueObject;



public class AllegatoDichiarazioneVO  implements ValueObject 
{
   
  /**
   * 
   */
  private static final long serialVersionUID = -349686159062781315L;
  
  private Long  idAllegatoDichiarazione;
  private Long  idAllegato;
  private Long  idDichiarazioneConsistenza;  
	private Long idTipoAllegato;
	private Long idTipoFirma;
	private String descTipoFirma;
	private String nomeFisico;
	private byte[] fileAllegato;
	private Long extIdDocumentoIndex;
	private Date dataUltimoAggiornamento;
	private String descTipoAllegato;
	private Date dataInizioValidita;
	private String flagInseribile;
	private Long idUtenteAggiornamento;
	private String queryAbilitazione;
	private String flagDaFirmare;
	private String stileFirma;
	private String descrizioneTipoFirma;
	
	
  public Long getIdAllegatoDichiarazione()
  {
    return idAllegatoDichiarazione;
  }
  public void setIdAllegatoDichiarazione(Long idAllegatoDichiarazione)
  {
    this.idAllegatoDichiarazione = idAllegatoDichiarazione;
  }
  public Long getIdAllegato()
  {
    return idAllegato;
  }
  public void setIdAllegato(Long idAllegato)
  {
    this.idAllegato = idAllegato;
  }
  public Long getIdDichiarazioneConsistenza()
  {
    return idDichiarazioneConsistenza;
  }
  public void setIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
  {
    this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
  }
  public Long getIdTipoAllegato()
  {
    return idTipoAllegato;
  }
  public void setIdTipoAllegato(Long idTipoAllegato)
  {
    this.idTipoAllegato = idTipoAllegato;
  }
  public String getNomeFisico()
  {
    return nomeFisico;
  }
  public void setNomeFisico(String nomeFisico)
  {
    this.nomeFisico = nomeFisico;
  }
  public byte[] getFileAllegato()
  {
    return fileAllegato;
  }
  public void setFileAllegato(byte[] fileAllegato)
  {
    this.fileAllegato = fileAllegato;
  }
  public Long getExtIdDocumentoIndex()
  {
    return extIdDocumentoIndex;
  }
  public void setExtIdDocumentoIndex(Long extIdDocumentoIndex)
  {
    this.extIdDocumentoIndex = extIdDocumentoIndex;
  }
  public Long getIdTipoFirma()
  {
    return idTipoFirma;
  }
  public void setIdTipoFirma(Long idTipoFirma)
  {
    this.idTipoFirma = idTipoFirma;
  }
  public String getDescTipoFirma()
  {
    return descTipoFirma;
  }
  public void setDescTipoFirma(String descTipoFirma)
  {
    this.descTipoFirma = descTipoFirma;
  }
  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }
  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }
  public String getDescTipoAllegato()
  {
    return descTipoAllegato;
  }
  public void setDescTipoAllegato(String descTipoAllegato)
  {
    this.descTipoAllegato = descTipoAllegato;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  public String getFlagInseribile()
  {
    return flagInseribile;
  }
  public void setFlagInseribile(String flagInseribile)
  {
    this.flagInseribile = flagInseribile;
  }
  public Long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }
  public void setIdUtenteAggiornamento(Long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }
  public String getQueryAbilitazione()
  {
    return queryAbilitazione;
  }
  public void setQueryAbilitazione(String queryAbilitazione)
  {
    this.queryAbilitazione = queryAbilitazione;
  }
  public String getFlagDaFirmare()
  {
    return flagDaFirmare;
  }
  public void setFlagDaFirmare(String flagDaFirmare)
  {
    this.flagDaFirmare = flagDaFirmare;
  }
  public String getStileFirma()
  {
    return stileFirma;
  }
  public void setStileFirma(String stileFirma)
  {
    this.stileFirma = stileFirma;
  }
  public String getDescrizioneTipoFirma()
  {
    return descrizioneTipoFirma;
  }
  public void setDescrizioneTipoFirma(String descrizioneTipoFirma)
  {
    this.descrizioneTipoFirma = descrizioneTipoFirma;
  }

	
  

}