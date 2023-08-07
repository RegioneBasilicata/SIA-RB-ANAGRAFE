package it.csi.smranag.smrgaa.integration.terreni;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.IsolaParcellaVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

public class IsolaParcellaGaaDAO extends it.csi.solmr.integration.BaseDAO
{

  public IsolaParcellaGaaDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public IsolaParcellaGaaDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }
  
  
  
  /**
   * 
   * estrae le isole_parcelle associate ad una azienda.
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<IsolaParcellaVO> getElencoIsoleParcelle(String nomeLib, long idAzienda)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<IsolaParcellaVO> vIsolaParcella = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::getElencoIsoleParcelle] BEGIN.");

      query = "" +
      		"SELECT " +
          "       SOMMA.ID_ISOLA_PARCELLA, " + 
          "       SOMMA.ID_ILO, " +
          "       SOMMA.SUPERFICIE, " +
          "       SOMMA.ID_DICHIARAZIONE_CONSISTENZA, " + 
          "       MAX(SOMMA.DATA_AGGIORNAMENTO) AS DATA_AGGIORNAMENTO_UV, " +
          "       MAX(SOMMA.ID_PARTICELLA) AS ID_PARTICELLA, " +
          "       SUM(DECODE(SOMMA.PARCELLA_SELEZIONATA,'S',SOMMA.AREA,0)) SUP_VIT, " +
          "       COUNT(*) AS NUMMEROUV " +
     //     "       "+nomeLib+".PARCELLA_IN_TOLLERANZA_GIS(?, SOMMA.ID_ISOLA_PARCELLA) AS TOLLERANZA " +
          "FROM " +
          "      (SELECT " + 
          "             ISOLA.ID_ISOLA_PARCELLA, " + 
          "             ISOLA.ID_ILO, " +
          "             ISOLA.SUPERFICIE, " +
          "             ISOLA.ID_DICHIARAZIONE_CONSISTENZA, " +
          "             ISOLA.ID_PARTICELLA, " +
          "             SUA.ID_UNITA_ARBOREA, " +
          "             SUA.AREA, " +
          "             SUA.DATA_AGGIORNAMENTO, " +
          "             NVL((SELECT UP.PARCELLA_SELEZIONATA " +
          "                  FROM   DB_UNAR_PARCELLA UP " +
          "                  WHERE  UP.ID_ISOLA_PARCELLA = ISOLA.ID_ISOLA_PARCELLA " +
          "                  AND    UP.ID_UNITA_ARBOREA = SUA.ID_UNITA_ARBOREA),'S') " +
          "            PARCELLA_SELEZIONATA " +
          "      FROM  DB_STORICO_UNITA_ARBOREA SUA, " + 
          "            (SELECT ISOPA.ID_ISOLA_PARCELLA, " + 
          "                    ISO.ID_ILO, " +
          "                    ISOPA.SUPERFICIE, " + 
          "                    CD.ID_PARTICELLA, " +
          "                    DC.ID_DICHIARAZIONE_CONSISTENZA " +
          "             FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "                    DB_ISOLA_DICHIARATA IDICH, " +
          "                    DB_ISOLA ISO, " +
          "                    DB_ISOLA_PARCELLA ISOPA, " + 
          "                    DB_PARCELLA_CONDUZIONE PC, " +
          "                    DB_CONDUZIONE_DICHIARATA CD " +
          "             WHERE  DC.ID_AZIENDA = ? " +
          "             AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " + 
          "                    (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " + 
          "                     FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, " +
          "                            DB_TIPO_MOTIVO_DICHIARAZIONE TMC, " +
          "                            DB_ISOLA_DICHIARATA IDICH1 " +
          "                     WHERE  DC1.ID_MOTIVO_DICHIARAZIONE = TMC.ID_MOTIVO_DICHIARAZIONE " +
          "                     AND    IDICH1.DATA_FINE_VALIDITA IS NULL " +
          "                     AND    TMC.TIPO_DICHIARAZIONE <> 'C' " +
          "                     AND    DC1.ID_DICHIARAZIONE_CONSISTENZA = IDICH1.ID_DICHIARAZIONE_CONSISTENZA " + 
          "                     AND    DC1.ID_AZIENDA = DC.ID_AZIENDA " +
          "                     AND    DC1.FLAG_AGGIORNAMENTO_ISOLE = 'S' " +
          "                    ) " +
          "             AND DC.ID_DICHIARAZIONE_CONSISTENZA = IDICH.ID_DICHIARAZIONE_CONSISTENZA " + 
          "             AND IDICH.DATA_FINE_VALIDITA IS NULL " +
          "             AND IDICH.ID_ISOLA_DICHIARATA = ISO.ID_ISOLA_DICHIARATA " + 
          "             AND ISO.ID_ISOLA = ISOPA.ID_ISOLA " +
          "             AND ISOPA.ID_ELEGGIBILITA_FIT = 26 " +
          "             AND ISOPA.ID_ISOLA_PARCELLA = PC.ID_ISOLA_PARCELLA " + 
          "             AND PC.ID_CONDUZIONE_DICHIARATA = CD.ID_CONDUZIONE_DICHIARATA " + 
          "             GROUP BY ISOPA.ID_ISOLA_PARCELLA, " +
          "                      ISO.ID_ILO, " +
          "                      ISOPA.SUPERFICIE, " + 
          "                      CD.ID_PARTICELLA, " +
          "                      DC.ID_DICHIARAZIONE_CONSISTENZA " +
          "            ) ISOLA " +
          "      WHERE  SUA.ID_AZIENDA =  ? " +
          "      AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "      AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "      AND    NOT EXISTS (SELECT UP.ID_UNAR_PARCELLA " +
          "                         FROM   DB_UNAR_PARCELLA UP " +
          "                         WHERE  UP.ID_ISOLA_PARCELLA = ISOLA.ID_ISOLA_PARCELLA " +
          "                         AND    UP.ID_UNITA_ARBOREA = SUA.ID_UNITA_ARBOREA " +
          "                         AND    UP.PARCELLA_SELEZIONATA = 'N' " +
          "                         AND    ROWNUM < 2  " +                              
          "                        ) " +
          "      AND    EXISTS (SELECT * " +
          "                     FROM    DB_UTE UT, " + 
          "                             DB_CONDUZIONE_PARTICELLA CP " + 
          "                     WHERE   UT.DATA_FINE_ATTIVITA IS NULL " +
          "                     AND     UT.ID_AZIENDA = SUA.ID_AZIENDA "+
          "                     AND     CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "                     AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
          "                     AND     CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +    
          "                    ) " +
          "      AND    ISOLA.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "      GROUP BY ISOLA.ID_ISOLA_PARCELLA, " +
          "               ISOLA.ID_ILO, " +
          "               ISOLA.SUPERFICIE, " + 
          "               ISOLA.ID_DICHIARAZIONE_CONSISTENZA, " +
          "               ISOLA.ID_PARTICELLA, " +
          "               SUA.ID_UNITA_ARBOREA, " +
          "               SUA.AREA, " +
          "               SUA.DATA_AGGIORNAMENTO) SOMMA " +
          "GROUP BY SOMMA.ID_ISOLA_PARCELLA, " +
          "         SOMMA.ID_ILO, " +
          "         SOMMA.SUPERFICIE, " + 
          "         SOMMA.ID_DICHIARAZIONE_CONSISTENZA";    
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::getElencoIsoleParcelle] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
      stmt.setLong(2, idAzienda);
      //stmt.setLong(3, idAzienda); 
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(vIsolaParcella == null)
        {
          vIsolaParcella = new Vector<IsolaParcellaVO>();
        }
        IsolaParcellaVO isolaParcellaVO = new IsolaParcellaVO();
        isolaParcellaVO.setIdIsolaParcella(checkLongNull(rs.getString("ID_ISOLA_PARCELLA")));
        isolaParcellaVO.setIdIlo(checkLongNull(rs.getString("ID_ILO")));
        isolaParcellaVO.setSupParcella(rs.getBigDecimal("SUPERFICIE"));
        isolaParcellaVO.setSupVitata(rs.getBigDecimal("SUP_VIT"));
        isolaParcellaVO.setNumeroUV(checkInt(rs.getString("NUMMEROUV")));
        isolaParcellaVO.setIdParticella(checkLongNull(rs.getString("ID_PARTICELLA")));
        isolaParcellaVO.setIdDichiarazioneConsistenza(checkLongNull(rs.getString("ID_DICHIARAZIONE_CONSISTENZA")));
        isolaParcellaVO.setDataAggiornamntoUV(rs.getTimestamp("DATA_AGGIORNAMENTO_UV"));
      //  isolaParcellaVO.setTolleranza(checkInt(rs.getString("TOLLERANZA")));
        
        vIsolaParcella.add(isolaParcellaVO);
      }
      
      return vIsolaParcella;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vIsolaParcella", vIsolaParcella) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[IsolaParcellaGaaDAO::getElencoIsoleParcelle] ", t,
          query, variabili, parametri);

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
      SolmrLogger.debug(this,
          "[IsolaParcellaGaaDAO::getElencoIsoleParcelle] END.");
    }
  }
  
  
  
  /**
   * restituisce le isole e parcelle senza tolleranza
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<IsolaParcellaVO> getElencoIsoleParcelleNoToll(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<IsolaParcellaVO> vIsolaParcella = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::getElencoIsoleParcelleNoToll] BEGIN.");
    
      query = "" +
          "SELECT " +
          "       SOMMA.ID_ISOLA_PARCELLA, " + 
          "       SOMMA.ID_ILO, " +
          "       SOMMA.SUPERFICIE, " +
          "       SOMMA.ID_DICHIARAZIONE_CONSISTENZA, " + 
          "       MAX(SOMMA.DATA_AGGIORNAMENTO) AS DATA_AGGIORNAMENTO_UV, " +
          "       MAX(SOMMA.ID_PARTICELLA) AS ID_PARTICELLA, " +
          "       SUM(DECODE(SOMMA.PARCELLA_SELEZIONATA,'S',SOMMA.AREA,0)) SUP_VIT, " +
          "       COUNT(*) AS NUMMEROUV " +
          "FROM " +
          "      (SELECT " + 
          "             ISOLA.ID_ISOLA_PARCELLA, " + 
          "             ISOLA.ID_ILO, " +
          "             ISOLA.SUPERFICIE, " +
          "             ISOLA.ID_DICHIARAZIONE_CONSISTENZA, " +
          "             ISOLA.ID_PARTICELLA, " +
          "             SUA.ID_UNITA_ARBOREA, " +
          "             SUA.AREA, " +
          "             SUA.DATA_AGGIORNAMENTO, " +
          "             NVL((SELECT UP.PARCELLA_SELEZIONATA " +
          "                  FROM   DB_UNAR_PARCELLA UP " +
          "                  WHERE  UP.ID_ISOLA_PARCELLA = ISOLA.ID_ISOLA_PARCELLA " +
          "                  AND    UP.ID_UNITA_ARBOREA = SUA.ID_UNITA_ARBOREA),'S') " +
          "            PARCELLA_SELEZIONATA " +
          "      FROM  DB_STORICO_UNITA_ARBOREA SUA, " +
          "            (SELECT ISOPA.ID_ISOLA_PARCELLA, " + 
          "                    ISO.ID_ILO, " +
          "                    ISOPA.SUPERFICIE, " + 
          "                    CD.ID_PARTICELLA, " +
          "                    DC.ID_DICHIARAZIONE_CONSISTENZA " +
          "             FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "                    DB_ISOLA_DICHIARATA IDICH, " +
          "                    DB_ISOLA ISO, " +
          "                    DB_ISOLA_PARCELLA ISOPA, " + 
          "                    DB_PARCELLA_CONDUZIONE PC, " +
          "                    DB_CONDUZIONE_DICHIARATA CD " +
          "             WHERE  DC.ID_AZIENDA = ? " +
          "             AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " + 
          "                    (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " + 
          "                     FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, " +
          "                            DB_TIPO_MOTIVO_DICHIARAZIONE TMC, " +
          "                            DB_ISOLA_DICHIARATA IDICH1 " +
          "                     WHERE  DC1.ID_MOTIVO_DICHIARAZIONE = TMC.ID_MOTIVO_DICHIARAZIONE " +
          "                     AND    IDICH1.DATA_FINE_VALIDITA IS NULL " +
          "                     AND    TMC.TIPO_DICHIARAZIONE <> 'C' " +
          "                     AND    DC1.ID_DICHIARAZIONE_CONSISTENZA = IDICH1.ID_DICHIARAZIONE_CONSISTENZA " + 
          "                     AND    DC1.ID_AZIENDA = DC.ID_AZIENDA " +
          "                     AND    DC1.FLAG_AGGIORNAMENTO_ISOLE = 'S' " +
          "                    ) " +
          "             AND DC.ID_DICHIARAZIONE_CONSISTENZA = IDICH.ID_DICHIARAZIONE_CONSISTENZA " + 
          "             AND IDICH.DATA_FINE_VALIDITA IS NULL " +
          "             AND IDICH.ID_ISOLA_DICHIARATA = ISO.ID_ISOLA_DICHIARATA " + 
          "             AND ISO.ID_ISOLA = ISOPA.ID_ISOLA " +
          "             AND ISOPA.ID_ELEGGIBILITA_FIT = 26 " +
          "             AND ISOPA.ID_ISOLA_PARCELLA = PC.ID_ISOLA_PARCELLA " + 
          "             AND PC.ID_CONDUZIONE_DICHIARATA = CD.ID_CONDUZIONE_DICHIARATA " + 
          "             GROUP BY ISOPA.ID_ISOLA_PARCELLA, " +
          "                      ISO.ID_ILO, " +
          "                      ISOPA.SUPERFICIE, " + 
          "                      CD.ID_PARTICELLA, " +
          "                      DC.ID_DICHIARAZIONE_CONSISTENZA " +
          "            ) ISOLA " +
          "      WHERE  SUA.ID_AZIENDA =  ? " +
          "      AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "      AND    EXISTS (SELECT * " +
          "                     FROM    DB_UTE UT, " + 
          "                             DB_CONDUZIONE_PARTICELLA CP " + 
          "                     WHERE   UT.DATA_FINE_ATTIVITA IS NULL " +
          "                     AND     UT.ID_AZIENDA = SUA.ID_AZIENDA "+
          "                     AND     CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "                     AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
          "                     AND     CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +    
          "                    ) " +
          "      AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "      AND    ISOLA.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "      GROUP BY ISOLA.ID_ISOLA_PARCELLA, " +
          "               ISOLA.ID_ILO, " +
          "               ISOLA.SUPERFICIE, " + 
          "               ISOLA.ID_DICHIARAZIONE_CONSISTENZA, " +
          "               ISOLA.ID_PARTICELLA, " +
          "               SUA.ID_UNITA_ARBOREA, " +
          "               SUA.AREA, " +
          "               SUA.DATA_AGGIORNAMENTO) SOMMA " +
          "GROUP BY SOMMA.ID_ISOLA_PARCELLA, " +
          "         SOMMA.ID_ILO, " +
          "         SOMMA.SUPERFICIE, " + 
          "         SOMMA.ID_DICHIARAZIONE_CONSISTENZA";    
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::getElencoIsoleParcelleNoToll] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
      stmt.setLong(2, idAzienda);
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(vIsolaParcella == null)
        {
          vIsolaParcella = new Vector<IsolaParcellaVO>();
        }
        IsolaParcellaVO isolaParcellaVO = new IsolaParcellaVO();
        isolaParcellaVO.setIdIsolaParcella(checkLongNull(rs.getString("ID_ISOLA_PARCELLA")));
        isolaParcellaVO.setIdIlo(checkLongNull(rs.getString("ID_ILO")));
        isolaParcellaVO.setSupParcella(rs.getBigDecimal("SUPERFICIE"));
        isolaParcellaVO.setSupVitata(rs.getBigDecimal("SUP_VIT"));
        isolaParcellaVO.setNumeroUV(checkInt(rs.getString("NUMMEROUV")));
        isolaParcellaVO.setIdParticella(checkLongNull(rs.getString("ID_PARTICELLA")));
        isolaParcellaVO.setIdDichiarazioneConsistenza(checkLongNull(rs.getString("ID_DICHIARAZIONE_CONSISTENZA")));
        isolaParcellaVO.setDataAggiornamntoUV(rs.getTimestamp("DATA_AGGIORNAMENTO_UV"));
        
        vIsolaParcella.add(isolaParcellaVO);
      }
      
      return vIsolaParcella;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vIsolaParcella", vIsolaParcella) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[IsolaParcellaGaaDAO::getElencoIsoleParcelleNoToll] ", t,
          query, variabili, parametri);
    
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
      SolmrLogger.debug(this,
          "[IsolaParcellaGaaDAO::getElencoIsoleParcelleNoToll] END.");
    }
  }
  
  
  
  
  
  /**
   * 
   * estrae gli id_UNITA_ARBOREA per le UV
   * che appartengono a più di una parcella
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getElencoIdUVParcelleDoppie(long idAzienda)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<Long> vIdUnitaArborea = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::getElencoIdUVParcelleDoppie] BEGIN.");

      query = "" +
          "SELECT  " +     
          "       SUA.ID_UNITA_ARBOREA " + 
          "FROM   DB_STORICO_UNITA_ARBOREA SUA, " + 
          "       (SELECT ISOPA.ID_ISOLA_PARCELLA, " + 
          "               ISO.ID_ILO, " +
          "               ISOPA.SUPERFICIE, " + 
          "               CD.ID_PARTICELLA " +
          "        FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " + 
          "               DB_ISOLA_DICHIARATA IDICH, " +
          "               DB_ISOLA ISO, " +
          "               DB_ISOLA_PARCELLA ISOPA, " + 
          "               DB_PARCELLA_CONDUZIONE PC, " +
          "               DB_CONDUZIONE_DICHIARATA CD " +
          "        WHERE  DC.ID_AZIENDA = ? " +
          "        AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " + 
          "               (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " + 
          "               FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, " +
          "                      DB_TIPO_MOTIVO_DICHIARAZIONE TMC, " +
          "                      DB_ISOLA_DICHIARATA IDICH1 " +
          "               WHERE  DC1.ID_MOTIVO_DICHIARAZIONE = TMC.ID_MOTIVO_DICHIARAZIONE " +
          "               AND    IDICH1.DATA_FINE_VALIDITA IS NULL " +
          "               AND    TMC.TIPO_DICHIARAZIONE <> 'C' " +
          "               AND    DC1.ID_DICHIARAZIONE_CONSISTENZA = IDICH1.ID_DICHIARAZIONE_CONSISTENZA " + 
          "               AND    DC1.ID_AZIENDA = DC.ID_AZIENDA " +
          "               AND    DC1.FLAG_AGGIORNAMENTO_ISOLE = 'S' " +
          "               ) " +
          "      AND DC.ID_DICHIARAZIONE_CONSISTENZA = IDICH.ID_DICHIARAZIONE_CONSISTENZA " +
          "      AND IDICH.DATA_FINE_VALIDITA IS NULL " +
          "      AND IDICH.ID_ISOLA_DICHIARATA = ISO.ID_ISOLA_DICHIARATA " + 
          "      AND ISO.ID_ISOLA = ISOPA.ID_ISOLA " +
          "      AND ISOPA.ID_ELEGGIBILITA_FIT = 26 " +
          "      AND ISOPA.ID_ISOLA_PARCELLA = PC.ID_ISOLA_PARCELLA " + 
          "      AND PC.ID_CONDUZIONE_DICHIARATA = CD.ID_CONDUZIONE_DICHIARATA " + 
          "      GROUP BY ISOPA.ID_ISOLA_PARCELLA, " +
          "               ISO.ID_ILO, " +
          "               ISOPA.SUPERFICIE, " + 
          "               CD.ID_PARTICELLA " +
          "      ) ISOLA " +
          "WHERE  SUA.ID_AZIENDA =  ? " + 
          "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    EXISTS (SELECT * " +
          "               FROM    DB_UTE UT, " + 
          "                       DB_CONDUZIONE_PARTICELLA CP " + 
          "               WHERE   UT.DATA_FINE_ATTIVITA IS NULL " +
          "               AND     UT.ID_AZIENDA = SUA.ID_AZIENDA "+
          "               AND     CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "               AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
          "               AND     CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +    
          "              ) " +
          "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "AND    ISOLA.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "GROUP BY ID_UNITA_ARBOREA " +
          "HAVING COUNT(*) > 1";
              
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato

        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::getElencoIdUVParcelleDoppie] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
      stmt.setLong(2, idAzienda); 
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(vIdUnitaArborea == null)
        {
          vIdUnitaArborea = new Vector<Long>();
        }
        vIdUnitaArborea.add(new Long(rs.getLong("ID_UNITA_ARBOREA")));
      }
      
      return vIdUnitaArborea;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vIdUnitaArborea", vIdUnitaArborea) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[IsolaParcellaGaaDAO::getElencoIdUVParcelleDoppie] ", t,
          query, variabili, parametri);

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
      SolmrLogger.debug(this,
          "[IsolaParcellaGaaDAO::getElencoIdUVParcelleDoppie] END.");
    }
  }
  
  
  /**
   * 
   * Elenco delel parcelle associate alla UV che appartiene a più
   * parcelle
   * 
   * 
   * @param idAzienda
   * @param idUnitaArborea
   * @return
   * @throws DataAccessException
   */
  public Vector<IsolaParcellaVO> getElencoIsoleParcelleDoppiePerUV(long idAzienda,long idUnitaArborea)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<IsolaParcellaVO> vIsolaParcella = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::getElencoIsoleParcelleDoppiePerUV] BEGIN.");
    
      query = "" +
          "SELECT " +
          "      ISOLA.ID_ISOLA_PARCELLA, " + 
          "      ISOLA.ID_ILO, " +
          "      ISOLA.SUPERFICIE, " +
          "      SUA.ID_UNITA_ARBOREA, " +
          "      (SELECT UP.PARCELLA_SELEZIONATA " +
          "       FROM   DB_UNAR_PARCELLA UP " +
          "       WHERE  UP.ID_ISOLA_PARCELLA = ISOLA.ID_ISOLA_PARCELLA " +
          "       AND    UP.ID_UNITA_ARBOREA = SUA.ID_UNITA_ARBOREA " +
          "      ) AS PARCELLA_SELEZIONATA " +
          "FROM  DB_STORICO_UNITA_ARBOREA SUA, " + 
          "       (SELECT ISOPA.ID_ISOLA_PARCELLA, " + 
          "               ISO.ID_ILO, " +
          "               ISOPA.SUPERFICIE, " + 
          "               CD.ID_PARTICELLA " +
          "        FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " + 
          "               DB_ISOLA_DICHIARATA IDICH, " +
          "               DB_ISOLA ISO, " +
          "               DB_ISOLA_PARCELLA ISOPA, " + 
          "               DB_PARCELLA_CONDUZIONE PC, " +
          "               DB_CONDUZIONE_DICHIARATA CD " +
          "        WHERE  DC.ID_AZIENDA = ? " +
          "        AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " + 
          "               (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " + 
          "                FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, " +
          "                       DB_TIPO_MOTIVO_DICHIARAZIONE TMC, " +
          "                       DB_ISOLA_DICHIARATA IDICH1 " +
          "                WHERE  DC1.ID_MOTIVO_DICHIARAZIONE = TMC.ID_MOTIVO_DICHIARAZIONE " +
          "                AND    IDICH1.DATA_FINE_VALIDITA IS NULL " +
          "                AND    TMC.TIPO_DICHIARAZIONE <> 'C' " +
          "                AND    DC1.ID_DICHIARAZIONE_CONSISTENZA = IDICH1.ID_DICHIARAZIONE_CONSISTENZA " + 
          "                AND    DC1.ID_AZIENDA = DC.ID_AZIENDA " +
          "                AND    DC1.FLAG_AGGIORNAMENTO_ISOLE = 'S' " +
          "               ) " +
          "       AND DC.ID_DICHIARAZIONE_CONSISTENZA = IDICH.ID_DICHIARAZIONE_CONSISTENZA " +
          "       AND IDICH.DATA_FINE_VALIDITA IS NULL " +
          "       AND IDICH.ID_ISOLA_DICHIARATA = ISO.ID_ISOLA_DICHIARATA " + 
          "       AND ISO.ID_ISOLA = ISOPA.ID_ISOLA " +
          "       AND ISOPA.ID_ELEGGIBILITA_FIT = 26 " +
          "       AND ISOPA.ID_ISOLA_PARCELLA = PC.ID_ISOLA_PARCELLA " + 
          "       AND PC.ID_CONDUZIONE_DICHIARATA = CD.ID_CONDUZIONE_DICHIARATA " + 
          "       GROUP BY ISOPA.ID_ISOLA_PARCELLA, " +
          "                ISO.ID_ILO, " +
          "                ISOPA.SUPERFICIE, " + 
          "                CD.ID_PARTICELLA " +
          "      ) ISOLA " +
          "WHERE  SUA.ID_AZIENDA =  ? " +
          "AND    SUA.ID_UNITA_ARBOREA = ? " + 
          "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    EXISTS (SELECT * " +
          "               FROM    DB_UTE UT, " + 
          "                       DB_CONDUZIONE_PARTICELLA CP " + 
          "               WHERE   UT.DATA_FINE_ATTIVITA IS NULL " +
          "               AND     UT.ID_AZIENDA = SUA.ID_AZIENDA "+
          "               AND     CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "               AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
          "               AND     CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +    
          "              ) " +
          "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "AND    ISOLA.ID_PARTICELLA = SUA.ID_PARTICELLA";    
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::getElencoIsoleParcelleDoppiePerUV] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
      stmt.setLong(2, idAzienda);
      stmt.setLong(3, idUnitaArborea); 
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(vIsolaParcella == null)
        {
          vIsolaParcella = new Vector<IsolaParcellaVO>();
        }
        IsolaParcellaVO isolaParcellaVO = new IsolaParcellaVO();
        isolaParcellaVO.setIdIsolaParcella(checkLongNull(rs.getString("ID_ISOLA_PARCELLA")));
        isolaParcellaVO.setIdIlo(checkLongNull(rs.getString("ID_ILO")));
        isolaParcellaVO.setSupParcella(rs.getBigDecimal("SUPERFICIE"));
        isolaParcellaVO.setParcellaSelezionata(rs.getString("PARCELLA_SELEZIONATA"));
        
        vIsolaParcella.add(isolaParcellaVO);
      }
      
      return vIsolaParcella;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vIsolaParcella", vIsolaParcella) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idUnitaArborea", idUnitaArborea)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[IsolaParcellaGaaDAO::getElencoIsoleParcelleDoppiePerUV] ", t,
          query, variabili, parametri);
    
      
      //Rimappo e rilancio l'eccezione come DataAccessException.
      
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
       //Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
       //ignora ogni eventuale eccezione)
       
      close(null, stmt, conn);
    
      // Fine metodo
      SolmrLogger.debug(this,
          "[IsolaParcellaGaaDAO::getElencoIsoleParcelleDoppiePerUV] END.");
    }
  }
  
  
  
  public Vector<StoricoParticellaVO> getElencoUVIsoleParcelleByIdUnitaArborea(
      long idAzienda, Vector<Long> vIdUnitaArboree, long idUtente)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<StoricoParticellaVO> vElencoUV = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::getElencoUVIsoleParcelleByIdUnitaArborea] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "SELECT SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SP.SUP_CATASTALE, " +
        "       SP.SUPERFICIE_GRAFICA, " +
        "       SUA.ID_STORICO_UNITA_ARBOREA, " +
        "       SUA.ID_UNITA_ARBOREA, " +
        "       SUA.ID_PARTICELLA, " +
        "       SUA.PROGR_UNAR, " +
        "       SUA.ID_VARIETA, " +
        "       TVAR.DESCRIZIONE AS DESC_VARIETA, " +
        "       TVAR.CODICE_VARIETA AS COD_VAR, " +
        "       SUA.AREA, " +
        "       SUA.DATA_IMPIANTO, " +
        "       SUA.SESTO_SU_FILA, " +
        "       SUA.SESTO_TRA_FILE " +
        "FROM   DB_STORICO_PARTICELLA SP, " +
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_TIPO_VARIETA TVAR " +
        "WHERE  SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "AND    SUA.ID_VARIETA = TVAR.ID_VARIETA(+) " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
        "AND    SUA.ID_UNITA_ARBOREA IN ( ").append(
            getIdListFromVectorLongForSQL(vIdUnitaArboree)).append(")" +
        "ORDER BY C.DESCOM, " +
        "         SP.SEZIONE, " +
        "         SP.FOGLIO, " +
        "         SP.PARTICELLA, " +
        "         SP.SUBALTERNO, " +
        "         SUA.PROGR_UNAR, " +
        "         TVAR.DESCRIZIONE ");
      
      
      query = queryBuf.toString();
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[IsolaParcellaGaaDAO::getElencoUVIsoleParcelleByIdUnitaArborea] Query="
                    + query);
      }
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(vElencoUV == null)
        {
          vElencoUV = new Vector<StoricoParticellaVO>();
        }
        StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
        StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
        storicoParticellaVO.setIstatComune(rs.getString("COMUNE"));
        ComuneVO comuneVO = new ComuneVO();
        comuneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        comuneVO.setDescom(rs.getString("DESCOM"));
        storicoParticellaVO.setComuneParticellaVO(comuneVO);
        storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
        storicoParticellaVO.setSupCatastale(rs.getString("SUP_CATASTALE"));
        storicoParticellaVO.setSuperficieGrafica(rs.getString("SUPERFICIE_GRAFICA"));
        storicoUnitaArboreaVO.setIdStoricoUnitaArborea(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
        long idUnitaArborea = rs.getLong("ID_UNITA_ARBOREA");
        storicoUnitaArboreaVO.setIdUnitaArborea(new Long(idUnitaArborea));
        storicoUnitaArboreaVO.setIdParticella(new Long(rs.getLong("ID_PARTICELLA")));
        storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
          storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
          tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
          tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
          tipoVarietaVO.setCodiceVarieta(rs.getString("COD_VAR"));
          storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
        }
        storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
        storicoUnitaArboreaVO.setDataImpianto(rs.getTimestamp("DATA_IMPIANTO"));
        storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        
        Vector<IsolaParcellaVO> vIsolaParcella = getElencoIsoleParcelleDoppiePerUV(idAzienda, idUnitaArborea);
        
        //Inserisco sulla tabella DB_UNAR_PARCELLA
        //se non esiste già il record
        if(vIsolaParcella != null)
        {
          for(int i=0;i<vIsolaParcella.size();i++)
          {
             Long idUnarParcella = existRecordOnDBUnarParcella(
                 vIsolaParcella.get(i).getIdIsolaParcella().longValue(), idUnitaArborea); 
             if(idUnarParcella == null)
             {
               idUnarParcella = insertDBUnarParcella(idUnitaArborea, vIsolaParcella.get(i).getIdIsolaParcella().longValue(), 
                   idUtente);
             }
             vIsolaParcella.get(i).setIdUnarParcella(idUnarParcella);
          }          
        }        
        
        storicoUnitaArboreaVO.setvIsolaParcella(vIsolaParcella);
        
        storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);
        
        vElencoUV.add(storicoParticellaVO);
      }
      
      return vElencoUV;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("vElencoUV", vElencoUV)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdUnitaArboree", vIdUnitaArboree) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[IsolaParcellaGaaDAO::getElencoUVIsoleParcelleByIdUnitaArborea] ", t,
          query, variabili, parametri);
    
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
      SolmrLogger.debug(this,
          "[IsolaParcellaGaaDAO::getElencoUVIsoleParcelleByIdUnitaArborea] END.");
    }
  }
  
  
  
  public Long insertDBUnarParcella(long idUnitaArborea, long idIsolaParcella, long idUtente) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Long idUnarParcella = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::insertDBUnarParcella] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
      
      idUnarParcella = getNextPrimaryKey(SolmrConstants.SEQ_DB_UNAR_PARCELLA);
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   INSERT INTO DB_UNAR_PARCELLA   " 
              + "     (ID_UNAR_PARCELLA, "
              + "     ID_UNITA_ARBOREA, "
              + "     ID_ISOLA_PARCELLA,   "
              + "     PARCELLA_SELEZIONATA, "
              + "     DATA_AGGIORNAMENTO, "
              + "     ID_UTENTE_AGGIORNAMENTO) "
              + "   VALUES(?,?,?,?,?,?) ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::insertDBUnarParcella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idUnarParcella.longValue());
      stmt.setLong(++indice, idUnitaArborea);
      stmt.setLong(++indice, idIsolaParcella);
      stmt.setString(++indice, "N");
      stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
      stmt.setLong(++indice, idUtente);
      
      
  
      stmt.executeUpdate();
      
      return idUnarParcella;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
        new Variabile("idUnarParcella", idUnarParcella)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUnitaArborea", idUnitaArborea),
        new Parametro("idIsolaParcella", idIsolaParcella),
        new Parametro("idUtente", idUtente) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[IsolaParcellaGaaDAO::insertDBUnarParcella] ",
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
              "[IsolaParcellaGaaDAO::insertDBUnarParcella] END.");
    }
  }
  
  /**
   * 
   * ritorna true se esit
   * ste già un record sulal tabella
   * rispetto ai parametri passati
   * 
   * @param idIsolaParcella
   * @param idUnitaArborea
   * @return
   * @throws DataAccessException
   */
  public Long existRecordOnDBUnarParcella(long idIsolaParcella, long idUnitaArborea)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Long result = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::existRecordOnDBUnarParcella] BEGIN.");
    
      query = "" +
          "SELECT " +
          "      ID_UNAR_PARCELLA " +
          "FROM  DB_UNAR_PARCELLA " +
          "WHERE ID_ISOLA_PARCELLA =  ? " +
          "AND   ID_UNITA_ARBOREA = ? ";    
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::existRecordOnDBUnarParcella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idIsolaParcella);      
      stmt.setLong(2, idUnitaArborea);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = checkLongNull(rs.getString("ID_UNAR_PARCELLA"));
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("result", result) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idIsolaParcella", idIsolaParcella),
        new Parametro("idUnitaArborea", idUnitaArborea)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[IsolaParcellaGaaDAO::existRecordOnDBUnarParcella] ", t,
          query, variabili, parametri);
    
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
      SolmrLogger.debug(this,
          "[IsolaParcellaGaaDAO::existRecordOnDBUnarParcella] END.");
    }
  }
  
  
  /**
   * Aggiorna la tabella db_dichiarazione_consistenza
   * evidenziando il fatto che si è passati dall'associa parcella
   * 
   * 
   * 
   * @param idDichiarazioneConsistenza
   * @throws DataAccessException
   */
  public void updateDichCons(long idDichiarazioneConsistenza) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::updateDichCons] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_DICHIARAZIONE_CONSISTENZA   " 
              + "     SET DATA_CONNESSIONE_UNAR_PARCELLA = SYSDATE "
              + "   WHERE  "
              + "     ID_DICHIARAZIONE_CONSISTENZA = ?  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::updateDichCons] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idDichiarazioneConsistenza);     
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[IsolaParcellaGaaDAO::updateDichCons] ",
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
              "[IsolaParcellaGaaDAO::updateDichCons] END.");
    }
  }
  
  
  /**
   * 
   * restituisce tutti gli idUnarParcella relativi all'azienda in questione
   * (sarebbero le uv che appartengono a più parcelle)
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> getElencoIdUnarParcella(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<Long> vIdUnarParcella = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::getElencoIdUnarParcella] BEGIN.");
    
      query = "" +
          "SELECT " +
          "      UP.ID_UNAR_PARCELLA " +
          "FROM  DB_STORICO_UNITA_ARBOREA SUA, " +
          "      DB_UNAR_PARCELLA UP, " +
          "       (SELECT ISOPA.ID_ISOLA_PARCELLA, " + 
          "               ISO.ID_ILO, " +
          "               ISOPA.SUPERFICIE, " + 
          "               CD.ID_PARTICELLA " +
          "        FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " + 
          "               DB_ISOLA_DICHIARATA IDICH, " +
          "               DB_ISOLA ISO, " +
          "               DB_ISOLA_PARCELLA ISOPA, " + 
          "               DB_PARCELLA_CONDUZIONE PC, " +
          "               DB_CONDUZIONE_DICHIARATA CD " +
          "        WHERE  DC.ID_AZIENDA = ? " +
          "        AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " +
          "               (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " +
          "                FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, " +
          "                       DB_TIPO_MOTIVO_DICHIARAZIONE TMC, " +
          "                       DB_ISOLA_DICHIARATA IDICH1 " +
          "                WHERE  DC1.ID_MOTIVO_DICHIARAZIONE = TMC.ID_MOTIVO_DICHIARAZIONE " +
          "                AND    IDICH1.DATA_FINE_VALIDITA IS NULL " +
          "                AND    TMC.TIPO_DICHIARAZIONE <> 'C' " +
          "                AND    DC1.ID_DICHIARAZIONE_CONSISTENZA = IDICH1.ID_DICHIARAZIONE_CONSISTENZA " + 
          "                AND    DC1.ID_AZIENDA = DC.ID_AZIENDA " +
          "                AND    DC1.FLAG_AGGIORNAMENTO_ISOLE = 'S' " +
          "               ) " +
          "       AND DC.ID_DICHIARAZIONE_CONSISTENZA = IDICH.ID_DICHIARAZIONE_CONSISTENZA " +
          "       AND IDICH.DATA_FINE_VALIDITA IS NULL " +
          "       AND IDICH.ID_ISOLA_DICHIARATA = ISO.ID_ISOLA_DICHIARATA " + 
          "       AND ISO.ID_ISOLA = ISOPA.ID_ISOLA " +
          "       AND ISOPA.ID_ELEGGIBILITA_FIT = 26 " +
          "       AND ISOPA.ID_ISOLA_PARCELLA = PC.ID_ISOLA_PARCELLA " +
          "       AND PC.ID_CONDUZIONE_DICHIARATA = CD.ID_CONDUZIONE_DICHIARATA " +
          "       GROUP BY ISOPA.ID_ISOLA_PARCELLA, " +
          "                ISO.ID_ILO, " +
          "                ISOPA.SUPERFICIE, " + 
          "                CD.ID_PARTICELLA " +
          "      ) ISOLA " +
          "WHERE  SUA.ID_AZIENDA =  ? " +
          "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    EXISTS (SELECT * " +
          "               FROM    DB_UTE UT, " + 
          "                       DB_CONDUZIONE_PARTICELLA CP " + 
          "               WHERE   UT.DATA_FINE_ATTIVITA IS NULL " +
          "               AND     UT.ID_AZIENDA = SUA.ID_AZIENDA "+
          "               AND     CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "               AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
          "               AND     CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +    
          "              ) " +
          "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "AND    ISOLA.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "AND    SUA.ID_UNITA_ARBOREA = UP.ID_UNITA_ARBOREA " +
          "AND    ISOLA.ID_ISOLA_PARCELLA = UP.ID_ISOLA_PARCELLA";    
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::getElencoIdUnarParcella] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
      stmt.setLong(2, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(vIdUnarParcella == null)
        {
          vIdUnarParcella = new Vector<Long>();
        }        
        vIdUnarParcella.add(new Long(rs.getLong("ID_UNAR_PARCELLA")));
      }
      
      return vIdUnarParcella;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vIdUnarParcella", vIdUnarParcella) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[IsolaParcellaGaaDAO::getElencoIdUnarParcella] ", t,
          query, variabili, parametri);
    
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
      SolmrLogger.debug(this,
          "[IsolaParcellaGaaDAO::getElencoIdUnarParcella] END.");
    }
  }
  
  
  
  public void updateDBUnarParcellaConferma(Vector<Long> vIdUnarParcella, 
      Vector<Long> vIdUnarParcellaSel, long idUtente) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::updateDBUnarParcellaConferma] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf.append(""
         + "UPDATE DB_UNAR_PARCELLA   " 
         + "SET PARCELLA_SELEZIONATA = ?, "
         + "    DATA_AGGIORNAMENTO = ?, "
         + "    ID_UTENTE_AGGIORNAMENTO = ? "
         + "WHERE ID_UNAR_PARCELLA = ? ");
             
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::updateDBUnarParcellaConferma] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      
      for (int i=0;i<vIdUnarParcella.size();i++)
      {
        int indice = 0;
        Long idUnarParcella = vIdUnarParcella.get(i);
        String parcellaSelezionata = "N";
        if((vIdUnarParcellaSel !=null) && vIdUnarParcellaSel.contains(idUnarParcella))
        {
          parcellaSelezionata = "S";
        }
        
        stmt.setString(++indice, parcellaSelezionata); 
        stmt.setTimestamp(++indice, convertDateToTimestamp(new Date()));
        stmt.setLong(++indice, idUtente);
        stmt.setLong(++indice, idUnarParcella.longValue());
        
        stmt.addBatch();
      }
      
      
  
      stmt.executeBatch();
      
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), 
        new Variabile("queryBuf", queryBuf)
        };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdUnarParcella", vIdUnarParcella),
        new Parametro("vIdUnarParcellaSel", vIdUnarParcellaSel),
        new Parametro("idUtente", idUtente) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[IsolaParcellaGaaDAO::updateDBUnarParcellaConferma] ",
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
              "[IsolaParcellaGaaDAO::updateDBUnarParcellaConferma] END.");
    }
  }
  
  
  
  
  /**
   * restituisce i dati relativi all'ultima dihiarazione
   * di consisetenza a cui sono associate parcelle
   * 
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public ConsistenzaVO getDichConsUVParcelleDoppie(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    ConsistenzaVO result = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::getDichConsUVParcelleDoppie] BEGIN.");
    
      query = "" +
          "SELECT  " +     
          "       SUA.ID_UNITA_ARBOREA," +
          "       ISOLA.ID_DICHIARAZIONE_CONSISTENZA, " +
          "       ISOLA.DATA_CONNESSIONE_UNAR_PARCELLA " + 
          "FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
          "       (SELECT ISOPA.ID_ISOLA_PARCELLA, " + 
          "               ISO.ID_ILO, " +
          "               ISOPA.SUPERFICIE, " + 
          "               CD.ID_PARTICELLA, " +
          "               DC.ID_DICHIARAZIONE_CONSISTENZA, " +
          "               DC.DATA_CONNESSIONE_UNAR_PARCELLA " +
          "        FROM   DB_DICHIARAZIONE_CONSISTENZA DC, " + 
          "               DB_ISOLA_DICHIARATA IDICH, " +
          "               DB_ISOLA ISO, " +
          "               DB_ISOLA_PARCELLA ISOPA, " + 
          "               DB_PARCELLA_CONDUZIONE PC, " +
          "               DB_CONDUZIONE_DICHIARATA CD " +
          "        WHERE  DC.ID_AZIENDA = ? " +
          "        AND    DC.DATA_INSERIMENTO_DICHIARAZIONE = " + 
          "               (SELECT MAX(DC1.DATA_INSERIMENTO_DICHIARAZIONE) " + 
          "               FROM   DB_DICHIARAZIONE_CONSISTENZA DC1, " +
          "                      DB_TIPO_MOTIVO_DICHIARAZIONE TMC, " +
          "                      DB_ISOLA_DICHIARATA IDICH1 " +
          "               WHERE  DC1.ID_MOTIVO_DICHIARAZIONE = TMC.ID_MOTIVO_DICHIARAZIONE " + 
          "               AND    IDICH1.DATA_FINE_VALIDITA IS NULL " +
          "               AND    TMC.TIPO_DICHIARAZIONE <> 'C' " +
          "               AND    DC1.ID_DICHIARAZIONE_CONSISTENZA = IDICH1.ID_DICHIARAZIONE_CONSISTENZA " + 
          "               AND    DC1.ID_AZIENDA = DC.ID_AZIENDA " +
          "               AND    DC1.FLAG_AGGIORNAMENTO_ISOLE = 'S' " +
          "               ) " +
          "      AND DC.ID_DICHIARAZIONE_CONSISTENZA = IDICH.ID_DICHIARAZIONE_CONSISTENZA " +
          "      AND IDICH.DATA_FINE_VALIDITA IS NULL " +
          "      AND IDICH.ID_ISOLA_DICHIARATA = ISO.ID_ISOLA_DICHIARATA " + 
          "      AND ISO.ID_ISOLA = ISOPA.ID_ISOLA " +
          "      AND ISOPA.ID_ELEGGIBILITA_FIT = 26 " +
          "      AND ISOPA.ID_ISOLA_PARCELLA = PC.ID_ISOLA_PARCELLA " + 
          "      AND PC.ID_CONDUZIONE_DICHIARATA = CD.ID_CONDUZIONE_DICHIARATA " + 
          "      GROUP BY ISOPA.ID_ISOLA_PARCELLA, " +
          "               ISO.ID_ILO, " +
          "               ISOPA.SUPERFICIE, " + 
          "               CD.ID_PARTICELLA, " +
          "               DC.ID_DICHIARAZIONE_CONSISTENZA, " +
          "               DC.DATA_CONNESSIONE_UNAR_PARCELLA " +
          "      ) ISOLA " +
          "WHERE  SUA.ID_AZIENDA =  ? " +
          "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    EXISTS (SELECT * " +
          "               FROM    DB_UTE UT, " + 
          "                       DB_CONDUZIONE_PARTICELLA CP " + 
          "               WHERE   UT.DATA_FINE_ATTIVITA IS NULL " +
          "               AND     UT.ID_AZIENDA = SUA.ID_AZIENDA "+
          "               AND     CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "               AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
          "               AND     CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +    
          "              ) " +
          "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "AND    ISOLA.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "GROUP BY SUA.ID_UNITA_ARBOREA, " +
          "         ISOLA.ID_DICHIARAZIONE_CONSISTENZA, " +
          "         ISOLA.DATA_CONNESSIONE_UNAR_PARCELLA " +
          "HAVING COUNT(*) > 1";
              
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::getDichConsUVParcelleDoppie] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
      stmt.setLong(2, idAzienda); 
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = new ConsistenzaVO();
        result.setIdDichiarazioneConsistenza(rs.getString("ID_DICHIARAZIONE_CONSISTENZA"));
        result.setDataConnessioneUnarParcella(rs.getTimestamp("DATA_CONNESSIONE_UNAR_PARCELLA"));
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("result", result) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[IsolaParcellaGaaDAO::getDichConsUVParcelleDoppie] ", t,
          query, variabili, parametri);
    
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
      SolmrLogger.debug(this,
          "[IsolaParcellaGaaDAO::getDichConsUVParcelleDoppie] END.");
    }
  }
  
  
  
  public HashMap<Long,Vector<Long>> getHashIdParticellaIdStoricoUnitaArborea(long idIsolaParcella, long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    HashMap<Long,Vector<Long>> result = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::getHashIdParticellaIdStoricoUnitaArborea] BEGIN.");
    
      query = "" +
          "SELECT ISOLA.ID_ISOLA_PARCELLA, " +
          "       ISOLA.ID_PARTICELLA, " +
          "       SUA.ID_STORICO_UNITA_ARBOREA " +
          "FROM  DB_STORICO_UNITA_ARBOREA SUA, " +
          "       (SELECT ISOPA.ID_ISOLA_PARCELLA, " +
          "               CD.ID_PARTICELLA " +
          "        FROM   DB_ISOLA_PARCELLA ISOPA, " + 
          "               DB_PARCELLA_CONDUZIONE PC, " +
          "               DB_CONDUZIONE_DICHIARATA CD " +
          "       WHERE   ISOPA.ID_ISOLA_PARCELLA = ? " +
          "       AND ISOPA.ID_ISOLA_PARCELLA = PC.ID_ISOLA_PARCELLA " + 
          "       AND PC.ID_CONDUZIONE_DICHIARATA = CD.ID_CONDUZIONE_DICHIARATA " +
          "       AND ISOPA.ID_ELEGGIBILITA_FIT = 26 " +
          "       GROUP BY ISOPA.ID_ISOLA_PARCELLA, " +
          "                CD.ID_PARTICELLA " +
          "      ) ISOLA " +
          "WHERE  SUA.ID_AZIENDA = ? " +
          "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    EXISTS (SELECT * " +
          "               FROM    DB_UTE UT, " + 
          "                       DB_CONDUZIONE_PARTICELLA CP " + 
          "               WHERE   UT.DATA_FINE_ATTIVITA IS NULL " +
          "               AND     UT.ID_AZIENDA = SUA.ID_AZIENDA "+
          "               AND     CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "               AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
          "               AND     CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +    
          "              ) " +
          "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "AND    ISOLA.ID_PARTICELLA = SUA.ID_PARTICELLA";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::getHashIdParticellaIdStoricoUnitaArborea] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idIsolaParcella);      
      stmt.setLong(2, idAzienda); 
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(result == null)
        {
          result = new HashMap<Long,Vector<Long>>();
        }
        Long idParticella = new Long(rs.getLong("ID_PARTICELLA"));
        
        if(result.get(idParticella) != null)
        {
          Vector<Long> vIdStoricoUnitaArborea = result.get(idParticella);
          vIdStoricoUnitaArborea.add(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
          result.put(idParticella, vIdStoricoUnitaArborea);
        }
        else
        {
          Vector<Long> vIdStoricoUnitaArborea = new Vector<Long>();
          vIdStoricoUnitaArborea.add(new Long(rs.getLong("ID_STORICO_UNITA_ARBOREA")));
          result.put(idParticella, vIdStoricoUnitaArborea);
        }
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("result", result) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idIsolaParcella", idIsolaParcella),
        new Parametro("idAzienda", idAzienda)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[IsolaParcellaGaaDAO::getHashIdParticellaIdStoricoUnitaArborea] ", t,
          query, variabili, parametri);
    
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
      SolmrLogger.debug(this,
          "[IsolaParcellaGaaDAO::getHashIdParticellaIdStoricoUnitaArborea] END.");
    }
  }
  
  
  
  /*public Integer getTolleranzaPlSql(String nomeLib, long idAzienda, long idUnitaArborea) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    Integer tolleranza = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::getTolleranzaPlSql] BEGIN.");
      
       // FUNCTION Uv_In_Tolleranza_Gis (pIdAzienda    IN DB_AZIENDA.ID_AZIENDA%TYPE,
         //                        pIdParticella IN DB_CONDUZIONE_PARTICELLA.ID_PARTICELLA%TYPE
           //                      ) RETURN INTEGER IS
       
  
      // CONCATENAZIONE/CREAZIONE QUERY BEGIN. 
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{ ? = call ").append(nomeLib).append(".Uv_In_Tolleranza_Gis(?,?)}");
      
      
      
      
      query = queryBuf.toString();
      // CONCATENAZIONE/CREAZIONE QUERY END. 
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[IsolaParcellaGaaDAO::getTolleranzaPlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.registerOutParameter(1,Types.INTEGER);
      cs.setLong(2, idAzienda);
      cs.setLong(3, idUnitaArborea);
      
      cs.executeUpdate();
      
      tolleranza = checkInt(cs.getString(1));
      
      
      
      
      return tolleranza;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("tolleranza", tolleranza) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idUnitaArborea", idUnitaArborea)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[IsolaParcellaGaaDAO::getTolleranzaPlSql] ",
              t, query, variabili, parametri);
      
      // Rimappo e rilancio l'eccezione come DataAccessException.
       
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      // Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
      // ignora ogni eventuale eccezione)
      
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[IsolaParcellaGaaDAO::getTolleranzaPlSql] END.");
    }
  } */
  
  
  
  
  
  
  
  
  
  
  
  
  
  

}
