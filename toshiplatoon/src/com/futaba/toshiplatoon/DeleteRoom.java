package com.futaba.toshiplatoon;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteRoom extends HttpServlet {
	private static final Logger log = Logger.getLogger(DeleteRoom.class.getName());
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        
		String passwd = req.getParameter("passwd");
		String id = (String) req.getParameter("id");
		boolean procOK = false;
		
		PersistenceManagerFactory factory = PMF.get();
        PersistenceManager pm = factory.getPersistenceManager();
        
        //Ç®ÇÌÇËéûä‘ÇÃëÂÇ´Ç¢èá
        Query query = pm.newQuery(RoomData.class);
        query.setFilter("id == :id");
        //query.declareParameters("Long id");
        
        List<RoomData> roomData = (List<RoomData>)query.execute( Long.parseLong(id) );
        
        if(roomData!=null){
        	log.info("roomData cnt = "+roomData.size());

        	if(roomData.size()==1){
        		if(roomData.get(0).getPasswd().equals(passwd)){
        			//å©Ç¬Ç©Ç¡ÇΩ
        			pm.deletePersistent(roomData.get(0));
        			procOK=true;
        		}
        	}
        }
        
        pm.close();
        
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/del_room.jsp");
        try {
        	req.setAttribute("procOK", procOK);
			dispatcher.forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
