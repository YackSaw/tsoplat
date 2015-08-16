<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>とし部屋チャット</title>
</head>
<body>
	<script src='/_ah/channel/jsapi'></script>
	<script "type=text/javascript">
		var connected;
		var socket;
		var channel;
		var loggedin = false;

		//メッセージ入力チェック
		function isValidMessage() {
		   var str = document.getElementById("msg").value;
		   if( str.indexOf("\$")==0 || str.length == 0) {
		      alert("不正な文字列です");
		      return false;
		   }
		   return true;
		}

		function sendMessage(path,msg){
			var key = document.querySelector('#channelKey').value;
			var name = document.querySelector('#name').value;

			var xhr = new XMLHttpRequest();
			xhr.open('POST',path+'?channelKey='+key+'&message='+encodeURI(msg)+'&name='+encodeURI(name),true);
			xhr.send();
			
			document.getElementById("msg").value="";
		}
	
		var onOpened = function() {
			connected = true;
			sendMessage("chat","");
		};
		var onMessage= function(param) {
			if(param.data.indexOf("\$")==0 ){
				//制御文字
				if(param.data.indexOf("\$member:")==0 ){
					//メンバー通知
					var memberList = document.querySelector('#memberList');
					var members = param.data.split("\$member:");
					if(members!= null && members.length>1){
						memberList.innerHTML = members[1] ;
					}
				}
				
				if( param.data.indexOf("\$oldLog:")==0 ){
					//過去ログ
					if(!loggedin){
						var ul = document.querySelector('#list');
						var text = param.data.split("\$oldLog:");
						
						if(text != null && text.length > 1){
							//入室済みの場合はなんもせん
							ul.innerHTML = text[1];
						}
						loggedin = true;
					}
				}
			}else{
				var ul = document.querySelector('#list');
				ul.innerHTML = '<li>' + param.data + '</li>' + ul.innerHTML;
			}
		};
		var onError = function(e){
			//alert("error:[" + e.code + "]" + e.description);
		};
		var onClose = function() {
			connected = false;
			//alert("close");
		};
      
      	function nameCheck(){
      		if( document.getElementById("name").value == "" ){
      			return false;
      		}else{
      			return true;
      		}
      	}
      	
		function openChannel(token){
			//alert('opench');
			var valid = nameCheck();
			if(valid){
				document.getElementById("name").disabled = true;
				document.getElementById("idBtnLogin").disabled = true;
				document.getElementById("msg").disabled = false;
				document.getElementById("idBtnSend").disabled = false;
				
				var token = document.querySelector('#token').value;
				channel = new goog.appengine.Channel(token);
				socket = channel.open();
				socket.onopen = onOpened;
				socket.onmessage = onMessage;
				socket.onerror = onError;
				socket.onclose = onClose;
			}else{
				alert("おなまえを入力してください");
			}
		}
		
		function createChannelCallback(request){
			var token = document.querySelector('#token').value;
			token.value = request.responseText;
		}
		
		function createChannel(path){
			var key = document.querySelector('#channelKey').value;
			var xhr = new XMLHttpRequest();
			xhr.open('GET',path+'?channelKey='+key,true);
			xhr.onreadystatechange = function(){
				if(xhr.readyState == 4 && xhr.status == 200 ) {
					createChannelCallback(xhr);
				}
			};
			xhr.send();
		}
		
		function doAction(){
//			alert("doact");
			if(isValidMessage()){
				var msg = document.querySelector('#msg').value;
				sendMessage("chat",msg);
			}
		}
		
		function initialize(){
			createChannel('chat');
			document.body.onunload = function(){
				try{
					socket.close();
				}catch(e){}
			}
		}
		
		setTimeout(initialize,100);
	</script>
	<input type="hidden" id="channelKey" value="${channelKey}" />
	<input type="hidden" id="token" value="${token}" />
	おなまえ：<input type="text" id="name" /><button id="idBtnLogin" onclick="openChannel();">入室</button>
	メッセージ：<input type="text" id="msg" disabled>
	<button id="idBtnSend" onclick="doAction();" disabled>送信</button>
	<br>
	現在のメンバー：<span id="memberList"></span>
	<br>
	<ul id="list"></ul>
</body>
</html>