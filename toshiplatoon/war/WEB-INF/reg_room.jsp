<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>スプラのとし部屋登録</title>
</head>
<body>
	<c:if test="${procOK}">
	部屋が登録されました<br>
	<font color="#FF0000">部屋の削除パスワード：<c:out value="${passwd_confirm}" /></font>
	</c:if>
	<c:if test="${!procOK}">
	部屋の登録に失敗しました
	</c:if>

	<br>
	<a href = "./rooms" >戻る</a>

</body>
</html>