package it.csi.smranag.smrgaa.util;

import it.csi.solmr.util.services.Validator;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import it.csi.solmr.util.SolmrLogger;

public class JaxbUtils
{
	private static String THIS_CLASS = "JaxbUtils"; 

  private static final String DATA_XML = "yyyy-MM-dd";

  public JaxbUtils()
  {
  }

  /**
   * Realizza l'unmarshalling del messaggio XML fornito dall' input stream
   * @param is l'input stream che fornisce l'XML
   * @param jaxbPkg il package jaxb di riferiemnto (da cui ottenere il JAXBContext)
   * @return l'oggetto root
   * @throws Exception
   */
  public static Object unmarshal(InputStream is, String jaxbPkg) throws Exception
  {
    JAXBContext jc = JAXBContext.newInstance(jaxbPkg);
    Unmarshaller u = jc.createUnmarshaller();
    return u.unmarshal(is);
  }
  
  /**
   * Realizza il marshal dell'oggetto jaxb.
   * @param obj l'oggetto root jaxb
   * @param jaxbPkg il package di riferimento (lo stesso dell'oggetto jaxb)
   * @param schemaLocation l'attributo schemaLocation da includere (non incluso se null)
   * @param noNamespaceSchemaLocation l'attributo noNamespaceSchemaLocation da includere (non incluso se null)
   * @param encoding l'encoding da utilizzare per il marshalling
   * @return l'XML sotto forma di bytes
   * @throws Exception
   * 
   * example
   * obj = istanza di Fascicolo.java
   * jaxbPkg = package classe Fascicolo
   * schemaLocation = null;
   * noNamespaceSchemaLocation = null
   * encoding = "utf-8"
   * 
   * public static byte[] marshal ( Object obj, String jaxbPkg,
   *   String schemaLocation, String noNamespaceSchemaLocation, String encoding )
   * throws Exception
   * 
   * 
   * 
   */
  public static byte[] marshal ( Object obj, String jaxbPkg,
      String schemaLocation, String noNamespaceSchemaLocation, String encoding )
    throws Exception
  {
    JAXBContext jc = JAXBContext.newInstance(jaxbPkg);
    Marshaller m = jc.createMarshaller();

    m.setProperty(Marshaller.JAXB_ENCODING, encoding);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    m.marshal(obj, baos);
    String s = baos.toString(encoding);

    /// toglie il primo return dopo il primo tag
    /// <?xml version="1.0" encoding="iso-8859-1" standalone="yes"?>
    int i_primo_end = s.indexOf("?>");
    int i_secondo_start = s.indexOf('<',i_primo_end);
    s = s.substring(0,i_primo_end+2)+s.substring(i_secondo_start);

    // li devo ricalcolare
    i_primo_end = s.indexOf("?>");
    i_secondo_start = s.indexOf('<',i_primo_end);
    if ((schemaLocation!=null && !schemaLocation.equals(""))||
        (noNamespaceSchemaLocation!=null && !noNamespaceSchemaLocation.equals("")))
    {
      int iMessage = s.indexOf(">",i_secondo_start);
      if (iMessage>0)
      {
        String ns = " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
        String loc="";
        if (schemaLocation!=null && !schemaLocation.equals(""))
        {
          loc= " xsi:schemaLocation=\""+schemaLocation+"\" ";
        }
        if (noNamespaceSchemaLocation!=null && !noNamespaceSchemaLocation.equals(""))
        {
          loc= " xsi:noNamespaceSchemaLocation=\""+noNamespaceSchemaLocation+"\" ";
        }
        s = s.substring(0,iMessage)+ns+" "+loc+s.substring(iMessage);
      }
    }
    return s.getBytes(encoding);
  }

  /**
   * Formattazione data gg/mm/aaaa
   * Metodo wrapper di it.csi.solmr.util.DateUtils
   * per permettere eventuali future personalizzazioni
   * @param d
   * @return String
   */
  public static String formatDate(Date d)
  {
    SimpleDateFormat format = new SimpleDateFormat(DATA_XML);
    String s = format.format(d); 	
  	SolmrLogger.debug(null, "["+THIS_CLASS+"::formatDate] Data convertita a stringa: " + s);
  	return s;
  }
  
  public static Date parseDate(String s)
  {
  	if (Validator.isEmpty(s)) return null;

  	Date sData = null;
  	
  	try
  	{
      SimpleDateFormat format = new SimpleDateFormat(DATA_XML);
      sData = format.parse(s);
		}
  	catch (Exception e)
  	{
  		SolmrLogger.error(null, "["+THIS_CLASS+"::parseDate] Errore nella parsificazione - " + e.getMessage());
		}
  	
  	return sData;
  }
}