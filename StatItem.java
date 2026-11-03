package com.score.entity;

/**
 * 统计结果实体类（平均分、及格率等）
 * 用于教师端按课程 / 按班级统计展示
 */
public class StatItem {

    /** 统计维度名称（课程名或班级名） */
    private String name;
    /** 参考维度名称（班级统计时可能显示任课教师） */
    private String extra;
    /** 参与统计的学生人数 */
    private long studentCount;
    /** 平均分 */
    private double avgScore;
    /** 最高分 */
    private double maxScore;
    /** 最低分 */
    private double minScore;
    /** 及格率（%） */
    private double passRate;
    /** 优秀率（%）>=90 */
    private double excellentRate;
    /** 不及格人数（<60） */
    private long failCount;
    /** 分数段分布：[<60, 60-69, 70-79, 80-89, 90-100] */
    private long[] bands;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public long getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(long studentCount) {
        this.studentCount = studentCount;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public double getMinScore() {
        return minScore;
    }

    public void setMinScore(double minScore) {
        this.minScore = minScore;
    }

    public double getPassRate() {
        return passRate;
    }

    public void setPassRate(double passRate) {
        this.passRate = passRate;
    }

    public double getExcellentRate() {
        return excellentRate;
    }

    public void setExcellentRate(double excellentRate) {
        this.excellentRate = excellentRate;
    }

    public long getFailCount() {
        return failCount;
    }

    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }

    public long[] getBands() {
        return bands;
    }

    public void setBands(long[] bands) {
        this.bands = bands;
    }
}
