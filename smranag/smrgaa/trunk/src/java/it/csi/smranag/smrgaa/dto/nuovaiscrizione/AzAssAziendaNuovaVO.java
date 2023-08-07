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

public class AzAssAziendaNuovaVO implements Serializable
{  
 
  
  /**
   * 
   */
  private static final long serialVersionUID = -4356971693728681498L;
  
  
  
  private Long idAssociateAzNuove;
  private Long idAziendaNuova;
  private Date dataIngresso;
  private Date dataUscita;
  private Long idSoggAssAzNuova;
  private Long idAziendaAssociata;
  private String codEnte;
  private Long idSoggettoAssociato;
  private Long idAziendaCollegata;
  private Long idRichiestaAzienda;
  private String flagEliminato;
  
  //Tabella SMRGAA_W_SOGG_ASS_AZ_NUOVA
  private String cuaa;
  private String denominazione;
  private String partitaIva;
  private String indirizzo;
  private String istatComune;
  private String cap;
  private String desCom;
  private String sglProv;
  
  
  public Long getIdAssociateAzNuove()
  {
    return idAssociateAzNuove;
  }
  public void setIdAssociateAzNuove(Long idAssociateAzNuove)
  {
    this.idAssociateAzNuove = idAssociateAzNuove;
  }
  public Long getIdAziendaNuova()
  {
    return idAziendaNuova;
  }
  public void setIdAziendaNuova(Long idAziendaNuova)
  {
    this.idAziendaNuova = idAziendaNuova;
  }
  public Date getDataIngresso()
  {
    return dataIngresso;
  }
  public void setDataIngresso(Date dataIngresso)
  {
    this.dataIngresso = dataIngresso;
  }
  public Long getIdSoggAssAzNuova()
  {
    return idSoggAssAzNuova;
  }
  public void setIdSoggAssAzNuova(Long idSoggAssAzNuova)
  {
    this.idSoggAssAzNuova = idSoggAssAzNuova;
  }
  public Long getIdAziendaAssociata()
  {
    return idAziendaAssociata;
  }
  public void setIdAziendaAssociata(Long idAziendaAssociata)
  {
    this.idAziendaAssociata = idAziendaAssociata;
  }
  public String getCodEnte()
  {
    return codEnte;
  }
  public void setCodEnte(String codEnte)
  {
    this.codEnte = codEnte;
  }
  public String getCuaa()
  {
    return cuaa;
  }
  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public String getPartitaIva()
  {
    return partitaIva;
  }
  public void setPartitaIva(String partitaIva)
  {
    this.partitaIva = partitaIva;
  }
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public String getCap()
  {
    return cap;
  }
  public void setCap(String cap)
  {
    this.cap = cap;
  }
  public String getDesCom()
  {
    return desCom;
  }
  public void setDesCom(String desCom)
  {
    this.desCom = desCom;
  }
  public String getSglProv()
  {
    return sglProv;
  }
  public void setSglProv(String sglProv)
  {
    this.sglProv = sglProv;
  }
  public Long getIdSoggettoAssociato()
  {
    return idSoggettoAssociato;
  }
  public void setIdSoggettoAssociato(Long idSoggettoAssociato)
  {
    this.idSoggettoAssociato = idSoggettoAssociato;
  }
  public Long getIdAziendaCollegata()
  {
    return idAziendaCollegata;
  }
  public void setIdAziendaCollegata(Long idAziendaCollegata)
  {
    this.idAziendaCollegata = idAziendaCollegata;
  }
  public Long getIdRichiestaAzienda()
  {
    return idRichiestaAzienda;
  }
  public void setIdRichiestaAzienda(Long idRichiestaAzienda)
  {
    this.idRichiestaAzienda = idRichiestaAzienda;
  }
  public Date getDataUscita()
  {
    return dataUscita;
  }
  public void setDataUscita(Date dataUscita)
  {
    this.dataUscita = dataUscita;
  }
  public String getFlagEliminato()
  {
    return flagEliminato;
  }
  public void setFlagEliminato(String flagEliminato)
  {
    this.flagEliminato = flagEliminato;
  }
  
  
  
  
  
  
}
