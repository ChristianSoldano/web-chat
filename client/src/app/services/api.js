async function request(url, method, body = null, jwtToken = null) {
  const rootUrl = process.env.NEXT_PUBLIC_API_BASE_URL;
  const headers = {
    "Content-Type": "application/json",
  };
  if (jwtToken) {
    headers.Authorization = `Bearer ${jwtToken}`;
  }

  const init = {
    method,
    headers,
  };
  if (body) {
    init.body = JSON.stringify(body);
  }

  const response = await fetch(rootUrl + url, init);
  if (!response.ok) {
    throw new Error(response.status);
  }

  return await response.json();
}

export async function loginRequest(credentials) {
  return await request("/auth/login", "POST", credentials);
}

export async function fetchChats(user) {
  return await request("/chats", "GET", undefined, user.token);
}

export async function fetchMessages(
  user,
  selectedChatId,
  lastMessageId = null
) {
  const url = `/chats/${selectedChatId}/messages${
    lastMessageId ? `?lastMessageId=${lastMessageId}` : ""
  }`;

  return await request(url, "GET", undefined, user.token);
}

export async function sendMessage(user, selectedChatId, message) {
  const url = `/chats/${selectedChatId}/messages`;

  return await request(url, "POST", message, user.token);
}
