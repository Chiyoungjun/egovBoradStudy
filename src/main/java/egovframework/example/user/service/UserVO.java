package egovframework.example.user.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserVO implements Serializable {

    private Long uiIdx;
    private String uiName;
    private String uiId;
    private String uiPwd;
    private Timestamp uiLastlogin;
    private Timestamp timeCreated;
    private Timestamp timeModified;

    // ID 저장 체크박스용
    private String saveId;

    public Long getUiIdx() {
        return uiIdx;
    }

    public void setUiIdx(Long uiIdx) {
        this.uiIdx = uiIdx;
    }

    public String getUiName() {
        return uiName;
    }

    public void setUiName(String uiName) {
        this.uiName = uiName;
    }

    public String getUiId() {
        return uiId;
    }

    public void setUiId(String uiId) {
        this.uiId = uiId;
    }

    public String getUiPwd() {
        return uiPwd;
    }

    public void setUiPwd(String uiPwd) {
        this.uiPwd = uiPwd;
    }

    public Timestamp getUiLastlogin() {
        return uiLastlogin;
    }

    public void setUiLastlogin(Timestamp uiLastlogin) {
        this.uiLastlogin = uiLastlogin;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Timestamp getTimeModified() {
        return timeModified;
    }

    public void setTimeModified(Timestamp timeModified) {
        this.timeModified = timeModified;
    }

    public String getSaveId() {
        return saveId;
    }

    public void setSaveId(String saveId) {
        this.saveId = saveId;
    }
}