package com.mdcbeta.data.remote.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class PreviousSchedule{

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

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setDoctorId(String doctorId){
		this.doctorId = doctorId;
	}

	public String getDoctorId(){
		return doctorId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setSlotPrice(String slotPrice){
		this.slotPrice = slotPrice;
	}

	public String getSlotPrice(){
		return slotPrice;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}
}