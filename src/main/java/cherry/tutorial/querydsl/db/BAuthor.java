package cherry.tutorial.querydsl.db;

import javax.annotation.processing.Generated;

/**
 * BAuthor is a Querydsl bean type
 */
@Generated("com.querydsl.codegen.BeanSerializer")
public class BAuthor {

    private java.time.LocalDateTime createdAt;

    private Integer deletedFlg;

    private Long id;

    private Integer lockVersion;

    private String loginId;

    private String name;

    private java.time.LocalDateTime updatedAt;

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getDeletedFlg() {
        return deletedFlg;
    }

    public void setDeletedFlg(Integer deletedFlg) {
        this.deletedFlg = deletedFlg;
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

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
         return "createdAt = " + createdAt + ", deletedFlg = " + deletedFlg + ", id = " + id + ", lockVersion = " + lockVersion + ", loginId = " + loginId + ", name = " + name + ", updatedAt = " + updatedAt;
    }

}

