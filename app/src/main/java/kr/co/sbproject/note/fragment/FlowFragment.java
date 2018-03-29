/**
 * (주)오픈잇 | http://www.openit.co.kr
 * Copyright (c)2006-2018, openit Inc.
 * All rights reserved.
 */
package kr.co.sbproject.note.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;

import kr.co.sbproject.note.bus.Bus;
import kr.co.sbproject.note.constant.Common;
import kr.co.sbproject.note.listener.OnBusListener;

public abstract class FlowFragment extends Fragment implements OnBusListener {
    public final Bus mBus;
    private String key;

    public abstract void onView();

    public abstract boolean onBusEvent(String tag, Object o);

    public abstract void onBusError(String tag, Exception e);

    public FlowFragment() {
        this.mBus = Bus.getInstance();
    }

    public void setInit(String key) {
        this.key = key;
        mBus.send(Common.Key.MAIN, key);
        mBus.subscribe(key, this);
        onView();
    }

    @Override
    public void onPause() {
        mBus.deregister(key);
        super.onPause();
    }

    @Override
    public boolean onEvent(String tag, Object o) {
        Log.i("sgim", String.format("%s  %s", tag, o.toString()));
        return onBusEvent(tag, o);
    }

    @Override
    public void onError(String tag, Exception e) {
        onBusError(tag, e);
    }
}
