package com.livecard.front.common.security.oauth;

import com.livecard.front.common.exception.SearchNotFoundException;
import com.livecard.front.common.security.CustomUserDetails;
import com.livecard.front.domain.entity.MbrUserEntity;
import com.livecard.front.domain.repository.MbrUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    @Value("${jwt.secret-key}")
    String secretKey;
    @Value("${jwt.issuer}")
    String issuer;

    private final MbrUserRepository mbrUserRepository;

    public String generateToken(MbrUserEntity user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, MbrUserEntity user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getSocialId()) //TODO:확인
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        //Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
//        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities)
//                , token
//                , authorities);


        CustomUserDetails customUserDetails = new CustomUserDetails(claims.getSubject(), authorities.stream().toList());

        //TODO: throw하기
        MbrUserEntity user = mbrUserRepository.findBySocialId(claims.getSubject()).orElseThrow();
        customUserDetails.setMemberName(user.getName());
        customUserDetails.setId(user.getId());
        customUserDetails.setProviderCd(user.getProviderCd());
        customUserDetails.setProfileImg(user.getProfileImg());

        return new UsernamePasswordAuthenticationToken(customUserDetails, token, authorities);
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}