package cherry.tutorial.querydsl.db;

import javax.annotation.processing.Generated;

/**
 * BTodo is a Querydsl bean type
 */
@Generated("com.querydsl.codegen.BeanSerializer")
public class BTodo {

    private java.time.LocalDateTime createdAt;

    private String description;

    private java.time.LocalDateTime doneAt;

    private Integer doneFlg;

    private java.time.LocalDate dueDt;

    private Long id;

    private Integer lockVersion;

    private java.time.LocalDateTime postedAt;

    private String postedBy;

    private java.time.LocalDateTime updatedAt;

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.time.LocalDateTime getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(java.time.LocalDateTime doneAt) {
        this.doneAt = doneAt;
    }

    public Integer getDoneFlg() {
        return doneFlg;
    }

    public void setDoneFlg(Integer doneFlg) {
        this.doneFlg = doneFlg;
    }

    public java.time.LocalDate getDueDt() {
        return dueDt;
    }

    public void setDueDt(java.time.LocalDate dueDt) {
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

    public java.time.LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(java.time.LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
         return "createdAt = " + createdAt + ", description = " + description + ", doneAt = " + doneAt + ", doneFlg = " + doneFlg + ", dueDt = " + dueDt + ", id = " + id + ", lockVersion = " + lockVersion + ", postedAt = " + postedAt + ", postedBy = " + postedBy + ", updatedAt = " + updatedAt;
    }

}

