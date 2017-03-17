package com.zac4j.yoda.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Weibo Model
 * Created by Zac on 2017/3/15.
 */
public class Weibo {

  @SerializedName("created_at") @Expose private String createdAt;
  @SerializedName("id") @Expose private Integer id;
  @SerializedName("mid") @Expose private String mid;
  @SerializedName("idstr") @Expose private String idstr;
  @SerializedName("text") @Expose private String text;
  @SerializedName("source_allowclick") @Expose private Integer sourceAllowclick;
  @SerializedName("source_type") @Expose private Integer sourceType;
  @SerializedName("source") @Expose private String source;
  @SerializedName("favorited") @Expose private Boolean favorited;
  @SerializedName("truncated") @Expose private Boolean truncated;
  @SerializedName("in_reply_to_status_id") @Expose private String inReplyToStatusId;
  @SerializedName("in_reply_to_user_id") @Expose private String inReplyToUserId;
  @SerializedName("in_reply_to_screen_name") @Expose private String inReplyToScreenName;
  @SerializedName("pic_urls") @Expose private List<Object> picUrls = null;
  @SerializedName("geo") @Expose private Object geo;
  @SerializedName("user") @Expose private User user;
  @SerializedName("retweeted_status") @Expose private RepostedWeibo repostedWeibo;
  @SerializedName("reposts_count") @Expose private Integer repostsCount;
  @SerializedName("comments_count") @Expose private Integer commentsCount;
  @SerializedName("attitudes_count") @Expose private Integer attitudesCount;
  @SerializedName("isLongText") @Expose private Boolean isLongText;
  @SerializedName("mlevel") @Expose private Integer mlevel;
  @SerializedName("visible") @Expose private Visible visible;
  @SerializedName("biz_feature") @Expose private Integer bizFeature;
  @SerializedName("hasActionTypeCard") @Expose private Integer hasActionTypeCard;
  @SerializedName("darwin_tags") @Expose private List<Object> darwinTags = null;
  @SerializedName("hot_weibo_tags") @Expose private List<Object> hotWeiboTags = null;
  @SerializedName("text_tag_tips") @Expose private List<Object> textTagTips = null;
  @SerializedName("rid") @Expose private String rid;
  @SerializedName("userType") @Expose private Integer userType;
  @SerializedName("cardid") @Expose private String cardid;
  @SerializedName("positive_recom_flag") @Expose private Integer positiveRecomFlag;
  @SerializedName("gif_ids") @Expose private String gifIds;
  @SerializedName("is_show_bulletin") @Expose private Integer isShowBulletin;

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getMid() {
    return mid;
  }

  public void setMid(String mid) {
    this.mid = mid;
  }

  public String getIdstr() {
    return idstr;
  }

  public void setIdstr(String idstr) {
    this.idstr = idstr;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Integer getSourceAllowclick() {
    return sourceAllowclick;
  }

  public void setSourceAllowclick(Integer sourceAllowclick) {
    this.sourceAllowclick = sourceAllowclick;
  }

  public Integer getSourceType() {
    return sourceType;
  }

  public void setSourceType(Integer sourceType) {
    this.sourceType = sourceType;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Boolean getFavorited() {
    return favorited;
  }

  public void setFavorited(Boolean favorited) {
    this.favorited = favorited;
  }

  public Boolean getTruncated() {
    return truncated;
  }

  public void setTruncated(Boolean truncated) {
    this.truncated = truncated;
  }

  public String getInReplyToStatusId() {
    return inReplyToStatusId;
  }

  public void setInReplyToStatusId(String inReplyToStatusId) {
    this.inReplyToStatusId = inReplyToStatusId;
  }

  public String getInReplyToUserId() {
    return inReplyToUserId;
  }

  public void setInReplyToUserId(String inReplyToUserId) {
    this.inReplyToUserId = inReplyToUserId;
  }

  public String getInReplyToScreenName() {
    return inReplyToScreenName;
  }

  public void setInReplyToScreenName(String inReplyToScreenName) {
    this.inReplyToScreenName = inReplyToScreenName;
  }

  public List<Object> getPicUrls() {
    return picUrls;
  }

  public void setPicUrls(List<Object> picUrls) {
    this.picUrls = picUrls;
  }

  public Object getGeo() {
    return geo;
  }

  public void setGeo(Object geo) {
    this.geo = geo;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public RepostedWeibo getRepostedWeibo() {
    return repostedWeibo;
  }

  public void setRetweetedStatus(RepostedWeibo repostedWeibo) {
    this.repostedWeibo = repostedWeibo;
  }

  public Integer getRepostsCount() {
    return repostsCount;
  }

  public void setRepostsCount(Integer repostsCount) {
    this.repostsCount = repostsCount;
  }

  public Integer getCommentsCount() {
    return commentsCount;
  }

  public void setCommentsCount(Integer commentsCount) {
    this.commentsCount = commentsCount;
  }

  public Integer getAttitudesCount() {
    return attitudesCount;
  }

  public void setAttitudesCount(Integer attitudesCount) {
    this.attitudesCount = attitudesCount;
  }

  public Boolean getIsLongText() {
    return isLongText;
  }

  public void setIsLongText(Boolean isLongText) {
    this.isLongText = isLongText;
  }

  public Integer getMlevel() {
    return mlevel;
  }

  public void setMlevel(Integer mlevel) {
    this.mlevel = mlevel;
  }

  public Visible getVisible() {
    return visible;
  }

  public void setVisible(Visible visible) {
    this.visible = visible;
  }

  public Integer getBizFeature() {
    return bizFeature;
  }

  public void setBizFeature(Integer bizFeature) {
    this.bizFeature = bizFeature;
  }

  public Integer getHasActionTypeCard() {
    return hasActionTypeCard;
  }

  public void setHasActionTypeCard(Integer hasActionTypeCard) {
    this.hasActionTypeCard = hasActionTypeCard;
  }

  public List<Object> getDarwinTags() {
    return darwinTags;
  }

  public void setDarwinTags(List<Object> darwinTags) {
    this.darwinTags = darwinTags;
  }

  public List<Object> getHotWeiboTags() {
    return hotWeiboTags;
  }

  public void setHotWeiboTags(List<Object> hotWeiboTags) {
    this.hotWeiboTags = hotWeiboTags;
  }

  public List<Object> getTextTagTips() {
    return textTagTips;
  }

  public void setTextTagTips(List<Object> textTagTips) {
    this.textTagTips = textTagTips;
  }

  public String getRid() {
    return rid;
  }

  public void setRid(String rid) {
    this.rid = rid;
  }

  public Integer getUserType() {
    return userType;
  }

  public void setUserType(Integer userType) {
    this.userType = userType;
  }

  public String getCardid() {
    return cardid;
  }

  public void setCardid(String cardid) {
    this.cardid = cardid;
  }

  public Integer getPositiveRecomFlag() {
    return positiveRecomFlag;
  }

  public void setPositiveRecomFlag(Integer positiveRecomFlag) {
    this.positiveRecomFlag = positiveRecomFlag;
  }

  public String getGifIds() {
    return gifIds;
  }

  public void setGifIds(String gifIds) {
    this.gifIds = gifIds;
  }

  public Integer getIsShowBulletin() {
    return isShowBulletin;
  }

  public void setIsShowBulletin(Integer isShowBulletin) {
    this.isShowBulletin = isShowBulletin;
  }
}
