package it.csi.solmr.util;

import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpSession;

public class WebUtils {
  //private static final String PROFILE = "profile";
  private static final String RUOLO_UTENZA = "ruoloUtenza";
  private static final String PATH_TO_FOLLOW = "pathToFollow";
  private static final String ANAG_ACCESS = "anagAccess";
  private static final String UMA_ACCESS = "umaAccess";
  private static final String POINT_TO = "pointTo";
  private static final String IRIDE2_ABILITAZIONI = "iride2AbilitazioniVO";
  private static final String USER = "userid";
  private static final String PASSWORD = "password";
  private static final String CERTIFICATO = "certificato";
  private static final String CERT_ACCESS_POINT = "certAccessPoint";
  private static final String IDENTITA = "identita";
  private static final String ELENCO_CUAA_TITOLARE = "elencoCUAATitolare";
  private static final String PORTAL_NAME = "PORTAL_NAME";
  private static final String STATTRACE_PARAM = "STATTRACE_PARAM";
  private static final String STATTRACE_PORTALE = "STATTRACE_PORTALE";
  private static final String HIRIDE2ABILITAZIONI = "hIride2Abilitazioni";
  private static final String DIRITTORW = "dirittoRW";
  private static final String VACTOR = "vActor";
  private static final String UTENTE_ABILITAZIONI = "utenteAbilitazioni";
  
  private static final String IDAZIENDA_ELENCO_SOCI = "idAziendaElencoSoci";
  
  //Filtri
  private static final String PARTICELLAREVO =  "filtriParticellareRicercaVO";
  private static final String DOCUMENTORICERCAVO = "documentoRicercaVO";
  private static final String IDPIANORIFERIMENTOFABBRICATO = "idPianoRiferimentoFabbricato";
  private static final String IDPIANORIFERIMENTOMANODOPERA = "idPianoRiferimentoManodopera";
  private static final String UNITAARBOREARICERCAVO = "filtriUnitaArboreaRicercaVO";
  private static final String PRATICHEANNICOMBO = "filtriPraticheAnniCombo"; //Pratiche
  private static final String PRATICHEPROCEDIMENTI = "filtriPraticheProcedimenti"; //Pratiche
  private static final String IDAZIENDAPRATICHE = "idAziendaPratiche"; //Pratiche
  private static final String PIANORIFERIMENTALLEVAMENTO = "idPianoRiferimentoAllevamento";
  private static final String ELENCOAZIENDELINK   = "elencoAziendeLink"; //Aziende collegate
  private static final String AZIENDACOLLEGATACORRENTE = "aziendaCollegataCorrente"; //Aziende collegate
  private static final String ELENCOIDAZIENDECOLLEGATE = "elencoIdAziendeCollegate"; //Aziende collegate
  private static final String VAZIENDAINS = "vAziendaIns"; //Aziende collegate
  private static final String LEVELAZIENDACOLLEGATA = "levelAziendaCollegata"; //Aziende Collegate
  private static final String ELENCOIDAZIENDACOLLEGATAAZCOLLALL = "elencoIdAziendaCollegataAzCollAll"; //Aziende Collegate
  private static final String ELENCODATAINGRESSOAZCOLLALL = "elencoDataIngressoAzCollAll"; //Aziende Collegate
  private static final String ELENCODATAUSCITAAZCOLLALL = "elencoDataUscitaAzCollAll"; //Aziende Collegate
  private static final String ELENCOCOLLEGATE = "elencoCollegate"; //Aziende Collegate
  private static final String FLAGSTORICOAZCOLL = "flagStoricoAzColl"; //Aziende collegate
  private static final String FILTRIRICERCAAZIENDECOLLEGATE = "filtriRicercaAziendeCollegateVO"; //Aziende collegate
  private static final String ELENCOIDAZIENDECOLLEGATEEXCEL = "elencoIdAziendeCollegateExcel"; //Aziende collegate
  private static final String TOTSUPAZIENDECOLLEGATE = "totSupAziendeCollegate"; //Aziende Collegate
  private static final String SBLOCCASOCIINSAMANO = "sbloccaSociInsAMano"; //Aziende Collegate
  private static final String SIANTERRENIVO = "sianTerritorioVO"; //Terreni/SIAN
  private static final String ELENCO_REGISTRO_STALLA = "elencoRegistroStalla"; //Allevamenti/Registro di stalla
  private static final String IDALLEVAMENTO_REGISTRO_STALLA = "idAllevamentoRegistroStalla"; //Allevamenti/Registro di stalla
  private static final String MOD_ANAGAZIENDAVO = "modAnagAziendaVO"; //Anagrafica/dichiarazione insedimaneto
  private static final String MOD_PERSONAVO = "modPersonaVO"; //Anagrafica/dichiarazione insedimaneto
  private static final String ANOMALIE_DICHIARAZIONE = "anomalieDichiarazione"; //Anagrafica/dichiarazione insedimaneto
  private static final String DESCRIZIONE_SPECIE = "descrizioneSpecie"; //Allevamenti/Registro di stalla
  private static final String IDINTERMEDIARIO_1 = "idIntermediario_1"; //servizi/trasferimentoUfficio
  private static final String IDINTERMEDIARIO_2 = "idIntermediario_2"; //servizi/trasferimentoUfficio
  private static final String CUAATRASFERIMENTOUFFICIO = "cuaaTrasferimentoUfficio";  //servizi/trasferimentoUfficio
  private static final String UV_RESPONSE_CCIAA = "uvResponseCCIAA";  //terreni/unità vitate/albo vigneti
  private static final String SOTTO_CATEGORIE_ALLEVAMENTI = "sottoCategorieAllevamenti";  //alevamenti/inserisci e modifica
  private static final String STABULAZIONI_TRATTAMENTI = "stabulazioniTrattamenti";  //alevamenti/inserisci e modifica
  private static final String ALLEVAMENTO_MOD = "allevamentoMod";  //alevamenti/modifica
  private static final String ID_UNITA_CCIAA = "idUnitaCCIAA";  //terreni/unità vitate/albo vigneti
  private static final String STORICO_PARTICELLAVO_CCIAA = "storicoParticellaVOCCIAA";  //terreni/unità vitate/albo vigneti
  private static final String ELENCOIDPARTICELLEIMPORTAASSERVIMENTO = "elencoIdParticelleImportaAsservimento";  //terreni/importa asservimento
  private static final String ANAGAZIENDAVOIMPORTAASSERVIMENTO = "anagAziendaSearchVoImportaAsservimento";  //terreni/importa asservimento
  private static final String ELENCOIDPARTICELLAIMPORTAASSERVIMENTO = "elencoIdParticellaImportaAsservimento"; //terreni/importa asservimento
  private static final String IDUTEIMPORTAASSERVIMENTO = "idUteImportaAsservimento"; //terreni/importa asservimento
  private static final String RICERCAIMPORTAASSERVIMENTO = "ricercaImportaAsservimento"; //terreni/importa asservimento
  private static final String FILTRIRICERCATERRENOIMPORTAASSERVIMENTO = "filtriRicercaTerrenoImportaAsservimentoVO"; //terreni/importa asservimento
  private static final String VCESSIONIACQUISIZIONI = "vCessioniAcquisizioni"; //comunicazione10R
  private static final String VSTOCCAGGIO = "vStoccaggio"; //comunicazione10R
  private static final String VEFFLUENTI = "vEffluenti"; //comunicazione10R
  private static final String VACQUEREFLUE = "vAcqueReflue"; //comunicazione10R
  private static final String VVOLUMESOTTOGRIGLIATO = "vVolumeSottogrigliato"; //comunicazione10R
  private static final String VVOLUMEREFLUOAZIENDA = "vVolumeRefluoAzienda"; //comunicazione10R
  private static final String VCOM10R = "vCom10r"; //comunicazione10R
  private static final String ANOMALIEDICHIARAZIONICONSISTENZA = "anomalieDichiarazioniConsistenza"; //Controlli Terreni
  private static final String HFILTRIVERIFICATERRENI = "hFiltriVerificaTerreni"; //Controlli Terreni
  private static final String DOCUMENTO_VO = "documentoVO"; //Documenti
  private static final String ELENCO_PROPRIETARI = "elencoProprietari"; //Documenti
  private static final String ELENCO_PARTICELLE = "elencoParticelle"; //Documenti
  private static final String PARTICELLE_ASSOCIATE = "particelleAssociate"; //Documenti
  private static final String PARTICELLE_DA_ASSOCIARE = "particelleDaAssociare"; //Documenti
  private static final String ELENCO_TIPI_USO_SUOLO_PRIMARIO = "elencoTipiUsoSuoloPrimario"; //Documenti
  private static final String VAZIENDAATECOSEC = "vAziendaAtecoSec"; //modificaAzienda
  private static final String GESTOREFASCICOLOSIAN = "gestoreFascicoloSIAN"; //Gestore fascicolo
  private static final String ARRPAGAMENTOEROGATOVO = "arrPagamentoErogatoVO"; //Pratiche
  private static final String TTIPOLOGIAPRATICA = "tTipologiaPratica"; //Pratiche
  private static final String ARRRECUPEROPREGRESSOVO = "arrRecuperoPregressoVO"; //Pratiche
  private static final String FILTRI_RICERCA_VARIAZIONI_AZIENDALI = "filtriRicercaVariazioniAziendaliVO"; //Variazioni
  private static final String NUM_VARIAZIONI_SELEZIONATE = "numVariazioniSelezionate"; //Variazioni
  private static final String RITORNOPRATICHECCAGRISERVVO = "ritornoPraticheCCAgriservVO"; //Conti correnti
  private static final String UVRESPONSECCIAA = "uvResponseCCIAA"; //Albo CCIAA
  private static final String UVIMPORTADACCIAA = "uvImportaDaCCIAA"; //Albo CCIAA
  private static final String IDDOCUMENTOPROTOCOLLO = "idDocumentoStampaProtocollo"; //Documenti
  private static final String VIDISOLAPARCELLA = "vIdIsolaParcella"; //Alliena UV a gis
  private static final String IDDICHCONSALLINEAUVLG = "idDichConsAllineaUVLg"; //Alliena UV a gis
  private static final String ELENCOIDALLINEAUV = "elencoIDAllineaUV"; //Allinea piano colturale
  private static final String VELENCOFILEALLEGATI ="vElencoFileAllegati"; //allega file
  private static final String ALLEGATODOCUMENTOVO ="allegatoDocumentoVO"; //allega file
  private static final String SCHEDECREDITO = "schedeCredito"; //Pratiche
  private static final String ELENCONOTIFICHE = "elencoNotifiche"; //Notifiche
  private static final String NOTIFICAMODIFICAVO = "notificaModificaVO"; //Notifiche
  private static final String ELENCOIDALLINEAPERCUSOELEGG = "elencoIDAllineaPercUsoElegg"; //Allinea % uso
  private static final String CUAANUOVAISCRIZIONE = "cuaaNuovaIscrizione"; //nuovaIscrizione
  private static final String IDAZIENDANUOVA = "idAziendaNuova"; //nuovaIscrizione
  private static final String VUTEAZIENDANUOVA = "vUteAziendaNuova"; //nuovaIscrizione
  private static final String VFABBRAZIENDANUOVA = "vFabbrAziendaNuova"; //nuovaIscrizione
  private static final String VPARTFABBRAZIENDANUOVA = "vPartFabbrAziendaNuova"; //nuovaIscrizione
  private static final String VPARTICELLEAZIENDANUOVA = "vParticelleAziendaNuova"; //nuovaIscrizione
  private static final String VALLEVAMENTIAZIENDANUOVA = "vAllevamentiAziendaNuova"; //nuovaIscrizione
  private static final String ELENCOALLEVAMENTIBDN = "elencoAllevamentiBdn"; //nuovaIscrizione
  private static final String VCCAZIENDANUOVA = "vCCAziendaNuova"; //nuovaIscrizione
  private static final String FILTRO_POLIZZA = "filtroPolizzaVO"; //Polizze
  private static final String TITOLARITA_SIGMATER = "titolarita"; //Sigmater
  private static final String PAGINACORINDEX = "paginaCorIndex"; //dematerializzazione
  private static final String DOCUMENTOALLEGATOVO = "documentoAllegatoVO"; //documenti
  private static final String FILTRIRICERCAMACCHINEAGRICOLEVO = "filtriRicercaMacchineAgricoleVO"; //Macchine agricole
  private static final String SIANFASCICOLORESPONSEVO = "sianFascicoloResponseVO";  //fonti certificate/Sian
  private static final String VATECOSECUTE = "vAtecoSecUte";  //ute/inserimento
  
  public static void removeUselessAttributes(HttpSession session)
  {
    Enumeration<?> enumeration = (Enumeration<?>)session.getAttributeNames();
    Vector<String> elements = new Vector<String>();
    while (enumeration.hasMoreElements())
    {
      String currentAttribute = (String) enumeration.nextElement();
      if (!StringUtils.in(currentAttribute, new String[] { RUOLO_UTENZA, 
          PATH_TO_FOLLOW, ANAG_ACCESS, UMA_ACCESS, POINT_TO,
          IRIDE2_ABILITAZIONI, USER, PASSWORD, CERTIFICATO, CERT_ACCESS_POINT,
          IDENTITA, ELENCO_CUAA_TITOLARE, PORTAL_NAME, IDAZIENDA_ELENCO_SOCI, STATTRACE_PARAM,
          STATTRACE_PORTALE, HIRIDE2ABILITAZIONI, DIRITTORW, VACTOR, SolmrConstants.SESSION_MESSAGGI_TESTATA,
          SolmrConstants.SESSION_ERRORI_PAGINA, UTENTE_ABILITAZIONI}))
      {
        if (SolmrLogger.isDebugEnabled(WebUtils.class))
          SolmrLogger.debug(WebUtils.class, "Removing attribute: "
              + currentAttribute);
        if (currentAttribute != null)
        {
          elements.add(currentAttribute);
        }
      }
    }
    for (int i = 0; i < elements.size(); i++)
    {
      session.removeAttribute((String) elements.elementAt(i));
    }
  }

  /**
   * Elimina dalla sessione tutti gli oggetti filtro meno quelli passati nella
   * String noRemove (essa deve essere formata da una serie di campi separati da
   * virgole). Se noRemove è null o stringa vuota rimuove tutti quelli inclusi
   * nel range dei filtri.
   *
   * @param session
   * @param noRemove
   */
  public static void removeUselessFilter(HttpSession session, String noRemove)
  {
    Enumeration<?> enumeration = session.getAttributeNames();
    Vector<String> elements = new Vector<String>();
    while (enumeration.hasMoreElements())
    {
      String currentAttribute = (String) enumeration.nextElement();
      if (StringUtils.in(currentAttribute, new String[] { PARTICELLAREVO,
          DOCUMENTORICERCAVO, IDPIANORIFERIMENTOFABBRICATO,IDPIANORIFERIMENTOMANODOPERA,
          UNITAARBOREARICERCAVO, PRATICHEANNICOMBO, PRATICHEPROCEDIMENTI,IDAZIENDAPRATICHE,
          PIANORIFERIMENTALLEVAMENTO, ELENCOAZIENDELINK,
          AZIENDACOLLEGATACORRENTE, ELENCOIDAZIENDECOLLEGATE,
          VAZIENDAINS, LEVELAZIENDACOLLEGATA,
          ELENCOIDAZIENDACOLLEGATAAZCOLLALL, ELENCODATAINGRESSOAZCOLLALL,
          ELENCODATAUSCITAAZCOLLALL, ELENCOCOLLEGATE, FLAGSTORICOAZCOLL,
          SIANTERRENIVO, ELENCO_REGISTRO_STALLA,
          IDALLEVAMENTO_REGISTRO_STALLA, DESCRIZIONE_SPECIE, IDINTERMEDIARIO_1,
          IDINTERMEDIARIO_2, CUAATRASFERIMENTOUFFICIO,MOD_ANAGAZIENDAVO,MOD_PERSONAVO,
          ANOMALIE_DICHIARAZIONE,UV_RESPONSE_CCIAA, ID_UNITA_CCIAA, STORICO_PARTICELLAVO_CCIAA,
          ELENCOIDPARTICELLEIMPORTAASSERVIMENTO,
          ANAGAZIENDAVOIMPORTAASSERVIMENTO,ELENCOIDPARTICELLAIMPORTAASSERVIMENTO,
          IDUTEIMPORTAASSERVIMENTO,RICERCAIMPORTAASSERVIMENTO,FILTRIRICERCATERRENOIMPORTAASSERVIMENTO,
          SOTTO_CATEGORIE_ALLEVAMENTI,
          ALLEVAMENTO_MOD,STABULAZIONI_TRATTAMENTI,
          VCESSIONIACQUISIZIONI,VSTOCCAGGIO,VEFFLUENTI,VACQUEREFLUE,VVOLUMESOTTOGRIGLIATO,
          VVOLUMEREFLUOAZIENDA,VCOM10R,ANOMALIEDICHIARAZIONICONSISTENZA,HFILTRIVERIFICATERRENI,
          DOCUMENTO_VO,ELENCO_PROPRIETARI,ELENCO_PARTICELLE,PARTICELLE_ASSOCIATE,
          PARTICELLE_DA_ASSOCIARE,ELENCO_TIPI_USO_SUOLO_PRIMARIO,VAZIENDAATECOSEC,
          FILTRIRICERCAAZIENDECOLLEGATE,ELENCOIDAZIENDECOLLEGATEEXCEL,TOTSUPAZIENDECOLLEGATE,
          SBLOCCASOCIINSAMANO, GESTOREFASCICOLOSIAN, ARRPAGAMENTOEROGATOVO,TTIPOLOGIAPRATICA,
          ARRRECUPEROPREGRESSOVO,FILTRI_RICERCA_VARIAZIONI_AZIENDALI,NUM_VARIAZIONI_SELEZIONATE,
          RITORNOPRATICHECCAGRISERVVO, UVRESPONSECCIAA,UVIMPORTADACCIAA,IDDOCUMENTOPROTOCOLLO,VIDISOLAPARCELLA,IDDICHCONSALLINEAUVLG,
          ELENCOIDALLINEAUV,VELENCOFILEALLEGATI,ALLEGATODOCUMENTOVO,SCHEDECREDITO,ELENCONOTIFICHE,NOTIFICAMODIFICAVO,
          ELENCOIDALLINEAPERCUSOELEGG,CUAANUOVAISCRIZIONE,IDAZIENDANUOVA,VUTEAZIENDANUOVA,VFABBRAZIENDANUOVA,
          VPARTFABBRAZIENDANUOVA,VPARTICELLEAZIENDANUOVA,VALLEVAMENTIAZIENDANUOVA,ELENCOALLEVAMENTIBDN,
          VCCAZIENDANUOVA,FILTRO_POLIZZA,TITOLARITA_SIGMATER,PAGINACORINDEX,DOCUMENTOALLEGATOVO,FILTRIRICERCAMACCHINEAGRICOLEVO,
          SIANFASCICOLORESPONSEVO,VATECOSECUTE}))
      {
        boolean flagTrovato = false;
        if (currentAttribute != null)
        {
          if (Validator.isNotEmpty(noRemove))
          {
            StringTokenizer strToken = new StringTokenizer(noRemove, ",");
            String strTmp = "";
            while (strToken.hasMoreTokens())
            {
              strTmp = strToken.nextToken().trim();
              if (strTmp.equalsIgnoreCase(currentAttribute))
              {
                flagTrovato = true;
              }
            }
          }

          if (!flagTrovato)
          {
            if (SolmrLogger.isDebugEnabled(WebUtils.class))
            {
              SolmrLogger.debug(WebUtils.class, "Removing attribute: "
                  + currentAttribute);
            }
            elements.add(currentAttribute);
          }
        }
      }
    }
    for (int i = 0; i < elements.size(); i++)
    {
      session.removeAttribute((String) elements.elementAt(i));
    }
  }


  public static void putInHashtable(Hashtable<Object,Object> ht, String key, Object value)
  {
    ht.remove(key);
    if (value != null)
    {
      ht.put(key, value);
    }
  }

  /**
   * Metodo utilizzato per calcolare il totale delle superfici catastali
   *
   * @param elencoParticelle
   * @param filtriParticellareRicercaVO
   * @return java.lang.String
   */
  public static String calcolaTotSupCatastale(Collection<?> elencoParticelle,
      FiltriParticellareRicercaVO filtriParticellareRicercaVO)
  {
    Hashtable<Long,String> mappa = new Hashtable<Long,String>();
    String totale = "";
    for (int i = 0; i < elencoParticelle.size(); i++)
    {
      StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle
          .toArray()[i];
      if ((mappa.get(storicoParticellaVO.getIdStoricoParticella()) == null))
      {
        mappa.put(storicoParticellaVO.getIdStoricoParticella(),
            storicoParticellaVO.getSupCatastale());
      }
    }
    Enumeration<Long> enumeraMappa = mappa.keys();
    double totSupCatastale = 0;
    while (enumeraMappa.hasMoreElements())
    {
      Long idConduzione = (Long) enumeraMappa.nextElement();
      String supCatastale = (String) mappa.get(idConduzione);
      totSupCatastale += Double.valueOf(supCatastale.replace(',', '.'))
          .doubleValue();
    }
    totale = StringUtils.parseSuperficieField(String.valueOf(totSupCatastale));
    return totale;
  }
  
  
  public static String calcolaTotSuperficieGrafica(Collection<?> elencoParticelle,
      FiltriParticellareRicercaVO filtriParticellareRicercaVO)
  {
    Hashtable<Long,String> mappa = new Hashtable<Long,String>();
    String totale = "";
    for (int i = 0; i < elencoParticelle.size(); i++)
    {
      StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle
          .toArray()[i];
      if ((mappa.get(storicoParticellaVO.getIdStoricoParticella()) == null))
      {
        mappa.put(storicoParticellaVO.getIdStoricoParticella(),
            storicoParticellaVO.getSuperficieGrafica());
      }
    }
    Enumeration<Long> enumeraMappa = mappa.keys();
    double totSuperficieGrafica = 0;
    while (enumeraMappa.hasMoreElements())
    {
      Long idConduzione = (Long) enumeraMappa.nextElement();
      String superficieGrafica = (String) mappa.get(idConduzione);
      totSuperficieGrafica += Double.valueOf(superficieGrafica.replace(',', '.'))
          .doubleValue();
    }
    totale = StringUtils.parseSuperficieField(String.valueOf(totSuperficieGrafica));
    return totale;
  }

  /**
   * Metodo utilizzato per calcolare il totale delle superfici catastali
   * relativo alle unità arboree
   *
   * @param elencoParticelleArboree
   * @return java.lang.String
   */
  public static String calcolaTotSupCatastaleUnar(
      StoricoParticellaVO[] elencoParticelleArboree)
  {
    Hashtable<Long,String> mappa = new Hashtable<Long,String>();
    String totale = "";
    for (int i = 0; i < elencoParticelleArboree.length; i++)
    {
      StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelleArboree[i];
      if ((mappa.get(storicoParticellaVO.getIdStoricoParticella()) == null))
      {
        mappa.put(storicoParticellaVO.getIdStoricoParticella(),
            storicoParticellaVO.getSupCatastale());
      }
    }
    Enumeration<Long> enumeraMappa = mappa.keys();
    double totSupCatastale = 0;
    while (enumeraMappa.hasMoreElements())
    {
      Long idConduzione = (Long) enumeraMappa.nextElement();
      String supCatastale = (String) mappa.get(idConduzione);
      totSupCatastale += Double.valueOf(supCatastale.replace(',', '.'))
          .doubleValue();
    }
    totale = StringUtils.parseSuperficieField(String.valueOf(totSupCatastale));
    return totale;
  }

  /**
   * Metodo utilizzato per calcolare il totale delle superfici condotte
   *
   * @param elencoParticelle
   * @param filtriParticellareRicercaVO
   * @return java.lang.String
   */
  public static String calcolaTotSupCondotte(Collection<?> elencoParticelle,
      FiltriParticellareRicercaVO filtriParticellareRicercaVO)
  {
    Hashtable<Long,String> mappa = new Hashtable<Long,String>();
    String totale = "";
    boolean isStorico = false;
    if (filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() > 0)
    {
      isStorico = true;
    }
    for (int i = 0; i < elencoParticelle.size(); i++)
    {
      StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle
          .toArray()[i];
      if (!isStorico)
      {
        if ((mappa.get(((ConduzioneParticellaVO) storicoParticellaVO
            .getElencoConduzioni()[0]).getIdConduzioneParticella()) == null))
        {
          mappa.put(((ConduzioneParticellaVO) storicoParticellaVO
              .getElencoConduzioni()[0]).getIdConduzioneParticella(),
              ((ConduzioneParticellaVO) storicoParticellaVO
                  .getElencoConduzioni()[0]).getSuperficieCondotta());
        }
      }
      else
      {
        if ((mappa.get(((ConduzioneDichiarataVO) storicoParticellaVO
            .getElencoConduzioniDichiarate()[0]).getIdConduzioneDichiarata()) == null))
        {
          mappa.put(((ConduzioneDichiarataVO) storicoParticellaVO
              .getElencoConduzioniDichiarate()[0]).getIdConduzioneDichiarata(),
              ((ConduzioneDichiarataVO) storicoParticellaVO
                  .getElencoConduzioniDichiarate()[0]).getSuperficieCondotta());
        }
      }
    }
    Enumeration<Long> enumeraMappa = mappa.keys();
    double totSupCondotta = 0;
    while (enumeraMappa.hasMoreElements())
    {
      Long idConduzione = (Long) enumeraMappa.nextElement();
      String supCondotta = (String) mappa.get(idConduzione);
      totSupCondotta += Double.valueOf(supCondotta.replace(',', '.'))
          .doubleValue();
    }
    totale = StringUtils.parseSuperficieField(String.valueOf(totSupCondotta));
    return totale;
  }

  /**
   * Metodo utilizzato per calcolare il totale delle superfici agronomiche
   *
   * @param elencoParticelle
   * @param filtriParticellareRicercaVO
   * @return java.lang.String
   */
  public static String calcolaTotSupAgronomiche(Collection<?> elencoParticelle,
      FiltriParticellareRicercaVO filtriParticellareRicercaVO)
  {
    Hashtable<Long,String> mappa = new Hashtable<Long,String>();
    String totale = "";
    boolean isStorico = false;
    if (filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() > 0)
    {
      isStorico = true;
    }
    for (int i = 0; i < elencoParticelle.size(); i++)
    {
      StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle
          .toArray()[i];
      if (!isStorico)
      {
        if(mappa.get(storicoParticellaVO.getElencoConduzioni()[0]
            .getIdConduzioneParticella()) == null)
        {          
          if(storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso()
            .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
          {
            mappa.put(storicoParticellaVO.getElencoConduzioni()[0].getIdConduzioneParticella(),
                storicoParticellaVO.getElencoConduzioni()[0].getSuperficieCondotta());
          }          
          else if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0]
            .getSuperficieAgronomica()))
          {
            mappa.put(storicoParticellaVO.getElencoConduzioni()[0].getIdConduzioneParticella(),
                storicoParticellaVO.getElencoConduzioni()[0].getSuperficieAgronomica());
          }
        }
      }
      else
      {
        if(mappa.get(storicoParticellaVO.getElencoConduzioniDichiarate()[0]
          .getIdConduzioneDichiarata()) == null)
        {
          if(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getIdTitoloPossesso()
              .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
          {
            mappa.put(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getIdConduzioneDichiarata(),
                storicoParticellaVO.getElencoConduzioniDichiarate()[0].getSuperficieCondotta());
          }   
          else if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioniDichiarate()[0]
            .getSuperficieAgronomica()))
          {
            mappa.put(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getIdConduzioneDichiarata(),
                storicoParticellaVO.getElencoConduzioniDichiarate()[0].getSuperficieAgronomica());
          }
        }
      }
    }
    Enumeration<Long> enumeraMappa = mappa.keys();
    double totSupAgronomica = 0;
    while (enumeraMappa.hasMoreElements())
    {
      Long idConduzione = (Long) enumeraMappa.nextElement();
      String supAgronomica = (String) mappa.get(idConduzione);
      totSupAgronomica += Double.valueOf(supAgronomica.replace(',', '.'))
          .doubleValue();
    }
    totale = StringUtils.parseSuperficieField(String.valueOf(totSupAgronomica));
    return totale;
  }

  /**
   * Metodo utilizzato per calcolare il totale delle superfici ad uso primario
   *
   * @param elencoParticelle
   * @param filtriParticellareRicercaVO
   * @return java.lang.String
   */
  public static String calcolaTotSupUsoPrimario(Collection<?> elencoParticelle,
      FiltriParticellareRicercaVO filtriParticellareRicercaVO)
  {
    Hashtable<Long,String> mappa = new Hashtable<Long,String>();
    String totale = "";
    boolean isStorico = false;
    //Long idUtilizzoAsservimento = new Long(-5);
    //Long idSfrigu = new Long(-1);
    //double totSupCondotta = 0;
    //double totSupSfrigu = 0;
    if (filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() > 0)
    {
      isStorico = true;
    }
    for (int i = 0; i < elencoParticelle.size(); i++)
    {
      StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle
          .toArray()[i];
      if (!isStorico)
      {
        //Sommo le superifici in asservimento
        /*if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento())
            && filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase("N"))
        {
          if(storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso()
              .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
          {
            if(mappa.get(idUtilizzoAsservimento) != null)
            {
              String supCondotta = mappa.get(idUtilizzoAsservimento);
              totSupCondotta = Double.valueOf(supCondotta.replace(',', '.')).doubleValue();
              supCondotta = storicoParticellaVO.getElencoConduzioni()[0].getSuperficieCondotta();
              totSupCondotta += Double.valueOf(supCondotta.replace(',', '.'))
                  .doubleValue();
              supCondotta = StringUtils.parseSuperficieField(String.valueOf(totSupCondotta));
              mappa.put(idUtilizzoAsservimento, supCondotta);
            }
            else
            {
              mappa.put(idUtilizzoAsservimento,storicoParticellaVO.getElencoConduzioni()[0].getSuperficieCondotta());
            }
          }
        }*/
        
        //Tolto da richiesta di teresa il 02/05/2016
        /*if(Validator.isNotEmpty(filtriParticellareRicercaVO.getIdUtilizzo())
          && (filtriParticellareRicercaVO.getIdUtilizzo() <= 0))
        {
          //Sommo gli sfrigu
          if((storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0]
            .getIdUtilizzoParticella().compareTo(idSfrigu) == 0)
            && (storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso()
              .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) != 0))
          {
            if(mappa.get(idSfrigu) != null)
            {
              String supSfrigu = mappa.get(idSfrigu);
              totSupSfrigu = Double.valueOf(supSfrigu.replace(',', '.')).doubleValue();
              supSfrigu = storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata();
              totSupSfrigu += Double.valueOf(supSfrigu.replace(',', '.'))
                  .doubleValue();
              supSfrigu = StringUtils.parseSuperficieField(String.valueOf(totSupSfrigu));
              mappa.put(idSfrigu, supSfrigu);
            }
            else
            {
              mappa.put(idSfrigu, storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata());
            }
          }
        }*/
        
        if(storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso()
            .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) != 0)
        {
        
          if (storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0]
             .getIdUtilizzoParticella().longValue() > -1)
          {
            if(mappa.get(storicoParticellaVO.getElencoConduzioni()[0]
              .getElencoUtilizzi()[0].getIdUtilizzoParticella()) == null)
            {
              mappa.put(storicoParticellaVO.getElencoConduzioni()[0]
                .getElencoUtilizzi()[0].getIdUtilizzoParticella(), storicoParticellaVO.getElencoConduzioni()[0]
                .getElencoUtilizzi()[0].getSuperficieUtilizzata());
            }
          }
        }
        
        /*if (storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0]
            .getIdUtilizzoParticella().longValue() > -1)
        {
          if(mappa.get(storicoParticellaVO.getElencoConduzioni()[0]
            .getElencoUtilizzi()[0].getIdUtilizzoParticella()) == null)
          {
            mappa.put(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0]
              .getIdUtilizzoParticella(), storicoParticellaVO.getElencoConduzioni()[0]
              .getElencoUtilizzi()[0].getSuperficieUtilizzata());
          }
        }*/
      }
      else
      {
        
        //Sommo le superifici in asservimento
        /*if(Validator.isNotEmpty(filtriParticellareRicercaVO.getCheckEscludiAsservimento())
            && filtriParticellareRicercaVO.getCheckEscludiAsservimento().equalsIgnoreCase("N"))
        {
          if(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getIdTitoloPossesso()
              .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
          {
            if(mappa.get(idUtilizzoAsservimento) != null)
            {
              String supCondotta = mappa.get(idUtilizzoAsservimento);
              totSupCondotta = Double.valueOf(supCondotta.replace(',', '.')).doubleValue();
              supCondotta = storicoParticellaVO.getElencoConduzioniDichiarate()[0].getSuperficieCondotta();
              totSupCondotta += Double.valueOf(supCondotta.replace(',', '.'))
                  .doubleValue();
              supCondotta = StringUtils.parseSuperficieField(String.valueOf(totSupCondotta));
              mappa.put(idUtilizzoAsservimento, supCondotta);
            }
            else
            {
              mappa.put(idUtilizzoAsservimento,storicoParticellaVO.getElencoConduzioniDichiarate()[0].getSuperficieCondotta());
            }
          }
        }*/
        
        
        if (storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi() != null
            && storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getIdUtilizzoDichiarato() != null)
        {
          if (storicoParticellaVO.getElencoConduzioniDichiarate()[0]
              .getElencoUtilizzi()[0].getIdUtilizzoDichiarato().longValue() > -1)
          {
            if (mappa.get(storicoParticellaVO.getElencoConduzioniDichiarate()[0]
                  .getElencoUtilizzi()[0].getIdUtilizzoDichiarato()) == null)
            {
              mappa.put(storicoParticellaVO.getElencoConduzioniDichiarate()[0]
                .getElencoUtilizzi()[0].getIdUtilizzoDichiarato(), storicoParticellaVO
                .getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata());
            }
          }
        }
      }
    }
    Enumeration<Long> enumeraMappa = mappa.keys();
    double totSupUsoPrimario = 0;
    while (enumeraMappa.hasMoreElements())
    {
      Long idUtilizzo = (Long) enumeraMappa.nextElement();
      String supUsoPrimario = (String) mappa.get(idUtilizzo);
      totSupUsoPrimario += Double.valueOf(supUsoPrimario.replace(',', '.'))
          .doubleValue();
    }
    totale = StringUtils
        .parseSuperficieField(String.valueOf(totSupUsoPrimario));
    return totale;
  }

  /**
   * Metodo utilizzato per calcolare il totale delle superfici ad uso secondario
   *
   * @param elencoParticelle
   * @param filtriParticellareRicercaVO
   * @return java.lang.String
   */
  public static String calcolaTotSupUsoSecondario(Collection<?> elencoParticelle,
      FiltriParticellareRicercaVO filtriParticellareRicercaVO)
  {
    Hashtable<Long,String> mappa = new Hashtable<Long,String>();
    String totale = "";
    boolean isStorico = false;
    if (filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() > 0)
    {
      isStorico = true;
    }
    for (int i = 0; i < elencoParticelle.size(); i++)
    {
      StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelle
          .toArray()[i];
      if (!isStorico)
      {
        if(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi() != null)
        {
          if(mappa.get(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0]
                  .getIdUtilizzoParticella()) == null)
          {
            if (Validator.isNotEmpty(storicoParticellaVO
                    .getElencoConduzioni()[0].getElencoUtilizzi()[0]
                    .getSupUtilizzataSecondaria()))
            {
              mappa.put(storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0]
                          .getIdUtilizzoParticella(), 
                        storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0]
                          .getSupUtilizzataSecondaria());
            }
          }
        }
      }
      else
      {
        if (storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi() != null)
        {        
          if(mappa.get(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0]
                  .getIdUtilizzoDichiarato()) == null)
          {
            if (Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0]
                    .getSupUtilizzataSecondaria()))
            {
              mappa.put(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getIdUtilizzoDichiarato(),
                        storicoParticellaVO.getElencoConduzioniDichiarate()[0].getElencoUtilizzi()[0].getSupUtilizzataSecondaria());
            }
          }
        }
      }
    }
    Enumeration<Long> enumeraMappa = mappa.keys();
    double totSupUsoSecondario = 0;
    while (enumeraMappa.hasMoreElements())
    {
      Long idUtilizzo = (Long) enumeraMappa.nextElement();
      String supUsoSecondario = (String) mappa.get(idUtilizzo);
      totSupUsoSecondario += Double.valueOf(supUsoSecondario.replace(',', '.'))
          .doubleValue();
    }
    totale = StringUtils.parseSuperficieField(String
        .valueOf(totSupUsoSecondario));
    return totale;
  }

  /**
   * Metodo che mi consente di calcolare il totale della superficie vitata
   *
   * @param elencoParticelleArboree
   * @param filtriUnitaArboreaRicercaVO
   * @return java.lang.String
   */
  public static String calcolaTotSupVitata(
      StoricoParticellaVO[] elencoParticelleArboree,
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO)
  {
    Hashtable<Long,String> mappa = new Hashtable<Long,String>();
    String totale = "";
    for (int i = 0; i < elencoParticelleArboree.length; i++)
    {
      StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelleArboree[i];
      if (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0)
      {
        if ((mappa.get(storicoParticellaVO.getStoricoUnitaArboreaVO()
            .getIdStoricoUnitaArborea()) == null))
        {
          mappa.put(storicoParticellaVO.getStoricoUnitaArboreaVO()
              .getIdStoricoUnitaArborea(), storicoParticellaVO
              .getStoricoUnitaArboreaVO().getArea());
        }
      }
      else
      {
        if ((mappa.get(storicoParticellaVO.getUnitaArboreaDichiarataVO()
            .getIdUnitaArboreaDichiarata()) == null))
        {
          mappa.put(storicoParticellaVO.getUnitaArboreaDichiarataVO()
              .getIdUnitaArboreaDichiarata(), storicoParticellaVO
              .getUnitaArboreaDichiarataVO().getArea());
        }
      }
    }
    Enumeration<Long> enumeraMappa = mappa.keys();
    double totArea = 0;
    while (enumeraMappa.hasMoreElements())
    {
      Long idUnita = (Long) enumeraMappa.nextElement();
      String supVitata = (String) mappa.get(idUnita);
      totArea += Double.valueOf(supVitata.replace(',', '.')).doubleValue();
    }
    totale = StringUtils.parseSuperficieField(String.valueOf(totArea));
    return totale;
  }
  
  /**
   * Metodo che mi consente di calcolare il totale della superficie da iscrivere albo
   * 
   * @param elencoParticelleArboree
   * @param filtriUnitaArboreaRicercaVO
   * @return
   */
  public static String calcolaTotSupDaIscrivereAlbo(
      StoricoParticellaVO[] elencoParticelleArboree,
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO)
  {
    Hashtable<Long,String> mappa = new Hashtable<Long,String>();
    String totale = "";
    for (int i = 0; i < elencoParticelleArboree.length; i++)
    {
      StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) elencoParticelleArboree[i];
      if (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0)
      {
        if ((mappa.get(storicoParticellaVO.getStoricoUnitaArboreaVO()
            .getIdStoricoUnitaArborea()) == null))
        {
          String supDaIscrivere = storicoParticellaVO
            .getStoricoUnitaArboreaVO().getSuperficieDaIscrivereAlbo();
          if(supDaIscrivere == null)
          {
            supDaIscrivere = SolmrConstants.DEFAULT_SUPERFICIE;
          }
          mappa.put(storicoParticellaVO.getStoricoUnitaArboreaVO()
              .getIdStoricoUnitaArborea(), supDaIscrivere);
        }
      }
      else
      {
        if ((mappa.get(storicoParticellaVO.getUnitaArboreaDichiarataVO()
            .getIdUnitaArboreaDichiarata()) == null))
        {
          
          String supDaIscrivere = storicoParticellaVO
            .getUnitaArboreaDichiarataVO().getSuperficieDaIscrivereAlbo();
          if(supDaIscrivere == null)
          {
            supDaIscrivere = SolmrConstants.DEFAULT_SUPERFICIE;
          }
          mappa.put(storicoParticellaVO.getUnitaArboreaDichiarataVO()
              .getIdUnitaArboreaDichiarata(), supDaIscrivere);
        }
      }
    }
    Enumeration<Long> enumeraMappa = mappa.keys();
    double totSupDaIscrivere = 0;
    while (enumeraMappa.hasMoreElements())
    {
      Long idUnita = (Long) enumeraMappa.nextElement();
      String supDaIscrivere = (String) mappa.get(idUnita);
      totSupDaIscrivere += Double.valueOf(supDaIscrivere.replace(',', '.')).doubleValue();
    }
    totale = StringUtils.parseSuperficieField(String.valueOf(totSupDaIscrivere));
    return totale;
  }

}
