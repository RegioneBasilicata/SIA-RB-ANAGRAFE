package it.csi.solmr.exception;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.ServletException;

/**
 * The class <code>ValidationException</code> indicates that some data
 * inserted in a web form is invalid.
 *
 * @author  Luigi R. Viggiano
 * @version 1.0, 1/02/02
 * @see     java.lang.Exception
 */
public class ValidationException extends ServletException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8455600611018634863L;

	/**
     * Constructs a <code>ValidationException</code> with no specified details.
     */
    public ValidationException() {
    }

    /**
     * Constructs a new <code>ValidationException</code> with the specified
     * error message.
     *
     * @param   message   the error message. The error message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public ValidationException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new <code>ValidationException</code> with the specified
     * error message and page to handle the message.
     *
     * @param   message     the error message.
     * @param   errorPage   the error page.
     */
    public ValidationException(String msg, String errorPage) {
	super(msg);
        setErrorPage(errorPage);
    }

    /**
     * Constructs a new <code>ValidationException</code> with the specified
     * error messages and page to handle the messages.
     *
     * @param   messages    a <code>Map</code> containing messages identified
     *          by positions.
     * @param   errorPage   the error page.
     */
    public ValidationException(Map<Object,Object> messages, String errorPage) {
	setMessages(messages);
        setErrorPage(errorPage);
    }

    /**
     * The relative URL to the JSP page that will show the error to the user.
     */
    private String errorPage;

    /**
     * The Map containing error messages and positions in the html where to
     * display them.
     */
    private Map<Object,Object> messages;

    /**
     * Returns the URL to the JSP page that will show messages specified by the
     * <code>ValidationException</code> object.
     *
     * @return  the URL to the JSP page that will show messages specified by the
     *          <code>ValidationException</code> object.
     */
    public String getErrorPage() {
        return errorPage;
    }

    /**
     * Set the errorPage property indicating the page that will handle the
     * error messages.
     *
     * @param   errorPage   the error page. The error page is saved for
     *          later retrieval by the {@link #getErrorPage()} method.
     */
    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    /**
     * Set the mapping between messages and positions.
     *
     * @param   messages    a <code>Map</code> containing messages identified
     *          by positions.
     */
    protected void setMessages(java.util.Map<Object,Object> messages) {
        this.messages = messages;
    }

    /**
     * Returns the mapping between messages and positions where they will be
     * displayed by the error page.
     *
     * @return  a <code>Map</code> containing messages identified by positions.
     */
    public java.util.Map<Object,Object> getMessages() {
        if (null == messages)
            messages = new HashMap<Object,Object>();
        return messages;
    }

    /**
     * Add a message to be displayed in a specific position in the errorPage
     *
     * @param   message   the error message. The error message is associated to
     *          the position.
     * @param   position  the message position (as key for the message) where
     *          the it must be printed in the errorPage.
     */
    public void addMessage(String message, String position) {
        getMessages().put(position, message);
    }

    /**
     * Removes the message specified by the position.
     *
     * @param   position  the message position (as key for the message)
     */
    public void removeMessage(String position) {
        getMessages().remove(position);
    }

    /**
     * Returns the error message string of this throwable object.
     *
     * @param   position   the message position (as key for the message).
     * @return  the error message specified by a position (the key of the
     *          message), or an empty string if there is no message to display
     *          in that position.
     */
    public String getMessage(String position) {
        String msg = (String) getMessages().get(position);
        return (null == msg) ? "" : msg;
    }

    /**
     * Throws <code>this</code> exception if any message has been added.
     */
    public void raise() throws ValidationException {
        if (null != messages && ! messages.isEmpty())
            throw this;
    }

    public Throwable getRootCause() {
        return this;
    }

}