package es.miw.tfm.linkal.viewModel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import es.miw.tfm.linkal.data.repositories.ChatRepository;
import es.miw.tfm.linkal.models.responses.ChatResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChatViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock private ChatRepository            mockRepository;
    @Mock private Call<List<ChatResponse>>  mockChatsCall;
    private ChatViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new ChatViewModel(mockRepository);
        doReturn(mockChatsCall).when(mockRepository).findAllByUser(anyString());
    }

    // Estado inicial ------------------------------------------------------------

    @Test
    public void chats_initialValue_isNull() {
        assertNull(viewModel.getChats().getValue());
    }

    @Test
    public void error_initialValue_isNull() {
        assertNull(viewModel.getError().getValue());
    }

    // Getters LiveData -----------------------------------------------------------

    @Test
    public void getChats_returnsLiveData() {
        assertNotNull(viewModel.getChats());
    }

    @Test
    public void getError_returnsLiveData() {
        assertNotNull(viewModel.getError());
    }

    // ─── loadChats — delegación ───────────────────────────────────────────────

    @Test
    public void loadChats_delegatesToRepository() {
        viewModel.loadChats("Bearer token");
        verify(mockRepository).findAllByUser("Bearer token");
    }

    @Test
    public void loadChats_withDifferentToken_passesItToRepository() {
        viewModel.loadChats("Bearer other-token");
        verify(mockRepository).findAllByUser("Bearer other-token");
    }

    // loadChats — éxito ---------------------------------------------------

    @Test
    public void loadChats_onSuccess_updatesChatsLiveData() {
        List<ChatResponse> chats = Arrays.asList(
                buildChat("Nike Spain", "Campaña Verano"),
                buildChat("Adidas", "Colección Otoño"));

        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onResponse(mockChatsCall, Response.success(chats));
            return null;
        }).when(mockChatsCall).enqueue(any());

        viewModel.loadChats("Bearer token");

        assertNotNull(viewModel.getChats().getValue());
        assertEquals(2, viewModel.getChats().getValue().size());
    }

    @Test
    public void loadChats_onSuccess_chatsContainDisplayNameAndCampaignTitle() {
        List<ChatResponse> chats = Collections.singletonList(
                buildChat("Nike Spain", "Campaña Verano 2025"));

        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onResponse(mockChatsCall, Response.success(chats));
            return null;
        }).when(mockChatsCall).enqueue(any());

        viewModel.loadChats("Bearer token");

        ChatResponse first = viewModel.getChats().getValue().get(0);
        assertEquals("Nike Spain", first.getDisplayName());
        assertEquals("Campaña Verano 2025", first.getCampaignTitle());
    }

    @Test
    public void loadChats_onSuccessWithEmptyList_updatesChatsLiveData() {
        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onResponse(mockChatsCall, Response.success(Collections.emptyList()));
            return null;
        }).when(mockChatsCall).enqueue(any());

        viewModel.loadChats("Bearer token");

        assertNotNull(viewModel.getChats().getValue());
        assertTrue(viewModel.getChats().getValue().isEmpty());
    }

    @Test
    public void loadChats_onSuccess_doesNotSetError() {
        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onResponse(mockChatsCall, Response.success(Collections.emptyList()));
            return null;
        }).when(mockChatsCall).enqueue(any());

        viewModel.loadChats("Bearer token");

        assertNull(viewModel.getError().getValue());
    }

    // loadChats — error HTTP ----------------------------------------------------

    @Test
    public void loadChats_on401_setsErrorLiveData() {
        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onResponse(mockChatsCall,
                            Response.error(401, okhttp3.ResponseBody.create(null, "")));
            return null;
        }).when(mockChatsCall).enqueue(any());

        viewModel.loadChats("Bearer expired");

        assertNotNull(viewModel.getError().getValue());
        assertTrue(viewModel.getError().getValue().contains("401"));
    }

    @Test
    public void loadChats_on401_doesNotUpdateChats() {
        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onResponse(mockChatsCall,
                            Response.error(401, okhttp3.ResponseBody.create(null, "")));
            return null;
        }).when(mockChatsCall).enqueue(any());

        viewModel.loadChats("Bearer expired");

        assertNull(viewModel.getChats().getValue());
    }

    // loadChats — fallo de red ----------------------------------------------------------

    @Test
    public void loadChats_onNetworkFailure_setsErrorLiveData() {
        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onFailure(mockChatsCall, new RuntimeException("sin conexión"));
            return null;
        }).when(mockChatsCall).enqueue(any());

        viewModel.loadChats("Bearer token");

        assertNotNull(viewModel.getError().getValue());
        assertTrue(viewModel.getError().getValue().contains("sin conexión"));
    }

    @Test
    public void loadChats_onNetworkFailure_doesNotUpdateChats() {
        doAnswer(inv -> {
            ((Callback<List<ChatResponse>>) inv.getArgument(0))
                    .onFailure(mockChatsCall, new RuntimeException("timeout"));
            return null;
        }).when(mockChatsCall).enqueue(any());

        viewModel.loadChats("Bearer token");

        assertNull(viewModel.getChats().getValue());
    }

    // helpers -------------------------------------------------------------------

    private ChatResponse buildChat(String displayName, String campaignTitle) {
        ChatResponse c = new ChatResponse();
        c.setId("chat-uuid");
        c.setDisplayName(displayName);
        c.setCampaignTitle(campaignTitle);
        return c;
    }
}
