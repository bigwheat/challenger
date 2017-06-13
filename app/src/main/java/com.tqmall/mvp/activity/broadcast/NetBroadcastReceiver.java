package com.tqmall.mvp.activity.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.tqmall.utils.NetUtil;

/**
 * Created by Jay on 17/1/11.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

        public  NetEventHandler netEventHandler;

        public static final String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(NET_CHANGE_ACTION)) {
                int netWorkState = NetUtil.getNetworkState(context);
                if(netEventHandler!=null){
                    netEventHandler.onNetChange(netWorkState);
                }
            }
        }

        public interface NetEventHandler {
             void onNetChange(int netWorkState);
        }

        public void setNetEventHandlerListener(NetEventHandler netEventHandler){
            this.netEventHandler=netEventHandler;
        }
}
