package it.csi.solmr.util;

import it.csi.jsf.htmpl.StringProcessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * <p>Title: ADVI: Acquisizione Domande Via Internet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: CSI Piemonte</p>
 * @author Luigi R. Viggiano
 * @version $Revision: 1.1 $
 */
public class JavaScriptStringProcessor extends StringProcessor {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5374889257854695148L;
	
	private Map<Object,Object> caratteriSpeciali = new HashMap<Object,Object>();

    public JavaScriptStringProcessor() {
        Map<Object,Object> cs = caratteriSpeciali;
        cs.put("'", "\\'");
    }

    public String process(String input) {
        if (input == null) {
            return null;
        }

        StringBuffer newString = new StringBuffer(input);

        Map<Object,Object> cs = caratteriSpeciali;
        Set<?> entrySet = cs.entrySet() ;
        Iterator<?> iter = entrySet.iterator();
        while (iter.hasNext()) {
            Map.Entry<?,?> item = (Map.Entry<?,?>)iter.next();
            String str = newString.toString();
            String key = (String)item.getKey();
            String value = (String)item.getValue();
            int idxChar;
            int fromIndex = 0;
            while ((idxChar = str.indexOf(key, fromIndex)) != -1) {
                int end = idxChar + key.length();
                newString.replace(idxChar, end, value);
                str = newString.toString();
                fromIndex = end + 1;
            }
        }
        return newString.toString();
    }
}
