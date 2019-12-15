package net.codelet.cloud.config;

import net.codelet.cloud.filter.HttpAccessControlFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Web 安全配置。
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 取得跨域请求头设置滤器实例。
     * @return 跨域请求头设置滤器实例
     */
    @Bean
    public HttpAccessControlFilter httpAccessControlFilter() {
        return new HttpAccessControlFilter();
    }

    /**
     * 配置 HTTP 安全。
     * @param httpSecurity HTTP 安全设置
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeRequests()
            .anyRequest()
            .permitAll()
            .and()
            .addFilterBefore(httpAccessControlFilter(), UsernamePasswordAuthenticationFilter.class)
            .csrf()
            .disable();
    }

    /**
     * 配置 Web 安全。
     * @param webSecurity Web 安全设置
     */
    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/**");
    }
}
