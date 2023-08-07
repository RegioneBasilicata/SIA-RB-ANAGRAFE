package it.csi.solmr.integration.anag;

import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.ElencoNotificheVO;
import it.csi.solmr.dto.anag.NotificaEntitaVO;
import it.csi.solmr.dto.anag.NotificaVO;
import it.csi.solmr.dto.anag.terreni.ConduzioneDichiarataVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoTipologiaVinoVO;
import it.csi.solmr.dto.anag.terreni.TipoUtilizzoVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.UtilizzoDichiaratoVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;
import it.csi.util.performance.StopWatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

public class NotificaDAO extends it.csi.solmr.integration.BaseDAO {

  public NotificaDAO() throws ResourceAccessException{
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public NotificaDAO(String refName) throws ResourceAccessException {
   super(refName);
  }

  // Metodo per effettuare la ricerca delle notifiche in relazione a dei parametri di ricerca
  public ElencoNotificheVO ricercaNotificheByParametri(NotificaVO notificaVO, 
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza, Boolean storico, 
      int maxRecord) 
      throws DataAccessException, SolmrException 
  {

    Vector<NotificaVO> elencoNotifiche = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    ElencoNotificheVO elencoNotificheVO = new ElencoNotificheVO();

    try 
    {
      conn = getDatasource().getConnection();

      String query = "";

      // Se l'utente è un funzionario PA
      if(!ruoloUtenza.isUtenteIntermediario() && !ruoloUtenza.isUtenteAziendaAgricola() && !ruoloUtenza.isUtenteTitolareCf()) 
      {

        query = " SELECT   N.ID_NOTIFICA, " +
                "          N.ID_AZIENDA, " +
                "          N.ID_UTENTE_INSERIMENTO, " +
                "          N.ID_PROCEDIMENTO_MITTENTE, " +
                "          N.DATA_INSERIMENTO, " +
                "          N.DESCRIZIONE AS DESC_NOTIFICA, " +
                "          N.DATA_CHIUSURA, " +
                "          N.ID_UTENTE_CHIUSURA, " +
                "          N.ID_TIPOLOGIA_NOTIFICA, " +
                "          N.ID_CATEGORIA_NOTIFICA, " +
                "          N.ID_PROCEDIMENTO_DESTINATARIO, " +
                "          N.NOTE_CHIUSURA, " +
                "          TCN.DESCRIZIONE AS DESC_CATEGORIA_NOTIFICA, " +
                "          AA.CUAA, " +
                "          AA.DENOMINAZIONE, " +
                "          C.DESCOM, " +
                "          AA.ID_ANAGRAFICA_AZIENDA, " +
                "          (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                "          || ' ' " + 
                "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                "         WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " + 
                "          AS DEN_UTENTE_APERTURA, " +
                "       (SELECT PVU.DENOMINAZIONE " +
                "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
                "        WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " +
                "          AS ENTE_APERTURA, " +
                "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                "          || ' ' " + 
                "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                "         WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " + 
                "          AS DEN_UTENTE_CHIUSURA, " +
                "       (SELECT PVU.DENOMINAZIONE " +
                "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
                "        WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " +
                "          AS ENTE_CHIUSURA " +
                " FROM     DB_NOTIFICA N, " +
               /* "          PAPUA_V_UTENTE_LOGIN PVU, " +
                "          PAPUA_V_UTENTE_LOGIN PVU_CHIUSURA, " +*/
                "          DB_ANAGRAFICA_AZIENDA AA, " +
                "          COMUNE C, " +
                "          DB_TIPO_CATEGORIA_NOTIFICA TCN " +
                " WHERE    N.ID_PROCEDIMENTO_DESTINATARIO = ? " +
                /*" AND      N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN " +
                " AND      N.ID_UTENTE_CHIUSURA = PVU_CHIUSURA.ID_UTENTE_LOGIN (+) " +*/
                " AND      N.ID_CATEGORIA_NOTIFICA = TCN.ID_CATEGORIA_NOTIFICA ";

        // Ricerco per tipologia notifica solo la voce selezionata dall'utente è diversa da tutte.
        if(notificaVO.getIdTipologiaNotifica().compareTo(new Long(0)) != 0) 
        {
          query += " AND      N.ID_TIPOLOGIA_NOTIFICA = ? ";
        }
        
        if(Validator.isNotEmpty(notificaVO.getIdCategoriaNotifica())) 
        {
          query += " AND      N.ID_CATEGORIA_NOTIFICA = ? ";
        }

        query += " AND      TRUNC(N.DATA_INSERIMENTO) >= ? ";
        
        if(!storico.booleanValue()) 
        {
          query += " AND N.DATA_CHIUSURA IS NULL ";
        }
                 

        if (Validator.isNotEmpty(notificaVO.getDataAl())) 
        {
          query += " AND TRUNC(N.DATA_INSERIMENTO) <= ? ";
        }

        query += " AND N.ID_AZIENDA = AA.ID_AZIENDA " +
                 " AND AA.DATA_FINE_VALIDITA IS NULL ";

        if (Validator.isNotEmpty(notificaVO.getIstatProvinciaUtente()) 
            && !notificaVO.getIstatProvinciaUtente().equalsIgnoreCase("0")) 
        {
          if((notificaVO.getIdTipologiaNotifica().compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_VARIAZIONECATASTALE) == 0)
            || (notificaVO.getIdTipologiaNotifica().compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_BLOCCAPROCEDIMENTI) == 0))
          {
            if(notificaVO.getIdTipologiaNotifica().compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_VARIAZIONECATASTALE) == 0)
            {
              query +=  "AND    EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
                  "              FROM   DB_NOTIFICA_ENTITA NE, " +
                  "                     DB_TIPO_ENTITA TE, " +
                  "                     DB_STORICO_PARTICELLA SP " +
                  "              WHERE  TE.CODICE_TIPO_ENTITA = 'P' " +
                  "              AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
                  "              AND    NE.IDENTIFICATIVO = SP.ID_PARTICELLA " +
                  "              AND    SP.DATA_FINE_VALIDITA IS NULL " +
                  "              AND    SUBSTR(SP.COMUNE,1,3) = ? " +
                  "              AND    NE.ID_NOTIFICA = N.ID_NOTIFICA) ";
            }
            else if(notificaVO.getIdTipologiaNotifica().compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_BLOCCAPROCEDIMENTI) == 0)
            {
              query +=   " AND  EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
                  "               FROM   DB_NOTIFICA_ENTITA NE, " +
                  "                      DB_TIPO_ENTITA TE, " +
                  "                      DB_STORICO_PARTICELLA SP, " +
                  "                      DB_STORICO_UNITA_ARBOREA SUA " +
                  "               WHERE  TE.CODICE_TIPO_ENTITA = 'U' " +
                  "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
                  "               AND    NE.IDENTIFICATIVO = SUA.ID_UNITA_ARBOREA " +
                  "               AND    SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
                  "               AND    SP.DATA_FINE_VALIDITA IS NULL " +
                  //"               AND    SUA.DATA_FINE_VALIDITA IS NULL " +
                  "               AND    SUBSTR(SP.COMUNE,1,3) = ? " +
                  "               AND    NE.ID_NOTIFICA = N.ID_NOTIFICA " +
                  "               AND    N.ID_AZIENDA = SUA.ID_AZIENDA)  ";
            }            
          }
          else
          {
            query += " AND AA.PROVINCIA_COMPETENZA = ? ";
          }
        }

        query += " AND      AA.DATA_CESSAZIONE IS NULL " +
                 " AND      AA.SEDELEG_COMUNE(+) = C.ISTAT_COMUNE " +
                 " ORDER BY N.DATA_INSERIMENTO DESC ";


        stmt = conn.prepareStatement(query);

        SolmrLogger.debug(this, "Executing query ricercaNotificheByParametri utente PA: " + query);

        int indice = 0;

        stmt.setLong(++indice, notificaVO.getIdProcedimentoDestinatario().longValue());
        // Ricerco per tipologia notifica solo la voce selezionata dall'utente è diversa da tutte.
        if(notificaVO.getIdTipologiaNotifica().compareTo(new Long(0)) != 0) 
        {
          stmt.setLong(++indice, notificaVO.getIdTipologiaNotifica().longValue());
        }
        if(Validator.isNotEmpty(notificaVO.getIdCategoriaNotifica())) 
        {
          stmt.setLong(++indice, notificaVO.getIdCategoriaNotifica().longValue());
        }
        stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(notificaVO.getDataDal()).getTime()));
        if (Validator.isNotEmpty(notificaVO.getDataAl())) 
        {
          stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(notificaVO.getDataAl()).getTime()));
        }
        if (Validator.isNotEmpty(notificaVO.getIstatProvinciaUtente()) && !notificaVO.getIstatProvinciaUtente().equalsIgnoreCase("0")) 
        {
          stmt.setString(++indice, notificaVO.getIstatProvinciaUtente());
        }
      }
      // Se invece è un intermediario
      else if(ruoloUtenza.isUtenteIntermediario()) 
      {
        // Se è un intermediario con livello "Z"
        if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA"))) 
        {

          query = " SELECT   N.ID_NOTIFICA, " +
                  "          N.ID_AZIENDA, " +
                  "          N.ID_UTENTE_INSERIMENTO, " +
                  "          N.ID_PROCEDIMENTO_MITTENTE, " +
                  "          N.DATA_INSERIMENTO, " +
                  "          N.DESCRIZIONE AS DESC_NOTIFICA, " +
                  "          N.DATA_CHIUSURA, " +
                  "          N.ID_UTENTE_CHIUSURA, " +
                  "          N.ID_TIPOLOGIA_NOTIFICA, " +
                  "          N.ID_CATEGORIA_NOTIFICA, " +
                  "          N.ID_PROCEDIMENTO_DESTINATARIO, " +
                  "          N.NOTE_CHIUSURA, " +
                  "          TCN.DESCRIZIONE AS DESC_CATEGORIA_NOTIFICA, " +
                  "          AA.CUAA, " +
                  "          AA.DENOMINAZIONE, " +
                  "          C.DESCOM, " +
                  "          AA.ID_ANAGRAFICA_AZIENDA, " +
                  "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                  "          || ' ' " + 
                  "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                  "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                  "         WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " + 
                  "          AS DEN_UTENTE_APERTURA, " +
                  "       (SELECT PVU.DENOMINAZIONE " +
                  "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
                  "        WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " +
                  "          AS ENTE_APERTURA, " +
                  "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                  "          || ' ' " + 
                  "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                  "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                  "         WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " + 
                  "          AS DEN_UTENTE_CHIUSURA, " +
                  "       (SELECT PVU.DENOMINAZIONE " +
                  "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
                  "        WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " +
                  "          AS ENTE_CHIUSURA " +
                  " FROM     DB_NOTIFICA N, " +
                 /* "          PAPUA_V_UTENTE_LOGIN PVU, " +
                  "          PAPUA_V_UTENTE_LOGIN PVU_CHIUSURA, " +*/
                  "          DB_ANAGRAFICA_AZIENDA AA, " +
                  "          DB_DELEGA D, " +
                  "          COMUNE C, " +
                  "          DB_INTERMEDIARIO I, " +
                  "          DB_TIPO_CATEGORIA_NOTIFICA TCN " +
                  " WHERE    N.ID_PROCEDIMENTO_DESTINATARIO = ? " +
                  " AND      TRUNC(N.DATA_INSERIMENTO) >= ? " +
               /*   " AND      N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN " +
                  " AND      N.ID_UTENTE_CHIUSURA = PVU_CHIUSURA.ID_UTENTE_LOGIN (+) " +*/
                  " AND      N.ID_CATEGORIA_NOTIFICA = TCN.ID_CATEGORIA_NOTIFICA ";

          // Ricerco per tipologia notifica solo la voce selezionata dall'utente è diversa da tutte.
          if(notificaVO.getIdTipologiaNotifica().compareTo(new Long(0)) != 0) 
          {
            query += " AND      N.ID_TIPOLOGIA_NOTIFICA = ? ";
          }
          
          if(Validator.isNotEmpty(notificaVO.getIdCategoriaNotifica())) 
          {
            query += " AND      N.ID_CATEGORIA_NOTIFICA = ? ";
          }

          if (Validator.isNotEmpty(notificaVO.getDataAl())) 
          {
            query += " AND TRUNC(N.DATA_INSERIMENTO) <= ? ";
          }
          
          if(!storico.booleanValue()) 
          {
            query += " AND N.DATA_CHIUSURA IS NULL ";
          }

          query += " AND N.ID_AZIENDA = AA.ID_AZIENDA " +
                   " AND AA.DATA_FINE_VALIDITA IS NULL " +
                   " AND AA.DATA_CESSAZIONE IS NULL ";

          if (Validator.isNotEmpty(notificaVO.getIstatProvinciaUtente()) 
              && !notificaVO.getIstatProvinciaUtente().equalsIgnoreCase("0")) 
          {
            query += " AND  SUBSTR(AA.SEDELEG_COMUNE,1,3) = ? ";
          }

          query += " AND        C.ISTAT_COMUNE = AA.SEDELEG_COMUNE(+) " +
                   " AND        D.ID_AZIENDA = N.ID_AZIENDA " +
                   " AND        D.ID_PROCEDIMENTO = ? " +
                   " AND        D.DATA_FINE IS NULL " +
                   " AND        D.ID_INTERMEDIARIO = ? " +
                   " AND        D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO " +
                   " ORDER BY   N.DATA_INSERIMENTO DESC";
        }
        // Se è un intermediario con livello "P"
        if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE"))) 
        {
          query = " SELECT   N.ID_NOTIFICA, " +
                  "          N.ID_AZIENDA, " +
                  "          N.ID_UTENTE_INSERIMENTO, " +
                  "          N.ID_PROCEDIMENTO_MITTENTE, " +
                  "          N.DATA_INSERIMENTO, " +
                  "          N.DESCRIZIONE AS DESC_NOTIFICA, " +
                  "          N.DATA_CHIUSURA, " +
                  "          N.ID_UTENTE_CHIUSURA, " +
                  "          N.ID_TIPOLOGIA_NOTIFICA, " +
                  "          N.ID_CATEGORIA_NOTIFICA, " +
                  "          N.ID_PROCEDIMENTO_DESTINATARIO, " +
                  "          N.NOTE_CHIUSURA, " +
                  "          TCN.DESCRIZIONE AS DESC_CATEGORIA_NOTIFICA, " +
                  "          AA.CUAA, " +
                  "          AA.DENOMINAZIONE, " +
                  "          C.DESCOM, " +
                  "          AA.ID_ANAGRAFICA_AZIENDA, " +
                  "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                  "          || ' ' " + 
                  "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                  "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                  "         WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " + 
                  "          AS DEN_UTENTE_APERTURA, " +
                  "       (SELECT PVU.DENOMINAZIONE " +
                  "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
                  "        WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " +
                  "          AS ENTE_APERTURA, " +
                  "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                  "          || ' ' " + 
                  "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                  "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                  "         WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " + 
                  "          AS DEN_UTENTE_CHIUSURA, " +
                  "       (SELECT PVU.DENOMINAZIONE " +
                  "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
                  "        WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " +
                  "          AS ENTE_CHIUSURA " +
                  " FROM     DB_NOTIFICA N, " +
                 /* "          PAPUA_V_UTENTE_LOGIN PVU, " +
                  "          PAPUA_V_UTENTE_LOGIN PVU_CHIUSURA, " +*/
                  "          DB_ANAGRAFICA_AZIENDA AA, " +
                  "          DB_DELEGA D, " +
                  "          COMUNE C, " +
                  "          DB_INTERMEDIARIO I, " +
                  "          DB_TIPO_CATEGORIA_NOTIFICA TCN " +
                  " WHERE    N.ID_PROCEDIMENTO_DESTINATARIO = ? " +
                  " AND      TRUNC(N.DATA_INSERIMENTO) >= ? " +
                 /* " AND      N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN " +
                  " AND      N.ID_UTENTE_CHIUSURA = PVU_CHIUSURA.ID_UTENTE_LOGIN (+) " +*/
                  " AND      N.ID_CATEGORIA_NOTIFICA = TCN.ID_CATEGORIA_NOTIFICA ";

          // Ricerco per tipologia notifica solo la voce selezionata dall'utente è diversa da tutte.
          if(notificaVO.getIdTipologiaNotifica().compareTo(new Long(0)) != 0) 
          {
            query += " AND      N.ID_TIPOLOGIA_NOTIFICA = ? ";
          }
          
          if(Validator.isNotEmpty(notificaVO.getIdCategoriaNotifica())) 
          {
            query += " AND      N.ID_CATEGORIA_NOTIFICA = ? ";
          }

          if (Validator.isNotEmpty(notificaVO.getDataAl())) 
          {
            query += " AND TRUNC(N.DATA_INSERIMENTO) <= ? ";
          }
          
          if(!storico.booleanValue()) 
          {
            query += " AND N.DATA_CHIUSURA IS NULL ";
          }

          query += " AND N.ID_AZIENDA = AA.ID_AZIENDA " +
                   " AND AA.DATA_FINE_VALIDITA IS NULL " +
                   " AND AA.DATA_CESSAZIONE IS NULL ";

          if (Validator.isNotEmpty(notificaVO.getIstatProvinciaUtente()) 
              && !notificaVO.getIstatProvinciaUtente().equalsIgnoreCase("0")) 
          {
            query += " AND  SUBSTR(AA.SEDELEG_COMUNE,1,3) = ? ";
          }

          query += " AND        C.ISTAT_COMUNE = AA.SEDELEG_COMUNE(+) " +
                   " AND        D.ID_AZIENDA = N.ID_AZIENDA " +
                   " AND        D.ID_PROCEDIMENTO = ? " +
                   " AND        D.DATA_FINE IS NULL " +
                   " AND        D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO " +
                   " AND        SUBSTR(I.CODICE_FISCALE,1,6) = ? " +
                   " AND        I.TIPO_INTERMEDIARIO = 'C' " +
                   " ORDER BY   N.DATA_INSERIMENTO DESC";

        }
        // Se è un intermediario con livello "R"
        if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE"))) 
        {
          query = " SELECT   N.ID_NOTIFICA, " +
                  "          N.ID_AZIENDA, " +
                  "          N.ID_UTENTE_INSERIMENTO, " +
                  "          N.ID_PROCEDIMENTO_MITTENTE, " +
                  "          N.DATA_INSERIMENTO, " +
                  "          N.DESCRIZIONE AS DESC_NOTIFICA, " +
                  "          N.DATA_CHIUSURA, " +
                  "          N.ID_UTENTE_CHIUSURA, " +
                  "          N.ID_TIPOLOGIA_NOTIFICA, " +
                  "          N.ID_CATEGORIA_NOTIFICA, " +
                  "          N.ID_PROCEDIMENTO_DESTINATARIO, " +
                  "          N.NOTE_CHIUSURA, " +
                  "          TCN.DESCRIZIONE AS DESC_CATEGORIA_NOTIFICA, " +
                  "          AA.CUAA, " +
                  "          AA.DENOMINAZIONE, " +
                  "          C.DESCOM, " +
                  "          AA.ID_ANAGRAFICA_AZIENDA, " +
                  "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                  "          || ' ' " + 
                  "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                  "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                  "         WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " + 
                  "          AS DEN_UTENTE_APERTURA, " +
                  "       (SELECT PVU.DENOMINAZIONE " +
                  "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
                  "        WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " +
                  "          AS ENTE_APERTURA, " +
                  "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
                  "          || ' ' " + 
                  "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
                  "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
                  "         WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " + 
                  "          AS DEN_UTENTE_CHIUSURA, " +
                  "       (SELECT PVU.DENOMINAZIONE " +
                  "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
                  "        WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " +
                  "          AS ENTE_CHIUSURA " +
                  "          PVU_CHIUSURA.DENOMINAZIONE AS ENTE_CHIUSURA " +
                  " FROM     DB_NOTIFICA N, " +
                 /* "          PAPUA_V_UTENTE_LOGIN PVU, " +
                  "          PAPUA_V_UTENTE_LOGIN PVU_CHIUSURA, " +*/
                  "          DB_ANAGRAFICA_AZIENDA AA, " +
                  "          DB_DELEGA D, " +
                  "          COMUNE C, " +
                  "          DB_INTERMEDIARIO I, " +
                  "          DB_TIPO_CATEGORIA_NOTIFICA TCN " +
                  " WHERE    N.ID_PROCEDIMENTO_DESTINATARIO = ? " +
                  " AND      TRUNC(N.DATA_INSERIMENTO) >= ? " +
                 /* " AND      N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN " +
                  " AND      N.ID_UTENTE_CHIUSURA = PVU_CHIUSURA.ID_UTENTE_LOGIN (+) " +*/
                  " AND      N.ID_CATEGORIA_NOTIFICA = TCN.ID_CATEGORIA_NOTIFICA ";

          // Ricerco per tipologia notifica solo la voce selezionata dall'utente è diversa da tutte.
          if(notificaVO.getIdTipologiaNotifica().compareTo(new Long(0)) != 0) 
          {
            query += " AND      N.ID_TIPOLOGIA_NOTIFICA = ? ";
          }
          
          // Ricerco per categoria notifica solo la voce selezionata dall'utente è diversa da tutte.
          if(Validator.isNotEmpty(notificaVO.getIdCategoriaNotifica())) 
          {
            query += " AND      N.ID_CATEGORIA_NOTIFICA = ? ";
          }

          if (Validator.isNotEmpty(notificaVO.getDataAl())) 
          {
            query += " AND TRUNC(N.DATA_INSERIMENTO) <= ? ";
          }
          
          if(!storico.booleanValue()) 
          {
            query += " AND N.DATA_CHIUSURA IS NULL ";
          }

          query += " AND N.ID_AZIENDA = AA.ID_AZIENDA " +
                   " AND AA.DATA_FINE_VALIDITA IS NULL " +
                   " AND AA.DATA_CESSAZIONE IS NULL ";

          if (Validator.isNotEmpty(notificaVO.getIstatProvinciaUtente()) 
              && !notificaVO.getIstatProvinciaUtente().equalsIgnoreCase("0")) 
          {
            query += " AND  SUBSTR(AA.SEDELEG_COMUNE,1,3) = ? ";
          }

          query += " AND        C.ISTAT_COMUNE = AA.SEDELEG_COMUNE(+) " +
                   " AND        D.ID_AZIENDA = N.ID_AZIENDA " +
                   " AND        D.ID_PROCEDIMENTO = ? " +
                   " AND        D.DATA_FINE IS NULL " +
                   " AND        D.ID_INTERMEDIARIO = I.ID_INTERMEDIARIO " +
                   " AND        SUBSTR(I.CODICE_FISCALE,1,3) = ? " +
                   " AND        I.TIPO_INTERMEDIARIO = 'C' " +
                   " ORDER BY   N.DATA_INSERIMENTO DESC";

        }

        stmt = conn.prepareStatement(query);

        SolmrLogger.debug(this, "Executing query ricercaNotificheByParametri utente Intermediario: " + query);

        int indice = 0;

        // Se l'intermediario ha livello di abilitazione "Z"
        if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_UFFICIO_ZONA"))) 
        {
          stmt.setLong(++indice, notificaVO.getIdProcedimentoDestinatario().longValue());
          stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(notificaVO.getDataDal()).getTime()));
          // Ricerco per tipologia notifica solo la voce selezionata dall'utente è diversa da tutte.
          if(notificaVO.getIdTipologiaNotifica().compareTo(new Long(0)) != 0) 
          {
            stmt.setLong(++indice, notificaVO.getIdTipologiaNotifica().longValue());
          }
          if(Validator.isNotEmpty(notificaVO.getIdCategoriaNotifica()))
          {
            stmt.setLong(++indice, notificaVO.getIdCategoriaNotifica().longValue());
          }
          if (Validator.isNotEmpty(notificaVO.getDataAl())) 
          {
            stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(notificaVO.getDataAl()).getTime()));
          }
          if (Validator.isNotEmpty(notificaVO.getIstatProvinciaUtente()) && !notificaVO.getIstatProvinciaUtente().equalsIgnoreCase("0")) 
          {
            stmt.setString(++indice, notificaVO.getIstatProvinciaUtente().substring(0,3));
          }
          stmt.setLong(++indice, notificaVO.getIdProcedimentoDestinatario().longValue());
          stmt.setLong(++indice, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario());
        }
        // Se l'intermediario ha livello di abilitazione "P"
        if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_PROVINCIALE"))) 
        {
          stmt.setLong(++indice, notificaVO.getIdProcedimentoDestinatario().longValue());
          stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(notificaVO.getDataDal()).getTime()));
          // Ricerco per tipologia notifica solo la voce selezionata dall'utente è diversa da tutte.
          if(notificaVO.getIdTipologiaNotifica().compareTo(new Long(0)) != 0) 
          {
            stmt.setLong(++indice, notificaVO.getIdTipologiaNotifica().longValue());
          }
          if(Validator.isNotEmpty(notificaVO.getIdCategoriaNotifica()))
          {
            stmt.setLong(++indice, notificaVO.getIdCategoriaNotifica().longValue());
          }
          if (Validator.isNotEmpty(notificaVO.getDataAl())) 
          {
            stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(notificaVO.getDataAl()).getTime()));
          }
          if (Validator.isNotEmpty(notificaVO.getIstatProvinciaUtente()) && !notificaVO.getIstatProvinciaUtente().equalsIgnoreCase("0")) 
          {
            stmt.setString(++indice, notificaVO.getIstatProvinciaUtente().substring(0,3));
          }
          stmt.setLong(++indice, notificaVO.getIdProcedimentoDestinatario().longValue());
          stmt.setString(++indice, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte().substring(0,6));
        }
        // Se l'intermediario ha livello di abilitazione "R"
        if(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getLivello().equalsIgnoreCase((String)SolmrConstants.get("LIVELLO_INTERMEDIARIO_REGIONALE"))) 
        {
          stmt.setLong(++indice, notificaVO.getIdProcedimentoDestinatario().longValue());
          stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(notificaVO.getDataDal()).getTime()));
          // Ricerco per tipologia notifica solo la voce selezionata dall'utente è diversa da tutte.
          if(notificaVO.getIdTipologiaNotifica().compareTo(new Long(0)) != 0) 
          {
            stmt.setLong(++indice, notificaVO.getIdTipologiaNotifica().longValue());
          }
          if(Validator.isNotEmpty(notificaVO.getIdCategoriaNotifica()))
          {
            stmt.setLong(++indice, notificaVO.getIdCategoriaNotifica().longValue());
          }
          if (Validator.isNotEmpty(notificaVO.getDataAl())) 
          {
            stmt.setDate(++indice, new java.sql.Date(DateUtils.parseDate(notificaVO.getDataAl()).getTime()));
          }
          if (Validator.isNotEmpty(notificaVO.getIstatProvinciaUtente()) && !notificaVO.getIstatProvinciaUtente().equalsIgnoreCase("0")) 
          {
            stmt.setString(++indice, notificaVO.getIstatProvinciaUtente().substring(0,3));
          }
          stmt.setLong(++indice, notificaVO.getIdProcedimentoDestinatario().longValue());
          stmt.setString(++indice, utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte().substring(0,3));
        }
      }

      ResultSet rs = stmt.executeQuery();

      

      while (rs.next()) 
      {  
        if(elencoNotifiche == null)
          elencoNotifiche = new Vector<NotificaVO>();
        
        NotificaVO resultNotificaVO = new NotificaVO();
        resultNotificaVO.setIdNotifica(new Long(rs.getLong("ID_NOTIFICA")));
        resultNotificaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        resultNotificaVO.setIdUtenteInserimento(new Long(rs.getLong("ID_UTENTE_INSERIMENTO")));
        resultNotificaVO.setIdProcedimentoMittente(new Long(rs.getLong("ID_PROCEDIMENTO_MITTENTE")));
        resultNotificaVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
        resultNotificaVO.setDescrizione(rs.getString("DESC_NOTIFICA"));
        resultNotificaVO.setDataChiusura(rs.getDate("DATA_CHIUSURA"));
        if (Validator.isNotEmpty(rs.getString("ID_UTENTE_CHIUSURA"))) 
        {
          resultNotificaVO.setIdUtenteChiusura(new Long(rs.getLong("ID_UTENTE_CHIUSURA")));
        }
        resultNotificaVO.setIdTipologiaNotifica(new Long(rs.getLong("ID_TIPOLOGIA_NOTIFICA")));
        resultNotificaVO.setIdCategoriaNotifica(new Long(rs.getLong("ID_CATEGORIA_NOTIFICA")));
        resultNotificaVO.setDescCategoriaNotifica(rs.getString("DESC_CATEGORIA_NOTIFICA"));
        resultNotificaVO.setIdProcedimentoDestinatario(new Long(rs.getLong("ID_PROCEDIMENTO_DESTINATARIO")));
        resultNotificaVO.setNoteChisura(rs.getString("NOTE_CHIUSURA"));
        resultNotificaVO.setCuaa(rs.getString("CUAA"));
        resultNotificaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
        resultNotificaVO.setDescrizioneComuneSedeLegale(rs.getString("DESCOM"));
        resultNotificaVO.setIdAnagraficaAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
        resultNotificaVO.setDenominazioneUtenteInserimento(rs.getString("DEN_UTENTE_APERTURA"));
        resultNotificaVO.setDescEnteAppartenenzaUtenteInserimento(rs.getString("ENTE_APERTURA"));
        resultNotificaVO.setDenominazioneUtenteChiusura(rs.getString("DEN_UTENTE_CHIUSURA"));
        resultNotificaVO.setDescEnteAppartenenzaUtenteChiusura(rs.getString("ENTE_CHIUSURA"));
        
        elencoNotifiche.add(resultNotificaVO);
      }
      
     
      rs.close();
      stmt.close();

      if(elencoNotifiche == null) {
        elencoNotificheVO.setMessaggioErrore((String)AnagErrors.get("ERR_NESSUNA_NOTIFICA"));
      }
      
      
      //se troppi resetto l'elenco
      if(elencoNotifiche != null && elencoNotifiche.size() > maxRecord)
      {
        elencoNotificheVO.setMessaggioErrore(AnagErrors.ERR_TROPPE_NOTIFICHE);
        elencoNotifiche = null;
      }
      
      elencoNotificheVO.setvElencoNotifiche(elencoNotifiche);
      
      
      

      //SolmrLogger.debug(this, "ricercaNotificheByParametri - Found "+elencoNotifiche.size()+" records");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "ricercaNotificheByParametri - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (SolmrException sex) {
      SolmrLogger.error(this, "ricercaNotificheByParametri - SolmrException: "+sex.getMessage());
      throw sex;
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "ricercaNotificheByParametri - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "ricercaNotificheByParametri - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "ricercaNotificheByParametri - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoNotificheVO;
  }

  // Metodo per effettuare la ricerca della notifica a partire dalla chiave primaria
  public NotificaVO findNotificaByPrimaryKey(Long idNotifica) throws DataAccessException {

    NotificaVO notificaVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT N.ID_NOTIFICA, " +
        "       N.ID_AZIENDA, " +
        "       N.ID_UTENTE_INSERIMENTO, " +
        "       N.ID_PROCEDIMENTO_MITTENTE, " +
        "       N.DATA_INSERIMENTO, " +
        "       N.DESCRIZIONE AS DESC_NOTIFICA, " +
        "       N.DATA_CHIUSURA, " +
        "       N.ID_UTENTE_CHIUSURA, " +
        "       N.ID_TIPOLOGIA_NOTIFICA, " +
        "       N.ID_CATEGORIA_NOTIFICA, " +
        "       N.ID_PROCEDIMENTO_DESTINATARIO, " +
        "       N.NOTE_CHIUSURA, " +
        "       TCN.DESCRIZIONE AS DESC_CATEGORIA_NOTIFICA, " +
        "       TTN.DESCRIZIONE AS DESC_TIPOLOGIA_NOTIFICA, " +
        "       TCN.ID_TIPO_ENTITA, " +
        "       TCN.CHIUSA_DA_ALTRO_RUOLO, " +
        "       TCN.INVIA_EMAIL_AD_EVENTO_NOTIFICA, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DEN_INSERIMENTO, " +
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " +
        "       AS ENTE_INSERIMENTO, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DEN_CHIUSURA, " +
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " +
        "       AS ENTE_CHIUSURA, " +
        "       AA.ID_ANAGRAFICA_AZIENDA " +
        "FROM   DB_NOTIFICA N, " +
        "       DB_TIPO_TIPOLOGIA_NOTIFICA TTN, " +
       /* "       PAPUA_V_UTENTE_LOGIN PVU, " +
        "       PAPUA_V_UTENTE_LOGIN PVU2, " +*/
        "       DB_ANAGRAFICA_AZIENDA AA, " +
        "       DB_TIPO_CATEGORIA_NOTIFICA TCN " +
        "WHERE  N.ID_NOTIFICA = ? " +
        "AND    N.ID_TIPOLOGIA_NOTIFICA = TTN.ID_TIPOLOGIA_NOTIFICA " +
        /*"AND    PVU.ID_UTENTE_LOGIN = N.ID_UTENTE_INSERIMENTO " +
        "AND    PVU2.ID_UTENTE_LOGIN(+) = N.ID_UTENTE_CHIUSURA " +*/
        "AND    AA.ID_AZIENDA = N.ID_AZIENDA " +
        "AND    AA.DATA_FINE_VALIDITA IS NULL " +
        "AND    N.ID_CATEGORIA_NOTIFICA = TCN.ID_CATEGORIA_NOTIFICA ";

      SolmrLogger.debug(this, "Executing query findNotificaByPrimaryKey: " + query);

      stmt = conn.prepareStatement(query);
      stmt.setLong(1, idNotifica.longValue());

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {
        notificaVO = new NotificaVO();
        notificaVO.setIdNotifica(new Long(rs.getLong("ID_NOTIFICA")));
        notificaVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        notificaVO.setIdUtenteInserimento(new Long(rs.getLong("ID_UTENTE_INSERIMENTO")));
        notificaVO.setIdProcedimentoMittente(new Long(rs.getLong("ID_PROCEDIMENTO_MITTENTE")));
        notificaVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
        notificaVO.setStrDataInserimento(DateUtils.formatDate(rs.getDate("DATA_INSERIMENTO")));
        notificaVO.setDescrizione(rs.getString("DESC_NOTIFICA"));
        notificaVO.setDataChiusura(rs.getDate("DATA_CHIUSURA"));
        if(Validator.isNotEmpty(rs.getDate("DATA_CHIUSURA"))) 
        {
          notificaVO.setStrDataChiusura(DateUtils.formatDate(rs.getDate("DATA_CHIUSURA")));
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTENTE_CHIUSURA"))) 
        {
          notificaVO.setIdUtenteChiusura(new Long(rs.getLong("ID_UTENTE_CHIUSURA")));
        }
        notificaVO.setIdTipologiaNotifica(new Long(rs.getLong("ID_TIPOLOGIA_NOTIFICA")));
        notificaVO.setTipiCategoriaNotifica(rs.getString("ID_CATEGORIA_NOTIFICA"));
        notificaVO.setIdCategoriaNotifica(new Long(rs.getLong("ID_CATEGORIA_NOTIFICA")));
        notificaVO.setIdTipoEntita(checkIntegerNull(rs.getString("ID_TIPO_ENTITA")));
        notificaVO.setIdProcedimentoDestinatario(new Long(rs.getLong("ID_PROCEDIMENTO_DESTINATARIO")));
        notificaVO.setNoteChisura(rs.getString("NOTE_CHIUSURA"));
        notificaVO.setDescCategoriaNotifica(rs.getString("DESC_CATEGORIA_NOTIFICA"));
        notificaVO.setDescTipologiaNotifica(rs.getString("DESC_TIPOLOGIA_NOTIFICA"));
        notificaVO.setDenominazioneUtenteInserimento(rs.getString("DEN_INSERIMENTO"));
        notificaVO.setDescEnteAppartenenzaUtenteInserimento(rs.getString("ENTE_INSERIMENTO"));
        notificaVO.setChiusaDaAltroRuolo(rs.getString("CHIUSA_DA_ALTRO_RUOLO"));
        if(Validator.isNotEmpty(rs.getString("DEN_CHIUSURA"))) 
        {
          notificaVO.setDenominazioneUtenteChiusura(rs.getString("DEN_CHIUSURA"));
          notificaVO.setDescEnteAppartenenzaUtenteChiusura(rs.getString("ENTE_CHIUSURA"));
        }
        notificaVO.setIdAnagraficaAzienda(new Long(rs.getLong("ID_ANAGRAFICA_AZIENDA")));
        notificaVO.setInviaEmail(rs.getString("INVIA_EMAIL_AD_EVENTO_NOTIFICA"));
      }
      rs.close();
      stmt.close();
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "findNotificaByPrimaryKey - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "findNotificaByPrimaryKey - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "findNotificaByPrimaryKey - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "findNotificaByPrimaryKey - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return notificaVO;
  }

  // Metodo per recuperare l'elenco delle notifiche in relazione ad un'azienda agricola e ad
  // una situazione(attuale o storica) relative ad un procedimento specificato
  public Vector<NotificaVO> getElencoNotificheByIdAzienda(NotificaVO notificaVO, Boolean storico, String ordinamento) throws DataAccessException, SolmrException {
    Vector<NotificaVO> elencoNotifiche = new Vector<NotificaVO>();
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();

      String query = 
         "SELECT N.ID_NOTIFICA, " +
         "       N.ID_AZIENDA, " +
         "       N.ID_TIPOLOGIA_NOTIFICA, " +
         "       N.ID_CATEGORIA_NOTIFICA, " +
         "       N.DATA_INSERIMENTO, " +
         "       N.DATA_CHIUSURA, " +
         "       N.DESCRIZIONE AS DESC_NOTIFICA, " +
         "       N.ID_UTENTE_INSERIMENTO, " +
         "       N.ID_UTENTE_CHIUSURA, " +
         "       TCN.DESCRIZIONE AS DESC_CATEGORIA_NOTIFICA, " +
         "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
         "          || ' ' " + 
         "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
         "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
         "         WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " + 
         "       AS DEN_APER, " +
         "       (SELECT PVU.DENOMINAZIONE " +
         "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
         "        WHERE N.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " +
         "       AS ENTE_APER, " +
         "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
         "          || ' ' " + 
         "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
         "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
         "         WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " + 
         "       AS DEN_CHIU, " +
         "       (SELECT PVU.DENOMINAZIONE " +
         "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
         "        WHERE N.ID_UTENTE_CHIUSURA = PVU.ID_UTENTE_LOGIN) " +
         "       AS ENTE_CHIU," +
         "       TTN.DESCRIZIONE AS DESC_TIPOLOGIA_NOTIFICA " +
         "FROM   DB_NOTIFICA N, " +
        /* "       PAPUA_V_UTENTE_LOGIN PVU, " +
         "       PAPUA_V_UTENTE_LOGIN PVU2, " +*/
         "       DB_TIPO_CATEGORIA_NOTIFICA TCN, " +
         "       DB_TIPO_TIPOLOGIA_NOTIFICA TTN " +
         "WHERE  N.ID_PROCEDIMENTO_DESTINATARIO = ? " +
        /* "AND    PVU.ID_UTENTE_LOGIN = N.ID_UTENTE_INSERIMENTO " +
         "AND    PVU2.ID_UTENTE_LOGIN(+) = N.ID_UTENTE_CHIUSURA " +*/
         "AND    N.ID_AZIENDA = ? " +
         "AND    N.ID_CATEGORIA_NOTIFICA = TCN.ID_CATEGORIA_NOTIFICA " +
         "AND    N.ID_TIPOLOGIA_NOTIFICA = TTN.ID_TIPOLOGIA_NOTIFICA ";

      if(!storico.booleanValue()) {
        query += " AND N.DATA_CHIUSURA IS NULL ";
      }

      // Se non specifico l'ordinamento imposto di default la data di inserimento
      if(Validator.isEmpty(ordinamento)) {
        query += " ORDER BY N.DATA_INSERIMENTO DESC ";
      }
      // Altrimenti per gravità di segnalazione
      else {
        query += " ORDER BY N.ID_TIPOLOGIA_NOTIFICA ASC ";
      }

      SolmrLogger.debug(this, "Executing query getElencoNotificheByIdAzienda: " + query);

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, notificaVO.getIdProcedimentoDestinatario().longValue());
      stmt.setLong(2, notificaVO.getIdAzienda().longValue());

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        NotificaVO notificaElencoVO = new NotificaVO();
        notificaElencoVO.setIdNotifica(new Long(rs.getLong("ID_NOTIFICA")));
        notificaElencoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        notificaElencoVO.setIdTipologiaNotifica(new Long(rs.getLong("ID_TIPOLOGIA_NOTIFICA")));
        notificaElencoVO.setIdCategoriaNotifica(new Long(rs.getLong("ID_CATEGORIA_NOTIFICA")));
        notificaElencoVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
        notificaElencoVO.setStrDataInserimento(DateUtils.formatDate(rs.getDate("DATA_INSERIMENTO")));
        if(Validator.isNotEmpty(rs.getString("DATA_CHIUSURA"))) 
        {
          notificaElencoVO.setDataChiusura(rs.getDate("DATA_CHIUSURA"));
          notificaElencoVO.setStrDataChiusura(DateUtils.formatDate(rs.getDate("DATA_CHIUSURA")));
        }
        notificaElencoVO.setDescrizione(rs.getString("DESC_NOTIFICA"));
        notificaElencoVO.setDescCategoriaNotifica(rs.getString("DESC_CATEGORIA_NOTIFICA"));
        
        
        
        notificaElencoVO.setDenominazioneUtenteInserimento(rs.getString("DEN_APER"));
        notificaElencoVO.setDescEnteAppartenenzaUtenteInserimento(rs.getString("ENTE_APER"));
        String utenChiusura=rs.getString("DEN_CHIU");
        if(Validator.isNotEmpty(utenChiusura)) 
        {
          notificaElencoVO.setDenominazioneUtenteChiusura(utenChiusura);
          notificaElencoVO.setDescEnteAppartenenzaUtenteChiusura(rs.getString("ENTE_CHIU"));
        }
        notificaElencoVO.setDescTipologiaNotifica(rs.getString("DESC_TIPOLOGIA_NOTIFICA"));
        
        elencoNotifiche.add(notificaElencoVO);
      }

      rs.close();
      stmt.close();

      if(elencoNotifiche.size() == 0 && !storico.booleanValue()) 
      {
        throw new SolmrException((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"));
      }
      else if(elencoNotifiche.size() == 0 && storico.booleanValue()) 
      {
        throw new SolmrException((String)AnagErrors.get("ERR_NO_NOTIFICHE_FOR_AZIENDA"));
      }

    }
    catch(SQLException exc) 
    {
    	SolmrLogger.error(this, "getElencoNotificheByIdAzienda - SQLException: "+exc);
    	throw new DataAccessException(exc.getMessage());
    }
    catch(SolmrException se) 
    {
    	SolmrLogger.debug(this, "getElencoNotificheByIdAzienda - SolmrException: "+se);
    	throw new SolmrException(se.getMessage());
    }
    catch(Exception ex) 
    {
    	SolmrLogger.error(this, "getElencoNotificheByIdAzienda - Generic Exception: "+ex);
    	throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
    	try 
    	{
    		if(stmt != null) stmt.close();
    		if(conn != null) conn.close();
    	}
    	catch(SQLException exc) 
    	{
    		SolmrLogger.error(this, "getElencoNotificheByIdAzienda - SQLException while closing Statement and Connection: "+exc);
    		throw new DataAccessException(exc.getMessage());
    	}
    	catch (Exception ex) 
    	{
    		SolmrLogger.error(this, "getElencoNotificheByIdAzienda - Generic Exception while closing Statement and Connection: "+ex);
    		throw new DataAccessException(ex.getMessage());
    	}
    }
    return elencoNotifiche;
  }
  
  
  public Vector<NotificaVO> getElencoNotifichePopUp(NotificaVO notificaVO) 
    throws DataAccessException
  {
    Vector<NotificaVO> elencoNotifiche = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String query = 
         "SELECT N.ID_NOTIFICA, " +
         "       N.ID_AZIENDA, " +
         "       N.ID_TIPOLOGIA_NOTIFICA, " +
         "       N.ID_CATEGORIA_NOTIFICA, " +
         "       N.DATA_INSERIMENTO, " +
         "       N.DATA_CHIUSURA, " +
         "       N.DESCRIZIONE AS DESC_NOTIFICA, " +
         "       TCN.DESCRIZIONE AS DESC_CATEGORIA_NOTIFICA, " +
         "       TTN.DESCRIZIONE AS DESC_TIPOLOGIA_NOTIFICA " +
         "FROM   DB_NOTIFICA N, " +
         "       DB_TIPO_CATEGORIA_NOTIFICA TCN, " +
         "       DB_TIPO_TIPOLOGIA_NOTIFICA TTN " +
         "WHERE  N.ID_PROCEDIMENTO_DESTINATARIO = ? " +
         "AND    N.ID_AZIENDA = ? " +
         "AND    N.ID_CATEGORIA_NOTIFICA = TCN.ID_CATEGORIA_NOTIFICA " +
         "AND    N.ID_TIPOLOGIA_NOTIFICA = TTN.ID_TIPOLOGIA_NOTIFICA " +
         "AND    N.DATA_CHIUSURA IS NULL " +
         "AND    TCN.VISUALIZZA_POP_UP = 'S' " +
         "ORDER BY N.DATA_INSERIMENTO DESC ";
   

      SolmrLogger.debug(this, "Executing query getElencoNotifichePopUp: " + query);

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, notificaVO.getIdProcedimentoDestinatario().longValue());
      stmt.setLong(2, notificaVO.getIdAzienda().longValue());

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(elencoNotifiche == null)
        {
          elencoNotifiche = new Vector<NotificaVO>();
        }
        NotificaVO notificaElencoVO = new NotificaVO();
        notificaElencoVO.setIdNotifica(new Long(rs.getLong("ID_NOTIFICA")));
        notificaElencoVO.setIdAzienda(new Long(rs.getLong("ID_AZIENDA")));
        notificaElencoVO.setIdTipologiaNotifica(new Long(rs.getLong("ID_TIPOLOGIA_NOTIFICA")));
        notificaElencoVO.setIdCategoriaNotifica(new Long(rs.getLong("ID_CATEGORIA_NOTIFICA")));
        notificaElencoVO.setDataInserimento(rs.getDate("DATA_INSERIMENTO"));
        notificaElencoVO.setStrDataInserimento(DateUtils.formatDate(rs.getDate("DATA_INSERIMENTO")));        
        notificaElencoVO.setDescrizione(rs.getString("DESC_NOTIFICA"));
        notificaElencoVO.setDescCategoriaNotifica(rs.getString("DESC_CATEGORIA_NOTIFICA"));       
        notificaElencoVO.setDescTipologiaNotifica(rs.getString("DESC_TIPOLOGIA_NOTIFICA"));
        
        elencoNotifiche.add(notificaElencoVO);
      }

      rs.close();
      stmt.close();

    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getElencoNotifichePopUp - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getElencoNotifichePopUp - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getElencoNotifichePopUp - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) 
      {
        SolmrLogger.error(this, "getElencoNotifichePopUp - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    return elencoNotifiche;
  }

  // Metodo per effettuare l'inserimento di una notifica
  public Long insertNotifica(NotificaVO notificaVO, Long idUtente) throws DataAccessException {

    Long idNotifica = getNextPrimaryKey((String)SolmrConstants.get("SEQ_NOTIFICA"));
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = getDatasource().getConnection();

      String query = " INSERT INTO DB_NOTIFICA (ID_NOTIFICA, " +
                     "                          ID_AZIENDA, " +
                     "                          ID_UTENTE_INSERIMENTO, " +
                     "                          ID_PROCEDIMENTO_MITTENTE, " +
                     "                          DATA_INSERIMENTO, " +
                     "                          DESCRIZIONE, " +
                     "                          DATA_CHIUSURA, " +
                     "                          ID_UTENTE_CHIUSURA, " +
                     "                          ID_TIPOLOGIA_NOTIFICA, " +
                     "                          ID_CATEGORIA_NOTIFICA, " +
                     "                          ID_PROCEDIMENTO_DESTINATARIO, " +
                     "                          NOTE_CHIUSURA," +
                     "                          ID_UTENTE_AGGIORNAMENTO, " +
                     "                          DATA_AGGIORNAMENTO) " +
                     " VALUES                  (?, ?, ?, ?, SYSDATE, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE) ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idNotifica.longValue());
      stmt.setLong(2, notificaVO.getIdAzienda().longValue());
      stmt.setLong(3, idUtente.longValue());
      stmt.setLong(4, notificaVO.getIdProcedimentoMittente().longValue());
      stmt.setString(5, notificaVO.getDescrizione());
      if(Validator.isNotEmpty(notificaVO.getDataChiusura())) {
        stmt.setDate(6, new java.sql.Date(notificaVO.getDataChiusura().getTime()));
      }
      else {
        stmt.setString(6, null);
      }
      if(Validator.isNotEmpty(notificaVO.getIdUtenteChiusura())) {
        stmt.setLong(7, notificaVO.getIdUtenteChiusura().longValue());
      }
      else {
        stmt.setString(7, null);
      }
      stmt.setLong(8, notificaVO.getIdTipologiaNotifica().longValue());
      stmt.setLong(9, notificaVO.getIdCategoriaNotifica().longValue());
      stmt.setLong(10, notificaVO.getIdProcedimentoDestinatario().longValue());
      stmt.setString(11, notificaVO.getNoteChisura());
      stmt.setLong(12, idUtente.longValue());

      SolmrLogger.debug(this, "Executing query insertNotifica: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertNotifica");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "insertNotifica - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "insertNotifica - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "insertNotifica - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "insertNotifica - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idNotifica;
  }
  
  public void updateNotifica(NotificaVO notificaVO, Long idUtente) throws DataAccessException 
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = getDatasource().getConnection();

      String query = 
        "UPDATE DB_NOTIFICA " +
      	"  SET DESCRIZIONE = ?, " +
      	"      ID_CATEGORIA_NOTIFICA = ?, " +
      	"      ID_UTENTE_AGGIORNAMENTO = ?, " +
      	"      DATA_AGGIORNAMENTO = SYSDATE " +
        "WHERE ID_NOTIFICA = ? ";

      stmt = conn.prepareStatement(query);

      int indice = 0;
      stmt.setString(++indice, notificaVO.getDescrizione());
      stmt.setLong(++indice, notificaVO.getIdCategoriaNotifica().longValue());
      stmt.setLong(++indice, idUtente.longValue());
      stmt.setLong(++indice, notificaVO.getIdNotifica().longValue());
      

      SolmrLogger.debug(this, "Executing query updateNotifica: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed updateNotifica");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "updateNotifica - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "updateNotifica - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "updateNotifica - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "updateNotifica - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  
  public Long insertNotificaEntita(NotificaEntitaVO notificaEntitaVO) throws DataAccessException 
  {
    Long idNotificaEntita = getNextPrimaryKey(SolmrConstants.SEQ_DB_NOTIFICA_ENTITA);
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      conn = getDatasource().getConnection();
      String query = 
        " INSERT INTO DB_NOTIFICA_ENTITA " +
        "  (ID_NOTIFICA_ENTITA, " +
        "   ID_NOTIFICA, " +
        "   ID_TIPO_ENTITA, " +
        "   IDENTIFICATIVO, " +
        "   NOTE, " +
        "   DATA_INIZIO_VALIDITA, " +
        "   DATA_FINE_VALIDITA, " +
        "   ID_UTENTE_AGGIORNAMENTO, " +
        "   DATA_AGGIORNAMENTO," +
        "   ID_DICHIARAZIONE_CONSISTENZA," +
        "   ID_UTENTE_INSERIMENTO) " +
        " VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

      stmt = conn.prepareStatement(query);
      int indice = 0;
       
      stmt.setLong(++indice, idNotificaEntita.longValue());
      stmt.setLong(++indice, notificaEntitaVO.getIdNotifica().longValue());
      stmt.setInt(++indice, notificaEntitaVO.getIdTipoEntita().intValue());
      stmt.setLong(++indice, notificaEntitaVO.getIdentificativo().longValue());
      stmt.setString(++indice, notificaEntitaVO.getNote());
      stmt.setTimestamp(++indice, convertDateToTimestamp(notificaEntitaVO.getDataInizioValidita()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(notificaEntitaVO.getDataFineValidita()));
      stmt.setLong(++indice, notificaEntitaVO.getIdUtenteAggiornamento().longValue());
      stmt.setTimestamp(++indice, convertDateToTimestamp(notificaEntitaVO.getDataAggiornamento()));
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(notificaEntitaVO.getIdDichiarazioneConsistenza()));
      stmt.setLong(++indice, notificaEntitaVO.getIdUtenteAggiornamento().longValue());
      
      SolmrLogger.debug(this, "Executing query insertNotificaEntita: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed insertNotificaEntita");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "insertNotificaEntita - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "insertNotificaEntita - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "insertNotificaEntita - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "insertNotificaEntita - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idNotificaEntita;
  }
  
  public void storicizzaNotificaEntita(long idNotifica, long idUtente) throws DataAccessException 
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      conn = getDatasource().getConnection();
      String query = 
        " UPDATE DB_NOTIFICA_ENTITA " +
        "   SET  DATA_FINE_VALIDITA = SYSDATE, " +
        "        DATA_AGGIORNAMENTO = SYSDATE, " +
        "        ID_UTENTE_AGGIORNAMENTO = ? " +
        " WHERE ID_NOTIFICA = ? " +
        " AND   DATA_FINE_VALIDITA IS NULL ";

      stmt = conn.prepareStatement(query);
      int indice = 0;
       
      stmt.setLong(++indice, idUtente);
      stmt.setLong(++indice, idNotifica);
      
      SolmrLogger.debug(this, "Executing query storicizzaNotificaEntita: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed storicizzaNotificaEntita");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "storicizzaNotificaEntita - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "storicizzaNotificaEntita - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "storicizzaNotificaEntita - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "storicizzaNotificaEntita - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  public void storicizzaNotificaEntitaByPrimaryKey(long idNotificaEntita, long idUtente) throws DataAccessException 
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      conn = getDatasource().getConnection();
      String query = 
        " UPDATE DB_NOTIFICA_ENTITA " +
        "   SET  DATA_FINE_VALIDITA = SYSDATE, " +
        "        DATA_AGGIORNAMENTO = SYSDATE, " +
        "        ID_UTENTE_AGGIORNAMENTO = ? " +
        " WHERE ID_NOTIFICA_ENTITA = ? ";

      stmt = conn.prepareStatement(query);
      int indice = 0;
       
      stmt.setLong(++indice, idUtente);
      stmt.setLong(++indice, idNotificaEntita);
      
      SolmrLogger.debug(this, "Executing query storicizzaNotificaEntitaByPrimaryKey: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed storicizzaNotificaEntitaByPrimaryKey");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "storicizzaNotificaEntitaByPrimaryKey - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "storicizzaNotificaEntitaByPrimaryKey - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "storicizzaNotificaEntitaByPrimaryKey - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "storicizzaNotificaEntitaByPrimaryKey - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  public void chiudiNotificaEntita(NotificaEntitaVO notificaEntitaVO, long idUtente) throws DataAccessException 
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    try 
    {
      conn = getDatasource().getConnection();
      String query = 
        " UPDATE DB_NOTIFICA_ENTITA " +
        "   SET  DATA_FINE_VALIDITA = SYSDATE, " +
        "        DATA_AGGIORNAMENTO = SYSDATE, " +
        "        ID_UTENTE_AGGIORNAMENTO = ?, " +
        "        NOTE_CHIUSURA_ENTITA = ? " +
        " WHERE ID_NOTIFICA_ENTITA = ? ";

      stmt = conn.prepareStatement(query);
      int indice = 0;
       
      stmt.setLong(++indice, idUtente);
      stmt.setString(++indice, notificaEntitaVO.getNoteChiusuraEntita());
      stmt.setLong(++indice, notificaEntitaVO.getIdNotificaEntita());
      
      SolmrLogger.debug(this, "Executing query chiudiNotificaEntita: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed chiudiNotificaEntita");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "chiudiNotificaEntita - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "chiudiNotificaEntita - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "chiudiNotificaEntita - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "chiudiNotificaEntita - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  // Metodo per effettuare la chiusura di una notifica
  public void closeNotifica(NotificaVO notificaVO, long idUtenteAggiornamento) throws DataAccessException {

    Connection conn = null;
    PreparedStatement stmt = null;

    try {
      conn = getDatasource().getConnection();

      String query = " UPDATE DB_NOTIFICA " +
                     " SET    DATA_CHIUSURA = SYSDATE, " +
                     "        ID_UTENTE_CHIUSURA = ?, " +
                     "        NOTE_CHIUSURA = ? " +
                     " WHERE  ID_NOTIFICA = ? ";

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idUtenteAggiornamento);
      stmt.setString(2, notificaVO.getNoteChisura());
      stmt.setLong(3, notificaVO.getIdNotifica().longValue());

      SolmrLogger.debug(this, "Executing query closeNotifica: " + query);

      stmt.executeUpdate();

      stmt.close();

      SolmrLogger.debug(this, "Executed closeNotifica");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "closeNotifica - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "closeNotifica - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "closeNotifica - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "closeNotifica - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  
  /**
   * ritorna la tipologia dalla categoria
   * 
   * 
   * @param idCategoriaNotifica
   * @return
   * @throws DataAccessException
   */
  public Long getIdTipologiaNotificaFromCategoria(Long idCategoriaNotifica) throws DataAccessException
  {
    Long idTipologiaNotifica = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String query = 
         "SELECT TCN.ID_TIPOLOGIA_NOTIFICA " +
         "FROM   DB_TIPO_CATEGORIA_NOTIFICA TCN " +
         "WHERE  TCN.ID_CATEGORIA_NOTIFICA = ? ";

      

      SolmrLogger.debug(this, "Executing query getIdTipologiaNotificaFromCategoria: " + query);

      stmt = conn.prepareStatement(query);

      stmt.setLong(1, idCategoriaNotifica.longValue());

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {
        idTipologiaNotifica = new Long(rs.getLong("ID_TIPOLOGIA_NOTIFICA"));
      }

      rs.close();
      stmt.close();


    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getIdTipologiaNotificaFromCategoria - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getIdTipologiaNotificaFromCategoria - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getIdTipologiaNotificaFromCategoria - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) 
      {
        SolmrLogger.error(this, "getIdTipologiaNotificaFromCategoria - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    return idTipologiaNotifica;
  }
  
  /**
   * 
   * Elenco notifiche associate all'identificativo
   * indipendentemente dalla storicizzazione
   * 
   */
  public Vector<NotificaVO> getElencoNotificheForIdentificato(long identificativo, 
      String codiceTipo,
      long idAzienda,
      Long idDichiarazioneConsistenza) 
      throws DataAccessException
  {
    Vector<NotificaVO> vNotifiche = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT TTN.DESCRIZIONE AS DESC_TIPOLOGIA_NOTIFICA, " +
        "       TCN.DESCRIZIONE AS DESC_CATEGORIA_NOTIFICA, " +
        "       NE.DATA_INIZIO_VALIDITA, " +
        "       NE.DATA_FINE_VALIDITA, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE NE.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DEN_INSERIMENTO, " +
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE NE.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " +
        "       AS ENTE_INSERIMENTO, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DEN_CHIUSURA, " +
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "       AS ENTE_CHIUSURA, " +
        "       NE.NOTE, " +
        "       NE.NOTE_CHIUSURA_ENTITA " +
        "FROM   DB_NOTIFICA_ENTITA NE, " +
        "       DB_NOTIFICA NO, " +
       /* "       PAPUA_V_UTENTE_LOGIN PVU, " + 
        "       PAPUA_V_UTENTE_LOGIN PVU2, " +*/
        "       DB_TIPO_ENTITA TE, " +
        "       DB_TIPO_TIPOLOGIA_NOTIFICA TTN, " +
        "       DB_TIPO_CATEGORIA_NOTIFICA TCN " +
        "WHERE  NE.IDENTIFICATIVO = ? " +
        "AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
        "AND    NO.ID_TIPOLOGIA_NOTIFICA = TTN.ID_TIPOLOGIA_NOTIFICA " +
        "AND    NO.ID_CATEGORIA_NOTIFICA = TCN.ID_CATEGORIA_NOTIFICA " +
        "AND    NO.ID_AZIENDA = ? " +
        "AND    NE.ID_TIPO_ENTITA = TE.ID_TIPO_ENTITA " + 
        "AND    TE.CODICE_TIPO_ENTITA = ? " +
       /* "AND    NE.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN " +
        "AND    NE.ID_UTENTE_AGGIORNAMENTO = PVU2.ID_UTENTE_LOGIN " +*/
        "AND    NE.DATA_FINE_VALIDITA IS NULL ";
      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
      {
        query +=
        "AND    NE.ID_DICHIARAZIONE_CONSISTENZA = ? ";
      }
      query +=
         "UNION  " +
         "SELECT TTN.DESCRIZIONE AS DESC_TIPOLOGIA_NOTIFICA, " +
          "      TCN.DESCRIZIONE AS DESC_CATEGORIA_NOTIFICA, " +
          "      NE.DATA_INIZIO_VALIDITA, " +
          "      NE.DATA_FINE_VALIDITA, " +
          "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE NE.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " + 
          "       AS DEN_INSERIMENTO, " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE NE.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN) " +
          "       AS ENTE_INSERIMENTO, " +
          "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
          "          || ' ' " + 
          "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
          "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
          "         WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
          "       AS DEN_CHIUSURA, " +
          "       (SELECT PVU.DENOMINAZIONE " +
          "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
          "        WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
          "       AS ENTE_CHIUSURA, " +
          "      NE.NOTE, " +
          "      NE.NOTE_CHIUSURA_ENTITA " +
          "FROM  DB_NOTIFICA_ENTITA NE, " +
          "      DB_NOTIFICA NO, " +
         /* "      PAPUA_V_UTENTE_LOGIN PVU, " + 
          "      PAPUA_V_UTENTE_LOGIN PVU2, " +*/
          "      DB_TIPO_ENTITA TE, " +
          "      DB_TIPO_TIPOLOGIA_NOTIFICA TTN, " + 
          "      DB_TIPO_CATEGORIA_NOTIFICA TCN " +
          "WHERE NE.IDENTIFICATIVO = ? " +  
          "AND   NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
          "AND   NO.ID_TIPOLOGIA_NOTIFICA = TTN.ID_TIPOLOGIA_NOTIFICA " +
          "AND   NO.ID_CATEGORIA_NOTIFICA = TCN.ID_CATEGORIA_NOTIFICA " + 
          "AND   NO.ID_AZIENDA = ? " +
          "AND   NE.ID_TIPO_ENTITA = TE.ID_TIPO_ENTITA " +
          "AND   TE.CODICE_TIPO_ENTITA = ? ";
        /*  "AND   NE.ID_UTENTE_INSERIMENTO = PVU.ID_UTENTE_LOGIN " + 
          "AND   NE.ID_UTENTE_AGGIORNAMENTO = PVU2.ID_UTENTE_LOGIN ";*/
        if(Validator.isNotEmpty(idDichiarazioneConsistenza))
        {
          query +=
          "AND   NE.ID_DICHIARAZIONE_CONSISTENZA = ? ";
        }
        query +=
          "AND   NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
          "                               FROM   DB_NOTIFICA_ENTITA NE1 " +
          "                               WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
          "                               AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
          "                               AND    NE1.DATA_FINE_VALIDITA IS NOT NULL) " +
          "AND   NE.IDENTIFICATIVO NOT IN (SELECT NE1.IDENTIFICATIVO " +
          "                                FROM   DB_NOTIFICA_ENTITA NE1 " + 
          "                                WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
          "                                AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
          "                                AND    NE1.DATA_FINE_VALIDITA IS NULL) " +      
          "ORDER BY DATA_INIZIO_VALIDITA ASC ";


      SolmrLogger.debug(this, "Executing query getElencoNotificheForIdentificato: " + query);

      stmt = conn.prepareStatement(query);
      
      int idx = 0;
      
      stmt.setLong(++idx, identificativo);
      stmt.setLong(++idx, idAzienda);
      stmt.setString(++idx, codiceTipo);
      
      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
      {
        stmt.setLong(++idx, idDichiarazioneConsistenza.longValue());
      }
      
      stmt.setLong(++idx, identificativo);
      stmt.setLong(++idx, idAzienda);
      stmt.setString(++idx, codiceTipo);
      
      if(Validator.isNotEmpty(idDichiarazioneConsistenza))
      {
        stmt.setLong(++idx, idDichiarazioneConsistenza.longValue());
      }

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(vNotifiche == null)
        {
          vNotifiche = new Vector<NotificaVO>();
        }
        
        NotificaVO notificaVO = new NotificaVO();
        notificaVO.setDescTipologiaNotifica(rs.getString("DESC_TIPOLOGIA_NOTIFICA"));
        notificaVO.setDescCategoriaNotifica(rs.getString("DESC_CATEGORIA_NOTIFICA"));
        NotificaEntitaVO notificaEntitaVO = new NotificaEntitaVO(); 
        notificaEntitaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
        notificaEntitaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        notificaEntitaVO.setDenUtente(rs.getString("DEN_INSERIMENTO"));
        notificaEntitaVO.setDenEnteUtente(rs.getString("ENTE_INSERIMENTO"));
        notificaEntitaVO.setNoteChiusuraEntita(rs.getString("NOTE_CHIUSURA_ENTITA"));
        if(Validator.isNotEmpty(notificaEntitaVO.getDataFineValidita()))
        {          
          notificaEntitaVO.setDenUtenteChiusura(rs.getString("DEN_CHIUSURA"));
          notificaEntitaVO.setDenEnteUtenteChiusura(rs.getString("ENTE_CHIUSURA"));
        }
        notificaEntitaVO.setNote(rs.getString("NOTE"));
        
        Vector<NotificaEntitaVO> vNotificaEntita = new Vector<NotificaEntitaVO>();
        vNotificaEntita.add(notificaEntitaVO);        
        notificaVO.setvNotificaEntita(vNotificaEntita);
        
        vNotifiche.add(notificaVO);
      }

      rs.close();
      stmt.close();


    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getElencoNotificheForIdentificato - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getElencoNotificheForIdentificato - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getElencoNotificheForIdentificato - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) 
      {
        SolmrLogger.error(this, "getElencoNotificheForIdentificato - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vNotifiche;
  }
  
  /**
   * ritorna tutte le entita incluse quelle
   * che sono state chiuse 
   * 
   * 
   * 
   * @param idNotifica
   * @return
   * @throws DataAccessException
   */
  public Vector<NotificaEntitaVO> getElencoNotificheEntitaByIdNotifica(long idNotifica, boolean storico) 
      throws DataAccessException
  {
    Vector<NotificaEntitaVO> vNotificheEntita = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String query = 
        "SELECT NE.ID_NOTIFICA_ENTITA, " +
        "       NE.ID_NOTIFICA, " +
        "       NE.ID_TIPO_ENTITA, " +
        "       NE.IDENTIFICATIVO, " +
        "       NE.NOTE, " +
        "       NE.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       NE.NOTE_CHIUSURA_ENTITA, " +
        "       NE.DATA_FINE_VALIDITA, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DEN_INSERIMENTO, " +
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "       AS ENTE_INSERIMENTO " +
        "FROM   DB_NOTIFICA_ENTITA NE " +
     //   "       PAPUA_V_UTENTE_LOGIN PVU " + 
        "WHERE  NE.ID_NOTIFICA = ? " +
       // "AND    NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
        "AND    NE.DATA_FINE_VALIDITA IS NULL ";
      if(storico)
      {
        query +=
        "UNION " +
        "SELECT NE.ID_NOTIFICA_ENTITA, " +
        "       NE.ID_NOTIFICA, " +
        "       NE.ID_TIPO_ENTITA, " +
        "       NE.IDENTIFICATIVO, " +
        "       NE.NOTE, " +
        "       NE.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       NE.NOTE_CHIUSURA_ENTITA, " +
        "       NE.DATA_FINE_VALIDITA, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DEN_INSERIMENTO, " +
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "       AS ENTE_INSERIMENTO " +
        "FROM DB_NOTIFICA_ENTITA NE " +
        //"PAPUA_V_UTENTE_LOGIN PVU " +
        "WHERE NE.ID_NOTIFICA = ? " + 
        //"AND NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
        "AND NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
        "                             FROM   DB_NOTIFICA_ENTITA NE1 " +
        "                             WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
        "                             AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
        "                             AND    NE1.DATA_FINE_VALIDITA IS NOT NULL) " +
        "AND NE.IDENTIFICATIVO NOT IN (SELECT NE1.IDENTIFICATIVO " +
        "                              FROM   DB_NOTIFICA_ENTITA NE1 " +
        "                              WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
        "                              AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
        "                              AND    NE1.DATA_FINE_VALIDITA IS NULL) ";
      }
      
      query +=
         "ORDER BY ID_NOTIFICA_ENTITA ASC ";


      SolmrLogger.debug(this, "Executing query getElencoNotificheEntitaByIdNotifica: " + query);

      stmt = conn.prepareStatement(query);
      int indice = 0;
      stmt.setLong(++indice, idNotifica);
      if(storico)
      {
        stmt.setLong(++indice, idNotifica);
      }

      ResultSet rs = stmt.executeQuery();

      while(rs.next()) 
      {
        if(vNotificheEntita == null)
        {
          vNotificheEntita = new Vector<NotificaEntitaVO>();
        }
        
        NotificaEntitaVO notificaEntitaVO = new NotificaEntitaVO();
        
        notificaEntitaVO.setIdNotificaEntita(new Long(rs.getLong("ID_NOTIFICA_ENTITA")));
        notificaEntitaVO.setIdNotifica(new Long(rs.getLong("ID_NOTIFICA")));
        notificaEntitaVO.setIdTipoEntita(new Integer(rs.getInt("ID_TIPO_ENTITA")));
        notificaEntitaVO.setIdentificativo(new Long(rs.getLong("IDENTIFICATIVO")));
        notificaEntitaVO.setNote(rs.getString("NOTE"));        
        notificaEntitaVO.setIdDichiarazioneConsistenza(checkLongNull(rs.getString("ID_DICHIARAZIONE_CONSISTENZA")));
        notificaEntitaVO.setNoteChiusuraEntita(rs.getString("NOTE_CHIUSURA_ENTITA"));
        notificaEntitaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        notificaEntitaVO.setDenUtente(rs.getString("DEN_INSERIMENTO"));
        notificaEntitaVO.setDenEnteUtente(rs.getString("ENTE_INSERIMENTO"));
        
        vNotificheEntita.add(notificaEntitaVO);
      }

      rs.close();
      stmt.close();


    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "getElencoNotificheEntitaByIdNotifica - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "getElencoNotificheEntitaByIdNotifica - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "getElencoNotificheEntitaByIdNotifica - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) 
      {
        SolmrLogger.error(this, "getElencoNotificheEntitaByIdNotifica - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vNotificheEntita;
  }
  
  
  /**
   * ritorna true se il ruolo passato può
   * chiudere la notifica con la ctegoria in questione.
   * 
   * 
   * 
   * @param codiceRuolo
   * @param idCategoriaNotifica
   * @return
   * @throws DataAccessException
   */
  public boolean isChiusuraNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica) 
      throws DataAccessException
  {
    boolean isModificabile = false;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String query = 
         "SELECT TNR.ID_TIPO_NOTIFICA_RUOLO " +
         "FROM   DB_R_TIPO_NOTIFICA_RUOLO TNR, " +
         "       DB_D_TIPO_RUOLO TR " +
         "WHERE  TR.RUOLO = ? " +
         "AND    TR.ID_TIPO_RUOLO = TNR.EXT_ID_TIPO_RUOLO " +
         "AND    TNR.ID_CATEGORIA_NOTIFICA = ? ";
      if(ruoloUtenza.isReadWrite())
      {
        query += ""+ 
          "AND    (TNR.ACCESSO_RW_CHIUSURA = 'S' OR TNR.ACCESSO_RW_CHIUSURA = 'N') ";
      }
      else
      {
        query += ""+ 
          "AND    TNR.ACCESSO_RW_CHIUSURA = 'N' ";
      }


      SolmrLogger.debug(this, "Executing query isChiusuraNotificaRuoloPossibile: " + query);

      stmt = conn.prepareStatement(query);

      int indice = 0;
      stmt.setString(++indice, ruoloUtenza.getCodiceRuolo());
      stmt.setLong(++indice, idCategoriaNotifica);

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {
        isModificabile = true;        
      }

      rs.close();
      stmt.close();


    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "isChiusuraNotificaRuoloPossibile - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "isChiusuraNotificaRuoloPossibile - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "isChiusuraNotificaRuoloPossibile - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) 
      {
        SolmrLogger.error(this, "isChiusuraNotificaRuoloPossibile - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    return isModificabile;
  }
  
  
  /**
   * 
   * ritorna true se il ruolo passato può
   * modificare la notifica con la ctegoria in questione.
   * 
   * 
   * 
   * 
   * @param ruoloUtenza
   * @param idCategoriaNotifica
   * @return
   * @throws DataAccessException
   */
  public boolean isModificaNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica) 
      throws DataAccessException
  {
    boolean isModificabile = false;
    Connection conn = null;
    PreparedStatement stmt = null;
    try 
    {
      conn = getDatasource().getConnection();

      String query = 
         "SELECT TNR.ID_TIPO_NOTIFICA_RUOLO " +
         "FROM   DB_R_TIPO_NOTIFICA_RUOLO TNR, " +
         "       DB_D_TIPO_RUOLO TR " +
         "WHERE  TR.RUOLO = ? " +
         "AND    TR.ID_TIPO_RUOLO = TNR.EXT_ID_TIPO_RUOLO " +
         "AND    TNR.ID_CATEGORIA_NOTIFICA = ? ";
      if(ruoloUtenza.isReadWrite())
      {
        query += ""+ 
          "AND    (TNR.ACCESSO_RW = 'S' OR TNR.ACCESSO_RW = 'N') ";
      }
      else
      {
        query += ""+ 
          "AND    TNR.ACCESSO_RW = 'N' ";
      }


      SolmrLogger.debug(this, "Executing query isModificaNotificaRuoloPossibile: " + query);

      stmt = conn.prepareStatement(query);

      int indice = 0;
      stmt.setString(++indice, ruoloUtenza.getCodiceRuolo());
      stmt.setLong(++indice, idCategoriaNotifica);

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) 
      {
        isModificabile = true;        
      }

      rs.close();
      stmt.close();


    }
    catch(SQLException exc) 
    {
      SolmrLogger.error(this, "isModificaNotificaRuoloPossibile - SQLException: "+exc);
      throw new DataAccessException(exc.getMessage());
    }
    catch(Exception ex) 
    {
      SolmrLogger.error(this, "isModificaNotificaRuoloPossibile - Generic Exception: "+ex);
      throw new DataAccessException(ex.getMessage());
    }
    finally 
    {
      try 
      {
        if(stmt != null) stmt.close();
        if(conn != null) conn.close();
      }
      catch(SQLException exc) 
      {
        SolmrLogger.error(this, "isModificaNotificaRuoloPossibile - SQLException while closing Statement and Connection: "+exc);
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) 
      {
        SolmrLogger.error(this, "isModificaNotificaRuoloPossibile - Generic Exception while closing Statement and Connection: "+ex);
        throw new DataAccessException(ex.getMessage());
      }
    }
    return isModificabile;
  }
  
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaUvFromIdNotifica(
      long ids[]) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    NotificaEntitaVO notificaEntitaVO = null;
    Vector<NotificaEntitaVO> vNotificaEntita = null;
    HashMap<Long,Vector<NotificaEntitaVO>> hResult = null;
    try
    {
      SolmrLogger.debug(this,
          "[NotificaDAO::getNotificheEntitaFromIdNotifica] BEGIN.");
      
      
      StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      watcher.start();

      conn = getDatasource().getConnection();

      Long idJavaIns = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_JAVA_INSERT);

      String insert="INSERT INTO SMRGAA_W_JAVA_INSERT (ID_JAVA_INSERT, ID_DETTAGLIO_INS) "+
                    "VALUES ( ?, ?) ";

      stmt = conn.prepareStatement(insert);

      for (int i=0;i<ids.length;i++)
      {
        stmt.setLong(1,idJavaIns.longValue());
        stmt.setLong(2,ids[i]);
        stmt.addBatch();
      }
      stmt.executeBatch();

      watcher.dumpElapsed("NotificaDAO", "getNotificheEntitaFromIdNotifica", "Esecuzione query insert", null);

      SolmrLogger.debug(this,"[NotificaDAO::getNotificheEntitaFromIdNotifica] insert executed");

      stmt.close();  
      

      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "WITH ENTITA AS " +
        "      (SELECT NE.ID_NOTIFICA_ENTITA, " +
        "              NE.ID_NOTIFICA, " +
        "              NE.ID_TIPO_ENTITA, " +
        "              NE.IDENTIFICATIVO, " +
        "              NE.NOTE, " +
        "              NE.ID_DICHIARAZIONE_CONSISTENZA, " +
        "              NE.NOTE_CHIUSURA_ENTITA, " +
        "              NE.DATA_FINE_VALIDITA, " +
        "              NE.ID_UTENTE_AGGIORNAMENTO " +
        "       FROM DB_NOTIFICA_ENTITA NE, " +
        "            SMRGAA_W_JAVA_INSERT WIJ, " +
        "            DB_TIPO_ENTITA TE " +
        "       WHERE WIJ.ID_JAVA_INSERT = ? " +
        "       AND   WIJ.ID_DETTAGLIO_INS = NE.ID_NOTIFICA " +
        "       AND   NE.DATA_FINE_VALIDITA IS NULL " +
        "       AND   NE.ID_TIPO_ENTITA = TE.ID_TIPO_ENTITA " +
        "       AND   TE.CODICE_TIPO_ENTITA = 'U' " +
        "       UNION " +
        "       SELECT NE.ID_NOTIFICA_ENTITA, " +
        "              NE.ID_NOTIFICA, " +
        "              NE.ID_TIPO_ENTITA, " +
        "              NE.IDENTIFICATIVO, " +
        "              NE.NOTE, " +
        "              NE.ID_DICHIARAZIONE_CONSISTENZA, " +
        "              NE.NOTE_CHIUSURA_ENTITA, " +
        "              NE.DATA_FINE_VALIDITA, " +
        "              NE.ID_UTENTE_AGGIORNAMENTO " +
        "       FROM   DB_NOTIFICA_ENTITA NE, " +
        "              SMRGAA_W_JAVA_INSERT WIJ, " +
        "              DB_TIPO_ENTITA TE " +
        "       WHERE WIJ.ID_JAVA_INSERT = ? " +
        "       AND   WIJ.ID_DETTAGLIO_INS = NE.ID_NOTIFICA " +
        "       AND   NE.ID_TIPO_ENTITA = TE.ID_TIPO_ENTITA " +
        "       AND   TE.CODICE_TIPO_ENTITA = 'U' " +
        "       AND NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
        "                                    FROM   DB_NOTIFICA_ENTITA NE1 " +
        "                                    WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
        "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
        "                                    AND    NE1.DATA_FINE_VALIDITA IS NOT NULL) " +
        "                                    AND    NE.IDENTIFICATIVO NOT IN (SELECT NE1.IDENTIFICATIVO " +
        "                                                                     FROM   DB_NOTIFICA_ENTITA NE1 " +
        "                                                                     WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
        "                                                                     AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
        "                                                                     AND    NE1.DATA_FINE_VALIDITA IS NULL) " +
        "     ) " +
        "SELECT NE.ID_NOTIFICA_ENTITA, " +
        "       NE.ID_NOTIFICA, " +
        "       NE.ID_TIPO_ENTITA, " +
        "       NE.IDENTIFICATIVO, " +
        "       NE.NOTE, " +
        "       NE.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       NE.NOTE_CHIUSURA_ENTITA, " +
        "       NE.DATA_FINE_VALIDITA, " +
        "       COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SUA.PROGR_UNAR, " +
        "       TU.CODICE AS COD_UTILIZZO, " +
        "       TU.DESCRIZIONE AS DESC_UTILIZZO, " +
        "       TTV.CODICE AS COD_TIPOLOGIA, " +
        "       TTV.DESCRIZIONE AS DESC_TIPOLOGIA, " +
        "       SUA.ANNO_IMPIANTO, " +
        "       TV.CODICE_VARIETA, " +
        "       TV.DESCRIZIONE AS DESC_VARIETA, " +
        "       SUA.ANNO_ISCRIZIONE_ALBO, " +
        "       SUA.AREA, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DEN_UTENTE, " +
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "       AS ENTE " +
        "FROM   ENTITA NE, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_UNITA_ARBOREA_DICHIARATA UAD, " +
        "       DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       COMUNE COM, " +
        "       PROVINCIA PROV, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_TIPO_TIPOLOGIA_VINO TTV, " +
        "       DB_TIPO_VARIETA TV " +
        //"       PAPUA_V_UTENTE_LOGIN PVU " +
        "WHERE  NE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "AND    UAD.ID_STORICO_UNITA_ARBOREA = SUA.ID_STORICO_UNITA_ARBOREA " +  
        "AND    NE.IDENTIFICATIVO = SUA.ID_UNITA_ARBOREA " +
        "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA  = PROV.ISTAT_PROVINCIA " +
        "AND    UAD.ID_UTILIZZO = TU.ID_UTILIZZO (+) " +
        "AND    UAD.ID_VARIETA = TV.ID_VARIETA (+) " +
        "AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO (+) " +
        //"AND    NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
        "ORDER BY NE.ID_NOTIFICA, COM.DESCOM, SP.FOGLIO, SP.PARTICELLA, SP.SEZIONE, SP.SUBALTERNO, SUA.PROGR_UNAR ");

      
      

      //conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[NotificaDAO::getNotificheEntitaFromIdNotifica] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idJavaIns.longValue());
      stmt.setLong(2, idJavaIns.longValue());
      
      ResultSet rs = stmt.executeQuery();
      watcher.dumpElapsed("NotificaDAO", "getNotificheEntitaFromIdNotifica", "Esecuzione query select", null);
      
      Long idNotificaTmp = new Long(0);
      int i=0;
      while (rs.next())
      {
        if(hResult == null)
        {
          hResult = new HashMap<Long,Vector<NotificaEntitaVO>>();
        }
       
        Long idNotifica = checkLong(rs.getString("ID_NOTIFICA"));
        if(idNotifica.compareTo(idNotificaTmp) != 0)
        {
          if(i != 0)
          {
            hResult.put(idNotificaTmp, vNotificaEntita);
          }
          
          vNotificaEntita = new Vector<NotificaEntitaVO>();
        }
        
       
        
        notificaEntitaVO = new NotificaEntitaVO();
        notificaEntitaVO.setIdNotifica(idNotifica);
        notificaEntitaVO.setNote(rs.getString("NOTE"));
        notificaEntitaVO.setNoteChiusuraEntita(rs.getString("NOTE_CHIUSURA_ENTITA"));
        notificaEntitaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        notificaEntitaVO.setDenUtente(rs.getString("DEN_UTENTE"));
        notificaEntitaVO.setDenEnteUtente(rs.getString("ENTE"));
        
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        ComuneVO comuneParticellaVO = new ComuneVO();
        comuneParticellaVO.setDescom(rs.getString("DESCOM"));
        comuneParticellaVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));        
        storicoParticellaVO.setComuneParticellaVO(comuneParticellaVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO(); 
        storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        if(Validator.isNotEmpty(rs.getString("COD_UTILIZZO"))) 
        {
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setCodice(rs.getString("COD_UTILIZZO"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_UTILIZZO"));
          storicoUnitaArboreaVO.setTipoUtilizzoVO(tipoUtilizzoVO);
        }
        
        if(Validator.isNotEmpty(rs.getString("CODICE_VARIETA"))) 
        {
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
          storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
        }
        
        if(Validator.isNotEmpty(rs.getString("COD_TIPOLOGIA")))
        {
          TipoTipologiaVinoVO tipoTipologiaVinoVO = new TipoTipologiaVinoVO();
          tipoTipologiaVinoVO.setCodice(rs.getString("COD_TIPOLOGIA"));
          tipoTipologiaVinoVO.setDescrizione(rs.getString("DESC_TIPOLOGIA"));
          storicoUnitaArboreaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
        }
        storicoUnitaArboreaVO.setAnnoImpianto(rs.getString("ANNO_IMPIANTO"));
        storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
        storicoUnitaArboreaVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
        
        notificaEntitaVO.setStoricoParticellaVO(storicoParticellaVO);
        
        
        
        
        vNotificaEntita.add(notificaEntitaVO);
        idNotificaTmp = idNotifica;
        i++;
      }
      
      if(i > 0)
      {
        hResult.put(idNotificaTmp, vNotificaEntita);
      }
      
      
      
      return hResult;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hResult", hResult),
          new Variabile("vNotificaEntita", vNotificaEntita),
          new Variabile("notificaEntitaVO", notificaEntitaVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("ids", ids) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagrafeGaaDAO::getNotificheEntitaFromIdNotifica] ", t,
          query, variabili, parametri);
      
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[AnagrafeGaaDAO::getNotificheEntitaFromIdNotifica] END.");
    }
  }
  
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaParticellaFromIdNotifica(
      long ids[]) throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    NotificaEntitaVO notificaEntitaVO = null;
    Vector<NotificaEntitaVO> vNotificaEntita = null;
    HashMap<Long,Vector<NotificaEntitaVO>> hResult = null;
    Vector<StoricoParticellaVO>  vParticelle = null;
    StoricoParticellaVO storicoParticellaVO = null;
    Vector<ConduzioneDichiarataVO>  vConduzioni = null;
    ConduzioneDichiarataVO conduzioneDichiarataVO = null;
    Vector<UtilizzoDichiaratoVO>  vUtilizzi = null;
    UtilizzoDichiaratoVO utilizzoDichiaratoVO = null;
    try
    {
      SolmrLogger.debug(this,
          "[NotificaDAO::getNotificheEntitaParticellaFromIdNotifica] BEGIN.");
      
      
      StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
      watcher.start();

      conn = getDatasource().getConnection();

      Long idJavaIns = getNextPrimaryKey(SolmrConstants.SEQ_SMRGAA_W_JAVA_INSERT);

      String insert="INSERT INTO SMRGAA_W_JAVA_INSERT (ID_JAVA_INSERT, ID_DETTAGLIO_INS) "+
                    "VALUES ( ?, ?) ";

      stmt = conn.prepareStatement(insert);

      for (int i=0;i<ids.length;i++)
      {
        stmt.setLong(1,idJavaIns.longValue());
        stmt.setLong(2,ids[i]);
        stmt.addBatch();
      }
      stmt.executeBatch();

      watcher.dumpElapsed("NotificaDAO", "getNotificheEntitaParticellaFromIdNotifica", "Esecuzione query insert", null);

      SolmrLogger.debug(this,"[NotificaDAO::getNotificheEntitaParticellaFromIdNotifica] insert executed");

      stmt.close();  
      

      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "WITH ENTITA AS " +
        "      (SELECT NE.ID_NOTIFICA_ENTITA, " +
        "              NE.ID_NOTIFICA, " +
        "              NE.ID_TIPO_ENTITA, " +
        "              NE.IDENTIFICATIVO, " +
        "              NE.NOTE, " +
        "              NE.ID_DICHIARAZIONE_CONSISTENZA, " +
        "              NE.NOTE_CHIUSURA_ENTITA, " +
        "              NE.DATA_FINE_VALIDITA, " +
        "              NE.ID_UTENTE_AGGIORNAMENTO " +
        "       FROM DB_NOTIFICA_ENTITA NE, " +
        "            SMRGAA_W_JAVA_INSERT WIJ, " +
        "            DB_TIPO_ENTITA TE " +
        "       WHERE WIJ.ID_JAVA_INSERT = ? " +
        "       AND   WIJ.ID_DETTAGLIO_INS = NE.ID_NOTIFICA " +
        "       AND   NE.DATA_FINE_VALIDITA IS NULL " +
        "       AND   NE.ID_TIPO_ENTITA = TE.ID_TIPO_ENTITA " +
        "       AND   TE.CODICE_TIPO_ENTITA = 'P' " +
        "       UNION " +
        "       SELECT NE.ID_NOTIFICA_ENTITA, " +
        "              NE.ID_NOTIFICA, " +
        "              NE.ID_TIPO_ENTITA, " +
        "              NE.IDENTIFICATIVO, " +
        "              NE.NOTE, " +
        "              NE.ID_DICHIARAZIONE_CONSISTENZA, " +
        "              NE.NOTE_CHIUSURA_ENTITA, " +
        "              NE.DATA_FINE_VALIDITA, " +
        "              NE.ID_UTENTE_AGGIORNAMENTO " +
        "       FROM   DB_NOTIFICA_ENTITA NE, " +
        "              SMRGAA_W_JAVA_INSERT WIJ, " +
        "              DB_TIPO_ENTITA TE " +
        "       WHERE WIJ.ID_JAVA_INSERT = ? " +
        "       AND   WIJ.ID_DETTAGLIO_INS = NE.ID_NOTIFICA " +
        "       AND   NE.ID_TIPO_ENTITA = TE.ID_TIPO_ENTITA " +
        "       AND   TE.CODICE_TIPO_ENTITA = 'P' " +
        "       AND NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
        "                                    FROM   DB_NOTIFICA_ENTITA NE1 " +
        "                                    WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
        "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
        "                                    AND    NE1.DATA_FINE_VALIDITA IS NOT NULL) " +
        "                                    AND    NE.IDENTIFICATIVO NOT IN (SELECT NE1.IDENTIFICATIVO " +
        "                                                                     FROM   DB_NOTIFICA_ENTITA NE1 " +
        "                                                                     WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
        "                                                                     AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
        "                                                                     AND    NE1.DATA_FINE_VALIDITA IS NULL) " +
        "     ) " +
        "SELECT NE.ID_NOTIFICA_ENTITA, " +
        "       NE.ID_NOTIFICA, " +
        "       NE.ID_TIPO_ENTITA, " +
        "       NE.IDENTIFICATIVO, " +
        "       NE.NOTE, " +
        "       NE.ID_DICHIARAZIONE_CONSISTENZA, " +
        "       NE.NOTE_CHIUSURA_ENTITA, " +
        "       NE.DATA_FINE_VALIDITA, " +
        "       COM.DESCOM, " +
        "       PROV.SIGLA_PROVINCIA, " +
        "       SP.ID_STORICO_PARTICELLA, " +
        "       SP.ID_PARTICELLA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       UD.ID_UTILIZZO_DICHIARATO, " +
        "       UD.ID_VARIETA, " +
        "       TVAR.DESCRIZIONE AS DESC_VARIETA, " +
        "       TVAR.CODICE_VARIETA AS COD_VARIETA, " +
        "       UD.ID_UTILIZZO, " +
        "       UD.SUPERFICIE_UTILIZZATA, " +
        "       TU.CODICE AS COD_UTILIZZO, " +
        "       TU.DESCRIZIONE AS DESC_UTILIZZO, " +
        "       CD.ID_CONDUZIONE_DICHIARATA, " +
        "       CD.ID_TITOLO_POSSESSO, " +
        "       CD.PERCENTUALE_POSSESSO, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " + 
        "       AS DEN_UTENTE, " +
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "       AS ENTE " +
        "FROM   ENTITA NE, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_UTILIZZO_DICHIARATO UD, " +
        "       DB_STORICO_PARTICELLA SP, " +
        "       COMUNE COM, " +
        "       PROVINCIA PROV, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_TIPO_VARIETA TVAR " +
        //"       PAPUA_V_UTENTE_LOGIN PVU " +
        "WHERE  NE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "AND    NE.IDENTIFICATIVO = SP.ID_PARTICELLA " +
        "AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    CD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "AND    CD.ID_CONDUZIONE_DICHIARATA = UD.ID_CONDUZIONE_DICHIARATA (+) " +  
        "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
        "AND    COM.ISTAT_PROVINCIA  = PROV.ISTAT_PROVINCIA " +
        "AND    UD.ID_UTILIZZO = TU.ID_UTILIZZO (+) " +
        "AND    UD.ID_VARIETA = TVAR.ID_VARIETA (+) " +
        //"AND    NE.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
        "ORDER BY NE.ID_NOTIFICA, " +
        "         COM.DESCOM, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA, " +
        "         SP.SEZIONE, " +
        "         SP.SUBALTERNO, " +
        "         CD.ID_TITOLO_POSSESSO, " +
        "         CD.ID_CONDUZIONE_DICHIARATA, " +
        "         TU.DESCRIZIONE ASC, " +
        "         TVAR.DESCRIZIONE ASC ");

      
      

      //conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[NotificaDAO::getNotificheEntitaParticellaFromIdNotifica] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      stmt.setLong(1, idJavaIns.longValue());
      stmt.setLong(2, idJavaIns.longValue());
      
      ResultSet rs = stmt.executeQuery();
      watcher.dumpElapsed("NotificaDAO", "getNotificheEntitaParticellaFromIdNotifica", "Esecuzione query select", null);
      
      Long idNotificaTmp = new Long(0);
      long idParticellaTmp = 0;
      long idConduzioneTmp = 0;
      Long idUtilizzo = null;
      int i=0;
      while (rs.next())
      {
        if(hResult == null)
        {
          hResult = new HashMap<Long,Vector<NotificaEntitaVO>>();
        }
       
        Long idNotifica = checkLong(rs.getString("ID_NOTIFICA"));
        if(idNotifica.compareTo(idNotificaTmp) != 0)
        {
          if(i != 0)
          {
            vConduzioni.add(conduzioneDichiarataVO);
            storicoParticellaVO.setvConduzioniDichiarate(vConduzioni);
            notificaEntitaVO.setStoricoParticellaVO(storicoParticellaVO);
            vNotificaEntita.add(notificaEntitaVO);
            hResult.put(idNotificaTmp, vNotificaEntita);
          }
          idParticellaTmp = 0;
          idConduzioneTmp = 0;
          
          vNotificaEntita = new Vector<NotificaEntitaVO>();
        }
        
        
        long idParticella = rs.getLong("ID_PARTICELLA");
        long idConduzione = rs.getLong("ID_CONDUZIONE_DICHIARATA");
        idUtilizzo = checkLongNull(rs.getString("ID_UTILIZZO_DICHIARATO"));
        if(idParticellaTmp != idParticella)
        {
          if(idParticellaTmp != 0)
          {
            vConduzioni.add(conduzioneDichiarataVO);
            storicoParticellaVO.setvConduzioniDichiarate(vConduzioni);
            notificaEntitaVO.setStoricoParticellaVO(storicoParticellaVO);
            vNotificaEntita.add(notificaEntitaVO);
            idConduzioneTmp = 0;
          }          
          vConduzioni = new Vector<ConduzioneDichiarataVO>();
          
          notificaEntitaVO = new NotificaEntitaVO();
          notificaEntitaVO.setIdNotifica(idNotifica);
          notificaEntitaVO.setNote(rs.getString("NOTE"));
          notificaEntitaVO.setNoteChiusuraEntita(rs.getString("NOTE_CHIUSURA_ENTITA"));
          notificaEntitaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
          notificaEntitaVO.setDenUtente(rs.getString("DEN_UTENTE"));
          notificaEntitaVO.setDenEnteUtente(rs.getString("ENTE"));
          
          storicoParticellaVO = new StoricoParticellaVO();
          storicoParticellaVO.setIdParticella(new Long(idParticella));
          storicoParticellaVO.setIdStoricoParticella(new Long(rs.getLong("ID_STORICO_PARTICELLA")));
          ComuneVO comuneParticellaVO = new ComuneVO();
          comuneParticellaVO.setDescom(rs.getString("DESCOM"));
          comuneParticellaVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
          storicoParticellaVO.setComuneParticellaVO(comuneParticellaVO);
          storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
          storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
          storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
          storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));          
          
        }
        
        if(idConduzioneTmp != idConduzione)
        {            
          if(idConduzioneTmp !=0)
          {
            conduzioneDichiarataVO.setvUtilizzi(vUtilizzi);
            vConduzioni.add(conduzioneDichiarataVO);
          }
          if(Validator.isNotEmpty(idUtilizzo))
          {
            vUtilizzi = new Vector<UtilizzoDichiaratoVO>();
          }
          
          conduzioneDichiarataVO = new ConduzioneDichiarataVO();
          conduzioneDichiarataVO.setIdConduzioneDichiarata(new Long(rs.getLong("ID_CONDUZIONE_DICHIARATA")));
          conduzioneDichiarataVO.setIdTitoloPossesso(new Long(rs.getLong("ID_TITOLO_POSSESSO")));
          conduzioneDichiarataVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
          
        }
        
        if(Validator.isNotEmpty(idUtilizzo))
        {
          utilizzoDichiaratoVO = new UtilizzoDichiaratoVO();
          utilizzoDichiaratoVO.setIdUtilizzoDichiarato(idUtilizzo);
          TipoUtilizzoVO tipoUtilizzoVO = new TipoUtilizzoVO();
          tipoUtilizzoVO.setCodice(rs.getString("COD_UTILIZZO"));
          tipoUtilizzoVO.setDescrizione(rs.getString("DESC_UTILIZZO"));
          utilizzoDichiaratoVO.setTipoUtilizzoVO(tipoUtilizzoVO);
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VARIETA"));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          utilizzoDichiaratoVO.setTipoVarietaVO(tipoVarietaVO);
          utilizzoDichiaratoVO.setSupUtilizzataBg(rs.getBigDecimal("SUPERFICIE_UTILIZZATA"));
          
          if(conduzioneDichiarataVO.getvUtilizzi() != null)
          {  
            vUtilizzi = conduzioneDichiarataVO.getvUtilizzi(); 
          }
          vUtilizzi.add(utilizzoDichiaratoVO);
          conduzioneDichiarataVO.setvUtilizzi(vUtilizzi);
          
        }
       
        idNotificaTmp = idNotifica;
        idParticellaTmp = idParticella;
        idConduzioneTmp = idConduzione;
        i++;
      
      }
      
      if(i > 0)
      {
        vConduzioni.add(conduzioneDichiarataVO);
        storicoParticellaVO.setvConduzioniDichiarate(vConduzioni);
        notificaEntitaVO.setStoricoParticellaVO(storicoParticellaVO);
        vNotificaEntita.add(notificaEntitaVO);
        hResult.put(idNotificaTmp, vNotificaEntita);
      }
      
      
      
      return hResult;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query),
          new Variabile("hResult", hResult),
          new Variabile("vNotificaEntita", vNotificaEntita), new Variabile("notificaEntitaVO", notificaEntitaVO), 
          new Variabile("vParticelle", vParticelle), new Variabile("storicoParticellaVO", storicoParticellaVO),
          new Variabile("vConduzioni", vConduzioni), new Variabile("conduzioneDichiarataVO", conduzioneDichiarataVO),
          new Variabile("vConduzioni", vUtilizzi), new Variabile("utilizzoDichiaratoVO", utilizzoDichiaratoVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("ids", ids) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[AnagrafeGaaDAO::getNotificheEntitaParticellaFromIdNotifica] ", t,
          query, variabili, parametri);
      
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      close(null, stmt, conn);

      // Fine metodo
      SolmrLogger.debug(this,
          "[AnagrafeGaaDAO::getNotificheEntitaParticellaFromIdNotifica] END.");
    }
  }


}
