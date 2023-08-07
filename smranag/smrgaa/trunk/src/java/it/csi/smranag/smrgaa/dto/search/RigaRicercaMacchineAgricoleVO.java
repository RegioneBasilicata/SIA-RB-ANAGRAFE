package it.csi.smranag.smrgaa.dto.search;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.dto.search.paginazione.IPaginazione;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.Validator;

import java.text.MessageFormat;
import java.util.Date;

/**
 * Rappresenta una riga dei risultati di una ricerca macchine.
 * 
 * @author TOBECONFIG (Matr. 71646)
 * 
 */
public class RigaRicercaMacchineAgricoleVO implements IPaginazione
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -2141177022882022892L;
  
  
  private String htmlStringKO = "<img src=\"{0}\" "+
      "title=\"{1}\" border=\"0\"></a>";
  
  
  private long idPossessoMacchina;
  private Long idMacchina;
  private String comune;
  private String sglProvincia;
  private String indirizzo;  
  private String descTipoMacchina;
  private String descTipoGenereMacchina;
  private String descTipoCategoriaMacchina;
  private String descTipoMarca;
  private String descModello;
  private String numeroTarga;
  private String matricolaTelaio;
  private Integer annoCostruzione;  
  private Date dataCarico;
  private Date dataScarico;
  private boolean checked;
  private String cuaa;
  private String denominazione;
  private String flagValida;
  
  private String imgConfermata;
  
  
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(" Tipologia: ").append(this.getDescTipoMacchina());
    sb.append(" Genere: ").append(this.getDescTipoGenereMacchina());
    
    return sb.toString();
  }
  
  public void scriviRiga(Htmpl htmpl, String blk, String id[])
  {
    scriviRiga(htmpl, blk);
  }

  public void scriviRiga(Htmpl htmpl, String blk)
  {
   
    htmpl.set(blk+".idPossessoMacchina", ""+this.getIdPossessoMacchina());    
    
    if(checked)
    {
      htmpl.set(blk + ".checked", "checked=\"checked\"", null);
    }
    
    String ute = this.getComune()+" ("+this.getSglProvincia()+") "+this.getIndirizzo();    
    htmpl.set(blk + ".descUte", ute);
    String descAzienda = this.getCuaa()+" - "+this.getDenominazione();
    htmpl.set(blk + ".descAzienda", descAzienda);    
    htmpl.set(blk + ".descTipoMacchina", this.descTipoMacchina);
    htmpl.set(blk + ".descTipoGenereMacchina",this.descTipoGenereMacchina);
    htmpl.set(blk + ".descTipoCategoriaMacchina",this.descTipoCategoriaMacchina);
    htmpl.set(blk + ".descTipoMarca",this.descTipoMarca);
    htmpl.set(blk + ".descModello",this.descModello);
    htmpl.set(blk + ".numeroTarga",this.numeroTarga);
    htmpl.set(blk + ".matricolaTelaio",this.matricolaTelaio);
    if(Validator.isNotEmpty(this.annoCostruzione))
      htmpl.set(blk + ".annoCostruzione",""+this.annoCostruzione);
    htmpl.set(blk + ".dataCarico", DateUtils.formatDateNotNull(this.dataCarico));
    htmpl.set(blk + ".dataScarico", DateUtils.formatDateNotNull(this.dataScarico));
    
    if("S".equalsIgnoreCase(this.flagValida)) 
    {
      htmpl.set(blk+".imgConfermata", MessageFormat.format(htmlStringKO, new Object[] { this.imgConfermata, "Confermata", ""}), null);
    }
    else 
    {
      String msg = "Per confermare la macchina procedere alla Presa in carico "+
        "della Richiesta variazione irroratrici dalla Ricerca richieste azienda.";
      htmpl.set(blk+".imgConfermata", MessageFormat.format(htmlStringKO, new Object[] { this.imgConfermata, msg, ""}), null);             
    }
    
  }
  
  
  
  
  public String getComune()
  {
    return comune;
  }
  public void setComune(String comune)
  {
    this.comune = comune;
  }
  public String getSglProvincia()
  {
    return sglProvincia;
  }
  public void setSglProvincia(String sglProvincia)
  {
    this.sglProvincia = sglProvincia;
  }
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }
  public String getDescTipoMacchina()
  {
    return descTipoMacchina;
  }
  public void setDescTipoMacchina(String descTipoMacchina)
  {
    this.descTipoMacchina = descTipoMacchina;
  }
  public String getDescTipoGenereMacchina()
  {
    return descTipoGenereMacchina;
  }
  public void setDescTipoGenereMacchina(String descTipoGenereMacchina)
  {
    this.descTipoGenereMacchina = descTipoGenereMacchina;
  }
  public String getDescTipoCategoriaMacchina()
  {
    return descTipoCategoriaMacchina;
  }
  public void setDescTipoCategoriaMacchina(String descTipoCategoriaMacchina)
  {
    this.descTipoCategoriaMacchina = descTipoCategoriaMacchina;
  }
  public String getDescTipoMarca()
  {
    return descTipoMarca;
  }
  public void setDescTipoMarca(String descTipoMarca)
  {
    this.descTipoMarca = descTipoMarca;
  }
  public String getDescModello()
  {
    return descModello;
  }
  public void setDescModello(String descModello)
  {
    this.descModello = descModello;
  }
  public String getNumeroTarga()
  {
    return numeroTarga;
  }
  public void setNumeroTarga(String numeroTarga)
  {
    this.numeroTarga = numeroTarga;
  }
  public String getMatricolaTelaio()
  {
    return matricolaTelaio;
  }
  public void setMatricolaTelaio(String matricolaTelaio)
  {
    this.matricolaTelaio = matricolaTelaio;
  }
  public Date getDataCarico()
  {
    return dataCarico;
  }
  public void setDataCarico(Date dataCarico)
  {
    this.dataCarico = dataCarico;
  }
  public Date getDataScarico()
  {
    return dataScarico;
  }
  public void setDataScarico(Date dataScarico)
  {
    this.dataScarico = dataScarico;
  }
  public Long getIdMacchina()
  {
    return idMacchina;
  }
  public void setIdMacchina(Long idMacchina)
  {
    this.idMacchina = idMacchina;
  }
  public boolean isChecked()
  {
    return checked;
  }
  public void setChecked(boolean checked)
  {
    this.checked = checked;
  }
  public Integer getAnnoCostruzione()
  {
    return annoCostruzione;
  }
  public void setAnnoCostruzione(Integer annoCostruzione)
  {
    this.annoCostruzione = annoCostruzione;
  }

  public long getIdPossessoMacchina()
  {
    return idPossessoMacchina;
  }

  public void setIdPossessoMacchina(long idPossessoMacchina)
  {
    this.idPossessoMacchina = idPossessoMacchina;
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

  public String getFlagValida()
  {
    return flagValida;
  }

  public void setFlagValida(String flagValida)
  {
    this.flagValida = flagValida;
  }

  public String getImgConfermata()
  {
    return imgConfermata;
  }

  public void setImgConfermata(String imgConfermata)
  {
    this.imgConfermata = imgConfermata;
  }
  
  
  
  

  
}
