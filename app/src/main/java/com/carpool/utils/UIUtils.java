/*
 * Copyright 2014 Google Inc. All rights reserved.
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

package com.carpool.utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class UIUtils {

    protected ActionBarActivity mActivity;

    private UIUtils(ActionBarActivity activity) {
        mActivity = activity;
    }

    public static UIUtils getInstance(ActionBarActivity activity) {
        return new UIUtils(activity);
    }

    /**
     * Convert dp to px
     *
     * @author Sachin
     * @param i
     * @param mContext
     * @return
     */

    public static int dpToPx(int i, Context mContext) {

        DisplayMetrics displayMetrics = mContext.getResources()
                .getDisplayMetrics();
        return (int) ((i * displayMetrics.density) + 0.5);
    }

}
