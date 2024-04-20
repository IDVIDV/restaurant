package servicelayer.services;

import org.example.restaurant.datalayer.entities.Table;
import org.example.restaurant.datalayer.mappers.TableMapper;
import org.example.restaurant.datalayer.repositories.TableRepository;
import org.example.restaurant.servicelayer.services.TableService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    TableMapper tableMapper;
    @Mock
    TableRepository tableRepository;
    @InjectMocks
    TableService tableService;
}
