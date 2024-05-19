package br.com.StreamChallenge.service;

import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.repository.VideoRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@ExtendWith(MockitoExtension.class)
public class BaseServiceTest <T> {
    @InjectMocks
    protected T service;
    @Mock
    protected Pageable pageable;
}
