//TODO use .env
const rootUrl = "http://localhost:8080/api/";

async function request(url, method, body = null, jwtToken = null) {
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
  return await request("auth/login", "POST", credentials);
}

export async function fetchChats(user) {
  return await request("chats", "GET", undefined, user.token);
}

export async function fetchMessages(user, selectedChatId) {
  return await request(
    `chats/${selectedChatId}/messages`,
    "GET",
    undefined,
    user.token
  );
}
