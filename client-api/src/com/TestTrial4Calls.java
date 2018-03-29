package com;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ArrayUtils;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.order.OrderChangeWS;
import com.sapienter.jbilling.server.order.OrderPeriodWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIException;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

public class TestTrial4Calls {

	public static Set<Integer> SUCCESFULL_USERS = new HashSet<Integer>();
	public static Set<Integer> FAILED_USER = new HashSet<Integer>();
	public static Set<Integer> INVOICES_NOT_GENERATRED_ORDER_IDS = new HashSet<Integer>();
	public static Set<Integer> INVOICES_GENERATRED_IDS = new HashSet<Integer>();
	public static Set<Integer> ONE_DAY_BILLABLE_ORDER_IDS = new HashSet<Integer>();
	public static Set<Integer> FAILED_TO_UPDATE_NID = new HashSet<Integer>();
	public static Integer REMOVED_STATUS_ID = 602;
	private static List<MissingInvoice> MISSING_INVOICES_LIST = new ArrayList<>();
	
	private static final String STRING_ARRAY_SAMPLE = "/home/jbilling/Documents/amaysim/output/";
	
	public static void main(String[] args) throws JbillingAPIException, IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		JbillingAPI hessianApiClient 	= JbillingAPIFactory.getAPI("apiClient");
		Map<Integer, Integer> userToInvoiceCount = new HashMap<Integer, Integer>();
		
		Integer[] orderIds = {
				//41302,57680
				//first Sheet
				41302,41610,42049,42311,42318,42428,42556,42734,42745,42749,42781,42804,42875,42945,42953,43074,43301,43338,43379,43395,
				43434,43505,43695,43797,43872,43916,44073,44201,44301,44304,44326,44432,44472,44506,44521,44541,44658,44668,44973,45017,
				45026,45070,45086,45090,45172,45237,45276,45295,45401,45564,45570,45578,45625,45666,45728,45736,45805,46009,46144,46167,
				46233,46255,46447,46504,46731,46848,46850,46911,46919,47033,47034,47064,47116,47234,47355,47531,47547,47627,47753,47895,
				47917,47960,47981,48000,48046,48056,48065,48173,48471,48662,48773,48950,49287,49308,49443,49538,49552,49578,49703,49704,
				49737,49748,49994,50081,50250,50305,50311,50331,50884,50998,51067,51138,51214,51469,51494,51717,51799,51801,52106,52175,
				52265,52489,52715,52837,52939,53082,53125,53154,53163,53192,53239,53304,53374,53599,53647,53655,53666,53718,53737,53809,
				53889,53959,53960,53972,54006,54081,54106,54125,54169,54278,54280,54281,54344,54353,54374,54440,54678,54709,54729,54843,
				54901,55065,55089,55101,55139,55197,55223,55419,55453,55687,55803,55909,55940,56469,56474,56480,56626,56662,56688,56795,
				56851,56854,56857,56858,56935,56942,57208,57316,57350,57401,57581,57754,57884,58301,58410,58412,58622,58688,58714,58903,
				58932,58997,59378,59383,59656,59729,59794,60039,60078,60150,60240,60527,60567,60920,61228,61438,61678,61794,61960,62127,
				62186,62189,62460,62509,62639,62806,62924,62938,63007,63219,63467,63615,63913,64092,64383,64426,64601,65049,65055,65076,
				65162,65344,65373,65683,66002,66408,66493,66659,66806,67103,67116,67165,67407,67779,68176,68203,68355,68781,68825,68876,
				69107,69246,69285,69469,69887,70066,70106,70475,71195,71385,71433,71591,71702,72252,72581,72797,72865,72939,73062,73204,
				73893,74296,74591,
				//Second Sheet
				41410,45471,45544,47771,48058,48193,52770,52834,55303,57680,58669,58775,59071,59123,59862,61066,61067,61523,61727,
				63384,63540,64737,64810,65056,65326,65368,65376,65668,65713,65950,66051,66073,66299,66347,66614,66940,66967,67186,
				67409,67462,67797,67824,67995,68073,68154,68191,68506,70021,70109,71112,71126,71190,71611,71700,72188,72343,72393,
				72395,72501,72589,73390,74009,74143,74149,74485,74500
				
				};
		try{
			createInvoiceForOrders(hessianApiClient, userToInvoiceCount, orderIds);
			updateStatusToRemoved(hessianApiClient);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		System.out.println("Total Order Ids :: " + orderIds.length);
		
		System.out.println("SUCCESFULL_USERS count :: " + SUCCESFULL_USERS.size());
		printUsers("successful user", SUCCESFULL_USERS);
		
		System.out.println("FAILED_USER count :: " + FAILED_USER.size());
		printUsers("failed user", FAILED_USER);
		
		System.out.println("INVOICES_NOT_GENERATRED_ORDER_IDS count :: " + INVOICES_NOT_GENERATRED_ORDER_IDS.size());
		printUsers("order", INVOICES_NOT_GENERATRED_ORDER_IDS);
		
		System.out.println("INVOICES_GENERATRED_IDS count :: " + INVOICES_GENERATRED_IDS.size());
		printUsers("success invoice", INVOICES_GENERATRED_IDS);
		
		System.out.println("FAILED_TO_UPDATE_NID count :: " + FAILED_TO_UPDATE_NID.size());
		printUsers("FAILED_TO_UPDATE_NID", FAILED_TO_UPDATE_NID);
		System.out.println("===============================================================================================");
		for(MissingInvoice missingInvoice : MISSING_INVOICES_LIST){
			System.out.println(missingInvoice.toString());
		}
		
		writeToCSV(MISSING_INVOICES_LIST, "MISSING_INVOICES_LIST");
		/*writeToCSV(INVOICES_NOT_GENERATRED_ORDER_IDS, "INVOICES_NOT_GENERATRED_ORDER_IDS");
		writeToCSV(FAILED_USER, "FAILED_USER");
		writeToCSV(SUCCESFULL_USERS, "SUCCESFULL_USERS");
		writeToCSV(INVOICES_GENERATRED_IDS, "INVOICES_GENERATRED_IDS");
		writeToCSV(FAILED_TO_UPDATE_NID, "FAILED_TO_UPDATE_NID");*/
	}

	public static void createInvoiceForOrders(JbillingAPI hessianApiClient,
			Map<Integer, Integer> userToInvoiceCount, Integer[] orderIds) {
		for (Integer orderId : orderIds) {
			MissingInvoice missingInvoice = new MissingInvoice();
			OrderWS orderWS = hessianApiClient.getOrder(orderId);
			Integer count = !orderWS.getStatusStr().equalsIgnoreCase("finished")? getInvoiceCount(orderWS):-1;
			
			System.out.println("Diff in months :: " + count);
			userToInvoiceCount.put(orderWS.getUserId(), count);
			//missingInvoice.setOrderId(orderId);
		//	missingInvoice.setExpectedCount(count);
			missingInvoice.setUserId(orderWS.getUserId());
			if(null != orderWS.getActiveUntil() && orderWS.getActiveUntil().equals(null != orderWS.getNextBillableDay()?
					orderWS.getNextBillableDay(): orderWS.getActiveSince())){
				missingInvoice.addMessage("One Day Billable Order");
				ONE_DAY_BILLABLE_ORDER_IDS.add(orderWS.getId());
			}
			if(!orderWS.getStatusStr().equalsIgnoreCase("finished")){
			while (count > 0) {
				--count;
				orderWS = hessianApiClient.getOrder(orderId);
				Date invoiceGenerationDate = null != orderWS.getNextBillableDay()?
						orderWS.getNextBillableDay(): orderWS.getActiveSince();
				UserWS userWS = hessianApiClient.getUserWS(orderWS.getUserId());
				
				
					System.out.println("invoiceGenerationDate :: " + invoiceGenerationDate );
					System.out.println("next invoice date :: "+ userWS.getNextInvoiceDate());
					try {
						if(null != orderWS.getActiveUntil() && orderWS.getActiveUntil().equals(invoiceGenerationDate)){
							missingInvoice.addMessage("One Day Billable Order");
							ONE_DAY_BILLABLE_ORDER_IDS.add(orderWS.getId());
						}else{
							updateNID(hessianApiClient, userWS.getId(), invoiceGenerationDate);
							Integer[] invoiceIds = hessianApiClient.createInvoiceWithDate(userWS.getId(), invoiceGenerationDate, null, null, false);
							if (ArrayUtils.isNotEmpty(invoiceIds)) {
								InvoiceWS invoice = hessianApiClient.getInvoiceWS(invoiceIds[0]) ;
								missingInvoice.addInvoiceId(invoice.getId());
								INVOICES_GENERATRED_IDS.add(invoice.getId());
								SUCCESFULL_USERS.add(userWS.getId());
							}else {
								System.out.println("invoice not generated for " + userWS.getId() + "for order : " +  orderId);
								INVOICES_NOT_GENERATRED_ORDER_IDS.add(orderId);
								missingInvoice.addMessage("INVOICES_NOT_GENERATRED_ORDER_ID -- " + orderWS.getPeriodStr());
							}
						}
					} catch (Exception e) {
						System.out.println("User faild : " + userWS.getId());
						FAILED_USER.add(userWS.getId());
						missingInvoice.addMessage(e.getMessage());
						e.printStackTrace();
					}
					
			}
			} else {
				missingInvoice.addMessage("Order is finished");
			}
			MISSING_INVOICES_LIST.add(missingInvoice);
		}
	}

	public static void updateStatusToRemoved(JbillingAPI hessianApiClient) {
		for (Integer userId : SUCCESFULL_USERS) {
			try {
				UserWS userWS = hessianApiClient.getUserWS(userId);
			//	userWS.setNextInvoiceDate(new Date());
				userWS.setStatusId(REMOVED_STATUS_ID);
				System.out.println("userId :: "+userWS.getId());
				hessianApiClient.updateUser(userWS);
				userWS = hessianApiClient.getUserWS(userId);
				System.out.println("userId :: "+userWS.getId() +"  "+ "user status :: " + userWS.getStatus());
			} catch (Exception e) {
				FAILED_TO_UPDATE_NID.add(userId);
				e.printStackTrace();
			}
		}
	}

	public static Integer getInvoiceCount(OrderWS orderWS) {
		Integer count;
		if (null != orderWS.getActiveUntil() && orderWS.getActiveUntil().before(new Date())) {
			 count= getDiffInMonths(null != orderWS.getNextBillableDay()?
						orderWS.getNextBillableDay(): orderWS.getActiveSince(),orderWS.getActiveUntil());	
		}else {
			count= getDiffInMonths(null != orderWS.getNextBillableDay()?
					orderWS.getNextBillableDay(): orderWS.getActiveSince(),new Date());	
		}
		return count;
	}

	public static void printUsers(String message, Set<Integer> ids) {
		for (Integer id : ids) {
			System.out.println(message +" : "+ id);
		}
	}

	public static List<OrderChangeWS> getSorrtedOrderChanges(
			JbillingAPI hessianApiClient, Integer orderId) {
		OrderChangeWS[] orderChangeWSs = hessianApiClient.getOrderChanges(orderId);
		List<OrderChangeWS> list =  Arrays.asList(orderChangeWSs);

		Collections.sort(list, new Comparator<OrderChangeWS>() {
			public int compare(OrderChangeWS o1, OrderChangeWS o2) {
				return o1.getNextBillableDate().compareTo(o2.getNextBillableDate());
			}
		});
		return list;
	}
	
	public static Integer getDiffInMonths(Date orderNextBillableDay,Date activeUntillDate) {
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(orderNextBillableDay);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(activeUntillDate);

		Integer diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		Integer count = 0;
		count = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		if (count==0) {
		return getDifferenceDays(activeUntillDate, orderNextBillableDay) >= 1 ? 1 : 0 ;
		}
		return count;
	}
	
    public static void writeToCSV(List<MissingInvoice> invoices,String fileName) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException{
    	try (Writer writer = Files.newBufferedWriter(Paths.get(createFile(fileName)));) {
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                		.withSeparator(',')
                        .build();
                beanToCsv.write(invoices);
            }catch (Exception e) {
				e.printStackTrace();
			}
    	
    }
    
    public static String createFile(String fileName) throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String filePath = STRING_ARRAY_SAMPLE;
		fileName = new StringBuilder()
					.append(filePath)
					.append(fileName)
					.append("-")
					.append(format.format(new Date())).toString();
		fileName = fileName +".csv";
		return fileName;
	}
    

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
    
    public static void updateNID(JbillingAPI hessianApiClient,Integer userId,Date invoiceGenerationDate){
    	
    	UserWS userWS = hessianApiClient.getUserWS(userId);
    	userWS.setNextInvoiceDate(invoiceGenerationDate);
    	hessianApiClient.updateUser(userWS);
    	userWS = hessianApiClient.getUserWS(userId);
    }
    
}	
