<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script "type=text/javascript">
	function onSetEndTimeToNextNews(isChecked){
		document.getElementById("idendYear").disabled = true;
		document.getElementById("idendMonth").disabled = true;
		document.getElementById("idendDate").disabled = true;
		document.getElementById("idendHour").disabled = true;
		document.getElementById("idendMin").disabled = true;
	}
	
	function onSetEndTimeToFreeTime(isChecked){
		document.getElementById("idendYear").disabled = false;
		document.getElementById("idendMonth").disabled = false;
		document.getElementById("idendDate").disabled = false;
		document.getElementById("idendHour").disabled = false;
		document.getElementById("idendMin").disabled = false;
	}

	function onRegularRadioClicked(isChecked){
		document.getElementById("idroomPass").disabled = true;
		document.getElementById("idlistTagMember").disabled = true;
		document.getElementById("idlistPrivateMember").disabled = true;
		document.getElementById("idlistPrivateRule").disabled = true;
		document.getElementById("idlistPrivateStage").disabled = true;
	}

	function onTagRadioClicked(isChecked){
		document.getElementById("idroomPass").disabled = false;
		
		//タッグバトル関係のUIを表示
		document.getElementById("idlistTagMember").disabled = !isChecked;
		
		//プライベート関係のUIは非表示
		document.getElementById("idlistPrivateMember").disabled = isChecked;
		document.getElementById("idlistPrivateRule").disabled = isChecked;
		document.getElementById("idlistPrivateStage").disabled = isChecked;
	}
	
	function onPrivateRadioClicked(isChecked){
		document.getElementById("idroomPass").disabled = false;
		
		//タッグバトル関係のUIを非表示
		document.getElementById("idlistTagMember").disabled = isChecked;
	
		//プライベート関係のUIを表示
		document.getElementById("idlistPrivateMember").disabled = !isChecked;
		document.getElementById("idlistPrivateRule").disabled = !isChecked;
		document.getElementById("idlistPrivateStage").disabled = !isChecked;
	}
</script>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>スプラのとし部屋一覧</title>
</head>
<body>
	<c:if test="${passwd_gen}">
	部屋が登録されました<br>
	<font color="#FF0000">部屋の削除パスワード：<%=session.getAttribute("passwd_confirm")%> </font><br>
	一覧への反映には時間がかかる場合があります<br>
	</c:if>

	<table border="1">
	<tr>
	<th bgcolor="#CCCCCC"><font color="#000000">ステータス</font></th>
	<th bgcolor="#CCCCCC"><font color="#000000">なまえ(省略可)</font></th>
	<th bgcolor="#CCCCCC"><font color="#000000">ID</font></th>
	<th bgcolor="#CCCCCC"><font color="#000000">ルール</font></th>
	<th bgcolor="#CCCCCC"><font color="#000000">開始時間</font></th>
	<th bgcolor="#CCCCCC"><font color="#000000">終了時間</font></th>
	<th bgcolor="#CCCCCC"><font color="#000000">部屋のパスワード(省略可)</font></th>
	<th bgcolor="#CCCCCC"><font color="#000000">備考(省略可)</font></th>
	<th bgcolor="#CCCCCC"><font color="#000000"></font></th>
	</tr>
	
	<c:forEach var="room" items="${roomDataList}">
	<c:if test="${2 != room.stat}"> 
	<tr  valign="bottom">
	<td>
		<font color="#000000">
		<c:if test="${0 == room.stat}">
			<c:out value="待機中" />
		</c:if>
		<c:if test="${1 == room.stat}">
			<b><c:out value="開催中" /></b>
		</c:if>
		<c:if test="${2 == room.stat}">
			<c:out value="終了" />
		</c:if>
		</font>
	</td>
	<td><c:out value="${room.name}" /></td>
	<td><c:out value="${room.uid}" /></td>
	<td>
		<c:if test="${'regular' == room.rule}">
			<c:out value="レギュラー" />
		</c:if>
		<!--
		<c:if test="${'ranked' == room.rule}">
			<c:out value="ガチ" />
		</c:if>
		-->
		<!-- タッグ -->
		<c:if test="${'ranked_tag' == room.rule}">
			<c:out value="タッグ" /><br>
			　人数:
			<c:if test="${'two' == room.members}">
				<c:out value="2人" /><br>
			</c:if>
			<c:if test="${'three' == room.members}">
				<c:out value="3人" /><br>
			</c:if>
			<c:if test="${'four' == room.members}">
				<c:out value="4人" /><br>
			</c:if>
			
			
		</c:if>
		
		<!-- プライベート-->
		<c:if test="${'ranked_private' == room.rule}">
			<c:out value="プライベート" /><br>
			　人数:
			<c:if test="${'two' == room.members}">
				<c:out value="2人" /><br>
			</c:if>
			<c:if test="${'three' == room.members}">
				<c:out value="3人" /><br>
			</c:if>
			<c:if test="${'four' == room.members}">
				<c:out value="4人" /><br>
			</c:if>
			<c:if test="${'five' == room.members}">
				<c:out value="5人" /><br>
			</c:if>
			<c:if test="${'six' == room.members}">
				<c:out value="6人" /><br>
			</c:if>
			<c:if test="${'seven' == room.members}">
				<c:out value="7人" /><br>
			</c:if>
			<c:if test="${'eight' == room.members}">
				<c:out value="8人" /><br>
			</c:if>
			
			<!-- プライベートルール -->
			　ルール:
			<c:if test="${'private_rule_ranked_rainmaker' == room.privateRule}">
				<c:out value="ガチホコ" /><br>
			</c:if>
			<c:if test="${'private_rule_ranked_tower' == room.privateRule}">
				<c:out value="ガチヤグラ" /><br>
			</c:if>
			<c:if test="${'private_rule_ranked_area' == room.privateRule}">
				<c:out value="ガチエリア" /><br>
			</c:if>
			<c:if test="${'private_rule_regular' == room.privateRule}">
				<c:out value="ナワバリ" /><br>
			</c:if>
			
			<!-- ステージ -->
			　ステージ:
			<c:if test="${'private_stage_decline' == room.privateStage}">
				<c:out value="デカライン高架下" /><br>
			</c:if>
			<c:if test="${'private_stage_hakofugu' == room.privateStage}">
				<c:out value="ハコフグ倉庫" /><br>
			</c:if>
			<c:if test="${'private_stage_shionome' == room.privateStage}">
				<c:out value="シオノメ油田" /><br>
			</c:if>
			<c:if test="${'private_stage_arowana' == room.privateStage}">
				<c:out value="アロワナモール" /><br>
			</c:if>
			<c:if test="${'private_stage_bbus' == room.privateStage}">
				<c:out value="Bバスパーク" /><br>
			</c:if>
			<c:if test="${'private_stage_mongara' == room.privateStage}">
				<c:out value="モンガラキャンプ場" /><br>
			</c:if>
			<c:if test="${'private_stage_hokke' == room.privateStage}">
				<c:out value="ホッケふ頭" /><br>
			</c:if>
			<c:if test="${'private_stage_mozuku' == room.privateStage}">
				<c:out value="モズク農園" /><br>
			</c:if>
			<c:if test="${'private_stage_tachiuo' == room.privateStage}">
				<c:out value="タチウオパーキング" /><br>
			</c:if>
			<c:if test="${'private_stage_negitoro' == room.privateStage}">
				<c:out value="ネギトロ炭鉱" /><br>
			</c:if>
			<c:if test="${'private_stage_omakase' == room.privateStage}">
				<c:out value="おまかせ" /><br>
			</c:if>
			<c:if test="${'private_stage_others' == room.privateStage}">
				<c:out value="その他" /><br>
			</c:if>
			
			
		</c:if>	
	</td>
	<td><fmt:formatDate value="${room.startTime}" pattern="yyyy/MM/dd HH:mm" timeZone="Asia/Tokyo" /></td>
	<td><fmt:formatDate value="${room.endTime}" pattern="yyyy/MM/dd HH:mm" timeZone="Asia/Tokyo" /></td>
	<td>
		<c:if test="${'ranked_tag' == room.rule}">
			<c:out value="${room.roomPass}" /><br>
		</c:if>
		<c:if test="${'ranked_private' == room.rule}">
			<c:out value="${room.roomPass}" /><br>
		</c:if>
	
	</td>
	<td><c:out value="${room.comment}" escapeXml="false"/></td>
	<td>
		<a href="chat?channelKey=<c:out value='${room.id}' />">簡易チャット(お試し版)</a>
		<form action="/del_room" method="post">
			<input type="passwd" name="passwd" />
			<input type="hidden" name="id" value="${room.id}"/>
			<button type="submit" name="del_room" />削除</td>
		</form>
	</td>
	</tr>
	</c:if>
	</c:forEach>
	
	<tr>
	<th bgcolor="#CCCCFF"><font color="#000000"></font></th>
	<th bgcolor="#CCCCFF"><font color="#000000">なまえ(省略可)</font></th>
	<th bgcolor="#CCCCFF"><font color="#000000">ID</font></th>
	<th bgcolor="#CCCCFF"><font color="#000000">ルール</font></th>
	<th bgcolor="#CCCCFF"><font color="#000000">開始時間</font></th>
	<th bgcolor="#CCCCFF"><font color="#000000">終了時間</font></th>
	<th bgcolor="#CCCCFF"><font color="#000000">部屋のパスワード(省略可)</font></th>
	<th bgcolor="#CCCCFF"><font color="#000000">備考(省略可)</font></th>
	<th bgcolor="#CCCCFF"><font color="#000000"></font></th>
	</tr>
	
	<form action="/rooms" method="post">
	<tr  valign="bottom">
	<td><font color="#000000"></font></th>
	<td><input type="text" name="name" /></td>
	<td>
		<input type="text" name="uid" />
		<c:if test="${uid_error}">
		<br><font color="#FF0000">IDを入れないと誰も検索できないよ</font>
		</c:if>
	</td>
	<td>
		<input type="radio" name="ruleType" value="regular"
		onClick="onRegularRadioClicked(this.checked);"  
		checked>レギュラー</input><br>
		<!-- <input type="radio" name="ruleType" value="ranked">ガチ</input><br> -->
		<input type="radio" name="ruleType" value="ranked_tag"
		 onClick="onTagRadioClicked(this.checked);"
		 >タッグ</input><br>
		　人数：<select name="listTagMember" id="idlistTagMember" disabled>
			<option value="two">2人</option>
			<option value="three">3人</option>
			<option value="four">4人</option>
		</select><br>
		
		<input type="radio" name="ruleType" value="ranked_private" 
		onClick="onPrivateRadioClicked(this.checked);"
		>プライベート</input><br>
		　人数：<select name="listPrivateMember" id="idlistPrivateMember" disabled>
			<option value="two">2人</option>
			<option value="three">3人</option>
			<option value="four">4人</option>
			<option value="five">5人</option>
			<option value="six">6人</option>
			<option value="seven">7人</option>
			<option value="eight">8人</option>
		</select><br>
		　ルール：<select name="listPrivateRule" id="idlistPrivateRule" disabled>
			<option value="private_rule_regular">ナワバリ</option>
			<option value="private_rule_ranked_area">ガチエリア</option>
			<option value="private_rule_ranked_tower">ガチヤグラ</option>
			<option value="private_rule_ranked_rainmaker">ガチホコ</option>
		</select><br>
		　ステージ：<select name="listPrivateStage" id="idlistPrivateStage" disabled>
			<option value="private_stage_decline">デカライン高架下</option>
			<option value="private_stage_hakofugu">ハコフグ倉庫</option>
			<option value="private_stage_shionome">シオノメ油田</option>
			<option value="private_stage_arowana">アロワナモール</option>
			<option value="private_stage_bbus">Bバスパーク</option>
			<option value="private_stage_mongara">モンガラキャンプ場</option>
			<option value="private_stage_hokke">ホッケふ頭</option>
			<option value="private_stage_mozuku">モズク農園</option>
			<option value="private_stage_tachiuo">タチウオパーキング</option>
			<option value="private_stage_negitoro">ネギトロ炭鉱</option>
			<option value="private_stage_omakase">おまかせ</option>
			<option value="private_stage_others">その他</option>
		</select><br>
	</td>
	<td>
		<select name="startYear">
		<c:forEach begin="0" end="1" var="i">
		<option <c:if test="${i+currentYear==startYear}">selected</c:if>>
		<c:out value="${i+currentYear}" /></option>
		</c:forEach>
		</select>年
		
		<select name="startMonth">
		<c:forEach begin="0" end="11" var="i">
		<option value="<c:out value="${i}" />" <c:if test="${i == startMonth}">selected</c:if>><c:out value="${i+1}" /></option>
		</c:forEach>
		</select>月
		
		<select name="startDate">
		<c:forEach begin="1" end="31" var="i">
		<option <c:if test="${i == startDate}">selected</c:if>><c:out value="${i}" /></option>
		</c:forEach>
		</select>日
		<br>
		<select name="startHour">
		<c:forEach begin="0" end="23" var="i">
		<option <c:if test="${i == startHour}">selected</c:if>><c:out value="${i}" /></option>
		</c:forEach>
		</select>時
		
		<select name="startMin">
		<c:forEach begin="0" end="59" step="5" var="i">
		<option <c:if test="${i == startMin}">selected</c:if>><c:out value="${i}" /></option>
		</c:forEach>
		</select>分
	</td>
	<td>
		<input type="radio" name="shutType" value="news" checked
		 onClick="onSetEndTimeToNextNews(this.checked);" >次回ニュースまで</input><br>
		<input type="radio" name="shutType" value="option"
		 onClick="onSetEndTimeToFreeTime(this.checked);" >任意時間まで</input><br>
		
		<select name="endYear" id="idendYear" disabled>
		<c:forEach begin="0" end="1" var="i">
		<option <c:if test="${i+currentYear==endYear}">selected</c:if>>
		<c:out value="${i+currentYear}" /></option>
		</c:forEach>
		</select>年
		
		<select name="endMonth" id="idendMonth" disabled>
		<c:forEach begin="0" end="11" var="i">
		<option value="<c:out value="${i}" />" <c:if test="${i == endMonth}">selected</c:if>><c:out value="${i+1}" /></option>
		</c:forEach>
		</select>月
		
		<select name="endDate" id="idendDate" disabled>
		<c:forEach begin="1" end="31" var="i">
		<option <c:if test="${i == endDate}">selected</c:if>><c:out value="${i}" /></option>
		</c:forEach>
		</select>日
		<br>
		<select name="endHour" id="idendHour" disabled>
		<c:forEach begin="0" end="23" var="i">
		<option <c:if test="${i == endHour}">selected</c:if>><c:out value="${i}" /></option>
		</c:forEach>
		</select>時
		
		<select name="endMin" id="idendMin" disabled>
		<c:forEach begin="0" end="59" step="5" var="i">
		<option <c:if test="${i == endMin}">selected</c:if>><c:out value="${i}" /></option>
		</c:forEach>
		</select>分
		
		<c:if test="${endDate_error}">
		<br><font color="#FF0000">その時間は変だ</font>
		</c:if>
		<c:if test="${endDate_toolong}">
		<br><font color="#FF0000">いくらなんでも長すぎる！死ぬ気か！</font>
		</c:if>
	</td>
	<td><textarea name="roomPass" id="idroomPass" type="text" disabled></textarea></td>
	<td><textarea name="comment" type="text" cols="20" rows="3" ></textarea></td>
	<td><button type="submit" name="register" />新規登録</td>
	</tr>
	</form>
	</table>
	
	<br>
	<div>
	としイカ5つの誓い<br>
	・どんなに煽られても決して煽り返さぬこと<br>
	・としイカ以外のイカとも仲良く遊ぶこと<br>
	・負けても仲間のイカのせいにしないこと<br>
	・連敗して心を荒らしてもスレは荒らさぬこと<br>
	・ムキムキしだしたらもう寝なさい<br>
	</div>
</body>
</html>