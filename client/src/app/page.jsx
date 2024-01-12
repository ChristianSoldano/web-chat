"use client";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { useUser } from "./context/UserProvider";

export default function Home() {
  const { user, login, isLoadingUser } = useUser();
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
      await login({ username, password });
      router.push("/chat");
    } catch (error) {
      if (error.message === "403") {
        setLoginFailedMessage(
          "The username or password you’ve entered is incorrect"
        );
      } else {
        setLoginFailedMessage("Something went wrong. Try again later");
      }

      setHideloginFailed(false);
    }
  };

  useEffect(() => {
    if (user && !isLoadingUser) {
      router.push("/chat");
    }
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
