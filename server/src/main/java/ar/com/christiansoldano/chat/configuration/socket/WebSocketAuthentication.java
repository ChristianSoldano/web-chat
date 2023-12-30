package ar.com.christiansoldano.chat.configuration.socket;

import ar.com.christiansoldano.chat.configuration.auth.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class WebSocketAuthentication implements ChannelInterceptor {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor.getCommand().equals(StompCommand.CONNECT)) {
            String authorization = accessor.getFirstNativeHeader("Authorization");

            UsernamePasswordAuthenticationToken user = getAuthenticatedUser(authorization);
            accessor.setUser(user);
        }

        return message;
    }

    private UsernamePasswordAuthenticationToken getAuthenticatedUser(String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtUtils.extractUsername(token);
        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean isTokenValid = jwtUtils.isTokenValid(token, userDetails);
            if (isTokenValid) {
                return new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
            }
        }

        return null;
    }
}
