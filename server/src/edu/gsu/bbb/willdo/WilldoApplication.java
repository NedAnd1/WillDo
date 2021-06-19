package edu.gsu.bbb.willdo;

import java.util.Map;
import javax.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
@SpringBootApplication
public class WilldoApplication implements ErrorViewResolver {

	public static void main(String[] args) {
		SpringApplication.run(WilldoApplication.class, args);
	}

	@RequestMapping({ "/group*/**", "/task*/**", "/profile/**", "/(*)" })
    public String forward() {
        return "forward:/index.html"; // redirects to the single-page application without error
    }

	@Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model)
	{
		if ( status == HttpStatus.NOT_FOUND )
		{
			String acceptHeader= request.getHeader("accept");
			if ( acceptHeader == null || ! acceptHeader.toLowerCase().contains("application/json") )
				return new ModelAndView("forward:/index.html", status); // keeps the error as we redirect to the single-page app
		}
		return new ModelAndView(null, model, status); // returns the default error response
	}

    @Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder()
	{
        return new BCryptPasswordEncoder();
    }

}
