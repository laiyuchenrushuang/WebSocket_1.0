package soket.hc.com.websoketdemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private static final int FRAME_QUEUE_SIZE = 5;
    private static final int CONNECT_TIMEOUT = 5000;
    @BindView(R.id.button1)
    Button kaishi;

    @BindView(R.id.show)
    EditText show;


    private WsListener wsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WebSocket ws = new WebSocketFactory().createSocket("ws://echo.websocket.org", CONNECT_TIMEOUT) //ws地址，和设置超时时间
                            .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
                            .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                            .addListener(wsListener = new WsListener())//添加回调监听
                            .connectAsynchronously();//异步连接

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


//        webSocketConnect();

    }

    private OkHttpClient mOkHttpClient;

    private void webSocketConnect() {
        mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("ws://echo.websocket.org")
                .build();
        ClientWebSocketListener listener = new ClientWebSocketListener();
        mOkHttpClient.newWebSocket(request, listener);
        mOkHttpClient.dispatcher().executorService().shutdown();
    }

    /**
     * 继承默认的监听空实现WebSocketAdapter,重写我们需要的方法
     * onTextMessage 收到文字信息
     * onConnected 连接成功
     * onConnectError 连接失败
     * onDisconnected 连接关闭
     */
    class WsListener extends WebSocketAdapter {
        @Override
        public void onTextMessage(WebSocket websocket, String text) throws Exception {
            super.onTextMessage(websocket, text);
            String[] msgs = text.split("\\|");
            if (msgs.length >= 2) {
//                NotificationShow(msgs[0], msgs[1]);
//                sendReceiveMessageBroadcast(msgs[0], msgs[1]);
            }
            Log.i("lylog", "text = " + text);
        }

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers)
                throws Exception {
            super.onConnected(websocket, headers);
            websocket.sendText("hahaha");
            Log.i("lylog", "连接成功");
        }

        @Override
        public void onConnectError(WebSocket websocket, WebSocketException exception)
                throws Exception {
            super.onConnectError(websocket, exception);
            Log.i("lylog", "连接错误");
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer)
                throws Exception {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
        }

    }
}
//