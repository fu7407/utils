package com.zzf.common.googlepr;

import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Copyright 2008
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class GooglePageRank {
    // google pagerank服务器ip地址列表（最近google小气了很多，反复查询�?��封ip�?
    final static String[] GoogleServiceIP = new String[] { "toolbarqueries.google.com" };
    // google用识别标�?
    final static private int GOOGLE_MAGIC = 0xE6359A60;
    // ch数�?混合�?
    private class CHMix {
        int a;
        int b;
        int c;
        public CHMix() {
            this(0, 0, 0);
        }
        public CHMix(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
    /**
     * 按google要求混合成ch数据
     * 
     * @param mix
     */
    private static void mix(final CHMix mix) {
        mix.a -= mix.b;
        mix.a -= mix.c;
        mix.a ^= mix.c >> 13;
        mix.b -= mix.c;
        mix.b -= mix.a;
        mix.b ^= mix.a << 8;
        mix.c -= mix.a;
        mix.c -= mix.b;
        mix.c ^= mix.b >> 13;
        mix.a -= mix.b;
        mix.a -= mix.c;
        mix.a ^= mix.c >> 12;
        mix.b -= mix.c;
        mix.b -= mix.a;
        mix.b ^= mix.a << 16;
        mix.c -= mix.a;
        mix.c -= mix.b;
        mix.c ^= mix.b >> 5;
        mix.a -= mix.b;
        mix.a -= mix.c;
        mix.a ^= mix.c >> 3;
        mix.b -= mix.c;
        mix.b -= mix.a;
        mix.b ^= mix.a << 10;
        mix.c -= mix.a;
        mix.c -= mix.b;
        mix.c ^= mix.b >> 15;
    }
    /**
     * 获得ch数�?混合�?
     * 
     * @return
     */
    public static CHMix getInnerCHMix() {
        return new GooglePageRank().new CHMix();
    }
    /**
     * 通过url获得googlech(google数据库针对页面的全球唯一标识)
     * 
     * @param url
     * @return
     */
    public static String GoogleCH(final String url) {
        // 格式化为google要求的info:url模式
        String nUrl = String.format("info:%s", new Object[] { url });
        // 获得新url字符串格�?
        char[] urls = nUrl.toCharArray();
        // 获得新url长度
        int length = urls.length;
        // 获得�?��ch数�?混合�?
        CHMix chMix = GooglePageRank.getInnerCHMix();
        // 为c注入google识别标识
        chMix.c = GOOGLE_MAGIC;
        // 为a、b项注入google要求的初始标�?
        chMix.a = chMix.b = 0x9E3779B9;
        int k = 0;
        int len = length;
        while (len >= 12) {
            chMix.a += (int) (urls[k + 0] + (urls[k + 1] << 8)
                    + (urls[k + 2] << 16) + (urls[k + 3] << 24));
            chMix.b += (int) (urls[k + 4] + (urls[k + 5] << 8)
                    + (urls[k + 6] << 16) + (urls[k + 7] << 24));
            chMix.c += (int) (urls[k + 8] + (urls[k + 9] << 8)
                    + (urls[k + 10] << 16) + (urls[k + 11] << 24));
            // 获得混合运算后的数据
            GooglePageRank.mix(chMix);
            k += 12;
            len -= 12;
        }
        chMix.c += length;
        // 产生googlech�?1位标�?
        switch (len) {
        case 11:
            chMix.c += (int) (urls[k + 10] << 24);
        case 10:
            chMix.c += (int) (urls[k + 9] << 16);
        case 9:
            chMix.c += (int) (urls[k + 8] << 8);
        case 8:
            chMix.b += (int) (urls[k + 7] << 24);
        case 7:
            chMix.b += (int) (urls[k + 6] << 16);
        case 6:
            chMix.b += (int) (urls[k + 5] << 8);
        case 5:
            chMix.b += (int) (urls[k + 4]);
        case 4:
            chMix.a += (int) (urls[k + 3] << 24);
        case 3:
            chMix.a += (int) (urls[k + 2] << 16);
        case 2:
            chMix.a += (int) (urls[k + 1] << 8);
        case 1:
            chMix.a += (int) (urls[k + 0]);
            break;
        default:
            break;
        }
        // 获得混合运算后的数据
        GooglePageRank.mix(chMix);
        // 获得未修订的CH
        String tch = String.valueOf(chMix.c);
        // 矫正差�?后反馈正确CH
        return String
                .format("6%s", new Object[] { tch.length() < 10 ? ("-" + tch)
                        .intern() : tch });
    }
    /**
     * 正则匹配pagerank结果
     * 
     * @param value
     * @return
     */
    private static String MatchRank(final String value) {
        Pattern pattern = Pattern.compile("Rank_1:[0-9]:([0-9]+)");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "0";
    }
    /**
     * 获得指定页面的google pagerank�?
     * 
     * @param url
     * @return
     */
    public static String GooglePR(final String url) {
        String rip = GoogleServiceIP[new Random()
                .nextInt(GoogleServiceIP.length)];
        return GooglePR(url, rip);
    }
    /**
     * 以指定的google服务器获得指定页面的google pagerank�?
     * 
     * @param url
     * @param ip
     * @return
     */
    public static String GooglePR(final String url, final String ip) {
        // 产生查询用唯�?���?
        String checksum = GoogleCH(url);
        // 产生查询用url
        String queryUrl = String
                .format(
                        "http://%s/search?client=navclient-auto&ch=%s&features=Rank&q=info:%s",
                        new Object[] { ip, checksum, url });
        String response;
        try {
        	System.out.println("***********queryUrl="+queryUrl);
            response = SimpleWebClient.getRequestHttp(queryUrl);
        } catch (IOException e) {
            response = "";
        }
        if (response.length() == 0) {
            return "0";
        } else {
            return GooglePageRank.MatchRank(response);
        }
    }
}
