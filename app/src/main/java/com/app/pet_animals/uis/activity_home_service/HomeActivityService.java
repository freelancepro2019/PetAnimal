package com.app.pet_animals.uis.activity_home_service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.pet_animals.R;
import com.app.pet_animals.adapters.MyPagerAdapter;
import com.app.pet_animals.databinding.ActivityHomeServiceBinding;
import com.app.pet_animals.databinding.ActivityHomeUserBinding;
import com.app.pet_animals.language.Language;
import com.app.pet_animals.uis.activity_base.ActivityBase;
import com.app.pet_animals.uis.activity_home_user.FragmentNavigationBaseUser;
import com.app.pet_animals.uis.activity_login.LoginActivity;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class HomeActivityService extends ActivityBase implements ViewPager.OnPageChangeListener, NavigationBarView.OnItemSelectedListener {
    private ActivityHomeServiceBinding binding;
    private List<Fragment> fragments;
    private MyPagerAdapter adapter;
    private Map<Integer, Integer> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_service);
        initView();
    }

    private void initView() {
        map = new HashMap<>();
        fragments = new ArrayList<>();


        map.put(0, R.id.home);
        map.put(1, R.id.profile);
        fragments.add(FragmentNavigationBaseUser.newInstance(R.layout.fragment_home_base_service, R.id.navHomeHostFragmentService));
        fragments.add(FragmentNavigationBaseUser.newInstance(R.layout.fragment_profile_base_service, R.id.navProfileHostFragmentService));


        adapter = new MyPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments, new ArrayList<>());
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());
        binding.pager.addOnPageChangeListener(this);
        binding.bottomNavigationView.setOnItemSelectedListener(this);
    }

    public void logout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            clearUserModel();
            navigateToLoginActivity();
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int itemId = map.get(position);
        if (binding.bottomNavigationView.getSelectedItemId() != itemId) {
            binding.bottomNavigationView.setSelectedItemId(itemId);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int page = getPagePos(item.getItemId());
        if (page != binding.pager.getCurrentItem()) {
            binding.pager.setCurrentItem(page);
        }

        return true;
    }

    private int getPagePos(int itemId) {
        for (int pos : map.keySet()) {
            int id = map.get(pos);
            if (id == itemId) {
                return pos;
            }
        }
        return 0;
    }

    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }
    @Override
    public void onBackPressed() {
        FragmentNavigationBaseUser fragmentNavigationBase = (FragmentNavigationBaseUser) fragments.get(binding.pager.getCurrentItem());
        if (!fragmentNavigationBase.onBackPressed()) {
            if (binding.pager.getCurrentItem() != 0) {
                binding.pager.setCurrentItem(0);
            } else {
                super.onBackPressed();

            }
        }

    }

}