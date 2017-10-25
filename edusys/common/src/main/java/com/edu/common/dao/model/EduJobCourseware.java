package com.edu.common.dao.model;

import java.io.Serializable;

public class EduJobCourseware implements Serializable {
    /**
     * 编号ID
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 课件ID
     *
     * @mbg.generated
     */
    private Integer coursewareId;

    /**
     * 学习任务ID
     *
     * @mbg.generated
     */
    private Integer jobId;

    private Integer sortNum;

    private EduCourseware courseware;//课件


    public EduCourseware getCourseware() {
        return courseware;
    }

    public void setCourseware(EduCourseware courseware) {
        this.courseware = courseware;
    }

    private static final long serialVersionUID = 1L;

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCoursewareId() {
        return coursewareId;
    }

    public void setCoursewareId(Integer coursewareId) {
        this.coursewareId = coursewareId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", coursewareId=").append(coursewareId);
        sb.append(", jobId=").append(jobId);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        EduJobCourseware other = (EduJobCourseware) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCoursewareId() == null ? other.getCoursewareId() == null : this.getCoursewareId().equals(other.getCoursewareId()))
                && (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCoursewareId() == null) ? 0 : getCoursewareId().hashCode());
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        return result;
    }
}