package it.csi.smranag.smrgaa.dto.search;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.dto.search.paginazione.IPaginazione;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;
import it.csi.solmr.etc.SolmrConstants;

import java.math.BigDecimal;

/**
 * Rappresenta una riga dei risultati di una ricerca terreni.
 * 
 * @author TOBECONFIG (Matr. 71646)
 * 
 */
public class RigaRicercaTerreniImportaAsservimentoVO implements IPaginazione
{
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 9055572670000074366L;
  
  
  
  private long              idParticella;                           // DB_STORICO_PARTICELLA.ID_PARTICELLA
  private Long              idConduzioneDichiarata;                 // DB_CONDUZIONE_DICHIARATA.ID_CONDUZIONE_DICHIARATA
  // PER
  // ISTAT_COMUNE
  private String            descrizioneComune;                      // COMUNE.DESCOM
  private String            siglaProvinciaParticella;
  // PER
  // ISTAT_COMUNE
  private String            istatComune;                            // DB_STORICO_PARTICELLA.COMUNE
  private String            sezione;                                // DB_STORICO_PARTICELLA.SEZIONE
  private long              foglio;                                 // DB_STORICO_PARTICELLA.FOGLIO
  private Long              particella;                             // DB_STORICO_PARTICELLA.PARTICELLA
  private String            subalterno;                             // DB_STORICO_PARTICELLA.SUBALTERNO
  private BigDecimal        supCatastale;                           // DB_STORICO_PARTICELLA.SUP_CATASTALE
  private BigDecimal        superficieGrafica;                      // DB_STORICO_PARTICELLA.SUPERFICIE_GRAFICA
  private BigDecimal        supCondotta;                            // DB_CONDUZIONE_DICHIARATA.SUP_CONDOTTA
  private long              idTitoloPosesso;                        // DB_CONDUZIONE_DICHIARATA.ID_TITOLO_POSSESSO
  private long              percentualePossesso;                    // DB_CONDUZIONE_DICHIARATA.PERCENTUALE_POSSESSO
  private String            tipoRicerca;                            // usato per capire se ricarca fatta con CUAA o con chiave catastale
  private boolean           checked;                            

  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
  }
  
  public long getPercentualePossesso()
  {
    return percentualePossesso;
  }

  public void setPercentualePossesso(long percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }

  public long getIdParticella()
  {
    return idParticella;
  }

  public void setIdParticella(long idParticella)
  {
    this.idParticella = idParticella;
  }

  public String getDescrizioneComune()
  {
    return descrizioneComune;
  }

  public void setDescrizioneComune(String descrizioneComune)
  {
    this.descrizioneComune = descrizioneComune;
  }

  public BigDecimal getSuperficieGrafica()
  {
    return superficieGrafica;
  }

  public void setSuperficieGrafica(BigDecimal superficieGrafica)
  {
    this.superficieGrafica = superficieGrafica;
  }

  public String getIstatComune()
  {
    return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }

  public String getSezione()
  {
    return sezione;
  }

  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }

  public long getFoglio()
  {
    return foglio;
  }

  public void setFoglio(long foglio)
  {
    this.foglio = foglio;
  }

  public Long getParticella()
  {
    return particella;
  }

  public void setParticella(Long particella)
  {
    this.particella = particella;
  }

  public String getSubalterno()
  {
    return subalterno;
  }

  public void setSubalterno(String subalterno)
  {
    this.subalterno = subalterno;
  }

  public BigDecimal getSupCatastale()
  {
    return supCatastale;
  }

  public void setSupCatastale(BigDecimal supCatastale)
  {
    this.supCatastale = supCatastale;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    
    sb.append("Comune: ").append(descrizioneComune);
    
    if (Validator.isNotEmpty(sezione))
    {
      sb.append(" Sez. ").append(sezione);
    }
    sb.append(" Fgl. ").append(foglio);
    if (Validator.isNotEmpty(particella))
    {
      sb.append(" Part. ").append(particella);
    }
    if (Validator.isNotEmpty(subalterno))
    {
      sb.append(" Sub. ").append(subalterno);
    }
    return sb.toString();
  }
  
  public void scriviRiga(Htmpl htmpl, String blk, String id[])
  {
    scriviRiga(htmpl, blk);
  }

  public void scriviRiga(Htmpl htmpl, String blk)
  {
    
    htmpl.set(blk + ".descComuneParticella",
        this.descrizioneComune);
    
    htmpl.set(blk + ".siglaProvinciaParticella",
        this.siglaProvinciaParticella);
    
    if(tipoRicerca.equalsIgnoreCase(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CUAA))
    {
      htmpl.set(blk + ".idParticella", String
          .valueOf(this.idConduzioneDichiarata));
      
      htmpl.set(blk + ".idElem", String
          .valueOf(this.idConduzioneDichiarata));
    }
    else
    {
      htmpl.set(blk + ".idParticella", String
          .valueOf(this.idParticella));
      
      htmpl.set(blk + ".idElem", String
          .valueOf(this.idParticella));
    }
    
    if(checked)
    {
      htmpl.set(blk + ".checked", "checked=\"checked\"");
    }
    
    htmpl.set(blk + ".sezione", this.sezione);
    htmpl.set(blk + ".foglio", String.valueOf(this.foglio));
    htmpl.set(blk + ".particella", StringUtils.checkNull(this.particella));
    htmpl.set(blk + ".subalterno", StringUtils.checkNull(this.subalterno));
    htmpl.set(blk + ".supCatastale", Formatter.formatDouble4(this.supCatastale));
    htmpl.set(blk + ".superficieGrafica", Formatter.formatDouble4(this.superficieGrafica));
    if(tipoRicerca.equalsIgnoreCase(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CUAA))
    {
      htmpl.newBlock(blk+".blkRicercaParticelle");
      htmpl.set(blk+".blkRicercaParticelle.idTitoloPossesso", String.valueOf(idTitoloPosesso));
      htmpl.set(blk+".blkRicercaParticelle.percentualePossesso", String.valueOf(percentualePossesso));
    }
  }

  public Long getIdConduzioneDichiarata()
  {
    return idConduzioneDichiarata;
  }

  public void setIdConduzioneDichiarata(Long idConduzioneDichiarata)
  {
    this.idConduzioneDichiarata = idConduzioneDichiarata;
  }

  public BigDecimal getSupCondotta()
  {
    return supCondotta;
  }

  public void setSupCondotta(BigDecimal supCondotta)
  {
    this.supCondotta = supCondotta;
  }

  public long getIdTitoloPosesso()
  {
    return idTitoloPosesso;
  }

  public void setIdTitoloPosesso(long idTitoloPosesso)
  {
    this.idTitoloPosesso = idTitoloPosesso;
  }

  public String getTipoRicerca()
  {
    return tipoRicerca;
  }

  public void setTipoRicerca(String tipoRicerca)
  {
    this.tipoRicerca = tipoRicerca;
  }

	public String getSiglaProvinciaParticella()
	{
		return siglaProvinciaParticella;
	}

	public void setSiglaProvinciaParticella(String siglaProvinciaParticella)
	{
		this.siglaProvinciaParticella = siglaProvinciaParticella;
	}
}
