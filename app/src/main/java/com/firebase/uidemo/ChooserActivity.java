/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.firebase.uidemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.util.ExtraConstants;
import com.firebase.uidemo.auth.AuthUiActivity;
import com.firebase.uidemo.databinding.ActivityChooserBinding;
import com.firebase.uidemo.weather.WeatherActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class ChooserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AuthUI.canHandleIntent(getIntent())) {
            Intent intent = new Intent(ChooserActivity.this, AuthUiActivity
                    .class);
            intent.putExtra(ExtraConstants.EMAIL_LINK_SIGN_IN, Objects.requireNonNull(getIntent().getData()).toString());
            startActivity(intent);
            finish();
            return;
        }
        com.firebase.uidemo.databinding.ActivityChooserBinding mBinding = ActivityChooserBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.activities.setLayoutManager(new LinearLayoutManager(this));
        mBinding.activities.setAdapter(new ActivityChooserAdapter());
        mBinding.activities.setHasFixedSize(true);
    }

    private static class ActivityChooserAdapter
            extends RecyclerView.Adapter<ActivityStarterHolder> {
        private static final Class[] CLASSES = new Class[]{
                AuthUiActivity.class,
                WeatherActivity.class,
        };

        private static final int[] DESCRIPTION_NAMES = new int[]{
                R.string.title_auth_activity,
                R.string.title_weather_activity,
        };

        private static final int[] DESCRIPTION_IDS = new int[]{
                R.string.desc_auth,
                R.string.desc_weather,
        };

        @NonNull
        @Override
        public ActivityStarterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ActivityStarterHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.activity_chooser_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ActivityStarterHolder holder, int position) {
            holder.bind(CLASSES[position], DESCRIPTION_NAMES[position], DESCRIPTION_IDS[position]);
        }

        @Override
        public int getItemCount() {
            return CLASSES.length;
        }
    }

    private static class ActivityStarterHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView mTitle;
        private final TextView mDescription;

        private Class mStarterClass;

        public ActivityStarterHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.text1);
            mDescription = itemView.findViewById(R.id.text2);
        }

        private void bind(Class aClass, @StringRes int name, @StringRes int description) {
            mStarterClass = aClass;

            mTitle.setText(name);
            mDescription.setText(description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemView.getContext().startActivity(new Intent(itemView.getContext(), mStarterClass));
        }
    }
}
