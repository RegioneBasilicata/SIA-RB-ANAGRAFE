package it.csi.smranag.smrgaa.util;

import it.csi.solmr.util.DateUtils;

import java.util.Date;

public class CodiceFiscaleUtils
{
  
  
  public static String estraiSessoCodiceFiscale(String codFisc) 
  {
    String val = "";
    try
    {
      String numStr = codFisc.substring(9,11);
      int num = new Integer(numStr).intValue();
      if(num > 40)
        val = "F";
      else
        val = "M";
    }
    catch(Exception ex)
    {}
    
    return val;
  }
  
  public static String estraiCodiceFiscaleComune(String codFisc) 
  {
    String val = "";
    try
    {
      val = codFisc.substring(11,15);
    }
    catch(Exception ex)
    {}
    
    return val;
  }
  
  public static Date estraiDataNascitaCodiceFiscale(String codFisc) 
  {
    Date dataNascita = null;
    try
    {
      String annoStr = codFisc.substring(6,8);
      int anno = new Integer(annoStr).intValue();
      String annoCorrenteStr = DateUtils.getCurrentYear().toString();
      int annoCorrente = new Integer(annoCorrenteStr.substring(2,4)).intValue();
      int totale = anno - annoCorrente;
      if(totale < 0)
        annoStr = "20"+annoStr;
      else
        annoStr = "19"+annoStr;
       
      int giornoNascita = 0; 
      String numStr = codFisc.substring(9,11);
      int num = new Integer(numStr).intValue();
      if(num > 40)
        giornoNascita = num - 40;
      else
        giornoNascita = num;
      
      
      String meseLetteraStr = codFisc.substring(8,9);
      String meseStr = "";
      if("A".equalsIgnoreCase(meseLetteraStr))
        meseStr = "01";
      else if("B".equalsIgnoreCase(meseLetteraStr))
        meseStr = "02";
      else if("C".equalsIgnoreCase(meseLetteraStr))
        meseStr = "03";
      else if("D".equalsIgnoreCase(meseLetteraStr))
        meseStr = "04";
      else if("E".equalsIgnoreCase(meseLetteraStr))
        meseStr = "05";
      else if("H".equalsIgnoreCase(meseLetteraStr))
        meseStr = "06";
      else if("L".equalsIgnoreCase(meseLetteraStr))
        meseStr = "07";
      else if("M".equalsIgnoreCase(meseLetteraStr))
        meseStr = "08";
      else if("P".equalsIgnoreCase(meseLetteraStr))
        meseStr = "09";
      else if("R".equalsIgnoreCase(meseLetteraStr))
        meseStr = "10";
      else if("S".equalsIgnoreCase(meseLetteraStr))
        meseStr = "11";
      else if("T".equalsIgnoreCase(meseLetteraStr))
        meseStr = "12";
      
      
      String dataNascitaStr = giornoNascita+"/"+meseStr+"/"+annoStr;
      
      dataNascita = DateUtils.parseDate(dataNascitaStr);
    }
    catch(Exception ex)
    {}
    
    return dataNascita;
  }

  
  
}
