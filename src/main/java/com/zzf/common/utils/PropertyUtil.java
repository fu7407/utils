package com.zzf.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *  加载资源文件
 */
public class PropertyUtil {
    private static final String PARAMS_FILE = "params.properties";
    //单例模式
    private static PropertyUtil propertyUtil = null;

    private Map<String, String> propertyMap = null;

    /**
     * @function 获取该类实例对象
     * @return
     */
    public static PropertyUtil getInstance() {
        if (propertyUtil == null) {
            propertyUtil = new PropertyUtil();
        }
        return propertyUtil;
    }

    /**
     * 返回String
     * @param key
     * @return
     */
    public String getValue(String key) {
        String tempStr = propertyUtil.getMap().get(key);
        return tempStr == null ? "" : tempStr;
    }

    /**
     * 返回Map
     * @return
     */
    public Map<String, String> getMap() {
        return propertyMap;
    }

    /**
     * 构造方法
     */
    private PropertyUtil() {
        InputStream inStream = null;
        Properties property = null;
        try {
            inStream = PropertyUtil.class.getClassLoader().getResourceAsStream(PARAMS_FILE);
            property = new Properties();
            property.load(inStream);
            propertyMap = new HashMap<String, String>();
            Enumeration<?> enumer = property.keys();
            while (enumer.hasMoreElements()) {
                String keyName = (String) enumer.nextElement();
                propertyMap.put(keyName, property.getProperty(keyName));
            }
        }
        catch (Exception e) {
            new Throwable(e);
        }
        finally {
            if (inStream != null) {
                try {
                    inStream.close();
                }
                catch (IOException e) {
                    new Throwable(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(PropertyUtil.getInstance().getValue("email_user_name"));
    }

}
