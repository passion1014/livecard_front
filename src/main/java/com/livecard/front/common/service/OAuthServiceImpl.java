package com.livecard.front.common.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2 처리 서비스
 */
public class OAuthServiceImpl implements OAuthService {
    @Value("${security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    final private String REDIRECT_URI = "/";

    /**
     * 카카오 AccessToken 얻기
     * @param code
     * @return
     */
    @Override
    public String getKakaoAccessToken(String code) {
        String access_Token="";
        String refresh_Token ="";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(kakaoClientId); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=" + REDIRECT_URI); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=").append(code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            /*int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);*/
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            //System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            //System.out.println("access_token : " + access_Token);
            //System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    /**
     * 네이버 AccessToken 얻기
     * @param code
     * @return
     */
    @Override
    public String getNaverAccessToken(String code, String state) {
//        WebClient webclient = WebClient.builder()
//                .baseUrl("https://nid.naver.com")
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//
//        JSONObject response = webclient.post()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/oauth2.0/token")
//                        .queryParam("client_id", ClientId)
//                        .queryParam("client_secret", ClientSecret)
//                        .queryParam("grant_type", "authorization_code")
//                        .queryParam("state", state)
//                        .queryParam("code", code)
//                        .build())
//                .retrieve().bodyToMono(JSONObject.class).block();
//
//        // 네이버에서 온 응답에서 토큰을 추출
//        return response.get("access_token").toString();
        return "";
    }

    /**
     * 구글 AccessToken 얻기
     * @param code
     * @return
     */
    @Override
    public String getGoogleAccessToken(String code) {
//        return ClientRegistration.withRegistrationId("google")
//                .clientId(googleClientId)
//                .clientSecret(googleClientSecret)
//                .redirectUriTemplate(googleRedirectUri)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.valueOf(googleClientAuthenticationMethod))
//                .authorizationGrantType(AuthorizationGrantType.valueOf(googleAuthorizationGrantType))
//                .scope(googleScope)
//                .authorizationUri(googleAuthorizationUri)
//                .tokenUri(googleTokenUri)
//                .userInfoUri(googleUserInfoUri)
//                .userNameAttributeName("email")
//                .clientName(googleClientName)
//                .build();

        return "";

    }
}
