/*
 * This file is generated by jOOQ.
 */
package stroom.annotation.impl.db.jooq.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Row12;
import org.jooq.impl.UpdatableRecordImpl;

import stroom.annotation.impl.db.jooq.tables.Annotation;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AnnotationRecord extends UpdatableRecordImpl<AnnotationRecord> implements Record12<Long, Integer, Long, String, Long, String, Long, Long, String, String, String, String> {

    private static final long serialVersionUID = 1261750875;

    /**
     * Setter for <code>stroom.annotation.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>stroom.annotation.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>stroom.annotation.version</code>.
     */
    public void setVersion(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>stroom.annotation.version</code>.
     */
    public Integer getVersion() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>stroom.annotation.create_time_ms</code>.
     */
    public void setCreateTimeMs(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>stroom.annotation.create_time_ms</code>.
     */
    public Long getCreateTimeMs() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>stroom.annotation.create_user</code>.
     */
    public void setCreateUser(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>stroom.annotation.create_user</code>.
     */
    public String getCreateUser() {
        return (String) get(3);
    }

    /**
     * Setter for <code>stroom.annotation.update_time_ms</code>.
     */
    public void setUpdateTimeMs(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>stroom.annotation.update_time_ms</code>.
     */
    public Long getUpdateTimeMs() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>stroom.annotation.update_user</code>.
     */
    public void setUpdateUser(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>stroom.annotation.update_user</code>.
     */
    public String getUpdateUser() {
        return (String) get(5);
    }

    /**
     * Setter for <code>stroom.annotation.meta_id</code>.
     */
    public void setMetaId(Long value) {
        set(6, value);
    }

    /**
     * Getter for <code>stroom.annotation.meta_id</code>.
     */
    public Long getMetaId() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>stroom.annotation.event_id</code>.
     */
    public void setEventId(Long value) {
        set(7, value);
    }

    /**
     * Getter for <code>stroom.annotation.event_id</code>.
     */
    public Long getEventId() {
        return (Long) get(7);
    }

    /**
     * Setter for <code>stroom.annotation.title</code>.
     */
    public void setTitle(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>stroom.annotation.title</code>.
     */
    public String getTitle() {
        return (String) get(8);
    }

    /**
     * Setter for <code>stroom.annotation.subject</code>.
     */
    public void setSubject(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>stroom.annotation.subject</code>.
     */
    public String getSubject() {
        return (String) get(9);
    }

    /**
     * Setter for <code>stroom.annotation.status</code>.
     */
    public void setStatus(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>stroom.annotation.status</code>.
     */
    public String getStatus() {
        return (String) get(10);
    }

    /**
     * Setter for <code>stroom.annotation.assigned_to</code>.
     */
    public void setAssignedTo(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>stroom.annotation.assigned_to</code>.
     */
    public String getAssignedTo() {
        return (String) get(11);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record12 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<Long, Integer, Long, String, Long, String, Long, Long, String, String, String, String> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<Long, Integer, Long, String, Long, String, Long, Long, String, String, String, String> valuesRow() {
        return (Row12) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Annotation.ANNOTATION.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return Annotation.ANNOTATION.VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return Annotation.ANNOTATION.CREATE_TIME_MS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Annotation.ANNOTATION.CREATE_USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return Annotation.ANNOTATION.UPDATE_TIME_MS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Annotation.ANNOTATION.UPDATE_USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field7() {
        return Annotation.ANNOTATION.META_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field8() {
        return Annotation.ANNOTATION.EVENT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return Annotation.ANNOTATION.TITLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return Annotation.ANNOTATION.SUBJECT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return Annotation.ANNOTATION.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field12() {
        return Annotation.ANNOTATION.ASSIGNED_TO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component2() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component3() {
        return getCreateTimeMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getCreateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component5() {
        return getUpdateTimeMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getUpdateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component7() {
        return getMetaId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component8() {
        return getEventId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component10() {
        return getSubject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component11() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component12() {
        return getAssignedTo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value2() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getCreateTimeMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getCreateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getUpdateTimeMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getUpdateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value7() {
        return getMetaId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value8() {
        return getEventId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getSubject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value12() {
        return getAssignedTo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value2(Integer value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value3(Long value) {
        setCreateTimeMs(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value4(String value) {
        setCreateUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value5(Long value) {
        setUpdateTimeMs(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value6(String value) {
        setUpdateUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value7(Long value) {
        setMetaId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value8(Long value) {
        setEventId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value9(String value) {
        setTitle(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value10(String value) {
        setSubject(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value11(String value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord value12(String value) {
        setAssignedTo(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationRecord values(Long value1, Integer value2, Long value3, String value4, Long value5, String value6, Long value7, Long value8, String value9, String value10, String value11, String value12) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AnnotationRecord
     */
    public AnnotationRecord() {
        super(Annotation.ANNOTATION);
    }

    /**
     * Create a detached, initialised AnnotationRecord
     */
    public AnnotationRecord(Long id, Integer version, Long createTimeMs, String createUser, Long updateTimeMs, String updateUser, Long metaId, Long eventId, String title, String subject, String status, String assignedTo) {
        super(Annotation.ANNOTATION);

        set(0, id);
        set(1, version);
        set(2, createTimeMs);
        set(3, createUser);
        set(4, updateTimeMs);
        set(5, updateUser);
        set(6, metaId);
        set(7, eventId);
        set(8, title);
        set(9, subject);
        set(10, status);
        set(11, assignedTo);
    }
}
