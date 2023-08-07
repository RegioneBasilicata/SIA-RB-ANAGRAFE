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

public class SoggettoAziendaNuovaVO implements Serializable
{  
 
 
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 5112138225973593269L;
  
  
  
  private Long idSoggettiAziendaNuova;
  private Long idAziendaNuova;
  private String codiceFiscale;
  private String cognome;
  private String nome;
  private String istatComune;
  private String desCom;
  private String istatProvincia;
  private String sglProv;
  private String indirizzo;
  private String telefono;
  private String email;
  private Integer idRuolo;
  private Date dataInizioRuolo;
  private Date dataFineRuolo;
  private String cap;
  private String descTipoRuolo;
  
  
  
  public Long getIdSoggettiAziendaNuova()
  {
    return idSoggettiAziendaNuova;
  }
  public void setIdSoggettiAziendaNuova(Long idSoggettiAziendaNuova)
  {
    this.idSoggettiAziendaNuova = idSoggettiAziendaNuova;
  }
  public Long getIdAziendaNuova()
  {
    return idAziendaNuova;
  }
  public void setIdAziendaNuova(Long idAziendaNuova)
  {
    this.idAziendaNuova = idAziendaNuova;
  }
  public String getCodiceFiscale()
  {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale)
  {
    this.codiceFiscale = codiceFiscale;
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
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public String getDesCom()
  {
    return desCom;
  }
  public void setDesCom(String desCom)
  {
    this.desCom = desCom;
  }
  public String getIstatProvincia()
  {
    return istatProvincia;
  }
  public void setIstatProvincia(String istatProvincia)
  {
    this.istatProvincia = istatProvincia;
  }
  public String getSglProv()
  {
    return sglProv;
  }
  public void setSglProv(String sglProv)
  {
    this.sglProv = sglProv;
  }
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }
  public String getTelefono()
  {
    return telefono;
  }
  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }
  public String getEmail()
  {
    return email;
  }
  public void setEmail(String email)
  {
    this.email = email;
  }
  public Integer getIdRuolo()
  {
    return idRuolo;
  }
  public void setIdRuolo(Integer idRuolo)
  {
    this.idRuolo = idRuolo;
  }
  public Date getDataInizioRuolo()
  {
    return dataInizioRuolo;
  }
  public void setDataInizioRuolo(Date dataInizioRuolo)
  {
    this.dataInizioRuolo = dataInizioRuolo;
  }
  public Date getDataFineRuolo()
  {
    return dataFineRuolo;
  }
  public void setDataFineRuolo(Date dataFineRuolo)
  {
    this.dataFineRuolo = dataFineRuolo;
  }
  public String getCap()
  {
    return cap;
  }
  public void setCap(String cap)
  {
    this.cap = cap;
  }
  public String getDescTipoRuolo()
  {
    return descTipoRuolo;
  }
  public void setDescTipoRuolo(String descTipoRuolo)
  {
    this.descTipoRuolo = descTipoRuolo;
  }
  
  
  
  
  
  
  
  
}
