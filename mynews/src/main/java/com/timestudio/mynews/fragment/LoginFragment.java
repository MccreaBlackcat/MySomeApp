package com.timestudio.mynews.fragment;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.timestudio.mynews.R;
import com.timestudio.mynews.activity.MainActivity;
import com.timestudio.mynews.util.ConnectUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    //碎片控件
    TextView tv_login_login;
    TextView tv_login_register;
    EditText et_account_number;
    EditText et_password;
    EditText et_password_again;
    EditText et_emails;
    TextView tv_link;
    Button btn_login;
    TextView tv_lost_password;

    //帐号密码
    private String account_number;
    private String password;
    private String password_again;
    private String emails;



    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        //绑定控件
        tv_login_login = (TextView) v.findViewById(R.id.tv_login_login);
        tv_login_register = (TextView) v.findViewById(R.id.tv_login_register);
        et_account_number = (EditText) v.findViewById(R.id.et_account_number);
        et_password = (EditText) v.findViewById(R.id.et_password);
        et_password_again = (EditText) v.findViewById(R.id.et_password_again);
        et_emails = (EditText) v.findViewById(R.id.et_emails);
        tv_link = (TextView) v.findViewById(R.id.tv_link);
        btn_login = (Button) v.findViewById(R.id.btn_login);
        btn_login.setEnabled(false);
        btn_login.setBackgroundResource(R.color.sel_btn_login_true);
        setListener();

        return v;
    }


    //设置监听
    private void setListener() {
        tv_login_login.setOnClickListener(this);
        tv_login_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        //监听输入状态和获取输入后的文本
        et_account_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                account_number = s.toString();
                if (account_number.equals("")) {
                    btn_login.setEnabled(true);
                    btn_login.setBackgroundResource(R.color.sel_btn_login_true);
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
                if (password.equals("")) {
                    btn_login.setEnabled(true);
                    btn_login.setBackgroundResource(R.color.sel_btn_login_true);
                } else if(!account_number.equals("")){
                    btn_login.setEnabled(true);
                    btn_login.setBackgroundResource(R.drawable.sle_btn_login);
                }
            }
        });
        et_password_again.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password_again = s.toString();
            }
        });
        et_emails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emails = s.toString();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_login:
                btn_login.setText(getString(R.string.fragment_login_login));
                et_password_again.setVisibility(View.INVISIBLE);
                et_emails.setVisibility(View.INVISIBLE);
                tv_link.setVisibility(View.INVISIBLE);
                et_account_number.setText("");
                et_password.setText("");
                btn_login.setEnabled(false);


                break;
            case R.id.tv_login_register:
                btn_login.setText(getString(R.string.fragment_login_register));
                et_password_again.setVisibility(View.VISIBLE);
                et_emails.setVisibility(View.VISIBLE);
                tv_link.setVisibility(View.VISIBLE);
                et_account_number.setText("");
                et_password.setText("");
                et_password_again.setText("");
                et_emails.setText("");
                btn_login.setEnabled(false);
                break;
            case R.id.btn_login:
                ConnectUtil connectUtil = new ConnectUtil();
                if (!ConnectUtil.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.netWork_not), Toast.LENGTH_SHORT).show();
                } else {
                    if (btn_login.getText().equals("登录")) {
                        if (connectUtil.inspectUserName(account_number) && connectUtil.inspectPassWord(password)) {
                            MyAsyncTask task = new MyAsyncTask();
                            Log.i("shen", et_account_number.getText().toString());
                            Log.i("shen", et_password.getText().toString());
                            task.execute(ConnectUtil.APPCONET, account_number, password);
                        } else {
                            Toast.makeText(getActivity(),getString(R.string.fragment_login_error),Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (btn_login.getText().equals("注册")) {
                        MyRegisterTask task = new MyRegisterTask();
                        if (password.equals(password_again)) {
                            if (connectUtil.inspectUserName(account_number)) {
                                if (connectUtil.inspectPassWord(password)) {
                                    if (connectUtil.inspectEmails(emails)) {
                                        task.execute(ConnectUtil.APPCONET , account_number , emails, password );
                                    } else {
                                        Toast.makeText(getActivity(),getString(R.string.fragment_login_error_emails),Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(),getString(R.string.fragment_login_error_password),Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(),getString(R.string.fragment_login_error_account),Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(),getString(R.string.fragment_login_error_2password),Toast.LENGTH_SHORT).show();
                            et_password.setText("");
                            et_password_again.setText("");btn_login.setEnabled(false);
                        }

                    }
                }
                break;
        }
    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuffer strContent = null;
            try {
                // 建立连接
                URL url = new URL(params[0] + "/user_login");
                String content = "ver=1" + "&uid=" + params[1] + "&pwd=" + params[2] + "&device=0";
                // 获取HttpURLConnection实例
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //设置请求方式
                connection.setRequestMethod("POST");
                //设置请求头格式
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", content.length() + "");
                //允许HttpURLConnection实例可以使用输出流
                connection.setDoOutput(true);
                //获取输出流，输出
                OutputStream op = connection.getOutputStream();
                op.write(content.getBytes());
                //关闭输出流
                op.close();
                //获取响应码
                int code = connection.getResponseCode();
                //判断响应
                if (code == 200) {
                    //获取服务器响应
                    BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                    //建立字节缓冲区
                    byte[] buffer = new byte[1024];
                    int count = 0;
                    strContent = new StringBuffer();
                    while ((count = in.read(buffer)) != -1) {
                        strContent.append(new String(buffer, 0, count));
                    }
                    in.close();
                }

                // 设置请求方式(默认为GET)
//                connection.setRequestMethod("GET");
                // 建立输入流,接收网络获取到的输入流
//                InputStream in = connection.getInputStream();
                // 转换成高级流,速度快
//                BufferedInputStream bu = new BufferedInputStream(in);
                //  404   获取相应码200 为正常
//                int code = connection.getResponseCode();
                // 创建缓冲区,把io流转换成字符串
//                byte[] st = new byte[1024];
//                int count = 0;
//                strContent = new StringBuffer();
//                while ((count = bu.read(st)) != -1) {
//                    strContent.append(new String(st, 0, count));
//                }
//                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return strContent.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                // 解析字符串
                Log.i("shen",s);
                //实例化解析对象
                JSONObject jsonObject = new JSONObject(s);
                //获取解析对象jsonObject中对应KEY的内容
                String result;
                String message = jsonObject.getString("message");
                result = message;
                if (message.equals("OK")) {
                    JSONObject object = jsonObject.getJSONObject("data");
                    String explain = object.getString("explain");
                    //登录成功，保存信息到SharedPreference
                    SharedPreferences preferences = getActivity().getSharedPreferences("login", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("login", true);
                    editor.putString("token", object.getString("token"));
                    editor.putString("user",account_number);
                    editor.commit();
                    ((MainActivity)getActivity()).addMainFragment(account_number);
                    result = explain;
                } else {
                    et_password.setText("");
                    et_account_number.setText("");
                    btn_login.setEnabled(false);
                    btn_login.setBackgroundColor(getResources().getColor(R.color.sel_btn_login_true));
                }
                Toast.makeText(getActivity(), result,Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class MyRegisterTask extends AsyncTask<String , Void ,String>{

        @Override
        protected String doInBackground(String... params) {
            StringBuffer strContent = null;
            try {
                //建立连接
                URL url = new URL(params[0] + "user_register");
                String content = "ver=1" + "&uid=" + params[1] + "&email=" + params[2] + "&pwd=" + params[3] ;
                //获取HttpURLConnection实力
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //设置请求方式
                urlConnection.setRequestMethod("POST");
                //设置请求头格式
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", content.length() + "");
                //允许HttpURLConnection实例可以使用输出流
                urlConnection.setDoOutput(true);
                //获取输出流，输出
                OutputStream op = urlConnection.getOutputStream();
                //输出
                op.write(content.getBytes());
                //关闭输出流
                op.close();
                //获取响应码
                int code = urlConnection.getResponseCode();
                //判断响应是否成功
                if (code == 200) {
                    //获取服务器响应
                    BufferedInputStream bIp = new BufferedInputStream(urlConnection.getInputStream());
                    //建立字节缓冲区
                    byte[] buffer = new byte[1024];
                    int count = 0;
                    strContent = new StringBuffer();
                    while ((count = bIp.read(buffer)) != -1) {
                        strContent.append(new String(buffer), 0, count);
                    }
                    bIp.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strContent.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //利用JSONObject解析字符串
            try {
                JSONObject jsonObject = new JSONObject(s);
                //获取到对象中对应的KEY的值
                String result;
                String message = jsonObject.getString("message");
                result = message;
                if (message.equals("OK")) {
                    //解析data里的数据
                    JSONObject object = jsonObject.getJSONObject("data");
                    String explain = object.getString("explain");
                    if (explain.equals("注册成功")) {
                        new MyAsyncTask().execute(ConnectUtil.APPCONET, account_number, password);
                    } else {
                        et_password.setText("");
                        et_account_number.setText("");
                        et_password_again.setText("");
                        et_emails.setText("");
                        btn_login.setEnabled(false);
                        btn_login.setBackgroundResource(R.color.sel_btn_login_true);
                    }
                    result = explain;
                } else {
                    et_password.setText("");
                    et_account_number.setText("");
                    et_password_again.setText("");
                    et_emails.setText("");
                    btn_login.setEnabled(false);
                    btn_login.setBackgroundResource(R.color.sel_btn_login_true);

                }
                Toast.makeText(getActivity(), result,Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
