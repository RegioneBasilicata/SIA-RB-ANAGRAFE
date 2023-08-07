package it.csi.solmr.client;

import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.client.anag.IAnagClient;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.exception.SolmrException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @todo Rinominare la classe come EntityFactory o ListOfValueFactory
 * 
 * @Carica i metodi della business logic tramite reflection
 * @author Luigi R. Viggiano
 * @version $Revision: 1.1 $
 */
public class MapFactory
{

  private static MapFactory mapFactory;
  private Map<Object, Object> cacheMap = null;
  private HashMap<String, String> hm = new HashMap<String, String>();
  private HashMap<Object, Object> hmFather = new HashMap<Object, Object>();

  public static MapFactory getInstance()
  {
    if (null == mapFactory)
    {
      mapFactory = new MapFactory();
    }
    return mapFactory;
  }

  private MapFactory()
  {
    hm = new HashMap<String, String>();
    hmFather = new HashMap<Object, Object>();

    cacheMap = new HashMap<Object, Object>();

    // Caricamento della Tabella Hash dei metodi per l'interfaccia Anag
    Class<?> prof = IAnagClient.class;
    Method[] methods = prof.getMethods();
    for (int i = 0; i < methods.length; i++)
    {
      hm.put(methods[i].getName(), prof.getName());

    }
    hmFather.put(prof.getName(), new AnagFacadeClient());

  }

  public Iterator<?> getCachedEntities()
  {
    return cacheMap.keySet().iterator();
  }

  public void clearCachedEntity(String entita)
  {
    cacheMap.remove(entita);
  }

  public void clearAllEntities()
  {
    cacheMap.clear();
  }

  public int size()
  {
    return cacheMap.size();
  }

  @SuppressWarnings("unchecked")
  public Map<Object, Object> getMap(String entita)
  {
    Map<Object, Object> entitaMap = null;
    if (cacheMap.containsKey(entita))
    {
      entitaMap = (Map<Object, Object>) cacheMap.get(entita);
    }
    else
    {
      try
      {
        entitaMap = loadMap(entita, false);
        cacheMap.put(entita, entitaMap);
      }
      catch (SolmrException ex)
      {
        ex.printStackTrace();
      }
    }
    return entitaMap;
  }

  @SuppressWarnings("unchecked")
  public Map<Object, Object> getMapOrder(String entita, boolean orderId)
  {
    Map<Object, Object> entitaMap = null;
    if (cacheMap.containsKey(entita))
    {
      entitaMap = (Map<Object, Object>) cacheMap.get(entita);
    }
    else
    {
      try
      {
        entitaMap = loadMap(entita, orderId);
        cacheMap.put(entita, entitaMap);
      }
      catch (SolmrException ex)
      {
        ex.printStackTrace();
      }
    }
    return entitaMap;
  }

  @SuppressWarnings("unchecked")
  private Map<Object, Object> loadMap(String entita, boolean orderId)
      throws SolmrException
  {
    Map<Object, Object> returnvalue = null;
    if (!orderId)
    {
      returnvalue = new ComboMap();
    }
    else
    {
      returnvalue = new TreeMap<Object, Object>(
          new ComboMap().new OrdinamentoComboForCodice());
    }

    try
    {

      // Ricerca nelle tabelle Hash precaricate da Interfaccia, del metodo da
      // invocare
      // Invocazione del metodo sull'interfaccia opportuna

      // Cambio nome metodo per gestire nome proprietà sulla direttiva
      // automatica di recupero parametri da request e valorizzazione VO
      entita = ("" + entita.charAt(0)).toUpperCase() + entita.substring(1);

      String metodo = "get" + entita;
      Object obj = hmFather.get(hm.get(metodo));
      Method m = obj.getClass().getMethod(metodo, new Class[0]);

      Vector<Object> value = (Vector<Object>) m.invoke(obj, new Object[0]);
      Iterator<Object> iter = value.iterator();
      while (iter.hasNext())
      {
        Object object = iter.next();
        if (object instanceof CodeDescription)
        {
          CodeDescription cd = (CodeDescription) object;
          returnvalue.put(cd.getCode(), cd.getDescription());
        }
        else
        {
          StringcodeDescription scd = (StringcodeDescription) object;
          returnvalue.put(scd.getCode(), scd.getDescription());
        }
      }

    }
    catch (NoSuchMethodException exc1)
    {
      throw (new SolmrException());
    }
    catch (InvocationTargetException exc2)
    {
      throw (new SolmrException());
    }
    catch (IllegalAccessException exc3)
    {
      throw (new SolmrException());
    }

    return returnvalue;
  }
}
