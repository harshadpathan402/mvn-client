package com;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.ArrayUtils;
import org.joda.time.DateTime;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.order.OrderChangeWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIException;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;


public class TestAmaysimClient  implements Runnable  {
	
	
	private static HostnameVerifier allowAllHosts() {

		try {

			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}}
			}; 

			SSLContext sc = SSLContext.getInstance("SSL");

			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					// TODO Auto-generated method stub
					return true;
				}
			};


			return allHostsValid;


		} catch(Exception ex) {

			return null;

		}

	}

	public static Set<Integer> SUCCESFULL_USERS = new HashSet<Integer>();
	public static Set<Integer> FAILED_USER = new HashSet<Integer>();
	public static Set<Integer> INVOICES_NOT_GENERATRED_ORDER_IDS = new HashSet<Integer>();
	public static Set<Integer> INVOICES_GENERATRED_IDS = new HashSet<Integer>();
	public static Set<Integer> ONE_DAY_BILLABLE_ORDER_IDS = new HashSet<Integer>();
	public static Set<Integer> FAILED_TO_UPDATE_NID = new HashSet<Integer>();
	public static Integer REMOVED_STATUS_ID = 602;
	private static List<MissingInvoice> MISSING_INVOICES_LIST = new ArrayList<>();
	
	public Set<Integer> USERS = new HashSet<Integer>();
	public static JbillingAPI hessianApiClient 	= null;
	
	public TestAmaysimClient(List<Integer> userIds){
		System.out.println("TestAmaysimClient.TestAmaysimClient()"+userIds.size());
		USERS = new HashSet<>(userIds);
	}
	
	private static final String STRING_ARRAY_SAMPLE = "/home/jbilling/Documents/amaysim/output/";
	
	public static void main(String[] args) throws JbillingAPIException, IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		allowAllHosts();
		hessianApiClient 	= JbillingAPIFactory.getAPI("apiClient");
		
		
		long startTime = System.nanoTime();
		Integer[] orderIds = {
				// these orders where included later
				48444,56438,68863,69184,69962,72414,44971,
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
			Set<Integer> userIds = getUsersList(hessianApiClient, orderIds);
			List<Integer> userList = new ArrayList<>();
			userList.addAll(userIds);
			List<List<Integer>> partUserIds = partition(userList, 10);
			ExecutorService executor = Executors.newFixedThreadPool(5);
			for(List<Integer> subList : partUserIds){
	            Runnable worker = new TestAmaysimClient(subList);
	            executor.execute(worker);
			}
			executor.shutdown();
		    while (!executor.isTerminated()) {
		    }
			System.out.println("Total Users : "+userIds.size());
			updateStatusToRemoved(hessianApiClient);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		//System.out.println("Total Order Ids :: " + orderIds.length);
		
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
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println("Total time to execute : "+totalTime);
	}
	
	private static Set<Integer> getUsersList(JbillingAPI hessianApiClient, Integer[] orderIds){
		System.out.println("TestAmaysimClient.getUsersList()");
		Set<Integer> users = new HashSet<>();
		for(Integer orderId : orderIds){
			users.add(hessianApiClient.getOrder(orderId).getUserId());
		}
		return users;
	}

	public static void createInvoiceForUsers(JbillingAPI hessianApiClient,
			 Set<Integer> userIds) {
		for (Integer userId : userIds) {
			System.out.println("users completed count============================================================================="+SUCCESFULL_USERS.size());
				List<OrderWS> orders = getActiveOrders(hessianApiClient, userId);
				
				if(!orders.isEmpty()){
					OrderWS orderWS = orders.get(0);
					Integer orderId = orderWS.getId();
					
					MissingInvoice missingInvoice = new MissingInvoice();
					missingInvoice.setUserId(userId);
					List<Date> skippableDates = checkActiveUntilNBD(orders);
					Date invoiceGenerationStartDate = null != orderWS.getNextBillableDay()?
							orderWS.getNextBillableDay(): orderWS.getActiveSince();
					if(!orderWS.getStatusStr().equalsIgnoreCase("finished")){
						while (invoiceGenerationStartDate.before(new Date())) {
							
							//orderWS = hessianApiClient.getOrder(orderId);
							UserWS userWS = hessianApiClient.getUserWS(orderWS.getUserId());
							
							if(skippableDates.contains(invoiceGenerationStartDate)){
								missingInvoice.addMessage("Skipping invoice generation for date : "+invoiceGenerationStartDate);
								invoiceGenerationStartDate = plusOneDay(invoiceGenerationStartDate);
								updateNID(hessianApiClient, userId, invoiceGenerationStartDate);
								System.out.println("user ID " + userId+ " NID : " + hessianApiClient.getUserWS(userId).getNextInvoiceDate());
							} else {
								System.out.println("invoiceGenerationDate :: " + invoiceGenerationStartDate );
								System.out.println("next invoice date :: "+ userWS.getNextInvoiceDate());
								try {
									updateNID(hessianApiClient, userId, invoiceGenerationStartDate);
									Integer[] invoiceIds = hessianApiClient.createInvoiceWithDate(userId, invoiceGenerationStartDate, null, null, false);
									if (ArrayUtils.isNotEmpty(invoiceIds)) {
										InvoiceWS invoice = hessianApiClient.getInvoiceWS(invoiceIds[0]) ;
										missingInvoice.addInvoiceId(invoice.getId());
										for(Integer iOrderId : invoice.getOrders()){
											missingInvoice.addOrderId(iOrderId);
										}
										INVOICES_GENERATRED_IDS.add(invoice.getId());
										//missingInvoicesUser.addMissingInvoice(missingInvoice);
										SUCCESFULL_USERS.add(userId);
									}else {
										System.out.println("invoice not generated for " + userId + "for date : " +  invoiceGenerationStartDate);
										INVOICES_NOT_GENERATRED_ORDER_IDS.add(orderId);
									}
								} catch (Exception e) {
									System.out.println("User failed : " + userId);
									FAILED_USER.add(userId);
									missingInvoice.addMessage(e.getMessage());
									e.printStackTrace();
								}
								invoiceGenerationStartDate = plusOneDay(invoiceGenerationStartDate);
							}
							
						}
					} else {
						missingInvoice.addMessage("Order is finished");
					}
					MISSING_INVOICES_LIST.add(missingInvoice);
			}
		}
	}
	public static Date plusOneDay(Date invoiceGenerationStartDate) {
		DateTime dtOrg = new DateTime(invoiceGenerationStartDate);
		DateTime dtPlusOne = dtOrg.plusDays(1);
		return dtPlusOne.toDate();
	}
	
	
	public static List<Date> checkActiveUntilNBD(List<OrderWS> orders){
		List<Date> skippableDates = new ArrayList<>();
		for(OrderWS orderWS : orders){
			if(null != orderWS.getActiveUntil() && orderWS.getActiveUntil().equals(null != orderWS.getNextBillableDay()?
					orderWS.getNextBillableDay(): orderWS.getActiveSince())){
				skippableDates.add(orderWS.getActiveUntil());
				ONE_DAY_BILLABLE_ORDER_IDS.add(orderWS.getId());
			}
		}
		return skippableDates;
	}

	public static void updateStatusToRemoved(JbillingAPI hessianApiClient) {
		for (Integer userId : SUCCESFULL_USERS) {
			try {
				UserWS userWS = hessianApiClient.getUserWS(userId);
			//	userWS.setNextInvoiceDate(new Date());
				userWS.setStatusId(REMOVED_STATUS_ID);
				System.out.println("userId :: "+userId);
				hessianApiClient.updateUser(userWS);
				userWS = hessianApiClient.getUserWS(userId);
				System.out.println("userId :: "+userId +"  "+ "user status :: " + userWS.getStatus());
			} catch (Exception e) {
				FAILED_TO_UPDATE_NID.add(userId);
				e.printStackTrace();
			}
		}
	}
	
	public static List<OrderWS> getActiveOrders(JbillingAPI hessianApiClient, Integer userId) {
		
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

	@Override
	public void run() {
		try {
			createInvoiceForUsers(hessianApiClient, USERS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static <T> List<List<T>> partition(List<T> list, int batchSize) {
		List<List<T>> parts = new ArrayList<List<T>>();
		int size = list.size();
		for (int i = 0; i < size; i += batchSize) {
			parts.add(new ArrayList<T>(
					list.subList(i, Math.min(size, i + batchSize)))
					);
		}
		return parts;
	}
    
}	