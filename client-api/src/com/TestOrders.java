package com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIException;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

public class TestOrders {

public static void main(String[] args) throws JbillingAPIException, IOException {
	
	
	JbillingAPI hessianApiClient 	= JbillingAPIFactory.getAPI("apiClient");
	getActiveOrderIds(hessianApiClient, 20773);
	
	
	
}	

public static List<OrderWS> getActiveOrderIds(JbillingAPI hessianApiClient, Integer userId) {
	
	List<OrderWS> list =  new ArrayList<>();
	
	Integer[] orderIds = hessianApiClient.getOrderByPeriod(userId, 700);
	for (Integer orderId : orderIds) {
		OrderWS orderWS = hessianApiClient.getOrder(orderId);
		if (orderWS.getOrderStatusWS().getDescription().equalsIgnoreCase("Active")) {
			list.add(orderWS);
		}
	}
	
	return getSortedOrders(list);
}

public static List<OrderWS> getSortedOrders(List<OrderWS> orderList ){
	List<OrderWS> list = new ArrayList<>();
	SortedMap<Date, OrderWS> sortedMap = new TreeMap<>();
	for(OrderWS order : orderList){
		sortedMap.put((null != order.getNextBillableDay()) ? order.getNextBillableDay() : order.getActiveSince(), order);
	}
	Set s = sortedMap.entrySet();
    // Using iterator in SortedMap
    Iterator i = s.iterator();
    // produced sorted (by keys) output .
    while (i.hasNext())
    {
        Map.Entry m = (Map.Entry)i.next();
        Date key = (Date)m.getKey();
        Integer value = ((OrderWS)m.getValue()).getId();
        System.out.println("Key : " + key +
                        "  value : " + value);
        list.add((OrderWS)m.getValue());
    }
	return list;
}

/**
 * Collections.sort(list, new Comparator<OrderWS>() {
		public int compare(OrderChangeWS o1, OrderChangeWS o2) {
			return o1.getNextBillableDate().compareTo(o2.getNextBillableDate());
		}
	});
 */
}


