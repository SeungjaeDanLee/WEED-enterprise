package com.example.weed.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.net.URLDecoder;
import java.util.List;

@Entity
@Getter
@Setter
public class Dept {

    @Id
    @GeneratedValue
    private Long id;

    private String deptName;

    @OneToMany(mappedBy = "dept")
    private List<Member> members;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Dept() {
        // Default constructor
    }


    // Constructor with id parameter
    public Dept(Long id) {
        this.id = id;
    }

    // Constructor with deptName parameter
    public Dept(String deptName) {
        this.deptName = deptName;
    }
}

