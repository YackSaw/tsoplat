package com.futaba.toshiplatoon;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class ToshiplatoonServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(ToshiplatoonServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {	

        
/*
        String param1 = req.getParameter("id");
        PrintWriter out = resp.getWriter();
        List<RoomData> list = null;
        if (param1 == null || param1 ==""){
            String query = "select from " + RoomData.class.getName();
            try {
                list = (List<RoomData>)manager.newQuery(query).execute();
            } catch(JDOObjectNotFoundException e){}
        } else {
            try {
            	RoomData data = (RoomData)manager.getObjectById(RoomData.class,Long.parseLong(param1));
                list = new ArrayList();
                list.add(data);
            } catch(JDOObjectNotFoundException e){}
        }
        String res = "[";
        if (list != null){
            for(RoomData data:list){
                res += "{id:" + data.getId() + ",url:'" + data.getUid() + "',title:'" +
                    data.getTitle() + "',date:'" + data.getStartTime() +
                    "',comment:'" + data.getComment() + "'},";
            }
        }
        res += "]";
        out.println(res);
        manager.close();
        */

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/index.jsp");
        try {
			dispatcher.forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//super.doPost(req, resp);
		log.info("passwd check!");
		
		String passwd = req.getParameter("passwd");
		if(passwd.equals("tosiaki")){
			log.info("ok!");
   
		    HttpSession session = req.getSession();
	        session.setAttribute("passwd_check", true);
	        resp.sendRedirect("./rooms");
		}else{
			log.info("ng!");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/index.jsp");
	        try {
				dispatcher.forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
