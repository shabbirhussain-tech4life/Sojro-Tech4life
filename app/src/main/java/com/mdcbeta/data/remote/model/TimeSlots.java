package com.mdcbeta.data.remote.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class TimeSlots{

	@SerializedName("start_time")
	private String startTime;

	@SerializedName("end_time")
	private String endTime;

	@SerializedName("id")
	private String id;

	@SerializedName("schedule_id")
	private String scheduleId;

	@SerializedName("day")
	private String day;

	@SerializedName("date")
	private String date;

	@SerializedName("open")
	private String open;

	public String getStartTime(){
		return startTime;
	}

	public String getEndTime(){
		return endTime;
	}

	public String getId(){
		return id;
	}

	public String getScheduleId(){
		return scheduleId;
	}

	public String getDay(){
		return day;
	}

	public String getOpen(){
		return open;
	}

	public String getDate(){
		return date;
	}
}