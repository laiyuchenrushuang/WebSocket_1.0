package soket.hc.com.websoketdemo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by ly on 2019/6/17.
 */

class ClientWebSocketListener extends WebSocketListener {
    private WebSocket mWebSocket;
    private Handler mWebSocketHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {


        }
    };
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        mWebSocket=webSocket;
        webSocket.send("hello world");
        webSocket.send("welcome");
        webSocket.send(ByteString.decodeHex("adef"));
        webSocket.close(1000, "再见");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Message message=Message.obtain();
        message.obj=text;
        mWebSocketHandler.sendMessage(message);
        Log.i("lylog","2222 + text = "+text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Message message=Message.obtain();
        message.obj=bytes.utf8();
        mWebSocketHandler.sendMessage(message);
        Log.i("lylog","3333  byte = "+(String)bytes.utf8());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        if(null!=mWebSocket){
            mWebSocket.close(1000,"再见");
            mWebSocket=null;
        }
        Log.i("lylog","4444  reason = "+reason + "  code = "+code);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.i("lylog","5555  reason = "+reason + "  code = "+code);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        Log.i("lylog","6666 Throwable = "+t.toString());
    }
}
