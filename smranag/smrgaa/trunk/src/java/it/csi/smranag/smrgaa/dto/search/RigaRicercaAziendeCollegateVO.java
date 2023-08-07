package it.csi.smranag.smrgaa.dto.search;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.dto.search.paginazione.IPaginazione;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.dto.anag.SoggettoAssociatoVO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.Validator;
import it.csi.solmr.util.ProfileUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Rappresenta una riga dei risultati di una ricerca terreni.
 * 
 * @author TOBECONFIG (Matr. 71646)
 * 
 */
public class RigaRicercaAziendeCollegateVO implements IPaginazione
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -7836592934910898340L;
  
  
  
  private boolean           checked;
  private String cuaa;
  private String partitaIva;
  private String nomeAzienda;
  private String comune;
  private String sglProvincia;
  private String indirizzo;
  private String cap;
  private String istatComune;
  private Date dataIngresso;
  private Date dataUscita;
  private Date dataInizioValidita;
  private Date dataFineValidita;
  private Date dataAggiornamento;
  private String descrizioneUtenteModifica = null;
  private String descrizioneEnteUtenteModifica = null;
  private Long idAziendaCollegata = null;
  private Long idAziendaAssociata = null;
  private Long idSoggettoAssociato = null;
  private String sedeCittaEstero = null;
  private String sedeEstero = null;
  private SoggettoAssociatoVO soggettoAssociato = null;
  private boolean storico;
  private Long idAzienda;
  private Long idUtenteAggiornamento;
  private boolean paLocale;
  private BigDecimal supCondotta;
  private BigDecimal supSAU;
  private Date dataValidazione;
  private Long idUtenteValidazione;
  private String detentoreFascicolo;

  public boolean isPaLocale()
  {
    return paLocale;
  }

  public void setPaLocale(boolean paLocale)
  {
    this.paLocale = paLocale;
  }

  public Long getIdAzienda()
  {
    return idAzienda;
  }

  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public Long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }

  public void setIdUtenteAggiornamento(Long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    if(this.idSoggettoAssociato != null)
    {
      sb.append(" CUAA: ").append(this.getSoggettoAssociato().getCuaa());
      sb.append(" ").append(this.getSoggettoAssociato().getDenominazione());
    }
    else
    {
      sb.append(" CUAA: ").append(cuaa);
      sb.append(" ").append(nomeAzienda);
    }
    return sb.toString();
  }
  
  public void scriviRiga(Htmpl htmpl, String blk, String id[])
  {
    scriviRiga(htmpl, blk);
  }

  public void scriviRiga(Htmpl htmpl, String blk)
  {
    boolean flagCensita = true;
    String storicoChk = "false";
    
    
    if(this.dataFineValidita !=null)
    {
      storicoChk = "true";
    }
    
    String idMulti = null;
    if(this.idSoggettoAssociato != null)
    {
      idMulti = this.idSoggettoAssociato.toString();
      flagCensita = false;
    }
    else
    {
      idMulti = this.idAziendaAssociata.toString();
    }
    
    String censita = "false";
    if(flagCensita)
    {
      censita = "true";
    }
    
    String chk = idMulti+","+this.idAziendaCollegata.toString()+","+storicoChk+","+censita;
    htmpl.set(blk+".chkIdAzienda", chk);    
    htmpl.set(blk + ".idElem", this.idAziendaCollegata.toString());
    
    if(checked)
    {
      htmpl.set(blk + ".checked", "checked=\"checked\"");
    }
    
    if(flagCensita)
    {
    
      htmpl.set(blk + ".cuaa",this.cuaa);
      htmpl.set(blk + ".partitaIva", this.partitaIva);
      htmpl.set(blk + ".nomeAzienda",this.nomeAzienda);
      
      String sedeLegale = "";
      if(Validator.isNotEmpty(this.sedeCittaEstero))
      {
        htmpl.set(blk + ".comune", this.sedeCittaEstero);
        htmpl.set(blk + ".indirizzo", this.indirizzo);
      }
      else
      {
        String comuneStr = sedeLegale += this.comune +" ("+this.sglProvincia+") ";
        htmpl.set(blk + ".comune", comuneStr);
        htmpl.set(blk + ".indirizzo", this.indirizzo);
        htmpl.set(blk + ".cap", this.cap);
      }
    }
    else
    {
    
      SoggettoAssociatoVO soggVO = this.soggettoAssociato;
      htmpl.set(blk + ".cuaa",soggVO.getCuaa());
      htmpl.set(blk + ".partitaIva",soggVO.getPartitaIva());
      htmpl.set(blk + ".nomeAzienda",soggVO.getDenominazione());
              
      String comune = soggVO.getDenominazioneComune() +" ("+soggVO.getSglProv()+") ";
      htmpl.set(blk + ".comune",comune);
      htmpl.set(blk + ".indirizzo", soggVO.getIndirizzo());
      htmpl.set(blk + ".cap", soggVO.getCap());
      
    
    }
    
    
    
    htmpl.set(blk + ".dataIngresso",
      DateUtils.formatDateNotNull(this.dataIngresso));
    htmpl.set(blk + ".dataUscita",
      DateUtils.formatDateNotNull(this.dataUscita));
    htmpl.set(blk + ".dataInizioValidita",
      DateUtils.formatDateNotNull(this.dataInizioValidita));
    
    //solo se selzionato il check Storico visualizzo la data fine validità  
    if(storico)
    {
      htmpl.newBlock("blkOrdineStorico");
      htmpl.set(blk + ".blkOrdineStorico.dataUscita",
        DateUtils.formatDateNotNull(this.dataUscita));
      htmpl.set(blk + ".blkOrdineStorico.dataInizioValidita",
        DateUtils.formatDateNotNull(this.dataInizioValidita));
      htmpl.set(blk + ".blkOrdineStorico.dataFineValidita",
      DateUtils.formatDateNotNull(this.dataFineValidita));
    }
    
    
    htmpl.set(blk + ".supCondotta",
        Formatter.formatDouble4(this.supCondotta));
    
    htmpl.set(blk + ".supSAU",
        Formatter.formatDouble4(this.supSAU));
    
    htmpl.set(blk + ".dataValidazione",
        DateUtils.formatDateTimeNotNull(this.dataValidazione));
    
    htmpl.set(blk + ".detentoreFascicolo", this.detentoreFascicolo,null);
    
    String dateUlt = DateUtils.formatDateNotNull(this.dataAggiornamento);
    ProfileUtils.setFieldUltimaModificaByRuolo(paLocale, htmpl,
      blk+ ".ultimaModificaVw", dateUlt, this.descrizioneUtenteModifica,
      this.descrizioneEnteUtenteModifica,null); 
  }

  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
  }

  public String getCuaa()
  {
    return cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public String getPartitaIva()
  {
    return partitaIva;
  }

  public void setPartitaIva(String partitaIva)
  {
    this.partitaIva = partitaIva;
  }

  public String getNomeAzienda()
  {
    return nomeAzienda;
  }

  public void setNomeAzienda(String nomeAzienda)
  {
    this.nomeAzienda = nomeAzienda;
  }

  public String getComune()
  {
    return comune;
  }

  public void setComune(String comune)
  {
    this.comune = comune;
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

  public Date getDataIngresso()
  {
    return dataIngresso;
  }

  public void setDataIngresso(Date dataIngresso)
  {
    this.dataIngresso = dataIngresso;
  }

  public Date getDataUscita()
  {
    return dataUscita;
  }

  public void setDataUscita(Date dataUscita)
  {
    this.dataUscita = dataUscita;
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

  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }

  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }

  public String getDescrizioneUtenteModifica()
  {
    return descrizioneUtenteModifica;
  }

  public void setDescrizioneUtenteModifica(String descrizioneUtenteModifica)
  {
    this.descrizioneUtenteModifica = descrizioneUtenteModifica;
  }

  public String getDescrizioneEnteUtenteModifica()
  {
    return descrizioneEnteUtenteModifica;
  }

  public void setDescrizioneEnteUtenteModifica(
      String descrizioneEnteUtenteModifica)
  {
    this.descrizioneEnteUtenteModifica = descrizioneEnteUtenteModifica;
  }

  public Long getIdAziendaCollegata()
  {
    return idAziendaCollegata;
  }

  public void setIdAziendaCollegata(Long idAziendaCollegata)
  {
    this.idAziendaCollegata = idAziendaCollegata;
  }

  public Long getIdAziendaAssociata()
  {
    return idAziendaAssociata;
  }

  public void setIdAziendaAssociata(Long idAziendaAssociata)
  {
    this.idAziendaAssociata = idAziendaAssociata;
  }
  
  public Long getIdSoggettoAssociato()
  {
    return idSoggettoAssociato;
  }

  public void setIdSoggettoAssociato(Long idSoggettoAssociato)
  {
    this.idSoggettoAssociato = idSoggettoAssociato;
  }
  
  public String getSedeCittaEstero()
  {
    return sedeCittaEstero;
  }

  public void setSedeCittaEstero(String sedeCittaEstero)
  {
    this.sedeCittaEstero = sedeCittaEstero;
  }

  public String getSglProvincia()
  {
    return sglProvincia;
  }

  public void setSglProvincia(String sglProvincia)
  {
    this.sglProvincia = sglProvincia;
  }
  
  public SoggettoAssociatoVO getSoggettoAssociato()
  {
    return soggettoAssociato;
  }

  public void setSoggettoAssociato(SoggettoAssociatoVO soggettoAssociato)
  {
    this.soggettoAssociato = soggettoAssociato;
  }

  public boolean isStorico()
  {
    return storico;
  }

  public void setStorico(boolean storico)
  {
    this.storico = storico;
  }

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }

  public String getSedeEstero()
  {
    return sedeEstero;
  }

  public void setSedeEstero(String sedeEstero)
  {
    this.sedeEstero = sedeEstero;
  }

  public BigDecimal getSupCondotta()
  {
    return supCondotta;
  }

  public void setSupCondotta(BigDecimal supCondotta)
  {
    this.supCondotta = supCondotta;
  }

  public BigDecimal getSupSAU()
  {
    return supSAU;
  }

  public void setSupSAU(BigDecimal supSAU)
  {
    this.supSAU = supSAU;
  }

  public Date getDataValidazione()
  {
    return dataValidazione;
  }

  public void setDataValidazione(Date dataValidazione)
  {
    this.dataValidazione = dataValidazione;
  }

  public Long getIdUtenteValidazione()
  {
    return idUtenteValidazione;
  }

  public void setIdUtenteValidazione(Long idUtenteValidazione)
  {
    this.idUtenteValidazione = idUtenteValidazione;
  }

  public String getDetentoreFascicolo()
  {
    return detentoreFascicolo;
  }

  public void setDetentoreFascicolo(String detentoreFascicolo)
  {
    this.detentoreFascicolo = detentoreFascicolo;
  }
}
