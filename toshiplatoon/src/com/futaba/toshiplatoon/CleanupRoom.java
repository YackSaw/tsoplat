package com.futaba.toshiplatoon;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CleanupRoom extends HttpServlet {
	private static final Logger log = Logger.getLogger(RoomMan.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PersistenceManagerFactory factory = PMF.get();
        PersistenceManager pm = factory.getPersistenceManager();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        
        //Ç∑Ç¨ÇΩèáÇ…
        Query query = pm.newQuery(RoomData.class);
        query.setFilter("endTime < :currentTime");
        
        List<RoomData> roomData = (List<RoomData>)query.execute(new Date());
        
        if(roomData!=null && roomData.size() > 0){
        	log.info("cleanup task invoked");
        	//ëSÇƒè¡Ç∑
        	pm.deletePersistentAll(roomData);
        }

        pm.close();
	}

}
