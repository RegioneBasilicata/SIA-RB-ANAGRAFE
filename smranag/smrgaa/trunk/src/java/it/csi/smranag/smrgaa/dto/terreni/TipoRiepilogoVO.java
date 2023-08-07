package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_RIEPILOGO
 * 
 * @author TOBECONFIG
 *
 */
public class TipoRiepilogoVO implements Serializable 
{  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -1218757811108437951L;
  
  
  private long idTipoRiepilogo;
  private String nome;
  private String descrizione;
  private String flagFunzionalita;
  private String flagDefault;
  private String flagEscludiAsservimento;
  
  
  
  public long getIdTipoRiepilogo()
  {
    return idTipoRiepilogo;
  }
  public void setIdTipoRiepilogo(long idTipoRiepilogo)
  {
    this.idTipoRiepilogo = idTipoRiepilogo;
  }
  public String getNome()
  {
    return nome;
  }
  public void setNome(String nome)
  {
    this.nome = nome;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getFlagFunzionalita()
  {
    return flagFunzionalita;
  }
  public void setFlagFunzionalita(String flagFunzionalita)
  {
    this.flagFunzionalita = flagFunzionalita;
  }
  public String getFlagDefault()
  {
    return flagDefault;
  }
  public void setFlagDefault(String flagDefault)
  {
    this.flagDefault = flagDefault;
  }
  public String getFlagEscludiAsservimento()
  {
    return flagEscludiAsservimento;
  }
  public void setFlagEscludiAsservimento(String flagEscludiAsservimento)
  {
    this.flagEscludiAsservimento = flagEscludiAsservimento;
  }
  
  
  
  
}
