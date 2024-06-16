package com.livecard.front.common.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2 처리 서비스
 */
@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    private ClientRegistrationRepository clientRegistrationRepository;

    private OAuth2AuthorizedClientService authorizedClientService;

    final private String REDIRECT_URI = "http://localhost:3000/loginCallback";

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
            sb.append("&redirect_uri=" + "http://localhost:3000/loginCallback?provider=kakao"); // TODO 인가코드 받은 redirect_uri 입력
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
        WebClient webclient = WebClient.builder()
                .baseUrl("https://nid.naver.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        JSONObject response = webclient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2.0/token")
                        .queryParam("client_id", naverClientId)
                        .queryParam("client_secret", naverClientSecret)
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("state", state)
                        .queryParam("code", code)
                        .build())
                .retrieve().bodyToMono(JSONObject.class).block();

        // 네이버에서 온 응답에서 토큰을 추출
        return response.get("access_token").toString();
    }

    /**
     * 구글 AccessToken 얻기
     * @param code
     * @return
     */
    @Override
    public String getGoogleAccessToken(String code) {
        String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();

        params.put("code", code);
        params.put("client_id", googleClientId);
        params.put("client_secret", googleClientSecret);
        params.put("redirect_uri", "http://localhost:3000/loginCallback?provider=google");
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params,String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String jsonResponse = responseEntity.getBody();

            try {
                // ObjectMapper를 사용하여 JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);

                // "access_token" 필드의 값을 추출
                String accessToken = jsonNode.get("access_token").asText();

                return accessToken;
            } catch (Exception e) {
                // 예외 처리
                e.printStackTrace();
            }
        }
        return null;


//        return ClientRegistration.withRegistrationId("google")
//                .clientId(googleClientId)
//                .clientSecret(googleClientSecret)
//                .redirectUri(googleRedirectUri)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.valueOf(googleClientAuthenticationMethod))
//                .authorizationGrantType(AuthorizationGrantType.valueOf(googleAuthorizationGrantType))
//                .scope(googleScope)
//                .authorizationUri(googleAuthorizationUri)
//                .tokenUri(googleTokenUri)
//                .userInfoUri(googleUserInfoUri)
//                .userNameAttributeName("email")
//                .clientName(googleClientName)
//                .build();

    }
}
