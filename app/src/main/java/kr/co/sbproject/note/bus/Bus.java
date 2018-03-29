/**
 * (주)오픈잇 | http://www.openit.co.kr
 * Copyright (c)2006-2018, openit Inc.
 * All rights reserved.
 */
package kr.co.sbproject.note.bus;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import kr.co.sbproject.note.listener.OnBusListener;

/**
 * The type Bus.
 * Rx를 이용한 이벤트 버스
 */
public class Bus {
    private static Bus mInstance;
    private HashMap<String, Data> maps = new HashMap<>();
    private HashMap<String, Disposable> disposables = new HashMap<>();

    private class Data {
        /**
         * The Subjects.
         */
        public BehaviorSubject subjects;
        /**
         * The Is complete.
         */
        boolean isComplete = false;
    }

    /**
     * Gets instance.생성자
     *
     * @return the instance
     */
    public static Bus getInstance() {
        if (mInstance == null) {
            synchronized (Bus.class) {
                if (mInstance == null) {
                    mInstance = new Bus();
                }
            }
        }
        return mInstance;
    }

    /**
     * Register.등록
     *
     * @param tags the tags
     * @return the boolean
     */
    public void register(String[] tags) {
        if (tags != null) {
            for (String tag : tags) {
                if (tag != null) {
                    isRegister(tag);
                }
            }
        }
    }

    /**
     * Register.등록
     *
     * @param tag the tag
     * @return the boolean
     */
    public void register(String tag) {
        if (tag != null) {
            isRegister(tag);
        }
    }

    /**
     * Register.등록 여부
     *
     * @param tag the tag
     */
    private boolean isRegister(String tag) {
        try {
            Data data = maps.get(tag);
            if (data == null) {
                data = new Data();
                data.subjects = BehaviorSubject.create();
                maps.put(tag, data);
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Deregister.해제
     *
     * @param tag the tag
     */
    public void deregister(String tag) {
        try {
            if (tag != null) {
                disposables.get(tag).dispose();
                if (disposables.get(tag).isDisposed()) {
                    disposables.remove(tag);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * Deregister.해제
     */
    public void deregister() {
        try {
            for (Map.Entry<String, Disposable> disposableEntry : disposables.entrySet()) {
                disposableEntry.getValue().dispose();
            }
            disposables.clear();
        } catch (Exception e) {
        }
    }

    /**
     * Subscribe.실행
     *
     * @param tag      the tag
     * @param listener the listener
     */
    public void subscribe(final String tag, final OnBusListener listener) {
        final Data data = maps.get(tag);
        if (data != null) {
            synchronized (data.subjects) {
                Disposable disposable = disposables.get(tag);
                if (disposable == null) {
                    disposable = data.subjects.subscribe(o -> {
                        try {
                            if (!data.isComplete) {
                                data.isComplete = listener.onEvent(tag, o);
                            }
                        } catch (Exception e) {
                            listener.onError(tag, e);
                        }
                    });
                    disposables.put(tag, disposable);
                }
            }
        } else {
            if (isRegister(tag)) {
                subscribe(tag, listener);
            }
        }
    }

    /**
     * Post.전달
     * Subject이 없을 경우 생성하여 다시 전달
     *
     * @param tag the tag
     * @param o   the o
     */
    public void send(String tag, Object o) {
        Data data = maps.get(tag);
        if (data != null) {
            data.isComplete = false;
            data.subjects.onNext(o);
        } else {
            if (isRegister(tag)) {
                send(tag, o);
            }
        }
    }
}
