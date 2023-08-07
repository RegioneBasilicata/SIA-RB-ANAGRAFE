package it.csi.solmr.dto.anag;
/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author
 * @version 1.0
 */


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProprietaCertificataVO implements Serializable
{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 1258883897494124780L;
  
  
  private Long idProprietaCertificata;
  private String cuaa;
  private Long idSoggetto;
  private Long idTipoDiritto;
  private BigDecimal percentualePossesso;
  private Date dataInizioTitolarita;
  private Long idParticellaCertificata;
  private Date dataAggiornamento;
  private Long idTitoloPossesso;
  private String nome;
  private String denominazione;
  private String cognome;
  private String descDiritto;
  private String codiceDiritto;
  private String descUtenteAggiornamento;
  private String descFonte;
  
  
  
  
  public Long getIdProprietaCertificata()
  {
    return idProprietaCertificata;
  }
  public void setIdProprietaCertificata(Long idProprietaCertificata)
  {
    this.idProprietaCertificata = idProprietaCertificata;
  }
  public String getCuaa()
  {
    return cuaa;
  }
  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }
  public Long getIdSoggetto()
  {
    return idSoggetto;
  }
  public void setIdSoggetto(Long idSoggetto)
  {
    this.idSoggetto = idSoggetto;
  }
  public Long getIdTipoDiritto()
  {
    return idTipoDiritto;
  }
  public void setIdTipoDiritto(Long idTipoDiritto)
  {
    this.idTipoDiritto = idTipoDiritto;
  }
  public BigDecimal getPercentualePossesso()
  {
    return percentualePossesso;
  }
  public void setPercentualePossesso(BigDecimal percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }
  public Date getDataInizioTitolarita()
  {
    return dataInizioTitolarita;
  }
  public void setDataInizioTitolarita(Date dataInizioTitolarita)
  {
    this.dataInizioTitolarita = dataInizioTitolarita;
  }
  public Long getIdParticellaCertificata()
  {
    return idParticellaCertificata;
  }
  public void setIdParticellaCertificata(Long idParticellaCertificata)
  {
    this.idParticellaCertificata = idParticellaCertificata;
  }
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }
  public Long getIdTitoloPossesso()
  {
    return idTitoloPossesso;
  }
  public void setIdTitoloPossesso(Long idTitoloPossesso)
  {
    this.idTitoloPossesso = idTitoloPossesso;
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
  public String getCognome()
  {
    return cognome;
  }
  public void setCognome(String cognome)
  {
    this.cognome = cognome;
  }
  public String getDescDiritto()
  {
    return descDiritto;
  }
  public void setDescDiritto(String descDiritto)
  {
    this.descDiritto = descDiritto;
  }
  public String getCodiceDiritto()
  {
    return codiceDiritto;
  }
  public void setCodiceDiritto(String codiceDiritto)
  {
    this.codiceDiritto = codiceDiritto;
  }
  public String getDescUtenteAggiornamento()
  {
    return descUtenteAggiornamento;
  }
  public void setDescUtenteAggiornamento(String descUtenteAggiornamento)
  {
    this.descUtenteAggiornamento = descUtenteAggiornamento;
  }
  public String getDescFonte()
  {
    return descFonte;
  }
  public void setDescFonte(String descFonte)
  {
    this.descFonte = descFonte;
  }
  
  
  
  
  
  
  
  
}
