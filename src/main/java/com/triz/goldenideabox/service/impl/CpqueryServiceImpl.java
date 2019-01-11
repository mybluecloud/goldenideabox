package com.triz.goldenideabox.service.impl;

import com.triz.goldenideabox.common.schedule.AutoUpdateCaseState;
import com.triz.goldenideabox.dao.CpqueryMapper;
import com.triz.goldenideabox.dao.CpqueryResultMapper;
import com.triz.goldenideabox.dao.PatentRecordMapper;
import com.triz.goldenideabox.model.Cpquery;
import com.triz.goldenideabox.model.CpqueryResult;
import com.triz.goldenideabox.model.PatentRecord;
import com.triz.goldenideabox.service.CpqueryService;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service("cpqueryService")
public class CpqueryServiceImpl implements CpqueryService {

    @Autowired
    private CpqueryMapper cpqueryMapper;

    @Autowired
    private CpqueryResultMapper cpqueryResultMapper;

    @Autowired
    private PatentRecordMapper patentRecordMapper;

    @Autowired
    private AutoUpdateCaseState autoUpdateCaseState;

    private static Logger logger = LoggerFactory.getLogger("cpqueryService");

    private static CloseableHttpClient httpclient;

    @Value("${goldenideabox.path.temp}")
    private String tempPath;

//    @PostConstruct
//    public void init() {
//        if (httpclient == null) {
//            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
////            HttpHost proxy = new HttpHost("127.0.0.1",8888);
////            httpclient = httpClientBuilder.setProxy(proxy).build();
//            httpclient = httpClientBuilder.build();
//        }
//    }

    @Override
    public List<Cpquery> getCpqueryInfo() {
        return cpqueryMapper.getCpqueryInfo();
    }

    @Override
    public Cpquery getCpqueryInfoById(int id) {
        return cpqueryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateCpqueryInfo(Cpquery cpquery) {
        return cpqueryMapper.updateByPrimaryKey(cpquery);
    }

    @Override
    public List<PatentRecord> getCpqueryRecord() {
        return patentRecordMapper.getCpqueryRecord();
    }

    @Override
    public int deleteCpqueryInfo(int id) {
        return cpqueryMapper.deleteCpqueryInfo(id);
    }

    @Override
    public void stopCpquery() {
        AutoUpdateCaseState.isRun = false;
    }

    @Override
    public List<CpqueryResult> getCpqueryResult() {
        return cpqueryResultMapper.getCpqueryResult();
    }

    @Override
    public int addCpqueryInfo(Cpquery cpquery) {
        return cpqueryMapper.insert(cpquery);
    }

    @Override
    public String getJsessionID() {

        CloseableHttpResponse httpresponse = null;
        String jsessionID = "";
        try {
            if (httpclient != null) {
                httpclient.close();
            }
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            httpclient = httpClientBuilder.build();

            HttpGet httpget = new HttpGet("http://cpquery.sipo.gov.cn");

            httpget.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
            httpget.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpget.setHeader("accept-encoding", "gzip, deflate");
            httpget.setHeader("accept-language", "zh-CN,zh;q=0.9");
            httpget.setHeader("Cache-Control", "no-cache");
            httpget.setHeader("Connection", "keep-alive");
            httpget.setHeader("Host", "cpquery.sipo.gov.cn");
            httpget.setHeader("Pragma", "no-cache");
            httpget.setHeader("Upgrade-Insecure-Requests", "1");

            httpresponse = httpclient.execute(httpget);
            if (httpresponse.getStatusLine().getStatusCode() != 200) {
                logger.error("请求网页失败",httpresponse);
                return "";
            }
            Header[] heads = httpresponse.getAllHeaders();
            for (Header header:heads) {

                if (header.getName().equalsIgnoreCase("Set-Cookie")) {
                    jsessionID = header.getValue();
                    break;
                }
            }




            return  jsessionID;

        } catch (IOException e) {
            logger.error("请求网页异常",e);
            e.printStackTrace();
        } finally {
            if (httpresponse != null) {
                try {
                    httpresponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    @Override
    public String getJcaptchaImage(Date date) {
        CloseableHttpResponse httpresponse = null;
        try {
            String url = "http://cpquery.sipo.gov.cn/JcaptchaServlet?type=1&usertype=1&date=" + URLEncoder.encode(date + "" + Math.random(), "UTF-8");

            HttpGet httpget = new HttpGet(url);

            httpget.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
            httpget.setHeader("accept", "image/webp,image/apng,image/*,*/*;q=0.8");
            httpget.setHeader("accept-encoding", "gzip, deflate");
            httpget.setHeader("accept-language", "zh-CN,zh;q=0.9");
            httpget.setHeader("Cache-Control", "no-cache");
            httpget.setHeader("Connection", "keep-alive");
            //httpget.setHeader("Cookie", jsessionID);
            httpget.setHeader("Host", "cpquery.sipo.gov.cn");
            httpget.setHeader("Pragma", "no-cache");
            httpget.setHeader("Referer", "http://cpquery.sipo.gov.cn/");
            httpget.setHeader("X-Requested-With", "XMLHttpRequest");
            httpresponse = httpclient.execute(httpget);
            if (httpresponse.getStatusLine().getStatusCode() != 200.) {
                logger.error("请求验证码图片失败",httpresponse);
                return "";
            }

            InputStream in = httpresponse.getEntity().getContent();
            String filename = UUID.randomUUID().toString().replace("-", "").toLowerCase() + ".jpg";

            File file = new File(tempPath + filename);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            // 构建缓冲区
            byte[] buf = new byte[1024];
            int size;
            // 写入到文件
            while (-1 != (size = in.read(buf))) {
                out.write(buf, 0, size);
            }
            out.close();
            in.close();

            return filename;
        } catch (IOException e) {
            logger.error("请求验证码图片异常",e);
            e.printStackTrace();
        } finally {
            if (httpresponse != null) {
                try {
                    httpresponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }

    @Override
    public String getJcaptchaText(Date date) {
        CloseableHttpResponse httpresponse = null;
        try {
            String url = "http://cpquery.sipo.gov.cn/JcaptchaServlet?&usertype=1&date=" + URLEncoder.encode(date + "" + Math.random(), "UTF-8");

            HttpGet httpget = new HttpGet(url);


            httpget.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
            httpget.setHeader("accept", "*/*");
            httpget.setHeader("accept-encoding", "gzip, deflate");
            httpget.setHeader("accept-language", "zh-CN,zh;q=0.9");
            httpget.setHeader("Cache-Control", "no-cache");
            httpget.setHeader("Connection", "keep-alive");
            //httpget.setHeader("Cookie", jsessionID);
            httpget.setHeader("Host", "cpquery.sipo.gov.cn");
            httpget.setHeader("Pragma", "no-cache");
            httpget.setHeader("Referer", "http://cpquery.sipo.gov.cn/");
            httpget.setHeader("X-Requested-With", "XMLHttpRequest");

            httpresponse = httpclient.execute(httpget);
            if (httpresponse.getStatusLine().getStatusCode() != 200.) {
                logger.error("请求验证码文本失败",httpresponse);

                return "";
            }

            String text = EntityUtils.toString(httpresponse.getEntity(),"UTF-8");

            return text;
        } catch (IOException e) {
            logger.error("请求验证码文本异常",e);
            e.printStackTrace();
        } finally {
            if (httpresponse != null) {
                try {
                    httpresponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }

    @Override
    public int getValidationStatus(String code) {
        CloseableHttpResponse httpresponse = null;
        try {
            String url = "http://cpquery.sipo.gov.cn/LoginServlet?usertype=1";

            HttpPost httppost = new HttpPost(url);
            List<NameValuePair> formparams = new ArrayList<>();

            for (String params : code.split(";")) {
                String[] patam = params.split(":");
                formparams.add(new BasicNameValuePair(patam[0], patam[1]));
            }

            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");

            httppost.setEntity(uefEntity);

            httppost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");
            httppost.setHeader("accept", "*/*");
            httppost.setHeader("accept-encoding", "gzip, deflate");
            httppost.setHeader("accept-language", "zh-CN,zh;q=0.9");
            httppost.setHeader("Cache-Control", "no-cache");
            httppost.setHeader("Connection", "keep-alive");
            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            //httppost.setHeader("Cookie", jsessionID);
            httppost.setHeader("Host", "cpquery.sipo.gov.cn");
            httppost.setHeader("Origin", "http://cpquery.sipo.gov.cn");
            httppost.setHeader("Pragma", "no-cache");
            httppost.setHeader("Referer", "http://cpquery.sipo.gov.cn/");
            httppost.setHeader("X-Requested-With", "XMLHttpRequest");

            httpresponse = httpclient.execute(httppost);



            if (httpresponse.getStatusLine().getStatusCode() != 200) {
                logger.error("验证码效验失败",httpresponse);

                return 1;
            }

            if (EntityUtils.toString(httpresponse.getEntity(),"UTF-8").equalsIgnoreCase("true")) {

                return 0;
            } else {
                logger.error("验证码效验失败",httpresponse);

            }
        } catch (IOException e) {
            logger.error("验证码效验异常",e);
            e.printStackTrace();
        } finally {
            if (httpresponse != null) {
                try {
                    httpresponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 1;
    }


    @Override
    public String beginQuery(int id) {
        CloseableHttpResponse httpresponse = null;
        try {
            Cpquery cpquery = cpqueryMapper.selectByPrimaryKey(id);

            HttpPost httppost = new HttpPost("http://cpquery.sipo.gov.cn/txn999999.ajax?usertype=2");

            List<NameValuePair> formparams = new ArrayList<>();
            formparams.add(new BasicNameValuePair("username", cpquery.getAccount()));
            formparams.add(new BasicNameValuePair("password", cpquery.getPassword()));
            formparams.add(new BasicNameValuePair("userrole", "2"));
            formparams.add(new BasicNameValuePair("vidcode", "1"));
            formparams.add(new BasicNameValuePair("language", "zh"));
            formparams.add(new BasicNameValuePair("logout_flag", "true"));

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
            httppost.setHeader("X-Requested-With", "XMLHttpRequest");
            httppost.setHeader("Host", "cpquery.sipo.gov.cn");
            httppost.setHeader("Origin", "http://cpquery.sipo.gov.cn");
            httppost.setHeader("Referer", "http://cpquery.sipo.gov.cn/");
            //httppost.setHeader("Cookie", jsessionID);

            httpresponse = httpclient.execute(httppost);
            //System.out.println(EntityUtils.toString(httpresponse.getEntity()));
            SAXReader reader = new SAXReader();
            org.dom4j.Document doc = reader.read(httpresponse.getEntity().getContent());
            org.dom4j.Element root = doc.getRootElement();
            String retcode = root.element("error-code").getTextTrim();

            if (retcode.equalsIgnoreCase("000000")) {
                //String ssoToken = httpresponse.getHeaders("Set-Cookie")[0].getValue();
                autoUpdateCaseState.executeCpquery(httpclient,cpquery);

                return "000000";
            } else {
                logger.error("登录专利查询网站失败",retcode);
                return retcode;
            }


        } catch (Exception e) {
            logger.error("登录专利查询网站异常",e);
            e.printStackTrace();
        } finally {
            if (httpresponse != null) {
                try {
                    httpresponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }






}
