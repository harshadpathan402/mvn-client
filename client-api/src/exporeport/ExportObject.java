package exporeport;

import java.util.Date;
import java.util.List;

public class ExportObject {
	
	private Integer userID;
	private Integer orderID;
	private String lineDescription ;
	private Integer numberOfMissingInvoices; 
	private Date activeSince; 
	private Date orderNBD; 
	private Date activeUntil;
	private List<Integer>  invoiceIDsGenerated; 
	private String issueDescription;
	
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}


	public String getLineDescription() {
		return lineDescription;
	}
	public void setLineDescription(String lineDescription) {
		this.lineDescription = lineDescription;
	}
	public Integer getNumberOfMissingInvoices() {
		return numberOfMissingInvoices;
	}
	public void setNumberOfMissingInvoices(Integer numberOfMissingInvoices) {
		this.numberOfMissingInvoices = numberOfMissingInvoices;
	}
	public Date getActiveSince() {
		return activeSince;
	}
	public void setActiveSince(Date activeSince) {
		this.activeSince = activeSince;
	}
	public Date getOrderNBD() {
		return orderNBD;
	}
	public void setOrderNBD(Date orderNBD) {
		this.orderNBD = orderNBD;
	}
	public Date getActiveUntil() {
		return activeUntil;
	}
	public void setActiveUntil(Date activeUntil) {
		this.activeUntil = activeUntil;
	}
	public List<Integer> getInvoiceIDsGenerated() {
		return invoiceIDsGenerated;
	}
	public void setInvoiceIDsGenerated(List<Integer> invoiceIDsGenerated) {
		this.invoiceIDsGenerated = invoiceIDsGenerated;
	}
	public String getIssueDescription() {
		return issueDescription;
	}
	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}

	/**
	 * @return the orderID
	 */
	public Integer getOrderID() {
		return orderID;
	}
	/**
	 * @param orderID the orderID to set
	 */
	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}
	@Override
	public String toString() {
		return String
				.format("ExportObject [userID=%s, orderID=%s, lineDescription=%s, numberOfMissingInvoices=%s, activeSince=%s, orderNBD=%s, activeUntil=%s, invoiceIDsGenerated=%s, issueDescription=%s]",
						userID, orderID, lineDescription,
						numberOfMissingInvoices, activeSince, orderNBD,
						activeUntil, invoiceIDsGenerated, issueDescription);
	}

	
	


	
}
