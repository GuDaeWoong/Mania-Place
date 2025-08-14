package com.example.place.common.config;

import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.common.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        //메세지의 헤더에 접근가능하게 해주는 객체

        if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
            if (authorizationHeader == null) {
                log.warn("Missing Authorization header in STOMP {}", accessor.getCommand());
                throw new IllegalArgumentException("Authorization header is missing");
            }
            try {
                String token = jwtUtil.subStringToken(authorizationHeader);
                if (!jwtUtil.validateToken(token)) {
                    throw new IllegalArgumentException("Invalid JWT token");
                }
                Claims claims = jwtUtil.extractClaims(token);
                Long userId = Long.valueOf(claims.getSubject());
                String name = claims.get("name", String.class);
                String nickname = claims.get("nickname", String.class);
                String email = claims.get("email", String.class);
                String role = claims.get("userRole", String.class);

                CustomPrincipal principal = new CustomPrincipal(
                        userId,
                        name,
                        nickname,
                        email,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                );
                accessor.setUser(principal);
                return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
            } catch (Exception e) {
                throw new IllegalArgumentException("WebSocket authentication failed: " + e.getMessage());
            }
        }
        return message;
    }
}