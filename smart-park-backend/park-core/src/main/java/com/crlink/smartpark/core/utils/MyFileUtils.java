package com.crlink.smartpark.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class MyFileUtils {

    /**
     * 获取文件名后缀
     */
    public static String getFilenameSuffix(String filename) {
        String suffix = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(filename)) {
            int lastIndexOf = filename.lastIndexOf(".");
            int paramIndexOf = filename.lastIndexOf("?");
            if (lastIndexOf != -1) {
                if (paramIndexOf != -1) {
                    suffix = StringUtils.substring(filename, lastIndexOf + 1, paramIndexOf);
                } else {
                    suffix = StringUtils.substring(filename, lastIndexOf + 1);
                }
                return suffix;
            }
        }
        return suffix;
    }

    public static void makePathWritable(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.setWritable(true, false);
            file.mkdirs();

        }
    }


    //解压文件到指定目录
    @SuppressWarnings("rawtypes")
    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        //解决zip文件中有中文目录或者中文文件
        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + File.separator + zipEntryName).replaceAll("\\*", "/");
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            log.info("解压文件路径为：" + outPath);
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            File unZipFile = new File(outPath);
            if (unZipFile.isDirectory()) {
                //if (unZipToParentFile) {
                //    FileUtil.moveContent(unZipFile, unZipFile.getParentFile(), false);
                //    //FileUtils.moveDirectoryToDirectory(unZipFile, unZipFile.getParentFile(), true);
                //}
                continue;
            }
            FileUtils.copyInputStreamToFile(in, new File(outPath));
            in.close();
        }
        zip.close();
    }
}