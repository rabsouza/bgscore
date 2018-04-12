package br.com.battista.bgscore.model.dto;

import android.os.SystemClock;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Date;

import br.com.battista.bgscore.constants.EntityConstant;
import br.com.battista.bgscore.util.LogUtils;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackupDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Date createdAt;

    private Date updatedAt;

    private Long version;

    private Boolean entitySynchronized;

    private Date synchronizedAt;

    private String deviceId;

    private String user;

    private String versionName;

    public void initEntity() {
        id = SystemClock.currentThreadTimeMillis();
        createdAt = new Date();
        updatedAt = createdAt;
        version = EntityConstant.DEFAULT_VERSION;
        entitySynchronized = Boolean.FALSE;
    }

    public void synchronize() {
        if (getVersion() == null || getVersion() == 0) {
            initEntity();
        }

        entitySynchronized = Boolean.TRUE;
        synchronizedAt = new Date();
    }

    public void updateEntity() {
        updatedAt = new Date();
        entitySynchronized = Boolean.FALSE;
        version++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getEntitySynchronized() {
        return entitySynchronized;
    }

    public void setEntitySynchronized(Boolean entitySynchronized) {
        this.entitySynchronized = entitySynchronized;
    }

    public Date getSynchronizedAt() {
        return synchronizedAt;
    }

    public void setSynchronizedAt(Date synchronizedAt) {
        this.synchronizedAt = synchronizedAt;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BackupDto)) return false;
        BackupDto backupDto = (BackupDto) o;
        return Objects.equal(getId(), backupDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public BackupDto deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public BackupDto user(String user) {
        this.user = user;
        return this;
    }

    public BackupDto versionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    @Override
    public String toString() {
        if(!LogUtils.isLoggable()){
            return EntityConstant.EMPTY_STRING;
        }

        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("version", version)
                .add("entitySynchronized", entitySynchronized)
                .add("synchronizedAt", synchronizedAt)
                .add("deviceId", deviceId)
                .add("user", user)
                .add("versionName", versionName)
                .toString();
    }
}
