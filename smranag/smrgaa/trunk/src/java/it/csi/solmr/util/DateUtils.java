package it.csi.solmr.util;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * <br>
 *
 * Classe di utilità generica
 *
 * @author Luca Romanello e Ivan Morra
 * @version 1.0
 */

//import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public abstract class DateUtils {
  /**
  */
  public final static String DATA     = "dd/MM/yyyy";
  /**
  */
  public final static String ORA      = "HH:mm:ss";
  /**
  */
  public final static String DATA_ORA = "dd/MM/yyyy HH:mm:ss";
  
  public final static String DATA_TRAT     = "dd-MM-yyyy";
  
  public final static String DATA_TERAMO     = "yyyy-MM-dd";

  public final static int DAYS   = 0;
  public final static int MONTHS = 1;
  public final static int YEARS  = 2;

  /**
   *
   * @return l'anno corrente
   */
  public static Integer getCurrentYear() {
    return getCurrentPartial(Calendar.YEAR);
  }

  /**
   *
   * @return il mese corrente
   */
  public static Integer getCurrentMonth() {
    return new Integer(getCurrentPartial(Calendar.MONTH).intValue()+1);
  }

  /**
   *
   * @return il giorno corrente
   */
  public static Integer getCurrentDay() {
    return getCurrentPartial(Calendar.DAY_OF_MONTH);
  }

  /**
   *
   * @param whichOne quale parte della data odierna (giorno, mese o anno) bisogna restituire
   * @return la parte della data odierna richiesta
   */
  private static Integer getCurrentPartial(int whichOne) {
    Integer currPart = null;
    currPart = new Integer(getCurrentCalendar().get(whichOne));
    return currPart;
  }

  /**
   *
   * @return la data e ora corrente
   */
  public static String getCurrent() {
    return getCurrent(DATA_ORA);
  }

  public static String getCurrentDateString() {
    try {
      String giorno="";
      if(extractDayFromDate(parseDate(getCurrent()))<=9)
        giorno = "0"+extractDayFromDate(parseDate(getCurrent()));
      else
        giorno = ""+extractDayFromDate(parseDate(getCurrent()));
      String mese="/";
      String anno="/"+extractYearFromDate(parseDate(getCurrent()));
      if(extractMonthFromDate(parseDate(getCurrent()))<=9)
        mese += "0"+extractMonthFromDate(parseDate(getCurrent()));
      else mese += ""+extractMonthFromDate(parseDate(getCurrent()));
      return giorno+mese+anno;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   *
   * @param format formato da utilizzare per la data/ora corrente
   * @return la data/ora corrente formattata come richiesto
   */
  public static String getCurrent(String format) {
    String current = null;
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    current = sdf.format(getCurrentCalendar().getTime());
    return current;
  }

  /**
   *
   * @param time la string con l'ora da trasformare in un oggetto data
   * @return l'oggetto data parsificato secondo il contenuto della costante ORA
   * @throws Exception se la parsificazione fallisce
   */
  public static Date parseTime(String time) throws Exception {
    return parse(time, ORA);
  }

  /**
   *
   * @param date la stringa con la data da trasformare in un oggetto data
   * @return l'oggetto data parsificato secondo il contenuto della costante DATA
   * @throws Exception se la parsificazione fallisce
   */
  public static Date parseDate(String date) throws Exception {
    return parse(date, DATA);
  }
  
  public static Date parseDateTrat(String date) throws Exception {
    return parse(date, DATA_TRAT);
  }
  
  public static Date parseDateTeramo(String date) throws Exception {
    return parse(date, DATA_TERAMO);
  }

  /**
   *
   * @param fullDate la stringa con la data completa (data + ora) da trasformare in un
   *                 oggetto data
   * @return l'oggetto data parsificato secondo il contenuto della costante DATA_ORA
   * @throws Exception se la parsificazione fallisce
   */
  public static Date parseFullDate(String fullDate) throws Exception {
    return parse(fullDate, DATA_ORA);
  }


  /**
   *
   * @param day
   * @param month
   * @param year
   * @return date formattata in giorno/mese/anno
   * @throws Exception
   */
  public static Date parseDate(String day, String month, String year) throws Exception {
    return parse(day+"/"+month+"/"+year, DATA);
  }

  /**
   *
   * @param toParse la stringa da trasformare in un oggetto data
   * @param format il formato secondo il quale parsificare la stringa
   * @return l'oggetto data parsificato  secondo il contenuto del parametro format
   * @throws Exception se la parsificazione fallisce
   */
  public static Date parse(String toParse, String format) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.parse(toParse);
  }

  /**
   *
   * @return un'istanza di Calendar corrispondente alla data di sistema
   */
  private static Calendar getCurrentCalendar() {
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    cal.setTime(new Date(System.currentTimeMillis()));
    return cal;
  }

  /**
   *
   * @param month il mese su cui effettuare il calcolo
   * @param year l'anno su cui effettuare il calcolo
   * @return il numero di giorni nel mese e anno selezionati
   */
  public static int getDayInMonth(Integer month, Integer year) {
    if (month.intValue()==2) {
      int iYear = year.intValue();
      return iYear <=0 || (iYear % 4 != 0 && iYear % 100 == 0) && iYear % 400 != 0 ? 28 : 29;
    } else
      switch(month.intValue()) {
	case 4:
	case 6:
	case 9:
	case 11:
	  return 30;
	default:
	  return 31;
      }
  }

  public static int getAgeInDays(Date firstDate, Date secondDate) {
    return minus(firstDate, secondDate, DateUtils.DAYS).intValue();
  }

  public static int getAgeInMonths(Date firstDate, Date secondDate) {
    return minus(firstDate, secondDate, DateUtils.MONTHS).intValue();
  }

  public static int getAgeInYears(Date firstDate, Date secondDate) {
    return minus(firstDate, secondDate, DateUtils.YEARS).intValue();
  }

  private static Integer minus(Date beginDate, Date endDate, int unit) {
    Integer difference = null;
    if (beginDate!=null && endDate!=null) {
      if (beginDate.after(endDate)) {
        Date swapDate = endDate;
        endDate = beginDate;
        beginDate = swapDate;
      }
      if (unit != DateUtils.DAYS) {
        int riportoMese = 0;
        int riportoAnno = 0;
        if (extractDayFromDate(endDate)<extractDayFromDate(beginDate))
          riportoMese = -1;
        if (extractMonthFromDate(endDate)<extractMonthFromDate(beginDate)) {
          riportoMese = riportoMese +
                        (12 +
                        extractMonthFromDate(endDate) -
                        extractMonthFromDate(beginDate));
          riportoAnno = -12+riportoMese;
        } else {
          riportoMese = riportoMese +
                        (extractMonthFromDate(endDate) -
                        extractMonthFromDate(beginDate));
          riportoAnno = riportoMese;
        }
        riportoAnno = riportoAnno +
                      12 * (extractYearFromDate(endDate) -
                      extractYearFromDate(beginDate));
        switch (unit) {
          case DateUtils.MONTHS:
            difference = new Integer(riportoAnno);
            break;
          case DateUtils.YEARS:
            difference = new Integer(riportoAnno/12);
            break;
        }
      } else {
        long lBeginDate = beginDate.getTime();
        long lEndDate   = endDate.getTime();
        long lDiff      = lEndDate - lBeginDate;
        difference = new Integer(""+lDiff/(1000*60*60*24));
      }
    }
    return difference;
  }

  public static int extractDayFromDate(Date date) {
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    cal.setTime(date);
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  public static int extractMonthFromDate(Date date) {
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    cal.setTime(date);
    return (cal.get(Calendar.MONTH)+1);
  }

  public static int extractYearFromDate(Date date) {
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    cal.setTime(date);
    return cal.get(Calendar.YEAR);
  }
  
  public static int extractHourFromDate(Date date) {
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    cal.setTime(date);
    return cal.get(Calendar.HOUR_OF_DAY);
  }
  
  public static int extractMinuteFromDate(Date date) {
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    cal.setTime(date);
    return cal.get(Calendar.MINUTE);
  }
  
  public static int extractSecondFromDate(Date date) {
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    cal.setTime(date);
    return cal.get(Calendar.SECOND);
  }

  //Metodo che restituisce true se la data passata come parametro è quella odiernA
  public static boolean isToday(Date data) {
    GregorianCalendar dataInput = new GregorianCalendar();
    Calendar dataOggi = Calendar.getInstance();
    dataInput.setTime(data);

    return (dataOggi.get(Calendar.DAY_OF_MONTH) == dataInput.get(Calendar.DAY_OF_MONTH)
            &&dataOggi.get(Calendar.MONTH) == dataInput.get(Calendar.MONTH)
            &&dataOggi.get(Calendar.YEAR) == dataInput.get(Calendar.YEAR));
  }


  // Metodo per aumentare o decrementare una data
  public static Date rollDate(Date date, int calendarField, int amount) {
    Date result = null;
    if(date != null) {
      Calendar rollableDate = Calendar.getInstance();
      rollableDate.setTime(date);
      rollableDate.setLenient(false);
      rollableDate.add(calendarField, amount);
      result = new Date(rollableDate.getTime().getTime());
    }
    return result;
  }

  // Metodo per ottenere l'ultimo giorno corretto a partire da mese e anno
  public static Date dataFinePeriodo(String meseAnno) throws Exception{
    return getDataPeriodo(meseAnno, false);
  }


  private static Date getDataPeriodo(String periodo, boolean isInizio) throws Exception{
    Date result = null;
    int indiceSlash = periodo.indexOf("/");

    if(indiceSlash>0)
    {
      String mese = periodo.substring(0, indiceSlash);
      String anno = periodo.substring(indiceSlash+1);

      if(isInizio)
        result = parseDate("01", mese, anno);
      else
        result = parseDate(String.valueOf(getDayInMonth(new Integer(mese), new Integer(anno))));
    }

    return result;
  }

  public static String formatDate(Date date) {
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    cal.setTime(date);
    String day = ""+cal.get(Calendar.DAY_OF_MONTH);
    String month = ""+(cal.get(Calendar.MONTH)+1);
    String year = ""+cal.get(Calendar.YEAR);
    if(day.length()==1)
      day="0"+day;
    if(month.length()==1)
      month="0"+month;
    String returnDate;
    try{
      returnDate = day+"/"+month+"/"+year;
    }catch(Exception ex){
      returnDate="";
    }
    return returnDate;
  }
  
  public static String formatDateNotNull(Date date) {
    
    String returnDate = "";
    
    if(date !=null)
    {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault());
      cal.setTime(date);
      String day = ""+cal.get(Calendar.DAY_OF_MONTH);
      String month = ""+(cal.get(Calendar.MONTH)+1);
      String year = ""+cal.get(Calendar.YEAR);
      if(day.length()==1)
        day="0"+day;
      if(month.length()==1)
        month="0"+month;
      
      returnDate = day+"/"+month+"/"+year;
    }
    
    return returnDate;
  }
  
  
public static String formatDateTimeNotNull(Date date) {
    
    String returnDate = "";
    
    if(date !=null)
    {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault());
      cal.setTime(date);
      String day = ""+cal.get(Calendar.DAY_OF_MONTH);
      String month = ""+(cal.get(Calendar.MONTH)+1);
      String year = ""+cal.get(Calendar.YEAR);
      if(day.length()==1)
        day="0"+day;
      if(month.length()==1)
        month="0"+month;
      
      String hour = ""+cal.get(Calendar.HOUR_OF_DAY);
      if(hour.length()==1)
        hour="0"+hour;
      String minute = ""+cal.get(Calendar.MINUTE);
      if(minute.length()==1)
        minute="0"+minute;
      String second = ""+cal.get(Calendar.SECOND);
      if(second.length()==1)
        second="0"+second;
      
      returnDate = day+"/"+month+"/"+year+" "+hour+":"+minute+":"+second;
    }
    
    return returnDate;
  }

  // Metodo che restituisce la data di nascita a partire dal codice fiscale
  public static Date getDataNascitaFromCF(String codFiscale) {
    int giorno;
    int mese;
    int anno;
    int annoCorrente;

    String mesiCF = "ABCDEHLMPRST";

    Date result = null;

    try {
      anno = Integer.parseInt(codFiscale.substring(6,8));
      mese = mesiCF.indexOf(codFiscale.toUpperCase().charAt(8))+1;
      giorno = Integer.parseInt(codFiscale.substring(9,11));

      anno += 2000;

      annoCorrente = Calendar.getInstance().get(Calendar.YEAR);

      if(anno > annoCorrente)
        anno -= 100;

      if(giorno>40)
        giorno -= 40;
      result = parseDate(String.valueOf(giorno), String.valueOf(mese), String.valueOf(anno));
    }
    catch(Exception e) {
    }
    return result;
  }

  /**
   * Metodo che restituisce la data della Pasquetta dell'anno passato come argomento
   * secondo il calendario gregoriano (viene calcolata la Pasqua Cattolica)
   * mediante l'algoritmo di Oudin-Tøndering
   *
   * http://xoomer.virgilio.it/esongi/oudin.htm
   *
   * Serve per l'elenco delle verifiche (CU-GUMA-86)
   * @param anno
   * @return Oggetto GregorianCalendar con la data della Pasquetta. Serve per poi usare il metodo
   */
  public static Date getPasquettaGregoriana(String anno) {
    int annoPasqua = Integer.parseInt(anno);
    int restoCicliMetone = annoPasqua%19;
    int secoli = annoPasqua/100;
    int fattore1 = (secoli-secoli/4-(8*secoli+13)/25+19*restoCicliMetone+15)%30;
    int fattore2 = fattore1-fattore1/28*(1-(fattore1/28)*(29/(fattore1+1))*((21-restoCicliMetone)/11));
    int fattore3 = (annoPasqua+annoPasqua/4+fattore2+2-secoli+secoli/4)%7;
    int criterioPasquale = fattore2 - fattore3;
    int mesePasqua = 3+(criterioPasquale+40)/44;
    int giornoPasqua = criterioPasquale+28-31*(mesePasqua/4);

    /****************************************************************************************\
      Il meccanismo scelto per passare da Pasqua a Pasquetta è un po' più pulito e sicuro di

        Calendar gc = new GregorianCalendar(annoPasqua,mesePasqua-1,giornoPasqua+1);

      anche se funziona lo stesso, perfino se si inserisce il 31 febbraio!
    \****************************************************************************************/
    GregorianCalendar pasquetta = new GregorianCalendar(annoPasqua,mesePasqua-1,giornoPasqua);
    pasquetta.add(Calendar.DAY_OF_MONTH, 1);
    return pasquetta.getTime();
  }

  /**
   * Metodo che restituisce un Vector contenente le date delle festività
   * nell'anno passato come parametro, incluso il giorno di Pasquetta
   * @param anno String contenente l'anno per il quale si vuole l'elenco delle festività
   * @return Vector contenente le date
   */
  public static HashMap<Object,Object> getElencoFestivita(String anno){
    int annoInt = Integer.parseInt(anno);
    HashMap<Object,Object> elencoFestivita = new HashMap<Object,Object>();
    GregorianCalendar data = new GregorianCalendar(annoInt,0,1); // 1 gennaio
    elencoFestivita.put(formatDate(data.getTime()),"");
    data.set(annoInt,0,6);  // 6 gennaio
    elencoFestivita.put(formatDate(data.getTime()),"");
    elencoFestivita.put(formatDate(getPasquettaGregoriana(anno)),""); // Pasquetta (18 marzo-20 aprile)
    data.set(annoInt,3,25); // 25 aprile
    elencoFestivita.put(formatDate(data.getTime()),"");
    data.set(annoInt,4,1); // 1 maggio
    elencoFestivita.put(formatDate(data.getTime()),"");
    data.set(annoInt,5,2); // 2 giugno
    elencoFestivita.put(formatDate(data.getTime()),"");
    data.set(annoInt,7,15); // 15 agosto
    elencoFestivita.put(formatDate(data.getTime()),"");
    data.set(annoInt,10,1); // 1 novembre
    elencoFestivita.put(formatDate(data.getTime()),"");
    data.set(annoInt,11,8); // 8 dicembre
    elencoFestivita.put(formatDate(data.getTime()),"");
    data.set(annoInt,11,25); // 25 dicembre
    elencoFestivita.put(formatDate(data.getTime()),"");
    data.set(annoInt,11,26); // 26 dicembre
    elencoFestivita.put(formatDate(data.getTime()),"");
    return elencoFestivita;
  }

  public static boolean isLeapYear(String anno){
    int annoInt = Integer.parseInt(anno);
    GregorianCalendar gc = new GregorianCalendar();
    return gc.isLeapYear(annoInt);
  }

  public static String getDescrizioneMese(String mese) {
    int intMese = new Integer(mese).intValue();
    String result = null;
    switch (intMese) {
      case 1:  result = "Gennaio";   break;
      case 2:  result = "Febbraio";  break;
      case 3:  result = "Marzo";     break;
      case 4:  result = "Aprile";    break;
      case 5:  result = "Maggio";    break;
      case 6:  result = "Giugno";    break;
      case 7:  result = "Luglio";    break;
      case 8:  result = "Agosto";    break;
      case 9:  result = "Settembre"; break;
      case 10: result = "Ottobre";   break;
      case 11: result = "Novembre";  break;
      case 12: result = "Dicembre";  break;
    }
    return result;
  }
  
  
  public static Date parseDateNull(String date) throws Exception 
  {
    if(Validator.isNotEmpty(date))
    {
      return parse(date, DATA);
    }
    
    return null;
  }
  
  public static Date convert(XMLGregorianCalendar xmlGregorianDate){
	  Date date=null;
	  
	  if (xmlGregorianDate!=null)
		date=new Date(xmlGregorianDate.toGregorianCalendar().getTimeInMillis());
	  return date;
  }
  
  //Convert Calendar to Date
  public static Date convertCalendar(Calendar c)
  {
	  if (c!=null)
		  return  c.getTime();
	  else return null;
  }
  
  public static Calendar convertDate(Date d)
  {
	  if (d!=null){
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTime(d);
		  return calendar;
	  }
	  else return null;
  }
  
  
  public static String convertXmlGregorianToString(XMLGregorianCalendar xc) throws Exception{
	  SolmrLogger.debug("DateUtils", "BEGIN convertXmlGregorianToString");
	  String dateString = null;
	  if(xc != null){
		  //Locale.setDefault(Locale.ITALIAN);
	      DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH.mm.ss");
	
	      GregorianCalendar gCalendar = xc.toGregorianCalendar();
	
	      //Converted to date object
	      Date date = gCalendar.getTime();
	
	      //Formatted to String value
	      dateString = df.format(date);
	  }
	  SolmrLogger.debug("DateUtils", "END convertXmlGregorianToString");
      return dateString;
  }
  
  public static XMLGregorianCalendar convertDateToXmlGregorianCalendar(Date data) throws Exception{
	  SolmrLogger.debug("DateUtils", "BEGIN convertDateToXmlGregorianCalendar");
	  XMLGregorianCalendar xmlDate = null;
	  GregorianCalendar gc = new GregorianCalendar();
	  gc.setTime(data);

	  try{
	     xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
	  }
	  catch(Exception e){
		  SolmrLogger.error("DateUtils", "Exception in convertDateToXmlGregorianCalendar ="+e.getMessage());
	    throw e;
	  }
	  finally{
		  SolmrLogger.debug("DateUtils", "E_ND convertDateToXmlGregorianCalendar"); 
	  }
	  return xmlDate;
  }
}
