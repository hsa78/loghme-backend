package ie.Filter;

import ie.logic.JwtUtil;
import ie.repository.DAO.UserDAO;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName="AuthenticationFilter", urlPatterns="/*")
public class AuthenticationFilter implements Filter{
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        String path = ((HttpServletRequest) servletRequest).getRequestURI();
        if (path.equals("/CA7_backend/registration") || path.equals("/CA7_backend/login") || path.equals("/CA7_backend/googleLogin")) {
            chain.doFilter(servletRequest, servletResponse); // Just continue chain.
        } else {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String jwtToken = request.getHeader("Authorization");

            if (jwtToken == null) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setStatus(401);
                return;
            }

            UserDAO user = JwtUtil.getInstance().parseToken(jwtToken);
            if(user == null){
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setStatus(403);
                return;
            }
            if(path.equals("/jwtValidation")){
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setStatus(200);
                return;
            }
            request.setAttribute("email", user.getEmail());
            chain.doFilter(request, servletResponse);
        }
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }
}
