package puzzle;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * Servlet implementation class ServletPieces
 */

public class ServletPieces extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletPieces() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/png");
		HttpSession session = request.getSession();
		FicheHtml ficheHtml = (FicheHtml) session.getAttribute("ficheHtml");
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			int noImage = ApplicationParams.removeAntiCachePiece(pathInfo);
			BufferedImage image = ficheHtml.getPieces().get(noImage);
			OutputStream os = response.getOutputStream();
			ImageIO.write(image, "PNG", os); // write image as PNG (more)
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
