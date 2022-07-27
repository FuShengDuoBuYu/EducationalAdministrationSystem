package com.se.lab2_backend.Interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.se.lab2_backend.common.VerifyToken;
import com.se.lab2_backend.entity.Admin;
import com.se.lab2_backend.entity.Student;
import com.se.lab2_backend.entity.Teacher;
import com.se.lab2_backend.service.impl.AdminServiceImpl;
import com.se.lab2_backend.service.impl.StudentServiceImpl;
import com.se.lab2_backend.service.impl.TeacherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class AuthInterceptor implements HandlerInterceptor {
    private AdminServiceImpl adminServiceImpl;
    private StudentServiceImpl studentServiceImpl;
    private TeacherServiceImpl teacherServiceImpl;

    @Autowired
    public AuthInterceptor(AdminServiceImpl adminServiceImpl, StudentServiceImpl studentServiceImpl, TeacherServiceImpl teacherServiceImpl) {
        this.adminServiceImpl = adminServiceImpl;
        this.studentServiceImpl = studentServiceImpl;
        this.teacherServiceImpl = teacherServiceImpl;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中取出token
        String token = request.getHeader("token");
        //如果不是映射到Controller的方法直接放行
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查需不需要验证token
        if(method.isAnnotationPresent(VerifyToken.class)){
            VerifyToken loginToken = method.getAnnotation(VerifyToken.class);
//            if(loginToken.required()){
                if(token == null || token == ""){
                    throw new RuntimeException("暂无token信息！请先登录获取token！");
                }
                String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j){
                    throw new RuntimeException("token非法！请重新登录！");
                }
//                if(JWT.decode(token).getExpiresAt().compareTo(new Date())<0){
//                    throw new RuntimeException("token已过期！请重新登录！");
//                }
                String password = findUserPassword(userId);
                if(password.equals("")){
                    throw new RuntimeException("用户不存在！请重新登录！");
                }
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(password)).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException j){
                    throw new RuntimeException("校验token发生异常！");
                }
                String identity = JWT.decode(token).getClaim("identity").asString();
                return identityCheck(identity, loginToken.identity());
//            }
        }
        return true;
    }

    private boolean identityCheck(String identity, String[] requiredIdentity) {
        if(requiredIdentity.length==0 || Arrays.asList(requiredIdentity).contains(identity)) return true;
        else throw new RuntimeException("没有权限！");
    }

    private String findUserPassword(String userId) {
        String res = "";
        Admin admin = adminServiceImpl.getAdminByUuid(userId);
        Student student = studentServiceImpl.getStudentByUuid(userId);
        Teacher teacher = teacherServiceImpl.getTeacherByUuid(userId);
        if(admin != null) res = admin.getPassword();
        else if(student != null) res = student.getPassword();
        else if(teacher != null) res = teacher.getPassword();
        else res = "";
        return res;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
