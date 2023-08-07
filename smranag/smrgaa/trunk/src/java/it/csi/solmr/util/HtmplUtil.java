package it.csi.solmr.util;

import it.csi.jsf.htmpl.Htmpl;
import it.csi.jsf.htmpl.StringProcessor;
import it.csi.jsf.htmpl.util.HTMLStringProcessor;
import it.csi.solmr.client.MapFactory;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.attestazioni.TipoParametriAttestazioneVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.FiltriParticellareRicercaVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.TipoMacroUsoVO;
import it.csi.solmr.etc.SolmrConstants;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * <p>Title: ADVI: Acquisizione Domande Via Internet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: CSI Piemonte</p>
 * @author Luigi R. Viggiano
 * @version $Revision: 1.1 $
 */
public abstract class HtmplUtil implements Serializable 
{

	/**
   * 
   */
  private static final long serialVersionUID = 2469669492172767834L;
  
  
  private static StringProcessor jssp = new JavaScriptStringProcessor();
	private static StringProcessor htmlsp = new HTMLStringProcessor();

	private static String pathToFollow = "";
	private static String imok = "ok.gif";
	private static String imko = "ko.gif";


	static Enumeration<?> getPropertyNames(Object obj) {
		if (obj instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest)obj;
			return request.getParameterNames();
		}
		else {
			Class<?> clazz = obj.getClass();
			Method[] methods = clazz.getMethods();
			Collection<String> methodNamesCollection = new ArrayList<String>();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				String name = method.getName();
				if (name.startsWith("get") && (name.length() > 3)) {
					String propName = Character.toLowerCase(name.charAt(3)) +
					name.substring(4);
					if (propName.equals("class")) {
						continue;
					}
					methodNamesCollection.add(propName);
				}
			}
			return Collections.enumeration(methodNamesCollection);
		}
	}

	static String getPropertyValue(Object obj, String name) {
		if (name.startsWith("err_")) {
			return null; // salta i messaggi di errore
		}
		// (poiché vengono gestiti dal metodo setErrors che non
		// fa uso di questo metodo)

		if (obj instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest)obj;
			if(request.getParameter(name) != null) {
				return (String)request.getParameter(name);
			}
			else {
				return (String)request.getAttribute(name);
			}
		}
		else {
			Class<?> clazz = obj.getClass();
			String methodName = "get" + Character
			.toUpperCase(name.charAt(0)) + name.substring(1);
			try {
				Method method = clazz.getMethod(methodName, (Class[])null);
				Object retValue = method.invoke(obj, (Object[])null);
				return (retValue != null) ? String.valueOf(retValue) : "";
			}
			catch (Exception ex) {
				//ex.printStackTrace(System.err);
				return null;
			}
		}
	}

	static String[] getPropertyValues(Object obj, String name) {
		if (name.startsWith("err_")) {
			return null; // salta i messaggi di errore
		}
		// (poiché vengono gestiti dal metodo setErrors che non
		// fa uso di questo metodo)

		if (obj instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest)obj;
			return request.getParameterValues(name);
		}
		else {
			Class<?> clazz = obj.getClass();
			String methodName = "get" + Character
			.toUpperCase(name.charAt(0)) + name.substring(1);
			try {
				Method method = clazz.getMethod(methodName, (Class[])null);
				Object retValue = method.invoke(obj, (Object[])null);
				if (retValue instanceof String) {
					return new String[] {(String)retValue};
				}
				else if (retValue instanceof String[]) {
					return (String[])retValue;
				}
				else {
					return (retValue != null) ? new String[] {String.valueOf(retValue)}
					: new String[] {};
				}
			}
			catch (Exception ex) {
				return null;
			}
		}
	}


	/**
	 * Gestisce i messaggi di errore.
	 *
	 * @param htmpl Htmpl
	 * @param errors ValidationErrors
	 * @param request HttpServletRequest
	 * @param application ServletContext
	 */
	public static void setErrors(Htmpl htmpl, ValidationErrors errors,
			HttpServletRequest request,
			javax.servlet.ServletContext application) {
		if (null == errors || null == htmpl) { // evita i NullPointerException
			return;
		}

		pathToFollow = (String)(request.getSession(false).getAttribute(
		"pathToFollow"));

		// ottiene le variabili parsificando il layout
		Iterator<?> iter = htmpl.getVariableIterator();
		while (iter.hasNext()) {
			String key = (String)iter.next();
			if (key.startsWith("err_")) {
				doError(key, errors, htmpl, request, application);
			}
		}
	}

	/**
	 * Gestisce i TextField e le label. Esempio: &lt;input type="text"
	 * name="cognome" value="$$cognome" maxlength="100"&gt;
	 *
	 * @param key String
	 * @param obj Object
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @param application ServletContext
	 */
	private static void doError(String key, Object obj, Htmpl htmpl,
			HttpServletRequest request,
			javax.servlet.ServletContext application) {
		ValidationErrors errors = (ValidationErrors)obj;
		String htmlStringKO =
			"<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" " +
			"title=\"{2}\" border=\"0\"></a>";
		String htmlStringOK = "<img src=\"{0}\" alt=\"{1}\">";
		String popupError = "alert({0})";

		pathToFollow = (String)(request.getSession(false).getAttribute(
		"pathToFollow"));
		// Parametro che arriva dalla pagina grafica di RUPAR o SISTEMA PIEMONTE
		String activeSite = (String)(request.getSession(false).getAttribute(
		"activeSite"));
		String pathErrori = "";
		if (!Validator.isNotEmpty(activeSite)) {
			if (pathToFollow.equalsIgnoreCase("rupar")) {
				activeSite = application.getInitParameter("immaginiSviluppoRupar");
				pathErrori = application.getInitParameter("erroriRupar");
			}
			else if(pathToFollow.equalsIgnoreCase("sispie")){
				activeSite = application.getInitParameter("immaginiSviluppoSispie");
				pathErrori = application.getInitParameter("erroriSispie");
			}
			else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
				activeSite = application.getInitParameter("immaginiSviluppoTOBECONFIG");
				pathErrori = application.getInitParameter("erroriTOBECONFIG");
			}
		}

		String property = key.substring(4);
		ValidationError firstError = null;
		Iterator<?> errorIterator = errors.get(property);
		if (errorIterator != null) {
			firstError = (ValidationError)errorIterator.next();
		}

		if (key.equals("err_error")) {
			// messaggio di errore su pop-up
			if (firstError != null) {
				String message = "'" + jssp.process(firstError.getMessage()) +
				"'";
				/** @@todo bset **/
				bset(htmpl, key, MessageFormat
						.format(popupError, new Object[] {message}), null);
				htmpl.set(key, MessageFormat
						.format(popupError, new Object[] {message}), null);
			}
		}
		else {
			// messaggi di errore con iconcina

			if (firstError != null) {
				// c'è un errore
				String message = firstError.getMessage();
				/** @@todo bset **/
				bset(htmpl, key,
						MessageFormat.format(
								htmlStringKO,
								new Object[] {
										//request.getContextPath()+"/"+
										//pathToFollow,
										pathErrori + imko,
										"'" + jssp.process(firstError.getMessage()) + "'",
										message
								}),
								null);
				htmpl.set(key,
						MessageFormat.format(
								htmlStringKO,
								new Object[] {
										//request.getContextPath()+"/"+
										//pathToFollow
										pathErrori + imko,
										"'" + jssp.process(firstError.getMessage()) + "'",
										message
								}),
								null);
			}
			else {
				// non c'è un errore
				/** @@todo bset **/
				bset(htmpl, key,
						MessageFormat.format(
								htmlStringOK,
								new Object[] {
										//request.getContextPath()+"/"+
										/*pathToFollow*/pathErrori + imok, ""}),
										null);
				htmpl.set(key,
						MessageFormat.format(
								htmlStringOK,
								new Object[] {
										//request.getContextPath()+"/"+
										/*pathToFollow*/pathErrori + imok, ""}),
										null);
			}
		}
	}

	public static void setValues(Htmpl htmpl, Object obj, String strpathToFollow) {
		setValues(htmpl, obj);
		pathToFollow = strpathToFollow;
	}

	public static void setValues(Htmpl htmpl, Object obj) {
		if (null == obj || null == htmpl) { // evita i NullPointerException
			return;
		}
		if (obj instanceof HttpServletRequest) {
			pathToFollow = (String)((HttpServletRequest)obj).getSession(false).getAttribute("pathToFollow");
		}

		// Processa il layout alla ricerca di marcatori "speciali"
		// e li imposta il layout, gestendo i controlli
		Iterator<?> iter = htmpl.getVariableIterator();
		while (iter.hasNext()) {
			String key = (String)iter.next();
			/** @todo quì si potrebbe applicare il command pattern */
			if (key.startsWith("cmb_")) {
				doCombo(key, obj, htmpl);
			}
			else if (key.startsWith("des_")) {
				doDescriptionLabel(key, obj, htmpl);
			}
			else if (key.startsWith("rad_")) {
				doRadio(key, obj, htmpl);
			}
			else if (key.startsWith("mul_")) { // MultipleChoice (checkbox)
				doMultipleChoice(key, obj, htmpl);
			}
			else if (key.startsWith("sng_")) { // SingleChoice (radio)
				doSingleChoiceOrizzontale(key, obj, htmpl);
			}
			else {
				doTextField(key, obj, htmpl);
			}
		}
	}

	/**
	 * Gestisce i TextField e le label. Esempio: &lt;input type="text"
	 * name="cognome" value="$$cognome" maxlength="100"&gt;
	 *
	 * @param key String
	 * @param obj Object
	 * @param htmpl Htmpl
	 */
	private static void doTextField(String key, Object obj, Htmpl htmpl) {
		/** @@todo bset **/
		bset(htmpl, key, getPropertyValue(obj, key));
		htmpl.set(key, getPropertyValue(obj, key));
	}

	/**
	 * Gestisce un controllo HTML "Radio"
	 * formato del campo radio : rad_nomeproprieta_nomevaloreselezionato.
	 * Esempio:
	 *
	 * <pre><code>
	 *  &lt;input type="radio" name="tipoCodice" value="tipoPartitaIva" $$rad_tipoCodice_tipoPartitaIva&gt;
	 *  &lt;input type="radio" name="tipoCodice" value="tipoCodiceFiscale" $$rad_tipoCodice_tipoCodiceFiscale&gt;
	 * </code></pre>
	 *
	 * @param key la chiave. Per esempio rad_tipoCodice_tipoPartitaIva
	 * @param obj l'oggetto che contiene il valore selezionato come proprietà (esempio la HttpRequest)
	 * @param htmpl il gestore del layout
	 */
	private static void doRadio(String key, Object obj, Htmpl htmpl) {
		String property = key.substring(4, key.lastIndexOf('_'));
		String value = getPropertyValue(obj, property);
		/** @@todo bset **/
		bset(htmpl, "rad_" + property + "_" + value, "checked"); //seleziona quello indicato
		htmpl.set("rad_" + property + "_" + value, "checked"); //seleziona quello indicato
	}

	/**
	 * Gestisce un controllo HTML "Select" (Combobox)
	 * formato del campo : cmb_nomeproprieta
	 * Esempio:
	 * $$cmb_tipoSoggettoBeneficiario
	 *
	 * In questo caso il "tipoSoggettoBeneficiario" è il nome della proprietà
	 * dell'oggetto e anche il nome dell'entità.
	 * Questo metodo accede ad un file di properties (config.properties) che
	 * contiene la query associata a "tipoSoggettoBeneficiario" per caricare i
	 * codici e le descrizioni corrispondenti. Quindi produce un combobox simile
	 * a questa:
	 *
	 *<pre><code>
	 * &lt;select name=&quot;tipoSoggettoBeneficiario&quot;&gt;
	 *   &lt;option value=&quot;D&quot; &gt;Due&lt;/option&gt;
	 *   &lt;option value=&quot;U&quot; &gt;Uno&lt;/option&gt;
	 *   &lt;option value=&quot;T&quot; &gt;Tre&lt;/option&gt;
	 *   &lt;option value=&quot;T2&quot; &gt;Tipol 2&lt;/option&gt;
	 *   &lt;option value=&quot;T1&quot; &gt;Tipol 1&lt;/option&gt;
	 * &lt;/select&gt;
	 *<code></pre>
	 *
	 * @param key la chiave. Per esempio rad_tipoCodice_tipoPartitaIva
	 * @param obj l'oggetto che contiene il valore selezionato come proprietà (esempio la HttpRequest)
	 * @param htmpl il gestore del layout
	 */
	private static void doCombo(String key, Object obj, Htmpl htmpl) {
		String property = key.substring(4);

		//Cambio nome metodo per gestire nome proprietà sulla direttiva
		// automatica di recupero parametri da request e valorizzazione VO
		property = ("" + property.charAt(0)).toLowerCase() + property.substring(1);

		// Modificato 11/12/2003...Se per qualche ragione non trovo + la modifica sbrano
		// chi l'ha toccata!!!!!.
		String baseProperty = property;

		if (baseProperty.indexOf("_") >= 0) {
			StringTokenizer st = new StringTokenizer(property, "_");
			if (st.hasMoreTokens()) {
				baseProperty = st.nextToken();
			}
		}

		boolean orderId = false;
		if (property.equalsIgnoreCase("tipiTipologiaNotifica")) {
			orderId = true;
		}

		String value = getPropertyValue(obj, baseProperty);

		String indPoint = null;
		if (obj instanceof HttpServletRequest) {
			indPoint = (String)((javax.servlet.http.HttpServletRequest)obj).
			getSession(false).getAttribute("pointTo");
		}
		else {
		}

		/** @@todo bset **/
		bset(htmpl, key, getCombo(property, value, indPoint, orderId), null);
		htmpl.set(key, getCombo(property, value, indPoint, orderId), null);
	}

	private static void doMultipleChoice(String key, Object obj, Htmpl htmpl) {
		String property = key.substring(4);
		String values[] = getPropertyValues(obj, property);
		String indPoint = (String)((javax.servlet.http.HttpServletRequest)obj).
		getSession(false).getAttribute("pointTo");
		/** @@todo bset **/
		bset(htmpl, key, getMultipleChoice(property, values, indPoint), null);
		htmpl.set(key, getMultipleChoice(property, values, indPoint), null);
	}


	private static void doSingleChoiceOrizzontale(String key, Object obj,
			Htmpl htmpl) {
		String property = key.substring(4);
		String value = getPropertyValue(obj, property);
		String indPoint = (String)((javax.servlet.http.HttpServletRequest)obj).
		getSession(false).getAttribute("pointTo");
		/** @@todo bset **/
		bset(htmpl, key, getSingleChoiceOrizzontale(property, value, indPoint), null);
		htmpl.set(key, getSingleChoiceOrizzontale(property, value, indPoint), null);
	}

	/**
	 * Gestisce il campo descrittivo selezionato da una combo o da una lista di
	 * valori (quelle definite nel file config.properties alla voci
	 * persistence.entity) Questo è utile quando vogliamo visualizzare il campo
	 * descrittivo associato ad un codice (attraverso una tavola di decodifica).
	 *
	 * @param key String
	 * @param obj Object
	 * @param htmpl Htmpl
	 */
	private static void doDescriptionLabel(String key, Object obj, Htmpl htmpl) {
		String property = key.substring(4);
		String value = getPropertyValue(obj, property);
		MapFactory mapFact = MapFactory.getInstance();
		Map<?,?> map = mapFact.getMap(property);
		/** @@todo bset **/
		bset(htmpl, key, String.valueOf(map.get(value)));
		htmpl.set(key, String.valueOf(map.get(value)));
	}

	/**
	 * Recupera una Map di codici/descrizioni e la trasforma in una lista di
	 * elementi HTML di tipo <option> con l'ausilio di un oggetto HTMPL.
	 *
	 * @param property la Map di coppie codice/descrizione
	 * @param selectedItem l'elemento della lista da evidenziare
	 * @param indPoint String
	 * @param orderId boolean
	 * @return String
	 */
	static String getCombo(String property, String selectedItem, String indPoint,
			boolean orderId) {
		StringBuffer combo = new StringBuffer();
		if (selectedItem == null || selectedItem.length() == 0) {
			selectedItem = "null";
		}

		// Modificato 11/12/2003...Se per qualche ragione non trovo + la modifica sbrano
		// chi l'ha toccata!!!!!.
		String baseProperty = property;
		boolean shrink = false;
		boolean noBlank = false;
		if (baseProperty.indexOf("_") >= 0) {
			StringTokenizer st = new StringTokenizer(property, "_");
			if (st.hasMoreTokens()) {
				baseProperty = st.nextToken();
				shrink = true;
				noBlank = true;
			}
		}

		if (baseProperty.indexOf(".") >= 0) {
			StringTokenizer st = new StringTokenizer(property, ".");
			orderId = true;
			if (st.hasMoreTokens()) {
				baseProperty = st.nextToken();
				shrink = true;
			}
		}

		combo.append("<select id=\"" + baseProperty + "\" name=\"" + baseProperty +
				"\"" + (shrink ? "" : " class=\"lista\"") + ">");
		// Fine Modifica!!!!!!!!!


		String option = "<option value=\"{0}\" {1}>{2}</option>";
		MapFactory mapFact = MapFactory.getInstance();
		//Map map = mapFact.getMap(baseProperty, orderId);
		Map<?,?> map = mapFact.getMapOrder(baseProperty, orderId);
		Iterator<?> iter = map.entrySet().iterator();

		//Borgogno 02/05/2003 - Aggiunge Elemento Blanck al Combo
		String key = "";
		String value = "";
		String selected = key.equals(selectedItem) ? "selected" : "";

		if (!noBlank) {
			combo.append(MessageFormat.format(option,
					new Object[] {htmlsp.process(key),
					selected, htmlsp.process(value)}));
		}
		//Borgogno 02/05/2003 - Aggiunge Elemento Blanck al Combo

		while (iter.hasNext()) {
			Map.Entry<?,?> entry = (Map.Entry<?,?>)iter.next();
			//Object entry = iter.next();

			Object obj = entry.getKey();
			if (obj instanceof String) {
				key = (String)obj;
			}
			else {
				key = ((Integer)obj).toString();
			}
			value = (String)entry.getValue();

			selected = key.equals(selectedItem) ? "selected" : "";
			combo.append(MessageFormat.format(option,
					new Object[] {htmlsp.process(key),
					selected, htmlsp.process(value)}
			));
		}

		combo.append("</select>");
		return combo.toString();
	}

	/**
	 * Recupera una Map di codici/descrizioni e la trasforma in una lista di
	 * elementi HTML a scelta multipla (checkbox).
	 *
	 * @param property la Map di coppie codice/descrizione
	 * @param selectedItems gli elementi selezionati
	 * @param indPoint String
	 * @return String
	 */
	static String getMultipleChoice(String property, String[] selectedItems,
			String indPoint) {
		if (selectedItems == null) {
			selectedItems = new String[0];
		}

		Collection<?> selectedColl = Arrays.asList(selectedItems);

		StringBuffer multiplechoice = new StringBuffer();

		String html =
			"<input name=\"{0}\" type=\"checkbox\" value=\"{1}\" {2}>{3}<br>\n";

		String checked = "checked";
		String unchecked = "";

		MapFactory mapFact = MapFactory.getInstance();
		Map<?,?> map = mapFact.getMap(property);
		Iterator<?> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<?,?> entry = (Map.Entry<?,?>)iter.next();

			String key = (String)entry.getKey();
			String value = (String)entry.getValue();

			multiplechoice.append(
					MessageFormat.format(html,
							new Object[] {
							property,
							key,
							selectedColl.contains(key) ? checked :
								unchecked,
								value}
					)
			);

		}

		return multiplechoice.toString();
	}

	/**
	 * Recupera una Map di codici/descrizioni e la trasforma in una lista di
	 * elementi HTML a scelta singola (radio).
	 *
	 * @param property la Map di coppie codice/descrizione
	 * @param selectedItem l'elemento selezionato
	 * @param indPoint String
	 * @return String
	 */
	static String getSingleChoice(String property, String selectedItem,
			String indPoint) {
		if (selectedItem == null) {
			selectedItem = "null";
		}

		StringBuffer multiplechoice = new StringBuffer();

		String html =
			"<input name=\"{0}\" type=\"radio\" value=\"{1}\" {2}>{3}<br>\n";

		String checked = "checked";
		String unchecked = "";

		MapFactory mapFact = MapFactory.getInstance();
		Map<?,?> map = mapFact.getMap(property);
		Iterator<?> iter = map.entrySet().iterator();

		while (iter.hasNext()) {

			Map.Entry<?,?> entry = (Map.Entry<?,?>)iter.next();

			String key = (String)entry.getKey();
			String value = (String)entry.getValue();

			multiplechoice.append(
					MessageFormat.format(html,
							new Object[] {
							property,
							key,
							selectedItem.equals(key) ? checked :
								unchecked,
								value}
					)
			);
		}

		return multiplechoice.toString();
	}


	static String getSingleChoiceOrizzontale(String property, String selectedItem,
			String indPoint) {
		if (selectedItem == null) {
			selectedItem = "null";
		}

		StringBuffer multiplechoice = new StringBuffer();

		String html =
			"<input name=\"{0}\" type=\"radio\" value=\"{1}\" {2}>{3}\n";

		String checked = "checked";
		String unchecked = "";

		MapFactory mapFact = MapFactory.getInstance();
		Map<?,?> map = mapFact.getMap(property);
		Iterator<?> iter = map.entrySet().iterator();

		while (iter.hasNext()) {

			Map.Entry<?,?> entry = (Map.Entry<?,?>)iter.next();

			String key = (String)entry.getKey();
			String value = (String)entry.getValue();

			multiplechoice.append(
					MessageFormat.format(html,
							new Object[] {
							property,
							key,
							selectedItem.equals(key) ? checked :
								unchecked,
								value}
					)
			);
		}

		return multiplechoice.toString();
	}


	/**
	 * Converte in una request string un oggetto, in modo che i dati in esso
	 * contenuti possano essere passati come parametro di request ad un
	 * componente web (JSP o Servlet) sulla sua richiesta GET.
	 *
	 * @param obj l'oggetto da ricodificare.
	 * @return i parametri da accodare sulla GET del componente web
	 */
	public static String toRequestString(Object obj) {
		if (obj == null) {
			return "";
		}

		StringBuffer reqString = new StringBuffer("?");

		Enumeration<?> enumeration = getPropertyNames(obj);
		while (enumeration.hasMoreElements()) {
			String name = (String)enumeration.nextElement();
			String[] values = getPropertyValues(obj, name);

			for (int i = 0; i < values.length; i++) {
				reqString.append(name)
				.append('=')
				.append(values[i])
				.append('&');
			}
		}
		reqString.setLength(reqString.length() - 1);
		return reqString.toString();
	}

	public String getImko() {
		return imko;
	}

	public void setImko(String imko) {
		HtmplUtil.imok = imko;
	}

	public String getImok() {
		return imok;
	}

	public void setImok(String imok) {
		HtmplUtil.imok = imok;
	}

	private static void bset(Htmpl htmpl, String p0, String p1,
			StringProcessor p2) {
		if (p1 != null) {
			htmpl.bset(p0, p1, p2);
		}
	}

	private static void bset(Htmpl htmpl, String p0, String p1) {
		if (p1 != null) {
			htmpl.bset(p0, p1);
		}
	}

	/**
	 * Permette di rimuovere l'hash di un metodo getTipi
	 * valorizzato dalla classe SingleTone MapFactory
	 *
	 * @param entita nome del postFisso del nome metodo
	 *                         get+TipiProva, entita=tipiProva //t minuscola
	 * @param obj request dell'applicativo
	 */
	public static void clearCachedEntity(String entita, Object obj) {
		MapFactory mapFact = MapFactory.getInstance();
		mapFact.clearCachedEntity(entita);
	}

	/**
	 * Permette di rimuovere tutto il contenuto
	 * valorizzato dalla classe SingleTone MapFactory
	 *
	 * @param obj request dell'applicativo
	 */
	public static void clearAllEntities(Object obj) {
		MapFactory mapFact = MapFactory.getInstance();
		mapFact.clearAllEntities();
	}

	/**
	 * Metodo che si occupa di settare gli errori all'interno di blocchi di codici HTML
	 * @param htmpl Htmpl
	 * @param errors ValidationErrors
	 * @param request HttpServletRequest
	 * @param application ServletContext
	 * @param htmlBlock String
	 */
	public static void setErrorsSpecifiedBlock(Htmpl htmpl,
			ValidationErrors errors,
			HttpServletRequest request,
			javax.servlet.ServletContext
			application, String htmlBlock) {
		if (null == errors || null == htmpl) { // evita i NullPointerException
			return;
		}

		pathToFollow = (String)(request.getSession(false).getAttribute(
		"pathToFollow"));

		// ottiene le variabili parsificando il layout
		Iterator<?> iter = htmpl.getVariableIterator();
		while (iter.hasNext()) {
			String key = (String)iter.next();
			if (key.startsWith("err_")) {
				doErrorSpecifiedBlock(key, errors, htmpl, request, application,
						htmlBlock);
			}
		}
	}

	/**
	 * Metodo che si occupa di valorizzare i messaggi e le icone di errore all'interno di un determinato blocco di codice HTML
	 * @param key String
	 * @param obj Object
	 * @param htmpl Htmpl
	 * @param request HttpServletRequest
	 * @param application ServletContext
	 * @param htmlBlock String
	 */
	private static void doErrorSpecifiedBlock(String key, Object obj, Htmpl htmpl,
			HttpServletRequest request,
			javax.servlet.ServletContext
			application, String htmlBlock) {
		ValidationErrors errors = (ValidationErrors)obj;
		String htmlStringKO =
			"<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" " +
			"title=\"{2}\" border=\"0\"></a>";
		String htmlStringOK = "<img src=\"{0}\" title=\"{1}\">";
		String popupError = "alert({0})";

		pathToFollow = (String)(request.getSession(false).getAttribute(
		"pathToFollow"));
		// Parametro che arriva dalla pagina grafica di RUPAR o SISTEMA PIEMONTE
		String activeSite = (String)(request.getSession(false).getAttribute(
		"activeSite"));
		String pathErrori = "";
		if (!Validator.isNotEmpty(activeSite)) {
			if (pathToFollow.equalsIgnoreCase("rupar")) {
				activeSite = application.getInitParameter("immaginiSviluppoRupar");
				pathErrori = application.getInitParameter("erroriRupar");
			}
			else if(pathToFollow.equalsIgnoreCase("sispie")){
				activeSite = application.getInitParameter("immaginiSviluppoSispie");
				pathErrori = application.getInitParameter("erroriSispie");
			}
			else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
				activeSite = application.getInitParameter("immaginiSviluppoTOBECONFIG");
				pathErrori = application.getInitParameter("erroriTOBECONFIG");
			}
		}

		String property = key.substring(4);
		ValidationError firstError = null;
		Iterator<?> errorIterator = errors.get(property);
		if (errorIterator != null) {
			firstError = (ValidationError)errorIterator.next();
		}

		if (key.equals("err_error")) {
			// messaggio di errore su pop-up
			if (firstError != null) {
				String message = "'" + jssp.process(firstError.getMessage()) + "'";
				bset(htmpl, htmlBlock + "." + key,
						MessageFormat.format(popupError, new Object[] {message}), null);
				htmpl.set(htmlBlock + "." + key,
						MessageFormat.format(popupError, new Object[] {message}), null);
			}
		}
		else {
			// messaggi di errore con iconcina
			if (firstError != null) {
				// c'è un errore
				String message = firstError.getMessage();
				bset(htmpl, htmlBlock + "." + key,
						MessageFormat.format(htmlStringKO,
								new Object[] {pathErrori + imko,
								"'" + jssp.process(firstError.getMessage()) +
								"'", message}), null);
				htmpl.set(htmlBlock + "." + key,
						MessageFormat.format(htmlStringKO,
								new Object[] {pathErrori +
								imko,
								"'" + jssp.process(firstError.getMessage()) +
								"'", message}), null);
			}
			else {
				bset(htmpl, htmlBlock + "." + key,
						MessageFormat.format(htmlStringOK,
								new Object[] {pathErrori + imok,
								""}), null);
				htmpl.set(htmlBlock + "." + key,
						MessageFormat.format(htmlStringOK,
								new Object[] {pathErrori +
								imok, ""}), null);
			}
		}
	}

	/**
	 * Metodo che si occupa di recuperare le proprietà e settare i valori,
	 * dell'oggetto passato come parametro, all'interno di uno specifico blocco di
	 * codice HTML
	 *
	 * @param htmpl Htmpl
	 * @param obj Object
	 * @param htmlBlock String
	 * @param valueNotInclude String
	 */
	public static void setValuesSpecifiedBlock(Htmpl htmpl, Object obj,
			String htmlBlock, String valueNotInclude) {
		if (null == obj || null == htmpl) { // evita i NullPointerException
			return;
		}
		if (obj instanceof HttpServletRequest) {
			pathToFollow = (String)((HttpServletRequest)obj).getSession(false).
			getAttribute("pathToFollow");
		}

		// Processa il layout alla ricerca di marcatori "speciali"
		// e li imposta il layout, gestendo i controlli
		Iterator<?> iter = htmpl.getVariableIterator();
		while (iter.hasNext()) {
			String key = (String)iter.next();
			/** @todo quì si potrebbe applicare il command pattern */
			if (key.startsWith("cmb_")) {
				doCombo(key, obj, htmpl);
			}
			else if (key.startsWith("des_")) {
				doDescriptionLabel(key, obj, htmpl);
			}
			else if (key.startsWith("rad_")) {
				doRadio(key, obj, htmpl);
			}
			else if (key.startsWith("mul_")) { // MultipleChoice (checkbox)
				doMultipleChoice(key, obj, htmpl);
			}
			else if (key.startsWith("sng_")) { // SingleChoice (radio)
				doSingleChoiceOrizzontale(key, obj, htmpl);
			}
			else {
				if(Validator.isNotEmpty(valueNotInclude)) {
					doTextFieldSpecifiedBlock(htmlBlock, key, obj, htmpl);
				}
				else {
					doTextFieldValueToExclude(htmlBlock, key, obj, htmpl, valueNotInclude);
				}
			}
		}
	}

	/**
	 * Metodo che si occupa di settare i valori all'interno dei campi text di un determinato blocco di codice HTML
	 * @param htmlBlock String
	 * @param key String
	 * @param obj Object
	 * @param htmpl Htmpl
	 */
	private static void doTextFieldSpecifiedBlock(String htmlBlock, String key, Object obj, Htmpl htmpl) {
		bset(htmpl, htmlBlock + "." + key, getPropertyValue(obj, key));
		htmpl.set(htmlBlock + "." + key, getPropertyValue(obj, key));
	}

	/**
	 * Metodo per settare i valori all'interno della view escludendo(cioè
	 * svuotando il texfield che si desidera)
	 *
	 * @param htmlBlock String
	 * @param key String
	 * @param obj Object
	 * @param htmpl Htmpl
	 * @param valueNotInclude String
	 */
	private static void doTextFieldValueToExclude(String htmlBlock, String key, Object obj, Htmpl htmpl, String valueNotInclude) {
		if(!htmlBlock.concat(".").concat(key).equalsIgnoreCase(valueNotInclude)) {
			bset(htmpl, htmlBlock + "." + key, getPropertyValue(obj, key));
			htmpl.set(htmlBlock + "." + key, getPropertyValue(obj, key));
		}
	}

	/**
	 * Metodo che si occupa di costruire tutti gli elementi relativi alla gestione della
	 * paginazione
	 * 
	 * @param paginaCorrente
	 * @param records
	 * @param htmpl
	 * @param request
	 * @param errors
	 * @param nomeBlocco
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object paginazionePerGruppi(int paginaCorrente, Object records, Htmpl htmpl, HttpServletRequest request, ValidationErrors errors, String nomeBlocco, javax.servlet.ServletContext application, Object filtriRicercaVO) {
		int c_pagine_per_gruppo = Integer.parseInt((String)SolmrConstants.NUM_PAGINE_PER_GRUPPO);
		int c_gruppi_sinistra = Integer.parseInt((String)SolmrConstants.NUM_GRUPPI_SX);
		int c_gruppi_destra = Integer.parseInt((String)SolmrConstants.NUM_GRUPPI_DX);

		int totRecord = 0;
		if(records instanceof Vector) {
			totRecord = ((Vector<?>)records).size();
		}
		else if(records instanceof StoricoParticellaVO[]) {
			totRecord = ((StoricoParticellaVO[])records).length;
		}
		int recordPerPagina = Integer.parseInt(SolmrConstants.NUMBER_RECORDS_FOR_PAGE_TERRENI);

		int totPagine = totRecord/recordPerPagina;
		int restoPagine = totRecord%recordPerPagina;
		if(restoPagine != 0) {
			totPagine++;
		}
		htmpl.set("numeroTotale", String.valueOf(totPagine));
		htmpl.set("pagina", (String)request.getAttribute("paginaCorrente"));
		if(records instanceof Vector) {
			htmpl.set(nomeBlocco+".numeroTotRecords", String.valueOf(((Vector<?>)records).size()));
		}
		else {
			htmpl.set(nomeBlocco+".numeroTotRecords", String.valueOf(((StoricoParticellaVO[])records).length));
		}
		if(errors == null || errors.size() == 0) {
			htmpl.set(nomeBlocco+".paginaCorrente", (String)request.getAttribute("paginaCorrente"));
		}
		else {
			htmpl.set(nomeBlocco+".paginaCorrente", (String)request.getParameter("paginaCorrente"));
			doErrorSpecifiedBlock("err_paginaCorrente", errors, htmpl, request, application, nomeBlocco);
		}
		htmpl.set(nomeBlocco+".numeroTotale", String.valueOf(totPagine));


		if(paginaCorrente < totPagine) {
			htmpl.newBlock(nomeBlocco+".avanti");
		}
		if(paginaCorrente > 1) {
			htmpl.newBlock(nomeBlocco+".indietro");
		}

		Vector<StoricoParticellaVO> newRecords = new Vector<StoricoParticellaVO>();
		TreeMap<Integer,Vector<?>> mapFiglio = new TreeMap<Integer,Vector<?>>();
		TreeMap<String,TreeMap<Integer,Vector<?>>> mapPadre = new TreeMap<String,TreeMap<Integer,Vector<?>>>();
		int a = 0;
		int b = 0;
		int c = 1;
		int y = 1;

		String keyPadre = (String)request.getSession().getAttribute("keyPadre");
		if(keyPadre == null) {
			keyPadre = "1";
		}
		if(records instanceof Vector) {
			Iterator<?> iter = ((Vector<?>)records).iterator();
			Vector<CodeDescription> elencoTitoliPossesso = new Vector<CodeDescription>();
			Vector<CodeDescription> elencoCasiParticolare = new Vector<CodeDescription>();
			Vector<TipoMacroUsoVO> elencoMacroUso = new Vector<TipoMacroUsoVO>();
			while(iter.hasNext()) {
				StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)iter.next();
				if(Validator.isNotEmpty(storicoParticellaVO.getIdCasoParticolare())) {
					elencoCasiParticolare.add(storicoParticellaVO.getCasoParticolare());
				}
				ConduzioneParticellaVO[] elencoConduzioneParticella = (ConduzioneParticellaVO[])storicoParticellaVO.getElencoConduzioni();
				ConduzioneDichiarataVO[] elencoConduzioneDichiarata = (ConduzioneDichiarataVO[])storicoParticellaVO.getElencoConduzioniDichiarate();
				if(elencoConduzioneParticella != null && elencoConduzioneParticella.length > 0) {
					for(int i = 0; i < elencoConduzioneParticella.length; i++) {
						ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO)elencoConduzioneParticella[i];
						elencoTitoliPossesso.add(conduzioneParticellaVO.getTitoloPossesso());
						if(conduzioneParticellaVO.getElencoUtilizzi() != null && conduzioneParticellaVO.getElencoUtilizzi()[0] != null && conduzioneParticellaVO.getElencoUtilizzi()[0].getIdUtilizzoParticella() != null && conduzioneParticellaVO.getElencoUtilizzi()[0].getIdUtilizzoParticella().longValue() > 0 && conduzioneParticellaVO.getElencoUtilizzi()[0].getTipoMacroUsoVO() != null) {
							elencoMacroUso.add(conduzioneParticellaVO.getElencoUtilizzi()[0].getTipoMacroUsoVO());
						}
					}
				}
				else if(elencoConduzioneDichiarata != null && elencoConduzioneDichiarata.length > 0) {
					for(int i = 0; i < elencoConduzioneDichiarata.length; i++) {
						ConduzioneDichiarataVO conduzioneDichiarataVO = (ConduzioneDichiarataVO)elencoConduzioneDichiarata[i]; 
						elencoTitoliPossesso.add(conduzioneDichiarataVO.getTitoloPossesso());
						if(conduzioneDichiarataVO.getElencoUtilizzi() != null && conduzioneDichiarataVO.getElencoUtilizzi()[0] != null && conduzioneDichiarataVO.getElencoUtilizzi()[0].getIdUtilizzoDichiarato() != null  && conduzioneDichiarataVO.getElencoUtilizzi()[0].getTipoMacroUsoVO() != null) {
							elencoMacroUso.add(conduzioneDichiarataVO.getElencoUtilizzi()[0].getTipoMacroUsoVO());
						}
					}
				}
				a++;
				if(a <= recordPerPagina) {
					newRecords.add(storicoParticellaVO);
				}
				if(a == recordPerPagina || (iter.hasNext() == false)) {
					a = 0;
					b++;
					mapFiglio.put(new Integer(y),newRecords);
					newRecords = new Vector<StoricoParticellaVO>();
					y++;
				}
				if((c == 1 && b == (c_pagine_per_gruppo-1)) || b == c_pagine_per_gruppo || (iter.hasNext() == false)) {
					b = 0;
					mapPadre.put(""+c,mapFiglio);
					mapFiglio = new TreeMap<Integer,Vector<?>>();
					c++;
				}
			}	  
			// Setto i totali delle superfici
			if(filtriRicercaVO instanceof FiltriParticellareRicercaVO) 
			{
				htmpl.set(nomeBlocco+".totSupCatastale", WebUtils.calcolaTotSupCatastale(((Vector<?>)records), (FiltriParticellareRicercaVO)request.getSession().getAttribute("filtriParticellareRicercaVO")));
				//htmpl.set(nomeBlocco+".totSupCondotta", WebUtils.calcolaTotSupCondotte(((Vector<?>)records), (FiltriParticellareRicercaVO)request.getSession().getAttribute("filtriParticellareRicercaVO")));
				htmpl.set(nomeBlocco+".totSuperficieGrafica", WebUtils.calcolaTotSuperficieGrafica(((Vector<?>)records), (FiltriParticellareRicercaVO)request.getSession().getAttribute("filtriParticellareRicercaVO")));
				/*if(Validator.isNotEmpty(((FiltriParticellareRicercaVO)filtriRicercaVO).getCheckUsoAgronomico()) && ((FiltriParticellareRicercaVO)filtriRicercaVO).getCheckUsoAgronomico().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
					htmpl.newBlock(nomeBlocco+".blkTotSupAgronomica");
					htmpl.set(nomeBlocco+".blkTotSupAgronomica.totSupAgronomica", WebUtils.calcolaTotSupAgronomiche(((Vector<?>)records), (FiltriParticellareRicercaVO)request.getSession().getAttribute("filtriParticellareRicercaVO")));
				}*/
				
        htmpl.set(nomeBlocco+".totSupAgronomica", WebUtils.calcolaTotSupAgronomiche(((Vector<?>)records), (FiltriParticellareRicercaVO)request.getSession().getAttribute("filtriParticellareRicercaVO")));
				htmpl.set(nomeBlocco+".totSupUtilizzata", WebUtils.calcolaTotSupUsoPrimario(((Vector<?>)records), (FiltriParticellareRicercaVO)request.getSession().getAttribute("filtriParticellareRicercaVO")));
				/*if(Validator.isNotEmpty(((FiltriParticellareRicercaVO)filtriRicercaVO).getCheckUsoSecondario()) && ((FiltriParticellareRicercaVO)filtriRicercaVO).getCheckUsoSecondario().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
					htmpl.newBlock(nomeBlocco+".blkTotSupUtilizzataSec");
					htmpl.set(nomeBlocco+".blkTotSupUtilizzataSec.totSupUtilizzataSecondaria", WebUtils.calcolaTotSupUsoSecondario(((Vector<?>)records), (FiltriParticellareRicercaVO)request.getSession().getAttribute("filtriParticellareRicercaVO")));
				}*/
				
				htmpl.set(nomeBlocco+".totSupUtilizzataSecondaria", WebUtils.calcolaTotSupUsoSecondario(((Vector<?>)records), (FiltriParticellareRicercaVO)request.getSession().getAttribute("filtriParticellareRicercaVO")));
			}

			// LEGENDA
			Vector<CodeDescription> elencoCodiciPossesso = StringUtils.getLegenda(elencoTitoliPossesso);
			Vector<CodeDescription> elencoCodiciParticolari = null;
			if(elencoCasiParticolare != null && elencoCasiParticolare.size() > 0) {
				elencoCodiciParticolari = StringUtils.getLegenda(elencoCasiParticolare);
				for(int i = 0; i < elencoCodiciParticolari.size(); i++) {
					CodeDescription code = (CodeDescription)elencoCodiciParticolari.elementAt(i);
					htmpl.newBlock(nomeBlocco+".blkLegendaCasiParticolari");
					htmpl.set(nomeBlocco+".blkLegendaCasiParticolari.idCasoParticolare", String.valueOf(code.getCode())+" - ");
					if(i == (elencoCodiciParticolari.size() - 1)) {
						htmpl.set(nomeBlocco+".blkLegendaCasiParticolari.descCasoParticolare", code.getDescription());
					}
					else {
						htmpl.set(nomeBlocco+".blkLegendaCasiParticolari.descCasoParticolare", code.getDescription()+", ");
					}
				}
			}
			for(int i = 0; i < elencoCodiciPossesso.size(); i++) {
				CodeDescription code = (CodeDescription)elencoCodiciPossesso.elementAt(i);
				htmpl.newBlock(nomeBlocco+".blkLegendaConduzione");
				htmpl.set(nomeBlocco+".blkLegendaConduzione.idTitoloPossesso", String.valueOf(code.getCode())+" - ");
				if(i == (elencoCodiciPossesso.size() - 1)) {
					htmpl.set(nomeBlocco+".blkLegendaConduzione.descTitoloPossesso", code.getDescription());
				}
				else {
					htmpl.set(nomeBlocco+".blkLegendaConduzione.descTitoloPossesso", code.getDescription()+", ");
				}
			}
			if(elencoMacroUso != null && elencoMacroUso.size() > 0) 
			{
				TreeMap<String,String> elencoMacro = StringUtils.getAndSortLegenda(elencoMacroUso);
				Set<String> elencoKeys = elencoMacro.keySet();
				Iterator<String> itera = elencoKeys.iterator();
				int contatore = 0;
				while(itera.hasNext()) {
					String key = (String)itera.next();
					String descrizione = (String)elencoMacro.get(key);
					htmpl.newBlock(nomeBlocco+".blkLegendaMacroUso");
					htmpl.set(nomeBlocco+".blkLegendaMacroUso.codice", key+" - ");
					if(contatore == (elencoMacro.size() - 1)) {
						htmpl.set(nomeBlocco+".blkLegendaMacroUso.descMacroUso", descrizione);
					}
					else {
						htmpl.set(nomeBlocco+".blkLegendaMacroUso.descMacroUso", descrizione+", ");
					}
					contatore++;
				}
			}
		}
		else if(records instanceof StoricoParticellaVO[]) {
			for(int i = 0; i < ((StoricoParticellaVO[])records).length; i++) {
				StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)((StoricoParticellaVO[])records)[i];
				a++;
				if(a <= recordPerPagina) {
					newRecords.add(storicoParticellaVO);
				}
				if(a == recordPerPagina || i ==  ((StoricoParticellaVO[])records).length - 1) {
					a = 0;
					b++;
					mapFiglio.put(new Integer(y),newRecords);
					newRecords = new Vector<StoricoParticellaVO>();
					y++;
				}
				if((c == 1 && b == (c_pagine_per_gruppo-1)) || b == c_pagine_per_gruppo || i ==  ((StoricoParticellaVO[])records).length - 1) {
					b = 0;
					mapPadre.put(""+c,mapFiglio);
					mapFiglio = new TreeMap<Integer,Vector<?>>();
					c++;
				}
			}
			htmpl.set(nomeBlocco+".totSupCatastale", WebUtils.calcolaTotSupCatastaleUnar((StoricoParticellaVO[])records));
			htmpl.set(nomeBlocco+".totArea", WebUtils.calcolaTotSupVitata((StoricoParticellaVO[])records, ((FiltriUnitaArboreaRicercaVO)filtriRicercaVO)));
			//htmpl.set(nomeBlocco+".totSuperficeDaIscrivereAlbo", WebUtils.calcolaTotSupDaIscrivereAlbo((StoricoParticellaVO[])records, ((FiltriUnitaArboreaRicercaVO)filtriRicercaVO)));
		}

		Set<?> setKeyPadre = mapPadre.keySet();
		Iterator<?> iterPadre = setKeyPadre.iterator();
		boolean esci = false;
		while(iterPadre.hasNext()){
			keyPadre = (String)iterPadre.next();
			mapFiglio = (TreeMap<Integer,Vector<?>>)mapPadre.get(keyPadre);
			Set<Integer> setKeyFiglio = mapFiglio.keySet();
			Iterator<Integer> iterFiglio = setKeyFiglio.iterator();
			while(iterFiglio.hasNext()){
				Integer keyFiglio = (Integer)iterFiglio.next();
				if(paginaCorrente == keyFiglio.intValue()){
					newRecords = (Vector)mapFiglio.get(keyFiglio);
					esci = true;
					break;
				}
			}
			if(esci)
				break;
		}

		Set<?> setKeyFiglio = mapFiglio.keySet();
		Iterator<?> iterFiglio = setKeyFiglio.iterator();

		int z = 1;
		while(iterFiglio.hasNext()) {
			Integer keyFiglio = (Integer)iterFiglio.next();
			if(Integer.parseInt(keyPadre) > 1 && z < c_gruppi_sinistra) {
				int j = c_gruppi_sinistra;
				boolean primo = true;
				while(z <= c_gruppi_sinistra) {
					int nextPage = keyFiglio.intValue();
					if(nextPage >= (j * c_pagine_per_gruppo)) {
						if(primo && Integer.parseInt(keyPadre) <= 3) {
							nextPage -= (j * c_pagine_per_gruppo - 1);
						}
						else {
							nextPage -= (j * c_pagine_per_gruppo );
						}
						htmpl.newBlock(nomeBlocco+".blkGruppoPagine");
						htmpl.newBlock(nomeBlocco+".blkGruppoPagine.blkGruppoSuccessivo");
						htmpl.set(nomeBlocco+".blkGruppoPagine.pagina",""+nextPage);
						htmpl.set(nomeBlocco+".blkGruppoPagine.nextPage",""+nextPage);
						String descTooltip = getDescrizioneTooltip(mapPadre,nextPage);
						htmpl.set(nomeBlocco+".blkGruppoPagine.descPagina",descTooltip);
						primo = false;
					}
					j--;
					z++;
				}
			}
			htmpl.newBlock(nomeBlocco+".blkGruppoPagine");
			htmpl.set(nomeBlocco+".blkGruppoPagine.pagina",keyFiglio.toString());
			htmpl.set(nomeBlocco+".blkGruppoPagine.nextPage",keyFiglio.toString());
			String descTooltip = getDescrizioneTooltip(mapPadre,keyFiglio.intValue());
			htmpl.set(nomeBlocco+".blkGruppoPagine.descPagina",descTooltip);
			if(paginaCorrente == keyFiglio.intValue()){
				htmpl.newBlock(nomeBlocco+".blkGruppoPagine.blkPaginaCorrente");
				newRecords = (Vector)mapFiglio.get(keyFiglio);
			}
			if(keyFiglio.intValue() == (c_pagine_per_gruppo * Integer.parseInt(keyPadre))-1)
			{
				int x = 1;
				int nextPage = keyFiglio.intValue()+x;
				if(nextPage <= totPagine){
					htmpl.newBlock(nomeBlocco+".blkGruppoPagine");
					htmpl.newBlock(nomeBlocco+".blkGruppoPagine.blkGruppoSuccessivo");
					htmpl.set(nomeBlocco+".blkGruppoPagine.pagina",""+nextPage);
					htmpl.set(nomeBlocco+".blkGruppoPagine.nextPage",""+nextPage);
					descTooltip = getDescrizioneTooltip(mapPadre,nextPage);
					htmpl.set(nomeBlocco+".blkGruppoPagine.descPagina",descTooltip);
					while(x < c_gruppi_destra){
						nextPage += c_pagine_per_gruppo;
						if(nextPage <= totPagine){
							htmpl.newBlock(nomeBlocco+".blkGruppoPagine");
							htmpl.newBlock(nomeBlocco+".blkGruppoPagine.blkGruppoSuccessivo");
							htmpl.set(nomeBlocco+".blkGruppoPagine.pagina",""+nextPage);
							htmpl.set(nomeBlocco+".blkGruppoPagine.nextPage",""+nextPage);
							descTooltip = getDescrizioneTooltip(mapPadre,nextPage);
							htmpl.set(nomeBlocco+".blkGruppoPagine.descPagina",descTooltip);
						}
						else{
							break;
						}
						x++;
					}
				}
			}
		}
		request.getSession().setAttribute("keyPadre",keyPadre);
		if(records instanceof StoricoParticellaVO[]) {
			return (StoricoParticellaVO[])newRecords.toArray(new StoricoParticellaVO[newRecords.size()]);
		}
		else {
			return newRecords;
		}
	}

	/**
	 * Metodo che si occupa, sfruttando la reflection, di visualizzare un tooltip,
	 * definito nell'object passato come parametro di input, che indica il primo
	 * oggetto nella pagina che si intenderà visualizzare
	 * 
	 * @param mapPadre
	 * @param nextPage
	 * @param obj
	 * @return
	 */
	private static String getDescrizioneTooltip(TreeMap<?,?> mapPadre,int nextPage){
		String descTooltip = "";
		Set<?> setKeyPadre = mapPadre.keySet();
		Iterator<?> iterPadre = setKeyPadre.iterator();
		boolean esci = false;
		String keyPadre = "";
		Collection<?> newRecords = new ArrayList<Collection<?>>();
		while(iterPadre.hasNext()){
			keyPadre = (String)iterPadre.next();
			TreeMap<?,?> mapFiglio = (TreeMap<?,?>)mapPadre.get(keyPadre);
			Set<?> setKeyFiglio = mapFiglio.keySet();
			Iterator<?> iterFiglio = setKeyFiglio.iterator();
			while(iterFiglio.hasNext()){
				Integer keyFiglio = (Integer)iterFiglio.next();
				if(nextPage == keyFiglio.intValue()){
					newRecords = (Collection<?>)mapFiglio.get(keyFiglio);
					esci = true;
					break;
				}
			}
			if(esci)
				break;
		}
		try{
			Iterator<?> itera = newRecords.iterator();
			if(itera.hasNext()) {
				Object obj = (Object)itera.next();
				java.lang.reflect.Method m = obj.getClass().getMethod("getDescForTooltip", (Class[])null);
				descTooltip = (String)m.invoke(obj,(Object[])null);
			}
		}
		catch(Exception ex){
		}
		return descTooltip;
	}

	/**
	 * Metodo per creare i campi da sostituire alle variabili delle attestazioni
	 * 
	 * @param parametriAttAziendaVO
	 * @param parametriAttDichiarataVO
	 * @param idPianoRiferimento
	 * @param tipoAttestazioneVO
	 * @param tipoParametriAttestazioneVO
	 * @param elencoErrori
	 * @param pathErrori
	 * @param reload
	 * @param request
	 * @return java.lang.String
	 */
	public static String creaLayoutForAttestazioni(ParametriAttAziendaVO parametriAttAziendaVO, ParametriAttDichiarataVO parametriAttDichiarataVO, Long idPianoRiferimento, TipoAttestazioneVO tipoAttestazioneVO, TipoParametriAttestazioneVO tipoParametriAttestazioneVO, Hashtable<?,?> elencoErrori, String pathErrori, boolean reload, HttpServletRequest request) {
		try { 
			StringBuffer sb = new StringBuffer(tipoAttestazioneVO.getDescrizione());
			while(sb.toString().toUpperCase().lastIndexOf("##") != -1) {
				String nomeParametro = "";
				String codiceHtml = "";
				int start = sb.toString().indexOf("##");
				int end = sb.toString().indexOf(" ", start);
				if(end != -1) {
					nomeParametro = sb.substring(start, end);
				}
				else {
					nomeParametro = sb.substring(start);
				}
				nomeParametro = StringUtils.replace(nomeParametro, "##", "");
				Object valore = null;
				// Piano di riferimento "in lavorazione"
				if(idPianoRiferimento.longValue() == 0) {
					if(parametriAttAziendaVO != null) {
						valore = parametriAttAziendaVO.getClass().getMethod("get" +org.apache.commons.lang.StringUtils.capitalize(nomeParametro), (Class[])null).invoke(parametriAttAziendaVO, (Object[])null);
					}
					else {
						valore = "";
					}
				}
				else {
					if(parametriAttDichiarataVO != null) {
						valore = parametriAttDichiarataVO.getClass().getMethod("get" +org.apache.commons.lang.StringUtils.capitalize(nomeParametro), (Class[])null).invoke(parametriAttDichiarataVO, (Object[])null);
					}
					else {
						valore = "";
					}
				}
				if(tipoParametriAttestazioneVO != null) {
					Object tipoParametro = tipoParametriAttestazioneVO.getClass().getMethod("get" +org.apache.commons.lang.StringUtils.capitalize(nomeParametro), (Class[])null).invoke(tipoParametriAttestazioneVO, (Object[])null);
					if(Validator.isNotEmpty((String)tipoParametro)) {
						if(reload) {
							valore = request.getParameter(nomeParametro+tipoAttestazioneVO.getIdAttestazione());
						}
						if(((String)tipoParametro).equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_DATA)) {
							codiceHtml = "<input name=\""+nomeParametro+tipoAttestazioneVO.getIdAttestazione()+"\" id=\""+nomeParametro+tipoAttestazioneVO.getIdAttestazione()+"\" class=\"textmedio\" type=\"text\" value=\""+valore+"\" maxlength=\"10\" />";
							if(elencoErrori != null && elencoErrori.get(tipoAttestazioneVO.getIdAttestazione()) != null) {
								it.csi.solmr.dto.StringcodeDescription messaggio = (it.csi.solmr.dto.StringcodeDescription)elencoErrori.get(tipoAttestazioneVO.getIdAttestazione());
								if(messaggio.getCode().equalsIgnoreCase(nomeParametro)) {
									codiceHtml += "<a href=\"#\"><img src=\""+pathErrori + imko+"\" onClick=\"alert('"+messaggio.getDescription()+"')\" title=\""+messaggio.getDescription()+"\" border=\"0\"></a>";
								}
							}
						}
						else if(((String)tipoParametro).equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_NUMERICO)) {
							codiceHtml = "<input name=\""+nomeParametro+tipoAttestazioneVO.getIdAttestazione()+"\" id=\""+nomeParametro+tipoAttestazioneVO.getIdAttestazione()+"\" class=\"textmedio\" type=\"text\" value=\""+valore+"\" maxlength=\"10\"/>";
							if(elencoErrori != null && elencoErrori.get(tipoAttestazioneVO.getIdAttestazione()) != null) {
								it.csi.solmr.dto.StringcodeDescription messaggio = (it.csi.solmr.dto.StringcodeDescription)elencoErrori.get(tipoAttestazioneVO.getIdAttestazione());
								if(messaggio.getCode().equalsIgnoreCase(nomeParametro)) {
									codiceHtml += "<a href=\"#\"><img src=\""+pathErrori + imko+"\" onClick=\"alert('"+messaggio.getDescription()+"')\" title=\""+messaggio.getDescription()+"\" border=\"0\"></a>";
								}
							}
						}
						else if(((String)tipoParametro).equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO)) {
							codiceHtml = "<input name=\""+nomeParametro+tipoAttestazioneVO.getIdAttestazione()+"\" id=\""+nomeParametro+tipoAttestazioneVO.getIdAttestazione()+"\" class=\"text\" type=\"text\" value=\""+valore+"\" />";
							if(elencoErrori != null && elencoErrori.get(tipoAttestazioneVO.getIdAttestazione()) != null) {
								it.csi.solmr.dto.StringcodeDescription messaggio = (it.csi.solmr.dto.StringcodeDescription)elencoErrori.get(tipoAttestazioneVO.getIdAttestazione());
								if(messaggio.getCode().equalsIgnoreCase(nomeParametro)) {
									codiceHtml += "<a href=\"#\"><img src=\""+pathErrori + imko+"\" onClick=\"alert('"+messaggio.getDescription()+"')\" title=\""+messaggio.getDescription()+"\" border=\"0\"></a>";
								}
							}
						}
						else if(((String)tipoParametro).equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO_MULTILINE)) {
							codiceHtml = "<textarea name=\""+nomeParametro+tipoAttestazioneVO.getIdAttestazione()+"\" id=\""+nomeParametro+tipoAttestazioneVO.getIdAttestazione()+"\" cols=\"85\" rows=\"4\" class=\"textmedio\" />"+valore+"</textarea>";
							if(elencoErrori != null && elencoErrori.get(tipoAttestazioneVO.getIdAttestazione()) != null) {
								it.csi.solmr.dto.StringcodeDescription messaggio = (it.csi.solmr.dto.StringcodeDescription)elencoErrori.get(tipoAttestazioneVO.getIdAttestazione());
								if(messaggio.getCode().equalsIgnoreCase(nomeParametro)) {
									codiceHtml += "<a href=\"#\"><img src=\""+pathErrori + imko+"\" onClick=\"alert('"+messaggio.getDescription()+"')\" title=\""+messaggio.getDescription()+"\" border=\"0\"></a>";
								}
							}
						}
						else if(((String)tipoParametro).equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO_READ_ONLY)) {
							codiceHtml = "<input name=\""+nomeParametro+tipoAttestazioneVO.getIdAttestazione()+"\" id=\""+nomeParametro+tipoAttestazioneVO.getIdAttestazione()+"\" class=\"text\" type=\"text\" value=\""+valore+"\" readOnly=\"readOnly\" />";
							if(elencoErrori != null && elencoErrori.get(tipoAttestazioneVO.getIdAttestazione()) != null) {
								it.csi.solmr.dto.StringcodeDescription messaggio = (it.csi.solmr.dto.StringcodeDescription)elencoErrori.get(tipoAttestazioneVO.getIdAttestazione());
								if(messaggio.getCode().equalsIgnoreCase(nomeParametro)) {
									codiceHtml += "<a href=\"#\"><img src=\""+pathErrori + imko+"\" onClick=\"alert('"+messaggio.getDescription()+"')\" title=\""+messaggio.getDescription()+"\" border=\"0\"></a>";
								}
							}
						}
					}
				}
				if(valore != null) {
					if(end != -1) {
						sb.replace(start, end, codiceHtml);
					}
					else {
						sb.replace(start, sb.length(), codiceHtml);
					}
				}
				else {
					if(end != -1) {
						sb.replace(start, end, codiceHtml);
					}
					else {
						sb.replace(start, sb.length(), codiceHtml);
					}
				}
			}
			return sb.toString();
		}
		catch(NoSuchMethodException nme) {
			return "";
		}
		catch(IllegalAccessException iae) {
			return "";
		}
		catch(InvocationTargetException ite) {
			return "";
		}
	}
	
	
	/**
   * Metodo che si occupa di costruire tutti gli elementi relativi alla gestione della
   * paginazione
   * 
   * @param paginaCorrente
   * @param records
   * @param htmpl
   * @param request
   * @param errors
   * @param nomeBlocco
   * @param obj
   * @return
   */
  public static Object paginazionePerGruppiTrasfAzienda(int paginaCorrente, Object records, Htmpl htmpl, HttpServletRequest request, ValidationErrors errors, String nomeBlocco, javax.servlet.ServletContext application) {
    int c_pagine_per_gruppo = Integer.parseInt((String)SolmrConstants.NUM_PAGINE_PER_GRUPPO);
    int c_gruppi_sinistra = Integer.parseInt((String)SolmrConstants.NUM_GRUPPI_SX);
    int c_gruppi_destra = Integer.parseInt((String)SolmrConstants.NUM_GRUPPI_DX);

    int totRecord = 0;
    
    totRecord = ((Vector<?>)records).size();
    
    
    int recordPerPagina = Integer.parseInt(SolmrConstants.NUMBER_RECORDS_FOR_PAGE_TRASFERIMENTO_AZIENDA);

    int totPagine = totRecord/recordPerPagina;
    int restoPagine = totRecord%recordPerPagina;
    if(restoPagine != 0) {
      totPagine++;
    }
    htmpl.set("numeroTotale", String.valueOf(totPagine));
    htmpl.set("pagina", (String)request.getAttribute("paginaCorrente"));
    htmpl.set(nomeBlocco+".numeroTotRecords", String.valueOf(((Vector<?>)records).size()));
    
    if(errors == null || errors.size() == 0) {
      htmpl.set(nomeBlocco+".paginaCorrente", (String)request.getAttribute("paginaCorrente"));
    }
    else {
      htmpl.set(nomeBlocco+".paginaCorrente", (String)request.getParameter("paginaCorrente"));
      doErrorSpecifiedBlock("err_paginaCorrente", errors, htmpl, request, application, nomeBlocco);
    }
    htmpl.set(nomeBlocco+".numeroTotale", String.valueOf(totPagine));


    if(paginaCorrente < totPagine) {
      htmpl.newBlock(nomeBlocco+".avanti");
    }
    if(paginaCorrente > 1) {
      htmpl.newBlock(nomeBlocco+".indietro");
    }

    Vector<AnagAziendaVO> newRecords = new Vector<AnagAziendaVO>();
    TreeMap<Integer,Vector<AnagAziendaVO>> mapFiglio = new TreeMap<Integer,Vector<AnagAziendaVO>>();
    TreeMap<String,TreeMap<Integer,Vector<AnagAziendaVO>>> mapPadre = new TreeMap<String,TreeMap<Integer,Vector<AnagAziendaVO>>>();
    int a = 0;
    int b = 0;
    int c = 1;
    int y = 1;

    String keyPadre = (String)request.getSession().getAttribute("keyPadre");
    if(keyPadre == null) {
      keyPadre = "1";
    }
    if(records instanceof Vector) {
      Iterator<?> iter = ((Vector<?>)records).iterator();
      while(iter.hasNext()) {
        AnagAziendaVO anagVO = (AnagAziendaVO)iter.next();
        
        a++;
        if(a <= recordPerPagina) {
          newRecords.add(anagVO);
        }
        if(a == recordPerPagina || (iter.hasNext() == false)) {
          a = 0;
          b++;
          mapFiglio.put(new Integer(y),newRecords);
          newRecords = new Vector<AnagAziendaVO>();
          y++;
        }
        if((c == 1 && b == (c_pagine_per_gruppo-1)) || b == c_pagine_per_gruppo || (iter.hasNext() == false)) {
          b = 0;
          mapPadre.put(""+c,mapFiglio);
          mapFiglio = new TreeMap<Integer,Vector<AnagAziendaVO>>();
          c++;
        }
      }   
    }

    Set<?> setKeyPadre = mapPadre.keySet();
    Iterator<?> iterPadre = setKeyPadre.iterator();
    boolean esci = false;
    while(iterPadre.hasNext()){
      keyPadre = (String)iterPadre.next();
      mapFiglio = (TreeMap<Integer,Vector<AnagAziendaVO>>)mapPadre.get(keyPadre);
      Set<?> setKeyFiglio = mapFiglio.keySet();
      Iterator<?> iterFiglio = setKeyFiglio.iterator();
      while(iterFiglio.hasNext()){
        Integer keyFiglio = (Integer)iterFiglio.next();
        if(paginaCorrente == keyFiglio.intValue()){
          newRecords = (Vector<AnagAziendaVO>)mapFiglio.get(keyFiglio);
          esci = true;
          break;
        }
      }
      if(esci)
        break;
    }

    Set<?> setKeyFiglio = mapFiglio.keySet();
    Iterator<?> iterFiglio = setKeyFiglio.iterator();

    int z = 1;
    while(iterFiglio.hasNext()) {
      Integer keyFiglio = (Integer)iterFiglio.next();
      if(Integer.parseInt(keyPadre) > 1 && z < c_gruppi_sinistra) {
        int j = c_gruppi_sinistra;
        boolean primo = true;
        while(z <= c_gruppi_sinistra) {
          int nextPage = keyFiglio.intValue();
          if(nextPage >= (j * c_pagine_per_gruppo)) {
            if(primo && Integer.parseInt(keyPadre) <= 3) {
              nextPage -= (j * c_pagine_per_gruppo - 1);
            }
            else {
              nextPage -= (j * c_pagine_per_gruppo );
            }
            htmpl.newBlock(nomeBlocco+".blkGruppoPagine");
            htmpl.newBlock(nomeBlocco+".blkGruppoPagine.blkGruppoSuccessivo");
            htmpl.set(nomeBlocco+".blkGruppoPagine.pagina",""+nextPage);
            htmpl.set(nomeBlocco+".blkGruppoPagine.nextPage",""+nextPage);
            String descTooltip = getDescrizioneTooltip(mapPadre,nextPage);
            htmpl.set(nomeBlocco+".blkGruppoPagine.descPagina",descTooltip);
            primo = false;
          }
          j--;
          z++;
        }
      }
      htmpl.newBlock(nomeBlocco+".blkGruppoPagine");
      htmpl.set(nomeBlocco+".blkGruppoPagine.pagina",keyFiglio.toString());
      htmpl.set(nomeBlocco+".blkGruppoPagine.nextPage",keyFiglio.toString());
      String descTooltip = getDescrizioneTooltip(mapPadre,keyFiglio.intValue());
      htmpl.set(nomeBlocco+".blkGruppoPagine.descPagina",descTooltip);
      if(paginaCorrente == keyFiglio.intValue()){
        htmpl.newBlock(nomeBlocco+".blkGruppoPagine.blkPaginaCorrente");
        newRecords = (Vector<AnagAziendaVO>)mapFiglio.get(keyFiglio);
      }
      if(keyFiglio.intValue() == (c_pagine_per_gruppo * Integer.parseInt(keyPadre))-1)
      {
        int x = 1;
        int nextPage = keyFiglio.intValue()+x;
        if(nextPage <= totPagine){
          htmpl.newBlock(nomeBlocco+".blkGruppoPagine");
          htmpl.newBlock(nomeBlocco+".blkGruppoPagine.blkGruppoSuccessivo");
          htmpl.set(nomeBlocco+".blkGruppoPagine.pagina",""+nextPage);
          htmpl.set(nomeBlocco+".blkGruppoPagine.nextPage",""+nextPage);
          descTooltip = getDescrizioneTooltip(mapPadre,nextPage);
          htmpl.set(nomeBlocco+".blkGruppoPagine.descPagina",descTooltip);
          while(x < c_gruppi_destra){
            nextPage += c_pagine_per_gruppo;
            if(nextPage <= totPagine){
              htmpl.newBlock(nomeBlocco+".blkGruppoPagine");
              htmpl.newBlock(nomeBlocco+".blkGruppoPagine.blkGruppoSuccessivo");
              htmpl.set(nomeBlocco+".blkGruppoPagine.pagina",""+nextPage);
              htmpl.set(nomeBlocco+".blkGruppoPagine.nextPage",""+nextPage);
              descTooltip = getDescrizioneTooltip(mapPadre,nextPage);
              htmpl.set(nomeBlocco+".blkGruppoPagine.descPagina",descTooltip);
            }
            else{
              break;
            }
            x++;
          }
        }
      }
    }
    request.getSession().setAttribute("keyPadre",keyPadre);
    
    return newRecords;
    
  }

}



