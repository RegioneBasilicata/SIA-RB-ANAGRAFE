package it.csi.smranag.smrgaa.dto.search;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.dto.search.paginazione.IPaginazione;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.Validator;

import java.util.Date;

/**
 * Rappresenta una riga dei risultati di una ricerca terreni.
 * 
 * @author TOBECONFIG (Matr. 71646)
 * 
 */
public class RigaRicercaDocumentiIndexVO implements IPaginazione
{
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 4045655304714321068L;
  
  
  
  private boolean           checked;
  private Long idDocumento;
  private String descTipoDocumento;
  private Date dataFineValidita;
  private String nomeFile;
  private Date dataInserimento;
  private Integer anno;
  private String numeroProtocollo;  
  private Date dataProtocollo;
  private String descCategoriaDocumento;
  private String descTipologiaDocumento;
  private String descProcedimento;
  private String daFirmare;
  private String immagineStampa;
  private String titleAllegato;
 

  
  
  public void scriviRiga(Htmpl htmpl, String blk, String id[])
  {
    scriviRiga(htmpl, blk);
  }

  public void scriviRiga(Htmpl htmpl, String blk)
  {        
    
    htmpl.set(blk+".chkIdDocumento", ""+this.idDocumento);    
    htmpl.set(blk+".idDocumentoIndex", ""+this.idDocumento);
    
    
    /*String immagineStampa = "";
    String titleAllegato = "";
    if(Validator.isNotEmpty(this.daFirmare))
    {
      if(this.daFirmare.equalsIgnoreCase(SolmrConstants.FIRMATA_CARTA))
      {
        immagineStampa = "firmaCarta";
        titleAllegato = "Firmata su carta";
      }
      else if(this.daFirmare.equalsIgnoreCase(SolmrConstants.FIRMATA_TABLET))
      {
        immagineStampa = "firmaTablet";
        titleAllegato = "Firmata grafometricamente";
      }
      else if(this.daFirmare.equalsIgnoreCase(SolmrConstants.FIRMATA_ELETTRONICAMENTE))
      {
        immagineStampa = "firmaElettronica";
        titleAllegato = "Firmata elettronicamente";
      }
      else if(this.daFirmare.equalsIgnoreCase(SolmrConstants.DA_FIRMARE))
      {
        immagineStampa = "noFirma";
        titleAllegato = "Non firmata";
      }
    }
    else
    {
      immagineStampa = "noFirma";
      titleAllegato = "Non firmata";
    }*/
     
    htmpl.set(blk+".immagineStampa", this.immagineStampa);   
    htmpl.set(blk+".titleAllegato", this.titleAllegato);
    
    if(checked)
    {
      htmpl.set(blk + ".checked", "checked=\"checked\"");
    }
    
    htmpl.set(blk + ".descTipoDocumento",this.descTipoDocumento);
    if(Validator.isEmpty(this.dataFineValidita)
      || (!Validator.isEmpty(this.dataFineValidita) && this.dataFineValidita.after(new Date())))
    { 
      htmpl.set(blk+".classStatoDocumento", SolmrConstants.IMMAGINE_ESITO_POSITIVO);
      htmpl.set(blk+".titleStatoDocumento", SolmrConstants.TITLE_STATO_DOCUMENTO_ATTIVO);        
    }
    else
    {
      htmpl.set(blk+".classStatoDocumento", SolmrConstants.CLASS_STATO_DOCUMENTO_SCADUTO);
      htmpl.set(blk+".titleStatoDocumento", SolmrConstants.TITLE_STATO_DOCUMENTO_SCADUTO);
    }
    
    
    htmpl.set(blk + ".nomeFile", this.nomeFile);
    htmpl.set(blk+ ".dataInserimento", DateUtils.formatDateTimeNotNull(this.dataInserimento));
    htmpl.set(blk+".anno", ""+this.anno.intValue());
    htmpl.set(blk+".numeroProtocollo", this.numeroProtocollo);
    htmpl.set(blk+".dataProtocollo", DateUtils.formatDateTimeNotNull(this.dataProtocollo));    
    htmpl.set(blk+".descCategoriaDocumento", this.descCategoriaDocumento);
    htmpl.set(blk+".descTipologiaDocumento", this.descTipologiaDocumento);
    htmpl.set(blk+".descProcedimento", this.descProcedimento);
  }

  
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append(" Nome file: ").append(this.getNomeFile());
    sb.append(" Repertorio: ").append(this.getNumeroProtocollo());
    
    return sb.toString();
  }
  
  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
  }

  public Long getIdDocumento()
  {
    return idDocumento;
  }

  public void setIdDocumento(Long idDocumento)
  {
    this.idDocumento = idDocumento;
  }

  public String getDescTipoDocumento()
  {
    return descTipoDocumento;
  }

  public void setDescTipoDocumento(String descTipoDocumento)
  {
    this.descTipoDocumento = descTipoDocumento;
  }

  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }

  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }

  public String getNomeFile()
  {
    return nomeFile;
  }

  public void setNomeFile(String nomeFile)
  {
    this.nomeFile = nomeFile;
  }

  public Date getDataInserimento()
  {
    return dataInserimento;
  }

  public void setDataInserimento(Date dataInserimento)
  {
    this.dataInserimento = dataInserimento;
  }

  public Integer getAnno()
  {
    return anno;
  }

  public void setAnno(Integer anno)
  {
    this.anno = anno;
  }

  public String getNumeroProtocollo()
  {
    return numeroProtocollo;
  }

  public void setNumeroProtocollo(String numeroProtocollo)
  {
    this.numeroProtocollo = numeroProtocollo;
  }

  public Date getDataProtocollo()
  {
    return dataProtocollo;
  }

  public void setDataProtocollo(Date dataProtocollo)
  {
    this.dataProtocollo = dataProtocollo;
  }

  public String getDescCategoriaDocumento()
  {
    return descCategoriaDocumento;
  }

  public void setDescCategoriaDocumento(String descCategoriaDocumento)
  {
    this.descCategoriaDocumento = descCategoriaDocumento;
  }

  public String getDescTipologiaDocumento()
  {
    return descTipologiaDocumento;
  }

  public void setDescTipologiaDocumento(String descTipologiaDocumento)
  {
    this.descTipologiaDocumento = descTipologiaDocumento;
  }

  public String getDescProcedimento()
  {
    return descProcedimento;
  }

  public void setDescProcedimento(String descProcedimento)
  {
    this.descProcedimento = descProcedimento;
  }

  public String getDaFirmare()
  {
    return daFirmare;
  }

  public void setDaFirmare(String daFirmare)
  {
    this.daFirmare = daFirmare;
  }

  public String getImmagineStampa()
  {
    return immagineStampa;
  }

  public void setImmagineStampa(String immagineStampa)
  {
    this.immagineStampa = immagineStampa;
  }

  public String getTitleAllegato()
  {
    return titleAllegato;
  }

  public void setTitleAllegato(String titleAllegato)
  {
    this.titleAllegato = titleAllegato;
  }

  
}
