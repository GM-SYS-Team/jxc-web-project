package com.gms.entity.jxc;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author wuchuantong
 * @version V1.0
 * @Title: todo
 * @Package com.gms.entity.jxc
 * @Description: todo
 * @date 2017/11/18 23:05
 */
@Entity
@Table(name = "t_push_job")
public class PushJob {

    @Id
    @GeneratedValue
    @Column(name = "push_job_id")
    private Long id;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "url")
    private String url;

    @Basic
    @Column(name = "valid")
    private String valid;

    @Basic
    @Column(name = "open_type")
    private String openType;

    @Basic
    @Column(name = "object_id")
    private Integer objectId;

    @Basic
    @Column(name = "device_platform")
    private String devicePlatform;

    @Basic
    @Column(name = "push_platform")
    private String pushPlatform;

    //任务状态，0：已删除；1：已保存；2：已提交推送任务；3：已撤回；4：已推送；5：推送失败
    @Basic
    @Column(name = "push_status")
    private String pushStatus;

    @Basic
    @Column(name = "push_type")
    private String pushType;

    @Basic
    @Column(name = "push_policy_id")
    private String pushPolicyId;

    @Basic
    @Column(name = "min_version")
    private Integer minVersion;

    @Basic
    @Column(name = "max_version")
    private Integer maxVersion;

    @Basic
    @Column(name = "push_msg_id")
    private String pushMsgId;

    @Basic
    @Column(name = "push_time")
    private Timestamp pushTime;

    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;

    @Basic
    @Column(name = "modify_time")
    private Timestamp modifyTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }


    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getDevicePlatform() {
        return devicePlatform;
    }

    public void setDevicePlatform(String devicePlatform) {
        this.devicePlatform = devicePlatform;
    }

    public String getPushPlatform() {
        return pushPlatform;
    }

    public void setPushPlatform(String pushPlatform) {
        this.pushPlatform = pushPlatform;
    }

    public String getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus) {
        this.pushStatus = pushStatus;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getPushPolicyId() {
        return pushPolicyId;
    }

    public void setPushPolicyId(String pushPolicyId) {
        this.pushPolicyId = pushPolicyId;
    }

    public Integer getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(Integer minVersion) {
        this.minVersion = minVersion;
    }

    public Integer getMaxVersion() {
        return maxVersion;
    }

    public void setMaxVersion(Integer maxVersion) {
        this.maxVersion = maxVersion;
    }

    public String getPushMsgId() {
        return pushMsgId;
    }

    public void setPushMsgId(String pushMsgId) {
        this.pushMsgId = pushMsgId;
    }

    public Timestamp getPushTime() {
        return pushTime;
    }

    public void setPushTime(Timestamp pushTime) {
        this.pushTime = pushTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PushJob pushJob = (PushJob) o;

        if (id.equals(pushJob.id)) return false;
        if (title != null ? !title.equals(pushJob.title) : pushJob.title != null) return false;
        if (content != null ? !content.equals(pushJob.content) : pushJob.content != null) return false;
        if (url != null ? !url.equals(pushJob.url) : pushJob.url != null) return false;
        if (valid != null ? !valid.equals(pushJob.valid) : pushJob.valid != null) return false;
        if (openType != null ? !openType.equals(pushJob.openType) : pushJob.openType != null) return false;
        if (objectId != null ? !objectId.equals(pushJob.objectId) : pushJob.objectId != null) return false;
        if (devicePlatform != null ? !devicePlatform.equals(pushJob.devicePlatform) : pushJob.devicePlatform != null)
            return false;
        if (pushPlatform != null ? !pushPlatform.equals(pushJob.pushPlatform) : pushJob.pushPlatform != null)
            return false;
        if (pushStatus != null ? !pushStatus.equals(pushJob.pushStatus) : pushJob.pushStatus != null) return false;
        if (pushType != null ? !pushType.equals(pushJob.pushType) : pushJob.pushType != null) return false;
        if (pushPolicyId != null ? !pushPolicyId.equals(pushJob.pushPolicyId) : pushJob.pushPolicyId != null)
            return false;
        if (minVersion != null ? !minVersion.equals(pushJob.minVersion) : pushJob.minVersion != null) return false;
        if (maxVersion != null ? !maxVersion.equals(pushJob.maxVersion) : pushJob.maxVersion != null) return false;
        if (pushMsgId != null ? !pushMsgId.equals(pushJob.pushMsgId) : pushJob.pushMsgId != null) return false;
        if (pushTime != null ? !pushTime.equals(pushJob.pushTime) : pushJob.pushTime != null) return false;
        if (createTime != null ? !createTime.equals(pushJob.createTime) : pushJob.createTime != null) return false;
        if (modifyTime != null ? !modifyTime.equals(pushJob.modifyTime) : pushJob.modifyTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (valid != null ? valid.hashCode() : 0);
        result = 31 * result + (openType != null ? openType.hashCode() : 0);
        result = 31 * result + (objectId != null ? objectId.hashCode() : 0);
        result = 31 * result + (devicePlatform != null ? devicePlatform.hashCode() : 0);
        result = 31 * result + (pushPlatform != null ? pushPlatform.hashCode() : 0);
        result = 31 * result + (pushStatus != null ? pushStatus.hashCode() : 0);
        result = 31 * result + (pushType != null ? pushType.hashCode() : 0);
        result = 31 * result + (pushPolicyId != null ? pushPolicyId.hashCode() : 0);
        result = 31 * result + (minVersion != null ? minVersion.hashCode() : 0);
        result = 31 * result + (maxVersion != null ? maxVersion.hashCode() : 0);
        result = 31 * result + (pushMsgId != null ? pushMsgId.hashCode() : 0);
        result = 31 * result + (pushTime != null ? pushTime.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (modifyTime != null ? modifyTime.hashCode() : 0);
        return result;
    }
}
