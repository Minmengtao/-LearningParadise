package com.mmt.user.security;

import com.mmt.user.security.component.CustomAuthenticationProvider;
import com.mmt.user.security.handler.CustomAuthenticationFailureHandler;
import com.mmt.user.security.handler.CustomAuthenticationSuccessHandler;
import com.mmt.user.security.handler.CustomExpiredSessionStrategy;
import com.mmt.user.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * 用于处理页面重定向问题
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 如果token表不存在，使用下面语句可以初始化该表；若存在，请注释掉这条语句，否则会报错。
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 如果有允许匿名的url，填在下面
//                .antMatchers().permitAll()
                //.antMatchers("/register").permitAll()
                .antMatchers("/login/invalid", "/register_user", "/register").permitAll()
                .antMatchers("/getVerifyCode").permitAll()
                .anyRequest().authenticated()
                // 设置登陆页
                .and().formLogin().loginPage("/login")
                // 设置登陆成功页
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler).permitAll()
                //.defaultSuccessUrl("/").permitAll()
                //.failureUrl("/login/error")
                // 自定义登陆用户名和密码参数，默认为username和password
//                .usernameParameter("username")
//                .passwordParameter("password")
                .authenticationDetailsSource(authenticationDetailsSource)
                .and().logout().permitAll()

                //自动登录
                .and().rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    // 有效时间：单位s
                    .tokenValiditySeconds(7200)
                    .userDetailsService(userDetailsService)
                .and().sessionManagement()
                    .invalidSessionUrl("/login/invalid")
                    //最大登录数
                    .maximumSessions(1)
                    // 当达到最大值时，是否保留已经登录的用户
                    .maxSessionsPreventsLogin(false)
                    // 当达到最大值时，旧用户被踢出后的操作
                    .expiredSessionStrategy(new CustomExpiredSessionStrategy())
                    .sessionRegistry(sessionRegistry());;


        // 关闭CSRF跨域
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

    /**
     * 主动踢下用户
     * @return
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
