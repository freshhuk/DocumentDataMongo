package com.document.documentdatamongo.Services;

import com.document.documentdatamongo.Repositories.DataRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DataServiceTest {

    @InjectMocks
    private DataService service;

    @Mock
    private DataRepository repository;




}
