package com.futaba.toshiplatoon;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class RoomMan extends HttpServlet {
	private static final Logger log = Logger.getLogger(RoomMan.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        
        Calendar calender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        Calendar current = (Calendar) calender.clone(); 
        
        int min = (calender.get(Calendar.MINUTE) / 5) * 5;
        req.setAttribute("currentYear", calender.get(Calendar.YEAR));
        req.setAttribute("startYear", calender.get(Calendar.YEAR));
        req.setAttribute("startMonth", calender.get(Calendar.MONTH));
        req.setAttribute("startDate", calender.get(Calendar.DATE));
        req.setAttribute("startHour", calender.get(Calendar.HOUR_OF_DAY));
        req.setAttribute("startMin", min);
        
        calender.set(Calendar.MINUTE, min);
        calender.add(Calendar.MINUTE, 30);
        req.setAttribute("endYear", calender.get(Calendar.YEAR));
        req.setAttribute("endMonth", calender.get(Calendar.MONTH));
        req.setAttribute("endDate", calender.get(Calendar.DATE));
        req.setAttribute("endHour", calender.get(Calendar.HOUR_OF_DAY));
        req.setAttribute("endMin", calender.get(Calendar.MINUTE));

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        
        PersistenceManagerFactory factory = PMF.get();
        PersistenceManager pm = factory.getPersistenceManager();
        //おわり時間の大きい順
        Query query = pm.newQuery(RoomData.class);
        query.setFilter("endTime >= :currentTime");
        
        List<RoomData> roomData = (List<RoomData>)query.execute(new Date());
        Collections.sort(roomData,new RoomDataComparator());
        if(roomData!=null){
        	log.info("roomData cnt = "+roomData.size());
        	
        	       	
        	Date currentDate = current.getTime();
        	
        	for(RoomData data:roomData){
        		if(currentDate.after(data.getEndTime())){
        			//終了
        			data.setStat(2);
        		}else if(data.getStartTime().after(currentDate)){
        			//待機中
        			data.setStat(0);
        		}else{
        			//開催中
        			data.setStat(1);
        		}
        	}
        }
        pm.close();
        
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/rooms.jsp");
        try {
        	req.setAttribute("roomDataList", roomData);
			dispatcher.forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        HttpSession session = req.getSession(true);
        session.invalidate();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String uid = req.getParameter("uid");
		String rule = req.getParameter("ruleType");
		String shutType = req.getParameter("shutType");
		String comment = req.getParameter("comment");
		int startYear = Integer.parseInt(req.getParameter("startYear"));
		int startMonth = Integer.parseInt(req.getParameter("startMonth"));
		int startDate = Integer.parseInt(req.getParameter("startDate"));
		int startHour = Integer.parseInt(req.getParameter("startHour"));
		int startMin = Integer.parseInt(req.getParameter("startMin"));

		int endYear = 0;
		int endMonth = 0;
		int endDate = 0;
		int endHour = 0;
		int endMin = 0;
		if(shutType.equals("option")){
			endYear = Integer.parseInt(req.getParameter("endYear"));
			endMonth = Integer.parseInt(req.getParameter("endMonth"));
			endDate = Integer.parseInt(req.getParameter("endDate"));
			endHour = Integer.parseInt(req.getParameter("endHour"));
			endMin = Integer.parseInt(req.getParameter("endMin"));	
		}
		
		String members = "";
		String privateRule = "";
		String privateStage = "";
		String roomPass = "";
		if(rule.equals("ranked_tag")){
			members = req.getParameter("listTagMember");	//two or three or four
			roomPass = req.getParameter("roomPass");
		}else{
			members = req.getParameter("listPrivateMember");	//two or three or four
			privateRule = req.getParameter("listPrivateRule");
			privateStage = req.getParameter("listPrivateStage");
			roomPass = req.getParameter("roomPass");
		}
		
		boolean hasErrors = false;
		
		if(uid == null || uid.equals("")){
			req.setAttribute("uid_error", true);
			hasErrors = true;
		}
		
		Calendar startCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
		Calendar endCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
		startCalendar.set(Calendar.YEAR, startYear);
		startCalendar.set(Calendar.MONTH, startMonth);
		startCalendar.set(Calendar.DATE, startDate);
		startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
		startCalendar.set(Calendar.MINUTE, startMin);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);
		
		if(shutType.equals("option")){
			//任意時間で終了な時
			endCalendar.set(Calendar.YEAR, endYear);
			endCalendar.set(Calendar.MONTH, endMonth);
			endCalendar.set(Calendar.DATE, endDate);
			endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
			endCalendar.set(Calendar.MINUTE, endMin);
			endCalendar.set(Calendar.SECOND, 0);
			endCalendar.set(Calendar.MILLISECOND, 0);
		}else{
			//ニュースの時間で終了
			endCalendar = (Calendar) startCalendar.clone();
			if(startCalendar.get(Calendar.HOUR_OF_DAY) < 3)
			{
				//明らかに次の日の3時
				endCalendar.add(Calendar.DATE, 1);
				endCalendar.set(Calendar.HOUR_OF_DAY, 3);
				endCalendar.set(Calendar.MINUTE, 0);
				endCalendar.set(Calendar.SECOND, 0);
				endCalendar.set(Calendar.MILLISECOND, 0);
			}
			if(startCalendar.get(Calendar.HOUR_OF_DAY) < 7){
				//同じ日の7時
				endCalendar.set(Calendar.HOUR_OF_DAY, 7);
				endCalendar.set(Calendar.MINUTE, 0);
				endCalendar.set(Calendar.SECOND, 0);
				endCalendar.set(Calendar.MILLISECOND, 0);
			}else if(startCalendar.get(Calendar.HOUR_OF_DAY) < 11){
				//同じ日の11時
				endCalendar.set(Calendar.HOUR_OF_DAY, 11);
				endCalendar.set(Calendar.MINUTE, 0);
				endCalendar.set(Calendar.SECOND, 0);
				endCalendar.set(Calendar.MILLISECOND, 0);
			}else if(startCalendar.get(Calendar.HOUR_OF_DAY) < 15){
				//同じ日の15時
				endCalendar.set(Calendar.HOUR_OF_DAY, 15);
				endCalendar.set(Calendar.MINUTE, 0);
				endCalendar.set(Calendar.SECOND, 0);
				endCalendar.set(Calendar.MILLISECOND, 0);
			}else if(startCalendar.get(Calendar.HOUR_OF_DAY) < 19){
				//同じ日の19時
				endCalendar.set(Calendar.HOUR_OF_DAY, 19);
				endCalendar.set(Calendar.MINUTE, 0);
				endCalendar.set(Calendar.SECOND, 0);
				endCalendar.set(Calendar.MILLISECOND, 0);
			}else if(startCalendar.get(Calendar.HOUR_OF_DAY) < 23){
				//同じ日の23時
				endCalendar.set(Calendar.HOUR_OF_DAY, 23);
				endCalendar.set(Calendar.MINUTE, 0);
				endCalendar.set(Calendar.SECOND, 0);
				endCalendar.set(Calendar.MILLISECOND, 0);
			}else{
				//明らかに次の日の3時
				endCalendar.add(Calendar.DATE, 1);
				endCalendar.set(Calendar.HOUR_OF_DAY, 3);
				endCalendar.set(Calendar.MINUTE, 0);
				endCalendar.set(Calendar.SECOND, 0);
				endCalendar.set(Calendar.MILLISECOND, 0);
			}
		}
		
		Calendar tmp = (Calendar) startCalendar.clone();
		tmp.add(Calendar.DATE,3);
		if(endCalendar.before(startCalendar)){
			//時間の設定が変
			req.setAttribute("endDate_error", true);
			hasErrors = true;
		}else if(endCalendar.after(tmp)){
			//時間の設定が変
			req.setAttribute("endDate_toolong", true);
			hasErrors = true;
		}
		
		if(hasErrors){
			Calendar current = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
			req.setAttribute("currentYear", current.get(Calendar.YEAR));
			req.setAttribute("name", name);
			req.setAttribute("uid", uid);
			req.setAttribute("rule", rule);
			req.setAttribute("shutType", shutType);
			req.setAttribute("startYear", current.get(Calendar.YEAR));
			req.setAttribute("startMonth", startMonth);
			req.setAttribute("startDate", startDate);
			req.setAttribute("startHour", startHour);
			req.setAttribute("startMin", startMin);
			req.setAttribute("endYear", endYear);
			req.setAttribute("endMonth", endMonth);
			req.setAttribute("endDate", endDate);
			req.setAttribute("endHour", endHour);
			req.setAttribute("endMin", endMin);
			req.setAttribute("comment", comment);
			
			PersistenceManagerFactory factory = PMF.get();
	        PersistenceManager pm = factory.getPersistenceManager();
	        //おわり時間の大きい順
	        Query query = pm.newQuery(RoomData.class);
	        query.setFilter("endTime >= :currentTime");
	        query.setOrdering("endTime");
	        
	        List<RoomData> roomData = (List<RoomData>)query.execute(new Date());
	        
	        if(roomData!=null){
	        	log.info("roomData cnt = "+roomData.size());
	        	req.setAttribute("roomDataList", roomData);
	        }
	        pm.close();
	
	        
	        
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/rooms.jsp");
	        try {
				dispatcher.forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			int ran = (int)(Math.random()*10000);
			String passwd = String.format("%04d", ran);
			
			name = doEscape(name);
			
			String htmlComment = doEscape(comment);
			htmlComment = htmlComment.replace("\n", "<br>");
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			//ぜんぶOK!
			RoomData roomData = new RoomData(
					doEscape(name), 
					doEscape(uid), 
					htmlComment,
					passwd, 
					startCalendar.getTime(), 
					endCalendar.getTime(), rule, 
					members,
					privateRule,
					privateStage,
					roomPass,
					shutType);
			pm.makePersistent(roomData);
			pm.close();
			/*
			req.setAttribute("passwd_gen", true);
			req.setAttribute("passwd_confirm", passwd);
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/reg_room.jsp");
	        try {
				dispatcher.forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
	        
	        HttpSession session = req.getSession();
	        session.setAttribute("passwd_gen", true);
	        session.setAttribute("passwd_confirm", passwd);
	        resp.sendRedirect("./rooms");
	        
		}
	}

	private String doEscape(String s) {
		String ret = s;
		ret = ret.replace("&", "&amp;");
		ret = ret.replace("<", "&lt;");
		ret = ret.replace(">", "&gt;");
		ret = ret.replace("\"", "&#034");
		ret = ret.replace("\'", "&#039");
		return ret;
	}


}
