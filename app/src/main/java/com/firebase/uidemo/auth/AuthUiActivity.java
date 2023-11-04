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

package com.firebase.uidemo.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.firebase.ui.auth.util.ExtraConstants;
import com.firebase.uidemo.R;
import com.firebase.uidemo.databinding.AuthUiLayoutBinding;
import com.firebase.uidemo.util.ConfigurationUtils;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class AuthUiActivity extends AppCompatActivity implements ActivityResultCallback<FirebaseAuthUIAuthenticationResult> {

    private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";
    private static final String FIREBASE_PRIVACY_POLICY_URL = "https://firebase.google.com/terms/analytics/#7_privacy";
    private AuthUiLayoutBinding mBinding;
    private final ActivityResultLauncher<Intent> signIn = registerForActivityResult(new FirebaseAuthUIActivityResultContract(), this);

    @NonNull
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, AuthUiActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = AuthUiLayoutBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.signIn.setOnClickListener(view -> signIn());

        mBinding.signInSilent.setOnClickListener(view -> silentSignIn());

        catchEmailLinkSignIn();
    }

    public void catchEmailLinkSignIn() {
        if (getIntent().getExtras() == null) {
            return;
        }
        String link = getIntent().getExtras().getString(ExtraConstants.EMAIL_LINK_SIGN_IN);
        if (link != null) {
            signInWithEmailLink(link);
        }
    }

    public void signIn() {
        signIn.launch(getSignInIntent(/*link=*/null));
    }

    public void signInWithEmailLink(@Nullable String link) {
        signIn.launch(getSignInIntent(link));
    }

    @NonNull
    public AuthUI getAuthUI() {
        AuthUI authUI = AuthUI.getInstance();
        return authUI;
    }

    private Intent getSignInIntent(@Nullable String link) {
        AuthUI.SignInIntentBuilder builder = getAuthUI().createSignInIntentBuilder()
                .setTheme(getSelectedTheme())
                .setLogo(getSelectedLogo())
                .setAvailableProviders(getSelectedProviders())
                .setIsSmartLockEnabled(false, false);

        if (getSelectedTosUrl() != null && getSelectedPrivacyPolicyUrl() != null) {
            builder.setTosAndPrivacyPolicyUrls(getSelectedTosUrl(), getSelectedPrivacyPolicyUrl());
        }

        if (link != null) {
            builder.setEmailLink(link);
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null && auth.getCurrentUser().isAnonymous()) {
            builder.enableAnonymousUsersAutoUpgrade();
        }
        return builder.build();
    }

    public void silentSignIn() {
        getAuthUI().silentSignIn(this, getSelectedProviders())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startSignedInActivity(null);
                    } else {
                        showSnackbar(R.string.sign_in_failed);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null && getIntent().getExtras() == null) {
            startSignedInActivity(null);
            finish();
        }
    }

    private void handleSignInResponse(int resultCode, @Nullable IdpResponse response) {
        // Successfully signed in
        if (resultCode == RESULT_OK) {
            startSignedInActivity(response);
            finish();
        } else {
            // Error handling ...
        }
    }

    private void startSignedInActivity(@Nullable IdpResponse response) {
        startActivity(SignedInActivity.createIntent(this, response));
    }

    @StyleRes
    private int getSelectedTheme() {
        // Simplified theme selection...
        return R.style.AppTheme;
    }

    @DrawableRes
    private int getSelectedLogo() {
        // Simplified logo selection...
        return R.drawable.firebase_auth_120dp;
    }

    private List<IdpConfig> getSelectedProviders() {
        List<IdpConfig> selectedProviders = new ArrayList<>();
        selectedProviders.add(new IdpConfig.EmailBuilder().build());
        selectedProviders.add(new IdpConfig.AnonymousBuilder().build());
        return selectedProviders;
    }

    @Nullable
    private String getSelectedTosUrl() {
        return FIREBASE_TOS_URL;
    }

    @Nullable
    private String getSelectedPrivacyPolicyUrl() {
        return FIREBASE_PRIVACY_POLICY_URL;
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mBinding.getRoot(), errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(@NonNull FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        handleSignInResponse(result.getResultCode(), response);
    }
}
