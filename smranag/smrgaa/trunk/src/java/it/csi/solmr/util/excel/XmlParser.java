package it.csi.solmr.util.excel;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Element;



public class XmlParser {

  private LoadXml xmlFile;

  public XmlParser(java.io.InputStream in) {
    this.xmlFile=new LoadXml();
    try{
      xmlFile.load(in);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  /**
   * Restituisce una lista di elements con attributi uguali a quelli passati con AttributeList.
   * @param dad elemento padre
   * @param childName nome degli elementi figli che si vogliono estrarre
   * @param att elenco degli attributi che l'element deve avere per essere prelavato
   * @param getAllAttribute se true preleva solo gli elementi i cui attributi sono uguali a quelli passati con AttributeList.
   *                        Se false preleva gli elementi con almeno un attributo uguale a quelli passati da AttibuteList
   * @return
   */
  @SuppressWarnings("unchecked")
  public static List<Object> getElementChildren(Element dad,String childName,AttributeList att,boolean getAllAttribute){
    List<Object> children = dad.getChildren(childName);
    Vector<Object> ris = new Vector<Object>();
    for(int i=0;i<children.size();i++){
      Element tmp =(Element)children.get(i);
      if(att!=null){
        checkAttributes(tmp,ris,att,getAllAttribute);
      }
    }
    return ris;
  }

  /**
   * Verifica se gli attributi dell'element tmp sono uguali a quelli presenti nell'AttributeList.
   * @param tmp elemento su cui fare la verifica
   * @param ris Oggetto che verrà riempito con gli element con caratteristiche corrsipondenti ai criteri di ricerca impostati
   * @param att lista di valori da ricercare
   * @param getAllAttribute true se vanno prelevati solo gli elementi i cui attributi devono essere tutti uguali a quelli di AttributeList
   * @throws Exception
   */

  private static void checkAttributes(Element tmp,List<Object> ris,AttributeList att,boolean getAllAttribute) {
    boolean allEquals=false;
    for(int j=0;j<att.size();j++){
      if(getAllAttribute){
        if(tmp.getAttribute(att.getAttributeAt(j).getName())!=null){
          if(tmp.getAttribute(att.getAttributeAt(j).getName()).getValue().equals(att.getAttributeAt(j).getValue())){
            allEquals=true;
          }
          else{
            allEquals=false;
            break;
          }
        }
      }
      else{
        if(tmp.getAttribute(att.getAttributeAt(j).getName())!=null){
          if(tmp.getAttribute(att.getAttributeAt(j).getName()).getValue().equals(att.getAttributeAt(j).getValue())){
            ris.add(tmp);
          }
        }
      }
    }
    if(allEquals){
      ris.add(tmp);
    }
  }

  /**
   * Dato un elenco di Element estrae l'element che ha gli attributi specificati da AttributeList
   * @param list elenco deli elementi
   * @param childName nome dell'elemento su cui effettuare il confronto degli attributi
   * @param att lista di attributi che l'elemento figlio deve avere.
   * @param getAllAttributes se true vengono prelevati solo gli elementi in cui la lista di attributi è esattamente uguale a quella contenuta in AttributeList
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<Object> extractElementFromList(List<Object> list,String childName,AttributeList att,boolean getAllAttributes) throws Exception{
    Vector<Object> ris = new Vector<Object>();
    for(int i=0;i<list.size();i++){
      Element el = (Element)list.get(i);
      if(el.getName().equals(childName)){
        List<Object> attList = el.getAttributes();
        if(attList!=null){
          checkAttributes(el,ris,att,getAllAttributes);
        }
      }
    }
    return ris;
  }

  public Element extractElementFromList(List<Object> list,String childName,AttributeList att) throws Exception{
    List<Object> tmp = extractElementFromList(list,childName,att,true);
    if(tmp.size()>0){
      return (Element)tmp.get(0);
    }
    return new Element("Non_trovato");
  }


  public Element getElement(Element dad, String childName){
    return dad.getChild(childName);
  }

  public String getElementText(Element dad,String childName){
    return getElement(dad,childName).getText();
  }

  @SuppressWarnings("unchecked")
  public List<Object> getElementChildren(Element dad,String childName){
    return dad.getChildren(childName);
  }

  public String getAttributeValue(Element el,String attributeName){
    Attribute att = el.getAttribute(attributeName);
    return att.getValue();
  }



  /**
   * Imposta il valore alla variabile WebUtil.
   * @param util
   */

  public void setContextPath(String path){
  }


  /**
   * Restituisce l'elemento figlio della root con le caratteristiche specificate da AttributeList
   * @param fileName nome del file xml
   * @param childName nome del figlio della root che dev'essere prelvato
   * @param getAllAttribute se true tutti gli attributi del figlio da prelevare devono essere uguali a quelli contenuti nell'oggetto AttributeList (parametro da eliminare)
   * @param att lista degli attributi (nome ==> valore) che il figlio deve possedere x essere prelevato
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public Element getRootChild(String childName,boolean getAllAttribute,AttributeList att) throws Exception{
    Vector<Object> v = (Vector)XmlParser.getElementChildren(getRoot(),childName,att,true);
    if(v.size()==0){
      throw new Exception("Nessun elemento trovato con gli attributi richiesti."+att+".");
    }
    return (Element)v.elementAt(0);
  }

  @SuppressWarnings("unchecked")
  public Element getChild(Element dad,String nameChild,AttributeList al){
    Element ris = null;
    try {
      ris = extractElementFromList(dad.getChildren(),nameChild,al);
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return ris;
  }

  public Element getRootChild(String childName,AttributeList att) throws Exception{
    return getRootChild(childName,true,att);
  }

  /**
   * Restituisce la root del file xml
   * @param fileName
   * @return
   * @throws Exception
   */

  public Element getRoot() throws Exception{
    return xmlFile.load().getRootElement();
  }

}
