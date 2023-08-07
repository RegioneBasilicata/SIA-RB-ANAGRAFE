package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_R_CATALOGO_MATRICE
 * 
 * @author TOBECONFIG
 *
 */
public class CatalogoMatriceVO implements Serializable 
{  
  
 
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -7753018929040659066L;
  
  
  
  private long idCatalogoMatrice;
  private long idUtilizzo;
  private long idTipoDestinazione; 
  private long idTipoDettaglioUso;
  private long idTipoQualitaUso;
  private long idVarieta;
  private String flagNonAmmissibileEfa;
  private String flagPratoPermanente;
  private String flagColturaSommersa;
  private String flagRiposo;
  private String flagPratoForaggera;
  private String flagLeguminosa;
  private String flagProteaginosa;
  private String flagBiologico;
  private String flagSeminativo;
  private String permanente;
  private BigDecimal coefficienteRiduzione;
  private Date dataFineValidita;
  private String flagUnarModificabile;
  
  
  
  
  public long getIdCatalogoMatrice()
  {
    return idCatalogoMatrice;
  }
  public void setIdCatalogoMatrice(long idCatalogoMatrice)
  {
    this.idCatalogoMatrice = idCatalogoMatrice;
  }
  public long getIdTipoDestinazione()
  {
    return idTipoDestinazione;
  }
  public void setIdTipoDestinazione(long idTipoDestinazione)
  {
    this.idTipoDestinazione = idTipoDestinazione;
  }
  public long getIdVarieta()
  {
    return idVarieta;
  }
  public void setIdVarieta(long idVarieta)
  {
    this.idVarieta = idVarieta;
  }
  public String getFlagNonAmmissibileEfa()
  {
    return flagNonAmmissibileEfa;
  }
  public void setFlagNonAmmissibileEfa(String flagNonAmmissibileEfa)
  {
    this.flagNonAmmissibileEfa = flagNonAmmissibileEfa;
  }
  public String getFlagPratoPermanente()
  {
    return flagPratoPermanente;
  }
  public void setFlagPratoPermanente(String flagPratoPermanente)
  {
    this.flagPratoPermanente = flagPratoPermanente;
  }
  public String getFlagColturaSommersa()
  {
    return flagColturaSommersa;
  }
  public void setFlagColturaSommersa(String flagColturaSommersa)
  {
    this.flagColturaSommersa = flagColturaSommersa;
  }
  public String getFlagRiposo()
  {
    return flagRiposo;
  }
  public void setFlagRiposo(String flagRiposo)
  {
    this.flagRiposo = flagRiposo;
  }
  public String getFlagPratoForaggera()
  {
    return flagPratoForaggera;
  }
  public void setFlagPratoForaggera(String flagPratoForaggera)
  {
    this.flagPratoForaggera = flagPratoForaggera;
  }
  public String getFlagLeguminosa()
  {
    return flagLeguminosa;
  }
  public void setFlagLeguminosa(String flagLeguminosa)
  {
    this.flagLeguminosa = flagLeguminosa;
  }
  public String getFlagProteaginosa()
  {
    return flagProteaginosa;
  }
  public void setFlagProteaginosa(String flagProteaginosa)
  {
    this.flagProteaginosa = flagProteaginosa;
  }
  public String getFlagBiologico()
  {
    return flagBiologico;
  }
  public void setFlagBiologico(String flagBiologico)
  {
    this.flagBiologico = flagBiologico;
  }
  public String getFlagSeminativo()
  {
    return flagSeminativo;
  }
  public void setFlagSeminativo(String flagSeminativo)
  {
    this.flagSeminativo = flagSeminativo;
  }
  public long getIdTipoDettaglioUso()
  {
    return idTipoDettaglioUso;
  }
  public void setIdTipoDettaglioUso(long idTipoDettaglioUso)
  {
    this.idTipoDettaglioUso = idTipoDettaglioUso;
  }
  public String getPermanente()
  {
    return permanente;
  }
  public void setPermanente(String permanente)
  {
    this.permanente = permanente;
  }
  public BigDecimal getCoefficienteRiduzione()
  {
    return coefficienteRiduzione;
  }
  public void setCoefficienteRiduzione(BigDecimal coefficienteRiduzione)
  {
    this.coefficienteRiduzione = coefficienteRiduzione;
  }
  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }
  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }
  public long getIdUtilizzo()
  {
    return idUtilizzo;
  }
  public void setIdUtilizzo(long idUtilizzo)
  {
    this.idUtilizzo = idUtilizzo;
  }
  public long getIdTipoQualitaUso()
  {
    return idTipoQualitaUso;
  }
  public void setIdTipoQualitaUso(long idTipoQualitaUso)
  {
    this.idTipoQualitaUso = idTipoQualitaUso;
  }
  public String getFlagUnarModificabile()
  {
    return flagUnarModificabile;
  }
  public void setFlagUnarModificabile(String flagUnarModificabile)
  {
    this.flagUnarModificabile = flagUnarModificabile;
  }
  
  
  
  
  
  
  
}
