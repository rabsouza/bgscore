package br.com.battista.bgscore.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Unique;

import java.io.Serializable;
import java.util.Date;

import br.com.battista.bgscore.constants.EntityConstant;
import br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry;


@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseEntity extends SugarRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Unique
    @Column(name = BaseEntry.COLUMN_NAME_PK, unique = true)
    private Long pk;

    @Column(name = BaseEntry.COLUMN_NAME_CREATED_AT, notNull = true)
    private Date createdAt;

    @Column(name = BaseEntry.COLUMN_NAME_UPDATED_AT, notNull = true)
    private Date updatedAt;

    @Column(name = BaseEntry.COLUMN_NAME_VERSION, notNull = true)
    private Long version;

    @Column(name = BaseEntry.COLUMN_NAME_ENTITY_SYNCHRONIZED, notNull = true)
    private Boolean entitySynchronized;

    @Column(name = BaseEntry.COLUMN_NAME_SYNCHRONIZED_AT)
    private Date synchronizedAt;

    public void initEntity() {
        createdAt = new Date();
        updatedAt = createdAt;
        version = EntityConstant.DEFAULT_VERSION;
        entitySynchronized = Boolean.FALSE;
        pk = getId();
    }

    public void synchronize() {
        if (getVersion() == null || getVersion() == 0) {
            initEntity();
        }

        entitySynchronized = Boolean.TRUE;
        synchronizedAt = new Date();
        pk = getId();
    }

    public void updateEntity() {
        updatedAt = new Date();
        entitySynchronized = Boolean.FALSE;
        version++;
        pk = getId();
    }

    public void reloadId(){
        if(getId() == null || getId() == 0){
            setId(pk);
        }
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
        setPk(id);
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equal(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("version", version)
                .add("entitySynchronized", entitySynchronized)
                .add("synchronizedAt", synchronizedAt)
                .toString();
    }

    public BaseEntity id(Long id) {
        setId(id);
        return this;
    }

    public BaseEntity createdAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public BaseEntity updatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public BaseEntity version(Long version) {
        this.version = version;
        return this;
    }

    public BaseEntity entitySynchronized(Boolean entitySynchronized) {
        this.entitySynchronized = entitySynchronized;
        return this;
    }

    public BaseEntity synchronizedAt(Date synchronizedAt) {
        this.synchronizedAt = synchronizedAt;
        return this;
    }
}
