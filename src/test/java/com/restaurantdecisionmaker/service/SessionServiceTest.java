package com.restaurantdecisionmaker.service;

import com.restaurantdecisionmaker.exception.SessionNotFoundException;
import com.restaurantdecisionmaker.model.Restaurant;
import com.restaurantdecisionmaker.model.Session;
import com.restaurantdecisionmaker.repository.RestaurantRepository;
import com.restaurantdecisionmaker.repository.SessionRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private RestaurantRepository restaurantRepository;

    @MockBean
    private WebSocketService webSocketService;

    @BeforeEach
    public void setUp() {
        sessionRepository.deleteAllInBatch();
    }

    @Test
    public void testGetAllSessions_EmptyList() {
        when(sessionRepository.findAll()).thenReturn(Collections.emptyList());

        List<Session> sessions = sessionService.getAllSessions();

        verify(sessionRepository, times(1)).findAll();
        assertEquals(Collections.emptyList(), sessions);
    }

    @Test
    public void testGetAllSessions_PopulatedList() {
        Session mockSession = new Session();
        mockSession.setId(1L);
        Session mockSession1 = new Session();
        mockSession.setId(2L);
        List<Session> mockSessions = Arrays.asList(mockSession, mockSession1);
        when(sessionRepository.findAll()).thenReturn(mockSessions);

        List<Session> sessions = sessionService.getAllSessions();

        verify(sessionRepository, times(1)).findAll();
        assertEquals(mockSessions, sessions);
    }

    @Test
    public void testSaveSession_NewSession() {
        Session newSession = new Session();

        when(sessionRepository.save(newSession)).thenReturn(newSession);

        Session savedSession = sessionService.saveSession(newSession);

        verify(sessionRepository, times(1)).save(newSession);
        assertEquals(newSession, savedSession);
    }

    @Test
    public void testGetSessionById_ExistingSession() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        Session retrievedSession = sessionService.getSessionById(sessionId);

        verify(sessionRepository, times(1)).findById(sessionId);
        assertEquals(mockSession, retrievedSession);
    }

    @Test(expected = SessionNotFoundException.class)
    public void testGetSessionById_NonExistentSession() {
        Long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        sessionService.getSessionById(sessionId);

        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    public void testJoinSession_ExistingAndNotEndedSession() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setEnded(false);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        Session joinedSession = sessionService.joinSession(sessionId);

        verify(sessionRepository, times(1)).findById(sessionId);
        assertEquals(mockSession, joinedSession);
    }

    @Test(expected = SessionNotFoundException.class)
    public void testJoinSession_NonExistentSession() {
        Long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        sessionService.joinSession(sessionId);

        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test(expected = SessionNotFoundException.class)
    public void testJoinSession_ExistingButEndedSession() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setEnded(true);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        sessionService.joinSession(sessionId);

        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    public void testEndSession_ExistingSession() {
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setEnded(false);
        Restaurant restaurant = new Restaurant();
        restaurant.setSessionId(sessionId);
        restaurant.setName("KFC");
        List<Restaurant> mockRestaurants = List.of(restaurant);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        when(restaurantRepository.findBySessionId(sessionId)).thenReturn(mockRestaurants);
        doNothing().when(webSocketService).sendSelectedRestaurantUpdate(any());

        Restaurant selectedRestaurant = sessionService.endSession(sessionId);

        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, times(1)).save(mockSession);
        verify(restaurantRepository, times(1)).findBySessionId(sessionId);
        verify(webSocketService, times(1)).sendSelectedRestaurantUpdate(any());

        assertTrue(mockSession.isEnded());
    }

}