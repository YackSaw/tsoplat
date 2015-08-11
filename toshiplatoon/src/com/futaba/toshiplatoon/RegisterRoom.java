package com.futaba.toshiplatoon;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterRoom extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String passwd = req.getParameter("passwd");
		
		req.setAttribute("passwd_gen", true);
		req.setAttribute("passwd_confirm", passwd);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/reg_room.jsp");
        try {
			dispatcher.forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
