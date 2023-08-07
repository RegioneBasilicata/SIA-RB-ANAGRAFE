package it.csi.solmr.dto.anag;

import java.io.Serializable;
import java.math.BigDecimal;
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

public class ParticellaCertElegVO implements Serializable
{
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 2842657233939140355L;
  
  
  
  
  private long idParticellaCertEleg;           // DB_PARTICELLA_CERT_ELEG.ID_PARTICELLA_CERT_ELEG
  private long idParticellaCertificata;        // DB_PARTICELLA_CERT_ELEG.ID_PARTICELLA_CERTIFICATA
  private long idEleggibilita;                 // DB_PARTICELLA_CERT_ELEG.ID_ELEGGIBILITA
  private Long idEleggibilitaFit;              // DB_PARTICELLA_CERT_ELEG.ID_ELEGGIBILITA_FIT
  private BigDecimal superficie;               // DB_PARTICELLA_CERT_ELEG.SUPERFICIE
  private BigDecimal superficieTara;           // DB_PARTICELLA_CERT_ELEG.SUPERFICIE_TARA
  private BigDecimal superficieDiff;           // SUM(DB_PARTICELLA_CERT_ELEG.SUPERFICIE - DB_PARTICELLA_CERT_ELEG.SUPERFICIE_TARA)
  private String descrizione;                  // DB_TIPO_ELEGGIBILITA.DESCRIZIONE
  private String descrizioneFit;               // DB_TIPO_ELEGGIBILITA_FIT.DESCRIZIONE
  private int annoCampagna;                    // DB_PARTICELLA_CERT_ELEG.ANNO_CAMPAGNA
  private Date dataFotointerpretazione;        // DB_PARTICELLA_CERT_ELEG.FOTOINTERPRETAZIONE
  private BigDecimal superficieEleggibileNetta;
  
  
  
  
  
  
  
  public long getIdParticellaCertEleg()
  {
    return idParticellaCertEleg;
  }
  public void setIdParticellaCertEleg(long idParticellaCertEleg)
  {
    this.idParticellaCertEleg = idParticellaCertEleg;
  }
  public long getIdParticellaCertificata()
  {
    return idParticellaCertificata;
  }
  public void setIdParticellaCertificata(long idParticellaCertificata)
  {
    this.idParticellaCertificata = idParticellaCertificata;
  }
  public long getIdEleggibilita()
  {
    return idEleggibilita;
  }
  public void setIdEleggibilita(long idEleggibilita)
  {
    this.idEleggibilita = idEleggibilita;
  }
  public Long getIdEleggibilitaFit()
  {
    return idEleggibilitaFit;
  }
  public void setIdEleggibilitaFit(Long idEleggibilitaFit)
  {
    this.idEleggibilitaFit = idEleggibilitaFit;
  }
  public BigDecimal getSuperficie()
  {
    return superficie;
  }
  public void setSuperficie(BigDecimal superficie)
  {
    this.superficie = superficie;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getDescrizioneFit()
  {
    return descrizioneFit;
  }
  public void setDescrizioneFit(String descrizioneFit)
  {
    this.descrizioneFit = descrizioneFit;
  }
  public BigDecimal getSuperficieTara()
  {
    return superficieTara;
  }
  public void setSuperficieTara(BigDecimal superficieTara)
  {
    this.superficieTara = superficieTara;
  }
  public BigDecimal getSuperficieDiff()
  {
    return superficieDiff;
  }
  public void setSuperficieDiff(BigDecimal superficieDiff)
  {
    this.superficieDiff = superficieDiff;
  }
  public int getAnnoCampagna()
  {
    return annoCampagna;
  }
  public void setAnnoCampagna(int annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }
  public Date getDataFotointerpretazione()
  {
    return dataFotointerpretazione;
  }
  public void setDataFotointerpretazione(Date dataFotointerpretazione)
  {
    this.dataFotointerpretazione = dataFotointerpretazione;
  }
  public BigDecimal getSuperficieEleggibileNetta()
  {
    return superficieEleggibileNetta;
  }
  public void setSuperficieEleggibileNetta(BigDecimal superficieEleggibileNetta)
  {
    this.superficieEleggibileNetta = superficieEleggibileNetta;
  }
  
  
  
  
  
  
  
  
  
  
  

  

}
