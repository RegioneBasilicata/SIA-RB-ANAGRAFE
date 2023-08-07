package it.csi.smranag.smrgaa.dto.search;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.dto.search.paginazione.IPaginazione;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;

import java.util.Date;

/**
 * Rappresenta una riga dei risultati di una ricerca terreni.
 * 
 * @author TOBECONFIG
 * 
 */
public class RigaRicercaVariazioniAziendaliVO implements IPaginazione
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 5890537333099314597L;
  
  private Long              idDettaglioVariazioneAzi; //DB_DETTAGLIO_VARIAZIONE_AZI.ID_DETTAGLIO_VARIAZIONE_AZI
  private String            descProvAmmComp; //PROVINCIA.DESCRIZIONE
  private String            cuaa; //DB_ANAGRAFICA_AZIENDA.CUAA
  private String            partitaIva; //DB_ANAGRAFICA_AZIENDA.PARTITA_IVA
  private String            denominazione; //DB_ANAGRAFICA_AZIENDA.DENOMINAZIONE
  private String            comune; //COMUNE.DESCOM + PROVINCIA.SIGLA_PROVINCIA
  private String            indirizzo; //DB_ANAGRAFICA_AZIENDA.SEDELEG_INDIRIZZO
  private String            cap; //DB_ANAGRAFICA_AZIENDA.SEDELEG_CAP
  private String            tipologiaVariazione; //DB_TIPO_TIPOLOGIA_VARIAZIONE.DESCRIZIONE
  private String            variazione; //DB_TIPO_VARIAZIONE_AZIENDA.DESCRIZIONE
  private Date              dataVariazione; //DB_DETTAGLIO_VARIAZIONE_AZI.DATA_VARIAZION
  private Long              idVisioneVariazioneAzi; //DB_VISIONE_VARIAZIONE_AZI.ID_VISIONE_VARIAZIONE_AZI
  private boolean           flagStoricizzato; //
  
  
  public Long getIdDettaglioVariazioneAzi()
  {
    return idDettaglioVariazioneAzi;
  }
  public void setIdDettaglioVariazioneAzi(Long idDettaglioVariazioneAzi)
  {
    this.idDettaglioVariazioneAzi = idDettaglioVariazioneAzi;
  }
  public String getDescProvAmmComp()
  {
    return descProvAmmComp;
  }
  public void setDescProvAmmComp(String descProvAmmComp)
  {
    this.descProvAmmComp = descProvAmmComp;
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
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
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
  public String getTipologiaVariazione()
  {
    return tipologiaVariazione;
  }
  public void setTipologiaVariazione(String tipologiaVariazione)
  {
    this.tipologiaVariazione = tipologiaVariazione;
  }
  public String getVariazione()
  {
    return variazione;
  }
  public void setVariazione(String variazione)
  {
    this.variazione = variazione;
  }
  public Date getDataVariazione()
  {
    return dataVariazione;
  }
  public void setDataVariazione(Date dataVariazione)
  {
    this.dataVariazione = dataVariazione;
  }
  public boolean isFlagStoricizzato()
  {
    return flagStoricizzato;
  }
  public void setFlagStoricizzato(boolean flagStoricizzato)
  {
    this.flagStoricizzato = flagStoricizzato;
  }
  public Long getIdVisioneVariazioneAzi()
  {
    return idVisioneVariazioneAzi;
  }
  public void setIdVisioneVariazioneAzi(Long idVisioneVariazioneAzi)
  {
    this.idVisioneVariazioneAzi = idVisioneVariazioneAzi;
  }
  
  
  public void scriviRiga(Htmpl htmpl, String blk, String idPresaVisione[])
  {
    if (idPresaVisione!=null)
      for(int i=0;i<idPresaVisione.length;i++)
      {
        if ((""+idDettaglioVariazioneAzi).equals(idPresaVisione[i]))
        {
          htmpl.set(blk + ".checkPresaVisione", "checked=\"checked\"");
          break;
        }
      }
    //Se è storicizzato scrivo i dati in rosso
    if (flagStoricizzato) htmpl.newBlock(blk + ".blkRed");
    if (this.idVisioneVariazioneAzi!=null) htmpl.newBlock(blk + ".blkPresaVisione");
    htmpl.set(blk + ".idDettaglioVariazioneAzi", String.valueOf(this.idDettaglioVariazioneAzi));
    htmpl.set(blk + ".descProvAmmComp", StringUtils.checkNull(this.descProvAmmComp));
    htmpl.set(blk + ".cuaa", StringUtils.checkNull(this.cuaa));
    htmpl.set(blk + ".partitaIva", StringUtils.checkNull(this.partitaIva));
    htmpl.set(blk + ".denominazione", StringUtils.checkNull(this.denominazione));
    htmpl.set(blk + ".comune", StringUtils.checkNull(this.comune));
    htmpl.set(blk + ".indirizzo", StringUtils.checkNull(this.indirizzo));
    htmpl.set(blk + ".cap", StringUtils.checkNull(this.cap));
    htmpl.set(blk + ".tipologiaVariazione", StringUtils.checkNull(this.tipologiaVariazione));
    htmpl.set(blk + ".variazione", StringUtils.checkNull(this.variazione));
    
    if (dataVariazione != null)
    {
      htmpl.set(blk + ".dataVariazione", DateUtils
          .formatDate(this.dataVariazione));
    }
  }
  
  public void scriviRiga(Htmpl htmpl, String blk)
  {
    scriviRiga(htmpl, blk, null);
  }

}
