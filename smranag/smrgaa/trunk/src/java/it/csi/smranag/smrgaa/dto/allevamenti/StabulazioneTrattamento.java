package it.csi.smranag.smrgaa.dto.allevamenti;

import it.csi.solmr.util.Validator;

import java.io.Serializable;
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

public class StabulazioneTrattamento implements Serializable
{
  /**
   * serial version UID
   */
  private static final long serialVersionUID = 8046652140217476232L;

  private long              idStabulazioneTrattamento;              // DB_STABULAZIONE_TRATTAMENTO.ID_STABULAZIONE_TRATTAMENTO
  private String            idSottoCategoriaAllevamento;            // DB_STABULAZIONE_TRATTAMENTO.ID_SOTTOCATEGORIA_ALLEVAMENTO
  private String            idStabulazione;                         // DB_STABULAZIONE_TRATTAMENTO.ID_STABULAZIONE
  private String            quantita;                               // DB_STABULAZIONE_TRATTAMENTO.QUANTITA_STABULATI
  private String            idTrattamento;                          // DB_STABULAZIONE_TRATTAMENTO.ID_TRATTAMENTO
  private String            descTrattamento;                        // DB_TIPO_TRATTAMENTO.DESCRIZIONE
 
  private String            descCategoria;                          // DB_TIPO_CATEGORIA_ANIMALE.DESCRIZIONE
  private String            descSottoCategoria;                     // DB_TIPO_SOTTOCATEGORIA_ANIMALE.DESCRIZIONE
  
  private String            descStabulazione;                       // DB_TIPO_STABULAZIONE.DESCRIZIONE
  private String            idSottoCategoriaAnimale;                // DB_SOTTOCATEGORIA_ALLEVAMENTO.ID_SOTTOCATEGORIA_ANIMALE

  private String            palabile;
  private String            palabileTAnno;
  private String            nonPalabile;
  private String            palabileTrat;
  private String            nonPalabileTrat;
  private String            totaleAzoto;
  private String            azotoPalabile;
  private String            azotoNonPalabile;
  private boolean           flagCalcolo      = true;                // DB_TIPO_TRATTAMENTO.FLAG_CALCOLO
  private String[][]        errori;                                 // utilizzato per gestire gli errori
  private Vector<EffluenteProdotto> vEffluenteProdotto;
  
  //Usate dai calcoli
  private SottoCategoriaAnimStab stabPal;
  private SottoCategoriaAnimStab stabNonPal;
  private SottoCategoriaAllevamento sottoCatAll;
  private TipoTrattamento tipoTrattamento;
  private double palabileMax=0.0;
  private double palabileMin=0.0;
  private double palabileTAnnoDb=0.0;
  private double nonPalabileMax=0.0;
  private double nonPalabileMin=0.0;
  private String permanenzaInStalla;
  private String escretoAlPascolo;
  private String lettieraPermanente;
 
  
  public double getPalabileMax()
  {
    return palabileMax;
  }

  public void setPalabileMax(double palabileMax)
  {
    this.palabileMax = palabileMax;
  }

  public double getPalabileMin()
  {
    return palabileMin;
  }

  public void setPalabileMin(double palabileMin)
  {
    this.palabileMin = palabileMin;
  }

  public double getNonPalabileMax()
  {
    return nonPalabileMax;
  }

  public void setNonPalabileMax(double nonPalabileMax)
  {
    this.nonPalabileMax = nonPalabileMax;
  }

  public double getNonPalabileMin()
  {
    return nonPalabileMin;
  }

  public void setNonPalabileMin(double nonPalabileMin)
  {
    this.nonPalabileMin = nonPalabileMin;
  }
  
  

  public double getPalabileTAnnoDb()
  {
    return palabileTAnnoDb;
  }

  public void setPalabileTAnnoDb(double palabileTAnnoDb)
  {
    this.palabileTAnnoDb = palabileTAnnoDb;
  }

  public SottoCategoriaAllevamento getSottoCatAll()
  {
    return sottoCatAll;
  }

  public void setSottoCatAll(SottoCategoriaAllevamento sottoCatAll)
  {
    this.sottoCatAll = sottoCatAll;
  }
  
  public SottoCategoriaAnimStab getStabPal()
  {
    return stabPal;
  }

  public void setStabPal(SottoCategoriaAnimStab stabPal)
  {
    this.stabPal = stabPal;
  }

  public SottoCategoriaAnimStab getStabNonPal()
  {
    return stabNonPal;
  }

  public void setStabNonPal(SottoCategoriaAnimStab stabNonPal)
  {
    this.stabNonPal = stabNonPal;
  }

  public boolean isFlagCalcolo()
  {
    return flagCalcolo;
  }

  public void setFlagCalcolo(boolean flagCalcolo)
  {
    this.flagCalcolo = flagCalcolo;
  }

  public String getPalabile()
  {
    return palabile;
  }

  public void setPalabile(String palabile)
  {
    this.palabile = palabile;
  }

  public String getNonPalabile()
  {
    return nonPalabile;
  }

  public void setNonPalabile(String nonPalabile)
  {
    this.nonPalabile = nonPalabile;
  }

  /*public String getPalabileTrat()
  {
    return palabileTrat;
  }

  public void setPalabileTrat(String palabileTrat)
  {
    this.palabileTrat = palabileTrat;
  }

  public String getNonPalabileTrat()
  {
    return nonPalabileTrat;
  }

  public void setNonPalabileTrat(String nonPalabileTrat)
  {
    this.nonPalabileTrat = nonPalabileTrat;
  }*/
  
  

  public String getPalabileTAnno()
  {
    return palabileTAnno;
  }

  public void setPalabileTAnno(String palabileTAnno)
  {
    this.palabileTAnno = palabileTAnno;
  }

  public String getDescStabulazione()
  {
    return descStabulazione;
  }

  public void setDescStabulazione(String descStabulazione)
  {
    this.descStabulazione = descStabulazione;
  }

  public String getIdStabulazione()
  {
    return idStabulazione;
  }

  public void setIdStabulazione(String idStabulazione)
  {
    this.idStabulazione = idStabulazione;
  }

  public long getIdStabulazioneTrattamento()
  {
    return idStabulazioneTrattamento;
  }

  public void setIdStabulazioneTrattamento(long idStabulazioneTrattamento)
  {
    this.idStabulazioneTrattamento = idStabulazioneTrattamento;
  }

  public String getIdSottoCategoriaAllevamento()
  {
    return idSottoCategoriaAllevamento;
  }

  public void setIdSottoCategoriaAllevamento(String idSottoCategoriaAllevamento)
  {
    this.idSottoCategoriaAllevamento = idSottoCategoriaAllevamento;
  }

  public String getQuantita()
  {
    return quantita;
  }

  public void setQuantita(String quantita)
  {
    this.quantita = quantita;
  }

  public String getIdTrattamento()
  {
    return idTrattamento;
  }

  public void setIdTrattamento(String idTrattamento)
  {
    this.idTrattamento = idTrattamento;
  }

  public String getIdSottoCategoriaAnimale()
  {
    return idSottoCategoriaAnimale;
  }

  public void setIdSottoCategoriaAnimale(String idSottoCategoriaAnimale)
  {
    this.idSottoCategoriaAnimale = idSottoCategoriaAnimale;
  }
  
  public String[][] getErrori()
  {
    return errori;
  }

  public void setErrori(String[][] errori)
  {
    this.errori = errori;
  }
  
  public TipoTrattamento getTipoTrattamento()
  {
    return tipoTrattamento;
  }

  public void setTipoTrattamento(TipoTrattamento tipoTrattamento)
  {
    this.tipoTrattamento = tipoTrattamento;
  }
  
  public String getDescTrattamento()
  {
    return descTrattamento;
  }

  public void setDescTrattamento(String descTrattamento)
  {
    this.descTrattamento = descTrattamento;
  }
  
  public String getDescCategoria()
  {
    return descCategoria;
  }

  public void setDescCategoria(String descCategoria)
  {
    this.descCategoria = descCategoria;
  }

  public String getDescSottoCategoria()
  {
    return descSottoCategoria;
  }

  public void setDescSottoCategoria(String descSottoCategoria)
  {
    this.descSottoCategoria = descSottoCategoria;
  }
  
  /*public String getTotaleAzoto()
  {
    return totaleAzoto;
  }

  public void setTotaleAzoto(String totaleAzoto)
  {
    this.totaleAzoto = totaleAzoto;
  }*/
  
  
  
  public boolean validateInsert()
  {
    Vector<String[]> errors = new Vector<String[]>();
    
    if (!Validator.isNotEmpty(idSottoCategoriaAnimale))
    {
      String errore[] = new String[2];
      errore[0] = "err_idCatSottocatStab";
      errore[1] = "Selezionare la Categoria - Sottocategoria";
      errors.add(errore);
    }
    if (!Validator.isNotEmpty(idStabulazione))
    {
      String errore[] = new String[2];
      errore[0] = "err_idStabulazione";
      errore[1] = "Selezionare la Stabulazione";
      errors.add(errore);
    }
    try
    {
      String errore[] = new String[2];
      errore[0] = "err_quantitaStab";
      errore[1] = "La quantità indicata deve essere un numero intero compreso tra 1 e 9999999";
      long quantitaL = Long.parseLong(quantita);
      if (quantitaL <= 0 || quantitaL > 9999999)
        errors.add(errore);
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      errore[0] = "err_quantitaStab";
      errore[1] = "La quantità indicata deve essere un numero intero compreso tra 1 e 9999999";
      errors.add(errore);
    }

    errori = errors.size() == 0 ? null : (String[][]) errors.toArray(new String[0][]);

    if (errori != null)
      return true;
    else
      return false;
  }
  
  
  
  public String getAzotoPalabile()
  {
    return azotoPalabile;
  }

  public void setAzotoPalabile(String azotoPalabile)
  {
    this.azotoPalabile = azotoPalabile;
  }

  public String getAzotoNonPalabile()
  {
    return azotoNonPalabile;
  }

  public void setAzotoNonPalabile(String azotoNonPalabile)
  {
    this.azotoNonPalabile = azotoNonPalabile;
  }

  public boolean validateVolumiInsert()
  {
    Vector<String[]> errors = new Vector<String[]>();
    
    try
    {
      String errore[] = new String[2];
      errore[0] = "err_palabile";
      errore[1] = "L'effluente palabile deve essere compreso tra " + palabileMin + " e " + palabileMax;
      double palabileD = Double.parseDouble(palabile.replace(',', '.'));
      if (palabileD < palabileMin || palabileD > palabileMax)
        errors.add(errore);
      else
      {
        String temp = checkDecimals(palabileD, 1);
        if (temp != null)
        {
          errore[1] = temp;
          errors.add(errore);
        }
      }
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      errore[0] = "err_palabile";
      errore[1] = "L'effluente palabile deve essere compreso tra " + palabileMin + " e " + palabileMax;
      errors.add(errore);
    }
    
    try
    {
      String errore[] = new String[2];
      errore[0] = "err_nonPalabile";
      errore[1] = "L'effluente non palabile deve essere compreso tra " + nonPalabileMin + " e " + nonPalabileMax;
      double nonPalabileD = Double.parseDouble(nonPalabile.replace(',', '.'));
      if (nonPalabileD < nonPalabileMin || nonPalabileD > nonPalabileMax)
        errors.add(errore);
      else
      {
        String temp = checkDecimals(nonPalabileD, 1);
        if (temp != null)
        {
          errore[1] = temp;
          errors.add(errore);
        }
      }
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      errore[0] = "err_nonPalabile";
      errore[1] = "L'effluente non palabile deve essere compreso tra " + nonPalabileMin + " e " + nonPalabileMax;
      errors.add(errore);
    }
    
    /*try
    {
      String errore[] = new String[2];
      errore[0] = "err_palabileTrat";
      errore[1] = "L'effluente palabile deve essere compreso tra 0 e 999999,9";
      double palabileD = Double.parseDouble(palabileTrat.replace(',', '.'));
      if (palabileD < 0 || palabileD > 999999.9)
        errors.add(errore);
      else
      {
        String temp = checkDecimals(palabileD, 1);
        if (temp != null)
        {
          errore[1] = temp;
          errors.add(errore);
        }
      }
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      errore[0] = "err_palabileTrat";
      errore[1] = "L'effluente palabile deve essere compreso tra 0 e 999999,9";
      errors.add(errore);
    }*/
    
    
    /*try
    {
      String errore[] = new String[2];
      errore[0] = "err_nonPalabileTrat";
      errore[1] = "L'effluente non palabile deve essere compreso tra 0 e 999999,9";
      double palabileD = Double.parseDouble(nonPalabileTrat.replace(',', '.'));
      if (palabileD < 0 || palabileD > 999999.9)
        errors.add(errore);
      else
      {
        String temp = checkDecimals(palabileD, 1);
        if (temp != null)
        {
          errore[1] = temp;
          errors.add(errore);
        }
      }
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      errore[0] = "err_nonPalabileTrat";
      errore[1] = "L'effluente non palabile deve essere compreso tra 0 e 999999,9";
      errors.add(errore);
    }*/
    
    /*if (!isFlagCalcolo())
    {
      //se db_tipo_trattamento.flag_calcolo='N' 
      //Controlli: deve essere >=0      
      try
      {
        String errore[] = new String[2];
        errore[0] = "err_totaleAzoto";
        errore[1] = "Il Totale Azoto deve essere compreso tra 0 e 999999,9";
        double totaleAzotoD = Double.parseDouble(totaleAzoto.replace(',', '.'));
        if (totaleAzotoD < 0 || totaleAzotoD > 999999.9)
          errors.add(errore);
        else
        {
          String temp = checkDecimals(totaleAzotoD, 1);
          if (temp != null)
          {
            errore[1] = temp;
            errors.add(errore);
          }
        }
      }
      catch (Exception e)
      {
        String errore[] = new String[2];
        errore[0] = "err_totaleAzoto";
        errore[1] = "Il Totale Azoto deve essere compreso tra 0 e 999999,9";
        errors.add(errore);
      }
    }*/
    

    errori = errors.size() == 0 ? null : (String[][]) errors.toArray(new String[0][]);

    if (errori != null)
      return true;
    else
      return false;
  }
  
  
  //Questo metodo esegue i calcoli ed imposta i risultati nelle variabili
  //che saranno poi essere usate per validare i dati
  public void effettuaCalcoli()
  {
    //calcolo prima il non palabile perchè alcuni valori del palabile dipendono 
    //dal non palabile
    calcolaNonPalabile();
    calcolaPalabile();
    
  }
  
  /*public void effettuaCalcoliEfflPostTratt()
  {
    try
    {
      if (isFlagCalcolo() && tipoTrattamento!=null)
      {
        double tempNonPal = Double.parseDouble(nonPalabile.replace(',', '.'));
        double tempPal = Double.parseDouble(palabile.replace(',', '.'));
        //applico il trattamento
        nonPalabileTratDefault=round(tempNonPal * (1-(tipoTrattamento.getPercVolumeSolido()/100)));
        palabileTratDefault=round(tempPal+(tempNonPal-nonPalabileTratDefault));
        palabileTrat=""+palabileTratDefault;
        nonPalabileTrat=""+nonPalabileTratDefault;
      }
      if (!isFlagCalcolo())
      {
        //se db_tipo_trattamento.flag_calcolo='N' il valore viene digitato dall'utente      
        //Controlli: deve essere >=0      
        //Default: visualizzare la somma dell'Azoto palabile prodotto pre trattamento Aziendale 
        //e dell'Azoto non palabile prodotto pre trattamento Aziendale
        totaleAzoto="" + round(azotoNonPalabilePreTrattAz()+ azotoPalabilePreTrattAz());
      }
    }
    catch(Exception e) {}
  }*/
  
  public void effettuaCalcoliEfflPreTratt()
  {
    try
    {
      //se db_tipo_trattamento.flag_calcolo='N' il valore viene digitato dall'utente      
      //Controlli: deve essere >=0      
      //Default: visualizzare la somma dell'Azoto palabile prodotto pre trattamento Aziendale 
      //e dell'Azoto non palabile prodotto pre trattamento Aziendale
      azotoNonPalabile="" + round(azotoNonPalabilePreTratt());
      azotoPalabile=""+ round(azotoPalabilePreTratt());
    }
    catch(Exception e) {}
  }
  
  //Questo metodo imposta i valori di min calcolati nel volume dei palabili/non palibili
  //pre e post trattamento
  public void impostaValoriDefault()
  {
    
    palabile=""+round(palabileMin);
    nonPalabile=""+round(nonPalabileMin);
    palabileTAnno=""+round(palabileTAnnoDb);
    /*if (tipoTrattamento==null)
    {
      palabileTrat=palabile;
      nonPalabileTrat=nonPalabile;
    }
    else 
      if(isFlagCalcolo())
      {
        //Se l'utente non ha selezionato altri trattamenti non devo lasciare il
        //valore inserito dall'utente
        palabileTrat=""+round(palabileTratDefault);
        nonPalabileTrat=""+round(nonPalabileTratDefault);
      }*/
  }
  
 
  //Volume palabile (m3/anno)= (peso vivo unitario /1000) * n.capi stabulati 
  //* coefficiente volume palabile * (gg permanenza stalla / 365)
  private void calcolaPalabile()
  {
    //Se esiste un solo effluente palabile legato alla sottocategoria/trattamenti:
    //Calcolare il volume min, MAX legato ai coefficienti db_sottocategoria_anim_stab.COEFF_VOLUME _MIN,
    //db_sottocategoria_anim_stab .COEFF_VOLUME _MAX.Vedi: 
    //Formule per il calcolo effluenti (Elenco formule Rif.C)q Se esistono più effluenti palabili 
    //legati alla sottocategoria/trattamenti (anomalia su base dati) 
    //oppure se non ne esistono:Il volume calcolato min, MAX = 0 
    if (stabPal==null)
    {
      //se è null min e MAX li lascio a 0
      palabileMax=0.0;
      palabileMin=0.0;
      palabileTAnnoDb=0.0;
    }
    else
    {
      double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
      double nCapiStabulati=Double.parseDouble(quantita);
      double temp = (pesoVivoUnitario / 1000) * nCapiStabulati * (sottoCatAll.ggPermanenzaStalla() / 365);
      palabileMax=round(temp*stabPal.getCoeffVolumeMax());
      palabileMin=round(temp*stabPal.getCoeffVolumeMin());
      palabileTAnnoDb =round(temp * stabPal.getCoeffVolumeMin() * stabPal.getCoeffM3TPua() / 10); 
    }
  }
  
  //Volume non palabile (m3/anno): (peso vivo unitario /1000) * n.capi stabulati * 
  //coefficiente volume non palabile * (gg permanenza stalla / 365)
  private void calcolaNonPalabile()
  {
    //Se esiste un solo effluente nonpalabile legato alla sottocategoria/trattamenti:
    //Calcolare il volume min, MAX legato ai coefficienti db_sottocategoria_anim_stab.COEFF_VOLUME _MIN,
    //db_sottocategoria_anim_stab .COEFF_VOLUME _MAX.Vedi: 
    //Formule per il calcolo effluenti (Elenco formule Rif.E) Se esistono più effluenti non palabili 
    //legati alla sottocategoria/trattamenti (anomalia su base dati) 
    //oppure se non ne esistono:Il volume calcolato min, MAX = 0 
    if (stabNonPal==null)
    {
      //se è null min e MAX li lascio a 0
      nonPalabileMax=0.0;
      nonPalabileMin=0.0;
    }
    else
    {
      double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
      double nCapiStabulati=Double.parseDouble(quantita);
      double temp = (pesoVivoUnitario / 1000) * nCapiStabulati * (sottoCatAll.ggPermanenzaStalla() / 365);
      nonPalabileMax=round(temp*stabNonPal.getCoeffVolumeMax());
      nonPalabileMin=round(temp*stabNonPal.getCoeffVolumeMin());
    }
  }
  
  
  
  //Riferimento C
  public double volumePalabileRifC()
  {
    double result=0;
    double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
    double nCapiStabulati=Double.parseDouble(quantita);
    result = (pesoVivoUnitario / 1000) * nCapiStabulati * stabPal.getCoeffVolumeMin() * (sottoCatAll.ggPermanenzaStalla() / 365);
    return round(result);  
  }
  
  
  //Riferimento D
  public double volumePalabileAziendale()
  {
    double result=0;
    //  Volume palabile aziendale (m3/anno) = 
    //  (peso vivo unitario /1000) * n.capi stabulati * coefficiente volume palabile * (gg permanenza stalla aziendale / 365)
    
    double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
    double nCapiStabulati=Double.parseDouble(quantita);
    result = (pesoVivoUnitario / 1000) * nCapiStabulati * stabPal.getCoeffVolumeMin()* (sottoCatAll.ggPermanenzaStallaAziendale() / 365);
    return round(result);  
  }
  
  
  //Riferimento E
  public double volumeNonPalabileRifE()
  {
    double result=0;
    double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
    double nCapiStabulati=Double.parseDouble(quantita);
    result = (pesoVivoUnitario / 1000) * nCapiStabulati * stabNonPal.getCoeffVolumeMin() * (sottoCatAll.ggPermanenzaStalla() / 365);
    return round(result);  
  }
  
  
  
  //Riferimento F
  public double volumeNonPalabileAziendale()
  {
    double result=0;
    //  Volume non palabile aziendale (m3/anno) = 
    //  peso vivo unitario /1000 * n.capi stabulati * coefficiente volume non palabile * gg permanenza stalla aziendale / 365
    
    double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
    double nCapiStabulati=Double.parseDouble(quantita);
    result = (pesoVivoUnitario / 1000) * nCapiStabulati * stabNonPal.getCoeffVolumeMin()* (sottoCatAll.ggPermanenzaStallaAziendale() / 365);
    return round(result);  
  }

  
  //Riferimento G
  public double azotoPalabilePreTratt()
  {
    double result=0;
    //  Azoto palabile prodotto pre trattamento =  
    //  peso vivo unitario / 1000 * n. capi stabulati * coefficiente azoto palabile * gg permanenza stalla / 365
    
    double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
    double nCapiStabulati=Double.parseDouble(quantita);
    result = (pesoVivoUnitario / 1000) * nCapiStabulati * stabPal.getCoeffAzotoMin() * (sottoCatAll.ggPermanenzaStalla() / 365);
    return round(result);  
  }
  
  //Riferimento H
  public double azotoPalabilePreTrattAz()
  {
    double result=0;
    //  Azoto palabile prodotto pre trattamento aziendale =  
    //  peso vivo unitario / 1000 * n. capi stabulati * coefficiente azoto palabile * gg permanenza stalla aziendale / 365
    
    double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
    double nCapiStabulati=Double.parseDouble(quantita);
    result = (pesoVivoUnitario / 1000) * nCapiStabulati * stabPal.getCoeffAzotoMin() * (sottoCatAll.ggPermanenzaStallaAziendale() / 365);
    return round(result);  
  }
  
  
  
  
  //Riferimento I
  public double azotoNonPalabilePreTratt()
  {
    double result=0;
    //  Azoto non palabile prodotto pre trattamento =  
    //  peso vivo unitario / 1000 * n. capi stabulati * coefficiente azoto non palabile * gg permanenza stalla / 365
    double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
    double nCapiStabulati=Double.parseDouble(quantita);
    result = (pesoVivoUnitario / 1000) * nCapiStabulati * stabNonPal.getCoeffAzotoMin() * (sottoCatAll.ggPermanenzaStalla() / 365);
    return round(result);  
  }
  
  //Riferimento L
  public double azotoNonPalabilePreTrattAz()
  {
    double result=0;
    //  Azoto non palabile prodotto pre trattamento aziendale =
    //  peso vivo unitario / 1000 * n. capi stabulati * coefficiente azoto non palabile * gg permanenza stalla aziendale / 365
    
    double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
    double nCapiStabulati=Double.parseDouble(quantita);
    result = (pesoVivoUnitario / 1000) * nCapiStabulati * stabNonPal.getCoeffAzotoMin() * (sottoCatAll.ggPermanenzaStallaAziendale() / 365);
    return round(result);  
  }
  /*
  //Riferimento M
  public double volumeNonPalabilePostTrattRifM()
  {
    double result=0;
    // Volume non palabile post trattamento (m3/anno) = 
    // volume non palabile pre trattamento* (1-(coefficiente volume trattamento /100))  
    
    volume non palabile pre trattamento=Dato corrispondente al valore calcolato (Rif. E)  o modificato dall’utente coefficiente volume trattamento= considerare il valore corrispondente al relativo trattamento (db_tipo_trattamento .perc_volume_solido)
    
    
    double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
    double nCapiStabulati=Double.parseDouble(quantita);
    result = (pesoVivoUnitario / 1000) * nCapiStabulati * stabNonPal.getCoeffAzotoMin() * (sottoCatAll.ggPermanenzaStallaAziendale() / 365);
    return round(result);  
  }
  */
  
  
  
  
  //Riferimento O
  public double azotoProdNonPalabilePostTratt()
  {
    double result=0;
    //  Azoto prodotto post trattamento non palabile = 
    //  (1- coefficiente di azoto volatile) * azoto non palabile pre trattamento * (1-(coefficiente azoto trattamento /100))
   
    try
    {
      if (isFlagCalcolo() && tipoTrattamento!=null)
        result=(1- tipoTrattamento.getPercAzotoVolatile()/100) * azotoNonPalabilePreTratt() * (1-(tipoTrattamento.getPercAzotoSolido() /100));  
      else
        result=azotoNonPalabilePreTratt();
    }
    catch(Exception e){}
    return round(result);  
  }
  
  //Riferimento P
  public double azotoProdPalabilePostTratt()
  {
    //  Azoto prodotto post trattamento palabile = 
    //  Azoto prodotto pre trattamento non palabile – azoto prodotto post trattamento non palabile
    return round(azotoNonPalabilePreTratt()-azotoProdNonPalabilePostTratt());
  }
  
  //Riferimento Q
  public double volNonPalPostTrattAziend()
  {
    double result=0;
    //  Volume non palabile post trattamento (m3/anno) aziendale = 
    //  volume non palabile aziendale* (1-(coefficiente volume trattamento /100))
    try
    {
      result=volumeNonPalabileAziendale() * (1- (tipoTrattamento.getPercVolumeSolido() /100));    
    }
    catch(Exception e){}
    return round(result);  
    
  }
  
  //Riferimento R
  public double volPalPostTrattAziend()
  {
    //  Volume palabile post trattamento (m3/anno) aziendale = 
    //  Volume non palabile pre trattamento aziendale – volume non palabile post trattamento aziendale
    return round(volumeNonPalabileAziendale()-volNonPalPostTrattAziend());
    
  }
  
  //Riferimento S
  public double azotoProdPostTrattNonPalAz()
  {
    double result=0;
    //  Azoto prodotto post trattamento non palabile aziendale = 
    //  (1- coefficiente di azoto volatile) * azoto non palabile pre trattamento aziendale * (1-(coefficiente azoto trattamento /100))
    try
    {
      if (isFlagCalcolo() && tipoTrattamento!=null)
        result=(1- tipoTrattamento.getPercAzotoVolatile()/100) * azotoNonPalabilePreTrattAz() * (1- (tipoTrattamento.getPercAzotoSolido() /100));   
      else
        result=azotoNonPalabilePreTrattAz();
    }
    catch(Exception e){}
    return round(result);  
  }
  
  //Riferimento T
  public double azotoProdPostTrattPalAz()
  {
    //  Azoto prodotto post trattamento palabile aziendale = 
    //  Azoto prodotto pre trattamento non palabile aziendale – azoto prodotto post trattamento non palabile aziendale
    return round(azotoNonPalabilePreTrattAz()-azotoProdPostTrattNonPalAz());
  }
  
  
  //Riferimento X
  public double escretoAlPascolo()
  {
    double result=0;
    //  Azoto palabile prodotto pre trattamento aziendale =  
    //  peso vivo unitario / 1000 * n. capi stabulati * pesoVivoAzoto * (1-(gg permanenza stalla aziendale / 365)
    
    double pesoVivoUnitario=Double.parseDouble(sottoCatAll.getPesoVivo().replace(',','.'));
    double nCapiStabulati=Double.parseDouble(quantita);
    result = (pesoVivoUnitario / 1000) * nCapiStabulati * sottoCatAll.getPesoVivoAzoto() * (1-(sottoCatAll.ggPermanenzaStalla() / 365));
    return round(result);  
  }
  
  
  
  private static String checkDecimals(double value, int numDecimals)
  {
    if (numDecimals > 0)
    {
      double poweredValue = value * Math.pow(10, numDecimals);
      poweredValue = Math.round(poweredValue);
      poweredValue = poweredValue / Math.pow(10, numDecimals);
      if (poweredValue != value)
        return "Attenzione, troppe cifre decimali (massimo " + numDecimals + ")";
    }
    return null;
  }
  
  private double round(double d)
  {
    d=d*10;
    d=Math.round(d);
    d=d/10;
    return d;
  }

  public Vector<EffluenteProdotto> getVEffluenteProdotto()
  {
    return vEffluenteProdotto;
  }

  public void setVEffluenteProdotto(Vector<EffluenteProdotto> effluenteProdotto)
  {
    vEffluenteProdotto = effluenteProdotto;
  }

  public String getPermanenzaInStalla()
  {
    return permanenzaInStalla;
  }

  public void setPermanenzaInStalla(String permanenzaInStalla)
  {
    this.permanenzaInStalla = permanenzaInStalla;
  }

  public String getEscretoAlPascolo()
  {
    return escretoAlPascolo;
  }

  public void setEscretoAlPascolo(String escretoAlPascolo)
  {
    this.escretoAlPascolo = escretoAlPascolo;
  }

  public String getLettieraPermanente()
  {
    return lettieraPermanente;
  }

  public void setLettieraPermanente(String lettieraPermanente)
  {
    this.lettieraPermanente = lettieraPermanente;
  }

  public String getPalabileTrat()
  {
    return palabileTrat;
  }

  public void setPalabileTrat(String palabileTrat)
  {
    this.palabileTrat = palabileTrat;
  }

  public String getNonPalabileTrat()
  {
    return nonPalabileTrat;
  }

  public void setNonPalabileTrat(String nonPalabileTrat)
  {
    this.nonPalabileTrat = nonPalabileTrat;
  }

  public String getTotaleAzoto()
  {
    return totaleAzoto;
  }

  public void setTotaleAzoto(String totaleAzoto)
  {
    this.totaleAzoto = totaleAzoto;
  }
  
  

}
