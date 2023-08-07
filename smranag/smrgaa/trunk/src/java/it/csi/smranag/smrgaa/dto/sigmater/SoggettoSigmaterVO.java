package it.csi.smranag.smrgaa.dto.sigmater;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delal tabella DB_SOGGETTO_SIGMATER
 * 
 * @author TOBECONFIG
 *
 */
public class SoggettoSigmaterVO implements Serializable 
{
	
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -8013357155806538393L;
  
  
  
  
  
  private Long idSoggettoSigmater;
  private Long idRichiestaSigmater;
  private Long idSigmater;
  private String tipoSoggetto;  
  private String cognome;
  private String nome;
  private String denominazione;
  private String sesso;
  private Date dataNascita;
  private String luogoNascita;
  private String codiceFiscale;
  private Date dataAggiornamento;
  private String sede;
  
  
  
  
  public Long getIdSoggettoSigmater()
  {
    return idSoggettoSigmater;
  }
  public void setIdSoggettoSigmater(Long idSoggettoSigmater)
  {
    this.idSoggettoSigmater = idSoggettoSigmater;
  }
  public Long getIdRichiestaSigmater()
  {
    return idRichiestaSigmater;
  }
  public void setIdRichiestaSigmater(Long idRichiestaSigmater)
  {
    this.idRichiestaSigmater = idRichiestaSigmater;
  }
  public Long getIdSigmater()
  {
    return idSigmater;
  }
  public void setIdSigmater(Long idSigmater)
  {
    this.idSigmater = idSigmater;
  }
  public String getTipoSoggetto()
  {
    return tipoSoggetto;
  }
  public void setTipoSoggetto(String tipoSoggetto)
  {
    this.tipoSoggetto = tipoSoggetto;
  }
  public String getCognome()
  {
    return cognome;
  }
  public void setCognome(String cognome)
  {
    this.cognome = cognome;
  }
  public String getNome()
  {
    return nome;
  }
  public void setNome(String nome)
  {
    this.nome = nome;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public String getSesso()
  {
    return sesso;
  }
  public void setSesso(String sesso)
  {
    this.sesso = sesso;
  }
  public Date getDataNascita()
  {
    return dataNascita;
  }
  public void setDataNascita(Date dataNascita)
  {
    this.dataNascita = dataNascita;
  }
  public String getLuogoNascita()
  {
    return luogoNascita;
  }
  public void setLuogoNascita(String luogoNascita)
  {
    this.luogoNascita = luogoNascita;
  }
  public String getCodiceFiscale()
  {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale)
  {
    this.codiceFiscale = codiceFiscale;
  }
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }
  public String getSede()
  {
    return sede;
  }
  public void setSede(String sede)
  {
    this.sede = sede;
  }
  
  
  
  
  
  
  
}
