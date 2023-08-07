/**
 * UvResponseCCIAA.java
 *
 * VO utilizzato per wrappare l'omologo VO restituito da CCIAA (UvResponse)
 */

package it.csi.smranag.smrgaa.dto.ws.cciaa;

public class UvResponseCCIAA  implements java.io.Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 307927427566377784L;

  private String codRet;  //Stringa di codifica della risposta restituita dal CCIAA
  private String segnalazione; //Descrizione segnalazione restituita dal CCIAA
  private DatiUvCCIAA[] datiUv;  //array delle U.V.
  private double totSuperficieH;  //totale delle sup. iscritta dele U.V.
  private String[] albo;  //Utilizzato per popolare la combo che permette di filtrare la colonna albo
  private String[] vitigno; //Utilizzato per popolare la combo che permette di filtrare la colonna vitigno
  private String filtroAlbo;
  private String filtroVitigno;
  private String ordComune;
  private String ordAlbo;
  private boolean particellaP; // Se true indica che la ricerca è stata fatta aggiungendo P alla particella

  public String getCodRet()
  {
    return codRet;
  }

  public void setCodRet(String codRet)
  {
    this.codRet = codRet;
  }

  public String getSegnalazione()
  {
    return segnalazione;
  }

  public void setSegnalazione(String segnalazione)
  {
    this.segnalazione = segnalazione;
  }

  public DatiUvCCIAA[] getDatiUv()
  {
    return datiUv;
  }

  public void setDatiUv(DatiUvCCIAA[] datiUv)
  {
    this.datiUv = datiUv;
  }

  public double getTotSuperficieH()
  {
    return totSuperficieH;
  }

  public void setTotSuperficieH(double totSuperficieH)
  {
    this.totSuperficieH = totSuperficieH;
  }

  public String[] getAlbo()
  {
    return albo;
  }

  public void setAlbo(String[] albo)
  {
    this.albo = albo;
  }

  public String[] getVitigno()
  {
    return vitigno;
  }

  public void setVitigno(String[] vitigno)
  {
    this.vitigno = vitigno;
  }

  public String getFiltroAlbo()
  {
    return filtroAlbo;
  }

  public void setFiltroAlbo(String filtroAlbo)
  {
    this.filtroAlbo = filtroAlbo;
  }

  public String getFiltroVitigno()
  {
    return filtroVitigno;
  }

  public void setFiltroVitigno(String filtroVitigno)
  {
    this.filtroVitigno = filtroVitigno;
  }

  public String getOrdComune()
  {
    return ordComune;
  }

  public void setOrdComune(String ordComune)
  {
    this.ordComune = ordComune;
  }

  public String getOrdAlbo()
  {
    return ordAlbo;
  }

  public void setOrdAlbo(String ordAlbo)
  {
    this.ordAlbo = ordAlbo;
  }

  public boolean isParticellaP()
  {
    return particellaP;
  }

  public void setParticellaP(boolean particellaP)
  {
    this.particellaP = particellaP;
  }
}
