package com.timestudio.mynews.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Interpolator;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.timestudio.mynews.BaseActivity;
import com.timestudio.mynews.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestActivity extends BaseActivity {

    ImageView iv_TestWrod;
    TextView tv_testContent;
    Bitmap bit;

    @Override
    protected int setContent() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        iv_TestWrod = (ImageView) findViewById(R.id.iv_TestWord);
        tv_testContent = (TextView) findViewById(R.id.tv_testContent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://paper.people.com.cn/rmrb/res/2016-10/19/01/rmrb2016101901p29_b.jpg");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream inp = urlConnection.getInputStream();
                    bit = BitmapFactory.decodeStream(inp);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_TestWrod.setImageBitmap(bit);
                        }
                    });

                }
            }
        }).start();
        task.execute("http://118.244.212.82:9092/newsClient/news_list?ver=1&subid=1&dir=2&nid=10&stamp=20161013&cnt=20");
    }

    @Override
    protected void setListener() {

    }

    AsyncTask<String ,Void , String> task = new AsyncTask<String, Void, String>() {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String str = null;
            try {
                Thread.sleep(3000);
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inp = connection.getInputStream();
                byte[] buffer = new byte[1024];
                while (inp.read(buffer) != -1) {
                    str += new String(buffer,0,buffer.length);
                    buffer = new byte[1024];
                }
                inp.close();



            } catch (Exception e) {
                e.printStackTrace();
            }finally {

            }
            return str;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            tv_testContent.setText(string);
        }
    };
}
