package com.mdcbeta.data.remote.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class CareGiver{

	@SerializedName("send_to")
	private String sendTo;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("patient_id")
	private String patientId;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	@SerializedName("relation")
	private String relation;

	public void setSendTo(String sendTo){
		this.sendTo = sendTo;
	}

	public String getSendTo(){
		return sendTo;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setPatientId(String patientId){
		this.patientId = patientId;
	}

	public String getPatientId(){
		return patientId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
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

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setRelation(String relation){
		this.relation = relation;
	}

	public String getRelation(){
		return relation;
	}
}