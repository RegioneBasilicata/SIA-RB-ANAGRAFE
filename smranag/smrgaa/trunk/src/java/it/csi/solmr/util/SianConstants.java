package it.csi.solmr.util;

/**
 * <p>
 * Title: Sian
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author
 * @version 1.0
 */

public abstract class SianConstants extends AbstractSianEtc {
	protected static final Class<?> THIS_CLASS = SianConstants.class;

	public static int SIAN_TIME_OUT;

	// Inzio variabili asscoiate al servizio vecchio

	public static String SIAN_HEADER;
	public static String SIAN_HEADER_SHR;

	// Start SianAnagrafeDettaglio
	// public static String SIAN_USERNAME_ANAGRAFICA_DETTAGLIO;
	// public static String SIAN_PASSWORD_ANAGRAFICA_DETTAGLIO;
	// public static String SIAN_URL_ANAGRAFICA_DETTAGLIO;
	// End SianAnagrafeDettaglio

	// Fine variabili associate al servizio vecchio

	// Start ricercaAnagrafica
	public static String SIAN_URL_RICERCA_ANAGRAFICA;
	// End ricercaAnagrafica

	// Start posta certificata
	public static String SMRCOMM_URL_POSTA_CERTIFICATA;
	// End posta certificata

	// Start posta certificata
	public static String PAPUA_URL_WS;
	// End posta certificata
	
	
	// Url servizi Infocamere
	public static String INFOCAMERE_URL_WS;

	public static String WS_NAME; // WSBridgeConstants.WS_AGEA_AUT_NAME;
	public static String WS_USERNAME; // WSBridgeConstants.WS_AGEA_AUT_USERNAME;
	public static String WS_PASSWORD; // WSBridgeConstants.WS_AGEA_AUT_PASSWORD;
	public static String WS_NAMESPACE; // WSBridgeConstants.WS_AGEA_AUT_NAMESPACE;
	public static String WS_WSDLURI; // WSBridgeConstants.WS_AGEA_AUT_WSDLURI;
	public static String WS_URI; // WSBridgeConstants.WS_AGEA_AUT_URI;
	public static final boolean PROXY_ACTIVE = true;
	public static String PROXY_SERVER;
	public static int PROXY_PORT;

	public static void dump() {
		// String THIS_METHOD = THIS_CLASS+"::dump() - ";
	}

	public static void initConstants() {
		Object objConstantManager = XMLManager.getInstance();
		initClassConstants(THIS_CLASS, objConstantManager);
	}

	static {
		initConstants();
	}

}
