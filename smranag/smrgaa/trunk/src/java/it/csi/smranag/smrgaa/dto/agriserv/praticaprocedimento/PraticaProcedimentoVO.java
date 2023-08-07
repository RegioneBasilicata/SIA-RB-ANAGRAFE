package it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento;

import java.io.Serializable;
import java.util.Date;

/**
 * Contiene i dati di una pratica di un procedimento relative ad una certa
 * particella Sep 4, 2008
 * 
 * @author TOBECONFIG
 */
public class PraticaProcedimentoVO implements Serializable
{
  /**
   * Flag per indicare al servizio di restituire solo l'ultimo stato delle pratiche
   */  
  public static final int FLAG_STATO_SOLO_ULTIMO = 0x00000001;
  /**
   * Flag per indicare al servizio di escludere le pratiche in bozza
   */  
  public static final int FLAG_STATO_ESCLUDI_BOZZA = 0x00000002;
  /**
   * Flag per indicare al servizio di escludere le pratiche respinte
   */  
  public static final int FLAG_STATO_ESCLUDI_RESPINTO = 0x00000004;
  /**
   * Flag per indicare al servizio di escludere le pratiche annullate
   */  
  public static final int FLAG_STATO_ESCLUDI_ANNULLATO = 0x00000008;
  /**
   * Flag per indicare al servizio di escludere le pratiche annullate per sostituzione
   */  
  public static final int FLAG_STATO_ESCLUDI_ANNULLATO_PER_SOSTITUZIONE = 0x00000010;
  /** serialVersionUID */
  private static final long   serialVersionUID                                  = -4391892627211412319L;
  private long                idAzienda;
  private int                 idProcedimento;
  private String              numeroPratica;
  private String              descrizione;
  private Long                idDichiarazioneConsistenza;
  private String              stato;
  private String              descrizioneStato;
  private Date                dataValiditaStato;
  private String              flagCessazioneAzAmmessa;
  private int                 annoCampagna;
  private Long                extIdAmmCompetenza;
  private long                idStoricoParticella;
  private PPUtilizzoVO        utilizzi[];

  /**
   * Valore possibile per il parametro tipologiaStati del servizio
   * agriservSearchPraticheProcedimento che indica di restituire tutte le
   * pratiche allo stato corrente <b>COMPRESE le pratiche in bozza o respinte</b>.
   */
  public static final int     TIPOLOGIA_PRATICHE_ULTIMO_STATO                   = 1;

  /**
   * Valore possibile per il parametro tipologiaStati del servizio
   * agriservSearchPraticheProcedimento che indica di restituire tutte le
   * pratiche allo stato corrente <b>ESCLUDENDO le pratiche in bozza o respinte</b>
   */
  public static final int     TIPOLOGIA_PRATICHE_ULTIMO_STATO_NO_RESPINTE_BOZZA = 2;

  public static final int     ORDINAMENTO_NESSUN_ORDINE                         = 0;
  public static final int     ORDINAMENTO_PER_PRATICHE                          = 1;
  public static final int     ORDINAMENTO_PER_PRATICHE_E_USI_DEL_SUOLO          = 2;

  public PPUtilizzoVO[] getUtilizzi()
  {
    return utilizzi;
  }

  public void setUtilizzi(PPUtilizzoVO utilizzi[])
  {
    this.utilizzi = utilizzi;
  }

  public long getIdAzienda()
  {
    return idAzienda;
  }

  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public int getIdProcedimento()
  {
    return idProcedimento;
  }

  public void setIdProcedimento(int idProcedimento)
  {
    this.idProcedimento = idProcedimento;
  }

  public String getNumeroPratica()
  {
    return numeroPratica;
  }

  public void setNumeroPratica(String numeroPratica)
  {
    this.numeroPratica = numeroPratica;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getStato()
  {
    return stato;
  }

  public void setStato(String stato)
  {
    this.stato = stato;
  }

  public String getDescrizioneStato()
  {
    return descrizioneStato;
  }

  public void setDescrizioneStato(String descrizioneStato)
  {
    this.descrizioneStato = descrizioneStato;
  }

  public Date getDataValiditaStato()
  {
    return dataValiditaStato;
  }

  public void setDataValiditaStato(Date dataValiditaStato)
  {
    this.dataValiditaStato = dataValiditaStato;
  }

  public String getFlagCessazioneAzAmmessa()
  {
    return flagCessazioneAzAmmessa;
  }

  public void setFlagCessazioneAzAmmessa(String flagCessazioneAzAmmessa)
  {
    this.flagCessazioneAzAmmessa = flagCessazioneAzAmmessa;
  }

  public int getAnnoCampagna()
  {
    return annoCampagna;
  }

  public void setAnnoCampagna(int annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }

  public Long getExtIdAmmCompetenza()
  {
    return extIdAmmCompetenza;
  }

  public void setExtIdAmmCompetenza(Long extIdAmmCompetenza)
  {
    this.extIdAmmCompetenza = extIdAmmCompetenza;
  }

  public long getIdStoricoParticella()
  {
    return idStoricoParticella;
  }

  public void setIdStoricoParticella(long idStoricoParticella)
  {
    this.idStoricoParticella = idStoricoParticella;
  }

  public Long getIdDichiarazioneConsistenza()
  {
    return idDichiarazioneConsistenza;
  }

  public void setIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
  {
    this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
  }
}
