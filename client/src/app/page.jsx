"use client";
import { setCookie, getCookie, deleteCookie } from "cookies-next";
import { useRouter } from "next/navigation";
import jwt from "jsonwebtoken";
import { login } from "@/app/services/api";
import { useState, useEffect } from "react";

export default function Home() {
  const router = useRouter();
  const [hideUsernameError, setHideUsernameError] = useState(true);
  const [hidePasswordError, setHidePasswordError] = useState(true);
  const [hideLoginFailed, setHideloginFailed] = useState(true);
  const [loginFailedMessage, setLoginFailedMessage] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleUsernameChange = (event) => {
    const value = event.target.value;
    setHideUsernameError(!!value);
    setUsername(value);
  };

  const handlePasswordChange = (event) => {
    const value = event.target.value;
    setPassword(value);
    setHidePasswordError(!!value);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const loginResponse = await login({ username, password });
      const jwtData = jwt.decode(loginResponse.access_token);
      loginResponse.loggedUser = jwtData.sub;
      setCookie("session", loginResponse);
      router.push("/chat");
    } catch (error) {
      setHideloginFailed(false);
      if (error.message === "403") {
        setLoginFailedMessage(
          "The username or password you’ve entered is incorrect"
        );
      } else {
        setLoginFailedMessage("Something went wrong. Try again later");
      }
    }
  };

  useEffect(() => {
    const sessionCookie = getCookie("session");
    if (!sessionCookie) {
      return;
    }

    const session = JSON.parse(sessionCookie);
    const accessToken = jwt.decode(session.access_token);
    const isExpired = Math.floor(Date.now() / 1000) > accessToken.exp;
    isExpired ? deleteCookie("session") : router.push("/chat");
  }, []);

  return (
    <div className="container">
      <div className="login">
        <h1>SIGN IN</h1>
        <form id="loginForm" onSubmit={handleSubmit}>
          <div className="inputs">
            <span className="error-span" hidden={hideLoginFailed}>
              {loginFailedMessage}
            </span>
            <input
              type="text"
              name="username"
              placeholder="Username"
              onChange={handleUsernameChange}
              autoComplete="new-password"
            />
            <span className="error-span" hidden={hideUsernameError}>
              * Username is required
            </span>
            <input
              type="password"
              name="password"
              placeholder="Password"
              onChange={handlePasswordChange}
              autoComplete="new-password"
            />
            <span className="error-span" hidden={hidePasswordError}>
              * Password is required
            </span>
          </div>
          <div className="form-footer">
            <button type="submit">Login</button>
            <span className="sign-up">
              Don't have an account? <strong>Sign up!</strong>
            </span>
          </div>
        </form>
      </div>
    </div>
  );
}
