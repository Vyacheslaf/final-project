package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.servlet.http.HttpServletRequest;

import org.example.dao.BookDao;
import org.example.entity.Book;
import org.example.exception.DaoException;
import org.example.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class BookServiceTest {

	private static final String REQ_PARAM_ISBN = "isbn";
	private static final String REQ_PARAM_AUTHOR = "author";
	private static final String REQ_PARAM_TITLE = "title";
	private static final String REQ_PARAM_PUBLICATION = "publication";
	private static final String REQ_PARAM_PUBLICATION_YEAR = "year";
	private static final String REQ_PARAM_QUANTITY = "quantity";
	private static final String ERROR_WRONG_ISBN = "error.wrong.isbn";
	private static final String ERROR_WRONG_PUBLICATION_YEAR = "error.wrong.publication.year";
	private static final String ERROR_WRONG_QUANTITY = "error.wrong.quantity";
	private static final String ERROR_WRONG_AUTHOR = "error.wrong.author";
	private static final String ERROR_WRONG_TITLE = "error.wrong.title";
	private static final String ERROR_WRONG_PUBLICATION = "error.wrong.publication";
	
	private HttpServletRequest req;
	
	@BeforeEach
	public void prepareMockHttpServletRequest() {
		String isbn = "9780435193058";
		String author = "William Shakespeare";
		String title = "Othello";
		String publication = "Heinemann";
		String publicationYear = "2000";
		String quantity = "1";
		req = Mockito.mock(HttpServletRequest.class);
		Mockito.when(req.getParameter(REQ_PARAM_ISBN)).thenReturn(isbn);
		Mockito.when(req.getParameter(REQ_PARAM_AUTHOR)).thenReturn(author);
		Mockito.when(req.getParameter(REQ_PARAM_TITLE)).thenReturn(title);
		Mockito.when(req.getParameter(REQ_PARAM_PUBLICATION)).thenReturn(publication);
		Mockito.when(req.getParameter(REQ_PARAM_PUBLICATION_YEAR)).thenReturn(publicationYear);
		Mockito.when(req.getParameter(REQ_PARAM_QUANTITY)).thenReturn(quantity);
	}

	@Test
	public void addBookTest() throws DaoException, ServiceException {
		String isbn = "9780435193058";
		String author = "William Shakespeare";
		String title = "Othello";
		String publication = "Heinemann";
		String publicationYear = "2000";
		String quantity = "1";
		Book expectedBook = new Book.Builder().setISBN(isbn)
				 							  .setAuthor(author)
				 							  .setTitle(title)
				 							  .setPublication(publication)
				 							  .setPublicationYear(Integer.parseInt(publicationYear))
				 							  .setQuantity(Integer.parseInt(quantity))
				 							  .build();
		
		Book actualBook = new Book.Builder().build();
		BookDao mockBookDao = Mockito.mock(BookDao.class);
		ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
		Mockito.doReturn(actualBook).when(mockBookDao).create(captor.capture());

		BookService bookService = new BookService(mockBookDao);
		bookService.addBook(req);
		
		assertEquals(expectedBook, captor.getValue());
	}
	
	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"9780000000000",
						"X000000000",
						"000000000X",
						"a"},
			   nullValues = {"NULL"})
	public void wrongISBNTest(String isbn) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_ISBN)).thenReturn(isbn);
		
		BookDao mockBookDao = Mockito.mock(BookDao.class);
		Mockito.when(mockBookDao.create(Mockito.any(Book.class))).then(AdditionalAnswers.returnsFirstArg());
		BookService bookService = new BookService(mockBookDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  bookService.addBook(req));
		assertEquals(ERROR_WRONG_ISBN, ex.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"?"},
			   nullValues = {"NULL"})
	public void wrongAuthorTest(String author) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_AUTHOR)).thenReturn(author);
		
		BookDao mockBookDao = Mockito.mock(BookDao.class);
		Mockito.when(mockBookDao.create(Mockito.any(Book.class))).then(AdditionalAnswers.returnsFirstArg());
		BookService bookService = new BookService(mockBookDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  bookService.addBook(req));
		assertEquals(ERROR_WRONG_AUTHOR, ex.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"}"},
			   nullValues = {"NULL"})
	public void wrongTitleTest(String title) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_TITLE)).thenReturn(title);
		
		BookDao mockBookDao = Mockito.mock(BookDao.class);
		Mockito.when(mockBookDao.create(Mockito.any(Book.class))).then(AdditionalAnswers.returnsFirstArg());
		BookService bookService = new BookService(mockBookDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  bookService.addBook(req));
		assertEquals(ERROR_WRONG_TITLE, ex.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"}"},
			   nullValues = {"NULL"})
	public void wrongPublicationTest(String publication) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_PUBLICATION)).thenReturn(publication);
		
		BookDao mockBookDao = Mockito.mock(BookDao.class);
		Mockito.when(mockBookDao.create(Mockito.any(Book.class))).then(AdditionalAnswers.returnsFirstArg());
		BookService bookService = new BookService(mockBookDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  bookService.addBook(req));
		assertEquals(ERROR_WRONG_PUBLICATION, ex.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"1899",
						"2023",
						"a"},
			   nullValues = {"NULL"})
	public void wrongPublicationYearTest(String publicationYear) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_PUBLICATION_YEAR)).thenReturn(publicationYear);
		
		BookDao mockBookDao = Mockito.mock(BookDao.class);
		Mockito.when(mockBookDao.create(Mockito.any(Book.class))).then(AdditionalAnswers.returnsFirstArg());
		BookService bookService = new BookService(mockBookDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  bookService.addBook(req));
		assertEquals(ERROR_WRONG_PUBLICATION_YEAR, ex.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"0",
						"-1",
						"a"},
			   nullValues = {"NULL"})
	public void wrongQuantityTest(String quantity) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_QUANTITY)).thenReturn(quantity);
		
		BookDao mockBookDao = Mockito.mock(BookDao.class);
		Mockito.when(mockBookDao.create(Mockito.any(Book.class))).then(AdditionalAnswers.returnsFirstArg());
		BookService bookService = new BookService(mockBookDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  bookService.addBook(req));
		assertEquals(ERROR_WRONG_QUANTITY, ex.getMessage());
	}
}
