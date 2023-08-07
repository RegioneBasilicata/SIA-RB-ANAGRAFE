package it.csi.solmr.client.services;

/**
 *
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.solmr.dto.services.CommandVO;
import it.csi.solmr.dto.services.ServiceVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.SolmrLogger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ServicesBuilder extends DefaultHandler {
  private static ServicesBuilder sf = null;
  private HashMap<Object,Object> theServices = new HashMap<Object,Object>();
  private HashMap<Object,Object> theProperties = new HashMap<Object,Object>();
  private String configFileURL = null;
 
  /**
   * Costruisce un'istanza di ServicesBuilder utilizzando il file di default
   * definito dalla costante <code>SolmrConstants.get("FILE_SERVICES")</code>
   * @throws Exception
   */
  private ServicesBuilder() throws Exception {
    this((String)SolmrConstants.get("FILE_SERVICES"));
  }
 
  /**
   * Costruisce un'istanza di ServicesBuilder utilizzando il file di configurazione
   * passato come parametro
   * @param configFileURL il file di configurazione con l'elenco dei servizi
   * @throws Exception
   */
  private ServicesBuilder(String configFileURL) throws Exception {
    this.configFileURL = configFileURL;
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating(true);
    try {
      InputStream is = this.getClass().getResourceAsStream(configFileURL);
      if (is == null) {
	throw new IOException("File not found: "+configFileURL);
      }
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse( is, this);
    } catch (ParserConfigurationException ex) {
    } catch (SAXException ex) {
    } catch (IOException ex) {
      SolmrLogger.fatal(this, ex);
    } 
  }

  /**
   * Restituisce il singleton di ServicesBuilder
   * @return l'istanza statica di ServicesBuilder
   * @throws Exception
   */
  public static ServicesBuilder getInstance() throws Exception {
    if (sf == null) sf = new ServicesBuilder();
    return sf;
  }

  /**
   * Restituisce il singleton di ServicesBuilder basato sul file di configurazione
   * <code>configFileURL</code>
   * @param configFileURL il file di configurazione con l'elenco dei servizi
   * @return l'istanza statica di ServicesBuilder
   * @throws Exception
   */
  public static ServicesBuilder getInstance(String configFileURL) throws Exception {
    if (sf == null) sf = new ServicesBuilder(configFileURL);
    return sf;
  }

  private Object getService(String serviceName) throws Exception {
    Object bc = null;
    bc = theServices.get(serviceName);
    if (bc==null) {
      ServiceVO sVO = (ServiceVO)theProperties.get(serviceName);
      if (sVO==null) throw new Exception("No such service: "+serviceName);
      if (SolmrLogger.isDebugEnabled(this)) SolmrLogger.debug(this, "Generating service: "+sVO.toString());
      if (sVO.getServiceType()==ServiceVO.EXTERNAL) {
	InfoPortaDelegata info = PDConfigReader.read(getClass().getResourceAsStream(sVO.getConfigurationFile()));
	bc = PDProxy.newInstance(info);
      } else if (sVO.getServiceType()==ServiceVO.INTERNAL) {
	Class<?> calledService = Class.forName(sVO.getServiceClass());
	Constructor<?> constr = calledService.getConstructor(new Class[] {String.class});
	bc = constr.newInstance(new Object[] {sVO.getConfigurationFile()});
      }
      theServices.put(serviceName, bc);
    }
    return bc;
  }

  /**
   * Invoca sul servizio richiesto il comando specificato.
   * @param serviceName nome del servizio richiesto
   * @param command comando invocato
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, CommandVO command) throws Exception {
    if (command==null||(serviceName==null&&command.getServiceName()==null)||command.getCommandName()==null)
      throw new Exception("Invalid service or command invoked - null");
    if (command.getParameterTypes()==null||command.getParameterValues()==null||
	command.getParameterTypes().length!=command.getParameterValues().length)
      throw new Exception("Wrong number of parameters on command: "+command.getCommandName());
    command.setServiceName(serviceName);
    Object result = null;
    Object bc = getService(serviceName);
    Class<?> clientClass = null;
    clientClass = bc.getClass();
    Method invokedMethod = clientClass.getMethod(command.getCommandName(), command.getParameterTypes());
    result = invokedMethod.invoke(bc, command.getParameterValues());
    return result;
  }

  /**
   * Invoca il comando specificato, a patto che al suo interno sia indicato il nome
   * del servizio
   * @param command comando da eseguire
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(CommandVO command) throws Exception {
    if (command==null||command.getServiceName()==null)
      throw new Exception("Invalid service or command invoked - null");
    return execute(command.getServiceName(), command);
  }

  /**
   * Invoca sul servizio richiesto un comando con nome <code>commandName</code>
   * senza passare alcun parametro
   * @param serviceName nome del servizio richiesto
   * @param commandName nome del comando invocato
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, String commandName) throws Exception {
    return execute(serviceName,
		   new CommandVO(commandName,
		   new Class[] {},
		   new Object[] {}));
  }

  /**
   * Invoca sul servizio richiesto un comando con nome <code>commandName</code>
   * passando un solo parametro
   * @param serviceName nome del servizio richiesto
   * @param commandName nome del comando invocato
   * @param parameter1 parametro numero 1 da passare al comando
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, String commandName, Object parameter1) throws Exception {
    return execute(serviceName,
		   new CommandVO(commandName,
		   new Class[] {parameter1==null?null:parameter1.getClass()},
		   new Object[] {parameter1}));
  }

  /**
   * Invoca sul servizio richiesto un comando con nome <code>commandName</code>
   * passando due parametri
   * @param serviceName nome del servizio richiesto
   * @param commandName nome del comando invocato
   * @param parameter1 parametro numero 1 da passare al comando
   * @param parameter2 parametro numero 2 da passare al comando
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, String commandName,
			Object parameter1, Object parameter2) throws Exception {
    return execute(serviceName,
		   new CommandVO(commandName,
		   new Class[] {parameter1==null?null:parameter1.getClass(),parameter2==null?null:parameter2.getClass()},
		   new Object[] {parameter1, parameter2}));
  }

  /**
   * Invoca sul servizio richiesto un comando con nome <code>commandName</code>
   * passando tre parametri
   * @param serviceName nome del servizio richiesto
   * @param commandName nome del comando invocato
   * @param parameter1 parametro numero 1 da passare al comando
   * @param parameter2 parametro numero 2 da passare al comando
   * @param parameter3 parametro numero 3 da passare al comando
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, String commandName,
			Object parameter1, Object parameter2, Object parameter3) throws Exception {
    return execute(serviceName,
		   new CommandVO(commandName,
		   new Class[] {parameter1==null?null:parameter1.getClass(),parameter2==null?null:parameter2.getClass(),
			        parameter3==null?null:parameter3.getClass()},
		   new Object[] {parameter1,parameter2,parameter3}));
  }

  /**
   * Invoca sul servizio richiesto un comando con nome <code>commandName</code>
   * passando quattro parametri
   * @param serviceName nome del servizio richiesto
   * @param commandName nome del comando invocato
   * @param parameter1 parametro numero 1 da passare al comando
   * @param parameter2 parametro numero 2 da passare al comando
   * @param parameter3 parametro numero 3 da passare al comando
   * @param parameter4 parametro numero 4 da passare al comando
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, String commandName, Object parameter1,
			Object parameter2, Object parameter3, Object parameter4) throws Exception {
    return execute(serviceName,
		   new CommandVO(commandName,
		   new Class[] {parameter1==null?null:parameter1.getClass(),parameter2==null?null:parameter2.getClass(),
			        parameter3==null?null:parameter3.getClass(),parameter4==null?null:parameter4.getClass()},
		   new Object[] {parameter1,parameter2,parameter3,parameter4}));
  }

  /**
   * Invoca sul servizio richiesto un comando con nome <code>commandName</code>
   * passando cinque parametri
   * @param serviceName nome del servizio richiesto
   * @param commandName nome del comando invocato
   * @param parameter1 parametro numero 1 da passare al comando
   * @param parameter2 parametro numero 2 da passare al comando
   * @param parameter3 parametro numero 3 da passare al comando
   * @param parameter4 parametro numero 4 da passare al comando
   * @param parameter5 parametro numero 5 da passare al comando
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, String commandName, Object parameter1,
			Object parameter2, Object parameter3, Object parameter4, Object parameter5) throws Exception {
    return execute(serviceName,
		   new CommandVO(commandName,
		   new Class[] {parameter1==null?null:parameter1.getClass(),parameter2==null?null:parameter2.getClass(),
			        parameter3==null?null:parameter3.getClass(),parameter4==null?null:parameter4.getClass(),
		                parameter5==null?null:parameter5.getClass()},
		   new Object[] {parameter1,parameter2,parameter3,parameter4,parameter5}));
  }

  /**
   * Invoca sul servizio richiesto un comando con nome <code>commandName</code>
   * passando sei parametri
   * @param serviceName nome del servizio richiesto
   * @param commandName nome del comando invocato
   * @param parameter1 parametro numero 1 da passare al comando
   * @param parameter2 parametro numero 2 da passare al comando
   * @param parameter3 parametro numero 3 da passare al comando
   * @param parameter4 parametro numero 4 da passare al comando
   * @param parameter5 parametro numero 5 da passare al comando
   * @param parameter6 parametro numero 6 da passare al comando
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, String commandName, Object parameter1,
			Object parameter2, Object parameter3, Object parameter4, Object parameter5,
                        Object parameter6) throws Exception {
    return execute(serviceName,
		   new CommandVO(commandName,
		   new Class[] {parameter1==null?null:parameter1.getClass(),parameter2==null?null:parameter2.getClass(),
			        parameter3==null?null:parameter3.getClass(),parameter4==null?null:parameter4.getClass(),
		                parameter5==null?null:parameter5.getClass(),parameter6==null?null:parameter6.getClass()},
		   new Object[] {parameter1,parameter2,parameter3,parameter4,parameter5,
		                 parameter6}));
  }

  /**
   * Invoca sul servizio richiesto un comando con nome <code>commandName</code>
   * passando sette parametri
   * @param serviceName nome del servizio richiesto
   * @param commandName nome del comando invocato
   * @param parameter1 parametro numero 1 da passare al comando
   * @param parameter2 parametro numero 2 da passare al comando
   * @param parameter3 parametro numero 3 da passare al comando
   * @param parameter4 parametro numero 4 da passare al comando
   * @param parameter5 parametro numero 5 da passare al comando
   * @param parameter6 parametro numero 6 da passare al comando
   * @param parameter7 parametro numero 7 da passare al comando
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, String commandName, Object parameter1,
			Object parameter2, Object parameter3, Object parameter4, Object parameter5,
                        Object parameter6, Object parameter7) throws Exception {
    return execute(serviceName,
		   new CommandVO(commandName,
		   new Class[] {parameter1==null?null:parameter1.getClass(),parameter2==null?null:parameter2.getClass(),
			        parameter3==null?null:parameter3.getClass(),parameter4==null?null:parameter4.getClass(),
		                parameter5==null?null:parameter5.getClass(),parameter6==null?null:parameter6.getClass(),
		                parameter7==null?null:parameter7.getClass()},
		   new Object[] {parameter1,parameter2,parameter3,parameter4,parameter5,
		                 parameter6, parameter7}));
  }

  /**
   * Invoca sul servizio richiesto un comando con nome <code>commandName</code>
   * passando otto parametri
   * @param serviceName nome del servizio richiesto
   * @param commandName nome del comando invocato
   * @param parameter1 parametro numero 1 da passare al comando
   * @param parameter2 parametro numero 2 da passare al comando
   * @param parameter3 parametro numero 3 da passare al comando
   * @param parameter4 parametro numero 4 da passare al comando
   * @param parameter5 parametro numero 5 da passare al comando
   * @param parameter6 parametro numero 6 da passare al comando
   * @param parameter7 parametro numero 7 da passare al comando
   * @param parameter8 parametro numero 8 da passare al comando
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, String commandName, Object parameter1,
			Object parameter2, Object parameter3, Object parameter4, Object parameter5,
                        Object parameter6, Object parameter7, Object parameter8) throws Exception {
    return execute(serviceName,
		   new CommandVO(commandName,
		   new Class[] {parameter1==null?null:parameter1.getClass(),parameter2==null?null:parameter2.getClass(),
			        parameter3==null?null:parameter3.getClass(),parameter4==null?null:parameter4.getClass(),
		                parameter5==null?null:parameter5.getClass(),parameter6==null?null:parameter6.getClass(),
		                parameter7==null?null:parameter7.getClass(),parameter8==null?null:parameter8.getClass()},
		   new Object[] {parameter1,parameter2,parameter3,parameter4,parameter5,
		                 parameter6,parameter7,parameter8}));
  }

  /**
   * Invoca sul servizio richiesto un comando con nome <code>commandName</code>
   * passando nove parametri
   * @param serviceName nome del servizio richiesto
   * @param commandName nome del comando invocato
   * @param parameter1 parametro numero 1 da passare al comando
   * @param parameter2 parametro numero 2 da passare al comando
   * @param parameter3 parametro numero 3 da passare al comando
   * @param parameter4 parametro numero 4 da passare al comando
   * @param parameter5 parametro numero 5 da passare al comando
   * @param parameter6 parametro numero 6 da passare al comando
   * @param parameter7 parametro numero 7 da passare al comando
   * @param parameter8 parametro numero 8 da passare al comando
   * @param parameter9 parametro numero 9 da passare al comando
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, String commandName, Object parameter1,
			Object parameter2, Object parameter3, Object parameter4, Object parameter5,
                        Object parameter6, Object parameter7, Object parameter8, Object parameter9) throws Exception {
    return execute(serviceName,
		   new CommandVO(commandName,
		   new Class[] {parameter1==null?null:parameter1.getClass(),parameter2==null?null:parameter2.getClass(),
			        parameter3==null?null:parameter3.getClass(),parameter4==null?null:parameter4.getClass(),
		                parameter5==null?null:parameter5.getClass(),parameter6==null?null:parameter6.getClass(),
		                parameter7==null?null:parameter7.getClass(),parameter8==null?null:parameter8.getClass(),
		                parameter9==null?null:parameter9.getClass()},
		   new Object[] {parameter1,parameter2,parameter3,parameter4,parameter5,
		                 parameter6,parameter7,parameter8,parameter9}));
  }

  /**
   * Invoca sul servizio richiesto un comando con nome <code>commandName</code>
   * passando dieci parametri
   * @param serviceName nome del servizio richiesto
   * @param commandName nome del comando invocato
   * @param parameter1 parametro numero 1 da passare al comando
   * @param parameter2 parametro numero 2 da passare al comando
   * @param parameter3 parametro numero 3 da passare al comando
   * @param parameter4 parametro numero 4 da passare al comando
   * @param parameter5 parametro numero 5 da passare al comando
   * @param parameter6 parametro numero 6 da passare al comando
   * @param parameter7 parametro numero 7 da passare al comando
   * @param parameter8 parametro numero 8 da passare al comando
   * @param parameter9 parametro numero 9 da passare al comando
   * @param parameter10 parametro numero 10 da passare al comando
   * @return risultato dell'elaborazione
   * @throws Exception
   */
  public Object execute(String serviceName, String commandName, Object parameter1,
			Object parameter2, Object parameter3, Object parameter4, Object parameter5,
                        Object parameter6, Object parameter7, Object parameter8, Object parameter9, Object parameter10) throws Exception {
    return execute(serviceName,
		   new CommandVO(commandName,
		   new Class[] {parameter1==null?null:parameter1.getClass(),parameter2==null?null:parameter2.getClass(),
			        parameter3==null?null:parameter3.getClass(),parameter4==null?null:parameter4.getClass(),
		                parameter5==null?null:parameter5.getClass(),parameter6==null?null:parameter6.getClass(),
		                parameter7==null?null:parameter7.getClass(),parameter8==null?null:parameter8.getClass(),
		                parameter9==null?null:parameter9.getClass(),parameter10==null?null:parameter10.getClass()},
		   new Object[] {parameter1,parameter2,parameter3,parameter4,parameter5,
		                 parameter6,parameter7,parameter8,parameter9,parameter10}));
  }

  public void endDocument() throws SAXException {
    if (SolmrLogger.isDebugEnabled(this)) SolmrLogger.debug(this, "Finish document parsing");
  }

  public void startDocument() throws SAXException {
    if (SolmrLogger.isDebugEnabled(this)) SolmrLogger.debug(this, "Start document parsing: file "+configFileURL+"...");
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

    if (eName.equals("service")) {
      if (attrs != null) {
	ServiceVO thisService = new ServiceVO();
	thisService.setServiceName(attrs.getValue("serviceName"));
	thisService.setServiceType(Integer.valueOf(attrs.getValue("serviceType")).intValue());
	thisService.setConfigurationFile(attrs.getValue("configurationFile"));
	thisService.setServiceClass(attrs.getValue("serviceClass"));

	theProperties.put(thisService.getServiceName(), thisService);
	if (SolmrLogger.isDebugEnabled(this))
	  SolmrLogger.debug(this, "Loaded service configuration for: "+thisService);
      }
    }
  }
}