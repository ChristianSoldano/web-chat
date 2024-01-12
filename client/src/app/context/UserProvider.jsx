"use client";
import { useState, createContext, useContext, useEffect } from "react";
import jwt from "jsonwebtoken";
import { loginRequest } from "@/app/services/api";
import { setCookie, getCookie, deleteCookie } from "cookies-next";
import moment from "moment";

const UserContext = createContext();

export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error("useUser must be used within a UserProvider");
  }

  return context;
};

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoadingUser, setIsLoadingUser] = useState(true);

  const login = async (username, password) => {
    setIsLoadingUser(true);
    const response = await loginRequest(username, password);
    const jwtData = jwt.decode(response.access_token);
    const userObject = {
      username: jwtData.sub,
      token: response.access_token,
      expires: moment.unix(jwtData.exp).format(),
    };
    setUser(userObject);
    setCookie("user", userObject);
    setIsLoadingUser(false);
  };

  const logout = () => {
    setIsLoadingUser(true);
    deleteCookie("user");
    setUser(null);
    setIsLoadingUser(false);
  };

  useEffect(() => {
    setIsLoadingUser(true);
    if (user) {
      const isExpired = moment(Date.now()).isAfter(user.expires);
      if (isExpired) {
        deleteCookie("user");
      }
    } else {
      const userCookie = getCookie("user");
      if (userCookie) {
        setUser(JSON.parse(userCookie));
      }
    }
    setIsLoadingUser(false);
  }, [user]);

  return (
    <UserContext.Provider value={{ user, login, logout, isLoadingUser }}>
      {children}
    </UserContext.Provider>
  );
};
