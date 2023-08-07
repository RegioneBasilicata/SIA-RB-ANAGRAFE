package it.csi.solmr.integration.anag;

import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.anag.DettaglioTerreniVO;
import it.csi.solmr.dto.anag.ParticelleVO;
import it.csi.solmr.dto.anag.TerreniVO;
import it.csi.solmr.dto.anag.terreni.TipoSettoreAbacoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.SolmrErrors;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

public class TerreniDAO extends it.csi.solmr.integration.BaseDAO{
  public TerreniDAO() throws ResourceAccessException {
    super(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
  }

  public TerreniDAO(String refName) throws ResourceAccessException {
    super(refName);
  }

  // Restituisce l'elenco (code description) delle unità produttive legate all'azienda
  public Vector<CodeDescription> getUnitaProduttive(Long idAzienda)throws DataAccessException, SolmrException{
    Vector<CodeDescription> result = null;
    CodeDescription cd = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();
      String query = "select u.id_ute, c.descom "+
                     "from db_ute u, comune c "+
                     "where u.id_azienda = ? "+
                     "and c.istat_comune = u.comune";
      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if(rs!=null){
        result = new Vector<CodeDescription>();
        while (rs.next()) {
          cd = new CodeDescription();
          cd.setCode(new Integer(rs.getInt(1)));
          cd.setDescription(rs.getString(2));
          result.add(cd);
        }
      }
      else
        throw new DataAccessException();

      if(result.size()==0)
        throw new SolmrException(AnagErrors.RICERCA_TERRENI_UTE);

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getUnitaProduttive - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getUnitaProduttive - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "getUnitaProduttive - ResultSet null");
      throw daexc;
    }

    catch (SolmrException sex) {
     SolmrLogger.error(this, "getUnitaProduttive - SolmrException");
     throw sex;
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getUnitaProduttive - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getUnitaProduttive - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getUnitaProduttive - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Restituisce l'elenco degli anni presenti in archivio
  public Vector<Long> getAnniRilevamento()throws DataAccessException, SolmrException{
    Vector<Long> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();
      String query = "select distinct anno_rilevazione "+
                     "from db_particella_import "+
                     "order by anno_rilevazione desc";
      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: "+query);

      //stmt.setLong(1, idAzienda.longValue());

      ResultSet rs = stmt.executeQuery();

      if(rs!=null){
        result = new Vector<Long>();
        while (rs.next()) {
          result.add(new Long(rs.getLong(1)));
        }
      }

      else
        throw new DataAccessException();

      if(result.size()==0)
        throw new SolmrException(AnagErrors.RICERCA_ANNI_TERRENI);

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getAnniRilevamento - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getAnniRilevamento - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "getAnniRilevamento - ResultSet null");
      throw daexc;
    }
    catch (SolmrException sex) {
      SolmrLogger.error(this, "getAnniRilevamento - No data found");
      throw sex;
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getAnniRilevamento - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getAnniRilevamento - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }

      catch (Exception ex) {
        SolmrLogger.fatal(this, "getAnniRilevamento - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // restituisce il totale delle superfici (totale e utilizzata) di un'unità produttiva
  public TerreniVO getTotaleSuperfici(Long idAzienda, Long idUte, Long anno) throws DataAccessException{
    TerreniVO terreniVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();
      String query1 = "SELECT nvl(sum(pi.superficie_utilizzata),0) SAU "+
                      "FROM db_particella_import pi, "+
                      "db_tipo_utilizzo_import tui, "+
                      "db_ute u "+
                      "WHERE pi.id_utilizzo_import = tui.id_utilizzo_import and "+
                      "pi.id_ute = u.id_ute and "+
                      "pi.anno_rilevazione = ? and "+
                      "u.id_azienda = ? ";

      String query2 = "SELECT nvl(sum(p.sup_catastale), 0) TOTALE "+
                      "FROM db_particella p, "+
                      "db_particella_import pi, "+
                      "db_ute u "+
                      "WHERE pi.ID_PARTICELLA = p.ID_PARTICELLA and "+
                      "pi.id_ute = u.id_ute and "+
                      "pi.anno_rilevazione = ? and "+
                      "u.id_azienda = ? ";
      if(idUte!=null){
        query1+="and u.id_ute = ? ";
        query2+="and u.id_ute = ? ";
      }
      query1+="and tui.flag_sau= '"+SolmrConstants.FLAG_S+"'";
      stmt = conn.prepareStatement(query1);
      SolmrLogger.debug(this, "Executing query: "+query1);

      stmt.setLong(1, anno.longValue());
      stmt.setLong(2, idAzienda.longValue());
      if(idUte!=null){
        stmt.setLong(3, idUte.longValue());
      }

      ResultSet rs1 = stmt.executeQuery();
      if(rs1!=null){
        while (rs1.next()) {
          terreniVO = new TerreniVO();
          terreniVO.setSommaSAU((new Double(rs1.getDouble(1))));
        }
      }
      else
        throw new DataAccessException();

      rs1.close();
      stmt.close();

      stmt = conn.prepareStatement(query2);
      SolmrLogger.debug(this, "Executing query: "+query2);

      stmt.setLong(1, anno.longValue());
      stmt.setLong(2, idAzienda.longValue());
      if(idUte!=null)
        stmt.setLong(3, idUte.longValue());
      ResultSet rs2 = stmt.executeQuery();
      if(rs2!=null){
        while (rs2.next()) {
          terreniVO.setSommaSuperficieTotale(new Double(rs2.getDouble(1)));
        }
      }
      else
        throw new DataAccessException();

      SolmrLogger.debug(this, "getTotaleSuperfici - Found record(s)");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getTotaleSuperfici - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "getTotaleSuperfici - ResultSet null");
      throw daexc;
    }

    catch (Exception ex) {
      SolmrLogger.fatal(this, "getTotaleSuperfici - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getTotaleSuperfici - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getTotaleSuperfici - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return terreniVO;
  }

  public Vector<DettaglioTerreniVO> getTerreniGroupByComune(Long idAzienda, Long idUte, Long anno)throws DataAccessException, SolmrException{
    Vector<DettaglioTerreniVO> result = null;
    DettaglioTerreniVO dtVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    HashMap<String,DettaglioTerreniVO> hm = null;
    try {
      conn = getDatasource().getConnection();
      String query1 = "SELECT c.istat_comune, "+
                      "c.descom, "+
                      "pr.sigla_provincia, "+
                      "nvl(sum(pi.superficie_utilizzata), 0) sau "+
                      "FROM db_particella p, "+
                      "db_particella_import pi, "+
                      "db_tipo_utilizzo_import tui, "+
                      "comune c, "+
                      "db_ute u, "+
                      "provincia pr "+
                      "WHERE pi.id_utilizzo_import =tui.id_utilizzo_import and "+
                      "tui.flag_sau = '"+SolmrConstants.FLAG_S+"' and "+
                      "pi.anno_rilevazione = ? and "+
                      "pi.id_particella = p.id_particella and "+
                      "p.comune = c.istat_comune and "+
                      "c.istat_provincia = pr.istat_provincia and "+
                      "pi.id_ute = u.id_ute and "+
                      "u.id_azienda = ? ";

      String query2 = "SELECT c.istat_comune, "+
                      "c.descom, "+
                      "pr.sigla_provincia, "+
                      "nvl(sum(p.sup_catastale), 0) sup_totale "+
                      "FROM db_particella p, "+
                      "db_particella_import pi, "+
                      "comune c, "+
                      "db_ute u, "+
                      "provincia pr "+
                      "WHERE pi.anno_rilevazione = ? and "+
                      "pi.id_particella = p.id_particella and "+
                      "p.comune = c.istat_comune and "+
                      "c.istat_provincia = pr.istat_provincia and "+
                      "pi.id_ute = u.id_ute and "+
                      "u.id_azienda = ? ";

      if(idUte!=null){
        query1+=      "and pi.id_ute = ? ";
        query2+=      "and pi.id_ute = ? ";
      }
      query1+=        "group by c.istat_comune, c.descom, pr.sigla_provincia";
      query2+=        "group by c.istat_comune, c.descom, pr.sigla_provincia";

      stmt = conn.prepareStatement(query1);
      SolmrLogger.debug(this, "Executing query: "+query1);

      stmt.setLong(1, anno.longValue());
      stmt.setLong(2, idAzienda.longValue());
      if(idUte!=null){
        stmt.setLong(3, idUte.longValue());
      }
      ResultSet rs1 = stmt.executeQuery();

      if(rs1!=null){
        result = new Vector<DettaglioTerreniVO>();
        hm = new HashMap<String,DettaglioTerreniVO>();
        while (rs1.next()) {
          dtVO = new DettaglioTerreniVO();
          dtVO.setIdComune(rs1.getString(1));
          dtVO.setDescComune(rs1.getString(2));
          dtVO.setDescProvincia(rs1.getString(3));
          dtVO.setSAU(new Double(rs1.getDouble(4)));
          result.add(dtVO);
          hm.put(dtVO.getIdComune(),dtVO);
        }
      }
      else
        throw new DataAccessException();

      if(result.size()==0)
        throw new SolmrException(AnagErrors.RICERCA_TERRENI);

      rs1.close();
      stmt.close();

      stmt = conn.prepareStatement(query2);
      SolmrLogger.debug(this, "Executing query: "+query2);

      stmt.setLong(1, anno.longValue());
      stmt.setLong(2, idAzienda.longValue());
      if(idUte!=null){
        stmt.setLong(3, idUte.longValue());
      }
      ResultSet rs2 = stmt.executeQuery();

      if(rs2!=null){
        while (rs2.next()) {
          String idComune = rs2.getString(1);
          dtVO = (DettaglioTerreniVO)hm.get(idComune);
          if(dtVO==null){
            dtVO = new DettaglioTerreniVO();
            dtVO.setIdComune(rs2.getString(1));
            dtVO.setDescComune(rs2.getString(2));
            dtVO.setDescProvincia(rs2.getString(3));
            result.add(dtVO);
            hm.put(dtVO.getIdComune(),dtVO);
          }
          dtVO.setSupTotale(new Double(rs2.getDouble(4)));
        }
      }
      else
        throw new DataAccessException();

      SolmrLogger.debug(this, "getTerreniGroupByComune - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getTerreniGroupByComune - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "getTerreniGroupByComune - ResultSet null");
      throw daexc;
    }
    catch (SolmrException sex){
      SolmrLogger.fatal(this, "getTerreniGroupByComune - SolmrException");
      throw sex;
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getTerreniGroupByComune - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getTerreniGroupByComune - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getTerreniGroupByComune - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<DettaglioTerreniVO> getTerreniGroupByConduzione(Long idAzienda, Long idUte, Long anno)throws DataAccessException, SolmrException{
    Vector<DettaglioTerreniVO> result = null;
    DettaglioTerreniVO dtVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    HashMap<Long,DettaglioTerreniVO> hm = null;
    try {
      conn = getDatasource().getConnection();
      String query1 = " SELECT tfc.id_forma_conduzione, "+
                      " tfc.descrizione, "+
                      "nvl(sum(pi.superficie_utilizzata), 0) sau "+
                      "FROM db_particella p, "+
                      "db_particella_import pi, "+"db_tipo_utilizzo_import tui, "+
                      "db_tipo_forma_conduzione tfc, "+
                      "db_ute u "+
                      "WHERE pi.id_utilizzo_import =tui.id_utilizzo_import and "+
                      "tui.flag_sau = '"+SolmrConstants.FLAG_S+"' and "+
                      "pi.anno_rilevazione = ? and "+
                      "pi.id_particella = p.id_particella and "+
                      "pi.id_ute = u.id_ute and "+
                      "u.id_azienda = ? and "+
                      "pi.id_forma_conduzione = tfc.id_forma_conduzione ";


      String query2 = "SELECT tfc.id_forma_conduzione, "+
                      "tfc.descrizione, "+
                      "nvl(sum(p.sup_catastale),0) sup_totale "+
                      "FROM db_particella p, "+
                      "db_particella_import pi, "+
                      "db_tipo_forma_conduzione tfc, "+
                      "db_ute u "+
                      "WHERE pi.anno_rilevazione = ? and "+
                      "pi.id_particella = p.id_particella and "+
                      "pi.id_ute = u.id_ute and "+
                      "u.id_azienda = ? and "+
                      "pi.id_forma_conduzione = tfc.id_forma_conduzione ";

      if(idUte!=null){
        query1+=       "and pi.id_ute = ? ";
        query2+=       "and pi.id_ute = ? ";
      }
      query1+=        "group by tfc.id_forma_conduzione, tfc.descrizione";
      query2+=        "group by tfc.id_forma_conduzione, tfc.descrizione";


      stmt = conn.prepareStatement(query1);
      SolmrLogger.debug(this, "Executing query: "+query1);

      stmt.setLong(1, anno.longValue());
      stmt.setLong(2, idAzienda.longValue());
      if(idUte!=null){
        stmt.setLong(3, idUte.longValue());
      }
      ResultSet rs1 = stmt.executeQuery();

      if(rs1!=null){
        result = new Vector<DettaglioTerreniVO>();
        hm = new HashMap<Long,DettaglioTerreniVO>();
        while (rs1.next()) {
          dtVO = new DettaglioTerreniVO();
          dtVO.setIdTipoConduzione(new Long(rs1.getLong(1)));
          dtVO.setDescTipoConduzione(rs1.getString(2));
          dtVO.setSAU(new Double(rs1.getDouble(3)));
          result.add(dtVO);
          hm.put(dtVO.getIdTipoConduzione(),dtVO);
        }
      }
      else
        throw new DataAccessException();

      if(result.size()==0)
        throw new SolmrException(AnagErrors.RICERCA_TERRENI);

      rs1.close();
      stmt.close();

      stmt = conn.prepareStatement(query2);
      SolmrLogger.debug(this, "Executing query: "+query2);

      stmt.setLong(1, anno.longValue());
      stmt.setLong(2, idAzienda.longValue());
      if(idUte!=null){
        stmt.setLong(3, idUte.longValue());
      }
      ResultSet rs2 = stmt.executeQuery();

      if(rs2!=null){
        while (rs2.next()) {
          Long idTipoConduzione = new Long(rs2.getLong(1));
          dtVO = (DettaglioTerreniVO)hm.get(idTipoConduzione);
          if(dtVO==null){
            dtVO = new DettaglioTerreniVO();
            dtVO.setIdTipoConduzione(new Long(rs2.getLong(1)));
            dtVO.setDescTipoConduzione(rs2.getString(2));
            dtVO.setSupTotale(new Double(rs2.getDouble(3)));
            result.add(dtVO);
            hm.put(dtVO.getIdTipoConduzione(),dtVO);
          }
          dtVO.setSupTotale(new Double(rs2.getDouble(3)));
        }
      }
      else
        throw new DataAccessException();

      rs2.close();
      stmt.close();

      SolmrLogger.debug(this, "getTerreniGroupByConduzione - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getTerreniGroupByConduzione - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "getTerreniGroupByConduzione - ResultSet null");
      throw daexc;
    }
    catch (SolmrException sex) {
     SolmrLogger.error(this, "getTerreniGroupByConduzione - No data found");
     throw sex;
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getTerreniGroupByConduzione - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getTerreniGroupByConduzione - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getTerreniGroupByConduzione - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<DettaglioTerreniVO> getTerreniGroupByUtilizzo(Long idAzienda, Long idUte, Long anno)throws DataAccessException, SolmrException{
    Vector<DettaglioTerreniVO> result = null;
    DettaglioTerreniVO dtVO = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    HashMap<Long,DettaglioTerreniVO> hm = null;
    try {
      conn = getDatasource().getConnection();
      String query1 = "SELECT tui.id_utilizzo_import, "+
                      "tui.descrizione, "+
                      "nvl(sum(pi.superficie_utilizzata),0) sau "+
                      "FROM db_particella p, "+
                      "db_particella_import pi, "+
                      "db_tipo_utilizzo_import tui, "+
                      "db_ute u "+
                      "WHERE pi.id_utilizzo_import = tui.id_utilizzo_import and "+
                      "tui.flag_sau = '"+SolmrConstants.FLAG_S+"' and "+
                      "pi.anno_rilevazione = ? and "+
                      "pi.id_particella = p.id_particella and "+
                      "pi.id_ute = u.id_ute and "+
                      "u.id_azienda = ? ";

      String query2 = "SELECT tui.id_utilizzo_import, "+
                      "tui.descrizione, "+
                      "nvl(sum(p.sup_catastale),0) sup_totale "+
                      "FROM db_particella p, "+
                      "db_particella_import pi, "+
                      "db_tipo_utilizzo_import tui, "+
                      "db_ute u "+
                      "WHERE pi.id_utilizzo_import = tui.id_utilizzo_import and "+
                      "pi.anno_rilevazione = ? and "+
                      "pi.id_particella = p.id_particella and "+
                      "pi.id_ute = u.id_ute and "+
                      "u.id_azienda = ? ";

      if(idUte!=null){
        query1+=       "and pi.id_ute = ? ";
        query2+=       "and pi.id_ute = ? ";
      }
      query1+=        "GROUP BY tui.id_utilizzo_import, tui.descrizione";
      query2+=        "GROUP BY tui.id_utilizzo_import, tui.descrizione";

      stmt = conn.prepareStatement(query1);
      SolmrLogger.debug(this, "Executing query: "+query1);

      stmt.setLong(1, anno.longValue());
      stmt.setLong(2, idAzienda.longValue());
      if(idUte!=null){
        stmt.setLong(3, idUte.longValue());
      }
      ResultSet rs1 = stmt.executeQuery();

      if(rs1!=null){
        result = new Vector<DettaglioTerreniVO>();
        hm = new HashMap<Long,DettaglioTerreniVO>();
        while (rs1.next()) {
          dtVO = new DettaglioTerreniVO();
          dtVO.setIdTipoUtilizzo(new Long(rs1.getLong(1)));
          dtVO.setDescUtilizzo(rs1.getString(2));
          dtVO.setSAU(new Double(rs1.getDouble(3)));
          result.add(dtVO);
          hm.put(dtVO.getIdTipoUtilizzo(),dtVO);
        }
      }
      else
        throw new DataAccessException();

      if(result.size()==0)
        throw new SolmrException(AnagErrors.RICERCA_TERRENI);

      rs1.close();
      stmt.close();

      stmt = conn.prepareStatement(query2);
      SolmrLogger.debug(this, "Executing query: "+query2);

      stmt.setLong(1, anno.longValue());
      stmt.setLong(2, idAzienda.longValue());
      if(idUte!=null){
        stmt.setLong(3, idUte.longValue());
      }
      ResultSet rs2 = stmt.executeQuery();

      if(rs2!=null){
        while (rs2.next()) {
          Long idTipoUtilizzo = new Long(rs2.getLong(1));
          dtVO = (DettaglioTerreniVO)hm.get(idTipoUtilizzo);
          if(dtVO==null){
            dtVO = new DettaglioTerreniVO();
            dtVO.setIdTipoUtilizzo(new Long(rs2.getLong(1)));
            dtVO.setDescUtilizzo(rs2.getString(2));
            dtVO.setSupTotale(new Double(rs2.getDouble(3)));
            result.add(dtVO);
            hm.put(dtVO.getIdTipoUtilizzo(),dtVO);
          }
          dtVO.setSupTotale(new Double(rs2.getDouble(3)));
        }
      }
      else
        throw new DataAccessException();

      rs2.close();
      stmt.close();

      SolmrLogger.debug(this, "getTerreniGroupByUtilizzo - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getTerreniGroupByUtilizzo - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "getTerreniGroupByUtilizzo - ResultSet null");
      throw daexc;
    }
    catch (SolmrException sex) {
     SolmrLogger.error(this, "getTerreniGroupByUtilizzo - No data found");
     throw sex;
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getTerreniGroupByUtilizzo - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getTerreniGroupByUtilizzo - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getTerreniGroupByUtilizzo - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  // Recupero gli IdParticella per l'impaginazione...
  public Vector<Vector<Long>> getIdParticelle(Long idAzienda, Long idUte, Long anno, String criterio, Long valore)throws DataAccessException, SolmrException{
    Vector<Vector<Long>> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDatasource().getConnection();
      String query = "select p.id_particella, pi.id_particella_import "+
                     "from provincia pr, comune c, db_particella p, "+
                     "db_particella_import pi, db_tipo_forma_conduzione tfc, "+
                     "db_tipo_utilizzo_import tui, db_tipo_zona_altimetrica za, "+
                     "db_tipo_area ta, db_tipo_caso_particolare cp, db_ute u "+
                     "where pr.istat_provincia = c.istat_provincia "+
                     "and c.istat_comune = p.comune "+
                     "and p.id_particella = pi.id_particella "+
                     "and tfc.id_forma_conduzione = pi.id_forma_conduzione "+
                     "and tui.id_utilizzo_import = pi.id_utilizzo_import "+
                     "and za.id_zona_altimetrica = p.id_zona_altimetrica "+
                     "and ta.id_area(+) = p.id_area "+
                     "and u.id_ute = pi.id_ute "+
                     "and u.id_azienda = ? "+
                     "and pi.anno_rilevazione = ? "+
                     "and cp.id_caso_particolare(+) = p.id_caso_particolare ";
      if(criterio.equals(SolmrConstants.RICERCA_TERRENI_PER_COMUNE))
                     query+="and p.comune = ? ";
      else if(criterio.equals(SolmrConstants.RICERCA_TERRENI_PER_TIPO_CONDUZIONE))
        query+="and pi.id_forma_conduzione = ? ";
      else if(criterio.equals(SolmrConstants.RICERCA_TERRENI_PER_UTILIZZO))
        query+="and pi.id_utilizzo_import = ? ";
      if(idUte!=null)
        query+="and pi.id_ute = ? ";

      query += " order by pr.descrizione, c.descom, p.sezione, p.foglio, p.particella, p.subalterno ";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: "+query);

      stmt.setLong(1, idAzienda.longValue());
      stmt.setLong(2, anno.longValue());
      stmt.setLong(3, valore.longValue());
      if(idUte!=null)
        stmt.setLong(4, idUte.longValue());

      ResultSet rs = stmt.executeQuery();

      if(rs!=null){
        if(rs.getRow()>SolmrConstants.NUM_MAX_ROWS_RESULT)
          throw new SolmrException(SolmrErrors.EXC_TROPPI_RECORD_SELEZIONATI);
        result = new Vector<Vector<Long>>();
        while (rs.next()) {
          //result.add(new Long(rs.getLong(1)));
          Vector<Long> v = new Vector<Long>();
          v.addElement(new Long(rs.getLong(1)));
          v.addElement(new Long(rs.getLong(2)));
          result.addElement(v);
        }
        if(result.size()==0)
          throw new SolmrException(SolmrErrors.EXC_NOT_FOUND);
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getIdParticelle - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getIdParticelle - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "getIdParticelle - ResultSet null");
      throw daexc;
    }
    catch (SolmrException sex) {
      SolmrLogger.error(this, "getIdParticelle - SolmrException");
      throw sex;
    }
    catch (Exception ex) {
      SolmrLogger.fatal(this, "getIdParticelle - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getIdParticelle - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getIdParticelle - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }

  public Vector<ParticelleVO> getParticelleByIdRange(Vector<Vector<Long>> idRange)throws DataAccessException, SolmrException{
    Vector<ParticelleVO> result = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    ParticelleVO partVO = null;
    String inDoubleCondition = "";
    String inCondition = "";
    try {
      conn = getDatasource().getConnection();
      
      for(int i=0;i<idRange.size();i++){
        if(!inDoubleCondition.equals("") && !inDoubleCondition.endsWith(","))
          inDoubleCondition+=",";
        Vector<Long> subVector = (Vector<Long>)idRange.elementAt(i);
        inDoubleCondition += "("+((Long)subVector.elementAt(0)).toString()+","+((Long)subVector.elementAt(1)).toString()+")";
      }
      if(inDoubleCondition.length()>0 && inDoubleCondition.charAt(inDoubleCondition.length()-1)==',')
        inDoubleCondition = inDoubleCondition.substring(0,inDoubleCondition.length()-2)+")";
      if(!inDoubleCondition.equals(""))
        inDoubleCondition = "("+inDoubleCondition+")";
      if(!inDoubleCondition.equals(""))
        inCondition = " and (p.id_particella, pi.id_particella_import)in "+inDoubleCondition;

      String query = "select pr.sigla_provincia provincia, c.descom comune, "+
                     "p.sezione, p.foglio, p.id_particella, pi.id_particella_import, "+
                     "p.particella, p.subalterno, p.sup_catastale, "+
                     "pi.superficie_utilizzata, pi.flag_biologico, "+
                     "tfc.descrizione titolo_uso, tui.descrizione tipo_utilizzo, "+
                     "pi.denominazione_proprietario proprietario, "+
                     "pi.codice_fiscale_proprietario, za.descrizione zona_alt, "+
                     "ta.descrizione area , cp.descrizione caso_particolare "+
                     "from provincia pr, comune c, db_particella p, "+
                     "db_particella_import pi, db_tipo_forma_conduzione tfc, "+
                     "db_tipo_utilizzo_import tui, db_tipo_zona_altimetrica za, "+
                     "db_tipo_area ta, db_tipo_caso_particolare cp "+
                     "where pr.istat_provincia = c.istat_provincia "+
                     "and c.istat_comune = p.comune "+
                     "and p.id_particella = pi.id_particella "+
                     "and tfc.id_forma_conduzione = pi.id_forma_conduzione "+
                     "and tui.id_utilizzo_import = pi.id_utilizzo_import "+
                     "and za.id_zona_altimetrica = p.id_zona_altimetrica "+
                     "and ta.id_area(+) = p.id_area "+
                     "and cp.id_caso_particolare(+) = p.id_caso_particolare ";
      query+=inCondition+" order by pr.descrizione, c.descom, "+
                     "p.sezione, p.foglio, p.particella, p.subalterno";

      stmt = conn.prepareStatement(query);
      SolmrLogger.debug(this, "Executing query: "+query);

      ResultSet rs = stmt.executeQuery();

      if(rs!=null){
        result = new Vector<ParticelleVO>();
        while (rs.next()) {
          partVO = new ParticelleVO();
          partVO.setDescProvincia(rs.getString(1));
          partVO.setDescComune(rs.getString(2));
          partVO.setSezione(rs.getString(3));
          partVO.setFoglio(new Long(rs.getLong(4)));
          partVO.setIdParticella(new Long(rs.getLong(5)));
          partVO.setIdParticellaImport(new Long(rs.getLong(6)));
          partVO.setParticella(new Long(rs.getLong(7)));
          partVO.setSubalterno(rs.getString(8));
          partVO.setSupCatastale(new Double(rs.getDouble(9)));
          partVO.setSupUtilizzata(new Double(rs.getDouble(10)));
          partVO.setFlagBiologico(rs.getString(11));
          partVO.setDescTipoConduzione(rs.getString(12));
          partVO.setUtilizzo(rs.getString(13));
          partVO.setDenominazioneProprietario(rs.getString(14));
          partVO.setCodFiscaleProprietario(rs.getString(15));
          partVO.setZonaAltimetrica(rs.getString(16));
          partVO.setTipoArea(rs.getString(17));
          partVO.setCasiParticolari(rs.getString(18));
          result.add(partVO);
        }
      }
      else
        throw new DataAccessException();

      rs.close();
      stmt.close();

      SolmrLogger.debug(this, "getParticelleByIdRange - Found "+result.size()+" record(s)");
    }
    catch(SQLException exc) {
      SolmrLogger.fatal(this, "getParticelleByIdRange - SQLException: "+exc.getMessage());
      throw new DataAccessException(exc.getMessage());
    }
    catch (DataAccessException daexc) {
      SolmrLogger.fatal(this, "getParticelleByIdRange - ResultSet null");
      throw daexc;
    }

    catch (Exception ex) {
      SolmrLogger.fatal(this, "getParticelleByIdRange - Generic Exception: "+ex.getMessage());
      throw new DataAccessException(ex.getMessage());
    }
    finally {
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      }
      catch (SQLException exc) {
        SolmrLogger.fatal(this, "getParticelleByIdRange - SQLException while closing Statement and Connection: "+exc.getMessage());
        throw new DataAccessException(exc.getMessage());
      }
      catch (Exception ex) {
        SolmrLogger.fatal(this, "getParticelleByIdRange - Generic Exception while closing Statement and Connection: "+ex.getMessage());
        throw new DataAccessException(ex.getMessage());
      }
    }
    return result;
  }
  
  /**
   * 
   * Restitusce i record sdella tabella DB_TIPO_SETTORE_ABACO
   * con flag_visualizza = 'S'
   * 
   * 
   * @return
   * @throws DataAccessException
   */
  public Vector<TipoSettoreAbacoVO> getListSettoreAbaco() 
    throws DataAccessException
  {
    String query = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    StringBuffer queryBuf = null;
    Vector<TipoSettoreAbacoVO> vTipoSettoreAbaco = null;
  
    try
    {
      SolmrLogger
        .debug(this,
            "[TerreniDAO::getListSettoreAbaco] BEGIN.");
  
      /* CONCATENAZIONE/CREAZIONE QUERY BEGIN. */
  
      queryBuf = new StringBuffer();
      queryBuf
          .append("   SELECT   " 
              + "     TSA.ID_SETTORE_ABACO, "
              + "     TSA.COD_SETTORE, "
              + "     TSA.DESCRIZIONE, "
              + "     TSA.FLAG_VISUALIZZA, "
              + "     TSA.DESC_BREVE   "
              + "   FROM   "
              + "     DB_TIPO_SETTORE_ABACO TSA   "
              + "   WHERE  "
              + "     TSA.FLAG_VISUALIZZA = 'S'  ");
      
      
      
      
      query = queryBuf.toString();
      /* CONCATENAZIONE/CREAZIONE QUERY END. */
  
      conn = getDatasource().getConnection();
      if (SolmrLogger.isDebugEnabled(this))
      {
        // Dato che la query costruita dinamicamente è un dato importante la
        // registro sul file di log se il
        // debug è abilitato
  
        SolmrLogger.debug(this,
            "[TerreniDAO::getListSettoreAbaco] Query="
                + query);
      }
      stmt = conn.prepareStatement(query);
  
      // Setto i parametri della query
      ResultSet rs = stmt.executeQuery();
      
      while (rs.next())
      {
        if(vTipoSettoreAbaco == null)
        {
          vTipoSettoreAbaco = new Vector<TipoSettoreAbacoVO>();
        }
        TipoSettoreAbacoVO tipoVO  = new TipoSettoreAbacoVO();
        tipoVO.setIdSettoreAbaco(rs.getLong("ID_SETTORE_ABACO"));
        tipoVO.setCodSettore(rs.getLong("COD_SETTORE"));
        tipoVO.setDescrizione(rs.getString("DESCRIZIONE"));
        String flag = rs.getString("FLAG_VISUALIZZA");
        if(Validator.isNotEmpty(flag))
        {
          tipoVO.setFlagVisualizza(true);
        }
        tipoVO.setDesc_breve(rs.getString("DESC_BREVE"));
        
        vTipoSettoreAbaco.add(tipoVO);
      }
      return vTipoSettoreAbaco;
    }
    catch (Throwable t)
    {
      // Vettore di variabili interne del metodo
      Variabile variabili[] = new Variabile[]
      { new Variabile("query", query), new Variabile("queryBuf", queryBuf),
          new Variabile("vTipoSettoreAbaco", vTipoSettoreAbaco) };
  
      
      // Logging dell'eccezione, query, variabili e parametri del metodo
      LoggerUtils
          .logDAOError(
              this,
              "[TerreniDAO::getListSettoreAbaco] ",
              t, query, variabili, null);
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
              "[TerreniDAO::getListSettoreAbaco] END.");
    }
  }

}