package it.csi.smranag.smrgaa.dto.allevamenti;

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
 * @author Diana Luca
 * @version 0.1
 */
public class AllevamentoBioVO implements Serializable
{
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 8387227404372705109L;
  
  
  
  
  
  private long idAllevamentoBio;
  private long idAzienda;
  private long idUte;
  private String codiceAziendaZootecnica;
  private long idCategoriaAnimale;
  private long idSpecieAnimale;
  private BigDecimal quantita;
  private BigDecimal quantitaConvenzionale;
  private BigDecimal quantitaBiologico;
  private Date dataInizioValidita;
  private Date dataFineValidita;
  private String descrizioneCategoria;
  private String unitaMisura;
  
  
  
  
  
  
  
  
  public String getDescrizioneCategoria()
  {
    return descrizioneCategoria;
  }
  public void setDescrizioneCategoria(String descrizioneCategoria)
  {
    this.descrizioneCategoria = descrizioneCategoria;
  }
  public String getUnitaMisura()
  {
    return unitaMisura;
  }
  public void setUnitaMisura(String unitaMisura)
  {
    this.unitaMisura = unitaMisura;
  }
  public long getIdAllevamentoBio()
  {
    return idAllevamentoBio;
  }
  public void setIdAllevamentoBio(long idAllevamentoBio)
  {
    this.idAllevamentoBio = idAllevamentoBio;
  }
  public long getIdUte()
  {
    return idUte;
  }
  public void setIdUte(long idUte)
  {
    this.idUte = idUte;
  }
  public String getCodiceAziendaZootecnica()
  {
    return codiceAziendaZootecnica;
  }
  public void setCodiceAziendaZootecnica(String codiceAziendaZootecnica)
  {
    this.codiceAziendaZootecnica = codiceAziendaZootecnica;
  }
  public long getIdCategoriaAnimale()
  {
    return idCategoriaAnimale;
  }
  public void setIdCategoriaAnimale(long idCategoriaAnimale)
  {
    this.idCategoriaAnimale = idCategoriaAnimale;
  }
  public long getIdSpecieAnimale()
  {
    return idSpecieAnimale;
  }
  public void setIdSpecieAnimale(long idSpecieAnimale)
  {
    this.idSpecieAnimale = idSpecieAnimale;
  }
  public BigDecimal getQuantita()
  {
    return quantita;
  }
  public void setQuantita(BigDecimal quantita)
  {
    this.quantita = quantita;
  }
  public BigDecimal getQuantitaConvenzionale()
  {
    return quantitaConvenzionale;
  }
  public void setQuantitaConvenzionale(BigDecimal quantitaConvenzionale)
  {
    this.quantitaConvenzionale = quantitaConvenzionale;
  }
  public BigDecimal getQuantitaBiologico()
  {
    return quantitaBiologico;
  }
  public void setQuantitaBiologico(BigDecimal quantitaBiologico)
  {
    this.quantitaBiologico = quantitaBiologico;
  }
  public long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
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
  
  
  
  
  
  
  
  
  
}
