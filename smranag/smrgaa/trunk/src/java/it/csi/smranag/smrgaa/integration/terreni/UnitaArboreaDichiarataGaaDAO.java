package it.csi.smranag.smrgaa.integration.terreni;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.RiepiloghiUnitaArboreaVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.anag.terreni.FiltriUnitaArboreaRicercaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaArboreaExcelVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class UnitaArboreaDichiarataGaaDAO extends it.csi.solmr.integration.BaseDAO
{

  public UnitaArboreaDichiarataGaaDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public UnitaArboreaDichiarataGaaDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }

  /**
   * 
   * 
   * Usata nel riepilogo destinazione produttiva comune.
   * Restituisce l'elenco delle unita vitate relativamente ai comuni
   * e alla dichiarazione di consistenza
   * 
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaComuneDichiarato(long idDichiarazioneConsistenza)
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
              "[UnitaArboreaDichiarataGaaDAO::riepilogoDestinazioneProduttivaComuneDichiarato] BEGIN.");
    
      query = " " +
          "SELECT PROV.DESCRIZIONE, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       COM.DESCOM, " +
          "       COM.ISTAT_COMUNE, " +
          "       SUM (DECODE (RCM.FLAG_TIPO_UVA,'"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_VINO+"', NVL (UAD.AREA, 0), 0)) " +
          "          AS SUM_UVA_DA_VINO, " +
          "       SUM (DECODE (RCM.FLAG_TIPO_UVA,'"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_MENSA+"', NVL (UAD.AREA, 0), 0)) " +
          "          AS SUM_UVA_DA_MENSA, " +
          "       SUM (DECODE (RCM.FLAG_TIPO_UVA,'"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_VINO+"', 0,'"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_MENSA+"', 0,  NVL (UAD.AREA, 0))) " +
          "          AS ALTRE_DESTINAZIONI, " +
          "       SUM (NVL (UAD.AREA, 0)) AS SUP_TOTALE " +
          "  FROM DB_UNITA_ARBOREA_DICHIARATA UAD, " +
          "       DB_STORICO_PARTICELLA SP, " +
          "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "       COMUNE COM, " +
          "       PROVINCIA PROV, " +
          "       DB_R_CATALOGO_MATRICE RCM " +
          " WHERE DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "       AND DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
          "       AND UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
          "       AND UAD.ID_TIPOLOGIA_UNAR = 2 " +
          "       AND SP.COMUNE = COM.ISTAT_COMUNE " +
          "       AND COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "       AND UAD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "GROUP BY PROV.DESCRIZIONE, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       COM.DESCOM, " +
          "       COM.ISTAT_COMUNE " +
          "ORDER BY PROV.DESCRIZIONE, COM.DESCOM";
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idDichiarazioneConsistenza);
    
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
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UnitaArboreaDichiarataGaaDAO::riepilogoDestinazioneProduttivaComuneDichiarato] ", t,
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
          "[UnitaArboreaDichiarataGaaDAO::riepilogoDestinazioneProduttivaComuneDichiarato] END.");
    }
  }
  
  /**
   * 
   * Usata nel riepilogo destinazione produttiva UVA DA VINO.
   * Restituisce l'elenco delle unita vitate relativamente ai comuni
   * e alla dichiarazione di consistenza
   * 
   * 
   * 
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaUvaDaVinoDichiarato(
      long idDichiarazioneConsistenza)
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
              "[UnitaArboreaDichiarataGaaDAO::riepilogoDestinazioneProduttivaUvaDaVinoDichiarato] BEGIN.");
    
      query = " " +
          "SELECT PROV.DESCRIZIONE, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       COM.DESCOM, " +
          "       COM.ISTAT_COMUNE, " +
          "       SUM(DECODE(TTV.VINO_DOC, 'S', NVL(UAD.AREA,0), 0)) AS SUM_SUP_VITATA_DOP, " +
          "       SUM(DECODE(TTV.VINO_DOC, 'S', NVL(UAD.SUPERFICIE_DA_ISCRIVERE_ALBO,0), 0)) AS SUM_SUP_ISCRITTA_DOP, " +
          "       SUM(DECODE(NVL(TTV.VINO_DOC,'N'), 'N', NVL(UAD.AREA,0), 0)) AS SUM_SUP_VITATA_VINO_TAVOLA, " +
          "       SUM(DECODE(NVL(TTV.VINO_DOC,'N'), 'N', NVL(UAD.SUPERFICIE_DA_ISCRIVERE_ALBO,0), 0)) AS SUM_SUP_ISCRITTA_VINO_TAVOLA, " +
          "       SUM(NVL(UAD.AREA,0)) AS SUP_VITATA, " +
          "       SUM(NVL(UAD.SUPERFICIE_DA_ISCRIVERE_ALBO,0)) AS SUP_ISCRITTA " +
          "FROM   DB_UNITA_ARBOREA_DICHIARATA UAD, " +
          "       DB_STORICO_PARTICELLA SP, " +
          "       DB_R_CATALOGO_MATRICE RCM, " +
          "       DB_TIPO_TIPOLOGIA_VINO TTV, " +
          "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "       COMUNE COM, " +
          "       PROVINCIA PROV " +
          "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
          "AND    UAD.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
          "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "AND    UAD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "AND    RCM.FLAG_TIPO_UVA = '"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_VINO+"' "+
          "AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
          "GROUP BY PROV.DESCRIZIONE, PROV.SIGLA_PROVINCIA, COM.DESCOM, COM.ISTAT_COMUNE " +
          "ORDER BY PROV.DESCRIZIONE, COM.DESCOM";
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idDichiarazioneConsistenza);
    
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
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UnitaArboreaDichiarataGaaDAO::riepilogoDestinazioneProduttivaUvaDaVinoDichiarato] ", t,
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
          "[UnitaArboreaDichiarataGaaDAO::riepilogoDestinazioneProduttivaUvaDaVinoDichiarato] END.");
    }
  }
  
  /**
   * 
   * 
   * Usata nel riepilogo destinazione produttiva per VINO DOP.
   * Restituisce l'elenco delle unita vitate relativamente alla descrizione del vino
   * e alla dichiarazione di consistenza
   * 
   * 
   * 
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoDestinazioneProduttivaVinoDOPDichiarato(
      long idDichiarazioneConsistenza)
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
              "[UnitaArboreaDichiarataGaaDAO::riepilogoDestinazioneProduttivaVinoDOPDichiarato] BEGIN.");
    
      query = " " +
          "SELECT TTV.DESCRIZIONE, " +
          "       TTV.ID_TIPOLOGIA_VINO, " +
          "       COM.DESCOM, " +
          "       COM.ISTAT_COMUNE, " +
          "       PROV.SIGLA_PROVINCIA, " +
          "       SUM(NVL(UAD.AREA,0)) AS SUP_VITATA, " +
          "       SUM(NVL(UAD.SUPERFICIE_DA_ISCRIVERE_ALBO,0)) AS SUP_ISCRITTA " +
          "FROM   DB_UNITA_ARBOREA_DICHIARATA UAD, " +
          "       DB_R_CATALOGO_MATRICE RCM, " +
          "       DB_STORICO_PARTICELLA SP, " +
          "       DB_TIPO_TIPOLOGIA_VINO TTV, " +
          "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
          "       COMUNE COM, " +
          "       PROVINCIA PROV " +
          "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
          "AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
          "AND    UAD.ID_TIPOLOGIA_UNAR = 2 " +
          "AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
          "AND    SP.COMUNE = COM.ISTAT_COMUNE " +
          "AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +
          "AND    UAD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
          "AND    RCM.FLAG_TIPO_UVA = '"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_VINO+"' "+
          "AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO " +
          "AND    TTV.VINO_DOC = 'S' " +
          "GROUP BY TTV.DESCRIZIONE, TTV.ID_TIPOLOGIA_VINO, COM.DESCOM, COM.ISTAT_COMUNE, PROV.SIGLA_PROVINCIA " +
          "ORDER BY TTV.DESCRIZIONE, COM.DESCOM";
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idDichiarazioneConsistenza);
    
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
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UnitaArboreaDichiarataGaaDAO::riepilogoDestinazioneProduttivaVinoDOPDichiarato] ", t,
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
          "[UnitaArboreaDichiarataGaaDAO::riepilogoDestinazioneProduttivaVinoDOPDichiarato] END.");
    }
  }
  
  
  /**
   * 
   * Usata nel riepilogo per VINO DOP e provincia.
   * Restituisce l'elenco delle unita vitate relativamente alla descrizione del vino
   * e alla dichiarazione di consistenza
   * 
   * 
   * 
   * 
   * @param idDichiarazioneConsistenza
   * @return
   * @throws DataAccessException
   */
  public Vector<RiepiloghiUnitaArboreaVO> riepilogoProvinciaVinoDOPDichiarato(
      long idDichiarazioneConsistenza)
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
              "[UnitaArboreaDichiarataGaaDAO::riepilogoProvinciaVinoDOPDichiarato] BEGIN.");
    
      query = " " +
         "SELECT UV.DESCRIZIONE, " +
         "       UV.ID_TIPOLOGIA_VINO, " +
         "       UV.RESA, " +
         "       UV.ISTAT_PROVINCIA, " + 
         "       UV.DESC_PROV, " +
         "       SUM(NVL(UV.UAD_AREA_100,0)) AS SUP_VITATA_100, " +
         "       SUM(NVL(UV.UAD_AREA_70,0)) AS SUP_VITATA_70, " +
         "       SUM(NVL(UV.UAD_AREA_0,0)) AS SUP_VITATA_0, " +
         "       ROUND ((SUM (NVL (UV.UAD_AREA_100, 0)  * NVL (UV.RESA, 0))), 2) AS PROD_RESA_100, " + 
         "       ROUND ((SUM (NVL (UV.UAD_AREA_70, 0) * 0.7  * NVL (UV.RESA, 0))), 2) AS PROD_RESA_70, " + 
         "       ROUND ((SUM (NVL (UV.UAD_AREA_0, 0) * 0  * NVL (UV.RESA, 0))), 2) AS PROD_RESA_0 " +
         "FROM  (SELECT TTV.DESCRIZIONE, " +  
         "              TTV.ID_TIPOLOGIA_VINO, " + 
         "              TTV.RESA, " +
         "              PROV.ISTAT_PROVINCIA, " + 
         "              PROV.DESCRIZIONE AS DESC_PROV, " +
         "              UAD.AREA, " +
         "              CASE " +
         "                   WHEN EXTRACT (YEAR FROM UAD.DATA_PRIMA_PRODUZIONE) < EXTRACT (YEAR FROM SYSDATE) " +
         "                    AND UAD.FLAG_IMPRODUTTIVA = 'N' " +
         "                   THEN UAD.AREA " +                           
         "                   ELSE 0 " +                           
         "               END AS UAD_AREA_100, " + 
         "               CASE " +
         "                   WHEN EXTRACT (YEAR FROM UAD.DATA_PRIMA_PRODUZIONE) = EXTRACT (YEAR FROM SYSDATE) " +
         "                    AND UAD.FLAG_IMPRODUTTIVA = 'N' " +
         "                   THEN UAD.AREA " +                           
         "                   ELSE 0 " +                           
         "               END AS UAD_AREA_70, " + 
         "               CASE " +
         "                   WHEN EXTRACT (YEAR FROM UAD.DATA_PRIMA_PRODUZIONE) > EXTRACT (YEAR FROM SYSDATE) " +
         "                   OR UAD.FLAG_IMPRODUTTIVA = 'S' " + 
         "                   THEN UAD.AREA " +                           
         "                   ELSE 0 " +                           
         "               END AS UAD_AREA_0 " + 
         "       FROM   DB_UNITA_ARBOREA_DICHIARATA UAD, " +
         "              DB_R_CATALOGO_MATRICE RCM, " +
         "              DB_STORICO_PARTICELLA SP, " +
         "              DB_TIPO_TIPOLOGIA_VINO TTV, " +
         "              DB_DICHIARAZIONE_CONSISTENZA DC, " +  
         "              COMUNE COM, " +
         "              PROVINCIA PROV " +
         "       WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +  
         "       AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +  
         "       AND    UAD.ID_TIPOLOGIA_UNAR = 2 " +
         "       AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +  
         "       AND    SP.COMUNE = COM.ISTAT_COMUNE " +
         "       AND    COM.ISTAT_PROVINCIA = PROV.ISTAT_PROVINCIA " +  
         "       AND    UAD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE " +
         "       AND    RCM.FLAG_TIPO_UVA = '"+SolmrConstants.FLAG_UTILIZZO_UVA_DA_VINO+"' "+
         "       AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO) UV " +
         "GROUP BY UV.DESCRIZIONE, " +
         "         UV.ID_TIPOLOGIA_VINO, " +
         "         UV.RESA, " +
         "         UV.ISTAT_PROVINCIA, " + 
         "         UV.DESC_PROV " +
         "ORDER BY UV.DESCRIZIONE, " +
         "         UV.DESC_PROV";
      
         
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      stmt.setLong(1, idDichiarazioneConsistenza);
    
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
      { new Parametro("idDichiarazioneConsistenza", idDichiarazioneConsistenza) };
    
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[UnitaArboreaDichiarataGaaDAO::riepilogoProvinciaVinoDOPDichiarato] ", t,
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
          "[UnitaArboreaDichiarataGaaDAO::riepilogoProvinciaVinoDOPDichiarato] END.");
    }
  }
  
  
  /**
   * restituisce i record per genrare l'excel semplice delle uv
   * alla dichiarazione di consistenza
   * 
   * 
   * @param idAzienda
   * @param filtriUnitaArboreaRicercaVO
   * @return
   * @throws DataAccessException
   */
  public Vector<StoricoParticellaArboreaExcelVO> searchUnitaArboreaDichiarataExcelSempliceByParameters( 
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
              "[UnitaArboreaDichiarataGaaDAO::searchUnitaArboreaDichiarataExcelSempliceByParameters] BEGIN.");
      
      queryBuf = new StringBuffer();
      
      queryBuf.append(
        "WITH START_RIESAME AS " +
        "      (SELECT IR.DATA_RICHIESTA, " +
        "              (CASE " +
        "               WHEN (NVL(IR.DATA_EVASIONE, SYSDATE)) <  DCIR.DATA_INSERIMENTO_DICHIARAZIONE " +
        "               THEN IR.DATA_EVASIONE  " +           
        "               END) AS DATA_EVASIONE, " +               
        "               IR.ID_PARTICELLA " +
        "       FROM   DB_ISTANZA_RIESAME IR, " +
        "              DB_DICHIARAZIONE_CONSISTENZA DCIR " +
        "       WHERE  IR. DATA_RICHIESTA = (  " +                                      
        "                                    SELECT MAX(IRTMP.DATA_RICHIESTA) " +
        "                                    FROM   DB_ISTANZA_RIESAME IRTMP, " +                                    
        "                                           DB_DICHIARAZIONE_CONSISTENZA DCIRTMP " +
        "                                    WHERE  IRTMP.ID_AZIENDA = IR.ID_AZIENDA " +
        "                                    AND    IRTMP.ID_PARTICELLA = IR.ID_PARTICELLA " +
        "                                    AND    DCIRTMP.ID_DICHIARAZIONE_CONSISTENZA = DCIR.ID_DICHIARAZIONE_CONSISTENZA " +
        "                                    AND    IRTMP.DATA_ANNULLAMENTO IS NULL " +
        "                                    AND    IRTMP.DATA_RICHIESTA <   DCIRTMP.DATA_INSERIMENTO_DICHIARAZIONE " +
        "                                    ) " +
        "       AND     DCIR.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "       AND     IR.ID_AZIENDA = ? " +
        "      ), " +
        "   COND_ELEG AS " +
        "              (SELECT ED.ID_PARTICELLA, " +
        "                      ED.PERCENTUALE_UTILIZZO " +
        "               FROM   DB_ELEGGIBILITA_DICHIARATA ED, " +
        "                      DB_DICHIARAZIONE_CONSISTENZA CD " +
        "               WHERE  CD.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "               AND    CD.CODICE_FOTOGRAFIA_TERRENI = ED.CODICE_FOTOGRAFIA_TERRENI " +
        "               AND    ED.ID_ELEGGIBILITA_FIT = 26 ) " +
        "SELECT UAD.MATRICOLA_CCIAA, " +
        "       SUA.ID_UNITA_ARBOREA, " +
        "       DC.ID_DICHIARAZIONE_CONSISTENZA, " +
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
        "       SUM(CD.PERCENTUALE_POSSESSO) AS PERCENTUALE_POSSESSO, " +
        "       PC.ID_PARTICELLA_CERTIFICATA, "+
        "       NVL(PCK_SMRGAA_LIBRERIA.SelTotSupElegByPartEVetCat(PC.ID_PARTICELLA_CERTIFICATA, UAD.ID_CATALOGO_MATRICE),0) AS SUPERFICIE_ELEG, " +
        "       UAD.ID_STORICO_UNITA_ARBOREA, " +
        "       UAD.ID_CATALOGO_MATRICE, " +
        "       UAD.PROGR_UNAR, " +
        "       UAD.DATA_IMPIANTO, " +
        "       UAD.DATA_PRIMA_PRODUZIONE, " +
        "       UAD.AREA, " +
        "       UAD.ID_TIPOLOGIA_VINO, " +
        "       TTV.DESCRIZIONE AS DESC_TIPO_VINO, " +
        "       UAD.ANNO_ISCRIZIONE_ALBO, " +
        "       RCM.ID_VARIETA, " +
        "       TVAR.DESCRIZIONE AS DESC_VARIETA, " +
        "       TVAR.CODICE_VARIETA AS COD_VAR, " +
        "       RCM.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE AS DESC_TIPO_UTILIZZO, " +
        "       UAD.SESTO_SU_FILA, " +
        "       UAD.SESTO_TRA_FILE, " +
        "       UAD.NUM_CEPPI, " +
        "       UAD.ID_FORMA_ALLEVAMENTO, " +
        "       TFA.DESCRIZIONE AS DESC_FORMA_ALLEVAMENTO, " +
        "       UAD.PERCENTUALE_VARIETA, " +
        "       UAD.PRESENZA_ALTRI_VITIGNI, " +
        "       UAD.DATA_FINE_VALIDITA, " +
        "       UAD.ID_CAUSALE_MODIFICA, " +
        "       TCM.DESCRIZIONE AS DESC_CAUSALE_MODIFICA, " +
        "       UAD.VIGNA, " +
        "       UAD.PERCENTUALE_FALLANZA, " +
        "       UAD.FLAG_IMPRODUTTIVA, " +
        "       SRI.DATA_RICHIESTA, " +    
        "       SRI.DATA_EVASIONE, " +
        "       VI.MENZIONE, " +
        "       (CASE " +
        "         WHEN EXISTS (SELECT NE.ID_NOTIFICA_ENTITA " + 
        "               FROM   DB_NOTIFICA_ENTITA NE, " +
        "                      DB_TIPO_ENTITA TE " +
        "               WHERE  TE.CODICE_TIPO_ENTITA = 'U' " +
        "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
        "               AND    NE.IDENTIFICATIVO = SUA.ID_UNITA_ARBOREA " +
        "               AND    NE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "               AND    NE.DATA_FINE_VALIDITA IS NULL) " +
        "         THEN 'NOTIFICA' " +
        "         END " +
        "        ) AS IN_NOTIFICA, " +
        "       NVL(CEL.PERCENTUALE_UTILIZZO, SUM(CD.PERCENTUALE_POSSESSO)) AS PERCENTUALE_UTILIZZO, " +
        "       NVL2(UAD.ID_MENZIONE_GEOGRAFICA, TMG.DESCRIZIONE , (SELECT     MG.DESCRIZIONE  " +
        "                                                           FROM   DB_R_MENZIONE_PARTICELLA MP, " +
        "                                                                  DB_TIPO_MENZIONE_GEOGRAFICA MG  " +
        "                                                           WHERE  MP.ID_PARTICELLA = SP.ID_PARTICELLA " + 
        "                                                           AND    MP.ID_MENZIONE_GEOGRAFICA = MG.ID_MENZIONE_GEOGRAFICA " +
        "                                                           AND    MG.ID_TIPOLOGIA_VINO = UAD.ID_TIPOLOGIA_VINO " +
        "                                                           AND    MP.DATA_FINE_VALIDITA IS NULL " +
        "                                                           AND    MG.DATA_FINE_VALIDITA IS NULL " +
        "                                                           AND    MP.DATA_INIZIO_VALIDITA < ? " +
        "                                                           AND    NVL(MP.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) > ? " +
        "                                                           AND    MG.DATA_INIZIO_VALIDITA < ? " +
        "                                                           AND    NVL(MG.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) > ? " +
        "                                                           AND ROWNUM = 1) " +
        "       ) AS DESC_MENZIONE_GEOGRAFICA " +
        "FROM   DB_STORICO_PARTICELLA SP, " +
        "       DB_STORICO_UNITA_ARBOREA SUA, " +
        "       DB_R_CATALOGO_MATRICE RCM, " +
        "       COMUNE C, " +
        "       PROVINCIA P, " +
        "       DB_UNITA_ARBOREA_DICHIARATA UAD, " +
        "       DB_CONDUZIONE_DICHIARATA CD, " +
        "       DB_TIPO_FORMA_ALLEVAMENTO TFA, " + 
        "       DB_TIPO_VARIETA TVAR, " +
        "       DB_TIPO_UTILIZZO TU, " +
        "       DB_PARTICELLA_CERTIFICATA PC, " +
        "       DB_DICHIARAZIONE_CONSISTENZA DC, " +
        "       DB_TIPO_TIPOLOGIA_VINO TTV, " +
        "       DB_TIPO_CAUSALE_MODIFICA TCM, " +
        "       START_RIESAME SRI, " +
        "       COND_ELEG CEL, " +
        "       DB_TIPO_MENZIONE_GEOGRAFICA TMG, " +
        "       DB_VIGNA VI ");
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        queryBuf.append(
        "       ,DB_DICHIARAZIONE_SEGNALAZIONE DDS ");
      }
      queryBuf.append(
        "WHERE  DC.ID_DICHIARAZIONE_CONSISTENZA = ? " +
        "AND    DC.CODICE_FOTOGRAFIA_TERRENI = UAD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    UAD.ID_STORICO_PARTICELLA = SP.ID_STORICO_PARTICELLA " +
        "AND    UAD.ID_STORICO_UNITA_ARBOREA = SUA.ID_STORICO_UNITA_ARBOREA " +
        "AND    SP.COMUNE = C.ISTAT_COMUNE " +
        "AND    C.ISTAT_PROVINCIA = P.ISTAT_PROVINCIA " +
        "AND    UAD.ID_FORMA_ALLEVAMENTO = TFA.ID_FORMA_ALLEVAMENTO(+) " +
        "AND    UAD.ID_CATALOGO_MATRICE = RCM.ID_CATALOGO_MATRICE (+) " +
        "AND    RCM.ID_VARIETA = TVAR.ID_VARIETA(+) " +
        "AND    RCM.ID_UTILIZZO = TU.ID_UTILIZZO(+) " +
        "AND    UAD.ID_TIPOLOGIA_VINO = TTV.ID_TIPOLOGIA_VINO(+) " +
        "AND    UAD.ID_CAUSALE_MODIFICA = TCM.ID_CAUSALE_MODIFICA(+) " +
        "AND    UAD.ID_AZIENDA = ? " +
        "AND    UAD.ID_STORICO_PARTICELLA = CD.ID_STORICO_PARTICELLA " + 
        "AND    DC.CODICE_FOTOGRAFIA_TERRENI = CD.CODICE_FOTOGRAFIA_TERRENI " +
        "AND    CD.ID_TITOLO_POSSESSO NOT IN (5,6) " +
        "AND    SP.COMUNE = PC.COMUNE(+) " +
        "AND    NVL(SP.SEZIONE, '-') = NVL(PC.SEZIONE(+), '-') " +
        "AND    SP.FOGLIO = PC.FOGLIO(+) " +
        "AND    SP.PARTICELLA = PC.PARTICELLA(+) " +
        "AND    NVL(SP.SUBALTERNO, '-') = NVL(PC.SUBALTERNO(+), '-') " +
        "AND    PC.DATA_INIZIO_VALIDITA(+) <= ? " +
        "AND    TRUNC(NVL(PC.DATA_FINE_VALIDITA(+), TO_DATE('31/12/9999', 'DD/MM/YYYY'))) > ? " +
        "AND    SP.ID_PARTICELLA = SRI.ID_PARTICELLA (+) " +
        "AND    SP.ID_PARTICELLA = CEL.ID_PARTICELLA (+) " +
        "AND    UAD.ID_MENZIONE_GEOGRAFICA = TMG.ID_MENZIONE_GEOGRAFICA (+) " +
        "AND    UAD.ID_VIGNA = VI.ID_VIGNA(+) ");
      
      // Se l'utente ha indicato il tipo di controllo
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo()))
      {
        queryBuf.append(
        "AND    UAD.ID_STORICO_UNITA_ARBOREA = DDS.ID_STORICO_UNITA_ARBOREA " +
        "AND    DDS.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA " +
        "AND    DDS.ID_CONTROLLO = ? ");
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
          "AND      UAD.ID_TIPOLOGIA_VINO IS NULL ");
        }
        else
        {
          queryBuf.append(
          "AND     UAD.ID_TIPOLOGIA_VINO = ? ");
        }
      }
      // Se l'utente ha indicato il genere iscrizione
      if(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione() != null) 
      {
        queryBuf.append(
        "AND    UAD.ID_GENERE_ISCRIZIONE = ? ");
      }
    //Notifiche
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica())
          || (Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica())))
      {
        queryBuf.append(
            " AND  EXISTS  (SELECT NE.ID_NOTIFICA_ENTITA " + 
            "               FROM   DB_NOTIFICA_ENTITA NE, " +
            "                      DB_NOTIFICA NO, " +
            "                      DB_TIPO_ENTITA TE " +
            "               WHERE  TE.CODICE_TIPO_ENTITA = 'U' " +
            "               AND    TE.ID_TIPO_ENTITA = NE.ID_TIPO_ENTITA " +
            "               AND    NE.IDENTIFICATIVO = SUA.ID_UNITA_ARBOREA " +
            "               AND    NE.ID_NOTIFICA = NO.ID_NOTIFICA " +
            "               AND    NE.ID_DICHIARAZIONE_CONSISTENZA = DC.ID_DICHIARAZIONE_CONSISTENZA ");
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
              "                                    AND    NE1.ID_NOTIFICA = NE.ID_NOTIFICA " +
              "                                    AND    NE1.ID_DICHIARAZIONE_CONSISTENZA = NE.ID_DICHIARAZIONE_CONSISTENZA)) )");
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
        "AND    UAD.ID_CAUSALE_MODIFICA = ? ");
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
        "AND (UAD.ESITO_CONTROLLO = ? ");
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
        "UAD.ESITO_CONTROLLO = ? ");
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
        "UAD.ESITO_CONTROLLO = ? ");
        isFirst = false;
      }
      if(!isFirst) 
      {
        queryBuf.append(
        ") ");
      } 
      
      queryBuf.append(
        "GROUP BY " +
        "       UAD.MATRICOLA_CCIAA, " +
        "       SUA.ID_UNITA_ARBOREA, " +
        "       DC.ID_DICHIARAZIONE_CONSISTENZA, " +
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
        "       SP.ID_PARTICELLA, " +
        "       PC.ID_PARTICELLA_CERTIFICATA, "+
        "       UAD.ID_STORICO_UNITA_ARBOREA, " +
        "       UAD.ID_CATALOGO_MATRICE, " +
        "       UAD.PROGR_UNAR, " +
        "       UAD.DATA_IMPIANTO, " +
        "       UAD.DATA_PRIMA_PRODUZIONE, " +
        "       UAD.AREA, " +
        "       UAD.ID_TIPOLOGIA_VINO, " +
        "       TTV.DESCRIZIONE, " +
        "       UAD.ANNO_ISCRIZIONE_ALBO, " +
        "       RCM.ID_VARIETA, " +
        "       TVAR.DESCRIZIONE, " +
        "       TVAR.CODICE_VARIETA, " +
        "       RCM.ID_UTILIZZO, " +
        "       TU.CODICE, " +
        "       TU.DESCRIZIONE, " +
        "       UAD.SESTO_SU_FILA, " +
        "       UAD.SESTO_TRA_FILE, " +
        "       UAD.NUM_CEPPI, " +
        "       UAD.ID_FORMA_ALLEVAMENTO, " +
        "       TFA.DESCRIZIONE, " +
        "       UAD.PERCENTUALE_VARIETA, " +
        "       UAD.PRESENZA_ALTRI_VITIGNI, " +
        "       UAD.DATA_FINE_VALIDITA, " +
        "       UAD.ID_CAUSALE_MODIFICA, " +
        "       TCM.DESCRIZIONE, " +
        "       UAD.VIGNA, " +
        "       UAD.PERCENTUALE_FALLANZA, " +
        "       UAD.FLAG_IMPRODUTTIVA, " +
        "       SRI.DATA_RICHIESTA, " +
        "       SRI.DATA_EVASIONE, " +
        "       VI.MENZIONE, " +
        "       CEL.PERCENTUALE_UTILIZZO, " +
        "       UAD.ID_MENZIONE_GEOGRAFICA, " +
        "       TMG.DESCRIZIONE " +
        "ORDER BY  " +
        "       C.DESCOM, " +
        "       SP.SEZIONE, " +
        "       SP.FOGLIO, " +
        "       SP.PARTICELLA, " +
        "       SP.SUBALTERNO, " +
        "       UAD.PROGR_UNAR ASC, " +
        "       TTV.DESCRIZIONE ASC");
      
      
      
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
                "[UnitaArboreaDichiarataGaaDAO::searchUnitaArboreaDichiarataExcelSempliceByParameters] Query="
                    + query);
      }
      
      stmt = conn.prepareStatement(query);
    
      // Setto i parametri della query
      int indice = 0;
      //Istanza di riesame
      stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
      stmt.setLong(++indice, idAzienda.longValue());
      
      //CONDUZIONE_ELEGGIBILE
      stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
      
      //menzione geografica
      stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
      stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
      stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
      stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
      
      stmt.setLong(++indice, filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue());
      stmt.setLong(++indice, idAzienda.longValue());
      stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
      stmt.setDate(++indice, new java.sql.Date(filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione().getTime()));
      //Se l'utente ha selezionato il tipo di controllo
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
        storicoParticellaArboreaExcelVO.setSuperficieCatastale(StringUtils.parseSuperficieField(rs.getString("SUP_CATASTALE")));
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
        if(Validator.isNotEmpty(rs.getString("ID_TIPOLOGIA_VINO"))) {
          storicoParticellaArboreaExcelVO.setDescTipologiaVino(rs.getString("DESC_TIPO_VINO"));
        }       
        storicoParticellaArboreaExcelVO.setAnnoIscrizioneAlbo(rs.getString("ANNO_ISCRIZIONE_ALBO"));
        if(Validator.isNotEmpty(rs.getString("ID_VARIETA"))) {
          storicoParticellaArboreaExcelVO.setVarieta("["+rs.getString("COD_VAR")+"] "+rs.getString("DESC_VARIETA"));
        }
        if(Validator.isNotEmpty(rs.getString("ID_UTILIZZO"))) {
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
        if(rs.getString("PRESENZA_ALTRI_VITIGNI").equalsIgnoreCase(SolmrConstants.FLAG_S)) 
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
        
        storicoParticellaArboreaExcelVO.setDescMenzioneGeografica(rs.getString("DESC_MENZIONE_GEOGRAFICA"));
        storicoParticellaArboreaExcelVO.setPercentualeUsoElegg(rs.getBigDecimal("PERCENTUALE_UTILIZZO"));
        
        result.add(storicoParticellaArboreaExcelVO);
        
      }
      
      /*rs.close();      
      stmt.close();
      
      if((result != null)  && (result.size() > 0))
      {
        Vector<Long> vIdTipologiaVino = getIdTipologiaVinoByResult(result);
        if(Validator.isNotEmpty(vIdTipologiaVino))
        {
          HashMap<Long,Vector<VignaVO>> hVigna = getHashVignaByIdTipologiaVinoAllaDichiarazione(vIdTipologiaVino, 
              filtriUnitaArboreaRicercaVO.getDataInserimentoDichiarazione(), conn);
          if(hVigna != null)
          {
            aggiungiRisultatiSecondaQuery(result, hVigna);
          }
        }
        
      }*/
      
      
      
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
          "[UnitaArboreaDichiarataGaaDAO::searchUnitaArboreaDichiarataExcelSempliceByParameters] ", t,
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
          "[UnitaArboreaDichiarataGaaDAO::searchUnitaArboreaDichiarataExcelSempliceByParameters] END.");
    }
  }
  
  
  
  /**
   * 
   * restituisce i dati di db_vigna relativamente
   * all'id_tipologia_vino e alla data di inserimento della dichiarazione 
   * 
   * @param vIdTipologiaVino
   * @param dataInserimentoDichiarazione
   * @param conn
   * @return
   * @throws DataAccessException
   */
  /*public HashMap<Long,Vector<VignaVO>> getHashVignaByIdTipologiaVinoAllaDichiarazione(Vector<Long> vIdTipologiaVino,
      Date dataInserimentoDichiarazione, Connection conn) 
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
            "[TipoTipologiaVinoDAO::getHashVignaByIdTipologiaVinoAllaDichiarazione] BEGIN.");
  
      
  
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
                "AND    DV.DATA_INIZIO_VALIDITA < ? " 
              + "AND    NVL(DV.DATA_FINE_VALIDITA, TO_DATE('31/12/9999', 'dd/mm/yyyy')) > ? " 
              + "ORDER BY "
              + "       DV.ID_TIPOLOGIA_VINO, DV.MENZIONE ");   
      
      
      
      query = queryBuf.toString();
      
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TipoTipologiaVinoDAO::getHashVignaByIdTipologiaVinoAllaDichiarazione] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
      
      int indice = 0;
      stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
      stmt.setTimestamp(++indice,convertDateToTimestamp(dataInserimentoDichiarazione));
  
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
      { new Parametro("vIdTipologiaVino", vIdTipologiaVino),
        new Parametro("dataInserimentoDichiarazione", dataInserimentoDichiarazione)};
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TipoTipologiaVinoDAO::getHashVignaByIdTipologiaVinoAllaDichiarazione] ",
              t, query, variabili, parametri);
      
      throw new DataAccessException(t.getMessage());
    }
    finally
    {
      
      close(rs, stmt, null);
  
      // Fine metodo
      SolmrLogger
          .debug(this,
              "[TipoTipologiaVinoDAO::getHashVignaByIdTipologiaVinoAllaDichiarazione] END.");
    }
  }*/
  
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

  
  

}
