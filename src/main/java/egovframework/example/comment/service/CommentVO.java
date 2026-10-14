package egovframework.example.comment.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class CommentVO implements Serializable {

    private Long biIdx;
    private Long uiIdx;
    private String uiName;
    private Integer biType;
    private String biContent;
    private Long biRefidx;
    private Timestamp timeCreated;
    private Timestamp timeModified;

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

	public String getBiContent() {
		return biContent;
	}

	public void setBiContent(String biContent) {
		this.biContent = biContent;
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

}