package it.csi.smranag.smrgaa.dto.anagrafe;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

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

public class AziendaColVarExcelVO implements Serializable
{  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 7220517728975642501L;
  
  
  
  private String cuaa;
  private String partitaIva;
  private String denominazione;
  private String codAttivitaAteco;
  private String comune;
  private String indirizzo;
  private String cap;
  private Date dataIngresso;
  private Date dataUscita;
  private Date dataInizioValidita;
  private Date dataFineValidita;
  private String datiUltimoAggiornamento;
  private Date dataUltimaValidazione;
  private Vector<ColturaVarietaVO> colturaVarieta;
  private Vector<FruttaGuscioVO> vFruttaGuscio;
  
  
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
  public String getCodAttivitaAteco()
  {
    return codAttivitaAteco;
  }
  public void setCodAttivitaAteco(String codAttivitaAteco)
  {
    this.codAttivitaAteco = codAttivitaAteco;
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
  public String getDatiUltimoAggiornamento()
  {
    return datiUltimoAggiornamento;
  }
  public void setDatiUltimoAggiornamento(String datiUltimoAggiornamento)
  {
    this.datiUltimoAggiornamento = datiUltimoAggiornamento;
  }
  public Date getDataUltimaValidazione()
  {
    return dataUltimaValidazione;
  }
  public void setDataUltimaValidazione(Date dataUltimaValidazione)
  {
    this.dataUltimaValidazione = dataUltimaValidazione;
  }
  public Vector<ColturaVarietaVO> getColturaVarieta()
  {
    return colturaVarieta;
  }
  public void setColturaVarieta(Vector<ColturaVarietaVO> colturaVarieta)
  {
    this.colturaVarieta = colturaVarieta;
  }
  public Vector<FruttaGuscioVO> getvFruttaGuscio()
  {
    return vFruttaGuscio;
  }
  public void setvFruttaGuscio(Vector<FruttaGuscioVO> vFruttaGuscio)
  {
    this.vFruttaGuscio = vFruttaGuscio;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
}
