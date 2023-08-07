package it.csi.smranag.smrgaa.dto.allevamenti;

import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Vector;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 0.1
 */
public class AllevamentoAcquaLavaggio implements Serializable
{
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 8486693282045899437L;
  
  
  
  private Long idAllevamentoAcquaLavaggio;
  private Long idAllevamento;
  private Long idDestinoAcquaLavaggio;
  private BigDecimal quantitaAcquaLavaggio;
  private String quantitaAcquaLavaggioStr;
  private String descDestinoAcquaLavaggio;
  private String[][]        errori;
  
  
  
  public Long getIdAllevamentoAcquaLavaggio()
  {
    return idAllevamentoAcquaLavaggio;
  }
  public void setIdAllevamentoAcquaLavaggio(Long idAllevamentoAcquaLavaggio)
  {
    this.idAllevamentoAcquaLavaggio = idAllevamentoAcquaLavaggio;
  }
  public Long getIdAllevamento()
  {
    return idAllevamento;
  }
  public void setIdAllevamento(Long idAllevamento)
  {
    this.idAllevamento = idAllevamento;
  }
  public Long getIdDestinoAcquaLavaggio()
  {
    return idDestinoAcquaLavaggio;
  }
  public void setIdDestinoAcquaLavaggio(Long idDestinoAcquaLavaggio)
  {
    this.idDestinoAcquaLavaggio = idDestinoAcquaLavaggio;
  }
  public BigDecimal getQuantitaAcquaLavaggio()
  {
    return quantitaAcquaLavaggio;
  }
  public void setQuantitaAcquaLavaggio(BigDecimal quantitaAcquaLavaggio)
  {
    this.quantitaAcquaLavaggio = quantitaAcquaLavaggio;
  }
  public String getQuantitaAcquaLavaggioStr()
  {
    return quantitaAcquaLavaggioStr;
  }
  public void setQuantitaAcquaLavaggioStr(String quantitaAcquaLavaggioStr)
  {
    this.quantitaAcquaLavaggioStr = quantitaAcquaLavaggioStr;
  }  
  public String[][] getErrori()
  {
    return errori;
  }
  public void setErrori(String[][] errori)
  {
    this.errori = errori;
  }
  
  
  public boolean validateInsert()
  {
    Vector<String[]> errors = new Vector<String[]>();
    
    if (Validator.isEmpty(quantitaAcquaLavaggioStr))
    {
      String errore[] = new String[2];
      errore[0] = "err_destQuantitaAcquaLavaggio";
      errore[1] = "il campo è obbligatorio";
      errors.add(errore);
    }
    else
    {
      try
      {
        String errore[] = new String[2];
        errore[0] = "err_destQuantitaAcquaLavaggio";
        errore[1] = "La quantità indicata deve essere un numero intero compreso tra 0,0001 e 999999,9999";
        double destQuantitaAcquaLavaggioDB = Double.parseDouble(quantitaAcquaLavaggioStr.replace(',', '.'));
        if (destQuantitaAcquaLavaggioDB < 0 || destQuantitaAcquaLavaggioDB > 999999.9999)
          errors.add(errore);
        else
        {
          String temp = checkDecimals(destQuantitaAcquaLavaggioDB, 4);
          if (temp != null)
          {
            errore[1] = temp;
            errors.add(errore);
          }
          else
          {
            quantitaAcquaLavaggio = new BigDecimal(quantitaAcquaLavaggioStr.replace(',', '.'));
          }
        }
      }
      catch (Exception e)
      {
        String errore[] = new String[2];
        errore[0] = "err_destQuantitaAcquaLavaggio";
        errore[1] = "La quantità indicata deve essere un numero intero compreso tra 0,0001 e 999999,9999";
        errors.add(errore);
      }
    }

    errori = errors.size() == 0 ? null : (String[][]) errors.toArray(new String[0][]);

    if (errori != null)
      return true;
    else
      return false;
  }
  
  
  private static String checkDecimals(double value, int numDecimals)
  {
    if (numDecimals > 0)
    {
      double poweredValue = value * Math.pow(10, numDecimals);
      poweredValue = Math.round(poweredValue);
      poweredValue = poweredValue / Math.pow(10, numDecimals);
      if (poweredValue != value)
        return "Attenzione, troppe cifre decimali (massimo " + numDecimals + ")";
    }
    return null;
  }
  
  
  
  public String getDescDestinoAcquaLavaggio()
  {
    return descDestinoAcquaLavaggio;
  }
  public void setDescDestinoAcquaLavaggio(String descDestinoAcquaLavaggio)
  {
    this.descDestinoAcquaLavaggio = descDestinoAcquaLavaggio;
  }
  
  
  
 
  
  
}
