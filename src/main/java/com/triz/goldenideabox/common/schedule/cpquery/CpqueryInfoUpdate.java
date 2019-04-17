package com.triz.goldenideabox.common.schedule.cpquery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.common.helper.FileUploadHelper;
import com.triz.goldenideabox.constant.CpqueryState;
import com.triz.goldenideabox.dao.CpqueryAnnounceInfoMapper;
import com.triz.goldenideabox.dao.CpqueryApplicationInfoMapper;
import com.triz.goldenideabox.dao.CpqueryCostInfoMapper;
import com.triz.goldenideabox.dao.CpqueryMapper;
import com.triz.goldenideabox.dao.CpqueryPostInfoMapper;
import com.triz.goldenideabox.dao.CpqueryResultMapper;
import com.triz.goldenideabox.dao.CpqueryReviewInfoMapper;
import com.triz.goldenideabox.dao.OperationLogMapper;
import com.triz.goldenideabox.dao.PatentPropertyMapper;
import com.triz.goldenideabox.dao.PatentRecordMapper;
import com.triz.goldenideabox.model.Cpquery;
import com.triz.goldenideabox.model.CpqueryAnnounceInfo;
import com.triz.goldenideabox.model.CpqueryApplicationInfo;
import com.triz.goldenideabox.model.CpqueryCostInfo;
import com.triz.goldenideabox.model.CpqueryPostInfo;
import com.triz.goldenideabox.model.CpqueryResult;
import com.triz.goldenideabox.model.CpqueryReviewInfo;
import com.triz.goldenideabox.model.OperationLog;
import com.triz.goldenideabox.model.OperationLog.LogCategory;
import com.triz.goldenideabox.model.OperationLog.LogType;
import com.triz.goldenideabox.model.PatentProperty;
import com.triz.goldenideabox.model.PatentRecord;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private CpqueryApplicationInfoMapper cpqueryApplicationInfoMapper;

    @Autowired
    private CpqueryReviewInfoMapper cpqueryReviewInfoMapper;

    @Autowired
    private CpqueryCostInfoMapper cpqueryCostInfoMapper;

    @Autowired
    private CpqueryPostInfoMapper cpqueryPostInfoMapper;

    @Autowired
    private CpqueryAnnounceInfoMapper cpqueryAnnounceInfoMapper;

    public static boolean isRun = false;

    private static String token = "";

    private static String castType = "";

    @Value("${goldenideabox.path.document}")
    private String document;

    private static Logger logger = LoggerFactory.getLogger("CpqueryInfoUpdate");


    @Async
    public void executeCpquery(CloseableHttpClient httpclient, Cpquery cpquery, String checks, boolean isWhole) {
        int status = 0;
        int countQueried = 0;
        int successQueried = 0;
        int notQueried = 0;
        int applicationQueried = 0;
        int reviewQueried = 0;
        int costQueried = 0;
        int postQueried = 0;
        int announceQueried = 0;

        try {
            isRun = true;
            operationLogMapper.deleteExpiredLog(6);

            cpqueryMapper.updateStatus(cpquery.getId(), 1);

            getCpqueryToken(httpclient);

            cpqueryResultMapper.updateApplicationNumbers(cpquery.getId(), cpquery.getTemplateId(),
                    cpquery.getMatchField(), cpquery.getMatchChar(), cpquery.getApplicationNumberField());
            List<CpqueryResult> results;
            if (isWhole) {
                results = cpqueryResultMapper.getCpqueryResultByID(cpquery.getId());
            } else {
                results = cpqueryResultMapper.getFailResultByID(cpquery.getId());
            }
            countQueried = results.size();

            for (CpqueryResult result : results) {
                cpqueryResultMapper.updateTime(result.getApplicationNumber());
                //申请信息查询
                int retCode = queryApplicationInfo(httpclient, cpquery, result);
                if (retCode == CpqueryState.FAIL) {
                    ++applicationQueried;
                    cpqueryResultMapper.updateApplicationStatus(result.getApplicationNumber(), CpqueryState.FAIL);
                    getCpqueryToken(httpclient);
                    continue;
                } else {
                    cpqueryResultMapper.updateApplicationStatus(result.getApplicationNumber(), retCode);
                }

                //审查信息查询
                if (checks.substring(0, 1).equalsIgnoreCase("1")) {

                    try {
                        retCode = CpqueryState.FAIL;
                        retCode = queryReviewInfo(httpclient, result);

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("执行审查信息异常", e);
                    }
                    if (retCode == CpqueryState.FAIL) {
                        ++reviewQueried;
                        cpqueryResultMapper.updateReviewStatus(result.getApplicationNumber(), CpqueryState.FAIL);
                        getCpqueryToken(httpclient);
                    } else {
                        cpqueryResultMapper.updateReviewStatus(result.getApplicationNumber(), retCode);
                    }
                } else {
                    cpqueryResultMapper.updateReviewStatus(result.getApplicationNumber(), CpqueryState.NOEXECUTE);
                }

                //费用信息查询
                if (checks.substring(2, 3).equalsIgnoreCase("1")) {

                    try {
                        retCode = CpqueryState.FAIL;
                        retCode = queryCostInfo(httpclient, result);

                    } catch (Exception e) {
                        logger.error("执行费用信息异常", e);
                    }
                    if (retCode == CpqueryState.FAIL) {
                        ++costQueried;
                        cpqueryResultMapper.updateCostStatus(result.getApplicationNumber(), CpqueryState.FAIL);
                        getCpqueryToken(httpclient);
                    } else {
                        cpqueryResultMapper.updateCostStatus(result.getApplicationNumber(), retCode);
                    }
                } else {
                    cpqueryResultMapper.updateCostStatus(result.getApplicationNumber(), CpqueryState.NOEXECUTE);
                }

                //发文信息查询
                if (checks.substring(4, 5).equalsIgnoreCase("1")) {

                    try {
                        retCode = CpqueryState.FAIL;
                        retCode = queryPostInfo(httpclient, result);

                    } catch (Exception e) {
                        logger.error("执行发文信息异常", e);
                    }
                    if (retCode == CpqueryState.FAIL) {
                        ++postQueried;
                        cpqueryResultMapper.updatePostStatus(result.getApplicationNumber(), CpqueryState.FAIL);
                        getCpqueryToken(httpclient);
                    } else {
                        cpqueryResultMapper.updatePostStatus(result.getApplicationNumber(), retCode);
                    }
                } else {
                    cpqueryResultMapper.updatePostStatus(result.getApplicationNumber(), CpqueryState.NOEXECUTE);
                }

                //公布公告查询
                if (checks.substring(6).equalsIgnoreCase("1")) {

                    try {
                        retCode = CpqueryState.FAIL;
                        retCode = queryAnnounceInfo(httpclient, result);

                    } catch (Exception e) {
                        logger.error("执行公布公告异常", e);
                    }
                    if (retCode == CpqueryState.FAIL) {
                        ++announceQueried;
                        cpqueryResultMapper.updateAnnounceStatus(result.getApplicationNumber(), CpqueryState.FAIL);
                        getCpqueryToken(httpclient);
                    } else {
                        cpqueryResultMapper.updateAnnounceStatus(result.getApplicationNumber(), retCode);
                    }
                } else {
                    cpqueryResultMapper.updateAnnounceStatus(result.getApplicationNumber(), CpqueryState.NOEXECUTE);
                }

                if (!isRun) {
                    break;
                }

                ++successQueried;
            }


        } catch (Exception e) {
            logger.error("执行任务异常", e);
            status = 2;
            e.printStackTrace();
        }

        notQueried = countQueried - successQueried;

        String strStatus, strLogInfo;
        if (isRun) {
            if (status == 0) {
                strStatus = "成功";
            } else {
                strStatus = "异常";
            }
        } else {
            strStatus = "停止";
        }

        strLogInfo = String.format(
                "执行查询任务（编号：%d）%s。预计查询数为%d，完成查询数为%d，申请信息查询失败数为%d，审查信息查询失败数为%d，费用信息查询失败数为%d，发文信息查询失败数为%d，公布公告查询失败数为%d，未查询数为%d。",
                cpquery.getId(), strStatus, countQueried, successQueried,
                applicationQueried, reviewQueried, costQueried, postQueried, announceQueried, notQueried);

        OperationLog log = new OperationLog();
        log.setCategory(LogCategory.CPQUERY);
        log.setType(LogType.INFO);
        log.setContent(strLogInfo);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        log.setCreateTime(new Date());
        operationLogMapper.insert(log);

        isRun = false;
        cpqueryMapper.updateStatus(cpquery.getId(), status);

    }

    private void getCpqueryToken(CloseableHttpClient httpclient) throws Exception {
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
            logger.error("获取token页面失败", httpresponse);
            httpresponse.close();
            throw new Exception();
        }
        Document doc = Jsoup.parse(EntityUtils.toString(httpresponse.getEntity()));

        token = getHiddenValue(doc, "sq_token");

        if (token == null || token.length() == 0) {
            logger.error("获取token失败,当前账号登录人数超出系统限制，请稍后再试", httpresponse);
            httpresponse.close();
            throw new Exception();
        }
        httpresponse.close();
    }

    private int queryApplicationInfo(CloseableHttpClient httpclient, Cpquery cpquery, CpqueryResult result) throws Exception {

        long flag = System.currentTimeMillis();
        int state = CpqueryState.SUCCESS;

        CpqueryApplicationInfo cpqueryApplicationInfo = cpqueryApplicationInfoMapper.selectByPrimaryKey(result.getApplicationNumber());

        if (cpqueryApplicationInfo == null) {
            cpqueryApplicationInfo = new CpqueryApplicationInfo();
        }

        cpqueryApplicationInfo.setApplicationNumber(result.getApplicationNumber());

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

        token = getHiddenValue(doc, "sc_token");

        castType = getHiddenValue(doc, "record_zlx:zhuanlilx");

        List<String> idlst = getSipoIds(doc);

        Elements lst = doc.select("span[name='record_zlx:shenqingr']");
        if (lst != null && !lst.isEmpty()) {
            cpqueryApplicationInfo.setApplicationDate(getRealValue(lst, 0, idlst));
        }

        lst = doc.select("span[name='record_zlx:anjianywzt']");
        if (lst != null && !lst.isEmpty()) {
            String value = getRealValue(lst, 0, idlst);
            if (!value.equalsIgnoreCase(cpqueryApplicationInfo.getCaseStatus())) {
                state = CpqueryState.CHANGE;
                cpqueryApplicationInfo.setCaseStatus(value);
            }
        }

        lst = doc.select("span[name='record_zlx:zhufenlh']");
        if (lst != null && !lst.isEmpty()) {
            cpqueryApplicationInfo.setMainCategoryNumber(getRealValue(lst, 0, idlst));
        }

        lst = doc.select("span[name='record_zlx:fenantjr']");
        if (lst != null && !lst.isEmpty()) {
            cpqueryApplicationInfo.setSubmissionDate(getRealValue(lst, 0, idlst));
        }

        lst = doc.select("span[name='record_sqr:shenqingrxm']");
        if (lst != null && !lst.isEmpty()) {
            cpqueryApplicationInfo.setApplicant(getRealValue(lst, 0, idlst));
        }

        lst = doc.select("span[name='record_fmr:famingrxm']");
        if (lst != null && !lst.isEmpty()) {
            cpqueryApplicationInfo.setInventor(getRealValue(lst, 0, idlst));
        }

        lst = doc.select("span[name='record_zldl:dailijgmc']");
        if (lst != null && !lst.isEmpty()) {
            cpqueryApplicationInfo.setAgency(getRealValue(lst, 0, idlst));
        }

        lst = doc.select("span[name='record_zldl:diyidlrxm']");
        if (lst != null && !lst.isEmpty()) {
            cpqueryApplicationInfo.setFirstAgent(getRealValue(lst, 0, idlst));
        }

        JSONArray array = JSON.parseArray("[]");

        lst = doc.select("span[name='record_zlxbg:biangengrq']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("biangengrq", lst.get(i).attr("title"));
                array.add(i, jsonObject);
            }
        }

        lst = doc.select("span[name='record_zlxbg:biangengsx']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = array.getJSONObject(i);
                jsonObject.put("biangengsx", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_zlxbg:biangengqnr']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = array.getJSONObject(i);
                jsonObject.put("biangengqnr", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_zlxbg:biangenghnr']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = array.getJSONObject(i);
                jsonObject.put("biangenghnr", getRealValue(lst, i, idlst));
            }
        }


        if (!array.toJSONString().equalsIgnoreCase(cpqueryApplicationInfo.getProjectChange())) {
            state = CpqueryState.CHANGE;
            cpqueryApplicationInfo.setProjectChange(array.toJSONString());
        }


        cpqueryApplicationInfoMapper.insert(cpqueryApplicationInfo);

        String value = patentPropertyMapper.getPropertyValue(result.getPatentId(), cpquery.getStatusField());
        if (value != null && value.compareTo(cpqueryApplicationInfo.getCaseStatus()) != 0) {
            String statusName = patentPropertyMapper.getPropertyName(result.getPatentId(), cpquery.getStatusField());
            PatentRecord record = new PatentRecord();
            record.setRecordType(1);
            record.setPatentId(result.getPatentId());
            record.setReocrd(statusName + " 变更为 " + cpqueryApplicationInfo.getCaseStatus());
            patentRecordMapper.insert(record);

            PatentProperty property = new PatentProperty();
            property.setPatentId(result.getPatentId());
            property.setSortId(cpquery.getStatusField());
            property.setValue(cpqueryApplicationInfo.getCaseStatus());
            patentPropertyMapper.editProperty(property);
        }

        httpresponse.close();

        return state;
    }


    private int queryReviewInfo(CloseableHttpClient httpclient, CpqueryResult result) throws Exception {

        long flag = System.currentTimeMillis();
        int state = CpqueryState.SUCCESS;

        String url = "http://cpquery.sipo.gov.cn/txnQueryPatentFileData.do?select-key:shenqingh="
                + result.getApplicationNumber()
                + "&select-key:zhuanlilx="
                + castType
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
        httpget.setHeader("Pragma", "no-cache");
        httpget.setHeader("Upgrade-Insecure-Requests", "1");

        CloseableHttpResponse httpresponse = httpclient.execute(httpget);

        if (httpresponse.getStatusLine().getStatusCode() != 200) {
            httpresponse.close();
            throw new Exception();
        }

        Document doc = Jsoup.parse(EntityUtils.toString(httpresponse.getEntity()));

        String sc_token = getHiddenValue(doc, "sc_token");

        HttpPost httppost = new HttpPost("http://cpquery.sipo.gov.cn/txnQueryPatentFileData.jdo");
        List<NameValuePair> formparams = new ArrayList<>();

        formparams.add(new BasicNameValuePair("select-key:shenqingh", result.getApplicationNumber()));
        formparams.add(new BasicNameValuePair("token", sc_token));

        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");

        httppost.setEntity(uefEntity);

        httppost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
        httppost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        httppost.setHeader("Accept-encoding", "gzip, deflate");
        httppost.setHeader("Accept-language", "zh-CN,zh;q=0.9");
        httppost.setHeader("Cache-Control", "no-cache");
        httppost.setHeader("Connection", "keep-alive");
        httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httppost.setHeader("Host", "cpquery.sipo.gov.cn");
        httppost.setHeader("Origin", "http://cpquery.sipo.gov.cn");
        httppost.setHeader("Pragma", "no-cache");
        httppost.setHeader("X-Requested-With", "XMLHttpRequest");

        httpresponse = httpclient.execute(httppost);

        String fileData = EntityUtils.toString(httpresponse.getEntity(), "UTF-8");

        //FileUploadHelper.saveAsFileWriter(document + result.getApplicationNumber(), "file.json", fileData);
        JSONObject jsonObject = JSON.parseObject(fileData);

        if (!jsonObject.getString("error-code").equalsIgnoreCase("000000")) {
            return CpqueryState.FAIL;
        }

        JSONArray fileArray = jsonObject.getJSONArray("result");

        for (int i = 0; i < fileArray.size(); ++i) {

            JSONObject file = fileArray.getJSONObject(i);
            String id = file.getString("rid");

            if (!file.containsKey("wenjianlx") || !file.containsKey("showcont")) {
                continue;

            }
            String type = file.getString("wenjianlx");
            int showcont = file.getInteger("showcont");
            if (showcont != 2) {
                continue;
            }

            //去掉按类型获取，通过type不为空来获取所有文件，否则新添加的文件类型无法获取
            //&& (type.equalsIgnoreCase("01") || type.equalsIgnoreCase("02") || type.equalsIgnoreCase("03"))

            CpqueryReviewInfo cpqueryReviewInfo = cpqueryReviewInfoMapper.selectByPrimaryKey(id);
            if (cpqueryReviewInfo == null) {
                cpqueryReviewInfo = new CpqueryReviewInfo();
                state = CpqueryState.CHANGE;
            }
            String fileUrl = cpqueryReviewInfo.getFileUrl();
            if (fileUrl != null && fileUrl.length() > 0) {
                continue;
            }


            cpqueryReviewInfo.setId(id);
            cpqueryReviewInfo.setApplicationNumber(result.getApplicationNumber());
            cpqueryReviewInfo.setFileName(file.getString("filename"));
            cpqueryReviewInfo.setType(type);

            for (int j = 0; j < 3; j++) {
                fileUrl = getReviewInfoFile(httpclient, result.getApplicationNumber(), id, file.getString("fid"),
                        type, file.getString("filecode"));
                if (fileUrl.length() > 0) {
                    cpqueryReviewInfo.setFileUrl(fileUrl);
                    break;
                }
            }

            cpqueryReviewInfoMapper.insert(cpqueryReviewInfo);

            if (cpqueryReviewInfo.getFileUrl() == null || cpqueryReviewInfo.getFileUrl().length() == 0) {
                state = CpqueryState.FAIL;
            }


        }

        httpresponse.close();

        return state;
    }

    private String getReviewInfoFile(CloseableHttpClient httpclient, String applicationNumber, String rid, String fid, String type, String filecode)
            throws Exception {

        HttpPost httppost = new HttpPost("http://cpquery.sipo.gov.cn/txnShowPatentFileData.ajax");
        List<NameValuePair> formparams = new ArrayList<>();

        formparams.add(new BasicNameValuePair("select-key:shenqingh", applicationNumber));
        formparams.add(new BasicNameValuePair("select-key:rid", rid));
        formparams.add(new BasicNameValuePair("select-key:fid", fid));
        formparams.add(new BasicNameValuePair("select-key:wenjianlx", type));
        formparams.add(new BasicNameValuePair("select-key:wendanglx", "01"));
        formparams.add(new BasicNameValuePair("select-key:wenjiandm", filecode));
        formparams.add(new BasicNameValuePair("inner-flag:open-type", "new-window"));
        formparams.add(new BasicNameValuePair("inner-flag:freeze-stamp", System.currentTimeMillis() + ""));
        formparams.add(new BasicNameValuePair("charset-encoding", "UTF-8"));

        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");

        httppost.setEntity(uefEntity);

        httppost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
        httppost.setHeader("Accept", "application/xml, text/xml, */*; q=0.01");
        httppost.setHeader("Accept-encoding", "gzip, deflate");
        httppost.setHeader("Accept-language", "zh-CN,zh;q=0.9");
        httppost.setHeader("Cache-Control", "no-cache");
        httppost.setHeader("Connection", "keep-alive");
        httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httppost.setHeader("Host", "cpquery.sipo.gov.cn");
        httppost.setHeader("Origin", "http://cpquery.sipo.gov.cn");
        httppost.setHeader("Pragma", "no-cache");
        httppost.setHeader("X-Requested-With", "XMLHttpRequest");

        CloseableHttpResponse httpresponse = httpclient.execute(httppost);

        SAXReader reader = new SAXReader();
        org.dom4j.Document doc = reader.read(httpresponse.getEntity().getContent());

        org.dom4j.Element root = doc.getRootElement();
        String retcode = root.element("error-code").getTextTrim();
        if (!retcode.equalsIgnoreCase("000000")) {
            return "";
        }
        org.dom4j.Element element = root.element("record").element("url");
        if (element != null) {
            if (element.getTextTrim().equalsIgnoreCase("/layout/images/empty_date.png")) {
                return "";
            }

        }

        JSONArray array = JSON.parseArray("[]");

        List<org.dom4j.Element> records = root.elements("record_url");
        if (records.size() == 0) {
            logger.error(doc.asXML());
        }
        for (int i = 0; i < records.size(); ++i) {
            if (records.get(i).element("url") != null) {
                String fileName = downloadReviewInfoFile(httpclient, applicationNumber, records.get(i).element("url").getTextTrim(),
                        records.get(i).element("imgToken").getTextTrim());

                JSONObject jsonObject = new JSONObject();
                jsonObject.put(String.valueOf(i), fileName);
                array.add(i, jsonObject);
            }

        }

        httpresponse.close();

        return array.toJSONString();
    }

    private String downloadReviewInfoFile(CloseableHttpClient httpclient, String applicationNumber, String url, String imgToken) throws Exception {

        HttpGet httpget = new HttpGet("http://cpquery.sipo.gov.cn/freeze.main?txn-code=txnImgToken&token=" + url + "&imgToken=" + imgToken);

        httpget.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
        httpget.setHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        httpget.setHeader("Accept-encoding", "gzip, deflate");
        httpget.setHeader("Accept-language", "zh-CN,zh;q=0.9");
        httpget.setHeader("Cache-Control", "no-cache");
        httpget.setHeader("Connection", "keep-alive");
        httpget.setHeader("Host", "cpquery.sipo.gov.cn");
        httpget.setHeader("Pragma", "no-cache");
        CloseableHttpResponse httpresponse = httpclient.execute(httpget);
        if (httpresponse.getStatusLine().getStatusCode() != 200) {
            logger.error("请求文件失败", httpresponse);
            return "";
        }

        InputStream in = httpresponse.getEntity().getContent();
        String fileName = UUID.randomUUID().toString().replace("-", "").toLowerCase() + ".jpg";

        File file = new File(document + applicationNumber);
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(document + applicationNumber + File.separator + fileName);

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

        byte[] buf = new byte[1024];
        int size;

        while (-1 != (size = in.read(buf))) {
            out.write(buf, 0, size);
        }
        out.close();
        in.close();

        httpresponse.close();

        return fileName;
    }


    private int queryCostInfo(CloseableHttpClient httpclient, CpqueryResult result) throws Exception {

        int state = CpqueryState.SUCCESS;
        long flag = System.currentTimeMillis();

        CpqueryCostInfo cpqueryCostInfo = cpqueryCostInfoMapper.selectByPrimaryKey(result.getApplicationNumber());

        if (cpqueryCostInfo == null) {
            cpqueryCostInfo = new CpqueryCostInfo();
        }

        cpqueryCostInfo.setApplicationNumber(result.getApplicationNumber());

        String url = "http://cpquery.sipo.gov.cn/txnQueryFeeData.do?select-key:shenqingh="
                + result.getApplicationNumber()
                + "&select-key:zhuanlilx="
                + castType
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

        token = getHiddenValue(doc, "sq_token");

        List<String> idlst = getSipoIds(doc);

        //应缴费信息
        JSONArray payableArray = JSON.parseArray("[]");

        Elements lst = doc.select("span[name='record_yingjiaof:yingjiaofydm']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("yingjiaofydm", getRealValue(lst, i, idlst));
                payableArray.add(i, jsonObject);
            }
        }

        lst = doc.select("span[name='record_yingjiaof:shijiyjje']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = payableArray.getJSONObject(i);
                jsonObject.put("shijiyjje", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_yingjiaof:jiaofeijzr']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = payableArray.getJSONObject(i);
                jsonObject.put("jiaofeijzr", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_yingjiaof:feiyongzt']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = payableArray.getJSONObject(i);
                jsonObject.put("feiyongzt", getRealValue(lst, i, idlst));
            }
        }

        if (!payableArray.toJSONString().equalsIgnoreCase(cpqueryCostInfo.getPayable())) {
            state = CpqueryState.CHANGE;
            cpqueryCostInfo.setPayable(payableArray.toJSONString());
        }

        //已缴费信息
        JSONArray paidArray = JSON.parseArray("[]");

        lst = doc.select("span[name='record_yijiaof:feiyongzldm']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("feiyongzldm", getRealValue(lst, i, idlst));
                paidArray.add(i, jsonObject);
            }
        }

        lst = doc.select("span[name='record_yijiaof:jiaofeije']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = paidArray.getJSONObject(i);
                jsonObject.put("jiaofeije", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_yijiaof:jiaofeisj']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = paidArray.getJSONObject(i);
                jsonObject.put("jiaofeisj", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_yijiaof:jiaofeirxm']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = paidArray.getJSONObject(i);
                jsonObject.put("jiaofeirxm", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_yijiaof:shoujuh']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = paidArray.getJSONObject(i);
                jsonObject.put("shoujuh", getRealValue(lst, i, idlst));
            }
        }

        if (!paidArray.toJSONString().equalsIgnoreCase(cpqueryCostInfo.getPaid())) {
            state = CpqueryState.CHANGE;
            cpqueryCostInfo.setPaid(paidArray.toJSONString());
        }


        //退费信息

        //滞纳金信息

        //收据发文信息

        cpqueryCostInfoMapper.insert(cpqueryCostInfo);

        httpresponse.close();

        return state;
    }

    private int queryPostInfo(CloseableHttpClient httpclient, CpqueryResult result) throws Exception {

        int state = CpqueryState.SUCCESS;
        long flag = System.currentTimeMillis();

        CpqueryPostInfo cpqueryPostInfo = cpqueryPostInfoMapper.selectByPrimaryKey(result.getApplicationNumber());

        if (cpqueryPostInfo == null) {
            cpqueryPostInfo = new CpqueryPostInfo();
        }

        cpqueryPostInfo.setApplicationNumber(result.getApplicationNumber());

        String url = "http://cpquery.sipo.gov.cn/txnQueryDeliveryData.do?select-key:shenqingh="
                + result.getApplicationNumber()
                + "&select-key:zhuanlilx="
                + castType
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

        token = getHiddenValue(doc, "sq_token");

        List<String> idlst = getSipoIds(doc);

        //通知书发文
        JSONArray noticeArray = JSON.parseArray("[]");

        Elements lst = doc.select("span[name='record_fawen:tongzhislx']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tongzhislx", getRealValue(lst, i, idlst));
                noticeArray.add(i, jsonObject);
            }
        }

        lst = doc.select("span[name='record_fawen:fawenrq']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = noticeArray.getJSONObject(i);
                jsonObject.put("fawenrq", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_fawen:shoujianrxm']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = noticeArray.getJSONObject(i);
                jsonObject.put("shoujianrxm", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_fawen:shoujianyzbm']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = noticeArray.getJSONObject(i);
                jsonObject.put("shoujianyzbm", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_fawen:xiazaisj']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = noticeArray.getJSONObject(i);
                jsonObject.put("xiazaisj", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_fawen:xiazaiip']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = noticeArray.getJSONObject(i);
                jsonObject.put("xiazaiip", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_fawen:fawenlx']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = noticeArray.getJSONObject(i);
                jsonObject.put("fawenlx", getRealValue(lst, i, idlst));
            }
        }

        if (!noticeArray.toJSONString().equalsIgnoreCase(cpqueryPostInfo.getNotice())) {
            state = CpqueryState.CHANGE;
            cpqueryPostInfo.setNotice(noticeArray.toJSONString());
        }



        //专利证书
        JSONArray patentCertificateArray = JSON.parseArray("[]");

        lst = doc.select("span[name='record_zhengshu:fawenrq']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fawenrq", getRealValue(lst, i, idlst));
                patentCertificateArray.add(i, jsonObject);
            }
        }

        lst = doc.select("span[name='record_zhengshu:shoujianrxm']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = patentCertificateArray.getJSONObject(i);
                jsonObject.put("shoujianrxm", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_zhengshu:shoujianyzbm']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = patentCertificateArray.getJSONObject(i);
                jsonObject.put("shoujianyzbm", lst.get(i).attr("title"));
            }
        }

        if (!patentCertificateArray.toJSONString().equalsIgnoreCase(cpqueryPostInfo.getPatentCertificate())) {
            state = CpqueryState.CHANGE;
            cpqueryPostInfo.setPatentCertificate(patentCertificateArray.toJSONString());
        }

        //退信

        cpqueryPostInfoMapper.insert(cpqueryPostInfo);

        httpresponse.close();

        return state;
    }

    private int queryAnnounceInfo(CloseableHttpClient httpclient, CpqueryResult result) throws Exception {

        int state = CpqueryState.SUCCESS;
        long flag = System.currentTimeMillis();

        CpqueryAnnounceInfo cpqueryAnnounceInfo = cpqueryAnnounceInfoMapper.selectByPrimaryKey(result.getApplicationNumber());

        if (cpqueryAnnounceInfo == null) {
            cpqueryAnnounceInfo = new CpqueryAnnounceInfo();
        }

        cpqueryAnnounceInfo.setApplicationNumber(result.getApplicationNumber());

        String url = "http://cpquery.sipo.gov.cn/txnQueryPublicationData.do?select-key:shenqingh="
                + result.getApplicationNumber()
                + "&select-key:zhuanlilx="
                + castType
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

        token = getHiddenValue(doc, "sq_token");

        List<String> idlst = getSipoIds(doc);

        //发明公布/授权公告
        JSONArray publishArray = JSON.parseArray("[]");

        Elements lst = doc.select("span[name='record_gkgg:gonggaoh']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("gonggaoh", getRealValue(lst, i, idlst));
                publishArray.add(i, jsonObject);
            }
        }

        lst = doc.select("span[name='record_gkgg:gongkaigglx']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = publishArray.getJSONObject(i);
                jsonObject.put("gongkaigglx", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_gkgg:juanqih']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = publishArray.getJSONObject(i);
                jsonObject.put("juanqih", getRealValue(lst, i, idlst));
            }
        }

        lst = doc.select("span[name='record_gkgg:gonggaor']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = publishArray.getJSONObject(i);
                jsonObject.put("gonggaor", getRealValue(lst, i, idlst));
            }
        }

        if (!publishArray.toJSONString().equalsIgnoreCase(cpqueryAnnounceInfo.getPublish())) {
            state = CpqueryState.CHANGE;
            cpqueryAnnounceInfo.setPublish(publishArray.toJSONString());
        }


        //事务公告
        JSONArray transactionPublishArray = JSON.parseArray("[]");

        lst = doc.select("span[name='record_swgg:shiwugglxdm']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("shiwugglxdm", lst.get(i).attr("title"));
                transactionPublishArray.add(i, jsonObject);
            }
        }

        lst = doc.select("span[name='record_swgg:juanqih']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = transactionPublishArray.getJSONObject(i);
                jsonObject.put("juanqih", lst.get(i).attr("title"));
            }
        }

        lst = doc.select("span[name='record_swgg:gonggaor']");
        if (lst != null && !lst.isEmpty()) {
            for (int i = 0; i < lst.size(); ++i) {
                JSONObject jsonObject = transactionPublishArray.getJSONObject(i);
                jsonObject.put("gonggaor", lst.get(i).attr("title"));
            }
        }

        if (!transactionPublishArray.toJSONString().equalsIgnoreCase(cpqueryAnnounceInfo.getTransactionPublish())) {
            state = CpqueryState.CHANGE;
            cpqueryAnnounceInfo.setTransactionPublish(transactionPublishArray.toJSONString());
        }


        cpqueryAnnounceInfoMapper.insert(cpqueryAnnounceInfo);

        httpresponse.close();

        return state;
    }

    private List<String> getSipoIds(Document doc) {

        String attr = doc.getElementsByTag("span").last().attr("id");

        int b = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < attr.length(); i += 2) {

            int a = Integer.parseInt(attr.substring(i, i + 2), 16) ^ b++;

            if (b > 255) {
                b = 0;
            }
            sb.append((char) a);
        }

        List<String> idlst = Arrays.asList(sb.toString().split(","));

        return idlst;
    }

    private String getRealValue(Elements elements, int index, List<String> idlst) {
        Elements spans = elements.get(index).children();
        String value = "";
        for (Element span : spans) {
            if (idlst.contains(span.attr("id"))) {
                value = value + span.text();
            }
        }

        return value;
    }


    private String getHiddenValue(Document doc, String id) {
        String token = "";
        Element sq_token = doc.getElementById(id);

        if (sq_token != null && sq_token.attr("value").length() > 0) {
            token = sq_token.attr("value");
        }

        return token;
    }

}
