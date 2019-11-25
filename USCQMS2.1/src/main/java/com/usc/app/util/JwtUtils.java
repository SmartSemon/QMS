package com.usc.app.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

import com.usc.dto.Dto;
import com.usc.dto.impl.MapDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @description Jwt工具类
 * @author caochengde
 * @date 2019年7月23日
 */
@Component
public class JwtUtils implements Serializable {

	private static final long serialVersionUID = 1944837314756967021L;

	/*
	 * iss (issuer)：签发人
	 * exp (expiration time)：过期时间
	 * sub (subject)：主题
	 * aud (audience)：受众
	 * nbf (Not Before)：生效时间
	 * iat (Issued At)：签发时间
	 * jti (JWT ID)：编号
	 */

	// c 加密私钥
    private static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMJCvbmeb5t7bH1j_V_g5Q56Zk1h1akGMP2des6ckzU0Mw4DXQprwg3Xf7m69XseymQTTxamyfrvjm5uRa5gLlImQpHj9U8TNTrvbzYIj7aFL8qz_xF-fiQK3Cx3vJbOr2gwW5_Da0HzpjLRZk1_g1_sqhuXP0d8Yk4NV9MdN9GlAgMBAAECgYEAoM601XIk-3V5QndCKFs34ftKsmX5g3i_l0IEhgIxDcrPNJtuKpE3S1QHxEzSpv-_FIazYpHhA1vI8PtRrV6rGB6iX13I_LDCZe5kDnig5sR7sRyGYZJl74kA94Nv3OAPv8EyURxddM_1KMRnG3Vd4LNK-yPW0DawE1WSjtYZISUCQQDgm72mRvG7RGkIaXT3b9Ul-qDY634FpAuJzzyt7O8q8exFVe1hXiq6LLE-DZ4iWbJk-KRvuAnOs6MZnJIxc8IDAkEA3WkxJ0xTDsZyThwOT373mH3n4_qBLHAaKSMEq_720RvKT9WiYg7SWgBiltf5ubE64Fs8lKkNChIJS_4WvvJhNwJAeS6t6Ot3-sCTJq23JNUanC4X4FFWIfb7HrRIGdKuy23ROyt8Mr9asC3yxvcT7ZaVcHLSTVBvQi2bfvMB0VqSPwJBAJsktH6W6rQ1ta6p1hU6IIBH6Q9EXvMmcg5VLVbfx07rLC4Ywjn3rMnH701HdQL6_whqpd59PlfKH52SI5oU-K0CQB4DfQFmtQHSbZdDIzECJwS4C5q6cWQHY2SUkB_OwCkuJ4j7AUNRyKq1OnJMPftD1eiM6ZluXuQzbBtKofXNHdc";

    // c token过期时间
    private static final long  ttlMillis = 60000*3;

	/**
	 * @description 生成JWT令牌
	 * @author caochengde
	 * @return
	 */
	public static String buildJwt(Map claims) {
		// c 生成JWT的时间
		long nowMillis = System.currentTimeMillis();
		Date iat = new Date(nowMillis);
        // c header属性
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typ", "JWT");
		map.put("alg", "RS512");
		// c 这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
        		// c 设置header
        		.setHeader(map)
        		// c 设置claims
        		.setClaims(claims)
        		//iat: jwt的签发时间
        		.setIssuedAt(iat)
        		// c 设置签名使用的签名算法和签名使用的秘钥
        		.signWith(SignatureAlgorithm.RS512, RSAUtils.getPrivateKey(privateKey))
        		;
        // c 设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
		return builder.compact();
	}

	/**
	 * @description 解析JWT，获取数据声明
	 * @author caochengde
	 * @param token
	 * @param publicKey
	 * @return
	 */
	public static Claims parserJwt(String token, String publicKey) {
		Claims claims = null;
		try {
			return claims = Jwts.parser()
					.setSigningKey(RSAUtils.getPublicKey(publicKey))
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			return claims;
		}

	}

	/**
	 * @description 判断令牌是否过期，过期返回true，未过期返回false
	 * @author caochengde
	 * @param token
	 * @param publicKey
	 * @return
	 */
	public static Boolean isTokenExpired(String token, String publicKey) {
        try {
            Claims claims = parserJwt(token, publicKey);
            Date expiration = claims.getExpiration();
            // c 过期时间在当前时间之前，则未过期，返回false
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

	/**
	 * @description 刷新JWT令牌
	 * @author caochengde
	 * @param token
	 * @param publicKey
	 * @return
	 */
	public static String refreshToken(String token, String publicKey) {
        String refreshedToken;
        try {
            Claims claims = parserJwt(token, publicKey);
            Dto dto = new MapDto(claims);
            refreshedToken = buildJwt(dto);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

	/**
	 * @description 验证令牌是否有效，有效返回用户名，无效返回null
	 * @author caochengde
	 * @param token
	 * @param userName
	 * @param publicKey
	 * @return
	 */
	public static String verifyToken(String token, String publicKey) {
		try {
			Claims claims= parserJwt(token, publicKey);
	        String userName_token = claims.get("userName").toString();
	        return userName_token;
		} catch (Exception e) {
			return null;
		}


    }

}
