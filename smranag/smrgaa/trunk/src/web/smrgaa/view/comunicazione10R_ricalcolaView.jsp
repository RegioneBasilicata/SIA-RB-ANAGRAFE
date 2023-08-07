<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>


<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteCesAcqVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteStocExtVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.AcquaExtraVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.comunicazione10R.RefluoEffluenteVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/comunicazione10R_ricalcola.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  HashMap hElencoUte = (HashMap)request.getAttribute("hElencoUte");
  Vector vTotCom10r = (Vector)request.getAttribute("vTotCom10r");
  String flagAdesioneDeroga = (String)request.getAttribute("flagAdesioneDeroga");
  String flagModAdesioneDeroga = (String)request.getAttribute("flagModAdesioneDeroga");
  final int CESSIONI = 1;
  
  
  //Variabili che prmettono di creare il blocco solo la prima volta
  boolean flagPrimoCessAcqu = true;
  boolean flagPrimoStocc = true;
  boolean flagPrimoVolumeSottogrigliato = true;
  boolean flagPrimoVolumeRefluoAzienda = true;
  boolean flagPrimoAcqueReflue = true;
  //***************************************
  if(vTotCom10r !=null) //esiste almeno una dichiarazione 10r collegata ad una ute
  {
    htmpl.newBlock("blkNoErr");
    //data ricalcolo e data ultimo aggiornamento e adesione deroga sono uguali per tutte le ute quindi ne prendo una a caso, la prima!!
    HashMap hCom10RData = (HashMap)vTotCom10r.get(0);
    Comunicazione10RVO com10Data = (Comunicazione10RVO)hCom10RData.get("comunicazione10R");
    htmpl.set("blkNoErr.dataUltimoRicalcolo", DateUtils.formatDateTimeNotNull(com10Data.getDataRicalcolo()));
    
    
    //Solo in questo caso non devo visualizzare nelle altre tre combinazione si
    //quindi faccio il negato
    if(!("N".equalsIgnoreCase(flagAdesioneDeroga) && "N".equalsIgnoreCase(flagModAdesioneDeroga)))
    {
      htmpl.newBlock("blkNoErr.blkAdesioneDeroga");
    }
    
    
    
    //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
    //al ruolo!
    ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
      "blkNoErr.ultimoAggiornamento", DateUtils.formatDateNotNull(com10Data.getDataAggiornamento()), 
      com10Data.getUtenteUltimaModifica(), com10Data.getEnteUltimaModifica(), null);
      
      
    //Terreni  
    BigDecimal totConduzioneZVN = new BigDecimal(0);
    BigDecimal totConduzioneNoZVN = new BigDecimal(0);
    BigDecimal totAsservimentoZVN = new BigDecimal(0);
    BigDecimal totAsservimentoNoZVN = new BigDecimal(0);
    BigDecimal totRicettivitaAzoto = new BigDecimal(0);
    BigDecimal totAzotoZooAziendale = new BigDecimal(0);
    BigDecimal totAzotoEscretoPascolo = new BigDecimal(0);
    BigDecimal totAzotoNettCessioni = new BigDecimal(0);
    //Stoccaggio
    BigDecimal totStocNettoPalabile = new BigDecimal(0);
    BigDecimal totStocDispPalabile = new BigDecimal(0);
    BigDecimal totStocNecPalabile = new BigDecimal(0);
    BigDecimal totStocNettoNonPalabile = new BigDecimal(0);
    BigDecimal totStocDispNonPalabile = new BigDecimal(0);
    BigDecimal totStocNecNonPalabile = new BigDecimal(0);
    
    //aziendaZVN
    BigDecimal totSupSauPiemonte = new BigDecimal(0);
    BigDecimal totSupSauPiemonteZVN = new BigDecimal(0);
      
      
    for(int i=0;i<vTotCom10r.size();i++)
    {
      HashMap hCom10R = (HashMap)vTotCom10r.get(i);
      Comunicazione10RVO com10 = (Comunicazione10RVO)hCom10R.get("comunicazione10R");
      
      
      
      UteVO uteVO = (UteVO)hElencoUte.get(new Long(com10.getIdUte()));
      
      //Solo in questo caso non devo visualizzare nelle altre tre combinazione si
      //quindi faccio il negato
      if(!("N".equalsIgnoreCase(flagAdesioneDeroga) && "N".equalsIgnoreCase(flagModAdesioneDeroga)))
      {
        htmpl.newBlock("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga");
        //UteVO uteVO = (UteVO)hElencoUte.get(new Long(com10.getIdUte()));
        
        htmpl.set("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga.descUteAdesioneDeroga", uteVO.getComuneUte().getDescom()
         +" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") - "+uteVO.getIndirizzo()); 
        
        
        String adesioneDeroga = "No";
        if("S".equalsIgnoreCase(com10.getAdesioneDeroga()))
        {
          adesioneDeroga = "Si";
        }
        htmpl.set("blkNoErr.blkAdesioneDeroga.blkElencoAdesioneDeroga.adesioneDeroga", adesioneDeroga);
      }
      
      //Note      
      htmpl.newBlock("blkNote");  
      htmpl.set("blkNoErr.blkNote.descComuneUteNote", uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")");
      htmpl.set("blkNoErr.blkNote.indirizzoUteNote", uteVO.getIndirizzo());
      htmpl.set("blkNoErr.blkNote.noteComunicazione", com10.getNote());
      
      
      //*******************Calcolo totali aziendali!!!!
      totConduzioneZVN = totConduzioneZVN.add(com10.getSuperficieConduzioneZVN());
      totConduzioneNoZVN = totConduzioneNoZVN.add(com10.getSuperficieConduzioneNoZVN());
      totAsservimentoZVN = totAsservimentoZVN.add(com10.getSuperficieAsservimentoZVN());
      totAsservimentoNoZVN = totAsservimentoNoZVN.add(com10.getSuperficieAsservimentoNoZVN());
      
      totRicettivitaAzoto = totRicettivitaAzoto.add(com10.getAzotoConduzioneZVN());
      totRicettivitaAzoto = totRicettivitaAzoto.add(com10.getAzotoConduzioneNoZVN());
      totRicettivitaAzoto = totRicettivitaAzoto.add(com10.getAzotoAsservimentoZVN());
      totRicettivitaAzoto = totRicettivitaAzoto.add(com10.getAzotoAsservimentoNoZVN());
      
      totAzotoZooAziendale = totAzotoZooAziendale.add(com10.getTotaleAzotoAziendale()); 
         
      
      if(com10.getTotaleAzotoAziendale() != null)
      {
        totAzotoNettCessioni = totAzotoNettCessioni.add(com10.getTotaleAzotoAziendale());
      }
      
      //totStocNettoPalabile = totStocNettoPalabile.add(com10.getStocNettoPalabile());
      totStocDispPalabile = totStocDispPalabile.add(com10.getStocDispPalabileVol());
      
      //totStocNettoNonPalabile = totStocNettoNonPalabile.add(com10.getStocNettoNonPalabile());
      totStocDispNonPalabile = totStocDispNonPalabile.add(com10.getStocDispNonPalabileVol());
    
      totStocNecNonPalabile = totStocNecNonPalabile.add(com10.getStocAcqueNecVol());
      
      if(com10.getSuperficieSauPiemonte()!= null)
      {
        totSupSauPiemonte = totSupSauPiemonte.add(com10.getSuperficieSauPiemonte());
      }
      
      if(com10.getSuperficieSauPiemonteZVN()!= null)
      {
        totSupSauPiemonteZVN = totSupSauPiemonteZVN.add(com10.getSuperficieSauPiemonteZVN());
      }
      
      //****************************************************************************      
      
      htmpl.newBlock("blkTerrenoRefluo");
      
      //UteVO uteVO = (UteVO)hElencoUte.get(new Long(com10.getIdUte()));
      htmpl.set("blkNoErr.blkTerrenoRefluo.descComuneUte", uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")");
      htmpl.set("blkNoErr.blkTerrenoRefluo.indirizzoUte", uteVO.getIndirizzo()); 
      htmpl.set("blkNoErr.blkTerrenoRefluo.supCondZVN", Formatter.formatDouble4(com10.getSuperficieConduzioneZVN()));
      htmpl.set("blkNoErr.blkTerrenoRefluo.azotoCondZVN", Formatter.formatAndRoundBigDecimal0(com10.getAzotoConduzioneZVN()));
      htmpl.set("blkNoErr.blkTerrenoRefluo.supAssZVN", Formatter.formatDouble4(com10.getSuperficieAsservimentoZVN()));
      htmpl.set("blkNoErr.blkTerrenoRefluo.azotoAssZVN", Formatter.formatAndRoundBigDecimal0(com10.getAzotoAsservimentoZVN()));
      htmpl.set("blkNoErr.blkTerrenoRefluo.supCondNoZVN", Formatter.formatDouble4(com10.getSuperficieConduzioneNoZVN()));
      htmpl.set("blkNoErr.blkTerrenoRefluo.azotoCondNoZVN", Formatter.formatAndRoundBigDecimal0(com10.getAzotoConduzioneNoZVN()));
      htmpl.set("blkNoErr.blkTerrenoRefluo.supAssNoZVN", Formatter.formatDouble4(com10.getSuperficieAsservimentoNoZVN()));
      htmpl.set("blkNoErr.blkTerrenoRefluo.azotoAssNoZVN", Formatter.formatAndRoundBigDecimal0(com10.getAzotoAsservimentoNoZVN()));
      
      
      BigDecimal totaleSup = new BigDecimal(0);
      totaleSup = totaleSup.add(com10.getSuperficieConduzioneZVN());
      totaleSup = totaleSup.add(com10.getSuperficieAsservimentoZVN());
      totaleSup = totaleSup.add(com10.getSuperficieConduzioneNoZVN());
      totaleSup = totaleSup.add(com10.getSuperficieAsservimentoNoZVN());
      htmpl.set("blkNoErr.blkTerrenoRefluo.totaleSup",  Formatter.formatDouble4(totaleSup));
      
      BigDecimal totaleAzoto = new BigDecimal(0);
      totaleAzoto = totaleAzoto.add(com10.getAzotoConduzioneZVN());
      totaleAzoto = totaleAzoto.add(com10.getAzotoAsservimentoZVN());
      totaleAzoto = totaleAzoto.add(com10.getAzotoConduzioneNoZVN());
      totaleAzoto = totaleAzoto.add(com10.getAzotoAsservimentoNoZVN());
      htmpl.set("blkNoErr.blkTerrenoRefluo.totaleAzoto", Formatter.formatAndRoundBigDecimal0(totaleAzoto));
      
      BigDecimal totaleAzotoAziendale = Formatter.roundBigDecimal0(com10.getTotaleAzotoAziendale());
      htmpl.set("blkNoErr.blkTerrenoRefluo.totaleAzotoAziendale", Formatter.formatDouble(totaleAzotoAziendale));
      
      BigDecimal azotoEscretoPascolo = new BigDecimal(0);
      if(com10.getAzotoEscretoPascolo() != null)
      {
        azotoEscretoPascolo = Formatter.roundBigDecimal0(com10.getAzotoEscretoPascolo());
        totAzotoEscretoPascolo = totAzotoEscretoPascolo.add(azotoEscretoPascolo);
      }
      htmpl.set("blkNoErr.blkTerrenoRefluo.azotoEscretoPascolo", Formatter.formatDouble(azotoEscretoPascolo));
            
      BigDecimal totaleAzotoAziendaleNetCessAcqu = new BigDecimal(0);
      if(com10.getTotaleAzotoAziendale() != null)
      {
        totaleAzotoAziendaleNetCessAcqu = totaleAzotoAziendaleNetCessAcqu.add(com10.getTotaleAzotoAziendale());
      }
      
      Vector<EffluenteCesAcqVO> vAcquisizioniPerTot = (Vector<EffluenteCesAcqVO>)hCom10R.get("vAcquisizioni");
      if(vAcquisizioniPerTot != null)
      {
        for(int j=0;j<vAcquisizioniPerTot.size();j++)
        {
          EffluenteCesAcqVO effVO = vAcquisizioniPerTot.get(j);
          totAzotoNettCessioni = totAzotoNettCessioni.add(Formatter.roundBigDecimal0(effVO.getQuantitaAzotoDichiarato()));
          if(effVO.getIdUte() == com10.getIdUte())
          {
            totaleAzotoAziendaleNetCessAcqu = totaleAzotoAziendaleNetCessAcqu.add(Formatter.roundBigDecimal0(effVO.getQuantitaAzotoDichiarato()));
          }
        }
      }
      
      Vector<EffluenteCesAcqVO> vCessioniPerTot = (Vector<EffluenteCesAcqVO>)hCom10R.get("vCessioni");
      if(vCessioniPerTot != null)
      {
        for(int j=0;j<vCessioniPerTot.size();j++)
        {
          EffluenteCesAcqVO effVO = vCessioniPerTot.get(j);
          totAzotoNettCessioni = totAzotoNettCessioni.subtract(Formatter.roundBigDecimal0(effVO.getQuantitaAzotoDichiarato()));
          if(effVO.getIdUte() == com10.getIdUte())
          {
            totaleAzotoAziendaleNetCessAcqu = totaleAzotoAziendaleNetCessAcqu.subtract(Formatter.roundBigDecimal0(effVO.getQuantitaAzotoDichiarato()));
          }
        }
      }
      
      /*if(com10.getAzotoEscretoPascolo() != null)
      {
        totaleAzotoAziendaleNetCessAcqu = totaleAzotoAziendaleNetCessAcqu.subtract(com10.getAzotoEscretoPascolo());
        //totAzotoNettCessioni = totAzotoNettCessioni.subtract(com10.getAzotoEscretoPascolo()); 
      }*/
      
      htmpl.set("blkNoErr.blkTerrenoRefluo.totaleAzotoAziendaleNetCessAcqu", Formatter.formatAndRoundBigDecimal0(totaleAzotoAziendaleNetCessAcqu));
      
      
      
      if(totaleAzotoAziendaleNetCessAcqu != null)
      {
        if(totaleAzotoAziendaleNetCessAcqu.compareTo(totaleAzoto) > 0 )
          htmpl.set("blkNoErr.blkTerrenoRefluo.coloreTotale", "tot red");
        else
          htmpl.set("blkNoErr.blkTerrenoRefluo.coloreTotale", "tot");
      }
      else
      {
        htmpl.set("blkNoErr.blkTerrenoRefluo.coloreTotale", "tot");
      }
    
    
      Vector<EffluenteVO> vEffluenti = (Vector)hCom10R.get("vEffluenti");
      if(vEffluenti !=null)
      {
      
        
        BigDecimal totStoccaggioDiponibileVol = new BigDecimal(0);
        BigDecimal totStocNecVol = new BigDecimal(0);     
        
        int intPalabile = 0;
        int intNonPalabile = 0;
        boolean flagPrimoPalabile = true;
        boolean flagPrimoNonPalabile = true;
        
        //Calcolo il numero di palabili e quelli non palabili
        //per la colonna stoccaggio disponibile m3
        for(int j=0;j<vEffluenti.size();j++)
        {
          EffluenteVO effVO = vEffluenti.get(j);
          if(effVO.getTipoEffluente().equals("Palabile"))
          {
            intPalabile++;   
          }
          else
          {
            intNonPalabile++;
          }      
        }
        
      
      
        htmpl.newBlock("blkEffluentiPrimo");
        htmpl.set("blkNoErr.blkEffluentiPrimo.descComuneUtePrimo", uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")");
        htmpl.set("blkNoErr.blkEffluentiPrimo.indirizzoUtePrimo", uteVO.getIndirizzo()); 
        for(int j=0;j<vEffluenti.size();j++)
        {
          EffluenteVO effVO = vEffluenti.get(j);
          htmpl.newBlock("blkElencoEffluentiPrimo");
          htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.tipoEffluente", 
            effVO.getTipoEffluente());
          htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.descrizione", 
            effVO.getDescrizione());
            
          BigDecimal refluoUtilizzoAgronomico = new BigDecimal(0);
          if(Validator.isNotEmpty(effVO.getIdTrattamento()))
          {
            if(Validator.isNotEmpty(effVO.getVolumePostDichiarato()))
            {
              refluoUtilizzoAgronomico = refluoUtilizzoAgronomico.add(effVO.getVolumePostDichiarato());
            }
            
            if(Validator.isNotEmpty(effVO.getVolumeAcquisizioneStoccato()))
            {
              refluoUtilizzoAgronomico = refluoUtilizzoAgronomico.add(effVO.getVolumeAcquisizioneStoccato());
            }
            if(Validator.isNotEmpty(effVO.getVolumeCessioneNoStoccato()))
            {
              refluoUtilizzoAgronomico = refluoUtilizzoAgronomico.subtract(effVO.getVolumeCessioneNoStoccato());
            }
          
          }
          else
          {
            if(Validator.isNotEmpty(effVO.getVolumeIniziale()))
            {
              refluoUtilizzoAgronomico = refluoUtilizzoAgronomico.add(effVO.getVolumeIniziale());
            }
            if(Validator.isNotEmpty(effVO.getVolumeAcquisizioneStoccato()))
            {
              refluoUtilizzoAgronomico = refluoUtilizzoAgronomico.add(effVO.getVolumeAcquisizioneStoccato());
            }
            if(Validator.isNotEmpty(effVO.getVolumeCessioneNoStoccato()))
            {
              refluoUtilizzoAgronomico = refluoUtilizzoAgronomico.subtract(effVO.getVolumeCessioneNoStoccato());
            }          
            if(Validator.isNotEmpty(effVO.getVolumeTrattato()))
            {
              refluoUtilizzoAgronomico = refluoUtilizzoAgronomico.subtract(effVO.getVolumeTrattato());
            }         
          }
          
          if(refluoUtilizzoAgronomico.compareTo(new BigDecimal(0)) < 0)
          {
            refluoUtilizzoAgronomico = new BigDecimal(0);
          }
          
          htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.nettoCessioni", 
            Formatter.formatDouble1(refluoUtilizzoAgronomico));
          
          if(effVO.getTipoEffluente().equals("Palabile"))
          {
            //Totali aziendali
            totStocNecPalabile = totStocNecPalabile.add(effVO.getStocNecVol());
            totStocNettoPalabile = totStocNettoPalabile.add(refluoUtilizzoAgronomico);
            //*****************************    
            
            
            if((intPalabile > 0) && flagPrimoPalabile)
            {
              flagPrimoPalabile = false;
              htmpl.newBlock("blkRigaPrimo");
              htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkRigaPrimo.dimRighe", 
                ""+intPalabile);
              htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkRigaPrimo.stoccaggioDiponibileVol",
                Formatter.formatDouble1(com10.getStocDispPalabileVol()));
                
              totStoccaggioDiponibileVol = totStoccaggioDiponibileVol.add(com10.getStocDispPalabileVol()); 
            }
          }
          else
          {
            //Totali aziendali
            totStocNecNonPalabile = totStocNecNonPalabile.add(effVO.getStocNecVol());
            totStocNettoNonPalabile = totStocNettoNonPalabile.add(refluoUtilizzoAgronomico);
            //************************************************************
            
                    
            if((intNonPalabile > 0) && flagPrimoNonPalabile)
            {
              intNonPalabile++; //incremento perchè aggiungo riga altre acque
              flagPrimoNonPalabile = false;
              htmpl.newBlock("blkRigaPrimo");
              htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkRigaPrimo.dimRighe", 
                ""+intNonPalabile);
              htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkRigaPrimo.stoccaggioDiponibileVol",
                Formatter.formatDouble1(com10.getStocDispNonPalabileVol()));
                
              totStoccaggioDiponibileVol = totStoccaggioDiponibileVol.add(com10.getStocDispNonPalabileVol()); 
            }
          }
             
          
          
          htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.stocNecVol", 
            Formatter.formatDouble1(effVO.getStocNecVol()));          
          totStocNecVol = totStocNecVol.add(effVO.getStocNecVol());
          
          htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.stocNecGG", 
            Formatter.formatAndRoundBigDecimal0(effVO.getStocNecGg()));
            
            
          //Totali
          //Ultimo (j == (intPalabile -1)) dei palabili aggiungo il totale
          //Ultimo (ultimo record: j == (vEffluenti.size() - 1) dei non palabili aggiungo il totale
          if((intPalabile > 0 ) && (j == (intPalabile -1)))
          {
            htmpl.newBlock("blkTotaleEffluentiPrimo");
            if(totStoccaggioDiponibileVol.compareTo(totStocNecVol) < 0)
            {
              htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkTotaleEffluentiPrimo.coloreTotPrimo", 
                  "tot red"); 
            }
            else
            {
              htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkTotaleEffluentiPrimo.coloreTotPrimo", 
                  "tot"); 
            }
          
          
            htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkTotaleEffluentiPrimo.totStoccaggioDiponibileVol", 
                Formatter.formatDouble1(totStoccaggioDiponibileVol));
                
            htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkTotaleEffluentiPrimo.totStocNecVol", 
                Formatter.formatDouble1(totStocNecVol));
                
            totStoccaggioDiponibileVol = new BigDecimal(0);
            totStocNecVol = new BigDecimal(0);
            
          } 
          
            
            
          if(j == (vEffluenti.size() - 1))
          {
            htmpl.newBlock("blkElencoEffluentiPrimo");
            htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.tipoEffluente", 
              "Non palabile");
            htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.descrizione", 
              "Altro (acque...)");
                   
            htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.nettoCessioni", 
              Formatter.formatAndRoundBigDecimal1(com10.getStocAcqueNettoCessione()));
              
            if(Validator.isNotEmpty(com10.getStocAcqueNettoCessione())) 
              totStocNettoNonPalabile = totStocNettoNonPalabile.add(com10.getStocAcqueNettoCessione());
              
            if(intNonPalabile == 0)
            {
              htmpl.newBlock("blkRigaPrimo");
              htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkRigaPrimo.dimRighe", 
                "1");
              htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkRigaPrimo.stoccaggioDiponibileVol",
                Formatter.formatDouble1(com10.getStocDispNonPalabileVol()));
                
              totStoccaggioDiponibileVol = totStoccaggioDiponibileVol.add(com10.getStocDispNonPalabileVol()); 
            }
            
            
            htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.stocNecVol", 
              Formatter.formatAndRoundBigDecimal1(com10.getStocAcqueNecVol()));
            htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.stocNecGG", 
              Formatter.formatAndRoundBigDecimal0(com10.getStocAcqueNecGG()));
              
              
            //Nuova Aggiunta idea Sergio
            totStocNecVol = totStocNecVol.add(com10.getStocAcqueNecVol());  
              
            //Totali
            htmpl.newBlock("blkTotaleEffluentiPrimo");
            if(totStoccaggioDiponibileVol.compareTo(totStocNecVol) < 0)
            {
              htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkTotaleEffluentiPrimo.coloreTotPrimo", 
                  "tot red"); 
            }
            else
            {
              htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkTotaleEffluentiPrimo.coloreTotPrimo", 
                  "tot"); 
            }
          
          
            htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkTotaleEffluentiPrimo.totStoccaggioDiponibileVol", 
                Formatter.formatDouble1(totStoccaggioDiponibileVol));
                
            htmpl.set("blkNoErr.blkEffluentiPrimo.blkElencoEffluentiPrimo.blkTotaleEffluentiPrimo.totStocNecVol", 
                Formatter.formatDouble1(totStocNecVol));
                
            totStoccaggioDiponibileVol = new BigDecimal(0);
            totStocNecVol = new BigDecimal(0);    
          
          }              
        }
      }
        
      Vector<RefluoEffluenteVO> vReflui = (Vector<RefluoEffluenteVO>)hCom10R.get("vReflui");
      if(Validator.isNotEmpty(vReflui))
      {       
        htmpl.newBlock("blkNoErr.blkReflui");
        htmpl.set("blkNoErr.blkReflui.descComuneUteSecondo", uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")");
        htmpl.set("blkNoErr.blkReflui.indirizzoUteSecondo", uteVO.getIndirizzo());
        BigDecimal totVolumeEffProdStalla = new BigDecimal(0);
        BigDecimal totAzotoEffProdStalla = new BigDecimal(0);
        BigDecimal totVolumeAcquisito = new BigDecimal(0);
        BigDecimal totAzotoAcquisito = new BigDecimal(0);
        BigDecimal totVolumePostTrattamentoCalc = new BigDecimal(0);
        BigDecimal totAzotoPostTrattamentoCalc = new BigDecimal(0);
        BigDecimal totVolumePostTrattamentoDich = new BigDecimal(0);
        BigDecimal totAzotoPostTrattamentoDich = new BigDecimal(0);
        BigDecimal totVolumeCeduto = new BigDecimal(0);
        BigDecimal totAzotoCeduto = new BigDecimal(0);
        BigDecimal totVolumeUtilizzoAgronomico = new BigDecimal(0);
        BigDecimal totAzotoUtilizzoAgronomico = new BigDecimal(0); 
               
        for(int j=0;j<vReflui.size();j++)
        {
          RefluoEffluenteVO refluoVO = (RefluoEffluenteVO)vReflui.get(j);
          if(!((refluoVO.getVolumeEffProdStalla().compareTo(BigDecimal.ZERO) == 0)
            && (refluoVO.getAzotoEffProdStalla().compareTo(BigDecimal.ZERO) == 0)
            && (refluoVO.getVolumeAcquisito().compareTo(BigDecimal.ZERO) == 0)
            && (refluoVO.getAzotoAcquisito().compareTo(BigDecimal.ZERO) == 0)
            && (refluoVO.getVolumePostTrattamentoCalc().compareTo(BigDecimal.ZERO) == 0)
            && (refluoVO.getAzotoPostTrattamentoCalc().compareTo(BigDecimal.ZERO) == 0)
            && (refluoVO.getVolumePostTrattamentoDich().compareTo(BigDecimal.ZERO) == 0)
            && (refluoVO.getAzotoPostTrattamentoDich().compareTo(BigDecimal.ZERO) == 0)
            && (refluoVO.getVolumeCeduto().compareTo(BigDecimal.ZERO) == 0)
            && (refluoVO.getAzotoCeduto().compareTo(BigDecimal.ZERO) == 0)
            && (refluoVO.getVolumeUtilizzoAgronomico().compareTo(BigDecimal.ZERO) == 0)
            && (refluoVO.getAzotoUtilizzoAgronomico().compareTo(BigDecimal.ZERO) == 0)))
          {
          
	          htmpl.newBlock("blkNoErr.blkReflui.blkElencoReflui");
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.tipoEffluente", 
	            refluoVO.getTipoEffluente());
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.descrizione", 
	            refluoVO.getDescrizione());
	            
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.volumeEffProdStalla", 
	            Formatter.formatDouble1(refluoVO.getVolumeEffProdStalla()));
	          totVolumeEffProdStalla = totVolumeEffProdStalla.add(refluoVO.getVolumeEffProdStalla());
	          
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.azotoEffProdStalla", 
	            Formatter.formatAndRoundBigDecimal0(refluoVO.getAzotoEffProdStalla()));
	          totAzotoEffProdStalla = totAzotoEffProdStalla.add(Formatter.roundBigDecimal0(
	            refluoVO.getAzotoEffProdStalla()));
	            
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.volumeAcquisito", 
	            Formatter.formatDouble1(refluoVO.getVolumeAcquisito()));
	          totVolumeAcquisito = totVolumeAcquisito.add(refluoVO.getVolumeAcquisito());
	          
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.azotoAcquisito", 
	            Formatter.formatAndRoundBigDecimal0(refluoVO.getAzotoAcquisito()));
	          totAzotoAcquisito = totAzotoAcquisito.add(Formatter.roundBigDecimal0(
	            refluoVO.getAzotoAcquisito()));
	            
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.volumePostTrattamentoCalc", 
	            Formatter.formatDouble1(refluoVO.getVolumePostTrattamentoCalc()));
	          totVolumePostTrattamentoCalc = totVolumePostTrattamentoCalc.add(refluoVO.getVolumePostTrattamentoCalc());
	          
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.azotoPostTrattamentoCalc", 
	            Formatter.formatAndRoundBigDecimal0(refluoVO.getAzotoPostTrattamentoCalc()));
	          totAzotoPostTrattamentoCalc = totAzotoPostTrattamentoCalc.add(Formatter.roundBigDecimal0(
	            refluoVO.getAzotoPostTrattamentoCalc()));
	            
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.volumePostTrattamentoDich", 
	            Formatter.formatDouble1(refluoVO.getVolumePostTrattamentoDich()));
	          totVolumePostTrattamentoDich = totVolumePostTrattamentoDich.add(refluoVO.getVolumePostTrattamentoDich());
	          
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.azotoPostTrattamentoDich", 
	            Formatter.formatAndRoundBigDecimal0(refluoVO.getAzotoPostTrattamentoDich()));
	          totAzotoPostTrattamentoDich = totAzotoPostTrattamentoDich.add(Formatter.roundBigDecimal0(
	            refluoVO.getAzotoPostTrattamentoDich()));
	            
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.volumeCeduto", 
	            Formatter.formatDouble1(refluoVO.getVolumeCeduto()));
	          totVolumeCeduto = totVolumeCeduto.add(refluoVO.getVolumeCeduto());
	          
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.azotoCeduto", 
	            Formatter.formatAndRoundBigDecimal0(refluoVO.getAzotoCeduto()));
	          totAzotoCeduto = totAzotoCeduto.add(Formatter.roundBigDecimal0(
	            refluoVO.getAzotoCeduto()));
	            
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.volumeUtilizzoAgronomico", 
	            Formatter.formatDouble1(refluoVO.getVolumeUtilizzoAgronomico()));
	          totVolumeUtilizzoAgronomico = totVolumeUtilizzoAgronomico.add(refluoVO.getVolumeUtilizzoAgronomico());
	          
	          htmpl.set("blkNoErr.blkReflui.blkElencoReflui.azotoUtilizzoAgronomico", 
	            Formatter.formatAndRoundBigDecimal0(refluoVO.getAzotoUtilizzoAgronomico()));
	          totAzotoUtilizzoAgronomico = totAzotoUtilizzoAgronomico.add(Formatter.roundBigDecimal0(
	            refluoVO.getAzotoUtilizzoAgronomico()));
	            
	            
	        }
	      
        }
        
        htmpl.set("blkNoErr.blkReflui.totVolumeEffProdStalla", 
            Formatter.formatDouble1(totVolumeEffProdStalla));  
        htmpl.set("blkNoErr.blkReflui.totAzotoEffProdStalla", 
            Formatter.formatAndRoundBigDecimal0(totAzotoEffProdStalla));
        htmpl.set("blkNoErr.blkReflui.totVolumeAcquisito", 
            Formatter.formatDouble1(totVolumeAcquisito));  
        htmpl.set("blkNoErr.blkReflui.totAzotoAcquisito", 
            Formatter.formatAndRoundBigDecimal0(totAzotoAcquisito));
        htmpl.set("blkNoErr.blkReflui.totVolumePostTrattamentoCalc", 
            Formatter.formatDouble1(totVolumePostTrattamentoCalc));  
        htmpl.set("blkNoErr.blkReflui.totAzotoPostTrattamentoCalc", 
            Formatter.formatAndRoundBigDecimal0(totAzotoPostTrattamentoCalc));
        htmpl.set("blkNoErr.blkReflui.totVolumePostTrattamentoDich", 
            Formatter.formatDouble1(totVolumePostTrattamentoDich));  
        htmpl.set("blkNoErr.blkReflui.totAzotoPostTrattamentoDich", 
            Formatter.formatAndRoundBigDecimal0(totAzotoPostTrattamentoDich));
        htmpl.set("blkNoErr.blkReflui.totVolumeCeduto", 
            Formatter.formatDouble1(totVolumeCeduto));  
        htmpl.set("blkNoErr.blkReflui.totAzotoCeduto", 
            Formatter.formatAndRoundBigDecimal0(totAzotoCeduto));
        htmpl.set("blkNoErr.blkReflui.totVolumeUtilizzoAgronomico", 
            Formatter.formatDouble1(totVolumeUtilizzoAgronomico));  
        htmpl.set("blkNoErr.blkReflui.totAzotoUtilizzoAgronomico", 
            Formatter.formatAndRoundBigDecimal0(totAzotoUtilizzoAgronomico));   
        
      
     
      } 
    
      Vector<EffluenteCesAcqVO> vAcquisizioni = (Vector)hCom10R.get("vAcquisizioni");
      if(vAcquisizioni != null)
      {
        if(flagPrimoCessAcqu)
        {
          htmpl.newBlock("blkCessAcqu");
          flagPrimoCessAcqu = false;
        }
        
        for(int j=0;j<vAcquisizioni.size();j++)
        {
          EffluenteCesAcqVO effVO = vAcquisizioni.get(j);          
          
          htmpl.newBlock("blkElencoCessAcqu");
          
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.cuaaCessAcqu", 
            effVO.getCuaa());
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.denominazioneCessAcqu", 
            effVO.getDenominazione());
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.comuneCessAcqu", 
            effVO.getDescComune());
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.siglaProvCessAcqu", 
            effVO.getSglProv());            
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.descComuneUteCessAcqu", 
            uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")"
            +" - "+uteVO.getIndirizzo());
            
          if(effVO.isFlagStoccaggioBl())
          {
            htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.flagStoccaggioCessAcqu", "SI"); 
          }
          else
          {
            htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.flagStoccaggioCessAcqu", "NO"); 
          }
                             
          //htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.descrizioneCausaleEffluente",
            //effVO.getDescrizione());
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.descrizioneEffluente", 
            effVO.getDescTipoEffluente()); 
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.quantitaCessAcqu", 
            Formatter.formatAndRoundBigDecimal1(effVO.getQuantita()));          
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.azotoCessAcqu", 
            Formatter.formatAndRoundBigDecimal0(effVO.getQuantitaAzoto()));
          htmpl.set("blkNoErr.blkCessAcqu.blkElencoCessAcqu.azotoDichiaratoCessAcqu", 
            Formatter.formatAndRoundBigDecimal0(effVO.getQuantitaAzotoDichiarato()));        
        }
      }
      
      Vector<EffluenteCesAcqVO> vCessioni = (Vector)hCom10R.get("vCessioni");
      if(vCessioni != null)
      {
        if(flagPrimoCessAcqu)
        {
          htmpl.newBlock("blkCessioni");
          flagPrimoCessAcqu = false;
        }
        
        for(int j=0;j<vCessioni.size();j++)
        {
          EffluenteCesAcqVO effVO = vCessioni.get(j);
          
                   
          htmpl.newBlock("blkElencoCessioni");
          
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.cuaaCessAcqu", 
            effVO.getCuaa());
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.denominazioneCessAcqu", 
            effVO.getDenominazione());
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.comuneCessAcqu", 
            effVO.getDescComune());
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.siglaProvCessAcqu", 
            effVO.getSglProv());            
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.descComuneUteCessAcqu", 
            uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")"
            +" - "+uteVO.getIndirizzo());
            
          if(effVO.isFlagStoccaggioBl())
          {
            htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.flagStoccaggioCessAcqu", "SI"); 
          }
          else
          {
            htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.flagStoccaggioCessAcqu", "NO"); 
          }
                             
          //htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.descrizioneCausaleEffluente",
            //effVO.getDescrizione());
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.descrizioneEffluente", 
            effVO.getDescTipoEffluente()); 
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.quantitaCessAcqu", 
            Formatter.formatAndRoundBigDecimal1(effVO.getQuantita()));          
          htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.azotoCessAcqu", 
            Formatter.formatAndRoundBigDecimal0(effVO.getQuantitaAzoto()));
          //htmpl.set("blkNoErr.blkCessioni.blkElencoCessioni.azotoDichiaratoCessAcqu", 
            //Formatter.formatAndRoundBigDecimal0(effVO.getQuantitaAzotoDichiarato()));        
        }
      }
      
      
      Vector<EffluenteVO> vEffluentiTratt = (Vector<EffluenteVO>)hCom10R.get("vEffluentiTratt");
      if(vEffluentiTratt != null)
      {
        htmpl.newBlock("blkTrattamenti");
        for(int j=0;j<vEffluentiTratt.size();j++)
        {
          EffluenteVO effVO = vEffluentiTratt.get(j);
          htmpl.newBlock("blkElencoTrattamenti");         
          
          UteVO uteTrattVO = (UteVO)hElencoUte.get(new Long(effVO.getIdUte()));
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.descUteTrattamenti", uteVO.getComuneUte().getDescom()
            +" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+") - "+uteVO.getIndirizzo()); 
          
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.descrizioneEffluente", effVO.getDescrizione());
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.tipologiaEffluenteTratt", effVO.getTipoEffluente());
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.codiceTrattamento", effVO.getCodiceTrattamento());
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaTratt", Formatter.formatDouble1(effVO.getVolumeIniziale()));
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoTratt", Formatter.formatDouble1(effVO.getAzotoIniziale()));
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaPalDichTratt", Formatter.formatDouble1(effVO.getVolumePostDichiarato()));
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoPalDichTratt", Formatter.formatDouble1(effVO.getAzotoPostDichiarato()));
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.quantitaNonPalDichTratt", Formatter.formatDouble1(effVO.getVolumePostDichiaratoNonPal()));
          htmpl.set("blkNoErr.blkTrattamenti.blkElencoTrattamenti.azotoNonPalDichTratt", Formatter.formatDouble1(effVO.getAzotoPostDichiaratoNonPal()));
              
        }
      }
      
      
      
    
      Vector vStoccaggio = (Vector)hCom10R.get("vStoccaggio");
      if(vStoccaggio != null)
      {
        if(flagPrimoStocc)
        {
          htmpl.newBlock("blkStocc");
          flagPrimoStocc = false;
        }
        
        for(int j=0;j<vStoccaggio.size();j++)
        {
          EffluenteStocExtVO effVO = (EffluenteStocExtVO)vStoccaggio.get(j);
          htmpl.newBlock("blkElencoStocc");
           
          htmpl.set("blkNoErr.blkStocc.blkElencoStocc.cuaaStocc", 
            effVO.getCuaa());
          htmpl.set("blkNoErr.blkStocc.blkElencoStocc.denominazioneStocc", 
            effVO.getDenominazione());
          htmpl.set("blkNoErr.blkStocc.blkElencoStocc.comuneStocc", 
            effVO.getDescComune());
          htmpl.set("blkNoErr.blkStocc.blkElencoStocc.siglaProvStocc", 
            effVO.getSglProv());            
          htmpl.set("blkNoErr.blkStocc.blkElencoStocc.descComuneUteStocc", 
            uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")"
            +" - "+uteVO.getIndirizzo());     
          htmpl.set("blkNoErr.blkStocc.blkElencoStocc.descrizioneFabbricato", 
            effVO.getDescrizione());
          htmpl.set("blkNoErr.blkStocc.blkElencoStocc.quantitaStocc", 
            Formatter.formatAndRoundBigDecimal1(effVO.getQuantita()));
              
        }
      }
      
      if(Validator.isNotEmpty(com10.getVolumeSottogrigliato()))
      {
        if(flagPrimoVolumeSottogrigliato)
        {
          htmpl.newBlock("blkVolumeSottogrigliato");
          flagPrimoVolumeSottogrigliato = false;
        }
        htmpl.set("blkNoErr.blkVolumeSottogrigliato.blkElencoVolumeSottogrigliato.descComuneUteVolumeSottogrigliato", 
              uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")"
              +" - "+uteVO.getIndirizzo()); 
        htmpl.set("blkNoErr.blkVolumeSottogrigliato.blkElencoVolumeSottogrigliato.volumeSottoGrigliato", 
          Formatter.formatAndRoundBigDecimal1(com10.getVolumeSottogrigliato()));
      }
        
      if(Validator.isNotEmpty(com10.getVolumeRefluoAzienda()))
      {  
        if(flagPrimoVolumeRefluoAzienda)
        {
          htmpl.newBlock("blkVolumeRefluoAzienda");
          flagPrimoVolumeRefluoAzienda = false;
        }
        htmpl.set("blkNoErr.blkVolumeRefluoAzienda.blkElencoVolumeRefluoAzienda.descComuneUteVolumeRefluoAzienda", 
              uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")"
              +" - "+uteVO.getIndirizzo()); 
        htmpl.set("blkNoErr.blkVolumeRefluoAzienda.blkElencoVolumeRefluoAzienda.volumeRefluoAzienda", 
          Formatter.formatAndRoundBigDecimal1(com10.getVolumeRefluoAzienda()));
      }
    
      Vector vAcqueReflue = (Vector)hCom10R.get("vAcqueReflue");
      if(vAcqueReflue != null)
      {
        if(flagPrimoAcqueReflue)
        {
          htmpl.newBlock("blkAcqueReflue");
          flagPrimoAcqueReflue = false;
        }
        
        for(int j=0;j<vAcqueReflue.size();j++)
        {
          AcquaExtraVO acquVO = (AcquaExtraVO)vAcqueReflue.get(j);
          
          
          htmpl.newBlock("blkElencoAcqueReflue");
          htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.descComuneUteAcqueReflue", 
            uteVO.getComuneUte().getDescom()+" ("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")"
            +" - "+uteVO.getIndirizzo()); 
          htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.descrizioneAcquaAgronomica", 
            acquVO.getDescrizione());
          htmpl.set("blkNoErr.blkAcqueReflue.blkElencoAcqueReflue.volumeRefluo", 
            Formatter.formatDouble1(acquVO.getVolumeRefluo()));        
        }
      }
      
    }
    
    String aziendaZVN = "No";
    BigDecimal totAziendaZVN = new BigDecimal(0);
    if(totSupSauPiemonte.compareTo(new BigDecimal(0)) > 0)
    {
      totSupSauPiemonteZVN = totSupSauPiemonteZVN.divide(totSupSauPiemonte,4,BigDecimal.ROUND_HALF_UP);
      totAziendaZVN = totSupSauPiemonteZVN.multiply(new BigDecimal(100));
      if(totAziendaZVN.compareTo(new BigDecimal(25)) >= 0)
      {
         aziendaZVN = "Si";
      }
    }
    htmpl.set("blkNoErr.aziendaZVN", aziendaZVN);
    
    //Visualizzazione totali
    htmpl.set("blkNoErr.totConduzioneZVN", Formatter.formatDouble4(totConduzioneZVN));
    htmpl.set("blkNoErr.totConduzioneNoZVN", Formatter.formatDouble4(totConduzioneNoZVN));
    htmpl.set("blkNoErr.totAsservimentoZVN", Formatter.formatDouble4(totAsservimentoZVN));
    htmpl.set("blkNoErr.totAsservimentoNoZVN", Formatter.formatDouble4(totAsservimentoNoZVN));    
    htmpl.set("blkNoErr.totRicettivitaAzoto", Formatter.formatAndRoundBigDecimal0(totRicettivitaAzoto));
    htmpl.set("blkNoErr.totAzotoEscretoPascolo", Formatter.formatAndRoundBigDecimal0(totAzotoEscretoPascolo));    
    htmpl.set("blkNoErr.totAzotoZooAziendale", Formatter.formatAndRoundBigDecimal0(totAzotoZooAziendale));   
    htmpl.set("blkNoErr.totAzotoNettCessioni", Formatter.formatAndRoundBigDecimal0(totAzotoNettCessioni));
    
    
    if(totAzotoNettCessioni.compareTo(totRicettivitaAzoto) > 0 )
      htmpl.set("blkNoErr.coloreSommeTotale", "tot red");
    else
      htmpl.set("blkNoErr.coloreSommeTotale", "tot");
      
    htmpl.set("blkNoErr.totStocNettoPalabile", Formatter.formatDouble1(totStocNettoPalabile));
    htmpl.set("blkNoErr.totStocDispPalabile", Formatter.formatDouble1(totStocDispPalabile));
    htmpl.set("blkNoErr.totStocNecPalabile", Formatter.formatDouble1(totStocNecPalabile));
    htmpl.set("blkNoErr.totStocNettoNonPalabile", Formatter.formatDouble1(totStocNettoNonPalabile));
    htmpl.set("blkNoErr.totStocDispNonPalabile", Formatter.formatDouble1(totStocDispNonPalabile));
    htmpl.set("blkNoErr.totStocNecNonPalabile", Formatter.formatDouble1(totStocNecNonPalabile));
    
  }
  
  

  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
  
%>
