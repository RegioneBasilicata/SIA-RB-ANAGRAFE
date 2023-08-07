package it.csi.solmr.etc;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */

public abstract class SolmrErrors extends AbstractSolmrEtc {
  protected static final String MY_RESOURCE_BUNDLE = "it.csi.solmr.etc.generalErrors";
  protected static final Class<?> THIS_CLASS = SolmrErrors.class;
  public static String EXC_BUSINESS;
  public static String EXC_DATA_ACCESS;
  public static String EXC_NOT_FOUND;
  public static String EXC_NOT_FOUND_PK;
  public static String EXC_RESOURCE_ACCESS;
  public static String EXC_AUTHORIZATION;
  public static String GENERIC_SYSTEM_EXCEPTION;
  public static String DUPLICATED_ACCESS_EXCEPTION;
  public static String UTENTE_NON_ABILITATO;
  public static String PROFILO_MULTIPLO_IN_TABELLA_PROFILI;
  public static String PROFILO_UTENTE_SCONOSCIUTO;
  public static String AZIENDA_AGRICOLA_SCONOSCIUTA;
  public static String INTERMEDIARIO_SCONOSCIUTO;
  public static String PROVINCIA_SCONOSCIUTA;
  public static String EXC_CONF_FILE_NF;
  public static String EXC_DESCOM_VOID;
  public static String EXC_TROPPE_AZIENDE_ATTIVE;
  public static String EXC_TROPPI_RECORD_SELEZIONATI;
  public static String ERR_PROVINCIA_INESISTENTE;
  public static String DATI_AZIENDA_NON_REPERIBILI;

  public static Object get(String key) {
    return get(THIS_CLASS, key);
  }

  static {
    initClass(THIS_CLASS, MY_RESOURCE_BUNDLE);
  }
}
