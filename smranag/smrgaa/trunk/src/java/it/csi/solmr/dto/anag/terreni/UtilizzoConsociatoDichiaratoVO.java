package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_UTILIZZO_CONSOCIATO_DICH
 * 
 * @author Mauro Vocale
 *
 */

public class UtilizzoConsociatoDichiaratoVO implements Serializable 
{
		
	private static final long serialVersionUID = 3914227749217097610L;
	private Long idUtilizzoConsociatoDich = null;
	private Long idUtilizzoDichiarato = null;
	private Long idPianteConsociate = null;
	private String numeroPiante = null;
	
	
	
	/**
	 * @return the idPianteConsociate
	 */
	public Long getIdPianteConsociate() {
		return idPianteConsociate;
	}
	/**
	 * @param idPianteConsociate the idPianteConsociate to set
	 */
	public void setIdPianteConsociate(Long idPianteConsociate) {
		this.idPianteConsociate = idPianteConsociate;
	}
	/**
	 * @return the idUtilizzoConsociatoDich
	 */
	public Long getIdUtilizzoConsociatoDich() {
		return idUtilizzoConsociatoDich;
	}
	/**
	 * @param idUtilizzoConsociatoDich the idUtilizzoConsociatoDich to set
	 */
	public void setIdUtilizzoConsociatoDich(Long idUtilizzoConsociatoDich) {
		this.idUtilizzoConsociatoDich = idUtilizzoConsociatoDich;
	}
	/**
	 * @return the idUtilizzoDichiarato
	 */
	public Long getIdUtilizzoDichiarato() {
		return idUtilizzoDichiarato;
	}
	/**
	 * @param idUtilizzoDichiarato the idUtilizzoDichiarato to set
	 */
	public void setIdUtilizzoDichiarato(Long idUtilizzoDichiarato) {
		this.idUtilizzoDichiarato = idUtilizzoDichiarato;
	}
	/**
	 * @return the numeroPiante
	 */
	public String getNumeroPiante() {
		return numeroPiante;
	}
	/**
	 * @param numeroPiante the numeroPiante to set
	 */
	public void setNumeroPiante(String numeroPiante) {
		this.numeroPiante = numeroPiante;
	}
	
				
}