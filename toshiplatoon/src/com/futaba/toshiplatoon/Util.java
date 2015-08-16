package com.futaba.toshiplatoon;

public class Util {
	public static String doEscape(String s) {
		String ret = s;
		ret = ret.replace("&", "&amp;");
		ret = ret.replace("<", "&lt;");
		ret = ret.replace(">", "&gt;");
		ret = ret.replace("\"", "&#034");
		ret = ret.replace("\'", "&#039");
		return ret;
	}

}
