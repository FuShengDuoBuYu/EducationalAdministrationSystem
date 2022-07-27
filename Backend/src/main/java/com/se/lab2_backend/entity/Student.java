package com.se.lab2_backend.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Student {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "uuid", length = 36, nullable = false)
    private String uuid;

    //学号
    @Column(name = "job_num", length = 6,nullable = false)
    private String jobNum;

    //姓名
    @Column(name = "username", length = 256 ,nullable = false)
    private String username;

    //身份证号
    @Column(name = "idcard_num", length = 18,nullable = false)
    private String idcardNum;

    //手机号码
    @Column(name = "phone_num", length = 11,nullable = false)
    private String phoneNum;

    //邮箱
    @Column(name = "email", length = 256,nullable = false)
    private String email;

    //密码
    @Column(name = "password", length = 256,nullable = false)
    private String password;

    //专业
    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

//    //专业id
//    @Column(name = "major_id", length = 100,nullable = false)
//    private String majorId;

    //状态
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

//    //状态id
//    @Column(name = "status_id", length = 100,nullable = false)
//    private String statusId;

    public Student(String jobNum, String username, String idcardNum, String phoneNum, String email, String password, Major major, Status status) {
        this.jobNum = jobNum;
        this.username = username;
        this.idcardNum = idcardNum;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.major = major;
        this.status = status;
    }


//    public Student(String jobNum, String username, String idcardNum, String phoneNum, String email, String password, String majorId, String statusId) {
//        this.jobNum = jobNum;
//        this.username = username;
//        this.idcardNum = idcardNum;
//        this.phoneNum = phoneNum;
//        this.email = email;
//        this.password = password;
//        this.majorId = majorId;
//        this.statusId = statusId;
//    }
}
