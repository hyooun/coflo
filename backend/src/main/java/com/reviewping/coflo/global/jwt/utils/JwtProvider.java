package com.reviewping.coflo.global.jwt.utils;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.reviewping.coflo.domain.user.entity.PrincipalDetail;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.enums.Role;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtProvider {

	public static String secretKey = JwtConstants.key;

	public static String getTokenFromHeader(String header) {
		return header.split(" ")[1];
	}

	public static String generateToken(Map<String, Object> valueMap, int validTime) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(JwtProvider.secretKey.getBytes(StandardCharsets.UTF_8));
			return Jwts.builder()
				.setHeader(Map.of("typ", "JWT"))
				.setClaims(valueMap)
				.setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
				.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(validTime).toInstant()))
				.signWith(key)
				.compact();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static Authentication getAuthentication(String token) {
		Map<String, Object> claims = validateToken(token);

		String oauth2Id = (String)claims.get("oauth2Id");
		String role = (String)claims.get("role");

		Role memberRole = Role.valueOf(role);
		User user = User.builder().oauth2Id(oauth2Id).role(memberRole).build();

		Set<SimpleGrantedAuthority> authorities = Collections.singleton(
			new SimpleGrantedAuthority(memberRole.getValue()));
		PrincipalDetail principalDetail = new PrincipalDetail(user, authorities);
		return new UsernamePasswordAuthenticationToken(principalDetail, "", authorities);
	}

	public static Map<String, Object> validateToken(String token) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(JwtProvider.secretKey.getBytes(StandardCharsets.UTF_8));

			Map<String, Object> claim = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
				.getBody();

			return claim;
		} catch (ExpiredJwtException expiredJwtException) {
			log.error("만료된 토큰입니다.");
			throw new JwtException("만료된 토큰입니다.");
		} catch (Exception e) {
			log.error("존재하지 않는 토큰입니다.");
			throw new JwtException("존재하지 않는 토큰입니다.");
		}
	}

}
