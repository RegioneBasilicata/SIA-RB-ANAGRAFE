package it.csi.solmr.servlet.anag;

import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;
import it.csi.solmr.util.excel.XlsGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExcelBuilderServlet extends HttpServlet {

	private static final long serialVersionUID = -8877458268609844844L;

	public ExcelBuilderServlet() {
		try {
			jbInit();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	//Initialize global variables
	public void init() throws ServletException {
		super.init();
	}

	//Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	//Process the HTTP Post request
	@SuppressWarnings("unchecked")
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SolmrLogger.debug(this, "Invocating method doPost in ExcelBuilderServlet\n");
		Vector<Object> elenco = null;
		String foglioExcel = null;
		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getAttribute("anagAziendaVO");
		String fileName = (String)request.getAttribute("fileName");
		try {
			// Dalla request recupera i dati per l'export
			elenco = (Vector)request.getAttribute("elenco");
			foglioExcel = (String)request.getAttribute("foglioExcel");

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		XlsGenerator xls = new XlsGenerator();

		response.setContentType("application/x-download");
		if(anagAziendaVO == null) {
			if(!Validator.isNotEmpty(fileName))
				response.addHeader("Content-Disposition","attachment;filename = report.xls");
			else
				response.addHeader("Content-Disposition","attachment;filename = " + fileName + ".xls");
		}
		else {
			if(Validator.isNotEmpty(fileName)) {
				response.addHeader("Content-Disposition","attachment;filename = "+anagAziendaVO.getCUAA()+"_"+fileName+".xls");
			}
			else {
				response.addHeader("Content-Disposition","attachment;filename = "+anagAziendaVO.getCUAA()+".xls");
			}
		}
		try {
			OutputStream os = response.getOutputStream();
			// Scarica in excel dal menù terreni
			if(foglioExcel.equalsIgnoreCase("elencoParticelle")) {
				String descrizionePiano = (String)request.getAttribute("descrizionePiano");
				os.write(xls.parseElementsToXls(elenco, foglioExcel, "STAMPA PIANO COLTURALE AGGIORNATO AL "+descrizionePiano.toUpperCase()).toByteArray());
			}
			// Scarica in excel dall'elenco dei documenti
			else if(foglioExcel.equalsIgnoreCase("elencoDocumenti")) {
				SolmrLogger.debug(this, "Invocating method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
				os.write(xls.parseElementsToXls(elenco, foglioExcel, "ELENCO DOCUMENTI").toByteArray());
				SolmrLogger.debug(this, "Invocated method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
			}
			// Scarica in excel dall'elenco dei titoli
			else if(foglioExcel.equalsIgnoreCase("elencoTitoli")) {
				SolmrLogger.debug(this, "Invocating method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
				os.write(xls.parseElementsToXls(elenco, foglioExcel, "STAMPA TITOLI RIFORMA PAC SINGOLI AGGIORNATI AL").toByteArray());
				SolmrLogger.debug(this, "Invocated method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
			}
			// Scarica in excel dall'elenco delle unità arboree
			else if(foglioExcel.equalsIgnoreCase("elencoUnitaArboree")) {
				String descrizionePiano = (String)request.getAttribute("descrizionePiano");
				SolmrLogger.debug(this, "Invocating method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
				os.write(xls.parseElementsToXls(elenco, foglioExcel, "STAMPA ELENCO UNITA' ARBOREE AGGIORNATO AL "+descrizionePiano).toByteArray());
				SolmrLogger.debug(this, "Invocated method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
			}
			else if(foglioExcel.equalsIgnoreCase("elencoSianTerritorio")) {
				SolmrLogger.debug(this, "Invocating method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
				os.write(xls.parseElementsToXls(elenco, foglioExcel, "Piano Colturale SIAN").toByteArray());
				SolmrLogger.debug(this, "Invocated method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
			}
			// Gestione scarico report "Riepilogo mandati e validazioni (Intermediari e non)"
			else if(foglioExcel.equalsIgnoreCase("elencoMandatiValidazioni")) {
				SolmrLogger.debug(this, "Invocating method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
				os.write(xls.parseElementsToXls(elenco, foglioExcel, "ELENCO MANDATI E VALIDAZIONI").toByteArray());
				SolmrLogger.debug(this, "Invocated method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
			}
			else if(foglioExcel.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediario")) {
				SolmrLogger.debug(this, "Invocating method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
				os.write(xls.parseElementsToXls(elenco, foglioExcel, "ELENCO MANDATI E VALIDAZIONI").toByteArray());
				SolmrLogger.debug(this, "Invocated method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
			}
			else if(foglioExcel.equalsIgnoreCase("elencoMandatiValidazioniNotIntermediarioForProvincia")) {
				SolmrLogger.debug(this, "Invocating method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
				os.write(xls.parseElementsToXls(elenco, foglioExcel, "ELENCO MANDATI E VALIDAZIONI").toByteArray());
				SolmrLogger.debug(this, "Invocated method parseElementsToXls in method doPost in ExcelBuilderServlet\n");
			}
			//  -----------------
			else {
				SolmrLogger.debug(this, "Invocating method parseCollectionToXls in method doPost in ExcelBuilderServlet\n");
				os.write(xls.parseCollectionToXls(elenco, foglioExcel).toByteArray());
				SolmrLogger.debug(this, "Invocated method parseCollectionToXls in method doPost in ExcelBuilderServlet\n");
			}
			os.flush();
			os.close();
		}
		catch (SolmrException ex) {
			SolmrLogger.error("[Export::doPost] catturata eccezione su creazione file di report", ex);
			throw new ServletException(ex.getMessage());
		}
		SolmrLogger.debug(this, "Invocated method doPost in ExcelBuilderServlet\n");
	}

	//Clean up resources
	public void destroy() {
		super.destroy();
	}

	/**
	 * ritorna la Stringa il cui valore corrisponde ...
	 *
	 * @param aCollection Collection
	 * @return String
	 * @throws SolmrException
	 */
	String checkType(Collection<?> aCollection) throws SolmrException {
		if (aCollection == null || aCollection.isEmpty()) {
			throw new SolmrException("Collezione di dati Vuota!");
		}

		String ret_str = null;
		Object obj = aCollection.toArray()[0];

		// effettuo il controllo con questo metodo
		if(obj instanceof Object) {
			ret_str = "";
		}
		else if (obj instanceof Object) {
			ret_str = "";
		}
		return ret_str;
	}

	private void jbInit() throws Exception {
	}
}
