package br.com.StreamChallenge.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @PreAuthorize("hasAuthority('ADMIN')")
    public void adminAuthorization(){

    }
}
