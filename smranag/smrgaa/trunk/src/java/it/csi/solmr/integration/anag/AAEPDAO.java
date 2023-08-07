package it.csi.solmr.integration.anag;


import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.smrcomms.reportdin.util.Validator;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.TipoSezioniAaepVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.profile.AgriConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.integration.BaseDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.ws.infoc.Azienda;
import it.csi.solmr.ws.infoc.ProcConcors;
import it.csi.solmr.ws.infoc.RappresentanteLegale;
import it.csi.solmr.ws.infoc.Sede;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

 // <p>Title: S.O.L.M.R.</p>
 // <p>Description: Servizi On-Line per il Mondo Rurale</p>
 // <p>Copyright: Copyright (c) 2003</p>
 // <p>Company: TOBECONFIG</p>
 // @author
 // @version 1.0

public class AAEPDAO extends BaseDAO
{

  public AAEPDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public AAEPDAO(String refName) throws ResourceAccessException {
    super(refName);
  }

  public void aggiornaDatiAAEP(Azienda aziendaAAEP, RappresentanteLegale rappresentanteLegaleAAEP, 
      Sede sedeAAEP[], List<ProcConcors> procConcorsInfoc, String cuaa)
      throws DataAccessException
  {
    Long primaryKey = null;
    Long idAziendaAAEP = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      idAziendaAAEP = getNextPrimaryKey((String)SolmrConstants.get("SEQ_AZIENDA_AAEP"));
      conn = getDatasource().getConnection();

      /**
       * Per prima cosa cancello tutti i dati, mi basta cancellarli dalla
       * tabella DB_AZIENDA_AAEP perchè le altre due sono legate
       * da una relazione DELETE_ON_CASCADE
       * */
      String delete="DELETE FROM DB_AZIENDA_AAEP WHERE CUAA = ?";

      stmt = conn.prepareStatement(delete);
      stmt.setString(1, cuaa);
      SolmrLogger.debug(this, "Executing aggiornaDatiAAEP:delete: "+delete);
      stmt.executeUpdate();
      stmt.close();
      SolmrLogger.debug(this, "Executing aggiornaDatiAAEP:delete:");



      String insertAziendaAAEP = "INSERT INTO DB_AZIENDA_AAEP "+
                     "(ID_AZIENDA_AAEP, ID_NATURA_GIURIDICA, DESCRIZIONE_NATURA_GIURIDICA, "+
                     " ID_ATECO, DESCRIZIONE_ATECO, PARTITA_IVA, NUMERO_CCIAA, "+
                     " ANNO_CCIAA, PROVINCIA_CCIAA, DENOMINAZIONE, OGGETTO_SOCIALE, "+
                     " NUM_REGISTRO_IMPRESE, CODICE_CAUSALE_CESSAZIONE, "+
                     " DESCRIZIONE_CAUSALE_CESSAZIONE, DATA_COSTITUZIONE, "+
                     " DATA_CESSAZIONE, DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, "+
                     " FLAG_PRESENTE_IN_AAEP, DATA_AGGIORNAMENTO, CUAA) "+
                     "VALUES"+
                     "(?,"+       //ID_AZIENDA_AAEP                    1
                      "?,"+       //ID_NATURA_GIURIDICA                2
                      "?,"+       //DESCRIZIONE_NATURA_GIURIDICA       3
                      "?,"+       //ID_ATECO                           4
                      "?,"+       //DESCRIZIONE_ATECO                  5
                      "?,"+       //PARTITA_IVA                        6
                      "?,"+       //NUMERO_CCIAA                       7
                      "?,"+       //ANNO_CCIAA                         8
                      "?,"+       //PROVINCIA_CCIAA                    9
                      "?,"+       //DENOMINAZIONE                     10
                      "?,"+       //OGGETTO_SOCIALE                   11
                      "?,"+       //NUM_REGISTRO_IMPRESE              12
                      "?,"+       //CODICE_CAUSALE_CESSAZIONE         13
                      "?,"+       //DESCRIZIONE_CAUSALE_CESSAZIONE    14
                      "?,"+       //DATA_COSTITUZIONE                 15
                      "?,"+       //DATA_CESSAZIONE                   16
                      "?,"+       //DATA_INIZIO_VALIDITA              17
                      "?,"+       //DATA_FINE_VALIDITA                18
                      "'S',"+     //FLAG_PRESENZA_IN_AAEP
                      "SYSDATE,"+ //DATA_AGGIORNAMENTO
                      "?)";        //CUAA                              19

      stmt = conn.prepareStatement(insertAziendaAAEP);

      if (aziendaAAEP.getIdNaturaGiuridica()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getIdNaturaGiuridica "+aziendaAAEP.getIdNaturaGiuridica().getValue().length());
      if (aziendaAAEP.getDescrizioneNaturaGiuridica()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getDescrizioneNaturaGiuridica "+aziendaAAEP.getDescrizioneNaturaGiuridica().getValue().length());
      if (aziendaAAEP.getCodATECO2007()!=null)
      {
        String codAteco = aziendaAAEP.getCodATECO2007().getValue();
        codAteco = codAteco.replaceAll("\\.", "");
        SolmrLogger.debug(this, "\n\nLunghezza getCodATECO2007 "+codAteco.length());
      }  
      if (aziendaAAEP.getDescrATECO2007()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getDescrATECO2007 "+aziendaAAEP.getDescrATECO2007().getValue().length());
      if (aziendaAAEP.getPartitaIva()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getPartitaIva "+aziendaAAEP.getPartitaIva().getValue().length());
      if (aziendaAAEP.getNumeroCCIAA()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getNumeroCCIAA "+aziendaAAEP.getNumeroCCIAA().getValue().length());
      if (aziendaAAEP.getAnnoCCIAA()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getAnnoCCIAA "+aziendaAAEP.getAnnoCCIAA().getValue().length());
      if (aziendaAAEP.getProvinciaCCIAA()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getProvinciaCCIAA "+aziendaAAEP.getProvinciaCCIAA().getValue().length());
      if (aziendaAAEP.getRagioneSociale()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getRagioneSociale "+aziendaAAEP.getRagioneSociale().getValue().length());
      if (aziendaAAEP.getOggettoSociale()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getOggettoSociale "+aziendaAAEP.getOggettoSociale().getValue().length());
      if (aziendaAAEP.getNRegistroImpreseCCIAA()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getNRegistroImpreseCCIAA "+aziendaAAEP.getNRegistroImpreseCCIAA().getValue().length());
      if (aziendaAAEP.getCodiceCausaleCessazione()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getCodiceCausaleCessazione "+aziendaAAEP.getCodiceCausaleCessazione().getValue().length());
      if (aziendaAAEP.getDescrizioneCausaleCessazione()!=null)
        SolmrLogger.debug(this, "\n\nLunghezza getDescrizioneCausaleCessazione "+aziendaAAEP.getDescrizioneCausaleCessazione().getValue().length());
      if (cuaa!=null)
        SolmrLogger.debug(this, "\n\nLunghezza CUAA "+cuaa.length());

      stmt.setLong(1, idAziendaAAEP.longValue()); //ID_AZIENDA_AAEP
      stmt.setString(2, aziendaAAEP.getIdNaturaGiuridica().getValue()); //ID_NATURA_GIURIDICA
      stmt.setString(3, aziendaAAEP.getDescrizioneNaturaGiuridica().getValue()); //DESCRIZIONE_NATURA_GIURIDICA
      if(Validator.isNotEmpty(aziendaAAEP.getCodATECO2007()))
      {
        String codAteco = aziendaAAEP.getCodATECO2007().getValue();
        codAteco = codAteco.replaceAll("\\.", "");
        stmt.setString(4, codAteco);  //ID_ATECO
      }
      else
      {
        stmt.setString(4, null);  //ID_ATECO
      }
      stmt.setString(5, aziendaAAEP.getDescrATECO2007().getValue()); //DESCRIZIONE_ATECO
      stmt.setString(6, aziendaAAEP.getPartitaIva().getValue());  //PARTITA_IVA
      stmt.setString(7, aziendaAAEP.getNumeroCCIAA().getValue());  //NUMERO_CCIAA
      stmt.setString(8, aziendaAAEP.getAnnoCCIAA().getValue()); //ANNO_CCIAA
      stmt.setString(9, aziendaAAEP.getProvinciaCCIAA().getValue()); //PROVINCIA_CCIAA
      stmt.setString(10, aziendaAAEP.getRagioneSociale().getValue()); //DENOMINAZIONE
      stmt.setString(11, aziendaAAEP.getOggettoSociale().getValue()); //OGGETTO_SOCIALE
      stmt.setString(12, aziendaAAEP.getNRegistroImpreseCCIAA().getValue()); //NUM_REGISTRO_IMPRESE
      stmt.setString(13, aziendaAAEP.getCodiceCausaleCessazione().getValue()); //CODICE_CAUSALE_CESSAZIONE
      stmt.setString(14, aziendaAAEP.getDescrizioneCausaleCessazione().getValue()); //DESCRIZIONE_CAUSALE_CESSAZIONE
      stmt.setDate(15, checkDate(DateUtils.parseDateNull(aziendaAAEP.getDataCostituzione().getValue()))); //DATA_COSTITUZIONE
      stmt.setDate(16, checkDate(DateUtils.parseDateNull(aziendaAAEP.getDataCessazione().getValue()))); //DATA_CESSAZIONE
      stmt.setDate(17, null); //DATA_INIZIO_VALIDITA
      stmt.setDate(18, null); //DATA_FINE_VALIDITA
      stmt.setString(19, cuaa); //CUAA

      SolmrLogger.debug(this, "Executing aggiornaDatiAAEP:insertAziendaAAEP"+insertAziendaAAEP);

      stmt.executeUpdate();
      stmt.close();
      SolmrLogger.debug(this, "Executed aggiornaDatiAAEP:insertAziendaAAEP");

      /**
       * Se ho trovato il rappresentante legale lo inserisco
       * */
      if (rappresentanteLegaleAAEP!=null)
      {
        primaryKey = getNextPrimaryKey((String)SolmrConstants.get("SEQ_RAPPRESENTANTE_LEGALE_AAEP"));
        String insertRappresentanteLegaleAAEP = "INSERT INTO DB_RAPPRESENTANTE_LEGALE_AAEP "+
                       "(ID_RAPPRESENTANTE_LEGALE_AAEP, DATA_INIZIO_VALIDITA, CODICE_FISCALE,"+
                       "COGNOME, NOME, SESSO, DATA_NASCITA, CODICE_COMUNE_NASCITA,"+
                       "NOME_COMUNE_NASCITA, CODICE_COMUNE_RESIDENZA, NOME_COMUNE_RESIDENZA,"+
                       "TOPONIMO, INDIRIZZO, NUMERO_CIVICO, CAP, INDIRIZZO_RAPPRESENTANTE, "+
                       "ID_AZIENDA_AAEP)"+
                       "VALUES"+
                       "(?,"+       //ID_RAPPRESENTANTE_LEGALE_AAEP    1
                        "?,"+       //DATA_INIZIO_VALIDITA             2
                        "?,"+       //CODICE_FISCALE                   3
                        "?,"+       //COGNOME                          4
                        "?,"+       //NOME                             5
                        "?,"+       //SESSO                            6
                        "?,"+       //DATA_NASCITA                     7
                        "?,"+       //CODICE_COMUNE_NASCITA            8
                        "?,"+       //NOME_COMUNE_NASCITA              9
                        "?,"+       //CODICE_COMUNE_RESIDENZA         10
                        "?,"+       //NOME_COMUNE_RESIDENZA           11
                        "?,"+       //TOPONIMO                        12
                        "?,"+       //INDIRIZZO                       13
                        "?,"+       //NUMERO_CIVICO                   14
                        "?,"+       //CAP                             15
                        "?,"+       //INDIRIZZO_RAPPRESENTANTE        16
                        "?)";       //ID_AZIENDA_AAEP                 17

        stmt = conn.prepareStatement(insertRappresentanteLegaleAAEP);
        stmt.setLong(1, primaryKey.longValue()); //ID_RAPPRESENTANTE_LEGALE_AAEP
        stmt.setDate(2, null); //DATA_INIZIO_VALIDITA
        stmt.setString(3,rappresentanteLegaleAAEP.getCodiceFiscale().getValue()); //CODICE_FISCALE
        stmt.setString(4,rappresentanteLegaleAAEP.getCognome().getValue()); //COGNOME
        stmt.setString(5,rappresentanteLegaleAAEP.getNome().getValue()); //NOME
        stmt.setString(6,rappresentanteLegaleAAEP.getSesso().getValue()); //SESSO
        stmt.setDate(7,checkDate(DateUtils.parseDateNull(rappresentanteLegaleAAEP.getDataNascita().getValue()))); //DATA_NASCITA
        stmt.setString(8,rappresentanteLegaleAAEP.getCodComuneNascita().getValue()); //CODICE_COMUNE_NASCITA
        stmt.setString(9,rappresentanteLegaleAAEP.getDescrComuneNascita().getValue()); //NOME_COMUNE_NASCITA
        stmt.setString(10,rappresentanteLegaleAAEP.getCodComuneResidenza().getValue()); //CODICE_COMUNE_RESIDENZA
        stmt.setString(11,rappresentanteLegaleAAEP.getDescrComuneResidenza().getValue()); //NOME_COMUNE_RESIDENZA
        stmt.setString(12,rappresentanteLegaleAAEP.getTipoVia().getValue()); //TOPONIMO
        stmt.setString(13,rappresentanteLegaleAAEP.getIndirizzo().getValue()); //INDIRIZZO
        stmt.setString(14,rappresentanteLegaleAAEP.getNumeroCivico().getValue()); //NUMERO_CIVICO
        stmt.setString(15,rappresentanteLegaleAAEP.getCap().getValue()); //CAP
        stmt.setString(16,rappresentanteLegaleAAEP.getIndirizzo().getValue()); //INDIRIZZO_RAPPRESENTANTE
        stmt.setLong(17, idAziendaAAEP.longValue()); //ID_AZIENDA_AAEP

        SolmrLogger.debug(this, "Executing aggiornaDatiAAEP:insertRappresentanteLegaleAAEP"+insertRappresentanteLegaleAAEP);
        stmt.executeUpdate();
        stmt.close();
        SolmrLogger.debug(this, "Executed aggiornaDatiAAEP:insertRappresentanteLegaleAAEP");
      }

      /**
       * Se ho trovato una o più sedi le inserisco
       * */
      if (sedeAAEP!=null && sedeAAEP.length!=0)
      {
        for (int i=0;i<sedeAAEP.length;i++)
        {
          if(sedeAAEP[i] != null){	
	          primaryKey = getNextPrimaryKey((String)SolmrConstants.get("SEQ_SEDE_AAEP"));
	          String insertSedeAAEP = "INSERT INTO DB_SEDE_AAEP "+
	                         "(ID_SEDE_AAEP, ID_AZIENDA_AAEP, DATA_INIZIO_VALIDITA, "+
	                         "ID_TIPO_SEDE, DESCRIZIONE_TIPO_SEDE, DENOMINAZIONE_SEDE, "+
	                         "DESCRIZIONE_ATTIVITA_SEDE, SIGLA_PROVINCIA, COD_COMUNE, "+
	                         "NOME_COMUNE, TOPONIMO, INDIRIZZO, NUMERO_CIVICO, CAP, "+
	                         "INDIRIZZO_SEDE, CODICE_STATO_ESTERO, DESCRIZIONE_STATO_ESTERO, "+
	                         "COD_CAUSALE_CESSAZIONE, DESCRIZIONE_CAUSALE_CESSAZIONE, "+
	                         "DATA_INIZIO_ATTIVITA, DATA_FINE_ATTIVITA) "+
	                         "VALUES"+
	                         "(?,"+       //ID_SEDE_AAEP                     1
	                          "?,"+       //ID_AZIENDA_AAEP                  2
	                          "?,"+       //DATA_INIZIO_VALIDITA             3
	                          "?,"+       //ID_TIPO_SEDE                     4
	                          "?,"+       //DESCRIZIONE_TIPO_SEDE            5
	                          "?,"+       //DENOMINAZIONE_SEDE               6
	                          "?,"+       //DESCRIZIONE_ATTIVITA_SEDE        7
	                          "?,"+       //SIGLA_PROVINCIA                  8
	                          "?,"+       //COD_COMUNE                       9
	                          "?,"+       //NOME_COMUNE                     10
	                          "?,"+       //TOPONIMO                        11
	                          "?,"+       //INDIRIZZO                       12
	                          "?,"+       //NUMERO_CIVICO                   13
	                          "?,"+       //CAP                             14
	                          "?,"+       //INDIRIZZO_SEDE                  15
	                          "?,"+       //CODICE_STATO_ESTERO             16
	                          "?,"+       //DESCRIZIONE_STATO_ESTERO        17
	                          "?,"+       //COD_CAUSALE_CESSAZIONE          18
	                          "?,"+       //DESCRIZIONE_CAUSALE_CESSAZIONE  19
	                          "?,"+       //DATA_INIZIO_ATTIVITA            20
	                          "?)";       //DATA_FINE_ATTIVITA              21
	
	          stmt = conn.prepareStatement(insertSedeAAEP);
	          stmt.setLong(1, primaryKey.longValue());     //ID_SEDE_AAEP
	          stmt.setLong(2, idAziendaAAEP.longValue()); //ID_AZIENDA_AAEP
	          stmt.setDate(3,checkDate(DateUtils.convert(sedeAAEP[i].getDataInizioValidita().getValue()))); //DATA_INIZIO_VALIDITA
	          stmt.setString(4,sedeAAEP[i].getIdTipoSede().getValue()); //ID_TIPO_SEDE
	          stmt.setString(5,sedeAAEP[i].getDescrizioneTipoSede().getValue()); //DESCRIZIONE_TIPO_SEDE
	          stmt.setString(6,sedeAAEP[i].getDenominazioneSede().getValue()); //DENOMINAZIONE_SEDE
	          stmt.setString(7,sedeAAEP[i].getDescrizioneAttivitaSede().getValue()); //DESCRIZIONE_ATTIVITA_SEDE
	          stmt.setString(8,sedeAAEP[i].getSiglaProvincia().getValue()); //SIGLA_PROVINCIA
	          stmt.setString(9,sedeAAEP[i].getCodComune().getValue()); //COD_COMUNE
	          stmt.setString(10,sedeAAEP[i].getNomeComune().getValue()); //NOME_COMUNE
	          stmt.setString(11,sedeAAEP[i].getToponimo().getValue()); //TOPONIMO
	          stmt.setString(12,sedeAAEP[i].getIndirizzo().getValue()); //INDIRIZZO
	          stmt.setString(13,sedeAAEP[i].getNumeroCivico().getValue()); //NUMERO_CIVICO
	          stmt.setString(14,sedeAAEP[i].getCap().getValue()); //CAP
	          stmt.setString(15,sedeAAEP[i].getIndirizzoSede().getValue()); //INDIRIZZO_SEDE
	          stmt.setString(16,sedeAAEP[i].getCodiceStatoEstero().getValue()); //CODICE_STATO_ESTERO
	          stmt.setString(17,sedeAAEP[i].getDescrStatoEstero().getValue()); //DESCRIZIONE_STATO_ESTERO
	          stmt.setString(18,sedeAAEP[i].getCodCausaleCessazione().getValue()); //COD_CAUSALE_CESSAZIONE
	          stmt.setString(19,sedeAAEP[i].getDescrizioneCausaleCessazione().getValue()); //DESCRIZIONE_CAUSALE_CESSAZIONE
	          stmt.setDate(20,checkDate(DateUtils.convert(sedeAAEP[i].getDataInizioAttivita().getValue()))); //DATA_INIZIO_ATTIVITA
	          stmt.setDate(21,checkDate(DateUtils.convert(sedeAAEP[i].getDataFineAttivita().getValue()))); //DATA_FINE_ATTIVITA
	
	          SolmrLogger.debug(this, "query aggiornaDatiAAEP:insertSedeAAEP"+insertSedeAAEP);
	          stmt.executeUpdate();
	          stmt.close();
	          SolmrLogger.debug(this, "eseguito INSERT INTO DB_SEDE_AAEP");
          }  
        }
      }
      
      
      
    //Se ho trovato uno o più proccedure concorsi
      if (procConcorsInfoc!=null && procConcorsInfoc.size()!=0)
      {
        for (int i=0;i<procConcorsInfoc.size();i++)
        {
          primaryKey = getNextPrimaryKey(SolmrConstants.SEQ_DB_PROC_CONCORSUALE_AAEP);
          String insertProcConcAAEP = "INSERT INTO DB_PROCEDURA_CONCORSUALE_AAEP "+
                         "(ID_PROCEDURA_CONCORSUALE_AAEP, PROGRESSIVO_LIQUIDAZIONE, ID_AZIENDA_AAEP, " +
                         "ID_FONTE_AAEP, TIPO_LIQUIDAZIONE, DATA_APERTURA, "+
                         "DATA_FINE_LIQUIDAZIONE, DATA_REVOCA, DATA_CHIUSURA, "+
                         "CODICE_ATTO, DESCRIZIONE_ATTO, ESECUTORE_ATTO, "+
                         "NOTAIO, TRIBUNALE, ALTRE_INDICAZIONI, NUMERO_REGISTRAZIONE_ATTO, "+
                         "DATA_REGISTRAZIONE_ATTO, LOCALITA_REGISTRAZIONE_ATTO, PROVINCIA_REGISTRAZIONE_ATTO, "+
                         "DATA_ESECUZIONE_CONCORDATO, DATA_ULTIMO_AGGIORNAMENTO) "+
                         "VALUES"+
                         "(?,"+      //ID_PROCEDURA_CONCORSUALE_AAEP    1
                         "?,"+       //PROGRESSIVO_LIQUIDAZIONE         2 
                         "?,"+       //ID_AZIENDA_AAEP                  3
                         "?,"+       //ID_FONTE_AAEP                    4
                         "?,"+       //TIPO_LIQUIDAZIONE                5
                         "?,"+       //DATA_APERTURA                    6
                         "?,"+       //DATA_FINE_LIQUIDAZIONE           7
                         "?,"+       //DATA_REVOCA                      8
                         "?,"+       //DATA_CHIUSURA                    9
                         "?,"+       //CODICE_ATTO                      10
                         "?,"+       //DESCRIZIONE_ATTO                 11
                         "?,"+       //ESECUTORE_ATTO                   12
                         "?,"+       //NOTAIO                           13
                         "?,"+       //TRIBUNALE                        14
                         "?,"+       //ALTRE_INDICAZIONI                15
                         "?,"+       //NUMERO_REGISTRAZIONE_ATTO        16
                         "?,"+       //DATA_REGISTRAZIONE_ATTO          17
                         "?,"+       //LOCALITA_REGISTRAZIONE_ATTO      18
                         "?,"+       //PROVINCIA_REGISTRAZIONE_ATTO     19
                         "?,"+       //DATA_ESECUZIONE_CONCORDATO       20
                         "SYSDATE)"; //DATA_ULTIMO_AGGIORNAMENTO        21

          stmt = conn.prepareStatement(insertProcConcAAEP);
          stmt.setLong(1, primaryKey.longValue());     //ID_SEDE_AAEP
          stmt.setLong(2, procConcorsInfoc.get(i).getProgrLiquidazione()); //PROGRESSIVO_LIQUIDAZIONE
          stmt.setLong(3, idAziendaAAEP.longValue()); //ID_AZIENDA_AAEP
          //stmt.setLong(4, procConcorsInfoc.get(i).getIdAAEPFonteDato()); //ID_FONTE_AAEP
          stmt.setLong(4, new Long(SolmrConstants.FONTE_DATO_INFOCAMERE_STR)); //ID_FONTE_AAEP
          stmt.setString(5, procConcorsInfoc.get(i).getCodLiquidazione().getValue()); //TIPO_LIQUIDAZIONE
          stmt.setTimestamp(6, convertDateToTimestamp(DateUtils.convert(procConcorsInfoc.get(i).getDataAperturaProc().getValue()))); //DATA_APERTURA
          stmt.setTimestamp(7, convertDateToTimestamp(DateUtils.convert(procConcorsInfoc.get(i).getDataFineLiquidaz().getValue()))); //DATA_FINE_LIQUIDAZIONE
          stmt.setTimestamp(8, convertDateToTimestamp(DateUtils.convert(procConcorsInfoc.get(i).getDataRevocalLiquidaz().getValue()))); //DATA_REVOCA
          stmt.setTimestamp(9, convertDateToTimestamp(DateUtils.convert(procConcorsInfoc.get(i).getDataChiusuraLiquidaz().getValue()))); //DATA_CHIUSURA
          stmt.setString(10, procConcorsInfoc.get(i).getCodAtto().getValue()); //CODICE_ATTO
          stmt.setString(11, procConcorsInfoc.get(i).getDescrCodAtto().getValue()); //DESCRIZIONE_ATTO
          stmt.setString(12, procConcorsInfoc.get(i).getDescIndicatEsecutAtto().getValue()); //ESECUTORE_ATTO
          stmt.setString(13, procConcorsInfoc.get(i).getDescrNotaio().getValue()); //NOTAIO
          stmt.setString(14, procConcorsInfoc.get(i).getDescrTribunale().getValue()); //TRIBUNALE
          stmt.setString(15, procConcorsInfoc.get(i).getDescrAltreIndicazioni().getValue()); //ALTRE_INDICAZIONI
          stmt.setString(16, procConcorsInfoc.get(i).getNumRestistrAtto().getValue()); //NUMERO_REGISTRAZIONE_ATTO
          stmt.setTimestamp(17, convertDateToTimestamp(DateUtils.convert(procConcorsInfoc.get(i).getDataRegistroAtto().getValue()))); //DATA_REGISTRAZIONE_ATTO
          stmt.setString(18, procConcorsInfoc.get(i).getLocalRegistroAtto().getValue()); //LOCALITA_REGISTRAZIONE_ATTO
          stmt.setString(19, procConcorsInfoc.get(i).getSiglaProvRegAtto().getValue()); //PROVINCIA_REGISTRAZIONE_ATTO
          stmt.setTimestamp(20, convertDateToTimestamp(DateUtils.convert(procConcorsInfoc.get(i).getDataEsecConcordPrevent().getValue()))); //DATA_ESECUZIONE_CONCORDATO

          SolmrLogger.debug(this, "Executing aggiornaDatiAAEP:insertProcConcAAEP"+insertProcConcAAEP);
          stmt.executeUpdate();
          stmt.close();
          SolmrLogger.info(this, "Executed aggiornaDatiAAEP:insertProcConcAAEP");
        }
      }



    }
    catch (SQLException exc)
    {
      /**
       * Se l'error code è uguale ad 1 significa che sono stati fatti degli
       * accessi contemporanei e quindi non è un errore in quanto è impossibile
       * che i dati restituiti da AAEP fra due richieste così vicine siano
       * cambiati
       */
      if (exc.getErrorCode()!=1)
      {
        SolmrLogger.fatal(this, "SQLException: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this, "SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Metodo utilizzato se su AAEP non è presente un'azienda con quel CUAA
   * oppure è presente, ma non è fonte Infocamere
   * */
  public void aggiornaDatiAAEP(String CUAA)
      throws DataAccessException
  {
    Long primaryKey = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      primaryKey = getNextPrimaryKey((String)SolmrConstants.get("SEQ_AZIENDA_AAEP"));
      conn = getDatasource().getConnection();

      /**
       * Per prima cosa cancello tutti i dati, mi basta cancellarli dalla
       * tabella DB_AZIENDA_AAEP perchè le altre due sono legate
       * da una relazione DELETE_ON_CASCADE
       * */
      String delete="DELETE FROM DB_AZIENDA_AAEP WHERE CUAA = ?";

      stmt = conn.prepareStatement(delete);
      stmt.setString(1, CUAA);
      SolmrLogger.debug(this, "Executing aggiornaDatiAAEP:delete: "+delete);
      stmt.executeUpdate();
      stmt.close();
      SolmrLogger.debug(this, "Executing aggiornaDatiAAEP:delete:");



      String insertAziendaAAEP = "INSERT INTO DB_AZIENDA_AAEP "+
                     "(ID_AZIENDA_AAEP, FLAG_PRESENTE_IN_AAEP, DATA_AGGIORNAMENTO, CUAA)"+
                     "VALUES"+
                     "(?,"+       //ID_AZIENDA_AAEP                    1
                      "'N',"+     //FLAG_PRESENZA_IN_AAEP
                      "SYSDATE,"+ //DATA_AGGIORNAMENTO
                      "?)";       //CUAA                              2


      stmt = conn.prepareStatement(insertAziendaAAEP);


      stmt.setLong(1, primaryKey.longValue()); //ID_AZIENDA_AAEP
      stmt.setString(2,CUAA); //CUAA

      SolmrLogger.debug(this, "Executing aggiornaDatiAAEP:insertAziendaAAEP"+insertAziendaAAEP);

      stmt.executeUpdate();
      SolmrLogger.debug(this, "Executed aggiornaDatiAAEP:insertAziendaAAEP");

      stmt.close();

    }
    catch (SQLException exc)
    {
      if (exc.getErrorCode()!=1)
      {
        SolmrLogger.fatal(this, "SQLException: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
    }
    catch (Exception ex)
    {
      SolmrLogger.fatal(this, "Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally
    {
      try
      {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc)
      {
        SolmrLogger.fatal(this, "SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex)
      {
        SolmrLogger.fatal(this, "Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  public String isFlagPrevalente(Long[] idAteco) throws DataAccessException
  {
	  String query = null;
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  StringBuffer queryBuf = null;
	  String flagPrevalente = null;
	  
	  try
	  {
	    SolmrLogger.debug(this,"[AAEPDAO::isFlagPrevalente] BEGIN.");
	
	    /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
	
	    queryBuf = new StringBuffer();
	    queryBuf.append(" SELECT FLAG_PREVALENTE " + 
	        		" FROM  DB_CONVERSIONE_ATECO " + 
	        		" WHERE ID_ATTIVITA_ATECO_OLD in (?");
	    
	    int atecoDim = idAteco.length;
	    int atecoCount = 1;
	    for(int i=1;i<atecoDim;i++)
	    	if(idAteco[i]!=null){
	    		queryBuf.append(",?");
	    		atecoCount++;
	    	}
	    queryBuf.append(")");
	    
	    query = queryBuf.toString();
	    /* CONCATENAZIONE/CREAZIONE QUERY END. */
	
	    conn = getDatasource().getConnection();
	    if (SolmrLogger.isDebugEnabled(this))
	    {
	      // Dato che la query costruita dinamicamente è un dato importante la
	      // registro sul file di log se il
	      // debug è abilitato
	
	      SolmrLogger.debug(this,"[AAEPDAO::isFlagPrevalente] Query="+ query);
	    }
	    stmt = conn.prepareStatement(query);
	    int i=0;
	    for(int k=0;k<atecoCount;k++)
	    	stmt.setLong(++i,idAteco[k]);
	    //stmt.setLong(1,idAteco[0]);
	    //stmt.setLong(2,idAteco[1]);
	    
	
	    // Setto i parametri della query
	    ResultSet rs = stmt.executeQuery();
	    
	    while (rs.next())
	    {
	  	  flagPrevalente = rs.getString("FLAG_PREVALENTE");
	  	  if(flagPrevalente.equalsIgnoreCase("S"))
	  		  break;
	    }
	    
	  }
	  catch (Throwable t)
	  {
	    // Vettore di variabili interne del metodo
	    Variabile variabili[] = new Variabile[]
	    { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
	        new Variabile("flagPrevalente", flagPrevalente) };
	
	    // Vettore di parametri passati al metodo
	    Parametro parametri[] = new Parametro[]
	    { new Parametro("idAteco", idAteco) };
	    // Logging dell'eccezione, query, variabili e parametri del metodo
	    LoggerUtils.logDAOError(this,"[AAEPDAO::isFlagPrevalente] ",t, query, variabili, parametri);
	    /*
	     * Rimappo e rilancio l'eccezione come DataAccessException.
	     */
	    throw new DataAccessException(t.getMessage());
	  }
	  finally
	  {
	    /*
	     * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
	     * ignora ogni eventuale eccezione)
	     */
	    close(null, stmt, conn);
	
	    // Fine metodo
	    SolmrLogger.debug(this,"[AAEPDAO::isFlagPrevalente] END.");
	  }
	  return flagPrevalente;
  }

  public CodeDescription[] getAttivitaATECONewByOld(Integer idAtecoOld) throws DataAccessException
  {
	  String query = null;
	  Connection conn = null;
	  PreparedStatement stmt = null;
	  StringBuffer queryBuf = null;
	  Vector<CodeDescription> result = new Vector<CodeDescription>();
	  
	  try
	  {
	    SolmrLogger.debug(this,"[AAEPDAO::getAttivitaATECONewByOld] BEGIN.");
	
	    /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
	
	    queryBuf = new StringBuffer();
	    queryBuf.append(" SELECT ID_ATTIVITA_ATECO_NEW, FLAG_PREVALENTE " + 
	        		" FROM  DB_CONVERSIONE_ATECO " + 
	        		" WHERE ID_ATTIVITA_ATECO_OLD = ?");
	    
	    query = queryBuf.toString();
	    /* CONCATENAZIONE/CREAZIONE QUERY END. */
	
	    conn = getDatasource().getConnection();
	    if (SolmrLogger.isDebugEnabled(this))
	    {
	      // Dato che la query costruita dinamicamente è un dato importante la
	      // registro sul file di log se il
	      // debug è abilitato
	
	      SolmrLogger.debug(this,"[AAEPDAO::getAttivitaATECONewByOld] Query="+ query);
	    }
	    stmt = conn.prepareStatement(query);
	    
    	stmt.setInt(1,idAtecoOld);
	    
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next())
        {
            CodeDescription cd = new CodeDescription();
            
            cd.setCode(new Integer(resultSet.getInt("ID_ATTIVITA_ATECO_NEW")));
            cd.setCodeFlag(resultSet.getString("FLAG_PREVALENTE"));
      
            result.add(cd);
        }
	  }
	  catch (Throwable t)
	  {
	    // Vettore di variabili interne del metodo
	    Variabile variabili[] = new Variabile[]
	    { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
	        new Variabile("result", result) };
	
	    // Vettore di parametri passati al metodo
	    Parametro parametri[] = new Parametro[]
	    { new Parametro("idAtecoOld", idAtecoOld) };
	    // Logging dell'eccezione, query, variabili e parametri del metodo
	    LoggerUtils.logDAOError(this,"[AAEPDAO::getAttivitaATECONewByOld] ",t, query, variabili, parametri);
	    /*
	     * Rimappo e rilancio l'eccezione come DataAccessException.
	     */
	    throw new DataAccessException(t.getMessage());
	  }
	  finally
	  {
	    /*
	     * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
	     * ignora ogni eventuale eccezione)
	     */
	    close(null, stmt, conn);
	
	    // Fine metodo
	    SolmrLogger.debug(this,"[AAEPDAO::getAttivitaATECONewByOld] END.");
	  }
	  return result.size() == 0 ? null : (CodeDescription[]) result.toArray(new CodeDescription[0]);
  }
  
  
  public TipoSezioniAaepVO getTipoSezioneAaepByCodiceSez(String codiceSezione) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    TipoSezioniAaepVO tipoSezioniAaepVO = null;
    
    try
    {
      SolmrLogger.debug(this,"[AAEPDAO::getTipoSezioneAaepByCodiceSez] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
      	"SELECT ID_TIPO_SEZIONI_AAEP, " +
      	"       DESCRIZIONE, " +
      	"       CODICE_SEZIONE, " +
      	"       DATA_INIZIO_VALIDITA " + 
        "FROM   DB_TIPO_SEZIONI_AAEP " + 
        "WHERE  CODICE_SEZIONE = ? ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,"[AAEPDAO::getTipoSezioneAaepByCodiceSez] Query="+ query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setString(1, codiceSezione);
      
      ResultSet rs = stmt.executeQuery();

      if (rs.next())
      {
        tipoSezioniAaepVO = new TipoSezioniAaepVO();
        
        tipoSezioniAaepVO.setIdTipoSezioniAaep(rs.getLong("ID_TIPO_SEZIONI_AAEP"));
        tipoSezioniAaepVO.setCodiceSezione(rs.getString("CODICE_SEZIONE"));
        tipoSezioniAaepVO.setDescrizione(rs.getString("DESCRIZIONE"));
        tipoSezioniAaepVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
      }
    }
    catch (Exception t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("tipoSezioniAaepVO", tipoSezioniAaepVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("codiceSezione", codiceSezione) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,"[AAEPDAO::getTipoSezioneAaepByCodiceSez] ",t, query, variabili, parametri);
      /*
       * Rimappo e rilancio l'eccezione come DataAccessException.
       */
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      /*
       * Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       * ignora ogni eventuale eccezione)
       */
      close(null, stmt, conn);
  
      // Fine metodo
      SolmrLogger.debug(this,"[AAEPDAO::getTipoSezioneAaepByCodiceSez] END.");
    }
    return tipoSezioniAaepVO;
  }

}
