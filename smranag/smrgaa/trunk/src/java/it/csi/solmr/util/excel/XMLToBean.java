package it.csi.solmr.util.excel;


import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import jxl.LabelCell;
import jxl.write.DateFormat;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;

import org.jdom.Attribute;
import org.jdom.Element;


public class XMLToBean {

	private XmlParser xml;


	public XMLToBean(InputStream in) {
		XmlParser xml = new XmlParser(in);
		this.xml = xml;
	}

	/**
	 * Metodo che si occupa di recuperare le labels dei filtri di ricerca o delle intestazioni
	 * che compongono il file excel più quelle della tabella contenente i risultati
	 * che si vogliono visualizzare nel brogliaccio
	 *
	 * @param tableName
	 * @param startIndex
	 * @param sheet
	 * @return jxl.write.Label[]
	 * @throws Exception
	 */
	public Label[] getLabels(String tableName, int startIndex, WritableSheet sheet) throws Exception {
		SolmrLogger.debug(this, "Invocating method getLabels in XMLToBean\n");
		Vector<Object> v_result = new Vector<Object>();
		try {
			List<?> columnList = getColumnList(tableName);
			SolmrLogger.debug(this, "Table  : " + tableName );
			Element tabHeader = getTableHeader(tableName);

			for(int i = 0; i < columnList.size(); i++) {
				Element e = (Element)columnList.get(i);
				String colName = getNameLabel(e);
				// Setta il formato delle Intestazioni
				WritableFont title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
				WritableCellFormat titleFormat = new WritableCellFormat(title);
				LabelCell label = null;
				if(tabHeader != null) 
				{
					// Sono nel foglio excel relativo all'elenco dei documenti
					if(tableName.equalsIgnoreCase("elencoDocumenti")) 
					{
						titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
						titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
						label = new Label(i, startIndex + 4, colName, titleFormat);
					}
					else 
					{						
						// Se sono nel foglio excel relativo alla funzione di menù "titoli"
						if(tableName.equalsIgnoreCase("elencoTitoli")) 
						{
							// Setto la parte grafica delle colonne
							title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
							titleFormat = new WritableCellFormat(title);
							titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
							titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
							titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
							if(colName.equalsIgnoreCase("Identificativo")) {
								label = new Label(i, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i, 3 + tabHeader.getChildren("bean").size(), i, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Valore (Euro)")) {
								label = new Label(i, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i, 3 + tabHeader.getChildren("bean").size(), i, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Superficie (ha)")) {
								label = new Label(i, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i, 3 + tabHeader.getChildren("bean").size(), i, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Uba obbl.")) {
								titleFormat.setWrap(true);
								label = new Label(i, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i, 3 + tabHeader.getChildren("bean").size(), i, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Tipo")) {
								label = new Label(i, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i, 3 + tabHeader.getChildren("bean").size(), i, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Origine")) {
								label = new Label(i, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i, 3 + tabHeader.getChildren("bean").size(), i, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Movimento")) {
								label = new Label(i, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i, 3 + tabHeader.getChildren("bean").size(), i + 2, 3 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Codice")) {
								label = new Label(i - 1, 4 + tabHeader.getChildren("bean").size(), colName, titleFormat);
							}
							else if(colName.equalsIgnoreCase("Data")) {
								label = new Label(i - 1, 4 + tabHeader.getChildren("bean").size(), colName, titleFormat);
							}
							else if(colName.equalsIgnoreCase("Validazione")) {
								label = new Label(i - 1, 4 + tabHeader.getChildren("bean").size(), colName, titleFormat);
							}
							else if(colName.equalsIgnoreCase("Data ultimo util.")) {
								titleFormat.setWrap(true);
								label = new Label(i - 1, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i - 1, 3 + tabHeader.getChildren("bean").size(), i - 1, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Data fine possesso")) {
								titleFormat.setWrap(true);
								label = new Label(i - 1, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i - 1, 3 + tabHeader.getChildren("bean").size(), i - 1, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Stato")) {
								label = new Label(i - 1, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i - 1, 3 + tabHeader.getChildren("bean").size(), i - 1, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Cuaa proprietario")) {
								titleFormat.setWrap(true);
								label = new Label(i - 1, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i - 1, 3 + tabHeader.getChildren("bean").size(), i - 1, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Cuaa Soccidario")) {
								titleFormat.setWrap(true);
								label = new Label(i - 1, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i - 1, 3 + tabHeader.getChildren("bean").size(), i - 1, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Identificativo titolo fraz.")) {
								titleFormat.setWrap(true);
								label = new Label(i - 1, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i - 1, 3 + tabHeader.getChildren("bean").size(), i - 1, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Zona primo util.")) {
								titleFormat.setWrap(true);
								label = new Label(i - 1, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i - 1, 3 + tabHeader.getChildren("bean").size(), i - 1, 4 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Anno campagna")) {
								label = new Label(i - 1, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						        sheet.mergeCells(i - 1, 3 + tabHeader.getChildren("bean").size(), i, 3 + tabHeader.getChildren("bean").size());
							}
							else if(colName.equalsIgnoreCase("Inizio")) {
								titleFormat.setWrap(true);
								label = new Label(i - 2, 4 + tabHeader.getChildren("bean").size(), colName, titleFormat);
							}
							else if(colName.equalsIgnoreCase("Fine")) {
								titleFormat.setWrap(true);
								label = new Label(i - 2, 4 + tabHeader.getChildren("bean").size(), colName, titleFormat);
							}
						}
						//Gestione report Riepilogo e validazioni con tab-header
						else if(tableName.equalsIgnoreCase("elencoMandatiValidazioni")){
							title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
							titleFormat = new WritableCellFormat(title);
							titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
							titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
							titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

							label = new Label(i, 2, colName, titleFormat);
						}
						else if(tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediario")){
							title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
							titleFormat = new WritableCellFormat(title);
							titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
							titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
							titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

							label = new Label(i, 2, colName, titleFormat);
						}
						else if(tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediarioForProvincia")){
							title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
							titleFormat = new WritableCellFormat(title);
							titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
							titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
							titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

							label = new Label(i, 2, colName, titleFormat);
						}
						else {
							titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
							label = new Label(i, 3 + tabHeader.getChildren("bean").size(), colName, titleFormat);
						}
					}
				}
				else {
					//Gestione report Riepilogo e validazioni
					if(tableName.equalsIgnoreCase("elencoMandatiValidazioni")){
						title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
						titleFormat = new WritableCellFormat(title);
						titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
						titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
						titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

						label = new Label(i, 0, colName, titleFormat);
					}
					else if(tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediario")){
						title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
						titleFormat = new WritableCellFormat(title);
						titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
						titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
						titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

						label = new Label(i, 0, colName, titleFormat);
					}
					else if(tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediarioForProvincia")){
						title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
						titleFormat = new WritableCellFormat(title);
						titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
						titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
						titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

						label = new Label(i, 0, colName, titleFormat);
					}
					else
					label = new Label(i, 0, colName, titleFormat);
				}
				v_result.add(label);
			}
		}
		catch (Exception ex) {
			SolmrLogger.error(this, "Catching Exception in method getLabels in XMLToBean with this message: "+ex.getMessage()+"\n");
			throw new Exception(ex.getMessage());
		}
		Label[] result = new Label[v_result.size()];
		v_result.toArray(result);
		SolmrLogger.debug(this, "Invocated method getLabels in XMLToBean\n");
		return result;
	}

	/**
	 * Metodo che si occupa di settare i valori relativi ai fogli excel: restituisce
	 * un array di obj generici in modo da poter permettere la restituzione di tipologie
	 * celle differenti(Label per le stringhe, Number per i valori numerici)
	 *
	 * - Aggiunta gestione scarico report 'Riepilogo mandati e validazioni'
	 * 		@author 71554
	 *
	 * @param tableName String
	 * @param a_vo Collection
	 * @param nomeBrogliaccio String
	 * @param startIndex int
	 * @return Object[]
	 */
	public Object[] setVOValues(String tableName, Collection<Object> a_vo, String nomeBrogliaccio, int startIndex) 
	{
		SolmrLogger.debug(this, "Invocating method setVOValues in XMLToBean\n");
		Vector<Object> v = null;
		try 
		{
			List<?> columnList = getColumnList(tableName);
			Element tabHeader = getTableHeader(tableName);
			// Ora non usato servirà nel caso per la leggenda
			//Element tabFooter = getTableFooter(tableName);

			v = new Vector<Object>();
			Iterator<Object> iter = a_vo.iterator();
			WritableCellFormat a = new WritableCellFormat();
			WritableCellFormat aCenter = new WritableCellFormat();
			NumberFormat n = new NumberFormat("#####0.0000");
			NumberFormat n1 = new NumberFormat("#####0");
			NumberFormat n4 = new NumberFormat(SolmrConstants.NUMBER_FORMAT_4);
			NumberFormat ef = new NumberFormat(SolmrConstants.NUMBER_EURO_FORMAT);
			NumberFormat df = new NumberFormat(SolmrConstants.NUMBER_DOUBLE_FORMAT);
			DateFormat dateF = new DateFormat(SolmrConstants.DATE_EUROPE_FORMAT);
			WritableCellFormat a4 = new WritableCellFormat(n4);
			WritableCellFormat b = new WritableCellFormat(n);
			WritableCellFormat b1 = new WritableCellFormat(n1);
			WritableCellFormat euroFormat = new WritableCellFormat(ef);
			WritableCellFormat doubleFormat = new WritableCellFormat(df);
			WritableCellFormat dateFormat = new WritableCellFormat(dateF);
			for(int j = 0; j < a_vo.size(); j++) 
			{
				Object vo = iter.next();
				if(tabHeader != null) 
				{
					if(j == 0) 
					{
						Attribute aInnerObject = tabHeader.getAttribute("innerObject");
						if(aInnerObject != null) 
						{
							//Ë un inner object
							String methodName = "get" +org.apache.commons.lang.StringUtils.capitalize(tabHeader.getAttribute("property").getValue());
							Object innerObject = vo.getClass().getMethod(methodName, ((Class[])null)).invoke(vo, (Object[])null);
							//ora ciclo sui figli bean di tab-header e prelevo da innerObject le properties di interesse
							List<?> chTabHeaders = tabHeader.getChildren("bean");
							if(chTabHeaders != null) 
							{
								// Setto la prima intestazione, quella relativa al nome del brogliaccio, e quella con i dati dell'azienda
                // Setta il formato delle Intestazioni
                WritableFont title = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
                WritableCellFormat titleFormat = new WritableCellFormat(title);
                Label primaIntestazione = null;
                if (tableName.equalsIgnoreCase("elencoMandatiValidazioni") 
                    || tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediario") 
                    || tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediarioForProvincia"))
                {	;
                }
                else 
                {
                	primaIntestazione = new Label(0, 0, nomeBrogliaccio + " " +DateUtils.getCurrentDateString(), titleFormat);
                }
                v.add(primaIntestazione);
                // Scarico da menù brogliaccio,da menù titoli
                if(tableName.equalsIgnoreCase("elencoTitoli")) 
                {
                	for(int z = 0; z < chTabHeaders.size(); z++) 
                	{
                		// Recupero la label dal file XML
                		StringBuffer txtLabel = new StringBuffer();
                  	Element _tmpBean = (Element)chTabHeaders.get(z);
                  	String labelVal = _tmpBean.getAttributeValue("label");
                  	txtLabel.append(labelVal);
                  	// Recupero la proprietà dell'oggetto per valorizzare la label del
                  	// filtro dall'XML
                  	StringBuffer txtValue = new StringBuffer();
                		Element _tmpBeanProperty = (Element)chTabHeaders.get(z);
                		String methName = "get" + org.apache.commons.lang.StringUtils.capitalize(_tmpBeanProperty.getAttributeValue("property"));
                		Object val = innerObject.getClass().getMethod(methName, (Class[])null).invoke(innerObject, (Object[])null);
                		txtValue.append(val);
                    // Valorizzo label filtro/intestazione
  									WritableFont titleLable = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
  									WritableCellFormat titleFormatLabel = new WritableCellFormat(titleLable);
  									Label labelIntestazione = new Label(0, z + 2, txtLabel.toString(), titleFormatLabel);
  									v.add(labelIntestazione);
  									// Valorizzo valore filtro/intestazione
  									WritableFont valueTitle = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
  									WritableCellFormat valueTitleFormat = new WritableCellFormat(valueTitle);
  									Label valueIntestazione = new Label(1, z + 2, txtValue.toString(),valueTitleFormat);
  									v.add(valueIntestazione);
  				        }
                }
                else if(tableName.equalsIgnoreCase("elencoMandatiValidazioni") 
                     || tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediario") 
                     || tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediarioForProvincia"))
                {
              		StringBuffer txtLabel = new StringBuffer();
                	Element _tmpBean = (Element)chTabHeaders.get(0);
                	String labelVal = _tmpBean.getAttributeValue("label");
                	txtLabel.append(labelVal);
                	// Recupero la proprietà dell'oggetto per valorizzare la label del
                	// filtro dall'XML
                	StringBuffer txtValue = new StringBuffer();
              		Element _tmpBeanProperty = (Element)chTabHeaders.get(0);
              		String methName = "get" + org.apache.commons.lang.StringUtils.capitalize(_tmpBeanProperty.getAttributeValue("property"));
              		Object val = innerObject.getClass().getMethod(methName, (Class[])null).invoke(innerObject, (Object[])null);
              		txtValue.append(val);
  			          // Valorizzo label filtro/intestazione valore
  								WritableFont titleLable = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
  								WritableCellFormat titleFormatLabel = new WritableCellFormat(titleLable);
  								Label labelIntestazioneValue = new Label(0, 0, txtLabel.toString() + txtValue.toString(), titleFormatLabel);
  								v.add(labelIntestazioneValue);
  			        }
							}
							// Foglio excel relativo all'elenco dei documenti
							if(tableName.equalsIgnoreCase("elencoDocumenti")) 
							{
								WritableFont title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
								WritableCellFormat titleFormat = new WritableCellFormat(title);
								Label primaIntestazione = new Label(0, 0, nomeBrogliaccio, titleFormat);
								v.add(primaIntestazione);
								StringBuffer txtLabel = null;
								StringBuffer txtValue = null;
								int valore = 0;
								for(int z = 0; z < chTabHeaders.size(); z++) 
								{
									// Label del filtro
									txtLabel = new StringBuffer();
									Element _tmpBean = (Element)chTabHeaders.get(z);
									String labelVal = _tmpBean.getAttributeValue("label");
									txtLabel.append(labelVal);

									// Valori del filtro
									txtValue = new StringBuffer();
									String methName = "get" + org.apache.commons.lang.StringUtils.capitalize(_tmpBean.getAttributeValue("property"));
									Object val = innerObject.getClass().getMethod(methName, (Class[])null).invoke(innerObject, (Object[])null);
									txtValue.append(val);
									// Se il filtro è stato valorizzato lo visualizzo
									if(val != null) 
									{
										if(z == 0) {
											valore = z + 2;
										}
										else {
											valore++;
										}
										// sezione label filtro
										WritableFont titleLable = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
										WritableCellFormat titleFormatLabel = new WritableCellFormat(titleLable);
										Label labelIntestazione = new Label(0, valore, txtLabel.toString(), titleFormatLabel);
										v.add(labelIntestazione);

										// sezione valore filtro
										WritableFont valueTitle = new WritableFont(WritableFont.ARIAL, 10);
										WritableCellFormat valueTitleFormat = new WritableCellFormat(valueTitle);
										Label valueIntestazione = new Label(1, valore, txtValue.toString(),valueTitleFormat);
										v.add(valueIntestazione);
									}
								}
							}
						}
					}
				}
				else 
				{
					if(tableName.equalsIgnoreCase("elencoSianTerritorio")) 
					{
						WritableFont title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
						WritableCellFormat titleFormat = new WritableCellFormat(title);
						Label primaIntestazione = new Label(0, 0, nomeBrogliaccio, titleFormat);
						v.add(primaIntestazione);
					}
				}
				for(int i = 0; i < columnList.size(); i++) 
				{
					Element e = (Element)columnList.get(i);
					String propertyName = e.getTextTrim();
					Method m = null;
					try {
						m = vo.getClass().getMethod("get" +org.apache.commons.lang.StringUtils.capitalize(propertyName), (Class[])null);
					}
					catch(NoSuchMethodException nfe) {
						// Se non viene trovato il metodo non faccio nulla
						// perchè il propertyName richiamato potrebbe riferirsi solo
						// ad una macrolonna del file excel e non ad un attributo da visualizzare
					}
					if(m != null) 
					{
						Object obj = m.invoke(vo, (Object[])null);
						SolmrLogger.debug(this,"Invocation method [get" + propertyName + "]");
						Label l = null;
						jxl.write.Number number = null;
						jxl.write.DateTime date = null;
						if(tabHeader != null) {
							// Foglio excel relativo all'elenco dei documenti
							if(tableName.equalsIgnoreCase("elencoDocumenti")) 
							{
								a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
								l = new Label(i, startIndex + j + 5, obj == null ? "" : obj.toString(), a);
							}
							else 
							{
								// Foglio excel relativo ai titoli riforma PAC singoli
								if(tableName.equalsIgnoreCase("elencoTitoli")) {
									switch(i) {
										case 0:
											a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
											a.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
											l = new Label(i, j + 7, obj == null ? "" : obj.toString(), a);
											break;
										case 1:
											if(Validator.isNotEmpty(obj)) {
												euroFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												euroFormat.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												euroFormat.setAlignment(jxl.format.Alignment.CENTRE);
												euroFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												number = new jxl.write.Number(i, j + 7, Double.parseDouble(obj.toString()), euroFormat);
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i, j + 7, "", aCenter);
											}
											break;
										case 2:
											if(Validator.isNotEmpty(obj)) {
												b.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												b.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												b.setAlignment(jxl.format.Alignment.CENTRE);
												b.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												number = new jxl.write.Number(i, j + 7, Double.parseDouble(obj.toString().replace(',', '.')), b);
												break;
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i, j + 7, "", aCenter);
											}
											break;
										case 3:
											if(Validator.isNotEmpty(obj)) {
												doubleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												doubleFormat.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												doubleFormat.setAlignment(jxl.format.Alignment.CENTRE);
												doubleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												number = new jxl.write.Number(i, j + 7, Double.parseDouble(obj.toString().replace(',', '.')), doubleFormat);
												break;
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i, j + 7, "", aCenter);
											}
											break;
										case 4:
											if(Validator.isNotEmpty(obj)) {
												a4.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												a4.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												a4.setAlignment(jxl.format.Alignment.CENTRE);
												a4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												number = new jxl.write.Number(i, j + 7, Double.parseDouble(obj.toString().replace(',', '.')), a4);
												break;
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i, j + 7, "", aCenter);
											}
											break;
										case 5:
											if(Validator.isNotEmpty(obj)) {
												a4.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												a4.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												a4.setAlignment(jxl.format.Alignment.CENTRE);
												a4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												number = new jxl.write.Number(i, j + 7, Double.parseDouble(obj.toString().replace(',', '.')), a4);
												break;
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i, j + 7, "", aCenter);
											}
											break;
										case 7:
											a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
											a.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
											l = new Label(i - 1, j + 7, obj == null ? "" : obj.toString(), a);
											break;
										case 8:
											if(Validator.isNotEmpty(obj)) {
												dateFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												dateFormat.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												dateFormat.setAlignment(jxl.format.Alignment.CENTRE);
												dateFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												date = new jxl.write.DateTime(i - 1, j + 7, DateUtils.parseDate(obj.toString()), dateFormat);
												break;
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i - 1, j + 7, "", aCenter);
											}
											break;
										case 9:
											a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
											a.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
											l = new Label(i - 1, j + 7, obj == null ? "" : obj.toString(), a);
											break;
										case 10:
											if(Validator.isNotEmpty(obj)) {
												dateFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												dateFormat.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												dateFormat.setAlignment(jxl.format.Alignment.CENTRE);
												dateFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												date = new jxl.write.DateTime(i - 1, j + 7, DateUtils.parseDate(obj.toString()), dateFormat);
												break;
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i - 1, j + 7, "", aCenter);
											}
											break;
										case 11:
											if(Validator.isNotEmpty(obj)) {
												dateFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												dateFormat.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												dateFormat.setAlignment(jxl.format.Alignment.CENTRE);
												dateFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												date = new jxl.write.DateTime(i - 1, j + 7, DateUtils.parseDate(obj.toString()), dateFormat);
												break;
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i - 1, j + 7, "", aCenter);
											}
											break;
										case 12:
											a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
											a.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
											l = new Label(i - 1, j + 7, obj == null ? "" : obj.toString(), a);
											break;
										case 13:
											a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
											a.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
											l = new Label(i - 1, j + 7, obj == null ? "" : obj.toString(), a);
											break;
										case 14:
											a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
											a.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
											l = new Label(i - 1, j + 7, obj == null ? "" : obj.toString(), a);
											break;
										case 15:
											if(Validator.isNotEmpty(obj)) {
												a4.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												a4.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												a4.setAlignment(jxl.format.Alignment.CENTRE);
												a4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												number = new jxl.write.Number(i - 1, j + 7, Double.parseDouble(obj.toString().replace(',', '.')), a4);
												break;
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i - 1, j + 7, "", aCenter);
											}
											break;
										case 16:
											if(Validator.isNotEmpty(obj)) {
												a4.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												a4.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												a4.setAlignment(jxl.format.Alignment.CENTRE);
												a4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												number = new jxl.write.Number(i - 1, j + 7, Double.parseDouble(obj.toString().replace(',', '.')), a4);
												break;
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i - 1, j + 7, "", aCenter);
											}
											break;
										case 18:
											if(Validator.isNotEmpty(obj)) {
												a4.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												a4.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												a4.setAlignment(jxl.format.Alignment.CENTRE);
												a4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												number = new jxl.write.Number(i - 2, j + 7, Double.parseDouble(obj.toString()), a4);
												break;
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i - 2, j + 7, "", aCenter);
											}
											break;
										case 19:
											if(Validator.isNotEmpty(obj) && Validator.isNotEmpty(obj.toString())) {
												a4.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												a4.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												a4.setAlignment(jxl.format.Alignment.CENTRE);
												a4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												number = new jxl.write.Number(i - 2, j + 7, Double.parseDouble(obj.toString()), a4);
												break;
											}
											else {
												aCenter.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												aCenter.setFont(new jxl.write.WritableFont(jxl.write.WritableFont.ARIAL, 8));
												aCenter.setAlignment(jxl.format.Alignment.CENTRE);
												aCenter.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
												l = new Label(i - 2, j + 7, "", aCenter);
											}
											break;
									}
								}
//								Riepilogo mandati utenti intermediari con tab-header
								if(tableName.equalsIgnoreCase("elencoMandatiValidazioni")){
											if(i == 5 || i == 6){
												if(Validator.isNotEmpty(obj)) {
													b1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
													number = new jxl.write.Number(i, j + 3, Double.parseDouble(obj.toString().replace(',', '.')), b1);
												}
												else{
													a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
													l = new Label(i, j + 3, obj == null ? "" : obj.toString(),a);
												}
											}
											else{
												a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												l = new Label(i, j + 3, obj == null ? "" : obj.toString(),a);
											}
								}
								//Riepilogo mandati utenti NON intermediari con tab-header
								else if(tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediario")){
									if(i == 2 || i == 3){
										if(Validator.isNotEmpty(obj)) {
											b1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
											number = new jxl.write.Number(i, j + 3, Double.parseDouble(obj.toString().replace(',', '.')), b1);
										}
										else{
											a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
											l = new Label(i, j + 3, obj == null ? "" : obj.toString(),a);
										}
									}
									else{
										a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
										l = new Label(i, j + 3, obj == null ? "" : obj.toString(),a);
									}
								}
								//Riepilogo mandati utenti NON intermediari divisi x provincia con tab-header
								else if(tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediarioForProvincia")){
									if(i == 3 || i == 4){
										if(Validator.isNotEmpty(obj)) {
											b1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
											number = new jxl.write.Number(i, j + 3, Double.parseDouble(obj.toString().replace(',', '.')), b1);
										}
										else{
											a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
											l = new Label(i, j + 3, obj == null ? "" : obj.toString(),a);
										}
									}
									else{
										a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

										l = new Label(i, j + 3, obj == null ? "" : obj.toString(),a);
									}
								}
							}
						}
						else {
							//Riepilogo mandati utenti intermediari
							if(tableName.equalsIgnoreCase("elencoMandatiValidazioni")){
										if(i == 5 || i == 6){
											if(Validator.isNotEmpty(obj)) {
												b1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												number = new jxl.write.Number(i, j + 1, Double.parseDouble(obj.toString().replace(',', '.')), b1);
											}
											else{
												a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
												l = new Label(i, j + 1, obj == null ? "" : obj.toString(),a);
											}
										}
										else{
											a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
											l = new Label(i, j + 1, obj == null ? "" : obj.toString(),a);
										}
							}
							//Riepilogo mandati utenti NON intermediari
							else if(tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediario")){
								if(i == 2 || i == 3){
									if(Validator.isNotEmpty(obj)) {
										b1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
										number = new jxl.write.Number(i, j + 1, Double.parseDouble(obj.toString().replace(',', '.')), b1);
									}
									else{
										a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
										l = new Label(i, j + 1, obj == null ? "" : obj.toString(),a);
									}
								}
								else{
									a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
									l = new Label(i, j + 1, obj == null ? "" : obj.toString(),a);
								}
							}
							//Riepilogo mandati utenti NON intermediari
							else if(tableName.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediarioForProvincia")){
								if(i == 3 || i == 4){
									if(Validator.isNotEmpty(obj)) {
										b1.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
										number = new jxl.write.Number(i, j + 1, Double.parseDouble(obj.toString().replace(',', '.')), b1);
									}
									else{
										a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
										l = new Label(i, j + 1, obj == null ? "" : obj.toString(),a);
									}
								}
								else{
									a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
									if(i==1){//Centro la sigla della provincia
										SolmrLogger.debug(this, "Centro provincia " + i);
										a.setAlignment(jxl.format.Alignment.CENTRE);
									}
									l = new Label(i, j + 1, obj == null ? "" : obj.toString(),a);
								}
							}
							else if(tableName.equalsIgnoreCase("elencoSianTerritorio")) {
								if(i == 8) {
									if(Validator.isNotEmpty(obj)) {
										b.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
										number = new jxl.write.Number(i, startIndex + j + 2, Double.parseDouble(obj.toString().replace(',', '.')), b);
									}
									else {
										a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
										l = new Label(i, startIndex + j + 2, obj == null ? "" : obj.toString(),a );
									}
								}
								else {
									a.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
									l = new Label(i, startIndex + j + 2, obj == null ? "" : obj.toString(), a);
								}
							}
							else {
								l = new Label(i, j + 1, obj == null ? "" : obj.toString());
							}
						}
						if(l != null) {
							v.add(l);
						}
						else if(number != null){
							v.add(number);
						}
						else {
							v.add(date);
						}
					}
				}
			}
			
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		Object[] a_l = new Object[v.size()];
		v.toArray(a_l);
		return a_l;
	}

	public Element getTableHeader(String tableName) throws Exception {
		Element eTable = getTableElement(tableName);
		return eTable.getChild("tab-header");
	}

	@SuppressWarnings("unchecked")
	public List<Element> getColumnList(String tableName) throws Exception {
		SolmrLogger.debug(this, "Invocating method getColumnList in XMLToBean\n");
		Element eTable = getTableElement(tableName);
		Element eBean = getBeanElement(eTable);
		List<Element> columnList = eBean.getChildren("column");
		SolmrLogger.debug(this, "Invocated method getColumnList in XMLToBean\n");
		return columnList;
	}


	public List<String> getWidthList(List<Element> columnList) throws Exception {
		List<String> elencoWidht = new ArrayList<String>();
		for(int i = 0; i < columnList.size(); i++) {
			Element e = (Element)columnList.get(i);
			String width = getWidthLabel(e);
			if(width != null) {
				elencoWidht.add(width);
			}
		}
		return elencoWidht;
	}


	/**
	 *
	 * @param eBean Element
	 * @return java.lang.String
	 */
	private String getNameLabel(Element eBean) {
		return eBean.getAttribute("name").getValue();
	}


	/**
	 * Metodo per settare la larghezza delle labels
	 * @param column
	 * @return
	 */
	public String getWidthLabel(Element column) {
		if(Validator.isNotEmpty(column.getAttribute("width"))) {
			return column.getAttribute("width").getValue();
		}
		else {
			return null;
		}
	}

	/**
	 * Metodo per recuperare la larghezza delle colonne dei filtri
	 *
	 * @param tableName String
	 * @return String
	 * @throws Exception
	 */
	public List<String> getWidthLabelFiltri(String tableName) throws Exception {
		SolmrLogger.debug(this, "Invocating method getWidthLabelFiltri in XMLToBean\n");
		List<String> elencoWidht = new ArrayList<String>();
		Element tabHeader = getTableHeader(tableName);
		if(tabHeader != null) {
			List<?> chTabHeaders = tabHeader.getChildren("bean");
			Element _tmpBean = (Element)chTabHeaders.get(0);
			if(Validator.isNotEmpty(_tmpBean.getAttribute("width"))) {
				elencoWidht.add(_tmpBean.getAttribute("width").getValue());
			}
		}
		SolmrLogger.debug(this, "Invocated method getWidthLabelFiltri in XMLToBean\n");
		return elencoWidht;
	}


	/**
	 *
	 * @param eTable Element
	 * @return org.jdom.Element
	 */
	private Element getBeanElement(Element eTable) {
		Element eBean = xml.getElement(eTable, "bean");
		return eBean;
	}


	/**
	 *
	 * @param tableName String
	 * @return org.jdom.Element
	 * @throws Exception
	 */
	private Element getTableElement(String tableName) throws Exception {
		Element eTable = xml.getRootChild("table", true, new AttributeList().addAttribute("name", tableName));
		return eTable;
	}

}
