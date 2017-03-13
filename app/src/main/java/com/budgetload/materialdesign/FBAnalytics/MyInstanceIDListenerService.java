package com.budgetload.materialdesign.FBAnalytics;

import android.util.Log;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by andrewlaurienrsocia on 11/11/2016.
 */

public class MyInstanceIDListenerService extends InstanceIDListenerService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        try {
            synchronized (this) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken("YOUR_GCM_SENDER_ID", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                AppEventsLogger.setPushNotificationsRegistrationId(token);
                Log.d("Data", token);
            }
        } catch (Exception e) {
            Log.e("test", "Failed to complete token refresh", e);
        }
    }

}
