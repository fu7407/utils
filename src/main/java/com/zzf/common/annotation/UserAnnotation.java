package com.zzf.common.annotation;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个类专门用来测试注解使用
 * @author tmser
 */
@TestA   //使用了类注解
public class UserAnnotation {
	@TestA //使用了类成员注解
    private Integer age;
    
    @TestA //使用了构造方法注解
    public UserAnnotation(){
        
    }
    @TestA //使用了类方法注解
    public void a(){
        @TestA //使用了局部变量注解
        Map m = new HashMap(0);
    }
    
    public void b(@TestA Integer a){ //使用了方法参数注解
        
    }

}
