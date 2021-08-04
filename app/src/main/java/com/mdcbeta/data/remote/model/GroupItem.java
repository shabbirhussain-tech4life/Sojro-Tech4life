package com.mdcbeta.data.remote.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GroupItem{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("color")
	private String color;

	@SerializedName("group_id")
	private String groupId;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("permission")
	private String permission;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setColor(String color){
		this.color = color;
	}

	public String getColor(){
		return color;
	}

	public void setGroupId(String groupId){
		this.groupId = groupId;
	}

	public String getGroupId(){
		return groupId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPermission(String permission){
		this.permission = permission;
	}

	public String getPermission(){
		return permission;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId(){
		return id;
	}
}