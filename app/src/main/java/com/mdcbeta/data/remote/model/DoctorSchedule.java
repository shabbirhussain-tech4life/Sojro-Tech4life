package com.mdcbeta.data.remote.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class DoctorSchedule{

	@SerializedName("end_date")
	private String endDate;

	@SerializedName("doctor_id")
	private String doctorId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("slot_price")
	private String slotPrice;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("start_date")
	private String startDate;

	public String getEndDate(){
		return endDate;
	}

	public String getDoctorId(){
		return doctorId;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getSlotPrice(){
		return slotPrice;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public String getStartDate(){
		return startDate;
	}
}