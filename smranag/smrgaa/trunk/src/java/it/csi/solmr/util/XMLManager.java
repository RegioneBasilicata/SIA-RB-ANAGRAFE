package it.csi.solmr.util;

/**
 * <p>Title: Sian</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TOBECONFIG</p>
 * @author
 * @version 1.0
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Classe di gestione del file XML contenente le query
 */
public class XMLManager {
	private static XMLManager instance = null;
	private static Hashtable<Object,Object> htConstants = null;
	private static Context ctx = null;

	/**
	 * Carica il file query.xml e lo rende disponibile per l'ambiente BEA
	 */
	protected static final void init() {
		try {			
						
			ResourceBundle res = ResourceBundle.getBundle("config");
			String CONSTANTS_RESOURCE_REFERENCE = res.getString("sianConstants");
			
			Document doc = creaDocumentDaXML(CONSTANTS_RESOURCE_REFERENCE);
			Hashtable<Object,Object> constants = new Hashtable<Object,Object>();
			leggiXml(doc,constants, "constant");
			htConstants = constants;
		}
		catch(NamingException ne) {
			ne.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crea il Document effettuando il parsing del file query.xml
	 *
	 * @param resourceName
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws Exception
	 */
	private static Document creaDocumentDaXML(String resourceName) throws IOException, Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = new FileInputStream(resourceName);
		org.w3c.dom.Document doc=db.parse(is);
		is.close();
		is = null;
		return doc;
	}

	/**
	 * Legge il file XML
	 *
	 * @param doc
	 * @param query
	 * @throws IOException
	 * @throws Exception
	 */
	private static void leggiXml(org.w3c.dom.Document doc, Hashtable<Object,Object> data, String strXmlSource) throws IOException, Exception {
		NodeList list = null;
		list = doc.getElementsByTagName(strXmlSource);
		Node node = null;
		int listLength = list ==null ? 0 :list.getLength();
		for(int i = 0; i < listLength; i++) {
			node = list.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				NodeList listFigli=node.getChildNodes();
				Node nodeFiglio,nodeValue;
				/**
				 * Per ogni nodo vado a leggermi tutti i figli
				 */
				String name = null, value = null;
				for(int j = 0; j <listFigli.getLength(); j++) {
					nodeFiglio = listFigli.item(j);
					if(nodeFiglio.getNodeType() == Node.ELEMENT_NODE) {
						if("name".equals(nodeFiglio.getNodeName())) {
							nodeValue = nodeFiglio.getFirstChild();
							if(nodeValue != null) {
								name = nodeValue.getNodeValue();
							}
						}
						if("value".equals(nodeFiglio.getNodeName())) {
							nodeValue = nodeFiglio.getFirstChild();
							if(nodeValue != null) {
								value = nodeValue.getNodeValue();
							}
						}
					}
				}
				if(name != null && value != null) {
					data.put(name, value);
				}
			}
		}
	}

	protected static Hashtable<Object,Object> getHashtableConstants() {
		return htConstants;
	}

	/**
	 * Crea un'istanza della classe
	 *
	 * @return
	 */
	protected static XMLManager getInstance() {
		if(instance == null) {
			try {
				instance = new XMLManager();
				ctx = new InitialContext();
				init();
			}
			catch(NamingException ne) {
				ne.printStackTrace();
			}
		}
		return instance;
	}
}
