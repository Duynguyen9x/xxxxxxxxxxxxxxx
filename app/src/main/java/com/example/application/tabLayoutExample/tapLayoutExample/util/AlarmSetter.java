//package Util;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
///**
// * Created by 8470p on 11/11/2016.
// */
//public class AlarmSetter extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Intent service = new Intent(context, AlarmService.class);
//        service.setAction(AlarmService.CREATE);
//        context.startService(service);
//    }
//
//}