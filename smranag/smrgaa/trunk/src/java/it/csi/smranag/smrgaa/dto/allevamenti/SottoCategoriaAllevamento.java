package it.csi.smranag.smrgaa.dto.allevamenti;

import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
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

public class SottoCategoriaAllevamento implements Serializable
{
  /**
   * serial version UID
   */
  private static final long serialVersionUID = 1505700989685697219L;

  // Campi della tabella DB_SOTTOCATEGORIA_ALLEVAMENTO
  private long              idSottoCategoriaAllevamento;            // DB_SOTTOCATEGORIA_ALLEVAMENTO.ID_SOTTOCATEGORIA_ALLEVAMENTO
  private long              idSottoCategoriaAnimale;                // DB_SOTTOCATEGORIA_ALLEVAMENTO.ID_SOTTOCATEGORIA_ANIMALE
  private String            orePascoloInverno="0";                  // DB_SOTTOCATEGORIA_ALLEVAMENTO.ORE_PASCOLO_INVERNO
  private long              idCategorieAllevamento;                 // DB_SOTTOCATEGORIA_ALLEVAMENTO.ID_CATEGORIE_ALLEVAMENTO
  private String            quantita;                               // DB_SOTTOCATEGORIA_ALLEVAMENTO.QUANTITA
  private String            quantitaProprieta;                      // DB_SOTTOCATEGORIA_ALLEVAMENTO.QUANTITA_PROPRIETA
  private String            pesoVivo;                               // DB_SOTTOCATEGORIA_ALLEVAMENTO.PESO_VIVO
  private String            cicli;                                  // DB_SOTTOCATEGORIA_ALLEVAMENTO.CICLI
  private String            giorniVuotoSanitario;                   // DB_SOTTOCATEGORIA_ALLEVAMENTO.GIORNI_VUOTO_SANITARIO
  private String            numeroCicliAnnuali="1";                     // DB_SOTTOCATEGORIA_ALLEVAMENTO.NUMERI_CICLI_ANNUALI
  private String            giorniPascoloEstate="0";                // DB_SOTTOCATEGORIA_ALLEVAMENTO.GIORNI_PASCOLO_ESTATE
  private String            orePascoloEstate="0";                   // DB_SOTTOCATEGORIA_ALLEVAMENTO.ORE_PASCOLO_ESTATE
  private String            giorniPascoloInverno="0";               // DB_SOTTOCATEGORIA_ALLEVAMENTO.GIORNI_PASCOLO_INVERNO
  private Vector<StabulazioneTrattamento>            vStabulazioneTrattamentoVO;             // Vettore di StabulazioneTrattamento

  // campi utilizzati per far vedere le descrizioni
  private String            descSottoCategoriaAnimale;              // DB_TIPO_SOTTOCATEGORIA_ANIMALE.DESCRIZIONE
  private String            descCategoriaAnimale;                   // DB_TIPO_CATEGORIA_ANIMALE.DESCRIZIONE
  private long              idCategoriaAnimale;                     // DB_SOTTOCATEGORIA_ALLEVAMENTO.ID_CATEGORIA_ANIMALE

  // Campi utilizzati per fare i controlli
  private double            pesoVivoMin;                            // DB_TIPO_SOTTOCATEGORIA_ANIMALE.PESO_VIVO_MIN
  private double            pesoVivoMax;                            // DB_TIPO_SOTTOCATEGORIA_ANIMALE.PESO_VIVO_MAX
  private double            pesoVivoAzoto;                            // DB_TIPO_SOTTOCATEGORIA_ANIMALE.PESO_VIVO_AZOTO
  private boolean           flagStallaPascolo;                        // DB_TIPO_SPECIE_ANIMALE.FLAG_STALLA_PASCOLO
  private int               ggVuotoSanitario;                       // DB_TIPO_SOTTOCATEGORIA_ANIMALE.GIORNI_VUOTO_SANITARIO
  private int               ggDurataCiclo;                          // DB_TIPO_SOTTOCATEGORIA_ANIMALE.GIORNI_DURATA_CICLO
  private double            coefficienteUba;                        // DB_TIPO_CATEGORIA_ANIMALE.COEFFICIENTE_UBA
  private boolean           flagSottocatFittizia;                   // DB_TIPO_SOTTOCATEGORIA_ANIMALE.FLAG_SOTTOCAT_FITTIZIA
  private String            lattazione;                             // DB_TIPO_CATEGORIA_ANIMALE
  
  private String[][]        errori;                                 // utilizzato
                                                                    // per
                                                                    // gestire
                                                                    // gli
                                                                    // errori

  public ValidationErrors validateInsConsistenzaZootec(ValidationErrors errors, String idSpecie, String idCategoria, String idSottoCategoria)
  {
    /*
     * verifico che l'utente abbia selezionato la specie, la categoria e la
     * sottocategoria
     */
    if (!Validator.isNotEmpty(idSpecie))
      errors.add("idSpecie", new ValidationError(AnagErrors.ERR_SPECIE_OBB));
    if (!Validator.isNotEmpty(idCategoria))
      errors.add("idCategoria", new ValidationError(AnagErrors.ERR_CATEGORIA_OBB));
    if (!Validator.isNotEmpty(idSottoCategoria))
      errors.add("idSottoCategoria", new ValidationError(AnagErrors.ERR_SOTTO_CATEGORIA_OBB));
    return errors;
  }

  public long getIdSottoCategoriaAllevamento()
  {
    return idSottoCategoriaAllevamento;
  }

  public void setIdSottoCategoriaAllevamento(long idSottoCategoriaAllevamento)
  {
    this.idSottoCategoriaAllevamento = idSottoCategoriaAllevamento;
  }

  public long getIdSottoCategoriaAnimale()
  {
    return idSottoCategoriaAnimale;
  }

  public void setIdSottoCategoriaAnimale(long idSottoCategoriaAnimale)
  {
    this.idSottoCategoriaAnimale = idSottoCategoriaAnimale;
  }

  public String getOrePascoloInverno()
  {
    return orePascoloInverno;
  }

  public void setOrePascoloInverno(String orePascoloInverno)
  {
    this.orePascoloInverno = orePascoloInverno;
  }

  public long getIdCategorieAllevamento()
  {
    return idCategorieAllevamento;
  }

  public void setIdCategorieAllevamento(long idCategorieAllevamento)
  {
    this.idCategorieAllevamento = idCategorieAllevamento;
  }

  public String getQuantita()
  {
    return quantita;
  }

  public void setQuantita(String quantita)
  {
    this.quantita = quantita;
  }

  public String getPesoVivo()
  {
    return pesoVivo;
  }

  public void setPesoVivo(String pesoVivo)
  {
    this.pesoVivo = pesoVivo;
  }

  public String getCicli()
  {
    return cicli;
  }

  public void setCicli(String cicli)
  {
    this.cicli = cicli;
  }

  public String getGiorniPascoloEstate()
  {
    return giorniPascoloEstate;
  }

  public void setGiorniPascoloEstate(String giorniPascoloEstate)
  {
    this.giorniPascoloEstate = giorniPascoloEstate;
  }

  public String getOrePascoloEstate()
  {
    return orePascoloEstate;
  }

  public void setOrePascoloEstate(String orePascoloEstate)
  {
    this.orePascoloEstate = orePascoloEstate;
  }

  public String getGiorniPascoloInverno()
  {
    return giorniPascoloInverno;
  }

  public void setGiorniPascoloInverno(String giorniPascoloInverno)
  {
    this.giorniPascoloInverno = giorniPascoloInverno;
  }

  public String getDescSottoCategoriaAnimale()
  {
    return descSottoCategoriaAnimale;
  }

  public void setDescSottoCategoriaAnimale(String descSottoCategoriaAnimale)
  {
    this.descSottoCategoriaAnimale = descSottoCategoriaAnimale;
  }

  public String getDescCategoriaAnimale()
  {
    return descCategoriaAnimale;
  }

  public void setDescCategoriaAnimale(String descCategoriaAnimale)
  {
    this.descCategoriaAnimale = descCategoriaAnimale;
  }

  public double getPesoVivoMin()
  {
    return pesoVivoMin;
  }

  public void setPesoVivoMin(double pesoVivoMin)
  {
    this.pesoVivoMin = pesoVivoMin;
  }

  public double getPesoVivoMax()
  {
    return pesoVivoMax;
  }

  public void setPesoVivoMax(double pesoVivoMax)
  {
    this.pesoVivoMax = pesoVivoMax;
  }

  public String[][] getErrori()
  {
    return errori;
  }

  public void setErrori(String[][] errori)
  {
    this.errori = errori;
  }

  public boolean isFlagStallaPascolo()
  {
    return flagStallaPascolo;
  }

  public void setFlagStallaPascolo(boolean flagStallaPascolo)
  {
    this.flagStallaPascolo = flagStallaPascolo;
  }

  /*public int getGgVuotoSanitario()
  {
    return ggVuotoSanitario;
  }

  public void setGgVuotoSanitario(int ggVuotoSanitario)
  {
    this.ggVuotoSanitario = ggVuotoSanitario;
  }*/

  public int getGgDurataCiclo()
  {
    return ggDurataCiclo;
  }

  public void setGgDurataCiclo(int ggDurataCiclo)
  {
    this.ggDurataCiclo = ggDurataCiclo;
  }

  public long getIdCategoriaAnimale()
  {
    return idCategoriaAnimale;
  }

  public void setIdCategoriaAnimale(long idCategoriaAnimale)
  {
    this.idCategoriaAnimale = idCategoriaAnimale;
  }

  public double getCoefficienteUba()
  {
    return coefficienteUba;
  }

  public void setCoefficienteUba(double coefficienteUba)
  {
    this.coefficienteUba = coefficienteUba;
  }

  public boolean isFlagSottocatFittizia()
  {
    return flagSottocatFittizia;
  }

  public void setFlagSottocatFittizia(boolean flagSottocatFittizia)
  {
    this.flagSottocatFittizia = flagSottocatFittizia;
  }

  public boolean validateInsert(boolean ugualiPropDet, boolean proprietario)
  {
    Vector<String[]> errors = new Vector<String[]>();

    boolean flagQuantita = false;
    try
    {
      String errore[] = new String[2];
      errore[0] = "err_capiTab";
      errore[1] = "La quantità indicata deve essere un numero intero compreso tra 0 e 9999999";
      long quantitaL = Long.parseLong(quantita);
      if (quantitaL < 0 || quantitaL > 9999999)
        errors.add(errore);
      else
        flagQuantita = true;
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      errore[0] = "err_capiTab";
      errore[1] = "La quantità indicata deve essere un numero intero compreso tra 0 e 9999999";
      errors.add(errore);
    }
    
    boolean flagQuantitaProprieta = false;
    try
    {
      String errore[] = new String[2];
      errore[0] = "err_capiProprietaTab";
      errore[1] = "La quantità indicata deve essere un numero intero compreso tra 0 e 9999999";
      long quantitaL = Long.parseLong(quantitaProprieta);
      if (quantitaL < 0 || quantitaL > 9999999)
        errors.add(errore);
      else
        flagQuantitaProprieta = true;
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      errore[0] = "err_capiProprietaTab";
      errore[1] = "La quantità indicata deve essere un numero intero compreso tra 0 e 9999999";
      errors.add(errore);
    }
    
    if(flagQuantita && flagQuantitaProprieta)
    {
      long quantitaL = Long.parseLong(quantita);
      long quantitaProprietaL = Long.parseLong(quantitaProprieta);
     
      if(ugualiPropDet)
      {        
        if(quantitaL != quantitaProprietaL)
        {
          String errore[] = new String[2];
          errore[0] = "err_capiTab";
          errore[1] = "La quantità indicata del proprietario deve essere la stessa del detentore";
          errors.add(errore);
          String errore2[] = new String[2];
          errore2[0] = "err_capiProprietaTab";
          errore2[1] = "La quantità indicata del proprietario deve essere la stessa del detentore";
          errors.add(errore2);
        }
      }
      else
      {
        if(proprietario)
        {
          if(quantitaL > 0)
          {
            String errore[] = new String[2];
            errore[0] = "err_capiTab";
            errore[1] = "La quantità indicata deve essere 0";
            errors.add(errore);
          }               
        }
        else
        {
          if(quantitaProprietaL > 0)
          {
            String errore[] = new String[2];
            errore[0] = "err_capiProprietaTab";
            errore[1] = "La quantità indicata deve essere 0";
            errors.add(errore);
          }
        }
      }
    }

    try
    {
      String errore[] = new String[2];
      errore[0] = "err_pesoVivoTab";
      errore[1] = "Il peso vivo medio unitario deve essere compreso tra " + pesoVivoMin + " e " + pesoVivoMax;
      if (Validator.isNotEmpty(pesoVivo))
      {
        double pesoVivoD = Double.parseDouble(pesoVivo.replace(',', '.'));
        if (pesoVivoD < pesoVivoMin || pesoVivoD > pesoVivoMax)
          errors.add(errore);
        else
        {
          String temp = checkDecimals(pesoVivoD, 1);
          if (temp != null)
          {
            errore[1] = temp;
            errors.add(errore);
          }
        }
      }
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      errore[0] = "err_pesoVivoTab";
      errore[1] = "Il peso vivo medio unitario deve essere compreso tra " + pesoVivoMin + "e" + pesoVivoMax;
      errors.add(errore);
    }

    // l’obbligatorietà o meno di queste colonne deve essere dinamica e dipende
    // da db_tipo_specie_animale.FLAG_STALLA_PASCOLO
    // se “S” I dati sono obbligatori se <>”S” i dati non sono obbligatori
    if (isFlagStallaPascolo())
    {
      boolean nnCicli = true;
      try
      {
        String errore[] = new String[2];
        errore[0] = "err_numeroCicliAnnualiTab";
        errore[1] = "n./cicli deve essere un numero intero compreso tra 1 e 365";
        long numeroCicliAnnualiL = Long.parseLong(numeroCicliAnnuali);
        if (numeroCicliAnnualiL < 1 || numeroCicliAnnualiL > 365)
        {
          errors.add(errore);
          nnCicli = false;
        }
      }
      catch (Exception e)
      {
        nnCicli = false;
        String errore[] = new String[2];
        errore[0] = "err_numeroCicliAnnualiTab";
        errore[1] = "n./cicli deve essere un numero intero compreso tra 1 e 365";
        errors.add(errore);
      }

      try
      {
        String errore[] = new String[2];
        errore[0] = "err_cicliTab";
        errore[1] = "Durata ciclo (gg) deve essere un numero intero compreso tra 1 e 365";
        long cicliL = Long.parseLong(cicli);
        if (cicliL < 1 || cicliL > 365)
        {
          errors.add(errore);
          nnCicli = false;
        }
      }
      catch (Exception e)
      {
        nnCicli = false;
        String errore[] = new String[2];
        errore[0] = "err_cicliTab";
        errore[1] = "Durata ciclo (gg) deve essere un numero intero compreso tra 1 e 365";
        errors.add(errore);
      }
      
      int totaleGiorniCicli = 0;      
      try
      {
        int cicliI = Integer.parseInt(cicli);
        int numeroCicliAnnualiI = Integer.parseInt(numeroCicliAnnuali);
        totaleGiorniCicli = cicliI * numeroCicliAnnualiI;
        
        if((totaleGiorniCicli > 365) && nnCicli)
        {
          String errore[] = new String[2];
          errore[0] = "err_cicliTab";
          errore[1] = "Durata ciclo (gg) moltiplicato per n./cicli non deve superare 365 giorni";
          errors.add(errore);
          String errore2[] = new String[2];
          errore2[0] = "err_numeroCicliAnnualiTab";
          errore2[1] = "Durata ciclo (gg) moltiplicato per n./cicli non deve superare 365 giorni";
          errors.add(errore2);          
        }
          
        
      }
      //Darebbe errore nei controlli sopra
      catch (Exception e)
      {}

      // Controlli: La somma dei valori imputati per i giorni seguenti deve
      // essere <=365
      int totaleGiorni = 0;
      boolean giorniPermanenzaPascolo = true;

      try
      {
        String errore[] = new String[2];
        errore[0] = "err_giorniPascoloEstateTab";
        errore[1] = "GG deve essere un numero intero compreso tra 0 e 183";
        long temp = Long.parseLong(giorniPascoloEstate);
        totaleGiorni += temp;
        if (temp < 0 || temp > 183)
        {
          errors.add(errore);
          giorniPermanenzaPascolo = false;
        }
      }
      catch (Exception e)
      {
        giorniPermanenzaPascolo = false;
        String errore[] = new String[2];
        errore[0] = "err_giorniPascoloEstateTab";
        errore[1] = "GG deve essere un numero intero compreso tra 0 e 183";
        errors.add(errore);
      }

      try
      {
        String errore[] = new String[2];
        errore[0] = "err_giorniPascoloInvernoTab";
        errore[1] = "GG deve essere un numero intero compreso tra 0 e 183";
        long temp = Long.parseLong(giorniPascoloInverno);
        totaleGiorni += temp;
        if (temp < 0 || temp > 183)
        {
          errors.add(errore);
          giorniPermanenzaPascolo = false;
        }
      }
      catch (Exception e)
      {
        giorniPermanenzaPascolo = false;
        String errore[] = new String[2];
        errore[0] = "err_giorniPascoloInvernoTab";
        errore[1] = "GG deve essere un numero intero compreso tra 0 e 183";
        errors.add(errore);
      }

      // Controlli: La somma dei valori imputati per i giorni seguenti deve
      // essere <=365
      if (totaleGiorni > 365 && giorniPermanenzaPascolo)
      {
        String errore[] = new String[2];
        errore[0] = "err_giorniPascoloEstateTab";
        errore[1] = "La somma dei giorni di permanenza pascolo non deve superare 365";
        String errore2[] = new String[2];
        errore2[0] = "err_giorniPascoloInvernoTab";
        errore2[1] = "La somma dei giorni di permanenza pascolo non deve superare 365";
        errors.add(errore);
        errors.add(errore2);
      }

      try
      {
        String errore[] = new String[2];
        errore[0] = "err_orePascoloEstateTab";
        errore[1] = "Ore/gg deve essere un numero intero compreso tra 0 e 24";
        long temp = Long.parseLong(orePascoloEstate);
        if (temp < 0 || temp > 24)
        {
          errors.add(errore);
          giorniPermanenzaPascolo = false;
        }
      }
      catch (Exception e)
      {
        giorniPermanenzaPascolo = false;
        String errore[] = new String[2];
        errore[0] = "err_orePascoloEstateTab";
        errore[1] = "Ore/gg deve essere un numero intero compreso tra 0 e 24";
        errors.add(errore);
      }

      try
      {
        String errore[] = new String[2];
        errore[0] = "err_orePascoloInvernoTab";
        errore[1] = "Ore/gg deve essere un numero intero compreso tra 0 e 24";
        long temp = Long.parseLong(orePascoloInverno);
        if (temp < 0 || temp > 24)
        {
          errors.add(errore);
          giorniPermanenzaPascolo = false;
        }
      }
      catch (Exception e)
      {
        giorniPermanenzaPascolo = false;
        String errore[] = new String[2];
        errore[0] = "err_orePascoloInvernoTab";
        errore[1] = "Ore/gg deve essere un numero intero compreso tra 0 e 24";
        errors.add(errore);
      }
      
      
      try
      {
        long tempGg = Long.parseLong(giorniPascoloInverno);
        long tempOre = Long.parseLong(orePascoloInverno);
        
        if((tempGg > 0) && (tempOre == 0) && giorniPermanenzaPascolo)
        {
          String errore[] = new String[2];
          errore[0] = "err_giorniPascoloInvernoTab";
          errore[1] = "GG maggiore di 0, anche il campo Ore/gg deve essere maggiore di 0";
          errors.add(errore);
          String errore2[] = new String[2];
          errore2[0] = "err_orePascoloInvernoTab";
          errore2[1] = "GG maggiore di 0, anche il campo Ore/gg deve essere maggiore di 0";
          errors.add(errore2);
        }
        else if((tempOre > 0) && (tempGg == 0) && giorniPermanenzaPascolo)
        {
          String errore[] = new String[2];
          errore[0] = "err_giorniPascoloInvernoTab";
          errore[1] = "Ore/gg maggiore di 0, anche il campo gg deve essere maggiore di 0";
          errors.add(errore);
          String errore2[] = new String[2];
          errore2[0] = "err_orePascoloInvernoTab";
          errore2[1] = "Ore/gg maggiore di 0, anche il campo gg deve essere maggiore di 0";
          errors.add(errore2);
        }
      }
      //Darebbe errore nei controlli sopra
      catch (Exception e)
      {}
      
      try
      {
        long tempGg = Long.parseLong(giorniPascoloEstate);
        long tempOre = Long.parseLong(orePascoloEstate);
        
        if((tempGg > 0) && (tempOre == 0) && giorniPermanenzaPascolo)
        {
          String errore[] = new String[2];
          errore[0] = "err_giorniPascoloEstateTab";
          errore[1] = "gg maggiore di 0, anche il campo Ore/gg deve essere maggiore di 0";
          errors.add(errore);
          String errore2[] = new String[2];
          errore2[0] = "err_orePascoloEstateTab";
          errore2[1] = "gg maggiore di 0, anche il campo Ore/gg deve essere maggiore di 0";
          errors.add(errore2);
        }
        else if((tempOre > 0) && (tempGg == 0) && giorniPermanenzaPascolo)
        {
          String errore[] = new String[2];
          errore[0] = "err_giorniPascoloEstateTab";
          errore[1] = "Ore/gg maggiore di 0, anche il campo gg deve essere maggiore di 0";
          errors.add(errore);
          String errore2[] = new String[2];
          errore2[0] = "err_orePascoloEstateTab";
          errore2[1] = "Ore/gg maggiore di 0, anche il campo gg deve essere maggiore di 0";
          errors.add(errore2);
        }
      }
      //Darebbe errore nei controlli sopra
      catch (Exception e)
      {}

    }

    errori = errors.size() == 0 ? null : (String[][]) errors.toArray(new String[0][]);

    if (errori != null)
      return true;
    else
      return false;
  }
  
  
  public boolean validateInsert()
  {
    Vector<String[]> errors = new Vector<String[]>();

    try
    {
      String errore[] = new String[2];
      errore[0] = "err_capiTab";
      errore[1] = "La quantità indicata deve essere un numero intero compreso tra 0 e 9999999";
      long quantitaL = Long.parseLong(quantita);
      if (quantitaL < 0 || quantitaL > 9999999)
        errors.add(errore);
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      errore[0] = "err_capiTab";
      errore[1] = "La quantità indicata deve essere un numero intero compreso tra 0 e 9999999";
      errors.add(errore);
    }
    
    
    if(Validator.isNotEmpty(quantitaProprieta))
    {
      try
      {
        String errore[] = new String[2];
        errore[0] = "err_capiProprietaTab";
        errore[1] = "La quantità indicata deve essere un numero intero compreso tra 0 e 9999999";
        long quantitaL = Long.parseLong(quantitaProprieta);
        if (quantitaL < 0 || quantitaL > 9999999)
          errors.add(errore);
      }
      catch (Exception e)
      {
        String errore[] = new String[2];
        errore[0] = "err_capiPropritaTab";
        errore[1] = "La quantità indicata deve essere un numero intero compreso tra 0 e 9999999";
        errors.add(errore);
      }
    }

    try
    {
      String errore[] = new String[2];
      errore[0] = "err_pesoVivoTab";
      errore[1] = "Il peso vivo medio unitario deve essere compreso tra " + pesoVivoMin + " e " + pesoVivoMax;
      if (Validator.isNotEmpty(pesoVivo))
      {
        double pesoVivoD = Double.parseDouble(pesoVivo.replace(',', '.'));
        if (pesoVivoD < pesoVivoMin || pesoVivoD > pesoVivoMax)
          errors.add(errore);
        else
        {
          String temp = checkDecimals(pesoVivoD, 1);
          if (temp != null)
          {
            errore[1] = temp;
            errors.add(errore);
          }
        }
      }
    }
    catch (Exception e)
    {
      String errore[] = new String[2];
      errore[0] = "err_pesoVivoTab";
      errore[1] = "Il peso vivo medio unitario deve essere compreso tra " + pesoVivoMin + "e" + pesoVivoMax;
      errors.add(errore);
    }

    // l’obbligatorietà o meno di queste colonne deve essere dinamica e dipende
    // da db_tipo_specie_animale.FLAG_STALLA_PASCOLO
    // se “S” I dati sono obbligatori se <>”S” i dati non sono obbligatori
    if (isFlagStallaPascolo())
    {
      boolean nnCicli = true;
      try
      {
        String errore[] = new String[2];
        errore[0] = "err_numeroCicliAnnualiTab";
        errore[1] = "n/cicli deve essere un numero intero compreso tra 1 e 365";
        long numeroClicliAnnualiL = Long.parseLong(numeroCicliAnnuali);
        if (numeroClicliAnnualiL < 1 || numeroClicliAnnualiL > 365)
        {
          errors.add(errore);
          nnCicli = false;
        }
      }
      catch (Exception e)
      {
        nnCicli = false;
        String errore[] = new String[2];
        errore[0] = "err_numeroCicliAnnualiTab";
        errore[1] = "n/cicli deve essere un numero intero compreso tra 1 e 365";
        errors.add(errore);
      }

      try
      {
        String errore[] = new String[2];
        errore[0] = "err_cicliTab";
        errore[1] = "Cicli(n. GG) deve essere un numero intero compreso tra 1 e 365";
        long cicliL = Long.parseLong(cicli);
        if (cicliL < 1 || cicliL > 365)
        {
          errors.add(errore);
          nnCicli = false;
        }
      }
      catch (Exception e)
      {
        nnCicli = false;
        String errore[] = new String[2];
        errore[0] = "err_cicliTab";
        errore[1] = "Cicli(n. GG) deve essere un numero intero compreso tra 1 e 365";
        errors.add(errore);
      }
      
      
      int totaleGiorniCicli = 0;      
      try
      {
        int cicliI = Integer.parseInt(cicli);
        int numeroCicliAnnualiI = Integer.parseInt(numeroCicliAnnuali);
        totaleGiorniCicli = cicliI * numeroCicliAnnualiI;
        
        if((totaleGiorniCicli > 365) && nnCicli)
        {
          String errore[] = new String[2];
          errore[0] = "err_cicliTab";
          errore[1] = "Durata ciclo (gg) moltiplicato per n./cicli non deve superare 365 giorni";
          errors.add(errore);
          String errore2[] = new String[2];
          errore2[0] = "err_numeroCicliAnnualiTab";
          errore2[1] = "Durata ciclo (gg) moltiplicato per n./cicli non deve superare 365 giorni";
          errors.add(errore2);          
        }
          
        
      }
      //Darebbe errore nei controlli sopra
      catch (Exception e)
      {}
      
      

      // Controlli: La somma dei valori imputati per i giorni seguenti deve
      // essere <=365
      int totaleGiorni = 0;
      boolean giorniPermanenzaPascolo = true;

      try
      {
        String errore[] = new String[2];
        errore[0] = "err_giorniPascoloEstateTab";
        errore[1] = "GG deve essere un numero intero compreso tra 0 e 183";
        long temp = Long.parseLong(giorniPascoloEstate);
        totaleGiorni += temp;
        if (temp < 0 || temp > 183)
        {
          errors.add(errore);
          giorniPermanenzaPascolo = false;
        }
      }
      catch (Exception e)
      {
        giorniPermanenzaPascolo = false;
        String errore[] = new String[2];
        errore[0] = "err_giorniPascoloEstateTab";
        errore[1] = "GG deve essere un numero intero compreso tra 0 e 183";
        errors.add(errore);
      }

      try
      {
        String errore[] = new String[2];
        errore[0] = "err_giorniPascoloInvernoTab";
        errore[1] = "GG deve essere un numero intero compreso tra 0 e 183";
        long temp = Long.parseLong(giorniPascoloInverno);
        totaleGiorni += temp;
        if (temp < 0 || temp > 183)
        {
          errors.add(errore);
          giorniPermanenzaPascolo = false;
        }
      }
      catch (Exception e)
      {
        giorniPermanenzaPascolo = false;
        String errore[] = new String[2];
        errore[0] = "err_giorniPascoloInvernoTab";
        errore[1] = "GG deve essere un numero intero compreso tra 0 e 183";
        errors.add(errore);
      }

      // Controlli: La somma dei valori imputati per i giorni seguenti deve
      // essere <=365
      if (totaleGiorni > 365 && giorniPermanenzaPascolo)
      {
        String errore[] = new String[2];
        errore[0] = "err_giorniPascoloEstateTab";
        errore[1] = "La somma dei giorni di permanenza pascolo non deve superare 365";
        String errore2[] = new String[2];
        errore2[0] = "err_giorniPascoloInvernoTab";
        errore2[1] = "La somma dei giorni di permanenza pascolo non deve superare 365";
        errors.add(errore);
        errors.add(errore2);
      }

      try
      {
        String errore[] = new String[2];
        errore[0] = "err_orePascoloEstateTab";
        errore[1] = "Ore/GG deve essere un numero intero compreso tra 0 e 24";
        long temp = Long.parseLong(orePascoloEstate);
        if (temp < 0 || temp > 24)
          errors.add(errore);
      }
      catch (Exception e)
      {
        giorniPermanenzaPascolo = false;
        String errore[] = new String[2];
        errore[0] = "err_orePascoloEstateTab";
        errore[1] = "Ore/GG deve essere un numero intero compreso tra 0 e 24";
        errors.add(errore);
      }

      try
      {
        String errore[] = new String[2];
        errore[0] = "err_orePascoloInvernoTab";
        errore[1] = "Ore/GG deve essere un numero intero compreso tra 0 e 24";
        long temp = Long.parseLong(orePascoloInverno);
        if (temp < 0 || temp > 24)
          errors.add(errore);
      }
      catch (Exception e)
      {
        giorniPermanenzaPascolo = false;
        String errore[] = new String[2];
        errore[0] = "err_orePascoloInvernoTab";
        errore[1] = "Ore/GG deve essere un numero intero compreso tra 0 e 24";
        errors.add(errore);
      }

    }

    errori = errors.size() == 0 ? null : (String[][]) errors.toArray(new String[0][]);

    if (errori != null)
      return true;
    else
      return false;
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

  // Formula rif. A
  public double ggPermanenzaStalla()
  {
    // GG permanenza stalla (GG/anno)= 
    // 365 
    // – 
    // (
    //   (ore pascolo estivo /24 * giorni pascolo estate) + 
    //   (ore pascolo inverno / 24 * giorni pascolo inverno).
    // )
    double risultato = 0;
    //double ggVuotoSanDich = Integer.parseInt(this.giorniVuotoSanitario);
    //double ggCicloDich = Integer.parseInt(this.cicli);
    double hPascEstate = Integer.parseInt(this.orePascoloEstate);
    double hPascInverno = Integer.parseInt(this.orePascoloInverno);
    double ggPascEstate = Integer.parseInt(this.giorniPascoloEstate);
    double ggPascInverno = Integer.parseInt(this.giorniPascoloInverno);

    risultato = 365 - ((hPascEstate / 24 * ggPascEstate) + (hPascInverno / 24 * ggPascInverno));

    SolmrLogger.debug(this,"\n\nFormula rif. A : ggPermanenzaStalla:" +(double)Math.round(risultato)+"\n\n");
    
    
    
    return Math.round(risultato);
  }
  
  //Formula rif. B
  public double ggPermanenzaStallaAziendale()
  {
    // 365 – 
    // (
    //   (gg di vuoto sanitario dichiarato – gg vuoto sanitario parametro) * 
    //   (
    //     (gg di ciclo dichiarato – gg di ciclo parametro)/365
    //   )
    // )
    /*double risultato=0; 
    double numeroClicliAnnualiDb = Integer.parseInt(this.numeroCicliAnnuali);
    double ggCicloDich = Integer.parseInt(this.cicli);
    
    risultato=numeroClicliAnnualiDb * ggCicloDich;
    
    SolmrLogger.debug(this,"\n\nFormula rif. B : ggPermanenzaStallaAziendale:" +(double)Math.round(risultato)+"\n\n");
 
    return Math.round(risultato);*/
    
 // GG permanenza stalla (GG/anno)= 
    // 365 
    // – 
    // (
    //   (ore pascolo estivo /24 * giorni pascolo estate) + 
    //   (ore pascolo inverno / 24 * giorni pascolo inverno).
    // )
    double risultato = 0;
    //double ggVuotoSanDich = Integer.parseInt(this.giorniVuotoSanitario);
    //double ggCicloDich = Integer.parseInt(this.cicli);
    double hPascEstate = Integer.parseInt(this.orePascoloEstate);
    double hPascInverno = Integer.parseInt(this.orePascoloInverno);
    double ggPascEstate = Integer.parseInt(this.giorniPascoloEstate);
    double ggPascInverno = Integer.parseInt(this.giorniPascoloInverno);

    risultato = 365 - ((hPascEstate / 24 * ggPascEstate) + (hPascInverno / 24 * ggPascInverno));

    SolmrLogger.debug(this,"\n\nFormula rif. A : ggPermanenzaStalla:" +(double)Math.round(risultato)+"\n\n");
    
    
    
    return Math.round(risultato);
    
  }

  public Vector<StabulazioneTrattamento> getVStabulazioneTrattamentoVO()
  {
    return vStabulazioneTrattamentoVO;
  }

  public void setVStabulazioneTrattamentoVO(Vector<StabulazioneTrattamento> stabulazioneTrattamentoVO)
  {
    vStabulazioneTrattamentoVO = stabulazioneTrattamentoVO;
  }

  public String getNumeroCicliAnnuali()
  {
    return numeroCicliAnnuali;
  }

  public void setNumeroCicliAnnuali(String numeroCicliAnnuali)
  {
    this.numeroCicliAnnuali = numeroCicliAnnuali;
  }

  public double getPesoVivoAzoto()
  {
    return pesoVivoAzoto;
  }

  public void setPesoVivoAzoto(double pesoVivoAzoto)
  {
    this.pesoVivoAzoto = pesoVivoAzoto;
  }

  public String getQuantitaProprieta()
  {
    return quantitaProprieta;
  }

  public void setQuantitaProprieta(String quantitaProprieta)
  {
    this.quantitaProprieta = quantitaProprieta;
  }

  public String getGiorniVuotoSanitario()
  {
    return giorniVuotoSanitario;
  }

  public void setGiorniVuotoSanitario(String giorniVuotoSanitario)
  {
    this.giorniVuotoSanitario = giorniVuotoSanitario;
  }

  public int getGgVuotoSanitario()
  {
    return ggVuotoSanitario;
  }

  public void setGgVuotoSanitario(int ggVuotoSanitario)
  {
    this.ggVuotoSanitario = ggVuotoSanitario;
  }

  public String getLattazione()
  {
    return lattazione;
  }

  public void setLattazione(String lattazione)
  {
    this.lattazione = lattazione;
  }
  
  

}
