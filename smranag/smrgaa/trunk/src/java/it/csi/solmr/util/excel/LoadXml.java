package it.csi.solmr.util.excel;

import org.jdom.*;
import org.jdom.input.*;
import java.io.*;

/**
 *
 * <p>Title: </p>
 * <p>Description: Carica un file xml contenuto all'interno della directory WEB-INF/classes
 * e lo mantiene in memoria.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Pelli Edoardo
 * @version 1.0
 */

public class LoadXml {

  private Document xmlDocument;

  static{
  }

  public LoadXml() {

  }

  public  void load(InputStream in) throws Exception{
    try {
      xmlDocument=new SAXBuilder().build(in);
    }
    catch (JDOMException ex) {
      ex.printStackTrace();
      throw new Exception("Errore lettura file xml.");
    }
  }

  public  Document load(String fileName) throws Exception{
    return xmlDocument;
  }


  public Document load(){
    return xmlDocument;
  }

}
