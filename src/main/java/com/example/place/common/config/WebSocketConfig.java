package com.example.place.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat") //stomp 연결을 위한 엔드포인트 설정
//                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOriginPatterns("*");// 모든 origin 허용
//                .withSockJS()
    }

    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        // 메세지브로커에서 클라이언트가 구독할 prefix
        registry.setApplicationDestinationPrefixes("/pub");
        // 클라이언트가 서버로 메세지 보낼 때 prefix
    }

}

