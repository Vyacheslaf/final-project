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
	private String servletName;
	
	public String getSearchText() {
		return searchText;
	}

	public String getServletName() {
		return servletName;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	public int getCurrentPage() {
		return currentPage;
	}
	
	public int getNextPage() {
		return nextPage;
	}
	
	public int getPreviousPage() {
		return previousPage;
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

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			out.println("<table class=\"pagination-table\">");
			out.println("<tr>");
			if (currentPage == previousPage) {
				out.println("<td></td>");
			} else {
				out.print("<td><a href=\"" + servletName + "?text=");
				out.print(searchText + "&page=");
				out.println(previousPage + "\">Previous page</a></td>");
			}
			if (currentPage == nextPage) {
				out.println("<td></td>");
			} else {
				out.print("<td class=\"right\"><a href=\"" + servletName);
				out.print("?text=" + searchText + "&page=");
				out.println(nextPage + "\">Next page</a></td>");
			}
			out.println("</tr>");
			out.println("</table>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	
}
