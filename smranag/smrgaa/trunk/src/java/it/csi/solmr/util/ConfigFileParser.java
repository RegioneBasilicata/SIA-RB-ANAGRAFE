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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigFileParser extends DefaultHandler {
  private String configFileURL = null;
  private String pointTo       = null;
  private HashMap<Object,Object> mapping      = null;
  private Vector<Object> getAccepted   = null;
  private String defCtrl       = null;
  private String errorPage     = null;
  private HashMap<Object,Object> noLogin      = null;

  public ConfigFileParser() {
    this(SolmrConstants.FILE_CONF_CLIENT);
  }

  public ConfigFileParser(String configFileURL) {
    if (SolmrLogger.isDebugEnabled(this))
      SolmrLogger.debug(this, "Reading configuration file "+configFileURL+"...");
    this.configFileURL = configFileURL;
    mapping = new HashMap<Object,Object>();
    getAccepted = new Vector<Object>();
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating(true);
    noLogin=new HashMap<Object,Object>();
    try {
      InputStream is = this.getClass().getResourceAsStream(configFileURL);
      if (is == null) {
	throw new IOException(SolmrErrors.EXC_CONF_FILE_NF);
      }
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse( is, this);
    } catch (ParserConfigurationException ex) {
    } catch (SAXException ex) {
    } catch (IOException ex) {
      SolmrLogger.fatal(this, ex);
    }
  }

  public void endDocument() throws SAXException {
    if (SolmrLogger.isDebugEnabled(this))
      SolmrLogger.debug(this, "Finish document parsing");
  }

  public void startDocument() throws SAXException {
    if (SolmrLogger.isDebugEnabled(this))
      SolmrLogger.debug(this, "Start document parsing: file "+configFileURL+"...");
  }

  public void error(SAXParseException e) throws SAXException {
    SolmrLogger.error(this, e);
  }

  public void startElement(String namespaceURI,
			   String lName,
                           String qName,
                           Attributes attrs) throws SAXException {
    String eName = lName;
    if ("".equals(eName)) eName = qName;

    if (eName.equals("property")) {
      if (attrs != null) {
	pointTo = attrs.getValue("pointTo");
      }
    }

    if (eName.equals("mapping")) {
      if (attrs != null) {
	mapping.put(attrs.getValue("layout"), attrs.getValue("controller"));
      }
    }

    if (eName.equals("get-accepted")) {
      if (attrs != null) {
	getAccepted.add(attrs.getValue("requested"));
      }
    }

    if (eName.equals("default-ctrl")) {
      if (attrs != null) {
	defCtrl = attrs.getValue("controller");
      }
    }

    if (eName.equals("error-page")) {
      if (attrs != null) {
	errorPage = attrs.getValue("page");
      }
    }

    if (eName.equals("no-login")) {
      if (attrs != null) {
        String page=attrs.getValue("page");
        noLogin.put(page,page);
      }
    }

  }

  public String getAttributePointTo() {
    return pointTo;
  }

  public HashMap<Object,Object> getMapping() {
    return mapping;
  }

  public Vector<Object> getGetAccepted() {
    return getAccepted;
  }

  public String getDefaultController() {
    return defCtrl;
  }

  public String getErrorPage() {
    return errorPage;
  }

  public HashMap<Object,Object> getNoLogin()
  {
    return noLogin;
  }

}
