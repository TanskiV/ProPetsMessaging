package messaging.filters;

import messaging.utils.MessagingConstants;
import okhttp3.*;
import org.apache.catalina.filters.RequestFilter;
import org.apache.juli.logging.Log;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidTokenFilter extends RequestFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String token = req.getHeader("X-Token");

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", mediaType);
        Request requestBoolean = null;
        if (token == null){
            resp.getWriter().write("This request without token");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        }else {
            requestBoolean = new Request.Builder()
                    .url(MessagingConstants.VALIDATE_BOOLEAN_URL)
                    .method("PUT", body)
                    .addHeader("X-Token", token)
                    .build();
            Response responseBoolean = client.newCall(requestBoolean).execute();
            ResponseBody responseBody = responseBoolean.body();
            boolean booleanValue = Boolean.parseBoolean(responseBody.string());
            if (booleanValue) {
                chain.doFilter(request, response);
            } else {
                response.getWriter().write("Token not valid");
                resp.setStatus(HttpServletResponse.SC_CONFLICT);

            }
        }
    }

    @Override
    protected Log getLogger() {

        return null;
    }
}
