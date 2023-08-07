package it.csi.solmr.util;

/**
 * <p>Title: Sian</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TOBECONFIG</p>
 * @author
 * @version 1.0
 */

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;


public abstract class AbstractSianEtc {
	protected static final Class<?> THIS_CLASS = AbstractSianEtc.class;
	public    static HashMap<Object,Object> OTHERS = new HashMap<Object,Object>();

	public final static String DATA     = "dd/MM/yyyy";

	/**
	 * Carica le costanti stringhe dal file di proprietà
	 *
	 * @param theClass
	 * @param resourceName
	 */
	protected static synchronized void initClassConstants(Class<?> theClass, Object objConstantManager) {
		Hashtable<Object,Object> htConstants = XMLManager.getHashtableConstants();
		int num = 0;
		Enumeration<Object> eNum = htConstants.keys();
		while(eNum.hasMoreElements()) {
			num++;
			String currentKey = (String)eNum.nextElement();
			try {
				Field currentField = theClass.getField(currentKey);
				setStaticFieldData(currentField,(String) htConstants.get(currentKey));
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void setStaticFieldData(Field field, String data) {
		if(data == null || field == null) {
			return;
		}
		try {
			String type = field.getType().getName();
			if("java.lang.String".equals(type)) {
				field.set(null,data);
			}
			else {
				if("java.lang.Long".equals(type) || "long".equals(type)) {
					field.set(null,new Long(AbstractSianEtc.parseLong(data.trim())));
				}
				else {
					if("java.lang.Integer".equals(type) || "int".equals(type)) {
						field.set(null,new Integer((int)AbstractSianEtc.parseLong(data.trim())));
					}
					else {
						if("java.lang.Double".equals(type) || "double".equals(type)) {
							field.set(null,new Double(data.trim()));
						}
						else {
							if("java.lang.Float".equals(type) || "float".equals(type)) {
								field.set(null,new Float(data.trim()));
							}
							else {
								if("java.util.Date".equals(type)) {
									field.set(null,AbstractSianEtc.parseDate(data));
								}
								else {
									if("java.sql.Date".equals(type)) {
										field.set(null,new java.sql.Date(AbstractSianEtc.parseDate(data).getTime()));
									}
									else {
										if("java.lang.Boolean".equals(type) || "boolean".equals(type)) {
											field.set(null,new Boolean(data.trim()));
										}
										else {
											if("java.lang.Character".equals(type) || "char".equals(type)) {
												field.set(null,new Character(data.charAt(0)));
											}
											else {
												if("java.lang.Byte".equals(type) || "byte".equals(type)) {
													field.set(null,new Byte(data.trim()));
												}
												else {
													if("java.lang.Short".equals(type) || "short".equals(type)) {
														field.set(null,new Short((short)AbstractSianEtc.parseLong(data.trim())));
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Esegue la conversione di una stringa in un numero, accetta anche i numeri
	 * in formato esadecimale
	 * @param number
	 * @return
	 */
	public static long parseLong(String number) {
		long valore = 0;
		if(number != null) {
			// Numero esadecimale 
			if(number.startsWith("0x")) {
				valore = Long.parseLong(number.substring(2),16);
			}
			else {
				valore = new Long(number).longValue();
			}
		}
		return valore;
	}

	/**
	 *
	 * @param date la stringa con la data da trasformare in un oggetto data
	 * @return l'oggetto data parsificato secondo il contenuto della costante DATA
	 * @throws Exception se la parsificazione fallisce
	 */
	public static Date parseDate(String date) throws Exception {
		Date parsedDate = parse(date, DATA);
		return parsedDate;
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
		Date parsedDate = sdf.parse(toParse);
		return parsedDate;
	}
}
