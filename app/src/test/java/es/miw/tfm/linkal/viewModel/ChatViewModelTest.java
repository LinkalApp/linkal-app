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
import es.miw.tfm.linkal.models.responses.MessageResponse;
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

    @Mock
    private ChatRepository mockRepository;

    private ChatViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new ChatViewModel(mockRepository);
    }

    @Test
    public void chats_initialValue_isNull() {
        assertNull(viewModel.getChats().getValue());
    }

    @Test
    public void messageSent_initialValue_isNull() {
        assertNull(viewModel.getMessageSent().getValue());
    }

    @Test
    public void messages_initialValue_isNull() {
        assertNull(viewModel.getMessages().getValue());
    }

    @Test
    public void error_initialValue_isNull() {
        assertNull(viewModel.getError().getValue());
    }

    // Getters LiveData ---------------------------------------------------------------

    @Test
    public void getChats_returnsLiveData() {
        assertNotNull(viewModel.getChats());
    }

    @Test
    public void getMessageSent_returnsLiveData() {
        assertNotNull(viewModel.getMessageSent());
    }

    @Test
    public void getMessages_returnsLiveData() {
        assertNotNull(viewModel.getMessages());
    }

    @Test
    public void getError_returnsLiveData() {
        assertNotNull(viewModel.getError());
    }

    // loadChats -------------------------------------------------------------------

    @Test
    public void loadChats_delegatesToRepository() {
        viewModel.loadChats("Bearer token");

        verify(mockRepository).findAllByUser(eq("Bearer token"), any(), any(), any());
    }

    @Test
    public void loadChats_withDifferentToken_passesItToRepository() {
        viewModel.loadChats("Bearer other-token");

        verify(mockRepository).findAllByUser(eq("Bearer other-token"), any(), any(), any());
    }

    @Test
    public void loadChats_doesNotCallSendMessage() {
        viewModel.loadChats("Bearer token");

        verify(mockRepository, never()).sendMessage(any(), any(), any(), any(), any(), any());
    }

    // sendMessage ----------------------------------------------------------------------

    @Test
    public void sendMessage_delegatesToRepository() {
        viewModel.sendMessage("Bearer token", "chat-uuid", "Hola!");

        verify(mockRepository).sendMessage(eq("Bearer token"), eq("chat-uuid"), eq("Hola!"), any(), any(), any());
    }

    @Test
    public void sendMessage_withDifferentToken_passesItToRepository() {
        viewModel.sendMessage("Bearer other", "chat-uuid", "Hola!");

        verify(mockRepository).sendMessage(eq("Bearer other"), eq("chat-uuid"), eq("Hola!"), any(), any(), any());
    }

    @Test
    public void sendMessage_withDifferentChatId_passesItToRepository() {
        viewModel.sendMessage("Bearer token", "other-chat", "Hola!");

        verify(mockRepository).sendMessage(eq("Bearer token"), eq("other-chat"), eq("Hola!"), any(), any(), any());
    }

    @Test
    public void sendMessage_withDifferentText_passesItToRepository() {
        viewModel.sendMessage("Bearer token", "chat-uuid", "Adios!");

        verify(mockRepository).sendMessage(eq("Bearer token"), eq("chat-uuid"), eq("Adios!"), any(), any(), any());
    }

    @Test
    public void sendMessage_doesNotCallLoadChats() {
        viewModel.sendMessage("Bearer token", "chat-uuid", "Hola!");

        verify(mockRepository, never()).findAllByUser(any(), any(), any(), any());
    }

    // loadMessages ----------------------------------------------------------------------------

    @Test
    public void loadMessages_delegatesToRepository() {
        viewModel.loadMessages("Bearer token", "chat-uuid");

        verify(mockRepository).getMessages(eq("Bearer token"), eq("chat-uuid"), any(), any(), any());
    }

    @Test
    public void loadMessages_withDifferentToken_passesItToRepository() {
        viewModel.loadMessages("Bearer other", "chat-uuid");

        verify(mockRepository).getMessages(eq("Bearer other"), eq("chat-uuid"), any(), any(), any());
    }

    @Test
    public void loadMessages_withDifferentChatId_passesItToRepository() {
        viewModel.loadMessages("Bearer token", "other-chat");

        verify(mockRepository).getMessages(eq("Bearer token"), eq("other-chat"), any(), any(), any());
    }

    @Test
    public void loadMessages_doesNotCallSendMessage() {
        viewModel.loadMessages("Bearer token", "chat-uuid");

        verify(mockRepository, never()).sendMessage(any(), any(), any(), any(), any(), any());
    }

    // helpers -------------------------------------------------------------------

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
