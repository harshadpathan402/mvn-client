/**
 * 
 */
package com;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jbilling
 *
 */
public class MissingInvoice {
    
    private Set<Integer> orderIds = new HashSet<>();
    private List<Integer> invoiceIds = new ArrayList<>();
    private Integer userId;
    private Integer invoiceCount;
    private List<String> messages = new ArrayList<>();
    
    public List<String> getMessages() {
        return messages;
    }
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
    public void addMessage(String message){
        getMessages().add(message);
    }
    public Integer getInvoiceCount() {
        return getInvoiceIds().size();
    }
    public Set<Integer> getOrderIds() {
        return orderIds;
    }
    public void setOrderIds(Set<Integer> orderIds) {
        this.orderIds = orderIds;
    }
    public void addOrderId(Integer orderId){
        getOrderIds().add(orderId);
    }
    public List<Integer> getInvoiceIds() {
        return invoiceIds;
    }
    public void setInvoiceIds(List<Integer> invoiceIds) {
        this.invoiceIds = invoiceIds;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void addInvoiceId(Integer invoiceId){
        getInvoiceIds().add(invoiceId);
    }
    @Override
    public String toString() {
        return String
                .format("MissingInvoice [orderId=%s, invoiceIds=%s, userId=%s, invoiceCount=%s, message=%s]",
                        orderIds, invoiceIds, userId,
                        invoiceIds.size(), messages);
    
    }
}