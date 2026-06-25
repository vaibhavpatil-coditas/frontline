package com.coditas.frontline.security.jwt;

import com.coditas.frontline.constants.ExceptionMessage;
import com.coditas.frontline.entity.Ticket;
import com.coditas.frontline.entity.User;
import com.coditas.frontline.exception.NotFoundException;
import com.coditas.frontline.repository.TicketRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final TicketRepository ticketRepository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpRequest servletRequest = (HttpRequest) request;
        String authHeader = servletRequest.getHeaders().get("Authorization").getFirst();
        String requestURI = servletRequest.getURI().toString();
        log.info("Intercepted URI: {}",requestURI);
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            log.info("Failed due to invalid Authorization header");
            return false;
        }
        String[] split = requestURI.split("/");
        Long ticketId;
        if(split.length<5){
            return false;
        }
        ticketId = Long.parseLong(split[6]);
        String token = authHeader.split(" ")[1];
        String username = jwtUtil.extractUsername(token);
        User user = (User) userDetailsService.loadUserByUsername(username);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException(ExceptionMessage.TICKET_NOT_FOUND));
        return ticket.getCustomer().getId().equals(user.getId()) || ticket.getAgent().getId().equals(user.getId());
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, @Nullable Exception exception) {}
}
