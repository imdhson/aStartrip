package ce.daegu.ac.kr.aStartrip.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.spec.ECField;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMsg;
        if (exception instanceof BadCredentialsException) {
            errorMsg = "아이디, 비밀번호가 올바르지 않음.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMsg = "내부 시스템 문제로 로그인 요청 처리 불가";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMsg = "존재하지 않는 계정입니다. 회원가입 후 로그인 해주세요."; // 나중에 회원가입 처리해야할 때 필요한 exception
            //redirect to /signup
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMsg = "인증 요청이 거부 되었음.";
        } else {
            errorMsg = "알수 없는 오류";
        }

        errorMsg = URLEncoder.encode(errorMsg, "utf-8");
        response.sendRedirect("/loginError?msg=" + errorMsg);
    }
}
