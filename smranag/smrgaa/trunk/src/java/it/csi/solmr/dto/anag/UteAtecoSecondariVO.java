package it.csi.solmr.dto.anag;

import java.io.Serializable;
import java.util.Date;

public class UteAtecoSecondariVO implements Serializable {
  
	

	/**
   * 
   */
  private static final long serialVersionUID = 2268080923180027267L;
  
  
  
  
  private long idUteAtecoSecondari;
	private long idUte;
	private long idAttivitaAteco;
	private Date dataInizioValidita;
	private Date dataFineValidita;
	private String descAttivitaAteco;
	private String codiceAteco;
	
	
  public long getIdUteAtecoSecondari()
  {
    return idUteAtecoSecondari;
  }
  public void setIdUteAtecoSecondari(long idUteAtecoSecondari)
  {
    this.idUteAtecoSecondari = idUteAtecoSecondari;
  }
  public long getIdUte()
  {
    return idUte;
  }
  public void setIdUte(long idUte)
  {
    this.idUte = idUte;
  }
  public long getIdAttivitaAteco()
  {
    return idAttivitaAteco;
  }
  public void setIdAttivitaAteco(long idAttivitaAteco)
  {
    this.idAttivitaAteco = idAttivitaAteco;
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
  public String getDescAttivitaAteco()
  {
    return descAttivitaAteco;
  }
  public void setDescAttivitaAteco(String descAttivitaAteco)
  {
    this.descAttivitaAteco = descAttivitaAteco;
  }
  public String getCodiceAteco()
  {
    return codiceAteco;
  }
  public void setCodiceAteco(String codiceAteco)
  {
    this.codiceAteco = codiceAteco;
  }
	
	
	
	
}
