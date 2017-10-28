package com.example.http;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView tv_data;
    private String jsonData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_Finddata = (Button) findViewById(R.id.btn_finddata);
        tv_data = (TextView) findViewById(R.id.tv_data);
        btn_Finddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity", "按钮点击，从服务器获取数据ing...");
                getDataFromServer();
                //getDataByOkhttp();
            }
        });
    }

    /*private void getDataByOkhttp() {
        //1-获取一个okhttp对象
        new OkHttpClient.Builder();
        //2-创建发送网咯请求的对象

        //3-发送网络请求
    }*/

    private void getDataFromServer() {
        //连接服务器，操作是耗时操作，所有的耗时操作都要放在子线程中
        //启动一个子线程
        new Thread() {
            public void run() {
                //1--连接的服务器的ip，本机，10.0.2.2--------本机IP地址（android）---wifi
                //访问的数据:http://10.0.2.2:8080/student.json
                //地址封装成url对象
                //URL url=new URL("http://10.0.0.2:8080/student.json");
                //genymotion使用本机的10.0.0.3端口

                try {
                    URL url = new URL("http://10.0.3.2:8080/student.json");

                    //2--创建一个连接
                    HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

                    //3--设置连接超时的时间
                    httpConnection.setConnectTimeout(2000);//毫秒
                    InputStream in = httpConnection.getInputStream();

                    //BufferedReader br =new BufferedReader(new InputStreamReader(in));

                    //4--获取连接返回的代码
                    if (httpConnection.getResponseCode() == 200) {

                        byte[] buf = new byte[1024];
                        int len = 0;

                        while ((len = in.read(buf)) != -1) {
                            jsonData = jsonData + new String(buf, 0, len);
                        }
                        //jsonData接收到的数据
                        //Log.i("MainActivity","服务器接收的数据："+jsonData);
                        JSONObject jsonObject = new JSONObject(jsonData);
                        //解析json字符串
                        parseJson(jsonData);
                        //showInfo();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

   /* private void showInfo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cgdsb.setText(jsonData);
            }
        });
    }*/

    private void parseJson(String jsonData) {
        String name = "";//复制一行,ctrl+d
        String address = "";
        String sex = "";
        int age = 0;
        //1--创建Json对象,根据json字符串
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            //2--根据关键字,找到对应的值
            if (jsonObject.has("name")) {
                name = jsonObject.getString("name");
            }
            if (jsonObject.has("address")) {
                address = jsonObject.getString("address");
            }
            if (jsonObject.has("sex")) {
                sex = jsonObject.getString("sex");
            }
            if (jsonObject.has("age")) {
                age = jsonObject.getInt("age");
            }
            Log.i("MainActivity", "解析的json:");
            Log.i("MainActivity", "名字:" + name);
            Log.i("MainActivity", "地址:" + address);
            Log.i("MainActivity", "性别:" + sex);
            Log.i("MainActivity", "年龄:" + age);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
