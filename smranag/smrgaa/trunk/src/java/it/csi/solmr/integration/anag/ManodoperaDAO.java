package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.DettaglioAttivitaVO;
import it.csi.solmr.dto.anag.DettaglioManodoperaVO;
import it.csi.solmr.dto.anag.ManodoperaVO;
import it.csi.solmr.dto.anag.TipoFormaConduzioneVO;
import it.csi.solmr.dto.anag.TipoIscrizioneINPSVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.NotFoundException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.integration.BaseDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Nadia B.
 * @version 1.0
 */

public class ManodoperaDAO
    extends BaseDAO {

  public ManodoperaDAO(String refName) throws ResourceAccessException {
    super(refName);
  }

  /**
   * Ricerca le dichiarazioni relative alla manodopera associata a un'azienda
   * in base al tipo classi manodopera passato in input
   * @param manodoperaVO
   * @param vCodeTipoClassiManodopera
   * @return Vector
   * @throws DataAccessException
   */
  public Vector<DettaglioManodoperaVO> getManodoperaAnnua(
      ManodoperaVO manodoperaVO, Vector<String> vCodeTipoClassiManodopera) 
    throws DataAccessException 
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    Vector<DettaglioManodoperaVO> vDettaglioManodopera = null;
    DettaglioManodoperaVO dettaglioManodoperaVO = null;
    java.util.Date dataInizioAnno = null;
    java.util.Date dataFineAnno = null;
    String sWhereCodeTipoClassiManodopera = "";
    int indiceStatement = 0;

    String anno = null;

    try 
    {
      anno = "" + DateUtils.extractYearFromDate(manodoperaVO.getDataSituazioneAlDate());
    }
    catch (Exception ex) 
    {
      anno = DateUtils.getCurrentYear().toString();
    }

    String dataInizioAnnoStr = "31/12/" + anno;
    String dataFineAnnoStr = "01/01/" + anno;

    try 
    {
      dataInizioAnno = Validator.parseDate(dataInizioAnnoStr, "dd/MM/yyyy");
      dataFineAnno = Validator.parseDate(dataFineAnnoStr, "dd/MM/yyyy");
      SolmrLogger.debug(this, "manodoperaVO.getDataSituazioneAl(): " + manodoperaVO.getDataSituazioneAl() + " dataInizioAnno: " + dataInizioAnno + " dataFineAnno: " + dataFineAnno);
    }
    catch (ParseException ex) 
    {
      SolmrLogger.debug(this, "Date Inizio/Fine non parsificabili");
    }

    try 
    {
      conn = getDatasource().getConnection();

      //viene composta dinamicamente la condizione where per ottenere le somme
      //in base alle varie combinazioni di tipo classi manodopera
      if (vCodeTipoClassiManodopera != null && vCodeTipoClassiManodopera.size() > 1)
      {
        for (int i = 1; i < vCodeTipoClassiManodopera.size(); i++)
        {
           sWhereCodeTipoClassiManodopera = " OR TIP_CL_MAN.CODICE = ? ";
        }
      }
      
      
      
      String search =
          "WITH MAN_CLASSI  AS " +
          " (SELECT MAN.ID_MANODOPERA, " +
          "         SUM (DETT_MAN.UOMINI) TOT_UOMINI, " +
          "         SUM (DETT_MAN.DONNE) TOT_DONNE " +
          "  FROM DB_TIPO_CLASSI_MANODOPERA TIP_CL_MAN, " +
          "       DB_DETTAGLIO_MANODOPERA DETT_MAN, " +
          "       DB_MANODOPERA MAN " +
          "  WHERE  MAN.ID_AZIENDA = ? " +
          "  AND    MAN.DATA_INIZIO_VALIDITA  <= ? " +
          "  AND   (MAN.DATA_FINE_VALIDITA IS NULL OR MAN.DATA_FINE_VALIDITA >= ?) " +
          "  AND (TIP_CL_MAN.CODICE = ? " + sWhereCodeTipoClassiManodopera + ")" +
          "  AND MAN.ID_MANODOPERA = DETT_MAN.ID_MANODOPERA " +
          "  AND DETT_MAN.ID_CLASSE_MANODOPERA = TIP_CL_MAN.ID_CLASSE_MANODOPERA " +
          "  GROUP BY MAN.ID_MANODOPERA) " +
          "SELECT MAN.ID_MANODOPERA, " +
          "       MAN.CODICE_INPS, " +
          "       MAN.MATRICOLA_INAIL, " +
          "       MCL.TOT_UOMINI, " +
          "       MCL.TOT_DONNE, " +
          "       MAN.DATA_INIZIO_VALIDITA, " +
          "       MAN.DATA_FINE_VALIDITA, " +
          "       MAN.ID_FORMA_CONDUZIONE, " +
          "       TIP_FO_COND.DESCRIZIONE DES_TIP_FORMA_CONDUZ " +
          "FROM   DB_MANODOPERA MAN, " +
          "       MAN_CLASSI MCL, " +
          "       DB_TIPO_FORMA_CONDUZIONE TIP_FO_COND " +
          "WHERE  MAN.ID_AZIENDA = ? " +
          "AND    MAN.ID_MANODOPERA = MCL.ID_MANODOPERA(+) " +
          "AND    MAN.ID_FORMA_CONDUZIONE = TIP_FO_COND.ID_FORMA_CONDUZIONE(+) " +
          "AND    MAN.DATA_INIZIO_VALIDITA  <= ? " +
          "AND   (MAN.DATA_FINE_VALIDITA IS NULL OR MAN.DATA_FINE_VALIDITA >= ?) " +
          "ORDER BY MAN.DATA_INIZIO_VALIDITA DESC ";

      /*String search =
        "SELECT MAN.ID_MANODOPERA, " +
        "       MAN.CODICE_INPS, " +
        "       MAN.MATRICOLA_INAIL, " +
        "       SUM(DETT_MAN.UOMINI) TOT_UOMINI, " +
        "       SUM(DETT_MAN.DONNE) TOT_DONNE, " +
        "       MAN.DATA_INIZIO_VALIDITA, " +
        "       MAN.DATA_FINE_VALIDITA, " +
        "       MAN.ID_FORMA_CONDUZIONE, " +
        "       TIP_FO_COND.DESCRIZIONE DES_TIP_FORMA_CONDUZ " +
        "FROM   DB_MANODOPERA MAN, " +
        "       DB_DETTAGLIO_MANODOPERA DETT_MAN, " +
        "       DB_TIPO_CLASSI_MANODOPERA TIP_CL_MAN, " +
        "       DB_TIPO_FORMA_CONDUZIONE TIP_FO_COND " +
        "WHERE  MAN.ID_AZIENDA = ? " +
        "AND    MAN.DATA_INIZIO_VALIDITA  <= ? " +
        "AND   (MAN.DATA_FINE_VALIDITA IS NULL OR MAN.DATA_FINE_VALIDITA >= ?) " +
        "AND (TIP_CL_MAN.CODICE = ? " + sWhereCodeTipoClassiManodopera + ")" +
        "AND MAN.ID_MANODOPERA = DETT_MAN.ID_MANODOPERA (+) " +
        "AND DETT_MAN.ID_CLASSE_MANODOPERA = TIP_CL_MAN.ID_CLASSE_MANODOPERA (+) " +
        "AND MAN.ID_FORMA_CONDUZIONE = TIP_FO_COND.ID_FORMA_CONDUZIONE (+) " +
        "GROUP BY MAN.ID_MANODOPERA, " +
        "         MAN.CODICE_INPS, " +
        "         MAN.MATRICOLA_INAIL, " +
        "      MAN.DATA_INIZIO_VALIDITA, " +
        "      MAN.DATA_FINE_VALIDITA, " +
          " MAN.ID_FORMA_CONDUZIONE, TIP_FO_COND.DESCRIZIONE " +
          " ORDER BY MAN.DATA_INIZIO_VALIDITA DESC ";*/

      stmt = conn.prepareStatement(search);

      stmt.setBigDecimal(++indiceStatement, convertLongToBigDecimal(manodoperaVO.getIdAziendaLong()));
      stmt.setDate(++indiceStatement, this.convertSqlDate(dataInizioAnno));
      stmt.setDate(++indiceStatement, this.convertSqlDate(dataFineAnno));
      

      SolmrLogger.debug(this, "idAzienda: " + manodoperaVO.getIdAziendaLong() + " dataInizioAnno: " + dataInizioAnno + " dataFineAnno: " + dataFineAnno);
      SolmrLogger.debug(this, "vCodeTipoClassiManodopera.size(): " + vCodeTipoClassiManodopera.size());
      for (int i = 0; i < vCodeTipoClassiManodopera.size(); i++) {
        SolmrLogger.debug(this, "vCodeTipoClassiManodopera.get(i): " + vCodeTipoClassiManodopera.get(i));
        stmt.setString(++indiceStatement,
                       (String) vCodeTipoClassiManodopera.get(i));
        SolmrLogger.debug(this, "indiceStatement: " + indiceStatement);
      }
      
      stmt.setBigDecimal(++indiceStatement, convertLongToBigDecimal(manodoperaVO.getIdAziendaLong()));
      stmt.setDate(++indiceStatement, this.convertSqlDate(dataInizioAnno));
      stmt.setDate(++indiceStatement, this.convertSqlDate(dataFineAnno));

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      vDettaglioManodopera = new Vector<DettaglioManodoperaVO>();

      while (rs.next()) {
        dettaglioManodoperaVO = new DettaglioManodoperaVO();

        dettaglioManodoperaVO.setIdManodoperaLong(checkLongNull(rs.getString(
            "ID_MANODOPERA")));
        dettaglioManodoperaVO.setCodiceInps(rs.getString("CODICE_INPS"));
        dettaglioManodoperaVO.setMatricolaInail(rs.getString("MATRICOLA_INAIL"));
        dettaglioManodoperaVO.setDonneLong(checkLong(rs.getString("TOT_DONNE")));
        dettaglioManodoperaVO.setUominiLong(checkLong(rs.getString(
            "TOT_UOMINI")));
        dettaglioManodoperaVO.setDataInizioValiditaDate(this.checkDate(rs.
            getDate("DATA_INIZIO_VALIDITA")));
        dettaglioManodoperaVO.setDataFineValiditaDate(this.checkDate(rs.
            getDate("DATA_FINE_VALIDITA")));

        TipoFormaConduzioneVO tipoFormaConduzioneVO = new TipoFormaConduzioneVO();
        tipoFormaConduzioneVO.setIdFormaConduzioneLong(checkLongNull(rs.
            getString("ID_FORMA_CONDUZIONE")));
        tipoFormaConduzioneVO.setDescrizione(rs.getString(
            "DES_TIP_FORMA_CONDUZ"));
        dettaglioManodoperaVO.setTipoFormaConduzioneVO(tipoFormaConduzioneVO);

        vDettaglioManodopera.add(dettaglioManodoperaVO);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
                       "getManodoperaAnnua - Executed query - Found " +
                       vDettaglioManodopera.size() + " result(s).");
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "getManodoperaAnnua - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
                        "getManodoperaAnnua - Generic Exception: " +
                        ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
                          "getManodoperaAnnua - SQLException while closing Statement and Connection: " +
                          exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
                          "getManodoperaAnnua - Generic Exception while closing Statement and Connection: " +
                          ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vDettaglioManodopera;
  }
  
  
  /**
   * Ricerca le dichiarazioni relative alla manodopera associata a un'azienda
   * in base al tipo classi manodopera passato in input
   * @param manodoperaVO
   * @param vCodeTipoClassiManodopera
   * @return Vector
   * @throws DataAccessException
   */
  public Vector<DettaglioManodoperaVO> getManodoperaAnnua(ManodoperaVO manodoperaVO,Vector<String> vCodeTipoClassiManodopera,Long idPianoRiferimento) 
    throws DataAccessException 
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    Vector<DettaglioManodoperaVO> vDettaglioManodopera = null;
    DettaglioManodoperaVO dettaglioManodoperaVO = null;
    String sWhereCodeTipoClassiManodopera = "";
    int indiceStatement = 0;



    try 
    {
      conn = getDatasource().getConnection();

      //viene composta dinamicamente la condizione where per ottenere le somme
      //in base alle varie combinazioni di tipo classi manodopera
      if (vCodeTipoClassiManodopera != null && vCodeTipoClassiManodopera.size() > 1)
      {
        for (int i = 1; i < vCodeTipoClassiManodopera.size(); i++)
        {
           sWhereCodeTipoClassiManodopera = " OR TIP_CL_MAN.CODICE = ? ";
        }
      }

      String search =
        "WITH MAN_CLASSI  AS " +
        " (SELECT MAN.ID_MANODOPERA, " +
        "         SUM (DETT_MAN.UOMINI) TOT_UOMINI, " +
        "         SUM (DETT_MAN.DONNE) TOT_DONNE " +
        "  FROM DB_TIPO_CLASSI_MANODOPERA TIP_CL_MAN, " +
        "       DB_DETTAGLIO_MANODOPERA DETT_MAN, " +
        "       DB_MANODOPERA MAN ";
      if(idPianoRiferimento.intValue() > 0) 
      {
        search +=   
        "        ,DB_DICHIARAZIONE_CONSISTENZA DC ";
      }
      search +=
        "  WHERE  MAN.ID_AZIENDA = ? " +
        "  AND (TIP_CL_MAN.CODICE = ? " + sWhereCodeTipoClassiManodopera + ")" +
        "  AND MAN.ID_MANODOPERA = DETT_MAN.ID_MANODOPERA " +
        "  AND DETT_MAN.ID_CLASSE_MANODOPERA = TIP_CL_MAN.ID_CLASSE_MANODOPERA ";
      if(idPianoRiferimento.intValue() > 0) 
      {
        search +=   
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    MAN.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(MAN.DATA_FINE_VALIDITA, ?) > DC.DATA_INSERIMENTO_DICHIARAZIONE ";
      }
      else
      {
        search +=
        "         AND MAN.DATA_FINE_VALIDITA IS NULL ";
      }
      search +=
        "  GROUP BY MAN.ID_MANODOPERA) " +
        "SELECT MAN.ID_MANODOPERA, " +
        "         MAN.CODICE_INPS, " +
        "         MAN.MATRICOLA_INAIL, " +
        "         MCL.TOT_UOMINI, " +
        "         MCL.TOT_DONNE, " +
        "         MAN.DATA_INIZIO_VALIDITA, " +
        "         MAN.DATA_FINE_VALIDITA, " +
        "         MAN.ID_FORMA_CONDUZIONE, " +
        "         TIP_FO_COND.DESCRIZIONE DES_TIP_FORMA_CONDUZ " +
        "    FROM DB_MANODOPERA MAN, " +
        "         MAN_CLASSI MCL, " +
        "         DB_TIPO_FORMA_CONDUZIONE TIP_FO_COND ";
        if(idPianoRiferimento.intValue() > 0) 
        {
          search +=   
          "        ,DB_DICHIARAZIONE_CONSISTENZA DC ";
        }
      search +=
        "   WHERE     MAN.ID_AZIENDA = ? " +
        "         AND MAN.ID_MANODOPERA = MCL.ID_MANODOPERA(+) " +
        "         AND MAN.ID_FORMA_CONDUZIONE = TIP_FO_COND.ID_FORMA_CONDUZIONE(+) ";
      if(idPianoRiferimento.intValue() > 0) 
      {
        search +=   
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    MAN.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(MAN.DATA_FINE_VALIDITA, ?) > DC.DATA_INSERIMENTO_DICHIARAZIONE ";
      }
      else
      {
        search +=
        "         AND MAN.DATA_FINE_VALIDITA IS NULL ";
      }
      search +=
        "ORDER BY MAN.DATA_INIZIO_VALIDITA DESC ";
          
          
          
      /*  "SELECT MAN.ID_MANODOPERA, " +
        "       MAN.CODICE_INPS, " +
        "       MAN.MATRICOLA_INAIL, " +
        "       SUM(DETT_MAN.UOMINI) TOT_UOMINI, " +
        "       SUM(DETT_MAN.DONNE) TOT_DONNE, " +
        "       MAN.DATA_INIZIO_VALIDITA, " +
        "       MAN.DATA_FINE_VALIDITA, " +
        "       MAN.ID_FORMA_CONDUZIONE, " +
        "       TIP_FO_COND.DESCRIZIONE DES_TIP_FORMA_CONDUZ " +
        "FROM   DB_MANODOPERA MAN, " +
        "       DB_DETTAGLIO_MANODOPERA DETT_MAN, " +
        "       DB_TIPO_CLASSI_MANODOPERA TIP_CL_MAN, " +
        "       DB_TIPO_FORMA_CONDUZIONE TIP_FO_COND ";
      if(idPianoRiferimento.intValue() > 0) 
      {
        search +=   
        "        ,DB_DICHIARAZIONE_CONSISTENZA DC ";
      }
      search +=
        "WHERE MAN.ID_AZIENDA = ? " +
        "AND (TIP_CL_MAN.CODICE = ? " + sWhereCodeTipoClassiManodopera + ")" +
        "AND MAN.ID_MANODOPERA = DETT_MAN.ID_MANODOPERA (+) " +
        "AND DETT_MAN.ID_CLASSE_MANODOPERA = TIP_CL_MAN.ID_CLASSE_MANODOPERA (+)" +
        "AND MAN.ID_FORMA_CONDUZIONE = TIP_FO_COND.ID_FORMA_CONDUZIONE (+) ";
      if(idPianoRiferimento.intValue() > 0) 
      {
        search +=   
        "AND    DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    MAN.DATA_INIZIO_VALIDITA < DC.DATA_INSERIMENTO_DICHIARAZIONE " +
        "AND    NVL(MAN.DATA_FINE_VALIDITA, ?) > DC.DATA_INSERIMENTO_DICHIARAZIONE ";
      }
      else 
      {
        search +=   
        "AND    MAN.DATA_FINE_VALIDITA IS NULL ";
      }
      search+=
        "GROUP BY MAN.ID_MANODOPERA, " +
        "         MAN.CODICE_INPS, " +
        "         MAN.MATRICOLA_INAIL, " +
        "         MAN.DATA_INIZIO_VALIDITA, " +
        "         MAN.DATA_FINE_VALIDITA, " +
        "         MAN.ID_FORMA_CONDUZIONE, " +
        "         TIP_FO_COND.DESCRIZIONE "+
        "ORDER BY MAN.DATA_INIZIO_VALIDITA DESC ";*/

      stmt = conn.prepareStatement(search);

      stmt.setBigDecimal(++indiceStatement, convertLongToBigDecimal(manodoperaVO.getIdAziendaLong()));

      SolmrLogger.debug(this, "idAzienda: " + manodoperaVO.getIdAziendaLong() + " idPianoRiferimento: " + idPianoRiferimento);
      SolmrLogger.debug(this, "vCodeTipoClassiManodopera.size(): " + vCodeTipoClassiManodopera.size());
      for (int i = 0; i < vCodeTipoClassiManodopera.size(); i++) 
      {
        SolmrLogger.debug(this, "vCodeTipoClassiManodopera.get(i): " + vCodeTipoClassiManodopera.get(i));
        stmt.setString(++indiceStatement,
                       (String) vCodeTipoClassiManodopera.get(i));
        SolmrLogger.debug(this, "indiceStatement: " + indiceStatement);
      }
      if(idPianoRiferimento.intValue() > 0) 
      {
        stmt.setLong(++indiceStatement, idPianoRiferimento.longValue());
        stmt.setDate(++indiceStatement, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      }
      
      stmt.setBigDecimal(++indiceStatement, convertLongToBigDecimal(manodoperaVO.getIdAziendaLong()));

      SolmrLogger.debug(this, "idAzienda: " + manodoperaVO.getIdAziendaLong() + " idPianoRiferimento: " + idPianoRiferimento);
      if(idPianoRiferimento.intValue() > 0) 
      {
        stmt.setLong(++indiceStatement, idPianoRiferimento.longValue());
        stmt.setDate(++indiceStatement, new java.sql.Date(DateUtils.parseDate(SolmrConstants.ORACLE_FINAL_DATE).getTime()));
      }

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      vDettaglioManodopera = new Vector<DettaglioManodoperaVO>();

      while (rs.next()) {
        dettaglioManodoperaVO = new DettaglioManodoperaVO();

        dettaglioManodoperaVO.setIdManodoperaLong(checkLongNull(rs.getString(
            "ID_MANODOPERA")));
        dettaglioManodoperaVO.setCodiceInps(rs.getString("CODICE_INPS"));
        dettaglioManodoperaVO.setMatricolaInail(rs.getString("MATRICOLA_INAIL"));
        dettaglioManodoperaVO.setDonneLong(checkLong(rs.getString("TOT_DONNE")));
        dettaglioManodoperaVO.setUominiLong(checkLong(rs.getString(
            "TOT_UOMINI")));
        dettaglioManodoperaVO.setDataInizioValiditaDate(this.checkDate(rs.
            getDate("DATA_INIZIO_VALIDITA")));
        dettaglioManodoperaVO.setDataFineValiditaDate(this.checkDate(rs.
            getDate("DATA_FINE_VALIDITA")));

        TipoFormaConduzioneVO tipoFormaConduzioneVO = new TipoFormaConduzioneVO();
        tipoFormaConduzioneVO.setIdFormaConduzioneLong(checkLongNull(rs.
            getString("ID_FORMA_CONDUZIONE")));
        tipoFormaConduzioneVO.setDescrizione(rs.getString(
            "DES_TIP_FORMA_CONDUZ"));
        dettaglioManodoperaVO.setTipoFormaConduzioneVO(tipoFormaConduzioneVO);

        vDettaglioManodopera.add(dettaglioManodoperaVO);
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
                       "getManodoperaAnnua - Executed query - Found " +
                       vDettaglioManodopera.size() + " result(s).");
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "getManodoperaAnnua - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
                        "getManodoperaAnnua - Generic Exception: " +
                        ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
                          "getManodoperaAnnua - SQLException while closing Statement and Connection: " +
                          exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
                          "getManodoperaAnnua - Generic Exception while closing Statement and Connection: " +
                          ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vDettaglioManodopera;
  }
  

  /**
   * Dati manodopera, forma di conduzione e utente di aggiornamento
   * @param idManodopera
   * @return ManodoperaVO
   * @throws DataAccessException
   */
  public ManodoperaVO findManodoperaByIdManodopera(Long idManodopera) throws
      DataAccessException 
  {

    Connection conn = null;
    PreparedStatement stmt = null;

    ManodoperaVO manodoperaVO = null;

    SolmrLogger.debug(this, "idManodopera: " + idManodopera);

    try 
    {
      conn = getDatasource().getConnection();

      String search =
        "SELECT MAN.CODICE_INPS, " +
        "       MAN.MATRICOLA_INAIL, " +
        "       MAN.DATA_INIZIO_VALIDITA, " +
        "       MAN.DATA_FINE_VALIDITA, " +
        "       MAN.DATA_AGGIORNAMENTO, " +
        "       MAN.ID_UTENTE_AGGIORNAMENTO, " +
        "         (SELECT NVL (PVU.COGNOME_UTENTE, TRIM (UPPER (PVU.COGNOME_UTENTE_LOGIN))) " +
        "          || ' ' " + 
        "          || NVL (PVU.NOME_UTENTE, TRIM (UPPER (PVU.NOME_UTENTE_LOGIN))) " +
        "         FROM PAPUA_V_UTENTE_LOGIN PVU " + 
        "         WHERE MAN.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "       AS UTENTE, " +
        "       (SELECT PVU.DENOMINAZIONE " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU " +
        "        WHERE MAN.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN) " +
        "       AS DESCRIZIONE_ENTE_APPARTENENZA, " +
        "       (SELECT INTE.ID_INTERMEDIARIO  " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU,DB_INTERMEDIARIO INTE  " +
        "        WHERE MAN.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
        "        AND PVU.EXT_ID_INTERMEDIARIO = INTE.ID_INTERMEDIARIO ) AS ID_INTERMEDIARIO, " +
        "       (SELECT INTE.DENOMINAZIONE  " +
        "        FROM PAPUA_V_UTENTE_LOGIN PVU,DB_INTERMEDIARIO INTE " +  
        "        WHERE MAN.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
        "        AND PVU.EXT_ID_INTERMEDIARIO = INTE.ID_INTERMEDIARIO ) AS DENOMINAZIONE_INTERMEDIARIO, " + 
        "       TIP.ID_FORMA_CONDUZIONE, " +
        "       TIP.CODICE COD_CONDUZ, " +
        "       TIP.FORMA, " +
        "       TIP.DESCRIZIONE DES_CONDUZ, " +
        "       MAN.ID_AZIENDA," +
        "       MAN.ID_TIPO_ISCRIZIONE_INPS, " +
        "       MAN.DATA_INIZIO_ISCRIZIONE, " +
        "       MAN.DATA_CESSAZIONE_ISCRIZIONE," +
        "       TII.DESCRIZIONE AS DESC_TIPO_ISCRIZ_INPS " +
        "FROM   DB_MANODOPERA MAN, " +
        //"       PAPUA_V_UTENTE_LOGIN PVU, " +
        //"       DB_INTERMEDIARIO INTE, " +
        "       DB_TIPO_FORMA_CONDUZIONE TIP, " +
        "       DB_TIPO_ISCRIZIONE_INPS TII " +
        "WHERE  MAN.ID_MANODOPERA = ? " +
        //"AND    MAN.ID_UTENTE_AGGIORNAMENTO = PVU.ID_UTENTE_LOGIN " +
        //"AND    PVU.EXT_ID_INTERMEDIARIO = INTE.ID_INTERMEDIARIO (+) " +
        "AND    MAN.ID_FORMA_CONDUZIONE = TIP.ID_FORMA_CONDUZIONE (+) " +
        "AND    MAN.ID_TIPO_ISCRIZIONE_INPS = TII.ID_TIPO_ISCRIZIONE_INPS (+) ";

      stmt = conn.prepareStatement(search);

      stmt.setBigDecimal(1, convertLongToBigDecimal(idManodopera));

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        manodoperaVO = new ManodoperaVO();

        manodoperaVO.setIdManodoperaLong(idManodopera);
        manodoperaVO.setIdAziendaLong(checkLongNull(rs.getString(
            "ID_AZIENDA")));
        manodoperaVO.setCodiceInps(rs.getString("CODICE_INPS"));
        manodoperaVO.setMatricolaInail(rs.getString("MATRICOLA_INAIL"));
        manodoperaVO.setDataInizioValiditaDate(checkDate(rs.
            getDate("DATA_INIZIO_VALIDITA")));
        SolmrLogger.debug(this,
                          "checkDate DATA_FINE_VALIDITA: " +
                          checkDate(rs.getDate("DATA_FINE_VALIDITA")));
        manodoperaVO.setDataFineValiditaDate(checkDate(rs.
            getDate("DATA_FINE_VALIDITA")));
        SolmrLogger.debug(this,
                          "checkDate DATA_AGGIORNAMENTO: " +
                          checkDate(rs.getDate("DATA_AGGIORNAMENTO")));
        manodoperaVO.setDataAggiornamentoDate(checkDate(rs.
            getDate("DATA_AGGIORNAMENTO")));

        UtenteIrideVO utenteAggiornamento = new UtenteIrideVO();
        utenteAggiornamento.setIdUtente(checkLongNull(rs.getString(
            "ID_UTENTE_AGGIORNAMENTO")));
        utenteAggiornamento.setDenominazione(rs.getString("UTENTE"));
        utenteAggiornamento.setDescrizioneEnteAppartenenza(rs.getString(
            "DESCRIZIONE_ENTE_APPARTENENZA"));
        utenteAggiornamento.setIdIntermediario(checkLongNull(rs.getString(
            "ID_INTERMEDIARIO")));
        manodoperaVO.setUtenteAggiornamento(utenteAggiornamento);
        manodoperaVO.setDenominazioneIntermediario(rs.getString("DENOMINAZIONE_INTERMEDIARIO"));

        TipoFormaConduzioneVO tipoFormaConduzioneVO = new TipoFormaConduzioneVO();
        tipoFormaConduzioneVO.setIdFormaConduzioneLong(checkLongNull(rs.
            getString("ID_FORMA_CONDUZIONE")));
        tipoFormaConduzioneVO.setCodice(rs.getString("COD_CONDUZ"));
        tipoFormaConduzioneVO.setForma(rs.getString("FORMA"));
        tipoFormaConduzioneVO.setDescrizione(rs.getString("DES_CONDUZ"));
        manodoperaVO.setTipoFormaConduzioneVO(tipoFormaConduzioneVO);
        manodoperaVO.setTipoFormaConduzione(rs.getString("ID_FORMA_CONDUZIONE"));
        manodoperaVO.setIdTipoIscrizioneINPS(checkIntegerNull(rs.getString("ID_TIPO_ISCRIZIONE_INPS")));
        manodoperaVO.setDataInizioIscrizioneDate(rs.getTimestamp("DATA_INIZIO_ISCRIZIONE"));
        if(manodoperaVO.getDataInizioIscrizioneDate() != null)
          manodoperaVO.setDataInizioIscrizione(DateUtils.formatDate(manodoperaVO.getDataInizioIscrizioneDate()));
        manodoperaVO.setDataCessazioneIscrizioneDate(rs.getTimestamp("DATA_CESSAZIONE_ISCRIZIONE"));
        if(manodoperaVO.getDataCessazioneIscrizioneDate() != null)
          manodoperaVO.setDataCessazioneIscrizione(DateUtils.formatDate(manodoperaVO.getDataCessazioneIscrizioneDate()));
        manodoperaVO.setDescTipoIscrizioneINPS(rs.getString("DESC_TIPO_ISCRIZ_INPS"));
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
                       "findManodoperaByIdManodopera - Executed query - Found " +
                       "1 result(s).");
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "findManodoperaByIdManodopera - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
                        "findManodoperaByIdManodopera - Generic Exception: " +
                        ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
            "findManodoperaByIdManodopera - SQLException while closing Statement and Connection: " +
            exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
                          "findManodoperaByIdManodopera - Generic Exception while closing Statement and Connection: " +
                          ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return manodoperaVO;
  }
  
  
  
  public ManodoperaVO findManodoperaAttivaByIdAzienda(long idAzienda) throws
    DataAccessException 
  {
  
    Connection conn = null;
    PreparedStatement stmt = null;
    
    ManodoperaVO manodoperaVO = null;
    
    SolmrLogger.debug(this, "idAzienda: " + idAzienda);
    
    try 
    {
      conn = getDatasource().getConnection();
    
      String search =
        "SELECT MAN.ID_MANODOPERA, " +
        "       MAN.CODICE_INPS, " +
        "       MAN.MATRICOLA_INAIL, " +
        "       MAN.ID_AZIENDA," +
        "       MAN.ID_TIPO_ISCRIZIONE_INPS, " +
        "       MAN.DATA_INIZIO_ISCRIZIONE, " +
        "       MAN.DATA_CESSAZIONE_ISCRIZIONE," +
        "       TII.DESCRIZIONE AS DESC_TIPO_ISCRIZ_INPS " +
        "FROM   DB_MANODOPERA MAN, " +
        "       DB_TIPO_ISCRIZIONE_INPS TII " +
        "WHERE  MAN.ID_AZIENDA = ? " +
        "AND    MAN.ID_TIPO_ISCRIZIONE_INPS = TII.ID_TIPO_ISCRIZIONE_INPS (+) " +
        "AND    MAN.DATA_FINE_VALIDITA IS NULL ";
    
      stmt = conn.prepareStatement(search);
    
      stmt.setLong(1, idAzienda);
    
      SolmrLogger.debug(this, "Executing query: " + search);
    
      ResultSet rs = stmt.executeQuery();
    
      if (rs.next()) 
      {
        manodoperaVO = new ManodoperaVO();
    
        manodoperaVO.setIdManodoperaLong(new Long(rs.getLong("ID_MANODOPERA")));
        manodoperaVO.setIdAziendaLong(new Long(rs.getLong(
            "ID_AZIENDA")));
        manodoperaVO.setCodiceInps(rs.getString("CODICE_INPS"));
            
        manodoperaVO.setIdTipoIscrizioneINPS(checkIntegerNull(rs.getString("ID_TIPO_ISCRIZIONE_INPS")));
        manodoperaVO.setDataInizioIscrizioneDate(rs.getTimestamp("DATA_INIZIO_ISCRIZIONE"));
        manodoperaVO.setDataCessazioneIscrizioneDate(rs.getTimestamp("DATA_CESSAZIONE_ISCRIZIONE"));
        manodoperaVO.setDescTipoIscrizioneINPS(rs.getString("DESC_TIPO_ISCRIZ_INPS"));
      }
    
      rs.close();
      stmt.close();
    
      SolmrLogger.debug(this,
                       "findManodoperaAttivaByIdAzienda - Executed query - Found " +
                       "1 result(s).");
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "findManodoperaAttivaByIdAzienda - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
                        "findManodoperaAttivaByIdAzienda - Generic Exception: " +
                        ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
            "findManodoperaAttivaByIdAzienda - SQLException while closing Statement and Connection: " +
            exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
                          "findManodoperaAttivaByIdAzienda - Generic Exception while closing Statement and Connection: " +
                          ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return manodoperaVO;
  }

  /**
   * Attività complementari svolte in azienda
   * @param idManodopera
   * @return Vector
   * @throws DataAccessException
   */
  public Vector<DettaglioAttivitaVO> getDetAttivitaComplByIdManodopera(Long idManodopera) throws
      DataAccessException {

    Connection conn = null;
    PreparedStatement stmt = null;

    Vector<DettaglioAttivitaVO> vDettaglioAttivita = null;
    DettaglioAttivitaVO dettaglioAttivitaVO = null;

    SolmrLogger.debug(this, "idManodopera: " + idManodopera);

    try {
      conn = getDatasource().getConnection();

      String search =
          " SELECT DET.ID_DETTAGLIO_ATTIVITA, TIP.ID_ATTIVITA_COMPLEMENTARI, TIP.DESCRIZIONE DES_TIPO_ATTIVITA, " +
          " DET.DESCRIZIONE DES_DETT_ATTIVITA " +
          " FROM DB_DETTAGLIO_ATTIVITA DET, DB_TIPO_ATTIVITA_COMPLEMENTARI TIP " +
          " WHERE DET.ID_MANODOPERA = ? " +
          " AND DET.ID_ATTIVITA_COMPLEMENTARI = TIP.ID_ATTIVITA_COMPLEMENTARI ";

      stmt = conn.prepareStatement(search);

      stmt.setBigDecimal(1, convertLongToBigDecimal(idManodopera));

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

        vDettaglioAttivita = new Vector<DettaglioAttivitaVO>();

        while (rs.next()) {
          dettaglioAttivitaVO = new DettaglioAttivitaVO();

          dettaglioAttivitaVO.setIdDettaglioAttivitaLong(checkLongNull(rs.
              getString(
              "ID_DETTAGLIO_ATTIVITA")));
          dettaglioAttivitaVO.setIdManodoperaLong(idManodopera);
          dettaglioAttivitaVO.setAttivitaComplementari(new CodeDescription(new
              Integer(rs.getInt("ID_ATTIVITA_COMPLEMENTARI")),
              rs.getString("DES_TIPO_ATTIVITA")));
          dettaglioAttivitaVO.setDescrizione(rs.getString("DES_DETT_ATTIVITA"));

          vDettaglioAttivita.add(dettaglioAttivitaVO);
        }


      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "getDetAttivitaComplByIdManodopera - Executed query - Found " +
          vDettaglioAttivita.size() + " result(s).");
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "getDetAttivitaComplByIdManodopera - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
          "getDetAttivitaComplByIdManodopera - Generic Exception: " +
          ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
                          "getDetAttivitaComplByIdManodopera - SQLException while closing Statement and Connection: " +
                          exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
                          "getDetAttivitaComplByIdManodopera - Generic Exception while closing Statement and Connection: " +
                          ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vDettaglioAttivita;
  }

  /**
   * Dettaglio manodopera suddiviso per tipo classe
   * @param idManodopera
   * @return Vector
   * @throws DataAccessException
   */
  public Vector<DettaglioManodoperaVO> getDettaglioManClassiByIdManodopera(Long idManodopera) throws
      DataAccessException {

    Connection conn = null;
    PreparedStatement stmt = null;

    Vector<DettaglioManodoperaVO> vDettaglioManodopera = null;
    DettaglioManodoperaVO dettaglioManodoperaVO = null;

    SolmrLogger.debug(this, "idManodopera: " + idManodopera);

    try {
      conn = getDatasource().getConnection();

      String search =
          " SELECT DET.ID_DETTAGLIO_MANODOPERA, DET.ID_CLASSE_MANODOPERA, TIP.CODICE COD_CLASSE, " +
          " TIP.DESCRIZIONE DES_CLASSE, DET.UOMINI, DET.DONNE, " +
          " DET.GIORNATE_ANNUE " +
          " FROM DB_DETTAGLIO_MANODOPERA DET, DB_TIPO_CLASSI_MANODOPERA TIP " +
          " WHERE DET.ID_MANODOPERA = ? " +
          " AND DET.ID_CLASSE_MANODOPERA (+) = TIP.ID_CLASSE_MANODOPERA " +
          " ORDER BY TIP.CODICE ";

      stmt = conn.prepareStatement(search);

      stmt.setBigDecimal(1, convertLongToBigDecimal(idManodopera));

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();


      while (rs.next()) 
      {
        if(vDettaglioManodopera == null)
          vDettaglioManodopera = new Vector<DettaglioManodoperaVO>();
          
        
        dettaglioManodoperaVO = new DettaglioManodoperaVO();

        dettaglioManodoperaVO.setIdDettaglioManodoperaLong(checkLongNull(rs.
            getString(
            "ID_DETTAGLIO_MANODOPERA")));
        dettaglioManodoperaVO.setIdManodoperaLong(idManodopera);

        dettaglioManodoperaVO.setIdClasseManodoperaLong(checkLongNull(rs.
            getString("ID_CLASSE_MANODOPERA")));
        dettaglioManodoperaVO.setCodTipoClasseManodopera(rs.getString(
            "COD_CLASSE"));
        dettaglioManodoperaVO.setDesTipoClasseManodopera(rs.getString(
            "DES_CLASSE"));

        dettaglioManodoperaVO.setUominiLong(checkLong(rs.getString("UOMINI")));
        dettaglioManodoperaVO.setDonneLong(checkLong(rs.getString("DONNE")));
        dettaglioManodoperaVO.setGiornateAnnueLong(checkLong(rs.getString(
            "GIORNATE_ANNUE")));

        vDettaglioManodopera.add(dettaglioManodoperaVO);
      }

      rs.close();
      stmt.close();

    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "getDettaglioManClassiByIdManodopera - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
          "getDettaglioManClassiByIdManodopera - Generic Exception: " +
          ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
                          "getDettaglioManClassiByIdManodopera - SQLException while closing Statement and Connection: " +
                          exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
                          "getDettaglioManClassiByIdManodopera - Generic Exception while closing Statement and Connection: " +
                          ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return vDettaglioManodopera;
  }

  /**
   * Cancellazione fisica dei record dettaglio attivita
   * @param idManodopera
   * @throws DataAccessException
   */
  public void deleteDettaglioAttivita(Long idManodopera) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    SolmrLogger.debug(this, "idManodopera: " + idManodopera);

    try {
      conn = getDatasource().getConnection();

      String delete =
          " DELETE FROM DB_DETTAGLIO_ATTIVITA " +
          " WHERE ID_MANODOPERA = ? ";

      stmt = conn.prepareStatement(delete);

      stmt.setBigDecimal(1, convertLongToBigDecimal(idManodopera));

      SolmrLogger.debug(this, "Executing delete: " + delete);

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed delete.");

      stmt.close();
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "deleteDettaglioAttivita - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
          "deleteDettaglioAttivita - Generic Exception: " +
          ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
                          "deleteDettaglioAttivita - SQLException while closing Statement and Connection: " +
                          exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
                          "deleteDettaglioAttivita - Generic Exception while closing Statement and Connection: " +
                          ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Cancellazione fisica dei record dettaglio manodopera
   * @param idManodopera
   * @throws DataAccessException
   */
  public void deleteDettaglioManodopera(Long idManodopera) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    SolmrLogger.debug(this, "idManodopera: " + idManodopera);

    try {
      conn = getDatasource().getConnection();

      String delete =
          " DELETE FROM DB_DETTAGLIO_MANODOPERA " +
          " WHERE ID_MANODOPERA = ? ";

      stmt = conn.prepareStatement(delete);

      stmt.setBigDecimal(1, convertLongToBigDecimal(idManodopera));

      SolmrLogger.debug(this, "Executing delete: " + delete);

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed delete.");

      stmt.close();
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "deleteDettaglioManodopera - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
          "deleteDettaglioManodopera - Generic Exception: " +
          ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
                          "deleteDettaglioManodopera - SQLException while closing Statement and Connection: " +
                          exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
                          "deleteDettaglioManodopera - Generic Exception while closing Statement and Connection: " +
                          ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Cancellazione fisica del record manodopera
   * @param idManodopera
   * @throws DataAccessException
   */
  public void deleteManodopera(Long idManodopera) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    SolmrLogger.debug(this, "idManodopera: " + idManodopera);

    try {
      conn = getDatasource().getConnection();

      String delete =
          " DELETE FROM DB_MANODOPERA " +
          " WHERE ID_MANODOPERA = ? ";

      stmt = conn.prepareStatement(delete);

      stmt.setBigDecimal(1, convertLongToBigDecimal(idManodopera));

      SolmrLogger.debug(this, "Executing delete: " + delete);

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed delete.");

      stmt.close();
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "deleteManodopera - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
          "deleteManodopera - Generic Exception: " +
          ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
                          "deleteManodopera - SQLException while closing Statement and Connection: " +
                          exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
                          "deleteManodopera - Generic Exception while closing Statement and Connection: " +
                          ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Inserimento manodopera
   * @param manodoperaVO
   * @return Long
   * @throws DataAccessException
   */
  public Long insertManodopera(ManodoperaVO manodoperaVO) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    Long primaryKey = null;

    SolmrLogger.debug(this, "manodoperaVO: " + manodoperaVO);

    try {
      primaryKey = getNextPrimaryKey((String) SolmrConstants.get("SEQ_MANODOPERA"));

      SolmrLogger.debug(this, "primaryKey: " + primaryKey);

      conn = getDatasource().getConnection();

      String insert =
          " INSERT INTO DB_MANODOPERA ( " +
          " ID_MANODOPERA, " +
          " ID_AZIENDA, " +
          " CODICE_INPS, " +
          " MATRICOLA_INAIL, " +
          " ID_FORMA_CONDUZIONE, " +
          " DATA_INIZIO_VALIDITA, " +
          " DATA_FINE_VALIDITA, " +
          " DATA_AGGIORNAMENTO, " +
          " ID_UTENTE_AGGIORNAMENTO, " +
          " ID_TIPO_ISCRIZIONE_INPS, " +
          " DATA_INIZIO_ISCRIZIONE, " +
          " DATA_CESSAZIONE_ISCRIZIONE " +
          " ) VALUES (?, ?, ?, ?, ?, SYSDATE, NULL, SYSDATE, ?, ?, ?, ?) ";

      stmt = conn.prepareStatement(insert);

      SolmrLogger.debug(this, "\n\n\n INIZIO parametri inserimento insertMandopera\n\n\n");
      SolmrLogger.debug(this, "primaryKey: " + primaryKey);
      SolmrLogger.debug(this, "manodoperaVO.getIdAziendaLong(): " + manodoperaVO.getIdAziendaLong());
      Long idFormaConduzione = null;
      if(manodoperaVO.getTipoFormaConduzioneVO() != null)
        idFormaConduzione = manodoperaVO.getTipoFormaConduzioneVO().getIdFormaConduzioneLong();
      SolmrLogger.debug(this, "manodoperaVO.getTipoFormaConduzioneVO().getIdFormaConduzioneLong(): " + idFormaConduzione);
      SolmrLogger.debug(this, "manodoperaVO.getDataInizioValiditaDate(): " + manodoperaVO.getDataInizioValiditaDate());
      SolmrLogger.debug(this, "manodoperaVO.getUtenteAggiornamento(): " + manodoperaVO.getUtenteAggiornamento());
      
      int indice = 0;
       
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(primaryKey));
      stmt.setBigDecimal(++indice,
                         convertLongToBigDecimal(manodoperaVO.getIdAziendaLong()));
      stmt.setString(++indice, manodoperaVO.getCodiceInps());
      stmt.setString(++indice, manodoperaVO.getMatricolaInail());
      stmt.setBigDecimal(++indice, convertLongToBigDecimal(idFormaConduzione));
      stmt.setBigDecimal(++indice,
                         convertLongToBigDecimal(manodoperaVO.getUtenteAggiornamento().
                                                 getIdUtente()));
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(manodoperaVO.getIdTipoIscrizioneINPS()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(manodoperaVO.getDataInizioIscrizioneDate()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(manodoperaVO.getDataCessazioneIscrizioneDate()));

      SolmrLogger.debug(this, "\n\n\n FINE parametri inserimento insertMandopera\n\n\n");

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed insert on Primary Key: " + primaryKey);

      stmt.close();
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "insertManodopera - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
                        "insertManodopera - Generic Exception: " +
                        ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
            "insertManodopera - SQLException while closing Statement and Connection: " +
            exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
            "insertManodopera - Generic Exception while closing Statement and Connection: " +
            ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }

    return primaryKey;
  }

  /**
   * Inserimento dettaglio manodopera
   * @param idManodopera, dettaglioManodoperaVO
   * @return Long
   * @throws DataAccessException
   */
  public Long insertDettaglioManodopera(Long idManodopera, DettaglioManodoperaVO dettaglioManodoperaVO) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    Long primaryKey = null;

    SolmrLogger.debug(this, "dettaglioManodoperaVO: " + dettaglioManodoperaVO);

    try {
      primaryKey = getNextPrimaryKey((String) SolmrConstants.get("SEQ_DETTAGLIO_MANODOPERA"));

      SolmrLogger.debug(this, "primaryKey: " + primaryKey);

      conn = getDatasource().getConnection();

      String insert =
          " INSERT INTO DB_DETTAGLIO_MANODOPERA ( " +
          " ID_DETTAGLIO_MANODOPERA, " +
          " ID_MANODOPERA, " +
          " ID_CLASSE_MANODOPERA, " +
          " UOMINI, " +
          " DONNE, " +
          " GIORNATE_ANNUE " +
          " ) VALUES (?, ?, ?, ?, ?, ?) ";

      stmt = conn.prepareStatement(insert);

      stmt.setBigDecimal(1, convertLongToBigDecimal(primaryKey));
      stmt.setBigDecimal(2, convertLongToBigDecimal(idManodopera));
      stmt.setBigDecimal(3, convertLongToBigDecimal(dettaglioManodoperaVO.getIdClasseManodoperaLong()));
      stmt.setBigDecimal(4, convertLongToBigDecimal(dettaglioManodoperaVO.getUominiLong()));
      stmt.setBigDecimal(5, convertLongToBigDecimal(dettaglioManodoperaVO.getDonneLong()));
      stmt.setBigDecimal(6, convertLongToBigDecimal(dettaglioManodoperaVO.getGiornateAnnueLong()));

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed insert on Primary Key: " + primaryKey);

      stmt.close();
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "insertDettaglioManodopera - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
                        "insertDettaglioManodopera - Generic Exception: " +
                        ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
            "insertDettaglioManodopera - SQLException while closing Statement and Connection: " +
            exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
            "insertDettaglioManodopera - Generic Exception while closing Statement and Connection: " +
            ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }

    return primaryKey;
  }

  /**
   * Inserimento dettaglio attivita
   * @param idManodopera, dettaglioManodoperaVO
   * @return Long
   * @throws DataAccessException
   */
  public Long insertDettaglioAttivita(Long idManodopera, DettaglioAttivitaVO dettaglioAttivitaVO) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    Long primaryKey = null;

    SolmrLogger.debug(this, "dettaglioAttivitaVO: " + dettaglioAttivitaVO);

    try {
      primaryKey = getNextPrimaryKey((String) SolmrConstants.get("SEQ_DETTAGLIO_ATTIVITA"));

      SolmrLogger.debug(this, "primaryKey: " + primaryKey);

      conn = getDatasource().getConnection();

      String insert =
          " INSERT INTO DB_DETTAGLIO_ATTIVITA ( " +
          " ID_DETTAGLIO_ATTIVITA, " +
          " ID_MANODOPERA, " +
          " ID_ATTIVITA_COMPLEMENTARI, " +
          " DESCRIZIONE " +
          " ) VALUES (?, ?, ?, ?) ";

      stmt = conn.prepareStatement(insert);

      stmt.setBigDecimal(1, convertLongToBigDecimal(primaryKey));
      stmt.setBigDecimal(2, convertLongToBigDecimal(idManodopera));
      stmt.setBigDecimal(3, convertIntegerToBigDecimal(dettaglioAttivitaVO.getAttivitaComplementari().getCode()));
      stmt.setString(4, dettaglioAttivitaVO.getDescrizione());

      SolmrLogger.debug(this, "Executing insert: " + insert);

      stmt.executeUpdate();

      SolmrLogger.debug(this, "Executed insert on Primary Key: " + primaryKey);

      stmt.close();
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "insertDettaglioAttivita - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
                        "insertDettaglioAttivita - Generic Exception: " +
                        ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
            "insertDettaglioAttivita - SQLException while closing Statement and Connection: " +
            exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
            "insertDettaglioAttivita - Generic Exception while closing Statement and Connection: " +
            ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }

    return primaryKey;
  }

  /**
   * restituisce true se trova un record con data fine validita
   * impostata a null
   * @param idManodopera
   * @return boolean
   * @throws DataAccessException
   */
  public boolean isManodoperaValida(Long idManodopera, Long idAzienda) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    boolean esiste = false;

    SolmrLogger.debug(this, "idManodopera: " + idManodopera + " idAzienda: " + idAzienda);

    try {
      conn = getDatasource().getConnection();

      String search =
          " SELECT ID_MANODOPERA " +
          " FROM DB_MANODOPERA " +
          " WHERE DATA_FINE_VALIDITA IS NULL " +
          " AND ID_AZIENDA = ? ";

      if (idManodopera != null)
      {
        search += " AND ID_MANODOPERA = ? ";
      }

      stmt = conn.prepareStatement(search);

      stmt.setBigDecimal(1, convertLongToBigDecimal(idAzienda));

      if (idManodopera != null)
      {
        stmt.setBigDecimal(2, convertLongToBigDecimal(idManodopera));
      }

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        esiste = true;
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
                       "findManodoperaByIdManodopera - Executed query - Found " +
                       "1 result(s).");
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "findManodoperaByIdManodopera - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
                        "findManodoperaByIdManodopera - Generic Exception: " +
                        ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
            "findManodoperaByIdManodopera - SQLException while closing Statement and Connection: " +
            exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
                          "findManodoperaByIdManodopera - Generic Exception while closing Statement and Connection: " +
                          ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }

    return esiste;
  }

  /**
   * Ultima dichiarazione di manodopera non valida
   * @return ManodoperaVO
   * @throws DataAccessException
   */
  public ManodoperaVO findLastManodopera(Long idAzienda) throws DataAccessException {

    Connection conn = null;
    PreparedStatement stmt = null;

    ManodoperaVO manodoperaVO = null;

    try {
      conn = getDatasource().getConnection();

      String search =
          " SELECT MAX(DATA_FINE_VALIDITA) " +
          " FROM   DB_MANODOPERA " +
          " WHERE  ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(search);

      stmt.setLong(1, idAzienda.longValue());

      SolmrLogger.debug(this, "Executing query: " + search);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        manodoperaVO = new ManodoperaVO();
        manodoperaVO.setDataFineValiditaDate(checkDate(rs.getDate(1)));
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this,
          "findLastManodopera - Executed query - Found " +
          " 1 result(s).");
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "findLastManodopera - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
          "findLastManodopera - Generic Exception: " +
          ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
                          "findLastManodopera - SQLException while closing Statement and Connection: " +
                          exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
                          "findLastManodopera - Generic Exception while closing Statement and Connection: " +
                          ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return manodoperaVO;
  }

  /**
   * Modifica manodopera
   * @param manodoperaVO
   * @throws DataAccessException
   */
  public void updateManodopera(ManodoperaVO manodoperaVO) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    SolmrLogger.debug(this, "manodoperaVO: " + manodoperaVO);

    try {
      conn = getDatasource().getConnection();

      String update =
          " UPDATE DB_MANODOPERA SET " +
          " CODICE_INPS = ?, " +
          " MATRICOLA_INAIL = ?, " +
          " ID_FORMA_CONDUZIONE = ?, " +
          " DATA_AGGIORNAMENTO = SYSDATE, " +
          " ID_UTENTE_AGGIORNAMENTO = ?, " +
          " ID_TIPO_ISCRIZIONE_INPS = ?, " +
          " DATA_INIZIO_ISCRIZIONE = ?, " +
          " DATA_CESSAZIONE_ISCRIZIONE = ? " +
          " WHERE ID_MANODOPERA = ? ";

      stmt = conn.prepareStatement(update);

      int indice = 0;
      
      stmt.setString(++indice, manodoperaVO.getCodiceInps());
      stmt.setString(++indice, manodoperaVO.getMatricolaInail());
      stmt.setBigDecimal(++indice,
                         convertLongToBigDecimal(manodoperaVO.getTipoFormaConduzioneVO().
                                                 getIdFormaConduzioneLong()));
      stmt.setBigDecimal(++indice,
                         convertLongToBigDecimal(manodoperaVO.getUtenteAggiornamento().
                                                 getIdUtente()));
      
      stmt.setBigDecimal(++indice, convertIntegerToBigDecimal(manodoperaVO.getIdTipoIscrizioneINPS()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(manodoperaVO.getDataInizioIscrizioneDate()));
      stmt.setTimestamp(++indice, convertDateToTimestamp(manodoperaVO.getDataCessazioneIscrizioneDate()));
      
      stmt.setBigDecimal(++indice,
          convertLongToBigDecimal(manodoperaVO.getIdManodoperaLong()));

      SolmrLogger.debug(this, "Executing update: " + update);

      stmt.executeUpdate();

      stmt.close();
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "updateManodopera - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
                        "updateManodopera - Generic Exception: " +
                        ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
            "updateManodopera - SQLException while closing Statement and Connection: " +
            exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
            "updateManodopera - Generic Exception while closing Statement and Connection: " +
            ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }

  /**
   * Modifica dettaglio manodopera
   * @param manodoperaVO
   * @throws DataAccessException
   */
  public void updateDettaglioManodopera(DettaglioManodoperaVO dettaglioManodoperaVO) throws DataAccessException
  {
    Connection conn = null;
    PreparedStatement stmt = null;

    SolmrLogger.debug(this, "manodoperaVO: " + dettaglioManodoperaVO);

    try {
      conn = getDatasource().getConnection();

      String update =
          " UPDATE DB_DETTAGLIO_MANODOPERA SET " +
          " UOMINI = ?, " +
          " DONNE = ?, " +
          " GIORNATE_ANNUE = ? " +
          " WHERE ID_DETTAGLIO_MANODOPERA = ? ";

      stmt = conn.prepareStatement(update);

      stmt.setBigDecimal(1, convertLongToBigDecimal(dettaglioManodoperaVO.getUominiLong()));
      stmt.setBigDecimal(2, convertLongToBigDecimal(dettaglioManodoperaVO.getDonneLong()));
      stmt.setBigDecimal(3, convertLongToBigDecimal(dettaglioManodoperaVO.getGiornateAnnueLong()));
      stmt.setBigDecimal(4, convertLongToBigDecimal(dettaglioManodoperaVO.getIdDettaglioManodoperaLong()));

      SolmrLogger.debug(this, "Executing update: " + update);

      stmt.executeUpdate();

      stmt.close();
    }
    catch (SQLException exc) {
      SolmrLogger.fatal(this,
                        "updateDettaglioManodopera - SQLException: " +
                        exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this,
                        "updateDettaglioManodopera - Generic Exception: " +
                        ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this,
            "updateDettaglioManodopera - SQLException while closing Statement and Connection: " +
            exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this,
            "updateDettaglioManodopera - Generic Exception while closing Statement and Connection: " +
            ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }


  /**
   * controlla che la data passata sia sia minore della data dell'ultima
   * dichiarazione di consistenza dell'azienda
   * @param idManodopera
   * @return boolean
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public boolean isManodoperaDichiarata(Long idManodopera, Date dataInizio)
      throws DataAccessException
  {
    boolean result = false;

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();

      String query = "SELECT DATA_INIZIO_VALIDITA,ID_AZIENDA "+
                     "FROM DB_MANODOPERA "+
                     "WHERE ID_MANODOPERA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: "+query);
      SolmrLogger.debug(this, "idAllevamento = "+idManodopera);

      stmt.setLong(1, idManodopera.longValue());

      ResultSet rs = stmt.executeQuery();

      Date dataInizioAll=null,dataDichiarazione=null;
      long idAzienda=0;

      if (rs.next())
      {
        dataInizioAll=rs.getTimestamp("DATA_INIZIO_VALIDITA");
        //dataInizioAll=rs.getDate("DATA_INIZIO_VALIDITA");
        idAzienda=rs.getLong("ID_AZIENDA");
      }
      /**
       * In teoria non dovrebbe mai passare di qua perchè un record deve trovarlo.
       * Se non lo trova non ho un idazienda valido
       */
      else return false;

      rs.close();
      stmt.close();

      if (dataInizio!=null) dataInizioAll=dataInizio;

      query = "SELECT MAX(C.DATA_INSERIMENTO_DICHIARAZIONE) AS DATA_DICHIARAZIONE "+
              "FROM DB_DICHIARAZIONE_CONSISTENZA C "+
              "WHERE C.ID_AZIENDA = ? ";

      stmt = conn.prepareStatement(query);

      SolmrLogger.debug(this, "Executing query: "+query);
      SolmrLogger.debug(this, "idManodopera = "+idManodopera);
      SolmrLogger.debug(this, "idAzienda = "+idAzienda);
      SolmrLogger.debug(this, "dataInizioAll = "+dataInizioAll);

      stmt.setLong(1, idAzienda);

      rs = stmt.executeQuery();

      if (rs.next())
        dataDichiarazione=rs.getTimestamp("DATA_DICHIARAZIONE");
        //dataDichiarazione=rs.getDate("DATA_DICHIARAZIONE");
      
      SolmrLogger.debug(this, "dataDichiarazione = "+dataDichiarazione);

      if (dataDichiarazione==null)
        result=false;
      else
      {
        if (dataInizioAll.before(dataDichiarazione)) result=true;
        else result=false;
      }

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "Executed query - Found records");
    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "isManodoperaDichiarata - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "isManodoperaDichiarata - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "isManodoperaDichiarata - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "isManodoperaDichiarata - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }


  /**
   * controlla che db_manodopera.data_inizio_validita sia minore della data dell'ultima
   * dichiarazione di consistenza dell'azienda
   * @param idAllevamento
   * @return boolean
   * @throws DataAccessException
   * @throws NotFoundException
   */
  public boolean isManodoperaDichiarata(Long idManodopera)
      throws DataAccessException
  {
    return isManodoperaDichiarata(idManodopera,null);
  }


  /**
   * Inserisce un nuovo allevamento e imposta a SYSDATE la data fine validità
   * dell'allevamento corrente
   */
  public void storicizzaManodopera(ManodoperaVO manodoperaVO)
      throws DataAccessException
  {

    Connection conn = null;
    PreparedStatement stmt = null;
    try
    {
      conn = getDatasource().getConnection();
      String update =
          " UPDATE DB_MANODOPERA SET " +
          " DATA_FINE_VALIDITA = SYSDATE, " +
          " DATA_AGGIORNAMENTO = SYSDATE, " +
          " ID_UTENTE_AGGIORNAMENTO = ? " +
          " WHERE ID_MANODOPERA = ? ";

      SolmrLogger.debug(this,"\nstoricizzaManodopera="+update);
      stmt = conn.prepareStatement(update);

      stmt.setBigDecimal(1,
                         convertLongToBigDecimal(manodoperaVO.getUtenteAggiornamento().
                                                 getIdUtente()));
      stmt.setBigDecimal(2,
                         convertLongToBigDecimal(manodoperaVO.getIdManodoperaLong()));

      stmt.executeUpdate();

      stmt.close();

    } catch (SQLException exc) {
      SolmrLogger.fatal(this, "storicizzaManodopera - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    } catch (Exception ex) {
      SolmrLogger.fatal(this, "storicizzaManodopera - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (SQLException exc) {
        SolmrLogger.fatal(this, "storicizzaManodopera - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      } catch (Exception ex) {
        SolmrLogger.fatal(this, "storicizzaManodopera - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
  }
  
  
  /**
   * 
   * 
   * @param idManodopera
   * @return
   * @throws DataAccessException
   */
  public boolean lockTableManodopera(long idManodopera) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    boolean flagLock = false;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[ManodoperaDAO::lockTableManodopera] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append(" SELECT * " +
              "FROM DB_MANODOPERA " +
              "WHERE ID_MANODOPERA  = ? " +
              "FOR UPDATE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ManodoperaDAO::lockTableManodopera] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice,idManodopera);
      
      
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        flagLock = true;
      }
      
      return flagLock;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("flagLock", flagLock) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idManodopera", idManodopera)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ManodoperaDAO::lockTableManodopera] ",
              t, query, variabili, parametri);
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
      SolmrLogger
          .debug(this,
              "[ManodoperaDAO::lockTableManodopera] END.");
    }
  }
  
  
  public Vector<TipoIscrizioneINPSVO> getElencoTipoIscrizioneINPSAttivi() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoIscrizioneINPSVO> vTipoIscrINPS = null;
    TipoIscrizioneINPSVO tipoIscrizioneINPSVO = null;
    
    try
    {
      SolmrLogger.debug(this, "[ManodoperaDAO::getElencoTipoIscrizioneINPSAttivi] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append("" +
        "SELECT  TII.ID_TIPO_ISCRIZIONE_INPS, " +
        "        TII.CODICE_TIPO_ISCRIZIONE,  " +
        "        TII.DESCRIZIONE " +
        "FROM    DB_TIPO_ISCRIZIONE_INPS TII " +
        "WHERE   TII.DATA_FINE_VALIDITA IS NULL " +
        "ORDER BY TII.DESCRIZIONE ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[ManodoperaDAO::getElencoTipoIscrizioneINPSAttivi] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoIscrINPS == null)
        {
          vTipoIscrINPS = new Vector<TipoIscrizioneINPSVO>();
        }
        tipoIscrizioneINPSVO = new TipoIscrizioneINPSVO();
        
        tipoIscrizioneINPSVO.setIdTipoIscrizioneINPS(rs.getInt("ID_TIPO_ISCRIZIONE_INPS"));
        tipoIscrizioneINPSVO.setCodiceTipoIscrizione(rs.getString("CODICE_TIPO_ISCRIZIONE"));
        tipoIscrizioneINPSVO.setDescrizione(rs.getString("DESCRIZIONE"));
        
        vTipoIscrINPS.add(tipoIscrizioneINPSVO);
      }
      
      return vTipoIscrINPS;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoIscrINPS", vTipoIscrINPS),  
          new Variabile("tipoIscrizioneINPSVO", tipoIscrizioneINPSVO) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[ManodoperaDAO::getElencoTipoIscrizioneINPSAttivi] ",
              t, query, variabili, parametri);
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
      SolmrLogger
          .debug(this,
              "[ManodoperaDAO::getElencoTipoIscrizioneINPSAttivi] END.");
    }
  }
}
