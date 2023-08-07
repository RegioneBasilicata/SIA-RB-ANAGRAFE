package it.csi.solmr.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

public class PageCache {
  private Map<Object,Object> cachedPage = null;

  public PageCache() {
    cachedPage = new HashMap<Object,Object>();
  }

  public String requestRemotePage(String url, ServletContext sc) throws Exception{

    String cacheTime = sc.getInitParameter("cacheTime");
    long cacheTimeLong = Long.parseLong(cacheTime);

    Date currentDate = new Date();
    Long longTime = new Long(currentDate.getTime());
    String currentTime = longTime.toString();

    // controllo se c'è la chiave url nella map
    // o se il tempo trascorso dall'ultima immissione in cache supera un certo limite
    if(!isCached(url, currentTime, cacheTimeLong)){
      PageRequest httpRequestPage = new PageRequest();
      String newCode = httpRequestPage.getHtmlCode(url);

      // crea una nuova linea nella map o updata quella vecchia esistente
      cachePage(url, newCode, currentTime);
    }

    Page htmlPage = (Page)cachedPage.get(url);
    return htmlPage.getCode();
  }

  private boolean isCached(String key, String currentTime, long maxTime) {

    if(cachedPage.containsKey(key)) {
//      SolmrLogger.debug(this, " --> questa URL è già presente in cache");
      Page page = (Page)cachedPage.get(key);
      long pageTime = Long.parseLong(page.getTime());
      long current = Long.parseLong(currentTime);
      if((current - pageTime) > maxTime){
//        SolmrLogger.debug(this, "         e dev'essere ricaricata in cache");
        return false;
      } else {
//        SSolmrLogger.debug(this, "         ed è in cache da poco");
        return true;
      }
    } else {
//      SolmrLogger.debug(this, " --> questa URL non è ancora presente in cache");
      return false;
    }
  }

  private void cachePage(String key, String code, String time){

    Page page = null;

    if(cachedPage.containsKey(key)) {
//      SSolmrLogger.debug(this, " aggiorno il codice nella cache");
      page = (Page)cachedPage.get(key);
      page.setCode(code);
      page.setTime(time);
    } else {
//      SolmrLogger.debug(this, (" immetto il codice nella cache");
      page = new Page();
      page.setCode(code);
      page.setTime(time);
      cachedPage.put(key, page);
    }
  }

  public void clearCache(){
//    SolmrLogger.debug(this, (" rimuovo tutto dalla cache ");
    cachedPage.clear();
  }

}
