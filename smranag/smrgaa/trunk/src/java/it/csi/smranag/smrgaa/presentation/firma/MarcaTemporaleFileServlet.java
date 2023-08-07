package it.csi.smranag.smrgaa.presentation.firma;

import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.util.SolmrLogger;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

/**
 * Classe generica per la generazione dei file excel
 * <p>
 * Title: Smrgaa
 * </p>
 * <p>
 * Description: Classe di utilità per la generazione di file excel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 1.0
 */
public class MarcaTemporaleFileServlet extends
		it.csi.smranag.smrgaa.presentation.excel.ExcelServlet {

	/**
   * 
   */
	private static final long serialVersionUID = -5892158174529073863L;

	/**
	 * Inizializza il servlet
	 *
	 * @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	public void destroy() {
		super.destroy();
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			SolmrLogger.debug(this,
					" - MarcaTemporaleFileServlet - INIZIO PAGINA ");

			AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
					.getAttribute(("anagAziendaVO"));
			
			@SuppressWarnings("unchecked")
			GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
			
			String nomeFile = (String)request.getSession().getAttribute("fileName2");
			byte[] fileToMark = (byte[]) request.getSession().getAttribute(
					"fileAllegatoB");
			byte[] marcaTermporale = gaaFacadeClient
					.getMarcaTemporale(fileToMark);

			if (marcaTermporale != null) {
				response.resetBuffer();
				response.setContentLength(marcaTermporale.length);
				response.setContentType("application/x-download");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + "marcaTemporale"+ nomeFile.substring(0, nomeFile.lastIndexOf(".")) +".tsr" + "\"");

				OutputStream out = response.getOutputStream();
				out.write(marcaTermporale);
				out.flush();
				out.close();
			} else {
//				String newURL = response.encodeRedirectURL("../layout/strumentiFirmaMarca.htm");
//				response.sendRedirect(newURL);
				String msgError = "Impossibile ottenere la marca temporale del file.";
				request.setAttribute("errorReport", msgError);
				SolmrLogger.fatal(this, "MarcaTemporaleFileServlet Exception  "
						+ msgError);
				throw new ServletException(msgError);
			}

			SolmrLogger.debug(this,
					" - MarcaTemporaleFileServlet - FINE PAGINA ");
		} catch (Exception ex) {
			request.setAttribute("errorReport", ex);
			SolmrLogger.fatal(this, "MarcaTemporaleFileServlet Exception  "
					+ ex.toString());
			throw new ServletException(ex.getMessage());
		}
	}

}
