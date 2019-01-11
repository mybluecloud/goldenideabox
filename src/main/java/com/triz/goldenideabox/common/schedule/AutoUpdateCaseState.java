package com.triz.goldenideabox.common.schedule;

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
import com.triz.goldenideabox.model.PatentProperty;
import com.triz.goldenideabox.model.PatentRecord;
import java.io.IOException;
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
public class AutoUpdateCaseState {

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

    private static Logger logger = LoggerFactory.getLogger("AutoUpdateCaseState");

    @Async
    public void executeCpquery(CloseableHttpClient httpclient, Cpquery cpquery) {
        int status = 0;
        int countQueried = 0;
        int successQueried = 0;
        int failQueried = 0;
        int notQueried = 0;

        try {
            isRun = true;
            cpqueryMapper.updateStatus(cpquery.getId(),1);

            String token = getCpqueryToken(httpclient);

            cpqueryResultMapper.deleteExpiredData(5);
            cpqueryResultMapper.updateApplicationNumbers(cpquery.getId(),cpquery.getTemplateId(),
                cpquery.getMatchField(),cpquery.getMatchChar(),cpquery.getApplicationNumberField(),cpquery.getStatusField());
            List<CpqueryResult> results = cpqueryResultMapper.getApplicationNumbers(cpquery.getId());

            long flag = System.currentTimeMillis();
            countQueried = results.size();
            for (CpqueryResult result:results) {

                int retCode = searchPatentStatus(httpclient,cpquery,result,flag,token,true);
                if (retCode == 1) {
                    token = getCpqueryToken(httpclient);
                    if (searchPatentStatus(httpclient,cpquery,result,flag,token,false) == 0) {
                        ++successQueried;
                    } else {
                        ++failQueried;
                    }
                } else {
                    ++successQueried;
                }

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

        StringBuilder sb = new StringBuilder();
        sb.append("执行查询任务（编号：");
        sb.append(cpquery.getId());
        if (isRun) {
            if (status == 0) {
                sb.append("）成功。");
            } else {
                sb.append("）异常。");
            }
        } else {
            sb.append("）停止。");
        }

        sb.append("预计查询数为");
        sb.append(countQueried);
        sb.append("，成功查询数为");
        sb.append(successQueried);
        sb.append("，失败查询数为");
        sb.append(failQueried);
        sb.append("，未查询数为");
        sb.append(notQueried);
        sb.append("。");

        OperationLog log = new OperationLog();
        log.setCategory(LogCategory.CPQUERY);
        log.setType(LogType.INFO);
        log.setContent(sb.toString());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        log.setCreateTime(new Date());
        operationLogMapper.insert(log);

        isRun = false;
        cpqueryMapper.updateStatus(cpquery.getId(),status);

    }

    private int searchPatentStatus(CloseableHttpClient httpclient,Cpquery cpquery,CpqueryResult result,long flag,String token,boolean isFirst) throws Exception {
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
        //httpget.setHeader("Cache-Control", "no-cache");
        httpget.setHeader("Connection", "keep-alive");
        //httpget.setHeader("Cookie", jsessionID);
        httpget.setHeader("Host", "cpquery.sipo.gov.cn");
        //httpget.setHeader("Pragma", "no-cache");

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
            if (!isFirst) {
                result.setUpdateTime(new Date());
                result.setStatus(2);
                cpqueryResultMapper.updateResult(result);
                logger.warn("获取案件为空",httpresponse);
            }
            return 1;
        }


        List<String> idlst = Arrays.asList(parseSipoIds(attr).split(","));
        Elements lst = doc.getElementsByAttributeValue("name", "record_zlx:anjianywzt");
        if (lst != null && !lst.isEmpty()) {
            Elements spans = lst.first().getElementsByTag("span");
            String caseStatus = "";
            for (Element span : spans) {

                if (!span.attr("name").equalsIgnoreCase("record_zlx:anjianywzt")
                    && idlst.contains(span.attr("id"))) {
                    caseStatus = caseStatus + span.text();
                }
            }

            if (caseStatus.length() > 0 ) {

                if (caseStatus.compareTo(result.getOfficialStatus()) != 0) {
                    String statusName = patentPropertyMapper.getPropertyName(result.getPatentId(),cpquery.getStatusField());
                    PatentRecord record = new PatentRecord();
                    record.setRecordType(1);
                    record.setPatentId(result.getPatentId());
                    record.setReocrd(statusName + " 变更为 "+caseStatus);
                    patentRecordMapper.insert(record);

                    result.setOfficialStatus(caseStatus);


                    PatentProperty property = new PatentProperty();
                    property.setPatentId(result.getPatentId());
                    property.setSortId(cpquery.getStatusField());
                    property.setValue(caseStatus);
                    patentPropertyMapper.editProperty(property);
                }
                result.setStatus(1);
                result.setUpdateTime(new Date());
                cpqueryResultMapper.updateResult(result);

            }

            Thread.sleep(1000 * (int)(Math.random()*10) );

        } else {

            result.setUpdateTime(new Date());
            result.setStatus(3);
            cpqueryResultMapper.updateResult(result);
            logger.warn("获取案件状态为空",httpresponse);
        }

        httpresponse.close();

        return 0;
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
        //httpget.setHeader("Cookie", jsessionID);
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
        Element sq_token = doc.getElementById("sq_token");

        if (sq_token == null || sq_token.attr("value").length() == 0 ) {
            logger.error("获取token失败,当前账号登录人数超出系统限制，请稍后再试",httpresponse);
            httpresponse.close();
            throw new Exception();
        }
        httpresponse.close();
        return sq_token.attr("value");
    }


    private void getDisclaimer(CloseableHttpClient httpclient) throws IOException {
        String url =
            "http://cpquery.sipo.gov.cn/txnDisclaimerDetail.do?time="+ System.currentTimeMillis() +"&select-key:yuzhong=zh&select-key:gonggaolx=3";

        HttpGet httpget = new HttpGet(url);

        httpget.setHeader("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
        httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpget.setHeader("Accept-Encoding", "gzip, deflate");
        httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpget.setHeader("Cache-Control", "no-cache");
        httpget.setHeader("Connection", "keep-alive");
        //httpget.setHeader("Cookie", jsessionID+ ";"+token);
        httpget.setHeader("Host", "cpquery.sipo.gov.cn");
        httpget.setHeader("Pragma", "no-cache");
        httpget.setHeader("Upgrade-Insecure-Requests", "1");

        httpclient.execute(httpget);

        httpget.releaseConnection();
    }

    private String parseSipoIds(String enStr) {
        int b4 = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < enStr.length(); i += 2) {
            if (b4 > 255) {
                b4 = 0;
            }
            int c = Integer.parseInt(enStr.substring(i, i + 2), 16) ^ b4++;
            sb.append((char) c);
        }
        return sb.toString();
    }

}
