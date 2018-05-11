package com.zzf.common.safe;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Safe {
	
	/**
	 * 产生密钥
	 *
	 */
	public void key_res(){
		try {
//			Blowfish(32至448之间可以被8整除的数)、DES(56)、DESede(112/168)、HmacMD5(64)、HmacSHA1(64)、AES(128/192/256)
			KeyGenerator kg = KeyGenerator.getInstance("DESede");
			kg.init(168);
			SecretKey k = kg.generateKey();
//			System.out.println(k.getFormat());
//			byte [] a = k.getEncoded();
//			for(int i=0;i<a.length;i++){
//				System.out.print(a[i]+",");
//			}
			FileOutputStream f = new FileOutputStream("d:\\key1.dat");
			ObjectOutputStream b = new ObjectOutputStream(f);
			b.writeObject(k);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取密钥进行加密
	 *
	 */
	public void key_kb(){
		try {
			//从文件中获取密钥
			FileInputStream f = new FileInputStream("d:\\key1.dat");
			ObjectInputStream b = new ObjectInputStream(f);
			Key k = (Key)b.readObject();
			byte [] a = k.getEncoded();
			for(int i=0;i<a.length;i++){
				System.out.print(a[i]+",");
			}
			//创建密码器
			Cipher cp = Cipher.getInstance("DESede");
			//初始化密码器
			cp.init(Cipher.ENCRYPT_MODE, k);
			//获取要加密的明文
			String s="hello world!";
			byte [] ptext = s.getBytes("UTF8");
			//执行加密
			byte [] ctext = cp.doFinal(ptext);
			//处理加密结果
			FileOutputStream f2 = new FileOutputStream("d:\\key2.dat");
			f2.write(ctext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解密加密的文件
	 *
	 */
	public void sDec(){
		try{
			// 获取密文
			FileInputStream f = new FileInputStream("d:\\key2.dat");
			int num = f.available();
			byte[] ctext = new byte[num];
			f.read(ctext);
			// 获取密钥
			FileInputStream f2 = new FileInputStream("d:\\key1.dat");
			int num2 = f2.available();
			byte[] keykb = new byte[num2];
			f2.read(keykb);
			SecretKeySpec k = new SecretKeySpec(keykb, "DESede");
			// 解密
			Cipher cp = Cipher.getInstance("DESede");
			cp.init(Cipher.DECRYPT_MODE, k);
			byte[] ptext = cp.doFinal(ctext);
			// 显示明文
			String p = new String(ptext, "UTF8");
			System.out.println(p); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Safe safe = new Safe();
		safe.key_res();
	}

}
