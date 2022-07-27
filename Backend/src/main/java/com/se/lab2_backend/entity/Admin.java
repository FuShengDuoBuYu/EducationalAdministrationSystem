/*
* @author : 郭仲天
* @time : 2022年3月2日23:05:32
* */

package com.se.lab2_backend.entity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Admin {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "uuid", length = 36, nullable = false)
    private String uuid;

    //工号
    @Column(name = "job_num", length = 8,nullable = false)
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

    //选课开关
    @Column(name = "course_selection", nullable = false)
    private boolean courseSelection;

    //选课轮次
    @Column(name = "course_selection_round", nullable = false)
    private String courseSelectionRound;

    //当前学年
    @Column(name = "current_year", nullable = false)
    private String currentYear;

    //当前学期
    @Column(name = "current_term", nullable = false)
    private String currentTerm;
}
