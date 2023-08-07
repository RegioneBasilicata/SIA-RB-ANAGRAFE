package it.csi.solmr.dto.anag;

import it.csi.smranag.smrgaa.util.ErrorUtils;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca D.
 * @version 1.0
 */

public class AziendaCollegataVO implements Serializable {
	/**
   * 
   */
  private static final long serialVersionUID = 4655298356813587906L;
  /**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	
  private Long idAziendaCollegata = null;
  private Long idAzienda= null;
  private Long idAziendaAssociata = null;
  private Date dataIngresso = null;
  private String dataIngressoStr = null;
  private Date dataInizioValidita = null;
  private Date dataUscita = null;
  private String dataUscitaStr = null;
  private Date dataFineValidita = null;
  private Date dataAggiornamento = null;
  private Long idUtenteAggiornamento = null;
  private String denominazione = null;
  private SoggettoAssociatoVO soggettoAssociato = null;
  private String sglProv;
  private String denominazioneComune;
  private String istatComune;
  private String cuaa;
  private String partitaIva;
  private String indirizzo;
  private String cap;
  private Long idSoggettoAssociato = null;
  private String sedeEstero = null;
  private String sedeCittaEstero = null;
  private String descrizioneUtenteModifica = null;
  private String descrizioneEnteUtenteModifica = null;
  
  
  public String getDescrizioneUtenteModifica()
  {
    return descrizioneUtenteModifica;
  }
  public void setDescrizioneUtenteModifica(String descrizioneUtenteModifica)
  {
    this.descrizioneUtenteModifica = descrizioneUtenteModifica;
  }
  public String getDescrizioneEnteUtenteModifica()
  {
    return descrizioneEnteUtenteModifica;
  }
  public void setDescrizioneEnteUtenteModifica(
      String descrizioneEnteUtenteModifica)
  {
    this.descrizioneEnteUtenteModifica = descrizioneEnteUtenteModifica;
  }
  public String getSedeEstero()
  {
    return sedeEstero;
  }
  public void setSedeEstero(String sedeEstero)
  {
    this.sedeEstero = sedeEstero;
  }
  public String getSedeCittaEstero()
  {
    return sedeCittaEstero;
  }
  public void setSedeCittaEstero(String sedeCittaEstero)
  {
    this.sedeCittaEstero = sedeCittaEstero;
  }
  public Long getIdSoggettoAssociato()
  {
    return idSoggettoAssociato;
  }
  public void setIdSoggettoAssociato(Long idSoggettoAssociato)
  {
    this.idSoggettoAssociato = idSoggettoAssociato;
  }
  public String getCap()
  {
    return cap;
  }
  public void setCap(String cap)
  {
    this.cap = cap;
  }
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
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
  public String getSglProv()
  {
    return sglProv;
  }
  public void setSglProv(String sglProv)
  {
    this.sglProv = sglProv;
  }
  public String getDenominazioneComune()
  {
    return denominazioneComune;
  }
  public void setDenominazioneComune(String denominazioneComune)
  {
    this.denominazioneComune = denominazioneComune;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public Long getIdAziendaCollegata()
  {
    return idAziendaCollegata;
  }
  public void setIdAziendaCollegata(Long idAziendaCollegata)
  {
    this.idAziendaCollegata = idAziendaCollegata;
  }
  public Long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public Long getIdAziendaAssociata()
  {
    return idAziendaAssociata;
  }
  public void setIdAziendaAssociata(Long idAziendaAssociata)
  {
    this.idAziendaAssociata = idAziendaAssociata;
  }
  public Date getDataIngresso()
  {
    return dataIngresso;
  }
  public void setDataIngresso(Date dataIngresso)
  {
    this.dataIngresso = dataIngresso;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  public Date getDataUscita()
  {
    return dataUscita;
  }
  public void setDataUscita(Date dataUscita)
  {
    this.dataUscita = dataUscita;
  }
  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }
  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }
  public Long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }
  public void setIdUtenteAggiornamento(Long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public String getDataUscitaStr()
  {
    return dataUscitaStr;
  }
  public void setDataUscitaStr(String dataUscitaStr)
  {
    this.dataUscitaStr = dataUscitaStr;
  }
  public String getDataIngressoStr()
  {
    return dataIngressoStr;
  }
  public void setDataIngressoStr(String dataIngressoStr)
  {
    this.dataIngressoStr = dataIngressoStr;
  }
  
	
  public ValidationErrors validateInsertAziende() {
    ValidationErrors errors = null;
    
    if(Validator.isNotEmpty(dataIngressoStr))
    {
      try
      {
        if(Validator.validateDateF(dataIngressoStr))
        {
          dataIngresso = DateUtils.parse(dataIngressoStr, "dd/MM/yyyy");
        }
        else
        {
          errors = setErrorsControlNull("dataIngresso",errors,AnagErrors.ERRORE_FROMATO_DATA);
        }
      }
      catch(Exception ex)
      {
        errors = setErrorsControlNull("dataIngresso",errors,AnagErrors.ERRORE_FROMATO_DATA);
      }
    }
    else
    {
      errors = setErrorsControlNull("dataIngresso",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);  
    }
    
    if(Validator.isNotEmpty(dataUscitaStr))
    {
      try
      {
        if(Validator.validateDateF(dataUscitaStr))
        {
          dataUscita = DateUtils.parse(dataUscitaStr, "dd/MM/yyyy");
        }
        else
        {
          errors = setErrorsControlNull("dataUscita",errors,AnagErrors.ERRORE_FROMATO_DATA);
        }
      }
      catch(Exception ex)
      {
        errors = setErrorsControlNull("dataUscita",errors,AnagErrors.ERRORE_FROMATO_DATA);
      }
    }
    
    if(Validator.isNotEmpty(dataIngresso) && Validator.isNotEmpty(dataUscita))
    {
      if(dataIngresso.after(dataUscita))
      {
        errors = setErrorsControlNull("dataIngresso",errors,AnagErrors.ERRORE_DATA_INGRESSO_POST_DATA_USCITA);
        errors = setErrorsControlNull("dataUscita",errors,AnagErrors.ERRORE_DATA_INGRESSO_POST_DATA_USCITA);
      }
    }
    

    return errors;
  }
  
  private ValidationErrors setErrorsControlNull(String nomeCampo, ValidationErrors errors, String labelErrore)
  {
    if(errors == null)
    {
      errors = new ValidationErrors();
    }
    errors.add(nomeCampo, new ValidationError(labelErrore));
    
    return errors; 
  }
  
  
  public SoggettoAssociatoVO getSoggettoAssociato()
  {
    return soggettoAssociato;
  }
  public void setSoggettoAssociato(SoggettoAssociatoVO soggettoAssociato)
  {
    this.soggettoAssociato = soggettoAssociato;
  }
  
  public ValidationErrors validateRicAziendeCollegate(HttpServletRequest request)
  {
    ValidationErrors errors = null;

    String cuaa = request.getParameter("cuaa");
    String partitaIva = request.getParameter("partitaIva");

    
    if (Validator.isNotEmpty(cuaa) && Validator.isNotEmpty(partitaIva))
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "cuaa", "Inserire solo CUAA o solo Partita IVA");
      errors = ErrorUtils.setValidErrNoNull(errors, "partitaIva", "Inserire solo CUAA o solo Partita IVA");
    }
    else
    {
    
      if(Validator.isNotEmpty(cuaa))
      {
        cuaa = cuaa.trim();
        cuaa = cuaa.toUpperCase();
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
      
      if(Validator.isNotEmpty(partitaIva))
      {
        partitaIva = partitaIva.trim();
        partitaIva = partitaIva.toUpperCase();
        
        if (partitaIva.length() == 11) 
        {
          if(!Validator.controlloPIVA(partitaIva)) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "partitaIva", (String)AnagErrors.get("ERR_PARTITA_IVA_ERRATA"));
          }
        }
        else //diverso da 11
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "partitaIva", (String)AnagErrors.get("ERR_PARTITA_IVA_ERRATA"));
        }
      }
    }
    return errors;
  }

  
  //E' simile al precedente ma non deve controllare che il cuaa e la partita iva siano
  //valorizzate in modo esclusivo
  public ValidationErrors validateRicAziendeCollegateForSoci(HttpServletRequest request)
  {
    ValidationErrors errors = null;

    String cuaa = request.getParameter("cuaa");
    String partitaIva = request.getParameter("partitaIva");
    String denominazione = request.getParameter("denominazione");
    if(Validator.isNotEmpty(cuaa))
    {
      cuaa = cuaa.trim();
      cuaa = cuaa.toUpperCase();
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
    
    if(Validator.isNotEmpty(partitaIva))
    {
      partitaIva = partitaIva.trim();
      partitaIva = partitaIva.toUpperCase();
      
      if (partitaIva.length() == 11) 
      {
        if(!Validator.controlloPIVA(partitaIva)) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "partitaIva", (String)AnagErrors.get("ERR_PARTITA_IVA_ERRATA"));
        }
      }
      else //diverso da 11
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "partitaIva", (String)AnagErrors.get("ERR_PARTITA_IVA_ERRATA"));
      }
    }
    
    
    if(Validator.isNotEmpty(denominazione))
    {
      denominazione = denominazione.trim();
      denominazione = denominazione.toUpperCase();
    }
    
    
    return errors;
  }
	
}
