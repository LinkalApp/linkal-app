package es.miw.tfm.linkal.data.repositories;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import es.miw.tfm.linkal.data.api.ChatApiService;
import es.miw.tfm.linkal.models.requests.SendMessageRequest;
import es.miw.tfm.linkal.models.responses.ChatResponse;
import es.miw.tfm.linkal.models.responses.MessageResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import androidx.lifecycle.MutableLiveData;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ChatRepositoryTest {
    @Mock private ChatApiService                   mockApiService;
    @Mock private Call<List<ChatResponse>> mockChatsCall;
    @Mock private Call<MessageResponse> mockSendCall;
    @Mock private MutableLiveData<List<ChatResponse>> chatsLiveData;
    @Mock private MutableLiveData<Boolean> messageSentLiveData;
    @Mock private MutableLiveData<String> errorLiveData;
    @Mock private MutableLiveData<Boolean> loadingLiveData;
    @Mock private ResponseBody mockErrorBody;

    private ChatRepository repository;

    @Before
    public void setUp() {
        repository = new ChatRepository(mockApiService);
        doReturn(mockChatsCall).when(mockApiService).findAllByUser(anyString());
        doReturn(mockSendCall).when(mockApiService).sendMessage(anyString(), anyString(), any());
    }


    @Test
    public void getInstance_returnsSameInstance() {
        assertSame(ChatRepository.getInstance(), ChatRepository.getInstance());
    }

    // findAllByUser ----------------------------------------------------------------

    @Test
    public void findAllByUser_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockChatsCall).enqueue(any());

        repository.findAllByUser("Bearer token", chatsLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void findAllByUser_callsApiWithToken() {
        doAnswer(inv -> null).when(mockChatsCall).enqueue(any());

        repository.findAllByUser("Bearer token", chatsLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).findAllByUser("Bearer token");
    }

    @Test
    public void findAllByUser_withDifferentToken_passesItToApi() {
        doAnswer(inv -> null).when(mockChatsCall).enqueue(any());

        repository.findAllByUser("Bearer other-token", chatsLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).findAllByUser("Bearer other-token");
    }

    @Test
    public void findAllByUser_onSuccess_postsChatsToResult() {
        List<ChatResponse> chats = Arrays.asList(
                buildChat("Nike Spain", "Campana Verano"),
                buildChat("Adidas", "Coleccion Otono"));
        doAnswer(invocation -> {
            ((Callback<List<ChatResponse>>) invocation.getArgument(0))
                    .onResponse(mockChatsCall, Response.success(chats));
            return null;
        }).when(mockChatsCall).enqueue(any());

        repository.findAllByUser("Bearer token", chatsLiveData, errorLiveData, loadingLiveData);

        verify(chatsLiveData).postValue(chats);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void findAllByUser_onSuccessWithEmptyList_postsEmptyList() {
        doAnswer(invocation -> {
            ((Callback<List<ChatResponse>>) invocation.getArgument(0))
                    .onResponse(mockChatsCall, Response.success(Collections.emptyList()));
            return null;
        }).when(mockChatsCall).enqueue(any());

        repository.findAllByUser("Bearer token", chatsLiveData, errorLiveData, loadingLiveData);

        verify(chatsLiveData).postValue(Collections.emptyList());
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void findAllByUser_on401_postsErrorMessage() {
        doAnswer(invocation -> {
            ((Callback<List<ChatResponse>>) invocation.getArgument(0))
                    .onResponse(mockChatsCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockChatsCall).enqueue(any());

        repository.findAllByUser("Bearer expired", chatsLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 401", captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
        verify(chatsLiveData, never()).postValue(any());
    }

    @Test
    public void findAllByUser_on403_postsErrorMessage() {
        doAnswer(invocation -> {
            ((Callback<List<ChatResponse>>) invocation.getArgument(0))
                    .onResponse(mockChatsCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockChatsCall).enqueue(any());

        repository.findAllByUser("Bearer token", chatsLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 403", captor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
        verify(chatsLiveData, never()).postValue(any());
    }

    @Test
    public void findAllByUser_on500_postsErrorMessage() {
        doAnswer(invocation -> {
            ((Callback<List<ChatResponse>>) invocation.getArgument(0))
                    .onResponse(mockChatsCall, Response.error(500, mockErrorBody));
            return null;
        }).when(mockChatsCall).enqueue(any());

        repository.findAllByUser("Bearer token", chatsLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 500", captor.getValue().contains("500"));
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void findAllByUser_onNetworkFailure_postsErrorMessage() {
        doAnswer(invocation -> {
            ((Callback<List<ChatResponse>>) invocation.getArgument(0))
                    .onFailure(mockChatsCall, new RuntimeException("sin conexion"));
            return null;
        }).when(mockChatsCall).enqueue(any());

        repository.findAllByUser("Bearer token", chatsLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el texto del error", captor.getValue().contains("sin conexion"));
        verify(loadingLiveData).postValue(false);
        verify(chatsLiveData, never()).postValue(any());
    }

    @Test
    public void findAllByUser_onNullThrowableMessage_postsErrorMessage() {
        doAnswer(invocation -> {
            ((Callback<List<ChatResponse>>) invocation.getArgument(0))
                    .onFailure(mockChatsCall, new RuntimeException((String) null));
            return null;
        }).when(mockChatsCall).enqueue(any());

        repository.findAllByUser("Bearer token", chatsLiveData, errorLiveData, loadingLiveData);

        verify(errorLiveData).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    // sendMessage -----------------------------------------------------------------------

    @Test
    public void sendMessage_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockSendCall).enqueue(any());

        repository.sendMessage("Bearer token", "chat-uuid", "Hola!", messageSentLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void sendMessage_callsApiWithCorrectParams() {
        doAnswer(inv -> null).when(mockSendCall).enqueue(any());

        repository.sendMessage("Bearer token", "chat-uuid", "Hola!", messageSentLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).sendMessage(eq("Bearer token"), eq("chat-uuid"), any());
    }

    @Test
    public void sendMessage_buildsBodyWithText() {
        doAnswer(inv -> null).when(mockSendCall).enqueue(any());

        repository.sendMessage("Bearer token", "chat-uuid", "Hola!", messageSentLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<SendMessageRequest> captor = ArgumentCaptor.forClass(SendMessageRequest.class);
        verify(mockApiService).sendMessage(any(), any(), captor.capture());
        assertEquals("Hola!", captor.getValue().getText());
    }

    @Test
    public void sendMessage_onSuccess_postsMessageSentTrue() {
        doAnswer(invocation -> {
            ((Callback<MessageResponse>) invocation.getArgument(0))
                    .onResponse(mockSendCall, Response.success(buildMessage("Hola!")));
            return null;
        }).when(mockSendCall).enqueue(any());

        repository.sendMessage("Bearer token", "chat-uuid", "Hola!", messageSentLiveData, errorLiveData, loadingLiveData);

        verify(messageSentLiveData).postValue(true);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void sendMessage_on403_postsErrorMessage() {
        doAnswer(invocation -> {
            ((Callback<MessageResponse>) invocation.getArgument(0))
                    .onResponse(mockSendCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockSendCall).enqueue(any());

        repository.sendMessage("Bearer token", "chat-uuid", "Hola!", messageSentLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 403", captor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
        verify(messageSentLiveData, never()).postValue(any());
    }

    @Test
    public void sendMessage_on401_postsErrorMessage() {
        doAnswer(invocation -> {
            ((Callback<MessageResponse>) invocation.getArgument(0))
                    .onResponse(mockSendCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockSendCall).enqueue(any());

        repository.sendMessage("Bearer expired", "chat-uuid", "Hola!", messageSentLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 401", captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
        verify(messageSentLiveData, never()).postValue(any());
    }

    @Test
    public void sendMessage_on500_postsErrorMessage() {
        doAnswer(invocation -> {
            ((Callback<MessageResponse>) invocation.getArgument(0))
                    .onResponse(mockSendCall, Response.error(500, mockErrorBody));
            return null;
        }).when(mockSendCall).enqueue(any());

        repository.sendMessage("Bearer token", "chat-uuid", "Hola!", messageSentLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 500", captor.getValue().contains("500"));
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void sendMessage_on403_doesNotPostResult() {
        doAnswer(invocation -> {
            ((Callback<MessageResponse>) invocation.getArgument(0))
                    .onResponse(mockSendCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockSendCall).enqueue(any());

        repository.sendMessage("Bearer token", "chat-uuid", "Hola!", messageSentLiveData, errorLiveData, loadingLiveData);

        verify(messageSentLiveData, never()).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void sendMessage_onNetworkFailure_postsErrorMessage() {
        doAnswer(invocation -> {
            ((Callback<MessageResponse>) invocation.getArgument(0))
                    .onFailure(mockSendCall, new RuntimeException("timeout"));
            return null;
        }).when(mockSendCall).enqueue(any());

        repository.sendMessage("Bearer token", "chat-uuid", "Hola!", messageSentLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el texto del error", captor.getValue().contains("timeout"));
        verify(loadingLiveData).postValue(false);
        verify(messageSentLiveData, never()).postValue(any());
    }

    @Test
    public void sendMessage_onNullThrowableMessage_postsErrorMessage() {
        doAnswer(invocation -> {
            ((Callback<MessageResponse>) invocation.getArgument(0))
                    .onFailure(mockSendCall, new RuntimeException((String) null));
            return null;
        }).when(mockSendCall).enqueue(any());

        repository.sendMessage("Bearer token", "chat-uuid", "Hola!", messageSentLiveData, errorLiveData, loadingLiveData);

        verify(errorLiveData).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    // helpers ------------------------------------------------------------------------------------

    private ChatResponse buildChat(String displayName, String campaignTitle) {
        ChatResponse c = new ChatResponse();
        c.setId("chat-uuid");
        c.setDisplayName(displayName);
        c.setCampaignTitle(campaignTitle);
        return c;
    }

    private MessageResponse buildMessage(String text) {
        MessageResponse m = new MessageResponse();
        m.setId("msg-uuid");
        m.setText(text);
        m.setSenderId("sender-uuid");
        return m;
    }
}
