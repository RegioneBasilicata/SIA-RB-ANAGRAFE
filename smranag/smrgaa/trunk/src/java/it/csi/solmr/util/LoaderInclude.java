package it.csi.solmr.util;


/**
 * <p>Title: SMRGAA</p>
 * <p>Description: Anagrafe</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: TOBECONFIG</p>
 * @author
 * @version 1.0
 */
/**
 * Librerie
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpSession;


public class LoaderInclude
{
  private static LoaderInclude _this = null;
  private Properties propInclude = null;
  private LoaderInclude()
  {
  }

  /** Ritorna l'istanza SingleTone */
  public static synchronized LoaderInclude getInstance()
  {
    if (_this == null)
    {
      _this = new LoaderInclude();
    }

    return _this;
  }

  /**
   * Recupera lo stream relativo all'include selezionata
   *
   * @param strInclude
   * @return
   */
  public String getInclude(String strInclude, HttpSession session)
  {
    String strResult = null;

    if (strInclude != null)
    {
      try
      {
        //Leggo le propInclude dalla session
        propInclude = (Properties) session.getAttribute("loaderInclude");

        if (propInclude == null)
        {
          propInclude = new Properties();
        }

        strResult = propInclude.getProperty(strInclude);

        if (strResult == null)
        {
          URL urlRemote = new URL(strInclude);
          URLConnection urlConnection = urlRemote.openConnection();
          InputStream isRemote = urlConnection.getInputStream();
          strResult = readStream(isRemote);
          propInclude.setProperty(strInclude, strResult);
        }

        //Imposto le propInclude in session
        session.setAttribute("loaderInclude", propInclude);
      }
      catch (MalformedURLException ex)
      {
        SolmrLogger.error(this,
          "[LoaderInclude] Problemi durante il recupero della include: " +
          strInclude + "\n" + ex);
      }
      catch (IOException ex)
      {
        SolmrLogger.error(this,
          "[LoaderInclude] Problemi durante il recupero della include: " +
          strInclude + "\n" + ex);
      }
    }
    else
      SolmrLogger.error(this,
        "[LoaderInclude] URL per il recupero degli include non è valorizzato!");

    return strResult;
  }

  /**
   * Legge lo stream per recuperare il data set conenente la include
   *
   * @param isRemote
   * @return
   * @throws IOException
   */
  private String readStream(InputStream isRemote) throws IOException
  {
    StringBuffer strBuffer = new StringBuffer();
    byte[] b = new byte[1];

    while (isRemote.read(b) != -1)
    {
      strBuffer.append(new String(b));
    }

    isRemote.close();

    return strBuffer.toString();
  }

  /**
   * Ritorna un iteratore sui contenuti url memorizzati in cache
   */
  public Iterator<?> getCachedEntities()
  {
    return propInclude.keySet().iterator();
  }

  /**
   * Pulisce nella cache il contenuto recuperato all'url specificata
   */
  public void clearCachedEntity(String strInclude)
  {
    propInclude.remove(strInclude);
  }

  /**
   * Pulisce nella cache dei contenuti recuperati
   */
  public void clearAllEntities()
  {
    propInclude.clear();
  }

  /**
   * Riceve la dimensione della cache dei contenuti
   */
  public int size()
  {
    return propInclude.size();
  }
}
