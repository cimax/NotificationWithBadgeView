/*
 * Copyright (C) 2014 Arturo Gutiérrez Díaz-Guerra.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.max.administrator.notificationwithbadgeview.com.max.administrator.notificationwithbadgeview.provider;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.max.administrator.notificationwithbadgeview.com.max.administrator.notificationwithbadgeview.activity.MainActivity;
import com.max.administrator.notificationwithbadgeview.com.max.administrator.notificationwithbadgeview.provider.collaborators.NicationInfo;


/**
 * Abstract class created to be implemented by different classes to provide badge change support on
 * different launchers.
 *
 * @author Arturo Gutiérrez Díaz-Guerra
 */

public abstract class BadgeProvider {

    protected Context mContext;
    protected NotificationManager mNotificationManager;
    protected Notification notification;
    public static NicationInfo defulteInfo = new NicationInfo(null, null, 0, 0, false, null);

    public BadgeProvider(Context context) {
        mContext = context;
    }

    protected void initNotification(NicationInfo miNicationInfo) {
        mNotificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (miNicationInfo.isNotifyed) {
            try {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                        .setContentTitle(miNicationInfo.Title).setContentText(miNicationInfo.content).setSmallIcon(miNicationInfo.icon).setAutoCancel(true);
                Intent i = new Intent();
                if (miNicationInfo.launchClass != null) {
                    i.setClass(mContext, miNicationInfo.launchClass);
                } else {
                    i = i.setClass(mContext, MainActivity.class);
                }
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (miNicationInfo.getBundle()!=null){
                    i.putExtras(miNicationInfo.getBundle());
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
                notification = builder
                        .setDefaults(NotificationCompat.DEFAULT_SOUND)
                        .setContentIntent(pendingIntent)
                        .build();
            } catch (Exception e) {
                if (e != null && e.getMessage() != null)
                    Log.e("BadgeProvider", e.getMessage());
            }
        }
    }

    public void notification(NicationInfo info) throws UnsupportedOperationException {
        initNotification(info);
        if (notification!=null){
            mNotificationManager.notify(0,notification);
        }
    }

    public void warningTone() throws UnsupportedOperationException {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(mContext, notification);
        r.play();
    }
    public abstract void setBadge(NicationInfo info) throws UnsupportedOperationException;
    public abstract void removeBadge() throws UnsupportedOperationException;

    protected String getPackageName() {
        return mContext.getPackageName();
    }

    protected String getMainActivityClassName() {
        return mContext.getPackageManager().getLaunchIntentForPackage(getPackageName()).getComponent().getClassName();
    }
}
