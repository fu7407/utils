package com.zzf.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 * 从类似如下的文本文件中读取出所有的姓名，并打印出重复的姓名和重复的次数，并按重复次数排序：
 * 1,张三,28
 * 2,李四,35
 * 3,张三,28
 * 4,王五,35
 * 5,张三,28
 * 6,李四,35
 * 7,赵六,28
 * 8,田七,35
 * @author Administrator
 *
 */
public class GetNameTest {
	
	public static void main(String[] args) {
         Map<String,Integer> results = new HashMap<String,Integer>();
         InputStream ips = GetNameTest.class.getResourceAsStream("info.txt");
         BufferedReader in = new BufferedReader(new InputStreamReader(ips));
         String line = null;
         try {
               while((line=in.readLine())!=null){
                     dealLine(line,results);
               }
               sortResults(results);
         } catch (IOException e) {
               e.printStackTrace();
         }
    }

	   public static void dealLine(String line,Map<String,Integer> map){
	         if(!"".equals(line.trim())){
	               String [] results = line.split(",");
	               if(results.length == 3){
	                     String name = results[1];
	                     Integer value = (Integer)map.get(name);
	                     if(value == null) value = new Integer(0);
	                     map.put(name,new Integer(value.intValue()+1));
	               }
	         }
	   }

   static class User{
         public  String name;
         public Integer value;
         public User(String name,Integer value){
               this.name = name;
               this.value = value;
         }

         public boolean equals(Object obj) {
               boolean result = super.equals(obj);
               return result;
         }
   }

   

   private static void sortResults(Map<String,Integer> results) {
         TreeSet<User> sortedResults = new TreeSet<User>(
                     new Comparator(){
                           public int compare(Object o1, Object o2) {
                                 User user1 = (User)o1;
                                 User user2 = (User)o2;
                                 if(user1.value.intValue()<user2.value.intValue()){
                                       return -1;
                                 }else if(user1.value.intValue()>user2.value.intValue()){
                                       return 1;
                                 }else{
                                       return user1.name.compareTo(user2.name);
                                 }
                           }
                     }
         );
         Iterator<String> iterator = results.keySet().iterator();
         while(iterator.hasNext()){
               String name = (String)iterator.next();
               Integer value = (Integer)results.get(name);
               if(value.intValue() > 1){                       
                     sortedResults.add(new User(name,value));                    
               }
         }
         printResults(sortedResults);
   }

   private static void printResults(TreeSet<User> sortedResults) {
         Iterator<User> iterator  = sortedResults.iterator();
         while(iterator.hasNext()) {
               User user = (User)iterator.next();
               System.out.println(user.name + ":" + user.value);
         }     
   }

}
