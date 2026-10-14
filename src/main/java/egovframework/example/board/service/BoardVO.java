package egovframework.example.board.service;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

public class BoardVO implements Serializable {

    private Long biIdx;
    private Long uiIdx;
    private String uiName;
    private Integer biType;
    private String biTitle;
    private String biContent;
    private String biFilename;
    private String biFilepath;
    private Long biRefidx;
    private Timestamp timeCreated;
    private Timestamp timeModified;
    
    private String biCategory;
    private Integer recommendCount;

    public Integer getRecommendCount() {
		return recommendCount;
	}

	public void setRecommendCount(Integer recommendCount) {
		this.recommendCount = recommendCount;
	}

	public String getBiCategory() {
		return biCategory;
	}

	public void setBiCategory(String biCategory) {
		this.biCategory = biCategory;
	}

	// 업로드 파일
    private transient MultipartFile uploadFile;

    public Long getBiIdx() {
        return biIdx;
    }

    public void setBiIdx(Long biIdx) {
        this.biIdx = biIdx;
    }

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

    public Integer getBiType() {
        return biType;
    }

    public void setBiType(Integer biType) {
        this.biType = biType;
    }

    public String getBiTitle() {
        return biTitle;
    }

    public void setBiTitle(String biTitle) {
        this.biTitle = biTitle;
    }

    public String getBiContent() {
        return biContent;
    }

    public void setBiContent(String biContent) {
        this.biContent = biContent;
    }

    public String getBiFilename() {
        return biFilename;
    }

    public void setBiFilename(String biFilename) {
        this.biFilename = biFilename;
    }

    public String getBiFilepath() {
        return biFilepath;
    }

    public void setBiFilepath(String biFilepath) {
        this.biFilepath = biFilepath;
    }

    public Long getBiRefidx() {
        return biRefidx;
    }

    public void setBiRefidx(Long biRefidx) {
        this.biRefidx = biRefidx;
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

    public MultipartFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(MultipartFile uploadFile) {
        this.uploadFile = uploadFile;
    }
}