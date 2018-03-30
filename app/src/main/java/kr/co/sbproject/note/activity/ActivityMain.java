package kr.co.sbproject.note.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import io.realm.Realm;
import kr.co.sbproject.note.R;
import kr.co.sbproject.note.bus.Bus;
import kr.co.sbproject.note.constant.Common;
import kr.co.sbproject.note.fragment.Fragment1Home;
import kr.co.sbproject.note.fragment.Fragment2Memo;
import kr.co.sbproject.note.fragment.Fragment3Calendar;
import kr.co.sbproject.note.fragment.Fragment4Diary;
import kr.co.sbproject.note.fragment.Fragment5UserInfo;
import kr.co.sbproject.note.listener.OnBusListener;
import kr.co.sbproject.note.model.Data1Home;
import kr.co.sbproject.note.model.Data3Calendar;
import kr.co.sbproject.note.model.EventModel;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnBusListener {
    private Realm mRealm;
    private Bus mBus;
    private NavigationView nvView;
    private DrawerLayout drawerLayout;
    private View btnTitle;
    private TextView tvTitle1;
    private TextView tvTitle2;
    private TextView tvTitleSub;
    private Toolbar toolBar;

    public void setInit() {
        this.toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBar = inflater.inflate(R.layout.view_toolbar, null);
        getSupportActionBar().setCustomView(actionBar, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        btnTitle = actionBar.findViewById(R.id.btn_title);
        btnTitle.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        tvTitle1 = actionBar.findViewById(R.id.tv_title_1);
        tvTitle2 = actionBar.findViewById(R.id.tv_title_2);
        tvTitleSub = actionBar.findViewById(R.id.tv_title_sub);
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "Lobster_1.3.otf");
        tvTitle1.setTypeface(typeface);
        tvTitle2.setTypeface(typeface);
        tvTitleSub.setTypeface(typeface);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvView = (NavigationView) findViewById(R.id.nv_main);
        nvView.setNavigationItemSelectedListener(this);
        MenuItem item = nvView.getMenu().findItem(R.id.navi_item_6);
        try {
            item.setTitle(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (Exception e) {
        }

        mRealm = Realm.getDefaultInstance();
        mBus = Bus.getInstance();
        mBus.register(new String[]{Common.Key.MAIN, Common.Key.FRAGMENT_HOME, Common.Key.FRAGMENT_MEMO, Common.Key.FRAGMENT_CALENDAR, Common.Key.FRAGMENT_DIARY, Common.Key.FRAGMENT_SETTING});
        mBus.subscribe(Common.Key.MAIN, this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new Fragment1Home());
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navi_item_1:
                fragment = new Fragment1Home();
                break;
            case R.id.navi_item_2:
                fragment = new Fragment2Memo();
                break;
            case R.id.navi_item_3:
                fragment = new Fragment3Calendar();
                break;
            case R.id.navi_item_4:
                fragment = new Fragment4Diary();
                break;
            case R.id.navi_item_5:
                fragment = new Fragment5UserInfo();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        mBus.deregister();
        super.onDestroy();
    }

    @Override
    public boolean onEvent(String tag, Object o) {
        Log.i("sgim", String.format("%s  %s", tag, o.toString()));
        Gson gson = new Gson();
        EventModel data = gson.fromJson((String) o, EventModel.class);
        if (Common.Cast.DATA_MAIN.equals(data.cast)) {
            Data1Home main = gson.fromJson((String) o, Data1Home.class);
            Log.i("sgim", "DATA_MAIN " + main.data);
        } else if (Common.Cast.DATA_CALENDAR.equals(data.cast)) {
            Data3Calendar calendar = gson.fromJson((String) o, Data3Calendar.class);
            Log.i("sgim", "DATA_CALENDAR " + calendar.data);
        }
        return true;
    }

    @Override
    public void onError(String tag, Exception e) {

    }
}
