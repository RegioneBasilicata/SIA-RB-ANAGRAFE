package it.csi.solmr.dto.anag;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Monica Di Marco
 * @version 1.0
 */

import java.io.Serializable;
import java.util.Date;

public class IntermediarioAnagVO implements Serializable 
{
	
	
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -4813238518393209546L;
  
  
  
  
  private long idIntermediario;
	private String denominazione;
	private String codiceFiscale;
	private String indirizzo;
	private String cap;
	private String comune;
	private String tipoIntermediario;
	private Long idIntermediarioPadre;
	private String livello;
	private String partitaIva;
	private Date dataFineValidita;
	private String responsabile;
	private String telefono;
	private String fax;
	private String email;
	private String pec;
	private String desCom;
	private String sglProv;
	private Long extIdAzienda;
	private String extCuaa;
	
	
	
	
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
  public long getIdIntermediario()
  {
    return idIntermediario;
  }
  public void setIdIntermediario(long idIntermediario)
  {
    this.idIntermediario = idIntermediario;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public String getCodiceFiscale()
  {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale)
  {
    this.codiceFiscale = codiceFiscale;
  }
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }
  public String getCap()
  {
    return cap;
  }
  public void setCap(String cap)
  {
    this.cap = cap;
  }
  public String getComune()
  {
    return comune;
  }
  public void setComune(String comune)
  {
    this.comune = comune;
  }
  public String getTipoIntermediario()
  {
    return tipoIntermediario;
  }
  public void setTipoIntermediario(String tipoIntermediario)
  {
    this.tipoIntermediario = tipoIntermediario;
  }
  public Long getIdIntermediarioPadre()
  {
    return idIntermediarioPadre;
  }
  public void setIdIntermediarioPadre(Long idIntermediarioPadre)
  {
    this.idIntermediarioPadre = idIntermediarioPadre;
  }
  public String getLivello()
  {
    return livello;
  }
  public void setLivello(String livello)
  {
    this.livello = livello;
  }
  public String getPartitaIva()
  {
    return partitaIva;
  }
  public void setPartitaIva(String partitaIva)
  {
    this.partitaIva = partitaIva;
  }
  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }
  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }
  public String getResponsabile()
  {
    return responsabile;
  }
  public void setResponsabile(String responsabile)
  {
    this.responsabile = responsabile;
  }
  public String getTelefono()
  {
    return telefono;
  }
  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }
  public String getFax()
  {
    return fax;
  }
  public void setFax(String fax)
  {
    this.fax = fax;
  }
  public String getEmail()
  {
    return email;
  }
  public void setEmail(String email)
  {
    this.email = email;
  }
  public String getPec()
  {
    return pec;
  }
  public void setPec(String pec)
  {
    this.pec = pec;
  }
  public Long getExtIdAzienda()
  {
    return extIdAzienda;
  }
  public void setExtIdAzienda(Long extIdAzienda)
  {
    this.extIdAzienda = extIdAzienda;
  }
  public String getExtCuaa()
  {
    return extCuaa;
  }
  public void setExtCuaa(String extCuaa)
  {
    this.extCuaa = extCuaa;
  }
	

}
