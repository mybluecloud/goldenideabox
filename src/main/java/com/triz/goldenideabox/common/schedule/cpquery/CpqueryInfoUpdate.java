package com.triz.goldenideabox.common.schedule.cpquery;

import com.triz.goldenideabox.dao.CpqueryMapper;
import com.triz.goldenideabox.dao.CpqueryResultMapper;
import com.triz.goldenideabox.dao.OperationLogMapper;
import com.triz.goldenideabox.dao.PatentPropertyMapper;
import com.triz.goldenideabox.dao.PatentRecordMapper;
import com.triz.goldenideabox.model.Cpquery;
import com.triz.goldenideabox.model.CpqueryResult;
import com.triz.goldenideabox.model.OperationLog;
import com.triz.goldenideabox.model.OperationLog.LogCategory;
import com.triz.goldenideabox.model.OperationLog.LogType;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CpqueryInfoUpdate {

    @Autowired
    private CpqueryMapper cpqueryMapper;

    @Autowired
    private PatentRecordMapper patentRecordMapper;

    @Autowired
    private CpqueryResultMapper cpqueryResultMapper;

    @Autowired
    private PatentPropertyMapper patentPropertyMapper;

    @Autowired
    private OperationLogMapper operationLogMapper;

    public static boolean isRun = false;

    private static Logger logger = LoggerFactory.getLogger("CpqueryInfoUpdate");


    @Async
    public void executeCpquery(CloseableHttpClient httpclient, Cpquery cpquery) {
        int status = 0;
        int countQueried = 0,successQueried = 0,failQueried = 0,notQueried = 0;


        try {
            isRun = true;

            cpqueryMapper.updateStatus(cpquery.getId(),1);

            String token = getCpqueryToken(httpclient);

            cpqueryResultMapper.deleteExpiredData(5);
            cpqueryResultMapper.updateApplicationNumbers(cpquery.getId(),cpquery.getTemplateId(),
                cpquery.getMatchField(),cpquery.getMatchChar(),cpquery.getApplicationNumberField(),cpquery.getStatusField());
            List<CpqueryResult> results = cpqueryResultMapper.getApplicationNumbers(cpquery.getId());


            countQueried = results.size();
            for (CpqueryResult result:results) {

                int retCode = queryApplicationInfo(httpclient,cpquery,result,token);


                if (!isRun) {
                    break;
                }
            }


        } catch (Exception e) {
            logger.error("执行任务异常",e);
            status = 2;
            e.printStackTrace();
        }

        notQueried = countQueried - successQueried - failQueried;

        String strStatus,strLogInfo;
        if (isRun) {
            if (status == 0) {
                strStatus = "成功";
            } else {
                strStatus = "异常";
            }
        } else {
            strStatus = "停止";
        }

        strLogInfo = String.format("执行查询任务（编号：%d）%s。预计查询数为%d，成功查询数为%d，失败查询数为%d，未查询数为%d。",
            cpquery.getId(),strStatus,countQueried,successQueried,failQueried,notQueried);


        OperationLog log = new OperationLog();
        log.setCategory(LogCategory.CPQUERY);
        log.setType(LogType.INFO);
        log.setContent(strLogInfo);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        log.setCreateTime(new Date());
        operationLogMapper.insert(log);

        isRun = false;
        cpqueryMapper.updateStatus(cpquery.getId(),status);

    }

    private String getCpqueryToken(CloseableHttpClient httpclient) throws Exception {
        String url =
            "http://cpquery.sipo.gov.cn/txnPantentInfoList.do?inner-flag:open-type=window&inner-flag:flowno=" + System.currentTimeMillis();

        HttpGet httpget = new HttpGet(url);

        httpget.setHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
        httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpget.setHeader("Accept-encoding", "gzip, deflate");
        httpget.setHeader("Accept-language", "zh-CN,zh;q=0.9");
        httpget.setHeader("Cache-Control", "no-cache");
        httpget.setHeader("Connection", "keep-alive");
        httpget.setHeader("Host", "cpquery.sipo.gov.cn");
        httpget.setHeader("Pragma", "no-cache");

        httpget.setHeader("Upgrade-Insecure-Requests", "1");

        CloseableHttpResponse httpresponse = httpclient.execute(httpget);

        if (httpresponse.getStatusLine().getStatusCode() != 200) {
            logger.error("获取token页面失败",httpresponse);
            httpresponse.close();
            throw new Exception();
        }
        Document doc = Jsoup.parse(EntityUtils.toString(httpresponse.getEntity()));
        String token = getToken(doc);

        if (token == null || token.length() == 0 ) {
            logger.error("获取token失败,当前账号登录人数超出系统限制，请稍后再试",httpresponse);
            httpresponse.close();
            throw new Exception();
        }
        httpresponse.close();
        return token;
    }

    private int queryApplicationInfo(CloseableHttpClient httpclient,Cpquery cpquery,CpqueryResult result,String token) throws Exception {

        long flag = System.currentTimeMillis();

        String url = "http://cpquery.sipo.gov.cn/txnQueryBibliographicData.do?select-key:shenqingh="
            + result.getApplicationNumber()
            + "&select-key:backPage=http://cpquery.sipo.gov.cn/txnQueryOrdinaryPatents.do?select-key:shenqingh="
            + result.getApplicationNumber()
            + "&select-key:zhuanlimc=&select-key:shenqingrxm=&select-key:zhuanlilx=&select-key:shenqingr_from=&select-key:shenqingr_to=&select-key:dailirxm=&inner-flag:open-type=window&inner-flag:flowno="
            + flag
            + "&token="
            + token
            + "&inner-flag:open-type=window&inner-flag:flowno=" + System.currentTimeMillis();
        HttpGet httpget = new HttpGet(url);

        httpget.setHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
        httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpget.setHeader("Accept-Encoding", "gzip, deflate");
        httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpget.setHeader("Connection", "keep-alive");
        httpget.setHeader("Host", "cpquery.sipo.gov.cn");
        httpget.setHeader("Upgrade-Insecure-Requests", "1");

        CloseableHttpResponse httpresponse = httpclient.execute(httpget);

        if (httpresponse.getStatusLine().getStatusCode() != 200) {
            httpresponse.close();
            throw new Exception();
        }

        Document doc = Jsoup.parse(EntityUtils.toString(httpresponse.getEntity()));
        String attr = doc.getElementsByTag("span").last().attr("id");
        if (attr == null || attr.length() == 0) {
            httpresponse.close();

            return 1;
        }

        Elements lst = doc.select("span[name='record_zlx:anjianywzt']");
        if (lst != null && !lst.isEmpty()) {

            String caseStatus = lst.first().attr("title");


        }






        httpresponse.close();

        return 0;
    }












    private String getToken(Document doc) {
        String token = "";
        Element sq_token = doc.getElementById("sq_token");

        if (sq_token != null && sq_token.attr("value").length() > 0 ) {
            token = sq_token.attr("value");
        }

        return token;
    }

}
