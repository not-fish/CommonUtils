package com.cus.main;

import com.cus.utils.HttpHelper;
import com.cus.utils.HttpHelperRequestHandler;

import java.net.URLConnection;

/**
 * @author Peko
 */
public class HttpHelperTest {
    public static void main(String[] args) {
        String result = "";
        String url = "xxx.xxx.xxx.xxx";
        String date = "this is date";
        try {
            result = HttpHelper.shareHelper().Post(url, date, "UTF-8",null,
                    new HttpHelperRequestHandler() {
                        @Override
                        public void OnPreSend(URLConnection request) {
                            request.addRequestProperty("Content-type", "application/json");
                        }
                    });
        } catch (Exception e) {
            System.out.println("post失败 " + url);
        }
    }
}
