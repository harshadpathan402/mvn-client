package exporeport;

import java.util.List;

public class InvoicesToOrder {
	
	private List<Integer> invoices;
	private Integer orderId;
	public List<Integer> getOrderIds() {
		return invoices;
	}
	public void setOrderIds(List<Integer> orderIds) {
		this.invoices = orderIds;
	}
	public Integer getInvoiceId() {
		return orderId;
	}
	public void setInvoiceId(Integer invoiceId) {
		this.orderId = invoiceId;
	}
	
	@Override
	public String toString() {
		return String.format("InvoiceToOrders [orderIds=%s, invoiceId=%s]",
				invoices, orderId);
	}
	
}
