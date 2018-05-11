package com.zzf.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Json {
	 /**
    * @param obj任意对象
    * @return String
    */
   public static String object2json(Object obj) {
   	StringBuffer json = new StringBuffer();
       if (obj == null) {
           json.append("\"\"");
       } else if (obj instanceof String || obj instanceof Integer || obj instanceof Float 
               || obj instanceof Short || obj instanceof Double || obj instanceof Long || obj instanceof BigDecimal
               || obj instanceof BigInteger || obj instanceof Byte) {
           json.append("\"").append(string2json(obj.toString())).append("\"");
       } else if ( obj instanceof Boolean) {
           json.append(string2json(obj.toString()));
       } else if (obj instanceof Object[]) {
           json.append(array2json((Object[]) obj));
       } else if (obj instanceof List) {
           json.append(list2json((List) obj));
       } else if (obj instanceof Map) {
           json.append(map2json((Map) obj));
       } else if (obj instanceof Set) {
           json.append(set2json((Set) obj));
       } else {
           json.append(bean2json(obj));
       }
       return json.toString();
   }
   /**
    * @param bean bean对象
    * @return String
    */
   public static String bean2json(Object bean) {
   	StringBuffer json = new StringBuffer();
       json.append("{");
       Field [] fields = bean.getClass().getDeclaredFields();
       if (fields != null) {
           for (int i = 0; i < fields.length; i++) {
               try {
                   json.append(fields[i].getName());
                   json.append(":");
                   PropertyDescriptor pd = new PropertyDescriptor(fields[i].getName(), bean.getClass());
                   json.append(object2json(pd.getReadMethod().invoke(bean,null)));
                   json.append(",");
               } catch (Exception e) {
               }
           }
           json.setCharAt(json.length() - 1, '}');
       } else {
           json.append("}");
       }
       return json.toString();
   }
   /**
    * @param list list对象
    * @return String
    */
   public static String list2json(List list) {
   	StringBuffer json = new StringBuffer();
       json.append("[");
       if (list != null && list.size() > 0) {
           for (int i=0;i<list.size();i++) {
           	Object obj = list.get(i);
               json.append(object2json(obj));
               json.append(",");
           }
           json.setCharAt(json.length() - 1, ']');
       } else {
           json.append("]");
       }
       return json.toString();
   }
   /**
    * @param array 对象数组
    * @return String
    */
   public static String array2json(Object[] array) {
   	StringBuffer json = new StringBuffer();
       json.append("[");
       if (array != null && array.length > 0) {
           for (int i=0;i<array.length;i++) {
               json.append(object2json(array[i]));
               json.append(",");
           }
           json.setCharAt(json.length() - 1, ']');
       } else {
           json.append("]");
       }
       return json.toString();
   }
   /**
    * @param map map对象
    * @return String
    */
   public static String map2json(Map map) {
   	StringBuffer json = new StringBuffer();
       json.append("{");
       if (map != null && map.size() > 0) {
       	Iterator it = map.entrySet().iterator();   
           while (it.hasNext()) {   
               Map.Entry entry = (Map.Entry) it.next();   
               json.append(object2json(entry.getKey()));
               json.append(":");
               json.append(object2json(entry.getValue()));
               json.append(",");  
         
           }
           json.setCharAt(json.length() - 1, '}');
       } else {
           json.append("}");
       }
       return json.toString();
   }
   /**
    * @param set 集合对象
    * @return String
    */
   public static String set2json(Set set) {
   	StringBuffer json = new StringBuffer();
       json.append("[");
       if (set != null && set.size() > 0) {
       	Iterator it = set.iterator();
       	while(it.hasNext()){
       		json.append(object2json(it.next()));
               json.append(",");
       	}
           json.setCharAt(json.length() - 1, ']');
       } else {
           json.append("]");
       }
       return json.toString();
   }
   /**
    * @param s 参数
    * @return String
    */
   public static String string2json(String s) {
       if (s == null)
           return "";
       StringBuffer sb = new StringBuffer();
       for (int i = 0; i < s.length(); i++) {
           char ch = s.charAt(i);
           switch (ch) {
           case '"':
               sb.append("\\\"");
               break;
           case '\\':
               sb.append("\\\\");
               break;
           case '\b':
               sb.append("\\b");
               break;
           case '\f':
               sb.append("\\f");
               break;
           case '\n':
               sb.append("\\n");
               break;
           case '\r':
               sb.append("\\r");
               break;
           case '\t':
               sb.append("\\t");
               break;
           case '/':
               sb.append("\\/");
               break;
           default:
               if (ch >= '\u0000' && ch <= '\u001F') {
                   String ss = Integer.toHexString(ch);
                   sb.append("\\u");
                   for (int k = 0; k < 4 - ss.length(); k++) {
                       sb.append('0');
                   }
                   sb.append(ss.toUpperCase());
               } else {
                   sb.append(ch);
               }
           }
       }
       return sb.toString();
   }
}
