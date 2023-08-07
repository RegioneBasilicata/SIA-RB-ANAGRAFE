package it.csi.smranag.smrgaa.presentation.client.stampe.comunicazione10r;

import inetsoft.report.ReportSheet;
import inetsoft.report.StyleConstants;
import inetsoft.report.lens.DefaultTableLens;
import it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO;
import it.csi.smranag.smrgaa.dto.stampe.RichiestaTipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.smranag.smrgaa.presentation.client.stampe.SubReport;
import it.csi.smranag.smrgaa.util.Formatter;
import it.csi.smrcomms.reportdin.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;

import java.awt.Dimension;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class RispettoVincoli10R extends SubReport
{
  private final static String TEMPLATE_NAME     = "/it/csi/solmr/etc/anag/stampe/comunicazione10r/RispettoVincoli10R.srt";
  private final static String CODICE_SUB_REPORT = "RISPETTO_VINCOLI_10R";

  public RispettoVincoli10R() throws IOException, SolmrException
  {
    super(TEMPLATE_NAME, CODICE_SUB_REPORT);
  }

  public void processSubReport(RichiestaTipoReportVO richiestaTipoReportVO,
      ReportSheet subReport, HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, HashMap<String, Object> parametri)
      throws Exception
  {
    
    setQuadroAndSezione(subReport, richiestaTipoReportVO);    
    
    Comunicazione10RVO[] comunicazione10RVO = (Comunicazione10RVO[])parametri.get("comunicazione10RVO");
    
    String flagAdesioneDeroga = (String)anagFacadeClient.getValoreParametroAltriDati("DEROGA");
    String flagModAdesioneDeroga = (String)anagFacadeClient.getValoreParametroAltriDati("MOD_DEROGA");
    Date dataDettaglioAllevamenti = (Date)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DETT_ALLEVAMENTI);
    Date dataInserimentoDichiarazione = (Date)parametri.get("dataInserimentoDichiarazione");
    
    if (comunicazione10RVO != null && comunicazione10RVO.length!=0)
    { 
      
      
      int size = comunicazione10RVO.length;
      int col = 0;
      // Rimuovo il testo indicante l'assenza di cessioni
      // associate all'azienda
      subReport.removeElement("txtNoCom10r");
      subReport.removeElement("nlNoCom10r");
      
      
      
      //Solo in questo caso non devo visualizzare nelle altre tre combinazione si
      //quindi faccio il negato
      if(!("N".equalsIgnoreCase(flagAdesioneDeroga) && "N".equalsIgnoreCase(flagModAdesioneDeroga)))
      {
      
      
      
        //Adesione deroga
        DefaultTableLens tblAdesione = new DefaultTableLens(subReport.getTable("tblAdesione"));  
        tblAdesione.setRowCount(size + 1);// +1 necessario per l'header
        
        // Imposto l'header        
        tblAdesione.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblAdesione.setAlignment(0, col++, StyleConstants.H_CENTER);
        tblAdesione.setColWidth(0, 130);
        
        tblAdesione.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
        tblAdesione.setAlignment(0, col++, StyleConstants.H_CENTER);
        tblAdesione.setColWidth(1, 440);
        
        
        
        
        int rigaAdesione = 0;
        for (int i = 0; i < size; i++)
        {
          Comunicazione10RVO com10R = comunicazione10RVO[i];
          col = 0;
          rigaAdesione++;
          UteVO uteVO = anagFacadeClient.findUteByPrimaryKey(new Long(com10R.getIdUte()));
          String denominazioneUte = uteVO.getComuneUte().getDescom()+" ("+
            uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") - "+uteVO.getIndirizzo();
          
          tblAdesione.setObject(rigaAdesione, col++, StampeGaaServlet.checkNull(denominazioneUte));
          String adesioneDeroga = "No";
          if("S".equalsIgnoreCase(comunicazione10RVO[i].getAdesioneDeroga()))
          {
            adesioneDeroga = "Si";
          }
          tblAdesione.setObject(rigaAdesione, col, StampeGaaServlet.checkNull(adesioneDeroga));
          tblAdesione.setAlignment(rigaAdesione, col++, StyleConstants.H_CENTER);
          
        }
        
        subReport.setElement("tblAdesione", tblAdesione);
      }
      else
      {
        subReport.removeElement("txtAdesioneDeroga");
        subReport.removeElement("tblAdesione");
      }
      
      
      
      
      
      //Terreni
      DefaultTableLens tblVincoliTerreni = new DefaultTableLens(subReport.getTable("tblVincoliTerreni"));

      tblVincoliTerreni.setRowCount(size + 3);// +2 necessario per l'header, +1 per i totali
      
      // Imposto l'header
      col = 0;
      tblVincoliTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblVincoliTerreni.setColWidth(0, 130);
      
      tblVincoliTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblVincoliTerreni.setColWidth(1, 80);
      
      if(Validator.isEmpty(dataInserimentoDichiarazione)
        || (Validator.isNotEmpty(dataInserimentoDichiarazione)) && dataInserimentoDichiarazione.after(dataDettaglioAllevamenti))
      {
        tblVincoliTerreni.setObject(0, col, StampeGaaServlet.checkNull("Azoto prodotto\nin stalla (kg/anno)"));
      }
      tblVincoliTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setAlignment(0, col, StyleConstants.H_CENTER);
      tblVincoliTerreni.setColWidth(2, 80);
      
      tblVincoliTerreni.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setAlignment(1, col++, StyleConstants.H_CENTER);
      tblVincoliTerreni.setColWidth(2, 80);
      
      tblVincoliTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setAlignment(0, col, StyleConstants.H_CENTER);
      tblVincoliTerreni.setColWidth(3, 80);
      
      tblVincoliTerreni.setFont(1, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setAlignment(1, col++, StyleConstants.H_CENTER);
      tblVincoliTerreni.setColWidth(3, 80);
      
      if(Validator.isEmpty(dataInserimentoDichiarazione)
        || (Validator.isNotEmpty(dataInserimentoDichiarazione)) && dataInserimentoDichiarazione.after(dataDettaglioAllevamenti))
      {
        tblVincoliTerreni.setObject(0, col, StampeGaaServlet.checkNull("Azoto all'utilizzo\nagronomico (kg)"));
      }
      tblVincoliTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblVincoliTerreni.setColWidth(4, 110);
      
      tblVincoliTerreni.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblVincoliTerreni.setColWidth(5, 80);
      
      BigDecimal totRicettivitaAzoto = new BigDecimal(0);
      BigDecimal totTotaleAzotoAziendale = new BigDecimal(0);
      BigDecimal totAzotoEscretoPascolo = new BigDecimal(0);
      //BigDecimal totAzotoCessAcqu = new BigDecimal(0);
      BigDecimal totAzotoUtilizzoAgronomico = new BigDecimal(0);
      BigDecimal totDiffRicett_AzotoCessAcqu = new BigDecimal(0);
      
      
      for (int i = 0; i < size; i++)
      {
        Comunicazione10RVO com10R = comunicazione10RVO[i];
        col = 0;
        UteVO uteVO = anagFacadeClient.findUteByPrimaryKey(new Long(com10R.getIdUte()));
        String denominazioneUte = uteVO.getComuneUte().getDescom()+" ("+
          uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") - "+uteVO.getIndirizzo();        
        
        
        //Terreni
        tblVincoliTerreni.setObject(i + 2, col++, StampeGaaServlet.checkNull(denominazioneUte));
        
        BigDecimal ricettivitaAzoto = new BigDecimal(0);
        ricettivitaAzoto = ricettivitaAzoto.add(com10R.getAzotoConduzioneZVN());
        ricettivitaAzoto = ricettivitaAzoto.add(com10R.getAzotoConduzioneNoZVN());
        ricettivitaAzoto = ricettivitaAzoto.add(com10R.getAzotoAsservimentoZVN());
        ricettivitaAzoto = ricettivitaAzoto.add(com10R.getAzotoAsservimentoNoZVN());
        
        if(ricettivitaAzoto.compareTo(new BigDecimal(0)) < 0)
        {
          tblVincoliTerreni.setObject(i + 2, col, " - ");
          tblVincoliTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        }
        else
        {
          tblVincoliTerreni.setObject(i + 2, col, Formatter.formatAndRoundBigDecimal0(ricettivitaAzoto));
          tblVincoliTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
          totRicettivitaAzoto = totRicettivitaAzoto.add(Formatter.roundBigDecimal0(ricettivitaAzoto));
        }        
        
        
        BigDecimal azotoUtilizzoAgronomico = new BigDecimal(0); 
        tblVincoliTerreni.setObject(i + 2, col, Formatter.formatAndRoundBigDecimal0(com10R.getTotaleAzotoAziendale()));
        tblVincoliTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        totTotaleAzotoAziendale = totTotaleAzotoAziendale.add(Formatter.roundBigDecimal0(com10R.getTotaleAzotoAziendale()));  
        azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.add(com10R.getTotaleAzotoAziendale());
        
        
        Vector<BigDecimal> vSommaCessAqu = gaaFacadeClient
            .getSommaEffluentiCessAcquPerStampa(com10R.getIdComunicazione10R());
        if(Validator.isNotEmpty(vSommaCessAqu))
        {
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(vSommaCessAqu.get(0));
          azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.add(vSommaCessAqu.get(1));
        }
        
        
        
        BigDecimal azotoEscretoPascolo = new BigDecimal(0);
        if(com10R.getAzotoEscretoPascolo() != null)
        {
          azotoEscretoPascolo = azotoEscretoPascolo.add(com10R.getAzotoEscretoPascolo());
        }
        
        if(azotoEscretoPascolo.compareTo(new BigDecimal(0)) < 0)
        {
          tblVincoliTerreni.setObject(i + 2, col, " - ");
          tblVincoliTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        }
        else
        {
          tblVincoliTerreni.setObject(i + 2, col, Formatter.formatAndRoundBigDecimal0(azotoEscretoPascolo));
          tblVincoliTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
          totAzotoEscretoPascolo = totAzotoEscretoPascolo.add(Formatter.roundBigDecimal0(azotoEscretoPascolo));
          //azotoUtilizzoAgronomico = azotoUtilizzoAgronomico.subtract(azotoEscretoPascolo);
        }
        
        tblVincoliTerreni.setObject(i + 2, col, Formatter.formatAndRoundBigDecimal0(azotoUtilizzoAgronomico));
        tblVincoliTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        totAzotoUtilizzoAgronomico = totAzotoUtilizzoAgronomico.add(Formatter.roundBigDecimal0(azotoUtilizzoAgronomico));
        
        
        /*BigDecimal azotoCessAcqu = com10R.getTotaleAzotoAziendale();
        Vector<BigDecimal> vSommaCessAqu = gaaFacadeClient
          .getSommaEffluentiCessAcquPerStampa(com10R.getIdComunicazione10R());
        azotoCessAcqu = azotoCessAcqu.subtract(vSommaCessAqu.get(0));
        azotoCessAcqu = azotoCessAcqu.add(vSommaCessAqu.get(1));
        
        if(azotoEscretoPascolo.compareTo(new BigDecimal(0)) < 0)
        {
          tblVincoliTerreni.setObject(i + 2, col, " - ");
          tblVincoliTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        }
        else
        {
          tblVincoliTerreni.setObject(i + 2, col, Formatter.formatAndRoundBigDecimal0(azotoCessAcqu));
          tblVincoliTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
          totAzotoCessAcqu = totAzotoCessAcqu.add(Formatter.roundBigDecimal0(azotoCessAcqu));
        }*/
        
        //BigDecimal diffRicett_AzotoCessAcqu = ricettivitaAzoto.subtract(azotoCessAcqu);
        BigDecimal diffRicett_AzotoCessAcqu = ricettivitaAzoto.subtract(azotoUtilizzoAgronomico);
        if(diffRicett_AzotoCessAcqu.compareTo(new BigDecimal(0)) < 0)
        {
          tblVincoliTerreni.setObject(i + 2, col, " - ");
          tblVincoliTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
        }
        else
        {
          tblVincoliTerreni.setObject(i + 2, col, Formatter.formatAndRoundBigDecimal0(diffRicett_AzotoCessAcqu));
          tblVincoliTerreni.setAlignment(i + 2, col++, StyleConstants.H_CENTER);
          totDiffRicett_AzotoCessAcqu = totDiffRicett_AzotoCessAcqu.add(Formatter.roundBigDecimal0(diffRicett_AzotoCessAcqu));
        }
      }
      
      //Terreni
      tblVincoliTerreni.setObject(size + 2, 0, "Totale ");
      tblVincoliTerreni.setAlignment(size + 2, 0, StyleConstants.H_RIGHT);
      tblVincoliTerreni.setFont(size + 2, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setObject(size + 2, 1,  Formatter.formatAndRoundBigDecimal0(totRicettivitaAzoto));
      tblVincoliTerreni.setAlignment(size + 2, 1, StyleConstants.H_CENTER);
      tblVincoliTerreni.setFont(size + 2, 1, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setObject(size + 2, 2,  Formatter.formatAndRoundBigDecimal0(totTotaleAzotoAziendale));
      tblVincoliTerreni.setAlignment(size + 2, 2, StyleConstants.H_CENTER);
      tblVincoliTerreni.setFont(size + 2, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setObject(size + 2, 3,  Formatter.formatAndRoundBigDecimal0(totAzotoEscretoPascolo));
      tblVincoliTerreni.setAlignment(size + 2, 3, StyleConstants.H_CENTER);
      tblVincoliTerreni.setFont(size + 2, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setObject(size + 2, 4,  Formatter.formatAndRoundBigDecimal0(totAzotoUtilizzoAgronomico));
      tblVincoliTerreni.setAlignment(size + 2, 4, StyleConstants.H_CENTER);
      tblVincoliTerreni.setFont(size + 2, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliTerreni.setObject(size + 2, 5,  Formatter.formatAndRoundBigDecimal0(totDiffRicett_AzotoCessAcqu));
      tblVincoliTerreni.setAlignment(size + 2, 5, StyleConstants.H_CENTER);
      tblVincoliTerreni.setFont(size + 2, 5, StampeGaaServlet.FONT_SERIF_BOLD_10);
      
      setNoBorderVincoliTerreni(tblVincoliTerreni);
      
      subReport.setElement("tblVincoliTerreni", tblVincoliTerreni);
      
      
      
      
      //Stoccaggio
      DefaultTableLens tblVincoliStoccaggio = new DefaultTableLens(subReport.getTable("tblVincoliStoccaggio"));

      tblVincoliStoccaggio.setRowCount(size*2 + 2);// *2 (moltiplicatore + palabile e non palabile) +1 necessario per l'header, +1 per i totali
      // Imposto l'header
      col = 0;
      tblVincoliStoccaggio.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliStoccaggio.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblVincoliStoccaggio.setColWidth(0, 130);
      
      tblVincoliStoccaggio.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliStoccaggio.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblVincoliStoccaggio.setColWidth(1, 100);
      
      tblVincoliStoccaggio.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliStoccaggio.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblVincoliStoccaggio.setColWidth(2, 100);
      
      tblVincoliStoccaggio.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliStoccaggio.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblVincoliStoccaggio.setColWidth(3, 100);
      
      tblVincoliStoccaggio.setFont(0, col, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliStoccaggio.setAlignment(0, col++, StyleConstants.H_CENTER);
      tblVincoliStoccaggio.setColWidth(4, 130);
      
      BigDecimal totDisponibile = new BigDecimal(0);
      BigDecimal totNecessario = new BigDecimal(0);
      BigDecimal totDiffDisponibileNecessario = new BigDecimal(0);
      
      int rigaStoc = 0;
      for (int i = 0; i < size; i++)
      {
        Comunicazione10RVO com10R = comunicazione10RVO[i];
        col = 0;
        rigaStoc++;
        UteVO uteVO = anagFacadeClient.findUteByPrimaryKey(new Long(com10R.getIdUte()));
        String denominazioneUte = uteVO.getComuneUte().getDescom()+" ("+
          uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") - "+uteVO.getIndirizzo();
        
        //una riga per il palabile
        col = 0;
        tblVincoliStoccaggio.setObject(rigaStoc, col++, StampeGaaServlet.checkNull(denominazioneUte));
        
        tblVincoliStoccaggio.setObject(rigaStoc, col, "Palabile");
        tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
        
        BigDecimal disponibile = com10R.getStocDispPalabileVol();
        if(disponibile.compareTo(new BigDecimal(0)) < 0)
        {
          tblVincoliStoccaggio.setObject(rigaStoc, col, " - ");
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
        }
        else
        {
          tblVincoliStoccaggio.setObject(rigaStoc, col, Formatter.formatAndRoundBigDecimal1(disponibile));
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
          totDisponibile = totDisponibile.add(Formatter.roundBigDecimal1(disponibile));
        }
        
        BigDecimal necessario = new BigDecimal(0);
        BigDecimal stocNecessarioVol = gaaFacadeClient.getSommaEffluenti10RPerStampa(com10R.getIdComunicazione10R(), true);
        if(Validator.isNotEmpty(stocNecessarioVol))
        {
          necessario = necessario.add(stocNecessarioVol);
        }
        
        if(necessario.compareTo(new BigDecimal(0)) < 0)
        {
          tblVincoliStoccaggio.setObject(rigaStoc, col, " - ");
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
          necessario = new BigDecimal(0);
        }
        else
        {
          tblVincoliStoccaggio.setObject(rigaStoc, col, Formatter.formatAndRoundBigDecimal1(necessario));
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
          totNecessario = totNecessario.add(Formatter.roundBigDecimal1(necessario));
        }
        
        BigDecimal diffDisponibileNecessario = disponibile;
        diffDisponibileNecessario = diffDisponibileNecessario.subtract(necessario);
        if(diffDisponibileNecessario.compareTo(new BigDecimal(0)) < 0)
        {
          tblVincoliStoccaggio.setObject(rigaStoc, col, " - ");
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
        }
        else
        {          
          tblVincoliStoccaggio.setObject(rigaStoc, col, Formatter.formatAndRoundBigDecimal1(diffDisponibileNecessario));
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
          totDiffDisponibileNecessario = totDiffDisponibileNecessario.add(Formatter.roundBigDecimal1(diffDisponibileNecessario));
        }
        
        //una riga per il non palabile
        col = 0;
        rigaStoc++;
        tblVincoliStoccaggio.setObject(rigaStoc, col++, StampeGaaServlet.checkNull(denominazioneUte));
        
        tblVincoliStoccaggio.setObject(rigaStoc, col, "Non palabile");
        tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
        
        disponibile = com10R.getStocDispNonPalabileVol();
        if(disponibile.compareTo(new BigDecimal(0)) < 0)
        {
          tblVincoliStoccaggio.setObject(rigaStoc, col, " - ");
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
        }
        else
        {
          tblVincoliStoccaggio.setObject(rigaStoc, col, Formatter.formatAndRoundBigDecimal1(disponibile));
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
          totDisponibile = totDisponibile.add(Formatter.roundBigDecimal1(disponibile));
        }
        
        necessario = com10R.getStocAcqueNecVol();
        stocNecessarioVol = gaaFacadeClient.getSommaEffluenti10RPerStampa(com10R.getIdComunicazione10R(), false);
        if(Validator.isNotEmpty(stocNecessarioVol))
        {
          necessario = necessario.add(stocNecessarioVol);
        }
        if(necessario.compareTo(new BigDecimal(0)) < 0)
        {
          tblVincoliStoccaggio.setObject(rigaStoc, col, " - ");
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
          necessario = new BigDecimal(0);
        }
        else
        {
          tblVincoliStoccaggio.setObject(rigaStoc, col, Formatter.formatAndRoundBigDecimal1(necessario));
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
          totNecessario = totNecessario.add(Formatter.roundBigDecimal1(necessario));
        }
        
        diffDisponibileNecessario = disponibile;
        diffDisponibileNecessario = diffDisponibileNecessario.subtract(necessario);
        if(diffDisponibileNecessario.compareTo(new BigDecimal(0)) < 0)
        {
          tblVincoliStoccaggio.setObject(rigaStoc, col, " - ");
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
        }
        else
        {
          tblVincoliStoccaggio.setObject(rigaStoc, col, Formatter.formatAndRoundBigDecimal1(diffDisponibileNecessario));
          tblVincoliStoccaggio.setAlignment(rigaStoc, col++, StyleConstants.H_CENTER);
          totDiffDisponibileNecessario = totDiffDisponibileNecessario.add(Formatter.roundBigDecimal1(diffDisponibileNecessario));
        }
      }
      
      tblVincoliStoccaggio.setSpan(size*2 + 1, 0 , new Dimension(2, 1));
      tblVincoliStoccaggio.setObject(size*2 + 1, 0, "Totale ");
      tblVincoliStoccaggio.setAlignment(size*2 + 1, 0, StyleConstants.H_RIGHT);
      tblVincoliStoccaggio.setFont(size*2 + 1, 0, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliStoccaggio.setObject(size*2 + 1, 2,  Formatter.formatAndRoundBigDecimal1(totDisponibile));
      tblVincoliStoccaggio.setAlignment(size*2 + 1, 2, StyleConstants.H_CENTER);
      tblVincoliStoccaggio.setFont(size*2 + 1, 2, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliStoccaggio.setObject(size*2 + 1, 3,  Formatter.formatAndRoundBigDecimal1(totNecessario));
      tblVincoliStoccaggio.setAlignment(size*2 + 1, 3, StyleConstants.H_CENTER);
      tblVincoliStoccaggio.setFont(size*2 + 1, 3, StampeGaaServlet.FONT_SERIF_BOLD_10);
      tblVincoliStoccaggio.setObject(size*2 + 1, 4,  Formatter.formatAndRoundBigDecimal1(totDiffDisponibileNecessario));
      tblVincoliStoccaggio.setAlignment(size*2 + 1, 4, StyleConstants.H_CENTER);
      tblVincoliStoccaggio.setFont(size*2 + 1, 4, StampeGaaServlet.FONT_SERIF_BOLD_10);
      
      setNoBorderVincoliStoccaggio(tblVincoliStoccaggio);
     
      subReport.setElement("tblVincoliStoccaggio", tblVincoliStoccaggio);
      
    }
    else
    {
      // Rimuovo tutte le tabelle
      subReport.removeElement("txtAdesioneDeroga");
      subReport.removeElement("nlAdesioneDeroga1");
      subReport.removeElement("tblAdesioneDeroga");
      subReport.removeElement("nlAdesioneDeroga2");
      subReport.removeElement("txtTerreni");
      subReport.removeElement("nlTerreni");
      subReport.removeElement("tblVincoliTerreni");
      subReport.removeElement("nlVincoliTerreni");
      subReport.removeElement("txtStoccaggio");
      subReport.removeElement("nlVincoliStoccaggio");
      subReport.removeElement("tblVincoliStoccaggio");
      subReport.removeElement("tblAdesione");
    }   
    
    
  }
  
  
  private final void setNoBorderVincoliTerreni(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 0, StyleConstants.NO_BORDER);
  }
  
  private final void setNoBorderVincoliStoccaggio(DefaultTableLens tblLens)
  {
    int row=tblLens.getRowCount();
    tblLens.setColBorder(row-1, -1, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setColBorder(row-1, 0, StyleConstants.NO_BORDER);
    tblLens.setRowBorder(row-1, 1, StyleConstants.NO_BORDER);
  }
  
}