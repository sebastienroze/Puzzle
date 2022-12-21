package puzzle;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.imageio.ImageIO;

/**
 * Servlet implementation class ServletMasks
 */
public class ServletMasks extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletMasks() {
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

		String filePath = ApplicationParams.getMasksPath();
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			OutputStream os;
			try {
				os = response.getOutputStream();
				String fileName;
				fileName =  filePath+URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
				ImageIO.write(ApplicationParams.loadResizedImage(fileName), "PNG", os);
			} catch (IOException e) {
			}
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
