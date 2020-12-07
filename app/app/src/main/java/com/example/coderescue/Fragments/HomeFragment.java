package com.example.coderescue.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.coderescue.Adapters.SectionsPagerAdapter;
import com.example.coderescue.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
public class HomeFragment extends AppCompatActivity {

    ViewPager viewPager;
    soup.neumorphism.NeumorphCardView tile_victim, tile_rescue, tile_third, tile_notif;
    LinearLayout normal_victim, normal_rescue, normal_third, normal_notif;
    int totalFragments = 0;
    public static String diss_idd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home2);
        viewPager = findViewById(R.id.view_pager);
        diss_idd="aa";
        List<Fragment> fragments = getFragments();
        totalFragments = fragments.size();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(0);

    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new RescueTeamLoginFragment());
        return fragments;
    }


}