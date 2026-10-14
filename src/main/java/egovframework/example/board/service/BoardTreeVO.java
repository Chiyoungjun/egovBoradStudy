package egovframework.example.board.service;

import java.io.Serializable;

public class BoardTreeVO implements Serializable {

    private String treeId;
    private String parentTreeId;
    private String treeName;
    private String nodeType;
    private String target;
    private String categoryCode;
    private Integer minRecommend;
    private Long biIdx;
    private Integer recommendCount;
    private Boolean isParent;

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getParentTreeId() {
        return parentTreeId;
    }

    public void setParentTreeId(String parentTreeId) {
        this.parentTreeId = parentTreeId;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Integer getMinRecommend() {
        return minRecommend;
    }

    public void setMinRecommend(Integer minRecommend) {
        this.minRecommend = minRecommend;
    }

    public Long getBiIdx() {
        return biIdx;
    }

    public void setBiIdx(Long biIdx) {
        this.biIdx = biIdx;
    }

    public Integer getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(Integer recommendCount) {
        this.recommendCount = recommendCount;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }
}