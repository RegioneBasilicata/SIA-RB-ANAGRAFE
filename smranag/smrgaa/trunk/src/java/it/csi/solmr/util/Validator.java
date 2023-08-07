package it.csi.solmr.util;

import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.CodiceFiscaleException;
import it.csi.solmr.exception.ValidationException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpSession;

public class Validator implements Serializable {
  
  

    /**
   * 
   */
  private static final long serialVersionUID = -2978343473373193958L;
  
  
    private String errorPage;
    private ValidationException validationEx;
    private static char carattere[] = {'A','B','C','D','E','F','G','H','I','J','K','L',
      'M','N','O','P','Q','R','S','T','U','V','W','X',
      'Y','Z','0','1','2','3','4','5','6','7','8','9'};
    private static int valore_pari[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,
      19,20,21,22,23,24,25,0,1,2,3,4,5,6,7,8,9};
    private static int valore_dispari[] = {1,0,5,7,9,13,15,17,19,21,2,4,18,20,11,3,6,8,
      12,14,16,10,22,25,24,23,1,0,5,7,9,13,15,17,19,21};
    private static String consonanti = "BCDFGHJKLMNPQRSTVWXYZ";
    private static String vocali = "AEIOU";
    private static char mesiCf[] = {'A','B','C','D','E','H','L','M','P','R','S','T'};
    private static final String CIFRE="0123456789";
    private static final byte CONTRIBUTO_BBAN[]={1,0,5,7,9,13,15,17,19,21,2,4,18,20,11,3,6,8,12,14,16,10,22,25,24,23};
    private static final String ALPHA="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static HashMap<Object,Object> MapValueIBAN=null;
    
    public static void setMapValueIBAN(){
    	MapValueIBAN = new HashMap<Object,Object>();
    	MapValueIBAN.put("A", "10");
    	MapValueIBAN.put("B", "11");
    	MapValueIBAN.put("C", "12");
    	MapValueIBAN.put("D", "13");
    	MapValueIBAN.put("E", "14");
    	MapValueIBAN.put("F", "15");
    	MapValueIBAN.put("G", "16");
    	MapValueIBAN.put("H", "17");
    	MapValueIBAN.put("I", "18");
    	MapValueIBAN.put("J", "19");
    	MapValueIBAN.put("K", "20");
    	MapValueIBAN.put("L", "21");
    	MapValueIBAN.put("M", "22");
    	MapValueIBAN.put("N", "23");
    	MapValueIBAN.put("O", "24");
    	MapValueIBAN.put("P", "25");
    	MapValueIBAN.put("Q", "26");
    	MapValueIBAN.put("R", "27");
    	MapValueIBAN.put("S", "28");
    	MapValueIBAN.put("T", "29");
    	MapValueIBAN.put("U", "30");
    	MapValueIBAN.put("V", "31");
    	MapValueIBAN.put("W", "32");
    	MapValueIBAN.put("X", "33");
    	MapValueIBAN.put("Y", "34");
    	MapValueIBAN.put("Z", "35");
    }
    
    public Validator() {
    }

    public Validator(String errorPage) {
        this.errorPage = errorPage;
    }

    private ValidationException createValidationException() {
        if (null == validationEx)
            validationEx = new ValidationException(
                "Errore nella validazione dei dati", errorPage);
        return validationEx;
    }

    private static Date validateDate(String field, String name) {
        // if (field == null || field.length() == 0) return null;
        String pattern = "dd/MM/yyyy";
        try {
            return customParseDate(field, pattern, "-");
        } catch (ParseException parseEx) {
            return null;
        }
    }


    // Metodi per la nuova gestione dei messaggi di errore
    private static Date validateDate(String field) {
        String pattern = "dd/MM/yyyy";
        try {
            return customParseDate(field, pattern, "/");
        } catch (ParseException parseEx) {
            return null;
        }
    }

    public static boolean isDate(String field) {
      try {
        Date data = validateDate(field);
        if(data != null) {
          return true;
        }
        else {
          return false;
        }
      }
      catch (Exception ex) {
        return false;
      }
    }



    //Metodo che controlla la validità di una data e se questa sia minore o
    //uguale alla data odierna
    public Date validateUntilCurrentDate(String field, String name, String message) {
      Date date = validateDate(field, name);
      if(date!=null)
      {
        Date today = new Date();
        if(date.compareTo(today)>0)
          createValidationException().addMessage(message, name);
      }
      return date;
    }

    public Date validateUntilCurrentDate(String giorno, String mese, String anno,
                                          String name, String message) {
      return validateUntilCurrentDate(giorno+"-"+mese+"-"+anno, name, message);
    }

    //Metodo che controlla la validità di una data e se questa sia maggiore o
    //uguale alla data odierna
    public Date validateFromCurrentDate(String field, String name, String message) {
      Date date = validateDate(field, name);
      if(date!=null) {
        Date today = new Date();
        if(date.compareTo(today)<0)
          createValidationException().addMessage(message, name);
      }
      return date;
    }

    public Date validateFromCurrentDate(String giorno, String mese, String anno,
                                          String name, String message) {
      return validateFromCurrentDate(giorno+"-"+mese+"-"+anno, name, message);
    }

    public Date validateDate(String gg, String mm, String yy, String name) {
        return validateDate(gg + "-" + mm + "-" + yy, name);
    }

    //Metodo che cerca di riparare all'apparente baco della classe DateFormatter,
    //che non permette di rilevare come data errata una data come 01/01/200x,
    //trasformandola invece in 01/01/0200.
    //Se la data non è in formato corretto viene lanciata dal metodo una ParseException
    private static Date customParseDate(String data, String pattern, String separator)
                                                            throws ParseException {
      try {
        SimpleDateFormat dateFormatter = null;
        StringTokenizer st = new StringTokenizer(data, separator);
        while(st.hasMoreTokens()) {
          Integer.parseInt(st.nextToken());
        }

        dateFormatter = new SimpleDateFormat(pattern);
        dateFormatter.setLenient(false);
        return dateFormatter.parse(data);
      }
      catch(NumberFormatException exc) {
        throw new ParseException("La data deve avere campi numerici", 0);
      }
    }

    public String validateInteger(String field, String name) {
      String result = field;
      if(field != null && field.length() > 0) {
        try {
          double value = StringUtils.convertNumericField(field);
          if(isInteger(value, name))
            result = ""+((long)value);
        }
        catch(ParseException pe) {
          createValidationException().addMessage("Il campo non è numerico", name);
        }
      }
      return result;
    }

    public static boolean isNumericInteger(String value) {
      boolean ok = false;
      if(value == null || value.equals(""))
        ok = false;
      else{
        try {
          double doubleValue = StringUtils.convertNumericField(value);
          double longValue = Math.rint(doubleValue);
          if(doubleValue!=longValue) {
            ok = false;
          }
          else if(doubleValue < 0) {
            ok = false;
          }
          else
            ok = true;
        }
        catch(ParseException pe) {
          ok = false;
        }
      }
      return ok;
    }
    
    public static boolean isPositiveInteger(String value) {
      boolean ok = false;
      if(value == null || value.equals(""))
        ok = false;
      else{
        try {
          double doubleValue = StringUtils.convertNumericField(value);
          double longValue = Math.rint(doubleValue);
          if(doubleValue!=longValue) {
            ok = false;
          }
          else if(doubleValue <= 0) {
            ok = false;
          }
          else
            ok = true;
        }
        catch(ParseException pe) {
          ok = false;
        }
      }
      return ok;
    }
    
    
    public static boolean isNumberPercentualeMaggioreZero(String value) 
    {
      boolean ok = false;
      if(value == null || value.equals(""))
      {
        ok = false;
      }
      else
      {
        try 
        {
          double doubleValue = StringUtils.convertNumericField(value);
          double longValue = Math.rint(doubleValue);
          if(doubleValue!=longValue) 
          {
            ok = false;
          }
          else if((doubleValue <= 0)
            || (doubleValue > 100))
          {
            ok = false;
          }
          else
          {
            ok = true;
          }
        }
        catch(ParseException pe) {
          ok = false;
        }
      }
      return ok;
    }
    
    
    public static boolean isNumberPercentuale(String value) 
    {
      boolean ok = false;
      if(value == null || value.equals(""))
      {
        ok = false;
      }
      else
      {
        try 
        {
          double doubleValue = StringUtils.convertNumericField(value);
          double longValue = Math.rint(doubleValue);
          if(doubleValue!=longValue) 
          {
            ok = false;
          }
          else if((doubleValue < 0)
            || (doubleValue > 100))
          {
            ok = false;
          }
          else
          {
            ok = true;
          }
        }
        catch(ParseException pe) {
          ok = false;
        }
      }
      return ok;
    }
    
    
    public static boolean isNumberPercentualeMaggioreZeroDecimali(String value) 
    {
      boolean ok = false;
      if(value == null || value.equals(""))
      {
        ok = false;
      }
      else
      {
        try 
        {
          double doubleValue = StringUtils.convertNumericField(value);
          if((doubleValue <= 0)
            || (doubleValue > 100))
          {
            ok = false;
          }
          else
          {
            ok = true;
          }
        }
        catch(ParseException pe) {
          ok = false;
        }
      }
      return ok;
    }

    public String validateInteger(String field, String name, long maxVal) {
      String result = field;
      if(field != null && field.length() > 0) {
        try {
          double value = StringUtils.convertNumericField(field);
          if(isInteger(value, name)) {
            long longValue = (long)value;
            if(longValue > maxVal) {
              createValidationException()
                .addMessage("Valore massimo uguale a "+maxVal, name);
            }
            else
              result = ""+longValue;
          }
        }
        catch(ParseException pe) {
          createValidationException().addMessage("Il campo non è numerico", name);
        }
      }
      return result;
    }

    public String validateInteger(String field, String name, int minVal, int maxVal) {
      String result = field;
      if(field != null && field.length() > 0) {
        try {
          double value = StringUtils.convertNumericField(field);
          if(isInteger(value, name)) {
            int intValue = (int)value;
            if(intValue > maxVal) {
              createValidationException()
                .addMessage("Valore massimo uguale a "+maxVal, name);
            }
            else if(intValue < minVal) {
              createValidationException()
                .addMessage("Valore minimo uguale a "+minVal, name);
            }
            else
              result = ""+intValue;
          }
        }
        catch(ParseException pe) {
          createValidationException().addMessage("Il numero non è numerico", name);
        }
      }
      return result;
    }

    public String validateDoubleDigit(String field, String name, int numDecimals) {
      String result = field;
      if(field != null && field.length() > 0) {
        try {
          double value = StringUtils.convertNumericField(field);
          if(checkDecimals(value, numDecimals))
            result = ""+value;
        }
        catch(ParseException pe) {
          createValidationException().addMessage("Il campo non è numerico", name);
        }
      }
      return result;
    }

    //Il parametro numInteger indica il numero massimo di cifre intere che può
    //contenere un campo numerico.
    public String validateDoubleDigit(String field, String name, int numInteger,
                                      int numDecimals) {
      String result = field;
      if(field != null && field.length() > 0) {
        try {
          double value = StringUtils.convertNumericField(field);
          if(checkDecimals(value, numDecimals)) {
            result = ""+value;
            if((result+".").indexOf(".")>numInteger)
              createValidationException().addMessage("Il numero deve avere al "+
                                                      "massimo "+numInteger+
                                                      " cifre intere", name);
          }
        }
        catch(ParseException pe) {
          createValidationException().addMessage("Il campo non è numerico", name);
        }
      }
      return result;
    }

    public static String validateDoubleDigit(String field, double maxVal, int numDecimals) {
    	String result = field;
    	if(field != null && field.length() > 0) {
    		try {
    			for(int i = 0; i < field.length(); i++) {
    				char c = field.charAt(i);
    				if(c == 'e' || c == 'E') {
    					result = null;
    					return result;
    				}
    			}
    			double value = StringUtils.convertNumericField(field);
    			if(checkDecimals(value, numDecimals)) {
    				if(value > maxVal || value <= 0) {
    					result = null;
    				}
    				else
    					result = ""+value;
    			}
    			else {
    				result = null;
    			}
    		}
    		catch (ParseException pe) {
    			result = null;
    		}
    	}
    	return result;
    }
    
    public static String validateDoubleDigitIncludeZero(String field, double maxVal, int numDecimals) {
      String result = field;
      if(field != null && field.length() > 0) {
        try {
          for(int i = 0; i < field.length(); i++) {
            char c = field.charAt(i);
            if(c == 'e' || c == 'E') {
              result = null;
              return result;
            }
          }
          double value = StringUtils.convertNumericField(field);
          if(checkDecimals(value, numDecimals)) {
            if(value > maxVal || value < 0) {
              result = null;
            }
            else
              result = ""+value;
          }
          else {
            result = null;
          }
        }
        catch (ParseException pe) {
          result = null;
        }
      }
      return result;
    }

    public String validateDouble(String field, String name) {
      return validateDoubleDigit(field, name, 0);
    }

    public static String validateDouble(String field, double maxVal) {
    	if(maxVal == SolmrConstants.FORMAT_SUP_CATASTALE) {
    		return validateDoubleDigit(field, maxVal, 4);
    	}
    	else if((maxVal == SolmrConstants.FORMAT_SUP_ONEDECIMAL)
    	    || (maxVal == SolmrConstants.FORMAT_FIVE_SUP_ONEDECIMAL))
    	{
    	  return validateDoubleDigit(field, maxVal, 1);
    	}
    	else if(maxVal == SolmrConstants.FORMAT_FIVE_SUP_TWODECIMAL)
    	{
    	  return validateDoubleDigit(field, maxVal, 2);
    	}
    	else {
    		return validateDoubleDigit(field, maxVal, 0);
    	}
    }
    
    public static String validateDoubleIncludeZero(String field, double maxVal) {
      if(maxVal == SolmrConstants.FORMAT_SUP_CATASTALE) {
        return validateDoubleDigitIncludeZero(field, maxVal, 4);
      }
      else if(maxVal == SolmrConstants.FORMAT_SUP_ONEDECIMAL)
      {
        return validateDoubleDigitIncludeZero(field, maxVal, 1);
      }
      else if(maxVal == SolmrConstants.FORMAT_FIVE_SUP_TWODECIMAL)
      {
        return validateDoubleDigitIncludeZero(field, maxVal, 2);
      }
      else {
        return validateDoubleDigitIncludeZero(field, maxVal, 0);
      }
    }

    public String validateEuro(String field, String name) {
      return validateDoubleDigit(field, name, 2);
    }

    //Il parametro numInteger indica il numero massimo di cifre intere che può
    //contenere un campo numerico.
    public String validateEuro(String field, String name, int numInteger) {
      return validateDoubleDigit(field, name, numInteger, 2);
    }

    public String validateEuro(String field, String name, double maxVal) {
      return validateDoubleDigit(field, maxVal, 2);
    }

    public Double validateCurrency(String field, String name) {
      if (field == null || field.length() == 0) return null;
      try {
          double value = Double.parseDouble(field);
          return new Double(value);
      }
      catch (NumberFormatException nfEx) {
          createValidationException()
              .addMessage("Formato numerico errato", name);
          return null;
      }
    }

    public static boolean isValidCurrency(String field) {
      if (field == null || field.length() == 0) return false;
      try {
          Double.parseDouble(field);
          return true;
      }
      catch (NumberFormatException nfEx) {
          return false;
      }
    }

    //Questo metodo, oltre a controllare che un campo sia numerico, controlla
    //che il valore inserito non superi un valore massimo
    public Double validateCurrency(String field, String name, int maxVal) {
        if (field == null || field.length() == 0) return null;
        try {
            double value = Double.parseDouble(field);
            if(value>(double)maxVal) {
              createValidationException()
                .addMessage("Valore massimo uguale a "+maxVal, name);
              return null;
            }
            return new Double(value);
        }
        catch (NumberFormatException nfEx) {
            createValidationException()
                .addMessage("Formato numerico errato", name);
            return null;
        }
    }

    //Questo metodo controlla che il primo argomento sia minore (o minore o uguale
    //nel caso che l'argomento boolean sia valorizzato a true) al secondo argomento
    public boolean validatePrecSucc (String campoPrec, String campoSucc,
                                      String name, boolean equal) {
      boolean result;
      try {
        double value1 = StringUtils.convertNumericField(campoPrec);
        double value2 = StringUtils.convertNumericField(campoSucc);
        if(equal)
          result = value1 <= value2;
        else
          result = value1 < value2;
      }
      catch(ParseException pe) {
         return true;
      }
      if(!result)
        createValidationException()
                .addMessage(""+campoPrec+" non è minore di "+campoSucc, name);
      return result;
    }

    public String validateAlphabetic(String field, String name) {
      String campo;
      if(field==null||field.length()==0)
        return field;
      campo = field.trim().toUpperCase();

      for(int i=0; i<campo.length(); i++) {
        char carattere = campo.charAt(i);

        if ((carattere < 'A' || carattere > 'Z')&&carattere!=' ') {
          createValidationException()
                .addMessage("Inserire solo lettere", name);
          return null;
        }
      }
      return field;
    }

    //Il metodo controlla se il campo field ha lunghezza length, o lunghezza
    //zero nel caso la variabile canBeEmpty sia true
    public boolean validateFieldLength(String field, int length, String name,
                                        boolean canBeEmpty) {
      if(field==null)
        return false;

      if(canBeEmpty&&field.length()==0)
        return true;

      if(field.length() != length) {
        createValidationException()
                .addMessage("Campo di lunghezza "+length, name);
        return false;
      }

      return true;
    }

    public boolean validateMinimumFieldLength(String field, int minLength,
                                              String name, boolean canBeEmpty) {
      if(field==null)
        return false;

      if(canBeEmpty&&field.length()==0)
        return true;

      if(field.length() < minLength) {
        createValidationException()
                .addMessage("Campo di lunghezza minima "+minLength, name);
        return false;
      }

      return true;
    }

    public static boolean validateMinimumValue(String field, double minValue) {

      if(field == null || field.length()==0) {
        return true;
      }

      try {
        double value = StringUtils.convertNumericField(field);

        if (value < minValue) {
          return false;
        }
      }
      catch(ParseException pe) {
      }

      return true;
    }

    //Il metodo controlla che la prima data sia precedente alla seconda
    public boolean checkDateOrder(String gg1, String mm1, String aaaa1,
                                  String gg2, String mm2, String aaaa2,
                                  String message, String name) {
      return checkDateOrder(gg1+"-"+mm1+"-"+aaaa1, gg2+"-"+mm2+"-"+aaaa2,
                            message, name, true);
    }

    public boolean checkDateOrder(String dataPrec, String dataSucc,
                                  String message,String name) {
      return checkDateOrder(dataPrec, dataSucc, message, name, true);
    }

    public boolean checkDateOrder(String gg1, String mm1, String aaaa1,
                                  String gg2, String mm2, String aaaa2,
                                  String message, String name, boolean canBeEqual) {
      return checkDateOrder(gg1+"-"+mm1+"-"+aaaa1, gg2+"-"+mm2+"-"+aaaa2,
                            message, name, canBeEqual);
    }

    public boolean checkDateOrder(String dataPrec, String dataSucc,
                                  String message,String name, boolean canBeEqual) {
      String pattern = "dd-MM-yyyy";
      SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
      try {
        Date dataInizio =  dateFormatter.parse(dataPrec);
        Date dataFine = dateFormatter.parse(dataSucc);
        int confronto = dataInizio.compareTo(dataFine);
        if(confronto>0 ||(!canBeEqual && confronto==0)) {
          createValidationException()
            .addMessage(message , name);
          return false;
        }
      }
      catch (ParseException parseEx) {
      }
      return true;
    }

    public boolean checkDateOrder(Timestamp dataPrec, Timestamp dataSucc,
                                  String message, String name) {
      return checkDateOrder(dataPrec, dataSucc, message, name, true);
    }

    public boolean checkDateOrder(Timestamp dataPrec, Timestamp dataSucc,
                                  String message, String name, boolean canBeEqual) {
      try {
        int confronto = dataPrec.compareTo(dataSucc);
        if(confronto>0||(!canBeEqual && confronto==0)) {
          createValidationException()
            .addMessage(message , name);
          return false;
        }
      }
      catch(Exception exc) {
      }
      return true;
    }

    //Il metodo controlla che le due date siano differenti, altrimenti crea
    //un messaggio di errore
    public boolean checkDateDifferent(String gg1, String mm1, String aaaa1,
                                      String gg2, String mm2, String aaaa2,
                                      String message, String name) {
      return checkDateDifferent(gg1+"-"+mm1+"-"+aaaa1, gg2+"-"+mm2+"-"+aaaa2,
                                message, name);
    }

    public boolean checkDateDifferent(String dataPrec, String dataSucc,
                                      String message,String name) {
      String pattern = "dd-MM-yyyy";
      SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
      try {
        Date dataInizio = dateFormatter.parse(dataPrec);
        Date dataFine = dateFormatter.parse(dataSucc);
        int confronto = dataInizio.compareTo(dataFine);
        if(confronto == 0 ) {
          createValidationException()
            .addMessage(message , name);
          return false;
        }
      }
      catch (ParseException parseEx) {
      }
      return true;
    }

    public static boolean validateDateF(String field) {
        String pattern = "dd/MM/yyyy";
        boolean ok = false;
        try {
            //return customParseDate(field, pattern, "/");
          if(customParseDate(field, pattern, "/")!=null){
            ok = true;
          }
          if(field.length()<10)
            ok = false;
        }
        catch (ParseException parseEx) {
            /*createValidationException()
                .addMessage("inserire data corretta", name);*/
            ok = false;
        }
        return ok;
    }

    public void validate() throws ValidationException {
        if (validationEx != null) {
            ValidationException valEx = validationEx;
            reset();
            valEx.raise();
        }
    }

    public void reset() {
        validationEx = null;
    }

    public void checkSession(HttpSession session, String timeoutPage)
        throws ValidationException {

        if (null == session.getAttribute("$isAlive")) {
            ValidationException validEx =
                new ValidationException("session expired", timeoutPage);
            validEx.addMessage("sessione scaduta", "errSession");
            throw validEx;
        }
    }

    public void beginSession(HttpSession session) {
        session.setAttribute("$isAlive", new Object());
    }

    public void endSession(HttpSession session) {
        session.removeAttribute("$isAlive");
    }

  // Metodo per controllare la correttezza del codice fiscale
  public static boolean verificaCorrettezzaCf(String codiceFiscale) {

    boolean giusto = false;
    codiceFiscale = codiceFiscale.toUpperCase();
    char codFiscale;
    int valore = 0;
    int somma = 0;

    if (codiceFiscale.length() == 16 ) {
      for (int i = 1; i < 16; i++) {
        //se i caratteri sono nella posizione pari
        if ( 0 == i%2) {
          codFiscale = codiceFiscale.charAt(i-1);
          for( int j = 1; j <= 36; j++) {
            if (codFiscale == carattere[j-1])
              valore = valore_pari[j-1];
          }
        }
        //se i caratteri sono nella posizione dispari
        if ( 0 != i%2) {
          codFiscale = codiceFiscale.charAt(i-1);
          for (int j = 1; j <= 36; j++) {
            if(codFiscale == carattere[j-1])
              valore = valore_dispari[j-1];
          }
        }
        somma+= valore;
      }
      int resto = somma%26;
      //il ciclo per il controllo
      if(mesiCf[resto] == codiceFiscale.charAt(15))
        giusto = true;
    }
    return giusto;
  }



  public static boolean controlloCf(String nome,
                    String cognome,
                    String sesso,
                                Date dataNascita,
                        String codFiscComune,
                                String codiceFiscale) {
    boolean ok = controlloCf(codiceFiscale);
    if(ok) {
      String codFiscaleGenerato = getCf(nome, cognome, sesso, dataNascita, codFiscComune);
      ok = codiceFiscale.equals(codFiscaleGenerato);
      if(!ok) {
        ok = verificaCFOmonimi(codiceFiscale,codFiscaleGenerato);
      }
    }
    return ok;
  }


  public static void verificaCf(String nome,
                                    String cognome,
                                    String sesso,
                                    Date dataNascita,
                                    String codFiscComune,
                                    String codiceFiscale) throws CodiceFiscaleException {

    boolean ok = controlloCf(codiceFiscale);
    if(ok) {
      String codFiscaleGenerato = getCf(nome, cognome, sesso, dataNascita, codFiscComune);
      verificaOmonimiCF(codiceFiscale,codFiscaleGenerato);
    } else {
      throw new CodiceFiscaleException(it.csi.solmr.etc.anag.AnagErrors.ERR_GENERIC_CODICE_FISCALE);
    }
  }


  private static boolean verificaCFOmonimi(String codiceFiscale, String codFiscaleGenerato) {

    /*int posizioniCifre[] = {6,7,9,10,12,13,14};

    for(int i = 0; i < posizioniCifre.length; i++) {
      char carattere = codiceFiscale.charAt(posizioniCifre[i]);
      if(carattere < '0' || carattere > '9') {
        carattere = convertiCF(carattere);
        codiceFiscale = codiceFiscale.substring(0,posizioniCifre[i])+carattere+codiceFiscale.substring(posizioniCifre[i]+1);
      }
    }*/
    return StringUtils.convertiCFOmonimo(codiceFiscale).equalsIgnoreCase(codFiscaleGenerato);
  }

  private static void verificaOmonimiCF(String codiceFiscale, String codFiscaleGenerato) throws CodiceFiscaleException{

    codiceFiscale = StringUtils.convertiCFOmonimo(codiceFiscale);
    confrontaCF(codiceFiscale,codFiscaleGenerato);
  }

  private static void confrontaCF(String codice1, String codice2) throws CodiceFiscaleException {

    CodiceFiscaleException ce = null;
    codice1 = codice1.toUpperCase();
    codice2 = codice2.toUpperCase();

    int giorno1 = 0;
    int giorno2 = 0;
    try {
      giorno1 = Integer.parseInt(codice1.substring(9,11));
      giorno2 = Integer.parseInt(codice2.substring(9,11));
      if(giorno1 > 40 ^ giorno2 > 40) {
        ce = new CodiceFiscaleException();
        ce.setSesso(true);
      }
    }
    catch(Exception e) {
      try {
        giorno1 = Integer.parseInt(""+StringUtils.convertiDaCF(codice1.charAt(9))+codice1.charAt(10));
        giorno2 = Integer.parseInt(""+StringUtils.convertiDaCF(codice2.charAt(9))+codice2.charAt(10));
      }
      catch(Exception ex) {
        ce = new CodiceFiscaleException();
        ce.setGiornoNascita(true);
      }
    }

    // Non controlliamo il check digit perché la verifica della correttezza formale viene fatta nei
    // validate dei VO(tradotto: GUAI A CHI LO TOCCA!!!!)
    for(int i = 0; i<15; i++) {
      if(codice1.charAt(i) != codice2.charAt(i)) {
        if(codice1.charAt(i) != '-' && codice2.charAt(i) != '-') {
          if(ce == null) {
            ce = new CodiceFiscaleException();
          }
          ce.setErrore(i);
        }
      }
    }
    if(ce != null) {
      //if(ce.getSesso()) {
      if(giorno1 != 0 && giorno1 != 99 && giorno2 != 0 && giorno2 != 99) {
        if(Math.abs(giorno1-giorno2)==40)
          ce.setGiornoNascita(false);
      }
      else {
        ce.setGiornoNascita(false);
      }
      //}
      if(ce.getPresenzaErrori()) {
        throw ce;
      }
    }
  }
  /*private static char convertiCF(char carattere) {

    char caratteriCF[] = {'L','M','N','P','Q','R','S','T','U','V'};
    char result = carattere;
    for(int i = 0; i < caratteriCF.length; i++) {
      if(caratteriCF[i] == carattere) {
        result = (char)('0'+i);
        break;
      }
    }
    return result;
  }*/

  public static boolean controlloCf(String stringa) {
    char caratt;
    int controllo = -1;
    boolean ok = false;
    int resto;
    int sum_pari = 0;
    int sum_dispari = 0;

    if((stringa != null) && (stringa.length()==16)) {
      stringa = stringa.toUpperCase();
      for (int i=1; i<=15; i++) {
        int row;
        caratt = stringa.charAt(0);
        stringa = stringa.substring(1);

        for (row=1; row<=36; row++) {
          if (carattere[row-1] == caratt) {
            if ( (i/2)*2 == i ) {
              sum_pari = sum_pari + valore_pari[row-1];
              break;
            }
            else {
              sum_dispari = sum_dispari + valore_dispari[row-1];
              break;
            }
          }
        }
        //Occorre controllare se l'utente ha inserito caratteri non alfanumerici,
        //perché in alcuni casi, con probabilità minima ma non nulla, il metodo
        //potrebbe non restituire il messaggio di errore
        if(row>36) {
          //Il carattere non corrisponde a nessun valore salvato nell'array
          //'carattere', per cui viene creato il messaggio di errore e si
          //forza l'uscita dal metodo, per non eseguire altro codice a questo
          //punto inutile
          //createValidationException().addMessage("Codice Fiscale errato", name);

          return ok;
        }
      }

      resto = (sum_pari + sum_dispari) - ((sum_pari + sum_dispari)/26) * 26;

      caratt =  stringa.charAt(0);

      for (int row=1; row<=36; row++) {
        if (carattere[row-1]  == caratt ) {
          controllo = valore_pari[row-1];
          break;
        }
      }

      if (controllo == resto)
        ok = true;
    }
    return ok;
  }

  public static String getCf(String nome, String cognome, String sesso, Date dataNascita, String codFiscComune) {
    String cf = "";
    // Controllo che i parametri passati non siano nulli
    /*if (cognome==null||
    nome==null||
        (sesso==null||(!sesso.equalsIgnoreCase("M")&&!sesso.equalsIgnoreCase("F")))||
    dataNascita==null||
        codFiscComune==null)
      return null;*/
    Vector<Object> vVocali = new Vector<Object>();
    Vector<Object> vConsonanti = new Vector<Object>();
    int i = 0;
    int j = 0;

    if(cognome == null) {
      cf += "---";
    }
    else {
      cognome = cognome.toUpperCase();
      // Estraggo i 3 caratteri del cognome
      for (i=0; i<cognome.length(); i++) {
        String theChar = String.valueOf(cognome.charAt(i));
        if (consonanti.indexOf(theChar)>=0)
          vConsonanti.add(theChar);
        else if (vocali.indexOf(theChar)>=0)
          vVocali.add(theChar);
      }

      for(i=0;i<vConsonanti.size()&&i<3;i++) {
        cf += (String)vConsonanti.elementAt(i);
      }
      if(i<3) {
        for(j=0;j < vVocali.size() && j+i < 3; j++) {
          cf += (String)vVocali.elementAt(j);
        }
      }
      if(j+i<3) {
        for(;j+i<3;j++) {
          cf+= "X";
        }
      }
    }

    if(nome == null) {
      cf += "---";
    }
    else {
      // Estraggo i 3 caratteri del nome
      nome = nome.toUpperCase();
      vVocali = new Vector<Object>();
      vConsonanti = new Vector<Object>();
      for (i=0; i<nome.length(); i++) {
        String theChar = String.valueOf(nome.charAt(i));
        if (consonanti.indexOf(theChar)>=0)
          vConsonanti.add(theChar);
        else if (vocali.indexOf(theChar)>=0)
          vVocali.add(theChar);
      }
      if (vConsonanti.size()>=4) {
        cf += (String)vConsonanti.elementAt(0)+
              (String)vConsonanti.elementAt(2)+
              (String)vConsonanti.elementAt(3);
      }
      else {
        i = 0;
        j = 0;
        for(;i<vConsonanti.size();i++) {
          cf += (String)vConsonanti.elementAt(i);
        }
        if(i<3) {
          for(;j < vVocali.size() && j+i < 3; j++) {
            cf += (String)vVocali.elementAt(j);
          }
        }
        if(j+i<3) {
          for(;j+i<3;j++) {
            cf+= "X";
          }
        }
      }
    }
    if(dataNascita == null) {
      String cfSesso = "--";
      cf += "---";
      if(sesso != null) {
        if(sesso.equalsIgnoreCase("M"))
          cfSesso = "00";
        if(sesso.equalsIgnoreCase("F"))
          cfSesso = "99";
      }
      cf += cfSesso;
    }
    else {
      // Aggiungo la data ed il comune
      String anno = String.valueOf(DateUtils.extractYearFromDate(dataNascita)).substring(2);
      String mese = String.valueOf(mesiCf[DateUtils.extractMonthFromDate(dataNascita)-1]);
      /*String giorno = String.valueOf(sesso.equalsIgnoreCase("M")?
                                     DateUtils.extractDayFromDate(dataNascita):
                                     DateUtils.extractDayFromDate(dataNascita)+40);*/
      String giorno = String.valueOf(DateUtils.extractDayFromDate(dataNascita));

      if (giorno.length()==1)
        giorno = "0"+giorno;

      if(sesso == null || (!sesso.equalsIgnoreCase("M") && !sesso.equalsIgnoreCase("F"))) {
        giorno = ""+StringUtils.convertiInCF(giorno.charAt(0))+giorno.charAt(1);
      }
      else {
        if(sesso.equalsIgnoreCase("F")) {
          giorno = ""+(Integer.parseInt(giorno)+40);
        }
      }
      cf += anno+mese+giorno;
    }

    if(codFiscComune==null)
    {
      codFiscComune = "----";
    }
    else
    {
      codFiscComune = codFiscComune.toUpperCase();
    }

    cf+=codFiscComune;
    // Aggiungo il check digit

    if(cf.indexOf("-")<0)
      cf += getCheckCf(cf);
    else
      cf += "-";

    return cf;
  }

  private static String getCheckCf(String stringa) {
    String check = "";
    char caratt;
    int resto;
    int sum_pari = 0;
    int sum_dispari = 0;
    for (int i=1; i<=15; i++) {
      int row;
      caratt = stringa.charAt(0);
      stringa = stringa.substring(1);

      for (row=1; row<=36; row++) {
    if (carattere[row-1] == caratt) {
      if ( (i/2)*2 == i ) {
        sum_pari = sum_pari + valore_pari[row-1];
        break;
      }
      else {
        sum_dispari = sum_dispari + valore_dispari[row-1];
        break;
      }
    }
      }
      //Occorre controllare se l'utente ha inserito caratteri non alfanumerici,
      //perché in alcuni casi, con probabilità minima ma non nulla, il metodo
      //potrebbe non restituire il messaggio di errore
      if(row>36) {
    //Il carattere non corrisponde a nessun valore salvato nell'array
    //'carattere', per cui viene creato il messaggio di errore e si
    //forza l'uscita dal metodo, per non eseguire altro codice a questo
    //punto inutile
    //createValidationException().addMessage("Codice Fiscale errato", name);

    return null;
      }
    }

    resto = (sum_pari + sum_dispari) - ((sum_pari + sum_dispari)/26) * 26;

    /*caratt =  stringa.charAt(0);

    for (int row=1; row<=36; row++) {
      if (carattere[row-1]  == caratt ) {
    controllo = valore_pari[row-1];
    break;
      }
    }*/

    check = String.valueOf(carattere[resto]);

    return check;
  }

  public static boolean controlloPIVA(String stringa) {
    boolean ok = false;

    int somma = 0;

    if(!isValidCurrency(stringa))
      return ok;

    if((stringa != null) && (stringa.length()==11)) {
      for(int i=0; i<=8; i+=2) {
        somma += stringa.charAt(i)-'0';
      }

      for(int i=1; i<=9; i+=2) {
        int temp = (stringa.charAt(i)-'0')*2;
        if(temp > 9)
          temp-=9;
        somma += temp;
      }

      if((10-somma%10)%10 == stringa.charAt(10)-'0') {
        ok = true;
      }
    }
    return ok;
    /*if(!ok)
      createValidationException().addMessage("Partita IVA errata", name);*/
  }

  private boolean isInteger(double value, String name) {
    double longValue = Math.rint(value);
    if(value!=longValue) {
      if(name != null)
        createValidationException().addMessage("Il numero deve essere intero", name);
      return false;
    }
    return true;
  }

  private static boolean checkDecimals(double value, int numDecimals) {
    if(numDecimals > 0) {
      double poweredValue = value*Math.pow(10, numDecimals);
      poweredValue = Math.round(poweredValue);
      poweredValue = poweredValue/Math.pow(10, numDecimals);
      if(poweredValue!=value) {
        return false;
      }
    }
    return true;
  }

  // Controlla che l'inidirizzo e-mail sia valido:non ho considerato il caso in cui la stringa
  // sia null solo perchè,non essendo la mail un campo obbligatorio,eseguo il controllo solo nel
  // caso in cui la stringa sia valorizzata.
  //Borgogno 13/06/03 - HtmplUtil - Inizio
  //  public boolean isValidEmail(String email) {
  //Borgogno 13/06/03 - HtmplUtil - Fine
  public static boolean isValidEmail(String email) {

    // non deve contenere spazi
    if ( (email.indexOf(' ') != -1) || (email==null)) {
      return false;
    }
    // l'email valida più breve non scende sotto 5 caratteri
    if (email.length() < 5) {
      return false;
    }
    int idxAt = email.indexOf('@');
    if (idxAt == -1) {
      return false;
    }
    // partendo da "@" cerca il "."
    int idxDot = email.indexOf('.', idxAt + 1);
    if (idxDot == -1) {
      return false;
    }
    // controlla che @ e . non siano contigui
    if (idxDot - idxAt <= 1) {
            return false;
    }
    if (!(Character.isLetterOrDigit(email.charAt(0)) && Character.isLetterOrDigit(email.charAt(email.length()-1)) &&
          Character.isLetterOrDigit(email.charAt(idxAt+1)))) {
      return false;
    }
    return true;
    }


    //Borgogno 13/06/03 - HtmplUtil - Inizio
    public static boolean isNotEmpty(String field) {
    if (field == null || field.trim().length() == 0)
        return false;
    return true;
    }
    
    public static boolean isNotEmptyNotZero(Long field) {
      if (field == null || Long.valueOf(0).compareTo(field) == 0)
        return false;
      return true;
    }


    public static boolean isNotEmpty(Object field) {
      if (field instanceof String)
        return isNotEmpty((String)field);
      return (field != null);
    }

    public static boolean isDefaultComboValue(Object value) {
      return ("null".equals(value));
    }
    //Borgogno 13/06/03 - HtmplUtil - Fine

    /**
     * @param numero  numero da controllare
     * @param min  minimo valore che numero può assumere
     * @param max  massimo valore che numero può assumere
     * @param precision  massimo numero di cifre decimali che può avere il numero
     * @return restituisce true se il numero inserito è corretto (cioè se è
     * un numero reale maggiore (o uguale) di min e minore (o uguale) di max
     * e di precisione massima precision
     */
    public static boolean isDouble(String numero,double min,double max,int precision)
    {
      char characterCheck;
      int counterPunto = 0;
      int posPunto = 0;
      int i;
      try
      {
        numero=numero.replace(',','.');
        double num=Double.parseDouble(numero);
        if (num<min) return false;
        if (num>max) return false;
      }
      catch(Exception e)
      {
        return false;
      }
      for (i=0; i < numero.length(); i++)
      {
        //Assigns the CharacterCheck variable to each character of input in succession
        characterCheck = numero.substring(i, i+1).charAt(0);
        if ('.' == (characterCheck))
        {
           counterPunto++;
           posPunto = i;
           if (counterPunto >1) return false;
        }
        if (! (((characterCheck >= '0') && (characterCheck <= '9')) || (characterCheck=='.') || (characterCheck=='-')))
           return false;
      }

      if (posPunto>0)
          if ( (i-posPunto-1) > precision )
             return false;
          return true;
  }

  /**
   * Effettua controlli di validità sulla data
   * @param date
   * @param name name da usare nella add dell'errors
   * @param txtName nome del campo per esteso (da visualizzare all'utente in caso di errore)
   * @param errors
   * @param required indica se la data è obbligatoria
   * @param maxToday indica se la data deve essere inferiore a quella odierna
   * @return data come oggetto Date
   */
  public static Date validateDateAll(String date,String name,String txtName,ValidationErrors errors, boolean required, boolean maxToday)
  {
    Date valDate=null;
    if (!Validator.isNotEmpty(date))
    {
      if (required)
      {
        errors.add(name,new ValidationError("Inserire la "+txtName));
      }
    }
    else
    {
      if (Validator.isDate(date))
      {
        if (date.trim().length()!=10)
        {
          errors.add(name,new ValidationError("Inserire la data nel formato gg/mm/aaaa"));
        }
        else
        {
          try
          {
            valDate=validateDate(date);
          }
          catch(Exception e)
          {
            errors.add(name,new ValidationError("Data non valida"));
            return null;
          }
          if (maxToday && new Date().compareTo(valDate)<0)
          {
            errors.add(name,new ValidationError("Non è possibile inserire una data posteriore a quella odierna"));
            valDate=null;
          }
        }
      }
      else
      {
        errors.add(name,new ValidationError("Data non valida"));
      }
    }
    return valDate;
  }

  /**
   * Effettua controlli di validità sulla data posteriore alla data minToday
   * @param date
   * @param name name da usare nella add dell'errors
   * @param txtName nome del campo per esteso (da visualizzare all'utente in caso di errore)
   * @param errors
   * @param required indica se la data è obbligatoria
   * @param maxToday indica se la data deve essere inferiore a quella odierna
   * @return data come oggetto Date
   */
  public static Date validateDateAfterToDay(String date,String name,String txtName,ValidationErrors errors, boolean required, boolean minToday)
  {
    Date valDate=null;
    if (!Validator.isNotEmpty(date))
    {
      if (required)
      {
        errors.add(name,new ValidationError("Inserire la "+txtName));
      }
    }
    else
    {
      if (Validator.isDate(date))
      {
        if (date.trim().length()!=10)
        {
          errors.add(name,new ValidationError("Inserire la data nel formato gg/mm/aaaa"));
        }
        else
        {
          try
          {
            valDate=validateDate(date);
          }
          catch(Exception e)
          {
            errors.add(name,new ValidationError("Data non valida"));
            return null;
          }
          Date toDay = new Date();
                 //if (minToday && new Date().compareTo(valDate)>0 && (!DateUtils.isToday(valDate)))
           if (minToday && toDay.after(valDate))
           {
             errors.add(name,new ValidationError("Non è possibile inserire una data anteriore a quella odierna"));
             valDate=null;
           }
        }
      }
      else
      {
        errors.add(name,new ValidationError("Data non valida"));
      }
    }
    return valDate;
  }

  /**
   * Controlla se la dataFine è successiva, o uguale, alla dataInizio
   * @param dataInizio
   * @param dataFine
   * @param name
   * @param canBeEqual indica se la dataInizio e la dataFine possono essere uguali
   * @param errors
   */
  public static void validateDateAfterDate(String dataInizio, String dataFine, String name, boolean canBeEqual, ValidationErrors errors)
  {
    Date dateStart = new Date();
    Date dateEnd = new Date();

    if (Validator.isDate(dataInizio) && Validator.isDate(dataFine))
    {
      try
      {
        dateStart = validateDate(dataInizio);
        dateEnd = validateDate(dataFine);
      }
      catch (Exception ex)
      {
        errors.add(name,new ValidationError("Data non valida"));
        return;
      }

      int confronto = dateStart.compareTo(dateEnd);
      if(confronto>0 ||(!canBeEqual && confronto==0))
      {
        errors.add(name,new ValidationError("La Data Fine deve essere successiva alla Data Inizio"));
      }
    }
    else
    {
      errors.add(name,new ValidationError("Data non valida"));
    }
  }

  /**
   * Effettua controlli di validità sulla data posteriore alla data minDate
   * @param date
   * @param name name da usare nella add dell'errors
   * @param txtName nome del campo per esteso (da visualizzare all'utente in caso di errore)
   * @param errors
   * @param required indica se la data è obbligatoria
   * @param maxToday indica se la data deve essere inferiore a quella odierna
   * @return data come oggetto Date
   */
  public static Date validateDateAfterDate(String newDate, String minDate, String name,String txtName,ValidationErrors errors, boolean required)
  {
    Date valDate=null;
    if (!Validator.isNotEmpty(newDate))
    {
      if (required)
      {
        errors.add(name,new ValidationError("Inserire la "+txtName));
      }
    }
    else
    {
      if (Validator.isDate(newDate))
      {
        if (newDate.trim().length()!=10)
        {
          errors.add(name,new ValidationError("Inserire la data nel formato gg/mm/aaaa"));
        }
        else
        {
          try
          {
            valDate=validateDate(newDate);
          }
          catch(Exception e)
          {
            errors.add(name,new ValidationError("Data non valida"));
            return null;
          }
          Date dMinDate = validateDate(minDate);
                 //if (minToday && new Date().compareTo(valDate)>0 && (!DateUtils.isToday(valDate)))
           if (dMinDate.after(valDate))
           {
             errors.add(name,new ValidationError("Non è possibile inserire una data anteriore al " + minDate));
             valDate=null;
           }
        }
      }
      else
      {
        errors.add(name,new ValidationError("Data non valida"));
      }
    }
    return valDate;
  }

  //=======================================================================
  // Controlla la correttezza di un formato time hh:mm:ss
  //=======================================================================
  public static boolean validateTime(String strTime,
                                     String name,
                                     String txtName,
                                     ValidationErrors errors,
                                     boolean required)  {
    boolean result = true;
    if (!Validator.isNotEmpty(strTime)) {
      if (required) {
        errors.add(name,new ValidationError("Inserire l''orario "+txtName));
      }
    } else {
      if (strTime.trim().length() != 8) {
        errors.add(name,new ValidationError("Inserire l''orario nel formato hh:mm:ss"));
      }
      else {
        StringTokenizer st = new StringTokenizer(strTime, ":");
        if (st.countTokens() != 3) {
          errors.add(name, new ValidationError("Inserire l''orario nel formato hh:mm:ss"));
          result = false;
        }
        int token = 0;
        while (st.hasMoreTokens() && (result != false)) {
          int intToken = 0;
          try {
            intToken = new Integer(st.nextToken()).intValue();
          } catch (NumberFormatException e) {
            errors.add(name, new ValidationError("Inserire l''orario nel formato hh:mm:ss"));
            result = false;
          }
          switch (token) {
            case 0:
              if (intToken > 23) {
                errors.add(name,new ValidationError("Orario non corretto. Inserire l''orario nel formato hh:mm:ss"));
                result = false;
              }
              break;
            case 1:
              if (intToken > 59) {
                errors.add(name,new ValidationError("Orario non corretto. Inserire l''orario nel formato hh:mm:ss"));
                result = false;
              }
              break;
            case 2:
              if (intToken > 59) {
                errors.add(name,new ValidationError("Orario non corretto. Inserire l''orario nel formato hh:mm:ss"));
                result = false;
              }
              break;
          }
          token++;
        }
      }
    }
   return result;
  }

  //Conversioni al volo di stringhe in date con pattern specificato
  public static Date parseDate(String data, String pattern)
                                                        throws ParseException {
    SimpleDateFormat dateFormatter = null;
    dateFormatter = new SimpleDateFormat(pattern);
    dateFormatter.setLenient(false);
    return dateFormatter.parse(data);
  }

  //Conversioni al volo di stringhe in date con pattern specificato
  // restituisce null, se la stringa non è parsificabile
  public static Date parseDateCatch(String data, String pattern){
    Date result;
    try{
      SimpleDateFormat dateFormatter = null;
      dateFormatter = new SimpleDateFormat(pattern);
      dateFormatter.setLenient(false);
      result = dateFormatter.parse(data);
    }
    catch(ParseException ex){
      result=null;
    }
    return result;
  }

  /**
   * Indica se una stringa è di tipo alfanumerico. Per alfanumerico si intente
   * composta solo caratteri 'A'..'Z' e '0'..'9'.
   * @param value
   * @return true se la stringa è alfanumerica (oppure null), false altrimenti
   */
  public static boolean isAlphaNumeric(String value)
  {
    if (value!=null)
    {
      int length=value.length();
      value = value.toUpperCase();
      for(int i=0;i<length;i++)
      {
        if (ALPHA.indexOf(value.charAt(i))<0)
        {
          return false;
        }
      }
    }
    return true;
  }
  /**
   * Algoritmo di controllo sulla validità del codice IBAN di un conto corrente
   * @author 71554 (Garonzi Alessio)
   * @param Iban
   * @return
   */
  public static String checkIBAN(String Iban){

	   if(null == Iban)
		   return AnagErrors.ERRORE_CAMPO_ERRATO;
	   
	   if(!Validator.isNotEmpty(Iban))
		   return AnagErrors.ERRORE_CAMPO_OBBLIGATORIO;

	   //if(Iban.length() < 5)
		   //return "La lunghezza è minore di 5 caratteri";
	   
	   for(int i=0;i<Iban.length();i++){
		   if(Character.isLowerCase(Iban.charAt(i)))
			   return "Il codice IBAN non può contenere caratteri minuscoli";

	   }
	   
	   String subIban = Iban.substring(4,Iban.length()) + Iban.substring(0,4);
	   String numIban = "";
	   Vector<Object> ibanVect = new Vector<Object>();
	   setMapValueIBAN();
	   for(int i=0; i< subIban.length();i++){		   
		 if(!Validator.isNumericInteger(String.valueOf(subIban.charAt(i))))
			 numIban = numIban + getValueatChar(String.valueOf(subIban.charAt(i)));
		 else
			 numIban = numIban + String.valueOf(subIban.charAt(i));	   
	   }
	   int lenIban = numIban.length();
	   int Tok = lenIban / 8;
	   int res = lenIban % 8;
	   int x = 0;
	   while(Tok > 0){
	   ibanVect.add(numIban.substring(x,x + 8));
	   x = x + 8;
	   Tok--;
	   }
	   if(res > 0)
		   ibanVect.add(numIban.substring(numIban.length() - res,numIban.length())); 
	   
	   long numdiv = -1;
	   String numdivStr = "";
	   for(int i=0;i<ibanVect.size();i++){
		   numdivStr = numdivStr + (String)ibanVect.get(i);
		   numdiv = Long.parseLong(numdivStr) % 97;
		   numdivStr = String.valueOf(numdiv);	   
	   }
	  
	   if(numdiv != 1)
		   return "IL CODICE IBAN NON RISULTA CORRETTO";
	   else
		   return "OK";
  }
  
  
  /**
   * Controllo la validità di un codice BBAN dato dall'unione del CIN,ABI,CAB e
   * numero di cc. Non vengono fatti controlli sui valori di CIN,ABI,CAB e
   * numero di cc ma solo sul BBAN corrispondente.
   * @param CIN
   * @param ABI
   * @param CAB
   * @param cc
   * @return
   */
  public static boolean checkBBAN(char CIN,String ABI,String CAB, String cc)
  {
    StringBuffer sb=new StringBuffer();
    sb.append(CIN);
    sb.append(ABI);
    sb.append(CAB);
    sb.append(cc);
    char bban[]=sb.toString().toUpperCase().toCharArray();
    if (bban.length!=23)
    {
      return false;
    }
    String lettere=vocali+consonanti;
    // tutti gli indici partono da 0
    if (lettere.indexOf(bban[0])<0)
    {
      // Il CIN deve essere una lettera
      return false;
    }
    for(int i=1;i<11;i++)
    {
      if (CIFRE.indexOf(bban[i])<0)
      {
        // Le posizioni dalla 1 alla 10 devono essere occupate da cifre
        return false;
      }
    }
    String valoriValidi=lettere+CIFRE;
    for(int i=11;i<23;i++)
    {
      if (valoriValidi.indexOf(bban[i])<0)
      {
        // Sono ammesse solo lettere e cifre
        return false;
      }
    }
    int valoreControllo=0;
    for(int i=1;i<23;i++)
    {
      int val=0;
      if (lettere.indexOf(bban[i])>=0)
      {
        val=bban[i]-'A';
      }
      else
      {
        val=bban[i]-'0';
      }
      if ( (i & 1)==0) // pari
      {
        valoreControllo+=val;
      }
      else
      {
        valoreControllo+=CONTRIBUTO_BBAN[val];
      }
    }
    return bban[0]==(byte)((valoreControllo % 26)+'A');
  }

  /**
   * Confronta due oggetti: restituisce true se sono diversi
   * */
  public static boolean confrontaOggetto(Object a,Object b)
  {
    if (a!=null && b!=null)
    {
      if (!a.equals(b))
        return true;
    }
    else
    {
      if (!(a==null && b==null))
        return true;
    }
    return false;
  }

  /**
   * Confronta due stringhe: questo metodo è diverso da quello che confronta
   * gli object perchè per noi una stringa vuota è uguale ad una stringa
   * che contiene null (anche perchè il DB non distingue fra stringa vuota
   * stringa null)
   * */
  public static boolean confrontaOggetto(String a,String b)
  {
    if (a!=null && b!=null)
    {
      if (!a.equals(b))
        return true;
    }
    else
    {
      if (!((a==null || "".equals(a))
            &&
            (b==null || "".equals(b))))
        return true;
    }
    return false;
  }
  public static String getValueatChar(String ch){
	  return (String)MapValueIBAN.get(ch);
  }

  
  public static boolean isEmpty(String field)
  {
    return !isNotEmpty(field);
  }

  public static boolean isEmpty(Object field)
  {
    return !isNotEmpty(field);
  }
  
  
  public static boolean isCapOk(String field)
  {
    field = field.trim();
    if((field.length() == 5) && isNumericInteger(field))
    {
      return true;
    }
    return false;
  }
  
  
  public static boolean isFilenameValid(String file) 
  {
    File f = new File(file);
    try 
    {
      f.getCanonicalPath();
      return true;
    } 
    catch (IOException e) 
    {
      return false;
    }
  }
  
}
