package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Classe utilizzata per i confronti con il CCIAA e l'albo
 * 
 * @author Mauro Vocale
 *
 */
public class UnitaArboreaCCIAAVO implements Serializable {
	
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -1320514120163036955L;
  
  
  
  
  private boolean trovataConduzione;
  private boolean presenzaVitignoCCIAA;
  private boolean trovatoAlbo;
  private Long  annoImpianto;
  private Long  idStoricoUnitaArborea;
  private Long  idTipologiaVino;
  private BigDecimal supIscritta;
  private boolean modificaVITI;
  private boolean consolidamentoGis;
	
	public BigDecimal getSupIscritta()
  {
    return supIscritta;
  }

  public void setSupIscritta(BigDecimal supIscritta)
  {
    this.supIscritta = supIscritta;
  }

  public boolean isTrovataConduzione()
  {
    return trovataConduzione;
  }

  public void setTrovataConduzione(boolean trovataConduzione)
  {
    this.trovataConduzione = trovataConduzione;
  }

  /**
	 * Costruttore di default 
	 */
	public UnitaArboreaCCIAAVO() {
		super();
	}

  public boolean isPresenzaVitignoCCIAA()
  {
    return presenzaVitignoCCIAA;
  }

  public void setPresenzaVitignoCCIAA(boolean presenzaVitignoCCIAA)
  {
    this.presenzaVitignoCCIAA = presenzaVitignoCCIAA;
  }

  public boolean isTrovatoAlbo()
  {
    return trovatoAlbo;
  }

  public void setTrovatoAlbo(boolean trovatoAlbo)
  {
    this.trovatoAlbo = trovatoAlbo;
  }

  public Long getAnnoImpianto()
  {
    return annoImpianto;
  }

  public void setAnnoImpianto(Long annoImpianto)
  {
    this.annoImpianto = annoImpianto;
  }

  public Long getIdStoricoUnitaArborea()
  {
    return idStoricoUnitaArborea;
  }

  public void setIdStoricoUnitaArborea(Long idStoricoUnitaArborea)
  {
    this.idStoricoUnitaArborea = idStoricoUnitaArborea;
  }

  public Long getIdTipologiaVino()
  {
    return idTipologiaVino;
  }

  public void setIdTipologiaVino(Long idTipologiaVino)
  {
    this.idTipologiaVino = idTipologiaVino;
  }

  public boolean isModificaVITI()
  {
    return modificaVITI;
  }

  public void setModificaVITI(boolean modificaVITI)
  {
    this.modificaVITI = modificaVITI;
  }

  public boolean isConsolidamentoGis()
  {
    return consolidamentoGis;
  }

  public void setConsolidamentoGis(boolean consolidamentoGis)
  {
    this.consolidamentoGis = consolidamentoGis;
  }

  

	

	
	
}