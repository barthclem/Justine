package com.justice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * Created by aanu.oyeyemi on 1/4/17.
 * Project name -> demojustice
 */
@Configuration
@EnableWebSecurity
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    @Autowired
    private ClientDetailsService detailsService;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("clem").password("clem").roles("USER")
                .and()
                .withUser("admin").password("admin123").roles("ADMIN");

        try {
            System.out.println("\n\n\n" + auth.getDefaultUserDetailsService().loadUserByUsername("admin").getUsername() + "\n\n\n");
        }
        catch (UsernameNotFoundException e){
            System.out.println("\n\n\nUserNameNotFOUND Exception : "+e.getMessage());
        }

    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .anonymous().disable()
                .authorizeRequests()
                .antMatchers("/oauth/token")
                .permitAll()

        ;
    }



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }

    @Bean
    @Autowired
    public TokenStoreUserApprovalHandler tokenStoreUser(TokenStore tokenStore){
        TokenStoreUserApprovalHandler handler=new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setClientDetailsService(detailsService);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(detailsService));
        return handler;
    }

    @Bean
    @Autowired
    public ApprovalStore approvalStore(TokenStore tokenStore){
        TokenApprovalStore store=new TokenApprovalStore();
        store.setTokenStore(tokenStore);
        return store;
    }
}
