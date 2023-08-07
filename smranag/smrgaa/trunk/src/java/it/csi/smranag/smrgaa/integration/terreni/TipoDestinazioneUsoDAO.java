package it.csi.smranag.smrgaa.integration.terreni;

import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class TipoDestinazioneUsoDAO extends it.csi.solmr.integration.BaseDAO
{

  public TipoDestinazioneUsoDAO() throws ResourceAccessException
  {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public TipoDestinazioneUsoDAO(String refName) throws ResourceAccessException
  {
    super(refName);
  }
  
  /**
   * 
   * Metodo usato in eliminaParticelle per trovare se sulla conduzione
   * esitono unita arboree attive
   * 
   * 
   * @param idConduzioneParticella
   * @param idAzienda
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoDettaglioUsoVO> getListDettaglioUsoByMatrice(
      long idUtilizzo, long idTipoDestinazione)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = null;
    TipoDettaglioUsoVO tipoDettaglioUsoVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[TipoDestinazioneUsoDAO::getListDettaglioUsoByMatrice] BEGIN.");

      query = " " +
          "SELECT DISTINCT " +
          "       TD.ID_TIPO_DETTAGLIO_USO," +
          "       TD.DESCRIZIONE_DETTAGLIO_USO, " +
          "       TD.CODICE_DETTAGLIO_USO," +
          "       TD.ORDINAMENTO " +
          "FROM   DB_R_CATALOGO_MATRICE RCM, " +
          "       DB_TIPO_DETTAGLIO_USO TD " +
          "WHERE  RCM.ID_UTILIZZO = ? " +
          "AND    RCM.ID_TIPO_DESTINAZIONE = ? " +
          "AND    RCM.ID_TIPO_DETTAGLIO_USO = TD.ID_TIPO_DETTAGLIO_USO  " +
          "AND    RCM.DATA_FINE_VALIDITA IS NULL " +
          "ORDER BY TD.ORDINAMENTO, TD.DESCRIZIONE_DETTAGLIO_USO ";
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      int idx = 0;
      // Setto i parametri della query
      stmt.setLong(++idx, idUtilizzo);
      stmt.setLong(++idx, idTipoDestinazione);
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      while (rs.next())
      {
        if(vTipoDettaglioUso == null)
        {
          vTipoDettaglioUso = new Vector<TipoDettaglioUsoVO>();
        }

        tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
        
        tipoDettaglioUsoVO.setIdTipoDettaglioUso(rs.getLong("ID_TIPO_DETTAGLIO_USO"));
        tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("CODICE_DETTAGLIO_USO"));
        tipoDettaglioUsoVO.setDescrizione(rs.getString("DESCRIZIONE_DETTAGLIO_USO"));
        tipoDettaglioUsoVO.setIdTipoDestinazione(idTipoDestinazione);
        
        vTipoDettaglioUso.add(tipoDettaglioUsoVO);
      }
      
      return vTipoDettaglioUso;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("tipoDettaglioUsoVO", tipoDettaglioUsoVO),
          new Variabile("vTipoDettaglioUso", vTipoDettaglioUso) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idUtilizzo", idUtilizzo),
          new Parametro("idTipoDestinazione", idTipoDestinazione)  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[TipoDestinazioneUsoDAO::getListDettaglioUsoByMatrice] ", t,
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
          "[TipoDestinazioneUsoDAO::getListDettaglioUsoByMatrice] END.");
    }
  }
  
  
  public TipoDettaglioUsoVO findDettaglioUsoByPrimaryKey(
      long idTipoDettaglioUso)
      throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    TipoDettaglioUsoVO tipoDettaglioUsoVO = null;
    
    try
    {
      SolmrLogger
          .debug(this,
              "[TipoDestinazioneUsoDAO::findDettaglioUsoByPrimaryKey] BEGIN.");

      query = " " +
          "SELECT TDU.ID_TIPO_DETTAGLIO_USO, " +
          "       TDU.CODICE_DETTAGLIO_USO, " +
          "       TDU.DESCRIZIONE_DETTAGLIO_USO, " +
          "       TDU.FLAG_NON_AMMISSIBILE_EFA, " +
          "       TDU.FLAG_PRATO_PERMANENTE, " +
          "       TDU.FLAG_COLTURA_SOMMERSA, " +
          "       TDU.FLAG_RIPOSO, " +
          "       TDU.FLAG_PRATO_FORAGGERA, " +
          "       TDU.FLAG_LEGUMINOSA, " +
          "       TDU.FLAG_PROTEAGINOSA, " +
          "       TDU.FLAG_BIOLOGICO, " +
          "       TDU.FLAG_SEMINATIVO " + 
          "FROM   DB_TIPO_DETTAGLIO_USO TDU " +
          "WHERE  TDU.ID_TIPO_DETTAGLIO_USO = ? ";
      
      
      conn = getDatasource().getConnection();
      stmt = conn.prepareStatement(query);

      // Setto i parametri della query
      stmt.setLong(1, idTipoDettaglioUso);
      
      
      ResultSet rs = stmt.executeQuery();
      
      
      
      if(rs.next())
      {

        tipoDettaglioUsoVO = new TipoDettaglioUsoVO();
        
        tipoDettaglioUsoVO.setIdTipoDettaglioUso(rs.getLong("ID_TIPO_DETTAGLIO_USO"));
        tipoDettaglioUsoVO.setCodiceDettaglioUso(rs.getString("CODICE_DETTAGLIO_USO"));
        tipoDettaglioUsoVO.setDescrizione(rs.getString("DESCRIZIONE_DETTAGLIO_USO"));
        //tipoDettaglioUsoVO.setIdVarieta(rs.getLong("ID_VARIETA"));
        tipoDettaglioUsoVO.setFlagNonAmmissibileEfa(rs.getString("FLAG_NON_AMMISSIBILE_EFA"));
        tipoDettaglioUsoVO.setFlagPratoPermanente(rs.getString("FLAG_PRATO_PERMANENTE"));
        tipoDettaglioUsoVO.setFlagColturaSommersa(rs.getString("FLAG_COLTURA_SOMMERSA"));
        tipoDettaglioUsoVO.setFlagRiposo(rs.getString("FLAG_RIPOSO"));
        tipoDettaglioUsoVO.setFlagPratoForaggera(rs.getString("FLAG_PRATO_FORAGGERA"));
        tipoDettaglioUsoVO.setFlagLeguminosa(rs.getString("FLAG_LEGUMINOSA"));
        tipoDettaglioUsoVO.setFlagProteaginosa(rs.getString("FLAG_PROTEAGINOSA"));
        tipoDettaglioUsoVO.setFlagBiologico(rs.getString("FLAG_BIOLOGICO"));
        tipoDettaglioUsoVO.setFlagSeminativo(rs.getString("FLAG_SEMINATIVO"));
        
      }
      
      return tipoDettaglioUsoVO;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query),
          new Variabile("tipoDettaglioUsoVO", tipoDettaglioUsoVO) };

      // Vettore di parametri passati al metodo
      Parametro parametri[] = new Parametro[]
      { new Parametro("idTipoDettaglioUso", idTipoDettaglioUso)  };

      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils.logDAOError(this,
          "[TipoDestinazioneUsoDAO::findDettaglioUsoByPrimaryKey] ", t,
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
          "[TipoDestinazioneUsoDAO::findDettaglioUsoByPrimaryKey] END.");
    }
  }
  
  
  
  

}
