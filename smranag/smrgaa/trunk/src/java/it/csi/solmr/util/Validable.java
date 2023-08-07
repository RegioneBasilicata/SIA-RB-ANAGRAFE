package it.csi.solmr.util;

import java.io.Serializable;

/**
 *
 * <p>Title: ADVI: Acquisizione Domande Via Internet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: CSI Piemonte</p>
 * @author Luigi R. Viggiano
 * @version $Revision: 1.1 $
 */
public interface Validable extends Serializable {
    public ValidationErrors validate();
}
