package com.zac4j.yoda.ui.weibo.send;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sina.weibo.sdk.auth.sso.AccessTokenKeeper;
import com.zac4j.yoda.R;
import com.zac4j.yoda.ui.base.BaseActivity;
import com.zac4j.yoda.ui.main.MainActivity;
import com.zac4j.yoda.ui.weibo.WeiboImageActivity;
import com.zac4j.yoda.util.PermissionHelper;
import com.zac4j.yoda.util.RetrofitHelper;
import com.zac4j.yoda.util.RxUtils;
import com.zac4j.yoda.util.image.ImageUtil;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Send Weibo
 * Created by zac on 3/27/2017.
 */

public class WeiboSendActivity extends BaseActivity implements WeiboSendView {

    private static final int REQUEST_PICK_CODE = 0x001;
    private static final int REQUEST_CAPTURE_CODE = 0x002;

    private static final int REQUEST_STORAGE_PERMISSION_CODE = 0x103;
    private final Map<String, RequestBody> mPictureWeiboMap = new HashMap<>();
    private final Map<String, String> mTextWeiboMap = new HashMap<>();
    @Inject
    WeiboSendPresenter mPresenter;
    @BindView(R.id.weibo_send_main_view)
    View mMainView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.root_layout)
    View mRootView;
    @BindView(R.id.weibo_send_media_container)
    FrameLayout mMediaContainer;
    @BindView(R.id.weibo_send_et_content)
    EditText mContentInput;
    @BindView(R.id.weibo_send_progressbar)
    ProgressBar mProgressBar;
    private boolean hasLocation = false;
    private boolean hasPicture = false;
    private boolean hasMultiPicture = false;

    private Uri mImageUri;
    private String mImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_send);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        mPresenter.attach(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({
        R.id.weibo_send_iv_action_camera, R.id.weibo_send_iv_action_gallery,
        R.id.weibo_send_iv_action_location, R.id.weibo_send_iv_action_visibility,
        R.id.weibo_send_tv_action_send
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.weibo_send_iv_action_camera:
                if (PermissionHelper.hasStoragePermission(WeiboSendActivity.this)) {
                    getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    mImagePath =
                        ImageUtil.capturePhoto(WeiboSendActivity.this, REQUEST_CAPTURE_CODE);
                } else {
                    PermissionHelper.requestStoragePermission(WeiboSendActivity.this,
                        REQUEST_STORAGE_PERMISSION_CODE);
                }
                break;
            case R.id.weibo_send_iv_action_gallery:
                ImageUtil.pickPhoto(WeiboSendActivity.this, REQUEST_PICK_CODE);
                break;
            case R.id.weibo_send_iv_action_location:
                break;
            case R.id.weibo_send_iv_action_visibility:
                break;
            case R.id.weibo_send_tv_action_send:
                String weiboContent = mContentInput.getText().toString();
                if (TextUtils.isEmpty(weiboContent)) {
                    showMessage("Not allowed empty weibo content !");
                    return;
                }
                setupWeiboContent(weiboContent);
                if (hasPicture) {
                    sendPictureWeibo();
                    return;
                }
                mPresenter.sendTextWeibo(mTextWeiboMap);
                break;
        }
    }

    private void setupWeiboContent(String content) {
        setupToken();
        if (hasPicture) {
            mPictureWeiboMap.put(getString(R.string.weibo_text),
                RetrofitHelper.createPartFromString(content));
        } else {
            mTextWeiboMap.put(getString(R.string.weibo_text), content);
        }
    }

    private void setupToken() {
        String token = AccessTokenKeeper.readAccessToken(WeiboSendActivity.this).getToken();
        if (TextUtils.isEmpty(token)) {
            return;
        }
        if (hasPicture) {
            mPictureWeiboMap.put(getString(R.string.weibo_token),
                RetrofitHelper.createPartFromString(token));
        } else {
            mTextWeiboMap.put(getString(R.string.weibo_token), token);
        }
    }

    private void sendPictureWeibo() {
        MultipartBody.Part imagePart =
            ImageUtil.prepareImagePart(WeiboSendActivity.this, getString(R.string.weibo_picture),
                mImageUri);
        mPresenter.sendPictureWeibo(mPictureWeiboMap, imagePart);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_PICK_CODE:
                if (data != null) {
                    mImageUri = data.getData();
                }
                break;
            case REQUEST_CAPTURE_CODE:
                mImageUri = Uri.fromFile(new File(mImagePath));
                break;
        }

        if (mImageUri == null) {
            return;
        }
        hasPicture = true;
        ImageView imageView = new ImageView(WeiboSendActivity.this);
        setupImageView(imageView, mImageUri);
    }

    private void setupImageView(ImageView imageView, final Uri uri) {
        FrameLayout.LayoutParams layoutParams =
            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeiboSendActivity.this, WeiboImageActivity.class);
                intent.putExtra(WeiboImageActivity.EXTRA_IMAGE_URI, uri);
                startActivity(intent);
            }
        });

        Glide.with(imageView.getContext())
            .load(uri)
            .apply(new RequestOptions().centerCrop())
            .into(imageView);
        mMediaContainer.addView(imageView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mImagePath = ImageUtil.capturePhoto(WeiboSendActivity.this, REQUEST_CAPTURE_CODE);
        } else {
            showMessage("Bad permission request !");
        }
    }

    @Override
    public void showProgress(boolean show) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(show ? VISIBLE : GONE);
        }
    }

    @Override
    public void showEmptyView(boolean show) {

    }

    //@Override public void onTokenInvalid() {
    //  AccessTokenKeeper.clear(this);
    //  startActivity(new Intent(this, LoginActivity.class));
    //}

    @Override
    public void showMessage(String message) {
        Snackbar.make(mRootView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void finishPublish() {
        final Intent intent = new Intent(WeiboSendActivity.this, MainActivity.class);
        Single.just("")
            .delay(2, TimeUnit.SECONDS)
            .compose(RxUtils.applySchedulers())
            .subscribeWith(new DisposableSingleObserver<Object>() {
                @Override
                public void onSuccess(Object o) {
                    startActivity(intent);
                }

                @Override
                public void onError(Throwable e) {
                    startActivity(intent);
                }
            });
    }
}
