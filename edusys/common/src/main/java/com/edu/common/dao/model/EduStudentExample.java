package com.edu.common.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EduStudentExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    private Integer limit;

    private Integer offset;

    public EduStudentExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria implements Serializable {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria otherOperate(String condition){
            addCriterion(condition);
            return (Criteria) this;
        }

        public Criteria andStuIdIsNull() {
            addCriterion("stu_id is null");
            return (Criteria) this;
        }

        public Criteria andStuIdIsNotNull() {
            addCriterion("stu_id is not null");
            return (Criteria) this;
        }

        public Criteria andStuIdEqualTo(Integer value) {
            addCriterion("stu_id =", value, "stuId");
            return (Criteria) this;
        }

        public Criteria andStuIdNotEqualTo(Integer value) {
            addCriterion("stu_id <>", value, "stuId");
            return (Criteria) this;
        }

        public Criteria andStuIdGreaterThan(Integer value) {
            addCriterion("stu_id >", value, "stuId");
            return (Criteria) this;
        }

        public Criteria andStuIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("stu_id >=", value, "stuId");
            return (Criteria) this;
        }

        public Criteria andStuIdLessThan(Integer value) {
            addCriterion("stu_id <", value, "stuId");
            return (Criteria) this;
        }

        public Criteria andStuIdLessThanOrEqualTo(Integer value) {
            addCriterion("stu_id <=", value, "stuId");
            return (Criteria) this;
        }

        public Criteria andStuIdIn(List<Integer> values) {
            addCriterion("stu_id in", values, "stuId");
            return (Criteria) this;
        }

        public Criteria andStuIdNotIn(List<Integer> values) {
            addCriterion("stu_id not in", values, "stuId");
            return (Criteria) this;
        }

        public Criteria andStuIdBetween(Integer value1, Integer value2) {
            addCriterion("stu_id between", value1, value2, "stuId");
            return (Criteria) this;
        }

        public Criteria andStuIdNotBetween(Integer value1, Integer value2) {
            addCriterion("stu_id not between", value1, value2, "stuId");
            return (Criteria) this;
        }

        public Criteria andStuNameIsNull() {
            addCriterion("stu_name is null");
            return (Criteria) this;
        }

        public Criteria andStuNameIsNotNull() {
            addCriterion("stu_name is not null");
            return (Criteria) this;
        }

        public Criteria andStuNameEqualTo(String value) {
            addCriterion("stu_name =", value, "stuName");
            return (Criteria) this;
        }

        public Criteria andStuNameNotEqualTo(String value) {
            addCriterion("stu_name <>", value, "stuName");
            return (Criteria) this;
        }

        public Criteria andStuNameGreaterThan(String value) {
            addCriterion("stu_name >", value, "stuName");
            return (Criteria) this;
        }

        public Criteria andStuNameGreaterThanOrEqualTo(String value) {
            addCriterion("stu_name >=", value, "stuName");
            return (Criteria) this;
        }

        public Criteria andStuNameLessThan(String value) {
            addCriterion("stu_name <", value, "stuName");
            return (Criteria) this;
        }

        public Criteria andStuNameLessThanOrEqualTo(String value) {
            addCriterion("stu_name <=", value, "stuName");
            return (Criteria) this;
        }

        public Criteria andStuNameLike(String value) {
            addCriterion("stu_name like", value, "stuName");
            return (Criteria) this;
        }

        public Criteria andStuNameNotLike(String value) {
            addCriterion("stu_name not like", value, "stuName");
            return (Criteria) this;
        }

        public Criteria andStuNameIn(List<String> values) {
            addCriterion("stu_name in", values, "stuName");
            return (Criteria) this;
        }

        public Criteria andStuNameNotIn(List<String> values) {
            addCriterion("stu_name not in", values, "stuName");
            return (Criteria) this;
        }

        public Criteria andStuNameBetween(String value1, String value2) {
            addCriterion("stu_name between", value1, value2, "stuName");
            return (Criteria) this;
        }

        public Criteria andStuNameNotBetween(String value1, String value2) {
            addCriterion("stu_name not between", value1, value2, "stuName");
            return (Criteria) this;
        }

        public Criteria andCardNoIsNull() {
            addCriterion("card_no is null");
            return (Criteria) this;
        }

        public Criteria andCardNoIsNotNull() {
            addCriterion("card_no is not null");
            return (Criteria) this;
        }

        public Criteria andCardNoEqualTo(String value) {
            addCriterion("card_no =", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoNotEqualTo(String value) {
            addCriterion("card_no <>", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoGreaterThan(String value) {
            addCriterion("card_no >", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoGreaterThanOrEqualTo(String value) {
            addCriterion("card_no >=", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoLessThan(String value) {
            addCriterion("card_no <", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoLessThanOrEqualTo(String value) {
            addCriterion("card_no <=", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoLike(String value) {
            addCriterion("card_no like", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoNotLike(String value) {
            addCriterion("card_no not like", value, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoIn(List<String> values) {
            addCriterion("card_no in", values, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoNotIn(List<String> values) {
            addCriterion("card_no not in", values, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoBetween(String value1, String value2) {
            addCriterion("card_no between", value1, value2, "cardNo");
            return (Criteria) this;
        }

        public Criteria andCardNoNotBetween(String value1, String value2) {
            addCriterion("card_no not between", value1, value2, "cardNo");
            return (Criteria) this;
        }

        public Criteria andStuNoIsNull() {
            addCriterion("stu_no is null");
            return (Criteria) this;
        }

        public Criteria andStuNoIsNotNull() {
            addCriterion("stu_no is not null");
            return (Criteria) this;
        }

        public Criteria andStuNoEqualTo(String value) {
            addCriterion("stu_no =", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoNotEqualTo(String value) {
            addCriterion("stu_no <>", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoGreaterThan(String value) {
            addCriterion("stu_no >", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoGreaterThanOrEqualTo(String value) {
            addCriterion("stu_no >=", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoLessThan(String value) {
            addCriterion("stu_no <", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoLessThanOrEqualTo(String value) {
            addCriterion("stu_no <=", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoLike(String value) {
            addCriterion("stu_no like", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoNotLike(String value) {
            addCriterion("stu_no not like", value, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoIn(List<String> values) {
            addCriterion("stu_no in", values, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoNotIn(List<String> values) {
            addCriterion("stu_no not in", values, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoBetween(String value1, String value2) {
            addCriterion("stu_no between", value1, value2, "stuNo");
            return (Criteria) this;
        }

        public Criteria andStuNoNotBetween(String value1, String value2) {
            addCriterion("stu_no not between", value1, value2, "stuNo");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1IsNull() {
            addCriterion("organization_id1 is null");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1IsNotNull() {
            addCriterion("organization_id1 is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1EqualTo(Integer value) {
            addCriterion("organization_id1 =", value, "organizationId1");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1NotEqualTo(Integer value) {
            addCriterion("organization_id1 <>", value, "organizationId1");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1GreaterThan(Integer value) {
            addCriterion("organization_id1 >", value, "organizationId1");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1GreaterThanOrEqualTo(Integer value) {
            addCriterion("organization_id1 >=", value, "organizationId1");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1LessThan(Integer value) {
            addCriterion("organization_id1 <", value, "organizationId1");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1LessThanOrEqualTo(Integer value) {
            addCriterion("organization_id1 <=", value, "organizationId1");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1In(List<Integer> values) {
            addCriterion("organization_id1 in", values, "organizationId1");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1NotIn(List<Integer> values) {
            addCriterion("organization_id1 not in", values, "organizationId1");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1Between(Integer value1, Integer value2) {
            addCriterion("organization_id1 between", value1, value2, "organizationId1");
            return (Criteria) this;
        }

        public Criteria andOrganizationId1NotBetween(Integer value1, Integer value2) {
            addCriterion("organization_id1 not between", value1, value2, "organizationId1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1IsNull() {
            addCriterion("organization_name1 is null");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1IsNotNull() {
            addCriterion("organization_name1 is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1EqualTo(String value) {
            addCriterion("organization_name1 =", value, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1NotEqualTo(String value) {
            addCriterion("organization_name1 <>", value, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1GreaterThan(String value) {
            addCriterion("organization_name1 >", value, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1GreaterThanOrEqualTo(String value) {
            addCriterion("organization_name1 >=", value, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1LessThan(String value) {
            addCriterion("organization_name1 <", value, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1LessThanOrEqualTo(String value) {
            addCriterion("organization_name1 <=", value, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1Like(String value) {
            addCriterion("organization_name1 like", value, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1NotLike(String value) {
            addCriterion("organization_name1 not like", value, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1In(List<String> values) {
            addCriterion("organization_name1 in", values, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1NotIn(List<String> values) {
            addCriterion("organization_name1 not in", values, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1Between(String value1, String value2) {
            addCriterion("organization_name1 between", value1, value2, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationName1NotBetween(String value1, String value2) {
            addCriterion("organization_name1 not between", value1, value2, "organizationName1");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2IsNull() {
            addCriterion("organization_id2 is null");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2IsNotNull() {
            addCriterion("organization_id2 is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2EqualTo(Integer value) {
            addCriterion("organization_id2 =", value, "organizationId2");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2NotEqualTo(Integer value) {
            addCriterion("organization_id2 <>", value, "organizationId2");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2GreaterThan(Integer value) {
            addCriterion("organization_id2 >", value, "organizationId2");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2GreaterThanOrEqualTo(Integer value) {
            addCriterion("organization_id2 >=", value, "organizationId2");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2LessThan(Integer value) {
            addCriterion("organization_id2 <", value, "organizationId2");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2LessThanOrEqualTo(Integer value) {
            addCriterion("organization_id2 <=", value, "organizationId2");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2In(List<Integer> values) {
            addCriterion("organization_id2 in", values, "organizationId2");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2NotIn(List<Integer> values) {
            addCriterion("organization_id2 not in", values, "organizationId2");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2Between(Integer value1, Integer value2) {
            addCriterion("organization_id2 between", value1, value2, "organizationId2");
            return (Criteria) this;
        }

        public Criteria andOrganizationId2NotBetween(Integer value1, Integer value2) {
            addCriterion("organization_id2 not between", value1, value2, "organizationId2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2IsNull() {
            addCriterion("organization_name2 is null");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2IsNotNull() {
            addCriterion("organization_name2 is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2EqualTo(String value) {
            addCriterion("organization_name2 =", value, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2NotEqualTo(String value) {
            addCriterion("organization_name2 <>", value, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2GreaterThan(String value) {
            addCriterion("organization_name2 >", value, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2GreaterThanOrEqualTo(String value) {
            addCriterion("organization_name2 >=", value, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2LessThan(String value) {
            addCriterion("organization_name2 <", value, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2LessThanOrEqualTo(String value) {
            addCriterion("organization_name2 <=", value, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2Like(String value) {
            addCriterion("organization_name2 like", value, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2NotLike(String value) {
            addCriterion("organization_name2 not like", value, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2In(List<String> values) {
            addCriterion("organization_name2 in", values, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2NotIn(List<String> values) {
            addCriterion("organization_name2 not in", values, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2Between(String value1, String value2) {
            addCriterion("organization_name2 between", value1, value2, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrganizationName2NotBetween(String value1, String value2) {
            addCriterion("organization_name2 not between", value1, value2, "organizationName2");
            return (Criteria) this;
        }

        public Criteria andOrString(String valstr){
            addCriterion(valstr);
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion implements Serializable {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}