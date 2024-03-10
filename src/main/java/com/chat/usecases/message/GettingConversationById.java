package com.chat.usecases.message;

import java.util.List;

import com.chat.domains.Conversation;
import com.chat.usecases.UseCase;
import com.chat.usecases.adapters.DataStorage;
import com.chat.usecases.message.GettingAllConversation.GettingAllConversationResult;

public class GettingConversationById
		extends UseCase<GettingConversationById.InputValues, GettingConversationById.OutputValues> {
	private DataStorage _dataStorage;

	public GettingConversationById(DataStorage dataStorage) {
		this._dataStorage = dataStorage;
	}

	public static class InputValues {
		private String _userId;
		private String _conversationId;

		public InputValues(String userId, String conversationId) {
			this._userId = userId;
			this._conversationId = conversationId;
		}

	}

	public static class OutputValues {
		private GettingConversationByIdResult _result;
		private Conversation _conversation;

		public OutputValues(GettingConversationByIdResult result, Conversation conversations) {
			this._result = result;
			this._conversation = conversations;
		}

		public GettingConversationByIdResult getResult() {
			return _result;
		}

		public Conversation getConversations() {
			return _conversation;
		}

	}

	public enum GettingConversationByIdResult {
		Successed, Failed
	}

	@Override
	public OutputValues execute(InputValues input) {
		GettingAllConversation.InputValues gettingAllConversationInput = new GettingAllConversation.InputValues(
				input._userId);
		GettingAllConversation gettingAllConversation = new GettingAllConversation(_dataStorage);
		GettingAllConversation.OutputValues gettingAllConversationOutput = gettingAllConversation
				.execute(gettingAllConversationInput);

		if (gettingAllConversationOutput.getResult() == GettingAllConversationResult.Successed) {
			List<Conversation> conversations = gettingAllConversationOutput.getConversations();

			for (Conversation conversation : conversations) {
				if (conversation.getId().equals(input._conversationId)) {
					return new OutputValues(GettingConversationByIdResult.Successed, conversation);
				}
			}
		}
		return new OutputValues(GettingConversationByIdResult.Failed, null);
	}
}
