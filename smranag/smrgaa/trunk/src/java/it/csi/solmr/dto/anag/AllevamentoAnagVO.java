package it.csi.solmr.dto.anag;

import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoAcquaLavaggio;
import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento;
import it.csi.smranag.smrgaa.util.ErrorUtils;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author Castagno Raffaele
 * @version 0.1
 */

public class AllevamentoAnagVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long               serialVersionUID                       = -7447004083744611207L;

  public static final int         MODIFICA_OLD                           = 1;
  public static final int         MODIFICA_FINE_VALIDITA                 = 2;
  public static final int         MODIFICA_CAPI_UBICAZIONE               = 3;
  public static final int         MODIFICA_FINE_VALIDITA_CAPI_UBICAZIONE = 4;

  public static final String      ID_SPECIE_BOVINI_CARNE                 = "1";
  public static final String      ID_SPECIE_BOVINI_ALLEVAMENTO           = "2";
  public static final String      ID_SPECIE_OVINI                        = "5";
  public static final String      ID_SPECIE_CAPRINI                      = "6";
  public static final String      ID_AVICOLI                             = "7";

  private String                  idAllevamento;
  private String                  idUTE;
  private String                  idASL;
  private String                  istatComuneAllevamento;
  private String                  codiceProvinciaAllevamento;
  private String                  idSpecieAnimale;
  private String                  codiceAziendaZootecnica;
  private String                  dataInizio;
  private Date                    dataInizioDate;
  private String                  dataFine;
  private Date                    dataFineDate;
  private String                  note;
  private String                  dataAggiornamento;
  private Date                    dataAggiornamentoDate;
  private String                  idUtenteAggiornamento;
  private String                  indirizzo;
  private String                  cap;
  private String                  telefono;

  private String                  sommaQuantita;
  private String                  sommaQuantitaProprietario;
  private String                  sommaUBA;
  private TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO;
  private TipoASLAnagVO           tipoASLAnagVO;
  private UtenteIrideVO           utenteAggiornamento;

  private Vector<?>                  categorieAllevamentoVector;
  private Vector<?>                  stabulazioniTrattamenti;
  
  private Vector<AllevamentoAcquaLavaggio>  vAllevamentoAcquaLavaggio;

  private int                     tipoModifica;
  private ComuneVO                comuneVO;

  private String                  codiceFiscaleProprietario;                                     // CODICE_FISCALE_PROPRIETARIO
  private String                  denominazioneProprietario;                                     // DENOMINAZIONE_PROPRIETARIO
  private String                  codiceFiscaleDetentore;                                        // CODICE_FISCALE_DETENTORE
  private String                  denominazioneDetentore;                                        // DENOMINAZIONE_DETENTORE
  private String                  dataInizioDetenzione;                                          // DATA_INIZIO_DETENZIONE
  private String                  dataFineDetenzione;                                            // DATA_FINE_DETENZIONE
  private boolean                 flagSoccida;                                                   // FLAG_SOCCIDA
  private String                  idTipoProduzione;                                              // ID_TIPO_PRODUZIONE
  private String                  idOrientamentoProduttivo;                                      // ID_ORIENTAMENTO_PRODUTTIVO
  private String                  idTipoProduzioneCosman;                                        // ID_TIPO_PRODUZIONE_COSMAN
  private String                  descTipoProduzione;
  private String                  descOrientamentoProduttivo;
  private String                  descTipoProduzioneCosman;
  private String									flagAssicuratoCosman;																					 // DB_ALLEVAMENTO.FLAG_ASSICURATO_COSMAN
  
  private String                  mediaCapiLattazione;                                           // DB_ALLEVAMENTO.MEDIA_CAPI_LATTAZIONE
  private String                  quantitaAcquaLavaggio;                                         // DB_ALLEVAMENTO.QUANTITA_ACQUA_LAVAGGIO
  private String                  flagAcqueEffluenti;                                            // DB_ALLEVAMENTO.FLAG_ACQUE_EFFLUENTI
  private String                  idMungitura;                                                   // DB_ALLEVAMENTO.ID_MUNGITURA
  private String                  descMungitura;                                                 // DB_TIPO_MUNGITURA.DESCRIZIONE
  private String                  descrizioneAltriTrattam;                                       // DB_ALLEVAMENTO.DESCRIZIONE_ALTRI_TRATTAM
  private String                  flagDeiezioneAvicoli;                                          // DB_ALLEVAMENTO.FLAG_DEIEZIONE_AVICOLI
  private String                  superficieLettieraPermanente;                                  // DB_ALLEVAMENTO.SUPERFICIE_LETTIERA_PERMANENTE
  boolean                         strutturaMungitura;                                            // Questo campo mi indica se devo far vedere la struttura di mungitura o meno
  private String                  altezzaLettieraPermanente;                                     // DB_ALLEVAMENTO.ALTEZZA_LETTIERA_PERMANENTE
  private double            altLettieraPermMin;
  private double            altLettieraPermMax;
  private String biologico;
  private String denominazione;
  private String longitudineStr;
  private String latitudineStr;
  private BigDecimal longitudine;
  private BigDecimal latitudine;
  private String dataInizioAttivita;
  private String motivoSoccida;
  private ComuneVO                comuneUteVO;
  private String indirizzoUte;
  private String esitoControllo;
  
  
  

  public double getAltLettieraPermMin()
  {
    return altLettieraPermMin;
  }

  public void setAltLettieraPermMin(double altLettieraPermMin)
  {
    this.altLettieraPermMin = altLettieraPermMin;
  }

  public double getAltLettieraPermMax()
  {
    return altLettieraPermMax;
  }

  public void setAltLettieraPermMax(double altLettieraPermMax)
  {
    this.altLettieraPermMax = altLettieraPermMax;
  }

  public String getDescrizioneAltriTrattam()
  {
    return descrizioneAltriTrattam;
  }

  public void setDescrizioneAltriTrattam(String descrizioneAltriTrattam)
  {
    this.descrizioneAltriTrattam = descrizioneAltriTrattam;
  }

  public String getFlagDeiezioneAvicoli()
  {
    return flagDeiezioneAvicoli;
  }

  public void setFlagDeiezioneAvicoli(String flagDeiezioneAvicoli)
  {
    this.flagDeiezioneAvicoli = flagDeiezioneAvicoli;
  }

  public AllevamentoAnagVO()
  {
    tipoSpecieAnimaleAnagVO = new TipoSpecieAnimaleAnagVO();
    tipoASLAnagVO = new TipoASLAnagVO();
  }

  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }

  public String getIndirizzo()
  {
    return indirizzo;
  }

  public void setCap(String cap)
  {
    this.cap = cap;
  }

  public String getCap()
  {
    return cap;
  }

  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }

  public String getTelefono()
  {
    return telefono;
  }

  public String getIdAllevamento()
  {
    return idAllevamento;
  }

  public void setIdAllevamento(String idAllevamento)
  {
    this.idAllevamento = idAllevamento;
  }

  public Long getIdAllevamentoLong()
  {
    try
    {
      return new Long(idAllevamento);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setIdAllevamentoLong(Long idAllevamento)
  {
    this.idAllevamento = idAllevamento == null ? null : idAllevamento.toString();
  }

  public String getIdUTE()
  {
    return idUTE;
  }

  public void setIdUTE(String idUTE)
  {
    this.idUTE = idUTE;
  }

  public Long getIdUTELong()
  {
    try
    {
      return new Long(idUTE);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setIdUTELong(Long idUTE)
  {
    this.idUTE = idUTE == null ? null : idUTE.toString();
  }

  public String getIdASL()
  {
    return idASL;
  }

  public void setIdASL(String idASL)
  {
    this.idASL = idASL;
  }

  public Long getIdASLLong()
  {
    try
    {
      return new Long(idASL);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setIdASLLong(Long idASL)
  {
    this.idASL = idASL == null ? null : idASL.toString();
  }

  public String getIstatComuneAllevamento()
  {
    return istatComuneAllevamento;
  }

  public void setIstatComuneAllevamento(String istatComuneAllevamento)
  {
    this.istatComuneAllevamento = istatComuneAllevamento;
  }

  public String getCodiceProvinciaAllevamento()
  {
    return codiceProvinciaAllevamento;
  }

  public void setCodiceProvinciaAllevamento(String codiceProvinciaAllevamento)
  {
    this.codiceProvinciaAllevamento = codiceProvinciaAllevamento;
  }

  public String getIdSpecieAnimale()
  {
    return idSpecieAnimale;
  }

  public void setIdSpecieAnimale(String idSpecieAnimale)
  {
    this.idSpecieAnimale = idSpecieAnimale;
  }

  public Long getIdSpecieAnimaleLong()
  {
    try
    {
      return new Long(idSpecieAnimale);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setIdSpecieAnimaleLong(Long idSpecieAnimale)
  {
    this.idSpecieAnimale = idSpecieAnimale == null ? null : idSpecieAnimale.toString();
  }

  public String getCodiceAziendaZootecnica()
  {
    return codiceAziendaZootecnica;
  }

  public void setCodiceAziendaZootecnica(String codiceAziendaZootecnica)
  {
    this.codiceAziendaZootecnica = codiceAziendaZootecnica;
  }

  public void setDataInizio(String dataInizio)
  {
    this.dataInizio = dataInizio;
    try
    {
      this.dataInizioDate = DateUtils.parseDate(dataInizio);
    }
    catch (Exception ex)
    {
    }
  }

  public String getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizioDate(Date dataInizioDate)
  {
    this.dataInizioDate = dataInizioDate;
    try
    {
      this.dataInizio = DateUtils.formatDate(dataInizioDate);
    }
    catch (Exception ex)
    {
    }
  }

  public java.util.Date getDataInizioDate()
  {
    return dataInizioDate;
  }

  public void setDataFine(String dataFine)
  {
    this.dataFine = dataFine;
    try
    {
      this.dataFineDate = DateUtils.parseDate(dataFine);
    }
    catch (Exception ex)
    {
    }
  }

  public String getDataFine()
  {
    return dataFine;
  }

  public void setDataFineDate(Date dataFineDate)
  {
    this.dataFineDate = dataFineDate;
    try
    {
      this.dataFine = DateUtils.formatDate(dataFineDate);
    }
    catch (Exception ex)
    {
    }
  }

  public java.util.Date getDataFineDate()
  {
    return dataFineDate;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public void setDataAggiornamento(String dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
    try
    {
      this.dataAggiornamentoDate = DateUtils.parseDate(dataAggiornamento);
    }
    catch (Exception ex)
    {
    }
  }

  public String getDataAggiornamento()
  {
    return dataAggiornamento;
  }

  public void setDataAggiornamentoDate(Date dataAggiornamentoDate)
  {
    this.dataAggiornamentoDate = dataAggiornamentoDate;
    try
    {
      this.dataAggiornamento = DateUtils.formatDate(dataAggiornamentoDate);
    }
    catch (Exception ex)
    {
    }
  }

  public java.util.Date getDataAggiornamentoDate()
  {
    return dataAggiornamentoDate;
  }

  public String getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }

  public void setIdUtenteAggiornamento(String idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }

  public Long getIdUtenteAggiornamentoLong()
  {
    try
    {
      return new Long(idUtenteAggiornamento);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setIdUtenteAggiornamentoLong(Long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento == null ? null : idUtenteAggiornamento.toString();
  }

  public String getSommaQuantita()
  {
    return sommaQuantita;
  }

  public void setSommaQuantita(String sommaQuantita)
  {
    this.sommaQuantita = sommaQuantita;
  }

  public Long getSommaQuantitaLong()
  {
    try
    {
      return new Long(sommaQuantita);
    }
    catch (Exception ex)
    {
      return null;
    }
  }
  
  public Long getSommaQuantitaPropriatarioLong()
  {
    try
    {
      return new Long(sommaQuantitaProprietario);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setSommaQuantitaLong(Long sommaQuantita)
  {
    this.sommaQuantita = sommaQuantita == null ? null : sommaQuantita.toString();
  }
  
  public void setSommaQuantitaProprietarioLong(Long sommaQuantitaProprietario)
  {
    this.sommaQuantitaProprietario = sommaQuantitaProprietario == null ? null : sommaQuantitaProprietario.toString();
  }

  public String getSommaUBA()
  {
    return sommaUBA;
  }

  public void setSommaUBA(String sommaUBA)
  {
    this.sommaUBA = sommaUBA;
  }

  public Double getsommaUBADouble()
  {
    try
    {
      return new Double(sommaUBA);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setsommaUBADouble(Double sommaUBA)
  {
    this.sommaUBA = sommaUBA == null ? null : sommaUBA.toString();
  }

  public void setTipoSpecieAnimaleAnagVO(TipoSpecieAnimaleAnagVO tipoSpecieAnimaleAnagVO)
  {
    this.tipoSpecieAnimaleAnagVO = tipoSpecieAnimaleAnagVO;
  }

  public TipoSpecieAnimaleAnagVO getTipoSpecieAnimaleAnagVO()
  {
    return this.tipoSpecieAnimaleAnagVO;
  }

  public void setTipoASLAnagVO(TipoASLAnagVO tipoASLAnagVO)
  {
    this.tipoASLAnagVO = tipoASLAnagVO;
  }

  public TipoASLAnagVO getTipoASLAnagVO()
  {
    return this.tipoASLAnagVO;
  }

  public void setUtenteAggiornamentoVO(UtenteIrideVO utenteAggiornamento)
  {
    this.utenteAggiornamento = utenteAggiornamento;
  }

  public UtenteIrideVO getUtenteAggiornamento()
  {
    return this.utenteAggiornamento;
  }

  public void setTipoModifica(int tipoModifica)
  {
    this.tipoModifica = tipoModifica;
  }

  public int getTipoModifica()
  {
    return tipoModifica;
  }

  public String getCodiceFiscaleProprietario()
  {
    return codiceFiscaleProprietario;
  }

  public void setCodiceFiscaleProprietario(String codiceFiscaleProprietario)
  {
    this.codiceFiscaleProprietario = codiceFiscaleProprietario;
  }

  public String getDenominazioneProprietario()
  {
    return denominazioneProprietario;
  }

  public void setDenominazioneProprietario(String denominazioneProprietario)
  {
    this.denominazioneProprietario = denominazioneProprietario;
  }

  public String getCodiceFiscaleDetentore()
  {
    return codiceFiscaleDetentore;
  }

  public void setCodiceFiscaleDetentore(String codiceFiscaleDetentore)
  {
    this.codiceFiscaleDetentore = codiceFiscaleDetentore;
  }

  public String getDenominazioneDetentore()
  {
    return denominazioneDetentore;
  }

  public void setDenominazioneDetentore(String denominazioneDetentore)
  {
    this.denominazioneDetentore = denominazioneDetentore;
  }

  public String getDataInizioDetenzione()
  {
    return dataInizioDetenzione;
  }

  public void setDataInizioDetenzione(String dataInizioDetenzione)
  {
    this.dataInizioDetenzione = dataInizioDetenzione;
  }

  public String getDataFineDetenzione()
  {
    return dataFineDetenzione;
  }

  public void setDataFineDetenzione(String dataFineDetenzione)
  {
    this.dataFineDetenzione = dataFineDetenzione;
  }

  public boolean isFlagSoccida()
  {
    return flagSoccida;
  }

  public void setFlagSoccida(boolean flagSoccida)
  {
    this.flagSoccida = flagSoccida;
  }

  public String getIdTipoProduzione()
  {
    return idTipoProduzione;
  }

  public void setIdTipoProduzione(String idTipoProduzione)
  {
    this.idTipoProduzione = idTipoProduzione;
  }

  public String getIdOrientamentoProduttivo()
  {
    return idOrientamentoProduttivo;
  }

  public void setIdOrientamentoProduttivo(String idOrientamentoProduttivo)
  {
    this.idOrientamentoProduttivo = idOrientamentoProduttivo;
  }

  public String getDescTipoProduzione()
  {
    return descTipoProduzione;
  }

  public void setDescTipoProduzione(String descTipoProduzione)
  {
    this.descTipoProduzione = descTipoProduzione;
  }

  public String getDescOrientamentoProduttivo()
  {
    return descOrientamentoProduttivo;
  }

  public void setDescOrientamentoProduttivo(String descOrientamentoProduttivo)
  {
    this.descOrientamentoProduttivo = descOrientamentoProduttivo;
  }

  public void setCategorieAllevamentoVector(Vector<?> categorieAllevamentoVector)
  {
    this.categorieAllevamentoVector = categorieAllevamentoVector;

    // La parte seguente va lasciata per garantire la retrocompatibilità
    // con il passato e soprattutto con chi richiama i servizi di anagrafe
    if (categorieAllevamentoVector != null)
    {
      int size = categorieAllevamentoVector.size();
      if (size > 0)
      {
        if (categorieAllevamentoVector.get(0) instanceof CategorieAllevamentoAnagVO)
        {
          Long sommaQuantita = new Long(0);
          Long sommaQuantitaPropietario = new Long(0);
          Double sommaUBA = new Double(0);
          BigDecimal bd = new BigDecimal(0);

          for (int i = 0; i < size; i++)
          {
            CategorieAllevamentoAnagVO catAll = (CategorieAllevamentoAnagVO) categorieAllevamentoVector.get(i);
            if (catAll.getQuantitaLong() != null)
              sommaQuantita = new Long(catAll.getQuantitaLong().longValue() + sommaQuantita.longValue());
            
            if (catAll.getQuantitaProprietaLong() != null)
              sommaQuantitaPropietario = new Long(catAll.getQuantitaProprietaLong().longValue() + sommaQuantitaPropietario.longValue());
            

            if (catAll.getTipoCategoriaAnimaleAnagVO().getCoefficienteUBADouble() != null && catAll.getQuantitaLong() != null)
              bd = new BigDecimal(catAll.getTipoCategoriaAnimaleAnagVO().getCoefficienteUBADouble().doubleValue()).multiply(new BigDecimal(catAll.getQuantitaLong().longValue()));

            sommaUBA = new Double(bd.doubleValue() + sommaUBA.doubleValue());
          }

          this.setSommaQuantitaLong(sommaQuantita);
          this.setSommaQuantitaProprietarioLong(sommaQuantitaPropietario);
          this.setsommaUBADouble(sommaUBA);
        }
      }
    }

  }

  public Vector<?> getCategorieAllevamentoVector()
  {
    return this.categorieAllevamentoVector;
  }

  public String getMediaCapiLattazione()
  {
    return mediaCapiLattazione;
  }

  public void setMediaCapiLattazione(String mediaCapiLattazione)
  {
    this.mediaCapiLattazione = mediaCapiLattazione;
  }

  public String getQuantitaAcquaLavaggio()
  {
    return quantitaAcquaLavaggio;
  }

  public void setQuantitaAcquaLavaggio(String quantitaAcquaLavaggio)
  {
    this.quantitaAcquaLavaggio = quantitaAcquaLavaggio;
  }

  public String getFlagAcqueEffluenti()
  {
    return flagAcqueEffluenti;
  }

  public void setFlagAcqueEffluenti(String flagAcqueEffluenti)
  {
    this.flagAcqueEffluenti = flagAcqueEffluenti;
  }

  public int hashCode()
  {
    return 0;
  }

  public String getIdMungitura()
  {
    return idMungitura;
  }

  public void setIdMungitura(String idMungitura)
  {
    this.idMungitura = idMungitura;
  }

  public String getDescMungitura()
  {
    return descMungitura;
  }

  public void setDescMungitura(String descMungitura)
  {
    this.descMungitura = descMungitura;
  }

  public String getSuperficieLettieraPermanente()
  {
    return superficieLettieraPermanente;
  }

  public void setSuperficieLettieraPermanente(String superficieLettieraPermanente)
  {
    this.superficieLettieraPermanente = superficieLettieraPermanente;
  }
  
  public String getAltezzaLettieraPermanente()
  {
    return altezzaLettieraPermanente;
  }

  public void setAltezzaLettieraPermanente(String altezzaLettieraPermanente)
  {
    this.altezzaLettieraPermanente = altezzaLettieraPermanente;
  }

  public boolean isStrutturaMungitura()
  {
    return ID_SPECIE_BOVINI_CARNE.equals(idSpecieAnimale) || ID_SPECIE_BOVINI_ALLEVAMENTO.equals(idSpecieAnimale) || ID_SPECIE_OVINI.equals(idSpecieAnimale)
        || ID_SPECIE_CAPRINI.equals(idSpecieAnimale);
  }

  public Vector<?> getStabulazioniTrattamenti()
  {
    return stabulazioniTrattamenti;
  }

  public void setStabulazioniTrattamenti(Vector<?> stabulazioniTrattamenti)
  {
    this.stabulazioniTrattamenti = stabulazioniTrattamenti;
  }

  public boolean equals(Object o)
  {
    if (o instanceof AllevamentoAnagVO)
    {
      return true;
    }
    else
      return false;
  }

  public static boolean isNumericInteger(String value)
  {
    boolean ok = false;
    if (value == null || value.equals(""))
      ok = false;
    else
    {
      try
      {
        double doubleValue = AllevamentoAnagVO.convertNumericField(value);
        double longValue = Math.rint(doubleValue);
        if (doubleValue != longValue)
        {
          ok = false;
        }
        else
          if (doubleValue < 0)
          {
            ok = false;
          }
          else
            ok = true;
      }
      catch (ParseException e)
      {
        ok = false;
      }
    }
    return ok;
  }

  public static double convertNumericField(String field) throws ParseException
  {
    double result = 0;
    if (field != null)
    {
      NumberFormat nf = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(Locale.ITALY));

      checkNumericField(field);
      // Si considera anche il punto come un separatore decimale
      field = field.replace('.', ',');
      result = nf.parse(field).doubleValue();
    }
    return result;
  }

  private static void checkNumericField(String field) throws ParseException
  {
    field = field.replace(',', '.');

    try
    {
      Double.parseDouble(field);
      // Occorre considerare l'eventualita di numeri con suffissi speciali,
      // riconosciuti da Java come facenti parte del formato numerico
      // (ad esempio, 20d o 134f). Per evitare che la conversione numerica
      // comprenda questi caratteri, la stringa viene spezzata in due
      if (field.length() > 1)
        Double.parseDouble(field.substring(field.length() - 1));
    }
    catch (NumberFormatException nfe)
    {
      throw new ParseException(nfe.getMessage(), 0);
    }
  }

  /**
   * @return the comuneVO
   */
  public ComuneVO getComuneVO()
  {
    return comuneVO;
  }

  /**
   * @param comuneVO
   *          the comuneVO to set
   */
  public void setComuneVO(ComuneVO comuneVO)
  {
    this.comuneVO = comuneVO;
  }

  public void setCategorieAllevamentoByElenco(Vector<String> categorie, Vector<String> quantita, Vector<String> elencoPesi, Long idAllevamento)
  {
    CategorieAllevamentoAnagVO catAllVO = null;
    Vector<CategorieAllevamentoAnagVO> categorieAllevamento = new Vector<CategorieAllevamentoAnagVO>();
    for (int i = 0; i < categorie.size(); i++)
    {
      catAllVO = new CategorieAllevamentoAnagVO();
      if (AllevamentoAnagVO.isNumericInteger((String) quantita.get(i)) && new Long((String) quantita.get(i)).longValue() > 0)
      {
        catAllVO.setIdAllevamentoLong(idAllevamento);
        catAllVO.setIdCategoriaAnimale((String) categorie.get(i));
        catAllVO.setQuantita((String) quantita.get(i));
        if (elencoPesi != null && elencoPesi.size() > 0)
        {
          if (elencoPesi.get(i) != null && !"".equalsIgnoreCase((String) elencoPesi.get(i)))
          {
            catAllVO.setPesoVivoUnitario(StringUtils.parseDoubleField((String) elencoPesi.get(i)));
          }
        }
        categorieAllevamento.add(catAllVO);
      }
    }
    this.setCategorieAllevamentoVector(categorieAllevamento);
  }

  public void setCategorieAllevamentoByElenco(String[] categorie, String[] quantita, String[] elencoPesoVivo, Long idAllevamento)
  {
    Vector<String> categorieVet = new Vector<String>(categorie.length);
    Vector<String> quantitaVet = new Vector<String>(quantita.length);
    Vector<String> pesiVet = null;
    for (int i = 0; i < categorie.length; i++)
    {
      categorieVet.add(categorie[i]);
    }
    for (int i = 0; i < quantita.length; i++)
    {
      quantitaVet.add(quantita[i]);
    }
    if (elencoPesoVivo != null && elencoPesoVivo.length > 0)
    {
      pesiVet = new Vector<String>(elencoPesoVivo.length);
      for (int i = 0; i < elencoPesoVivo.length; i++)
      {
        pesiVet.add(elencoPesoVivo[i]);
      }
    }
    this.setCategorieAllevamentoByElenco(categorieVet, quantitaVet, pesiVet, idAllevamento);
  }

  public ValidationErrors validate()
  {
    ValidationErrors errors = new ValidationErrors();

    return errors;
  }

  public ValidationErrors validateUpdate(ValidationErrors errors, 
      boolean ugualiDetProp, boolean isProprietarioCat, boolean isProprietario, boolean isDetentore)
  {

    /*
     * verifico che l'idUTE sia valorizzato siccome viene valorizzato in una
     * combo prendendo dati da una tabella di decodifica non vengono eseguiti
     * ulteriori controlli
     */
    if (!Validator.isNotEmpty(idUTE))
    {
      errors.add("idUTE", new ValidationError("Selezionare un UTE"));
    }

    // verifico la correttezza del Codice Azienda Zootecnica
    if (Validator.isNotEmpty(codiceAziendaZootecnica))
    {
      if (codiceAziendaZootecnica.length() != 8)
      {
        errors.add("codiceAziendaZootecnica", new ValidationError("Il Codice Azienda Zootecnica deve essere lunga 8 caratteri"));
      }
      else
      {
        // il codice deve essere alfanumerico
        if (!Validator.isAlphaNumeric(codiceAziendaZootecnica.trim())) 
        {
          errors.add("codiceAziendaZootecnica", new ValidationError("Il Codice Azienda Zootecnica deve essere una stringa alfanumerica"));
        }
      }
      // gli ultimi tre caratteri devono avere un valore compreso tra i due seguenti intervalli
      if (!Validator.isNumericInteger(codiceAziendaZootecnica.substring(5)))
      {
        // A01 => A99
        if (!codiceAziendaZootecnica.substring(5, 6).equalsIgnoreCase("A") 
            || !Validator.isNumericInteger(codiceAziendaZootecnica.substring(6)))
        {
          errors.add("codiceAziendaZootecnica", new ValidationError("Il formato del Codice Azienda Zootecnica è errato"));
        }
        else
        {
          // 01 => 99
          if (new Integer(codiceAziendaZootecnica.substring(6)).intValue() == 0)
          {
            errors.add("codiceAziendaZootecnica", new ValidationError("Il formato del Codice Azienda Zootecnica è errato"));
          }
        }
      }
      else
      {
        // 001 => 999
        if (new Integer(codiceAziendaZootecnica.substring(5)).intValue() == 0)
        {
          errors.add("codiceAziendaZootecnica", new ValidationError("Il formato del Codice Azienda Zootecnica è errato"));
        }
      }
    }

    // Controllo che l'indirizzo sia valorizzato
    if (!Validator.isNotEmpty(indirizzo))
    {
      errors.add("indirizzo", new ValidationError((String) AnagErrors.get("ERR_INDIRIZZO_OBBLIGATORIO")));
    }

    // controllo che il comune di ubicazione sia stato valorizzato, e che sia
    // conforme con il Codice Azienda Zootecnica
    if (!Validator.isNotEmpty(this.getIstatComuneAllevamento()))
    {
      errors.add("descComune", new ValidationError("Il comune di ubicazione dell''allevamento è obbligatorio"));
    }
    else
    {
      if (Validator.isNotEmpty(this.getIstatComuneAllevamento()))
      {
        //Controllo fatto se e solo se è diverso da N il controllo consistenza comune
        if(!"N".equalsIgnoreCase(tipoSpecieAnimaleAnagVO.getFlagControlloComune()))
        {
          if (Validator.isNotEmpty(codiceAziendaZootecnica) && codiceAziendaZootecnica.length() == 8)
          {
            if (!codiceAziendaZootecnica.substring(0, 3).equals(istatComuneAllevamento.substring(3)))
            { // i primi tre caratteri devono corrispondere all'istat comune
              // dell'allevamento
              errors.add("descComune", new ValidationError("Il comune selezionato non è coerente con il Codice Azienda Zootecnica"));
            }
          }
        }
      }
    }
    // controllo che la provincia di ubicazione sia stata valorizzata, e che sia
    // conforme con il Codice Azienda Zootecnica
    if (!Validator.isNotEmpty(this.getCodiceProvinciaAllevamento()))
    {
      errors.add("provincia", new ValidationError("La provincia di ubicazione dell''allevamento è obbligatoria"));
    }
    else
    {
      if (Validator.isNotEmpty(this.getCodiceAziendaZootecnica()) && codiceAziendaZootecnica.length() == 8)
      {
        //Controllo fatto se e solo se è diverso da N il controllo consistenza comune
        if(!"N".equalsIgnoreCase(tipoSpecieAnimaleAnagVO.getFlagControlloComune()))
        {
          if (!codiceAziendaZootecnica.substring(3, 5).equals(this.getCodiceProvinciaAllevamento()))
          {
            errors.add("codiceAziendaZootecnica", new ValidationError("Il Codice Azienda Zootecnica non è coerente con i dati inseriti nella sezione Ubicazione"));
          }
        }
      }
    }
    
    
    Validator.validateDateAll(dataInizioAttivita, "dataInizioAttivita", "data apertura allevamento", errors, true, true);
    
    
    if (Validator.isNotEmpty(latitudineStr))
    {
      if(Validator.validateDouble(latitudineStr, 99.999999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "latitudine", AnagErrors.ERRORE_CAMPO_ERRATO_COORD);
      }
      else if(Double.parseDouble(Validator.validateDouble(latitudineStr, 99.999999)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "latitudine", AnagErrors.ERRORE_CAMPO_ERRATO_COORD);
      }
      else 
      {
        latitudineStr = latitudineStr.replace(",", ".");
        latitudine = new BigDecimal(latitudineStr);
      }
    }
    else
    {
      latitudine = null;
    }
    
    if (Validator.isNotEmpty(longitudineStr))
    {
      if(Validator.validateDouble(longitudineStr, 99.999999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "longitudine", AnagErrors.ERRORE_CAMPO_ERRATO_COORD);
      }
      else if(Double.parseDouble(Validator.validateDouble(longitudineStr, 99.999999)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "longitudine", AnagErrors.ERRORE_CAMPO_ERRATO_COORD);
      }
      else 
      {
        longitudineStr = longitudineStr.replace(",", ".");
        longitudine = new BigDecimal(longitudineStr);
      }
    }
    else
    {
      longitudine = null;
    }
    
    // Controllo che il CAP sia valorizzato
    if (!Validator.isNotEmpty(cap))
    {
      errors.add("cap", new ValidationError((String) AnagErrors.get("ERR_CAP_OBBLIGATORIO")));
    }
    // Se lo è controllo che sia un numerico intero positivo di 5 cifre
    else
    {
      if (!Validator.isNumericInteger(cap) || cap.length() != 5)
      {
        errors.add("cap", new ValidationError((String) AnagErrors.get("ERR_CAP_ERRATO")));
      }
    }
    
    
    if (Validator.isEmpty(idTipoProduzione))
    {
      errors.add("idTipoProduzione", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    
    if (Validator.isEmpty(idOrientamentoProduttivo))
    {
      errors.add("idOrientamentoProduttivo", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    
    
    /*
     * controllo che la specie animale sia stata valorizzata. siccome viene
     * valorizzata in una combo prendendo dati da una tabella di decodifica non
     * vengono eseguiti controlli
     */
    int numCapiMaxLattazione = 0;
    boolean almenoUnaLettiera = false;
    if (!Validator.isNotEmpty(idSpecieAnimale))
    {
      errors.add("idSpecie", new ValidationError("La specie animale è obbligatoria"));
    }
    // verifico se almeno è stata inserita almeno una categoria con quantità
    // maggiore di 0
    else
    {
      if (categorieAllevamentoVector == null || categorieAllevamentoVector.size() == 0)
      {
        errors.add("idSpecie", new ValidationError("E'' necessario inserire almeno una categoria animale con quantità maggiore di 0"));
      }
      else
      {
        int size = categorieAllevamentoVector.size();

        // Controllo i valori inseriti
        boolean errori = false;
        for (int i = 0; i < size; i++)
        {
          SottoCategoriaAllevamento sottoCat = (SottoCategoriaAllevamento) categorieAllevamentoVector.get(i);
          
          
          if (Validator.isNotEmpty(codiceFiscaleDetentore)  
              && Validator.isNotEmpty(codiceFiscaleProprietario))
          {
            
            
            if("S".equalsIgnoreCase(sottoCat.getLattazione()))
            {
              if(isProprietarioCat)
              {
                if(Validator.isNotEmpty(sottoCat.getQuantitaProprieta()))
                  numCapiMaxLattazione +=  new Integer(sottoCat.getQuantitaProprieta()).intValue(); 
              }
              else
              {
                if(Validator.isNotEmpty(sottoCat.getQuantita()))
                  numCapiMaxLattazione +=  new Integer(sottoCat.getQuantita()).intValue(); 
              }
              
            }
          
            if (sottoCat.validateInsert(ugualiDetProp, isProprietarioCat))
             errori = true;
          }
          else
          {
            if (sottoCat.validateInsert())
              errori = true;
          }
        }
        // Se ho trovato almeno un errore devo generare un errors fittizia
        //controllo gli errori delle stabulazioni solo se sono state inserite,altrimenti no dato che non è obbligatorio
        if (stabulazioniTrattamenti!=null && stabulazioniTrattamenti.size()!=0)
        {
          int sizeStab = stabulazioniTrattamenti.size();
          for (int i = 0; i < sizeStab; i++)
          {
            StabulazioneTrattamento stab = (StabulazioneTrattamento) stabulazioniTrattamenti.get(i);
            if (stab.validateInsert()) 
              errori = true;
          }
        }
        
        // Se ho trovato almeno un errore che viene visualizzato vicino al pulsante calcola
        if (errori)
          errors.add("Fittizia", new ValidationError("Fittizia"));
        else
        {
          if (stabulazioniTrattamenti!=null  && stabulazioniTrattamenti.size()!=0)
          {
            boolean erroriQuantitaTot=false;
            //Devo controllare che per ogni consistenza inserita ci siano una o più stabulazione legate 
            //e che la somma delle quantità delle stabulazioni (per una consistenza) sia uguale alla 
            //quantita definita nella consistenza
            for (int i = 0; i < size; i++)
            {
              SottoCategoriaAllevamento sottoCat = (SottoCategoriaAllevamento) categorieAllevamentoVector.get(i);
              long quantitaConsistenza=Long.parseLong(sottoCat.getQuantita());
              long idSottoCategoria=sottoCat.getIdSottoCategoriaAnimale();
              
              //scorro le stabulazioni
              int sizeStab = stabulazioniTrattamenti.size();
              long quantitaStabulazione=0;
              for (int j = 0; j < sizeStab; j++)
              {
                StabulazioneTrattamento stab = (StabulazioneTrattamento) stabulazioniTrattamenti.get(j);
                
                if("S".equalsIgnoreCase(stab.getLettieraPermanente()))
                {
                  almenoUnaLettiera = true;                 
                }
                
                if ((idSottoCategoria+"").equals(stab.getIdSottoCategoriaAnimale()))
                {
                  quantitaStabulazione+=Long.parseLong(stab.getQuantita());
                  stab.setSottoCatAll(sottoCat);
                }
              }
              if (quantitaConsistenza!=quantitaStabulazione)
              {
                errors.add("calcolaStabulazione", new ValidationError("Il numero di capi stabulati e le Sottocategorie indicate devono corrispondere con quelle dichiarate nella sezione Consistenza"));
                erroriQuantitaTot=true;
                break;
              }
            }
            if (!erroriQuantitaTot)
            {
              //Se non ci sono errori procedo al controllo dei campi palabile e non palabile
              int sizeStab = stabulazioniTrattamenti.size();
              for (int i = 0; i < sizeStab; i++)
              {
                StabulazioneTrattamento stab = (StabulazioneTrattamento) stabulazioniTrattamenti.get(i);
                                
                          
                //effettuo i calcoli
                stab.effettuaCalcoli();
                stab.effettuaCalcoliEfflPreTratt();
                
                //if (!stab.isFlagCalcolo()) stab.setTotaleAzoto(totAzotoOld);
                
                if (stab.validateVolumiInsert()) errori = true;
              }
              // Se ho trovato almeno un errore che viene visualizzato vicino al pulsante calcola
              if (errori)
                errors.add("Fittizia", new ValidationError("Fittizia"));
            }
          }
        }

      }
    }
    // verifico che le note, se valorizzate, non siano più lunghe di 300
    // caratteri
    if (Validator.isNotEmpty(note) && note.length() > 300)
    {
      errors.add("note", new ValidationError("Le note non possono essere più lunghe di 300 caratteri"));
    }
    
    
    if (Validator.isNotEmpty(codiceFiscaleProprietario))
    {
      // controllo uno dei due campi, se è valorizzato
      // significa che lo sono tutti e due
      if (Validator.isNotEmpty(codiceFiscaleProprietario))
      {
        codiceFiscaleProprietario = codiceFiscaleProprietario.trim();
        if (codiceFiscaleProprietario.length() == 16)
        {
          // Controllo la correttezza del codice fiscale
          if (!Validator.controlloCf(codiceFiscaleProprietario))
          {
            errors.add("codiceFiscaleProprietario", new ValidationError(AnagErrors.ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA));
          }
        }
        else
        {
          // Controllo la correttezza della partita iva
          if (!Validator.controlloPIVA(codiceFiscaleProprietario))
          {
            errors.add("codiceFiscaleProprietario", new ValidationError(AnagErrors.ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA));
          }
        }
      }
    }
    else
    {
      errors.add("codiceFiscaleProprietario", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));    
    }
    
    if (Validator.isNotEmpty(codiceFiscaleDetentore))
    {
      // controllo uno dei due campi, se è valorizzato
      // significa che lo sono tutti e due
      if (Validator.isNotEmpty(codiceFiscaleDetentore))
      {
        codiceFiscaleDetentore = codiceFiscaleDetentore.trim();
        if (codiceFiscaleDetentore.length() == 16)
        {
          // Controllo la correttezza del codice fiscale
          if (!Validator.controlloCf(codiceFiscaleDetentore))
          {
            errors.add("codiceFiscaleDetentore", new ValidationError(AnagErrors.ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA));
          }
        }
        else
        {
          // Controllo la correttezza della partita iva
          if (!Validator.controlloPIVA(codiceFiscaleDetentore))
          {
            errors.add("codiceFiscaleDetentore", new ValidationError(AnagErrors.ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA));
          }
        }
      }
    }
    else
    {
      errors.add("codiceFiscaleDetentore", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));    
    }
    
    if(!tipoSpecieAnimaleAnagVO.isFlagMofCodAzZoot())
    {
      if(!isDetentore)
      {
        errors.add("codiceFiscaleDetentore", new ValidationError(AnagErrors.ERRORE_NO_DET_PROP_BDN));    
      }
      
      if(!isProprietario)
      {
        errors.add("codiceFiscaleProprietario", new ValidationError(AnagErrors.ERRORE_NO_DET_PROP_BDN));
      }
    }
    else
    {
      if(!isProprietario && !isDetentore)
      {
        errors.add("codiceFiscaleDetentore", new ValidationError(AnagErrors.ERRORE_NO_DET_PROP));    
        errors.add("codiceFiscaleProprietario", new ValidationError(AnagErrors.ERRORE_NO_DET_PROP));
      }
    }
    
    
    // Controllo la correttezza della dataInizioDetenzione
    Date temp = Validator.validateDateAll(dataInizioDetenzione, "dataInizioDetenzione", "Data inizio detenzione", errors, true, true);
    try
    {
      if (temp != null && temp.before(DateUtils.parseDate("01/01/1900")))
        errors.add("dataInizioDetenzione", new ValidationError(AnagErrors.ERRORE_GENERIC_DATA));
    }
    catch (Exception e)
    {
    }
    temp = Validator.validateDateAfterToDay(dataFineDetenzione, "dataFineDetenzione", "Data fine detenzione", errors, false, false);
    if (temp != null)
    {
      // La data fine detenzione deve essere maggiore o uguale
      // alla data di sistema
      if (!DateUtils.isToday(temp))
      {
        Date today = new Date();
        if (today.after(temp))
          errors.add("dataFineDetenzione", new ValidationError(AnagErrors.ERRORE_DATA_MIN_TODAY));
      }
    }
    
    if (Validator.isNotEmpty(codiceFiscaleDetentore) 
        && Validator.isNotEmpty(codiceFiscaleProprietario))
    {
      if (this.isFlagSoccida())
      {
        //il codice fiscale e la denominazione del proprietario devono essere diversi da quelli del detentore. 
        //In caso contrario visualizzare il messaggio di errore sul detentore.
        if (codiceFiscaleDetentore.equalsIgnoreCase(codiceFiscaleProprietario))
        {
          if(Validator.isEmpty(motivoSoccida))
          {
            errors.add("motivoSoccida", new ValidationError(AnagErrors.ERR_MOTIVO_SOCCIDA_SI));
          }
          //errors.add("soccida", new ValidationError(AnagErrors.ERR_SOCCIDA_SEL));
        }
        else
        {
          if(Validator.isNotEmpty(motivoSoccida))
          {
            errors.add("motivoSoccida", new ValidationError(AnagErrors.ERR_MOTIVO_SOCCIDA_SI_NO_INS));
          }
        }
      }
      else
      {
        //il codice fiscale e la denominazione del proprietario devono essere uguali a quelli del detentore. 
        //In caso contrario visualizzare il messaggio di errore sul detentore.
        if (!codiceFiscaleDetentore.equalsIgnoreCase(codiceFiscaleProprietario))
        {
          if(Validator.isEmpty(motivoSoccida))
          {
            //errors.add("soccida", new ValidationError(AnagErrors.ERR_SOCCIDA_NON_SEL));
            errors.add("motivoSoccida", new ValidationError(AnagErrors.ERR_MOTIVO_SOCCIDA_NO));
          }
        }
        else
        {
          if(Validator.isNotEmpty(motivoSoccida))
          {
            errors.add("motivoSoccida", new ValidationError(AnagErrors.ERR_MOTIVO_SOCCIDA_NO_NO_INS));
        
          }
        }
      }
    }
    
    if (Validator.isEmpty(denominazioneProprietario))
    {
      errors.add("denominazioneProprietario", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    
    if (Validator.isEmpty(denominazioneDetentore))
    {
      errors.add("denominazioneDetentore", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    
    if (isStrutturaMungitura())
    {
      // devo controllare i dati alla struttura di mungitura che non sono obbligatori... però l'utente deve inserire tutti i dati
      // o nessuno
      boolean datiStrutMungIns=false;
      if (Validator.isNotEmpty(mediaCapiLattazione) 
          || Validator.isNotEmpty(idMungitura) 
          || Validator.isNotEmpty(quantitaAcquaLavaggio)
          || Validator.isNotEmpty(vAllevamentoAcquaLavaggio))
      {
        datiStrutMungIns=true;
      }
      if (datiStrutMungIns)
      {
        try
        {
          long tempLg = Long.parseLong(mediaCapiLattazione);
          if(numCapiMaxLattazione == 0)
          {
            errors.add("mediaCapiLattazione", new ValidationError("Il n. capi in lattazione non puo'' essere valorizzato, non sono state inserite categorie del tipo congruo"));
          }
          else
          {
            if (tempLg < 0 || tempLg > numCapiMaxLattazione)
            {
              errors.add("mediaCapiLattazione", new ValidationError("Il n. capi in lattazione deve essere un numero intero compreso tra 0 e "+numCapiMaxLattazione));
            }
          }
        }
        catch (Exception e)
        {
          errors.add("mediaCapiLattazione", new ValidationError("Il n. capi in lattazione deve essere un numero intero compreso tra 0 e 9999999"));
        }
        // Visibile ed obbligatorio se la specie dichiarata in
        // consistenza zootecnica è bovini
        if (AllevamentoAnagVO.ID_SPECIE_BOVINI_CARNE.equals(idSpecieAnimale) || AllevamentoAnagVO.ID_SPECIE_BOVINI_ALLEVAMENTO.equals(idSpecieAnimale))
        {
          if (!Validator.isNotEmpty(idMungitura))
            errors.add("idMungitura", new ValidationError("Selezionare la tipologia struttura mungitura"));
        }
  
        boolean flagAcquaLavaggio = true;
        try
        {
          double tempDb = Double.parseDouble(quantitaAcquaLavaggio.replace(',', '.'));
          if (tempDb <= 0 || tempDb > 999999.9)
          {
            errors.add("quantitaAcquaLavaggio", new ValidationError("Acque di lavaggio deve essere maggiore di 0 e minore o uguale a 999999,9999"));
            flagAcquaLavaggio = false;
          }
          else
          {
            String err = checkDecimals(tempDb, 1);
            if (err != null)
            {
              errors.add("quantitaAcquaLavaggio", new ValidationError(err));
              flagAcquaLavaggio = false;
            }
          }
        }
        catch (Exception e)
        {
          errors.add("quantitaAcquaLavaggio", new ValidationError("Acque di lavaggio deve essere maggiore di 0 e minore o uguale a 999999,9999"));
          flagAcquaLavaggio = false;
        }
        
        //se è ok acquaLavaggio
        if(flagAcquaLavaggio)
        {
          if(vAllevamentoAcquaLavaggio == null)
          {
            errors.add("quantitaAcquaLavaggio", new ValidationError("Deve essere selezionato almeno un valore di destino acqua di lavaggio"));
          }
          else
          {
            boolean errori = false;
            BigDecimal sommaAcque = new BigDecimal(0);
            for (int i = 0; i < vAllevamentoAcquaLavaggio.size(); i++)
            {
              AllevamentoAcquaLavaggio allAcquaLav = vAllevamentoAcquaLavaggio.get(i);                           
              if(allAcquaLav.validateInsert())
                errori = true;
              else
                sommaAcque = sommaAcque.add(allAcquaLav.getQuantitaAcquaLavaggio());
            }
            // Se ho trovato almeno un errore che viene visualizzato vicino al pulsante calcola
            if (errori)
              errors.add("Fittizia", new ValidationError("Fittizia"));
            else
            {
              if(sommaAcque.compareTo(new BigDecimal(quantitaAcquaLavaggio.replace(",", "."))) != 0)
              {
                errors.add("quantitaAcquaLavaggio", new ValidationError("le Acque di lavaggio totali non coincidono con la somma dei volumi indicati per i singoli destini delle acque di lavaggio"));
              }
              
            }
              
            
          }
          
        }
        
        
        
      }
    }
    

    try
    {
      // superficieLettieraPermanente non è obbligatorio, quindi eseguo il
      // controllo
      // di correttezza solo se è stato inserito
      if (Validator.isNotEmpty(superficieLettieraPermanente))
      {
        double tempDb = Double.parseDouble(superficieLettieraPermanente.replace(',', '.'));
        if (tempDb < 0 || tempDb > 999999.9)
          errors.add("superficieLettieraPermanente", new ValidationError("Superficie lettiera permanente (m2) deve essere compreso tra 0 e 999999,9999"));
        else
        {
          String err = checkDecimals(tempDb, 1);
          if (err != null)
            errors.add("superficieLettieraPermanente", new ValidationError(err));
        }
      }
      else
      {
        if(almenoUnaLettiera)
          errors.add("superficieLettieraPermanente", new ValidationError("Campo Obbligatorio: e' stata selezionata almeno una stabulazione con lettiera"));
      }
    }
    catch (Exception e)
    {
      errors.add("superficieLettieraPermanente", new ValidationError("Superficie lettiera permanente (m2) deve essere compreso tra 0 e 999999,9999"));
    }
    
    try
    {
      // altezzaLettieraPermanente Obbligatorio se viene impostata la superficie
      if (Validator.isNotEmpty(superficieLettieraPermanente))
      {
        double tempDb = Double.parseDouble(altezzaLettieraPermanente.replace(',', '.'));
        if (tempDb < altLettieraPermMin || tempDb > altLettieraPermMax)
          errors.add("altezza", new ValidationError("Altezza (m) deve essere compreso tra "+altLettieraPermMin +" e "+altLettieraPermMax));
        else
        {
          String err = checkDecimals(tempDb, 2);
          if (err != null)
            errors.add("altezza", new ValidationError(err));
        }
      }
    }
    catch (Exception e)
    {
      errors.add("altezza", new ValidationError("Altezza (m) deve essere compreso tra "+altLettieraPermMin +" e "+altLettieraPermMax));
    }

    return errors;
  }

  public ValidationErrors validateForInsert(ValidationErrors errors, 
      boolean isProprietario, boolean isDetentore)
  {

    /*
     * verifico che l'idUTE sia valorizzato siccome viene valorizzato in una
     * combo prendendo dati da una tabella di decodifica non vengono eseguiti
     * ulteriori controlli
     */
    if (!Validator.isNotEmpty(idUTE))
    {
      errors.add("idUTE", new ValidationError("Selezionare un UTE"));
    }

    // verifico la correttezza del Codice Azienda Zootecnica
    if (Validator.isNotEmpty(codiceAziendaZootecnica))
    {
      if (codiceAziendaZootecnica.length() != 8)
      {
        errors.add("codiceAziendaZootecnica", new ValidationError("Il Codice Azienda Zootecnica deve essere lunga 8 caratteri"));
      }
      else
      {
        // il codice deve essere alfanumerico
        if (!Validator.isAlphaNumeric(codiceAziendaZootecnica.trim()))
        {
          errors.add("codiceAziendaZootecnica", new ValidationError("Il Codice Azienda Zootecnica deve essere una stringa alfanumerica"));
        }
        else
        {
          // gli ultimi tre caratteri devono avere un valore compreso tra i due seguenti intervalli
          if (!Validator.isNumericInteger(codiceAziendaZootecnica.substring(5)))
          {
            // A01 => A99
            if (!codiceAziendaZootecnica.substring(5, 6).equalsIgnoreCase("A") 
                || !Validator.isNumericInteger(codiceAziendaZootecnica.substring(6)))
            {
              errors.add("codiceAziendaZootecnica", new ValidationError("Il formato del Codice Azienda Zootecnica è errato"));
            }
            else
            {
              // 01 => 99
              if (new Integer(codiceAziendaZootecnica.substring(6)).intValue() == 0)
              {
                errors.add("codiceAziendaZootecnica", new ValidationError("Il formato del Codice Azienda Zootecnica è errato"));
              }
            }
          }
          else
          {
            // 001 => 999
            if (new Integer(codiceAziendaZootecnica.substring(5)).intValue() == 0)
            {
              errors.add("codiceAziendaZootecnica", new ValidationError("Il formato del Codice Azienda Zootecnica è errato"));
            }
          }
        }
      }
    }

    // Controllo che l'indirizzo sia valorizzato
    if (!Validator.isNotEmpty(indirizzo))
    {
      errors.add("indirizzo", new ValidationError((String) AnagErrors.get("ERR_INDIRIZZO_OBBLIGATORIO")));
    }

    // controllo che il comune di ubicazione sia stato valorizzato, e che sia
    // conforme con il Codice Azienda Zootecnica
    if (!Validator.isNotEmpty(this.getIstatComuneAllevamento()))
    {
      errors.add("descComune", new ValidationError("Il comune di ubicazione dell''allevamento è obbligatorio"));
    }
    else
    {
      if (Validator.isNotEmpty(this.getIstatComuneAllevamento()))
      {
        if (Validator.isNotEmpty(codiceAziendaZootecnica) && codiceAziendaZootecnica.length() == 8)
        {
          if (!codiceAziendaZootecnica.substring(0, 3).equals(istatComuneAllevamento.substring(3)))
          { // i primi tre caratteri devono corrispondere all'istat comune
            // dell'allevamento
            errors.add("descComune", new ValidationError("Il comune selezionato non è coerente con il Codice Azienda Zootecnica"));
          }
        }
      }
    }
    // controllo che la provincia di ubicazione sia stata valorizzata, e che sia
    // conforme con il Codice Azienda Zootecnica
    if (!Validator.isNotEmpty(this.getCodiceProvinciaAllevamento()))
    {
      errors.add("provincia", new ValidationError("La provincia di ubicazione dell''allevamento è obbligatoria"));
    }
    else
    {
      if (Validator.isNotEmpty(this.getCodiceAziendaZootecnica()) && codiceAziendaZootecnica.length() == 8)
      {

        if (!codiceAziendaZootecnica.substring(3, 5).equals(this.getCodiceProvinciaAllevamento()))
        {
          errors.add("codiceAziendaZootecnica", new ValidationError("Il Codice Azienda Zootecnica non è coerente i dati inseriti nella sezione Ubicazione"));
        }
      }
    }
    
    
    Validator.validateDateAll(dataInizioAttivita, "dataInizioAttivita", "data apertura allevamento", errors, true, true);
    
    
    
    
    if(Validator.isNotEmpty(latitudineStr))
    {
      if(Validator.validateDouble(latitudineStr, 99.999999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "latitudine", AnagErrors.ERRORE_CAMPO_ERRATO_COORD);
      }
      else if(Double.parseDouble(Validator.validateDouble(latitudineStr, 99.999999)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "latitudine", AnagErrors.ERRORE_CAMPO_ERRATO_COORD);
      }
      else 
      {
        latitudineStr = latitudineStr.replace(",", ".");
        latitudine = new BigDecimal(latitudineStr);
      }
    }
    else
    {
      latitudine = null;
    }
    
    if(Validator.isNotEmpty(longitudineStr))
    {
      if(Validator.validateDouble(longitudineStr, 99.999999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "longitudine", AnagErrors.ERRORE_CAMPO_ERRATO_COORD);
      }
      else if(Double.parseDouble(Validator.validateDouble(longitudineStr, 99.999999)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "longitudine", AnagErrors.ERRORE_CAMPO_ERRATO_COORD);
      }
      else 
      {
        longitudineStr = longitudineStr.replace(",", ".");
        longitudine = new BigDecimal(longitudineStr);
      }
    }
    else
    {
      longitudine = null;
    }
    
    
    // Controllo che il CAP sia valorizzato
    if (!Validator.isNotEmpty(cap))
    {
      errors.add("cap", new ValidationError((String) AnagErrors.get("ERR_CAP_OBBLIGATORIO")));
    }
    // Se lo è controllo che sia un numerico intero positivo di 5 cifre
    else
    {
      if (!Validator.isNumericInteger(cap) || cap.length() != 5)
      {
        errors.add("cap", new ValidationError((String) AnagErrors.get("ERR_CAP_ERRATO")));
      }
    }
    
    
    if (Validator.isEmpty(idTipoProduzione))
    {
      errors.add("idTipoProduzione", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    
    if (Validator.isEmpty(idOrientamentoProduttivo))
    {
      errors.add("idOrientamentoProduttivo", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
    }
    
    
    
    
    /*
     * controllo che la specie animale sia stata valorizzata. siccome viene
     * valorizzata in una combo prendendo dati da una tabella di decodifica non
     * vengono eseguiti controlli
     */
    boolean almenoUnaLettiera = false;
    if (!Validator.isNotEmpty(idSpecieAnimale))
    {
      errors.add("idSpecie", new ValidationError("La specie animale è obbligatoria"));
    }
    // verifico se almeno è stata inserita almeno una categoria con quantità
    // maggiore di 0
    else
    {
      if (categorieAllevamentoVector == null || categorieAllevamentoVector.size() == 0)
      {
        errors.add("idSpecie", new ValidationError("E'' necessario inserire almeno una categoria animale con quantità maggiore di 0"));
      }
      else
      {
        int size = categorieAllevamentoVector.size();

        // Controllo i valori inseriti
        boolean errori = false;
        for (int i = 0; i < size; i++)
        {
          SottoCategoriaAllevamento sottoCat = (SottoCategoriaAllevamento) categorieAllevamentoVector.get(i);
          
          if (Validator.isNotEmpty(codiceFiscaleDetentore)  
            && Validator.isNotEmpty(codiceFiscaleProprietario))
          {
            boolean ugualiPropDet = false;  
            if(isProprietario && isDetentore)
            {
              ugualiPropDet = true;
            }
          
            if (sottoCat.validateInsert(ugualiPropDet, isProprietario))
             errori = true;
          }
          else
          {
            if (sottoCat.validateInsert())
              errori = true;
          }
        }
        
        
        //controllo gli errori delle stabulazioni solo se sono state inserite,altrimenti no dato che non è obbligatorio
        if (stabulazioniTrattamenti!=null && stabulazioniTrattamenti.size()!=0)
        {
          int sizeStab = stabulazioniTrattamenti.size();
          //boolean altriTrattamenti=false;
          for (int i = 0; i < sizeStab; i++)
          {
            StabulazioneTrattamento stab = (StabulazioneTrattamento) stabulazioniTrattamenti.get(i);
            //if (!stab.isFlagCalcolo()) altriTrattamenti=true;
            if("S".equalsIgnoreCase(stab.getLettieraPermanente()))
            {
              almenoUnaLettiera = true;                 
            }
            
            
            if (stab.validateInsert()) errori = true;
          }
         
        }
        // Se ho trovato almeno un errore che viene visualizzato vicino al pulsante calcola
        if (errori)
          errors.add("Fittizia", new ValidationError("Fittizia"));
        else
        {
          if (stabulazioniTrattamenti!=null  && stabulazioniTrattamenti.size()!=0)
          {
            boolean erroriQuantitaTot=false;
            //Devo controllare che per ogni consistenza inserita ci siano una o più stabulazione legate 
            //e che la somma delle quantità delle stabulazioni (per una consistenza) sia uguale alla 
            //quantita definita nella consistenza
            for (int i = 0; i < size; i++)
            {
              SottoCategoriaAllevamento sottoCat = (SottoCategoriaAllevamento) categorieAllevamentoVector.get(i);
              long quantitaConsistenza=Long.parseLong(sottoCat.getQuantita());
              long idSottoCategoria=sottoCat.getIdSottoCategoriaAnimale();
              
              //scorro le stabulazioni
              int sizeStab = stabulazioniTrattamenti.size();
              long quantitaStabulazione=0;
              for (int j = 0; j < sizeStab; j++)
              {
                StabulazioneTrattamento stab = (StabulazioneTrattamento) stabulazioniTrattamenti.get(j);
                if ((idSottoCategoria+"").equals(stab.getIdSottoCategoriaAnimale()))
                {
                  quantitaStabulazione+=Long.parseLong(stab.getQuantita());
                  stab.setSottoCatAll(sottoCat);
                }
              }
              if (quantitaConsistenza!=quantitaStabulazione)
              {
                errors.add("calcolaStabulazione", new ValidationError("Il numero di capi stabulati e le Sottocategorie indicate devono corrispondere con quelle dichiarate nella sezione Consistenza"));
                erroriQuantitaTot=true;
                break;
              }
            }
            if (!erroriQuantitaTot)
            {
              //Se non ci sono errori procedo al controllo dei campi palabile e non palabile
              int sizeStab = stabulazioniTrattamenti.size();
              for (int i = 0; i < sizeStab; i++)
              {
                StabulazioneTrattamento stab = (StabulazioneTrattamento) stabulazioniTrattamenti.get(i);
                //effettuo i calcoli
                
                //String totAzotoOld=stab.getTotaleAzoto();
                
                stab.effettuaCalcoli();
                stab.effettuaCalcoliEfflPreTratt();
                //stab.effettuaCalcoliEfflPostTratt();
                
                //if (!stab.isFlagCalcolo()) stab.setTotaleAzoto(totAzotoOld);
                
                if (stab.validateVolumiInsert()) errori = true;
              }
              // Se ho trovato almeno un errore che viene visualizzato vicino al pulsante calcola
              if (errori)
                errors.add("Fittizia", new ValidationError("Fittizia"));
            }
          }
        }
      }
    }

    // verifico che le note, se valorizzate, non siano più lunghe di 300
    // caratteri
    if (Validator.isNotEmpty(note) && note.length() > 300)
    {
      errors.add("note", new ValidationError("Le note non possono essere più lunghe di 300 caratteri"));
    }

    Validator.validateDateAll(dataInizio, "dal", "data inizio", errors, true, true);    
    
    if (Validator.isNotEmpty(codiceFiscaleProprietario))
    {
      // controllo uno dei due campi, se è valorizzato
      // significa che lo sono tutti e due
      if (Validator.isNotEmpty(codiceFiscaleProprietario))
      {
        codiceFiscaleProprietario = codiceFiscaleProprietario.trim();
        if (codiceFiscaleProprietario.length() == 16)
        {
          // Controllo la correttezza del codice fiscale
          if (!Validator.controlloCf(codiceFiscaleProprietario))
          {
            errors.add("codiceFiscaleProprietario", new ValidationError(AnagErrors.ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA));
          }
        }
        else
        {
          // Controllo la correttezza della partita iva
          if (!Validator.controlloPIVA(codiceFiscaleProprietario))
          {
            errors.add("codiceFiscaleProprietario", new ValidationError(AnagErrors.ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA));
          }
        }
      }
    }
    else
    {
      errors.add("codiceFiscaleProprietario", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));    
    }
    
    if (Validator.isNotEmpty(codiceFiscaleDetentore))
    {
      // controllo uno dei due campi, se è valorizzato
      // significa che lo sono tutti e due
      if (Validator.isNotEmpty(codiceFiscaleDetentore))
      {
        codiceFiscaleDetentore = codiceFiscaleDetentore.trim();
        if (codiceFiscaleDetentore.length() == 16)
        {
          // Controllo la correttezza del codice fiscale
          if (!Validator.controlloCf(codiceFiscaleDetentore))
          {
            errors.add("codiceFiscaleDetentore", new ValidationError(AnagErrors.ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA));
          }
        }
        else
        {
          // Controllo la correttezza della partita iva
          if (!Validator.controlloPIVA(codiceFiscaleDetentore))
          {
            errors.add("codiceFiscaleDetentore", new ValidationError(AnagErrors.ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA));
          }
        }
      }
    }
    else
    {
      errors.add("codiceFiscaleDetentore", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));    
    }
    
    if(!isProprietario && !isDetentore)
    {
      errors.add("codiceFiscaleDetentore", new ValidationError(AnagErrors.ERRORE_NO_DET_PROP));    
      errors.add("codiceFiscaleProprietario", new ValidationError(AnagErrors.ERRORE_NO_DET_PROP));
    }
    
    
    // Controllo la correttezza della dataInizioDetenzione
    Date temp = Validator.validateDateAll(dataInizioDetenzione, "dataInizioDetenzione", "Data inizio detenzione", errors, true, true);
    try
    {
      if (temp != null && temp.before(DateUtils.parseDate("01/01/1900")))
        errors.add("dataInizioDetenzione", new ValidationError(AnagErrors.ERRORE_GENERIC_DATA));
    }
    catch (Exception e)
    {
    }
    temp = Validator.validateDateAfterToDay(dataFineDetenzione, "dataFineDetenzione", "Data fine detenzione", errors, false, false);
    if (temp != null)
    {
      // La data fine detenzione deve essere maggiore o uguale
      // alla data di sistema
      if (!DateUtils.isToday(temp))
      {
        Date today = new Date();
        if (today.after(temp))
          errors.add("dataFineDetenzione", new ValidationError(AnagErrors.ERRORE_DATA_MIN_TODAY));
      }
    }
    
    if (Validator.isNotEmpty(codiceFiscaleDetentore) 
      && Validator.isNotEmpty(codiceFiscaleProprietario))
    {
      if (this.isFlagSoccida())
      {
        //il codice fiscale e la denominazione del proprietario devono essere diversi da quelli del detentore. 
        //In caso contrario visualizzare il messaggio di errore sul detentore.
        if (codiceFiscaleDetentore.equalsIgnoreCase(codiceFiscaleProprietario))
        {
          if(Validator.isEmpty(motivoSoccida))
          {
            //errors.add("soccida", new ValidationError(AnagErrors.ERR_SOCCIDA_SEL));
            errors.add("motivoSoccida", new ValidationError(AnagErrors.ERR_MOTIVO_SOCCIDA_SI));
          }          
        }
        else
        {
          if(Validator.isNotEmpty(motivoSoccida))
          {
            errors.add("motivoSoccida", new ValidationError(AnagErrors.ERR_MOTIVO_SOCCIDA_SI_NO_INS));
          }
        }
      }
      else
      {
        //il codice fiscale e la denominazione del proprietario devono essere uguali a quelli del detentore. 
        //In caso contrario visualizzare il messaggio di errore sul detentore.
        if (!codiceFiscaleDetentore.equalsIgnoreCase(codiceFiscaleProprietario))
        {
          if(Validator.isEmpty(motivoSoccida))
          {
            //errors.add("soccida", new ValidationError(AnagErrors.ERR_SOCCIDA_NON_SEL));
            errors.add("motivoSoccida", new ValidationError(AnagErrors.ERR_MOTIVO_SOCCIDA_NO));
          }
        }
        else
        {
          if(Validator.isNotEmpty(motivoSoccida))
          {
            errors.add("motivoSoccida", new ValidationError(AnagErrors.ERR_MOTIVO_SOCCIDA_NO_NO_INS));
          }
        }
      }
    }
    
    if (Validator.isEmpty(denominazioneProprietario))
    {
      errors.add("denominazioneProprietario", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    
    if (Validator.isEmpty(denominazioneDetentore))
    {
      errors.add("denominazioneDetentore", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }

    
    try
    {
      // superficieLettieraPermanente non è obbligatorio, quindi eseguo il
      // controllo
      // di correttezza solo se è stato inserito
      if (Validator.isNotEmpty(superficieLettieraPermanente))
      {
        double tempDb = Double.parseDouble(superficieLettieraPermanente.replace(',', '.'));
        if (tempDb < 0 || tempDb > 999999.9999)
          errors.add("superficieLettieraPermanente", new ValidationError("Superficie lettiera permanente (m2) deve essere compreso tra 0 e 999999,9999"));
        else
        {
          String err = checkDecimals(tempDb, 4);
          if (err != null)
            errors.add("superficieLettieraPermanente", new ValidationError(err));
        }
      }
      else
      {
        if(almenoUnaLettiera)
        {
          errors.add("superficieLettieraPermanente", new ValidationError("Campo Obbligatorio: e' stata selezionata almeno una stabulazione con lettiera"));
        }
      }
    }
    catch (Exception e)
    {
      errors.add("superficieLettieraPermanente", new ValidationError("Superficie lettiera permanente (m2) deve essere compreso tra 0 e 999999,9999"));
    }
    
    try
    {
      // altezzaLettieraPermanente Obbligatorio se viene impostata la superficie
      if (Validator.isNotEmpty(superficieLettieraPermanente))
      {
        double tempDb = Double.parseDouble(altezzaLettieraPermanente.replace(',', '.'));
        if (tempDb < altLettieraPermMin || tempDb > altLettieraPermMax)
          errors.add("altezza", new ValidationError("Altezza (m) deve essere compreso tra "+altLettieraPermMin +" e "+altLettieraPermMax));
        else
        {
          String err = checkDecimals(tempDb, 2);
          if (err != null)
            errors.add("altezza", new ValidationError(err));
        }
      }
    }
    catch (Exception e)
    {
      errors.add("altezza", new ValidationError("Altezza (m) deve essere compreso tra "+altLettieraPermMin +" e "+altLettieraPermMax));
    }

    return errors;
  }

  // Viene usato per validare i dati necessari ad eseguire i calcoli
  public ValidationErrors validateCalcoli(ValidationErrors errors, boolean ugualiDetProp, 
      boolean isProprietario)
  {
    // verifico se almeno è stata inserita almeno una categoria con quantità
    // maggiore di 0
    if (categorieAllevamentoVector == null || categorieAllevamentoVector.size() == 0)
    {
      errors.add("idSpecie", new ValidationError("E'' necessario inserire almeno una categoria animale con quantità maggiore di 0"));
    }
    else
    {
      int size = categorieAllevamentoVector.size();

      // Controllo i valori inseriti
      boolean errori = false;
      for (int i = 0; i < size; i++)
      {
        SottoCategoriaAllevamento sottoCat = (SottoCategoriaAllevamento) categorieAllevamentoVector.get(i);
        
        
        if (Validator.isNotEmpty(codiceFiscaleDetentore)  
            && Validator.isNotEmpty(codiceFiscaleProprietario))
        {
          if (sottoCat.validateInsert(ugualiDetProp, isProprietario))
            errori = true;
        }
        else
        {
          if (sottoCat.validateInsert())
            errori = true;
        }
        
        
        //if (sottoCat.validateInsert())
          //errori = true;
      }
      //controllo gli errori delle stabulazioni
      if (stabulazioniTrattamenti!=null)
      {
        int sizeStab = stabulazioniTrattamenti.size();
        for (int i = 0; i < sizeStab; i++)
        {
          StabulazioneTrattamento stab = (StabulazioneTrattamento) stabulazioniTrattamenti.get(i);
          if (stab.validateInsert()) errori = true;
        }
      }
      // Se ho trovato almeno un errore che viene visualizzato vicino al pulsante calcola
      if (errori)
        errors.add("calcolaStabulazione", new ValidationError("Non è stato possibile effettuare il calcolo in quanto uno o più valori inseriti non sono corretti"));
      else
      {
        if (stabulazioniTrattamenti!=null)
        {
          //Devo controllare che per ogni consistenza inserita ci siano una o più stabulazione legate 
          //e che la somma delle quantità delle stabulazioni (per una consistenza) sia uguale alla 
          //quantita definita nella consistenza
          for (int i = 0; i < size; i++)
          {
            SottoCategoriaAllevamento sottoCat = (SottoCategoriaAllevamento) categorieAllevamentoVector.get(i);
            long quantitaConsistenza=Long.parseLong(sottoCat.getQuantita());
            long idSottoCategoria=sottoCat.getIdSottoCategoriaAnimale();
            
            //scorro le stabulazioni
            int sizeStab = stabulazioniTrattamenti.size();
            long quantitaStabulazione=0;
            for (int j = 0; j < sizeStab; j++)
            {
              StabulazioneTrattamento stab = (StabulazioneTrattamento) stabulazioniTrattamenti.get(j);
              if ((idSottoCategoria+"").equals(stab.getIdSottoCategoriaAnimale()))
              {
                quantitaStabulazione+=Long.parseLong(stab.getQuantita());
                stab.setSottoCatAll(sottoCat);
              }
            }
            if (quantitaConsistenza!=quantitaStabulazione)
            {
              errors.add("calcolaStabulazione", new ValidationError("Il numero di capi stabulati e le Sottocategorie indicate devono corrispondere con quelle dichiarate nella sezione Consistenza dei capi in detenzione."));
              return errors;
            }
          }
        }
      }
    }

    return errors;
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

  public String getBiologico()
  {
    return biologico;
  }

  public void setBiologico(String biologico)
  {
    this.biologico = biologico;
  }

  public String getDenominazione()
  {
    return denominazione;
  }

  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }

  public BigDecimal getLongitudine()
  {
    return longitudine;
  }

  public void setLongitudine(BigDecimal longitudine)
  {
    this.longitudine = longitudine;
  }

  public BigDecimal getLatitudine()
  {
    return latitudine;
  }

  public void setLatitudine(BigDecimal latitudine)
  {
    this.latitudine = latitudine;
  }

  public String getDataInizioAttivita()
  {
    return dataInizioAttivita;
  }

  public void setDataInizioAttivita(String dataInizioAttivita)
  {
    this.dataInizioAttivita = dataInizioAttivita;
  }

  public String getLongitudineStr()
  {
    return longitudineStr;
  }

  public void setLongitudineStr(String longitudineStr)
  {
    this.longitudineStr = longitudineStr;
  }

  public String getLatitudineStr()
  {
    return latitudineStr;
  }

  public void setLatitudineStr(String latitudineStr)
  {
    this.latitudineStr = latitudineStr;
  }

  public String getMotivoSoccida()
  {
    return motivoSoccida;
  }

  public void setMotivoSoccida(String motivoSoccida)
  {
    this.motivoSoccida = motivoSoccida;
  }

  public Vector<AllevamentoAcquaLavaggio> getvAllevamentoAcquaLavaggio()
  {
    return vAllevamentoAcquaLavaggio;
  }

  public void setvAllevamentoAcquaLavaggio(
      Vector<AllevamentoAcquaLavaggio> vAllevamentoAcquaLavaggio)
  {
    this.vAllevamentoAcquaLavaggio = vAllevamentoAcquaLavaggio;
  }

  public ComuneVO getComuneUteVO()
  {
    return comuneUteVO;
  }

  public void setComuneUteVO(ComuneVO comuneUteVO)
  {
    this.comuneUteVO = comuneUteVO;
  }

  public String getIndirizzoUte()
  {
    return indirizzoUte;
  }

  public void setIndirizzoUte(String indirizzoUte)
  {
    this.indirizzoUte = indirizzoUte;
  }

  public String getSommaQuantitaProprietario()
  {
    return sommaQuantitaProprietario;
  }

  public void setSommaQuantitaProprietario(String sommaQuantitaProprietario)
  {
    this.sommaQuantitaProprietario = sommaQuantitaProprietario;
  }

  public String getIdTipoProduzioneCosman()
  {
    return idTipoProduzioneCosman;
  }

  public void setIdTipoProduzioneCosman(String idTipoProduzioneCosman)
  {
    this.idTipoProduzioneCosman = idTipoProduzioneCosman;
  }

  public String getDescTipoProduzioneCosman()
  {
    return descTipoProduzioneCosman;
  }

  public void setDescTipoProduzioneCosman(String descTipoProduzioneCosman)
  {
    this.descTipoProduzioneCosman = descTipoProduzioneCosman;
  }

  public String getFlagAssicuratoCosman() {
		return flagAssicuratoCosman;
	}

	public void setFlagAssicuratoCosman(String flagAssicuratoCosman) {
		this.flagAssicuratoCosman = flagAssicuratoCosman;
	}

	public String getEsitoControllo()
  {
    return esitoControllo;
  }

  public void setEsitoControllo(String esitoControllo)
  {
    this.esitoControllo = esitoControllo;
  }

  
}
