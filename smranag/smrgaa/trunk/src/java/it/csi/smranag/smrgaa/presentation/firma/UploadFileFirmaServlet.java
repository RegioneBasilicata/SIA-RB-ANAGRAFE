package it.csi.smranag.smrgaa.presentation.firma;

import it.csi.smranag.smrgaa.dto.allevamenti.AllevamentoVO;
import it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento;
import it.csi.smranag.smrgaa.presentation.excel.ExcelServlet;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

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
public class UploadFileFirmaServlet extends
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
					" - UploadFileFirmaServlet - INIZIO PAGINA ");

			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			HashMap hmRequest = null;

			long sizeInBytes;
			byte[] binaySigned = null;
			String contentType = "";
			if (isMultipart) {
				hmRequest = new HashMap();

				// Create a factory for disk-based file items
				DiskFileItemFactory factory = new DiskFileItemFactory();

				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setFileSizeMax(5 * 1024 * 1024);

				// Parse the request
				List items = upload.parseRequest(request);

				// Process the uploaded items
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					binaySigned = item.get();
					contentType = item.getContentType();
					boolean isInMemory = item.isInMemory();
					sizeInBytes = item.getSize();

				}
			}
			String nomeFile = (String) request.getSession().getAttribute(
					"fileName");
			
			if ("application/pkcs7-mime".equalsIgnoreCase(contentType))
				nomeFile += nomeFile + ".p7m";
			else 
				contentType = "application/pdf";
			
			//binaySigned = FileUtils.readFileToByteArray(new File("D:/Firmami.pdf"));
			
			response.setContentLength(binaySigned.length);
			response.setContentType(contentType);
			response.addHeader("Content-Disposition", "attachment;filename ="
					+ nomeFile);

			OutputStream out = response.getOutputStream();
			out.write(binaySigned);
			out.flush();
			out.close();
			

			SolmrLogger.debug(this, " - UploadFileFirmaServlet - FINE PAGINA ");
		} catch (Exception ex) {
			request.setAttribute("errorReport", ex);
			SolmrLogger.fatal(this,
					"DownloadFileFirmaServlet Exception  " + ex.toString());
			throw new ServletException(ex.getMessage());
		}
	}

}
