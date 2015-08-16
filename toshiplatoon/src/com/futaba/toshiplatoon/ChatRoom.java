package com.futaba.toshiplatoon;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.smartcardio.ResponseAPDU;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class ChatRoom extends HttpServlet {
	private static final Logger log = Logger.getLogger(ChatRoom.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 4391812315893169920L;
	static final String logListName = "logList";
	static final String memberListName = "memberList";
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        
        //チャンネルキーの取得
      	String channelKey = req.getParameter("channelKey");
        //チャンネルキーが、存在する部屋のIDと等しい場合のみ下記の処理を行う
        boolean valid = checkValidRoomID(channelKey);
        
        if(valid){
			//Serviceの取得
			ChannelService channelService = ChannelServiceFactory.getChannelService();
			
			//チャンネルの作成
			String token = channelService.createChannel(channelKey);
			//トークンをクライアントに返す
			//resp.getWriter().println(token);
			
			req.setAttribute("token", token);
			req.setAttribute("channelKey",channelKey);	        
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/chat.jsp");
	        try {
				dispatcher.forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else{
        	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/chat_err.jsp");
	        try {
				dispatcher.forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	private boolean checkValidRoomID(String id) {
		boolean ret = false;
		Calendar current = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
		Date currentDate = current.getTime();
		PersistenceManagerFactory factory = PMF.get();
        PersistenceManager pm = factory.getPersistenceManager();
        //おわり時間の大きい順
        Query query = pm.newQuery(RoomData.class);
        query.setFilter("endTime >= :currentTime");
        
        List<RoomData> roomData = (List<RoomData>)query.execute(currentDate);
        Collections.sort(roomData,new RoomDataComparator());
        if(roomData!=null){
        	log.info("roomData cnt = "+roomData.size());
        	
        	for(RoomData data:roomData){
        		if(currentDate.after(data.getEndTime())){
        			//終了してる
        		}else{
        			//それ以外
        			if(data.getId().toString().equals(id)){
        				ret = true;
        				break;
        			}
        		}
        	}
        }
        pm.close();
        
		return ret;
	}

	String makeDateString(){
		Calendar calender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
		// 1.MessageFormatオブジェクトを作成します。
		// ここで書式を設定します。

		MessageFormat mf = new MessageFormat("{0,date,yyyy/MM/dd HH:mm:ss}");
		// 2.CalendarオブジェクトからDate型で取り出し、
		// Object配列として作成します。
		Object[] objs = {calender.getTime()};

		// 3.1で作成したMessageFormatに、2で作成したObjectを処理させます。
		// resultには、YYYY/MM/DD HH:MM:SS にフォーマットされた文字が代入されます。
		String result = mf.format(objs);

		return result;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
	    req.setCharacterEncoding("UTF-8");

		ChannelService channelService = ChannelServiceFactory.getChannelService();
		String channelKey = req.getParameter("channelKey");
		String message = req.getParameter("message");
		String name = req.getParameter("name");
		
		message = Util.doEscape(message);
		name = Util.doEscape(name);
		
		String result = makeDateString();
		
		log.log(Level.INFO, "sendMsg:"+message);
		ServletContext app = this.getServletContext();
		
        if (message == null || message == "") {
			//過去ログ行けるか?
        	message = "<font color=#006666>" + result + " " + name + "さんが入室しました</font>";
        	
        	sendOldLogMessage(app,channelService,channelKey);
        	
        	sendMemberListMessage(app,channelService,name,channelKey);
        	
        } else {
        	message =result + " " + name + ":" + message;
        }
        channelService.sendMessage(new ChannelMessage(channelKey,message));
        addLog(app, channelKey, message);
                
        return;
	}

	private void sendOldLogMessage(ServletContext app, ChannelService channelService, String channelKey) {
		ArrayList<String> logList = (ArrayList<String>)app.getAttribute(logListName+channelKey);
		String logListStr = "";
        if (logList != null) {
        	//メンバーリストを追加
	        app.setAttribute(logListName+channelKey, logList);
            for (String logElem : logList) {
            	logListStr += "<li>" + logElem + "</li>";
            }
        }
        channelService.sendMessage(new ChannelMessage(channelKey, "$oldLog:" + logListStr));
	}
	
	private void addLog(ServletContext app,String channelKey,String message){
		ArrayList<String> logList = (ArrayList<String>)app.getAttribute(logListName+channelKey);
        if (logList != null) {
        	//ログリストを追加
        }else{
        	//ログリストを生成
        	logList = new ArrayList<String>();
        }
        logList.add(0,message);
    	app.setAttribute(logListName+channelKey, logList);
	}

	private void sendMemberListMessage(ServletContext app,ChannelService channelService,String name,String channelKey) {
		ArrayList<String> memberList = (ArrayList<String>)app.getAttribute(memberListName+channelKey);
        if (memberList != null) {
        	//メンバーリストを追加
        	memberList.add(name);
	        app.setAttribute(memberListName+channelKey, memberList);
	        String memberListStr = "";
            for (String memberElem : memberList) {
                memberListStr += " " + memberElem;
            }
            channelService.sendMessage(new ChannelMessage(channelKey, "$member:" + memberListStr));
        }else{
        	//メンバーリストを生成
        	memberList = new ArrayList<String>();
        	memberList.add(name);
        	app.setAttribute(memberListName+channelKey, memberList);
            channelService.sendMessage(new ChannelMessage(channelKey, "$member:" + name));
        }
	}
}
