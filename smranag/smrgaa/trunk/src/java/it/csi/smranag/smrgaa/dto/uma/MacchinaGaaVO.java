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
public class MacchinaGaaVO implements Serializable 
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -3691626761071170108L;
  
  
  
  private long idMacchina;
  private TipoGenereMacchinaGaaVO genereMacchinaVO;
  private TipoCategoriaGaaVO tipoCategoriaVO;
  private Long idMarca;
  private String descMarca;
  private String modello;
  private NumeroTargaGaaVO lastNumeroTargaVO;
  private Integer annoCostruzione;
  private String matricolaTelaio;
  private String matricolaMotore;
  private Integer calorie;
  private Integer potenzaKw;
  private Integer potenzaCv;
  private Integer idAlimentazione;
  private String descTipoAlimentazione;
  private Integer idNazionalita;
  private String descTipoNazionalita;
  private BigDecimal lordo;
  private BigDecimal tara;
  private String note;
  private long extIdUtenteAggiornamento;
  private Date dataAggiornamento;
  private Long idGenereMacchina;
  private Long idCategoria;
  private String numeroOmologazione;
  private String numeroAssi;
  private String illuminazione;
  private String descTipoTrazione;
  
  
  
  public long getIdMacchina()
  {
    return idMacchina;
  }
  public void setIdMacchina(long idMacchina)
  {
    this.idMacchina = idMacchina;
  }
  public TipoGenereMacchinaGaaVO getGenereMacchinaVO()
  {
    return genereMacchinaVO;
  }
  public void setGenereMacchinaVO(TipoGenereMacchinaGaaVO genereMacchinaVO)
  {
    this.genereMacchinaVO = genereMacchinaVO;
  }
  public TipoCategoriaGaaVO getTipoCategoriaVO()
  {
    return tipoCategoriaVO;
  }
  public void setTipoCategoriaVO(TipoCategoriaGaaVO tipoCategoriaVO)
  {
    this.tipoCategoriaVO = tipoCategoriaVO;
  }
  public Long getIdMarca()
  {
    return idMarca;
  }
  public void setIdMarca(Long idMarca)
  {
    this.idMarca = idMarca;
  }
  public String getDescMarca()
  {
    return descMarca;
  }
  public void setDescMarca(String descMarca)
  {
    this.descMarca = descMarca;
  }
  public String getModello()
  {
    return modello;
  }
  public void setModello(String modello)
  {
    this.modello = modello;
  }
 
  public Integer getAnnoCostruzione()
  {
    return annoCostruzione;
  }
  public void setAnnoCostruzione(Integer annoCostruzione)
  {
    this.annoCostruzione = annoCostruzione;
  }
  public String getMatricolaTelaio()
  {
    return matricolaTelaio;
  }
  public void setMatricolaTelaio(String matricolaTelaio)
  {
    this.matricolaTelaio = matricolaTelaio;
  }
  public String getMatricolaMotore()
  {
    return matricolaMotore;
  }
  public void setMatricolaMotore(String matricolaMotore)
  {
    this.matricolaMotore = matricolaMotore;
  }
  public Integer getCalorie()
  {
    return calorie;
  }
  public void setCalorie(Integer calorie)
  {
    this.calorie = calorie;
  }
  public Integer getPotenzaKw()
  {
    return potenzaKw;
  }
  public void setPotenzaKw(Integer potenzaKw)
  {
    this.potenzaKw = potenzaKw;
  }
  public Integer getPotenzaCv()
  {
    return potenzaCv;
  }
  public void setPotenzaCv(Integer potenzaCv)
  {
    this.potenzaCv = potenzaCv;
  }
  public Integer getIdAlimentazione()
  {
    return idAlimentazione;
  }
  public void setIdAlimentazione(Integer idAlimentazione)
  {
    this.idAlimentazione = idAlimentazione;
  }
  public String getDescTipoAlimentazione()
  {
    return descTipoAlimentazione;
  }
  public void setDescTipoAlimentazione(String descTipoAlimentazione)
  {
    this.descTipoAlimentazione = descTipoAlimentazione;
  }
  public Integer getIdNazionalita()
  {
    return idNazionalita;
  }
  public void setIdNazionalita(Integer idNazionalita)
  {
    this.idNazionalita = idNazionalita;
  }
  public String getDescTipoNazionalita()
  {
    return descTipoNazionalita;
  }
  public void setDescTipoNazionalita(String descTipoNazionalita)
  {
    this.descTipoNazionalita = descTipoNazionalita;
  }
  public BigDecimal getLordo()
  {
    return lordo;
  }
  public void setLordo(BigDecimal lordo)
  {
    this.lordo = lordo;
  }
  public BigDecimal getTara()
  {
    return tara;
  }
  public void setTara(BigDecimal tara)
  {
    this.tara = tara;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }
  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }
  public NumeroTargaGaaVO getLastNumeroTargaVO()
  {
    return lastNumeroTargaVO;
  }
  public void setLastNumeroTargaVO(NumeroTargaGaaVO lastNumeroTargaVO)
  {
    this.lastNumeroTargaVO = lastNumeroTargaVO;
  }
  public Long getIdGenereMacchina()
  {
    return idGenereMacchina;
  }
  public void setIdGenereMacchina(Long idGenereMacchina)
  {
    this.idGenereMacchina = idGenereMacchina;
  }
  public Long getIdCategoria()
  {
    return idCategoria;
  }
  public void setIdCategoria(Long idCategoria)
  {
    this.idCategoria = idCategoria;
  }
  
  
  public boolean equalsDatiMacchinaConNote(Object obj) 
  {
    final MacchinaGaaVO other = (MacchinaGaaVO) obj;
    //ANNO_COSTRUZIONE
    if(Validator.isEmpty(this.annoCostruzione)) 
    {
      if(Validator.isNotEmpty(other.annoCostruzione)) 
      {
        return false;
      }
    } 
    else if (Validator.isNotEmpty(this.annoCostruzione))
    {
      if(Validator.isEmpty(other.annoCostruzione))
      {
        return false;
      }
      else if(this.annoCostruzione.compareTo(other.annoCostruzione) !=0)
        return false;
    }
    // ID_MARCA
    if(Validator.isEmpty(this.idMarca)) {
      if(Validator.isNotEmpty(other.idMarca)) {
        return false;
      }
    }
    else if (Validator.isNotEmpty(this.idMarca)) {
      if(Validator.isEmpty(other.idMarca)) 
      {
        return false;
      }
      else if(this.idMarca.compareTo(other.idMarca) !=0)
        return false;
    }
    // MODELLO
    if(Validator.isEmpty(this.modello)) {
      if(Validator.isNotEmpty(other.modello)) {
        return false;
      }
    }
    else if (Validator.isNotEmpty(this.modello))
    {
      if(Validator.isEmpty(other.modello)) 
      {
        return false;
      }
      else if(!this.modello.equalsIgnoreCase(other.modello))
        return false; 
    }
    //matricola telaio
    if(Validator.isEmpty(this.matricolaTelaio)) {
      if(Validator.isNotEmpty(other.matricolaTelaio)) {
        return false;
      }
    }
    else if (Validator.isNotEmpty(this.matricolaTelaio))
    {
      if(Validator.isEmpty(other.matricolaTelaio)) 
      {
        return false;
      }
      else if(!this.matricolaTelaio.equalsIgnoreCase(other.matricolaTelaio))
        return false; 
    }
    //note
    if(Validator.isEmpty(this.note)) {
      if(Validator.isNotEmpty(other.note)) {
        return false;
      }
    }
    else if (Validator.isNotEmpty(this.note))
    {
      if(Validator.isEmpty(other.note)) 
      {
        return false;
      }
      else if(!this.note.equalsIgnoreCase(other.note))
        return false; 
    }
    
    
    return true;
  }
  
  
  
  public boolean equalsDatiMacchina(Object obj) 
  {
    final MacchinaGaaVO other = (MacchinaGaaVO) obj;
    //ANNO_COSTRUZIONE
    if(Validator.isEmpty(this.annoCostruzione)) 
    {
      if(Validator.isNotEmpty(other.annoCostruzione)) 
      {
        return false;
      }
    } 
    else if (Validator.isNotEmpty(this.annoCostruzione))
    {
      if(Validator.isEmpty(other.annoCostruzione))
      {
        return false;
      }
      else if(this.annoCostruzione.compareTo(other.annoCostruzione) !=0)
        return false;
    }
    // ID_MARCA
    if(Validator.isEmpty(this.idMarca)) {
      if(Validator.isNotEmpty(other.idMarca)) {
        return false;
      }
    }
    else if (Validator.isNotEmpty(this.idMarca)) {
      if(Validator.isEmpty(other.idMarca)) 
      {
        return false;
      }
      else if(this.idMarca.compareTo(other.idMarca) !=0)
        return false;
    }
    // MODELLO
    if(Validator.isEmpty(this.modello)) {
      if(Validator.isNotEmpty(other.modello)) {
        return false;
      }
    }
    else if (Validator.isNotEmpty(this.modello))
    {
      if(Validator.isEmpty(other.modello)) 
      {
        return false;
      }
      else if(!this.modello.equalsIgnoreCase(other.modello))
        return false; 
    }
    //matricola telaio
    if(Validator.isEmpty(this.matricolaTelaio)) {
      if(Validator.isNotEmpty(other.matricolaTelaio)) {
        return false;
      }
    }
    else if (Validator.isNotEmpty(this.matricolaTelaio))
    {
      if(Validator.isEmpty(other.matricolaTelaio)) 
      {
        return false;
      }
      else if(!this.matricolaTelaio.equalsIgnoreCase(other.matricolaTelaio))
        return false; 
    }    
    
    return true;
  }
  
  public String getNumeroOmologazione()
  {
    return numeroOmologazione;
  }
  public void setNumeroOmologazione(String numeroOmologazione)
  {
    this.numeroOmologazione = numeroOmologazione;
  }
  public String getNumeroAssi()
  {
    return numeroAssi;
  }
  public void setNumeroAssi(String numeroAssi)
  {
    this.numeroAssi = numeroAssi;
  }
  public String getIlluminazione()
  {
    return illuminazione;
  }
  public void setIlluminazione(String illuminazione)
  {
    this.illuminazione = illuminazione;
  }
  public String getDescTipoTrazione()
  {
    return descTipoTrazione;
  }
  public void setDescTipoTrazione(String descTipoTrazione)
  {
    this.descTipoTrazione = descTipoTrazione;
  }
 
  
  
  
}
