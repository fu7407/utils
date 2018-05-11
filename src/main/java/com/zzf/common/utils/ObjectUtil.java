package com.zzf.common.utils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Properties;

import com.zzf.common.Person;
import com.zzf.common.PersonTemp;

/**
 * @author 张增福
 */
public class ObjectUtil {
	
	/**
	 * 从properties文件中读出某个关键字的值,参数用数组中的值替换
	 * @param fileName 
	 * @param key
	 * @param objs
	 * @return
	 */
    public static String getValueByKeyFromProperties(String fileName,String key,Object[] objs){
    	Properties p = new Properties();
		InputStream is = ObjectUtil.class.getResourceAsStream(fileName);
		try {
			p.load(is);
			String output = p.getProperty(key);
			if (!"".equals(output) && output != null) {
				if(objs!=null && objs.length > 0) {
					MessageFormat mf = new MessageFormat("");
					mf.applyPattern(output);
					return mf.format(objs);
				} else {
					return output;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return "";
    }
	
	/**
	 * 将目标对象dest所有String类型属性的值执行trim（）方法
	 * @param dest
	 * @return
	 */
	public static void trim(Object dest){
		if(dest==null) return;
		try{
			Field [] fields = dest.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getType().equals(String.class)) {//属性类型为String类型
					Object value = getProperty(dest, fields[i].getName());
					if(value!=null)
						setProperty(dest, fields[i].getName(),String.valueOf(value).trim());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 将源对象（sourceObj）的值复制到目标对象（targetObj）中，只复制相同属性的值(属性名和类型都相同)
	 * @param sourceObj 源对象
	 * @param targetObj 目标对象
	 */
	public static void copyIdenticalAttributes(Object sourceObj,Object targetObj){
		if(sourceObj==null || targetObj==null) return;
		try{
			Field [] sourceObjFields = sourceObj.getClass().getDeclaredFields();
			Field [] targetObjFields = targetObj.getClass().getDeclaredFields();
			for (int i = 0; i < sourceObjFields.length; i++) {
				for (int j = 0; j < targetObjFields.length; j++) {
					if(targetObjFields[j].getName().equals(sourceObjFields[i].getName())
							&& targetObjFields[j].getType().equals(sourceObjFields[i].getType())){//源对象和目标对象的属性名和类型都相同
						Object value = getProperty(sourceObj, sourceObjFields[i].getName());//得到源对象的属性值
						setProperty(targetObj,targetObjFields[j].getName(),value);//设置目标对象的属性值
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

 
	
	/**
	 * 获得对象某一属性的值
	 * @param dest 对象
	 * @param name 属性名
	 * @return 属性名对应的属性值
	 */
	public static Object getProperty(Object dest,String name){
		if(dest==null||name==null) return null;
		try{
			PropertyDescriptor pd = new PropertyDescriptor(name, dest.getClass());
			Method rMethod = pd.getReadMethod();//获得读方法
			return rMethod.invoke(dest, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得对象某一属性的值
	 * @param obj 对象
	 * @param attName 属性名
	 * @return 属性名对应的属性值
	 */
	public static Object getter(Object obj,String attName){
		try{
			Method met = obj.getClass().getMethod("get"+initStr(attName),null);
			return met.invoke(obj, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 设置对象某一属性的值
	 * @param dest 对象
	 * @param name 属性名
	 * @param value 属性值(请注意传入的参数类型是否与属性的类型匹配) 
	 */
	public static void setProperty(Object dest,String name,Object value){
		if(dest==null||name==null) return;
		try{
			PropertyDescriptor pd = new PropertyDescriptor(name, dest.getClass());
			Method wMethod = pd.getWriteMethod();//获得写方法
			wMethod.invoke(dest, new Object[]{value});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置对象某一属性的值
	 * @param obj 对象
	 * @param attName 属性名
	 * @param value 属性值
	 * @param type 属性的类型
	 */
	public static void setter(Object obj,String attName,Object value,Class type){
		try{
			Method met = obj.getClass().getMethod("set"+initStr(attName),new Class[]{type});
			met.invoke(obj, new Object[]{value});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 将字符串首字母大写
	 * @param str
	 * @return
	 */
	public static String initStr(String str){
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		Person p = new Person();
		p.setId("1  ");
		p.setName("zhangsan  ");
		p.setSex("  男  ");
		p.setSchool("  某某中学");
		System.out.println("id="+p.getId()+",age="+p.getAge()+",name="+p.getName()+",school="+p.getSchool()+",sex="+p.getSex()+",");
		ObjectUtil.trim(p);
		System.out.println("id="+p.getId()+",age="+p.getAge()+",name="+p.getName()+",school="+p.getSchool()+",sex="+p.getSex()+",");
		ObjectUtil.setProperty(p, "age",new Integer(123));
		System.out.println("age===="+ObjectUtil.getProperty(p,"age"));
		ObjectUtil.setter(p, "age",new Integer(234),int.class);
		System.out.println("age****"+ObjectUtil.getter(p,"age"));
		System.out.println("id="+p.getId()+",age="+p.getAge()+",name="+p.getName()+",school="+p.getSchool()+",sex="+p.getSex()+",");
		PersonTemp pt = new PersonTemp();
		ObjectUtil.copyIdenticalAttributes(p, pt);
		System.out.println("age="+pt.getAge()+",name="+p.getName()+",salary="+pt.getSalary()+",sex="+p.getSex()+",");
		
	}

}
