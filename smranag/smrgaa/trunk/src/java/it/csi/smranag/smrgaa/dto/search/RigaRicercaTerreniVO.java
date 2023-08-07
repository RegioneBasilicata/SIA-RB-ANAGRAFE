package it.csi.smranag.smrgaa.dto.search;

import java.math.BigDecimal;
import java.util.Date;


import it.csi.jsf.htmpl.Htmpl;
import it.csi.smranag.smrgaa.dto.search.paginazione.IPaginazione;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

/**
 * Rappresenta una riga dei risultati di una ricerca terreni.
 * 
 * @author TOBECONFIG
 * 
 */
public class RigaRicercaTerreniVO implements IPaginazione
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4728214417392775669L;
  private long              idParticella;                    // DB_STORICO_PARTICELLA.ID_PARTICELLA
  private String            siglaProvincia;                         // PROVINCIA.SIGLA_PROVINCIA
  // PER
  // ISTAT_COMUNE
  private String            descrizioneComune;                      // COMUNE.DESCOM
  // PER
  // ISTAT_COMUNE
  private String            istatComune;                            // DB_STORICO_PARTICELLA.COMUNE
  private String            sezione;                                // DB_STORICO_PARTICELLA.SEZIONE
  private long              foglio;                                 // DB_STORICO_PARTICELLA.FOGLIO
  private Long              particella;                             // DB_STORICO_PARTICELLA.PARTICELLA
  private String            subalterno;                             // DB_STORICO_PARTICELLA.SUBALTERNO
  private BigDecimal        supCatastale;                           // DB_STORICO_PARTICELLA.SUP_CATASTALE
  private BigDecimal        superficieGrafica;                      // DB_STORICO_PARTICELLA.SUPERFICIE_GRAFICA
  private Date              dataInizioDecorrenza;                   // DB_PARTICELLA.DATA_CREAZIONE
  private Date              dataCessazione;                         // DB_PARTICELLA.DATA_CESSAZIONE
  private String            flagEstero;

  public BigDecimal getSuperficieGrafica()
  {
    return superficieGrafica;
  }

  public void setSuperficieGrafica(BigDecimal superficieGrafica)
  {
    this.superficieGrafica = superficieGrafica;
  }

  public String getFlagEstero()
  {
    return flagEstero;
  }

  public void setFlagEstero(String flagEstero)
  {
    this.flagEstero = flagEstero;
  }

  public long getIdParticella()
  {
    return idParticella;
  }

  public void setIdParticella(long idParticella)
  {
    this.idParticella = idParticella;
  }

  public String getSiglaProvincia()
  {
    return siglaProvincia;
  }

  public void setSiglaProvincia(String siglaProvincia)
  {
    this.siglaProvincia = siglaProvincia;
  }

  public String getDescrizioneComune()
  {
    return descrizioneComune;
  }

  public void setDescrizioneComune(String descrizioneComune)
  {
    this.descrizioneComune = descrizioneComune;
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

  public Date getDataInizioDecorrenza()
  {
    return dataInizioDecorrenza;
  }

  public void setDataInizioDecorrenza(Date dataInizioDecorrenza)
  {
    this.dataInizioDecorrenza = dataInizioDecorrenza;
  }

  public Date getDataCessazione()
  {
    return dataCessazione;
  }

  public void setDataCessazione(Date dataCessazione)
  {
    this.dataCessazione = dataCessazione;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    if (SolmrConstants.FLAG_S.equals(flagEstero))
    {
      sb.append("Stato: ").append(descrizioneComune);
    }
    else
    {
      sb.append("Comune: ").append(descrizioneComune).append(" (").append(
          siglaProvincia).append(")");
    }
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
    if (SolmrConstants.FLAG_S.equals(flagEstero))
    {
      htmpl.newBlock(blk + ".blkStatoEstero");
      htmpl.set(blk + ".blkStatoEstero.statoEstero",
          this.descrizioneComune);
    }
    else
    {
      htmpl.newBlock(blk + ".blkComuneItalia");
      htmpl.set(blk + ".blkComuneItalia.descrizioneComune",
          this.descrizioneComune);
      htmpl.set(blk + ".blkComuneItalia.siglaProvincia", this.siglaProvincia);
    }
    htmpl.set(blk + ".idParticella", String
        .valueOf(this.idParticella));
    htmpl.set(blk + ".sezione", this.sezione);
    htmpl.set(blk + ".foglio", String.valueOf(this.foglio));
    htmpl.set(blk + ".particella", StringUtils.checkNull(this.particella));
    htmpl.set(blk + ".subalterno", StringUtils.checkNull(this.subalterno));
    htmpl.set(blk + ".supCatastale", Formatter.formatDouble4(this.supCatastale));
    htmpl.set(blk + ".superficieGrafica", Formatter.formatDouble4(this.superficieGrafica));
    if (dataInizioDecorrenza != null)
    {
      htmpl.set(blk + ".dataInizioDecorrenza", DateUtils
          .formatDate(this.dataInizioDecorrenza));
    }
    if (dataCessazione != null)
    {
      htmpl.set(blk + ".dataCessazione", DateUtils
          .formatDate(this.dataCessazione));
    }
  }
}
