package it.csi.smranag.smrgaa.dto.agriserv.domande;

import java.io.*;

/**
 * Value object utilizzato da agriservSearchDomande
 *
 * @author TOBECONFIG
 * @since 2.5.0
 */
public class DomandaVO implements Serializable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -1872369475789872055L;

  private String numeroPratica;  //Numero pratica nel db del procedimento chiamato
  private String codiceAgea;  //Codice AGEA della pratica nel db del procedimento chiamato
  private long idAzienda;  // idAzienda dell’azienda associato alla domanda
  private Integer annoPratica;  //Viene valorizzato con l’anno della pratica.
  private Integer annoCampagna;  //Viene valorizzato con l’anno campagna della domanda, se esiste.
  private String ammCompetenza;  //Codice dell’amministrazione di competenza della pratica
  private StatoPraticaVO[] statiPratica;  //Storico degli stati attraverso cui è passata la pratica legata alla domanda.
  private DecodificaEstesaVO livello;  //Livello della pratica (id, codice, descrizione)
  private DecodificaVO aiuto; //Aiuto della pratica (id, descrizione)
  private long extIdProcedimento; //Identificativo del procedimento associato alla pratica

  public DomandaVO()
  {
  }

  public String getNumeroPratica()
  {
    return numeroPratica;
  }
  public void setNumeroPratica(String numeroPratica)
  {
    this.numeroPratica = numeroPratica;
  }

  public String getCodiceAgea()
  {
    return codiceAgea;
  }
  public void setCodiceAgea(String codiceAgea)
  {
    this.codiceAgea = codiceAgea;
  }

  public long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public Integer getAnnoPratica()
  {
    return annoPratica;
  }
  public void setAnnoPratica(Integer annoPratica)
  {
    this.annoPratica = annoPratica;
  }

  public Integer getAnnoCampagna()
  {
    return annoCampagna;
  }
  public void setAnnoCampagna(Integer annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }

  public String getAmmCompetenza()
  {
    return ammCompetenza;
  }
  public void setAmmCompetenza(String ammCompetenza)
  {
    this.ammCompetenza = ammCompetenza;
  }

  public StatoPraticaVO[] getStatiPratica()
  {
    return statiPratica;
  }
  public void setStatiPratica(StatoPraticaVO[] statiPratica)
  {
    this.statiPratica = statiPratica;
  }

  public DecodificaEstesaVO getLivello()
  {
    return livello;
  }
  public void setLivello(DecodificaEstesaVO livello)
  {
    this.livello = livello;
  }

  public DecodificaVO getAiuto()
  {
    return aiuto;
  }
  public void setAiuto(DecodificaVO aiuto)
  {
    this.aiuto = aiuto;
  }
  
  public long getExtIdProcedimento()
  {
    return extIdProcedimento;
  }

  public void setExtIdProcedimento(long extIdProcedimento)
  {
    this.extIdProcedimento = extIdProcedimento;
  }
}
