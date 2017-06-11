package com.junhee.android.mutiplecount;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvs[] = new TextView[4];
    public static final int SET_COUNT = 91240;

    // sub thread에서 전달받은 메시지를 받기 위한 컨트롤러인 handler를 생성함
    Handler handler = new Handler(){
        // sub thread에서 메시지를 전달하면 handleMessage 함수 동작

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SET_COUNT:
                    tvs[msg.arg1].setText("" + msg.arg2);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i =0; i < 4; i++){
            // 텍스트로 아이디 가져오기
            int resId = getResources().getIdentifier("textView" + (1+i), "id", getPackageName());
            tvs[i] = (TextView) findViewById(resId);
        }
        Counter counter_1 = new Counter(0, handler);
        counter_1.start();

        Counter counter_2 = new Counter(1, handler);
        counter_2.start();

        Counter counter_3 = new Counter(2, handler);
        counter_3.start();

        Counter counter_4 = new Counter(3, handler);
        counter_4.start();
    }

     class Counter extends Thread{
         Handler handler;
         int tvIndex;
         int count = 0;

         public Counter(int tvIndex, Handler handler){
             this.tvIndex = tvIndex;
             this.handler = handler;
         }

         @Override
         public void run() {
             for(int i=0; i<10; i++){
                 // sub thread에서 ui 조작하기 위해 핸들러를 통해 메시지 전달
                 count++;
                 Message msg = new Message();
                 msg.what = MainActivity.SET_COUNT;
                 msg.arg1 = tvIndex;
                 msg.arg2 = count;

                 handler.sendMessage(msg);

                 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         }
     }
}

