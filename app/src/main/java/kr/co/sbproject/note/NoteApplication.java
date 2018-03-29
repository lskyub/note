/**
 * (주)오픈잇 | http://www.openit.co.kr
 * Copyright (c)2006-2018, openit Inc.
 * All rights reserved.
 */
package kr.co.sbproject.note;

import android.app.Application;

import io.realm.Realm;

public class NoteApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
