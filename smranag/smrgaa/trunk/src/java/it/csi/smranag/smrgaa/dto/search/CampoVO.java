package it.csi.smranag.smrgaa.dto.search;

import it.csi.jsf.htmpl.Htmpl;

import java.io.Serializable;

/**
 * Restituisce la stringa necessaria a fare un ordinamento dinamico
 * 
 * @author TOBECONFIG
 * 
 */
public class CampoVO implements Serializable
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 4501243775636121133L;
  
  private String campo; //Contiene il nome del campo usato nella order by (es. A.CUAA)
  private String segnaPostoHtmpl; //Contiene il nome del segnaposto usato nell'HTML (es. CUAA)
  private boolean ordinamento; //indica l'ordinamento: true ASC, false DESC
  
  public CampoVO(String campo,String segnaPostoHtmpl,boolean ordinamento)
  {
    this.segnaPostoHtmpl=segnaPostoHtmpl;
    this.campo=campo;
    this.ordinamento=ordinamento;
  }
  
  
  public String getCampo()
  {
    return campo;
  }
  public void setCampo(String campo)
  {
    this.campo = campo;
  }
  public boolean isOrdinamento()
  {
    return ordinamento;
  }
  public void setOrdinamento(boolean ordinamento)
  {
    this.ordinamento = ordinamento;
  }
  public String getSegnaPostoHtmpl()
  {
    return segnaPostoHtmpl;
  }
  public void setSegnaPostoHtmpl(String segnaPostoHtmpl)
  {
    this.segnaPostoHtmpl = segnaPostoHtmpl;
  }
  
  /**
   * Metodo usato per scrivere sulla pagina html i dati relativi all'ordinamento
   * @param htmpl
   * @param blocco
   */
  public void writeOrdinamento(Htmpl htmpl,String blocco)
  {
    if (isOrdinamento())
    {
      htmpl.set(blocco+"ordina"+getSegnaPostoHtmpl(), "giu");
      htmpl.set(blocco+"descOrdina"+getSegnaPostoHtmpl(), "ordine decrescente");
      htmpl.set(blocco+"ordinaCampo"+getSegnaPostoHtmpl(), getSegnaPostoHtmpl());
    }
    else 
    {
      htmpl.set(blocco+"ordina"+getSegnaPostoHtmpl(), "su");
      htmpl.set(blocco+"descOrdina"+getSegnaPostoHtmpl(), "ordine crescente");
      htmpl.set(blocco+"ordinaCampo"+getSegnaPostoHtmpl(), getSegnaPostoHtmpl());
    } 
  }
}
