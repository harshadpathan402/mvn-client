package com;

import java.util.ArrayList;
import java.util.List;

public class MissingInvoicesUser {
	
	private Integer userId;
	private List<MissingInvoice> missingInvoice = new ArrayList<>();
	private String message;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public List<MissingInvoice> getMissingInvoices() {
		return missingInvoice;
	}
	public void setMissingInvoices(List<MissingInvoice> orders) {
		this.missingInvoice = orders;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void addMissingInvoice(MissingInvoice missingInvoice){
		getMissingInvoices().add(missingInvoice);
	}
	@Override
	public String toString() {
		return "MissingInvoicesUser [userId=" + userId + ", missingInvoice="
				+ missingInvoice + ", message=" + message + "]";
	}
	
	

}
