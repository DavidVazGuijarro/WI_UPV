package upv.welcomeincoming.com.infoFragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;

import upv.welcomeincoming.com.R;


/**
 * Created by Marcos on 30/04/14.
 */
public class Activity_Transportes extends FragmentActivity {
    ViewPager pager = null;
    PagerTabStrip tabStrip;
    FragmentPagerAdapter_Info pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info_transportes);

        this.pager = (ViewPager) findViewById(R.id.pager);
        this.tabStrip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
        pager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setAlpha(normalizedposition);
            }
        });
        pagerAdapter = new FragmentPagerAdapter_Info(getSupportFragmentManager(), this);
        pagerAdapter.addFragment(new Fragment_Metro());
        pagerAdapter.addFragment(new Fragment_EMT());
        pagerAdapter.addFragment(new Fragment_Avion());
        pagerAdapter.addFragment(new Fragment_Tren());
        pagerAdapter.addFragment(new Fragment_Taxi());
        pager.setAdapter(pagerAdapter);
    }
}