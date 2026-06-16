package es.miw.tfm.linkal.data.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import es.miw.tfm.linkal.data.api.ChatApiService;
import es.miw.tfm.linkal.models.responses.ChatResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ChatRepositoryTest {
    @Mock private ChatApiService mockApiService;
    @Mock private Call<List<ChatResponse>> mockChatsCall;
    @Mock private ResponseBody mockErrorBody;

    private ChatRepository repository;

    @Before
    public void setUp() {
        repository = new ChatRepository(mockApiService);
        doReturn(mockChatsCall).when(mockApiService).findAllByUser(anyString());
    }

    // findAllByUser ----------------------------------------------------------------

    @Test
    public void findAllByUser_callsApiWithToken() {
        repository.findAllByUser("Bearer token");
        verify(mockApiService).findAllByUser("Bearer token");
    }

    @Test
    public void findAllByUser_returnsCallFromApi() {
        Call<List<ChatResponse>> result = repository.findAllByUser("Bearer token");
        assertSame(mockChatsCall, result);
    }

    @Test
    public void findAllByUser_withDifferentToken_passesItToApi() {
        repository.findAllByUser("Bearer other-token");
        verify(mockApiService).findAllByUser("Bearer other-token");
    }

    @Test
    public void findAllByUser_onSuccess_callbackReceivesChats() {
        List<ChatResponse> chats = Arrays.asList(
                buildChat("Nike Spain", "Campaña Verano"),
                buildChat("Adidas", "Colección Otoño"));

        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onResponse(mockChatsCall, Response.success(chats));
            return null;
        }).when(mockChatsCall).enqueue(any());

        final List<ChatResponse>[] captured = new List[1];
        repository.findAllByUser("Bearer token").enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(Call<List<ChatResponse>> call, Response<List<ChatResponse>> response) {
                captured[0] = response.body();
            }
            @Override
            public void onFailure(Call<List<ChatResponse>> call, Throwable t) {}
        });

        assertNotNull(captured[0]);
        assertEquals(2, captured[0].size());
        assertEquals("Nike Spain", captured[0].get(0).getDisplayName());
    }

    @Test
    public void findAllByUser_onSuccess_returnsEmptyList() {
        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onResponse(mockChatsCall, Response.success(Collections.emptyList()));
            return null;
        }).when(mockChatsCall).enqueue(any());

        final List<ChatResponse>[] captured = new List[1];
        repository.findAllByUser("Bearer token").enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(Call<List<ChatResponse>> call, Response<List<ChatResponse>> response) {
                captured[0] = response.body();
            }
            @Override
            public void onFailure(Call<List<ChatResponse>> call, Throwable t) {}
        });

        assertNotNull(captured[0]);
        assertTrue(captured[0].isEmpty());
    }

    @Test
    public void findAllByUser_on401_responseIsNotSuccessful() {
        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onResponse(mockChatsCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockChatsCall).enqueue(any());

        final boolean[] successCalled = {false};
        final int[] errorCode = {0};
        repository.findAllByUser("Bearer expired").enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(Call<List<ChatResponse>> call, Response<List<ChatResponse>> response) {
                successCalled[0] = response.isSuccessful();
                errorCode[0] = response.code();
            }
            @Override
            public void onFailure(Call<List<ChatResponse>> call, Throwable t) {}
        });

        assertFalse(successCalled[0]);
        assertEquals(401, errorCode[0]);
    }

    @Test
    public void findAllByUser_onNetworkFailure_callbackReceivesThrowable() {
        RuntimeException networkError = new RuntimeException("sin conexión");

        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onFailure(mockChatsCall, networkError);
            return null;
        }).when(mockChatsCall).enqueue(any());

        final Throwable[] captured = new Throwable[1];
        repository.findAllByUser("Bearer token").enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(Call<List<ChatResponse>> call, Response<List<ChatResponse>> response) {}
            @Override
            public void onFailure(Call<List<ChatResponse>> call, Throwable t) {
                captured[0] = t;
            }
        });

        assertNotNull(captured[0]);
        assertEquals("sin conexión", captured[0].getMessage());
    }

    // helpers ------------------------------------------------------------------------------------

    private ChatResponse buildChat(String displayName, String campaignTitle) {
        ChatResponse c = new ChatResponse();
        c.setId("chat-uuid");
        c.setDisplayName(displayName);
        c.setCampaignTitle(campaignTitle);
        return c;
    }
}
