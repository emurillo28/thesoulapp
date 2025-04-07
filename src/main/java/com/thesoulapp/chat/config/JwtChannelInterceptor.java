package com.thesoulapp.chat.config;

import com.thesoulapp.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                
                try {
                    String userEmail = jwtService.extractUsername(token);
                    
                    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                        
                        if (jwtService.isTokenValid(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authToken = 
                                new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                                );
                            
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            accessor.setUser(authToken);
                        }
                    }
                } catch (Exception e) {
                    // Log but still allow connection for now (will be unauthorized)
                    System.err.println("WebSocket JWT validation error: " + e.getMessage());
                }
            }
        }
        
        return message;
    }
} 