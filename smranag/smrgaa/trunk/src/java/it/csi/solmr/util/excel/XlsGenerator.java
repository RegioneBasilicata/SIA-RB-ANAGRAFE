package it.csi.solmr.util.excel;

import java.io.*;
import java.util.*;

import it.csi.solmr.dto.anag.*;
import it.csi.solmr.exception.*;
import it.csi.solmr.util.*;
import jxl.*;
import jxl.write.*;
import org.jdom.*;

/**
 * <p>Title: SMRGAA </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: CSI Piemonte</p>
 * @author Mauro Vocale
 * @version 1.0
 */

public class XlsGenerator {

	public XlsGenerator() {}

	public ByteArrayOutputStream parseCollectionToXls(Collection<Object> result, String foglioExcel) throws SolmrException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(baos);
			WritableSheet sheet = workbook.createSheet("Foglio1", 0);
			XMLToBean xml = new XMLToBean(XMLToBean.class.getResourceAsStream("/it/csi/solmr/etc/excel/mapTable.xml"));
			Label[] a_label = xml.getLabels(foglioExcel, 0, sheet);
			Object[] a_labels = xml.setVOValues(foglioExcel, result, null, 0);
			List<Element> columnList = xml.getColumnList(foglioExcel);

			// Intestazioni
			for(int i = 0; i < a_label.length; i++) {
				Label label = a_label[i];
				sheet.addCell(label);
				// Setta la larghezza delle colonne in relazione al tipo foglio excel che deve creare
				Element e = (Element)columnList.get(i);
				String widthLabel = xml.getWidthLabel(e);
				sheet.setColumnView(i, Integer.parseInt(widthLabel));
			}
			// Aggiungo i valori
			for(int i = 0; i < a_labels.length; i++) {
				Object obj = a_labels[i];
		    	if(obj instanceof Label) {
		    		//Label label = a_labels[i];
		    		sheet.addCell((Label)obj);
		    	}
		    	else {
		    		sheet.addCell((jxl.write.Number)obj);
		    	}
			}
			workbook.write();
			workbook.close();
		}
		catch(IOException io) {
			SolmrLogger.error("[XlsGenerator::parseArrayListToXls]", io);
			throw new SolmrException();
		}
		catch(WriteException we) {
			SolmrLogger.error("[XlsGenerator::parseArrayListToXls]", we);
			throw new SolmrException();
		}
		catch(Exception e) {
			SolmrLogger.error("[XlsGenerator::parseArrayListToXls]", e);
			throw new SolmrException();
		}
		return baos;
	}

	public ByteArrayOutputStream parseElementsToXls(Collection<Object> result, String foglioExcel, String nomeBrogliaccio) throws SolmrException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try 
  	{
  		WritableWorkbook workbook = Workbook.createWorkbook(baos);
  		WritableSheet sheet = workbook.createSheet("Foglio1", 0);
  		XMLToBean xml = new XMLToBean(XMLToBean.class.getResourceAsStream("/it/csi/solmr/etc/excel/mapTable.xml"));
  
  		// Foglio excel relativo ai documenti
  		int size = 1;
  		if(foglioExcel.equalsIgnoreCase("elencoDocumenti")) 
  		{
  			Iterator<Object> itera = result.iterator();
  			DocumentoExcelVO documentoVO = (DocumentoExcelVO)itera.next();
  			DocumentoFiltroExcelVO documentoFiltroExcelVO = documentoVO.getDocumentoFiltroExcelVO();
  			if(Validator.isNotEmpty(documentoFiltroExcelVO.getDataScadenza())) {
  				size++;
  			}
  			if(Validator.isNotEmpty(documentoFiltroExcelVO.getDescStatoDocumento())) {
  				size++;
  			}
  			if(Validator.isNotEmpty(documentoFiltroExcelVO.getDescTipoTipologiaDocumento())) {
  				size++;
  			}
  			if(Validator.isNotEmpty(documentoFiltroExcelVO.getDescTipoCategoriaDocumento())) {
  				size++;
  			}
  			if(Validator.isNotEmpty(documentoFiltroExcelVO.getDescTipoDocumento())) {
  				size++;
  			}
  			if(Validator.isNotEmpty(documentoFiltroExcelVO.getProtocollazione())) {
  				size++;
  			}

  			// Documento
  			WritableFont title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
  			WritableCellFormat titleFormat = new WritableCellFormat(title);
  			titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
  			titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
  			Label label = new Label(0, size + 3, "Documento", titleFormat);
  			sheet.addCell(label);
  			sheet.mergeCells(0, size + 3 ,2, size + 3);
  
  			// Validità documento
  			WritableFont title2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
  			WritableCellFormat titleFormat2 = new WritableCellFormat(title2);
  			titleFormat2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
  			titleFormat2.setAlignment(jxl.format.Alignment.CENTRE);
  			Label label2 = new Label(3, size + 3, "Validità documento", titleFormat2);
  			sheet.addCell(label2);
  			sheet.mergeCells(3 , size + 3 , 4, size + 3);
  
  			// Protocollo
  			WritableFont title3 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
  			WritableCellFormat titleFormat3 = new WritableCellFormat(title3);
  			titleFormat3.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
  			titleFormat3.setAlignment(jxl.format.Alignment.CENTRE);
  			Label label3 = new Label(5, size + 3, "Repertorio", titleFormat3);
  			sheet.addCell(label3);
  			sheet.mergeCells(5 , size + 3 , 6, size + 3);

  			// Stato
        WritableFont title4 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        WritableCellFormat titleFormat4 = new WritableCellFormat(title4);
        titleFormat4.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleFormat4.setAlignment(jxl.format.Alignment.CENTRE);
        titleFormat4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        Label label4 = new Label(7, size + 3, "Stato", titleFormat4);
        sheet.addCell(label4);
        sheet.mergeCells(7 , size + 3 , 7, size + 4);
  		}
  		if(foglioExcel.equalsIgnoreCase("elencoSianTerritorio")) 
  		{
  			size++;
        // Comune
        WritableFont title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        WritableCellFormat titleFormat = new WritableCellFormat(title);
        titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
        titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        Label label = new Label(0, size, "Comune", titleFormat);
        sheet.addCell(label);
        sheet.mergeCells(0 , size, 0, size + 1);
  
        //Sz.
        title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        titleFormat = new WritableCellFormat(title);
        titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
        titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        label = new Label(1, size, "Sz.", titleFormat);
        sheet.addCell(label);
        sheet.mergeCells(1 , size, 1, size + 1);
  
        //Fgl.
        title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        titleFormat = new WritableCellFormat(title);
        titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
        titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        label = new Label(2, size, "Fgl.", titleFormat);
        sheet.addCell(label);
        sheet.mergeCells(2 , size, 2, size + 1);
  
        //Part.
        title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        titleFormat = new WritableCellFormat(title);
        titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
        titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        label = new Label(3, size, "Part.", titleFormat);
        sheet.addCell(label);
        sheet.mergeCells(3 , size , 3, size + 1);
  
        //Sub.
        title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        titleFormat = new WritableCellFormat(title);
        titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
        titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        label = new Label(4, size, "Sub", titleFormat);
        sheet.addCell(label);
        sheet.mergeCells(4 , size , 4, size + 1);
  
  
        // Conduzione
        title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        titleFormat = new WritableCellFormat(title);
        titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
        label = new Label(5, size, "Conduzione", titleFormat);
        sheet.addCell(label);
        sheet.mergeCells(5, size,5, size+1);
  
        // Uso del suolo
        title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        titleFormat = new WritableCellFormat(title);
        titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
        label = new Label(6, size, "Uso del suolo", titleFormat);
        sheet.addCell(label);
        sheet.mergeCells(6, size,8, size);
  
        // Varietà
        title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        titleFormat = new WritableCellFormat(title);
        titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
        label = new Label(7, size+1, "Varietà", titleFormat);
        sheet.addCell(label);
        sheet.mergeCells(7, size+1,7, size+1);
  
        // Sup.
        title = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        titleFormat = new WritableCellFormat(title);
        titleFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);
        label = new Label(8, size+1, "Sup.", titleFormat);
        sheet.addCell(label);
        sheet.mergeCells(8, size+1,8, size+1);
  		}
  			
      Label[] a_label = xml.getLabels(foglioExcel, size, sheet);
      Object[] a_labels = xml.setVOValues(foglioExcel, result, nomeBrogliaccio, size);
      List<Element> columnList = xml.getColumnList(foglioExcel);
      List<String> columnWidth = xml.getWidthList(columnList);
      List<String> widthLabelFiltri = xml.getWidthLabelFiltri(foglioExcel);
  
      // Intestazioni
      for(int i = 0; i < a_label.length; i++) 
      {
      	Label label = a_label[i];
      	// Se sto creando il file excel relativo all'elenco dei documenti non inserisco l'ultima intestazione, quella relativa
      	// allo stato, in quanto è già stata creata e gestita sopra
      	if(foglioExcel.equalsIgnoreCase("elencoDocumenti")) {
      		if(i < (a_label.length - 1)) {
      			sheet.addCell(label);
      		}
      	}
      	else {
      		if(!foglioExcel.equalsIgnoreCase("elencoSianTerritorio")) {
      			sheet.addCell(label);
      		}
      	}
      	// Setta la larghezza delle colonne in relazione al tipo foglio excel che deve creare
      	if(foglioExcel.equalsIgnoreCase("elencoTitoli") && i < columnWidth.size()) {
      		sheet.setColumnView(i, Integer.parseInt((String)columnWidth.get(i)));
      	}
      	else 
      	{
      		if(!foglioExcel.equalsIgnoreCase("elencoTitoli")) 
      		{
  		    	Element e = (Element)columnList.get(i);
  			    String widthLabel = xml.getWidthLabel(e);
  			    if(Validator.isNotEmpty(widthLabel)) {
  			    	sheet.setColumnView(i, Integer.parseInt(widthLabel));
  			    }
      		}
      	}
      }
      // Aggiungo i valori
      for(int i = 0; i < a_labels.length; i++) 
      {
      	Object obj = a_labels[i];
      	if(obj instanceof Label) {
      		sheet.addCell((Label)obj);
      	}
      	else if(obj instanceof jxl.write.Number) {
      		sheet.addCell((jxl.write.Number)obj);
      	}
      	else if(obj instanceof jxl.write.DateTime) {
      		sheet.addCell((jxl.write.DateTime)obj);
      	}
      	if(foglioExcel.equalsIgnoreCase("elencoDocumenti")) {
      		if (i < widthLabelFiltri.size()) {
      			sheet.setColumnView(i,Integer.parseInt((String)widthLabelFiltri.get(i)));
      		}
      	}
      }
      workbook.write();
      workbook.close();
  	}
		catch(IOException io) {
  		SolmrLogger.error("[XlsGenerator::parseArrayListToXls]", io);
  		throw new SolmrException();
  	}
  	catch(WriteException we) {
  		SolmrLogger.error("[XlsGenerator::parseArrayListToXls]", we);
  		throw new SolmrException();
  	}
  	catch(Exception e) {
  		SolmrLogger.error("[XlsGenerator::parseArrayListToXls]", e);
  		throw new SolmrException();
  	}
		return baos;
	}
}
