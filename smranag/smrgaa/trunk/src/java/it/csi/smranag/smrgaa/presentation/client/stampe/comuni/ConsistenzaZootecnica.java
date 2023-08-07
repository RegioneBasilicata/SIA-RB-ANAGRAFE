package it.csi.smranag.smrgaa.presentation.client.stampe.comuni;

import inetsoft.report.ReportElement;
import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.TabularSheet;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAllevamento;
import it.csi.smranag.smrgaa.dto.allevamenti.StabulazioneTrattamento;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.AllevamentoAnagVO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.NumberUtils;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class ConsistenzaZootecnica extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comuni/ConsistenzaZootecnica.srt";
  private final static String CODICE_SUB_REPORT = "CONSISTENZA_ZOOTECNICA";

  public ConsistenzaZootecnica() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);
    
    TabularSheet layout = (TabularSheet) subReport;
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)parametri.get("anagAziendaVO");
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    if(dataInserimentoDichiarazione == null)
      dataInserimentoDichiarazione = new Date();
    
    Date dataDettaglioAllevamenti = (Date)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DETT_ALLEVAMENTI);
    
    
    
    
    Vector<Long> idall = anagFacadeClient.getAllevamentiQuadroG(anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione);
    int size = 0;
    if (idall != null)
      size = idall.size();

    if (size > 0)
    {
      // Rimuovo il testo indicante l'assenza di consistenze zootecniche
      // associate all'azienda
      subReport.removeElement("txtNessunaConsistenza");

      ReportElement element = null;
      AllevamentoAnagVO all = null;
      int elStartRow, elStartCol;
      Point elStartCell;
      String elID = null, elIDCurrent = null;
      DefaultTableLens tblTemp = null;

      // Recupero le coordinate della cella in cui mi trovo
      elStartCell = layout.getElementCell(subReport
          .getElement("tblConsZootecnica1"));
      elStartRow = (int) Math.round(elStartCell.getY());
      elStartCol = (int) Math.round(elStartCell.getX());


      for (int k = 0; k < size; k++)
      {

        elID = "tblConsZootecnica1";
        // Se siamo al primo ciclo, non clono nulla
        // altrimenti clono tblConsZootecnicaRiga1
        if (k == 0)
        {
          elIDCurrent = elID;
        }
        else
        {
          elIDCurrent = elID + k;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
        }
        // Popolo tblQuadroDTitolo (o il suo i-esimo clone)
        tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));

        //mi ricerco l'allevamento
        all = anagFacadeClient.getAllevamento((Long) idall.get(k));
        

        tblTemp.setObject(0, 0, "Cod.az.zoo:");
        tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblTemp.setObject(0, 1, StampeGaaServlet.checkNull(all.getCodiceAziendaZootecnica()));
        
        
        //Ricavo l'ute
        UteVO ute = anagFacadeClient.getUteById(all.getIdUTELong());
        //Mi ricavo i dati del comune dell'ute
        ComuneVO comune = anagFacadeClient.getComuneByISTAT(ute.getIstat());

        tblTemp.setObject(0, 2, StampeGaaServlet.checkNull("Specie:"));
        tblTemp.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblTemp.setObject(0, 3, StampeGaaServlet.checkNull(all.getTipoSpecieAnimaleAnagVO().getDescrizione()));
        
        
        
        tblTemp.setObject(0, 4, StampeGaaServlet.checkNull("UTE:"));
        tblTemp.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
        //comune = anagFacadeClient.getComuneByISTAT(all.getIstatComuneAllevamento());
        if (comune != null)
          tblTemp.setObject(0, 5,  StampeGaaServlet.checkNull(ute.getIndirizzo()) + " - "+ 
              StampeGaaServlet.checkNull(comune.getCap()) + " "+ StampeGaaServlet.checkNull(comune.getDescom())
              + " (" + StampeGaaServlet.checkNull(comune.getSiglaProv()) + ")");
        else
          tblTemp.setObject(0, 5, "");
        

        tblTemp.setColWidth(0, 50);
        tblTemp.setColWidth(1, 45);
        tblTemp.setColWidth(2, 30);
        tblTemp.setColWidth(3, 126);
        tblTemp.setColWidth(4, 24);
        tblTemp.setColWidth(5, 295);
        StampeGaaServlet.setNoBorder(tblTemp);
        subReport.setElement(elIDCurrent, tblTemp);

        /**
         * Creo la seconda riga della report (Cod azienda)
         */
        elIDCurrent = elID + "2" + k;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));

        tblTemp.setObject(0, 0, StampeGaaServlet.checkNull("Ubicaz. Allevam.:"));
        tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
        comune = anagFacadeClient.getComuneByISTAT(all.getIstatComuneAllevamento());
        if (comune != null)
          tblTemp.setObject(0, 1,  StampeGaaServlet.checkNull(all.getIndirizzo()) + " - "+ 
              StampeGaaServlet.checkNull(all.getCap()) + " "+StampeGaaServlet.checkNull(comune.getDescom()
              + " (" + StampeGaaServlet.checkNull(comune.getSiglaProv()) + ")"));
        else
          tblTemp.setObject(0, 1, "");
        
        
        
        
        
        if(Validator.isNotEmpty(dataInserimentoDichiarazione)
          && dataInserimentoDichiarazione.before(dataDettaglioAllevamenti))
        {
          tblTemp.setObject(0, 2, StampeGaaServlet.checkNull("Vol. lettiera perm.(m3):"));
          tblTemp.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
          try
          {
            BigDecimal b1=new BigDecimal(all.getSuperficieLettieraPermanente());
            BigDecimal b2=new BigDecimal(all.getAltezzaLettieraPermanente());
            b1=b1.multiply(b2);
            String volume=StringUtils.parseDoubleFieldTwoDecimal(b1.toString());
            tblTemp.setObject(0, 3, StampeGaaServlet.checkNull(volume));
          }
          catch(Exception e) 
          {
            tblTemp.setObject(0, 3, StampeGaaServlet.checkNull(""));
          }
        }
        else
        {
          tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(""));
          tblTemp.setObject(0, 3, StampeGaaServlet.checkNull(""));
        }
        
        
        
        
        
        tblTemp.setColWidth(0, 78);
        tblTemp.setColWidth(1, 339);
        tblTemp.setColWidth(2, 108);
        tblTemp.setColWidth(3, 45);
        tblTemp.setColWidth(4, 0);
        tblTemp.setColWidth(5, 0);
        StampeGaaServlet.setNoBorder(tblTemp);
        subReport.setElement(elIDCurrent, tblTemp);
        
        
        if(Validator.isNotEmpty(dataInserimentoDichiarazione)
            && dataInserimentoDichiarazione.before(dataDettaglioAllevamenti))
        {
          /**
           * Creo la terza riga della report (N. medio capi in lattaz.)
           */
          elIDCurrent = elID + "4" + k;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
          tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));
  
          tblTemp.setObject(0, 0, StampeGaaServlet.checkNull("N. medio capi in lattaz.:"));
          tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblTemp.setObject(0, 1, StampeGaaServlet.checkNull(all.getMediaCapiLattazione()));
          tblTemp.setObject(0, 2, StampeGaaServlet.checkNull("Tipol. strutt. Mung.:"));
          tblTemp.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblTemp.setObject(0, 3, StampeGaaServlet.checkNull(all.getDescMungitura()));
          tblTemp.setObject(0, 4, StampeGaaServlet.checkNull("Vol. acque lavaggio(m3):"));
          tblTemp.setFont(0, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
          
          if (Validator.isNotEmpty(all.getQuantitaAcquaLavaggio()))
            all.setQuantitaAcquaLavaggio(StringUtils.parsePesoCapi(all.getQuantitaAcquaLavaggio()));  
          
          tblTemp.setObject(0, 5, StampeGaaServlet.checkNull(all.getQuantitaAcquaLavaggio()));
          tblTemp.setColWidth(0, 115);
          tblTemp.setColWidth(1, 43);
          tblTemp.setColWidth(2, 97);
          tblTemp.setColWidth(3, 158);
          tblTemp.setColWidth(4, 114);
          tblTemp.setColWidth(5, 43);        
          StampeGaaServlet.setNoBorder(tblTemp);
          layout.setElement(elIDCurrent, tblTemp);
        }
        else
        {
          elIDCurrent = elID + "4" + k;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
          tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));
  
          tblTemp.setObject(0, 0, StampeGaaServlet.checkNull("Tipo produzione:"));
          tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblTemp.setObject(0, 1, StampeGaaServlet.checkNull(all.getDescTipoProduzione()));
          tblTemp.setObject(0, 2, StampeGaaServlet.checkNull("Orientamento produttivo:"));
          tblTemp.setFont(0, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
          tblTemp.setObject(0, 3, StampeGaaServlet.checkNull(all.getDescOrientamentoProduttivo()));
          tblTemp.setObject(0, 4, StampeGaaServlet.checkNull(""));
          tblTemp.setObject(0, 5, StampeGaaServlet.checkNull(""));
          tblTemp.setColWidth(0, 78);
          tblTemp.setColWidth(1, 150);
          tblTemp.setColWidth(2, 113);
          tblTemp.setColWidth(3, 200);
          tblTemp.setColWidth(4, 1);
          tblTemp.setColWidth(5, 1);        
          StampeGaaServlet.setNoBorder(tblTemp);
          layout.setElement(elIDCurrent, tblTemp);
          
          
          elIDCurrent = elID + "6" + k;
          element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
          layout.addElement(elStartRow, elStartCol, element);
          tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));
          tblTemp.setObject(0, 0, StampeGaaServlet.checkNull("Tipo produzione ai fini assicurativi:"));
          tblTemp.setFont(0, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);          
          tblTemp.setObject(0, 1, StampeGaaServlet.checkNull(all.getDescTipoProduzioneCosman()));
          tblTemp.setObject(0, 2, StampeGaaServlet.checkNull(""));
          tblTemp.setObject(0, 3, StampeGaaServlet.checkNull(""));
          tblTemp.setObject(0, 4, StampeGaaServlet.checkNull(""));
          tblTemp.setObject(0, 5, StampeGaaServlet.checkNull(""));
          tblTemp.setColWidth(0, 170);
          tblTemp.setColWidth(1, 200);
          tblTemp.setColWidth(2, 1);
          tblTemp.setColWidth(3, 1);
          tblTemp.setColWidth(4, 1);
          tblTemp.setColWidth(5, 1);        
          StampeGaaServlet.setNoBorder(tblTemp);
          layout.setElement(elIDCurrent, tblTemp);
        }

        /**
         * Aggiungo il new line
         */
        if (k == 0)
          elID = "spConsZootecnica1";
        else
          elID = "spConsZootecnica10";
        elIDCurrent = elID + k;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        if (k == 0)
          elID = "nlConsZootecnica1";
        else
          elID = "nlConsZootecnica10";
        elIDCurrent = elID + k;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        if (k == 0)
        {
          subReport.removeElement("spConsZootecnica1");
          subReport.removeElement("nlConsZootecnica1");
        }

        /**
         * aggiungo le tabelle con le varie categorie ed il numero di capi
         * posseduto
         */
        
        
        Vector<SottoCategoriaAllevamento> sottoCategorieAllevamenti = 
          gaaFacadeClient.getTipiSottoCategoriaAllevamento(all.getIdAllevamentoLong().longValue());
        
        Vector<StabulazioneTrattamento> stabulazioniTrattamenti = 
          gaaFacadeClient.getStabulazioni(all.getIdAllevamentoLong().longValue(),true);
        
        int sizeCat=0,sizeStab=0;
        
        if (sottoCategorieAllevamenti!=null)
          sizeCat=sottoCategorieAllevamenti.size();
        if (stabulazioniTrattamenti!=null)
          sizeStab=stabulazioniTrattamenti.size();
        
        elID = "tblNumeroCapi";
        elIDCurrent = elID + k;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);

        tblTemp = new DefaultTableLens(subReport.getTable(elIDCurrent));
        
        for (int i=0;i<9;i++)
        {
          //Imposto il grasseto dell'header 
          tblTemp.setFont(0, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
          //imposto che le colonne siano centrate
          tblTemp.setAlignment(0,i, StyleConstants.H_CENTER);
          //imposto l'unione verticale delle prime 3 celle di ogno colonna
          tblTemp.setSpan(0, i , new Dimension(1, 3));
        }
        
        //Imposto il grasseto dell'header 
        tblTemp.setFont(0, 12, StampeGaaServlet.FONT_SERIF_BOLD_8);
        //imposto che le colonne siano centrate
        tblTemp.setAlignment(0,12, StyleConstants.H_CENTER);
        //imposto l'unione verticale delle prime 3 celle di ogno colonna
        tblTemp.setSpan(0, 12, new Dimension(1, 3));
        
        for (int i=8;i<12;i++)
        {
          //Imposto il grasseto dell'header 
          tblTemp.setFont(2, i, StampeGaaServlet.FONT_SERIF_BOLD_8);
          //imposto che le colonne siano centrate
          tblTemp.setAlignment(2,i, StyleConstants.H_CENTER);
        }

        
        
        
        tblTemp.setObject(0, 0, StampeGaaServlet.checkNull("Categoria"));
        tblTemp.setObject(0, 1, StampeGaaServlet.checkNull("Sottocateg."));
        tblTemp.setObject(0, 2, StampeGaaServlet.checkNull("Stabulazione"));
        if (sizeStab>0)
          tblTemp.setObject(0, 3, StampeGaaServlet.checkNull(all.getTipoSpecieAnimaleAnagVO().getUnitaDiMisura())+" stabulati");
        else
          tblTemp.setObject(0, 3, StampeGaaServlet.checkNull(all.getTipoSpecieAnimaleAnagVO().getUnitaDiMisura()));
        tblTemp.setObject(0, 4, StampeGaaServlet.checkNull("Peso vivo sanitario"));
        tblTemp.setObject(0, 5, StampeGaaServlet.checkNull("Tot Peso vivo"));
        if(Validator.isNotEmpty(dataInserimentoDichiarazione)
          && dataInserimentoDichiarazione.before(dataDettaglioAllevamenti))
        {
          tblTemp.setObject(0, 6, StampeGaaServlet.checkNull("n. gg vuoto san."));
        }
        else
        {
          tblTemp.setObject(0, 6, StampeGaaServlet.checkNull("Cicli allev.\nn/anno"));
        }
        tblTemp.setObject(0, 7, StampeGaaServlet.checkNull("Durata ciclo (gg)"));
        
        //Unisco le celle del pascolo
        tblTemp.setSpan(0, 8 , new Dimension(4, 1));
        tblTemp.setObject(0, 8, StampeGaaServlet.checkNull("Pascolo"));
        
        //Unisco le celle della primavera estate
        tblTemp.setSpan(1, 8 , new Dimension(2, 1));
        tblTemp.setObject(1, 8, StampeGaaServlet.checkNull("Primavera Estate"));
        
        //Imposto il grasseto dell'header 
        tblTemp.setFont(1, 8, StampeGaaServlet.FONT_SERIF_BOLD_8);
        //imposto che le colonne siano centrate
        tblTemp.setAlignment(1,8, StyleConstants.H_CENTER);
        
        
        //Unisco le celle dell'Autunno Inverno
        tblTemp.setSpan(1, 10 , new Dimension(2, 1));
        tblTemp.setObject(1, 10, StampeGaaServlet.checkNull("Autunno Inverno"));
        
        //Imposto il grasseto dell'header 
        tblTemp.setFont(1, 10, StampeGaaServlet.FONT_SERIF_BOLD_8);
        //imposto che le colonne siano centrate
        tblTemp.setAlignment(1, 10, StyleConstants.H_CENTER);

        
        tblTemp.setObject(2, 8, StampeGaaServlet.checkNull("gg"));
        tblTemp.setObject(2, 9, StampeGaaServlet.checkNull("hh"));
        tblTemp.setObject(2, 10, StampeGaaServlet.checkNull("gg"));
        tblTemp.setObject(2, 11, StampeGaaServlet.checkNull("hh"));
        tblTemp.setObject(0, 12, StampeGaaServlet.checkNull("Tot. UBA"));

        tblTemp.setColWidth(0, 80);
        tblTemp.setColWidth(1, 100);
        tblTemp.setColWidth(2, 105);
        tblTemp.setColWidth(3, 37);
        tblTemp.setColWidth(4, 37);
        tblTemp.setColWidth(5, 37);
        tblTemp.setColWidth(6, 25);
        tblTemp.setColWidth(7, 30);
        tblTemp.setColWidth(8, 17);
        tblTemp.setColWidth(9, 17);
        tblTemp.setColWidth(10,17);
        tblTemp.setColWidth(11, 17);
        tblTemp.setColWidth(12, 30);
        subReport.setElement(elIDCurrent, tblTemp);
        
        
        //parto da 3 perchè ci sono tre righe di intestazione
        int row=3;
        
        BigDecimal totaliPesi = new BigDecimal(0);
        int sommaTotaleCapi = 0;
        double sommaTotaleUBA =0;
        
        for (int j=0;j<sizeCat;j++)
        {
          SottoCategoriaAllevamento sottoCategoria = (SottoCategoriaAllevamento) sottoCategorieAllevamenti.get(j);
          
          if (sizeStab>0)
          {
            for (int ii=0;ii<sizeStab;ii++)
            {
              StabulazioneTrattamento stab = (StabulazioneTrattamento) stabulazioniTrattamenti.get(ii);
              
              if ((sottoCategoria.getIdSottoCategoriaAnimale()+"").equals(stab.getIdSottoCategoriaAnimale()))
              {
                tblTemp.addRow();
                tblTemp.setObject(row,0,StampeGaaServlet.checkNull(sottoCategoria.getDescCategoriaAnimale()));
                tblTemp.setObject(row,1,StampeGaaServlet.checkNull(sottoCategoria.getDescSottoCategoriaAnimale()));
                tblTemp.setObject(row,2,stab.getDescStabulazione());
                tblTemp.setObject(row,3,StampeGaaServlet.checkNull(stab.getQuantita()));
                tblTemp.setAlignment(row, 3, StyleConstants.H_RIGHT);
                tblTemp.setObject(row,4,StampeGaaServlet.checkNull(StringUtils.parsePesoCapi(sottoCategoria.getPesoVivo().replace(',','.'))));
                tblTemp.setAlignment(row, 4, StyleConstants.H_RIGHT);
                
                //Calcolo il peso vivo totale
                try
                {
                  BigDecimal quantita = new BigDecimal(stab.getQuantita());
                  BigDecimal pesoVivoUnitario = new BigDecimal(sottoCategoria.getPesoVivo().replace(',', '.'));
                  BigDecimal totalePesoVivo = quantita.multiply(pesoVivoUnitario);
                  totaliPesi = totaliPesi.add(totalePesoVivo);
                  sommaTotaleCapi+=quantita.intValue();
                  tblTemp.setObject(row,5,StampeGaaServlet.checkNull(StringUtils.parsePesoCapi(totalePesoVivo.toString())));
                  tblTemp.setAlignment(row, 5, StyleConstants.H_RIGHT);
                  double totaleUBA = quantita.doubleValue() * sottoCategoria.getCoefficienteUba();
                  sommaTotaleUBA += totaleUBA;
                  tblTemp.setObject(row,12,StringUtils.parsePesoCapi(String.valueOf(NumberUtils.arrotonda(totaleUBA, 2))));
                  tblTemp.setAlignment(row, 12, StyleConstants.H_RIGHT);
                }
                catch (Exception e)
                {
                  tblTemp.setObject(row,5,StampeGaaServlet.checkNull(""));
                  tblTemp.setObject(row,12,StampeGaaServlet.checkNull(""));
                }
                
                if(Validator.isNotEmpty(dataInserimentoDichiarazione)
                    && dataInserimentoDichiarazione.before(dataDettaglioAllevamenti))
                {
                  tblTemp.setObject(row,6,StampeGaaServlet.checkNull(sottoCategoria.getGiorniVuotoSanitario()));
                }
                else
                {
                  tblTemp.setObject(row,6,StampeGaaServlet.checkNull(sottoCategoria.getNumeroCicliAnnuali()));
                }
                tblTemp.setAlignment(row, 6, StyleConstants.H_RIGHT);
                tblTemp.setObject(row,7,StampeGaaServlet.checkNull(sottoCategoria.getCicli()));
                tblTemp.setAlignment(row, 7, StyleConstants.H_RIGHT);
                tblTemp.setObject(row,8,StampeGaaServlet.checkNull(sottoCategoria.getGiorniPascoloEstate()));
                tblTemp.setAlignment(row, 8, StyleConstants.H_RIGHT);
                tblTemp.setObject(row,9,StampeGaaServlet.checkNull(sottoCategoria.getOrePascoloEstate()));
                tblTemp.setAlignment(row, 9, StyleConstants.H_RIGHT);
                tblTemp.setObject(row,10,StampeGaaServlet.checkNull(sottoCategoria.getGiorniPascoloInverno()));
                tblTemp.setAlignment(row, 10, StyleConstants.H_RIGHT);
                tblTemp.setObject(row,11,StampeGaaServlet.checkNull(sottoCategoria.getOrePascoloInverno()));
                tblTemp.setAlignment(row, 11, StyleConstants.H_RIGHT); 
                row++;
              }
              
            }
          }
          else
          {
            tblTemp.addRow();
            tblTemp.setObject(row,0,StampeGaaServlet.checkNull(sottoCategoria.getDescCategoriaAnimale()));
            tblTemp.setObject(row,1,StampeGaaServlet.checkNull(sottoCategoria.getDescSottoCategoriaAnimale()));
            tblTemp.setObject(row,2,"");
            tblTemp.setObject(row,3,StampeGaaServlet.checkNull(sottoCategoria.getQuantita()));
            tblTemp.setAlignment(row, 3, StyleConstants.H_RIGHT);
            if (sottoCategoria.getPesoVivo()!=null)
            {
              tblTemp.setObject(row,4,StampeGaaServlet.checkNull(StringUtils.parsePesoCapi(sottoCategoria.getPesoVivo().replace(',','.'))));
              tblTemp.setAlignment(row, 4, StyleConstants.H_RIGHT);
            }
            else tblTemp.setObject(row,4,"");
            
            //Calcolo il peso vivo totale
            try
            {
              BigDecimal quantita = new BigDecimal(sottoCategoria.getQuantita());
              BigDecimal pesoVivoUnitario = new BigDecimal(0);
              if (sottoCategoria.getPesoVivo()!=null)
                pesoVivoUnitario = new BigDecimal(sottoCategoria.getPesoVivo().replace(',', '.'));
              BigDecimal totalePesoVivo = quantita.multiply(pesoVivoUnitario);
              totaliPesi = totaliPesi.add(totalePesoVivo);
              sommaTotaleCapi+=quantita.intValue();
              tblTemp.setObject(row,5,StampeGaaServlet.checkNull(StringUtils.parsePesoCapi(totalePesoVivo.toString())));
              tblTemp.setAlignment(row, 5, StyleConstants.H_RIGHT);
              double totaleUBA = quantita.doubleValue() * sottoCategoria.getCoefficienteUba();
              sommaTotaleUBA += totaleUBA;
              tblTemp.setObject(row,12,StringUtils.parsePesoCapi(String.valueOf(NumberUtils.arrotonda(totaleUBA, 2))));
              tblTemp.setAlignment(row, 12, StyleConstants.H_RIGHT);
            }
            catch (Exception e)
            {
              tblTemp.setObject(row,5,StampeGaaServlet.checkNull(""));
              tblTemp.setObject(row,12,StampeGaaServlet.checkNull(""));
            }
            
            if(Validator.isNotEmpty(dataInserimentoDichiarazione)
                && dataInserimentoDichiarazione.before(dataDettaglioAllevamenti))
            {
              tblTemp.setObject(row,6,StampeGaaServlet.checkNull(sottoCategoria.getGiorniVuotoSanitario()));
            }
            else
            {
              tblTemp.setObject(row,6,StampeGaaServlet.checkNull(sottoCategoria.getNumeroCicliAnnuali()));
            }
            tblTemp.setAlignment(row, 6, StyleConstants.H_RIGHT);
            tblTemp.setObject(row,7,StampeGaaServlet.checkNull(sottoCategoria.getCicli()));
            tblTemp.setAlignment(row, 7, StyleConstants.H_RIGHT);
            tblTemp.setObject(row,8,StampeGaaServlet.checkNull(sottoCategoria.getGiorniPascoloEstate()));
            tblTemp.setAlignment(row, 8, StyleConstants.H_RIGHT);
            tblTemp.setObject(row,9,StampeGaaServlet.checkNull(sottoCategoria.getOrePascoloEstate()));
            tblTemp.setAlignment(row, 9, StyleConstants.H_RIGHT);
            tblTemp.setObject(row,10,StampeGaaServlet.checkNull(sottoCategoria.getGiorniPascoloInverno()));
            tblTemp.setAlignment(row, 10, StyleConstants.H_RIGHT);
            tblTemp.setObject(row,11,StampeGaaServlet.checkNull(sottoCategoria.getOrePascoloInverno()));
            tblTemp.setAlignment(row, 11, StyleConstants.H_RIGHT); 
            row++;
          }
        }
        
        tblTemp.addRow();
        
        if(Validator.isNotEmpty(dataInserimentoDichiarazione)
            && dataInserimentoDichiarazione.after(dataDettaglioAllevamenti))
        {
          tblTemp.setFont(row, 1, StampeGaaServlet.FONT_SERIF_BOLD_8);
          //imposto che le colonne siano centrate
          tblTemp.setAlignment(row, 1, StyleConstants.H_CENTER);
          tblTemp.setObject(row, 1, StampeGaaServlet.checkNull("Totale"));
        }
        else
        {
          tblTemp.setFont(row, 2, StampeGaaServlet.FONT_SERIF_BOLD_8);
          //imposto che le colonne siano centrate
          tblTemp.setAlignment(row, 2, StyleConstants.H_CENTER);
          tblTemp.setObject(row, 2, StampeGaaServlet.checkNull("Totale"));
        }
        
        tblTemp.setFont(row, 3, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTemp.setAlignment(row, 3, StyleConstants.H_RIGHT);
        tblTemp.setObject(row, 3, StampeGaaServlet.checkNull(sommaTotaleCapi+""));
        
        tblTemp.setFont(row, 5, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTemp.setAlignment(row, 5, StyleConstants.H_RIGHT);
        tblTemp.setObject(row, 5, StampeGaaServlet.checkNull(StringUtils.parsePesoCapi(totaliPesi.toString())));
        
        tblTemp.setFont(row, 12, StampeGaaServlet.FONT_SERIF_BOLD_8);
        tblTemp.setAlignment(row, 12, StyleConstants.H_RIGHT);
        tblTemp.setObject(row, 12, StringUtils.parsePesoCapi(String.valueOf(NumberUtils.arrotonda(sommaTotaleUBA,2))));
        
        setTotConsNoBorder(tblTemp);
        
        
        if(Validator.isNotEmpty(dataInserimentoDichiarazione)
            && dataInserimentoDichiarazione.after(dataDettaglioAllevamenti))
        {
          //rimuovo stabulazioni
          tblTemp.removeColumn(2);
        }
        
        /**
         * Aggiungo il new line
         */
        if (k == 0)
          elID = "spConsZootecnica2";
        else
          elID = "spConsZootecnica20";
        elIDCurrent = elID + k;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        if (k == 0)
          elID = "nlConsZootecnica2";
        else
          elID = "nlConsZootecnica20";
        elIDCurrent = elID + k;
        element = StampeGaaServlet.reportElementClone(subReport, elID, elIDCurrent);
        layout.addElement(elStartRow, elStartCol, element);
        if (k == 0)
        {
          subReport.removeElement("spConsZootecnica2");
          subReport.removeElement("nlConsZootecnica2");
        }

      }
      subReport.removeElement("tblNumeroCapi");
    }
    else
    {
      subReport.removeElement("tblConsZootecnica1");
      subReport.removeElement("spConsZootecnica1");
      subReport.removeElement("nlConsZootecnica1");
      subReport.removeElement("spConsZootecnica2");
      subReport.removeElement("nlConsZootecnica2");
      subReport.removeElement("tblNumeroCapi");
    }
    
    
    
  }
  
  private final void setTotConsNoBorder(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 2, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 4, StyleConstants.NO_BORDER);
    for (int i=6;i<12;i++)
    {
      tblLens.setRowBorder(row-1, i, StyleConstants.NO_BORDER);
      if (i<11)
        tblLens.setColBorder(row-1, i, StyleConstants.NO_BORDER);
    }
  }
  
  
}