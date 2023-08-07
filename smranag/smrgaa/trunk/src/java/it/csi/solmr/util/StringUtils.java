package it.csi.solmr.util;

/**
 * <p>Title: Servizi Educativi</p>
 * <p>Description: Classe astratta contenente metodi di utilità per le stringhe</p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: CSI Piemonte</p>
 * @author Ivan Morra
 * @author Michele Piantà
 * @version 1.1
 */
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttAziendaVO;
import it.csi.solmr.dto.anag.attestazioni.ParametriAttDichiarataVO;
import it.csi.solmr.dto.anag.attestazioni.TipoAttestazioneVO;
import it.csi.solmr.dto.anag.terreni.TipoCausaleModificaVO;
import it.csi.solmr.dto.anag.terreni.TipoMacroUsoVO;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

public abstract class StringUtils {

	public static String checkNull(String stringa) {
		if(stringa==null)
			stringa="";
		return stringa;
	}

	public static String checkNull(Object obj) {
		if(obj ==null)
			return new String("");
		else
			return obj.toString();
	}

	public static String parseQuote(String stringa) {
		String result = "";

		if(stringa==null||stringa.length()==0)
			return "";

		StringTokenizer st = new StringTokenizer(stringa,"\"");
		while (st.hasMoreTokens()) {
			result += st.nextToken() + "&#34;";
		}

		if(!stringa.endsWith("\""))
			result = result.substring(0,result.length()-5);

		return result;
	}

	public static String parseBracket(String stringa) {
		String result = "";
		StringTokenizer st = null;

		if(stringa==null||stringa.length()==0)
			return "";

		st = new StringTokenizer(stringa,"<");
		while (st.hasMoreTokens()) {
			result += st.nextToken() + "&lt;";
		}

		if(!stringa.endsWith("<"))
			result = result.substring(0,result.length()-4);

		return result;
	}

	public static String parseDateField(String stringa) {
		String result;
		if(stringa == null||stringa.length()==0)
			return stringa;

		result = stringa;

		try {
			int campo = Integer.parseInt(result);
			if(campo < 10)
				result = "0"+campo;
		}
		catch(NumberFormatException nfe) {
			return parseQuote(result);
		}

		return result;
	}

	// Metodo per parsificare la stringa data dal formato americano "aaaa-mm-dd" a quello
	// europeo "dd/mm/yyyy"
	public static String parseDateFieldToEuropeStandard(String patterFrom, String patternTo, String data) {
		String result = "";
		try {
			SimpleDateFormat dateFormat_one = new SimpleDateFormat(patterFrom);
			SimpleDateFormat dateFormat_two = new SimpleDateFormat(patternTo);
			result = dateFormat_two.format(dateFormat_one.parse(data));
		}
		catch(ParseException pe) {
			return null;
		}
		return result;
	}

        public static String parseNumeroCapi(String stringa) {

                String euroPattern = "#,###";
                return parseNumericField(stringa, euroPattern);
        }

        public static String parsePesoCapi(String stringa) {

                String euroPattern = "#,##0.0";
                return parseNumericField(stringa, euroPattern);
        }
        
        public static String parsePesoCapiMod(String stringa) {

          String euroPattern = "###0.0";
          return parseNumericField(stringa, euroPattern);
        }



	public static String parseEuroField(String stringa) {

		String euroPattern = "###0.00";
		return parseNumericField(stringa, euroPattern);
	}

	public static String parseDoubleField(String stringa) {
		String doublePattern = "###0.0#";
		return parseNumericField(stringa, doublePattern);
	}
	
	public static String parseDoubleFieldOneDecimal(String stringa) {
    String doublePattern = "###0.0";
    return parseNumericField(stringa, doublePattern);
  }
	
	public static String parseDoubleFieldTwoDecimal(String stringa) {
    String doublePattern = "###0.00";
    return parseNumericField(stringa, doublePattern);
  }
	
	public static String parseDoubleFieldBigDecimal(BigDecimal stringaBig) 
	{
	  if(stringaBig !=null)
    { 
	    String stringa = stringaBig.toString();
      String doublePattern = "###0.0";
      return parseNumericField(stringa, doublePattern);
    }
	  else
	  {
	    return "";
	  }
  }

	public static String parseUBAField(String stringa) {
		String doublePattern = "####0.0###";
		return parseNumericField(stringa, doublePattern);
	}

	public static String parseSuperficieField(String stringa) {
		String doublePattern = "#########0.0000";
		return parseNumericField(stringa, doublePattern);
	}
	
	public static String parseSuperficieFieldBigDecimal(BigDecimal stringaBig) 
	{
	  if(stringaBig !=null)
	  {
	    String stringa = stringaBig.toString();
	    String doublePattern = "#########0.0000";
	    return parseNumericField(stringa, doublePattern);
	  }
	  else
	  {
	    return "";
	  }
  }

	public static String parseIntegerField(String stringa) {
		String integerPattern = "####";
		return parseNumericField(stringa, integerPattern);
	}
	
	public static String parseIntegerFieldBigDecimal(BigDecimal stringaBig) 
	{
	  if(stringaBig !=null)
    { 
      String stringa = stringaBig.toString();
      String integerPattern = "####";
      return parseNumericField(stringa, integerPattern);
    }
	  else
	  {
	    return "";
	  }
	  
  }

	private static String parseNumericField(String stringa, String pattern) {
		String result = stringa;
		if(stringa != null&&stringa.length()>0) {
			try {
				NumberFormat nf = new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ITALY));
				result = nf.format(Double.parseDouble(stringa));
			}
			catch(Exception exc) {
				result = parseQuote(stringa);
			}
		}
		return result;
	}

	public static double convertNumericField(String field) throws ParseException {
		double result = 0;
		if(field!=null) {
			NumberFormat nf =
				new DecimalFormat("###,##0.00", new DecimalFormatSymbols(Locale.ITALY));

			checkNumericField(field);
			//Si considera anche il punto come un separatore decimale
			field = field.replace('.', ',');
			result = nf.parse(field).doubleValue();
		}
		return result;
	}

	private static void checkNumericField(String field) throws ParseException  {
		field = field.replace(',', '.');

		try {
			Double.parseDouble(field);
			//Occorre considerare l'eventualita di numeri con suffissi speciali,
			//riconosciuti da Java come facenti parte del formato numerico
			//(ad esempio, 20d o 134f). Per evitare che la conversione numerica
			//comprenda questi caratteri, la stringa viene spezzata in due
			if(field.length()>1)
				Double.parseDouble(field.substring(field.length()-1));
		}
		catch(NumberFormatException nfe) {
			throw new ParseException(nfe.getMessage(),0);
		}
	}

	public static String eliminaCarattere(String field, char c) {
		if(field != null && field.length()>0)
			while(field.indexOf(c)>=0) {
				if(field.indexOf(c)==0)
					field = field.substring(1);
				else {
					String parsedField = field.substring(0, field.indexOf(c));
					if(field.indexOf(c)<field.length()-1)
						parsedField += field.substring(field.indexOf(c)+1);
					field = parsedField;
				}
			}
		return field;
	}

	public static String replace(String text, String repl, String with) {
		//Oltre a controllare che il campo non sia vuoto, si verifica che la
		//stringa che deve sostituire non contenga quella da sostituire
		/*if(campo != null &&campo.length()>0) {
      for(int indice=0; indice<campo.length(); ) {
        indice = campo.indexOf(elimina, indice);
        if(indice==0)
          campo = rimpiazzo+campo.substring(elimina.length());
        else {
          String parsedField = campo.substring(0, indice)+rimpiazzo;
          if(indice<campo.length()-1)
            parsedField += campo.substring(indice+elimina.length());
          campo = parsedField;
        }
        indice += rimpiazzo.length();
      }
    }*/
		StringBuffer buf = new StringBuffer(text.length());
		int start = 0, end = 0;
		while ((end = text.indexOf(repl, start)) != -1) {
			buf.append(text.substring(start, end)).append(with);
			start = end + repl.length();
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	// Metodo che restituisce il sesso a partire dal codice fiscale
	public static String getSessoFromCF(String codiceFiscale) {
		String sesso = null;
		try {
			int valoreSesso = Integer.parseInt(codiceFiscale.substring(9,11));
			if(valoreSesso < 40) {
				sesso = "M";
			}
			else {
				sesso = "F";
			}
		}
		catch(Exception e) {
		}
		return sesso;
	}


	public static String convertiCFOmonimo(String codiceFiscale) {

		int posizioniCifre[] = {6,7,9,10,12,13,14};

		for(int i = 0; i < posizioniCifre.length; i++) {
			char carattere = codiceFiscale.charAt(posizioniCifre[i]);
			if(carattere < '0' || carattere > '9') {
				carattere = convertiDaCF(carattere);
				codiceFiscale = codiceFiscale.substring(0,posizioniCifre[i])+carattere+codiceFiscale.substring(posizioniCifre[i]+1);
			}
		}
		return codiceFiscale;
	}


	// Senza definizione ha accesso "package"
	static char convertiDaCF(char carattere) {
		char caratteriCF[] = {'L','M','N','P','Q','R','S','T','U','V'};
		char result = carattere;
		for(int i = 0; i < caratteriCF.length; i++) {
			if(caratteriCF[i] == carattere) {
				result = (char)('0'+i);
				break;
			}
		}
		return result;
	}

	// Senza definizione ha accesso "package"
	static char convertiInCF(char carattere) {
		char caratteriCF[] = {'L','M','N','P','Q','R','S','T','U','V'};
		char result = carattere;
		if(carattere >= '0' && carattere <='9') {
			result = caratteriCF[carattere - '0'];
		}
		return result;
	}

	// Codifica il double (xxxxx,x) nel formato (x.xxx,x000) a seconda del numero
	// massimo e minimo di cifre decimali passate come parametro
	public static String parseDoubleFractionDigits(Double number, int max, int min){
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setGroupingSeparator('.');
		df.setDecimalFormatSymbols(dfs);
		df.setMaximumFractionDigits(max);
		df.setMinimumFractionDigits(min);
		return df.format(number);
	}



	// Le seguenti righe di codice forniscono gli strumenti per
	// trasformare in lettere un numero espresso in cifre
	// es. 21318 = VENTUNOMILATRECENTODICIOTTO
	//
	// Attualmente si possono convertire solo numeri interi non negativi
	// inferiori ad un quadriliardo.
	// Visto che questa conversione nasce per gli euro, per il momento
	// si presuppone che tale estremo superiore sia ragionevolmente
	// sufficiente.
	//
	// "One Quintillion [Euros] ought to be enough for anybody"
	// (Michele Piantà, 17/11/2003)

	// "Costanti" di tipo String per la rappresentazione dei singoli elementi
	// costituenti il numero
	private static String[] NUM_UNITA=
	{"","Uno","Due","Tre","Quattro","Cinque","Sei","Sette","Otto","Nove"};
	private static String[] NUM_UNITA_BIS=
	{"Dieci","Undici","Dodici","Tredici","Quattordici","Quindici","Sedici","Diciassette","Diciotto","Diciannove"};
	private static final String[] NUM_DECINE=
	{"","","Venti","Trenta","Quaranta","Cinquanta","Sessanta","Settanta","Ottanta","Novanta"};
	private static String NUM_CENTO="Cento";
	private static String[] NUM_OLTRE=
	{"","mila","Milioni","Miliardi","Biliardi","Triliardi"};
	private static String[] NUM_OLTRE_UNO=
	{"","Mille","UnMilione","UnMiliardo","UnBiliardo","UnTriliardo"};

	// Trasforma in lettere il gruppo di tre cifre contenuto nel parametro numero
	// Il parametro gruppo serve per concatenare l'unità di misura
	private static String decodeGruppo(int gruppo, String numero)
	{
		int lungo = numero.length(), cifra, potenza;
		int ventina = 0, cento = 0;
		boolean numOltreUno = false;
		StringBuffer parola=new StringBuffer();

		for (int posizione=0; posizione<lungo; posizione++)
		{
			cifra = numero.charAt(posizione)-"0".charAt(0);
			potenza = lungo-posizione-1;
			switch (potenza)
			{
			case 0:
				numOltreUno = false;
				switch (ventina)
				{
				case 0:
					int lp=parola.length();
					switch (cifra)
					{
					case 0:
						break;
					case 1:
						if (lp == 0 && gruppo > 0)
						{
							numOltreUno = true;
							parola.append(NUM_OLTRE_UNO[gruppo]);
							break;
						}
					case 8:
						if (lp > 0 && cento == 0)
							parola.setLength(lp-1);
					default:
						parola.append(NUM_UNITA[cifra]);
					}
					break;
				default:
					parola.append(NUM_UNITA_BIS[cifra]);
				}
				cento = 0;
				if (parola.length()>0 && !numOltreUno)
					parola.append(NUM_OLTRE[gruppo]);
				break;
			case 1:
				switch (cifra)
				{
				case 0:
					break;
				case 1:
					ventina = 1;
					break;
				default:
					parola.append(NUM_DECINE[cifra]);
				cento = 0;
				}
				break;
			case 2:
				switch (cifra)
				{
				case 0:
				case 1:
					break;
				default:
					parola.append(NUM_UNITA[cifra]);
				}
				parola.append(cifra==0?"":NUM_CENTO);
				cento = 1;
				break;
			}
		}
		return parola.toString();
	}

	// Trasforma il numero contenuto nel parametro numero suddividendolo
	// in gruppi di tre cifre che, da uno a novecentonovantanove, sono trattati
	// nello stesso modo, salvo poi concatenare l'unità di misura corretta
	// mila, milioni, triliardi
	private static final String decode(String numero)
	{
		StringBuffer parola=new StringBuffer();
		int lungo=numero.length();
		int indice=lungo, indiceDa, indiceA, gruppo;
		indiceDa = lungo-indice;
		while (indice > 0)
		{
			indice = ((indice-1) / 3) * 3;
			indiceA = lungo-indice;
			gruppo = indice/3;
			parola.append(StringUtils.decodeGruppo(gruppo, numero.substring(indiceDa,indiceA)));
			indiceDa = indiceA;
		}
		return parola.toString();
	}

	// Vengono resi disponibili all'esterno solo i seguenti metodi,
	// che contengono il controllo sulla validità del parametro.
	// In caso il numero sia al di fuori dell'intervallo di validità
	// viene restituito null, altrimenti una stringa che rappresenta
	// il numero passato con il parametro.
	public static final String decode(long numeroLong)
	{
		String numero=null;
		boolean negativo=(numeroLong<0);
		if (negativo)
			numeroLong = -numeroLong;
		// estremi dell'intervallo di validità
		if (numeroLong>0 && numeroLong<1000000000000000000l)
			numero=StringUtils.decode(String.valueOf(numeroLong));
		return (negativo?"MENO ":"")+numero;
	}
	public static final String decode(Long numeroLong)
	{
		return StringUtils.decode(numeroLong.longValue());
	}

	// Qui di seguito c'è il necessario per trasformare
	// numeri arabi da 1 a 3999 in numeri romani
	private static int[]    cifre    = { 1000,  900,  500,  400,  100,   90,
		50,   40,   10,    9,    5,    4,    1 };

	private static String[] lettere  = { "M",  "CM",  "D",  "CD", "C",  "XC",
		"L",  "XL",  "X",  "IX", "V",  "IV", "I" };

	// Per ogni valore di i, cifre[i] è l'equivalente int
	// del numerale romano lettere[i].
	// L'elaborazione avviene con un ciclo for che esegue tutti i cicli while
	// uno dopo l'altro
	//
	// Il metodo restituisce la rappresentazione standard romana
	// del numero contenuto nel parametro num
	public static String arabicToRoman(int num)
	{
		// La rappresentazione romana
		String roman = "";

		// Solo da 1 a 3999, altrimenti restituisce stringa vuota
		if (num > 0 && num < 4000)
		{
			// N rappresenta la parte del numero originale che deve essere ancora convertita
			int N = num;

			// Si procede "da sinistra a destra", ossia dalle cifre (o gruppi di cifre)
			// più significative alle meno significative
			for (int i = 0; i < cifre.length; i++)
				while (N >= cifre[i])
				{
					roman += lettere[i];
					N -= cifre[i];
				}
		}

		return roman;
	}

	public static boolean in(String string, String[] strArray) {
		if (strArray==null||strArray.length==0) return false;
		for (int i=0; i<strArray.length; i++)
			if (string==null&&strArray[i]==null||
					string!=null&&string.equals(strArray[i])) return true;
		return false;
	}

	// Metodo per parsificare la stringa del codice AGEA in tre numeri e il punto
	public static String parseCodiceAgea(String codiceAgea) {
		String firstStep = codiceAgea.substring(0,3)+".";
		String secondStep = codiceAgea.substring(3,6)+".";
		String thirdStep = codiceAgea.substring(6,9);
		return firstStep+secondStep+thirdStep;
	}

	// Metodo per ottenere il vettore, senza codici ripetuti, per la legenda del SIAN
	public static Vector<String> getElencoCodiciForLegendaSIAN(Vector<String> elencoCodici) {
		Vector<String> newElencoCodici = new Vector<String>();
		boolean isPresent = false;
		for(int i = 0; i < elencoCodici.size(); i++) {
			String codice = (String)elencoCodici.elementAt(i);
			if(newElencoCodici.size() == 0) {
				newElencoCodici.add(codice);
			}
			else {
				for(int b = 0; b < newElencoCodici.size(); b++) {
					if(!codice.equalsIgnoreCase((String)newElencoCodici.elementAt(b))) {
						isPresent = false;
					}
					else {
						isPresent = true;
						break;
					}
				}
				if(!isPresent) {
					newElencoCodici.add(codice);
				}
			}
		}
		return newElencoCodici;
	}

	/**
	 * Metodo che restituisce la stringa priva di spazi
	 * @param stringa String
	 * @return String
	 */
	public static String deleteSpaces(String stringa) {
		if(stringa == null || stringa.equals("")) {
			return stringa;
		}
		else {
			return stringa.trim();
		}
	}

	/**
	 * Metodo che si occupa di eliminare tutti gli spazi all'interno di una stringa, non solo all'inizio e alla fine come fatto
	 * dalla funzione trim() della classe java.lang.String
	 * @param str String
	 * @return String
	 */
	public static String eliminaSpazi(String str) {
		StringBuffer result = new StringBuffer();
		if(str==null || str.equals("")) {
			return "";
		}
		int size = str.length();
		char old = str.charAt(0);
		boolean spazio = false;
		if(old==' ') {
			spazio = true;
		}
		if(!spazio) {
			result.append(old);
		}
		for (int i = 1; i < size; i++) {
			if (!(spazio && str.charAt(i) == ' ')) {
				result.append(str.charAt(i));
			}
			old = str.charAt(i);
			if (old == ' ') {
				spazio = true;
			}
			else {
				spazio = false;
			}
		}
		return result.toString().trim();
	}

	/**
	 * Metodo che si occupa di parsificare a video il numero di protocollo del documento
	 * @param stringa String
	 * @return String
	 */
	public static String parseNumeroProtocolloField(String stringa) {
		String pattern = "0000000000";
		String numeroProgressivo = stringa.substring(stringa.lastIndexOf(".")+1);
		numeroProgressivo = parseNumericField(numeroProgressivo, pattern);
		return stringa.substring(0, stringa.lastIndexOf(".")+1).concat(numeroProgressivo);
	}
	
	public static String trasfToDBNumeroProtocolloField(String stringa) 
	{
	  String result = "";
	  try
	  {
      String numeroProgressivo = stringa.substring(stringa.lastIndexOf(".")+1);
      Long numLg = new Long(numeroProgressivo);
      result = stringa.substring(0, stringa.lastIndexOf(".")+1).concat(numLg.toString());
	  }
	  catch(Exception ex)
	  {}
	  
    return result;
  }

	public static String getProvinciaByDescrizioneIride2(String provincia) 
	{
		String pattern = "Provincia di ";
		int lunghezzaPattern = pattern.length();
		String provinciaOut = null;
		if(provincia != null)
		{
			int startPattern = provincia.lastIndexOf(pattern);
			if(startPattern!=-1)
			{
				provinciaOut = provincia.substring(lunghezzaPattern);
			}
			else
			{
			  provinciaOut = provincia;
			}
		}
		else{
			provinciaOut = null;
		}
		return provinciaOut;
	}

	/**
	 * Metodo che mi restituisce la legenda dei codici presenti in un elenco
	 * @param elencoCodici
	 * @return
	 */
	public static Vector<CodeDescription> getLegenda(Vector<CodeDescription> elencoCodeDescription) {
		Vector<CodeDescription> newElencoCodici = new Vector<CodeDescription>();
		boolean isPresent = false;
		for(int i = 0; i < elencoCodeDescription.size(); i++) {
			CodeDescription code = (CodeDescription)elencoCodeDescription.elementAt(i);
			if(newElencoCodici.size() == 0) {
				newElencoCodici.add(code);
			}
			else {
				for(int b = 0; b < newElencoCodici.size(); b++) {
					if(code.getCode().intValue() != ((CodeDescription)newElencoCodici.elementAt(b)).getCode().intValue()) {
						isPresent = false;
					}
					else {
						isPresent = true;
						break;
					}
				}
				if(!isPresent) {
					newElencoCodici.add(code);
				}
			}
		}
		return newElencoCodici;
	}

	/**
	 * Metodo utilizzato per inserire e ordinare un elenco secondo la sua chiave logica
	 * @param elenco
	 * @return
	 */
	public static TreeMap<String,String> getAndSortLegenda(Object elenco) 
	{
		TreeMap<String,String> elencoMap = new TreeMap<String,String>();
		Vector<TipoMacroUsoVO> newElenco = new Vector<TipoMacroUsoVO>();
		Vector<TipoCausaleModificaVO> newElencoCasuMod = new Vector<TipoCausaleModificaVO>();
		if(elenco instanceof Vector) 
		{
			boolean isPresent = false;
			for(int i = 0; i < ((Vector<?>)elenco).size(); i++) 
			{
				Object obj = ((Vector<?>)elenco).elementAt(i);
				if(obj instanceof TipoMacroUsoVO) 
				{
					if(newElenco.size() == 0) 
					{
						newElenco.add((TipoMacroUsoVO)obj);
						elencoMap.put(((TipoMacroUsoVO)obj).getCodice(), ((TipoMacroUsoVO)obj).getDescrizione());
					}
					else 
					{
						for(int a = 0; a < newElenco.size(); a++) 
						{
							if(!((TipoMacroUsoVO)obj).getCodice().equalsIgnoreCase(((TipoMacroUsoVO)newElenco.elementAt(a)).getCodice())) 
							{
								isPresent = false;
							}
							else {
								isPresent = true;
								break;
							}
						}
						if(!isPresent) 
						{
							elencoMap.put(((TipoMacroUsoVO)obj).getCodice(), ((TipoMacroUsoVO)obj).getDescrizione());
						}
					}
				}
				if(obj instanceof TipoCausaleModificaVO) 
        {
          if(newElencoCasuMod.size() == 0) 
          {
            newElencoCasuMod.add((TipoCausaleModificaVO)obj);
            elencoMap.put(((TipoCausaleModificaVO)obj).getIdCausaleModifica().toString(), ((TipoCausaleModificaVO)obj).getDescrizione());
          }
          else 
          {
            for(int a = 0; a < newElencoCasuMod.size(); a++) 
            {
              if(((TipoCausaleModificaVO)obj)
                  .getIdCausaleModifica().compareTo(
                      ((TipoCausaleModificaVO)newElencoCasuMod.elementAt(a)).getIdCausaleModifica()) != 0) 
              {
                isPresent = false;
              }
              else {
                isPresent = true;
                break;
              }
            }
            if(!isPresent) 
            {
              elencoMap.put(((TipoCausaleModificaVO)obj).getIdCausaleModifica().toString(), ((TipoCausaleModificaVO)obj).getDescrizione());
            }
          }
        }
			}
		}
		return elencoMap;
	}

	/**
	 * Metodo che controlla se una stringa segue un pattern espresso mediante
	 * una regular expression
	 *
	 * @param   stringa la stringa da controllare
	 * @param   regExp il pattern su cui effettuare il controllo
	 *
	 * @return  true se la stringa segue il pattern, false se non lo segue o se
	 *          la regula expression passata come parametro non è corretta
	 */

	public static boolean matchRegExp(String stringa, String regExp) {
		try{
			org.apache.regexp.RE re = new org.apache.regexp.RE(regExp);
			return re.match(stringa);
		}catch(Exception exc) {
			exc.printStackTrace();
			return false;
		}
	}

	/**
	 * Metodo che si occupa di effettuare automaticamente la conversione dei valori parametrizzati
	 * sul DB
	 *
	 * @param parametriAttAziendaVO
	 * @param tipoAttestazioneVO
	 */
	public static void parsificaDescrizioneAttestazioni(ParametriAttAziendaVO parametriAttAziendaVO, TipoAttestazioneVO tipoAttestazioneVO) {
		try {
			StringBuffer sb = new StringBuffer(tipoAttestazioneVO.getDescrizione());
			while(sb.toString().toUpperCase().lastIndexOf("##") != -1) {
				int start = sb.toString().indexOf("##");
				int end = sb.toString().indexOf(" ", start);
				String nomeParametro = "";
				if(end != -1) {
					nomeParametro = sb.substring(start, end);
				}
				else {
					nomeParametro = sb.substring(start);
				}
				nomeParametro = StringUtils.replace(nomeParametro, "##", "");
				if(parametriAttAziendaVO != null) {
					Object valore = parametriAttAziendaVO.getClass().getMethod("get" +org.apache.commons.lang.StringUtils.capitalize(nomeParametro), (Class[])null).invoke(parametriAttAziendaVO, (Object[])null);
					if(valore != null) {
						if(end != -1) {
							tipoAttestazioneVO.setDescrizione(sb.replace(start, end, (String)valore).toString());
						}
						else {
							tipoAttestazioneVO.setDescrizione(sb.replace(start, sb.length(), (String)valore).toString());
						}
					}
					else {
						if(end != -1) {
							tipoAttestazioneVO.setDescrizione(sb.replace(start, end, "*****").toString());
						}
						else {
							tipoAttestazioneVO.setDescrizione(sb.replace(start, sb.length(), "*****").toString());
						}
					}
				}
				else {
					if(end != -1) {
						tipoAttestazioneVO.setDescrizione(sb.replace(start, end, "*****").toString());
					}
					else {
						tipoAttestazioneVO.setDescrizione(sb.replace(start, sb.length(), "*****").toString());
					}
				}
			}
		}
		catch(NoSuchMethodException nme) {
		}
		catch(IllegalAccessException iae) {
		}
		catch(InvocationTargetException ite) {
		}
	}

	/**
	 * Metodo che si occupa di effettuare automaticamente la conversione dei valori parametrizzati
	 * sul DB alla dichiarazione di consistenza
	 *
	 * @param parametriAttDichiarataVO
	 * @param tipoAttestazioneVO
	 */
	public static void parsificaDescrizioneAttestazioniDichiarate(ParametriAttDichiarataVO parametriAttDichiarataVO, TipoAttestazioneVO tipoAttestazioneVO) {
		try {
			StringBuffer sb = new StringBuffer(tipoAttestazioneVO.getDescrizione());
			while(sb.toString().toUpperCase().lastIndexOf("##") != -1) {
				int start = sb.toString().indexOf("##");
				int end = sb.toString().indexOf(" ", start);
				String nomeParametro = "";
				if(end != -1) {
					nomeParametro = sb.substring(start, end);
				}
				else {
					nomeParametro = sb.substring(start);
				}
				nomeParametro = StringUtils.replace(nomeParametro, "##", "");
				if(parametriAttDichiarataVO != null) {
					Object valore = parametriAttDichiarataVO.getClass().getMethod("get" +org.apache.commons.lang.StringUtils.capitalize(nomeParametro), (Class[])null).invoke(parametriAttDichiarataVO, (Object[])null);
					if(valore != null) {
						if(end != -1) {
							tipoAttestazioneVO.setDescrizione(sb.replace(start, end, (String)valore).toString());
						}
						else {
							tipoAttestazioneVO.setDescrizione(sb.replace(start, sb.length(), (String)valore).toString());
						}
					}
					else {
						if(end != -1) {
							tipoAttestazioneVO.setDescrizione(sb.replace(start, end, "*****").toString());
						}
						else {
							tipoAttestazioneVO.setDescrizione(sb.replace(start, sb.length(), "*****").toString());
						}
					}
				}
				else {
					if(end != -1) {
						tipoAttestazioneVO.setDescrizione(sb.replace(start, end, "*****").toString());
					}
					else {
						tipoAttestazioneVO.setDescrizione(sb.replace(start, sb.length(), "*****").toString());
					}
				}
			}
		}
		catch(NoSuchMethodException nme) {
		}
		catch(IllegalAccessException iae) {
		}
		catch(InvocationTargetException ite) {
		}
	}
	
	/**
	 * Questa funzione elimina qualsiasi carattere non numerico dalla stringa
	 * passata in input e restituisce il numero così ricavato
	 * @param s
	 * @return
	 */
	public static String eliminaCaratteriNumero(String s)
	{
	  /*StringBuffer num=new StringBuffer("");
	  if (s!=null)
	  {
	    for (int i=0;i<s.length();i++)
	    {
	      char c=s.charAt(i);
	      //Se è un carattere numerico lo restituisco.. altrimenti no
	      if (c>='0' && c<='9')
	        num.append(c);
	    }
	  }
	  return num.toString();*/
	  String numero = "";
	  if(s !=null)
	  {
	    numero=s.replaceAll("[\\D]+","");
	  }
	  return numero;
	  
	}
	
	public static String sostituisceCaratteriNonNumeroConSpazio(String s)
  {
    /*StringBuffer num=new StringBuffer("");
    if (s!=null)
    {
      for (int i=0;i<s.length();i++)
      {
        char c=s.charAt(i);
        //Se è un carattere numerico lo restituisco.. altrimenti no
        if (c>='0' && c<='9')
          num.append(c);
      }
    }
    return num.toString();*/
    String numero = "";
    if(s !=null)
    {
      numero=s.replaceAll("[\\D]+"," ");
    }
    return numero;
    
  }
	
}
