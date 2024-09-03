package com.zerobase.user.config.filter;

import com.zerobase.user.service.CustomerService;
import com.zerobase.zerobasedomain.config.JwtAuthenticationProvider;
import com.zerobase.zerobasedomain.domain.common.UserVo;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@WebFilter(urlPatterns = "/customer/*")
@RequiredArgsConstructor
public class CustomerFilter implements Filter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomerService customerService;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("X-AUTH-TOKEN");
        if (!jwtAuthenticationProvider.validateToken(token)){
            throw new ServletException("Invalid Access");
        }
        UserVo vo = jwtAuthenticationProvider.getUserVo(token);
        customerService.findByIdAndEmail(vo.getId(), vo.getEmail()).orElseThrow(
                ()-> new ServletException("Invalid access")
        );
        chain.doFilter(request, response);
    }
}
