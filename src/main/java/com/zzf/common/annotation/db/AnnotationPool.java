package com.zzf.common.annotation.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 将annotation都定义在这个文件方便看
 * @author Administrator
 *
 */
public class AnnotationPool {

}

@Retention(RetentionPolicy.RUNTIME)   
@Target(ElementType.TYPE)   
@interface DbInfo  {  
    String url();  //数据库地址  
    String un();  //数据库连接用户名  
    String pw();  //数据库连接密码  
    String tableName();  //model对应数据表  
}  
  
@Retention(RetentionPolicy.RUNTIME)   
@Target(ElementType.FIELD)   
@interface Id  {  
    String column();  //数据库对应字段  
    String describe();  //字段描述  
    String generator() default "uuid";  //id生成方式，默认是uuid  
}  
  
@Retention(RetentionPolicy.RUNTIME)   
@Target(ElementType.FIELD)   
@interface columns  {  
    String type();  //数据库类型  
    String column();  //数据库对应字段  
    int length() default 200;  //数据库字段长度  
    String describe();  //字段描述  
}  
