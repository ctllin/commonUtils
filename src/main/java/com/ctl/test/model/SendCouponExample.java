package com.ctl.test.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendCouponExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SendCouponExample() {
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

    protected abstract static class GeneratedCriteria {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCouponTypeIsNull() {
            addCriterion("coupon_type is null");
            return (Criteria) this;
        }

        public Criteria andCouponTypeIsNotNull() {
            addCriterion("coupon_type is not null");
            return (Criteria) this;
        }

        public Criteria andCouponTypeEqualTo(Integer value) {
            addCriterion("coupon_type =", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeNotEqualTo(Integer value) {
            addCriterion("coupon_type <>", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeGreaterThan(Integer value) {
            addCriterion("coupon_type >", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("coupon_type >=", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeLessThan(Integer value) {
            addCriterion("coupon_type <", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeLessThanOrEqualTo(Integer value) {
            addCriterion("coupon_type <=", value, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeIn(List<Integer> values) {
            addCriterion("coupon_type in", values, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeNotIn(List<Integer> values) {
            addCriterion("coupon_type not in", values, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeBetween(Integer value1, Integer value2) {
            addCriterion("coupon_type between", value1, value2, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("coupon_type not between", value1, value2, "couponType");
            return (Criteria) this;
        }

        public Criteria andCouponIdIsNull() {
            addCriterion("coupon_id is null");
            return (Criteria) this;
        }

        public Criteria andCouponIdIsNotNull() {
            addCriterion("coupon_id is not null");
            return (Criteria) this;
        }

        public Criteria andCouponIdEqualTo(Integer value) {
            addCriterion("coupon_id =", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdNotEqualTo(Integer value) {
            addCriterion("coupon_id <>", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdGreaterThan(Integer value) {
            addCriterion("coupon_id >", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("coupon_id >=", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdLessThan(Integer value) {
            addCriterion("coupon_id <", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdLessThanOrEqualTo(Integer value) {
            addCriterion("coupon_id <=", value, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdIn(List<Integer> values) {
            addCriterion("coupon_id in", values, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdNotIn(List<Integer> values) {
            addCriterion("coupon_id not in", values, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdBetween(Integer value1, Integer value2) {
            addCriterion("coupon_id between", value1, value2, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponIdNotBetween(Integer value1, Integer value2) {
            addCriterion("coupon_id not between", value1, value2, "couponId");
            return (Criteria) this;
        }

        public Criteria andCouponUuidIsNull() {
            addCriterion("coupon_uuid is null");
            return (Criteria) this;
        }

        public Criteria andCouponUuidIsNotNull() {
            addCriterion("coupon_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andCouponUuidEqualTo(String value) {
            addCriterion("coupon_uuid =", value, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponUuidNotEqualTo(String value) {
            addCriterion("coupon_uuid <>", value, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponUuidGreaterThan(String value) {
            addCriterion("coupon_uuid >", value, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponUuidGreaterThanOrEqualTo(String value) {
            addCriterion("coupon_uuid >=", value, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponUuidLessThan(String value) {
            addCriterion("coupon_uuid <", value, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponUuidLessThanOrEqualTo(String value) {
            addCriterion("coupon_uuid <=", value, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponUuidLike(String value) {
            addCriterion("coupon_uuid like", value, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponUuidNotLike(String value) {
            addCriterion("coupon_uuid not like", value, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponUuidIn(List<String> values) {
            addCriterion("coupon_uuid in", values, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponUuidNotIn(List<String> values) {
            addCriterion("coupon_uuid not in", values, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponUuidBetween(String value1, String value2) {
            addCriterion("coupon_uuid between", value1, value2, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponUuidNotBetween(String value1, String value2) {
            addCriterion("coupon_uuid not between", value1, value2, "couponUuid");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberIsNull() {
            addCriterion("coupon_batch_number is null");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberIsNotNull() {
            addCriterion("coupon_batch_number is not null");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberEqualTo(String value) {
            addCriterion("coupon_batch_number =", value, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberNotEqualTo(String value) {
            addCriterion("coupon_batch_number <>", value, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberGreaterThan(String value) {
            addCriterion("coupon_batch_number >", value, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberGreaterThanOrEqualTo(String value) {
            addCriterion("coupon_batch_number >=", value, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberLessThan(String value) {
            addCriterion("coupon_batch_number <", value, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberLessThanOrEqualTo(String value) {
            addCriterion("coupon_batch_number <=", value, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberLike(String value) {
            addCriterion("coupon_batch_number like", value, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberNotLike(String value) {
            addCriterion("coupon_batch_number not like", value, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberIn(List<String> values) {
            addCriterion("coupon_batch_number in", values, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberNotIn(List<String> values) {
            addCriterion("coupon_batch_number not in", values, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberBetween(String value1, String value2) {
            addCriterion("coupon_batch_number between", value1, value2, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponBatchNumberNotBetween(String value1, String value2) {
            addCriterion("coupon_batch_number not between", value1, value2, "couponBatchNumber");
            return (Criteria) this;
        }

        public Criteria andCouponNameIsNull() {
            addCriterion("coupon_name is null");
            return (Criteria) this;
        }

        public Criteria andCouponNameIsNotNull() {
            addCriterion("coupon_name is not null");
            return (Criteria) this;
        }

        public Criteria andCouponNameEqualTo(String value) {
            addCriterion("coupon_name =", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameNotEqualTo(String value) {
            addCriterion("coupon_name <>", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameGreaterThan(String value) {
            addCriterion("coupon_name >", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameGreaterThanOrEqualTo(String value) {
            addCriterion("coupon_name >=", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameLessThan(String value) {
            addCriterion("coupon_name <", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameLessThanOrEqualTo(String value) {
            addCriterion("coupon_name <=", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameLike(String value) {
            addCriterion("coupon_name like", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameNotLike(String value) {
            addCriterion("coupon_name not like", value, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameIn(List<String> values) {
            addCriterion("coupon_name in", values, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameNotIn(List<String> values) {
            addCriterion("coupon_name not in", values, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameBetween(String value1, String value2) {
            addCriterion("coupon_name between", value1, value2, "couponName");
            return (Criteria) this;
        }

        public Criteria andCouponNameNotBetween(String value1, String value2) {
            addCriterion("coupon_name not between", value1, value2, "couponName");
            return (Criteria) this;
        }

        public Criteria andExecTimeIsNull() {
            addCriterion("exec_time is null");
            return (Criteria) this;
        }

        public Criteria andExecTimeIsNotNull() {
            addCriterion("exec_time is not null");
            return (Criteria) this;
        }

        public Criteria andExecTimeEqualTo(Date value) {
            addCriterion("exec_time =", value, "execTime");
            return (Criteria) this;
        }

        public Criteria andExecTimeNotEqualTo(Date value) {
            addCriterion("exec_time <>", value, "execTime");
            return (Criteria) this;
        }

        public Criteria andExecTimeGreaterThan(Date value) {
            addCriterion("exec_time >", value, "execTime");
            return (Criteria) this;
        }

        public Criteria andExecTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("exec_time >=", value, "execTime");
            return (Criteria) this;
        }

        public Criteria andExecTimeLessThan(Date value) {
            addCriterion("exec_time <", value, "execTime");
            return (Criteria) this;
        }

        public Criteria andExecTimeLessThanOrEqualTo(Date value) {
            addCriterion("exec_time <=", value, "execTime");
            return (Criteria) this;
        }

        public Criteria andExecTimeIn(List<Date> values) {
            addCriterion("exec_time in", values, "execTime");
            return (Criteria) this;
        }

        public Criteria andExecTimeNotIn(List<Date> values) {
            addCriterion("exec_time not in", values, "execTime");
            return (Criteria) this;
        }

        public Criteria andExecTimeBetween(Date value1, Date value2) {
            addCriterion("exec_time between", value1, value2, "execTime");
            return (Criteria) this;
        }

        public Criteria andExecTimeNotBetween(Date value1, Date value2) {
            addCriterion("exec_time not between", value1, value2, "execTime");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeIsNull() {
            addCriterion("message_notice is null");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeIsNotNull() {
            addCriterion("message_notice is not null");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeEqualTo(Integer value) {
            addCriterion("message_notice =", value, "messageNotice");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeNotEqualTo(Integer value) {
            addCriterion("message_notice <>", value, "messageNotice");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeGreaterThan(Integer value) {
            addCriterion("message_notice >", value, "messageNotice");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeGreaterThanOrEqualTo(Integer value) {
            addCriterion("message_notice >=", value, "messageNotice");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeLessThan(Integer value) {
            addCriterion("message_notice <", value, "messageNotice");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeLessThanOrEqualTo(Integer value) {
            addCriterion("message_notice <=", value, "messageNotice");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeIn(List<Integer> values) {
            addCriterion("message_notice in", values, "messageNotice");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeNotIn(List<Integer> values) {
            addCriterion("message_notice not in", values, "messageNotice");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeBetween(Integer value1, Integer value2) {
            addCriterion("message_notice between", value1, value2, "messageNotice");
            return (Criteria) this;
        }

        public Criteria andMessageNoticeNotBetween(Integer value1, Integer value2) {
            addCriterion("message_notice not between", value1, value2, "messageNotice");
            return (Criteria) this;
        }

        public Criteria andDelFlagIsNull() {
            addCriterion("del_flag is null");
            return (Criteria) this;
        }

        public Criteria andDelFlagIsNotNull() {
            addCriterion("del_flag is not null");
            return (Criteria) this;
        }

        public Criteria andDelFlagEqualTo(Byte value) {
            addCriterion("del_flag =", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotEqualTo(Byte value) {
            addCriterion("del_flag <>", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThan(Byte value) {
            addCriterion("del_flag >", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThanOrEqualTo(Byte value) {
            addCriterion("del_flag >=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThan(Byte value) {
            addCriterion("del_flag <", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThanOrEqualTo(Byte value) {
            addCriterion("del_flag <=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagIn(List<Byte> values) {
            addCriterion("del_flag in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotIn(List<Byte> values) {
            addCriterion("del_flag not in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagBetween(Byte value1, Byte value2) {
            addCriterion("del_flag between", value1, value2, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotBetween(Byte value1, Byte value2) {
            addCriterion("del_flag not between", value1, value2, "delFlag");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
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