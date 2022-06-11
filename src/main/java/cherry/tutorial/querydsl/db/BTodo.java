package cherry.tutorial.querydsl.db;

import javax.annotation.processing.Generated;

/**
 * BTodo is a Querydsl bean type
 */
@Generated("com.querydsl.codegen.BeanSerializer")
public class BTodo {

    private java.sql.Timestamp createdAt;

    private Integer deletedFlg;

    private String description;

    private java.sql.Timestamp doneAt;

    private Integer doneFlg;

    private java.sql.Date dueDt;

    private Long id;

    private Integer lockVersion;

    private java.sql.Timestamp postedAt;

    private String postedBy;

    private java.sql.Timestamp updatedAt;

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getDeletedFlg() {
        return deletedFlg;
    }

    public void setDeletedFlg(Integer deletedFlg) {
        this.deletedFlg = deletedFlg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.sql.Timestamp getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(java.sql.Timestamp doneAt) {
        this.doneAt = doneAt;
    }

    public Integer getDoneFlg() {
        return doneFlg;
    }

    public void setDoneFlg(Integer doneFlg) {
        this.doneFlg = doneFlg;
    }

    public java.sql.Date getDueDt() {
        return dueDt;
    }

    public void setDueDt(java.sql.Date dueDt) {
        this.dueDt = dueDt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
    }

    public java.sql.Timestamp getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(java.sql.Timestamp postedAt) {
        this.postedAt = postedAt;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public java.sql.Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.sql.Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
         return "createdAt = " + createdAt + ", deletedFlg = " + deletedFlg + ", description = " + description + ", doneAt = " + doneAt + ", doneFlg = " + doneFlg + ", dueDt = " + dueDt + ", id = " + id + ", lockVersion = " + lockVersion + ", postedAt = " + postedAt + ", postedBy = " + postedBy + ", updatedAt = " + updatedAt;
    }

}

