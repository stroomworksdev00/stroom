package stroom.security.impl.db;

import java.util.Objects;

/**
 * TODO: Rename this first opportunity, for now it's to distinguish it from the hibernate based user.
 */
public class UserJooq {

    // Value of a long to represent an undefined id.
    private static final long UNDEFINED_ID = -1;

    /**
     * This is a technical key (the primary key) You should consider your own
     * business key (e.g. string name) if required. You should not reference
     * this key in the application code or in any screen.
     */
    long id = UNDEFINED_ID;

    /**
     * Standard EJB3 optimistic locking
     */
    private byte version = -1;

    private String name;

    private String uuid;

    /**
     * Is this user a user group or a regular user? TODO : At some point split
     * out logon and credential details into another entity.
     */
    private boolean group;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJooq user = (UserJooq) o;
        return id == user.id &&
                version == user.version &&
                group == user.group &&
                Objects.equals(name, user.name) &&
                Objects.equals(uuid, user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, name, uuid, group);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", version=").append(version);
        sb.append(", name='").append(name).append('\'');
        sb.append(", uuid='").append(uuid).append('\'');
        sb.append(", group=").append(group);
        sb.append('}');
        return sb.toString();
    }
}
