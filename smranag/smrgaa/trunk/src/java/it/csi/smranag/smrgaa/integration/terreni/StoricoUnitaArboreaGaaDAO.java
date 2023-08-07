package it.csi.smranag.smrgaa.integration.terreni;

import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.CompensazioneAziendaVO;
import it.csi.smranag.smrgaa.dto.terreni.DirittoCompensazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.RiepiloghiUnitaArboreaVO;
import it.csi.smranag.smrgaa.dto.terreni.RiepilogoCompensazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.UVCompensazioneVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoUnitaArboreaVO;
import it.csi.solmr.dto.anag.terreni.TipoVarietaVO;
import it.csi.solmr.dto.anag.terreni.VignaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

public class StoricoUnitaArboreaGaaDAO extends it.csi.solmr.integration.BaseDAO
{

  public StoricoUnitaArboreaGaaDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public StoricoUnitaArboreaGaaDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  /**
   * 
   * Usata nel riepilogo destinazione produttiva comune.
   * Restituisce l'elenco delle unita vitate relativamente ai comuni
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComune(long idAzienda)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<RiepiloghiUnitaArboreaVO> vRiepiloghi = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaComune] BEGIN.");
      query = " " +
          "WITH UNITA_VITATE " +
          " AS (SELECT SUA.ID_STORICO_UNITA_ARBOREA AS ID_STORICO_UNITA_ARBOREA, " +
          "            SUA.ID_PARTICELLA AS ID_PARTICELLA, " +
          "            RCM.ID_UTILIZZO AS ID_UTILIZZO, " +
          "            RCM.FLAG_TIPO_UVA, " +
          "            SUA.AREA AS AREA " +
          "     FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
          "            DB_UTE U, " +
          "            DB_CONDUZIONE_PARTICELLA CP, " +
          "            DB_R_CATALOGO_MATRICE RCM " +
          "     WHERE  SUA.ID_AZIENDA = ? " +
          "     AND    SUA.ID_AZIENDA = U.ID_AZIENDA " +
          "     AND    U.DATA_FINE_ATTIVITA IS NULL " +
          "     AND    U.ID_UTE = CP.ID_UTE " +
          "     AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "     AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "     AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "     AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "     AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "     GROUP BY SUA.ID_STORICO_UNITA_ARBOREA, " +
          "              SUA.ID_PARTICELLA, " +
          "              RCM.ID_UTILIZZO, " +
          "              RCM.FLAG_TIPO_UVA, " +
          "              SUA.AREA ) " +
          "SELECT PROV.DESCRIZIONE, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       COM.DESCOM, " +
          "       COM.ISTAT_COMUNE, " +
          "       SUM (DECODE (UA.FLAG_TIPO_UVA, '"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_VINO+"', NVL (UA.AREA, 0), 0)) " +
          "          AS SUM_UVA_DA_VINO, " +
          "       SUM (DECODE (UA.FLAG_TIPO_UVA,'"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_MENSA+"', NVL (UA.AREA, 0), 0)) " +
          "          AS SUM_UVA_DA_MENSA, " +
          "       SUM (DECODE (UA.FLAG_TIPO_UVA, '"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_VINO+"', 0,  '"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_MENSA+"', 0,  NVL (UA.AREA, 0))) " +
          "          AS ALTRE_DESTINAZIONI, " +
          "       SUM (NVL (UA.AREA, 0)) AS SUP_TOTALE " +
          "  FROM UNITA_VITATE UA, " +
          "       DB_STORICO_PARTICELLA SP, " +
          "       COMUNE COM, " +
          "       PROVINCIA PROV " +
          " WHERE     UA.ID_PARTICELLA = SP.ID_PARTICELLA " +
          "       AND SP.DATA_FINE_VALIDITA IS NULL " +
          "       AND SP.COMUNE = COM.ISTAT_COMUNE " +
          "       AND COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "GROUP BY PROV.DESCRIZIONE, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       COM.DESCOM, " +
          "       COM.ISTAT_COMUNE " +
          "ORDER BY PROV.DESCRIZIONE, COM.DESCOM";
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaComune] Query="
                    + query);
      }

      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(vRiepiloghi == null)
        {
          vRiepiloghi = new Vector<RiepiloghiUnitaArboreaVO>();
        }
        
        RiepiloghiUnitaArboreaVO riepiloghiUnitaArboreaVO = new RiepiloghiUnitaArboreaVO();
        riepiloghiUnitaArboreaVO.setDescProv(rs.getString("DESCRIZIONE"));
        riepiloghiUnitaArboreaVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        riepiloghiUnitaArboreaVO.setDescComune(rs.getString("DESCOM"));
        riepiloghiUnitaArboreaVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        riepiloghiUnitaArboreaVO.setUvaDaVino(rs.getBigDecimal("SUM_UVA_DA_VINO"));
        riepiloghiUnitaArboreaVO.setUvaDaMensa(rs.getBigDecimal("SUM_UVA_DA_MENSA"));
        riepiloghiUnitaArboreaVO.setAltreDestinazioniProduttive(rs.getBigDecimal("ALTRE_DESTINAZIONI"));
        riepiloghiUnitaArboreaVO.setSupVitata(rs.getBigDecimal("SUP_TOTALE"));
        
        vRiepiloghi.add(riepiloghiUnitaArboreaVO);
      }
      
      return vRiepiloghi;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("vRiepiloghi", vRiepiloghi)};

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaComune] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaComune] END.");
    }
  }
  
  /**
   * 
   * Usata nel riepilogo destinazione produttiva UVA DA VINO.
   * Restituisce l'elenco delle unita vitate relativamente ai comuni
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVino(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<RiepiloghiUnitaArboreaVO> vRiepiloghi = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaUvaDaVino] BEGIN.");
      
      query = " " +
          "WITH UNITA_VITATE AS " +
          "     (SELECT SUA.ID_STORICO_UNITA_ARBOREA AS ID_STORICO_UNITA_ARBOREA, " +
          "             SUA.ID_PARTICELLA AS ID_PARTICELLA, " +
          "             SUA.SUPERFICIE_DA_ISCRIVERE_ALBO AS SUPERFICIE_DA_ISCRIVERE_ALBO, " +
          "             SUA.AREA AS AREA, " +
          "             TTV.VINO_DOC AS VINO_DOC " +
          "      FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
          "             DB_R_CATALOGO_MATRICE RCM, " +
          "             DB_TIPO_TIPOLOGIA_VINO TTV, " +
          "             DB_UTE U, " +
          "             DB_CONDUZIONE_PARTICELLA CP " +
          "      WHERE  SUA.ID_AZIENDA = ? " +
          "      AND    SUA.ID_AZIENDA = U.ID_AZIENDA " +
          "      AND    U.DATA_FINE_ATTIVITA IS NULL " +
          "      AND    U.ID_UTE = CP.ID_UTE " +
          "      AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "      AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "      AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "      AND    SUA.ID_TIPOLOGIA_UNAR   = 2 " +
          "      AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "      AND    RCM.FLAG_TIPO_UVA = '"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_VINO+"' "+
          "      AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
          "      GROUP BY " +
          "             SUA.ID_STORICO_UNITA_ARBOREA, " +
          "             SUA.ID_PARTICELLA, " +
          "             SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
          "             SUA.AREA, " +
          "             TTV.VINO_DOC) " +  
          "SELECT PROV.DESCRIZIONE, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       COM.DESCOM, " +
          "       COM.ISTAT_COMUNE, " +
          "       SUM(DECODE(UA.VINO_DOC, 'S', NVL(UA.AREA,0), 0)) AS SUM_SUP_VITATA_DOP, " +
          "       SUM(DECODE(UA.VINO_DOC, 'S', NVL(UA.SUPERFICIE_DA_ISCRIVERE_ALBO,0), 0)) AS SUM_SUP_ISCRITTA_DOP, " +
          "       SUM(DECODE(NVL(UA.VINO_DOC,'N'), 'N', NVL(UA.AREA,0), 0)) AS SUM_SUP_VITATA_VINO_TAVOLA, " +
          "       SUM(DECODE(NVL(UA.VINO_DOC,'N'), 'N', NVL(UA.SUPERFICIE_DA_ISCRIVERE_ALBO,0), 0)) AS SUM_SUP_ISCRITTA_VINO_TAVOLA, " +
          "       SUM(NVL(UA.AREA,0)) AS SUP_VITATA, " +
          "       SUM(NVL(UA.SUPERFICIE_DA_ISCRIVERE_ALBO,0)) AS SUP_ISCRITTA " +
          "FROM   UNITA_VITATE UA, " +
          "       DB_STORICO_PARTICELLA SP, " +
          "       COMUNE COM, " +
          "       PROVINCIA PROV " +
          "WHERE  UA.ID_PARTICELLA = SP.ID_PARTICELLA " +
          "AND    SP.DATA_FINE_VALIDITA IS NULL " +
          "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "GROUP BY " +
          "       PROV.DESCRIZIONE, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       COM.DESCOM, " +
          "       COM.ISTAT_COMUNE " +
          "ORDER BY " +
          "       PROV.DESCRIZIONE, " +
          "       COM.DESCOM ";
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaUvaDaVino] Query="
                    + query);
      }
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(vRiepiloghi == null)
        {
          vRiepiloghi = new Vector<RiepiloghiUnitaArboreaVO>();
        }
        
        RiepiloghiUnitaArboreaVO riepiloghiUnitaArboreaVO = new RiepiloghiUnitaArboreaVO();
        riepiloghiUnitaArboreaVO.setDescProv(rs.getString("DESCRIZIONE"));
        riepiloghiUnitaArboreaVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        riepiloghiUnitaArboreaVO.setDescComune(rs.getString("DESCOM"));
        riepiloghiUnitaArboreaVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        riepiloghiUnitaArboreaVO.setSupVitataDOP(rs.getBigDecimal("SUM_SUP_VITATA_DOP"));
        riepiloghiUnitaArboreaVO.setSupIscrittaDOP(rs.getBigDecimal("SUM_SUP_ISCRITTA_DOP"));
        riepiloghiUnitaArboreaVO.setSupVitataVinoTavola(rs.getBigDecimal("SUM_SUP_VITATA_VINO_TAVOLA"));
        riepiloghiUnitaArboreaVO.setSupIscrittaVinoTavola(rs.getBigDecimal("SUM_SUP_ISCRITTA_VINO_TAVOLA"));
        riepiloghiUnitaArboreaVO.setSupVitata(rs.getBigDecimal("SUP_VITATA"));
        riepiloghiUnitaArboreaVO.setSupIscritta(rs.getBigDecimal("SUP_ISCRITTA"));
        
        vRiepiloghi.add(riepiloghiUnitaArboreaVO);
      }
      
      return vRiepiloghi;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("vRiepiloghi", vRiepiloghi)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaUvaDaVino] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaUvaDaVino] END.");
    }
  }
  
  /**
   * 
   * Usata nel riepilogo destinazione produttiva per VINO DOP.
   * Restituisce l'elenco delle unita vitate relativamente alla descrizione del vino
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOP(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<RiepiloghiUnitaArboreaVO> vRiepiloghi = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaVinoDOP] BEGIN.");      
      
      query = " " +
          "WITH UNITA_VITATE AS " +
          "      (SELECT SUA.ID_STORICO_UNITA_ARBOREA AS ID_STORICO_UNITA_ARBOREA, " +
          "              SUA.ID_PARTICELLA AS ID_PARTICELLA, " +
          "              SUA.SUPERFICIE_DA_ISCRIVERE_ALBO AS SUPERFICIE_DA_ISCRIVERE_ALBO, " +
          "              SUA.AREA AS AREA, " +
          "              TTV.DESCRIZIONE AS DESCRIZIONE, " +
          "              TTV.ID_TIPOLOGIA_VINO AS ID_TIPOLOGIA_VINO " +
          "       FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
          "              DB_R_CATALOGO_MATRICE RCM, " +
          "              DB_TIPO_TIPOLOGIA_VINO TTV, " +
          "              DB_UTE U, " +
          "              DB_CONDUZIONE_PARTICELLA CP " +
          "       WHERE  SUA.ID_AZIENDA = ? " + 
          "       AND    SUA.ID_AZIENDA = U.ID_AZIENDA " +
          "       AND    U.DATA_FINE_ATTIVITA IS NULL " +
          "       AND    U.ID_UTE = CP.ID_UTE " +
          "       AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "       AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "       AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "       AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "       AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "       AND    RCM.FLAG_TIPO_UVA = '"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_VINO+"' "+
          "       AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " +
          "       AND    TTV.VINO_DOC = 'S' " +
          "       GROUP BY " +
          "              SUA.ID_STORICO_UNITA_ARBOREA, " +
          "              SUA.ID_PARTICELLA, " +
          "              SUA.SUPERFICIE_DA_ISCRIVERE_ALBO, " +
          "              SUA.AREA, " +
          "              TTV.DESCRIZIONE, " +
          "              TTV.ID_TIPOLOGIA_VINO  ) " +     
          "SELECT UA.DESCRIZIONE, " +
          "       UA.ID_TIPOLOGIA_VINO, " +
          "       COM.DESCOM, " +
          "       COM.ISTAT_COMUNE, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       SUM(NVL(UA.AREA,0)) AS SUP_VITATA, " +
          "       SUM(NVL(UA.SUPERFICIE_DA_ISCRIVERE_ALBO,0)) AS SUP_ISCRITTA " +
          "FROM   UNITA_VITATE UA, " +
          "       DB_STORICO_PARTICELLA SP, " +
          "       COMUNE COM, " +
          "       PROVINCIA PROV " +
          "WHERE  UA.ID_PARTICELLA = SP.ID_PARTICELLA " +
          "AND    SP.DATA_FINE_VALIDITA IS NULL " +
          "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "GROUP BY " +
          "       UA.DESCRIZIONE, " +
          "       UA.ID_TIPOLOGIA_VINO, " +
          "       COM.DESCOM, " +
          "       COM.ISTAT_COMUNE, " +
          "       PROV.SIGLA_PROVINCIA " +
          "ORDER BY " +
          "       UA.DESCRIZIONE, " +
          "       COM.DESCOM ";
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaVinoDOP] Query="
                    + query);
      }
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(vRiepiloghi == null)
        {
          vRiepiloghi = new Vector<RiepiloghiUnitaArboreaVO>();
        }
        
        RiepiloghiUnitaArboreaVO riepiloghiUnitaArboreaVO = new RiepiloghiUnitaArboreaVO();
        riepiloghiUnitaArboreaVO.setTipoTipolgiaVino(rs.getString("DESCRIZIONE"));
        riepiloghiUnitaArboreaVO.setIdTipolgiaVino(checkLongNull(rs.getString("ID_TIPOLOGIA_VINO")));
        riepiloghiUnitaArboreaVO.setDescComune(rs.getString("DESCOM"));
        riepiloghiUnitaArboreaVO.setIstatComune(rs.getString("ISTAT_COMUNE"));
        riepiloghiUnitaArboreaVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        riepiloghiUnitaArboreaVO.setSupVitata(rs.getBigDecimal("SUP_VITATA"));
        riepiloghiUnitaArboreaVO.setSupIscritta(rs.getBigDecimal("SUP_ISCRITTA"));
        
        vRiepiloghi.add(riepiloghiUnitaArboreaVO);
      }
      
      return vRiepiloghi;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("vRiepiloghi", vRiepiloghi)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaVinoDOP] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::riepilogoDestinazioneProduttivaVinoDOP] END.");
    }
  }
  
  
  /**
   * 
   * Usata nel riepilogo VINO DOP - provincia.
   * Restituisce l'elenco delle unita vitate relativamente alla descrizione del vino 
   * ed alal provincia
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOP(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<RiepiloghiUnitaArboreaVO> vRiepiloghi = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::riepilogoProvinciaVinoDOP] BEGIN.");      
      
      query = "" +
          "WITH UNITA_VITATE AS " +
          "      (SELECT SUA.ID_STORICO_UNITA_ARBOREA AS ID_STORICO_UNITA_ARBOREA, " + 
          "              SUA.ID_PARTICELLA AS ID_PARTICELLA, " +
          "              SUA.AREA AS AREA, " +
          "              TTV.DESCRIZIONE AS DESCRIZIONE, " + 
          "              TTV.ID_TIPOLOGIA_VINO AS ID_TIPOLOGIA_VINO, " +
          "              TTV.RESA, " +
          "              CASE " +
          "                  WHEN EXTRACT (YEAR FROM SUA.DATA_PRIMA_PRODUZIONE) < EXTRACT (YEAR FROM SYSDATE) " +
          "                   AND SUA.FLAG_IMPRODUTTIVA = 'N' " +
          "                  THEN SUA.AREA " +                           
          "                  ELSE 0 " +                           
          "              END AS SUA_AREA_100, " +
          "              CASE " +
          "                  WHEN EXTRACT (YEAR FROM SUA.DATA_PRIMA_PRODUZIONE) = EXTRACT (YEAR FROM SYSDATE) " +
          "                   AND SUA.FLAG_IMPRODUTTIVA = 'N' " + 
          "                  THEN SUA.AREA " +                           
          "                  ELSE 0 " +                           
          "              END AS SUA_AREA_70, " +
          "              CASE " +
          "                  WHEN EXTRACT (YEAR FROM SUA.DATA_PRIMA_PRODUZIONE) > EXTRACT (YEAR FROM SYSDATE) " +
          "                  OR SUA.FLAG_IMPRODUTTIVA = 'S' " + 
          "                  THEN SUA.AREA " +                           
          "                  ELSE 0 " +                           
          "              END AS SUA_AREA_0 " +
          "       FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
          "              DB_R_CATALOGO_MATRICE RCM, " +
          "              DB_TIPO_TIPOLOGIA_VINO TTV, " +
          "              DB_UTE U, " +
          "              DB_CONDUZIONE_PARTICELLA CP " +
          "       WHERE  SUA.ID_AZIENDA = ? " +
          "       AND    SUA.ID_AZIENDA = U.ID_AZIENDA " + 
          "       AND    U.DATA_FINE_ATTIVITA IS NULL " +
          "       AND    U.ID_UTE = CP.ID_UTE " +
          "       AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "       AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
          "       AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "       AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "       AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "       AND    RCM.FLAG_TIPO_UVA = '"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_VINO+"' "+
          "       AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " +
          "       GROUP BY SUA.ID_STORICO_UNITA_ARBOREA, " +
          "                SUA.ID_PARTICELLA, " +
          "                SUA.AREA, " +
          "                TTV.DESCRIZIONE, " +
          "                TTV.ID_TIPOLOGIA_VINO, " +
          "                TTV.RESA, " +
          "                SUA.DATA_PRIMA_PRODUZIONE," +
          "                SUA.FLAG_IMPRODUTTIVA )  " +     
          "SELECT UA.DESCRIZIONE, " +
          "       UA.ID_TIPOLOGIA_VINO, " +
          "       UA.RESA, " +
          "       PROV.ISTAT_PROVINCIA, " +
          "       PROV.DESCRIZIONE AS DESC_PROV, " +
          "       SUM(NVL(UA.SUA_AREA_100,0)) AS SUP_VITATA_100, " +
          "       SUM(NVL(UA.SUA_AREA_70,0)) AS SUP_VITATA_70, " +
          "       SUM(NVL(UA.SUA_AREA_0,0)) AS SUP_VITATA_0, " +
          "       ROUND ((SUM (NVL (UA.SUA_AREA_100, 0)  * NVL (UA.RESA, 0))), 2) AS PROD_RESA_100, " +
          "       ROUND ((SUM (NVL (UA.SUA_AREA_70, 0) * 0.7  * NVL (UA.RESA, 0))), 2) AS PROD_RESA_70, " +
          "       ROUND ((SUM (NVL (UA.SUA_AREA_0, 0) * 0  * NVL (UA.RESA, 0))), 2) AS PROD_RESA_0 " +
          "FROM   UNITA_VITATE UA, " +
          "       DB_STORICO_PARTICELLA SP, " + 
          "       COMUNE COM, " +
          "       PROVINCIA PROV " +
          "WHERE  UA.ID_PARTICELLA = SP.ID_PARTICELLA " + 
          "AND    SP.DATA_FINE_VALIDITA IS NULL " +
          "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "GROUP BY UA.DESCRIZIONE, " +
          "         UA.ID_TIPOLOGIA_VINO, " +
          "         UA.RESA, " +
          "         PROV.ISTAT_PROVINCIA, " +
          "         PROV.DESCRIZIONE " +
          "ORDER BY " +
          "       UA.DESCRIZIONE, " + 
          "       PROV.DESCRIZIONE";
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::riepilogoProvinciaVinoDOP] Query="
                    + query);
      }
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(vRiepiloghi == null)
        {
          vRiepiloghi = new Vector<RiepiloghiUnitaArboreaVO>();
        }
        
        RiepiloghiUnitaArboreaVO riepiloghiUnitaArboreaVO = new RiepiloghiUnitaArboreaVO();
        riepiloghiUnitaArboreaVO.setTipoTipolgiaVino(rs.getString("DESCRIZIONE"));
        riepiloghiUnitaArboreaVO.setIdTipolgiaVino(checkLongNull(rs.getString("ID_TIPOLOGIA_VINO")));
        riepiloghiUnitaArboreaVO.setIstatProv(rs.getString("ISTAT_PROVINCIA"));
        riepiloghiUnitaArboreaVO.setDescProv(rs.getString("DESC_PROV"));
        riepiloghiUnitaArboreaVO.setResa(rs.getBigDecimal("RESA"));
        riepiloghiUnitaArboreaVO.setProdResa100(rs.getBigDecimal("PROD_RESA_100"));
        riepiloghiUnitaArboreaVO.setProdResa70(rs.getBigDecimal("PROD_RESA_70"));
        riepiloghiUnitaArboreaVO.setSupVitata100(rs.getBigDecimal("SUP_VITATA_100"));
        riepiloghiUnitaArboreaVO.setSupVitata70(rs.getBigDecimal("SUP_VITATA_70"));
        riepiloghiUnitaArboreaVO.setSupVitata0(rs.getBigDecimal("SUP_VITATA_0"));
        riepiloghiUnitaArboreaVO.setProdResa0(rs.getBigDecimal("PROD_RESA_0"));
        
        vRiepiloghi.add(riepiloghiUnitaArboreaVO);
      }
      
      return vRiepiloghi;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("vRiepiloghi", vRiepiloghi)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::riepilogoProvinciaVinoDOP] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::riepilogoProvinciaVinoDOP] END.");
    }
  }
  
  
  
  
  public TreeMap<String,Vector<RiepiloghiUnitaArboreaVO>> riepilogoElencoSociProvinciaVinoDOP(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    TreeMap<String,Vector<RiepiloghiUnitaArboreaVO>> tRiepiloghi = null;
    Vector<RiepiloghiUnitaArboreaVO> vRiepiloghi = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::riepilogoElencoSociProvinciaVinoDOP] BEGIN.");      
      
      query = "" +
          "WITH UNITA_VITATE AS " +
          "      (SELECT SUA.ID_STORICO_UNITA_ARBOREA AS ID_STORICO_UNITA_ARBOREA, " + 
          "              SUA.ID_PARTICELLA AS ID_PARTICELLA, " +
          "              SUA.AREA AS AREA, " +
          "              TTV.DESCRIZIONE AS DESCRIZIONE, " + 
          "              TTV.ID_TIPOLOGIA_VINO AS ID_TIPOLOGIA_VINO, " +
          "              TTV.RESA, " +
          "              SUA.ID_AZIENDA, " +
          "              CASE " +
          "                  WHEN EXTRACT (YEAR FROM SUA.DATA_PRIMA_PRODUZIONE) < EXTRACT (YEAR FROM SYSDATE) " +
          "                  AND SUA.FLAG_IMPRODUTTIVA = 'N' " +
          "                  THEN SUA.AREA " +                           
          "                  ELSE 0 " +                           
          "              END AS SUA_AREA_100, " +
          "              CASE " +
          "                  WHEN EXTRACT (YEAR FROM SUA.DATA_PRIMA_PRODUZIONE) = EXTRACT (YEAR FROM SYSDATE) " +
          "                  AND SUA.FLAG_IMPRODUTTIVA = 'N' " + 
          "                  THEN SUA.AREA " +                           
          "                  ELSE 0 " +                           
          "              END AS SUA_AREA_70, " +
          "              CASE " +
          "                  WHEN EXTRACT (YEAR FROM SUA.DATA_PRIMA_PRODUZIONE) > EXTRACT (YEAR FROM SYSDATE) " +
          "                  OR SUA.FLAG_IMPRODUTTIVA = 'S' " + 
          "                  THEN SUA.AREA " +                           
          "                  ELSE 0 " +                           
          "              END AS SUA_AREA_0 " +
          "       FROM   DB_STORICO_UNITA_ARBOREA SUA, " + 
          "              DB_TIPO_TIPOLOGIA_VINO TTV, " +
          "              DB_AZIENDA_COLLEGATA AC, " +
          "              DB_R_CATALOGO_MATRICE RCM "+
          "       WHERE  AC.ID_AZIENDA = ? " +
          "       AND    AC.ID_AZIENDA_ASSOCIATA = SUA.ID_AZIENDA " + 
          "       AND    NVL(AC.DATA_FINE_VALIDITA, TO_DATE('31/12/9999','dd/mm/yyyy')) > SYSDATE" +
          "       AND    SUA.DATA_FINE_VALIDITA IS NULL " +
          "       AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
        //  "       AND    SUA.ID_UTILIZZO = "+SolmrConstants.UVA_DA_VINO+" "+
          "       AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "       AND    RCM.FLAG_TIPO_UVA = '"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_VINO+"' "+
          "       AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " +
          "       AND EXISTS (SELECT 1 " +
          "                   FROM   DB_UTE U, " + 
          "                          DB_CONDUZIONE_PARTICELLA CP " +
          "                   WHERE  SUA.ID_AZIENDA = U.ID_AZIENDA  " +
          "                   AND    U.DATA_FINE_ATTIVITA IS NULL  " +
          "                   AND    U.ID_UTE = CP.ID_UTE " +
          "                   AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "                   AND    CP.DATA_FINE_CONDUZIONE IS NULL) " +
          "      )  " +     
          "SELECT AA.DENOMINAZIONE, " +
          "       AA.CUAA, " +
          "       UA.ID_AZIENDA, " +
          "       UA.DESCRIZIONE, " +
          "       UA.ID_TIPOLOGIA_VINO, " +
          "       UA.RESA, " +
          "       PROV.ISTAT_PROVINCIA, " +
          "       PROV.DESCRIZIONE AS DESC_PROV, " +
          "       SUM(NVL(UA.SUA_AREA_100,0)) AS SUP_VITATA_100, " +
          "       SUM(NVL(UA.SUA_AREA_70,0)) AS SUP_VITATA_70, " +
          "       SUM(NVL(UA.SUA_AREA_0,0)) AS SUP_VITATA_0, " +
          "       ROUND ((SUM (NVL (UA.SUA_AREA_100, 0)  * NVL (UA.RESA, 0))), 2) AS PROD_RESA_100, " +
          "       ROUND ((SUM (NVL (UA.SUA_AREA_70, 0) * 0.7  * NVL (UA.RESA, 0))), 2) AS PROD_RESA_70, " +
          "       ROUND ((SUM (NVL (UA.SUA_AREA_0, 0) * 0  * NVL (UA.RESA, 0))), 2) AS PROD_RESA_0 " +
          "FROM   UNITA_VITATE UA, " +
          "       DB_STORICO_PARTICELLA SP, " + 
          "       COMUNE COM, " +
          "       PROVINCIA PROV, " +
          "       DB_ANAGRAFICA_AZIENDA AA " +
          "WHERE  UA.ID_PARTICELLA = SP.ID_PARTICELLA " + 
          "AND    SP.DATA_FINE_VALIDITA IS NULL " +
          "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "AND    UA.ID_AZIENDA = AA.ID_AZIENDA " +
          "AND    AA.DATA_FINE_VALIDITA IS NULL " +
          "AND    AA.DATA_CESSAZIONE IS NULL " +
          "GROUP BY AA.DENOMINAZIONE, " +
          "         AA.CUAA, " +
          "         UA.ID_AZIENDA, " +
          "         UA.DESCRIZIONE, " +
          "         UA.ID_TIPOLOGIA_VINO, " +
          "         UA.RESA, " +
          "         PROV.ISTAT_PROVINCIA, " +
          "         PROV.DESCRIZIONE " +
          "ORDER BY " +
          "       AA.DENOMINAZIONE, " +
          "       UA.DESCRIZIONE, " + 
          "       PROV.DESCRIZIONE";
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::riepilogoElencoSociProvinciaVinoDOP] Query="
                    + query);
      }
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(tRiepiloghi == null)
        {
          tRiepiloghi = new TreeMap<String,Vector<RiepiloghiUnitaArboreaVO>>();
        }
        
        String denAziendaTmp = rs.getString("DENOMINAZIONE");
        if(tRiepiloghi.get(denAziendaTmp) != null)
        {
          vRiepiloghi = tRiepiloghi.get(denAziendaTmp);
        }
        else
        {       
          vRiepiloghi = new Vector<RiepiloghiUnitaArboreaVO>();
        }
        
        RiepiloghiUnitaArboreaVO riepiloghiUnitaArboreaVO = new RiepiloghiUnitaArboreaVO();
        riepiloghiUnitaArboreaVO.setCuaaAzienda(rs.getString("CUAA"));
        riepiloghiUnitaArboreaVO.setDenominazioneAzienda(denAziendaTmp);
        riepiloghiUnitaArboreaVO.setTipoTipolgiaVino(rs.getString("DESCRIZIONE"));
        riepiloghiUnitaArboreaVO.setIdTipolgiaVino(checkLongNull(rs.getString("ID_TIPOLOGIA_VINO")));
        riepiloghiUnitaArboreaVO.setIstatProv(rs.getString("ISTAT_PROVINCIA"));
        riepiloghiUnitaArboreaVO.setDescProv(rs.getString("DESC_PROV"));
        riepiloghiUnitaArboreaVO.setResa(rs.getBigDecimal("RESA"));
        riepiloghiUnitaArboreaVO.setProdResa100(rs.getBigDecimal("PROD_RESA_100"));
        riepiloghiUnitaArboreaVO.setProdResa70(rs.getBigDecimal("PROD_RESA_70"));
        riepiloghiUnitaArboreaVO.setSupVitata100(rs.getBigDecimal("SUP_VITATA_100"));
        riepiloghiUnitaArboreaVO.setSupVitata70(rs.getBigDecimal("SUP_VITATA_70"));
        riepiloghiUnitaArboreaVO.setSupVitata0(rs.getBigDecimal("SUP_VITATA_0"));
        riepiloghiUnitaArboreaVO.setProdResa0(rs.getBigDecimal("PROD_RESA_0"));
        
        vRiepiloghi.add(riepiloghiUnitaArboreaVO);
        
        tRiepiloghi.put(denAziendaTmp, vRiepiloghi);
      }
      
      return tRiepiloghi;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), 
        new Variabile("vRiepiloghi", vRiepiloghi),
        new Variabile("tRiepiloghi", tRiepiloghi)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::riepilogoElencoSociProvinciaVinoDOP] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::riepilogoElencoSociProvinciaVinoDOP] END.");
    }
  }
  
  
  
  
  
  
  /**
   * Restituisce un vettore di id_conduzione_particella.
   * Sono restituiti solo gli id_conduzioni_particella per quelle conduzioni
   * alla cui particella è associata almeno una uv vallidata
   * 
   * 
   * @param elencoConduzioni
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> esisteUVValidataByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<Long> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::esisteUVValidataByConduzioneAndAzienda] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
          "SELECT CP.ID_CONDUZIONE_PARTICELLA " +
          "FROM   DB_UTE UT, " +
          "       DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_STORICO_UNITA_ARBOREA SUA " +
          "WHERE  UT.ID_AZIENDA = ? " +
          "AND    UT.ID_UTE = CP.ID_UTE " +
          "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
          "AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "AND    CP.ID_TITOLO_POSSESSO <> " +SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO+" "+
          "AND    CP.ID_TITOLO_POSSESSO <> " +SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO+" "+
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL " + 
          "AND    SUA.DATA_FINE_VALIDITA IS NULL  " +
          "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    SUA.ID_AZIENDA = UT.ID_AZIENDA " +
          "AND    SUA.STATO_UNITA_ARBOREA = 'V' " +
          "AND    CP.ID_CONDUZIONE_PARTICELLA IN ( ")
          .append(this.getIdListFromVectorLongForSQL(elencoConduzioni)).append(")");
      
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::esisteUVValidataByConduzioneAndAzienda] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<Long>();
        }
        Long idConduzioneParticella = new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")); 
        if(!result.contains(idConduzioneParticella))
        {
          result.add(idConduzioneParticella);
        }
        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("elencoConduzioni", elencoConduzioni),
          new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::esisteUVValidataByConduzioneAndAzienda] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::esisteUVValidataByConduzioneAndAzienda] END.");
    }
  }
  
  /**
   * 
   * Restituisce un vettore di id_conduzione_particella.
   * Sono restituiti solo gli id_conduzioni_particella per quelle conduzioni
   * alla cui particella è associata almeno una uv 
   * 
   * @param elencoConduzioni
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> esisteUVByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<Long> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::esisteUVByConduzioneAndAzienda] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
          "SELECT CP.ID_CONDUZIONE_PARTICELLA " +
          "FROM   DB_UTE UT, " +
          "       DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_STORICO_UNITA_ARBOREA SUA " +
          "WHERE  UT.ID_AZIENDA = ? " +
          "AND    UT.ID_UTE = CP.ID_UTE " +
          "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
          "AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "AND    CP.ID_TITOLO_POSSESSO <> " +SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO+" "+
          "AND    CP.ID_TITOLO_POSSESSO <> " +SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO+" "+
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL " + 
          "AND    SUA.DATA_FINE_VALIDITA IS NULL  " +
          "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    SUA.ID_AZIENDA = UT.ID_AZIENDA " +
          "AND    CP.ID_CONDUZIONE_PARTICELLA IN ( ")
          .append(this.getIdListFromVectorLongForSQL(elencoConduzioni)).append(")");
      
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::esisteUVByConduzioneAndAzienda] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<Long>();
        }
        Long idConduzioneParticella = new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")); 
        if(!result.contains(idConduzioneParticella))
        {
          result.add(idConduzioneParticella);
        }
        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("elencoConduzioni", elencoConduzioni),
          new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::esisteUVByConduzioneAndAzienda] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::esisteUVByConduzioneAndAzienda] END.");
    }
  }
  
  
  /**
   * 
   * Restituisce gli id_conduzione_particella per le conduzioni che hanno
   * almeno una uv modificata dal procedimenti VITI
   * 
   * 
   * @param elencoConduzioni
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<Long> esisteUVModProcVITIByConduzioneAndAzienda(Vector<Long> elencoConduzioni, Long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<Long> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::esisteUVModProcVITIByConduzioneAndAzienda] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
          "SELECT CP.ID_CONDUZIONE_PARTICELLA " +
          "FROM   DB_UTE UT, " +
          "       DB_CONDUZIONE_PARTICELLA CP, " +
          "       DB_STORICO_UNITA_ARBOREA SUA, " +
          "       DB_TIPO_CAUSALE_MODIFICA TCM " +
          "WHERE  UT.ID_AZIENDA = ? " +
          "AND    UT.ID_UTE = CP.ID_UTE " +
          "AND    UT.DATA_FINE_ATTIVITA IS NULL " +
          "AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
          "AND    CP.ID_TITOLO_POSSESSO <> " +SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO+" "+
          "AND    CP.ID_TITOLO_POSSESSO <> " +SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO+" "+
          "AND    CP.DATA_FINE_CONDUZIONE IS NULL " + 
          "AND    SUA.DATA_FINE_VALIDITA IS NULL  " +
          "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    SUA.ID_AZIENDA = UT.ID_AZIENDA " +
          "AND    SUA.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA " +
          "AND    TCM.ALTRO_PROCEDIMENTO = 'S' " +
          "AND    CP.ID_CONDUZIONE_PARTICELLA IN ( ")
          .append(this.getIdListFromVectorLongForSQL(elencoConduzioni)).append(")");
      
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::esisteUVModProcVITIByConduzioneAndAzienda] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);      
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<Long>();
        }
        Long idConduzioneParticella = new Long(rs.getLong("ID_CONDUZIONE_PARTICELLA")); 
        if(!result.contains(idConduzioneParticella))
        {
          result.add(idConduzioneParticella);
        }
        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("elencoConduzioni", elencoConduzioni),
          new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::esisteUVModProcVITIByConduzioneAndAzienda] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::esisteUVModProcVITIByConduzioneAndAzienda] END.");
    }
  }
  
  
  
  
  
  
  /**
   * 
   * restituisce i record per genrare l'excel semplice delle uv
   * 
   * 
   * @param idAzienda
   * @param filtriUnitaArboreaRicercaVO
   * @return
   * @throws DataAccessException
   */
  public Vector<StoricoParticellaArboreaExcelVO> searchStoricoUnitaArboreaExcelSempliceByParameters( 
      Long idAzienda, FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaArboreaExcelVO> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::searchStoricoUnitaArboreaExcelSempliceByParameters] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "WITH PARTICELLE AS " +
        "  (SELECT DISTINCT PC.ID_PARTICELLA_CERTIFICATA," +
        "                   PC.ID_PARTICELLA " +
        "   FROM   DB_UTE U,  " +
        "          DB_CONDUZIONE_PARTICELLA CP, " +
        "          DB_PARTICELLA_CERTIFICATA PC " +
        "   WHERE  U.ID_AZIENDA = ?  " +
        "   AND    U.DATA_FINE_ATTIVITA IS NULL " + 
        "   AND    U.ID_UTE = CP.ID_UTE " +
        "   AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "   AND    CP.ID_PARTICELLA = PC.ID_PARTICELLA " +
        "   AND    PC.DATA_FINE_VALIDITA IS NULL " +
        "  ), " +
        "    ISTANZA_RIESAME AS " +
        "                (SELECT IR.DATA_RICHIESTA, " +
        "                        IR.DATA_EVASIONE, " +                
        "                        IR.ID_PARTICELLA " +
        "                 FROM   DB_ISTANZA_RIESAME IR " +
        "                        WHERE IR.DATA_RICHIESTA = ( " +
        "                                                    SELECT MAX(IRTMP.DATA_RICHIESTA) " +
        "                                                    FROM   DB_ISTANZA_RIESAME IRTMP " +                                  
        "                                                    WHERE  IRTMP.ID_AZIENDA = IR.ID_AZIENDA " +
        "                                                    AND    IRTMP.ID_PARTICELLA = IR.ID_PARTICELLA " +
        "                                                    AND    IRTMP.DATA_ANNULLAMENTO IS NULL " +
        "                                                   ) " +
        "                 AND    IR.ID_AZIENDA = ? " +
        "                ), " +
        "    COND_ELEG AS " +
        "       (SELECT CE.ID_PARTICELLA, " +
        "               CE.PERCENTUALE_UTILIZZO " +
        "        FROM   DB_CONDUZIONE_ELEGGIBILITA CE " +
        "        WHERE  CE.ID_AZIENDA = ? " + 
        "        AND    CE.DATA_FINE_VALIDITA IS NULL" +
        "        AND    CE.ID_ELEGGIBILITA_FIT = 26 ) " +
        "SELECT SUA.MATRICOLA_CCIAA, " +
        "       SUA.ID_AZIENDA, " +
        "       SP.ID_STORICO_PARTICELLA, " +
        "       SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SP.SUP_CATASTALE, " +
        "       SP.SUPERFICIE_GRAFICA, " +
        "       SUM(CP.PERCENTUALE_POSSESSO) AS PERCENTUALE_POSSESSO, " +
        "       PART.ID_PARTICELLA_CERTIFICATA, " +
        "       NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(PART.ID_PARTICELLA_CERTIFICATA, SUA.ID_CATALOGO_MATRICE),0) AS SUPERFICIE_ELEG, " +
        "       SUA.ID_STORICO_UNITA_ARBOREA, " +
        "       SUA.ID_UNITA_ARBOREA, " +
        "       SUA.ID_CATALOGO_MATRICE, " +
        "       SUA.ID_PARTICELLA, " +
        "       SUA.PROGR_UNAR, " +
        "       SUA.DATA_IMPIANTO, " +
        "       SUA.DATA_PRIMA_PRODUZIONE, " +
        "       SUA.AREA, " +
        "       SUA.ID_TIPOLOGIA_VINO, " +
        "       TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
        "       SUA.ANNO_ISCRIZIONE_ALBO, " +
        "       RCM.ID_VARIETA, " +
        "       TVAR.DESCRIZIONE AS DESC_VARIETA, " +
        "       TVAR.CODICE_VARIETA AS COD_VAR, " +
        "       RCM.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
        "       SUA.SESTO_SU_FILA, " +
        "       SUA.SESTO_TRA_FILE, " +
        "       SUA.NUM_CEPPI, " +
        "       SUA.ID_FORMA_ALLEVAMENTO, " +
        "       TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
        "       SUA.PERCENTUALE_VARIETA, " +
        "       SUA.PRESENZA_ALTRI_VITIGNI, " +        
        "       SUA.DATA_FINE_VALIDITA, " +
        "       SUA.ID_CAUSALE_MODIFICA, " +
        "       TCM.DESCRIZIONE AS DESC_CAUSALE_MODIFICA, " +
        "       SUA.VIGNA, " +
        "       SUA.PERCENTUALE_FALLANZA, " +
        "       SUA.FLAG_IMPRODUTTIVA, " +
        "       ISR.DATA_RICHIESTA, " +
        "       ISR.DATA_EVASIONE, " +
        "       VI.MENZIONE, " +
        "       (CASE " +
        "         WHEN EXISTS (SELECT NE.ID_NOTIFICA_ENTITA " + 
        "               FROM   DB_NOTIFICA_ENTITA NE, " +
        "                      DB_NOTIFICA NO, " +
        "                      DB_TIPO_ENTITA TE " +
        "               WHERE  TE.CODICE_TIPO_ENTITA = 'U' " +
        "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
        "               AND    NE.IDENTIFICATIVO = SUA.ID_UNITA_ARBOREA " +
        "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
        "               AND    NO.ID_AZIENDA = SUA.ID_AZIENDA ) " +
        "         THEN 'NOTIFICA' " +
        "         END " +
        "        ) AS IN_NOTIFICA, " +
        "       NVL(CEL.PERCENTUALE_UTILIZZO, SUM(CP.PERCENTUALE_POSSESSO)) AS PERCENTUALE_UTILIZZO, " +
        "       NVL2(SUA.ID_MENZIONE_GEOGRAFICA, TMG.DESCRIZIONE , (SELECT     MG.DESCRIZIONE  " +
        "                                                           FROM   DB_R_MENZIONE_PARTICELLA MP, " +
        "                                                                  DB_TIPO_MENZIONE_GEOGRAFICA MG  " +
        "                                                           WHERE  MP.ID_PARTICELLA = SUA.ID_PARTICELLA " + 
        "                                                           AND    MP.ID_MENZIONE_GEOGRAFICA = MG.ID_MENZIONE_GEOGRAFICA " +
        "                                                           AND    MG.ID_TIPOLOGIA_VINO = SUA.ID_TIPOLOGIA_VINO " +
        "                                                           AND MP.DATA_FINE_VALIDITA IS NULL " +
        "                                                           AND MG.DATA_FINE_VALIDITA IS NULL " +
        "                                                           AND ROWNUM = 1) " +
        "  ) AS DESC_MENZIONE_GEOGRAFICA " +
        "FROM   DB_STORICO_PARTICELLA SP, " +
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_R_CATALOGO_MATRICE RCM, " +
        "       DB_TIPO_FORMA_ALLEVAMENTO TFA, " + 
        "       DB_TIPO_VARIETA TVAR, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_UTE U, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       PARTICELLE PART, " +
        "       DB_TIPO_TIPOLOGIA_VINO TTV, " +
        "       DB_TIPO_CAUSALE_MODIFICA TCM, " +
        "       ISTANZA_RIESAME ISR, " +
        "       DB_TIPO_MENZIONE_GEOGRAFICA TMG, " +
        "       COND_ELEG CEL, " +
        "       DB_VIGNA VI ");
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        queryBuf.append(
        "       ,DB_ESITO_CONTROLLO_UNAR ECU ");
      }
      queryBuf.append(
        "WHERE  SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "AND    SUA.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
        "AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE (+) " +
        "AND    RCM.ID_VARIETA = TVAR.ID_VARIETA(+) " +
        "AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        "AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
        "AND    SUA.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA(+) " +
        "AND    SUA.ID_AZIENDA = ? " +
        "AND    SUA.ID_AZIENDA = U.ID_AZIENDA " +
        "AND    U.ID_UTE = CP.ID_UTE " +
        "AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
        "AND    U.DATA_FINE_ATTIVITA IS NULL " +
        "AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "AND    CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +
        "AND    SP.DATA_FINE_VALIDITA IS NULL " +
        "AND    SUA.ID_PARTICELLA = PART.ID_PARTICELLA(+) " +
        "AND    SP.ID_PARTICELLA = ISR.ID_PARTICELLA(+) " +
        "AND    SUA.ID_MENZIONE_GEOGRAFICA = TMG.ID_MENZIONE_GEOGRAFICA (+) " +
        "AND    SP.ID_PARTICELLA = CEL.ID_PARTICELLA(+) " +
        "AND    SUA.ID_VIGNA = VI.ID_VIGNA(+) ");
      
      if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() < 0) 
      {
        queryBuf.append(
        "AND    SUA.DATA_FINE_VALIDITA IS NULL ");
      }
      
      //se l'utente ha selezionato un tipo di controllo
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        queryBuf.append(
        "AND    SUA.ID_STORICO_UNITA_ARBOREA = ECU.ID_STORICO_UNITA_ARBOREA " +
        "AND    ECU.ID_CONTROLLO = ? ");
      }
      // Se l'utente ha indicato la destinazione produttiva
      if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) 
      {
        queryBuf.append(
        "AND    RCM.ID_UTILIZZO = ? ");
      }
      // Se l'utente ha indicato il vitigno
      if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) 
      {
        queryBuf.append(
        "AND    RCM.ID_VARIETA = ? ");
      }
      // Se l'utente ha selezionato la tipologia del vino
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaVino())&&
        filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()!=new Long(-1)) 
      {
          
        //IdTipologiaVino()==-1 = qualunque tipologia di vino
        //IdTipologiaVino()==0 = senza tipologia di vino          
        if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue()==new Long(0))
        {
          //senza tipologia di vino
          queryBuf.append(
          "AND      SUA.ID_TIPOLOGIA_VINO IS NULL ");        
        }
        else
        {
          queryBuf.append(
          "AND      SUA.ID_TIPOLOGIA_VINO = ? ");
        }
      }
      // Se l'utente ha indicato il genere iscrizione
      if(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione() != null) 
      {
        queryBuf.append(
        "AND    SUA.ID_GENERE_ISCRIZIONE = ? ");
      }
      
      //Notifiche
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica())
          || (Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica())))
      {
        queryBuf.append(" AND  EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
                   "               FROM   DB_NOTIFICA_ENTITA NE, " +
                   "                      DB_NOTIFICA NO, " +
                   "                      DB_TIPO_ENTITA TE " +
                   "               WHERE  TE.CODICE_TIPO_ENTITA = 'U' " +
                   "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
                   "               AND    NE.IDENTIFICATIVO = SUA.ID_UNITA_ARBOREA " +
                   "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
                   "               AND    NO.ID_AZIENDA = SUA.ID_AZIENDA ");
        if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica()))
        {
          queryBuf.append(
              "               AND    NO.ID_TIPOLOGIA_NOTIFICA = ? ");          
        }
        if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica()))
        {
          queryBuf.append(
              "               AND    NO.ID_CATEGORIA_NOTIFICA = ? ");          
        }
        if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFlagNotificheChiuse()))
        {
          queryBuf.append(
              "AND    (NE.DATA_FINE_VALIDITA IS NULL " +
              "        OR NE.DATA_FINE_VALIDITA = (SELECT MAX(NE1.DATA_FINE_VALIDITA) " +
              "                                    FROM   DB_NOTIFICA_ENTITA NE1 " +
              "                                    WHERE  NE1.IDENTIFICATIVO = NE.IDENTIFICATIVO " +
              "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA )) )");
        }
        else
        {
          queryBuf.append(
              " AND    NE.DATA_FINE_VALIDITA IS NULL )");
        }
        
      }
      // Se l'utente ha indicato la causale modifica
      if(filtriUnitaArboreaRicercaVO.getIdCausaleModifica() != null) 
      {
        queryBuf.append(
        "AND    SUA.ID_CAUSALE_MODIFICA = ? ");
      }
      // Se l'utente ha indicato la provincia di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatProvincia())) 
      {
        queryBuf.append(
        "AND    P.ISTAT_PROVINCIA = ? ");
      }
      // Se l'utente ha indicato il comune di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) 
      {
        queryBuf.append(
        "AND    SP.COMUNE = ? ");
      }
      // Se l'utente ha indicato la sezione di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) 
      {
        queryBuf.append(
        "AND    SP.SEZIONE = ? ");
      }
      // Se l'utente ha indicato il foglio di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) 
      {
        queryBuf.append(
        "AND    SP.FOGLIO = ? ");
      }
      // Se l'utente ha indicato la particella di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) 
      {
        queryBuf.append(
        "AND    SP.PARTICELLA = ? ");
      }
      // Se l'utente ha indicato il subalterno di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) 
      {
        queryBuf.append(
        "AND    SP.SUBALTERNO = ? ");
      }
      // Se l'utente ha specificato la tipologia di anomalia bloccante
      boolean isFirst = true;
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) 
      {
        queryBuf.append(
        "AND (SUA.ESITO_CONTROLLO = ? ");
        isFirst = false;
      }
      // Se l'utente ha specificato la tipologia di anomalia warning
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) 
      {
        if(!isFirst) 
        {
          queryBuf.append(
          "OR ");
        }
        else 
        {
          queryBuf.append(
          "AND ( ");
        }
        queryBuf.append(
        "SUA.ESITO_CONTROLLO = ? ");
        isFirst = false;
      }
      // Se l'utente ha specificato la tipologia di anomalia OK
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) 
      {
        if(!isFirst) 
        {
          queryBuf.append(
          "OR ");
        }
        else 
        {
          queryBuf.append(
          "AND ( ");
        }
        queryBuf.append(
        "SUA.ESITO_CONTROLLO = ? ");
        isFirst = false;
      }
      if(!isFirst) 
      {
        queryBuf.append(
        ")");
      }
      queryBuf.append(
        "GROUP BY " +
        "       SUA.MATRICOLA_CCIAA, " +
        "       SUA.ID_AZIENDA, " +
        "       SP.ID_STORICO_PARTICELLA, " +
        "       SP.COMUNE, " +
        "       C.DESCOM, " +
        "       P.SIGLA_PROVINCIA, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SP.SUP_CATASTALE, " +
        "       SP.SUPERFICIE_GRAFICA, " +
        "       PART.ID_PARTICELLA_CERTIFICATA, " +
        "       SUA.ID_STORICO_UNITA_ARBOREA, " +
        "       SUA.ID_UNITA_ARBOREA, " +
        "       SUA.ID_CATALOGO_MATRICE, " +
        "       SUA.ID_PARTICELLA, " +
        "       SUA.PROGR_UNAR, " +
        "       SUA.DATA_IMPIANTO, " +
        "       SUA.DATA_PRIMA_PRODUZIONE, " +
        "       SUA.AREA, " +
        "       SUA.ID_TIPOLOGIA_VINO, " +
        "       TTV.DESCRIZIONE, " +
        "       SUA.ANNO_ISCRIZIONE_ALBO, " +
        "       RCM.ID_VARIETA, " +
        "       TVAR.DESCRIZIONE, " +
        "       TVAR.CODICE_VARIETA, " +
        "       RCM.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE, " +
        "       SUA.SESTO_SU_FILA, " +
        "       SUA.SESTO_TRA_FILE, " +
        "       SUA.NUM_CEPPI, " +
        "       SUA.ID_FORMA_ALLEVAMENTO, " +
        "       TFA.DESCRIZIONE, " +
        "       SUA.PERCENTUALE_VARIETA, " +
        "       SUA.PRESENZA_ALTRI_VITIGNI, " +        
        "       SUA.DATA_FINE_VALIDITA, " +
        "       SUA.ID_CAUSALE_MODIFICA, " +
        "       TCM.DESCRIZIONE, " +
        "       SUA.VIGNA, " +
        "       SUA.PERCENTUALE_FALLANZA, " +
        "       SUA.FLAG_IMPRODUTTIVA, " +
        "       ISR.DATA_RICHIESTA, " +
        "       ISR.DATA_EVASIONE, " +
        "       VI.MENZIONE, " +
        "       CEL.PERCENTUALE_UTILIZZO, " +
        "       SUA.ID_MENZIONE_GEOGRAFICA, " +
        "       TMG.DESCRIZIONE " +
        "ORDER BY  " +
        "       C.DESCOM, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       SUA.PROGR_UNAR ASC, " +
        "       TTV.DESCRIZIONE ASC ");
      
      
      
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::searchStoricoUnitaArboreaExcelSempliceByParameters] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int indice = 0;
      //particelle
      stmt.setLong(++indice, idAzienda.longValue());
      //istanza di riesame
      stmt.setLong(++indice, idAzienda.longValue());
      
      //conduzione eleggibile
      stmt.setLong(++indice, idAzienda.longValue());
      
      
      stmt.setLong(++indice, idAzienda.longValue());
      
      //se l'utente ha selezionato il tipo di controllo
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdControllo().longValue());
      }
      // Se l'utente ha indicato la destinazione produttiva
      if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null) 
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdUtilizzo().longValue());
      }
      // Se l'utente ha indicato il vitigno
      if(filtriUnitaArboreaRicercaVO.getIdVarieta() != null) 
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdVarieta().longValue());
      }
      // Se l'utente ha indicato il tipo vino
      if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino() != null &&
         filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(-1) &&
         filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue() != new Long(0)) 
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaVino().longValue());
      }
      // Se l'utente ha indicato il genere iscrizione
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione())) 
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdGenereIscrizione().longValue());
      }
      // Se l'utente ha indicato la tipologia notifica
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica().longValue());
      }
      // Se l'utente ha indicato la categoria notifica
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica())) {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica().longValue());
      }
      // Se l'utente ha indicato la causale modifica
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCausaleModifica())) 
      {
        stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdCausaleModifica().longValue());
      }
      // Se l'utente ha indicato la provincia di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatProvincia())) {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatProvincia());
      }
      // Se l'utente ha indicato il comune di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) 
      {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getIstatComune());
      }
      // Se l'utente ha indicato la sezione di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) 
      {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSezione().toUpperCase());
      }
      // Se l'utente ha indicato il foglio di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) 
      {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getFoglio());
      }
      // Se l'utente ha indicato la particella di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella()))
      {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getParticella());
      }
      // Se l'utente ha indicato il subalterno di riferimento
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) 
      {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getSubalterno());
      }
      // SEGNALAZIONI:
      // Se l'utente ha specificato la tipologia di anomalia bloccante
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) 
      {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante());
      }
      // Se l'utente ha specificato la tipologia di anomalia warning
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) 
      {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning());
      }
      // Se l'utente ha specificato la tipologia di anomalia OK
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) 
      {
        stmt.setString(++indice, filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk());
      }      
    
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<StoricoParticellaArboreaExcelVO>();
        }
        StoricoParticellaArboreaExcelVO storicoParticellaArboreaExcelVO = new StoricoParticellaArboreaExcelVO();
        storicoParticellaArboreaExcelVO.setMatricolaCCIAA(rs.getString("MATRICOLA_CCIAA"));
        storicoParticellaArboreaExcelVO.setDescrizioneComuneParticella(rs.getString("DESCOM")+" ("+rs.getString("SIGLA_PROVINCIA")+")");
        storicoParticellaArboreaExcelVO.setSezione(rs.getString("SEZIONE"));
        storicoParticellaArboreaExcelVO.setFoglio(rs.getString("FOGLIO"));
        storicoParticellaArboreaExcelVO.setParticella(rs.getString("PARTICELLA"));
        storicoParticellaArboreaExcelVO.setSubalterno(rs.getString("SUBALTERNO"));
        
        
        String superficieCatastale = rs.getString("SUP_CATASTALE");
        if(Validator.isNotEmpty(superficieCatastale))
        {
          storicoParticellaArboreaExcelVO.setSuperficieCatastale(
              StringUtils.parseSuperficieField(superficieCatastale));
        }
        else
        {
          storicoParticellaArboreaExcelVO.setSuperficieCatastale(SolmrConstants.DEFAULT_SUPERFICIE);
        }        
        
        String superficieGrafica = rs.getString("SUPERFICIE_GRAFICA");
        if(Validator.isNotEmpty(superficieGrafica))
        {
          storicoParticellaArboreaExcelVO.setSuperficieGrafica(
              StringUtils.parseSuperficieField(superficieGrafica));
        }
        else
        {
          storicoParticellaArboreaExcelVO.setSuperficieGrafica(SolmrConstants.DEFAULT_SUPERFICIE);
        }
        
        storicoParticellaArboreaExcelVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        
        String supEleggibile = rs.getString("SUPERFICIE_ELEG");
        if(Validator.isNotEmpty(supEleggibile))
        {
          storicoParticellaArboreaExcelVO.setSupEleggibile(
              StringUtils.parseSuperficieField(supEleggibile));         
        }
        else
        {
          storicoParticellaArboreaExcelVO.setSupEleggibile(SolmrConstants.DEFAULT_SUPERFICIE);
        }
        storicoParticellaArboreaExcelVO.setProgressivo(rs.getString("PROGR_UNAR"));
        storicoParticellaArboreaExcelVO.setDataImpianto(rs.getTimestamp("DATA_IMPIANTO"));
        storicoParticellaArboreaExcelVO.setDataPrimaProduzione(rs.getTimestamp("DATA_PRIMA_PRODUZIONE"));
        
        String area = rs.getString("AREA");
        if(Validator.isNotEmpty(area)) 
        {
          storicoParticellaArboreaExcelVO.setArea(
            StringUtils.parseSuperficieField(area));    
        }
        else
        {
          storicoParticellaArboreaExcelVO.setArea(SolmrConstants.DEFAULT_SUPERFICIE);
        }
        
        Long idTipologiaVino = checkLongNull(rs.getString("ID_TIPOLOGIA_VINO"));
        storicoParticellaArboreaExcelVO.setIdTipologiaVino(idTipologiaVino);        
        if(Validator.isNotEmpty(idTipologiaVino)) 
        {
          storicoParticellaArboreaExcelVO.setDescTipologiaVino(rs.getString("DESC_TIPO_VINO"));
        }       
        storicoParticellaArboreaExcelVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) 
        {
          storicoParticellaArboreaExcelVO.setVarieta("["+rs.getString("COD_VAR")+"] "+rs.getString("DESC_VARIETA"));
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) 
        {
          String destinazioneProduttiva = "";
          if(Validator.isNotEmpty(rs.getString("CODICE"))) {
            destinazioneProduttiva += "["+rs.getString("CODICE")+"] ";
          }
          destinazioneProduttiva += rs.getString("DESC_TIPO_UTILIZZO");
          storicoParticellaArboreaExcelVO.setDestinazioneProduttiva(destinazioneProduttiva);
        }
        storicoParticellaArboreaExcelVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
        storicoParticellaArboreaExcelVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
        storicoParticellaArboreaExcelVO.setNumeroCeppi(rs.getString("NUM_CEPPI"));
        if(Validator.isNotEmpty(rs.getString("ID_FORMA_ALLEVAMENTO"))) 
        {
          storicoParticellaArboreaExcelVO.setDescFormaAllevamento(rs.getString("DESC_FORMA_ALLEVAMENTO"));
        }
        storicoParticellaArboreaExcelVO.setPercentualeVarieta(rs.getString("PERCENTUALE_VARIETA"));
        if(SolmrConstants.FLAG_S.equalsIgnoreCase(rs.getString("PRESENZA_ALTRI_VITIGNI"))) 
        {
          storicoParticellaArboreaExcelVO.setAltriVitigni(SolmrConstants.FLAG_SI);
        }
        else 
        {
          storicoParticellaArboreaExcelVO.setAltriVitigni(SolmrConstants.FLAG_NO);
        }        
        storicoParticellaArboreaExcelVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
        storicoParticellaArboreaExcelVO.setIdCausaleModifica(checkLongNull(rs.getString("ID_CAUSALE_MODIFICA")));
        storicoParticellaArboreaExcelVO.setDescCausaleModifica(rs.getString("DESC_CAUSALE_MODIFICA"));
        storicoParticellaArboreaExcelVO.setVigna(rs.getString("VIGNA"));
        storicoParticellaArboreaExcelVO.setPercentualeFallanza(rs.getBigDecimal("PERCENTUALE_FALLANZA"));
        if(SolmrConstants.FLAG_S.equalsIgnoreCase(rs.getString("FLAG_IMPRODUTTIVA"))) 
        {
          storicoParticellaArboreaExcelVO.setFlagImproduttiva(SolmrConstants.FLAG_SI);
        }
        else 
        {
          storicoParticellaArboreaExcelVO.setFlagImproduttiva(SolmrConstants.FLAG_NO);
        }     
        
        storicoParticellaArboreaExcelVO.setDataRichiesta(rs.getTimestamp("DATA_RICHIESTA"));
        storicoParticellaArboreaExcelVO.setDataEvasione(rs.getTimestamp("DATA_EVASIONE"));
        storicoParticellaArboreaExcelVO.setVignaElencoReg(rs.getString("MENZIONE"));
        
        storicoParticellaArboreaExcelVO.setInNotifica(rs.getString("IN_NOTIFICA"));
        
        storicoParticellaArboreaExcelVO.setPercentualeUsoElegg(rs.getBigDecimal("PERCENTUALE_UTILIZZO"));
        storicoParticellaArboreaExcelVO.setDescMenzioneGeografica(rs.getString("DESC_MENZIONE_GEOGRAFICA"));
        
      
        
        result.add(storicoParticellaArboreaExcelVO);
        
      }
      
      
      /*if((result != null)  && (result.size() > 0))
      {
        Vector<Long> vIdTipologiaVino = getIdTipologiaVinoByResult(result);
        if(Validator.isNotEmpty(vIdTipologiaVino))
        {
          HashMap<Long,Vector<VignaVO>> hVigna = getHashVignaAttiveByIdTipologiaVino(vIdTipologiaVino, conn);
          if(hVigna != null)
          {
            aggiungiRisultatiSecondaQuery(result, hVigna);
          }
        }
        
      */
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("filtriUnitaArboreaRicercaVO", filtriUnitaArboreaRicercaVO),
          new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::searchStoricoUnitaArboreaExcelSempliceByParameters] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::searchStoricoUnitaArboreaExcelSempliceByParameters] END.");
    }
  }
  
  
  /**
   * 
   * restituisce i dati di db_vigna relativamente
   * all'id_tipologia_vino
   * 
   * 
   * @param vIdTipologiaVino
   * @param conn
   * @return
   * @throws DataAccessException
   */
  public HashMap<Long,Vector<VignaVO>> getHashVignaAttiveByIdTipologiaVino(Vector<Long> vIdTipologiaVino,
      Connection conn) 
    throws DataAccessException
  {
    String query = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    StringBuffer queryBuf = null;
    HashMap<Long,Vector<VignaVO>> result = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[TipoTipologiaVinoDAO::getHashVignaAttiveByIdTipologiaVino] BEGIN.");
  
      
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   "
              + "SELECT DV.ID_VIGNA, "
              + "       DV.ID_TIPOLOGIA_VINO, " 
              + "       DV.MENZIONE, "
              + "       DV.NOTE, "
              + "       DV.DATA_INIZIO_VALIDITA, "
              + "       DV.DATA_FINE_VALIDITA "
              + "FROM   "
              + "       DB_VIGNA DV " 
              + "WHERE  "
              + "       DV.ID_TIPOLOGIA_VINO IN (").append(
                  this.getIdListFromVectorLongForSQL(vIdTipologiaVino)).append(")")
           .append(
                "AND    DV.DATA_FINE_VALIDITA IS NULL " 
              + "ORDER BY "
              + "       DV.ID_TIPOLOGIA_VINO, DV.MENZIONE ");   
      
      
      
      query = queryBuf.toString();
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoTipologiaVinoDAO::getHashVignaAttiveByIdTipologiaVino] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query      
      rs = stmt.executeQuery();
      
      while(rs.next())
      {
        if(result == null)
        {
          result = new HashMap<Long,Vector<VignaVO>>();
        }
        
        Long idTipologiaVino = new Long(rs.getLong("ID_TIPOLOGIA_VINO"));
        if(result.get(idTipologiaVino) != null)
        {
          Vector<VignaVO> vVignaVO = result.get(idTipologiaVino); 
          VignaVO vignaVO  = new VignaVO();
          vignaVO.setIdVigna(new Long(rs.getLong("ID_VIGNA")));
          vignaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          vignaVO.setMenzione(rs.getString("MENZIONE"));
          vignaVO.setNote(rs.getString("NOTE"));
          vignaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
          vignaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
          vVignaVO.add(vignaVO);
          result.put(idTipologiaVino, vVignaVO);
        }
        else
        {
          Vector<VignaVO> vVignaVO = new Vector<VignaVO>(); 
          VignaVO vignaVO  = new VignaVO();
          vignaVO.setIdVigna(new Long(rs.getLong("ID_VIGNA")));
          vignaVO.setIdTipologiaVino(new Long(rs.getLong("ID_TIPOLOGIA_VINO")));
          vignaVO.setMenzione(rs.getString("MENZIONE"));
          vignaVO.setNote(rs.getString("NOTE"));
          vignaVO.setDataInizioValidita(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
          vignaVO.setDataFineValidita(rs.getTimestamp("DATA_FINE_VALIDITA"));
          vVignaVO.add(vignaVO);
          result.put(idTipologiaVino, vVignaVO);
        }
      }
      
      return result;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("result", result) };
      
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdTipologiaVino", vIdTipologiaVino) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoTipologiaVinoDAO::getHashVignaAttiveByIdTipologiaVino] ",
              t, query, variabili, parametri);
      
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      close(rs, stmt, null);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoTipologiaVinoDAO::getHashVignaAttiveByIdTipologiaVino] END.");
    }
  }
  
  
  /**
   * 
   * restituisce tutte le uv (presa l'ultima per data/id_unita_arborea
   * relativi agli di_particella meno quelle già associate all'azienda
   * 
   * 
   * 
   * @param vIdParticella
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<StoricoParticellaVO> getListUVForInserimento(Long idParticellaCurr, Vector<Long> vIdParticella, Long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<StoricoParticellaVO> result = null;
    StringBuffer queryBuf = null;
    
    Vector<Long> vIdParticellaTot = vIdParticella;
    if(Validator.isNotEmpty(idParticellaCurr))
    {
      if(vIdParticellaTot == null)
      {
        vIdParticellaTot = new Vector<Long>(); 
      }
      vIdParticellaTot.add(idParticellaCurr);
    }
    
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getListUVForInserimento] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      if(Validator.isNotEmpty(vIdParticellaTot))
      {
        queryBuf.append(
            "SELECT SUA.ID_STORICO_UNITA_ARBOREA, " +
            "       SUA.DATA_CESSAZIONE, " +
            "       COM.DESCOM, " +
            "       PROV.SIGLA_PROVINCIA, " +
            "       SP.SEZIONE, " +
            "       SP.FOGLIO, " +
            "       SP.PARTICELLA, " +
            "       SP.SUBALTERNO, " +
            "       SUA.PROGR_UNAR, " +
            "       SUA.AREA, " +
            "       SUA.ID_VARIETA, " +
            "       TV.CODICE_VARIETA, " +
            "       TV.DESCRIZIONE AS DESC_VARIETA, " +
            "       SUA.SESTO_SU_FILA, " +
            "       SUA.SESTO_TRA_FILE, " +
            "       AA.DENOMINAZIONE, " +
            "       AA.CUAA, " +
            "       UA.DATA_CONSOLIDAMENTO_GIS " +
            "FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
            "       DB_R_CATALOGO_MATRICE RCM, " +
            "       DB_STORICO_PARTICELLA SP, " +
            "       COMUNE COM, " +
            "       PROVINCIA PROV, " +
            "       DB_TIPO_VARIETA TV, " +
            "       DB_ANAGRAFICA_AZIENDA AA, " +
            "       DB_UNITA_ARBOREA UA " +
            "WHERE  SUA.ID_PARTICELLA IN( ").append(this.getIdListFromVectorLongForSQL(vIdParticellaTot)).append(") ")
         .append(
            "AND    SUA.ID_TIPOLOGIA_UNAR = '2' " +
            "AND    NVL(SUA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy')) " +
            "       = (SELECT MAX(NVL(SUA1.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy'))) " +
            "           FROM   DB_STORICO_UNITA_ARBOREA SUA1 " +
            "           WHERE  SUA1.ID_PARTICELLA = SUA.ID_PARTICELLA) " +
            "AND    SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
            "AND    NVL(SUA.ID_AZIENDA,0) <> ? " +
            "AND    SP.DATA_FINE_VALIDITA IS NULL " +
            "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
            "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
            "AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
            "AND    RCM.ID_VARIETA = TV.ID_VARIETA " +
            "AND    SUA.ID_AZIENDA = AA.ID_AZIENDA(+) " +
            "AND    AA.DATA_FINE_VALIDITA IS NULL " +
            "AND    AA.DATA_CESSAZIONE IS NULL " +
            "AND    SUA.ID_UNITA_ARBOREA = UA.ID_UNITA_ARBOREA ");
      }

      if(Validator.isNotEmpty(idParticellaCurr))
      {
        // Se arriviamo qui, significa che la idParticellaTot non è vuoto, dunque queryBuf contiene già la prima query
        // e quindi possiamo lasciare la clausola UNION senza far troppi distinguo
        queryBuf.append(
          "UNION " +
          "SELECT SUA.ID_STORICO_UNITA_ARBOREA, " +
          "       SUA.DATA_CESSAZIONE, " +
          "       COM.DESCOM, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       SP.SEZIONE, " +
          "       SP.FOGLIO, " +
          "       SP.PARTICELLA, " +
          "       SP.SUBALTERNO, " +
          "       SUA.PROGR_UNAR, " +
          "       SUA.AREA, " +
          "       SUA.ID_VARIETA, " +
          "       TV.CODICE_VARIETA, " +
          "       TV.DESCRIZIONE AS DESC_VARIETA, " +
          "       SUA.SESTO_SU_FILA, " +
          "       SUA.SESTO_TRA_FILE, " +
          "       AA.DENOMINAZIONE, " +
          "       AA.CUAA, " +
          "       UA.DATA_CONSOLIDAMENTO_GIS " +
          "FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
          "       DB_R_CATALOGO_MATRICE RCM, " +
          "       DB_STORICO_PARTICELLA SP, " +
          "       COMUNE COM, " +
          "       PROVINCIA PROV, " +
          "       DB_TIPO_VARIETA TV, " +
          "       DB_ANAGRAFICA_AZIENDA AA, " +
          "       DB_UNITA_ARBOREA UA " +
          "WHERE  SUA.ID_PARTICELLA = ? " +
          "AND    SUA.ID_TIPOLOGIA_UNAR = '2' " +
          "AND    NVL(SUA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy'))  " +
          "       = (SELECT MAX(NVL(SUA1.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy'))) " + 
          "           FROM   DB_STORICO_UNITA_ARBOREA SUA1 " +
          "           WHERE  SUA1.ID_PARTICELLA = SUA.ID_PARTICELLA) " +
          "AND    SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
          "AND    SUA.ID_AZIENDA = ? " +
          "AND    SUA.DATA_CESSAZIONE IS NOT NULL " +
          "AND    SP.DATA_FINE_VALIDITA IS NULL " +
          "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "AND    RCM.ID_VARIETA = TV.ID_VARIETA " +
          "AND    SUA.ID_AZIENDA = AA.ID_AZIENDA(+) " +
          "AND    AA.DATA_FINE_VALIDITA IS NULL " +
          "AND    AA.DATA_CESSAZIONE IS NULL " +
          "AND    SUA.ID_UNITA_ARBOREA = UA.ID_UNITA_ARBOREA ");
      }
      
      if(Validator.isNotEmpty(vIdParticella))
      {
        // Se arriviamo qui, significa che la idParticellaTot non è vuoto, dunque queryBuf contiene già almeno la prima query
        // e quindi possiamo lasciare la clausola UNION senza far troppi distinguo
        queryBuf.append(
          "UNION " +
          "SELECT SUA.ID_STORICO_UNITA_ARBOREA, " +
          "       SUA.DATA_CESSAZIONE, " +
          "       COM.DESCOM, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       SP.SEZIONE, " +
          "       SP.FOGLIO, " +
          "       SP.PARTICELLA, " +
          "       SP.SUBALTERNO, " +
          "       SUA.PROGR_UNAR, " +
          "       SUA.AREA, " +
          "       SUA.ID_VARIETA, " +
          "       TV.CODICE_VARIETA, " +
          "       TV.DESCRIZIONE AS DESC_VARIETA, " +
          "       SUA.SESTO_SU_FILA, " +
          "       SUA.SESTO_TRA_FILE, " +
          "       AA.DENOMINAZIONE, " +
          "       AA.CUAA, " +
          "       UA.DATA_CONSOLIDAMENTO_GIS " +
          "FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
          "       DB_R_CATALOGO_MATRICE RCM, " +
          "       DB_STORICO_PARTICELLA SP, " +
          "       COMUNE COM, " +
          "       PROVINCIA PROV, " +
          "       DB_TIPO_VARIETA TV, " +
          "       DB_ANAGRAFICA_AZIENDA AA, " +
          "       DB_UNITA_ARBOREA UA " +
          "WHERE  SUA.ID_PARTICELLA IN( ").append(this.getIdListFromVectorLongForSQL(vIdParticella)).append(") ")
        .append(
          "AND    SUA.ID_TIPOLOGIA_UNAR = '2' " +
          /*"AND    NVL(SUA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy')) " +
          "       = (SELECT MAX(NVL(SUA1.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy'))) " + 
          "           FROM   DB_STORICO_UNITA_ARBOREA SUA1, " +
          "                  DB_EVENTO_PARTICELLA EV1 " +
          "           WHERE  SUA1.ID_PARTICELLA = SUA.ID_PARTICELLA " +
          "                   OR (SUA1.ID_PARTICELLA = EV1.ID_PARTICELLA_NUOVA " +
          "                   AND EV1.ID_PARTICELLA_CESSATA = SUA.ID_PARTICELLA)) " +*/
          "AND    NVL(SUA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy'))  " +
          "       = (SELECT MAX(NVL(SUA1.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy'))) " + 
          "           FROM   DB_STORICO_UNITA_ARBOREA SUA1 " +
          "           WHERE  SUA1.ID_UNITA_ARBOREA = SUA.ID_UNITA_ARBOREA " +
          "           AND NVL(SUA1.ID_UNITA_ARBOREA_MADRE,-1) <> SUA1.ID_UNITA_ARBOREA) " +
          "AND    SUA.AREA > NVL((SELECT SUM(SUA1.AREA) " +
          "                   FROM   DB_STORICO_UNITA_ARBOREA SUA1 " +
          "                   WHERE  SUA1.DATA_FINE_VALIDITA IS NULL " +
          "                   AND    SUA1.ID_UNITA_ARBOREA_MADRE = SUA.ID_UNITA_ARBOREA),0) " +
          "AND    SUA.ID_PARTICELLA = SP.ID_PARTICELLA " +
          "AND    SUA.ID_AZIENDA = ? " +
          "AND    SP.DATA_FINE_VALIDITA IS NULL " +
          "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "AND    SUA.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "AND    RCM.ID_VARIETA = TV.ID_VARIETA " +
          "AND    SUA.ID_AZIENDA = AA.ID_AZIENDA(+) " +
          "AND    AA.DATA_FINE_VALIDITA IS NULL " +
          "AND    AA.DATA_CESSAZIONE IS NULL " +
          "AND    SUA.ID_UNITA_ARBOREA = UA.ID_UNITA_ARBOREA ");
      }
      
      query = queryBuf.toString();
      if (!"".equals(query))
      {
        conn = getDatasource().getConnection();
        if (SolmrLogger.isDebugEnabled(this))
        {
          // Dato che la query costruita dinamicamente è un dato importante la
          // registro sul file di log se il
          // debug è abilitato

          SolmrLogger
              .debug(
                  this,
                  "[StoricoUnitaArboreaGaaDAO::getListUVForInserimento] Query="
                      + query);
        }

        stmt = conn.prepareStatement(query);

        // Setto i parametri della query
        int idx = 0;
        stmt.setLong(++idx, idAzienda);
        if(Validator.isNotEmpty(idParticellaCurr))
        {
          stmt.setLong(++idx, idParticellaCurr);
          stmt.setLong(++idx, idAzienda);
        }

        if(Validator.isNotEmpty(vIdParticella))
        {
          stmt.setLong(++idx, idAzienda);
        }

        ResultSet rs = stmt.executeQuery();
        while (rs.next())
        {
          if(result == null)
          {
            result = new Vector<StoricoParticellaVO>();
          }
          StoricoParticellaVO storicoParticellaVO = new StoricoParticellaVO();
          StoricoUnitaArboreaVO storicoUnitaArboreaVO = new StoricoUnitaArboreaVO();
          storicoUnitaArboreaVO.setIdStoricoUnitaArborea(checkLongNull(rs.getString("ID_STORICO_UNITA_ARBOREA")));
          storicoUnitaArboreaVO.setDataCessazione(rs.getTimestamp("DATA_CESSAZIONE"));
          ComuneVO comuneParticellaVO = new ComuneVO();
          comuneParticellaVO.setDescom(rs.getString("DESCOM"));
          comuneParticellaVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
          storicoParticellaVO.setComuneParticellaVO(comuneParticellaVO);
          storicoParticellaVO.setSezione(rs.getString("SEZIONE"));
          storicoParticellaVO.setFoglio(rs.getString("FOGLIO"));
          storicoParticellaVO.setParticella(rs.getString("PARTICELLA"));
          storicoParticellaVO.setSubalterno(rs.getString("SUBALTERNO"));
          storicoUnitaArboreaVO.setProgrUnar(rs.getString("PROGR_UNAR"));
          storicoUnitaArboreaVO.setArea(rs.getString("AREA"));
          if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
            storicoUnitaArboreaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
            TipoVarietaVO tipoVarietaVO = new TipoVarietaVO();
            tipoVarietaVO.setIdVarieta(new Long(rs.getLong("ID_VARIETA")));
            tipoVarietaVO.setDescrizione(rs.getString("DESC_VARIETA"));
            tipoVarietaVO.setCodiceVarieta(rs.getString("CODICE_VARIETA"));
            storicoUnitaArboreaVO.setTipoVarietaVO(tipoVarietaVO);
          }
          storicoUnitaArboreaVO.setSestoSuFila(rs.getString("SESTO_SU_FILA"));
          storicoUnitaArboreaVO.setSestoTraFile(rs.getString("SESTO_TRA_FILE"));
          storicoUnitaArboreaVO.setDenominazione(rs.getString("DENOMINAZIONE"));
          storicoUnitaArboreaVO.setCuaa(rs.getString("CUAA"));
          storicoUnitaArboreaVO.setDataConsolidamentoGis(rs.getTimestamp("DATA_CONSOLIDAMENTO_GIS"));
          storicoParticellaVO.setStoricoUnitaArboreaVO(storicoUnitaArboreaVO);

          result.add(storicoParticellaVO);


        }
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticellaCurr", idParticellaCurr), 
          new Parametro("vIdParticella", vIdParticella),
          new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getListUVForInserimento] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getListUVForInserimento] END.");
    }
  }
  
  
  /*private Vector<Long> getIdTipologiaVinoByResult(Vector<StoricoParticellaArboreaExcelVO> elencoUV)
  {
    Vector<Long> result = null;
    for (int i=0;i<elencoUV.size();i++)
    {
      Long idTipologiaVino = elencoUV.get(i).getIdTipologiaVino();
      if(Validator.isNotEmpty(idTipologiaVino))
      {
        if(result == null)
        {
          result = new Vector<Long>();
        }
        
        if(!result.contains(idTipologiaVino))
        {
          result.add(idTipologiaVino);
        }
      }
    }    
    return result;
  }*/
  
  
  /*private void aggiungiRisultatiSecondaQuery(Vector<StoricoParticellaArboreaExcelVO> elencoUV,
      HashMap<Long,Vector<VignaVO>> hVigna)
  {
    for (int i=0;i<elencoUV.size();i++)
    {
      Long idTipologiaVino = elencoUV.get(i).getIdTipologiaVino();
      if(Validator.isNotEmpty(idTipologiaVino))
      {
        Vector<VignaVO> vVignaVO = hVigna.get(idTipologiaVino);
        elencoUV.get(i).setvVignaVO(vVignaVO);
      }
    }    
  }*/
  
  
  /**
   * 
   * Ritorna la superficie eleggibile della particella attraverso la varietà
   * 
   * 
   * 
   * 
   * @param idParticellaCertificata
   * @param idVarieta
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSupEleggibilePlSql(long idParticellaCertificata, long idCatalogoMatrice) 
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    BigDecimal supEleggibile = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getSupEleggibilePlSql] BEGIN.");
      /***
       *  FUNCTION SelTotSupElegByPartEVetCat (pIdPartCertif  IN DB_PARTICELLA_CERT_ELEG.ID_PARTICELLA_CERTIFICATA%TYPE,
       *                                 pStrVetVarieta IN VARCHAR2
       *                                 ) RETURN  DB_PARTICELLA_CERT_ELEG.SUPERFICIE%TYPE IS
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{ ? = call PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::getSupEleggibilePlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.registerOutParameter(1,Types.DECIMAL);
      cs.setLong(2, idParticellaCertificata);
      cs.setLong(3, idCatalogoMatrice);
      
      cs.executeUpdate();
      
      supEleggibile = cs.getBigDecimal(1);
      if(supEleggibile == null)
      {
        supEleggibile = new BigDecimal(0);
      }
      
      
      
      
      return supEleggibile;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("supEleggibile", supEleggibile) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idParticellaCertificata", idParticellaCertificata),
        new Parametro("idCatalogoMatrice", idCatalogoMatrice)
      };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StoricoUnitaArboreaGaaDAO::getSupEleggibilePlSql] ",
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
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getSupEleggibilePlSql] END.");
    }
  }
  
  
  
  public PlSqlCodeDescription compensazioneAziendalePlSql(long idAzienda, 
      long idUtenteAggiornamento) 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    CallableStatement cs = null;
    StringBuffer queryBuf = null;
    PlSqlCodeDescription plqObj = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::compensazioneAziendalePlSql] BEGIN.");
      /***
       *  PROCEDURE COMPENSAZIONE_AZIENDALE( pIdAzienda             IN     DB_AZIENDA.ID_AZIENDA%TYPE
                                     , pIdUtenteAggiornamento IN     DB_COMPENSAZIONE_AZIENDA.ID_UTENTE_AGGIORNAMENTO%TYPE
                                     , P_ESITO                IN OUT VARCHAR2
                                     , P_MSGERR               IN OUT VARCHAR2
                                     , P_CODERR               IN OUT VARCHAR2
          );
       */
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("{call PCK_SMRGAA_COMPENSAZIONE_UV.COMPENSAZIONE_AZIENDALE(?,?,?,?,?)}");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::compensazioneAziendalePlSql] Query="
                + query);
      }
      
      
      conn = getDatasource().getConnection();
      cs = conn.prepareCall(query);
      cs.setLong(1, idAzienda);
      cs.setLong(2, idUtenteAggiornamento);
      cs.registerOutParameter(3,Types.VARCHAR); //esito
      cs.registerOutParameter(4,Types.VARCHAR); //msg errore
      cs.registerOutParameter(5,Types.VARCHAR); //codice errore
      
      cs.executeUpdate();
      
      plqObj = new PlSqlCodeDescription();
      plqObj.setItem(cs.getString(3));
      plqObj.setOtherdescription(cs.getString(4));
      plqObj.setDescription(cs.getString(5));
      
      return plqObj;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("plqObj", plqObj) };
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda),
        new Parametro("idUtenteAggiornamento", idUtenteAggiornamento) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StoricoUnitaArboreaGaaDAO::compensazioneAziendalePlSql] ",
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
      closePlSql(cs, conn);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::compensazioneAziendalePlSql] END.");
    }
  }
  
  
  
  /**
   * 
   * Estrarre le uv che sono state generate dal pl per 
   * la compensazione
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<UVCompensazioneVO> getUVPerCompensazione(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<UVCompensazioneVO> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getUVPerCompensazione] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "SELECT NVL2(CUV.ID_STORICO_UNITA_ARBOREA, COM1.DESCOM, COM2.DESCOM) AS DESCOM, " +
        "       NVL2(CUV.ID_STORICO_UNITA_ARBOREA, PROV1.SIGLA_PROVINCIA, PROV2.SIGLA_PROVINCIA) AS SIGLA_PROVINCIA, " +
        "       NVL2(CUV.ID_STORICO_UNITA_ARBOREA, SP1.SEZIONE, SP2.SEZIONE) AS SEZIONE, " +
        "       NVL2(CUV.ID_STORICO_UNITA_ARBOREA, SP1.FOGLIO, SP2.FOGLIO) AS FOGLIO, " +
        "       NVL2(CUV.ID_STORICO_UNITA_ARBOREA, SP1.PARTICELLA, SP2.PARTICELLA) AS PARTICELLA, " +
        "       NVL2(CUV.ID_STORICO_UNITA_ARBOREA, SP1.SUBALTERNO, SP2.SUBALTERNO) AS SUBALTERNO, " +
        "       NVL2(CUV.ID_STORICO_UNITA_ARBOREA, SUA.PROGR_UNAR, UAD.PROGR_UNAR) AS PROGR_UNAR, " +
        "       NVL2(CUV.ID_STORICO_UNITA_ARBOREA, SUA.DATA_IMPIANTO, UAD.DATA_IMPIANTO) AS DATA_IMPIANTO, " +
        "       NVL2(CUV.ID_STORICO_UNITA_ARBOREA, SUA.DATA_PRIMA_PRODUZIONE, UAD.DATA_PRIMA_PRODUZIONE) AS DATA_PRIMA_PRODUZIONE, " +
        "       NVL2(CUV.ID_STORICO_UNITA_ARBOREA, TU1.DESCRIZIONE, TU2.DESCRIZIONE) AS DEST_PRODUTTIVA, " +
        "       NVL2(CUV.ID_STORICO_UNITA_ARBOREA, TV1.DESCRIZIONE, TV2.DESCRIZIONE) AS VITIGNO, " +
        "       NVL2(CUV.ID_STORICO_UNITA_ARBOREA, TTV1.DESCRIZIONE, TTV2.DESCRIZIONE) AS IDONEITA, " +
        "       NVL(CUV.AREA_DICHIARATA,0) AS AREA_DICHIARATA, " +
        "       NVL(CUV.AREA_GIS_RIPROPORZIONATA,0) AS AREA_GIS_RIPROPORZIONATA, " +
        "       NVL(CUV.AREA,0) AS AREA, " +
        "       NVL(CUV.AREA_POST_ALLINEA,0) AS AREA_POST_ALLINEA, " +
        "       CUV.ESITO_TOLLERANZA_GIS, " +
        "       NVL(CUV.DELTA_SUPERFICIE,0) AS DELTA, " +
        "       CUV.FLAG_ISTANZA_RIESAME, " +
        "       CUV.PERCENTUALE_POSSESSO " +
        "FROM   DB_COMPENSAZIONE_AZIENDA CA, " +
        "       DB_COMPENSAZIONE_UV CUV, " +
        "       DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_UNITA_ARBOREA_DICHIARATA UAD, " +
        "       DB_STORICO_PARTICELLA SP1, " +
        "       DB_STORICO_PARTICELLA SP2, " +
        "       COMUNE COM1, " +
        "       PROVINCIA PROV1, " +
        "       COMUNE COM2, " +
        "       PROVINCIA PROV2, " +
        "       DB_TIPO_UTILIZZO TU1, " +
        "       DB_TIPO_UTILIZZO TU2, " +
        "       DB_TIPO_VARIETA TV1, " +
        "       DB_TIPO_VARIETA TV2, " +
        "       DB_TIPO_TIPOLOGIA_VINO TTV1, " +
        "       DB_TIPO_TIPOLOGIA_VINO TTV2 " +   
        "WHERE  CA.ID_AZIENDA = ? " +
        "AND    CA.DATA_FINE_VALIDITA IS NULL " + 
        "AND    CA.ID_COMPENSAZIONE_AZIENDA = CUV.ID_COMPENSAZIONE_AZIENDA " +
        "AND    CUV.ID_STORICO_UNITA_ARBOREA = SUA.ID_STORICO_UNITA_ARBOREA (+) " +
        "AND    CUV.ID_UNITA_ARBOREA_DICHIARATA = UAD.ID_UNITA_ARBOREA_DICHIARATA (+) " +
        "AND    SUA.ID_PARTICELLA = SP1.ID_PARTICELLA (+) " +
        "AND    SP1.DATA_FINE_VALIDITA (+) IS NULL " +
        "AND    SP1.COMUNE = COM1.ISTAT_COMUNE (+) " +
        "AND    COM1.ISTAT_PROVINCIA = PROV1.ISTAT_PROVINCIA (+) " +
        "AND    SUA.ID_UTILIZZO = TU1.ID_UTILIZZO (+) " +
        "AND    SUA.ID_VARIETA = TV1.ID_VARIETA (+) " +
        "AND    SUA.ID_TIPOLOGIA_VINO = TTV1.ID_TIPOLOGIA_VINO (+) " +
        "AND    UAD.ID_STORICO_PARTICELLA = SP2.ID_STORICO_PARTICELLA (+) " +
        "AND    SP2.COMUNE = COM2.ISTAT_COMUNE (+) " +
        "AND    COM2.ISTAT_PROVINCIA = PROV2.ISTAT_PROVINCIA (+) " +
        "AND    UAD.ID_UTILIZZO = TU2.ID_UTILIZZO (+) " +
        "AND    UAD.ID_VARIETA = TV2.ID_VARIETA (+) " +
        "AND    UAD.ID_TIPOLOGIA_VINO = TTV2.ID_TIPOLOGIA_VINO (+) " +
        "ORDER BY NVL2(CUV.ID_STORICO_UNITA_ARBOREA, COM1.DESCOM, COM2.DESCOM), " +
        "        NVL2 (CUV.ID_STORICO_UNITA_ARBOREA, SP1.SEZIONE, SP2.SEZIONE), " +
        "        NVL2 (CUV.ID_STORICO_UNITA_ARBOREA, SP1.FOGLIO, SP2.FOGLIO), " +
        "        NVL2 (CUV.ID_STORICO_UNITA_ARBOREA, SP1.PARTICELLA, SP2.PARTICELLA), " +
        "        NVL2 (CUV.ID_STORICO_UNITA_ARBOREA, SP1.SUBALTERNO, SP2.SUBALTERNO), " +
        "        NVL2 (CUV.ID_STORICO_UNITA_ARBOREA, SUA.PROGR_UNAR, UAD.PROGR_UNAR), " +
        "        NVL2(CUV.ID_STORICO_UNITA_ARBOREA, TV1.DESCRIZIONE, TV2.DESCRIZIONE), " +
        "        NVL2(CUV.ID_STORICO_UNITA_ARBOREA, TTV1.DESCRIZIONE, TTV2.DESCRIZIONE) ");
     
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::getUVPerCompensazione] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<UVCompensazioneVO>();
        }
        UVCompensazioneVO uVCompensazioneVO = new UVCompensazioneVO();
        uVCompensazioneVO.setDescComune(rs.getString("DESCOM"));
        uVCompensazioneVO.setSiglaProv(rs.getString("SIGLA_PROVINCIA"));
        uVCompensazioneVO.setSezione(rs.getString("SEZIONE"));
        uVCompensazioneVO.setFoglio(rs.getString("FOGLIO"));
        uVCompensazioneVO.setParticella(rs.getString("PARTICELLA"));
        uVCompensazioneVO.setSubalterno(rs.getString("SUBALTERNO"));
        uVCompensazioneVO.setProgrUnar(rs.getString("PROGR_UNAR"));
        uVCompensazioneVO.setDataImpianto(rs.getTimestamp("DATA_IMPIANTO"));
        uVCompensazioneVO.setDataPrimaProduzione(rs.getTimestamp("DATA_PRIMA_PRODUZIONE"));
        uVCompensazioneVO.setDestinazioneProduttiva(rs.getString("DEST_PRODUTTIVA"));
        uVCompensazioneVO.setVitigno(rs.getString("VITIGNO"));
        uVCompensazioneVO.setIdoneita(rs.getString("IDONEITA"));
        uVCompensazioneVO.setAreaDichiarata(rs.getBigDecimal("AREA_DICHIARATA"));
        uVCompensazioneVO.setAreaGisRiproporzionata(rs.getBigDecimal("AREA_GIS_RIPROPORZIONATA"));
        uVCompensazioneVO.setArea(rs.getBigDecimal("AREA"));
        uVCompensazioneVO.setAreaPostAllinea(rs.getBigDecimal("AREA_POST_ALLINEA"));
        String tolleranzaExcel = null;
        Integer tolleranza = checkInt(rs.getString("ESITO_TOLLERANZA_GIS"));
        if(tolleranza.compareTo(SolmrConstants.IN_TOLLERANZA) == 0) 
        {     
          tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_OK;
        }
        else if(tolleranza.compareTo(SolmrConstants.FUORI_TOLLERANZA) == 0) 
        {
          tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_FUORI_KO;              
        }
        else if(tolleranza.compareTo(SolmrConstants.UVDOPPIE_TOLLERANZA) == 0) 
        {
          tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_UVDOPPIE_KO;              
        }
        else if(tolleranza.compareTo(SolmrConstants.NO_PARCELLE_TOLLERANZA) == 0) 
        {
          tolleranzaExcel = SolmrConstants.ISO_TOLLERANZA_NO_PARCELLE_KO;              
        }
        else if(tolleranza.compareTo(SolmrConstants.ERR_PL_TOLLERANZA) == 0)
        {
          tolleranzaExcel = SolmrConstants.ISO_TOLLERANZA_PLSQL_KO;              
        }
        else if(tolleranza.compareTo(SolmrConstants.UV_NON_PRESENTE) == 0)
        {
          tolleranzaExcel = SolmrConstants.UV_NON_PRESENTE_KO;              
        }
        else if(tolleranza.compareTo(SolmrConstants.PARTICELLA_ORFANA) == 0)
        {
          tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_PARTICELLA_ORFANA_KO;              
        }
        else if(tolleranza.compareTo(SolmrConstants.UV_PIU_OCCORR_ATTIVE) == 0)
        {
          tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_PIU_OCCORR_ATTIVE_KO;              
        }
        else
        {
          tolleranzaExcel = SolmrConstants.UV_TOLLERANZA_PARCELLE_NO_VITE_KO;              
        }
        uVCompensazioneVO.setTolleranza(tolleranzaExcel);
        uVCompensazioneVO.setDelta(rs.getBigDecimal("DELTA"));
        String istanzaRiesame = "No";
        int istanzaRiesameInt = rs.getInt("FLAG_ISTANZA_RIESAME");
        if(istanzaRiesameInt == 1)
        {
          istanzaRiesame = "Si";
        }
        uVCompensazioneVO.setIstanzaRiesame(istanzaRiesame);
        uVCompensazioneVO.setPercentualePossesso(rs.getBigDecimal("PERCENTUALE_POSSESSO"));
        
        result.add(uVCompensazioneVO);
        
        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getUVPerCompensazione] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getUVPerCompensazione] END.");
    }
  }
  
  
  /**
   * 
   * Estrarre i dati dalla tabella db_compensazione_azienda
   * per allinea UV con compensazione
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public CompensazioneAziendaVO getCompensazioneAzienda(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    CompensazioneAziendaVO result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getCompensazioneAzienda] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "SELECT CA.DATA_INIZIO_VALIDITA AS DATA_CALCOLO_COMPENSAZIONE, " +
        "       CA.DATA_COMPENSAZIONE, " +
        "       CA.DATA_CONSOLIDAMENTO_GIS, " +
        "       DC.DATA, " +
        "       IDI.DATA_INIZIO_VALIDITA AS DATA_ELABORAZIONE_ISOLE " +
        "FROM   DB_COMPENSAZIONE_AZIENDA CA, " +
        "       DB_ISOLA_DICHIARATA IDI, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC " +
        "WHERE  CA.ID_AZIENDA = ? " +
        "AND    CA.DATA_FINE_VALIDITA IS NULL " +
        "AND    CA.ID_ISOLA_DICHIARATA = IDI.ID_ISOLA_DICHIARATA (+) " +
        "AND    CA.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA (+)");
     
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::getCompensazioneAzienda] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
    
      ResultSet rs = stmt.executeQuery();
      if (rs.next())
      {
        result = new CompensazioneAziendaVO();
        result.setDataCalcoloCompensazione(rs.getTimestamp("DATA_CALCOLO_COMPENSAZIONE"));
        result.setDataAllineamentoCompensazione(rs.getTimestamp("DATA_COMPENSAZIONE"));
        result.setDataConsolidamentoGis(rs.getTimestamp("DATA_CONSOLIDAMENTO_GIS"));
        result.setDataDichiarazione(rs.getTimestamp("DATA"));
        result.setDataUltimaElaborazioneIsole(rs.getTimestamp("DATA_ELABORAZIONE_ISOLE"));        
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getCompensazioneAzienda] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getCompensazioneAzienda] END.");
    }
  }
  
  
  /**
   * Estrae i dati del riepilogo sup post allinea
   * dell'allinea uv concompensazione
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<RiepilogoCompensazioneVO> getRiepilogoPostAllinea(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<RiepilogoCompensazioneVO> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getRiepilogoPostAllinea] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "SELECT NVL(SUM(SUA.AREA),0) AS SUP_VIT_LAVORAZIONE, " +
        "       NVL(SUM(CUV.AREA_POST_ALLINEA),0) AS SUP_POST_ALLINEA, " +
        "       NVL(SUM(CUV.AREA_DICHIARATA),0) AS SUP_AREA_DICHIARATA, " +
        "       NVL(SUM(CUV.DELTA_SUPERFICIE),0) AS DELTA, " +
        "       SUA.ID_UTILIZZO, " +
        "       SUA.ID_VARIETA, " +
        "       SUA.ID_TIPOLOGIA_VINO, " +
        "       TU.CODICE AS COD_DEST_PROD, " +
        "       TU.DESCRIZIONE AS DEST_PROD, " +
        "       TV.CODICE_VARIETA AS COD_VITIGNO, " +
        "       TV.DESCRIZIONE AS DESC_VITIGNO, " +
        "       TTV.DESCRIZIONE AS IDONEITA " +
        "FROM   DB_COMPENSAZIONE_AZIENDA CA, " +
        "       DB_COMPENSAZIONE_UV CUV, " +
        "       DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_TIPOLOGIA_VINO TTV " +         
        "WHERE  CA.ID_AZIENDA = ? " +
        "AND    CA.DATA_FINE_VALIDITA IS NULL " +
        "AND    CA.ID_COMPENSAZIONE_AZIENDA = CUV.ID_COMPENSAZIONE_AZIENDA " +
        //"AND    CUV.AREA_POST_ALLINEA IS NOT NULL " +         
        "AND    CUV.ID_STORICO_UNITA_ARBOREA = SUA.ID_STORICO_UNITA_ARBOREA " +
        "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
        "AND    SUA.ID_UTILIZZO = TU.ID_UTILIZZO " +
        "AND    SUA.ID_VARIETA  = TV.ID_VARIETA " +
        "AND    SUA.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO (+) " +
        "GROUP BY SUA.ID_UTILIZZO, " +
        "         SUA.ID_VARIETA, " +
        "         SUA.ID_TIPOLOGIA_VINO, " +
        "         TU.CODICE, " +
        "         TU.DESCRIZIONE, " +
        "         TV.CODICE_VARIETA, " +
        "         TV.DESCRIZIONE, " +
        "         TTV.DESCRIZIONE " +
        "ORDER BY TU.DESCRIZIONE, " +
        "         TV.DESCRIZIONE, " +
        "         TTV.DESCRIZIONE ");
     
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::getRiepilogoPostAllinea] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<RiepilogoCompensazioneVO>();
        }
        RiepilogoCompensazioneVO riepilogoCompensazioneVO = new RiepilogoCompensazioneVO();
        riepilogoCompensazioneVO.setSupVitLavorazione(rs.getBigDecimal("SUP_VIT_LAVORAZIONE"));
        riepilogoCompensazioneVO.setSupPastAllinea(rs.getBigDecimal("SUP_POST_ALLINEA"));
        riepilogoCompensazioneVO.setSupAreaDichiarata(rs.getBigDecimal("SUP_AREA_DICHIARATA"));
        riepilogoCompensazioneVO.setSumDelta(rs.getBigDecimal("DELTA"));
        riepilogoCompensazioneVO.setCodDestProd(rs.getString("COD_DEST_PROD"));
        riepilogoCompensazioneVO.setDescDestProd(rs.getString("DEST_PROD"));
        riepilogoCompensazioneVO.setCodVitigno(rs.getString("COD_VITIGNO"));
        riepilogoCompensazioneVO.setDescVitigno(rs.getString("DESC_VITIGNO"));
        riepilogoCompensazioneVO.setIdoneita(rs.getString("IDONEITA"));        
        
        result.add(riepilogoCompensazioneVO);
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getRiepilogoPostAllinea] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getRiepilogoPostAllinea] END.");
    }
  }
  
  /**
   * Estrae i dati per il riepilogo diritti vitati
   * nella sezione allinea UV con compensazione
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<RiepilogoCompensazioneVO> getRiepilogoDirittiVitati(long idAzienda)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<RiepilogoCompensazioneVO> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getRiepilogoDirittiVitati] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "SELECT TU.CODICE AS COD_DEST_PROD, " +
        "       TU.DESCRIZIONE AS DESC_DEST_PROD, " +
        "       TV.CODICE_VARIETA AS COD_VITIGNO, " +
        "       TV.DESCRIZIONE AS DESC_VITIGNO, " +
        "       DU.SUPERFICIE, " +
        "       PCK_SMRGAA_COMPENSAZIONE_UV.RestituisciBloccoVitigno(DU.ID_VARIETA) AS VITIGNO_PARTICOLARE " + 
        "FROM   DB_COMPENSAZIONE_AZIENDA CA, " +
        "       DB_DIRITTO_UV DU, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_TIPO_VARIETA TV " +
        "WHERE  CA.ID_AZIENDA = ? " +
        "AND    CA.DATA_FINE_VALIDITA IS NULL " +
        "AND    CA.ID_COMPENSAZIONE_AZIENDA = DU.ID_COMPENSAZIONE_AZIENDA " +
        "AND    TU.ID_UTILIZZO = 487 " +
        "AND    DU.ID_VARIETA = TV.ID_VARIETA " +
        "ORDER BY TV.DESCRIZIONE ");
     
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::getRiepilogoDirittiVitati] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<RiepilogoCompensazioneVO>();
        }
        RiepilogoCompensazioneVO riepilogoCompensazioneVO = new RiepilogoCompensazioneVO();
        riepilogoCompensazioneVO.setSupVitata(rs.getBigDecimal("SUPERFICIE"));
        riepilogoCompensazioneVO.setCodDestProd(rs.getString("COD_DEST_PROD"));
        riepilogoCompensazioneVO.setDescDestProd(rs.getString("DESC_DEST_PROD"));
        riepilogoCompensazioneVO.setCodVitigno(rs.getString("COD_VITIGNO"));
        riepilogoCompensazioneVO.setDescVitigno(rs.getString("DESC_VITIGNO"));
        riepilogoCompensazioneVO.setVitignoParticolare(rs.getInt("VITIGNO_PARTICOLARE"));        
        
        result.add(riepilogoCompensazioneVO);
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getRiepilogoDirittiVitati] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getRiepilogoDirittiVitati] END.");
    }
  }
  
  
  /**
   * Estraggo gli id_storico_unita_arborea e la superficie post allinea 
   * relative alla uv presenti nella compensazione
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<RiepilogoCompensazioneVO> getVIdStoricoUVPerCompensazione(long idAzienda)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<RiepilogoCompensazioneVO> result = null;
    StringBuffer queryBuf = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getVIdStoricoUVPerCompensazione] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "SELECT SUA.ID_STORICO_UNITA_ARBOREA, " +
        "       SUA.ID_UNITA_ARBOREA, " +
        "       CU.AREA_POST_ALLINEA " +
        "FROM  DB_COMPENSAZIONE_AZIENDA CA, " +
        "      DB_COMPENSAZIONE_UV CU, " +
        "      DB_STORICO_UNITA_ARBOREA SUA " +
        "WHERE CA.ID_AZIENDA = ? " +
        "AND   CA.DATA_FINE_VALIDITA IS NULL " +
        "AND   CU.AREA_POST_ALLINEA IS NOT NULL " +
        "AND   CA.ID_COMPENSAZIONE_AZIENDA = CU.ID_COMPENSAZIONE_AZIENDA " +
        "AND   CU.ID_STORICO_UNITA_ARBOREA = SUA.ID_STORICO_UNITA_ARBOREA");
     
      conn = getDatasource().getConnection();
      query = queryBuf.toString();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger
            .debug(
                this,
                "[StoricoUnitaArboreaGaaDAO::getVIdStoricoUVPerCompensazione] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
    
      ResultSet rs = stmt.executeQuery();
      while (rs.next())
      {
        if(result == null)
        {
          result = new Vector<RiepilogoCompensazioneVO>();
        }
        
        RiepilogoCompensazioneVO riepilogoCompensazioneVO = new RiepilogoCompensazioneVO();
        riepilogoCompensazioneVO.setIdStoricoUnitaArborea(checkLong(rs.getString("ID_STORICO_UNITA_ARBOREA")));
        riepilogoCompensazioneVO.setIdUnitaArborea(checkLong(rs.getString("ID_UNITA_ARBOREA")));
        riepilogoCompensazioneVO.setSupPastAllinea(rs.getBigDecimal("AREA_POST_ALLINEA"));
        
        result.add(riepilogoCompensazioneVO);
      }
      
      return result;
      
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("queryBuf", queryBuf), new Variabile("query", query), 
        new Variabile("result", result)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getVIdStoricoUVPerCompensazione] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getVIdStoricoUVPerCompensazione] END.");
    }
  }
  
  
  /**
   * Estraggo le uv di un'azienda adatte all'allinemanto delle uv in tolleranza.
   * Chiave id_particella
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  /*public HashMap<Long,Vector<TolleranzaVO>> getHashIdParticellaIdStoricoUnitaArboreaTolleranza(
      long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    HashMap<Long,Vector<TolleranzaVO>> result = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getHashIdParticellaIdStoricoUnitaArboreaTolleranza] BEGIN.");
    
      query = "" +
        "SELECT SUA.ID_STORICO_UNITA_ARBOREA, " +
        "       SUA.ID_PARTICELLA, " +
        "       SUA.ID_CATALOGO_MATRICE, " +
        "       SUA.ID_VARIETA, " +
        "       PC.ID_PARTICELLA_CERTIFICATA " +
        "       PCK_SMRGAA_LIBRERIA.UV_IN_TOLLERANZA_GIS(?, SUA.ID_UNITA_ARBOREA) AS TOLLERANZA " +  
        "FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_PARTICELLA_CERTIFICATA PC " +
        "WHERE  SUA.ID_AZIENDA = ? " +
        "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
        "AND    SUA.DATA_CESSAZIONE IS NULL " +
        "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
        "AND    SUA.ID_PARTICELLA = PC.ID_PARTICELLA " +
        "AND    PC.DATA_FINE_VALIDITA IS NULL " +
        "AND    EXISTS (SELECT * " +
        "               FROM    DB_UTE UT, " +
        "                       DB_CONDUZIONE_PARTICELLA CP " +
        "               WHERE   UT.DATA_FINE_ATTIVITA IS NULL " +
        "               AND     UT.ID_AZIENDA = SUA.ID_AZIENDA " +
        "               AND     CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
        "               AND     CP.DATA_FINE_CONDUZIONE IS NULL " +
        "               AND     CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +   
        "               )";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::getHashIdParticellaIdStoricoUnitaArboreaTolleranza] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      stmt.setLong(2, idAzienda); 
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(result == null)
        {
          result = new HashMap<Long,Vector<TolleranzaVO>>();
        }
        Long idParticella = new Long(rs.getLong("ID_PARTICELLA"));
        
        if(result.get(idParticella) != null)
        {
          Vector<TolleranzaVO> vTolleranza = result.get(idParticella);
          TolleranzaVO tolleranzaVO = new TolleranzaVO();
          tolleranzaVO.setIdStoricoUnitaArborea(rs.getLong("ID_STORICO_UNITA_ARBOREA"));
          tolleranzaVO.setIdVarieta(rs.getLong("ID_VARIETA"));
          tolleranzaVO.setIdCatalogoMatrice(rs.getLong("ID_CATALOGO_MATRICE"));
          tolleranzaVO.setIdParticellaCertificata(rs.getLong("ID_PARTICELLA_CERTIFICATA"));
          tolleranzaVO.setTolleranza(rs.getInt("TOLLERANZA"));          
          vTolleranza.add(tolleranzaVO);
          result.put(idParticella, vTolleranza);
        }
        else
        {
          Vector<TolleranzaVO> vTolleranza = new Vector<TolleranzaVO>();
          TolleranzaVO tolleranzaVO = new TolleranzaVO();
          tolleranzaVO.setIdStoricoUnitaArborea(rs.getLong("ID_STORICO_UNITA_ARBOREA"));
          tolleranzaVO.setIdVarieta(rs.getLong("ID_VARIETA"));
          tolleranzaVO.setIdParticellaCertificata(rs.getLong("ID_PARTICELLA_CERTIFICATA"));
          tolleranzaVO.setTolleranza(rs.getInt("TOLLERANZA"));          
          vTolleranza.add(tolleranzaVO);
          result.put(idParticella, vTolleranza);
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
      { new Parametro("idAzienda", idAzienda)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getHashIdParticellaIdStoricoUnitaArboreaTolleranza] ", t,
          query, variabili, parametri);
    
      
      // Rimappo e rilancio l'eccezione come DataAccessException.
      
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      //  Chiudo Connection e PreparedStatement (il metodo è a prova di null ed
      //  ignora ogni eventuale eccezione)
       
      close(null, stmt, conn);
    
      // Fine metodo
      SolmrLogger.debug(this,
          "[StoricoUnitaArboreaGaaDAO::getHashIdParticellaIdStoricoUnitaArboreaTolleranza] END.");
    }
  }*/
  
  
  
  /**
   * 
   * Estraggo il record attivo su DB_COMPENSAZIONE_AZIENDA relativo all'azienda
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public CompensazioneAziendaVO getCompensazioneAziendaByIdAzienda(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    CompensazioneAziendaVO result = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getCompensazioneAziendaByIdAzienda] BEGIN.");
    
      query = "" +
        "SELECT CA.DATA_COMPENSAZIONE, " +
        "       CA.ID_ISOLA_DICHIARATA, " +
        "       CA.FLAG_COMPENSABILE, " +
        "       CA.DATA_INIZIO_VALIDITA  " +
        "FROM   DB_COMPENSAZIONE_AZIENDA CA " +
        "WHERE  CA.ID_AZIENDA = ? " +
        "AND    CA.DATA_FINE_VALIDITA IS NULL ";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::getCompensazioneAziendaByIdAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = new CompensazioneAziendaVO();
        result.setDataAllineamentoCompensazione(rs.getTimestamp("DATA_COMPENSAZIONE")); 
        result.setIdIsolaDichiarata(checkLongNull(rs.getString("ID_ISOLA_DICHIARATA")));
        result.setFlagCompensabile(rs.getString("FLAG_COMPENSABILE"));
        result.setDataCalcoloCompensazione(rs.getTimestamp("DATA_INIZIO_VALIDITA"));
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
          "[StoricoUnitaArboreaGaaDAO::getCompensazioneAziendaByIdAzienda] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getCompensazioneAziendaByIdAzienda] END.");
    }
  }
  
  
  /**
   * 
   * Conta il numero delle uv che possono essere allineate al gis
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public int countUVAllineabiliGis(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    int result = 0;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::countUVAllineabiliGis] BEGIN.");
    
      query = "" +
        "SELECT COUNT(*) AS NUMERO " +
        "FROM   DB_COMPENSAZIONE_AZIENDA CA," +
        "       DB_COMPENSAZIONE_UV CU " +
        "WHERE  CA.ID_AZIENDA = ? " +
        "AND    CA.DATA_FINE_VALIDITA IS NULL " +
        "AND    CA.ID_COMPENSAZIONE_AZIENDA = CU.ID_COMPENSAZIONE_AZIENDA " +
        "AND    CU.ID_STORICO_UNITA_ARBOREA IS NOT NULL " +
        "AND    CU.FLAG_ALLINEA = 'N' ";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::countUVAllineabiliGis] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = rs.getInt("NUMERO");
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
          "[StoricoUnitaArboreaGaaDAO::countUVAllineabiliGis] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::countUVAllineabiliGis] END.");
    }
  }
  
  /**
   * 
   * L'istanza di riesame è stata lavorata oppure non è stata richiesta l'istanza di riesame
   * 
   * counta il numro delle uv 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public int countUVIstRiesameCompen(long idAzienda)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    int result = 0;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::countUVIstRiesameCompen] BEGIN.");
    
      query = "" +
        "SELECT COUNT(*) AS NUMERO " +
        "FROM   DB_COMPENSAZIONE_AZIENDA CA, " +
        "       DB_COMPENSAZIONE_UV CU " +
        "WHERE  CA.ID_AZIENDA = ? " +
        "AND    CA.DATA_FINE_VALIDITA IS NULL " +
        "AND    CA.ID_COMPENSAZIONE_AZIENDA = CU.ID_COMPENSAZIONE_AZIENDA " +
        "AND    CU.ID_STORICO_UNITA_ARBOREA IS NOT NULL " +
        "AND    CU.FLAG_ISTANZA_RIESAME = 1 ";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::countUVIstRiesameCompen] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = rs.getInt("NUMERO");
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
          "[StoricoUnitaArboreaGaaDAO::countUVIstRiesameCompen] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::countUVIstRiesameCompen] END.");
    }
  }
  
  
  /**
   * 
   * La compensazione aziendale non ha individuato alcuna superficie vitata irregolare  sulle
   * quali il delta calcolato è negativo:
   * 
   * restituisce il numero di tali coorrenze
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public int countSupUVIIrregolari(long idAzienda)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    int result = 0;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::countSupUVIIrregolari] BEGIN.");
    
      query = "" +
        "SELECT COUNT(*) AS NUMERO " +
        "FROM   DB_COMPENSAZIONE_AZIENDA CA, " +
        "       DB_DIRITTO_UV DU " +
        "WHERE  CA.ID_AZIENDA = ? " +
        "AND    CA.DATA_FINE_VALIDITA IS NULL " +
        "AND    CA.ID_COMPENSAZIONE_AZIENDA = DU.ID_COMPENSAZIONE_AZIENDA " +
        "AND    DU.SUPERFICIE < 0 ";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::countSupUVIIrregolari] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = rs.getInt("NUMERO");
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
          "[StoricoUnitaArboreaGaaDAO::countSupUVIIrregolari] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::countSupUVIIrregolari] END.");
    }
  }
  
  
  /**
   * 
   * Calcolo la max data aggiornamento tra le conduzione delle uv 
   * e le uv stesse.
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Date getMaxDataAggiornamentoConduzioniAndUV(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Date dataAggiornamento = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getMaxDataAggiornamentoConduzioniAndUV] BEGIN.");
    
      query = "" +
        "SELECT MAX( " +
        "           CASE WHEN " + 
        "           UA.DATA_AGGIORNAMENTO > CP.DATA_AGGIORNAMENTO " +
        "           THEN UA.DATA_AGGIORNAMENTO " +
        "           ELSE CP.DATA_AGGIORNAMENTO " +
        "           END " +
        "        ) AS DATA_MAX " +
        "FROM " +
        "       DB_STORICO_UNITA_ARBOREA UA, " +
        "       DB_CONDUZIONE_PARTICELLA CP, " +
        "       DB_UTE UT " +
        "WHERE " +
        "       UA.ID_AZIENDA = ? " +
        "AND    UA.ID_TIPOLOGIA_UNAR = 2 " +          
        "AND    UA.ID_AZIENDA = UT.ID_AZIENDA " +
        "AND    CP.ID_PARTICELLA = UA.ID_PARTICELLA " + 
        "AND    CP.ID_TITOLO_POSSESSO NOT IN (5,6) ";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::getMaxDataAggiornamentoConduzioniAndUV] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        dataAggiornamento = rs.getTimestamp("DATA_MAX");
        
      }
      
      return dataAggiornamento;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("dataAggiornamento", dataAggiornamento) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getMaxDataAggiornamentoConduzioniAndUV] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getMaxDataAggiornamentoConduzioniAndUV] END.");
    }
  }
  
  /**
   * 
   * La modifica della fotointerpretazione GIS è precedente all'esportazione 
   * della  compensazione aziendale
   * 
   * Prende la max data_ultima_elaborazione
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Date getMaxDataFotoInterpretazioneUV(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Date dataAggiornamento = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getMaxDataFotoInterpretazioneUV] BEGIN.");
    
      query = "" +
        "SELECT MAX(PC.DATA_ULTIMA_LAVORAZIONE) AS MAX_DATA " +
        "FROM   DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_PARTICELLA_CERTIFICATA PC " +
        "WHERE  PC.ID_PARTICELLA = SUA.ID_PARTICELLA " +
        "AND    PC.DATA_FINE_VALIDITA IS NULL " +
        "AND    SUA.ID_AZIENDA = ? " +
        "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
        "AND    SUA.DATA_CESSAZIONE IS NULL " +
        "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
        "AND    EXISTS (SELECT *  " +
        "               FROM   DB_UTE UT, " +
        "                      DB_CONDUZIONE_PARTICELLA CP " +
        "               WHERE  UT.DATA_FINE_ATTIVITA IS NULL " +
        "               AND    UT.ID_AZIENDA = SUA.ID_AZIENDA " +
        "               AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
        "               AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "               AND    CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +   
        "              )";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::getMaxDataFotoInterpretazioneUV] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        dataAggiornamento = rs.getTimestamp("MAX_DATA");
        
      }
      
      return dataAggiornamento;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("dataAggiornamento", dataAggiornamento) };
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getMaxDataFotoInterpretazioneUV] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getMaxDataFotoInterpretazioneUV] END.");
    }
  }
  
  
  /**
   * 
   * restituisce true se è stata fatta
   * Generazione isole parcella avvenuta dopo l'ultima variazione di schedario.
   * Differenza piano lavorazione / piano culturale usato per generazione isole parcellle
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public boolean existsIsoleParcDopoVarSchedLavMinIso(long idAzienda, long isolaDichiarata)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result = false;;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::existsIsoleParcDopoVarSched] BEGIN.");
    
      query = "" +
        "SELECT SUA.ID_PARTICELLA " +
        "FROM   DB_STORICO_UNITA_ARBOREA SUA " +
        "WHERE  SUA.ID_AZIENDA = ? " +
        "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
        "AND    SUA.DATA_CESSAZIONE IS NULL " +
        "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
        "AND    EXISTS (SELECT * " +
        "               FROM   DB_UTE UT, " +
        "                      DB_CONDUZIONE_PARTICELLA CP " +
        "               WHERE  UT.DATA_FINE_ATTIVITA IS NULL " +
        "               AND    UT.ID_AZIENDA = SUA.ID_AZIENDA " +
        "               AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
        "               AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "               AND    CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +   
        "              ) " +
        "GROUP BY SUA.ID_PARTICELLA " +
        "MINUS " +
        "SELECT SUA.ID_PARTICELLA " +
        "FROM   DB_ISOLA_DICHIARATA ISD, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_UNITA_ARBOREA_DICHIARATA UD, " +
        "       DB_STORICO_UNITA_ARBOREA SUA  " +
        "WHERE  ISD.ID_ISOLA_DICHIARATA = ? " +
        "AND    ISD.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "AND    UD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    SUA.ID_STORICO_UNITA_ARBOREA = UD.ID_STORICO_UNITA_ARBOREA " +
        "GROUP BY SUA.ID_PARTICELLA ";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::existsIsoleParcDopoVarSched] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      stmt.setLong(2, isolaDichiarata);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = true;        
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
      { new Parametro("idAzienda", idAzienda),
        new Parametro("isolaDichiarata", isolaDichiarata) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::existsIsoleParcDopoVarSched] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::existsIsoleParcDopoVarSched] END.");
    }
  }
  
  /**
   * Aggiorno data_compensazione a sysdate.
   * Finito il ciclo dell'allineamento a compensazione
   * 
   * 
   * @param idAzienda
   * @throws DataAccessException
   */
  public void updateCompensazioneAzienda(long idAzienda, long idUtente) 
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
              "[StoricoUnitaArboreaGaaDAO::updateCompensazioneAzienda] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_COMPENSAZIONE_AZIENDA   " 
              + "     SET DATA_COMPENSAZIONE = SYSDATE, "
              + "         ID_UTENTE_AGGIORNAMENTO = ? "
              + "   WHERE  "
              + "         ID_AZIENDA = ?  "
              + "   AND   DATA_FINE_VALIDITA IS NULL  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::updateCompensazioneAzienda] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idUtente);    
      stmt.setLong(++indice, idAzienda);     
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtente", idUtente),
          new Parametro("idAzienda", idAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StoricoUnitaArboreaGaaDAO::updateCompensazioneAzienda] ",
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
              "[StoricoUnitaArboreaGaaDAO::updateCompensazioneAzienda] END.");
    }
  }
  
  
  /**
   * 
   * restituisce true se è stata fatta
   * Generazione isole parcella avvenuta dopo l'ultima variazione di schedario.
   * Differenza  piano culturale usato per generazione isole parcellle /piano lavorazione
   * 
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public boolean existsIsoleParcDopoVarSchedIsoMinLav(long idAzienda, long isolaDichiarata)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean result = false;;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::existsIsoleParcDopoVarSched] BEGIN.");
    
      query = "" +
        "SELECT SUA.ID_PARTICELLA " +
        "FROM   DB_ISOLA_DICHIARATA ISD, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_UNITA_ARBOREA_DICHIARATA UD, " +
        "       DB_STORICO_UNITA_ARBOREA SUA  " +
        "WHERE  ISD.ID_ISOLA_DICHIARATA = ? " +
        "AND    ISD.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "AND    UD.CODICE_FOTOGRAFIA_TERRENI = DC.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    SUA.ID_STORICO_UNITA_ARBOREA = UD.ID_STORICO_UNITA_ARBOREA " +
        "GROUP BY SUA.ID_PARTICELLA "+
        "MINUS " +
        "SELECT SUA.ID_PARTICELLA " +
        "FROM   DB_STORICO_UNITA_ARBOREA SUA " +
        "WHERE  SUA.ID_AZIENDA = ? " +
        "AND    SUA.DATA_FINE_VALIDITA IS NULL " +
        "AND    SUA.DATA_CESSAZIONE IS NULL " +
        "AND    SUA.ID_TIPOLOGIA_UNAR = 2 " +
        "AND    EXISTS (SELECT * " +
        "               FROM   DB_UTE UT, " +
        "                      DB_CONDUZIONE_PARTICELLA CP " +
        "               WHERE  UT.DATA_FINE_ATTIVITA IS NULL " +
        "               AND    UT.ID_AZIENDA = SUA.ID_AZIENDA " +
        "               AND    CP.ID_PARTICELLA = SUA.ID_PARTICELLA " +
        "               AND    CP.DATA_FINE_CONDUZIONE IS NULL " +
        "               AND    CP.ID_TITOLO_POSSESSO NOT IN (5,6) " +   
        "              ) " +
        "GROUP BY SUA.ID_PARTICELLA ";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::existsIsoleParcDopoVarSched] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, isolaDichiarata);
      stmt.setLong(2, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = true;        
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
      { new Parametro("idAzienda", idAzienda),
        new Parametro("isolaDichiarata", isolaDichiarata) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::existsIsoleParcDopoVarSched] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::existsIsoleParcDopoVarSched] END.");
    }
  }
  
  /**
   * 
   * ritorna il valore della superficie non compensabile
   * delta negativo
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSupNonCompensabile(long idAzienda)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal result = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getSupNonCompensabile] BEGIN.");
    
      query = "" +
        "SELECT SUM(CU.DELTA_SUPERFICIE) AS DELTA " +
        "FROM " +
        "       DB_COMPENSAZIONE_UV CU, " +
        "       DB_COMPENSAZIONE_AZIENDA CA " +
        "WHERE  CA.ID_AZIENDA = ? " +
        "AND    CA.DATA_FINE_VALIDITA IS NULL " +
        "AND    CA.ID_COMPENSAZIONE_AZIENDA = CU.ID_COMPENSAZIONE_AZIENDA ";
        //"AND    CU.ID_UNITA_ARBOREA_DICHIARATA IS NOT NULL ";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::getSupNonCompensabile] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = rs.getBigDecimal("DELTA");     
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
      { new Parametro("idAzienda", idAzienda) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getSupNonCompensabile] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getSupNonCompensabile] END.");
    }
  }
  
  
  /**
   * Conta il numro delle occorrenze con percentuale possesso maggiore di 100
   * 
   * 
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public int countPercPossessoCompensazioneMag100(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    int result = 0;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::countPercPossessoCompensazioneMag100] BEGIN.");
    
      query = "" +
        "SELECT COUNT(*) AS NUMERO " +
        "FROM   DB_COMPENSAZIONE_AZIENDA CA," +
        "       DB_COMPENSAZIONE_UV CU " +
        "WHERE  CA.ID_AZIENDA = ? " +
        "AND    CA.DATA_FINE_VALIDITA IS NULL " +
        "AND    CA.ID_COMPENSAZIONE_AZIENDA = CU.ID_COMPENSAZIONE_AZIENDA " +
        "AND    CU.ID_STORICO_UNITA_ARBOREA IS NOT NULL " +
        "AND    CU.PERCENTUALE_POSSESSO > 100 ";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::countPercPossessoCompensazioneMag100] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = rs.getInt("NUMERO");
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
          "[StoricoUnitaArboreaGaaDAO::countPercPossessoCompensazioneMag100] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::countPercPossessoCompensazioneMag100] END.");
    }
  }
  
  
  /**
   * 
   * setto data_consolidamento_gis!!!
   * 
   * 
   * @param idAzienda
   * @throws DataAccessException
   */
  public void updateCompensazioneAziendaConsolidamento(long idAzienda, long idUtente) 
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
              "[StoricoUnitaArboreaGaaDAO::updateCompensazioneAziendaConsolidamento] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   UPDATE DB_COMPENSAZIONE_AZIENDA   " 
              + "     SET DATA_CONSOLIDAMENTO_GIS = SYSDATE, " +
              "           ID_UTENTE_AGGIORNAMENTO = ? "
              + "   WHERE  "
              + "         ID_AZIENDA = ?  "
              + "   AND   DATA_FINE_VALIDITA IS NULL  ");
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::updateCompensazioneAziendaConsolidamento] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setLong(++indice, idUtente);
      stmt.setLong(++indice, idAzienda);     
  
      stmt.executeUpdate();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtente", idUtente),
          new Parametro("idAzienda", idAzienda)};
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StoricoUnitaArboreaGaaDAO::updateCompensazioneAziendaConsolidamento] ",
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
              "[StoricoUnitaArboreaGaaDAO::updateCompensazioneAziendaConsolidamento] END.");
    }
  }
  
  
  public void consolidaUnitaArboree(Vector<Long> vIdUnitaArborea) 
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
              "[StoricoUnitaArboreaGaaDAO::consolidaUnitaArboree] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("UPDATE DB_UNITA_ARBOREA " +
                  " SET    DATA_CONSOLIDAMENTO_GIS = SYSDATE " +
                  " WHERE  ID_UNITA_ARBOREA = ? ");
      
      query = queryBuf.toString();     

      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::consolidaUnitaArboree] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);

      for(int i = 0; i < vIdUnitaArborea.size(); i++) 
      {
        int indice = 0;
        stmt.setLong(++indice, vIdUnitaArborea.get(i).longValue());        
        stmt.addBatch();
      }
      
      stmt.executeBatch();
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf)};
  
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("vIdUnitaArborea", vIdUnitaArborea) };
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[StoricoUnitaArboreaGaaDAO::consolidaUnitaArboree] ",
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
              "[StoricoUnitaArboreaGaaDAO::consolidaUnitaArboree] END.");
    }
  }
  
  
  /**
   * 
   * EStare la percentuale utilizzata eleggibile a gis
   * 
   * 
   * @param idAzienda
   * @param idParticella
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getPercUtilizzoEleggibile(long idAzienda, long idParticella)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal result = new BigDecimal(0);
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getPercUtilizzoEleggibile] BEGIN.");
    
      query = "" +
        "SELECT CE.PERCENTUALE_UTILIZZO " +
        "FROM   DB_CONDUZIONE_ELEGGIBILITA CE " +
        "WHERE  CE.ID_AZIENDA = ? " +
        "AND    CE.DATA_FINE_VALIDITA IS NULL " +
        "AND    CE.ID_PARTICELLA = ? " +
        "AND    CE.ID_ELEGGIBILITA_FIT = 26 "; //Vite
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::getPercUtilizzoEleggibile] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      stmt.setLong(2, idParticella);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = rs.getBigDecimal("PERCENTUALE_UTILIZZO");
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
          "[StoricoUnitaArboreaGaaDAO::getPercUtilizzoEleggibile] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getPercUtilizzoEleggibile] END.");
    }
  }
  
  
  
  public Vector<Vector<DirittoCompensazioneVO>> getDirittiCalcolati(long idAzienda)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<Vector<DirittoCompensazioneVO>> vDirittiCalcolati = null;
    Vector<DirittoCompensazioneVO> vDirittiCalcolatiVarieta = null;
    DirittoCompensazioneVO dirittoCompensazioneVO = null;
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getDirittiCalcolati] BEGIN.");
    
      query = 
        "SELECT TV.DESCRIZIONE AS DESC_VARIETA, " +
        "       DC.ID_VARIETA, " +
        "       NVL(DC.SUP_DIRITTI_COMP,0) AS SUP_DIRITTI_COMP, " +
        "       TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
        "       DC.ID_TIPOLOGIA_VINO, " +
        "       NVL(DC.SUP_DIRITTI_IPO,0) AS SUP_DIRITTI_IPO, " +
        "       NVL(DC.SUP_VITI_ESTIRPO_VAR,0) AS SUP_VITI_ESTIRPO_VAR, " +
        "       DC.FLAG_VITIGNO_PARTICOLARE, " +
        "       NVL(DC.SUP_VITI_ESTIRPO_AZ,0) AS SUP_VITI_ESTIRPO_AZ, " +
        "       NVL(DC.SUP_VITI_ESTIRPO_VARBLOC_AZ,0) AS SUP_VITI_ESTIRPO_VARBLOC_AZ, " +
        "       NVL(DC.SUP_UV_DUPLICATE,0) AS SUP_UV_DUPLICATE, " +
        "       NVL(DC.SUP_DIRITTI_CONDIVISI,0) AS SUP_DIRITTI_CONDIVISI, " +
        "       DC.DIRITTI_CONDIV_TEMPO, " +
        "       NVL(DC.SUP_POST_CONS,0) AS SUP_POST_CONS,  " +
        "       NVL(DC.SUP_FINALE_ASSEGNATA,0) AS SUP_FINALE_ASSEGNATA " +
        "FROM   DB_COMPENSAZIONE_AZIENDA CA, " +
        "       DB_DIRITTO_DA_COMPENSAZIONE DC, " +
        "       DB_TIPO_VARIETA TV, " +
        "       DB_TIPO_TIPOLOGIA_VINO TTV " +
        "WHERE  CA.ID_AZIENDA = ? " +
        "AND    CA.DATA_CONSOLIDAMENTO_GIS IS NOT NULL " +
        "AND    CA.ID_COMPENSAZIONE_AZIENDA = DC.ID_COMPENSAZIONE_AZIENDA " +
        "AND    DC.ID_VARIETA = TV.ID_VARIETA " +
        "AND    DC.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " +
        "ORDER BY TV.DESCRIZIONE ASC, " +
        "         TTV.DESCRIZIONE ASC";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::getDirittiCalcolati] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idAzienda);
      
      ResultSet rs = stmt.executeQuery();
      
      
      Long idVarietaTmp = new Long(0);
      while (rs.next())
      {
        if(vDirittiCalcolati == null)
        {
          vDirittiCalcolati = new Vector<Vector<DirittoCompensazioneVO>>();
        }
        
        Long idVarieta = new Long(rs.getLong("ID_VARIETA"));
        if(idVarieta.compareTo(idVarietaTmp) != 0)
        {
          
          if(idVarietaTmp.compareTo(new Long(0)) !=0)
          {
            vDirittiCalcolati.add(vDirittiCalcolatiVarieta);
          }
          vDirittiCalcolatiVarieta = new Vector<DirittoCompensazioneVO>();          
        }        
        
        dirittoCompensazioneVO = new DirittoCompensazioneVO();
        dirittoCompensazioneVO.setDescVarieta(rs.getString("DESC_VARIETA"));
        dirittoCompensazioneVO.setIdVarieta(idVarieta);
        dirittoCompensazioneVO.setSupDirittiComp(rs.getBigDecimal("SUP_DIRITTI_COMP"));
        dirittoCompensazioneVO.setDescTipologiaVino(rs.getString("DESC_TIPO_VINO"));
        dirittoCompensazioneVO.setIdTipologiaVino(rs.getLong("ID_TIPOLOGIA_VINO"));
        dirittoCompensazioneVO.setSupDirittiIpo(rs.getBigDecimal("SUP_DIRITTI_IPO"));
        dirittoCompensazioneVO.setSupVitiEstirpoVar(rs.getBigDecimal("SUP_VITI_ESTIRPO_VAR"));
        dirittoCompensazioneVO.setFlagVitignoParticolare(rs.getString("FLAG_VITIGNO_PARTICOLARE"));
        dirittoCompensazioneVO.setSupVitiEstirpoAz(rs.getBigDecimal("SUP_VITI_ESTIRPO_AZ"));
        dirittoCompensazioneVO.setSupVitiEstirpoVarblocAz(rs.getBigDecimal("SUP_VITI_ESTIRPO_VARBLOC_AZ"));
        dirittoCompensazioneVO.setSupUvDuplicate(rs.getBigDecimal("SUP_UV_DUPLICATE"));
        dirittoCompensazioneVO.setSupDirittiCondivisi(rs.getBigDecimal("SUP_DIRITTI_CONDIVISI"));
        dirittoCompensazioneVO.setDirittiCondivTempo(rs.getString("DIRITTI_CONDIV_TEMPO"));
        dirittoCompensazioneVO.setSupPostCons(rs.getBigDecimal("SUP_POST_CONS"));
        dirittoCompensazioneVO.setSupFinaleAssegnata(rs.getBigDecimal("SUP_FINALE_ASSEGNATA"));       
        
        idVarietaTmp = idVarieta;
        vDirittiCalcolatiVarieta.add(dirittoCompensazioneVO);
      }
      
      if(vDirittiCalcolati != null)
      {
        vDirittiCalcolati.add(vDirittiCalcolatiVarieta);
      }
      
      return vDirittiCalcolati;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("vDirittiCalcolati", vDirittiCalcolati),
          new Variabile("vDirittiCalcolatiVarieta", vDirittiCalcolatiVarieta),
          new Variabile("dirittoCompensazioneVO", dirittoCompensazioneVO)};
    
      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idAzienda", idAzienda)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getDirittiCalcolati] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getDirittiCalcolati] END.");
    }
  }
  
  /**
   * 
   * Mi restituiesce l'area già assegnata di una divisione di un uv
   * 
   * 
   * @param idUnitaArboreaMadre
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumAreaGiaAssegnata(long idUnitaArborea)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal result = new BigDecimal(0);
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getSumAreaGiaAssegnata] BEGIN.");
    
      query = "" +
        "SELECT NVL(SUM(SUA.AREA),0) AS SUM_AREA " +
        "FROM   DB_STORICO_UNITA_ARBOREA SUA " +
        "WHERE  SUA.ID_UNITA_ARBOREA_MADRE = ? " +
        "AND    SUA.DATA_FINE_VALIDITA IS NULL ";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::getSumAreaGiaAssegnata] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idUnitaArborea);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = rs.getBigDecimal("SUM_AREA");
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
      { new Parametro("idUnitaArborea", idUnitaArborea)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getSumAreaGiaAssegnata] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getSumAreaGiaAssegnata] END.");
    }
  }
  
  
  /**
   * Ritorna la max area assegnabile ai discendenti di un uv in caso di frazionamento
   * 
   * 
   * 
   * @param idUnitaArborea
   * @return
   * @throws DataAccessException
   */
  public BigDecimal getSumAreaMaxAssegnabile(long idUnitaArborea)
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    BigDecimal result = new BigDecimal(0);
    try
    {
      SolmrLogger
          .debug(this,
              "[StoricoUnitaArboreaGaaDAO::getSumAreaMaxAssegnabile] BEGIN.");
    
      query = "" +
        "SELECT SUA.AREA " +
        "FROM   DB_STORICO_UNITA_ARBOREA SUA " +
        "WHERE  SUA.ID_UNITA_ARBOREA = ? " +
        "AND    NVL(SUA.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy'))  " +
        "       = (SELECT MAX(NVL(SUA1.DATA_FINE_VALIDITA,TO_DATE('31/12/9999','dd/mm/yyyy'))) " + 
        "           FROM   DB_STORICO_UNITA_ARBOREA SUA1 " +
        "           WHERE  SUA1.ID_UNITA_ARBOREA = SUA.ID_UNITA_ARBOREA" +
        "           AND NVL(SUA1.ID_UNITA_ARBOREA_MADRE,-1) <> SUA1.ID_UNITA_ARBOREA) ";
        //"WHERE  SUA.ID_STORICO_UNITA_ARBOREA = (SELECT MAX(SUA1.ID_STORICO_UNITA_ARBOREA) " +
        //"                                       FROM   DB_STORICO_UNITA_ARBOREA SUA1  " +
        //"                                       WHERE  SUA1.ID_UNITA_ARBOREA = ? )";
             
      
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
    
        SolmrLogger.debug(this,
            "[StoricoUnitaArboreaGaaDAO::getSumAreaMaxAssegnabile] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idUnitaArborea);
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if (rs.next())
      {
        result = rs.getBigDecimal("AREA");
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
      { new Parametro("idUnitaArborea", idUnitaArborea)  };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[StoricoUnitaArboreaGaaDAO::getSumAreaMaxAssegnabile] ", t,
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
          "[StoricoUnitaArboreaGaaDAO::getSumAreaMaxAssegnabile] END.");
    }
  }


}
