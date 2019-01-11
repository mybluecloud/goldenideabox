package com.triz.goldenideabox.common.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadHelper {

    public static int upload(MultipartFile file,String path, String filemame) {
        File tempFile = new File(path + filemame);

        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }
        if (!file.isEmpty()) {
            BufferedOutputStream out = null;
            try {
                out = new BufferedOutputStream(new FileOutputStream(tempFile));

                out.write(file.getBytes());
                out.flush();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return 1;
            } catch (IOException e) {
                e.printStackTrace();
                return 2;
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {
            return 1;
        }
        return 0;
    }

    public static int delete(String name) {

        File file = new File(name);

        if (!file.exists() || !file.isFile()) {
            return 1;
        }
        if (!file.delete()) {
            return 1;
        }

        return 0;

    }

    public static String getMD5HexCode(String filename) {
        String code = "";
        FileInputStream fs = null;
        try {

            fs = new FileInputStream(new File(filename));
            code = DigestUtils.md5Hex(fs);

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return code;
    }
}
