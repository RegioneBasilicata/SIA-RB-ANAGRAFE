package it.csi.solmr.util;


/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.SolmrErrors;
import it.csi.solmr.presentation.security.Autorizzazione;
import it.csi.solmr.presentation.security.cu.AccessoSistemaCU;
import it.csi.solmr.presentation.security.cu.AlboVignetiCCIAACU;
import it.csi.solmr.presentation.security.cu.AllineaUVAEleggibilitaCU;
import it.csi.solmr.presentation.security.cu.AllineaUtilizzoAEleggibilitaCU;
import it.csi.solmr.presentation.security.cu.AziendaTitolareCU;
import it.csi.solmr.presentation.security.cu.BuoniCarburanteCU;
import it.csi.solmr.presentation.security.cu.CessazioneAttivitaCU;
import it.csi.solmr.presentation.security.cu.CessazioneAziendaCU;
import it.csi.solmr.presentation.security.cu.ChiusuraNotificheCU;
import it.csi.solmr.presentation.security.cu.DirittiProduzioneCU;
import it.csi.solmr.presentation.security.cu.ElencoRichiesteAziendaCU;
import it.csi.solmr.presentation.security.cu.EsportaDatiUVCU;
import it.csi.solmr.presentation.security.cu.EstrazioneDatiCU;
import it.csi.solmr.presentation.security.cu.FascicoloDemViewCU;
import it.csi.solmr.presentation.security.cu.GestioneAllevamentiCU;
import it.csi.solmr.presentation.security.cu.GestioneAttestazioniCU;
import it.csi.solmr.presentation.security.cu.GestioneAziendaCU;
import it.csi.solmr.presentation.security.cu.GestioneBioCU;
import it.csi.solmr.presentation.security.cu.GestioneComunicazione10RCU;
import it.csi.solmr.presentation.security.cu.GestioneContiCorrentiCU;
import it.csi.solmr.presentation.security.cu.GestioneDocumentiCU;
import it.csi.solmr.presentation.security.cu.GestioneFabbricatiCU;
import it.csi.solmr.presentation.security.cu.GestioneMandatoCU;
import it.csi.solmr.presentation.security.cu.GestioneManodoperaCU;
import it.csi.solmr.presentation.security.cu.GestioneTerreniCU;
import it.csi.solmr.presentation.security.cu.GestioneTerrenoRicercaCU;
import it.csi.solmr.presentation.security.cu.GestioneUnitaArboreeCU;
import it.csi.solmr.presentation.security.cu.GestioneUtilitiCU;
import it.csi.solmr.presentation.security.cu.GestioneVariazioniAziendaCU;
import it.csi.solmr.presentation.security.cu.GestisciMotoriAgricoliCU;
import it.csi.solmr.presentation.security.cu.ImportaFonteAaepCU;
import it.csi.solmr.presentation.security.cu.ImportaFonteAtCU;
import it.csi.solmr.presentation.security.cu.ImportaFonteBdnCU;
import it.csi.solmr.presentation.security.cu.ImportaSoggettiAaepCU;
import it.csi.solmr.presentation.security.cu.ImportaTerreniCU;
import it.csi.solmr.presentation.security.cu.ImportaTerreniSianCU;
import it.csi.solmr.presentation.security.cu.ImportaUteAaepCU;
import it.csi.solmr.presentation.security.cu.InvioFascicoliCU;
import it.csi.solmr.presentation.security.cu.ModificaGestoreFascicoloCU;
import it.csi.solmr.presentation.security.cu.MonitoraggioCU;
import it.csi.solmr.presentation.security.cu.NotificheCU;
import it.csi.solmr.presentation.security.cu.NuovaAziendaCU;
import it.csi.solmr.presentation.security.cu.NuovaDichConsistenzaCU;
import it.csi.solmr.presentation.security.cu.NuovaParticellaCU;
import it.csi.solmr.presentation.security.cu.PianoGraficoCU;
import it.csi.solmr.presentation.security.cu.PolizzeCU;
import it.csi.solmr.presentation.security.cu.PraticheCU;
import it.csi.solmr.presentation.security.cu.RegistroDebitoriCU;
import it.csi.solmr.presentation.security.cu.ReportisticaCU;
import it.csi.solmr.presentation.security.cu.RevocaDelegaCU;
import it.csi.solmr.presentation.security.cu.RicercaAziendaCU;
import it.csi.solmr.presentation.security.cu.RicercaNotificaCU;
import it.csi.solmr.presentation.security.cu.RicercaPersonaCU;
import it.csi.solmr.presentation.security.cu.RicercaRichiestaCU;
import it.csi.solmr.presentation.security.cu.RicercaTerrenoCU;
import it.csi.solmr.presentation.security.cu.RicercaTerrenoSigmaterCU;
import it.csi.solmr.presentation.security.cu.RicercaVariazioniAziendaCU;
import it.csi.solmr.presentation.security.cu.RichiestaAziendaCU;
import it.csi.solmr.presentation.security.cu.RichiesteCessValCU;
import it.csi.solmr.presentation.security.cu.StampaFascicoloCU;
import it.csi.solmr.presentation.security.cu.StoricoAziendaCU;
import it.csi.solmr.presentation.security.cu.StoricoMandatiCU;
import it.csi.solmr.presentation.security.cu.VerificaConsistenzaCU;
import it.csi.solmr.presentation.security.cu.VisualizzaAllevamentiCU;
import it.csi.solmr.presentation.security.cu.VisualizzaAttestazioniCU;
import it.csi.solmr.presentation.security.cu.VisualizzaBdnCU;
import it.csi.solmr.presentation.security.cu.VisualizzaBioCU;
import it.csi.solmr.presentation.security.cu.VisualizzaComunicazione10RCU;
import it.csi.solmr.presentation.security.cu.VisualizzaConsistenzaCU;
import it.csi.solmr.presentation.security.cu.VisualizzaContiCorrentiCU;
import it.csi.solmr.presentation.security.cu.VisualizzaDatiAziendaCU;
import it.csi.solmr.presentation.security.cu.VisualizzaDocumentiCU;
import it.csi.solmr.presentation.security.cu.VisualizzaFabbricatiCU;
import it.csi.solmr.presentation.security.cu.VisualizzaFonteAaepCU;
import it.csi.solmr.presentation.security.cu.VisualizzaFonteAtCU;
import it.csi.solmr.presentation.security.cu.VisualizzaFonteSianCU;
import it.csi.solmr.presentation.security.cu.VisualizzaGestFascicoloCU;
import it.csi.solmr.presentation.security.cu.VisualizzaGis3DCU;
import it.csi.solmr.presentation.security.cu.VisualizzaIsoleParcelleCU;
import it.csi.solmr.presentation.security.cu.VisualizzaManodoperaCU;
import it.csi.solmr.presentation.security.cu.VisualizzaMotoriAgricoliCU;
import it.csi.solmr.presentation.security.cu.VisualizzaSchedarioCU;
import it.csi.solmr.presentation.security.cu.VisualizzaSigMaterCU;
import it.csi.solmr.presentation.security.cu.VisualizzaSoggettiAaepCU;
import it.csi.solmr.presentation.security.cu.VisualizzaSoggettiAtCU;
import it.csi.solmr.presentation.security.cu.VisualizzaTerreniCU;
import it.csi.solmr.presentation.security.cu.VisualizzaUnitaArboreeCU;
import it.csi.solmr.presentation.security.cu.VisualizzaUteAaepCU;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class IrideFileParser extends DefaultHandler 
{
	private String configFileURL = null;
	private HashMap<String,Autorizzazione> mapping = null;
	protected static final String ERRORE_CARICAMENTO_XML = "Errore di caricamento del file di mapping iride2";

	private static HashMap<String,Object> elencoSecurity = new HashMap<String,Object>(); 
	{
	    elencoSecurity.put("ACCESSO_SISTEMA", new 	AccessoSistemaCU());
	    elencoSecurity.put("RICERCA_AZIENDA", new 	RicercaAziendaCU());
	    elencoSecurity.put("AZIENDE_TITOLARE", new 	AziendaTitolareCU());
	    elencoSecurity.put("RICERCA_PERSONA", new 	RicercaPersonaCU());
	    elencoSecurity.put("RICERCA_TERRENO", new 	RicercaTerrenoCU());
	    elencoSecurity.put("GESTIONE_TERRENO_RICERCA", new 	GestioneTerrenoRicercaCU());
	    elencoSecurity.put("RICERCA_NOTIFICA", new 	RicercaNotificaCU());
	    elencoSecurity.put("NUOVA_AZIENDA", new 	NuovaAziendaCU());
	    elencoSecurity.put("CESSAZIONE_ATTIVITA", new 	CessazioneAttivitaCU());
	    elencoSecurity.put("GESTIONE_AZIENDA", new 	GestioneAziendaCU());
	    elencoSecurity.put("VISUALIZZA_DATI_AZIENDA", new 	VisualizzaDatiAziendaCU());
	    elencoSecurity.put("VISUALIZZA_FONTE_AAEP", new 	VisualizzaFonteAaepCU());
	    elencoSecurity.put("VISUALIZZA_SOGGETTI_AAEP", new 	VisualizzaSoggettiAaepCU());
	    elencoSecurity.put("VISUALIZZA_UTE_AAEP", new 	VisualizzaUteAaepCU());
	    elencoSecurity.put("VISUALIZZA_FONTE_AT", new 	VisualizzaFonteAtCU());
	    elencoSecurity.put("IMPORTA_FONTE_AAEP", new 	ImportaFonteAaepCU());
	    elencoSecurity.put("IMPORTA_FONTE_AT", new 	ImportaFonteAtCU());
	    elencoSecurity.put("IMPORTA_SOGGETTI_AAEP", new 	ImportaSoggettiAaepCU());
	    elencoSecurity.put("IMPORTA_UTE_AAEP", new 	ImportaUteAaepCU());
	    elencoSecurity.put("GESTIONE_MANDATO", new 	GestioneMandatoCU());
	    elencoSecurity.put("VISUALIZZA_TERRENI", new 	VisualizzaTerreniCU());
	    elencoSecurity.put("GESTIONE_TERRENI", new 	GestioneTerreniCU());
	    elencoSecurity.put("IMPORTA_TERRENI_SIAN", new 	ImportaTerreniSianCU());
	    elencoSecurity.put("VISUALIZZA_ALLEVAMENTI", new 	VisualizzaAllevamentiCU());
	    elencoSecurity.put("GESTIONE_ALLEVAMENTI", new 	GestioneAllevamentiCU());
	    elencoSecurity.put("VISUALIZZA_BDN", new 	VisualizzaBdnCU());
	    elencoSecurity.put("IMPORTA_FONTE_BDN", new 	ImportaFonteBdnCU());
	    elencoSecurity.put("VISUALIZZA_FABBRICATI", new 	VisualizzaFabbricatiCU());
	    elencoSecurity.put("GESTIONE_FABBRICATI", new 	GestioneFabbricatiCU());
	    elencoSecurity.put("VISUALIZZA_MANODOPERA", new 	VisualizzaManodoperaCU());
	    elencoSecurity.put("GESTIONE_MANODOPERA", new 	GestioneManodoperaCU());
	    elencoSecurity.put("VISUALIZZA_MOTORI_AGRICOLI", new 	VisualizzaMotoriAgricoliCU());
	    elencoSecurity.put("VISUALIZZA_CONTI_CORRENTI", new 	VisualizzaContiCorrentiCU());
	    elencoSecurity.put("GESTIONE_CONTI_CORRENTI", new 	GestioneContiCorrentiCU());
	    elencoSecurity.put("VISUALIZZA_DOCUMENTI", new 	VisualizzaDocumentiCU());
	    elencoSecurity.put("GESTIONE_DOCUMENTI", new 	GestioneDocumentiCU());
	    elencoSecurity.put("VERIFICA_CONSISTENZA", new 	VerificaConsistenzaCU());
	    elencoSecurity.put("NUOVA_DICH_CONSISTENZA", new 	NuovaDichConsistenzaCU());
	    elencoSecurity.put("DIRITTI_PRODUZIONE", new 	DirittiProduzioneCU());
	    elencoSecurity.put("PRATICHE", new 	PraticheCU());
	    elencoSecurity.put("STAMPA_FASCICOLO", new 	StampaFascicoloCU());
	    elencoSecurity.put("NOTIFICHE", new 	NotificheCU());
	    elencoSecurity.put("CHIUSURA_NOTIFICHE", new 	ChiusuraNotificheCU());
	    elencoSecurity.put("REPORTISTICA", new 	ReportisticaCU());
	    elencoSecurity.put("VISUALIZZA_BIO", new 	VisualizzaBioCU());
	    elencoSecurity.put("GESTIONE_BIO", new 	GestioneBioCU());
	    elencoSecurity.put("MOD_GEST_FASCICOLO", new ModificaGestoreFascicoloCU());
	    elencoSecurity.put("VISUALIZZA_ATTESTATI", new VisualizzaAttestazioniCU());
	    elencoSecurity.put("GESTIONE_ATTESTATI", new GestioneAttestazioniCU());
	    elencoSecurity.put("GESTIONE_UTILITI", new GestioneUtilitiCU());
	    elencoSecurity.put("GESTIONE_COMUNICAZIONE_10R", new GestioneComunicazione10RCU());
	    elencoSecurity.put("VISUALIZZA_COMUNICAZIONE_10R", new VisualizzaComunicazione10RCU());
	    elencoSecurity.put("CESSAZIONE_AZIENDA", new CessazioneAziendaCU());
	    elencoSecurity.put("NUOVA_PARTICELLA", new   NuovaParticellaCU());
	    elencoSecurity.put("GESTIONE_VARIAZIONI_AZIENDA", new   GestioneVariazioniAziendaCU());
	    elencoSecurity.put("RICERCA_VARIAZIONI_AZIENDA", new   RicercaVariazioniAziendaCU());
	    elencoSecurity.put("MONITORAGGIO", new   MonitoraggioCU());
	    elencoSecurity.put("VISUALIZZA_SIGMATER", new   VisualizzaSigMaterCU());
	    elencoSecurity.put("VISUALIZZA_GIS3D", new   VisualizzaGis3DCU());
	    elencoSecurity.put("VISUALIZZA_CONSISTENZA", new   VisualizzaConsistenzaCU());
	    elencoSecurity.put("ESTRAZIONE_DATI", new   EstrazioneDatiCU());
	    elencoSecurity.put("VISUALIZZA_GEST_FASCICOLO", new   VisualizzaGestFascicoloCU());
	    elencoSecurity.put("VISUALIZZA_SOGGETTI_AT", new   VisualizzaSoggettiAtCU());
	    elencoSecurity.put("STORICO_MANDATI", new   StoricoMandatiCU());
	    elencoSecurity.put("STORICO_AZIENDA", new   StoricoAziendaCU());
	    elencoSecurity.put("VISUALIZZA_ISOLEPARCELLE", new   VisualizzaIsoleParcelleCU());
	    elencoSecurity.put("ALBO_VIGNETI_CCIAA", new   AlboVignetiCCIAACU());
	    elencoSecurity.put("VISUALIZZA_SCHEDARIO", new   VisualizzaSchedarioCU());
	    elencoSecurity.put("RICERCA_TERRENO_SIGMATER", new   RicercaTerrenoSigmaterCU());
	    elencoSecurity.put("ALLINEA_UTILIZZO_A_ELEGGIBILITA", new   AllineaUtilizzoAEleggibilitaCU());
	    elencoSecurity.put("POLIZZE", new PolizzeCU());
	    elencoSecurity.put("VISUALIZZA_UNITA_ARBOREE", new VisualizzaUnitaArboreeCU());
	    elencoSecurity.put("GESTIONE_UNITA_ARBOREE", new GestioneUnitaArboreeCU());
	    elencoSecurity.put("ALLINEA_UV_A_ELEGGIBILITA", new AllineaUVAEleggibilitaCU());
	    elencoSecurity.put("IMPORTA_TERRENI", new ImportaTerreniCU());
	    elencoSecurity.put("ESPORTA_DATI_UV", new EsportaDatiUVCU());
	    elencoSecurity.put("REVOCA_DELEGA", new RevocaDelegaCU());
	    elencoSecurity.put("REGISTRO_DEBITORI", new RegistroDebitoriCU());
	    elencoSecurity.put("INVIO_FASCICOLI", new InvioFascicoliCU());
	    elencoSecurity.put("RICHIESTA_AZIENDA", new RichiestaAziendaCU());
	    elencoSecurity.put("RICERCA_RICHIESTA", new RicercaRichiestaCU());
	    elencoSecurity.put("BUONI_CARBURANTE", new   BuoniCarburanteCU());
	    elencoSecurity.put("FASCICOLO_DEM_VIEW", new   FascicoloDemViewCU());
	    elencoSecurity.put("ELENCO_RICHIESTE_AZIENDA", new   ElencoRichiesteAziendaCU());
	    elencoSecurity.put("GESTISCI_MOTORI_AGRICOLI", new GestisciMotoriAgricoliCU());
	    elencoSecurity.put("VISUALIZZA_FONTE_SIAN", new VisualizzaFonteSianCU());
	    elencoSecurity.put("RICHIESTE_CESS_VAL", new RichiesteCessValCU());
	    elencoSecurity.put("PIANO_GRAFICO", new PianoGraficoCU());
	}
	
	public Autorizzazione getMacroCU(String nameMacroCU)
	{
	  return (Autorizzazione)elencoSecurity.get(nameMacroCU);
	}

	public IrideFileParser() {
		this(SolmrConstants.FILE_XML_IRIDE2);
	}

	public IrideFileParser(String configFileURL) {
		if(SolmrLogger.isDebugEnabled(this)) {
			SolmrLogger.debug(this, "Reading configuration file " + configFileURL + "...");
		}
		this.configFileURL = configFileURL;
		mapping = new HashMap<String,Autorizzazione>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(true);
		try {
			InputStream is = this.getClass().getResourceAsStream(configFileURL);
			if(is == null) {
				throw new IOException(SolmrErrors.EXC_CONF_FILE_NF);
			}
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(is, this);
		}
		catch(ParserConfigurationException ex) {
		}
		catch(SAXException ex) {
		}
		catch(IOException ex) {
			SolmrLogger.fatal(this, ex);
		}
	}

	public void endDocument() throws SAXException {
		if(SolmrLogger.isDebugEnabled(this)) {
			SolmrLogger.debug(this, "Finish document parsing");
		}
	}

	public void startDocument() throws SAXException {
		if(SolmrLogger.isDebugEnabled(this)) {
			SolmrLogger.debug(this, "Start document parsing: file " + configFileURL + "...");
		}
	}

	public void error(SAXParseException e) throws SAXException {
		SolmrLogger.error(this, e);
	}

	public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException {
		String eName = lName;
		if("".equals(eName)) {
			eName = qName;
		}
		if(eName.equals("mapping")) {
			if(attrs != null) {
				String jspValue=attrs.getValue("jsp");
				if (jspValue == null) {
					SolmrLogger.fatal(this,"[IrideFileParser::startElement] Errore grave"+ " nel parsing del file di mapping iride2... Trovato elemento mapping"+" con attributo \"jsp\" mancante");
					throw new SAXException(ERRORE_CARICAMENTO_XML);
				}
				String iride2Value = attrs.getValue("iride2");
				if(iride2Value == null) {
					SolmrLogger.fatal(this,"[IrideFileParser::startElement] Errore grave"+" nel parsing del file di mapping iride2... Trovato elemento mapping"+" con attributo \"iride2\" mancante");
					throw new SAXException(ERRORE_CARICAMENTO_XML);
				}
				Autorizzazione autorizzazione=(Autorizzazione) elencoSecurity.get(iride2Value);
				if(autorizzazione == null) {
					SolmrLogger.fatal(this,"[IrideFileParser::startElement] Errore grave"+" nel parsing del file di mapping iride2... Trovato elemento mapping"+" con attributo \"iride2\" non valido. Valore dell'attributo = "+iride2Value);
					throw new SAXException(ERRORE_CARICAMENTO_XML);
				}
				mapping.put(jspValue,autorizzazione);
			}
		}
	}

	public HashMap<String,Autorizzazione> getMapping() {
		return mapping;
	}
	
	public HashMap<String,Object> getElencoSecurity() {
    return elencoSecurity;
  }
}
