package it.csi.solmr.util;

import it.csi.jsf.htmpl.StringProcessor;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Questo StringProcessor, sostituisce i caratteri "speciali" nella
 * corrispondente codifica HTML.
 *
 * @author Luigi R. Viggiano
 * @todo rifattorizzare utilizzando una struttura opportuna per contenere il
 *       mapping tra i caratteri e la rappresentazione HTML.
 * @version 1.0
 *
 * Modificata da TOBECONFIG
 * Inserito il controllo sull'apice singolo e doppio (evita problemi quando il
 * testo deve essere inserito nell'attributo value dei campi di testo).
 */
public class CustomHTMLStringProcessor extends StringProcessor {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7999601252254473834L;
	
	private Map<Object,Object> caratteriSpeciali = new TreeMap<Object,Object>(
        /**
         * Il comparatore ha la sola utilità di dare la precedenza
         * alla chiave "&" in modo che non vengano fatte
         * "sostituzioni sovrapposte"
         */
        new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                String s1 = (String) o1;
                String s2 = (String) o2;
                if ("&".equals(s1)) {
                    if ("&".equals(s2)) {
                        return 0;
                    }
                    return -1;
                }

                if ("&".equals(s2)) {
                    return 1;
                }
                return s1.compareTo(s2);
            }
        }
    );

    public CustomHTMLStringProcessor() {
        Map<Object,Object> cs = caratteriSpeciali;
        cs.put("&", "&amp;");
        cs.put("\"", "&quot;");
        cs.put("<", "&lt;");
        cs.put(">", "&gt;");
        // AGGIUNTA
        cs.put("'", "&#39");
        cs.put("\"", "&#34");
        // FINE AGGIUNTA
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