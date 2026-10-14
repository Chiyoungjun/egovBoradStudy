package egovframework.example.recommend.service;

import java.io.Serializable;

public class RecommendVO implements Serializable {

    private Long biIdx;
    private Long uiIdx;
    private Integer recommendCount;
    private String recommendedYn;

    public Long getBiIdx(){
        return biIdx;
    }

    public void setBiIdx(Long biIdx){
        this.biIdx=biIdx;
    }

    public Long getUiIdx(){
        return uiIdx;
    }

    public void setUiIdx(Long uiIdx){
        this.uiIdx=uiIdx;
    }

    public Integer getRecommendCount(){
        return recommendCount;
    }

    public void setRecommendCount(Integer recommendCount){
        this.recommendCount=recommendCount;
    }

    public String getRecommendedYn(){
        return recommendedYn;
    }

    public void setRecommendedYn(String recommendedYn){
        this.recommendedYn=recommendedYn;
    }
}