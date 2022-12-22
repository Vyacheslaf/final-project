package org.example.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Publication;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;
import org.example.exception.ServiceException;
import org.example.service.AuthorService;
import org.example.service.BookService;
import org.example.service.PublicationService;
import org.example.util.Messages;

/**
 * A servlet that searches the {@code Book} by ISBN in the catalog.
 * If currently logged user is not an admin, then the user is redirected to the welcome page.
 * If the book is found, then the admin is redirected to the page with book's details,
 * otherwise the admin is redirected to the admin's start page with an error message.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/findbook")
public class BookFinderServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = -6470491880039342370L;
	
	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(BookFinderServlet.class);
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the {@code Book} to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_BOOK = "book";

	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the list of publications to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_PUBLICATIONS = "publications";

	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the list of authors to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_AUTHORS = "authors";
	
	/**
	 * The key for getting a localized error message from the resource bundles if the book is not found
	 */
	private static final String ERROR_BOOK_NOT_FOUND = "error.book.not.found";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.ADMIN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		try {
			List<Publication> publications = new PublicationService().getAllPublications();
			List<Author> authors = new AuthorService().getAllAuthors();
			req.setAttribute(REQ_ATTR_PUBLICATIONS, publications);
			req.setAttribute(REQ_ATTR_AUTHORS, authors);
			Book book = new BookService().findBookByISBN(req);
			if (book == null) {
				req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
											  Messages.getMessage(req, ERROR_BOOK_NOT_FOUND));
				req.getRequestDispatcher(Constants.ADMIN_HOME_PAGE).forward(req, resp);
				return;
			}
			req.setAttribute(REQ_ATTR_BOOK, book);
		} catch (DaoException | ServiceException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			req.getRequestDispatcher(Constants.ADMIN_HOME_PAGE).forward(req, resp);
			return;
		}
		req.getRequestDispatcher(Constants.BOOK_DETAILS_PAGE).forward(req, resp);
	}
}
