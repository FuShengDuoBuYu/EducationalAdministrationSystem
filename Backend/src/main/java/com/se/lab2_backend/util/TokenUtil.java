package com.se.lab2_backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;


public class TokenUtil {
    /**
     * 过期时间（单位：秒）
     * 这边的长度为3天
     **/
    private static final long EXPIRATION = 3*24*3600*1000L;
//    private static final long EXPIRATION = 300*1000L;

    public static String getToken(String id, String identity, String jobNum, String username, String sec){
        return JWT.create().withAudience(id)
                .withClaim("identity",identity)
                .withClaim("jobNum", jobNum)
                .withClaim("username", username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(sec));
    }
    //算法：HS256
    //类型：jwt
    //withAudience：向有效负载添加特定的受众（“aud”）声明，我们可以在这里放入一些用户的信息，例如：用户 id
    //withClaim：添加自定义索赔值，我们使用用户的账户和密码进行一起加密生成 jwt
    //withExpiresAt：超时时间设置，超时 token 将失效
    //withIssuedAt：签发时间，一般设置为当前时间
    //sign：签名，我们可以自定义签名和算法
}
