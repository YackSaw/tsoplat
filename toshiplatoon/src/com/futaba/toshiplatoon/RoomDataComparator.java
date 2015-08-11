package com.futaba.toshiplatoon;

import java.util.Comparator;

public class RoomDataComparator implements Comparator<RoomData> {

	@Override
	public int compare(RoomData o1, RoomData o2) {
		if(o1.getStartTime().equals(o2.getStartTime())){
			return 0;
		}else if(o1.getStartTime().after(o2.getStartTime())){
			return 1;
		}else{
			return -1;
		}
	}

}
