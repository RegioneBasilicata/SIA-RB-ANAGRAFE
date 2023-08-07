package it.csi.solmr.dto.anag;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */
import java.io.Serializable;

/**
 * Qiesto VO è utilizzato solamente dai servizi di anagrafe come parametro di
 * ritorno, contiene un vettore di stringhe ed un vettore di long
 * */
public class ParametroRitornoVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
   static final long serialVersionUID = -4861367970915316363L;


  private String[] messaggio;
  private Long[] idAzienda;
  private it.csi.solmr.dto.StringcodeDescription[] ruoloAAEP;
  private Integer codeResult;

  public String[] getMessaggio()
  {
    return messaggio;
  }

  public void setMessaggio(String[] messaggio)
  {
    this.messaggio = messaggio;
  }

  public void setIdAzienda(Long[] idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public Long[] getIdAzienda()
  {
    return idAzienda;
  }
  public it.csi.solmr.dto.StringcodeDescription[] getRuoloAAEP() {
    return ruoloAAEP;
  }

  public void setRuoloAAEP(it.csi.solmr.dto.StringcodeDescription[] ruoloAAEP) {
    this.ruoloAAEP = ruoloAAEP;
  }
  
  public Integer getCodeResult()
  {
    return codeResult;
  }

  public void setCodeResult(Integer codeResult)
  {
    this.codeResult = codeResult;
  }

}
