package com.spt.selenium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by : Panupong Chantaklang
 * Created Date : 25/03/2015
 */
public class PhantomUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(PhantomUtil.class);
    private static String FILE_SEPARATE = System.getProperty("file.separator");

    public static File getPhantomJs() {
        String osName = System.getProperty("os.name");
        String tempDir = System.getProperty("java.io.tmpdir");

        InputStream input = null;
        OutputStream output;
        File phantomjs_exe = null;

        LOGGER.debug("OS: {} / TempDir :{}",osName,tempDir);

        if(osName.contains("Win")) {
            input = PhantomUtil.class.getResourceAsStream("/phantomjs-windows/phantomjs.exe");
            phantomjs_exe = new File(tempDir+FILE_SEPARATE+"phantomjs.exe");
        }else if (osName.contains("Mac")){
            input = PhantomUtil.class.getResourceAsStream("/phantomjs2_osx/phantomjs");
            phantomjs_exe = new File(tempDir+FILE_SEPARATE+"phantomjs");
        }else{
            LOGGER.error("-= Unknown OS =-");
        }

        try {
            output = new FileOutputStream(phantomjs_exe);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            input.close();
            output.close();

            phantomjs_exe.setReadable(true, false);
            phantomjs_exe.setExecutable(true, false);
            phantomjs_exe.setWritable(true, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return phantomjs_exe;
    }
}
