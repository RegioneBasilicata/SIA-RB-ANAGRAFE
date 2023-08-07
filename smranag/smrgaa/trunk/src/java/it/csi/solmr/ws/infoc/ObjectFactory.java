
package it.csi.solmr.ws.infoc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.csi.solmr.ws.infoc package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RappresentanteLegaleSesso_QNAME = new QName("http://bean.frontend.ls.com/xsd", "sesso");
    private final static QName _RappresentanteLegaleDescrComuneNascita_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrComuneNascita");
    private final static QName _RappresentanteLegaleCap_QNAME = new QName("http://bean.frontend.ls.com/xsd", "cap");
    private final static QName _RappresentanteLegaleCodComuneNascita_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codComuneNascita");
    private final static QName _RappresentanteLegaleCodiceFiscale_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codiceFiscale");
    private final static QName _RappresentanteLegaleNumeroCivico_QNAME = new QName("http://bean.frontend.ls.com/xsd", "numeroCivico");
    private final static QName _RappresentanteLegaleNome_QNAME = new QName("http://bean.frontend.ls.com/xsd", "nome");
    private final static QName _RappresentanteLegaleIndirizzo_QNAME = new QName("http://bean.frontend.ls.com/xsd", "indirizzo");
    private final static QName _RappresentanteLegaleTipoVia_QNAME = new QName("http://bean.frontend.ls.com/xsd", "tipoVia");
    private final static QName _RappresentanteLegaleDataNascita_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataNascita");
    private final static QName _RappresentanteLegaleDescrComuneResidenza_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrComuneResidenza");
    private final static QName _RappresentanteLegaleCognome_QNAME = new QName("http://bean.frontend.ls.com/xsd", "cognome");
    private final static QName _RappresentanteLegaleCodComuneResidenza_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codComuneResidenza");
    private final static QName _SedeEmail_QNAME = new QName("http://bean.frontend.ls.com/xsd", "email");
    private final static QName _SedeCodCausaleCessazione_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codCausaleCessazione");
    private final static QName _SedeFax_QNAME = new QName("http://bean.frontend.ls.com/xsd", "fax");
    private final static QName _SedeTelefono_QNAME = new QName("http://bean.frontend.ls.com/xsd", "telefono");
    private final static QName _SedeToponimo_QNAME = new QName("http://bean.frontend.ls.com/xsd", "toponimo");
    private final static QName _SedeCodiceStatoEstero_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codiceStatoEstero");
    private final static QName _SedeDataInizioAttivita_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataInizioAttivita");
    private final static QName _SedeDescrizioneCausaleCessazione_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrizioneCausaleCessazione");
    private final static QName _SedeNomeComune_QNAME = new QName("http://bean.frontend.ls.com/xsd", "nomeComune");
    private final static QName _SedeCodComune_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codComune");
    private final static QName _SedeDataFineAttivita_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataFineAttivita");
    private final static QName _SedeDenominazioneSede_QNAME = new QName("http://bean.frontend.ls.com/xsd", "denominazioneSede");
    private final static QName _SedeCodTipoLocalizzazione_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codTipoLocalizzazione");
    private final static QName _SedeSiglaProvincia_QNAME = new QName("http://bean.frontend.ls.com/xsd", "siglaProvincia");
    private final static QName _SedeFrazioneUL_QNAME = new QName("http://bean.frontend.ls.com/xsd", "frazioneUL");
    private final static QName _SedeDataCessaz_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataCessaz");
    private final static QName _SedeSiglaProvUL_QNAME = new QName("http://bean.frontend.ls.com/xsd", "siglaProvUL");
    private final static QName _SedeDescrizioneTipoSede_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrizioneTipoSede");
    private final static QName _SedeIdTipoSede_QNAME = new QName("http://bean.frontend.ls.com/xsd", "idTipoSede");
    private final static QName _SedeDataInizioValidita_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataInizioValidita");
    private final static QName _SedeIndirizzoSede_QNAME = new QName("http://bean.frontend.ls.com/xsd", "indirizzoSede");
    private final static QName _SedeDescrStatoEstero_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrStatoEstero");
    private final static QName _SedeDescrizioneAttivitaSede_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrizioneAttivitaSede");
    private final static QName _CaricaPersonaInfocCodiceCarica_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codiceCarica");
    private final static QName _CaricaPersonaInfocDescrCarica_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrCarica");
    private final static QName _CaricaPersonaInfocDataFineCarica_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataFineCarica");
    private final static QName _CaricaPersonaInfocDataInizioCarica_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataInizioCarica");
    private final static QName _ListaPersonaCaricaInfocCodFiscaleAzienda_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codFiscaleAzienda");
    private final static QName _ListaPersonaCaricaInfocCodFiscalePersona_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codFiscalePersona");
    private final static QName _AtecoRI2007InfocCodAteco2007_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codAteco2007");
    private final static QName _AtecoRI2007InfocCodImportanzaRI_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codImportanzaRI");
    private final static QName _AtecoRI2007InfocDataInizioAteco2007_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataInizioAteco2007");
    private final static QName _PersonaRIInfocCodComuneRes_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codComuneRes");
    private final static QName _PersonaRIInfocDescrFrazioneRes_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrFrazioneRes");
    private final static QName _PersonaRIInfocDescrStatoRes_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrStatoRes");
    private final static QName _PersonaRIInfocDescrToponimoResid_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrToponimoResid");
    private final static QName _PersonaRIInfocViaResidenza_QNAME = new QName("http://bean.frontend.ls.com/xsd", "viaResidenza");
    private final static QName _PersonaRIInfocCapResidenza_QNAME = new QName("http://bean.frontend.ls.com/xsd", "capResidenza");
    private final static QName _PersonaRIInfocDescrStatoNascita_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrStatoNascita");
    private final static QName _PersonaRIInfocNumCivicoResid_QNAME = new QName("http://bean.frontend.ls.com/xsd", "numCivicoResid");
    private final static QName _PersonaRIInfocSiglaProvResidenza_QNAME = new QName("http://bean.frontend.ls.com/xsd", "siglaProvResidenza");
    private final static QName _CercaPuntualePersonaRIResponseReturn_QNAME = new QName("http://servizio.frontend.ls.com", "return");
    private final static QName _CercaPuntualePersonaRICodFonte_QNAME = new QName("http://servizio.frontend.ls.com", "codFonte");
    private final static QName _CercaPuntualePersonaRICodiceFiscale_QNAME = new QName("http://servizio.frontend.ls.com", "codiceFiscale");
    private final static QName _CercaPuntualePersonaRIProgrPersona_QNAME = new QName("http://servizio.frontend.ls.com", "progrPersona");
    private final static QName _SezSpecDataFine_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataFine");
    private final static QName _SezSpecCodSezione_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codSezione");
    private final static QName _SezSpecCodiceSezSpec_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codiceSezSpec");
    private final static QName _ListaPersoneRIProgrPersona_QNAME = new QName("http://bean.frontend.ls.com/xsd", "progrPersona");
    private final static QName _AziendaDescrATECO2007_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrATECO2007");
    private final static QName _AziendaIdNaturaGiuridica_QNAME = new QName("http://bean.frontend.ls.com/xsd", "idNaturaGiuridica");
    private final static QName _AziendaDataIscrRegistroImpr_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataIscrRegistroImpr");
    private final static QName _AziendaRagioneSociale_QNAME = new QName("http://bean.frontend.ls.com/xsd", "ragioneSociale");
    private final static QName _AziendaRappresentanteLegale_QNAME = new QName("http://bean.frontend.ls.com/xsd", "rappresentanteLegale");
    private final static QName _AziendaPartitaIva_QNAME = new QName("http://bean.frontend.ls.com/xsd", "partitaIva");
    private final static QName _AziendaCodiceCausaleCessazione_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codiceCausaleCessazione");
    private final static QName _AziendaDataCancellazRea_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataCancellazRea");
    private final static QName _AziendaDataCostituzione_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataCostituzione");
    private final static QName _AziendaAnnoCCIAA_QNAME = new QName("http://bean.frontend.ls.com/xsd", "annoCCIAA");
    private final static QName _AziendaProvinciaCCIAA_QNAME = new QName("http://bean.frontend.ls.com/xsd", "provinciaCCIAA");
    private final static QName _AziendaOggettoSociale_QNAME = new QName("http://bean.frontend.ls.com/xsd", "oggettoSociale");
    private final static QName _AziendaDataCessazione_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataCessazione");
    private final static QName _AziendaCodATECO2007_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codATECO2007");
    private final static QName _AziendaDataIscrizioneRea_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataIscrizioneRea");
    private final static QName _AziendaPostaElettronicaCertificata_QNAME = new QName("http://bean.frontend.ls.com/xsd", "postaElettronicaCertificata");
    private final static QName _AziendaNRegistroImpreseCCIAA_QNAME = new QName("http://bean.frontend.ls.com/xsd", "nRegistroImpreseCCIAA");
    private final static QName _AziendaNumeroCCIAA_QNAME = new QName("http://bean.frontend.ls.com/xsd", "numeroCCIAA");
    private final static QName _AziendaDescrizioneNaturaGiuridica_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrizioneNaturaGiuridica");
    private final static QName _ListaSediProgrSede_QNAME = new QName("http://bean.frontend.ls.com/xsd", "progrSede");
    private final static QName _CercaPuntualeSedeProgrSede_QNAME = new QName("http://servizio.frontend.ls.com", "progrSede");
    private final static QName _ProcConcorsLocalRegistroAtto_QNAME = new QName("http://bean.frontend.ls.com/xsd", "localRegistroAtto");
    private final static QName _ProcConcorsDataChiusuraLiquidaz_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataChiusuraLiquidaz");
    private final static QName _ProcConcorsDataFineLiquidaz_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataFineLiquidaz");
    private final static QName _ProcConcorsDescrAltreIndicazioni_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrAltreIndicazioni");
    private final static QName _ProcConcorsCodAtto_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codAtto");
    private final static QName _ProcConcorsDescIndicatEsecutAtto_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descIndicatEsecutAtto");
    private final static QName _ProcConcorsSiglaProvRegAtto_QNAME = new QName("http://bean.frontend.ls.com/xsd", "siglaProvRegAtto");
    private final static QName _ProcConcorsDataRevocalLiquidaz_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataRevocalLiquidaz");
    private final static QName _ProcConcorsDataRegistroAtto_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataRegistroAtto");
    private final static QName _ProcConcorsDataAperturaProc_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataAperturaProc");
    private final static QName _ProcConcorsDataEsecConcordPrevent_QNAME = new QName("http://bean.frontend.ls.com/xsd", "dataEsecConcordPrevent");
    private final static QName _ProcConcorsDescrTribunale_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrTribunale");
    private final static QName _ProcConcorsCodLiquidazione_QNAME = new QName("http://bean.frontend.ls.com/xsd", "codLiquidazione");
    private final static QName _ProcConcorsDescrCodAtto_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrCodAtto");
    private final static QName _ProcConcorsNumRestistrAtto_QNAME = new QName("http://bean.frontend.ls.com/xsd", "numRestistrAtto");
    private final static QName _ProcConcorsDescrNotaio_QNAME = new QName("http://bean.frontend.ls.com/xsd", "descrNotaio");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.csi.solmr.ws.infoc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CercaPuntualePersonaRIResponse }
     * 
     */
    public CercaPuntualePersonaRIResponse createCercaPuntualePersonaRIResponse() {
        return new CercaPuntualePersonaRIResponse();
    }

    /**
     * Create an instance of {@link PersonaRIInfoc }
     * 
     */
    public PersonaRIInfoc createPersonaRIInfoc() {
        return new PersonaRIInfoc();
    }

    /**
     * Create an instance of {@link CercaPuntualePersonaRI }
     * 
     */
    public CercaPuntualePersonaRI createCercaPuntualePersonaRI() {
        return new CercaPuntualePersonaRI();
    }

    /**
     * Create an instance of {@link CercaPuntualeSedeResponse }
     * 
     */
    public CercaPuntualeSedeResponse createCercaPuntualeSedeResponse() {
        return new CercaPuntualeSedeResponse();
    }

    /**
     * Create an instance of {@link Sede }
     * 
     */
    public Sede createSede() {
        return new Sede();
    }

    /**
     * Create an instance of {@link CercaPerFiltriCodFiscFonte }
     * 
     */
    public CercaPerFiltriCodFiscFonte createCercaPerFiltriCodFiscFonte() {
        return new CercaPerFiltriCodFiscFonte();
    }

    /**
     * Create an instance of {@link CercaPerFiltriCodFiscFonteResponse }
     * 
     */
    public CercaPerFiltriCodFiscFonteResponse createCercaPerFiltriCodFiscFonteResponse() {
        return new CercaPerFiltriCodFiscFonteResponse();
    }

    /**
     * Create an instance of {@link ListaPersonaCaricaInfoc }
     * 
     */
    public ListaPersonaCaricaInfoc createListaPersonaCaricaInfoc() {
        return new ListaPersonaCaricaInfoc();
    }

    /**
     * Create an instance of {@link CercaPerCodiceFiscale }
     * 
     */
    public CercaPerCodiceFiscale createCercaPerCodiceFiscale() {
        return new CercaPerCodiceFiscale();
    }

    /**
     * Create an instance of {@link CercaPerCodiceFiscaleResponse }
     * 
     */
    public CercaPerCodiceFiscaleResponse createCercaPerCodiceFiscaleResponse() {
        return new CercaPerCodiceFiscaleResponse();
    }

    /**
     * Create an instance of {@link Azienda }
     * 
     */
    public Azienda createAzienda() {
        return new Azienda();
    }

    /**
     * Create an instance of {@link CercaPuntualeSede }
     * 
     */
    public CercaPuntualeSede createCercaPuntualeSede() {
        return new CercaPuntualeSede();
    }

    /**
     * Create an instance of {@link ListaPersoneRI }
     * 
     */
    public ListaPersoneRI createListaPersoneRI() {
        return new ListaPersoneRI();
    }

    /**
     * Create an instance of {@link ListaSedi }
     * 
     */
    public ListaSedi createListaSedi() {
        return new ListaSedi();
    }

    /**
     * Create an instance of {@link AtecoRI2007Infoc }
     * 
     */
    public AtecoRI2007Infoc createAtecoRI2007Infoc() {
        return new AtecoRI2007Infoc();
    }

    /**
     * Create an instance of {@link RappresentanteLegale }
     * 
     */
    public RappresentanteLegale createRappresentanteLegale() {
        return new RappresentanteLegale();
    }

    /**
     * Create an instance of {@link CaricaPersonaInfoc }
     * 
     */
    public CaricaPersonaInfoc createCaricaPersonaInfoc() {
        return new CaricaPersonaInfoc();
    }

    /**
     * Create an instance of {@link ProcConcors }
     * 
     */
    public ProcConcors createProcConcors() {
        return new ProcConcors();
    }

    /**
     * Create an instance of {@link SezSpec }
     * 
     */
    public SezSpec createSezSpec() {
        return new SezSpec();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "sesso", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleSesso(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleSesso_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrComuneNascita", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleDescrComuneNascita(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleDescrComuneNascita_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "cap", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleCap(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleCap_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codComuneNascita", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleCodComuneNascita(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleCodComuneNascita_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codiceFiscale", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleCodiceFiscale(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleCodiceFiscale_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "numeroCivico", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleNumeroCivico(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleNumeroCivico_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "nome", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleNome(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleNome_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "indirizzo", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleIndirizzo(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleIndirizzo_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "tipoVia", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleTipoVia(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleTipoVia_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataNascita", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleDataNascita(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleDataNascita_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrComuneResidenza", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleDescrComuneResidenza(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleDescrComuneResidenza_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "cognome", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleCognome(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleCognome_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codComuneResidenza", scope = RappresentanteLegale.class)
    public JAXBElement<String> createRappresentanteLegaleCodComuneResidenza(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleCodComuneResidenza_QNAME, String.class, RappresentanteLegale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "email", scope = Sede.class)
    public JAXBElement<String> createSedeEmail(String value) {
        return new JAXBElement<String>(_SedeEmail_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codCausaleCessazione", scope = Sede.class)
    public JAXBElement<String> createSedeCodCausaleCessazione(String value) {
        return new JAXBElement<String>(_SedeCodCausaleCessazione_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "fax", scope = Sede.class)
    public JAXBElement<String> createSedeFax(String value) {
        return new JAXBElement<String>(_SedeFax_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "telefono", scope = Sede.class)
    public JAXBElement<String> createSedeTelefono(String value) {
        return new JAXBElement<String>(_SedeTelefono_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "toponimo", scope = Sede.class)
    public JAXBElement<String> createSedeToponimo(String value) {
        return new JAXBElement<String>(_SedeToponimo_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codiceStatoEstero", scope = Sede.class)
    public JAXBElement<String> createSedeCodiceStatoEstero(String value) {
        return new JAXBElement<String>(_SedeCodiceStatoEstero_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataInizioAttivita", scope = Sede.class)
    public JAXBElement<XMLGregorianCalendar> createSedeDataInizioAttivita(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_SedeDataInizioAttivita_QNAME, XMLGregorianCalendar.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrizioneCausaleCessazione", scope = Sede.class)
    public JAXBElement<String> createSedeDescrizioneCausaleCessazione(String value) {
        return new JAXBElement<String>(_SedeDescrizioneCausaleCessazione_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "cap", scope = Sede.class)
    public JAXBElement<String> createSedeCap(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleCap_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "nomeComune", scope = Sede.class)
    public JAXBElement<String> createSedeNomeComune(String value) {
        return new JAXBElement<String>(_SedeNomeComune_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codComune", scope = Sede.class)
    public JAXBElement<String> createSedeCodComune(String value) {
        return new JAXBElement<String>(_SedeCodComune_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataFineAttivita", scope = Sede.class)
    public JAXBElement<XMLGregorianCalendar> createSedeDataFineAttivita(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_SedeDataFineAttivita_QNAME, XMLGregorianCalendar.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "numeroCivico", scope = Sede.class)
    public JAXBElement<String> createSedeNumeroCivico(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleNumeroCivico_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "indirizzo", scope = Sede.class)
    public JAXBElement<String> createSedeIndirizzo(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleIndirizzo_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "denominazioneSede", scope = Sede.class)
    public JAXBElement<String> createSedeDenominazioneSede(String value) {
        return new JAXBElement<String>(_SedeDenominazioneSede_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codTipoLocalizzazione", scope = Sede.class)
    public JAXBElement<String> createSedeCodTipoLocalizzazione(String value) {
        return new JAXBElement<String>(_SedeCodTipoLocalizzazione_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "siglaProvincia", scope = Sede.class)
    public JAXBElement<String> createSedeSiglaProvincia(String value) {
        return new JAXBElement<String>(_SedeSiglaProvincia_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "frazioneUL", scope = Sede.class)
    public JAXBElement<String> createSedeFrazioneUL(String value) {
        return new JAXBElement<String>(_SedeFrazioneUL_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataCessaz", scope = Sede.class)
    public JAXBElement<XMLGregorianCalendar> createSedeDataCessaz(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_SedeDataCessaz_QNAME, XMLGregorianCalendar.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "siglaProvUL", scope = Sede.class)
    public JAXBElement<String> createSedeSiglaProvUL(String value) {
        return new JAXBElement<String>(_SedeSiglaProvUL_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrizioneTipoSede", scope = Sede.class)
    public JAXBElement<String> createSedeDescrizioneTipoSede(String value) {
        return new JAXBElement<String>(_SedeDescrizioneTipoSede_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "idTipoSede", scope = Sede.class)
    public JAXBElement<String> createSedeIdTipoSede(String value) {
        return new JAXBElement<String>(_SedeIdTipoSede_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataInizioValidita", scope = Sede.class)
    public JAXBElement<XMLGregorianCalendar> createSedeDataInizioValidita(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_SedeDataInizioValidita_QNAME, XMLGregorianCalendar.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "indirizzoSede", scope = Sede.class)
    public JAXBElement<String> createSedeIndirizzoSede(String value) {
        return new JAXBElement<String>(_SedeIndirizzoSede_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrStatoEstero", scope = Sede.class)
    public JAXBElement<String> createSedeDescrStatoEstero(String value) {
        return new JAXBElement<String>(_SedeDescrStatoEstero_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrizioneAttivitaSede", scope = Sede.class)
    public JAXBElement<String> createSedeDescrizioneAttivitaSede(String value) {
        return new JAXBElement<String>(_SedeDescrizioneAttivitaSede_QNAME, String.class, Sede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codiceCarica", scope = CaricaPersonaInfoc.class)
    public JAXBElement<String> createCaricaPersonaInfocCodiceCarica(String value) {
        return new JAXBElement<String>(_CaricaPersonaInfocCodiceCarica_QNAME, String.class, CaricaPersonaInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrCarica", scope = CaricaPersonaInfoc.class)
    public JAXBElement<String> createCaricaPersonaInfocDescrCarica(String value) {
        return new JAXBElement<String>(_CaricaPersonaInfocDescrCarica_QNAME, String.class, CaricaPersonaInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataFineCarica", scope = CaricaPersonaInfoc.class)
    public JAXBElement<XMLGregorianCalendar> createCaricaPersonaInfocDataFineCarica(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_CaricaPersonaInfocDataFineCarica_QNAME, XMLGregorianCalendar.class, CaricaPersonaInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataInizioCarica", scope = CaricaPersonaInfoc.class)
    public JAXBElement<XMLGregorianCalendar> createCaricaPersonaInfocDataInizioCarica(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_CaricaPersonaInfocDataInizioCarica_QNAME, XMLGregorianCalendar.class, CaricaPersonaInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codiceCarica", scope = ListaPersonaCaricaInfoc.class)
    public JAXBElement<String> createListaPersonaCaricaInfocCodiceCarica(String value) {
        return new JAXBElement<String>(_CaricaPersonaInfocCodiceCarica_QNAME, String.class, ListaPersonaCaricaInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codFiscaleAzienda", scope = ListaPersonaCaricaInfoc.class)
    public JAXBElement<String> createListaPersonaCaricaInfocCodFiscaleAzienda(String value) {
        return new JAXBElement<String>(_ListaPersonaCaricaInfocCodFiscaleAzienda_QNAME, String.class, ListaPersonaCaricaInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrCarica", scope = ListaPersonaCaricaInfoc.class)
    public JAXBElement<String> createListaPersonaCaricaInfocDescrCarica(String value) {
        return new JAXBElement<String>(_CaricaPersonaInfocDescrCarica_QNAME, String.class, ListaPersonaCaricaInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataFineCarica", scope = ListaPersonaCaricaInfoc.class)
    public JAXBElement<XMLGregorianCalendar> createListaPersonaCaricaInfocDataFineCarica(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_CaricaPersonaInfocDataFineCarica_QNAME, XMLGregorianCalendar.class, ListaPersonaCaricaInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codFiscalePersona", scope = ListaPersonaCaricaInfoc.class)
    public JAXBElement<String> createListaPersonaCaricaInfocCodFiscalePersona(String value) {
        return new JAXBElement<String>(_ListaPersonaCaricaInfocCodFiscalePersona_QNAME, String.class, ListaPersonaCaricaInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codAteco2007", scope = AtecoRI2007Infoc.class)
    public JAXBElement<String> createAtecoRI2007InfocCodAteco2007(String value) {
        return new JAXBElement<String>(_AtecoRI2007InfocCodAteco2007_QNAME, String.class, AtecoRI2007Infoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codImportanzaRI", scope = AtecoRI2007Infoc.class)
    public JAXBElement<String> createAtecoRI2007InfocCodImportanzaRI(String value) {
        return new JAXBElement<String>(_AtecoRI2007InfocCodImportanzaRI_QNAME, String.class, AtecoRI2007Infoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataInizioAteco2007", scope = AtecoRI2007Infoc.class)
    public JAXBElement<XMLGregorianCalendar> createAtecoRI2007InfocDataInizioAteco2007(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AtecoRI2007InfocDataInizioAteco2007_QNAME, XMLGregorianCalendar.class, AtecoRI2007Infoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codComuneRes", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocCodComuneRes(String value) {
        return new JAXBElement<String>(_PersonaRIInfocCodComuneRes_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrFrazioneRes", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocDescrFrazioneRes(String value) {
        return new JAXBElement<String>(_PersonaRIInfocDescrFrazioneRes_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "sesso", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocSesso(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleSesso_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrStatoRes", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocDescrStatoRes(String value) {
        return new JAXBElement<String>(_PersonaRIInfocDescrStatoRes_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrToponimoResid", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocDescrToponimoResid(String value) {
        return new JAXBElement<String>(_PersonaRIInfocDescrToponimoResid_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "viaResidenza", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocViaResidenza(String value) {
        return new JAXBElement<String>(_PersonaRIInfocViaResidenza_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "capResidenza", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocCapResidenza(String value) {
        return new JAXBElement<String>(_PersonaRIInfocCapResidenza_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codComuneNascita", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocCodComuneNascita(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleCodComuneNascita_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrStatoNascita", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocDescrStatoNascita(String value) {
        return new JAXBElement<String>(_PersonaRIInfocDescrStatoNascita_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codiceFiscale", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocCodiceFiscale(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleCodiceFiscale_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "nome", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocNome(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleNome_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "numCivicoResid", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocNumCivicoResid(String value) {
        return new JAXBElement<String>(_PersonaRIInfocNumCivicoResid_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataNascita", scope = PersonaRIInfoc.class)
    public JAXBElement<XMLGregorianCalendar> createPersonaRIInfocDataNascita(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RappresentanteLegaleDataNascita_QNAME, XMLGregorianCalendar.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "cognome", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocCognome(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleCognome_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "siglaProvResidenza", scope = PersonaRIInfoc.class)
    public JAXBElement<String> createPersonaRIInfocSiglaProvResidenza(String value) {
        return new JAXBElement<String>(_PersonaRIInfocSiglaProvResidenza_QNAME, String.class, PersonaRIInfoc.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonaRIInfoc }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "return", scope = CercaPuntualePersonaRIResponse.class)
    public JAXBElement<PersonaRIInfoc> createCercaPuntualePersonaRIResponseReturn(PersonaRIInfoc value) {
        return new JAXBElement<PersonaRIInfoc>(_CercaPuntualePersonaRIResponseReturn_QNAME, PersonaRIInfoc.class, CercaPuntualePersonaRIResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "codFonte", scope = CercaPuntualePersonaRI.class)
    public JAXBElement<String> createCercaPuntualePersonaRICodFonte(String value) {
        return new JAXBElement<String>(_CercaPuntualePersonaRICodFonte_QNAME, String.class, CercaPuntualePersonaRI.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "codiceFiscale", scope = CercaPuntualePersonaRI.class)
    public JAXBElement<String> createCercaPuntualePersonaRICodiceFiscale(String value) {
        return new JAXBElement<String>(_CercaPuntualePersonaRICodiceFiscale_QNAME, String.class, CercaPuntualePersonaRI.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "progrPersona", scope = CercaPuntualePersonaRI.class)
    public JAXBElement<String> createCercaPuntualePersonaRIProgrPersona(String value) {
        return new JAXBElement<String>(_CercaPuntualePersonaRIProgrPersona_QNAME, String.class, CercaPuntualePersonaRI.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataFine", scope = SezSpec.class)
    public JAXBElement<XMLGregorianCalendar> createSezSpecDataFine(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_SezSpecDataFine_QNAME, XMLGregorianCalendar.class, SezSpec.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codSezione", scope = SezSpec.class)
    public JAXBElement<String> createSezSpecCodSezione(String value) {
        return new JAXBElement<String>(_SezSpecCodSezione_QNAME, String.class, SezSpec.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codiceSezSpec", scope = SezSpec.class)
    public JAXBElement<String> createSezSpecCodiceSezSpec(String value) {
        return new JAXBElement<String>(_SezSpecCodiceSezSpec_QNAME, String.class, SezSpec.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codiceFiscale", scope = ListaPersoneRI.class)
    public JAXBElement<String> createListaPersoneRICodiceFiscale(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleCodiceFiscale_QNAME, String.class, ListaPersoneRI.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "progrPersona", scope = ListaPersoneRI.class)
    public JAXBElement<String> createListaPersoneRIProgrPersona(String value) {
        return new JAXBElement<String>(_ListaPersoneRIProgrPersona_QNAME, String.class, ListaPersoneRI.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Azienda }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "return", scope = CercaPerCodiceFiscaleResponse.class)
    public JAXBElement<Azienda> createCercaPerCodiceFiscaleResponseReturn(Azienda value) {
        return new JAXBElement<Azienda>(_CercaPuntualePersonaRIResponseReturn_QNAME, Azienda.class, CercaPerCodiceFiscaleResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "codFonte", scope = CercaPerCodiceFiscale.class)
    public JAXBElement<String> createCercaPerCodiceFiscaleCodFonte(String value) {
        return new JAXBElement<String>(_CercaPuntualePersonaRICodFonte_QNAME, String.class, CercaPerCodiceFiscale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "codiceFiscale", scope = CercaPerCodiceFiscale.class)
    public JAXBElement<String> createCercaPerCodiceFiscaleCodiceFiscale(String value) {
        return new JAXBElement<String>(_CercaPuntualePersonaRICodiceFiscale_QNAME, String.class, CercaPerCodiceFiscale.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Sede }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "return", scope = CercaPuntualeSedeResponse.class)
    public JAXBElement<Sede> createCercaPuntualeSedeResponseReturn(Sede value) {
        return new JAXBElement<Sede>(_CercaPuntualePersonaRIResponseReturn_QNAME, Sede.class, CercaPuntualeSedeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrATECO2007", scope = Azienda.class)
    public JAXBElement<String> createAziendaDescrATECO2007(String value) {
        return new JAXBElement<String>(_AziendaDescrATECO2007_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "idNaturaGiuridica", scope = Azienda.class)
    public JAXBElement<String> createAziendaIdNaturaGiuridica(String value) {
        return new JAXBElement<String>(_AziendaIdNaturaGiuridica_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataIscrRegistroImpr", scope = Azienda.class)
    public JAXBElement<String> createAziendaDataIscrRegistroImpr(String value) {
        return new JAXBElement<String>(_AziendaDataIscrRegistroImpr_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "ragioneSociale", scope = Azienda.class)
    public JAXBElement<String> createAziendaRagioneSociale(String value) {
        return new JAXBElement<String>(_AziendaRagioneSociale_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RappresentanteLegale }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "rappresentanteLegale", scope = Azienda.class)
    public JAXBElement<RappresentanteLegale> createAziendaRappresentanteLegale(RappresentanteLegale value) {
        return new JAXBElement<RappresentanteLegale>(_AziendaRappresentanteLegale_QNAME, RappresentanteLegale.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrizioneCausaleCessazione", scope = Azienda.class)
    public JAXBElement<String> createAziendaDescrizioneCausaleCessazione(String value) {
        return new JAXBElement<String>(_SedeDescrizioneCausaleCessazione_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "partitaIva", scope = Azienda.class)
    public JAXBElement<String> createAziendaPartitaIva(String value) {
        return new JAXBElement<String>(_AziendaPartitaIva_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codiceCausaleCessazione", scope = Azienda.class)
    public JAXBElement<String> createAziendaCodiceCausaleCessazione(String value) {
        return new JAXBElement<String>(_AziendaCodiceCausaleCessazione_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataCancellazRea", scope = Azienda.class)
    public JAXBElement<String> createAziendaDataCancellazRea(String value) {
        return new JAXBElement<String>(_AziendaDataCancellazRea_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataCostituzione", scope = Azienda.class)
    public JAXBElement<String> createAziendaDataCostituzione(String value) {
        return new JAXBElement<String>(_AziendaDataCostituzione_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "annoCCIAA", scope = Azienda.class)
    public JAXBElement<String> createAziendaAnnoCCIAA(String value) {
        return new JAXBElement<String>(_AziendaAnnoCCIAA_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codiceFiscale", scope = Azienda.class)
    public JAXBElement<String> createAziendaCodiceFiscale(String value) {
        return new JAXBElement<String>(_RappresentanteLegaleCodiceFiscale_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "provinciaCCIAA", scope = Azienda.class)
    public JAXBElement<String> createAziendaProvinciaCCIAA(String value) {
        return new JAXBElement<String>(_AziendaProvinciaCCIAA_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "oggettoSociale", scope = Azienda.class)
    public JAXBElement<String> createAziendaOggettoSociale(String value) {
        return new JAXBElement<String>(_AziendaOggettoSociale_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataCessazione", scope = Azienda.class)
    public JAXBElement<String> createAziendaDataCessazione(String value) {
        return new JAXBElement<String>(_AziendaDataCessazione_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codATECO2007", scope = Azienda.class)
    public JAXBElement<String> createAziendaCodATECO2007(String value) {
        return new JAXBElement<String>(_AziendaCodATECO2007_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataIscrizioneRea", scope = Azienda.class)
    public JAXBElement<String> createAziendaDataIscrizioneRea(String value) {
        return new JAXBElement<String>(_AziendaDataIscrizioneRea_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "postaElettronicaCertificata", scope = Azienda.class)
    public JAXBElement<String> createAziendaPostaElettronicaCertificata(String value) {
        return new JAXBElement<String>(_AziendaPostaElettronicaCertificata_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "nRegistroImpreseCCIAA", scope = Azienda.class)
    public JAXBElement<String> createAziendaNRegistroImpreseCCIAA(String value) {
        return new JAXBElement<String>(_AziendaNRegistroImpreseCCIAA_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "numeroCCIAA", scope = Azienda.class)
    public JAXBElement<String> createAziendaNumeroCCIAA(String value) {
        return new JAXBElement<String>(_AziendaNumeroCCIAA_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrizioneNaturaGiuridica", scope = Azienda.class)
    public JAXBElement<String> createAziendaDescrizioneNaturaGiuridica(String value) {
        return new JAXBElement<String>(_AziendaDescrizioneNaturaGiuridica_QNAME, String.class, Azienda.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "progrSede", scope = ListaSedi.class)
    public JAXBElement<String> createListaSediProgrSede(String value) {
        return new JAXBElement<String>(_ListaSediProgrSede_QNAME, String.class, ListaSedi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "codFonte", scope = CercaPuntualeSede.class)
    public JAXBElement<String> createCercaPuntualeSedeCodFonte(String value) {
        return new JAXBElement<String>(_CercaPuntualePersonaRICodFonte_QNAME, String.class, CercaPuntualeSede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "progrSede", scope = CercaPuntualeSede.class)
    public JAXBElement<String> createCercaPuntualeSedeProgrSede(String value) {
        return new JAXBElement<String>(_CercaPuntualeSedeProgrSede_QNAME, String.class, CercaPuntualeSede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "codiceFiscale", scope = CercaPuntualeSede.class)
    public JAXBElement<String> createCercaPuntualeSedeCodiceFiscale(String value) {
        return new JAXBElement<String>(_CercaPuntualePersonaRICodiceFiscale_QNAME, String.class, CercaPuntualeSede.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "localRegistroAtto", scope = ProcConcors.class)
    public JAXBElement<String> createProcConcorsLocalRegistroAtto(String value) {
        return new JAXBElement<String>(_ProcConcorsLocalRegistroAtto_QNAME, String.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataChiusuraLiquidaz", scope = ProcConcors.class)
    public JAXBElement<XMLGregorianCalendar> createProcConcorsDataChiusuraLiquidaz(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ProcConcorsDataChiusuraLiquidaz_QNAME, XMLGregorianCalendar.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataFineLiquidaz", scope = ProcConcors.class)
    public JAXBElement<XMLGregorianCalendar> createProcConcorsDataFineLiquidaz(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ProcConcorsDataFineLiquidaz_QNAME, XMLGregorianCalendar.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrAltreIndicazioni", scope = ProcConcors.class)
    public JAXBElement<String> createProcConcorsDescrAltreIndicazioni(String value) {
        return new JAXBElement<String>(_ProcConcorsDescrAltreIndicazioni_QNAME, String.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codAtto", scope = ProcConcors.class)
    public JAXBElement<String> createProcConcorsCodAtto(String value) {
        return new JAXBElement<String>(_ProcConcorsCodAtto_QNAME, String.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descIndicatEsecutAtto", scope = ProcConcors.class)
    public JAXBElement<String> createProcConcorsDescIndicatEsecutAtto(String value) {
        return new JAXBElement<String>(_ProcConcorsDescIndicatEsecutAtto_QNAME, String.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "siglaProvRegAtto", scope = ProcConcors.class)
    public JAXBElement<String> createProcConcorsSiglaProvRegAtto(String value) {
        return new JAXBElement<String>(_ProcConcorsSiglaProvRegAtto_QNAME, String.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataRevocalLiquidaz", scope = ProcConcors.class)
    public JAXBElement<XMLGregorianCalendar> createProcConcorsDataRevocalLiquidaz(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ProcConcorsDataRevocalLiquidaz_QNAME, XMLGregorianCalendar.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataRegistroAtto", scope = ProcConcors.class)
    public JAXBElement<XMLGregorianCalendar> createProcConcorsDataRegistroAtto(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ProcConcorsDataRegistroAtto_QNAME, XMLGregorianCalendar.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataAperturaProc", scope = ProcConcors.class)
    public JAXBElement<XMLGregorianCalendar> createProcConcorsDataAperturaProc(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ProcConcorsDataAperturaProc_QNAME, XMLGregorianCalendar.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "dataEsecConcordPrevent", scope = ProcConcors.class)
    public JAXBElement<XMLGregorianCalendar> createProcConcorsDataEsecConcordPrevent(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ProcConcorsDataEsecConcordPrevent_QNAME, XMLGregorianCalendar.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrTribunale", scope = ProcConcors.class)
    public JAXBElement<String> createProcConcorsDescrTribunale(String value) {
        return new JAXBElement<String>(_ProcConcorsDescrTribunale_QNAME, String.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "codLiquidazione", scope = ProcConcors.class)
    public JAXBElement<String> createProcConcorsCodLiquidazione(String value) {
        return new JAXBElement<String>(_ProcConcorsCodLiquidazione_QNAME, String.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrCodAtto", scope = ProcConcors.class)
    public JAXBElement<String> createProcConcorsDescrCodAtto(String value) {
        return new JAXBElement<String>(_ProcConcorsDescrCodAtto_QNAME, String.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "numRestistrAtto", scope = ProcConcors.class)
    public JAXBElement<String> createProcConcorsNumRestistrAtto(String value) {
        return new JAXBElement<String>(_ProcConcorsNumRestistrAtto_QNAME, String.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://bean.frontend.ls.com/xsd", name = "descrNotaio", scope = ProcConcors.class)
    public JAXBElement<String> createProcConcorsDescrNotaio(String value) {
        return new JAXBElement<String>(_ProcConcorsDescrNotaio_QNAME, String.class, ProcConcors.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "codFonte", scope = CercaPerFiltriCodFiscFonte.class)
    public JAXBElement<String> createCercaPerFiltriCodFiscFonteCodFonte(String value) {
        return new JAXBElement<String>(_CercaPuntualePersonaRICodFonte_QNAME, String.class, CercaPerFiltriCodFiscFonte.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://servizio.frontend.ls.com", name = "codiceFiscale", scope = CercaPerFiltriCodFiscFonte.class)
    public JAXBElement<String> createCercaPerFiltriCodFiscFonteCodiceFiscale(String value) {
        return new JAXBElement<String>(_CercaPuntualePersonaRICodiceFiscale_QNAME, String.class, CercaPerFiltriCodFiscFonte.class, value);
    }

}
