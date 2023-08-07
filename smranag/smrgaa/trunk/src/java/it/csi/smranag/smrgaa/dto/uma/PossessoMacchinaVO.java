package it.csi.smranag.smrgaa.dto.uma;

import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati macchine agricole
 * 
 * @author TOBECONFIG
 *
 */
public class PossessoMacchinaVO implements Serializable 
{
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -2097312102443181336L;
  
  
  
  private long idPossessoMacchina;
  private String comunUte;
  private String sglProvinciaUte;
  private String indirizzoUte;
  private MacchinaGaaVO macchinaVO;
  private String dittaUma;
  private String siglaProvinciaUma;
  private String cuaa;
  private String partitaIva;
  private String denominazione;
  private TipoFormaPossessoGaaVO tipoFormaPossessoGaaVO;
  private Date dataCarico;
  private Date dataScarico;
  private Date dataInizioValidita;
  private Date dataFineValidita;
  private String descTipoScarico;
  private BigDecimal percentualePossesso;
  private String denominazioneAzLeasing;
  private Date dataScadenzaLeasing;
  private String comunSedeLeg;
  private String sglProvinciaSedeLeg;
  private String indirizzoSedeLeg;
  private String capSedeLeg;
  private Long idUte;
  private Long idTipoFormaPossesso;
  private Long extIdUtenteAggiornamento;
  private Long idScarico;
  private String flagValida;
  
  
  
  
  public long getIdPossessoMacchina()
  {
    return idPossessoMacchina;
  }
  public void setIdPossessoMacchina(long idPossessoMacchina)
  {
    this.idPossessoMacchina = idPossessoMacchina;
  }
  public String getComunUte()
  {
    return comunUte;
  }
  public void setComunUte(String comunUte)
  {
    this.comunUte = comunUte;
  }
  public String getSglProvinciaUte()
  {
    return sglProvinciaUte;
  }
  public void setSglProvinciaUte(String sglProvinciaUte)
  {
    this.sglProvinciaUte = sglProvinciaUte;
  }
  public String getIndirizzoUte()
  {
    return indirizzoUte;
  }
  public void setIndirizzoUte(String indirizzoUte)
  {
    this.indirizzoUte = indirizzoUte;
  }
  public MacchinaGaaVO getMacchinaVO()
  {
    return macchinaVO;
  }
  public void setMacchinaVO(MacchinaGaaVO macchinaVO)
  {
    this.macchinaVO = macchinaVO;
  }
  public String getDittaUma()
  {
    return dittaUma;
  }
  public void setDittaUma(String dittaUma)
  {
    this.dittaUma = dittaUma;
  }
  public String getSiglaProvinciaUma()
  {
    return siglaProvinciaUma;
  }
  public void setSiglaProvinciaUma(String siglaProvinciaUma)
  {
    this.siglaProvinciaUma = siglaProvinciaUma;
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
  public TipoFormaPossessoGaaVO getTipoFormaPossessoGaaVO()
  {
    return tipoFormaPossessoGaaVO;
  }
  public void setTipoFormaPossessoGaaVO(
      TipoFormaPossessoGaaVO tipoFormaPossessoGaaVO)
  {
    this.tipoFormaPossessoGaaVO = tipoFormaPossessoGaaVO;
  }
  public Date getDataCarico()
  {
    return dataCarico;
  }
  public void setDataCarico(Date dataCarico)
  {
    this.dataCarico = dataCarico;
  }
  public Date getDataScarico()
  {
    return dataScarico;
  }
  public void setDataScarico(Date dataScarico)
  {
    this.dataScarico = dataScarico;
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
  public String getDescTipoScarico()
  {
    return descTipoScarico;
  }
  public void setDescTipoScarico(String descTipoScarico)
  {
    this.descTipoScarico = descTipoScarico;
  }
  public BigDecimal getPercentualePossesso()
  {
    return percentualePossesso;
  }
  public void setPercentualePossesso(BigDecimal percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }
  public String getDenominazioneAzLeasing()
  {
    return denominazioneAzLeasing;
  }
  public void setDenominazioneAzLeasing(String denominazioneAzLeasing)
  {
    this.denominazioneAzLeasing = denominazioneAzLeasing;
  }
  public Date getDataScadenzaLeasing()
  {
    return dataScadenzaLeasing;
  }
  public void setDataScadenzaLeasing(Date dataScadenzaLeasing)
  {
    this.dataScadenzaLeasing = dataScadenzaLeasing;
  }
  public String getComunSedeLeg()
  {
    return comunSedeLeg;
  }
  public void setComunSedeLeg(String comunSedeLeg)
  {
    this.comunSedeLeg = comunSedeLeg;
  }
  public String getSglProvinciaSedeLeg()
  {
    return sglProvinciaSedeLeg;
  }
  public void setSglProvinciaSedeLeg(String sglProvinciaSedeLeg)
  {
    this.sglProvinciaSedeLeg = sglProvinciaSedeLeg;
  }
  public String getIndirizzoSedeLeg()
  {
    return indirizzoSedeLeg;
  }
  public void setIndirizzoSedeLeg(String indirizzoSedeLeg)
  {
    this.indirizzoSedeLeg = indirizzoSedeLeg;
  }
  public String getCapSedeLeg()
  {
    return capSedeLeg;
  }
  public void setCapSedeLeg(String capSedeLeg)
  {
    this.capSedeLeg = capSedeLeg;
  }
  public Long getIdUte()
  {
    return idUte;
  }
  public void setIdUte(Long idUte)
  {
    this.idUte = idUte;
  }
  public Long getIdTipoFormaPossesso()
  {
    return idTipoFormaPossesso;
  }
  public void setIdTipoFormaPossesso(Long idTipoFormaPossesso)
  {
    this.idTipoFormaPossesso = idTipoFormaPossesso;
  }
  public Long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }
  public void setExtIdUtenteAggiornamento(Long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }
  public Long getIdScarico()
  {
    return idScarico;
  }
  public void setIdScarico(Long idScarico)
  {
    this.idScarico = idScarico;
  }
  
  
  public String getFlagValida()
  {
    return flagValida;
  }
  public void setFlagValida(String flagValida)
  {
    this.flagValida = flagValida;
  }
  
  
  public boolean equalsDatiPossesso(Object obj) 
  {
    final PossessoMacchinaVO other = (PossessoMacchinaVO) obj;
    //PERCENTUALE_POSSESSO
    if(Validator.isEmpty(this.percentualePossesso)) 
    {
      if(Validator.isNotEmpty(other.percentualePossesso)) 
      {
        return false;
      }
    } 
    else if (Validator.isNotEmpty(this.percentualePossesso))
    {
      if(Validator.isEmpty(other.percentualePossesso))
      {
        return false;
      }
      else if(this.percentualePossesso.compareTo(other.percentualePossesso) !=0)
        return false;
    }
    // ID_TIPO_FORMA_POSSESSO
    if(Validator.isEmpty(this.idTipoFormaPossesso)) {
      if(Validator.isNotEmpty(other.idTipoFormaPossesso)) {
        return false;
      }
    }
    else if (Validator.isNotEmpty(this.idTipoFormaPossesso)) {
      if(Validator.isEmpty(other.idTipoFormaPossesso)) 
      {
        return false;
      }
      else if(this.idTipoFormaPossesso.compareTo(other.idTipoFormaPossesso) !=0)
        return false;
    }
    
    
    return true;
  }
  
  
  
  
}
