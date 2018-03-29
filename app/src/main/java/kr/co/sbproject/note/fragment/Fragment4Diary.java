/**
 * (주)오픈잇 | http://www.openit.co.kr
 * Copyright (c)2006-2018, openit Inc.
 * All rights reserved.
 */
package kr.co.sbproject.note.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.sbproject.note.R;
import kr.co.sbproject.note.constant.Common;

public class Fragment4Diary extends FlowFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setInit(Common.Key.FRAGMENT_DIARY);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment4_diary, container, false);
    }

    @Override
    public void onView() {

    }

    @Override
    public boolean onBusEvent(String tag, Object o) {
        return true;
    }

    @Override
    public void onBusError(String tag, Exception e) {

    }
}
