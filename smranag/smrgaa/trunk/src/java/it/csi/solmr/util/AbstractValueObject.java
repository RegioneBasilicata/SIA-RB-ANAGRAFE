package it.csi.solmr.util;

/**
 *
 * <p>Title: ADVI: Acquisizione Domande Via Internet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: CSI Piemonte</p>
 * @author Luigi R. Viggiano
 * @version $Revision: 1.1 $
 */
public abstract class AbstractValueObject implements ValueObject {

  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -2758442036888872322L;

    public AbstractValueObject() {
    }

    protected String trim(String value) {
        if (null!=value) {
            return value.trim();
        }else {
            return null;
        }
    }

    protected String upperCase(String value) {
        if (null!=value) {
            return value.toUpperCase();
        }else {
            return null;
        }
    }

    /*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_BEGIN*/
    protected void addPopup(ValidationErrors errors) {
        if (errors.size() > 0) {
            ValidationError popuperror = new ValidationError(
                    "Errore nella validazione dei dati. \\n"+
                    "E'' necessario correggere i campi contrassegnati "+
                    "dalla ''X\''.\\n\\n"+
                    "Cliccare sull'' icona ''X\'' vicino ai campi del form \\n"+
                    "per ulteriori dettagli relativi allo specifico errore.");
            errors.add("error", popuperror);
        }
    }
    /*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_END*/

}