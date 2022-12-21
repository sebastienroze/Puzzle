package puzzle;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ApplicationRADServlet extends HttpServlet {
	/**
	 * 
	 */	
	
	final static String apiURL = "./ApplicationRAD";

	private static final long serialVersionUID = 1L;
	
	public boolean checkLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String login = (String) session.getAttribute("login");
		if (login == null) {			
			FicheHtml ficheHtml = new FicheHtml(apiURL, "Login") ;
			new FicheLogin(ficheHtml);			
			ficheHtml.setValuesAndWriteResponse(request.getParameterMap(),response);
			response.getWriter().print(ficheHtml.getContent());
			return false;
		}
		return true;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(getFiche(request).getContent());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		FicheHtml ficheHtml = getFiche(request);
		ficheHtml = ficheHtml.setValuesAndWriteResponse(request.getParameterMap(),response);
		if (ficheHtml!=null) {
			HttpSession session = request.getSession();
			if (ficheHtml.storeSession()) {
			session.setAttribute("ficheHtml", ficheHtml);
			} else {
				session.invalidate();
			}
			response.getWriter().print(ficheHtml.getContent());			
		}
	}
	
	protected FicheHtml getFiche(HttpServletRequest request) {
		HttpSession session = request.getSession();
		FicheHtml ficheHtml = (FicheHtml) session.getAttribute("ficheHtml");		
		if (ficheHtml == null) {
			ficheHtml = new FicheHtml(apiURL, "Login");
			new FicheLogin(ficheHtml);			
		} 
		return ficheHtml;
	}
}
