//TODO use .env
const rootUrl = "http://localhost:8080/api/";

async function request(url, method, body = null, jwtToken = null) {
  const headers = {
    "content-type": "application/json",
  };

  if (jwtToken) {
    headers.Authorization = `Bearer ${jwtToken}`;
  }

  const response = await fetch(rootUrl + url, {
    method,
    headers,
    body: JSON.stringify(body),
  });

  if (response.ok) {
    return await response.json();
  }

  throw new Error(response.status);
}

export async function login(credentials) {
  return await request("auth/login", "POST", credentials);
}
