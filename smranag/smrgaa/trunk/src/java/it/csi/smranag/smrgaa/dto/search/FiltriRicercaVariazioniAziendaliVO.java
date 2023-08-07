package it.csi.smranag.smrgaa.dto.search;

import it.csi.smranag.smrgaa.util.ErrorUtils;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.util.Date;


public class FiltriRicercaVariazioniAziendaliVO 
  implements Serializable
{
  
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -5325933205651888134L;
  
  private int               numTotRecord;
  private int               numMaxRecordExcel;
  private int               paginaCorrente;
  private int               passo;
  private int               primoElemento;
  OrdinamentoCampiVO        campiPerOrderBy;
  private String            istatProvAmmComp; //Amministrazione di competenza(Le provincie TOBECONFIG)
  private String            idTipoTipologiaVariazione; //Categoria di variazione
  private String            idVariazioneAziendale; //Tipologia di variazione
  private String            strDataVariazioneDal; //Data relativa al campo db_dettaglio_variazione_azi.data_variazione
  private String            strDataVariazioneAl;  //Data relativa al campo db_dettaglio_variazione_azi.data_variazione
  private Date              dataVariazioneDal;  //Data relativa al campo db_dettaglio_variazione_azi.data_variazione
  private Date              dataVariazioneAl;  //Data relativa al campo db_dettaglio_variazione_azi.data_variazione
  private String            cuaa;  //Codice CUAA relativo all’azienda attiva 
  private Boolean           presaVisione; //Presa Visione
  private boolean           variazioniStoricizzate; //Variazioni storicizzate 
  private String            provinciaRicerca; //Comune (sede legale)
  private String            comuneRicerca;  //Comune (sede legale)
  private String            istatComuneRicerca; //Comune (sede legale)
  
  //Costanti con i nomi dei campi da ordinare
  public static String PROV_COMP="PROV_COMP";
  public static String CUAA="AA.CUAA";
  public static String DENOMINAZIONE="AA.DENOMINAZIONE";
  public static String TIPOLOGIA_VARIAZIONE="TIPOLOGIA_VARIAZIONE";
  public static String VARIAZIONE="VARIAZIONE";
  public static String COMUNE="COMUME_SEDE_LEGALE";
  public static String DATA_VARIAZIONE="DVA.DATA_VARIAZIONE";
  

  public int getNumTotRecord()
  {
    return numTotRecord;
  }

  public void setNumTotRecord(int numTotRecord)
  {
    this.numTotRecord = numTotRecord;
  }
  
  public int getNumMaxRecordExcel()
  {
    return numMaxRecordExcel;
  }

  public void setNumMaxRecordExcel(int numMaxRecordExcel)
  {
    this.numMaxRecordExcel = numMaxRecordExcel;
  }
  
  public boolean isExcelOK()
  {
    if (numTotRecord<=numMaxRecordExcel) return true;
    return false;
  }
  
  public int getPaginaCorrente()
  {
    return paginaCorrente;
  }

  public void setPaginaCorrente(int paginaCorrente)
  {
    this.paginaCorrente = paginaCorrente;
  }
  
  public int getPasso()
  {
    return passo;
  }

  public void setPasso(int passo)
  {
    this.passo = passo;
  }
  
  public int getPrimoElemento()
  {
    return primoElemento;
  }

  public void setPrimoElemento(int primoElemento)
  {
    this.primoElemento = primoElemento;
  }

  public String getIstatProvAmmComp()
  {
    return istatProvAmmComp;
  }
  
  public OrdinamentoCampiVO getCampiPerOrderBy()
  {
    return campiPerOrderBy;
  }

  public void setCampiPerOrderBy(OrdinamentoCampiVO campiPerOrderBy)
  {
    this.campiPerOrderBy = campiPerOrderBy;
  }

  public void setIstatProvAmmComp(String istatProvAmmComp)
  {
    this.istatProvAmmComp = istatProvAmmComp;
  }

  public String getIdTipoTipologiaVariazione()
  {
    return idTipoTipologiaVariazione;
  }

  public void setIdTipoTipologiaVariazione(String idTipoTipologiaVariazione)
  {
    this.idTipoTipologiaVariazione = idTipoTipologiaVariazione;
  }

  public String getIdVariazioneAziendale()
  {
    return idVariazioneAziendale;
  }

  public void setIdVariazioneAziendale(String idVariazioneAziendale)
  {
    this.idVariazioneAziendale = idVariazioneAziendale;
  }

  public String getStrDataVariazioneDal()
  {
    return strDataVariazioneDal;
  }

  public void setStrDataVariazioneDal(String strDataVariazioneDal)
  {
    this.strDataVariazioneDal = strDataVariazioneDal;
  }

  public String getStrDataVariazioneAl()
  {
    return strDataVariazioneAl;
  }

  public void setStrDataVariazioneAl(String strDataVariazioneAl)
  {
    this.strDataVariazioneAl = strDataVariazioneAl;
  }

  public Date getDataVariazioneDal()
  {
    return dataVariazioneDal;
  }

  public void setDataVariazioneDal(Date dataVariazioneDal)
  {
    this.dataVariazioneDal = dataVariazioneDal;
  }

  public Date getDataVariazioneAl()
  {
    return dataVariazioneAl;
  }

  public void setDataVariazioneAl(Date dataVariazioneAl)
  {
    this.dataVariazioneAl = dataVariazioneAl;
  }

  public String getCuaa()
  {
    return cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public Boolean getPresaVisione()
  {
    return presaVisione;
  }

  public void setPresaVisione(Boolean presaVisione)
  {
    this.presaVisione = presaVisione;
  }

  public boolean isVariazioniStoricizzate()
  {
    return variazioniStoricizzate;
  }

  public void setVariazioniStoricizzate(boolean variazioniStoricizzate)
  {
    this.variazioniStoricizzate = variazioniStoricizzate;
  }
  
  public String getProvinciaRicerca()
  {
    return provinciaRicerca;
  }

  public void setProvinciaRicerca(String provinciaRicerca)
  {
    this.provinciaRicerca = provinciaRicerca;
  }

  public String getComuneRicerca()
  {
    return comuneRicerca;
  }

  public void setComuneRicerca(String comuneRicerca)
  {
    this.comuneRicerca = comuneRicerca;
  }

  public String getIstatComuneRicerca()
  {
    return istatComuneRicerca;
  }

  public void setIstatComuneRicerca(String istatComuneRicerca)
  {
    this.istatComuneRicerca = istatComuneRicerca;
  }
  
  
  //Metodo per effettuare la validazione formale dei dati:utilizzato nella
  //funzione ricerca variazioni aziendali
  public ValidationErrors validateRicercaVariazioni(ValidationErrors errors)
  {
    
    //Se l'utente ha inserito il CUAA controllo che sia formalmente 
    //corretto
    if(Validator.isNotEmpty(cuaa))
    {      
      // Se si tratta di un codice fiscale
      if(cuaa.length() == 16) 
      {
        if(!Validator.controlloCf(cuaa)) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "cuaa", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
        }
      }
      else if (cuaa.length() == 11) 
      {
        if(!Validator.controlloPIVA(cuaa)) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "cuaa", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
        }
      }
      else //diverso da 11 e da 16
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "cuaa", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
      }
    }
    
    dataVariazioneAl = Validator.validateDateAll(strDataVariazioneAl, "dataVariazioneAl", "Al", errors, false, false);
    dataVariazioneDal = Validator.validateDateAll(strDataVariazioneDal, "dataVariazioneDal", "Dal", errors, false, false);
    //Se le date sono entrambe valorizzate controllo che dataAl sia >= di dataDal
    if (dataVariazioneAl!=null && dataVariazioneDal!=null && dataVariazioneDal.after(dataVariazioneAl))
    {
      errors.add("dataVariazioneAl",new ValidationError("La data Al deve essere maggiore o uguale alla data Dal"));
      errors.add("dataVariazioneDal",new ValidationError("La data Al deve essere maggiore o uguale alla data Dal"));
    }
    

    return errors;
  }
}
