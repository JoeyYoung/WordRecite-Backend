package tss.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "user", indexes = {
        @Index(name = "user_name_index", columnList = "user_name")
})
public class UserEntity {
    public static final int TYPE_MANAGER = 0;
    public static final int TYPE_TEACHER = 1;
    public static final int TYPE_TA = 2;
    public static final int TYPE_STUDENT = 3;
    public static final int TYPE_NUM = 4;


    private String uid;
    private String name;
    private String password;
    private Integer type;
    private String email;
    private String telephone;
    private String intro;

    private Set<RoleEntity> roles = new HashSet<>();


    @Column(name = "user_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "user_id", length = 10)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Column(name = "user_pwd", length = 44)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(length = 16)
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Column(length = 31)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public boolean haveAuthority(String uri) {
        for(RoleEntity role : roles) {
            for(AuthorityEntity authority : role.getAuthorities()) {
                if(authority.getUri().equals(uri)) {
                    return true;
                }
            }
        }
        return false;
    }

}
