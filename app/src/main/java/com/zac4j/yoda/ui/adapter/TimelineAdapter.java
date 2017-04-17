package com.zac4j.yoda.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sina.weibo.sdk.auth.sso.AccessTokenKeeper;
import com.zac4j.yoda.R;
import com.zac4j.yoda.data.model.ThumbUrl;
import com.zac4j.yoda.data.model.User;
import com.zac4j.yoda.data.model.Weibo;
import com.zac4j.yoda.di.ActivityContext;
import com.zac4j.yoda.ui.WebViewActivity;
import com.zac4j.yoda.ui.weibo.WeiboImageActivity;
import com.zac4j.yoda.ui.weibo.detail.WeiboDetailActivity;
import com.zac4j.yoda.util.TimeUtils;
import com.zac4j.yoda.util.WeiboTextHelper;
import com.zac4j.yoda.util.image.CircleTransformation;
import com.zac4j.yoda.util.image.PhotoUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

/**
 * Home Weibo List Adapter
 * Created by zac on 3/17/2017.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

  private Context mContext;
  private List<Weibo> mWeiboList;

  @Inject public TimelineAdapter(@ActivityContext Context context) {
    mContext = context;
    mWeiboList = new ArrayList<>();
  }

  public void addWeiboList(List<Weibo> weiboList) {
    if (weiboList == null || weiboList.isEmpty()) {
      return;
    }
    mWeiboList.addAll(weiboList);
    notifyDataSetChanged();
  }

  public void clear() {
    if (mWeiboList != null) {
      mWeiboList.clear();
    }
    notifyDataSetChanged();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.list_item_weibo_home, parent, false);
    return new ViewHolder(view);
  }

  @SuppressWarnings("deprecation") @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    if (mWeiboList == null || mWeiboList.isEmpty()) {
      return;
    }
    final Weibo weibo = mWeiboList.get(position);
    // 发送时间
    String pattern = "E MMM dd HH:mm:ss Z yyyy";
    String dateStr = TimeUtils.getDate(weibo.getCreatedAt(), pattern);
    holder.setPostTime(dateStr);
    // 微博内容
    String content = weibo.getText();
    holder.setContent(content);

    // 微博转发内容
    Weibo repostWeibo = weibo.getRepostWeibo();
    holder.setRepostContent(repostWeibo);

    // 转发数
    long repostCount = weibo.getRepostsCount();
    holder.setRepostNumber(repostCount);
    // 评论数
    long commentCount = weibo.getCommentsCount();
    holder.setReplyNumber(commentCount);
    // 点赞数
    long likeCount = weibo.getAttitudesCount();
    holder.setLikeNumber(likeCount);

    // 发送来源
    String source = weibo.getSource();
    if (!TextUtils.isEmpty(source)) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        holder.setPostSource(Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY));
      } else {
        holder.setPostSource(Html.fromHtml(source));
      }
    }
    // 用户信息
    setUserInfo(holder, weibo.getUser());

    // 多媒体消息
    String media = weibo.getBmiddlePic();
    boolean isMultiImages = weibo.getPicUrls().size() > 1;
    holder.setMediaContent(mContext, media, isMultiImages);
    ArrayList<ThumbUrl> imgUrlList = new ArrayList<>();
    if (isMultiImages) {
      imgUrlList = (ArrayList<ThumbUrl>) weibo.getPicUrls();
    } else {
      final String imgUrl = weibo.getOriginalPic();
      ThumbUrl thumbUrl = new ThumbUrl(imgUrl);
      imgUrlList.add(thumbUrl);
    }
    final Intent intent = new Intent(mContext, WeiboImageActivity.class);
    intent.putExtra(WeiboImageActivity.EXTRA_IMAGE_LIST, imgUrlList);
    holder.mMediaView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mContext.startActivity(intent);
      }
    });

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        goWeiboDetail(mContext, weibo);
      }
    });
  }

  private void goWeiboDetail(Context context, Weibo weibo) {
    if (weibo == null) {
      return;
    }
    final String uid = AccessTokenKeeper.readAccessToken(context).getUid();
    final String weiboUserId = weibo.getUser().getIdstr();
    if (uid.equals(weiboUserId)) { // 如果是自己发的微博
      Intent intent = new Intent(context, WeiboDetailActivity.class);
      intent.putExtra(WeiboDetailActivity.WEIBO_ID_EXTRA, weibo.getId());
      context.startActivity(intent);
    } else {
      Intent intent = new Intent(context, WebViewActivity.class);
      intent.putExtra(WebViewActivity.EXTRA_WEIBO_ID, weibo.getIdstr());
      intent.putExtra(WebViewActivity.EXTRA_UID, weiboUserId);
      context.startActivity(intent);
    }
  }

  private void setUserInfo(ViewHolder holder, User user) {
    if (user == null) {
      return;
    }
    holder.setAvatar(mContext, user.getProfileImageUrl());
    holder.setNickname(user.getScreenName());
    String username = "@" + user.getDomain();
    holder.setUsername(username);
  }

  @Override public int getItemCount() {
    if (mWeiboList == null || mWeiboList.isEmpty()) {
      return 0;
    }
    return mWeiboList.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.weibo_list_item_iv_avatar) ImageView mAvatarView;
    @BindView(R.id.weibo_list_item_tv_nickname) TextView mNicknameView;
    @BindView(R.id.weibo_list_item_tv_username) TextView mUsernameView;
    @BindView(R.id.weibo_list_item_tv_post_time) TextView mPostTimeView;
    @BindView(R.id.weibo_list_item_tv_post_from) TextView mPostSourceView;
    @BindView(R.id.weibo_list_item_tv_content) TextView mContentView;
    @BindView(R.id.weibo_list_item_tv_repost_content) TextView mRepostView;
    @BindView(R.id.weibo_list_item_iv_media) View mMediaView;
    @BindView(R.id.weibo_list_item_tv_repost) TextView mRepostBtn;
    @BindView(R.id.weibo_list_item_tv_reply) TextView mReplyBtn;
    @BindView(R.id.weibo_list_item_tv_like) TextView mLikeBtn;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(ViewHolder.this, itemView);
    }

    void setAvatar(Context context, String imageUrl) {
      if (TextUtils.isEmpty(imageUrl)) {
        Glide.clear(mAvatarView);
      } else {
        Glide.with(context)
            .load(imageUrl)
            .transform(new CircleTransformation(context))
            .into(mAvatarView);
      }
    }

    void setNickname(String nickname) {
      if (TextUtils.isEmpty(nickname)) {
        mNicknameView.setText("");
      } else {
        mNicknameView.setText(nickname);
      }
    }

    void setUsername(String username) {
      if (TextUtils.isEmpty(username)) {
        mUsernameView.setText("");
      } else {
        mUsernameView.setText(username);
      }
    }

    void setPostTime(String postTime) {
      if (TextUtils.isEmpty(postTime)) {
        mPostTimeView.setText("");
      } else {
        mPostTimeView.setText(postTime);
      }
    }

    void setPostSource(Spanned postSource) {
      if (TextUtils.isEmpty(postSource)) {
        mPostSourceView.setText("");
      } else {
        mPostSourceView.setText(postSource);
      }
    }

    void setContent(String content) {
      if (TextUtils.isEmpty(content)) {
        mContentView.setText("");
      } else {
        WeiboTextHelper.setupText(mContentView, content);
        //mContentView.setText(content);
      }
    }

    void setRepostContent(Weibo repostWeibo) {
      if (repostWeibo == null) {
        mRepostView.setText("");
        mRepostView.setBackgroundResource(0);
      } else {
        String weiboContent = repostWeibo.getText();
        String name = repostWeibo.getUser().getScreenName();

        if (TextUtils.isEmpty(weiboContent)) {
          mRepostView.setText("");
          return;
        }

        weiboContent = "@" + name + ": " + weiboContent;
        WeiboTextHelper.setupText(mRepostView, weiboContent);
        mRepostView.setBackgroundResource(R.drawable.bg_gray_border);
      }
    }

    void setMediaContent(final Context context, String mediaUrl, boolean isMultiImage) {
      final ImageView mediaImageView = (ImageView) mMediaView.findViewById(R.id.weibo_media_iv_img);
      final TextView mediaImageType = (TextView) mMediaView.findViewById(R.id.weibo_media_tv_type);
      if (TextUtils.isEmpty(mediaUrl)) {
        Glide.clear(mediaImageView);
        mediaImageType.setText("");
        mMediaView.setVisibility(View.GONE);
      } else {
        mMediaView.setVisibility(View.VISIBLE);

        Glide.with(context)
            .load(mediaUrl)
            .asBitmap()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mediaImageView);

        if (mediaUrl.endsWith("gif")) {
          mediaImageType.setText(R.string.weibo_media_type_gif);
          return;
        }

        if (isMultiImage) {
          mediaImageType.setText(R.string.weibo_media_type_multiple_image);
          return;
        }

        GenericRequestBuilder builder =
            PhotoUtils.getNetworkImageSizeRequest(context).load(Uri.parse(mediaUrl));
        builder.into(new SimpleTarget<BitmapFactory.Options>() {
          @Override public void onResourceReady(BitmapFactory.Options resource,
              GlideAnimation<? super BitmapFactory.Options> glideAnimation) {
            int imageHeight = resource.outHeight;
            if (imageHeight >= PhotoUtils.LONG_IMAGE_LENGTH) {
              mediaImageType.setText(R.string.weibo_media_type_long_image);
            } else {
              mediaImageType.setText("");
            }
          }
        });
      }
    }

    void setRepostNumber(long repostNumber) {
      mRepostBtn.setText(String.format(Locale.getDefault(), "%d", repostNumber));
    }

    void setReplyNumber(long replyNumber) {
      mReplyBtn.setText(String.format(Locale.getDefault(), "%d", replyNumber));
    }

    void setLikeNumber(long likeNumber) {
      mLikeBtn.setText(String.format(Locale.getDefault(), "%d", likeNumber));
    }
  }
}