package com.mailian.crawl.area;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: wangqiaoqing
 * @Date: 2018/10/25
 * @Description:
 */
public class CrawlArea {
    /* 采集地址 */
    private static final String AREA_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/index.html";


    private static final String CITY_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/%s";

    private static BufferedWriter bufferedWriter;
    private static final String INSERT_SQL = "insert into `t_sys_area` values (%s,'%s','%s',%s,%s,0,now(),now(),'system','system');";
    private static Integer id = 1;

    static ExecutorService executorService = Executors.newFixedThreadPool(10);


    private static String pname = "";
    private static String ccode = "";

    public static void main(String[] args) throws IOException {
        //cl();

        test();
        /*Document doc = Jsoup.connect(String.format(CITY_URL,"44/4403.html")).get();
        System.out.println(doc.body().html());*/
    }


    private static void test(){
        String insertSql = "insert into `t_sys_area` values (%s,'%s','%s',%s,%s,0,now(),now(),'system','system');";
        try {
            File file = new File("D:\\area\\20180810101641.html");
            Document doc = Jsoup.parse(file,"utf-8");
            Elements trEls = doc.select("tr[height=19]");
            int i = 1;
            Map<String,Integer> parentMap = new HashMap<>();

            System.out.println("总数:"+trEls.size());

            for (Element trEl : trEls) {
                Elements tdEls = trEl.getElementsByClass("xl7023736");
                String code = tdEls.get(0).text();
                String name = tdEls.get(1).text();

                System.out.println("insert into temp values ('"+code+"','"+name+"');");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cl(){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File("D:\\insertArea5.sql"),true);
            bufferedWriter = new BufferedWriter(fileWriter);

            Document doc = Jsoup.connect(AREA_URL).get();
            processProvince(doc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeSql(String sql) throws IOException {
        bufferedWriter.write(sql);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        id ++;
    }

    /**
     * 处理省份
     */
    private static void processProvince(Document doc) throws IOException {
        Elements provinceTrs = doc.select("tr[class='provincetr']");

        for (Element provinceTr : provinceTrs) {
            Elements aEls = provinceTr.getElementsByTag("a");
            if(!aEls.isEmpty()){
                for (Element aEl : aEls) {
                    String provinceUrl = aEl.attr("href");
                    String code = provinceUrl.substring(0,2)+"0000";
                    String name = aEl.text().replace("\"\"","");

                    String sql = String.format(INSERT_SQL,id,name,code,0,0);
                    int pid = id;
                    writeSql(sql);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    processCity(String.format(CITY_URL,provinceUrl),pid);

                    System.out.println("采集省份----------"+name+"--------完成");
                }
            }
        }
    }


    /**
     * 处理城市
     */
    private static void processCity(String url,Integer pid) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements cityTrEls = doc.select("tr[class='citytr']");
        for (Element cityTrEl : cityTrEls) {
            Elements aEls = cityTrEl.getElementsByTag("a");
            Element codeEl = aEls.first();
            Element nameEl = aEls.last();

            String code = codeEl.text().substring(0,6);
            String name = nameEl.text();

            int cid = pid;
            if(!"市辖区".equals(name) && !"自治区直辖县级行政区划".equals(name) && !"省直辖县级行政区划".equals(name)){
                cid = id;
                String sql = String.format(INSERT_SQL,cid,name,code,pid,1);
                writeSql(sql);
            }

            /*executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        processArea(String.format(CITY_URL,codeEl.attr("href")),cityId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("采集城市----------"+name+"--------完成");
                }
            });*/

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                processArea(String.format(CITY_URL,codeEl.attr("href")),cid);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("采集城市----------"+name+"--------完成");
        }
    }


    /**
     * 处理城市
     */
    private static void processArea(String url,Integer cid) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements areaTrEls = doc.select("tr[class='countytr']");

        for (Element areaTr : areaTrEls) {
            Elements aEls = areaTr.getElementsByTag("a");
            if(!aEls.isEmpty()) {
                Element codeEl = aEls.first();
                Element nameEl = aEls.last();

                String code = codeEl.text().substring(0, 6);
                String name = nameEl.text();

                if(!"自治区直辖县级行政区划".equals(name)) {
                    String sql = String.format(INSERT_SQL, id, name, code, cid, 2);
                    writeSql(sql);

                    System.out.println("采集区域----------" + name + "--------完成");
                }
            }
        }
    }
}
