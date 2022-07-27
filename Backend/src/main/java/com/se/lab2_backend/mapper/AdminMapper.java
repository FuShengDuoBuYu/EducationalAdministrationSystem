/*
 * @author : 郭仲天
 * @time : 2022年3月2日23:16:11
 */
package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper extends JpaRepository<Admin,Long> {

    Admin findAdminByJobNum(String jobNum);

    Admin findAdminByUuid(String uuid);

    //List<Admin> findAdminsByEmail(String email);
}
