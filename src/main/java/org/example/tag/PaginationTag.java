package org.example.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class PaginationTag extends TagSupport{

	private static final long serialVersionUID = 1L;
	private int currentPage;
	private int nextPage;
	private int previousPage;
	private String searchText;
	private String sortBy;
	private String sortType;
	private String nextButtonText;
	private String previousButtonText;
	
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	
	public void setPreviousPage(int previousPage) {
		this.previousPage = previousPage;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public void setNextButtonText(String nextButtonText) {
		this.nextButtonText = nextButtonText;
	}

	public void setPreviousButtonText(String previousButtonText) {
		this.previousButtonText = previousButtonText;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		String insertSearchText = "";
		if (!searchText.equalsIgnoreCase("")) {
			insertSearchText = "search=" + searchText + "&";
		}
		String insertSortType = "";
		if (!sortType.equalsIgnoreCase("")) {
			insertSortType = "sortType=" + sortType + "&";
		}
		if (!sortBy.equalsIgnoreCase("")) {
			String insertSortBy = "sortBy=" + sortBy + "&";
			insertSearchText = insertSearchText.concat(insertSortBy).concat(insertSortType);
		}
		try {
			out.println("<table class=\"pagination-table\">");
			out.println("<tr>");
			if (currentPage == previousPage) {
				out.println("<td></td>");
			} else {
				out.print("<td><a href=\"reader?");
				out.print(insertSearchText + "page=");
				out.println(previousPage + "\">" + previousButtonText + "</a></td>");
			}
			if (currentPage == nextPage) {
				out.println("<td></td>");
			} else {
				out.print("<td class=\"right\"><a href=\"reader?");
				out.print(insertSearchText + "page=");
				out.println(nextPage + "\">" + nextButtonText + "</a></td>");
			}
			out.println("</tr>");
			out.println("</table>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	
}
