/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.providers.media;

import android.content.Context;
import android.content.ContentValues;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.hardware.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


public class UsbReceiver extends BroadcastReceiver
{
    private final static String TAG = "UsbReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        boolean connected = extras.getBoolean(UsbManager.USB_CONNECTED);
        boolean mtpEnabled = UsbManager.USB_FUNCTION_ENABLED.equals(
                extras.getString(UsbManager.USB_FUNCTION_MTP));
        Log.d(TAG, "USB_CONNECTED: " + connected + " mtpEnabled: " + mtpEnabled);
        if (connected && mtpEnabled) {
            // make sure external media database is open.
            // this will trigger MtpService to start
            try {
                ContentValues values = new ContentValues();
                values.put("name", MediaProvider.EXTERNAL_VOLUME);
                context.getContentResolver().insert(Uri.parse("content://media/"), values);
            } catch (IllegalArgumentException ex) {
                Log.w(TAG, "failed to open media database");
            }
        }
    }

}


